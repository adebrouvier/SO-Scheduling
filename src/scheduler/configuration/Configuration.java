package scheduler.configuration;

import scheduler.Process;

public class Configuration {

    private Process[] processList;
    private String[] burstList;

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

    public Configuration(Process[] processList, String[] burstList) {

        this.processList = processList;
        this.burstList = burstList;
    }

}
