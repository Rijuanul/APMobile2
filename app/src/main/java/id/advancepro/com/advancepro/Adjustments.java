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
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import id.advancepro.com.advancepro.adapter.VibratorAdaptor;
import id.advancepro.com.advancepro.model.AdjustmentProductCheckEntity;
import id.advancepro.com.advancepro.model.AdjustmentsPickLocationEntity;
import id.advancepro.com.advancepro.model.NegAdjustmentProductEntity;
import id.advancepro.com.advancepro.service.AdjustmentPickingLocationService;
import id.advancepro.com.advancepro.service.AdjustmentProductSearchService;
import id.advancepro.com.advancepro.service.AdjustmentReduceQtyForPLService;
import id.advancepro.com.advancepro.service.AsynLoadingService;
import id.advancepro.com.advancepro.service.InternetConnection;
import id.advancepro.com.advancepro.service.NegAdjustmentProductSearchService;
import id.advancepro.com.advancepro.service.SubmitNegativeAdjustmentService;
import id.advancepro.com.advancepro.service.SubmitPositiveAdjustmentService;
import id.advancepro.com.advancepro.utils.Constant;

;

/**
 * Created by TASIV on 3/24/2017.
 */

public class Adjustments extends Fragment {

    private View rootView;
    private InternetConnection intConn;
    private SharedPreferences sharedpreferences;
    private String makeUrl;
    private String passCode;
    private String service_url;
    private String companyDBName;
    private Integer userID;
    private Integer warehouseID;
    private Boolean checkInternet = false;
    private String jsonString;
    private EditText adjustmentsSearchtxt;
    private EditText adjustmentsLottxt;
    private EditText adjustmentsSerialtxt;
    private EditText adjustmentsQtytxt;
    private EditText adjustmentsReasontxt;
    private LinearLayout locationLock;
    private LinearLayout locationUnlock;
    private LinearLayout lotLock;
    private LinearLayout lotUnlock;
    private LinearLayout serialLock;
    private LinearLayout serialUnlock;
    private LinearLayout qtyLock;
    private LinearLayout qtyUnlock;
    private LinearLayout reasonLock;
    private LinearLayout reasonUnlock;
    private LinearLayout adjustmentsPosBtnLayout;
    private LinearLayout adjustmentsNegBtnLayout;
    private LinearLayout adjustmentsPosBtnSubmitLayout;
    private LinearLayout adjustmentsNegBtnSubmitLayout;
    private LinearLayout adjustmentsTotalQtylayout;
    private Button adjustmentsSubmitPositive;
    private Button adjustmentsSubmitNegative;
    private Button adjustmentPosBtn;
    private Button adjustmentNegBtn;
    private Spinner adjustmentLocationSpinner;
    private TextView adjustmentHeading;
    private TextView adjustmentTotalQtytext;
    private Spinner adjustmentSelectKeyDropDown;
    private Boolean pickingLocationInitialize;
    private String selectKey;
    private Context context;
    private Boolean isPosAdjustment;
    private List<AdjustmentProductCheckEntity> adjustmentProductCheckEntityList;
    private Integer defaultPickingLocationNumber;
    private String pickingLocationID;
    private String pickingLocationName;
    private JSONArray jsonArray;
    private VibratorAdaptor vibratorAdaptor;
    private EditText locationtxt;
    private List<AdjustmentsPickLocationEntity> adjustmentsPickLocationEntityListRef;
    private View cView;
    private Context contex;



    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        try {
            rootView = inflater.inflate(R.layout.adjustments, container, false);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return rootView;
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        try {
            super.onViewCreated(view, savedInstanceState);
            //SharedPreferences Value
            sharedpreferences = this.getActivity().getSharedPreferences(Constant.LOGIN_PREF, Context.MODE_PRIVATE);
            passCode = sharedpreferences.getString(Constant.PASS_CODE, null);
            service_url = sharedpreferences.getString(Constant.SERVICE_URL, null);
            companyDBName = sharedpreferences.getString(Constant.LOGIN_DB, null);
            userID = Integer.parseInt(sharedpreferences.getString(Constant.LOGIN_USERID, "0"));
            warehouseID = Integer.parseInt(sharedpreferences.getString(Constant.DEFAULT_WAREHOUSE_ID, "0"));

            cView = view; //Current View
            contex = getContext(); //Current Context

            intConn = new InternetConnection();//Internet Connection Initialize
            pickingLocationInitialize = false; //Picking Location make false for first time page loading
            isPosAdjustment = true; // Positive Adjusment flag
            //Vibrator
            vibratorAdaptor = new VibratorAdaptor(getContext()); //Vibrator Class Initialize
            //Adjustment Screen Field Initialize
            adjustmentHeading = (TextView) view.findViewById(R.id.adjustmentsheading);
            adjustmentPosBtn = (Button) view.findViewById(R.id.adjustmentposbtn);
            adjustmentNegBtn = (Button) view.findViewById(R.id.adjustmentnegbtn);

            adjustmentsSubmitPositive = (Button) view.findViewById(R.id.submitadjustmentspos);
            adjustmentsSubmitNegative = (Button) view.findViewById(R.id.submitadjustmentsneg);
            adjustmentsPosBtnLayout = (LinearLayout) view.findViewById(R.id.adjustmentposbtnlayout);
            adjustmentsNegBtnLayout = (LinearLayout) view.findViewById(R.id.adjustmentnegbtnlayout);
            adjustmentsNegBtnLayout.setVisibility(View.GONE);

            adjustmentsTotalQtylayout = (LinearLayout) view.findViewById(R.id.adjustmentstotalqtylayout);
            adjustmentsTotalQtylayout.setVisibility(View.GONE);
            adjustmentTotalQtytext = (TextView) view.findViewById(R.id.adjustmentstotalqtytxt);

            adjustmentsPosBtnSubmitLayout = (LinearLayout) view.findViewById(R.id.adjustmentspossubmitlayout);
            adjustmentsNegBtnSubmitLayout = (LinearLayout) view.findViewById(R.id.adjustmentsnegsubmitlayout);
            adjustmentsNegBtnSubmitLayout.setVisibility(View.GONE);

            adjustmentLocationSpinner = (Spinner) view.findViewById(R.id.adjustmentslocationdrpdwn);
            adjustmentSelectKeyDropDown = (Spinner) view.findViewById(R.id.adjustmentselectkey);

            locationtxt = (EditText) view.findViewById(R.id.adjustmentslocationtxt);
            adjustmentsSearchtxt = (EditText) view.findViewById(R.id.adjustmentssearchtext);
            adjustmentsLottxt = (EditText) view.findViewById(R.id.adjustmentslot);
            adjustmentsSerialtxt = (EditText) view.findViewById(R.id.adjustmentsserialtxt);
            adjustmentsQtytxt = (EditText) view.findViewById(R.id.adjustmentqty);
            adjustmentsReasontxt = (EditText) view.findViewById(R.id.adjustmentsreasontxt);

            locationLock = (LinearLayout) view.findViewById(R.id.adjustlocationlocklayout);//Picking Location Lock Layout
            locationUnlock = (LinearLayout) view.findViewById(R.id.adjustlocationunlocklayout);//Picking Location UnLock Layout
            lotLock = (LinearLayout) view.findViewById(R.id.adjustmentslotlocklayout);//Lot Lock Layout
            lotUnlock = (LinearLayout) view.findViewById(R.id.adjustmentslotunlockimglayout); //Lot UnLock Layout
            serialLock = (LinearLayout) view.findViewById(R.id.adjustmentsseriallocklayout);// Serial Lock Layout
            serialUnlock = (LinearLayout) view.findViewById(R.id.adjustmentsserialunlockimglayout);// Serial UnLock Layout
            qtyLock = (LinearLayout) view.findViewById(R.id.adjustmentsqtyocklayout); //Quantity Lock Layout
            qtyUnlock = (LinearLayout) view.findViewById(R.id.adjustmentsqtyunlockimglayout); //Quantity UnLock Layout
            reasonLock = (LinearLayout) view.findViewById(R.id.adjustmentsreasonlocklayout); //Reason Lock Layout
            reasonUnlock = (LinearLayout) view.findViewById(R.id.adjustmentsreasonunlockimglayout); //Reason Unlock Layout


            context = this.getActivity().getApplicationContext();// Current Application Context
            hideKeyboard(contex); // Hide Keyboard
            //Adjustment Screen Positive Button Click
            adjustmentPosBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    adjustmentsNegBtnLayout.setVisibility(View.VISIBLE);
                    adjustmentsPosBtnLayout.setVisibility(View.GONE);
                    adjustmentHeading.setText("Adjustment is Negative");
                    adjustmentsPosBtnSubmitLayout.setVisibility(View.GONE);
                    adjustmentsNegBtnSubmitLayout.setVisibility(View.VISIBLE);
                    hideLockForNNegAdjustments();
                    isPosAdjustment = false;
                    commonSettingsForPosAndNegBtn();
                    requestToFocusField();


                }
            });
            //Adjustment Screen Negative Button Click
            adjustmentNegBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    adjustmentsPosBtnLayout.setVisibility(View.VISIBLE);
                    adjustmentsNegBtnLayout.setVisibility(View.GONE);
                    adjustmentHeading.setText("Adjustment is Positive");
                    adjustmentsPosBtnSubmitLayout.setVisibility(View.VISIBLE);
                    adjustmentsNegBtnSubmitLayout.setVisibility(View.GONE);
                    isPosAdjustment = true;
                    adjustmentLockUnlockSaved();
                    commonSettingsForPosAndNegBtn();
                    requestToFocusField();

                }
            });
            //Picking Location Lock Logic
            locationLock.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    locationLock.setVisibility(View.GONE);
                    locationUnlock.setVisibility(View.VISIBLE);
                    adjustmentLocationSpinner.setEnabled(false);
                    locationtxt.setEnabled(false);
                    requestToFocusField();
                    /*AdjustmentsPickLocationEntity adjustmentsPickLocationEntity=(AdjustmentsPickLocationEntity) adjustmentLocationSpinner.getSelectedItem();
                    System.out.println(adjustmentsPickLocationEntity.getPicklocname()+"=="+adjustmentsPickLocationEntity.getPickLocNameid());*/
                }
            });
            //Picking Location UnLock Logic
            locationUnlock.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    locationUnlock.setVisibility(View.GONE);
                    locationLock.setVisibility(View.VISIBLE);
                    adjustmentLocationSpinner.setEnabled(true);
                    locationtxt.setEnabled(true);
                    //locationtxt.requestFocus();
                    requestToFocusField();

                }
            });

            //Lot Lock Logic
            lotLock.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    lotLock.setVisibility(View.GONE);
                    lotUnlock.setVisibility(View.VISIBLE);
                    adjustmentsLottxt.setEnabled(false);
                    SharedPreferences.Editor editor = sharedpreferences.edit();
                    editor.putString(Constant.IS_ADJUSTMENT_LOT_LOCK, Constant.LOCK_TEXT);
                    editor.putString(Constant.ADJUSTMENT_LOT_VALUE, adjustmentsLottxt.getText().toString());
                    editor.commit();
                    requestToFocusField();
                }
            });
            //Lot UnLock Logic
            lotUnlock.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    lotUnlock.setVisibility(View.GONE);
                    lotLock.setVisibility(View.VISIBLE);
                    adjustmentsLottxt.setEnabled(true);
                    SharedPreferences.Editor editor = sharedpreferences.edit();
                    editor.putString(Constant.IS_ADJUSTMENT_LOT_LOCK, Constant.UNLOCK_TEXT);
                    editor.putString(Constant.ADJUSTMENT_LOT_VALUE, "");
                    editor.commit();
                    adjustmentsLottxt.setText("");
                    requestToFocusField();
                    //adjustmentsLottxt.requestFocus();
                }
            });
            // Serial Lock Logic
            serialLock.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    serialLock.setVisibility(View.GONE);
                    serialUnlock.setVisibility(View.VISIBLE);
                    adjustmentsSerialtxt.setEnabled(false);
                    SharedPreferences.Editor editor = sharedpreferences.edit();
                    editor.putString(Constant.IS_ADJUSTMENT_SERIAL_LOCK, Constant.LOCK_TEXT);
                    editor.putString(Constant.ADJUSTMENT_SERIAL_VALUE, adjustmentsSerialtxt.getText().toString());
                    editor.commit();
                    adjustmentsQtyLockMechanism();
                    //requestToFocusField();
                }
            });
            // Serial UnLock Logic
            serialUnlock.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    serialUnlock.setVisibility(View.GONE);
                    serialLock.setVisibility(View.VISIBLE);
                    adjustmentsSerialtxt.setEnabled(true);
                    SharedPreferences.Editor editor = sharedpreferences.edit();
                    editor.putString(Constant.IS_ADJUSTMENT_SERIAL_LOCK, Constant.UNLOCK_TEXT);
                    editor.putString(Constant.ADJUSTMENT_SERIAL_VALUE, "");
                    editor.commit();
                    adjustmentsQtyUnLockMechanism();
                    adjustmentsSerialtxt.setText("");
                    //requestToFocusField();
                    //adjustmentsSerialtxt.requestFocus();
                }
            });
            // Quantity Lock Logic
            qtyLock.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                   adjustmentsQtyLockMechanism();

                }
            });
            // Quantity UnLock Logic
            qtyUnlock.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    adjustmentsQtyUnLockMechanism();

                }
            });
            // Reason Lock Logic
            reasonLock.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    reasonLock.setVisibility(View.GONE);
                    reasonUnlock.setVisibility(View.VISIBLE);
                    adjustmentsReasontxt.setEnabled(false);

                    SharedPreferences.Editor editor = sharedpreferences.edit();
                    editor.putString(Constant.IS_ADJUSTMENT_REASON_LOCK, Constant.LOCK_TEXT);
                    editor.putString(Constant.ADJUSTMENT_REASON_VALUE, adjustmentsReasontxt.getText().toString());
                    editor.commit();
                    requestToFocusField();
                }
            });
            // Reason UnLock Logic
            reasonUnlock.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    reasonUnlock.setVisibility(View.GONE);
                    reasonLock.setVisibility(View.VISIBLE);
                    adjustmentsReasontxt.setEnabled(true);
                    adjustmentsReasontxt.setText("");
                    requestToFocusField();
                    //adjustmentsReasontxt.requestFocus();

                    SharedPreferences.Editor editor = sharedpreferences.edit();
                    editor.putString(Constant.IS_ADJUSTMENT_REASON_LOCK, Constant.UNLOCK_TEXT);
                    editor.putString(Constant.ADJUSTMENT_REASON_VALUE, "");
                    editor.commit();
                }
            });


            // Adjustment Search Product Event Logic
            adjustmentsSearchtxt.setOnKeyListener(new View.OnKeyListener() {
                @Override
                public boolean onKey(View v, int keyCode, KeyEvent event) {
                    if (keyCode == 66 && event.getAction() == KeyEvent.ACTION_UP) {
                        if (!StringUtils.isEmpty(adjustmentsSearchtxt.getText())) {
                            adjustmentProductCheck();
                            hideKeyboard(contex);
                        }
                    }
                    return false;
                }
            });
            // Adjustment SelectKey DroDown Logic
            adjustmentSelectKeyDropDown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    selectKey = parent.getSelectedItem().toString();
                    adjustmentsSearchtxt.setText("");
                    new AsynLoading().execute();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
            // Picking Location DropDown Logic
            adjustmentLocationSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if (!pickingLocationInitialize) {
                        pickingLocationInitialize = true;
                        return;
                    }
                    AdjustmentsPickLocationEntity adjustmentsPickLocationEntity = (AdjustmentsPickLocationEntity) parent.getSelectedItem();
                    pickingLocationID = adjustmentsPickLocationEntity.getPickLocNameid();
                    pickingLocationName = adjustmentsPickLocationEntity.getPicklocname();
                    locationtxt.setText(pickingLocationName);
                    requestToFocusField();

                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
            //Adjustments Lot Text Field Click Listener
            adjustmentsLottxt.setOnKeyListener(new View.OnKeyListener() {
                @Override
                public boolean onKey(View v, int keyCode, KeyEvent event) {
                    if (keyCode == 66 && event.getAction() == KeyEvent.ACTION_UP) {
                        hideKeyboard(contex);
                        new AsynLoading().execute();
                    }
                    return false;
                }
            });
            //Adjustment Serial click listener event
            adjustmentsSerialtxt.setOnKeyListener(new View.OnKeyListener() {
                @Override
                public boolean onKey(View v, int keyCode, KeyEvent event) {

                    if (keyCode == 66 && event.getAction() == KeyEvent.ACTION_UP) {
                        hideKeyboard(contex);
                        adjustmentsQtyLockMechanism();
                        new AsynLoading().execute();
                    }
                    return false;
                }
            });

            //Adjustment Quantity click listener event
            adjustmentsQtytxt.setOnKeyListener(new View.OnKeyListener() {
                @Override
                public boolean onKey(View v, int keyCode, KeyEvent event) {
                    if (keyCode == 66 && event.getAction() == KeyEvent.ACTION_UP) {
                        hideKeyboard(contex);
                        new AsynLoading().execute();
                    }

                    return false;
                }
            });
            //Adjustments Reason On key Listener
            adjustmentsReasontxt.setOnKeyListener(new View.OnKeyListener() {
                @Override
                public boolean onKey(View v, int keyCode, KeyEvent event) {
                    if (keyCode == 66 && event.getAction() == KeyEvent.ACTION_UP) {
                        hideKeyboard(contex);
                        new AsynLoading().execute();
                    }
                    return false;
                }
            });
            //Adjustment Positive Button Submit
            adjustmentsSubmitPositive.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!StringUtils.isEmpty(adjustmentsSearchtxt.getText())) {
                        submitPositiveAdjustments();
                    }
                }
            });
            //Adjustment Negative Button Submit
            adjustmentsSubmitNegative.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!StringUtils.isEmpty(adjustmentsSearchtxt.getText())) {
                        submitNegativeAdjustments();
                    }
                }
            });
            //location text field text change
            locationtxt.setOnKeyListener(new View.OnKeyListener() {
                @Override
                public boolean onKey(View v, int keyCode, KeyEvent event) {
                    if(keyCode==66 && event.getAction()==KeyEvent.ACTION_UP){
                        if (!StringUtils.isEmpty(locationtxt.getText())) {
                            Boolean matchFind = false;
                            if (adjustmentsPickLocationEntityListRef != null && adjustmentsPickLocationEntityListRef.size() > 0) {
                                for (int i = 0; i < adjustmentsPickLocationEntityListRef.size(); i++) {
                                    if (adjustmentsPickLocationEntityListRef.get(i).getPicklocname().equals(locationtxt.getText().toString())) {
                                        adjustmentLocationSpinner.setSelection(i);
                                        matchFind = true;
                                        break;
                                    }
                                }
                                if (!matchFind) {
                                    locationtxt.setText("");
                                }
                                hideKeyboard(contex);
                                new AsynLoading().execute();
                            }else{
                                locationtxt.setText("");
                            }
                        }
                    }
                    return false;
                }
            });
            // Load initial Data
            loadAdjustmentSelectKey(); // Load Adjustment Select Key
            adjustmentDefaultPickLocation(); // Load Adjustment Default Picking Location
            adjustmentLockUnlockSaved(); // Get Adjustment Previous Save data
            requestToFocusField(); // Current Focus Field

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    //Quantity Lock Mechanism
    private void adjustmentsQtyLockMechanism(){
        qtyLock.setVisibility(View.GONE);
        qtyUnlock.setVisibility(View.VISIBLE);
       // System.out.println("Hey:"+StringUtils.isEmpty(adjustmentsQtytxt.getText()) +"=="+!StringUtils.isEmpty(adjustmentsSerialtxt.getText()));
        if (StringUtils.isEmpty(adjustmentsQtytxt.getText()) || !StringUtils.isEmpty(adjustmentsSerialtxt.getText())) {
            adjustmentsQtytxt.setText("1");
            adjustmentsQtytxt.clearFocus();
        }
        adjustmentsQtytxt.setEnabled(false);
       //isInterneTConnected();
        requestToFocusField();
    }
    //Quantity Unlock Mechanism
    private void adjustmentsQtyUnLockMechanism() {
        if(StringUtils.isEmpty(adjustmentsSerialtxt.getText())) {
            qtyUnlock.setVisibility(View.GONE);
            qtyLock.setVisibility(View.VISIBLE);
            adjustmentsQtytxt.setEnabled(true);
            adjustmentsQtytxt.setText("");
           // adjustmentsQtytxt.requestFocus();
            vibratorAdaptor.makeVibration();
            requestToFocusField();
        }
        //isInterneTConnected();
    }
    //Common Settings For Positive And negative button
    private void commonSettingsForPosAndNegBtn() {
        adjustmentsSearchtxt.setText("");
        adjustmentsSearchtxt.requestFocus();
        adjustmentsTotalQtylayout.setVisibility(View.GONE);
        adjustmentTotalQtytext.setText("");

    }

    // Set Adjustment Select key
    private void loadAdjustmentSelectKey() {
        try {
            List<String> selectKey = new ArrayList<>();
            selectKey.add(Constant.SKU_TEXT);
            selectKey.add(Constant.UPC_TEXT);
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this.getActivity().getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, selectKey);
            adjustmentSelectKeyDropDown.setAdapter(adapter);
            adjustmentSelectKeyDropDown.setSelection(0);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    //Adjustment Default Picking location Text
    private void adjustmentDefaultPickLocation() {
        try {
            AdjustmentsPickLocationEntity adjustmentsPickLocationEntity = new AdjustmentsPickLocationEntity();
            adjustmentsPickLocationEntity.setPicklocname("--Select--");
            adjustmentsPickLocationEntity.setPickLocNameid("0");
            List<AdjustmentsPickLocationEntity> adjustmentsPickLocationListDefault = new ArrayList<>();
            adjustmentsPickLocationListDefault.add(adjustmentsPickLocationEntity);
            ArrayAdapter<AdjustmentsPickLocationEntity> adapter = new ArrayAdapter<AdjustmentsPickLocationEntity>(this.getActivity().getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, adjustmentsPickLocationListDefault);
            adjustmentLocationSpinner.setSelection(0);
            adjustmentLocationSpinner.setAdapter(adapter);
            locationLock.setVisibility(View.GONE);
            locationUnlock.setVisibility(View.GONE);
            pickingLocationInitialize = false;

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    // Check Adjustment Lock and Unlock Field Logic
    private void adjustmentLockUnlockSaved() {
        try {
            //Check Lot is lock or unlock
            if (!(StringUtils.isEmpty(sharedpreferences.getString(Constant.IS_ADJUSTMENT_LOT_LOCK, ""))) && (sharedpreferences.getString(Constant.IS_ADJUSTMENT_LOT_LOCK, "").equalsIgnoreCase(Constant.LOCK_TEXT))) {
                lotUnlock.setVisibility(View.VISIBLE);
                lotLock.setVisibility(View.GONE);
                adjustmentsLottxt.setText(sharedpreferences.getString(Constant.ADJUSTMENT_LOT_VALUE, ""));
                adjustmentsLottxt.setEnabled(false);
            } else {
                lotUnlock.setVisibility(View.GONE);
                lotLock.setVisibility(View.VISIBLE);
                adjustmentsLottxt.setText("");
                adjustmentsLottxt.setEnabled(true);
            }
            //Check Serial is lock or unlock
            if (!(StringUtils.isEmpty(sharedpreferences.getString(Constant.IS_ADJUSTMENT_SERIAL_LOCK, ""))) && (sharedpreferences.getString(Constant.IS_ADJUSTMENT_SERIAL_LOCK, "").equalsIgnoreCase(Constant.LOCK_TEXT))) {
                serialUnlock.setVisibility(View.VISIBLE);
                serialLock.setVisibility(View.GONE);
                adjustmentsSerialtxt.setText(sharedpreferences.getString(Constant.ADJUSTMENT_SERIAL_VALUE, ""));
                adjustmentsSerialtxt.setEnabled(false);
            } else {
                serialUnlock.setVisibility(View.GONE);
                serialLock.setVisibility(View.VISIBLE);
                adjustmentsSerialtxt.setText("");
                adjustmentsSerialtxt.setEnabled(true);
            }
            //Check Reason is lock or unlock
            if (!(StringUtils.isEmpty(sharedpreferences.getString(Constant.IS_ADJUSTMENT_REASON_LOCK, ""))) && (sharedpreferences.getString(Constant.IS_ADJUSTMENT_REASON_LOCK, "").equalsIgnoreCase(Constant.LOCK_TEXT))) {
                reasonUnlock.setVisibility(View.VISIBLE);
                reasonLock.setVisibility(View.GONE);
                adjustmentsReasontxt.setText(sharedpreferences.getString(Constant.ADJUSTMENT_REASON_VALUE, ""));
                adjustmentsReasontxt.setEnabled(false);
            } else {
                reasonUnlock.setVisibility(View.GONE);
                reasonLock.setVisibility(View.VISIBLE);
                adjustmentsReasontxt.setText("");
                adjustmentsReasontxt.setEnabled(true);
            }
            // Check Quantity field
            if (adjustmentsQtytxt.isEnabled()) {
                qtyUnlock.setVisibility(View.GONE);
                qtyLock.setVisibility(View.VISIBLE);
                adjustmentsQtytxt.setText("");
            }
            // Check Location Field
            if (adjustmentLocationSpinner.isEnabled()) {
                adjustmentDefaultPickLocation();
                locationtxt.setText("");
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    // Hide Lot and Serial Lock and Unlock image for Negative Adjustments
    private void hideLockForNNegAdjustments() {
        lotLock.setVisibility(View.GONE);
        lotUnlock.setVisibility(View.GONE);
        serialLock.setVisibility(View.GONE);
        serialUnlock.setVisibility(View.GONE);
        adjustmentsLottxt.setText("");
        adjustmentsSerialtxt.setText("");
        adjustmentsLottxt.setEnabled(true);
        adjustmentsSerialtxt.setEnabled(true);
    }

    // make Url for Picking Location
    private void getAdjustmentPickingLocation() {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("CompanyDbName", companyDBName);
            jsonObject.put("Whid", warehouseID);
            jsonObject.put("Searchkey", adjustmentsSearchtxt.getText());
            jsonObject.put("key", passCode);
            jsonString = jsonObject.toString();
            makeUrl = service_url + "/GetGlobalPickLocname";
            new AdjustmentPickingLocation().execute(makeUrl);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    // Get Adjustments Picking Location
    public class AdjustmentPickingLocation extends AsyncTask<String, String, List<AdjustmentsPickLocationEntity>> {
        @Override
        protected List<AdjustmentsPickLocationEntity> doInBackground(String... params) {
            AdjustmentPickingLocationService adjustmentPickingLocationService = new AdjustmentPickingLocationService();
            return adjustmentPickingLocationService.getAdjustmentPickingLocation(params[0], jsonString);

        }

        @Override
        protected void onPostExecute(List<AdjustmentsPickLocationEntity> pickingLocationList) {
            try {
                //System.out.println("Picking Location" + "===" + pickingLocationList);
                if (pickingLocationList != null && pickingLocationList.size() > 0) {
                    adjustmentsPickLocationEntityListRef = pickingLocationList;
                    if (adjustmentLocationSpinner.isEnabled()) {
                        ArrayAdapter<AdjustmentsPickLocationEntity> adapter = new ArrayAdapter<AdjustmentsPickLocationEntity>(context, android.R.layout.simple_spinner_dropdown_item, pickingLocationList);
                        adjustmentLocationSpinner.setAdapter(adapter);
                        getDefaultPickingLocationAndSetTotalQty(pickingLocationList);
                        adjustmentLocationSpinner.setSelection(defaultPickingLocationNumber);
                        locationLock.setVisibility(View.VISIBLE);
                        locationUnlock.setVisibility(View.GONE);
                        pickingLocationInitialize = false;
                    } else {
                        getDefaultPickingLocationAndSetTotalQty(pickingLocationList);
                    }
                } else {
                    Toast.makeText(context, Constant.NO_PICKING_LOCATION, Toast.LENGTH_SHORT).show();
                    adjustmentsSearchtxt.setText("");
                    locationtxt.setText("");
                    locationUnlock.setVisibility(View.GONE);
                    locationLock.setVisibility(View.VISIBLE);
                    adjustmentLocationSpinner.setEnabled(true);
                    locationtxt.setEnabled(true);
                    adjustmentDefaultPickLocation();
                    vibratorAdaptor.makeVibration();
                }
                requestToFocusField();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    //Get Default Picking Location And Set Total Quantity Value
    private void getDefaultPickingLocationAndSetTotalQty(List<AdjustmentsPickLocationEntity> adjustmentsPickLocationEntityList) {
        if (adjustmentLocationSpinner.isEnabled()) {
            for (int i = 0; i < adjustmentsPickLocationEntityList.size(); i++) {
                if (Integer.parseInt(adjustmentsPickLocationEntityList.get(i).getIsDefault()) == 1) {
                    //adjustmentLocationSpinner.setSelection(i);
                    defaultPickingLocationNumber = i;
                    pickingLocationID = adjustmentsPickLocationEntityList.get(i).getPickLocNameid();
                    pickingLocationName = adjustmentsPickLocationEntityList.get(i).getPicklocname();
                    adjustmentsTotalQtylayout.setVisibility(View.VISIBLE);
                    locationtxt.setText(pickingLocationName);
                    adjustmentTotalQtytext.setText("Total Quantity: " + adjustmentsPickLocationEntityList.get(i).getTotalQty());
                    break;
                }
            }
        } else {
            for (int i = 0; i < adjustmentsPickLocationEntityList.size(); i++) {
                if (adjustmentsPickLocationEntityList.get(i).getPickLocNameid().equalsIgnoreCase(pickingLocationID)) {
                    adjustmentsTotalQtylayout.setVisibility(View.VISIBLE);
                    adjustmentTotalQtytext.setText("Total Quantity: " + adjustmentsPickLocationEntityList.get(i).getTotalQty());
                    break;
                }
            }
        }
    }

    // make Url for Search Product
    private void adjustmentProductCheck() {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("CompanyDbName", companyDBName);
            jsonObject.put("Selectkey", selectKey);
            jsonObject.put("Searchkey", adjustmentsSearchtxt.getText());
            jsonObject.put("Key", passCode);
            jsonString = jsonObject.toString();
            makeUrl = service_url + "/Globaladjustproductcheck";
            new AdjustmentProductSearch().execute(makeUrl);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    // Get Adjustments Search Product
    public class AdjustmentProductSearch extends AsyncTask<String, String, List<AdjustmentProductCheckEntity>> {
        @Override
        protected List<AdjustmentProductCheckEntity> doInBackground(String... params) {
            AdjustmentProductSearchService adjustmentProductSearchService = new AdjustmentProductSearchService();
            return adjustmentProductSearchService.getAdjustmentSearchProduct(params[0], jsonString);
        }

        @Override
        protected void onPostExecute(List<AdjustmentProductCheckEntity> adjustmentProductCheckEntities) {
            try {
                //System.out.println("Adjustment Product Search" + "===" + adjustmentProductCheckEntities);
                if (adjustmentProductCheckEntities != null && adjustmentProductCheckEntities.size() > 0) {
                    adjustmentProductCheckEntityList = adjustmentProductCheckEntities;
                    getAdjustmentPickingLocation();
                    if (!isPosAdjustment) {
                        checkProductDetailsForNegAdjustment();
                    }
                } else {
                    Toast.makeText(context, Constant.NO_PRODUCT_FOUND_ADJUSTMENT, Toast.LENGTH_SHORT).show();
                    vibratorAdaptor.makeVibration();
                    adjustmentLockUnlockSaved();
                }
                requestToFocusField();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    //Get Product Details For Negative Adjustment
    private void checkProductDetailsForNegAdjustment() {
        try {
            makeUrl = service_url + "/CheckInBoundProductSerialLot/" + companyDBName + "/" + adjustmentProductCheckEntityList.get(0).getProductId() + "/" + passCode;
            new AdjustmentProductSearchNegAdjustment().execute(makeUrl);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    // Get Adjustments Search Product
    public class AdjustmentProductSearchNegAdjustment extends AsyncTask<String, String, List<NegAdjustmentProductEntity>> {
        @Override
        protected List<NegAdjustmentProductEntity> doInBackground(String... params) {

            NegAdjustmentProductSearchService negAdjustmentProductSearchService = new NegAdjustmentProductSearchService();
            return negAdjustmentProductSearchService.getProductDetails(params[0]);

        }

        @Override
        protected void onPostExecute(List<NegAdjustmentProductEntity> negAdjustmentProductEntityList) {
            try {
                //System.out.println("Negative Adjustment Product Search" + "===" + negAdjustmentProductEntityList);
                if (negAdjustmentProductEntityList != null && negAdjustmentProductEntityList.size() > 0) {
                    Integer negAdjProductLot = Integer.parseInt(negAdjustmentProductEntityList.get(0).getProductLot());
                    Integer negAdjProductSerial = Integer.parseInt(negAdjustmentProductEntityList.get(0).getProductSerial());
                    if (negAdjProductLot == 1 && negAdjProductSerial == 0) {
                        adjustmentsLottxt.setEnabled(true);
                        adjustmentsSerialtxt.setEnabled(false);

                    } else if (negAdjProductLot == 0 && negAdjProductSerial == 1) {
                        adjustmentsLottxt.setEnabled(false);
                        adjustmentsSerialtxt.setEnabled(true);

                    } else if (negAdjProductLot == 0 && negAdjProductSerial == 0) {
                        adjustmentsLottxt.setEnabled(false);
                        adjustmentsSerialtxt.setEnabled(false);
                    } else if (negAdjProductLot == 1 && negAdjProductSerial == 1) {
                        adjustmentsLottxt.setEnabled(true);
                        adjustmentsSerialtxt.setEnabled(true);
                    }
                }
                requestToFocusField();

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    //Submit Positive Adjustments
    private void submitPositiveAdjustments() {
        try {
            if (checkValidation()) {
                makeDataForPosAndNegAdjustments();
                makeUrl = service_url + "/GlobalAdjustmentseriallotAdd";
                new SubmitPositiveAdjustments().execute(makeUrl);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    //Positive Adjustments Service call for Submit
    public class SubmitPositiveAdjustments extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... params) {
            SubmitPositiveAdjustmentService submitPositiveAdjustmentService = new SubmitPositiveAdjustmentService();
            return submitPositiveAdjustmentService.submitPositiveAdjustment(params[0], jsonString);
        }

        @Override
        protected void onPostExecute(String status) {
            try {
                if (Integer.parseInt(status) == 1) {
                    Toast.makeText(context, Constant.PRODUCT_ADDED_SUCCESSFULL_ADJUSTMENT, Toast.LENGTH_SHORT).show();
                    vibratorAdaptor.makeVibration();
                    clearProductDeatilsList();
                    adjustmentLockUnlockSaved();
                    requestToFocusField();
                    commonSettingsForPosAndNegBtn();
                }
                //System.out.println("Adjustment Product Search" + "===" + adjustmentProductCheckEntities);

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    // Make the array for Positive And negative Adjustments
    private void makeDataForPosAndNegAdjustments() {
        try {

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("Picklocname", pickingLocationName);
            jsonObject.put("Serialno", adjustmentsSerialtxt.getText().toString());
            jsonObject.put("Lot", adjustmentsLottxt.getText().toString());
            jsonObject.put("Quantity", adjustmentsQtytxt.getText().toString());
            jsonArray = new JSONArray();
            jsonArray.put(jsonObject);
            //Final Json Object
            final JSONObject jsonObjectFinal = new JSONObject();
            jsonObjectFinal.put("CompanyDbName", companyDBName);
            jsonObjectFinal.put("Userid", userID);
            jsonObjectFinal.put("Productid", adjustmentProductCheckEntityList.get(0).getProductId());
            jsonObjectFinal.put("Combid", adjustmentProductCheckEntityList.get(0).getCombid());
            jsonObjectFinal.put("Reason", adjustmentsReasontxt.getText().toString());
            jsonObjectFinal.put("Whid", warehouseID);
            jsonObjectFinal.put("key", passCode);
            if (isPosAdjustment) {
                jsonObjectFinal.put("GlobalLotSerial", jsonArray);
                jsonObjectFinal.put("Picklocname", pickingLocationName);
            } else {
                jsonObjectFinal.put("LotSerial", jsonArray);
            }
            jsonString = jsonObjectFinal.toString();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    //Submit Negative Adjustments
    private void submitNegativeAdjustments() {
        try {
            if (checkValidation()) {
                checkReduceQtyFroPickLocation();
                //asd
                //System.out.println("NEG");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    //Negative Adjustments Service call for Submit
    public class SubmitNegativeAdjustments extends AsyncTask<String, String, Integer> {
        @Override
        protected Integer doInBackground(String... params) {
            SubmitNegativeAdjustmentService submitNegativeAdjustmentService = new SubmitNegativeAdjustmentService();
            return submitNegativeAdjustmentService.submitNegativeAdjustment(params[0], jsonString);
        }

        @Override
        protected void onPostExecute(Integer id) {
            try {
                if (id != null && id == 1) {
                    Toast.makeText(context, Constant.PRODUCT_REDUCED_SUCCESSFULL_ADJUSTMENT, Toast.LENGTH_SHORT).show();
                    vibratorAdaptor.makeVibration();
                    clearProductDeatilsList();
                    adjustmentLockUnlockSaved();
                    requestToFocusField();
                    commonSettingsForPosAndNegBtn();
                } else {
                    Toast.makeText(context, Constant.INVALID_LOT_SCANNED_ADJUSTMENT, Toast.LENGTH_SHORT).show();
                    vibratorAdaptor.makeVibration();
                    adjustmentsLottxt.setText("");
                    adjustmentsSerialtxt.setText("");
                }
                requestToFocusField();
                //System.out.println("Adjustment Product Search" + "===" + adjustmentProductCheckEntities);

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    // Clear Prroduct Details List
    private void clearProductDeatilsList() {
        try {
            adjustmentsSearchtxt.setText("");
            if (adjustmentProductCheckEntityList != null) {
                adjustmentProductCheckEntityList.clear();
            }
            adjustmentProductCheckEntityList = null;

        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    // Request to put Focus oon field
    private void requestToFocusField() {

        if (StringUtils.isEmpty(adjustmentsSearchtxt.getText())) {
            adjustmentsSearchtxt.requestFocus();
            return;
        }

        if (locationtxt.isEnabled() && StringUtils.isEmpty(locationtxt.getText())) {
            locationtxt.requestFocus();
            return;
        }

        if (adjustmentsLottxt.isEnabled() && StringUtils.isEmpty(adjustmentsLottxt.getText())) {
           // adjustmentsLottxt.setText("");
            //adjustmentsLottxt.setSelection(adjustmentsLottxt.getSelectionStart());
            adjustmentsLottxt.requestFocus();
            //System.out.println(adjustmentsLottxt.getSelectionStart());
            return;
        }

        if (adjustmentsSerialtxt.isEnabled() && StringUtils.isEmpty(adjustmentsSerialtxt.getText())) {
            adjustmentsSerialtxt.requestFocus();
            return;
        }

        if (adjustmentsQtytxt.isEnabled() && StringUtils.isEmpty(adjustmentsQtytxt.getText().toString())) {
            adjustmentsQtytxt.requestFocus();
            return;

        }

        if (StringUtils.isEmpty(adjustmentsReasontxt.getText().toString()) && adjustmentsReasontxt.isEnabled()) {
            adjustmentsReasontxt.requestFocus();
            return;
        }

        if (isPosAdjustment) {
            submitPositiveAdjustments();
        } else {
            submitNegativeAdjustments();
        }

    }

    //Validation Check for Adjustments
    private Boolean checkValidation() {
        if (StringUtils.isEmpty(adjustmentsSearchtxt.getText()) ||
                (adjustmentProductCheckEntityList == null || adjustmentProductCheckEntityList.size() < 0)) {
            Toast.makeText(context, Constant.NO_PRODUCT_FOUND_ADJUSTMENT, Toast.LENGTH_SHORT).show();
            vibratorAdaptor.makeVibration();
            return false;
        }

        if (locationtxt.isEnabled() && StringUtils.isEmpty(locationtxt.getText())) {
            Toast.makeText(context, Constant.LOCATION_ENTER_ADJUSTMENT, Toast.LENGTH_SHORT).show();
            vibratorAdaptor.makeVibration();
            return false;
        }

        if (isPosAdjustment) {
            if (adjustmentProductCheckEntityList.get(0).getIsSerial().equalsIgnoreCase("True")) {
                if (StringUtils.isEmpty(adjustmentsLottxt.getText()) || StringUtils.isEmpty(adjustmentsSerialtxt.getText())) {
                    Toast.makeText(context, Constant.LOT_SERAIL_MANDATORY_ADJUSTMENT, Toast.LENGTH_SHORT).show();
                    vibratorAdaptor.makeVibration();
                    return false;
                }
                if (!adjustmentsLottxt.isEnabled() || !adjustmentsSerialtxt.isEnabled()) {
                    Toast.makeText(context, Constant.LOT_SERAIL_UNLOCK_ADJUSTMENT, Toast.LENGTH_SHORT).show();
                    vibratorAdaptor.makeVibration();
                    return false;
                }
            }
        }

        if (adjustmentsLottxt.isEnabled() && StringUtils.isEmpty(adjustmentsLottxt.getText())) {
            Toast.makeText(context, Constant.LOT_ENTER_ADJUSTMENT, Toast.LENGTH_SHORT).show();
            vibratorAdaptor.makeVibration();
            return false;
        }

        if (adjustmentsSerialtxt.isEnabled() && StringUtils.isEmpty(adjustmentsSerialtxt.getText())) {
            Toast.makeText(context, Constant.SERIAL_ENTER_ADJUSTMENT, Toast.LENGTH_SHORT).show();
            vibratorAdaptor.makeVibration();
            return false;
        }

        if (StringUtils.isEmpty(adjustmentsQtytxt.getText())) {
            Toast.makeText(context, Constant.QTY_ENTER_ADJUSTMENT, Toast.LENGTH_SHORT).show();
            vibratorAdaptor.makeVibration();
            return false;
        }

        if (Integer.parseInt(adjustmentsQtytxt.getText().toString()) == 0) {
            Toast.makeText(context, Constant.QTY_ZERO_ADJUSTMENT, Toast.LENGTH_SHORT).show();
            vibratorAdaptor.makeVibration();
            return false;
        }

        if (StringUtils.isEmpty(adjustmentsReasontxt.getText()) && adjustmentsReasontxt.isEnabled()) {
            Toast.makeText(context, Constant.REASON_ENTER_ADJUSTMENT, Toast.LENGTH_SHORT).show();
            vibratorAdaptor.makeVibration();
            return false;
        }
        return true;
    }

    private void hideKeyboard(Context ctx) {
        InputMethodManager imm = (InputMethodManager) ctx.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(cView.getWindowToken(), 0);

    }

    //Check Internet
    private Boolean isInterneTConnected(){
        checkInternet=intConn.checkConnection(getContext()); // Internet connection check
        requestToFocusField();
        if(checkInternet) {
            return true;
        }else{
            return false;
        }
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
                requestToFocusField();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    //Check Quantity For Picking Location
    private void checkReduceQtyFroPickLocation(){
        try{
            JSONObject jsonObject=new JSONObject();
            jsonObject.put("CompanyDbName",companyDBName);
            jsonObject.put("Productid",adjustmentProductCheckEntityList.get(0).getProductId());
            jsonObject.put("ComboId",adjustmentProductCheckEntityList.get(0).getCombid());
            jsonObject.put("Picklocname",pickingLocationName);
            jsonObject.put("Whid",warehouseID);
            jsonObject.put("Qunatity",adjustmentsQtytxt.getText().toString());
            jsonObject.put("key",passCode);
            jsonString=jsonObject.toString();
            makeUrl=service_url + "/CheckGlobalQuantity" ;
            new AdjustmentReduceQtyPL().execute(makeUrl);

        }catch (Exception ex){
            ex.printStackTrace();
        }
    }
    //Check Adjustment Reduce Quantity For Picking Location
    public class AdjustmentReduceQtyPL extends AsyncTask<String, String, String> {
        @Override
        protected String  doInBackground(String... params) {
            AdjustmentReduceQtyForPLService adjustmentReduceQtyForPLService=new AdjustmentReduceQtyForPLService();
            return adjustmentReduceQtyForPLService.getAdjustmentReduceQtyForPL(params[0], jsonString);
        }

        @Override
        protected void onPostExecute(String status) {
            try {
                if(status!=null && !status.equals("")){
                    if(status.equalsIgnoreCase("avilable")) {
                        makeDataForPosAndNegAdjustments();
                        makeUrl = service_url + "/GlobalAdjustMinusSerialLot";
                        new SubmitNegativeAdjustments().execute(makeUrl);
                    }else{
                        Toast.makeText(context, status, Toast.LENGTH_SHORT).show();
                        vibratorAdaptor.makeVibration();
                    }
                }else{
                    Toast.makeText(context, Constant.SERVER_ERROR, Toast.LENGTH_SHORT).show();
                    vibratorAdaptor.makeVibration();
                }

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }


}
