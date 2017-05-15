package scheduler;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

public class Process {

    private Integer arrivalTime;
    private KernelLevelThread[] threads;

    private Integer currentThreadIndex = -1;

    private Queue<Thread> readyQueue;
    private Queue<Thread> blockedQueue;

    private Integer remainingTime;

    public Process(Integer arrivalTime, KernelLevelThread[] threads) {
        this.arrivalTime = arrivalTime;
        this.threads = threads;
    }

    /**
     *
     * @return true if burst of current thread is finished
     */
    public boolean decreaseTime() {
        remainingTime--;
        if (threads[currentThreadIndex].decreaseTime()) {
            //if (threads[currentThreadIndex].isFinished())
            //  threads[currentThreadIndex] = null;
            return true;
        }

        return false;
    }

    public Integer getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(Integer arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public List<KernelLevelThread> getThreads() {

        List<KernelLevelThread> list = new ArrayList<>();
        for (KernelLevelThread klt: threads) {
            list.add(klt);
        }

        return list;
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
