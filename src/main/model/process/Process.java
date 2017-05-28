package main.model.process;

import main.model.thread.KernelLevelThread;
import main.model.thread.ThreadState;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class Process {

    private ProcessState state;
    private List<KernelLevelThread> threads;
    private KernelLevelThread blockedThread;
    private Queue<KernelLevelThread> readyThreads;

    private List<KernelLevelThread> finishedThreads;

    /** Process ID */
    private final int PID;
    private static int processCount;

    public Process(List<KernelLevelThread> threads) {
        this.PID = ++processCount;
        this.state = ProcessState.NEW;

        this.threads = threads;
        readyThreads = new LinkedList<>();
        finishedThreads = new ArrayList<>();
    }

    public ProcessState getState() {
        return state;
    }

    public int getPID() {
        return PID;
    }

    public void setState(ProcessState state) {
        this.state = state;
    }

    public boolean isBlocked() {
        boolean blocked = blockedThread != null;
        if (blocked) {
            setState(ProcessState.BLOCKED);
        }
        return blocked;
    }

    public boolean isFinished() {
        for (KernelLevelThread klt: threads) {
            if (klt.getState() != ThreadState.FINISHED)
                return false;
        }
        return true;
    }

    public KernelLevelThread getNextKLT() {
//        for (KernelLevelThread klt : threads) {
//            if (klt.getState() == ThreadState.READY)
//                return klt;
//        }
//        return null;
        return readyThreads.poll();
    }

    public List<KernelLevelThread> getThreads() {
        return threads;
    }

    public boolean hasAvailableKLT() {
        for (KernelLevelThread klt : threads) {
            if (!klt.isFinished() && klt.getState() == ThreadState.READY) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }

        if (!obj.getClass().equals(this.getClass())) {
            return false;
        }

        Process process = (Process)obj;
        return process.PID == PID;
    }

    @Override
    public int hashCode() {
        return PID;
    }

    @Override
    public String toString() {
        return "P" + PID;
    }

    public KernelLevelThread getThread(int TID) {
        for (KernelLevelThread klt : threads) {
            if (klt.getTID() == TID) {
                return klt;
            }
        }
        return null;
    }

    public KernelLevelThread getBlockedThread() {
        return blockedThread;
    }

    public void setBlockedThread(KernelLevelThread blocked) {
        blocked.setState(ThreadState.BLOCKED);
        this.blockedThread = blocked;
    }

    public void addReady(KernelLevelThread klt) {
        klt.setState(ThreadState.READY);
        if (!readyThreads.contains(klt)) {
            readyThreads.add(klt);
        }
    }

    public List<KernelLevelThread> getReadyThreads() {
        List<KernelLevelThread> ready = new ArrayList<>();

        for (KernelLevelThread klt : threads) {
            if (klt.getState() == ThreadState.READY)
                ready.add(klt);
        }

        return ready;
    }

    public void update() {
        for (KernelLevelThread k : threads){
            k.update();
        }
    }
}