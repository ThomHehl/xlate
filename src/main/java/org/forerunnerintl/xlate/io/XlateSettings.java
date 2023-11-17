package org.forerunnerintl.xlate.io;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

public class XlateSettings {

    public static final String PROP_USER_HOME   = "user.home";
    public static final String XLATE_SETTINGS_FILE_NAME
                                                = "xlate.settings";
    public static final String XLATE_USER_HOME  = ".xlate";

    private static final String KEY_LAST_PROJECT  = "LastProject";

    private Properties      settings;

    public static String getXlateUserDirectory() {
        Path result = Paths.get(System.getProperty(PROP_USER_HOME), XLATE_USER_HOME);
        return result.toAbsolutePath().toString();
    }

    public static Path getXlateSettingsPath() {
        Path result = Paths.get(getXlateUserDirectory(), XLATE_SETTINGS_FILE_NAME);
        return result;
    }

    public XlateSettings() {
        settings = loadProperties();
    }

    private Properties loadProperties() {
        Properties result = new Properties();

        try {
            BufferedReader reader = new BufferedReader(new FileReader(getXlateSettingsPath().toFile()));
            result.load(reader);
        } catch (FileNotFoundException e) {
            createProperties(result);
        } catch (IOException ioe) {
            throw new RuntimeException("Error loading properties file", ioe);
        }

        return result;
    }

    private void createProperties(Properties properties) {
        properties.put(KEY_LAST_PROJECT, "");

        BufferedWriter writer = null;
        try {
            File file = getXlateSettingsPath().toFile();
            File dir = file.getParentFile();
            dir.mkdirs();
            writer = new BufferedWriter(new FileWriter(file));
            properties.store(writer, "Xlate Properties");
        } catch (IOException ioe) {
            throw new RuntimeException("Error creating settings", ioe);
        }
    }

    /**
     * The last project accessed
     * @return the path to the last project
     */
    public Path getLastProject() {
        String pathName = settings.getProperty(KEY_LAST_PROJECT);
        Path result = Paths.get(pathName);
        return result;
    }

    /**
     * Set the last project accessed
     * @param projectDirectory the project directory
     */
    public void setLastProject(Path projectDirectory) {
        String pathName = projectDirectory.toFile().getAbsolutePath();
        settings.setProperty(KEY_LAST_PROJECT, pathName);
    }

    public void saveSettings() {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(getXlateSettingsPath().toFile()));
            settings.store(writer, "Automatic update");
        } catch (IOException ioe) {
            throw new RuntimeException("Error storing settings", ioe);
        }
    }
}
