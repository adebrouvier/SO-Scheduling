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

    private Map<Integer, Integer> timeMap; // coreID -> time

    public SchedulerRoundRobin(int coreAmount, int ioCount, int quantum) {
        super(coreAmount, ioCount);
        this.quantum = quantum;
        timeMap = new HashMap<>();
        for (Core core : cores) {
            timeMap.put(core.getID(), 0);
        }
    }

    public void executeAlgorithm(Core core) {
        if (!core.isRunning()) {
            Process p = readyQueue.peek(); // puede haber mas de un klt del mismo proceso en distintos cores
            if (p == null) {
                addOSStep();
                timeMap.put(core.getID(), 0);
                return;
            }
            KernelLevelThread klt = p.getNextKLT();
            core.setCurrentKLT(klt);
        }

        int currentQuantum = timeMap.get(core.getID());

        KernelLevelThread klt = core.getCurrentKLT();
        Process process = processes.get(klt.getParentPID());

        if (currentQuantum != quantum) {        // quantum no termino
            klt.setState(ThreadState.RUNNING);
            process.setState(ProcessState.RUNNING);
            currentQuantum++;
            if (klt.executeCPU(core.getID())) {
                ThreadState state = klt.getState();
                core.setCurrentKLT(null);

                if (state == ThreadState.BLOCKED) {
                    IO io = IODevices.get(klt.getBlocked().getCurrentBurst().getType() - 1);
                    io.add(klt);

                    process.setBlockedThread(klt);
                    process.setState(ProcessState.BLOCKED);
                } else if (state == ThreadState.FINISHED) {
                    if (process.isFinished()) {
                        process.setState(ProcessState.FINISHED);
                    } else {
                        process.setState(ProcessState.READY);
                    }
                }
                currentQuantum = 0;
            }
        }
        else {  // termino el quantum
            currentQuantum = 0;
            core.setCurrentKLT(null);
            if (klt.getState() == ThreadState.RUNNING) {    // todavia le queda algo por correr
                klt.setState(ThreadState.READY);
                process.setState(ProcessState.READY);
                process.addReady(klt);

                // version 1
                Process p = process;
                process = readyQueue.poll();        // TODO ver bien esto, elegir una de las dos v
                if (!readyQueue.contains(p)) {
                    readyQueue.add(p);
                }
                if (process == null){
                    process = p;
                }
                // version 2
//                if (!readyQueue.contains(process)) {
//                    readyQueue.add(process);
//                }
//                process = readyQueue.peek();


                if (process != null) {
                    klt = process.getNextKLT();
                    core.setCurrentKLT(klt);
                    klt.setState(ThreadState.RUNNING);
                    process.setState(ProcessState.RUNNING);
                    if (klt.executeCPU(core.getID())) {
                        ThreadState state = klt.getState();
                        core.setCurrentKLT(null);

                        if (state == ThreadState.BLOCKED) {
                            IO io = IODevices.get(klt.getBlocked().getCurrentBurst().getType() - 1);
                            io.add(klt);

                            process.setBlockedThread(klt);
                            process.setState(ProcessState.BLOCKED);
                        } else if (state == ThreadState.FINISHED) {
                            if (process.isFinished()) {
                                process.setState(ProcessState.FINISHED);
                            } else {
                                process.setState(ProcessState.READY);
                            }
                        }
                        currentQuantum = 0;
                    }
                    currentQuantum++;
                }
            }
        }

        timeMap.put(core.getID(),currentQuantum);

        //el thread sigue corriendo
        if (process != null && !process.hasAvailableKLT()) { //Checkea que si al proceso le queda algo para correr.
            readyQueue.remove(process);
            //readyQueue.poll(); // TODO buscar una mejor forma
        }
    }

}

