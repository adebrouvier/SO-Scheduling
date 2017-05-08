package scheduler.configuration;

import org.apache.commons.cli.*;
import scheduler.Process;
import scheduler.Thread;
import scheduler.ThreadType;

import java.io.File;
import java.util.Scanner;

public class ConfigurationLoader {

    private String fileName = "./res/settings.so";
    private Integer processes;
    private Integer bursts;

    public ConfigurationLoader(String [] args){

        // create Options object
        Options options = new Options();

        // add option
        String[] optionList = {"p","b","c","pp","tl"};
        String[] optionDescriptions = {"number of processes","number of bursts","number of cores","process planification","thread library"};

        for (int i = 0;i < optionList.length;i++) {
            Option option = new Option(optionList[i], true, optionDescriptions[i]);
            option.setArgs(1);
            option.setRequired(true);
            options.addOption(option);
        }

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd;
        HelpFormatter formatter = new HelpFormatter();
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

        } catch (ParseException exp) {
            // oops, something went wrong
            System.err.println( "Parsing failed.  Reason: " + exp.getMessage() );
        }
    }

    public Configuration load (){

        File file = new File(fileName);
        Configuration config=new Configuration(null,null);

        try {
            Scanner sc = new Scanner(file);

            Process[] processList = new Process[processes];

            for (int i = 0; i < processes; i++){
                processList[i]=new Process( sc.nextInt(),null);
            }

            int[] numberOfThreads = new int[processes];

            for (int i = 0; i < processes; i++){
                numberOfThreads[i]=sc.nextInt();
            }

            String[] burstType = new String[bursts];

            for (int i = 0; i < bursts ; i++){
                burstType[i]=sc.next();
            }

            config.setBurstList(burstType);

            for (int i = 0; i<processes ; i++){

                Thread[] threadList = new Thread[numberOfThreads[i]];

                for (int j = 0; j<numberOfThreads[i] ; j++){

                    String threadTypeInput = String.valueOf(sc.next().charAt(0));

                    ThreadType type = null;

                    if (threadTypeInput.equals(ThreadType.KLT.threadType))
                        type=ThreadType.KLT;

                    if (threadTypeInput.equals(ThreadType.ULT.threadType))
                        type=ThreadType.ULT;

                    Integer[] burst = new Integer[bursts];

                    for (int k = 0; k<bursts ; k++){
                        burst[j] = sc.nextInt();
                    }

                    Thread t = new Thread(type,burst);
                    threadList[j]=t;
                }

                processList[i].setThreads(threadList);
            }

            config.setProcessList(processList);

        }catch (Exception e){
            e.printStackTrace();
        }

        return config;
    }


}
