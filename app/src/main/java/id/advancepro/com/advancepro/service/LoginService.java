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

import id.advancepro.com.advancepro.model.LoginInfoEntity;
import id.advancepro.com.advancepro.utils.Constant;


/**
 * Created by Rijuanul on 3/19/2017.
 */

// Login Service code and Call to webservice
public class LoginService {

    private LoginInfoEntity loginInfoEntity=null;
    public LoginInfoEntity sendLoginInfo(String urlLink,String json){

        HttpURLConnection connection = null;
        BufferedReader reader = null;
        try {

            URL url = new URL(urlLink);
            connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(5000);
            connection.setRequestProperty("Content-type", "application/json");
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            OutputStreamWriter writer=new OutputStreamWriter(connection.getOutputStream());
            //System.out.println(json);
            writer.write(json);
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
                    loginInfoEntity = new LoginInfoEntity();
                    loginInfoEntity.setStatus(finalObject.getString("Status"));
                    loginInfoEntity.setUserID(finalObject.getString("UserID"));
                    loginInfoEntity.setUserName(finalObject.getString("Username"));
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
        return loginInfoEntity;
    }

    private void errorMsgSet(){
        loginInfoEntity = new LoginInfoEntity();
        loginInfoEntity.setStatus(Constant.SERVER_ERROR);
        loginInfoEntity.setUserID("0");
        loginInfoEntity.setUserName("0");
    }
}
