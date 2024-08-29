package com.opethic.genivis.controller.Reports.Transactions;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.opethic.genivis.controller.tranx_sales.SalesOrderListController;
import com.opethic.genivis.dto.GSTR1.GSTR1NilRatedDTO;
import com.opethic.genivis.dto.Reports.JournalReportScreen1DTO;
import com.opethic.genivis.dto.Reports.PaymentReportScreen1DTO;
import com.opethic.genivis.dto.reqres.product.Communicator;
import com.opethic.genivis.network.APIClient;
import com.opethic.genivis.network.EndPoints;
import com.opethic.genivis.network.RequestType;
import com.opethic.genivis.utils.*;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.opethic.genivis.utils.FxmFileConstants.*;

public class JournalReportScreen1Controller implements Initializable {

    @FXML
    private TableView<JournalReportScreen1DTO> tvJournalReportScreen1;
    @FXML
    TableColumn<JournalReportScreen1DTO, String> tcJournalReportScreen1Date, tcJournalReportScreen1Particulars, tcJournalReportScreen1VoucherType, tcJournalReportScreen1VoucherNo, tcJournalReportScreen1DebitAmt,tcJournalReportScreen1CreditAmt;
    @FXML
    private TextField tfJournalReportScreen1LedgerSearch, dpJournalReportScreen1FromDate, dpJournalReportScreen1ToDate,tfJournalReportScreen1AmtFrom,tfJournalReportScreen1AmtTo;
    @FXML
    private Label labelJournalReportScreen1Debit, labelJournalReportScreen1Credit,labelJournalReportScreen1CompanyName;
    @FXML
    private ComboBox<String> cmbJournalReportScreen1Filter, cmbJournalReportScreen1Duration;
    @FXML
    private Button btnJournalReportScreen1Reset,btnJournalReportScreen1pdf,btnJournalReportScreen1excel,btnJournalReportScreen1csv,btnJournalReportScreen1print;

    private Node[] focusableNodes;

    private ObservableList<JournalReportScreen1DTO> originalData;
    // Assuming this format is compatible with your API
    DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    @FXML
    private BorderPane bpRootJournalReportScreen1Pane;
    private LocalDate fromDate, toDate;
    private String responseBody, message;

    private JsonObject jsonObject = null;

    private static final Logger logger = LogManager.getLogger(JournalReportScreen1Controller.class);
    ObservableList<JournalReportScreen1DTO> journalReportScreen1ObservableList = FXCollections.observableArrayList();

    private String startDate, endDate, companyName,selectedFilter, selectedDuration,duration="month";


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //Set the TableView Columns with proper height and Width
        tvJournalReportScreen1.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        journalReportScreen1TableDesign();

        Platform.runLater(() -> cmbJournalReportScreen1Filter.requestFocus());
        //Populate focusable nodes array with the desired order and Focus To next and Previous Element
        focusableNodes = new Node[]{cmbJournalReportScreen1Filter, tfJournalReportScreen1LedgerSearch, cmbJournalReportScreen1Duration, dpJournalReportScreen1FromDate, dpJournalReportScreen1ToDate,tfJournalReportScreen1AmtFrom,tfJournalReportScreen1AmtTo,btnJournalReportScreen1Reset,btnJournalReportScreen1pdf,btnJournalReportScreen1excel,btnJournalReportScreen1csv,btnJournalReportScreen1print,tvJournalReportScreen1};
        CommonValidationsUtils.setupFocusNavigation(focusableNodes);



        DateValidator.applyDateFormat(dpJournalReportScreen1FromDate);
        DateValidator.applyDateFormat(dpJournalReportScreen1ToDate);
        sceneInitilization();

        getJournalReportScreen1Data();

