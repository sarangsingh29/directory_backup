package com.maverick.fsbackup;

import com.google.inject.internal.util.Lists;
import org.apache.log4j.Logger;

import java.util.List;

public class BackupController {
    public void startBackup(ConnectionType connType,
                            Long lastRunTimestamp,
                            Integer inputThreadCount,
                            String srcRootPath,
                            String destRootPath) {

        //Initialize with Linux file commands.
        List<String> copyCommand = Lists.newArrayList("cp", "-r");
        List<String> tsCommand = Lists.newArrayList("date", "+%s", "-r");
        List<String> findCommand = Lists.newArrayList("find");

        //Modify commands to use ADB.
        if (connType == ConnectionType.ADB) {
                copyCommand = Lists.newArrayList("adb", "pull", "-a");
                tsCommand = Lists.newArrayList("adb", "shell", "date", "+%s", "-r");
                findCommand = Lists.newArrayList("adb", "shell", "find");
        }

        FileDeltaGenerator deltaGenerator = new FileDeltaGenerator(findCommand, tsCommand);
        List<String> fileList = deltaGenerator
                .generateFileList(srcRootPath, lastRunTimestamp)
                .orElseThrow(RuntimeException::new);

        int fileCount = fileList.size();
        int threadCount = (inputThreadCount < fileCount)? inputThreadCount : fileCount;
        int perThreadCount = (int) Math.ceil(((double)fileCount)/threadCount);

        Logger.getRootLogger().info("Number of files to be backed up: " + fileCount);

        BackupJob[] threadArray = new BackupJob[threadCount];


        for(int i = 1; i < threadCount + 1; i++) {
            int startIndex = (i - 1) * perThreadCount;
            int tempIndex = (i * perThreadCount);
            int endIndex = (tempIndex < fileCount)? tempIndex - 1 : fileCount - 1;
            if(startIndex <= endIndex)
                threadArray[i - 1] = new BackupJob(i, srcRootPath, destRootPath, fileList.subList(startIndex, endIndex + 1), copyCommand);
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

        Logger.getRootLogger().info("Results: Threads#: " + threadCount + " " + "Time: " + (endTime - startTime));
        Logger.getRootLogger().info("Backup Timestamp: " + System.currentTimeMillis()/1000);
    }
}
