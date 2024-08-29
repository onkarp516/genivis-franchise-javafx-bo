package com.opethic.genivis.dto.reqres;

import com.opethic.genivis.dto.UnitRateList;
import com.opethic.genivis.dto.cmp_t_row.BatchWindowTableDTO;
import com.opethic.genivis.models.tranx.sales.TranxSelectedBatch;
import com.opethic.genivis.models.tranx.sales.TranxSelectedProduct;

import java.util.ArrayList;
import java.util.List;


public class PurTranxToPrdRediRowDTO {

    private String purchase_id ;
    private String id ;
    private String details_id ;
    private String product_id ;
    private String current_index;

    private String sr_no ;
    private String particulars ;

    private String packages ;
    private String levelA ;
    private String levelB ;
    private String levelC ;
    private String unit ;

    private String levelA_id ;
    private String levelB_id ;
    private String levelC_id ;
    private String unit_id ;

    private String batch_or_serial ;
    private String quantity ;

    private String free ;
    ;
    private String rate ;
    private String gross_amount ;
    private String dis_per ;
    private String dis_amt ;
    private String tax ;
    private String net_amount ;
    private String action;

    private String is_batch ;

    private String is_serial ;
    private List<BatchWindowTableDTO> batchWindowTableDTOList;

    private String ledger_id ;

    private String dis_per2 ;

    private String org_net_amt ;

    private String taxable_amt ;

    private String total_taxable_amt ;

    private String igst ;

    private String sgst ;

    private String cgst ;

    private String final_tax_amt ;

    private String final_dis_amt ;

    private String base_amt ;

    private String dis_per_cal ;

    private String dis_amt_cal ;

    private String row_dis_amt ;

    private String gross_amount1 ;

    private String gst ;

    private String igst_per ;

    private String sgst_per ;

    private String cgst_per ;

    private String final_amount ;

    private String total_igst ;

    private String total_sgst ;

    private String total_cgst ;

    private String inventoryId ;

    private String unit_conv ;

    private String invoice_dis_amt ;

    private String total_amt ;

    private String b_details_id ;

    private String b_no ;

    private String b_rate ;

    private String b_purchase_rate ;

    private String b_expiry ;

    private String rate_a ;

    private String rate_b ;

    private String rate_c ;

    private String rate_d ;

    private String costing ;

    private String costing_with_tax ;

    private String min_margin ;

    private String manufacturing_date ;

    private String reference_type ;

    private String reference_id ;

    private String add_chg_amt ;

    private String transaction_status ;

    private String is_expired;

    List<UnitRateList> rateList = new ArrayList<>();


    //! Specific for Sales Invoice
    private TranxSelectedProduct selectedProduct;
    private TranxSelectedBatch selectedBatch;
    //! Specific for Sales Invoice


    public String getPurchase_id() {
        return purchase_id;
    }

