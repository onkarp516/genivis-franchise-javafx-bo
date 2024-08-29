package com.opethic.genivis.dto.reqres.product;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**** uses in GET BY PRODUCT ID  ****/
public class ProductResponseObject {
    @SerializedName("productName")
    @Expose
    private String productName;
    @SerializedName("id")
    @Expose
    private Long id;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("productCode")
    @Expose
    private String productCode;
    @SerializedName("isBatchNo")
    @Expose
    private Boolean isBatchNo;
    @SerializedName("isInventory")
    @Expose
    private Boolean isInventory;
    @SerializedName("isSerialNo")
    @Expose
    private Boolean isSerialNo;
    @SerializedName("barcodeNo")
    @Expose
    private Object barcodeNo;
    @SerializedName("shelfId")
    @Expose
    private Object shelfId;
    @SerializedName("barcodeSalesQty")
    @Expose
    private Object barcodeSalesQty;
    @SerializedName("purchaseRate")
    @Expose
    private Object purchaseRate;
    @SerializedName("margin")
    @Expose
    private Object margin;
    @SerializedName("brandId")
    @Expose
    private Long brandId;
    @SerializedName("packagingId")
    @Expose
    private Long packagingId;
    @SerializedName("groupId")
    @Expose
    private Long groupId;
    @SerializedName("subgroupId")
    @Expose
    private Long subgroupId;
    @SerializedName("categoryId")
    @Expose
    private Long categoryId;
    @SerializedName("subcategoryId")
    @Expose
    private Long subcategoryId;
    @SerializedName("weight")
    @Expose
    private Object weight;
    @SerializedName("weightUnit")
    @Expose
    private Object weightUnit;
    @SerializedName("disPer1")
    @Expose
    private Object disPer1;
    @SerializedName("hsnNo")
    @Expose
    private Long hsnNo;
    @SerializedName("tax")
    @Expose
    private Long tax;
    @SerializedName("taxApplicableDate")
    @Expose
    private Object taxApplicableDate;
    @SerializedName("taxType")
    @Expose
    private String taxType;
    @SerializedName("igst")
    @Expose
    private Double igst;
    @SerializedName("cgst")
    @Expose
    private Double cgst;
    @SerializedName("sgst")
    @Expose
    private Double sgst;
    @SerializedName("fsrmh")
    @Expose
    private Double fsrmh;
    @SerializedName("fsrai")
    @Expose
    private Double fsrai;
    @SerializedName("csrmh")
    @Expose
    private Double csrmh;
    @SerializedName("csrai")
    @Expose
    private Double csrai;
    @SerializedName("minStock")
    @Expose
    private Double minStock;
    @SerializedName("maxStock")
    @Expose
    private Double maxStock;
    @SerializedName("isWarranty")
    @Expose
    private Boolean isWarranty;
    @SerializedName("nodays")
    @Expose
    private Long nodays;
    @SerializedName("isCommision")
    @Expose
    private Boolean isCommision;
    @SerializedName("isGVProducts")
    @Expose
    private Boolean isGVProducts;
    @SerializedName("gvOfProducts")
    @Expose
    private String gvOfProducts;
    @SerializedName("drugType")
    @Expose
    private String drugType;
    @SerializedName("isMIS")
    @Expose
    private Boolean isMIS;
    @SerializedName("isGroup")
    @Expose
    private Boolean isGroup;
    @SerializedName("isFormulation")
    @Expose
    private Boolean isFormulation;
    @SerializedName("isCategory")
    @Expose
    private Boolean isCategory;
    @SerializedName("isSubcategory")
    @Expose
    private Boolean isSubcategory;
    @SerializedName("isPrescription")
    @Expose
    private Boolean isPrescription;
    @SerializedName("drugContent")
    @Expose
    private String drugContent;
    @SerializedName("uploadImage")
    @Expose
    private String uploadImage;
    @SerializedName("isEcom")
    @Expose
    private Boolean isEcom;
    @SerializedName("ecomType")
    @Expose
    private String ecomType;
    @SerializedName("ecomPrice")
    @Expose
    private String ecomPrice;
    @SerializedName("ecomDiscount")
    @Expose
    private String ecomDiscount;
    @SerializedName("ecomAmount")
    @Expose
    private String ecomAmount;
    @SerializedName("ecomLoyality")
    @Expose
    private String ecomLoyality;
    @SerializedName("imageExists")
    @Expose
    private Boolean imageExists;
    @SerializedName("prevImage1")
    @Expose
    private String prevImage1;
    @SerializedName("prevImage2")
    @Expose
    private String prevImage2;
    @SerializedName("prevImage3")
    @Expose
    private String prevImage3;
    @SerializedName("prevImage4")
    @Expose
    private String prevImage4;
    @SerializedName("prevImage5")
    @Expose
    private String prevImage5;
    @SerializedName("productType")
    @Expose
    private Object productType;
    @SerializedName("contentMap")
    @Expose
    private List<ProductContentMap> contentMap;
    @SerializedName("productrows")
    @Expose
    private List<Productrow> productrows;

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public Boolean getIsBatchNo() {
        return isBatchNo;
    }

