package scheduler.planification;

import scheduler.Burst;
import scheduler.Scheduler;

/**
 */
public class SchedulerRoundRobin extends Scheduler {

    private Integer quantum;

    public SchedulerRoundRobin(scheduler.Process[] processes, Burst[] bursts, Integer coreAmount, String threadLib) {
        super(processes, bursts, coreAmount, threadLib);
    }

    public void executeAlgorithm() {

    }
}

