package database;

import app.Main;
import database.settings.Settings;
import gui.MainFrame;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import resource.DBNode;
import resource.data.Row;
import resource.enums.AttributeType;
import resource.implementation.Attribute;
import resource.implementation.Entity;
import resource.implementation.InformationResource;

import javax.swing.plaf.nimbus.State;
import java.io.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.StringJoiner;
import java.util.stream.Collectors;

@Data
public class MYSQLrepository implements Repository{

    private Settings settings;
    private Connection connection;
    private ArrayList<String> imena=new ArrayList<>();
    private String tt = "";
    public MYSQLrepository(Settings settings) {
        this.settings = settings;
    }

    private void initConnection() throws SQLException, ClassNotFoundException{
        String ip = (String) settings.getParameter("mysql_ip");
        String database = (String) settings.getParameter("mysql_database");
        String username = (String) settings.getParameter("mysql_username");
        String password = (String) settings.getParameter("mysql_password");
        //Class.forName("net.sourceforge.jtds.jdbc.Driver");
        connection = DriverManager.getConnection("jdbc:mysql://"+ip+"/"+database,username,password);


    }

    private void closeConnection(){
        try{
            connection.close();
        }
        catch (SQLException e){
            e.printStackTrace();
        }
        finally {
            connection = null;
        }
    }


    @Override
    public DBNode getSchema() {

        try{
            this.initConnection();

            DatabaseMetaData metaData = connection.getMetaData();
            InformationResource ir = new InformationResource("RAF_bp_tim49");

            String tableType[] = {"TABLE"};
            ResultSet tables = metaData.getTables(connection.getCatalog(), null, null, tableType);

            while (tables.next()){

                String tableName = tables.getString("TABLE_NAME");
                if(tableName.contains("trace"))continue;
                imena.add(tableName);
                Entity newTable = new Entity(tableName, ir);
                ir.addChild(newTable);

                //Koje atribute imaja ova tabela?

                ResultSet columns = metaData.getColumns(connection.getCatalog(), null, tableName, null);

                while (columns.next()){

                    // COLUMN_NAME TYPE_NAME COLUMN_SIZE ....

                    String columnName = columns.getString("COLUMN_NAME");
                    String columnType = columns.getString("TYPE_NAME");

                    System.out.println(columnType);

                    int columnSize = Integer.parseInt(columns.getString("COLUMN_SIZE"));

//                    ResultSet pkeys = metaData.getPrimaryKeys(connection.getCatalog(), null, tableName);
//
//                    while (pkeys.next()){
//                        String pkColumnName = pkeys.getString("COLUMN_NAME");
//                    }


                    Attribute attribute = new Attribute(columnName, newTable,
                            AttributeType.valueOf(
                                    Arrays.stream(columnType.toUpperCase().split(" "))
                                    .collect(Collectors.joining("_"))),
                            columnSize);
                    newTable.addChild(attribute);

                }



            }


            //TODO Ogranicenja nad kolonama? Relacije?


            return ir;
            //String isNullable = columns.getString("IS_NULLABLE");
            // ResultSet foreignKeys = metaData.getImportedKeys(connection.getCatalog(), null, table.getName());
            // ResultSet primaryKeys = metaData.getPrimaryKeys(connection.getCatalog(), null, table.getName());

        }
        catch (SQLException e1) {
            e1.printStackTrace();
        }
        catch (ClassNotFoundException e2){ e2.printStackTrace();}
        finally {
            this.closeConnection();
        }

        return null;
    }

