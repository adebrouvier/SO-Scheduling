package main.model.thread;

import main.model.Burst;

import java.util.ArrayList;
import java.util.List;

public class KernelLevelThread extends Thread{

    private List<UserLevelThread> ults;
    private UserLevelThread blockedUlt;
    private int parent;
    private ThreadLibraryType threadLibraryType;

    public KernelLevelThread( List<Burst> burstList, int parent, ThreadLibraryType threadLibraryType) {
        super(burstList);
        this.parent = parent;
        this.threadLibraryType = threadLibraryType;
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
