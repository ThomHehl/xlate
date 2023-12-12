package org.forerunnerintl.xlate.note;

import com.heavyweightsoftware.io.KeyedSequentialFile;
import org.forerunnerintl.xlate.io.ProjectFiles;
import org.forerunnerintl.xlate.io.ProjectFilesImpl;
import org.forerunnerintl.xlate.io.ProjectSettings;

import java.util.Arrays;

public class PreferredTranslationFile {

    private KeyedSequentialFile keyedSequentialFile;
    private ProjectSettings projectSettings;

    public PreferredTranslationFile(ProjectSettings projectSettings) {
        this(projectSettings, new ProjectFilesImpl(projectSettings));
    }

    protected PreferredTranslationFile(ProjectSettings projectSettings, ProjectFiles projectFiles) {
        this.projectSettings = projectSettings;
        keyedSequentialFile = projectFiles.getPreferredTranslation();
    }

    public TranslationEntry get(String key) {
        String[] record = keyedSequentialFile.get(key);

        TranslationEntry result;
        if (record == null) {
            result = null;
        } else {
            result = buildEntryFromRecord(record);
        }

        return result;
    }

    protected static TranslationEntry buildEntryFromRecord(String[] record) {
        TranslationEntry result = new TranslationEntry();
        result.setKey(record[0]);

        String parts[] = record[1].split("\\|");
        result.setPrimary(parts[0].trim());
        String[] alts = Arrays.copyOfRange(parts, 1, parts.length);
        result.setAlternates(alts);

        return result;
    }

    protected static String[] buildRecordFromEntry(TranslationEntry entry) {
        String[] result = new String[2];

        result[0] = rightPPad(entry.getKey(), ProjectFilesImpl.PREFERRED_TRANSLATION_KEY_LENGTH);

        StringBuilder sb = new StringBuilder(entry.getPrimary());
        for (String alt : entry.getAlternates()) {
            sb.append("|");
            sb.append(alt);
        }
        result[1] = sb.toString();

        return result;
    }

    private static String rightPPad(String key, int length) {
        StringBuilder sb = new StringBuilder(key);

        while (sb.length() < length) {
            sb.append(' ');
        }

        return sb.toString();
    }

    public void store(TranslationEntry translationEntry) {
        String[] record = buildRecordFromEntry(translationEntry);

        String[] oldEntry = keyedSequentialFile.get(translationEntry.getKey());
        if (oldEntry == null) {
            keyedSequentialFile.insert(record[0], record[1]);
        } else {
            keyedSequentialFile.update(record[0], record[1]);
        }
    }
}
