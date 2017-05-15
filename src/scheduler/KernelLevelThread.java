package scheduler;

public class KernelLevelThread extends Thread {

    private UserLevelThread[] threads;

    public KernelLevelThread(Burst[] processingTime) {
        super(processingTime);
    }

    public UserLevelThread[] getThreads() {
        return threads;
    }

    public void setThreads(UserLevelThread[] threads) {
        this.threads = threads;
    }

}
