package database.checker.rules;

import database.checker.Rule;
import gui.MainFrame;

import java.util.ArrayList;
import java.util.Locale;

public class AliasRule implements Rule {
    @Override
    public String check() {
        //select last_name as "prezime zaposlenog"
        ArrayList<String>kljucneReci = MainFrame.getInstance().getMainPanel().getSqlReci();
        String query = MainFrame.getInstance().getMainPanel().getTekstSQL().getText().replaceAll("\\r|\\n", "");
        query.replaceAll("\n", " ");
        boolean asFlag = false;
        boolean recNakon = false;
        boolean greska = false;
        String recNakonAliasa = "";
        String alias = "";
        String[] reci = query.split(" ", 0);
        for (String string : reci){
            if (string.equalsIgnoreCase("AS")){
                asFlag = true;
                continue;
            }
            if (asFlag && !recNakon){
                alias = string;
                recNakon = true;
                continue;
            }
            if (asFlag && recNakon){
                recNakonAliasa = string;
                break;
            }
        }
        if (asFlag && alias!=""){
            if (alias.contains("\"")){
                greska = false;
            }
            else if (kljucneReci.contains(recNakonAliasa.toUpperCase(Locale.ROOT))|| recNakonAliasa.contains("\"")){
                greska = false;
            }
            else
                greska = true;

        }
        if(greska)return "ALIAS";
        else return null;
    }
}
