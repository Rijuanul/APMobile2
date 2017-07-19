package id.advancepro.com.advancepro.model;

/**
 * Created by TASIV on 5/5/2017.
 */

public class InProgressCycleCountEntity {
    private String ID;
    private String Name;
    private String Picklocid;
    private String PickLocname;
    private String Statusid;
    private String Statusname;
    private String Warehousename;

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getPicklocid() {
        return Picklocid;
    }

    public void setPicklocid(String picklocid) {
        Picklocid = picklocid;
    }

    public String getPickLocname() {
        return PickLocname;
    }

    public void setPickLocname(String pickLocname) {
        PickLocname = pickLocname;
    }

    public String getStatusid() {
        return Statusid;
    }

    public void setStatusid(String statusid) {
        Statusid = statusid;
    }

    public String getStatusname() {
        return Statusname;
    }

    public void setStatusname(String statusname) {
        Statusname = statusname;
    }

    public String getWarehousename() {
        return Warehousename;
    }

    public void setWarehousename(String warehousename) {
        Warehousename = warehousename;
    }


}
