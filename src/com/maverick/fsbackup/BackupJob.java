package com.maverick.fsbackup;

import com.google.inject.internal.util.Lists;
import lombok.AllArgsConstructor;
import org.apache.log4j.Logger;

import java.util.List;

@AllArgsConstructor
public class BackupJob extends Thread {
    int threadRank;
    String sourceRoot;
    String destinationRoot;
    List<String> fileList;
    List<String> command;
    @Override
    public void run() {
        int filesCopied = 0;

        for(String x: fileList) {
            Logger.getRootLogger().debug(x);
            Copier copier = new Copier(Lists.newArrayList(command));
            String sourceFile = x;
            String destinationFile = x.replace(sourceRoot, destinationRoot);
            filesCopied += copier.copy(sourceFile, destinationFile);
        }
        Logger.getRootLogger().info("Files backed up by Thread #" + threadRank + ": " + filesCopied);
    }
}
