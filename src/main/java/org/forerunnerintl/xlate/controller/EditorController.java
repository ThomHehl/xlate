package org.forerunnerintl.xlate.controller;

import org.forerunnerintl.xlate.io.ProjectSettings;

import java.io.File;

public interface EditorController {
    void openProjectDirectory(File dir);

    void createProject(File projectDir);

    void convertSource(ProjectSettings projectSettings);
}
