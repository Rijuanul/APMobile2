package id.advancepro.com.advancepro.service.SaleService.SalesServiceImpl;

import android.graphics.Bitmap;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;

import id.advancepro.com.advancepro.model.SalesOrderProductHierarchyEntity;
import id.advancepro.com.advancepro.model.SalesSearchProductEntity;
import id.advancepro.com.advancepro.model.SalesVariantDetailsEntity;
import id.advancepro.com.advancepro.model.SalesVariantItemEntity;
import id.advancepro.com.advancepro.model.SalesVariantProductInfoEntity;
import id.advancepro.com.advancepro.service.CommonGetPostService;
import id.advancepro.com.advancepro.service.SaleService.SalesOrderService;

/**
 * Created by TASIV on 7/5/2017.
 */

public class SalesOrderServiceImpl implements SalesOrderService {

    private CommonGetPostService commonGetPostService=new CommonGetPostService();
    private List<SalesOrderProductHierarchyEntity> salesOrderProductHierarchyEntities;
    private SalesOrderProductHierarchyEntity salesOrderProductHierarchyEntity;
    private List<SalesSearchProductEntity> salesSearchProductEntities;
    private SalesSearchProductEntity salesSearchProductEntity;
    private HttpURLConnection connection = null;
    private BufferedReader reader = null;
    private Bitmap bitmap;


    public List<SalesOrderProductHierarchyEntity> getSalesOrderProductHierarchy(String urlLink){
        try {
            connection = commonGetPostService.initiateGetConnection(urlLink);
            if (connection != null) {
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
                    salesOrderProductHierarchyEntities=new ArrayList<>();
                    salesOrderProductHierarchyEntity = new SalesOrderProductHierarchyEntity();
                    salesOrderProductHierarchyEntity.setId(0);
                    salesOrderProductHierarchyEntity.setName("- All -");
                    salesOrderProductHierarchyEntity.setParentID(0);
                    salesOrderProductHierarchyEntity.setErrorMsg("");
                    salesOrderProductHierarchyEntities.add(salesOrderProductHierarchyEntity);
                    for (int i = 0; i < jsonarray.length(); i++) {
                        JSONObject finalObject = jsonarray.getJSONObject(i);
                        salesOrderProductHierarchyEntity = new SalesOrderProductHierarchyEntity();
                        salesOrderProductHierarchyEntity.setId(finalObject.getInt("id"));
                        salesOrderProductHierarchyEntity.setName(finalObject.getString("name"));
                        salesOrderProductHierarchyEntity.setParentID(finalObject.getInt("parentID"));
                        salesOrderProductHierarchyEntity.setErrorMsg(finalObject.getString("ErrorMessage"));
                        salesOrderProductHierarchyEntities.add(salesOrderProductHierarchyEntity);
                    }
                }
            }
        }catch (Exception ex){
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
        return salesOrderProductHierarchyEntities;
    }

