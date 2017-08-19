package com.maverick.fsbackup;

import lombok.AllArgsConstructor;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * Handles the copying of a single file.
 */

@AllArgsConstructor
public class Copier {
    ArrayList<String> command;

    public int copy(String inputPath, String outputPath) {
        Runtime rt = Runtime.getRuntime();
        int retValue = 0;
        try {
            File outputFile = new File(outputPath);
            if (!outputFile.exists()) {
                outputFile.getParentFile().mkdirs();
            }
            command.add(inputPath);
            command.add(outputFile.getAbsolutePath());

            String[] runCmd = new String[command.size()];
            command.toArray(runCmd);

            Process process = rt.exec(runCmd);

            process.waitFor();

            retValue = (process.exitValue() == 0) ? 1 : 0;
            if(retValue == 0) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                reader.lines().forEach(x -> System.out.println(x));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return retValue;
    }
}
