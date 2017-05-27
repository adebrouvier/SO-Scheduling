package main.controller;

import main.controller.configuration.Configuration;
import main.controller.input.InputListener;
import main.model.process.Process;
import main.model.process.Scheduler;
import main.model.process.SchedulerFIFO;
import main.model.process.SchedulerRoundRobin;
import main.model.thread.UserLevelThread;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.Map;

public class Simulation extends JPanel implements Runnable {

    /** Arrival time -> processes/threads */
    private Map<Integer, List<Process>> processes;
    private Map<Integer, List<UserLevelThread>> threads;

    private Scheduler scheduler;
    private Gantt gantt;

    private int time;

    private InputListener input;

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

        gantt = new Gantt();

        setPreferredSize(new Dimension(500, 500));
        setFocusable(true);
        requestFocus();
    }

    public void start() {
        running = true;
        input = new InputListener();
        addKeyListener(input);

        Thread thread = new Thread(this);
        thread.start();
    }

    private boolean running;

    @Override
    public void run() {

        long timer = System.currentTimeMillis();

        while (true) {

            input.updateKeys();

            if (System.currentTimeMillis() - timer > 1000) { // every second
                timer += 1000;

                if (input.isPressed(InputListener.PAUSE)) {
                    running = !running;
                }

                if (running) {
                    scheduler.execute(processes.get(time), threads.get(time), time);
                    gantt.addTraceNode(scheduler);
                    gantt.print(time);
                    time++;
                }
            }
        }
    }

}
