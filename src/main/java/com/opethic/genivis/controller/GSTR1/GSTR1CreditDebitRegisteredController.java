package com.opethic.genivis.controller.GSTR1;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.opethic.genivis.dto.GSTR1.GSTR1CreditDebitRegisteredDTO;
import com.opethic.genivis.dto.LedgerReport1DTO;
import com.opethic.genivis.network.APIClient;
import com.opethic.genivis.network.EndPoints;
import com.opethic.genivis.utils.DateConvertUtil;
import com.opethic.genivis.utils.Globals;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class GSTR1CreditDebitRegisteredController implements Initializable {

    private static final Logger GSTR1CreditDebitRegisteredListLogger = LoggerFactory.getLogger(GSTR1CreditDebitRegisteredController.class);
    @FXML
    private TableView<GSTR1CreditDebitRegisteredDTO> tblvGSTR1CreditDebitRegisteredList;

    @FXML
    private TextField tfAccountsLedgerReport1ListSearch;

    @FXML
    private DatePicker tfStartDt,tfEndDt;

    private ObservableList<GSTR1CreditDebitRegisteredDTO> originalData;

    @FXML
    private TableColumn<GSTR1CreditDebitRegisteredDTO, String> tblGSTR1CreditDebitRegisteredTable_no, tblGSTR1CreditDebitRegisteredDates,tblGSTR1CreditDebitRegisteredInvoiceNo, tblGSTR1CreditDebitRegisteredParticulars,tblGSTR1CreditDebitRegisteredGSTIN_UIN,tblGSTR1CreditDebitRegisteredVoucherType,tblGSTR1CreditDebitRegisteredTaxableAmt,tblGSTR1CreditDebitRegisteredIGSTAmt,tblGSTR1CreditDebitRegisteredCGSTAmt,tblGSTR1CreditDebitRegisteredSGSTAmt,tblGSTR1CreditDebitRegisteredCessAmt,tblGSTR1CreditDebitRegisteredTaxAmt,tblGSTR1CreditDebitRegisteredInvAmt;

    public void initialize(URL url, ResourceBundle resourceBundle) {

        tblvGSTR1CreditDebitRegisteredList.setRowFactory(tv -> {
            TableRow<GSTR1CreditDebitRegisteredDTO> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (!row.isEmpty() && event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2) {
                    GSTR1CreditDebitRegisteredDTO clickedRow = row.getItem();

                }
            });
            return row;
        });

        fetchGSTR1CreditDebitRegisteredList("");
    }

    private void fetchGSTR1CreditDebitRegisteredList(String s) {

        GSTR1CreditDebitRegisteredListLogger.info("fetch GSTR1 Credit Debit Registered List : GSTR1CreditDebitRegisteredController");
        try {
            Map<String, String> body = new HashMap<>();
            body.put("searchText", s);
            body.put("sort", "{ \\\"colId\\\": null, \\\"isAsc\\\": true }");
            body.put("start_date","");
            body.put("end_date","");

            String requestBody = Globals.mapToString(body);

            HttpResponse<String> response = APIClient.postFormDataRequest(requestBody,EndPoints.GSTR1_CREDIT_DEBIT_REGISTERED_9B);
            JsonObject responseBody = new Gson().fromJson(response.body(), JsonObject.class);
            System.out.println("LedgerReport1_list" + responseBody);
            ObservableList<GSTR1CreditDebitRegisteredDTO> observableList = FXCollections.observableArrayList();
            if (responseBody.get("responseStatus").getAsInt() == 200) {
                JsonArray responseList = responseBody.getAsJsonArray("data");
                System.out.println("responseList" + responseList);

                tfStartDt.setValue(!responseBody.get("start_date").getAsString().isEmpty() ? DateConvertUtil.convertStringToLocalDate(responseBody.get("start_date").getAsString()) : null);

                tfEndDt.setValue(!responseBody.get("end_date").getAsString().isEmpty() ? DateConvertUtil.convertStringToLocalDate(responseBody.get("end_date").getAsString()) : null);

                if (responseList.size() > 0) {
//                    System.out.println("LedgerReport1_list" + responseBody);
                    tblvGSTR1CreditDebitRegisteredList.setVisible(true);
                    for (JsonElement element : responseList) {
                        JsonObject item = element.getAsJsonObject();
                        String tbl_no = "1";
                        String id = item.get("id").getAsString();
                        String transaction_date = item.get("transaction_date").getAsString();
                        String invoice_no = item.get("invoice_no").getAsString();
                        String ledger_name = item.get("ledger_name").getAsString();
                        String gstin_uin = item.get("gst_number").getAsString();
                        String type = item.get("voucher_type") != null ? item.get("voucher_type").getAsString() : "";
                        String taxable_amt = item.get("taxable_amt").getAsString();
                        String total_igst = item.get("total_igst").getAsString();
                        String total_cgst = item.get("total_cgst").getAsString();
                        String total_sgst = item.get("total_sgst").getAsString();
                        String cess_amt = "";
                        String total_tax = item.get("total_tax").getAsString();
                        String total_amt = item.get("total_amt").getAsString();

                        observableList.add(new GSTR1CreditDebitRegisteredDTO(tbl_no,id, transaction_date, invoice_no,ledger_name, gstin_uin,type, taxable_amt, total_igst, total_cgst, total_sgst,cess_amt,total_tax,total_amt));
                    }

                    tblGSTR1CreditDebitRegisteredTable_no.setCellValueFactory(new PropertyValueFactory<>("table_no"));
                    tblGSTR1CreditDebitRegisteredDates.setCellValueFactory(new PropertyValueFactory<>("table_no"));
                    tblGSTR1CreditDebitRegisteredInvoiceNo.setCellValueFactory(new PropertyValueFactory<>("table_no"));
                    tblGSTR1CreditDebitRegisteredParticulars.setCellValueFactory(new PropertyValueFactory<>("particulars"));
                    tblGSTR1CreditDebitRegisteredGSTIN_UIN.setCellValueFactory(new PropertyValueFactory<>("taxable_amt"));
                    tblGSTR1CreditDebitRegisteredVoucherType.setCellValueFactory(new PropertyValueFactory<>("igst_amt"));
                    tblGSTR1CreditDebitRegisteredTaxableAmt.setCellValueFactory(new PropertyValueFactory<>("igst_amt"));
                    tblGSTR1CreditDebitRegisteredIGSTAmt.setCellValueFactory(new PropertyValueFactory<>("igst_amt"));
                    tblGSTR1CreditDebitRegisteredCGSTAmt.setCellValueFactory(new PropertyValueFactory<>("cgst_amt"));
                    tblGSTR1CreditDebitRegisteredSGSTAmt.setCellValueFactory(new PropertyValueFactory<>("sgst_amt"));
                    tblGSTR1CreditDebitRegisteredCessAmt.setCellValueFactory(new PropertyValueFactory<>("cess_amt"));
                    tblGSTR1CreditDebitRegisteredTaxAmt.setCellValueFactory(new PropertyValueFactory<>("tax_amt"));
                    tblGSTR1CreditDebitRegisteredInvAmt.setCellValueFactory(new PropertyValueFactory<>("tax_amt"));


                    tblvGSTR1CreditDebitRegisteredList.setItems(observableList);
                    originalData = observableList;
//                    filterLedgerListByFilter();
                    GSTR1CreditDebitRegisteredListLogger.debug("Success in Displaying GSTR1 Credit Debit Registered List : GSTR1CreditDebitRegisteredController");
                } else {
                    tblvGSTR1CreditDebitRegisteredList.getItems().clear();
//                    calculateTotalAmount();
                    GSTR1CreditDebitRegisteredListLogger.debug("GSTR1 Credit Debit Registered List ResponseObject is null : GSTR1CreditDebitRegisteredController");
                }
            } else {
                tblvGSTR1CreditDebitRegisteredList.getItems().clear();
                GSTR1CreditDebitRegisteredListLogger.debug("Error in response of GSTR1 Credit Debit Registered List : GSTR1CreditDebitRegisteredController");
            }
        } catch (Exception e) {
            GSTR1CreditDebitRegisteredListLogger.error("GSTR1 Credit Debit Registered Error is " + e.getMessage());
            e.printStackTrace();
        }


    }

}
