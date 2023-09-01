package database.checker;

import database.checker.rules.*;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Stack;

public class Checker {

    Stack<String>greske = new Stack<>();
    ArrayList<Rule> pravila = new ArrayList<>();
    DescRepository descRepository=new DescRepository();

    public Checker() {
        this.pravila.add(new AliasRule());
        //this.pravila.add(new CSVRule());
        this.pravila.add(new ExistRule());
        this.pravila.add(new KeyWordRule());
        this.pravila.add(new OrderRule());
        this.pravila.add(new WhereRule());
        this.pravila.add(new GroupByRule());
        this.pravila.add(new ForeignKeyRule());

    }

    public Stack<String> check(){

        for (Rule rule:pravila){
            String s=rule.check();
            System.out.println(s);
            if(s!=null) {
                this.greske.push(s);
            }
        }

        return this.greske;
    }

    public DescRepository getDescRepository() {
        return descRepository;
    }

    public void setDescRepository(DescRepository descRepository) {
        this.descRepository = descRepository;
    }


    public Stack<String> getGreske() {
        return greske;
    }

    public void setGreske(Stack<String> greske) {
        this.greske = greske;
    }

    public ArrayList<Rule> getPravila() {
        return pravila;
    }

    public void setPravila(ArrayList<Rule> pravila) {
        this.pravila = pravila;
    }
}
