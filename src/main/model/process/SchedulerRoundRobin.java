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
                return;
            }
            KernelLevelThread klt = p.getNextKLT();

            core.setCurrentKLT(klt);
            klt.setState(ThreadState.RUNNING);
            p.setState(ProcessState.RUNNING);
        }

        int currentQuantum = timeMap.get(core.getID());

        KernelLevelThread klt = core.getCurrentKLT();
        Process process = processes.get(klt.getParentPID());

        if (currentQuantum != quantum) {        // quantum no termino
            klt.setState(ThreadState.RUNNING);
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
                    }
                }
                currentQuantum = 0;
            }
        }
        else {  // termino el quantum
            currentQuantum = 0;

            if (klt.getState() == ThreadState.RUNNING) {
                klt.setState(ThreadState.READY);
                process.setState(ProcessState.READY);
                process.addReady(klt);
                if (!readyQueue.contains(process)) {
                    readyQueue.add(process);
                }

                Process p = readyQueue.poll();
                if (p != null) {
                    klt = p.getNextKLT();
                    core.setCurrentKLT(klt);
                    klt.setState(ThreadState.RUNNING);
                    p.setState(ProcessState.RUNNING);
                    klt.executeCPU(core.getID());
                    currentQuantum++;
                }
            }

        }

        timeMap.put(core.getID(),currentQuantum);

        //el thread sigue corriendo
        if (!process.hasAvailableKLT()) { //Checkea que si al proceso le queda algo para correr.
            readyQueue.remove(process);
            //readyQueue.poll(); // TODO buscar una mejor forma
        }
    }

}

