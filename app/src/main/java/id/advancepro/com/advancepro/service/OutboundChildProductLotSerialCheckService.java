package id.advancepro.com.advancepro.service;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import id.advancepro.com.advancepro.model.OutboundChildProductLotSerialCheckEntity;

/**
 * Created by TASIV on 4/20/2017.
 */

public class OutboundChildProductLotSerialCheckService {
    private OutboundChildProductLotSerialCheckEntity outboundChildProductLotSerialCheckEntity;

    public OutboundChildProductLotSerialCheckEntity getLotSerial(String urlLink){
        HttpURLConnection connection = null;
        BufferedReader reader = null;
        try {
            URL url = new URL(urlLink);
            connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(1500);
            connection.setRequestProperty("Content-type", "application/json");
            connection.setRequestProperty("Accept-Charset", "UTF-8");
            connection.setRequestMethod("GET");
            connection.connect();
            //System.out.println(urlLink);
            //System.out.println(connection.getResponseCode() == 200);
            if (connection.getResponseCode() == 200) {
                InputStream input = connection.getInputStream();
                reader = new BufferedReader(new InputStreamReader(input));
                StringBuffer buffer = new StringBuffer();
                String line = "";
                while ((line = reader.readLine()) != null) {
                   // System.out.println(line);
                    buffer.append(line);
                }
                String finalJson = buffer.toString();
                JSONObject json = new JSONObject(finalJson);
                JSONArray jsonarray = json.getJSONArray("CheckInBoundProductSerialLotResult");
                if (jsonarray.length() > 0) {
                    outboundChildProductLotSerialCheckEntity = new OutboundChildProductLotSerialCheckEntity();
                    for (int i = 0; i < jsonarray.length(); i++) {
                        JSONObject finalObject = jsonarray.getJSONObject(i);
                        outboundChildProductLotSerialCheckEntity.setProductLot(finalObject.getString("ProductLot"));
                        outboundChildProductLotSerialCheckEntity.setProductSerial(finalObject.getString("ProductSerial"));
                    }
                }
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return  outboundChildProductLotSerialCheckEntity;
    }
}
