package id.advancepro.com.advancepro.model;

import android.graphics.Bitmap;

/**
 * Created by Rijuanul on 7/8/2017.
 */

public class SalesSearchProductEntity {
    private Integer PRODUCT_ID;
    private Integer COMB_ID;
    private String PRODUCT_NAME;
    private String SKU;
    private Double COST;
    private Double PRICE;
    private Double PRICENORMAL;
    private Double QTY;
    private Double QTY_AVAIL;
    private Double QTY_RESERVED;
    private Double QTY_RETURNED;
    private Double QTY_IN_STOCK;
    private Double MIN_PURCHASE_QTY;
    private Boolean IS_VARIANT;
    private Boolean IS_PRODUCT_KIT;
    private Boolean IS_TAXABLE;
    private Integer KIT_ID;
    private Boolean In_MultWH;
    private String UnitName;
    private Boolean ProductUnitYN;
    private Boolean IS_SERVICE;
    private Integer ASSEMBLY_ID;
    private Integer INVENTORY_ID;
    private Boolean IS_DROPSHIP_ITEM;
    private Boolean IS_CUSTOMERSKU;
    private String imagePath;
    private Bitmap prodBitImage;
    private String ErrorMessage;

    public Bitmap getProdBitImage() {
        return prodBitImage;
    }

    public void setProdBitImage(Bitmap prodBitImage) {
        this.prodBitImage = prodBitImage;
    }



    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }



    public Integer getPRODUCT_ID() {
        return PRODUCT_ID;
    }

    public void setPRODUCT_ID(Integer PRODUCT_ID) {
        this.PRODUCT_ID = PRODUCT_ID;
    }

    public Integer getCOMB_ID() {
        return COMB_ID;
    }

    public void setCOMB_ID(Integer COMB_ID) {
        this.COMB_ID = COMB_ID;
    }

    public String getPRODUCT_NAME() {
        return PRODUCT_NAME;
    }

    public void setPRODUCT_NAME(String PRODUCT_NAME) {
        this.PRODUCT_NAME = PRODUCT_NAME;
    }

    public String getSKU() {
        return SKU;
    }

    public void setSKU(String SKU) {
        this.SKU = SKU;
    }

    public Double getCOST() {
        return COST;
    }

    public void setCOST(Double COST) {
        this.COST = COST;
    }

    public Double getPRICE() {
        return PRICE;
    }

    public void setPRICE(Double PRICE) {
        this.PRICE = PRICE;
    }

    public Double getPRICENORMAL() {
        return PRICENORMAL;
    }

    public void setPRICENORMAL(Double PRICENORMAL) {
        this.PRICENORMAL = PRICENORMAL;
    }

    public Double getQTY() {
        return QTY;
    }

    public void setQTY(Double QTY) {
        this.QTY = QTY;
    }

    public Double getQTY_AVAIL() {
        return QTY_AVAIL;
    }

    public void setQTY_AVAIL(Double QTY_AVAIL) {
        this.QTY_AVAIL = QTY_AVAIL;
    }

    public Double getQTY_RESERVED() {
        return QTY_RESERVED;
    }

    public void setQTY_RESERVED(Double QTY_RESERVED) {
        this.QTY_RESERVED = QTY_RESERVED;
    }

    public Double getQTY_RETURNED() {
        return QTY_RETURNED;
    }

    public void setQTY_RETURNED(Double QTY_RETURNED) {
        this.QTY_RETURNED = QTY_RETURNED;
    }

    public Double getQTY_IN_STOCK() {
        return QTY_IN_STOCK;
    }

    public void setQTY_IN_STOCK(Double QTY_IN_STOCK) {
        this.QTY_IN_STOCK = QTY_IN_STOCK;
    }

    public Double getMIN_PURCHASE_QTY() {
        return MIN_PURCHASE_QTY;
    }

    public void setMIN_PURCHASE_QTY(Double MIN_PURCHASE_QTY) {
        this.MIN_PURCHASE_QTY = MIN_PURCHASE_QTY;
    }

    public Boolean getIS_VARIANT() {
        return IS_VARIANT;
    }

    public void setIS_VARIANT(Boolean IS_VARIANT) {
        this.IS_VARIANT = IS_VARIANT;
    }

    public Boolean getIS_PRODUCT_KIT() {
        return IS_PRODUCT_KIT;
    }

    public void setIS_PRODUCT_KIT(Boolean IS_PRODUCT_KIT) {
        this.IS_PRODUCT_KIT = IS_PRODUCT_KIT;
    }

    public Boolean getIS_TAXABLE() {
        return IS_TAXABLE;
    }

    public void setIS_TAXABLE(Boolean IS_TAXABLE) {
        this.IS_TAXABLE = IS_TAXABLE;
    }

    public Integer getKIT_ID() {
        return KIT_ID;
    }

    public void setKIT_ID(Integer KIT_ID) {
        this.KIT_ID = KIT_ID;
    }

    public Boolean getIn_MultWH() {
        return In_MultWH;
    }

    public void setIn_MultWH(Boolean in_MultWH) {
        In_MultWH = in_MultWH;
    }

    public String getUnitName() {
        return UnitName;
    }

    public void setUnitName(String unitName) {
        UnitName = unitName;
    }

    public Boolean getProductUnitYN() {
        return ProductUnitYN;
    }

    public void setProductUnitYN(Boolean productUnitYN) {
        ProductUnitYN = productUnitYN;
    }

    public Boolean getIS_SERVICE() {
        return IS_SERVICE;
    }

    public void setIS_SERVICE(Boolean IS_SERVICE) {
        this.IS_SERVICE = IS_SERVICE;
    }

    public Integer getASSEMBLY_ID() {
        return ASSEMBLY_ID;
    }

    public void setASSEMBLY_ID(Integer ASSEMBLY_ID) {
        this.ASSEMBLY_ID = ASSEMBLY_ID;
    }

    public Integer getINVENTORY_ID() {
        return INVENTORY_ID;
    }

    public void setINVENTORY_ID(Integer INVENTORY_ID) {
        this.INVENTORY_ID = INVENTORY_ID;
    }

    public Boolean getIS_DROPSHIP_ITEM() {
        return IS_DROPSHIP_ITEM;
    }

    public void setIS_DROPSHIP_ITEM(Boolean IS_DROPSHIP_ITEM) {
        this.IS_DROPSHIP_ITEM = IS_DROPSHIP_ITEM;
    }

    public Boolean getIS_CUSTOMERSKU() {
        return IS_CUSTOMERSKU;
    }

    public void setIS_CUSTOMERSKU(Boolean IS_CUSTOMERSKU) {
        this.IS_CUSTOMERSKU = IS_CUSTOMERSKU;
    }

    public String getErrorMessage() {
        return ErrorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        ErrorMessage = errorMessage;
    }


}
