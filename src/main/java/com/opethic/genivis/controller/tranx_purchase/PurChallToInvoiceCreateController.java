
package com.opethic.genivis.controller.tranx_purchase;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.opethic.genivis.GenivisApplication;
import com.opethic.genivis.controller.dialogs.AdditionalCharges;
import com.opethic.genivis.controller.dialogs.BatchWindow;
import com.opethic.genivis.controller.dialogs.SingleInputDialogs;
import com.opethic.genivis.controller.master.ledger.common.LedgerMessageConsts;
import com.opethic.genivis.controller.tranx_sales.common.LineNumbersCellFactory;
import com.opethic.genivis.dto.CommonDTO;
import com.opethic.genivis.dto.GstDTO;
import com.opethic.genivis.dto.GstDetailsDTO;
import com.opethic.genivis.dto.add_charges.CustomAddChargesDTO;
import com.opethic.genivis.dto.cmp_t_row.BatchWindowTableDTO;
import com.opethic.genivis.dto.pur_invoice.*;
import com.opethic.genivis.dto.pur_invoice.reqres.*;
import com.opethic.genivis.dto.reqres.product.Communicator;
import com.opethic.genivis.dto.reqres.product.TableCellCallback;
import com.opethic.genivis.dto.reqres.pur_tranx.*;
import com.opethic.genivis.network.APIClient;
import com.opethic.genivis.network.EndPoints;
import com.opethic.genivis.network.RequestType;
import com.opethic.genivis.utils.*;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;
import javafx.stage.Stage;
import org.apache.http.util.TextUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URL;
import java.net.http.HttpResponse;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static com.opethic.genivis.utils.FxmFileConstants.*;
import static com.opethic.genivis.utils.Globals.decimalFormat;
import static com.opethic.genivis.utils.Globals.mapToStringforFormData;
import static javafx.scene.control.PopupControl.USE_COMPUTED_SIZE;
import static javafx.scene.control.PopupControl.USE_PREF_SIZE;

public class PurChallToInvoiceCreateController implements Initializable {
    @FXML
    public TabPane tranxPurChToInvTabPane;
    @FXML
    public Tab tabPurChToInvLedger;
    @FXML
    public Tab tabPurChToInvProduct;
    @FXML
    public Label lblPurChToInvMarginPer;
    @FXML
    public Label lblPurChToInvFsrmh;
    @FXML
    private HBox pc2iBottomMain, pc2iTopFirstRow, pc2iTopSecondRow;
    @FXML
    private VBox pc2iBottomFirstV, pc2iBottomSecondV, pc2iBottomThirdV, pc2iTotalMainDiv, pc2iTotalMainInnerDiv;

    @FXML
    public Label lblPurChToInvPurRate;
    @FXML
    public Label lblPurChToInvMfgDate;

    @FXML
    public Label lblPurChToInvBatchNo;
    @FXML
    public Tab tabPurInvBatch;
    @FXML
    public Label lbPurInvBatchCsrai;
    @FXML
    public Label lblPurChToInvExpDate;
    @FXML
    public Label lbPurInvBatchCsrmh;
    @FXML
    public Label lblPurChToInvMrp;
    @FXML
    public Label lblPurChToInvFsrai;
    @FXML
    public Label lblPurChToInvBarcode;
    @FXML
    public Label lblPurChToInvFreeqty;
    @FXML
    public Label lblPurChToInvCost;
    @FXML
    public Label lblPurChToInvCostWithTax, lblPurChallTotalQty, lblPurChallFreeQty;
    @FXML
    public CheckBox chbPurChToInvRoundOff;
    @FXML
    public Label lblPurChToInvRoundOff;
    @FXML
    public ComboBox<String> cmbPurChToInvPaymentMode;
    private ObservableList<String> payment_mode_list = FXCollections.observableArrayList("Credit", "Cash");

    @FXML
    BorderPane purChtoInvScrollPane;

    @FXML
    private TableView<GstDTO> tblvGST_Table;

    @FXML
    private TableColumn<GstDTO, String> tblc_gst;

    @FXML
    private TableColumn<GstDTO, String> tblc_cgst;

    @FXML
    private TableColumn<GstDTO, String> tblc_sgst;


    @FXML
    private TableView<PurchaseInvoiceTable> tblvPurChToInvTable;

    @FXML
    private TableColumn<PurchaseInvoiceTable, String> tcPurChToInvSrNo;

    @FXML
    private TableColumn<PurchaseInvoiceTable, String> tcPurChToInvParticulars;

    @FXML
    private TableColumn<PurchaseInvoiceTable, String> tcPurChToInvPackage;


    @FXML
    private TableColumn<PurchaseInvoiceTable, String> tcPurChToInvLevelA;


    @FXML
    private TableColumn<PurchaseInvoiceTable, String> tcPurChToInvLevelB;


    @FXML
    private TableColumn<PurchaseInvoiceTable, String> tcPurChToInvLevelC;


    @FXML
    private TableColumn<PurchaseInvoiceTable, String> tcPurChToInvUnit;


    @FXML
    private TableColumn<PurchaseInvoiceTable, String> tcPurChToInvBatch;

    @FXML
    private TableColumn<PurchaseInvoiceTable, String> tcPurChToInvQuantity;

    @FXML
    private TableColumn<PurchaseInvoiceTable, String> tcPurChToInvRate;

    @FXML
    private TableColumn<PurchaseInvoiceTable, String> tcPurChToInvGrossAmount;

    @FXML
    private TableColumn<PurchaseInvoiceTable, String> tcPurChToInvDisPer;

    @FXML
    private TableColumn<PurchaseInvoiceTable, String> tcPurChToInvDisAmt;

    @FXML
    private TableColumn<PurchaseInvoiceTable, String> tcPurChToInvTax;

    @FXML
    private TableColumn<PurchaseInvoiceTable, String> tcPurChToInvNetAmount;

    @FXML
    private TableColumn<PurchaseInvoiceTable, String> tcPurChToInvAction;

    JSONArray rowDelDetailsIds = new JSONArray();

    private final ObservableList<LevelAForPurInvoice> levelAList = FXCollections.observableArrayList();

    private final ObservableList<LevelBForPurInvoice> levelBList = FXCollections.observableArrayList();

    private final ObservableList<CommonDTO> levelCList = FXCollections.observableArrayList();

    private final ObservableList<CommonDTO> unitList = FXCollections.observableArrayList();

    @FXML
    private TextField tfPurChToInvTranxDate, tfPurChToInvInvoiceNo, tfPurChToInvLedgerName, tfPurChToInvInvoiceDate, tfPurChToInvPurchaseSerial, tfPurChToInvNarration;

    @FXML
    private ComboBox<CommonDTO> cmbPurChToInvPurchaseAc;

    @FXML
    private ComboBox<CommonDTO> cmbPurChToInvSupplierGSTIN;

    String purchaseAcID = "";

    String supplierGSTIN_ID = "";

    String ledger_id = "";

//    @FXML
//    RadioButton rbCredit, rbCash;

    private final Logger logger = LogManager.getLogger(PurchaseInvoiceCreateController.class);

    private final ObservableList<CommonDTO> purchaseAcList = FXCollections.observableArrayList();

    private final ObservableList<CommonDTO> supplierGSTINList = FXCollections.observableArrayList();

    private final ToggleGroup toggleGroup = new ToggleGroup();

    private String selectedPayMode = "Credit";

    @FXML
    private Label lblPurChToInvGrossTotal, lblPurChToInvDiscount, lblPurChToInvTotal, lblPurChToInvTax, lblPurChToInvBillAmount;

    Map<String, String> purchase_invoice_map = new HashMap<>();

    private String ledgerStateCode = "";
    private Boolean taxFlag = false;

    private String totalamt = "";

    private Double total_taxable_amt = 0.0;

    private String total_purchase_discount_amt = "";

    @FXML
    private TextField tfPurChToInvDisPropPer, tfPurChToInvDisPropAmt, tfPurChToInvAddCharges;
    String gst_no = "";
    List<CustomAddChargesDTO> addChargesDTOList = new ArrayList<>();

    @FXML
    private TableView<InvoiceProductHistoryTable> tblvPurChToInvPrdHist;

    @FXML
    private TableColumn<InvoiceProductHistoryTable, String> tblcPrdHistSupplierName;

    @FXML
    private TableColumn<InvoiceProductHistoryTable, String> tblcPrdHistInvNo;

    @FXML
    private TableColumn<InvoiceProductHistoryTable, String> tblcPrdHistInvDate;

    @FXML
    private TableColumn<InvoiceProductHistoryTable, String> tblcPrdHistBatch;

    @FXML
    private TableColumn<InvoiceProductHistoryTable, String> tblcPrdHistMRP;

    @FXML
    private TableColumn<InvoiceProductHistoryTable, String> tblcPrdHistQty;

    @FXML
    private TableColumn<InvoiceProductHistoryTable, String> tblcPrdHistRate;

    @FXML
    private TableColumn<InvoiceProductHistoryTable, String> tblcPrdHistCost;

    @FXML
    private TableColumn<InvoiceProductHistoryTable, String> tblcPrdHistDisPer;

    @FXML
    private TableColumn<InvoiceProductHistoryTable, String> tblcPrdHistDisAmt;

    private String purchase_id = "";

    private final Map<String, Object> edit_response_map = new HashMap<>();

    private final Map<String, String> invoice_data_map = new HashMap<>();

    private final Map<String, Object> purChToInv_response_map = new HashMap<>();

    private final Map<String, String> purChToInv_invoice_data_map = new HashMap<>();

    private final List<RowListDTO> rowListDTOS = new ArrayList<>();
    private final List<TranxPurRowResEditDTO> purChToInvRowListDTOS = new ArrayList<>();
    private final List<AdditionalCharge> addChargesResLst = new ArrayList<>();

    @FXML
    private Button btnPurChToInvModify, btnPurChToInvCancel, btnPurChToInvsubmit;
    @FXML
    private Label lblPurChToInvGstNo, lblPurChToInvArea, lblPurChToInvBank, lblPurChToInvContactPerson, lblPurChToInvTransportName,
            lblPurChToInvCreditDays, lblPurChToInvFssai, lblPurChToInvLicenseNo, lblPurChToInvRoute, lblPurChToInvProductBrand,
            lblPurChToInvProductGroup, lblPurChToInvProductCategory, lblPurChToInvProductSubGroup, lblPurChToInvProductHsn,
            lblPurChToInvProductTaxType, lblPurChToInvProductTaxPer, lblPurChToInvProductMarginPer, lblPurChToInvProductCost,
            lblPurChToInvProductShelfId, lblPurChToInvProductMinStock, lblPurChToInvProductMaxStock;
    private Long stateCode = 0L, CompanyStateCode = 0L;

//    public void setEditId(Integer id) {
//        System.out.println("id 1 " + id);
//    }

