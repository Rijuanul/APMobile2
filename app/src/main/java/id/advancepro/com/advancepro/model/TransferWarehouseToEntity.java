package id.advancepro.com.advancepro.model;

/**
 * Created by TASIV on 4/13/2017.
 */

public class TransferWarehouseToEntity {
    private String WareHouseID;
    private String WareHouseName;

    public String getWareHouseID() {
        return WareHouseID;
    }

    public void setWareHouseID(String wareHouseID) {
        WareHouseID = wareHouseID;
    }

    public String getWareHouseName() {
        return WareHouseName;
    }

    public void setWareHouseName(String wareHouseName) {
        WareHouseName = wareHouseName;
    }

    @Override
    public String toString() {
        return WareHouseName;
    }
}
