package org.forerunnerintl.xlate.ui;

import org.forerunnerintl.xlate.controller.EditWordCommand;
import org.forerunnerintl.xlate.note.TranslationEntry;
import org.forerunnerintl.xlate.text.VerseReference;
import org.forerunnerintl.xlate.text.osis.OsisWord;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.NumberFormatter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.text.NumberFormat;
import java.util.Arrays;
import java.util.concurrent.ExecutionException;

public class EditTranslatedWordDialog extends JDialog
                                        implements ActionListener, DocumentListener {
    private static final String     NON_ECHO_CHARACTERS = ".Z";
    private static final String     TEXT_ALT_DEFINITIONS = "Alternate Definitions";
    private static final String     TEXT_COUNT = "Count";
    private static final String     TEXT_ECHO_DEFINITION = "Echo Definition";
    private static final String     TEXT_EDIT_TEXT = "Edit text";
    private static final String     TEXT_INSERT_AFTER = "Insert after";
    private static final String     TEXT_INSERT_BEFORE = "Insert before";
    private static final String     TEXT_MOVE = "Move";
    private static final String     TEXT_PRIMARY_DEFINITION = "Primary Definition";
    private static final String     TEXT_TEXT = "Text";
    private static final String     TITLE = "Edit word:";

    private JButton btnCancel;
    private JButton btnOk;
    private JRadioButton btnEditText;
    private JRadioButton btnInsertBefore;
    private JRadioButton btnInsertAfter;
    private JRadioButton btnMove;
    private JLabel lblAltDefinitions;
    private JTextField txtAltDefinitions;
    private JLabel lblCount;
    private JFormattedTextField txtCount;
    private JLabel lblPrimaryDefinition;
    private JTextField txtPrimaryDefinition;
    private JLabel lblText;
    private JTextField txtText;

    private EditWordCommand editWordCommand;
    private String altDefinition;
    private String primaryDefinition;
    private boolean definitionProvided;
    private VerseReference verseReference;
    private OsisWord word;

    public EditTranslatedWordDialog(Frame owner, EditWordCommand editWordCommand) {
        super(owner, buildTitle(editWordCommand.getWord()), true);
        this.verseReference = editWordCommand.getVerseReference();
        this.word = editWordCommand.getWord();

        setLocations(owner);

        setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
        createRadioButtonGroup();
        createTextControls();
        createActionButtons();

        setTranslationEntry(editWordCommand);
        txtText.setText(this.word.getBodyText());
        txtText.requestFocus();
        txtText.selectAll();
        txtPrimaryDefinition.setText(this.primaryDefinition);
        txtAltDefinitions.setText(this.altDefinition);

        setSize(UiConstants.DIALOG_SIZE);
        setVisible(true);
    }

    private void setTranslationEntry(EditWordCommand editWordCommand) {
        TranslationEntry translationEntry = null;
        try {
            translationEntry = editWordCommand.getTranslationEntryFuture().get();
        } catch (InterruptedException ie) {
            ie.printStackTrace();
        } catch (ExecutionException ee) {
            throw new RuntimeException(ee);
        }

        if (translationEntry == null) {
            this.primaryDefinition = "";
            this.altDefinition = "";
        } else {
            String primary = translationEntry.getPrimary();
            if (primary == null) {
                this.primaryDefinition = "";
            } else {
                this.primaryDefinition = primary;
                definitionProvided = true;
            }

            String alt = translationEntry.getAlternatesAsString();
            this.altDefinition = alt == null | alt.equals("") ? "" : alt;
        }
    }

    private void setLocations(Frame owner) {
        Rectangle bounds = owner.getGraphicsConfiguration().getBounds();
        Dimension dimension = bounds.getSize();
        int x = (int) (((dimension.getWidth() - owner.getWidth()) / 2) + bounds.getMinX());
        int y = (int) (((dimension.getHeight() - owner.getHeight()) / 2) + bounds.getMinY());
        setLocation(x, y);
    }

    private static String buildTitle(OsisWord word) {
        StringBuilder sb = new StringBuilder(TITLE);

        if (word.getSourceWord() != null && word.getLemma() != null){
            sb.append(word.getSourceWord());
            sb.append("(");
            sb.append(word.getLemma());
            sb.append(")");
        }

        return sb.toString();
    }

    private void createRadioButtonGroup() {
        JPanel radioPanel = new JPanel(new GridLayout(4, 1));

        btnEditText = new JRadioButton(TEXT_EDIT_TEXT);
        btnEditText.setMnemonic(KeyEvent.VK_E);
        btnEditText.setActionCommand(TEXT_EDIT_TEXT);
        btnEditText.setSelected(true);
        radioPanel.add(btnEditText);

        btnInsertBefore = new JRadioButton(TEXT_INSERT_BEFORE);
        btnInsertBefore.setMnemonic(KeyEvent.VK_B);
        btnInsertBefore.setActionCommand(TEXT_INSERT_BEFORE);
        radioPanel.add(btnInsertBefore);

        btnInsertAfter = new JRadioButton(TEXT_INSERT_AFTER);
        btnInsertAfter.setMnemonic(KeyEvent.VK_A);
        btnInsertAfter.setActionCommand(TEXT_INSERT_AFTER);
        radioPanel.add(btnInsertAfter);

        btnMove = new JRadioButton(TEXT_MOVE);
        btnMove.setMnemonic(KeyEvent.VK_M);
        btnMove.setActionCommand(TEXT_MOVE);
        radioPanel.add(btnMove);

        //Group the radio buttons.
        ButtonGroup group = new ButtonGroup();
        group.add(btnEditText);
        group.add(btnInsertBefore);
        group.add(btnInsertAfter);
        group.add(btnMove);

        //Register a listener for the radio buttons.
        btnEditText.addActionListener(this);
        btnInsertBefore.addActionListener(this);
        btnInsertAfter.addActionListener(this);
        btnMove.addActionListener(this);

        add(radioPanel);
    }

    private void createTextControls() {
        JPanel textControlsPanel = new JPanel(new GridLayout(4, 2));

        lblText = new JLabel(TEXT_TEXT);
        textControlsPanel.add(lblText);

        txtText = new JTextField();
        if (!definitionProvided) {
            txtText.setActionCommand(TEXT_ECHO_DEFINITION);
            txtText.getDocument().addDocumentListener(this);
        }
        textControlsPanel.add(txtText);

        lblPrimaryDefinition = new JLabel(TEXT_PRIMARY_DEFINITION);
        textControlsPanel.add(lblPrimaryDefinition);

        txtPrimaryDefinition = new JTextField();
        textControlsPanel.add(txtPrimaryDefinition);

        lblAltDefinitions = new JLabel(TEXT_ALT_DEFINITIONS);
        textControlsPanel.add(lblAltDefinitions);

        txtAltDefinitions = new JTextField();
        textControlsPanel.add(txtAltDefinitions);

        lblCount = new JLabel(TEXT_COUNT);
        textControlsPanel.add(lblCount);

        NumberFormat format = NumberFormat.getInstance();
        NumberFormatter formatter = new NumberFormatter(format);
        formatter.setValueClass(Integer.class);
        formatter.setMinimum(-10);
        formatter.setMaximum(10);
        formatter.setAllowsInvalid(false);
        formatter.setCommitsOnValidEdit(true);
        txtCount = new JFormattedTextField(formatter);
        textControlsPanel.add(txtCount);

        add(textControlsPanel);

        showText(true);
        showDefinitions(true);
        showCount(false);
    }

    private void createActionButtons() {
        JPanel buttonPanel = new JPanel(new GridLayout(1, 2));

        btnCancel = new JButton(UiConstants.TEXT_CANCEL);
        btnCancel.setMnemonic(KeyEvent.VK_L);
        btnCancel.setPreferredSize(UiConstants.BUTTON_SIZE);
        btnCancel.setActionCommand(UiConstants.TEXT_CANCEL);
        btnCancel.addActionListener(this);
        buttonPanel.add(btnCancel);

        btnOk = new JButton(UiConstants.TEXT_OK);
        btnOk.setMnemonic(KeyEvent.VK_O);
        btnOk.setPreferredSize(UiConstants.BUTTON_SIZE);
        btnOk.setActionCommand(UiConstants.TEXT_OK);
        btnOk.addActionListener(this);
        buttonPanel.add(btnOk);

        add(buttonPanel);
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        switch (event.getActionCommand()) {
            case UiConstants.TEXT_CANCEL:
                cancelButtonClicked();
                break;

            case TEXT_EDIT_TEXT:
                showText(true);
                showDefinitions(true);
                showCount(false);
                break;

            case TEXT_INSERT_AFTER:
            case TEXT_INSERT_BEFORE:
                showText(true);
                clearText();
                showDefinitions(false);
                showCount(false);
                break;

            case TEXT_MOVE:
                showText(false);
                showDefinitions(false);
                showCount(true);
                break;

            case UiConstants.TEXT_OK:
                okButtonClicked();
                break;
        }
    }

    private void cancelButtonClicked() {
        editWordCommand = null;
        setVisible(false);
    }

    private void echoDefinition() {
        StringBuilder sb = new StringBuilder();
        for (char ch : txtText.getText().toCharArray()) {
            if (!(NON_ECHO_CHARACTERS.indexOf(ch) >= 0)) {
                sb.append(ch);
            }
        }
        txtPrimaryDefinition.setText(txtText.getText());
    }

    private void okButtonClicked() {
        editWordCommand = new EditWordCommand();

        editWordCommand.setWord(word);
        editWordCommand.setCommandType(getCommandType());
        editWordCommand.setVerseReference(verseReference);

        switch (editWordCommand.getCommandType()) {
            case EditText:
                editWordCommand.setPrimaryDefinition(txtPrimaryDefinition.getText());
                editWordCommand.setAltDefinition(txtAltDefinitions.getText());
                // falling through
            case InsertAfter:
            case InsertBefore:
                editWordCommand.setText(txtText.getText());
                break;

            case Move:
                int count = (Integer)txtCount.getValue();
                editWordCommand.setCount(count);
                break;
        }

        setVisible(false);
    }

    private EditWordCommand.CommandType getCommandType() {
        EditWordCommand.CommandType result = EditWordCommand.CommandType.EditText;

        if (btnInsertAfter.isSelected()) {
            result = EditWordCommand.CommandType.InsertAfter;
        } else if (btnInsertBefore.isSelected()) {
            result = EditWordCommand.CommandType.InsertBefore;
        } else if (btnMove.isSelected()) {
            result = EditWordCommand.CommandType.Move;
        }

        return result;
    }

    private void showCount(boolean showing) {
        lblCount.setVisible(showing);
        txtCount.setVisible(showing);
    }

    private void showDefinitions(boolean showing) {
        lblAltDefinitions.setVisible(showing);
        txtAltDefinitions.setVisible(showing);

        lblPrimaryDefinition.setVisible(showing);
        txtPrimaryDefinition.setVisible(showing);
    }

    private void clearText() {
        txtText.setText("");
        txtText.requestFocus();
        txtText.selectAll();
    }

    private void showText(boolean showing) {
        lblText.setVisible(showing);
        txtText.setVisible(showing);
    }

    public EditWordCommand getEditWordCommand() {
        return editWordCommand;
    }

    @Override
    public void insertUpdate(DocumentEvent e) {
        echoDefinition();
    }

    @Override
    public void removeUpdate(DocumentEvent e) {
        echoDefinition();
    }

    @Override
    public void changedUpdate(DocumentEvent e) {
        echoDefinition();
    }
}
