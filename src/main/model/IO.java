package main.model;

import main.model.thread.KernelLevelThread;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 */
public class IO {

    private final int ID;
    private Queue<KernelLevelThread> blockedKlts;
    private KernelLevelThread currentKlt;
    private KernelLevelThread readyKlt;

    public IO (int ID){
        this.ID = ID;
        blockedKlts = new LinkedList<>();
    }

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

    /**
     *
     * @return el KLT si se desbloqueó
     */
    public KernelLevelThread getReady() {
        return  readyKlt;
    }

    public KernelLevelThread getCurrentKlt() {
        return currentKlt;
    }

    public void add(KernelLevelThread klt) {
        blockedKlts.add(klt);
    }

    public int getID() {
        return ID;
    }

    public List<KernelLevelThread> getBlockedThreads() {
        List<KernelLevelThread> klts = new ArrayList<>(blockedKlts);
        if (currentKlt != null) {
            klts.add(currentKlt);
        }
        return klts;
    }
}
