package scheduler.configuration;

import org.apache.commons.cli.*;
import scheduler.Burst;
import scheduler.KernelLevelThread;
import scheduler.Process;
import scheduler.UserLevelThread;

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

    public ConfigurationLoader(String [] args){

        // create Options object
        Options options = new Options();

        // add option
        String[] optionList = {"p","b","c","ps","tl", "io"};
        String[] optionDescriptions = {"number of processes","number of bursts",
                "number of cores","process scheduling","thread library","number of io devices"};

        for (int i = 0;i < optionList.length;i++) {
            Option option = new Option(optionList[i], true, optionDescriptions[i]);
            option.setArgs(1);
            option.setRequired(true);
            options.addOption(option);
        }

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd;
        HelpFormatter formatter = new HelpFormatter();

        //TODO: remove this line
        formatter.printHelp("scheduler",options);

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
            }

            if (cmd.hasOption("tl")){
                pOption = cmd.getOptionValue("tl");
                threadLibrary=pOption.toUpperCase();
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

        Process[] processList = new Process[processes];

        try {
            Scanner sc = new Scanner(file);

            //Create process list
            for (int i = 0; i < processes; i++){
                processList[i] = new Process( sc.nextInt(),null);
            }

            int[] numberOfThreads = new int[processes];

            // Obtain number of threads for each process
            for (int i = 0; i < processes; i++){
                numberOfThreads[i] = sc.nextInt();
            }

            Burst[] burst = new Burst[bursts];

            // Obtain burst type for each burst
            for (int i = 0; i < bursts ; i++){
                burst[i] = new Burst(0,sc.next());
            }

            for (int i = 0; i<processes ; i++){

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

                processList[i].setThreads(arrayifi(threadList));
            }

        }catch (Exception e){
            e.printStackTrace();
        }

        Configuration config = new Configuration(processList, cores, threadLibrary, processPlanification,IOCount);

        return config;
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
