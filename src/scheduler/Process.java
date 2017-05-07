package scheduler;

public class Process {

    private Integer arrivalTime;
    private Thread[] threads;

    public Process(Integer arrivalTime, Thread[] threads) {
        this.arrivalTime = arrivalTime;
        this.threads = threads;
    }

    public Integer getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(Integer arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public Thread[] getThreads() {
        return threads;
    }

    public void setThreads(Thread[] threads) {
        this.threads = threads;
    }
}
