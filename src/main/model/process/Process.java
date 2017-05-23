package main.model.process;

import main.model.thread.KernelLevelThread;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class Process {

    private ProcessState state;
    private List<KernelLevelThread> threads;
    private Queue<KernelLevelThread> blockedThreads;
    private Queue<KernelLevelThread> readyThreads;

    private List<KernelLevelThread> finishedThreads;

    /** Process ID */
    private final int PID;

    public Process (int PID, List<KernelLevelThread> threads) {
        this.PID = PID;
        this.state = ProcessState.NEW;

        this.threads = threads;
        blockedThreads = new LinkedList<>();
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
        boolean blocked = blockedThreads.size() == threads.size();
        if (blocked) {
            setState(ProcessState.BLOCKED);
        }
        return blocked;
    }

    public boolean isFinished() {
        boolean finished = threads.size() == finishedThreads.size();
        if (finished) {
            setState(ProcessState.FINISHED);
        }
        return finished;
    }


    // TODO LOGICA MERCA OP
    public KernelLevelThread getCurrentKLT() {
        if(!readyThreads.isEmpty()){
            KernelLevelThread nextklt = readyThreads.poll();
            return nextklt;
        }else{
            return null;
        }

    }

    public boolean hasAvailableKLT() {
        return !readyThreads.isEmpty();
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
}