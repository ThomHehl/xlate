package org.forerunnerintl.xlate.ui;

import org.forerunnerintl.xlate.io.ProjectSettings;
import org.forerunnerintl.xlate.io.XlateSettings;

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
    public void createProject(File projectDir) {
        mainEditorPane.newProject(projectSettings);
    }

    private void loadProject() {
    }
}
