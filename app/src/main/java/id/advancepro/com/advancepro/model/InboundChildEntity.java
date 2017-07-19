package id.advancepro.com.advancepro.model;

import android.support.annotation.NonNull;

/**
 * Created by TASIV on 3/31/2017.
 */

public class InboundChildEntity implements Comparable{

    private String OrderDetailid;
    private String ProductID;
    private String ProductName;
    private String ProductSKU;
    private String UPC;
    private String TotQuantity;
    private String quantityReceived;
    private String WareHouseName;
    private String CombID;
    private String Islotfound;
    private String Isbatchfound;
    private String ErrorMessage;
    private Integer status;


    public Boolean getQtyRecv() {
        return isQtyRecv;
    }

    public void setQtyRecv(Boolean qtyRecv) {
        isQtyRecv = qtyRecv;
    }

    private Boolean isQtyRecv;


    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }



    public String getOrderDetailid() {
        return OrderDetailid;
    }

    public void setOrderDetailid(String orderDetailid) {
        OrderDetailid = orderDetailid;
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

    public String getUPC() {
        return UPC;
    }

    public void setUPC(String UPC) {
        this.UPC = UPC;
    }

    public String getTotQuantity() {
        return TotQuantity;
    }

    public void setTotQuantity(String totQuantity) {
        TotQuantity = totQuantity;
    }

    public String getQuantityReceived() {
        return quantityReceived;
    }

    public void setQuantityReceived(String quantityReceived) {
        this.quantityReceived = quantityReceived;
    }

    public String getWareHouseName() {
        return WareHouseName;
    }

    public void setWareHouseName(String wareHouseName) {
        WareHouseName = wareHouseName;
    }

    public String getCombID() {
        return CombID;
    }

    public void setCombID(String combID) {
        CombID = combID;
    }

    public String getIslotfound() {
        return Islotfound;
    }

    public void setIslotfound(String islotfound) {
        Islotfound = islotfound;
    }

    public String getIsbatchfound() {
        return Isbatchfound;
    }

    public void setIsbatchfound(String isbatchfound) {
        Isbatchfound = isbatchfound;
    }

    public String getErrorMessage() {
        return ErrorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        ErrorMessage = errorMessage;
    }


    @Override
    public int compareTo(@NonNull Object obj) {
        int compareStatus=((InboundChildEntity)obj).getStatus();
        /* For Ascending order*/
        return this.status-compareStatus;

        /* For Descending order do like this */
        //return compareage-this.studentage;
    }
}
