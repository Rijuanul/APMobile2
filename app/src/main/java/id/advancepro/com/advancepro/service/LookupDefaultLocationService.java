package id.advancepro.com.advancepro.service;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import id.advancepro.com.advancepro.model.LookupDefaultLocationEntity;


/**
 * Created by TASIV on 4/21/2017.
 */

public class LookupDefaultLocationService {
    private LookupDefaultLocationEntity lookupDefaultLocationEntity;

    public LookupDefaultLocationEntity getLookupDefaultLocation(String urlLink){
        try{
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
            if(connection.getResponseCode() == 200){
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
                lookupDefaultLocationEntity=new LookupDefaultLocationEntity();
                for (int i = 0; i < jsonarray.length(); i++) {
                    JSONObject finalObject = jsonarray.getJSONObject(i);
                    lookupDefaultLocationEntity.setID(finalObject.getString("ID"));
                    lookupDefaultLocationEntity.setName(finalObject.getString("Name"));
                    lookupDefaultLocationEntity.setSelectedname(finalObject.getString("selectedname"));
                }
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return lookupDefaultLocationEntity;
    }

}
