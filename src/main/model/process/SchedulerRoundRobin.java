package main.model.process;

/**
 */
public class SchedulerRoundRobin extends Scheduler {

    private int quantum;

    public SchedulerRoundRobin(int coreAmount, int ioCount, int quantum) {
        super(coreAmount, ioCount);
        this.quantum = quantum;
    }


    public void executeAlgorithm() {

    }
}

