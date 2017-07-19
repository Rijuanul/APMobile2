package id.advancepro.com.advancepro.model;

/**
 * Created by TASIV on 6/28/2017.
 */

public class SalesCustListEntity {
    private Integer custID;
    private String custName;

    public Integer getCustID() {
        return custID;
    }

    public void setCustID(Integer custID) {
        this.custID = custID;
    }

    public String getCustName() {
        return custName;
    }

    public void setCustName(String custName) {
        this.custName = custName;
    }

    @Override
    public String toString() {
        return custName;
    }


}
