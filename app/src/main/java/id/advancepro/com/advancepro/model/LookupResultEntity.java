package id.advancepro.com.advancepro.model;

/**
 * Created by TASIV on 4/21/2017.
 */

public class LookupResultEntity {
    private String ProductID;
    private String Sku;
    private String Upc;
    private String Picklocname;
    private String Stock;
    private String Available;
    private String Unit;
    private String whid;
    private String warehousename;

    public String getProductID() {
        return ProductID;
    }

    public void setProductID(String productID) {
        ProductID = productID;
    }

    public String getSku() {
        return Sku;
    }

    public void setSku(String sku) {
        Sku = sku;
    }

    public String getUpc() {
        return Upc;
    }

    public void setUpc(String upc) {
        Upc = upc;
    }

    public String getPicklocname() {
        return Picklocname;
    }

    public void setPicklocname(String picklocname) {
        Picklocname = picklocname;
    }

    public String getStock() {
        return Stock;
    }

    public void setStock(String stock) {
        Stock = stock;
    }

    public String getAvailable() {
        return Available;
    }

    public void setAvailable(String available) {
        Available = available;
    }

    public String getUnit() {
        return Unit;
    }

    public void setUnit(String unit) {
        Unit = unit;
    }

    public String getWhid() {
        return whid;
    }

    public void setWhid(String whid) {
        this.whid = whid;
    }

    public String getWarehousename() {
        return warehousename;
    }

    public void setWarehousename(String warehousename) {
        this.warehousename = warehousename;
    }


}
