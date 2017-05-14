package scheduler;

public class Core {

    private boolean running = false;

    private Process currentProcess;

    private Integer ID;

    public Core(Integer id) {
        this.ID = id;
    }

    public boolean isRunning() {
        return running;
    }

    public void setRunning(boolean running) {
        this.running = running;
    }

    public Process getCurrentProcess() {
        return currentProcess;
    }

    public void setCurrentProcess(Process currentProcess) {
        this.currentProcess = currentProcess;
        if (this.currentProcess != null)
            this.setRunning(true);
    }

    public Integer getID() {
        return ID;
    }

}
