package com.opethic.genivis.dto.reqres.sales_tranx;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.opethic.genivis.dto.cmp_t_row.BatchWindowTableDTO;
import com.opethic.genivis.models.tranx.sales.TranxSelectedProduct;
import javafx.beans.property.SimpleStringProperty;

import java.util.List;

public class SalesOrderTable {
    private SimpleStringProperty id = new SimpleStringProperty("0");
    private String details_id;
    private SimpleStringProperty product_id = new SimpleStringProperty("0");
    private SimpleStringProperty current_index;

    private SimpleStringProperty sr_no;
    private SimpleStringProperty particulars;

    private SimpleStringProperty packages;
    private SimpleStringProperty levelA;
    private SimpleStringProperty levelB;
    private SimpleStringProperty levelC;
    private SimpleStringProperty unit;

    private SimpleStringProperty levelA_id= new SimpleStringProperty("");
    private SimpleStringProperty levelB_id= new SimpleStringProperty("");
    private SimpleStringProperty levelC_id= new SimpleStringProperty("");
    private SimpleStringProperty unit_id= new SimpleStringProperty("");

    private SimpleStringProperty batch_or_serial;
    private SimpleStringProperty quantity;

    private SimpleStringProperty free =new SimpleStringProperty("0"); ;
    private SimpleStringProperty rate;
    private SimpleStringProperty gross_amount;
    private SimpleStringProperty dis_per;
    private SimpleStringProperty dis_amt;
    private SimpleStringProperty tax = new SimpleStringProperty("");
    private SimpleStringProperty net_amount;
    private SimpleStringProperty action;

    private SimpleStringProperty is_batch =  new SimpleStringProperty("");

    private SimpleStringProperty is_serial =  new SimpleStringProperty("false");
    private List<BatchWindowTableDTO> batchWindowTableDTOList;

    private SimpleStringProperty ledger_id = new SimpleStringProperty("0");

    private SimpleStringProperty dis_per2 = new SimpleStringProperty("");

    private SimpleStringProperty org_net_amt= new SimpleStringProperty("");

    private SimpleStringProperty taxable_amt = new SimpleStringProperty("0.0");

    private SimpleStringProperty total_taxable_amt = new SimpleStringProperty("");

    private SimpleStringProperty igst = new SimpleStringProperty("");

    private SimpleStringProperty sgst= new SimpleStringProperty("");

    private SimpleStringProperty cgst= new SimpleStringProperty("");

    private SimpleStringProperty final_tax_amt= new SimpleStringProperty("");

    private SimpleStringProperty final_dis_amt= new SimpleStringProperty("");

    private SimpleStringProperty base_amt= new SimpleStringProperty("");

    private SimpleStringProperty dis_per_cal= new SimpleStringProperty("");

    private SimpleStringProperty dis_amt_cal= new SimpleStringProperty("");

    private SimpleStringProperty row_dis_amt= new SimpleStringProperty("");

    private SimpleStringProperty gross_amount1= new SimpleStringProperty("");

    private SimpleStringProperty gst= new SimpleStringProperty("");

    private SimpleStringProperty igst_per= new SimpleStringProperty("");

    private SimpleStringProperty sgst_per= new SimpleStringProperty("");

    private SimpleStringProperty cgst_per= new SimpleStringProperty("");

    private SimpleStringProperty final_amount= new SimpleStringProperty("");

    private SimpleStringProperty total_igst= new SimpleStringProperty("");

    private SimpleStringProperty total_sgst= new SimpleStringProperty("");

    private SimpleStringProperty total_cgst= new SimpleStringProperty("");

    private SimpleStringProperty inventoryId= new SimpleStringProperty("0");

    private SimpleStringProperty unit_conv = new SimpleStringProperty("0.0");

    private SimpleStringProperty invoice_dis_amt = new SimpleStringProperty("0.0");

    private SimpleStringProperty total_amt = new SimpleStringProperty("0.0");

    private SimpleStringProperty b_details_id = new SimpleStringProperty("");

    private SimpleStringProperty b_no = new SimpleStringProperty("");

    private SimpleStringProperty b_rate = new SimpleStringProperty("");

