package com.opethic.genivis.controller.Reports;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.opethic.genivis.dto.TransactionDebitNote1DTO;
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

import static com.opethic.genivis.utils.FxmFileConstants.TRANSACTION_DEBIT_NOTE2_LIST_SLUG;

public class TransactionDebitNote1Controller implements Initializable {

    private static final Logger TransactionDebitNote1Logger = LoggerFactory.getLogger(TransactionDebitNote1Controller.class);
    @FXML
    private TableView<TransactionDebitNote1DTO> tblListDebitNote1;
    @FXML
    private TableColumn<TransactionDebitNote1DTO, String> tblListDebitNote1Date, tblListDebitNote1Particulars, tblListDebitNote1VoucherTy, tblListDebitNote1VoucherNo, tblListDebitNote1DebitAmt, tblListDebitNote1CreditAmt;
    @FXML
    private TextField searchDebitNote1;

    @FXML
    private ComboBox<CommonStringOption> comboboxTypeDebitNote1, comboboxDurationDebitNote1;

    private ObservableList<TransactionDebitNote1DTO> originalData;

    @FXML
    private Label lblTotalDebit;
    @FXML

    private Label lblTotalCredit;
    @FXML
    private DatePicker fromDateDebitNote1, toDateDebitNote1;

    @FXML
    private TextField fromAmountDebitNote1;
    @FXML
    private TextField toAmountDebitNote1;
    @FXML
    private boolean filterByDate = false;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initData();
    }

    private void initData() {

        TransactionDebitNote1Logger.info("Start of Initialize method of : TransactionDebitNote1Controller");

        getFilterTypeList();
        getDurationList();
        fetchDebitNote1List("");

        searchDebitNote1.textProperty().addListener((observable, oldValue, newValue) -> {
            filterData(newValue.trim());
        });

        fromAmountDebitNote1.textProperty().addListener((obs, oldValue, newValueFromAmt) -> {
//            System.out.println("Fromt Amount: " + newValueFromAmt.trim());
            fromAmountDebitNote1.setText(newValueFromAmt.matches("^[0-9]*\\.?[0-9]*$") ? newValueFromAmt : oldValue);
//            fromAmountDebitNote1.setText(newValueFromAmt.trim());
        });

        toAmountDebitNote1.addEventFilter(KeyEvent.ANY, (KeyEvent event) -> {

            if (event.getCode() == KeyCode.TAB) {
                if (!fromAmountDebitNote1.getText().isEmpty() && !toAmountDebitNote1.getText().isEmpty()) {
                    filterDebitListByAmount();
                } else {
//                    fetchDebitNote1List("");
                }

            }
        });

        toAmountDebitNote1.textProperty().addListener((obs, oldValue, newValueToAmt) -> {
//            System.out.println("To Amount: " + newValueToAmt.trim());
            toAmountDebitNote1.setText(newValueToAmt.matches("^[0-9]*\\.?[0-9]*$") ? newValueToAmt : oldValue);
//            toAmountDebitNote1.setText(newValueToAmt.trim());
        });
        toDateDebitNote1.addEventHandler(KeyEvent.ANY, (KeyEvent event) -> {
            if (event.getCode() == KeyCode.ENTER) {
                if (fromDateDebitNote1.getValue() != null && toDateDebitNote1.getValue() != null) {
                    filterByDate = true;
                    fetchDebitNote1List("");
                } else {
                    filterByDate = false;
                    fetchDebitNote1List("");
                }


            }
        });

        Platform.runLater(() -> {
            searchDebitNote1.requestFocus();
        });

    }

    private void getFilterTypeList() {
        List<CommonStringOption> lst = Globals.getFilterType();
        ObservableList<CommonStringOption> lstFilterType = FXCollections.observableArrayList(lst);
        comboboxTypeDebitNote1.getItems().clear();
        comboboxTypeDebitNote1.getItems().addAll(lstFilterType);
        comboboxTypeDebitNote1.setConverter(new StringConverter<CommonStringOption>() {
            @Override
            public String toString(CommonStringOption taxType) {
                return taxType != null ? taxType.getValue() : "";
            }

            @Override
            public CommonStringOption fromString(String string) {
                // You can implement this method if needed
                return null;
            }
        });
        comboboxTypeDebitNote1.setValue(lstFilterType.get(0));


    }

    private void getDurationList() {
        List<CommonStringOption> lst = Globals.getDurationType();
        ObservableList<CommonStringOption> lstFilterType = FXCollections.observableArrayList(lst);

        comboboxDurationDebitNote1.getItems().clear();
        comboboxDurationDebitNote1.getItems().addAll(lstFilterType);
        comboboxDurationDebitNote1.setConverter(new StringConverter<CommonStringOption>() {
            @Override
            public String toString(CommonStringOption taxType) {
                return taxType != null ? taxType.getLabel() : "";
            }

            @Override
            public CommonStringOption fromString(String string) {
                // You can implement this method if needed
                return null;
            }
        });
        comboboxDurationDebitNote1.setValue(lstFilterType.get(0));
        comboboxDurationDebitNote1.getSelectionModel().selectedItemProperty().addListener((options, oldValue, newValue) -> {
            if(comboboxDurationDebitNote1.getSelectionModel().getSelectedItem().getValue().equalsIgnoreCase("fullYear")){
                GlobalController.getInstance().addTabStatic(TRANSACTION_DEBIT_NOTE2_LIST_SLUG, false);
            }else{
                fetchDebitNote1List("");
            }

        });
    }

    private void fetchDebitNote1List(String searchKey) {
        TransactionDebitNote1Logger.info("fetch Transaction Debit Note 1 List : TransactionDebitNote1Controller");
        try {
            Map<String, String> body = new HashMap<>();

            if (filterByDate) {
                body.put("start_date", fromDateDebitNote1.getValue().toString());
                body.put("end_date", toDateDebitNote1.getValue().toString());
            } else {
                body.put("duration", comboboxDurationDebitNote1.getSelectionModel().getSelectedItem().getValue());

            }


//

            String formData = Globals.mapToStringforFormData(body);
//            System.out.println("formData" + formData);
            HttpResponse<String> response = APIClient.postFormDataRequest(formData, EndPoints.TRANSACTION_DEBIT_NOTE1_LIST);
            JsonObject responseBody = new Gson().fromJson(response.body(), JsonObject.class);
//            System.out.println("LedgerReport1_list" + responseBody);
            ObservableList<TransactionDebitNote1DTO> observableList = FXCollections.observableArrayList();
            if (responseBody.get("responseStatus").getAsInt() == 200) {
                JsonArray responseList = responseBody.getAsJsonArray("response");
//                System.out.println("LedgerReport1_list" + responseBody);
//                System.out.println("LedgerReport1_list" + responseBody.get("d_end_date"));
//                System.out.println("LedgerReport1_list" + responseBody.get("d_start_date"));

                fromDateDebitNote1.setValue(!responseBody.get("d_start_date").getAsString().isEmpty() ? DateConvertUtil.convertStringToLocalDate(responseBody.get("d_start_date").getAsString()) : null);

                toDateDebitNote1.setValue(!responseBody.get("d_end_date").getAsString().isEmpty() ? DateConvertUtil.convertStringToLocalDate(responseBody.get("d_end_date").getAsString()) : null);

                if (responseList.size() > 0) {
//                    System.out.println("LedgerReport1_list" + responseList);

                    filterByDate = false;
                    tblListDebitNote1.setVisible(true);
                    for (JsonElement element : responseList) {
                        JsonObject item = element.getAsJsonObject();
                        String id = item.get("voucher_id").getAsString();
                        String transaction_date = item.get("transaction_date").getAsString();
                        String particulars = item.get("particulars").getAsString();
                        String type = item.get("voucher_type") != null ? item.get("voucher_type").getAsString() : "";
                        String voucher_no = item.get("voucher_no").getAsString();
                        String debit = item.get("debit").getAsString();
                        String credit = item.get("credit").getAsString();

                        observableList.add(new TransactionDebitNote1DTO(id, transaction_date, particulars, type, voucher_no, debit, credit));
                    }


                    tblListDebitNote1Date.setCellValueFactory(new PropertyValueFactory<>("date"));
                    tblListDebitNote1Particulars.setCellValueFactory(new PropertyValueFactory<>("particulars"));
                    tblListDebitNote1VoucherTy.setCellValueFactory(new PropertyValueFactory<>("voucher_type"));
                    tblListDebitNote1VoucherNo.setCellValueFactory(new PropertyValueFactory<>("voucher_no"));
//                    System.out.println("vchr_type" + vchr_type + " ");
//                    if (vchr_type.equalsIgnoreCase( "Purchase Invoice")){
                    tblListDebitNote1DebitAmt.setCellValueFactory(new PropertyValueFactory<>("debit_amt"));
//
                    tblListDebitNote1CreditAmt.setCellValueFactory(new PropertyValueFactory<>("credit_amt"));

                    tblListDebitNote1.setItems(observableList);
                    originalData = observableList;

                    calculationTotal();
                    Platform.runLater(() -> {
                        searchDebitNote1.requestFocus();

                    });
                    TransactionDebitNote1Logger.debug("Success in Displaying Transaction Debit Note 1 List : TransactionDebitNote1Controller");
                } else {
                    tblListDebitNote1.getItems().clear();
                    calculationTotal();
                    TransactionDebitNote1Logger.debug("Transaction Debit Note 1 List ResponseObject is null : TransactionDebitNote1Controller");
                }
            } else {
                tblListDebitNote1.getItems().clear();
                calculationTotal();
                TransactionDebitNote1Logger.debug("Error in response of Transaction Debit Note 1 List : TransactionDebitNote1Controller");
            }
        } catch (Exception e) {
            TransactionDebitNote1Logger.error("Transaction Debit Note 1 Error is " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void filterData(String keyword) {
        ObservableList<TransactionDebitNote1DTO> filteredData = FXCollections.observableArrayList();
        TransactionDebitNote1Logger.error("Search Transaction Debit Note 1 in TransactionDebitNote1Controller");
        if (keyword.isEmpty()) {
            tblListDebitNote1.setItems(originalData);
            calculationTotal();
            return;
        }

        for (TransactionDebitNote1DTO item : originalData) {
            if (matchesKeyword(item, keyword)) {
                filteredData.add(item);
            }
        }

        tblListDebitNote1.setItems(filteredData);
        calculationTotal();
    }

    private boolean matchesKeyword(TransactionDebitNote1DTO item, String keyword) {
        String lowerCaseKeyword = keyword.toLowerCase();

        return item.getDate().toLowerCase().contains(lowerCaseKeyword) ||
                item.getParticulars_name().toLowerCase().contains(lowerCaseKeyword) ||
                item.getVoucher_type().toLowerCase().contains(lowerCaseKeyword) ||
                item.getVoucher_no().toLowerCase().contains(lowerCaseKeyword) ||
                item.getDebit_amt().toLowerCase().contains(lowerCaseKeyword) ||
                item.getCredit_amt().toLowerCase().contains(lowerCaseKeyword);
    }

    @FXML
    private void handleResetAction() {
        fromAmountDebitNote1.setText("");
        toAmountDebitNote1.setText("");
        initData();
//        getFilterTypeList();
//        getDurationList();
//        fetchDebitNote1List("");
    }

    private void calculationTotal() {
        ObservableList<TransactionDebitNote1DTO> filteredData = tblListDebitNote1.getItems();
        // Calculate the Totals
        double totalDebit = 0.0;
        double totalCredit = 0.0;
        for (TransactionDebitNote1DTO item : filteredData) {
            totalDebit += Double.parseDouble(item.getDebit_amt().isEmpty() ? "0.0" : item.getDebit_amt());
            totalCredit += Double.parseDouble(item.getCredit_amt().isEmpty() ? "0.0" : item.getCredit_amt());
        }

        // Update Labels in the FXML
        lblTotalDebit.setText(String.format("%.2f", totalDebit));
        lblTotalCredit.setText(String.format("%.2f", totalCredit));
    }

    private void filterDebitListByAmount() {
        ObservableList<TransactionDebitNote1DTO> filteredData = FXCollections.observableArrayList();

        TransactionDebitNote1Logger.error("Search Transaction Debit Note 1 in TransactionDebitNote1Controller");
        Double tfFromAmount = Double.parseDouble(fromAmountDebitNote1.getText());
        Double tfToAmount = Double.parseDouble(toAmountDebitNote1.getText());
        filteredData = originalData.stream().filter((item -> ((Double.parseDouble(item.getDebit_amt()) > 0 && Double.parseDouble(item.getDebit_amt()) >= tfFromAmount && Double.parseDouble(item.getDebit_amt()) <= tfToAmount) || (Double.parseDouble(item.getCredit_amt()) > 0 && Double.parseDouble(item.getCredit_amt()) >= tfFromAmount && Double.parseDouble(item.getCredit_amt()) <= tfToAmount)))).collect(Collectors.toCollection(FXCollections::observableArrayList));

        tblListDebitNote1.setItems(filteredData);
        calculationTotal();
    }
}
