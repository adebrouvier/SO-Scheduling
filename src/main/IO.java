package main;

import java.util.LinkedList;
import java.util.Queue;

public class IO {

    private int id;
    private Queue<KernelLevelThread> blockedKlts;
    private KernelLevelThread currentKlt;
    private KernelLevelThread readyKlt; //TODO agregar próximo instante

    public IO (int id){
        this.id=id;
        blockedKlts = new LinkedList<>();
    }

    public boolean isBusy(){
        return currentKlt != null;
    }

    private void setCurrent() {
        currentKlt = blockedKlts.poll();
    }

    public void execute() {

        if (!isBusy()) {
            setCurrent();
        }

        if (isBusy()) {
            if (currentKlt.execute()) { // los procesos siempre terminan con burst de cpu entonces no es necesario checkear
                //TODO aca hay que actualizar las listas de ready en process
                readyKlt = currentKlt;
                currentKlt = null;
            }
        }

    }

    /**
     *
     * @return el KLT si se desbloqueó
     */
    public KernelLevelThread getReady() {
        return readyKlt;
    }

    public void add(KernelLevelThread klt) {
        blockedKlts.add(klt);
    }
}