    private SimpleStringProperty b_purchase_rate = new SimpleStringProperty("");

    private SimpleStringProperty b_expiry = new SimpleStringProperty("");

    private SimpleStringProperty rate_a = new SimpleStringProperty("0.0");

    private SimpleStringProperty rate_b = new SimpleStringProperty("0.0");

    private SimpleStringProperty rate_c = new SimpleStringProperty("0.0");

    private SimpleStringProperty rate_d = new SimpleStringProperty("0.0");

    private SimpleStringProperty costing = new SimpleStringProperty("0.0");

    private SimpleStringProperty costing_with_tax = new SimpleStringProperty("0.0");

    private SimpleStringProperty min_margin = new SimpleStringProperty("0.0");

    private SimpleStringProperty manufacturing_date = new SimpleStringProperty("");

    private SimpleStringProperty reference_type = new SimpleStringProperty("");

    private SimpleStringProperty reference_id = new SimpleStringProperty("");

    private SimpleStringProperty add_chg_amt = new SimpleStringProperty("0.0");
    //new
    private TranxSelectedProduct selectedProduct;




    public SalesOrderTable(String id, String current_index, String sr_no, String particulars,String packages, String levelA, String levelB, String levelC, String unit, String batch_or_serial, String quantity, String rate, String gross_amount, String dis_per, String dis_amt, String tax, String net_amount, String action) {
        this.id = new SimpleStringProperty(id);
        this.current_index = new SimpleStringProperty(current_index);
        this.sr_no = new SimpleStringProperty(sr_no);
        this.particulars = new SimpleStringProperty(particulars);
        this.packages = new SimpleStringProperty(packages);
        this.levelA = new SimpleStringProperty(levelA);
        this.levelB = new SimpleStringProperty(levelB);
        this.levelC = new SimpleStringProperty(levelC);
        this.unit = new SimpleStringProperty(unit);
        this.batch_or_serial = new SimpleStringProperty(batch_or_serial);
        this.quantity = new SimpleStringProperty(quantity);
        this.rate = new SimpleStringProperty(rate);
        this.gross_amount = new SimpleStringProperty(gross_amount);
        this.dis_per = new SimpleStringProperty(dis_per);
        this.dis_amt = new SimpleStringProperty(dis_amt);
        this.tax = new SimpleStringProperty(tax);
        this.net_amount = new SimpleStringProperty(net_amount);
        this.action = new SimpleStringProperty(action);
    }

//    public SalesOrderTable() {
//
//    }

    public String getDetails_id() {
        return details_id;
    }

    public void setDetails_id(String details_id) {
        this.details_id = details_id;
    }
    public String getAdd_chg_amt() {
        return add_chg_amt.get();
    }

    public SimpleStringProperty add_chg_amtProperty() {
        return add_chg_amt;
    }

    public void setAdd_chg_amt(String add_chg_amt) {
        this.add_chg_amt.set(add_chg_amt);
    }

    public String getB_details_id() {
        return b_details_id.get();
    }

    public SimpleStringProperty b_details_idProperty() {
        return b_details_id;
    }

    public void setB_details_id(String b_details_id) {
        this.b_details_id.set(b_details_id);
    }

    public String getB_no() {
        return b_no.get();
    }

    public SimpleStringProperty b_noProperty() {
        return b_no;
    }

    public void setB_no(String b_no) {
        this.b_no.set(b_no);
    }

    public String getB_rate() {
        return b_rate.get();
    }

    public SimpleStringProperty b_rateProperty() {
        return b_rate;
    }

    public void setB_rate(String b_rate) {
        this.b_rate.set(b_rate);
    }

    public String getB_purchase_rate() {
        return b_purchase_rate.get();
    }

    public SimpleStringProperty b_purchase_rateProperty() {
        return b_purchase_rate;
    }

    public void setB_purchase_rate(String b_purchase_rate) {
        this.b_purchase_rate.set(b_purchase_rate);
    }

    public String getB_expiry() {
        return b_expiry.get();
    }

    public SimpleStringProperty b_expiryProperty() {
        return b_expiry;
    }

