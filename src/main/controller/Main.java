package main.controller;

import main.controller.configuration.Configuration;
import main.controller.configuration.ConfigurationLoader;
import main.model.Burst;
import main.model.process.Process;
import main.model.thread.KernelLevelThread;
import main.model.thread.ThreadLibraryType;
import main.model.thread.UserLevelThread;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main {

    public static void main(String[] args) {
        //ConfigurationLoader cl = new ConfigurationLoader(args);
        //Configuration cfg = cl.load();

        Map<Integer, List<Process>> processMap = new HashMap<>(); // mapa global de procesos
        Map<Integer, List<UserLevelThread>> userLevelThreadMap = new HashMap<>();   // mapa global de ULTS

        List<Process> processes = new ArrayList<>();    // lista de procesos (para agregar al mapa)
        List<UserLevelThread> ultList = new ArrayList<>(); // lista de ults (para agregar al mapa)

        List<KernelLevelThread> klts = new ArrayList<>(); // para crear procesos
        List<UserLevelThread> ults = new ArrayList<>(); // para crear KLTS
        List<Burst> bursts = new ArrayList<>(); // para crear ULTS

        Burst burst;
        UserLevelThread ult;
        KernelLevelThread klt;
        Process process;

        // ULT1
        burst = new Burst(0, 3);
        bursts.add(burst);
        burst = new Burst(1, 2);
        bursts.add(burst);
        burst = new Burst(0, 5);
        bursts.add(burst);

        ult = new UserLevelThread(1, 1, bursts);
        ults.add(ult);
        ultList.add(ult);
        userLevelThreadMap.put(1, ultList);

        // ULT2
        bursts = new ArrayList<>();
        burst = new Burst(0, 4);
        bursts.add(burst);
        burst = new Burst(1, 3);
        bursts.add(burst);
        burst = new Burst(0, 3);
        bursts.add(burst);

        ult = new UserLevelThread(1, 1, bursts);
        ults.add(ult);
        ultList = new ArrayList<>();
        ultList.add(ult);
        userLevelThreadMap.put(2, ultList);

        // KLT1
        klt = new KernelLevelThread(1, ults, ThreadLibraryType.FIFO);
        klts.add(klt);

        //P1
        process = new Process(klts);
        processes.add(process);

        // mapas
        processMap.put(1, processes);




        ///////////////////////////////

        Configuration cfg = new Configuration(processMap, userLevelThreadMap, 1, "FIFO",
                "FIFO", 1, 0, 0);

        Simulation simulation = new Simulation(cfg);
        simulation.start();
    }

}
