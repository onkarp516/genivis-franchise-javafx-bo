
package com.opethic.genivis.controller.tranx_purchase;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.opethic.genivis.GenivisApplication;
import com.opethic.genivis.controller.dialogs.AdditionalCharges;
import com.opethic.genivis.controller.dialogs.BatchWindow;
import com.opethic.genivis.controller.dialogs.SingleInputDialogs;
import com.opethic.genivis.controller.master.ProductCreateController;
import com.opethic.genivis.controller.master.ledger.common.LedgerMessageConsts;
import com.opethic.genivis.controller.tranx_sales.common.LineNumbersCellFactory;
import com.opethic.genivis.dto.*;
import com.opethic.genivis.dto.add_charges.CustomAddChargesDTO;
import com.opethic.genivis.dto.cmp_t_row.BatchWindowTableDTO;
import com.opethic.genivis.dto.pur_invoice.*;
import com.opethic.genivis.dto.pur_invoice.reqres.*;
import com.opethic.genivis.dto.reqres.product.*;
import com.opethic.genivis.dto.reqres.sales_tranx.Row1;
import com.opethic.genivis.network.APIClient;
import com.opethic.genivis.network.EndPoints;
import com.opethic.genivis.utils.*;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.StringBinding;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Screen;
import javafx.stage.Stage;
import org.apache.http.util.TextUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URL;
import java.net.http.HttpResponse;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

import static com.opethic.genivis.utils.FxmFileConstants.*;
import static com.opethic.genivis.utils.Globals.decimalFormat;
import static com.opethic.genivis.utils.Globals.mapToStringforFormData;
import static javafx.scene.control.PopupControl.USE_COMPUTED_SIZE;
import static javafx.scene.control.PopupControl.USE_PREF_SIZE;

public class PurchaseOrderToInvoiceController implements Initializable {

    @FXML
    BorderPane purScrollPane;
    @FXML
    private HBox firstTop;

    @FXML
    private HBox po2iBottomMain, po2iFirstRow, po2iSecondRow;
    @FXML
    private VBox po2iBottomFirstV, po2iBottomSecondV, po2iBottomThirdV, po2iTotalMainDiv, po2iTotalMainInnerDiv;

    @FXML
    public TabPane tpPurOrderToInvoice;
    @FXML
    Tab tabPurOrderToInvoiceLedger, tabPurOrderToInvoiceProduct, tabPurOrderToInvoiceBatch;
    //Product Info Labels
    @FXML
    Label lbPurOrderToInvoiceProductBrand, lbPurOrderToInvoiceProductGroup, lbPurOrderToInvoiceProductSubGroup,
            lbPurOrderToInvoiceProductCategory, lbPurOrderToInvoiceProductMaxStock, lbPurOrderToInvoiceProductMinStock,
            lbPurOrderToInvoiceProductShelfId, lbPurOrderToInvoiceProductCost, lbPurOrderToInvoiceProductMargin,
            lbPurOrderToInvoiceProductTax, lbPurOrderToInvoiceProductTaxType, lbPurOrderToInvoiceProductHsn;

    //LedgerInfo Labels
    @FXML
    Label lbPurOrderToInvoiceLedgerGstNo, lbPurOrderToInvoiceLedgerFssai, lbPurOrderToInvoiceLedgerLicense,
            lbPurOrderToInvoiceLedgerArea, lbPurOrderToInvoiceLedgerBank, lbPurOrderToInvoiceLedgerRoute,
            lbPurOrderToInvoiceLedgerContactPerson, lbPurOrderToInvoiceLedgerTransport, lbPurOrderToInvoiceLedgerCreditDays;
    @FXML
    private TableView<GstDTO> tvGST_Table;

    @FXML
    private TableColumn<GstDTO, String> tc_gst, tc_sgst, tc_cgst, tc_igst;

    @FXML
    private TableView<PurchaseInvoiceTable> tvPurchaseInvoiceTable;

    @FXML
    private TableColumn<PurchaseInvoiceTable, String> tcSrNo, tcParticulars, tcPackage, tcLevelA, tcLevelB, tcLevelC, tcUnit, tcBatch, tcQuantity, tcFreeQty;

    @FXML
    private TableColumn<PurchaseInvoiceTable, String> tcRate;

    @FXML
    private TableColumn<PurchaseInvoiceTable, String> tcGrossAmount;

    @FXML
    private TableColumn<PurchaseInvoiceTable, String> tcDisPer;

    @FXML
    private TableColumn<PurchaseInvoiceTable, String> tcDisAmt;

    @FXML
    private TableColumn<PurchaseInvoiceTable, String> tcTax;

    @FXML
    private TableColumn<PurchaseInvoiceTable, String> tcNetAmount;

    @FXML
    private TableColumn<PurchaseInvoiceTable, String> tcAction;

    JSONArray rowDelDetailsIds = new JSONArray();

    private ObservableList<LevelAForPurInvoice> levelAList = FXCollections.observableArrayList();

    private ObservableList<LevelBForPurInvoice> levelBList = FXCollections.observableArrayList();

    private ObservableList<CommonDTO> levelCList = FXCollections.observableArrayList();

    private ObservableList<CommonDTO> unitList = FXCollections.observableArrayList();

    @FXML
    private TextField tfTranxDate, tfInvoiceNo, tfLedgerName, tfInvoiceDate, tfPurchaseSerial, tfNarration;

    @FXML
    private ComboBox<CommonDTO> cbPurchaseAc;

    @FXML
    private ComboBox<CommonDTO> cbSupplierGSTIN;

    String purchaseAcID = "";

    String supplierGSTIN_ID = "";

    String ledger_id = "";
    public static String orderToInvoice = "";

//    @FXML
//    RadioButton rbCredit, rbCash;

    private final Logger purchOrderToInvLogger = LogManager.getLogger(ProductCreateController.class);

    private ObservableList<CommonDTO> purchaseAcList = FXCollections.observableArrayList();

    private ObservableList<CommonDTO> supplierGSTINList = FXCollections.observableArrayList();

    private ToggleGroup toggleGroup = new ToggleGroup();

    private String selectedRadioValue = "Credit";
    @FXML
    private Button btnSubmit, btnCancel;
    private String referenceType = "", referenceId = "";

    @FXML
    private Label lbGrossTotal, lbDiscount, lbTotal, lbTax, lbBillAmount, lblPoToPiTotalQtyEdit, lblPoToPiFreeQtyEdit;

    Map<String, String> purchase_invoice_map = new HashMap<>();

    private String ledgerStateCode = "";
    private Boolean taxFlag;

    private String totalamt = "";

    private Double total_taxable_amt = 0.0;

    private String total_purchase_discount_amt = "";

    @FXML
    private TextField purchase_discount, purchase_discount_amt, tfAddCharges;
    String gst_no = "";
    List<CustomAddChargesDTO> addChargesDTOList = new ArrayList<>();
    private final List<AddChargesListDTO> addChargesResLst = new ArrayList<>();
    @FXML
    private CheckBox chbRounfOff;
    @FXML
    private Label lblPoToPiRoundOffEdit;

    @FXML
    private TableView tvInvoiceProductHistory;
//    private TableView<InvoiceProductHistoryTable> tvInvoiceProductHistory;

    @FXML
    private TableColumn PurOrderToInvoiceProHisSupplierName, PurOrderToInvoiceProHisInvNo, PurOrderToInvoiceProHisInvDate, PurOrderToInvoiceProHisBatch, PurOrderToInvoiceProHisMRP,
            PurOrderToInvoiceProHisQty, PurOrderToInvoiceProHisRate, PurOrderToInvoiceProHisCost, PurOrderToInvoiceProHisDisPer, PurOrderToInvoiceProHisDisAmt;

    @FXML
    public ComboBox<String> cmbPaymentMode;
    private String selectedPayMode = "Credit";
    private ObservableList<String> payment_mode_list = FXCollections.observableArrayList("Credit", "Cash");

    Integer purchase_id = -1;

    Map<String, Object> edit_response_map = new HashMap<>();

    Map<String, String> invoice_data_map = new HashMap<>();

    List<PurchaseOrderToInvoiceRowListDTO> rowListDTOS = new ArrayList<>();

    Long stateCode = 0L, CompanyStateCode = 0L;
    private String trackingNumber = "";

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        resposiveScreenCss();

        //         Enter traversal
        purScrollPane.addEventFilter(KeyEvent.KEY_PRESSED, (KeyEvent event) -> {
            if (event.getCode() == KeyCode.ENTER) {
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
            } else if (event.getCode() == KeyCode.X && event.isControlDown()) {
                btnCancel.fire();
            } else if (event.getCode() == KeyCode.S && event.isControlDown()) {
                btnSubmit.fire();
            }
        });

        System.out.println("orderToInvoiceIddd " + orderToInvoice);
        purchase_id = PurchaseInvoiceListController.getPurchaseId();
        PurchaseInvoiceListController.purchase_id = -1;

        getPurchaseInvoiceByIdNew(orderToInvoice);

        tvInvoiceProductHistory.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);

        gstInitialize();

        sceneInitilization();

        tvGST_Table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

//        purScrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

//        rbCash.setToggleGroup(toggleGroup);
//        rbCredit.setToggleGroup(toggleGroup);

        Platform.runLater(() -> {
            tfLedgerName.requestFocus();
        });

        tfLedgerName.setEditable(false);

        tableInitiliazation();
        responsiveCMPTRow();
        getPaymentModes();

        cmbPaymentMode.getSelectionModel().select("Credit");
        cmbPaymentMode.focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if (newValue) {
                    cmbPaymentMode.show();
                }
            }
        });
        tfPurchaseSerial.setText(invoice_data_map.get("purchase_sr_no"));

//            getPurchaseSerial();
        tfPurchaseSerial.setEditable(false);
        tfPurchaseSerial.setFocusTraversable(false);
        tvPurchaseInvoiceTable.setFocusTraversable(false);
        tvInvoiceProductHistory.setFocusTraversable(false);
        tvGST_Table.setFocusTraversable(false);
        //DateValidator.applyDateFormat(tfInvoiceDate);

        tfTranxDate.setFocusTraversable(false);
        tfTranxDate.setEditable(false);
        if (invoice_data_map.get("transaction_dt") != null) {
            LocalDate transaction_dt = LocalDate.parse(invoice_data_map.get("transaction_dt"));      //tobe uncomment
            tfTranxDate.setText(transaction_dt.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));    //tobe uncomment

        } else {
            LocalDate transaction_dt = LocalDate.now();      //tobe uncomment
            tfTranxDate.setText(transaction_dt.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));    //tobe uncomment

        }
        System.out.println("transaction_dttt-- " + invoice_data_map.get("transaction_dt") + " inviocee_dttt-- " + invoice_data_map.get("invoice_dt"));
        if (invoice_data_map.get("invoice_dt") != null) {

            LocalDate invoice_dt = LocalDate.parse(invoice_data_map.get("invoice_dt"));                //tobe uncomment
            tfInvoiceDate.setText(invoice_dt.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));       //tobe uncomment
            Communicator.purTranxDate = invoice_dt.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        } else {
            LocalDate invoice_dt = LocalDate.now();                //tobe uncomment
            tfInvoiceDate.setText(invoice_dt.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));       //tobe uncomment
            Communicator.purTranxDate = invoice_dt.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        }

        cbPurchaseAc.setFocusTraversable(true);

        cbSupplierGSTIN.setFocusTraversable(true);

        getPurchaseAc();
        getPurchaseSerial();

        tfLedgerName.setEditable(false);
//        tfLedgerName.setOnMouseClicked(event -> {
////            handleTfLedgerName();
//        });

//        AutoCompleteBox<CommonDTO> autoCompleteBox = new AutoCompleteBox<>(cbSupplierGSTIN, -1);


        tfLedgerName.setText(ledgersById(Long.valueOf(invoice_data_map.get("supplierId"))));      //tobe uncomment
        if (supplierGSTINList != null && !supplierGSTINList.isEmpty()) {
            cbSupplierGSTIN.setItems(supplierGSTINList);
            cbSupplierGSTIN.setValue(supplierGSTINList.get(0));
        }

        tfInvoiceNo.setText(invoice_data_map.get("invoice_no"));
        tfNarration.setText(invoice_data_map.get("narration"));

        for (CommonDTO commonDTO : purchaseAcList) {
            if (commonDTO.getId().equals(invoice_data_map.get("purchase_account_ledger_id"))) {
                cbPurchaseAc.setValue(commonDTO);
                purchaseAcID = commonDTO.getId();
            }
        }

        chbRounfOff.setOnAction(e -> {
            roundOffCalculations();
        });


        CommonValidationsUtils.onlyEnterNumbersLimit1(purchase_discount, 6);
        CommonValidationsUtils.onlyEnterNumbersLimit1(purchase_discount_amt, 6);

        nodetraversal(tfTranxDate, tfLedgerName);
        nodetraversal(tfLedgerName, cbSupplierGSTIN);
        nodetraversal(cbSupplierGSTIN, tfInvoiceNo);
        nodetraversal(tfInvoiceNo, tfInvoiceDate);
        nodetraversal(tfInvoiceDate, cbPurchaseAc);

        columnVisibility(tcLevelA, false);
        columnVisibility(tcLevelB, false);
        columnVisibility(tcLevelC, false);

