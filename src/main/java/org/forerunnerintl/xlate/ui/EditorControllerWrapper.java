package org.forerunnerintl.xlate.ui;

import java.io.File;

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
    public void createProject(File projectDir) {

        Thread startThread = new Thread() {
            @Override
            public void run() {
                super.run();
                editorController.createProject(projectDir);
            }
        };
        startThread.start();

    }
}
