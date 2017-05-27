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

    public boolean execute() {

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

    public int getPriority(){ return 0; } //1+(waitingtime/estimatedRuntime) Lo que dice la wiki de HRRN
}