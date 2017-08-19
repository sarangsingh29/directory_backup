package com.maverick.fsbackup;

import com.google.inject.internal.util.Lists;
import org.junit.Test;

public class FileDeltaGeneratorTest {
    @Test
    public void testFileGeneration() {
        String rootPath = "test_resources";
        Long lastTimestamp = 0L;
        FileDeltaGenerator generator = new FileDeltaGenerator(
                Lists.newArrayList("find"),
                Lists.newArrayList("cp", "-r"));
        generator.generateFileList(rootPath, lastTimestamp).get().forEach(x -> System.out.println(x));
    }
}
