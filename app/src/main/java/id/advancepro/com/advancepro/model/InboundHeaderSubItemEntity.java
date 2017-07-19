package id.advancepro.com.advancepro.model;

/**
 * Created by TASIV on 3/29/2017.
 */

public class InboundHeaderSubItemEntity {

    private Integer orderNo;
    private String vandorProductName;
    private String vendorProductQty;

    public Integer getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(Integer orderNo) {
        this.orderNo = orderNo;
    }

    public String getVandorProductName() {
        return vandorProductName;
    }

    public void setVandorProductName(String vandorProductName) {
        this.vandorProductName = vandorProductName;
    }

    public String getVendorProductQty() {
        return vendorProductQty;
    }

    public void setVendorProductQty(String vendorProductQty) {
        this.vendorProductQty = vendorProductQty;
    }


}
