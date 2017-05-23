package main.model.thread;

import java.util.List;

public class KernelLevelThread extends Thread {

    private List<UserLevelThread> threads;
    private UserLevelThread blockedThread;  // solo puede haber un solo ULT bloqueado
    private ThreadLibraryType threadLibraryType;

    public KernelLevelThread(int TID, int parentPID, List<UserLevelThread> threads, ThreadLibraryType threadLibraryType) {
        super(TID, parentPID, null);
        this.threads = threads;
        this.threadLibraryType = threadLibraryType;
    }

    public boolean execute() {
        //TODO reemplazar por biblioteca de hilos
        return super.execute();
    }
}
