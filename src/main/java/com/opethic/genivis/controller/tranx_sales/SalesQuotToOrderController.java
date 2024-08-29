package com.opethic.genivis.controller.tranx_sales;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.opethic.genivis.GenivisApplication;
import com.opethic.genivis.controller.dialogs.SingleInputDialogs;
import com.opethic.genivis.controller.tranx_sales.common.TranxCommonPopUps;
import com.opethic.genivis.controller.master.ledger.common.LedgerMessageConsts;
import com.opethic.genivis.dto.*;
import com.opethic.genivis.dto.pur_invoice.*;
import com.opethic.genivis.dto.reqres.product.Communicator;
import com.opethic.genivis.dto.reqres.product.TableCellCallback;
import com.opethic.genivis.dto.reqres.pur_tranx.TranxPurRowDTO;
import com.opethic.genivis.dto.reqres.sales_tranx.Row1;
import com.opethic.genivis.dto.reqres.sales_tranx.SalesOrderTable;
import com.opethic.genivis.dto.reqres.sales_tranx.SalesQuotEditMainDTO;
import com.opethic.genivis.dto.reqres.sales_tranx.SalesQuotToOrderMainDataDTO;
import com.opethic.genivis.models.tranx.sales.TranxSelectedProduct;
import com.opethic.genivis.network.APIClient;
import com.opethic.genivis.network.EndPoints;
import com.opethic.genivis.utils.*;
import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.util.StringConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;
import java.net.http.HttpResponse;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static com.opethic.genivis.utils.FxmFileConstants.SALES_ORDER_LIST_SLUG;
import static com.opethic.genivis.utils.FxmFileConstants.SALES_QUOTATION_LIST_SLUG;
import static com.opethic.genivis.utils.Globals.decimalFormat;
import static java.lang.Double.parseDouble;

public class SalesQuotToOrderController implements Initializable {

    @FXML
    private BorderPane spSlsQuotoOrderRootPane;

    @FXML
    private HBox topInnerHbTwo,topInnerHbOne,sqBottomMain;
    @FXML
    private VBox totalmainDiv,sqtotalInnervbox,sqBottomFirstV,sqBottomSecondV,sqtotalInnervbox1;

    @FXML
    private HBox sq2oTopInnerOne,sq2oTopInnerTwo;
    @FXML
    private ComboBox<CommonDTO> cmbSalesQuotToOrderSalesAC;
    @FXML
    private ComboBox<GstDetailsDTO> cmbSalesQuotToOrderClientGST;
    //    @FXML
//    private ComboBox  cmbSalesQuotToOrderClientGST, cmbSalesQuotToOrderSalesAC;
    @FXML
    TableView<SalesQuotationProductDTO> tblvSalesQuotToOrderCmptRow;
    @FXML
    TableView tvSalesQuotToOrderprodInvoiceDetails;
    @FXML
    TableColumn<SalesQuotationProductDTO, String> tblcSalesQuotToOrderSrNo, tblcSalesQuotToOrderParticular, tblcSalesQuotToOrderPackage, tcSalesQuotToOrderCmpTRowLevelA,
            tcSalesQuotToOrderCmpTRowLevelB, tcSalesQuotToOrderCmpTRowLevelC, tblcSalesQuotToOrderUnit, tblcSalesQuotToOrderQty,
            tblcSalesQuotToOrderFreeqty, tblcSalesQuotToOrderRate, tblcSalesQuotToOrderGrossAmt, tblcSalesQuotToOrderDisPer,
            tblcSalesQuotToOrderDisRs, tblcSalesQuotToOrderTax, tblcSalesQuotToOrderNetAmt, tblcSalesQuotToOrderCmpTRowActions;
    //Product Invoice table Column
    @FXML
    TableColumn tblcSalesQuotToOrderInvClienName, tblcSalesQuotToOrderInvInvoiceNo, tblcSalesQuotToOrderInvInvoiceDate, tblcSalesQuotToOrderInvBatch, tblcSalesQuotToOrderInvMrp,
            tblcSalesQuotToOrderInvQuantity, tblcSalesQuotToOrderInvRate, tblcSalesQuotToOrderInvCost, tblcSalesQuotToOrderInvDisPer, tblcSalesQuotToOrderInvDis;

    @FXML
    private TextField tfSalesQuotToOrderTranxDate, tfSalesQuotToOrderOrderDate;
    @FXML
    private TextField tfSalesQuotToOrderLedgerName, tfSalesQuotToOrderSalesSerial, tfSalesQuotToOrderOrderNo, tfSalesQuotToOrderNarration;
    private static final Logger SalesQuotToOrderListLogger = LoggerFactory.getLogger(SalesQuotationListController.class);
    @FXML
    private Label lbSalesQuotToOrderGrossTotal, lbSalesQuotToOrderDiscount, lbSalesQuotToOrderTotal, lbSalesQuotToOrderTax, lbSalesQuotToOrderBillAmount;

    @FXML
    Label lbSalesQuotToOrderledInfogstNo, lbSalesQuotToOrderledInfoContPerson, lbSalesQuotToOrderledInfoCreditDays, lbSalesQuotToOrderledInfoLiceNo,
            lbSalesQuotToOrderledInfoArea, lbSalesQuotToOrderledInfoMobNo, lbSalesQuotToOrderledInfoFssai, lbSalesQuotToOrderledInfoRoute, lbSalesQuotToOrderledInfoBank;
    //    Product Info labels
    @FXML
    Label lbSalesQuotToOrderProdInfoBrand, lbSalesQuotToOrderProdInfoHSN, lbSalesQuotToOrderProdInfoCost, lbSalesQuotToOrderProdInfoGroup,
            lbSalesQuotToOrderProdInfoTaxType, lbSalesQuotToOrderProdInfoShelfId, lbSalesQuotToOrderProdInfoCategory, lbSalesQuotToOrderProdInfoTax, lbSalesQuotToOrderProdInfoMinStock,
            lbSalesQuotToOrderProdInfoSupplier, lbSalesQuotToOrderProdInfoMargin, lbSalesQuotToOrderProdInfoMaxStock;
    @FXML
    public TabPane tpSalesQuotToOrder;
    @FXML
    private Button addRowInCmpTRow, btnSalesQuotToOrderSubmit, btnSalesQuotToOrderCancle;
    @FXML
    public Tab tabSalesQuotToOrderLedger, tabSalesQuotToOrderProduct;

    @FXML
    private VBox vboxSalesQuotToOrderRoot;
    private static ObservableList<String> unitList = FXCollections.observableArrayList();
    private String ledgerName;
    private String ledgerId, ledgerStateCode;
    String Debtor_id = "";
    private String productId = "";
    private String responseBody;
    String message = "", id = "", finaltotalCGST = "", finaltotalSGST = "", finaltotalIGST = "", finalroundOff = "";
    private Integer SalesQuotId = -1, details_id = -1;
    Long stateCode = 0L, CompanyStateCode = 0L;
    Double TotalFreeQty = 0.0;
    Long TotalQty = 0L;
    //    public static ArrayList<String> arrList;
    public static String QuotaToOrderId = "";
//    public void setEditId(Integer InId) {
//        SalesQuotId = InId;
//        System.out.println("Sales QuotId"+SalesQuotId);
//        if (InId > -1) {
//            setEditData();
//        }
//    }
//


