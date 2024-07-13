package org.example;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class AssessmentSplitter {

    public static void splitFiles(String folderName, String toDirectory) throws IOException {
        BufferedWriter outfile;
        File folder = new File(folderName);
        File[] listOfFiles = folder.listFiles();
        for (int i = 0; i < Objects.requireNonNull(listOfFiles).length; i++) {
            if (listOfFiles[i].isFile()) {
                LinkedList<ArrayList<String>> lists = new LinkedList<>();
                //System.out.println(listOfFiles[i].getName());
                Scanner scanner = new Scanner(listOfFiles[i]);
                String fileName = listOfFiles[i].getName();
                String line = scanner.nextLine();
                while (!line.contains("ASSESSMENT")) {
                    line = scanner.nextLine();
                }
                for (int j = 0; j < 3; j++) {
                    scanner.nextLine();
                }
                line = scanner.nextLine();
                lists.add(new ArrayList<>());
                while (scanner.hasNextLine()) {
                    while (!line.contains("ASSESSMENT")) {
                        if (!line.isEmpty()) {
                            lists.getLast().add(line);
                        }
                        if (scanner.hasNextLine()) {
                            line = scanner.nextLine();
                        } else {
                            break;
                        }
                    }
                    if (!scanner.hasNextLine()) {
                        break;
                    }
                    for (int j = 0; j < 3; j++) {
                        scanner.nextLine();
                    }
                    if (scanner.hasNextLine()) {
                        line = scanner.nextLine();
                    } else {
                        break;
                    }
                    lists.add(new ArrayList<>());
                }
                scanner.close();
                if (!lists.getLast().isEmpty() && lists.getLast().get(lists.getLast().size() - 1).trim().equals("}")) {
                    lists.getLast().remove(lists.getLast().size() - 1);
                }
                for (int j = 0; j < lists.size(); j++) {
                    OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(toDirectory + "/" + fileName + "-" + j), StandardCharsets.UTF_8);
                    outfile = new BufferedWriter(writer);
                    for (int k = 0; k < lists.get(j).size(); k++) {
                        outfile.write(lists.get(j).get(k));
                        outfile.newLine();
                    }
                    outfile.close();
                }
            }
        }
    }
}
