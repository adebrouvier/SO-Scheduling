package main.model.process;

import main.model.Core;
import main.model.IO;
import main.model.thread.KernelLevelThread;
import main.model.thread.ThreadState;
import main.model.thread.UserLevelThread;

import java.util.*;

/**
 * Main class, schedules and executes process.
 * @see Process
 */
public abstract class Scheduler {

    protected Map<Integer,Process> processes;
    protected Queue<Process> readyQueue;
    protected Queue<Process> blockedQueue;
    protected List<Core> cores;
    protected List<IO> IODevices;

    private List<Integer> OSTrace = new ArrayList<>();
    private int freeCores = 0;

    public Scheduler (int coreCount, int ioCount) {
        processes = new HashMap<>();
        readyQueue = new LinkedList<>();
        blockedQueue = new LinkedList<>();
        createCores(coreCount);
        createIODevices(ioCount);
    }

    private void createCores(int coreCount){
        this.cores = new ArrayList<>();

        for (int i = 0 ; i < coreCount ; i++){
            cores.add(new Core(i + 1));
        }
    }

    private void createIODevices(Integer ioCount) {
        IODevices = new ArrayList<>();

        for (int i = 0; i < ioCount; i++) {
            IODevices.add(new IO(i + 1));
        }
    }

    /**
     * Executes an instant of time and updates every process for the next instant
     * @param processes processes that arrived in this instant of time
     * @param threads threads that arrived in this instant of time
     * @param time current time
     */
    public void execute(Collection<Process> processes, Collection<UserLevelThread> threads, int time) {
        updateTrace(threads, time);

        runIO();                    // ejecuto io

        addProcesses(processes);    // agrego los procesos nuevos si los hubiere

        addThreads(threads);        // agrego los threads nuevos si los hubiere

        freeCores = 0;
        for (Core core : cores) {
            executeAlgorithm(core);         // ejecuto el proceso correspondiente segun la planificacion
        }

        for (Process p: this.processes.values()){
            p.update();                 //actualizo los tiempos de espera
        }
    }

    /**
     * Adds a new step to every {@link main.model.thread.Thread}'s trace and the OS.
     * For new threads, also adds a step for each instant passed.
     * @param threads threads that arrived in this instant of time
     * @param time current time
     */
    private void updateTrace(Collection<UserLevelThread> threads, int time) {
        int aux;
        if (threads != null) {
            for (UserLevelThread thread : threads) {
                aux = time;
                while (aux-- > 0)
                    thread.addStep();
            }
        }

        for (Process process : getProcesses()) {
            for (KernelLevelThread klt : process.getThreads()) {
                for (UserLevelThread ult : klt.getThreads()) {
                    if (ult.getState() != ThreadState.NEW) {
                        ult.addStep();
                    }
                }
            }
        }

        OSTrace.add(0);
    }

    /**
     * Executes an instant for each {@link IO} device and returns processes to the ready queue if they were unblocked
     * in the last instant.
     */
    public void runIO() {

        for (IO io : IODevices) {
            // agrego el proceso desbloqueado de nuevo a la ready queue
            KernelLevelThread klt = io.getReady();
            UserLevelThread ult;

            if (klt != null) {
                ult = klt.getUnblockedThread();
                ult.setState(ThreadState.READY);
                klt.setState(ThreadState.READY);
                klt.addReady(ult);

                Process process = processes.get(klt.getParentPID());
                process.setBlockedThread(null);
                process.setState(ProcessState.READY);
                process.addReady(klt);

                if (!readyQueue.contains(process)) {
                    readyQueue.add(process);
                }
            }

            io.execute();
        }
    }

    /**
     * Add processes to the scheduling.
     * @param processes list of process that arrived
     */
    private void addProcesses(Collection<Process> processes) {
        if (processes == null) {
            return;
        }

        for (Process process : processes) {
            process.setState(ProcessState.READY);
            this.processes.put(process.getPID(), process);
        }

        readyQueue.addAll(processes);
    }

    /**
     * Activates threads.
     * @param threads list of threads that arrived
     */
    private void addThreads(Collection<UserLevelThread> threads) {
        if (threads == null) {
            return;
        }

        for (UserLevelThread thread : threads) {
            thread.setState(ThreadState.READY);
            Process parent = processes.get(thread.getParentPID());
            KernelLevelThread klt = parent.getThread(thread.getParentKltID());
            klt.addReady(thread);
            klt.setState(ThreadState.READY);
            parent.addReady(klt);

//            if (!readyQueue.contains(parent) && parent.getState()!=ProcessState.BLOCKED){
//                readyQueue.add(parent);
//            }
        }
    }

    /**
     * Executes the scheduling algorithm for a given {@link Core}
     * @see SchedulerFIFO
     * @see SchedulerRoundRobin
     */
    public abstract void executeAlgorithm(Core core);

    public List<Process> getProcesses() {
        List<Process> list = new ArrayList<>();
        list.addAll(processes.values());
        return list;
    }

    /**
     * Adds a step to the OSTrace (when every core is free)
     */
    protected void addOSStep() {
        freeCores++;
        if (freeCores == cores.size()) {
            OSTrace.set(OSTrace.size() - 1, -1); // corre el os
        }
    }

    public List<Integer> getOSTrace() {
        return new ArrayList<>(OSTrace);
    }

    public List<Core> getCores() {
        return cores;
    }

    public List<IO> getIODevices() {
        return IODevices;
    }

    public Queue<Process> getReadyQueue() {
        return readyQueue;
    }

    public Set<KernelLevelThread> getBlockedThreads() {
        Set<KernelLevelThread> blockedThreads = new HashSet<>();

        for (IO io : IODevices){
            blockedThreads.addAll(io.getBlockedThreads());
        }

        return blockedThreads;
    }
}