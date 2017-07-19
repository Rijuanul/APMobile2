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

import id.advancepro.com.advancepro.model.CycleCountItemEntity;

/**
 * Created by TASIV on 5/8/2017.
 */

public class CycleCountItemService {

    private List<CycleCountItemEntity> resultList=null;
    private CycleCountItemEntity cycleCountItemEntity;

    public List<CycleCountItemEntity> getCycleCountItem(String urlLink){

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
                    resultList= new ArrayList<>();
                    for (int i = 0; i < jsonarray.length(); i++) {
                        JSONObject finalObject = jsonarray.getJSONObject(i);
                        cycleCountItemEntity= new CycleCountItemEntity();
                        cycleCountItemEntity.setCycleCountdetaild(finalObject.getString("CycleCountdetaild"));
                        cycleCountItemEntity.setCycleCountId(finalObject.getString("CycleCountId"));
                        cycleCountItemEntity.setProductid(finalObject.getString("Productid"));
                        cycleCountItemEntity.setProductName(finalObject.getString("ProductName"));
                        cycleCountItemEntity.setProductSku(finalObject.getString("ProductSku"));
                        cycleCountItemEntity.setProductQuantity(finalObject.getString("ProductQuantity"));
                        cycleCountItemEntity.setScannedqty(finalObject.getString("Scannedqty"));
                        cycleCountItemEntity.setComments(finalObject.getString("Comments"));
                        resultList.add(cycleCountItemEntity);
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
