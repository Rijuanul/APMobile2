package id.advancepro.com.advancepro.model;

/**
 * Created by TASIV on 4/13/2017.
 */

public class TransferPLFromLocationEntity {

    private String Picklocid;
    private String PickLocname;

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

    @Override
    public String toString() {
        return PickLocname;
    }
}
