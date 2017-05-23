package main.controller;

import main.model.process.Scheduler;

import java.util.ArrayList;
import java.util.List;

public class Gantt {

    private class TraceNode {

    }

    private List<TraceNode> trace;

    public Gantt() {
        trace = new ArrayList<>();
    }

    /**
     * Adds a new column to the Gantt diagram
     * @param scheduler
     */
    public void addTraceNode(Scheduler scheduler) {
        // TODO crear un TraceNode a partir de lo que tiene el scheduler en ese instante
        TraceNode node = new TraceNode();
        //
        trace.add(node);
    }

    /**
     * Prints the Gantt diagram at a given time
     * @param time
     */
    public void print(int time) {
        print(trace.get(time));
    }

    private void print(TraceNode node) {

        if (node == null) {
            //GG
        }

        //aca imprimo el nodo
    }


    /** - = CPU
    * n = IOn
    * n e N
    *  Procesos:
    *      P1K1U1 | -| -| -| 1| 1| 1|  | -|  | -|- |  |
    *      P1K2U2 |  |  |  | -| -| 2| 2|  | -|  |  |  |
    *             |  |  |  |  |  |  |  |  |  |  |  |  |
    *      Pm     |  |  |  |  |  | -| -| 3| 3| 3|  |- |
    *      --------0--1--2--3--4--5--6--7--8--9--10-11--------------------------
    *      OS |
    *
    */
}
