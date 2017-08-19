package com.maverick.fsbackup;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;

public class CopierTest {
    @Test
    public void testCopy() {
        String inputPath = "test_resources/test1/input/file1.txt";
        String outputPath = "test_resources/test1/output/file1.txt";
        Copier copier = new Copier(new ArrayList<String>(Arrays.asList(new String[]{"cp", "-r"})));
        copier.copy(inputPath, outputPath);
        System.out.println("Done!");
    }

    @Test
    public void testInnerDirectoryCopy() {
        String inputPath = "test_resources/test1/input/";
        String outputPath = "test_resources/test1/output/";
        Copier copier = new Copier(new ArrayList<String>(Arrays.asList(new String[]{"cp", "-r"})));
        copier.copy(inputPath, outputPath);
        System.out.println("Done!");
    }
}
