package id.advancepro.com.advancepro;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import id.advancepro.com.advancepro.adapter.VibratorAdaptor;
import id.advancepro.com.advancepro.model.TransferCheckProductLocationEntity;
import id.advancepro.com.advancepro.model.TransferPLFromLocationEntity;
import id.advancepro.com.advancepro.model.TransferWarehouseToEntity;
import id.advancepro.com.advancepro.service.AsynLoadingService;
import id.advancepro.com.advancepro.service.InternetConnection;
import id.advancepro.com.advancepro.service.TransferCheckProductLocationService;
import id.advancepro.com.advancepro.service.TransferPLFromService;
import id.advancepro.com.advancepro.service.TransferPostSubmitService;
import id.advancepro.com.advancepro.service.TransferWarehouseToService;
import id.advancepro.com.advancepro.service.VerifyLotForTransferService;
import id.advancepro.com.advancepro.utils.Constant;

/**
 * Created by TASIV on 3/24/2017.
 */

public class Transfer extends Fragment{

    private View rootView;
    private InternetConnection intConn;
    private SharedPreferences sharedpreferences;
    private String makeUrl;
    private String passCode;
    private String service_url;
    private String companyDBName;
    private Integer userID;
    private Integer warehouseID;
    private Boolean checkInternet=false;
    private String jsonString;
    private Boolean plFromInitialize;
    private Boolean plToInitialize;
    private Boolean warehouseToInitialize;
    private LinearLayout transferPlToLocakLayout;
    private LinearLayout transferPlToUnLocakLayout;
    private LinearLayout transferWarehouseToLocakLayout;
    private LinearLayout transferWarehouseToUnLocakLayout;
    private LinearLayout transferPlFromLockLayout;
    private LinearLayout transferPlFromUnLockLayout;
    private LinearLayout transferReasonLockLayout;
    private LinearLayout transferReasonUnLockLayout;
    private LinearLayout transferQtyLockLayout;
    private LinearLayout transferQtyUnLockLayout;
    private LinearLayout transferSelectKeyLockLayout;
    private LinearLayout transferSelectKeyUnLockLayout;
    private LinearLayout transferLotLayout;
    private LinearLayout transferVerifyLotCheckboxLayout;
    private EditText transferSearchtxt;
    private EditText transferReasontxt;
    private EditText transferQtytxt;
    private Spinner transferPlToDrpDwn;
    private Spinner transferPlFromDrpDwn;
    private Spinner transferWarehouseToDrpDwn;
    private Spinner transferSelectKeyDrpDwn;
    private Context context;
    private CheckBox verifyLotCheckbox;
    private EditText transferLotTxt;
    private Button submitTransferBtn;
    private String plFromName;
    private String warehouseToID;
    private String plToName;
    private String selectKeyName;
    private EditText locationfromtxt;
    private EditText locationtotxt;
    private List<TransferPLFromLocationEntity> transferPLFromLocationEntityListRef;
    private List<TransferPLFromLocationEntity> transferPLToLocationEntityListRef;
    private Integer warehoiseIDPlTO;

