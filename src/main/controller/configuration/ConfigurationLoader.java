package main.controller.configuration;

import main.model.Burst;
import main.model.process.Process;
import main.model.thread.KernelLevelThread;
import main.model.thread.UserLevelThread;
import org.apache.commons.cli.*;
import org.omg.PortableInterceptor.SYSTEM_EXCEPTION;

import java.io.File;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ConfigurationLoader {

    private String fileName = "./res/settings.so";
    private Integer processes = 1;
    private Integer bursts = 1;
    private String processPlanification = "FIFO";
    private String threadLibrary = "FIFO";
    private Integer cores=1;
    private Integer IOCount=1;
    private Integer schedulingQuantum=1;
    private Integer threadQuantum=2;

    public ConfigurationLoader(String [] args){

        // create Options object
        Options options = new Options();

        // add option
        String[] optionList = {"p","b","c","ps","tl","io"};
        String[] quantumOptionList = {"sq","tq"};
        //String[] longOption = {"processes","bursts","cores","scheduling","threadlibrary","iodevices"};
        String[] optionDescriptions = {"number of processes","number of bursts",
                "number of cores","type of process scheduling","thread library algorithm","number of io devices"};

        for (int i = 0;i < optionList.length;i++) {
            Option option = new Option(optionList[i], true, optionDescriptions[i]);
            option.setArgs(1);
            option.setRequired(true);
            //option.setLongOpt(longOption[i]);
            options.addOption(option);
        }

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd;
        HelpFormatter formatter = new HelpFormatter();

        //TODO: comando para help
        formatter.printHelp("main",options);

        try {
            cmd = parser.parse( options, args);

            String pOption;

            if (cmd.hasOption("p")) {
                pOption = cmd.getOptionValue("p");
                processes=Integer.parseInt(pOption);
            }

            if (cmd.hasOption("b")) {
                pOption = cmd.getOptionValue("b");
                bursts=Integer.parseInt(pOption);
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

                    if (threadQuantum <= 0 || threadQuantum.equals(schedulingQuantum)) {
                        System.err.println("Thread RR quantum has to be positive and different from Scheduling Quantum.");
                        System.exit(0);
                    }

                    threadLibrary="RR";
                }
            }

            if (cmd.hasOption("io")){
                pOption = cmd.getOptionValue("io");
                IOCount=Integer.parseInt(pOption);
            }

        } catch (ParseException exp) {
            // oops, something went wrong
            System.err.println( "Parsing failed.  Reason: " + exp.getMessage() );
        }
    }
    //TODO validation and exit (0)
    public Configuration load (){

        File file = new File(fileName);

        List<Process> processList = new ArrayList<>();

        Map<Integer,List<Process>> processArrivals = new HashMap<>();

        try {
            Scanner sc = new Scanner(file);

            int[] numberOfThreads = new int[processes];

            int[] arrivalTimes = new int[processes];

            // Obtener tiempos de llegada
            for (int i = 0; i < processes; i++){
                arrivalTimes[i] = sc.nextInt();
            }

            // Obtain number of threads for each process
            for (int i = 0; i < processes; i++){
                numberOfThreads[i] = sc.nextInt();
            }

            Burst[] burst = new Burst[bursts];

            // Obtain burst type for each burst
            for (int i = 0; i < bursts ; i++){
                burst[i] = new Burst(0,sc.next());
            }

            for (int i = 0; i< processes ; i++){

                Map<Integer,KernelLevelThread> threadList = new HashMap<>();

                for (int j = 0; j<numberOfThreads[i] ; j++){

                    String threadTypeInput = sc.next();

                    for (int k = 0; k < bursts ; k++){
                        burst[k].setTime(sc.nextInt());
                    }

                    Pattern p1 = Pattern.compile("P[0-9]+_K[0-9]+_U[0-9]+");
                    Pattern p2 = Pattern.compile("P[0-9]+_K[0-9]+");

                    Matcher m1 = p1.matcher(threadTypeInput);
                    Matcher m2 = p2.matcher(threadTypeInput);

                    String[] tokens = threadTypeInput.split("_");

                    int kernelThreadNumber = Integer.valueOf(tokens[1].substring(1))-1;

                    if (m1.matches()){

                        KernelLevelThread klt = threadList.get(kernelThreadNumber);

                        if (klt == null)
                            klt = new KernelLevelThread(burst);

                        UserLevelThread ult = new UserLevelThread(burst);

                        klt.addUserLevelThread(ult);

                        threadList.put(kernelThreadNumber,klt);

                    }else if (m2.matches()){

                        KernelLevelThread klt = new KernelLevelThread(burst);
                        threadList.put(kernelThreadNumber,klt);

                    }else{
                        System.err.println("Wrong parameters");
                        System.exit(0);
                    }

                }

                processList.add(new Process(i,arrayifi(threadList)));
            }

            for(int i = 0 ; i < processes ; i++){

                List<Process> instant = processArrivals.get(arrivalTimes[i]);

                if (instant == null){
                    instant =  new ArrayList<>();
                }

                instant.add(processList.get(i));

                processArrivals.put(arrivalTimes[i],instant);
            }

        }catch (Exception e){
            e.printStackTrace();
        }

        return new Configuration(processArrivals, cores, threadLibrary, processPlanification,IOCount,0,0);
    }

    private KernelLevelThread[] arrayifi(Map<Integer,KernelLevelThread> threadList) {

        int size = threadList.size();

        KernelLevelThread[] resul = new KernelLevelThread[size];

        for (int i = 0; i <size ; i++){
            resul[i] = threadList.get(i);
        }

        return resul;
    }


}
