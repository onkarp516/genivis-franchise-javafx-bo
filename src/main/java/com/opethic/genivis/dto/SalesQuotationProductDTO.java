//Model SalesQuotationProductDTO
package com.opethic.genivis.dto;

import com.opethic.genivis.models.tranx.sales.TranxSelectedProduct;
import javafx.beans.property.SimpleStringProperty;


public class SalesQuotationProductDTO {
    private SimpleStringProperty sr_no;
    private String details_id;
    private SimpleStringProperty product_id;
    private SimpleStringProperty particulars =  new SimpleStringProperty("");
    private SimpleStringProperty packing = new SimpleStringProperty("");

    private SimpleStringProperty unit = new SimpleStringProperty("");//unitname
    private SimpleStringProperty unit_id = new SimpleStringProperty("");//unitId

    private SimpleStringProperty quantity =new SimpleStringProperty("");
    private SimpleStringProperty free =new SimpleStringProperty("0");

    private SimpleStringProperty batchNo = new SimpleStringProperty("");
    private SimpleStringProperty rate =new SimpleStringProperty("");
    private SimpleStringProperty gross_amt =new SimpleStringProperty("");
    private SimpleStringProperty dis_per = new SimpleStringProperty("");
    private SimpleStringProperty dis_amt =new SimpleStringProperty("");
    private SimpleStringProperty tax_per =new SimpleStringProperty("") ;
    private SimpleStringProperty net_amt=new SimpleStringProperty("");

    private SimpleStringProperty org_net_amt =new SimpleStringProperty("");


    // New Fields Added

    private SimpleStringProperty levelA  = new SimpleStringProperty("");
    private SimpleStringProperty levelB  = new SimpleStringProperty("");
    private SimpleStringProperty levelC = new SimpleStringProperty("");

    private SimpleStringProperty level_a_id =new SimpleStringProperty("");
    private SimpleStringProperty level_b_id =new SimpleStringProperty("");
    private SimpleStringProperty level_c_id =new SimpleStringProperty("");
    private SimpleStringProperty free_qty =new SimpleStringProperty("");
    private Double unit_count ;
    private Double unit_conv;

    private Double base_amt;

    private Double dis_per_cal;

    private Double dis_amt_cal;

    private Double row_dis_amt;

    private Double add_chg_amt;

    private Double gross_amt1;

    private Double invoice_dis_amt;

    private Double total_amt;

    private Double taxable_amt;
//    private String details_id;

    private Double total_base_amt;

    private Double gst;

    private Double igst;

    private Double cgst;

    private Double sgst;
    private Double igstPer;

    private Double cgstPer;


    private Double sgstPer;

    private Double total_igst;
    private Double totaligst;

    private Double total_cgst;

    private Double total_sgst;

    private Double final_amt;

    private Double final_dis_amt;

    private Double dis_proportional_cal;

    private Double additional_charges_proportional_cal;

    private Double total_taxable_amt;
    private Double org_taxable_amt;
    private Double finTaxAmt;

    private String b_no;
    private String b_rate;
    private String rate_a;
    private String rate_b;
    private String rate_c;
    private String rate_d;
    private String tax;
    private String current_index;
    private String costing;
    private String costing_with_tax;
    private String min_margin;
    private String manufacturing_date;
    private String b_purchase_rate;
    private String b_expiry;
    private String b_details_id;
    private String is_batch;
    private Boolean is_serial;
    private String serialNo;
    private String reference_id;
    private String reference_type;
    private String sales_rate = "0";
    private SimpleStringProperty disPer2 = new SimpleStringProperty("0");
    private SimpleStringProperty action =new SimpleStringProperty("");
    //new
    private TranxSelectedProduct selectedProduct;

