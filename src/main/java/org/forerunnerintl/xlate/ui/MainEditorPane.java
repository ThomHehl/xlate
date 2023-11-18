package org.forerunnerintl.xlate.ui;

import org.forerunnerintl.xlate.io.ProjectSettings;

import java.awt.*;
import java.io.File;
import javax.swing.*;

public class MainEditorPane extends JPanel {
    public static final String  NAME_DEFINITION = "Definition";
    public static final String  NAME_TRANSLATION_NOTE = "Translation Note";

    private static final int    ANSWER_YES = 0;

    private JFrame          owner;

    private JEditorPane     editMainEditor;
    private JScrollPane     scrollMainEditor;
    private JTabbedPane     tabbedPane;

    private JButton         buttonEditDefinition;
    private JPanel          panelDefinition;
    private JScrollPane     scrollDefinition;
    private JTextArea       textDefinition;

    private JButton         buttonEditTranslation;
    private JPanel          panelTranslation;
    private JScrollPane     scrollTranslation;
    private JTextArea       textTranslation;

    private EditorController    editorController;

    public MainEditorPane(JFrame owner) {
        super(new BorderLayout());
        this.owner = owner;
        editorController = new EditorControllerWrapper(this);

        buildEditorPane();
        buildTabPane();
    }

    private void buildEditorPane() {
        editMainEditor = new JEditorPane();
        editMainEditor.setEditable(false);
        scrollMainEditor = new JScrollPane(editMainEditor);
        add(scrollMainEditor, BorderLayout.WEST);
    }

    private void buildTabPane() {
        tabbedPane = new JTabbedPane();
        add(tabbedPane, BorderLayout.EAST);

        addDefinitionTab();
        addTranslationNoteTab();
    }

    private void addDefinitionTab() {
        panelDefinition = new JPanel(new BorderLayout());
        panelDefinition.setName(NAME_DEFINITION);

        scrollDefinition = new JScrollPane();
        panelDefinition.add(scrollDefinition, BorderLayout.NORTH);

        textDefinition = new JTextArea();
        scrollDefinition.add(textDefinition);

        buttonEditDefinition = new JButton("Edit...");
        buttonEditDefinition.setPreferredSize(Constants.BUTTON_SIZE);
        panelDefinition.add(buttonEditDefinition, BorderLayout.SOUTH);

        tabbedPane.add(panelDefinition);
    }

    private void addTranslationNoteTab() {
        panelTranslation = new JPanel(new BorderLayout());
        panelTranslation.setName(NAME_TRANSLATION_NOTE);

        scrollTranslation = new JScrollPane();
        panelTranslation.add(scrollTranslation, BorderLayout.NORTH);

        textTranslation = new JTextArea();
        scrollTranslation.add(textTranslation);

        buttonEditTranslation = new JButton("Edit...");
        buttonEditTranslation.setPreferredSize(Constants.BUTTON_SIZE);
        panelTranslation.add(buttonEditTranslation, BorderLayout.SOUTH);

        tabbedPane.add(panelTranslation);
    }

    public void resizeComponents(int newWidth, int newHeight) {
        int leftWidth = (int) Math.floor(newWidth * .75);
        int rightWidth = newWidth - leftWidth;

        Dimension leftDim = new Dimension(leftWidth, newHeight);
        scrollMainEditor.setPreferredSize(leftDim);

        Dimension rightDim = new Dimension(rightWidth, newHeight);
        tabbedPane.setPreferredSize(rightDim);
    }

    public void openProject() {
        JFileChooser chooser = new JFileChooser();
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        chooser.setDialogTitle("Select bible project directory");
        int returnVal = chooser.showOpenDialog(this);
        if(returnVal == JFileChooser.APPROVE_OPTION) {
            File dir = chooser.getSelectedFile();
            editorController.openProjectDirectory(dir);
        }
    }

    /**
     * A project wasn't found for the specified directory. Handle it.
     * @param projectDir the project directory
     */
    public void handleNoProject(File projectDir) {
        Object[] buttonText = {"Yes", "No"};
        if (showYesNoDialog(buttonText,
                "No project found in: " + projectDir.getAbsolutePath() + ". Create new project?",
                "No Project Found")) {
            editorController.createProject(projectDir);
        }
    }

    private boolean showYesNoDialog(Object[] buttonText, String msg, String title) {
        int choice = JOptionPane.showOptionDialog(this,
                msg,
                title,
                JOptionPane.YES_NO_CANCEL_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                buttonText,
                buttonText[0]);

        return choice == ANSWER_YES;
    }

    public void newProject(ProjectSettings projectSettings) {
        NewProjectDialog dialog = new NewProjectDialog(owner);
        if (dialog.isCreating()) {
            projectSettings.setTitle(dialog.getTitle());
            projectSettings.setOldTestamentSourceFormat(dialog.getOldTestament());
            projectSettings.setNewTestamentSourceFormat(dialog.getNewTestament());
            projectSettings.store();
        }
    }
}
