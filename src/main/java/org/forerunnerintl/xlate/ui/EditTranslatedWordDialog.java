package org.forerunnerintl.xlate.ui;

import org.forerunnerintl.xlate.controller.EditWordCommand;
import org.forerunnerintl.xlate.note.TranslationEntry;
import org.forerunnerintl.xlate.text.VerseReference;
import org.forerunnerintl.xlate.text.osis.OsisWord;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.*;
import java.util.concurrent.ExecutionException;

public class EditTranslatedWordDialog extends JDialog
        implements ActionListener, DocumentListener, FocusListener {
    private static final String     NON_ECHO_CHARACTERS = ".,:;";
    private static final String     TEXT_ALT_DEFINITIONS = "Alternate Definitions";
    private static final String     TEXT_COUNT = "Count";
    private static final String     TEXT_ECHO_DEFINITION = "Echo Definition";
    private static final String     TEXT_EDIT_TEXT = "Edit text";
    private static final String     TEXT_INSERT_AFTER = "Insert after";
    private static final String     TEXT_INSERT_BEFORE = "Insert before";
    private static final String     TEXT_PRIMARY_DEFINITION = "Primary Definition";
    private static final String     TEXT_TEXT = "Text";
    private static final String     TITLE = "Edit word:";

    private JButton btnCancel;
    private JButton btnOk;
    private JRadioButton btnEditText;
    private JRadioButton btnInsertBefore;
    private JRadioButton btnInsertAfter;
    private JLabel lblAltDefinitions;
    private JTextField txtAltDefinitions;
    private JLabel lblPrimaryDefinition;
    private JTextField txtPrimaryDefinition;
    private JLabel lblText;
    private JTextField txtText;

    private EditWordCommand editWordCommand;
    private String altDefinition;
    private String primaryDefinition;
    private boolean echoingDefintion;
    private VerseReference verseReference;
    private OsisWord word;

    public EditTranslatedWordDialog(Frame owner, EditWordCommand editWordCommand) {
        super(owner, buildTitle(editWordCommand.getWord()), true);
        this.verseReference = editWordCommand.getVerseReference();
        this.word = editWordCommand.getWord();

        setLocations(owner);

        setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
        setTranslationEntry(editWordCommand);
        createRadioButtonGroup();
        createTextControls();
        createActionButtons();

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
            primaryDefinition = "";
            altDefinition = "";
            echoingDefintion = true;
        } else {
            String primary = translationEntry.getPrimary();
            if (primary == null) {
                primaryDefinition = "";
                echoingDefintion = true;
            } else {
                primaryDefinition = primary;
                String bodyText = editWordCommand.getWord().getBodyText();
                echoingDefintion = primary.equals(bodyText);
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

        //Group the radio buttons.
        ButtonGroup group = new ButtonGroup();
        group.add(btnEditText);
        group.add(btnInsertBefore);
        group.add(btnInsertAfter);

        //Register a listener for the radio buttons.
        btnEditText.addActionListener(this);
        btnInsertBefore.addActionListener(this);
        btnInsertAfter.addActionListener(this);

        add(radioPanel);
    }

    private void createTextControls() {
        JPanel textControlsPanel = new JPanel(new GridLayout(4, 2));

        lblText = new JLabel(TEXT_TEXT);
        textControlsPanel.add(lblText);

        txtText = new JTextField();
        txtText.addFocusListener(this);
        if (echoingDefintion) {
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

        add(textControlsPanel);

        showText(true);
        showDefinitions(true);
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
                break;

            case TEXT_INSERT_AFTER:
            case TEXT_INSERT_BEFORE:
                showText(true);
                clearText();
                showDefinitions(false);
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
            int pos = NON_ECHO_CHARACTERS.indexOf(ch);
            if (pos < 0) {
                sb.append(ch);
            }
        }
        txtPrimaryDefinition.setText(sb.toString());
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
        }

        setVisible(false);
    }

    private EditWordCommand.CommandType getCommandType() {
        EditWordCommand.CommandType result = EditWordCommand.CommandType.EditText;

        if (btnInsertAfter.isSelected()) {
            result = EditWordCommand.CommandType.InsertAfter;
        } else if (btnInsertBefore.isSelected()) {
            result = EditWordCommand.CommandType.InsertBefore;
        }

        return result;
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

    @Override
    public void focusGained(FocusEvent event) {
        JTextField textfield = (JTextField) event.getComponent();
        textfield.selectAll();
    }

    @Override
    public void focusLost(FocusEvent e) {

    }
}
