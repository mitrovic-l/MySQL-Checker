package controller.actions;

import gui.MainFrame;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class ResultSetAction extends AbstractAction {

    public ResultSetAction() {
        putValue(NAME, "ResultSet");
        putValue(SHORT_DESCRIPTION, "Result");

    }


    @Override
    public void actionPerformed(ActionEvent e) {

        JFileChooser jFileChooser=new JFileChooser();
        jFileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        jFileChooser.showOpenDialog(null);
        String putanja=jFileChooser.getSelectedFile().getAbsolutePath();
        System.out.println(jFileChooser.getSelectedFile().getAbsolutePath());
        MainFrame.getInstance().getAppCore().getMysqLrepository().export(putanja);


    }
}