    public void initialize(URL url, ResourceBundle resourceBundle) {

        ResponsiveWiseCssPicker();

        CommonValidationsUtils.changeStarColour(vboxSalesQuotToOrderRoot);
        SalesQuotToOrderListLogger.info("Sales Quotation To Order Start From Here");
        System.out.println("QuotaToOrderId>>> " + QuotaToOrderId);
        //todo : autofocus on challan Date
        Platform.runLater(() -> tfSalesQuotToOrderLedgerName.requestFocus());


        //todo: set the TranxDate Serial No TextField to Readonly
        tfSalesQuotToOrderSalesSerial.setDisable(true);
        tfSalesQuotToOrderTranxDate.setDisable(true);
        tblvSalesQuotToOrderCmptRow.setFocusTraversable(false);
        tvSalesQuotToOrderprodInvoiceDetails.setFocusTraversable(false);

        //         Enter traversal
        spSlsQuotoOrderRootPane.addEventFilter(KeyEvent.KEY_PRESSED, (KeyEvent event) -> {
            if (event.getCode() == KeyCode.ENTER) {
                if (event.getTarget() instanceof Button targetButton && targetButton.getText().equalsIgnoreCase("Cancel")) {
                } else if (event.getTarget() instanceof Button targetButton && targetButton.getText().equalsIgnoreCase("Submit")) {
                } else {

                    if (tfSalesQuotToOrderOrderNo.getText() != "") {
                        validateOrderNo();
                    }
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
            } else if (event.getCode() == KeyCode.TAB) {
                validateOrderNo();
            }else if (event.getCode() == KeyCode.S && event.isControlDown()) {
                btnSalesQuotToOrderSubmit.fire();
            } else if (event.getCode() == KeyCode.X && event.isControlDown()) {
                    btnSalesQuotToOrderCancle.fire();
            }
        });


        //todo: set Sales A/c Data
        getSalesAccounts();
        //todo:Order and SerialNo setting
        getSalesOrderSerialNo();

        //todo: Responsive code for tableview
        responsiveTable();
        //todo: Responsive code for bottom invoice Product detais tableview
        responsiveprodInfoTable();

        //todo:Quotation date set to current date in normal date format
        //todo:Set default value to current local date
//        LocalDate tranxDate=LocalDate.now();
//        LocalDate orderDate=LocalDate.now();
//        tfSalesQuotToOrderTranxDate.setText(tranxDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
//        tfSalesQuotToOrderOrderDate.setText(orderDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        //DatePicker to TextField with validation
        DateValidator.applyDateFormat(tfSalesQuotToOrderOrderDate);
        DateValidator.applyDateFormat(tfSalesQuotToOrderTranxDate);
        sceneInitilization();

        //todo:Product Row Data
        getSalesQuotById();

        //todo: set Calculation footer
//        calculateEachRowTraxTotal();
//        cmpTRowData();

//        //todo:Open Product Modal
//        //todo:On Clicking on the Particulars TextField and Select the Product From the Modal
//        tblvSalesQuotToOrderCmptRow.setEditable(true);
//        tblvSalesQuotToOrderCmptRow.getSelectionModel().setCellSelectionEnabled(true);
//        tblvSalesQuotToOrderCmptRow.getSelectionModel().selectFirst();
//        tblvSalesQuotToOrderCmptRow.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
//            if (newSelection == null) {
//                tblvSalesQuotToOrderCmptRow.getSelectionModel().select(oldSelection);
//            }
//        });
//        tblcSalesQuotToOrderParticular.setCellFactory(column -> {
//            return new TableCell<>() {
//                @Override
//                protected void updateItem(String item, boolean empty) {
//                    super.updateItem(item, empty);
//
//                    if (item == null || empty) {
//                        setText(null);
//                        setGraphic(null);
//                    } else {
//                        TextField textField = new TextField(item);
//                        setGraphic(textField);
//
//                        textField.setOnMouseClicked(event -> {
//                            if (!isEmpty()) {
//                                Stage stage = (Stage) spSlsQuotoOrderRootPane.getScene().getWindow();
//                                SingleInputDialogs.openProductPopUp(stage, "Product", input -> {
//                                    // Assuming input[0] contains the product name
//                                    String productName = (String) input[0];
//                                    productId = (String) input[1];
//                                    System.out.println("product id" + productId);
//
//                                    fetchSelectedProductData(productId);
//                                    getSupplierListbyProductId(productId);
//
//                                    String packaging = (String) input[2];
//                                    String mrp = (String) input[3];
//                                    String unit = (String) input[4];
//                                    String taxper = (String) input[5];
//                                    String salesrate = (String) input[6];
//
//
//                                    // Update the text field with the selected product name
//                                    textField.setText(productName);
//
//                                    // Get the index of the selected row
//                                    int selectedIndex = getTableRow().getIndex();
//                                    System.out.println("selected Index: " + selectedIndex);
//                                    // Create a new instance of CmpTRowDTO with empty values
//                                    CmpTRowDTO newRow = new CmpTRowDTO(String.valueOf(input[1]), productName, packaging, unit, "", "", mrp, "", "", "", taxper, "", "", "", "", "", "");
//
//                                    // Add or update the row in the table
//                                    if (selectedIndex < 0 || selectedIndex >= tblvSalesQuotToOrderCmptRow.getItems().size()) {
//                                        // Add the new row if selected index is invalid
//                                        tblvSalesQuotToOrderCmptRow.getItems().add(newRow);
//                                    } else {
//                                        // Update the existing row
//                                        tblvSalesQuotToOrderCmptRow.getItems().set(selectedIndex, newRow);
//                                    }
////++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++Add New Row Button++++++++++++++++++++++++++++++++++++++++++++++++++++++
//                                    // Add "+" symbol in the last column
//                                    TableColumn<CmpTRowDTO, Void> colActions = (TableColumn<CmpTRowDTO, Void>) tblvSalesQuotToOrderCmptRow.getColumns().get(tblvSalesQuotToOrderCmptRow.getColumns().size() - 1);
//                                    colActions.setCellFactory(column -> {
//                                        return new TableCell<>() {
//                                            final Button addButton = new Button("+");
//
//                                            {
//                                                addButton.setStyle("-fx-font-size: 24px;-fx-background-color: transparent; -fx-border-color: transparent; -fx-text-fill: black;");
//                                                addButton.setAlignment(Pos.CENTER_LEFT);
//                                                addButton.setMinWidth(10); // Set minimum width to maintain button visibility
//                                                addButton.setMinHeight(10); // Set minimum height to maintain button visibility
//                                                addButton.setPadding(new Insets(0, 0, 0, -5)); // Add padding to increase button size
//
//                                                addButton.setOnAction(event -> {
//                                                    // Handle action when "+" button is clicked
//                                                    // Implement the logic to add a new row or any other action you desire
//                                                    addRow(event);
//                                                });
//                                            }
//
//                                            @Override
//                                            protected void updateItem(Void item, boolean empty) {
//                                                super.updateItem(item, empty);
//
//                                                if (empty || productName == null) { // Hide the button if no product name is set
//                                                    setGraphic(null);
//                                                } else {
//                                                    setGraphic(addButton);
//                                                }
//                                            }
//                                        };
//                                    });
//                                });
//                            }
//                        });
//                    }
//                }
//            };
//        });


        //todo : submit sales quotation to Order
        Platform.runLater(() -> btnSalesQuotToOrderSubmit.setOnAction(actionEvent -> {
            String btnText = btnSalesQuotToOrderSubmit.getText();
            createSalesQuotationToOrder();
        }));

        btnSalesQuotToOrderCancle.setOnAction(event -> {
            AlertUtility.AlertConfirmation(AlertUtility.alertTypeConfirmation, "Are you sure want to cancel ?", input -> {
                if (input == 1) {
                    GlobalController.getInstance().addTabStatic(SALES_QUOTATION_LIST_SLUG, false);
                }
            });

        });

        tableInitiliazation();
        shortcutKeysSalesQuotToOrder();

    }
    //? On Clicking on the Modify Button From Create or Edit page redirect Back to List page
    public void backToListModify() {
        AlertUtility.AlertConfirmation(AlertUtility.alertTypeConfirmation, LedgerMessageConsts.msgGoList, input -> {
            if (input == 1) {
                GlobalController.getInstance().addTabStatic(SALES_QUOTATION_LIST_SLUG, false);
            }
        });
    }

    private void ResponsiveWiseCssPicker() {
        double height = Screen.getPrimary().getBounds().getHeight();
        double width = Screen.getPrimary().getBounds().getWidth();
        System.out.println("width" + width);
        if (width >= 992 && width <= 1024) {
            topInnerHbOne.setSpacing(8);
            topInnerHbTwo.setSpacing(8);
            totalmainDiv.setSpacing(10);
            sqtotalInnervbox.setSpacing(10);
            sqtotalInnervbox1.setSpacing(10);
            // Set preferred widths as percentages of the HBox width
            sqBottomFirstV.prefWidthProperty().bind(sqBottomMain.widthProperty().multiply(0.76));
            sqBottomSecondV.prefWidthProperty().bind(sqBottomMain.widthProperty().multiply(0.24));
//            sqBottomMain.setPrefHeight(260);
            tvSalesQuotToOrderprodInvoiceDetails.getStylesheets().add(GenivisApplication.class.getResource("ui/css/purchase_challan_css/supplier_table_css/supplier_table_992_1024.css").toExternalForm());
            spSlsQuotoOrderRootPane.getStylesheets().add(GenivisApplication.class.getResource("ui/css/TranxCommonCssStyles/tranxCommonCssStyle1.css").toExternalForm());
        } else if (width >= 1025 && width <= 1280) {
            topInnerHbOne.setSpacing(8);
            topInnerHbTwo.setSpacing(8);
            totalmainDiv.setSpacing(10);
            sqtotalInnervbox.setSpacing(10);
            sqtotalInnervbox1.setSpacing(10);
            // Set preferred widths as percentages of the HBox width
            sqBottomFirstV.prefWidthProperty().bind(sqBottomMain.widthProperty().multiply(0.76));
            sqBottomSecondV.prefWidthProperty().bind(sqBottomMain.widthProperty().multiply(0.24));
//            sqBottomMain.setPrefHeight(260);
            tvSalesQuotToOrderprodInvoiceDetails.getStylesheets().add(GenivisApplication.class.getResource("ui/css/purchase_challan_css/supplier_table_css/supplier_table_1025_1280.css").toExternalForm());
            spSlsQuotoOrderRootPane.getStylesheets().add(GenivisApplication.class.getResource("ui/css/TranxCommonCssStyles/tranxCommonCssStyle2.css").toExternalForm());
        } else if (width >= 1281 && width <= 1368) {
            topInnerHbOne.setSpacing(8);
            topInnerHbTwo.setSpacing(8);
            totalmainDiv.setSpacing(10);
            sqtotalInnervbox.setSpacing(10);
            sqtotalInnervbox1.setSpacing(10);
            // Set preferred widths as percentages of the HBox width
            sqBottomFirstV.prefWidthProperty().bind(sqBottomMain.widthProperty().multiply(0.76));
            sqBottomSecondV.prefWidthProperty().bind(sqBottomMain.widthProperty().multiply(0.24));
//            sqBottomMain.setPrefHeight(260);
            tvSalesQuotToOrderprodInvoiceDetails.getStylesheets().add(GenivisApplication.class.getResource("ui/css/purchase_challan_css/supplier_table_css/supplier_table_1281_1368.css").toExternalForm());
            spSlsQuotoOrderRootPane.getStylesheets().add(GenivisApplication.class.getResource("ui/css/TranxCommonCssStyles/tranxCommonCssStyle3.css").toExternalForm());
        } else if (width >= 1369 && width <= 1400) {
            topInnerHbOne.setSpacing(15);
            topInnerHbTwo.setSpacing(15);
            totalmainDiv.setSpacing(10);
            sqtotalInnervbox.setSpacing(15);
            sqtotalInnervbox1.setSpacing(15);
            // Set preferred widths as percentages of the HBox width
            sqBottomFirstV.prefWidthProperty().bind(sqBottomMain.widthProperty().multiply(0.76));
            sqBottomSecondV.prefWidthProperty().bind(sqBottomMain.widthProperty().multiply(0.24));
//            sqBottomMain.setPrefHeight(150);
            tvSalesQuotToOrderprodInvoiceDetails.getStylesheets().add(GenivisApplication.class.getResource("ui/css/purchase_challan_css/supplier_table_css/supplier_table_1369_1400.css").toExternalForm());
            spSlsQuotoOrderRootPane.getStylesheets().add(GenivisApplication.class.getResource("ui/css/TranxCommonCssStyles/tranxCommonCssStyle4.css").toExternalForm());
        } else if (width >= 1401 && width <= 1440) {
            topInnerHbOne.setSpacing(15);
            topInnerHbTwo.setSpacing(15);
            totalmainDiv.setSpacing(10);
            sqtotalInnervbox.setSpacing(15);
            sqtotalInnervbox1.setSpacing(15);
            // Set preferred widths as percentages of the HBox width
            sqBottomFirstV.prefWidthProperty().bind(sqBottomMain.widthProperty().multiply(0.78));
            sqBottomSecondV.prefWidthProperty().bind(sqBottomMain.widthProperty().multiply(0.22));
//            sqBottomMain.setPrefHeight(150);
            spSlsQuotoOrderRootPane.getStylesheets().add(GenivisApplication.class.getResource("ui/css/TranxCommonCssStyles/tranxCommonCssStyle5.css").toExternalForm());
            tvSalesQuotToOrderprodInvoiceDetails.getStylesheets().add(GenivisApplication.class.getResource("ui/css/purchase_challan_css/supplier_table_css/supplier_table_1401_1440.css").toExternalForm());
        } else if (width >= 1441 && width <= 1680) {
            topInnerHbOne.setSpacing(15);
            topInnerHbTwo.setSpacing(15);
            totalmainDiv.setSpacing(10);
            sqtotalInnervbox.setSpacing(15);
            sqtotalInnervbox1.setSpacing(15);
            // Set preferred widths as percentages of the HBox width
            sqBottomFirstV.prefWidthProperty().bind(sqBottomMain.widthProperty().multiply(0.8));
            sqBottomSecondV.prefWidthProperty().bind(sqBottomMain.widthProperty().multiply(0.2));
//            sqBottomMain.setPrefHeight(310);
            tvSalesQuotToOrderprodInvoiceDetails.getStylesheets().add(GenivisApplication.class.getResource("ui/css/purchase_challan_css/supplier_table_css/supplier_table_1441_1680.css").toExternalForm());
            spSlsQuotoOrderRootPane.getStylesheets().add(GenivisApplication.class.getResource("ui/css/TranxCommonCssStyles/tranxCommonCssStyle6.css").toExternalForm());
        } else if (width >= 1681 && width <= 1920) {
            topInnerHbOne.setSpacing(20);
            topInnerHbTwo.setSpacing(20);
            totalmainDiv.setSpacing(20);
            sqtotalInnervbox.setSpacing(20);
            sqtotalInnervbox1.setSpacing(20);
            // Set preferred widths as percentages of the HBox width
            sqBottomFirstV.prefWidthProperty().bind(sqBottomMain.widthProperty().multiply(0.8));
            sqBottomSecondV.prefWidthProperty().bind(sqBottomMain.widthProperty().multiply(0.2));
//            sqBottomMain.setPrefHeight(150);
            tvSalesQuotToOrderprodInvoiceDetails.getStylesheets().add(GenivisApplication.class.getResource("ui/css/invoice_product_history_table.css").toExternalForm());
            spSlsQuotoOrderRootPane.getStylesheets().add(GenivisApplication.class.getResource("ui/css/TranxCommonCssStyles/tranxCommonCssStyle7.css").toExternalForm());
        }
    }

    private void shortcutKeysSalesQuotToOrder() {
        spSlsQuotoOrderRootPane.addEventFilter(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {

            @Override
            public void handle(KeyEvent event) {
                if (event.getCode() == KeyCode.S && event.isControlDown()) {
                    createSalesQuotationToOrder();
                }

                if (event.getCode() == KeyCode.X && event.isControlDown()) {
                    AlertUtility.AlertConfirmation(AlertUtility.alertTypeConfirmation, "Are you sure want to cancel ?", input -> {
                        if (input == 1) {
                            GlobalController.getInstance().addTabStatic(SALES_QUOTATION_LIST_SLUG, false);
                        }
                    });
                }
            }
        });
    }
    private void createSalesQuotationToOrder(){
        AlertUtility.AlertConfirmation(AlertUtility.alertTypeConfirmation, "Do you want to Create", input -> {
            if (input == 1) {
                if (CommonValidationsUtils.validateForm(tfSalesQuotToOrderLedgerName, tfSalesQuotToOrderOrderNo, tfSalesQuotToOrderOrderDate)) {
                    SalesQuotToOrder();
                }
            }
        });
    }

    //todo: Validate quotation Number
    public void validateOrderNo() {
        try {
            SalesQuotToOrderListLogger.info("Validate Order Number");
            Map<String, String> body = new HashMap<>();
            body.put("salesOrderNo", tfSalesQuotToOrderOrderNo.getText());
            System.out.println("tfSalesQuotationtoOrderOrderNo.getText() " + tfSalesQuotToOrderOrderNo.getText());
            String requestBody = Globals.mapToStringforFormData(body);
            HttpResponse<String> response = APIClient.postFormDataRequest(requestBody, EndPoints.SALESQUOTATION_VALIDATE_SALES_ORDER_NO);
            SalesQuotToOrderListLogger.info("response data of Validating order number " + response.body());
            JsonObject jsonObject = new Gson().fromJson(response.body(), JsonObject.class);
            System.out.println("validateOrderNo " + jsonObject);

            if (jsonObject.get("responseStatus").getAsInt() == 200) {
                System.out.println("SUCCESS " + jsonObject.get("message").getAsString());
                SalesQuotToOrderListLogger.debug(jsonObject.get("message").getAsString());
                tfSalesQuotToOrderOrderDate.requestFocus();
            } else {
                AlertUtility.CustomCallback callback = (number) -> {
                };
                Stage stage = (Stage) spSlsQuotoOrderRootPane.getScene().getWindow();
                AlertUtility.AlertErrorTimeout(AlertUtility.alertTypeError, jsonObject.get("message").getAsString(), callback);
//                showAlert(jsonObject.get("message").getAsString());
                System.out.println("FAIL " + jsonObject.get("message").getAsString());
                SalesQuotToOrderListLogger.debug(jsonObject.get("message").getAsString());
                tfSalesQuotToOrderOrderNo.requestFocus();
            }
        } catch (Exception e) {
            SalesQuotToOrderListLogger.error("Error in Validating Order number is" + e.getMessage());
            e.printStackTrace();
        }
    }

    TableCellCallback<Object[]> callback = item -> {

        System.out.println("item: " + item[0]);
        System.out.println("item    ZXzx: " + item[1]);
        System.out.println("item: " + item[2]);
        System.out.println("item: " + item[3]);
        System.out.println("item: " + item[4]);
        System.out.println("item: " + item[5]);
        System.out.println("item: " + item[6]);
        System.out.println("item: " + item[7]);
        System.out.println("item: " + item[8]);

        System.out.println("taxCalculation " + item[9]);

        ObservableList<GstDTO> gstDTOObservableList = FXCollections.observableArrayList();
        ObservableList<?> item9List = (ObservableList<?>) item[9];
        for (Object obj : item9List) {
            if (obj instanceof GstDTO) {
                gstDTOObservableList.add((GstDTO) obj);
            }
        }

        JsonArray jsonArray = new JsonArray();
        for (GstDTO gstDTO : gstDTOObservableList) {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("d_gst", decimalFormat.format(Double.parseDouble(gstDTO.getTaxPer())));
            jsonObject.addProperty("gst", decimalFormat.format(Double.parseDouble(gstDTO.getTaxPer()) / 2));
            jsonObject.addProperty("amt", gstDTO.getCgst());
            jsonArray.add(jsonObject);
        }
        JsonObject jsonObject = new JsonObject();
        jsonObject.add("cgst", jsonArray);
        jsonObject.add("sgst", jsonArray);
//        purchase_invoice_map.put("taxCalculation", jsonObject.toString());
        lbSalesQuotToOrderGrossTotal.setText((String) item[3]);
        lbSalesQuotToOrderDiscount.setText((String) item[4]);
        lbSalesQuotToOrderTotal.setText((String) item[5]);
        lbSalesQuotToOrderTax.setText((String) item[6]);
        lbSalesQuotToOrderBillAmount.setText((String) item[2]); //totalNetAmt for bill amount
    };

    TableCellCallback<Integer> productCall = item -> {
        System.out.println("product id " + item);
        fetchSelectedProductData(String.valueOf(item));
        getSupplierListbyProductId(String.valueOf(item));
    };

    //new start
    TableCellCallback<Integer> unit_callback = currentIndex -> {
        System.out.println("i am in" + currentIndex);
        System.out.println("i am in ledgerStateCode" + ledgerStateCode);
        SalesQuotationProductDTO tranxRow = tblvSalesQuotToOrderCmptRow.getItems().get(currentIndex);

        if (ledgerStateCode != null) {
            if (ledgerStateCode.equalsIgnoreCase(Globals.mhStateCode)) {
                System.out.println("tranxRow.getUnitWiseRate() => " + tranxRow.getUnitWiseRateMH());
                tranxRow.setRate(tranxRow.getUnitWiseRateMH() + "");
            } else {
                System.out.println("tranxRow.getUnitWiseRate() => " + tranxRow.getUnitWiseRateAI());
                tranxRow.setRate(tranxRow.getUnitWiseRateAI() + "");
            }
        }
//        tvSalesOrderCmpTRow.getItems().set(currentIndex, tranxRow);
    };
    //new end


    public void tableInitiliazation() {

        tblvSalesQuotToOrderCmptRow.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tblvSalesQuotToOrderCmptRow.setEditable(true);
        tblvSalesQuotToOrderCmptRow.setFocusTraversable(false);


        Label headerLabel = new Label("Sr\nNo.");
        tblcSalesQuotToOrderSrNo.setGraphic(headerLabel);

//        tblvSalesQuotToOrderCmptRow.getItems().add(new SalesQuotationProductDTO("1", "", "", "", "", "", "", "",
//                "", "", "", "", "", "", "", "", "","",""));

        tblcSalesQuotToOrderSrNo.setCellValueFactory(cellData -> cellData.getValue().sr_noProperty());
        tblcSalesQuotToOrderSrNo.setStyle("-fx-alignment: CENTER;");

        tblcSalesQuotToOrderPackage.setCellValueFactory(cellData -> cellData.getValue().packingProperty());
        tblcSalesQuotToOrderPackage.setStyle("-fx-alignment: CENTER;");

        tcSalesQuotToOrderCmpTRowLevelB.setCellValueFactory(cellData -> cellData.getValue().level_b_idProperty());
        tcSalesQuotToOrderCmpTRowLevelB.setCellFactory(column -> new ComboBoxTableCellForLevelBSalesQuotToOrder("tcSalesQuotToOrderCmpTRowLevelB"));

        tcSalesQuotToOrderCmpTRowLevelA.setCellValueFactory(cellData -> cellData.getValue().level_a_idProperty());
        tcSalesQuotToOrderCmpTRowLevelA.setCellFactory(column -> new ComboBoxTableCellForLevelASalesQuotToOrder("tcSalesQuotToOrderCmpTRowLevelA"));

        tcSalesQuotToOrderCmpTRowLevelC.setCellValueFactory(cellData -> cellData.getValue().level_c_idProperty());
        tcSalesQuotToOrderCmpTRowLevelC.setCellFactory(column -> new ComboBoxTableCellForLevelCSalesQuotToOrder("tcSalesQuotToOrderCmpTRowLevelC"));


        tblcSalesQuotToOrderUnit.setCellValueFactory(cellData -> cellData.getValue().unitProperty());
        tblcSalesQuotToOrderUnit.setCellFactory(column -> new ComboBoxTableCellForUnitSalesQuotToOrder("tblcSalesQuotToOrderUnit", unit_callback));

        tblcSalesQuotToOrderParticular.setCellValueFactory(cellData -> cellData.getValue().particularsProperty());
        tblcSalesQuotToOrderParticular.setCellFactory(column -> new TextFieldTableCellForSaleQuotationToOrder("tblcSalesQuotToOrderParticular", callback, productCall, tfSalesQuotToOrderNarration));


//        tcBatch.setCellValueFactory(cellData -> cellData.getValue().batch_or_serialProperty());
//        tcBatch.setCellFactory(column -> new com.opethic.genivis.controller.tranx_purchase.TextFieldTableCellForSaleQuotation("tcBatch",callback));


        tblcSalesQuotToOrderQty.setCellValueFactory(cellData -> cellData.getValue().quantityProperty());
        tblcSalesQuotToOrderQty.setCellFactory(column -> new TextFieldTableCellForSaleQuotationToOrder("tblcSalesQuotToOrderQty", callback));

        tblcSalesQuotToOrderFreeqty.setCellValueFactory(cellData -> cellData.getValue().freeProperty());//new2.0
        tblcSalesQuotToOrderFreeqty.setCellFactory(column -> new TextFieldTableCellForSaleQuotationToOrder("tblcSalesQuotToOrderFreeqty", callback));//new2.0

        tblcSalesQuotToOrderRate.setCellValueFactory(cellData -> cellData.getValue().rateProperty());
        tblcSalesQuotToOrderRate.setCellFactory(column -> new TextFieldTableCellForSaleQuotationToOrder("tblcSalesQuotToOrderRate", callback));

        tblcSalesQuotToOrderGrossAmt.setCellValueFactory(cellData -> cellData.getValue().gross_amtProperty());
        tblcSalesQuotToOrderGrossAmt.setCellFactory(column -> new TextFieldTableCellForSaleQuotationToOrder("tblcSalesQuotToOrderGrossAmt", callback));

        tblcSalesQuotToOrderDisPer.setCellValueFactory(cellData -> cellData.getValue().dis_perProperty());
        tblcSalesQuotToOrderDisPer.setCellFactory(column -> new TextFieldTableCellForSaleQuotationToOrder("tblcSalesQuotToOrderDisPer", callback));

//        tblcSalesQuotCreateDisPer.setCellValueFactory(cellData -> cellData.getValue().dis_perProperty());
//        tblcSalesQuotCreateDisPer.setCellFactory(column -> new com.opethic.genivis.controller.tranx_purchase.TextFieldTableCellForSaleQuotation("tcDisPer",callback));

        tblcSalesQuotToOrderDisRs.setCellValueFactory(cellData -> cellData.getValue().dis_amtProperty());
        tblcSalesQuotToOrderDisRs.setCellFactory(column -> new TextFieldTableCellForSaleQuotationToOrder("tblcSalesQuotToOrderDisRs", callback));

        tblcSalesQuotToOrderTax.setCellValueFactory(cellData -> cellData.getValue().tax_perProperty());
        tblcSalesQuotToOrderTax.setCellFactory(column -> new TextFieldTableCellForSaleQuotationToOrder("tblcSalesQuotToOrderTax", callback));

        tblcSalesQuotToOrderNetAmt.setCellValueFactory(cellData -> cellData.getValue().net_amtProperty());
        tblcSalesQuotToOrderNetAmt.setCellFactory(column -> new TextFieldTableCellForSaleQuotationToOrder("tblcSalesQuotToOrderNetAmt", callback));
        tblcSalesQuotToOrderCmpTRowActions.setCellValueFactory(cellData -> cellData.getValue().actionProperty());
        tblcSalesQuotToOrderCmpTRowActions.setCellFactory(column -> new ButtonTableCellQuotToOrder());

    }

    public void sceneInitilization() {
        PurInvoiceCommunicator.unitForPurInvoiceList = FXCollections.observableArrayList();//new
        spSlsQuotoOrderRootPane.sceneProperty().addListener((observable, oldScene, newScene) -> {
            if (newScene != null && newScene.getWindow() instanceof Stage) {
                Communicator.stage = (Stage) newScene.getWindow();
            }
        });
    }

    /**** ADDING ROW for product adding ****/
    public void addRow(ActionEvent actionEvent) {
        SalesQuotToOrderListLogger.info("Adding new row to add another product in sales Quotation");
        tblvSalesQuotToOrderCmptRow.getItems().add(new SalesQuotationProductDTO("", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", ""));
    }

    //todo: get and set the Serial No and Order No
    private void getSalesOrderSerialNo() {
        HttpResponse<String> response = APIClient.getRequest(EndPoints.SALESORDER_GET_LAST_SALES_ORDER_RECORD);
        String responseBody = response.body();
        JsonObject jsonObject = new Gson().fromJson(responseBody, JsonObject.class);
        System.out.println("get_last_po_invoice_record-->" + jsonObject);
        if (jsonObject.get("responseStatus").getAsInt() == 200) {
            String count = jsonObject.get("count").getAsString();
            String OrderNo = jsonObject.get("serialNo").getAsString();
            System.out.println("count =>" + count);
            tfSalesQuotToOrderSalesSerial.setText(count);
            tfSalesQuotToOrderOrderNo.setText(OrderNo);
        }
    }

    public void getSalesQuotById() {
        try {
            SalesQuotToOrderListLogger.info("Fetching SalesQuotation Data by id");
            System.out.println("SalesQuotId to update " + SalesQuotId);

            Map<String, String> body = new HashMap<>();
//            JsonObject obj1 = new JsonObject();
//            JsonArray array = new JsonArray();
//
//            obj1.addProperty("id", SalesQuotInvId);
//            array.add(obj1);
            body.put("sale_quotation_ids", QuotaToOrderId);
            System.out.println("requestBody " + body);
//            SalesQuotToOrderListLogger.debug("SalesQuotInvId 123 " + SalesQuotInvId);
            String requestBody = Globals.mapToStringforFormData(body);
            HttpResponse<String> response = APIClient.postFormDataRequest(requestBody, EndPoints.GET_SALES_QUOTATION_WITH_IDS_QUOTTOORDER);
            SalesQuotToOrderListLogger.debug("response data of SalesQuotToOrder " + response.body());
            SalesQuotToOrderMainDataDTO responseBody = new Gson().fromJson(response.body(), SalesQuotToOrderMainDataDTO.class);
            System.out.println("SalesQuotToOrderData " + responseBody.getMessage());
            if (responseBody.getResponseStatus() == 200) {
                //todo: activating the ledger tab
                System.out.println("Api hit successfully");
                setSalesQuotationFormData(responseBody);
                //todo: setting data in Ledger details block in sales quotation page

            } else {

                SalesQuotToOrderListLogger.error("Error in response of fetching Sales Quotation Data by id");
            }
        } catch (Exception e) {
            SalesQuotToOrderListLogger.error("Error in Fetching Sales Quotation Data by id is" + e.getMessage());
            e.printStackTrace();
        }
    }

    public void setSalesQuotationFormData(SalesQuotToOrderMainDataDTO responseBody) {
//        SalesQuotToOrderInvData
//        fetchSelectedLedgerData(responseBody.getInvoiceData().getDebtorId().toString());
        LocalDate tranxDate = LocalDate.parse(responseBody.getInvoiceData().getSqTransactionDt());
        LocalDate OrderDate = LocalDate.parse(responseBody.getInvoiceData().getInvoiceDt());
        tfSalesQuotToOrderTranxDate.setText(tranxDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        tfSalesQuotToOrderOrderDate.setText(OrderDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
//        tfSalesQuotToOrderTranxDate.setText(String.valueOf(tranxDate));
//        tfSalesQuotToOrderOrderDate.setText(String.valueOf(QuotationDate));
//        tfSalesQuotToOrderOrderNo.setText(responseBody.getInvoiceData().getSqBillNo());
//        tfSalesQuotToOrderSalesSerial.setText(String.valueOf(responseBody.getInvoiceData().getSalesSrNo()));
        Debtor_id = responseBody.getInvoiceData().getDebtorsId().toString();
        ledgerStateCode = responseBody.getInvoiceData().getLedgerStateCode();//new
        System.out.println("ledgerStateCode " + ledgerStateCode);
        fetchSelectedLedgerData(Debtor_id);
        System.out.println("set Debtor_id  " + Debtor_id);
        tfSalesQuotToOrderLedgerName.setText(String.valueOf(responseBody.getInvoiceData().getDebtorsName()));
        tfSalesQuotToOrderNarration.setText(responseBody.getInvoiceData().getNarration());

        if (responseBody.getGstDetails() != null) {
            ObservableList<GstDetailsDTO> gstDetailsList = FXCollections.observableArrayList();
            gstDetailsList.addAll(responseBody.getGstDetails());
            cmbSalesQuotToOrderClientGST.setItems(gstDetailsList);

            String gstNo = responseBody.getInvoiceData().getGstNo();
            for (GstDetailsDTO gstDetailsDTO : gstDetailsList) {
                if (gstDetailsDTO.getGstNo().equalsIgnoreCase(gstNo)) {
                    cmbSalesQuotToOrderClientGST.getSelectionModel().select(gstDetailsDTO);
                }
            }

            cmbSalesQuotToOrderClientGST.setConverter(new StringConverter<GstDetailsDTO>() {
                @Override
                public String toString(GstDetailsDTO gstDetailsDTO) {
                    return gstDetailsDTO.getGstNo();
                }

                @Override
                public GstDetailsDTO fromString(String s) {
                    return null;
                }
            });
        }
        tblvSalesQuotToOrderCmptRow.getItems().clear();
        int index = 0;

        Double totaldis = 0.0, totalTax = 0.0;
        Double totalGrossAmt = 0.0, totalTaxable = 0.0, totalBillAmt = 0.0;

        int count = 0;
        int sr = 1;
        System.out.println("Size:" + responseBody.getRow1().size());
        for (Row1 mRow : responseBody.getRow1()) {
            count++;
            System.out.println("count " + count);
            productId = String.valueOf(mRow.getProductId());
            //Pass productId to ProductPopup for Focus in open ProductPopup in Edit sales Quot
            if (productId != "") {
                SingleInputDialogs.productId = productId;
            } else {
                SingleInputDialogs.productId = "";
            }
            ledgerId = Debtor_id;
            System.out.println("Edit Data" + productId);
            fetchSelectedProductData(productId);
            getSupplierListbyProductId(productId);

            SalesQuotationProductDTO row = new SalesQuotationProductDTO();
            row.setSr_no(String.valueOf(sr));
            TranxSelectedProduct selectedProduct = TranxCommonPopUps.getSelectedProductFromProductId(mRow.getProductName());//new
            row.setParticulars(mRow.getProductName());
            row.setProduct_id(mRow.getProductId().toString());
            row.setPacking(mRow.getPackName());
            row.setQuantity(String.valueOf(mRow.getQty().intValue()));
//            row.setBatchNo(mRow.getBatchNo());
            row.setSelectedProduct(selectedProduct);//new
            row.setUnit("");//new
            row.setUnit_id(mRow.getUnitId().toString());
            System.out.println("mRow.getRate().toString() " + mRow.getRate().toString());
            row.setRate(mRow.getRate().toString());
            row.setGross_amt(String.valueOf(mRow.getGrossAmt()));
            row.setDis_per(String.valueOf(mRow.getDisPer()));
            row.setDis_amt(String.valueOf(mRow.getDisAmt()));
            row.setTax_per(String.valueOf(mRow.getIgst()));
            row.setNet_amt(String.valueOf(mRow.getFinalAmt()));
            row.setFree_qty(String.valueOf(mRow.getFreeQty()));
            row.setIgst(mRow.getIgst());
            row.setSgst(mRow.getSgst());
            row.setCgst(mRow.getCgst());
            row.setTotal_igst(mRow.getTotalIgst());
            row.setTotal_sgst(mRow.getTotalSgst());
            row.setTotal_cgst(mRow.getTotalCgst());
            row.setTax_per(String.valueOf(mRow.getGst()));
            row.setTaxable_amt(mRow.getGrossAmt());
            row.setDetails_id(String.valueOf(mRow.getDetailsId()));
            row.setGross_amt(String.valueOf(mRow.getGrossAmt()));

//            cmpTRowDTO.setBatchNo(mRow.getBatchNo());
//            row.setTax_per(String.valueOf(mRow.getIgst()));
            row.setLevelA(mRow.getLevelA() !=null ?mRow.getLevelA() : "");
            row.setLevelA(mRow.getLevelB() !=null ?mRow.getLevelB() : "");
            row.setLevelA(mRow.getLevelC() !=null ?mRow.getLevelC() : "");

            row.setLevel_a_id(String.valueOf(mRow.getLevelAId()));
            row.setLevel_b_id(String.valueOf(mRow.getLevelBId()));
            row.setLevel_c_id(String.valueOf(mRow.getLevelCId()));
            row.setUnit_conv(mRow.getUnitConv());
            row.setReference_id(mRow.getReferenceId().toString());
            row.setReference_type(mRow.getReferenceType());
//            cmpTRowDTO.setBase_amt(String.valueOf(mRow.getBaseAmt()));
//            cmpTRowDTO.setB_no(mRow.getBatchNo());
//            cmpTRowDTO.setB_rate(String.valueOf(mRow.getbRate()));
//            cmpTRowDTO.setRate_a(String.valueOf(mRow.getMinRateA()));
//            cmpTRowDTO.setRate_b(String.valueOf(mRow.getMinRateB()));
//            cmpTRowDTO.setRate_c(String.valueOf(mRow.getMinRateC()));
//            cmpTRowDTO.setCosting(String.valueOf(mRow.getCosting()));
//            cmpTRowDTO.setCosting_with_tax(String.valueOf(mRow.getCostingWithTax()));
//            cmpTRowDTO.setMin_margin(String.valueOf(mRow.getMinMargin()));
//            cmpTRowDTO.setB_expiry(mRow.getbExpiry());
//            cmpTRowDTO.setIs_batch(String.valueOf(mRow.getIsBatch()));
//            cmpTRowDTO.setManufacturing_date(String.valueOf(mRow.getManufacturingDate()));
//            cmpTRowDTO.setB_purchase_rate(String.valueOf(mRow.getPurchaseRate()));
//            cmpTRowDTO.setB_details_id(String.valueOf(mRow.getbDetailsId()));

            row.setFree_qty(String.valueOf(mRow.getFreeQty()));
            row.setSerialNo("");
//            row.setReference_id("");
//            row.setReference_type("");
            row.setGst(Double.valueOf(mRow.getDetailsId()));
            row.setBase_amt(0.0);
            row.setRow_dis_amt(0.0);
            row.setDisPer2(String.valueOf(mRow.getDisPer2()));
            row.setTotal_amt(0.0);
            row.setIgst(mRow.getIgst());
            row.setCgst(mRow.getCgst());
            row.setSgst(mRow.getSgst());
            row.setTotal_igst(mRow.getTotalIgst());
            row.setTotal_sgst(mRow.getTotalSgst());
            row.setTotal_cgst(mRow.getTotalCgst());
            row.setFinal_amt(mRow.getFinalAmt());
            // row.setRo(mRow.get());
//            cmpTRowDTO.setIs_batch(String.valueOf(mRow.getIsBatch()));

            //new start
            List<LevelAForPurInvoice> levelAForPurInvoiceList = ProductUnitsPackingSaleQuotToOrder.getAllProductUnitsPackingFlavour(row.getProduct_id());
            System.out.println("levelAForPurInvoiceList" + levelAForPurInvoiceList);

            //                int index = getTableRow().getIndex();
            ObservableList<LevelAForPurInvoice> observableLevelAList = FXCollections.observableArrayList(levelAForPurInvoiceList);
            PurInvoiceCommunicator.levelAForPurInvoiceObservableList.add(observableLevelAList);
            //new end


            totalGrossAmt = totalGrossAmt + (mRow.getGrossAmt());
            System.out.println("row totalTaxable " + mRow.getBaseAmt());
            totalTaxable = totalTaxable + mRow.getBaseAmt();
            System.out.println("mRow.getFinalAmt() " + mRow.getFinalAmt());
            totalBillAmt = totalBillAmt + mRow.getFinalAmt();

            totaldis = totaldis + (mRow.getRowDisAmt());
            System.out.println("totaldis" + totaldis);
            totalTax = totalTax + (mRow.getTotalIgst());
            System.out.println("totalTax" + totalTax);
            lbSalesQuotToOrderDiscount.setText(String.format("%.2f",totaldis));
            lbSalesQuotToOrderTax.setText(String.format("%.2f",totalTax));
            lbSalesQuotToOrderBillAmount.setText(String.format("%.2f",totalBillAmt));
            lbSalesQuotToOrderTotal.setText(String.format("%.2f",totalTaxable));
            lbSalesQuotToOrderGrossTotal.setText(String.format("%.2f",totalGrossAmt));


            tblvSalesQuotToOrderCmptRow.getItems().add(row);

            index++;
            sr++;
        }

        Platform.runLater(() -> {
            tblvSalesQuotToOrderCmptRow.refresh();
        });
        System.out.println("Index--->" + index);

    }

    // todo:   all Row Calculation
    private void calculateEachRowTraxTotal() {

        Double totalGrossAmt = 0.0, grossAmt = 0.0;
        Double totaltaxableAmt = 0.0, taxableAmt = 0.0;
        Double totalDisAmt = 0.0, disAmt = 0.0;
        Double totalTaxAmt = 0.0, taxAmt = 0.0;
        Double totalNetAmt = 0.0, netAmount = 0.0;
        Long totalQty = 0L, qty = 0L;
        Double totalFreeQty = 0.0, freeQty = 0.0;

        for (SalesQuotationProductDTO cmpTRowDTO : tblvSalesQuotToOrderCmptRow.getItems()) {

//            Double taxAmt1 = cmpTRowDTO.getFinTaxAmt();
//            Double disAmt2 = cmpTRowDTO.getFinal_dis_amt();
//            System.out.println("sasasasasasas"+taxAmt1+disAmt2);

            //Total Calculation of gross amt ,taxable ,tax,discount
            //todo: Net amount
            if (cmpTRowDTO.getNet_amt() != null) {

                netAmount = parseDouble(cmpTRowDTO.getNet_amt());
                totalNetAmt += netAmount;
            } else {
                totalNetAmt = 0.0;
            }
            System.out.println("total net amount : " + totalNetAmt);

            //todo: Gross amount
            if (cmpTRowDTO.getGross_amt() != null) {
                grossAmt = parseDouble(cmpTRowDTO.getGross_amt());
                totalGrossAmt += grossAmt;
            } else {
                totalGrossAmt = 0.0;
            }
            System.out.println("total gross amount : " + totalGrossAmt);


            //todo: taxable amount
            if (cmpTRowDTO.getTaxable_amt() != null) {
                taxableAmt = cmpTRowDTO.getTaxable_amt();
                totaltaxableAmt += taxableAmt;
            } else {
                totaltaxableAmt = 0.0;
            }
            System.out.println("total amount : " + totaltaxableAmt);


            //todo: discount amount
            if (cmpTRowDTO.getRow_dis_amt() != null) {
                disAmt = cmpTRowDTO.getRow_dis_amt();
                totalDisAmt += disAmt;
            } else {
                totalDisAmt = 0.0;
            }
            System.out.println("total discount amount : " + totalDisAmt);


            //todo: Tax amount
            if (cmpTRowDTO.getTotal_igst() != null) {
                taxAmt = cmpTRowDTO.getTotal_igst();
                totalTaxAmt += taxAmt;
            } else {
                totalTaxAmt = 0.0;
            }
            System.out.println("total tax amount : " + totalTaxAmt);

            //todo: Total Quantity
            if (cmpTRowDTO.getQuantity() != null) {
                qty = Long.valueOf(cmpTRowDTO.getQuantity()
                );
                totalQty += qty;
            } else {
                totalQty = 0L;
            }
            System.out.println("total Quantity : " + totalQty);

            //todo: Total free Quantity
            if (cmpTRowDTO.getFree_qty() != null) {
                freeQty = cmpTRowDTO.getFree_qty() != null ? Double.valueOf(cmpTRowDTO.getFree_qty()) : 0.0;
                totalFreeQty += freeQty;
            } else {
                totalFreeQty = 0.0;
            }
            System.out.println("total Free Quantity : " + totalFreeQty);

        }
        TotalFreeQty = totalFreeQty;
        TotalQty = totalQty;
        System.out.println("TotalFreeQty " + TotalFreeQty + "TotalQty " + TotalQty);
        //Display  of gross amt ,taxable ,tax,discount
        lbSalesQuotToOrderBillAmount.setText(String.format("%.2f", totalNetAmt));

        lbSalesQuotToOrderGrossTotal.setText(String.format("%.2f", totalGrossAmt));

        lbSalesQuotToOrderDiscount.setText(String.format("%.2f", totalDisAmt));

        lbSalesQuotToOrderTax.setText(String.format("%.2f", totalTaxAmt));

        lbSalesQuotToOrderTotal.setText(String.format("%.2f", (totalGrossAmt - totalDisAmt)));

    }


    //todo: fetch Selected ledger data
    public void fetchSelectedLedgerData(String LedgerId) {
        try {
            SalesQuotToOrderListLogger.info("Fetching Selected Ledger Data to show in Ledger info");
            Map<String, String> body = new HashMap<>();
            body.put("ledger_id", LedgerId);
            System.out.println("ledger_id 123 " + LedgerId);
            String requestBody = Globals.mapToStringforFormData(body);
            HttpResponse<String> response = APIClient.postFormDataRequest(requestBody, EndPoints.TRANSACTION_LEDGER_DETAILS);
            SalesQuotToOrderListLogger.info("response data of selected Ledger " + response.body());
            JsonObject jsonObject = new Gson().fromJson(response.body(), JsonObject.class);
            ObservableList<TranxLedgerWindowDTO> observableList = FXCollections.observableArrayList();
            System.out.println("fetchedSelectedLedgerData " + jsonObject);
            if (jsonObject.get("responseStatus").getAsInt() == 200) {
                //todo: activating the ledger tab
                tpSalesQuotToOrder.getSelectionModel().select(tabSalesQuotToOrderLedger);

                CompanyStateCode = jsonObject.get("company_state_code").getAsLong();
                System.out.println("CompanyStateCode " + CompanyStateCode);
                JsonObject item = jsonObject.get("result").getAsJsonObject();
                stateCode = item.get("stateCode").getAsLong();
//                ledgerStateCode = item.get("stateCode").getAsString();
                System.out.println("stateCode " + stateCode);

                System.out.println("Item:" + item);
                String gstNo = item.get("gst_number").getAsString();
                String Area = item.get("area").getAsString();
                String bank = item.get("bank_name").getAsString();
                String contact_person = item.get("contact_name").getAsString();
                String mobile_no = item.get("contact_no").getAsString();
                String credit_days = item.get("credit_days").getAsString();
                String fssai = item.get("fssai_number").getAsString();
                String licence_no = item.get("license_number").getAsString();
                String route = item.get("route").getAsString();

//setting data in Ledger details block in sales quotation page

                lbSalesQuotToOrderledInfogstNo.setText(gstNo);
                lbSalesQuotToOrderledInfoArea.setText(Area);
                lbSalesQuotToOrderledInfoBank.setText(bank);
                lbSalesQuotToOrderledInfoContPerson.setText(contact_person);
                lbSalesQuotToOrderledInfoMobNo.setText(mobile_no);
                lbSalesQuotToOrderledInfoCreditDays.setText(credit_days);
                lbSalesQuotToOrderledInfoFssai.setText(fssai);
                lbSalesQuotToOrderledInfoLiceNo.setText(licence_no);
                lbSalesQuotToOrderledInfoRoute.setText(route);

            } else {

                SalesQuotToOrderListLogger.error("Error in response of fetching selected Ledger data");
            }
        } catch (Exception e) {
            SalesQuotToOrderListLogger.error("Error in Fetching Selected Ledger data is" + e.getMessage());
            e.printStackTrace();
        }
    }

    //todo: fetch Selected Product data for product info
    public void fetchSelectedProductData(String ProductId) {
        try {
            SalesQuotToOrderListLogger.info("Fetching Selected Product Data to show in product info");
            Map<String, String> body = new HashMap<>();
            body.put("product_id", ProductId);
            System.out.println("ProductId 123 " + ProductId);
            String requestBody = Globals.mapToStringforFormData(body);
            HttpResponse<String> response = APIClient.postFormDataRequest(requestBody, EndPoints.TRANSACTION_PRODUCT_DETAILS);
            System.out.println("Product Details response body" + response.body());
            JsonObject jsonObject = new Gson().fromJson(response.body(), JsonObject.class);
            ObservableList<TranxProductWindowDTO> observableList = FXCollections.observableArrayList();
            //todo: activating the product tab
//            tpSalesQuotToOrder.getSelectionModel().select(tabSalesQuotaCreateProduct);
            if (jsonObject.get("responseStatus").getAsInt() == 200) {
                tpSalesQuotToOrder.getSelectionModel().select(tabSalesQuotToOrderProduct);

                JsonObject item = jsonObject.get("result").getAsJsonObject();
                System.out.println("Item:" + item);
                String brand = item.get("brand").getAsString();
                String hsn = item.get("hsn").getAsString();
                String group = item.get("group").getAsString();
                String category = item.get("category").getAsString();
                String supplier = item.get("supplier").getAsString();
                String tax_type = item.get("tax_type").getAsString();
                String tax = item.get("tax_per").getAsString();
//                System.out.println("tax_per...... " + tax);
                String margin = item.get("margin_per").getAsString();
                String cost = item.get("cost").getAsString();
                String shelfId = item.get("shelf_id").getAsString();
                String min_stock = item.get("min_stocks").getAsString();
                String max_stock = item.get("max_stocks").getAsString();
                //setting data in Product details block in sales quotation page
                lbSalesQuotToOrderProdInfoBrand.setText(brand);
                lbSalesQuotToOrderProdInfoHSN.setText(hsn);
                lbSalesQuotToOrderProdInfoGroup.setText(group);
                lbSalesQuotToOrderProdInfoCategory.setText(category);
                lbSalesQuotToOrderProdInfoSupplier.setText(supplier);
                lbSalesQuotToOrderProdInfoTaxType.setText(tax_type);
                lbSalesQuotToOrderProdInfoTax.setText(tax);
                lbSalesQuotToOrderProdInfoMargin.setText(margin);
                lbSalesQuotToOrderProdInfoCost.setText(cost);
                lbSalesQuotToOrderProdInfoShelfId.setText(shelfId);
                lbSalesQuotToOrderProdInfoMinStock.setText(min_stock);
                lbSalesQuotToOrderProdInfoMaxStock.setText(max_stock);

            } else {
                System.out.println("Error in response");
            }
        } catch (Exception e) {
            SalesQuotToOrderListLogger.error("Error in Fetching Selected Product data is" + e.getMessage());
            e.printStackTrace();
        }
    }

    private void getSupplierListbyProductId(String id) {
        //todo: activating the product tab
//        tpSalesOrder.getSelectionModel().select(tabSalesOrderProduct);

        Map<String, String> map = new HashMap<>();
        map.put("productId", id);
        String formData = Globals.mapToStringforFormData(map);
        System.out.println("FormData: " + formData);
        HttpResponse<String> response = APIClient.postFormDataRequest(formData, EndPoints.GET_SUPPLIERLIST_BY_PRODUCTID_ENDPOINT);
        String responseBody = response.body();
        JsonObject jsonObject = new Gson().fromJson(responseBody, JsonObject.class);
        System.out.println("Supplier List Res ==>" + jsonObject);
        if (jsonObject.get("responseStatus").getAsInt() == 200) {
//            JsonObject resultObj = jsonObject.getAsJsonObject("result");
            JsonArray dataArray = jsonObject.getAsJsonArray("data");
            //todo: getting values
            System.out.println("i am the bosh" + dataArray);
            ObservableList<SalesOrderSupplierDetailsDTO> supplierDataList = FXCollections.observableArrayList();
            for (JsonElement element : dataArray) {
                JsonObject item = element.getAsJsonObject();
                String supplierName = item.get("supplier_name").getAsString();
                String supplierInvNo = item.get("invoice_no").getAsString();
                String supplierInvDate = item.get("invoice_date").getAsString();
                String supplierBatch = item.get("batch").getAsString();
                String supplierMrp = item.get("mrp").getAsString();
                String supplierQty = item.get("rate").getAsString();
                String supplierRate = item.get("quantity").getAsString();
                String supplierCost = item.get("cost").getAsString();
                String supplierDisPer = item.get("dis_per").getAsString();
                String supplierDisAmt = item.get("dis_amt").getAsString();

                supplierDataList.add(new SalesOrderSupplierDetailsDTO(supplierName, supplierInvNo, supplierInvDate, supplierBatch, supplierMrp, supplierRate, supplierQty, supplierCost, supplierDisPer, supplierDisAmt));
            }
            tblcSalesQuotToOrderInvClienName.setCellValueFactory(new PropertyValueFactory<>("supplierName"));
            tblcSalesQuotToOrderInvInvoiceNo.setCellValueFactory(new PropertyValueFactory<>("supplierInvNo"));
            tblcSalesQuotToOrderInvInvoiceDate.setCellValueFactory(new PropertyValueFactory<>("supplierInvDate"));
            tblcSalesQuotToOrderInvBatch.setCellValueFactory(new PropertyValueFactory<>("supplierBatch"));
            tblcSalesQuotToOrderInvMrp.setCellValueFactory(new PropertyValueFactory<>("supplierMrp"));
            tblcSalesQuotToOrderInvQuantity.setCellValueFactory(new PropertyValueFactory<>("supplierQty"));
            tblcSalesQuotToOrderInvRate.setCellValueFactory(new PropertyValueFactory<>("supplierRate"));
            tblcSalesQuotToOrderInvCost.setCellValueFactory(new PropertyValueFactory<>("supplierCost"));
            tblcSalesQuotToOrderInvDisPer.setCellValueFactory(new PropertyValueFactory<>("supplierDisPer"));
            tblcSalesQuotToOrderInvDis.setCellValueFactory(new PropertyValueFactory<>("supplierDisAmt"));

            tvSalesQuotToOrderprodInvoiceDetails.setItems(supplierDataList);

        }


    }

    //todo:get sales account dropdown option using api
    public void getSalesAccounts() {
        try {
            HttpResponse<String> response = APIClient.getRequest(EndPoints.SALESQUOTATION_GET_SALES_QUOTATION_ACCOUNTS);
            JsonObject jsonObject = new Gson().fromJson(response.body(), JsonObject.class);
            System.out.println(jsonObject);
            ObservableList<CommonDTO> observableList = FXCollections.observableArrayList();
            if (jsonObject.get("responseStatus").getAsInt() == 200) {
                JsonArray responseObject = jsonObject.getAsJsonArray("list");
                System.out.println("responseObject getPurchaseAccounts " + responseObject);
                if (responseObject.size() > 0) {
                    for (JsonElement element : responseObject) {
                        JsonObject item = element.getAsJsonObject();
                        String id = item.get("id").getAsString();
                        String name = item.get("name").getAsString();
                        String unique_code = item.get("unique_code").getAsString();


                        observableList.add(new CommonDTO(name, id));
                        cmbSalesQuotToOrderSalesAC.setItems(observableList);
                        cmbSalesQuotToOrderSalesAC.getSelectionModel().selectFirst();
                        cmbSalesQuotToOrderSalesAC.setConverter(new StringConverter<CommonDTO>() {
                            @Override
                            public String toString(CommonDTO o) {
                                return o.getText();
                            }

                            @Override
                            public CommonDTO fromString(String s) {
                                return null;
                            }

                        });
//                        tfSalesQuotationCreateQuotationNo.requestFocus();

                    }


                } else {
                    System.out.println("responseObject is null");
                }
            } else {
                System.out.println("Error in response");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void responsiveTable() {
        //todo: Responsive code for tableview
        tblcSalesQuotToOrderSrNo.prefWidthProperty().bind(tblvSalesQuotToOrderCmptRow.widthProperty().multiply(0.05));
//        tblcSalesQuotCreateParticular.prefWidthProperty().bind(tblvSalesQuotToOrderCmptRow.widthProperty().multiply(0.3));
        tblcSalesQuotToOrderPackage.prefWidthProperty().bind(tblvSalesQuotToOrderCmptRow.widthProperty().multiply(0.05));
        tblcSalesQuotToOrderUnit.prefWidthProperty().bind(tblvSalesQuotToOrderCmptRow.widthProperty().multiply(0.05));
        tblcSalesQuotToOrderQty.prefWidthProperty().bind(tblvSalesQuotToOrderCmptRow.widthProperty().multiply(0.05));
//        tblcSalesQuotCreateGrossAmt.prefWidthProperty().bind(tblvSalesQuotToOrderCmptRow.widthProperty().multiply(0.15));
        tblcSalesQuotToOrderDisPer.prefWidthProperty().bind(tblvSalesQuotToOrderCmptRow.widthProperty().multiply(0.05));
        tblcSalesQuotToOrderDisRs.prefWidthProperty().bind(tblvSalesQuotToOrderCmptRow.widthProperty().multiply(0.05));
        tblcSalesQuotToOrderTax.prefWidthProperty().bind(tblvSalesQuotToOrderCmptRow.widthProperty().multiply(0.05));
//        tblcSalesQuotCreateNetAmt.prefWidthProperty().bind(tblvSalesQuotToOrderCmptRow.widthProperty().multiply(0.08));
        tblcSalesQuotToOrderCmpTRowActions.prefWidthProperty().bind(tblvSalesQuotToOrderCmptRow.widthProperty().multiply(0.02));
        if (tblcSalesQuotToOrderFreeqty.isVisible()) {
            tblcSalesQuotToOrderFreeqty.prefWidthProperty().bind(tblvSalesQuotToOrderCmptRow.widthProperty().multiply(0.05));
            tblcSalesQuotToOrderRate.prefWidthProperty().bind(tblvSalesQuotToOrderCmptRow.widthProperty().multiply(0.05));
        } else {
            tblcSalesQuotToOrderRate.prefWidthProperty().bind(tblvSalesQuotToOrderCmptRow.widthProperty().multiply(0.1));
        }


        if (tcSalesQuotToOrderCmpTRowLevelA.isVisible() == true && tcSalesQuotToOrderCmpTRowLevelB.isVisible() == false && tcSalesQuotToOrderCmpTRowLevelC.isVisible() == false) {
            System.out.println("inside first condn");
            tblcSalesQuotToOrderParticular.prefWidthProperty().bind(tblvSalesQuotToOrderCmptRow.widthProperty().multiply(0.3));
            tblcSalesQuotToOrderGrossAmt.prefWidthProperty().bind(tblvSalesQuotToOrderCmptRow.widthProperty().multiply(0.13));
            tblcSalesQuotToOrderNetAmt.prefWidthProperty().bind(tblvSalesQuotToOrderCmptRow.widthProperty().multiply(0.05));
            tcSalesQuotToOrderCmpTRowLevelA.prefWidthProperty().bind(tblvSalesQuotToOrderCmptRow.widthProperty().multiply(0.05));
        } else if (tcSalesQuotToOrderCmpTRowLevelA.isVisible() == true && tcSalesQuotToOrderCmpTRowLevelB.isVisible() == true && tcSalesQuotToOrderCmpTRowLevelC.isVisible() == false) {
            System.out.println("inside second condn");
            tcSalesQuotToOrderCmpTRowLevelA.prefWidthProperty().bind(tblvSalesQuotToOrderCmptRow.widthProperty().multiply(0.05));
            tblcSalesQuotToOrderParticular.prefWidthProperty().bind(tblvSalesQuotToOrderCmptRow.widthProperty().multiply(0.28));
            tblcSalesQuotToOrderGrossAmt.prefWidthProperty().bind(tblvSalesQuotToOrderCmptRow.widthProperty().multiply(0.1));
            tblcSalesQuotToOrderNetAmt.prefWidthProperty().bind(tblvSalesQuotToOrderCmptRow.widthProperty().multiply(0.05));
            tcSalesQuotToOrderCmpTRowLevelB.prefWidthProperty().bind(tblvSalesQuotToOrderCmptRow.widthProperty().multiply(0.05));
        } else if (tcSalesQuotToOrderCmpTRowLevelA.isVisible() == true && tcSalesQuotToOrderCmpTRowLevelB.isVisible() == true && tcSalesQuotToOrderCmpTRowLevelC.isVisible() == true) {
            System.out.println("inside thrd condn");
            tblcSalesQuotToOrderParticular.prefWidthProperty().bind(tblvSalesQuotToOrderCmptRow.widthProperty().multiply(0.23));
            tblcSalesQuotToOrderGrossAmt.prefWidthProperty().bind(tblvSalesQuotToOrderCmptRow.widthProperty().multiply(0.1));
            tblcSalesQuotToOrderNetAmt.prefWidthProperty().bind(tblvSalesQuotToOrderCmptRow.widthProperty().multiply(0.05));
            tcSalesQuotToOrderCmpTRowLevelA.prefWidthProperty().bind(tblvSalesQuotToOrderCmptRow.widthProperty().multiply(0.05));
            tcSalesQuotToOrderCmpTRowLevelB.prefWidthProperty().bind(tblvSalesQuotToOrderCmptRow.widthProperty().multiply(0.05));
            tcSalesQuotToOrderCmpTRowLevelC.prefWidthProperty().bind(tblvSalesQuotToOrderCmptRow.widthProperty().multiply(0.05));
        } else {
            System.out.println("inside fourth condn");
            tblcSalesQuotToOrderParticular.prefWidthProperty().bind(tblvSalesQuotToOrderCmptRow.widthProperty().multiply(0.35));
            tblcSalesQuotToOrderGrossAmt.prefWidthProperty().bind(tblvSalesQuotToOrderCmptRow.widthProperty().multiply(0.1));
            tblcSalesQuotToOrderNetAmt.prefWidthProperty().bind(tblvSalesQuotToOrderCmptRow.widthProperty().multiply(0.08));

        }

    }

    public void responsiveprodInfoTable() {
        //todo: Responsive code for bottom productInfo tableview
        tblcSalesQuotToOrderInvClienName.prefWidthProperty().bind(tvSalesQuotToOrderprodInvoiceDetails.widthProperty().multiply(0.2));
        tblcSalesQuotToOrderInvInvoiceNo.prefWidthProperty().bind(tvSalesQuotToOrderprodInvoiceDetails.widthProperty().multiply(0.1));
        tblcSalesQuotToOrderInvInvoiceDate.prefWidthProperty().bind(tvSalesQuotToOrderprodInvoiceDetails.widthProperty().multiply(0.1));
        tblcSalesQuotToOrderInvBatch.prefWidthProperty().bind(tvSalesQuotToOrderprodInvoiceDetails.widthProperty().multiply(0.1));
        tblcSalesQuotToOrderInvMrp.prefWidthProperty().bind(tvSalesQuotToOrderprodInvoiceDetails.widthProperty().multiply(0.1));
        tblcSalesQuotToOrderInvQuantity.prefWidthProperty().bind(tvSalesQuotToOrderprodInvoiceDetails.widthProperty().multiply(0.1));
        tblcSalesQuotToOrderInvRate.prefWidthProperty().bind(tvSalesQuotToOrderprodInvoiceDetails.widthProperty().multiply(0.05));
        tblcSalesQuotToOrderInvCost.prefWidthProperty().bind(tvSalesQuotToOrderprodInvoiceDetails.widthProperty().multiply(0.1));
        tblcSalesQuotToOrderInvDisPer.prefWidthProperty().bind(tvSalesQuotToOrderprodInvoiceDetails.widthProperty().multiply(0.05));
        tblcSalesQuotToOrderInvDis.prefWidthProperty().bind(tvSalesQuotToOrderprodInvoiceDetails.widthProperty().multiply(0.1));

    }

    public void SalesQuotToOrder() {
        AlertUtility.CustomCallback callback = number -> {
            if (number == 1) {
        LocalDate OrderDate = LocalDate.parse(Communicator.text_to_date.fromString(tfSalesQuotToOrderOrderDate.getText()).toString());
        LocalDate tranxDate = LocalDate.parse(Communicator.text_to_date.fromString(tfSalesQuotToOrderTranxDate.getText()).toString());

        id = SalesQuotId.toString();

        Map<String, String> map = new HashMap<>();

        map.put("debtors_id", ledgerId);

        map.put("bill_dt", String.valueOf(OrderDate));
        map.put("newReference", "false");
        map.put("bill_no", tfSalesQuotToOrderOrderNo.getText());
        map.put("sales_acc_id", cmbSalesQuotToOrderSalesAC.getValue().getId());
        System.out.println("cmbSalesQuotToOrderSalesAC.getValue().toString() " + cmbSalesQuotToOrderSalesAC.getValue().getId());
        map.put("sales_sr_no", tfSalesQuotToOrderSalesSerial.getText());
        map.put("transaction_date", String.valueOf(tranxDate));

        map.put("reference_sq_id", QuotaToOrderId);
        map.put("reference", "SLSQTN");
//        map.put("isRoundOffCheck", String.valueOf(chbPurChallRoundOff.isSelected()));
        map.put("roundoff", "0.00");
        map.put("narration", tfSalesQuotToOrderNarration.getText());
        map.put("totalamt", lbSalesQuotToOrderTotal.getText());
        map.put("total_purchase_discount_amt", lbSalesQuotToOrderDiscount.getText());
        //todo : gst getting
        map.put("gstNo", cmbSalesQuotToOrderClientGST.getValue() != null ? cmbSalesQuotToOrderClientGST.getValue().getGstNo() : "");
//        map.put("totalcgst", finaltotalCGST);
//        map.put("totalsgst", finaltotalSGST);
//        map.put("totaligst", finaltotalIGST);
        map.put("tcs", "0");//static
        map.put("sales_discount", "0");//static
        map.put("sales_discount_amt", "0");//static
        map.put("total_sales_discount_amt", "0");//static
        map.put("additionalChargesTotal", "0");//static
        map.put("additionalCharges", "");
        map.put("total_qty", TotalQty.toString());//static
        map.put("total_free_qty", TotalFreeQty.toString());//static
        map.put("total_row_gross_amt", lbSalesQuotToOrderGrossTotal.getText());
        map.put("total_base_amt", lbSalesQuotToOrderGrossTotal.getText());
        map.put("total_invoice_dis_amt", lbSalesQuotToOrderDiscount.getText());
        map.put("taxable_amount", lbSalesQuotToOrderTotal.getText());
        map.put("total_tax_amt", lbSalesQuotToOrderTax.getText());
        map.put("bill_amount", lbSalesQuotToOrderBillAmount.getText());

        List<SalesQuotationProductDTO> currentItems = new ArrayList<>(tblvSalesQuotToOrderCmptRow.getItems());

        if (!currentItems.isEmpty()) {
            SalesQuotationProductDTO lastItem = currentItems.get(currentItems.size() - 1);

            if (lastItem.getParticulars() != null && lastItem.getParticulars().isEmpty()) {
                currentItems.remove(currentItems.size() - 1);
            }
        }

        List<SalesQuotationProductDTO> list = new ArrayList<>(currentItems);


        JsonArray jsonArray = new JsonArray();
        JsonObject jsonObject = new JsonObject();
        JsonObject jsonObject1 = new JsonObject();
        double taxPer = 0.0;
        for (SalesQuotationProductDTO cmpTRowDTO : list) {
            taxPer = cmpTRowDTO.getIgst() != null ? cmpTRowDTO.getIgst().doubleValue() : 0.0;
            System.out.println("taxPer----- " + taxPer);
        }
        if (CompanyStateCode.equals(stateCode)) {
            map.put("taxFlag", "true");// static data as of now
//            jsonObject.addProperty("d_gst", decimalFormat.format(taxPer));
            jsonObject.addProperty("gst", decimalFormat.format((taxPer) / 2));
            jsonObject.addProperty("amt", decimalFormat.format(Double.parseDouble(lbSalesQuotToOrderTax.getText()) / 2));
            jsonArray.add(jsonObject);

            jsonObject1.add("cgst", jsonArray);
            jsonObject1.add("sgst", jsonArray);
            map.put("taxCalculation", jsonObject1.toString());
            map.put("totalcgst", decimalFormat.format(Double.parseDouble(lbSalesQuotToOrderTax.getText()) / 2));
            map.put("totalsgst", decimalFormat.format(Double.parseDouble(lbSalesQuotToOrderTax.getText()) / 2));
            map.put("totaligst", "0.0");
            System.out.println("taxCalculation_cgst_sgst" + jsonObject1.toString());
        } else {
            map.put("taxFlag", "false");// static data as of now
//            jsonObject.addProperty("d_gst", decimalFormat.format(taxPer));
            jsonObject.addProperty("gst", decimalFormat.format(taxPer));
            jsonObject.addProperty("amt", decimalFormat.format(Double.parseDouble(lbSalesQuotToOrderTax.getText())));
            jsonArray.add(jsonObject);
            jsonObject1.add("igst", jsonArray);
            map.put("taxCalculation", jsonObject1.toString());
            map.put("totalcgst", "0.0");
            map.put("totalsgst", "0.0");
            map.put("totaligst", decimalFormat.format(Double.parseDouble(lbSalesQuotToOrderTax.getText())));
            System.out.println("taxCalculation_igst" + jsonObject1.toString());
        }
//            jsonArray.add(jsonObject);
        System.out.println("taxCalculation>>> " + jsonObject1);


//        map.put("taxCalculation", "12");//static
        System.out.println("qwerty " + map);
        /***** mapping Row Data into TranxPurRowDTO *****/
        ArrayList<TranxPurRowDTO> rowData = new ArrayList<>();
        for (SalesQuotationProductDTO cmpTRowDTO : tblvSalesQuotToOrderCmptRow.getItems()) {
            TranxPurRowDTO purParticularRow = new TranxPurRowDTO();
            purParticularRow.setProductId(cmpTRowDTO.getProduct_id());
            purParticularRow.setReferenceId(cmpTRowDTO.getReference_id());
            purParticularRow.setReferenceType(cmpTRowDTO.getReference_type());
            if (!cmpTRowDTO.getLevel_a_id().isEmpty()) {
                purParticularRow.setLevelaId(cmpTRowDTO.getLevel_a_id());
            } else {
                purParticularRow.setLevelaId("");
            }
            if (!cmpTRowDTO.getLevel_b_id().isEmpty()) {
                purParticularRow.setLevelbId(cmpTRowDTO.getLevel_b_id());
            } else {
                purParticularRow.setLevelbId("");
            }
            if (!cmpTRowDTO.getLevel_c_id().isEmpty()) {
                purParticularRow.setLevelcId(cmpTRowDTO.getLevel_c_id());
            } else {
                purParticularRow.setLevelcId("");
            }
            if (!cmpTRowDTO.getUnit().isEmpty()) {
                purParticularRow.setUnitId("1");
            } else {
                purParticularRow.setUnitId("");
            }
            if (cmpTRowDTO.getUnit_conv() != null) {
                purParticularRow.setUnitConv(String.valueOf(cmpTRowDTO.getUnit_conv()));
            } else {
                purParticularRow.setUnitConv("0.0");
            }
            if (!cmpTRowDTO.getQuantity().isEmpty()) {
                purParticularRow.setQty(cmpTRowDTO.getQuantity());
            } else {
                purParticularRow.setQty("0.0");
            }
            if (!cmpTRowDTO.getFree_qty().isEmpty()) {
                purParticularRow.setFreeQty(cmpTRowDTO.getFree_qty());
            } else {
                purParticularRow.setFreeQty("0.0");
            }
            if (!cmpTRowDTO.getRate().isEmpty()) {
                purParticularRow.setRate(cmpTRowDTO.getRate());
            } else {
                purParticularRow.setRate("0.0");
            }
            if (cmpTRowDTO.getBase_amt() != null) {
                purParticularRow.setBaseAmt(String.valueOf(cmpTRowDTO.getBase_amt()));
            } else {
                purParticularRow.setBaseAmt("0.0");
            }
            if (!cmpTRowDTO.getDis_amt().isEmpty()) {
                purParticularRow.setDisAmt(cmpTRowDTO.getDis_amt());
            } else {
                purParticularRow.setDisAmt("0.0");
            }
            if (!cmpTRowDTO.getDis_per().isEmpty()) {
                purParticularRow.setDisPer(cmpTRowDTO.getDis_per());
            } else {
                purParticularRow.setDisPer("0.0");
            }
            if (!cmpTRowDTO.getDisPer2().isEmpty()) {
                purParticularRow.setDisPer2(cmpTRowDTO.getDis_per());
            } else {
                purParticularRow.setDisPer2("0.0");
            }
            if (cmpTRowDTO.getDis_per_cal() != null) {
                purParticularRow.setDisPerCal(String.valueOf(cmpTRowDTO.getDis_per_cal()));
            } else {
                purParticularRow.setDisPerCal("0.0");
            }
            if (cmpTRowDTO.getDis_amt_cal() != null) {
                purParticularRow.setDisAmtCal(String.valueOf(cmpTRowDTO.getDis_amt_cal()));
            } else {
                purParticularRow.setDisAmtCal("0.0");
            }
            if (cmpTRowDTO.getRow_dis_amt() != null) {
                purParticularRow.setRowDisAmt(String.valueOf(cmpTRowDTO.getRow_dis_amt()));
            } else {
                purParticularRow.setRowDisAmt("0.0");
            }
            if (cmpTRowDTO.getGross_amt() != null) {
                purParticularRow.setGrossAmt(String.valueOf(cmpTRowDTO.getGross_amt()));
            } else {
                purParticularRow.setGrossAmt("0.0");
            }
            if (cmpTRowDTO.getAdd_chg_amt() != null) {
                purParticularRow.setAddChgAmt(String.valueOf(cmpTRowDTO.getAdd_chg_amt()));
            } else {
                purParticularRow.setAddChgAmt("0.0");
            }
            if (cmpTRowDTO.getGross_amt1() != null) {
                purParticularRow.setGrossAmt1(String.valueOf(cmpTRowDTO.getGross_amt1()));
            } else {
                purParticularRow.setGrossAmt1("0.0");
            }
            if (cmpTRowDTO.getInvoice_dis_amt() != null) {
                purParticularRow.setInvoiceDisAmt(String.valueOf(cmpTRowDTO.getInvoice_dis_amt()));
            } else {
                purParticularRow.setInvoiceDisAmt("0.0");
            }
            if (cmpTRowDTO.getTotal_amt() != null) {
                purParticularRow.setTotalAmt(String.valueOf(cmpTRowDTO.getTotal_amt()));
            } else {
                purParticularRow.setTotalAmt("0.0");
            }
            if (cmpTRowDTO.getGst() != null) {
                purParticularRow.setGst(String.valueOf(cmpTRowDTO.getGst()));
            } else {
                purParticularRow.setGst("0.0");
            }
            if (cmpTRowDTO.getCgst() != null) {
                purParticularRow.setCgst(String.valueOf(cmpTRowDTO.getCgst()));
            } else {
                purParticularRow.setCgst("0.0");
            }
            if (cmpTRowDTO.getIgst() != null) {
                purParticularRow.setIgst(String.valueOf(cmpTRowDTO.getIgst()));
            } else {
                purParticularRow.setIgst("0.0");
            }
            if (cmpTRowDTO.getSgst() != null) {
                purParticularRow.setSgst(String.valueOf(cmpTRowDTO.getSgst()));
            } else {
                purParticularRow.setSgst("0.0");
            }
            if (cmpTRowDTO.getTotal_igst() != null) {
                purParticularRow.setTotalIgst(String.valueOf(cmpTRowDTO.getTotal_igst()));
            } else {
                purParticularRow.setTotalIgst("0.0");
            }
            if (cmpTRowDTO.getTotal_cgst() != null) {
                purParticularRow.setTotalCgst(String.valueOf(cmpTRowDTO.getTotal_cgst()));
            } else {
                purParticularRow.setTotalCgst("0.0");
            }
            if (cmpTRowDTO.getTotal_sgst() != null) {
                purParticularRow.setTotalSgst(String.valueOf(cmpTRowDTO.getTotal_sgst()));
            } else {
                purParticularRow.setTotalSgst("0.0");
            }
            if (cmpTRowDTO.getFinal_amt() != null) {
                purParticularRow.setFinalAmt(String.valueOf(cmpTRowDTO.getFinal_amt()));
            } else {
                purParticularRow.setFinalAmt("0.0");
            }
            if (cmpTRowDTO.getSgst() != null) {
                purParticularRow.setSgst(String.valueOf(cmpTRowDTO.getSgst()));
            } else {
                purParticularRow.setSgst("0.0");
            }
            if (cmpTRowDTO.getDetails_id() != null) {
                purParticularRow.setDetailsId(String.valueOf(cmpTRowDTO.getDetails_id()));
            } else {
                purParticularRow.setDetailsId("0");
            }
            rowData.add(purParticularRow);
        }

        String mRowData = new Gson().toJson(rowData, new TypeToken<List<TranxPurRowDTO>>() {
        }.getType());
        System.out.println("mRowData=>" + mRowData);
        map.put("row", mRowData);
        String finalReq = Globals.mapToString(map);
        System.out.println("FinalReq=> i am in" + finalReq);

        HttpResponse<String> response;
        String formData = Globals.mapToStringforFormData(map);
        System.out.println("formData" + formData);
        response = APIClient.postFormDataRequest(formData, EndPoints.CREATE_SALES_QUOTATION_TO_ORDER);
        JsonObject responseBody = new Gson().fromJson(response.body(), JsonObject.class);
        System.out.println("ResponsequotToOrder=>" + responseBody);
        message = responseBody.get("message").getAsString();

        if (responseBody.get("responseStatus").getAsInt() == 200) {
            System.out.println("Quot to order Api hit successfully");
            AlertUtility.CustomCallback callback1 = (number1) -> {
                if (number1 == 1) {
                    GlobalController.getInstance().addTabStatic(SALES_ORDER_LIST_SLUG, false);
                }
            };
            AlertUtility.AlertSuccessTimeout("Success", responseBody.get("message").getAsString(), callback1);
//            Alert alert = new Alert(Alert.AlertType.INFORMATION);
//            alert.setTitle("Success");
//            alert.setHeaderText(message);
//            // alert.setContentText("HSN has been created successfully.");
//            alert.show();
//            PauseTransition delay = new PauseTransition(Duration.seconds(1));
//            delay.setOnFinished(event -> alert.close());
//            delay.play();
            tfSalesQuotToOrderLedgerName.setText("");
            cmbSalesQuotToOrderClientGST.getSelectionModel().clearSelection();
//            GlobalController.getInstance().addTabStatic(SALES_ORDER_LIST_SLUG,false);
        } else {
            AlertUtility.AlertErrorTimeout("Error", message, input -> {
                btnSalesQuotToOrderSubmit.requestFocus();
            });
        }
    }else {
            System.out.println("working!");
        }
    };
        AlertUtility.AlertConfirmation(AlertUtility.alertTypeConfirmation, LedgerMessageConsts.msgConfirmationOnSubmit + tfSalesQuotToOrderOrderNo.getText() + LedgerMessageConsts.msgInvoiceNumber, callback);
    }

}

class TextFieldTableCellForSaleQuotationToOrder extends TableCell<SalesQuotationProductDTO, String> {
    private TextField textField;
    private String columnName;

    TableCellCallback<Object[]> callback;

    TableCellCallback<Integer> product_callback;
    private TextField button;//new2.0


    public TextFieldTableCellForSaleQuotationToOrder(String columnName, TableCellCallback<Object[]> callback) {
        this.columnName = columnName;
        this.textField = new TextField();

        this.callback = callback;


        this.textField.setOnAction(event -> commitEdit(textField.getText()));

        // Commit when focus is lost
        this.textField.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal) {
                commitEdit(textField.getText());
            }
        });

        // Commit when Tab key is pressed
        this.textField.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.TAB) {
                commitEdit(textField.getText());
            }
        });


        textfieldStyle();

        particularsColumn();
        qtyColumn();
        rateColumn();
        freeQtyColumn();//new2.0
        discountAmount();
        discountPer();

        netAmountColumn();

    }

    public TextFieldTableCellForSaleQuotationToOrder(String columnName, TableCellCallback<Object[]> callback, TableCellCallback<Integer> product_callback, TextField button) {
        this.columnName = columnName;
        this.textField = new TextField();

        this.product_callback = product_callback;

        this.callback = callback;

        this.textField.setOnAction(event -> commitEdit(textField.getText()));

        // Commit when focus is lost
        this.textField.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal) {
                commitEdit(textField.getText());
            }
        });

