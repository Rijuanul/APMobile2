package id.advancepro.com.advancepro.service.SaleService;

import java.util.List;

import id.advancepro.com.advancepro.model.SalesOrderProductHierarchyEntity;
import id.advancepro.com.advancepro.model.SalesSearchProductEntity;
import id.advancepro.com.advancepro.model.SalesVariantItemEntity;
import id.advancepro.com.advancepro.model.SalesVariantProductInfoEntity;

/**
 * Created by TASIV on 7/5/2017.
 */

public interface SalesOrderService {
    public List<SalesOrderProductHierarchyEntity> getSalesOrderProductHierarchy(String urlLink);
    public List<SalesSearchProductEntity> getSalesSearchProduct(String urlLink,String service_url,String companyDBName,String passCode);
    public List<SalesVariantItemEntity> getSalesVariantDetails(String urlLink, String service_url, String companyDBName, String passCode,String prodID);
    public List<SalesVariantProductInfoEntity> getSalesVariantProdInfo(String urlLink, String service_url, String companyDBName, String passCode, String prodID, Integer custID, String jsonString);
}
