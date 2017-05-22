package main;

import java.util.List;

public class KernelLevelThread extends Thread{

    private List<UserLevelThread> ults;
    private UserLevelThread blockedUlt;
    private int parent;

    public KernelLevelThread( List<Burst> burstList, int parent) {
        super(burstList);
        this.parent = parent;
    }

    public int getParentID(){
        return parent;
    }

    public boolean execute() {
        // si tiene ults, usar la biblioteca
        // sino, usar super.execute();
        return super.execute();
    }
}
