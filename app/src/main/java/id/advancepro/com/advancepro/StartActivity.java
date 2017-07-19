package id.advancepro.com.advancepro;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

import id.advancepro.com.advancepro.adapter.VibratorAdaptor;
import id.advancepro.com.advancepro.model.CompanyDBNameEntity;
import id.advancepro.com.advancepro.service.CompanyDBService;
import id.advancepro.com.advancepro.service.InternetConnection;
import id.advancepro.com.advancepro.utils.Constant;

public class StartActivity extends AppCompatActivity {

    private EditText serviceUrl;
    private EditText securityKey;
    private Button submitButton;
    private String service_url = "";
    private String security_key = "";
    private String security_key_encrypt = "";
    private String storeServiceUrl;
    private String makeURL;
    private InternetConnection intConn=null;
    private SharedPreferences sharedpreferences;
    private Context connContext;
    private CompanyDBService cdbService=null;
    private VibratorAdaptor vibratorAdaptor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        serviceUrl = (EditText) findViewById(R.id.serviceurl);
        securityKey = (EditText) findViewById(R.id.securitykey);
        submitButton = (Button) findViewById(R.id.serviceUrlSubmit);

        serviceUrl.setText(""); //clear service url field value
        securityKey.setText(""); //clear security key field value
        intConn=new InternetConnection();
        connContext = this; // get current context
        //Vibrator
        vibratorAdaptor=new VibratorAdaptor(getApplicationContext());//Initialize Vibrator Class

        // Submit button click event and check serive url , security key field
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                service_url = serviceUrl.getText().toString().trim(); // trim service url value
                security_key = securityKey.getText().toString().trim(); // trim security value
                if (!(StringUtils.isEmpty(service_url)) && !(StringUtils.isEmpty(security_key))) { // Empty checking
                    encryptSecurityKey(); // Encrpty Security key
                    setWebseriveUrlAndSecurityKey(); // calling function
                } else {
                    Toast.makeText(StartActivity.this, "Empty Field Exist", Toast.LENGTH_SHORT).show();
                    vibratorAdaptor.makeVibration();
                }
            }
        });
        //End of submit button click event
    }

    private void encryptSecurityKey() {
        // Encrypt security key
        security_key_encrypt = new String(Hex.encodeHex(DigestUtils.md5(security_key)));
    }

    private void setWebseriveUrlAndSecurityKey() {
        Boolean checkInternet=intConn.checkConnection(connContext); // Internet connection check
        if(checkInternet){
            storeServiceUrl=service_url+"/APMobileService.svc";
            makeURL=storeServiceUrl+ "/GetCompanyList/" + security_key_encrypt;
            new JSONTask().execute(makeURL);
        }else{
            Toast.makeText(StartActivity.this, getApplicationContext().getResources().getString(R.string.error001), Toast.LENGTH_SHORT).show();
            vibratorAdaptor.makeVibration();
        }
    }

    public class JSONTask extends AsyncTask<String, String, List<CompanyDBNameEntity>> {
        @Override
        protected List<CompanyDBNameEntity> doInBackground(String... params) {
            // call to webservice for company list
            try {
                CompanyDBService cdbService = new CompanyDBService();
                return cdbService.getCompanyDBList(params[0]);
            }catch (Exception ex){
                ex.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(List<CompanyDBNameEntity> companyList) {
            try {
               //System.out.println("s" + " "+service_url+" "+companyList);
                if (companyList != null && companyList.size() > 0) {
                    if(companyList.get(1).getErrorMessage().equals("")) {
                        sharedpreferences = getSharedPreferences(Constant.LOGIN_PREF, connContext.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedpreferences.edit();
                        editor.putString(Constant.SERVICE_URL, storeServiceUrl);
                        editor.putString(Constant.PASS_CODE, security_key_encrypt);
                        editor.commit();
                        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }else{
                        Toast.makeText(StartActivity.this,companyList.get(1).getErrorMessage() , Toast.LENGTH_SHORT).show();
                        vibratorAdaptor.makeVibration();
                    }
                } else {
                    Toast.makeText(StartActivity.this, getApplicationContext().getResources().getString(R.string.error002), Toast.LENGTH_SHORT).show();
                    vibratorAdaptor.makeVibration();
                }
            }catch (Exception ex){
                ex.printStackTrace();
            }
        }
    }
}
