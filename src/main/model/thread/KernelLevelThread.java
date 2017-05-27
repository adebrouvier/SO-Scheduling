package main.model.thread;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class KernelLevelThread extends Thread {

    private List<UserLevelThread> threads;

    private Queue<UserLevelThread> readyThreads;
    private UserLevelThread runningThread;
    private UserLevelThread blockedThread;  // solo puede haber un solo ULT bloqueado
    private Algorithm algorithm;

    public KernelLevelThread(int parentPID, List<UserLevelThread> threads, ThreadLibraryType threadLibraryType,int quantum) {
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
        }
        readyThreads = new LinkedList<>();
    }

    /**
     * @param core core to execute
     * @return true if this thread is finished or blocked
     */
    public boolean executeCPU(int core) {
//        TNode tempNode;
//
        runningThread = algorithm.execute(readyThreads, runningThread, core);

        if (runningThread != null) {
            switch (runningThread.getState()) {
                case READY:
                case RUNNING:
                    return false;
                case BLOCKED:
                    setState(ThreadState.BLOCKED);
                    blockedThread = runningThread;
                    runningThread = null;
                    return true;
                case FINISHED:
                    runningThread.setState(ThreadState.FINISHED);
                    runningThread = null;
                    if (isFinished()) {
                        blockedThread = null; // no hace falta
                        setState(ThreadState.FINISHED);
                        return true;
                    }
                    break;
            }
        }

        return false;
    }

    private UserLevelThread unblockedThread = null;

//    public boolean executeIO(int device) {
//        if(blockedThread.execute(device)){
//            setState(ThreadState.READY);
//            readyThreads.add(blockedThread);
//            blockedThread = null;
//            return true;
//        }else{
//            return false;
//        }
//    }

    public boolean executeIO(int device) {
        unblockedThread = null;
        if(blockedThread.execute(device)) {
//            setState(ThreadState.READY);
//            if (!readyThreads.contains(blockedThread))
//                readyThreads.add(blockedThread);
            unblockedThread = blockedThread;
            blockedThread = null;
            return true;
        }

        return false;
    }

    public UserLevelThread getUnblockedThread(){
        return unblockedThread;
    }


    public UserLevelThread getRunningThread() {
        return runningThread;
    }

    public void addReady(UserLevelThread ult) {
        if (!readyThreads.contains(ult)) {
            readyThreads.add(ult);
        }
    }

    public List<UserLevelThread> getReadyThreads() {
        return new ArrayList<>(readyThreads);
    }

    public List<UserLevelThread> getThreads (){
        return threads;
    }

    public boolean isFinished() {
        for (UserLevelThread ult : threads) {
            if (ult.getState() != ThreadState.FINISHED) {
                return false;
            }
        }
        return true;
    }

    public Thread getBlocked() {
        return blockedThread;
    }
}
