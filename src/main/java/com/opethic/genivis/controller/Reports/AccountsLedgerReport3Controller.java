package com.opethic.genivis.controller.Reports;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.opethic.genivis.dto.LedgerReport1DTO;
import com.opethic.genivis.dto.LedgerReport2DTO;
import com.opethic.genivis.dto.LedgerReport3DTO;
import com.opethic.genivis.network.APIClient;
import com.opethic.genivis.network.EndPoints;
import com.opethic.genivis.utils.CommonTraversa;
import com.opethic.genivis.utils.GlobalController;
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
import org.json.JSONStringer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import static com.opethic.genivis.utils.FxmFileConstants.ACCOUNTS_LEDGER_REPORT2_LIST_SLUG;
import static com.opethic.genivis.utils.FxmFileConstants.STOCKS_STOCK_REPORT2_LIST_SLUG;

public class AccountsLedgerReport3Controller implements Initializable {
    @FXML
    private TextField tfAccountsLedgerReport3ListSearch,tfFromAmt,tfToAmt;

    private ObservableList<LedgerReport3DTO> originalData;
    public static String ledger_name = "";
    @FXML
    private Label ledger_label;
    @FXML
    private Label lblTotalDebit;
    @FXML
    public BorderPane borderpane;
    @FXML
    private Label lblTotalCredit, lblTotalCredit1, lblAvgTotal0, lblAvgTotal1, lblAvgTotal2;
    private static final Logger LedgerReport3ListLogger = LoggerFactory.getLogger(AccountsLedgerReport3Controller.class);
    @FXML
    private TableView<LedgerReport3DTO> tblvLedgerReport3List;
    @FXML
    private TableColumn<LedgerReport3DTO, String> tblLedgerReport3Particulars, tblLedgerReport3Debit, tblLedgerReport3Credit, tblLedgerReport3ClosingBalance, tblLedgerReport3Type;
    private Integer ledgerReportId = -1;

    @FXML
    private Button btReset, btExportPdf, btExportExcel, btExportCsv, btExportPrint;

    public void setEditId(Integer InId) {
        ledgerReportId = InId;
//        System.out.println("ledgerReportId"+ledgerReportId);
//        fetchLedgerReport3List();
        if (InId > -1) {
            setEditData();
        }
    }