    public static String purChallToInvId = "";

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        resposiveScreenCss();
        tblvGST_Table.setFocusTraversable(false);
        tblvPurChToInvTable.setFocusTraversable(false);
        tblvPurChToInvPrdHist.setFocusTraversable(false);

        tblvPurChToInvPrdHist.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tblvGST_Table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
//        purChtoInvScrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

        // Set Purchase ID to Call Edit API
        if (!purChallToInvId.isEmpty() && purChallToInvId != null) {
            purchase_id = purChallToInvId;
            purChallToInvId = "";
            getPurChallToPurInvById(purchase_id);
        }

        // autofocus on Ledger name
        Platform.runLater(() -> {
            tfPurChToInvLedgerName.requestFocus();
        });
        // Initial Enter Functionality Method
        initialEnterMethod();

        // Get Payment modes
        getPaymentModes();

        //gst Table Design
        gstTableDesign();


        // Date Validation
        dateValidation();

        sceneInitilization();

        tfPurChToInvLedgerName.setEditable(true);

        //Ledger Name Validations
        tfLedgerNameValidation();

        // Invoice Number Validations
        tfPurChToInvInvoiceNo();

        // table Initialization
        tableInitiliazation();

        // getPurchase Accounts
        getPurchaseAc();

        //getPurchase Serial Number
        getPurchaseSerial();

        // On MouseClick Ledger Pop Up Open
        tfPurChToInvLedgerName.setOnMouseClicked(event -> {
            handletfPurChToInvLedgerName();
        });

