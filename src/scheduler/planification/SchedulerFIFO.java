package scheduler.planification;

import scheduler.Burst;
import scheduler.Core;
import scheduler.Process;
import scheduler.Scheduler;

/**
 */
public class SchedulerFIFO extends Scheduler {


    public SchedulerFIFO(Process[] processes, Burst[] bursts, Integer coreAmount, String threadLib, Integer ioCount) {
        super(processes, bursts, coreAmount, threadLib, ioCount);
    }

    public void executeAlgorithm(){

        for(Core core : cores) {

            //readyQueue.peek().getNextBurst().isIO()

            //if (core.getCurrentProcess())
            if(!core.isRunning()){

                Process t = readyQueue.poll();

//                if(t.getAffinity().equals(core.getID())) {
//
//                }
            }

        }

    }

}
