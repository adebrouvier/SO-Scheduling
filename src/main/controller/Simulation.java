package main.controller;

import main.controller.configuration.Configuration;
import main.model.process.Process;
import main.model.process.Scheduler;
import main.model.process.SchedulerFIFO;
import main.model.process.SchedulerRoundRobin;
import main.model.thread.UserLevelThread;

import java.util.List;
import java.util.Map;

public class Simulation {

    /** Arrival time -> processes/threads */
    private Map<Integer, List<Process>> processes;
    private Map<Integer, List<UserLevelThread>> threads;

    private Scheduler scheduler;
    private Gantt gantt;

    private int time;

    public Simulation(Configuration cfg) {
        int cores = cfg.getCores();
        int ioCount = cfg.getIOCount();

        String scheduling = cfg.getScheduling();

        if (scheduling.equals("FIFO")) {
            scheduler = new SchedulerFIFO(cores, ioCount);
        } else if (scheduling.equals("RR")) {
            int quantum = cfg.getSchedulingQuantum();
            scheduler = new SchedulerRoundRobin(cores, ioCount, quantum);
        }

        processes = cfg.getProcesses();
        threads = cfg.getThreads();
        time = 0;
    }


    public void start() {

        boolean running = true;

        while (true) {

            // TODO check input (pause, resume, etc)

            if (running) {
                scheduler.execute(processes.get(time), threads.get(time));

                gantt.addTraceNode(scheduler);
                gantt.print(time);
                time++;

                try {
                    System.out.wait(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
