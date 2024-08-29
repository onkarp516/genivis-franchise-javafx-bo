package com.opethic.genivis.controller.Reports;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.opethic.genivis.dto.LedgerReport1DTO;
import com.opethic.genivis.dto.LedgerReport2DTO;
import com.opethic.genivis.dto.reqres.product.Communicator;
import com.opethic.genivis.network.APIClient;
import com.opethic.genivis.network.EndPoints;
import com.opethic.genivis.utils.*;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;

import java.net.URL;
import java.net.http.HttpResponse;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.opethic.genivis.utils.FxmFileConstants.*;

public class AccountsLedgerReport2Controller implements Initializable {

    private static final Logger LedgerReport2ListLogger = LoggerFactory.getLogger(AccountsLedgerReport2Controller.class);
    @FXML
    private TableView<LedgerReport2DTO> tblvLedgerReport2List;
    @FXML
    private TableColumn<LedgerReport2DTO, String> tblLedgerReport2Date, tblLedgerReport2Ledger, tblLedgerReport2VoucherTy, tblLedgerReport2VoucherNo, tblLedgerReport2Debit, tblLedgerReport2Credit, tblLedgerReport2ClosingBalance, tblLedgerReport2Type;

    private ObservableList<LedgerReport2DTO> originalData;
    public static String ledger_name = "";

    @FXML
    private TextField tfAccountsLedgerReport2ListSearch;
//    @FXML
//    public DatePicker DPAccountsLedgerReport2StartDT, DPAccountsLedgerReport2EndDT;
    @FXML
    private TextField tfLedgerReport2StartDT, tfLedgerReport2EndDT;
    @FXML
    private ComboBox<String> cbDuration;
    @FXML
    private ComboBox<String> cbType;
    @FXML
    private Label ledger_label;
    @FXML
    public BorderPane borderpane;
    @FXML
    private Label lblTotalDebit;
    @FXML
    private Label lblTotalCredit;
    @FXML
    private Label lblClosingBalanace;
    @FXML
    private Label lblOpeningBalance;
    String duration = "";
    String start_date = "";
    String end_date = "", selectedType = "";
    String selectedDuration = "", firstDate = "", secondDate = "";

    @FXML
    private Button btExportPdf, btExportExcel, btExportCsv, btExportPrint;

    private void handleDurationComboBoxAction(ActionEvent event) {
        selectedDuration = cbDuration.getSelectionModel().getSelectedItem();
//        System.out.println("Selected Duration: " + selectedDuration);
        if (selectedDuration == "This Month") {
            duration = "month";
//            System.out.println("duration " + duration);
        } else if (selectedDuration == "Last Month") {
            duration = "lastMonth";
//            System.out.println("duration " + duration);
        } else if (selectedDuration == "Half Year") {
            duration = "halfYear";
//            System.out.println("duration " + duration);
        } else if (selectedDuration == "Full Year") {
            duration = "year";
//            System.out.println("duration " + duration);
//            System.out.println("go to year page " + ledgerReportId);
            AccountsLedgerReport3Controller.ledger_name = ledger_name;
            GlobalController.getInstance().addTabStatic1(ACCOUNTS_LEDGER_REPORT3_LIST_SLUG, false, ledgerReportId);

        }
        fetchLedgerReport2List("");
    }

    private void handleTypeComboBoxAction(ActionEvent event) {
        selectedType = cbType.getSelectionModel().getSelectedItem();
//        System.out.println("Selected Type: " + selectedType);

        if (cbType.getSelectionModel().getSelectedItem() == "Sale Invoice") {
            selectedType = "sales invoice";
//            System.out.println("selectedType " + selectedType);
        } else if (cbType.getSelectionModel().getSelectedItem() == "Sale Returns") {
            selectedType = "sales return";
//            System.out.println("selectedType " + selectedType);
        } else if (cbType.getSelectionModel().getSelectedItem() == "Receipt") {
            selectedType = "receipt";
//            System.out.println("selectedType " + selectedType);
        } else if (cbType.getSelectionModel().getSelectedItem() == "Credit Note") {
            selectedType = "credit note";
        } else if (cbType.getSelectionModel().getSelectedItem() == "All") {
            selectedType = "all";
        }
        filterLedgerReport2ListByFilter();
    }

