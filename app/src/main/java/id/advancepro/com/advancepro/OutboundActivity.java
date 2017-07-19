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
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import id.advancepro.com.advancepro.adapter.OutboundChildAdapter;
import id.advancepro.com.advancepro.adapter.OutboundHeaderDetailsAdapter;
import id.advancepro.com.advancepro.adapter.ResetForAllAdapter;
import id.advancepro.com.advancepro.adapter.VibratorAdaptor;
import id.advancepro.com.advancepro.model.BoundSettingsEntity;
import id.advancepro.com.advancepro.model.OutboundChildProductLotSerialCheckEntity;
import id.advancepro.com.advancepro.model.OutboundHeaderDetailsEntity;
import id.advancepro.com.advancepro.model.OutboundLotSerialCheckEntity;
import id.advancepro.com.advancepro.model.OutboundOrderHeaderEntity;
import id.advancepro.com.advancepro.model.OutboundOrderProductQuantityCheckEntity;
import id.advancepro.com.advancepro.model.OutboundPOChildDetilsEntity;
import id.advancepro.com.advancepro.service.AsynLoadingService;
import id.advancepro.com.advancepro.service.BoundSettingsService;
import id.advancepro.com.advancepro.service.InternetConnection;
import id.advancepro.com.advancepro.service.OutboundChildProductLotSerialCheckService;
import id.advancepro.com.advancepro.service.OutboundHeaderDetailsService;
import id.advancepro.com.advancepro.service.OutboundOrderCompleteService;
import id.advancepro.com.advancepro.service.OutboundOrderHeaderService;
import id.advancepro.com.advancepro.service.OutboundOrderProductQuantityCheckService;
import id.advancepro.com.advancepro.service.OutboundPOChildDetailService;
import id.advancepro.com.advancepro.service.OutboundSerialLotFocusOutCheckService;
import id.advancepro.com.advancepro.service.ResetOutboundSerialService;
import id.advancepro.com.advancepro.utils.Constant;

