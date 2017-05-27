package main.model.process;

import main.model.Core;
import main.model.IO;
import main.model.thread.KernelLevelThread;
import main.model.thread.Thread;
import main.model.thread.ThreadState;
import main.model.thread.UserLevelThread;

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
     *
     * @param processes new processes created in this instant of time
     * @param threads new threads created in this instant of time
     */
    public boolean execute(Collection<Process> processes, Collection<? extends Thread> threads, int time) {
        resetULTs(threads, time);

        runIO();                    // ejecuto io

        addProcesses(processes);    // agrego los procesos nuevos si los hubiere

        addThreads(threads);        // agrego los threads nuevos si los hubiere

        for (Core core : cores) {
            executeAlgorithm(core);         // ejecuto el proceso correspondiente segun la planificacion
        }

        return false;
    }

    private void resetULTs(Collection<? extends Thread> threads, int time) {
        int aux;
        if (threads != null) {
            for (Thread thread : threads) {
                aux = time;
                while (aux-- > 0)
                    thread.addInstant();
            }
        }

        for (Process process : getProcesses()) {
            for (KernelLevelThread klt : process.getThreads()) {
                for (UserLevelThread ult : klt.getThreads()) {
                    if (ult.getState() != ThreadState.NEW) {
                        ult.addInstant();
                    }
                }
            }
        }

        OSTrace.add(0);
    }

    public void runIO() {

        for (IO io : IODevices) {
            // agrego el proceso desbloqueado de nuevo a la ready queue
            KernelLevelThread klt = io.getReady();
            UserLevelThread ult;

            if (klt != null) {
                ult = klt.getUnblockedThread();
                // no deberia ser null
                ult.setState(ThreadState.READY);
                klt.setState(ThreadState.READY);
                klt.addReady(ult);

                Process process = processes.get(klt.getParentPID());
                process.setState(ProcessState.READY);
                process.addReady(klt);

                if (!readyQueue.contains(process)) {
                    readyQueue.add(process);
                }
            }

            if (io.execute()) { // se termino la burst de io
//                Process process = processes.get(klt.getParentPID());
//                process.setBlockedThread(null);
            }
        }
    }

    public abstract void executeAlgorithm(Core core);

    /**
     * TODO Each process arrival time is the minimum of its respective ULTs arrival times
     * @param processes
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
     * @param threads
     */
    private void addThreads(Collection<? extends Thread> threads) {
        if (threads == null) {
            return;
        }

        for (Thread thread : threads) {
            thread.setState(ThreadState.READY);
            Process parent = processes.get(thread.getParentPID());
            KernelLevelThread klt = parent.getThread(((UserLevelThread)thread).getParentKltID());
            klt.addReady((UserLevelThread) thread);
            parent.addReady(klt);

//            if (parent.getState().equals(ProcessState.SLEEP)) {
//                  readyQueue.add(parent);
//            }
        }
    }

    public List<Process> getProcesses() {
        List<Process> list = new ArrayList<>();
        list.addAll(processes.values());
        return list;
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

    private List<Integer> OSTrace = new ArrayList<>();

    protected void addOSStep() {
        OSTrace.set(OSTrace.size() - 1 , -1); // corre el os
    }

    public List<Integer> getOSTrace() {
        return new ArrayList<>(OSTrace);
    }
}