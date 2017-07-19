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

import id.advancepro.com.advancepro.model.NegAdjustmentProductEntity;

/**
 * Created by TASIV on 4/12/2017.
 */

public class NegAdjustmentProductSearchService {

    List<NegAdjustmentProductEntity> negAdjustmentProductEntityList;
    NegAdjustmentProductEntity negAdjustmentProductEntity;
    public List<NegAdjustmentProductEntity> getProductDetails(String urlLink){
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
                JSONArray jsonarray = json.getJSONArray("CheckInBoundProductSerialLotResult");
                negAdjustmentProductEntityList=new ArrayList<>();
                for (int i = 0; i < jsonarray.length(); i++) {
                    JSONObject finalObject = jsonarray.getJSONObject(i);
                    negAdjustmentProductEntity=new NegAdjustmentProductEntity();
                    negAdjustmentProductEntity.setProductLot(finalObject.getString("ProductLot"));
                    negAdjustmentProductEntity.setProductSerial(finalObject.getString("ProductSerial"));
                    negAdjustmentProductEntityList.add(negAdjustmentProductEntity);
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
        return negAdjustmentProductEntityList;
    }
}
