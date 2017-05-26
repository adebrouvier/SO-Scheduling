package main.model.thread;

import main.model.Burst;

import java.util.List;

/**
 */
public class UserLevelThread extends Thread {

    private final int parentKltID;

    public UserLevelThread(int parentKltID, int parentPID, List<Burst> burstList) {
        super(parentPID, burstList, "ULT");
        this.parentKltID = parentKltID;
    }

    public int getParentKltID() {
        return parentKltID;
    }
}

