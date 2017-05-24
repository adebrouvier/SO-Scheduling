package main.model.thread;

import java.util.List;

public class KernelLevelThread extends Thread {

    private List<UserLevelThread> threads;
    private UserLevelThread runningThread;
    private UserLevelThread blockedThread;  // solo puede haber un solo ULT bloqueado
    private ThreadLibraryType threadLibraryType;

    public KernelLevelThread(int parentPID, List<UserLevelThread> threads, ThreadLibraryType threadLibraryType) {
        super(parentPID, null); // le paso null porque no existen los KLT puros
        this.threads = threads;
        this.threadLibraryType = threadLibraryType;
    }

    public boolean execute() {
        //TODO reemplazar por biblioteca de hilos
        return super.execute();
    }

    public UserLevelThread getRunningThread() {
        return runningThread;
    }

    public List<UserLevelThread> getThreads() {
        return threads;
    }

}
