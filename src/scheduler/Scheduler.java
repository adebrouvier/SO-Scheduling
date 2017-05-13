package scheduler;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.TimeUnit;

/**
 * Clase principal
 */
public class Scheduler {

    //Procesos a ejecutarse
    private Process[] processes;

    //Ráfagas de ejecución
    private Burst[] bursts;

    //Procesadores
    private Integer coreAmount;

    //Librería a utilizarse en los hilos ULT
    private ThreadLibrary threadLib;

    //Tipo de planificación para los procesos
    private ProcessPlanification planification;

    private Queue<Process> readyQueue;

    private Queue<Process> blockedQueue;

    private Core[] cores;

    private int time = 0;

    public Scheduler(Process[] processes, Burst[] bursts, Integer coreAmount, ProcessPlanification planification, ThreadLibrary threadLib) {
        this.processes = processes;
        this.bursts = bursts;
        this.coreAmount = coreAmount;
        this.threadLib = threadLib;
        this.planification = planification;
        this.readyQueue = new LinkedList<Process>();
        this.blockedQueue = new LinkedList<Process>();
        this.cores = new Core[coreAmount];
        createCores();
    }

    private void createCores(){
        for (int i = 0 ; i < coreAmount ; i++){
            this.cores[i]=new Core();
        }
    }


    public void run() {

        //checkear io

        //arrival time

        while (true){

            for (Core core : cores) {
                if (!core.isRunning()) {
                    System.out.println("hola");
                }
            }

            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void checkParse(){

        for (int i = 0 ; i< processes.length ; i++){
            System.out.println(processes[i].getArrivalTime());
            KernelLevelThread[] threads = processes[i].getThreads();

            for (int j = 0 ; j <threads.length ; j++){
                /*Integer[] processingTime = threads[j].getProcessingTime();

                for (Integer time : processingTime){
                    System.out.println(time);
                }*/

            }
        }

    }
}
