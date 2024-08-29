package com.opethic.genivis.controller.Reports.Transactions;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.opethic.genivis.controller.tranx_sales.SalesOrderListController;
import com.opethic.genivis.dto.GSTR1.GSTR1NilRatedDTO;
import com.opethic.genivis.dto.Reports.JournalReportScreen2DTO;
import com.opethic.genivis.dto.Reports.PaymentReportScreen2DTO;
import com.opethic.genivis.dto.reqres.product.Communicator;
import com.opethic.genivis.network.APIClient;
import com.opethic.genivis.network.EndPoints;
import com.opethic.genivis.network.RequestType;
import com.opethic.genivis.utils.AlertUtility;
import com.opethic.genivis.utils.DateValidator;
import com.opethic.genivis.utils.GlobalController;
import com.opethic.genivis.utils.Globals;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
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

import static com.opethic.genivis.utils.FxmFileConstants.GSTR1_DASHBOARD_LIST_SLUG;
import static com.opethic.genivis.utils.FxmFileConstants.JOURNAL_REPORT_SCREEN1_SLUG;

public class JournalReportScreen2Controller implements Initializable {

    @FXML
    private TableView<JournalReportScreen2DTO> tvJournalReportScreen2;
    @FXML
    TableColumn<JournalReportScreen2DTO, String> tcJournalScreen2Particulars, tcJournalScreen2Transactions, tcJournalScreen2NoOfVouchers, tcJournalScreen2PaymentAmt;
    @FXML
    private TextField tfJournalReportScreen2LedgerSearch, dpJournalReportScreen2FromDate, dpJournalReportScreen2ToDate;
    @FXML
    private Label labelJournalReportScreen2NoOfVoucher, labelJournalReportScreen2PaymentAmt,labelJournalReportScreen2CompanyName;
    @FXML
    private Button btnJournalReportScreen2pdf, btnJournalReportScreen2excel, btnJournalReportScreen2csv, btnJournalReportScreen2print;

    private Node[] focusableNodes;

    private ObservableList<JournalReportScreen2DTO> originalData;
    // Assuming this format is compatible with your API
    DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    @FXML
    private BorderPane bpJournalReportScreen2;
    private LocalDate fromDate, toDate;
    private String responseBody, message;

    private JsonObject jsonObject = null;

