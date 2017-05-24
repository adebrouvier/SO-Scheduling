package main.model.thread;

import java.util.List;
import java.util.Queue;


public class RR implements Algorithm {
    private int quantum;
    private int currentQuantum;

    public RR(int quantum){
        this.quantum = quantum;
        this.currentQuantum = 0;
    }

    @Override
    public UserLevelThread execute(List<UserLevelThread> ults, UserLevelThread runningUlt) {
        Queue<UserLevelThread> queue = (Queue<UserLevelThread>) ults;
        if(runningUlt == null){
            if(!queue.isEmpty()){
                runningUlt = queue.poll();
            }

        }
            if(runningUlt.execute()){
                if(runningUlt.getState() == ThreadState.BLOCKED){
                    //Settear el ult como el bloqueado. Pensar como lo vamos a implementar
                }else if(runningUlt.getState() == ThreadState.FINISHED){
                    //Ponerlo en una lista de terminados?
                }
                currentQuantum = 0;
                return null;
            }
            currentQuantum++;
            if(currentQuantum == quantum){
                currentQuantum=0;
                queue.add(runningUlt);
                runningUlt = null;
            }
        return runningUlt;
    }
}
