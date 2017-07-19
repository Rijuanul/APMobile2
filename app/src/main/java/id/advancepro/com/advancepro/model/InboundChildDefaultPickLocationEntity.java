package id.advancepro.com.advancepro.model;

/**
 * Created by TASIV on 4/6/2017.
 */

public class InboundChildDefaultPickLocationEntity {
    private String LocationName;
    private Integer LocationID;
    private Integer Isdefault;

    public String getLocationName() {
        return LocationName;
    }

    public void setLocationName(String locationName) {
        LocationName = locationName;
    }

    public Integer getLocationID() {
        return LocationID;
    }

    public void setLocationID(Integer locationID) {
        LocationID = locationID;
    }

    public Integer getIsdefault() {
        return Isdefault;
    }

    public void setIsdefault(Integer isdefault) {
        Isdefault = isdefault;
    }

    @Override
    public String toString() {
        return LocationName;
    }
}
