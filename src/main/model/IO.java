package main.model;

import main.model.process.Process;
import main.model.thread.KernelLevelThread;
import main.model.thread.ThreadState;

import java.util.*;

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

    public boolean execute() {
        readyKlt = null;

        if (!isBusy()) {
            currentKlt = blockedKlts.poll();
        }

        if (isBusy()) {
            if (currentKlt.executeIO(ID)) {
                // los threads siempre terminan con burst de cpu entonces
                // no es necesario checkear si el thread esta terminado
                //TODO aca hay que actualizar las listas de ready en process
                readyKlt = currentKlt;
                currentKlt = null;
                return true;
            }
        }
        return false;
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
