package main.controller.configuration;

import main.model.process.Process;
import main.model.thread.UserLevelThread;

import java.util.List;
import java.util.Map;

public class Configuration {

    private Map<Integer,List<Process>> processList;
    private Map<Integer, List<UserLevelThread>> threads;
    private int cores;
    private int IOCount;
    private String processScheduling;
    private String threadLibrary;
    private Integer schedulingQuantum;
    private Integer threadQuantum;

    public Configuration(Map<Integer,List<Process>> processList, Map<Integer, List<UserLevelThread>> threads, Integer cores,
                         String threadLibrary, String processScheduling, int ioCount, int schedulingQuantum, int threadQuantum) {
        this.processList = processList;
        this.threads = threads;
        this.cores = cores;
        this.threadLibrary = threadLibrary;
        this.processScheduling = processScheduling;
        this.IOCount = ioCount;
        this.schedulingQuantum = schedulingQuantum;
        this.threadQuantum = threadQuantum;
    }

    public int getCores() {
        return cores;
    }

   public String getThreadLibrary() {
        return threadLibrary;
    }

    public String getScheduling() {
        return processScheduling;
    }

    public int getIOCount() {
        return IOCount;
    }

    public int getSchedulingQuantum() {
        return schedulingQuantum;
    }

    public Map<Integer,List<Process>> getProcesses() {
        return processList;
    }

    public int  getThreadQuantum() {
        return threadQuantum;
    }

    public Map<Integer, List<UserLevelThread>> getThreads() {
        return threads;
    }
}
