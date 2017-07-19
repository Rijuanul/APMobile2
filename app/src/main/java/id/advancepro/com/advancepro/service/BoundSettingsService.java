package id.advancepro.com.advancepro.service;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import id.advancepro.com.advancepro.model.BoundSettingsEntity;

/**
 * Created by TASIV on 3/27/2017.
 */

public class BoundSettingsService {

    // Web service call for getting Inbound Setting List from Database
    public BoundSettingsEntity getInboundSettingsList(String urlLink){

        HttpURLConnection connection = null;
        BufferedReader reader = null;
        BoundSettingsEntity boundSettingsEntity =null;
        try {
            URL url = new URL(urlLink);
            connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(1000);
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
                    boundSettingsEntity =new BoundSettingsEntity();
                    boundSettingsEntity.setIsShowName(finalObject.getString("IsShowName"));
                    boundSettingsEntity.setIsShowProdName(finalObject.getString("IsShowProdName"));
                    boundSettingsEntity.setIsShowOrderQty(finalObject.getString("IsShowOrderQty"));
                    boundSettingsEntity.setIsShowOrderDate(finalObject.getString("IsShowOrderDate"));
                    boundSettingsEntity.setIsShowExpectedDate(finalObject.getString("IsShowExpectedDate"));
                    boundSettingsEntity.setSortOrderColumn(finalObject.getString("SortOrderColumn"));
                    boundSettingsEntity.setSortOrderType(finalObject.getString("SortOrderType"));
                    boundSettingsEntity.setErrorMessage(finalObject.getString("ErrorMessage"));
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
        return boundSettingsEntity;
    }
}
