package gui;

import javax.swing.*;
import javax.swing.event.ChangeListener;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Locale;

public class MainPanel extends JPanel {

    private JTextPane tekstSQL=new JTextPane();
    private JTable jTable = new JTable();
    private JPanel tablePanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0,0));
    private ArrayList<String> sqlReci;

    public MainPanel(JTable jTable) {
     /*   tekstSQL.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                //tekstSQL.setText(tekstSQL.getText());
                super.keyTyped(e);


            }

            @Override
            public void keyPressed(KeyEvent e) {
               // tekstSQL.setText(tekstSQL.getText());
                super.keyPressed(e);
            }

            @Override
            public void keyReleased(KeyEvent e) {
                tekstSQL.setText(tekstSQL.getText());
                super.keyReleased(e);
            }
        });

      */


        JScrollPane jsp = new JScrollPane(tekstSQL);
        jsp.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        jsp.setPreferredSize(new Dimension(300, 100));

    tekstSQL.setPreferredSize(new Dimension(300,100));

    tablePanel.setPreferredSize(new Dimension(300, 100));
    jTable.setPreferredSize(new Dimension(800, 300));
    jTable.setVisible(true);
    tablePanel.setVisible(true);
    this.setLayout(new BorderLayout(6,6));
    this.setPreferredSize(new Dimension(600,400));
    this.add(jsp,BorderLayout.NORTH);
    this.setVisible(true);
    sqlReci = new ArrayList<>();
    addReci();

    }
    public void addReci(){
        sqlReci.add("SELECT");
        sqlReci.add("FROM");
        sqlReci.add("UPDATE");
        sqlReci.add("WHERE");
        sqlReci.add("HAVING");
        sqlReci.add("GROUP");
        sqlReci.add("BY");
        sqlReci.add("JOIN");
        sqlReci.add("RIGHT");
        sqlReci.add("LEFT");
        sqlReci.add("ON");
        sqlReci.add("INSERT");
        sqlReci.add("IN");
        sqlReci.add("DELETE");
        sqlReci.add("EXEC");
        sqlReci.add("CREATE");
        sqlReci.add("LIKE");
        sqlReci.add("AS");
        sqlReci.add("BETWEEN");
        sqlReci.add("AND");

        ////////////////////
        sqlReci.add("\nSELECT");
        sqlReci.add("\nFROM");
        sqlReci.add("\nUPDATE");
        sqlReci.add("\nWHERE");
        sqlReci.add("\nHAVING");
        sqlReci.add("\nGROUP");
        sqlReci.add("\nBY");
        sqlReci.add("\nJOIN");
        sqlReci.add("\nRIGHT");
        sqlReci.add("\nLEFT");
        sqlReci.add("\nON");
        sqlReci.add("\nINSERT");
        sqlReci.add("\nIN");
        sqlReci.add("\nDELETE");
        sqlReci.add("\nEXEC");
        sqlReci.add("\nCREATE");
        sqlReci.add("\nLIKE");
        sqlReci.add("/nAS");
        sqlReci.add("/nBETWEEN");
        sqlReci.add("/nAND");




    }

    public void doPretty() throws BadLocationException {

        this.getTekstSQL().getText().replaceAll("\n", " ");
        StyledDocument styledDocument = this.getTekstSQL().getStyledDocument();
        String[] reci = styledDocument.getText(0, styledDocument.getLength()).split(" ");
        Style style = this.getTekstSQL().addStyle("test", null);
        Style pom = this.getTekstSQL().addStyle("obican", null);
        this.getTekstSQL().setText("");
        int brojac = 0;
        for (String string : reci){
            string.replaceAll(" ", "");

            string.replaceAll("(?m)^[ \t]*\r?\n", "");
            string.replace("\\n", "");
            string.replace("\\r", "");
            string.replace("\n", "");
            if (string.contains("\n")){
                System.out.println("Sadrzi novi red!...");
            }
            //SELECT \nSelect
            System.out.println(string);
            if (this.sqlReci.contains(string.toUpperCase(Locale.ROOT))){
                StyleConstants.setForeground(style, Color.BLUE);
                try {
                    if (brojac!=0 && !string.contains("\n")){
                        styledDocument.insertString(styledDocument.getLength(), "\n", pom);
                    }
                    styledDocument.insertString(styledDocument.getLength(), string.toUpperCase(), style);
                } catch (BadLocationException e) {
                    throw new RuntimeException(e);
                }
            } else {
                StyleConstants.setForeground(pom, Color.BLACK);
                try {
                    styledDocument.insertString(styledDocument.getLength(), string, pom);
                } catch (BadLocationException e) {
                    throw new RuntimeException(e);
                }
            }
            styledDocument.insertString(styledDocument.getLength(), " ", pom);

            this.repaint();
            this.revalidate();
            brojac++;
        }
        this.getTekstSQL().removeStyle("test");
        this.getTekstSQL().setLogicalStyle(pom);


    }


    public JTextPane getTekstSQL() {
        return tekstSQL;
    }

    public void setTekstSQL(JTextPane tekstSQL) {
        this.tekstSQL = tekstSQL;
    }



    public ArrayList<String> getSqlReci() {
        return sqlReci;
    }

    public void setSqlReci(ArrayList<String> sqlReci) {
        this.sqlReci = sqlReci;
    }

    public JTable getjTable() {
        return jTable;
    }

    public void setjTable(JTable jTable) {
        this.jTable = jTable;
    }

    public JPanel getTablePanel() {
        return tablePanel;
    }

    public void setTablePanel(JPanel tablePanel) {
        this.tablePanel = tablePanel;
    }
}

