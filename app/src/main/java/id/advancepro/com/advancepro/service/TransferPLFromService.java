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
import java.util.ArrayList;
import java.util.List;

import id.advancepro.com.advancepro.model.TransferPLFromLocationEntity;

/**
 * Created by TASIV on 4/13/2017.
 */

public class TransferPLFromService {

    List<TransferPLFromLocationEntity> transferPLFromLocationEntityList;
    TransferPLFromLocationEntity transferPLFromLocationEntity;
    public List<TransferPLFromLocationEntity> getPlToList(String urlLink, String jsonData){
        HttpURLConnection connection = null;
        BufferedReader reader = null;
        try {

            URL url = new URL(urlLink);
            connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(1000);
            connection.setRequestProperty("Content-type", "application/json");
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            OutputStreamWriter writer=new OutputStreamWriter(connection.getOutputStream());
            writer.write(jsonData);
            writer.flush();

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
                JSONObject json = new JSONObject(finalJson);
                JSONArray jsonarray = json.getJSONArray("Lanz_TransferGetPickLocationResult");
                transferPLFromLocationEntityList =new ArrayList<>();
                transferPLFromLocationEntity = new TransferPLFromLocationEntity();
                transferPLFromLocationEntity.setPickLocname("-----Select-----");
                transferPLFromLocationEntity.setPicklocid("0");
                transferPLFromLocationEntityList.add(transferPLFromLocationEntity);
                for (int i = 0; i < jsonarray.length(); i++) {
                    JSONObject finalObject = jsonarray.getJSONObject(i);
                    transferPLFromLocationEntity = new TransferPLFromLocationEntity();
                    transferPLFromLocationEntity.setPickLocname(finalObject.getString("PickLocname"));
                    transferPLFromLocationEntity.setPicklocid(finalObject.getString("Picklocid"));
                    transferPLFromLocationEntityList.add(transferPLFromLocationEntity);
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
        return transferPLFromLocationEntityList;
    }
}
