package scheduler;

public class Thread {

    private String type;
    private Integer[] processingTime;

    public Thread(String type, Integer[] processingTime) {
        this.type = type;
        this.processingTime = processingTime;

    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer[] getProcessingTime() {
        return processingTime;
    }

    public void setProcessingTime(Integer[] processingTime) {
        this.processingTime = processingTime;
    }


}
