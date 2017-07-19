package id.advancepro.com.advancepro.model;

/**
 * Created by TASIV on 5/11/2017.
 */

public class CycleCountVendorEntity {
    private Integer ID;
    private String Name;
    public Integer getID() {
        return ID;
    }

    public void setID(Integer ID) {
        this.ID = ID;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    @Override
    public String toString() {
        return Name;
    }

}
