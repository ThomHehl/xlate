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
    private static final String     TITLE = "New Project";

    private boolean creating;
    private TextFormat newTestament;
    private TextFormat oldTestament;

    private JComboBox<String> cmbNt;
    private JComboBox<String> cmbOt;

    public NewProjectDialog(JFrame owner) {
        super(owner, TITLE, true);
        setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));

        addSourceTextDropDowns();
        addButtons();

        setSize(Constants.DIALOG_SIZE);
        setVisible(true);
    }

    private void addButtons() {
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout());

        JButton btnCancel = new JButton(Constants.TEXT_CANCEL);
        btnCancel.setPreferredSize(Constants.BUTTON_SIZE);
        btnCancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cancelButtonClicked();
            }
        });
        panel.add(btnCancel);

        JButton btnCreate = new JButton(Constants.TEXT_CREATE);
        btnCreate.setPreferredSize(Constants.BUTTON_SIZE);
        btnCreate.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                createButtonClicked();
            }
        });
        panel.add(btnCreate);

        add(panel);
    }

    private void addSourceTextDropDowns() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(2,2));

        panel.add(new Label(FORMAT_OT));
        cmbOt = new JComboBox<String>(FORMAT_NAMES);
        cmbOt.setSelectedItem(FORMAT_OSIS);
        panel.add(cmbOt);

        panel.add(new Label(FORMAT_NT));
        cmbNt = new JComboBox<String>(FORMAT_NAMES);
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

    public boolean isCreating() {
        return creating;
    }
}
