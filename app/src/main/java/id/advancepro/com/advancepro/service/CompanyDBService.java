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

import id.advancepro.com.advancepro.model.CompanyDBNameEntity;
import id.advancepro.com.advancepro.utils.Constant;

/**
 * Created by TASIV on 3/15/2017.
 */

public class CompanyDBService {
    private List<CompanyDBNameEntity> resultList=null;
    // Web service call for getting company List from Database
    public List<CompanyDBNameEntity> getCompanyDBList(String urlLink){

        HttpURLConnection connection = null;
        BufferedReader reader = null;
        try {
            URL url = new URL(urlLink);
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
                if(jsonarray.length()>0) {
                    //System.out.println("jsonarray--"+jsonarray);
                    CompanyDBNameEntity companyEntityForDemoValue = new CompanyDBNameEntity();
                    companyEntityForDemoValue.setCompanyDBName("");
                    companyEntityForDemoValue.setCompanyName(Constant.SELECT_COMPANY_NAME);
                    resultList= new ArrayList<CompanyDBNameEntity>();

                    resultList.add(companyEntityForDemoValue);
                    for (int i = 0; i < jsonarray.length(); i++) {

                        JSONObject finalObject = jsonarray.getJSONObject(i);
                        //System.out.println("jsonarray1--"+finalObject.getString("ErrorMessage"));
                        CompanyDBNameEntity cdbEntity = new CompanyDBNameEntity();
                        cdbEntity.setCompanyDBName(finalObject.getString("CompanyDBname"));
                        cdbEntity.setCompanyName(finalObject.getString("CompanyName"));
                        cdbEntity.setErrorMessage(finalObject.getString("ErrorMessage"));
                        resultList.add(cdbEntity);
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
        return resultList;
    }
}
