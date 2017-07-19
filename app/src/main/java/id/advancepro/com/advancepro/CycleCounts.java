package id.advancepro.com.advancepro;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import id.advancepro.com.advancepro.adapter.CycleCountItemListAdapter;
import id.advancepro.com.advancepro.adapter.CycleCountProgressAdapter;
import id.advancepro.com.advancepro.adapter.NextCycleCountItemListAdapter;
import id.advancepro.com.advancepro.model.CycleCountItemEntity;
import id.advancepro.com.advancepro.model.CycleCountPickerEntity;
import id.advancepro.com.advancepro.model.CycleCountPickingLocationEntity;
import id.advancepro.com.advancepro.model.CycleCountProductCategoryEntity;
import id.advancepro.com.advancepro.model.CycleCountVendorEntity;
import id.advancepro.com.advancepro.model.InProgressCycleCountEntity;
import id.advancepro.com.advancepro.model.NextCycleCountCompleteEntity;
import id.advancepro.com.advancepro.model.NextCycleCountItemEntity;
import id.advancepro.com.advancepro.model.WareHouseListEntity;
import id.advancepro.com.advancepro.service.CycleCountInProgressService;
import id.advancepro.com.advancepro.service.CycleCountItemService;
import id.advancepro.com.advancepro.service.CycleCountPickerService;
import id.advancepro.com.advancepro.service.CycleCountPickingLocationService;
import id.advancepro.com.advancepro.service.CycleCountPopUpSaveService;
import id.advancepro.com.advancepro.service.CycleCountProductCategoryService;
import id.advancepro.com.advancepro.service.CycleCountVendorService;
import id.advancepro.com.advancepro.service.NextCycleCountCompleteService;
import id.advancepro.com.advancepro.service.NextCycleCountService;
import id.advancepro.com.advancepro.service.WareHouseListService;
import id.advancepro.com.advancepro.utils.Constant;

/**
 * Created by TASIV on 3/24/2017.
 */

public class CycleCounts extends Fragment {

    private View rootView;
    private SharedPreferences sharedpreferences;
    private String makeURL;
    private String passCode;
    private String service_url;
    private String companyDBName;
    private Integer userID;
    private String companyName;
    private Integer warehouseID;
    private String jsonString;
    private TextView cycleCountsHeading;
    private Button addNewCycleCounts;
    private EditText searchCycleCounts;
    private ListView cycleCountsListView;
    private Boolean isCycleCountsInProgress;
    private Boolean isCycleCountsListViewTouch;
    private List<InProgressCycleCountEntity> inProgressCycleCountEntityListRef;
    private List<CycleCountItemEntity> cycleCountItemEntityRefList;
    private CycleCountProgressAdapter cycleCountProgressAdapter;
    private CycleCountItemListAdapter cycleCountItemListAdapter;
    private TextView cycleCountPopUpProductName;
    private TextView cycleCountPopUpSkuname;
    private EditText cycleCountPopUpQty;
    private EditText cycleCountPopUpCmnts;
    private Button cycleCountPopUpSubmit;
    private Button cycleCountPopUpClose;
    private View cycleCountPopUpView;
    private CycleCountItemEntity cycleCountItemEntityRef;
    private ImageButton cycleCountbackBtn;
    private View newCycleCountView;
    private EditText newCycleCountDate;
    private EditText newCycleCountName;
    private Spinner newCycleCountPicker;
    private Spinner newCycleCountWarehouse;
    private Spinner newCycleCountPicLoc;
    private Spinner newCycleCountVendor;
    private Spinner newCycleCountProductCtg;
    private Spinner newCycleCountCustFld;
    private Button newCycleCountNext;
    private EditText newCycleCountItemSearch;
    private Button newCycleCountItemSaveBtn;
    private Button newCycleCountItemStartBtn;
    private ScrollView newCycleCountScrollView;
    private ScrollView newCycleCountItemScrollView;
    private LinearLayout newCycleCountPicLocLayout;
    private LinearLayout newCycleCountVendorLayout;
    private LinearLayout newCycleCountPrdCtgLayout;
    private LinearLayout newCycleCountCustFldLayout;
    private Button newCycleCountBackBtn;
    private Boolean isNewCycleCountItem;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
    private DatePickerDialog.OnDateSetListener date_picker;
    private String makeUrl;
    private Boolean isCycleCountPickingInitialize;
    private Boolean isCycleCountVendorInitialize;
    private Boolean isCycleCountproductCtgInitialize;
    private Boolean isNewCycleCountOptionSelected;
    private ListView newCycleCountItemList;
    private Integer newCycleCountPickerId;
    private Integer newCycleCountWarehouseId;
    private Integer newCycleCountPicLocId;
    private Integer newCycleCountVendorId;
    private Integer newCycleCountProductCtgId;
    private String newCycleCountCustomFld;
    private String newCycleCountNameVal;
    private String newCycleCountDateVal;
    private Integer recorPerpage=10;
    private Integer pageIndex=1;
    private NextCycleCountItemListAdapter nextCycleCountItemListAdapter;
    private List<NextCycleCountItemEntity> nextCycleCountItemEntityListRef;
    private Boolean nextCycleCountItemListTouch;
    private ProgressBar loading;
    private Integer startOrSaveCycleCount;
    private AlertDialog dialogNewCycleCountPopUp;

