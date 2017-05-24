package main.model.thread;

import java.util.List;
import java.util.Queue;

public class FIFO implements Algorithm{

    @Override
    public UserLevelThread execute(List<UserLevelThread> ults, UserLevelThread runningUlt) {
        //Que esto funcione, depende de como pongamos los ults en la lista. Ver como laburamos con arrival time y "desbloquear" threads
        Queue<UserLevelThread> queue = (Queue<UserLevelThread>) ults; //No se si esta truchada funciona.
        if(runningUlt == null) {
            if (!queue.isEmpty()) {
                runningUlt = queue.poll();
            }
            //Esto se repite bastante, se podria hacer una funcion.
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
