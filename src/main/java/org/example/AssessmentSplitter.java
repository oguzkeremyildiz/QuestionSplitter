package org.example;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class AssessmentSplitter {

    public static void splitFiles(String folderName) throws IOException {
        BufferedWriter outfile = null;
        File folder = new File(folderName);
        File[] listOfFiles = folder.listFiles();
        for (int i = 0; i < Objects.requireNonNull(listOfFiles).length; i++) {
            if (listOfFiles[i].isFile()) {
                Scanner scanner = new Scanner(listOfFiles[i]);
                int splits = 0;
                String filaName = listOfFiles[i].getName();
                scanner.nextLine();
                while (scanner.hasNextLine()) {
                    String line = scanner.nextLine();
                    if (line.startsWith("/** ASSESSMENT")) {
                        splits++;
                        if (outfile != null) {
                            outfile.close();
                        }
                        OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(filaName + "-" + splits), StandardCharsets.UTF_8);
                        outfile = new BufferedWriter(writer);
                        for (int j = 0; j < 3; j++) {
                            scanner.nextLine();
                        }
                        line = scanner.nextLine();
                    }
                    outfile.write(line);
                    outfile.newLine();
                }
                outfile.close();
                scanner.close();
            }
        }
    }
}
