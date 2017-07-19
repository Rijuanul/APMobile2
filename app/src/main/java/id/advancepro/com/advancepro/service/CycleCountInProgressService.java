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

import id.advancepro.com.advancepro.model.InProgressCycleCountEntity;

/**
 * Created by TASIV on 5/5/2017.
 */

public class CycleCountInProgressService {

    private List<InProgressCycleCountEntity> resultList=null;
    private InProgressCycleCountEntity inProgressCycleCountEntity;

    public List<InProgressCycleCountEntity> getInProgressAndCompletedList(String urlLink){

        HttpURLConnection connection = null;
        BufferedReader reader = null;
        try {
            URL url = new URL(urlLink);
            connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(1000);
            connection.setRequestProperty("Content-type", "application/json");
            connection.setRequestProperty("Accept-Charset", "UTF-8");
            connection.setRequestMethod("GET");
            connection.connect();
            //System.out.println(urlLink);
            //System.out.println(connection.getResponseCode() == 200);
            if(connection.getResponseCode() == 200){
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
                    resultList= new ArrayList<InProgressCycleCountEntity>();
                    for (int i = 0; i < jsonarray.length(); i++) {
                        JSONObject finalObject = jsonarray.getJSONObject(i);
                        inProgressCycleCountEntity= new InProgressCycleCountEntity();
                        inProgressCycleCountEntity.setID(finalObject.getString("ID"));
                        inProgressCycleCountEntity.setName(finalObject.getString("Name"));
                        inProgressCycleCountEntity.setPickLocname(finalObject.getString("PickLocname"));
                        inProgressCycleCountEntity.setPicklocid(finalObject.getString("Picklocid"));
                        inProgressCycleCountEntity.setStatusid(finalObject.getString("Statusid"));
                        inProgressCycleCountEntity.setStatusname(finalObject.getString("Statusname"));
                        inProgressCycleCountEntity.setWarehousename(finalObject.getString("Warehousename"));
                        resultList.add(inProgressCycleCountEntity);
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
        return resultList;
    }
}
