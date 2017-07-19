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

import id.advancepro.com.advancepro.model.BoundSettingsEntity;
import id.advancepro.com.advancepro.model.OutboundHeaderDetailsEntity;
import id.advancepro.com.advancepro.utils.Constant;

/**
 * Created by Rijuanul on 4/18/2017.
 */

public class OutboundHeaderDetailsService {

    private String makeURL;
    private String passCode;
    private String service_url;
    private String companyDBName;
    private Integer userID;
    private Integer warehouseID;
    private Integer pagePerIndex;
    private  Integer typeOfOutbound;
    private  String jsonString;
    private List<OutboundHeaderDetailsEntity> outboundHeaderDetailsEntityList;
    private OutboundHeaderDetailsEntity outboundHeaderDetailsEntity;
    private BoundSettingsEntity outboundSettingsEntityRef;
    private List<String> vendoproductNameList;

    public void initalValue(String serviceURL, String security_key, Integer user_id, String company_DB, Integer warehouse_id, Integer index_per_page, Integer type_of_outbound, BoundSettingsEntity outboundSettingsEntity){
        service_url=serviceURL;
        passCode=security_key;
        userID=user_id;
        companyDBName=company_DB;
        warehouseID=warehouse_id;
        pagePerIndex=index_per_page;
        typeOfOutbound=type_of_outbound;
        outboundSettingsEntityRef=outboundSettingsEntity;
        outboundHeaderDetailsEntityList=new ArrayList<OutboundHeaderDetailsEntity>();
    }

    public List<OutboundHeaderDetailsEntity> getOutboundHeaderDetails(String urlLink){
        HttpURLConnection connection = null;
        BufferedReader reader = null;
        try {

            URL url = new URL(urlLink);
            connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(2000);
            connection.setRequestProperty("Content-type", "application/json");
            connection.setRequestMethod("GET");
            connection.connect();
            //System.out.println(urlLink);
            //System.out.println(connection.getResponseCode());
            if(connection.getResponseCode() == 200){
                InputStream input = connection.getInputStream();
                reader = new BufferedReader(new InputStreamReader(input));
                StringBuffer buffer = new StringBuffer();
                String line = "";
                System.out.println(reader);
                while ((line = reader.readLine()) != null) {
                    //System.out.println("line"+line);
                    buffer.append(line);
                }
                String finalJson = buffer.toString();
                JSONArray jsonarray = new JSONArray(finalJson);
                if(jsonarray.length()>0) {
                    for (int i = 0; i < jsonarray.length(); i++) {
                        JSONObject finalObject = jsonarray.getJSONObject(i);
                        outboundHeaderDetailsEntity = new OutboundHeaderDetailsEntity();
                        outboundHeaderDetailsEntity.setOrderNo(finalObject.getString("OrderNo"));
                        outboundHeaderDetailsEntity.setPoNo(finalObject.getString("PoNo"));
                        outboundHeaderDetailsEntity.setCompanyname(finalObject.getString("Companyname"));
                        outboundHeaderDetailsEntity.setOrderDate(finalObject.getString("OrderDate"));
                        outboundHeaderDetailsEntity.setExpectedDate(finalObject.getString("ExpectedDate"));
                        outboundHeaderDetailsEntity.setQuantity(finalObject.getString("Quantity"));
                        outboundHeaderDetailsEntity.setStatusID(finalObject.getString("StatusID"));
                        String vendorProductAndQtyList = "";
                        if (Integer.parseInt(outboundSettingsEntityRef.getIsShowProdName()) == 1) {
                            vendorProductAndQtyList = getOutboundHeaderSubItem(finalObject.getString("OrderNo"));
                        }
                        outboundHeaderDetailsEntity.setVendorProductNameAndQuantity(vendorProductAndQtyList);
                        outboundHeaderDetailsEntityList.add(outboundHeaderDetailsEntity);
                    }

                    /*if (jsonarray != null && jsonarray.length() > 0) {
                        setMakeURL();
                    }*/
                }
            }else{
                errorMsgSet();
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            errorMsgSet();
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
        return outboundHeaderDetailsEntityList;
    }


    private void setMakeURL(){
        try{
            pagePerIndex=pagePerIndex+1;
            makeURL=service_url +"/GetOutBoundHeaderDetails/"+ companyDBName + "/" + userID + "/" + typeOfOutbound + "/" + warehouseID + "/" + Constant.RECORD_PER_PAGE + "/" + pagePerIndex + "/" + passCode;
            getOutboundHeaderDetails(makeURL);
        }catch (Exception ex){
            ex.printStackTrace();
        }

    }

    private void errorMsgSet(){
        outboundHeaderDetailsEntityList=null;
    }

    private String getOutboundHeaderSubItem(String orderNo){
        try{
            Integer orderNumber=Integer.parseInt(orderNo);
            makeURL=service_url + "/GetOutBoundChildDetails/" + companyDBName + "/" + userID + "/" + orderNumber + "/" + passCode;
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return getVendorproductNameList(makeURL);
    }

    private String getVendorproductNameList(String urlLink){
        HttpURLConnection connection = null;
        BufferedReader reader = null;
        String str="<li>";
        try {
            URL url = new URL(urlLink.replace(" ", "%20"));
            connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(2000);
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
                vendoproductNameList=new ArrayList<>();
                for (int i = 0; i < jsonarray.length(); i++) {
                    JSONObject finalObject = jsonarray.getJSONObject(i);
                    str=str+"<p><span>"+finalObject.getString("VendorProductName") +"</span>"+"&nbsp;&nbsp;&nbsp;&nbsp;"+"<span>QTY: "+ finalObject.getString("Quantity")+"</span></p>";
                }
            }
            str=str+"</li>";

        }  catch (Exception ex){
            ex.printStackTrace();

        }finally {
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
}
