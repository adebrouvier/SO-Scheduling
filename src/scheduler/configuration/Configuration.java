package scheduler.configuration;

import scheduler.Process;
import scheduler.ProcessPlanification;
import scheduler.ThreadLibrary;

public class Configuration {

    private Process[] processList;
    private String[] burstList;
    private Integer cores;
    private ThreadLibrary threadLibrary;
    private ProcessPlanification processPlanification;

    public Configuration(Process[] processList, String[] burstList, Integer cores, ThreadLibrary threadLibrary, ProcessPlanification processPlanification) {

        this.processList = processList;
        this.burstList = burstList;
        this.cores = cores;
        this.threadLibrary = threadLibrary;
        this.processPlanification = processPlanification;
    }

    public Integer getCores() {
        return cores;
    }

    public void setCores(Integer cores) {
        this.cores = cores;
    }

    public Process[] getProcessList() {
        return processList;
    }

    public void setProcessList(Process[] processList) {
        this.processList = processList;
    }

    public String[] getBurstList() {
        return burstList;
    }

    public void setBurstList(String[] burstList) {
        this.burstList = burstList;
    }

    public ThreadLibrary getThreadLibrary() {
        return threadLibrary;
    }

    public void setThreadLibrary(ThreadLibrary threadLibrary) {
        this.threadLibrary = threadLibrary;
    }

    public ProcessPlanification getProcessPlanification() {
        return processPlanification;
    }

    public void setProcessPlanification(ProcessPlanification processPlanification) {
        this.processPlanification = processPlanification;
    }

}
