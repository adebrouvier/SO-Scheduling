package main.controller.configuration;

import main.model.process.Process;

import java.util.List;
import java.util.Map;

public class Configuration {

    private Process[] processList;
    private String[] burstList;
    private Integer cores;
    private String threadLibrary;
    private String processScheduling;
    private int ioCount;
    private int quantum;

    public Configuration(Process[] processList, String[] burstList, Integer cores, String threadLibrary, String processScheduling) {
        this.processList = processList;
        this.burstList = burstList;
        this.cores = cores;
        this.threadLibrary = threadLibrary;
        this.processScheduling = processScheduling;
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

    public String getThreadLibrary() {
        return threadLibrary;
    }

    public String getScheduling() {
        return processScheduling;
    }

    public int getIOCount() {
        return ioCount;
    }

    public int getQuantum() {
        return quantum;
    }

    // TODO crear mapa en vez de lista
    public Map<Integer,List<Process>> getProcesses() {
        return null;
    }
}
