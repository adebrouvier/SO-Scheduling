package scheduler;

public class Thread {

    private ThreadType type;
    private Integer[] processingTime;

    public Thread(ThreadType type, Integer[] processingTime) {
        this.type = type;
        this.processingTime = processingTime;

    }

    public ThreadType getType() {
        return type;
    }

    public void setType(ThreadType type) {
        this.type = type;
    }

    public Integer[] getProcessingTime() {
        return processingTime;
    }

    public void setProcessingTime(Integer[] processingTime) {
        this.processingTime = processingTime;
    }


}
