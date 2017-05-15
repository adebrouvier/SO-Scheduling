package scheduler;

import java.util.ArrayList;

public class KernelLevelThread extends Thread {

    private ArrayList<UserLevelThread> threads;

    public KernelLevelThread(Burst[] processingTime) {
        super(processingTime);
    }

    public ArrayList<UserLevelThread> getThreads() {
        return threads;
    }

    public void setThreads(ArrayList<UserLevelThread> threads) {
        this.threads = threads;
    }

    public void addUserLevelThread(UserLevelThread u) {
        if (threads == null){
            threads = new ArrayList<>();
        }
        this.threads.add(u);
    }
}