        // Commit when Tab key is pressed
        this.textField.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.TAB) {
                commitEdit(textField.getText());
            }
        });


        textfieldStyle();

        particularsColumn();
        qtyColumn();
        rateColumn();
        discountAmount();
        discountPer();
//        batchColumn();

        netAmountColumn();

    }

    private void particularsColumn() {
        if ("tblcSalesQuotToOrderParticular".equals(columnName)) {
            textField.setEditable(false);
            textField.setOnKeyPressed(event -> {
                if (event.getCode() == KeyCode.SPACE) {
                    openProduct(getIndex());
                }
                if (event.isShiftDown() && event.getCode() == KeyCode.TAB) {
                    if (getIndex() == 1) {
                        TableColumn<SalesQuotationProductDTO, ?> colName = getTableView().getColumns().get(12);
                        getTableView().edit(getIndex() - 1, colName);
                    } else {
                        TableColumn<SalesQuotationProductDTO, ?> colName = getTableView().getColumns().get(15);
                        getTableView().edit(getIndex() - 1, colName);
                    }
                } else if (event.getCode() == KeyCode.ENTER || !event.isShiftDown() && event.getCode() == KeyCode.TAB) {
                    if (textField.getText().isEmpty() && getIndex() == getTableView().getItems().size() - 1 && getIndex() != 0) {
                        Platform.runLater(() -> {
                            button.requestFocus();
                        });
                    } else if (!textField.getText().isEmpty()) {
                        TableColumn<SalesQuotationProductDTO, ?> colName = getTableView().getColumns().get(6);
                        getTableView().edit(getIndex(), colName);
                    } else {
                        textField.requestFocus();
                    }
                    event.consume();
                }
            });

            textField.setOnMouseClicked(event -> {
//                if(textField.getText().isEmpty()){
//                    //Pass productId to ProductPopup for Focus in open ProductPopup on Space in Create if no product selected
//                    SingleInputDialogs.productId="";
//                }
                openProduct(getIndex());
            });
        }
    }

    private void qtyColumn() {
        if ("tblcSalesQuotToOrderQty".equals(columnName)) {
            textField.setEditable(true);
//            commitEdit(textField.getText());
            textField.setOnKeyPressed(event -> {
                CommonValidationsUtils.restrictToDecimalNumbers(textField);
                if (event.getCode() == KeyCode.SHIFT && event.getCode() == KeyCode.TAB) {
                    TableColumn<SalesQuotationProductDTO, ?> colName = getTableView().getColumns().get(6);
                    getTableView().edit(getIndex(), colName);

                } else if (event.getCode() == KeyCode.ENTER || event.getCode() == KeyCode.TAB) {
                    Integer index = getIndex();
                    if (!textField.getText().isEmpty()) {
                        TableColumn<SalesQuotationProductDTO, ?> colName = getTableView().getColumns().get(9);
                        getTableView().edit(index, colName);
                    } else {
                        textField.requestFocus();
                    }
                    salesQuotationCalculationToOrder.rowCalculationForPurcInvoice(index, getTableView(), callback);
                }
//                else if (event.getCode() == KeyCode.DOWN) {
//                    getTableView().getItems().addAll(new SalesQuotationProductDTO("", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", ""));
//                    TableColumn<SalesQuotationProductDTO, ?> colName = getTableView().getColumns().get(1);
//                    getTableView().edit(getIndex() + 1, colName);
//                }
            });
        }
    }

    //new2.0 start
    private void freeQtyColumn() {
        if ("tblcSalesQuotToOrderFreeqty".equals(columnName)) {
            textField.setEditable(true);
            textField.setOnKeyPressed(event -> {
                CommonValidationsUtils.restrictToDecimalNumbers(textField);
                if (event.isShiftDown() && event.getCode() == KeyCode.TAB) {
                    TableColumn<SalesQuotationProductDTO, ?> colName = getTableView().getColumns().get(7);
                    System.out.println("Col:" + colName.getText());
                    getTableView().edit(getIndex(), colName);

                } else if (event.getCode() == KeyCode.ENTER || event.getCode() == KeyCode.TAB) {
                    int index = getIndex();
                    TableColumn<SalesQuotationProductDTO, ?> colName = getTableView().getColumns().get(9);
                    getTableView().edit(getIndex(), colName);
                    salesQuotationCalculationToOrder.rowCalculationForPurcInvoice(index, getTableView(), callback);
                }
            });
        }
    }

    private void rateColumn() {
        if ("tblcSalesQuotToOrderRate".equals(columnName)) {
            textField.setEditable(true);
            textField.setOnKeyPressed(event -> {
                if (event.isShiftDown() && event.getCode() == KeyCode.TAB) {
                    TableColumn<SalesQuotationProductDTO, ?> colName = getTableView().getColumns().get(7);
                    getTableView().edit(getIndex() - 1, colName);
                } else if (event.getCode() == KeyCode.ENTER || event.getCode() == KeyCode.TAB) {
                    int index = getIndex();
                    if (!textField.getText().isEmpty()) {
                        TableColumn<SalesQuotationProductDTO, ?> colName = getTableView().getColumns().get(11);
                        getTableView().edit(index, colName);
                    } else {
                        textField.requestFocus();
                    }
                    salesQuotationCalculationToOrder.rowCalculationForPurcInvoice(index, getTableView(), callback);
                }
//                else if (event.getCode() == KeyCode.DOWN) {
//                    getTableView().getItems().addAll(new SalesQuotationProductDTO("", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", ""));
//                    TableColumn<SalesQuotationProductDTO, ?> colName = getTableView().getColumns().get(1);
//                    getTableView().edit(getIndex() + 1, colName);
//                }
            });

        }
    }
    //new2.0 end


