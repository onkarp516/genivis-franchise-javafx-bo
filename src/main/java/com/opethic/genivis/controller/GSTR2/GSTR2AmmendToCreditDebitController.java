package com.opethic.genivis.controller.GSTR2;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.opethic.genivis.dto.GSTR2.GSTR2CrDrUnregisterDTO;
import com.opethic.genivis.dto.reqres.product.Communicator;
import com.opethic.genivis.network.APIClient;
import com.opethic.genivis.network.EndPoints;
import com.opethic.genivis.utils.DateValidator;
import com.opethic.genivis.utils.Globals;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class GSTR2AmmendToCreditDebitController implements Initializable {
    private static final Logger gstr2AmmendRegLogger =  LoggerFactory.getLogger(GSTR2AmmendToCreditDebitController.class);
    @FXML
    private TextField tfGSTR2AmmendCrDrRegLedgerSearch,tfGSTR2AmmendCrDrRegFromDate,tfGSTR2AmmendCrDrRegToDate;
    @FXML
    private TableView<GSTR2CrDrUnregisterDTO> tblvGSTR2AmmendCrDrRegList;
    @FXML
    private TableColumn<GSTR2CrDrUnregisterDTO, String> tblcGSTR2AmmendCrDrRegSrNo,tblcGSTR2AmmendCrDrRegDate,tblcGSTR2AmmendCrDrRegInvoiceNo,tblcGSTR2AmmendCrDrRegParticulars,
            tblcGSTR2AmmendCrDrRegVoucherType, tblcGSTR2AmmendCrDrRegTaxableAmt,tblcGSTR2AmmendCrDrRegIntegratedTaxAmt,tblcGSTR2AmmendCrDrRegCentralTaxAmt,
            tblcGSTR2AmmendCrDrRegStateTaxAmt,tblcGSTR2AmmendCrDrRegCessAmt, tblcGSTR2AmmendCrDrRegTaxAmt,tblcGSTR2AmmendCrDrRegInvoiceAmt,tblcGSTR2AmmendCrDrRegGstNo;
    private ObservableList<GSTR2CrDrUnregisterDTO> originalData;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        responsiveTable();
        getAmmendCreditDebitList();
        DateValidator.applyDateFormat(tfGSTR2AmmendCrDrRegFromDate);
        DateValidator.applyDateFormat(tfGSTR2AmmendCrDrRegToDate);
        tfGSTR2AmmendCrDrRegToDate.setOnKeyPressed(event -> {
            if(event.getCode() == KeyCode.ENTER || event.getCode() == KeyCode.TAB){
                getAmmendCreditDebotListWithDate();
            }
        });
        tfGSTR2AmmendCrDrRegLedgerSearch.textProperty().addListener((observable, oldValue, newValue) -> {
            filterData(newValue.trim());
        });
    }
    //function for responsive of the table
    private void responsiveTable(){
        tblcGSTR2AmmendCrDrRegSrNo.prefWidthProperty().bind(tblvGSTR2AmmendCrDrRegList.widthProperty().multiply(0.04));
        tblcGSTR2AmmendCrDrRegDate.prefWidthProperty().bind(tblvGSTR2AmmendCrDrRegList.widthProperty().multiply(0.06));
        tblcGSTR2AmmendCrDrRegInvoiceNo.prefWidthProperty().bind(tblvGSTR2AmmendCrDrRegList.widthProperty().multiply(0.09));
        tblcGSTR2AmmendCrDrRegParticulars.prefWidthProperty().bind(tblvGSTR2AmmendCrDrRegList.widthProperty().multiply(0.14));
        tblcGSTR2AmmendCrDrRegGstNo.prefWidthProperty().bind(tblvGSTR2AmmendCrDrRegList.widthProperty().multiply(0.08));
        tblcGSTR2AmmendCrDrRegVoucherType.prefWidthProperty().bind(tblvGSTR2AmmendCrDrRegList.widthProperty().multiply(0.1));
        tblcGSTR2AmmendCrDrRegTaxableAmt.prefWidthProperty().bind(tblvGSTR2AmmendCrDrRegList.widthProperty().multiply(0.08));
        tblcGSTR2AmmendCrDrRegIntegratedTaxAmt.prefWidthProperty().bind(tblvGSTR2AmmendCrDrRegList.widthProperty().multiply(0.07));
        tblcGSTR2AmmendCrDrRegCentralTaxAmt.prefWidthProperty().bind(tblvGSTR2AmmendCrDrRegList.widthProperty().multiply(0.06));
        tblcGSTR2AmmendCrDrRegStateTaxAmt.prefWidthProperty().bind(tblvGSTR2AmmendCrDrRegList.widthProperty().multiply(0.07));
        tblcGSTR2AmmendCrDrRegCessAmt.prefWidthProperty().bind(tblvGSTR2AmmendCrDrRegList.widthProperty().multiply(0.05));
        tblcGSTR2AmmendCrDrRegTaxAmt.prefWidthProperty().bind(tblvGSTR2AmmendCrDrRegList.widthProperty().multiply(0.05));
        tblcGSTR2AmmendCrDrRegInvoiceAmt.prefWidthProperty().bind(tblvGSTR2AmmendCrDrRegList.widthProperty().multiply(0.1));
    }
    private void getAmmendCreditDebitList(){
        gstr2AmmendRegLogger.info("starting of getting the gstr2 ammend credit/debit note registered list");
        try{
            Map<String,String> map = new HashMap<>();
            Map<String, Object> sortObject = new HashMap<>();
            sortObject.put("colId", null);
            sortObject.put("isAsc", true);
            map.put("searchText", "");
            map.put("sort", sortObject.toString());
            String requestBody = Globals.mapToStringforFormData(map);
            HttpResponse<String> response = APIClient.postFormDataRequest(requestBody, EndPoints.GSTR2_AMMEND_CREDIT_DEBIT_UNREGISTER);
            JsonObject jsonObject = new Gson().fromJson(response.body(), JsonObject.class);
            System.out.println("jsonObjecttt in getAmmendCreditDebitList "+jsonObject);
            ObservableList<GSTR2CrDrUnregisterDTO> observableList = FXCollections.observableArrayList();
            if(jsonObject.get("responseStatus").getAsInt() == 200){
                JsonArray jsonArray = jsonObject.get("data").getAsJsonArray();
                String startDate = jsonObject.get("start_date").getAsString();
                String endDate = jsonObject.get("end_date").getAsString();
                if(jsonArray.size() >0){
                    for(JsonElement element:jsonArray){
                        JsonObject jsonItem = element.getAsJsonObject();
                        System.out.println("jsonItemmm "+jsonItem);

                        String gstNo = jsonItem.get("gst_number").getAsString();
                        String tranxId = jsonItem.get("id").getAsString();
                        String invoiceNo = jsonItem.get("invoice_no").getAsString();
                        String ledgerId = jsonItem.get("ledger_id").getAsString();
                        String ledgerName = jsonItem.get("ledger_name").getAsString();
                        String taxableAmt = jsonItem.get("taxable_amt").getAsString();
                        String totalAmt = jsonItem.get("total_amt").getAsString();
                        String totalCGST = jsonItem.get("total_cgst").getAsString();
                        String totalIGST= jsonItem.get("total_igst").getAsString();
                        String totalSGST = jsonItem.get("total_sgst").getAsString();
                        String totalTax = jsonItem.get("total_tax").getAsString();
                        String tranxDate = jsonItem.get("transaction_date").getAsString();
                        String voucherType = jsonItem.get("voucher_type").getAsString();

                        observableList.add(new GSTR2CrDrUnregisterDTO(gstNo,tranxId,startDate,endDate,invoiceNo,ledgerId,ledgerName,taxableAmt,totalAmt,totalCGST,totalIGST,totalSGST,
                                totalTax,tranxDate,voucherType));
                    }
                    tblcGSTR2AmmendCrDrRegDate.setCellValueFactory(new PropertyValueFactory<>("transaction_date"));
                    tblcGSTR2AmmendCrDrRegInvoiceNo.setCellValueFactory(new PropertyValueFactory<>("invoice_no"));
                    tblcGSTR2AmmendCrDrRegParticulars.setCellValueFactory(new PropertyValueFactory<>("ledger_name"));
                    tblcGSTR2AmmendCrDrRegGstNo.setCellValueFactory(new PropertyValueFactory<>("gst_number"));
                    tblcGSTR2AmmendCrDrRegVoucherType.setCellValueFactory(new PropertyValueFactory<>("voucher_type"));
                    tblcGSTR2AmmendCrDrRegTaxableAmt.setCellValueFactory(new PropertyValueFactory<>("taxable_amt"));
                    tblcGSTR2AmmendCrDrRegIntegratedTaxAmt.setCellValueFactory(new PropertyValueFactory<>("total_igst"));
                    tblcGSTR2AmmendCrDrRegCentralTaxAmt.setCellValueFactory(new PropertyValueFactory<>("total_cgst"));
                    tblcGSTR2AmmendCrDrRegStateTaxAmt.setCellValueFactory(new PropertyValueFactory<>("total_sgst"));
                    tblcGSTR2AmmendCrDrRegCessAmt.setCellValueFactory(new PropertyValueFactory<>(""));
                    tblcGSTR2AmmendCrDrRegTaxAmt.setCellValueFactory(new PropertyValueFactory<>("total_tax"));
                    tblcGSTR2AmmendCrDrRegInvoiceAmt.setCellValueFactory(new PropertyValueFactory<>("total_amt"));
                    tblvGSTR2AmmendCrDrRegList.setItems(observableList);

                    originalData=observableList;
                }
            }
        }catch (Exception e){
            gstr2AmmendRegLogger.error("error occured in getAmmendCreditDebitList()");
        }
    }

    private void getAmmendCreditDebotListWithDate(){
        gstr2AmmendRegLogger.info("starting of getting the gstr2 credit/debit note unregistered list");
        try{
            Map<String,String> map = new HashMap<>();
            Map<String, Object> sortObject = new HashMap<>();
            sortObject.put("colId", null);
            sortObject.put("isAsc", true);
            map.put("searchText", "");
            map.put("start_date", Communicator.text_to_date.fromString(tfGSTR2AmmendCrDrRegFromDate.getText()).toString());
            map.put("end_date",Communicator.text_to_date.fromString(tfGSTR2AmmendCrDrRegToDate.getText()).toString());
            map.put("sort", sortObject.toString());
            String requestBody = Globals.mapToStringforFormData(map);
            HttpResponse<String> response = APIClient.postFormDataRequest(requestBody, EndPoints.GSTR2_AMMEND_CREDIT_DEBIT_UNREGISTER);
            JsonObject jsonObject = new Gson().fromJson(response.body(), JsonObject.class);
            System.out.println("jsonObjecttt "+jsonObject);
            ObservableList<GSTR2CrDrUnregisterDTO> observableList = FXCollections.observableArrayList();
            if(jsonObject.get("responseStatus").getAsInt() == 200){
                JsonArray jsonArray = jsonObject.get("data").getAsJsonArray();
                String startDate = jsonObject.get("start_date").getAsString();
                String endDate = jsonObject.get("end_date").getAsString();
                if(jsonArray.size() >0){
                    for(JsonElement element:jsonArray){
                        JsonObject jsonItem = element.getAsJsonObject();
                        System.out.println("jsonItemmm "+jsonItem);

                        String gstNo = jsonItem.get("gst_number").getAsString();
                        String tranxId = jsonItem.get("id").getAsString();
                        String invoiceNo = jsonItem.get("invoice_no").getAsString();
                        String ledgerId = jsonItem.get("ledger_id").getAsString();
                        String ledgerName = jsonItem.get("ledger_name").getAsString();
                        String taxableAmt = jsonItem.get("taxable_amt").getAsString();
                        String totalAmt = jsonItem.get("total_amt").getAsString();
                        String totalCGST = jsonItem.get("total_cgst").getAsString();
                        String totalIGST= jsonItem.get("total_igst").getAsString();
                        String totalSGST = jsonItem.get("total_sgst").getAsString();
                        String totalTax = jsonItem.get("total_tax").getAsString();
                        String tranxDate = jsonItem.get("transaction_date").getAsString();
                        String voucherType = jsonItem.get("voucher_type").getAsString();

                        observableList.add(new GSTR2CrDrUnregisterDTO(gstNo,tranxId,startDate,endDate,invoiceNo,ledgerId,ledgerName,taxableAmt,totalAmt,totalCGST,totalIGST,totalSGST,
                                totalTax,tranxDate,voucherType));
                    }
                    tblcGSTR2AmmendCrDrRegDate.setCellValueFactory(new PropertyValueFactory<>("transaction_date"));
                    tblcGSTR2AmmendCrDrRegInvoiceNo.setCellValueFactory(new PropertyValueFactory<>("invoice_no"));
                    tblcGSTR2AmmendCrDrRegParticulars.setCellValueFactory(new PropertyValueFactory<>("ledger_name"));
                    tblcGSTR2AmmendCrDrRegGstNo.setCellValueFactory(new PropertyValueFactory<>("gst_number"));
                    tblcGSTR2AmmendCrDrRegVoucherType.setCellValueFactory(new PropertyValueFactory<>("voucher_type"));
                    tblcGSTR2AmmendCrDrRegTaxableAmt.setCellValueFactory(new PropertyValueFactory<>("taxable_amt"));
                    tblcGSTR2AmmendCrDrRegIntegratedTaxAmt.setCellValueFactory(new PropertyValueFactory<>("total_igst"));
                    tblcGSTR2AmmendCrDrRegCentralTaxAmt.setCellValueFactory(new PropertyValueFactory<>("total_cgst"));
                    tblcGSTR2AmmendCrDrRegStateTaxAmt.setCellValueFactory(new PropertyValueFactory<>("total_sgst"));
                    tblcGSTR2AmmendCrDrRegCessAmt.setCellValueFactory(new PropertyValueFactory<>(""));
                    tblcGSTR2AmmendCrDrRegTaxAmt.setCellValueFactory(new PropertyValueFactory<>("total_tax"));
                    tblcGSTR2AmmendCrDrRegInvoiceAmt.setCellValueFactory(new PropertyValueFactory<>("total_amt"));
                    tblvGSTR2AmmendCrDrRegList.setItems(observableList);

                    originalData=observableList;
                }


            }


        }catch (Exception e){
            gstr2AmmendRegLogger.error("error occured in getCreditDebotList()");
        }
    }
    private void filterData(String keyword) {
        ObservableList<GSTR2CrDrUnregisterDTO> filteredData = FXCollections.observableArrayList();

        gstr2AmmendRegLogger.error("Search Accounts Ledger Report 2 in AccountsLedgerReport1Controller");
        if (keyword.isEmpty()) {
            tblvGSTR2AmmendCrDrRegList.setItems(originalData);
            return;
        }

        for (GSTR2CrDrUnregisterDTO item : originalData) {
            if (matchesKeyword(item, keyword)) {
                filteredData.add(item);
            }
        }

        tblvGSTR2AmmendCrDrRegList.setItems(filteredData);
    }
    private boolean matchesKeyword(GSTR2CrDrUnregisterDTO item, String keyword) {
        String lowerCaseKeyword = keyword.toLowerCase();

        return
                item.getLedger_name().toLowerCase().contains(lowerCaseKeyword) ||
                        item.getLedger_name().toLowerCase().contains(lowerCaseKeyword) ||
                        item.getInvoice_no().toLowerCase().contains(lowerCaseKeyword) ||
                        item.getGst_number().toLowerCase().contains(lowerCaseKeyword) ||
                        item.getVoucher_type().toLowerCase().contains(lowerCaseKeyword);

    }
}
