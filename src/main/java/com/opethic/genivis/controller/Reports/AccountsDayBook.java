package com.opethic.genivis.controller.Reports;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.opethic.genivis.dto.reqres.product.Communicator;
import com.opethic.genivis.network.APIClient;
import com.opethic.genivis.network.EndPoints;
import com.opethic.genivis.dto.AccountsDayBookDTO;
import com.opethic.genivis.utils.CommonTraversa;
import com.opethic.genivis.utils.DateValidator;
import com.opethic.genivis.utils.Globals;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;
import java.net.http.HttpResponse;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class AccountsDayBook implements Initializable {

    private static final Logger AccountsDayBookListLogger = LoggerFactory.getLogger(AccountsDayBook.class);

    @FXML
    private TableView<AccountsDayBookDTO> tblAccontsDayBookList;
    @FXML
    private TableColumn<AccountsDayBookDTO, String> tblAccontsDayBookDate, tblAccontsDayBookParticulars, tblAccontsDayBookVoucherTy, tblAccontsDayBookVoucherNo, tblAccontsDayBookDebitAmt, tblAccontsDayBookCreditAmt;
    @FXML
    private TextField tfAccountsDayBookListSearch;

    String vchr_type = "";
    String startDate = "";

    @FXML
    private TextField tfDayBookDate;
    @FXML
    private BorderPane mainBorderPane;
    @FXML
    private Button exportToExcelButton;
    private ObservableList<AccountsDayBookDTO> originalData;

    @FXML
    private Label lblTotalDebit;
    @FXML
    private Label lblTotalCredit;

    @FXML
    private Button btExportPdf, btExportExcel, btExportCsv, btExportPrint;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        Platform.runLater(() -> {
            tfAccountsDayBookListSearch.requestFocus();
        });

        tfAccountsDayBookListSearch.setOnKeyPressed(event->{
            if(event.getCode() == KeyCode.ENTER){
                Node nextNode = CommonTraversa.getNextFocusableNode(tfAccountsDayBookListSearch.getScene());
                if (nextNode != null) {
                    nextNode.requestFocus();
                }
            }
        });

        LocalDate dayDate = LocalDate.now();
        tfDayBookDate.setText(dayDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        DateValidator.applyDateFormat(tfDayBookDate);

        tfDayBookDate.setOnKeyPressed(event -> {
            if(event.getCode() == KeyCode.ENTER){
//                System.out.println("Entered Value show -- " + tfDayBookDate.getText());
//                System.out.println("Entered Value -- " + Communicator.text_to_date.fromString(tfDayBookDate.getText()).toString());
                startDate = Communicator.text_to_date.fromString(tfDayBookDate.getText()).toString();
                fetchAccountsDayBookList("");
                Node nextNode = CommonTraversa.getNextFocusableNode(tfDayBookDate.getScene());
                if (nextNode != null) {
                    nextNode.requestFocus();
                }
            }

            if(event.getCode() == KeyCode.TAB){
                startDate = Communicator.text_to_date.fromString(tfDayBookDate.getText()).toString();
                fetchAccountsDayBookList("");
            }
        });

        if (startDate == "") {
            startDate = Communicator.text_to_date.fromString(tfDayBookDate.getText()).toString();
        }

        AccountsDayBookListLogger.info("Start of Initialize method of : AccountsDayBookList");
        fetchAccountsDayBookList("");

        tfAccountsDayBookListSearch.textProperty().addListener((observable, oldValue, newValue) -> {
            filterData(newValue.trim());
        });

        // Setting the handler for each button
        setDayBookEnterKeyPressedHandler(btExportPdf);
        setDayBookEnterKeyPressedHandler(btExportExcel);
        setDayBookEnterKeyPressedHandler(btExportCsv);
        setDayBookEnterKeyPressedHandler(btExportPrint);
        tblAccontsDayBookList.requestFocus();
    }

    // Method to handle key pressed event for exporting buttons
    private void setDayBookEnterKeyPressedHandler(Button button) {
        button.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                Node nextNode = CommonTraversa.getNextFocusableNode(button.getScene());
                if (nextNode != null) {
                    nextNode.requestFocus();
                }
            }
        });
    }


    //todo: fetch Accounts Day Book List
    private void fetchAccountsDayBookList(String searchKey) {
        AccountsDayBookListLogger.info(" fetch Accounts Day Book List : AccountsDayBookList");
        try {
            Map<String, String> body = new HashMap<>();
            body.put("startDate", startDate);
            String requestBody = Globals.mapToStringforFormData(body);
            HttpResponse<String> response = APIClient.postFormDataRequest(requestBody, EndPoints.ACCOUNTS_DAY_BOOK_LIST);
            JsonObject responseBody = new Gson().fromJson(response.body(), JsonObject.class);
//            System.out.println("accounts_book_list" + responseBody);
            ObservableList<AccountsDayBookDTO> observableList = FXCollections.observableArrayList();
            if (responseBody.get("responseStatus").getAsInt() == 200) {
                JsonArray responseList = responseBody.getAsJsonArray("responseList");
//                System.out.println("accounts_book_list" + responseList);
                if (responseList.size() > 0) {
//                    System.out.println("accounts_book_list" + responseList);
                    tblAccontsDayBookList.setVisible(true);
                    for (JsonElement element : responseList) {
                        JsonObject item = element.getAsJsonObject();
                        String id = item.get("id").getAsString();
                        String date = item.get("transaction_date").getAsString();
                        String ledger_name = item.get("perticulars").getAsString();
                        String voucher_type = item.get("voucher_type").getAsString();
                        vchr_type = item.get("voucher_type").getAsString();
                        String voucher_no = item.get("voucher_no") != null ? item.get("voucher_no").getAsString() : "";
                        String debit_amt = "", credit_amt = "";
                        if (vchr_type.equalsIgnoreCase("Sales Invoice") || vchr_type.equalsIgnoreCase("Payment") || vchr_type.equalsIgnoreCase("Purchase Return Invoice") || vchr_type.equalsIgnoreCase("Debitnote")) {
                            debit_amt = item.get("amount").getAsString();
//                            System.out.println("debit_amt "+ debit_amt + " ");
                        } else {
                            credit_amt = item.get("amount").getAsString();
//                            System.out.println("credit_amt "+ credit_amt + " ");
                        }

//                        System.out.printf("type " + vchr_type + " ");
                        observableList.add(new AccountsDayBookDTO(id, date, ledger_name, voucher_type, voucher_no, debit_amt, credit_amt));


                    }


                    tblAccontsDayBookDate.setCellValueFactory(new PropertyValueFactory<>("date"));
                    tblAccontsDayBookParticulars.setCellValueFactory(new PropertyValueFactory<>("ledger_name"));
                    tblAccontsDayBookVoucherTy.setCellValueFactory(new PropertyValueFactory<>("voucher_type"));
                    tblAccontsDayBookVoucherNo.setCellValueFactory(new PropertyValueFactory<>("voucher_no"));
//                    System.out.println("vchr_type" + vchr_type + " ");
//                    if (vchr_type.equalsIgnoreCase( "Purchase Invoice")){
                    tblAccontsDayBookDebitAmt.setCellValueFactory(new PropertyValueFactory<>("debit_amt"));
//
                    tblAccontsDayBookCreditAmt.setCellValueFactory(new PropertyValueFactory<>("credit_amt"));
//

                    tblAccontsDayBookList.setItems(observableList);
                    originalData = observableList;
                    calculateTotalAmount();
                    AccountsDayBookListLogger.info("Success in Displaying Accounts Day Book List : AccountsDayBook");
                } else {
                    tblAccontsDayBookList.getItems().clear();
                    calculateTotalAmount();
                    AccountsDayBookListLogger.debug("Accounts Day Book List ResponseObject is null : AccountsDayBook");
                }
            } else {
                AccountsDayBookListLogger.debug("Error in response of Accounts Day Book List : AccountsDayBook");
            }
        } catch (Exception e) {
            AccountsDayBookListLogger.error("Accounts Day Book List Error is " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void filterData(String keyword) {
        ObservableList<AccountsDayBookDTO> filteredData = FXCollections.observableArrayList();
        AccountsDayBookListLogger.info("Search Accounts Day Book List in AccountsDayBook");
        if (keyword.isEmpty()) {
            filteredData = (originalData);
        } else {
            for (AccountsDayBookDTO item : originalData) {
                if (matchesKeyword(item, keyword)) {
                    filteredData.add(item);
                }
            }
        }

        tblAccontsDayBookList.setItems(filteredData);
        calculateTotalAmount();
    }

    private boolean matchesKeyword(AccountsDayBookDTO item, String keyword) {
        String lowerCaseKeyword = keyword.toLowerCase();

        return item.getDate().toLowerCase().contains(lowerCaseKeyword) || item.getLedger_name().toLowerCase().contains(lowerCaseKeyword) || item.getVoucher_type().toLowerCase().contains(lowerCaseKeyword) || item.getVoucher_no().toLowerCase().contains(lowerCaseKeyword) || item.getDebit_amt().toLowerCase().contains(lowerCaseKeyword) || item.getCredit_amt().toLowerCase().contains(lowerCaseKeyword);

    }

    @FXML
    private void exportToExcel() {
        System.out.println("Excel Export");
    }

    private void calculateTotalAmount() {
        ObservableList<AccountsDayBookDTO> filteredData = tblAccontsDayBookList.getItems();
        // Calculate the Totals
        double totalDebit = 0.0;
        double totalCredit = 0.0;
        for (AccountsDayBookDTO item : filteredData) {
            totalDebit += Double.parseDouble(item.getDebit_amt().isEmpty() ? "0.0" : item.getDebit_amt());
            totalCredit += Double.parseDouble(item.getCredit_amt().isEmpty() ? "0.0" : item.getCredit_amt());
        }

        // Update Labels in the FXML
        lblTotalDebit.setText(String.format("%.2f", totalDebit));
        lblTotalCredit.setText(String.format("%.2f", totalCredit));
    }
}
