package com.opethic.genivis.controller.Reports;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.opethic.genivis.GenivisApplication;
import com.opethic.genivis.controller.account_entry.PaymentCreateController;
import com.opethic.genivis.dto.Reports.PurchaseRegisterDTO;
import com.opethic.genivis.dto.StocksStockValuation2DTO;
import com.opethic.genivis.dto.reqres.creditNote.CreditNoteByIdMainDTO;
import com.opethic.genivis.dto.reqres.payment.PaymentListDTO;
import com.opethic.genivis.dto.reqres.product.Communicator;
import com.opethic.genivis.network.APIClient;
import com.opethic.genivis.network.EndPoints;
import com.opethic.genivis.network.RequestType;
import com.opethic.genivis.utils.AlertUtility;
import com.opethic.genivis.utils.DateValidator;
import com.opethic.genivis.utils.GlobalController;
import com.opethic.genivis.utils.Globals;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URL;
import java.security.Key;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.opethic.genivis.utils.FxmFileConstants.*;

public class PurchaseRegisterController implements Initializable {

    @FXML
    private  BorderPane bpPurRegisterRootPane;
    @FXML
    private ComboBox <String> cmbPurRegSuplInv,cmbPurRegDuration;
    @FXML
    private TextField tfPurRegListSearch,tfPurRegListFromDate,tfPurRegListToDate,tfFromAmt,tfToAmt;
    @FXML
    private Button btReset;
    @FXML
    private TableView<PurchaseRegisterDTO> tblPurRegList;
    @FXML
    private TableColumn<PurchaseRegisterDTO, String> tblPurRegDate,tblPurRegLedger,tblPurRegVoucherType,tblPurRegVoucherNo,tblPurRegDebitAmt,tblPurRegCreditAmt;
    @FXML
    private Label lblTotalDebit,lblTotalCredit;

    String duration = "";
    String selectedDuration = "", selectedFilter = "",selectedType = "";

    private ObservableList<PurchaseRegisterDTO> originalData;
    Map<String, String> body = new HashMap<>(); // this used for mapping the data to call getPurchaseRegisterData() as per requirement  sending the Formdata
    String message = "";
    private JsonObject jsonObject = null;

    public static final Logger purchaseRegisterLogger = LoggerFactory.getLogger(PurchaseRegisterController.class);



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        // Initial Static Values Set Supplier/Voucher  Combobox
        cmbPurRegSuplInv.getItems().addAll("Supplier", "Voucher");
        cmbPurRegSuplInv.setPromptText("Filter Search");
        cmbPurRegSuplInv.setOnAction(this::handleFilterComboBoxAction);
        cmbPurRegSuplInv.setValue("Supplier");
        handleFilterComboBoxAction(null);

        // Initial Static Values Set Duration  Combobox
        cmbPurRegDuration.getItems().addAll("This Month", "Last Month", "Half Year", "Full Year");
        cmbPurRegDuration.setPromptText("Select Duration");
        cmbPurRegDuration.setOnAction(this::handleDurationComboBoxAction);
        cmbPurRegDuration.setValue(cmbPurRegDuration.getItems().get(0));
        duration = "month";



        // Purchase Register List API Call with Mapping body here and Calling the getPurchaseRegisterData();
        body.put("duration",duration);
        getPurchaseRegisterData();

        // Purchase Register List API Call with Mapping body with Dates here and Calling the getPurchaseRegisterData();
        tfPurRegListToDate.setOnKeyPressed(e->{
            if (e.getCode() == KeyCode.ENTER) {
              body.clear();

                if (tfPurRegListFromDate != null && tfPurRegListFromDate.getText() != null && !tfPurRegListFromDate.getText().isEmpty()) {
                    body.put("start_date", Communicator.text_to_date.fromString(tfPurRegListFromDate.getText()).toString());

                }
                if (tfPurRegListToDate != null && tfPurRegListToDate.getText() != null && !tfPurRegListToDate.getText().isEmpty()) {
                    body.put("end_date", Communicator.text_to_date.fromString(tfPurRegListToDate.getText()).toString());
                }
                getPurchaseRegisterData();
                tfPurRegListSearch.setText("");

            }
        });

        // Amountwise Filter after changing amount in the ToAMount field
        tfToAmt.textProperty().addListener((observable, oldValue, newValue) -> {
            filterAmountData();
        });

        btReset.setOnAction(e->{
            resetFields();
        });

