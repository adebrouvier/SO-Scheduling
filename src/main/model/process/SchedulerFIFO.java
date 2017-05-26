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
    public void executeAlgorithm() {

        for (Core core : cores) {

            if (core.getCurrentKLT() != null) {
                System.out.println("KLT ID: " + core.getCurrentKLT().getTID());
            }

            if (!core.isRunning()) {
                Process p = readyQueue.peek(); // puede haber mas de un klt del mismo proceso en distintos cores
                if (p == null) {
                    System.out.println("CORRE EL SO -----------------------------------");
                    //TODO Corre el SO
                    continue;
                }
                KernelLevelThread klt = p.getNextKLT();
                core.setCurrentKLT(klt);
                klt.setState(ThreadState.RUNNING);
                p.setState(ProcessState.RUNNING);
            }

            KernelLevelThread klt = core.getCurrentKLT();
            Process process = processes.get(klt.getParentPID());

            if (klt.executeCPU(core.getID())) { // se bloqueo o termino el thread
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
            }

            //el thread sigue corriendo
            if (!process.hasAvailableKLT()) { //Checkea que si al proceso le queda algo para correr.
                readyQueue.remove(process);
                //readyQueue.poll();
            }

        }


    }
}