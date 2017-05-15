package scheduler;

public class Thread {

    private Burst[] bursts;
    private Integer currentBurst;

    private Integer remainingTime;

    private Integer affinity;

    public Thread(Burst[] processingTime) {
        this.bursts = processingTime;
        for(Burst b : processingTime) {
            this.remainingTime += b.getTime();
        }
    }

    /**
     *
     * @return true if current burst is finished
     */
    public boolean decreaseTime() {

        boolean currentBurstFinished = bursts[currentBurst].decreaseTime();

        remainingTime--;

        if (currentBurstFinished) {
            currentBurst = currentBurst++;
            if (currentBurst == bursts.length) {
                // TODO que pasa cuando se ejecutó to do el thread
            }
        }

        return currentBurstFinished;
    }

    public Burst[] getBursts() {
        return bursts;
    }

    public void setBursts(Burst[] bursts) {
        this.bursts = bursts;
    }

    public Integer getAffinity() {
        return affinity;
    }


}