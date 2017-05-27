package main.model.thread.library;

import main.model.thread.ThreadState;
import main.model.thread.UserLevelThread;

import java.util.Queue;

public class HRRN implements Algorithm {

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