    public SalesQuotationProductDTO() {
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

    public String getUnit_id() {
        return unit_id.get();
    }

    public SimpleStringProperty unit_idProperty() {
        return unit_id;
    }

    public void setUnit_id(String unit_id) {
        this.unit_id.set(unit_id);
    }

    public String getRate_d() {
        return rate_d;
    }

    public void setRate_d(String rate_d) {
        this.rate_d = rate_d;
    }

    public String getTax() {
        return tax;
    }

    public void setTax(String tax) {
        this.tax = tax;
    }

    public String getCurrent_index() {
        return current_index;
    }

    public void setCurrent_index(String current_index) {
        this.current_index = current_index;
    }

    public SalesQuotationProductDTO(String sr_no, String product_id, String particulars, String packing, String unit, String quantity, String batchNo,
                                    String rate, String gross_amt, String dis_per, String dis_amt, String tax_per, String net_amt, String org_net_amt, String free_qty,
                                    String levelA, String levelB, String levelC, String action) {
        this.sr_no = new SimpleStringProperty(sr_no);
        this.product_id = new SimpleStringProperty(product_id);
        this.particulars = new SimpleStringProperty(particulars);
        this.packing = new SimpleStringProperty(packing);
        this.unit = new SimpleStringProperty(unit);
        this.quantity = new SimpleStringProperty(quantity);
        this.batchNo = new SimpleStringProperty(batchNo);
        this.rate = new SimpleStringProperty(rate);
        this.gross_amt = new SimpleStringProperty(gross_amt);
        this.dis_per = new SimpleStringProperty(dis_per);
        this.dis_amt = new SimpleStringProperty(dis_amt);
        this.tax_per = new SimpleStringProperty(tax_per);
        this.net_amt = new SimpleStringProperty(net_amt);
        this.org_net_amt = new SimpleStringProperty(org_net_amt);
        this.free_qty = new SimpleStringProperty(free_qty);
        this.levelA = new SimpleStringProperty(levelA);
        this.levelB = new SimpleStringProperty(levelB);
        this.levelC = new SimpleStringProperty(levelC);
//        this.level_a_id = new SimpleStringProperty(level_a_id);
//        this.level_b_id = new SimpleStringProperty(level_b_id);
//        this.level_c_id = new SimpleStringProperty(level_c_id);
        this.action = new SimpleStringProperty(action);
    }


    public SalesQuotationProductDTO(String sr_no,  String product_id, String particulars, String packing, String unit, String unitName, String quantity, String batchNo,
                                    String rate, String gross_amt, String dis_per, String dis_amt, String tax_per, String net_amt, String org_net_amt, String free_qty,
                                    String level_a_id, String level_b_id, String level_c_id, String action) {
        this.sr_no = new SimpleStringProperty(sr_no);
        this.product_id = new SimpleStringProperty(product_id);
        this.particulars = new SimpleStringProperty(particulars);
        this.packing = new SimpleStringProperty(packing);
        this.unit = new SimpleStringProperty(unit);
        this.quantity = new SimpleStringProperty(quantity);
        this.batchNo = new SimpleStringProperty(batchNo);
        this.rate = new SimpleStringProperty(rate);
        this.gross_amt = new SimpleStringProperty(gross_amt);
        this.dis_per = new SimpleStringProperty(dis_per);
        this.dis_amt = new SimpleStringProperty(dis_amt);
        this.tax_per = new SimpleStringProperty(tax_per);
        this.net_amt = new SimpleStringProperty(net_amt);
        this.org_net_amt = new SimpleStringProperty(org_net_amt);
        this.free_qty = new SimpleStringProperty(free_qty);
        this.level_a_id = new SimpleStringProperty(level_a_id);
        this.level_b_id = new SimpleStringProperty(level_b_id);
        this.level_c_id = new SimpleStringProperty(level_c_id);
        this.action = new SimpleStringProperty(action);
    }
    public String getSr_no() {
        return sr_no.get();
    }

    public SimpleStringProperty sr_noProperty() {
        return sr_no;
    }

    public void setSr_no(String sr_no) {
        this.sr_no = new SimpleStringProperty(sr_no);
    }



    public String getProduct_id() {
        return product_id.get();
    }

    public SimpleStringProperty product_idProperty() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = new SimpleStringProperty(product_id);
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

    public String getPacking() {
        return packing.get();
    }

    public SimpleStringProperty packingProperty() {
        return packing;
    }

    public void setPacking(String packing) {
        this.packing.set(packing);
//        this.packing = new SimpleStringProperty(packing);
    }




    public String getQuantity() {
        return quantity.get();
    }

    public SimpleStringProperty quantityProperty() {
        return quantity;
    }

    public void setQuantity(String quantity)
    { this.quantity.set(quantity);
//        this.quantity = new SimpleStringProperty(quantity);
    }
    public String getFree() {
        return free.get();
    }

    public SimpleStringProperty freeProperty() {
        return free;
    }

    public String getBatchNo() {
        return batchNo.get();
    }

    public SimpleStringProperty batchNoProperty() {
        return batchNo;
    }

    public void setBatchNo(String batchNo) {
        this.batchNo.set(batchNo);
//        this.batchNo = new SimpleStringProperty(batchNo);
    }


    public String getRate() {
        return rate.get();
    }

    public SimpleStringProperty rateProperty() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate.set(rate);
//        this.rate = new SimpleStringProperty(rate);
    }

