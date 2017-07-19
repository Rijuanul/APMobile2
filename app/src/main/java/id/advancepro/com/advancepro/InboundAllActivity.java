package id.advancepro.com.advancepro;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import id.advancepro.com.advancepro.adapter.InboundChildAdapter;
import id.advancepro.com.advancepro.adapter.InboundHeaderDetailsAdapter;
import id.advancepro.com.advancepro.adapter.ResetForAllAdapter;
import id.advancepro.com.advancepro.adapter.VibratorAdaptor;
import id.advancepro.com.advancepro.model.BoundSettingsEntity;
import id.advancepro.com.advancepro.model.InboundChildDefaultPickLocationEntity;
import id.advancepro.com.advancepro.model.InboundChildEntity;
import id.advancepro.com.advancepro.model.InboundHeaderDetailsEntity;
import id.advancepro.com.advancepro.model.InboundOrderHeaderEntity;
import id.advancepro.com.advancepro.service.AsynLoadingService;
import id.advancepro.com.advancepro.service.BoundSettingsService;
import id.advancepro.com.advancepro.service.InboundChildDefaultLocationService;
import id.advancepro.com.advancepro.service.InboundChildDetailsService;
import id.advancepro.com.advancepro.service.InboundCompleteOrderService;
import id.advancepro.com.advancepro.service.InboundHeaderDetailService;
import id.advancepro.com.advancepro.service.InboundOrderHeaderService;
import id.advancepro.com.advancepro.service.InternetConnection;
import id.advancepro.com.advancepro.utils.Constant;

