package main.model.thread;

import main.model.Burst;

public abstract class Thread {

    protected ThreadState state;
    protected Burst[] bursts;
    protected Integer currentBurst;

    protected Integer remainingTime;

    protected Integer affinity;

    public Thread(Burst[] processingTime) {
        this.bursts = processingTime;
        for(Burst b : processingTime) {
            this.remainingTime += b.getRemainingTime();
        }
        currentBurst = 0;
    }

    /**
     *
     * @return true if current burst is finished
     */
    public boolean execute() {
        boolean currentBurstFinished = bursts[currentBurst].execute();

        remainingTime--;

        if (currentBurstFinished) {
            currentBurst = currentBurst++;
            if (currentBurst == bursts.length) {
                state = ThreadState.FINISHED;
            }
        }

        return currentBurstFinished;
    }

    public Integer getAffinity() {
        return affinity;
    }

    public ThreadState getState() {
        return state;
    }
}