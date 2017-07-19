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
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import id.advancepro.com.advancepro.adapter.VibratorAdaptor;
import id.advancepro.com.advancepro.model.BoundSettingsEntity;
import id.advancepro.com.advancepro.model.LookupCycleCountsDefaultSettingEntity;
import id.advancepro.com.advancepro.model.OutboundInboundSortingOrderEntity;
import id.advancepro.com.advancepro.model.SearchListEntity;
import id.advancepro.com.advancepro.service.BoundSettingsService;
import id.advancepro.com.advancepro.service.InboundOutboundSortingOrderService;
import id.advancepro.com.advancepro.service.InternetConnection;
import id.advancepro.com.advancepro.service.LookupCycleCountSaveService;
import id.advancepro.com.advancepro.service.LookupCycleCountsDefaultSettingService;
import id.advancepro.com.advancepro.service.SaveOutboundInboundSettingsService;
import id.advancepro.com.advancepro.utils.Constant;

public class AllSettingsActivity extends AppCompatActivity {
    private String makeUrl;
    private String passCode;
    private String service_url;
    private String companyDBName;
    private Integer userID;
    private Integer warehouseID;
    private SharedPreferences sharedpreferences;
    private Integer settingsID;
    private InternetConnection intConn;
    private Boolean checkInternet=false;
    private List<OutboundInboundSortingOrderEntity> boundSortingOrderList;
    private List<OutboundInboundSortingOrderEntity> boundSortingOrderListOrderDate;
    private List<OutboundInboundSortingOrderEntity> boundSortingOrderListExpectedDate;
    private TextView settingHeadingName;
    private CheckBox customerName;
    private CheckBox productName;
    private CheckBox orderQuantity;
    private RadioButton orderDate;
    private RadioButton expectedDate;
    private Spinner sortingOrder;
    private RadioButton ascOrder;
    private RadioButton descOrder;
    private Button saveSetting;
    private Button backHomeFromSetting;
    private Button outboundSettingbtn;
    private Button inboundSettingbtn;
    private Button lookupAndCycleCountbtn;
    private ScrollView outboundInboundLayout;
    private LinearLayout lookupCycleCountLayout;
    private Integer customerNameTxt;
    private Integer productNameTxt;
    private Integer orderQuantityTxt;
    private Integer orderDateTxt;
    private Integer expectedDateTxt;
    private String sortingOrderVal;
    private String sortingOrderTypeTxt;
    private InboundOutboundSortingOrderService inboundOutboundSortingOrderService;
    private Boolean isLookupAndCycleCount;
    private VibratorAdaptor vibratorAdaptor;
    private Spinner lookupSettingDropdown;
    private List<SearchListEntity> lookupDropdownListItem;
    private  SearchListEntity searchListEntity;
    private String jsonString;
    private AlertDialog.Builder screenExitbuilder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_all_settings);

            intConn = new InternetConnection();// Internet Connection Initialize
            confirmExit(); // Confirm Exit with Alert dialogue box initialize
            //Sharepreferences Value
            sharedpreferences = getSharedPreferences(Constant.LOGIN_PREF, Context.MODE_PRIVATE);
            passCode = sharedpreferences.getString(Constant.PASS_CODE, null);
            service_url = sharedpreferences.getString(Constant.SERVICE_URL, null);
            companyDBName = sharedpreferences.getString(Constant.LOGIN_DB, null);
            userID = Integer.parseInt(sharedpreferences.getString(Constant.LOGIN_USERID, "0"));
            warehouseID = Integer.parseInt(sharedpreferences.getString(Constant.DEFAULT_WAREHOUSE_ID, "0"));
            // All Inbound Settings Screen Field Initialize
            settingHeadingName=(TextView)findViewById(R.id.settingheading) ;
            customerName = (CheckBox) findViewById(R.id.settingcustomername);
            productName = (CheckBox) findViewById(R.id.settingproductname);
            orderQuantity = (CheckBox) findViewById(R.id.settingorderqty);
            orderDate = (RadioButton) findViewById(R.id.settingorderdate);
            expectedDate = (RadioButton) findViewById(R.id.settingexpecteddate);
            sortingOrder = (Spinner) findViewById(R.id.settingdrpdown);
            ascOrder = (RadioButton) findViewById(R.id.settingasc);
            descOrder = (RadioButton) findViewById(R.id.settingdesc);
            saveSetting = (Button) findViewById(R.id.settingsave);
            backHomeFromSetting = (Button) findViewById(R.id.backsettingtohome);

            outboundSettingbtn=(Button)findViewById(R.id.outboundsettingsbtn);//Outbound Settings Button
            inboundSettingbtn=(Button)findViewById(R.id.inboundsettingsbtn);//Inbound Settings Button
            lookupAndCycleCountbtn=(Button)findViewById(R.id.lookupcyclecountsettingsbtn); // LookUp and Cycle Counts Settings button

            outboundInboundLayout=(ScrollView)findViewById(R.id.settingscroolviewlayout);
            lookupCycleCountLayout=(LinearLayout)findViewById(R.id.lookupcyclecountlayout);

            lookupSettingDropdown=(Spinner)findViewById(R.id.lookupsettingdrpdown);
            //Set Default Value
            setDefaultValue();
            //Get outbound sorting order
            getOutboundSortingOrder();
            //Get Outbound Default Settings
            getSetting();
            //Outbound Settings Button Click
            outboundSettingbtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    screenExitbuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            settingHeadingName.setText("outbound display options");
                            outboundSettingbtn.setBackgroundDrawable(getResources().getDrawable(R.drawable.button_border));
                            inboundSettingbtn.setBackgroundDrawable(getResources().getDrawable(R.drawable.button_border_white_background));
                            lookupAndCycleCountbtn.setBackgroundDrawable(getResources().getDrawable(R.drawable.button_border_white_background));
                            customerName.setText("Customer Name");
                            outboundInboundLayout.setVisibility(View.VISIBLE);
                            lookupCycleCountLayout.setVisibility(View.GONE);
                            settingsID = 1;
                            isLookupAndCycleCount=false;
                            //Get Outbound Sorting Order
                            getInboundSortingOrder();
                            //Get Setting for Outbound
                            getSetting();
                            System.gc();
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
            });
            //Inbound Settings Button Click
            inboundSettingbtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    screenExitbuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            settingHeadingName.setText("inbound display options");
                            outboundSettingbtn.setBackgroundDrawable(getResources().getDrawable(R.drawable.button_border_white_background));
                            inboundSettingbtn.setBackgroundDrawable(getResources().getDrawable(R.drawable.button_border));
                            lookupAndCycleCountbtn.setBackgroundDrawable(getResources().getDrawable(R.drawable.button_border_white_background));
                            customerName.setText("Vendor Name");
                            outboundInboundLayout.setVisibility(View.VISIBLE);
                            lookupCycleCountLayout.setVisibility(View.GONE);
                            settingsID = 2;
                            isLookupAndCycleCount=false;
                            //Get Inbound Sorting Order
                            getInboundSortingOrder();
                            //Get Setting for Inbound
                            getSetting();
                            System.gc();
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
            });
            //Lookup and Cycle Counts Settings Button Click
            lookupAndCycleCountbtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    screenExitbuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            settingHeadingName.setText("");
                            outboundSettingbtn.setBackgroundDrawable(getResources().getDrawable(R.drawable.button_border_white_background));
                            inboundSettingbtn.setBackgroundDrawable(getResources().getDrawable(R.drawable.button_border_white_background));
                            lookupAndCycleCountbtn.setBackgroundDrawable(getResources().getDrawable(R.drawable.button_border));
                            outboundInboundLayout.setVisibility(View.GONE);
                            lookupCycleCountLayout.setVisibility(View.VISIBLE);
                            isLookupAndCycleCount=true;
                            lookupAndCycleCountsDefaultSettings();
                            System.gc();
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
            });
            //Customer name Field Check/Uncheck Listener
            customerName.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if(isChecked){
                        customerNameTxt=1;
                    }else{
                        customerNameTxt=0;
                        customerName.setText("Vendor Name");
                    }
                }
            });
            //Product name Check/Uncheck Listener
            productName.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if(isChecked){
                        productNameTxt=1;
                    }else{
                        productNameTxt=0;
                    }
                }
            });
            //Order quantity Check/uncheck listener
            orderQuantity.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if(isChecked){
                        orderQuantityTxt=1;
                    }else{
                        orderQuantityTxt=0;
                    }
                }
            });
            //Order date click Listener
            orderDate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    orderDate.setChecked(true);
                    expectedDate.setChecked(false);
                    orderDateTxt=1;
                    expectedDateTxt=0;
                    populateSortingOrderList(boundSortingOrderListOrderDate);
                }
            });
            ////Expected date click Listener
            expectedDate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    expectedDate.setChecked(true);
                    orderDate.setChecked(false);
                    expectedDateTxt=1;
                    orderDateTxt=0;
                    populateSortingOrderList(boundSortingOrderListExpectedDate);
                }
            });
            //Sorting order dropdown item select listener
            sortingOrder.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    OutboundInboundSortingOrderEntity outboundInboundSortingOrderEntity=(OutboundInboundSortingOrderEntity)sortingOrder.getSelectedItem();
                    sortingOrderVal=outboundInboundSortingOrderEntity.getSortingOrderValue();
                }
                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
            //Asynchronous Order click Listener
            ascOrder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ascOrder.setChecked(true);
                    descOrder.setChecked(false);
                    sortingOrderTypeTxt="ASC";
                }
            });
            //Descending order Click Listener
            descOrder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ascOrder.setChecked(false);
                    descOrder.setChecked(true);
                    sortingOrderTypeTxt="desc";
                }
            });
            //Save Settings Click
            saveSetting.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(isLookupAndCycleCount){
                        saveLookupAndCycleCountSettings();
                    }else{
                        saveOutboundInboundSettings();
                    }
                }
            });
            //Custom back button click
            backHomeFromSetting.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    screenExitbuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            gotoHomePage();
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
            });

        }catch (Exception ex){
            ex.printStackTrace();
        }
    }
    //Device Back Key pressed
    @Override
    public void onBackPressed() {
        screenExitbuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                gotoHomePage();
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
    //Get Bound Settings (Web Service Call)
    private void getSetting(){
        try{
            makeUrl = service_url + "/GetBoundSettings/" + companyDBName + "/" + userID + "/" + settingsID + "/" + passCode;
            new BoundSetting().execute(makeUrl);
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    public class BoundSetting extends AsyncTask<String, String, BoundSettingsEntity> {
        @Override
        protected BoundSettingsEntity doInBackground (String...params){
            BoundSettingsService boundSettingsService = new BoundSettingsService();
            return boundSettingsService.getInboundSettingsList(params[0]);
        }

        @Override
        protected void onPostExecute (BoundSettingsEntity boundSettingsEntityRef){
            try {
                if(boundSettingsEntityRef!=null){
                    //Customer Or Vendor
                    if(Integer.parseInt(boundSettingsEntityRef.getIsShowName())==1){
                        customerName.setChecked(true);
                        customerNameTxt=1;
                    }else{
                        customerName.setChecked(false);
                        customerNameTxt=0;
                    }
                    //Product
                    if(Integer.parseInt(boundSettingsEntityRef.getIsShowProdName())==1){
                        productName.setChecked(true);
                        productNameTxt=1;
                    }else{
                        productName.setChecked(false);
                        productNameTxt=0;
                    }
                    //Order Quantity
                    if(Integer.parseInt(boundSettingsEntityRef.getIsShowOrderQty())==1){
                        orderQuantity.setChecked(true);
                        orderQuantityTxt=1;
                    }else{
                        orderQuantity.setChecked(false);
                        orderQuantityTxt=0;
                    }
                    //Order Date
                    if(Integer.parseInt(boundSettingsEntityRef.getIsShowOrderDate())==1){
                        orderDate.setChecked(true);
                        orderDateTxt=1;
                        expectedDate.setChecked(false);
                    }else{
                        orderDate.setChecked(false);
                        orderDateTxt=0;
                    }
                    //Expected Date
                    if(Integer.parseInt(boundSettingsEntityRef.getIsShowExpectedDate())==1){
                        expectedDate.setChecked(true);
                        expectedDateTxt=1;
                        orderDate.setChecked(false);
                    }else{
                        expectedDate.setChecked(false);
                        expectedDateTxt=0;
                    }
                    //Order Type
                    //Desc
                    if(boundSettingsEntityRef.getSortOrderType().equals("desc")){
                        descOrder.setChecked(true);
                        sortingOrderTypeTxt="desc";
                        ascOrder.setChecked(false);
                    }
                    //Asc
                    if(boundSettingsEntityRef.getSortOrderType().equals("ASC")){
                        ascOrder.setChecked(true);
                        sortingOrderTypeTxt="ASC";
                        descOrder.setChecked(false);
                    }
                    //Sorting Order
                    if(!StringUtils.isEmpty(boundSettingsEntityRef.getSortOrderColumn())){
                        if((boundSettingsEntityRef.getSortOrderColumn().equals("orderdate") && orderDate.isChecked()) || orderDate.isChecked()){
                            for(int i=0;i<boundSortingOrderListOrderDate.size();i++){
                                if(boundSortingOrderListOrderDate.get(i).getSortingOrderValue().equals(boundSettingsEntityRef.getSortOrderColumn())){
                                    populateSortingOrderList(boundSortingOrderListOrderDate);
                                    sortingOrder.setSelection(i);
                                    sortingOrderVal=boundSortingOrderList.get(i).getSortingOrderValue();
                                    break;
                                }
                            }
                        }else if((boundSettingsEntityRef.getSortOrderColumn().equals("expecteddate") && expectedDate.isChecked()) ||  expectedDate.isChecked()){
                            for(int i=0;i<boundSortingOrderListExpectedDate.size();i++){
                                if(boundSortingOrderListExpectedDate.get(i).getSortingOrderValue().equals(boundSettingsEntityRef.getSortOrderColumn())){
                                    populateSortingOrderList(boundSortingOrderListExpectedDate);
                                    sortingOrder.setSelection(i);
                                    sortingOrderVal=boundSortingOrderList.get(i).getSortingOrderValue();
                                    break;
                                }
                            }
                        }else{
                            for(int i=0;i<boundSortingOrderList.size();i++){
                                if(boundSortingOrderList.get(i).getSortingOrderValue().equals(boundSettingsEntityRef.getSortOrderColumn())){
                                    populateSortingOrderList(boundSortingOrderList);
                                    sortingOrder.setSelection(i);
                                    sortingOrderVal=boundSortingOrderList.get(i).getSortingOrderValue();
                                    break;
                                }
                            }
                        }

                    }
                }

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
    //Get Outbound Sorting Order
    private void getOutboundSortingOrder(){
        try {

            boundSortingOrderList = inboundOutboundSortingOrderService.getOutboundSortingOrder();
            setSortingOrderOrderDate();
            setSortingOrderExpectedDate();
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }
    //Get Inbound Sorting Order
    private void getInboundSortingOrder(){
        try {

            boundSortingOrderList = inboundOutboundSortingOrderService.getInboundSortingOrder();
            setSortingOrderOrderDate();
            setSortingOrderExpectedDate();

        }catch (Exception ex){
            ex.printStackTrace();
        }
    }
    //Sorting Order List  except Expected Date
    private void setSortingOrderOrderDate(){
        boundSortingOrderListOrderDate=new ArrayList<>();
        for(OutboundInboundSortingOrderEntity outboundInboundSortingOrderEntity: boundSortingOrderList){
            if(!outboundInboundSortingOrderEntity.getSortingOrderValue().equals("expecteddate")){
                boundSortingOrderListOrderDate.add(outboundInboundSortingOrderEntity);
            }
        }
    }
    //Sorting Order List  except Order Date
    private void setSortingOrderExpectedDate(){
        boundSortingOrderListExpectedDate=new ArrayList<>();
        for(OutboundInboundSortingOrderEntity outboundInboundSortingOrderEntity: boundSortingOrderList){
            if(!outboundInboundSortingOrderEntity.getSortingOrderValue().equals("orderdate")){
                boundSortingOrderListExpectedDate.add(outboundInboundSortingOrderEntity);
            }
        }
    }
    //Populate Sorting Order List
    private void populateSortingOrderList(List<OutboundInboundSortingOrderEntity> outboundInboundSortingOrderEntityList){
        ArrayAdapter<OutboundInboundSortingOrderEntity> adapter = new ArrayAdapter<OutboundInboundSortingOrderEntity>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, outboundInboundSortingOrderEntityList);
        sortingOrder.setAdapter(adapter);
    }
    //Set Initial Value
    private void setDefaultValue(){
        customerNameTxt=0;
        productNameTxt=0;
        orderQuantityTxt=0;
        orderDateTxt=0;
        expectedDateTxt=1;
        sortingOrderTypeTxt="ASC";

        outboundSettingbtn.setBackgroundDrawable(getResources().getDrawable(R.drawable.button_border));
        inboundSettingbtn.setBackgroundDrawable(getResources().getDrawable(R.drawable.button_border_white_background));
        lookupAndCycleCountbtn.setBackgroundDrawable(getResources().getDrawable(R.drawable.button_border_white_background));

        outboundInboundLayout.setVisibility(View.VISIBLE);
        lookupCycleCountLayout.setVisibility(View.GONE);

        settingsID = 1;
        inboundOutboundSortingOrderService=new InboundOutboundSortingOrderService();
        settingHeadingName.setText("outbound display options");

        isLookupAndCycleCount=false;

        vibratorAdaptor=new VibratorAdaptor(getApplicationContext());
    }


    //Save Outbound And Inbound Settings (Web Service Call)
    private void saveOutboundInboundSettings(){
        try {
            makeUrl = service_url + "/PostBoundSettings/" + companyDBName + "/" + userID + "/" + customerNameTxt + "/" + productNameTxt + "/" + orderQuantityTxt + "/" + orderDateTxt + "/" + expectedDateTxt + "/" + sortingOrderVal + "/" + sortingOrderTypeTxt + "/" + settingsID + "/" + passCode;
            new CompleteOutboundInboundSettings().execute(makeUrl);
        }catch (Exception ex){
            ex.printStackTrace();
        }
        //System.out.println(makeUrl);
    }

    public class CompleteOutboundInboundSettings extends AsyncTask<String, String, Integer> {
        @Override
        protected Integer doInBackground (String...params){
            SaveOutboundInboundSettingsService saveOutboundInboundSettingsService=new SaveOutboundInboundSettingsService();
            return saveOutboundInboundSettingsService.outboundInboundSettingsSave(params[0]);
        }

        @Override
        protected void onPostExecute (Integer status){
            try {
                if(status==1){
                    Toast.makeText(getApplicationContext(), "Settings save successfully", Toast.LENGTH_SHORT).show();
                    vibratorAdaptor.completeSound();
                }else{
                    Toast.makeText(getApplicationContext(), Constant.SERVER_ERROR, Toast.LENGTH_SHORT).show();
                    vibratorAdaptor.makeVibration();
                }
                gotoHomePage();
            }catch (Exception ex){
                ex.printStackTrace();
            }
        }
    }
    //Get Lookup and CycleCount Default Settings
    private void lookupAndCycleCountsDefaultSettings(){
        try{
            makeUrl=service_url + "/GetDefaultcriteriasetting/" + companyDBName + "/" + userID + "/" + warehouseID + "/" + passCode;
            new LookupCycleCountsStoredSettings().execute(makeUrl);
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }
    public class LookupCycleCountsStoredSettings extends AsyncTask<String, String, List<LookupCycleCountsDefaultSettingEntity>> {
        @Override
        protected List<LookupCycleCountsDefaultSettingEntity> doInBackground (String...params){
            LookupCycleCountsDefaultSettingService lookupCycleCountsDefaultSettingService=new LookupCycleCountsDefaultSettingService();
            return lookupCycleCountsDefaultSettingService.outboundInboundSettingsSave(params[0]);
        }

        @Override
        protected void onPostExecute (List<LookupCycleCountsDefaultSettingEntity> lookupCycleCountsDefaultSettingEntityList){
            try {
                if(lookupCycleCountsDefaultSettingEntityList!=null && lookupCycleCountsDefaultSettingEntityList.size()>0){
                    setLookupDropDown();
                    populateLookupList(lookupCycleCountsDefaultSettingEntityList);
                }

            }catch (Exception ex){
                ex.printStackTrace();
            }
        }
    }
    //Set Lookup DropDown
    private void setLookupDropDown(){
        String[][] SearchItemarray = new String[][]{
                {"sku","SKU"},
                {"upc","UPC"},
                {"serialnumber","Serial #"},
                {"PickLoc","Pick Loc"},
                {"Lot","Lot"},
        };
        lookupDropdownListItem=new ArrayList<>();
        for(int i=0;i<SearchItemarray.length;i++){
            searchListEntity=new SearchListEntity();
            searchListEntity.setValue(SearchItemarray[i][0]);
            searchListEntity.setName(SearchItemarray[i][1]);
            lookupDropdownListItem.add(searchListEntity);
        }

    }
    // Populate Look Search DropDown
    private void populateLookupList(List<LookupCycleCountsDefaultSettingEntity> lookupCycleCountsDefaultSettingEntityList){
        ArrayAdapter<SearchListEntity> adapter = new ArrayAdapter<SearchListEntity>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, lookupDropdownListItem);
        lookupSettingDropdown.setAdapter(adapter);
        for(int i=0;i<lookupDropdownListItem.size();i++){
            if(lookupDropdownListItem.get(i).getName().equals(lookupCycleCountsDefaultSettingEntityList.get(0).getSelectedname())){
                lookupSettingDropdown.setSelection(i);
                break;
            }
        }
    }
    //Save Lookup and CycleCount Settings (Web Service Call)
    private void saveLookupAndCycleCountSettings(){
        try{
            JSONArray array=new JSONArray();
            //Look up Json object
            JSONObject jsonObjectLookUp=new JSONObject();
            jsonObjectLookUp.put("Defaultcriterianame","lookup");
            jsonObjectLookUp.put("Defaultcriteriacolumnname",lookupSettingDropdown.getSelectedItem());
            array.put(jsonObjectLookUp);
            //CycleCount Json object
            JSONObject jsonObjectCycleCount=new JSONObject();
            jsonObjectCycleCount.put("Defaultcriterianame","cyclecount");
            jsonObjectCycleCount.put("Defaultcriteriacolumnname","Select");
            array.put(jsonObjectCycleCount);
            //Json object Submit
            JSONObject jsonObject=new JSONObject();
            jsonObject.put("CompanyDbName",companyDBName);
            jsonObject.put("Userid",userID);
            jsonObject.put("WhID",warehouseID);
            jsonObject.put("setdefaultCriteria",array);
            jsonObject.put("Key",passCode);
            jsonString=jsonObject.toString();
            makeUrl=service_url+ "/setdefaultCriteriasetting";
            new LookupCycleCountsSettingsSave().execute(makeUrl);

        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    public class LookupCycleCountsSettingsSave extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground (String...params){
            LookupCycleCountSaveService lookupCycleCountSaveService=new LookupCycleCountSaveService();
            return lookupCycleCountSaveService.lookupCycleCountSave(params[0],jsonString);
        }

        @Override
        protected void onPostExecute (String str){
            try {
               if(str.equals("saved successfully")){
                   Toast.makeText(getApplicationContext(), "Settings save successfully", Toast.LENGTH_SHORT).show();
                   vibratorAdaptor.completeSound();
               }else{
                   Toast.makeText(getApplicationContext(),Constant.SERVER_ERROR, Toast.LENGTH_SHORT).show();
                   vibratorAdaptor.makeVibration();
               }
                gotoHomePage();
            }catch (Exception ex){
                ex.printStackTrace();
            }
        }
    }
    //Go to Home Page
    private void gotoHomePage(){
        System.gc();
        Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
        startActivity(intent);
        finish();
    }
    //Confirm Exit
    private void confirmExit(){
        screenExitbuilder = new AlertDialog.Builder(AllSettingsActivity.this);
        screenExitbuilder.setMessage("Do you want to exit");
    }
}
