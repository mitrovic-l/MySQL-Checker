package app;

import database.Database;
import database.DatabaseImplementation;
import database.MYSQLrepository;
import database.checker.Checker;
import database.checker.DescRepository;
import database.settings.Settings;
import database.settings.SettingsImplementation;
import gui.MainFrame;
import gui.table.TableModel;
import lombok.Getter;
import lombok.Setter;
import observer.Notification;
import observer.enums.NotificationCode;
import observer.implementation.PublisherImplementation;
import org.json.simple.JSONObject;
import resource.implementation.InformationResource;
import tree.Tree;
import tree.implementation.TreeImplementation;
import utils.Constants;

import javax.swing.*;
import javax.swing.tree.DefaultTreeModel;
import java.io.File;
import java.util.ArrayList;
import java.util.Stack;

@Getter
@Setter
public class AppCore extends PublisherImplementation {

    private Database database;
    private Settings settings;
    private MYSQLrepository mysqLrepository;
    private TableModel tableModel;
    private DefaultTreeModel defaultTreeModel;
    private Tree tree;
    private Checker checker;
    private DescRepository descRepository;
    private ArrayList<JSONObject>jsonObjects=new ArrayList<>();
    File file;
    public AppCore() {
        this.settings = initSettings();
        this.mysqLrepository = new MYSQLrepository(this.settings);
        this.database = new DatabaseImplementation(this.mysqLrepository);
        this.tableModel = new TableModel();
        this.tree = new TreeImplementation();
        this.checker = new Checker();
        this.descRepository=new DescRepository();

    }

    private Settings initSettings() {
        Settings settingsImplementation = new SettingsImplementation();
        settingsImplementation.addParameter("mysql_ip", Constants.MYSQL_IP);
        settingsImplementation.addParameter("mysql_database", Constants.MYSQL_DATABASE);
        settingsImplementation.addParameter("mysql_username", Constants.MYSQL_USERNAME);
        settingsImplementation.addParameter("mysql_password", Constants.MYSQL_PASSWORD);
        return settingsImplementation;
    }


    public DefaultTreeModel loadResource(){
        InformationResource ir = (InformationResource) this.database.loadResource();
        return this.tree.generateTree(ir);
    }

    public void readDataFromTable(String fromTable){

        tableModel.setRows(this.database.readDataFromTable(fromTable));

        //Zasto ova linija moze da ostane zakomentarisana?
        this.notifySubscribers(new Notification(NotificationCode.DATA_UPDATED, this.getTableModel()));
    }


    public void run(){

        String upit = MainFrame.getInstance().getMainPanel().getTekstSQL().getText().replaceAll("\\r|\\n", "");
        Stack<String>greske = new Stack<>();
      // String upit = upit1.replaceAll("\\r\\n|\\r|\\n", " ");
        while(!this.getChecker().getGreske().empty()){
            this.getChecker().getGreske().pop();
        }
        greske=checker.check();

        DatabaseImplementation databaseImplementation = (DatabaseImplementation) database;


        if (this.getChecker().getGreske().empty()){
            //izvrsavanje
           // this.getMysqLrepository().izvrsavanje();
            this.tableModel.setRows(databaseImplementation.readQueryData(upit));
        System.out.println("Izvresno");

        } else {
            jsonObjects=checker.getDescRepository().citanje(greske);
            for(JSONObject j:jsonObjects){
                System.out.println(j.get("name"));
                //Prilikom ispisa poruke, zameniti %s sa konkretnim argumentom
                // koji treba biti uklonjen/popravljen u query-u
                JOptionPane.showMessageDialog(null,  j.get("desc").toString()+"\n"+j.get("sugg").toString(),j.get("name").toString(), JOptionPane.ERROR_MESSAGE);
            }
            jsonObjects.clear();
            while(!this.getChecker().getGreske().empty()){
                this.getChecker().getGreske().pop();
            }
            while(!greske.empty()){
                greske.pop();
            }
            System.out.println(greske.empty());
            System.out.println(this.getChecker().getGreske().empty());


        }


    }




}
