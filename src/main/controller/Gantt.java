package main.controller;

import main.model.process.Process;
import main.model.process.Scheduler;
import main.model.thread.KernelLevelThread;
import main.model.thread.UserLevelThread;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

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
        int pid, kid, uid;


        for (Process process : scheduler.getProcesses()) {
            for (KernelLevelThread klt : process.getThreads()) {
                for (UserLevelThread ult : klt.getThreads()) {
                    uid = ult.getTID();
                    kid = klt.getTID(); // same as ult.getParentKltID();
                    pid = process.getPID(); // same as ult.getParentPID() o klt.getParentPID();
                    System.out.print("P" + pid + "K" + kid + "U" + uid + " | ");

                    List<Integer> trace = ult.getTrace();

                    for (Integer instant : trace) {
                        if (instant == 0) {
                            System.out.print("    ");
                        } else if (instant < 0) {
                            System.out.print("CPU" + instant * (-1) );
                        } else {
                            System.out.print("I/O" + instant);
                        }
                        System.out.print(" | ");
                    }
                    System.out.println();
                }
                System.out.println();
            }
        }

        Queue<Process> readyQueue = scheduler.getReadyQueue();

        System.out.println("READY THREADS:");
        System.out.println(readyQueue);
        /*for (Process process : readyQueue) {
            for (KernelLevelThread klt : process.getReadyThreads()) {
                for (UserLevelThread ult : klt.getReadyThreads()) {
                    System.out.println("P" + process.getPID() + "K" + klt.getTID() + "U" + ult.getTID());
                }
            }
        }*/
        System.out.println();


        Queue<Process> blockedQueue = scheduler.getBlockedQueue();

//        System.out.println("RUNNING THREADS");
        KernelLevelThread klt;
//        for (Process process : blockedQueue) {
//            klt = process.getBlockedThread();
//            System.out.print("P" + process.getPID() + "K" + klt.getTID() + "U" + klt.getBlocked());
//        }

        System.out.println("BLOCKED THREADS:");
        for (Process process : blockedQueue) {
            klt = process.getBlockedThread();
            System.out.println("P" + process.getPID() + "K" + klt.getTID() + "U" + klt.getBlocked());
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
