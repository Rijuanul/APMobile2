package id.advancepro.com.advancepro.model;

/**
 * Created by Rijuanul on 4/18/2017.
 */

public class OutboundHeaderDetailsEntity {
    private String OrderNo;
    private String PoNo;
    private String Companyname;
    private String OrderDate;
    private String ExpectedDate;
    private String Quantity;
    private String StatusID;
    private String vendorProductNameAndQuantity;

    public String getVendorProductNameAndQuantity() {
        return vendorProductNameAndQuantity;
    }

    public void setVendorProductNameAndQuantity(String vendorProductNameAndQuantity) {
        this.vendorProductNameAndQuantity = vendorProductNameAndQuantity;
    }



    public String getOrderNo() {
        return OrderNo;
    }

    public void setOrderNo(String orderNo) {
        OrderNo = orderNo;
    }

    public String getPoNo() {
        return PoNo;
    }

    public void setPoNo(String poNo) {
        PoNo = poNo;
    }

    public String getCompanyname() {
        return Companyname;
    }

    public void setCompanyname(String companyname) {
        Companyname = companyname;
    }

    public String getOrderDate() {
        return OrderDate;
    }

    public void setOrderDate(String orderDate) {
        OrderDate = orderDate;
    }

    public String getExpectedDate() {
        return ExpectedDate;
    }

    public void setExpectedDate(String expectedDate) {
        ExpectedDate = expectedDate;
    }

    public String getQuantity() {
        return Quantity;
    }

    public void setQuantity(String quantity) {
        Quantity = quantity;
    }

    public String getStatusID() {
        return StatusID;
    }

    public void setStatusID(String statusID) {
        StatusID = statusID;
    }


}
