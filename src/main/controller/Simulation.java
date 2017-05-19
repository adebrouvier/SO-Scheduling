package main.controller;

import main.controller.configuration.Configuration;
import main.model.process.Process;
import main.model.process.Scheduler;
import main.model.process.SchedulerFIFO;
import main.model.process.SchedulerRoundRobin;
import main.model.thread.KernelLevelThread;

import java.util.List;
import java.util.Map;

public class Simulation {

    // procesos separados por arrival time;
    private Map<Integer, List<Process>> processes;

    private Scheduler scheduler;

    private Gantt gantt;

    private int time;

    public Simulation(Configuration cfg) {
        int cores = cfg.getCores();
        int ioCount = cfg.getIOCount();

        String scheduling = cfg.getScheduling();
        String threadLibrary = cfg.getThreadLibrary();

        if (scheduling.equals("FIFO")) {
            scheduler = new SchedulerFIFO(cores, threadLibrary, ioCount);
        } else if (scheduling.equals("RR")) {
            int quantum = cfg.getSchedulingQuantum();
            scheduler = new SchedulerRoundRobin(cores, threadLibrary, ioCount, quantum);
        }

        processes = cfg.getProcesses();

        time = 0;
    }


    public void start() {

        boolean running = true;

        while (true) {

            // TODO check input (pause, resume, etc)

            if (running) {
                scheduler.execute(processes.get(time));

                gantt.print(time, scheduler);
                time++;

                try {
                    System.out.wait(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }



    private Process createProcess(int pid, List<KernelLevelThread> klts) {
        return null;
    }

}
