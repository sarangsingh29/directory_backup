package com.maverick.fsbackup;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Generates the list of files to be copied based on timestamp.
 */
public class FileDeltaGenerator {

    public Optional<List<String>> generateFileList(String rootPath, Long lastTimestamp) {

        File rootFile = new File(rootPath);
        if(!rootFile.exists()) throw new RuntimeException("Root not found: " + rootPath);

        Runtime runtime = Runtime.getRuntime();
        try {
            Process process = runtime.exec(new String[]{"find", rootPath});
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

            ArrayList<String> fileList = new ArrayList<>();
            reader.lines().filter(x -> {
                File indivFile = new File(x);
                return !indivFile.isDirectory() && indivFile.lastModified() > lastTimestamp;
            }).forEach(y -> fileList.add(y));

            return Optional.of(fileList);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }
}
