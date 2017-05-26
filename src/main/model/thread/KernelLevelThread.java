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
    private ThreadLibraryType threadLibraryType;
    private Algorithm algorithm;

    public KernelLevelThread(int parentPID, List<UserLevelThread> threads, ThreadLibraryType threadLibraryType) {
        super(parentPID, null, "KLT"); // le paso null porque no existen los KLT puros
        this.threads = threads;
        this.threadLibraryType = threadLibraryType;
        switch (threadLibraryType) {
            case FIFO:
                algorithm = new FIFO();
                break;
            case RR:
                algorithm = new RR(4); // TODO FIX HARDCODEO
        }
        readyThreads = new LinkedList<>();
    }

    /**
     * @param core
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

//            if(tempNode.getRunning() == null) {
//                UserLevelThread blocked = tempNode.getBlocked();
//                if(blocked == null) { //FINISHED
//                    //Logica de cuando un ult termina
//                    runningThread.setState(ThreadState.FINISHED);
//                    if (isFinished()) {
//                        setState(ThreadState.FINISHED);
//                    } else {
//                        runningThread = null;
//                        setState(ThreadState.READY);
//                    }
//                }else{ //BLOCKED
//                    blockedThread = blocked;
//                    setState(ThreadState.BLOCKED);
//                }
//                runningThread = null;
//                return true;
//            }else{
//                runningThread = tempNode.getRunning();
//                setState(ThreadState.RUNNING);
//                return false;
//            }
//        }
//        return false; //Ver bien esto

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