    /*Did not put any commenting in cycle count because we have to re-work*/

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        try {
            rootView = inflater.inflate(R.layout.cyclecounts, container, false);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return rootView;
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

        try {

            super.onViewCreated(view, savedInstanceState);


            sharedpreferences = this.getActivity().getSharedPreferences(Constant.LOGIN_PREF, Context.MODE_PRIVATE);
            passCode = sharedpreferences.getString(Constant.PASS_CODE, null);
            service_url = sharedpreferences.getString(Constant.SERVICE_URL, null);
            companyDBName = sharedpreferences.getString(Constant.LOGIN_DB, null);
            userID = Integer.parseInt(sharedpreferences.getString(Constant.LOGIN_USERID, "0"));
            companyName=sharedpreferences.getString(Constant.LOGIN_COMPANY_NAME,null);
            warehouseID = Integer.parseInt(sharedpreferences.getString(Constant.DEFAULT_WAREHOUSE_ID, "0"));

            cycleCountsHeading = (TextView) view.findViewById(R.id.cyclecuntheading);
            addNewCycleCounts = (Button) view.findViewById(R.id.newcyclecountbtn);
            searchCycleCounts = (EditText) view.findViewById(R.id.cyclecountsearch);
            cycleCountsListView = (ListView) view.findViewById(R.id.cyclecountlistview);
            cycleCountbackBtn = (ImageButton) view.findViewById(R.id.cyclecountback);
            isCycleCountsInProgress = true;
            //Get  InProgress and Completed CycleCount
            getInprogrssAndCompletedCycleCount();
            //Cycle Count back
            cycleCountbackBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!isCycleCountsInProgress) {
                        getInprogrssAndCompletedCycleCount();
                    }
                }
            });
            //New Cycle Count Click Event
            addNewCycleCounts.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    isNewCycleCountItem=false;
                    openNewCycleCountPopUp();
                }
            });
            //Search InProgress CycleCount
            searchCycleCounts.setOnKeyListener(new View.OnKeyListener() {
                @Override
                public boolean onKey(View v, int keyCode, KeyEvent event) {
                    return false;
                }
            });
            //ListView Item Click
            cycleCountsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    System.gc();
                    if (isCycleCountsListViewTouch && isCycleCountsInProgress) {
                        isCycleCountsListViewTouch = false;
                        String cycleCountProgName = ((TextView) (view.findViewById(R.id.cyclecountprogressnamehide))).getText().toString();
                        String cycleCountProgProdID = ((TextView) (view.findViewById(R.id.cyclecountprogressidhide))).getText().toString();
                        String cycleCountProgStatusID = ((TextView) (view.findViewById(R.id.cyclecountprogressstatusidhide))).getText().toString();
                        //System.out.println("pid"+cycleCountProgProdID+"--"+cycleCountProgStatusID+"--"+cycleCountProgName);
                        if (Integer.parseInt(cycleCountProgStatusID) == 92) {
                            getCycleCountItemList(cycleCountProgName, cycleCountProgProdID);
                        } else {

                        }
                    } else if (isCycleCountsListViewTouch && !isCycleCountsInProgress) {
                        isCycleCountsListViewTouch = false;
                        cycleCountItemEntityRef = new CycleCountItemEntity();
                        cycleCountItemEntityRef.setProductName(((TextView) (view.findViewById(R.id.cyclecountitemname))).getText().toString());
                        cycleCountItemEntityRef.setProductSku(((TextView) (view.findViewById(R.id.cyclecountitemsku))).getText().toString());
                        cycleCountItemEntityRef.setScannedqty(((TextView) (view.findViewById(R.id.cyclecountscanqty))).getText().toString());
                        cycleCountItemEntityRef.setComments(((TextView) (view.findViewById(R.id.cyclecountitemcmnts))).getText().toString());
                        cycleCountItemEntityRef.setCycleCountdetaild(((TextView) (view.findViewById(R.id.cyclecountdetailidhide))).getText().toString());
                        cycleCountItemEntityRef.setCycleCountId(((TextView) (view.findViewById(R.id.cyclecountidhide))).getText().toString());
                        cycleCountItemEntityRef.setProductid(((TextView) (view.findViewById(R.id.cyclecountprodidhide))).getText().toString());
                        getCycleCountPopUp();
                    }
                }
            });
            //CycleCount ListView Touch
            cycleCountsListView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    isCycleCountsListViewTouch = true;
                    return false;
                }
            });

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    //Get InProgress and completed CycleCount
    private void getInprogrssAndCompletedCycleCount() {
        try {
            cycleCountsHeading.setText("Cycle Count List");
            isCycleCountsInProgress = true;
            cycleCountbackBtn.setVisibility(View.INVISIBLE);
            addNewCycleCounts.setVisibility(View.VISIBLE);
            makeURL = service_url + "/GetInProgressCycleCount/" + companyDBName + "/" + userID + "/" + warehouseID + "/" + passCode;
            new CycleCountInProgressAndCompleted().execute(makeURL);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    ////Get InProgress and completed CycleCount Service
    public class CycleCountInProgressAndCompleted extends AsyncTask<String, String, List<InProgressCycleCountEntity>> {
        @Override
        protected List<InProgressCycleCountEntity> doInBackground(String... params) {
            CycleCountInProgressService cycleCountInProgressService = new CycleCountInProgressService();
            return cycleCountInProgressService.getInProgressAndCompletedList(params[0]);

        }

        @Override
        protected void onPostExecute(List<InProgressCycleCountEntity> inProgressCycleCountEntities) {
            try {
                if (inProgressCycleCountEntities != null && inProgressCycleCountEntities.size() > 0) {
                    inProgressCycleCountEntityListRef = inProgressCycleCountEntities;
                    setCycleCountProgressListView(inProgressCycleCountEntities);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    //Set CycleCount Progress ListView
    private void setCycleCountProgressListView(List<InProgressCycleCountEntity> inProgressCycleCountEntities) {
        try {
            cycleCountProgressAdapter = new CycleCountProgressAdapter(getContext(), R.layout.cyclecountinprogresslist, inProgressCycleCountEntities);
            cycleCountsListView.setAdapter(cycleCountProgressAdapter);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    //Get CycleCount Item List
    private void getCycleCountItemList(String cycleCountProgName, String cycleCountProgProdID) {
        try {
            cycleCountsHeading.setText(cycleCountProgName);
            cycleCountsListView.setAdapter(null);
            isCycleCountsInProgress = false;
            cycleCountbackBtn.setVisibility(View.VISIBLE);
            addNewCycleCounts.setVisibility(View.INVISIBLE);
            //System.out.println("dd"+cycleCountProgProdID);
            makeURL = service_url + "/GetCycleCount/" + companyDBName + "/" + userID + "/" + warehouseID + "/" + cycleCountProgProdID + "/" + passCode;
            new CycleCountItemList().execute(makeURL);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    //Cycle Count Item List Web Services
    public class CycleCountItemList extends AsyncTask<String, String, List<CycleCountItemEntity>> {
        @Override
        protected List<CycleCountItemEntity> doInBackground(String... params) {
            CycleCountItemService cycleCountItemService = new CycleCountItemService();
            return cycleCountItemService.getCycleCountItem(params[0]);

        }

        @Override
        protected void onPostExecute(List<CycleCountItemEntity> cycleCountItemEntities) {
            try {
                //System.out.println("cc"+cycleCountItemEntities);
                if (cycleCountItemEntities != null && cycleCountItemEntities.size() > 0) {
                    System.gc();
                    cycleCountItemEntityRef = null;
                    cycleCountItemEntityRefList = cycleCountItemEntities;
                    setCycleCountItemListView(cycleCountItemEntities);
                } else {

                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    //Cycle Count Item List View
    private void setCycleCountItemListView(List<CycleCountItemEntity> cycleCountItemEntities) {
        cycleCountItemListAdapter = new CycleCountItemListAdapter(getContext(), R.layout.cyclecountitemlist, cycleCountItemEntities);
        cycleCountsListView.setAdapter(cycleCountItemListAdapter);
    }

    //Get Cycle Count PopUp
    private void getCycleCountPopUp() {
        try {
            if (cycleCountItemEntityRef != null) {
                cycleCountPopUpView = getActivity().getLayoutInflater().inflate(R.layout.cyclecountpopup, null);
                cycleCountPopUpProductName = (TextView) cycleCountPopUpView.findViewById(R.id.cyclecountpopupprodname);
                cycleCountPopUpSkuname = (TextView) cycleCountPopUpView.findViewById(R.id.cyclecountpopupsku);
                cycleCountPopUpQty = (EditText) cycleCountPopUpView.findViewById(R.id.cyclecountpopupqty);
                cycleCountPopUpCmnts = (EditText) cycleCountPopUpView.findViewById(R.id.cyclecountpopupcmnt);
                cycleCountPopUpSubmit = (Button) cycleCountPopUpView.findViewById(R.id.cyclecountpopupsubmit);
                cycleCountPopUpClose = (Button) cycleCountPopUpView.findViewById(R.id.cyclecountpopupclose);

                cycleCountPopUpProductName.setText(cycleCountItemEntityRef.getProductName());
                cycleCountPopUpSkuname.setText(cycleCountItemEntityRef.getProductSku());
                cycleCountPopUpQty.setText("");
                if (Integer.parseInt(cycleCountItemEntityRef.getScannedqty()) > 0) {
                    cycleCountPopUpQty.setText(cycleCountItemEntityRef.getScannedqty());
                }
                cycleCountPopUpCmnts.setText(cycleCountItemEntityRef.getComments());
                //Alert Dialougue
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setView(cycleCountPopUpView);
                final AlertDialog dialogCycleCountPopUp = builder.create();
                dialogCycleCountPopUp.show();

                cycleCountPopUpFocus();
                //Cycle Count Focus
                //Cycle Count Quantity on Click listener
                cycleCountPopUpQty.setOnKeyListener(new View.OnKeyListener() {
                    @Override
                    public boolean onKey(View v, int keyCode, KeyEvent event) {
                        if (keyCode == 66 && event.getAction() == KeyEvent.ACTION_UP) {
                            cycleCountPopUpFocus();
                        }
                        return false;
                    }
                });
                //Cycle Count Submit Button
                cycleCountPopUpSubmit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (StringUtils.isEmpty(cycleCountPopUpQty.getText()) || Integer.parseInt(cycleCountPopUpQty.getText().toString()) == 0) {
                            cycleCountPopUpFocus();
                        } else {
                            //System.out.println(cycleCountPopUpQty.getText().toString());
                            cycleCountItemEntityRef.setComments(cycleCountPopUpCmnts.getText().toString());
                            cycleCountItemEntityRef.setScannedqty(cycleCountPopUpQty.getText().toString());
                            saveCycleCountPopUp();
                            dialogCycleCountPopUp.dismiss();
                        }
                    }
                });
                //Cycle Count Pop up Close
                cycleCountPopUpClose.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        System.gc();
                        dialogCycleCountPopUp.dismiss();
                    }
                });
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    //Save Pop Up Cycle Count
    private void saveCycleCountPopUp() {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("CompanyDbName", companyDBName);
            jsonObject.put("Userid", userID);
            jsonObject.put("WhID", warehouseID);
            jsonObject.put("Cyclecountid", cycleCountItemEntityRef.getCycleCountId());
            jsonObject.put("CycleCountDetailid", cycleCountItemEntityRef.getCycleCountdetaild());
            jsonObject.put("ProductID", cycleCountItemEntityRef.getProductid());
            jsonObject.put("ScannedQuantity", cycleCountItemEntityRef.getScannedqty());
            jsonObject.put("Comments", cycleCountItemEntityRef.getComments());
            jsonObject.put("key", passCode);
            jsonString = jsonObject.toString();
            makeURL = service_url + "/PostSaveCycleCount";
            new CycleCountPopUpSave().execute(makeURL);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    //Save Cycle Count Pop Up
    //Cycle Count Item List Web Services
    public class CycleCountPopUpSave extends AsyncTask<String, String, Integer> {
        @Override
        protected Integer doInBackground(String... params) {
            CycleCountPopUpSaveService cycleCountPopUpSaveService = new CycleCountPopUpSaveService();
            return cycleCountPopUpSaveService.saveCycleCount(params[0], jsonString);
        }

        @Override
        protected void onPostExecute(Integer status) {
            try {
                //System.out.println("cc"+cycleCountItemEntities);
                if (status == 1) {
                    getCycleCountItemList(cycleCountItemEntityRef.getProductName(), cycleCountItemEntityRef.getCycleCountId());
                }else{
                    //Toast
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    //Cycle Count Pop up focus
    private void cycleCountPopUpFocus() {
        if (StringUtils.isEmpty(cycleCountPopUpQty.getText().toString())) {
            cycleCountPopUpQty.requestFocus();
            return;
        }

        if (StringUtils.isEmpty(cycleCountPopUpCmnts.getText().toString())) {
            cycleCountPopUpCmnts.requestFocus();
            return;
        }
    }

    //New Cycle Count Pop Up
    private void openNewCycleCountPopUp() {
        try {
            isNewCycleCountItem = false;
            newCycleCountView = getActivity().getLayoutInflater().inflate(R.layout.newcyclecountpopup, null);
            initializeNewCycleCountField(newCycleCountView);
            getCycleCountPicker();
            getWarehouseList();
            getCycleCountPicLoc();
            getCycleCountVendor();
            getCycleCountProductCategory();

            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setView(newCycleCountView);
            dialogNewCycleCountPopUp = builder.create();
            dialogNewCycleCountPopUp.setCanceledOnTouchOutside(false);
            dialogNewCycleCountPopUp.show();

            newCycleCountItemScrollView.setVisibility(View.GONE);
            Date date = new Date();
            newCycleCountDate.setText(dateFormat.format(date));
            focusChange();
            //New Cycle Count Next Button Click Event
            newCycleCountNext.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (nextCycleCountValidation()) {
                        isNewCycleCountItem = true;
                        newCycleCountItemScrollView.setVisibility(View.VISIBLE);
                        newCycleCountScrollView.setVisibility(View.GONE);
                        //
                        CycleCountPickerEntity cycleCountPickerEntity = (CycleCountPickerEntity) newCycleCountPicker.getSelectedItem();
                        newCycleCountPickerId = cycleCountPickerEntity.getId();
                        //
                        WareHouseListEntity wareHouseListEntity = (WareHouseListEntity) newCycleCountWarehouse.getSelectedItem();
                        newCycleCountWarehouseId = wareHouseListEntity.getWarhouseID();

                        newCycleCountNameVal = newCycleCountName.getText().toString();
                        newCycleCountDateVal = newCycleCountDate.getText().toString();
                        nextCycleCount();
                    } else {
                        focusChange();
                    }
                }
            });
            //New Cycle Count Back Button Click
            newCycleCountBackBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialogNewCycleCountPopUp.dismiss();
                    if (isNewCycleCountItem) {
                        openNewCycleCountPopUp();
                    }
                }
            });
            //
            newCycleCountDate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Calendar cal = Calendar.getInstance();
                    DatePickerDialog dd = new DatePickerDialog(getContext(), date_picker, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
                    dd.setCanceledOnTouchOutside(false);
                    dd.show();

                }
            });
            date_picker = new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                    int mnth = month + 1;
                    String setDate = year + "/" + mnth + "/" + dayOfMonth;
                    newCycleCountDate.setText(setDate);
                    focusChange();
                }
            };
            //
            newCycleCountPicLoc.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                    if (!isCycleCountPickingInitialize) {
                        isCycleCountPickingInitialize = true;
                        return;
                    }

                    CycleCountPickingLocationEntity cycleCountPickingLocationEntity = (CycleCountPickingLocationEntity) newCycleCountPicLoc.getSelectedItem();
                    if (cycleCountPickingLocationEntity.getId() != 0) {
                        newCycleCountVendorLayout.setVisibility(View.GONE);
                        newCycleCountPrdCtgLayout.setVisibility(View.GONE);
                        newCycleCountCustFldLayout.setVisibility(View.GONE);
                        isNewCycleCountOptionSelected = true;
                        newCycleCountPicLocId = cycleCountPickingLocationEntity.getId();
                    } else {
                        newCycleCountVendorLayout.setVisibility(View.VISIBLE);
                        newCycleCountPrdCtgLayout.setVisibility(View.VISIBLE);
                        newCycleCountCustFldLayout.setVisibility(View.VISIBLE);
                        isNewCycleCountOptionSelected = false;
                        newCycleCountPicLocId = 0;
                    }
                    focusChange();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

            //
            newCycleCountVendor.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                    if (!isCycleCountVendorInitialize) {
                        isCycleCountVendorInitialize = true;
                        return;
                    }
                    CycleCountVendorEntity cycleCountVendorEntity = (CycleCountVendorEntity) newCycleCountVendor.getSelectedItem();
                    if (cycleCountVendorEntity.getID() != 0) {
                        newCycleCountPicLocLayout.setVisibility(View.GONE);
                        newCycleCountPrdCtgLayout.setVisibility(View.GONE);
                        newCycleCountCustFldLayout.setVisibility(View.GONE);
                        isNewCycleCountOptionSelected = true;
                        newCycleCountVendorId = cycleCountVendorEntity.getID();
                    } else {
                        newCycleCountPicLocLayout.setVisibility(View.VISIBLE);
                        newCycleCountPrdCtgLayout.setVisibility(View.VISIBLE);
                        newCycleCountCustFldLayout.setVisibility(View.VISIBLE);
                        isNewCycleCountOptionSelected = false;
                        newCycleCountVendorId = 0;
                    }
                    focusChange();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
            //
            newCycleCountProductCtg.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                    if (!isCycleCountproductCtgInitialize) {
                        isCycleCountproductCtgInitialize = true;
                        return;
                    }
                    CycleCountProductCategoryEntity cycleCountProductCategoryEntity = (CycleCountProductCategoryEntity) newCycleCountProductCtg.getSelectedItem();
                    if (cycleCountProductCategoryEntity.getID() != 0) {
                        newCycleCountVendorLayout.setVisibility(View.GONE);
                        newCycleCountPicLocLayout.setVisibility(View.GONE);
                        newCycleCountCustFldLayout.setVisibility(View.GONE);
                        isNewCycleCountOptionSelected = true;
                        newCycleCountProductCtgId = cycleCountProductCategoryEntity.getID();
                    } else {
                        newCycleCountVendorLayout.setVisibility(View.VISIBLE);
                        newCycleCountPicLocLayout.setVisibility(View.VISIBLE);
                        newCycleCountCustFldLayout.setVisibility(View.VISIBLE);
                        isNewCycleCountOptionSelected = false;
                        newCycleCountProductCtgId = 0;
                    }
                    focusChange();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
            //Next Cycle Count Item List Touch
            newCycleCountItemList.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    nextCycleCountItemListTouch = true;
                    return false;
                }
            });
            //Next Cycle Count Item List Selected
            newCycleCountItemList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    if (nextCycleCountItemListTouch) {

                        String isSelect = ((TextView) (view.findViewById(R.id.newcyclecountitemselected))).getText().toString();
                        String prdID = ((TextView) (view.findViewById(R.id.newcyclecountitemprodidhide))).getText().toString();
                        String cmbID = ((TextView) (view.findViewById(R.id.newcyclecountitemcombidhide))).getText().toString();
                        //System.out.println(isSelect+"=="+prdID+"=="+cmbID);
                        for (int i = 0; i < nextCycleCountItemEntityListRef.size(); i++) {
                            if (nextCycleCountItemEntityListRef.get(i).getProductID().equals(prdID) &&
                                    nextCycleCountItemEntityListRef.get(i).getComboid().equals(cmbID)) {
                                if (Integer.parseInt(isSelect) == 0) {
                                    nextCycleCountItemEntityListRef.get(i).setIsItemSelected(1);
                                } else if (Integer.parseInt(isSelect) == 1) {
                                    nextCycleCountItemEntityListRef.get(i).setIsItemSelected(0);
                                }
                                Collections.sort(nextCycleCountItemEntityListRef);
                                nextCycleCountItemList();
                                break;
                            }
                        }


                    }
                }
            });
            //Cycle Count Item Save Button
            newCycleCountItemSaveBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startOrSaveCycleCount = Constant.SAVE_CYCLE_COUNT_VAL;
                    completeCycleCount();
                }
            });

            //Cycle COunt Item Start Button
            newCycleCountItemStartBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startOrSaveCycleCount = Constant.START_CYCLE_COUNT_VAL;
                    completeCycleCount();
                }
            });
            //
            newCycleCountItemSearch.setOnKeyListener(new View.OnKeyListener() {
                @Override
                public boolean onKey(View v, int keyCode, KeyEvent event) {
                    if(!StringUtils.isEmpty(newCycleCountItemSearch.getText().toString())) {
                        if (keyCode == 66 && event.getAction() == KeyEvent.ACTION_UP) {
                            for (int i = 0; i < nextCycleCountItemEntityListRef.size(); i++) {
                                if (nextCycleCountItemEntityListRef.get(i).getSku().equals(newCycleCountItemSearch.getText().toString())
                                        && nextCycleCountItemEntityListRef.get(i).getIsItemSelected()==0) {
                                    NextCycleCountItemEntity nextCycleCountItemEntity=new NextCycleCountItemEntity();
                                    nextCycleCountItemEntity=nextCycleCountItemEntityListRef.get(i);
                                    nextCycleCountItemEntityListRef.remove(i);
                                    nextCycleCountItemEntityListRef.add(0,nextCycleCountItemEntity);
                                }
                            }
                        }
                    }else{
                        nextCycleCountItemList();
                    }
                    return false;
                }
            });
        }catch (Exception ex){
            ex.printStackTrace();
        }

    }
    //Initialize New Cycle Count Item
    private void initializeNewCycleCountField(View v){

        newCycleCountDate=(EditText)v.findViewById(R.id.newcyclecountdate);
        newCycleCountName=(EditText)v.findViewById(R.id.newcyclecountname);
        newCycleCountPicker=(Spinner) v.findViewById(R.id.newcyclecountpickerdrpdwn);
        newCycleCountWarehouse=(Spinner)v.findViewById(R.id.newcyclecountwarehousedrpdwn);
        newCycleCountPicLoc=(Spinner)v.findViewById(R.id.newcyclecountpiclocdrpdwn);
        newCycleCountVendor=(Spinner)v.findViewById(R.id.newcyclecountvendordrpdwn);
        newCycleCountProductCtg=(Spinner)v.findViewById(R.id.newcyclecountproductctgdrpdwn);
        newCycleCountCustFld=(Spinner)v.findViewById(R.id.newcyclecountcustomflddrpdwn);
        newCycleCountNext=(Button) v.findViewById(R.id.newcyclecountpopupnext);
        newCycleCountItemSearch=(EditText)v.findViewById(R.id.newcyclecountitemfilter);
        newCycleCountItemSaveBtn=(Button)v.findViewById(R.id.newcyclecountitemsavebtn);
        newCycleCountItemStartBtn=(Button)v.findViewById(R.id.newcyclecountitemstartbtn);
        newCycleCountScrollView=(ScrollView) v.findViewById(R.id.newcyclecountscrollview);
        newCycleCountItemScrollView=(ScrollView)v.findViewById(R.id.newcyclecountitemscrollview);
        newCycleCountPicLocLayout=(LinearLayout) v.findViewById(R.id.newcyclecountpicloclayout);
        newCycleCountVendorLayout=(LinearLayout) v.findViewById(R.id.newcyclecountvendorlayout);
        newCycleCountPrdCtgLayout=(LinearLayout) v.findViewById(R.id.newcyclecountprdctglayout);
        newCycleCountCustFldLayout=(LinearLayout) v.findViewById(R.id.newcyclecountcustfldlayout);
        newCycleCountBackBtn=(Button)v.findViewById(R.id.newcyclecountpopupclose);
        newCycleCountItemList=(ListView)v.findViewById(R.id.newcyclecountitemlistview);
        loading=(ProgressBar) v.findViewById(R.id.nextcyclecountloading);


        isCycleCountPickingInitialize=false;
        isCycleCountVendorInitialize=false;
        isCycleCountproductCtgInitialize=false;
        isNewCycleCountOptionSelected=false;

        newCycleCountPickerId=0;
        newCycleCountWarehouseId=0;
        newCycleCountPicLocId=0;
        newCycleCountVendorId=0;
        newCycleCountProductCtgId=0;
        newCycleCountCustomFld="test";
    }

    //Get Cycle Count WareHouse List
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
                    ArrayAdapter<WareHouseListEntity> adapter=new ArrayAdapter<WareHouseListEntity>(getContext(),android.R.layout.simple_spinner_dropdown_item,wareHouseListEntities);
                    newCycleCountWarehouse.setAdapter(adapter);
                }else{
                    //Toast
                }
            }catch (Exception ex){
                ex.printStackTrace();
            }
        }
    }
    //Get Cycle Count Picker List
    private void getCycleCountPicker(){
        try{
            makeURL=service_url+ "/GetCyclecountpicker/" + companyDBName + "/" + passCode;
            new CycleCountPicker().execute(makeURL);
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    public class CycleCountPicker extends AsyncTask<String, String, List<CycleCountPickerEntity>> {
        @Override
        protected List<CycleCountPickerEntity> doInBackground(String... params) {
            CycleCountPickerService cycleCountPickerService=new CycleCountPickerService();
            return cycleCountPickerService.getPickerList(params[0]);
        }

        @Override
        protected void onPostExecute(List<CycleCountPickerEntity> cycleCountPickerEntities) {
            try {
                //System.out.println("Reset for All function "+ wareHouseListEntities);
                if(cycleCountPickerEntities!=null && cycleCountPickerEntities.size()>0){
                    ArrayAdapter<CycleCountPickerEntity> adapter=new ArrayAdapter<CycleCountPickerEntity>(getContext(),android.R.layout.simple_spinner_dropdown_item,cycleCountPickerEntities);
                    newCycleCountPicker.setAdapter(adapter);
                }
            }catch (Exception ex){
                ex.printStackTrace();
            }
        }
    }

    //Get Cycle Count Picking Location List
    private void getCycleCountPicLoc(){
        try{
            JSONObject jsonObject=new JSONObject();
            jsonObject.put("CompanyDbName",companyDBName);
            jsonObject.put("Userid",userID);
            jsonObject.put("Whid",warehouseID);
            jsonObject.put("key",passCode);
            makeURL=service_url+ "/GetCyclecountpickinglocation";
            jsonString=jsonObject.toString();
            new CycleCountPicloc().execute(makeURL);
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    public class CycleCountPicloc extends AsyncTask<String, String, List<CycleCountPickingLocationEntity>> {
        @Override
        protected List<CycleCountPickingLocationEntity> doInBackground(String... params) {
            CycleCountPickingLocationService cycleCountPickingLocationService=new CycleCountPickingLocationService();
            return cycleCountPickingLocationService.getCycleCountPicLoc(params[0],jsonString);
        }

        @Override
        protected void onPostExecute(List<CycleCountPickingLocationEntity> cycleCountPickingLocationEntities) {
            try {
                //System.out.println("Reset for All function "+ wareHouseListEntities);
                if(cycleCountPickingLocationEntities!=null && cycleCountPickingLocationEntities.size()>0){
                    ArrayAdapter<CycleCountPickingLocationEntity> adapter=new ArrayAdapter<CycleCountPickingLocationEntity>(getContext(),android.R.layout.simple_spinner_dropdown_item,cycleCountPickingLocationEntities);
                    newCycleCountPicLoc.setAdapter(adapter);
                }else{
                    //Toast
                }
            }catch (Exception ex){
                ex.printStackTrace();
            }
        }
    }

    //Get Cycle Count VendorList
    private void getCycleCountVendor(){
        try{
            makeURL=service_url+ "/GetVendor/" + companyDBName + "/" + passCode;
            new CycleCountVendor().execute(makeURL);
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    public class CycleCountVendor extends AsyncTask<String, String, List<CycleCountVendorEntity>> {
        @Override
        protected List<CycleCountVendorEntity> doInBackground(String... params) {
            CycleCountVendorService cycleCountVendorService=new CycleCountVendorService();
            return cycleCountVendorService.getCycleCountVendorList(params[0]);
        }

        @Override
        protected void onPostExecute(List<CycleCountVendorEntity> cycleCountVendorEntities) {
            try {
                //System.out.println("Reset for All function "+ wareHouseListEntities);
                if(cycleCountVendorEntities!=null && cycleCountVendorEntities.size()>0){
                    ArrayAdapter<CycleCountVendorEntity> adapter=new ArrayAdapter<CycleCountVendorEntity>(getContext(),android.R.layout.simple_spinner_dropdown_item,cycleCountVendorEntities);
                    newCycleCountVendor.setAdapter(adapter);
                }else{
                    //TOast
                }
            }catch (Exception ex){
                ex.printStackTrace();
            }
        }
    }

    //Get Cycle Count Product Category
    private void getCycleCountProductCategory(){
        try{
            makeURL=service_url+ "/GetProductCategory/" + companyDBName + "/" + passCode;
            new CycleCountProductCategory().execute(makeURL);
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    public class CycleCountProductCategory extends AsyncTask<String, String, List<CycleCountProductCategoryEntity>> {
        @Override
        protected List<CycleCountProductCategoryEntity> doInBackground(String... params) {
            CycleCountProductCategoryService cycleCountProductCategoryService=new CycleCountProductCategoryService();
            return cycleCountProductCategoryService.getCycleCountProductCategoryList(params[0]);
        }

        @Override
        protected void onPostExecute(List<CycleCountProductCategoryEntity> cycleCountProductCategoryEntities) {
            try {
                //System.out.println("Reset for All function "+ wareHouseListEntities);
                if(cycleCountProductCategoryEntities!=null && cycleCountProductCategoryEntities.size()>0){
                    ArrayAdapter<CycleCountProductCategoryEntity> adapter=new ArrayAdapter<CycleCountProductCategoryEntity>(getContext(),android.R.layout.simple_spinner_dropdown_item,cycleCountProductCategoryEntities);
                    newCycleCountProductCtg.setAdapter(adapter);
                }else{
                    //Toast
                }
            }catch (Exception ex){
                ex.printStackTrace();
            }
        }
    }
    //Cycle Count Focus change
    private void focusChange(){
        if(StringUtils.isEmpty(newCycleCountDate.getText())){
            newCycleCountDate.requestFocus();
            return;
        }

        if(StringUtils.isEmpty(newCycleCountName.getText())){
            newCycleCountName.requestFocus();
            return;
        }
    }
    //New Cycle Count next validation
    private Boolean nextCycleCountValidation(){
        if(StringUtils.isEmpty(newCycleCountName.getText())){
            Toast.makeText(getContext(), "Enter a Cycle Count Name", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(!isNewCycleCountOptionSelected){
            Toast.makeText(getContext(), "Please Select anyone", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
    //Next Cycle Count
    private void nextCycleCount(){
        try{
            loading.setVisibility(View.VISIBLE);
            newCycleCountItemList.setAdapter(null);
            makeURL=service_url+ "/GetCyclecountProductlist/" +companyDBName + "/" + warehouseID+ "/" + newCycleCountPicLocId + "/" + newCycleCountVendorId + "/" + newCycleCountProductCtgId + "/" + newCycleCountCustomFld ;//+ "/" +recorPerpage+ "/" +pageIndex+ "/" +passCode;
            new NextCycleCountItem().execute(makeURL);
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    public class NextCycleCountItem extends AsyncTask<String, String, List<NextCycleCountItemEntity>> {
        @Override
        protected List<NextCycleCountItemEntity> doInBackground(String... params) {
            NextCycleCountService nextCycleCountService=new NextCycleCountService();
            return nextCycleCountService.getNextItemCycleCount(params[0],recorPerpage,pageIndex,passCode);
        }

        @Override
        protected void onPostExecute(List<NextCycleCountItemEntity> nextCycleCountItemEntities) {
            try {
                //System.out.println("Reset for All function "+ wareHouseListEntities);
                if(nextCycleCountItemEntities!=null && nextCycleCountItemEntities.size()>0){
                    nextCycleCountItemEntityListRef=nextCycleCountItemEntities;
                    nextCycleCountItemList();

                }else{
                    loading.setVisibility(View.GONE);
                    //Toast
                }
            }catch (Exception ex){
                ex.printStackTrace();
            }
        }
    }

    //Bind Next Cycle Count Item
    private void nextCycleCountItemList(){
        loading.setVisibility(View.GONE);
        nextCycleCountItemListAdapter = new NextCycleCountItemListAdapter(getContext(), R.layout.nextcyclecounnexttitemlist, nextCycleCountItemEntityListRef);
        newCycleCountItemList.setAdapter(nextCycleCountItemListAdapter);

    }
    //Complete Cycle Count
    private void completeCycleCount(){
        try{
            //
            JSONArray selectedItem=new JSONArray();
            for(int i=0;i<nextCycleCountItemEntityListRef.size();i++){
                if(nextCycleCountItemEntityListRef.get(i).getIsItemSelected()==1){
                    JSONObject jsonObject=new JSONObject();
                    jsonObject.put("whid",newCycleCountWarehouseId);
                    jsonObject.put("Productid",nextCycleCountItemEntityListRef.get(i).getProductID());
                    jsonObject.put("Comboid",nextCycleCountItemEntityListRef.get(i).getComboid());
                    jsonObject.put("Productsku",nextCycleCountItemEntityListRef.get(i).getSku());
                    selectedItem.put(jsonObject);
                }
            }
            //
            if(selectedItem.length()<0){

            }else {
                //
                JSONArray arrayContent = new JSONArray();
                //
                JSONObject jsonContent = new JSONObject();
                jsonContent.put("Name", newCycleCountNameVal);
                jsonContent.put("CycleDate", newCycleCountDateVal);
                jsonContent.put("whid", newCycleCountWarehouseId);
                jsonContent.put("Userid", userID);
                jsonContent.put("Picklocid", newCycleCountPicLocId);
                jsonContent.put("Vendorid", newCycleCountVendorId);
                jsonContent.put("Pickerid", newCycleCountPickerId);
                jsonContent.put("Productcatid", newCycleCountProductCtgId);
                jsonContent.put("Customfiled", newCycleCountCustomFld);
                arrayContent.put(jsonContent);
                //
                JSONObject root=new JSONObject();
                root.put("CompanyDbName",companyDBName);
                root.put("Cyclecountproducts",arrayContent);
                root.put("Cyclecountdetailsproducts",selectedItem);
                root.put("key",passCode);
                jsonString=root.toString();
                makeUrl=service_url + "/PostCyclecountdetailsproduct";
                new NextCycleCountComplete().execute(makeUrl);
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }
    //
    public class NextCycleCountComplete extends AsyncTask<String, String, NextCycleCountCompleteEntity> {
        @Override
        protected NextCycleCountCompleteEntity doInBackground(String... params) {
            NextCycleCountCompleteService nextCycleCountCompleteService=new NextCycleCountCompleteService();
            return nextCycleCountCompleteService.completeNextCycleCount(params[0],jsonString);
        }

        @Override
        protected void onPostExecute(NextCycleCountCompleteEntity nextCycleCountCompleteEntity) {
            try {
                //System.out.println("Reset for All function "+ wareHouseListEntities);
                if(nextCycleCountCompleteEntity!=null){
                   if(nextCycleCountCompleteEntity.getID()==1){
                       dialogNewCycleCountPopUp.dismiss();
                       //System.out.println("create");
                       if(startOrSaveCycleCount==Constant.SAVE_CYCLE_COUNT_VAL){
                           isCycleCountsInProgress = true;
                           //Get  InProgress and Completed CycleCount
                           getInprogrssAndCompletedCycleCount();
                       }else if(startOrSaveCycleCount==Constant.START_CYCLE_COUNT_VAL){
                           getCycleCountItemList(nextCycleCountCompleteEntity.getStatus(),nextCycleCountCompleteEntity.getCyclecountid());
                       }
                   }
                }
            }catch (Exception ex){
                ex.printStackTrace();
            }
        }
    }


}
