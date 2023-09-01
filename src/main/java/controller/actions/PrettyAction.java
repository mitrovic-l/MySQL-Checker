package controller.actions;

import gui.MainFrame;
import gui.MainPanel;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Locale;

public class PrettyAction extends AbstractAction {

    public PrettyAction() {
        putValue(NAME, "Pretty");
        putValue(SHORT_DESCRIPTION, "Pretty");
    }

    @Override
    public void actionPerformed(ActionEvent e) {

       /* MainPanel mainPanel = MainFrame.getInstance().getMainPanel();
        JTextPane sqlArea = mainPanel.getTekstSQL();
        String textSQL = sqlArea.getText();
        String[] sqlWords = textSQL.split(" ", 0);
        StyledDocument doc = sqlArea.getStyledDocument();
        sqlArea.setText("");
        Style style = sqlArea.addStyle("", null);


        for (String s : sqlWords){
            if(s.equalsIgnoreCase("\n"))continue;
            if (mainPanel.getSqlReci().contains(s.toUpperCase(Locale.ROOT))){
                String pom = s.toUpperCase();

                StyleConstants.setForeground(style, Color.BLUE);
                StyleConstants.setBackground(style, Color.white);
                try {
                    if(!sqlArea.getText().isEmpty()){
                        doc.insertString(doc.getLength(), "\n", style);
                    }
                    doc.insertString(doc.getLength(), pom, style);
                    doc.insertString(doc.getLength(), " ", style);
                } catch (BadLocationException ex) {
                    throw new RuntimeException(ex);
                }
            } else {

                //Style style = sqlArea.addStyle("", null);
                StyleConstants.setForeground(style, Color.BLACK);
                StyleConstants.setBackground(style, Color.white);
                try {
                    doc.insertString(doc.getLength(), s, style);
                    doc.insertString(doc.getLength(), " ", style);
                } catch (BadLocationException ex) {
                    throw new RuntimeException(ex);
                }

            }


        }


        StyleConstants.setForeground(style, Color.BLACK);
        StyleConstants.setBackground(style, Color.white);
        MainFrame.getInstance().repaint();
        MainFrame.getInstance().revalidate();
       // mainPanel.getTekstSQL().getText().
       */
        try {
            MainFrame.getInstance().getMainPanel().doPretty();
        } catch (BadLocationException ex) {
            throw new RuntimeException(ex);
        }
    }
}
