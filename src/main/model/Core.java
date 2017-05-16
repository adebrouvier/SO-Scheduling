package main.model;

import main.model.process.Process;

public class Core {

    private Process currentProcess;

    private Integer ID;

    public Core(Integer id) {
        this.ID = id;
    }

    public boolean isRunning() {
        return currentProcess != null;
    }

    public Process getCurrentProcess() {
        return currentProcess;
    }

    public void setCurrentProcess(Process currentThread) {
        this.currentProcess = currentThread;
    }

    public Integer getID() {
        return ID;
    }

    public void free() {
        setCurrentProcess(null);
    }
}
