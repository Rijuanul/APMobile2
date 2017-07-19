package id.advancepro.com.advancepro.model;

/**
 * Created by TASIV on 4/11/2017.
 */

public class AdjustmentProductCheckEntity {
    private String ProductId;
    private String Combid;
    private String Sku;
    private String Upc;
    private String Islotserial;
    private String IsSerial;


    public String getProductId() {
        return ProductId;
    }

    public void setProductId(String productId) {
        ProductId = productId;
    }

    public String getCombid() {
        return Combid;
    }

    public void setCombid(String combid) {
        Combid = combid;
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

    public String getIslotserial() {
        return Islotserial;
    }

    public void setIslotserial(String islotserial) {
        Islotserial = islotserial;
    }

    public String getIsSerial() {
        return IsSerial;
    }

    public void setIsSerial(String isSerial) {
        IsSerial = isSerial;
    }



}
