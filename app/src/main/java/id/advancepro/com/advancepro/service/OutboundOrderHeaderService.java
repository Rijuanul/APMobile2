package id.advancepro.com.advancepro.service;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import id.advancepro.com.advancepro.model.OutboundOrderHeaderEntity;

/**
 * Created by TASIV on 4/19/2017.
 */

public class OutboundOrderHeaderService {
    OutboundOrderHeaderEntity outboundOrderHeaderEntity=null;
    public OutboundOrderHeaderEntity getOutboundOrderHeader(String urlLink){
        HttpURLConnection connection = null;
        BufferedReader reader = null;
        try {
            URL url = new URL(urlLink);
            connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(1500);
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
                    outboundOrderHeaderEntity=new OutboundOrderHeaderEntity();
                    for (int i = 0; i < jsonarray.length(); i++) {
                        JSONObject finalObject = jsonarray.getJSONObject(i);
                        outboundOrderHeaderEntity.setOrderID(finalObject.getString("orderID"));
                        outboundOrderHeaderEntity.setPurchaseno(finalObject.getString("Purchaseno"));
                        outboundOrderHeaderEntity.setCompanyName(finalObject.getString("CompanyName"));
                        outboundOrderHeaderEntity.setQuantityReceived(finalObject.getString("QuantityReceived"));
                        outboundOrderHeaderEntity.setTotQuantity(finalObject.getString("TotQuantity"));
                        outboundOrderHeaderEntity.setVpowhnotes(finalObject.getString("Vpowhnotes"));
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
        return outboundOrderHeaderEntity;
    }
}
