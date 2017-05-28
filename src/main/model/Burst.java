package main.model;

/**
 * Represents a burst of execution of a {@link main.model.thread.Thread}
 * Burst are divided into two types: CPU and IO, and each thread has a list of them.
 */
public class Burst {

    /** 0 = CPU; n = I/On */
    private int type;
    private int time;
    private int remainingTime;

    public Burst(int type, int time){
        this.type = type;
        this.time = time;
        this.remainingTime = time;
    }

    /**
     * @return true if this burst is finished
     */
    public boolean execute() {

        if (remainingTime > 0)
            remainingTime--;

        return remainingTime == 0;
    }

    public int getType(){
        return this.type;
    }

    public int getTime(){
        return time;
    }

    public int getRemainingTime() {
        return remainingTime;
    }

}