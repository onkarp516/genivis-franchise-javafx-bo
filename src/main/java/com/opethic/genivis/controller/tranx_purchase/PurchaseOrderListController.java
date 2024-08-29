package com.opethic.genivis.controller.tranx_purchase;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.opethic.genivis.GenivisApplication;
import com.opethic.genivis.controller.master.CompanyListController;
import com.opethic.genivis.controller.tranx_sales.SalesOrderListController;
import com.opethic.genivis.controller.tranx_sales.SalesQuotToOrderController;
import com.opethic.genivis.dto.*;
import com.opethic.genivis.dto.account_entry.DebitNoteListDTO;
import com.opethic.genivis.dto.reqres.product.Communicator;
import com.opethic.genivis.network.APIClient;
import com.opethic.genivis.network.EndPoints;
import com.opethic.genivis.network.RequestType;
import com.opethic.genivis.utils.*;

import static com.opethic.genivis.dto.reqres.product.Communicator.dateFormatter;
import static com.opethic.genivis.utils.Globals.purchaseOrderDTO;

import javafx.application.Platform;
import javafx.beans.Observable;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
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
import javafx.scene.paint.Color;
import javafx.scene.text.FontWeight;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Callback;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.URL;
import java.net.http.HttpResponse;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static com.opethic.genivis.utils.FxmFileConstants.*;
import static com.opethic.genivis.utils.Globals.salesOrderListDTO;

public class PurchaseOrderListController implements Initializable {

    private JsonObject jsonObject = null;
    private static final Logger logger = LogManager.getLogger(PurchaseOrderListController.class);
    ObservableList<PurchaseOrderDTO> purchaseOrderList = FXCollections.observableArrayList();
    private ObservableList<PurchaseOrderDTO> originalData;
    private String message = "";
    @FXML
    public Button btnPurOrdToInvoice;
    @FXML
    public Button btnPurOrdToChallan;
    @FXML
    private BorderPane bpRootPurOrderListPane;
    @FXML
    TableView<PurchaseOrderDTO> tblvPurOrdLst;

    @FXML
    private TableColumn<PurchaseOrderDTO, Boolean> tblcPurOrdLstSelect;
    @FXML
    private TableColumn<PurchaseOrderDTO, String> tblcPurOrdLstOrderNo, tblcPurOrdLstOrderDate, tblcPurOrdLstSupplierName,
            tblcPurOrdLstNarration, tblcPurOrdLstTaxable, tblcPurOrdLstTax, tblcPurOrdLstBillAmount, tblcPurOrdLstTranxStatus, tblcPurOrdTranxTrackingNo, tblcPurOrdLstPrint, tblcPurOrdLstAction;


    DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    @FXML
    private TextField tftranxPurOrdLstSearch;
    @FXML
    private LocalDate fromDate, toDate;
    @FXML
    private TextField tfPurOrdLstFromDate, tfPurOrdLstToDate;
    @FXML
    private DatePicker dptranxPurOrdLstToDate, dptranxPurOrdLstFromDate;
    @FXML
    private Button btnPurOrdLstCreatebtn;
    ObservableList<PurchaseOrderDTO> observableList;
    int i = 0;

    private Integer selectedIndex = 0;
    public static String focusStatus="create";
    public static Integer selectedRowIndex=-1;
    Integer ListSize = 0;

    //? Highlight the Record Start
    public static boolean isNewPurchaseOrderCreated = false; // Flag for new creation
    public static String editedPurchaseOrderId = null; // ID for edited Sales Order
    //? Highlight the Record End

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        ResponsiveWiseCssPicker();

        //todo : autofocus on search field
        Platform.runLater(() -> tftranxPurOrdLstSearch.requestFocus());

        //? this include all the Shortcut Keys
        initShortcutKeys();

        //? this inculde all the design related properties and important Fields
        madatoryFields();

        //? this include all the table view operations - Edit , Conversions , search , etc..
        tableViewOperations();

        //todo: Search without API Call in the Table
        originalData = tblvPurOrdLst.getItems();
        tftranxPurOrdLstSearch.textProperty().addListener((observable, oldValue, newValue) -> {
            filterData(newValue.trim());
        });

        observableList = FXCollections.observableArrayList();

        // List of all Company Admins Api call
        fetchDataOfAllPurchaseOrders("");

        btnPurOrdLstCreatebtn.setOnAction(event -> GlobalController.getInstance().addTabStatic(PURCHASE_ORDER_CREATE_SLUG, false));


        btnPurOrdToChallan.setOnAction((actionEvent) -> {
            for (PurchaseOrderDTO dto : tblvPurOrdLst.getItems()) {
                if (dto.isIs_row_selected()) {
                    goToPurChallan();
                }
            }
        });

