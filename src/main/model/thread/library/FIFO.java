package main.model.thread.library;

import main.model.thread.ThreadState;
import main.model.thread.UserLevelThread;

import java.util.Queue;

public class FIFO implements Algorithm{

    public FIFO(){
    }

    @Override
    public UserLevelThread execute(Queue<UserLevelThread> queue, UserLevelThread runningUlt, int core) {

        if(runningUlt == null) {
            runningUlt = queue.poll();
        }
        if (runningUlt != null) {
            runningUlt.setState(ThreadState.RUNNING);
            runningUlt.execute(core * (-1)); // el ult cambia su estado interno
        }

        return runningUlt;
    }
}
