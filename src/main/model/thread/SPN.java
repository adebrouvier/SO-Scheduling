package main.model.thread;

import java.util.Queue;


public class SPN implements Algorithm {
    @Override
    public TNode execute(Queue<UserLevelThread> ults, UserLevelThread runningUlt, int core) {
        TNode returnNode;
        if(runningUlt == null) {
            UserLevelThread shortest = ults.peek();
            for (UserLevelThread thread : ults) {
                if (thread.getCurrentBurst().getTime() < shortest.getCurrentBurst().getTime()) { //Se podria hacer un compare de Bursts
                    shortest = thread;
                }
                ults.remove(shortest);
                runningUlt = shortest;
            }
        }
            if(runningUlt.execute(core * (-1))) {
                if (runningUlt.getState() == ThreadState.BLOCKED) {
                    returnNode = new TNode(null, runningUlt);
                } else {
                    returnNode = new TNode(null, null);
                }
                return returnNode;
            }
        returnNode = new TNode(runningUlt, null);
        return returnNode;

    }
}
