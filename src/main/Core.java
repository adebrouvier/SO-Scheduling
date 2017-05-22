package main;

public class Core{

    private int id;
    private KernelLevelThread currentKLT; // porque puedo correr dos klt del mismo proceso en distintos cores (PLS NESTOR)

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
