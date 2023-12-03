package org.forerunnerintl.xlate.ui;

import org.forerunnerintl.xlate.text.TextFormat;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class NewProjectDialog extends JDialog {
    private static final String     FORMAT_NT = "New Testament Format";
    private static final String     FORMAT_OT = "Old Testament Format";
    private static final String     FORMAT_OSIS = "OSIS";
    private static final String     FORMAT_SBL = "SBL";
    private static final String[]   FORMAT_NAMES = {FORMAT_OSIS, FORMAT_SBL};
    private static final String     LABEL_TITLE = "Title";
    private static final String     TITLE = "New Project";

    private boolean creating;
    private TextFormat newTestament;
    private TextFormat oldTestament;
    private String title;

    private JComboBox<String> cmbNt;
    private JComboBox<String> cmbOt;
    private JTextField txtTitle;

    public NewProjectDialog(JFrame owner) {
        super(owner, TITLE, true);
        setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));

        addSourceTextDropDowns();
        addButtons();

        setSize(UiConstants.DIALOG_SIZE);
        setVisible(true);
    }

    private void addButtons() {
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout());

        JButton btnCancel = new JButton(UiConstants.TEXT_CANCEL);
        btnCancel.setPreferredSize(UiConstants.BUTTON_SIZE);
        btnCancel.addActionListener(e -> cancelButtonClicked());
        panel.add(btnCancel);

        JButton btnCreate = new JButton(UiConstants.TEXT_CREATE);
        btnCreate.setPreferredSize(UiConstants.BUTTON_SIZE);
        btnCreate.addActionListener(e -> createButtonClicked());
        panel.add(btnCreate);

        add(panel);
    }

    private void addSourceTextDropDowns() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(3,2));

        panel.add(new Label(LABEL_TITLE));
        txtTitle = new JTextField();
        panel.add(txtTitle);

        panel.add(new Label(FORMAT_OT));
        cmbOt = new JComboBox<>(FORMAT_NAMES);
        cmbOt.setSelectedItem(FORMAT_OSIS);
        panel.add(cmbOt);

        panel.add(new Label(FORMAT_NT));
        cmbNt = new JComboBox<>(FORMAT_NAMES);
        cmbNt.setSelectedItem(FORMAT_SBL);
        panel.add(cmbNt);

        add(panel);
    }

    public void cancelButtonClicked() {
        creating = false;
        setVisible(false);
    }

    public void createButtonClicked() {
        creating = true;
        newTestament = getSelectedTextFormat(cmbNt);
        oldTestament = getSelectedTextFormat(cmbOt);
        title = txtTitle.getText();
        setVisible(false);
    }

    private TextFormat getSelectedTextFormat(JComboBox<String> cmb) {
        String selection = (String) cmb.getSelectedItem();

        TextFormat result =
                switch (selection) {
                    case FORMAT_OSIS -> TextFormat.OSIS;
                    case FORMAT_SBL -> TextFormat.SBL;
                    default -> throw new IllegalStateException("Invalid text format: " + selection);
                };

        return result;
    }

    public TextFormat getNewTestament() {
        return newTestament;
    }

    public TextFormat getOldTestament() {
        return oldTestament;
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isCreating() {
        return creating;
    }
}