    public void setB_expiry(String b_expiry) {
        this.b_expiry.set(b_expiry);
    }

    public String getRate_a() {
        return rate_a.get();
    }

    public SimpleStringProperty rate_aProperty() {
        return rate_a;
    }

    public void setRate_a(String rate_a) {
        this.rate_a.set(rate_a);
    }

    public String getRate_b() {
        return rate_b.get();
    }

    public SimpleStringProperty rate_bProperty() {
        return rate_b;
    }

    public void setRate_b(String rate_b) {
        this.rate_b.set(rate_b);
    }

    public String getRate_c() {
        return rate_c.get();
    }

    public SimpleStringProperty rate_cProperty() {
        return rate_c;
    }

    public void setRate_c(String rate_c) {
        this.rate_c.set(rate_c);
    }

    public String getRate_d() {
        return rate_d.get();
    }

    public SimpleStringProperty rate_dProperty() {
        return rate_d;
    }

    public void setRate_d(String rate_d) {
        this.rate_d.set(rate_d);
    }

    public String getCosting() {
        return costing.get();
    }

    public SimpleStringProperty costingProperty() {
        return costing;
    }

    public void setCosting(String costing) {
        this.costing.set(costing);
    }

    public String getCosting_with_tax() {
        return costing_with_tax.get();
    }

    public SimpleStringProperty costing_with_taxProperty() {
        return costing_with_tax;
    }

    public void setCosting_with_tax(String costing_with_tax) {
        this.costing_with_tax.set(costing_with_tax);
    }

    public String getMin_margin() {
        return min_margin.get();
    }

    public SimpleStringProperty min_marginProperty() {
        return min_margin;
    }

    public void setMin_margin(String min_margin) {
        this.min_margin.set(min_margin);
    }

    public String getManufacturing_date() {
        return manufacturing_date.get();
    }

    public SimpleStringProperty manufacturing_dateProperty() {
        return manufacturing_date;
    }

    public void setManufacturing_date(String manufacturing_date) {
        this.manufacturing_date.set(manufacturing_date);
    }

    public String getReference_type() {
        return reference_type.get();
    }

    public SimpleStringProperty reference_typeProperty() {
        return reference_type;
    }

    public void setReference_type(String reference_type) {
        this.reference_type.set(reference_type);
    }

    public String getReference_id() {
        return reference_id.get();
    }

    public SimpleStringProperty reference_idProperty() {
        return reference_id;
    }

    public void setReference_id(String reference_id) {
        this.reference_id.set(reference_id);
    }

    public String getTotal_amt() {
        return total_amt.get();
    }

    public SimpleStringProperty total_amtProperty() {
        return total_amt;
    }

    public void setTotal_amt(String total_amt) {
        this.total_amt.set(total_amt);
    }

    public String getInventoryId() {
        return inventoryId.get();
    }

    public SimpleStringProperty inventoryIdProperty() {
        return inventoryId;
    }

    public void setInventoryId(String inventoryId) {
        this.inventoryId.set(inventoryId);
    }

    public String getInvoice_dis_amt() {
        return invoice_dis_amt.get();
    }

    public SimpleStringProperty invoice_dis_amtProperty() {
        return invoice_dis_amt;
    }

    public void setInvoice_dis_amt(String invoice_dis_amt) {
        this.invoice_dis_amt.set(invoice_dis_amt);
    }

    public String getUnit_conv() {
        return unit_conv.get();
    }

    public SimpleStringProperty unit_convProperty() {
        return unit_conv;
    }

    public void setUnit_conv(String unit_conv) {
        this.unit_conv.set(unit_conv);
    }

    public String getFree() {
        return free.get();
    }

    public SimpleStringProperty freeProperty() {
        return free;
    }

    public void setFree(String free) {
        this.free.set(free);
    }

    public String getTotal_igst() {
        return total_igst.get();
    }

    public SimpleStringProperty total_igstProperty() {
        return total_igst;
    }

    public void setTotal_igst(String total_igst) {
        this.total_igst.set(total_igst);
    }

    public String getTotal_sgst() {
        return total_sgst.get();
    }

