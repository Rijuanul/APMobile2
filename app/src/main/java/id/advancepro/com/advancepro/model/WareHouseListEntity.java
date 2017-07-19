package id.advancepro.com.advancepro.model;

/**
 * Created by TASIV on 3/23/2017.
 */

// Get Warehouse List Entity
public class WareHouseListEntity {

    private Integer warhouseID;
    private String warehouseName;

    public Integer getWarhouseID() {
        return warhouseID;
    }

    public void setWarhouseID(Integer warhouseID) {
        this.warhouseID = warhouseID;
    }

    public String getWarehouseName() {
        return warehouseName;
    }

    public void setWarehouseName(String warehouseName) {
        this.warehouseName = warehouseName;
    }

    @Override
    public String toString() {
        return warehouseName;
    }
}
