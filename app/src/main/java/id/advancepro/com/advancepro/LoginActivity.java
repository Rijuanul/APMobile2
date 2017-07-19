package id.advancepro.com.advancepro;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;

import java.util.List;

import id.advancepro.com.advancepro.adapter.VibratorAdaptor;
import id.advancepro.com.advancepro.model.CompanyDBNameEntity;
import id.advancepro.com.advancepro.model.DefaultWareHouse;
import id.advancepro.com.advancepro.model.LoginInfoEntity;
import id.advancepro.com.advancepro.service.CheckMobileVersionService;
import id.advancepro.com.advancepro.service.CompanyDBService;
import id.advancepro.com.advancepro.service.DefaultWareHouseService;
import id.advancepro.com.advancepro.service.InternetConnection;
import id.advancepro.com.advancepro.service.LoginService;
import id.advancepro.com.advancepro.utils.Constant;

public class LoginActivity extends AppCompatActivity {

    private SharedPreferences sharedpreferences;
    private InternetConnection intConn=null;
    private Spinner companyListSpinner;
    private boolean mSpinnerInitialized=false;//Company List DropDown make false for first time page loading
    private String passCode;
    private String service_url;
    private String makeURL;
    private Button resetButton;
    private Button loginButton;
    private EditText userName;
    private EditText passWord;
    private String selectedCompanyDB;
    private String selectedComapnyName;
    private Boolean checkInternet=false;
    private String json;
    private VibratorAdaptor vibratorAdaptor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //SharedPreferences Value
        sharedpreferences = getSharedPreferences(Constant.LOGIN_PREF, Context.MODE_PRIVATE);
        passCode=sharedpreferences.getString(Constant.PASS_CODE,null);
        service_url=sharedpreferences.getString(Constant.SERVICE_URL,null);

        intConn=new InternetConnection(); //Internet Connection Initialize
        //Login Screen Field Initialize
        resetButton=(Button)findViewById(R.id.resetService) ;
        loginButton=(Button)findViewById(R.id.login) ;
        userName=(EditText)findViewById(R.id.username);
        passWord=(EditText)findViewById(R.id.password);
        companyListSpinner=(Spinner)findViewById(R.id.company); // Company List Dropdown
        //Vibrator
        vibratorAdaptor=new VibratorAdaptor(getApplicationContext());//Vibrator Class Initialize

        userName.setText("");
        passWord.setText("");

        checkInternet=intConn.checkConnection(this); // Internet connection check
        if(checkInternet) {
            // Get company List
            makeURL = service_url + "/GetCompanyList/" + passCode;
            new JSONTask().execute(makeURL);
        }else{
            internetConnectionMessage(); //Internet Connection Error Message
        }

