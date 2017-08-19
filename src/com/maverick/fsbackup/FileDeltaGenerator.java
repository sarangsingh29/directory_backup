package com.maverick.fsbackup;

import com.beust.jcommander.internal.Lists;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * Generates the list of files to be copied based on timestamp.
 */

@AllArgsConstructor
public class FileDeltaGenerator {

    List<String> command;
    List<String> timestampCommand;

    public Optional<List<String>> generateFileList(String rootPath, Long lastTimestamp) {

        Runtime runtime = Runtime.getRuntime();
        try {
            command.add(rootPath);
            command.add("-type");
            command.add("f");

            String[] runCmd = new String[command.size()];
            command.toArray(runCmd);

            Process process = runtime.exec(runCmd);
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

            ArrayList<String> fileList = new ArrayList<>();
            reader.lines().filter(x -> {
                Logger.getRootLogger().debug(x);

                List<String> clonedTimestampCommand = com.google.inject.internal.util.Lists.newArrayList(timestampCommand);

                String newSearch = x.replaceAll("(?=[]\\[+&|!'(){}^\"~*?:\\\\-])", "\\\\").replaceAll(" ", "\\\\ ");
                clonedTimestampCommand.add(newSearch);
                String[] tsRunCmd = new String[clonedTimestampCommand.size()];
                clonedTimestampCommand.toArray(tsRunCmd);
                Long lastModTime = 0L;

                try {
                    Process tsProcess = Runtime.getRuntime().exec(tsRunCmd);
                    BufferedReader tsReader = new BufferedReader(new InputStreamReader(tsProcess.getInputStream()));
                    lastModTime = Long.parseLong(tsReader.readLine());
                } catch (Exception e) {
                    Logger.getRootLogger().error(newSearch, e);
                }
                return lastModTime >= lastTimestamp;
            }).forEach(y -> fileList.add(y));

            return Optional.of(fileList);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }
}
