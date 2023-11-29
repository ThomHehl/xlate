package com.heavyweightsoftware.util;

import java.io.*;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class StringHelper {
    public static List<String> fileToArray(File file) {
        List<String> result = new ArrayList<>();

        String line;
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));

            while ((line = reader.readLine()) != null) {
                result.add(line);
            }
        } catch (IOException ioe) {
            throw new RuntimeException(ioe);
        }

        return result;
    }

    public static List<String> pathToArray(Path path) {
        return fileToArray(path.toFile());
    }
}
