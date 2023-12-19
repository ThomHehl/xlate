package org.forerunnerintl.xlate.controller;

import org.forerunnerintl.xlate.io.ProjectSettingsImpl;
import org.forerunnerintl.xlate.note.TranslationEntry;
import org.forerunnerintl.xlate.text.osis.OsisDocument;
import org.forerunnerintl.xlate.ui.MainEditorPane;

import java.io.File;
import java.util.concurrent.Future;

public class EditorControllerWrapper implements EditorController {
    private EditorController editorController;

    public EditorControllerWrapper(MainEditorPane pane) {

       Thread startThread = new Thread() {
           @Override
           public void run() {
               super.run();
               editorController = new EditorControllerImpl(pane);
           }
       };
       startThread.start();
       Thread.yield();

    }

    @Override
    public void openProjectDirectory(File dir) {

        Thread startThread = new Thread() {
            @Override
            public void run() {
                super.run();
                editorController.openProjectDirectory(dir);
            }
        };
        startThread.start();

    }

    @Override
    public void convertSource(ProjectSettingsImpl projectSettings) {
        Thread startThread = new Thread() {
            @Override
            public void run() {
                super.run();
                editorController.convertSource(projectSettings);
            }
        };
        startThread.start();
    }

    @Override
    public Future<TranslationEntry> getPreferredTranslation(OsisDocument document, String key) {
        return editorController.getPreferredTranslation(document, key);
    }

    @Override
    public void loadBook(String bookCode) {
        Thread startThread = new Thread() {
            @Override
            public void run() {
                super.run();
                Thread.yield();
                editorController.loadBook(bookCode);
            }
        };
        startThread.start();
    }

    @Override
    public void editDocument(OsisDocument document, EditWordCommand cmd) {
        Thread startThread = new Thread() {
            @Override
            public void run() {
                super.run();
                Thread.yield();
                editorController.editDocument(document, cmd);
            }
        };
        startThread.start();
    }

    @Override
    public void storeDocument(OsisDocument document) {
        Thread startThread = new Thread() {
            @Override
            public void run() {
                super.run();
                Thread.yield();
                editorController.storeDocument(document);
            }
        };
        startThread.start();
    }

    @Override
    public void createProject(File projectDir) {
        Thread startThread = new Thread() {
            @Override
            public void run() {
                super.run();
                try {
                    Thread.sleep(100);
                } catch (InterruptedException ie) {
                    ie.printStackTrace();
                }
                editorController.createProject(projectDir);
            }
        };
        startThread.start();
    }
}
