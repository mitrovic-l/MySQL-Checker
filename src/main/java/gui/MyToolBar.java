package gui;

import javax.swing.*;

public class MyToolBar extends JToolBar {

    public MyToolBar() {
        super(HORIZONTAL);
        setFloatable(false);
        add(MainFrame.getInstance().getActionManager().getPrettyAction());
        addSeparator();
        add(MainFrame.getInstance().getActionManager().getBulkImportAction());
        addSeparator();
        add(MainFrame.getInstance().getActionManager().getResultSetAction());
        addSeparator();
        add(MainFrame.getInstance().getActionManager().getRunAction());
        addSeparator();
        setVisible(true);

    }
}
