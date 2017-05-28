package main.controller;

import main.controller.configuration.Configuration;
import main.model.process.Process;
import main.model.process.Scheduler;
import main.model.process.SchedulerFIFO;
import main.model.process.SchedulerRoundRobin;
import main.model.thread.UserLevelThread;
import sun.swing.UIAction;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;
import java.util.Map;

/**
 * Scheduling simulation class. Updates the {@link Scheduler} every second
 * and prints a {@link Gantt} diagram to the console.
 * @see Simulation#run()
 * Provides pause, back and forward functionality.
 */
public class Simulation extends JPanel implements Runnable {

    /** Arrival time -> processes/threads */
    private Map<Integer, List<Process>> processes;
    private Map<Integer, List<UserLevelThread>> threads;

    private Scheduler scheduler;
    private Gantt gantt;

    private JButton pause, back, forward;

    private int time;
    private int pausedTime;

    private boolean running;

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
        time = pausedTime = 0;

        gantt = new Gantt();

        setPreferredSize(new Dimension(500, 200));
        setFocusable(true);
        requestFocus();
    }

    private void createButtons() {
        setLayout(null);

        pause = new JButton("PAUSE");
        pause.setBounds(getWidth() / 2 - 100, 10, 200, 50);
        pause.setVisible(true);
        pause.setAction(new Pause());

        back = new JButton("BACK");
        back.setBounds(getWidth() / 2 - 100, 50 + 10 * 2, 200, 50);
        back.setAction(new MoveInTime("BACK"));
        back.setActionCommand("back");
        back.setEnabled(false);
        back.setVisible(true);

        forward = new JButton("FORWARD");
        forward.setBounds(getWidth() / 2 - 100, 50 * 2 + 10 * 3, 200, 50);
        forward.setAction(new MoveInTime("FORWARD"));
        forward.setActionCommand("forward");
        forward.setEnabled(false);
        forward.setVisible(true);

        add(pause);
        add(back);
        add(forward);
    }

    public void start() {
        createButtons();
        running = true;
        Thread thread = new Thread(this);
        thread.start();
    }

    /**
     * Executes the scheduler every second giving it the new Processes and UserLevelThreads
     * that arrived in that moment. Then prints the result in a Gantt diagram.
     */
    @Override
    public void run() {

        long timer = System.currentTimeMillis();

        while (true) {
            if (System.currentTimeMillis() - timer > 1000) { // every second
                timer += 1000;
                if (running) {
                    scheduler.execute(processes.get(time), threads.get(time), time);
                    gantt.addTraceNode(scheduler);
                    gantt.print(time);
                    pausedTime = time;
                    time++;
                }
            }
        }
    }

    private class Pause extends UIAction {
        public Pause() {
            super("PAUSE");
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            running = !running;
            if (running) {
                System.out.println("RESUMED");
                pause.setText("PAUSE");
                back.setEnabled(false);
                forward.setEnabled(false);
                pausedTime = time;
            } else {
                System.out.println("PAUSED");
                pause.setText("RESUME");
                if (pausedTime > 1) {
                    back.setEnabled(true);
                }
                if (pausedTime < time - 1) {
                    forward.setEnabled(true);
                }
            }
        }
    }

    private class MoveInTime extends UIAction {
        public MoveInTime(String s) {
            super(s);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getActionCommand().equals("back")) {
                if (pausedTime > 1) {
                    pausedTime--;
                    if (pausedTime == 1) {
                        back.setEnabled(false);
                    }
                    if (pausedTime < time - 1) {
                        forward.setEnabled(true);
                    }
                } else {
                    back.setEnabled(false);
                }
            } else if (e.getActionCommand().equals("forward")) {
                if (pausedTime < time - 1) {
                    pausedTime++;
                    if (pausedTime == time - 1) {
                        forward.setEnabled(false);
                    }
                    if (pausedTime > 1) {
                        back.setEnabled(true);
                    }
                } else {
                    forward.setEnabled(false);
                }
            }
            gantt.print(pausedTime);
        }
    }

}
