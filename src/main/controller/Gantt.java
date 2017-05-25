package main.controller;

import main.model.Burst;
import main.model.Core;
import main.model.IO;
import main.model.process.Process;
import main.model.process.Scheduler;
import main.model.thread.KernelLevelThread;
import main.model.thread.UserLevelThread;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Gantt {

    private class TraceNode {

    }

    private List<TraceNode> trace;

    public Gantt() {
        trace = new ArrayList<>();
    }

    /**
     * Adds a new column to the Gantt diagram
     * @param scheduler
     */
    public void addTraceNode(Scheduler scheduler) {
        // TODO crear un TraceNode a partir de lo que tiene el scheduler en ese instante
        TraceNode node = new TraceNode();

        ///TODO remove///
        print(scheduler);
        /////////////////

        trace.add(node);
    }

    /**
     * Prints the Gantt diagram at a given time
     * @param time
     */
    public void print(int time) {
        print(trace.get(time));
    }

    private void print(TraceNode node) {

        if (node == null) {
            //GG
        }
        //aca imprimo el nodo
    }

    // imprime una columna (un instante de tiempo)
    private void print(Scheduler scheduler) {
        Map<Core, Integer> runningThreads = new HashMap<>(); // Core -> running ult id

        for (Core core : scheduler.getCores()) {
            runningThreads.put(core, core.getCurrentKLT().getRunningThread().getTID());
        }

        int pid, kid, uid, burstType;
        Burst currentBurst;
        for (Process process : scheduler.getProcesses()) {
            for (KernelLevelThread klt : process.getThreads()) {
                for (UserLevelThread ult : klt.getReadyThreads()) {
                    uid = ult.getTID();
                    kid = klt.getTID(); // same as ult.getParentKltID();
                    pid = process.getPID(); // same as ult.getParentPID() o klt.getParentPID();
                    System.out.print("P" + pid + "K" + kid + "U" + uid + " | ");

                    currentBurst = ult.getCurrentBurst();
                    if (currentBurst == null) {
                        //finished
                    }

                    burstType = ult.getCurrentBurst().getType();
                    if (burstType == 0) { // CPU burst
                        for (Core core : scheduler.getCores()) {
                            if (uid == core.getCurrentKLT().getRunningThread().getTID()) {
                                System.out.print("CPU" + core.getID());
                            } else {
                                System.out.print("    ");
                            }
                        }
                    } else { // I/O burst
                        for (IO io : scheduler.getIODevices()) {
                            if (uid == io.getCurrentKlt().getRunningThread().getTID()) {
                                System.out.print("I/O" + io.getID());
                            } else {
                                System.out.print("    ");
                            }
                        }
                    }
                }
            }
        }
    }


    /**
     * - = CPU
     * n = IOn
     * n e N
     *  Procesos:
     *      P1K1U1 | -| -| -| 1| 1| 1|  | -|  | -|- |  |
     *      P1K2U2 |  |  |  | -| -| 2| 2|  | -|  |  |  |
     *             |  |  |  |  |  |  |  |  |  |  |  |  |
     *      Pm     |  |  |  |  |  | -| -| 3| 3| 3|  |- |
     *      --------0--1--2--3--4--5--6--7--8--9--10-11--------------------------
     *      OS     |
     *
     */
}
