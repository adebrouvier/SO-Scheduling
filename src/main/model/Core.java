package main.model;

import main.model.thread.KernelLevelThread;

public class Core{

    private int id;
    private KernelLevelThread currentKLT; // puedo correr dos klt del mismo proceso en distintos cores

    public Core (int id){
        this.id = id;
    }

    public boolean isRunning(){
        return currentKLT != null;
    }

    public void setCurrentKLT(KernelLevelThread currentKLT) {
        this.currentKLT = currentKLT;
    }

    public KernelLevelThread getCurrentKLT() {
        return currentKLT;
    }
}
