package scheduler;

public class Thread {

    private Burst[] processingTime;
    private Integer remainingTime;

    public Thread(){

    }

    public Thread(Burst[] processingTime) {
        this.processingTime = processingTime;
        for(Burst b : processingTime) {
            this.remainingTime += b.getTime();
        }

    }

    public Burst[] getProcessingTime() {
        return processingTime;
    }

    public void setProcessingTime(Burst[] processingTime) {
        this.processingTime = processingTime;
    }


}