    public String getGross_amt() {
        return gross_amt.get();
    }

    public SimpleStringProperty gross_amtProperty() {
        return gross_amt;
    }

    public void setGross_amt(String gross_amt) {
        this.gross_amt.set(gross_amt);
//        this.gross_amt = new SimpleStringProperty(gross_amt);
    }

    public String getDis_per() {
        return dis_per.get();
    }

    public SimpleStringProperty dis_perProperty() {
        return dis_per;
    }

    public void setDis_per(String dis_per) {
        this.dis_per.set(dis_per);
//        this.dis_per = new SimpleStringProperty(dis_per);
    }

    public String getDis_amt() {
        return dis_amt.get();
    }

    public SimpleStringProperty dis_amtProperty() {
        return dis_amt;
    }

    public void setDis_amt(String dis_amt) {
        this.dis_amt.set(dis_amt);
//        this.dis_amt = new SimpleStringProperty(dis_amt);
    }

    public String getTax_per() {
        return tax_per.get();
    }

    public SimpleStringProperty tax_perProperty() {
        return tax_per;
    }

    public void setTax_per(String tax_per) {
        this.tax_per.set(tax_per);
//        this.tax_per = new SimpleStringProperty(tax_per);
    }

    public String getNet_amt() {
        return net_amt.get();
    }

    public SimpleStringProperty net_amtProperty() {
        return net_amt;
    }

    public void setNet_amt(String net_amt) {
        this.net_amt.set(net_amt);
//        this.net_amt = new SimpleStringProperty(net_amt);
    }


    public String getOrg_net_amt() {
        return org_net_amt.get();
    }

    public SimpleStringProperty org_net_amtProperty() {
        return org_net_amt;
    }

    public void setOrg_net_amt(String org_net_amt) {
        this.org_net_amt.set(org_net_amt);
//        this.org_net_amt = new SimpleStringProperty(org_net_amt);
    }

    public String getLevel_a_id() {
        return level_a_id != null ? level_a_id.get() : "";
    }

    public SimpleStringProperty level_a_idProperty() {
        return level_a_id;
    }

    public void setLevel_a_id(String level_a_id) {
        this.level_a_id.set(level_a_id);
//        this.level_a_id = new SimpleStringProperty(level_a_id);
    }

    public String getLevel_b_id() {
        return level_b_id != null ? level_b_id.get() : "";
    }

    public SimpleStringProperty level_b_idProperty() {
        return level_b_id;
    }

    public void setLevel_b_id(String level_b_id) {
        this.level_b_id.set(level_b_id);
//        this.level_b_id = new SimpleStringProperty(level_b_id);
    }

    public String getLevel_c_id() {
        return level_c_id != null ? level_c_id.get() : "";
    }

    public SimpleStringProperty level_c_idProperty() {
        return level_c_id;
    }

    public void setLevel_c_id(String level_c_id) {

        this.level_c_id.set(level_c_id);
//        this.level_c_id = new SimpleStringProperty(level_c_id);
    }

    public String getFree_qty() {
        return free_qty.get();
    }

    public SimpleStringProperty free_qtyProperty() {
        return free_qty;
    }

