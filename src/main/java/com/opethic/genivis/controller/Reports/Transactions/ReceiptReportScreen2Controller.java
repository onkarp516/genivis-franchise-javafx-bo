package com.opethic.genivis.controller.Reports.Transactions;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.opethic.genivis.dto.Reports.PaymentReportScreen2DTO;
import com.opethic.genivis.dto.Reports.ReceiptReportScreen2DTO;
import com.opethic.genivis.dto.reqres.product.Communicator;
import com.opethic.genivis.network.APIClient;
import com.opethic.genivis.network.EndPoints;
import com.opethic.genivis.utils.DateValidator;
import com.opethic.genivis.utils.Globals;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
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

public class ReceiptReportScreen2Controller implements Initializable {
    private static final Logger receiptReport2Logger = LoggerFactory.getLogger(PaymentReportScreen2Controller.class);
    @FXML
    private TextField tfReceiptReport2FromDate,tfReceiptReport2ToDate,tfReceiptReport2ListSearch;
    @FXML
    private TableView<ReceiptReportScreen2DTO> tblvReceiptReport2List;
    @FXML
    private TableColumn<ReceiptReportScreen2DTO, String> tblcReceiptReport2Particulars,tblcReceiptReport2NoOfVouchers,tblcReceiptReport2PaymentAmt;
    private ObservableList<ReceiptReportScreen2DTO> originalData;
    @FXML
    private Label lblCompanyName;
    @FXML
    private Button btnPdf,btnExcel,btnCsv,btnPrint;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle){
        DateValidator.applyDateFormat(tfReceiptReport2FromDate);
        DateValidator.applyDateFormat(tfReceiptReport2ToDate);
        Platform.runLater(()->{
            tfReceiptReport2ListSearch.requestFocus();
        });

        nodetraversal(tfReceiptReport2ListSearch,tfReceiptReport2FromDate);
        nodetraversal(tfReceiptReport2FromDate,tfReceiptReport2ToDate);
        nodetraversal(tfReceiptReport2ToDate,btnPdf);
        getMontheWiseReceiptList();
        responsiveTable();
        tfReceiptReport2ToDate.setOnKeyPressed(event -> {
            if(event.getCode() == KeyCode.ENTER || event.getCode() == KeyCode.TAB){
                getMontheWiseReceiptListWithDate();
            }
        });
    }
    private void nodetraversal(Node current_node, Node next_node) {
        current_node.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                next_node.requestFocus();
                event.consume();
            }

            if (event.getCode() == KeyCode.SPACE) {
                if (current_node instanceof Button button) {
                    button.fire();
                }
            }
        });
    }
    private void responsiveTable(){
        tblcReceiptReport2Particulars.prefWidthProperty().bind(tblvReceiptReport2List.widthProperty().multiply(0.6));
        tblcReceiptReport2NoOfVouchers.prefWidthProperty().bind(tblvReceiptReport2List.widthProperty().multiply(0.2));
        tblcReceiptReport2PaymentAmt.prefWidthProperty().bind(tblvReceiptReport2List.widthProperty().multiply(0.2));
    }

    //function to get the recipt report screen 2 list month wise
    private void getMontheWiseReceiptList(){
        receiptReport2Logger.info("starting of getting the payment list by month wise");
        try{
            Map<String, String> body = new HashMap<>();
            body.put("", "");     //initially we don't send the dates
            String requestBody = Globals.mapToStringforFormData(body);
            HttpResponse<String> response = APIClient.postFormDataRequest(requestBody, EndPoints.GET_RECEIPT_REPORT2_DETAILS);
            JsonObject responseBody = new Gson().fromJson(response.body(), JsonObject.class);
            ObservableList<ReceiptReportScreen2DTO> observableList = FXCollections.observableArrayList();
            lblCompanyName.setText(responseBody.get("company_name").toString());
            if (responseBody.get("responseStatus").getAsInt() == 200) {
                JsonArray responseList = responseBody.getAsJsonArray("response");
                if (responseList.size() > 0) {
                    for (JsonElement element : responseList) {
                        JsonObject item = element.getAsJsonObject();
                        String month = item.get("month") != null ? item.get("month").getAsString() : "";
                        String noOfVouchers = item.get("no_vouchers") != null ? item.get("no_vouchers").getAsString() : "";
                        String endDate  = item.get("end_date") != null ? item.get("end_date").getAsString() : "";
                        String totalAmt = item.get("total_amt") != null ? item.get("total_amt").getAsString() : "";
                        String startDate = item.get("start_date") != null ? item.get("start_date").getAsString() : "";

                        observableList.add(new ReceiptReportScreen2DTO(month, noOfVouchers, endDate, totalAmt, startDate));
                    }

                    tblcReceiptReport2Particulars.setCellValueFactory(new PropertyValueFactory<>("month"));
                    tblcReceiptReport2NoOfVouchers.setCellValueFactory(new PropertyValueFactory<>("no_vouchers"));
                    tblcReceiptReport2PaymentAmt.setCellValueFactory(new PropertyValueFactory<>("total_amt"));

                    tblvReceiptReport2List.setItems(observableList);
                    originalData=observableList;
//                   calculateTotalAmount();
                    receiptReport2Logger.debug("Success in Displaying Ledger Report 3 List : AccountsLedgerReport3Controller");
                } else {
                    receiptReport2Logger.debug("Ledger Report 3 List ResponseObject is null : AccountsLedgerReport3Controller");
                }
            } else {
                receiptReport2Logger.debug("Error in response of Ledger Report 3 List : AccountsLedgerReport3Controller");
            }
        }catch (Exception e){
            receiptReport2Logger.error("error in getting the month wise payment list");
        }
    }

    //function to get the receipt report list by date
    private void getMontheWiseReceiptListWithDate(){
        receiptReport2Logger.info("starting of getting the payment list by month wise");
        try{
            Map<String, String> body = new HashMap<>();
            body.put("start_date", Communicator.text_to_date.fromString(tfReceiptReport2FromDate.getText()).toString());
            body.put("end_date",Communicator.text_to_date.fromString(tfReceiptReport2ToDate.getText()).toString());
            String requestBody = Globals.mapToStringforFormData(body);
            HttpResponse<String> response = APIClient.postFormDataRequest(requestBody, EndPoints.GET_RECEIPT_REPORT2_DETAILS);
            JsonObject responseBody = new Gson().fromJson(response.body(), JsonObject.class);
            lblCompanyName.setText(responseBody.get("company_name").toString());
//            tfReceiptReport2FromDate.
            ObservableList<ReceiptReportScreen2DTO> observableList = FXCollections.observableArrayList();
            if (responseBody.get("responseStatus").getAsInt() == 200) {
                JsonArray responseList = responseBody.getAsJsonArray("response");
                if (responseList.size() > 0) {
                    for (JsonElement element : responseList) {
                        JsonObject item = element.getAsJsonObject();
                        String month = item.get("month") != null ? item.get("month").getAsString() : "";
                        String noOfVouchers = item.get("no_vouchers") != null ? item.get("no_vouchers").getAsString() : "";
                        String endDate  = item.get("end_date") != null ? item.get("end_date").getAsString() : "";
                        String totalAmt = item.get("total_amt") != null ? item.get("total_amt").getAsString() : "";
                        String startDate = item.get("start_date") != null ? item.get("start_date").getAsString() : "";

                        observableList.add(new ReceiptReportScreen2DTO(month, noOfVouchers, endDate, totalAmt, startDate));
                    }

                    tblcReceiptReport2Particulars.setCellValueFactory(new PropertyValueFactory<>("month"));
                    tblcReceiptReport2NoOfVouchers.setCellValueFactory(new PropertyValueFactory<>("no_vouchers"));
                    tblcReceiptReport2PaymentAmt.setCellValueFactory(new PropertyValueFactory<>("total_amt"));

                    tblvReceiptReport2List.setItems(observableList);
                    originalData=observableList;
//                   calculateTotalAmount();
                    receiptReport2Logger.debug("Success in Displaying Ledger Report 3 List : AccountsLedgerReport3Controller");
                } else {
                    receiptReport2Logger.debug("Ledger Report 3 List ResponseObject is null : AccountsLedgerReport3Controller");
                }
            } else {
                receiptReport2Logger.debug("Error in response of Ledger Report 3 List : AccountsLedgerReport3Controller");
            }
        }catch (Exception e){
            receiptReport2Logger.error("error in getting the month wise payment list");
        }
    }
}
