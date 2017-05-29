package main.controller;

import main.model.process.Process;
import main.model.process.ProcessState;
import main.model.process.Scheduler;
import main.model.thread.KernelLevelThread;
import main.model.thread.UserLevelThread;

import java.util.*;

public class Gantt {

    /**
     * Inner class that saves the trace of every thread and
     * blocked, ready, running and finished processes
     */
    private class TraceNode {
        private Map<String, List<Integer>> traceMap; // Thread ID -> Trace
        private List<Integer> OSTrace;

        //running, blocked, ready and finished processes
        private List<String> runningProcesses;
        private List<String> blockedProcesses;
        private List<String> readyProcesses;
        private List<String> finishedProcesses;

        TraceNode() {
            traceMap = new HashMap<>();
            OSTrace = new ArrayList<>();
            runningProcesses = new ArrayList<>();
            blockedProcesses = new ArrayList<>();
            readyProcesses = new ArrayList<>();
            finishedProcesses = new ArrayList<>();
        }

        public void addProcess(String id, ProcessState state) {
            switch (state) {
                case RUNNING:
                    runningProcesses.add(id);
                    break;
                case BLOCKED:
                    blockedProcesses.add(id);
                    break;
                case READY:
                    readyProcesses.add(id);
                    break;
                case FINISHED:
                    finishedProcesses.add(id);
                    break;
            }
        }

        public void addTrace(String id, List<Integer> trace) {
            traceMap.put(id, new ArrayList<>(trace));
        }

        public void setOSTrace(List<Integer> OSTrace) {
            this.OSTrace = OSTrace;
        }

        public Map<String, List<Integer>> getTraceMap() {
            return traceMap;
        }

        public List<Integer> getOSTrace() {
            return OSTrace;
        }

        public List<String> getRunningProcesses() {
            return runningProcesses;
        }

        public List<String> getBlockedProcesses() {
            return blockedProcesses;
        }

        public List<String> getReadyProcesses() {
            return readyProcesses;
        }

        public List<String> getFinishedProcesses() {
            return finishedProcesses;
        }
    }

    private List<TraceNode> trace;

    public Gantt() {
        trace = new ArrayList<>();
    }

    /**
     * Adds a new column to the Gantt diagram
     * @param scheduler scheduler that needs to be printed
     */
    public void addTraceNode(Scheduler scheduler) {
        TraceNode node = new TraceNode();
        int pid, kid, uid;

        for (Process process : scheduler.getProcesses()) {
            node.addProcess("P" + process.getPID(), process.getState());
            for (KernelLevelThread klt : process.getThreads()) {
                for (UserLevelThread ult : klt.getThreads()) {
                    uid = ult.getTID();
                    kid = klt.getTID(); // same as ult.getParentKltID();
                    pid = process.getPID(); // same as ult.getParentPID() or klt.getParentPID();
                    node.addTrace("P" + pid + "K" + kid + "U" + uid, ult.getTrace());
                }
            }
        }

        node.setOSTrace(scheduler.getOSTrace());

        trace.add(node);
    }

    /**
     * Prints the Gantt diagram at a given time
     * @param time time to print
     */
    public void print(int time) {
        if (time >= trace.size()) {
            throw new IllegalArgumentException("Invalid time!");
        }

        print(trace.get(time), time);
    }

    private void print(TraceNode node, int time) {
        Set<String> set = new TreeSet<>(node.getTraceMap().keySet());

        int longestName = 0;

        for (String id : set) {
            if(id.length() > longestName) {
                longestName = id.length();
            }
        }

        int aux;
        for (String id : set) {
            System.out.print(id);
            aux = id.length();
            while(aux++ <  longestName) {
                System.out.print(" ");
            }
            System.out.print(" | ");
            for (Integer step : node.getTraceMap().get(id)) {
                if (step == 0) {
                    System.out.print("    ");
                } else if (step < 0) {
                    System.out.print("CPU" + step * (-1) );
                } else {
                    System.out.print("I/O" + step);
                }
                System.out.print(" | ");
            }
            System.out.println();
        }

        aux = 0;
        while(aux++ <  longestName) {
            System.out.print("-");
        }
        System.out.print("-|");
        for (int i = 1; i <= time; i++) {
            if (i > 99) {
                System.out.print("-" + i + "--|");
            } else if (i > 9) {
                System.out.print("--" + i + "--|");
            } else {
                System.out.print("---" + i + "--|");
            }
        }
        System.out.println();
        System.out.print("OS");
                aux = 0;
        while(aux++ <  longestName - 2) {
            System.out.print(" ");
        }
        System.out.print(" | ");

        for (int i = 1; i < node.getOSTrace().size(); i++) {
            if (node.getOSTrace().get(i) == 0) {
                System.out.print("    ");
            } else {
                System.out.print("  X ");
            }
            System.out.print(" | ");
        }

        System.out.println();
        System.out.println();
        System.out.println("RUNNING PROCESSES: " + node.getRunningProcesses());
        System.out.println("BLOCKED PROCESSES: " + node.getBlockedProcesses());
        System.out.println("READY PROCESSES: " + node.getReadyProcesses());
        System.out.println("FINISHED PROCESSES: " + node.getFinishedProcesses());
        System.out.println();
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
