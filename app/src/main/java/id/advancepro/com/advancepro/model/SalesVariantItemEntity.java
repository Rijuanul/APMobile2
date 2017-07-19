package id.advancepro.com.advancepro.model;

import java.util.List;

/**
 * Created by TASIV on 7/12/2017.
 */

public class SalesVariantItemEntity {

    private String variantheader;
    private List<SalesVariantDetailsEntity> salesVariantChildItem;
    public String getVariantheader() {
        return variantheader;
    }

    public void setVariantheader(String variantheader) {
        this.variantheader = variantheader;
    }

    public List<SalesVariantDetailsEntity> getSalesVariantChildItem() {
        return salesVariantChildItem;
    }

    public void setSalesVariantChildItem(List<SalesVariantDetailsEntity> salesVariantChildItem) {
        this.salesVariantChildItem = salesVariantChildItem;
    }


}
