package main.model.thread;

import java.util.List;

public class SRT implements Algorithm {
    @Override
    public UserLevelThread execute(List<UserLevelThread> ults, UserLevelThread runningUlt) {
        if(runningUlt == null){
            UserLevelThread shortest=ults.get(0);
            for(UserLevelThread thread: ults){
                if(thread.getCurrentBurst().getRemainingTime() < shortest.getCurrentBurst().getRemainingTime()) { //Se podria hacer un compare de Bursts
                    shortest = thread;
                }
                ults.remove(shortest);
                runningUlt = shortest;
            }

            if(runningUlt.execute()){
                if(runningUlt.getState() == ThreadState.BLOCKED){
                    //Settear el ult como el bloqueado. Pensar como lo vamos a implementar
                }else if(runningUlt.getState() == ThreadState.FINISHED){
                    //Ponerlo en una lista de terminados?
                }
                return null;
            }
        }
        return runningUlt;

    }
}

