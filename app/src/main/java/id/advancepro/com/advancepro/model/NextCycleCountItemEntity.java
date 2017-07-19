package id.advancepro.com.advancepro.model;

import android.support.annotation.NonNull;

/**
 * Created by TASIV on 5/12/2017.
 */

public class NextCycleCountItemEntity implements Comparable{
    private String ProductID;
    private String whid;
    private String Comboid;
    private String Inventoryid;
    private String Productname;
    private String Sku;
    private String Upc;
    private String Pickingqty;
    private Integer isItemSelected;

    public Integer getIsItemSelected() {
        return isItemSelected;
    }

    public void setIsItemSelected(Integer isItemSelected) {
        this.isItemSelected = isItemSelected;
    }





    public String getProductID() {
        return ProductID;
    }

    public void setProductID(String productID) {
        ProductID = productID;
    }

    public String getWhid() {
        return whid;
    }

    public void setWhid(String whid) {
        this.whid = whid;
    }

    public String getComboid() {
        return Comboid;
    }

    public void setComboid(String comboid) {
        Comboid = comboid;
    }

    public String getInventoryid() {
        return Inventoryid;
    }

    public void setInventoryid(String inventoryid) {
        Inventoryid = inventoryid;
    }

    public String getProductname() {
        return Productname;
    }

    public void setProductname(String productname) {
        Productname = productname;
    }

    public String getSku() {
        return Sku;
    }

    public void setSku(String sku) {
        Sku = sku;
    }

    public String getUpc() {
        return Upc;
    }

    public void setUpc(String upc) {
        Upc = upc;
    }

    public String getPickingqty() {
        return Pickingqty;
    }

    public void setPickingqty(String pickingqty) {
        Pickingqty = pickingqty;
    }

    @Override
    public int compareTo(@NonNull Object obj) {
        int compareStatus=((NextCycleCountItemEntity)obj).getIsItemSelected();
        /* For Ascending order*/
        return this.getIsItemSelected()-compareStatus;

        /* For Descending order do like this */
        //return compareage-this.studentage;
    }
}
