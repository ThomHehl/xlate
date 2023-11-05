package org.forerunnerintl.xlate.ui;

import javax.swing.*;
import java.awt.*;

public class MainEditorPane extends JPanel {
    private static final int    BUTTON_HEIGHT = 25;
    private static final int    BUTTON_WIDTH = 50;
    private static final Dimension  BUTTON_SIZE = new Dimension(BUTTON_WIDTH, BUTTON_HEIGHT);

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

    public MainEditorPane() {
        super(new BorderLayout());

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

        scrollDefinition = new JScrollPane();
        panelDefinition.add(scrollDefinition, BorderLayout.NORTH);

        textDefinition = new JTextArea();
        scrollDefinition.add(textDefinition);

        buttonEditDefinition = new JButton("Edit...");
        buttonEditDefinition.setPreferredSize(BUTTON_SIZE);
        panelDefinition.add(buttonEditDefinition, BorderLayout.SOUTH);

        tabbedPane.add(panelDefinition);
    }

    private void addTranslationNoteTab() {
        panelTranslation = new JPanel(new BorderLayout());

        scrollTranslation = new JScrollPane();
        panelTranslation.add(scrollTranslation, BorderLayout.NORTH);

        textTranslation = new JTextArea();
        scrollTranslation.add(textTranslation);

        buttonEditTranslation = new JButton("Edit...");
        buttonEditTranslation.setPreferredSize(BUTTON_SIZE);
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
}
