package main.model.thread;

import main.model.Burst;

public class KernelLevelThread extends Thread {

    private UserLevelThread[] threads;

    public KernelLevelThread(Burst[] processingTime) {
        super(processingTime);
    }

    //TODO
    public boolean execute() {
        // si tiene ults, usar la biblioteca
        // sino, usar super.execute();
        return false;
    }

    public UserLevelThread[] getThreads() {
        return threads;
    }

    public void setThreads(UserLevelThread[] threads) {
        this.threads = threads;
    }

    public int getDuration() {
        return remainingTime;
    }

}
