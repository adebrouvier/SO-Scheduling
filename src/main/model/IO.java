package main.model;

import main.model.thread.KernelLevelThread;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * Represents an IO device. Has a queue of {@link KernelLevelThread} and uses FIFO scheduling
 * to determine which KLT to execute.
 */
public class IO {

    private final int ID;
    private Queue<KernelLevelThread> blockedKlts;
    /** Current KLT using this device */
    private KernelLevelThread currentKlt;
    /** KLT that finished using this device in the last instant */
    private KernelLevelThread readyKlt;

    public IO (int ID){
        this.ID = ID;
        blockedKlts = new LinkedList<>();
    }

    /**
     * Executes an instant of IO for the current KLT
     */
    public void execute() {
        readyKlt = null;

        if (!isBusy()) {
            currentKlt = blockedKlts.poll();
        }

        if (isBusy()) {
            if (currentKlt.executeIO(ID)) {
                // los threads siempre terminan con burst de cpu entonces
                // no es necesario checkear si el thread esta terminado
                readyKlt = currentKlt;
                currentKlt = null;
            }
        }
    }

    public boolean isBusy(){
        return currentKlt != null;
    }

    public KernelLevelThread getReady() {
        return  readyKlt;
    }

    public void add(KernelLevelThread klt) {
        blockedKlts.add(klt);
    }

    public List<KernelLevelThread> getBlockedThreads() {
        List<KernelLevelThread> klts = new ArrayList<>(blockedKlts);
        if (currentKlt != null) {
            klts.add(currentKlt);
        }
        return klts;
    }

    public KernelLevelThread getCurrentKlt() {
        return currentKlt;
    }

    public int getID() {
        return ID;
    }
}