//    private void rateColumn(){
//        if("tblcSalesQuotToOrderRate".equals(columnName)){
//            textField.setEditable(true);
//            textField.setOnKeyPressed(event ->{
//                if(event.getCode() == KeyCode.ENTER || event.getCode() == KeyCode.TAB){
//                    Integer index = getIndex();
//                    salesQuotationCalculationToOrder.rowCalculationForPurcInvoice(index, getTableView(),callback);
//                }
//            });
//        }
//    }

//    private void freeQtyColumn() {
//        if ("tblcSalesQuotToOrderFreeqty".equals(columnName)) {
//            textField.setEditable(true);
//            textField.setOnKeyPressed(event -> {
//                if (event.isShiftDown() && event.getCode() == KeyCode.TAB) {
//                    TableColumn<SalesQuotationProductDTO, ?> colName = getTableView().getColumns().get(7);
//                    System.out.println("Col:" + colName.getText());
//                    getTableView().edit(getIndex(), colName);
//
//                } else if (event.getCode() == KeyCode.ENTER || event.getCode() == KeyCode.TAB) {
//                    int index = getIndex();
//                    TableColumn<SalesQuotationProductDTO, ?> colName = getTableView().getColumns().get(9);
//                    getTableView().edit(getIndex(), colName);
//                    salesQuotationCalculationToOrder.rowCalculationForPurcInvoice(index, getTableView(), callback);
//                }
//            });
//           /* TableColumn<PurchaseOrderTable, ?> colName = getTableView().getColumns().get(8);
//            getTableView().edit(getIndex(), colName);*/
//
//        }
//    }

    private void discountPer() {
        if ("tblcSalesQuotToOrderDisPer".equals(columnName)) {
            textField.setEditable(true);

            textField.setOnKeyPressed(event -> {
                if (event.isShiftDown() && event.getCode() == KeyCode.TAB) {
                    TableColumn<SalesQuotationProductDTO, ?> colName = getTableView().getColumns().get(9);
                    getTableView().edit(getIndex(), colName);
                } else if (event.getCode() == KeyCode.ENTER || event.getCode() == KeyCode.TAB) {
                    int index = getIndex();
                    TableColumn<SalesQuotationProductDTO, ?> colName = getTableView().getColumns().get(12);
                    getTableView().edit(index, colName);
                    salesQuotationCalculationToOrder.rowCalculationForPurcInvoice(index, getTableView(), callback);
                }
            });
        }
    }

    private void discountAmount() {
        if ("tblcSalesQuotToOrderDisRs".equals(columnName)) {
            textField.setEditable(true);

            //new2.0 start
            textField.setOnKeyPressed(event -> {
                if (event.isShiftDown() && event.getCode() == KeyCode.TAB) {
                    TableColumn<SalesQuotationProductDTO, ?> colName = getTableView().getColumns().get(11);
                    getTableView().edit(getIndex(), colName);
                } else if (event.getCode() == KeyCode.ENTER || event.getCode() == KeyCode.TAB) {
                    int index = getIndex();
                    int current_index = getTableRow().getIndex();
                    //new start
                    if (getTableRow().getItem().getSr_no() != null) {
                        int serial_no = Integer.parseInt(getTableRow().getItem().getSr_no());
                        getTableView().getItems().addAll(new SalesQuotationProductDTO(String.valueOf(serial_no + 1), "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", ""));
                    }
                    TableColumn<SalesQuotationProductDTO, ?> colName = getTableView().getColumns().get(1);
                    getTableView().edit(getIndex() + 1, colName);
                    salesQuotationCalculationToOrder.rowCalculationForPurcInvoice(index, getTableView(), callback);
//new2.0 end

                }
            });
        }
    }

    private void netAmountColumn() {

        if ("tblcSalesQuotToOrderNetAmt".equals(columnName)) {
            textField.setEditable(false);
            Platform.runLater(() -> {
                textField.setOnKeyPressed(event -> {
                    if (event.getCode() == KeyCode.SPACE) {
                        System.out.println(" getTableRow().getItem().getNet_amt() " + getTableRow().getItem().getNet_amt());

                        int current_index = getTableRow().getIndex();
                        //new start
                        if (getTableRow().getItem().getSr_no() != null) {
                            int serial_no = Integer.parseInt(getTableRow().getItem().getSr_no());
                            getTableView().getItems().addAll(new SalesQuotationProductDTO(String.valueOf(serial_no + 1), "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", ""));
                        }
                        //new end
                    }
                });
            });
        }
    }


    //new start
    private void openProduct(int currentIndex) {
        SalesQuotationProductDTO selectedRow = getTableView().getItems().get(currentIndex);

        TranxCommonPopUps.openProductPopUp(Communicator.stage, selectedRow.getProduct_id().isEmpty() ? 0 : Integer.valueOf(selectedRow.getProduct_id()), "Product", input -> {
//            Integer selectedIndex = tblTranxSalesInvoiceCreateCMPTRow.getSelectionModel().getSelectedIndex();
            System.out.println("input" + input);
            if (input != null) {
                getTableRow().getItem().setParticulars(input.getProductName());
                getTableRow().getItem().setProduct_id(input.getProductId().toString());
                getTableRow().getItem().setPacking(input.getPackageName());
                getTableRow().getItem().setIs_batch(input.getBatch().toString());
                getTableRow().getItem().setTax_per(input.getIgst().toString());
//                getTableRow().getItem().setUnit(input.get());
                getTableRow().getItem().setSelectedProduct(input);
                getTableRow().getItem().setRate(input.getSalesRate().toString());
                List<LevelAForPurInvoice> levelAForPurInvoiceList = ProductUnitsPackingSaleQuotToOrder.getAllProductUnitsPackingFlavour(input.getProductId().toString());
                System.out.println("levelAForPurInvoiceList" + levelAForPurInvoiceList);

                //                int index = getTableRow().getIndex();
                ObservableList<LevelAForPurInvoice> observableLevelAList = FXCollections.observableArrayList(levelAForPurInvoiceList);
                System.out.println("observableLevelAList1234" + observableLevelAList);
                if (currentIndex >= 0 && currentIndex < PurInvoiceCommunicator.levelAForPurInvoiceObservableList.size()) {
                    PurInvoiceCommunicator.levelAForPurInvoiceObservableList.set(currentIndex, observableLevelAList);
                    getTableRow().getItem().setLevelA(null);
                    getTableRow().getItem().setLevelA(levelAForPurInvoiceList.get(0)!=null?levelAForPurInvoiceList.get(0).getLabel():"");

                } else {
                    PurInvoiceCommunicator.levelAForPurInvoiceObservableList.add(observableLevelAList);
                    getTableRow().getItem().setLevelA(null);
                    getTableRow().getItem().setLevelA(levelAForPurInvoiceList.get(0)!=null?levelAForPurInvoiceList.get(0).getLabel():"");
                }
                TableColumn<SalesQuotationProductDTO, ?> colName = getTableView().getColumns().get(6);
                getTableView().edit(getIndex(),colName);
                Integer product_id = Integer.valueOf(getTableRow().getItem().getProduct_id());
                if (product_callback != null) {
                    product_callback.call(product_id);
                }
            }
        }, null);
    }
    //new end

