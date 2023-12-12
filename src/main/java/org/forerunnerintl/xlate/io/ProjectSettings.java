package org.forerunnerintl.xlate.io;

import org.forerunnerintl.xlate.text.TextFormat;

import java.nio.file.Path;

public interface ProjectSettings {
    TextFormat getNewTestamentSourceFormat();

    void setNewTestamentSourceFormat(TextFormat textFormat);

    TextFormat getOldTestamentSourceFormat();

    void setOldTestamentSourceFormat(TextFormat textFormat);

    String getTitle();

    void setTitle(String title);

    Path getNewTestamentSourceDirectory();

    Path getOldTestamentSourceDirectory();

    Path getDataDirectory();

    Path getTextDirectory();

    Path getSourceTextDirectory();

    Path getProjectDirectory();

    String getLastReferenceLocation();

    boolean setProjectDirectory(Path dir);

    Path getNotesDirectory();
}
