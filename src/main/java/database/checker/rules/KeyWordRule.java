package database.checker.rules;

import database.checker.Rule;
import gui.MainFrame;

import java.util.HashMap;
import java.util.Locale;

public class KeyWordRule implements Rule {

    private HashMap<String, String>obavezneReci = new HashMap<String, String>();
    //Svaka pocetna rec ima svoju obavezujucu kljucnu rec..

    void setMap(){
        obavezneReci.put("SELECT", "FROM");
        obavezneReci.put("INSERT", "INTO");
        obavezneReci.put("UPDATE", "SET");
        obavezneReci.put("DELETE", "FROM");
    }

    @Override
    public String check() {
        this.setMap();
        String query =MainFrame.getInstance().getMainPanel().getTekstSQL().getText().replaceAll("\\r|\\n", "");
        String[] reci = query.split(" ");
        String pom = reci[0].toUpperCase();
        boolean firstWord = false;
        boolean flag = false;
        if (MainFrame.getInstance().getMainPanel().getSqlReci().contains(pom.toUpperCase(Locale.ROOT))){
            firstWord = true;
        }
        if (firstWord) {
            String pom2 = obavezneReci.get(pom);

            for (int i = 1; i < reci.length; i++) {
                if (reci[i].equalsIgnoreCase(pom2)) {
                    flag = true;
                    break;
                }
            }
        }
        if (!flag){
            return "MISSING_PART";
        }

        return null;

    }
}
