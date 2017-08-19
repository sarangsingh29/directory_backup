package com.maverick.fsbackup;


import com.beust.jcommander.JCommander;
import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class BackupStarter {
    public static void main(String[] args) {
        InputArguments arguments = new InputArguments();

        JCommander parser = new JCommander();
        parser.addObject(arguments);
        parser.parse(args);

        String sourceRootPath = String.join("", arguments.getSrcRoot());
        String destinationRootPath = String.join("", arguments.getDestRoot());
        String tsPath = String.join("", arguments.getTsPath());
        String connType = arguments.getConnType();
        int inputThreadCount = arguments.getThreadCount();


        Long lastRunTimestamp = 0L;

        try {
            BufferedReader reader = new BufferedReader(new FileReader(tsPath));
            lastRunTimestamp = Long.parseLong(reader.readLine());
        } catch (IOException e) {
            Logger.getRootLogger().warn("Timestamp file not found. Default value: 0.");
        } catch (NumberFormatException e) {
            Logger.getRootLogger().warn("Timestamp not valid. Default value: 0.");

        }

        ConnectionType connectionType;
        switch (connType) {
            case "adb":
            case "ADB":
                connectionType = ConnectionType.ADB;
                break;
            default:
                connectionType = ConnectionType.FS;
        }

        new BackupController().startBackup(
                connectionType,
                lastRunTimestamp,
                inputThreadCount,
                sourceRootPath,
                destinationRootPath);

    }
}
