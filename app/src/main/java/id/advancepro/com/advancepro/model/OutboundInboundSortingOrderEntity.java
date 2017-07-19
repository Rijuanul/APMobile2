package id.advancepro.com.advancepro.model;

/**
 * Created by TASIV on 5/1/2017.
 */

public class OutboundInboundSortingOrderEntity {


    private String sortingOrderValue;
    private String sortingOrderName;

    public String getSortingOrderValue() {
        return sortingOrderValue;
    }

    public void setSortingOrderValue(String sortingOrderValue) {
        this.sortingOrderValue = sortingOrderValue;
    }

    public String getSortingOrderName() {
        return sortingOrderName;
    }

    public void setSortingOrderName(String sortingOrderName) {
        this.sortingOrderName = sortingOrderName;
    }
    @Override
    public String toString() {
        return sortingOrderName;
    }

}