    private void setEditData() {
        fetchLedgerReport3List();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        LedgerReport3ListLogger.info("Start of Initialize method of : AccountsLedgerReport3Controller " + ledgerReportId);

        // Setting the handler for each node
        setKeyPressedHandler(tfAccountsLedgerReport3ListSearch);
        setKeyPressedHandler(tfFromAmt);
        setKeyPressedHandler(tfToAmt);
        setKeyPressedHandler(btReset);
        setKeyPressedHandler(btExportPdf);
        setKeyPressedHandler(btExportExcel);
        setKeyPressedHandler(btExportCsv);
        setKeyPressedHandler(btExportPrint);
        tblvLedgerReport3List.requestFocus();

        tfToAmt.setOnKeyPressed(event->{
            if(event.getCode() == KeyCode.ENTER){
                if (!tfFromAmt.getText().isEmpty() && !tfToAmt.getText().isEmpty()) {
                    filterLedgerListByAmount();
                }
                Node nextNode = CommonTraversa.getNextFocusableNode(tfToAmt.getScene());
                if (nextNode != null) {
                    nextNode.requestFocus();
                }
            }
        });

        ledger_label.setText("Ledger Report Month wise - " + ledger_name);
        Platform.runLater(() -> tfAccountsLedgerReport3ListSearch.requestFocus());
        borderpane.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ESCAPE) {
//                System.out.println("Esc Clicked....!");
                GlobalController.getInstance().addTabStatic1(ACCOUNTS_LEDGER_REPORT2_LIST_SLUG, false, ledgerReportId);
            }
        });

        tfAccountsLedgerReport3ListSearch.textProperty().addListener((observable, oldValue, newValue) -> {
            filterData(newValue.trim());
        });


        tfFromAmt.textProperty().addListener((obs, oldValue, newValueFromAmt) -> {
//            System.out.println("Fromt Amount: " + newValueFromAmt.trim());
//            tfFromAmt.setText(newValueFromAmt.trim());
            tfFromAmt.setText(newValueFromAmt.matches("^[0-9]*\\.?[0-9]*$") ? newValueFromAmt : oldValue);
        });

        tfToAmt.addEventFilter(KeyEvent.ANY, (KeyEvent event) -> {
            if (event.getCode() == KeyCode.TAB) {
                if (!tfFromAmt.getText().isEmpty() && !tfToAmt.getText().isEmpty()) {
                    filterLedgerListByAmount();
                }else{
                    tblvLedgerReport3List.setItems(originalData);
                    calculateTotalAmount();
                }

            }
        });

        tfToAmt.textProperty().addListener((obs, oldValue, newValueToAmt) -> {
//            System.out.println("To Amount: " + newValueToAmt.trim());
//            tfToAmt.setText(newValueToAmt.trim());
            tfToAmt.setText(newValueToAmt.matches("^[0-9]*\\.?[0-9]*$") ? newValueToAmt : oldValue);
        });

        Platform.runLater(() -> {
            tfAccountsLedgerReport3ListSearch.requestFocus();
        });
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

    private void fetchLedgerReport3List() {
        LedgerReport3ListLogger.info(" fetch Ledger Report 3 List  : AccountsLedgerReport3Controller");

        try {
            Map<String, String> body = new HashMap<>();
//            System.out.println("ledger_master_id  "+String.valueOf(ledgerReportId));
//            body.put("ledger_master_id", String.valueOf(ledgerReportId));
            body.put("ledger_master_id", ledgerReportId.toString());

            String requestBody = Globals.mapToStringforFormData(body);
//            System.out.println("requestBody :" + requestBody);
            HttpResponse<String> response = APIClient.postFormDataRequest(requestBody, EndPoints.ACCOUNTS_LEDGER_REPORT3_LIST);
            JsonObject responseBody = new Gson().fromJson(response.body(), JsonObject.class);
//            System.out.println("fetchLedgerReport3List" + responseBody);
            ObservableList<LedgerReport3DTO> observableList = FXCollections.observableArrayList();
            if (responseBody.get("responseStatus").getAsInt() == 200) {
                JsonArray responseList = responseBody.getAsJsonArray("response");
//                System.out.println("fetchLedgerReport3List" + responseList);
                if (responseList.size() > 0) {
//                    System.out.println("List" + responseList);
                    tblvLedgerReport3List.setVisible(true);
                    for (JsonElement element : responseList) {
                        JsonObject item = element.getAsJsonObject();
                        String particulars = item.get("month_name") != null ? item.get("month_name").getAsString() : "";
                        String debit = item.get("debit") != null ? item.get("debit").getAsString() : "";
                        String credit = item.get("credit") != null ? item.get("credit").getAsString() : "";
                        String closing_balance = item.get("closing_balance") != null ? item.get("closing_balance").getAsString() : "";
                        String type = item.get("type") != null ? item.get("type").getAsString() : "";

                        observableList.add(new LedgerReport3DTO(particulars, debit, credit, closing_balance, type));

//                        System.out.println("observableList: " + item);
                    }



                    tblLedgerReport3Particulars.setCellValueFactory(new PropertyValueFactory<>("particulars"));
                    tblLedgerReport3Debit.setCellValueFactory(new PropertyValueFactory<>("debit"));
                    tblLedgerReport3Credit.setCellValueFactory(new PropertyValueFactory<>("credit"));
                    tblLedgerReport3ClosingBalance.setCellValueFactory(new PropertyValueFactory<>("closing_balance"));
                    tblLedgerReport3Type.setCellValueFactory(new PropertyValueFactory<>("type"));

                    tblvLedgerReport3List.setItems(observableList);
                    originalData=observableList;
                    calculateTotalAmount();
                    LedgerReport3ListLogger.debug("Success in Displaying Ledger Report 3 List : AccountsLedgerReport3Controller");
                } else {
                    LedgerReport3ListLogger.debug("Ledger Report 3 List ResponseObject is null : AccountsLedgerReport3Controller");
                }
            } else {
                LedgerReport3ListLogger.debug("Error in response of Ledger Report 3 List : AccountsLedgerReport3Controller");
            }
        } catch (Exception e) {
            LedgerReport3ListLogger.error("Ledger Report 3 List Error is " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void filterData(String keyword) {
        ObservableList<LedgerReport3DTO> filteredData = FXCollections.observableArrayList();

        LedgerReport3ListLogger.error("Search Accounts Ledger Report 3 in AccountsLedgerReport1Controller");
        if (keyword.isEmpty()) {
            tblvLedgerReport3List.setItems(originalData);
            calculateTotalAmount();
            return;
        }

        for (LedgerReport3DTO item : originalData) {
            if (matchesKeyword(item, keyword)) {
                filteredData.add(item);
            }
        }

        tblvLedgerReport3List.setItems(filteredData);
        calculateTotalAmount();
    }

    private void calculateTotalAmount() {

        ObservableList<LedgerReport3DTO> filteredData = tblvLedgerReport3List.getItems();

        double totalDebit = 0.0;
        double totalCredit = 0.0;
        double totalClosingBalance = 0.0;

        int avgTotalDebitCnt = 0, avgTotalCreditCnt = 0, avgTotalClosingBalCnt = 0;

        for (LedgerReport3DTO item : filteredData) {
            if (Double.parseDouble(item.getDebit()) > 0) {
                avgTotalDebitCnt++;
            }

            if (Double.parseDouble(item.getCredit()) > 0) {
                avgTotalCreditCnt++;
            }

            if (Double.parseDouble(item.getClosing_balance()) > 0) {
                avgTotalClosingBalCnt++;
            }

            totalDebit += Double.parseDouble(item.getDebit().isEmpty() ? "0.0" : item.getDebit());
            totalCredit += Double.parseDouble(item.getCredit().isEmpty() ? "0.0" : item.getCredit());
            totalClosingBalance += Double.parseDouble(item.getClosing_balance().isEmpty() ? "0.0" : item.getClosing_balance());

        }

        lblTotalDebit.setText(String.format("%.2f", totalDebit));
        lblTotalCredit.setText(String.format("%.2f", totalCredit));
        lblTotalCredit1.setText(String.format("%.2f", totalClosingBalance));

        lblAvgTotal0.setText(String.format("%.2f", totalDebit > 0.0 ? totalDebit / avgTotalDebitCnt : 0.00));
        lblAvgTotal1.setText(String.format("%.2f", totalCredit > 0.0 ? totalCredit / avgTotalCreditCnt : 0.00));
        lblAvgTotal2.setText(String.format("%.2f", totalClosingBalance > 0.0 ? totalClosingBalance / avgTotalClosingBalCnt : 0.00));
    }

    private boolean matchesKeyword(LedgerReport3DTO item, String keyword) {
        String lowerCaseKeyword = keyword.toLowerCase();

        return
                        item.getDebit().toLowerCase().contains(lowerCaseKeyword) ||
                        item.getCredit().toLowerCase().contains(lowerCaseKeyword) ||
                        item.getClosing_balance().toLowerCase().contains(lowerCaseKeyword) ||
                        item.getType().toLowerCase().contains(lowerCaseKeyword) ||
                        item.getParticulars().toLowerCase().contains(lowerCaseKeyword) ;

    }

    @FXML
    private void handleResetAction() {
        tfFromAmt.setText("");
        tfToAmt.setText("");

        tblvLedgerReport3List.setItems(originalData);

        tfAccountsLedgerReport3ListSearch.requestFocus();
        calculateTotalAmount();
    }

    private void filterLedgerListByAmount() {
        ObservableList<LedgerReport3DTO> filteredData = FXCollections.observableArrayList();

        LedgerReport3ListLogger.error("Search Accounts Ledger Report 1 in AccountsLedgerReport1Controller");
        Double tfFromAmount = Double.parseDouble(tfFromAmt.getText());
        Double tfToAmount = Double.parseDouble(tfToAmt.getText());
        filteredData = originalData.stream().filter((item -> ((Double.parseDouble(item.getDebit()) > 0 && Double.parseDouble(item.getDebit()) >= tfFromAmount && Double.parseDouble(item.getDebit()) <= tfToAmount) || (Double.parseDouble(item.getCredit()) > 0 && Double.parseDouble(item.getCredit()) >= tfFromAmount && Double.parseDouble(item.getCredit()) <= tfToAmount)))).collect(Collectors.toCollection(FXCollections::observableArrayList));

        tblvLedgerReport3List.setItems(filteredData);
        calculateTotalAmount();
    }

}
