package com.maverick.fsbackup;

import org.junit.Test;

public class FileDeltaGeneratorTest {
    @Test
    public void testFileGeneration() {
        String rootPath = "test_resources";
        Long lastTimestamp = 0L;
        FileDeltaGenerator generator = new FileDeltaGenerator();
        generator.generateFileList(rootPath, lastTimestamp).get().forEach(x -> System.out.println(x));
    }
}
