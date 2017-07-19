package id.advancepro.com.advancepro;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

import id.advancepro.com.advancepro.adapter.SalesSearchProdListAdapter;
import id.advancepro.com.advancepro.adapter.SalesVariantExpandableListViewAdapter;
import id.advancepro.com.advancepro.adapter.VibratorAdaptor;
import id.advancepro.com.advancepro.model.SalesCustListEntity;
import id.advancepro.com.advancepro.model.SalesOrderProductHierarchyEntity;
import id.advancepro.com.advancepro.model.SalesSearchProductEntity;
import id.advancepro.com.advancepro.model.SalesVariantItemEntity;
import id.advancepro.com.advancepro.model.SalesVariantProductInfoEntity;
import id.advancepro.com.advancepro.service.InternetConnection;
import id.advancepro.com.advancepro.service.SaleService.SalesServiceImpl.SalesOrderServiceImpl;
import id.advancepro.com.advancepro.service.SaleService.SalesServiceImpl.SalesServiceImpl;
import id.advancepro.com.advancepro.utils.Constant;

public class SalesActivity extends AppCompatActivity {

    private Spinner salesOrderProdCtg;
    private InternetConnection intConn;
    private SharedPreferences sharedpreferences;
    private String makeUrl;
    private String passCode;
    private String service_url;
    private String companyDBName;
    private String companyName;
    private Integer userID;
    private Integer wareHouseID;
    private Boolean checkInternet=false;
    private String jsonString;
    private VibratorAdaptor vibratorAdaptor;
    private Spinner salesOrderCustDropDown;
    private Integer salesDefCustID;
    private Button searchSalesProductBtn;
    private Integer custID;
    private Integer prodCatgID;
    private SalesSearchProdListAdapter salesSearchProdListAdapter;
    private ListView salesSearchProdListView;
    private ProgressBar salesSearchProdLoading;
    private  String prodID;
    private View slaesVariantProductpopUp;
    private ExpandableListView expandableListView;
    private SalesVariantExpandableListViewAdapter salesVariantExpandableListViewAdapter;
    private Button salesvarprodsearchbtn;
    private List<SalesVariantItemEntity> salesVariantItemEntitiesRef;
    private JSONObject finalJsonObject;
    private TextView salesVarProdName;
    private TextView salesVarProdAtsQty;
    private TextView salesVarProdprice;
    private Integer qtyAddtoOrder;
    private Button salesVariantprodAddPlacetoOrder;
    private EditText salesVarProdQtytext;
    private TextView salesOrderQtytext;
    private  AlertDialog dialogSalesVariantprod;
    private SalesVariantProductInfoEntity salesVariantProductInfoEntity;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sales);

        qtyAddtoOrder=0;
        intConn=new InternetConnection();//Internet connection class initialization
        //Get Sharedpreferences value
        sharedpreferences = getSharedPreferences(Constant.LOGIN_PREF, Context.MODE_PRIVATE);
        passCode=sharedpreferences.getString(Constant.PASS_CODE,null);
        service_url=sharedpreferences.getString(Constant.SERVICE_URL,null);
        companyDBName=sharedpreferences.getString(Constant.LOGIN_DB,null);
        companyName=sharedpreferences.getString(Constant.LOGIN_COMPANY_NAME,null);
        userID= Integer.parseInt(sharedpreferences.getString(Constant.LOGIN_USERID,"0"));
        wareHouseID= Integer.parseInt(sharedpreferences.getString(Constant.DEFAULT_WAREHOUSE_ID,"0"));
        salesDefCustID=Integer.parseInt(sharedpreferences.getString(Constant.SALES_DEFAULT_CUST_ID,"0"));
        //Initialize Sales Activity Field
        salesOrderProdCtg=(Spinner)findViewById(R.id.salesproductctgspinner);
        salesOrderCustDropDown=(Spinner)findViewById(R.id.salescustspinner);
        searchSalesProductBtn=(Button)findViewById(R.id.salessearchproductbtn);
        salesSearchProdListView=(ListView) findViewById(R.id.salesproductlist);
        salesSearchProdLoading=(ProgressBar)findViewById(R.id.salesprodlistprogressbar);
        salesSearchProdLoading.setVisibility(View.GONE);
        salesOrderQtytext=(TextView)findViewById(R.id.salesorderqtytext);
        //Vibrator
        vibratorAdaptor=new VibratorAdaptor(getApplicationContext());
        //get Product Hierarchy
        getSalesOrderprodhierarchy();
        //get Sales Cust List
        getSalesCustList();
        //Click Sales Search Button for Product
        searchSalesProductBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SalesOrderProductHierarchyEntity salesOrderProductHierarchyEntity=(SalesOrderProductHierarchyEntity)salesOrderProdCtg.getSelectedItem();
                SalesCustListEntity SalesCustListEntity=(SalesCustListEntity)salesOrderCustDropDown.getSelectedItem();
                custID=SalesCustListEntity.getCustID();
                prodCatgID=salesOrderProductHierarchyEntity.getId();
                getSalesSearchProductList();
            }
        });
        //Sales Lsit view Touch Event
        salesSearchProdListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                prodID = ((TextView) (view.findViewById(R.id.salessearchprodid))).getText().toString();
                String isVariant = ((TextView) (view.findViewById(R.id.salessearchprodisvariant))).getText().toString();
                if(Boolean.parseBoolean(isVariant)){
                    getSalesVariantproduct();
                }else{
                    addOrderQty(0);
                }
            }
        });

    }
    //get Sales Product Hierarchy
    private void getSalesOrderprodhierarchy(){
        try{
            checkInternet=intConn.checkConnection(this); // Internet connection check
            if(checkInternet) {
                makeUrl = service_url + "/GetSalesProductCategory/" + companyDBName   + "/" + passCode;
                new SalesProdHierarchy().execute(makeUrl);
            }else{
                internetConnectionMessage();
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }
    // Sales product hierarchy
    public class SalesProdHierarchy extends AsyncTask<String, String, List<SalesOrderProductHierarchyEntity>> {
        @Override
        protected List<SalesOrderProductHierarchyEntity> doInBackground(String... params) {
            SalesOrderServiceImpl salesOrderServiceImpl=new SalesOrderServiceImpl();
            return salesOrderServiceImpl.getSalesOrderProductHierarchy(makeUrl);
        }

        @Override
        protected void onPostExecute(List<SalesOrderProductHierarchyEntity> salesOrderProductHierarchyEntities) {
            if(salesOrderProductHierarchyEntities!=null && salesOrderProductHierarchyEntities.size()>0){
                populateSalesProdCtgDropDown(salesOrderProductHierarchyEntities);
            }
        }
    }
    //Populate sales prod category dropdown'
    private void populateSalesProdCtgDropDown(List<SalesOrderProductHierarchyEntity> salesOrderProductHierarchyEntities){
        try{
            ArrayAdapter<SalesOrderProductHierarchyEntity> adapter = new ArrayAdapter<SalesOrderProductHierarchyEntity>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item,salesOrderProductHierarchyEntities);
            salesOrderProdCtg.setAdapter(adapter);
        }catch (Exception ex){
            ex.printStackTrace();
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
                salesOrderCustDropDown.setAdapter(adapter);
                if(salesDefCustID!=0) {
                    for (int i = 0; i < salesCustListEntities.size(); i++) {
                        if (salesCustListEntities.get(i).getCustID() == salesDefCustID) {
                            salesOrderCustDropDown.setSelection(i);
                            break;
                        }
                    }
                }
            }
        }
    }
    //Get Sales Search product List
    private void getSalesSearchProductList(){
        try{
            checkInternet=intConn.checkConnection(this); // Internet connection check
            salesSearchProdLoading.setVisibility(View.VISIBLE);
            if(checkInternet) {
                makeUrl = service_url + "/SearchSalesProduct/" + companyDBName   + "/" + passCode + "/" + custID +"/"+ prodCatgID;
                new SalesSearchProductList().execute(makeUrl);
            }else{
                internetConnectionMessage();
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }
    //Search Sales Product List Service
    public class SalesSearchProductList extends AsyncTask<String, String, List<SalesSearchProductEntity>> {
        @Override
        protected List<SalesSearchProductEntity> doInBackground(String... params) {
            SalesOrderServiceImpl salesOrderServiceImpl=new SalesOrderServiceImpl();
            return salesOrderServiceImpl.getSalesSearchProduct(makeUrl,service_url,companyDBName,passCode);
        }

        @Override
        protected void onPostExecute(List<SalesSearchProductEntity> salesSearchProductEntities) {
            if(salesSearchProductEntities!=null && salesSearchProductEntities.size()>0){
                //System.out.println(salesSearchProductEntities);
                setSalesSearchProdListView(salesSearchProductEntities);
            }
        }
    }

    //Internet Connnection Error Message
    private void internetConnectionMessage(){
        Toast.makeText(SalesActivity.this, getApplicationContext().getResources().getString(R.string.error001), Toast.LENGTH_SHORT).show();
        vibratorAdaptor.makeVibration();

    }
    //Set Sales Search Product List View
    private void setSalesSearchProdListView(List<SalesSearchProductEntity> salesSearchProductEntities){
        try{
            salesSearchProdListView.setAdapter(null);
            salesSearchProdLoading.setVisibility(View.GONE);
            salesSearchProdListAdapter = new SalesSearchProdListAdapter(getApplicationContext(), R.layout.salessearchproductlistlayout, salesSearchProductEntities);
            salesSearchProdListView.setAdapter(salesSearchProdListAdapter);
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }
    //Get Sales Variant Product
    private void getSalesVariantproduct(){
        try {
            checkInternet=intConn.checkConnection(this); // Internet connection check
            if(checkInternet) {
                makeUrl = service_url + "/SalesVariantCategory/" + companyDBName   + "/" + passCode ;
                new SalesVariantProductList().execute(makeUrl);
            }else{
                internetConnectionMessage();
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    //Search Sales Product List Service
    public class SalesVariantProductList extends AsyncTask<String, String, List<SalesVariantItemEntity>> {
        @Override
        protected List<SalesVariantItemEntity> doInBackground(String... params) {
            SalesOrderServiceImpl salesOrderServiceImpl=new SalesOrderServiceImpl();
            return salesOrderServiceImpl.getSalesVariantDetails(makeUrl,service_url,companyDBName,passCode,prodID);
        }

        @Override
        protected void onPostExecute(List<SalesVariantItemEntity> salesVariantItemEntities) {
            if(salesVariantItemEntities!=null && salesVariantItemEntities.size()>0){
                salesVariantItemEntitiesRef=salesVariantItemEntities;
                //POP UP Field
                slaesVariantProductpopUp=getLayoutInflater().inflate(R.layout.salesproductchildview,null);
                expandableListView=(ExpandableListView) slaesVariantProductpopUp.findViewById(R.id.salesvariantitem);
                salesvarprodsearchbtn=(Button)slaesVariantProductpopUp.findViewById(R.id.searchsalesvaprodbtn);
                salesVarProdName=(TextView)slaesVariantProductpopUp.findViewById(R.id.salesvariantprodname);
                salesVarProdAtsQty=(TextView)slaesVariantProductpopUp.findViewById(R.id.salesvariantprodatsqty);
                salesVarProdprice=(TextView)slaesVariantProductpopUp.findViewById(R.id.salesvariantprodprice);
                salesVariantprodAddPlacetoOrder=(Button)slaesVariantProductpopUp.findViewById(R.id.salesvarprodaddtoorder);
                salesVarProdQtytext=(EditText)slaesVariantProductpopUp.findViewById(R.id.salesvarprodqty);

                //Alert Dialougue
                AlertDialog.Builder builder = new AlertDialog.Builder(SalesActivity.this);
                builder.setView(slaesVariantProductpopUp);
                dialogSalesVariantprod = builder.create();
                dialogSalesVariantprod.show();
                setVariantExpandableList();
                // Expandable List Child Click Listener
                expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
                    @Override
                    public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                        setVariantSelection(groupPosition,childPosition);
                        parent.collapseGroup(groupPosition);
                        return false;
                    }
                });
                //Sales Variant product Search Button
                salesvarprodsearchbtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            JSONArray jsonArray=new JSONArray();
                            for (int i = 0; i < salesVariantItemEntitiesRef.size(); i++) {
                                JSONObject jsonObject = new JSONObject();
                                for (int j = 0; j < salesVariantItemEntitiesRef.get(i).getSalesVariantChildItem().size(); j++) {
                                    if(salesVariantItemEntitiesRef.get(i).getSalesVariantChildItem().get(j).getSelect()) {
                                        jsonObject.put("C046_VAR_ID", salesVariantItemEntitiesRef.get(i).getSalesVariantChildItem().get(j).getVarID().toString());
                                        jsonObject.put("C046_PROP_ID", salesVariantItemEntitiesRef.get(i).getSalesVariantChildItem().get(j).getPropID().toString());
                                        break;
                                    }
                                }
                                jsonArray.put(jsonObject);
                            }
                            finalJsonObject=new JSONObject();
                            finalJsonObject.put("CompanyDbName",companyDBName);
                            finalJsonObject.put("Key",passCode);
                            finalJsonObject.put("ProductID",prodID);
                            finalJsonObject.put("VariantList",jsonArray);
                            jsonString=finalJsonObject.toString();
                            getSalesVariantProdInfo();

                        }catch (Exception ex){
                            ex.printStackTrace();
                        }
                    }
                });
                //Sales Variant product Add to order
                salesVariantprodAddPlacetoOrder.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        addOrderQty(1);
                    }
                });

            }
        }
    }
    //Set Variant product into expandable List
    private void setVariantExpandableList(){
        try {
            salesVariantExpandableListViewAdapter = new SalesVariantExpandableListViewAdapter(getApplicationContext(), salesVariantItemEntitiesRef);
            // setting list adapter
            expandableListView.setAdapter(salesVariantExpandableListViewAdapter);
            expandableListView.setSelected(true);
        }catch (Exception ex){
            ex.printStackTrace();
        }

    }
    //Set Variant selection
    private void setVariantSelection(Integer groupPosition,Integer childPosition){
        try{

            for(int j=0;j<salesVariantItemEntitiesRef.get(groupPosition).getSalesVariantChildItem().size();j++) {
                salesVariantItemEntitiesRef.get(groupPosition).getSalesVariantChildItem().get(j).setSelect(false);
            }
            salesVariantItemEntitiesRef.get(groupPosition).getSalesVariantChildItem().get(childPosition).setSelect(true);
            setVariantExpandableList();
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    //Get Sales Variant Product Information
    private void getSalesVariantProdInfo(){
        try{
            makeUrl = service_url + "/GetSalesProductVariantCombID" ;
            new SalesVariantProductInformation().execute(makeUrl);
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    //Search Sales Product List Service
    public class SalesVariantProductInformation extends AsyncTask<String, String, List<SalesVariantProductInfoEntity>> {
        @Override
        protected List<SalesVariantProductInfoEntity> doInBackground(String... params) {
            SalesOrderServiceImpl salesOrderServiceImpl=new SalesOrderServiceImpl();
            return salesOrderServiceImpl.getSalesVariantProdInfo(makeUrl,service_url,companyDBName,passCode,prodID,custID,jsonString);
        }

        @Override
        protected void onPostExecute(List<SalesVariantProductInfoEntity> salesVariantProductInfoEntities) {
            if(salesVariantProductInfoEntities!=null && salesVariantProductInfoEntities.size()>0){
                salesVariantProductInfoEntity=salesVariantProductInfoEntities.get(0);
                salesVarProdName.setText(salesVariantProductInfoEntities.get(0).getPRODUCT_NAME());
                salesVarProdAtsQty.setText("ATS QTY: " +salesVariantProductInfoEntities.get(0).getQTY_AVAIL().toString());
                salesVarProdprice.setText("$"+salesVariantProductInfoEntities.get(0).getPRICE().toString());

            }
        }
    }
    //Add Order quantity
    private void addOrderQty(Integer val){ // If val=0 than Non Varaint product otherwise variant prod
        if(val!=0) {
            qtyAddtoOrder = qtyAddtoOrder + Integer.parseInt(salesVarProdQtytext.getText().toString());
            dialogSalesVariantprod.dismiss();
        }else{
            qtyAddtoOrder=qtyAddtoOrder+1;
        }
        //System.out.println(qtyAddtoOrder);
        salesOrderQtytext.setText(qtyAddtoOrder.toString());

    }
}
