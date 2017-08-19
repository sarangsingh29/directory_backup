package com.maverick.fsbackup;

import com.beust.jcommander.internal.Lists;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BackupJobTest {
    @Test
    public void backupJobRun() {
        String srcRoot = "test_resources/jobtest/input";
        String destRoot = "test_resources/jobtest/output";
        FileDeltaGenerator generator = new FileDeltaGenerator(
                com.google.inject.internal.util.Lists.newArrayList("find"),
                com.google.inject.internal.util.Lists.newArrayList("cp", "-r"));
        List<String> fileList = generator.generateFileList(srcRoot, 1503102972772L).get();
        BackupJob backupJob = new BackupJob(1, srcRoot, destRoot, fileList, new ArrayList<String>(Arrays.asList(new String[]{"cp"})));
        backupJob.start();
        try {
            backupJob.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
