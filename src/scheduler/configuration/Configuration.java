package scheduler.configuration;

import scheduler.Process;

public class Configuration {

    private Process[] processList;
    private Integer cores;
    private String threadLibrary;
    private String processPlanification;
    private Integer IOCount;

    public Configuration(Process[] processList, Integer cores, String threadLibrary, String processPlanification, Integer IOCount) {

        this.processList = processList;
        this.cores = cores;
        this.threadLibrary = threadLibrary;
        this.processPlanification = processPlanification;
        this.IOCount = IOCount;
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

    public String getThreadLibrary() {
        return threadLibrary;
    }

    public void setThreadLibrary(String threadLibrary) {
        this.threadLibrary = threadLibrary;
    }

    public String getProcessPlanification() {
        return processPlanification;
    }

    public void setProcessPlanification(String processPlanification) {
        this.processPlanification = processPlanification;
    }

    public Integer getIOCount() {
        return IOCount;
    }

    public void setIOCount(Integer IOCount) {
        this.IOCount = IOCount;
    }
}
