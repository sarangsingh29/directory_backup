package com.maverick.fsbackup;

import org.junit.Test;

public class CopierTest {
    @Test
    public void testCopy() {
        String inputPath = "test_resources/test1/input/file1.txt";
        String outputPath = "test_resources/test1/output/file1.txt";
        Copier copier = new Copier();
        copier.copy(inputPath, outputPath);
        System.out.println("Done!");
    }

    @Test
    public void testInnerDirectoryCopy() {
        String inputPath = "test_resources/test1/input/dir1/inner1/file2.txt";
        String outputPath = "test_resources/test1/output/dir2/inner1/file2.txt";
        Copier copier = new Copier();
        copier.copy(inputPath, outputPath);
        System.out.println("Done!");
    }
}
