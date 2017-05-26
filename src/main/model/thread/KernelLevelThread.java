package main.model.thread;

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
        this.algorithm = new FIFO(); //Hacer un switch
        readyThreads = new LinkedList<>();
    }

    public boolean executeCPU(int core) {
        TNode tempNode;

        if(blockedThread == null) {

            tempNode = algorithm.execute(readyThreads, runningThread, core);

            if(tempNode.getRunning() == null){
                runningThread = null;
                UserLevelThread blocked = tempNode.getBlocked();
                if(blocked == null) { //FINISHED
                    //Logica de cuando un ult termina
                    if (isFinished()) {
                        setState(ThreadState.FINISHED);
                    } else {
                        runningThread = null;
                        setState(ThreadState.READY);
                    }
                }else{ //BLOCKED
                    blockedThread = blocked;
                    setState(ThreadState.BLOCKED);
                }
                return true;
            }else{
                runningThread = tempNode.getRunning();
                setState(ThreadState.RUNNING);
                return false;
            }
        }
        return false; //Ver bien esto
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

        if(blockedThread.execute(device)) {
            setState(ThreadState.READY);
            if (!readyThreads.contains(blockedThread))
                readyThreads.add(blockedThread);
            //unblockedThread = blockedThread;
            blockedThread = null;
            return true;
        } else {
            return false;
        }
    }


    public UserLevelThread getRunningThread() {
        return runningThread;
    }

    public Queue<UserLevelThread> getReadyThreads() {
        return readyThreads;
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
