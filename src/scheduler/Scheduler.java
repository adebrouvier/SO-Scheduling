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
    private String planification;

    public Scheduler(Process[] processes, String[] bursts, Integer cores, ThreadLibrary threadLib, String planification) {
        this.processes = processes;
        this.bursts = bursts;
        this.cores = cores;
        this.threadLib = threadLib;
        this.planification = planification;
    }


    public void run() {

        for ( int t = 0 ; t < Integer.MAX_VALUE ; t++ ){
            //schedule
        }

    }
}