        // Company selection from the dropdown
        companyListSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(!mSpinnerInitialized) {
                    mSpinnerInitialized=true;
                    return;
                }
                CompanyDBNameEntity selectedCompany = (CompanyDBNameEntity) parent.getSelectedItem();
                selectedCompanyDB=selectedCompany.getCompanyDBName();
                selectedComapnyName=selectedCompany.getCompanyName();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        // Reset Service URL
        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {

                    AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);// Alert Dialogue
                    builder.setMessage("Do you sure want to reset the service url ?");
                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener()
                    {
                        @Override
                        public void onClick(DialogInterface dialog, int which)
                        {
                            sharedpreferences.edit().clear().commit();
                            Intent intent = new Intent(getApplicationContext(), StartActivity.class);
                            startActivity(intent);
                            finish();

                            dialog.dismiss();
                        }
                    });
                    builder.setNegativeButton("No", new DialogInterface.OnClickListener()
                    {
                        @Override
                        public void onClick(DialogInterface dialog, int which)
                        {
                            dialog.dismiss();
                        }
                    });
                    builder.show(); // Alert Show
                }catch (Exception ex){
                    ex.printStackTrace();
                }
            }
        });

        // Login for homepage with username, password and selected company Database
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if (checkInternet) {
                        String login_user_name = userName.getText().toString();
                        String login_password = passWord.getText().toString();

                        //System.out.println(selectedCompanyDB);
                        if(StringUtils.isEmpty(login_user_name)){
                            Toast.makeText(LoginActivity.this, getApplicationContext().getResources().getString(R.string.errorUserName), Toast.LENGTH_SHORT).show();
                            vibratorAdaptor.makeVibration();
                        }else if(StringUtils.isEmpty(login_password)){
                            Toast.makeText(LoginActivity.this, getApplicationContext().getResources().getString(R.string.errorPassword), Toast.LENGTH_SHORT).show();
                            vibratorAdaptor.makeVibration();
                        }else if(StringUtils.isEmpty(selectedCompanyDB)) {
                            Toast.makeText(LoginActivity.this, getApplicationContext().getResources().getString(R.string.errorSelectCompany), Toast.LENGTH_SHORT).show();
                            vibratorAdaptor.makeVibration();
                        }else {
                            //System.out.println("MV");
                            checkMobileVersion();//Check Mobile Version
                        }
                    } else {
                        internetConnectionMessage();//Internet Connection Error Message
                    }
                }catch (Exception ex){
                    ex.printStackTrace();
                }
            }
        });

    }

    // Get company List
    public class JSONTask extends AsyncTask<String, String, List<CompanyDBNameEntity>> {
        @Override
        protected List<CompanyDBNameEntity> doInBackground(String... params) {
            // call to webservice for company list
            CompanyDBService cdbService=new CompanyDBService();
            return  cdbService.getCompanyDBList(params[0]);

    }

        @Override
        protected void onPostExecute(List<CompanyDBNameEntity> companyList) {
            try {
                //System.out.println("ss" + " "+service_url+" "+companyList);
                if (companyList != null && companyList.size() > 0) {
                    ArrayAdapter<CompanyDBNameEntity> adapter = new ArrayAdapter<CompanyDBNameEntity>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item,companyList);
                    //adapter.setDropDownViewResource(R.layout.activity_customspinner);
                    companyListSpinner.setAdapter(adapter);
                } else {
                    Toast.makeText(LoginActivity.this, getApplicationContext().getResources().getString(R.string.error002), Toast.LENGTH_SHORT).show();
                    vibratorAdaptor.makeVibration();
                }
            }catch (Exception ex){
                ex.printStackTrace();
            }
        }
    }
    //Internet Connection Error Message
    private void internetConnectionMessage(){
        Toast.makeText(LoginActivity.this, getApplicationContext().getResources().getString(R.string.error001), Toast.LENGTH_SHORT).show();
        vibratorAdaptor.makeVibration();

    }

    // Call to Webservice for Login
    public class LoginTask extends AsyncTask<String, String, LoginInfoEntity> {
        @Override
        protected LoginInfoEntity doInBackground(String... params) {
            LoginService loginService=new LoginService();
            return  loginService.sendLoginInfo(params[0],json);

        }

        @Override
        protected void onPostExecute(LoginInfoEntity loginEntity) {
            try {
                //System.out.println(loginEntity.getStatus());
                if(loginEntity!=null){
                    if(loginEntity.getStatus().equalsIgnoreCase("SUCCESSFULLY") && loginEntity.getUserID()!="0"){
                        //System.out.println("ssL" + loginEntity);
                        SharedPreferences.Editor editor = sharedpreferences.edit();
                        editor.putString(Constant.LOGIN_USERNAME, loginEntity.getUserName());
                        editor.putString(Constant.LOGIN_USERID, loginEntity.getUserID());
                        editor.putString(Constant.LOGIN_COMPANY_NAME, selectedComapnyName);
                        editor.putString(Constant.LOGIN_DB, selectedCompanyDB);
                        editor.commit();
                        getDefaultWarehouse();// get Default Warehouse

                    }else{
                        Toast.makeText(LoginActivity.this, loginEntity.getStatus(), Toast.LENGTH_SHORT).show();
                        vibratorAdaptor.makeVibration();
                    }
                }
            }catch (Exception ex){
                ex.printStackTrace();
            }
        }
    }
    // Default Warehouse Web Service Call
    private void getDefaultWarehouse(){
        try {
            Integer userID = Integer.parseInt(sharedpreferences.getString(Constant.LOGIN_USERID, "0"));
            makeURL = service_url + "/GetDefaultWareHouse/" + selectedCompanyDB + "/" + userID + "/" + selectedComapnyName + "/" + passCode;
            new WareHouseDefault().execute(makeURL);
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    public class WareHouseDefault extends AsyncTask<String, String, DefaultWareHouse> {
        @Override
        protected DefaultWareHouse doInBackground(String... params) {
            DefaultWareHouseService defaultWareHouseService=new DefaultWareHouseService();
            return  defaultWareHouseService.getDefaultWareHouse(params[0]);

        }
        @Override
        protected void onPostExecute(DefaultWareHouse defaultWareHouse) {
            //System.out.println(defaultWareHouse.getWareHouseName());
            try {
                if(defaultWareHouse!=null && StringUtils.isEmpty(defaultWareHouse.getErrorMessage()) && defaultWareHouse.getWareHouseId()!="0"){
                    SharedPreferences.Editor editor = sharedpreferences.edit();
                    editor.putString(Constant.DEFAULT_WAREHOUSE, defaultWareHouse.getWareHouseName());
                    editor.putString(Constant.DEFAULT_WAREHOUSE_ID, defaultWareHouse.getWareHouseId());
                    editor.commit();
                    Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                    startActivity(intent);
                    finish();
                }else {
                    Toast.makeText(LoginActivity.this, defaultWareHouse.getErrorMessage(), Toast.LENGTH_SHORT).show();
                    vibratorAdaptor.makeVibration();
                }
            }catch (Exception ex){
                ex.printStackTrace();
            }
        }
    }
    //Check mobile version
    private void checkMobileVersion(){
        try{
            JSONObject jsonObject=new JSONObject();
            jsonObject.put("CompanyDbName",selectedCompanyDB);
            jsonObject.put("VersionNumber",Constant.MOBILE_VERSION_NUMBER);
            jsonObject.put("key",passCode);
            json=jsonObject.toString();
            makeURL=service_url+ "/CheckMobileVersion";
            new MobileVersion().execute(makeURL);

        }catch (Exception ex){
            ex.printStackTrace();
        }
    }
    //Check Mobile version service
    public class MobileVersion extends AsyncTask<String, String, Integer> {
        @Override
        protected Integer doInBackground(String... params) {
            CheckMobileVersionService checkMobileVersionService=new CheckMobileVersionService();
            return  checkMobileVersionService.getMobileVersion(params[0],json);

        }
        @Override
        protected void onPostExecute(Integer result) {
            //System.out.println(defaultWareHouse.getWareHouseName());
            try {
               // System.out.println("LG1");
                if(result==0){
                    Toast.makeText(LoginActivity.this, "Please Use Latest db version", Toast.LENGTH_SHORT).show();
                    vibratorAdaptor.makeVibration();
                }else {
                   // System.out.println("LG");
                    String login_user_name = userName.getText().toString();
                    String login_password = passWord.getText().toString();
                    final JSONObject root = new JSONObject();
                    root.put("UserName", login_user_name);
                    root.put("PassWord", login_password);
                    root.put("CompanyDbName", selectedCompanyDB);
                    root.put("Key", passCode);
                    json = root.toString();
                    gotoLogin(); // Login Function

                }
            }catch (Exception ex){
                ex.printStackTrace();
            }
        }
    }
    //Login Buttton submit and WebService Call
    private void gotoLogin(){
        makeURL = service_url + "/UserLogin";
        new LoginTask().execute(makeURL);
    }
}
