package main.model.thread.library;

import main.model.thread.ThreadState;
import main.model.thread.UserLevelThread;

import java.util.Queue;

public class RR implements Algorithm {
    private final int quantum;
    private int currentQuantum;

    public RR(int quantum){
        this.quantum = quantum;
        this.currentQuantum = 0;
    }

    @Override
    public UserLevelThread execute(Queue<UserLevelThread> queue, UserLevelThread runningUlt, int core) {

        if (runningUlt == null) {
            runningUlt = queue.poll();
            if (runningUlt == null) {
                return null;
            }
        }

        runningUlt.setState(ThreadState.RUNNING);
        runningUlt.execute(core * (-1));
        currentQuantum++;

        if (currentQuantum < quantum) {
            if (runningUlt.getState() != ThreadState.RUNNING) { // se bloqueo o termino el ult
                currentQuantum = 0;
            }
        } else {
            currentQuantum = 0;
            if (runningUlt.getState() == ThreadState.RUNNING) {
                runningUlt.setState(ThreadState.READY);
                if (!queue.contains(runningUlt))
                    queue.add(runningUlt);
            }
        }

        return runningUlt;
    }

    public boolean isQuantumOver() {
        return currentQuantum == quantum;
    }
}
