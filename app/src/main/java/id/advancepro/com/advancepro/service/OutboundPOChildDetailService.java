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

import id.advancepro.com.advancepro.model.OutboundPOChildDetilsEntity;

/**
 * Created by TASIV on 4/19/2017.
 */

public class OutboundPOChildDetailService {

    List<OutboundPOChildDetilsEntity> outboundPOChildDetilsEntityList=null;

    public List<OutboundPOChildDetilsEntity> getOutboundChildDetails(String urlLink){
        HttpURLConnection connection = null;
        BufferedReader reader = null;
        try {
            URL url = new URL(urlLink);
            connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(5000);
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
                    outboundPOChildDetilsEntityList=new ArrayList<>();
                    for (int i = 0; i < jsonarray.length(); i++) {
                        JSONObject finalObject = jsonarray.getJSONObject(i);
                        OutboundPOChildDetilsEntity outboundPOChildDetilsEntity=new OutboundPOChildDetilsEntity();
                        outboundPOChildDetilsEntity.setOrderDetailId(finalObject.getString("OrderDetailId"));
                        outboundPOChildDetilsEntity.setProductID(finalObject.getString("ProductID"));
                        outboundPOChildDetilsEntity.setProductName(finalObject.getString("ProductName"));
                        outboundPOChildDetilsEntity.setProductSKU(finalObject.getString("ProductSKU"));
                        outboundPOChildDetilsEntity.setProductUPC(finalObject.getString("ProductUPC"));
                        outboundPOChildDetilsEntity.setQuantity(finalObject.getString("Quantity"));
                        outboundPOChildDetilsEntity.setQuantityRecd(finalObject.getString("QuantityRecd"));
                        outboundPOChildDetilsEntity.setLocationName(finalObject.getString("LocationName"));
                        outboundPOChildDetilsEntity.setCombid(finalObject.getString("Combid"));
                        outboundPOChildDetilsEntity.setIslotfound(finalObject.getString("Islotfound"));
                        outboundPOChildDetilsEntity.setIsbatchfound(finalObject.getString("Isbatchfound"));
                        outboundPOChildDetilsEntity.setIslotserial(finalObject.getString("Islotserial"));
                        outboundPOChildDetilsEntity.setProductPickingLocId(finalObject.getString("ProductPickingLocId"));
                        if(Integer.parseInt(finalObject.getString("Quantity")) == Integer.parseInt(finalObject.getString("QuantityRecd"))){
                            outboundPOChildDetilsEntity.setStatus(1);
                        }else{
                            outboundPOChildDetilsEntity.setStatus(0);
                        }
                        outboundPOChildDetilsEntity.setQtyInMiddle(false);
                        outboundPOChildDetilsEntityList.add(outboundPOChildDetilsEntity);
                    }
                    Collections.sort(outboundPOChildDetilsEntityList);
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
        return outboundPOChildDetilsEntityList;
    }
}
