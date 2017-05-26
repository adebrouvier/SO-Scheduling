package main.model.thread;

import java.util.LinkedList;
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
    public TNode execute(Queue<UserLevelThread> queue, UserLevelThread runningUlt, int core) {
        TNode returnNode;
        if(runningUlt == null){
            if(!queue.isEmpty()){
                runningUlt = queue.poll();
            }

        }
            if(runningUlt.execute(core * (-1))) {
                if (runningUlt.getState() == ThreadState.BLOCKED) {
                    returnNode = new TNode(null, runningUlt);
                } else{
                    returnNode = new TNode(null, null);
                }
                currentQuantum = 0;
                return returnNode;
            }
            currentQuantum++;
            if(currentQuantum == quantum){
                currentQuantum=0;
                queue.add(runningUlt);
                returnNode = new TNode(null, null);
            }
        returnNode = new TNode(runningUlt, null);
        return returnNode;
    }
}
