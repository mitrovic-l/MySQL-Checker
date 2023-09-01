package database.checker.rules;

import database.checker.Rule;
import gui.MainFrame;

import java.util.ArrayList;

public class ForeignKeyRule implements Rule {

    //Select last_name, first_name, department_name from hr.employees e
    // join hr.departments d on (e.department_name = d.department_name)
    @Override
    public String check() {
        String query = MainFrame.getInstance().getMainPanel().getTekstSQL().getText().replaceAll("\\r|\\n", "");
        if(query.contains("join ")|| query.contains("JOIN ")) {
            String[] reci = query.split(" ");
            String t = "";
            boolean tabela = false;
            for (String s : reci) {
                if (s.equalsIgnoreCase("FROM")) {
                    tabela = true;
                    continue;
                } else if (tabela) {
                    t = s;
                    break;
                }
            }
            ArrayList<String>straniKljucevi = MainFrame.getInstance().getAppCore().getMysqLrepository().fkHelper(t);
            if (straniKljucevi.isEmpty()){
                return "NOT_FK";
            }
            String[] pom = query.split("on");
            if (pom[1]==null){
                return "NOT_FK";
            }
            String kljuc = pom[1].replaceAll("\\(", "").replaceAll("\\)", "");
            String[] levi = kljuc.split("=");
            String provera = levi[0].replaceAll(" ", "");
            if (!straniKljucevi.contains(provera)){
                return "NOT_FK";
            } else {
                return null;
            }


        }
        return null;
    }
}