    public List<SalesSearchProductEntity> getSalesSearchProduct(String urlLink,String service_url,String companyDBName,String passCode){
        try {
            connection = commonGetPostService.initiateGetConnection(urlLink);
            if (connection != null) {
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
                    salesSearchProductEntities=new ArrayList<>();
                    for (int i = 0; i < jsonarray.length(); i++) {
                        JSONObject finalObject = jsonarray.getJSONObject(i);
                        salesSearchProductEntity = new SalesSearchProductEntity();
                        salesSearchProductEntity.setPRODUCT_ID(finalObject.getInt("PRODUCT_ID"));
                        salesSearchProductEntity.setCOMB_ID(finalObject.getInt("COMB_ID"));
                        salesSearchProductEntity.setPRODUCT_NAME(finalObject.getString("PRODUCT_NAME"));
                        salesSearchProductEntity.setSKU(finalObject.getString("SKU"));
                        salesSearchProductEntity.setCOST(finalObject.getDouble("PRICE"));
                        salesSearchProductEntity.setPRICE(finalObject.getDouble("PRICE"));
                        salesSearchProductEntity.setPRICENORMAL(finalObject.getDouble("PRICENORMAL"));
                        salesSearchProductEntity.setQTY(finalObject.getDouble("QTY"));
                        salesSearchProductEntity.setQTY_AVAIL(finalObject.getDouble("QTY_AVAIL"));
                        salesSearchProductEntity.setQTY_RESERVED(finalObject.getDouble("QTY_RESERVED"));
                        salesSearchProductEntity.setQTY_RETURNED(finalObject.getDouble("QTY_RETURNED"));
                        salesSearchProductEntity.setQTY_IN_STOCK(finalObject.getDouble("QTY_IN_STOCK"));
                        salesSearchProductEntity.setMIN_PURCHASE_QTY(finalObject.getDouble("MIN_PURCHASE_QTY"));
                        salesSearchProductEntity.setIS_VARIANT(finalObject.getBoolean("IS_VARIANT"));
                        salesSearchProductEntity.setIS_PRODUCT_KIT(finalObject.getBoolean("IS_PRODUCT_KIT"));
                        salesSearchProductEntity.setIS_TAXABLE(finalObject.getBoolean("IS_TAXABLE"));
                        salesSearchProductEntity.setKIT_ID(finalObject.getInt("KIT_ID"));
                        salesSearchProductEntity.setIn_MultWH(finalObject.getBoolean("In_MultWH"));
                        salesSearchProductEntity.setUnitName(finalObject.getString("UnitName"));
                        salesSearchProductEntity.setProductUnitYN(finalObject.getBoolean("ProductUnitYN"));
                        salesSearchProductEntity.setIS_SERVICE(finalObject.getBoolean("IS_SERVICE"));
                        salesSearchProductEntity.setASSEMBLY_ID(finalObject.getInt("ASSEMBLY_ID"));
                        salesSearchProductEntity.setINVENTORY_ID(finalObject.getInt("INVENTORY_ID"));
                        salesSearchProductEntity.setIS_DROPSHIP_ITEM(finalObject.getBoolean("IS_DROPSHIP_ITEM"));
                        salesSearchProductEntity.setIS_CUSTOMERSKU(finalObject.getBoolean("IS_CUSTOMERSKU"));
                        salesSearchProductEntity.setErrorMessage(finalObject.getString("ErrorMessage"));
                        //getSalesSearchProductImage(service_url, companyDBName, passCode,finalObject.getInt("PRODUCT_ID"),finalObject.getInt("COMB_ID"),salesSearchProductEntity);
                        salesSearchProductEntities.add(salesSearchProductEntity);
                    }
                }
            }
        }catch (Exception ex){
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
        return salesSearchProductEntities;
    }

