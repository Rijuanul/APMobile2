package id.advancepro.com.advancepro.service;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import id.advancepro.com.advancepro.model.OutboundOrderProductQuantityCheckEntity;

/**
 * Created by TASIV on 4/20/2017.
 */

public class OutboundOrderProductQuantityCheckService {
    private OutboundOrderProductQuantityCheckEntity outboundOrderProductQuantityCheckEntity;

    public OutboundOrderProductQuantityCheckEntity getOutboundProductQtyCheckResult(String urlLink,String jsonData){
        HttpURLConnection connection = null;
        BufferedReader reader = null;

        try {

            URL url = new URL(urlLink);
            connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(2000);
            connection.setRequestProperty("Content-type", "application/json");
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());
            writer.write(jsonData);
            writer.flush();

            //System.out.println("Connection:" + connection.getResponseCode());
            if (connection.getResponseCode() == 200) {
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
                JSONArray jsonarray = json.getJSONArray("CheckOutBoundOrderQuantitytboundQuantityResult");
                if (jsonarray.length() > 0) {
                    outboundOrderProductQuantityCheckEntity=new OutboundOrderProductQuantityCheckEntity();
                    for (int i = 0; i < jsonarray.length(); i++) {
                        JSONObject finalObject = jsonarray.getJSONObject(i);
                        outboundOrderProductQuantityCheckEntity.setId(finalObject.getString("id"));
                        outboundOrderProductQuantityCheckEntity.setStatus(finalObject.getString("Status"));
                    }
                }
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }

        return outboundOrderProductQuantityCheckEntity;
    }
}