        cmbJournalReportScreen1Duration.getItems().addAll("This Month", "Last Month", "Half Year", "Full Year");
        cmbJournalReportScreen1Duration.setPromptText("Select Duration");
        cmbJournalReportScreen1Duration.setOnAction(this::handleDurationComboBox);
        cmbJournalReportScreen1Duration.setValue(cmbJournalReportScreen1Duration.getItems().get(0));

        cmbJournalReportScreen1Filter.getItems().addAll("Supplier", "Invoice");
        cmbJournalReportScreen1Filter.setPromptText("Filter Search");
        cmbJournalReportScreen1Filter.setOnAction(this::handleFilterComboBoxAction);
        cmbJournalReportScreen1Filter.setValue("Supplier");
        handleFilterComboBoxAction(null);
        //Clear Fields on Reset Button Click
        btnJournalReportScreen1Reset.setOnAction(e -> {
            resetAllFields();



        });
        tfJournalReportScreen1AmtTo.addEventFilter(KeyEvent.ANY, (KeyEvent event) -> {
            if (event.getCode() == KeyCode.TAB || event.getCode() == KeyCode.ENTER) {
                if (!tfJournalReportScreen1AmtFrom.getText().isEmpty() && !tfJournalReportScreen1AmtTo.getText().isEmpty()) {
                    filterPaymentListByAmount();
//                    nodetraversal(tfPaymentReportToDate,tfJournalReportScreen1AmtFrom);
                } else {
//                    filterLedgerListByFilter();
                }

            }
        });
        dpJournalReportScreen1ToDate.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                fromDate = null;
                toDate = null;
                if (!dpJournalReportScreen1FromDate.getText().isEmpty()) {
                    fromDate = Communicator.text_to_date.fromString(dpJournalReportScreen1FromDate.getText());
                }
                if (!dpJournalReportScreen1ToDate.getText().isEmpty()) {
                    toDate = Communicator.text_to_date.fromString(dpJournalReportScreen1ToDate.getText());
                }
                getJournalReportScreen1DataWithDate();
                tfJournalReportScreen1LedgerSearch.requestFocus();
            }
        });
    }
    public void sceneInitilization() {
        bpRootJournalReportScreen1Pane.sceneProperty().addListener((observable, oldScene, newScene) -> {
            if (newScene != null && newScene.getWindow() instanceof Stage) {
                Communicator.stage = (Stage) newScene.getWindow();
            }
        });
    }
    //Design of table to display the data
    public void journalReportScreen1TableDesign() {
        //todo: Responsive code for bottom productInfo tableview
        tcJournalReportScreen1Date.prefWidthProperty().bind(tvJournalReportScreen1.widthProperty().multiply(0.10));
        tcJournalReportScreen1Particulars.prefWidthProperty().bind(tvJournalReportScreen1.widthProperty().multiply(0.5));
        tcJournalReportScreen1VoucherType.prefWidthProperty().bind(tvJournalReportScreen1.widthProperty().multiply(0.10));
        tcJournalReportScreen1VoucherNo.prefWidthProperty().bind(tvJournalReportScreen1.widthProperty().multiply(0.10));
        tcJournalReportScreen1DebitAmt.prefWidthProperty().bind(tvJournalReportScreen1.widthProperty().multiply(0.10));
        tcJournalReportScreen1CreditAmt.prefWidthProperty().bind(tvJournalReportScreen1.widthProperty().multiply(0.10));
    }

    public void resetAllFields(){
//        cmbJournalReportScreen1Filter.getItems().clear();
        tfJournalReportScreen1LedgerSearch.setText("");
//        cmbJournalReportScreen1Duration.getItems().clear();
        dpJournalReportScreen1FromDate.setText("");
        dpJournalReportScreen1ToDate.setText("");
        tfJournalReportScreen1AmtFrom.setText("");
        tfJournalReportScreen1AmtTo.setText("");
        getJournalReportScreen1Data();
    }

    //Get and Set the  List Data of GSTR1 Nil Rated
    public void getJournalReportScreen1Data() {
        APIClient apiClient = null;
//        SalesQuotationListLogger.info(" fetch Sales Quotation invoice List : SalesQuotationListController");
        try {
            journalReportScreen1ObservableList.clear();
            logger.debug("Get GSTR1 Nil Rated Data Started...");
            System.out.println("selectedDuration --->>> "+duration);
            // Format dates
            String startDate = fromDate != null ? fromDate.format(dateFormatter) : "";
            String endDate = toDate != null ? toDate.format(dateFormatter) : "";
            System.out.println("Start Date: " + startDate);
            System.out.println("End Date: " + endDate);
            Map<String, String> body = new HashMap<>();
            body.put("duration",duration);
            if (startDate != "" || endDate != "") {
                body.put("start_date", startDate);
                body.put("end_date", endDate);
            }

            String requestBody = Globals.mapToStringforFormData(body);
            System.out.println("i am in NEw Journal Report" + requestBody);
//            HttpResponse<String> response = APIClient.postJsonRequest(requestBody, EndPoints.GET_GSTR1_NIL_RATED_ENDPOINT);
            apiClient = new APIClient(EndPoints.GET_JOURNAL_REPORT1_DETAILS, requestBody, RequestType.FORM_DATA);
            apiClient.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
                @Override
                public void handle(WorkerStateEvent workerStateEvent) {
                    jsonObject = new Gson().fromJson(workerStateEvent.getSource().getValue().toString(), JsonObject.class);

//                    System.out.println("SalesOrder" + response.body());
//                    JsonObject jsonObject = new Gson().fromJson(response.body(), JsonObject.class);
                    System.out.println("this is Response of Nil Rated Report" + jsonObject);
                    journalReportScreen1ObservableList = FXCollections.observableArrayList();
                    if (jsonObject.get("responseStatus").getAsInt() == 200) {
                        LocalDate start_date = LocalDate.parse(jsonObject.get("d_start_date").getAsString());
                        LocalDate end_date = LocalDate.parse(jsonObject.get("d_end_date").getAsString());
                        dpJournalReportScreen1FromDate.setText(start_date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
                        dpJournalReportScreen1ToDate.setText(end_date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
                        labelJournalReportScreen1CompanyName.setText(jsonObject.get("company_name").getAsString());
//                        JsonObject responseObject = jsonObject.get("data").getAsJsonObject();
                        JsonArray responseArray = jsonObject.get("response").getAsJsonArray();

                        if (responseArray.size() > 0) {
                            tvJournalReportScreen1.setVisible(true);
                            // Variables to store total amounts
//                            double totalExempted = 0.0;
//                            double totalNilRated = 0.0;
                            for (JsonElement element : responseArray) {
                                JsonObject item = element.getAsJsonObject();
                                System.out.println("item in nil rated list " + item);
                                String row_id = item.get("row_id").getAsString();
                                String tranxDate = item.get("transaction_date").getAsString();
                                String voucherNo = item.get("voucher_no").getAsString();
                                String voucherId = item.get("voucher_id").getAsString();
                                String particulars = item.get("particulars").getAsString();
                                String voucherType = item.get("voucher_type").getAsString();
                                String crAmount = item.get("credit").getAsString();
                                String drAmount = item.get("debit").getAsString();

                                journalReportScreen1ObservableList.add(new JournalReportScreen1DTO(startDate,endDate, companyName, row_id, tranxDate, voucherNo, voucherId,particulars,voucherType,crAmount,drAmount));

                            }
                             tcJournalReportScreen1Date.setCellValueFactory(new PropertyValueFactory<>("transaction_date"));
                             tcJournalReportScreen1Particulars.setCellValueFactory(new PropertyValueFactory<>("particulars"));
                             tcJournalReportScreen1VoucherType.setCellValueFactory(new PropertyValueFactory<>("voucher_type"));
                             tcJournalReportScreen1VoucherNo.setCellValueFactory(new PropertyValueFactory<>("voucher_no"));
                             tcJournalReportScreen1DebitAmt.setCellValueFactory(new PropertyValueFactory<>("debit"));
                             tcJournalReportScreen1CreditAmt.setCellValueFactory(new PropertyValueFactory<>("credit"));
                              tvJournalReportScreen1.setItems(journalReportScreen1ObservableList);
                            originalData = journalReportScreen1ObservableList;
                            // Update labels with total amounts
                            calculateTotalAmount();
                            // Update originalData with new items

                            tvJournalReportScreen1.setItems(journalReportScreen1ObservableList);
//                            originalData.setAll(journalReportScreen1ObservableList);
                            originalData = journalReportScreen1ObservableList;

                        } else {
//                    SalesQuotationListLogger.debug("Sales Quotation invoice List ResponseObject is null : SalesQuotationListController");
                            //TODO : use logger for alert messages
                            AlertUtility.CustomCallback callback = (number) -> {
                                if (number == 0) {
                                    dpJournalReportScreen1ToDate.requestFocus();
                                }
                            };
                            Stage stage = (Stage) bpRootJournalReportScreen1Pane.getScene().getWindow();
                            AlertUtility.AlertError(stage, AlertUtility.alertTypeError, "Failed to Load Data ", callback);
                            journalReportScreen1ObservableList.clear();
//                            calculateTotalAmount(originalData);

                        }
                    } else {
//                SalesQuotationListLogger.debug("Error in response of Sales Quotation invoice List : SalesQuotationListController");
                        //TODO : use logger for alert messages
                        AlertUtility.CustomCallback callback = (number) -> {
                            if (number == 0) {
                                dpJournalReportScreen1ToDate.requestFocus();
                            }
                        };
//                        Stage stage = (Stage) bpRootJournalReportScreen1Pane.getScene().getWindow();
//                        AlertUtility.AlertError(stage, AlertUtility.alertTypeError, "Falied to connect server! ", callback);
                        journalReportScreen1ObservableList.clear();
//                        calculateTotalAmount(originalData);
                    }
                }
            });

            apiClient.setOnCancelled(new EventHandler<WorkerStateEvent>() {
                @Override
                public void handle(WorkerStateEvent workerStateEvent) {
                    logger.error("Network API cancelled in getJournalReportScreen1Data()" + workerStateEvent.getSource().getValue().toString());

                }
            });

            apiClient.setOnFailed(new EventHandler<WorkerStateEvent>() {
                @Override
                public void handle(WorkerStateEvent workerStateEvent) {
                    logger.error("Network API failed in getJournalReportScreen1Data()" + workerStateEvent.getSource().getValue().toString());
                }
            });
            apiClient.start();
            logger.debug("Get GSTR1 Nil Rated Data End...");
        } catch (Exception e) {
            //TODO : use logger for alert messages
            AlertUtility.CustomCallback callback = (number) -> {
                if (number == 0) {
                    dpJournalReportScreen1ToDate.requestFocus();
                }
            };
            Stage stage = (Stage) bpRootJournalReportScreen1Pane.getScene().getWindow();
            AlertUtility.AlertError(stage, AlertUtility.alertTypeError, "Falied to connect server! ", callback);

            e.printStackTrace();
        } finally {
            apiClient = null;
        }
    }

    //function for get the list by Filter
    private void handleFilterComboBoxAction(ActionEvent event) {
        selectedFilter = cmbJournalReportScreen1Filter.getSelectionModel().getSelectedItem();
//        System.out.println("Selected Filter: " + selectedFilter);
        if (cmbJournalReportScreen1Filter.getSelectionModel().getSelectedItem() == "Supplier") {
            selectedFilter = "supplier";
//            System.out.println("selectedType- " + selectedFilter);
        } else if (cmbJournalReportScreen1Filter.getSelectionModel().getSelectedItem() == "Invoice") {
            selectedFilter = "invoice";
//            System.out.println("selectedType- " + selectedFilter);
        }
        System.out.println("selectedFilterrr "+selectedFilter);
        fetchStockReport2ListByFilterSearch();
    }
    private void fetchStockReport2ListByFilterSearch() {
        ObservableList<JournalReportScreen1DTO> filteredData = FXCollections.observableArrayList();

        if (selectedFilter.equals("supplier")) {
            tfJournalReportScreen1LedgerSearch.textProperty().addListener((observable, oldValue, newValue) -> {
                filterData(newValue.trim(), JournalReportScreen1DTO::getParticulars);
            });
            calculateTotalAmount();
        } else if (selectedFilter.equals("invoice")) {
            tfJournalReportScreen1LedgerSearch.textProperty().addListener((observable, oldValue, newValue) -> {
                filterData(newValue.trim(), JournalReportScreen1DTO::getVoucher_no);
            });
            calculateTotalAmount();
        }
    }
    private void filterData(String keyword, Function<JournalReportScreen1DTO, String> fieldExtractor) {
        ObservableList<JournalReportScreen1DTO> filteredData = FXCollections.observableArrayList();
        logger.info("Search Stock Report 2 List in StocksStockReport2Controller");
        if (keyword.isEmpty()) {
            tvJournalReportScreen1.setItems(originalData);
            calculateTotalAmount();
            return;
        }
        for (JournalReportScreen1DTO item : originalData) {
            if (matchesKeyword(item, keyword, fieldExtractor)) {
                filteredData.add(item);
            }
        }
        tvJournalReportScreen1.setItems(filteredData);
        calculateTotalAmount();
    }
    private boolean matchesKeyword(JournalReportScreen1DTO item, String keyword, Function<JournalReportScreen1DTO, String> fieldExtractor) {
        String lowerCaseKeyword = keyword.toLowerCase();
        String fieldValue = fieldExtractor.apply(item).toLowerCase();
        return fieldValue.contains(lowerCaseKeyword);
    }
    //function for get the list by duration
    private void handleDurationComboBox(ActionEvent event){
        selectedDuration = cmbJournalReportScreen1Duration.getSelectionModel().getSelectedItem();
        if(selectedDuration == "This Month"){
            duration="month";
        }else if(selectedDuration == "Last Month"){
            duration = "lastMonth";
        }else if(selectedDuration == "Half Year"){
            duration = "halfYear";
        }else if(selectedDuration == "Full Year"){
            duration = "year";
            //to write the code of redirect to yearly page
            System.out.println("Next Page");
            GlobalController.getInstance().addTabStatic(JOURNAL_REPORT_SCREEN2_SLUG,false);
        }
        getJournalReportScreen1Data();
    }
    private void calculateTotalAmount(){
        ObservableList<JournalReportScreen1DTO> observableList = tvJournalReportScreen1.getItems();
        double totalDebit = 0.0;
        double totalCredit = 0.0;

        for(JournalReportScreen1DTO item:observableList){
            totalDebit += Double.parseDouble(item.getDebit().isEmpty()? "0.0" : item.getDebit());
            totalCredit += Double.parseDouble(item.getCredit().isEmpty() ? "0.0" : item.getCredit());
        }
        labelJournalReportScreen1Credit.setText(String.format("%.2f",totalCredit));
        labelJournalReportScreen1Debit.setText(String.format("%.2f",totalDebit));

    }
    //FUNCTION for filter the data by amount
    private void filterPaymentListByAmount() {
        ObservableList<JournalReportScreen1DTO> filteredData = FXCollections.observableArrayList();

        logger.info("Search Accounts Ledger Report 1 in AccountsLedgerReport1Controller");
        Double tfFromAmount = Double.parseDouble(tfJournalReportScreen1AmtFrom.getText());
        Double tfToAmount = Double.parseDouble(tfJournalReportScreen1AmtTo.getText());
        System.out.println();

//                filteredData = originalData.stream().filter((item -> Double.parseDouble(item.getDebit()) > 0 && Double.parseDouble(item.getDebit()) >= tfFromAmount && Double.parseDouble(item.getDebit()) <= tfToAmount )).collect(Collectors.toCollection(FXCollections::observableArrayList));

        filteredData = originalData.stream().filter((item -> ((Double.parseDouble(item.getDebit()) > 0 && Double.parseDouble(item.getDebit()) >= tfFromAmount && Double.parseDouble(item.getDebit()) <= tfToAmount) || (Double.parseDouble(item.getCredit()) > 0 && Double.parseDouble(item.getCredit()) >= tfFromAmount && Double.parseDouble(item.getCredit()) <= tfToAmount)) )).collect(Collectors.toCollection(FXCollections::observableArrayList));


        tvJournalReportScreen1.setItems(filteredData);
        calculateTotalAmount();
    }

    public void getJournalReportScreen1DataWithDate() {
        APIClient apiClient = null;
//        SalesQuotationListLogger.info(" fetch Sales Quotation invoice List : SalesQuotationListController");
        try {
            journalReportScreen1ObservableList.clear();
            logger.debug("Get GSTR1 Nil Rated Data Started...");
            // Format dates
            String startDate = fromDate != null ? fromDate.format(dateFormatter) : "";
            String endDate = toDate != null ? toDate.format(dateFormatter) : "";
            System.out.println("Start Date: " + startDate);
            System.out.println("End Date: " + endDate);
            Map<String, String> body = new HashMap<>();
            if (startDate != "" || endDate != "") {
                body.put("start_date", startDate);
                body.put("end_date", endDate);
            }
            String requestBody = Globals.mapToStringforFormData(body);
            System.out.println("i am in NEw Journal Report with Date" + requestBody);
//            HttpResponse<String> response = APIClient.postJsonRequest(requestBody, EndPoints.GET_GSTR1_NIL_RATED_ENDPOINT);
            apiClient = new APIClient(EndPoints.GET_JOURNAL_REPORT1_DETAILS, requestBody, RequestType.FORM_DATA);
            apiClient.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
                @Override
                public void handle(WorkerStateEvent workerStateEvent) {
                    jsonObject = new Gson().fromJson(workerStateEvent.getSource().getValue().toString(), JsonObject.class);

//                    System.out.println("SalesOrder" + response.body());
//                    JsonObject jsonObject = new Gson().fromJson(response.body(), JsonObject.class);
                    System.out.println("this is Response of Nil Rated Report" + jsonObject);
                    journalReportScreen1ObservableList = FXCollections.observableArrayList();
                    if (jsonObject.get("responseStatus").getAsInt() == 200) {
                        labelJournalReportScreen1CompanyName.setText(jsonObject.get("company_name").getAsString());
                        JsonArray responseArray = jsonObject.get("response").getAsJsonArray();

                        if (responseArray.size() > 0) {
                            tvJournalReportScreen1.setVisible(true);
                            // Variables to store total amounts
//                            double totalExempted = 0.0;
//                            double totalNilRated = 0.0;
                            for (JsonElement element : responseArray) {
                                JsonObject item = element.getAsJsonObject();
                                System.out.println("item in nil rated list " + item);
                                String row_id = item.get("row_id").getAsString();
                                String tranxDate = item.get("transaction_date").getAsString();
                                String voucherNo = item.get("voucher_no").getAsString();
                                String voucherId = item.get("voucher_id").getAsString();
                                String particulars = item.get("particulars").getAsString();
                                String voucherType = item.get("voucher_type").getAsString();
                                String crAmount = item.get("credit").getAsString();
                                String drAmount = item.get("debit").getAsString();

                                journalReportScreen1ObservableList.add(new JournalReportScreen1DTO(startDate,endDate, companyName, row_id, tranxDate, voucherNo, voucherId,particulars,voucherType,crAmount,drAmount));

                            }
                            tcJournalReportScreen1Date.setCellValueFactory(new PropertyValueFactory<>("transaction_date"));
                            tcJournalReportScreen1Particulars.setCellValueFactory(new PropertyValueFactory<>("particulars"));
                            tcJournalReportScreen1VoucherType.setCellValueFactory(new PropertyValueFactory<>("voucher_type"));
                            tcJournalReportScreen1VoucherNo.setCellValueFactory(new PropertyValueFactory<>("voucher_no"));
                            tcJournalReportScreen1DebitAmt.setCellValueFactory(new PropertyValueFactory<>("debit"));
                            tcJournalReportScreen1CreditAmt.setCellValueFactory(new PropertyValueFactory<>("credit"));
                            tvJournalReportScreen1.setItems(journalReportScreen1ObservableList);
                            originalData = journalReportScreen1ObservableList;
                            // Update labels with total amounts
                            calculateTotalAmount();
                            // Update originalData with new items

                            tvJournalReportScreen1.setItems(journalReportScreen1ObservableList);
//                            originalData.setAll(journalReportScreen1ObservableList);
                            originalData = journalReportScreen1ObservableList;
                            dpJournalReportScreen1FromDate.setText(startDate);
                            dpJournalReportScreen1ToDate.setText(endDate);

                        } else {
//                    SalesQuotationListLogger.debug("Sales Quotation invoice List ResponseObject is null : SalesQuotationListController");
                            //TODO : use logger for alert messages
                            AlertUtility.CustomCallback callback = (number) -> {
                                if (number == 0) {
                                    dpJournalReportScreen1ToDate.requestFocus();
                                }
                            };
                            Stage stage = (Stage) bpRootJournalReportScreen1Pane.getScene().getWindow();
                            AlertUtility.AlertError(stage, AlertUtility.alertTypeError, "Failed to Load Data ", callback);
                            journalReportScreen1ObservableList.clear();
//                            calculateTotalAmount(originalData);

                        }
                    } else {
//                SalesQuotationListLogger.debug("Error in response of Sales Quotation invoice List : SalesQuotationListController");
                        //TODO : use logger for alert messages
                        AlertUtility.CustomCallback callback = (number) -> {
                            if (number == 0) {
                                dpJournalReportScreen1ToDate.requestFocus();
                            }
                        };
//                        Stage stage = (Stage) bpRootJournalReportScreen1Pane.getScene().getWindow();
//                        AlertUtility.AlertError(stage, AlertUtility.alertTypeError, "Falied to connect server! ", callback);
                        journalReportScreen1ObservableList.clear();
//                        calculateTotalAmount(originalData);
                    }
                }
            });

            apiClient.setOnCancelled(new EventHandler<WorkerStateEvent>() {
                @Override
                public void handle(WorkerStateEvent workerStateEvent) {
                    logger.error("Network API cancelled in getJournalReportScreen1Data()" + workerStateEvent.getSource().getValue().toString());

                }
            });

            apiClient.setOnFailed(new EventHandler<WorkerStateEvent>() {
                @Override
                public void handle(WorkerStateEvent workerStateEvent) {
                    logger.error("Network API failed in getJournalReportScreen1Data()" + workerStateEvent.getSource().getValue().toString());
                }
            });
            apiClient.start();
            logger.debug("Get GSTR1 Nil Rated Data End...");
        } catch (Exception e) {
            //TODO : use logger for alert messages
            AlertUtility.CustomCallback callback = (number) -> {
                if (number == 0) {
                    dpJournalReportScreen1ToDate.requestFocus();
                }
            };
            Stage stage = (Stage) bpRootJournalReportScreen1Pane.getScene().getWindow();
            AlertUtility.AlertError(stage, AlertUtility.alertTypeError, "Falied to connect server! ", callback);

            e.printStackTrace();
        } finally {
            apiClient = null;
        }
    }
}
