package com.maverick.fsbackup;

import lombok.AllArgsConstructor;

import java.util.List;

@AllArgsConstructor
public class BackupJob extends Thread {
    int threadRank;
    String sourceRoot;
    String destinationRoot;
    List<String> fileList;
    @Override
    public void run() {
        Copier copier = new Copier();
        int filesCopied = 0;
        for(String x: fileList) {
            //System.out.println(x);
            String sourceFile = x;
            String destinationFile = x.replace(sourceRoot, destinationRoot);
            filesCopied += copier.copy(sourceFile, destinationFile);
        }
        System.out.println("Total files backed up by Thread #" + threadRank + ": " + filesCopied);
    }
}
