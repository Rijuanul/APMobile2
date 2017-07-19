package id.advancepro.com.advancepro;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import id.advancepro.com.advancepro.adapter.LookupListAdapter;
import id.advancepro.com.advancepro.adapter.VibratorAdaptor;
import id.advancepro.com.advancepro.model.LookupDefaultLocationEntity;
import id.advancepro.com.advancepro.model.LookupResultEntity;
import id.advancepro.com.advancepro.model.SearchListEntity;
import id.advancepro.com.advancepro.service.LookupDefaultLocationService;
import id.advancepro.com.advancepro.service.LookupSearchResultService;
import id.advancepro.com.advancepro.utils.Constant;

public class LookupActivity extends AppCompatActivity {

    private EditText lookupSearchtxt;
    private Spinner lookupDroDown;
    private ListView lookupListView;
    private SharedPreferences sharedpreferences;
    private String makeUrl;
    private String passCode;
    private String service_url;
    private String companyDBName;
    private Integer userID;
    private Integer wareHouseID;
    private List<SearchListEntity> lookupDropdownValue;
    private Boolean isSelectkeypopulated;
    private String jsonString;
    private LookupListAdapter lookupListAdapter;
    private VibratorAdaptor vibratorAdaptor;
    private SearchListEntity searchListEntity;
    private Button lookUpBackBtn;
    private final int SPEECH_RECOGNITION_CODE = 1;
    private ImageButton btnMicrophone;
    private String speechText;
    private LinearLayout lookupProgressbarLayout;
    private TextView searchTextLabel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_lookup);
            //SharedPreferences Value
            sharedpreferences = getSharedPreferences(Constant.LOGIN_PREF, Context.MODE_PRIVATE);
            passCode=sharedpreferences.getString(Constant.PASS_CODE,null);
            service_url=sharedpreferences.getString(Constant.SERVICE_URL,null);
            companyDBName=sharedpreferences.getString(Constant.LOGIN_DB,null);
            userID= Integer.parseInt(sharedpreferences.getString(Constant.LOGIN_USERID,"0"));
            wareHouseID= Integer.parseInt(sharedpreferences.getString(Constant.DEFAULT_WAREHOUSE_ID,"0"));
            isSelectkeypopulated=false;//Restriciton For Selectkey dropdown first time when page loading
            //Vibrator
            vibratorAdaptor=new VibratorAdaptor(getApplicationContext()); // Vibrator class initialization
            //
            lookupSearchtxt = (EditText) findViewById(R.id.lookupsearchtxt);// Search text initialize
            lookupDroDown = (Spinner) findViewById(R.id.lookupdrpdown); // Select key DropDown initialize
            lookupListView = (ListView) findViewById(R.id.lookuplistview); // ListView Initialize
            lookupProgressbarLayout= (LinearLayout) findViewById(R.id.lookuploadinglayout); // ProgressBar Initialize
            lookupProgressbarLayout.setVisibility(View.GONE);//Initially make progressbar disable
            searchTextLabel= (TextView)findViewById(R.id.lookupsearchtextlabel);

            lookUpBackBtn=(Button)findViewById(R.id.lookupbackbtn); // Lookup back button initialize

            populateLookupDroDown();
            //Look up Search  Event
            lookupSearchtxt.setOnKeyListener(new View.OnKeyListener() {
                @Override
                public boolean onKey(View v, int keyCode, KeyEvent event) {
                    if(keyCode==66 && event.getAction()==KeyEvent.ACTION_UP){
                        lookupListView.setAdapter(null);
                        getLookupSearchResult();
                    }
                    return false;
                }
            });
            //Lookup Drowp Down change
            lookupDroDown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if(!isSelectkeypopulated){
                        isSelectkeypopulated=true;
                        return;
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
            //Look Up Back Button Event
            lookUpBackBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    goToHomePage();

                }
            });
            //Speech Button
            btnMicrophone = (ImageButton)findViewById(R.id.btnmic);
            btnMicrophone.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startSpeechToText();
                }
            });
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    //Populate Lookup Select DropDown
    private void populateLookupDroDown(){
        try{
            //Create String with Default Array
            String[][] SearchItemarray = new String[][]{
                    {"sku","SKU"},
                    {"upc","UPC"},
                    {"serialnumber","Serial #"},
                    {"PickLoc","Pick Loc"},
                    {"Lot","Lot"},
            };
            lookupDropdownValue=new ArrayList<>(); // ArrayList for Lookup Selectkey DropDown
            for(int i=0;i<SearchItemarray.length;i++){
                searchListEntity=new SearchListEntity(); // Lookup Selectkey Item Entity
                searchListEntity.setValue(SearchItemarray[i][0]);
                searchListEntity.setName(SearchItemarray[i][1]);
                lookupDropdownValue.add(searchListEntity);
            }

            ArrayAdapter<SearchListEntity> lookupdrpdownAdapter=new ArrayAdapter<SearchListEntity>(getApplicationContext() ,android.R.layout.simple_spinner_dropdown_item, lookupDropdownValue);
            lookupDroDown.setAdapter(lookupdrpdownAdapter); // Lookup Selectkey dropdown populated with value
            getDefaultLookupSelection(); // Get Default Lookup Selectkey
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }
    //Get Default Lookup Dropdown
    private void getDefaultLookupSelection(){
        try{
            makeUrl= service_url + "/GetDefaultcriteriasetting/" + companyDBName + "/" + userID + "/" + wareHouseID + "/" + passCode;
            new LookupDefaultLocation().execute(makeUrl);
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    // Get Lookup Default Location
    public class LookupDefaultLocation extends AsyncTask<String, String, LookupDefaultLocationEntity> {
        @Override
        protected LookupDefaultLocationEntity doInBackground(String... params) {
            LookupDefaultLocationService lookupDefaultLocationService=new LookupDefaultLocationService();
            return  lookupDefaultLocationService.getLookupDefaultLocation(params[0]);
        }
        @Override
        protected void onPostExecute(LookupDefaultLocationEntity lookupDefaultLocationEntity) {
            try {
                if(lookupDefaultLocationEntity!=null ){
                    if(!StringUtils.isEmpty(lookupDefaultLocationEntity.getSelectedname())) {
                        for (int i = 0; i < lookupDropdownValue.size(); i++) {
                            if (lookupDropdownValue.get(i).equals(lookupDefaultLocationEntity.getSelectedname())) {
                                lookupDroDown.setSelection(i);
                                break;
                            }
                        }
                    }
                }else{
                    Toast.makeText(LookupActivity.this, Constant.SERVER_ERROR, Toast.LENGTH_SHORT).show();
                    vibratorAdaptor.makeVibration();
                }
            }catch (Exception ex){
                ex.printStackTrace();
            }
        }
    }

    //Get Lookup Search Result
    private void getLookupSearchResult(){
        try{
            lookupProgressbarLayout.setVisibility(View.VISIBLE);
            searchTextLabel.setText("Search For : "+ lookupSearchtxt.getText().toString());
            SearchListEntity selectKey=(SearchListEntity) lookupDroDown.getSelectedItem();
            JSONObject jsonObject=new JSONObject();
            jsonObject.put("CompanyDbName",companyDBName);
            jsonObject.put("Whid",wareHouseID);
            jsonObject.put("Searchkey",lookupSearchtxt.getText().toString());
            jsonObject.put("selectkey",selectKey.getValue());
            jsonObject.put("Key",passCode);
            jsonString=jsonObject.toString();
            makeUrl=service_url + "/SearchLookup";
            new LookupSearchResult().execute(makeUrl);

        }catch (Exception ex){
            ex.printStackTrace();
        }
    }
    //Get Lookup Search Result
    public class LookupSearchResult extends AsyncTask<String, String, List<LookupResultEntity>> {
        @Override
        protected List<LookupResultEntity> doInBackground(String... params) {
            LookupSearchResultService lookupSearchResultService=new LookupSearchResultService();
            return  lookupSearchResultService.getLookupResult(params[0],jsonString);
        }
        @Override
        protected void onPostExecute(List<LookupResultEntity> lookupResultEntities) {
            try {
                if(lookupResultEntities!=null && lookupResultEntities.size()>0){
                    setLookupResult(lookupResultEntities);
                }else{
                    lookupListView.setAdapter(null);
                    Toast.makeText(LookupActivity.this, Constant.EMPTY_DATA, Toast.LENGTH_SHORT).show();
                    vibratorAdaptor.makeVibration();
                }
                lookupSearchtxt.setText("");
                lookupSearchtxt.requestFocus();
                lookupProgressbarLayout.setVisibility(View.GONE);
            }catch (Exception ex){
                ex.printStackTrace();
            }
        }
    }

    //Populate Lookup ListView
    private void setLookupResult(List<LookupResultEntity> lookupResultEntities){
        try {
            if(lookupResultEntities!=null && lookupResultEntities.size()>0) {
                lookupListAdapter = new LookupListAdapter(getApplicationContext(), R.layout.lookuplistlayout, lookupResultEntities);
                lookupListView.setAdapter(lookupListAdapter);
            }else{
                lookupListView.setAdapter(null);
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    //Go to Home Page
    private void goToHomePage(){
        System.gc();
        Intent backToHome = new Intent(this, HomeActivity.class);
        //startActivityForResult(backToHome,1);
        startActivity(backToHome);//Start the Home Activity
        finish();
    }

    //Speech Code
    /**
     * Start speech to text intent. This opens up Google Speech Recognition API dialog box to listen the speech input.
     * */
    private void startSpeechToText() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
                "Speak something...");
        try {
            startActivityForResult(intent, SPEECH_RECOGNITION_CODE);
        } catch (Exception a) {
            Toast.makeText(getApplicationContext(),
                    "Sorry! Speech recognition is not supported in this device.",
                    Toast.LENGTH_SHORT).show();
        }
    }
    /**
     * Callback for speech recognition activity
     * */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case SPEECH_RECOGNITION_CODE: {
                if (resultCode == RESULT_OK && null != data) {
                    ArrayList<String> result = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    String regex = "[+_-]";
                    speechText=result.get(0).toString().replace(" ","");
                    speechText=speechText.replaceAll(regex,"");
                    lookupSearchtxt.setText(speechText);
                    getLookupSearchResult();
                }
                break;
            }
        }
    }

}
