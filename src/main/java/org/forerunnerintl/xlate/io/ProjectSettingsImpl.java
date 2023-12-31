package org.forerunnerintl.xlate.io;

import org.forerunnerintl.xlate.text.TextFormat;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

public class ProjectSettingsImpl implements ProjectSettings {
    private static final String        DEFAULT_REFERENCE = "Gen 1";
    private static final String         DIR_DATA = "data";
    private static final String         DIR_NEW_TESTAMENT = "NT";
    private static final String         DIR_NOTES = "notes";
    private static final String         DIR_OLD_TESTAMENT = "OT";
    private static final String         DIR_SOURCE_TEXT = "source-text";
    private static final String         DIR_TEXT = "text";
    private static final String         FILE_NAME = "xlate.settings";

    private static final String         KEY_LAST_REF = "LAST_REFERENCE";
    private static final String         KEY_NT_FORMAT = "NT_FORMAT";
    private static final String         KEY_OT_FORMAT = "OT_FORMAT";
    private static final String         KEY_TITLE = "TITLE";

    private Properties properties;
    private Path settingsDirectory;

    protected synchronized Properties getProperties() {
        return properties == null ? properties = new Properties() : properties;
    }

    @Override
    public TextFormat getNewTestamentSourceFormat() {
        TextFormat result = getTextFormat(KEY_NT_FORMAT);
        return result;
    }

    @Override
    public void setNewTestamentSourceFormat(TextFormat textFormat) {
        setTextFormat(KEY_NT_FORMAT, textFormat);
    }

    @Override
    public TextFormat getOldTestamentSourceFormat() {
        TextFormat result = getTextFormat(KEY_OT_FORMAT);
        return result;
    }

    @Override
    public void setOldTestamentSourceFormat(TextFormat textFormat) {
        setTextFormat(KEY_OT_FORMAT, textFormat);
    }

    private TextFormat getTextFormat(String key) {
        String strVal = getProperties().getProperty(key);
        TextFormat result = TextFormat.valueOf(strVal);
        return result;
    }

    private void setTextFormat(String key, TextFormat value) {
        String strVal = value.toString();
        getProperties().put(key, strVal);
    }

    @Override
    public String getTitle() {
        String result = getProperties().getProperty(KEY_TITLE);
        return result;
    }

    @Override
    public void setTitle(String title) {
        getProperties().put(KEY_TITLE, title);
    }

    @Override
    public Path getNewTestamentSourceDirectory() {
        Path result = Paths.get(getSourceTextDirectory().toString(), DIR_NEW_TESTAMENT);
        return result;
    }

    @Override
    public Path getOldTestamentSourceDirectory() {
        Path result = Paths.get(getSourceTextDirectory().toString(), DIR_OLD_TESTAMENT);
        return result;
    }

    @Override
    public Path getDataDirectory() {
        Path result = Paths.get(getProjectDirectory().toString(), DIR_DATA);
        return result;
    }

    @Override
    public Path getTextDirectory() {
        Path result = Paths.get(getProjectDirectory().toString(), DIR_TEXT);
        return result;
    }

    @Override
    public Path getSourceTextDirectory() {
        Path result = Paths.get(getProjectDirectory().toString(), DIR_SOURCE_TEXT);
        return result;
    }

    /**
     * Find the project settings in the specified directory
     * @return the path to the project home directory
     */
    @Override
    public Path getProjectDirectory() {
        return settingsDirectory;
    }

    @Override
    public String getLastReferenceLocation() {
        String lastRef = properties.getProperty(KEY_LAST_REF);
        if (lastRef == null) {
            lastRef = DEFAULT_REFERENCE;
        }

        return lastRef;
    }

    /**
     * Find the project settings in the specified directory
     * @param dir the directory for the project
     * @return true if the file exists. False if not
     */
    @Override
    public boolean setProjectDirectory(Path dir) {
        settingsDirectory = dir;
        properties = openProjectSettings(dir);
        return properties != null;
    }

    @Override
    public Path getNotesDirectory() {
        Path result = Paths.get(getProjectDirectory().toString(), DIR_NOTES);
        return result;
    }

    public void store() {
        Path settingsPath = Paths.get(settingsDirectory.toFile().getAbsolutePath(), FILE_NAME);
        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new FileWriter(settingsPath.toFile()));
            getProperties().store(writer, "Xlate Bible Translation Project Settings");
        } catch (IOException ioe) {
            throw new RuntimeException(ioe);
        }
    }

    private Properties openProjectSettings(Path dir) {
        Properties result = new Properties();

        Path settingsPath = Paths.get(dir.toFile().getAbsolutePath(), FILE_NAME);
        try {
            BufferedReader reader = new BufferedReader(new FileReader(settingsPath.toFile()));
            result.load(reader);
        } catch (FileNotFoundException fnfe) {
            result = null;
        } catch (IOException ioe) {
            result = null;
        }

        return result;
    }
}
