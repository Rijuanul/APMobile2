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

import id.advancepro.com.advancepro.model.NextCycleCountItemEntity;

/**
 * Created by TASIV on 5/12/2017.
 */

public class NextCycleCountService {

    private HttpURLConnection connection = null;
    private BufferedReader reader = null;
    List<NextCycleCountItemEntity> nextCycleCountItemEntities=null;
    NextCycleCountItemEntity nextCycleCountItemEntity;
    private Boolean isError;
    public List<NextCycleCountItemEntity> getNextItemCycleCount(String urlLink,Integer recordPerpage,Integer pageIndex,String passCode){

        try {
            String newUrlLink=urlLink + "/" + recordPerpage  + "/" + pageIndex + "/" + passCode;
            URL url = new URL(newUrlLink.replace(" ", "%20"));
            connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(2000);
            connection.setRequestProperty("Content-type", "application/json");
            connection.setRequestProperty("Accept-Charset", "UTF-8");
            connection.setRequestMethod("GET");
            connection.connect();
            //System.out.println(newUrlLink);
            //System.out.println(connection.getResponseCode() == 200);
            if(connection.getResponseCode() == 200) {
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
                if(jsonarray.length()>0 ) {
                    isError=false;
                    if(nextCycleCountItemEntities==null) {
                        nextCycleCountItemEntities = new ArrayList<>();
                        //System.out.println("Open");
                    }
                    for (int i = 0; i < jsonarray.length(); i++) {
                        JSONObject finalObject = jsonarray.getJSONObject(i);
                        if(finalObject.getString("ErrorMessage").equals("")) {
                            nextCycleCountItemEntity = new NextCycleCountItemEntity();
                            nextCycleCountItemEntity.setProductID(finalObject.getString("ProductID"));
                            nextCycleCountItemEntity.setComboid(finalObject.getString("Comboid"));
                            nextCycleCountItemEntity.setProductname(finalObject.getString("Productname"));
                            nextCycleCountItemEntity.setSku(finalObject.getString("Sku"));
                            nextCycleCountItemEntity.setIsItemSelected(0);
                            nextCycleCountItemEntities.add(nextCycleCountItemEntity);
                        }else{
                            isError=true;
                            break;
                        }
                    }
                    if(!isError) {
                        pageIndex = pageIndex + 1;
                        //System.out.println(pageIndex);
                        getNextItemCycleCount(urlLink, recordPerpage, pageIndex, passCode);
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
        return  nextCycleCountItemEntities;
    }
}