//    private void openProduct() {
//        SingleInputDialogs.openProductPopUp(Communicator.stage, "Product", input -> {
//
//            String productName = (String) input[0];
//            String productId = (String) input[1];
//            String packaging = (String) input[2];
//            String mrp = (String) input[3];
//            String unit = (String) input[4];
//            String taxper = (String) input[5];
//            String salesrate = (String) input[6];
//            String purchaseRate = (String) input[7];
//            String is_batch = (String) input[8];
//
//            getTableRow().getItem().setParticulars(productName);
//            getTableRow().getItem().setProduct_id(productId);
//            getTableRow().getItem().setPacking(packaging);
//            getTableRow().getItem().setIs_batch(is_batch);
//            getTableRow().getItem().setTax_per(taxper);
//            getTableRow().getItem().setUnit(unit);
//            getTableRow().getItem().setRate(salesrate);
//
//            // fetchSelectedProductData(productId);
//            if(productId!=""){
//                //Pass productId to ProductPopup for Focus in open ProductPopup after Product is Selected in Create
//                SingleInputDialogs.productId= productId;
//            }
//            else{
//                SingleInputDialogs.productId= "";
//            }
//
//            System.out.println("particulers "+getTableView().getItems().get(0).getParticulars());
//
//            List<LevelAForPurInvoice> levelAForPurInvoiceList = ProductUnitsPackingSaleQuotToOrder.getAllProductUnitsPackingFlavour(productId);
//            int index = getTableRow().getIndex();
//            ObservableList<LevelAForPurInvoice> observableLevelAList = FXCollections.observableArrayList(levelAForPurInvoiceList);
//
//            if (index >= 0 && index < PurInvoiceCommunicator.levelAForPurInvoiceObservableList.size()) {
//                PurInvoiceCommunicator.levelAForPurInvoiceObservableList.set(index, observableLevelAList);
//                getTableRow().getItem().setLevel_a_id(null);
//                getTableRow().getItem().setLevel_a_id(levelAForPurInvoiceList.get(0).getLabel());
//
//            }
//            else {
//                PurInvoiceCommunicator.levelAForPurInvoiceObservableList.add(observableLevelAList);
//                getTableRow().getItem().setLevel_a_id(null);
//                getTableRow().getItem().setLevel_a_id(levelAForPurInvoiceList.get(0).getLabel());
//            }
//
//            Integer product_id = Integer.parseInt(productId);
//            if(product_callback!=null){
//                product_callback.call(product_id);
//            }
//
//        });
//    }

    public void textfieldStyle() {
        if (columnName.equals("tblcSalesQuotToOrderParticular")) {
            textField.setPromptText("Particulars");
            textField.setEditable(false);
        } else {
            textField.setPromptText("0");
        }

        /*textField.setPrefHeight(38);
        textField.setMaxHeight(38);
        textField.setMinHeight(38);*/

        if (columnName.equals("tblcSalesQuotToOrderParticular")) {
            textField.setStyle("-fx-background-color: #f6f6f9; -fx-border-radius: 0px; -fx-border-width: 1; -fx-border-color: #f6f6f9;");
            textField.focusedProperty().addListener((obs, wasFocused, isNowFocused) -> {
                if (isNowFocused) {
                    textField.setStyle("-fx-background-color: #fff9c4; -fx-border-radius: 0px; -fx-border-width: 2; -fx-border-color: #00a0f5;");
                } else {
                    textField.setStyle("-fx-background-color: #f6f6f9; -fx-border-radius: 0px; -fx-border-width: 2; -fx-border-color: #f6f6f9;");
                }
            });

            textField.setOnMouseEntered(e -> {
                textField.setStyle("-fx-background-color: #f6f6f9; -fx-border-radius: 0px; -fx-border-width: 1; -fx-border-color:   #0099ff;");
            });

            textField.setOnMouseExited(e -> {
                if (!textField.isFocused()) {
                    textField.setStyle("-fx-background-color: #f6f6f9; -fx-border-radius: 0px; -fx-border-width: 1; -fx-border-color: #f6f6f9;");
                }
            });
        } else {
            textField.setStyle("-fx-background-color: #f6f6f9; -fx-alignment: CENTER-RIGHT; -fx-border-radius: 0px; -fx-border-width: 1; -fx-border-color: #f6f6f9;");
            textField.focusedProperty().addListener((obs, wasFocused, isNowFocused) -> {
                if (isNowFocused) {
                    textField.setStyle("-fx-background-color: #fff9c4; -fx-alignment: CENTER-RIGHT; -fx-border-radius: 0px; -fx-border-width: 1.5; -fx-border-color: #00a0f5;");
                } else {
                    textField.setStyle("-fx-background-color: #f6f6f9; -fx-alignment: CENTER-RIGHT; -fx-border-radius: 0px; -fx-border-width: 1.5; -fx-border-color: #f6f6f9;");
                }
            });
            textField.setOnMouseEntered(e -> {
                textField.setStyle("-fx-background-color: #f6f6f9; -fx-alignment: CENTER-RIGHT; -fx-border-radius: 0px; -fx-border-width: 1; -fx-border-color:   #0099ff;");
            });

            textField.setOnMouseExited(e -> {
                if (!textField.isFocused()) {
                    textField.setStyle("-fx-background-color: #f6f6f9; -fx-alignment: CENTER-RIGHT; -fx-border-radius: 0px; -fx-border-width: 1; -fx-border-color: #f6f6f9;");
                }
            });
        }


        if (columnName.equals("tblcSalesQuotToOrderNetAmt")) {
            this.textField.setOnKeyPressed(event -> {
                if (event.getCode() == KeyCode.ENTER) {
                    commitEdit(textField.getText());
                    getTableView().getItems().addAll(new SalesQuotationProductDTO("", "", "", "1", "", "", "", "", "", "", "", "", "", "", "", "", "", "", ""));
                }
            });
        }
    }

    @Override

    public void startEdit() {
        super.startEdit();
        setText(null);
        setGraphic(new VBox(textField));
        textField.requestFocus();

    }

    @Override
    public void cancelEdit() {
        super.cancelEdit();
        setText((String) getItem());
        setGraphic(null);
    }


    @Override
    protected void updateItem(String item, boolean empty) {
        super.updateItem(item, empty);
        if (empty) {
            setGraphic(null);
        } else {
            VBox vbox = new VBox(textField);
            vbox.setAlignment(Pos.CENTER);
            textField.setText(item.toString());
            setGraphic(vbox);
        }
    }

    @Override
    public void commitEdit(String newValue) {
        super.commitEdit(newValue);
        Object item = getTableRow().getItem();
        if (item != null && columnName.equals("tblcSalesQuotToOrderParticular")) {
            ((SalesQuotationProductDTO) item).setParticulars(newValue.isEmpty() ? "" : newValue);
        }
//        else if (columnName.equals("tcBatch")) {
//            ( getTableRow().getItem()).setBatch_or_serial(newValue.isEmpty() ? "" : newValue);
//        }
        else if (columnName.equals("tblcSalesQuotToOrderQty")) {
            (getTableRow().getItem()).setQuantity(newValue.isEmpty() ? "0" : newValue);
        } else if (columnName.equals("tcSalesOrderCmpTRowFreeqty")) {//new2.0
            (getTableRow().getItem()).setFree_qty(newValue.isEmpty() ? "0.0" : newValue);//new2.0
        } else if (columnName.equals("tblcSalesQuotToOrderFreeQty")) {
            (getTableRow().getItem()).setRate(newValue.isEmpty() ? "0.0" : newValue);
        } else if (columnName.equals("tblcSalesQuotToOrderGrossAmt")) {
            (getTableRow().getItem()).setGross_amt(newValue.isEmpty() ? "0.0" : newValue);
        } else if (columnName.equals("tblcSalesQuotToOrderDisPer")) {
            (getTableRow().getItem()).setDis_per(newValue.isEmpty() ? "0.0" : newValue);
        } else if (columnName.equals("tblcSalesQuotToOrderDisRs")) {
            (getTableRow().getItem()).setDis_amt(newValue.isEmpty() ? "0.0" : newValue);
        } else if (columnName.equals("tblcSalesQuotToOrderTax")) {
            (getTableRow().getItem()).setTax_per(newValue.isEmpty() ? "0.0" : newValue);
        } else if (columnName.equals("tblcSalesQuotToOrderNetAmt")) {
            (getTableRow().getItem()).setNet_amt(newValue.isEmpty() ? "0.0" : newValue);
        }

    }
}

