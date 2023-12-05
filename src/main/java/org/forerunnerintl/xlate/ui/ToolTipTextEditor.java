package org.forerunnerintl.xlate.ui;

import javax.swing.*;
import java.awt.event.MouseEvent;

public class ToolTipTextEditor extends JEditorPane {
    public static final String  RICH_TEXT = "text/rtf";

    final private ToolTipProvider toolTipProvider;

    public ToolTipTextEditor(ToolTipProvider toolTipProvider) {
        super(RICH_TEXT, "");
        this.toolTipProvider = toolTipProvider;

    }

    /**
     * get the ToolTipText for the given mouse event
     * @param event - the mouse event to handle
     */
    @Override
    public String getToolTipText(MouseEvent event) {
        // convert the mouse position to the offset
        int offset = viewToModel2D(event.getPoint());
        return toolTipProvider.getToolTipText(offset);
    }
}
