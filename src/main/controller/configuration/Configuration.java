package main.controller.configuration;

import main.model.process.Process;

import java.util.List;
import java.util.Map;

public class Configuration {

    private Map<Integer,List<Process>> processList;
    private Integer cores;
    private String threadLibrary;
    private String processScheduling;
    private Integer schedulingQuantum;
    private Integer threadQuantum;
    private Integer IOCount;

    public Configuration(Map<Integer,List<Process>> processList, Integer cores,
                         String threadLibrary, String processScheduling, int ioCount, int schedulingQuantum, int threadQuantum) {
        this.processList = processList;
        this.cores = cores;
        this.threadLibrary = threadLibrary;
        this.processScheduling = processScheduling;
        this.IOCount = ioCount;
        this.schedulingQuantum = schedulingQuantum;
        this.threadQuantum = threadQuantum;
    }

    public Integer getCores() {
        return cores;
    }

    public void setCores(Integer cores) {
        this.cores = cores;
    }

    public String getThreadLibrary() {
        return threadLibrary;
    }

    public String getScheduling() {
        return processScheduling;
    }

    public Integer getIOCount() {
        return IOCount;
    }

    public int getSchedulingQuantum() {
        return schedulingQuantum;
    }

    public Map<Integer,List<Process>> getProcesses() {
        return processList;
    }

    public void setThreadLibrary(String threadLibrary) {
            this.threadLibrary = threadLibrary;
    }

    public Integer getThreadQuantum() {
        return threadQuantum;
    }

}
