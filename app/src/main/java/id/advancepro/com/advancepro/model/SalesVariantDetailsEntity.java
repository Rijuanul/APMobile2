package id.advancepro.com.advancepro.model;

/**
 * Created by TASIV on 7/12/2017.
 */

public class SalesVariantDetailsEntity {
    private String propID;
    private String description;
    private String varID;
    private Boolean isSelect;

    public String getPropID() {
        return propID;
    }

    public void setPropID(String propID) {
        this.propID = propID;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getVarID() {
        return varID;
    }

    public void setVarID(String varID) {
        this.varID = varID;
    }
    public Boolean getSelect() {
        return isSelect;
    }

    public void setSelect(Boolean select) {
        isSelect = select;
    }
}
