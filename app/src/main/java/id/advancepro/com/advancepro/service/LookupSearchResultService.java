package id.advancepro.com.advancepro.service;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import id.advancepro.com.advancepro.model.LookupResultEntity;

/**
 * Created by TASIV on 4/21/2017.
 */

public class LookupSearchResultService {
    private List<LookupResultEntity> lookupResultEntityList;
    private LookupResultEntity lookupResultEntity;

    public List<LookupResultEntity> getLookupResult(String urlLink,String jsonData){
        try{
            HttpURLConnection connection = null;
            BufferedReader reader = null;


        URL url = new URL(urlLink);
        connection = (HttpURLConnection) url.openConnection();
        connection.setConnectTimeout(10000);
        connection.setRequestProperty("Content-type", "application/json");
        connection.setRequestMethod("POST");
        connection.setDoOutput(true);
        OutputStreamWriter writer=new OutputStreamWriter(connection.getOutputStream());
        //System.out.println(jsonData);
        writer.write(jsonData);
        writer.flush();

        //System.out.println(connection.getResponseCode());
        if(connection.getResponseCode() == 200){
            InputStream input = connection.getInputStream();
            reader = new BufferedReader(new InputStreamReader(input));
            StringBuffer buffer = new StringBuffer();
            String line = "";
            while ((line = reader.readLine()) != null) {
               // System.out.println("line"+line);
                buffer.append(line);
            }
            String finalJson = buffer.toString();
            JSONObject jsonObject=new JSONObject(finalJson);
            JSONArray jsonarray = jsonObject.getJSONArray("SearchLookupResult");
            if(jsonarray.length()>0) {
                lookupResultEntityList=new ArrayList<>();
                for (int i = 0; i < jsonarray.length(); i++) {
                    JSONObject finalObject = jsonarray.getJSONObject(i);
                    if(finalObject.getString("ErrorMessage").equals("")) {
                        lookupResultEntity = new LookupResultEntity();
                        lookupResultEntity.setProductID(finalObject.getString("ProductID"));
                        lookupResultEntity.setSku(finalObject.getString("Sku"));
                        lookupResultEntity.setUpc(finalObject.getString("Upc"));
                        lookupResultEntity.setPicklocname(finalObject.getString("Picklocname"));
                        lookupResultEntity.setStock(finalObject.getString("Stock"));
                        lookupResultEntity.setAvailable(finalObject.getString("Available"));
                        lookupResultEntity.setUnit(finalObject.getString("Unit"));
                        lookupResultEntity.setWhid(finalObject.getString("whid"));
                        lookupResultEntity.setWarehousename(finalObject.getString("Warehousename"));

                        lookupResultEntityList.add(lookupResultEntity);
                    }
                }
            }
        }
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return lookupResultEntityList;
    }
}
