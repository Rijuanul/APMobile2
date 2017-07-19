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
import java.util.Collections;
import java.util.List;

import id.advancepro.com.advancepro.model.InboundChildEntity;

/**
 * Created by TASIV on 4/3/2017.
 */

public class InboundChildDetailsService {
    List<InboundChildEntity> inboundChildEntityList=null;

    public List<InboundChildEntity> getInboundChildDetails(String urlLink){
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
                    inboundChildEntityList=new ArrayList<>();
                    for (int i = 0; i < jsonarray.length(); i++) {
                        JSONObject finalObject = jsonarray.getJSONObject(i);
                        InboundChildEntity inboundChildEntity=new InboundChildEntity();
                        inboundChildEntity.setOrderDetailid(finalObject.getString("OrderDetailid"));
                        inboundChildEntity.setProductID(finalObject.getString("ProductID"));
                        inboundChildEntity.setProductName(finalObject.getString("ProductName"));
                        inboundChildEntity.setProductSKU(finalObject.getString("ProductSKU"));
                        inboundChildEntity.setUPC(finalObject.getString("UPC"));
                        inboundChildEntity.setTotQuantity(finalObject.getString("TotQuantity"));
                        inboundChildEntity.setQuantityReceived(finalObject.getString("QuantityReceived"));
                        inboundChildEntity.setWareHouseName(finalObject.getString("WareHouseName"));
                        inboundChildEntity.setCombID(finalObject.getString("CombID"));
                        inboundChildEntity.setIslotfound(finalObject.getString("Islotfound"));
                        inboundChildEntity.setIsbatchfound(finalObject.getString("Isbatchfound"));
                        inboundChildEntity.setErrorMessage(finalObject.getString("ErrorMessage"));
                        if(Integer.parseInt(finalObject.getString("QuantityReceived")) == Integer.parseInt(finalObject.getString("TotQuantity"))){
                            inboundChildEntity.setStatus(1);
                        }else{
                            inboundChildEntity.setStatus(0);
                        }
                        inboundChildEntity.setQtyRecv(false);
                        inboundChildEntityList.add(inboundChildEntity);
                    }
                    Collections.sort(inboundChildEntityList);
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
        return inboundChildEntityList;
    }

}
