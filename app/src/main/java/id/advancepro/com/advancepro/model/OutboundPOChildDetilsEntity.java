package id.advancepro.com.advancepro.model;

import android.support.annotation.NonNull;

/**
 * Created by TASIV on 4/19/2017.
 */

public class OutboundPOChildDetilsEntity implements Comparable{
    private String ProductID;
    private String ProductName;
    private String ProductSKU;
    private String ProductUPC;
    private String Quantity;
    private String QuantityRecd;
    private String LocationName;
    private String OrderDetailId;
    private String ProductPickingLocId;
    private String Isbatchfound;
    private String Islotfound;
    private String Combid;
    private String Islotserial;
    private Integer status;

    public Boolean getQtyInMiddle() {
        return isQtyInMiddle;
    }

    public void setQtyInMiddle(Boolean qtyInMiddle) {
        isQtyInMiddle = qtyInMiddle;
    }

    private Boolean isQtyInMiddle;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }



    public String getProductID() {
        return ProductID;
    }

    public void setProductID(String productID) {
        ProductID = productID;
    }

    public String getProductName() {
        return ProductName;
    }

    public void setProductName(String productName) {
        ProductName = productName;
    }

    public String getProductSKU() {
        return ProductSKU;
    }

    public void setProductSKU(String productSKU) {
        ProductSKU = productSKU;
    }

    public String getProductUPC() {
        return ProductUPC;
    }

    public void setProductUPC(String productUPC) {
        ProductUPC = productUPC;
    }

    public String getQuantity() {
        return Quantity;
    }

    public void setQuantity(String quantity) {
        Quantity = quantity;
    }

    public String getQuantityRecd() {
        return QuantityRecd;
    }

    public void setQuantityRecd(String quantityRecd) {
        QuantityRecd = quantityRecd;
    }

    public String getLocationName() {
        return LocationName;
    }

    public void setLocationName(String locationName) {
        LocationName = locationName;
    }

    public String getOrderDetailId() {
        return OrderDetailId;
    }

    public void setOrderDetailId(String orderDetailId) {
        OrderDetailId = orderDetailId;
    }

    public String getProductPickingLocId() {
        return ProductPickingLocId;
    }

    public void setProductPickingLocId(String productPickingLocId) {
        ProductPickingLocId = productPickingLocId;
    }

    public String getIsbatchfound() {
        return Isbatchfound;
    }

    public void setIsbatchfound(String isbatchfound) {
        Isbatchfound = isbatchfound;
    }

    public String getIslotfound() {
        return Islotfound;
    }

    public void setIslotfound(String islotfound) {
        Islotfound = islotfound;
    }

    public String getCombid() {
        return Combid;
    }

    public void setCombid(String combid) {
        Combid = combid;
    }

    public String getIslotserial() {
        return Islotserial;
    }

    public void setIslotserial(String islotserial) {
        Islotserial = islotserial;
    }

    @Override
    public int compareTo(@NonNull Object obj) {
        int compareStatus=((OutboundPOChildDetilsEntity)obj).getStatus();
        /* For Ascending order*/
        return this.status-compareStatus;

        /* For Descending order do like this */
        //return compareage-this.studentage;
    }
}
