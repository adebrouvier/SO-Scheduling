package main.model.process;

import main.model.Core;
import main.model.IO;
import main.model.thread.KernelLevelThread;
import main.model.thread.ThreadState;

/**
 */
public class SchedulerFIFO extends Scheduler {

    public SchedulerFIFO (int coreCount, int ioCount){
        super(coreCount,ioCount);
    }

    @Override
    public void executeAlgorithm(Core core) {

        // si no hay nada en el core lo asigno
        if (!core.isRunning()) {
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

        // corro el klt
        KernelLevelThread klt = core.getCurrentKLT();
        Process process = processes.get(klt.getParentPID());

        if (process.getState() == ProcessState.BLOCKED) {       // se bloqueo el proceso pero este klt seguia corriendo
            core.setCurrentKLT(null);
            klt.setState(ThreadState.READY);
            process.addReady(klt);
            executeAlgorithm(core);
            return;
        }

        klt.executeCPU(core.getID());

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
                } else if (process.hasAvailableKLT()) {
                    process.setState(ProcessState.READY);
                    readyQueue.add(process);
                    return;
                }
                break;
        }

        if (cores.size() > 1) {
            if (process.hasAvailableKLT()) {
                process.setState(ProcessState.READY);
                readyQueue.add(process);
            }
        }
    }
}