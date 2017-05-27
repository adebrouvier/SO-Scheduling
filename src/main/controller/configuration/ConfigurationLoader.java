package main.controller.configuration;

import main.model.Burst;
import main.model.process.Process;
import main.model.thread.KernelLevelThread;
import main.model.thread.ThreadLibraryType;
import main.model.thread.UserLevelThread;
import org.apache.commons.cli.*;

import java.io.File;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ConfigurationLoader {

    private String fileName = "./res/settings.so";
    private String processPlanification = "FIFO";
    private String threadLibrary = "FIFO";
    private int cores=1;
    private int IOCount=1;
    private int schedulingQuantum=1;
    private int threadQuantum=0;

    public ConfigurationLoader(String [] args){

        // create Options object
        Options options = new Options();

        // add option
        String[] optionList = {"f","c","ps","tl"};
        //String[] longOption = {"filename","bursts","cores","scheduling","threadlibrary","iodevices"};
        String[] optionDescriptions = {"filename of input","number of cores",
                "type of process scheduling","thread library algorithm"};

        for (int i = 0; i < optionList.length ; i++) {
            Option option = new Option(optionList[i], true, optionDescriptions[i]);
            option.setArgs(1);
            option.setRequired(true);
            //option.setLongOpt(longOption[i]);
            options.addOption(option);
        }

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd;
        HelpFormatter formatter = new HelpFormatter();

        try {
            cmd = parser.parse( options, args);

            String pOption;

            if (cmd.hasOption("f")){
                pOption = cmd.getOptionValue("f");
                fileName = pOption;
            }

            if (cmd.hasOption("c")) {
                pOption = cmd.getOptionValue("c");
                cores=Integer.parseInt(pOption);
            }

            if (cmd.hasOption("ps")){
                pOption = cmd.getOptionValue("ps");
                processPlanification=pOption.toUpperCase();

                Pattern p = Pattern.compile("RR_[0-9]+");
                Matcher m = p.matcher(processPlanification);

                if (m.matches()){
                    schedulingQuantum=Integer.valueOf(processPlanification.split("_")[1]);

                    if (schedulingQuantum < 0){
                        System.err.println("Scheduling RR quantum has to be positive.");
                    }

                    processPlanification="RR";
                }

            }

            if (cmd.hasOption("tl")){
                pOption = cmd.getOptionValue("tl");
                threadLibrary=pOption.toUpperCase();

                Pattern p = Pattern.compile("RR_[0-9]+");
                Matcher m = p.matcher(threadLibrary);

                if (m.matches()){
                    threadQuantum=Integer.valueOf(threadLibrary.split("_")[1]);

                    if (threadQuantum <= 0 || threadQuantum == schedulingQuantum) {
                        System.err.println("Thread RR quantum has to be positive and different from Scheduling Quantum.");
                        System.exit(0);
                    }

                    threadLibrary="RR";
                }
            }

        } catch (ParseException exp) {
            // oops, something went wrong
            formatter.printHelp("main",options);
            System.err.println( "Parsing failed.  Reason: " + exp.getMessage() );
            System.exit(1);
        }
    }

    public Configuration load (){

        File file = new File(fileName);

        Map <Integer,Map<Integer,List<UserLevelThread>>> processList = new HashMap<>();
        Map <Integer,Integer> ultArrivalTimes = new HashMap<>(); // <ID,arrivalTime>

        try {
            Scanner sc = new Scanner(file);

            List<Integer> burstTypes = new ArrayList<>();

            String header = "";

            if (sc.hasNextLine()) {
                header = sc.nextLine();
            }

            Scanner headerScanner = new Scanner(header);

            if (headerScanner.hasNext()) {
                headerScanner.next(); //A
            }

            while (headerScanner.hasNext()){
                String burst = headerScanner.next();
                if (burst.equals("C")) {
                    burstTypes.add(0);
                } else {
                    int ioDevice = Integer.valueOf(burst);
                    burstTypes.add(ioDevice);
                }
            }

            int bursts = burstTypes.size();

            checkBurst(bursts, burstTypes);

            IOCount = devices(burstTypes);

            String line;

            while(sc.hasNextLine()) {
                line = sc.nextLine();
                if(line.length()==0) {continue;}
                //treatment

                Scanner lineScanner = new Scanner(line);

                String identifier;

                identifier = lineScanner.next();

                // Formato de identificador
                Pattern p1 = Pattern.compile("P[0-9]+_K[0-9]+_U[0-9]+");

                Matcher m1 = p1.matcher(identifier);

                String[] tokens = identifier.split("_");

                int processNumber = Integer.valueOf(tokens[0].substring(1));
                int kernelThreadNumber = Integer.valueOf(tokens[1].substring(1));
                int userThreadNumber = Integer.valueOf(tokens[2].substring(1));

                if (m1.matches()) {

                    int threadArrivalTime = lineScanner.nextInt();

                    if (threadArrivalTime < 0){
                        System.err.println("Arrival time must be greater than 0.");
                        System.exit(1);
                    }

                    ultArrivalTimes.put(userThreadNumber,threadArrivalTime);

                    List<Burst> burstList = new ArrayList<>();

                    for (Integer burstType : burstTypes) {

                        int burstTime = lineScanner.nextInt();

                        if (burstTime < 0) {
                            System.err.println("Burst time must be greater than 0.");
                            System.exit(1);
                        }

                        burstList.add(new Burst(burstType, burstTime));
                    }

                    Map<Integer,List<UserLevelThread>> kernelThreadList = processList.get(processNumber);

                    if (kernelThreadList == null){
                        kernelThreadList = new HashMap<>();
                    }

                    List<UserLevelThread> ults = kernelThreadList.get(kernelThreadNumber);

                    if (ults == null){
                        ults = new ArrayList<>();
                    }

                    UserLevelThread u = new UserLevelThread(kernelThreadNumber,processNumber,burstList);
                    ults.add(u);
                    kernelThreadList.put(kernelThreadNumber,ults);
                    processList.put(processNumber,kernelThreadList);

                } else {
                    System.err.println("Wrong parameters");
                    System.exit(1);
                }
                lineScanner.close();
            }
            sc.close();
        }catch (Exception e){
            e.printStackTrace();
        }

        List<Process> process = new ArrayList<>();

        for (Integer processNumber : processList.keySet()){

           Map<Integer,List<UserLevelThread>> kltMap = processList.get(processNumber);

           List<KernelLevelThread> kltList = new ArrayList<>();

           for (Integer kltNumber : kltMap.keySet()){
               KernelLevelThread k = new KernelLevelThread(processNumber,
                       kltMap.get(kltNumber), ThreadLibraryType.valueOf(threadLibrary),threadQuantum);
               kltList.add(k);
           }

            process.add(new Process(kltList));
        }

        Map<Integer,List<UserLevelThread>> ultArrivals = createULTMap(process,ultArrivalTimes);

        Map<Integer,List<Process>> processArrivals = createProcessMap(process,ultArrivalTimes);

        return new Configuration(processArrivals,ultArrivals,cores, threadLibrary,
                processPlanification,IOCount,schedulingQuantum,threadQuantum);
    }

    /**
     * Creates a map, the key is the arrival time, and the value is a list of process that arrive at that time
     * @param process list of processes
     * @param ultArrivalTimes arrival time of ults
     * @return the map
     */
    private Map<Integer,List<Process>> createProcessMap(List<Process> process, Map<Integer, Integer> ultArrivalTimes) {

        Map<Integer,List<Process>> processArrivals = new HashMap<>();

        for (Process p : process){

            int minTime = Integer.MAX_VALUE;

            for (KernelLevelThread k : p.getThreads()){

                for (UserLevelThread u : k.getThreads()){

                    Integer arrivalTime = ultArrivalTimes.get(u.getTID());

                    if (arrivalTime < minTime) {
                        minTime = arrivalTime;
                    }
                }
            }

            List<Process> arrivalList = processArrivals.get(minTime);

            if (arrivalList == null){
                arrivalList = new ArrayList<>();
            }

            arrivalList.add(p);

            processArrivals.put(minTime,arrivalList);

        }

        return processArrivals;
    }

    /**
     * Creates a map, the key is the arrival time and the value a list of ults that arrive at that time
     * @param process list of process
     * @param ultArrivalTimes arrival time of each ult
     * @return the map
     */
    private Map<Integer,List<UserLevelThread>> createULTMap(List<Process> process, Map<Integer, Integer> ultArrivalTimes) {

        Map<Integer,List<UserLevelThread>> ultArrivals = new HashMap<>();

        for (Process p : process){
            for (KernelLevelThread k : p.getThreads()){
                for (UserLevelThread u : k.getThreads()){

                    Integer arrivalTime = ultArrivalTimes.get(u.getTID());

                    List<UserLevelThread> arrivalList = ultArrivals.get(arrivalTime);

                    if (arrivalList == null){
                        arrivalList = new ArrayList<>();
                    }

                    arrivalList.add(u);

                    ultArrivals.put(arrivalTime,arrivalList);
                }
            }
        }

        return ultArrivals;
    }

    /**
     * Counts the number of io devices in the bursts
     * @param burstTypes list with the type of each burst
     * @return number of io devices
     */
    private int devices(List<Integer> burstTypes) {

        Set<Integer> devices = new HashSet<>();

        devices.addAll(burstTypes);

        return devices.size()-1;
    }

    /**
     * Checks if the number of bursts is corrects, if the first and last bursts are cpu and if bursts are interleaved
     * @param bursts number of bursts
     * @param burstTypes list with the type of each burst
     */
    private void checkBurst(Integer bursts, List<Integer> burstTypes) {
        if (!(burstTypes.size() == bursts)){
            System.err.println("Wrong number of bursts");
            System.exit(1);
        }

        if (burstTypes.get(0) != 0){ // if first burst isnt CPU
            System.err.println("First burst type must be CPU.");
            System.exit(1);
        }

        for (int i = 0; i < burstTypes.size() ; i++){
            if (i % 2 == 0){
                if (burstTypes.get(i) != 0){
                    System.err.println("Bursts must be intercalated.");
                    System.exit(1);
                }
            }else{

                if (i == burstTypes.size()-1){
                    System.err.println("Last burst must be CPU.");
                    System.exit(1);
                }

                if (burstTypes.get(i) == 0){
                    System.err.println("Bursts must be intercalated.");
                    System.exit(1);
                }
            }
        }


    }

}