public class InboundAllActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private InternetConnection intConn;
    private SharedPreferences sharedpreferences;
    private String makeUrl;
    private String passCode;
    private String service_url;
    private String companyDBName;
    private Integer userID;
    private Integer warehouseID;
    private Integer inboundSettingsID;
    private Boolean checkInternet=false;

    private Button inboundToday;
    private Button inboundToRecv;
    private Button inboundRecv;

    private TextView headingtext;

    private LinearLayout filterLayout;
    private LinearLayout backBtnAndLabelLayout;
    private LinearLayout completeBtnLayout;
    private LinearLayout upcSkuAddLayout;
    private TextView filtertext;
    private TextView upcSkutext;
    private BoundSettingsEntity boundSettingsEntity;
    private String jsonString;
    private ListView toRecvInboundHeaderListView;
    private InboundHeaderDetailsAdapter inboundHeaderDetailsAdapter;
    private InboundChildAdapter inboundChildAdapter;
    private InboundOrderHeaderEntity inboundOrderHeaderEntityRef;
    private List<InboundHeaderDetailsEntity> inboundHeaderDetailsEntitiesFilter;
    private List<InboundHeaderDetailsEntity> inboundHeaderDetailsEntitiesFilterList;
    private List<InboundChildEntity> inboundChildEntitiesFilter;
    private List<InboundChildEntity> inboundChildEntitiesFilterList;
    private InboundChildEntity inboundChildEntity;
    private ProgressBar loadingGif;
    private TextView poHeadding;
    private TextView qtyHeading;
    private TextView companyHeading;
    private ImageView torecvBackIcon;
    private Boolean childStatus=false;
    private Boolean isQuantityRecv;
    Integer inboundHeaderQtyReceived=0;
    private Integer orderId;
    private Boolean inboundChildSubmit=false;
    private Boolean inboundPOsubmit=false;
    private Boolean shouldClick=false;
    private String defaultInbound="";
    private Integer defaultInboundVal=0;// Default Inbound Value
    private JSONObject inboundOrderObject;
    private JSONArray  inboundOrderArray;
    private Button completeOrderBtn;
    private VibratorAdaptor vibratorAdaptor;
    private View inboundPopUpView;
    private EditText inboundPopUpBillRefTxt;
    private Button inboundPopUpBillRef;
    private Button inboundPopUpBillRefClose;
    private LinearLayout inboundUpcSkuLockLayout;
    private LinearLayout inboundUpcSkuUnLockLayout;
    private View inboundPopUpLotSerialView;
    private TextView inboundPopUpSkutxt;
    private TextView inboundPopUpUpctxt;
    private EditText inboundPopUpSerialTxt;
    private EditText inboundPopUpLotTxt;
    private EditText inboundPopUpQtyTxt;
    private Spinner inboundPopUpPickLocationDrpDwn;
    private Button inboundPopUpCompleteLotSerial;
    private Button inboundPopUpCloseLotSerial;
    private LinearLayout inboundPopUpSerialLockLayout;
    private LinearLayout inboundPopUpSerialUnLockLayout;
    private LinearLayout inboundPopUpLotLockLayout;
    private LinearLayout inboundPopUpLotUnLockLayout;
    private LinearLayout inboundPopUpQtyLockLayout;
    private LinearLayout inboundPopUpQtyUnLockLayout;
    private Boolean shouldInboundPopUpOpen=false;
    private AlertDialog dialogInboundPopUP ;
    private AlertDialog.Builder screenExitbuilder;
    private EditText inboundPopUpPickingLocationtxt;
    private Boolean inboundPoUppickinglocationInitialize;
    private List<InboundChildDefaultPickLocationEntity> inboundChildAvailablePickLocationEntityListRef;
    private TextView inboundPopUpTotQtyLabel;
    private Boolean isFirstTimePopOpen;
    private Integer inboundPopUpQtyRecvForitem;
    private Integer inboundPopUptotalQtyForItem;
    private Boolean afterComleteInboundPopup;


    /* Most of the ID field name start with torecv but used for rest of two functionality like Today and Received. It is little bit difficult to understand for
        new developer. Previously UI designed based on Tab view but changed the design into current one. Most of the work was done before change the design. That's
        why ID field name didn't change.It should be unique for all but just ignore that things.I hope this comment will help to understand why its same for all.
        Current design is based on Button click. which is almost working like tab view. But Its sharing same content for all Inbound Functionalities. */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inbound_all);

        try {

            //SharedPreferences Value
            sharedpreferences = getSharedPreferences(Constant.LOGIN_PREF, Context.MODE_PRIVATE);
            passCode = sharedpreferences.getString(Constant.PASS_CODE, null);
            service_url = sharedpreferences.getString(Constant.SERVICE_URL, null);
            companyDBName = sharedpreferences.getString(Constant.LOGIN_DB, null);
            userID = Integer.parseInt(sharedpreferences.getString(Constant.LOGIN_USERID, "0"));
            warehouseID = Integer.parseInt(sharedpreferences.getString(Constant.DEFAULT_WAREHOUSE_ID, "0"));
            inboundSettingsID = Integer.parseInt(sharedpreferences.getString(Constant.INBOUND_SETTING_ID, "0"));
            intConn = new InternetConnection();//Internet Connection Initialize

            confirmExit();//Home page initialize

            //Inbound Sku,Upc LocK Unlock Layout
            inboundUpcSkuLockLayout=(LinearLayout)findViewById(R.id.torecvUpcSkulocklayout);
            inboundUpcSkuUnLockLayout=(LinearLayout)findViewById(R.id.torecvUpcSkuunlocklayout);
            //Vibrator Initialization
            vibratorAdaptor=new VibratorAdaptor(getApplicationContext());
            inboundPoUppickinglocationInitialize=false;
            //orderId=0;
            defaultInbound="Today";

            toolbar = (Toolbar) findViewById(R.id.menutoolbarinbound);
            toolbar.setTitle("Inbound");
            setSupportActionBar(toolbar);

            inboundToday = (Button) findViewById(R.id.todaybutton); //Inbound Today Receive Button
            inboundToRecv = (Button) findViewById(R.id.torecvbutton); //Inbound To Receive Button
            inboundRecv = (Button) findViewById(R.id.recvbutton); //Inbound  Received Button

            completeOrderBtn=(Button)findViewById(R.id.torecevbutton); // Complete Order Button
            completeOrderBtn.setVisibility(View.INVISIBLE); // Hide Complete Order Button

            headingtext = (TextView) findViewById(R.id.inboundheading);
            isQuantityRecv=false; // Before Receive Quantity make it false

            afterComleteInboundPopup=false;//Initially it will be false
            //Reset some value from some specific table
            resetForAllFunction(); // Reset some table
            //Inbound settings for Inbound
            getInboundSetting(); // Get Inbound Settings
            headingtext.setText("Today"); // heading for Today Receive

            //When come to Inbound screen set background layout for today button
            inboundToday.setBackgroundDrawable(getResources().getDrawable(R.drawable.heading_button_border));
            //Inbound Screen Field Initialize
            filterLayout = (LinearLayout)findViewById(R.id.torecevfilterlayout);
            backBtnAndLabelLayout = (LinearLayout) findViewById(R.id.torecevlabellayout);
            completeBtnLayout = (LinearLayout) findViewById(R.id.torecvInboundCompBtnlayout);
            upcSkuAddLayout = (LinearLayout) findViewById(R.id.torecvUpcSkulayout);
            filtertext = (TextView) findViewById(R.id.filterTorecvTxt);
            loadingGif=(ProgressBar)findViewById(R.id.toRecvProgressBar);
            poHeadding=(TextView)findViewById(R.id.torecevheadingpo);
            qtyHeading=(TextView)findViewById(R.id.torecevheadinqty);
            companyHeading=(TextView)findViewById(R.id.torecevheading);
            torecvBackIcon=(ImageView)findViewById(R.id.torecevimageView);
            upcSkutext=(TextView)findViewById(R.id.torecvUpcSku);
            upcSkutext.setFocusableInTouchMode(true);

            //Inbound Today Button Click
            inboundToday.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(isQuantityRecv){
                        screenExitbuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            goTodayButton();
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
                    }else{
                        goTodayButton();
                    }

                }
            });
            //Inbound To Receive Button Click
            inboundToRecv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(isQuantityRecv) {
                        screenExitbuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            goToRecieveButton();
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
                    }else{
                        goToRecieveButton();
                    }
                }
            });
            // Inbound Completed order(Received) Button Click
            inboundRecv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(isQuantityRecv) {
                        screenExitbuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            goRecievedButton();
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
                    }else{
                        goRecievedButton();
                    }
                }
            });

            toRecvInboundHeaderListView = (ListView)findViewById(R.id.torecListView);//Inbound Item ListView Initialize
            childStatus=false; // When Showing PO Order and Sub Child Item is false
            orderId=0; // Without no Order amke order Id 0
            inboundPOsubmit=false; // make inboundPOsubmit false this is for making difference between PO order or Item(Child Item) of PO order and Also user for PO filtering
            inboundChildSubmit=false;// make inboundChildSubmit false this is for making difference between PO order or Item(Child Item) of PO order and Also user for PO child item filtering
            shouldClick=false; // this is identify that device listview touch
            toRecvInboundHeaderListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {  // Listview Item Click
                if(childStatus && shouldClick){ // Item touch and PO child item is in the List
                    String status=((TextView) (view.findViewById(R.id.inboundChildProductStatus))).getText().toString();
                    if(Integer.parseInt(status)==0){
                        vibratorAdaptor.makeScanAndTouchSound();
                        String orderDetailID = ((TextView) (view.findViewById(R.id.hiddenOrderDetId))).getText().toString();
                        //System.out.println(shouldInboundPopUpOpen);
                        if(shouldInboundPopUpOpen) {
                            openInboundSerialLotView(orderDetailID);
                        }else{
                            setInboundChildAndHeaderByScan(orderDetailID);
                        }
                    }
                }else if(!childStatus && shouldClick){ // Item touch and PO item is in the List
                    vibratorAdaptor.makeScanAndTouchSound();
                    String orderNO = ((TextView) (view.findViewById(R.id.inboundOrderNo))).getText().toString();
                    loadingGifEnable();
                    toRecvInboundHeaderListView.setAdapter(null);
                    getInboundOrderHeader(orderNO); // get header details based on  PO number
                    getInboundChildDetails(orderNO); // get child details based on  PO number
                }
                }
            });
            //Device Listview item touch
            toRecvInboundHeaderListView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                //System.out.println("Touch"+event);
                shouldClick=true;// If touch make it true
                upcSkutext.requestFocus();
                return false;
                }
            });
            //Custome Back Button Click not device
            torecvBackIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(isQuantityRecv) { //if a quantity receive
                        screenExitbuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                backButtonCommonFeature();
                                dialog.dismiss();
                            }
                        });
                        screenExitbuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                        screenExitbuilder.show(); // Alert Box Show
                    }else {
                        backButtonCommonFeature(); // Common Feature of back Button Click
                    }

                }
            });
            //Search PO number
            filtertext.setOnKeyListener(new View.OnKeyListener() {
                @Override
                public boolean onKey(View v, int keyCode, KeyEvent event) {
                    inboundPOsubmit=true; // make InboundPOSubmit true
                    if(!StringUtils.isEmpty(filtertext.getText())) {
                        if (keyCode == 66 && event.getAction() == KeyEvent.ACTION_UP) {
                            getFilterData(inboundHeaderDetailsEntitiesFilter, filtertext.getText().toString());// get Filter data based on PO Number
                            if(inboundHeaderDetailsEntitiesFilterList.size() > 0){
                                if (inboundHeaderDetailsEntitiesFilterList.size() == 1) {
                                    vibratorAdaptor.makeScanAndTouchSound();
                                    loadingGifEnable();
                                    getInboundOrderHeader(inboundHeaderDetailsEntitiesFilterList.get(0).getOrderNo());//Get Inbound Header Details
                                    getInboundChildDetails(inboundHeaderDetailsEntitiesFilterList.get(0).getOrderNo()); //Get Inbound Child Details
                                }else{
                                    filtertext.setText("");
                                    filtertext.requestFocus();
                                }
                            }else{
                                Toast.makeText(getApplicationContext(), Constant.EMPTY_DATA, Toast.LENGTH_SHORT).show();
                                vibratorAdaptor.makeVibration();
                                filtertext.setText("");
                                filtertext.requestFocus();
                                setListView(inboundHeaderDetailsEntitiesFilter);
                            }
                        }

                        inboundPOsubmit=false;
                    }
                    return false;
                }
            });
            filtertext.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {

                    if(!inboundPOsubmit) {
                        getFilterData(inboundHeaderDetailsEntitiesFilter, s.toString()); // Get Po based on text change
                    }
                }
            });

            filtertext.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                    if(actionId == EditorInfo.IME_ACTION_DONE){


                    }
                    return true;
                }
            });
            // Search Child Item of PO based on SKU/UPC
            upcSkutext.setOnKeyListener(new View.OnKeyListener() {
                @Override
                public boolean onKey(View v, int keyCode, KeyEvent event) {
                    if(!StringUtils.isEmpty(upcSkutext.getText())) {
                        if (keyCode == 66 && event.getAction() == KeyEvent.ACTION_UP) {
                            inboundChildSubmit = true;//make Inbound PO ChildSubmit true
                            //System.out.println("click");
                            getInboundChildFilterData(inboundChildEntitiesFilter, upcSkutext.getText().toString());//Get ChildItem Filter Data
                            if (inboundChildEntitiesFilterList.size() == 1 ) {
                                if(inboundChildEntitiesFilterList.get(0).getStatus()==0) {
                                    vibratorAdaptor.makeScanAndTouchSound();
                                    //System.out.println(shouldInboundPopUpOpen);
                                    if (shouldInboundPopUpOpen) {
                                        openInboundSerialLotView(inboundChildEntitiesFilterList.get(0).getOrderDetailid());

                                    } else {
                                        setInboundChildAndHeaderByScan(inboundChildEntitiesFilterList.get(0).getOrderDetailid().toString());
                                    }
                                }else{
                                    Toast.makeText(getApplicationContext(), Constant.RICH_LIMIT, Toast.LENGTH_SHORT).show();
                                    vibratorAdaptor.makeVibration();
                                }
                            } else {
                                Toast.makeText(getApplicationContext(), Constant.EMPTY_DATA, Toast.LENGTH_SHORT).show();
                                vibratorAdaptor.makeVibration();
                                setInboundChildView(inboundChildEntitiesFilter);
                            }
                            upcSkutext.setText("");
                        }

                        inboundChildSubmit = false;
                    }
                    return false;
                }
            });

            upcSkutext.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if(!inboundChildSubmit) {
                        //System.out.println("changeafter");
                        getInboundChildFilterData(inboundChildEntitiesFilter, s.toString());//Get Inbound Child item on tet change
                    }

                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });

            upcSkutext.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                    if(actionId == EditorInfo.IME_ACTION_DONE){


                    }
                    return true;
                }
            });

            completeOrderBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        openBillRefNoPppUp();//Open pop up for Bill Ref number after clicking the Complete Order button
                    }catch (Exception ex){
                        ex.printStackTrace();
                    }
                }
            });
            //Inbound Sku,Upc Lock Unlock Click Event
            //Lock
            inboundUpcSkuLockLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    inboundUpcSkuLockLayout.setVisibility(View.GONE);
                    inboundUpcSkuUnLockLayout.setVisibility(View.VISIBLE);
                    shouldInboundPopUpOpen=false;
                    SharedPreferences.Editor editor = sharedpreferences.edit();
                    editor.putString(Constant.IS_INBOUND_POPUP_OPEN, Constant.LOCK_TEXT);
                    editor.commit();
                }
            });
            //Unlock
            inboundUpcSkuUnLockLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    inboundUpcSkuLockLayout.setVisibility(View.VISIBLE);
                    inboundUpcSkuUnLockLayout.setVisibility(View.GONE);
                    shouldInboundPopUpOpen=true;
                    SharedPreferences.Editor editor = sharedpreferences.edit();
                    editor.putString(Constant.IS_INBOUND_POPUP_OPEN, Constant.UNLOCK_TEXT);
                    editor.commit();
                    upcSkutext.setText("");
                    upcSkutext.requestFocus();
                }
            });

        }catch (Exception ex){
            ex.printStackTrace();
        }

    }

    private void backToOriginalPage(){
        shouldClick=false;
        loadingGifEnable();
        toRecvInboundHeaderListView.setAdapter(null);
        hideChildLayout();
        orderId=0;
        getInboundHeaderPage(); // Get Inbound PO List
    }
    //Device Back Button Pressed
    @Override
    public void onBackPressed() {
        if(childStatus){
            if(isQuantityRecv) {
                screenExitbuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        backButtonCommonFeature();
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
            }else {
                backButtonCommonFeature(); // back Button Common Feature
            }
        }else{
            goToHomePage();

        }
    }
    //App Stop
    @Override
    protected void onStop()
    {
        super.onStop();
        if(childStatus){
            if(isQuantityRecv) {
                screenExitbuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        backButtonCommonFeature();
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
            }else {
                backButtonCommonFeature(); // back Button Common Feature
            }
        }/*else{
            goToHomePage();
        }*/
    }


    //Option Menu Creation
    public boolean onCreateOptionsMenu(Menu menu){
        try {
            MenuInflater menuInflater=getMenuInflater();
            menuInflater.inflate(R.menu.menu_inbound,menu);
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return super.onCreateOptionsMenu(menu);
    }

    //Option Menu SelectedItem
    public boolean onOptionsItemSelected(MenuItem menuItem){
        try {
            if (menuItem.getItemId() == R.id.inbound_refresh) {
                if(isQuantityRecv) {
                    screenExitbuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            inboundRefreshPage();
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
                }else {
                    inboundRefreshPage(); // Refresh Inbound Screen
                }

            }else if(menuItem.getItemId() == R.id.inbound_backto_home){
                if(childStatus){
                    if(isQuantityRecv) {
                        screenExitbuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                backButtonCommonFeature();
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
                    }else {
                        backButtonCommonFeature();// Common Feature of back Button
                    }
                }else{
                    goToHomePage(); // Go to home page
                    return true;
                }
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return super.onOptionsItemSelected(menuItem);
    }
    //Inbound Seetings
    private void getInboundSetting(){
        try{
            checkInternet=intConn.checkConnection(this); // Internet connection check
            if(checkInternet) {
                makeUrl = service_url + "/GetBoundSettings/" + companyDBName + "/" + userID + "/" + inboundSettingsID + "/" + passCode;
                new InboundSetting().execute(makeUrl);
            }else {
                internetConnectionMessage(); // Internet Connection Error Message
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    public class InboundSetting extends AsyncTask<String, String, BoundSettingsEntity> {
        @Override
        protected BoundSettingsEntity doInBackground (String...params){
            BoundSettingsService boundSettingsService = new BoundSettingsService();
            return boundSettingsService.getInboundSettingsList(params[0]);
        }

        @Override
        protected void onPostExecute (BoundSettingsEntity boundSettingsEntityRef){
            try {
                boundSettingsEntity = boundSettingsEntityRef;
                getInboundHeaderPage();// Get Inbound PO List
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    private void getInboundHeaderPage(){
        try {
            //System.out.println(defaultInbound);
            final JSONObject root = new JSONObject();
            root.put("CompanyDbName", companyDBName);
            root.put("UserId", userID);
            if(defaultInbound.equals("Today")){
                defaultInboundVal=Constant.INBOUND_TODAY;
                root.put("RecevingButtonId", Constant.INBOUND_TODAY);
            }else if(defaultInbound.equals("ToReceive")){
                defaultInboundVal=Constant.INBOUND_TO_RECEIVE;
                root.put("RecevingButtonId", Constant.INBOUND_TO_RECEIVE);
            }else if(defaultInbound.equals("Received")){
                defaultInboundVal=Constant.INBOUND_RECEIVED;
                root.put("RecevingButtonId", Constant.INBOUND_RECEIVED);
            }

            root.put("WhID", warehouseID);
            root.put("Rows", Constant.RECORD_PER_PAGE);
            root.put("clickcount", Constant.INDEX_PAGE);
            root.put("Key", passCode);
            root.put("SearchKey", filtertext.getText());
            jsonString= root.toString();
            makeUrl = service_url +"/GetInBoundHeaderDetails";
            new InboundHeaderDetails().execute(makeUrl);
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    public class InboundHeaderDetails extends AsyncTask<String, String, List<InboundHeaderDetailsEntity>> {
        @Override
        protected List<InboundHeaderDetailsEntity> doInBackground(String... params) {
            InboundHeaderDetailService inboundHeaderDetailService= new InboundHeaderDetailService();
            inboundHeaderDetailService.initalValue(service_url,passCode,userID,companyDBName,warehouseID,Constant.INDEX_PAGE,defaultInboundVal, boundSettingsEntity);
            return inboundHeaderDetailService.getInboundHeaderDetails(params[0],jsonString);
        }

        @Override
        protected void onPostExecute(List<InboundHeaderDetailsEntity> inboundHeaderDetailsEntities) {
            try {
                hideChildLayout(); // hide Inbound PO Child layout
                loadingGifDisable();
                //childStatus=false;
                if(inboundHeaderDetailsEntities!=null && inboundHeaderDetailsEntities.size()>0) {
                    setListView(inboundHeaderDetailsEntities); // Set inbound PO in List View
                    inboundHeaderDetailsEntitiesFilter = inboundHeaderDetailsEntities; // Assign Reference

                }else{
                    toRecvInboundHeaderListView.setAdapter(null);
                    Toast.makeText(getApplicationContext(), Constant.EMPTY_DATA, Toast.LENGTH_SHORT).show();
                    vibratorAdaptor.makeVibration();

                }

            }catch (Exception ex){
                ex.printStackTrace();
            }
            //System.out.println(childStatus);
        }
    }
    // Set inbound PO in List View
    private void setListView(List<InboundHeaderDetailsEntity> inboundHeaderDetailsEntities){
        try {
            if(inboundHeaderDetailsEntities!=null && inboundHeaderDetailsEntities.size()>0) {
                inboundHeaderDetailsAdapter = new InboundHeaderDetailsAdapter(getApplicationContext(), R.layout.inboundparentlayout, inboundHeaderDetailsEntities, boundSettingsEntity);
                toRecvInboundHeaderListView.setAdapter(inboundHeaderDetailsAdapter);
            }else{
                toRecvInboundHeaderListView.setAdapter(null);
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }
    //Inbound PO Filter Data
    private void getFilterData(List<InboundHeaderDetailsEntity> inboundHeaderDetailsEntities,String filterTxt){
        try {
            inboundHeaderDetailsEntitiesFilterList = new ArrayList<>();
            if(!StringUtils.isEmpty(filterTxt) && filterTxt.length()>0) {
                if(inboundHeaderDetailsEntities!=null && inboundHeaderDetailsEntities.size()>0){
                    for (InboundHeaderDetailsEntity inboundHeaderDetailsEntity : inboundHeaderDetailsEntities) {
                        if (inboundPOsubmit) {
                            if (inboundHeaderDetailsEntity.getPurchaseorderno().toLowerCase().toString().trim().equals(filterTxt.toLowerCase().trim())) {
                                inboundHeaderDetailsEntitiesFilterList.add(inboundHeaderDetailsEntity);
                            }
                        } else {
                            if (inboundHeaderDetailsEntity.getPurchaseorderno().toLowerCase().toString().trim().contains(filterTxt.toLowerCase().trim())) {
                                inboundHeaderDetailsEntitiesFilterList.add(inboundHeaderDetailsEntity); //On Text Change
                            }
                        }
                    }
                    if (inboundHeaderDetailsEntitiesFilterList != null && inboundHeaderDetailsEntitiesFilterList.size() > 0) {
                        setListView(inboundHeaderDetailsEntitiesFilterList);
                    } else {
                        setListView(inboundHeaderDetailsEntitiesFilter);
                    }
                }
            }else{
                setListView(inboundHeaderDetailsEntities);
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    //Inbound PO Order Header (Web Service Call)
    private void getInboundOrderHeader(String orderNo){
        try{
            makeUrl=service_url + "/GetInBoundOrderHeader/" + companyDBName + "/" + userID + "/" + orderNo + "/" + passCode ;
            new InboundOrderHeader().execute(makeUrl);
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    public class InboundOrderHeader extends AsyncTask<String, String, InboundOrderHeaderEntity> {
        @Override
        protected InboundOrderHeaderEntity doInBackground(String... params) {
            InboundOrderHeaderService inboundHeaderDetailService = new InboundOrderHeaderService();
            return inboundHeaderDetailService.getInboundOrderHeader(params[0]);
        }

        @Override
        protected void onPostExecute(InboundOrderHeaderEntity inboundOrderHeaderEntity) {
            try {
                if(inboundOrderHeaderEntity!=null){
                    orderId=0;
                    inboundOrderHeaderEntityRef=inboundOrderHeaderEntity;
                    orderId=Integer.parseInt(inboundOrderHeaderEntity.getOrderID());
                    poHeadding.setText("P.O #"+inboundOrderHeaderEntity.getPurchaseno());
                    qtyHeading.setText(inboundOrderHeaderEntity.getQuantityReceived() +"/"+ inboundOrderHeaderEntity.getTotQuantity());
                    companyHeading.setText(inboundOrderHeaderEntity.getCompanyName());
                    inboundHeaderQtyReceived=0;
                    inboundHeaderQtyReceived=inboundHeaderQtyReceived+Integer.parseInt(inboundOrderHeaderEntity.getQuantityReceived());
                    toRecvInboundHeaderListView.setAdapter(null);
                }else{
                    Toast.makeText(getApplicationContext(), Constant.SERVER_ERROR, Toast.LENGTH_SHORT).show();
                    vibratorAdaptor.makeVibration();
                }

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    // Get Inbound PO Child Details (Web Service Call)
    public void getInboundChildDetails(String orderNO){
        try{
            makeUrl=service_url + "/GetInBoundOrderChild/" + companyDBName + "/" + userID+ "/" + orderNO  + "/" + passCode ;
            new InboundChildDetails().execute(makeUrl);
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    public class InboundChildDetails extends AsyncTask<String, String, List<InboundChildEntity>> {
        @Override
        protected List<InboundChildEntity> doInBackground(String... params) {
            InboundChildDetailsService inboundChildDetailsService = new InboundChildDetailsService();
            return inboundChildDetailsService.getInboundChildDetails(params[0]);
        }

        @Override
        protected void onPostExecute(List<InboundChildEntity> inboundChildEntities) {
            try {
                loadingGifDisable();
                if(inboundChildEntities!=null && inboundChildEntities.size()>0){
                    visiblelayoutForChildDetails();
                    inboundChildEntitiesFilter=inboundChildEntities;
                    //System.out.println("2");
                    inboundOrderArray=new JSONArray();
                    //completeOrderEntityList=new ArrayList<>();
                    setInboundChildView(inboundChildEntitiesFilter);

                }else{
                    Toast.makeText(getApplicationContext(), Constant.EMPTY_DATA, Toast.LENGTH_SHORT).show();
                    vibratorAdaptor.makeVibration();
                }

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
    // Inbound PO Child Filter Data
    private void getInboundChildFilterData(List<InboundChildEntity> inboundChildEntities,String filterTxtSeq){
        try {
            inboundChildEntitiesFilterList = new ArrayList<>();
            if(!StringUtils.isEmpty(filterTxtSeq) && filterTxtSeq.length()>0) {
                for (InboundChildEntity inboundChildEntity : inboundChildEntities) {
                    if(inboundChildSubmit){
                        if (inboundChildEntity.getProductSKU().toString().equals(filterTxtSeq) ||
                                inboundChildEntity.getUPC().toString().equals(filterTxtSeq)) {
                            inboundChildEntitiesFilterList.add(inboundChildEntity);
                            //System.out.println("3-1");
                            break;
                        }
                    }else {
                        String searchText=filterTxtSeq.toLowerCase();
                        searchText=searchText.replace(" ","");
                        String skuTextInList=inboundChildEntity.getProductSKU().toLowerCase().toString();
                        skuTextInList=skuTextInList.replace(" ","");
                        String upcTextInList=inboundChildEntity.getUPC().toLowerCase().toString();
                        upcTextInList=upcTextInList.replace(" ","");
                        //System.out.println(searchText+"==="+ skuTextInList +"===="+ upcTextInList);
                        if (skuTextInList.trim().contains(searchText.trim()) || upcTextInList.trim().contains(searchText.trim())) {
                            inboundChildEntitiesFilterList.add(inboundChildEntity);// Inbound Child on text Change
                            //System.out.println("3");
                        }
                    }
                }
                if(inboundChildEntitiesFilterList!=null && inboundChildEntitiesFilterList.size()>0){
                    setInboundChildView(inboundChildEntitiesFilterList);
                }else{
                    setInboundChildView(inboundChildEntitiesFilter);
                }
            }else{
                Collections.sort(inboundChildEntitiesFilter);// Sort the List
                //System.out.println("4");
                setInboundChildView(inboundChildEntitiesFilter);
            }
        }catch (Exception ex){
            //makeSkuUpcFocusable();
            ex.printStackTrace();
        }
    }
    // Set inbound Po Child List
    private void setInboundChildView(List<InboundChildEntity> inboundChildEntitiesList){
        try {
            childStatus = true;
            toRecvInboundHeaderListView.setAdapter(null);
            inboundChildAdapter = new InboundChildAdapter(getApplicationContext(), R.layout.inboundchildlayout, inboundChildEntitiesList);
            toRecvInboundHeaderListView.setAdapter(inboundChildAdapter);
        }catch (Exception ex){
            //makeSkuUpcFocusable();
            ex.printStackTrace();
        }
    }

    private void setInboundChildAndHeaderByScan(String orderDetailsID){
        try {
            visibleCompleteOrderButton();// Visible Complete Order Button
            //System.out.println("ss");
            for (int i = 0; i < inboundChildEntitiesFilter.size(); i++) {
                if (Integer.parseInt(inboundChildEntitiesFilter.get(i).getQuantityReceived()) < Integer.parseInt(inboundChildEntitiesFilter.get(i).getTotQuantity()) &&
                        inboundChildEntitiesFilter.get(i).getOrderDetailid().equals(orderDetailsID)) {
                    //System.out.println("OrderchildID:" + orderDetailsID);
                    Integer qtyReceived = Integer.parseInt(inboundChildEntitiesFilter.get(i).getQuantityReceived()) + 1;
                    inboundHeaderQtyReceived = inboundHeaderQtyReceived + 1;
                    qtyHeading.setText(inboundHeaderQtyReceived.toString() + "/" + inboundOrderHeaderEntityRef.getTotQuantity());// Change Inbound Header Order Quantity
                    if(inboundHeaderQtyReceived==Integer.parseInt( inboundOrderHeaderEntityRef.getTotQuantity())){
                        completeOrderBtn.setBackgroundColor(Color.parseColor("#264d44"));// If fully received item make green color complete order button
                    }
                    inboundChildEntitiesFilter.get(i).setQuantityReceived(qtyReceived.toString());

                    inboundOrderObject=new JSONObject();
                    inboundOrderObject.put("OrderDetailid",Integer.parseInt(inboundChildEntitiesFilter.get(i).getOrderDetailid()));
                    inboundOrderObject.put("Productid",Integer.parseInt(inboundChildEntitiesFilter.get(i).getProductID()));
                    inboundOrderObject.put("Serialno","");
                    inboundOrderObject.put("ProductQuantity",Integer.parseInt("1"));
                    inboundOrderObject.put("Lot", "");
                    inboundOrderObject.put("CombID", Integer.parseInt(inboundChildEntitiesFilter.get(i).getCombID()));
                    inboundOrderObject.put("Totalquantity", Integer.parseInt(inboundChildEntitiesFilter.get(i).getTotQuantity()));
                    //System.out.println("loca st ");
                    isQuantityRecv=true;
                    getInboundChildDefaultLocation(inboundChildEntitiesFilter.get(i).getProductID(),inboundChildEntitiesFilter.get(i).getCombID());
                    if (Integer.parseInt(inboundChildEntitiesFilter.get(i).getQuantityReceived()) == Integer.parseInt(inboundChildEntitiesFilter.get(i).getTotQuantity())) {
                        inboundChildEntitiesFilter.get(i).setStatus(1);
                        inboundChildEntitiesFilter.get(i).setQtyRecv(false);
                        Collections.sort(inboundChildEntitiesFilter);
                    }else {
                        inboundChildEntitiesFilter.get(i).setQtyRecv(true);
                        InboundChildEntity inboundChildEntity = new InboundChildEntity();
                        inboundChildEntity = inboundChildEntitiesFilter.get(i);//get the Current Item from the List
                        inboundChildEntitiesFilter.remove(i); //Remove Current Item from the List
                        inboundChildEntitiesFilter.add(0, inboundChildEntity);//Add Current Item in the first position of the List
                    }
                    break;
                }
            }
            shouldClick=false;
            upcSkutext.setText("");
            //System.out.println("1");
            setInboundChildView(inboundChildEntitiesFilter);
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    //Inbound Child Default Location
    private void getInboundChildDefaultLocation(String productID, String combID){
        try {
            //System.out.println("loca in ");
            final JSONObject root = new JSONObject();
            root.put("CompanyDbName", companyDBName);
            root.put("ProductID", productID);
            root.put("WHID", warehouseID);
            root.put("Key", passCode);
            root.put("CombID", combID);
            root.put("Searchkey", upcSkutext.getText());
            jsonString = root.toString();
            makeUrl = service_url + "/GetInBoundSeiralNumber";
            new InboundChildDefaultPickLocation().execute(makeUrl);
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    public class InboundChildDefaultPickLocation extends AsyncTask<String, String, InboundChildDefaultPickLocationEntity> {
        @Override
        protected InboundChildDefaultPickLocationEntity doInBackground(String... params) {
            InboundChildDefaultLocationService inboundChildDefaultLocationService=new InboundChildDefaultLocationService();
            return inboundChildDefaultLocationService.getInboundChildDefaultLocation(params[0],jsonString);
        }

        @Override
        protected void onPostExecute(InboundChildDefaultPickLocationEntity inboundChildDefaultPickLocationEntity) {
            try {
                if(inboundChildDefaultPickLocationEntity!=null){
                    inboundOrderObject.put("Plid", inboundChildDefaultPickLocationEntity.getLocationID());
                }else{
                    inboundOrderObject.put("Plid", Integer.parseInt("0"));
                }

                inboundOrderArray.put(inboundOrderObject);
                completeOrderBtn.setVisibility(View.VISIBLE);

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    //Inbound complete order
    public class InboundCompleteOrder extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... params) {
            InboundCompleteOrderService inboundCompleteOrderService=new InboundCompleteOrderService();
            return inboundCompleteOrderService.completeInboundOrder(params[0],jsonString);
        }

        @Override
        protected void onPostExecute(String str) {
            try {
                if(!StringUtils.isEmpty(str)){
                    isQuantityRecv=false;
                    backToOriginalPage();
                    Toast.makeText(getApplicationContext(), "Bill Reference Number Saved", Toast.LENGTH_SHORT).show();
                    vibratorAdaptor.completeSound();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                isQuantityRecv=false;
                Toast.makeText(getApplicationContext(), Constant.SERVER_ERROR, Toast.LENGTH_SHORT).show();
                vibratorAdaptor.makeVibration();
            }
        }
    }


    private  void loadingGifDisable(){
        loadingGif.setVisibility(View.GONE);
    }

    private void loadingGifEnable(){
        loadingGif.setVisibility(View.VISIBLE);
    }

    // Visible layout for inbound PO Child
    private void visiblelayoutForChildDetails(){
        filterLayout.setVisibility(LinearLayout.GONE);
        backBtnAndLabelLayout.setVisibility(LinearLayout.VISIBLE);
        completeBtnLayout.setVisibility(LinearLayout.VISIBLE);
        upcSkuAddLayout.setVisibility(LinearLayout.VISIBLE);
        upcSkutext.requestFocus();
        //Inbound SKu,Upc Lock/Unlock For Popup
        if(sharedpreferences.getString(Constant.IS_INBOUND_POPUP_OPEN, "").equals(Constant.LOCK_TEXT)){
            inboundUpcSkuLockLayout.setVisibility(View.GONE);
            inboundUpcSkuUnLockLayout.setVisibility(View.VISIBLE);
            shouldInboundPopUpOpen=false;
        }else {
            inboundUpcSkuLockLayout.setVisibility(View.VISIBLE);
            inboundUpcSkuUnLockLayout.setVisibility(View.GONE);
            shouldInboundPopUpOpen=true;
        }
        isQuantityRecv=false;
    }
    // Hide Inbound  Child layout for PO
    private void hideChildLayout(){
        try {
            childStatus=false;
            inboundPOsubmit=true;
            inboundChildSubmit=true;
            filtertext.setText("");
            upcSkutext.setText("");
            filterLayout.setVisibility(LinearLayout.VISIBLE);
            backBtnAndLabelLayout.setVisibility(LinearLayout.GONE);
            completeBtnLayout.setVisibility(LinearLayout.GONE);
            upcSkuAddLayout.setVisibility(LinearLayout.GONE);
            filtertext.requestFocus();
            inboundPOsubmit=false;
            inboundChildSubmit=false;
            isQuantityRecv=false;
            visibleCompleteOrderButton();

        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    // Internet Connection Error Message
    private void internetConnectionMessage(){
        Toast.makeText(InboundAllActivity.this, getApplicationContext().getResources().getString(R.string.error001), Toast.LENGTH_SHORT).show();
        vibratorAdaptor.makeVibration();
    }

    private void listViewMakeBlank(){
        toRecvInboundHeaderListView.setAdapter(null);
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


    //Bill Ref POP UP
    private void openBillRefNoPppUp(){
        try {
            //POP UP Field
            inboundPopUpView=getLayoutInflater().inflate(R.layout.inboundbillrefpopup,null);
            inboundPopUpBillRefTxt=(EditText)inboundPopUpView.findViewById(R.id.inboundbillreftxt);
            inboundPopUpBillRef=(Button)inboundPopUpView.findViewById(R.id.inboundbillrefsubmit);
            inboundPopUpBillRefClose=(Button)inboundPopUpView.findViewById(R.id.inboundbillrefclose);
            //Alert Dialougue
            AlertDialog.Builder builder = new AlertDialog.Builder(InboundAllActivity.this);
            builder.setView(inboundPopUpView);
            final AlertDialog dialogInboundPopUPBillRef = builder.create();
            dialogInboundPopUPBillRef.show();
            //Bill/Ref Text On key Listener
            inboundPopUpBillRefTxt.setOnKeyListener(new View.OnKeyListener() {
                @Override
                public boolean onKey(View v, int keyCode, KeyEvent event) {
                    if(keyCode==66 && event.getAction()==KeyEvent.ACTION_UP){
                        if(!StringUtils.isEmpty(inboundPopUpBillRefTxt.getText())) {
                            completeInboundBillRef();
                        }
                    }
                    return false;
                }
            });
            //Inbound Pop Up Bill/Ref Close
            inboundPopUpBillRefClose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialogInboundPopUPBillRef.dismiss();
                }
            });
            //Inbound Pop Up Bill / Ref complete Submit
            inboundPopUpBillRef.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        if (StringUtils.isEmpty(inboundPopUpBillRefTxt.getText())) {
                            Toast.makeText(InboundAllActivity.this, "Enter the Bill Reference Number", Toast.LENGTH_SHORT).show();
                            vibratorAdaptor.makeVibration();
                        } else {
                            dialogInboundPopUPBillRef.dismiss();
                            completeInboundBillRef();
                        }
                    }catch (Exception ex){
                        ex.printStackTrace();
                    }
                }
            });

        }catch (Exception ex){
            ex.printStackTrace();
        }
    }
    //Complete Inbound Bill/Ref
    private void completeInboundBillRef(){
        try{
            final JSONObject root = new JSONObject();
            root.put("CompanyDbName", companyDBName);
            root.put("Userid", userID);
            root.put("whid", warehouseID);
            root.put("Billrefno", inboundPopUpBillRefTxt.getText());
            root.put("OrderId", orderId);
            root.put("key", passCode);
            root.put("Lotserial", inboundOrderArray);
            jsonString = root.toString();
            //System.out.println(inboundOrderArray.length());
            makeUrl = service_url + "/PostInboundCompletelotserial";
            new InboundCompleteOrder().execute(makeUrl);

        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    //Open Inbound Serial,Lot POP UP
    private void openInboundSerialLotView(String orderDetailid){
        try {
            //Inbound Alert property value initialization
            inboundPopUpLotSerialView=getLayoutInflater().inflate(R.layout.inboundpopup,null);
            inboundPopUpCompleteLotSerial=(Button)inboundPopUpLotSerialView.findViewById(R.id.inboundpopupsubmit);
            inboundPopUpCloseLotSerial=(Button)inboundPopUpLotSerialView.findViewById(R.id.inboundpopupclose);
            inboundPopUpSerialTxt=(EditText) inboundPopUpLotSerialView.findViewById(R.id.inboundpopupserialtxt);
            inboundPopUpLotTxt=(EditText)inboundPopUpLotSerialView.findViewById(R.id.inboundpopuplottxt);
            inboundPopUpQtyTxt=(EditText)inboundPopUpLotSerialView.findViewById(R.id.inboundpopupqtytxt);
            inboundPopUpPickingLocationtxt=(EditText)inboundPopUpLotSerialView.findViewById(R.id.inboundpickinglocationtxt);
            inboundPopUpPickLocationDrpDwn=(Spinner) inboundPopUpLotSerialView.findViewById(R.id.inboundpopuppicklocationdrpdwn);
            inboundPopUpSkutxt=(TextView) inboundPopUpLotSerialView.findViewById(R.id.inboundpopupskutxt);
            inboundPopUpUpctxt=(TextView)inboundPopUpLotSerialView.findViewById(R.id.inboundpopupupctxt);
            inboundPopUpTotQtyLabel=(TextView)inboundPopUpLotSerialView.findViewById(R.id.inboundpopuptotalqtylabel);
            //Linear Layout
            inboundPopUpLotLockLayout=(LinearLayout)inboundPopUpLotSerialView.findViewById(R.id.inboundpopuplotlocklayout);
            inboundPopUpLotUnLockLayout=(LinearLayout)inboundPopUpLotSerialView.findViewById(R.id.inboundpopuplotunlocklayout);
            inboundPopUpSerialLockLayout=(LinearLayout)inboundPopUpLotSerialView.findViewById(R.id.inboundpopupseriallocklayout);
            inboundPopUpSerialUnLockLayout=(LinearLayout)inboundPopUpLotSerialView.findViewById(R.id.inboundpopupseriaunlocklayout);
            inboundPopUpQtyLockLayout=(LinearLayout)inboundPopUpLotSerialView.findViewById(R.id.inboundpopupqtylocklayout);
            inboundPopUpQtyUnLockLayout=(LinearLayout)inboundPopUpLotSerialView.findViewById(R.id.inboundpopupqtyunlocklayout);

            //Find the product
            for (int i = 0; i < inboundChildEntitiesFilter.size(); i++) {
                if (inboundChildEntitiesFilter.get(i).getOrderDetailid().equals(orderDetailid)) {
                    inboundPopUpSkutxt.setText(inboundChildEntitiesFilter.get(i).getProductSKU());
                    inboundPopUpUpctxt.setText(inboundChildEntitiesFilter.get(i).getUPC());
                    inboundPopUpTotQtyLabel.setText("Quantity: "+inboundChildEntitiesFilter.get(i).getQuantityReceived()+"/"+inboundChildEntitiesFilter.get(i).getTotQuantity());
                    inboundPopUpQtyRecvForitem=0;
                    inboundPopUptotalQtyForItem=Integer.parseInt(inboundChildEntitiesFilter.get(i).getTotQuantity());
                    getInboundChildAvailableLocation(inboundChildEntitiesFilter.get(i).getProductID(), inboundChildEntitiesFilter.get(i).getCombID());
                    inboundChildEntity=inboundChildEntitiesFilter.get(i);
                    break;
                }
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }
    //Inbound Child Available location (Web Service Call)
    private void getInboundChildAvailableLocation(String productID, String combID){
        try {
            //System.out.println("loca in ");
            final JSONObject root = new JSONObject();
            root.put("CompanyDbName", companyDBName);
            root.put("ProductID", productID);
            root.put("WHID", warehouseID);
            root.put("Key", passCode);
            root.put("CombID", combID);
            root.put("Searchkey", "");
            jsonString = root.toString();
            makeUrl = service_url + "/GetInBoundSeiralNumber";
            new InboundChildAvailablePickLocation().execute(makeUrl);
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    public class InboundChildAvailablePickLocation extends AsyncTask<String, String, List<InboundChildDefaultPickLocationEntity>> {
        @Override
        protected List<InboundChildDefaultPickLocationEntity> doInBackground(String... params) {
            InboundChildDefaultLocationService inboundChildDefaultLocationService=new InboundChildDefaultLocationService();
            return inboundChildDefaultLocationService.getInboundChildLocationList(params[0],jsonString);
        }

        @Override
        protected void onPostExecute(List<InboundChildDefaultPickLocationEntity> inboundChildAvailablePickLocationEntityList) {
            try {
                //System.out.println(inboundChildAvailablePickLocationEntityList);
                if(inboundChildAvailablePickLocationEntityList!=null){
                    inboundChildAvailablePickLocationEntityListRef=inboundChildAvailablePickLocationEntityList;
                    ArrayAdapter<InboundChildDefaultPickLocationEntity> adapter = new ArrayAdapter<InboundChildDefaultPickLocationEntity>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, inboundChildAvailablePickLocationEntityList);
                    inboundPopUpPickLocationDrpDwn.setAdapter(adapter);
                    for(int i=0;i<inboundChildAvailablePickLocationEntityList.size();i++){
                        if(inboundChildAvailablePickLocationEntityList.get(i).getIsdefault()==1){
                            inboundPopUpPickLocationDrpDwn.setSelection(i);
                            inboundPopUpPickingLocationtxt.setText(inboundChildAvailablePickLocationEntityList.get(i).getLocationName());
                            break;
                        }
                    }
                    //inboundPopUpPickingLocationtxt.setText("");
                    //Inbound POP Up Alert Dialougue
                    AlertDialog.Builder builder = new AlertDialog.Builder(InboundAllActivity.this);
                    builder.setView(inboundPopUpLotSerialView);
                    dialogInboundPopUP = builder.create();
                    dialogInboundPopUP.setCanceledOnTouchOutside(false);
                    dialogInboundPopUP.show();
                    isFirstTimePopOpen=true;
                    //Get Inbound POP UP Saved Data
                    getInboundPopUpSavedData();
                    new AsynLoading().execute();
                    //Inbound POP UP Lot Lock
                    inboundPopUpLotLockLayout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            inboundPopUpLotLockLayout.setVisibility(View.GONE);
                            inboundPopUpLotUnLockLayout.setVisibility(View.VISIBLE);
                            SharedPreferences.Editor editor = sharedpreferences.edit();
                            editor.putString(Constant.INBOUND_POPUP_LOT_LOCK, Constant.LOCK_TEXT);
                            editor.putString(Constant.INBOUND_POPUP_LOT_VAL, inboundPopUpLotTxt.getText().toString());
                            editor.commit();
                            inboundPopUpLotTxt.setEnabled(false);
                            inboundPopUpFocus();
                        }
                    });
                    //Inbound POP UP Lot UnLock
                    inboundPopUpLotUnLockLayout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            inboundPopUpLotLockLayout.setVisibility(View.VISIBLE);
                            inboundPopUpLotUnLockLayout.setVisibility(View.GONE);
                            SharedPreferences.Editor editor = sharedpreferences.edit();
                            editor.putString(Constant.INBOUND_POPUP_LOT_LOCK, Constant.UNLOCK_TEXT);
                            editor.putString(Constant.INBOUND_POPUP_LOT_VAL, "");
                            editor.commit();
                            inboundPopUpLotTxt.setEnabled(true);
                            inboundPopUpLotTxt.requestFocus();
                        }
                    });
                    // //Inbound POP UP Serial Lock
                    inboundPopUpSerialLockLayout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            inboundPopUpSerialLockLayout.setVisibility(View.GONE);
                            inboundPopUpSerialUnLockLayout.setVisibility(View.VISIBLE);
                            SharedPreferences.Editor editor = sharedpreferences.edit();
                            editor.putString(Constant.INBOUND_POPUP_SERIAL_LOCK, Constant.LOCK_TEXT);
                            editor.putString(Constant.INBOUND_POPUP_SERIAL_VAL, inboundPopUpSerialTxt.getText().toString());
                            editor.commit();
                            inboundPopUpSerialTxt.setEnabled(false);
                            inboundPopUpFocus();
                        }
                    });
                    //Inbound POP UP Serial UnLock
                    inboundPopUpSerialUnLockLayout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            inboundPopUpSerialLockLayout.setVisibility(View.VISIBLE);
                            inboundPopUpSerialUnLockLayout.setVisibility(View.GONE);
                            SharedPreferences.Editor editor = sharedpreferences.edit();
                            editor.putString(Constant.INBOUND_POPUP_SERIAL_LOCK, Constant.UNLOCK_TEXT);
                            editor.putString(Constant.INBOUND_POPUP_SERIAL_VAL, "");
                            editor.commit();
                            inboundPopUpSerialTxt.setEnabled(true);
                            inboundPopUpSerialTxt.requestFocus();
                        }
                    });
                    //Inbound POP UP Quantity Lock
                    inboundPopUpQtyLockLayout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            inboundPopUpQtyLockMechanism();
                        }
                    });
                    //Inbound POP UP Quantity UnLock
                    inboundPopUpQtyUnLockLayout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            inboundPopUpQtyUnLockMechanism();
                        }
                    });
                    //Inbound POP UP Serial On key Listener
                    inboundPopUpSerialTxt.setOnKeyListener(new View.OnKeyListener() {
                        @Override
                        public boolean onKey(View v, int keyCode, KeyEvent event) {
                            if(keyCode==66 && event.getAction()==KeyEvent.ACTION_UP){
                                if(!StringUtils.isEmpty(inboundPopUpSerialTxt.getText().toString())){
                                    inboundPopUpQtyLockMechanism();
                                }
                                new AsynLoading().execute();
                            }
                            return false;
                        }
                    });
                    //Inbound POP UP Lot On key Listener
                    inboundPopUpLotTxt.setOnKeyListener(new View.OnKeyListener() {
                        @Override
                        public boolean onKey(View v, int keyCode, KeyEvent event) {
                            if(keyCode==66 && event.getAction()==KeyEvent.ACTION_UP){
                                new AsynLoading().execute();
                            }
                            return false;
                        }
                    });
                    //Inbound POP UP Quantity On key Listener
                    inboundPopUpQtyTxt.setOnKeyListener(new View.OnKeyListener() {
                        @Override
                        public boolean onKey(View v, int keyCode, KeyEvent event) {
                            if(keyCode==66 && event.getAction()==KeyEvent.ACTION_UP){
                                new AsynLoading().execute();
                            }
                            return false;
                        }
                    });
                    //Inbound POP UP Complete Lot/Serial Click Event
                    inboundPopUpCompleteLotSerial.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if(validationInboundPopUp()) {
                                inboundPoUppickinglocationInitialize=false;
                                completeLotSerial();
                            }
                        }
                    });
                    //Inbound POP UP Close Click Event
                    inboundPopUpCloseLotSerial.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            inboundChildEntity=null;
                            setInboundChildView(inboundChildEntitiesFilter);
                            inboundUpcSkuTextFieldFocus();
                            inboundPoUppickinglocationInitialize=false;
                            dialogInboundPopUP.dismiss();

                        }
                    });
                    //Inbound POP UP Picking location Dropdown
                    inboundPopUpPickLocationDrpDwn.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            //System.out.println(inboundPoUppickinglocationInitialize);
                            if(!inboundPoUppickinglocationInitialize){
                                inboundPoUppickinglocationInitialize=true;
                                return;
                            }
                            InboundChildDefaultPickLocationEntity inboundChildDefaultPickLocationEntity=(InboundChildDefaultPickLocationEntity)inboundPopUpPickLocationDrpDwn.getSelectedItem();
                            if(inboundChildDefaultPickLocationEntity.getLocationID()!=0) {
                                inboundPopUpPickingLocationtxt.setText(inboundChildDefaultPickLocationEntity.getLocationName().toString());
                            }else{
                                inboundPopUpPickingLocationtxt.setText("");
                            }
                            new AsynLoading().execute();
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                    //Inbound pop up picking text focus change
                    inboundPopUpPickingLocationtxt.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                        @Override
                        public void onFocusChange(View v, boolean hasFocus) {
                            if(!hasFocus){
                                if (!StringUtils.isEmpty(inboundPopUpPickingLocationtxt.getText())) {
                                    Boolean matchFind = false;
                                    if (inboundChildAvailablePickLocationEntityListRef != null && inboundChildAvailablePickLocationEntityListRef.size() > 0) {
                                        for (int i = 0; i < inboundChildAvailablePickLocationEntityListRef.size(); i++) {
                                            if (inboundChildAvailablePickLocationEntityListRef.get(i).getLocationName().equals(inboundPopUpPickingLocationtxt.getText().toString())) {
                                                inboundPopUpPickLocationDrpDwn.setSelection(i);
                                                matchFind = true;
                                                break;
                                            }
                                        }
                                        if (!matchFind) {
                                            inboundPopUpPickingLocationtxt.setText("");
                                        }
                                        new AsynLoading().execute();
                                    }
                                }
                            }
                        }
                    });
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
    //Inbound Pop Up Qty Lock Unclock
    private void inboundPopUpQtyLockMechanism(){
        inboundPopUpQtyLockLayout.setVisibility(View.GONE);
        inboundPopUpQtyUnLockLayout.setVisibility(View.VISIBLE);
        if(StringUtils.isEmpty(inboundPopUpQtyTxt.getText()) || !StringUtils.isEmpty(inboundPopUpSerialTxt.getText().toString())){
            inboundPopUpQtyTxt.setText("1");
        }
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString(Constant.INBOUND_POPUP_QTY_LOCK, Constant.LOCK_TEXT);
        editor.putString(Constant.INBOUND_POPUP_QTY_VAL, inboundPopUpQtyTxt.getText().toString());
        editor.commit();
        inboundPopUpQtyTxt.setEnabled(false);
        inboundPopUpFocus();
    }
    //
    private void inboundPopUpQtyUnLockMechanism(){
        if(StringUtils.isEmpty(inboundPopUpSerialTxt.getText().toString())) {
            inboundPopUpQtyLockLayout.setVisibility(View.VISIBLE);
            inboundPopUpQtyUnLockLayout.setVisibility(View.GONE);
            SharedPreferences.Editor editor = sharedpreferences.edit();
            editor.putString(Constant.INBOUND_POPUP_QTY_LOCK, Constant.UNLOCK_TEXT);
            editor.putString(Constant.INBOUND_POPUP_QTY_VAL, "");
            editor.commit();
            inboundPopUpQtyTxt.setEnabled(true);
            inboundPopUpQtyTxt.requestFocus();
        }
    }
    //Get Inbound POP UP Saved Data
    private void getInboundPopUpSavedData(){
        if(sharedpreferences.getString(Constant.INBOUND_POPUP_LOT_LOCK, "").equals(Constant.LOCK_TEXT)){
            inboundPopUpLotTxt.setText(sharedpreferences.getString(Constant.INBOUND_POPUP_LOT_VAL, ""));
            inboundPopUpLotTxt.setEnabled(false);
            inboundPopUpLotLockLayout.setVisibility(View.GONE);
        }else{
            inboundPopUpLotTxt.setText("");
        }

        if(sharedpreferences.getString(Constant.INBOUND_POPUP_SERIAL_LOCK, "").equals(Constant.LOCK_TEXT)){
            inboundPopUpSerialTxt.setText(sharedpreferences.getString(Constant.INBOUND_POPUP_SERIAL_VAL, ""));
            inboundPopUpSerialLockLayout.setVisibility(View.GONE);
            inboundPopUpSerialTxt.setEnabled(false);
        }else{
            inboundPopUpSerialTxt.setText("");
        }

        if(sharedpreferences.getString(Constant.INBOUND_POPUP_QTY_LOCK, "").equals(Constant.LOCK_TEXT)
                && ((inboundPopUpSerialTxt.isEnabled() || !inboundPopUpSerialTxt.isEnabled()) && StringUtils.isEmpty(inboundPopUpSerialTxt.getText().toString()))
                && !isFirstTimePopOpen){
            inboundPopUpQtyTxt.setText(sharedpreferences.getString(Constant.INBOUND_POPUP_QTY_VAL, ""));
            inboundPopUpQtyLockLayout.setVisibility(View.GONE);
            inboundPopUpQtyTxt.setEnabled(false);
        }else{
            inboundPopUpQtyTxt.setText("");
            inboundPopUpQtyLockLayout.setVisibility(View.VISIBLE);
            inboundPopUpQtyUnLockLayout.setVisibility(View.GONE);
            SharedPreferences.Editor editor = sharedpreferences.edit();
            editor.putString(Constant.INBOUND_POPUP_QTY_LOCK, Constant.UNLOCK_TEXT);
            editor.putString(Constant.INBOUND_POPUP_QTY_VAL, "");
            editor.commit();
        }
    }
    //Inbound POP UP Focus
    private void inboundPopUpFocus(){
        if(inboundPopUpPickingLocationtxt.isEnabled() && StringUtils.isEmpty(inboundPopUpPickingLocationtxt.getText())){
            inboundPopUpPickingLocationtxt.requestFocus();
            return;
        }

        if(inboundPopUpSerialTxt.isEnabled() && StringUtils.isEmpty(inboundPopUpSerialTxt.getText())){
            inboundPopUpSerialTxt.requestFocus();
            return;
        }

        if(inboundPopUpLotTxt.isEnabled() && StringUtils.isEmpty(inboundPopUpLotTxt.getText())){
            inboundPopUpLotTxt.requestFocus();
            return;
        }

        if(inboundPopUpQtyTxt.isEnabled() && StringUtils.isEmpty(inboundPopUpQtyTxt.getText())){
            inboundPopUpQtyTxt.requestFocus();
            return;
        }

        //System.out.println(isFirstTimePopOpen);
        if(!isFirstTimePopOpen && !afterComleteInboundPopup) {
            completeLotSerial();
        }else {
            return;
        }

    }
    //Comple Lot/Serial
    private void completeLotSerial(){
        try {
            Integer qtyRecvPopUp = Integer.parseInt(inboundPopUpQtyTxt.getText().toString()) + Integer.parseInt(inboundChildEntity.getQuantityReceived());
            if (qtyRecvPopUp <= Integer.parseInt(inboundChildEntity.getTotQuantity())) {
                visibleCompleteOrderButton();
                if (qtyRecvPopUp == Integer.parseInt(inboundChildEntity.getTotQuantity())) {
                    inboundUpcSkuTextFieldFocus();
                    dialogInboundPopUP.dismiss();
                }
                isQuantityRecv = true;
                inboundHeaderChangeForPopUp();
                completeInboundPopUpLotSerial();
            } else {
                Toast.makeText(InboundAllActivity.this, "Quantity can not be more than Total quantiy", Toast.LENGTH_SHORT).show();
                vibratorAdaptor.errorSound();
            }
        }catch (Exception ex){
            ex.printStackTrace();
            Toast.makeText(InboundAllActivity.this,  "Invalid Quantity", Toast.LENGTH_SHORT).show();
            vibratorAdaptor.errorSound();
            return ;
        }
    }
    //Inbound POP UP Validation
    private Boolean validationInboundPopUp(){
        try {
            if (inboundPopUpPickingLocationtxt.isEnabled() && StringUtils.isEmpty(inboundPopUpPickingLocationtxt.getText().toString())) {
                inboundPopUpPickingLocationtxt.requestFocus();
                Toast.makeText(InboundAllActivity.this, "Location is empty", Toast.LENGTH_SHORT).show();
                vibratorAdaptor.errorSound();
                return false;
            }
            if (inboundPopUpSerialTxt.isEnabled() && StringUtils.isEmpty(inboundPopUpSerialTxt.getText())) {
                inboundPopUpSerialTxt.requestFocus();
                Toast.makeText(InboundAllActivity.this, "Serail is empty", Toast.LENGTH_SHORT).show();
                vibratorAdaptor.errorSound();
                return false;
            }

            if (inboundPopUpLotTxt.isEnabled() && StringUtils.isEmpty(inboundPopUpLotTxt.getText())) {
                inboundPopUpLotTxt.requestFocus();
                Toast.makeText(InboundAllActivity.this, "Lot is empty", Toast.LENGTH_SHORT).show();
                vibratorAdaptor.errorSound();
                return false;
            }

            if (inboundPopUpQtyTxt.isEnabled() && StringUtils.isEmpty(inboundPopUpQtyTxt.getText())) {
                inboundPopUpQtyTxt.requestFocus();
                Toast.makeText(InboundAllActivity.this, "Quantity is empty", Toast.LENGTH_SHORT).show();
                vibratorAdaptor.errorSound();
                return false;
            }

            Integer qtyRecvPopUp = Integer.parseInt(inboundPopUpQtyTxt.getText().toString()) + Integer.parseInt(inboundChildEntity.getQuantityReceived());
            if (qtyRecvPopUp > Integer.parseInt(inboundChildEntity.getTotQuantity())) {
                Toast.makeText(InboundAllActivity.this, "Quantity can not be more than Total quantiy", Toast.LENGTH_SHORT).show();
                vibratorAdaptor.errorSound();
                return false;
            }
        }catch (Exception ex){
            Toast.makeText(InboundAllActivity.this, "Invalid Quantity", Toast.LENGTH_SHORT).show();
            vibratorAdaptor.errorSound();
            return false;
        }
        return true;
    }
    //Complete Inbound POP UP Lot/Serial
    private void completeInboundPopUpLotSerial(){

        try{
            inboundOrderObject=new JSONObject();
            inboundOrderObject.put("OrderDetailid",Integer.parseInt(inboundChildEntity.getOrderDetailid()));
            inboundOrderObject.put("Productid",Integer.parseInt(inboundChildEntity.getProductID()));
            inboundOrderObject.put("Serialno",inboundPopUpSerialTxt.getText().toString());
            inboundOrderObject.put("ProductQuantity",Integer.parseInt(inboundPopUpQtyTxt.getText().toString()));
            inboundOrderObject.put("Lot", inboundPopUpLotTxt.getText().toString());
            inboundOrderObject.put("CombID", Integer.parseInt(inboundChildEntity.getCombID()));
            inboundOrderObject.put("Totalquantity", Integer.parseInt(inboundChildEntity.getTotQuantity()));
            InboundChildDefaultPickLocationEntity inboundChildDefaultPickLocationEntity=(InboundChildDefaultPickLocationEntity)inboundPopUpPickLocationDrpDwn.getSelectedItem();
            inboundOrderObject.put("Plid", inboundChildDefaultPickLocationEntity.getLocationID());
            inboundOrderArray.put(inboundOrderObject);

            completeOrderBtn.setVisibility(View.VISIBLE);
            vibratorAdaptor.makeVibration();
            vibratorAdaptor.completeSound();
            getInboundPopUpSavedData(); // Inbound Popup save data
            afterComleteInboundPopup=true;
            new AsynLoading().execute(); //Fake Loading Serive for focus
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }
    //Inbound Header Change for POP UP
    private void inboundHeaderChangeForPopUp(){
        Integer qtyRecvPopUp=Integer.parseInt(inboundPopUpQtyTxt.getText().toString())+Integer.parseInt(inboundChildEntity.getQuantityReceived());
        inboundHeaderQtyReceived = inboundHeaderQtyReceived + Integer.parseInt(inboundPopUpQtyTxt.getText().toString());
        qtyHeading.setText(inboundHeaderQtyReceived.toString() + "/" + inboundOrderHeaderEntityRef.getTotQuantity());//Inbound header quantity update
        inboundPopUpQtyRecvForitem=inboundPopUpQtyRecvForitem+Integer.parseInt(inboundPopUpQtyTxt.getText().toString());
        inboundPopUpTotQtyLabel.setText("Quantity: "+inboundPopUpQtyRecvForitem+"/"+inboundPopUptotalQtyForItem);//Inbound Popup quantity label Update
        if(inboundHeaderQtyReceived==Integer.parseInt( inboundOrderHeaderEntityRef.getTotQuantity())){
            completeOrderBtn.setBackgroundColor(Color.parseColor("#264d44"));
        }
        for(int i=0;i<inboundChildEntitiesFilter.size();i++){
            if(inboundChildEntitiesFilter.get(i).getOrderDetailid().equals(inboundChildEntity.getOrderDetailid())){
                inboundChildEntitiesFilter.get(i).setQuantityReceived(qtyRecvPopUp.toString());
                if(Integer.parseInt(inboundChildEntitiesFilter.get(i).getQuantityReceived())==Integer.parseInt(inboundChildEntitiesFilter.get(i).getTotQuantity())){
                   inboundChildEntitiesFilter.get(i).setStatus(1);
                    inboundChildEntitiesFilter.get(i).setQtyRecv(false);
                    Collections.sort(inboundChildEntitiesFilter);
                }else{
                    inboundChildEntitiesFilter.get(i).setQtyRecv(true);
                    inboundChildEntity=inboundChildEntitiesFilter.get(i);
                    inboundChildEntitiesFilter.remove(i);
                    inboundChildEntitiesFilter.add(0,inboundChildEntity);
                }

                break;
            }
        }
        setInboundChildView(inboundChildEntitiesFilter);
    }
    //Visible Complete Order Button
    private void visibleCompleteOrderButton(){
        if(!isQuantityRecv) {
            completeOrderBtn.setVisibility(View.INVISIBLE);
            completeOrderBtn.setBackgroundColor(Color.parseColor("#c0c2c2"));
        }
    }
    //Inbound UPC,SKU Text Field Focus
    private void inboundUpcSkuTextFieldFocus(){
        upcSkutext.setText("");
        upcSkutext.requestFocus();
    }

    //Confirm Exit
    private void confirmExit(){
        screenExitbuilder = new AlertDialog.Builder(InboundAllActivity.this);
        screenExitbuilder.setMessage("Do you want to exit from this screen");
    }

    //Outbound Refresh Page
    private Boolean inboundRefreshPage() {
        System.gc();
        Intent refresh = new Intent(this, InboundAllActivity.class);
        startActivity(refresh);//Start the same Activity
        finish();
        return true;
    }
    //Back Button Common Feature
    private void backButtonCommonFeature(){
        backToOriginalPage();
        resetForAllFunction();
        System.gc();
    }
    //GO Today Button
    private void goTodayButton(){
        headingtext.setText("Today");
        inboundToday.setBackgroundDrawable(getResources().getDrawable(R.drawable.heading_button_border));
        inboundToRecv.setBackgroundColor(Color.WHITE);
        inboundRecv.setBackgroundColor(Color.WHITE);
        defaultInbound="Today";
        listViewMakeBlank();
        hideChildLayout();
        loadingGifEnable();
        resetForAllFunction();
        getInboundHeaderPage();
        System.gc();
    }
    //GO ToRECV Button
    private void goToRecieveButton(){
        headingtext.setText("To Receive");
        inboundToRecv.setBackgroundDrawable(getResources().getDrawable(R.drawable.heading_button_border));
        inboundRecv.setBackgroundColor(Color.WHITE);
        inboundToday.setBackgroundColor(Color.WHITE);
        defaultInbound="ToReceive";
        listViewMakeBlank();
        hideChildLayout();
        loadingGifEnable();
        resetForAllFunction();
        getInboundHeaderPage();
        System.gc();
    }
    //Go Recieved Button
    private void goRecievedButton(){
        headingtext.setText("Received");
        inboundRecv.setBackgroundDrawable(getResources().getDrawable(R.drawable.heading_button_border));
        inboundToRecv.setBackgroundColor(Color.WHITE);
        inboundToday.setBackgroundColor(Color.WHITE);
        defaultInbound="Received";
        listViewMakeBlank();
        hideChildLayout();
        loadingGifEnable();
        resetForAllFunction();
        getInboundHeaderPage();
        System.gc();
    }
    //Go to Home Page
    private void goToHomePage(){
        System.gc();
        Intent backToHome = new Intent(this, HomeActivity.class);
        //startActivityForResult(backToHome,1);
        startActivity(backToHome);//Start the Home Activity
        finish();
    }


    //Loading Check For Focus
    public class AsynLoading extends AsyncTask<String, String, Boolean> {
        @Override
        protected Boolean doInBackground(String... params) {
            AsynLoadingService asynLoadingService=new AsynLoadingService();
            return asynLoadingService.getLoadingStatus();
        }

        @Override
        protected void onPostExecute(Boolean status) {
            try {
                inboundPopUpFocus();
                isFirstTimePopOpen=false;
                afterComleteInboundPopup=false;
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
}
