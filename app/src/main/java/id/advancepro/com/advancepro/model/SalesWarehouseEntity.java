package id.advancepro.com.advancepro.model;

/**
 * Created by TASIV on 6/27/2017.
 */

public class SalesWarehouseEntity {
    private Integer whID;
    private String whName;
    private Boolean isDefault;
    public Integer getWhID() {
        return whID;
    }

    public String getWhName() {
        return whName;
    }

    public Boolean getDefault() {
        return isDefault;
    }



    public void setWhID(Integer whID) {
        this.whID = whID;
    }

    public void setWhName(String whName) {
        this.whName = whName;
    }

    public void setDefault(Boolean aDefault) {
        isDefault = aDefault;
    }

    @Override
    public String toString() {
        return whName;
    }

}
