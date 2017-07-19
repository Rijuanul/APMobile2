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

import id.advancepro.com.advancepro.model.CycleCountVendorEntity;

/**
 * Created by TASIV on 5/11/2017.
 */

public class CycleCountVendorService {

    public List<CycleCountVendorEntity> getCycleCountVendorList(String urlLink){
        HttpURLConnection connection = null;
        BufferedReader reader = null;
        List<CycleCountVendorEntity> cycleCountVendorEntities=null;
        CycleCountVendorEntity cycleCountVendorEntity;
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
                    cycleCountVendorEntities = new ArrayList<>();
                    cycleCountVendorEntity = new CycleCountVendorEntity();
                    cycleCountVendorEntity.setID(0);
                    cycleCountVendorEntity.setName("--Select--");
                    cycleCountVendorEntities.add(cycleCountVendorEntity);
                    for (int i = 0; i < jsonarray.length(); i++) {
                        JSONObject finalObject = jsonarray.getJSONObject(i);
                        cycleCountVendorEntity = new CycleCountVendorEntity();
                        cycleCountVendorEntity.setID(finalObject.getInt("id"));
                        cycleCountVendorEntity.setName(finalObject.getString("name"));
                        cycleCountVendorEntities.add(cycleCountVendorEntity);
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
        return  cycleCountVendorEntities;
    }
}
