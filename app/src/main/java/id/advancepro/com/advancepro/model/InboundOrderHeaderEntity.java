package id.advancepro.com.advancepro.model;

/**
 * Created by TASIV on 4/3/2017.
 */

public class InboundOrderHeaderEntity {
    private String orderID;
    private String Purchaseno;
    private String CompanyName;
    private String TotQuantity;
    private String quantityReceived;
    private String Vpowhnotes;
    private String ErrorMessage;

    public String getOrderID() {
        return orderID;
    }

    public void setOrderID(String orderID) {
        this.orderID = orderID;
    }

    public String getPurchaseno() {
        return Purchaseno;
    }

    public void setPurchaseno(String purchaseno) {
        Purchaseno = purchaseno;
    }

    public String getCompanyName() {
        return CompanyName;
    }

    public void setCompanyName(String companyName) {
        CompanyName = companyName;
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

    public String getVpowhnotes() {
        return Vpowhnotes;
    }

    public void setVpowhnotes(String vpowhnotes) {
        Vpowhnotes = vpowhnotes;
    }

    public String getErrorMessage() {
        return ErrorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        ErrorMessage = errorMessage;
    }


}