package main;

import javax.swing.*;
import java.util.List;

public class Thread {

    private List<Burst> bursts;
    private ThreadState state;
    private int currentBurstIndex;
    private LookAndFeel currentBurst;

    public Thread (List<Burst> burstList){
        this.bursts=burstList;
    }

    public void setThreadState(ThreadState state){
        this.state=state;
    }

    /**
     *
     * @return true si terminó la burst actual
     */
    public boolean execute() {

        boolean burstFinished = bursts.get(currentBurstIndex).execute();

        if (burstFinished){
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

    public ThreadState getState() {
        return state;
    }

    public Burst getCurrentBurst() {
//        if (state == ThreadState.FINISHED) {
//            return null;
//        }
        return bursts.get(currentBurstIndex);
    }
}
