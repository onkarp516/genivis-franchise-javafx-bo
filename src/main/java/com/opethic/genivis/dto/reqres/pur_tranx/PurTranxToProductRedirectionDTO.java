package com.opethic.genivis.dto.reqres.pur_tranx;

import com.opethic.genivis.dto.SalesQuotationProductDTO;
import com.opethic.genivis.dto.cmp_t_row.BatchWindowTableDTO;
import com.opethic.genivis.dto.pur_invoice.PurchaseInvoiceTable;
import com.opethic.genivis.dto.pur_invoice.PurchaseOrderTable;
import com.opethic.genivis.dto.reqres.payment.PaymentRowDTO;
import com.opethic.genivis.dto.reqres.sales_tranx.CounterSaleRowDTO;
import com.opethic.genivis.dto.reqres.sales_tranx.SalesInvoiceTable;
import com.opethic.genivis.dto.reqres.sales_tranx.SalesOrderTable;
import com.opethic.genivis.dto.tranx_sales.CmpTRowDTOSoToSc;

import java.util.ArrayList;
import java.util.List;

public class PurTranxToProductRedirectionDTO {

    String redirect;
    String tranxDate;
    String ledegrId;
    String gstNum;
    String purSerNum;
    String challanNo;
    String purAcc;
    String challanDate;
    String mobileNo;
    String patName;
    String patAdd;
    String docName;
    String docAdd;
    Boolean isConsSelected;

    String redirectProductId;
    Integer rediPrdCurrIndex;

    Boolean isLedgerRed;

    Boolean isProductRed;

    String tranxType;

    Integer purChallEditId;
    Boolean isRedirection;

    public Boolean getRedirection() {
        return isRedirection;
    }

    public void setRedirection(Boolean redirection) {
        isRedirection = redirection;
    }


    List<PurchaseInvoiceTable> rowData=new ArrayList<>();
    List<PurchaseOrderTable> rowPurchaseOrder=new ArrayList<>();
    List<SalesQuotationProductDTO> rowSaleQoq=new ArrayList<>();
    List<SalesInvoiceTable> rowSaleInvoice=new ArrayList<>();
    List<SalesOrderTable> rowSaleOrder=new ArrayList<>();
    List<CmpTRowDTOSoToSc> rowSaleChallan=new ArrayList<>();
    List<PaymentRowDTO> rowReceipt=new ArrayList<>();
    List<CounterSaleRowDTO> rowCounterSale=new ArrayList<>();

    public List<PaymentRowDTO> getRowReceipt() {
        return rowReceipt;
    }

    public void setRowReceipt(List<PaymentRowDTO> rowReceipt) {
        this.rowReceipt = rowReceipt;
    }

    public List<CmpTRowDTOSoToSc> getRowSaleChallan() {
        return rowSaleChallan;
    }

    public void setRowSaleChallan(List<CmpTRowDTOSoToSc> rowSaleChallan) {
        this.rowSaleChallan = rowSaleChallan;
    }

    public List<SalesOrderTable> getRowSaleOrder() {
        return rowSaleOrder;
    }

    public void setRowSaleOrder(List<SalesOrderTable> rowSaleOrder) {
        this.rowSaleOrder = rowSaleOrder;
    }

    public List<SalesInvoiceTable> getRowSaleInvoice() {
        return rowSaleInvoice;
    }

    public void setRowSaleInvoice(List<SalesInvoiceTable> rowSaleInvoice) {
        this.rowSaleInvoice = rowSaleInvoice;
    }

    public List<SalesQuotationProductDTO> getRowSaleQoq() {
        return rowSaleQoq;
    }

    public void setRowSaleQoq(List<SalesQuotationProductDTO> rowSaleQoq) {
        this.rowSaleQoq = rowSaleQoq;
    }

    public List<PurchaseOrderTable> getRowPurchaseOrder() {
        return rowPurchaseOrder;
    }

    public void setRowPurchaseOrder(List<PurchaseOrderTable> rowPurchaseOrder) {
        this.rowPurchaseOrder = rowPurchaseOrder;
    }

//    private List<PurTranxToPrdRediRowDTO> batchWindowTableDTOList;

    public String getRedirect() {
        return redirect;
    }

    public void setRedirect(String redirect) {
        this.redirect = redirect;
    }

    public String getTranxDate() {
        return tranxDate;
    }

    public void setTranxDate(String tranxDate) {
        this.tranxDate = tranxDate;
    }

    public String getLedegrId() {
        return ledegrId;
    }

    public void setLedegrId(String ledegrId) {
        this.ledegrId = ledegrId;
    }

    public String getGstNum() {
        return gstNum;
    }

    public void setGstNum(String gstNum) {
        this.gstNum = gstNum;
    }

    public String getPurSerNum() {
        return purSerNum;
    }

    public void setPurSerNum(String purSerNum) {
        this.purSerNum = purSerNum;
    }

    public String getChallanNo() {
        return challanNo;
    }

    public void setChallanNo(String challanNo) {
        this.challanNo = challanNo;
    }

    public String getPurAcc() {
        return purAcc;
    }

    public void setPurAcc(String purAcc) {
        this.purAcc = purAcc;
    }

    public String getChallanDate() {
        return challanDate;
    }

    public void setChallanDate(String challanDate) {
        this.challanDate = challanDate;
    }


    public List<PurchaseInvoiceTable> getRowData() {
        return rowData;
    }

    public void setRowData(List<PurchaseInvoiceTable> rowData) {
        this.rowData = rowData;
    }

    public String getRedirectProductId() {
        return redirectProductId;
    }

    public void setRedirectProductId(String redirectProductId) {
        this.redirectProductId = redirectProductId;
    }

    public Integer getRediPrdCurrIndex() {
        return rediPrdCurrIndex;
    }

    public void setRediPrdCurrIndex(Integer rediPrdCurrIndex) {
        this.rediPrdCurrIndex = rediPrdCurrIndex;
    }

    public Boolean getLedgerRed() {
        return isLedgerRed;
    }

    public void setLedgerRed(Boolean ledgerRed) {
        isLedgerRed = ledgerRed;
    }

    public Boolean getProductRed() {
        return isProductRed;
    }

    public void setProductRed(Boolean productRed) {
        isProductRed = productRed;
    }

    public String getTranxType() {
        return tranxType;
    }

    public void setTranxType(String tranxType) {
        this.tranxType = tranxType;
    }

    public Integer getPurChallEditId() {
        return purChallEditId;
    }

    public void setPurChallEditId(Integer purChallEditId) {
        this.purChallEditId = purChallEditId;
    }

    public List<CounterSaleRowDTO> getRowCounterSale() {
        return rowCounterSale;
    }

    public void setRowCounterSale(List<CounterSaleRowDTO> rowCounterSale) {
        this.rowCounterSale = rowCounterSale;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public String getPatName() {
        return patName;
    }

    public void setPatName(String patName) {
        this.patName = patName;
    }

    public String getPatAdd() {
        return patAdd;
    }

    public void setPatAdd(String patAdd) {
        this.patAdd = patAdd;
    }

    public String getDocName() {
        return docName;
    }

    public void setDocName(String docName) {
        this.docName = docName;
    }

    public String getDocAdd() {
        return docAdd;
    }

    public void setDocAdd(String docAdd) {
        this.docAdd = docAdd;
    }

    public Boolean getConsSelected() {
        return isConsSelected;
    }

    public void setConsSelected(Boolean consSelected) {
        isConsSelected = consSelected;
    }
}