        btnPurOrdToInvoice.setOnAction((acionEvent) -> {
            for (PurchaseOrderDTO dto : tblvPurOrdLst.getItems()) {
                if (dto.isIs_row_selected()) {
                    System.out.println("Invoice Id : " + dto.getId() + " Selected for To Invoice");
                    goToInvoice();
                }
            }
        });

        Platform.runLater(() -> {
            if (focusStatus.equalsIgnoreCase("create")) {
//                        Integer listSize = originalData.size();
//                System.out.println("listSize " + ListSize + " last index " + (ListSize - 1));
                tblvPurOrdLst.getSelectionModel().clearAndSelect(0);
                tblvPurOrdLst.requestFocus();
                tblvPurOrdLst.scrollTo(0);
            } else if (focusStatus.equalsIgnoreCase("edit") && selectedRowIndex != null) {
//                System.out.println("selected Row Index " + selectedRowIndex);
                tblvPurOrdLst.getSelectionModel().clearAndSelect(selectedRowIndex);
                tblvPurOrdLst.requestFocus();
                tblvPurOrdLst.scrollTo(selectedRowIndex);
            } else {
//                System.out.println("not create");
                tftranxPurOrdLstSearch.requestFocus();
            }

        });
    }

    private void ResponsiveWiseCssPicker() {
        double height = Screen.getPrimary().getBounds().getHeight();
        double width = Screen.getPrimary().getBounds().getWidth();
        System.out.println("width" + width);
        if (width >= 992 && width <= 1024) {
            bpRootPurOrderListPane.getStylesheets().add(GenivisApplication.class.getResource("ui/css/TranxCommonCssStyles/tranxCommonCssStyle1.css").toExternalForm());
        } else if (width >= 1025 && width <= 1280) {
            bpRootPurOrderListPane.getStylesheets().add(GenivisApplication.class.getResource("ui/css/TranxCommonCssStyles/tranxCommonCssStyle2.css").toExternalForm());
        } else if (width >= 1281 && width <= 1368) {
            bpRootPurOrderListPane.getStylesheets().add(GenivisApplication.class.getResource("ui/css/TranxCommonCssStyles/tranxCommonCssStyle3.css").toExternalForm());
        } else if (width >= 1369 && width <= 1400) {
            bpRootPurOrderListPane.getStylesheets().add(GenivisApplication.class.getResource("ui/css/TranxCommonCssStyles/tranxCommonCssStyle4.css").toExternalForm());
        } else if (width >= 1401 && width <= 1440) {
            bpRootPurOrderListPane.getStylesheets().add(GenivisApplication.class.getResource("ui/css/TranxCommonCssStyles/tranxCommonCssStyle5.css").toExternalForm());
        } else if (width >= 1441 && width <= 1680) {
            bpRootPurOrderListPane.getStylesheets().add(GenivisApplication.class.getResource("ui/css/TranxCommonCssStyles/tranxCommonCssStyle6.css").toExternalForm());
        } else if (width >= 1681 && width <= 1920) {
            bpRootPurOrderListPane.getStylesheets().add(GenivisApplication.class.getResource("ui/css/TranxCommonCssStyles/tranxCommonCssStyle7.css").toExternalForm());
        }
    }


    private void initShortcutKeys() {
        // Enter traversal
        bpRootPurOrderListPane.addEventFilter(KeyEvent.KEY_PRESSED, (KeyEvent event) -> {

            if (event.getCode() == KeyCode.ENTER) {
                if (event.getTarget() == tfPurOrdLstToDate) {
                    fromDate = null;
                    toDate = null;
                    if (!tfPurOrdLstFromDate.getText().isEmpty()) {
                        fromDate = Communicator.text_to_date.fromString(tfPurOrdLstFromDate.getText());
                    }
                    if (!tfPurOrdLstToDate.getText().isEmpty()) {
                        toDate = Communicator.text_to_date.fromString(tfPurOrdLstToDate.getText());
                    }
                    fetchDataOfAllPurchaseOrders("");
                    btnPurOrdLstCreatebtn.requestFocus();
                } else {
                    if (event.getTarget() instanceof Button targetButton && targetButton.getText().equalsIgnoreCase("create")) {
                        System.out.println(targetButton.getText());
                    } else {
                        purchaseOrderEditPage();
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
            }else if (event.getCode() == KeyCode.N && event.isControlDown()) {
                btnPurOrdLstCreatebtn.fire();
            }else if (event.getCode() == KeyCode.DOWN && tftranxPurOrdLstSearch.isFocused()) {
                tblvPurOrdLst.getSelectionModel().select(0);
                tblvPurOrdLst.requestFocus();
            } else if (event.getCode() == KeyCode.E && event.isControlDown()) {
                purchaseOrderEditPage();
            } else if (event.getCode() == KeyCode.D && event.isControlDown()) {
                handleCtrlDPressed();
            }
        });
    }

    private void madatoryFields() {
        tblvPurOrdLst.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tblvPurOrdLst.setEditable(true);
        CommonFunctionalUtils.restrictTextFieldToDigitsAndDateFormat(tfPurOrdLstFromDate);
        CommonFunctionalUtils.restrictTextFieldToDigitsAndDateFormat(tfPurOrdLstToDate);

        DateValidator.applyDateFormat(tfPurOrdLstFromDate);
        DateValidator.applyDateFormat(tfPurOrdLstToDate);
        sceneInitilization();
    }

    private void tableViewOperations() {
        tblcPurOrdLstSelect.setCellFactory(column -> new CheckBoxTableCell<>());
        tblcPurOrdLstSelect.setCellValueFactory(cellData -> {
            PurchaseOrderDTO cellValue = cellData.getValue();
            BooleanProperty property = cellValue.is_row_selectedProperty();

            // Add listener to handler change
            property.addListener((observable, oldValue, newValue) -> cellValue.setIs_row_selected(newValue));
            findOutSelectedRow();
            if (cellValue.isIs_row_selected()) {
                if (cellValue.getPurchase_order_status().equalsIgnoreCase("closed")) {

                    AlertUtility.AlertErrorTimeout(AlertUtility.alertTypeError, "The Order is closed", e -> {
                        cellValue.setIs_row_selected(false);
                    });

                } else if (cellValue.getOrder_status().equalsIgnoreCase("pending")) {
                    handleSelection(tblvPurOrdLst.getItems());
                } else if (!cellValue.getOrder_status().equalsIgnoreCase("Ready For Delivery")) {
                    AlertUtility.AlertErrorTimeout(AlertUtility.alertTypeError, "Order is Not Processed Yet!", e -> {
                        cellValue.setIs_row_selected(false);
                    });

                } else {
                    handleSelection(tblvPurOrdLst.getItems());
                }
            }
            return property;

        });

        // OnDoubleClick In Edit Page of Particular Purchase Challan Screen
        tblvPurOrdLst.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                purchaseOrderEditPage();
            }
        });

        tblvPurOrdLst.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
