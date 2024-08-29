package com.opethic.genivis.controller.Reports;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.opethic.genivis.dto.TransactionDebitNote2DTO;
import com.opethic.genivis.network.APIClient;
import com.opethic.genivis.network.EndPoints;
import com.opethic.genivis.utils.DateConvertUtil;
import com.opethic.genivis.utils.GlobalController;
import com.opethic.genivis.utils.Globals;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import static com.opethic.genivis.utils.FxmFileConstants.ACCOUNTS_LEDGER_REPORT1_LIST_SLUG;
import static com.opethic.genivis.utils.FxmFileConstants.TRANSACTION_DEBIT_NOTE1_LIST_SLUG;

public class TransactionDebitNote2Controller implements Initializable {

    private static final Logger TransactionDebitNote2Logger = LoggerFactory.getLogger(TransactionDebitNote2Controller.class);
    @FXML
    private TableView<TransactionDebitNote2DTO> tblListDebitNote2;
    @FXML
    private TableColumn<TransactionDebitNote2DTO, String> tblListDebitNote2Month, tblListDebitNote2NoVoucher, tblListDebitNote2TotalAmt;
    @FXML
    private TextField searchDebitNote2;

    private ObservableList<TransactionDebitNote2DTO> originalData;

    @FXML
    private Label lblTotalDebit;
    @FXML
    private Label lblTotalCredit;
    @FXML
    private DatePicker fromDateDebitNote2,toDateDebitNote2;
    @FXML
    public BorderPane tranx_dbt_nt2_borderpane;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initData();
    }

    private void initData() {

        TransactionDebitNote2Logger.info("Start of Initialize method of : TransactionDebitNote2Controller");

        fetchDebitNote1List("");

        searchDebitNote2.textProperty().addListener((observable, oldValue, newValue) -> {
            filterData(newValue.trim());
        });

        tranx_dbt_nt2_borderpane.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ESCAPE) {
//                System.out.println("Esc Clicked....!");
                GlobalController.getInstance().addTabStatic(TRANSACTION_DEBIT_NOTE1_LIST_SLUG, false);
            }
        });

        Platform.runLater(() -> {
            searchDebitNote2.requestFocus();
        });



    }

    private void fetchDebitNote1List(String searchKey) {
        TransactionDebitNote2Logger.info("fetch Transaction Debit Note 2 List : TransactionDebitNote2Controller");
        try {
            Map<String, String> body = new HashMap<>();

            String formData = Globals.mapToStringforFormData(body);
//            System.out.println("formData" + formData);
            HttpResponse<String> response = APIClient.postFormDataRequest(formData, EndPoints.TRANSACTION_DEBIT_NOTE2_LIST);
            JsonObject responseBody = new Gson().fromJson(response.body(), JsonObject.class);
//            System.out.println("LedgerReport1_list" + responseBody);
            ObservableList<TransactionDebitNote2DTO> observableList = FXCollections.observableArrayList();
            if (responseBody.get("responseStatus").getAsInt() == 200) {
                JsonArray responseList = responseBody.getAsJsonArray("response");
//                System.out.println("LedgerReport1_list" + responseBody);
//                System.out.println("LedgerReport1_list" + responseBody.get("d_end_date"));
//                System.out.println("LedgerReport1_list" + responseBody.get("d_start_date"));


                fromDateDebitNote2.setValue(!responseBody.get("d_start_date").getAsString().isEmpty() ? DateConvertUtil.convertStringToLocalDate(responseBody.get("d_start_date").getAsString()) : null);

                toDateDebitNote2.setValue(!responseBody.get("d_end_date").getAsString().isEmpty() ? DateConvertUtil.convertStringToLocalDate(responseBody.get("d_end_date").getAsString()) : null);
                if (responseList.size() > 0) {
                    System.out.println("LedgerReport1_list => " + responseList);

                    tblListDebitNote2.setVisible(true);
                    for (JsonElement element : responseList) {
                        JsonObject item = element.getAsJsonObject();
                        String month = item.get("month").getAsString();
                        String total_amt = String.valueOf(item.get("total_amt").getAsDouble());
                        String no_vouchers = String.valueOf(item.get("no_vouchers").getAsDouble());
                        observableList.add(new TransactionDebitNote2DTO(month, no_vouchers, total_amt));
                    }


                    tblListDebitNote2Month.setCellValueFactory(new PropertyValueFactory<TransactionDebitNote2DTO, String>("month"));
                        tblListDebitNote2NoVoucher.setCellValueFactory(new PropertyValueFactory<TransactionDebitNote2DTO, String>("no_vouchers"));
                    tblListDebitNote2TotalAmt.setCellValueFactory(new PropertyValueFactory<TransactionDebitNote2DTO, String>("total_amt"));

                    tblListDebitNote2.setItems(observableList);
                    tblListDebitNote2.refresh();
                    originalData = observableList;

                    calculationTotal();
                    Platform.runLater(() -> {
                        searchDebitNote2.requestFocus();

                    });
                    TransactionDebitNote2Logger.debug("Success in Displaying Transaction Debit Note 2 List : TransactionDebitNote2Controller");
                } else {
                    tblListDebitNote2.getItems().clear();
                    calculationTotal();
                    TransactionDebitNote2Logger.debug("Transaction Debit Note 2 List ResponseObject is null : TransactionDebitNote2Controller");
                }
            } else {
                tblListDebitNote2.getItems().clear();
                calculationTotal();
                TransactionDebitNote2Logger.debug("Error in response of Transaction Debit Note 2 List : TransactionDebitNote2Controller");
            }
        } catch (Exception e) {
            TransactionDebitNote2Logger.error("Transaction Debit Note 2 Error is " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void filterData(String keyword) {
        ObservableList<TransactionDebitNote2DTO> filteredData = FXCollections.observableArrayList();
        TransactionDebitNote2Logger.error("Search Transaction Debit Note 2 in TransactionDebitNote2Controller");
        if (keyword.isEmpty()) {
            tblListDebitNote2.setItems(originalData);
            calculationTotal();
            return;
        }

        for (TransactionDebitNote2DTO item : originalData) {
            if (matchesKeyword(item, keyword)) {
                filteredData.add(item);
            }
        }

        tblListDebitNote2.setItems(filteredData);
        calculationTotal();
    }

    private boolean matchesKeyword(TransactionDebitNote2DTO item, String keyword) {
        String lowerCaseKeyword = keyword.toLowerCase();

        return item.getMonth().toLowerCase().contains(lowerCaseKeyword) ||
                item.getNoVoucher().toLowerCase().contains(lowerCaseKeyword) ||
                item.getTotalAmt().toLowerCase().contains(lowerCaseKeyword);

    }

    private void calculationTotal() {
        ObservableList<TransactionDebitNote2DTO> filteredData = tblListDebitNote2.getItems();
        // Calculate the Totals
        double totalDebit = 0.0;
        double totalCredit = 0.0;
        for (TransactionDebitNote2DTO item : filteredData) {
            totalDebit += Double.parseDouble(item.getNoVoucher().isEmpty() ? "0.0" : item.getNoVoucher());
            totalCredit += Double.parseDouble(item.getTotalAmt().isEmpty() ? "0.0" : item.getTotalAmt());
        }

        // Update Labels in the FXML
        lblTotalDebit.setText(String.format("%.2f", totalDebit));
        lblTotalCredit.setText(String.format("%.2f", totalCredit));
    }


}
