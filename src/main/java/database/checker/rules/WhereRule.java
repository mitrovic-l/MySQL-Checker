package database.checker.rules;

import database.checker.Rule;
import gui.MainFrame;

import java.util.Locale;

public class WhereRule implements Rule{

    @Override
    public String check() {
        String query = MainFrame.getInstance().getMainPanel().getTekstSQL().getText().replaceAll("\\r|\\n", "");
        query.replaceAll("\n", " ");
        String strCount = "COUNT";
        String strMax = "MAX";
        String strMin = "MIN";
        String strAvg = "AVG";
        String strSum = "SUM";
        String[] reci = query.split(" ", 0);
        boolean greska = false;
        boolean agregacijaWhere = false;
        boolean where = false;
        String nakonWhere = "";
        for (String string : reci){
            if (string.equalsIgnoreCase("WHERE")){
                where = true;
                continue;
            }
            if (where){
                nakonWhere = string;
                break;
            }
        }
        if (where && nakonWhere!=""){
            if (nakonWhere.contains(strCount)||nakonWhere.contains(strAvg)||nakonWhere.contains(strMax)||nakonWhere.contains(strMin)||
            nakonWhere.contains(strSum) || nakonWhere.contains(strCount.toLowerCase(Locale.ROOT))|| nakonWhere.contains(strAvg.toLowerCase(Locale.ROOT))
                    || nakonWhere.contains(strMax.toLowerCase(Locale.ROOT))|| nakonWhere.contains(strMin.toLowerCase(Locale.ROOT))
                    || nakonWhere.contains(strSum.toLowerCase(Locale.ROOT))){
                greska = true;
            }
        }
        if(greska)return  "WHERE";
        else return null;
    }
}
