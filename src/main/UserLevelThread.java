package main;

import java.util.List;

public class UserLevelThread extends Thread{

    private int parent;

    public UserLevelThread(List<Burst> burstList, int parent) {
        super(burstList);
        this.parent = parent;
    }
}
