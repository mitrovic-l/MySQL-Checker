package controller.actions;

import database.MYSQLrepository;
import database.checker.rules.CSVRule;
import gui.MainFrame;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.io.*;

public class BulkImportAction extends AbstractAction {


    public BulkImportAction() {
        putValue(NAME, "BulkImport");
        putValue(SHORT_DESCRIPTION, "Bulk");
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        JFileChooser jFileChooser=new JFileChooser();
        jFileChooser.showOpenDialog(null);
       // System.out.println(jFileChooser.getSelectedFile().getName().toString());
        //OTKOMENTARISATI KADA SE PROVERE SVI USLOVI...
       String name = jFileChooser.getSelectedFile().getName();
       MainFrame.getInstance().getAppCore().setFile(jFileChooser.getSelectedFile());
        CSVRule csvRule = new CSVRule();
        csvRule.check();
        if (!(name.contains(".csv"))){
            System.out.println("Selektovani fajl nije .CSV!");
            return;
        }
        MainFrame.getInstance().getAppCore().setFile(jFileChooser.getSelectedFile());
        MainFrame.getInstance().getAppCore().getMysqLrepository().bulk(jFileChooser.getSelectedFile());



    }
}