    public SimpleStringProperty total_sgstProperty() {
        return total_sgst;
    }

    public void setTotal_sgst(String total_sgst) {
        this.total_sgst.set(total_sgst);
    }

    public String getTotal_cgst() {
        return total_cgst.get();
    }

    public SimpleStringProperty total_cgstProperty() {
        return total_cgst;
    }

    public void setTotal_cgst(String total_cgst) {
        this.total_cgst.set(total_cgst);
    }

    public String getTaxable_amt() {
        return taxable_amt.get();
    }

    public SimpleStringProperty taxable_amtProperty() {
        return taxable_amt;
    }

    public void setTaxable_amt(String taxable_amt) {
        this.taxable_amt.set(taxable_amt);
    }

    public String getTotal_taxable_amt() {
        return total_taxable_amt.get();
    }

    public SimpleStringProperty total_taxable_amtProperty() {
        return total_taxable_amt;
    }

    public void setTotal_taxable_amt(String total_taxable_amt) {
        this.total_taxable_amt.set(total_taxable_amt);
    }

    public String getIgst() {
        return igst.get();
    }

    public SimpleStringProperty igstProperty() {
        return igst;
    }

    public void setIgst(String igst) {
        this.igst.set(igst);
    }

    public String getSgst() {
        return sgst.get();
    }

    public SimpleStringProperty sgstProperty() {
        return sgst;
    }

    public void setSgst(String sgst) {
        this.sgst.set(sgst);
    }

    public String getCgst() {
        return cgst.get();
    }

    public SimpleStringProperty cgstProperty() {
        return cgst;
    }

    public void setCgst(String cgst) {
        this.cgst.set(cgst);
    }

    public String getFinal_tax_amt() {
        return final_tax_amt.get();
    }

    public SimpleStringProperty final_tax_amtProperty() {
        return final_tax_amt;
    }

    public void setFinal_tax_amt(String final_tax_amt) {
        this.final_tax_amt.set(final_tax_amt);
    }

    public String getFinal_dis_amt() {
        return final_dis_amt.get();
    }

    public SimpleStringProperty final_dis_amtProperty() {
        return final_dis_amt;
    }

    public void setFinal_dis_amt(String final_dis_amt) {
        this.final_dis_amt.set(final_dis_amt);
    }

    public String getBase_amt() {
        return base_amt.get();
    }

    public SimpleStringProperty base_amtProperty() {
        return base_amt;
    }

    public void setBase_amt(String base_amt) {
        this.base_amt.set(base_amt);
    }

    public String getDis_per_cal() {
        return dis_per_cal.get();
    }

    public SimpleStringProperty dis_per_calProperty() {
        return dis_per_cal;
    }

    public void setDis_per_cal(String dis_per_cal) {
        this.dis_per_cal.set(dis_per_cal);
    }

    public String getDis_amt_cal() {
        return dis_amt_cal.get();
    }

    public SimpleStringProperty dis_amt_calProperty() {
        return dis_amt_cal;
    }

    public void setDis_amt_cal(String dis_amt_cal) {
        this.dis_amt_cal.set(dis_amt_cal);
    }

    public String getRow_dis_amt() {
        return row_dis_amt.get();
    }

    public SimpleStringProperty row_dis_amtProperty() {
        return row_dis_amt;
    }

    public void setRow_dis_amt(String row_dis_amt) {
        this.row_dis_amt.set(row_dis_amt);
    }

    public String getGross_amount1() {
        return gross_amount1.get();
    }

    public SimpleStringProperty gross_amount1Property() {
        return gross_amount1;
    }

    public void setGross_amount1(String gross_amount1) {
        this.gross_amount1.set(gross_amount1);
    }

    public String getGst() {
        return gst.get();
    }

    public SimpleStringProperty gstProperty() {
        return gst;
    }

    public void setGst(String gst) {
        this.gst.set(gst);
    }

    public String getIgst_per() {
        return igst_per.get();
    }

    public SimpleStringProperty igst_perProperty() {
        return igst_per;
    }

    public void setIgst_per(String igst_per) {
        this.igst_per.set(igst_per);
    }

    public String getSgst_per() {
        return sgst_per.get();
    }

