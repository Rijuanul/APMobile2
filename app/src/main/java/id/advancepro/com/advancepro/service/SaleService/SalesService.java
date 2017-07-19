package id.advancepro.com.advancepro.service.SaleService;

import java.util.List;

import id.advancepro.com.advancepro.model.SalesCustListEntity;
import id.advancepro.com.advancepro.model.SalesDefaultSettingsEntity;
import id.advancepro.com.advancepro.model.SalesWarehouseEntity;

/**
 * Created by TASIV on 6/27/2017.
 */

public interface SalesService {
    public SalesDefaultSettingsEntity getSalesDefaultSettings(String url);
    public List<SalesWarehouseEntity> getSalesWH(String url);
    public List<SalesCustListEntity> getSalesCustList(String url);
    public String saveSalesSettings(String url,String jsonString);
}
