package com.opethic.genivis.controller.GSTR2;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.opethic.genivis.dto.GSTR2.GSTR2CrDrUnregisterDTO;
import com.opethic.genivis.dto.LedgerReport2DTO;
import com.opethic.genivis.dto.reqres.product.Communicator;
import com.opethic.genivis.network.APIClient;
import com.opethic.genivis.network.EndPoints;
import com.opethic.genivis.utils.DateValidator;
import com.opethic.genivis.utils.Globals;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import java.awt.*;
import javafx.scene.control.*;
import java.net.URL;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



public class GSTR2CreditDebitUnregisterController implements Initializable {
    private static final Logger gstr2UnregLogger =  LoggerFactory.getLogger(GSTR2CreditDebitUnregisterController.class);
    @FXML
    private TextField tfGSTR2CrDrUnregLedgerSearch,tfGSTR2CrDrUnregFromDate,tfGSTR2CrDrUnregToDate;
    @FXML
    private TableView<GSTR2CrDrUnregisterDTO> tblvGSTR2CrDrUnregList;
    @FXML
    private TableColumn<GSTR2CrDrUnregisterDTO, String> tblcGSTR2CrDrUnregSrNo,tblcGSTR2CrDrUnregDate,tblcGSTR2CrDrUnregInvoiceNo,tblcGSTR2CrDrUnregParticulars,tblcGSTR2CrDrUnregVoucherType,
            tblcGSTR2CrDrUnregTaxableAmt,tblcGSTR2CrDrUnregIntegratedTaxAmt,tblcGSTR2CrDrUnregCentralTaxAmt,tblcGSTR2CrDrUnregStateTaxAmt,tblcGSTR2CrDrUnregCessAmt,
            tblcGSTR2CrDrUnregTaxAmt,tblcGSTR2CrDrUnregInvoiceAmt;
    private ObservableList<GSTR2CrDrUnregisterDTO> originalData;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        responsiveTable();
        getCreditDebotList();
        DateValidator.applyDateFormat(tfGSTR2CrDrUnregFromDate);
        DateValidator.applyDateFormat(tfGSTR2CrDrUnregToDate);
        tfGSTR2CrDrUnregToDate.setOnKeyPressed(event -> {
            if(event.getCode() == KeyCode.ENTER || event.getCode() == KeyCode.TAB){
                getCreditDebotListWithDate("");
            }
        });
        tfGSTR2CrDrUnregLedgerSearch.textProperty().addListener((observable, oldValue, newValue) -> {
            filterData(newValue.trim());
        });
    }
    private void responsiveTable(){
        tblcGSTR2CrDrUnregSrNo.prefWidthProperty().bind(tblvGSTR2CrDrUnregList.widthProperty().multiply(0.04));
        tblcGSTR2CrDrUnregDate.prefWidthProperty().bind(tblvGSTR2CrDrUnregList.widthProperty().multiply(0.06));
        tblcGSTR2CrDrUnregInvoiceNo.prefWidthProperty().bind(tblvGSTR2CrDrUnregList.widthProperty().multiply(0.1));
        tblcGSTR2CrDrUnregParticulars.prefWidthProperty().bind(tblvGSTR2CrDrUnregList.widthProperty().multiply(0.15));
        tblcGSTR2CrDrUnregVoucherType.prefWidthProperty().bind(tblvGSTR2CrDrUnregList.widthProperty().multiply(0.1));
        tblcGSTR2CrDrUnregTaxableAmt.prefWidthProperty().bind(tblvGSTR2CrDrUnregList.widthProperty().multiply(0.08));
        tblcGSTR2CrDrUnregIntegratedTaxAmt.prefWidthProperty().bind(tblvGSTR2CrDrUnregList.widthProperty().multiply(0.07));
        tblcGSTR2CrDrUnregCentralTaxAmt.prefWidthProperty().bind(tblvGSTR2CrDrUnregList.widthProperty().multiply(0.1));
        tblcGSTR2CrDrUnregStateTaxAmt.prefWidthProperty().bind(tblvGSTR2CrDrUnregList.widthProperty().multiply(0.1));
        tblcGSTR2CrDrUnregCessAmt.prefWidthProperty().bind(tblvGSTR2CrDrUnregList.widthProperty().multiply(0.05));
        tblcGSTR2CrDrUnregTaxAmt.prefWidthProperty().bind(tblvGSTR2CrDrUnregList.widthProperty().multiply(0.05));
        tblcGSTR2CrDrUnregInvoiceAmt.prefWidthProperty().bind(tblvGSTR2CrDrUnregList.widthProperty().multiply(0.1));
    }
    //function for get the gstr2 credit/debit note unregister list
    private void getCreditDebotList(){
        gstr2UnregLogger.info("starting of getting the gstr2 credit/debit note unregistered list");
        try{
            Map<String,String> map = new HashMap<>();
            Map<String, Object> sortObject = new HashMap<>();
            sortObject.put("colId", null);
            sortObject.put("isAsc", true);
            map.put("searchText", "");
            map.put("sort", sortObject.toString());
            String requestBody = Globals.mapToStringforFormData(map);
            HttpResponse<String> response = APIClient.postFormDataRequest(requestBody, EndPoints.GSTR2_CREDIT_DEBIT_UNREGISTER);
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

                        observableList.add(new GSTR2CrDrUnregisterDTO(tranxId,startDate,endDate,invoiceNo,ledgerId,ledgerName,taxableAmt,totalAmt,totalCGST,totalIGST,totalSGST,
                                totalTax,tranxDate,voucherType));
                    }
                    tblcGSTR2CrDrUnregDate.setCellValueFactory(new PropertyValueFactory<>("transaction_date"));
                    tblcGSTR2CrDrUnregInvoiceNo.setCellValueFactory(new PropertyValueFactory<>("invoice_no"));
                    tblcGSTR2CrDrUnregParticulars.setCellValueFactory(new PropertyValueFactory<>("ledger_name"));
                    tblcGSTR2CrDrUnregVoucherType.setCellValueFactory(new PropertyValueFactory<>("voucher_type"));
                    tblcGSTR2CrDrUnregTaxableAmt.setCellValueFactory(new PropertyValueFactory<>("taxable_amt"));
                    tblcGSTR2CrDrUnregIntegratedTaxAmt.setCellValueFactory(new PropertyValueFactory<>("total_igst"));
                    tblcGSTR2CrDrUnregCentralTaxAmt.setCellValueFactory(new PropertyValueFactory<>("total_cgst"));
                    tblcGSTR2CrDrUnregStateTaxAmt.setCellValueFactory(new PropertyValueFactory<>("total_sgst"));
                    tblcGSTR2CrDrUnregCessAmt.setCellValueFactory(new PropertyValueFactory<>(""));
                    tblcGSTR2CrDrUnregTaxAmt.setCellValueFactory(new PropertyValueFactory<>("total_tax"));
                    tblcGSTR2CrDrUnregInvoiceAmt.setCellValueFactory(new PropertyValueFactory<>("total_amt"));
                    tblvGSTR2CrDrUnregList.setItems(observableList);

                    originalData=observableList;
                }

            }


        }catch (Exception e){
            gstr2UnregLogger.error("error occured in getCreditDebotList()");
        }
    }
    //function for get the gstr2 credit/debit note unregister list with date
    private void getCreditDebotListWithDate(String search){
        gstr2UnregLogger.info("starting of getting the gstr2 credit/debit note unregistered list");
        try{
            Map<String,String> map = new HashMap<>();
            Map<String, Object> sortObject = new HashMap<>();
            sortObject.put("colId", null);
            sortObject.put("isAsc", true);
            System.out.println("searchhh "+search);
            map.put("searchText", search);
            map.put("start_date", Communicator.text_to_date.fromString(tfGSTR2CrDrUnregFromDate.getText()).toString());
            map.put("end_date",Communicator.text_to_date.fromString(tfGSTR2CrDrUnregToDate.getText()).toString());
            map.put("sort", sortObject.toString());
            String requestBody = Globals.mapToStringforFormData(map);
            HttpResponse<String> response = APIClient.postFormDataRequest(requestBody, EndPoints.GSTR2_CREDIT_DEBIT_UNREGISTER);
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

                        observableList.add(new GSTR2CrDrUnregisterDTO(tranxId,startDate,endDate,invoiceNo,ledgerId,ledgerName,taxableAmt,totalAmt,totalCGST,totalIGST,totalSGST,
                                totalTax,tranxDate,voucherType));
                    }
                    tblcGSTR2CrDrUnregDate.setCellValueFactory(new PropertyValueFactory<>("transaction_date"));
                    tblcGSTR2CrDrUnregInvoiceNo.setCellValueFactory(new PropertyValueFactory<>("invoice_no"));
                    tblcGSTR2CrDrUnregParticulars.setCellValueFactory(new PropertyValueFactory<>("ledger_name"));
                    tblcGSTR2CrDrUnregVoucherType.setCellValueFactory(new PropertyValueFactory<>("voucher_type"));
                    tblcGSTR2CrDrUnregTaxableAmt.setCellValueFactory(new PropertyValueFactory<>("taxable_amt"));
                    tblcGSTR2CrDrUnregIntegratedTaxAmt.setCellValueFactory(new PropertyValueFactory<>("total_igst"));
                    tblcGSTR2CrDrUnregCentralTaxAmt.setCellValueFactory(new PropertyValueFactory<>("total_cgst"));
                    tblcGSTR2CrDrUnregStateTaxAmt.setCellValueFactory(new PropertyValueFactory<>("total_sgst"));
                    tblcGSTR2CrDrUnregCessAmt.setCellValueFactory(new PropertyValueFactory<>(""));
                    tblcGSTR2CrDrUnregTaxAmt.setCellValueFactory(new PropertyValueFactory<>("total_tax"));
                    tblcGSTR2CrDrUnregInvoiceAmt.setCellValueFactory(new PropertyValueFactory<>("total_amt"));
                    tblvGSTR2CrDrUnregList.setItems(observableList);
                    originalData=observableList;
                }


            }


        }catch (Exception e){
            gstr2UnregLogger.error("error occured in getCreditDebotList()");
        }
    }
    private void filterData(String keyword) {
        ObservableList<GSTR2CrDrUnregisterDTO> filteredData = FXCollections.observableArrayList();

        gstr2UnregLogger.error("Search Accounts Ledger Report 2 in AccountsLedgerReport1Controller");
        if (keyword.isEmpty()) {
            tblvGSTR2CrDrUnregList.setItems(originalData);
            return;
        }

        for (GSTR2CrDrUnregisterDTO item : originalData) {
            if (matchesKeyword(item, keyword)) {
                filteredData.add(item);
            }
        }

        tblvGSTR2CrDrUnregList.setItems(filteredData);
    }
    private boolean matchesKeyword(GSTR2CrDrUnregisterDTO item, String keyword) {
        String lowerCaseKeyword = keyword.toLowerCase();

        return
                item.getLedger_name().toLowerCase().contains(lowerCaseKeyword) ||
                        item.getLedger_name().toLowerCase().contains(lowerCaseKeyword) ||
                        item.getInvoice_no().toLowerCase().contains(lowerCaseKeyword) ||
                        item.getVoucher_type().toLowerCase().contains(lowerCaseKeyword);

    }
}
