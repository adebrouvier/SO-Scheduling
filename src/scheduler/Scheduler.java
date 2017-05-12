package scheduler;

/**
 * Clase principal
 */
public class Scheduler {

    //Procesos a ejecutarse
    private Process[] processes;

    //Ráfagas de ejecución
    private String[] bursts;

    //Procesadores
    private Integer cores;

    //Librería a utilizarse en los hilos ULT
    private ThreadLibrary threadLib;

    //Tipo de planificación para los procesos
    private ProcessPlanification planification;

    public Scheduler(Process[] processes, String[] bursts, Integer cores, ProcessPlanification planification, ThreadLibrary threadLib) {
        this.processes = processes;
        this.bursts = bursts;
        this.cores = cores;
        this.threadLib = threadLib;
        this.planification = planification;
    }


    public void run() {

        checkParse();

        for ( int t = 0 ; t < Integer.MAX_VALUE ; t++ ){
            //schedule
        }

    }

    private void checkParse(){

        for (int i = 0 ; i< processes.length ; i++){
            System.out.println(processes[i].getArrivalTime());
            Thread[] threads = processes[i].getThreads();

            for (int j = 0 ; j <threads.length ; j++){
                System.out.println(threads[j].getType());
                Integer[] processingTime = threads[j].getProcessingTime();

                for (Integer time : processingTime){
                    System.out.println(time);
                }

            }
        }

    }
}
