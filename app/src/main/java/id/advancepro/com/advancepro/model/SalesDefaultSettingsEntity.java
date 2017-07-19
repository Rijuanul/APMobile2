package id.advancepro.com.advancepro.model;

/**
 * Created by TASIV on 6/27/2017.
 */

public class SalesDefaultSettingsEntity {
    private Integer salesDefaultCustID;
    private Boolean salesIsDirectInvoice;
    private Boolean salesIsAllowCustEdit;

    public Boolean getSalesIsAllowCustEdit() {
        return salesIsAllowCustEdit;
    }

    public void setSalesIsAllowCustEdit(Boolean salesIsAllowCustEdit) {
        this.salesIsAllowCustEdit = salesIsAllowCustEdit;
    }

    public Integer getSalesDefaultCustID() {
        return salesDefaultCustID;
    }

    public Boolean getSalesIsDirectInvoice() {
        return salesIsDirectInvoice;
    }

    public void setSalesDefaultCustID(Integer salesDefaultCustID) {
        this.salesDefaultCustID = salesDefaultCustID;
    }

    public void setSalesIsDirectInvoice(Boolean salesIsDirectInvoice) {
        this.salesIsDirectInvoice = salesIsDirectInvoice;
    }


}
