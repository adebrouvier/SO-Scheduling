package main.model.thread;

import main.controller.Log;
import main.model.Burst;

import java.util.List;

/**
 * @see Thread
 */
public class UserLevelThread extends Thread {

    private final int parentKltID;
    private int waitTime;

    public UserLevelThread(int parentKltID, int parentPID, List<Burst> burstList) {
        super(parentPID, burstList, "ULT");
        this.parentKltID = parentKltID;
    }

    /**
     * Updates this ULT's waiting time
     */
    public void update() {

        if (state == ThreadState.READY){
            waitTime++;
        }
        if (state == ThreadState.BLOCKED | state == ThreadState.RUNNING){
            waitTime = 0;
        }
    }

    public void setState(ThreadState state) {
        super.setState(state);
        Log.addULTStateChange(getTID(), state);
    }

    public int getParentKltID() {
        return parentKltID;
    }

    public int getResponseRate(){
        return 1+(this.waitTime/(this.getCurrentBurst().getRemainingTime()));
    }

    public int getWaitTime (){
        return this.waitTime;
    }
}

