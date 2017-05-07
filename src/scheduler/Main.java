package scheduler;

public class Main {

    public static void main(String[] args) {

        ConfigurationLoader cl = new ConfigurationLoader(args);

        Thread t1 = new Thread("ult",new Integer[] {1,2,3});

        Thread t2 = new Thread("ult", new Integer[] {3,2,3});

        Process p1 = new Process(0,new Thread[] {t1,t2});

        Scheduler scheduler  = new Scheduler(new Process[]{p1},2,3,"fifo","fifo");

        scheduler.schedule();
    }

}
