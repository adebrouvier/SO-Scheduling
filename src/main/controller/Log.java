package main.controller;

import main.controller.configuration.Configuration;
import main.model.process.ProcessState;
import main.model.thread.ThreadState;

import java.io.*;

/**
 */
public final class Log {

    private static Log instance = new Log();
    private String logFilePath = "./res/log.so";

    private Log() {
        writeFile(logFilePath, "");
    }

    public static Log getInstance() {
        return instance;
    }

    public static void addConfiguration(Configuration cfg) {
        int quantum = 0;
        String scheduler = cfg.getScheduling();
        quantum = cfg.getSchedulingQuantum();

        getInstance().writeToFile(instance.logFilePath, "Scheduler: " + scheduler + (quantum != 0 ? ", quantum:" + quantum: ""));

        scheduler = cfg.getThreadLibrary();

        quantum = cfg.getThreadQuantum();

        getInstance().writeToFile(instance.logFilePath, "Thread Library: " + scheduler + (quantum != 0 ? ", quantum:" + quantum: ""));

        getInstance().writeToFile(instance.logFilePath, "Cores: " +  cfg.getCores() + ", IO Devices: " + cfg.getIOCount());
    }

    public static void addTimeStamp(int time) {
        getInstance().writeToFile(instance.logFilePath, "\nTime: " + time);
    }

    public static void addProcessStateChange(int PID, ProcessState state) {
        getInstance().writeToFile(instance.logFilePath, "Process: " + PID + ", State: " + state);
    }

    public static void addKLTStateChange(int TID, ThreadState state) {
        getInstance().writeToFile(instance.logFilePath, "KLT: " + TID + ", State: " + state);
    }

    public static void addULTStateChange(int TID, ThreadState state) {
        getInstance().writeToFile(instance.logFilePath, "ULT: " + TID + ", State: " + state);
    }

    public static void addIOBlockedKLT(int IODeviceID, int TID) {
        getInstance().writeToFile(instance.logFilePath, "IO: " + IODeviceID + ", KLT: " + TID + " blocked");
    }

    public static void addIOUnblockedKLT(int IODeviceID, int TID) {
        getInstance().writeToFile(instance.logFilePath, "IO: " + IODeviceID + ", KLT: " + TID + " unblocked");
    }


    private String readFile(String path) throws IOException {
        StringBuilder sb = new StringBuilder();
        String ls = System.lineSeparator();

        BufferedReader br = new BufferedReader(new FileReader(new File(path)));
        String line = br.readLine();

        while (line != null) {
            sb.append(line);
            sb.append(ls);
            line = br.readLine();
        }

        br.close();

        return sb.toString();
    }

    /**
     * Appends line to the file
     */
    private void writeToFile(String path, String line) {
        if (path == null) {
            throw new IllegalArgumentException("File path cannot be null");
        }
        if (line == null) {
            throw new IllegalArgumentException("String to write cannot be null");
        }

        try {
            String fileContent = readFile(path);
            BufferedWriter buffer = new BufferedWriter(new FileWriter(new File(path)));
            buffer.append(fileContent);
            buffer.append(line);
            buffer.close();
        } catch (IOException e) {
            System.out.println("Error writing file '" + path + "'");
        }
    }

    /**
     * Overrides the file
     */
    private void writeFile(String path, String line) {
        if (path == null) {
            throw new IllegalArgumentException("File path cannot be null");
        }
        if (line == null) {
            throw new IllegalArgumentException("String to write cannot be null");
        }

        try {
            BufferedWriter buffer = new BufferedWriter(new FileWriter(new File(path)));
            buffer.append(line);
            buffer.close();
        } catch (IOException e) {
            System.out.println("Error writing file '" + path + "'");
        }
    }

}