public class OutboundActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private InternetConnection intConn;
    private SharedPreferences sharedpreferences;
    private String makeUrl;
    private String passCode;
    private String service_url;
    private String companyDBName;
    private Integer userID;
    private Integer warehouseID;
    private Integer outboundSettingsID;
    private Boolean checkInternet=false;
    private Button pickingbtn;
    private Button packingbtn;
    private String defaultOutbound;
    private BoundSettingsEntity outboundSettingsEntity;
    private Integer defaultOutboundVal=0;
    private ProgressBar outboundLoadingImage;
    private OutboundHeaderDetailsAdapter outboundHeaderDetailsAdapter;
    private OutboundChildAdapter outboundChildAdapter;
    private List<OutboundHeaderDetailsEntity> outboundHeaderDetailsEntityListFilter;
    private List<OutboundHeaderDetailsEntity> outboundHeaderDetailsEntityList;
    private ListView outboundListView;
    private EditText outboundPoFilterTxt;
    private EditText outboundUpcSkuSearchTxt;
    private LinearLayout outboundLabelLayout;
    private LinearLayout outboundCompleteOrderBtnLayout;
    private LinearLayout outboundUpcSkuLayout;
    private LinearLayout outboundPoFilterLayout;
    private TextView outboundHeadingPotxt;
    private TextView outboundHeadingQtytxt;
    private TextView outboundHeaddingCompanyNametxt;
    private ImageView outboundChildBackToParent;
    private Boolean outboundListViewTouch;
    private Boolean outboundChildListItem;
    private OutboundOrderHeaderEntity outboundOrderHeaderEntityRef;
    private Integer orderId;
    private Integer outboundQtyRecieved;
    private List<OutboundPOChildDetilsEntity> outboundPOChildDetilsEntityListFilter;
    private List<OutboundPOChildDetilsEntity> outboundPOChildDetilsEntityList;
    private Boolean outboundPoSubmit;
    private Boolean outboundChildSubmit;
    //private OutboundChildProductLotSerialCheckEntity outboundChildProductLotSerialCheckEntityRef;
    private String jsonString;
    private JSONArray outboundSubmitArray;
    private String pickingItemQty;
    private OutboundPOChildDetilsEntity outboundPOChildDetilsEntity;
    private Boolean outboundChildqtyRecv;
    private Button outboundCompleteOrder;
    private String statusOutbound;
    private Integer postOutboundMenuId;
    private LinearLayout upcSkuLock;
    private LinearLayout upcSkuUnLock;
    private View outboundPopUpView;
    private Boolean shouldOpenOutboundPopUp;
    private TextView outboundPopUpHeading;
    private TextView outboundPopUpPickLocation;
    private TextView outboundPopUpProductname;
    private EditText outboundPopUpSerial;
    private EditText outboundPopUpLot;
    private EditText outboundPopUpQty;
    private Button outboundPopUpSubmit;
    private Button outboundPopUpClose;
    private static View cView;
    private VibratorAdaptor vibratorAdaptor;
    private AlertDialog.Builder screenExitbuilder;
    private AlertDialog dialogOutboundPopUP;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_outbound);
            //SharedPreferences Value
            sharedpreferences = getSharedPreferences(Constant.LOGIN_PREF, Context.MODE_PRIVATE);
            passCode = sharedpreferences.getString(Constant.PASS_CODE, null);
            service_url = sharedpreferences.getString(Constant.SERVICE_URL, null);
            companyDBName = sharedpreferences.getString(Constant.LOGIN_DB, null);
            userID = Integer.parseInt(sharedpreferences.getString(Constant.LOGIN_USERID, "0"));
            warehouseID = Integer.parseInt(sharedpreferences.getString(Constant.DEFAULT_WAREHOUSE_ID, "0"));
            outboundSettingsID = Integer.parseInt(sharedpreferences.getString(Constant.OUTBOUND_SETTING_ID, "0"));
            intConn = new InternetConnection();//Internet Connection Initialize
            defaultOutbound=Constant.OUTBOUND_PICKING; // Default Outbound

            toolbar = (Toolbar) findViewById(R.id.menutoolbaroutbound);
            toolbar.setTitle("Outbound");
            setSupportActionBar(toolbar);

            confirmExit();// Confirm Exit Alert Dialogue
            //Vibrator Initialization
            vibratorAdaptor=new VibratorAdaptor(getApplicationContext());
            //Outbound Filter and Sku,Upc Edit text Field
            outboundPoFilterTxt=(EditText)findViewById(R.id.outboundfiltertxt);//Outbound PO Search Text Field
            outboundUpcSkuSearchTxt=(EditText)findViewById(R.id.outboundupcskutxt); // Outbound PO Child Item Search Text Field

            //Picking and Pack Button
            pickingbtn = (Button) findViewById(R.id.pickingbutton);//Outbound Picking Button
            packingbtn = (Button) findViewById(R.id.packingbutton); // Outbound Packing Button
            //Progressbar Loading Image
            outboundLoadingImage=(ProgressBar)findViewById(R.id.outboundprogressbar) ;
            //ListView
            outboundListView=(ListView)findViewById(R.id.outboundlistview);
            //Layout
            outboundLabelLayout=(LinearLayout)findViewById(R.id.outboundlabellayout);
            outboundCompleteOrderBtnLayout=(LinearLayout)findViewById(R.id.outboundcompleteorderbtnlayout);
            outboundUpcSkuLayout=(LinearLayout)findViewById(R.id.outboundupcskulayout);
            outboundPoFilterLayout=(LinearLayout)findViewById(R.id.outboundfilterlayout);

            //Outbound Child textview Element
            outboundHeadingPotxt=(TextView)findViewById(R.id.outboundheadingpo);
            outboundHeadingQtytxt=(TextView)findViewById(R.id.outboundheadinqty);
            outboundHeaddingCompanyNametxt=(TextView)findViewById(R.id.outboundheadingcompanyname);
            //Back Button Imageviw
            outboundChildBackToParent=(ImageView)findViewById(R.id.outboundbackimageView);
            //Outbound Complete Order Button
            outboundCompleteOrder=(Button)findViewById(R.id.outboundcompletebtn) ;
            //UPC , SKU Lock and Unlock Image
            upcSkuLock=(LinearLayout)findViewById(R.id.outboundupcskulock);
            upcSkuUnLock=(LinearLayout)findViewById(R.id.outboundupcskuunlock);
            //Reset Some value from specific table
            resetForAllFunction();
            //Get Outbound Settings
            getOutboundSetting();
            // Call common Feature of pick and pack button
            commonFeaturePickPackBtnClick();
            //Default Pick Button Background
            pickingbtn.setBackgroundDrawable(getResources().getDrawable(R.drawable.heading_button_border));//When Come to Outbound screen change Picking Button Background layout
            packingbtn.setBackgroundColor(Color.WHITE);
            //Picking Button click Event
            pickingbtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(outboundChildqtyRecv){
                        screenExitbuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                goPicking();
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
                        goPicking();

                    }
                }
            });
            // Pack Button click Event
            packingbtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(outboundChildqtyRecv){
                        screenExitbuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                goPacking();
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
                        goPacking();

                    }

                }
            });
            //Outbound PO Filter text Click Event
            outboundPoFilterTxt.setOnKeyListener(new View.OnKeyListener() {
                @Override
                public boolean onKey(View v, int keyCode, KeyEvent event) {
                    if(!StringUtils.isEmpty(outboundPoFilterTxt.getText())){
                        //KeyDown
                        if(keyCode==66 && event.getAction()==KeyEvent.ACTION_DOWN){
                            outboundPoSubmit=true;
                        }
                        //KeyUp
                        if(keyCode==66 && event.getAction()==KeyEvent.ACTION_UP){
                            getOutboundPoFilterData(outboundHeaderDetailsEntityList,1); // 1 for scan Enter event
                            if(outboundHeaderDetailsEntityListFilter.size()>0){
                                if(outboundHeaderDetailsEntityListFilter.size()==1){
                                    visibleLayout();
                                    makeOutboundListViewBlank();
                                    loadingGifEnable();
                                    getOutboundOrderHeader(outboundHeaderDetailsEntityListFilter.get(0).getOrderNo());//Get Outbound header order details
                                    getOutboundChildDetails(outboundHeaderDetailsEntityListFilter.get(0).getOrderNo()); //get outbound PO Child Details
                                    if(Integer.parseInt(outboundHeaderDetailsEntityListFilter.get(0).getStatusID())==55){
                                        postOutboundMenuId=56; //Picking
                                    }else if(Integer.parseInt(outboundHeaderDetailsEntityListFilter.get(0).getStatusID())==56){
                                        postOutboundMenuId=57; // Packing
                                    }
                                }else{

                                }
                            }else{
                                Toast.makeText(getApplicationContext(), Constant.EMPTY_DATA, Toast.LENGTH_SHORT).show();
                                vibratorAdaptor.makeVibration();
                            }
                            outboundPoFilterTxt.setText("");
                            outboundPoSubmit=false;
                        }
                    }
                    return false;
                }
            });
            //Edit
            outboundPoFilterTxt.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                    if(actionId == EditorInfo.IME_ACTION_DONE){

                    }
                    return true;
                }
            });
            //On Text change
            outboundPoFilterTxt.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if(!outboundPoSubmit && !StringUtils.isEmpty(outboundPoFilterTxt.getText())){
                        getOutboundPoFilterData(outboundHeaderDetailsEntityList,0); // O is for on text change
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });

            //Outbound PO Child item SKU,UPC Search Key Listener
            outboundUpcSkuSearchTxt.setOnKeyListener(new View.OnKeyListener() {
                @Override
                public boolean onKey(View v, int keyCode, KeyEvent event) {
                    //v.playSoundEffect(SoundEffectConstants.CLICK);
                    if(!StringUtils.isEmpty(outboundUpcSkuSearchTxt.getText())){
                        if(keyCode==66 && event.getAction()==KeyEvent.ACTION_DOWN) {
                            outboundChildSubmit=true;
                        }
                        if(keyCode==66 && event.getAction()==KeyEvent.ACTION_UP){
                            getOutboundChildFilterData(outboundPOChildDetilsEntityList,1); // 1 for Scan Key up
                            if(outboundPOChildDetilsEntityListFilter.size()==1){
                                if(outboundPOChildDetilsEntityListFilter.get(0).getStatus()==0) { // 0 for Can Ship Quantity
                                    //System.out.println("bb");
                                    if(shouldOpenOutboundPopUp){
                                        //outboundPOChildDetilsEntityListFilter.get(0).getLocationName()..
                                        //outboundPOChildDetilsEntityListFilter.get(0).getProductPickingLocId()..
                                        openOutboundPopUp(outboundPOChildDetilsEntityListFilter.get(0).getOrderDetailId(),outboundPOChildDetilsEntityListFilter.get(0).getLocationName());//Open Outbound PopUp
                                    }else{
                                        if(Integer.parseInt(outboundPOChildDetilsEntityListFilter.get(0).getIslotserial())==1){
                                            Toast.makeText(getApplicationContext(), "Lot/Serial Required", Toast.LENGTH_SHORT).show();
                                            vibratorAdaptor.makeVibration();
                                            setOutboundChildView(outboundPOChildDetilsEntityList);
                                        }else {
                                            //Filter the  Outbound PO Child Item List based on SKU/UPC and Picking Location
                                            outboundChildQtyAdd(outboundPOChildDetilsEntityListFilter.get(0).getOrderDetailId(),outboundPOChildDetilsEntityListFilter.get(0).getLocationName());
                                        }
                                    }
                                }else{
                                    richLimitMessage();// Already Fully received Quantity message
                                }
                            }else if(outboundPOChildDetilsEntityListFilter.size()> 1) { //If Item found Same SKU/UPC but  2 Different Picking Location
                                //if(outboundPOChildDetilsEntityListFilter.get(0).getStatus()!=0) { // 0 for Can Ship Quantity
                                   // richLimitMessage();
                               // }
                            }
                            else{
                                Toast.makeText(getApplicationContext(), Constant.EMPTY_DATA, Toast.LENGTH_SHORT).show();
                                vibratorAdaptor.makeVibration();
                            }
                            outboundUpcSkuSearchTxt.setText("");
                            outboundChildSubmit=false;
                            outboundUpcSkuSearchTxt.requestFocus();
                        }
                    }
                    return false;
                }
            });
            //Edit
            outboundUpcSkuSearchTxt.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                    if(actionId == EditorInfo.IME_ACTION_DONE){

                    }
                    return true;
                }
            });
            //On Text Change
            outboundUpcSkuSearchTxt.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if(!outboundChildSubmit && !StringUtils.isEmpty(outboundUpcSkuSearchTxt.getText())){
                        getOutboundChildFilterData(outboundPOChildDetilsEntityList,0); // O for Text change
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {


                }
            });

            //Outbound ListView On Touch
            outboundListView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    outboundListViewTouch=true;
                    return false;
                }
            });
            //Outbound ListView Item Click
            outboundListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    if(outboundListViewTouch && !outboundChildListItem){
                        String orderNO = ((TextView) (view.findViewById(R.id.outboundorderno))).getText().toString();
                        visibleLayout();
                        makeOutboundListViewBlank();
                        loadingGifEnable();
                        getOutboundOrderHeader(orderNO);
                        getOutboundChildDetails(orderNO);
                        if(Integer.parseInt(((TextView) (view.findViewById(R.id.outboundstatusid))).getText().toString())==55){
                            postOutboundMenuId=56;//Picking
                        }else if(Integer.parseInt(((TextView) (view.findViewById(R.id.outboundstatusid))).getText().toString())==56){
                            postOutboundMenuId=57; //Packing
                        }
                    }else if(outboundListViewTouch && outboundChildListItem){
                        String status=((TextView) (view.findViewById(R.id.outboundchildproductstatus))).getText().toString();
                        if(Integer.parseInt(status)==0){
                            String orderDetailID=((TextView) (view.findViewById(R.id.hiddenorderdetailid))).getText().toString();
                            String picLocationName=((TextView) (view.findViewById(R.id.outboundchildproductlocation))).getText().toString();
                            if(shouldOpenOutboundPopUp){
                                openOutboundPopUp(orderDetailID,picLocationName);
                            }else{
                                Boolean isLotSerialReq=false;
                                for(int i=0;i<outboundPOChildDetilsEntityList.size();i++){
                                    if(outboundPOChildDetilsEntityList.get(i).getOrderDetailId().equals(orderDetailID) && outboundPOChildDetilsEntityList.get(i).getLocationName().equals(picLocationName)
                                            && (Integer.parseInt(outboundPOChildDetilsEntityList.get(i).getIslotserial())==1)){
                                        isLotSerialReq=true;
                                    }
                                }
                                if(isLotSerialReq){
                                    Toast.makeText(getApplicationContext(), "Lot/Serial Required", Toast.LENGTH_SHORT).show();
                                    vibratorAdaptor.makeVibration();
                                }else {
                                    outboundChildQtyAdd(orderDetailID,picLocationName);
                                }
                            }
                        }
                    }
                    outboundListViewTouch=false;
                }
            });
            //Custom Back Button click Event
            outboundChildBackToParent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(outboundChildqtyRecv){
                        screenExitbuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                /*commonFeaturePickPackBtnClick();
                                resetForAllFunction();
                                getOutboundHeaderPage();*/
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
                        commonFeaturePickPackBtnClick(); // Common Feature for Pick and Pack Button Click
                        resetForAllFunction(); // Clear some table with Web Service Call
                        getOutboundHeaderPage();// Get Outbound PO List
                    }
                }
            });
            //Outbound Complete Order Button Click
            outboundCompleteOrder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(defaultOutbound.equals(Constant.OUTBOUND_PICKING)) {
                        if(outboundQtyRecieved < Integer.parseInt(outboundOrderHeaderEntityRef.getTotQuantity())) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(OutboundActivity.this);
                            builder.setMessage("Are you sure you want to pick partially?");
                            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    selectPickOrPickPack();

                                }
                            });
                            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                            builder.show();
                        }else{
                            selectPickOrPickPack();
                        }

                    }else if(defaultOutbound.equals(Constant.OUTBOUND_PACKING)){
                        resetOutboundSerialNumber();
                    }
                }
            });
            //UPC , SKU Lock and Unlock Event
            // Lock Event Listener
            upcSkuLock.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    upcSkuUnLock.setVisibility(View.VISIBLE);
                    upcSkuLock.setVisibility(View.GONE);
                    SharedPreferences.Editor editor = sharedpreferences.edit();
                    editor.putString(Constant.OUTBOUND_UPC_SKU_TEXT_LOCK, Constant.LOCK_TEXT);
                    editor.commit();
                    shouldOpenOutboundPopUp=false;
                }
            });
            //UnLock Event Listener
            upcSkuUnLock.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    upcSkuLock.setVisibility(View.VISIBLE);
                    upcSkuUnLock.setVisibility(View.GONE);
                    SharedPreferences.Editor editor = sharedpreferences.edit();
                    editor.putString(Constant.OUTBOUND_UPC_SKU_TEXT_LOCK, Constant.UNLOCK_TEXT);
                    editor.commit();
                    shouldOpenOutboundPopUp=true;
                }
            });
        }catch (Exception ex){
            ex.printStackTrace();
        }

    }
    //Device Back Button Pressed
    @Override
    public void onBackPressed() {
        if(outboundChildListItem){
            if(outboundChildqtyRecv) {
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
                backButtonCommonFeature(); // Common feature for back Button
            }
        }else{
            goToHomePage();// Go back to home page
        }
    }
    //App Stop
    @Override
    protected void onStop()
    {
        super.onStop();
        if(outboundChildListItem){
            if(outboundChildqtyRecv) {
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
                backButtonCommonFeature();
            }
        }
    }
    //Select Pick or Pick & Pack Dialougue
    private void selectPickOrPickPack(){
        AlertDialog.Builder builder = new AlertDialog.Builder(OutboundActivity.this);
        builder.setMessage("Select any option");
        builder.setPositiveButton("Pick", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                completeOutboundOrder(Constant.OUTBOUND_PICKED);
                dialog.dismiss();
            }
        });
        builder.setNegativeButton("Pick & Pack", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                completeOutboundOrder(Constant.OUTBOUND_PICKED_PACKED);
                dialog.dismiss();
            }
        });
        builder.show();
    }
    //Outbound  Create Options menu
    public boolean onCreateOptionsMenu(Menu menu){
        try {
            MenuInflater menuInflater=getMenuInflater();
            menuInflater.inflate(R.menu.menu_outbound,menu);
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return super.onCreateOptionsMenu(menu);
    }

    //Outbound Options Item Selected
    public boolean onOptionsItemSelected(MenuItem menuItem){
        try {
            if (menuItem.getItemId() == R.id.outbound_refresh) {
                if(outboundChildqtyRecv){
                    screenExitbuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            outboundRefreshPage();
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
                    outboundRefreshPage();
                }
            }else if(menuItem.getItemId() == R.id.outbound_backto_home) {
                if(outboundChildListItem){
                    if(outboundChildqtyRecv) {
                        screenExitbuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                backButtonCommonFeature();// Common feature for back button
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
                        backButtonCommonFeature(); // Common feature For back Button
                    }
                }else {
                    goToHomePage(); // Go back to Home page
                    return true;
                }
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return super.onOptionsItemSelected(menuItem);
    }
    //Get Outbound PO Filter Data
    private  void getOutboundPoFilterData(List<OutboundHeaderDetailsEntity> outboundHeaderDetailsEntities,Integer type){
        try{
            if(!StringUtils.isEmpty(outboundPoFilterTxt.getText())) {
                outboundHeaderDetailsEntityListFilter = new ArrayList<>();
                String outboundPoFiltertxtData = outboundPoFilterTxt.getText().toString().toLowerCase();
                outboundPoFiltertxtData = outboundPoFiltertxtData.replace(" ", "");
                for (OutboundHeaderDetailsEntity outboundHeaderDetailsEntity : outboundHeaderDetailsEntities) {
                    String outboundPoNumber = outboundHeaderDetailsEntity.getPoNo().toLowerCase();
                    outboundPoNumber = outboundPoNumber.replace(" ", "");
                    if (type == 1 && outboundPoNumber.equalsIgnoreCase(outboundPoFiltertxtData)) {
                        outboundHeaderDetailsEntityListFilter.add(outboundHeaderDetailsEntity);
                        //break;
                    } else if (type == 0 && outboundPoNumber.contains(outboundPoFiltertxtData)) {
                        outboundHeaderDetailsEntityListFilter.add(outboundHeaderDetailsEntity);
                    }
                }
                if(outboundHeaderDetailsEntityListFilter!=null && outboundHeaderDetailsEntityListFilter.size()>0) {
                    setListView(outboundHeaderDetailsEntityListFilter);
                }else {
                    setListView(outboundHeaderDetailsEntityList);
                }
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    //Get Outbound PO Child List Filter Data
    private void getOutboundChildFilterData(List<OutboundPOChildDetilsEntity> outboundPOChildDetilsEntities,Integer type){
        try {
            if(!StringUtils.isEmpty(outboundUpcSkuSearchTxt.getText())) {
                outboundPOChildDetilsEntityListFilter = new ArrayList<>();
                String outboundUpcSkuFilter = outboundUpcSkuSearchTxt.getText().toString().toLowerCase();
                outboundUpcSkuFilter = outboundUpcSkuFilter.replace(" ", "");
                for (OutboundPOChildDetilsEntity outboundPOChildDetilsEntity : outboundPOChildDetilsEntities) {
                    String outboundChildSku = outboundPOChildDetilsEntity.getProductSKU().toLowerCase();
                    outboundChildSku = outboundChildSku.replace(" ", "");
                    String outboundChildUpc = outboundPOChildDetilsEntity.getProductUPC().toLowerCase();
                    outboundChildUpc = outboundChildUpc.replace(" ", "");
                    if (type == 1 && (outboundChildSku.equalsIgnoreCase(outboundUpcSkuFilter) || outboundChildUpc.equalsIgnoreCase(outboundUpcSkuFilter))) {
                        outboundPOChildDetilsEntityListFilter.add(outboundPOChildDetilsEntity);
                        //break;
                    } else if (type == 0 && (outboundChildSku.contains(outboundUpcSkuFilter) || outboundChildUpc.contains(outboundUpcSkuFilter))) {
                        outboundPOChildDetilsEntityListFilter.add(outboundPOChildDetilsEntity);
                    }
                }
                if(outboundPOChildDetilsEntityListFilter!=null && outboundPOChildDetilsEntityListFilter.size()>0) {
                    setOutboundChildView(outboundPOChildDetilsEntityListFilter);
                }else{
                    setOutboundChildView(outboundPOChildDetilsEntityList);
                }
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }
    //Outbound Child Quantity Add
    private void outboundChildQtyAdd(String orderDetailId,String pickLocationName){
        try {
            viewOutboundCompleteOrderButton();
            for(int i=0;i<outboundPOChildDetilsEntityList.size();i++) {
                //System.out.println(outboundPOChildDetilsEntityList.get(i).getOrderDetailId()+"----"+(orderDetailId)+"---ss--"+outboundPOChildDetilsEntityList.get(i).getLocationName()+"----"+(pickLocationName));
                //System.out.println(outboundPOChildDetilsEntityList.get(i).getOrderDetailId().equals(orderDetailId)+"---ss--"+outboundPOChildDetilsEntityList.get(i).getLocationName().equals(pickLocationName));
                if((outboundPOChildDetilsEntityList.get(i).getOrderDetailId().equals(orderDetailId) && outboundPOChildDetilsEntityList.get(i).getLocationName().equals(pickLocationName))
                        && (Integer.parseInt(outboundPOChildDetilsEntityList.get(i).getQuantity()) >Integer.parseInt(outboundPOChildDetilsEntityList.get(i).getQuantityRecd()))){
                    //Outbound Child Quantity Heading Set
                    //System.out.println("ss");
                    outboundQtyRecieved = outboundQtyRecieved + 1;
                    outboundHeadingQtytxt.setText(outboundQtyRecieved.toString() + "/" + outboundOrderHeaderEntityRef.getTotQuantity());
                    if(outboundQtyRecieved==Integer.parseInt(outboundOrderHeaderEntityRef.getTotQuantity())){
                        outboundCompleteOrder.setBackgroundColor(Color.parseColor("#264d44"));
                    }
                    //Outbound Child List Quantity Recieve Set
                    Integer setQtyRecieved=Integer.parseInt(outboundPOChildDetilsEntityList.get(i).getQuantityRecd())+1;
                    System.out.println(setQtyRecieved);
                    outboundPOChildDetilsEntityList.get(i).setQuantityRecd(setQtyRecieved.toString());
                    //System.out.println(outboundPOChildDetilsEntityList.get(i).getQuantityRecd());
                    pickingItemQty="1";
                    //System.out.println(outboundPOChildDetilsEntity.getQuantityRecd());
                    //check Recieved and Total Quantity
                    if(Integer.parseInt(outboundPOChildDetilsEntityList.get(i).getQuantity()) ==Integer.parseInt(outboundPOChildDetilsEntityList.get(i).getQuantityRecd())){
                        outboundPOChildDetilsEntityList.get(i).setStatus(1);
                        outboundPOChildDetilsEntityList.get(i).setQtyInMiddle(false);
                        outboundPOChildDetilsEntity=outboundPOChildDetilsEntityList.get(i);
                        Collections.sort(outboundPOChildDetilsEntityList);
                    }else{
                        outboundPOChildDetilsEntityList.get(i).setQtyInMiddle(true);
                        outboundPOChildDetilsEntity=outboundPOChildDetilsEntityList.get(i);
                    }

                    if(outboundPOChildDetilsEntityList.get(i).getQtyInMiddle()){//Inbetween Pick /Paclk
                        outboundPOChildDetilsEntityList.remove(i);
                        outboundPOChildDetilsEntityList.add(0,outboundPOChildDetilsEntity);
                    }
                    //System.out.println("Error");
                    checkOutBoundOrderQtytBoundQty(pickingItemQty,outboundPOChildDetilsEntity);
                    break;
                }else if(outboundPOChildDetilsEntityList.get(i).getOrderDetailId().equals(orderDetailId)  && outboundPOChildDetilsEntityList.get(i).getLocationName().equals(pickLocationName)
                        && (Integer.parseInt(outboundPOChildDetilsEntityList.get(i).getQuantity()) ==Integer.parseInt(outboundPOChildDetilsEntityList.get(i).getQuantityRecd()))){
                    Toast.makeText(getApplicationContext(), Constant.RICH_LIMIT, Toast.LENGTH_SHORT).show();
                    vibratorAdaptor.makeVibration();
                    break;
                }
            }
            outboundListViewTouch=false;
            setOutboundChildView(outboundPOChildDetilsEntityList);
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    //Get Outbound Settings (Web Service Call)
    private void getOutboundSetting(){
        try{
            checkInternet=intConn.checkConnection(this); // Internet connection check
            if(checkInternet) {
                makeUrl = service_url + "/GetBoundSettings/" + companyDBName + "/" + userID + "/" + outboundSettingsID + "/" + passCode;
                new OutboundSetting().execute(makeUrl);
            }else {
                internetConnectionMessage();
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    public class OutboundSetting extends AsyncTask<String, String, BoundSettingsEntity> {
        @Override
        protected BoundSettingsEntity doInBackground (String...params){
            BoundSettingsService boundSettingsService = new BoundSettingsService();
            return boundSettingsService.getInboundSettingsList(params[0]);
        }

        @Override
        protected void onPostExecute (BoundSettingsEntity boundSettingsEntityRef){
            try {
                outboundSettingsEntity= boundSettingsEntityRef;
                getOutboundHeaderPage();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
    // Get outbound PO List (Web Service call)
    private void getOutboundHeaderPage(){
        try {
            //System.out.println(defaultInbound);
            if(defaultOutbound.equals(Constant.OUTBOUND_PICKING)){
                defaultOutboundVal=Constant.PICKING_OUTBOUND;
            }else if(defaultOutbound.equals(Constant.OUTBOUND_PACKING)){
                defaultOutboundVal=Constant.PACKING_OUTBOUND;
            }
            makeUrl = service_url +"/GetOutBoundHeaderDetails/"+ companyDBName + "/" + userID + "/" + defaultOutboundVal + "/" + warehouseID + "/" + Constant.RECORD_PER_PAGE + "/" + Constant.INDEX_PAGE + "/" + passCode;
            new OutboundHeaderDetails().execute(makeUrl);
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    public class OutboundHeaderDetails extends AsyncTask<String, String, List<OutboundHeaderDetailsEntity>> {
        @Override
        protected List<OutboundHeaderDetailsEntity> doInBackground(String... params) {
            OutboundHeaderDetailsService outboundHeaderDetailsService=new OutboundHeaderDetailsService();
            outboundHeaderDetailsService.initalValue(service_url,passCode,userID,companyDBName,warehouseID,Constant.INDEX_PAGE,defaultOutboundVal,outboundSettingsEntity);
            return outboundHeaderDetailsService.getOutboundHeaderDetails(params[0]);
        }

        @Override
        protected void onPostExecute(List<OutboundHeaderDetailsEntity> outboundHeaderDetailsEntities) {
            try {
                hideChildLayout();//hide Outbound PO Child layout
                loadingGifDisable();
                outboundChildListItem=false;
                outboundPoSubmit=false;
                outboundChildqtyRecv=false;
                outboundPOChildDetilsEntity=null;
                if(outboundHeaderDetailsEntities!=null && outboundHeaderDetailsEntities.size()>0) {
                    setListView(outboundHeaderDetailsEntities);
                    outboundHeaderDetailsEntityList = outboundHeaderDetailsEntities;
                }else{
                    outboundListView.setAdapter(null);
                    Toast.makeText(getApplicationContext(), Constant.EMPTY_DATA, Toast.LENGTH_SHORT).show();
                    vibratorAdaptor.makeVibration();
                }

            }catch (Exception ex){
                ex.printStackTrace();
            }
            //System.out.println(childStatus);
        }
    }
    //Outbound PO List View
    private void setListView(List<OutboundHeaderDetailsEntity> outboundHeaderDetailsEntityList){
        try {
            outboundHeaderDetailsAdapter = new OutboundHeaderDetailsAdapter(getApplicationContext(), R.layout.outboundparentlayout, outboundHeaderDetailsEntityList, outboundSettingsEntity);
            outboundListView.setAdapter(outboundHeaderDetailsAdapter);
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }
    // Get Outbound Order Header
    private void getOutboundOrderHeader(String orderNo){
        try{
            makeUrl=service_url + "/GetOutBoundOrderHeader/" + companyDBName + "/" + userID + "/" + defaultOutboundVal + "/" + orderNo + "/" + passCode ;
            new OutboundOrderHeader().execute(makeUrl);
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    //Outbound Order Header Service
    public class OutboundOrderHeader extends AsyncTask<String, String, OutboundOrderHeaderEntity> {
        @Override
        protected OutboundOrderHeaderEntity doInBackground(String... params) {
            OutboundOrderHeaderService outboundOrderHeaderService=new OutboundOrderHeaderService();
            return outboundOrderHeaderService.getOutboundOrderHeader(params[0]);
        }

        @Override
        protected void onPostExecute(OutboundOrderHeaderEntity outboundOrderHeaderEntity) {
            try {
                if(outboundOrderHeaderEntity!=null){
                    orderId=0;
                    outboundOrderHeaderEntityRef=outboundOrderHeaderEntity;
                    orderId=Integer.parseInt(outboundOrderHeaderEntity.getOrderID());
                    outboundHeadingPotxt.setText("P.O #"+outboundOrderHeaderEntity.getPurchaseno());
                    outboundHeadingQtytxt.setText(outboundOrderHeaderEntity.getQuantityReceived() +"/"+ outboundOrderHeaderEntity.getTotQuantity());
                    outboundHeaddingCompanyNametxt.setText(outboundOrderHeaderEntity.getCompanyName());
                    outboundQtyRecieved=0;
                    outboundQtyRecieved=outboundQtyRecieved+Integer.parseInt(outboundOrderHeaderEntity.getQuantityReceived());
                }else{
                    Toast.makeText(getApplicationContext(), Constant.SERVER_ERROR, Toast.LENGTH_SHORT).show();
                    vibratorAdaptor.makeVibration();
                }

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    //Get Outbound PO Child Details
    private void getOutboundChildDetails(String orderNo){
        try {
            makeUrl=service_url + "/GetOutBoundOrderChild/" + companyDBName + "/" + userID+ "/"  + defaultOutboundVal + "/" + orderNo + "/" + warehouseID + "/" + passCode ;
            //System.out.println(makeUrl);
            new OutboundPOChildDetails().execute(makeUrl);
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    //Outbound PO Child Details Service
    public class OutboundPOChildDetails extends AsyncTask<String, String, List<OutboundPOChildDetilsEntity>> {
        @Override
        protected List<OutboundPOChildDetilsEntity> doInBackground(String... params) {
            OutboundPOChildDetailService outboundPOChildDetailService=new OutboundPOChildDetailService();
            return outboundPOChildDetailService.getOutboundChildDetails(params[0]);
        }

        @Override
        protected void onPostExecute(List<OutboundPOChildDetilsEntity> outboundPOChildDetilsEntities) {
            try {
                if(outboundPOChildDetilsEntities!=null && outboundPOChildDetilsEntities.size()>0){
                    outboundPOChildDetilsEntityList=outboundPOChildDetilsEntities;
                }else{
                    Toast.makeText(getApplicationContext(), Constant.SERVER_ERROR, Toast.LENGTH_SHORT).show();
                    vibratorAdaptor.makeVibration();
                }
                loadingGifDisable();
                outboundSubmitArray=new JSONArray();
                outboundChildqtyRecv=false;
                outboundPOChildDetilsEntity=null;
                setOutboundChildView(outboundPOChildDetilsEntities);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    //Outbound Child List view
    private void setOutboundChildView(List<OutboundPOChildDetilsEntity> outboundPOChildDetilsEntityList){
        try {
            if(outboundPOChildDetilsEntityList!=null && outboundPOChildDetilsEntityList.size()>0) {
                outboundChildAdapter = new OutboundChildAdapter(getApplicationContext(), R.layout.outboundchildlayout, outboundPOChildDetilsEntityList);
                outboundListView.setAdapter(outboundChildAdapter);
                outboundChildListItem = true;
            }
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }
    // Check outbound Product Quantity
    private void checkOutBoundOrderQtytBoundQty(String qty,OutboundPOChildDetilsEntity outboundPOChildDetilsEntity){
        try{
            //System.out.println(outboundPOChildDetilsEntity.getQuantityRecd());
            if(Integer.parseInt(qty)==0){
                return;
            }else if(Integer.parseInt(outboundPOChildDetilsEntity.getQuantity())<Integer.parseInt(qty)){
                return;
            }
            JSONObject jsonObject=new JSONObject();
            jsonObject.put("CompanyDbName",companyDBName);
            jsonObject.put("Userid",userID);
            jsonObject.put("OrderDetailedid",outboundPOChildDetilsEntity.getOrderDetailId());
            jsonObject.put("Productid",outboundPOChildDetilsEntity.getProductID());
            jsonObject.put("Totalqty",outboundPOChildDetilsEntity.getQuantity());
            jsonObject.put("whid",warehouseID);
            jsonObject.put("Prodcutpicklocid",outboundPOChildDetilsEntity.getProductPickingLocId());
            jsonObject.put("Quantity",Integer.parseInt(qty));
            jsonObject.put("key",passCode);
            jsonString=jsonObject.toString();
            makeUrl=service_url + "/CheckOutBoundOrderQuantitytboundQuantity";
            new OutboundOrderProductQuantityCheck().execute(makeUrl);
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }
    //check OutBound Order product Quantity
    public class OutboundOrderProductQuantityCheck extends AsyncTask<String, String,OutboundOrderProductQuantityCheckEntity> {
        @Override
        protected OutboundOrderProductQuantityCheckEntity doInBackground(String... params) {
            OutboundOrderProductQuantityCheckService outboundOrderProductQuantityCheckService=new OutboundOrderProductQuantityCheckService();
            return outboundOrderProductQuantityCheckService.getOutboundProductQtyCheckResult(params[0],jsonString);
        }

        @Override
        protected void onPostExecute(OutboundOrderProductQuantityCheckEntity outboundOrderProductQuantityCheckEntity) {
            try {
                if(outboundOrderProductQuantityCheckEntity!=null){
                    if(Integer.parseInt(outboundOrderProductQuantityCheckEntity.getId())==1){
                        addPickingItemsToArray(pickingItemQty);
                    }else if(Integer.parseInt(outboundOrderProductQuantityCheckEntity.getId())==0){
                        //System.out.println(outboundPOChildDetilsEntity.getQuantityRecd()+"=="+outboundPOChildDetilsEntity.getQuantity());
                        Toast.makeText(getApplicationContext(), outboundOrderProductQuantityCheckEntity.getStatus(), Toast.LENGTH_SHORT).show();
                        vibratorAdaptor.makeVibration();
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    //Make Submit Array for Outbound Picking Item
    private void addPickingItemsToArray(String qty){
        try{

            JSONObject jsonObject=new JSONObject();
            jsonObject.put("OrderDetailId",outboundPOChildDetilsEntity.getOrderDetailId());
            jsonObject.put("ProductId",outboundPOChildDetilsEntity.getProductID());
            jsonObject.put("Picklocname",outboundPOChildDetilsEntity.getLocationName());
            jsonObject.put("Quantity",Integer.parseInt(qty));
            jsonObject.put("StatusId",postOutboundMenuId);
            //Serial
            if(outboundPopUpSerial==null){
                jsonObject.put("SerialNumber","");
            }else if(StringUtils.isEmpty(outboundPopUpSerial.getText())){
                jsonObject.put("SerialNumber","");
            } else{
                jsonObject.put("SerialNumber",outboundPopUpSerial.getText());
            }
            //Lot
            if(outboundPopUpLot==null){
                jsonObject.put("Lot","");
            }else if(StringUtils.isEmpty(outboundPopUpLot.getText())){
                jsonObject.put("Lot","");
            }
            else{
                jsonObject.put("Lot",outboundPopUpLot.getText());
            }

            jsonObject.put("CombID",outboundPOChildDetilsEntity.getCombid());
            jsonObject.put("Totalquantity",outboundPOChildDetilsEntity.getQuantity());
            jsonObject.put("LocationId",outboundPOChildDetilsEntity.getProductPickingLocId());
            outboundSubmitArray.put(jsonObject);
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }
    //Check outbound Product Lot and Serial
    private void checkOutboundProductLotAndSerial(String productID){
        try{
            makeUrl=service_url + "/CheckInBoundProductSerialLot/" + companyDBName + "/" + productID+ "/" + passCode;
            new OutboundProductLotAndSerialCheck().execute(makeUrl);
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }
    //Get optional Outbound Product Lot and Serial Check
    public class OutboundProductLotAndSerialCheck extends AsyncTask<String, String,OutboundChildProductLotSerialCheckEntity> {
        @Override
        protected OutboundChildProductLotSerialCheckEntity doInBackground(String... params) {
            OutboundChildProductLotSerialCheckService outboundChildProductLotSerialCheckService=new OutboundChildProductLotSerialCheckService();
            return outboundChildProductLotSerialCheckService.getLotSerial(params[0]);
        }

        @Override
        protected void onPostExecute(OutboundChildProductLotSerialCheckEntity outboundChildProductLotSerialCheckEntity) {
            try {
                if(outboundChildProductLotSerialCheckEntity!=null){
                    //outboundChildProductLotSerialCheckEntityRef=outboundChildProductLotSerialCheckEntity;
                    //POP UP Field
                    outboundPopUpView=getLayoutInflater().inflate(R.layout.outboundpopup,null);
                    outboundPopUpHeading=(TextView) outboundPopUpView.findViewById(R.id.outboundpopupheadding);
                    outboundPopUpPickLocation=(TextView) outboundPopUpView.findViewById(R.id.outboundpopuppiclock);
                    outboundPopUpProductname=(TextView) outboundPopUpView.findViewById(R.id.outboundpopupproductname);
                    outboundPopUpSerial=(EditText) outboundPopUpView.findViewById(R.id.outboundpopupserial);
                    outboundPopUpLot=(EditText) outboundPopUpView.findViewById(R.id.outboundpopuplot);
                    outboundPopUpQty=(EditText) outboundPopUpView.findViewById(R.id.outboundpopupqty);
                    outboundPopUpSubmit=(Button) outboundPopUpView.findViewById(R.id.outboundpopupsubmit);
                    outboundPopUpClose=(Button) outboundPopUpView.findViewById(R.id.outboundpopupclose);

                    cView=outboundPopUpView;//Current Outbound Popup View

                    AlertDialog.Builder builder = new AlertDialog.Builder(OutboundActivity.this);
                    builder.setView(outboundPopUpView);
                    dialogOutboundPopUP = builder.create();// Outbound Popup
                    dialogOutboundPopUP.setCanceledOnTouchOutside(false);
                    dialogOutboundPopUP.show();

                    outboundPopUpHeading.setText("Enter Serial & Lot");
                    outboundPopUpPickLocation.setText("Pick loc : "+outboundPOChildDetilsEntity.getLocationName());
                    outboundPopUpProductname.setText(outboundPOChildDetilsEntity.getProductName());
                    //Lot and Serial Disabled
                    if(Integer.parseInt(outboundChildProductLotSerialCheckEntity.getProductLot())==0 && Integer.parseInt(outboundChildProductLotSerialCheckEntity.getProductSerial())==0 ){
                        outboundPopUpSerial.setEnabled(false);
                        outboundPopUpLot.setEnabled(false);
                    }
                    //Lot Enable and Serial Disabled
                    if(Integer.parseInt(outboundChildProductLotSerialCheckEntity.getProductLot())==1 && Integer.parseInt(outboundChildProductLotSerialCheckEntity.getProductSerial())==0 ){
                        outboundPopUpSerial.setEnabled(false);
                        outboundPopUpLot.setEnabled(true);
                    }
                    //Lot Disabled and Serial Enabled
                    if(Integer.parseInt(outboundChildProductLotSerialCheckEntity.getProductLot())==0 && Integer.parseInt(outboundChildProductLotSerialCheckEntity.getProductSerial())==1 ){
                        outboundPopUpSerial.setEnabled(true);
                        outboundPopUpLot.setEnabled(false);
                    }
                    //Lot and Serial Enabled
                    if(Integer.parseInt(outboundChildProductLotSerialCheckEntity.getProductLot())==1 && Integer.parseInt(outboundChildProductLotSerialCheckEntity.getProductSerial())==1 ){
                        outboundPopUpSerial.setEnabled(true);
                        outboundPopUpLot.setEnabled(true);
                    }
                    //Pop up Focus
                    popUPNextFocus();
                    //outboundPopUpLot Focus Change
                    outboundPopUpLot.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                        @Override
                        public void onFocusChange(View v, boolean hasFocus) {
                            if(!hasFocus){
                                if(outboundPopUpLot.isEnabled() && !StringUtils.isEmpty(outboundPopUpLot.getText())){
                                    hideKeyboard(getApplicationContext());
                                    outboundCheckLotAndSerialFocusOut();
                                    //popUPNextFocus();
                                }
                                //System.out.println("lot");
                            }
                        }
                    });
                    //outboundPopUpSerial Focus Change
                    outboundPopUpSerial.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                        @Override
                        public void onFocusChange(View v, boolean hasFocus) {
                            if(!hasFocus){
                                if(outboundPopUpSerial.isEnabled() && !StringUtils.isEmpty(outboundPopUpSerial.getText())){
                                    hideKeyboard(getApplicationContext());
                                    outboundCheckLotAndSerialFocusOut();
                                    //popUPNextFocus();
                                }
                                //System.out.println("Serial");
                            }
                        }
                    });
                    //Outbound POP UP QTY Focus Change
                    outboundPopUpQty.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                        @Override
                        public void onFocusChange(View v, boolean hasFocus) {
                            if(!hasFocus){
                                if(outboundPopUpQty.isEnabled() && !StringUtils.isEmpty(outboundPopUpQty.getText())){
                                    hideKeyboard(getApplicationContext());
                                    popUPNextFocus();
                                }
                                //System.out.println("QTY");
                            }
                        }
                    });
                    //Outbound POP UP Confirm Button Click
                    outboundPopUpSubmit.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if(popUPValidation()){
                                confirmPopUp();
                            }
                        }
                    });
                    //Outbound POP UP Close Button Click
                    outboundPopUpClose.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialogOutboundPopUP.dismiss();
                        }
                    });
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
    //POP UP Field Focus
    private void popUPNextFocus(){
        try {

            if (outboundPopUpSerial.isEnabled() && StringUtils.isEmpty(outboundPopUpSerial.getText())) {
                outboundPopUpSerial.requestFocus();
                return;
            }

            if (outboundPopUpLot.isEnabled() && StringUtils.isEmpty(outboundPopUpLot.getText())) {
                outboundPopUpLot.requestFocus();
                return;
            }

            if (outboundPopUpQty.isEnabled() && StringUtils.isEmpty(outboundPopUpQty.getText())) {
                outboundPopUpQty.requestFocus();
                return;
            }

            if (!StringUtils.isEmpty(outboundPopUpQty.getText()) && Integer.parseInt(outboundPopUpQty.getText().toString()) == 0) {
                outboundPopUpQty.requestFocus();
                return;
            }
            confirmPopUp();// Outbound popUp Confirm
        }catch (Exception ex){
            ex.printStackTrace();
            Toast.makeText(getApplicationContext(),"Invalid Quantity", Toast.LENGTH_SHORT).show();
            vibratorAdaptor.makeVibration();
            return;
        }
    }
    //Submit outbound POP UP Confirmation
    private void confirmPopUp(){
        try{
            Boolean isStockAvl=false;
            Integer checkQtyRecieved=Integer.parseInt(outboundPOChildDetilsEntity.getQuantityRecd())+Integer.parseInt(outboundPopUpQty.getText().toString());
            for(int i=0;i<outboundPOChildDetilsEntityList.size();i++) {
                if((outboundPOChildDetilsEntityList.get(i).getOrderDetailId().equals(outboundPOChildDetilsEntity.getOrderDetailId()) && outboundPOChildDetilsEntityList.get(i).getLocationName().equals(outboundPOChildDetilsEntity.getLocationName()))
                        && (Integer.parseInt(outboundPOChildDetilsEntityList.get(i).getQuantity()) >=checkQtyRecieved)){
                    isStockAvl=true;
                    //Outbound Child Quantity Heading Set
                    outboundQtyRecieved = outboundQtyRecieved + Integer.parseInt(outboundPopUpQty.getText().toString());
                    outboundHeadingQtytxt.setText(outboundQtyRecieved.toString() + "/" + outboundOrderHeaderEntityRef.getTotQuantity());
                    if(outboundQtyRecieved==Integer.parseInt(outboundOrderHeaderEntityRef.getTotQuantity())){
                        outboundCompleteOrder.setBackgroundColor(Color.parseColor("#264d44"));
                    }
                    //Outbound Child List Quantity Recieve Set
                    Integer setQtyRecieved=Integer.parseInt(outboundPOChildDetilsEntityList.get(i).getQuantityRecd())+Integer.parseInt(outboundPopUpQty.getText().toString());
                    outboundPOChildDetilsEntityList.get(i).setQuantityRecd(setQtyRecieved.toString());
                    //check Recieved and Total Quantity
                    if(Integer.parseInt(outboundPOChildDetilsEntityList.get(i).getQuantity()) ==Integer.parseInt(outboundPOChildDetilsEntityList.get(i).getQuantityRecd())){
                        outboundPOChildDetilsEntityList.get(i).setStatus(1);
                        outboundPOChildDetilsEntityList.get(i).setQtyInMiddle(false);
                        outboundPOChildDetilsEntity=outboundPOChildDetilsEntityList.get(i);
                        Collections.sort(outboundPOChildDetilsEntityList);
                    }else{
                        outboundPOChildDetilsEntityList.get(i).setQtyInMiddle(true);
                        outboundPOChildDetilsEntity=outboundPOChildDetilsEntityList.get(i);
                    }

                    pickingItemQty=outboundPopUpQty.getText().toString();
                    if(outboundPOChildDetilsEntityList.get(i).getQtyInMiddle()){
                        outboundPOChildDetilsEntityList.remove(i);
                        outboundPOChildDetilsEntityList.add(0,outboundPOChildDetilsEntity);
                    }
                    checkOutBoundOrderQtytBoundQty(pickingItemQty,outboundPOChildDetilsEntity);
                    outboundPopUpDismiss();
                    //new AsynLoading().execute();
                    break;
                }
            }
            if(!isStockAvl){
                Toast.makeText(getApplicationContext(), Constant.OUTBOUND_STOCK_NOT_AVAILABLE, Toast.LENGTH_SHORT).show();
                vibratorAdaptor.makeVibration();
            }
            outboundListViewTouch=false;
            setOutboundChildView(outboundPOChildDetilsEntityList);

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

    // Validation check for Outbound POP UP
    private Boolean popUPValidation(){
        try {
            if (outboundPopUpLot.isEnabled() && StringUtils.isEmpty(outboundPopUpLot.getText())) {
                outboundPopUpLot.requestFocus();
                Toast.makeText(getApplicationContext(), "Lot required", Toast.LENGTH_SHORT).show();
                vibratorAdaptor.makeVibration();
                return false;
            }

            if (outboundPopUpSerial.isEnabled() && StringUtils.isEmpty(outboundPopUpSerial.getText())) {
                outboundPopUpSerial.requestFocus();
                Toast.makeText(getApplicationContext(), "Serial required", Toast.LENGTH_SHORT).show();
                vibratorAdaptor.makeVibration();
                return false;
            }

            if (outboundPopUpQty.isEnabled() && StringUtils.isEmpty(outboundPopUpQty.getText())) {
                outboundPopUpQty.requestFocus();
                Toast.makeText(getApplicationContext(), "Quantity required", Toast.LENGTH_SHORT).show();
                vibratorAdaptor.makeVibration();
                return false;
            }

            if (!StringUtils.isEmpty(outboundPopUpQty.getText()) && Integer.parseInt(outboundPopUpQty.getText().toString()) == 0) {
                outboundPopUpQty.requestFocus();
                Toast.makeText(getApplicationContext(), "Enter a non-zero quantity", Toast.LENGTH_SHORT).show();
                vibratorAdaptor.makeVibration();
                return false;
            }

            Integer qtyRecv=Integer.parseInt(outboundPOChildDetilsEntity.getQuantityRecd())+ Integer.parseInt(outboundPopUpQty.getText().toString());
            /*System.out.println(outboundPOChildDetilsEntity.getQuantityRecd()+"=#==#"+outboundPopUpQty.getText().toString());
            System.out.println(qtyRecv+"=== "+outboundPOChildDetilsEntity.getQuantity());*/
            if(Integer.parseInt(outboundPOChildDetilsEntity.getQuantity())< qtyRecv){
                Toast.makeText(getApplicationContext(), Constant.OUTBOUND_STOCK_NOT_AVAILABLE, Toast.LENGTH_SHORT).show();
                vibratorAdaptor.makeVibration();
                return false;
            }
        }catch (Exception ex){
            ex.printStackTrace();
            Toast.makeText(getApplicationContext(),"Invalid Quantity", Toast.LENGTH_SHORT).show();
            vibratorAdaptor.makeVibration();
            return false;
        }

        return true;
    }
    //Complete Outbound Order
    private void completeOutboundOrder(String typeOfOutboundSubmit){
        try{
            Integer outboundPorPStatus=0;
            if(typeOfOutboundSubmit.equals(Constant.OUTBOUND_PICKED)){
                outboundPorPStatus=Constant.PICK_OUTBOUND;
                statusOutbound=Constant.OUTBOUND_PICKED;
            }else if(typeOfOutboundSubmit.equals(Constant.OUTBOUND_PICKED_PACKED)){
                outboundPorPStatus=Constant.PICK_PACK_OUTBOUND;
                statusOutbound=Constant.OUTBOUND_PICKED_PACKED;
            }else if(typeOfOutboundSubmit.equals(Constant.OUTBOUND_PACKED)){
                outboundPorPStatus=Constant.PACK_OUTBOUND;
                statusOutbound=Constant.OUTBOUND_PACKED;
            }
            JSONObject jsonObject=new JSONObject();
            jsonObject.put("CompanyDbName",companyDBName);
            jsonObject.put("Userid",userID);
            jsonObject.put("OrderId",orderId);
            jsonObject.put("whid",warehouseID);
            jsonObject.put("Status",outboundPorPStatus);
            jsonObject.put("key",passCode);
            jsonObject.put("Lotserial",outboundSubmitArray);
            jsonString=jsonObject.toString();
            makeUrl=service_url + "/PostOutboundCompleteLotserial";
            new OutboundOrderSubmitComplete().execute(makeUrl);

        }catch (Exception ex){

        }
    }
    //Complete Outbound Order
    public class OutboundOrderSubmitComplete extends AsyncTask<String, String,String> {
        @Override
        protected String doInBackground(String... params) {
            OutboundOrderCompleteService outboundOrderCompleteService=new OutboundOrderCompleteService();
            return outboundOrderCompleteService.completeOutboundOrder(params[0],jsonString);
        }

        @Override
        protected void onPostExecute(String str) {
            try {
                if(str!=null && str.equals("success")){
                    Toast.makeText(getApplicationContext(), "Product  "+statusOutbound+"  successfully", Toast.LENGTH_SHORT).show();
                    vibratorAdaptor.makeVibration();
                    makeOutboundListViewBlank();
                    commonFeaturePickPackBtnClick();
                    resetForAllFunction();
                    getOutboundHeaderPage();
                }else {
                    Toast.makeText(getApplicationContext(), "Product  "+statusOutbound+"  failed", Toast.LENGTH_SHORT).show();
                    vibratorAdaptor.makeVibration();
                }

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
    //Reset Outbound Serial Number
    private void resetOutboundSerialNumber(){
       try{
           JSONObject jsonObject=new JSONObject();
           jsonObject.put("CompanyDbName",companyDBName);
           jsonObject.put("orderid",orderId);
           jsonObject.put("key",passCode);
           jsonString=jsonObject.toString();
           makeUrl=service_url+ "/ResetSerialOutbound";
           new ResetOutboundSerial().execute(makeUrl);
       }catch (Exception ex){
           ex.printStackTrace();
       }
    }
    //Reset Outbound Serial Number Service
    public class ResetOutboundSerial extends AsyncTask<String, String,String> {
        @Override
        protected String doInBackground(String... params) {
            ResetOutboundSerialService resetOutboundSerialService=new ResetOutboundSerialService();
            return resetOutboundSerialService.resetOutboundSerial(params[0],jsonString);
        }

        @Override
        protected void onPostExecute(String str) {
            try {
                if(str!=null && str.equals("Success")){
                   completeOutboundOrder(Constant.OUTBOUND_PACKED);
                }else{
                    Toast.makeText(getApplicationContext(), Constant.SERVER_ERROR, Toast.LENGTH_SHORT).show();
                    vibratorAdaptor.makeVibration();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
    //Make Listview Blank
    private void makeOutboundListViewBlank(){
        outboundListView.setAdapter(null);
    }

    //Internet Connection Error Message
    private void internetConnectionMessage(){
        Toast.makeText(OutboundActivity.this, getApplicationContext().getResources().getString(R.string.error001), Toast.LENGTH_SHORT).show();
        vibratorAdaptor.makeVibration();
    }

    // Common Feature
    private void commonFeaturePickPackBtnClick(){
        outboundChildSubmit=false;
        outboundPoSubmit=false;
        //Make Listview Blank
        outboundListView.setAdapter(null);
        //Default ListView Touch condition
        outboundListViewTouch=false;
        //Default Outbound Child ListView click conition
        outboundChildListItem=false;
        hideChildLayout();
        loadingGifEnable();
    }

    // Hide some layout based on some condition
    private void hideChildLayout(){
        try{
            // Hide outbound PO child Layout
            outboundLabelLayout.setVisibility(View.GONE);
            outboundCompleteOrderBtnLayout.setVisibility(View.GONE);
            outboundCompleteOrder.setBackgroundColor(Color.parseColor("#c0c2c2"));
            outboundUpcSkuLayout.setVisibility(View.GONE);
            //Visible Outbound Po Layout
            outboundPoFilterLayout.setVisibility(View.VISIBLE);
            outboundPoFilterTxt.setText("");
            outboundPoFilterTxt.requestFocus();
            shouldOpenOutboundPopUp=false;
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    //Visible some layout based on some condition
    private void visibleLayout(){
        // Visible outbound PO child Layout
        outboundLabelLayout.setVisibility(View.VISIBLE);
        outboundCompleteOrderBtnLayout.setVisibility(View.GONE);
        outboundUpcSkuLayout.setVisibility(View.VISIBLE);
        //Hide Outbound Po Layout
        outboundUpcSkuSearchTxt.setText("");
        outboundUpcSkuSearchTxt.requestFocus();
        outboundPoFilterLayout.setVisibility(View.GONE);

        //UPC Sku Lock Unlock Logic
        String isLockUnlock=sharedpreferences.getString(Constant.OUTBOUND_UPC_SKU_TEXT_LOCK, "");
        if(isLockUnlock.equals(Constant.LOCK_TEXT)){
            upcSkuLock.setVisibility(View.GONE);
            upcSkuUnLock.setVisibility(View.VISIBLE);
            shouldOpenOutboundPopUp=false;
        }else if(isLockUnlock.equals(Constant.UNLOCK_TEXT)){
            upcSkuLock.setVisibility(View.VISIBLE);
            upcSkuUnLock.setVisibility(View.GONE);
            shouldOpenOutboundPopUp=true;
        }

        //hideKeyboard
       InputMethodManager imm = (InputMethodManager)getSystemService(
                Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(outboundUpcSkuSearchTxt.getWindowToken(), 0);//Everytime Hide keyboard When Come to PO Child screen
    }
    //Disable Loading Progressbar
    private  void loadingGifDisable(){
        outboundLoadingImage.setVisibility(View.GONE);
    }
    //Enable Loading Progressbar
    private void loadingGifEnable(){
        outboundLoadingImage.setVisibility(View.VISIBLE);
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

    //Outbound POP UP Open
    private void openOutboundPopUp(String orderDetailId,String pickLocationName){
        //System.out.println(orderDetailId+" "+pickLocationName);
        for(int i=0;i<outboundPOChildDetilsEntityList.size();i++) {
            if (outboundPOChildDetilsEntityList.get(i).getOrderDetailId().equals(orderDetailId) && outboundPOChildDetilsEntityList.get(i).getLocationName().equals(pickLocationName)
                    && (Integer.parseInt(outboundPOChildDetilsEntityList.get(i).getQuantity()) > Integer.parseInt(outboundPOChildDetilsEntityList.get(i).getQuantityRecd()))) {
                //optional call Outbound product Lot and Serial
                outboundPOChildDetilsEntity=outboundPOChildDetilsEntityList.get(i);
                checkOutboundProductLotAndSerial(outboundPOChildDetilsEntityList.get(i).getProductID());
                break;
            }
        }
    }
    //View Outbound Complete Order Button
    private void viewOutboundCompleteOrderButton()
    {
        if (!outboundChildqtyRecv) {
            outboundChildqtyRecv = true;
            outboundCompleteOrderBtnLayout.setVisibility(View.VISIBLE);
        }
    }

    //Outbound POP UP Lot and Serial Focus Out check
    private void outboundCheckLotAndSerialFocusOut(){
        try{
            JSONObject jsonObject=new JSONObject();
            jsonObject.put("CompanyDbName",companyDBName);
            jsonObject.put("Userid",userID);
            jsonObject.put("Orderid",outboundPOChildDetilsEntity.getOrderDetailId());
            jsonObject.put("whid",warehouseID);
            jsonObject.put("Productid",outboundPOChildDetilsEntity.getProductID());
            jsonObject.put("Lotno",outboundPopUpLot.getText());
            jsonObject.put("Combid",outboundPOChildDetilsEntity.getCombid());
            jsonObject.put("Serialno",outboundPopUpSerial.getText());
            jsonObject.put("status",postOutboundMenuId);
            jsonObject.put("Pickloc",outboundPOChildDetilsEntity.getProductPickingLocId());
            jsonObject.put("key",passCode);
            jsonString=jsonObject.toString();
            makeUrl=service_url + "/Checkoutboundserial";
            new OutboundSerialLotFocusOutCheck().execute(makeUrl);

        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    //POP UP Check Serial and Lot Focus Out Service
    public class OutboundSerialLotFocusOutCheck extends AsyncTask<String, String,List<OutboundLotSerialCheckEntity> > {
        @Override
        protected List<OutboundLotSerialCheckEntity>  doInBackground(String... params) {
            OutboundSerialLotFocusOutCheckService outboundSerialLotFocusOutCheckService=new OutboundSerialLotFocusOutCheckService();
            return outboundSerialLotFocusOutCheckService.checkOutboundLotSerial(params[0],jsonString);
        }

        @Override
        protected void onPostExecute(List<OutboundLotSerialCheckEntity>  outboundLotSerialCheckEntities) {
            try {

                if(outboundLotSerialCheckEntities!=null && outboundLotSerialCheckEntities.size()>0){
                    if(outboundLotSerialCheckEntities.get(0).getId()==0){
                        outboundPopUpSerial.setText("");
                        outboundPopUpQty.setText("");
                        Toast.makeText(OutboundActivity.this, outboundLotSerialCheckEntities.get(0).getStatus(), Toast.LENGTH_SHORT).show();
                        vibratorAdaptor.makeVibration();
                        popUPNextFocus();//Popup Current Focus
                    }else if(outboundLotSerialCheckEntities.get(0).getId()==1){
                        outboundPopUpQty.setText("1");
                        popUPNextFocus();
                    }
                }

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
    //Hide Keyboard
    public static void hideKeyboard(Context ctx) {
        InputMethodManager imm = (InputMethodManager)ctx.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(cView.getWindowToken(), 0);
    }
    //Rich Limit message
    private void richLimitMessage(){
        Toast.makeText(getApplicationContext(), Constant.RICH_LIMIT, Toast.LENGTH_SHORT).show();
        vibratorAdaptor.makeVibration();
    }

    //Confirm Exit
    private void confirmExit(){
        screenExitbuilder = new AlertDialog.Builder(OutboundActivity.this);
        screenExitbuilder.setMessage("Do you want to exit from this screen");
    }

    //Outbound Refresh Page
    private Boolean outboundRefreshPage(){
        Intent refresh = new Intent(this, OutboundActivity.class);
        startActivity(refresh);//Start the same Activity
        finish();
        return true;
    }

    //Go Picking
    private void goPicking(){
        pickingbtn.setBackgroundDrawable(getResources().getDrawable(R.drawable.heading_button_border));
        packingbtn.setBackgroundColor(Color.WHITE);
        defaultOutbound = Constant.OUTBOUND_PICKING;
        makeOutboundListViewBlank();
        commonFeaturePickPackBtnClick();
        resetForAllFunction();
        getOutboundHeaderPage();
        System.gc();
    }
    //Go Packing
    private void goPacking(){
        packingbtn.setBackgroundDrawable(getResources().getDrawable(R.drawable.heading_button_border));
        pickingbtn.setBackgroundColor(Color.WHITE);
        defaultOutbound = Constant.OUTBOUND_PACKING;
        makeOutboundListViewBlank();
        commonFeaturePickPackBtnClick();
        resetForAllFunction();
        getOutboundHeaderPage();
        System.gc();
    }

    //Back Button Common Feature
    private void backButtonCommonFeature(){
        commonFeaturePickPackBtnClick();
        resetForAllFunction();
        getOutboundHeaderPage();
        System.gc();
    }
    //Go to Home Page
    private void goToHomePage(){
        System.gc();
        Intent backToHome = new Intent(this, HomeActivity.class);
        startActivity(backToHome);//Start the Home Activity
         finish();

    }

    //Loading Check For Focus Field
    public class AsynLoading extends AsyncTask<String, String, Boolean> {
        @Override
        protected Boolean doInBackground(String... params) {
            AsynLoadingService asynLoadingService=new AsynLoadingService();
            return asynLoadingService.getLoadingStatus();
        }

        @Override
        protected void onPostExecute(Boolean status) {
            try {
                popUPNextFocus();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    //Outbound PopUp Close
    private void outboundPopUpDismiss(){
        viewOutboundCompleteOrderButton();
        outboundPopUpLot.setText("");
        outboundPopUpSerial.setText("");
        outboundPopUpQty.setText("");
        dialogOutboundPopUP.dismiss();
    }
}
