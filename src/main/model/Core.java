package main.model;

import main.model.thread.KernelLevelThread;

/**
 * A core serves as a container for a running {@link KernelLevelThread}
 * Two different cores can run two different KLTs of the same {@link main.model.process.Process}.
 */
public class Core {

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

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (!obj.getClass().equals(this.getClass())) {
            return false;
        }

        Core core = (Core)obj;

        return core.ID == this.ID;
    }

    @Override
    public int hashCode() {
        return ID;
    }

    @Override
    public String toString() {
        return "CPU" + ID;
    }
}
