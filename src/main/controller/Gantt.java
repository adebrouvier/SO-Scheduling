package main.controller;

import main.model.process.Scheduler;

import java.util.HashMap;
import java.util.Map;

public class Gantt {

    private class TraceNode {

    }

    //aca guardo todo por cada instante de tiempo
    private Map<Integer, TraceNode> trace;

    public Gantt() {
        trace = new HashMap<>();
    }


    public void print(int time, Scheduler scheduler) {

        // blah blah, creo un TraceNode a partir de lo que tiene el scheduler en ese instante
        TraceNode node = new TraceNode();

        trace.put(time, node);
        print(node);
    }

    public void print(int time) {
        print(trace.get(time));
    }

    private void print(TraceNode node) {

        if (node == null) {
            //GG
        }

        //aca imprimo el nodo
    }


}