class ComboBoxTableCellForLevelASalesQuotToOrder extends TableCell<SalesQuotationProductDTO, String> {

    String columnName;
    private final ComboBox<String> comboBox;
    ObservableList<LevelAForPurInvoice> comboList = null;

    public ComboBoxTableCellForLevelASalesQuotToOrder(String columnName) {

        this.columnName = columnName;
        this.comboBox = new ComboBox<>();

        this.comboBox.setPromptText("Select");

        /*comboBox.setPrefHeight(38);
        comboBox.setMaxHeight(38);
        comboBox.setMinHeight(38);*/

        this.comboBox.setStyle("-fx-background-color: #f6f6f9; -fx-border-radius: 0px; -fx-border-width: 1; -fx-border-color: #f6f6f9;");

        this.comboBox.focusedProperty().addListener((obs, wasFocused, isNowFocused) -> {
            if (isNowFocused) {
                this.comboBox.setStyle("-fx-background-color: #fff9c4; -fx-border-radius: 0px; -fx-border-width: 1.5; -fx-border-color: #00a0f5;");
            } else {
                this.comboBox.setStyle("-fx-background-color: #f6f6f9; -fx-border-radius: 0px; -fx-border-width: 1.5; -fx-border-color: #f6f6f9;");
            }
        });

        this.comboBox.hoverProperty().addListener((obs, wasHovered, isNowHovered) -> {
            if (isNowHovered && !comboBox.isFocused()) {
                this.comboBox.setStyle("-fx-background-color: #f6f6f9; -fx-border-radius: 0px; -fx-border-width: 1; -fx-border-color:   #0099ff;");

            } else if (!comboBox.isFocused()) {
                this.comboBox.setStyle("-fx-background-color: #f6f6f9; -fx-border-radius: 0px; -fx-border-width: 1; -fx-border-color: #f6f6f9;");
            }
        });

        if (comboList != null) {
            for (LevelAForPurInvoice commonDTO : comboList) {
                this.comboBox.getItems().add(commonDTO.getLabel());
            }
        }
        this.comboBox.setFocusTraversable(false);
//        AutoCompleteBox autoCompleteBox1 = new AutoCompleteBox<>(this.comboBox, -1);

        this.comboBox.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal) {
                commitEdit(String.valueOf(comboBox.getValue()));
            }
        });

        comBoAction();

    }

    public void comBoAction() {
        comboBox.setOnAction(e -> {
            String selectedItem = comboBox.getSelectionModel().getSelectedItem();

            if (selectedItem != null) {
                if (PurInvoiceCommunicator.levelAForPurInvoiceObservableList != null && !PurInvoiceCommunicator.levelAForPurInvoiceObservableList.isEmpty()) {
                    for (LevelAForPurInvoice levelAForPurInvoice : PurInvoiceCommunicator.levelAForPurInvoiceObservableList.get(getTableRow().getIndex())) {
                        if (selectedItem.equals(levelAForPurInvoice.getLabel())) {

                            getTableRow().getItem().setLevel_a_id(levelAForPurInvoice.getValue());

                            ObservableList<LevelBForPurInvoice> observableLevelAList = FXCollections.observableArrayList(levelAForPurInvoice.getLevelBOpts());

                            int index = getTableRow().getIndex();

                            if (index >= 0 && index < PurInvoiceCommunicator.levelBForPurInvoiceObservableList.size()) {
                                PurInvoiceCommunicator.levelBForPurInvoiceObservableList.set(index, observableLevelAList);
                                getTableRow().getItem().setLevelB(null);
                                getTableRow().getItem().setLevelB(levelAForPurInvoice.getLevelBOpts().get(0).getLabel());

                                for (LevelBForPurInvoice levelBForPurInvoice : PurInvoiceCommunicator.levelBForPurInvoiceObservableList.get(index)) {
                                    if (levelBForPurInvoice.getValue().equals(getTableRow().getItem().getLevel_b_id())) {
                                        getTableRow().getItem().setLevelB(null);
                                        getTableRow().getItem().setLevelB(levelBForPurInvoice.getLabel());
                                    }
                                }

                            }
                            else {
                                PurInvoiceCommunicator.levelBForPurInvoiceObservableList.add(observableLevelAList);
                                getTableRow().getItem().setLevelB(null);
                                getTableRow().getItem().setLevelB(levelAForPurInvoice.getLevelBOpts().get(0).getLabel());

                                for (LevelBForPurInvoice levelBForPurInvoice : PurInvoiceCommunicator.levelBForPurInvoiceObservableList.get(index)) {
                                    if (levelBForPurInvoice.getValue().equals(getTableRow().getItem().getLevel_b_id())) {
                                        getTableRow().getItem().setLevelB(null);
                                        getTableRow().getItem().setLevelB(levelBForPurInvoice.getLabel());
                                    }
                                }
                            }
                        }
                    }
                }
            }

        });
    }

    @Override
    public void startEdit() {
        super.startEdit();
        setText(null);
        setGraphic(new HBox(comboBox));
        comboBox.requestFocus();
    }

    @Override
    public void cancelEdit() {
        super.cancelEdit();
        setText((String) getItem());
        setGraphic(null);
    }

    @Override
    protected void updateItem(String item, boolean empty) {
        super.updateItem(item, empty);
        if (empty) {
            setGraphic(null);
        } else {

            comboBox.getItems().clear();

            if (item != null) {

                Platform.runLater(() -> {
                    int i = getTableRow().getIndex();
                    if (i >= 0 && i < PurInvoiceCommunicator.levelAForPurInvoiceObservableList.size()) {
                        for (LevelAForPurInvoice levelAForPurInvoice : PurInvoiceCommunicator.levelAForPurInvoiceObservableList.get(i)) {
                            comboBox.getItems().add(levelAForPurInvoice.getLabel());
                        }
                        String itemToSet = item;
                        if (comboBox.getItems().contains(itemToSet)) {
                            comboBox.setValue(itemToSet);
                        }
                    }
                });
            }


            HBox hbox = new HBox();
            hbox.getChildren().addAll(comboBox);
            hbox.setSpacing(5);

            setGraphic(hbox);
        }
    }

    @Override
    public void commitEdit(String newValue) {
        super.commitEdit(newValue);
        TableRow<SalesQuotationProductDTO> row = getTableRow();
        if (row != null) {
            SalesQuotationProductDTO item = row.getItem();
            if (item != null) {
                item.setLevelA(newValue);
            }
        }
    }

}

class ComboBoxTableCellForLevelBSalesQuotToOrder extends TableCell<SalesQuotationProductDTO, String> {

    String columnName;
    private final ComboBox<String> comboBox;
    ObservableList<LevelBForPurInvoice> comboList = null;

    public ComboBoxTableCellForLevelBSalesQuotToOrder(String columnName) {
        this.columnName = columnName;
        this.comboBox = new ComboBox<>();

        this.comboBox.setPromptText("Select");

        /*comboBox.setPrefHeight(38);
        comboBox.setMaxHeight(38);
        comboBox.setMinHeight(38);*/

        this.comboBox.setStyle("-fx-background-color: #f6f6f9; -fx-border-radius: 0px; -fx-border-width: 1; -fx-border-color: #f6f6f9;");

        this.comboBox.focusedProperty().addListener((obs, wasFocused, isNowFocused) -> {
            if (isNowFocused) {
                this.comboBox.setStyle("-fx-background-color: #fff9c4; -fx-border-radius: 0px; -fx-border-width: 1.5; -fx-border-color: #00a0f5;");
            } else {
                this.comboBox.setStyle("-fx-background-color: #f6f6f9; -fx-border-radius: 0px; -fx-border-width: 1.5; -fx-border-color: #f6f6f9;");
            }
        });

        this.comboBox.hoverProperty().addListener((obs, wasHovered, isNowHovered) -> {
            if (isNowHovered && !comboBox.isFocused()) {
                this.comboBox.setStyle("-fx-background-color: #f6f6f9; -fx-border-radius: 0px; -fx-border-width: 1; -fx-border-color:   #0099ff;");

            } else if (!comboBox.isFocused()) {
                this.comboBox.setStyle("-fx-background-color: #f6f6f9; -fx-border-radius: 0px; -fx-border-width: 1; -fx-border-color: #f6f6f9;");
            }
        });

        if (comboList != null) {
            for (LevelBForPurInvoice commonDTO : comboList) {
                this.comboBox.getItems().add(commonDTO.getLabel());
            }
        }
        this.comboBox.setFocusTraversable(true);
        AutoCompleteBox autoCompleteBox1 = new AutoCompleteBox<>(this.comboBox, -1);

        this.comboBox.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal) {
                commitEdit(String.valueOf(comboBox.getValue()));
            }
        });

        comBoAction();
    }

    public void comBoAction() {
        comboBox.setOnAction(e -> {
            String selectedItem = comboBox.getSelectionModel().getSelectedItem();

            if (selectedItem != null) {
                if (PurInvoiceCommunicator.levelBForPurInvoiceObservableList != null && !PurInvoiceCommunicator.levelBForPurInvoiceObservableList.isEmpty()) {
                    for (LevelBForPurInvoice levelBForPurInvoice : PurInvoiceCommunicator.levelBForPurInvoiceObservableList.get(getTableRow().getIndex())) {
                        if (selectedItem.equals(levelBForPurInvoice.getLabel())) {

                            getTableRow().getItem().setLevel_b_id(levelBForPurInvoice.getValue());

                            ObservableList<LevelCForPurInvoice> observableLevelAList = FXCollections.observableArrayList(levelBForPurInvoice.getLevelCOpts());

                            int index = getTableRow().getIndex();
                            if (index >= 0 && index < PurInvoiceCommunicator.levelCForPurInvoiceObservableList.size()) {
                                PurInvoiceCommunicator.levelCForPurInvoiceObservableList.set(index, observableLevelAList);
                                getTableRow().getItem().setLevelC(null);

                                List<LevelCForPurInvoice> levelCOpts = levelBForPurInvoice.getLevelCOpts();
                                if (!levelCOpts.isEmpty()) {
                                    getTableRow().getItem().setLevelC(levelCOpts.get(0).getLabel());
                                }


                                for (LevelCForPurInvoice levelCForPurInvoice : PurInvoiceCommunicator.levelCForPurInvoiceObservableList.get(index)) {
                                    if (levelCForPurInvoice.getValue().equals(getTableRow().getItem().getLevel_c_id())) {
                                        getTableRow().getItem().setLevelC(null);
                                        getTableRow().getItem().setLevelC(levelCForPurInvoice.getLabel());
                                    }
                                }

                            }
                            else {
                                PurInvoiceCommunicator.levelCForPurInvoiceObservableList.add(observableLevelAList);
                                getTableRow().getItem().setLevelC(null);
                                getTableRow().getItem().setLevelC(levelBForPurInvoice.getLevelCOpts().get(0).getLabel());

                                for (LevelCForPurInvoice levelCForPurInvoice : PurInvoiceCommunicator.levelCForPurInvoiceObservableList.get(index)) {
                                    if (levelCForPurInvoice.getValue().equals(getTableRow().getItem().getLevel_c_id())) {
                                        getTableRow().getItem().setLevelC(null);
                                        getTableRow().getItem().setLevelC(levelCForPurInvoice.getLabel());
                                    }
                                }
                            }

                        }
                    }
                }

            }

        });
    }

    @Override
    public void startEdit() {
        super.startEdit();
        setText(null);
        setGraphic(new HBox(comboBox));
        comboBox.requestFocus();
    }

    @Override
    public void cancelEdit() {
        super.cancelEdit();
        setText((String) getItem());
        setGraphic(null);
    }

    @Override
    protected void updateItem(String item, boolean empty) {
        super.updateItem(item, empty);
        if (empty) {
            setGraphic(null);
        } else {


            comboBox.getItems().clear();

            if (item != null) {

                Platform.runLater(() -> {
                    int i = getTableRow().getIndex();
                    if (i >= 0 && i < PurInvoiceCommunicator.levelBForPurInvoiceObservableList.size()) {
                        for (LevelBForPurInvoice levelBForPurInvoice : PurInvoiceCommunicator.levelBForPurInvoiceObservableList.get(i)) {
                            comboBox.getItems().add(levelBForPurInvoice.getLabel());
                        }
                        String itemToSet = item;
                        if (comboBox.getItems().contains(itemToSet)) {
                            comboBox.setValue(itemToSet);
                        }
                    }
                });
            }


            HBox hbox = new HBox();
            hbox.getChildren().addAll(comboBox);
            hbox.setSpacing(5);

            setGraphic(hbox);
        }
    }

    @Override
    public void commitEdit(String newValue) {
        super.commitEdit(newValue);
        (getTableRow().getItem()).setLevelB(newValue);
    }

}

