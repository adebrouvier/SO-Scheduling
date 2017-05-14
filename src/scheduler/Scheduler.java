package scheduler;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.TimeUnit;

/**
 * Clase principal
 * TODO ordenar los procesos por arrival time
 * TODO checkear en el parseo que los bursts esten intercalados y que termine siempre con burst de cpu
 */
public abstract class Scheduler {

    //Procesos a ejecutarse
    protected Process[] processes;
    protected Integer next = 0;

    //Ráfagas de ejecución
    protected Burst[] bursts;

    //Procesadores
    protected Integer coreAmount;

    //Librería a utilizarse en los hilos ULT
    protected String threadLib;

    protected Queue<Process> readyQueue;

    protected Queue<Process> blockedQueue;

    protected Core[] cores;

    // Dispositivos IO
    protected List<IO> IODevices;


    protected int time = 0;

    public Scheduler(Process[] processes, Burst[] bursts, Integer coreAmount, String threadLib, Integer ioCount) {
        this.processes = processes;
        this.bursts = bursts;
        this.coreAmount = coreAmount;
        this.threadLib = threadLib;
        this.cores = new Core[coreAmount];

        createCores();
        IODevices = new ArrayList<>();

        for (int i = 0; i < ioCount; i++) {
            IODevices.add(new IO(i));
        }
    }

    public void run() {

        while (true) {
            //checkear io

            for (IO io : IODevices) {
                if (!io.isBusy()) {
                    io.setCurrentProcess();
                }

                if (io.isBusy()) {
                    if (io.getCurrentProcess().decreaseTime()) { // los procesos siempre terminan con burst de cpu entonces no es necesario checkear
                        readyQueue.add(io.getCurrentProcess());
                        io.poll();
                    }
                }
            }

            //arrival time

           // TODO processes.get(time);

            while (processes[next].getArrivalTime() == time) {
                //readyQueue.addAll(processes[next].getThreads()); //TODO FIX
                next++;
            }

            executeAlgorithm();

            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public abstract void executeAlgorithm();

    private void createCores(){
        for (int i = 0 ; i < coreAmount ; i++){
            this.cores[i]= new Core(i);
        }
    }

//    private void checkParse(){
//
//        for (int i = 0 ; i< processes.length ; i++){
//            System.out.println(processes[i].getArrivalTime());
//            KernelLevelThread[] threads = processes[i].getThreads();
//
//            for (int j = 0 ; j <threads.length ; j++){
//                /*Integer[] processingTime = threads[j].getBursts();
//
//                for (Integer time : processingTime){
//                    System.out.println(time);
//                }*/
//
//            }
//        }
//
//    }

}
