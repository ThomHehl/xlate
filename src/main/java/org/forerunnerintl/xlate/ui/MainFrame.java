package org.forerunnerintl.xlate.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class MainFrame extends JFrame implements ComponentListener {

    private MainEditorPane mainEditorPanel;

    private JMenuBar menuBar;
    private JMenu menuFile;
    private JMenuItem mnuitmOpen;

    public MainFrame() {
        buildMenu();

        setTitle("Xlate -- Bible Translation Assistant");

        mainEditorPanel = new MainEditorPane();
        add(mainEditorPanel, BorderLayout.CENTER);

        addComponentListener(this);
    }

    private void buildMenu() {
        menuBar = new JMenuBar();

        menuFile = new JMenu("File");
        menuFile.setMnemonic(KeyEvent.VK_F);

        mnuitmOpen = new JMenuItem("Open...");
        menuFile.add(mnuitmOpen);
        menuBar.add(menuFile);

        setJMenuBar(menuBar);
    }

    @Override
    public void componentResized(ComponentEvent e) {
        Dimension dimension = getSize();
        mainEditorPanel.resizeComponents(dimension.width, dimension.height);
    }

    @Override
    public void componentMoved(ComponentEvent e) {

    }

    @Override
    public void componentShown(ComponentEvent e) {

    }

    @Override
    public void componentHidden(ComponentEvent e) {

    }
}