//        invoiceProductHistoryTable();

    }

    private void gstInitialize() {
        tvGST_Table.setFocusTraversable(false);
        tc_gst.setSortable(false);
        tc_cgst.setSortable(false);
        tc_sgst.setSortable(false);
        tc_igst.setSortable(false);

        tc_gst.setCellValueFactory(cellData -> {
            StringProperty taxProperty = cellData.getValue().taxPerProperty();
            return Bindings.createStringBinding(
                    () -> "(" + (int) Double.parseDouble(taxProperty.get()) + " %)",
                    taxProperty
            );
        });

        tc_gst.setStyle("-fx-alignment: CENTER;");
        tc_cgst.setCellValueFactory(cellData -> cellData.getValue().cgstProperty());
        tc_cgst.setCellFactory(col -> new TableCell<GstDTO, String>() {
            private final Label label = new Label();
            private VBox vBox = new VBox(label);

            @Override
            protected void updateItem(String item, boolean empty) {
                //label.setStyle("-fx-font-family: Arial; -fx-font-size: 16px;");
                vBox.setAlignment(Pos.CENTER);
                vBox.setMaxHeight(30);
                vBox.setMinHeight(30);
                vBox.setPrefHeight(30);
                vBox.setStyle("-fx-border-color: #DCDCDC; -fx-background-color: #EBEBEB; -fx-background-radius: 3; -fx-border-width: 1px; -fx-border-radius: 3;");
                vBox.setAlignment(Pos.CENTER);
                super.updateItem(item, empty);
                if (item == null || empty) {
                    setText(null);
                    setGraphic(null);
                } else {
                    label.setText(item);
                    // Add border with width
                    vBox.setStyle("-fx-border-color: #DCDCDC; -fx-background-color: #EBEBEB; -fx-background-radius: 3; -fx-border-width: 1px; -fx-border-radius: 3;");
                    setGraphic(vBox);
                }
            }
        });

        tc_cgst.setStyle("-fx-alignment: CENTER;");
        tc_sgst.setCellValueFactory(cellData -> cellData.getValue().sgstProperty());
        tc_sgst.setStyle("-fx-alignment: CENTER;");

        tc_sgst.setCellFactory(col -> new TableCell<GstDTO, String>() {
            private final Label label = new Label();
            private VBox vBox = new VBox(label);

            @Override
            protected void updateItem(String item, boolean empty) {
                //label.setStyle("-fx-font-family: Arial; -fx-font-size: 16px;");
                vBox.setAlignment(Pos.CENTER);
                vBox.setMaxHeight(30);
                vBox.setMinHeight(30);
                vBox.setPrefHeight(30);
                vBox.setStyle("-fx-border-color: #DCDCDC; -fx-background-color: #EBEBEB; -fx-background-radius: 3; -fx-border-width: 1px; -fx-border-radius: 3;");

                super.updateItem(item, empty);
                if (item == null || empty) {
                    setText(null);
                    setGraphic(null);
                } else {
                    label.setText(item);
                    // Add border with width
                    setGraphic(vBox);
                }
            }
        });
        tc_igst.setCellValueFactory(cellData -> cellData.getValue().igstProperty());
        tc_igst.setStyle("-fx-alignment: CENTER;");
        tc_igst.setCellFactory(col -> new TableCell<GstDTO, String>() {
            private final Label label = new Label();
            private VBox vBox = new VBox(label);

            @Override
            protected void updateItem(String item, boolean empty) {
                //label.setStyle("-fx-font-family: Arial; -fx-font-size: 16px;");
                vBox.setAlignment(Pos.CENTER);
                vBox.setMaxHeight(30);
                vBox.setMinHeight(30);
                vBox.setPrefHeight(30);
                vBox.setStyle("-fx-border-color: #DCDCDC; -fx-background-color: #EBEBEB; -fx-background-radius: 3; -fx-border-width: 1px; -fx-border-radius: 3;");

                super.updateItem(item, empty);
                if (item == null || empty) {
                    setText(null);
                    setGraphic(null);
                } else {
                    label.setText(item);
                    // Add border with width
                    setGraphic(vBox);
                }
            }
        });

        tvGST_Table.getItems().addAll(new GstDTO("", "", "", ""));

    }

    private void resposiveScreenCss() {
        // For Responsive Resolutions Css Style Apply
        double height = Screen.getPrimary().getBounds().getHeight();
        double width = Screen.getPrimary().getBounds().getWidth();
        System.out.println("width" + width);
        if (width >= 992 && width <= 1024) {
            po2iFirstRow.setSpacing(10);
            po2iSecondRow.setSpacing(10);
            po2iTotalMainDiv.setSpacing(10);
            po2iTotalMainInnerDiv.setSpacing(8);

            // Set preferred widths as percentages of the HBox width
            po2iBottomFirstV.prefWidthProperty().bind(po2iBottomMain.widthProperty().multiply(0.54));
            po2iBottomSecondV.prefWidthProperty().bind(po2iBottomMain.widthProperty().multiply(0.24));
            po2iBottomThirdV.prefWidthProperty().bind(po2iBottomMain.widthProperty().multiply(0.22));
//            so2cBottomMain.setPrefHeight(150);

            tvGST_Table.getStylesheets().add(GenivisApplication.class.getResource("ui/css/purchase_challan_css/gst_table_css/gst_table_992_1024.css").toExternalForm());
            tvInvoiceProductHistory.getStylesheets().add(GenivisApplication.class.getResource("ui/css/purchase_challan_css/supplier_table_css/supplier_table_992_1024.css").toExternalForm());
            purScrollPane.getStylesheets().add(GenivisApplication.class.getResource("ui/css/TranxCommonCssStyles/tranxCommonCssStyle1.css").toExternalForm());
        } else if (width >= 1025 && width <= 1280) {
            po2iFirstRow.setSpacing(10);
            po2iSecondRow.setSpacing(10);
            po2iTotalMainDiv.setSpacing(10);
            po2iTotalMainInnerDiv.setSpacing(8);
            // Set preferred widths as percentages of the HBox width
            po2iBottomFirstV.prefWidthProperty().bind(po2iBottomMain.widthProperty().multiply(0.58));
            po2iBottomSecondV.prefWidthProperty().bind(po2iBottomMain.widthProperty().multiply(0.20));
            po2iBottomThirdV.prefWidthProperty().bind(po2iBottomMain.widthProperty().multiply(0.22));
//            so2cBottomMain.setPrefHeight(150);
            tvGST_Table.getStylesheets().add(GenivisApplication.class.getResource("ui/css/purchase_challan_css/gst_table_css/gst_table_1025_1280.css").toExternalForm());
            tvInvoiceProductHistory.getStylesheets().add(GenivisApplication.class.getResource("ui/css/purchase_challan_css/supplier_table_css/supplier_table_1025_1280.css").toExternalForm());
            purScrollPane.getStylesheets().add(GenivisApplication.class.getResource("ui/css/TranxCommonCssStyles/tranxCommonCssStyle2.css").toExternalForm());
        } else if (width >= 1281 && width <= 1368) {

            po2iFirstRow.setSpacing(12);
            po2iSecondRow.setSpacing(12);
            po2iTotalMainDiv.setSpacing(12);
            po2iTotalMainInnerDiv.setSpacing(10);

            // Set preferred widths as percentages of the HBox width
            po2iBottomFirstV.prefWidthProperty().bind(po2iBottomMain.widthProperty().multiply(0.6));
            po2iBottomSecondV.prefWidthProperty().bind(po2iBottomMain.widthProperty().multiply(0.20));
            po2iBottomThirdV.prefWidthProperty().bind(po2iBottomMain.widthProperty().multiply(0.20));
//            so2cBottomMain.setPrefHeight(150);


            tvGST_Table.getStylesheets().add(GenivisApplication.class.getResource("ui/css/purchase_challan_css/gst_table_css/gst_table_1281_1368.css").toExternalForm());
            tvInvoiceProductHistory.getStylesheets().add(GenivisApplication.class.getResource("ui/css/purchase_challan_css/supplier_table_css/supplier_table_1281_1368.css").toExternalForm());
            purScrollPane.getStylesheets().add(GenivisApplication.class.getResource("ui/css/TranxCommonCssStyles/tranxCommonCssStyle3.css").toExternalForm());
        } else if (width >= 1369 && width <= 1400) {
            po2iFirstRow.setSpacing(15);
            po2iSecondRow.setSpacing(15);
            po2iTotalMainDiv.setSpacing(15);
            po2iTotalMainInnerDiv.setSpacing(10);
            // Set preferred widths as percentages of the HBox width
            po2iBottomFirstV.prefWidthProperty().bind(po2iBottomMain.widthProperty().multiply(0.54));
            po2iBottomSecondV.prefWidthProperty().bind(po2iBottomMain.widthProperty().multiply(0.22));
            po2iBottomThirdV.prefWidthProperty().bind(po2iBottomMain.widthProperty().multiply(0.24));
//            so2cBottomMain.setPrefHeight(150);


            tvGST_Table.getStylesheets().add(GenivisApplication.class.getResource("ui/css/purchase_challan_css/gst_table_css/gst_table_1369_1400.css").toExternalForm());
            tvInvoiceProductHistory.getStylesheets().add(GenivisApplication.class.getResource("ui/css/purchase_challan_css/supplier_table_css/supplier_table_1369_1400.css").toExternalForm());
            purScrollPane.getStylesheets().add(GenivisApplication.class.getResource("ui/css/TranxCommonCssStyles/tranxCommonCssStyle4.css").toExternalForm());
        } else if (width >= 1401 && width <= 1440) {
            po2iFirstRow.setSpacing(15);
            po2iSecondRow.setSpacing(15);
            po2iTotalMainDiv.setSpacing(15);
            po2iTotalMainInnerDiv.setSpacing(10);

            // Set preferred widths as percentages of the HBox width
            po2iBottomFirstV.prefWidthProperty().bind(po2iBottomMain.widthProperty().multiply(0.58));
            po2iBottomSecondV.prefWidthProperty().bind(po2iBottomMain.widthProperty().multiply(0.20));
            po2iBottomThirdV.prefWidthProperty().bind(po2iBottomMain.widthProperty().multiply(0.22));
//            so2cBottomMain.setPrefHeight(150);


            tvGST_Table.getStylesheets().add(GenivisApplication.class.getResource("ui/css/purchase_challan_css/gst_table_css/gst_table_1401_1440.css").toExternalForm());
            tvInvoiceProductHistory.getStylesheets().add(GenivisApplication.class.getResource("ui/css/purchase_challan_css/supplier_table_css/supplier_table_1401_1440.css").toExternalForm());
            purScrollPane.getStylesheets().add(GenivisApplication.class.getResource("ui/css/TranxCommonCssStyles/tranxCommonCssStyle5.css").toExternalForm());
        } else if (width >= 1441 && width <= 1680) {

            po2iFirstRow.setSpacing(20);
            po2iSecondRow.setSpacing(20);
            po2iTotalMainDiv.setSpacing(20);
            po2iTotalMainInnerDiv.setSpacing(10);
            // Set preferred widths as percentages of the HBox width
            po2iBottomFirstV.prefWidthProperty().bind(po2iBottomMain.widthProperty().multiply(0.62));
            po2iBottomSecondV.prefWidthProperty().bind(po2iBottomMain.widthProperty().multiply(0.18));
            po2iBottomThirdV.prefWidthProperty().bind(po2iBottomMain.widthProperty().multiply(0.20));
//            scBottomMain.setPrefHeight(150);


            tvGST_Table.getStylesheets().add(GenivisApplication.class.getResource("ui/css/purchase_challan_css/gst_table_css/gst_table_1441_1680.css").toExternalForm());
            tvInvoiceProductHistory.getStylesheets().add(GenivisApplication.class.getResource("ui/css/purchase_challan_css/supplier_table_css/supplier_table_1441_1680.css").toExternalForm());
            purScrollPane.getStylesheets().add(GenivisApplication.class.getResource("ui/css/TranxCommonCssStyles/tranxCommonCssStyle6.css").toExternalForm());
        } else if (width >= 1681 && width <= 1920) {
            po2iFirstRow.setSpacing(20);
            po2iSecondRow.setSpacing(20);
            po2iTotalMainDiv.setSpacing(20);
            po2iTotalMainInnerDiv.setSpacing(10);
            // Set preferred widths as percentages of the HBox width
            po2iBottomFirstV.prefWidthProperty().bind(po2iBottomMain.widthProperty().multiply(0.6));
            po2iBottomSecondV.prefWidthProperty().bind(po2iBottomMain.widthProperty().multiply(0.18));
            po2iBottomThirdV.prefWidthProperty().bind(po2iBottomMain.widthProperty().multiply(0.22));
//            scBottomMain.setPrefHeight(150);

            tvGST_Table.getStylesheets().add(GenivisApplication.class.getResource("ui/css/gst_table.css").toExternalForm());
            tvInvoiceProductHistory.getStylesheets().add(GenivisApplication.class.getResource("ui/css/invoice_product_history_table.css").toExternalForm());
            purScrollPane.getStylesheets().add(GenivisApplication.class.getResource("ui/css/TranxCommonCssStyles/tranxCommonCssStyle7.css").toExternalForm());

        }
    }

    public String ledgersById(Long id) {

        Map<String, String> map = new HashMap<>();
        map.put("id", String.valueOf(id));

        String formData = mapToStringforFormData(map);
        HttpResponse<String> response = APIClient.postFormDataRequest(formData, "get_ledgers_by_id");

        String json = response.body();
        JsonObject jsonObject = new Gson().fromJson(json, JsonObject.class);
        JsonObject responseObj = jsonObject.getAsJsonObject("response");
        String ledgerName = responseObj.get("ledger_name").getAsString();
        ledgerStateCode = responseObj.get("ledgerStateCode").getAsString();
        JsonArray gstdetailsArray = responseObj.getAsJsonArray("gstdetails");
        if (gstdetailsArray != null) {
            for (JsonElement element : gstdetailsArray) {
                JsonObject gstdetail = element.getAsJsonObject();
                String gstin = gstdetail.get("gstin").getAsString();
                String gst_id = gstdetail.get("id").getAsString();
                supplierGSTINList.add(new CommonDTO(gstin, gst_id));
            }
        }

        return ledgerName;
    }

    //function for payment mode selection
    public void handleCmbPaymentMode(ActionEvent actionEvent) {
        selectedPayMode = cmbPaymentMode.getValue();

    }

    private void getPaymentModes() {
        cmbPaymentMode.setItems(payment_mode_list);
    }

    private void getPurchaseInvoiceByIdNew(String purchase_id) {

        try {
            System.out.println("purchase_idtt " + purchase_id);
            Map<String, String> map = new HashMap<>();
            map.put("purchase_order_id", purchase_id);
            String formData = Globals.mapToStringforFormData(map);
            HttpResponse<String> response = APIClient.postFormDataRequest(formData, "get_po_invoices_with_ids");
            JsonObject resp = new Gson().fromJson(response.body(), JsonObject.class);
            System.out.println("PO TO PI RESPONSE " + resp);
            PurOrderToInvoiceDTO purchaseEditResponseDTO = new Gson().fromJson(response.body(), PurOrderToInvoiceDTO.class);
            if (purchaseEditResponseDTO.getResponseStatus() == 200) {
                setEditResponse(purchaseEditResponseDTO);
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setEditResponse(PurOrderToInvoiceDTO purchaseEditResponseDTO) {

        Double totalFinalQty = 0.0;
        Double totalFinalFreeQty = 0.0;
//        System.out.println("inside setEditResponseee "+purchaseEditResponseDTO.getRow().size()+" getTotlQty "+ purchaseEditResponseDTO.getRow().get(0).getQty());
        for (int i = 0; i < purchaseEditResponseDTO.getRow().size(); i++) {
            totalFinalQty += purchaseEditResponseDTO.getRow().get(i).getQty();
            totalFinalFreeQty += purchaseEditResponseDTO.getRow().get(i).getFreeQty() != null ? purchaseEditResponseDTO.getRow().get(i).getFreeQty() : 0.0;
        }
        lblPoToPiTotalQtyEdit.setText(String.valueOf(totalFinalQty));
        lblPoToPiFreeQtyEdit.setText(String.valueOf(totalFinalFreeQty));
//        System.out.println("purchaseEditResponseDTO setEditResponse"+purchaseEditResponseDTO);
        edit_response_map.put("narration", purchaseEditResponseDTO.getInvoiceData().getNarration());
        edit_response_map.put("discountLedgerId", purchaseEditResponseDTO.getDiscountLedgerId());
        edit_response_map.put("discountInAmt", purchaseEditResponseDTO.getDiscountInAmt());
        edit_response_map.put("discountInPer", purchaseEditResponseDTO.getDiscountInPer());
        edit_response_map.put("totalPurchaseDiscountAmt", purchaseEditResponseDTO.getTotalPurchaseDiscountAmt());
        edit_response_map.put("totalQty", purchaseEditResponseDTO.getRow().get(0).getQty());
        edit_response_map.put("totalFreeQty", purchaseEditResponseDTO.getRow().get(0).getFreeQty());
        edit_response_map.put("grossTotal", purchaseEditResponseDTO.getRow().get(0).getGrossAmt());
        edit_response_map.put("totalTax", purchaseEditResponseDTO.getRow().get(0).getTotalIgst());
        edit_response_map.put("is_batch", purchaseEditResponseDTO.getRow().get(0).getIsBatch());
        edit_response_map.put("reference_type", purchaseEditResponseDTO.getRow().get(0).getReferenceType());
        edit_response_map.put("reference_id", purchaseEditResponseDTO.getRow().get(0).getReferenceId());
//        System.out.println("refTypeee " + purchaseEditResponseDTO.getRow().get(0).getReferenceType() + "  refIddd " + purchaseEditResponseDTO.getRow().get(0).getReferenceId());
        referenceType = purchaseEditResponseDTO.getRow().get(0).getReferenceType();
        referenceId = String.valueOf(purchaseEditResponseDTO.getRow().get(0).getReferenceId());
//        System.out.println("row Data => " + purchaseEditResponseDTO.getRow());
        rowListDTOS.clear();
//        for (PurchaseOrderToInvoiceRowListDTO order : purchaseEditResponseDTO.getRow()) {
//            order.setExpiryDate(DateConvertUtil.convertDispDateFormat(order.getExpiryDate()));
////            order.setPurchaseRate();
//            rowListDTOS.add(order);       //setting the row level data...
//        }
        rowListDTOS.addAll(purchaseEditResponseDTO.getRow());

        String product_id = String.valueOf(rowListDTOS.get(0).getProductId());
        getSupplierListbyProductId(product_id);
        fetchSelectedProductData(product_id);

//        System.out.println(" set product_id------" + product_id);
        edit_response_map.put("invoiceData", purchaseEditResponseDTO.getInvoiceData());
        InvoiceDataPurToInvDTO invoiceDataResDTO = purchaseEditResponseDTO.getInvoiceData();

        String Ledger_id = String.valueOf(invoiceDataResDTO.getSupplierId());
        trackingNumber = invoiceDataResDTO.getTransactionTrackingNo();
//        System.out.println(" set Ledger_id------" + ledger_id);
        fetchSelectedLedgerData(Ledger_id);
        invoice_data_map.put("id", String.valueOf(invoiceDataResDTO.getId()));
        invoice_data_map.put("invoice_dt", String.valueOf(invoiceDataResDTO.getInvoiceDt()));
        invoice_data_map.put("invoice_no", String.valueOf(invoiceDataResDTO.getPoSrNo()));
        invoice_data_map.put("narration", String.valueOf(invoiceDataResDTO.getNarration()));
//        invoice_data_map.put("tranx_unique_code",String.valueOf(invoiceDataResDTO.getTranxUniqueCode()));
//        invoice_data_map.put("purchase_sr_no",String.valueOf(invoiceDataResDTO.getPurchaseSrNo()));
        invoice_data_map.put("purchase_account_ledger_id", String.valueOf(invoiceDataResDTO.getPurchaseId()));
        invoice_data_map.put("supplierId", String.valueOf(invoiceDataResDTO.getSupplierId()));
        ledger_id = invoice_data_map.get("supplierId");
        invoice_data_map.put("transaction_dt", String.valueOf(invoiceDataResDTO.getPiTransactionDt()));
//        invoice_data_map.put("additional_charges_total",String.valueOf(invoiceDataResDTO.getAdditionalChargesTotal()));
        invoice_data_map.put("gstNo", String.valueOf(invoiceDataResDTO.getGstNo()));
//        invoice_data_map.put("reference_type",invoiceDataResDTO.getReference());    //refType and refId is comes in row DTO (PurchaseOrderToInvoiceRowListDTO) not in invoice data
//        invoice_data_map.put("reference_id",invoiceDataResDTO.getReferenceId());
//        invoice_data_map.put("isRoundOffCheck",String.valueOf(invoiceDataResDTO.getIsRoundOffCheck()));   //
//        invoice_data_map.put("roundoff",String.valueOf(invoiceDataResDTO.getRoundoff()));
        chbRounfOff.setSelected(true);
//        edit_response_map.put("bills",purchaseEditResponseDTO.getBills());
//        edit_response_map.put("barcodeList",purchaseEditResponseDTO.getBarcodeList());
        edit_response_map.put("additionalCharges", purchaseEditResponseDTO.getAdditionalCharges());

    }

    //function for setting the additional charges data
    private void setAddChargesInTable() {
        for (AddChargesListDTO addChgLst : addChargesResLst) {
            CustomAddChargesDTO customAddChargesDTO = new CustomAddChargesDTO();
            customAddChargesDTO.setAdditional_charges_details_id(String.valueOf(addChgLst.getAdditionalChargesDetailsId()));
            customAddChargesDTO.setLedgerId(String.valueOf(addChgLst.getLedgerId()));
            customAddChargesDTO.setAmt(String.valueOf(addChgLst.getAmt()));
            customAddChargesDTO.setPercent(String.valueOf(addChgLst.getPercent()));
            addChargesDTOList.add(customAddChargesDTO);
        }
    }

    public void sceneInitilization() {
        purScrollPane.sceneProperty().addListener((observable, oldScene, newScene) -> {
            if (newScene != null && newScene.getWindow() instanceof Stage) {
                Communicator.stage = (Stage) newScene.getWindow();
            }
        });
    }

    //    public void handlePayAction(ActionEvent actionEvent) {
//        RadioButton selectedRadioButton = (RadioButton) toggleGroup.getSelectedToggle();
//        if (selectedRadioButton != null) {
//            selectedRadioValue = selectedRadioButton.getText();
//        }
//    }
    private void validatePurchaseInvoiceNo() {
        try {
            Map<String, String> map = new HashMap<>();
            map.put("supplier_id", ledger_id);
            map.put("bill_no", tfInvoiceNo.getText());

            String formData = Globals.mapToStringforFormData(map);
            HttpResponse<String> response = APIClient.postFormDataRequest(formData, EndPoints.VALIDATE_PUR_INV_NUMBER);
            String resBody = response.body();
            JsonObject obj = new Gson().fromJson(resBody, JsonObject.class);
            if (obj.get("responseStatus").getAsInt() == 409) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(obj.get("message").getAsString());

                alert.showAndWait();
                alert.show();
                tfInvoiceNo.setText("");
                tfInvoiceNo.requestFocus();
            }
        } catch (Exception e) {
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            String exceptionAsString = sw.toString();
            purchOrderToInvLogger.error("Exception in Pur Return validatePurchaseInvoice():" + exceptionAsString);
        }

    }

    private void nodetraversal(Node current_node, Node next_node) {
        current_node.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                next_node.requestFocus();
                event.consume();
            } else if (event.getCode() == KeyCode.SPACE) {
                if (current_node instanceof TextField textField) {
                    if (textField.getId().equals("tfLedgerName")) {
//                        handleTfLedgerName();
                    }
                }

                if (current_node instanceof RadioButton radioButton) {
                    radioButton.setSelected(!radioButton.isSelected());
                    radioButton.fire();
                }
            }
        });
    }

    public void getPurchaseAc() {

        try {

            HttpResponse<String> response = APIClient.getRequest("get_purchase_accounts");
            PurchaseAcRes purchaseAcRes = new Gson().fromJson(response.body(), PurchaseAcRes.class);

            if (purchaseAcRes.getResponseStatus() == 200) {

                List<PurchaseAcList> purchaseAcResLists = purchaseAcRes.getList();

                for (PurchaseAcList purchaseAcList1 : purchaseAcResLists) {
                    purchaseAcList.add(new CommonDTO(purchaseAcList1.getName(), purchaseAcList1.getId().toString()));
                }
                cbPurchaseAc.setItems(purchaseAcList);
                AutoCompleteBox<CommonDTO> autoCompleteBox = new AutoCompleteBox<>(cbPurchaseAc, -1);

            } else {
                System.out.println("responseObject is null");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void getPurchaseSerial() {

        try {

            HttpResponse<String> response = APIClient.getRequest("get_last_invoice_record");
            PurchaseSerialRes purchaseSerialRes = new Gson().fromJson(response.body(), PurchaseSerialRes.class);

            if (purchaseSerialRes.getResponseStatus() == 200) {
                tfPurchaseSerial.setText(String.valueOf(purchaseSerialRes.getCount()));
            } else {
                System.out.println("responseObject is null");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void setCbPurchaseAc(ActionEvent actionEvent) {
        Object[] object = new Object[1];
        object[0] = cbPurchaseAc.getSelectionModel().getSelectedItem();
        if (object[0] != null) {
            for (CommonDTO commonDTO : purchaseAcList) {
                if (object[0].equals(commonDTO)) {
                    purchaseAcID = commonDTO.getId();
                }
            }
        }
        nodetraversal(cbSupplierGSTIN, tfInvoiceNo);
    }

    TableCellCallback<Object[]> callback = item -> {

        System.out.println("item0: " + item[0]);
        System.out.println("item1: " + item[1]);
        System.out.println("item2: " + item[2]);
        System.out.println("item3: " + item[3]);
        System.out.println("item4: " + item[4]);
        System.out.println("item5: " + item[5]);
        System.out.println("item6: " + item[6]);
        System.out.println("item7: " + item[7]);
        System.out.println("item8: " + item[8]);

        System.out.println("taxCalculation item9 " + item[9]);

        tvGST_Table.getItems().clear();
        ObservableList<GstDTO> gstDTOObservableList = FXCollections.observableArrayList();
        ObservableList<?> item9List = (ObservableList<?>) item[9];
        for (Object obj : item9List) {
            if (obj instanceof GstDTO) {
                gstDTOObservableList.add((GstDTO) obj);
            }
        }

        tvGST_Table.getItems().addAll(gstDTOObservableList);

        if (!ledgerStateCode.equalsIgnoreCase(GlobalTranx.getCompanyStateCode().toString())) {
            tc_igst.setVisible(true);
            tc_sgst.setVisible(false);
            tc_cgst.setVisible(false);
        } else {
            tc_igst.setVisible(false);
            tc_sgst.setVisible(true);
            tc_cgst.setVisible(true);
        }
        JsonArray jsonArray = new JsonArray();
        JsonObject jsonResult = new JsonObject();
        if (ledgerStateCode.equalsIgnoreCase(GlobalTranx.getCompanyStateCode().toString())) {
            for (GstDTO gstDTO : gstDTOObservableList) {
                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("d_gst", decimalFormat.format(Double.parseDouble(gstDTO.getTaxPer())));
                jsonObject.addProperty("gst", decimalFormat.format(Double.parseDouble(gstDTO.getTaxPer()) / 2));
                jsonObject.addProperty("amt", gstDTO.getCgst());
                jsonArray.add(jsonObject);
            }
            jsonResult.add("cgst", jsonArray);
            jsonResult.add("sgst", jsonArray);
        } else {
            for (GstDTO gstDTO : gstDTOObservableList) {
                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("d_gst", decimalFormat.format(Double.parseDouble(gstDTO.getTaxPer())));
                jsonObject.addProperty("gst", decimalFormat.format(Double.parseDouble(gstDTO.getTaxPer())));
                jsonObject.addProperty("amt", gstDTO.getCgst());
                jsonArray.add(jsonObject);
            }
            jsonResult.add("igst", jsonArray);

        }
        purchase_invoice_map.put("taxCalculation", jsonResult.toString());

        lbBillAmount.setText((String) item[2]); //totalNetAmt for bill amount
        totalamt = (String) item[2];

        total_purchase_discount_amt = (String) item[4];

        if (ledgerStateCode.equalsIgnoreCase(GlobalTranx.getCompanyStateCode().toString())) {
            taxFlag = true;
        }

        purchase_invoice_map.put("total_qty", (String) item[0]);
        purchase_invoice_map.put("total_free_qty", (String) item[1]);

        purchase_invoice_map.put("total_base_amt", (String) item[3]);
        purchase_invoice_map.put("total_invoice_dis_amt", (String) item[4]);
        purchase_invoice_map.put("taxable_amount", (String) item[5]);
        purchase_invoice_map.put("bill_amount", (String) item[2]);
        purchase_invoice_map.put("total_tax_amt", (String) item[6]);

        purchase_invoice_map.put("totaligst", (String) item[6]);
        purchase_invoice_map.put("totalsgst", (String) item[7]);
        purchase_invoice_map.put("totalcgst", (String) item[8]);


        purchase_invoice_map.put("taxFlag", String.valueOf(taxFlag));

        total_taxable_amt = Double.parseDouble((String) item[5]);


        lbGrossTotal.setText((String) item[3]);
        lbDiscount.setText((String) item[4]);
        lbTotal.setText((String) item[5]);
        lbTax.setText((String) item[6]);

        roundOffCalculations();
    };
    TableCellCallback<Integer> productCall = item -> {
        System.out.println("product id " + item);
        fetchSelectedProductData(String.valueOf(item));
        getSupplierListbyProductId(String.valueOf(item));
    };

    //function for rounf off calculation
    private void roundOffCalculations() {
        Double billamt = 0.00; // local variable to store row wise  Bill amount
        Double total_bill_amt = 0.00; // local variable to store Final bill Amount without roundoff
        Double final_r_off_bill_amt = 0.00; // local variable to store Final Bill amount with round off
        Double roundOffAmt = 0.00; // local variable to store Roundoff Amount
        for (PurchaseInvoiceTable cmpTRowDTO : tvPurchaseInvoiceTable.getItems()) {
            billamt = Double.valueOf(cmpTRowDTO.getNet_amount());
            total_bill_amt += billamt;
        }
        final_r_off_bill_amt = (double) Math.round(total_bill_amt);
        roundOffAmt = final_r_off_bill_amt - total_bill_amt;

        if (chbRounfOff.isSelected()) {
            lbBillAmount.setText(String.format("%.2f", final_r_off_bill_amt));
            lblPoToPiRoundOffEdit.setText(String.format("%.2f", roundOffAmt));
        } else {
            lbBillAmount.setText(String.format("%.2f", total_bill_amt));
            lblPoToPiRoundOffEdit.setText("0.00");
        }

    }

    private void responsiveCMPTRow() {
        tcSrNo.prefWidthProperty().bind(tvPurchaseInvoiceTable.widthProperty().multiply(0.05));
        tcParticulars.prefWidthProperty().bind(tvPurchaseInvoiceTable.widthProperty().multiply(0.2));
//        tcLevelA.prefWidthProperty().bind(tvPurchaseInvoiceTable.widthProperty().multiply(0.0));
//        tcLevelB.prefWidthProperty().bind(tvPurchaseInvoiceTable.widthProperty().multiply(0.0));
//        tcLevelC.prefWidthProperty().bind(tvPurchaseInvoiceTable.widthProperty().multiply(0.0));
        tcPackage.prefWidthProperty().bind(tvPurchaseInvoiceTable.widthProperty().multiply(0.05));
        tcUnit.prefWidthProperty().bind(tvPurchaseInvoiceTable.widthProperty().multiply(0.05));
        tcBatch.prefWidthProperty().bind(tvPurchaseInvoiceTable.widthProperty().multiply(0.1));
        tcQuantity.prefWidthProperty().bind(tvPurchaseInvoiceTable.widthProperty().multiply(0.05));
        tcRate.prefWidthProperty().bind(tvPurchaseInvoiceTable.widthProperty().multiply(0.1));
        tcGrossAmount.prefWidthProperty().bind(tvPurchaseInvoiceTable.widthProperty().multiply(0.1));
        tcDisPer.prefWidthProperty().bind(tvPurchaseInvoiceTable.widthProperty().multiply(0.05));
        tcDisAmt.prefWidthProperty().bind(tvPurchaseInvoiceTable.widthProperty().multiply(0.05));
        tcTax.prefWidthProperty().bind(tvPurchaseInvoiceTable.widthProperty().multiply(0.05));
        tcNetAmount.prefWidthProperty().bind(tvPurchaseInvoiceTable.widthProperty().multiply(0.1));
        tcAction.prefWidthProperty().bind(tvPurchaseInvoiceTable.widthProperty().multiply(0.05));
    }

    public void tableInitiliazation() {

        tvPurchaseInvoiceTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tvPurchaseInvoiceTable.setEditable(true);
        tvPurchaseInvoiceTable.setFocusTraversable(false);

        Label headerLabel = new Label("Sr\nNo.");
        tcSrNo.setGraphic(headerLabel);


        int count = 1;
        int index = 0;
        for (PurchaseOrderToInvoiceRowListDTO rowListDTO : rowListDTOS) {
            System.out.println("rowListDTO -> " + rowListDTO);
            PurchaseInvoiceTable purchaseInvoiceTable = new PurchaseInvoiceTable();
//                purchaseInvoiceTable.setPurchase_id(String.valueOf(purchase_id));    //-1
            purchaseInvoiceTable.setSr_no(String.valueOf(count));
            purchaseInvoiceTable.setDetails_id(String.valueOf(rowListDTO.getDetailsId()));
            purchaseInvoiceTable.setProduct_id(String.valueOf(rowListDTO.getProductId()));
            purchaseInvoiceTable.setParticulars(String.valueOf(rowListDTO.getProductName()));
            purchaseInvoiceTable.setLevelA_id(String.valueOf(rowListDTO.getLevelAId()));
            purchaseInvoiceTable.setLevelB_id(String.valueOf(rowListDTO.getLevelBId()));
            purchaseInvoiceTable.setLevelC_id(String.valueOf(rowListDTO.getLevelCId()));
            purchaseInvoiceTable.setUnit_id(String.valueOf(rowListDTO.getUnitId()));
            purchaseInvoiceTable.setUnit_conv(String.valueOf(rowListDTO.getUnitConv()));
            purchaseInvoiceTable.setPackages(String.valueOf(rowListDTO.getPackName()));
            purchaseInvoiceTable.setQuantity(String.valueOf(rowListDTO.getQty()));
            purchaseInvoiceTable.setRate(String.valueOf(rowListDTO.getRate()));
            purchaseInvoiceTable.setBase_amt(String.valueOf(rowListDTO.getBaseAmt()));
            purchaseInvoiceTable.setDis_amt(String.valueOf(rowListDTO.getDisAmt()));
            purchaseInvoiceTable.setDis_per(String.valueOf(rowListDTO.getDisPer()));
            purchaseInvoiceTable.setDis_per_cal(String.valueOf(rowListDTO.getDisPerCal()));
            purchaseInvoiceTable.setDis_amt_cal(String.valueOf(rowListDTO.getDisAmtCal()));
            purchaseInvoiceTable.setTotal_amt(String.valueOf(rowListDTO.getTotalAmt()));
            purchaseInvoiceTable.setGst(String.valueOf(rowListDTO.getGst()));
            purchaseInvoiceTable.setIgst_per(String.valueOf(rowListDTO.getIgst()));
            purchaseInvoiceTable.setSgst_per(String.valueOf(rowListDTO.getSgst()));
            purchaseInvoiceTable.setCgst_per(String.valueOf(rowListDTO.getCgst()));
            purchaseInvoiceTable.setIgst(String.valueOf(rowListDTO.getIgst()));
            purchaseInvoiceTable.setCgst(String.valueOf(rowListDTO.getCgst()));
            purchaseInvoiceTable.setSgst(String.valueOf(rowListDTO.getSgst()));
            purchaseInvoiceTable.setNet_amount(String.valueOf(rowListDTO.getFinalAmt()));
            purchaseInvoiceTable.setFree(rowListDTO.getFreeQty() != null ? String.valueOf(rowListDTO.getFreeQty()) : "0");
            purchaseInvoiceTable.setRow_dis_amt(String.valueOf(rowListDTO.getRowDisAmt()));
            purchaseInvoiceTable.setGross_amount(String.valueOf(rowListDTO.getGrossAmt()));
            purchaseInvoiceTable.setGross_amount1(String.valueOf(rowListDTO.getGrossAmt()));
            purchaseInvoiceTable.setReference_type(rowListDTO.getReferenceType());
            purchaseInvoiceTable.setReference_id(rowListDTO.getReferenceId().toString());
//                purchaseInvoiceTable.setAdd_chg_amt(String.valueOf(rowListDTO.g);
            purchaseInvoiceTable.setInvoice_dis_amt(String.valueOf(rowListDTO.getInvoiceDisAmt()));
//                purchaseInvoiceTable.setInventoryId(String.valueOf(rowListDTO.()));
//                purchaseInvoiceTable.setTransaction_status(String.valueOf(rowListDTO.get()));
//                purchaseInvoiceTable.setIs_expired(String.valueOf(rowListDTO.getIsExpired()));
            purchaseInvoiceTable.setB_details_id(String.valueOf(rowListDTO.getbDetailsId()));   //
            System.out.println("getBatchNooo " + rowListDTO.getBatchNo());
            purchaseInvoiceTable.setBatch_or_serial(String.valueOf(rowListDTO.getBatchNo()));
            purchaseInvoiceTable.setB_expiry(String.valueOf(rowListDTO.getExpiryDate()));
            purchaseInvoiceTable.setB_rate(String.valueOf(rowListDTO.getRate()));
            purchaseInvoiceTable.setB_purchase_rate(rowListDTO.getPurchaseRate() + "");
//                purchaseInvoiceTable.setB_purchase_rate(String.valueOf(rowListDTO.()));
            purchaseInvoiceTable.setIs_batch(String.valueOf(rowListDTO.getIsBatch()));

            purchaseInvoiceTable.setRate_a(String.valueOf(rowListDTO.getMinRateA() == null ? "" : rowListDTO.getMinRateA()));
            purchaseInvoiceTable.setRate_b(String.valueOf(rowListDTO.getMinRateB() == null ? "" : rowListDTO.getMinRateB()));
            purchaseInvoiceTable.setRate_c(String.valueOf(rowListDTO.getMinRateC() == null ? "" : rowListDTO.getMinRateB()));
            // purchaseInvoiceTable.setRate_d(String.valueOf(rowListDTO.getMinRateD()));

//                purchaseInvoiceTable.setManufacturing_date(String.valueOf(rowListDTO.getManufacturingDate()));
//                purchaseInvoiceTable.setMin_margin(String.valueOf(rowListDTO.getMarginPer()));

//                purchaseInvoiceTable.setCosting(String.valueOf(rowListDTO.getCosting()));
            purchaseInvoiceTable.setLedger_id(invoice_data_map.get("supplierId"));
            purchaseInvoiceTable.setTax(String.valueOf(rowListDTO.getGst()));

            BatchWindowTableDTO batchWindowTableDTO = new BatchWindowTableDTO();

            batchWindowTableDTO.setProductId(purchaseInvoiceTable.getProduct_id());
            batchWindowTableDTO.setLevelAId(purchaseInvoiceTable.getLevelA_id());
            batchWindowTableDTO.setLevelBId(purchaseInvoiceTable.getLevelB_id());
            batchWindowTableDTO.setLevelCId(purchaseInvoiceTable.getLevelC_id());
            batchWindowTableDTO.setUnitId(purchaseInvoiceTable.getUnit_id());
            System.out.println("expDateee " + purchaseInvoiceTable.getB_expiry() + " purrate- " + purchaseInvoiceTable.getB_purchase_rate());
            batchWindowTableDTO.setExpiryDate(purchaseInvoiceTable.getB_expiry());
            batchWindowTableDTO.setPurchaseRate(purchaseInvoiceTable.getB_purchase_rate());

            batchWindowTableDTO.setBatchNo(purchaseInvoiceTable.getBatch_or_serial());
            batchWindowTableDTO.setB_details_id(purchaseInvoiceTable.getB_details_id());
            System.out.println("purchaseInvoiceTable.getManufacturing_date() " + purchaseInvoiceTable.getManufacturing_date());
            if (purchaseInvoiceTable.getManufacturing_date().equalsIgnoreCase("null") && !purchaseInvoiceTable.getManufacturing_date().equalsIgnoreCase("")) {
                LocalDate manu_dt = LocalDate.parse(purchaseInvoiceTable.getManufacturing_date());
                batchWindowTableDTO.setManufacturingDate(manu_dt.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
            }
            if (!purchaseInvoiceTable.getB_expiry().equalsIgnoreCase("null") && !purchaseInvoiceTable.getB_expiry().equalsIgnoreCase("")) {
                LocalDate exp_dt = LocalDate.parse(purchaseInvoiceTable.getB_expiry());
                batchWindowTableDTO.setExpiryDate(exp_dt.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
            }

            batchWindowTableDTO.setMrp(rowListDTO.getMrp() + "");
            batchWindowTableDTO.setPurchaseRate(purchaseInvoiceTable.getB_purchase_rate());

            System.out.println("purchaseInvoiceTable.getQuantity()= " + purchaseInvoiceTable.getQuantity());
            batchWindowTableDTO.setQuantity(purchaseInvoiceTable.getQuantity());
            batchWindowTableDTO.setFree(purchaseInvoiceTable.getFree());

            batchWindowTableDTO.setDiscountPercentage(purchaseInvoiceTable.getDis_per());
            batchWindowTableDTO.setDiscountAmount(purchaseInvoiceTable.getDis_amt());

//                batchWindowTableDTO.setBarcode(purchaseInvoiceTable.getBarcode());   //to be uncomment
            batchWindowTableDTO.setMargin(purchaseInvoiceTable.getMin_margin());


            batchWindowTableDTO.setFsr_mh(purchaseInvoiceTable.getRate_a());
            batchWindowTableDTO.setFsr_ai(purchaseInvoiceTable.getRate_b());
            batchWindowTableDTO.setCsr_mh(purchaseInvoiceTable.getRate_c());
            batchWindowTableDTO.setCsr_ai(0.0 + "");
//
            batchWindowTableDTO.setSupplier_id(purchaseInvoiceTable.getLedger_id());
            batchWindowTableDTO.setTax(purchaseInvoiceTable.getTax());

            List<BatchWindowTableDTO> listBatch = new ArrayList<>();
            listBatch.add(batchWindowTableDTO);

            purchaseInvoiceTable.setBatchWindowTableDTOList(listBatch);

            tvPurchaseInvoiceTable.getItems().addAll(purchaseInvoiceTable);
            PurchaseInvoiceCalculationForPurOrderToInvoice.rowCalculationForPurcInvoice(index, tvPurchaseInvoiceTable, callback);

            List<LevelAForPurInvoice> levelAForPurInvoiceList = ProductUnitsPackingForPurOrderToInvoice.getAllProductUnitsPackingFlavour(purchaseInvoiceTable.getProduct_id());

            ObservableList<LevelAForPurInvoice> observableLevelAList = FXCollections.observableArrayList(levelAForPurInvoiceList);

            if (index >= 0 && index < PurInvoiceCommunicator.levelAForPurInvoiceObservableList.size()) {
                PurInvoiceCommunicator.levelAForPurInvoiceObservableList.set(index, observableLevelAList);

                tvPurchaseInvoiceTable.getItems().get(index).setLevelA(null);
                tvPurchaseInvoiceTable.getItems().get(index).setLevelA(levelAForPurInvoiceList.get(0).getLabel());

                for (LevelAForPurInvoice levelAForPurInvoice : PurInvoiceCommunicator.levelAForPurInvoiceObservableList.get(index)) {
                    if (tvPurchaseInvoiceTable.getItems().get(index).getLevelA_id().equals(levelAForPurInvoice.getValue())) {
                        tvPurchaseInvoiceTable.getItems().get(index).setLevelA(null);
                        tvPurchaseInvoiceTable.getItems().get(index).setLevelA(levelAForPurInvoice.getLabel());
                    }
                }

            } else {
                PurInvoiceCommunicator.levelAForPurInvoiceObservableList.add(observableLevelAList);

                tvPurchaseInvoiceTable.getItems().get(index).setLevelA(null);
                tvPurchaseInvoiceTable.getItems().get(index).setLevelA(levelAForPurInvoiceList.get(0).getLabel());

                for (LevelAForPurInvoice levelAForPurInvoice : PurInvoiceCommunicator.levelAForPurInvoiceObservableList.get(index)) {
                    if (tvPurchaseInvoiceTable.getItems().get(index).getLevelA_id().equals(levelAForPurInvoice.getValue())) {
                        tvPurchaseInvoiceTable.getItems().get(index).setLevelA(null);
                        tvPurchaseInvoiceTable.getItems().get(index).setLevelA(levelAForPurInvoice.getLabel());
                    }
                }
            }
            count++;
            index++;
        }
//            setAddChargesInTable();
//            tvPurchaseInvoiceTable.getItems().addAll(new PurchaseInvoiceTable("","0","1","","","","","","","","","","","","","","",""));

//        tcSrNo.setCellValueFactory(cellData -> cellData.getValue().sr_noProperty());
        tcSrNo.setCellFactory(new LineNumbersCellFactory());
        tcSrNo.setStyle("-fx-alignment: CENTER;");

        tcPackage.setCellValueFactory(cellData -> cellData.getValue().packagesProperty());
        tcPackage.setStyle("-fx-alignment: CENTER;");

        tcLevelB.setCellValueFactory(cellData -> cellData.getValue().levelBProperty());
        tcLevelB.setCellFactory(column -> new ComboBoxTableCellForLevelBForPurOrderToInvoice("tcLevelB"));

        tcLevelA.setCellValueFactory(cellData -> cellData.getValue().levelAProperty());
        tcLevelA.setCellFactory(column -> new ComboBoxTableCellForLevelAForPurOrderToInvoice("tcLevelA"));

        tcLevelC.setCellValueFactory(cellData -> cellData.getValue().levelCProperty());
        tcLevelC.setCellFactory(column -> new ComboBoxTableCellForLevelCForPurOrderToInvoice("tcLevelC"));

        tcUnit.setCellValueFactory(cellData -> cellData.getValue().unitProperty());
        tcUnit.setCellFactory(column -> new ComboBoxTableCellForUnitForPurInvoiceForPurOrderToInvoice("tcUnit"));

        tcParticulars.setCellValueFactory(cellData -> cellData.getValue().particularsProperty());
        tcParticulars.setCellFactory(column -> new TextFieldTableCellForPurInvoiceForPurOrderToInvoice("tcParticulars", callback, productCall, tfNarration));
//        rbCash.setOnKeyPressed(event -> {
//            tvPurchaseInvoiceTable.edit(0, tcParticulars);
//        });


        tcBatch.setCellValueFactory(cellData -> cellData.getValue().batch_or_serialProperty());
        tcBatch.setCellFactory(column -> new TextFieldTableCellForPurInvoiceForPurOrderToInvoice("tcBatch", callback));


        tcQuantity.setCellValueFactory(cellData -> cellData.getValue().quantityProperty());
        tcQuantity.setCellFactory(column -> new TextFieldTableCellForPurInvoiceForPurOrderToInvoice("tcQuantity", callback));


        tcFreeQty.setCellValueFactory(cellData -> cellData.getValue().freeProperty());
        tcFreeQty.setCellFactory(column -> new TextFieldTableCellForPurInvoiceForPurOrderToInvoice("tcFreeQty", callback));

        tcRate.setCellValueFactory(cellData -> cellData.getValue().rateProperty());
        tcRate.setCellFactory(column -> new TextFieldTableCellForPurInvoiceForPurOrderToInvoice("tcRate", callback));

        tcGrossAmount.setCellValueFactory(cellData -> cellData.getValue().gross_amountProperty());
        tcGrossAmount.setCellFactory(column -> new TextFieldTableCellForPurInvoiceForPurOrderToInvoice("tcGrossAmount", callback));

        tcDisPer.setCellValueFactory(cellData -> cellData.getValue().dis_perProperty());
        tcDisPer.setCellFactory(column -> new TextFieldTableCellForPurInvoiceForPurOrderToInvoice("tcDisPer", callback));

        tcDisPer.setCellValueFactory(cellData -> cellData.getValue().dis_perProperty());
        tcDisPer.setCellFactory(column -> new TextFieldTableCellForPurInvoiceForPurOrderToInvoice("tcDisPer", callback));

        tcDisAmt.setCellValueFactory(cellData -> cellData.getValue().dis_amtProperty());
        tcDisAmt.setCellFactory(column -> new TextFieldTableCellForPurInvoiceForPurOrderToInvoice("tcDisAmt", callback));

        tcTax.setCellValueFactory(cellData -> cellData.getValue().taxProperty());
        tcTax.setCellFactory(column -> new TextFieldTableCellForPurInvoiceForPurOrderToInvoice("tcTax", callback));

        tcNetAmount.setCellValueFactory(cellData -> cellData.getValue().net_amountProperty());
        tcNetAmount.setCellFactory(column -> new TextFieldTableCellForPurInvoiceForPurOrderToInvoice("tcNetAmount", callback));

//        tcAction.setCellValueFactory(cellData -> cellData.getValue().net_amountProperty());
        tcAction.setCellFactory(column -> new ButtonTableCellForPurOrderToInvoice());


    }

    private void columnVisibility(TableColumn<PurchaseInvoiceTable, String> column, boolean visible) {
        if (visible) {
            column.setPrefWidth(USE_COMPUTED_SIZE);
            column.setMinWidth(USE_PREF_SIZE);
            column.setMaxWidth(Double.MAX_VALUE);
        } else {
            column.setPrefWidth(0);
            column.setMinWidth(0);
            column.setMaxWidth(0);
        }
    }


    public void setCbSupplierGSTN(ActionEvent actionEvent) {
        Object[] object = new Object[1];
        object[0] = cbSupplierGSTIN.getSelectionModel().getSelectedItem();
        if (object[0] != null) {
            for (CommonDTO commonDTO : supplierGSTINList) {
                if (object[0].equals(commonDTO)) {
                    supplierGSTIN_ID = commonDTO.getId();
                    gst_no = commonDTO.getText();
                }
            }
        }
    }


    public void onClickAdditionalCharges(ActionEvent actionEvent) {
        Stage stage = (Stage) purScrollPane.getScene().getWindow();

        AdditionalCharges.additionalCharges(stage, addChargesDTOList, total_taxable_amt, (input1, input2) -> {
            addChargesDTOList.clear();
            addChargesDTOList.addAll(input2);
            tfAddCharges.setText(input1[1]);
        });
    }


    public void onClickSubmit(ActionEvent actionEvent) {
        AlertUtility.AlertConfirmation("Confirm", LedgerMessageConsts.msgConfirmationOnSubmit + tfInvoiceNo.getText() + LedgerMessageConsts.msgInvoiceNumber, callback -> {
            if (callback == 1) {
                createPurchaseInvoice();
            }
        });

    }

    public void createPurchaseInvoice() {

        Object idValue = invoice_data_map.get("id");
        if (idValue != null) {
            purchase_invoice_map.put("id", idValue.toString());
        }

        purchase_invoice_map.put("invoice_date", Communicator.text_to_date.fromString(tfInvoiceDate.getText()).toString());

        purchase_invoice_map.put("transaction_date", Communicator.text_to_date.fromString(tfTranxDate.getText()).toString());

        purchase_invoice_map.put("newReference", "false");

        purchase_invoice_map.put("invoice_no", tfInvoiceNo.getText());

        purchase_invoice_map.put("purchase_id", purchaseAcID);

        purchase_invoice_map.put("purchase_sr_no", tfPurchaseSerial.getText());

        purchase_invoice_map.put("supplier_code_id", ledger_id);
        purchase_invoice_map.put("reference_type", "PRSORD");
        System.out.println("orderToInvoice value refIdd " + orderToInvoice);
        purchase_invoice_map.put("reference_id", orderToInvoice);
        purchase_invoice_map.put("transactionTrackingNo", trackingNumber);


        System.out.println("roundOffCheckkk " + chbRounfOff.isSelected() + "  roundOffValue-- " + lblPoToPiRoundOffEdit.getText());
        if (chbRounfOff.isSelected() == true) {
            purchase_invoice_map.put("isRoundOffCheck", "true");
        } else {
            purchase_invoice_map.put("isRoundOffCheck", "false");
        }


        purchase_invoice_map.put("roundoff", lblPoToPiRoundOffEdit.getText());

        purchase_invoice_map.put("totalamt", totalamt);

        purchase_invoice_map.put("total_purchase_discount_amt", total_purchase_discount_amt);

        purchase_invoice_map.put("gstNo", gst_no);

        purchase_invoice_map.put("paymentMode", selectedPayMode);

        purchase_invoice_map.put("tcs_per", "0.00");

        purchase_invoice_map.put("tcs_amt", "0.00");

        purchase_invoice_map.put("tcs_mode", "");

        purchase_invoice_map.put("purchase_discount", purchase_discount.getText().isEmpty() || purchase_discount == null ? "0.0" : purchase_discount.getText());

        purchase_invoice_map.put("purchase_discount_amt", purchase_discount_amt.getText().isEmpty() || purchase_discount_amt == null ? "0.0" : purchase_discount_amt.getText());

        System.out.println("addChargesDTOList at createPurInv " + addChargesDTOList);
        purchase_invoice_map.put("additionalChargesTotal", "0.0");

        purchase_invoice_map.put("narration", tfNarration.getText());

        List<PurchaseInvoiceTable> list = tvPurchaseInvoiceTable.getItems();

        for (PurchaseInvoiceTable purchaseInvoiceTable : list) {
            if (!purchaseInvoiceTable.getTotal_taxable_amt().equals("0.0")) {
                purchaseInvoiceTable.setTotal_amt(purchaseInvoiceTable.getTotal_taxable_amt());
            } else {
                purchaseInvoiceTable.setTotal_amt(purchaseInvoiceTable.getTaxable_amt());
            }
        }
        Double total_row_gross_amt = 0.0;

        for (PurchaseInvoiceTable purchaseInvoiceTable : list) {
            if (purchaseInvoiceTable.getGross_amount().isEmpty()) {
                total_row_gross_amt = total_row_gross_amt + 0.0;
            } else {
                total_row_gross_amt = total_row_gross_amt + Double.parseDouble(purchaseInvoiceTable.getGross_amount());
            }
        }
//        list.stream().filter((v)->v.getProduct_id().isEmpty()).collect()        .collect(Collectors.toList());
        list = list.stream().filter(v -> !(v.getProduct_id().equalsIgnoreCase("0"))).collect(Collectors.toList());
//        System.out.println("final_list" + list);
        purchase_invoice_map.put("total_row_gross_amt", String.valueOf(total_row_gross_amt));
        purchase_invoice_map.put("row", list.toString());
//        System.out.println("purchase_invoice_map in createPurInv " + purchase_invoice_map);
        try {
//            String formdata = mapToStringforFormData(purchase_invoice_map);

            Map<String, String> headers = new HashMap<>();
            headers.put("branch", Globals.headerBranch);
            String response = null;
            response = APIClient.postMultipartRequest(purchase_invoice_map, null, "create_purchase_invoices", headers);
            JsonObject jsonObject = new Gson().fromJson(response, JsonObject.class);
            if (jsonObject.get("responseStatus").getAsInt() == 200) {
                AlertUtility.AlertSuccessTimeout(AlertUtility.alertTypeSuccess, jsonObject.get("message").getAsString(), input -> {
                    GlobalController.getInstance().addTabStatic(PURCHASE_ORDER_LIST_SLUG, false);
                });
            } else {
                AlertUtility.AlertErrorTimeout(AlertUtility.alertTypeError, jsonObject.get("message").getAsString(), in -> {
                    btnSubmit.requestFocus();
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
            purchOrderToInvLogger.error("Exception on createPurchaseInvoice()" + Globals.getExceptionString(e));
        }

    }

    public void purc_disc_per(KeyEvent keyEvent) {
        String discText = purchase_discount.getText();
        if (!TextUtils.isEmpty(discText)) {
            double totalTaxableAmt = 0.0;
            for (int i = 0; i < tvPurchaseInvoiceTable.getItems().size(); i++) {
                PurchaseInvoiceCalculationForPurOrderToInvoice.rowCalculationForPurcInvoice(i, tvPurchaseInvoiceTable, callback);
                PurchaseInvoiceTable purchaseInvoiceTable = tvPurchaseInvoiceTable.getItems().get(i);
                totalTaxableAmt = totalTaxableAmt + Double.parseDouble(purchaseInvoiceTable.getTaxable_amt());
            }
            double disc_per = Double.parseDouble(discText);
            Double amount = (totalTaxableAmt * disc_per) / 100;
            System.out.println("totalTaxableAmt in purc_disc_per " + totalTaxableAmt + " and amount= " + amount);
            purchase_discount_amt.setText(String.format("%.2f", amount));
            System.out.println("purchase_discount_amt.getText " + purchase_discount_amt.getText());
            PurchaseInvoiceCalculationForPurOrderToInvoice.discountPropotionalCalculation(disc_per + "", "0", tfAddCharges.getText(), tvPurchaseInvoiceTable, callback);
            if (!tfAddCharges.getText().isEmpty()) {
                System.out.println("Additioanal CHarges Called >> ");
                PurchaseInvoiceCalculationForPurOrderToInvoice.additionalChargesCalculation(tfAddCharges.getText(), tvPurchaseInvoiceTable, callback);
            }
        } else {
            purchase_discount_amt.setText("");
            PurchaseInvoiceCalculationForPurOrderToInvoice.discountPropotionalCalculation("0", "0", tfAddCharges.getText(), tvPurchaseInvoiceTable, callback);
            if (!tfAddCharges.getText().isEmpty()) {
                System.out.println("Additioanal CHarges Called >> ");
                PurchaseInvoiceCalculationForPurOrderToInvoice.additionalChargesCalculation(tfAddCharges.getText(), tvPurchaseInvoiceTable, callback);
            }

        }
        PurchaseInvoiceCalculationForPurOrderToInvoice.calculateGst(tvPurchaseInvoiceTable, callback);

    }

    public void purc_disc_amt(KeyEvent keyEvent) {
        String discAmtText = purchase_discount_amt.getText();
        if (!TextUtils.isEmpty(discAmtText)) {
            double totalTaxableAmt = 0.0;
            for (int i = 0; i < tvPurchaseInvoiceTable.getItems().size(); i++) {
                System.out.println("Gross amt in Prop--->" + tvPurchaseInvoiceTable.getItems().get(i).getGross_amount());
                PurchaseInvoiceCalculationForPurOrderToInvoice.rowCalculationForPurcInvoice(i, tvPurchaseInvoiceTable, callback);//call row calculation function
                PurchaseInvoiceTable purchaseInvoiceTable = tvPurchaseInvoiceTable.getItems().get(i);
                totalTaxableAmt = totalTaxableAmt + Double.parseDouble(purchaseInvoiceTable.getTaxable_amt());
            }
            double disc_amt = Double.parseDouble(discAmtText);
            double percentage = (disc_amt / totalTaxableAmt) * 100;
            System.out.println("totalTaxableAmt in purc_dis_amt() " + totalTaxableAmt + " and percentage " + percentage + " and totalTaxableAmt= " + totalTaxableAmt);

            purchase_discount.setText(String.format("%.2f", percentage));
            PurchaseInvoiceCalculationForPurOrderToInvoice.discountPropotionalCalculation("0", disc_amt + "",
                    tfAddCharges.getText(), tvPurchaseInvoiceTable, callback);
        } else {
            purchase_discount.setText("");
            PurchaseInvoiceCalculation.discountPropotionalCalculation("0", "0", tfAddCharges.getText(), tvPurchaseInvoiceTable, callback);
            if (!tfAddCharges.getText().isEmpty()) {
                PurchaseInvoiceCalculationForPurOrderToInvoice.additionalChargesCalculation(tfAddCharges.getText(), tvPurchaseInvoiceTable, callback);
            }
        }
        PurchaseInvoiceCalculationForPurOrderToInvoice.calculateGst(tvPurchaseInvoiceTable, callback);
    }

    public void onClickCancel(ActionEvent actionEvent) {
        AlertUtility.AlertConfirmation(AlertUtility.alertTypeConfirmation, LedgerMessageConsts.msgGoList, callback -> {
            if (callback == 1) {
                GlobalController.getInstance().addTabStatic(PURCHASE_ORDER_LIST_SLUG, false);
            }
        });
    }

    //todo:Set the SUpplier Details of the Selected Product Bottom in Supplier Table
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
            PurOrderToInvoiceProHisSupplierName.setCellValueFactory(new PropertyValueFactory<>("supplierName"));
            PurOrderToInvoiceProHisInvNo.setCellValueFactory(new PropertyValueFactory<>("supplierInvNo"));
            PurOrderToInvoiceProHisInvDate.setCellValueFactory(new PropertyValueFactory<>("supplierInvDate"));
            PurOrderToInvoiceProHisBatch.setCellValueFactory(new PropertyValueFactory<>("supplierBatch"));
            PurOrderToInvoiceProHisMRP.setCellValueFactory(new PropertyValueFactory<>("supplierMrp"));
            PurOrderToInvoiceProHisQty.setCellValueFactory(new PropertyValueFactory<>("supplierQty"));
            PurOrderToInvoiceProHisRate.setCellValueFactory(new PropertyValueFactory<>("supplierRate"));
            PurOrderToInvoiceProHisCost.setCellValueFactory(new PropertyValueFactory<>("supplierCost"));
            PurOrderToInvoiceProHisDisPer.setCellValueFactory(new PropertyValueFactory<>("supplierDisPer"));
            PurOrderToInvoiceProHisDisAmt.setCellValueFactory(new PropertyValueFactory<>("supplierDisAmt"));

            tvInvoiceProductHistory.setItems(supplierDataList);

        }
    }

    //todo: fetch Selected Product data for product info
    public void fetchSelectedProductData(String ProductId) {
        try {
            purchOrderToInvLogger.info("Fetching Selected Product Data to show in product info");
            Map<String, String> body = new HashMap<>();
            body.put("product_id", ProductId);
            System.out.println("ProductId 123 " + ProductId);
            String requestBody = Globals.mapToStringforFormData(body);
            HttpResponse<String> response = APIClient.postFormDataRequest(requestBody, EndPoints.TRANSACTION_PRODUCT_DETAILS);
            System.out.println("Product Details response body" + response.body());
            JsonObject jsonObject = new Gson().fromJson(response.body(), JsonObject.class);
            System.out.println("jsonObject of Product details " + jsonObject);
            ObservableList<TranxProductWindowDTO> observableList = FXCollections.observableArrayList();
            //todo: activating the product tab
            tpPurOrderToInvoice.getSelectionModel().select(tabPurOrderToInvoiceProduct);
            if (jsonObject.get("responseStatus").getAsInt() == 200) {
                tpPurOrderToInvoice.getSelectionModel().select(tabPurOrderToInvoiceProduct);
                JsonObject item = jsonObject.get("result").getAsJsonObject();
                System.out.println("Item:" + item);
                String brand = item.get("brand").getAsString();
                String hsn = item.get("hsn").getAsString();
                String group = item.get("group").getAsString();
                String subgroup = item.get("subgroup").getAsString();
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
                lbPurOrderToInvoiceProductBrand.setText(brand);
                lbPurOrderToInvoiceProductGroup.setText(group);
                lbPurOrderToInvoiceProductSubGroup.setText(subgroup);
                lbPurOrderToInvoiceProductCategory.setText(category);
                lbPurOrderToInvoiceProductHsn.setText(hsn);
                lbPurOrderToInvoiceProductTaxType.setText(tax_type);
                lbPurOrderToInvoiceProductTax.setText(tax);
                lbPurOrderToInvoiceProductMargin.setText(margin);
                lbPurOrderToInvoiceProductCost.setText(cost);
                lbPurOrderToInvoiceProductShelfId.setText(shelfId);
                lbPurOrderToInvoiceProductMinStock.setText(min_stock);
                lbPurOrderToInvoiceProductMaxStock.setText(max_stock);

            } else {
                System.out.println("Error in response");
            }
        } catch (Exception e) {
            purchOrderToInvLogger.error("Error in Fetching Selected Product data is" + e.getMessage());
            e.printStackTrace();
        }
    }

    //todo: fetch Selected ledger data
    public void fetchSelectedLedgerData(String LedgerId) {
        try {
            purchOrderToInvLogger.info("Fetching Selected Ledger Data to show in Ledger info");
            Map<String, String> body = new HashMap<>();
            body.put("ledger_id", LedgerId);
            System.out.println("ledger_id 123 " + LedgerId);
            String requestBody = Globals.mapToStringforFormData(body);
            HttpResponse<String> response = APIClient.postFormDataRequest(requestBody, EndPoints.TRANSACTION_LEDGER_DETAILS);
            purchOrderToInvLogger.info("response data of selected Ledger " + response.body());
            JsonObject jsonObject = new Gson().fromJson(response.body(), JsonObject.class);
            ObservableList<TranxLedgerWindowDTO> observableList = FXCollections.observableArrayList();
            System.out.println("fetchedSelectedLedgerData " + jsonObject);
            if (jsonObject.get("responseStatus").getAsInt() == 200) {
                //todo: activating the ledger tab
                tpPurOrderToInvoice.getSelectionModel().select(tabPurOrderToInvoiceLedger);

                CompanyStateCode = jsonObject.get("company_state_code").getAsLong();
                JsonObject item = jsonObject.get("result").getAsJsonObject();
                stateCode = item.get("stateCode").getAsLong();
                ledgerStateCode = item.get("stateCode").getAsString();

                System.out.println("Item:" + item);
                String gstNo = item.get("gst_number").getAsString();
                String Area = item.get("area").getAsString();
                String bank = item.get("bank_name").getAsString();
                String contact_person = item.get("contact_name").getAsString();
                String mobile_no = item.get("contact_no").getAsString();
//                String transport = item.get("transport").getAsString();
                String credit_days = item.get("credit_days").getAsString();
                String fssai = item.get("fssai_number").getAsString();
                String licence_no = item.get("license_number").getAsString();
                String route = item.get("route").getAsString();
                //setting data in Ledger details block in sales quotation page
                lbPurOrderToInvoiceLedgerGstNo.setText(gstNo);
                lbPurOrderToInvoiceLedgerArea.setText(Area);
                lbPurOrderToInvoiceLedgerBank.setText(bank);
                lbPurOrderToInvoiceLedgerContactPerson.setText(contact_person);
                lbPurOrderToInvoiceLedgerTransport.setText("");
                lbPurOrderToInvoiceLedgerCreditDays.setText(credit_days);
                lbPurOrderToInvoiceLedgerFssai.setText(fssai);
                lbPurOrderToInvoiceLedgerLicense.setText(licence_no);
                lbPurOrderToInvoiceLedgerRoute.setText(route);

            } else {

                purchOrderToInvLogger.error("Error in response of fetching selected Ledger data");
            }
        } catch (Exception e) {
            purchOrderToInvLogger.error("Error in Fetching Selected Ledger data is" + e.getMessage());
            e.printStackTrace();
        }
    }
}

class TextFieldTableCellForPurInvoiceForPurOrderToInvoice extends TableCell<PurchaseInvoiceTable, String> {
    private final TextField textField;
    private final String columnName;

    private final TableCellCallback<Object[]> callback;
    private TextField button;

    TableCellCallback<Integer> product_callback;

    public TextFieldTableCellForPurInvoiceForPurOrderToInvoice(String columnName, TableCellCallback<Object[]> callback) {
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
        quantityColumn();
        rateColumn();
        DisPerColumn();
        DisAmtColumn();

        batchColumn();

        netAmountColumn();

    }

    public TextFieldTableCellForPurInvoiceForPurOrderToInvoice(String columnName, TableCellCallback<Object[]> callback, TableCellCallback<Integer> product_callback, TextField button) {
        this.columnName = columnName;
        this.textField = new TextField();
        this.product_callback = product_callback;
        this.callback = callback;
        this.button = button;
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
        quantityColumn();
        freeColumn();
        rateColumn();
        DisPerColumn();
        DisAmtColumn();
        batchColumn();
        netAmountColumn();
    }


    private void batchColumn() {
        if ("tcBatch".equals(columnName)) {
            textField.setEditable(false);

            textField.setOnKeyPressed(event -> {
                if (event.getCode() == KeyCode.SPACE) {
                    openBatchWindow();
                } else if (event.isShiftDown() && event.getCode() == KeyCode.TAB) {
                    TableColumn<PurchaseInvoiceTable, ?> colName = getTableView().getColumns().get(6);
                    getTableView().edit(getIndex(), colName);
                } else if (event.getCode() == KeyCode.ENTER || event.getCode() == KeyCode.TAB) {
                    if (!textField.getText().isEmpty()) {
                        if ((getTableView().getItems().size() - 1) == getIndex()) {
                            getTableView().getItems().add(new PurchaseInvoiceTable("", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", ""));
                        }
                        TableColumn<PurchaseInvoiceTable, ?> colName = getTableView().getColumns().get(1);
                        getTableView().edit(getIndex() + 1, colName);
                    } else {
                        textField.requestFocus();
                    }
                }
            });

            textField.setOnMouseClicked(event -> {
                openBatchWindow();
            });
        }
    }

    private void particularsColumn() {
        if ("tcParticulars".equals(columnName)) {
            textField.setEditable(false);
            textField.setOnKeyPressed(event -> {
                if (event.getCode() == KeyCode.SPACE) {
                    openProduct();
                } else if (event.isShiftDown() && event.getCode() == KeyCode.TAB) {
                    if (getIndex() == 1) {
                        TableColumn<PurchaseInvoiceTable, ?> colName = getTableView().getColumns().get(7);
                        getTableView().edit(getIndex() - 1, colName);
                    } else {
                        TableColumn<PurchaseInvoiceTable, ?> colName = getTableView().getColumns().get(12);
                        getTableView().edit(getIndex() - 1, colName);
                    }
                } else if (event.getCode() == KeyCode.ENTER || !event.isShiftDown() && event.getCode() == KeyCode.TAB) {
                    if (textField.getText().isEmpty() && getIndex() == getTableView().getItems().size() - 1 && getIndex() != 0) {
                        Platform.runLater(() -> {
                            button.requestFocus();
                        });
                    } else if (!textField.getText().isEmpty()) {
                        TableColumn<PurchaseInvoiceTable, ?> colName = getTableView().getColumns().get(6);
                        getTableView().edit(getIndex(), colName);
                    } else {
                        textField.requestFocus();
                    }
                    event.consume();
                }
            });

            textField.setOnMouseClicked(event -> {
                openProduct();
            });
        }
    }

    private void netAmountColumn() {

        if ("tcNetAmount".equals(columnName)) {
            textField.setEditable(false);
            textField.setOnKeyPressed(event -> {
                if (event.getCode() == KeyCode.ENTER) {
                    System.out.println(getTableRow().getItem().getNet_amount());

                    int current_index = getTableRow().getIndex();

                    int serial_no = Integer.parseInt(getTableRow().getItem().getSr_no());

//                    getTableView().getItems().addAll(new PurchaseInvoiceTable("",String.valueOf(current_index+1),String.valueOf(serial_no+1),"","","","","","","","","","","","","","",""));
                }
            });
        }
    }

    private void quantityColumn() {
        if ("tcQuantity".equals(columnName)) {
            textField.setEditable(true);
            textField.setOnKeyPressed(event -> {
                if (!textField.getText().isEmpty()) {
                    CommonValidationsUtils.restrictToNumbers(textField);
                }
                if (event.getCode() == KeyCode.SHIFT && event.getCode() == KeyCode.TAB) {
                    TableColumn<PurchaseInvoiceTable, ?> colName = getTableView().getColumns().get(7);
                    getTableView().edit(getIndex(), colName);
                } else if (event.getCode() == KeyCode.ENTER || event.getCode() == KeyCode.TAB) {
                    Integer index = getIndex();
                    if (!textField.getText().isEmpty()) {
                        TableColumn<PurchaseInvoiceTable, ?> colName = getTableView().getColumns().get(9);
                        getTableView().edit(index, colName);
                    } else {
                        textField.requestFocus();
                    }
                    PurchaseInvoiceCalculationForPurOrderToInvoice.rowCalculationForPurcInvoice(index, getTableView(), callback);
                }
            });

        }
    }

    private void freeColumn() {
        if ("tcFreeQty".equals(columnName)) {
            textField.setEditable(true);
            textField.setOnKeyPressed(event -> {
                if (!textField.getText().isEmpty()) {
                    CommonValidationsUtils.restrictToNumbers(textField);
                }
                if (event.getCode() == KeyCode.SHIFT && event.getCode() == KeyCode.TAB) {
                    TableColumn<PurchaseInvoiceTable, ?> colName = getTableView().getColumns().get(7);
                    getTableView().edit(getIndex(), colName);

                } else if (event.getCode() == KeyCode.ENTER || event.getCode() == KeyCode.TAB) {
                    Integer index = getIndex();
                    if (!textField.getText().isEmpty()) {
                        TableColumn<PurchaseInvoiceTable, ?> colName = getTableView().getColumns().get(9);
                        getTableView().edit(index, colName);
                    } else {
                        textField.requestFocus();
                    }
                    PurchaseInvoiceCalculationForPurOrderToInvoice.rowCalculationForPurcInvoice(index, getTableView(), callback);
                }
//                else if (event.getCode() == KeyCode.DOWN) {
//                    getTableView().getItems().addAll(new PurchaseInvoiceTable("", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", ""));
//                    TableColumn<PurchaseInvoiceTable, ?> colName = getTableView().getColumns().get(1);
//                    getTableView().edit(getIndex() + 1, colName);
//                }
            });

        }
    }

    private void rateColumn() {
        if ("tcRate".equals(columnName)) {
            textField.setEditable(true);
            textField.setOnKeyPressed(event -> {
                if (!textField.getText().isEmpty()) {
                    CommonValidationsUtils.restrictToDecimalNumbers(textField);
                }
                if (event.isShiftDown() && event.getCode() == KeyCode.TAB) {
                    TableColumn<PurchaseInvoiceTable, ?> colName = getTableView().getColumns().get(8);
                    getTableView().edit(getIndex() - 1, colName);
                } else if (event.getCode() == KeyCode.ENTER || event.getCode() == KeyCode.TAB) {
                    int index = getIndex();
                    if (!textField.getText().isEmpty()) {

                        TableColumn<PurchaseInvoiceTable, ?> colName = getTableView().getColumns().get(11);
                        getTableView().edit(index, colName);
                    } else {
                        textField.requestFocus();
                    }
                    PurchaseInvoiceCalculationForPurOrderToInvoice.rowCalculationForPurcInvoice(index, getTableView(), callback);
                }
            });

        }
    }

    private void DisPerColumn() {
        if ("tcDisPer".equals(columnName)) {
            textField.setEditable(true);
            textField.setOnKeyPressed(event -> {
                if (!textField.getText().isEmpty()) {
                    CommonValidationsUtils.restrictToDecimalNumbers(textField);
                }
                if (event.isShiftDown() && event.getCode() == KeyCode.TAB) {
                    TableColumn<PurchaseInvoiceTable, ?> colName = getTableView().getColumns().get(9);
                    getTableView().edit(getIndex(), colName);
                }
                if (event.getCode() == KeyCode.ENTER || event.getCode() == KeyCode.TAB) {
                    int index = getIndex();

                    TableColumn<PurchaseInvoiceTable, ?> colName = getTableView().getColumns().get(12);
                    getTableView().edit(index, colName);
                    PurchaseInvoiceCalculationForPurOrderToInvoice.rowCalculationForPurcInvoice(index, getTableView(), callback);
                }
            });
        }
    }

    private void DisAmtColumn() {
        if ("tcDisAmt".equals(columnName)) {
            textField.setEditable(true);

            textField.setOnKeyPressed(event -> {
                if (!textField.getText().isEmpty()) {
                    CommonValidationsUtils.restrictToDecimalNumbers(textField);
                }
                if (event.isShiftDown() && event.getCode() == KeyCode.TAB) {
                    TableColumn<PurchaseInvoiceTable, ?> colName = getTableView().getColumns().get(11);
                    getTableView().edit(getIndex(), colName);
                } else if (event.getCode() == KeyCode.ENTER || event.getCode() == KeyCode.TAB) {
                    int index = getIndex();
                    getTableView().getItems().addAll(new PurchaseInvoiceTable("", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", ""));
                    TableColumn<PurchaseInvoiceTable, ?> colName = getTableView().getColumns().get(1);
                    getTableView().edit(getIndex() + 1, colName);
                    PurchaseInvoiceCalculationForPurOrderToInvoice.rowCalculationForPurcInvoice(index, getTableView(), callback);

                }
            });
        }
    }

    private void openProduct() {
        SingleInputDialogs.openProductPopUp(Communicator.stage, "Product", input -> {

            String productName = (String) input[0];
            String productId = (String) input[1];
            String packaging = (String) input[2];
            String mrp = (String) input[3];
            String unit = (String) input[4];
            String taxper = (String) input[5];
            String salesrate = (String) input[6];
            String purchaseRate = (String) input[7];
            String is_batch = (String) input[8];

            /**** Multi Unit Rate : rateList contains unit wise rate ****/
            List<UnitRateList> rateList = new ArrayList<>();
            rateList.addAll((Collection<? extends UnitRateList>) input[9]);
            getTableRow().getItem().setRateList(rateList);

            getTableRow().getItem().setParticulars(productName);
            getTableRow().getItem().setProduct_id(productId);
            getTableRow().getItem().setPackages(packaging);
            getTableRow().getItem().setIs_batch(is_batch);
            getTableRow().getItem().setTax(taxper);

            if (productId != "") {
                //Pass productId to ProductPopup for Focus in open ProductPopup after Product is Selected in Create
                SingleInputDialogs.productId = productId;
            } else {
                SingleInputDialogs.productId = "";
            }


            List<LevelAForPurInvoice> levelAForPurInvoiceList = ProductUnitsPackingForPurOrderToInvoice.getAllProductUnitsPackingFlavour(productId);
            int index = getTableRow().getIndex();
            ObservableList<LevelAForPurInvoice> observableLevelAList = FXCollections.observableArrayList(levelAForPurInvoiceList);

            if (index >= 0 && index < PurInvoiceCommunicator.levelAForPurInvoiceObservableList.size()) {
                PurInvoiceCommunicator.levelAForPurInvoiceObservableList.set(index, observableLevelAList);
                getTableRow().getItem().setLevelA(null);
                getTableRow().getItem().setLevelA(levelAForPurInvoiceList.get(0).getLabel());


            } else {
                PurInvoiceCommunicator.levelAForPurInvoiceObservableList.add(observableLevelAList);
                getTableRow().getItem().setLevelA(null);
                getTableRow().getItem().setLevelA(levelAForPurInvoiceList.get(0).getLabel());

            }

            Integer product_id = Integer.parseInt(productId);
            if (product_callback != null) {
                product_callback.call(product_id);
            }
        });
    }

    public void openBatchWindow() {
        int current_index = getTableRow().getIndex();
        List<PurchaseInvoiceTable> tableList = getTableView().getItems();

        List<BatchWindowTableDTO> batchWindowTableDTOList;
        PurchaseInvoiceTable current_table_row = tableList.get(current_index);
        batchWindowTableDTOList = current_table_row.getBatchWindowTableDTOList();
        batchWindowTableDTOList.get(0).setCurrentIndex(String.valueOf(current_index));     //imp for setting the row index of CMPT

        System.out.println("batchWindowTableDTOList  :  " + batchWindowTableDTOList);

        Map<String, String> product_details_to_batch_window = new HashMap<>();
        product_details_to_batch_window.put("product_id", current_table_row.getProduct_id());
        product_details_to_batch_window.put("levelA_id", current_table_row.getLevelA_id());
        product_details_to_batch_window.put("levelB_id", current_table_row.getLevelB_id());
        product_details_to_batch_window.put("levelC_id", current_table_row.getLevelC_id());
        product_details_to_batch_window.put("unit_id", current_table_row.getUnit_id());
        product_details_to_batch_window.put("tax", current_table_row.getTax());
        product_details_to_batch_window.put("ledger_id", getTableView().getItems().get(0).getLedger_id());
        System.out.println("current_table_row.getRate_b()" + current_table_row.getRate_b());
        product_details_to_batch_window.put("fsrmh", "" + current_table_row.getRate_b()); //! Specific FR CSR RATE
        product_details_to_batch_window.put("fsrai", "" + 0.0);
        product_details_to_batch_window.put("csrmh", "" + 0.0);
        product_details_to_batch_window.put("csrai", "" + 0.0);
       /* List<UnitRateList> rateList = current_table_row.getRateList();
        for (UnitRateList unitRateList : rateList) {
            if (unitRateList.getUnitId() == Long.parseLong(current_table_row.getUnit_id())) {
                product_details_to_batch_window.put("fsrmh", "" + unitRateList.getFsrmh());
                product_details_to_batch_window.put("fsrai", "" + unitRateList.getFsrai());
                product_details_to_batch_window.put("csrmh", "" + unitRateList.getCsrmh());
                product_details_to_batch_window.put("csrai", "" + unitRateList.getCsrai());

            }
        }*/

        Long ledgerid = Long.parseLong(getTableView().getItems().get(0).getLedger_id());
//        System.out.println("ledgerId in openBatchWindow "+ledgerid+" is_batchhh "+current_table_row.getIs_batch());
        if (current_table_row.getIs_batch().equals("true") && ledgerid != 0) {
            BatchWindow.batchWindow(String.valueOf(current_index), product_details_to_batch_window, batchWindowTableDTOList, input -> {

                System.out.println("input.get(0) " + input.get(0));

                getTableView().getItems().get(Integer.parseInt(input.get(0).getCurrentIndex())).setBatchWindowTableDTOList(input);

                for (BatchWindowTableDTO batchWindowTableDTOSs : input) {
                    getTableView().getItems().get(Integer.parseInt(input.get(0).getCurrentIndex())).setBatch_or_serial(batchWindowTableDTOSs.getBatchNo());
                    getTableView().getItems().get(Integer.parseInt(input.get(0).getCurrentIndex())).setQuantity(batchWindowTableDTOSs.getQuantity());
                    getTableView().getItems().get(Integer.parseInt(input.get(0).getCurrentIndex())).setRate(batchWindowTableDTOSs.getPurchaseRate());
                    getTableView().getItems().get(Integer.parseInt(input.get(0).getCurrentIndex())).setDis_per(batchWindowTableDTOSs.getDiscountPercentage());
                    getTableView().getItems().get(Integer.parseInt(input.get(0).getCurrentIndex())).setDis_amt(batchWindowTableDTOSs.getDiscountAmount());

                    getTableView().getItems().get(Integer.parseInt(input.get(0).getCurrentIndex())).setB_details_id(batchWindowTableDTOSs.getB_details_id());
                    getTableView().getItems().get(Integer.parseInt(input.get(0).getCurrentIndex())).setB_no(batchWindowTableDTOSs.getBatchNo());
                    getTableView().getItems().get(Integer.parseInt(input.get(0).getCurrentIndex())).setB_rate(batchWindowTableDTOSs.getMrp());
                    getTableView().getItems().get(Integer.parseInt(input.get(0).getCurrentIndex())).setB_purchase_rate(batchWindowTableDTOSs.getPurchaseRate());

                    double purchase_rate = Double.parseDouble(batchWindowTableDTOSs.getPurchaseRate());

                    double discount_per = 0.0;
                    if (!batchWindowTableDTOSs.getDiscountPercentage().isEmpty()) {
                        discount_per = Double.parseDouble(batchWindowTableDTOSs.getDiscountPercentage());
                    }

                    double discount_amt = 0.0;

                    if (!batchWindowTableDTOSs.getDiscountAmount().isEmpty()) {
                        discount_amt = Double.parseDouble(batchWindowTableDTOSs.getDiscountAmount());
                    }

                    double total_batch_discount = (purchase_rate * discount_per) / 100;

                    total_batch_discount = total_batch_discount + discount_amt;

                    double costing_without_tax = purchase_rate - total_batch_discount;

                    double current_row_tax = Double.parseDouble(getTableView().getItems().get(Integer.parseInt(input.get(0).getCurrentIndex())).getTax());

                    double tax_amt_of_brate = (costing_without_tax * current_row_tax) / 100;

                    double costing_with_tax = costing_without_tax + tax_amt_of_brate;


                    getTableView().getItems().get(Integer.parseInt(input.get(0).getCurrentIndex())).setCosting(String.valueOf(costing_without_tax));

                    getTableView().getItems().get(Integer.parseInt(input.get(0).getCurrentIndex())).setCosting_with_tax(String.valueOf(costing_with_tax));


                    getTableView().getItems().get(Integer.parseInt(input.get(0).getCurrentIndex())).setB_expiry(batchWindowTableDTOSs.getExpiryDate());

                    getTableView().getItems().get(Integer.parseInt(input.get(0).getCurrentIndex())).setRate_a(batchWindowTableDTOSs.getFsr_mh().isEmpty() ? String.valueOf(0.0) : batchWindowTableDTOSs.getFsr_mh());
                    getTableView().getItems().get(Integer.parseInt(input.get(0).getCurrentIndex())).setRate_b(batchWindowTableDTOSs.getFsr_ai().isEmpty() ? String.valueOf(0.0) : batchWindowTableDTOSs.getFsr_ai());
                    getTableView().getItems().get(Integer.parseInt(input.get(0).getCurrentIndex())).setRate_c(batchWindowTableDTOSs.getCsr_mh().isEmpty() ? String.valueOf(0.0) : batchWindowTableDTOSs.getCsr_mh());
                    getTableView().getItems().get(Integer.parseInt(input.get(0).getCurrentIndex())).setRate_d(batchWindowTableDTOSs.getCsr_ai().isEmpty() ? String.valueOf(0.0) : batchWindowTableDTOSs.getCsr_ai());

                    getTableView().getItems().get(Integer.parseInt(input.get(0).getCurrentIndex())).setMin_margin(batchWindowTableDTOSs.getMargin());

                    getTableView().getItems().get(Integer.parseInt(input.get(0).getCurrentIndex())).setManufacturing_date(batchWindowTableDTOSs.getManufacturingDate());

                }
                String batchNo = getTableView().getItems().get(Integer.parseInt(input.get(0).getCurrentIndex())).getBatch_or_serial();
                String bDetailsId = getTableView().getItems().get(Integer.parseInt(input.get(0).getCurrentIndex())).getB_details_id();

                PurchaseInvoiceCalculationForPurOrderToInvoice.rowCalculationForPurcInvoice(Integer.parseInt(input.get(0).getCurrentIndex()), getTableView(), callback);
                PurchaseInvoiceCalculationForPurOrderToInvoice.calculateGst(getTableView(), callback);

                getTableView().getItems().addAll(new PurchaseInvoiceTable("", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", ""));
                TableColumn<PurchaseInvoiceTable, ?> colName = getTableView().getColumns().get(1);
                getTableView().edit(getIndex() + 1, colName);
            });
        }
    }

    public void textfieldStyle() {
        if (columnName.equals("tcParticulars")) {
            textField.setPromptText("Particulars");
            textField.setEditable(false);
        } else {
            textField.setPromptText("0");
        }

        textField.setPrefHeight(38);
        textField.setMaxHeight(38);
        textField.setMinHeight(38);

        if (columnName.equals("tcParticulars")) {
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


        if (columnName.equals("tcNetAmount")) {
            this.textField.setOnKeyPressed(event -> {
                if (event.getCode() == KeyCode.ENTER) {
                    commitEdit(textField.getText());
                    getTableView().getItems().addAll(new PurchaseInvoiceTable("", "", "1", "", "", "", "", "", "", "", "", "", "", "", "", "", "", ""));
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
        if (item != null && columnName.equals("tcParticulars")) {
            ((PurchaseInvoiceTable) item).setParticulars(newValue.isEmpty() ? "" : newValue);
        } else if (columnName.equals("tcBatch")) {
            (getTableRow().getItem()).setBatch_or_serial(newValue.isEmpty() ? "" : newValue);
        } else if (columnName.equals("tcQuantity")) {
            (getTableRow().getItem()).setQuantity(newValue.isEmpty() ? "0" : newValue);
        } else if (columnName.equals("tcFreeQty")) {
            (getTableRow().getItem()).setFree(newValue.isEmpty() ? "0" : newValue);
        } else if (columnName.equals("tcRate")) {
            (getTableRow().getItem()).setRate(newValue.isEmpty() ? "0.0" : newValue);
        } else if (columnName.equals("tcGrossAmount")) {
            (getTableRow().getItem()).setGross_amount(newValue.isEmpty() ? "0.0" : newValue);
        } else if (columnName.equals("tcDisPer")) {
            (getTableRow().getItem()).setDis_per(newValue.isEmpty() ? "0.0" : newValue);
        } else if (columnName.equals("tcDisAmt")) {
            (getTableRow().getItem()).setDis_amt(newValue.isEmpty() ? "0.0" : newValue);
        } else if (columnName.equals("tcTax")) {
            (getTableRow().getItem()).setTax(newValue.isEmpty() ? "0.0" : newValue);
        } else if (columnName.equals("tcNetAmount")) {
            (getTableRow().getItem()).setNet_amount(newValue.isEmpty() ? "0.0" : newValue);
        }
    }
}

class ComboBoxTableCellForLevelAForPurOrderToInvoice extends TableCell<PurchaseInvoiceTable, String> {

    String columnName;
    private final ComboBox<String> comboBox;
    ObservableList<LevelAForPurInvoice> comboList = null;

    public ComboBoxTableCellForLevelAForPurOrderToInvoice(String columnName) {

        this.columnName = columnName;
        this.comboBox = new ComboBox<>();

        this.comboBox.setPromptText("Select");

        comboBox.setPrefHeight(38);
        comboBox.setMaxHeight(38);
        comboBox.setMinHeight(38);

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
                if (PurInvoiceCommunicator.levelAForPurInvoiceObservableList != null && !PurInvoiceCommunicator.levelAForPurInvoiceObservableList.isEmpty()) {
                    for (LevelAForPurInvoice levelAForPurInvoice : PurInvoiceCommunicator.levelAForPurInvoiceObservableList.get(getTableRow().getIndex())) {
                        if (selectedItem.equals(levelAForPurInvoice.getLabel())) {

                            getTableRow().getItem().setLevelA_id(levelAForPurInvoice.getValue());

                            ObservableList<LevelBForPurInvoice> observableLevelAList = FXCollections.observableArrayList(levelAForPurInvoice.getLevelBOpts());

                            int index = getTableRow().getIndex();

                            if (index >= 0 && index < PurInvoiceCommunicator.levelBForPurInvoiceObservableList.size()) {
                                PurInvoiceCommunicator.levelBForPurInvoiceObservableList.set(index, observableLevelAList);

                                getTableRow().getItem().setLevelB(null);
                                getTableRow().getItem().setLevelB(levelAForPurInvoice.getLevelBOpts().get(0).getLabel());

                                for (LevelBForPurInvoice levelBForPurInvoice : PurInvoiceCommunicator.levelBForPurInvoiceObservableList.get(index)) {
                                    if (levelBForPurInvoice.getValue().equals(getTableRow().getItem().getLevelB_id())) {
                                        getTableRow().getItem().setLevelB(null);
                                        getTableRow().getItem().setLevelB(levelBForPurInvoice.getLabel());
                                    }
                                }

                            } else {
                                PurInvoiceCommunicator.levelBForPurInvoiceObservableList.add(observableLevelAList);
                                getTableRow().getItem().setLevelB(null);
                                getTableRow().getItem().setLevelB(levelAForPurInvoice.getLevelBOpts().get(0).getLabel());

                                for (LevelBForPurInvoice levelBForPurInvoice : PurInvoiceCommunicator.levelBForPurInvoiceObservableList.get(index)) {
                                    if (levelBForPurInvoice.getValue().equals(getTableRow().getItem().getLevelB_id())) {
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
        TableRow<PurchaseInvoiceTable> row = getTableRow();
        if (row != null) {
            PurchaseInvoiceTable item = row.getItem();
            if (item != null) {
                item.setLevelA(newValue);
            }
        }
    }

}

class ComboBoxTableCellForLevelBForPurOrderToInvoice extends TableCell<PurchaseInvoiceTable, String> {

    String columnName;
    private final ComboBox<String> comboBox;
    ObservableList<LevelBForPurInvoice> comboList = null;

    public ComboBoxTableCellForLevelBForPurOrderToInvoice(String columnName) {
        this.columnName = columnName;
        this.comboBox = new ComboBox<>();

        this.comboBox.setPromptText("Select");

        comboBox.setPrefHeight(38);
        comboBox.setMaxHeight(38);
        comboBox.setMinHeight(38);

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

                            getTableRow().getItem().setLevelB_id(levelBForPurInvoice.getValue());

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
                                    if (levelCForPurInvoice.getValue().equals(getTableRow().getItem().getLevelC_id())) {
                                        getTableRow().getItem().setLevelC(null);
                                        getTableRow().getItem().setLevelC(levelCForPurInvoice.getLabel());
                                    }
                                }

                            } else {
                                PurInvoiceCommunicator.levelCForPurInvoiceObservableList.add(observableLevelAList);
                                getTableRow().getItem().setLevelC(null);
                                getTableRow().getItem().setLevelC(levelBForPurInvoice.getLevelCOpts().get(0).getLabel());

                                for (LevelCForPurInvoice levelCForPurInvoice : PurInvoiceCommunicator.levelCForPurInvoiceObservableList.get(index)) {
                                    if (levelCForPurInvoice.getValue().equals(getTableRow().getItem().getLevelC_id())) {
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

class ComboBoxTableCellForLevelCForPurOrderToInvoice extends TableCell<PurchaseInvoiceTable, String> {

    String columnName;
    private final ComboBox<String> comboBox;
    ObservableList<LevelAForPurInvoice> comboList = null;

    public ComboBoxTableCellForLevelCForPurOrderToInvoice(String columnName) {


        this.columnName = columnName;
        this.comboBox = new ComboBox<>();

        this.comboBox.setPromptText("Select");

        comboBox.setPrefHeight(38);
        comboBox.setMaxHeight(38);
        comboBox.setMinHeight(38);

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

                            getTableRow().getItem().setLevelC_id(levelCForPurInvoice.getValue());
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

                            } else {
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

class ComboBoxTableCellForUnitForPurInvoiceForPurOrderToInvoice extends TableCell<PurchaseInvoiceTable, String> {

    String columnName;
    private final ComboBox<String> comboBox;

    public ComboBoxTableCellForUnitForPurInvoiceForPurOrderToInvoice(String columnName) {


        this.columnName = columnName;
        this.comboBox = new ComboBox<>();


        this.comboBox.setPromptText("Select");

        comboBox.setPrefHeight(38);
        comboBox.setMaxHeight(38);
        comboBox.setMinHeight(38);

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
                            getTableRow().getItem().setUnit_conv(String.valueOf(unitForPurInvoice.getUnitConversion()));

                        }
                    }
                }
            }

        });
        comboBox.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.SPACE) {
                comboBox.show();
            }
            if (event.isShiftDown() && event.getCode() == KeyCode.TAB) {
                TableColumn<PurchaseInvoiceTable, ?> colName = getTableView().getColumns().get(1);
                getTableView().edit(getIndex(), colName);
            }

            if (event.getCode() == KeyCode.ENTER || !event.isShiftDown() && event.getCode() == KeyCode.TAB) {
                TableColumn<PurchaseInvoiceTable, ?> colName = getTableView().getColumns().get(7);
                System.out.println("Col name:" + colName.getText());
                getTableView().edit(getIndex(), colName);
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

class ButtonTableCellForPurOrderToInvoice extends TableCell<PurchaseInvoiceTable, String> {
    private Button delete;

    public ButtonTableCellForPurOrderToInvoice() {


        this.delete = createButtonWithImage();


        delete.setOnAction(actionEvent -> {
            PurchaseInvoiceTable table = getTableView().getItems().get(getIndex());
            getTableView().getItems().remove(table);
//            getTableView().refresh();
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

class ProductUnitsPackingForPurOrderToInvoice {

    public static List<LevelAForPurInvoice> getAllProductUnitsPackingFlavour(String product_id) {


        Map<String, String> map = new HashMap<>();
        map.put("product_id", product_id);

        List<LevelAForPurInvoice> levelAForPurInvoicesList = new ArrayList<>();
        try {
            String formdata = mapToStringforFormData(map);

            HttpResponse<String> response = APIClient.postFormDataRequest(formdata, "get_all_product_units_packings_flavour");
            String responseBody = response.body();
            JsonObject jsonObject = new Gson().fromJson(responseBody, JsonObject.class);

            int responseStatus = jsonObject.get("responseStatus").getAsInt();
            System.out.println("responseStatus: " + responseStatus);

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

class PurchaseInvoiceCalculationForPurOrderToInvoice {

    public static void rowCalculationForPurcInvoice(int rowIndex, TableView<PurchaseInvoiceTable> tableView, TableCellCallback<Object[]> callback) {
        PurchaseInvoiceTable purchaseInvoiceTable = tableView.getItems().get(rowIndex);
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
//        r_rate = (purchaseInvoiceTable.getRate().isEmpty() || purchaseInvoiceTable.getRate() == null) ? 0.0 : purchaseInvoiceTable.getRate() != null ? Double.parseDouble(purchaseInvoiceTable.getRate()) : 0.0;
        r_rate = !purchaseInvoiceTableData.equalsIgnoreCase("null") ? Double.parseDouble(purchaseInvoiceTableData + "") : 0.0;
        if (!purchaseInvoiceTable.getDis_amt().isEmpty())
            r_dis_amt = Double.parseDouble(purchaseInvoiceTable.getDis_amt());
        if (!purchaseInvoiceTable.getDis_per().isEmpty())
            r_dis_per = Double.parseDouble(purchaseInvoiceTable.getDis_per());
        if (purchaseInvoiceTable.getDis_per2() != null && !purchaseInvoiceTable.getDis_per2().isEmpty()) {
            disPer2 = Double.parseDouble(purchaseInvoiceTable.getDis_per2());
        } else {
            disPer2 = 0.0;
        }
        r_tax_per = Double.parseDouble(purchaseInvoiceTable.getTax());
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
        PurchaseInvoiceTable selectedItem = tableView.getItems().get(rowIndex);
        selectedItem.setGross_amount(GlobalTranx.setDecimalFromStr(base_amt));
        selectedItem.setNet_amount(GlobalTranx.setDecimalFromStr(net_amt));
        selectedItem.setOrg_net_amt(GlobalTranx.setDecimalFromStr(net_amt));
//        selectedItem.setOrg_taxable_amt(taxable_amt);
        selectedItem.setTaxable_amt(GlobalTranx.setDecimalFromStr(taxable_amt));
        selectedItem.setTotal_taxable_amt(GlobalTranx.setDecimalFromStr(total_taxable_amt));
        selectedItem.setIgst(GlobalTranx.setDecimalFromStr(totalTax)); // for Tax Amount
        selectedItem.setSgst(GlobalTranx.setDecimalFromStr(totalTax / 2)); // for Tax Amount
        selectedItem.setCgst(GlobalTranx.setDecimalFromStr(totalTax / 2)); // for Tax  Amount
        selectedItem.setFinal_tax_amt(GlobalTranx.setDecimalFromStr(totalTax));
        selectedItem.setFinal_dis_amt(GlobalTranx.setDecimalFromStr(row_dis_amt));

        // Create API Payload Parameters Adding
        selectedItem.setBase_amt(GlobalTranx.setDecimalFromStr(base_amt));
//        selectedItem.setGross_amt(GlobalTranx.setDecimalFromStr(gross_amt));
        selectedItem.setDis_per(GlobalTranx.setDecimalFromStr((r_dis_per)));
        selectedItem.setDis_per_cal(GlobalTranx.setDecimalFromStr(total_dis_per));
        selectedItem.setDis_amt(GlobalTranx.setDecimalFromStr((r_dis_amt)));
        selectedItem.setDis_amt_cal(GlobalTranx.setDecimalFromStr(r_dis_amt));
        selectedItem.setRow_dis_amt(GlobalTranx.setDecimalFromStr(row_dis_amt));
        selectedItem.setGross_amount1(GlobalTranx.setDecimalFromStr(taxable_amt));
        // tax percentage store cmptrow
        selectedItem.setGst(GlobalTranx.setDecimalFromStr(r_tax_per));
        selectedItem.setIgst_per(GlobalTranx.setDecimalFromStr(r_tax_per));
        selectedItem.setSgst_per(GlobalTranx.setDecimalFromStr(r_tax_per / 2));
        selectedItem.setCgst_per(GlobalTranx.setDecimalFromStr(r_tax_per / 2));
        selectedItem.setTotal_igst(GlobalTranx.setDecimalFromStr(totalTax));
        selectedItem.setTotal_sgst(GlobalTranx.setDecimalFromStr(totalTax / 2));
        selectedItem.setTotal_cgst(GlobalTranx.setDecimalFromStr(totalTax / 2));
        selectedItem.setFinal_amount(GlobalTranx.setDecimalFromStr(net_amt));
        selectedItem.setDis_per2(GlobalTranx.setDecimalFromStr(disPer2));

//        //Display data to table view
//        tblcPurChallCmpTRowGrossAmt.setCellValueFactory(new PropertyValueFactory<>("gross_amt"));
//        tblcPurChallCmpTRowNetAmt.setCellValueFactory(new PropertyValueFactory<>("net_amt"));
        calculateGst(tableView, callback);

    }

    public static void calculateGst(TableView<PurchaseInvoiceTable> tableView, TableCellCallback<Object[]> callback) {

        Map<Double, Double> cgstTotals = new HashMap<>(); //used to merge same cgst Percentage
        Map<Double, Double> sgstTotals = new HashMap<>();//used to merge same sgst Percentage
        Map<Double, Double> igstTotals = new HashMap<>();
        // Initialize totals to zero for each tax percentage
        for (Double taxPercentage : new Double[]{0.0, 3.0, 5.0, 12.0, 18.0, 28.0}) {
            cgstTotals.put(taxPercentage, 0.0);
            sgstTotals.put(taxPercentage, 0.0);
            igstTotals.put(taxPercentage, 0.0);
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
        for (PurchaseInvoiceTable purchaseInvoiceTable : tableView.getItems()) {
            System.out.println("purchaseInvoiceTable.getTax()" + purchaseInvoiceTable.getTax());
            if (purchaseInvoiceTable.getTax().isEmpty()) {
                taxPercentage = 0.0;
            } else {
                taxPercentage = Double.parseDouble(purchaseInvoiceTable.getTax());
            }
            Double quantity = Double.parseDouble(purchaseInvoiceTable.getQuantity());
            if (purchaseInvoiceTable.getFree().isEmpty()) {
                freeQuantity = 0.0;
            } else {
                System.out.println("purchaseInvoiceTable.getFree().. " + purchaseInvoiceTable.getFree());
                if (purchaseInvoiceTable.getFree() != null && !purchaseInvoiceTable.getFree().equalsIgnoreCase("null")) {
                    freeQuantity = Double.parseDouble(purchaseInvoiceTable.getFree());
                } else freeQuantity = 0.0;
            }

            // Total Calculations of each IGST, SGST, CGST
            totalQuantity += quantity;
            totalFreeQuantity += freeQuantity;
            Double igst = Double.parseDouble(purchaseInvoiceTable.getIgst());
            Double cgst = Double.parseDouble(purchaseInvoiceTable.getCgst());
            Double sgst = Double.parseDouble(purchaseInvoiceTable.getSgst());
            totalFinalIgst += igst;
            totalFinalSgst += sgst;
            totalFinalCgst += cgst;
            Double currentCgstTotal = cgstTotals.get(taxPercentage) != null ? Double.parseDouble(cgstTotals.get(taxPercentage).toString()) : 0.0;
            cgstTotals.put(taxPercentage, currentCgstTotal + cgst);
            Double currentSgstTotal = sgstTotals.get(taxPercentage) != null ? Double.parseDouble(sgstTotals.get(taxPercentage).toString()) : 0.0;
            sgstTotals.put(taxPercentage, currentSgstTotal + sgst);
            Double currentIgstTotal = igstTotals.get(taxPercentage) != null ? Double.parseDouble(igstTotals.get(taxPercentage).toString()) : 0.0;
            igstTotals.put(taxPercentage, currentIgstTotal + igst);
//            cgstTotals.put(taxPercentage, cgstTotals != null ? Double.parseDouble(cgstTotals.get(taxPercentage).toString()): 0.0 + cgst ); //0.0
//            sgstTotals.put(taxPercentage,  sgstTotals != null ? Double.parseDouble(sgstTotals.get(taxPercentage).toString()): 0.0  + sgst);

            //Total Calculation of gross amt ,taxable ,tax,discount
            netAmount = Double.parseDouble(purchaseInvoiceTable.getNet_amount());
            totalNetAmt += netAmount;
            grossAmt = Double.parseDouble(purchaseInvoiceTable.getGross_amount());
            totalGrossAmt += grossAmt;
            taxableAmt = Double.parseDouble(purchaseInvoiceTable.getTaxable_amt());
            totaltaxableAmt += taxableAmt;
            disAmt = Double.parseDouble(purchaseInvoiceTable.getFinal_dis_amt());
            totalDisAmt += disAmt;
            taxAmt = Double.parseDouble(purchaseInvoiceTable.getFinal_tax_amt());
            totalTaxAmt += taxAmt;

        }

        ObservableList<GstDTO> observableList = FXCollections.observableArrayList();
        for (Double taxPer : new Double[]{3.0, 5.0, 12.0, 18.0, 28.0}) {
            double totalCGST = cgstTotals.get(taxPer);
            double totalSGST = sgstTotals.get(taxPer);
            double totalIGST = igstTotals.get(taxPer);

            if (totalCGST > 0) {
                System.out.println("Tax Percentage: " + taxPer + "%");
                System.out.println("Total CGST: " + totalCGST);
                System.out.println("Total SGST: " + totalSGST);
                System.out.println();

                observableList.add(new GstDTO(GlobalTranx.setDecimalFromStr(taxPer), GlobalTranx.setDecimalFromStr(totalCGST), GlobalTranx.setDecimalFromStr(totalSGST), GlobalTranx.setDecimalFromStr(totalIGST)));

            }
        }


        if (callback != null) {

            Object[] object = new Object[10];

            object[0] = GlobalTranx.setQtyFormat(totalQuantity);

            object[1] = GlobalTranx.setQtyFormat(totalFreeQuantity);

            object[2] = GlobalTranx.setDecimalFromStr(totalNetAmt);

            object[3] = GlobalTranx.setDecimalFromStr(totalGrossAmt);

            object[4] = GlobalTranx.setDecimalFromStr(totalDisAmt);

            //  lblPurChallTotalTaxableAmount.setText(GlobalTranx.setDecimalFromStr( (totalGrossAmt - totalDisAmt + addChargAmount)));
            object[5] = GlobalTranx.setDecimalFromStr((totalGrossAmt - totalDisAmt + 0.0));

            object[6] = GlobalTranx.setDecimalFromStr(totalTaxAmt);

            object[7] = GlobalTranx.setDecimalFromStr(totalFinalSgst);

            object[8] = GlobalTranx.setDecimalFromStr(totalFinalCgst);

            object[9] = observableList;


            if (callback != null) {
                callback.call(object);
            }
        }
    }


    public static void discountPropotionalCalculation(String disproportionalPer, String disproportionalAmt, String additionalCharges, TableView<PurchaseInvoiceTable> tableView, TableCellCallback<Object[]> callback) {

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
            System.out.println("Gross amt in Prop--->" + tableView.getItems().get(i).getGross_amount());
//            rowCalculationForPurcInvoice(i,tableView,callback);//call row calculation function
            PurchaseInvoiceTable purchaseInvoiceTable = tableView.getItems().get(i);
            totalTaxableAmt = totalTaxableAmt + Double.parseDouble(purchaseInvoiceTable.getTaxable_amt());

        }
        System.out.println("totalTaxableAmt discountPropotionalCalculation :: " + totalTaxableAmt);

        Double rowDisc = 0.0;
        //get data of netamount, tax per,and row taxable amount from cmptrow
        for (PurchaseInvoiceTable purchaseInvoiceTable : tableView.getItems()) {
            System.out.println("getFinal_dis_amt--->" + purchaseInvoiceTable.getFinal_dis_amt());
            if (purchaseInvoiceTable.getFinal_dis_amt().isEmpty())
                rowDisc = 0.0;
            else
                rowDisc = Double.parseDouble(purchaseInvoiceTable.getFinal_dis_amt());
            System.out.println("netAmt--->" + purchaseInvoiceTable.getNet_amount());
            if (purchaseInvoiceTable.getNet_amount().isEmpty()) {
                netAmt = 0.0;
            } else {

                netAmt = Double.parseDouble(purchaseInvoiceTable.getNet_amount());
            }


            if (purchaseInvoiceTable.getTax().isEmpty())
                r_tax_per = 0.0;
            else
                r_tax_per = Double.parseDouble(purchaseInvoiceTable.getTax());
            rowTaxableAmt = Double.parseDouble(purchaseInvoiceTable.getTaxable_amt());
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
            purchaseInvoiceTable.setNet_amount(GlobalTranx.setDecimalFromStr(netAmt));
            purchaseInvoiceTable.setTotal_taxable_amt(GlobalTranx.setDecimalFromStr(totalTaxableAmtAdditional));
            System.out.println("Total Tax Amt--->" + total_tax_amt);
            purchaseInvoiceTable.setIgst(GlobalTranx.setDecimalFromStr(total_tax_amt));
            purchaseInvoiceTable.setCgst(GlobalTranx.setDecimalFromStr(total_tax_amt / 2));
            purchaseInvoiceTable.setSgst(GlobalTranx.setDecimalFromStr(total_tax_amt / 2));
            purchaseInvoiceTable.setFinal_tax_amt(GlobalTranx.setDecimalFromStr(total_tax_amt));
            purchaseInvoiceTable.setFinal_dis_amt(GlobalTranx.setDecimalFromStr(rowDisc));
            purchaseInvoiceTable.setInvoice_dis_amt(GlobalTranx.setDecimalFromStr(rowDisPropAmt));

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


    public static void additionalChargesCalculation(String additionalCharges, TableView<PurchaseInvoiceTable> tableView, TableCellCallback<Object[]> callback) {


        Double additionalChargeAmt = 0.0;//store data textfield string to double

        if (!additionalCharges.isEmpty()) {
            additionalChargeAmt = Double.parseDouble(additionalCharges);//convert string to double


            Double finalNetAmt = 0.0;
            Double r_tax_per = 0.0;
            Double total_tax_amt = 0.0;
            Double netAmt = 0.0;
            Double rowTaxableAmt = 0.0;
            Double totalTaxableAmt = 0.0;
            Double totalDisPropAmt = 0.0;
            Double rowDisPropAmt = 0.0;

            for (int i = 0; i < tableView.getItems().size(); i++) {
                PurchaseInvoiceTable purchaseInvoiceTable = tableView.getItems().get(i);
                totalTaxableAmt = totalTaxableAmt + Double.parseDouble(purchaseInvoiceTable.getTotal_taxable_amt());

            }
            for (PurchaseInvoiceTable purchaseInvoiceTable : tableView.getItems()) {

                netAmt = Double.parseDouble(purchaseInvoiceTable.getNet_amount());
                r_tax_per = Double.parseDouble(purchaseInvoiceTable.getTax());
                rowTaxableAmt = Double.parseDouble(purchaseInvoiceTable.getTotal_taxable_amt());


                totalDisPropAmt = additionalChargeAmt;
                rowDisPropAmt = (rowTaxableAmt * totalDisPropAmt) / totalTaxableAmt;
                rowTaxableAmt = rowTaxableAmt + rowDisPropAmt;

                if (r_tax_per > 0) {
                    total_tax_amt = (r_tax_per / 100) * (rowTaxableAmt);
                    netAmt = rowTaxableAmt + total_tax_amt;
                }

                purchaseInvoiceTable.setNet_amount(GlobalTranx.setDecimalFromStr(netAmt));
                purchaseInvoiceTable.setCgst(GlobalTranx.setDecimalFromStr(total_tax_amt / 2));
                purchaseInvoiceTable.setSgst(GlobalTranx.setDecimalFromStr(total_tax_amt / 2));
                purchaseInvoiceTable.setFinal_tax_amt(GlobalTranx.setDecimalFromStr(total_tax_amt));


            }

        }
        //call calculate gst function
        calculateGst(tableView, callback);

    }


}








