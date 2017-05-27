package main.model.thread.library;

import main.model.thread.ThreadState;
import main.model.thread.UserLevelThread;

import java.util.Queue;

public class SRT implements Algorithm {
    @Override
    public UserLevelThread execute(Queue<UserLevelThread> ults, UserLevelThread runningUlt, int core) {

        if(runningUlt == null) {
            UserLevelThread shortest = ults.peek();
            for (UserLevelThread thread : ults) {
                if (thread.getCurrentBurst().getRemainingTime() < shortest.getCurrentBurst().getRemainingTime()) {
                    shortest = thread;
                }
            }
            ults.remove(shortest);
            runningUlt = shortest;
        }
        if(runningUlt != null){
            runningUlt.setState(ThreadState.RUNNING);
            runningUlt.execute(core * (-1)); // el ult cambia su estado interno
        }

        return runningUlt;

    }
}

