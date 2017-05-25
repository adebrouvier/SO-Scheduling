package main.model.thread;

import java.util.List;
import java.util.Queue;

public class KernelLevelThread extends Thread {

    private List<UserLevelThread> threads;

    private Queue<UserLevelThread> readyThreads;
    private UserLevelThread runningThread;
    private UserLevelThread blockedThread;  // solo puede haber un solo ULT bloqueado
    private ThreadLibraryType threadLibraryType;
    private Algorithm algortimo;
    private TNode tempNode;

    public KernelLevelThread(int parentPID, List<UserLevelThread> threads, ThreadLibraryType threadLibraryType) {
        super(parentPID, null); // le paso null porque no existen los KLT puros
        this.threads = threads;
        this.threadLibraryType = threadLibraryType;
        this.algortimo = new FIFO(); //Hacer un switch
    }

    public boolean execute() {
        if(blockedThread == null){
            tempNode = algortimo.execute(readyThreads, runningThread);
            if(tempNode.getRunning() == null){
                runningThread = null;
                if(tempNode.getBlocked() == null){ //FINISHED
                    //Logica de cuando un ult termina
                    if (isFinished()) {
                        setState(ThreadState.FINISHED);
                    } else {
                        setState(ThreadState.READY);
                    }
                }else{ //BLOCKED
                    blockedThread = tempNode.getBlocked();
                    setState(ThreadState.BLOCKED);
                }
                return true;
            }else{
                runningThread = tempNode.getRunning();
                setState(ThreadState.RUNNING);
                return false;
            }
        }else{
            if(blockedThread.execute()){
                setState(ThreadState.READY);
                readyThreads.add(blockedThread);
                blockedThread = null;
                return true;
            }else{
                return false;
            }
        }
    }

    public UserLevelThread getRunningThread() {
        return runningThread;
    }

    public Queue<UserLevelThread> getReadyThreads() {
        return readyThreads;
    }

    public boolean isFinished() {
        for (UserLevelThread ult : threads) {
            if (ult.getState() != ThreadState.FINISHED) {
                return false;
            }
        }
        return true;
    }
}
