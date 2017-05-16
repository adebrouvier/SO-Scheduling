package main.model.process;

import main.model.thread.KernelLevelThread;
import main.model.thread.Thread;
import main.model.thread.ThreadState;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

public class Process {

    private ProcessState state;
    private KernelLevelThread[] threads;

    private Integer currentThreadIndex = -1;

    // TODO KEH
    private Queue<Thread> readyQueue;
    private Queue<Thread> blockedQueue;

    private Integer remainingTime;

    protected Integer pid;

    public Process(KernelLevelThread[] threads) {
        this.threads = threads;

        for (int i = 0; i < threads.length; i++) {
            remainingTime += threads[i].getDuration();
        }

        state = ProcessState.NEW;
    }

    /**
     *  Runs the process 1 unit of time
     * @return true if burst of current thread is finished
     */
    public boolean execute() {
        if (remainingTime > 0) {
            remainingTime--;
        }

        if (currentThreadIndex == -1) { //primera vez que se ejecuta el proceso
            state = ProcessState.EXECUTING;
            currentThreadIndex = 0;
        }

        if (state == ProcessState.EXECUTING) {
            Thread currentThread = threads[currentThreadIndex];
            if (currentThread.execute()) {        // termino la burst del thread actual
                if (currentThread.getState() == ThreadState.FINISHED) {
                    threads[currentThreadIndex] = null;
                    currentThreadIndex++;
                    if (currentThreadIndex == threads.length) { // se corrieron todos los threads, en orden. Esto es asi???
                        state = ProcessState.FINISHED;
                    }
                }
                if (currentThread.getState() == ThreadState.BLOCKED) {
                    state = ProcessState.BLOCKED; // TODO CHECKEAR CON NESTOR

                }
                return true;
            }
        }

        return false;
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

    public void setState(ProcessState state) {
        this.state = state;
    }

    public ProcessState getState() {
        return state;
    }

}
