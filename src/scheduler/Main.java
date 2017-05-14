package scheduler;

import scheduler.configuration.Configuration;
import scheduler.configuration.ConfigurationLoader;

public class Main {

    public static void main(String[] args) {

        ConfigurationLoader cl = new ConfigurationLoader(args);

        Configuration cfg = cl.load();

        //Scheduler scheduler  = new Scheduler(cfg.getProcessList(),cfg.getBurstList(),cfg.getCores(),cfg.getProcessPlanification(),
        //        cfg.getThreadLibrary());

        //scheduler.run();
    }

}
