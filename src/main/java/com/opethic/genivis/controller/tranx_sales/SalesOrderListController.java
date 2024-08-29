package com.opethic.genivis.controller.tranx_sales;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.opethic.genivis.GenivisApplication;
import com.opethic.genivis.dto.SalesOrderListDTO;
import com.opethic.genivis.dto.bill_format.BillFormatDTO;
import com.opethic.genivis.dto.bill_format.BillFormatProductDetailsDTO;
import com.opethic.genivis.dto.reqres.product.Communicator;
import com.opethic.genivis.network.APIClient;
import com.opethic.genivis.network.EndPoints;
import com.opethic.genivis.network.RequestType;
import com.opethic.genivis.utils.*;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Worker;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.print.PrinterJob;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Screen;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import static com.opethic.genivis.utils.FxmFileConstants.*;
import static com.opethic.genivis.utils.FxmFileConstants.SALES_ORDER_TO_INVOICE_SLUG;
import static com.opethic.genivis.utils.Globals.salesOrderListDTO;

public class SalesOrderListController implements Initializable {
    private JsonObject jsonObject = null;

    private static final Logger logger = LogManager.getLogger(SalesOrderListController.class);
    ObservableList<SalesOrderListDTO> salesOrderObservableList = FXCollections.observableArrayList();
    @FXML
    private TableView<SalesOrderListDTO> tvSalesOrderList;
    @FXML
    TableColumn<SalesOrderListDTO, Boolean> tcSalesOrderListSelect;
    @FXML
    private TableColumn<SalesOrderListDTO, String> tcSalesOrderListSONo;
    @FXML
    private TableColumn<SalesOrderListDTO, String> tcSalesOrderListRefNo;
    @FXML
    private TableColumn<SalesOrderListDTO, String> tcSalesOrderListSODate;
    @FXML
    private TableColumn<SalesOrderListDTO, String> tcSalesOrderListClientName;
    @FXML
    private TableColumn<SalesOrderListDTO, String> tcSalesOrderListNarration;
    @FXML
    private TableColumn<SalesOrderListDTO, String> tcSalesOrderListTaxable;
    @FXML
    private TableColumn<SalesOrderListDTO, String> tcSalesOrderListTax;
    @FXML
    private TableColumn<SalesOrderListDTO, String> tcSalesOrderListBillAmount;
    @FXML
    private TableColumn<SalesOrderListDTO, String> tcSalesOrderListTranxStatus;
    @FXML
    private TableColumn<SalesOrderListDTO, String> tcSalesOrderListPrint;
    //    @FXML
//    private TableColumn<SalesOrderListDTO, String> tcSalesOrderListAction;
    @FXML
    private VBox div1;

    @FXML
    private TextField tfSalesOrderListSearch;
    @FXML
    private TextField dpSalesOrderListFromDate, dpSalesOrderListToDate;
    @FXML
    private Button btnSalesOrderListCreate, btnSalesOrderToChallan, btnSalesOrderToInvoice;
    private String conversionId;

    private Node[] focusableNodes;

    private ObservableList<SalesOrderListDTO> originalData;
    // Assuming this format is compatible with your API
    DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    @FXML
    private BorderPane spRootSalesOrdeListPane;
    private LocalDate fromDate, toDate;
    private String responseBody, message;
    private Integer id1;

    //? Highlight the Record Start
    public static boolean isNewSalesOrderCreated = false; // Flag for new creation
    public static String editedSalesOrderId = null; // ID for edited Sales Order
    //? Highlight the Record End

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        ResponsiveWiseCssPicker();

        //? this include all the Shortcut Keys
        initShortcutKeys();

        //? this inculde all the design related properties and important Fields
        madatoryFields();

        //? this include all the table view operations - Edit , Conversions , search , etc..
        tableViewOperations();


        Platform.runLater(() -> {
            //? set the Focus on the first field of the page
            tfSalesOrderListSearch.requestFocus();
        });

        //? Function to Get the List of Franchise
        getSalesOrderData("");

        //? Search without API Call in the Table
        originalData = tvSalesOrderList.getItems();
        tfSalesOrderListSearch.textProperty().addListener((observable, oldValue, newValue) -> {
            filterData(newValue.trim());
        });

