package id.advancepro.com.advancepro.service;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import id.advancepro.com.advancepro.model.LookupCycleCountsDefaultSettingEntity;

/**
 * Created by TASIV on 5/2/2017.
 */

public class LookupCycleCountsDefaultSettingService {

    private List<LookupCycleCountsDefaultSettingEntity> lookupCycleCountsDefaultSettingEntityList;
    private LookupCycleCountsDefaultSettingEntity lookupCycleCountsDefaultSettingEntity;
    public  List<LookupCycleCountsDefaultSettingEntity> outboundInboundSettingsSave(String urlLink) {
        try {
            HttpURLConnection connection = null;
            BufferedReader reader = null;

            URL url = new URL(urlLink);
            connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(1500);
            connection.setRequestProperty("Content-type", "application/json");
            connection.setRequestProperty("Accept-Charset", "UTF-8");
            connection.setRequestMethod("GET");
            connection.connect();
            //System.out.println(connection.getResponseCode());
            if (connection.getResponseCode() == 200) {
                InputStream input = connection.getInputStream();
                reader = new BufferedReader(new InputStreamReader(input));
                StringBuffer buffer = new StringBuffer();
                String line = "";
                while ((line = reader.readLine()) != null) {
                    //System.out.println("line"+line);
                    buffer.append(line);
                }
                String finalJson = buffer.toString();
                JSONArray jsonarray = new JSONArray(finalJson);
                if (jsonarray.length() > 0) {
                    lookupCycleCountsDefaultSettingEntityList=new ArrayList<>();
                    for (int i = 0; i < jsonarray.length(); i++) {
                        lookupCycleCountsDefaultSettingEntity=new LookupCycleCountsDefaultSettingEntity();
                        JSONObject finalObject = jsonarray.getJSONObject(i);
                        lookupCycleCountsDefaultSettingEntity.setID(finalObject.getString("ID"));
                        lookupCycleCountsDefaultSettingEntity.setName(finalObject.getString("Name"));
                        lookupCycleCountsDefaultSettingEntity.setSelectedname(finalObject.getString("selectedname"));
                        lookupCycleCountsDefaultSettingEntityList.add(lookupCycleCountsDefaultSettingEntity);
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return lookupCycleCountsDefaultSettingEntityList;
    }
}
