package com.opethic.genivis.controller.Reports.Transactions;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.opethic.genivis.dto.LedgerReport3DTO;
import com.opethic.genivis.dto.Reports.PaymentReportScreen1DTO;
import com.opethic.genivis.dto.Reports.PaymentReportScreen2DTO;
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

import java.awt.*;
import java.net.URL;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PaymentReportScreen2Controller implements Initializable {
    private static final Logger paymentReport2Logger = LoggerFactory.getLogger(PaymentReportScreen2Controller.class);
    @FXML
    private TextField tfPaymentReport2ListSearch,tfPaymentReport2FromDate,tfPaymentReport2ToDate;
    @FXML
    private TableView<PaymentReportScreen2DTO> tblvPaymentReport2List;
    @FXML
    private TableColumn<PaymentReportScreen2DTO, String> tblcPaymentReport2Particulars,tblcPaymentReport2NoOfVouchers,tblcPaymentReport2PaymentAmt;
    private ObservableList<PaymentReportScreen2DTO> originalData;
    @FXML
    private Label lblTotalNoOfVouchers,lblTotalPaymentAmt;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        DateValidator.applyDateFormat(tfPaymentReport2FromDate);
        DateValidator.applyDateFormat(tfPaymentReport2ToDate);

        responsiveTable();
        getMontheWisePaymentList();
        Platform.runLater(()->{
            tfPaymentReport2ListSearch.requestFocus();
        });
        nodetraversal(tfPaymentReport2ListSearch,tfPaymentReport2FromDate);
        nodetraversal(tfPaymentReport2FromDate,tfPaymentReport2ToDate);
        tfPaymentReport2ToDate.setOnKeyPressed(event -> {
            if(event.getCode() == KeyCode.ENTER || event.getCode() == KeyCode.TAB){
                getMontheWisePaymentListWithDate();
            }
        });
    }
    //function for cursor focus
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
        tblcPaymentReport2Particulars.prefWidthProperty().bind(tblvPaymentReport2List.widthProperty().multiply(0.6));
        tblcPaymentReport2NoOfVouchers.prefWidthProperty().bind(tblvPaymentReport2List.widthProperty().multiply(0.2));
        tblcPaymentReport2PaymentAmt.prefWidthProperty().bind(tblvPaymentReport2List.widthProperty().multiply(0.2));
    }
    private void getMontheWisePaymentList(){
       paymentReport2Logger.info("starting of getting the payment list by month wise");
       try{
           Map<String, String> body = new HashMap<>();
           body.put("", "");     //initially we don't send the dates
           String requestBody = Globals.mapToStringforFormData(body);
           HttpResponse<String> response = APIClient.postFormDataRequest(requestBody, EndPoints.GET_PAYMENT_REPORT2_DETAILS);
           JsonObject responseBody = new Gson().fromJson(response.body(), JsonObject.class);
           ObservableList<PaymentReportScreen2DTO> observableList = FXCollections.observableArrayList();
           if (responseBody.get("responseStatus").getAsInt() == 200) {
               JsonArray responseList = responseBody.getAsJsonArray("response");
//                System.out.println("fetchLedgerReport3List" + responseList);
               if (responseList.size() > 0) {
//                    System.out.println("List" + responseList);
//                   tblvLedgerReport3List.setVisible(true);
                   for (JsonElement element : responseList) {
                       JsonObject item = element.getAsJsonObject();
                       String month = item.get("month") != null ? item.get("month").getAsString() : "";
                       String noOfVouchers = item.get("no_vouchers") != null ? item.get("no_vouchers").getAsString() : "";
                       String endDate  = item.get("end_date") != null ? item.get("end_date").getAsString() : "";
                       String totalAmt = item.get("total_amt") != null ? item.get("total_amt").getAsString() : "";
                       String startDate = item.get("start_date") != null ? item.get("start_date").getAsString() : "";

                       observableList.add(new PaymentReportScreen2DTO(month, noOfVouchers, endDate, totalAmt, startDate));
                   }

                   tblcPaymentReport2Particulars.setCellValueFactory(new PropertyValueFactory<>("month"));
                   tblcPaymentReport2NoOfVouchers.setCellValueFactory(new PropertyValueFactory<>("no_vouchers"));
                   tblcPaymentReport2PaymentAmt.setCellValueFactory(new PropertyValueFactory<>("total_amt"));

                   tblvPaymentReport2List.setItems(observableList);
                   originalData=observableList;
//                   calculateTotalAmount();

                   paymentReport2Logger.debug("Success in Displaying Ledger Report 3 List : AccountsLedgerReport3Controller");
               } else {
                   paymentReport2Logger.debug("Ledger Report 3 List ResponseObject is null : AccountsLedgerReport3Controller");
               }
           } else {
               paymentReport2Logger.debug("Error in response of Ledger Report 3 List : AccountsLedgerReport3Controller");
           }
       }catch (Exception e){
           paymentReport2Logger.error("error in getting the month wise payment list");
       }
    }
    //function when enters the date
    private void getMontheWisePaymentListWithDate(){
        paymentReport2Logger.info("starting of getting the payment list by month wise");
        try{
            Map<String, String> body = new HashMap<>();
            System.out.println("startDate "+Communicator.text_to_date.fromString(tfPaymentReport2FromDate.getText()).toString());
            System.out.println("endDate "+Communicator.text_to_date.fromString(tfPaymentReport2ToDate.getText()).toString());
            body.put("start_date", Communicator.text_to_date.fromString(tfPaymentReport2FromDate.getText()).toString());
            body.put("end_date", Communicator.text_to_date.fromString(tfPaymentReport2ToDate.getText()).toString());
            String requestBody = Globals.mapToStringforFormData(body);
            HttpResponse<String> response = APIClient.postFormDataRequest(requestBody, EndPoints.GET_PAYMENT_REPORT2_DETAILS);
            JsonObject responseBody = new Gson().fromJson(response.body(), JsonObject.class);
            ObservableList<PaymentReportScreen2DTO> observableList = FXCollections.observableArrayList();
            if (responseBody.get("responseStatus").getAsInt() == 200) {
                JsonArray responseList = responseBody.getAsJsonArray("response");
//                System.out.println("fetchLedgerReport3List" + responseList);
                if (responseList.size() > 0) {
                    for (JsonElement element : responseList) {
                        JsonObject item = element.getAsJsonObject();
                        String month = item.get("month") != null ? item.get("month").getAsString() : "";
                        String noOfVouchers = item.get("no_vouchers") != null ? item.get("no_vouchers").getAsString() : "";
                        String endDate  = item.get("end_date") != null ? item.get("end_date").getAsString() : "";
                        String totalAmt = item.get("total_amt") != null ? item.get("total_amt").getAsString() : "";
                        String startDate = item.get("start_date") != null ? item.get("start_date").getAsString() : "";

                        observableList.add(new PaymentReportScreen2DTO(month, noOfVouchers, endDate, totalAmt, startDate));
                    }

                    tblcPaymentReport2Particulars.setCellValueFactory(new PropertyValueFactory<>("month"));
                    tblcPaymentReport2NoOfVouchers.setCellValueFactory(new PropertyValueFactory<>("no_vouchers"));
                    tblcPaymentReport2PaymentAmt.setCellValueFactory(new PropertyValueFactory<>("total_amt"));

                    tblvPaymentReport2List.setItems(observableList);
                    originalData=observableList;
                    calculateTotalAmount();
                    paymentReport2Logger.debug("Success in Displaying Ledger Report 3 List : AccountsLedgerReport3Controller");
                } else {
                    paymentReport2Logger.debug("Ledger Report 3 List ResponseObject is null : AccountsLedgerReport3Controller");
                }
            } else {
                paymentReport2Logger.debug("Error in response of Ledger Report 3 List : AccountsLedgerReport3Controller");
            }
        }catch (Exception e){
            paymentReport2Logger.error("error in getting the month wise payment list");
        }
    }
    //function for bottom calculation of total no of vouchers and total payment amount
    private void calculateTotalAmount(){
        ObservableList<PaymentReportScreen2DTO> observableList = tblvPaymentReport2List.getItems();
        double totalPaymentAmount = 0.0;
        double totalVouchers = 0.0;

        for(PaymentReportScreen2DTO item:observableList){
            totalPaymentAmount += Double.parseDouble(item.getNo_vouchers().isEmpty() ? "0.0" : item.getNo_vouchers());
            totalVouchers += Double.parseDouble(item.getTotal_amt().isEmpty() ? "0.0" : item.getTotal_amt());
        }
        lblTotalNoOfVouchers.setText(String.format("%.2f",totalVouchers));
        lblTotalPaymentAmt.setText(String.format("%.2f",totalPaymentAmount));

    }

}