    //Get Sales Search Product Image
    private void getSalesSearchProductImage(String service_url,String companyDBName,String passCode,Integer prodID,Integer combID ,SalesSearchProductEntity salesSearchProductEntity){
        try{

            String makeUrl=service_url +"/"+ "SearchSalesProductImage" +"/" +companyDBName+ "/" + passCode + "/" + prodID +"/"+ combID;
            connection = commonGetPostService.initiateGetConnection(makeUrl);
            //System.out.println("pp: "+prodID);
            if (connection != null) {
                InputStream input = connection.getInputStream();
                reader = new BufferedReader(new InputStreamReader(input));
                StringBuffer buffer = new StringBuffer();
                String line = "";
                while ((line = reader.readLine()) != null) {
                    System.out.println("line"+line);
                    buffer.append(line);
                }

                //byte[] byteArray =  Base64.decode(line.getBytes(), Base64.DEFAULT) ;
                //System.out.println("line"+byteArray);
                System.out.println("line"+buffer);
                //byte [] decodeByte= line.getBytes();
                /*Bitmap bmp = BitmapFactory.decodeByteArray(decodeByte, 0, decodeByte.length);
                System.out.println(decodeByte);
                System.out.println(bmp);
                String finalJson = buffer.toString();
                JSONArray jsonarray = new JSONArray(finalJson);
                if (jsonarray.length() > 0) {
                    for (int i = 0; i < jsonarray.length(); i++) {
                        JSONObject finalObject = jsonarray.getJSONObject(i);
                        //String path=finalObject.getString("ImagePath").replace("\\","/");
                       /*ByteArrayOutputStream byteArray = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArray);
                        byte[] byteArr = byteArray.toByteArray();*//*
                      *//*  String imageInBase64 = Base64.encodeToString(finalObject.getString("ImagePath"), Base64.NO_WRAP | Base64.URL_SAFE);
                        System.out.println(imageInBase64);
                        byte [] decodeByte= Base64.decode(finalObject.getString("ImagePath"),Base64.NO_WRAP|Base64.URL_SAFE);
                        ByteArrayOutputStream byteArray = new ByteArrayOutputStream(decodeByte);
                        InputStream inputStream  = new ByteArrayInputStream(decodeByte);
                        Bitmap bitmap1  = BitmapFactory.decodeStream(inputStream);
                        Bitmap bitmap= BitmapFactory.decodeByteArray(decodeByte, 0, decodeByte.length);
                        System.out.println(decodeByte);
                        System.out.println(bitmap);
                        System.out.println(bitmap1);*//*
                        //byte [] decodeByte=Base64.encodeToString(finalObject.getString("ImagePath"), Base64.NO_WRAP | Base64.URL_SAFE);
                        String path=finalObject.getString("ImagePath").replaceAll("\\\\","");
                        System.out.println(path);
                        salesSearchProductEntity.setImagePath(path);
                    }
                }*/
            }
        }catch (Exception ex){
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
    }

    //Sales Variant Detials
    private List<SalesVariantItemEntity> salesVariantItemEntities;
    private List<SalesVariantDetailsEntity> salesVariantDetailsEntities;
    private SalesVariantDetailsEntity salesVariantDetailsEntity;
    private SalesVariantItemEntity salesVariantItemEntity;
    public List<SalesVariantItemEntity> getSalesVariantDetails(String urlLink, String service_url, String companyDBName, String passCode ,String prodID){
        try {
            connection = commonGetPostService.initiateGetConnection(urlLink);
            if (connection != null) {
                InputStream input = connection.getInputStream();
                reader = new BufferedReader(new InputStreamReader(input));
                StringBuffer buffer = new StringBuffer();
                String line = "";
                while ((line = reader.readLine()) != null) {
                    System.out.println("line"+line);
                    buffer.append(line);
                }
                String finalJson = buffer.toString();
                JSONArray jsonarray = new JSONArray(finalJson);
                if (jsonarray.length() > 0) {
                    salesVariantItemEntities=new ArrayList<>();

                    for (int i = 0; i < jsonarray.length(); i++) {
                        JSONObject finalObject = jsonarray.getJSONObject(i);
                        salesVariantItemEntity = new SalesVariantItemEntity();
                        salesVariantItemEntity.setVariantheader(finalObject.getString("Description"));
                        getSalesVariantDetailsList(  service_url,  companyDBName,  passCode,prodID,finalObject.getBoolean("IsDisplayVariant"),salesVariantItemEntity);
                        salesVariantItemEntities.add(salesVariantItemEntity);
                    }
                }
            }
        }catch (Exception ex){
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
        return salesVariantItemEntities;
    }
    //Get Sales Variant Details List
    private void getSalesVariantDetailsList(String service_url, String companyDBName, String passCode ,String prodID,Boolean isVariant,SalesVariantItemEntity salesVariantItemEntity){
        try {
            String urlLink=service_url +"/SalesVariantCategoryDetails/"+ companyDBName +"/"+passCode +"/"+prodID +"/"+isVariant;
            connection = commonGetPostService.initiateGetConnection(urlLink);
            if (connection != null) {
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
                    salesVariantDetailsEntities=new ArrayList<>();

                    for (int i = 0; i < jsonarray.length(); i++) {
                        JSONObject finalObject = jsonarray.getJSONObject(i);
                        salesVariantDetailsEntity = new SalesVariantDetailsEntity();
                        salesVariantDetailsEntity.setVarID(finalObject.getString("C045_VAR_ID"));
                        salesVariantDetailsEntity.setDescription(finalObject.getString("C045_DESCRIPTION"));
                        salesVariantDetailsEntity.setPropID(finalObject.getString("C045_PROP_ID"));
                        salesVariantDetailsEntities.add(salesVariantDetailsEntity);
                        salesVariantItemEntity.setSalesVariantChildItem(salesVariantDetailsEntities);
                    }
                }
            }
        }catch (Exception ex){
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

    }

    private List<SalesVariantProductInfoEntity> salesVariantProductInfoEntities;
    private SalesVariantProductInfoEntity salesVariantProductInfoEntity;
    private Boolean isCombIDMatch;
    //Get Sales Variant product Information
    public List<SalesVariantProductInfoEntity> getSalesVariantProdInfo(String urlLink, String service_url, String companyDBName, String passCode, String prodID, Integer custID, String jsonString){
        try{
            System.out.println("jsonString"+jsonString);
            connection = commonGetPostService.initiatePostConnection(urlLink,jsonString);
            if (connection != null) {
                InputStream input = connection.getInputStream();
                reader = new BufferedReader(new InputStreamReader(input));
                StringBuffer buffer = new StringBuffer();
                String line = "";
                while ((line = reader.readLine()) != null) {
                    System.out.println("line"+line);
                    buffer.append(line);
                }

                String finalJson = buffer.toString();
                JSONArray jsonarray = new JSONArray(finalJson);
                if (jsonarray.length() > 0) {
                    isCombIDMatch=false;
                    for (int i = 0; i < jsonarray.length(); i++) {
                        JSONObject firstObject = jsonarray.getJSONObject(i);
                        if(!isCombIDMatch) {
                            for (int j = 1; j < jsonarray.length(); j++) {
                                JSONObject secondObject = jsonarray.getJSONObject(j);
                                if (firstObject.getInt("CombID") == secondObject.getInt("CombID")) {
                                    isCombIDMatch = true;
                                    salesVariantProductInfoEntities=getSalesVariantProdInfoList( service_url,  companyDBName,  passCode,  prodID,  custID, secondObject.getInt("CombID"));
                                    break;
                                }
                            }
                        }
                    }

                }
            }

        }catch (Exception ex){
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
        return salesVariantProductInfoEntities;
    }

    //Get Sales Variant Product Information
    private List<SalesVariantProductInfoEntity> getSalesVariantProdInfoList( String service_url,  String companyDBName,  String passCode, String prodID, Integer custID, Integer CombID){
        try{
            String urlLink=service_url +"/GetSalesProductVariantInfo/"+companyDBName+"/"+passCode+"/"+custID+"/"+CombID+"/"+prodID;
            connection = commonGetPostService.initiateGetConnection(urlLink);
            if (connection != null) {
                InputStream input = connection.getInputStream();
                reader = new BufferedReader(new InputStreamReader(input));
                StringBuffer buffer = new StringBuffer();
                String line = "";
                while ((line = reader.readLine()) != null) {
                    System.out.println("line" + line);
                    buffer.append(line);
                }
                String finalJson = buffer.toString();
                JSONArray jsonarray = new JSONArray(finalJson);
                if (jsonarray.length() > 0) {
                    salesVariantProductInfoEntities=new ArrayList<>();
                    for (int i = 0; i < jsonarray.length(); i++) {
                        JSONObject finalObject = jsonarray.getJSONObject(i);
                        salesVariantProductInfoEntity=new SalesVariantProductInfoEntity();
                        salesVariantProductInfoEntity.setCOMB_ID(finalObject.getInt("COMB_ID"));
                        salesVariantProductInfoEntity.setPRODUCT_NAME(finalObject.getString("PRODUCT_NAME"));
                        salesVariantProductInfoEntity.setSKU(finalObject.getString("SKU"));
                        salesVariantProductInfoEntity.setQTY(finalObject.getDouble("QTY"));
                        salesVariantProductInfoEntity.setQTY_AVAIL(finalObject.getDouble("QTY_AVAIL"));
                        salesVariantProductInfoEntity.setUnitName(finalObject.getString("UnitName"));
                        salesVariantProductInfoEntity.setPRICE(finalObject.getDouble("PRICE"));
                        salesVariantProductInfoEntity.setPRODUCT_ID(finalObject.getInt("PRODUCT_ID"));
                        salesVariantProductInfoEntities.add(salesVariantProductInfoEntity);
                    }
                }
            }
        }catch (Exception ex){
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
        return salesVariantProductInfoEntities;
    }

}