    public void setIsBatchNo(Boolean isBatchNo) {
        this.isBatchNo = isBatchNo;
    }

    public Boolean getIsInventory() {
        return isInventory;
    }

    public void setIsInventory(Boolean isInventory) {
        this.isInventory = isInventory;
    }

    public Boolean getIsSerialNo() {
        return isSerialNo;
    }

    public void setIsSerialNo(Boolean isSerialNo) {
        this.isSerialNo = isSerialNo;
    }

    public Object getBarcodeNo() {
        return barcodeNo;
    }

    public void setBarcodeNo(Object barcodeNo) {
        this.barcodeNo = barcodeNo;
    }

    public Object getShelfId() {
        return shelfId;
    }

    public void setShelfId(Object shelfId) {
        this.shelfId = shelfId;
    }

    public Object getBarcodeSalesQty() {
        return barcodeSalesQty;
    }

    public void setBarcodeSalesQty(Object barcodeSalesQty) {
        this.barcodeSalesQty = barcodeSalesQty;
    }

    public Object getPurchaseRate() {
        return purchaseRate;
    }

    public void setPurchaseRate(Object purchaseRate) {
        this.purchaseRate = purchaseRate;
    }

    public Boolean getBatchNo() {
        return isBatchNo;
    }

    public void setBatchNo(Boolean batchNo) {
        isBatchNo = batchNo;
    }

    public Boolean getInventory() {
        return isInventory;
    }

    public void setInventory(Boolean inventory) {
        isInventory = inventory;
    }

    public Boolean getSerialNo() {
        return isSerialNo;
    }

    public void setSerialNo(Boolean serialNo) {
        isSerialNo = serialNo;
    }

    public Double getFsrmh() {
        return fsrmh;
    }

    public void setFsrmh(Double fsrmh) {
        this.fsrmh = fsrmh;
    }

    public Double getFsrai() {
        return fsrai;
    }

    public void setFsrai(Double fsrai) {
        this.fsrai = fsrai;
    }

    public Double getCsrmh() {
        return csrmh;
    }

    public void setCsrmh(Double csrmh) {
        this.csrmh = csrmh;
    }

    public Double getCsrai() {
        return csrai;
    }

    public void setCsrai(Double csrai) {
        this.csrai = csrai;
    }

    public Boolean getWarranty() {
        return isWarranty;
    }

    public void setWarranty(Boolean warranty) {
        isWarranty = warranty;
    }

    public Boolean getCommision() {
        return isCommision;
    }

    public void setCommision(Boolean commision) {
        isCommision = commision;
    }

    public Boolean getGVProducts() {
        return isGVProducts;
    }

    public void setGVProducts(Boolean GVProducts) {
        isGVProducts = GVProducts;
    }

    public Boolean getMIS() {
        return isMIS;
    }

    public void setMIS(Boolean MIS) {
        isMIS = MIS;
    }

    public Boolean getGroup() {
        return isGroup;
    }

    public void setGroup(Boolean group) {
        isGroup = group;
    }

    public Boolean getFormulation() {
        return isFormulation;
    }

    public void setFormulation(Boolean formulation) {
        isFormulation = formulation;
    }

    public Boolean getCategory() {
        return isCategory;
    }

    public void setCategory(Boolean category) {
        isCategory = category;
    }

    public Boolean getSubcategory() {
        return isSubcategory;
    }

    public void setSubcategory(Boolean subcategory) {
        isSubcategory = subcategory;
    }

    public Boolean getPrescription() {
        return isPrescription;
    }

    public void setPrescription(Boolean prescription) {
        isPrescription = prescription;
    }

    public Boolean getEcom() {
        return isEcom;
    }

    public void setEcom(Boolean ecom) {
        isEcom = ecom;
    }

    public Object getMargin() {
        return margin;
    }

    public void setMargin(Object margin) {
        this.margin = margin;
    }

    public Long getBrandId() {
        return brandId;
    }

    public void setBrandId(Long brandId) {
        this.brandId = brandId;
    }

    public Long getPackagingId() {
        return packagingId;
    }

    public void setPackagingId(Long packagingId) {
        this.packagingId = packagingId;
    }

    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    public Long getSubgroupId() {
        return subgroupId;
    }

