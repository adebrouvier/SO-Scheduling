package main.model.thread;

import main.model.Burst;

import java.util.List;

/**
 */
public class UserLevelThread extends Thread {

    private final int parentKltID;

    public UserLevelThread(int TID, int parentKltID, int parentPID, List<Burst> burstList) {
        super(TID, parentPID, burstList);
        this.parentKltID = parentKltID;
    }
}
