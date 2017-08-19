package com.maverick.fsbackup;

import org.junit.Test;

import java.util.List;

public class BackupJobTest {
    @Test
    public void backupJobRun() {
        String srcRoot = "test_resources/jobtest/input";
        String destRoot = "test_resources/jobtest/output";
        FileDeltaGenerator generator = new FileDeltaGenerator();
        List<String> fileList = generator.generateFileList(srcRoot, 1503102972772L).get();
        BackupJob backupJob = new BackupJob(1, srcRoot, destRoot, fileList);
        backupJob.start();
        try {
            backupJob.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
