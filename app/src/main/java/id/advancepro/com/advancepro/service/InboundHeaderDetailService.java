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

import id.advancepro.com.advancepro.model.BoundSettingsEntity;
import id.advancepro.com.advancepro.model.InboundHeaderDetailsEntity;
import id.advancepro.com.advancepro.utils.Constant;

/**
 * Created by TASIV on 3/28/2017.
 */

public class InboundHeaderDetailService {

    private String makeURL;
    private String passCode;
    private String service_url;
    private String companyDBName;
    private Integer userID;
    private Integer warehouseID;
    private Integer pagePerIndex;
    private  Integer typeOfInbound;
    private  String jsonString;
    private List<InboundHeaderDetailsEntity> inboundHeaderDetailsEntitiesList;
    private InboundHeaderDetailsEntity inboundHeaderDetailsEntity;
    private BoundSettingsEntity boundSettingsEntityRef;
    private List<String> vendoproductNameList;


    public void initalValue(String serviceURL, String security_key, Integer user_id, String company_DB, Integer warehouse_id, Integer index_per_page, Integer type_of_inbound, BoundSettingsEntity boundSettingsEntity){
        service_url=serviceURL;
        passCode=security_key;
        userID=user_id;
        companyDBName=company_DB;
        warehouseID=warehouse_id;
        pagePerIndex=index_per_page;
        typeOfInbound=type_of_inbound;
        boundSettingsEntityRef = boundSettingsEntity;
        inboundHeaderDetailsEntitiesList=new ArrayList<InboundHeaderDetailsEntity>();
    }

    public List<InboundHeaderDetailsEntity> getInboundHeaderDetails(String urlLink, String stringJson){
        HttpURLConnection connection = null;
        BufferedReader reader = null;
        try {

            URL url = new URL(urlLink);
            connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(1000);
            connection.setRequestProperty("Content-type", "application/json");
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            OutputStreamWriter writer=new OutputStreamWriter(connection.getOutputStream());
            writer.write(stringJson);
            writer.flush();
            //System.out.println(urlLink);
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
                //System.out.println("Json Array"+jsonarray.length());
                if(jsonarray.length()>0) {
                    for (int i = 0; i < jsonarray.length(); i++) {
                        JSONObject finalObject = jsonarray.getJSONObject(i);
                        inboundHeaderDetailsEntity = new InboundHeaderDetailsEntity();
                        inboundHeaderDetailsEntity.setOrderNo(finalObject.getString("OrderNo"));
                        inboundHeaderDetailsEntity.setPurchaseorderno(finalObject.getString("Purchaseorderno"));
                        inboundHeaderDetailsEntity.setCompanyname(finalObject.getString("Companyname"));
                        inboundHeaderDetailsEntity.setOrderdate(finalObject.getString("Orderdate"));
                        inboundHeaderDetailsEntity.setExpecteddate(finalObject.getString("Expecteddate"));
                        inboundHeaderDetailsEntity.setQuantity(finalObject.getString("Quantity"));
                        inboundHeaderDetailsEntity.setQuantityreceived(finalObject.getString("Quantityreceived"));
                        inboundHeaderDetailsEntity.setStatus(finalObject.getString("Status"));
                        inboundHeaderDetailsEntity.setErrorMessage(finalObject.getString("ErrorMessage"));
                        //System.out.println(boundSettingsEntityRef.getIsShowProdName());
                        String vendorProductAndQtyList = "";
                        if (Integer.parseInt(boundSettingsEntityRef.getIsShowProdName()) == 1) {
                            vendorProductAndQtyList = getInboundHeaderSubItem(finalObject.getString("OrderNo"));
                        /*if(!StringUtils.isEmpty(vendorProductAndQtyList)){

                        }*/
                        }
                        inboundHeaderDetailsEntity.setVendorProductNameAndQty(vendorProductAndQtyList);
                        inboundHeaderDetailsEntitiesList.add(inboundHeaderDetailsEntity);
                    }

                    if (jsonarray != null && jsonarray.length() > 0) {
                        setJsonString();
                        setMakeURL();
                    }
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
        return inboundHeaderDetailsEntitiesList;
    }

    private  void setJsonString(){
        pagePerIndex=pagePerIndex+1;
        try{
            final JSONObject root = new JSONObject();
            root.put("CompanyDbName", companyDBName);
            root.put("UserId", userID);
            root.put("RecevingButtonId", typeOfInbound);
            root.put("WhID", warehouseID);
            root.put("Rows", Constant.RECORD_PER_PAGE);
            root.put("clickcount", pagePerIndex);
            root.put("Key", passCode);
            root.put("SearchKey", "");
            jsonString= root.toString();

        }catch (Exception ex){
            ex.printStackTrace();
        }

    }
    private void setMakeURL(){
        try{
            makeURL = service_url +"/GetInBoundHeaderDetails";
            getInboundHeaderDetails(makeURL,jsonString);
        }catch (Exception ex){
            ex.printStackTrace();
        }

    }

    private void errorMsgSet(){
        inboundHeaderDetailsEntitiesList=null;
    }

    private String getInboundHeaderSubItem(String orderNo){
        try{
            Integer orderNumber=Integer.parseInt(orderNo);
            makeURL = service_url +"/GetInBoundChildDetails/" + companyDBName +"/"+ userID +"/"+ orderNumber +"/"+ passCode ;
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
            connection.setConnectTimeout(1000);
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
                    //String str=finalObject.getString("VendorProductName") +"   "+ finalObject.getString("Quantity");
                    str=str+"<p><span>"+finalObject.getString("VendorProductName") +"</span>"+"&nbsp;&nbsp;&nbsp;&nbsp;"+"<span>QTY: "+ finalObject.getString("Quantity")+"</span></p>";
                    //vendoproductNameList.add(str);
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
        //return vendoproductNameList;
    }
}
