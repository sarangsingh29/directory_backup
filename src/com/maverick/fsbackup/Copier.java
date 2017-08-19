package com.maverick.fsbackup;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;

/**
 *  Handles the copying of a single file.
 */
public class Copier {
    public int copy(String inputPath, String outputPath) {
        Runtime rt = Runtime.getRuntime();
        int retValue = 0;
        try {
            File outputFile = new File(outputPath);
            if(!outputFile.exists()) {
                outputFile.getParentFile().mkdirs();
            }
           Process process = rt.exec(new String[]{"cp", "-r", inputPath, outputFile.getAbsolutePath()});
           BufferedReader reader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
           if(reader.lines().count() == 0) retValue = 1;
           else {
               reader.lines().forEach(x -> System.out.println(x));
           }
        } catch(Exception e){
            e.printStackTrace();
        }
        return retValue;
    }
}
