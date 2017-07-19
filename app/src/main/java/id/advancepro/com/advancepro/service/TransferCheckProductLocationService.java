package id.advancepro.com.advancepro.service;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import id.advancepro.com.advancepro.model.TransferCheckProductLocationEntity;

/**
 * Created by TASIV on 4/17/2017.
 */

public class TransferCheckProductLocationService {

    TransferCheckProductLocationEntity transferCheckProductLocationEntity;

    public TransferCheckProductLocationEntity getCheckProductLocation(String urlLink,String jsonData){
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

            //System.out.println(connection.getResponseCode());
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
                JSONArray jsonarray = new JSONArray(finalJson);
                for (int i = 0; i < jsonarray.length(); i++) {
                    JSONObject finalObject = jsonarray.getJSONObject(i);
                    transferCheckProductLocationEntity=new TransferCheckProductLocationEntity();
                    transferCheckProductLocationEntity.setId(finalObject.getString("id"));
                    transferCheckProductLocationEntity.setComboid(finalObject.getString("Comboid"));
                    transferCheckProductLocationEntity.setLotno(finalObject.getString("Lotno"));
                    transferCheckProductLocationEntity.setSerialno(finalObject.getString("Serialno"));
                    transferCheckProductLocationEntity.setProductid(finalObject.getString("Productid"));
                }
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return transferCheckProductLocationEntity;
    }
}