    private static final Logger logger = LogManager.getLogger(JournalReportScreen2Controller.class);
    ObservableList<JournalReportScreen2DTO> journalReportScreen2ObservableList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //Set the TableView Columns with proper height and Width
        tvJournalReportScreen2.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        journalReportScreen2TableDesign();
        Platform.runLater(() -> {
            tfJournalReportScreen2LedgerSearch.requestFocus();
        });
        DateValidator.applyDateFormat(dpJournalReportScreen2FromDate);
        DateValidator.applyDateFormat(dpJournalReportScreen2ToDate);
        sceneInitilization();
        dpJournalReportScreen2ToDate.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                fromDate = null;
                toDate = null;
                if (!dpJournalReportScreen2FromDate.getText().isEmpty()) {
                    fromDate = Communicator.text_to_date.fromString(dpJournalReportScreen2FromDate.getText());
                }
                if (!dpJournalReportScreen2ToDate.getText().isEmpty()) {
                    toDate = Communicator.text_to_date.fromString(dpJournalReportScreen2ToDate.getText());
                }
                getJournalScreen2Data("");
                tfJournalReportScreen2LedgerSearch.requestFocus();
            }
        });

        bpJournalReportScreen2.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ESCAPE) {
//                System.out.println("Esc Clicked....!");
                GlobalController.getInstance().addTabStatic(JOURNAL_REPORT_SCREEN1_SLUG, false);
            }
        });
        getJournalScreen2Data("");

        //Search without API Call in the Table
                originalData = tvJournalReportScreen2.getItems();
        tfJournalReportScreen2LedgerSearch.textProperty().addListener((observable, oldValue, newValue) -> {
            filterData(newValue.trim());
        });
    }
    public void sceneInitilization() {
        bpJournalReportScreen2.sceneProperty().addListener((observable, oldScene, newScene) -> {
            if (newScene != null && newScene.getWindow() instanceof Stage) {
                Communicator.stage = (Stage) newScene.getWindow();
            }
        });
    }
    //Design of table to display the data
    public void journalReportScreen2TableDesign() {
        //todo: Responsive code for bottom productInfo tableview
        tcJournalScreen2Particulars.prefWidthProperty().bind(tvJournalReportScreen2.widthProperty().multiply(0.80));
        tcJournalScreen2NoOfVouchers.prefWidthProperty().bind(tvJournalReportScreen2.widthProperty().multiply(0.10));
        tcJournalScreen2PaymentAmt.prefWidthProperty().bind(tvJournalReportScreen2.widthProperty().multiply(0.10));
    }

    //Get and Set the  List Data of GSTR1 Nil Rated
    public void getJournalScreen2Data(String searchKey) {
        APIClient apiClient = null;
//        SalesQuotationListLogger.info(" fetch Sales Quotation invoice List : SalesQuotationListController");
        try {
            journalReportScreen2ObservableList.clear();
            logger.debug("Get GSTR1 Nil Rated Data Started...");
            // Format dates
            String startDate = fromDate != null ? fromDate.format(dateFormatter) : "";
            String endDate = toDate != null ? toDate.format(dateFormatter) : "";
            System.out.println("Start Date: " + startDate);
            System.out.println("End Date: " + endDate);
            Map<String, String> body = new HashMap<>();
//            body.put("pageNo", "1");
//            body.put("pageSize", "50");
//            body.put("searchText", "");
//            body.put("sort", "");
            if (startDate != "" || endDate != "") {
                body.put("start_date", startDate);
                body.put("end_date", endDate);
            }

            String requestBody = Globals.mapToStringforFormData(body);
            System.out.println("i am in" + requestBody);
//            HttpResponse<String> response = APIClient.postJsonRequest(requestBody, EndPoints.GET_GSTR1_NIL_RATED_ENDPOINT);
            apiClient = new APIClient(EndPoints.GET_JOURNAL_REPORT2_DETAILS, requestBody, RequestType.FORM_DATA);
            apiClient.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
                @Override
                public void handle(WorkerStateEvent workerStateEvent) {
                    jsonObject = new Gson().fromJson(workerStateEvent.getSource().getValue().toString(), JsonObject.class);

//                    System.out.println("SalesOrder" + response.body());
//                    JsonObject jsonObject = new Gson().fromJson(response.body(), JsonObject.class);
                    System.out.println("this is Response of JOURNAL PAGE 2" + jsonObject);
                    journalReportScreen2ObservableList = FXCollections.observableArrayList();
                    if (jsonObject.get("responseStatus").getAsInt() == 200) {
                        LocalDate start_date = LocalDate.parse(jsonObject.get("d_start_date").getAsString());
                        LocalDate end_date = LocalDate.parse(jsonObject.get("d_end_date").getAsString());
                        dpJournalReportScreen2FromDate.setText(start_date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
                        dpJournalReportScreen2ToDate.setText(end_date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
                        labelJournalReportScreen2CompanyName.setText(jsonObject.get("company_name").getAsString());

//                        JsonObject responseObject = jsonObject.get("data").getAsJsonObject();
                        JsonArray responseArray = jsonObject.get("response").getAsJsonArray();

                        if (responseArray.size() > 0) {
                            tvJournalReportScreen2.setVisible(true);
                            // Variables to store total amounts
//                            double totalExempted = 0.0;
//                            double totalNilRated = 0.0;
                            for (JsonElement element : responseArray) {
                                JsonObject item = element.getAsJsonObject();
                                System.out.println("item in nil rated list " + item);
                                String month = item.get("month") != null ? item.get("month").getAsString() : "";
                                String noOfVouchers = item.get("no_vouchers") != null ? item.get("no_vouchers").getAsString() : "";
                                String endDate = item.get("end_date") != null ? item.get("end_date").getAsString() : "";
                                String totalAmt = item.get("total_amt") != null ? item.get("total_amt").getAsString() : "";
                                String startDate = item.get("start_date") != null ? item.get("start_date").getAsString() : "";
                                String company_name = item.get("company_name") != null ? item.get("company_name").getAsString() : "";

                                journalReportScreen2ObservableList.add(new JournalReportScreen2DTO(month, noOfVouchers, endDate, totalAmt, startDate, company_name));

                            }
                            tcJournalScreen2Particulars.setCellValueFactory(new PropertyValueFactory<>("month"));
                            tcJournalScreen2NoOfVouchers.setCellValueFactory(new PropertyValueFactory<>("no_vouchers"));
                            tcJournalScreen2PaymentAmt.setCellValueFactory(new PropertyValueFactory<>("total_amt"));

                            // Update originalData with new items

                            tvJournalReportScreen2.setItems(journalReportScreen2ObservableList);
//                            originalData.setAll(journalReportScreen2ObservableList);
                            originalData = journalReportScreen2ObservableList;
                            calculateTotalAmount(originalData);

                        } else {
//                    SalesQuotationListLogger.debug("Sales Quotation invoice List ResponseObject is null : SalesQuotationListController");
                            //TODO : use logger for alert messages
                            AlertUtility.CustomCallback callback = (number) -> {
                                if (number == 0) {
                                    dpJournalReportScreen2ToDate.requestFocus();
                                }
                            };
                            Stage stage = (Stage) bpJournalReportScreen2.getScene().getWindow();
                            AlertUtility.AlertError(stage, AlertUtility.alertTypeError, "Failed to Load Data ", callback);
                            journalReportScreen2ObservableList.clear();
                            calculateTotalAmount(originalData);

                        }
                    } else {
//                SalesQuotationListLogger.debug("Error in response of Sales Quotation invoice List : SalesQuotationListController");
                        //TODO : use logger for alert messages
                        AlertUtility.CustomCallback callback = (number) -> {
                            if (number == 0) {
                                dpJournalReportScreen2ToDate.requestFocus();
                            }
                        };
                        Stage stage = (Stage) bpJournalReportScreen2.getScene().getWindow();
                        AlertUtility.AlertError(stage, AlertUtility.alertTypeError, "Falied to connect server! ", callback);
                        journalReportScreen2ObservableList.clear();
                        calculateTotalAmount(originalData);
                    }
                }
            });

            apiClient.setOnCancelled(new EventHandler<WorkerStateEvent>() {
                @Override
                public void handle(WorkerStateEvent workerStateEvent) {
                    logger.error("Network API cancelled in getGSTR1NilRatedData()" + workerStateEvent.getSource().getValue().toString());

                }
            });

            apiClient.setOnFailed(new EventHandler<WorkerStateEvent>() {
                @Override
                public void handle(WorkerStateEvent workerStateEvent) {
                    logger.error("Network API failed in getGSTR1NilRatedData()" + workerStateEvent.getSource().getValue().toString());
                }
            });
            apiClient.start();
            logger.debug("Get GSTR1 Nil Rated Data End...");
        } catch (Exception e) {
            //TODO : use logger for alert messages
            AlertUtility.CustomCallback callback = (number) -> {
                if (number == 0) {
                    dpJournalReportScreen2ToDate.requestFocus();
                }
            };
            Stage stage = (Stage) bpJournalReportScreen2.getScene().getWindow();
            AlertUtility.AlertError(stage, AlertUtility.alertTypeError, "Falied to connect server! ", callback);

            e.printStackTrace();
        } finally {
            apiClient = null;
        }


    }

    private void filterData(String keyword) {
        ObservableList<JournalReportScreen2DTO> filteredData = FXCollections.observableArrayList();

        if (keyword.isEmpty()) {
            tvJournalReportScreen2.setItems(originalData);
            calculateTotalAmount(originalData); // Calculate totals for all records
            return;
        }

        for (JournalReportScreen2DTO item : originalData) {
            if (matchesKeyword(item, keyword)) {
                filteredData.add(item);
            }
        }

        tvJournalReportScreen2.setItems(filteredData);
        calculateTotalAmount(filteredData); // Calculate totals for filtered records
    }

    private void calculateTotalAmount(ObservableList<JournalReportScreen2DTO> data)
    {
        // Calculate the Totals
        double totalPaymentAmt = 0.0;
        double totalVoucherNo = 0.0;
        for (JournalReportScreen2DTO item : data) {
            // Update total amounts
                totalVoucherNo += Double.parseDouble(item.getNo_vouchers().isEmpty() ? "0.0" : item.getNo_vouchers());
                totalPaymentAmt += Double.parseDouble(item.getTotal_amt().isEmpty() ? "0.0" : item.getTotal_amt());
        }

        // Update Labels in the FXMLlabelJournalReportScreen2NoOfVoucher
        labelJournalReportScreen2PaymentAmt.setText(String.format("%.2f", totalPaymentAmt));
        labelJournalReportScreen2NoOfVoucher.setText(String.format("%.2f", totalVoucherNo));
    }
    //todo:Search Function to Search in the Table for columns
    private boolean matchesKeyword(JournalReportScreen2DTO item, String keyword) {
        String lowerCaseKeyword = keyword.toLowerCase();

        // todo:Check if any of the columns contain the keyword
        return item.getMonth().toLowerCase().contains(lowerCaseKeyword) || item.getNo_vouchers().toLowerCase().contains(lowerCaseKeyword) || item.getTotal_amt().toLowerCase().contains(lowerCaseKeyword);
    }
}