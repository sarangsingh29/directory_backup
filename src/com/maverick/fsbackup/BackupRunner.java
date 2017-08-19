package com.maverick.fsbackup;


import com.beust.jcommander.JCommander;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

public class BackupRunner {
    public static void main(String[] args) {
        InputArguments arguments = new InputArguments();

        JCommander parser = new JCommander();
        parser.addObject(arguments);
        parser.parse(args);

        String sourceRootPath = String.join("", arguments.getSrcRoot());
        String destinationRootPath = String.join("", arguments.getDestRoot());
        String tsPath = String.join("", arguments.getTsPath());
        int inputThreadCount = arguments.getThreadCount();

        Long lastRunTimestamp = 0L;

        try {
            BufferedReader reader = new BufferedReader(new FileReader(tsPath));
            lastRunTimestamp = Long.parseLong(reader.readLine());
        } catch (IOException e) {
            System.out.println("Timestamp file not found. Falling back to the default timestamp value: 0.");
        } catch (NumberFormatException e) {
            System.out.println("Timestamp not valid. Falling back to the default timestamp value: 0.");
        }
        //
        FileDeltaGenerator deltaGenerator = new FileDeltaGenerator();
        List<String> fileList = deltaGenerator
                .generateFileList(sourceRootPath, lastRunTimestamp)
                .orElseThrow(RuntimeException::new);

        int fileCount = fileList.size();
        System.out.println("Number of files to be backed up: " + fileCount);

        int threadCount = (inputThreadCount < fileCount)? inputThreadCount : fileCount;
        BackupJob[] threadArray = new BackupJob[threadCount];

        int perThreadCount = (int) Math.ceil(((double)fileCount)/threadCount);


        for(int i = 1; i < threadCount + 1; i++) {
            int startIndex = (i - 1) * perThreadCount;
            int tempIndex = (i * perThreadCount);
            int endIndex = (tempIndex < fileCount)? tempIndex - 1 : fileCount - 1;
            if(startIndex <= endIndex)
                threadArray[i - 1] = new BackupJob(i, sourceRootPath, destinationRootPath, fileList.subList(startIndex, endIndex + 1));
            else
                threadArray[i - 1] = null;
        }

        Long startTime = System.currentTimeMillis();

        for(BackupJob job: threadArray) {
            if(job != null) job.start();
        }

        try {
            for(BackupJob job: threadArray) {
                if(job != null) job.join();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Long endTime = System.currentTimeMillis();

        System.out.println("Results: Threads#: " + threadCount + " " + "Time: " + (endTime - startTime));
        System.out.println("Backup Timestamp: " + System.currentTimeMillis());
    }
}
