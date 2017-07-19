package id.advancepro.com.advancepro.model;

/**
 * Created by TASIV on 3/22/2017.
 */

// Module Counts Entity
public class ModuleCountsEntity {
    private Integer OutboundCountID;
    private Integer OutboundCount;
    private Integer InboundCountID;
    private Integer InboundCount;
    private Integer WarehouseCountID;
    private Integer WarehouseCount;
    private Integer InboundStatus;
    private Integer OutboundStatus;
    private Integer WarehouseStatus;
    private Integer LookupStatus;
    private String ErrorMessage;

    public Integer getOutboundCountID() {
        return OutboundCountID;
    }

    public void setOutboundCountID(Integer outboundCountID) {
        OutboundCountID = outboundCountID;
    }

    public Integer getOutboundCount() {
        return OutboundCount;
    }

    public void setOutboundCount(Integer outboundCount) {
        OutboundCount = outboundCount;
    }

    public Integer getInboundCountID() {
        return InboundCountID;
    }

    public void setInboundCountID(Integer inboundCountID) {
        InboundCountID = inboundCountID;
    }

    public Integer getInboundCount() {
        return InboundCount;
    }

    public void setInboundCount(Integer inboundCount) {
        InboundCount = inboundCount;
    }

    public Integer getWarehouseCountID() {
        return WarehouseCountID;
    }

    public void setWarehouseCountID(Integer warehouseCountID) {
        WarehouseCountID = warehouseCountID;
    }

    public Integer getWarehouseCount() {
        return WarehouseCount;
    }

    public void setWarehouseCount(Integer warehouseCount) {
        WarehouseCount = warehouseCount;
    }

    public Integer getInboundStatus() {
        return InboundStatus;
    }

    public void setInboundStatus(Integer inboundStatus) {
        InboundStatus = inboundStatus;
    }

    public Integer getOutboundStatus() {
        return OutboundStatus;
    }

    public void setOutboundStatus(Integer outboundStatus) {
        OutboundStatus = outboundStatus;
    }

    public Integer getWarehouseStatus() {
        return WarehouseStatus;
    }

    public void setWarehouseStatus(Integer warehouseStatus) {
        WarehouseStatus = warehouseStatus;
    }

    public Integer getLookupStatus() {
        return LookupStatus;
    }

    public void setLookupStatus(Integer lookupStatus) {
        LookupStatus = lookupStatus;
    }

    public String getErrorMessage() {
        return ErrorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        ErrorMessage = errorMessage;
    }


}
