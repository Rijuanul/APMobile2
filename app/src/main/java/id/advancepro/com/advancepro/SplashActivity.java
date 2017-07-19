package id.advancepro.com.advancepro;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import org.apache.commons.lang3.StringUtils;

import id.advancepro.com.advancepro.utils.Constant;

public class SplashActivity extends AppCompatActivity {

    private SharedPreferences sharedpreferences;
    private String passCode;
    private String service_url;
    private Context connContext;
    private SplashActivity splashActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        connContext = this; // set current context
        splashActivity=this; // set current activity
        //SharedPreferences Value
        sharedpreferences = getSharedPreferences(Constant.LOGIN_PREF, Context.MODE_PRIVATE); // Get storage value
        passCode=sharedpreferences.getString(Constant.PASS_CODE,null);
        service_url=sharedpreferences.getString(Constant.SERVICE_URL,null);

        Thread splashThread=new Thread() {
            public void run() { // Threadd For Splash Screen
                try {
                    sleep(2000); // 2 ms sleep for splash screen
                    if(StringUtils.isEmpty(passCode) && StringUtils.isEmpty(service_url)){ // if New user then call to Start Page
                        Intent intent= new Intent(connContext,StartActivity.class);
                        startActivity(intent);
                        finish();
                    }else{ // if service url and Security code already saved then call to Login Page
                        Intent intent= new Intent(connContext,LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        };
        splashThread.start();
    }


}
