package main.model.process;

import main.model.Core;
import main.model.IO;
import main.model.thread.KernelLevelThread;
import main.model.thread.ThreadState;

import java.util.HashMap;
import java.util.Map;

/**
 */
public class SchedulerRoundRobin extends Scheduler {

    private int quantum;

    private Map<Integer, Integer> quantumMap; // coreID -> time

    public SchedulerRoundRobin(int coreAmount, int ioCount, int quantum) {
        super(coreAmount, ioCount);
        this.quantum = quantum;
        quantumMap = new HashMap<>();
        for (Core core : cores) {
            quantumMap.put(core.getID(), 0);
        }
    }

    @Override
    public void executeAlgorithm(Core core) {
        if (!core.isRunning()) {
            quantumMap.put(core.getID(), 0);
            Process process = readyQueue.poll();
            if (process == null) {
                addOSStep();
                return;
            }

            KernelLevelThread klt = process.getNextKLT();
            core.setCurrentKLT(klt);
            klt.setState(ThreadState.RUNNING);
            if (process.allThreadsRunning()) {
                process.setState(ProcessState.RUNNING);
            }
        }

        KernelLevelThread klt = core.getCurrentKLT();
        Process process = processes.get(klt.getParentPID());
        int currentQuantum = quantumMap.get(core.getID());

        if (process.getState() == ProcessState.BLOCKED) {       // se bloqueo el proceso en otro core, pero este klt seguia corriendo
            core.setCurrentKLT(null);
            quantumMap.put(core.getID(), 0);
            klt.setState(ThreadState.READY);
            process.addReady(klt);
            executeAlgorithm(core);
            return;
        }

        klt.executeCPU(core.getID());
        ++currentQuantum;

        if (currentQuantum < quantum) {
            quantumMap.put(core.getID(), currentQuantum);
            switch (klt.getState()) {
                case RUNNING:
                    break;
                case BLOCKED:
                    core.setCurrentKLT(null);
                    process.setState(ProcessState.BLOCKED);
                    process.setBlockedThread(klt);
                    IO io = IODevices.get(klt.getBlocked().getCurrentBurst().getType() - 1);
                    io.add(klt);
                    break;
                case FINISHED:
                    core.setCurrentKLT(null);
                    if (process.isFinished()) {
                        process.setState(ProcessState.FINISHED);
                    }
                    else if (process.hasAvailableKLT()) {
                        process.setState(ProcessState.READY);
                        readyQueue.add(process);
                        return;
                    }
                    break;
            }

        } else {
            core.setCurrentKLT(null);
            quantumMap.put(core.getID(), 0);    // reseteo el quantum
            switch (klt.getState()) {
                case RUNNING:
                    klt.setState(ThreadState.READY);
                    process.addReady(klt);
                    if (process.hasAvailableKLT()) {        // creo que esto da siempre true
                        process.setState(ProcessState.READY);
                        readyQueue.add(process);
                        return;
                    }
                    break;
                case BLOCKED:
                    process.setState(ProcessState.BLOCKED);
                    process.setBlockedThread(klt);
                    IO io = IODevices.get(klt.getBlocked().getCurrentBurst().getType() - 1);
                    io.add(klt);
                    break;
                case FINISHED:
                    if (process.isFinished()) {
                        process.setState(ProcessState.FINISHED);
                    } else if (process.hasAvailableKLT()) {
                        process.setState(ProcessState.READY);
                        readyQueue.add(process);
                        return;
                    }
                    break;
            }
        }

        if (cores.size() > 1) {
            if (process.hasAvailableKLT()) {
                process.setState(ProcessState.READY);
                readyQueue.add(process);
            }
        }
    }

//    public void executeAlgorithm(Core core) {
//        if (!core.isRunning()) {
//            Process p = readyQueue.peek(); // puede haber mas de un klt del mismo proceso en distintos cores
//            if (p == null) {
//                addOSStep();
//                quantumMap.put(core.getID(), 0);
//                return;
//            }
//            KernelLevelThread klt = p.getNextKLT();
//            core.setCurrentKLT(klt);
//        }
//
//        int currentQuantum = quantumMap.get(core.getID());
//
//        KernelLevelThread klt = core.getCurrentKLT();
//        Process process = processes.get(klt.getParentPID());
//
//        if (currentQuantum != quantum) {        // quantum no termino
//            klt.setState(ThreadState.RUNNING);
//            process.setState(ProcessState.RUNNING);
//            currentQuantum++;
//            if (klt.executeCPU(core.getID())) {
//                ThreadState state = klt.getState();
//                core.setCurrentKLT(null);
//                currentQuantum = 0;
//
//                if (state == ThreadState.BLOCKED) {
//                    IO io = IODevices.get(klt.getBlocked().getCurrentBurst().getType() - 1);
//                    io.add(klt);
//
//                    process.setBlockedThread(klt);
//                    process.setState(ProcessState.BLOCKED);
//                } else if (state == ThreadState.FINISHED) {
//                    if (process.isFinished()) {
//                        process.setState(ProcessState.FINISHED);
//                    } else {
//                        process.setState(ProcessState.READY);
//                        if (process.hasAvailableKLT()) {
//                            readyQueue.add(process);
//                            quantumMap.put(core.getID(),currentQuantum);
//                            return;
//                        }
//                    }
//                }
//            }
//        }
//        else {  // termino el quantum
//            currentQuantum = 0;
//            core.setCurrentKLT(null);
//            if (klt.getState() == ThreadState.RUNNING) {    // todavia le queda algo por correr
//                klt.setState(ThreadState.READY);
//                process.setState(ProcessState.READY);
//                process.addReady(klt);
//
//                // version 1
//                Process p = process;
//                process = readyQueue.poll();        // TODO ver bien esto, elegir una de las dos v
//                if (!readyQueue.contains(p)) {
//                    readyQueue.add(p);
//                }
//                if (process == null){
//                    process = p;
//                }
//                // version 2
////                if (!readyQueue.contains(process)) {
////                    readyQueue.add(process);
////                }
////                process = readyQueue.peek();
//
//
//                if (process != null) {
//                    klt = process.getNextKLT();
//                    core.setCurrentKLT(klt);
//                    klt.setState(ThreadState.RUNNING);
//                    process.setState(ProcessState.RUNNING);
//                    if (klt.executeCPU(core.getID())) {
//                        ThreadState state = klt.getState();
//                        core.setCurrentKLT(null);
//
//                        if (state == ThreadState.BLOCKED) {
//                            IO io = IODevices.get(klt.getBlocked().getCurrentBurst().getType() - 1);
//                            io.add(klt);
//
//                            process.setBlockedThread(klt);
//                            process.setState(ProcessState.BLOCKED);
//                        } else if (state == ThreadState.FINISHED) {
//                            if (process.isFinished()) {
//                                process.setState(ProcessState.FINISHED);
//                            } else {
//                                process.setState(ProcessState.READY);
//                                if (process.hasAvailableKLT()) {
//                                    readyQueue.add(process);
//                                    quantumMap.put(core.getID(),currentQuantum);
//                                    return;
//                                }
//                            }
//                        }
//                        currentQuantum = 0;
//                    }
//                    currentQuantum++;
//                }
//            }
//        }
//
//        quantumMap.put(core.getID(),currentQuantum);
//
//        //el thread sigue corriendo
//        if (cores.size() == 1) {
//            readyQueue.remove(process);
//        }
//        else if (process != null && !process.hasAvailableKLT()) {
//            readyQueue.remove(process); // TODO buscar una mejor forma
//            //readyQueue.poll();
//        }
//    }

}

