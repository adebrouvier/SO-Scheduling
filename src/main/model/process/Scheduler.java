package main.model.process;

import main.model.Core;
import main.model.IO;

import java.util.*;

/**
 * Clase principal
 * TODO validar que los procesos tengan arrival time positivo, etc (Cuando se crea el proceso)
 * TODO checkear en el parseo que los bursts esten intercalados y que termine siempre con burst de cpu
 */
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
            // agrego el proceso desbloqueado de nuevo a la ready queue
            Process ready = processes.get(io.getReady().getParentPID());

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
            this.processes.put(process.getPID(),process);
        }

        readyQueue.addAll(processes);

    }

}