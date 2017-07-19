package id.advancepro.com.advancepro.service.SaleService.SalesServiceImpl;

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

import id.advancepro.com.advancepro.model.SalesCustListEntity;
import id.advancepro.com.advancepro.model.SalesDefaultSettingsEntity;
import id.advancepro.com.advancepro.model.SalesWarehouseEntity;
import id.advancepro.com.advancepro.service.SaleService.SalesService;

/**
 * Created by TASIV on 6/27/2017.
 */

public class SalesServiceImpl implements SalesService {

    private SalesDefaultSettingsEntity salesDefaultSettingsEntity;
    private List<SalesWarehouseEntity> salesWarehouseEntityList;
    private SalesWarehouseEntity salesWarehouseEntity;
    private List<SalesCustListEntity> salesCustListEntities;
    private SalesCustListEntity salesCustListEntity;
    private HttpURLConnection connection = null;
    private BufferedReader reader = null;
    private URL url;
    private String str;
    public SalesDefaultSettingsEntity getSalesDefaultSettings(String urlLink){
        try {
            //System.out.println(connection.getResponseCode());
            CommonConnectionSettings(urlLink);
            if(connection.getResponseCode() == 200) {
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
                if (jsonarray.length() > 0) {
                    salesDefaultSettingsEntity = new SalesDefaultSettingsEntity();
                    for (int i = 0; i < jsonarray.length(); i++) {
                        JSONObject finalObject = jsonarray.getJSONObject(i);
                        salesDefaultSettingsEntity.setSalesDefaultCustID(finalObject.getInt("custID"));
                        salesDefaultSettingsEntity.setSalesIsDirectInvoice(finalObject.getBoolean("is_invoice"));
                        salesDefaultSettingsEntity.setSalesIsAllowCustEdit(finalObject.getBoolean("allow_Cust_Edit"));
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
        return salesDefaultSettingsEntity;
    }

    //Get Sales WH List
    public List<SalesWarehouseEntity> getSalesWH(String urlLink){
        try{
            CommonConnectionSettings(urlLink);
            if(connection.getResponseCode() == 200) {
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
                if (jsonarray.length() > 0) {
                   salesWarehouseEntityList=new ArrayList<>();
                    for (int i = 0; i < jsonarray.length(); i++) {
                        JSONObject finalObject = jsonarray.getJSONObject(i);
                        salesWarehouseEntity=new SalesWarehouseEntity();
                        salesWarehouseEntity.setWhID(finalObject.getInt("whID"));
                        salesWarehouseEntity.setWhName(finalObject.getString("whName"));
                        salesWarehouseEntity.setDefault(finalObject.getBoolean("isWHDefault"));
                        salesWarehouseEntityList.add(salesWarehouseEntity);
                    }
                }
            }
        }catch (Exception ex) {
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
        return  salesWarehouseEntityList;
    }

    //Get Sales Cust List
    public List<SalesCustListEntity> getSalesCustList(String urlLink){
        try{
            CommonConnectionSettings(urlLink);
            if(connection.getResponseCode() == 200) {
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
                if (jsonarray.length() > 0) {
                    salesCustListEntities=new ArrayList<>();
                    for (int i = 0; i < jsonarray.length(); i++) {
                        JSONObject finalObject = jsonarray.getJSONObject(i);
                        salesCustListEntity=new SalesCustListEntity();
                        salesCustListEntity.setCustID(finalObject.getInt("custID"));
                        salesCustListEntity.setCustName(finalObject.getString("custName"));
                        salesCustListEntities.add(salesCustListEntity);
                    }
                }
            }
        }catch (Exception ex) {
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
        return  salesCustListEntities;
    }
    //Save Sales Settings
    public String saveSalesSettings(String urlLink,String jsonData){

        try {
            URL savesalesUrl = new URL(urlLink);
            connection = (HttpURLConnection) savesalesUrl.openConnection();
            connection.setConnectTimeout(10000);
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
                    //System.out.println(line);
                    str=line.replace("\"","");
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
        return str;
    }
    //Common Connection Settings
    private void CommonConnectionSettings(String urlLink){
        try{
            url = new URL(urlLink);
            connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(1000);
            connection.setRequestProperty("Content-type", "application/json");
            connection.setRequestProperty("Accept-Charset", "UTF-8");
            connection.setRequestMethod("GET");
            connection.connect();
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }
}

