package id.advancepro.com.advancepro.model;

/**
 * Created by TASIV on 4/10/2017.
 */

public class AdjustmentsPickLocationEntity {
    private String IsDefault;
    private String PickLocNameid;
    private String Picklocname;
    private String Statusid;
    private String TotalQty;

    public String getIsDefault() {
        return IsDefault;
    }

    public void setIsDefault(String isDefault) {
        IsDefault = isDefault;
    }

    public String getPickLocNameid() {
        return PickLocNameid;
    }

    public void setPickLocNameid(String pickLocNameid) {
        PickLocNameid = pickLocNameid;
    }

    public String getPicklocname() {
        return Picklocname;
    }

    public void setPicklocname(String picklocname) {
        Picklocname = picklocname;
    }

    public String getStatusid() {
        return Statusid;
    }

    public void setStatusid(String statusid) {
        Statusid = statusid;
    }

    public String getTotalQty() {
        return TotalQty;
    }

    public void setTotalQty(String totalQty) {
        TotalQty = totalQty;
    }

    @Override
    public String toString() {
        return Picklocname;
    }

}
