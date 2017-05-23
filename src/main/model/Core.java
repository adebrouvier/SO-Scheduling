package main.model;

import main.model.thread.KernelLevelThread;

public class Core{

    private int ID;
    private KernelLevelThread currentKLT; // puedo correr dos klt del mismo proceso en distintos cores

    public Core (int ID){
        this.ID = ID;
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

    public int getID() {
        return ID;
    }
}