        // On KeyTyped Ledger Pop Up Open
        tfPurChToInvLedgerName.setOnKeyTyped(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                handletfPurChToInvLedgerName();
            }
        });

        AutoCompleteBox<CommonDTO> autoCompleteBox = new AutoCompleteBox<>(cmbPurChToInvSupplierGSTIN, -1);

        if (!purchase_id.isEmpty() && purchase_id != null) {
            tfPurChToInvLedgerName.setText(ledgersById(Long.valueOf(purChToInv_invoice_data_map.get("supplierId"))));
            if (supplierGSTINList != null && !supplierGSTINList.isEmpty()) {
                cmbPurChToInvSupplierGSTIN.setItems(supplierGSTINList);
                cmbPurChToInvSupplierGSTIN.setValue(supplierGSTINList.get(0));
            }
        }

        if (!purchase_id.isEmpty() && purchase_id != null) {
            tfPurChToInvInvoiceNo.setText(purChToInv_invoice_data_map.get("invoice_no"));
        }

        if (!purchase_id.isEmpty() && purchase_id != null) {
            for (CommonDTO commonDTO : purchaseAcList) {
                if (commonDTO.getId().equals(purChToInv_invoice_data_map.get("purchase_account_ledger_id"))) {
                    cmbPurChToInvPurchaseAc.setValue(commonDTO);
                    purchaseAcID = commonDTO.getId();
                }
            }
        }


        discPropDataSetInEdit();

        invoiceProductHistoryTable();

        btnPurChToInvModify.setOnAction(actionEvent -> {
            GlobalController.getInstance().addTabStatic(PURCHASE_CHALLAN_LIST_SLUG, false);
        });

        btnPurChToInvCancel.setOnAction(actionEvent -> {
            GlobalController.getInstance().addTabStatic(PURCHASE_CHALLAN_LIST_SLUG, false);
        });

        chbPurChToInvRoundOff.setOnAction(e -> {
            roundOffCalculations();
        });

        cmbPurChToInvPaymentMode.getSelectionModel().select("Credit");
        cmbPurChToInvPaymentMode.focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if (newValue) {
                    cmbPurChToInvPaymentMode.show();
                }
            }
        });

        tfPurChToInvAddCharges.setEditable(false);
        tfPurChToInvAddCharges.setFocusTraversable(false);
        btnPurChToInvCancel.setOnAction(actionEvent -> {
            AlertUtility.AlertConfirmation(AlertUtility.alertTypeConfirmation, LedgerMessageConsts.msgGoList, input -> {
                if (input == 1) {
                    GlobalController.getInstance().addTabStatic(PURCHASE_CHALLAN_LIST_SLUG, false);
                }
            });
        });

    }

    private void resposiveScreenCss() {
        // For Responsive Resolutions Css Style Apply
        double height = Screen.getPrimary().getBounds().getHeight();
        double width = Screen.getPrimary().getBounds().getWidth();
        System.out.println("width" + width);
        if (width >= 992 && width <= 1024) {
            pc2iTopFirstRow.setSpacing(2);
            pc2iTopSecondRow.setSpacing(2);
            pc2iTotalMainDiv.setSpacing(5);
            pc2iTotalMainInnerDiv.setSpacing(5);
            // Set preferred widths as percentages of the HBox width
            pc2iBottomFirstV.prefWidthProperty().bind(pc2iBottomMain.widthProperty().multiply(0.53));
            pc2iBottomSecondV.prefWidthProperty().bind(pc2iBottomMain.widthProperty().multiply(0.25));
            pc2iBottomThirdV.prefWidthProperty().bind(pc2iBottomMain.widthProperty().multiply(0.22));
//            so2cBottomMain.setPrefHeight(150);
            tblvGST_Table.getStylesheets().add(GenivisApplication.class.getResource("ui/css/purchase_challan_css/gst_table_css/gst_table_992_1024.css").toExternalForm());
            tblvPurChToInvPrdHist.getStylesheets().add(GenivisApplication.class.getResource("ui/css/purchase_challan_css/supplier_table_css/supplier_table_992_1024.css").toExternalForm());
            purChtoInvScrollPane.getStylesheets().add(GenivisApplication.class.getResource("ui/css/TranxCommonCssStyles/tranxCommonCssStyle1.css").toExternalForm());
        } else if (width >= 1025 && width <= 1280) {
            pc2iTopFirstRow.setSpacing(12);
            pc2iTopSecondRow.setSpacing(12);
            pc2iTotalMainDiv.setSpacing(12);
            pc2iTotalMainInnerDiv.setSpacing(8);
            // Set preferred widths as percentages of the HBox width
            pc2iBottomFirstV.prefWidthProperty().bind(pc2iBottomMain.widthProperty().multiply(0.56));
            pc2iBottomSecondV.prefWidthProperty().bind(pc2iBottomMain.widthProperty().multiply(0.22));
            pc2iBottomThirdV.prefWidthProperty().bind(pc2iBottomMain.widthProperty().multiply(0.22));
//            so2cBottomMain.setPrefHeight(150);
            tblvGST_Table.getStylesheets().add(GenivisApplication.class.getResource("ui/css/purchase_challan_css/gst_table_css/gst_table_1025_1280.css").toExternalForm());
            tblvPurChToInvPrdHist.getStylesheets().add(GenivisApplication.class.getResource("ui/css/purchase_challan_css/supplier_table_css/supplier_table_1025_1280.css").toExternalForm());
            purChtoInvScrollPane.getStylesheets().add(GenivisApplication.class.getResource("ui/css/TranxCommonCssStyles/tranxCommonCssStyle2.css").toExternalForm());
        } else if (width >= 1281 && width <= 1368) {
            pc2iTopFirstRow.setSpacing(12);
            pc2iTopSecondRow.setSpacing(12);
            pc2iTotalMainDiv.setSpacing(12);
            pc2iTotalMainInnerDiv.setSpacing(10);
            // Set preferred widths as percentages of the HBox width
            pc2iBottomFirstV.prefWidthProperty().bind(pc2iBottomMain.widthProperty().multiply(0.6));
            pc2iBottomSecondV.prefWidthProperty().bind(pc2iBottomMain.widthProperty().multiply(0.20));
            pc2iBottomThirdV.prefWidthProperty().bind(pc2iBottomMain.widthProperty().multiply(0.20));
//            so2cBottomMain.setPrefHeight(150);
            tblvGST_Table.getStylesheets().add(GenivisApplication.class.getResource("ui/css/purchase_challan_css/gst_table_css/gst_table_1281_1368.css").toExternalForm());
            tblvPurChToInvPrdHist.getStylesheets().add(GenivisApplication.class.getResource("ui/css/purchase_challan_css/supplier_table_css/supplier_table_1281_1368.css").toExternalForm());
            purChtoInvScrollPane.getStylesheets().add(GenivisApplication.class.getResource("ui/css/TranxCommonCssStyles/tranxCommonCssStyle3.css").toExternalForm());
        } else if (width >= 1369 && width <= 1400) {
            pc2iTopFirstRow.setSpacing(15);
            pc2iTopSecondRow.setSpacing(15);
            pc2iTotalMainDiv.setSpacing(15);
            pc2iTotalMainInnerDiv.setSpacing(10);
            // Set preferred widths as percentages of the HBox width
            pc2iBottomFirstV.prefWidthProperty().bind(pc2iBottomMain.widthProperty().multiply(0.57));
            pc2iBottomSecondV.prefWidthProperty().bind(pc2iBottomMain.widthProperty().multiply(0.22));
            pc2iBottomThirdV.prefWidthProperty().bind(pc2iBottomMain.widthProperty().multiply(0.21));
//            so2cBottomMain.setPrefHeight(150);
            tblvGST_Table.getStylesheets().add(GenivisApplication.class.getResource("ui/css/purchase_challan_css/gst_table_css/gst_table_1369_1400.css").toExternalForm());
            tblvPurChToInvPrdHist.getStylesheets().add(GenivisApplication.class.getResource("ui/css/purchase_challan_css/supplier_table_css/supplier_table_1369_1400.css").toExternalForm());
            purChtoInvScrollPane.getStylesheets().add(GenivisApplication.class.getResource("ui/css/TranxCommonCssStyles/tranxCommonCssStyle4.css").toExternalForm());
        } else if (width >= 1401 && width <= 1440) {
            pc2iTopFirstRow.setSpacing(15);
            pc2iTopSecondRow.setSpacing(15);
            pc2iTotalMainDiv.setSpacing(15);
            pc2iTotalMainInnerDiv.setSpacing(10);
            // Set preferred widths as percentages of the HBox width
            pc2iBottomFirstV.prefWidthProperty().bind(pc2iBottomMain.widthProperty().multiply(0.57));
            pc2iBottomSecondV.prefWidthProperty().bind(pc2iBottomMain.widthProperty().multiply(0.22));
            pc2iBottomThirdV.prefWidthProperty().bind(pc2iBottomMain.widthProperty().multiply(0.21));
//            so2cBottomMain.setPrefHeight(150);
            tblvGST_Table.getStylesheets().add(GenivisApplication.class.getResource("ui/css/purchase_challan_css/gst_table_css/gst_table_1401_1440.css").toExternalForm());
            tblvPurChToInvPrdHist.getStylesheets().add(GenivisApplication.class.getResource("ui/css/purchase_challan_css/supplier_table_css/supplier_table_1401_1440.css").toExternalForm());
            purChtoInvScrollPane.getStylesheets().add(GenivisApplication.class.getResource("ui/css/TranxCommonCssStyles/tranxCommonCssStyle5.css").toExternalForm());
        } else if (width >= 1441 && width <= 1680) {
            pc2iTopFirstRow.setSpacing(20);
            pc2iTopSecondRow.setSpacing(20);
            pc2iTotalMainDiv.setSpacing(20);
            pc2iTotalMainInnerDiv.setSpacing(10);
            // Set preferred widths as percentages of the HBox width
            pc2iBottomFirstV.prefWidthProperty().bind(pc2iBottomMain.widthProperty().multiply(0.6));
            pc2iBottomSecondV.prefWidthProperty().bind(pc2iBottomMain.widthProperty().multiply(0.20));
            pc2iBottomThirdV.prefWidthProperty().bind(pc2iBottomMain.widthProperty().multiply(0.20));
//            scBottomMain.setPrefHeight(150);
            tblvGST_Table.getStylesheets().add(GenivisApplication.class.getResource("ui/css/purchase_challan_css/gst_table_css/gst_table_1441_1680.css").toExternalForm());
            tblvPurChToInvPrdHist.getStylesheets().add(GenivisApplication.class.getResource("ui/css/purchase_challan_css/supplier_table_css/supplier_table_1441_1680.css").toExternalForm());
            purChtoInvScrollPane.getStylesheets().add(GenivisApplication.class.getResource("ui/css/TranxCommonCssStyles/tranxCommonCssStyle6.css").toExternalForm());
        } else if (width >= 1681 && width <= 1920) {
            pc2iTopFirstRow.setSpacing(20);
            pc2iTopSecondRow.setSpacing(20);
            pc2iTotalMainDiv.setSpacing(20);
            pc2iTotalMainInnerDiv.setSpacing(10);
            // Set preferred widths as percentages of the HBox width
            pc2iBottomFirstV.prefWidthProperty().bind(pc2iBottomMain.widthProperty().multiply(0.6));
            pc2iBottomSecondV.prefWidthProperty().bind(pc2iBottomMain.widthProperty().multiply(0.18));
            pc2iBottomThirdV.prefWidthProperty().bind(pc2iBottomMain.widthProperty().multiply(0.22));
//            scBottomMain.setPrefHeight(150);
            tblvGST_Table.getStylesheets().add(GenivisApplication.class.getResource("ui/css/gst_table.css").toExternalForm());
            tblvPurChToInvPrdHist.getStylesheets().add(GenivisApplication.class.getResource("ui/css/invoice_product_history_table.css").toExternalForm());
            purChtoInvScrollPane.getStylesheets().add(GenivisApplication.class.getResource("ui/css/TranxCommonCssStyles/tranxCommonCssStyle7.css").toExternalForm());

        }
    }

    public void tfLedgerNameValidation() {
        tfPurChToInvLedgerName.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal) {
                if (tfPurChToInvLedgerName.getText().isEmpty()) {
                    tfPurChToInvLedgerName.requestFocus();
                }

            }
        });

    }

    private void tfPurChToInvInvoiceNo() {
        tfPurChToInvInvoiceNo.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER || event.getCode() == KeyCode.TAB) {
                if (!tfPurChToInvInvoiceNo.getText().isEmpty()) {

                    validatePurchaseInvoiceNo();
                } else {
                    tfPurChToInvInvoiceNo.requestFocus();
                }
            }
        });
    }

    private void gstTableDesign() {
        tblvGST_Table.setFocusTraversable(false);
        tblc_gst.setSortable(false);
        tblc_cgst.setSortable(false);
        tblc_sgst.setSortable(false);

        tblc_gst.setCellValueFactory(cellData -> {
            StringProperty taxProperty = cellData.getValue().taxPerProperty();
            return Bindings.createStringBinding(
                    () -> "(" + (int) Double.parseDouble(taxProperty.get()) + " %)",
                    taxProperty
            );
        });

        tblc_gst.setStyle("-fx-alignment: CENTER;");
        tblc_cgst.setCellValueFactory(cellData -> cellData.getValue().cgstProperty());
        tblc_cgst.setCellFactory(col -> new TableCell<GstDTO, String>() {
            private final Label label = new Label();
            private final VBox vBox = new VBox(label);

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


        tblc_cgst.setStyle("-fx-alignment: CENTER;");
        tblc_sgst.setCellValueFactory(cellData -> cellData.getValue().sgstProperty());
        tblc_sgst.setStyle("-fx-alignment: CENTER;");

        tblc_sgst.setCellFactory(col -> new TableCell<GstDTO, String>() {
            private final Label label = new Label();
            private final VBox vBox = new VBox(label);

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

        tblvGST_Table.getItems().addAll(new GstDTO("28", "12345678", "12345678", ""));


    }

    private void dateValidation() {

        LocalDate current_date = LocalDate.now();

        if (!purchase_id.isEmpty() && purchase_id != null) {
            LocalDate currentDate = LocalDate.now();
            tfPurChToInvTranxDate.setText(currentDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));

            LocalDate invoice_dt = LocalDate.parse(purChToInv_invoice_data_map.get("invoice_dt"));
            tfPurChToInvInvoiceDate.setText(invoice_dt.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        } else {
            tfPurChToInvTranxDate.setText(current_date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
            DateValidator.applyDateFormat(tfPurChToInvInvoiceDate);
            LocalDate currentDate = LocalDate.now();
            tfPurChToInvInvoiceDate.setText(currentDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
            sceneInitilization();
        }
    }

    private void getPaymentModes() {
        cmbPurChToInvPaymentMode.setItems(payment_mode_list);
    }

    private void initialEnterMethod() {
        purChtoInvScrollPane.addEventFilter(KeyEvent.KEY_PRESSED, (KeyEvent event) -> {
            if (event.getCode() == KeyCode.ENTER) {
                if (event.getTarget() instanceof Button targetButton && targetButton.getText().equalsIgnoreCase("modify")) {
                } else if (event.getTarget() instanceof Button targetButton && targetButton.getText().equalsIgnoreCase("submit")) {
                } else if (event.getTarget() instanceof Button targetButton && targetButton.getText().equalsIgnoreCase("cancel")) {
                } else {
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
            }else if (event.isControlDown() && event.getCode() == KeyCode.S) {
                invoiceValidations();
            }else if(event.getCode()==KeyCode.X && event.isControlDown()){
                btnPurChToInvCancel.fire();
            }
        });

    }

    private void discPropDataSetInEdit() {

        if (!purchase_id.isEmpty() && purchase_id != null) {
            Double dis_per_float = (Double) purChToInv_response_map.get("discountInPer");
            tfPurChToInvDisPropPer.setText(String.valueOf(dis_per_float));
            String discText = tfPurChToInvDisPropPer.getText();
            if (!TextUtils.isEmpty(discText)) {
                double totalTaxableAmt = 0.0;
                for (int i = 0; i < tblvPurChToInvTable.getItems().size(); i++) {
                    PurchaseInvoiceCalculation.rowCalculationForPurcInvoice(i, tblvPurChToInvTable, callback);//call row calculation function
                    PurchaseInvoiceTable purchaseInvoiceTable = tblvPurChToInvTable.getItems().get(i);
                    totalTaxableAmt = totalTaxableAmt + Double.parseDouble(purchaseInvoiceTable.getTaxable_amt());
                }
                double disc_per = Double.parseDouble(discText);
                Double amount = (totalTaxableAmt * disc_per) / 100;
                tfPurChToInvDisPropAmt.setText(String.valueOf(amount));
                PurchaseInvoiceCalculation.discountPropotionalCalculation(disc_per + "", "0", tfPurChToInvAddCharges.getText(), tblvPurChToInvTable, callback);
                if (!tfPurChToInvAddCharges.getText().isEmpty()) {
                    PurchaseInvoiceCalculation.additionalChargesCalculation(tfPurChToInvAddCharges.getText(), tblvPurChToInvTable, callback);
                }
            } else {
                tfPurChToInvDisPropAmt.setText("");
                PurchaseInvoiceCalculation.discountPropotionalCalculation("0", "0", tfPurChToInvAddCharges.getText(), tblvPurChToInvTable, callback);
                if (!tfPurChToInvAddCharges.getText().isEmpty()) {
                    PurchaseInvoiceCalculation.additionalChargesCalculation(tfPurChToInvAddCharges.getText(), tblvPurChToInvTable, callback);
                }
            }

            //? Calculate Tax for each Row
            PurchaseInvoiceCalculation.calculateGst(tblvPurChToInvTable, callback);


        }
    }


    TableCellCallback<Object[]> productID_callback = item -> {
        if (item.length == 1) {
            tranxProductDetailsFun((String) item[0]);
            logger.debug("tranxBatchDetailsFun() started");
            getSupplierListbyProductId((String) item[0]);
        } else {
            logger.debug("tranxBatchDetailsFun() started");
            tranxBatchDetailsFun((String) item[0], (String) item[1]);
        }
    };

    /**
     * Info :  Set the SUpplier Details of the Selected Product Bottom in Supplier Table
     *
     * @Author : Ashwin Shendre
     */

    private void getSupplierListbyProductId(String id) {
        APIClient apiClient = null;
        try {
            Map<String, String> map = new HashMap<>();
            map.put("productId", id);
            String formData = Globals.mapToStringforFormData(map);
            logger.debug("Form Data for getSupplierListbyProductId in Purchase Invoice Controller:" + formData);
            logger.debug("Network call started getSupplierListbyProductId() in Purchase Invoice Controller:");
            apiClient = new APIClient(EndPoints.GET_SUPPLIERLIST_BY_PRODUCTID_ENDPOINT, formData, RequestType.FORM_DATA);
            apiClient.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
                @Override
                public void handle(WorkerStateEvent workerStateEvent) {
                    JsonObject jsonObject = new Gson().fromJson(workerStateEvent.getSource().getValue().toString(), JsonObject.class);
                    if (jsonObject.get("responseStatus").getAsInt() == 200) {
                        JsonArray dataArray = jsonObject.getAsJsonArray("data");
                        System.out.println("dataArray >> : " + dataArray);
                        //todo: getting values
                        ObservableList<InvoiceProductHistoryTable> supplierDataList = FXCollections.observableArrayList();
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
                            supplierDataList.add(new InvoiceProductHistoryTable(supplierName, supplierInvNo, supplierInvDate, supplierBatch, supplierMrp, supplierRate, supplierQty, supplierCost, supplierDisPer, supplierDisAmt));
                        }
                        tblvPurChToInvPrdHist.setItems(supplierDataList);

                        logger.debug("Network call Successed getSupplierListbyProductId() in Purchase Invoice Controller:");
                    }

                }
            });
            apiClient.setOnCancelled(new EventHandler<WorkerStateEvent>() {
                @Override
                public void handle(WorkerStateEvent workerStateEvent) {
                    logger.error("Network API cancelled in getSupplierListbyProductId()" + workerStateEvent.getSource().getValue().toString());
                }
            });
            apiClient.setOnFailed(new EventHandler<WorkerStateEvent>() {
                @Override
                public void handle(WorkerStateEvent workerStateEvent) {
                    logger.error("Network API failed in getSupplierListbyProductId()" + workerStateEvent.getSource().getValue().toString());
                }
            });
            apiClient.start();
        } catch (Exception e) {
            System.out.println("exception:" + e);
        } finally {
            apiClient = null;
        }
        logger.debug("tranxBatchDetailsFun() Ended");
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
        if (ledgerStateCode.equalsIgnoreCase(GlobalTranx.getCompanyStateCode().toString())) {
            taxFlag = true;
        }
        return ledgerName;
    }

    private void invoiceProductHistoryTable() {

        tblvPurChToInvPrdHist.setFocusTraversable(false);

        tblcPrdHistSupplierName.setCellValueFactory(cellData -> cellData.getValue().supplier_nameProperty());
        tblcPrdHistSupplierName.setStyle("-fx-alignment: CENTER;");
        tblcPrdHistInvNo.setCellValueFactory(cellData -> cellData.getValue().invoice_noProperty());
        tblcPrdHistInvNo.setStyle("-fx-alignment: CENTER;");
        tblcPrdHistInvDate.setCellValueFactory(cellData -> cellData.getValue().invoice_dateProperty());
        tblcPrdHistInvDate.setStyle("-fx-alignment: CENTER;");
        tblcPrdHistBatch.setCellValueFactory(cellData -> cellData.getValue().batchProperty());
        tblcPrdHistBatch.setStyle("-fx-alignment: CENTER;");
        tblcPrdHistMRP.setCellValueFactory(cellData -> cellData.getValue().mrpProperty());
        tblcPrdHistMRP.setStyle("-fx-alignment: CENTER;");
        tblcPrdHistQty.setCellValueFactory(cellData -> cellData.getValue().qtyProperty());
        tblcPrdHistQty.setStyle("-fx-alignment: CENTER;");
        tblcPrdHistRate.setCellValueFactory(cellData -> cellData.getValue().rateProperty());
        tblcPrdHistRate.setStyle("-fx-alignment: CENTER;");
        tblcPrdHistCost.setCellValueFactory(cellData -> cellData.getValue().costProperty());
        tblcPrdHistCost.setStyle("-fx-alignment: CENTER;");
        tblcPrdHistDisPer.setCellValueFactory(cellData -> cellData.getValue().dis_perProperty());
        tblcPrdHistDisPer.setStyle("-fx-alignment: CENTER;");
        tblcPrdHistDisAmt.setCellValueFactory(cellData -> cellData.getValue().dis_amtProperty());
        tblcPrdHistDisAmt.setStyle("-fx-alignment: CENTER;");
    }

    public void sceneInitilization() {
        purChtoInvScrollPane.sceneProperty().addListener((observable, oldScene, newScene) -> {
            if (newScene != null && newScene.getWindow() instanceof Stage) {
                Communicator.stage = (Stage) newScene.getWindow();
            }
        });
    }

    public void handlePayAction(ActionEvent actionEvent) {
        RadioButton selectedRadioButton = (RadioButton) toggleGroup.getSelectedToggle();
        if (selectedRadioButton != null) {
            selectedPayMode = selectedRadioButton.getText();
        }
    }


    public void handletfPurChToInvLedgerName() {
        Stage stage = (Stage) purChtoInvScrollPane.getScene().getWindow();
        SingleInputDialogs.openLedgerPopUp(stage, "Creditors", input -> {
            supplierGSTINList.clear();
            System.out.println("input ledger selection =>" + input[3]);
            tfPurChToInvLedgerName.setText(input[0].toString());
            ledger_id = (String) input[1];
            ledgerStateCode = (String) input[3];
            tblvPurChToInvTable.getItems().get(0).setLedger_id(ledger_id);
            @SuppressWarnings("unchecked")
            ObservableList<GstDetailsDTO> gstDetailsDTOS = (ObservableList<GstDetailsDTO>) input[2];

            for (GstDetailsDTO gstDetailsDTO : gstDetailsDTOS) {
                supplierGSTINList.add(new CommonDTO(gstDetailsDTO.getGstNo(), gstDetailsDTO.getId()));
            }
            if (supplierGSTINList != null && !supplierGSTINList.isEmpty()) {
                cmbPurChToInvSupplierGSTIN.setItems(supplierGSTINList);
                cmbPurChToInvSupplierGSTIN.setValue(supplierGSTINList.get(0));
                if (supplierGSTINList.size() > 1) {
                    cmbPurChToInvSupplierGSTIN.requestFocus();
                } else {
                    tfPurChToInvInvoiceNo.requestFocus();
                }
            } else {
                tfPurChToInvInvoiceNo.requestFocus();
            }


            if (ledgerStateCode.equalsIgnoreCase(GlobalTranx.getCompanyStateCode().toString())) {
                taxFlag = true;
            }
            tranxLedgerDetailsFun(ledger_id);
        }, in -> {
            System.out.println("In >> Called >. " + in);
            if (in == true) {
//                            isLedgerRed = true;
//                            setPurChallDataToProduct();
            }
        });
    }

    private void tranxLedgerDetailsFun(String id) {
        tranxPurChToInvTabPane.getSelectionModel().select(tabPurChToInvLedger);

        APIClient apiClient = null;
        try {
            Map<String, String> map = new HashMap<>();
            map.put("ledger_id", id);
            String formData = Globals.mapToStringforFormData(map);
            apiClient = new APIClient(EndPoints.TRANSACTION_LEDGER_DETAILS, formData, RequestType.FORM_DATA);
            apiClient.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
                @Override
                public void handle(WorkerStateEvent workerStateEvent) {
                    JsonObject jsonObject = new Gson().fromJson(workerStateEvent.getSource().getValue().toString(), JsonObject.class);
                    if (jsonObject.get("responseStatus").getAsInt() == 200) {
                        JsonObject jsonObj = jsonObject.getAsJsonObject("result");
                        CompanyStateCode = jsonObject.get("company_state_code").getAsLong();
                        stateCode = jsonObj.get("stateCode").getAsLong();

                        lblPurChToInvGstNo.setText(jsonObj.get("gst_number").getAsString());
                        lblPurChToInvArea.setText(jsonObj.get("area").getAsString());
                        lblPurChToInvBank.setText(jsonObj.get("bank_name").getAsString());
                        lblPurChToInvContactPerson.setText(jsonObj.get("contact_name").getAsString());
                        lblPurChToInvCreditDays.setText(jsonObj.get("credit_days").getAsString());
                        lblPurChToInvFssai.setText(jsonObj.get("fssai_number").getAsString());
                        lblPurChToInvLicenseNo.setText(jsonObj.get("license_number").getAsString());
                        lblPurChToInvRoute.setText(jsonObj.get("route").getAsString());
                    }
                }
            });
            apiClient.setOnCancelled(new EventHandler<WorkerStateEvent>() {
                @Override
                public void handle(WorkerStateEvent workerStateEvent) {
                    logger.error("Network API cancelled in tranxLedgerDetailsFun() Pur Invoice" + workerStateEvent.getSource().getValue().toString());
                }
            });
            apiClient.setOnFailed(new EventHandler<WorkerStateEvent>() {
                @Override
                public void handle(WorkerStateEvent workerStateEvent) {
                    logger.error("Network API failed in tranxLedgerDetailsFun() Pur Invoice" + workerStateEvent.getSource().getValue().toString());
                }
            });
            apiClient.start();
        } catch (Exception e) {
            System.out.println("exception:" + e);
        } finally {
            apiClient = null;
        }

    }


    private void nodetraversal(Node current_node, Node next_node) {
        current_node.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                next_node.requestFocus();
                event.consume();
            } else if (event.getCode() == KeyCode.SPACE) {
                if (current_node instanceof TextField textField) {
                    if (textField.getId().equals("tfPurChToInvLedgerName")) {
                        handletfPurChToInvLedgerName();
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
                cmbPurChToInvPurchaseAc.setItems(purchaseAcList);
                cmbPurChToInvPurchaseAc.getSelectionModel().selectFirst();
                AutoCompleteBox<CommonDTO> autoCompleteBox = new AutoCompleteBox<>(cmbPurChToInvPurchaseAc, 0);

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
                tfPurChToInvPurchaseSerial.setText(String.valueOf(purchaseSerialRes.getCount()));
            } else {
                System.out.println("responseObject is null");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void setcmbPurChToInvPurchaseAc(ActionEvent actionEvent) {
        Object[] object = new Object[1];
        object[0] = cmbPurChToInvPurchaseAc.getSelectionModel().getSelectedItem();
        if (object[0] != null) {
            for (CommonDTO commonDTO : purchaseAcList) {
                if (object[0].equals(commonDTO)) {
                    purchaseAcID = commonDTO.getId();
                }
            }
        }
        nodetraversal(cmbPurChToInvSupplierGSTIN, tfPurChToInvInvoiceNo);
    }

    TableCellCallback<Object[]> callback = item -> {

        System.out.println("item: " + item[0]);
        System.out.println("item: " + item[1]);
        System.out.println("item: " + item[2]);
        System.out.println("item: " + item[3]);
        System.out.println("item: " + item[4]);
        System.out.println("item: " + item[5]);
        System.out.println("item: " + item[6]);
        System.out.println("item: " + item[7]);
        System.out.println("item: " + item[8]);

        System.out.println("taxCalculation " + item[9]);

        tblvGST_Table.getItems().clear();
        ObservableList<GstDTO> gstDTOObservableList = FXCollections.observableArrayList();
        ObservableList<?> item9List = (ObservableList<?>) item[9];
        for (Object obj : item9List) {
            if (obj instanceof GstDTO) {
                gstDTOObservableList.add((GstDTO) obj);
            }
        }

        tblvGST_Table.getItems().addAll(gstDTOObservableList);

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
        purchase_invoice_map.put("taxCalculation", jsonObject.toString());

        lblPurChToInvBillAmount.setText((String) item[2]); //totalNetAmt for bill amount
        totalamt = (String) item[2];

        total_purchase_discount_amt = (String) item[4];

        System.out.println("ledgerStateCode =>" + ledgerStateCode + "GlobalTranx.getCompanyStateCode() =>" + GlobalTranx.getCompanyStateCode());


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


        total_taxable_amt = Double.parseDouble((String) item[5]);


        lblPurChToInvGrossTotal.setText((String) item[3]);
        lblPurChToInvDiscount.setText((String) item[4]);
        lblPurChToInvTotal.setText((String) item[5]);
        lblPurChToInvTax.setText((String) item[6]);
        chbPurChToInvRoundOff.setSelected(true);
        roundOffCalculations();
    };

    private void roundOffCalculations() {
        Double billamt = 0.00; // local variable to store row wise  Bill amount
        Double total_bill_amt = 0.00; // local variable to store Final bill Amount without roundoff
        Double final_r_off_bill_amt = 0.00; // local variable to store Final Bill amount with round off
        Double roundOffAmt = 0.00; // local variable to store Roundoff Amount
        for (PurchaseInvoiceTable cmpTRowDTO : tblvPurChToInvTable.getItems()) {
            billamt = Double.valueOf(cmpTRowDTO.getNet_amount());
            total_bill_amt += billamt;
        }
        final_r_off_bill_amt = (double) Math.round(total_bill_amt);
        roundOffAmt = final_r_off_bill_amt - total_bill_amt;

        if (chbPurChToInvRoundOff.isSelected()) {
            lblPurChToInvBillAmount.setText(String.format("%.2f", final_r_off_bill_amt));
            lblPurChToInvRoundOff.setText(String.format("%.2f", roundOffAmt));
        } else {
            lblPurChToInvBillAmount.setText(String.format("%.2f", total_bill_amt));
            lblPurChToInvRoundOff.setText("0.00");
        }

    }


    public void tableInitiliazation() {

        tblvPurChToInvTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tblvPurChToInvTable.setEditable(true);
        tblvPurChToInvTable.setFocusTraversable(false);


        Label headerLabel = new Label("Sr\nNo.");
        tcPurChToInvSrNo.setGraphic(headerLabel);

        if (!purchase_id.isEmpty() && purchase_id != null) {

            int count = 1;
            int index = 0;
            Double qty = 0.0;
            Double totalQty = 0.0;
            Double freeQty = 0.0;
            Double totalFreeQty = 0.0;
            for (TranxPurRowResEditDTO rowListDTO : purChToInvRowListDTOS) {

                PurchaseInvoiceTable purchaseInvoiceTable = new PurchaseInvoiceTable();
                purchaseInvoiceTable.setPurchase_id(String.valueOf(purchase_id));
                purchaseInvoiceTable.setSr_no(String.valueOf(count));
                purchaseInvoiceTable.setDetails_id("0.0");
                purchaseInvoiceTable.setProduct_id(String.valueOf(rowListDTO.getProductId()));
                purchaseInvoiceTable.setParticulars(String.valueOf(rowListDTO.getProductName()));

                purchaseInvoiceTable.setLevelA_id(String.valueOf(rowListDTO.getLevelAId()));
                purchaseInvoiceTable.setLevelB_id(String.valueOf(rowListDTO.getLevelBId()));
                purchaseInvoiceTable.setLevelC_id(String.valueOf(rowListDTO.getLevelCId()));
                purchaseInvoiceTable.setUnit_id(String.valueOf(rowListDTO.getUnitId()));
                purchaseInvoiceTable.setUnit_conv(String.valueOf(rowListDTO.getUnitConv()));

                purchaseInvoiceTable.setPackages(String.valueOf(rowListDTO.getPacking()));
                purchaseInvoiceTable.setPackages(String.valueOf(rowListDTO.getPackName()));

                qty = rowListDTO.getQty();
                totalQty += qty;
                purchaseInvoiceTable.setQuantity(String.valueOf(rowListDTO.getQty()));

                freeQty = rowListDTO.getFreeQty();
                totalFreeQty += freeQty;
                purchaseInvoiceTable.setFree(String.valueOf(rowListDTO.getFreeQty()));

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
                purchaseInvoiceTable.setRow_dis_amt(String.valueOf(rowListDTO.getRowDisAmt()));

                purchaseInvoiceTable.setGross_amount(String.valueOf(rowListDTO.getGrossAmt()));
                purchaseInvoiceTable.setGross_amount1(String.valueOf(rowListDTO.getGrossAmt()));

                purchaseInvoiceTable.setAdd_chg_amt(String.valueOf(rowListDTO.getAddChgAmt()));


                purchaseInvoiceTable.setInvoice_dis_amt(String.valueOf(rowListDTO.getInvoiceDisAmt()));
                purchaseInvoiceTable.setInventoryId(String.valueOf(rowListDTO.getInventoryId()));

                purchaseInvoiceTable.setTransaction_status(String.valueOf(rowListDTO.getTransactionStatus()));

                purchaseInvoiceTable.setIs_expired(String.valueOf(rowListDTO.getExpired()));


                purchaseInvoiceTable.setB_details_id(String.valueOf(rowListDTO.getbDetailsId()));

                purchaseInvoiceTable.setB_no(String.valueOf(rowListDTO.getBatchNo()));
                purchaseInvoiceTable.setBatch_or_serial(String.valueOf(rowListDTO.getBatchNo()));
                purchaseInvoiceTable.setB_expiry(String.valueOf(rowListDTO.getbExpiry()));
                purchaseInvoiceTable.setB_rate(String.valueOf(rowListDTO.getbRate()));
                purchaseInvoiceTable.setB_purchase_rate(String.valueOf(rowListDTO.getPurchaseRate()));
                purchaseInvoiceTable.setIs_batch(String.valueOf(rowListDTO.getBatch()));
                purchaseInvoiceTable.setReference_id(String.valueOf(rowListDTO.getReferenceId()));
                purchaseInvoiceTable.setReference_type(String.valueOf(rowListDTO.getReferenceType()));

                purchaseInvoiceTable.setRate_a(String.valueOf(rowListDTO.getMinRateA() == null ? "" : rowListDTO.getMinRateA()));
                purchaseInvoiceTable.setRate_b(String.valueOf(rowListDTO.getMinRateB() == null ? "" : rowListDTO.getMinRateB()));
                purchaseInvoiceTable.setRate_c(String.valueOf(rowListDTO.getMinRateC() == null ? "" : rowListDTO.getMinRateB()));
                // purchaseInvoiceTable.setRate_d(String.valueOf(rowListDTO.getMinRateD()));

                purchaseInvoiceTable.setManufacturing_date(String.valueOf(rowListDTO.getManufacturingDate()));
                purchaseInvoiceTable.setMin_margin(String.valueOf(rowListDTO.getMarginPer()));

                purchaseInvoiceTable.setCosting(String.valueOf(rowListDTO.getCosting()));
                purchaseInvoiceTable.setCosting_with_tax(String.valueOf(rowListDTO.getCosting()));
                purchaseInvoiceTable.setLedger_id(String.valueOf(rowListDTO.getCosting()));
                purchaseInvoiceTable.setLedger_id(purChToInv_invoice_data_map.get("supplierId"));
                purchaseInvoiceTable.setTax(String.valueOf(rowListDTO.getGst()));


                BatchWindowTableDTO batchWindowTableDTO = new BatchWindowTableDTO();

                batchWindowTableDTO.setProductId(purchaseInvoiceTable.getProduct_id());
                batchWindowTableDTO.setLevelAId(purchaseInvoiceTable.getLevelA_id());
                batchWindowTableDTO.setLevelBId(purchaseInvoiceTable.getLevelB_id());
                batchWindowTableDTO.setLevelCId(purchaseInvoiceTable.getLevelC_id());
                batchWindowTableDTO.setUnitId(purchaseInvoiceTable.getUnit_id());

                batchWindowTableDTO.setBatchNo(purchaseInvoiceTable.getBatch_or_serial());
                batchWindowTableDTO.setB_details_id(purchaseInvoiceTable.getB_details_id());
                LocalDate manu_dt = null;
                if (!purchaseInvoiceTable.getManufacturing_date().isEmpty()) {
                    manu_dt = LocalDate.parse(purchaseInvoiceTable.getManufacturing_date());
                    batchWindowTableDTO.setManufacturingDate(manu_dt.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
                } else {
                    batchWindowTableDTO.setManufacturingDate("");
                }
                LocalDate exp_dt = null;
                if (purchaseInvoiceTable.getB_expiry().isEmpty()) {
                    exp_dt = LocalDate.parse(purchaseInvoiceTable.getB_expiry());
                    batchWindowTableDTO.setExpiryDate(exp_dt.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
                } else {
                    batchWindowTableDTO.setExpiryDate("");
                }

                batchWindowTableDTO.setMrp(purchaseInvoiceTable.getB_rate());
                batchWindowTableDTO.setPurchaseRate(purchaseInvoiceTable.getB_purchase_rate());

                batchWindowTableDTO.setQuantity(purchaseInvoiceTable.getQuantity());
                batchWindowTableDTO.setFree(purchaseInvoiceTable.getFree());

                batchWindowTableDTO.setDiscountPercentage(purchaseInvoiceTable.getDis_per());
                batchWindowTableDTO.setDiscountAmount(purchaseInvoiceTable.getDis_amt());

                //batchWindowTableDTO.setBarcode(purchaseInvoiceTable.getBarcode());
                batchWindowTableDTO.setMargin(purchaseInvoiceTable.getMin_margin());


                batchWindowTableDTO.setCsr_mh(purchaseInvoiceTable.getRate_c());
                batchWindowTableDTO.setCsr_ai(purchaseInvoiceTable.getRate_d().equals("0.0") ? "" : purchaseInvoiceTable.getRate_d());
                batchWindowTableDTO.setFsr_mh(purchaseInvoiceTable.getRate_a());
                batchWindowTableDTO.setFsr_ai(purchaseInvoiceTable.getRate_b());

                batchWindowTableDTO.setSupplier_id(purchaseInvoiceTable.getLedger_id());
                batchWindowTableDTO.setTax(purchaseInvoiceTable.getTax());

                List<BatchWindowTableDTO> listBatch = new ArrayList<>();
                listBatch.add(batchWindowTableDTO);

                purchaseInvoiceTable.setBatchWindowTableDTOList(listBatch);

                tblvPurChToInvTable.getItems().addAll(purchaseInvoiceTable);
                PurchaseInvoiceCalculation.rowCalculationForPurcInvoice(index, tblvPurChToInvTable, callback);

                PurchaseInvoiceCalculation.calculateGst(tblvPurChToInvTable, callback);
                List<LevelAForPurInvoice> levelAForPurInvoiceList = ProductUnitsPacking.getAllProductUnitsPackingFlavour(purchaseInvoiceTable.getProduct_id());

                ObservableList<LevelAForPurInvoice> observableLevelAList = FXCollections.observableArrayList(levelAForPurInvoiceList);

                if (index >= 0 && index < PurInvoiceCommunicator.levelAForPurInvoiceObservableList.size()) {
                    PurInvoiceCommunicator.levelAForPurInvoiceObservableList.set(index, observableLevelAList);

                    tblvPurChToInvTable.getItems().get(index).setLevelA(null);
                    tblvPurChToInvTable.getItems().get(index).setLevelA(levelAForPurInvoiceList.get(0).getLabel());

                    for (LevelAForPurInvoice levelAForPurInvoice : PurInvoiceCommunicator.levelAForPurInvoiceObservableList.get(index)) {
                        if (tblvPurChToInvTable.getItems().get(index).getLevelA_id().equals(levelAForPurInvoice.getValue())) {
                            tblvPurChToInvTable.getItems().get(index).setLevelA(null);
                            tblvPurChToInvTable.getItems().get(index).setLevelA(levelAForPurInvoice.getLabel());
                        }
                    }

                } else {
                    PurInvoiceCommunicator.levelAForPurInvoiceObservableList.add(observableLevelAList);

                    tblvPurChToInvTable.getItems().get(index).setLevelA(null);
                    tblvPurChToInvTable.getItems().get(index).setLevelA(levelAForPurInvoiceList.get(0).getLabel());

                    for (LevelAForPurInvoice levelAForPurInvoice : PurInvoiceCommunicator.levelAForPurInvoiceObservableList.get(index)) {
                        if (tblvPurChToInvTable.getItems().get(index).getLevelA_id().equals(levelAForPurInvoice.getValue())) {
                            tblvPurChToInvTable.getItems().get(index).setLevelA(null);
                            tblvPurChToInvTable.getItems().get(index).setLevelA(levelAForPurInvoice.getLabel());
                        }
                    }
                }

                count++;
                index++;
            }

            lblPurChallTotalQty.setText(totalQty.toString());
            lblPurChallFreeQty.setText(totalFreeQty.toString());
            setAddChargesInTable();
        } else {
            tblvPurChToInvTable.getItems().addAll(new PurchaseInvoiceTable("", "0", "1", "", "", "", "", "", "", "", "", "", "", "", "", "", "", ""));
        }


//        tcPurChToInvSrNo.setCellValueFactory(cellData -> cellData.getValue().sr_noProperty());
        tcPurChToInvSrNo.setCellFactory(new LineNumbersCellFactory());

        tcPurChToInvSrNo.setStyle("-fx-alignment: CENTER;");

        tcPurChToInvPackage.setCellValueFactory(cellData -> cellData.getValue().packagesProperty());
        tcPurChToInvPackage.setStyle("-fx-alignment: CENTER;");

        tcPurChToInvLevelB.setCellValueFactory(cellData -> cellData.getValue().levelBProperty());
        tcPurChToInvLevelB.setCellFactory(column -> new ComboBoxTableCellForLevelB("tcPurChToInvLevelB"));

        tcPurChToInvLevelA.setCellValueFactory(cellData -> cellData.getValue().levelAProperty());
        tcPurChToInvLevelA.setCellFactory(column -> new ComboBoxTableCellForLevelA("tcPurChToInvLevelA"));

        tcPurChToInvLevelC.setCellValueFactory(cellData -> cellData.getValue().levelCProperty());
        tcPurChToInvLevelC.setCellFactory(column -> new ComboBoxTableCellForLevelC("tcPurChToInvLevelC"));

        tcPurChToInvUnit.setCellValueFactory(cellData -> cellData.getValue().unitProperty());
        tcPurChToInvUnit.setCellFactory(column -> new ComboBoxTableCellForUnit("tcPurChToInvUnit"));

        tcPurChToInvParticulars.setCellValueFactory(cellData -> cellData.getValue().particularsProperty());
        // tcPurChToInvParticulars.setCellFactory(column -> new TextFieldTableCellForPurInvoice("tcPurChToInvParticulars",
        //           callback, productID_callback));


        tcPurChToInvBatch.setCellValueFactory(cellData -> cellData.getValue().batch_or_serialProperty());
        //  tcPurChToInvBatch.setCellFactory(column -> new TextFieldTableCellForPurInvoice("tcPurChToInvBatch", callback, productID_callback));


        tcPurChToInvQuantity.setCellValueFactory(cellData -> cellData.getValue().quantityProperty());
        tcPurChToInvQuantity.setCellFactory(column -> new TextFieldTableCellForPurInvoice("tcPurChToInvQuantity", callback));

        tcPurChToInvRate.setCellValueFactory(cellData -> cellData.getValue().rateProperty());
        tcPurChToInvRate.setCellFactory(column -> new TextFieldTableCellForPurInvoice("tcPurChToInvRate", callback));

        tcPurChToInvGrossAmount.setCellValueFactory(cellData -> cellData.getValue().gross_amountProperty());
        tcPurChToInvGrossAmount.setCellFactory(column -> new TextFieldTableCellForPurInvoice("tcPurChToInvGrossAmount", callback));

        tcPurChToInvDisPer.setCellValueFactory(cellData -> cellData.getValue().dis_perProperty());
        tcPurChToInvDisPer.setCellFactory(column -> new TextFieldTableCellForPurInvoice("tcPurChToInvDisPer", callback));

        tcPurChToInvDisPer.setCellValueFactory(cellData -> cellData.getValue().dis_perProperty());
        tcPurChToInvDisPer.setCellFactory(column -> new TextFieldTableCellForPurInvoice("tcPurChToInvDisPer", callback));

        tcPurChToInvDisAmt.setCellValueFactory(cellData -> cellData.getValue().dis_amtProperty());
        tcPurChToInvDisAmt.setCellFactory(column -> new TextFieldTableCellForPurInvoice("tcPurChToInvDisAmt", callback));

        tcPurChToInvTax.setCellValueFactory(cellData -> cellData.getValue().taxProperty());
        tcPurChToInvTax.setCellFactory(column -> new TextFieldTableCellForPurInvoice("tcPurChToInvTax", callback));

        tcPurChToInvNetAmount.setCellValueFactory(cellData -> cellData.getValue().net_amountProperty());
        tcPurChToInvNetAmount.setCellFactory(column -> new TextFieldTableCellForPurInvoice("tcPurChToInvNetAmount", callback));

        tcPurChToInvAction.setCellValueFactory(cellData -> cellData.getValue().net_amountProperty());
        tcPurChToInvAction.setCellFactory(column -> new ButtonTableCell());
        columnVisibility(tcPurChToInvLevelA, Globals.getUserControlsWithSlug("is_level_a"));
        columnVisibility(tcPurChToInvLevelB, Globals.getUserControlsWithSlug("is_level_b"));
        columnVisibility(tcPurChToInvLevelC, Globals.getUserControlsWithSlug("is_level_c"));

        tcPurChToInvLevelA.setText(Globals.getUserControlsNameWithSlug("is_level_a"));
        tcPurChToInvLevelB.setText(Globals.getUserControlsNameWithSlug("is_level_B"));
        tcPurChToInvLevelC.setText(Globals.getUserControlsNameWithSlug("is_level_C"));

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

    private void tranxProductDetailsFun(String id) {
        tranxPurChToInvTabPane.getSelectionModel().select(tabPurChToInvProduct);

        APIClient apiClient = null;
        try {
            Map<String, String> map = new HashMap<>();
            map.put("product_id", id);
            String formData = Globals.mapToStringforFormData(map);
            apiClient = new APIClient(EndPoints.TRANSACTION_PRODUCT_DETAILS, formData, RequestType.FORM_DATA);
            apiClient.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
                @Override
                public void handle(WorkerStateEvent workerStateEvent) {
                    JsonObject jsonObject = new Gson().fromJson(workerStateEvent.getSource().getValue().toString(), JsonObject.class);
                    if (jsonObject.get("responseStatus").getAsInt() == 200) {
                        JsonObject productJsonObj = jsonObject.getAsJsonObject("result");

                        lblPurChToInvProductBrand.setText(productJsonObj.get("brand").getAsString());
                        lblPurChToInvProductGroup.setText(productJsonObj.get("group").getAsString());
                        lblPurChToInvProductSubGroup.setText(productJsonObj.get("subgroup").getAsString());
                        lblPurChToInvProductCategory.setText(productJsonObj.get("category").getAsString());
                        lblPurChToInvProductHsn.setText(productJsonObj.get("hsn").getAsString());
                        lblPurChToInvProductTaxType.setText(productJsonObj.get("tax_type").getAsString());
                        lblPurChToInvProductTaxPer.setText(productJsonObj.get("tax_per").getAsString());
                        lblPurChToInvProductMarginPer.setText(productJsonObj.get("margin_per").getAsString());
                        lblPurChToInvProductCost.setText(productJsonObj.get("cost").getAsString());
                        lblPurChToInvProductShelfId.setText(productJsonObj.get("shelf_id").getAsString());
                        lblPurChToInvProductMinStock.setText(productJsonObj.get("min_stocks").getAsString());
                        lblPurChToInvProductMaxStock.setText(productJsonObj.get("max_stocks").getAsString());
                    }
                }
            });
            apiClient.setOnCancelled(new EventHandler<WorkerStateEvent>() {
                @Override
                public void handle(WorkerStateEvent workerStateEvent) {
                    logger.error("Network API cancelled in tranxProductDetailsFun()" + workerStateEvent.getSource().getValue().toString());
                }
            });
            apiClient.setOnFailed(new EventHandler<WorkerStateEvent>() {
                @Override
                public void handle(WorkerStateEvent workerStateEvent) {
                    logger.error("Network API failed in tranxProductDetailsFun()" + workerStateEvent.getSource().getValue().toString());
                }
            });
            apiClient.start();
        } catch (Exception e) {
            System.out.println("exception:" + e);
        } finally {
            apiClient = null;
        }

    }


    public void setCbSupplierGSTN(ActionEvent actionEvent) {
        Object[] object = new Object[1];
        object[0] = cmbPurChToInvSupplierGSTIN.getSelectionModel().getSelectedItem();
        if (object[0] != null) {
            for (CommonDTO commonDTO : supplierGSTINList) {
                if (object[0].equals(commonDTO)) {
                    supplierGSTIN_ID = commonDTO.getId();
                    gst_no = commonDTO.getText();
                }
            }
        }
    }

    public void onClickAdditionalCharges() {
        Stage stage = (Stage) purChtoInvScrollPane.getScene().getWindow();
        AdditionalCharges.additionalCharges(stage, addChargesDTOList, total_taxable_amt, (input1, input2) -> {
            addChargesDTOList.clear();
            addChargesDTOList.addAll(input2);
            tfPurChToInvAddCharges.setText(input1[1]);
            PurchaseInvoiceCalculation.additionalChargesCalculation(input1[1], tblvPurChToInvTable, callback);

        });
    }


    public void onClickSubmit(ActionEvent actionEvent) {
        invoiceValidations();
    }

    private void invoiceValidations() {
        //openAdjustBillWindow();
        Stage stage = (Stage) purChtoInvScrollPane.getScene().getWindow();
        if (tfPurChToInvLedgerName.getText().isEmpty()) {
            AlertUtility.AlertDialogForErrorWithStage(stage, "Validation Error", "Enter mandatory Ledger Name Field ", output -> {
                if (output) {
                    tfPurChToInvLedgerName.requestFocus();
                }
            });
        } else if (tfPurChToInvInvoiceNo.getText().isEmpty()) {
            AlertUtility.AlertDialogForErrorWithStage(stage, "Validation Error", "Enter mandatory " +
                    "Invoice No Field", output -> {
                if (output) {
                    tfPurChToInvInvoiceNo.requestFocus();
                }
            });
        } else if (tfPurChToInvInvoiceDate.getText().isEmpty()) {
            AlertUtility.AlertDialogForErrorWithStage(stage, "Validation Error", "Enter mandatory " +
                    "Invoice Date Field", output -> {
                if (output) {
                    tfPurChToInvInvoiceDate.requestFocus();
                }
            });
        } else {
            AlertUtility.AlertConfirmation(AlertUtility.alertTypeConfirmation, LedgerMessageConsts.msgConfirmationOnSubmit + tfPurChToInvInvoiceNo.getText() + LedgerMessageConsts.msgInvoiceNumber, input -> {
                if (input == 1) {
                    createPurchaseInvoice();
                }
            });

        }

    }

    private void validatePurchaseInvoiceNo() {
        try {
            logger.debug("Validating Pur Invoice Number");
            Map<String, String> map = new HashMap<>();
            map.put("supplier_id", ledger_id);
            map.put("bill_no", tfPurChToInvInvoiceNo.getText());
            System.out.println("Ledger Id:" + ledger_id);
            System.out.println("Bill No:" + tfPurChToInvInvoiceNo.getText());

            String formData = Globals.mapToStringforFormData(map);
            HttpResponse<String> response = APIClient.postFormDataRequest(formData, EndPoints.VALIDATE_PUR_INV_NUMBER);
            String resBody = response.body();
            JsonObject obj = new Gson().fromJson(resBody, JsonObject.class);
            if (obj.get("responseStatus").getAsInt() == 409) {
                AlertUtility.AlertWarningTimeout(AlertUtility.alertTypeWarning, obj.get("message").getAsString(), callback -> {
                    logger.debug("Validate Pur Invoice Success");
                });
                tfPurChToInvInvoiceNo.setText(tfPurChToInvInvoiceNo.getText());
                tfPurChToInvInvoiceNo.requestFocus();
            }
        } catch (Exception e) {
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            String exceptionAsString = sw.toString();
            logger.error("Exception in Purchase invoice validate():" + exceptionAsString);
        }

    }

    public void createPurchaseInvoice() {

        Object idValue = purChToInv_invoice_data_map.get("id");
        if (idValue != null) {
            purchase_invoice_map.put("id", idValue.toString());
        }

        purchase_invoice_map.put("taxFlag", String.valueOf(taxFlag));

        purchase_invoice_map.put("narration", tfPurChToInvNarration.getText());

        purchase_invoice_map.put("invoice_date", Communicator.text_to_date.fromString(tfPurChToInvInvoiceDate.getText()).toString());

        purchase_invoice_map.put("transaction_date", Communicator.text_to_date.fromString(tfPurChToInvTranxDate.getText()).toString());

        purchase_invoice_map.put("newReference", "false");

        purchase_invoice_map.put("invoice_no", tfPurChToInvInvoiceNo.getText());

        purchase_invoice_map.put("purchase_id", purchaseAcID);

        purchase_invoice_map.put("purchase_sr_no", tfPurChToInvPurchaseSerial.getText());

        purchase_invoice_map.put("supplier_code_id", ledger_id);

        purchase_invoice_map.put("isRoundOffCheck", "false");

        purchase_invoice_map.put("roundoff", "0.00");

        purchase_invoice_map.put("totalamt", totalamt);

        purchase_invoice_map.put("total_purchase_discount_amt", total_purchase_discount_amt);

        purchase_invoice_map.put("gstNo", gst_no);

        purchase_invoice_map.put("paymentMode", selectedPayMode);

        purchase_invoice_map.put("tcs_per", "0.00");

        purchase_invoice_map.put("tcs_amt", "0.00");

        purchase_invoice_map.put("tcs_mode", "");

        purchase_invoice_map.put("purchase_discount", tfPurChToInvDisPropPer.getText().isEmpty() || tfPurChToInvDisPropPer == null ? "0.0" : tfPurChToInvDisPropPer.getText());

        purchase_invoice_map.put("purchase_discount_amt", tfPurChToInvDisPropAmt.getText().isEmpty() || tfPurChToInvDisPropPer == null ? "0.0" : tfPurChToInvDisPropAmt.getText());

        purchase_invoice_map.put("additionalChargesTotal", tfPurChToInvAddCharges.getText().isEmpty() ? "0" : tfPurChToInvAddCharges.getText());

        purchase_invoice_map.put("additionalCharges", addChargesDTOList.toString());

        purchase_invoice_map.put("reference_po_ids", purchase_id);
        purchase_invoice_map.put("reference", purChToInv_response_map.get("reference_type").toString());
        purchase_invoice_map.put("reference_type", purChToInv_response_map.get("reference_type").toString());

        List<PurchaseInvoiceTable> list = tblvPurChToInvTable.getItems();

        for (PurchaseInvoiceTable purchaseInvoiceTable : list) {
            if (!purchaseInvoiceTable.getTotal_taxable_amt().equals("0.0")) {
                purchaseInvoiceTable.setTotal_amt(purchaseInvoiceTable.getTotal_taxable_amt());
            } else {
                purchaseInvoiceTable.setTotal_amt(purchaseInvoiceTable.getTaxable_amt());
            }
        }

        Double total_row_gross_amt = 0.0;

        for (PurchaseInvoiceTable purchaseInvoiceTable : list) {
            total_row_gross_amt = total_row_gross_amt + Double.parseDouble(purchaseInvoiceTable.getGross_amount1());
        }

        purchase_invoice_map.put("total_row_gross_amt", String.valueOf(total_row_gross_amt));

        purchase_invoice_map.put("row", list.toString());

        System.out.println(purchase_invoice_map);

        try {
            String formdata = mapToStringforFormData(purchase_invoice_map);

            Map<String, String> headers = new HashMap<>();
            headers.put("branch", Globals.headerBranch);

            String response = null;
            APIClient apiClient = null;
            System.out.println(" ---else---- ");
            apiClient = new APIClient(EndPoints.CREATE_PUR_INVOICE_END, purchase_invoice_map, headers, null, RequestType.MULTI_PART);
            apiClient.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
                @Override
                public void handle(WorkerStateEvent workerStateEvent) {
                    logger.info("Purchase Invoice create---> on Succeeded ");
                    String response = workerStateEvent.getSource().getValue().toString();
                    JsonObject resDTO = new Gson().fromJson(response, JsonObject.class);
//                    if (resDTO.get("responseStatus").getAsInt() == 200) {
//                        Stage stage = (Stage) purChtoInvScrollPane.getScene().getWindow();
//                        AlertUtility.AlertDialogForSuccessWithStage(stage, "Success",
//                                "Purchase Invoice Created Successfully", input -> {
//                                    if (input) {
//                                        GlobalController.getInstance().addTabStatic(PURCHAE_INV_LIST_SLUG, false);
//                                    }
//                                });
//                    }
                    if (resDTO.get("responseStatus").getAsInt() == 200) {
                        AlertUtility.AlertSuccessTimeout(AlertUtility.alertTypeSuccess, resDTO.get("message").getAsString(), input -> {
                            GlobalController.getInstance().addTabStatic(SALES_ORDER_LIST_SLUG, false);
                        });
                    } else {
                        AlertUtility.AlertErrorTimeout(AlertUtility.alertTypeError, resDTO.get("message").getAsString(), in -> {
                            btnPurChToInvsubmit.requestFocus();
                        });
                    }

                }
            });
            apiClient.start();

        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    public void purc_disc_per(KeyEvent keyEvent) {
        String discText = tfPurChToInvDisPropPer.getText();
        CommonValidationsUtils.restrictToDecimalNumbers(tfPurChToInvDisPropPer);
        if (!TextUtils.isEmpty(discText)) {
            double totalTaxableAmt = 0.0;
            for (int i = 0; i < tblvPurChToInvTable.getItems().size(); i++) {
                System.out.println("Gross amt in Prop--->" + tblvPurChToInvTable.getItems().get(i).getGross_amount());
                PurchaseInvoiceCalculation.rowCalculationForPurcInvoice(i, tblvPurChToInvTable, callback);//call row calculation function
                PurchaseInvoiceTable purchaseInvoiceTable = tblvPurChToInvTable.getItems().get(i);
                totalTaxableAmt = totalTaxableAmt + Double.parseDouble(purchaseInvoiceTable.getTaxable_amt());

            }
            double disc_per = Double.parseDouble(discText);
            System.out.println("totalTaxableAmount >> : " + totalTaxableAmt);
            Double amount = (totalTaxableAmt * disc_per) / 100;
            tfPurChToInvDisPropAmt.setText(String.valueOf(amount));
            PurchaseInvoiceCalculation.discountPropotionalCalculation(disc_per + "", "0", tfPurChToInvAddCharges.getText(), tblvPurChToInvTable, callback);
            if (!tfPurChToInvAddCharges.getText().isEmpty()) {
                System.out.println("Additioanal CHarges Called >> ");
                PurchaseInvoiceCalculation.additionalChargesCalculation(tfPurChToInvAddCharges.getText(), tblvPurChToInvTable, callback);
            }
        } else {
            tfPurChToInvDisPropAmt.setText("");
            PurchaseInvoiceCalculation.discountPropotionalCalculation("0", "0", tfPurChToInvAddCharges.getText(), tblvPurChToInvTable, callback);
            if (!tfPurChToInvAddCharges.getText().isEmpty()) {
                System.out.println("Additioanal CHarges Called >> ");
                PurchaseInvoiceCalculation.additionalChargesCalculation(tfPurChToInvAddCharges.getText(), tblvPurChToInvTable, callback);
            }
        }

        //? Calculate Tax for each Row
        PurchaseInvoiceCalculation.calculateGst(tblvPurChToInvTable, callback);

    }

    public void purc_disc_amt(KeyEvent keyEvent) {
        String discAmtText = tfPurChToInvDisPropAmt.getText();
        CommonValidationsUtils.restrictToDecimalNumbers(tfPurChToInvDisPropAmt);
        if (!TextUtils.isEmpty(discAmtText)) {
            double totalTaxableAmt = 0.0;
            for (int i = 0; i < tblvPurChToInvTable.getItems().size(); i++) {
                System.out.println("Gross amt in Prop--->" + tblvPurChToInvTable.getItems().get(i).getGross_amount());
                PurchaseInvoiceCalculation.rowCalculationForPurcInvoice(i, tblvPurChToInvTable, callback);//call row calculation function
                PurchaseInvoiceTable purchaseInvoiceTable = tblvPurChToInvTable.getItems().get(i);
                totalTaxableAmt = totalTaxableAmt + Double.parseDouble(purchaseInvoiceTable.getTaxable_amt());

            }
            double disc_amt = Double.parseDouble(discAmtText);
            double percentage = (disc_amt / totalTaxableAmt) * 100;
            tfPurChToInvDisPropPer.setText(String.format("%.2f", percentage));

            PurchaseInvoiceCalculation.discountPropotionalCalculation("0", disc_amt + "", tfPurChToInvAddCharges.getText(), tblvPurChToInvTable, callback);
            if (!tfPurChToInvAddCharges.getText().isEmpty()) {
                System.out.println("Additioanal CHarges Called >> ");
                PurchaseInvoiceCalculation.additionalChargesCalculation(tfPurChToInvAddCharges.getText(), tblvPurChToInvTable, callback);
            }
        } else {
            tfPurChToInvDisPropPer.setText("");
            PurchaseInvoiceCalculation.discountPropotionalCalculation("0", "0", tfPurChToInvAddCharges.getText(), tblvPurChToInvTable, callback);
            if (!tfPurChToInvAddCharges.getText().isEmpty()) {
                System.out.println("Additioanal CHarges Called >> ");
                PurchaseInvoiceCalculation.additionalChargesCalculation(tfPurChToInvAddCharges.getText(), tblvPurChToInvTable, callback);
            }
        }

        //? Calculate Tax for each Row
        PurchaseInvoiceCalculation.calculateGst(tblvPurChToInvTable, callback);
    }


    private void tranxBatchDetailsFun(String bNo, String bId) {
        APIClient apiClient = null;
        tranxPurChToInvTabPane.getSelectionModel().select(tabPurInvBatch);
        Map<String, String> map = new HashMap<>();
        map.put("batchNo", bNo);
        map.put("id", bId);
        String formData = Globals.mapToStringforFormData(map);
        System.out.println("FormData: " + formData);
        logger.debug("Form Data for GET_BATCH_DETAILS_ENDPOINT in Purchase Invoice Controller:" + formData);
        logger.debug("Network call started GET_BATCH_DETAILS_ENDPOINT in Purchase Invoice Controller:");

        apiClient = new APIClient(EndPoints.GET_BATCH_DETAILS_ENDPOINT, formData, RequestType.FORM_DATA);

        apiClient.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent workerStateEvent) {
                JsonObject jsonObject = new Gson().fromJson(workerStateEvent.getSource().getValue().toString(), JsonObject.class);
                if (jsonObject.get("responseStatus").getAsInt() == 200) {
                    JsonObject batchJsonObj = jsonObject.getAsJsonObject("response");
                    lblPurChToInvBatchNo.setText(batchJsonObj.get("batchNo").getAsString());
                    lblPurChToInvMfgDate.setText(DateConvertUtil.convertDispDateFormat(batchJsonObj.get("mfgDate").getAsString()));
                    lblPurChToInvExpDate.setText(DateConvertUtil.convertDispDateFormat(batchJsonObj.get("expDate").getAsString()));
                    lblPurChToInvBarcode.setText(DateConvertUtil.convertDispDateFormat(batchJsonObj.get("barcode").getAsString()));
                    lblPurChToInvPurRate.setText(batchJsonObj.get("pur_rate").getAsString());
                    lblPurChToInvCost.setText(batchJsonObj.get("cost").getAsString());
                    lblPurChToInvCostWithTax.setText(batchJsonObj.get("cost_with_tax").getAsString());
                    lblPurChToInvMrp.setText(batchJsonObj.get("batchMrp").getAsString());
                    lblPurChToInvFsrmh.setText(batchJsonObj.get("fsrmh").getAsString());
                    lblPurChToInvFssai.setText(batchJsonObj.get("fsrai").getAsString());
                    lbPurInvBatchCsrmh.setText(batchJsonObj.get("csrmh").getAsString());
                    lbPurInvBatchCsrai.setText(batchJsonObj.get("csrai").getAsString());
                    lblPurChToInvFreeqty.setText(batchJsonObj.get("free_qty").getAsString());
                    lblPurChToInvMarginPer.setText(batchJsonObj.get("min_margin").getAsString());
                    logger.debug("Network call Success GET_BATCH_DETAILS_ENDPOINT:");

                }
            }
        });
        apiClient.setOnCancelled(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent workerStateEvent) {
                logger.error("Network API cancelled in tranxBatchDetailsFun()" + workerStateEvent.getSource().getValue().toString());

            }
        });

        apiClient.setOnFailed(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent workerStateEvent) {
                logger.error("Network API failed in tranxBatchDetailsFun()" + workerStateEvent.getSource().getValue().toString());
            }
        });
        apiClient.start();
        logger.debug("tranxBatchDetailsFun() Ended");
    }

    public void handlecmbPurChToInvPaymentMode(ActionEvent actionEvent) {
        selectedPayMode = cmbPurChToInvPaymentMode.getValue();

    }


    /**************** Purchase Challan TO Purchase Invoice Conversion Started ************/

    private void getPurChallToPurInvById(String purchase_id) {

        try {
            Map<String, String> map = new HashMap<>();
            map.put("po_challan_ids", String.valueOf(purchase_id));
            String formData = mapToStringforFormData(map);
            System.out.println("Hello FormData >> : " + formData);
            HttpResponse<String> response = APIClient.postFormDataRequest(formData, "get_po_challan_invoices_with_ids");
            PurChallToInvMainDTO purchaseEditResponseDTO = new Gson().fromJson(response.body(), PurChallToInvMainDTO.class);
            System.out.println("res update: " + purchaseEditResponseDTO);

            if (purchaseEditResponseDTO.getResponseStatus() == 200) {
                setEditResponsePurChallToPurInv(purchaseEditResponseDTO);
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setEditResponsePurChallToPurInv(PurChallToInvMainDTO purchaseEditResponseDTO) {

        tfPurChToInvInvoiceNo.setText(purchaseEditResponseDTO.getInvoiceData().getPurchaseOrder());
        tfPurChToInvNarration.setText(purchaseEditResponseDTO.getInvoiceData().getNarration());
        ledger_id = String.valueOf(purchaseEditResponseDTO.getInvoiceData().getSupplierId());
        tranxLedgerDetailsFun(ledger_id);

        purChToInv_response_map.put("discountLedgerId", purchaseEditResponseDTO.getDiscountLedgerId());
        purChToInv_response_map.put("discountInAmt", purchaseEditResponseDTO.getDiscountInAmt());
        purChToInv_response_map.put("discountInPer", purchaseEditResponseDTO.getDiscountInPer());
        purChToInv_response_map.put("totalPurchaseDiscountAmt", purchaseEditResponseDTO.getTotalPurchaseDiscountAmt());
        purChToInv_response_map.put("reference_type", purchaseEditResponseDTO.getReferenceType());

        purChToInvRowListDTOS.addAll(purchaseEditResponseDTO.getRow());
        addChargesResLst.addAll(purchaseEditResponseDTO.getAdditionalCharges());

        purChToInv_response_map.put("invoiceData", purchaseEditResponseDTO.getInvoiceData());
        purChToInv_response_map.put("additionalCharges", purchaseEditResponseDTO.getAdditionalCharges());

        PurChallToInvInvoiceDataDTO invoiceDataResDTO = purchaseEditResponseDTO.getInvoiceData();
        purChToInv_invoice_data_map.put("id", String.valueOf(invoiceDataResDTO.getId()));
        purChToInv_invoice_data_map.put("invoice_dt", String.valueOf(invoiceDataResDTO.getInvoiceDt()));
        purChToInv_invoice_data_map.put("invoice_no", String.valueOf(invoiceDataResDTO.getPurchaseOrder()));
        purChToInv_invoice_data_map.put("purchase_account_ledger_id", String.valueOf(invoiceDataResDTO.getPurchaseId()));
        purChToInv_invoice_data_map.put("purchase_name", String.valueOf(invoiceDataResDTO.getPurchaseName()));
        purChToInv_invoice_data_map.put("supplierId", String.valueOf(invoiceDataResDTO.getSupplierId()));
        ledger_id = purChToInv_invoice_data_map.get("supplierId");
        purChToInv_invoice_data_map.put("supplier_name", String.valueOf(invoiceDataResDTO.getSupplierName()));
        purChToInv_invoice_data_map.put("po_sr_no", String.valueOf(invoiceDataResDTO.getPoSrNo()));
        purChToInv_invoice_data_map.put("gstNo", String.valueOf(invoiceDataResDTO.getGstNo()));
        purChToInv_invoice_data_map.put("po_date", String.valueOf(invoiceDataResDTO.getPoDate()));
        purChToInv_invoice_data_map.put("transport_name", String.valueOf(invoiceDataResDTO.getTransportName()));
        purChToInv_invoice_data_map.put("narration", String.valueOf(invoiceDataResDTO.getNarration()));
        purChToInv_invoice_data_map.put("reference", String.valueOf(invoiceDataResDTO.getReference()));
    }

    private void setAddChargesInTable() {
        for (AdditionalCharge addChgLst : addChargesResLst) {
            CustomAddChargesDTO customAddChargesDTO = new CustomAddChargesDTO();
            customAddChargesDTO.setAdditional_charges_details_id(String.valueOf(addChgLst.getAdditionalChargesDetailsId()));
            customAddChargesDTO.setLedgerId(String.valueOf(addChgLst.getLedgerId()));
            customAddChargesDTO.setAmt(String.valueOf(addChgLst.getAmt()));
            customAddChargesDTO.setPercent(String.valueOf(addChgLst.getPercent()));
            addChargesDTOList.add(customAddChargesDTO);
        }
    }

}