//                System.out.println("Key Pressed Order List : " + event.getCode());
                if (event.isControlDown()) {
//                    System.out.println("Control Key is pressed");
                    if (event.getCode() == KeyCode.E) {
//                        System.out.println("Ctrl+E is pressed");
                        PurchaseOrderDTO selectedItem = (PurchaseOrderDTO) tblvPurOrdLst.getSelectionModel().getSelectedItem();
                        Integer purOrderId = Integer.valueOf(selectedItem.getId());
                        GlobalController.getInstance().addTabStaticWithParam(PURCHASE_ORDER_EDIT_SLUG, false, purOrderId);
                        event.consume();
                    } else if (event.getCode() == KeyCode.D) {
//                        System.out.println("Ctrl+D is pressed");
                        PurchaseOrderDTO selectedItem = (PurchaseOrderDTO) tblvPurOrdLst.getSelectionModel().getSelectedItem();
                        Integer purOrderId = Integer.valueOf(selectedItem.getId());
                        deletePurOrderList(purOrderId);
                    }
                }
            }
        });

        tblcPurOrdLstTranxStatus.setCellValueFactory(new PropertyValueFactory<>("tblcPurOrdLstTranxStatus"));
        tblcPurOrdLstTranxStatus.setCellFactory(column -> {
            return new TableCell<PurchaseOrderDTO, String>() {
                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if (item == null || empty) {
                        setText(null);
                    } else {
                        setText(item);
                        // Set text color to white
//                        setTextFill(Color.WHITE);
                        setStyle("-fx-font-weight: bold; -fx-text-fill: white;");
                        // Check the value of tcSalesOrderListTranxStatus
                        if (item.equalsIgnoreCase("opened")) {
                            // Set green background color for opened status
//                            setTextFill(Color.GREEN);
                            setStyle("-fx-font-weight: bold; -fx-text-fill: green;");
                        } else if (item.equalsIgnoreCase("closed")) {
                            // Set red background color for closed status
//                            setTextFill(Color.RED);
                            setStyle("-fx-font-weight: bold; -fx-text-fill: red;");
                        }
                    }
                }
            };
        });
    }

    //function for conversion
    private void handleSelection(ObservableList<PurchaseOrderDTO> items) {
        String errorMessage = "Different Ledger selected!";
        String jsonFormat = " [%s] ";   //"selectedIds":

        StringJoiner selectedIds = new StringJoiner(",");
        JsonObject obj = new JsonObject();
        JsonArray array = new JsonArray();
        String lastLedgerName = null;

        for (PurchaseOrderDTO item : items) {
            if (item.isIs_row_selected()) {
                if (lastLedgerName == null) {
                    lastLedgerName = item.getSundry_creditor_name();
                } else if (!lastLedgerName.equals(item.getSundry_creditor_name())) {
                    AlertUtility.AlertErrorTimeout(AlertUtility.alertTypeError, errorMessage, input -> {
                        item.setIs_row_selected(false);
                    });
                    return;
                }
                obj.addProperty("id", item.getId());
                selectedIds.add(String.valueOf(obj));
            }
        }
        array.add(selectedIds.toString());
//        System.out.println("obj " + array);
        // Ledger names are the same, show JSON format
        String jsonResult = "";
        if (!selectedIds.toString().isEmpty()) {
            jsonResult = String.format(jsonFormat, selectedIds);
/*            System.out.println("jsonResult1>>>>>> " + selectedIds);
            System.out.println("jsonResult>>>>>> " + jsonResult);*/
        }
        PurchaseOrderToInvoiceController.orderToInvoice = jsonResult;
        PurOrderToChallanCreateController.purOrdToChallId = jsonResult;


    }

    public void goToInvoice() {

        ObservableList<PurchaseOrderDTO> list = tblvPurOrdLst.getItems();
        for (PurchaseOrderDTO object : list) {
            if (object.isIs_row_selected()) {
                if (object.getPurchase_order_status().equalsIgnoreCase("closed")) {
//                    AlertUtility.CustomCallback callback = (number) -> {
//                        if (number == 0) {
//                            tftranxPurOrdLstSearch.requestFocus();
//                        }
//                    };
//                    Stage stage = (Stage) bpRootPurOrderListPane.getScene().getWindow();
//                    AlertUtility.AlertError(stage, AlertUtility.alertTypeError, "The Order is closed", callback);
                    AlertUtility.AlertErrorTimeout(AlertUtility.alertTypeError, "The Order is closed", in -> {
                        tftranxPurOrdLstSearch.requestFocus();
                    });
//                    System.out.println("cellValue.getId() " + object.getId() + " cellValue.getSales_quotation_status()  " + object.getPurchase_order_status());
                } else {
                    GlobalController.getInstance().addTabStatic(PURCHASE_ORDER_TO_INV_EDIT_SLUG, false);
                }

            }
        }

    }

    public void goToPurChallan() {

        ObservableList<PurchaseOrderDTO> list = tblvPurOrdLst.getItems();
        for (PurchaseOrderDTO object : list) {
            if (object.isIs_row_selected()) {
                System.out.println("get status >> : " + object.getPurchase_order_status());
                if (object.getPurchase_order_status().equalsIgnoreCase("closed")) {
//                    AlertUtility.CustomCallback callback = (number) -> {
//                        if (number == 0) {
//                            tftranxPurOrdLstSearch.requestFocus();
//                        }
//                    };
//                    Stage stage = (Stage) bpRootPurOrderListPane.getScene().getWindow();
//                    AlertUtility.AlertError(stage, AlertUtility.alertTypeError, "The Order is closed", callback);
                    AlertUtility.AlertErrorTimeout(AlertUtility.alertTypeError, "The Order is closed", in -> {
                        tftranxPurOrdLstSearch.requestFocus();
                    });
                    System.out.println("cellValue.getId() " + object.getId() + " cellValue.goToPurChallan()  " + object.getPurchase_order_status());
                } else {
//                    GlobalController.getInstance().addTabStatic(PURCHASE_INV_EDIT_SLUG, false);
                    GlobalController.getInstance().addTabStatic(PURCHASE_ORDER_TO_CHALLAN_SLUG, false);
                }

            }
        }

    }

    //todo: Search Function to Search in the Table
    private void filterData(String keyword) {
        ObservableList<PurchaseOrderDTO> filteredData = FXCollections.observableArrayList();

        if (keyword.isEmpty()) {
            tblvPurOrdLst.setItems(originalData);
            return;
        }

        for (PurchaseOrderDTO item : originalData) {
            if (matchesKeyword(item, keyword)) {
                filteredData.add(item);
            }
        }

        tblvPurOrdLst.setItems(filteredData);
    }

    //todo: Search Function to Search in the Table for columns
    private boolean matchesKeyword(PurchaseOrderDTO item, String keyword) {
        String lowerCaseKeyword = keyword.toLowerCase();

        // Check if any of the columns contain the keyword
        return item.getInvoice_no().toLowerCase().contains(lowerCaseKeyword) ||
                item.getInvoice_date().toLowerCase().contains(lowerCaseKeyword) ||
                item.getSundry_creditor_name().toLowerCase().contains(lowerCaseKeyword) ||
                item.getNarration().toLowerCase().contains(lowerCaseKeyword) ||
                item.getTaxable_amt().toLowerCase().contains(lowerCaseKeyword) ||
                item.getTax().toLowerCase().contains(lowerCaseKeyword) ||
//                item.getAction().toLowerCase().contains(lowerCaseKeyword)||
                item.getTotal_amount().toLowerCase().contains(lowerCaseKeyword) ||
                item.getPurchase_order_status().toLowerCase().contains(lowerCaseKeyword);
//                item.getPrint().toLowerCase().contains(lowerCaseKeyword);
    }

    //todo: Open Create Page Only for Edit in the Same Tab
    public void purchaseOrderEditPage() {
        try {
            PurchaseOrderDTO selectedItem = (PurchaseOrderDTO) tblvPurOrdLst.getSelectionModel().getSelectedItem();
            Integer purOrderId = Integer.valueOf(selectedItem.getId());
            //? Highlight
            PurchaseOrderListController.editedPurchaseOrderId = String.valueOf(purOrderId);
            PurchaseOrderController.selectedRowIndex = tblvPurOrdLst.getSelectionModel().getFocusedIndex();
            GlobalController.getInstance().addTabStaticWithParam(PURCHASE_ORDER_EDIT_SLUG, false, purOrderId);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sceneInitilization() {
        bpRootPurOrderListPane.sceneProperty().addListener((observable, oldScene, newScene) -> {
            if (newScene != null && newScene.getWindow() instanceof Stage) {
                Communicator.stage = (Stage) newScene.getWindow();
            }
        });
    }

    private void findOutSelectedRow() {
        Boolean canShowButton = false;
        ObservableList<PurchaseOrderDTO> list = tblvPurOrdLst.getItems();
        for (PurchaseOrderDTO object : list) {
            if (object.isIs_row_selected()) {
                canShowButton = true;
                selectedIndex = Integer.valueOf(object.getId());
            }
        }
        if (canShowButton) {
            btnPurOrdLstCreatebtn.setVisible(false);
            btnPurOrdToInvoice.setVisible(true);
            btnPurOrdToChallan.setVisible(true);
        } else {
            btnPurOrdLstCreatebtn.setVisible(true);
            btnPurOrdToInvoice.setVisible(false);
            btnPurOrdToChallan.setVisible(false);
        }

    }

    public void updateTVData() {
        tblcPurOrdLstOrderNo.setCellValueFactory(new PropertyValueFactory<>("invoice_no"));
        tblcPurOrdTranxTrackingNo.setCellValueFactory(new PropertyValueFactory<>("transactionTrackingNo"));
        tblcPurOrdLstOrderDate.setCellValueFactory(new PropertyValueFactory<>("invoice_date"));
        tblcPurOrdLstSupplierName.setCellValueFactory(new PropertyValueFactory<>("sundry_creditor_name"));
        tblcPurOrdLstNarration.setCellValueFactory(new PropertyValueFactory<>("narration"));
        tblcPurOrdLstTaxable.setCellValueFactory(new PropertyValueFactory<>("taxable_amt"));
        tblcPurOrdLstTax.setCellValueFactory(new PropertyValueFactory<>("tax_amt"));
        tblcPurOrdLstBillAmount.setCellValueFactory(new PropertyValueFactory<>("total_amount"));
        tblcPurOrdLstTranxStatus.setCellValueFactory(new PropertyValueFactory<>("purchase_order_status"));
//        tblcPurOrdLstPrint.setCellFactory(new PropertyValueFactory<>("print"));
//        tblcPurOrdLstPrint.setCellFactory(new PropertyValueFactory<>(""));
//        tblcPurOrdLstAction.setCellFactory(param -> {
//            final TableCell<PurchaseOrderDTO, String> cell = new TableCell<>() {
//                private ImageView delImg = new ImageView(new Image(String.valueOf(GenivisApplication.class.getResource("ui/assets/del.png"))));
//                private ImageView edtImg = new ImageView(new Image(String.valueOf(GenivisApplication.class.getResource("ui/assets/edt.png"))));
//
//                {
//                    delImg.setFitHeight(20.0);
//                    delImg.setFitWidth(20.0);
//                    edtImg.setFitHeight(20.0);
//                    edtImg.setFitWidth(20.0);
//                }
//
//                private final Button delButton = new Button("", delImg);
//                private final Button edtButton = new Button("", edtImg);
//
//                {
////                    edtButton.setOnAction(actionEvent -> {
////                        Integer id = Integer.valueOf(getTableView().getItems().get(getIndex()).getId());
////                        GlobalController.getInstance().addTabStaticWithParam(PURCHASE_ORDER_LIST_SLUG, false, id);
////                    });
//                    edtButton.setOnAction(actionEvent -> {
//                        PurchaseOrderDTO id = getTableView().getItems().get(getIndex());
//                        String status = id.getPurchase_order_status();
//                        if (status != null && status.equalsIgnoreCase("opened")) {
//                            Integer id1 = Integer.valueOf(id.getId());
//                            GlobalController.getInstance().addTabStaticWithParam(PURCHASE_ORDER_EDIT_SLUG, false, id1);
//                        } else {
//                            // Show an alert indicating that the order is already closed
////                            AlertUtility.CustomCallback callback = (number) -> {
////                                if (number == 0) {
////                                    tftranxPurOrdLstSearch.requestFocus();
////                                }
////                            };
////                            Stage stage = (Stage) bpRootPurOrderListPane.getScene().getWindow();
////                            AlertUtility.AlertError(stage, AlertUtility.alertTypeError, "The Order is Closed", callback);
//                            AlertUtility.AlertErrorTimeout(AlertUtility.alertTypeError, "The Order is Closed", in -> {
//                                tftranxPurOrdLstSearch.requestFocus();
//                            });
//                        }
////                                            Integer id = Integer.valueOf(getTableView().getItems().get(getIndex()).getId());
////                                            GlobalController.getInstance().addTabStaticWithParam(SALES_ORDER_EDIT_SLUG, false, id);
//                    });
//                }
//
//                {
////                    delButton.setOnAction(actionEvent -> {
////                        PurchaseOrderDTO purOrderListId = getTableView().getItems().get(getIndex());
////                        AlertUtility.AlertConfirmation(AlertUtility.alertTypeConfirmation, "Are you Sure?", input -> {
////                            if (input == 1) {
////                                deletePurOrderList(Integer.parseInt(purOrderListId.getId()));
////                            }
////                        });
////                    });
//                    delButton.setOnAction(actionEvent -> {
//                        PurchaseOrderDTO id = getTableView().getItems().get(getIndex());
//                        String status = id.getPurchase_order_status();
//                        // Check if the status is "opened"
//                        if (status != null && status.equalsIgnoreCase("opened")) {
//                            Integer id1 = Integer.valueOf(id.getId());
//
//                            deletePurOrderList(id1);
//                        } else {
//                            // Show an alert indicating that the order is already closed
////                            AlertUtility.CustomCallback callback = (number) -> {
////                                if (number == 0) {
////                                    tftranxPurOrdLstSearch.requestFocus();
////                                }
////                            };
////                            Stage stage = (Stage) bpRootPurOrderListPane.getScene().getWindow();
////                            AlertUtility.AlertError(stage, AlertUtility.alertTypeError, "The Order is Closed", callback);
//                            AlertUtility.AlertErrorTimeout(AlertUtility.alertTypeError, "The Order is Closed", in -> {
//                                tftranxPurOrdLstSearch.requestFocus();
//                            });
//                        }
////                                    GlobalController.getInstance().addTabStaticWithParam(SALES_ORDER_EDIT_SLUG, false, id);
//                    });
//                }
//
//                HBox hbActions = new HBox();
//
//                {
//                    hbActions.getChildren().add(edtButton);
////                                hbActions.getChildren().add(viewButton);
//                    hbActions.getChildren().add(delButton);
//                    hbActions.setSpacing(10.0);
//                }
//
//                // Set the action for the view button
//                @Override
//                protected void updateItem(String item, boolean empty) {
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
//        tblcPurOrdLstPrint.setCellValueFactory(new PropertyValueFactory<>("tblcPurOrdLstPrint"));
        tblcPurOrdLstPrint.setCellFactory(param -> {
            final TableCell<PurchaseOrderDTO, String> cell = new TableCell<>() {
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
                        PurchaseOrderDTO id = getTableView().getItems().get(getIndex());
                        String status = id.getPurchase_order_status();
                        // Check if the status is "opened"
                        if (status != null) {
//                            id1 = Integer.valueOf(id.getId());
//                            printPurchaseOrder(id1);
                        } else {
                            // Show an alert indicating that the order is already closed
                            AlertUtility.CustomCallback callback = (number) -> {
                                if (number == 0) {
                                    tftranxPurOrdLstSearch.requestFocus();
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
        originalData.setAll(observableList);

        tblvPurOrdLst.setItems(observableList);
        //******************************** Highlight on the Created/Edited Record in the List Start ********************************
        if (PurchaseOrderListController.isNewPurchaseOrderCreated) {
            tblvPurOrdLst.getSelectionModel().selectFirst();
            tblvPurOrdLst.scrollTo(0);
            PurchaseOrderListController.isNewPurchaseOrderCreated = false; // Reset the flag
        } else if (PurchaseOrderListController.editedPurchaseOrderId != null) {
            for (PurchaseOrderDTO purchaseOrder : observableList) {
                if (purchaseOrder.getId().equals(PurchaseOrderListController.editedPurchaseOrderId)) {
                    tblvPurOrdLst.getSelectionModel().select(purchaseOrder);
                    tblvPurOrdLst.scrollTo(purchaseOrder);
                    PurchaseOrderListController.editedPurchaseOrderId = null; // Reset the ID
                    break;
                }
            }
        }
        //******************************** Highlight on the Created/Edited Record in the List End ********************************

        tblvPurOrdLst.refresh();

        if (Globals.purchaseOrderListSrc.equalsIgnoreCase("M")) {
            tblvPurOrdLst.requestFocus();
            tblvPurOrdLst.getSelectionModel().select(0);
            Globals.purchaseOrderListSrc = "C";
        }

    }

    //TODO: Delete API Function
    public void deletePurOrderList(int id) {

        try {
            AlertUtility.CustomCallback callback = number -> {
                if (number == 1) {
                    APIClient apiClient = null;
                    try {
                        logger.debug("Delete Purchase Order List Data Started...");
                        Map<String, String> map = new HashMap<>();
                        map.put("id", String.valueOf(id));
                        String formData = Globals.mapToStringforFormData(map);
//                        HttpResponse<String> response = APIClient.postFormDataRequest(formData, EndPoints.SALES_ORDER_DELETE_ENDPOINT);
                        apiClient = new APIClient(EndPoints.PURCHASE_ORDER_LIST_DELETE, formData, RequestType.FORM_DATA);

                        apiClient.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
                            @Override
                            public void handle(WorkerStateEvent workerStateEvent) {
                                jsonObject = new Gson().fromJson(workerStateEvent.getSource().getValue().toString(), JsonObject.class);
//                                System.out.println("jsonObject-->" + jsonObject);
//                                responseBody = response.body();
//                                JsonObject jsonObject = new Gson().fromJson(responseBody, JsonObject.class);
                                message = jsonObject.get("message").getAsString();
//                                System.out.println("Delete->" + jsonObject.get("message"));
//                                AlertUtility.CustomCallback callback1 = (number1) -> {
//                                    if (number1 == 1) {
//                                        // Update the list after successful deletion
//                                        observableList.removeIf(item -> item.getId().equals(String.valueOf(id)));
//                                        tblvPurOrdLst.setItems(observableList);
//                                    }
//                                };
//                                Stage stage2 = (Stage) bpRootPurOrderListPane.getScene().getWindow();
//                                if (jsonObject.get("responseStatus").getAsInt() == 200) {
//                                    AlertUtility.AlertSuccess(stage2, "Success", message, callback1);
//                                } else {
//                                    AlertUtility.AlertError(stage2, AlertUtility.alertTypeError, message, callback1);
//                                }
                                if (jsonObject.get("responseStatus").getAsInt() == 200) {
                                    AlertUtility.AlertSuccessTimeout(AlertUtility.alertTypeSuccess, message, input -> {
                                        // Update the list after successful deletion
                                        observableList.removeIf(item -> item.getId().equals(String.valueOf(id)));
                                        tblvPurOrdLst.setItems(observableList);
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
                                logger.error("Network API cancelled in Delete Purchase Order List()" + workerStateEvent.getSource().getValue().toString());

                            }
                        });
                        apiClient.setOnFailed(new EventHandler<WorkerStateEvent>() {
                            @Override
                            public void handle(WorkerStateEvent workerStateEvent) {
                                logger.error("Network API failed in Delete Purchase Order List()" + workerStateEvent.getSource().getValue().toString());
                            }
                        });
                        apiClient.start();
                        logger.debug("Delete Purchase Order List Data End...");

                    } catch (Exception e) {
                        e.printStackTrace();
                        logger.error("Exception in Delete purchase Order" + Globals.getExceptionString(e));
                    } finally {
                        apiClient = null;
                    }
                } else {
                    System.out.println("working!");
                }
            };
            AlertUtility.AlertConfirmation(AlertUtility.alertTypeConfirmation, "Do you want to Delete", callback);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void fetchDataOfAllPurchaseOrders(String searchKey) {
        APIClient apiClient = null;
        try {
            tblvPurOrdLst.getItems().clear();
            // Format dates
            String startDate = fromDate != null ? fromDate.format(dateFormatter) : "";
            String endDate = toDate != null ? toDate.format(dateFormatter) : "";
//            System.out.println("Start Date: " + startDate);
//            System.out.println("End Date: " + endDate);
            Map<String, String> body = new HashMap<>();
            body.put("pageNo", "1");
            body.put("pageSize", "50");
            body.put("searchText", searchKey);
            body.put("sort", "");
            body.put("startDate", startDate);
            body.put("endDate", endDate);
            String requestBody = Globals.mapToString(body);

            apiClient = new APIClient(EndPoints.LIST_OF_ALL_PO_INVOICE_LIST, requestBody, RequestType.JSON);

            apiClient.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
                @Override
                public void handle(WorkerStateEvent workerStateEvent) {
                    jsonObject = new Gson().fromJson(workerStateEvent.getSource().getValue().toString(), JsonObject.class);
//                    System.out.println("jsonObject----"+jsonObject);

                    observableList.clear();
                    if (jsonObject.get("responseStatus").getAsInt() == 200) {
                        JsonObject responseObject = jsonObject.get("responseObject").getAsJsonObject();
                        JsonArray responseArray = responseObject.get("data").getAsJsonArray();
//                        System.out.println("Data==>" + responseArray);

                        if (responseArray.size() > 0) {
                            tblvPurOrdLst.setVisible(true);
                            for (JsonElement element : responseArray) {
                                JsonObject item = element.getAsJsonObject();
                                String id = item.get("id").getAsString();
                                String invoice_no = item.get("invoice_no").getAsString();
                                String invoice_date = DateConvertUtil.convertDispDateFormat(item.get("invoice_date").getAsString());
                                String sundry_creditor_name = item.get("sundry_creditor_name").getAsString();
                                String narration = item.get("narration").getAsString();
                                String taxable_amt = item.get("taxable_amt").getAsString();
                                String tax_amt = item.get("tax_amt").getAsString();
                                String total_amount = item.get("total_amount").getAsString();
                                String purchase_order_status = item.get("purchase_order_status").getAsString();
                                String order_status = item.get("orderStatus").getAsString();
                                String transactionTrackingNo = item.get("transactionTrackingNo").getAsString();
                                String print = "";
                                String action = "";

                                observableList.add(new PurchaseOrderDTO(id, invoice_no, invoice_date, sundry_creditor_name, narration, taxable_amt, tax_amt, total_amount, purchase_order_status, order_status, print, action, transactionTrackingNo));
                            }

                            updateTVData();


                        } else {
                            System.out.println("responseObject is null");
                        }
                    } else {
                        System.out.println("Error in response");
                    }
                   /* observableList.addListener(new ListChangeListener<PurchaseOrderDTO>() {
                        @Override
                        public void onChanged(Change<? extends PurchaseOrderDTO> change) {
                            while (change.next()){
                                if(change.wasUpdated()){
                                    System.out.println("Updated " + (i++));
                                }
                            }
                        }
                    });*/

                }
            });
            apiClient.setOnCancelled(new EventHandler<WorkerStateEvent>() {
                @Override
                public void handle(WorkerStateEvent workerStateEvent) {
                    logger.error("Network API cancelled in fetchDataOfAllPurchaseOrders()" + workerStateEvent.getSource().getValue().toString());
                }
            });
            apiClient.setOnFailed(new EventHandler<WorkerStateEvent>() {
                @Override
                public void handle(WorkerStateEvent workerStateEvent) {
                    logger.error("Network API failed in fetchDataOfAllPurchaseOrders()" + workerStateEvent.getSource().getValue().toString());
                }
            });
            apiClient.start();


//            HttpResponse<String> response = APIClient.postJsonRequest(requestBody, "list_po_invoice");
//            System.out.println("purchaseOrderFetchAllOrders==>" + response.body());
//            JsonObject jsonObject = new Gson().fromJson(response.body(), JsonObject.class);
//            System.out.println("jsonObject==>" + jsonObject);

        } catch (Exception e) {
            System.out.println("exception:" + e);
            logger.error("Exception in fetchDataOfAllPurchaseOrders(): "+Globals.getExceptionString(e));
        } finally {
            apiClient = null;
        }
    }

    @FXML
    private void handleCtrlDPressed() {
        try {
            purchaseOrderDTO = tblvPurOrdLst.getSelectionModel().getSelectedItem();
            if (Globals.purchaseOrderDTO != null) {
                Integer id = Integer.valueOf(purchaseOrderDTO.getId());
//                System.out.println("ctrl+D" + id);
                deletePurOrderList(id);
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("Exception in handleCtrlDPressed() : "+Globals.getExceptionString(e));
        }
    }
}
