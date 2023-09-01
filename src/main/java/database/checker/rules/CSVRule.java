package database.checker.rules;

import app.Main;
import database.checker.Rule;
import gui.MainFrame;

import java.io.*;
import java.util.ArrayList;

public class CSVRule implements Rule {


    @Override
    public String check() {

        try {
            BufferedReader bufferedReader=new BufferedReader(new FileReader(MainFrame.getInstance().getAppCore().getFile()));
            String tabela = MainFrame.getInstance().getjTree().getLastSelectedPathComponent().toString();
            ArrayList<String>kolone=new ArrayList<>();
            kolone=MainFrame.getInstance().getAppCore().getMysqLrepository().getKolone(tabela);
            String line="";
            line=bufferedReader.readLine();
            line.replaceAll("\\(", "").replaceAll("\\)","").replaceAll(" ","");
            String[] strings=line.split(",",0);
            for(String s:strings){
                System.out.println("KOLONE U CSV: ");
                System.out.println(s);
                s.replaceAll(" ", "");
                s.replaceAll(",", "");
                s.replaceAll("\\(", "");
                s.replaceAll("\\)", "");
                if(s!=null&&!kolone.contains(s)){
                    System.out.println("NE POSTOJI KOLONA /CSVRULE");
                    return "CSV_INCORRECT";
                }
                
            }
            return null;
            //Ako ima samo brojeve onda je INTEGER
            //Ako ima brojeve i jednu tacku FLOAT
            //Ako ima dve tacke ili dve / onda je DATE
            //Ako ima samo slova onda je VARCHAR

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


        return null;

    }
}
