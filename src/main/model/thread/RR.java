package main.model.thread;

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
        }

        if (runningUlt != null) {
            if (!isQuantumOver()) {
                runningUlt.setState(ThreadState.RUNNING);
                runningUlt.execute(core * (-1));
                currentQuantum++;
                if (runningUlt.getState() == ThreadState.BLOCKED) {
                    currentQuantum = 0;
                }
            }
            else {
                currentQuantum = 0;
                if (runningUlt.getState() == ThreadState.RUNNING) { // no termino de correr, pero se le termino el quantum
                    runningUlt.setState(ThreadState.READY);
                    queue.add(runningUlt);
                }
                runningUlt = queue.poll();
                if (runningUlt != null) {
                    runningUlt.execute(core * (-1));
                }
            }
        }

        return runningUlt;
    }

    public boolean isQuantumOver() {
        return currentQuantum == quantum;
    }
}