    @Override
    public List<Row> get(String from) {

        List<Row> rows = new ArrayList<>();


        try{
            this.initConnection();

            String query = "SELECT * FROM " + from;
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet rs = preparedStatement.executeQuery();
            ResultSetMetaData resultSetMetaData = rs.getMetaData();

            while (rs.next()){

                Row row = new Row();
                row.setName(from);

                for (int i = 1; i<=resultSetMetaData.getColumnCount(); i++){
                    row.addField(resultSetMetaData.getColumnName(i), rs.getString(i));
                }
                rows.add(row);


            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            this.closeConnection();
        }

        return rows;
    }

    public void pomFja(ResultSet rs, ResultSetMetaData rsmd, String table, List<Row>rows){
       try{
           while(rs.next()){
               Row row = new Row();
               row.setName(table);
               for (int i =1;i<=rsmd.getColumnCount();i++){
                   row.addField(rsmd.getColumnName(i),rs.getString(i));
               }
               rows.add(row);
           }
       }catch (Exception e){
           e.printStackTrace();

       }

    }

    public void export(String s){

        try {
            if (s==null)
                return;
            this.initConnection();
            String upit=MainFrame.getInstance().getMainPanel().getTekstSQL().getText();
            Statement statement=connection.createStatement();

            CSVFormat format = CSVFormat.DEFAULT.withRecordSeparator("\n");
            String fileName = "proba2.csv";
            //Kreiranje fajla u odabrani direktorijum radi..
            File file = new File(s+"/"+fileName);
            //Dodat novi catch sa IOException-om..
            FileWriter fw = new FileWriter(file);
            CSVPrinter printer = new CSVPrinter(fw, format);
            ResultSet resultSet=statement.executeQuery(upit);
            printer.printRecords(resultSet);
            fw.close();

            file.createNewFile();


        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            this.closeConnection();
        }


    }


    public void bulk(File file){

        //this.file=file;
        String line = "";
        String split=",";
        String selektovano= MainFrame.getInstance().getjTree().getLastSelectedPathComponent().toString();
        //vrednosti koje vraca getColumnType(), zbog parsiranja iz CSV fajla...
        int VARCHAR = 12;
        int INT = 4;
        int DECIMAL = 3;
        int DATE = 91;
        try {
            this.initConnection();
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery("select * from "+selektovano);
            ResultSetMetaData rsmd = rs.getMetaData();
            int colCount = rsmd.getColumnCount();
          //  System.out.println("Broj kolona tabele : "+selektovano+ " je: "+colCount);
            //GENERISANJE STRINGA KOJI CE ICI U VALUES() ZA UPIT...
            String valuesString = "";
            for (int i=1;i<colCount;i++){
                valuesString += "?, ";
            }
            valuesString+="?";
        //    System.out.println("("+valuesString+")");

            BufferedReader bufferedReader= new BufferedReader(new FileReader(file));
            int brojac=0;
            int brojacLinija=1;
            while((line=bufferedReader.readLine())!=null){
                if(brojacLinija==1){
                    brojacLinija++;
                    continue;
                }
                System.out.println(line);
                brojacLinija++;
                String query ="INSERT INTO "+selektovano+" VALUES("+valuesString+")";
               // System.out.println(query);
                PreparedStatement preparedStatement = connection.prepareStatement(query);
                brojac = 0;
                String[] niz=line.split(split);
                for (int a = 0;a<niz.length;a++){
                    System.out.println("Brojac : "+brojac);
                if(brojac!=colCount) {
                    if (rsmd.getColumnType(brojac + 1) == INT) {
                        System.out.println(niz[brojac]);
                        preparedStatement.setInt(brojac + 1, Integer.parseInt(niz[brojac]));
                    } else if (rsmd.getColumnType(brojac + 1) == VARCHAR) {
                        preparedStatement.setString(brojac + 1, niz[brojac].toString());
                    } else if (rsmd.getColumnType(brojac + 1) == DATE) {
                        preparedStatement.setDate(brojac + 1, Date.valueOf(niz[brojac]));
                    } else if (rsmd.getColumnType(brojac + 1) == DECIMAL) {
                        preparedStatement.setFloat(brojac + 1, Float.parseFloat(niz[brojac]));
                    } else {
                        preparedStatement.setString(brojac + 1, niz[brojac]);
                    }

                }
                    brojac++;
                }

                //Ovde treba da se izvrsi INSERT
                int t = preparedStatement.executeUpdate();
                System.out.println("Izvrsen INSERT..");

            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }catch (IOException ex) {
            ex.printStackTrace();
        }
        finally {
            this.closeConnection();
        }




    }
    public ArrayList<String> getKolone(String ime){

        ArrayList<String> imena=new ArrayList<>();
        try {
            this.initConnection();
            String query="SELECT * FROM " + ime;
            PreparedStatement preparedStatement=connection.prepareStatement(query);
            ResultSet resultSet=preparedStatement.executeQuery();
            ResultSetMetaData resultSetMetaData=resultSet.getMetaData();
           int count=resultSetMetaData.getColumnCount();
           for(int i=1;i<=count;i++){
               imena.add(resultSetMetaData.getColumnName(i));
           }
        } catch (SQLException e) {
            System.out.println("Ne postoji tabela");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }finally{
            this.closeConnection();
        }

        return imena;

    }


    @Override
    public List<Row> runQ(String query) {
    /*    if (!MainFrame.getInstance().getAppCore().getChecker().getGreske().empty()){
            return null;
        }*/
        System.out.println("Ovo je trenutni query");
        System.out.println(query);
        query.replaceAll("(\r\n|\n)", " ");

        List <Row> rows= new ArrayList<>();
        String[] reci = query.split(" ");
        try{
            this.initConnection();
            if (reci[0].equalsIgnoreCase("delete")){
                PreparedStatement statement = connection.prepareStatement(query);
                statement.executeUpdate();
                String qq = "SELECT * FROM "+reci[2];
                ResultSet resultSet = statement.executeQuery(qq);
                ResultSetMetaData rsmd = resultSet.getMetaData();
                this.pomFja(resultSet, rsmd, reci[2], rows);
            }
            else if (reci[0].equalsIgnoreCase("insert")){
                PreparedStatement statement = connection.prepareStatement(query);
                statement.executeUpdate(query);
                String qq = "SELECT * FROM "+reci[2];
                ResultSet resultSet = statement.executeQuery(qq);
                ResultSetMetaData rsmd = resultSet.getMetaData();
                this.pomFja(resultSet, rsmd, reci[2], rows);
            }
            else if(reci[0].equalsIgnoreCase("update")){
                PreparedStatement statement = connection.prepareStatement(query);
                statement.executeUpdate(query);
                String qq = "SELECT * FROM "+reci[1];
                ResultSet resultSet = statement.executeQuery(qq);
                ResultSetMetaData rsmd = resultSet.getMetaData();
                this.pomFja(resultSet, rsmd, reci[1], rows);
            }
            else {
                PreparedStatement statement = connection.prepareStatement(query);
                ResultSet resultSet = statement.executeQuery(query);
                ResultSetMetaData rsmd = resultSet.getMetaData();
                String ime = "tabela";
                this.pomFja(resultSet, rsmd, ime, rows);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }finally{

            this.closeConnection();
        }
        return rows;
    }

    public ArrayList<String> fkHelper(String tabela){
        String fkColumnName="";
        ArrayList<String>foreignKeys = new ArrayList<>();
        try {
            this.initConnection();
            DatabaseMetaData databaseMetaData = connection.getMetaData();
            ResultSet resultSet = databaseMetaData.getImportedKeys(connection.getCatalog(), null, tabela);
            while(resultSet.next()){
                fkColumnName = resultSet.getString("FKCOLUMN_NAME");
                System.out.println(fkColumnName);
                foreignKeys.add(fkColumnName);

            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }finally {
            this.closeConnection();
        }

        return foreignKeys;
    }

    public ArrayList<String> getImena() {
        return imena;
    }

    public void setImena(ArrayList<String> imena) {
        this.imena = imena;
    }
}