    private static View cView;
    private VibratorAdaptor vibratorAdaptor;
    private Context contx;
    private Boolean transferSelectKeyinitialize;



    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        try {
            rootView = inflater.inflate(R.layout.transfer, container, false);
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        try {
            super.onViewCreated(view, savedInstanceState);
            //SsharedPreferences Value
            sharedpreferences = this.getActivity().getSharedPreferences(Constant.LOGIN_PREF, Context.MODE_PRIVATE);
            passCode=sharedpreferences.getString(Constant.PASS_CODE,null);
            service_url=sharedpreferences.getString(Constant.SERVICE_URL,null);
            companyDBName=sharedpreferences.getString(Constant.LOGIN_DB,null);
            userID= Integer.parseInt(sharedpreferences.getString(Constant.LOGIN_USERID,"0"));
            warehouseID= Integer.parseInt(sharedpreferences.getString(Constant.DEFAULT_WAREHOUSE_ID,"0"));

            intConn=new InternetConnection(); // Internet Connection Initialize
            plFromInitialize=false; // From Picking Location initialize false for first time page loading
            plToInitialize=false; // To Picking Location initialize false for first time page loading
            warehouseToInitialize=false; // Warehouse Location initialize false for first time page loading
            transferSelectKeyinitialize=false; // Select Key initialize false for first time page loading

            //Vibrator
            vibratorAdaptor=new VibratorAdaptor(getContext()); //Vibrator Class

            //Initialize all the fields,lock ,unlock
            transferPlToLocakLayout=(LinearLayout)view.findViewById(R.id.transferlocationtolocklayout); //transfer PL to lock layout
            transferPlToUnLocakLayout=(LinearLayout)view.findViewById(R.id.transferlocationtounlocklayout);//transfer PL to unlock layout
            transferWarehouseToLocakLayout=(LinearLayout)view.findViewById(R.id.transferwarehousetolocklayout); // transfer warehouse lock layout
            transferWarehouseToUnLocakLayout=(LinearLayout)view.findViewById(R.id.transferwarehousetounlocklayout);// transfer warehouse unlock layout
            transferPlFromLockLayout=(LinearLayout)view.findViewById(R.id.transferlocationfromlocklayout); // transfer pl from lock layout
            transferPlFromUnLockLayout=(LinearLayout)view.findViewById(R.id.transferlocationfromunlocklayout); // transfer pl from unlock layout
            transferReasonLockLayout=(LinearLayout)view.findViewById(R.id.transferreasonlocklayout); //transfer reason lock layout
            transferReasonUnLockLayout=(LinearLayout)view.findViewById(R.id.transferreasonunlocklayout); //transfer reason unlock layout
            transferQtyLockLayout=(LinearLayout)view.findViewById(R.id.transferqtylocklayout); //transfer Quantity lock layout
            transferQtyUnLockLayout=(LinearLayout)view.findViewById(R.id.transferqtyunlocklayout); // transfer Quantity unlock layout
            transferSelectKeyLockLayout=(LinearLayout)view.findViewById(R.id.transferselectkeylocklayout); // transfer Select key lock layout
            transferSelectKeyUnLockLayout=(LinearLayout)view.findViewById(R.id.transferselectkeyunlocklayout);// transfer Select key unlock layout
            //Lot layout based on Serial # (Initialize)
            transferLotLayout=(LinearLayout)view.findViewById(R.id.transferlotseriallayout);
            transferLotLayout.setVisibility(View.GONE);
            transferVerifyLotCheckboxLayout=(LinearLayout)view.findViewById(R.id.transferverifylotcheckboxlayout);
            verifyLotCheckbox=(CheckBox)view.findViewById(R.id.transferverifylotcheckbox);
            transferLotTxt=(EditText)view.findViewById(R.id.transferlottxt);

            transferSearchtxt=(EditText)view.findViewById(R.id.transferproducttxt); // Transfer Search text field
            transferReasontxt=(EditText)view.findViewById(R.id.transfertxtreason); //transfer reason text field
            transferQtytxt=(EditText)view.findViewById(R.id.transferqtytxt); // transfer Quantity text field

            locationfromtxt=(EditText)view.findViewById(R.id.transferlocationfromtxt); // Transfer Pl from text field
            locationtotxt=(EditText)view.findViewById(R.id.transferlocationtotxt); //  Transfer Pl to text field

            // Dropdown
            transferPlToDrpDwn=(Spinner)view.findViewById(R.id.transferlocationtospinnner); // Transfer PL to dropdown
            transferPlFromDrpDwn=(Spinner)view.findViewById(R.id.transferlocationfromspinnner); // Transfer PL from Dropdown
            transferWarehouseToDrpDwn=(Spinner)view.findViewById(R.id.transferwarehousetospinner); // Transfer Warehouse Dropdown
            transferSelectKeyDrpDwn=(Spinner)view.findViewById(R.id.transferproductselectkeyspinner); // Transfer Select key Dropdown
            context=this.getActivity().getApplicationContext(); // assign current activity context
            contx=getContext(); // assign current context
            cView=this.getView(); // assign Current transfer view

            //this.getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
            // Submit Transfer Button
            submitTransferBtn=(Button)view.findViewById(R.id.transfersubmitbtn);


            hideKeyboard(contx);// hide device keyboard
            //PL Lock
            transferPlToLocakLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    TransferPLFromLocationEntity transferPLFromLocationEntity=(TransferPLFromLocationEntity)transferPlToDrpDwn.getSelectedItem();
                    if(transferPLFromLocationEntity!=null) {
                        if (Integer.parseInt(transferPLFromLocationEntity.getPicklocid()) == 0 || (!transferPLFromLocationEntity.getPickLocname().equals(locationtotxt.getText().toString()))) {
                            Toast.makeText(context, "Please Select Correct PL To", Toast.LENGTH_SHORT).show();
                            vibratorAdaptor.makeVibration();
                        } else {
                            transferPlToLocakLayout.setVisibility(View.GONE);
                            transferPlToUnLocakLayout.setVisibility(View.VISIBLE);
                            SharedPreferences.Editor editor = sharedpreferences.edit();
                            editor.putString(Constant.IS_TRANSFER_PL_TO_LOCK, Constant.LOCK_TEXT);

                            if (transferPLFromLocationEntity != null) {
                                if (Integer.parseInt(transferPLFromLocationEntity.getPicklocid()) == 0) {
                                    editor.putString(Constant.TRANSFER_PL_TO_VALUE, "");
                                } else {
                                    editor.putString(Constant.TRANSFER_PL_TO_VALUE, transferPLFromLocationEntity.getPickLocname());
                                }
                                editor.commit();
                            }
                            transferPlToDrpDwn.setEnabled(false);
                            locationtotxt.setEnabled(false);
                            requestFocus();
                        }
                    }else{
                        Toast.makeText(context, "Please Select Correct PL To", Toast.LENGTH_SHORT).show();
                        vibratorAdaptor.makeVibration();
                    }

                }
            });
            // PL Unlock
            transferPlToUnLocakLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    transferPlToLocakLayout.setVisibility(View.VISIBLE);
                    transferPlToUnLocakLayout.setVisibility(View.GONE);
                    SharedPreferences.Editor editor = sharedpreferences.edit();
                    editor.putString(Constant.IS_TRANSFER_PL_TO_LOCK, Constant.UNLOCK_TEXT);
                    editor.putString(Constant.TRANSFER_PL_TO_VALUE, "");
                    editor.commit();
                    transferPlToDrpDwn.setEnabled(true);
                    locationtotxt.setEnabled(true);
                    locationtotxt.requestFocus();
                }
            });
            //Warehouse To Lock
            transferWarehouseToLocakLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    TransferWarehouseToEntity transferWarehouseToEntity=(TransferWarehouseToEntity)transferWarehouseToDrpDwn.getSelectedItem();
                    if(Integer.parseInt(transferWarehouseToEntity.getWareHouseID())==0) {
                        Toast.makeText(context, "Please Select Warehouse", Toast.LENGTH_SHORT).show();
                        vibratorAdaptor.makeVibration();
                    }else{
                        transferWarehouseToLocakLayout.setVisibility(View.GONE);
                        transferWarehouseToUnLocakLayout.setVisibility(View.VISIBLE);
                        SharedPreferences.Editor editor = sharedpreferences.edit();
                        editor.putString(Constant.IS_TRANSFER_WAREHOUSE_TO_LOCK, Constant.LOCK_TEXT);
                        editor.putString(Constant.TRANSFER_WAREHOUSE_TO_VALUE, transferWarehouseToEntity.getWareHouseName());
                        editor.commit();
                        transferWarehouseToDrpDwn.setEnabled(false);

                    }
                    requestFocus();

                }
            });
            //Warehouse To Unlock
            transferWarehouseToUnLocakLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    transferWarehouseToLocakLayout.setVisibility(View.VISIBLE);
                    transferWarehouseToUnLocakLayout.setVisibility(View.GONE);
                    SharedPreferences.Editor editor = sharedpreferences.edit();
                    editor.putString(Constant.IS_TRANSFER_WAREHOUSE_TO_LOCK, Constant.UNLOCK_TEXT);
                    editor.putString(Constant.TRANSFER_WAREHOUSE_TO_VALUE, "");
                    editor.commit();
                    transferWarehouseToDrpDwn.setEnabled(true);
                    transferWarehouseToDrpDwn.requestFocus();
                }
            });
            //PL From Lock
            transferPlFromLockLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    TransferPLFromLocationEntity transferPLFromLocationEntity=(TransferPLFromLocationEntity)transferPlFromDrpDwn.getSelectedItem();
                    if(Integer.parseInt(transferPLFromLocationEntity.getPicklocid())==0 && (!transferPLFromLocationEntity.getPickLocname().equals(locationfromtxt.getText().toString()))){
                        Toast.makeText(context, "Please Select PL From", Toast.LENGTH_SHORT).show();
                        vibratorAdaptor.makeVibration();
                    }else {
                        transferPlFromLockLayout.setVisibility(View.GONE);
                        transferPlFromUnLockLayout.setVisibility(View.VISIBLE);
                        SharedPreferences.Editor editor = sharedpreferences.edit();
                        editor.putString(Constant.IS_TRANSFER_PL_FROM_LOCK, Constant.LOCK_TEXT);

                        if (Integer.parseInt(transferPLFromLocationEntity.getPicklocid()) == 0) {
                            editor.putString(Constant.TRANSFER_PL_FROM_VALUE, "");
                        } else {
                            editor.putString(Constant.TRANSFER_PL_FROM_VALUE, transferPLFromLocationEntity.getPickLocname());
                        }
                        editor.commit();
                        transferPlFromDrpDwn.setEnabled(false);
                        locationfromtxt.setEnabled(false);
                        requestFocus();
                    }
                }
            });
            // PL From Unlock
            transferPlFromUnLockLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    transferPlFromLockLayout.setVisibility(View.VISIBLE);
                    transferPlFromUnLockLayout.setVisibility(View.GONE);
                    SharedPreferences.Editor editor = sharedpreferences.edit();
                    editor.putString(Constant.IS_TRANSFER_PL_FROM_LOCK, Constant.UNLOCK_TEXT);
                    editor.putString(Constant.TRANSFER_PL_FROM_VALUE, "");
                    editor.commit();
                    transferPlFromDrpDwn.setEnabled(true);
                    locationfromtxt.setEnabled(true);
                    locationfromtxt.requestFocus();
                }
            });
            // Transfer Reason Lock
            transferReasonLockLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    transferReasonLockLayout.setVisibility(View.GONE);
                    transferReasonUnLockLayout.setVisibility(View.VISIBLE);
                    SharedPreferences.Editor editor = sharedpreferences.edit();
                    editor.putString(Constant.IS_TRANSFER_REASON_LOCK, Constant.LOCK_TEXT);
                    editor.putString(Constant.TRANSFER_REASON_VALUE, transferReasontxt.getText().toString());
                    editor.commit();
                    transferReasontxt.setEnabled(false);
                    requestFocus();
                }
            });
            //Transfer Reason unlock
            transferReasonUnLockLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    transferReasonLockLayout.setVisibility(View.VISIBLE);
                    transferReasonUnLockLayout.setVisibility(View.GONE);
                    SharedPreferences.Editor editor = sharedpreferences.edit();
                    editor.putString(Constant.IS_TRANSFER_REASON_LOCK, Constant.UNLOCK_TEXT);
                    editor.putString(Constant.TRANSFER_REASON_VALUE, "");
                    editor.commit();
                    transferReasontxt.setEnabled(true);
                    transferReasontxt.requestFocus();
                }
            });
            //Transfer Quantity Lock
            transferQtyLockLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    transferQtyLockLayout.setVisibility(View.GONE);
                    transferQtyUnLockLayout.setVisibility(View.VISIBLE);
                    if(StringUtils.isEmpty(transferQtytxt.getText())){
                        transferQtytxt.setText("1");
                    }
                    SharedPreferences.Editor editor = sharedpreferences.edit();
                    editor.putString(Constant.IS_TRANSFER_QUANTITY_LOCK, Constant.LOCK_TEXT);
                    editor.putString(Constant.TRANSFER_QUANTITY_VALUE, transferQtytxt.getText().toString());
                    editor.commit();
                    transferQtytxt.setEnabled(false);
                    requestFocus();
                }
            });
            //Transfer Quantity Unlock
            transferQtyUnLockLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    transferQtyLockLayout.setVisibility(View.VISIBLE);
                    transferQtyUnLockLayout.setVisibility(View.GONE);
                    SharedPreferences.Editor editor = sharedpreferences.edit();
                    editor.putString(Constant.IS_TRANSFER_QUANTITY_LOCK, Constant.UNLOCK_TEXT);
                    editor.putString(Constant.TRANSFER_QUANTITY_VALUE,"");
                    editor.commit();
                    transferQtytxt.setEnabled(true);
                    transferQtytxt.requestFocus();
                }
            });
            //Transfer Product Select Key lock
            transferSelectKeyLockLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    transferSelectKeyLockLayout.setVisibility(View.GONE);
                    transferSelectKeyUnLockLayout.setVisibility(View.VISIBLE);
                    SharedPreferences.Editor editor = sharedpreferences.edit();
                    editor.putString(Constant.IS_TRANSFER_SELECT_KEY_LOCK, Constant.LOCK_TEXT);
                    String selectKey=(String)transferSelectKeyDrpDwn.getSelectedItem();
                    if(StringUtils.isEmpty(selectKey)){
                        editor.putString(Constant.TRANSFER_SELECT_KEY_VALUE, "");
                    }else{
                        editor.putString(Constant.TRANSFER_SELECT_KEY_VALUE, selectKey);
                    }
                    editor.commit();
                    transferSelectKeyDrpDwn.setEnabled(false);
                    requestFocus();
                }
            });
            //Transfer Product Select Key Unlock
            transferSelectKeyUnLockLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    transferSelectKeyLockLayout.setVisibility(View.VISIBLE);
                    transferSelectKeyUnLockLayout.setVisibility(View.GONE);
                    SharedPreferences.Editor editor = sharedpreferences.edit();
                    editor.putString(Constant.IS_TRANSFER_SELECT_KEY_LOCK, Constant.UNLOCK_TEXT);
                    editor.putString(Constant.TRANSFER_SELECT_KEY_VALUE, "");
                    editor.commit();
                    transferSelectKeyDrpDwn.setEnabled(true);
                    transferSelectKeyDrpDwn.requestFocus();
                }
            });
            //Transfer Product Search Text key listener
            transferSearchtxt.setOnKeyListener(new View.OnKeyListener() {
                @Override
                public boolean onKey(View v, int keyCode, KeyEvent event) {
                    if(keyCode==66 && event.getAction()==KeyEvent.ACTION_UP){
                        hideKeyboard(contx);
                        //submitTransfer();
                        new AsynLoading().execute();

                    }
                    return false;
                }
            });
            //Transfer Quantity text key Listener
            transferQtytxt.setOnKeyListener(new View.OnKeyListener() {
                @Override
                public boolean onKey(View v, int keyCode, KeyEvent event) {
                    if(keyCode==66 && event.getAction()==KeyEvent.ACTION_UP){
                        hideKeyboard(contx);
                        new AsynLoading().execute();
                    }
                    return false;
                }
            });
            //Transfer Lot key Listener
            transferLotTxt.setOnKeyListener(new View.OnKeyListener() {
                @Override
                public boolean onKey(View v, int keyCode, KeyEvent event) {
                    if(keyCode==66 && event.getAction()==KeyEvent.ACTION_UP){
                        hideKeyboard(contx);
                        new AsynLoading().execute();
                    }
                    return false;
                }
            });
            //PL To dropdown change
            transferPlToDrpDwn.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if(!plToInitialize){
                        plToInitialize=true;
                        return;
                    }
                    TransferPLFromLocationEntity transferPLFromLocationEntity=(TransferPLFromLocationEntity)transferPlToDrpDwn.getSelectedItem();
                    if(Integer.parseInt(transferPLFromLocationEntity.getPicklocid())!=0){
                        locationtotxt.setText(transferPLFromLocationEntity.getPickLocname());
                    }else{
                        locationtotxt.setText("");
                    }
                    requestFocus();
                }
                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
            //Warehouse to Dropdown change
            transferWarehouseToDrpDwn.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if(!warehouseToInitialize){
                        warehouseToInitialize=true;
                        return;
                    }
                    TransferWarehouseToEntity transferWarehouseToEntity=(TransferWarehouseToEntity)transferWarehouseToDrpDwn.getSelectedItem();
                    warehoiseIDPlTO=Integer.parseInt(transferWarehouseToEntity.getWareHouseID());
                    getTransferPLToLocation();
                    requestFocus();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
            //PL from Dropdown change
            transferPlFromDrpDwn.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    //System.out.println(plFromInitialize);
                    if(!plFromInitialize){
                        plFromInitialize=true;
                        return;
                    }
                    TransferPLFromLocationEntity transferPLFromLocationEntity=(TransferPLFromLocationEntity)transferPlFromDrpDwn.getSelectedItem();
                    //System.out.println(transferPLFromLocationEntity.getPicklocid());
                    if(Integer.parseInt(transferPLFromLocationEntity.getPicklocid())!=0){
                        //System.out.println(transferPLFromLocationEntity.getPickLocname());
                        locationfromtxt.setText(transferPLFromLocationEntity.getPickLocname());
                    }else{
                        locationfromtxt.setText("");
                    }
                    requestFocus();
                }
                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
            //Select Key Dropdown Change
            transferSelectKeyDrpDwn.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if(!transferSelectKeyinitialize){
                        transferSelectKeyinitialize=true;
                        return;
                    }
                    String selectedKey=(String)parent.getSelectedItem();
                    if(selectedKey.equalsIgnoreCase(Constant.TRANSFER_SERIAL_TEXT)){
                        makeQtyOneForSerial();
                        isVeryLotEnableForTransfer();
                    }else{
                        transferLotLayout.setVisibility(View.GONE);
                        verifyLotCheckbox.setChecked(false);
                    }
                    transferSearchtxt.setText("");
                    requestFocus();
                }
                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

            //Verify Lot checkBox Click Event
            verifyLotCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    transferLotTxt.setVisibility(View.VISIBLE);
                }else{
                    transferLotTxt.setVisibility(View.GONE);
                    transferLotTxt.setText("");
                }
                requestFocus();
                }
            });
            // Transfer Submit Button
            submitTransferBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    hideKeyboard(contx);
                    submitTransfer();
                }
            });
            //Transfer location from text change event
            locationfromtxt.setOnKeyListener(new View.OnKeyListener() {
                @Override
                public boolean onKey(View v, int keyCode, KeyEvent event) {
                    if(keyCode==66 && event.getAction()==KeyEvent.ACTION_UP){
                        if (!StringUtils.isEmpty(locationfromtxt.getText())) {
                            Boolean matchFind = false;
                            if (transferPLFromLocationEntityListRef != null && transferPLFromLocationEntityListRef.size()>0){
                                for (int i = 0; i < transferPLFromLocationEntityListRef.size(); i++) {
                                    if (transferPLFromLocationEntityListRef.get(i).getPickLocname().equals(locationfromtxt.getText().toString())) {
                                        transferPlFromDrpDwn.setSelection(i);
                                        matchFind = true;
                                        break;
                                    }
                                }
                                if (!matchFind) {
                                    locationfromtxt.setText("");
                                    Toast.makeText(context, "Location not found", Toast.LENGTH_SHORT).show();
                                    vibratorAdaptor.makeVibration();
                                }
                            }else{
                                locationfromtxt.setText("");
                            }
                            hideKeyboard(contx);
                            new AsynLoading().execute();
                        }
                    }
                    return false;
                }
            });
            //Transfer location to change evvent
            locationtotxt.setOnKeyListener(new View.OnKeyListener() {
                @Override
                public boolean onKey(View v, int keyCode, KeyEvent event) {
                    if(keyCode==66 && event.getAction()==KeyEvent.ACTION_UP){
                        if (!StringUtils.isEmpty(locationtotxt.getText())) {
                            Boolean matchFind = false;
                            //System.out.println(transferPLToLocationEntityListRef.size());
                            if(transferPLToLocationEntityListRef!=null && transferPLToLocationEntityListRef.size()>0) {
                                for (int i = 0; i < transferPLToLocationEntityListRef.size(); i++) {
                                    if (transferPLToLocationEntityListRef.get(i).getPickLocname().equals(locationtotxt.getText().toString())) {
                                        transferPlToDrpDwn.setSelection(i);
                                        matchFind = true;
                                        break;
                                    }
                                }
                                if (!matchFind) {
                                    locationtotxt.setText("");
                                    Toast.makeText(context, "Location not found", Toast.LENGTH_SHORT).show();
                                    vibratorAdaptor.makeVibration();
                                }
                            }else{
                                locationtotxt.setText("");
                            }
                            hideKeyboard(contx);
                            new AsynLoading().execute();
                        }
                    }
                    return false;
                }
            });

            //Load DroDown value by calling Web Service
            checkTransferSavedSettings();// Check previous saved data for transfer
            getTransferPLFromLocation(); // Get transfer From Picking location
            getTransferWarehouseList(); // Get transfer Warehouse List
            setSelectKeyDropDown(); // Set Transfer Select key DropDown Value
            requestFocus(); // Requesting focus

        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    // Check Transfer previous Save Data
    private void checkTransferSavedSettings(){
        //Pl From
        if(sharedpreferences.getString(Constant.IS_TRANSFER_PL_FROM_LOCK,"").equalsIgnoreCase(Constant.LOCK_TEXT)){
            transferPlFromDrpDwn.setEnabled(false);
            transferPlFromLockLayout.setVisibility(View.GONE);
            transferPlFromUnLockLayout.setVisibility(View.VISIBLE);
            locationfromtxt.setEnabled(false);
        }else{
            transferPlFromDrpDwn.setEnabled(true);
            transferPlFromDrpDwn.setSelection(0);
            transferPlFromLockLayout.setVisibility(View.VISIBLE);
            transferPlFromUnLockLayout.setVisibility(View.GONE);
            locationfromtxt.setEnabled(true);
            locationfromtxt.setText("");
        }

        //Warehouse To
        if(sharedpreferences.getString(Constant.IS_TRANSFER_WAREHOUSE_TO_LOCK,"").equalsIgnoreCase(Constant.LOCK_TEXT)){
            transferWarehouseToDrpDwn.setEnabled(false);
            transferWarehouseToLocakLayout.setVisibility(View.GONE);
            transferWarehouseToUnLocakLayout.setVisibility(View.VISIBLE);
        }else{
            transferWarehouseToDrpDwn.setEnabled(true);
            transferWarehouseToDrpDwn.setSelection(0);
            transferWarehouseToLocakLayout.setVisibility(View.VISIBLE);
            transferWarehouseToUnLocakLayout.setVisibility(View.GONE);
        }

        //Pl To
        if(sharedpreferences.getString(Constant.IS_TRANSFER_PL_TO_LOCK,"").equalsIgnoreCase(Constant.LOCK_TEXT)
                && sharedpreferences.getString(Constant.IS_TRANSFER_WAREHOUSE_TO_LOCK,"").equalsIgnoreCase(Constant.LOCK_TEXT)){
            transferPlToDrpDwn.setEnabled(false);
            transferPlToLocakLayout.setVisibility(View.GONE);
            transferPlToUnLocakLayout.setVisibility(View.VISIBLE);
            locationtotxt.setEnabled(false);
        }else{
            transferPlToDrpDwn.setEnabled(true);
            transferPlToDrpDwn.setSelection(0);
            transferPlToLocakLayout.setVisibility(View.VISIBLE);
            transferPlToUnLocakLayout.setVisibility(View.GONE);
            locationtotxt.setEnabled(true);
            locationtotxt.setText("");
        }

        // Reason
        if(sharedpreferences.getString(Constant.IS_TRANSFER_REASON_LOCK,"").equalsIgnoreCase(Constant.LOCK_TEXT)){
            transferReasontxt.setEnabled(false);
            transferReasontxt.setText(sharedpreferences.getString(Constant.TRANSFER_REASON_VALUE,""));
            transferReasonLockLayout.setVisibility(View.GONE);
            transferReasonUnLockLayout.setVisibility(View.VISIBLE);
        }else{
            transferReasontxt.setEnabled(true);
            transferReasontxt.setText("");
            transferReasonLockLayout.setVisibility(View.VISIBLE);
            transferReasonUnLockLayout.setVisibility(View.GONE);
        }
        //Quantity
        if(sharedpreferences.getString(Constant.IS_TRANSFER_QUANTITY_LOCK,"").equalsIgnoreCase(Constant.LOCK_TEXT)){
            transferQtytxt.setEnabled(false);
            transferQtytxt.setText(sharedpreferences.getString(Constant.TRANSFER_QUANTITY_VALUE,""));
            transferQtyLockLayout.setVisibility(View.GONE);
            transferQtyUnLockLayout.setVisibility(View.VISIBLE);
        }else{
            transferQtytxt.setEnabled(true);
            transferQtytxt.setText("");
            transferQtyLockLayout.setVisibility(View.VISIBLE);
            transferQtyUnLockLayout.setVisibility(View.GONE);
        }
        //Select Key
        if(sharedpreferences.getString(Constant.IS_TRANSFER_SELECT_KEY_LOCK,"").equalsIgnoreCase(Constant.LOCK_TEXT)){
            transferSelectKeyDrpDwn.setEnabled(false);
            transferSelectKeyLockLayout.setVisibility(View.GONE);
            transferSelectKeyUnLockLayout.setVisibility(View.VISIBLE);
            if(transferSelectKeyDrpDwn.getSelectedItem()!=null && transferSelectKeyDrpDwn.getSelectedItem().equals(Constant.TRANSFER_SERIAL_TEXT)){
                makeQtyOneForSerial();
            }
        }else{
            transferSelectKeyDrpDwn.setEnabled(true);
            //transferSelectKeyDrpDwn.setSelection(0);
            if(transferSelectKeyDrpDwn.getSelectedItem()!=null && transferSelectKeyDrpDwn.getSelectedItem().equals(Constant.TRANSFER_SERIAL_TEXT)){
                makeQtyOneForSerial();
            }
            transferSelectKeyLockLayout.setVisibility(View.VISIBLE);
            transferSelectKeyUnLockLayout.setVisibility(View.GONE);
        }

        //ProductSearch Text
        transferSearchtxt.setText("");
        //Lot Text
        transferLotTxt.setText("");
    }
    //Populate Transfer Select key DropDown with Value
    private void setSelectKeyDropDown(){
        try {
            List<String> selectKey = new ArrayList<>();
            selectKey.add(Constant.TRANSFER_SKU_TEXT);
            selectKey.add(Constant.TRANSFER_UPC_TEXT);
            selectKey.add(Constant.TRANSFER_SERIAL_TEXT);
            selectKey.add(Constant.TRANSFER_LOT_TEXT);
            selectKey.add(Constant.TRANSFER_BATCH_TEXT);
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_dropdown_item, selectKey);
            transferSelectKeyDrpDwn.setAdapter(adapter);
            //Check previous save Select key value
            if (!StringUtils.isEmpty(sharedpreferences.getString(Constant.TRANSFER_SELECT_KEY_VALUE, ""))) {
                for (int i = 0; i < selectKey.size(); i++) {
                    if (selectKey.get(i).equalsIgnoreCase(sharedpreferences.getString(Constant.TRANSFER_SELECT_KEY_VALUE, ""))) {
                        transferSelectKeyDrpDwn.setSelection(i);
                        if(selectKey.get(i).equalsIgnoreCase(Constant.TRANSFER_SERIAL_TEXT)){
                            transferLotLayout.setVisibility(View.VISIBLE);
                            transferLotTxt.setVisibility(View.GONE);
                            transferVerifyLotCheckboxLayout.setVisibility(View.VISIBLE);
                            makeQtyOneForSerial();
                        }
                        break;
                    }
                }
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }

    }
    //Call web service for Transsfer Pl from
    private void getTransferPLFromLocation(){
        try{
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("CompanyDbName", companyDBName);
            jsonObject.put("Whid", warehouseID);
            jsonObject.put("UserId", userID);
            jsonObject.put("Key", passCode);
            jsonString = jsonObject.toString();
            makeUrl = service_url + "/Lanz_TransferGetPickLocation";
            new TransferPLFromLocation().execute(makeUrl);
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    // Get Transfer  PL From Location
    public class TransferPLFromLocation extends AsyncTask<String, String, List<TransferPLFromLocationEntity>> {
        @Override
        protected List<TransferPLFromLocationEntity> doInBackground(String... params) {
            TransferPLFromService transferPLFromService =new TransferPLFromService();
            return  transferPLFromService.getPlToList(params[0],jsonString);
        }
        @Override
        protected void onPostExecute(List<TransferPLFromLocationEntity> transferPLFromLocationEntities) {
            try {
                //System.out.println("Transfer Picking Location To" + "===" + transferPLToLocationEntities);
                if(transferPLFromLocationEntities!=null && transferPLFromLocationEntities.size()>0){
                    bindTransferPlFromDropDown(transferPLFromLocationEntities);
                }
            }catch (Exception ex){
                ex.printStackTrace();
            }
        }
    }

    //Bind Transfer Pl from Dropdown with value
    private void bindTransferPlFromDropDown(List<TransferPLFromLocationEntity> transferPLFromLocationEntityList){
        try {
            if(transferPLFromLocationEntityList!=null && transferPLFromLocationEntityList.size()>0) {
                ArrayAdapter<TransferPLFromLocationEntity> adapter = new ArrayAdapter<TransferPLFromLocationEntity>(context, android.R.layout.simple_spinner_dropdown_item, transferPLFromLocationEntityList);
                transferPlFromDrpDwn.setAdapter(adapter);
                transferPLFromLocationEntityListRef =transferPLFromLocationEntityList;
                //Transfer PL From DropDown bind with data
                if (!transferPlFromDrpDwn.isEnabled()) {
                    for (int i = 0; i < transferPLFromLocationEntityList.size(); i++) {
                        if (transferPLFromLocationEntityList.get(i).getPickLocname().equalsIgnoreCase(sharedpreferences.getString(Constant.TRANSFER_PL_FROM_VALUE, ""))) {
                            transferPlFromDrpDwn.setSelection(i);
                            locationfromtxt.setText(sharedpreferences.getString(Constant.TRANSFER_PL_FROM_VALUE, ""));
                        }
                    }
                }
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }
    //Transfer PL TO Location based on warehouse
    private void getTransferPLToLocation(){
        try{
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("CompanyDbName", companyDBName);
            jsonObject.put("Whid", warehoiseIDPlTO);
            jsonObject.put("UserId", userID);
            jsonObject.put("Key", passCode);
            jsonString = jsonObject.toString();
            makeUrl = service_url + "/Lanz_TransferGetPickLocation";
            new TransferPLToLocation().execute(makeUrl);
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    // Get Transfer  PL TO Location
    public class TransferPLToLocation extends AsyncTask<String, String, List<TransferPLFromLocationEntity>> {
        @Override
        protected List<TransferPLFromLocationEntity> doInBackground(String... params) {
            TransferPLFromService transferPLFromService =new TransferPLFromService();
            return  transferPLFromService.getPlToList(params[0],jsonString);
        }
        @Override
        protected void onPostExecute(List<TransferPLFromLocationEntity> transferPLToLocationEntities) {
            try {
                //System.out.println("Transfer Picking Location To" + "===" + transferPLToLocationEntities);
                if(transferPLToLocationEntities!=null && transferPLToLocationEntities.size()>0){
                    bindTransferPlToDropDown(transferPLToLocationEntities);
                }else{
                    requestFocus();
                }
            }catch (Exception ex){
                ex.printStackTrace();
            }
        }
    }

    //Populate transfer Pl to dropdown with value
    private void bindTransferPlToDropDown(List<TransferPLFromLocationEntity> transferPLFromLocationEntityList){
        try {
            if(transferPLFromLocationEntityList!=null && transferPLFromLocationEntityList.size()>0) {
                ArrayAdapter<TransferPLFromLocationEntity> adapter = new ArrayAdapter<TransferPLFromLocationEntity>(context, android.R.layout.simple_spinner_dropdown_item, transferPLFromLocationEntityList);
                transferPlToDrpDwn.setAdapter(adapter);
                transferPLToLocationEntityListRef =transferPLFromLocationEntityList;
                //Transfer PL To DropDown bind with data
                if (!transferPlToDrpDwn.isEnabled()) {
                    for (int i = 0; i < transferPLFromLocationEntityList.size(); i++) {
                        if (transferPLFromLocationEntityList.get(i).getPickLocname().equalsIgnoreCase(sharedpreferences.getString(Constant.TRANSFER_PL_TO_VALUE, ""))) {
                            transferPlToDrpDwn.setSelection(i);
                            locationtotxt.setText(sharedpreferences.getString(Constant.TRANSFER_PL_TO_VALUE, ""));
                        }
                    }
                }
            }
            requestFocus();
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }
    //Warehouse List
    private void getTransferWarehouseList(){
        try{
            makeUrl = service_url+ "/lanz_Warehouselist/" + companyDBName + "/" + userID + "/" + warehouseID + "/" + passCode;
            new TransferWareHouseList().execute(makeUrl);
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    // Get Transfer  Warehouse
    public class TransferWareHouseList extends AsyncTask<String, String, List<TransferWarehouseToEntity>> {
        @Override
        protected List<TransferWarehouseToEntity> doInBackground(String... params) {
            TransferWarehouseToService transferWarehouseToService=new TransferWarehouseToService();
            return  transferWarehouseToService.getTransferWarehouseList(params[0]);
        }
        @Override
        protected void onPostExecute(List<TransferWarehouseToEntity> transferWarehouseToEntities) {
            try {
                //System.out.println("Transfer Picking Location To" + "===" + transferPLToLocationEntities);
                if(transferWarehouseToEntities!=null && transferWarehouseToEntities.size()>0){
                    bindTransferWarehouseList(transferWarehouseToEntities);
                }
            }catch (Exception ex){
                ex.printStackTrace();
            }
        }
    }

    //Populate warehouse list with value
    private void bindTransferWarehouseList(List<TransferWarehouseToEntity> transferWarehouseToEntityList){
        try {
            ArrayAdapter<TransferWarehouseToEntity> adapter = new ArrayAdapter<TransferWarehouseToEntity>(context, android.R.layout.simple_spinner_dropdown_item, transferWarehouseToEntityList);
            transferWarehouseToDrpDwn.setAdapter(adapter);
            if (!transferWarehouseToDrpDwn.isEnabled()) {
                for (int i = 0; i < transferWarehouseToEntityList.size(); i++) {
                    if (transferWarehouseToEntityList.get(i).getWareHouseName().equalsIgnoreCase(sharedpreferences.getString(Constant.TRANSFER_WAREHOUSE_TO_VALUE, ""))) {
                        transferWarehouseToDrpDwn.setSelection(i);
                        warehoiseIDPlTO=Integer.parseInt(transferWarehouseToEntityList.get(i).getWareHouseID());
                        getTransferPLToLocation();
                    }
                }
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    // set transfer screen field focus priority
    private void requestFocus(){
        try {

            TransferWarehouseToEntity transferWarehouseToEntity=(TransferWarehouseToEntity)transferWarehouseToDrpDwn.getSelectedItem();
            TransferPLFromLocationEntity transferPLFromLocationEntity;
            // PL From check
            transferPLFromLocationEntity = (TransferPLFromLocationEntity) transferPlFromDrpDwn.getSelectedItem();
            //PL From Focus
            if(transferPLFromLocationEntity!=null) {
                if ((transferPlFromDrpDwn.isEnabled() && Integer.parseInt(transferPLFromLocationEntity.getPicklocid()) == 0) || (locationfromtxt.isEnabled() && StringUtils.isEmpty(locationfromtxt.getText()))) {
                    //transferPlFromDrpDwn.requestFocus();
                    locationfromtxt.requestFocus();
                    return;
                }
            }else{
                locationfromtxt.requestFocus();
                return;
            }

            //Warehouse To Focus
            if (transferWarehouseToDrpDwn.isEnabled() && Integer.parseInt(transferWarehouseToEntity.getWareHouseID())==0) {
                transferWarehouseToDrpDwn.requestFocus();
                return;
            }
            //Location To text
            transferPLFromLocationEntity = (TransferPLFromLocationEntity) transferPlToDrpDwn.getSelectedItem();
            if(transferPLFromLocationEntity!=null) {
                if ((transferPlToDrpDwn.isEnabled() && Integer.parseInt(transferPLFromLocationEntity.getPicklocid()) == 0) || (locationtotxt.isEnabled() && StringUtils.isEmpty(locationtotxt.getText()))) {
                    locationtotxt.requestFocus();
                    return;
                }
            }else{
                locationtotxt.requestFocus();
                return;
            }

            //Transfer Reason Text
            if (transferReasontxt.isEnabled() && StringUtils.isEmpty(transferReasontxt.getText())) {
                transferReasontxt.requestFocus();
                return;
            }
            //Transfer Quantity Text
            if (transferQtytxt.isEnabled() && StringUtils.isEmpty(transferQtytxt.getText())) {
                transferQtytxt.requestFocus();
                return;
            }

            //Transfer Product Search Focus
            if (StringUtils.isEmpty(transferSearchtxt.getText())) {
                //System.out.println("ff");
                transferSearchtxt.requestFocus();
                //System.out.println("F");
                return;
            }
            //verifyLotCheckbox Validation
            if(verifyLotCheckbox.isChecked()){
                if(StringUtils.isEmpty(transferLotTxt.getText().toString())){
                    transferLotTxt.requestFocus();
                    return;
                }
            }
        }catch (Exception ex){
            ex.printStackTrace();
            return;
        }

        submitTransfer();// Submit transfer
    }

    // Transfer screen submit validation check
    private Boolean transferValidationCheck(){
        try {
            TransferPLFromLocationEntity transferPLFromLocationEntity;
            // PL From check
            transferPLFromLocationEntity = (TransferPLFromLocationEntity) transferPlFromDrpDwn.getSelectedItem();
            if (Integer.parseInt(transferPLFromLocationEntity.getPicklocid()) == 0 || StringUtils.isEmpty(locationfromtxt.getText())) {
                Toast.makeText(context, Constant.TRANSFER_PLFROM_ERROR, Toast.LENGTH_SHORT).show();
                locationfromtxt.setText("");
                transferPlFromDrpDwn.setSelection(0);
                vibratorAdaptor.makeVibration();
                return false;
            }else{
                if(transferPLFromLocationEntity.getPickLocname().equals(locationfromtxt.getText().toString())) {
                    plFromName = transferPLFromLocationEntity.getPickLocname();
                }else{
                    Toast.makeText(context, "Please Select Correct PL From", Toast.LENGTH_SHORT).show();
                    locationfromtxt.setText("");
                    transferPlFromDrpDwn.setSelection(0);
                    vibratorAdaptor.makeVibration();
                    return false;
                }
            }
            //Warehouse To check
            TransferWarehouseToEntity transferWarehouseToEntity = (TransferWarehouseToEntity) transferWarehouseToDrpDwn.getSelectedItem();
            if (Integer.parseInt(transferWarehouseToEntity.getWareHouseID()) == 0) {
                Toast.makeText(context, Constant.TRANSFER_WAREHOUSETO_ERROR, Toast.LENGTH_SHORT).show();
                vibratorAdaptor.makeVibration();
                return false;
            }else{
                warehouseToID=transferWarehouseToEntity.getWareHouseID();
            }
            //PL To check
            transferPLFromLocationEntity = (TransferPLFromLocationEntity) transferPlToDrpDwn.getSelectedItem();
            if (Integer.parseInt(transferPLFromLocationEntity.getPicklocid()) == 0 || StringUtils.isEmpty(locationtotxt.getText())) {
                Toast.makeText(context, Constant.TRANSFER_PLTO_ERROR, Toast.LENGTH_SHORT).show();
                locationtotxt.setText("");
                transferPlToDrpDwn.setSelection(0);
                vibratorAdaptor.makeVibration();
                return false;
            }else{
                if(transferPLFromLocationEntity.getPickLocname().equals(locationtotxt.getText().toString())) {
                    plToName = transferPLFromLocationEntity.getPickLocname();
                }else{
                    Toast.makeText(context, "Please Select Correct PL To", Toast.LENGTH_SHORT).show();
                    locationtotxt.setText("");
                    transferPlToDrpDwn.setSelection(0);
                    vibratorAdaptor.makeVibration();
                    return false;
                }
            }
            //Reason Check
            if (transferReasontxt.isEnabled() && StringUtils.isEmpty(transferReasontxt.getText())) {
                Toast.makeText(context, Constant.TRANSFER_REASON_ERROR, Toast.LENGTH_SHORT).show();
                vibratorAdaptor.makeVibration();
                return false;
            }
            //Quantity check
            if (StringUtils.isEmpty(transferQtytxt.getText())) {
                Toast.makeText(context, Constant.TRANSFER_QUANTITY_ERROR, Toast.LENGTH_SHORT).show();
                vibratorAdaptor.makeVibration();
                return false;
            }

            if (Integer.parseInt(transferQtytxt.getText().toString()) == 0) {
                Toast.makeText(context, Constant.TRANSFER_QUANTITY_ERROR, Toast.LENGTH_SHORT).show();
                vibratorAdaptor.makeVibration();
                return false;
            }
            //Select Key
            String selectKey=(String)transferSelectKeyDrpDwn.getSelectedItem();
            if(!StringUtils.isEmpty(selectKey)){
                selectKeyName=selectKey;
            }
            //Product Search Text check
            if (StringUtils.isEmpty(transferSearchtxt.getText())) {
                Toast.makeText(context, Constant.TRANSFER_SEARCH_TEXT_ERROR, Toast.LENGTH_SHORT).show();
                vibratorAdaptor.makeVibration();
                return false;
            }
            //Verify Lot Validation
            if(verifyLotCheckbox.isChecked()){
                if(StringUtils.isEmpty(transferLotTxt.getText().toString())){
                    Toast.makeText(context, "Enter Lot", Toast.LENGTH_SHORT).show();
                    vibratorAdaptor.makeVibration();
                }
            }
        }catch (Exception ex){
            ex.printStackTrace();
            return false;
        }
        return true;
    }

    // transfer scrren transfer button click event
    private void submitTransfer(){
        try {
            if (transferValidationCheck()) {
                saveTransferData();
            } else {
                requestFocus();
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    // json data for submit transfer (Product Location Check)
    private void saveTransferData(){
        try{
            JSONObject jsonObject=new JSONObject();
            jsonObject.put("CompanyDbName",companyDBName);
            jsonObject.put("Userid",userID);
            jsonObject.put("Whid",warehouseID);
            jsonObject.put("Selectkey",selectKeyName);
            jsonObject.put("Searchkey",transferSearchtxt.getText().toString());
            jsonObject.put("Picklocname",plFromName);
            jsonObject.put("quantity",transferQtytxt.getText().toString());
            jsonObject.put("Picklocto",plToName);
            jsonObject.put("Whidto",warehouseToID);
            jsonObject.put("key",passCode);
            jsonString=jsonObject.toString();
            makeUrl=service_url + "/Lanz_Checkproductlocation";
            new TransferCheckProductLocation().execute(makeUrl);

        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    // Submit Transfer Data ( Web Service Call)
    public class TransferCheckProductLocation extends AsyncTask<String, String, TransferCheckProductLocationEntity> {
        @Override
        protected TransferCheckProductLocationEntity doInBackground(String... params) {
            TransferCheckProductLocationService transferCheckProductLocationService=new TransferCheckProductLocationService();
            return  transferCheckProductLocationService.getCheckProductLocation(params[0],jsonString);
        }
        @Override
        protected void onPostExecute(TransferCheckProductLocationEntity transferCheckProductLocationEntity ) {
            try {
                if(transferCheckProductLocationEntity!=null){
                    if(Integer.parseInt(transferCheckProductLocationEntity.getId())==0){
                        if(transferCheckProductLocationEntity.getLotno().equalsIgnoreCase("Product assigned for serial")){
                            Toast.makeText(context, transferCheckProductLocationEntity.getLotno()+" Please Select Serial#", Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(context, transferCheckProductLocationEntity.getLotno(), Toast.LENGTH_SHORT).show();
                        }
                        vibratorAdaptor.makeVibration();
                    }

                    if(selectKeyName.equalsIgnoreCase(Constant.TRANSFER_SERIAL_TEXT) && Integer.parseInt(transferCheckProductLocationEntity.getId())!=0){
                        transferQtytxt.setText("1");
                        if(verifyLotCheckbox.isChecked()){
                            //System.out.println(transferCheckProductLocationEntity.getLotno()+"=="+transferLotTxt.getText().toString());
                            if(!transferCheckProductLocationEntity.getLotno().equals(transferLotTxt.getText().toString())){
                                Toast.makeText(context, "Lot Doesn't Match", Toast.LENGTH_SHORT).show();
                                vibratorAdaptor.makeVibration();
                                return;
                            }
                        }
                    }

                    if(Integer.parseInt(transferCheckProductLocationEntity.getId())!=0){
                        //System.out.println("as");
                        postTransfer(transferCheckProductLocationEntity.getProductid(), transferCheckProductLocationEntity.getComboid());
                    }
                }else{
                    Toast.makeText(context, Constant.SERVER_ERROR, Toast.LENGTH_SHORT).show();
                    vibratorAdaptor.makeVibration();
                }
                //System.out.println("Transfer Picking Location To" + "===" + transferPLToLocationEntities);

            }catch (Exception ex){
                ex.printStackTrace();
            }
        }
    }
    // json data for submit transfer after product location check
    private void postTransfer(String productID,String combID){
        try{
           JSONObject jsonObject=new JSONObject();
            jsonObject.put("CompanyDbName",companyDBName);
            jsonObject.put("Userid",userID);
            jsonObject.put("Whid",warehouseID);
            jsonObject.put("Selectkey",selectKeyName);
            jsonObject.put("Searchkey",transferSearchtxt.getText().toString());
            jsonObject.put("Picklocname",plFromName);
            jsonObject.put("quantity",transferQtytxt.getText().toString());
            jsonObject.put("Picklocto",plToName);
            jsonObject.put("Whidto",warehouseToID);
            jsonObject.put("Reason",transferReasontxt.getText().toString());
            jsonObject.put("Productid",productID);
            jsonObject.put("combid",combID);
            jsonObject.put("key",passCode);
            jsonString=jsonObject.toString();
            makeUrl=service_url + "/Lanz_Posttransfer";
            new PostTransferSubmit().execute(makeUrl);
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    // Submit Transfer Data (Web Service Call)
    public class PostTransferSubmit extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... params) {
            TransferPostSubmitService transferPostSubmitService=new TransferPostSubmitService();
            return  transferPostSubmitService.getPostSubmitData(params[0],jsonString);
        }
        @Override
        protected void onPostExecute(String status ) {
            try {
                if(!StringUtils.isEmpty(status)){
                    Toast.makeText(context, status, Toast.LENGTH_SHORT).show();
                    vibratorAdaptor.makeVibration();

                }else{
                    Toast.makeText(context, Constant.SERVER_ERROR, Toast.LENGTH_SHORT).show();
                    vibratorAdaptor.makeVibration();
                }
                checkTransferSavedSettings(); // Check Transfer Previous Saved data
                transferSearchtxt.setText("");
                requestFocus(); //Current Focus Field

            }catch (Exception ex){
                ex.printStackTrace();
            }
        }
    }

    // hide Device keyboard
    public  void hideKeyboard(Context ctx) {
        InputMethodManager imm = (InputMethodManager)ctx.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(cView.getWindowToken(), 0);
    }

    //Loading Check For Scan
    public class AsynLoading extends AsyncTask<String, String, Boolean> {
        @Override
        protected Boolean doInBackground(String... params) {
            AsynLoadingService asynLoadingService=new AsynLoadingService();
            return asynLoadingService.getLoadingStatus();
        }

        @Override
        protected void onPostExecute(Boolean status) {
            try {
                requestFocus();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
    //make Quantity 1 for serial
    private void makeQtyOneForSerial(){
        transferQtytxt.setText("1");
    }
    //isVerifyLotEnableForTransfer Logic with Web Service Call
    private void isVeryLotEnableForTransfer(){
        try{
            JSONObject jsonObject=new JSONObject();
            jsonObject.put("CompanyDbName",companyDBName);
            jsonObject.put("Userid",userID);
            jsonObject.put("Whid",warehouseID);
            jsonObject.put("key",passCode);
            jsonString=jsonObject.toString();
            makeUrl=service_url +"/"+ "LanzVerifyLotOrNot";
            new VerifyLotForTransfer().execute(makeUrl);

        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    //Verify Lot(Visible or Disable) layout based on Serial
    public class VerifyLotForTransfer extends AsyncTask<String, String, Boolean> {
        @Override
        protected Boolean doInBackground(String... params) {
            VerifyLotForTransferService verifyLotForTransferService=new VerifyLotForTransferService();
            return verifyLotForTransferService.isVerifyLotForTransfer(makeUrl,jsonString);
        }

        @Override
        protected void onPostExecute(Boolean status) {
            try {
                if(status!=null) {
                    if (status) {
                        transferLotLayout.setVisibility(View.VISIBLE);
                        transferLotTxt.setVisibility(View.GONE);
                        transferVerifyLotCheckboxLayout.setVisibility(View.VISIBLE);
                    } else {
                        transferLotLayout.setVisibility(View.GONE);
                        verifyLotCheckbox.setChecked(false);
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
}
