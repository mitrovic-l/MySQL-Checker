package database.checker.rules;

import database.checker.Rule;
import gui.MainFrame;

import java.util.ArrayList;
import java.util.Locale;

public class GroupByRule implements Rule {
    //Ukoliko se u select-u nalazi funkcija agregacije i pored nje neka kolona,
    //ta kolona mora biti smestena u group by, u suprotnom vrati gresku,
    //npr: select max(salary), department_id from hr.employees group by department_id
    @Override
    public String check() {

        String query = MainFrame.getInstance().getMainPanel().getTekstSQL().getText().replaceAll("\\r|\\n", "");
        if (query.contains("group by")|| query.contains("GROUP BY")) {
            ArrayList<String> kljucne = MainFrame.getInstance().getMainPanel().getSqlReci();
            String[] reci = query.split(" ");
            String[] kolone = new String[32];
            int brojKolonaBezAgr = 0;
            boolean agregacija = query.contains("avg") || query.contains("sum") || query.contains("count")
                    || query.contains("min") || query.contains("max");
            if (reci[0].equalsIgnoreCase("SELECT") && agregacija) {
                for (int i = 1, j = 0; i < reci.length && !kljucne.contains(reci[i].toUpperCase(Locale.ROOT)); i++) {
                    reci[i].replaceAll(",", "");
                    // u if ulaze samo kolone koje nisu pod funkcijama agregacije,
                    //tj. one koje moraju biti u GROUP BY
                    if (!reci[i].contains("avg") && !reci[i].contains("sum") && !reci[i].contains("count") &&
                            !reci[i].contains("min") && !reci[i].contains("max")) {
                        System.out.println("DODAO SAM KOLONU BEZ AGREGACIJE U NIZ");
                        brojKolonaBezAgr++;
                        kolone[j++] = reci[i];
                    }
                }
                if (brojKolonaBezAgr > 0 && !query.contains("group by")) {
                    return "GROUP_BY";
                }
                int ostatak = -1;
                ostatak = query.lastIndexOf("group");
                if (ostatak != -1 && brojKolonaBezAgr > 0) {
                    String pomocni = query.substring(ostatak);
                    boolean flag = true;
                    for (int i = 0; i < kolone.length && pomocni != "" && kolone[i] != null; i++) {
                        if (!pomocni.contains(kolone[i])) {
                            flag = false;
                            break;
                        }
                    }
                    if (!flag) {
                        return "GROUP_BY";
                    }

                } else if (ostatak == -1 && brojKolonaBezAgr == 0) {
                    return null;
                }


            } else {
                return null;
            }
        }

        return null;
    }
}