        btnSalesOrderListCreate.setOnAction(event -> {
                    Globals.salesOrderListDTO = null;
                    GlobalController.getInstance().addTabStatic(SALES_ORDER_CREATE_SLUG, false);

                }
        );
    }

    private void ResponsiveWiseCssPicker() {
        double height = Screen.getPrimary().getBounds().getHeight();
        double width = Screen.getPrimary().getBounds().getWidth();
        System.out.println("width" + width);
        if (width >= 992 && width <= 1024) {
            spRootSalesOrdeListPane.getStylesheets().add(GenivisApplication.class.getResource("ui/css/TranxCommonCssStyles/tranxCommonCssStyle1.css").toExternalForm());
        } else if (width >= 1025 && width <= 1280) {
            spRootSalesOrdeListPane.getStylesheets().add(GenivisApplication.class.getResource("ui/css/TranxCommonCssStyles/tranxCommonCssStyle2.css").toExternalForm());
        } else if (width >= 1281 && width <= 1368) {
            spRootSalesOrdeListPane.getStylesheets().add(GenivisApplication.class.getResource("ui/css/TranxCommonCssStyles/tranxCommonCssStyle3.css").toExternalForm());
        } else if (width >= 1369 && width <= 1400) {
            spRootSalesOrdeListPane.getStylesheets().add(GenivisApplication.class.getResource("ui/css/TranxCommonCssStyles/tranxCommonCssStyle4.css").toExternalForm());
        } else if (width >= 1401 && width <= 1440) {
            spRootSalesOrdeListPane.getStylesheets().add(GenivisApplication.class.getResource("ui/css/TranxCommonCssStyles/tranxCommonCssStyle5.css").toExternalForm());
        } else if (width >= 1441 && width <= 1680) {
            spRootSalesOrdeListPane.getStylesheets().add(GenivisApplication.class.getResource("ui/css/TranxCommonCssStyles/tranxCommonCssStyle6.css").toExternalForm());
        } else if (width >= 1681 && width <= 1920) {
            spRootSalesOrdeListPane.getStylesheets().add(GenivisApplication.class.getResource("ui/css/TranxCommonCssStyles/tranxCommonCssStyle7.css").toExternalForm());
        }
    }

    private void tableViewOperations() {
        //? Assuming tcSalesOrderListTranxStatus is of type String
        tcSalesOrderListTranxStatus.setCellValueFactory(new PropertyValueFactory<>("tcSalesOrderListTranxStatus"));
        tcSalesOrderListTranxStatus.setCellFactory(column -> {
            return new TableCell<SalesOrderListDTO, String>() {
                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if (item == null || empty) {
                        setText(null);
                    } else {
                        setText(item);
                        // Set text color to white
//                        setTextFill(Color.WHITE);
                        setStyle("-fx-font-weight:bold;-fx-text-fill:#fff");

                        // Check the value of tcSalesOrderListTranxStatus
                        if (item.equalsIgnoreCase("opened")) {
                            // Set green background color for opened status
//                            setTextFill(Color.GREEN);
                            setStyle("-fx-font-weight:bold;-fx-text-fill:green");
                        } else if (item.equalsIgnoreCase("closed")) {
                            // Set red background color for closed status
//                            setTextFill(Color.RED);
                            setStyle("-fx-font-weight:bold;-fx-text-fill:red");
                        }
                    }
                }
            };
        });

        //? get the id onDoubleClick for Edit
        tvSalesOrderList.setRowFactory(tv -> {
            TableRow<SalesOrderListDTO> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && !row.isEmpty()) {
                    SalesOrderListDTO selectedItem = row.getItem();
                    String status = selectedItem.getTcSalesOrderListTranxStatus();
                    // Check if the status is "opened"
                    if (status != null && status.equalsIgnoreCase("opened")) {
                        salesOrderEditPage();
//                        GlobalController.getInstance().addTabStaticWithParam(SALES_ORDER_EDIT_SLUG, false, Integer.valueOf(salesOrderId));
                    } else {
                        // Show an alert indicating that the order is already closed
                        AlertUtility.AlertErrorTimeout(AlertUtility.alertTypeError, "The Order is closed", in -> {
                            tfSalesOrderListSearch.requestFocus();
                        });
//                        AlertUtility.CustomCallback callback = (number) -> {
//                            if (number == 0) {
//                                tfSalesOrderListSearch.requestFocus();
//                            }
//                        };
//                        Stage stage = (Stage) spRootSalesOrdeListPane.getScene().getWindow();
//                        AlertUtility.AlertError(stage, AlertUtility.alertTypeError, "Order is already closed", callback);
                    }
                }
            });
            return row;
        });


        //? code for conversion
        tcSalesOrderListSelect.setCellFactory(column -> new CheckBoxTableCell<>());
        tcSalesOrderListSelect.setCellValueFactory(cellData -> {
            SalesOrderListDTO cellValue = cellData.getValue();
            conversionId = cellValue.getId();             //we get the id of sales order which list we have selected
            String ledgId = cellValue.getLedgerId();
            String listId = cellValue.getId();
            SaleOrderToChallanController.scConversionId = conversionId;

            findOutSelectedRow();
            BooleanProperty property = cellValue.is_row_selectedProperty();
            if (cellValue.isIs_row_selected()) {
                if (cellValue.getTcSalesOrderListTranxStatus().equalsIgnoreCase("closed")) {
//                    AlertUtility.AlertError("Error","The Order is Closed",callback->{
//                        tfSalesOrderListSearch.requestFocus();
//
//                    });
                    AlertUtility.AlertErrorTimeout(AlertUtility.alertTypeError, "The Order is Closed", in -> {
                        tfSalesOrderListSearch.requestFocus();
                    });
                } else {
                    handleSelection(tvSalesOrderList.getItems());
                }
            }

            // Add listener to handler change
            property.addListener((observable, oldValue, newValue) -> cellValue.setIs_row_selected(newValue));

            return property;
        });
    }

    private void madatoryFields() {
        //Set the TableView Columns with proper height and Width
        tvSalesOrderList.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        CommonFunctionalUtils.restrictTextFieldToDigitsAndDateFormat(dpSalesOrderListFromDate);
        CommonFunctionalUtils.restrictTextFieldToDigitsAndDateFormat(dpSalesOrderListToDate);

        sceneInitilization();
        DateValidator.applyDateFormat(dpSalesOrderListFromDate);
        DateValidator.applyDateFormat(dpSalesOrderListToDate);
    }

    private void initShortcutKeys() {
        // Enter traversal
        spRootSalesOrdeListPane.addEventFilter(KeyEvent.KEY_PRESSED, (KeyEvent event) -> {

            if (event.getCode() == KeyCode.ENTER) {
                if (event.getTarget() == dpSalesOrderListToDate) {
                    fromDate = null;
                    toDate = null;
                    if (!dpSalesOrderListFromDate.getText().isEmpty()) {
                        fromDate = Communicator.text_to_date.fromString(dpSalesOrderListFromDate.getText());
                    }
                    if (!dpSalesOrderListToDate.getText().isEmpty()) {
                        toDate = Communicator.text_to_date.fromString(dpSalesOrderListToDate.getText());
                    }
                    getSalesOrderData("");
                    btnSalesOrderListCreate.requestFocus();
                } else {
                    if (event.getTarget() instanceof Button targetButton && targetButton.getText().equalsIgnoreCase("create")) {
                        System.out.println(targetButton.getText());
                    } else {
                        salesOrderEditPage();
                        KeyEvent newEvent = new KeyEvent(
                                null,
                                null,
                                KeyEvent.KEY_PRESSED,
                                "",
                                "\t",
                                KeyCode.TAB,
                                event.isShiftDown(),
                                event.isControlDown(),
                                event.isAltDown(),
                                event.isMetaDown()
                        );

                        Event.fireEvent(event.getTarget(), newEvent);
                        event.consume();
                    }
                }
            } else if (event.getCode() == KeyCode.N && event.isControlDown()) {
                btnSalesOrderListCreate.fire();
            } else if (event.getCode() == KeyCode.DOWN && tfSalesOrderListSearch.isFocused()) {
                tvSalesOrderList.getSelectionModel().select(0);
                tvSalesOrderList.requestFocus();
            } else if (event.getCode() == KeyCode.E && event.isControlDown()) {
                salesOrderEditPage();
            } else if (event.getCode() == KeyCode.D && event.isControlDown()) {
                handleCtrlDPressed();
            }
        });
    }

    public void sceneInitilization() {
        spRootSalesOrdeListPane.sceneProperty().addListener((observable, oldScene, newScene) -> {
            if (newScene != null && newScene.getWindow() instanceof Stage) {
                Communicator.stage = (Stage) newScene.getWindow();
            }
        });
    }

    //? Search Function to Search in the Table
    private void filterData(String keyword) {
        ObservableList<SalesOrderListDTO> filteredData = FXCollections.observableArrayList();

        if (keyword.isEmpty()) {
            tvSalesOrderList.setItems(originalData);
            return;
        }

        for (SalesOrderListDTO item : originalData) {
            if (matchesKeyword(item, keyword)) {
                filteredData.add(item);
            }
        }

        tvSalesOrderList.setItems(filteredData);
    }

    //
    //? Search Function to Search in the Table for columns
    private boolean matchesKeyword(SalesOrderListDTO item, String keyword) {
        String lowerCaseKeyword = keyword.toLowerCase();

        // ? Check if any of the columns contain the keyword
        return item.getTcSalesOrderListSONo().toLowerCase().contains(lowerCaseKeyword) ||
                item.getTcSalesOrderListRefNo().toLowerCase().contains(lowerCaseKeyword) ||
                item.getTcSalesOrderListSODate().toLowerCase().contains(lowerCaseKeyword) ||
                item.getTcSalesOrderListClientName().toLowerCase().contains(lowerCaseKeyword) ||
                item.getTcSalesOrderListNarration().toLowerCase().contains(lowerCaseKeyword) ||
                item.getTcSalesOrderListTaxable().toLowerCase().contains(lowerCaseKeyword) ||
                item.getTcSalesOrderListTax().toLowerCase().contains(lowerCaseKeyword) ||
                item.getTcSalesOrderListBillAmount().toLowerCase().contains(lowerCaseKeyword) ||
                item.getTcSalesOrderListTranxStatus().toLowerCase().contains(lowerCaseKeyword) ||
                item.getTcSalesOrderListPrint().toLowerCase().contains(lowerCaseKeyword);
//                item.getTcSalesOrderListAction().toLowerCase().contains(lowerCaseKeyword);
    }

    //Get and Set the  List Data of Sales Order
    public void getSalesOrderData(String searchKey) {
        APIClient apiClient = null;
//        SalesQuotationListLogger.info(" fetch Sales Quotation invoice List : SalesQuotationListController");
        try {
            logger.debug("Get Sales Order Data Started...");
            // Format dates
            String startDate = fromDate != null ? fromDate.format(dateFormatter) : "";
            String endDate = toDate != null ? toDate.format(dateFormatter) : "";
            System.out.println("Start Date: " + startDate);
            System.out.println("End Date: " + endDate);
            Map<String, String> body = new HashMap<>();
            body.put("pageNo", "1");
            body.put("pageSize", "50");
            body.put("searchText", searchKey);
            body.put("sort", "");
            body.put("startDate", startDate);
            body.put("endDate", endDate);
            String requestBody = Globals.mapToString(body);
//            HttpResponse<String> response = APIClient.postJsonRequest(requestBody, EndPoints.SALES_ORDER_LIST_ENDPOINT);
            apiClient = new APIClient(EndPoints.SALES_ORDER_LIST_ENDPOINT, requestBody, RequestType.JSON);
            apiClient.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
                @Override
                public void handle(WorkerStateEvent workerStateEvent) {
                    jsonObject = new Gson().fromJson(workerStateEvent.getSource().getValue().toString(), JsonObject.class);

//                    System.out.println("SalesOrder" + response.body());
//                    JsonObject jsonObject = new Gson().fromJson(response.body(), JsonObject.class);
                    System.out.println("111Q" + jsonObject);
                    salesOrderObservableList = FXCollections.observableArrayList();
                    if (jsonObject.get("responseStatus").getAsInt() == 200) {
                        JsonObject responseObject = jsonObject.get("responseObject").getAsJsonObject();
                        JsonArray responseArray = responseObject.get("data").getAsJsonArray();

                        if (responseArray.size() > 0) {
                            tvSalesOrderList.setVisible(true);
                            for (JsonElement element : responseArray) {
                                JsonObject item = element.getAsJsonObject();
                                System.out.println("item in sale order list " + item);
                                String id = item.get("id").getAsString();
                                String ledgerId = item.get("sundry_debtors_id").getAsString();    //imp for conversion check
                                String tcSalesOrderListSONo = item.get("bill_no") != null ? item.get("bill_no").getAsString() : "";
                                String tcSalesOrderListRefNo = "";
                                String tcSalesOrderListSODate = DateConvertUtil.convertDispDateFormat(item.get("bill_date").getAsString());
                                String tcSalesOrderListClientName = item.get("sundry_debtors_name").getAsString();
                                String tcSalesOrderListNarration = item.get("narration").getAsString();
                                String tcSalesOrderListTaxable = item.get("taxable_amt").getAsString();
                                String tcSalesOrderListTax = item.get("tax_amt").getAsString();
                                String tcSalesOrderListBillAmount = item.get("total_amount").getAsString();
                                String tcSalesOrderListTranxStatus = item.get("sales_order_status").getAsString();
                                String tcSalesOrderListPrint = "";
                                String tcSalesOrderListAction = "";

                                salesOrderObservableList.add(new SalesOrderListDTO(id, tcSalesOrderListSONo, tcSalesOrderListRefNo, tcSalesOrderListSODate, tcSalesOrderListClientName,
                                        tcSalesOrderListNarration, tcSalesOrderListTaxable, tcSalesOrderListTax, tcSalesOrderListBillAmount, tcSalesOrderListTranxStatus,
                                        tcSalesOrderListPrint, tcSalesOrderListAction, ledgerId)
                                );
                            }
                            tcSalesOrderListSONo.setCellValueFactory(new PropertyValueFactory<>("tcSalesOrderListSONo"));
//                    tblcPurChallLstRefNo.setCellValueFactory(new PropertyValueFactory<>("referenceNo"));
                            tcSalesOrderListRefNo.setCellValueFactory(new PropertyValueFactory<>("tcSalesOrderListRefNo"));
                            tcSalesOrderListSODate.setCellValueFactory(new PropertyValueFactory<>("tcSalesOrderListSODate"));
                            tcSalesOrderListClientName.setCellValueFactory(new PropertyValueFactory<>("tcSalesOrderListClientName"));
                            tcSalesOrderListNarration.setCellValueFactory(new PropertyValueFactory<>("tcSalesOrderListNarration"));
                            tcSalesOrderListTaxable.setCellValueFactory(new PropertyValueFactory<>("tcSalesOrderListTaxable"));
                            tcSalesOrderListTax.setCellValueFactory(new PropertyValueFactory<>("tcSalesOrderListTax"));
                            tcSalesOrderListBillAmount.setCellValueFactory(new PropertyValueFactory<>("tcSalesOrderListBillAmount"));
                            tcSalesOrderListTranxStatus.setCellValueFactory(new PropertyValueFactory<>("tcSalesOrderListTranxStatus"));
//                            tcSalesOrderListPrint.setCellValueFactory(new PropertyValueFactory<>("tcSalesOrderListPrint"));
//                    tcSalesOrderListAction.setCellValueFactory(new PropertyValueFactory<>("tcSalesOrderListAction"));
//                            tcSalesOrderListAction.setCellFactory(param -> {
//                                final TableCell<SalesOrderListDTO, String> cell = new TableCell<>() {
//                                    private ImageView delImg = new ImageView(new Image(String.valueOf(GenivisApplication.class.getResource("ui/assets/del.png"))));
//                                    private ImageView edtImg = new ImageView(new Image(String.valueOf(GenivisApplication.class.getResource("ui/assets/edt.png"))));
////                            private ImageView viewImg = new ImageView(new Image(String.valueOf(GenivisApplication.class.getResource("ui/assets/view.png"))));
//
//                                    {
//                                        delImg.setFitHeight(20.0);
//                                        delImg.setFitWidth(20.0);
//                                        edtImg.setFitHeight(20.0);
//                                        edtImg.setFitWidth(20.0);
////                                viewImg.setFitHeight(20.0);
////                                viewImg.setFitWidth(20.0);
//                                    }
//
//                                    private final Button delButton = new Button("", delImg);
//                                    private final Button edtButton = new Button("", edtImg);
////                            private final Button viewButton = new Button("", viewImg);
//
//                                    {
//                                        edtButton.setOnAction(actionEvent -> {
//                                            SalesOrderListDTO id = getTableView().getItems().get(getIndex());
//                                            String status = id.getTcSalesOrderListTranxStatus();
//                                            if (status != null && status.equalsIgnoreCase("opened")) {
//                                                Integer id1 = Integer.valueOf(id.getId());
////                                                GlobalController.getInstance().addTabStaticWithParam(SALES_ORDER_EDIT_SLUG, false, id1);
//                                            } else {
//                                                // Show an alert indicating that the order is already closed
//                                                AlertUtility.CustomCallback callback = (number) -> {
//                                                    if (number == 0) {
//                                                        tfSalesOrderListSearch.requestFocus();
//                                                    }
//                                                };
//                                                Stage stage = (Stage) spRootSalesOrdeListPane.getScene().getWindow();
//                                                AlertUtility.AlertError(stage, AlertUtility.alertTypeError, "Order is already closed", callback);
//                                            }
////                                            Integer id = Integer.valueOf(getTableView().getItems().get(getIndex()).getId());
////                                            GlobalController.getInstance().addTabStaticWithParam(SALES_ORDER_EDIT_SLUG, false, id);
//                                        });
//                                    }{
//                                        delButton.setOnAction(actionEvent -> {
//                                            SalesOrderListDTO id = getTableView().getItems().get(getIndex());
//                                            String status = id.getTcSalesOrderListTranxStatus();
//                                            // Check if the status is "opened"
//                                            if (status != null && status.equalsIgnoreCase("opened")) {
//                                                 id1 = Integer.valueOf(id.getId());
//                                                deleteSaleOrder(id1);
//                                            } else {
//                                                // Show an alert indicating that the order is already closed
//                                                AlertUtility.CustomCallback callback = (number) -> {
//                                                    if (number == 0) {
//                                                        tfSalesOrderListSearch.requestFocus();
//                                                    }
//                                                };
//                                                Stage stage = (Stage) spRootSalesOrdeListPane.getScene().getWindow();
//                                                AlertUtility.AlertError(stage, AlertUtility.alertTypeError, "Order is already closed", callback);
//                                            }
////                                    GlobalController.getInstance().addTabStaticWithParam(SALES_ORDER_EDIT_SLUG, false, id);
//                                        });
//                                    }
//
//                                    HBox hbActions = new HBox();
//
//                                    {
//                                        hbActions.getChildren().add(edtButton);
////                                hbActions.getChildren().add(viewButton);
//                                        hbActions.getChildren().add(delButton);
//                                        hbActions.setSpacing(10.0);
//                                    }
//
//                                    // Set the action for the view button
//                                    @Override
//                                    protected void updateItem(String item, boolean empty) {
//                                        super.updateItem(item, empty);
//                                        if (empty) {
//                                            setGraphic(null);
//                                        } else {
//                                            setGraphic(hbActions);
//                                        }
//                                    }
//                                };
//                                return cell;
//                            });
                            tcSalesOrderListPrint.setCellValueFactory(new PropertyValueFactory<>("tcSalesOrderListPrint"));
                            tcSalesOrderListPrint.setCellFactory(param -> {
                                final TableCell<SalesOrderListDTO, String> cell = new TableCell<>() {
                                    private ImageView printImg = new ImageView(new Image(String.valueOf(GenivisApplication.class.getResource("ui/assets/print.png"))));
//                            private ImageView viewImg = new ImageView(new Image(String.valueOf(GenivisApplication.class.getResource("ui/assets/view.png"))));

                                    {
                                        printImg.setFitHeight(20.0);
                                        printImg.setFitWidth(20.0);
                                    }

                                    private final Button printButton = new Button("", printImg);
//                            private final Button viewButton = new Button("", viewImg);

                                    {
                                        printButton.setOnAction(actionEvent -> {
                                            SalesOrderListDTO id = getTableView().getItems().get(getIndex());
                                            String status = id.getTcSalesOrderListTranxStatus();
                                            // Check if the status is "opened"
                                            if (status != null) {
                                                id1 = Integer.valueOf(id.getId());
                                                printSaleOrder(id1);
                                            } else {
                                                // Show an alert indicating that the order is already closed
                                                AlertUtility.CustomCallback callback = (number) -> {
                                                    if (number == 0) {
                                                        tfSalesOrderListSearch.requestFocus();
                                                    }
                                                };
                                            }
//                                    GlobalController.getInstance().addTabStaticWithParam(SALES_ORDER_EDIT_SLUG, false, id);
                                        });
                                    }

                                    HBox hbActions = new HBox();

                                    {
//                                        hbActions.getChildren().add(edtButton);
//                                hbActions.getChildren().add(viewButton);
                                        hbActions.getChildren().add(printButton);
                                        hbActions.setSpacing(10.0);
                                    }

                                    // Set the action for the view button
                                    @Override
                                    protected void updateItem(String item, boolean empty) {
                                        super.updateItem(item, empty);
                                        if (empty) {
                                            setGraphic(null);
                                        } else {
                                            setGraphic(hbActions);
                                        }
                                    }
                                };
                                return cell;
                            });

                            // Update originalData with new items
                            originalData.setAll(salesOrderObservableList);

                            tvSalesOrderList.setItems(salesOrderObservableList);
                            //******************************** Highlight on the Created/Edited Record in the List Start ********************************
                            if (SalesOrderListController.isNewSalesOrderCreated) {
                                tvSalesOrderList.getSelectionModel().selectFirst();
                                tvSalesOrderList.scrollTo(0);
                                SalesOrderListController.isNewSalesOrderCreated = false; // Reset the flag
                            } else if (SalesOrderListController.editedSalesOrderId != null) {
                                for (SalesOrderListDTO salesOrder : salesOrderObservableList) {
                                    if (salesOrder.getId().equals(SalesOrderListController.editedSalesOrderId)) {
                                        tvSalesOrderList.getSelectionModel().select(salesOrder);
                                        tvSalesOrderList.scrollTo(salesOrder);
                                        SalesOrderListController.editedSalesOrderId = null; // Reset the ID
                                        break;
                                    }
                                }
                            }
                            //******************************** Highlight on the Created/Edited Record in the List End ********************************

                        } else {
                            logger.error("Failed to Load Data ");
                        }
                    } else {
                        logger.debug("Error in response of Sales Order  List : SalesOrderListController");
                        //? use logger for alert messages
//                        AlertUtility.CustomCallback callback = (number) -> {
//                            if(number==0) {
//                                dpSalesOrderListToDate.requestFocus();
//                            }
//                        };
//                        Stage stage = (Stage) spRootSalesOrdeListPane.getScene().getWindow();
//                        AlertUtility.AlertError(stage,AlertUtility.alertTypeError, "Falied to connect server! ", callback);
                        AlertUtility.AlertErrorTimeout(AlertUtility.alertTypeError, " Falied to connect server!", in -> {
                        });

                    }
                }
            });

            apiClient.setOnCancelled(new EventHandler<WorkerStateEvent>() {
                @Override
                public void handle(WorkerStateEvent workerStateEvent) {
                    logger.error("Network API cancelled in getSalesOrderData()" + workerStateEvent.getSource().getValue().toString());

                }
            });

            apiClient.setOnFailed(new EventHandler<WorkerStateEvent>() {
                @Override
                public void handle(WorkerStateEvent workerStateEvent) {
                    logger.error("Network API failed in getSalesOrderData()" + workerStateEvent.getSource().getValue().toString());
                }
            });
            apiClient.start();
            logger.debug("Get Sales Order Data End...");
        } catch (Exception e) {
            logger.error("Sales Order List Error is " + e.getMessage());
            //? use logger for alert messages
//            AlertUtility.CustomCallback callback = (number) -> {
//                if(number==0) {
//                            dpSalesOrderListToDate.requestFocus();
//                }
//            };
//            Stage stage = (Stage) spRootSalesOrdeListPane.getScene().getWindow();
//            AlertUtility.AlertError(stage,AlertUtility.alertTypeError, "Falied to connect server! ", callback);
            AlertUtility.AlertErrorTimeout(AlertUtility.alertTypeError, " Falied to connect server!", in -> {
            });

            e.printStackTrace();
        } finally {
            apiClient = null;
        }
    }

    //Delete API Function
    public void deleteSaleOrder(int id) {
        try {
            AlertUtility.CustomCallback callback = number -> {
                if (number == 1) {
                    APIClient apiClient = null;
                    try {
                        logger.debug("Delete Sales Order Data Started...");
                        Map<String, String> map = new HashMap<>();
                        map.put("id", String.valueOf(id));
                        String formData = Globals.mapToStringforFormData(map);
//                    HttpResponse<String> response = APIClient.postFormDataRequest(formData, EndPoints.SALES_ORDER_DELETE_ENDPOINT);
                        apiClient = new APIClient(EndPoints.SALES_ORDER_DELETE_ENDPOINT, formData, RequestType.FORM_DATA);

                        apiClient.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
                            @Override
                            public void handle(WorkerStateEvent workerStateEvent) {
                                jsonObject = new Gson().fromJson(workerStateEvent.getSource().getValue().toString(), JsonObject.class);

//                            responseBody = response.body();
//                            JsonObject jsonObject = new Gson().fromJson(responseBody, JsonObject.class);
                                message = jsonObject.get("message").getAsString();
                                if (jsonObject.get("responseStatus").getAsInt() == 200) {
                                    AlertUtility.AlertSuccessTimeout(AlertUtility.alertTypeSuccess, message, input -> {
                                        // Update the list after successful deletion
                                        salesOrderObservableList.removeIf(item -> item.getId().equals(String.valueOf(id)));
                                        tvSalesOrderList.setItems(salesOrderObservableList);
                                    });
                                } else {
                                    AlertUtility.AlertErrorTimeout(AlertUtility.alertTypeError, message, in -> {
                                    });
                                }
                            }
                        });
                        apiClient.setOnCancelled(new EventHandler<WorkerStateEvent>() {
                            @Override
                            public void handle(WorkerStateEvent workerStateEvent) {
                                logger.error("Network API cancelled in deleteSaleOrder()" + workerStateEvent.getSource().getValue().toString());

                            }
                        });

                        apiClient.setOnFailed(new EventHandler<WorkerStateEvent>() {
                            @Override
                            public void handle(WorkerStateEvent workerStateEvent) {
                                logger.error("Network API failed in deleteSaleOrder()" + workerStateEvent.getSource().getValue().toString());
                            }
                        });
                        apiClient.start();
                        logger.debug("Delete Sales Order Data End...");

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    logger.error("\"Failed to Delete: deleteSaleOrder()");
                }
            };
            AlertUtility.AlertConfirmation(AlertUtility.alertTypeConfirmation, "Do you want to Delete", callback);
//        Globals.salesOrderListDTO=null;
//        GlobalController.getInstance().addTabStatic(FRANCHISE_LIST_SLUG,false);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    //?  Open Create Page Only for Edit in the Same Tab
    public void salesOrderEditPage() {
//        int index=tvSalesOrderList.getSelectionModel().getSelectedIndex();
        try {
            salesOrderListDTO = tvSalesOrderList.getSelectionModel().getSelectedItem();
            SalesOrderListDTO selectedItem = salesOrderListDTO;
            String status = selectedItem.getTcSalesOrderListTranxStatus();
            if (status != null && status.equalsIgnoreCase("opened")) {
                if (Globals.salesOrderListDTO != null) {
                    //? Highlight
                    SalesOrderListController.editedSalesOrderId = salesOrderListDTO.getId();
                    Integer id = Integer.valueOf(salesOrderListDTO.getId());
                    GlobalController.getInstance().addTabStaticWithParam(SALES_ORDER_EDIT_SLUG, false, id);
                }
            } else {
                // Show an alert indicating that the order is already closed
                AlertUtility.AlertErrorTimeout(AlertUtility.alertTypeError, "The Order is closed", in -> {
//                    tvSalesOrderList.getSelectionModel().select(index);
//                    tvSalesOrderList.scrollTo(index);
                    tfSalesOrderListSearch.requestFocus();
                });
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //function for selecting row for sale order to challan OR sale order to invoice
    private void findOutSelectedRow() {
        Boolean canShowButton = false;
        ObservableList<SalesOrderListDTO> list = tvSalesOrderList.getItems();
        for (SalesOrderListDTO object : list) {
            if (object.isIs_row_selected()) {
                canShowButton = true;
                System.out.println("Selected list order no. " + object.getTcSalesOrderListSONo());
            }
        }
        if (canShowButton) {
            btnSalesOrderListCreate.setVisible(false);
            btnSalesOrderToChallan.setVisible(true);
            btnSalesOrderToInvoice.setVisible(true);
        } else {
            btnSalesOrderListCreate.setVisible(true);
            btnSalesOrderToChallan.setVisible(false);
            btnSalesOrderToInvoice.setVisible(false);
        }
    }

    //function for get the id in list for
    private void handleSelection(ObservableList<SalesOrderListDTO> items) {
        String jsonFormat = " [%s] ";

        StringJoiner selectedIds = new StringJoiner(",");
        JsonObject obj = new JsonObject();
        JsonArray array = new JsonArray();
        String lastLedgerId = null;

        for (SalesOrderListDTO item : items) {
//            System.out.println("itemmmm " + item.getTcSalesOrderListTranxStatus());
            boolean selectedItem = item.isIs_row_selected();

            if (item.isIs_row_selected()) {
                if (lastLedgerId == null) {
                    lastLedgerId = item.getLedgerId();
                } else if (item.isIs_row_selected() && item.getTcSalesOrderListTranxStatus().equalsIgnoreCase("closed")) {
                    AlertUtility.AlertErrorTimeout(AlertUtility.alertTypeError, "The Order is closed", in -> {
                        tfSalesOrderListSearch.requestFocus();
                    });
//                    AlertUtility.CustomCallback callback = (number) -> {
//                        if (number == 0) {
//                            tfSalesOrderListSearch.requestFocus();
//                        }
//                    };
//                    Stage stage = (Stage) spRootSalesOrdeListPane.getScene().getWindow();
//                    AlertUtility.AlertError(stage, AlertUtility.alertTypeError, "The Order is closed", callback);
                } else if (!lastLedgerId.equals(item.getLedgerId())) {
                    System.out.println("iam in same ledger condition");
                    // Ledger names are different
//                    AlertUtility.CustomCallback callback = (number) -> {
//                        if (number == 0) {
//                            tfSalesOrderListSearch.requestFocus();
//                        }
//                    };
//                    Stage stage = (Stage) spRootSalesOrdeListPane.getScene().getWindow();
//                    AlertUtility.AlertError(stage, AlertUtility.alertTypeError, "You have selected a different ledger", callback);
                    AlertUtility.AlertErrorTimeout(AlertUtility.alertTypeError, "You have selected a different ledger", in -> {
                        tfSalesOrderListSearch.requestFocus();
                    });
//                    return;
                }
                obj.addProperty("id", item.getId());
                selectedIds.add(String.valueOf(obj));
            }
        }
        array.add(selectedIds.toString());

        // Ledger names are the same, show JSON format
        String jsonResult = "";
        if (!selectedIds.toString().isEmpty()) {
            jsonResult = String.format(jsonFormat, selectedIds);
            System.out.println("jsonResult1>>>>>> " + selectedIds);  //? 2
            System.out.println("jsonResult>>>>>> " + jsonResult);    //? 3
        }
        SaleOrderToChallanController.input = jsonResult;
        TranxSalesOrderToInvoiceController.input = jsonResult;

    }

    //function for redirect to challan page
    public void redirectToChallan() {
//        System.out.println("idddd " + conversionId);

        GlobalController.getInstance().addTabStatic1(SALES_ORDER_TO_CHALLAN_SLUG, false, Integer.valueOf(conversionId));
    }

    public void redirectToInvoice() {
//        System.out.println("id in redirectToInvoice-- " + conversionId);
        GlobalController.getInstance().addTabStatic1(SALES_ORDER_TO_INVOICE_SLUG, false, Integer.valueOf(conversionId));
    }

    @FXML
    private void handleCtrlDPressed() {
        try {
            salesOrderListDTO = tvSalesOrderList.getSelectionModel().getSelectedItem();
            if (Globals.salesOrderListDTO != null) {
                Integer id = Integer.valueOf(salesOrderListDTO.getId());
                System.out.println("ctrl+D" + id);
                deleteSaleOrder(id);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //? Bill Format Function START

    //? Print API Function
    public void printSaleOrder(int id) {
        try {
            AlertUtility.CustomCallback callback = number -> {
                if (number == 1) {
                    APIClient apiClient = null;
                    try {
                        logger.debug("Print Sales Order Data Started...");
                        Map<String, String> map = new HashMap<>();
                        map.put("id", String.valueOf(id));
                        map.put("print_type", "list");
                        map.put("source", "sales_order");
                        String formData = Globals.mapToStringforFormData(map);
                        apiClient = new APIClient(EndPoints.SALES_ORDER_PRINT_ENDPOINT, formData, RequestType.FORM_DATA);

                        apiClient.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
                            @Override
                            public void handle(WorkerStateEvent workerStateEvent) {
                                jsonObject = new Gson().fromJson(workerStateEvent.getSource().getValue().toString(), JsonObject.class);

                                message = jsonObject.get("message").getAsString();
                                System.out.println("i am in Print" + jsonObject);
                                if (jsonObject.get("responseStatus").getAsInt() == 200) {

                                    Stage primaryStage = (Stage) spRootSalesOrdeListPane.getScene().getWindow();
                                    primaryStage.setTitle("Print HTML Example");

                                    // Call the handlePrintAction method to load the content and print
                                    handlePrintAction(primaryStage, jsonObject);
                                } else {
                                    AlertUtility.AlertErrorTimeout(AlertUtility.alertTypeError, message, in -> {
                                    });
                                }
                            }
                        });
                        apiClient.setOnCancelled(new EventHandler<WorkerStateEvent>() {
                            @Override
                            public void handle(WorkerStateEvent workerStateEvent) {
                                logger.error("Network API cancelled in printSaleOrder()" + workerStateEvent.getSource().getValue().toString());

                            }
                        });

                        apiClient.setOnFailed(new EventHandler<WorkerStateEvent>() {
                            @Override
                            public void handle(WorkerStateEvent workerStateEvent) {
                                logger.error("Network API failed in printSaleOrder()" + workerStateEvent.getSource().getValue().toString());
                            }
                        });
                        apiClient.start();
                        logger.debug("Pint Sales Order Data End...");

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    logger.error("\"Failed to Print: printSaleOrder()");
                }
            };
            AlertUtility.AlertConfirmation(AlertUtility.alertTypeConfirmation, "Do you want to Print ?", callback);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void handlePrintAction(Stage primaryStage, JsonObject jsonObject) {
//        primaryStage.getScene().getWindow()
//        String path=primaryStage.getScene().getClass().getResource("log4j2.xml").getPath();
//        System.out.println("ImgPath=>"+path);
        WebView webView = new WebView();
        webView.setMaxWidth(1400);
        webView.setMaxHeight(800);
        WebEngine webEngine = webView.getEngine();
        BillFormatDTO responseBody = new Gson().fromJson(jsonObject.getAsJsonObject(), BillFormatDTO.class);
        String supplierCompanyName = responseBody.getSupplierData().getCompanyName();
        String supplierCompanyAddress = responseBody.getSupplierData().getCompanyAddress();
        String supplierPhoneNumber = String.valueOf(responseBody.getSupplierData().getPhoneNumber());
        String supplierEmailAddress = responseBody.getSupplierData().getEmailAddress();
        String supplierGstNumber = responseBody.getSupplierData().getGstNumber();
        String customerSupplierName = responseBody.getCustomerData().getSupplierName();
        String customerSupplierAddress = responseBody.getCustomerData().getSupplierAddress();
        Object customerSupplierGSTIN = responseBody.getCustomerData().getSupplierGstin();
        String customerSupplierPhone = String.valueOf(responseBody.getCustomerData().getSupplierPhone());
        String invoiceId = String.valueOf(responseBody.getInvoiceData().getId());
        String invoiceDate = responseBody.getInvoiceData().getInvoiceDt();
        String invoiceNo = String.valueOf(responseBody.getInvoiceData().getInvoiceNo());
        String invoiceStateCode = responseBody.getInvoiceData().getStateCode();
        String invoiceStateName = responseBody.getInvoiceData().getStateName();
        String invoiceTaxableAmt = String.valueOf(responseBody.getInvoiceData().getTaxableAmt());
        String invoiceTaxAmount = String.valueOf(responseBody.getInvoiceData().getTaxAmount());
        String invoiceTotalCGST = String.valueOf(responseBody.getInvoiceData().getTotalCgst());
        String invoiceTotalSGST = String.valueOf(responseBody.getInvoiceData().getTotalSgst());
        String invoiceNetAmount = String.valueOf(responseBody.getInvoiceData().getNetAmount());
        String invoiceTotalAmount = String.valueOf(responseBody.getInvoiceData().getTotalAmount());
        String message = responseBody.getMessage();
        String responseStatus = String.valueOf(responseBody.getResponseStatus());

        String productId = responseBody.getProductDetails().toString();
        String productName = responseBody.getProductDetails().toString();
        String productDetailsId = responseBody.getProductDetails().toString();
        String productUnitConv = responseBody.getProductDetails().toString();
        String productQty = responseBody.getProductDetails().toString();
        String productRate = responseBody.getProductDetails().toString();
        String productBaseAmt = responseBody.getProductDetails().toString();
        String productFinalAmt = responseBody.getProductDetails().toString();
        String productGst = responseBody.getProductDetails().toString();
        String productPackageId = responseBody.getProductDetails().toString();
        String productPackName = responseBody.getProductDetails().toString();
        String productFlavourId = responseBody.getProductDetails().toString();
        String productFlavourName = responseBody.getProductDetails().toString();
        String productBDetailsId = responseBody.getProductDetails().toString();
        String productBNo = responseBody.getProductDetails().toString();
        String productIsBatch = responseBody.getProductDetails().toString();
        String productUnitId = responseBody.getProductDetails().toString();
        String productUnitName = responseBody.getProductDetails().toString();



        String tabledata = "";

// Assuming responseBody.getProductDetails() returns a list of BillFormatProductDetailsDTO
        List<BillFormatProductDetailsDTO> products = responseBody.getProductDetails();
//        System.out.println(products);

        for (int i = 0; i < responseBody.getProductDetails().size(); i++) {
            BillFormatProductDetailsDTO product = products.get(i);
//            System.out.println(product);

            // Using i as the index number
            tabledata += "<tr>\n" +
                    "    <td style=\"border-top:1px solid #333; font-size:8px; border-right:1px solid #333; border-bottom:1px solid #333; text-align:center;\"></td>\n" +
                    "    <td style=\"border-top:1px solid #333; font-size:8px; border-right:1px solid #333; border-bottom:1px solid #333; text-align:center;\">" + product.getQty() + "</td>\n" +
                    "    <td style=\"border-top:1px solid #333; font-size:8px; border-right:1px solid #333; border-bottom:1px solid #333; text-align:center;\"></td>\n" +
                    "    <td style=\"border-top:1px solid #333; font-size:8px; border-right:1px solid #333; border-bottom:1px solid #333; text-align:center;\">" + product.getProductName() + "</td>\n" +
                    "    <td style=\"border-top:1px solid #333; font-size:8px; border-right:1px solid #333; border-bottom:1px solid #333; text-align:center;\">" + product.getPackName() + "</td>\n" +
                    "    <td style=\"border-top:1px solid #333; font-size:8px; border-right:1px solid #333; border-bottom:1px solid #333; text-align:center;\">" + product.getbNo() + "</td>\n" +
                    "    <td style=\"border-top:1px solid #333; font-size:8px; border-right:1px solid #333; border-bottom:1px solid #333; text-align:center;\"></td>\n" +
                    "    <td style=\"border-top:1px solid #333; font-size:8px; border-right:1px solid #333; border-bottom:1px solid #333; text-align:center;\">30049099</td>\n" +
                    "    <td style=\"border-top:1px solid #333; font-size:8px; border-right:1px solid #333; border-bottom:1px solid #333; text-align:center;\">1,280.00</td>\n" +
                    "    <td style=\"border-top:1px solid #333; font-size:8px; border-right:1px solid #333; border-bottom:1px solid #333; text-align:center;\">" + product.getRate() + "</td>\n" +
                    "    <td style=\"border-top:1px solid #333; font-size:8px; border-right:1px solid #333; border-bottom:1px solid #333; text-align:center;\">0.00</td>\n" +
                    "    <td style=\"border-top:1px solid #333; font-size:8px; border-right:1px solid #333; border-bottom:1px solid #333; text-align:center;\">" + product.getGst() + "</td>\n" +
//                    "    <td style=\"border-top:1px solid #333; font-size:8px; border-right:1px solid #333; border-bottom:1px solid #333; text-align:center;\"></td>\n" +
                    "    <td style=\"border-top:1px solid #333; font-size:8px; border-right:1px solid #333; border-bottom:1px solid #333; text-align:center;\">" + product.getFinalAmt() + "</td>\n" +
                    "    <td style=\"border-top:1px solid #333; font-size:8px; border-bottom:1px solid #333; text-align:center;\">" + product.getFinalAmt() + "</td>\n" +
                    "</tr>\n";

            if (i > 0) {
                tabledata += "+";
            }
        }

// Now tabledata contains all rows appended
        System.out.println(tabledata);
        System.out.println(tabledata.toString());
        System.out.println("Bill Format Company Name" + supplierCompanyName);
        // Load HTML content
        String imagePath="file:///E:/Pune/genivis-fx-ui/src/main/resources/com/opethic/genivis/ui/assets/billformatlogo.png";
        String htmlContent = "<!DOCTYPE html>\n" +
                "<html lang=\"en\">\n" +
                "<body style=\"font-size:11.5px;width:730px;height:650px;margin:0px;padding:0px;\">\n" +
                "        <p style=\"text-align:center;margin:0;font-weight:bold;font-size:10px\">GST INVOICE</p>\n" +

                "    <div style=\"border:1px solid;\">\n" +
                "        <div style=\"display: flex; justify-content: space-between;\">\n" +
                "            <div style=\"flex: 0.4; padding:5px; text-align: center; border-right:1px solid;\">\n" +
                "            </div>\n" +
                "            <div style=\"flex: 0.6; padding:2px; border-right:1px solid;font-size:8px\">\n" +
                "                <div style=\"margin:0;font-size:8px\">" + supplierCompanyName + "</div>\n" +
                "                <div style=\"font-size:8px\">" + supplierCompanyAddress + "</div>\n" +
                "                <div style=\"font-size:8px\"></div>\n" +
                "                <div style=\"font-size:8px\">Phone Number : <span style=\"font-size:8px\">" + supplierPhoneNumber + "</span></div>\n" +
                "                <div style=\"font-size:8px\">E-Mail : " + supplierEmailAddress + "</div>\n" +
                "                <div style=\"border-top:1px solid; border-bottom:1px solid;font-size:8px\">D.L.NO.: <span style=\"font-size:8px\">21B-HR-11-3418-W/H / 20B-HR-11-3418</span></div>\n" +
                "                <div style=\"font-size:8px\">GSTIN : <span style=\"font-size:8px\">" + supplierGstNumber + "</span></div>\n" +
                "            </div>\n" +
                "            <div style=\"flex: 1; padding:2px; border-right:1px solid;\">\n" +
                "                <div style=\"font-size:8px\">TO," + customerSupplierName + "</div>\n" +
                "                <div style=\"font-size:8px\">" + customerSupplierName + "</div>\n" +
                "                <div style=\"margin-top:25px;font-size:8px\">" + customerSupplierPhone + "</div>\n" +
                "                <div style=\"font-size:8px\">D.L.No.: <span style=\"font-size:8px\">20B-MH-SOL-383047 21B-MH-SOL-383048</span></div>\n" +
                "                <div style=\"font-size:8px\">GSTIN : <span style=\"font-size:8px\">" + customerSupplierGSTIN + "</span></div>\n" +
                "                <div style=\"font-size:8px\">DUE DATE : <span style=\"font-size:8px\">25-07-2023</span></div>\n" +
                "                <div style=\"font-size:8px\">DUE BAL.: <span style=\"font-size:8px\">18924.00</span></div>\n" +
                "            </div>\n" +
                "            <div style=\"flex: 0.4; padding:2px;\">\n" +
                "                <div style=\"font-size:8px\">Inv No. : <span style=\"font-size:8px\">" + invoiceNo + "</span></div>\n" +
                "                <div style=\"font-size:8px\">Inv Date : <span style=\"font-size:8px\">" + invoiceDate + "</span></div>\n" +
                "                <div style=\"font-size:8px\">Rep Name : <span style=\"font-size:8px\"></span></div>\n" +
                "                <div style=\"font-size:8px\">Mobile No : <span style=\"font-size:8px\"></span></div>\n" +
                "                <div style=\"font-size:8px\">L.R. NO : <span style=\"font-size:8px\"></span></div>\n" +
                "                <div style=\"font-size:8px\">L.R. Dt : <span style=\"font-size:8px\">25-07-2023</span></div>\n" +
                "                <div style=\"font-size:8px\">No of case : <span style=\"font-size:8px\">0</span></div>\n" +
                "                <div style=\"font-size:8px\">Transport : <span style=\"font-size:8px\"></span></div>\n" +
                "                <div style=\"font-size:8px\">Way Bill No. : <span style=\"font-size:8px\"></span></div>\n" +
                "            </div>\n" +
                "        </div>\n" +
                "        <div>\n" +
                "            <table style=\"width:100%; border: 1px solid transparent; border-collapse: collapse;\">\n" +
                "                <thead>\n" +
                "                    <tr>\n" +
                "                        <th style=\"border-top:1px solid #333; font-size:8px; border-right:1px solid #333; border-bottom:1px solid #333; background-color:#c2fafa; text-align:center;\">MFR</th>\n" +
                "                        <th style=\"border-top:1px solid #333; font-size:8px; border-right:1px solid #333; border-bottom:1px solid #333; background-color:#c2fafa; text-align:center;\">QTY</th>\n" +
                "                        <th style=\"border-top:1px solid #333; font-size:8px; border-right:1px solid #333; border-bottom:1px solid #333; background-color:#c2fafa; text-align:center;\">FREE</th>\n" +
                "                        <th style=\"border-top:1px solid #333; font-size:8px; border-right:1px solid #333; border-bottom:1px solid #333; background-color:#c2fafa; text-align:center;\">ITEM NAME</th>\n" +
                "                        <th style=\"border-top:1px solid #333; font-size:8px; border-right:1px solid #333; border-bottom:1px solid #333; background-color:#c2fafa; text-align:center;\">PACK</th>\n" +
                "                        <th style=\"border-top:1px solid #333; font-size:8px; border-right:1px solid #333; border-bottom:1px solid #333; background-color:#c2fafa; text-align:center;\">BATCH</th>\n" +
                "                        <th style=\"border-top:1px solid #333; font-size:8px; border-right:1px solid #333; border-bottom:1px solid #333; background-color:#c2fafa; text-align:center;\">EXP</th>\n" +
                "                        <th style=\"border-top:1px solid #333; font-size:8px; border-right:1px solid #333; border-bottom:1px solid #333; background-color:#c2fafa; text-align:center;\">HSN</th>\n" +
                "                        <th style=\"border-top:1px solid #333; font-size:8px; border-right:1px solid #333; border-bottom:1px solid #333; background-color:#c2fafa; text-align:center;\">M.R.P.</th>\n" +
                "                        <th style=\"border-top:1px solid #333; font-size:8px; border-right:1px solid #333; border-bottom:1px solid #333; background-color:#c2fafa; text-align:center;\">RATE</th>\n" +
                "                        <th style=\"border-top:1px solid #333; font-size:8px; border-right:1px solid #333; border-bottom:1px solid #333; background-color:#c2fafa; text-align:center;\">DIS</th>\n" +
                "                        <th style=\"border-top:1px solid #333; font-size:8px; border-right:1px solid #333; border-bottom:1px solid #333; background-color:#c2fafa; text-align:center;\">IGST</th>\n" +
//                "                        <th style=\"border-top:1px solid #333; font-size:8px; border-right:1px solid #333; border-bottom:1px solid #333; background-color:#c2fafa; text-align:center;\"></th>\n" +
                "                        <th style=\"border-top:1px solid #333; font-size:8px; border-right:1px solid #333; border-bottom:1px solid #333; background-color:#c2fafa; text-align:center;\">AMOUNT</th>\n" +
                "                        <th style=\"border-top:1px solid #333; font-size:8px; border-bottom:1px solid #333; background-color:#c2fafa; text-align:center;\">NET AMOUNT</th>\n" +
                "                    </tr>\n" +
                "                </thead>\n" +
                "                <tbody>\n" +
                "                   " + tabledata +
                "                </tbody>\n" +
                "            </table>\n" +
                "        </div>\n" +
                "        <div style=\"display: flex; justify-content: space-between;\">\n" +
                "            <div style=\"flex: 1; padding:2px;\">\n" +
                "                <div style=\"font-size:8px\">FLASH : <span style=\"font-size:8px\"></span></div>\n" +
                "            </div>\n" +
                "            <div style=\"flex: 1; padding:2px;\">\n" +
                "                <div style=\"font-size:8px\">MRP VALUE TOTAL : <span style=\"font-size:8px\">1,68,960.00</span></div>\n" +
                "            </div>\n" +
                "        </div>\n" +
                "        <div style=\"display: flex; justify-content: space-between; border-top:1px solid; border-bottom:2px solid;\">\n" +
                "            <div style=\"flex: 1;\">\n" +
                "                <div style=\"display: flex; justify-content: space-between;\">\n" +
                "                    <div style=\"flex: 1;\">\n" +
                "                        <table style=\"width:100%; border: 1px solid transparent; border-collapse: collapse;\">\n" +
                "                            <thead>\n" +
                "                                <tr>\n" +
                "                                    <th style=\"border-right:1px solid #333; font-size:8px; border-bottom:1px solid #333; text-align:center;\">CLASS</th>\n" +
                "                                    <th style=\"border-right:1px solid #333; font-size:8px; border-bottom:1px solid #333; text-align:center;\">SUB TOTAL</th>\n" +
                "                                    <th style=\"border-right:1px solid #333; font-size:8px; border-bottom:1px solid #333; text-align:center;\">SCHEME</th>\n" +
                "                                    <th style=\"border-right:1px solid #333; font-size:8px; border-bottom:1px solid #333; text-align:center;\">DISCOUNT</th>\n" +
                "                                    <th style=\"border-right:1px solid #333; font-size:8px; border-bottom:1px solid #333; text-align:center;\">IGST</th>\n" +
                "                                    <th style=\"border-right:1px solid #333; font-size:8px; border-bottom:1px solid #333; text-align:center;\"></th>\n" +
                "                                    <th style=\"border-right:1px solid #333; font-size:8px; border-bottom:1px solid #333; text-align:center;\">TOTAL IGST</th>\n" +
                "                                </tr>\n" +
                "                            </thead>\n" +
                "                            <tbody>\n" +
                "                                <tr>\n" +
                "                                    <td style=\"border-right:1px solid #333; font-size:8px; border-bottom:1px solid #333; text-align:center;\">IGST 5%</td>\n" +
                "                                    <td style=\"border-right:1px solid #333; font-size:8px; border-bottom:1px solid #333; text-align:center;\">0.00</td>\n" +
                "                                    <td style=\"border-right:1px solid #333; font-size:8px; border-bottom:1px solid #333; text-align:center;\">0.00</td>\n" +
                "                                    <td style=\"border-right:1px solid #333; font-size:8px; border-bottom:1px solid #333; text-align:center;\">0.00</td>\n" +
                "                                    <td style=\"border-right:1px solid #333; font-size:8px; border-bottom:1px solid #333; text-align:center;\">0.00</td>\n" +
                "                                    <td style=\"border-right:1px solid #333; font-size:8px; border-bottom:1px solid #333; text-align:center;\">0.00</td>\n" +
                "                                    <td style=\"border-right:1px solid #333; font-size:8px; border-bottom:1px solid #333; text-align:center;\">0.00</td>\n" +
                "                                </tr>\n" +
                "                                <tr>\n" +
                "                                    <td style=\"border-right:1px solid #333; font-size:8px; border-bottom:1px solid #333; text-align:center;\">IGST 12%</td>\n" +
                "                                    <td style=\"border-right:1px solid #333; font-size:8px; border-bottom:1px solid #333; text-align:center;\">16,896.00</td>\n" +
                "                                    <td style=\"border-right:1px solid #333; font-size:8px; border-bottom:1px solid #333; text-align:center;\">0.00</td>\n" +
                "                                    <td style=\"border-right:1px solid #333; font-size:8px; border-bottom:1px solid #333; text-align:center;\">0.00</td>\n" +
                "                                    <td style=\"border-right:1px solid #333; font-size:8px; border-bottom:1px solid #333; text-align:center;\">2,027.52</td>\n" +
                "                                    <td style=\"border-right:1px solid #333; font-size:8px; border-bottom:1px solid #333; text-align:center;\">0.00</td>\n" +
                "                                    <td style=\"border-right:1px solid #333; font-size:8px; border-bottom:1px solid #333; text-align:center;\">2,027.52</td>\n" +
                "                                </tr>\n" +
                "                                <tr>\n" +
                "                                    <td style=\"border-right:1px solid #333; font-size:8px; border-bottom:1px solid #333; text-align:center;\">IGST 5%</td>\n" +
                "                                    <td style=\"border-right:1px solid #333; font-size:8px; border-bottom:1px solid #333; text-align:center;\">0.00</td>\n" +
                "                                    <td style=\"border-right:1px solid #333; font-size:8px; border-bottom:1px solid #333; text-align:center;\">0.00</td>\n" +
                "                                    <td style=\"border-right:1px solid #333; font-size:8px; border-bottom:1px solid #333; text-align:center;\">0.00</td>\n" +
                "                                    <td style=\"border-right:1px solid #333; font-size:8px; border-bottom:1px solid #333; text-align:center;\">0.00</td>\n" +
                "                                    <td style=\"border-right:1px solid #333; font-size:8px; border-bottom:1px solid #333; text-align:center;\">0.00</td>\n" +
                "                                    <td style=\"border-right:1px solid #333; font-size:8px; border-bottom:1px solid #333; text-align:center;\">0.00</td>\n" +
                "                                </tr>\n" +
                "                                <tr>\n" +
                "                                    <td style=\"border-right:1px solid #333; font-size:8px; border-bottom:1px solid #333; text-align:center;\">IGST 5%</td>\n" +
                "                                    <td style=\"border-right:1px solid #333; font-size:8px; border-bottom:1px solid #333; text-align:center;\">0.00</td>\n" +
                "                                    <td style=\"border-right:1px solid #333; font-size:8px; border-bottom:1px solid #333; text-align:center;\">0.00</td>\n" +
                "                                    <td style=\"border-right:1px solid #333; font-size:8px; border-bottom:1px solid #333; text-align:center;\">0.00</td>\n" +
                "                                    <td style=\"border-right:1px solid #333; font-size:8px; border-bottom:1px solid #333; text-align:center;\">0.00</td>\n" +
                "                                    <td style=\"border-right:1px solid #333; font-size:8px; border-bottom:1px solid #333; text-align:center;\">0.00</td>\n" +
                "                                    <td style=\"border-right:1px solid #333; font-size:8px; border-bottom:1px solid #333; text-align:center;\">0.00</td>\n" +
                "                                </tr>\n" +
                "                            </tbody>\n" +
                "                            <tfoot>\n" +
                "                                <tr>\n" +
                "                                    <th style=\"border-right:1px solid #333; font-size:8px; border-bottom:1px solid #333; text-align:center;\">SUB TOTAL</th>\n" +
                "                                    <th style=\"border-right:1px solid #333; font-size:8px; border-bottom:1px solid #333; text-align:center;\">16,896.00</th>\n" +
                "                                    <th style=\"border-right:1px solid #333; font-size:8px; border-bottom:1px solid #333; text-align:center;\">0.00</th>\n" +
                "                                    <th style=\"border-right:1px solid #333; font-size:8px; border-bottom:1px solid #333; text-align:center;\">0.00</th>\n" +
                "                                    <th style=\"border-right:1px solid #333; font-size:8px; border-bottom:1px solid #333; text-align:center;\">2,027.52</th>\n" +
                "                                    <th style=\"border-right:1px solid #333; font-size:8px; border-bottom:1px solid #333; text-align:center;\">0.00</th>\n" +
                "                                    <th style=\"border-right:1px solid #333; font-size:8px; border-bottom:1px solid #333; text-align:center;\">2,027.52</th>\n" +
                "                                </tr>\n" +
                "                            </tfoot>\n" +
                "                        </table>\n" +
                "                    </div>\n" +
                "                    <div style=\"flex: 0.4;\"></div>\n" +
                "                </div>\n" +
                "                <div style=\"border-top:1px solid; padding:5px;font-size:8px\">Rs. Eighteen Thousand Nine Hundred Twenty Four Only</div>\n" +
                "            </div>\n" +
                "            <div style=\"flex: 0.2; padding:2px; border-left:1px solid;\">\n" +
                "                <div style=\"display:flex; border-bottom:1px solid;\">\n" +
                "                    <div style=\"font-size:8px\">SUB TOTAL</div>\n" +
                "                    <div style=\"margin-left:auto;font-size:8px\">" + invoiceNetAmount + "</div>\n" +
                "                </div>\n" +
                "                <div style=\"display:flex;\">\n" +
                "                    <div style=\"font-size:8px\">DISCOUNT</div>\n" +
                "                    <div style=\"margin-left:auto;font-size:8px\">0.00</div>\n" +
                "                </div>\n" +
                "                <div style=\"display:flex;\">\n" +
                "                    <div style=\"font-size:8px\">IGST PAYABLE</div>\n" +
                "                    <div style=\"margin-left:auto;font-size:8px\">2,027.52</div>\n" +
                "                </div>\n" +
                "                <div style=\"display:flex;\">\n" +
                "                    <div style=\"font-size:8px\">PAYABLE</div>\n" +
                "                    <div style=\"margin-left:auto;font-size:8px\">0.00</div>\n" +
                "                </div>\n" +
                "                <div style=\"display:flex;\">\n" +
                "                    <div style=\"font-size:8px\">CR/DR NOTE</div>\n" +
                "                    <div style=\"margin-left:auto;font-size:8px\">0.00</div>\n" +
                "                </div>\n" +
                "                <div style=\"display:flex; border-top:1px solid; background-color:#c2fafa;\">\n" +
                "                    <div style=\"font-size:8px\">GRAND TOTAL</div>\n" +
                "                    <div style=\"margin-left:auto;font-size:8px\">" + invoiceTotalAmount + "</div>\n" +
                "                </div>\n" +
                "            </div>\n" +
                "        </div>\n" +
                "        <div style=\"display: flex; justify-content: space-between;\">\n" +
                "            <div style=\"flex: 1; padding:2px; border-right:1px solid;\">\n" +
                "                <div style=\"font-size:8px\">Tearms & Conditions</div>\n" +
                "                <div style=\"font-size:8px\">Goods once sold will not be taken back or exchanged.</div>\n" +
                "                <div style=\"font-size:8px\">Bills not paid due date will attract 24% interest.</div>\n" +
                "                <div style=\"font-size:8px\">All disputes subject to PANCHKULA jurisdication only.</div>\n" +
                "            </div>\n" +
                "            <div style=\"flex: 0.5; padding:2px; border-right:1px solid;\">\n" +
                "                <div style=\"color:#3e348d; font-weight:600; font-size:8px;\">BANK DETAILS</div>\n" +
                "                <div style=\"color:#3e348d; font-weight:600; font-size:8px;\">Bank Name : <span>Kotak Mahindra Bank</span></div>\n" +
                "                <div style=\"color:#3e348d; font-weight:600; font-size:8px;\">A/C No. : <span>9864564565446</span></div>\n" +
                "                <div style=\"color:#3e348d; font-weight:600; font-size:8px;\">IFSC Code : <span>KKKID00007</span></div>\n" +
                "                <div style=\"color:#3e348d; font-weight:600; font-size:8px;\">Branch : <span>PANCHKULA</span></div>\n" +
                "            </div>\n" +
                "            <div style=\"flex: 0.6; padding:2px; border-right:1px solid; text-align:center;\">\n" +
                "                <div style=\"font-size:8px\">RECEIVER</div>\n" +
                "            </div>\n" +
                "            <div style=\"flex: 0.8; padding:2px; text-align:center;\">\n" +
                "                <div style=\"font-size:8px\">For MANWELL PHARMACEUTICALS</div>\n" +
                "                <div style=\"font-size:8px\">Authorised signatory</div>\n" +
                "            </div>\n" +
                "        </div>\n" +
                "    </div>\n" +
                "</div>\n" +
                "</body>\n" +
                "</html>\n";

        // Set up the scene and add the WebView
        StackPane root = new StackPane();
        root.getChildren().add(webView);
        Scene scene = new Scene(root, 1200, 600);
        primaryStage.setScene(scene);
        primaryStage.show();

//        div1.getChildren().add(webView);
//        div1.maxWidth(800);
//        div1.maxHeight(600);
//        div1.setManaged(true);
        // Define the format for the date and time
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");
        // Get the current date and time
        LocalDateTime now = LocalDateTime.now();
        // Format the current date and time
        String formattedDateTime = now.format(formatter);
        // Create the output file name
        String outputPath = formattedDateTime + ".pdf";
        // Output the result
        System.out.println(outputPath);

        //System.out.println("PDF created successfully!");

//        try (FileOutputStream fos = new FileOutputStream(outputPath)) {
//           // HtmlConverter.convertToPdf(htmlContent, fos);
//            System.out.println("PDF saved successfully.");
//        } catch (IOException e) {
//            e.printStackTrace();
//            System.err.println("Failed to save PDF.");
//        }
        // Load the HTML content
        webEngine.loadContent(htmlContent);

        // Wait for the WebEngine to load the content
        webEngine.getLoadWorker().stateProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == Worker.State.SUCCEEDED) {
                // Create a PrinterJob
                PrinterJob job = PrinterJob.createPrinterJob();
                if (job != null && job.showPrintDialog(webView.getScene().getWindow())) {
                    // Print the WebView node

                    boolean success = job.printPage(webView);
                    if (success) {
                        job.endJob();
                        System.out.println("PDF saved successfully.");
                    }
                } else {
                    // Handle the print job creation failure
//                    showAlert(AlertType.ERROR, "Error", "Failed to create a print job.");
                    System.err.println("Failed to save PDF.");
                }
            }
        });
    }
    //? Bill Format Function END
}