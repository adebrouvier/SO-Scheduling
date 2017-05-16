package main.model.process;

import main.model.Core;

/**
 */
public class SchedulerFIFO extends Scheduler {

    public SchedulerFIFO(Integer coreAmount, String threadLib, Integer ioCount) {
        super(coreAmount, threadLib, ioCount);
    }

    //TODO affinity??
    public void executeAlgorithm(){

        for (Core core: cores) {
            if (core.getCurrentProcess() == null) {              // si el core esta idle
                core.setCurrentProcess(readyQueue.poll());
            }

            Process currentProcess = core.getCurrentProcess();

            if (currentProcess != null) {
                if (currentProcess.execute()) {      // se bloqueó o terminó
                    if (currentProcess.getState() == ProcessState.FINISHED) {
                        //TODO FIN?
                    }
                    if (currentProcess.getState() == ProcessState.BLOCKED) {
                        blockedQueue.add(currentProcess);
                    }
                    core.free(); //libero el core
                }
            }
        }

    }

}
