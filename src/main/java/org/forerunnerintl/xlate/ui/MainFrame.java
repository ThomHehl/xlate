package org.forerunnerintl.xlate.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.KeyEvent;

public class MainFrame extends JFrame implements ComponentListener {

    private final MainEditorPane mainEditorPanel;

    private JMenuBar menuBar;
    private JMenu menuFile;
    private JMenuItem mnuitmOpen;

    public MainFrame() {
        buildMenu();

        setTitle("Xlate -- Bible Translation Assistant");
        setLocations(this);

        mainEditorPanel = new MainEditorPane(this);
        add(mainEditorPanel, BorderLayout.CENTER);

        addComponentListener(this);
    }

    private void setLocations(MainFrame frame) {
        Rectangle bounds = frame.getGraphicsConfiguration().getBounds();
        Dimension dimension = bounds.getSize();
        int x = (int) (((dimension.getWidth() - frame.getWidth()) / 2) + bounds.getMinX());
        int y = (int) (((dimension.getHeight() - frame.getHeight()) / 2) + bounds.getMinY());
        setLocation(x, y);
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
