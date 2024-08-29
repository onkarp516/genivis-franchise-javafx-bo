package com.opethic.genivis.controller.GSTR1;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.opethic.genivis.controller.tranx_sales.SalesOrderListController;
import com.opethic.genivis.dto.GSTR1.GSTR1NilRatedDTO;
import com.opethic.genivis.dto.reqres.product.Communicator;
import com.opethic.genivis.network.APIClient;
import com.opethic.genivis.network.EndPoints;
import com.opethic.genivis.network.RequestType;
import com.opethic.genivis.utils.*;
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
public class GSTR1CreditDebitUnRegisteredController implements Initializable {

    @FXML
    private TableView<GSTR1NilRatedDTO> tvGSTR1NilRated;
    @FXML
    TableColumn<GSTR1NilRatedDTO, String> tcGSTR1NilRatedSrNo, tcGSTR1NilRatedVoucherType, tcGSTR1NilRatedParticulars, tcGSTR1NilRatedNilRated, tcGSTR1NilRatedExempted;
    @FXML
    private TextField dpGSTR1NilRatedFromDate, dpGSTR1NilRatedToDate, tfGSTR1NilRatedSearch;
    @FXML
    private Label labelGSTR1NilRatedNilRated, labelGSTR1NilRatedExempted;
    @FXML
    private Button tfGSTR1NilRatedUpload;

    private Node[] focusableNodes;

    private ObservableList<GSTR1NilRatedDTO> originalData;
    // Assuming this format is compatible with your API
    DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    @FXML
    private BorderPane spRootNilRatedPane;
    private LocalDate fromDate, toDate;
    private String responseBody, message;

    private JsonObject jsonObject = null;

    private static final Logger logger = LogManager.getLogger(SalesOrderListController.class);
    ObservableList<GSTR1NilRatedDTO> gstr1nilratedObservableList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //Set the TableView Columns with proper height and Width
        tvGSTR1NilRated.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        GSTR1NilRatedTableDesign();
        Platform.runLater(() -> dpGSTR1NilRatedFromDate.requestFocus());
        //Populate focusable nodes array with the desired order and Focus To next and Previous Element
        focusableNodes = new Node[]{dpGSTR1NilRatedFromDate, dpGSTR1NilRatedToDate, tfGSTR1NilRatedSearch, tfGSTR1NilRatedUpload, tvGSTR1NilRated};
        CommonValidationsUtils.setupFocusNavigation(focusableNodes);

