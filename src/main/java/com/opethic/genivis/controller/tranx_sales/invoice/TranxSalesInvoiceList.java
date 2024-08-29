package com.opethic.genivis.controller.tranx_sales.invoice;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.opethic.genivis.GenivisApplication;
import com.opethic.genivis.controller.tranx_purchase.PurchaseInvoiceEditController;
import com.opethic.genivis.dto.PurchaseInvoiceDTO;
import com.opethic.genivis.dto.bill_format.BillFormatDTO;
import com.opethic.genivis.dto.bill_format.BillFormatProductDetailsDTO;
import com.opethic.genivis.dto.master.ledger.LedgerListDTO;
import com.opethic.genivis.dto.reqres.product.Communicator;
import com.opethic.genivis.dto.reqres.sales_tranx.CounterResDTO;
import com.opethic.genivis.dto.reqres.sales_tranx.TranxSalesInvoiceListResDTO;
import com.opethic.genivis.models.tranx.sales.TranxSalesInvoiceListMDL;
import com.opethic.genivis.network.APIClient;
import com.opethic.genivis.network.EndPoints;
import com.opethic.genivis.network.RequestType;
import com.opethic.genivis.utils.*;
import javafx.application.Platform;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Worker;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.print.PrinterJob;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.web.WebEngine;
import javafx.stage.Screen;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URL;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.scene.web.WebView;

import static com.opethic.genivis.utils.FxmFileConstants.*;

public class TranxSalesInvoiceList implements Initializable {

    private static final Logger logger = LogManager.getLogger(TranxSalesInvoiceList.class);
    @FXML
    private BorderPane bpRootPane;

    @FXML
    private TableColumn<TranxSalesInvoiceListMDL, Void> tcAction;

    @FXML
    private TableColumn<?, ?> tcBillAmt;

    @FXML
    private TableColumn<?, ?> tcClientName;

    @FXML
    private TableColumn<?, ?> tcInvoiceNo;

    @FXML
    private TableColumn<?, ?> tcNarration;

    @FXML
    private TableColumn<?, ?> tcPayMode;

    @FXML
    private TableColumn<?, ?> tcPayStatus;

    @FXML
    private TableColumn<TranxSalesInvoiceListMDL, Void> tcPrint;

    @FXML
    private TableColumn<?, ?> tcSIDate;

    @FXML
    private TableColumn<?, ?> tcTax;

    @FXML
    private TableColumn<?, ?> tcTaxable;

    @FXML
    private TableColumn<?, ?> tcTranxId;

    @FXML
    private TextField tfSearch,tfFromDate, tfToDate;
    @FXML
    private Button btnCreate;
    private Integer id1;
    private JsonObject jsonObject = null;
    private String message;

    @FXML
    private TableView<TranxSalesInvoiceListMDL> tvSalesInvoiceList;
    ObservableList<TranxSalesInvoiceListMDL> lstSalesInvoice = FXCollections.observableArrayList();
    public static Integer purchase_id = -1;

    public static Integer getPurchaseId() {
        return purchase_id;
    }

    //? Highlight the Record Start
    public static boolean isNewSalesInvoiceCreated = false; // Flag for new creation
    public static String editedSalesInvoiceId = null; // ID for edited franchise
    //? Highlight the Record End

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        ResponsiveWiseCssPicker();
        Platform.runLater(() ->{
            //? set the Focus on the first field of the page
            tfSearch.requestFocus();
        });

        //Set the TableView Columns with proper height and Width
        tvSalesInvoiceList.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        initSalesInvoiceListTable();
        initShortcutKeys();

