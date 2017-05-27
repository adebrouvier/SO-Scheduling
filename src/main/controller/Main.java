package main.controller;

import main.controller.configuration.Configuration;
import main.controller.configuration.ConfigurationLoader;

import javax.swing.*;

public class Main {

    public static void main(String[] args) {
        ConfigurationLoader cl = new ConfigurationLoader(args);
        Configuration cfg = cl.load();

        JFrame frame = new JFrame("Scheduling");
        Simulation simulation = new Simulation(cfg);
        frame.setContentPane(simulation);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        simulation.start();
    }

}
