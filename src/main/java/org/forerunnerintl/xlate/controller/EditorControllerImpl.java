package org.forerunnerintl.xlate.controller;

import org.forerunnerintl.xlate.io.ProjectSettings;
import org.forerunnerintl.xlate.io.XlateSettings;
import org.forerunnerintl.xlate.ui.MainEditorPane;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

public class EditorControllerImpl implements EditorController {
    final private MainEditorPane mainEditorPane;
    private Path projectPath;
    private ProjectSettings projectSettings;
    final private XlateSettings xlateSettings;

    public EditorControllerImpl(MainEditorPane pane) {
        mainEditorPane = pane;
        xlateSettings = new XlateSettings();
        Path settingsPath = xlateSettings.getLastProject();
        if (settingsPath.toString().isEmpty()) {
            mainEditorPane.openProject();
        }
    }

    @Override
    public void openProjectDirectory(File dir) {
        xlateSettings.saveSettings();
        projectSettings = new ProjectSettings();
        Path dirPath = Paths.get(dir.toURI());
        if (projectSettings.setProjectDirectory(dirPath)) {
            projectPath = dirPath;
            loadProject();
        } else {
            mainEditorPane.handleNoProject(dir);
        }
    }

    @Override
    public void convertSource(ProjectSettings projectSettings) {
        if (convertOldTestamentSource(projectSettings)) {
            convertNewTestamentSource(projectSettings);
        }
    }

    private boolean convertNewTestamentSource(ProjectSettings projectSettings) {
        boolean result = true;
        return result;
    }

    private boolean convertOldTestamentSource(ProjectSettings projectSettings) {
        boolean result = true;

        Path otSource = projectSettings.getOldTestamentSourceDirectory();
        if (!otSource.toFile().exists()) {
            mainEditorPane.directoryNotFound("Cannot find OT source directory", otSource.toFile().getAbsolutePath());
            result = false;
        }

        return result;
    }

    @Override
    public void createProject(File projectDir) {
        mainEditorPane.newProject(projectSettings);
    }

    private void loadProject() {
    }
}