        //SI testing issue start
        DateValidator.applyDateFormat(tfFromDate);
        DateValidator.applyDateFormat(tfToDate);
        tfSearch.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.TAB || event.getCode() == KeyCode.ENTER){
                tfFromDate.requestFocus();
            }
        });
        tfFromDate.setOnKeyPressed(event -> {
            if (event.isShiftDown() && event.getCode() == KeyCode.TAB) {
                tfSearch.requestFocus();
            } else if (event.getCode() == KeyCode.TAB || event.getCode() == KeyCode.ENTER) {
                tfToDate.requestFocus();
            } else if (event.getCode() == KeyCode.DOWN) {
                tvSalesInvoiceList.getSelectionModel().select(0);
                tvSalesInvoiceList.requestFocus();
            }
            event.consume();
        });
        tfToDate.setOnKeyPressed(event -> {
            if (event.isShiftDown() && event.getCode() == KeyCode.TAB) {
                tfFromDate.requestFocus();
            } else if (event.getCode() == KeyCode.TAB || event.getCode() == KeyCode.ENTER) {
                getSalesInvoiceList("");
                btnCreate.requestFocus();
            } else if (event.getCode() == KeyCode.DOWN) {
                tvSalesInvoiceList.getSelectionModel().select(0);
                tvSalesInvoiceList.requestFocus();
            }
            event.consume();
        });
        //SI testing issue end
        //Redirect to Create Page from List Page on CLick on the Create Button and in the Same Tab of TabPane
        btnCreate.setOnAction( event ->{
            GlobalController.getInstance().addTabStatic(SALES_INVOICE_CREATE_SLUG,false);
        });

        //SI testing issue start
        btnCreate.addEventFilter(KeyEvent.KEY_PRESSED, event ->  {
            System.out.println("event==>> "+event.getCode());
            if (event.getCode() == KeyCode.ENTER){
                GlobalController.getInstance().addTabStatic(SALES_INVOICE_CREATE_SLUG,false);
            }else if (event.isShiftDown() && event.getCode() == KeyCode.TAB){
                tfToDate.requestFocus();
            } else if(event.getCode()==KeyCode.DOWN){
                tvSalesInvoiceList.getSelectionModel().select(0);
                tvSalesInvoiceList.requestFocus();
            }
            event.consume();
        });
        //SI testing issue end
    }

    private void ResponsiveWiseCssPicker() {
        double height = Screen.getPrimary().getBounds().getHeight();
        double width = Screen.getPrimary().getBounds().getWidth();
        System.out.println("width" + width);
        if (width >= 992 && width <= 1024) {
            bpRootPane.getStylesheets().add(GenivisApplication.class.getResource("ui/css/TranxCommonCssStyles/tranxCommonCssStyle1.css").toExternalForm());
        } else if (width >= 1025 && width <= 1280) {
            bpRootPane.getStylesheets().add(GenivisApplication.class.getResource("ui/css/TranxCommonCssStyles/tranxCommonCssStyle2.css").toExternalForm());
        } else if (width >= 1281 && width <= 1368) {
            bpRootPane.getStylesheets().add(GenivisApplication.class.getResource("ui/css/TranxCommonCssStyles/tranxCommonCssStyle3.css").toExternalForm());
        } else if (width >= 1369 && width <= 1400) {
            bpRootPane.getStylesheets().add(GenivisApplication.class.getResource("ui/css/TranxCommonCssStyles/tranxCommonCssStyle4.css").toExternalForm());
        } else if (width >= 1401 && width <= 1440) {
            bpRootPane.getStylesheets().add(GenivisApplication.class.getResource("ui/css/TranxCommonCssStyles/tranxCommonCssStyle5.css").toExternalForm());
        } else if (width >= 1441 && width <= 1680) {
            bpRootPane.getStylesheets().add(GenivisApplication.class.getResource("ui/css/TranxCommonCssStyles/tranxCommonCssStyle6.css").toExternalForm());
        } else if (width >= 1681 && width <= 1920) {
            bpRootPane.getStylesheets().add(GenivisApplication.class.getResource("ui/css/TranxCommonCssStyles/tranxCommonCssStyle7.css").toExternalForm());
        }
    }

    private void initShortcutKeys() {
            //         Enter traversal
            bpRootPane.addEventFilter(KeyEvent.KEY_PRESSED, (KeyEvent event) -> {
                if (event.getCode() == KeyCode.ENTER) {
                    if (tvSalesInvoiceList.isFocused()) {
                        System.out.println("edit enter clicked id => " );
                        TranxSalesInvoiceListMDL selectedItem = (TranxSalesInvoiceListMDL) tvSalesInvoiceList.getSelectionModel().getSelectedItem();
                        Integer Id = selectedItem.getId();
                        purchase_id = Id;
                        System.out.println("SalesQuotId 1purid -->" + Id+ " jhgf " + purchase_id);

                        GlobalController.getInstance().addTabStaticWithParam(SALES_INVOICE_EDIT_SLUG, false, Id);

                    }
                    if(event.getTarget() instanceof Button targetButton &&  targetButton.getText().equalsIgnoreCase("create")){

                    }else{
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

                }else if (event.getCode() == KeyCode.N && event.isControlDown()) {
                    btnCreate.fire();
                }else if (event.getCode() == KeyCode.DOWN && tfSearch.isFocused()) {
                    tvSalesInvoiceList.getSelectionModel().select(0);
                    tvSalesInvoiceList.requestFocus();
                } else if (event.getCode() == KeyCode.E && event.isControlDown()) {
//                salesInvoiceEditPage();
                    if (tvSalesInvoiceList.isFocused()) {
                        System.out.println("edit enter clicked id => " );
                        TranxSalesInvoiceListMDL selectedItem = (TranxSalesInvoiceListMDL) tvSalesInvoiceList.getSelectionModel().getSelectedItem();
                        Integer Id = selectedItem.getId();
                        purchase_id = Id;
                        System.out.println("SalesQuotId 1purid -->" + Id+ " jhgf " + purchase_id);

                        GlobalController.getInstance().addTabStaticWithParam(SALES_INVOICE_EDIT_SLUG, false, Id);

                    }
                } else if (event.getCode() == KeyCode.D && event.isControlDown()) {
                    handleCtrlDPressed();
                }
            });

        //code for go to edit page of Purchase Invoice
        tvSalesInvoiceList.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                Integer id = lstSalesInvoice.get(tvSalesInvoiceList.getSelectionModel().getSelectedIndex()).getId();
                System.out.println("edit clicked id => " + id);
                purchase_id = Integer.valueOf(id);
                GlobalController.getInstance().addTabStaticWithParam(SALES_INVOICE_EDIT_SLUG, false, id);
            }
        });
        tvSalesInvoiceList.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                Integer id = lstSalesInvoice.get(tvSalesInvoiceList.getSelectionModel().getSelectedIndex()).getId();
                System.out.println("edit clicked id => " + id);
                purchase_id = Integer.valueOf(id);
                GlobalController.getInstance().addTabStaticWithParam(SALES_INVOICE_EDIT_SLUG, false, id);
            }
        });
    }

    private void initSalesInvoiceListTable() {
        getSalesInvoiceList("");
        tcInvoiceNo.setCellValueFactory(new PropertyValueFactory<>("invoiceNo"));
        tcTranxId.setCellValueFactory(new PropertyValueFactory<>("tranxCode"));
        tcSIDate.setCellValueFactory(new PropertyValueFactory<>("invoiceDate"));
        tcClientName.setCellValueFactory(new PropertyValueFactory<>("sundryDebtorName"));
        tcNarration.setCellValueFactory(new PropertyValueFactory<>("narration"));
        tcTaxable.setCellValueFactory(new PropertyValueFactory<>("taxableAmt"));
        tcTax.setCellValueFactory(new PropertyValueFactory<>("taxAmt"));
        tcBillAmt.setCellValueFactory(new PropertyValueFactory<>("totalAmount"));
        tcPayMode.setCellValueFactory(new PropertyValueFactory<>("paymentMode"));
        tcPrint.setCellFactory(param -> {
            final TableCell<TranxSalesInvoiceListMDL, Void> cell = new TableCell<>() {
                private ImageView printImg = new ImageView(new Image(String.valueOf(GenivisApplication.class.getResource("ui/assets/print.png"))));

                {
                    printImg.setFitHeight(20.0);
                    printImg.setFitWidth(20.0);
                }

                private final Button printButton = new Button("", printImg);

                {
                    printButton.setOnAction(actionEvent -> {
                        TranxSalesInvoiceListMDL id = getTableView().getItems().get(getIndex());
//                        String status = id.getTranxCode();
                        id1 = Integer.valueOf(id.getId());
                        // Check if the status is "opened"
                        if (id1 != null) {
                            System.out.println("i am in");
                            printSaleInvoice(id1);
                        } else {
                            // Show an alert indicating that the order is already closed
                            AlertUtility.CustomCallback callback = (number) -> {
                                if (number == 0) {
                                    tfSearch.requestFocus();
                                }
                            };
                        }
                    });
                }

                HBox hbActions = new HBox();

                {
                    hbActions.getChildren().add(printButton);
                    hbActions.setSpacing(10.0);
                }

                // Set the action for the view button
                @Override
                protected void updateItem(Void item, boolean empty) {
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
//        tcPayStatus.setCellValueFactory(new PropertyValueFactory<>("paymentStatus"));
//        tcAction.setCellFactory(param -> {
//            final TableCell<TranxSalesInvoiceListMDL, Void> cell = new TableCell<>() {
//                private ImageView delImg = new ImageView(new Image(String.valueOf(GenivisApplication.class.getResource("ui/assets/del.png"))));
//                private ImageView edtImg = new ImageView(new Image(String.valueOf(GenivisApplication.class.getResource("ui/assets/edt.png"))));
//
//
//                {
//                    delImg.setFitHeight(20.0);
//                    delImg.setFitWidth(20.0);
//                    edtImg.setFitHeight(20.0);
//                    edtImg.setFitWidth(20.0);
//
//                }
//
//                private final Button actionButton = new Button("", delImg);
//                private final Button edtButton = new Button("", edtImg);
//
//
//                {
//                    edtButton.setOnAction(actionEvent -> {
////                                    System.out.println("ledger id -> " + row.get(getIndex()) != null ? row.get(getIndex()).getId() : null);
//                        Integer id = lstSalesInvoice.get(getIndex()).getId();
//                        System.out.println("edit clicked id => " + id);
//                        purchase_id = Integer.valueOf(id);
////                        PurchaseInvoiceEditController.id = Long.parseLong(purchaseInvoiceId);
//                        GlobalController.getInstance().addTabStaticWithParam(SALES_INVOICE_EDIT_SLUG, false, id);
//                    });
//                }
//
//                HBox hbActions = new HBox();
//
//                {
//                    hbActions.getChildren().add(edtButton);
//                    hbActions.getChildren().add(actionButton);
//                    hbActions.setSpacing(10.0);
//                }
//                // Set the action for the view button
//
//                @Override
//                protected void updateItem(Void item, boolean empty) {
//                    super.updateItem(item, empty);
//                    if (empty) {
//                        setGraphic(null);
//                    } else {
//                        setGraphic(hbActions);
//                    }
//                }
//            };
//            return cell;
//        });
        tvSalesInvoiceList.setItems(lstSalesInvoice);
        //******************************** Highlight on the Created/Edited Record in the List Start ********************************
        if (TranxSalesInvoiceList.isNewSalesInvoiceCreated) {
            tvSalesInvoiceList.getSelectionModel().selectLast();
            tvSalesInvoiceList.scrollTo(tvSalesInvoiceList.getItems().size() - 1);
            TranxSalesInvoiceList.isNewSalesInvoiceCreated = false; // Reset the flag
        } else if (TranxSalesInvoiceList.editedSalesInvoiceId != null) {
            for (TranxSalesInvoiceListMDL salesInvoice : lstSalesInvoice) {
                if (salesInvoice.getId().equals(TranxSalesInvoiceList.editedSalesInvoiceId)) {
                    tvSalesInvoiceList.getSelectionModel().select(salesInvoice);
                    tvSalesInvoiceList.scrollTo(salesInvoice);
                    TranxSalesInvoiceList.editedSalesInvoiceId = null; // Reset the ID
                    break;
                }
            }
        }
        //******************************** Highlight on the Created/Edited Record in the List End ********************************
    }

    @FXML
    private void handleCtrlDPressed() {
        TranxSalesInvoiceListMDL selectedItem = (TranxSalesInvoiceListMDL) tvSalesInvoiceList.getSelectionModel().getSelectedItem();
        Integer Id = selectedItem.getId();

        System.out.println("Selected index to be deleted -->" + Id);
        deleteSaleQuotation(Id);


    }
    public void deleteSaleQuotation(int id) {
        AlertUtility.CustomCallback callback = number -> {
            if (number == 1) {
                try {
                    Map<String, String> map = new HashMap<>();
                    map.put("id", String.valueOf(id));
                    String formData = Globals.mapToStringforFormData(map);
                    HttpResponse<String> response = APIClient.postFormDataRequest(formData, EndPoints.SALES_INVOICE_DELETE_ENDPOINT);
                    String responseBody = response.body();
                    JsonObject jsonObject = new Gson().fromJson(responseBody, JsonObject.class);
                    String message = jsonObject.get("message").getAsString();
                    System.out.println("Delete->" + jsonObject.get("message"));

                    if (jsonObject.get("responseStatus").getAsInt() == 200) {
                        AlertUtility.AlertSuccessTimeout(AlertUtility.alertTypeSuccess, message, input -> {
                            // Update the list after successful deletion
                            lstSalesInvoice.removeIf(item -> item.getId().equals(String.valueOf(id)));
                            getSalesInvoiceList("");
                        });
                    } else {
                        AlertUtility.AlertErrorTimeout(AlertUtility.alertTypeError, message, in -> {
                        });
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                System.out.println("working!");
            }
        };
        AlertUtility.AlertConfirmation(AlertUtility.alertTypeConfirmation, "Do you want to Delete", callback);

    }


    private void getSalesInvoiceList(String searchKey) {
        JSONObject jsonSort = new JSONObject();
        jsonSort.put("colId", "null");
        jsonSort.put("isAsc", "true");
        Map<String, String> body = new HashMap<>();
        body.put("pageNo", "1");
        body.put("pageSize", "50");
        body.put("searchText", searchKey);
//        body.put("sort", jsonSort.toString());
        body.put("sort", "");
        //SI testing issue start
//        System.out.println("fromDate== "+tfFromDate.getText() +"  toDate== "+tfToDate.getText());
        if(tfFromDate.getText().isEmpty()){
            body.put("startDate", "");
        }else{
            body.put("startDate", Communicator.text_to_date.fromString(tfFromDate.getText()).toString());
        }
        if (tfToDate.getText().isEmpty()){
            body.put("endDate", "");
        }else {
            body.put("endDate", Communicator.text_to_date.fromString(tfToDate.getText()).toString());
        }
        //SI testing issue end
        String requestBody = Globals.mapToString(body);
        HttpResponse<String> response = APIClient.postJsonRequest(requestBody, EndPoints.TRANX_SALES_INVOICE_LIST);
        JSONObject resObj = new JSONObject(response.body());
        lstSalesInvoice.clear();
        if (resObj.getInt("responseStatus") == 200) {
            JSONArray jArr = resObj.getJSONObject("responseObject").getJSONArray("data");
            for (Object object : jArr) {
                TranxSalesInvoiceListResDTO tranxSalesInvoiceListResDTO = new Gson().fromJson(object.toString(), TranxSalesInvoiceListResDTO.class);
                lstSalesInvoice.add(new TranxSalesInvoiceListMDL(tranxSalesInvoiceListResDTO.getId(), tranxSalesInvoiceListResDTO.getPaymentMode(), tranxSalesInvoiceListResDTO.getTransactionTrackingNo(), tranxSalesInvoiceListResDTO.getReferenceNo(), tranxSalesInvoiceListResDTO.getReferenceType(), tranxSalesInvoiceListResDTO.getInvoiceNo(), tranxSalesInvoiceListResDTO.getTaxableAmt(), tranxSalesInvoiceListResDTO.getTranxCode(), DateConvertUtil.convertDispDateFormat(tranxSalesInvoiceListResDTO.getInvoiceDate()), tranxSalesInvoiceListResDTO.getSaleSerialNumber(), tranxSalesInvoiceListResDTO.getSundryDebtorId(), tranxSalesInvoiceListResDTO.getSundryDebtorName(), tranxSalesInvoiceListResDTO.getTaxAmt(), tranxSalesInvoiceListResDTO.getNarration(), tranxSalesInvoiceListResDTO.getTotalIgst(), tranxSalesInvoiceListResDTO.getTotalCgst(), tranxSalesInvoiceListResDTO.getTotalSgst(), tranxSalesInvoiceListResDTO.getSaleAccountName(), tranxSalesInvoiceListResDTO.getTotalAmount()));
            }
        }
    }

    //? Bill Format Function START

    //? Print API Function
    public void printSaleInvoice(int id) {
        System.out.println("i am in");
        try {
            AlertUtility.CustomCallback callback = number -> {
                if (number == 1) {
                    APIClient apiClient = null;
                    try {
                        logger.debug("Print Sales Order Data Started...");
                        Map<String, String> map = new HashMap<>();
                        map.put("id", String.valueOf(id));
                        map.put("print_type", "list");
                        map.put("source", "sales_invoice");
                        String formData = Globals.mapToStringforFormData(map);
                        apiClient = new APIClient(EndPoints.SALES_INVOICE_PRINT_ENDPOINT, formData, RequestType.FORM_DATA);

                        apiClient.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
                            @Override
                            public void handle(WorkerStateEvent workerStateEvent) {
                                jsonObject = new Gson().fromJson(workerStateEvent.getSource().getValue().toString(), JsonObject.class);

                                message = jsonObject.get("message").getAsString();
                                System.out.println("i am in Print" + jsonObject);
                                if (jsonObject.get("responseStatus").getAsInt() == 200) {

                                    Stage primaryStage = (Stage) bpRootPane.getScene().getWindow();
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
