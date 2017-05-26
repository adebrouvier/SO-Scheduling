package main.model.thread;

import java.util.Queue;

public class FIFO implements Algorithm{

    public FIFO(){
    }

    @Override
    public UserLevelThread execute(Queue<UserLevelThread> queue, UserLevelThread runningUlt, int core) {
        //Que esto funcione, depende de como pongamos los ults en la lista. Ver como laburamos con arrival time y "desbloquear" threads
       // TNode returnNode;

        if(runningUlt == null) {
            runningUlt = queue.poll();
        }
        if (runningUlt != null) {
            runningUlt.setState(ThreadState.RUNNING);
            runningUlt.execute(core * (-1)); // el ult cambia su estado interno
        }

        return runningUlt;
//        if (runningUlt != null) {
//            //Esto se repite bastante, se podria hacer una funcion.
//            if (runningUlt.execute(core * (-1))) {
//                if (runningUlt.getState() == ThreadState.BLOCKED) {
//                    returnNode = new TNode(null, runningUlt);
//                } else {
//                    returnNode = new TNode(null, null);
//                }
//                return returnNode;
//            }
//        }
//        returnNode = new TNode(runningUlt, null);
//        return returnNode;
    }
}
