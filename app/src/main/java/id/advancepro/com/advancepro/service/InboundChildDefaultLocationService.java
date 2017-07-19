package id.advancepro.com.advancepro.service;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import id.advancepro.com.advancepro.model.InboundChildDefaultPickLocationEntity;

/**
 * Created by TASIV on 4/6/2017.
 */

public class InboundChildDefaultLocationService {

    private InboundChildDefaultPickLocationEntity inboundChildDefaultPickLocationEntity =null;
    private List<InboundChildDefaultPickLocationEntity> inboundChildDefaultPickLocationEntityList =null;
    HttpURLConnection connection = null;
    BufferedReader reader = null;
    public InboundChildDefaultPickLocationEntity getInboundChildDefaultLocation(String urlLink, String jsonData){
        try {

            URL url = new URL(urlLink);
            connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(1000);
            connection.setRequestProperty("Content-type", "application/json");
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
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
                    //System.out.println("line"+line);
                    buffer.append(line);
                }
                String finalJson = buffer.toString();
                JSONArray jsonarray = new JSONArray(finalJson);
                for (int i = 0; i < jsonarray.length(); i++) {
                    JSONObject finalObject = jsonarray.getJSONObject(i);
                    if(finalObject.getInt("Isdefault")==1) {
                        inboundChildDefaultPickLocationEntity = new InboundChildDefaultPickLocationEntity();
                        inboundChildDefaultPickLocationEntity.setLocationName(finalObject.getString("LocationName"));
                        inboundChildDefaultPickLocationEntity.setLocationID(finalObject.getInt("LocationID"));
                        inboundChildDefaultPickLocationEntity.setIsdefault(finalObject.getInt("Isdefault"));
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
        return inboundChildDefaultPickLocationEntity;
    }

    public List<InboundChildDefaultPickLocationEntity> getInboundChildLocationList(String urlLink, String jsonData){
        try {

            URL url = new URL(urlLink);
            connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(1000);
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
                    //System.out.println("line"+line);
                    buffer.append(line);
                }
                String finalJson = buffer.toString();
                JSONArray jsonarray = new JSONArray(finalJson);
                if(jsonarray.length()>0) {
                    inboundChildDefaultPickLocationEntityList=new ArrayList<>();
                    inboundChildDefaultPickLocationEntity = new InboundChildDefaultPickLocationEntity();
                    inboundChildDefaultPickLocationEntity.setLocationName("--Select--");
                    inboundChildDefaultPickLocationEntity.setLocationID(0);
                    inboundChildDefaultPickLocationEntity.setIsdefault(0);
                    inboundChildDefaultPickLocationEntityList.add(inboundChildDefaultPickLocationEntity);
                    for (int i = 0; i < jsonarray.length(); i++) {
                        JSONObject finalObject = jsonarray.getJSONObject(i);
                        //if (finalObject.getInt("Isdefault") == 1) {
                            inboundChildDefaultPickLocationEntity = new InboundChildDefaultPickLocationEntity();
                            inboundChildDefaultPickLocationEntity.setLocationName(finalObject.getString("LocationName"));
                            inboundChildDefaultPickLocationEntity.setLocationID(finalObject.getInt("LocationID"));
                            inboundChildDefaultPickLocationEntity.setIsdefault(finalObject.getInt("Isdefault"));
                            inboundChildDefaultPickLocationEntityList.add(inboundChildDefaultPickLocationEntity);
                        //}
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
        return inboundChildDefaultPickLocationEntityList;
    }
}
