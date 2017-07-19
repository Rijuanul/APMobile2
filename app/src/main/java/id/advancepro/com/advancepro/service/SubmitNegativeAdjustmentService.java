package id.advancepro.com.advancepro.service;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by TASIV on 4/13/2017.
 */

public class SubmitNegativeAdjustmentService {
    private Integer id;
    public Integer submitNegativeAdjustment(String urlLink,String jsonData){
        HttpURLConnection connection = null;
        BufferedReader reader = null;

        try {

            URL url = new URL(urlLink);
            connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(10000);
            connection.setRequestProperty("Content-type", "application/json");
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            OutputStreamWriter writer=new OutputStreamWriter(connection.getOutputStream());
            writer.write(jsonData);
            writer.flush();

            System.out.println("Connection:"+connection.getResponseCode());
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
                JSONObject json = new JSONObject(finalJson);
                JSONArray jsonarray = json.getJSONArray("GlobalAdjustMinusSerialLotResult");
                for (int i = 0; i < jsonarray.length(); i++) {
                    JSONObject finalObject = jsonarray.getJSONObject(i);
                    id=finalObject.getInt("id");
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
        return id;
    }
}
