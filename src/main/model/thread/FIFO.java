package main.model.thread;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class FIFO implements Algorithm{

    public FIFO(){
    }

    @Override
    public TNode execute(Queue<UserLevelThread> queue, UserLevelThread runningUlt) {
        //Que esto funcione, depende de como pongamos los ults en la lista. Ver como laburamos con arrival time y "desbloquear" threads
        TNode returnNode;
        if(runningUlt == null) {
            if (!queue.isEmpty()) {
                runningUlt = queue.poll();
            }
        }
            //Esto se repite bastante, se podria hacer una funcion.
            if(runningUlt.execute()){
                if(runningUlt.getState() == ThreadState.BLOCKED){
                    returnNode = new TNode(null, runningUlt);
                }else{
                    returnNode = new TNode(null, null);
                }
                return returnNode;
            }
        returnNode = new TNode(runningUlt, null);
        return returnNode;
    }
}
