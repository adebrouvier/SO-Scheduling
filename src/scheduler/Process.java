package scheduler;

import java.util.Queue;

public class Process {

    private Integer arrivalTime;
    private KernelLevelThread[] threads;

    private Queue<Thread> readyQueue;
    private Queue<Thread> blockedQueue;

    private Integer remainingTime;

    public Process(Integer arrivalTime, KernelLevelThread[] threads) {
        this.arrivalTime = arrivalTime;
        this.threads = threads;
    }

    public Integer getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(Integer arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public KernelLevelThread[] getThreads() {
        return threads;
    }

    public void setThreads(KernelLevelThread[] threads) {
        this.threads = threads;
    }

    public Queue<Thread> getReadyQueue() {
        return readyQueue;
    }

    public void setReadyQueue(Queue<Thread> readyQueue) {
        this.readyQueue = readyQueue;
    }

    public Queue<Thread> getBlockedQueue() {
        return blockedQueue;
    }

    public void setBlockedQueue(Queue<Thread> blockedQueue) {
        this.blockedQueue = blockedQueue;
    }

    public Integer getRemainingTime() {
        return remainingTime;
    }

    public void setRemainingTime(Integer remainingTime) {
        this.remainingTime = remainingTime;
    }
}
