package main.model.thread;

import main.model.Burst;

import java.util.ArrayList;

public class KernelLevelThread extends Thread {

    private ArrayList<UserLevelThread> threads;

    public KernelLevelThread(Burst[] processingTime) {
        super(processingTime);
    }

    //TODO
    public boolean execute() {
        // si tiene ults, usar la biblioteca
        // sino, usar super.execute();
        return false;
    }

    public ArrayList<UserLevelThread> getThreads() {
        return threads;
    }

    public int getDuration() {
        return remainingTime;
    }

    public void addUserLevelThread(UserLevelThread u) {
        if (threads == null){
            threads = new ArrayList<>();
        }
        this.threads.add(u);
    }
}