    public SimpleStringProperty sgst_perProperty() {
        return sgst_per;
    }

    public void setSgst_per(String sgst_per) {
        this.sgst_per.set(sgst_per);
    }

    public String getCgst_per() {
        return cgst_per.get();
    }

    public SimpleStringProperty cgst_perProperty() {
        return cgst_per;
    }

    public void setCgst_per(String cgst_per) {
        this.cgst_per.set(cgst_per);
    }

    public String getFinal_amount() {
        return final_amount.get();
    }

    public SimpleStringProperty final_amountProperty() {
        return final_amount;
    }

    public void setFinal_amount(String final_amount) {
        this.final_amount.set(final_amount);
    }

    public String getOrg_net_amt() {
        return org_net_amt.get();
    }

    public SimpleStringProperty org_net_amtProperty() {
        return org_net_amt;
    }

    public void setOrg_net_amt(String org_net_amt) {
        this.org_net_amt.set(org_net_amt);
    }

    public String getDis_per2() {
        return dis_per2.get();
    }

    public SimpleStringProperty dis_per2Property() {
        return dis_per2;
    }

    public void setDis_per2(String dis_per2) {
        this.dis_per2.set(dis_per2);
    }

    public String getIs_serial() {
        return is_serial.get();
    }

    public SimpleStringProperty is_serialProperty() {
        return is_serial;
    }

    public void setIs_serial(String is_serial) {
        this.is_serial.set(is_serial);
    }

    public String getLevelA_id() {
        return levelA_id.get();
    }

    public SimpleStringProperty levelA_idProperty() {
        return levelA_id;
    }

    public void setLevelA_id(String levelA_id) {
        this.levelA_id.set(levelA_id);
    }

    public String getLevelB_id() {
        return levelB_id.get();
    }

    public SimpleStringProperty levelB_idProperty() {
        return levelB_id;
    }

    public void setLevelB_id(String levelB_id) {
        this.levelB_id.set(levelB_id);
    }

    public String getLevelC_id() {
        return levelC_id.get();
    }

    public SimpleStringProperty levelC_idProperty() {
        return levelC_id;
    }

    public void setLevelC_id(String levelC_id) {
        this.levelC_id.set(levelC_id);
    }

    public String getUnit_id() {
        return unit_id.get();
    }

    public SimpleStringProperty unit_idProperty() {
        return unit_id;
    }

    public void setUnit_id(String unit_id) {
        this.unit_id.set(unit_id);
    }

    public String getLedger_id() {
        return ledger_id.get();
    }

    public SimpleStringProperty ledger_idProperty() {
        return ledger_id;
    }

    public void setLedger_id(String ledger_id) {
        this.ledger_id.set(ledger_id);
    }

    public String getIs_batch() {
        return is_batch.get();
    }

    public SimpleStringProperty is_batchProperty() {
        return is_batch;
    }

    public void setIs_batch(String is_batch) {
        this.is_batch.set(is_batch);
    }


    public List<BatchWindowTableDTO> getBatchWindowTableDTOList() {
        return batchWindowTableDTOList;
    }

    public void setBatchWindowTableDTOList(List<BatchWindowTableDTO> batchWindowTableDTOList) {
        this.batchWindowTableDTOList = batchWindowTableDTOList;
    }

    public String getProduct_id() {
        return product_id.get();
    }

