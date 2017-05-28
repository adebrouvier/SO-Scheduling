package main.model.thread.library;

import main.model.thread.*;

import java.util.Queue;

/**
 * Scheduling algorithm for {@link UserLevelThread}, used by {@link KernelLevelThread}
 */
public interface Algorithm {

    /**
     * Executes the scheduling algorithm and runs the next ready ULT.
     * @param readyQueue queue that contains the ULTs to be executed. This should be modified by the algorithm.
     * @param runningUlt previous running ULT
     * @param core core to execute
     * @return the new running {@link UserLevelThread}
     */
    UserLevelThread execute(Queue<UserLevelThread> readyQueue, UserLevelThread runningUlt, int core);

}