        getGSTR1NilRatedData("");
        spRootNilRatedPane.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ESCAPE) {
//                System.out.println("Esc Clicked....!");
                GlobalController.getInstance().addTabStatic(GSTR1_DASHBOARD_LIST_SLUG, false);
            }
        });
        DateValidator.applyDateFormat(dpGSTR1NilRatedFromDate);
        DateValidator.applyDateFormat(dpGSTR1NilRatedToDate);
        sceneInitilization();

        dpGSTR1NilRatedToDate.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                fromDate = null;
                toDate = null;
                if (!dpGSTR1NilRatedFromDate.getText().isEmpty()) {
                    fromDate = Communicator.text_to_date.fromString(dpGSTR1NilRatedFromDate.getText());
                }
                if (!dpGSTR1NilRatedToDate.getText().isEmpty()) {
                    toDate = Communicator.text_to_date.fromString(dpGSTR1NilRatedToDate.getText());
                }
                getGSTR1NilRatedData("");
                tfGSTR1NilRatedSearch.requestFocus();
            }
        });

        //todo:Search without API Call in the Table
        originalData = tvGSTR1NilRated.getItems();
        tfGSTR1NilRatedSearch.textProperty().addListener((observable, oldValue, newValue) -> {
            filterData(newValue.trim());
        });
    }

    public void sceneInitilization() {
        spRootNilRatedPane.sceneProperty().addListener((observable, oldScene, newScene) -> {
            if (newScene != null && newScene.getWindow() instanceof Stage) {
                Communicator.stage = (Stage) newScene.getWindow();
            }
        });
    }
    private void filterData(String keyword) {
        ObservableList<GSTR1NilRatedDTO> filteredData = FXCollections.observableArrayList();

        if (keyword.isEmpty()) {
            tvGSTR1NilRated.setItems(originalData);
            calculateTotalAmount(originalData); // Calculate totals for all records
            return;
        }

        for (GSTR1NilRatedDTO item : originalData) {
            if (matchesKeyword(item, keyword)) {
                filteredData.add(item);
            }
        }

        tvGSTR1NilRated.setItems(filteredData);
        calculateTotalAmount(filteredData); // Calculate totals for filtered records
    }

    private void calculateTotalAmount(ObservableList<GSTR1NilRatedDTO> data)
    {
        // Calculate the Totals
        double totalNilRated = 0.0;
        double totalExempted = 0.0;
        for (GSTR1NilRatedDTO item : data) {
            // Update total amounts
            if ("exempted".equalsIgnoreCase(item.getTaxType())) {
                totalExempted += Double.parseDouble(item.getExempted().isEmpty() ? "0.0" : item.getExempted());
            } else if ("nilrated".equalsIgnoreCase(item.getTaxType())) {
                totalNilRated += Double.parseDouble(item.getNilRated().isEmpty() ? "0.0" : item.getNilRated());
            }
        }

        // Update Labels in the FXML
        labelGSTR1NilRatedNilRated.setText(String.format("%.2f", totalNilRated));
        labelGSTR1NilRatedExempted.setText(String.format("%.2f", totalExempted));
    }

    //
    //todo:Search Function to Search in the Table for columns
    private boolean matchesKeyword(GSTR1NilRatedDTO item, String keyword) {
        String lowerCaseKeyword = keyword.toLowerCase();

        // todo:Check if any of the columns contain the keyword
        return item.getId().toLowerCase().contains(lowerCaseKeyword) || item.getVoucherType().toLowerCase().contains(lowerCaseKeyword) || item.getParticulars().toLowerCase().contains(lowerCaseKeyword) || item.getNilRated().toLowerCase().contains(lowerCaseKeyword) || item.getExempted().toLowerCase().contains(lowerCaseKeyword);
    }

    //Design of table to display the data
    public void GSTR1NilRatedTableDesign() {
        //todo: Responsive code for bottom productInfo tableview
        tcGSTR1NilRatedSrNo.prefWidthProperty().bind(tvGSTR1NilRated.widthProperty().multiply(0.05));
        tcGSTR1NilRatedVoucherType.prefWidthProperty().bind(tvGSTR1NilRated.widthProperty().multiply(0.10));
        tcGSTR1NilRatedParticulars.prefWidthProperty().bind(tvGSTR1NilRated.widthProperty().multiply(0.65));
        tcGSTR1NilRatedNilRated.prefWidthProperty().bind(tvGSTR1NilRated.widthProperty().multiply(0.10));
        tcGSTR1NilRatedExempted.prefWidthProperty().bind(tvGSTR1NilRated.widthProperty().multiply(0.10));
    }

    //Get and Set the  List Data of GSTR1 Nil Rated
    public void getGSTR1NilRatedData(String searchKey) {
        APIClient apiClient = null;
//        SalesQuotationListLogger.info(" fetch Sales Quotation invoice List : SalesQuotationListController");
        try {
            gstr1nilratedObservableList.clear();
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
            apiClient = new APIClient(EndPoints.GET_GSTR1_CredDebUnreg_RATED_ENDPOINT, requestBody, RequestType.FORM_DATA);
            apiClient.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
                @Override
                public void handle(WorkerStateEvent workerStateEvent) {
                    jsonObject = new Gson().fromJson(workerStateEvent.getSource().getValue().toString(), JsonObject.class);

//                    System.out.println("SalesOrder" + response.body());
//                    JsonObject jsonObject = new Gson().fromJson(response.body(), JsonObject.class);
                    System.out.println("this is Response of Nil Rated Report" + jsonObject);
                    gstr1nilratedObservableList = FXCollections.observableArrayList();
                    if (jsonObject.get("responseStatus").getAsInt() == 200) {
                        LocalDate start_date = LocalDate.parse(jsonObject.get("start_date").getAsString());
                        LocalDate end_date = LocalDate.parse(jsonObject.get("end_date").getAsString());
                        dpGSTR1NilRatedFromDate.setText(start_date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
                        dpGSTR1NilRatedToDate.setText(end_date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
//                        JsonObject responseObject = jsonObject.get("data").getAsJsonObject();
                        JsonArray responseArray = jsonObject.get("data").getAsJsonArray();

                        if (responseArray.size() > 0) {
                            tvGSTR1NilRated.setVisible(true);
                            // Variables to store total amounts
//                            double totalExempted = 0.0;
//                            double totalNilRated = 0.0;
                            for (JsonElement element : responseArray) {
                                JsonObject item = element.getAsJsonObject();
                                System.out.println("item in nil rated list " + item);
                                String id = item.get("id").getAsString();
                                String voucherType = item.get("voucher_type").getAsString();    //imp for conversion check
                                String particulars = item.get("ledger_name") != null ? item.get("ledger_name").getAsString() : "";
                                String nilRated = item.get("total_amount").getAsString();
                                String exempted = item.get("total_amount").getAsString();
                                String taxType = item.get("tax_type").getAsString();

                                gstr1nilratedObservableList.add(new GSTR1NilRatedDTO(id, voucherType, particulars, nilRated, exempted, taxType));
                                // Update total amounts
//                                if ("exempted".equalsIgnoreCase(taxType)) {
//                                    totalExempted += Double.parseDouble(exempted);
//                                } else if ("nilrated".equalsIgnoreCase(taxType)) {
//                                    totalNilRated += Double.parseDouble(nilRated);
//                                }
                            }
                            tcGSTR1NilRatedVoucherType.setCellValueFactory(new PropertyValueFactory<>("voucherType"));
                            tcGSTR1NilRatedParticulars.setCellValueFactory(new PropertyValueFactory<>("particulars"));
                            tcGSTR1NilRatedExempted.setCellValueFactory(new PropertyValueFactory<>("exempted"));
                            tcGSTR1NilRatedExempted.setCellFactory(column -> {
                                return new TableCell<GSTR1NilRatedDTO, String>() {
                                    @Override
                                    protected void updateItem(String item, boolean empty) {
                                        super.updateItem(item, empty);
                                        if (item == null || empty) {
                                            setText(null);
                                        } else {
                                            GSTR1NilRatedDTO gstr1NilRatedDTO = getTableView().getItems().get(getIndex());
                                            if (gstr1NilRatedDTO.getTaxType().equalsIgnoreCase("exempted")) {
                                                setText(gstr1NilRatedDTO.getExempted());
                                            } else {
                                                setText(null); // Clear the cell if tax type is not exempted
                                            }
                                        }
                                    }
                                };
                            });

                            tcGSTR1NilRatedNilRated.setCellValueFactory(new PropertyValueFactory<>("nilRated"));
                            tcGSTR1NilRatedNilRated.setCellFactory(column -> {
                                return new TableCell<GSTR1NilRatedDTO, String>() {
                                    @Override
                                    protected void updateItem(String item, boolean empty) {
                                        super.updateItem(item, empty);
                                        if (item == null || empty) {
                                            setText(null);
                                        } else {
                                            GSTR1NilRatedDTO gstr1NilRatedDTO = getTableView().getItems().get(getIndex());
                                            if (gstr1NilRatedDTO.getTaxType().equalsIgnoreCase("nilrated")) {
                                                setText(gstr1NilRatedDTO.getNilRated());
                                            } else {
                                                setText(null); // Clear the cell if tax type is not nilrated
                                            }
                                        }
                                    }
                                };
                            });
                            // Set cell factory for the "Serial Number" column
                            tcGSTR1NilRatedSrNo.setCellFactory(column -> {
                                return new TableCell<GSTR1NilRatedDTO, String>() {
                                    @Override
                                    protected void updateItem(String item, boolean empty) {
                                        super.updateItem(item, empty);

                                        if (empty || getIndex() < 0) {
                                            setText(null);
                                        } else {
                                            // Set the serial number as the index of the row + 1
                                            setText(String.valueOf(getIndex() + 1));
                                        }
                                    }
                                };
                            });
                            // Update originalData with new items

                            tvGSTR1NilRated.setItems(gstr1nilratedObservableList);
//                            originalData.setAll(gstr1nilratedObservableList);
                            originalData = gstr1nilratedObservableList;
                            // Update labels with total amounts
//                            labelGSTR1NilRatedNilRated.setText(String.format("%.2f", totalNilRated));
//                            labelGSTR1NilRatedExempted.setText(String.format("%.2f", totalExempted));
                            calculateTotalAmount(originalData);

                        } else {
//                    SalesQuotationListLogger.debug("Sales Quotation invoice List ResponseObject is null : SalesQuotationListController");
                            //TODO : use logger for alert messages
                            AlertUtility.CustomCallback callback = (number) -> {
                                if (number == 0) {
                                    dpGSTR1NilRatedToDate.requestFocus();
                                }
                            };
                            Stage stage = (Stage) spRootNilRatedPane.getScene().getWindow();
                            AlertUtility.AlertError(stage, AlertUtility.alertTypeError, "Failed to Load Data ", callback);
                            gstr1nilratedObservableList.clear();
                            calculateTotalAmount(originalData);

                        }
                    } else {
//                SalesQuotationListLogger.debug("Error in response of Sales Quotation invoice List : SalesQuotationListController");
                        //TODO : use logger for alert messages
                        AlertUtility.CustomCallback callback = (number) -> {
                            if (number == 0) {
                                dpGSTR1NilRatedToDate.requestFocus();
                            }
                        };
                        Stage stage = (Stage) spRootNilRatedPane.getScene().getWindow();
                        AlertUtility.AlertError(stage, AlertUtility.alertTypeError, "Falied to connect server! ", callback);
                        gstr1nilratedObservableList.clear();
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
                    dpGSTR1NilRatedToDate.requestFocus();
                }
            };
            Stage stage = (Stage) spRootNilRatedPane.getScene().getWindow();
            AlertUtility.AlertError(stage, AlertUtility.alertTypeError, "Falied to connect server! ", callback);

            e.printStackTrace();
        } finally {
            apiClient = null;
        }
    }

}
