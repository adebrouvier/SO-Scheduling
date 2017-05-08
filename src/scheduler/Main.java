package scheduler;

public class Main {

    public static void main(String[] args) {

        ConfigurationLoader cl = new ConfigurationLoader(args);

        Configuration cfg = cl.load();

        Scheduler scheduler  = new Scheduler(cfg.getProcessList(),cfg.getBurstList(),2,
                "fifo","fifo");

        scheduler.schedule();
    }

}
