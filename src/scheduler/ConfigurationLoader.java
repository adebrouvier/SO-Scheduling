package scheduler;

import java.io.File;
import java.util.Scanner;
import org.apache.commons.cli.*;

public class ConfigurationLoader {

    private String fileName = "./res/settings.so";
    Integer processes;
    Integer bursts;

    public ConfigurationLoader(String [] args){

        // create Options object
        Options options = new Options();

        // add option
        //@TODO: hacer required
        options.addOption("p", true, "number of processes");
        options.addOption("b", true, "number of bursts");
        options.addOption("c", true, "number of cores");
        options.addOption("pp", true, "process planification");
        options.addOption("tl", true, "thread library");

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

        load();
    }

    private void load (){

        File file = new File(fileName);

        try {
            Scanner sc = new Scanner(file);

            for (int i = 0; i < processes; i++){
                System.out.println("Arrival time: " +sc.next());
            }

        }catch (Exception e){
            e.printStackTrace();
        }

    }


}