    public void setFree_qty(String free_qty) {
        this.free_qty.set(free_qty);
//        this.free_qty = new SimpleStringProperty(free_qty);
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


    public Double getUnit_count() {
        return unit_count;
    }

    public void setUnit_count(Double unit_count) {
        this.unit_count = unit_count;
    }

    public Double getUnit_conv() {
        return unit_conv;
    }

    public void setUnit_conv(Double unit_conv) {
        this.unit_conv = unit_conv;
    }

    public Double getBase_amt() {
        return base_amt;
    }

    public void setBase_amt(Double base_amt) {
        this.base_amt = base_amt;
    }

    public Double getDis_per_cal() {
        return dis_per_cal;
    }

    public void setDis_per_cal(Double dis_per_cal) {
        this.dis_per_cal = dis_per_cal;
    }

    public Double getDis_amt_cal() {
        return dis_amt_cal;
    }

    public void setDis_amt_cal(Double dis_amt_cal) {
        this.dis_amt_cal = dis_amt_cal;
    }

    public Double getRow_dis_amt() {
        return row_dis_amt;
    }

    public void setRow_dis_amt(Double row_dis_amt) {
        this.row_dis_amt = row_dis_amt;
    }

    public Double getAdd_chg_amt() {
        return add_chg_amt;
    }

    public void setAdd_chg_amt(Double add_chg_amt) {
        this.add_chg_amt = add_chg_amt;
    }

    public Double getGross_amt1() {
        return gross_amt1;
    }

    public void setGross_amt1(Double gross_amt1) {
        this.gross_amt1 = gross_amt1;
    }

    public Double getInvoice_dis_amt() {
        return invoice_dis_amt;
    }

    public void setInvoice_dis_amt(Double invoice_dis_amt) {
        this.invoice_dis_amt = invoice_dis_amt;
    }

    public Double getTotal_amt() {
        return total_amt;
    }

    public void setTotal_amt(Double total_amt) {
        this.total_amt = total_amt;
    }

    public Double getTaxable_amt() {
        return taxable_amt;
    }

    public void setTaxable_amt(Double taxable_amt) {
        this.taxable_amt = taxable_amt;
    }

//    public void setDetails_id(String details_id) {
//        this.details_id = details_id;
//    }
//    public String getDetails_id() {
//        return details_id;
//    }


    public Double getTotal_base_amt() {
        return total_base_amt;
    }

    public void setTotal_base_amt(Double total_base_amt) {
        this.total_base_amt = total_base_amt;
    }

    public Double getGst() {
        return gst;
    }

    public void setGst(Double gst) {
        this.gst = gst;
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

    public Double getTotal_igst() {
        return total_igst;
    }

    public void setTotal_igst(Double total_igst) {
        this.total_igst = total_igst;
    }
    public Double getTotaligst() {
        return totaligst;
    }

    public void setTotaligst(Double total_igst) {
        this.totaligst = total_igst;
    }

    public Double getTotal_cgst() {
        return total_cgst;
    }

    public void setTotal_cgst(Double total_cgst) {
        this.total_cgst = total_cgst;
    }

    public Double getTotal_sgst() {
        return total_sgst;
    }

    public void setTotal_sgst(Double total_sgst) {
        this.total_sgst = total_sgst;
    }

    public Double getFinal_amt() {
        return final_amt;
    }

    public void setFinal_amt(Double final_amt) {
        this.final_amt = final_amt;
    }

    public Double getFinal_dis_amt() {
        return final_dis_amt;
    }

    public void setFinal_dis_amt(Double final_dis_amt) {
        this.final_dis_amt = final_dis_amt;
    }

    public Double getDis_proportional_cal() {
        return dis_proportional_cal;
    }

    public void setDis_proportional_cal(Double dis_proportional_cal) {
        this.dis_proportional_cal = dis_proportional_cal;
    }

    public Double getAdditional_charges_proportional_cal() {
        return additional_charges_proportional_cal;
    }

    public void setAdditional_charges_proportional_cal(Double additional_charges_proportional_cal) {
        this.additional_charges_proportional_cal = additional_charges_proportional_cal;
    }

    public Double getTotal_taxable_amt() {
        return total_taxable_amt;
    }

    public void setTotal_taxable_amt(Double total_taxable_amt) {
        this.total_taxable_amt = total_taxable_amt;
    }

    public Double getOrg_taxable_amt() {
        return org_taxable_amt;
    }

    public void setOrg_taxable_amt(Double org_taxable_amt) {
        this.org_taxable_amt = org_taxable_amt;
    }

    public Double getFinTaxAmt() {
        return finTaxAmt;
    }

    public void setFinTaxAmt(Double finTaxAmt) {
        this.finTaxAmt = finTaxAmt;
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

    public String getB_details_id() {
        return b_details_id;
    }

    public void setB_details_id(String b_details_id) {
        this.b_details_id = b_details_id;
    }

    public String getIs_batch() {
        return is_batch;
    }

    public void setIs_batch(String is_batch) {
        this.is_batch = is_batch;
    }

    public String getSerialNo() {
        return serialNo;
    }

    public void setSerialNo(String serialNo) {
        this.serialNo = serialNo;
    }

    public String getReference_id() {
        return reference_id;
    }

    public void setReference_id(String reference_id) {
        this.reference_id = reference_id;
    }

    public String getReference_type() {
        return reference_type;
    }

    public void setReference_type(String reference_type) {
        this.reference_type = reference_type;
    }

    public String getDisPer2() {
        return disPer2.get();
    }

    public SimpleStringProperty disPer2Property() {
        return disPer2;
    }

    public void setDisPer2(String disPer2) {
        this.disPer2.set(disPer2);
    }

    public Double getIgstPer() {
        return igstPer;
    }

    public void setIgstPer(Double igstPer) {
        this.igstPer = igstPer;
    }

    public Double getCgstPer() {
        return cgstPer;
    }

    public void setCgstPer(Double cgstPer) {
        this.cgstPer = cgstPer;
    }

    public Double getSgstPer() {
        return sgstPer;
    }

    public void setSgstPer(Double sgstPer) {
        this.sgstPer = sgstPer;
    }

    public String getSales_rate() {
        return sales_rate;
    }

    public void setSales_rate(String sales_rate) {
        this.sales_rate = sales_rate;
    }

    public String getDetails_id() {
        return details_id;
    }

    public void setDetails_id(String details_id) {
        this.details_id = details_id;
    }
    public Boolean getIs_serial() {
        return is_serial;
    }

    public void setIs_serial(Boolean is_serial) {
        this.is_serial = is_serial;
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


    public TranxSelectedProduct getSelectedProduct() {
        return selectedProduct;
    }

    public void setSelectedProduct(TranxSelectedProduct selectedProduct) {
        this.selectedProduct = selectedProduct;
    }

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

    @Override
    public String toString() {
        return "SalesQuotationProductDTO{" +
                "sr_no=" + sr_no +
                ", details_id='" + details_id + '\'' +
                ", product_id=" + product_id +
                ", particulars=" + particulars +
                ", packing=" + packing +
                ", unit=" + unit +
                ", unit_id=" + unit_id +
                ", quantity=" + quantity +
                ", batchNo=" + batchNo +
                ", rate=" + rate +
                ", gross_amt=" + gross_amt +
                ", dis_per=" + dis_per +
                ", dis_amt=" + dis_amt +
                ", tax_per=" + tax_per +
                ", net_amt=" + net_amt +
                ", org_net_amt=" + org_net_amt +
                ", levelA=" + levelA +
                ", levelB=" + levelB +
                ", levelC=" + levelC +
                ", level_a_id=" + level_a_id +
                ", level_b_id=" + level_b_id +
                ", level_c_id=" + level_c_id +
                ", free_qty=" + free_qty +
                ", unit_count=" + unit_count +
                ", unit_conv=" + unit_conv +
                ", base_amt=" + base_amt +
                ", dis_per_cal=" + dis_per_cal +
                ", dis_amt_cal=" + dis_amt_cal +
                ", row_dis_amt=" + row_dis_amt +
                ", add_chg_amt=" + add_chg_amt +
                ", gross_amt1=" + gross_amt1 +
                ", invoice_dis_amt=" + invoice_dis_amt +
                ", total_amt=" + total_amt +
                ", taxable_amt=" + taxable_amt +
                ", total_base_amt=" + total_base_amt +
                ", gst=" + gst +
                ", igst=" + igst +
                ", cgst=" + cgst +
                ", sgst=" + sgst +
                ", igstPer=" + igstPer +
                ", cgstPer=" + cgstPer +
                ", sgstPer=" + sgstPer +
                ", total_igst=" + total_igst +
                ", totaligst=" + totaligst +
                ", total_cgst=" + total_cgst +
                ", total_sgst=" + total_sgst +
                ", final_amt=" + final_amt +
                ", final_dis_amt=" + final_dis_amt +
                ", dis_proportional_cal=" + dis_proportional_cal +
                ", additional_charges_proportional_cal=" + additional_charges_proportional_cal +
                ", total_taxable_amt=" + total_taxable_amt +
                ", org_taxable_amt=" + org_taxable_amt +
                ", finTaxAmt=" + finTaxAmt +
                ", b_no='" + b_no + '\'' +
                ", b_rate='" + b_rate + '\'' +
                ", rate_a='" + rate_a + '\'' +
                ", rate_b='" + rate_b + '\'' +
                ", rate_c='" + rate_c + '\'' +
                ", costing='" + costing + '\'' +
                ", costing_with_tax='" + costing_with_tax + '\'' +
                ", min_margin='" + min_margin + '\'' +
                ", manufacturing_date='" + manufacturing_date + '\'' +
                ", b_purchase_rate='" + b_purchase_rate + '\'' +
                ", b_expiry='" + b_expiry + '\'' +
                ", b_details_id='" + b_details_id + '\'' +
                ", is_batch='" + is_batch + '\'' +
                ", is_serial=" + is_serial +
                ", serialNo='" + serialNo + '\'' +
                ", reference_id='" + reference_id + '\'' +
                ", reference_type='" + reference_type + '\'' +
                ", sales_rate='" + sales_rate + '\'' +
                ", disPer2=" + disPer2 +
                ", action=" + action +
                ", selectedProduct=" + selectedProduct +
                '}';
    }
}



