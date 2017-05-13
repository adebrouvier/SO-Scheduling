package scheduler;

public class Core {

    private boolean running = false;

    private Process currentProcess;

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

}
