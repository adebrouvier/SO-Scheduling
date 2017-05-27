package main.model.thread;

import java.util.Queue;

public class HRRN implements Algorithm {
    //No nos faltaria agregar una variable de tiempo de espera?
    @Override
    public UserLevelThread execute(Queue<UserLevelThread> ults, UserLevelThread runningUlt, int core) {


        if(runningUlt == null) {
            UserLevelThread highest = ults.peek();
            for (UserLevelThread thread : ults) {
                if (thread.getCurrentBurst().getPriority() > highest.getCurrentBurst().getPriority()) {//See getPriority()
                    highest = thread;
                }
                ults.remove(highest);
                runningUlt = highest;
            }
        }
        if(runningUlt != null){
            runningUlt.setState(ThreadState.RUNNING);
            runningUlt.execute(core * (-1)); // el ult cambia su estado interno
        }

        return runningUlt;


    }
}
