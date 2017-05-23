package main.model;

import main.model.thread.KernelLevelThread;

import java.util.LinkedList;
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

        if (!isBusy()) {
            currentKlt = blockedKlts.poll();
        }

        if (isBusy()) {
            if (currentKlt.execute()) {
                // los threads siempre terminan con burst de cpu entonces
                // no es necesario checkear si el thread esta terminado
                //TODO aca hay que actualizar las listas de ready en process
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
        return readyKlt;
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
}
