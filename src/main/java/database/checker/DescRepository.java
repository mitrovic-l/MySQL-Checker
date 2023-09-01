package database.checker;


import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;


import java.io.FileReader;
import java.util.ArrayList;
import java.util.Stack;

public class DescRepository {

    private ArrayList<JSONObject>jsonObjectssvi=new ArrayList<>();
    private ArrayList<JSONObject>jsonObjects=new ArrayList<>();

public ArrayList citanje (Stack<String>strings){

    JSONParser jsonParser=new JSONParser();
    try {
      //  JSONArray a = (JSONArray) jsonParser.parse(new FileReader("C:\\Users\\Mateja\\Desktop\\domaci-mateja-civkaroski-luka-mitrovic\\rules.json"));
        JSONArray a = (JSONArray) jsonParser.parse(new FileReader("rules.json"));



        for (Object o : a)
        {
            JSONObject person = (JSONObject) o;
            jsonObjectssvi.add(person);

        }
        while(!strings.empty()){
            String s=strings.peek();
            strings.pop();
            for (JSONObject j : jsonObjectssvi)
            {
                if(s.equalsIgnoreCase((String) j.get("name")) && !jsonObjects.contains(j)){
                    jsonObjects.add(j);
            }

            }
        }


    }catch (Exception e){
        e.printStackTrace();
    }

return jsonObjects;
}

}
