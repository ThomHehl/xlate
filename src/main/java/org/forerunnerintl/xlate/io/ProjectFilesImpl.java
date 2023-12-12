package org.forerunnerintl.xlate.io;

import com.heavyweightsoftware.io.KeyedSequentialFile;
import com.heavyweightsoftware.io.KeyedSequentialFileImpl;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ProjectFilesImpl implements ProjectFiles {
    public static final String PREFERRED_TRANSLATION_FILE_NAME = "preferredTranslation";
    public static final int PREFERRED_TRANSLATION_DATA_LENGTH = 100;
    public static final int PREFERRED_TRANSLATION_KEY_LENGTH = 12;
    private ProjectSettings projectSettings;

    public ProjectFilesImpl(ProjectSettings projectSettings) {
        this.projectSettings = projectSettings;
    }

    @Override
    public KeyedSequentialFile getPreferredTranslation() {
        Path notesPath = getNotesPath();
        Path ksdsPath = Paths.get(notesPath.toString(), PREFERRED_TRANSLATION_FILE_NAME);
        File ksdsFile = ksdsPath.toFile();
        KeyedSequentialFileImpl result;
        if (ksdsFile.exists()) {
            result = new KeyedSequentialFileImpl(ksdsPath);
        } else {
            result = new KeyedSequentialFileImpl(ksdsPath, PREFERRED_TRANSLATION_KEY_LENGTH, PREFERRED_TRANSLATION_DATA_LENGTH);
        }

        return result;
    }

    protected Path getNotesPath() {
        Path result = projectSettings.getNotesDirectory();

        File file = result.toFile();
        file.mkdirs();

        return result;
    }
}
