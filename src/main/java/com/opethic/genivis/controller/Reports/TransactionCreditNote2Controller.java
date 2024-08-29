package com.opethic.genivis.controller.Reports;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.opethic.genivis.dto.TransactionCreditNote2DTO;
import com.opethic.genivis.models.master.ledger.CommonStringOption;
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
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.util.StringConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import static com.opethic.genivis.utils.FxmFileConstants.ACCOUNTS_LEDGER_REPORT2_LIST_SLUG;
import static com.opethic.genivis.utils.FxmFileConstants.TRANSACTION_CREDIT_NOTE1_LIST_SLUG;

public class TransactionCreditNote2Controller implements Initializable {

    private static final Logger TransactionCreditNote2Logger = LoggerFactory.getLogger(TransactionCreditNote2Controller.class);
    @FXML
    private TableView<TransactionCreditNote2DTO> tblListCreditNote2;
    @FXML
    private TableColumn<TransactionCreditNote2DTO, String> tblListCreditNote2Month, tblListCreditNote2NoVoucher, tblListCreditNote2TotalAmt;
    @FXML
    private TextField searchCreditNote2;

    private ObservableList<TransactionCreditNote2DTO> originalData;

    @FXML
    private Label lblTotalDebit;
    @FXML
    private Label lblTotalCredit;
    @FXML
    private DatePicker fromDateCreditNote2,toDateCreditNote2;
    @FXML
    public BorderPane tranx_cdt_nt2_borderpane;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initData();
    }

    private void initData() {

        TransactionCreditNote2Logger.info("Start of Initialize method of : TransactionCreditNote2Controller");

        fetchCreditNote1List("");

        searchCreditNote2.textProperty().addListener((observable, oldValue, newValue) -> {
            filterData(newValue.trim());
        });

        tranx_cdt_nt2_borderpane.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ESCAPE) {
//                System.out.println("Esc Clicked....!");
                GlobalController.getInstance().addTabStatic(TRANSACTION_CREDIT_NOTE1_LIST_SLUG, false);
            }
        });
        Platform.runLater(() -> {
            searchCreditNote2.requestFocus();
        });

    }

    private void fetchCreditNote1List(String searchKey) {
        TransactionCreditNote2Logger.info("fetch Transaction Credit Note 2 List : TransactionCreditNote2Controller");
        try {
            Map<String, String> body = new HashMap<>();

            String formData = Globals.mapToStringforFormData(body);
//            System.out.println("formData" + formData);
            HttpResponse<String> response = APIClient.postFormDataRequest(formData, EndPoints.TRANSACTION_CREDIT_NOTE2_LIST);
            JsonObject responseBody = new Gson().fromJson(response.body(), JsonObject.class);
//            System.out.println("LedgerReport1_list" + responseBody);
            ObservableList<TransactionCreditNote2DTO> observableList = FXCollections.observableArrayList();
            if (responseBody.get("responseStatus").getAsInt() == 200) {
                JsonArray responseList = responseBody.getAsJsonArray("response");
//                System.out.println("LedgerReport1_list" + responseBody);
//                System.out.println("LedgerReport1_list" + responseBody.get("d_end_date"));
//                System.out.println("LedgerReport1_list" + responseBody.get("d_start_date"));


                fromDateCreditNote2.setValue(!responseBody.get("d_start_date").getAsString().isEmpty() ? DateConvertUtil.convertStringToLocalDate(responseBody.get("d_start_date").getAsString()) : null);

                toDateCreditNote2.setValue(!responseBody.get("d_end_date").getAsString().isEmpty() ? DateConvertUtil.convertStringToLocalDate(responseBody.get("d_end_date").getAsString()) : null);
                if (responseList.size() > 0) {
                    System.out.println("LedgerReport1_list => " + responseList);

                    tblListCreditNote2.setVisible(true);
                    for (JsonElement element : responseList) {
                        JsonObject item = element.getAsJsonObject();
                        String month = item.get("month").getAsString();
                        String total_amt = String.valueOf(item.get("total_amt").getAsDouble());
                        String no_vouchers = String.valueOf(item.get("no_vouchers").getAsDouble());
                        observableList.add(new TransactionCreditNote2DTO(month, no_vouchers, total_amt));
                    }


                    tblListCreditNote2Month.setCellValueFactory(new PropertyValueFactory<TransactionCreditNote2DTO, String>("month"));
                        tblListCreditNote2NoVoucher.setCellValueFactory(new PropertyValueFactory<TransactionCreditNote2DTO, String>("no_vouchers"));
                    tblListCreditNote2TotalAmt.setCellValueFactory(new PropertyValueFactory<TransactionCreditNote2DTO, String>("total_amt"));

                    tblListCreditNote2.setItems(observableList);
                    tblListCreditNote2.refresh();
                    originalData = observableList;

                    calculationTotal();
                    Platform.runLater(() -> {
                        searchCreditNote2.requestFocus();

                    });
                    TransactionCreditNote2Logger.debug("Success in Displaying Transaction Credit Note 2 List : TransactionCreditNote2Controller");
                } else {
                    tblListCreditNote2.getItems().clear();
                    calculationTotal();
                    TransactionCreditNote2Logger.debug("Transaction Credit Note 2 List ResponseObject is null : TransactionCreditNote2Controller");
                }
            } else {
                tblListCreditNote2.getItems().clear();
                calculationTotal();
                TransactionCreditNote2Logger.debug("Error in response of Transaction Credit Note 2 List : TransactionCreditNote2Controller");
            }
        } catch (Exception e) {
            TransactionCreditNote2Logger.error("Transaction Credit Note 2 Error is " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void filterData(String keyword) {
        ObservableList<TransactionCreditNote2DTO> filteredData = FXCollections.observableArrayList();
        TransactionCreditNote2Logger.error("Search Transaction Credit Note 2 in TransactionCreditNote2Controller");
        if (keyword.isEmpty()) {
            tblListCreditNote2.setItems(originalData);
            calculationTotal();
            return;
        }

        for (TransactionCreditNote2DTO item : originalData) {
            if (matchesKeyword(item, keyword)) {
                filteredData.add(item);
            }
        }

        tblListCreditNote2.setItems(filteredData);
        calculationTotal();
    }

    private boolean matchesKeyword(TransactionCreditNote2DTO item, String keyword) {
        String lowerCaseKeyword = keyword.toLowerCase();

        return item.getMonth().toLowerCase().contains(lowerCaseKeyword) ||
                item.getNoVoucher().toLowerCase().contains(lowerCaseKeyword) ||
                item.getTotalAmt().toLowerCase().contains(lowerCaseKeyword);

    }

    private void calculationTotal() {
        ObservableList<TransactionCreditNote2DTO> filteredData = tblListCreditNote2.getItems();
        // Calculate the Totals
        double totalDebit = 0.0;
        double totalCredit = 0.0;
        for (TransactionCreditNote2DTO item : filteredData) {
            totalDebit += Double.parseDouble(item.getNoVoucher().isEmpty() ? "0.0" : item.getNoVoucher());
            totalCredit += Double.parseDouble(item.getTotalAmt().isEmpty() ? "0.0" : item.getTotalAmt());
        }

        // Update Labels in the FXML
        lblTotalDebit.setText(String.format("%.2f", totalDebit));
        lblTotalCredit.setText(String.format("%.2f", totalCredit));
    }


}
