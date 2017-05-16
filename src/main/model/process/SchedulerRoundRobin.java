package main.model.process;

/**
 */
public class SchedulerRoundRobin extends Scheduler {

    private Integer quantum;

    public SchedulerRoundRobin(Integer coreAmount, String threadLib, Integer ioCount, int quantum) {
        super(coreAmount, threadLib, ioCount);
        this.quantum = quantum;
    }


    public void executeAlgorithm() {

    }
}

