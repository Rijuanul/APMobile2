package id.advancepro.com.advancepro;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import id.advancepro.com.advancepro.model.WarehouseCountEntity;
import id.advancepro.com.advancepro.service.InternetConnection;
import id.advancepro.com.advancepro.service.WareHouseCountService;
import id.advancepro.com.advancepro.utils.Constant;

public class WarehouseActivity extends AppCompatActivity {
    private InternetConnection intConn;
    private Boolean checkInternet=false;
    private AlertDialog.Builder screenExitbuilder;
    private SharedPreferences sharedpreferences;
    private String makeUrl;
    private String passCode;
    private String service_url;
    private String companyDBName;
    private Integer userID;
    private Integer warehouseID;
    private String cycleCount;
    private TabLayout tabLayout;
    private Boolean isAdjustmentsVisisble;


    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_warehouse);
            //SharedPreferrences value
            sharedpreferences = getSharedPreferences(Constant.LOGIN_PREF, Context.MODE_PRIVATE);
            passCode = sharedpreferences.getString(Constant.PASS_CODE, null);
            service_url = sharedpreferences.getString(Constant.SERVICE_URL, null);
            companyDBName = sharedpreferences.getString(Constant.LOGIN_DB, null);
            userID = Integer.parseInt(sharedpreferences.getString(Constant.LOGIN_USERID, "0"));
            warehouseID = Integer.parseInt(sharedpreferences.getString(Constant.DEFAULT_WAREHOUSE_ID, "0"));

            intConn = new InternetConnection();//Internet Connection Initialization

            //Warehouse Toolbar
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            toolbar.setTitle("Warehouse");
            setSupportActionBar(toolbar);

            confirmExit();//Alert initialization for exit screen
            //Get Warehouse Count
            getWarehouseCount();// Get Warehouse count which one will be enable or disable

        }catch (Exception ex){
            ex.printStackTrace();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_warehouse, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        int id = item.getItemId(); // Toolbar option menu id

        if (id == R.id.warehouse_refresh) { //Warehouse Refresh Button click
            screenExitbuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) { // Logic Basedd on Yes Selection
                    warehouseRefreshPage();
                    dialog.dismiss();
                }
            });
            screenExitbuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) { // Logic Basedd on No Selection
                    dialog.dismiss();
                }
            });
            screenExitbuilder.show();
            return true;

        }else if (id == R.id.warehouse_backto_home) { //Warehouse back button click
            screenExitbuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    goToHomePage();
                    dialog.dismiss();
                }
            });
            screenExitbuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            screenExitbuilder.show();//Alert box Show
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //Device Back Button Pressed
    @Override
    public void onBackPressed() {
        screenExitbuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                goToHomePage();
                dialog.dismiss();
            }
        });
        screenExitbuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        screenExitbuilder.show();//Alert Box Show
    }


    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {
        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            try {
                checkInternet=intConn.checkConnection(getApplicationContext()); // Internet connection check
                if(checkInternet) {
                    switch (position) {
                        case 0: //O Position is for Transfer
                            Transfer transfer = new Transfer();
                            return transfer;
                        case 1: //1 Position is for CycleCounts
                            CycleCounts cycleCounts = new CycleCounts();
                            return cycleCounts;
                        case 2: //2 Position is for Adjustments
                            if(isAdjustmentsVisisble) {
                                Adjustments adjustments = new Adjustments();
                                return adjustments;
                            }
                    }
                }else {
                    internetConnectionMessage(); // Call Internet Connection Error Message
                }
            }catch (Exception ex){
                ex.printStackTrace();
            }
            return null;
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            if(isAdjustmentsVisisble) { //If Adjustment is visible from AdvancePro Desktop then Tab will be 3
                return 3;
            }else {
                return 2; // Other wise tab will be only 2 (Transfer and CycleCounts)
            }
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Transfer"; // Transfer Title
                case 1:
                    if(Integer.parseInt(cycleCount.toString())==0){
                        return "CycleCount"; // CycleCounts Title
                    }else {
                        return "CycleCount" + "(" + cycleCount + ")";// CycleCounts Title with Active Amount
                    }
                case 2:
                    if(isAdjustmentsVisisble) {
                        return "Adjustments";
                    }
            }
            return null;
        }
    }

    // Internet Connection Error Message
    private void internetConnectionMessage(){
        Toast.makeText(WarehouseActivity.this, getApplicationContext().getResources().getString(R.string.error001), Toast.LENGTH_SHORT).show();

    }

    //Confirm Exit
    private void confirmExit(){
        screenExitbuilder = new AlertDialog.Builder(WarehouseActivity.this);
        screenExitbuilder.setMessage("Do you want to exit from this screen");
    }
    //Go to Home Page
    private void goToHomePage(){
        Intent backToHome = new Intent(this, HomeActivity.class);
        startActivity(backToHome);//Start the Home Activity
         finish();
    }
    //WareHouse Refresh page
    private void warehouseRefreshPage() {
        Intent refresh = new Intent(this, WarehouseActivity.class);
        startActivity(refresh);//Start the same Activity
        finish();
    }
    //Get warehouse count
    private void getWarehouseCount(){
        try{
            makeUrl=service_url+ "/GetWarehouseCount/" + companyDBName+ "/" + userID + "/" + warehouseID+ "/" + passCode;
            new WarehouseCount().execute(makeUrl);
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }
    //WarehouseCount Service
    public class WarehouseCount extends AsyncTask<String, String, WarehouseCountEntity> {
        @Override
        protected WarehouseCountEntity doInBackground (String...params){
            WareHouseCountService wareHouseCountService=new WareHouseCountService();
            return wareHouseCountService.getWareHouseCount(params[0]);
        }
        @Override
        protected void onPostExecute (WarehouseCountEntity warehouseCountEntity){
            try {
                if(warehouseCountEntity!=null){
                    cycleCount=warehouseCountEntity.getCycleCount(); //get warehouse cyclecount witth amount

                    if(Integer.parseInt(warehouseCountEntity.getAdjustmentStatus())==0){ // get adjustments status based on AdvancePro Desktop App
                        isAdjustmentsVisisble=false; // If disable from desktop app make false
                        //tabLayout.getChildAt(2).setEnabled(false);
                    }else{
                        isAdjustmentsVisisble=true;// // If enable from desktop app make true
                    }
                    // Create the adapter that will return a fragment for each of the three
                    // primary sections of the activity.
                    mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
                    // Set up the ViewPager with the sections adapter.
                    mViewPager = (ViewPager) findViewById(R.id.container);
                    mViewPager.setAdapter(mSectionsPagerAdapter);

                    tabLayout = (TabLayout) findViewById(R.id.tabs);// Warehouse tabs initialization
                    tabLayout.setupWithViewPager(mViewPager);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                ex.getMessage();
            }
        }
    }
}