        // TextFields Converting in DateField
        DateValidator.applyDateFormat(tfPurRegListFromDate);
        DateValidator.applyDateFormat(tfPurRegListToDate);

    }

    private void handleFilterComboBoxAction(ActionEvent event) {
        selectedFilter = cmbPurRegSuplInv.getSelectionModel().getSelectedItem();
        if (cmbPurRegSuplInv.getSelectionModel().getSelectedItem() == "Supplier") {
            selectedFilter = "supplier";
        } else if (cmbPurRegSuplInv.getSelectionModel().getSelectedItem() == "Voucher No") {
            selectedFilter = "voucher";
        }
        getPurRegDataByFilter();
    }

    private void getPurRegDataByFilter() {
        ObservableList<PurchaseRegisterDTO> filteredData = FXCollections.observableArrayList();

        if (selectedFilter.equalsIgnoreCase("supplier")) {
            tfPurRegListSearch.textProperty().addListener((observable, oldValue, newValue) -> {
                filterData(newValue.trim(), PurchaseRegisterDTO::getLedger);
            });
            calculateTotalAmount();
        } else if (selectedFilter.equalsIgnoreCase("voucher")) {
            tfPurRegListSearch.textProperty().addListener((observable, oldValue, newValue) -> {
                filterData(newValue.trim(), PurchaseRegisterDTO::getVoucherNo);
            });
            calculateTotalAmount();
        }
    }

    private void filterData(String keyword, Function<PurchaseRegisterDTO, String> fieldExtractor) {
        ObservableList<PurchaseRegisterDTO> filteredData = FXCollections.observableArrayList();
        purchaseRegisterLogger.error("Search Purchase Register List in PurchaseRegisterController");
        if (keyword.isEmpty()) {
            tblPurRegList.setItems(originalData);
            calculateTotalAmount();
            return;
        }
        for (PurchaseRegisterDTO item : originalData) {
            if (matchesKeyword(item, keyword, fieldExtractor)) {
                filteredData.add(item);
            }
        }
        tblPurRegList.setItems(filteredData);
        calculateTotalAmount();
    }

    private boolean matchesKeyword(PurchaseRegisterDTO item, String keyword, Function<PurchaseRegisterDTO, String> fieldExtractor) {
        String lowerCaseKeyword = keyword.toLowerCase();
        String fieldValue = fieldExtractor.apply(item).toLowerCase();
        return fieldValue.contains(lowerCaseKeyword);
    }

    private void handleDurationComboBoxAction(Event event) {
        selectedDuration = cmbPurRegDuration.getSelectionModel().getSelectedItem();
        if (selectedDuration == "This Month") {
            duration = "month";
        } else if (selectedDuration == "Last Month") {
            duration = "lastMonth";
        } else if (selectedDuration == "Half Year") {
            duration = "halfYear";
        } else if (selectedDuration == "Full Year") {
            duration = "year";
            GlobalController.getInstance().addTabStatic(PURCHASE_REGISTER_YEARLY_SLUG, false);
        }
        getPurchaseRegisterData();

    }


    private void getPurchaseRegisterData() {
        APIClient apiClient = null;
        purchaseRegisterLogger.debug("Get getPurchaseRegisterData Started...");
        try {
            String formData = Globals.mapToStringforFormData(body);
            apiClient = new APIClient(EndPoints.getPurchaseRegisterData, formData, RequestType.FORM_DATA);
            apiClient.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
                @Override
                public void handle(WorkerStateEvent workerStateEvent) {
                    jsonObject = new Gson().fromJson(workerStateEvent.getSource().getValue().toString(), JsonObject.class);
                    ObservableList<PurchaseRegisterDTO> observableList = FXCollections.observableArrayList();
                    if (jsonObject.get("responseStatus").getAsInt() == 200) {
                        purchaseRegisterLogger.debug("Get getPurchaseRegisterData Success...");
                        JsonArray responseArray = jsonObject.get("debit").getAsJsonArray();
                        String st_dt = (jsonObject.get("d_start_date").getAsString());
                        LocalDate start_date = LocalDate.parse(st_dt);
                        String en_dt = jsonObject.get("d_end_date").getAsString();
                        LocalDate end_date = LocalDate.parse(en_dt);
                        tfPurRegListFromDate.setText(start_date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
                        tfPurRegListToDate.setText(end_date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));

                        if (responseArray.size() > 0) {
                            for (JsonElement element : responseArray) {
                                JsonObject item = element.getAsJsonObject();
                                String id = item.get("row_id").getAsString();
                                String date = item.get("transaction_date").getAsString();
                                String ledger = item.get("particulars").getAsString();
                                String voucherType = item.get("voucher_type").getAsString();
                                String voucherNo = item.get("voucher_no").getAsString();
                                String debit = item.get("debit").getAsString();
                                String credit = item.get("credit").getAsString();
                                observableList.add(new PurchaseRegisterDTO(id, date, ledger, voucherType, voucherNo, debit,credit));
                            }

                            tblPurRegDate.setCellValueFactory(new PropertyValueFactory<>("date"));
                            tblPurRegLedger.setCellValueFactory(new PropertyValueFactory<>("ledger"));
                            tblPurRegVoucherType.setCellValueFactory(new PropertyValueFactory<>("voucherType"));
                            tblPurRegVoucherNo.setCellValueFactory(new PropertyValueFactory<>("voucherNo"));
                            tblPurRegDebitAmt.setCellValueFactory(new PropertyValueFactory<>("debit"));
                            tblPurRegCreditAmt.setCellValueFactory(new PropertyValueFactory<>("credit"));
                            tblPurRegList.setItems(observableList);
                            originalData = observableList;
                            calculateTotalAmount();
                        } else {
                            purchaseRegisterLogger.debug("Get getAllPaymentsList responseObject is null...");
                            tblPurRegList.getItems().clear();
                        }
//                        setCreditNoteFormData(creditNoteByIdMainDTO);
                    }
                }
            });

            apiClient.setOnFailed(new EventHandler<WorkerStateEvent>() {
                @Override
                public void handle(WorkerStateEvent workerStateEvent) {
                    purchaseRegisterLogger.error("Network API cancelled in getPurchaseRegisterData()" + workerStateEvent.getSource().getValue().toString());
                }
            });
            apiClient.setOnCancelled(new EventHandler<WorkerStateEvent>() {
                @Override
                public void handle(WorkerStateEvent workerStateEvent) {
                    purchaseRegisterLogger.error("Network API cancelled in getPurchaseRegisterData()" + workerStateEvent.getSource().getValue().toString());
                }
            });
            apiClient.start();
            purchaseRegisterLogger.debug("Get getPurchaseRegisterData End...");

        }catch (Exception e){
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            String exceptionAsString = sw.toString();
            purchaseRegisterLogger.error("Exception in getPurchaseRegisterData():" + exceptionAsString);
        } finally {
            apiClient = null;
        }
    }

    private void filterAmountData() {
        purchaseRegisterLogger.debug("Get filterAmountData Started...");
        String fromAmountText = tfFromAmt.getText();
        String toAmountText = tfToAmt.getText();
        if (toAmountText.isEmpty()) {
            tblPurRegList.setItems(FXCollections.observableList(originalData));
            calculateTotalAmount();
            return;
        }

        double fromAmount;
        double toAmount;
        String message = "Please Enter a Valid Amount Range.";
        String message1 = "Please Enter Valid Numeric Amounts.";
        Stage stage2 = (Stage) bpPurRegisterRootPane.getScene().getWindow();
        AlertUtility.CustomCallback callback = (number) -> {
            if (number == 1) {
            }
        };
        try {
            fromAmount = Double.parseDouble(fromAmountText);
            toAmount = Double.parseDouble(toAmountText);
        } catch (NumberFormatException e) {
            purchaseRegisterLogger.error("Get filterAmountData NumberFormatException...");
            AlertUtility.AlertError(stage2, AlertUtility.alertTypeError, message1, callback);
            return;
        }


//        if (fromAmount > toAmount) {
//            tfToAmt.setOnKeyPressed(e->{
//                if(e.getCode() == KeyCode.ENTER){
//                    if(fromAmount > toAmount){
//                        purchaseRegisterLogger.error("Get filterAmountData fromAmount > toAmount...");
//                        AlertUtility.AlertError(stage2, AlertUtility.alertTypeError, message, callback);
//                    }
//                }
//            });
//            return;
//        }

        List<PurchaseRegisterDTO> filteredData = new ArrayList<>();
        for (PurchaseRegisterDTO purRegData : originalData) {
            Double dobCred = Double.parseDouble(purRegData.getCredit());
            Double dobDeb = Double.parseDouble(purRegData.getDebit());
            if ((dobCred >= fromAmount && dobCred <= toAmount) || (dobDeb >= fromAmount && dobDeb <= toAmount)  ) {
                purchaseRegisterLogger.debug("Get filterAmountData SuccessFully Executed...");
                filteredData.add(purRegData);
//                tblPurRegList.setItems(purRegData);
            }

        }

        tblPurRegList.setItems(FXCollections.observableList(filteredData));
        calculateTotalAmount();
        purchaseRegisterLogger.debug("Get filterAmountData End...");
    }

    private void calculateTotalAmount(){
        double totalCredit = 0.0;
        double totalDebit = 0.0;
        for (PurchaseRegisterDTO credit : tblPurRegList.getItems()) {
            System.out.println("credit AMounts : >>"+ credit.getCredit());
            Double creditAmt = Double.parseDouble(credit.getCredit());
            totalCredit += creditAmt;
            Double debitAmt = Double.parseDouble(credit.getDebit());
            totalDebit += debitAmt;
        }
        System.out.println("TotalAMount >> : "+ totalCredit);
        System.out.println("TotalAMount >> : "+ totalDebit);

        lblTotalCredit.setText(String.valueOf(totalCredit));
        lblTotalDebit.setText(String.valueOf(totalDebit));
//        return totalCredit;
    }

    private void resetFields(){
        body.clear();
        tfPurRegListSearch.setText("");
        tfFromAmt.setText("");
        tfToAmt.setText("");
        body.put("duration",duration);
        getPurchaseRegisterData();
    }

}

