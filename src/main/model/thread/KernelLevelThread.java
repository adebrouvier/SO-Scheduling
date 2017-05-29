package main.model.thread;

import main.controller.Log;
import main.model.thread.library.*;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * Represents a KLT of a {@link main.model.process.Process}.
 * Each KLT has a list of {@link UserLevelThread} and uses and {@link Algorithm} to schedule their execution.
 * this algorithm is different to the {@link main.model.process.Scheduler}
 */
public class KernelLevelThread extends Thread {

    private List<UserLevelThread> threads;

    private Queue<UserLevelThread> readyThreads;
    private UserLevelThread runningThread;
    private UserLevelThread blockedThread;  // solo puede haber un solo ULT bloqueado
    private UserLevelThread unblockedThread; // ult que se desbloqueó en el instante anterior

    private Algorithm algorithm;

    public KernelLevelThread(int parentPID, List<UserLevelThread> threads, ThreadLibraryType threadLibraryType, int quantum) {
        super(parentPID, null, "KLT"); // le paso null porque no existen los KLT puros
        this.threads = threads;
        switch (threadLibraryType) {
            case FIFO:
                algorithm = new FIFO();
                break;
            case RR:
                algorithm = new RR(quantum);
                break;
            case SPN:
                algorithm = new SPN();
                break;
            case SRT:
                algorithm = new SRT();
                break;
            case HRRN:
                algorithm = new HRRN();
                break;
        }
        readyThreads = new LinkedList<>();
    }

    /**
     * Executes and instant of CPU burst
     * @param core core to execute
     * @return true if this KLT is finished or blocked
     */
    public void executeCPU(int core) {
        runningThread = algorithm.execute(readyThreads, runningThread, core);

        if (runningThread != null) {
            switch (runningThread.getState()) {
                case READY:
                case RUNNING:
                    break;
                case BLOCKED:
                    setState(ThreadState.BLOCKED);
                    blockedThread = runningThread;
                    runningThread = null;
                    break;
                case FINISHED:
                    //runningThread.setState(ThreadState.FINISHED);
                    runningThread = null;
                    if (isFinished()) {
                        blockedThread = null; // no hace falta
                        setState(ThreadState.FINISHED);
                    }
                    break;
            }
        }
    }

    /**
     * Executes an instant of IO burst
     * @param device io device
     * @return true if this KLT is no longer blocked
     */
    public boolean executeIO(int device) {
        unblockedThread = null;
        if(blockedThread.execute(device)) {
            unblockedThread = blockedThread;
            blockedThread = null;
            return true;
        }

        return false;
    }

    /**
     * @return true if every ULT of this KLT is finished
     */
    public boolean isFinished() {
        for (UserLevelThread ult : threads) {
            if (ult.getState() != ThreadState.FINISHED) {
                return false;
            }
        }
        return true;
    }

    /**
     * Adds a ready ULT to the readyThreads Queue
     * @param ult
     */
    public void addReady(UserLevelThread ult) {
        if (!readyThreads.contains(ult)) {
            readyThreads.add(ult);
        }
    }

    /**
     * Updates every ULT
     */
    public void update(){
        for (UserLevelThread u : threads){
            u.update();
        }
    }

    public void setState(ThreadState state) {
        super.setState(state);
        Log.addKLTStateChange(getTID(), state);
    }

    public UserLevelThread getUnblockedThread(){
        return unblockedThread;
    }

    public List<UserLevelThread> getThreads(){
        return threads;
    }

    public Thread getBlocked() {
        return blockedThread;
    }

    public UserLevelThread getRunningThread() {
        return runningThread;
    }

    public List<UserLevelThread> getReadyThreads() {
        return new ArrayList<>(readyThreads);
    }

}
