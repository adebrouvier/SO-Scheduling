package main.model.process;

import main.model.Core;
import main.model.IO;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Queue;

/**
 * Clase principal
 * TODO validar que los procesos tengan arrival time positivo, etc (Cuando se crea el proceso)
 * TODO checkear en el parseo que los bursts esten intercalados y que termine siempre con burst de cpu
 */
public abstract class Scheduler extends Process {

    //Procesadores y IO
    protected Integer coreAmount;
    protected Integer IOCount;

    //Librería a utilizarse en los hilos ULT
    protected String threadLib;

    protected Queue<Process> readyQueue;
    protected Queue<Process> blockedQueue;

    protected Core[] cores;

    // Dispositivos IO
    protected List<IO> IODevices;

    public Scheduler(Integer coreAmount, String threadLib, Integer ioCount) {
        super(null);
        this.coreAmount = coreAmount;
        this.threadLib = threadLib;

        createCores();
        createIODevices(ioCount);
    }

    private void createCores(){
        this.cores = new Core[coreAmount];

        for (int i = 0 ; i < coreAmount ; i++){
            this.cores[i]= new Core(i);
        }
    }

    private void createIODevices(Integer ioCount) {
        IODevices = new ArrayList<>();

        for (int i = 0; i < ioCount; i++) {
            IODevices.add(new IO(i));
        }
    }

    /**
     * @see Process#execute()
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
            if (!io.isBusy()) {
                io.setCurrentProcess();
            }

            if (io.isBusy()) {
                if (io.getCurrentProcess().execute()) { // los procesos siempre terminan con burst de cpu entonces no es necesario checkear
                    readyQueue.add(io.getCurrentProcess());
                    io.poll();
                }
            }
        }
    }

    public abstract void executeAlgorithm();

    public void addProcesses(Collection<Process> processes) {
        for (Process process : processes) {
            process.setState(ProcessState.READY);
        }

        readyQueue.addAll(processes);
    }

}