    public SimpleStringProperty product_idProperty() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id.set(product_id);
    }

    public String getPackages() {
        return packages.get();
    }

    public SimpleStringProperty packagesProperty() {
        return packages;
    }

    public void setPackages(String packages) {
        this.packages.set(packages);
    }

    public String getRate() {
        return rate.get();
    }

    public SimpleStringProperty rateProperty() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate.set(rate);
    }

    public String getUnit() {
        return unit.get();
    }

    public SimpleStringProperty unitProperty() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit.set(unit);
    }

    public String getSr_no() {
        return sr_no.get();
    }

    public SimpleStringProperty sr_noProperty() {
        return sr_no;
    }

    public void setSr_no(String sr_no) {
        this.sr_no.set(sr_no);
    }

    public String getId() {
        return id.get();
    }

    public SimpleStringProperty idProperty() {
        return id;
    }

    public void setId(String id) {
        this.id.set(id);
    }

    public String getCurrent_index() {
        return current_index.get();
    }

    public SimpleStringProperty current_indexProperty() {
        return current_index;
    }

    public void setCurrent_index(String current_index) {
        this.current_index.set(current_index);
    }

    public String getParticulars() {
        return particulars.get();
    }

    public SimpleStringProperty particularsProperty() {
        return particulars;
    }

    public void setParticulars(String particulars) {
        this.particulars.set(particulars);
    }

    public String getLevelA() {
        return levelA.get();
    }

    public SimpleStringProperty levelAProperty() {
        return levelA;
    }

    public void setLevelA(String levelA) {
        this.levelA.set(levelA);
    }

    public String getLevelB() {
        return levelB.get();
    }

    public SimpleStringProperty levelBProperty() {
        return levelB;
    }

    public void setLevelB(String levelB) {
        this.levelB.set(levelB);
    }

    public String getLevelC() {
        return levelC.get();
    }

    public SimpleStringProperty levelCProperty() {
        return levelC;
    }

    public void setLevelC(String levelC) {
        this.levelC.set(levelC);
    }

    public String getBatch_or_serial() {
        return batch_or_serial.get();
    }

    public SimpleStringProperty batch_or_serialProperty() {
        return batch_or_serial;
    }

    public void setBatch_or_serial(String batch_or_serial) {
        this.batch_or_serial.set(batch_or_serial);
    }

    public String getQuantity() {
        return quantity.get();
    }

    public SimpleStringProperty quantityProperty() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity.set(quantity);
    }

    public String getGross_amount() {
        return gross_amount.get();
    }

    public SimpleStringProperty gross_amountProperty() {
        return gross_amount;
    }

    public void setGross_amount(String gross_amount) {
        this.gross_amount.set(gross_amount);
    }

    public String getDis_per() {
        return dis_per.get();
    }

    public SimpleStringProperty dis_perProperty() {
        return dis_per;
    }

    public void setDis_per(String dis_per) {
        this.dis_per.set(dis_per);
    }

    public String getDis_amt() {
        return dis_amt.get();
    }

    public SimpleStringProperty dis_amtProperty() {
        return dis_amt;
    }

    public void setDis_amt(String dis_amt) {
        this.dis_amt.set(dis_amt);
    }

    public String getTax() {
        return tax.get();
    }

    public SimpleStringProperty taxProperty() {
        return tax;
    }

    public void setTax(String tax) {
        this.tax.set(tax);
    }

    public String getNet_amount() {
        return net_amount.get();
    }

    public SimpleStringProperty net_amountProperty() {
        return net_amount;
    }

    public void setNet_amount(String net_amount) {
        this.net_amount.set(net_amount);
    }

    public String getAction() {
        return action.get();
    }

    public SimpleStringProperty actionProperty() {
        return action;
    }

    public void setAction(String action) {
        this.action.set(action);
    }
    //new start
    public TranxSelectedProduct getSelectedProduct() {
        return selectedProduct;
    }

    public void setSelectedProduct(TranxSelectedProduct selectedProduct) {
        this.selectedProduct = selectedProduct;
    }
    //new end

    @Override
    public String toString(){

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("current_index", current_index.getValue());
        jsonObject.addProperty("productId", product_id.getValue());
        jsonObject.addProperty("inventoryId",inventoryId.getValue());
        jsonObject.addProperty("levelaId", levelA_id.getValue());
        jsonObject.addProperty("levelbId", levelB_id.getValue());
        jsonObject.addProperty("levelcId", levelC_id.getValue());
        jsonObject.addProperty("unitId", unit_id.getValue());
        jsonObject.addProperty("qcty", quantity.getValue());
        jsonObject.addProperty("free_qty", free.getValue());
        jsonObject.addProperty("unit_conv", unit_conv.getValue());
        jsonObject.addProperty("rate", rate.getValue());
        jsonObject.addProperty("dis_amt", dis_amt.getValue());
        jsonObject.addProperty("dis_per", dis_per.getValue());
        jsonObject.addProperty("dis_per2", dis_per2.getValue());
        jsonObject.addProperty("row_dis_amt", row_dis_amt.getValue());
        jsonObject.addProperty("gross_amt", gross_amount1.getValue());
        jsonObject.addProperty("add_chg_amt", add_chg_amt.getValue());
        jsonObject.addProperty("gross_amt1", gross_amount1.getValue());
        jsonObject.addProperty("invoice_dis_amt", invoice_dis_amt.getValue());
        jsonObject.addProperty("dis_per_cal", dis_per_cal.getValue());
        jsonObject.addProperty("dis_amt_cal", dis_amt_cal.getValue());
        jsonObject.addProperty("total_amt", total_amt.getValue());
        jsonObject.addProperty("igst", igst.getValue());
        jsonObject.addProperty("sgst", sgst.getValue());
        jsonObject.addProperty("cgst", cgst.getValue());
        jsonObject.addProperty("total_igst", total_igst.getValue());
        jsonObject.addProperty("total_sgst", total_sgst.getValue());
        jsonObject.addProperty("total_cgst", total_cgst.getValue());
        jsonObject.addProperty("final_amt", net_amount.getValue());
        jsonObject.addProperty("is_batch", is_batch.getValue());
        jsonObject.addProperty("is_serial", is_serial.getValue());
        jsonObject.addProperty("b_details_id", b_details_id.getValue());
        jsonObject.addProperty("b_no", b_no.getValue());
        jsonObject.addProperty("b_rate", b_rate.getValue());
        jsonObject.addProperty("b_purchase_rate", b_purchase_rate.getValue());
        jsonObject.addProperty("b_expiry", b_expiry.getValue());
        jsonObject.addProperty("rate_a", rate_a.getValue());
        jsonObject.addProperty("rate_b", rate_b.getValue());
        jsonObject.addProperty("rate_c", rate_c.getValue());
        jsonObject.addProperty("rate_d", rate_d.getValue());
        jsonObject.addProperty("costing", costing.getValue());
        jsonObject.addProperty("costing_with_tax",costing_with_tax.getValue());
        jsonObject.addProperty("min_margin", min_margin.getValue());
        jsonObject.addProperty("manufacturing_date", manufacturing_date.getValue());
        jsonObject.addProperty("reference_type", reference_type.getValue());
        jsonObject.addProperty("reference_id", reference_id.getValue());

        Gson gson = new Gson();
        return gson.toJson(jsonObject);
    }
    //new start
    public double getUnitWiseRateMH() {
        double rtnrate = 0.0;

            if (Integer.parseInt(getUnit_id()) == selectedProduct.getUnit1Id() && selectedProduct.getUnit1ActualStock() > 0) {
                rtnrate = selectedProduct.getUnit1FSRMH();
            }
            if (Integer.parseInt(getUnit_id()) == selectedProduct.getUnit2Id() && selectedProduct.getUnit2ActualStock() > 0) {
                rtnrate = selectedProduct.getUnit2FSRMH();
            }
            if (Integer.parseInt(getUnit_id()) == selectedProduct.getUnit3Id() && selectedProduct.getUnit3ActualStock() > 0) {
                rtnrate = selectedProduct.getUnit3FSRMH();
        }


        return rtnrate;
    }

    public double getUnitWiseRateAI() {
        double rtnrate = 0.0;

            if (Integer.parseInt(getUnit_id()) == selectedProduct.getUnit1Id() && selectedProduct.getUnit1ActualStock() > 0) {
                rtnrate = selectedProduct.getUnit1FSRAI();
            }
            if (Integer.parseInt(getUnit_id()) == selectedProduct.getUnit2Id() && selectedProduct.getUnit2ActualStock() > 0) {
                rtnrate = selectedProduct.getUnit2FSRAI();
            }
            if (Integer.parseInt(getUnit_id()) == selectedProduct.getUnit3Id() && selectedProduct.getUnit3ActualStock() > 0) {
                rtnrate = selectedProduct.getUnit3FSRAI();
            }



        return rtnrate;
    }
    //new end
}
