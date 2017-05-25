package main.model.thread;

import java.util.Queue;

public class KernelLevelThread extends Thread {

    private Queue<UserLevelThread> readyThreads;
    private UserLevelThread runningThread;
    private UserLevelThread blockedThread;  // solo puede haber un solo ULT bloqueado
    private ThreadLibraryType threadLibraryType;
    private Algorithm algortimo;
    private TNode tempNode;

    public KernelLevelThread(int parentPID, Queue<UserLevelThread> readyThreads, ThreadLibraryType threadLibraryType) {
        super(parentPID, null); // le paso null porque no existen los KLT puros
        this.readyThreads = readyThreads;
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
                }else{ //BLOCKED
                    blockedThread = tempNode.getBlocked();
                }
                return true;
            }else{
                runningThread = tempNode.getRunning();
                return false;
            }
        }else{
            if(blockedThread.execute()){
                blockedThread.setState(ThreadState.READY);
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

}
