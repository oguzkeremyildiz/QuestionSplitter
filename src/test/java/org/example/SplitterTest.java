package org.example;

import junit.framework.TestCase;
import org.junit.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class SplitterTest extends TestCase {

    @Test
    public void testSplitter() throws BracesNotMatchException, FileNotFoundException {
        ArrayList<String> codeLines = new ArrayList<>();
        Scanner source = new Scanner(new File("test1.txt"));
        while (source.hasNext()) {
            codeLines.add(source.nextLine());
        }
        source.close();
        ArrayList<Integer> output = Splitter.split(codeLines);
        ArrayList<Integer> correct = new ArrayList<>();
        correct.add(0);
        correct.add(2);
        correct.add(6);
        correct.add(8);
        correct.add(9);
        correct.add(12);
        correct.add(19);
        correct.add(26);
        correct.add(27);
        correct.add(28);
        assertEquals(output, correct);
    }

}