package id.advancepro.com.advancepro.service;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by TASIV on 3/16/2017.
 */
public class InternetConnection {
    // Internet connection checking code
    public Boolean checkConnection(Context context){
        Boolean conn=false;
       try {
           ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
           NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
           if (null != activeNetwork) {
               if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI) {
                   conn = true;
               } else if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE) {
                   conn = true;
               }
           } else {
               conn = false;
           }
       }catch (Exception ex){
           ex.printStackTrace();
       }
        return conn;
    }
}
