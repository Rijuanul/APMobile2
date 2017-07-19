package id.advancepro.com.advancepro.service;

import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by TASIV on 7/5/2017.
 */

public class CommonGetPostService {

    private HttpURLConnection connection = null;
    private URL url;

    //Post Connection
    public HttpURLConnection initiatePostConnection(String urlLink,String jsonData){
        try {
            url = new URL(urlLink);
            connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(5000);
            connection.setRequestProperty("Content-type", "application/json");
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());
            writer.write(jsonData);
            writer.flush();

            //System.out.println(connection.getResponseCode());
            if (connection.getResponseCode() == 200) {
                return connection;
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return connection;
    }
    //Get Connection
    public HttpURLConnection initiateGetConnection(String urlLink) {
        try {
            url = new URL(urlLink);
            connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(5000);
            connection.setRequestProperty("Content-type", "application/json");
            connection.setRequestProperty("Accept-Charset", "UTF-8");
            connection.setRequestMethod("GET");
            connection.connect();

            if (connection.getResponseCode() == 200) {
                return connection;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return connection;
    }


}
