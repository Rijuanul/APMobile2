package id.advancepro.com.advancepro.service;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import id.advancepro.com.advancepro.model.CycleCountProductCategoryEntity;

/**
 * Created by TASIV on 5/11/2017.
 */

public class CycleCountProductCategoryService {

    public List<CycleCountProductCategoryEntity> getCycleCountProductCategoryList(String urlLink){
        HttpURLConnection connection = null;
        BufferedReader reader = null;
        List<CycleCountProductCategoryEntity> cycleCountProductCategoryEntities=null;
        CycleCountProductCategoryEntity cycleCountProductCategoryEntity;
        try {
            URL url = new URL(urlLink.replace(" ", "%20"));
            connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(1000);
            connection.setRequestProperty("Content-type", "application/json");
            connection.setRequestProperty("Accept-Charset", "UTF-8");
            connection.setRequestMethod("GET");
            connection.connect();
            //System.out.println(urlLink);
            //System.out.println(connection.getResponseCode() == 200);
            if(connection.getResponseCode() == 200) {
                InputStream input = connection.getInputStream();
                reader = new BufferedReader(new InputStreamReader(input));
                StringBuffer buffer = new StringBuffer();
                String line = "";
                while ((line = reader.readLine()) != null) {
                    //System.out.println(line);
                    buffer.append(line);
                }
                String finalJson = buffer.toString();
                JSONArray jsonarray = new JSONArray(finalJson);
                if(jsonarray.length()>0) {
                    cycleCountProductCategoryEntities = new ArrayList<>();
                    cycleCountProductCategoryEntity = new CycleCountProductCategoryEntity();
                    cycleCountProductCategoryEntity.setID(0);
                    cycleCountProductCategoryEntity.setName("--Select--");
                    cycleCountProductCategoryEntities.add(cycleCountProductCategoryEntity);
                    for (int i = 0; i < jsonarray.length(); i++) {
                        JSONObject finalObject = jsonarray.getJSONObject(i);
                         cycleCountProductCategoryEntity = new CycleCountProductCategoryEntity();
                        cycleCountProductCategoryEntity.setID(finalObject.getInt("id"));
                        cycleCountProductCategoryEntity.setName(finalObject.getString("name"));
                        cycleCountProductCategoryEntities.add(cycleCountProductCategoryEntity);
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();

        } finally {
            connection.disconnect();
            try {
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return  cycleCountProductCategoryEntities;
    }
}
