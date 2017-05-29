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
            if (process == null || !process.hasAvailableKLT()) {
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
                case READY:
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
                case READY:
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

}

