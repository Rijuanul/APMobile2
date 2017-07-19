package id.advancepro.com.advancepro;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import id.advancepro.com.advancepro.adapter.ResetForAllAdapter;
import id.advancepro.com.advancepro.adapter.VibratorAdaptor;
import id.advancepro.com.advancepro.model.ModuleCountsEntity;
import id.advancepro.com.advancepro.model.SalesCustListEntity;
import id.advancepro.com.advancepro.model.SalesDefaultSettingsEntity;
import id.advancepro.com.advancepro.model.SalesWarehouseEntity;
import id.advancepro.com.advancepro.model.WareHouseListEntity;
import id.advancepro.com.advancepro.service.InternetConnection;
import id.advancepro.com.advancepro.service.ModuleCountsService;
import id.advancepro.com.advancepro.service.SaleService.SalesServiceImpl.SalesServiceImpl;
import id.advancepro.com.advancepro.service.SetDefaultWareHouseService;
import id.advancepro.com.advancepro.service.WareHouseListService;
import id.advancepro.com.advancepro.utils.Constant;

public class HomeActivity extends AppCompatActivity {

    private InternetConnection intConn;
    private Toolbar toolbar;
    private SharedPreferences sharedpreferences;
    private String makeUrl;
    private String passCode;
    private String service_url;
    private String companyDBName;
    private String companyName;
    private Integer userID;
    private Integer wareHouseID;
    private Boolean checkInternet=false;
    private TextView outboundtextView;
    private TextView inboundtextView;
    private TextView wareHousetextView;
    private ListView wareHouseListView;
    private TextView selectedWarehouse;
    private Button confirmWarehouse;
    private View mWarehouseListView;
    List<String> warehouseList;
    List<WareHouseListEntity> warehouseEntityList;
    private String findWareHouseFromList;
    private Integer setdefaultWareHouseID;
    private Menu menuCustom;
    private ImageButton outboundBackImage;
    private ImageButton inboundBackImage;
    private ImageButton warehouseBackImage;
    private ImageButton lookupBackImage;
    private ImageButton salesImageBtn;
    private VibratorAdaptor vibratorAdaptor;
    private AlertDialog.Builder screenExitbuilder;
    private AlertDialog saleSettingsDialog;
    private View saleSettingsView;
    private Spinner salesDefaultCust;
    private Spinner salesDefaultWH;
    private CheckBox salesIsDirectInvoice;
    private Button saveSalesSettings;
    private Button backSaleToHome;
    private Integer salesDefCustomerID;
    private Integer salesDefWHID;
    private Boolean isSalesDefCustdrpdwnInitialize;
    private Boolean isSalesDefWHdrpdwnInitialize;
    private String jsonString;
    private Boolean salesSettingsClick;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        System.gc(); //Gerbage Collection Call

        intConn=new InternetConnection();//Internet connection class initialization
        //Textview Initialization
        outboundtextView=(TextView)findViewById(R.id.textViewOutbound);
        inboundtextView=(TextView)findViewById(R.id.textViewInbound);
        wareHousetextView=(TextView)findViewById(R.id.textViewWarehouse);
        //Get Sharedpreferences value
        sharedpreferences = getSharedPreferences(Constant.LOGIN_PREF, Context.MODE_PRIVATE);
        passCode=sharedpreferences.getString(Constant.PASS_CODE,null);
        service_url=sharedpreferences.getString(Constant.SERVICE_URL,null);
        companyDBName=sharedpreferences.getString(Constant.LOGIN_DB,null);
        companyName=sharedpreferences.getString(Constant.LOGIN_COMPANY_NAME,null);
        userID= Integer.parseInt(sharedpreferences.getString(Constant.LOGIN_USERID,"0"));
        wareHouseID= Integer.parseInt(sharedpreferences.getString(Constant.DEFAULT_WAREHOUSE_ID,"0"));
        //System.out.println(wareHouseID);
        //Image Button Intialization
        outboundBackImage=(ImageButton)findViewById(R.id.outboundimage);
        inboundBackImage=(ImageButton)findViewById(R.id.inboundimae);
        warehouseBackImage=(ImageButton)findViewById(R.id.warehouseimage);
        lookupBackImage=(ImageButton)findViewById(R.id.lookupimage);
        salesImageBtn=(ImageButton)findViewById(R.id.salesimage);

        //Vibrator
        vibratorAdaptor=new VibratorAdaptor(getApplicationContext());
        confirmExit();

