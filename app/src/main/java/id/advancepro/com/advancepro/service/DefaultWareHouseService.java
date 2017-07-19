package id.advancepro.com.advancepro.service;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import id.advancepro.com.advancepro.model.DefaultWareHouse;
import id.advancepro.com.advancepro.utils.Constant;

/**
 * Created by TASIV on 3/22/2017.
 */

// Get Default Warehouse and Call to webservice
public class DefaultWareHouseService {

    private DefaultWareHouse defaultWareHouse;
    public DefaultWareHouse getDefaultWareHouse(String urlLink){
        HttpURLConnection connection = null;
        BufferedReader reader = null;
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
                for (int i = 0; i < jsonarray.length(); i++) {
                    JSONObject finalObject = jsonarray.getJSONObject(i);
                    defaultWareHouse = new DefaultWareHouse();
                    defaultWareHouse.setWareHouseName(finalObject.getString("WareHouseName"));
                    defaultWareHouse.setWareHouseId(finalObject.getString("WareHouseID"));
                    defaultWareHouse.setErrorMessage("");
                }
            }else{
                errorMessageSet();
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            errorMessageSet();

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
        return defaultWareHouse;
    }

    private void errorMessageSet(){
        defaultWareHouse = new DefaultWareHouse();
        defaultWareHouse.setWareHouseName("");
        defaultWareHouse.setWareHouseId("0");
        defaultWareHouse.setErrorMessage(Constant.SERVER_ERROR);

    }
}
