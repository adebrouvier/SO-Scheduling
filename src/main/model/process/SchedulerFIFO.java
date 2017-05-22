package main.model.process;

import main.model.Core;
import main.model.IO;
import main.model.thread.KernelLevelThread;
import main.model.thread.ThreadState;

/**
 */
public class SchedulerFIFO extends Scheduler{

    public SchedulerFIFO (int coreCount, int ioCount){
        super(coreCount,ioCount);
    }

    @Override
    public void executeAlgorithm() {

        for (Core core : cores) {

            if (!core.isRunning()) {

                Process p = readyQueue.peek(); // puede haber mas de un klt del mismo proceso en distintos cores

                if (p == null) {
                    //TODO Corre el SO
                    continue;
                }



                KernelLevelThread klt = p.getCurrentKLT(); //TODO este metodo hace la merca (elige el klt segun nuestra logica merca dentro del proceso merca dentro del proceso merca)
                core.setCurrentKLT(klt);
            }

            KernelLevelThread klt = core.getCurrentKLT();
            Process process = processes.get(klt.getParentID());
            if (klt.execute()) { // se bloqueo o termino el thread
                ThreadState state = klt.getState();

                if (state == ThreadState.BLOCKED) {
                    //TODO actualizar las listas de bloqueados en process
                    IO io = IODevices.get(klt.getCurrentBurst().getType());
                    io.add(klt);

                    // if (todos los klt del proceso estan bloqueados)
                    if (process.isBlocked()) { // TODO update lists
                        //process.setState(ProcessState.BLOCKED);
                        blockedQueue.add(process);
                    }
                } else if (state == ThreadState.FINISHED) {
                    // if (todos los klt del proceso estan finished)
                    if (process.isFinished()) {
                        //process.setState(ProcessState.FINISHED);
                    }
                }
            }

            //el thread sigue corriendo

            if (process.hasAvailableKLT()) {
                readyQueue.poll();
            }

        }


    }
}