    private Integer ledgerReportId = -1;

    public void setEditId(Integer InId) {
        ledgerReportId = InId;
//        System.out.println("ledgerReportId"+ledgerReportId);
        if (InId > -1) {
            setEditData();
        }
    }

    private void setEditData() {
        fetchLedgerReport2List(String.valueOf(ledgerReportId));
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        LedgerReport2ListLogger.info("Start of Initialize method of : AccountsLedgerReport2Controller");

        // Setting the handler for each node
        setKeyPressedHandler(tfAccountsLedgerReport2ListSearch);
        setKeyPressedHandler(cbDuration);
        setKeyPressedHandler(tfLedgerReport2StartDT);
        setKeyPressedHandler(tfLedgerReport2EndDT);
        setKeyPressedHandler(cbType);
        setKeyPressedHandler(btExportPdf);
        setKeyPressedHandler(btExportExcel);
        setKeyPressedHandler(btExportCsv);
        setKeyPressedHandler(btExportPrint);
        tblvLedgerReport2List.requestFocus();

        tfLedgerReport2EndDT.setOnKeyPressed(event ->{
            if(event.getCode() == KeyCode.ENTER){
                System.out.println("DateFilter From Date " + tfLedgerReport2StartDT.getText().toString());
                System.out.println("DateFilter To Date " + tfLedgerReport2EndDT.getText().toString());
                fetchLedgerReport2List("");
                Node nextNode = CommonTraversa.getNextFocusableNode(cbType.getScene());
                if (nextNode != null) {
                    nextNode.requestFocus();
                }
            }
        });

        ledger_label.setText("Monthly Ledger Report - " + ledger_name);
        Platform.runLater(() -> tfAccountsLedgerReport2ListSearch.requestFocus());
        borderpane.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ESCAPE) {
//                System.out.println("Esc Clicked....!");
                GlobalController.getInstance().addTabStatic(ACCOUNTS_LEDGER_REPORT1_LIST_SLUG, false);
            }
        });

        cbDuration.getItems().addAll("This Month", "Last Month", "Half Year", "Full Year");
        cbDuration.setPromptText("Select Duration");
        cbDuration.setOnAction(this::handleDurationComboBoxAction);
        cbDuration.setValue(cbDuration.getItems().get(0));
        duration = "month";
        //todo:open Filter dropdown on Space
        cbDuration.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode() == KeyCode.SPACE && !cbDuration.isShowing()) {
                cbDuration.show();
                event.consume(); // Consume the event to prevent other actions
            }
        });


        cbType.getItems().addAll("All", "Sale Invoice", "Sale Returns", "Receipt", "Credit Note");
        cbType.setPromptText("Select Type");
        cbType.setOnAction(this::handleTypeComboBoxAction);
        cbType.setValue(cbType.getItems().get(0));
        selectedType = "all";
        //todo:open Filter dropdown on Space
        cbType.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode() == KeyCode.SPACE && !cbType.isShowing()) {
                cbType.show();
                event.consume(); // Consume the event to prevent other actions
            }
        });

        fetchLedgerReport2List("");


        tfAccountsLedgerReport2ListSearch.textProperty().addListener((observable, oldValue, newValue) -> {
            filterData(newValue.trim());
        });
        Platform.runLater(() -> {
            tfAccountsLedgerReport2ListSearch.requestFocus();
        });

        DateValidator.applyDateFormat(tfLedgerReport2StartDT);
        DateValidator.applyDateFormat(tfLedgerReport2EndDT);
    }

    // Method to handle key pressed event for any Node
    private void setKeyPressedHandler(Node node) {
        node.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                Node nextNode = CommonTraversa.getNextFocusableNode(node.getScene());
                if (nextNode != null) {
                    nextNode.requestFocus();
                }
            }
        });
    }

    private void fetchLedgerReport2List(String searchKey) {
        LedgerReport2ListLogger.info(" fetch Ledger Report 2 List  : AccountsLedgerReport2Controller");

        try {
            Map<String, String> body = new HashMap<>();
            body.put("ledger_master_id", String.valueOf(ledgerReportId));
//                body.put("start_date", "2024-03-01");
//                body.put("end_date", "2024-03-31");
            System.out.println("duration---------> " + duration);
            body.put("duration", duration);
            if(!tfLedgerReport2StartDT.getText().equalsIgnoreCase("")  || !tfLedgerReport2EndDT.getText().equalsIgnoreCase("")){
                System.out.println("date sdf " );
            body.put("start_date", Communicator.text_to_date.fromString(tfLedgerReport2StartDT.getText()).toString());
            body.put("end_date", Communicator.text_to_date.fromString(tfLedgerReport2EndDT.getText()).toString());
            }
            String requestBody = Globals.mapToStringforFormData(body);
            System.out.println("requestBody----> " + requestBody);
            HttpResponse<String> response = APIClient.postFormDataRequest(requestBody, EndPoints.ACCOUNTS_LEDGER_REPORT2_LIST);
            JsonObject responseBody = new Gson().fromJson(response.body(), JsonObject.class);
            System.out.println("fetchLedgerReport2List" + responseBody);
            ObservableList<LedgerReport2DTO> observableList = FXCollections.observableArrayList();
            if (responseBody.get("responseStatus").getAsInt() == 200) {
                JsonArray responseList = responseBody.getAsJsonArray("response");
//                System.out.println("fetchLedgerReport2List----> " + responseList);
                String Opening_bal = responseBody.get("Opening_bal").getAsString();

//                DPAccountsLedgerReport2StartDT.setValue(!responseBody.get("d_start_date").getAsString().isEmpty() ? DateConvertUtil.convertStringToLocalDate(responseBody.get("d_start_date").getAsString()) : null);
//                DPAccountsLedgerReport2EndDT.setValue(!responseBody.get("d_end_date").getAsString().isEmpty() ? DateConvertUtil.convertStringToLocalDate(responseBody.get("d_end_date").getAsString()) : null);

                firstDate = (responseBody.get("d_start_date").getAsString());
                secondDate = responseBody.get("d_end_date").getAsString();
                System.out.println("firstDate-------> " + firstDate);
                System.out.println("secondDate-------> " + secondDate);
                LocalDate firstDateObj = LocalDate.parse(firstDate);
                LocalDate secondDateObj = LocalDate.parse(secondDate);


                if (responseList.size() > 0) {
                    tblvLedgerReport2List.setVisible(true);
                    for (JsonElement element : responseList) {
                        JsonObject item = element.getAsJsonObject();
                        String id = item.get("id").getAsString();
                        String date = item.get("transaction_date").getAsString();
                        String ledger_name = item.get("ledgerName").getAsString();
                        String voucher_type = item.get("voucher_type").getAsString();
                        String voucher_no = item.get("invoice_no").getAsString();
                        String debit = item.get("debit").getAsString();
                        String credit = item.get("credit").getAsString();
                        String closing_balance = item.get("Closing_balance").getAsString();
                        String type = item.get("transc_type").getAsString();

                        observableList.add(new LedgerReport2DTO(id, date, ledger_name, voucher_type, voucher_no, debit, credit, closing_balance, type));
                    }

//                    tfLedgerReport2StartDT.setText(firstDateObj.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
//                    tfLedgerReport2EndDT.setText(secondDateObj.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));

                    lblOpeningBalance.setText(String.format(Opening_bal));

                    tblLedgerReport2Date.setCellValueFactory(new PropertyValueFactory<>("date"));
                    tblLedgerReport2Ledger.setCellValueFactory(new PropertyValueFactory<>("ledger_name"));
                    tblLedgerReport2VoucherTy.setCellValueFactory(new PropertyValueFactory<>("voucher_type"));
                    tblLedgerReport2VoucherNo.setCellValueFactory(new PropertyValueFactory<>("voucher_no"));
                    tblLedgerReport2Debit.setCellValueFactory(new PropertyValueFactory<>("debit"));
                    tblLedgerReport2Credit.setCellValueFactory(new PropertyValueFactory<>("credit"));
                    tblLedgerReport2ClosingBalance.setCellValueFactory(new PropertyValueFactory<>("closing_balance"));
                    tblLedgerReport2Type.setCellValueFactory(new PropertyValueFactory<>("type"));

                    tblvLedgerReport2List.setItems(observableList);
                    originalData = observableList;
                    calculateTotalAmount();
                    LedgerReport2ListLogger.debug("Success in Displaying Ledger Report 2 List : AccountsLedgerReport2Controller");
                } else {
                    tblvLedgerReport2List.getItems().clear();
                    calculateTotalAmount();
                    LedgerReport2ListLogger.debug("Ledger Report 2 List ResponseObject is null : AccountsLedgerReport2Controller");
                }
            } else {
                tblvLedgerReport2List.getItems().clear();
                calculateTotalAmount();
                LedgerReport2ListLogger.debug("Error in response of Ledger Report 2 List : AccountsLedgerReport2Controller");
            }
        } catch (Exception e) {
            LedgerReport2ListLogger.error("Ledger Report 2 List Error is " + e.getMessage());
            e.printStackTrace();
        }
    }


    private void filterLedgerReport2ListByFilter() {
        ObservableList<LedgerReport2DTO> filteredData = FXCollections.observableArrayList();
//        System.out.println("isFunded->>>>" + isFunded);

        LedgerReport2ListLogger.error("Search Accounts Ledger Report 2 in AccountsLedgerReport2Controller");
        if (selectedType != "all") {
            filteredData = originalData.stream().filter((item -> item.getVoucher_type().toString().equalsIgnoreCase(selectedType))).collect(Collectors.toCollection(FXCollections::observableArrayList));

        } else {
            filteredData = originalData.stream().collect(Collectors.toCollection(FXCollections::observableArrayList));

        }


        tblvLedgerReport2List.setItems(filteredData);

        calculateTotalAmount();


    }

    private void calculateTotalAmount() {
        ObservableList<LedgerReport2DTO> filteredData = tblvLedgerReport2List.getItems();
        // Calculate the Totals
        double totalDebit = 0.0;
        double totalCredit = 0.0;
        double totalClosingBalanace = 0.0;
        for (LedgerReport2DTO item : filteredData) {
            totalDebit += Double.parseDouble(item.getDebit().isEmpty() ? "0.0" : item.getDebit());
            totalCredit += Double.parseDouble(item.getCredit().isEmpty() ? "0.0" : item.getCredit());
            totalClosingBalanace += Double.parseDouble(item.getClosing_balance().isEmpty() ? "0.0" : item.getClosing_balance());
        }

        // Update Labels in the FXML
        lblTotalDebit.setText(String.format("%.2f", totalDebit));
        lblTotalCredit.setText(String.format("%.2f", totalCredit));
        lblClosingBalanace.setText(String.format("%.2f", totalClosingBalanace));
    }

    private void filterData(String keyword) {
        ObservableList<LedgerReport2DTO> filteredData = FXCollections.observableArrayList();

        LedgerReport2ListLogger.error("Search Accounts Ledger Report 2 in AccountsLedgerReport1Controller");
        if (keyword.isEmpty()) {
            tblvLedgerReport2List.setItems(originalData);
            return;
        }

        for (LedgerReport2DTO item : originalData) {
            if (matchesKeyword(item, keyword)) {
                filteredData.add(item);
            }
        }

        tblvLedgerReport2List.setItems(filteredData);
        calculateTotalAmount();
    }

    private boolean matchesKeyword(LedgerReport2DTO item, String keyword) {
        String lowerCaseKeyword = keyword.toLowerCase();

        return
                item.getLedger_name().toLowerCase().contains(lowerCaseKeyword) ||
                        item.getType().toLowerCase().contains(lowerCaseKeyword) ||
                        item.getDebit().toLowerCase().contains(lowerCaseKeyword) ||
                        item.getCredit().toLowerCase().contains(lowerCaseKeyword) ||
                        item.getVoucher_type().toLowerCase().contains(lowerCaseKeyword) ||
                        item.getVoucher_no().toLowerCase().contains(lowerCaseKeyword) ||
                        item.getClosing_balance().toLowerCase().contains(lowerCaseKeyword) ||
                        item.getType().toLowerCase().contains(lowerCaseKeyword) ||
                        item.getDate().toLowerCase().contains(lowerCaseKeyword);

    }

}
