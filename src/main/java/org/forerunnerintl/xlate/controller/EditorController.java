package org.forerunnerintl.xlate.controller;

import org.forerunnerintl.xlate.io.ProjectSettingsImpl;
import org.forerunnerintl.xlate.text.osis.OsisDocument;

import java.io.File;

public interface EditorController {
    void openProjectDirectory(File dir);

    void createProject(File projectDir);

    void convertSource(ProjectSettingsImpl projectSettings);

    void loadBook(String bookCode);

    void editDocument(OsisDocument document, EditWordCommand cmd);
}