class ComboBoxTableCellForLevelCSalesQuotToOrder extends TableCell<SalesQuotationProductDTO, String> {

    String columnName;
    private final ComboBox<String> comboBox;
    ObservableList<LevelAForPurInvoice> comboList = null;

    public ComboBoxTableCellForLevelCSalesQuotToOrder(String columnName) {


        this.columnName = columnName;
        this.comboBox = new ComboBox<>();

        this.comboBox.setPromptText("Select");

        /*comboBox.setPrefHeight(38);
        comboBox.setMaxHeight(38);
        comboBox.setMinHeight(38);*/

        this.comboBox.setStyle("-fx-background-color: #f6f6f9; -fx-border-radius: 0px; -fx-border-width: 1; -fx-border-color: #f6f6f9;");

        this.comboBox.focusedProperty().addListener((obs, wasFocused, isNowFocused) -> {
            if (isNowFocused) {
                this.comboBox.setStyle("-fx-background-color: #fff9c4; -fx-border-radius: 0px; -fx-border-width: 1.5; -fx-border-color: #00a0f5;");
            } else {
                this.comboBox.setStyle("-fx-background-color: #f6f6f9; -fx-border-radius: 0px; -fx-border-width: 1.5; -fx-border-color: #f6f6f9;");
            }
        });

        this.comboBox.hoverProperty().addListener((obs, wasHovered, isNowHovered) -> {
            if (isNowHovered && !comboBox.isFocused()) {
                this.comboBox.setStyle("-fx-background-color: #f6f6f9; -fx-border-radius: 0px; -fx-border-width: 1; -fx-border-color:   #0099ff;");

            } else if (!comboBox.isFocused()) {
                this.comboBox.setStyle("-fx-background-color: #f6f6f9; -fx-border-radius: 0px; -fx-border-width: 1; -fx-border-color: #f6f6f9;");
            }
        });

        if (comboList != null) {
            for (LevelAForPurInvoice commonDTO : comboList) {
                this.comboBox.getItems().add(commonDTO.getLabel());
            }
        }
        this.comboBox.setFocusTraversable(true);
        AutoCompleteBox autoCompleteBox1 = new AutoCompleteBox<>(this.comboBox, -1);

        this.comboBox.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal) {
                commitEdit(String.valueOf(comboBox.getValue()));
            }
        });

        comBoAction();

    }

    public void comBoAction() {
        comboBox.setOnAction(e -> {
            String selectedItem = comboBox.getSelectionModel().getSelectedItem();

            if (selectedItem != null) {
                if (PurInvoiceCommunicator.levelCForPurInvoiceObservableList != null && !PurInvoiceCommunicator.levelCForPurInvoiceObservableList.isEmpty()) {

                    for (LevelCForPurInvoice levelCForPurInvoice : PurInvoiceCommunicator.levelCForPurInvoiceObservableList.get(getTableRow().getIndex())) {
                        if (selectedItem.equals(levelCForPurInvoice.getLabel())) {

                            getTableRow().getItem().setLevel_c_id(levelCForPurInvoice.getValue());
                            ObservableList<UnitForPurInvoice> unitForPurInvoiceObservableList = FXCollections.observableArrayList(levelCForPurInvoice.getUnitOpts());

                            int index = getTableRow().getIndex();

                            if (index >= 0 && index < PurInvoiceCommunicator.unitForPurInvoiceList.size()) {
                                PurInvoiceCommunicator.unitForPurInvoiceList.set(index, unitForPurInvoiceObservableList);
                                getTableRow().getItem().setUnit(null);

                                List<UnitForPurInvoice> unitForPurInvoiceList = levelCForPurInvoice.getUnitOpts();
                                if (!unitForPurInvoiceList.isEmpty()) {
                                    getTableRow().getItem().setUnit(unitForPurInvoiceList.get(0).getLabel());
                                }

                                for (UnitForPurInvoice unitForPurInvoice : PurInvoiceCommunicator.unitForPurInvoiceList.get(index)) {
                                    if (unitForPurInvoice.getValue().equals(getTableRow().getItem().getUnit_id())) {
                                        getTableRow().getItem().setUnit(null);
                                        getTableRow().getItem().setUnit(unitForPurInvoice.getLabel());
                                    }
                                }

                            }
                            else {
                                PurInvoiceCommunicator.unitForPurInvoiceList.add(unitForPurInvoiceObservableList);
                                getTableRow().getItem().setUnit(null);
                                getTableRow().getItem().setUnit(levelCForPurInvoice.getUnitOpts().get(0).getLabel());

                                for (UnitForPurInvoice unitForPurInvoice : PurInvoiceCommunicator.unitForPurInvoiceList.get(index)) {
                                    if (unitForPurInvoice.getValue().equals(getTableRow().getItem().getUnit_id())) {
                                        getTableRow().getItem().setUnit(null);
                                        getTableRow().getItem().setUnit(unitForPurInvoice.getLabel());
                                    }
                                }
                            }


                        }
                    }
                }
            }

        });
    }

    @Override
    public void startEdit() {
        super.startEdit();
        setText(null);
        setGraphic(new HBox(comboBox));
        comboBox.requestFocus();
    }

    @Override
    public void cancelEdit() {
        super.cancelEdit();
        setText((String) getItem());
        setGraphic(null);
    }

    @Override
    protected void updateItem(String item, boolean empty) {
        super.updateItem(item, empty);
        if (empty) {
            setGraphic(null);
        } else {

            comboBox.getItems().clear();

            if (item != null) {

                Platform.runLater(() -> {
                    int i = getTableRow().getIndex();
                    if (i >= 0 && i < PurInvoiceCommunicator.levelCForPurInvoiceObservableList.size()) {
                        for (LevelCForPurInvoice levelCForPurInvoice : PurInvoiceCommunicator.levelCForPurInvoiceObservableList.get(i)) {
                            comboBox.getItems().add(levelCForPurInvoice.getLabel());
                        }
                        String itemToSet = item;
                        if (comboBox.getItems().contains(itemToSet)) {
                            comboBox.setValue(itemToSet);
                        }
                    }
                });
            }


            HBox hbox = new HBox();
            hbox.getChildren().addAll(comboBox);
            hbox.setSpacing(5);

            setGraphic(hbox);
        }
    }

    @Override
    public void commitEdit(String newValue) {
        super.commitEdit(newValue);
        (getTableRow().getItem()).setLevelC(newValue);
    }

}

class ComboBoxTableCellForUnitSalesQuotToOrder extends TableCell<SalesQuotationProductDTO, String> {

    String columnName;
    TableCellCallback<Integer> unit_callback;//new
    private final ComboBox<String> comboBox;

    public ComboBoxTableCellForUnitSalesQuotToOrder(String columnName, TableCellCallback<Integer> unit_callback) {


        this.columnName = columnName;
        this.comboBox = new ComboBox<>();
        this.unit_callback = unit_callback;//new

        this.comboBox.setPromptText("Select");

        /*comboBox.setPrefHeight(38);
        comboBox.setMaxHeight(38);
        comboBox.setMinHeight(38);*/

        this.comboBox.setStyle("-fx-background-color: #f6f6f9; -fx-border-radius: 0px; -fx-border-width: 1; -fx-border-color: #f6f6f9;");

        this.comboBox.focusedProperty().addListener((obs, wasFocused, isNowFocused) -> {
            if (isNowFocused) {
                this.comboBox.setStyle("-fx-background-color: #fff9c4; -fx-border-radius: 0px; -fx-border-width: 1.5; -fx-border-color: #00a0f5;");
            } else {
                this.comboBox.setStyle("-fx-background-color: #f6f6f9; -fx-border-radius: 0px; -fx-border-width: 1.5; -fx-border-color: #f6f6f9;");
            }
        });

        this.comboBox.hoverProperty().addListener((obs, wasHovered, isNowHovered) -> {
            if (isNowHovered && !comboBox.isFocused()) {
                this.comboBox.setStyle("-fx-background-color: #f6f6f9; -fx-border-radius: 0px; -fx-border-width: 1; -fx-border-color:   #0099ff;");

            } else if (!comboBox.isFocused()) {
                this.comboBox.setStyle("-fx-background-color: #f6f6f9; -fx-border-radius: 0px; -fx-border-width: 1; -fx-border-color: #f6f6f9;");
            }
        });

//        if (comboList != null) {
//            for (LevelAForPurInvoice commonDTO : comboList) {
//                this.comboBox.getItems().add(commonDTO.getLabel());
//            }
//        }
        this.comboBox.setFocusTraversable(true);
//        AutoCompleteBox autoCompleteBox1 = new AutoCompleteBox<>(this.comboBox, -1);

        this.comboBox.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal) {
                commitEdit(String.valueOf(comboBox.getValue()));
            }
        });

        comBoAction();

    }

    public void comBoAction() {
        comboBox.setOnAction(e -> {
            String selectedItem = comboBox.getSelectionModel().getSelectedItem();

            if (selectedItem != null) {
                if (PurInvoiceCommunicator.unitForPurInvoiceList != null && !PurInvoiceCommunicator.unitForPurInvoiceList.isEmpty()) {

                    for (UnitForPurInvoice unitForPurInvoice : PurInvoiceCommunicator.unitForPurInvoiceList.get(getTableRow().getIndex())) {
                        if (selectedItem.equals(unitForPurInvoice.getLabel())) {
                            int index = getTableRow().getIndex();
                            getTableRow().getItem().setUnit_id(unitForPurInvoice.getValue());
                            System.out.println("unitForPurInvoice.getValue() " + unitForPurInvoice.getValue() + " unitForPurInvoice.getUnitConversion() " + unitForPurInvoice.getUnitConversion());
                            getTableRow().getItem().setUnit_conv(unitForPurInvoice.getUnitConversion());
                            System.out.println("getUnittt " + getTableRow().getItem().getUnit());
                            //new
                            if (this.unit_callback != null) {
                                this.unit_callback.call(getIndex());
                            }
                        }
                    }
                }
            }

        });
        //new2.0 start
        comboBox.setOnKeyPressed(event -> {
            if (event.isShiftDown() && event.getCode() == KeyCode.TAB) {
                TableColumn<SalesQuotationProductDTO, ?> colName = getTableView().getColumns().get(1);
                getTableView().edit(getIndex(), colName);
            }

            if (event.getCode() == KeyCode.ENTER || !event.isShiftDown() && event.getCode() == KeyCode.TAB) {
                TableColumn<SalesQuotationProductDTO, ?> colName = getTableView().getColumns().get(7);
                System.out.println("Col name:" + colName.getText());
                getTableView().edit(getIndex(), colName);
            }
//            if (event.getCode() == KeyCode.DOWN) {
//                comboBox.show();
//                event.consume();
//            }
        });
        //new2.0 end
    }


    @Override
    public void startEdit() {
        super.startEdit();
        setText(null);
        setGraphic(new HBox(comboBox));
        comboBox.requestFocus();
    }

    @Override
    public void cancelEdit() {
        super.cancelEdit();
        setText((String) getItem());
        setGraphic(null);
    }

    @Override
    protected void updateItem(String item, boolean empty) {
        super.updateItem(item, empty);
        if (empty) {
            setGraphic(null);
        } else {
            comboBox.getItems().clear();

            if (item != null) {

                Platform.runLater(() -> {
                    int i = getTableRow().getIndex();
                    if (i >= 0 && i < PurInvoiceCommunicator.unitForPurInvoiceList.size()) {
                        for (UnitForPurInvoice unitForPurInvoice : PurInvoiceCommunicator.unitForPurInvoiceList.get(i)) {
                            comboBox.getItems().add(unitForPurInvoice.getLabel());
                        }
                        String itemToSet = item;
                        if (comboBox.getItems().contains(itemToSet)) {
                            comboBox.setValue(itemToSet);
                        }
                    }
                });
            } //new
            else {
                int i = getTableRow().getIndex();
                if (i >= 0) {
                    comboBox.getItems().clear();
                }
            }
            HBox hbox = new HBox();
            hbox.getChildren().addAll(comboBox);
            hbox.setSpacing(5);

            setGraphic(hbox);
        }
    }


    @Override
    public void commitEdit(String newValue) {
        super.commitEdit(newValue);
        (getTableRow().getItem()).setUnit(newValue);
    }

}

class ButtonTableCellQuotToOrder extends TableCell<SalesQuotationProductDTO, String> {
    private Button delete;

    public ButtonTableCellQuotToOrder() {


        this.delete = createButtonWithImage();


        delete.setOnAction(actionEvent -> {
            SalesQuotationProductDTO table = getTableView().getItems().get(getIndex());
            getTableView().getItems().remove(table);
            getTableView().refresh();
        });

    }


    private Button createButtonWithImage() {

        ImageView imageView = new ImageView(new Image(GenivisApplication.class.getResourceAsStream("/com/opethic/genivis/ui/assets/del.png")));
        imageView.setFitWidth(28);
        imageView.setFitHeight(28);
        Button button = new Button();
        button.setMaxHeight(38);
        button.setPrefHeight(38);
        button.setMinHeight(38);
        button.setPrefWidth(35);
        button.setMaxWidth(35);
        button.setMinWidth(35);
        button.setGraphic(imageView);
        button.setStyle("-fx-background-color: #f6f6f9;");
        button.focusedProperty().addListener((obs, wasFocused, isNowFocused) -> {
            if (isNowFocused) {
                button.setStyle("-fx-background-color: #fff9c4; -fx-border-radius: 0px; -fx-border-width: 1.5; -fx-border-color: #f86464");
            } else {
                button.setStyle("-fx-background-color: #f6f6f9;");
            }
        });

        button.hoverProperty().addListener((obs, wasHovered, isNowHovered) -> {
            if (isNowHovered && !button.isFocused()) {
                button.setStyle("-fx-background-color: #f6f6f9; -fx-border-width: 1; -fx-border-color: #f86464");
            } else if (!button.isFocused()) {
                button.setStyle("-fx-background-color: #f6f6f9;");
            }
        });

        return button;
    }

    //
    @Override
    public void startEdit() {
        super.startEdit();
        setText(null);
    }

    @Override
    public void cancelEdit() {
        super.cancelEdit();
        setGraphic(null);
    }


    @Override
    protected void updateItem(String item, boolean empty) {
        super.updateItem(item, empty);
        if (empty) {
            setGraphic(null);
        } else {
            int rowIndex = getIndex();
            if (rowIndex == 0) {
                setGraphic(null);
            } else {
                HBox vbox = new HBox(delete);
                vbox.setAlignment(Pos.CENTER);
                setGraphic(vbox);
            }
        }
    }

    @Override
    public void commitEdit(String newValue) {
        super.commitEdit(newValue);
    }

}

class ProductUnitsPackingSaleQuotToOrder {
    public static List<LevelAForPurInvoice> getAllProductUnitsPackingFlavour(String product_id) {


        Map<String, String> map = new HashMap<>();
        System.out.println("product_id" + product_id);
        map.put("product_id", product_id);

        List<LevelAForPurInvoice> levelAForPurInvoicesList = new ArrayList<>();
        try {
            String formdata = Globals.mapToStringforFormData(map);

            HttpResponse<String> response = APIClient.postFormDataRequest(formdata, "get_all_product_units_packings_flavour");
            String responseBody = response.body();
            JsonObject jsonObject = new Gson().fromJson(responseBody, JsonObject.class);

            int responseStatus = jsonObject.get("responseStatus").getAsInt();

            if (responseStatus == 200) {
                JsonObject resObj = jsonObject.get("responseObject").getAsJsonObject();
                JsonArray lstPackages = resObj.get("lst_packages").getAsJsonArray();

                for (JsonElement lstPackage : lstPackages) {
                    LevelAForPurInvoice levelAForPurInvoice = new Gson().fromJson(lstPackage, LevelAForPurInvoice.class);
                    levelAForPurInvoicesList.add(levelAForPurInvoice);
                }


            } else {
                System.out.println("responseObject is null");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }


        return levelAForPurInvoicesList;
    }

}

class salesQuotationCalculationToOrder {

