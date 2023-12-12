package org.forerunnerintl.xlate.text.osis;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.TreeSet;

public class LemmaExtractor {
    public static void main(String[] args) throws IOException, InterruptedException {
        TreeSet<String> prefixes = new TreeSet<>();
        TreeSet<String> suffixes = new TreeSet<>();
        String cmd = "grep \"lemma=\" /home/thomas/workspace/forerunner-bible/source-text/OT/*.xml";
        Runtime runtime = Runtime.getRuntime();
        Process process = runtime.exec(new String[] { "sh", "-c", cmd });
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String line;
        while ((line = reader.readLine()) != null) {
            String ref = parseReference(line);
            if (isInteresting(ref)) {
                String prefix = getPrefix(ref);
                if (prefix != null) {
                    prefixes.add(prefix);
                }

                String suffix = getSuffix(ref);
                if (suffix != null) {
                    suffixes.add(suffix);
                }
            }
        }

        System.out.println("Prefixes:" + prefixes.size());
        System.out.println(prefixes);

        System.out.println("Suffixes:" + suffixes.size());
        System.out.println(suffixes);
    }

    private static String parseReference(String line) {
        final String searchString = "lemma=\"";
        int pos = line.indexOf(searchString);
        pos += searchString.length();
        String str = line.substring(pos);

        pos = str.indexOf('"');
        String result = str.substring(0, pos);

        return result;
    }

    private static String getPrefix(String ref) {
        StringBuilder sb = new StringBuilder();

        for (char ch : ref.toCharArray()) {
            if (Character.isDigit(ch)) {
                break;
            } else {
                sb.append(ch);
            }
        }

        String result = sb.toString();
        return result == "" ? null : result;
    }

    private static String getSuffix(String ref) {
        StringBuilder sb = new StringBuilder();
        final int lookingForNumber = 0;
        final int lookingForSuffix = 1;
        final int addingSuffix = 2;
        int state = lookingForNumber;

        for (char ch : ref.toCharArray()) {
            switch (state) {
                case lookingForNumber:
                    if (Character.isDigit(ch)) {
                        state = lookingForSuffix;
                    }
                    break;

                case lookingForSuffix:
                    if (Character.isDigit(ch)) {
                        break;
                    } else {
                        state = addingSuffix;
                        // falling through
                    }

                case addingSuffix:
                    sb.append(ch);
                    break;
            }
        }

        String result = sb.toString();
        return result == "" ? null : result;
    }

    private static boolean isInteresting(String str) {
        boolean result = false;

        for (char ch : str.toCharArray()) {
            result = ch == ' ' || ch == '/' || Character.isAlphabetic(ch);
            if (result) {
                break;
            }
        }

        return result;
    }
}