        salesSettingsClick=false; //Initially Sales Setting click is false
        //Get Sales Default Settings
        getSalesDefaultSettings();
        // call to Module Counts Web Service
        getModuleCount();
        //Call to Reset Web Service
        resetForAllFunction();
        //Toolbar
        toolbar=(Toolbar)findViewById(R.id.menutoolbar);
        toolbar.setTitle("APMobile");
        setSupportActionBar(toolbar);
        //Outbound Button Click
        outboundBackImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.gc();
                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putString(Constant.OUTBOUND_SETTING_ID, "1");
                editor.commit();
                Intent intent= new Intent(getApplicationContext(),OutboundActivity.class);
                startActivity(intent);
                finish();
            }
        });

        //Inbound Button Click
        inboundBackImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            System.gc();
            SharedPreferences.Editor editor = sharedpreferences.edit();
            editor.putString(Constant.INBOUND_SETTING_ID, "2");
            editor.commit();
            Intent intent= new Intent(getApplicationContext(),InboundAllActivity.class);
            startActivity(intent);
            finish();
            //System.out.println("ss");
            }
        });
        //Warehuse Button Click
        warehouseBackImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            System.gc();
            Intent intent= new Intent(getApplicationContext(),WarehouseActivity.class);
            startActivity(intent);
            finish();
            }
        });
        //Lookup Button Click
        lookupBackImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.gc();
                Intent intent= new Intent(getApplicationContext(),LookupActivity.class);
                startActivity(intent);
            }
        });
        //Sales Image Btn Click
        salesImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.gc();
                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putString(Constant.SALES_DEFAULT_CUST_ID, salesDefCustomerID.toString());
                editor.commit();
                Intent intent= new Intent(getApplicationContext(),SalesActivity.class);
                startActivity(intent);
                //finish();
            }
        });
    }
    //Device Back Button Pressed
    @Override
    public void onBackPressed() {

        screenExitbuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //backToLoginPage();
                finish();
                dialog.dismiss();
            }
        });
        screenExitbuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        screenExitbuilder.show();

    }
    //Confirm Exit (Alert Dialogue Initialize )
    private void confirmExit(){
        screenExitbuilder = new AlertDialog.Builder(HomeActivity.this);
        screenExitbuilder.setMessage("Do you want to exit from this screen");
    }
    // Create option Menu
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater menuInflater=getMenuInflater();
        menuInflater.inflate(R.menu.settingsmenu,menu);
        return super.onCreateOptionsMenu(menu);
    }
    //Prepare the option menu
    public boolean onPrepareOptionsMenu(Menu menu) {

        menuCustom=menu;
        menu.findItem(R.id.menuitemdefaultwarehouse).setTitle(sharedpreferences.getString(Constant.DEFAULT_WAREHOUSE,""));
        menu.findItem(R.id.menuitemuser).setTitle(sharedpreferences.getString(Constant.LOGIN_USERNAME,""));
        return super.onPrepareOptionsMenu(menu);
    }

    //Home page option Item Selection
    public boolean onOptionsItemSelected(MenuItem menuItem){
        try {
            if (menuItem.getItemId() == R.id.signout) { // if signout click
                checkInternet=intConn.checkConnection(this); // Internet connection check
                if(checkInternet) {
                    try {
                        // Yes OR No Alert dialougue for Signout
                        AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this);
                        builder.setMessage(Constant.SIGNOUT_WARNING);
                        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                               /* resetForAllFunction();
                                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                                startActivity(intent);
                                finish();*/
                                backToLoginPage();
                                dialog.dismiss();
                            }
                        });
                        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                        builder.show();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }else {
                    internetConnectionMessage();//Internet Connection Error message
                }
            } else if (menuItem.getItemId() == R.id.menuitemdefaultwarehouse) {//Warehouse Selection Click
                mWarehouseListView=getLayoutInflater().inflate(R.layout.warehouse_select,null);
                wareHouseListView=(ListView)mWarehouseListView.findViewById(R.id.warehouselist);
                selectedWarehouse=(TextView)mWarehouseListView.findViewById(R.id.selectedwarehouse);
                confirmWarehouse=(Button)mWarehouseListView.findViewById(R.id.confirmwarehouse);
                //Warehouse List Webservice Call
                getWarehouseList();
                AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this);
                builder.setView(mWarehouseListView);
                final AlertDialog dialogWarehouseList = builder.create();
                dialogWarehouseList.show();
                findWareHouseFromList="";
                //Default Warehouse Selection from Alert
                wareHouseListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {//Warehouse List View Item Click
                        ///System.out.println(warehouseList.get(position));
                        //System.out.println("wh="+warehouseEntityList.get(position).getWarehouseName());
                        if(warehouseEntityList.get(position).getWarehouseName().equalsIgnoreCase(warehouseList.get(position))) {
                            findWareHouseFromList = warehouseEntityList.get(position).getWarehouseName();
                            selectedWarehouse.setText(warehouseEntityList.get(position).getWarehouseName());
                            setdefaultWareHouseID=warehouseEntityList.get(position).getWarhouseID();
                            SharedPreferences.Editor editor = sharedpreferences.edit();
                            editor.putString(Constant.DEFAULT_WAREHOUSE, warehouseEntityList.get(position).getWarehouseName());
                            editor.putString(Constant.DEFAULT_WAREHOUSE_ID, warehouseEntityList.get(position).getWarhouseID().toString());
                            editor.commit();
                        }
                    }
                });
                //Confirm Warehouse from Alert
                confirmWarehouse.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //System.out.println(selectedWarehouse.getText() +" === "+ findWareHouseFromList + "=== "+ selectedWarehouse.getText().toString().equals(findWareHouseFromList));
                        if(StringUtils.isEmpty(selectedWarehouse.getText()) || !(selectedWarehouse.getText().toString().equalsIgnoreCase(findWareHouseFromList))){
                            Toast.makeText(HomeActivity.this, Constant.WAREHOUSE_SELECTION_ERROR, Toast.LENGTH_SHORT).show();
                            vibratorAdaptor.makeVibration();
                        }else{
                            setDefaultWareHouse(); //Set default Warehouse
                            dialogWarehouseList.dismiss();
                        }
                    }
                });
            }else  if (menuItem.getItemId() == R.id.changesettings) {
                Intent intent= new Intent(getApplicationContext(),AllSettingsActivity.class);
                startActivity(intent);
            }
            else  if (menuItem.getItemId() == R.id.salesettings) {
                    salesSettingsClick=true; //After Clicking Sales Setting click it will be true
                    openSalesPopUp();
            }

        }catch (Exception ex){
            ex.printStackTrace();
        }
        return super.onOptionsItemSelected(menuItem);
    }
    //Module Count (Web Service Call)
    private void getModuleCount(){
        try {
            checkInternet=intConn.checkConnection(this); // Internet connection check
            if(checkInternet) {
                makeUrl = service_url + "/GetModuleCounts/" + companyDBName + "/"+ userID +"/" + wareHouseID + "/" + passCode;
                new ModuleCounts().execute(makeUrl);
            }else {
                internetConnectionMessage();
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }

    }
    public class ModuleCounts extends AsyncTask<String, String,ModuleCountsEntity> {
        @Override
        protected ModuleCountsEntity doInBackground(String... params) {
            ModuleCountsService moduleCountsService = new ModuleCountsService();
            return moduleCountsService.getModuleCounst(params[0]);
        }

        @Override
        protected void onPostExecute(ModuleCountsEntity moduleCountsEntities) {
            try {
                if(moduleCountsEntities!=null && StringUtils.isEmpty(moduleCountsEntities.getErrorMessage())){
                    SharedPreferences.Editor editor = sharedpreferences.edit();
                    editor.putString(Constant.OUTBOUND_COUNT_ID, moduleCountsEntities.getOutboundCountID().toString());
                    editor.putString(Constant.INBOUND_COUNT_ID, moduleCountsEntities.getInboundCountID().toString());
                    editor.putString(Constant.WAREHOUSE_COUNT_ID, moduleCountsEntities.getWarehouseCountID().toString());
                    editor.commit();
                    outboundtextView.setText("Outbound (" + moduleCountsEntities.getOutboundCount().toString() + ")"); // Outbound Button with Count
                    inboundtextView.setText("Inbound (" + moduleCountsEntities.getInboundCount().toString() + ")" );// Inbound Button with Count
                    wareHousetextView.setText("Warehouse (" + moduleCountsEntities.getWarehouseCount().toString() + ")");// Warehouse Button with Count
                    // Disable home page option based on AdvancePro Settings

                    if(moduleCountsEntities.getOutboundStatus()==0){ // outbound disable
                        outboundBackImage.setEnabled(false);
                        outboundBackImage.getDrawable().mutate().setAlpha(70);
                        outboundBackImage.invalidate();
                    }

                    if(moduleCountsEntities.getInboundStatus()==0){ // Inbound disable
                        inboundBackImage.setEnabled(false);
                        inboundBackImage.getDrawable().mutate().setAlpha(70);
                        inboundBackImage.invalidate();

                    }

                    if(moduleCountsEntities.getWarehouseStatus()==0){ // Warehouse Disable
                        warehouseBackImage.setEnabled(false);
                        warehouseBackImage.getDrawable().mutate().setAlpha(70);
                        warehouseBackImage.invalidate();

                    }

                    if(moduleCountsEntities.getLookupStatus()==0){ // Lookup disable
                        lookupBackImage.setEnabled(false);
                        lookupBackImage.getDrawable().mutate().setAlpha(70);
                        lookupBackImage.invalidate();
                    }

                }else{
                    Toast.makeText(HomeActivity.this, moduleCountsEntities.getErrorMessage(), Toast.LENGTH_SHORT).show();
                    vibratorAdaptor.makeVibration();
                    disableHomePageOption();//// Disable home page option based on AdvancePro Settings
                }
            }catch (Exception ex){
                ex.printStackTrace();
            }
        }
    }

    //Reset Function
    private void resetForAllFunction(){
        try{
            makeUrl=service_url + "/ResetForAll/" + companyDBName +"/"+ userID +"/"+ passCode;
            ResetForAllAdapter resetForAllAdapter=new ResetForAllAdapter(makeUrl,getApplicationContext());

        }catch (Exception ex){
            ex.printStackTrace();
        }
    }


    //Get Warehouse List (Web Service Call)
    private void getWarehouseList(){
        try{
            //System.out.println("ss");
            makeUrl=service_url + "/GetWareHouseList/" + companyDBName +"/"+ userID +"/"+ companyName +"/"+ passCode;
            new WareHouseList().execute(makeUrl);

        }catch (Exception ex){
            ex.printStackTrace();
        }

    }

    public class WareHouseList extends AsyncTask<String, String, List<WareHouseListEntity>> {
        @Override
        protected List<WareHouseListEntity> doInBackground(String... params) {
            WareHouseListService wareHouseListService = new WareHouseListService();
            return wareHouseListService.getWareHouseList(params[0]);
        }

        @Override
        protected void onPostExecute(List<WareHouseListEntity> wareHouseListEntities) {
            try {
                //System.out.println("Reset for All function "+ wareHouseListEntities);
                if(wareHouseListEntities!=null && wareHouseListEntities.size()>0){
                    warehouseEntityList=wareHouseListEntities;
                    warehouseList=new ArrayList<>();
                    for(WareHouseListEntity warehouse: wareHouseListEntities) {
                        warehouseList.add(warehouse.getWarehouseName());
                    }
                    ArrayAdapter<String> adapter=new ArrayAdapter<String>(getApplicationContext(),android.R.layout.select_dialog_item,warehouseList);
                    wareHouseListView.setAdapter(adapter);
                }else{
                    Toast.makeText(HomeActivity.this, Constant.SERVER_ERROR, Toast.LENGTH_SHORT).show();
                    vibratorAdaptor.makeVibration();
                }
            }catch (Exception ex){
                ex.printStackTrace();
            }
        }
    }
    //Set default Warehouse (Web Service Call)
    private void setDefaultWareHouse(){
        try{
            checkInternet=intConn.checkConnection(this); // Internet connection check
            if(checkInternet) {
                makeUrl = service_url + "/SetDefaultWareHouse/" + companyDBName + "/" + userID + "/" + setdefaultWareHouseID + "/" + passCode;
                new SetDefaultWareHouse().execute(makeUrl);
            }else{
                internetConnectionMessage();
            }
        }catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public class SetDefaultWareHouse extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... params) {
            SetDefaultWareHouseService resetForAllFunctionService = new SetDefaultWareHouseService();
            return resetForAllFunctionService.setWareHouse(params[0]);
        }

        @Override
        protected void onPostExecute(String statusCode) {
            //System.out.println("Code "+ statusCode);
            onPrepareOptionsMenu(menuCustom);
        }
    }
    //Internet Connnection Error Message
    private void internetConnectionMessage(){
        Toast.makeText(HomeActivity.this, getApplicationContext().getResources().getString(R.string.error001), Toast.LENGTH_SHORT).show();
        vibratorAdaptor.makeVibration();

    }
    // Disable home page option based on AdvancePro Settings
    private void disableHomePageOption(){

        outboundBackImage.setEnabled(false);
        outboundBackImage.getDrawable().mutate().setAlpha(70);
        outboundBackImage.invalidate();

        inboundBackImage.setEnabled(false);
        inboundBackImage.getDrawable().mutate().setAlpha(70);
        inboundBackImage.invalidate();

        warehouseBackImage.setEnabled(false);
        warehouseBackImage.getDrawable().mutate().setAlpha(70);
        warehouseBackImage.invalidate();

        lookupBackImage.setEnabled(false);
        lookupBackImage.getDrawable().mutate().setAlpha(70);
        lookupBackImage.invalidate();

    }

    //Back to Login Page
    private void backToLoginPage(){
        resetForAllFunction();
        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(intent);
        finish();

    }

    //Sales Settings Pop Up
    private void openSalesPopUp(){
        try {
            isSalesDefCustdrpdwnInitialize=false;
            isSalesDefWHdrpdwnInitialize=false;
            saleSettingsView = getLayoutInflater().inflate(R.layout.salesettingspopup, null);
            salesDefaultCust = (Spinner) saleSettingsView.findViewById(R.id.defaultcustdropdown);
            salesDefaultWH = (Spinner) saleSettingsView.findViewById(R.id.defaultwhdropdown);
            salesIsDirectInvoice = (CheckBox) saleSettingsView.findViewById(R.id.salesisdirectinvoice);
            saveSalesSettings = (Button) saleSettingsView.findViewById(R.id.savesalesettings);
            backSaleToHome = (Button) saleSettingsView.findViewById(R.id.backsalesettings);

            //Alert Dialougue
            AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this);
            builder.setView(saleSettingsView);
            saleSettingsDialog = builder.create();
            saleSettingsDialog.show();
            //Get Sales Default Settings
            getSalesDefaultSettings();
            //get Sales Warehouse Settings
            getSalesWarehouseSettings();
            //Back Button Click
            backSaleToHome.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    saleSettingsDialog.dismiss();
                }
            });
            //Sales Save Button Click Event
            saveSalesSettings.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    saveSalesSettings();
                }
            });
            //Sales Default Cust Drop Down
            salesDefaultCust.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if(!isSalesDefCustdrpdwnInitialize){
                        isSalesDefCustdrpdwnInitialize=true;
                        return;
                    }
                    SalesCustListEntity salesCustListEntity=(SalesCustListEntity)salesDefaultCust.getSelectedItem();
                    salesDefCustomerID=salesCustListEntity.getCustID();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
            //Sales Default WH Drop Down
            salesDefaultWH.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if(!isSalesDefWHdrpdwnInitialize){
                        isSalesDefWHdrpdwnInitialize=true;
                        return;
                    }
                    SalesWarehouseEntity salesWarehouseEntity=(SalesWarehouseEntity)salesDefaultWH.getSelectedItem();
                    salesDefWHID=salesWarehouseEntity.getWhID();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    //Get Sales Default Settings
    private void getSalesDefaultSettings(){
        try{
            checkInternet=intConn.checkConnection(this); // Internet connection check
            if(checkInternet) {
                makeUrl = service_url + "/GetSalesSettings/" + companyDBName + "/" + userID  + "/" + passCode;
                new GetSaleDefaultSettings().execute(makeUrl);
            }else{
                internetConnectionMessage();
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }
    //Get Sales Default Settings
    public class GetSaleDefaultSettings extends AsyncTask<String, String, SalesDefaultSettingsEntity> {
        @Override
        protected SalesDefaultSettingsEntity doInBackground(String... params) {
            SalesServiceImpl salesServiceImpl=new SalesServiceImpl();
            return salesServiceImpl.getSalesDefaultSettings(makeUrl);
        }

        @Override
        protected void onPostExecute(SalesDefaultSettingsEntity salesDefaultSettingsEntity) {
            if(salesDefaultSettingsEntity!=null){
                //Sales Default Cust ID
                salesDefCustomerID=salesDefaultSettingsEntity.getSalesDefaultCustID();
                if(salesSettingsClick) {
                    if (salesDefaultSettingsEntity.getSalesIsAllowCustEdit()) {
                        salesDefaultCust.setEnabled(true);
                        getSalesCustList();
                    } else {
                        salesDefaultCust.setEnabled(false);
                    }
                    //IS Direct Invoice
                    if (salesDefaultSettingsEntity.getSalesIsDirectInvoice()) {
                        salesIsDirectInvoice.setChecked(true);
                    } else {
                        salesIsDirectInvoice.setChecked(false);
                    }
                }
            }else{
                salesDefCustomerID=0;
            }
        }
    }
    //Sales Warehouse Settings
    private void getSalesWarehouseSettings(){
        try{
            checkInternet=intConn.checkConnection(this); // Internet connection check
            if(checkInternet) {
                makeUrl = service_url + "/GetSalesWHSettings/" + companyDBName + "/" + userID  + "/" + passCode;
                new GetSaleWHSettings().execute(makeUrl);
            }else{
                internetConnectionMessage();
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }
    //Get Sales Default Settings
    public class GetSaleWHSettings extends AsyncTask<String, String, List<SalesWarehouseEntity>> {
        @Override
        protected List<SalesWarehouseEntity> doInBackground(String... params) {
            SalesServiceImpl salesServiceImpl=new SalesServiceImpl();
            return salesServiceImpl.getSalesWH(makeUrl);
        }

        @Override
        protected void onPostExecute(List<SalesWarehouseEntity> salesWarehouseEntities) {
            if(salesWarehouseEntities!=null && salesWarehouseEntities.size()>0){
                ArrayAdapter<SalesWarehouseEntity> adapter = new ArrayAdapter<SalesWarehouseEntity>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item,salesWarehouseEntities);
                salesDefaultWH.setAdapter(adapter);
                for(int i=0;i<salesWarehouseEntities.size();i++){
                    if(salesWarehouseEntities.get(i).getDefault()){
                        salesDefWHID=salesWarehouseEntities.get(i).getWhID();
                        salesDefaultWH.setSelection(i);
                        break;
                    }
                }
            }
        }
    }
    //Get Sales Cust List
    private void getSalesCustList(){
        try{
            checkInternet=intConn.checkConnection(this); // Internet connection check
            if(checkInternet) {
                makeUrl = service_url + "/GetSalesCustList/" + companyDBName   + "/" + passCode;
                new GetSaleCustList().execute(makeUrl);
            }else{
                internetConnectionMessage();
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }
    //Sales Cust List
    public class GetSaleCustList extends AsyncTask<String, String, List<SalesCustListEntity>> {
        @Override
        protected List<SalesCustListEntity> doInBackground(String... params) {
            SalesServiceImpl salesServiceImpl=new SalesServiceImpl();
            return salesServiceImpl.getSalesCustList(makeUrl);
        }

        @Override
        protected void onPostExecute(List<SalesCustListEntity> salesCustListEntities) {
            if(salesCustListEntities!=null && salesCustListEntities.size()>0){
                ArrayAdapter<SalesCustListEntity> adapter = new ArrayAdapter<SalesCustListEntity>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item,salesCustListEntities);
                salesDefaultCust.setAdapter(adapter);
                if(salesDefCustomerID!=0) {
                    for (int i = 0; i < salesCustListEntities.size(); i++) {
                        if (salesCustListEntities.get(i).getCustID() == salesDefCustomerID) {
                            salesDefaultCust.setSelection(i);
                            break;
                        }
                    }
                }
            }
        }
    }
    //Save Sales Settings
    private void saveSalesSettings(){
        try{
            JSONObject jsonObject=new JSONObject();
            jsonObject.put("CompanyDBName",companyDBName);
            jsonObject.put("Key",passCode);
            jsonObject.put("UserId",userID);
            jsonObject.put("IsDirectInvoice",salesIsDirectInvoice.isChecked());
            jsonObject.put("CustId",salesDefCustomerID);
            jsonObject.put("WhId",salesDefWHID);
            jsonString=jsonObject.toString();
            checkInternet=intConn.checkConnection(this); // Internet connection check
            if(checkInternet) {
                makeUrl = service_url + "/SaveSalesSettings";
                //System.out.println(jsonString);
                new SaveSales().execute(makeUrl);
            }else{
                internetConnectionMessage();
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    //Save Sales
    public class SaveSales extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... params) {
            SalesServiceImpl salesServiceImpl=new SalesServiceImpl();
            return salesServiceImpl.saveSalesSettings(makeUrl,jsonString);
        }

        @Override
        protected void onPostExecute(String str) {
            if(str!=null ){
                Toast.makeText(HomeActivity.this, str, Toast.LENGTH_SHORT).show();
                vibratorAdaptor.makeVibration();
                saleSettingsDialog.dismiss();
            }
        }
    }
}
