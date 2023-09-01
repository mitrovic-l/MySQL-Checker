package database.checker.rules;
import database.checker.Rule;
import gui.MainFrame;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

public class OrderRule implements Rule {

    HashMap<String, Integer>redosled = new HashMap<>();

    void ubacivanje(){
        this.redosled.put("SELECT", 1);
        this.redosled.put("UPDATE", 1);
        this.redosled.put("INSERT", 1);
        this.redosled.put("DELETE", 1);
        this.redosled.put("FROM", 2);
        this.redosled.put("SET", 2);
        this.redosled.put("INTO", 2);
        this.redosled.put("WHERE", 3);
        this.redosled.put("VALUES", 3);
        this.redosled.put("GROUP", 4);
        this.redosled.put("BY", 5);
        this.redosled.put("HAVING", 5);
    }

    @Override
    public String check() {
        ubacivanje();
        String query = MainFrame.getInstance().getMainPanel().getTekstSQL().getText().replaceAll("\\r|\\n", "");
        String[] reci = query.split(" ", 0);
        Integer[] prioriteti = new Integer[10];
        ArrayList<String>kljucneReci = MainFrame.getInstance().getMainPanel().getSqlReci();
        int brojac = 0;
        for (String s : reci){
            if (kljucneReci.contains(s.toUpperCase(Locale.ROOT))){
                String pom = s.toUpperCase();
                System.out.println("Dodato u nizP");
                prioriteti[brojac]=this.redosled.get(pom);
                System.out.println(prioriteti[brojac]);
                brojac++;
            }
        }
        System.out.println(prioriteti.length);
        boolean rasporedjeno = true;
        for (int i =0, j=1;j< prioriteti.length-1;i++, j++){
            if (prioriteti[j]==null)
                break;
            if (prioriteti[i]>prioriteti[j]){
                rasporedjeno = false;
            }
        }
        if (rasporedjeno){
           return null;
        } else {
            return "ORDER";
        }
    }
}
