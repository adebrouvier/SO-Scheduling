package main.model;

public class Burst {

    private int type;
    private int time;
    private int remainingTime;

    public Burst(int type, int time){
        this.type = type;
        this.time = time;
        this.remainingTime = time;
    }

    public boolean execute(int instant) {
        //System.out.println(instant);

        if (remainingTime > 0)
            remainingTime--;

        return remainingTime == 0;
    }

    public int getType (){
        return this.type;
    }

    public int getTime (){
        return time;
    }

    public int getRemainingTime() {
        return remainingTime;
    }
}