    public void setSubgroupId(Long subgroupId) {
        this.subgroupId = subgroupId;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public Long getSubcategoryId() {
        return subcategoryId;
    }

    public void setSubcategoryId(Long subcategoryId) {
        this.subcategoryId = subcategoryId;
    }

    public Object getWeight() {
        return weight;
    }

    public void setWeight(Object weight) {
        this.weight = weight;
    }

    public Object getWeightUnit() {
        return weightUnit;
    }

    public void setWeightUnit(Object weightUnit) {
        this.weightUnit = weightUnit;
    }

    public Object getDisPer1() {
        return disPer1;
    }

    public void setDisPer1(Object disPer1) {
        this.disPer1 = disPer1;
    }

    public Long getHsnNo() {
        return hsnNo;
    }

    public void setHsnNo(Long hsnNo) {
        this.hsnNo = hsnNo;
    }

    public Long getTax() {
        return tax;
    }

    public void setTax(Long tax) {
        this.tax = tax;
    }

    public Object getTaxApplicableDate() {
        return taxApplicableDate;
    }

    public void setTaxApplicableDate(Object taxApplicableDate) {
        this.taxApplicableDate = taxApplicableDate;
    }

    public String getTaxType() {
        return taxType;
    }

    public void setTaxType(String taxType) {
        this.taxType = taxType;
    }

    public Double getIgst() {
        return igst;
    }

    public void setIgst(Double igst) {
        this.igst = igst;
    }

    public Double getCgst() {
        return cgst;
    }

    public void setCgst(Double cgst) {
        this.cgst = cgst;
    }

    public Double getSgst() {
        return sgst;
    }

    public void setSgst(Double sgst) {
        this.sgst = sgst;
    }

    public Double getMinStock() {
        return minStock;
    }

    public void setMinStock(Double minStock) {
        this.minStock = minStock;
    }

    public Double getMaxStock() {
        return maxStock;
    }

    public void setMaxStock(Double maxStock) {
        this.maxStock = maxStock;
    }

    public Boolean getIsWarranty() {
        return isWarranty;
    }

    public void setIsWarranty(Boolean isWarranty) {
        this.isWarranty = isWarranty;
    }

    public Long getNodays() {
        return nodays;
    }

    public void setNodays(Long nodays) {
        this.nodays = nodays;
    }

    public Boolean getIsCommision() {
        return isCommision;
    }

    public void setIsCommision(Boolean isCommision) {
        this.isCommision = isCommision;
    }

    public Boolean getIsGVProducts() {
        return isGVProducts;
    }

    public void setIsGVProducts(Boolean isGVProducts) {
        this.isGVProducts = isGVProducts;
    }

    public String getGvOfProducts() {
        return gvOfProducts;
    }

    public void setGvOfProducts(String gvOfProducts) {
        this.gvOfProducts = gvOfProducts;
    }

    public String getDrugType() {
        return drugType;
    }

    public void setDrugType(String drugType) {
        this.drugType = drugType;
    }

    public Boolean getIsMIS() {
        return isMIS;
    }

    public void setIsMIS(Boolean isMIS) {
        this.isMIS = isMIS;
    }

    public Boolean getIsGroup() {
        return isGroup;
    }

    public void setIsGroup(Boolean isGroup) {
        this.isGroup = isGroup;
    }

    public Boolean getIsFormulation() {
        return isFormulation;
    }

    public void setIsFormulation(Boolean isFormulation) {
        this.isFormulation = isFormulation;
    }

    public Boolean getIsCategory() {
        return isCategory;
    }

    public void setIsCategory(Boolean isCategory) {
        this.isCategory = isCategory;
    }

    public Boolean getIsSubcategory() {
        return isSubcategory;
    }

    public void setIsSubcategory(Boolean isSubcategory) {
        this.isSubcategory = isSubcategory;
    }

    public Boolean getIsPrescription() {
        return isPrescription;
    }

    public void setIsPrescription(Boolean isPrescription) {
        this.isPrescription = isPrescription;
    }

    public String getDrugContent() {
        return drugContent;
    }

    public void setDrugContent(String drugContent) {
        this.drugContent = drugContent;
    }

    public String getUploadImage() {
        return uploadImage;
    }

    public void setUploadImage(String uploadImage) {
        this.uploadImage = uploadImage;
    }

    public Boolean getIsEcom() {
        return isEcom;
    }

    public void setIsEcom(Boolean isEcom) {
        this.isEcom = isEcom;
    }

    public String getEcomType() {
        return ecomType;
    }

    public void setEcomType(String ecomType) {
        this.ecomType = ecomType;
    }

    public String getEcomPrice() {
        return ecomPrice;
    }

    public void setEcomPrice(String ecomPrice) {
        this.ecomPrice = ecomPrice;
    }

    public String getEcomDiscount() {
        return ecomDiscount;
    }

    public void setEcomDiscount(String ecomDiscount) {
        this.ecomDiscount = ecomDiscount;
    }

    public String getEcomAmount() {
        return ecomAmount;
    }

    public void setEcomAmount(String ecomAmount) {
        this.ecomAmount = ecomAmount;
    }

    public String getEcomLoyality() {
        return ecomLoyality;
    }

    public void setEcomLoyality(String ecomLoyality) {
        this.ecomLoyality = ecomLoyality;
    }

    public Boolean getImageExists() {
        return imageExists;
    }

    public void setImageExists(Boolean imageExists) {
        this.imageExists = imageExists;
    }

    public String getPrevImage1() {
        return prevImage1;
    }

    public void setPrevImage1(String prevImage1) {
        this.prevImage1 = prevImage1;
    }

    public String getPrevImage2() {
        return prevImage2;
    }

    public void setPrevImage2(String prevImage2) {
        this.prevImage2 = prevImage2;
    }

    public String getPrevImage3() {
        return prevImage3;
    }

    public void setPrevImage3(String prevImage3) {
        this.prevImage3 = prevImage3;
    }

    public String getPrevImage4() {
        return prevImage4;
    }

    public void setPrevImage4(String prevImage4) {
        this.prevImage4 = prevImage4;
    }

    public String getPrevImage5() {
        return prevImage5;
    }

    public void setPrevImage5(String prevImage5) {
        this.prevImage5 = prevImage5;
    }

    public Object getProductType() {
        return productType;
    }

    public void setProductType(Object productType) {
        this.productType = productType;
    }

    public List<ProductContentMap> getContentMap() {
        return contentMap;
    }

    public void setContentMap(List<ProductContentMap> contentMap) {
        this.contentMap = contentMap;
    }

    public List<Productrow> getProductrows() {
        return productrows;
    }

    public void setProductrows(List<Productrow> productrows) {
        this.productrows = productrows;
    }

    @Override
    public String toString() {
        return "ProductResponseObject{" +
                "productName='" + productName + '\'' +
                ", id=" + id +
                ", description='" + description + '\'' +
                ", productCode='" + productCode + '\'' +
                ", isBatchNo=" + isBatchNo +
                ", isInventory=" + isInventory +
                ", isSerialNo=" + isSerialNo +
                ", barcodeNo=" + barcodeNo +
                ", shelfId=" + shelfId +
                ", barcodeSalesQty=" + barcodeSalesQty +
                ", purchaseRate=" + purchaseRate +
                ", margin=" + margin +
                ", brandId=" + brandId +
                ", packagingId=" + packagingId +
                ", groupId=" + groupId +
                ", subgroupId=" + subgroupId +
                ", categoryId=" + categoryId +
                ", subcategoryId=" + subcategoryId +
                ", weight=" + weight +
                ", weightUnit=" + weightUnit +
                ", disPer1=" + disPer1 +
                ", hsnNo=" + hsnNo +
                ", tax=" + tax +
                ", taxApplicableDate=" + taxApplicableDate +
                ", taxType='" + taxType + '\'' +
                ", igst=" + igst +
                ", cgst=" + cgst +
                ", sgst=" + sgst +
                ", minStock=" + minStock +
                ", maxStock=" + maxStock +
                ", isWarranty=" + isWarranty +
                ", nodays=" + nodays +
                ", isCommision=" + isCommision +
                ", isGVProducts=" + isGVProducts +
                ", gvOfProducts='" + gvOfProducts + '\'' +
                ", drugType='" + drugType + '\'' +
                ", isMIS=" + isMIS +
                ", isGroup=" + isGroup +
                ", isFormulation=" + isFormulation +
                ", isCategory=" + isCategory +
                ", isSubcategory=" + isSubcategory +
                ", isPrescription=" + isPrescription +
                ", drugContent='" + drugContent + '\'' +
                ", uploadImage='" + uploadImage + '\'' +
                ", isEcom=" + isEcom +
                ", ecomType='" + ecomType + '\'' +
                ", ecomPrice='" + ecomPrice + '\'' +
                ", ecomDiscount='" + ecomDiscount + '\'' +
                ", ecomAmount='" + ecomAmount + '\'' +
                ", ecomLoyality='" + ecomLoyality + '\'' +
                ", imageExists=" + imageExists +
                ", prevImage1='" + prevImage1 + '\'' +
                ", prevImage2='" + prevImage2 + '\'' +
                ", prevImage3='" + prevImage3 + '\'' +
                ", prevImage4='" + prevImage4 + '\'' +
                ", prevImage5='" + prevImage5 + '\'' +
                ", productType=" + productType +
                ", contentMap=" + contentMap +
                ", productrows=" + productrows +
                '}';
    }
}