    public static void rowCalculationForPurcInvoice(int rowIndex, TableView<SalesQuotationProductDTO> tableView, TableCellCallback<Object[]> callback) {
        SalesQuotationProductDTO purchaseInvoiceTable = tableView.getItems().get(rowIndex);
        if (!purchaseInvoiceTable.getQuantity().isEmpty() && Double.parseDouble(purchaseInvoiceTable.getQuantity()) > 0 && !purchaseInvoiceTable.getRate().isEmpty() && Double.parseDouble(purchaseInvoiceTable.getRate()) > 0) {

            String purchaseInvoiceTableData = purchaseInvoiceTable.getRate();
            System.out.println("In Row Calculation CMPTDATA=>" + purchaseInvoiceTableData);
            // Local parameter  to calculate gross amt and net amt
            String r_qty = "";  //declare to store row qty data
            Double r_rate = 0.0; //declare to store row rate data
            Double base_amt = 0.0; //declare to store base Amount data
            Double row_fin_grossAmt = 0.0; //declare to store row final gross amount
            Double r_dis_amt = 0.0; //declare to store row discount amount
            Double r_dis_per = 0.0; //declare to store row discount percent
            Double r_tax_per = 0.0; //declare to store row tax percent
            Double total_dis_amt = 0.0; //declare to store row total discount amount
            Double total_dis_per = 0.0; //declare to store row total discount percent
            Double row_dis_amt = 0.0; //declare to store total discount amt data
            Double total_grossAmt = 0.0;
            Double totalTax = 0.0; //to store total tax amt
            Double total_amt = 0.0; //to store total amt with discount minus
            Double net_amt = 0.0; // to store net  amount
            Double gross_amt = 0.0; //to store gross amount
            Double total_base_amt = 0.0; //store base amt
            Double dis_amt_cal = 0.0;
            Double total_taxable_amt = 0.0; //store total taxable amount
            Double taxable_amt = 0.0;//to store taxable amount
            Double igst = 0.0;
            Double cgst = 0.0;
            Double sgst = 0.0;
            Double totalIgst = 0.0;
            Double totalCgst = 0.0;
            Double totalSgst = 0.0;
            Double prevTaxPer = 0.0;
            Double finDisAmtCal = 0.0;
            Double disPer2 = 0.0;//to store discount per2

            //get the row level data by row index wise
            r_qty = purchaseInvoiceTable.getQuantity();
            r_rate = Double.parseDouble(purchaseInvoiceTable.getRate());
            if (!purchaseInvoiceTable.getDis_amt().isEmpty())
                r_dis_amt = Double.parseDouble(purchaseInvoiceTable.getDis_amt());
            if (!purchaseInvoiceTable.getDis_per().isEmpty())
                r_dis_per = Double.parseDouble(purchaseInvoiceTable.getDis_per());
            if (purchaseInvoiceTable.getDisPer2() != null && !purchaseInvoiceTable.getDisPer2().isEmpty()) {
                disPer2 = Double.parseDouble(purchaseInvoiceTable.getDisPer2());
            } else {
                disPer2 = 0.0;
            }
            r_tax_per = !purchaseInvoiceTable.getTax_per().isEmpty() ? Double.parseDouble(purchaseInvoiceTable.getTax_per()) : 0;
            prevTaxPer = r_tax_per;


            //calculate base amount
            base_amt = Double.parseDouble(r_qty) * r_rate;
            gross_amt = base_amt;
            total_amt = base_amt;
            total_base_amt = base_amt;
            taxable_amt = base_amt;
            total_dis_amt = r_dis_amt;

            //calculate discount amt with gross amt

            if (r_dis_amt > 0) {
                gross_amt = gross_amt - total_dis_amt;
                total_amt = total_amt - total_dis_amt;
                total_base_amt = total_amt;
                dis_amt_cal = total_dis_amt;

                row_dis_amt = row_dis_amt + total_dis_amt;
                taxable_amt = total_amt;
            }

            //calculate discount percentage
            if (r_dis_per > 0) {
                total_dis_per = (r_dis_per / 100) * (gross_amt);

                gross_amt = gross_amt - total_dis_per;
                total_base_amt = total_base_amt - total_dis_per;
                total_amt = total_amt - total_dis_per;
                row_dis_amt = row_dis_amt + total_dis_per;
                taxable_amt = total_amt;

            }

            // tax GST and CGST and SGST Calculations

            if (r_tax_per >= 0) {
                totalTax = (r_tax_per / 100) * (total_amt);
                net_amt = total_amt + totalTax;

            }
            // set all calculated above data to CMPTROW DTO
            SalesQuotationProductDTO selectedItem = tableView.getItems().get(rowIndex);
            selectedItem.setGross_amt(String.valueOf(base_amt));
            selectedItem.setNet_amt(String.valueOf(net_amt));
            selectedItem.setOrg_net_amt(String.valueOf(net_amt));
//        selectedItem.setOrg_taxable_amt(taxable_amt);
            selectedItem.setTaxable_amt(Double.valueOf(String.valueOf(taxable_amt)));
            selectedItem.setTotal_taxable_amt(Double.valueOf(String.valueOf(total_taxable_amt)));
            selectedItem.setIgst(Double.valueOf(String.valueOf(totalTax))); // for Tax Amount
            selectedItem.setSgst(Double.valueOf(String.valueOf(totalTax / 2))); // for Tax Amount
            selectedItem.setCgst(Double.valueOf(String.valueOf(totalTax / 2))); // for Tax  Amount
            selectedItem.setFinTaxAmt(Double.valueOf(String.valueOf(totalTax)));
            selectedItem.setFinal_dis_amt(Double.valueOf(String.valueOf(row_dis_amt)));

            // Create API Payload Parameters Adding
            selectedItem.setBase_amt(Double.valueOf(String.valueOf(base_amt)));
            selectedItem.setGross_amt(String.valueOf(gross_amt));
            selectedItem.setDis_per(String.valueOf((r_dis_per)));
            selectedItem.setDis_per_cal(Double.valueOf(String.valueOf(total_dis_per)));
            selectedItem.setDis_amt(String.valueOf((r_dis_amt)));
            selectedItem.setDis_amt_cal(Double.valueOf(String.valueOf(r_dis_amt)));
            selectedItem.setRow_dis_amt(Double.valueOf(String.valueOf(row_dis_amt)));
            selectedItem.setGross_amt1(Double.valueOf(String.valueOf(taxable_amt)));
            // tax percentage store cmptrow
            selectedItem.setGst(Double.valueOf(String.valueOf(r_tax_per)));
            selectedItem.setIgst(Double.valueOf(String.valueOf(r_tax_per)));
            selectedItem.setSgst(Double.valueOf(String.valueOf(r_tax_per / 2)));
            selectedItem.setCgst(Double.valueOf(String.valueOf(r_tax_per / 2)));
            selectedItem.setTotal_igst(Double.valueOf(String.valueOf(totalTax)));
            selectedItem.setTotal_sgst(Double.valueOf(String.valueOf(totalTax / 2)));
            selectedItem.setTotal_cgst(Double.valueOf(String.valueOf(totalTax / 2)));
            selectedItem.setFinal_amt(Double.valueOf(String.valueOf(net_amt)));
            selectedItem.setDisPer2(String.valueOf(disPer2));

//        //Display data to table view
//        tblcPurChallCmpTRowGrossAmt.setCellValueFactory(new PropertyValueFactory<>("gross_amt"));
//        tblcPurChallCmpTRowNetAmt.setCellValueFactory(new PropertyValueFactory<>("net_amt"));
            calculateGst(tableView, callback);
        }
    }

    public static void calculateGst(TableView<SalesQuotationProductDTO> tableView, TableCellCallback<Object[]> callback) {


//        String addChgAmt = tfPurchaseChallanAddChgAmt.getText();
//        Double addChargAmount = 0.0;
//        if(!addChgAmt.isEmpty()){
//            addChargAmount = Double.parseDouble(addChgAmt);
//        }

        Map<Double, Double> cgstTotals = new HashMap<>(); //used to merge same cgst Percentage
        Map<Double, Double> sgstTotals = new HashMap<>();//used to merge same sgst Percentage
        // Initialize totals to zero for each tax percentage
        for (Double taxPercentage : new Double[]{0.0, 3.0, 5.0, 12.0, 18.0, 28.0}) {
            cgstTotals.put(taxPercentage, 0.0);
            sgstTotals.put(taxPercentage, 0.0);
        }
        Double taxPercentage = 0.0; // variable to store tax percantage
        Double totalQuantity = 0.0; // variable to store  total Quantity
        Double totalFreeQuantity = 0.0; // variable to store  totalFree Quantity
        Double freeQuantity = 0.0; // variable to store row wise free qty
        Double totalGrossAmt = 0.0, grossAmt = 0.0; // variable to store total Gross Amount and row wise gross amount
        Double totaltaxableAmt = 0.0, taxableAmt = 0.0; // variable to store total taxable Amount and row wise taxable amount
        Double totalDisAmt = 0.0, disAmt = 0.0; // variable to store total discount Amount and row wise discount amount
        Double totalTaxAmt = 0.0, taxAmt = 0.0;// variable to store total taxamount Amount and row wise taxamount amount
        Double totalNetAmt = 0.0, netAmount = 0.0; // variable to store total Net Amount and row wise Net amount


        Double totalFinalSgst = 0.00; // variable to store total SGST Amount
        Double totalFinalCgst = 0.00; // variable to store total CGST Amount
        Double totalFinalIgst = 0.00; // variable to store total IGST Amount
        Double rowDiscount = 0.0;

        // Calculate totals for each transaction
        for (SalesQuotationProductDTO purchaseInvoiceTable : tableView.getItems()) {
            if (!purchaseInvoiceTable.getQuantity().isEmpty() && Double.parseDouble(purchaseInvoiceTable.getQuantity()) > 0 && !purchaseInvoiceTable.getRate().isEmpty() && Double.parseDouble(purchaseInvoiceTable.getRate()) > 0) {

                taxPercentage = Double.parseDouble(purchaseInvoiceTable.getTax_per());
                Double quantity = Double.parseDouble(purchaseInvoiceTable.getQuantity());
                if (purchaseInvoiceTable.getFree_qty().isEmpty()) {
                    freeQuantity = 0.0;
                } else {
                    freeQuantity = Double.parseDouble(purchaseInvoiceTable.getFree_qty());
                }

                // Total Calculations of each IGST, SGST, CGST
                totalQuantity += quantity;
                totalFreeQuantity += freeQuantity;
                Double igst = Double.parseDouble(purchaseInvoiceTable.getIgst().toString());
                Double cgst = Double.parseDouble(purchaseInvoiceTable.getCgst().toString());
                Double sgst = Double.parseDouble(purchaseInvoiceTable.getSgst().toString());
                totalFinalIgst += igst;
                totalFinalSgst += sgst;
                totalFinalCgst += cgst;

                cgstTotals.put(taxPercentage, cgstTotals.get(taxPercentage) + cgst); //0.0
                sgstTotals.put(taxPercentage, sgstTotals.get(taxPercentage) + sgst);

                //Total Calculation of gross amt ,taxable ,tax,discount
                netAmount = Double.parseDouble(purchaseInvoiceTable.getNet_amt());
                totalNetAmt += netAmount;
                grossAmt = Double.parseDouble(purchaseInvoiceTable.getGross_amt());
                totalGrossAmt += grossAmt;
                taxableAmt = Double.parseDouble(purchaseInvoiceTable.getTaxable_amt().toString());
                totaltaxableAmt += taxableAmt;
                disAmt = Double.parseDouble(purchaseInvoiceTable.getFinal_dis_amt().toString());
                totalDisAmt += disAmt;
                taxAmt = Double.parseDouble(purchaseInvoiceTable.getFinTaxAmt().toString());
                totalTaxAmt += taxAmt;

            }
        }

        ObservableList<GstDTO> observableList = FXCollections.observableArrayList();
        for (Double taxPer : new Double[]{3.0, 5.0, 12.0, 18.0, 28.0}) {
            double totalCGST = cgstTotals.get(taxPer);
            double totalSGST = sgstTotals.get(taxPer);
            double totalIGST = 0.0;

            if (totalCGST > 0) {
//                GstDTO gstDto :
                System.out.println("Tax Percentage: " + taxPer + "%");
                System.out.println("Total CGST: " + totalCGST);
                System.out.println("Total SGST: " + totalSGST);
                System.out.println();

                observableList.add(new GstDTO(String.format("%.2f", taxPer), String.format("%.2f", totalCGST), String.format("%.2f", totalSGST), String.valueOf(totalIGST)));

            }
//            tblcPurChallGST.setCellValueFactory(new PropertyValueFactory<>("taxPer"));
//            tblcPurChallCGST.setCellValueFactory(new PropertyValueFactory<>("cgst"));
//            tblcPurChallSGST.setCellValueFactory(new PropertyValueFactory<>("sgst"));
        }
//        tblvPurChallGST.setItems(observableList);
//        Platform.runLater(() -> {
//            tblvPurChallGST.refresh();
//
//        });

        if (callback != null) {

            Object[] object = new Object[10];

            object[0] = decimalFormat.format(totalQuantity);

            object[1] = String.valueOf(totalFreeQuantity);

            object[2] = String.format("%.2f", totalNetAmt);

            object[3] = String.format("%.2f", totalGrossAmt);

            object[4] = String.format("%.2f", totalDisAmt);

            //  lblPurChallTotalTaxableAmount.setText(String.format("%.2f", (totalGrossAmt - totalDisAmt + addChargAmount)));
            object[5] = String.format("%.2f", (totalGrossAmt - totalDisAmt + 0.0));

            object[6] = String.format("%.2f", totalTaxAmt);

            object[7] = String.format("%.2f", totalFinalSgst);

            object[8] = String.format("%.2f", totalFinalCgst);

            object[9] = observableList;


            if (callback != null) {
                callback.call(object);
            }
        }
    }


    public static void discountPropotionalCalculation(String disproportionalPer, String disproportionalAmt, String additionalCharges, TableView<SalesQuotationProductDTO> tableView, TableCellCallback<Object[]> callback) {

        //get discount percentage and discount amount to textfield

        Double dis_proportional_per = 0.0;//store the textfield discount per String to double
        Double dis_proportional_amt = 0.0;//store the textfield discount amt String to double
        if (!disproportionalPer.isEmpty()) {// check if row level discount per is not empty
            //row level Discount Percentage
            dis_proportional_per = Double.parseDouble(disproportionalPer);
        } else {
            dis_proportional_per = 0.0;
        }
        if (!disproportionalAmt.isEmpty()) {// check if row level discount amt is not empty
            //row level Discount amount
            dis_proportional_amt = Double.parseDouble(disproportionalAmt);
        } else {
            dis_proportional_amt = 0.0;
        }
        System.out.println("dis_proportional_per discountPropotionalCalculation :: " + dis_proportional_per);
        Double finalNetAmt = 0.0;
        Double r_tax_per = 0.0;//store the Tax per data
        Double total_tax_amt = 0.0;//store total tax amount of each row level
        Double netAmt = 0.0;//Store net Amount after calculating  dis and tax
        Double rowTaxableAmt = 0.0; //store the row level calculated taxable amount
        Double totalTaxableAmt = 0.0;//store the total calculated taxable amount
        Double totalDisPropAmt = 0.0;//store the discount calculated proportional amt
        Double rowDisPropAmt = 0.0;//stotre the row level discount calculated amount
        Double totalTaxableAmtAdditional = 0.0;
        Double rowtotalTaxableAmtAdditional = 0.0;


        //calculate total taxable amount
        for (int i = 0; i < tableView.getItems().size(); i++) {
            System.out.println("Gross amt in Prop--->" + tableView.getItems().get(i).getGross_amt());
            rowCalculationForPurcInvoice(i, tableView, callback);//call row calculation function
            SalesQuotationProductDTO purchaseInvoiceTable = tableView.getItems().get(i);
            totalTaxableAmt = totalTaxableAmt + Double.parseDouble(purchaseInvoiceTable.getTaxable_amt().toString());

        }
        System.out.println("totalTaxableAmt discountPropotionalCalculation :: " + totalTaxableAmt);

        Double rowDisc = 0.0;
        //get data of netamount, tax per,and row taxable amount from cmptrow
        for (SalesQuotationProductDTO purchaseInvoiceTable : tableView.getItems()) {
            System.out.println("getFinal_dis_amt--->" + purchaseInvoiceTable.getFinal_dis_amt());
            rowDisc = Double.parseDouble(purchaseInvoiceTable.getFinal_dis_amt().toString());
            System.out.println("netAmt--->" + purchaseInvoiceTable.getNet_amt());
            netAmt = Double.parseDouble(purchaseInvoiceTable.getNet_amt());
            System.out.println("netAmt--->" + purchaseInvoiceTable.getTax_per());
            r_tax_per = Double.parseDouble(purchaseInvoiceTable.getTax_per());
            rowTaxableAmt = Double.parseDouble(purchaseInvoiceTable.getTaxable_amt().toString());
            System.out.println("rowTaxableAmt discountPropotionalCalculation :: " + rowTaxableAmt + "netAmt" + netAmt);

            //calculate discount proportional percentage and  the taxable amount
            if (dis_proportional_per > 0.0) {
                totalDisPropAmt = (dis_proportional_per / 100) * (totalTaxableAmt);
                rowDisPropAmt = (rowTaxableAmt * totalDisPropAmt) / totalTaxableAmt;
                System.out.println("row dis pro amt: " + rowDisPropAmt);
                rowTaxableAmt = rowTaxableAmt - rowDisPropAmt;
                rowDisc += rowDisPropAmt;
                totalTaxableAmtAdditional = rowTaxableAmt;
            } else {
                totalTaxableAmtAdditional = rowTaxableAmt;
            }

            System.out.println("rowDisc : " + rowDisc);


//
            if (dis_proportional_amt > 0.0) {
                totalDisPropAmt = dis_proportional_amt;
                rowDisPropAmt = (rowTaxableAmt * totalDisPropAmt) / totalTaxableAmt;
                rowDisc += rowDisPropAmt;
                rowTaxableAmt = rowTaxableAmt - rowDisPropAmt;
                totalTaxableAmtAdditional = rowTaxableAmt;
            } else {
                totalTaxableAmtAdditional = rowTaxableAmt;

            }


            //calculate tax per and store the data to final net AMount
            if (additionalCharges.isEmpty()) {
                if (r_tax_per > 0) {
                    total_tax_amt = (r_tax_per / 100) * (rowTaxableAmt);
                    System.out.println("total_tax_amt" + total_tax_amt);
                    netAmt = rowTaxableAmt + total_tax_amt;
                    System.out.println("total netAmt  :" + netAmt + "" + rowTaxableAmt);
//
                }
            }

            //Set data to cmpTRow
            purchaseInvoiceTable.setNet_amt(String.format("%.2f", netAmt));
            purchaseInvoiceTable.setTotal_taxable_amt(Double.parseDouble(String.valueOf(totalTaxableAmtAdditional)));
            System.out.println("Total Tax Amt--->" + total_tax_amt);
            purchaseInvoiceTable.setIgst(Double.valueOf(String.valueOf(total_tax_amt)));
            purchaseInvoiceTable.setCgst(Double.valueOf(String.valueOf(total_tax_amt / 2)));
            purchaseInvoiceTable.setSgst(Double.valueOf(String.valueOf(total_tax_amt / 2)));
            purchaseInvoiceTable.setFinTaxAmt(Double.valueOf(String.valueOf(total_tax_amt)));
            purchaseInvoiceTable.setFinal_dis_amt(Double.valueOf(String.valueOf(rowDisc)));
            purchaseInvoiceTable.setInvoice_dis_amt(Double.valueOf(String.valueOf(rowDisPropAmt)));

        }
        //Check additional charge is Empty or not if not empty call to the additional charges function
        if (!additionalCharges.isEmpty()) {
            //additionalChargesCalculation();
        } /*else {
            System.out.println("Additional Charges is empty");
            additionalChargesCalculation();
        }*/
        System.out.println("from discountPropotionalCalculation");
        calculateGst(tableView, callback);
    }


//    public static void additionalChargesCalculation(String additionalCharges, TableView<SalesQuotationProductDTO> tableView,TableCellCallback<Object[]> callback) {
//
//
//        Double additionalChargeAmt = 0.0;//store data textfield string to double
//
//        if (!additionalCharges.isEmpty()) {
//            additionalChargeAmt = Double.parseDouble(additionalCharges);//convert string to double
//
//
//            Double finalNetAmt = 0.0;
//            Double r_tax_per = 0.0;
//            Double total_tax_amt = 0.0;
//            Double netAmt = 0.0;
//            Double rowTaxableAmt = 0.0;
//            Double totalTaxableAmt = 0.0;
//            Double totalDisPropAmt = 0.0;
//            Double rowDisPropAmt = 0.0;
//
//            for (int i = 0; i < tableView.getItems().size(); i++) {
//                PurchaseInvoiceTable purchaseInvoiceTable = tableView.getItems().get(i);
//                totalTaxableAmt = totalTaxableAmt + Double.parseDouble(purchaseInvoiceTable.getTotal_taxable_amt());
//
//            }
//            for (SalesQuotationProductDTO purchaseInvoiceTable : tableView.getItems()) {
//
//                netAmt = Double.parseDouble(purchaseInvoiceTable.getNet_amount());
//                r_tax_per = Double.parseDouble(purchaseInvoiceTable.getTax());
//                rowTaxableAmt = Double.parseDouble(purchaseInvoiceTable.getTotal_taxable_amt());
//
//
//                totalDisPropAmt = additionalChargeAmt;
//                rowDisPropAmt = (rowTaxableAmt * totalDisPropAmt) / totalTaxableAmt;
//                rowTaxableAmt = rowTaxableAmt + rowDisPropAmt;
//
//                if (r_tax_per > 0) {
//                    total_tax_amt = (r_tax_per / 100) * (rowTaxableAmt);
//                    netAmt = rowTaxableAmt + total_tax_amt;
//                }
//
//                purchaseInvoiceTable.setNet_amount(String.format("%.3f", netAmt));
//                purchaseInvoiceTable.setCgst(String.valueOf(total_tax_amt / 2));
//                purchaseInvoiceTable.setSgst(String.valueOf(total_tax_amt / 2));
//                purchaseInvoiceTable.setFinal_tax_amt(String.valueOf(total_tax_amt));
//
//
//            }
//
//        }
//        //call calculate gst function
//        calculateGst(tableView,callback);
//
//    }

}
