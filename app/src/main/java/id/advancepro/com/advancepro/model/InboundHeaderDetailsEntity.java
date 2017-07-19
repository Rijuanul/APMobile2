package id.advancepro.com.advancepro.model;

/**
 * Created by TASIV on 3/29/2017.
 */

public class InboundHeaderDetailsEntity {

    private String OrderNo;
    private String Purchaseorderno;
    private String Companyname;
    private String Orderdate;
    private String Expecteddate;
    private String Quantity;
    private String Quantityreceived;
    private String Status;
    private String ErrorMessage;

    public String getVendorProductNameAndQty() {
        return vendorProductNameAndQty;
    }

    public void setVendorProductNameAndQty(String vendorProductNameAndQty) {
        this.vendorProductNameAndQty = vendorProductNameAndQty;
    }

    private String vendorProductNameAndQty;

    /*private List<String> vendorProductNameAndQty;
    public List<String> getVendorProductNameAndQty() {
        return vendorProductNameAndQty;
    }

    public void setVendorProductNameAndQty(List<String> vendorProductNameAndQty) {
        this.vendorProductNameAndQty = vendorProductNameAndQty;
    }
*/


    public String getOrderNo() {
        return OrderNo;
    }

    public void setOrderNo(String orderNo) {
        OrderNo = orderNo;
    }

    public String getPurchaseorderno() {
        return Purchaseorderno;
    }

    public void setPurchaseorderno(String purchaseorderno) {
        Purchaseorderno = purchaseorderno;
    }

    public String getCompanyname() {
        return Companyname;
    }

    public void setCompanyname(String companyname) {
        Companyname = companyname;
    }

    public String getOrderdate() {
        return Orderdate;
    }

    public void setOrderdate(String orderdate) {
        Orderdate = orderdate;
    }

    public String getExpecteddate() {
        return Expecteddate;
    }

    public void setExpecteddate(String expecteddate) {
        Expecteddate = expecteddate;
    }

    public String getQuantity() {
        return Quantity;
    }

    public void setQuantity(String quantity) {
        Quantity = quantity;
    }

    public String getQuantityreceived() {
        return Quantityreceived;
    }

    public void setQuantityreceived(String quantityreceived) {
        Quantityreceived = quantityreceived;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public String getErrorMessage() {
        return ErrorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        ErrorMessage = errorMessage;
    }


}