    public void setPurchase_id(String purchase_id) {
        this.purchase_id = purchase_id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDetails_id() {
        return details_id;
    }

    public void setDetails_id(String details_id) {
        this.details_id = details_id;
    }

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public String getCurrent_index() {
        return current_index;
    }

    public void setCurrent_index(String current_index) {
        this.current_index = current_index;
    }

    public String getSr_no() {
        return sr_no;
    }

    public void setSr_no(String sr_no) {
        this.sr_no = sr_no;
    }

    public String getParticulars() {
        return particulars;
    }

    public void setParticulars(String particulars) {
        this.particulars = particulars;
    }

    public String getPackages() {
        return packages;
    }

    public void setPackages(String packages) {
        this.packages = packages;
    }

    public String getLevelA() {
        return levelA;
    }

    public void setLevelA(String levelA) {
        this.levelA = levelA;
    }

    public String getLevelB() {
        return levelB;
    }

    public void setLevelB(String levelB) {
        this.levelB = levelB;
    }

    public String getLevelC() {
        return levelC;
    }

    public void setLevelC(String levelC) {
        this.levelC = levelC;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getLevelA_id() {
        return levelA_id;
    }

    public void setLevelA_id(String levelA_id) {
        this.levelA_id = levelA_id;
    }

    public String getLevelB_id() {
        return levelB_id;
    }

    public void setLevelB_id(String levelB_id) {
        this.levelB_id = levelB_id;
    }

    public String getLevelC_id() {
        return levelC_id;
    }

    public void setLevelC_id(String levelC_id) {
        this.levelC_id = levelC_id;
    }

    public String getUnit_id() {
        return unit_id;
    }

    public void setUnit_id(String unit_id) {
        this.unit_id = unit_id;
    }

    public String getBatch_or_serial() {
        return batch_or_serial;
    }

    public void setBatch_or_serial(String batch_or_serial) {
        this.batch_or_serial = batch_or_serial;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getFree() {
        return free;
    }

    public void setFree(String free) {
        this.free = free;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public String getGross_amount() {
        return gross_amount;
    }

    public void setGross_amount(String gross_amount) {
        this.gross_amount = gross_amount;
    }

    public String getDis_per() {
        return dis_per;
    }

    public void setDis_per(String dis_per) {
        this.dis_per = dis_per;
    }

    public String getDis_amt() {
        return dis_amt;
    }

    public void setDis_amt(String dis_amt) {
        this.dis_amt = dis_amt;
    }

    public String getTax() {
        return tax;
    }

    public void setTax(String tax) {
        this.tax = tax;
    }

    public String getNet_amount() {
        return net_amount;
    }

    public void setNet_amount(String net_amount) {
        this.net_amount = net_amount;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getIs_batch() {
        return is_batch;
    }

    public void setIs_batch(String is_batch) {
        this.is_batch = is_batch;
    }

    public String getIs_serial() {
        return is_serial;
    }

    public void setIs_serial(String is_serial) {
        this.is_serial = is_serial;
    }

    public List<BatchWindowTableDTO> getBatchWindowTableDTOList() {
        return batchWindowTableDTOList;
    }

    public void setBatchWindowTableDTOList(List<BatchWindowTableDTO> batchWindowTableDTOList) {
        this.batchWindowTableDTOList = batchWindowTableDTOList;
    }

    public String getLedger_id() {
        return ledger_id;
    }

    public void setLedger_id(String ledger_id) {
        this.ledger_id = ledger_id;
    }

    public String getDis_per2() {
        return dis_per2;
    }

    public void setDis_per2(String dis_per2) {
        this.dis_per2 = dis_per2;
    }

    public String getOrg_net_amt() {
        return org_net_amt;
    }

    public void setOrg_net_amt(String org_net_amt) {
        this.org_net_amt = org_net_amt;
    }

    public String getTaxable_amt() {
        return taxable_amt;
    }

    public void setTaxable_amt(String taxable_amt) {
        this.taxable_amt = taxable_amt;
    }

    public String getTotal_taxable_amt() {
        return total_taxable_amt;
    }

    public void setTotal_taxable_amt(String total_taxable_amt) {
        this.total_taxable_amt = total_taxable_amt;
    }

    public String getIgst() {
        return igst;
    }

    public void setIgst(String igst) {
        this.igst = igst;
    }

    public String getSgst() {
        return sgst;
    }

    public void setSgst(String sgst) {
        this.sgst = sgst;
    }

    public String getCgst() {
        return cgst;
    }

    public void setCgst(String cgst) {
        this.cgst = cgst;
    }

    public String getFinal_tax_amt() {
        return final_tax_amt;
    }

    public void setFinal_tax_amt(String final_tax_amt) {
        this.final_tax_amt = final_tax_amt;
    }

    public String getFinal_dis_amt() {
        return final_dis_amt;
    }

    public void setFinal_dis_amt(String final_dis_amt) {
        this.final_dis_amt = final_dis_amt;
    }

    public String getBase_amt() {
        return base_amt;
    }

    public void setBase_amt(String base_amt) {
        this.base_amt = base_amt;
    }

    public String getDis_per_cal() {
        return dis_per_cal;
    }

    public void setDis_per_cal(String dis_per_cal) {
        this.dis_per_cal = dis_per_cal;
    }

    public String getDis_amt_cal() {
        return dis_amt_cal;
    }

    public void setDis_amt_cal(String dis_amt_cal) {
        this.dis_amt_cal = dis_amt_cal;
    }

    public String getRow_dis_amt() {
        return row_dis_amt;
    }

    public void setRow_dis_amt(String row_dis_amt) {
        this.row_dis_amt = row_dis_amt;
    }

    public String getGross_amount1() {
        return gross_amount1;
    }

    public void setGross_amount1(String gross_amount1) {
        this.gross_amount1 = gross_amount1;
    }

    public String getGst() {
        return gst;
    }

    public void setGst(String gst) {
        this.gst = gst;
    }

    public String getIgst_per() {
        return igst_per;
    }

    public void setIgst_per(String igst_per) {
        this.igst_per = igst_per;
    }

    public String getSgst_per() {
        return sgst_per;
    }

    public void setSgst_per(String sgst_per) {
        this.sgst_per = sgst_per;
    }

    public String getCgst_per() {
        return cgst_per;
    }

    public void setCgst_per(String cgst_per) {
        this.cgst_per = cgst_per;
    }

    public String getFinal_amount() {
        return final_amount;
    }

    public void setFinal_amount(String final_amount) {
        this.final_amount = final_amount;
    }

    public String getTotal_igst() {
        return total_igst;
    }

    public void setTotal_igst(String total_igst) {
        this.total_igst = total_igst;
    }

    public String getTotal_sgst() {
        return total_sgst;
    }

    public void setTotal_sgst(String total_sgst) {
        this.total_sgst = total_sgst;
    }

    public String getTotal_cgst() {
        return total_cgst;
    }

    public void setTotal_cgst(String total_cgst) {
        this.total_cgst = total_cgst;
    }

    public String getInventoryId() {
        return inventoryId;
    }

    public void setInventoryId(String inventoryId) {
        this.inventoryId = inventoryId;
    }

    public String getUnit_conv() {
        return unit_conv;
    }

    public void setUnit_conv(String unit_conv) {
        this.unit_conv = unit_conv;
    }

    public String getInvoice_dis_amt() {
        return invoice_dis_amt;
    }

    public void setInvoice_dis_amt(String invoice_dis_amt) {
        this.invoice_dis_amt = invoice_dis_amt;
    }

    public String getTotal_amt() {
        return total_amt;
    }

    public void setTotal_amt(String total_amt) {
        this.total_amt = total_amt;
    }

    public String getB_details_id() {
        return b_details_id;
    }

    public void setB_details_id(String b_details_id) {
        this.b_details_id = b_details_id;
    }

    public String getB_no() {
        return b_no;
    }

    public void setB_no(String b_no) {
        this.b_no = b_no;
    }

    public String getB_rate() {
        return b_rate;
    }

    public void setB_rate(String b_rate) {
        this.b_rate = b_rate;
    }

    public String getB_purchase_rate() {
        return b_purchase_rate;
    }

    public void setB_purchase_rate(String b_purchase_rate) {
        this.b_purchase_rate = b_purchase_rate;
    }

    public String getB_expiry() {
        return b_expiry;
    }

    public void setB_expiry(String b_expiry) {
        this.b_expiry = b_expiry;
    }

    public String getRate_a() {
        return rate_a;
    }

    public void setRate_a(String rate_a) {
        this.rate_a = rate_a;
    }

    public String getRate_b() {
        return rate_b;
    }

    public void setRate_b(String rate_b) {
        this.rate_b = rate_b;
    }

    public String getRate_c() {
        return rate_c;
    }

    public void setRate_c(String rate_c) {
        this.rate_c = rate_c;
    }

    public String getRate_d() {
        return rate_d;
    }

    public void setRate_d(String rate_d) {
        this.rate_d = rate_d;
    }

    public String getCosting() {
        return costing;
    }

    public void setCosting(String costing) {
        this.costing = costing;
    }

    public String getCosting_with_tax() {
        return costing_with_tax;
    }

    public void setCosting_with_tax(String costing_with_tax) {
        this.costing_with_tax = costing_with_tax;
    }

    public String getMin_margin() {
        return min_margin;
    }

    public void setMin_margin(String min_margin) {
        this.min_margin = min_margin;
    }

    public String getManufacturing_date() {
        return manufacturing_date;
    }

    public void setManufacturing_date(String manufacturing_date) {
        this.manufacturing_date = manufacturing_date;
    }

    public String getReference_type() {
        return reference_type;
    }

    public void setReference_type(String reference_type) {
        this.reference_type = reference_type;
    }

    public String getReference_id() {
        return reference_id;
    }

    public void setReference_id(String reference_id) {
        this.reference_id = reference_id;
    }

    public String getAdd_chg_amt() {
        return add_chg_amt;
    }

    public void setAdd_chg_amt(String add_chg_amt) {
        this.add_chg_amt = add_chg_amt;
    }

    public String getTransaction_status() {
        return transaction_status;
    }

    public void setTransaction_status(String transaction_status) {
        this.transaction_status = transaction_status;
    }

    public String getIs_expired() {
        return is_expired;
    }

    public void setIs_expired(String is_expired) {
        this.is_expired = is_expired;
    }

    public List<UnitRateList> getRateList() {
        return rateList;
    }

    public void setRateList(List<UnitRateList> rateList) {
        this.rateList = rateList;
    }

    public TranxSelectedProduct getSelectedProduct() {
        return selectedProduct;
    }

    public void setSelectedProduct(TranxSelectedProduct selectedProduct) {
        this.selectedProduct = selectedProduct;
    }

    public TranxSelectedBatch getSelectedBatch() {
        return selectedBatch;
    }

    public void setSelectedBatch(TranxSelectedBatch selectedBatch) {
        this.selectedBatch = selectedBatch;
    }
}
