package main;

import java.util.*;

public abstract class Scheduler {

    protected Map<Integer,Process> processes;
    protected Queue<Process> readyQueue;
    protected Queue<Process> blockedQueue;
    protected List<Core> cores;
    protected List<IO> IODevices;

    public Scheduler (int coreCount, int ioCount){
        processes = new HashMap<>();
        readyQueue = new LinkedList<>();
        blockedQueue = new LinkedList<>();
        createCores(coreCount);
        createIODevices(ioCount);
    }

    private void createCores(int coreCount){
        this.cores = new ArrayList<>();

        for (int i = 0 ; i < coreCount ; i++){
            cores.add(new Core(i));
        }
    }

    private void createIODevices(Integer ioCount) {
        IODevices = new ArrayList<>();

        for (int i = 0; i < ioCount; i++) {
            IODevices.add(new IO(i));
        }
    }

    /**
     *
     * @param processes new processes created in this instant of time
     */
    public boolean execute(Collection<Process> processes) {
        runIO();                 // ejecuto io

        addProcesses(processes); // agrego los procesos nuevos si los hubiere

        executeAlgorithm();      // ejecuto el proceso correspondiente segun la planificacion

        return false;
    }

    public void runIO() {

        for (IO io : IODevices) {

            Process ready = processes.get(io.getReady().getParentID());

            if (ready != null) {
                readyQueue.add(ready);
            }

            io.execute();
        }
    }

    public abstract void executeAlgorithm();

    public void addProcesses(Collection<Process> processes) {

        for (Process process : processes) {
            process.setState(ProcessState.READY);
            this.processes.put(process.getPid(),process);
        }

        readyQueue.addAll(processes);

    }

}