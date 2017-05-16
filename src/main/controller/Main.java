package main.controller;

import main.controller.configuration.Configuration;
import main.controller.configuration.ConfigurationLoader;

public class Main {

    public static void main(String[] args) {

        ConfigurationLoader cl = new ConfigurationLoader(args);
        Configuration cfg = cl.load();

        Simulation simulation = new Simulation(cfg);
        simulation.start();

        //Scheduler main  = new Scheduler(cfg.getProcessList(),cfg.getBurstList(),cfg.getCores(),cfg.getProcessPlanification(),
        //        cfg.getThreadLibrary());

        //main.run();
    }

}
