package main.model;

import main.model.process.Process;

import java.util.LinkedList;
import java.util.Queue;

/**
 */
public class IO {

    private Queue<Process> processQueue;
    private Integer ID;

    private Process currentProcess;

    public IO(Integer ID) {
        this.ID = ID;
        processQueue = new LinkedList<>();
    }

    public Process poll() {
        return processQueue.poll();
    }

    public void add(Process process) {
        processQueue.add(process);
    }

    public Integer getID() {
        return ID;
    }

    public Process getCurrentProcess() {
        return currentProcess;
    }

    public void setCurrentProcess() {
        currentProcess = poll();
    }

    public boolean isBusy() {
        return currentProcess != null;
    }

}
