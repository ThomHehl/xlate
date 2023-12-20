package org.forerunnerintl.xlate.ui;

import org.forerunnerintl.xlate.text.DocumentNote;
import org.forerunnerintl.xlate.text.DocumentNoteType;
import org.forerunnerintl.xlate.text.VerseReference;
import org.forerunnerintl.xlate.text.osis.OsisHelper;
import org.forerunnerintl.xlate.text.osis.OsisNote;
import org.forerunnerintl.xlate.text.osis.OsisVerse;
import org.forerunnerintl.xlate.text.osis.OsisWord;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class NoteEditor extends JDialog
        implements ActionListener, ItemListener {
    public static final String      MSG_ENTER_REQUIRED = "Enter Required Fields";
    public static final String      TEXT_NOTE_FOR_VERSE = "Note for verse";
    public static final String      TEXT_NOTE_FOR_WORD = "Note for word:";
    public static final String      TEXT_NOTE_ID = "Note ID";
    public static final String      TEXT_NOTE_TYPE = "Note Type";

    private OsisVerse verse;
    private OsisWord word;

    private boolean updated;

    private JButton btnCancel;
    private JButton btnOk;
    private JLabel lblNoteId;
    private JTextField txtNoteId;
    private JLabel lblNoteType;
    private JComboBox<DocumentNoteType> cmbNoteType;
    private JTextArea txtNoteText;

    private JRadioButton radNoteForVerse;
    private JRadioButton radNoteForWord;

    public NoteEditor(JFrame owner, OsisVerse verse, OsisWord word) {
        super(owner, buildTitle(verse), true);
        this.verse = verse;
        this.word = word;

        setLocations(owner);
        setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));

        createRadioButtons();
        createInfoFields();
        createActionButtons();

        loadWordNote();
        loadVerseNote();

        setSize(UiConstants.DIALOG_SIZE);
        setVisible(true);
    }

    private void loadWordNote() {
        String noteId = word.getNoteId();
        if (noteId != null) {
            txtNoteId.setText(noteId);
            OsisNote note = OsisHelper.getNote(verse, noteId);
            txtNoteText.setText(note.getText());
            cmbNoteType.setSelectedItem(note.getType());
        }
    }

    private void loadVerseNote() {
        for (OsisNote note : verse.getOsisNotes()) {
            if (note.getNoteId() == null) {
                txtNoteId.setText("");
                txtNoteText.setText(note.getText());
                cmbNoteType.setSelectedItem(note.getType());
                break;
            }
        }
    }

    private static String buildTitle(OsisVerse verse) {
        StringBuilder sb = new StringBuilder("Edit Note for ");

        VerseReference verseReference = OsisHelper.getVerseRef(verse);
        sb.append(verseReference);

        return sb.toString();
    }

    private void setLocations(Frame owner) {
        Rectangle bounds = owner.getGraphicsConfiguration().getBounds();
        Dimension dimension = bounds.getSize();
        int x = (int) (((dimension.getWidth() - owner.getWidth()) / 2) + bounds.getMinX());
        int y = (int) (((dimension.getHeight() - owner.getHeight()) / 2) + bounds.getMinY());
        setLocation(x, y);
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

    private void createRadioButtons() {
        JPanel radioPanel = new JPanel(new GridLayout(2, 1));

        String text = TEXT_NOTE_FOR_WORD + word.getBodyText();
        radNoteForWord = new JRadioButton(text);
        radNoteForWord.setMnemonic(KeyEvent.VK_W);
        radNoteForWord.setActionCommand(TEXT_NOTE_FOR_WORD);
        radNoteForWord.setSelected(true);
        radioPanel.add(radNoteForWord);

        radNoteForVerse = new JRadioButton(TEXT_NOTE_FOR_VERSE);
        radNoteForVerse.setMnemonic(KeyEvent.VK_V);
        radNoteForVerse.setActionCommand(TEXT_NOTE_FOR_VERSE);
        radioPanel.add(radNoteForVerse);

        //Group the radio buttons.
        ButtonGroup group = new ButtonGroup();
        group.add(radNoteForWord);
        group.add(radNoteForVerse);

        //Register a listener for the radio buttons.
        radNoteForWord.addActionListener(this);
        radNoteForVerse.addActionListener(this);

        add(radioPanel);
    }

    private void createInfoFields() {
        JPanel textFieldPanel = new JPanel(new GridLayout(2, 2));

        lblNoteId = new JLabel(TEXT_NOTE_ID);
        textFieldPanel.add(lblNoteId);

        txtNoteId = new JTextField();
        textFieldPanel.add(txtNoteId);

        lblNoteType = new JLabel(TEXT_NOTE_TYPE);
        textFieldPanel.add(lblNoteType);

        cmbNoteType = new JComboBox<>(DocumentNoteType.values());
        cmbNoteType.addItemListener(this);
        textFieldPanel.add(cmbNoteType);

        add(textFieldPanel);

        txtNoteText = new JTextArea();
        txtNoteText.setLineWrap(true);
        txtNoteText.setWrapStyleWord(true);
        add(txtNoteText);
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        switch (event.getActionCommand()) {
            case UiConstants.TEXT_CANCEL:
                cancelButtonClicked();
                break;

            case UiConstants.TEXT_OK:
                okButtonClicked();
                break;

            case TEXT_NOTE_FOR_WORD:
                loadWordNote();
                showNoteId(true);
                break;

            case TEXT_NOTE_FOR_VERSE:
                loadVerseNote();
                showNoteId(false);
                break;

        }
    }

    private void cancelButtonClicked() {
        setVisible(false);
    }

    private void okButtonClicked() {
        boolean valid = false;

        String noteId = txtNoteId.getText();
        String noteText = txtNoteText.getText();
        if (radNoteForWord.isSelected()) {
            valid = !((noteId == null || noteId.equals(""))
                            || (noteText == null || noteText.equals("")));
            if (!valid) {
                showError(MSG_ENTER_REQUIRED, "Note ID and Note Text are required");
            }
        } else {
            noteId = "";
            valid = !(noteText == null || noteText.equals(""));
            if (!valid) {
                showError(MSG_ENTER_REQUIRED, "Note Text is required");
            }
        }

        if (valid) {
            updateNotes(noteId, noteText);
        }

        setVisible(!valid);
    }

    private void showError(String title, String msg) {
        JOptionPane.showMessageDialog(this,
                msg,
                title,
                JOptionPane.ERROR_MESSAGE);
    }

    private void updateNotes(String noteId, String noteText) {
        if (radNoteForWord.isSelected()) {
            OsisNote note = OsisHelper.getNote(verse, noteId);

            if (note == null) {
                note = buildNote();
                verse.getOsisNotes().add(note);
                word.setNoteId(noteId);
                updated = true;
            } else {
                String text = note.getText();
                if (text == null) {
                    text = "";
                }
                text = text.trim();
                if (!text.equals(txtNoteText.getText())){
                    note.setText(text);
                    updated = true;
                }
            }
        } else {
            DocumentNoteType noteType = (DocumentNoteType) cmbNoteType.getSelectedItem();
            OsisNote note = OsisHelper.getNote(verse, noteType);
            if (note == null) {
                note = buildNote();
                verse.getOsisNotes().add(note);
                updated = true;
            } else {
                String text = note.getText();
                text = text.trim();
                note.setText(text);
                updated = true;
            }
        }
    }

    private OsisNote buildNote() {
        OsisNote result = new OsisNote();

        result.setType((DocumentNoteType) cmbNoteType.getSelectedItem());
        result.setOsisRef(verse.getUniqueId());
        result.setOsisId(buildOsisId());

        String text = txtNoteText.getText();
        if (text == null) {
            text = "";
        }
        text = text.trim();
        result.setText(text);

        if (radNoteForWord.isSelected()) {
            result.setNoteId(txtNoteId.getText());
        }

        return  result;
    }

    private String buildOsisId() {
        StringBuilder sb = new StringBuilder(verse.getUniqueId());
        sb.append(DocumentNote.REFERENCE_SEPARATOR);

        if (radNoteForWord.isSelected()) {
            sb.append(DocumentNote.NOTE_SEPARATOR);
            sb.append(txtNoteId.getText());
        } else {
            sb.append(cmbNoteType.getSelectedItem());
        }

        return sb.toString();
    }

    private void showNoteId(boolean showing) {
        lblNoteId.setVisible(showing);
        txtNoteId.setVisible(showing);
    }

    @Override
    public void itemStateChanged(ItemEvent event) {
        if (event.getStateChange() == ItemEvent.SELECTED) {
            DocumentNoteType noteType = (DocumentNoteType) event.getItem();
            OsisNote note;
            if (radNoteForWord.isSelected()) {
                String noteId = txtNoteId.getText();
                if (noteId != null) {
                    note = OsisHelper.getNote(verse, noteId);
                } else {
                    note = null;
                }
            } else {
                note = OsisHelper.getNote(verse, noteType);
            }

            loadFromNote(note);
        }
    }

    private void loadFromNote(OsisNote note) {
        if (note == null) {
            if (radNoteForVerse.isSelected()) {
                txtNoteId.setText("");
            }
            txtNoteText.setText("");
        } else {
            txtNoteId.setText(note.getNoteId());
            txtNoteText.setText(note.getText());
        }
    }

    public boolean isUpdated() {
        return updated;
    }
}
