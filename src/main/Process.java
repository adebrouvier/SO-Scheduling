package main;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class Process {

    private static int processCount;

    private ProcessState state;
    private List<KernelLevelThread> klt;
    private Queue<KernelLevelThread> blockedThreads;
    private Queue<KernelLevelThread> readyThreads;

    private List<KernelLevelThread> finishedThreads;

    private int pid;

    public Process (){
        this.pid = processCount++;
        this.state = ProcessState.NEW;

        klt = new ArrayList<>();
        blockedThreads = new LinkedList<>();
        readyThreads = new LinkedList<>();
        finishedThreads = new ArrayList<>();
    }

    public ProcessState getState() {
        return state;
    }

    public int getPid() {
        return pid;
    }

    public void setState(ProcessState state) {
        this.state = state;
    }

    public boolean isBlocked() {
        boolean blocked = blockedThreads.size() == klt.size();
        if (blocked) {
            setState(ProcessState.BLOCKED);
        }
        return blocked;
    }

    public boolean isFinished() {
        boolean finished = klt.size() == finishedThreads.size();
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
}
