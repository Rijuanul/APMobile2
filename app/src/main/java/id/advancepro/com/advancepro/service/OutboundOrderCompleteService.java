package id.advancepro.com.advancepro.service;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by TASIV on 4/20/2017.
 */

public class OutboundOrderCompleteService {
    private String str;

    public String completeOutboundOrder(String urlLink,String jsonData){
        HttpURLConnection connection = null;
        BufferedReader reader = null;
        try{
            URL url = new URL(urlLink);
            connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(10000);
            connection.setRequestProperty("Content-type", "application/json");
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            //System.out.println(jsonData);
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
                    //System.out.println(line);
                    str=line.replace("\"","");
                }

            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return str;
    }
}
