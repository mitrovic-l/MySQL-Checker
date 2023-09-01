package controller.actions;

import database.checker.rules.AliasRule;
import database.checker.rules.ExistRule;
import database.checker.rules.OrderRule;
import database.checker.rules.WhereRule;
import gui.MainFrame;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class RunAction extends AbstractAction {

    public RunAction() {
        putValue(NAME, "Run");
        putValue(SHORT_DESCRIPTION, "Run");
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        MainFrame.getInstance().getAppCore().run();
    }
}
