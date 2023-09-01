package database.checker.rules;

import database.MYSQLrepository;
import database.checker.Rule;
import gui.MainFrame;
import java.util.ArrayList;

public class ExistRule implements Rule {

    @Override
    public String check() {
        System.out.println("OVO JE QUERY "+MainFrame.getInstance().getMainPanel().getTekstSQL().getText());
        ArrayList<String>tableNames=MainFrame.getInstance().getAppCore().getMysqLrepository().getImena();
        ArrayList<String>kljucne=MainFrame.getInstance().getMainPanel().getSqlReci();
        String tekst= MainFrame.getInstance().getMainPanel().getTekstSQL().getText().replaceAll("\\r|\\n", "");
        ArrayList<String>kolone0=new ArrayList<>();
        ArrayList<String>kolone1=new ArrayList<>();
        ArrayList<String>kolone2=new ArrayList<>();
        String[] provera=tekst.split(" ",0);
        String tabela1=null,tabela2=null;
        boolean join=false,from=false;
        for (String s:provera){

                if(from && tabela1==null){
                    tabela1=s;
                }
                else if(join && tabela2==null){
                    tabela2=s;
                }
                else if(s.equalsIgnoreCase("FROM"))from=true;
                else if(s.equalsIgnoreCase("JOIN"))join=true;

        }
        if(!tableNames.contains(tabela1)) return "NO_COLUMN";
      //  if(join && !tableNames.contains(tabela2)) return "NO_COLUMN";
        tekst.replaceAll(","," ");
        String[] kolone=tekst.split(" ",0);
        for(String st:kolone){

            char c = st.charAt(0);
            boolean operacija = ! (st.contains(">") || st.contains("<") || st.contains("+")|| st.contains("=") || st.contains("-"));
                if(!kljucne.contains(st.toUpperCase()) && !st.equalsIgnoreCase(tabela1) && !st.equalsIgnoreCase(tabela2) && !st.equalsIgnoreCase("*")
                 && !Character.isDigit(c) && !st.contains("%") && operacija) {
                    System.out.println(st);
                    kolone0.add(st);
                }

        }
        System.out.println(kolone);
        kolone1=MainFrame.getInstance().getAppCore().getMysqLrepository().getKolone(tabela1);
        if(join)kolone2=MainFrame.getInstance().getAppCore().getMysqLrepository().getKolone(tabela2);
        boolean b=false;
        for(String s2:kolone0){
            for(String s1: kolone1){

                 if(!kljucne.contains(s2) && !s2.equalsIgnoreCase(tabela1) && !s2.equalsIgnoreCase(tabela2)&& s2.contains(s1)){
                    b=true;
                    int flag=0,velicina=0,pok1=-1,pok2=-1;
                    for(int i=0;i<s2.length();i++){
                            if(s2.charAt(i)==s1.charAt(0) && i<s2.length()/2 && flag==0){
                                    flag=1;
                                    pok1=i;
                            }
                       if(flag==1){

                           velicina++;
                           if(velicina==s1.length()){
                               pok2=i;
                               break;
                           }
                           else if(s2.charAt(i)!=s1.charAt(velicina-1)){
                               flag=0;
                               pok1=-1;
                               pok2=-1;
                               velicina=0;
                           }
                       }


                    }
                    //System.out.println("Dosao do komplikovanog if-a");
                   // System.out.println("Pok1: "+pok1 + " Pok2: "+pok2);
                   // System.out.println(s2.charAt(pok1-1));
                    //System.out.println(s2.charAt(pok2+1));
                    if(pok1!=-1 && pok2!=-1 && pok1!=0 && pok2!=s2.length()-1){
                        if((s2.charAt(pok1-1)!='('&& s2.charAt(pok1-1)!=','&& s2.charAt(pok1-1)!='='&&
                                s2.charAt(pok1-1)!='+'&&s2.charAt(pok1-1)!='-'&&s2.charAt(pok1-1)!='*'&&s2.charAt(pok1-1)!='/')&&s2.charAt(pok1-1)!='.'
                        ||(s2.charAt(pok2+1)!=')'&& s2.charAt(pok2+1)!=','&& s2.charAt(pok2+1)!='='&&
                                s2.charAt(pok2+1)!='+'&&s2.charAt(pok2+1)!='-'&&s2.charAt(pok2+1)!='*'&&s2.charAt(pok2+1)!='/')){
                            System.out.println("EXIST RULE:");
                            System.out.println(s1);
                            System.out.println(s2);
                           return "NO_COLUMN";
                        }
                    }

                }
            }
            if(!b){
                return "NO_COLUMN";
            }
            else b=!b;
        }
       return null;

    }


}
