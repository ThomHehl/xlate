package org.forerunnerintl.xlate.controller;

import org.forerunnerintl.xlate.io.ProjectSettingsImpl;
import org.forerunnerintl.xlate.note.TranslationEntry;
import org.forerunnerintl.xlate.text.osis.OsisDocument;

import java.io.File;
import java.util.concurrent.Future;

public interface EditorController {
    void openProjectDirectory(File dir);

    void createProject(File projectDir);

    void convertSource(ProjectSettingsImpl projectSettings);

    void loadBook(String bookCode);

    Future<TranslationEntry> getPreferredTranslation(OsisDocument document, String key);

    void editDocument(OsisDocument document, EditWordCommand cmd);
}
