package main.model.thread;

import main.model.Burst;

import java.util.ArrayList;
import java.util.List;

/**
 * Each thread has a {@link ThreadState} and a list of {@link Burst}s to execute.
 */
public abstract class Thread {

    private List<Burst> bursts;
    protected ThreadState state;
    private int currentBurstIndex;

    /** Thread ID and parent Process ID */
    private final int TID;
    private final int parentPID;

    private static int KLTCount;
    private static int ULTCount;

    /**
     * <0 = corrio cpu, core -n
     * 0  = no corrio
     * >0 = corrio io, device n
     */
    private List<Integer> trace;

    public Thread(int parentPID, List<Burst> burstList, String type) {
        if (type.equals("ULT")) {
            TID = ++ULTCount;
        } else {
            TID = ++KLTCount;
        }
        this.parentPID = parentPID;
        this.bursts = burstList;
        trace = new ArrayList<>();
        setState(ThreadState.NEW);
    }

    /**
     *
     * @return true if current burst finished
     */
    public boolean execute(int step) {

        trace.set(trace.size() -1, step);

        boolean burstFinished = bursts.get(currentBurstIndex).execute();

        if (burstFinished) {
            int burstType = bursts.get(currentBurstIndex).getType();
            if(burstType == 0) {
                state = ThreadState.BLOCKED;
            } else {
                state = ThreadState.READY;
            }

            currentBurstIndex++;
            if (currentBurstIndex == bursts.size()){
                state = ThreadState.FINISHED;
            }
        }

        return burstFinished;
    }

    public Burst getCurrentBurst() {
        return bursts.get(currentBurstIndex);
    }

    public ThreadState getState() {
        return state;
    }

    public void setState(ThreadState state){
        this.state = state;
    }

    public int getTID() {
        return TID;
    }

    public int getParentPID() {
        return parentPID;
    }

    public List<Integer> getTrace() {
        return trace;
    }

    public void addStep() {
        trace.add(0);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }

        if (!obj.getClass().equals(this.getClass())) {
            return false;
        }

        Thread thread = (Thread) obj;
        return thread.TID == TID;
    }

    @Override
    public int hashCode() {
        return TID;
    }

    @Override
    public String toString() {
        return getClass().equals(KernelLevelThread.class) ? "K" + TID : "U"  + TID;
    }
}