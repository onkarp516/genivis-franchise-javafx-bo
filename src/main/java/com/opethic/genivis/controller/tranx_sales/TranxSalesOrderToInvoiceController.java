

package com.opethic.genivis.controller.tranx_sales;

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
import com.opethic.genivis.controller.tranx_sales.common.TranxCommonPopUps;
import com.opethic.genivis.dto.*;
import com.opethic.genivis.dto.add_charges.CustomAddChargesDTO;
import com.opethic.genivis.dto.cmp_t_row.BatchWindowTableDTO;
import com.opethic.genivis.dto.cmp_t_row.NegBatchWindowTableDTO;
import com.opethic.genivis.dto.pur_invoice.*;
import com.opethic.genivis.dto.pur_invoice.reqres.*;
import com.opethic.genivis.dto.reqres.product.Communicator;
import com.opethic.genivis.dto.reqres.product.TableCellCallback;
import com.opethic.genivis.dto.reqres.sales_tranx.SalesInvoiceTable;
import com.opethic.genivis.dto.reqres.sales_tranx.SalesOrderTable;
import com.opethic.genivis.models.tranx.sales.TranxSelectedBatch;
import com.opethic.genivis.models.tranx.sales.TranxSelectedProduct;
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
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
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

import static com.opethic.genivis.utils.FxmFileConstants.SALES_INVOICE_LIST_SLUG;
import static com.opethic.genivis.utils.FxmFileConstants.SALES_ORDER_LIST_SLUG;
import static com.opethic.genivis.utils.Globals.decimalFormat;
import static com.opethic.genivis.utils.Globals.mapToStringforFormData;
import static javafx.scene.control.PopupControl.USE_COMPUTED_SIZE;
import static javafx.scene.control.PopupControl.USE_PREF_SIZE;

public class TranxSalesOrderToInvoiceController implements Initializable {
    @FXML
    public TabPane tranxPurInvTabPane;
    @FXML
    public Tab tabPurInvLedger;
    @FXML
    public Tab tabPurInvProduct;
    @FXML
    public Label lblPurInvMarginPer;
    @FXML
    public Label lblPurInvFsrmh;

    @FXML
    private HBox so2itopFirstRow, so2itopSecondRow, so2iBottomMain;
    @FXML
    private VBox so2iBottomFirstV, so2iBottomSecondV, so2iBottomThirdV, so2iTotalMainDiv, so2iTotalMainInnerDiv;

    @FXML
    private VBox totalMainDiv, middleVbox;
    @FXML
    private VBox totalInnerDivFirst;
    @FXML
    private HBox topSecondRow, topFirstRow, bottomHb;

    @FXML
    public Label lblPurInvPurRate;
    @FXML
    public Label lblPurInvMfgDate;

    @FXML
    public Label lblPurInvBatchNo;
    @FXML
    public Tab tabPurInvBatch;
    @FXML
    public Label lbPurInvBatchCsrai;
    @FXML
    public Label lblPurInvExpDate;
    @FXML
    public Label lbPurInvBatchCsrmh;
    @FXML
    public Label lblPurInvMrp;
    @FXML
    public Label lblPurInvFsrai;
    @FXML
    public Label lblPurInvBarcode;
    @FXML
    public Label lblPurInvFreeqty;
    @FXML
    public Label lblPurInvCost;
    @FXML
    public Label lblPurInvCostWithTax;
    @FXML
    public CheckBox chbPurInvRoundOff;
    @FXML
    public Label lblPurInvRoundOff;
    /*    @FXML
        public ComboBox<String> cmbPaymentMode;*/
    @FXML
    public Button btnSubmit,btnCancel;
    //    @FXML
//    public Button btnModify;
    @FXML
    public TextField tfNarration;
    private ObservableList<String> payment_mode_list = FXCollections.observableArrayList("Credit", "Cash");

    @FXML
    BorderPane purScrollPane;

    @FXML
    private TableView<GstDTO> tvGST_Table;

    @FXML
    private TableColumn<GstDTO, String> tc_gst;

    @FXML
    private TableColumn<GstDTO, String> tc_cgst,tc_sgst,tc_igst;


    @FXML
    private TableView<SalesInvoiceTable> tvPurchaseInvoiceTable;

    @FXML
    private TableColumn<SalesInvoiceTable, String> tcSrNo;

    @FXML
    private TableColumn<SalesInvoiceTable, String> tcParticulars;

    @FXML
    private TableColumn<SalesInvoiceTable, String> tcPackage;


    @FXML
    private TableColumn<SalesInvoiceTable, String> tcLevelA;


    @FXML
    private TableColumn<SalesInvoiceTable, String> tcLevelB;


    @FXML
    private TableColumn<SalesInvoiceTable, String> tcLevelC;


    @FXML
    private TableColumn<SalesInvoiceTable, String> tcUnit;


    @FXML
    private TableColumn<SalesInvoiceTable, String> tcBatch;

    @FXML
    private TableColumn<SalesInvoiceTable, String> tcQuantity,tcFreeQuantity,tcRate,tcGrossAmount,tcDisPer,tcDisAmt,tcTax,tcNetAmount;
    @FXML
    private TableColumn<SalesInvoiceTable, String> tcAction;

    JSONArray rowDelDetailsIds = new JSONArray();

    private final ObservableList<LevelAForPurInvoice> levelAList = FXCollections.observableArrayList();

    private final ObservableList<LevelBForPurInvoice> levelBList = FXCollections.observableArrayList();

    private final ObservableList<CommonDTO> levelCList = FXCollections.observableArrayList();

    private final ObservableList<CommonDTO> unitList = FXCollections.observableArrayList();

    @FXML
    private TextField tfTranxDate, tfInvoiceNo, tfLedgerName, tfInvoiceDate, tfPurchaseSerial;

    @FXML
    private ComboBox<CommonDTO> cbPurchaseAc;

    @FXML
    private ComboBox<CommonDTO> cbSupplierGSTIN;

    String purchaseAcID = "";

    String supplierGSTIN_ID = "";

    String ledger_id = "", ledgerName = "";

    //    @FXML
//    RadioButton rbCredit, rbCash;
    private final Logger logger = LogManager.getLogger(TranxSalesOrderToInvoiceController.class);

    private final ObservableList<CommonDTO> purchaseAcList = FXCollections.observableArrayList();

    private final ObservableList<CommonDTO> supplierGSTINList = FXCollections.observableArrayList();

    private final ToggleGroup toggleGroup = new ToggleGroup();

    private String selectedPayMode = "Credit";

    @FXML
    private Label lbGrossTotal, lbDiscount, lbTotal, lbTax, lbBillAmount;

    private Map<String, String> purchase_invoice_map = new HashMap<>();
    private Map<String, String> deleted_rows_map = new HashMap<>();

    private String ledgerStateCode = "";
    private Boolean taxFlag = false;

    private String totalamt = "";

    private Double total_taxable_amt = 0.0;

    private String total_purchase_discount_amt = "";

    @FXML
    private TextField purchase_discount, purchase_discount_amt, tfAddCharges;
    String gst_no = "";
    List<CustomAddChargesDTO> addChargesDTOList = new ArrayList<>();
    private final List<AddChargesListDTO> addChargesResLst = new ArrayList<>();

    @FXML
    private TableView<InvoiceProductHistoryTable> tvInvoiceProductHistory;

    @FXML
    private TableColumn<InvoiceProductHistoryTable, String> cmSupplierName;

    @FXML
    private TableColumn<InvoiceProductHistoryTable, String> cmInvNo,cmInvDate,cmBatch,cmMRP,cmQty,cmRate,cmCost;

    @FXML
    private TableColumn<InvoiceProductHistoryTable, String> cmDisPer;

    @FXML
    private TableColumn<InvoiceProductHistoryTable, String> cmDisAmt;

//    private Integer purchase_id = -1;

    private final Map<String, Object> edit_response_map = new HashMap<>();

    private final Map<String, String> invoice_data_map = new HashMap<>();

    private final List<SalesRowListDTO> rowListDTOS = new ArrayList<>();


    @FXML
    private Label lblPurInvGstNo, lblPurInvArea, lblPurInvBank, lblPurInvContactPerson, lblPurInvTransportName, lblPurInvCreditDays, lblPurInvFssai, lblPurInvLicenseNo, lblPurInvRoute, lblPurInvProductBrand, lblPurInvProductGroup, lblPurInvProductCategory, lblPurInvProductSubGroup, lblPurInvProductHsn, lblPurInvProductTaxType, lblPurInvProductTaxPer, lblPurInvProductMarginPer, lblPurInvProductCost, lblPurInvProductShelfId, lblPurInvProductMinStock, lblPurInvProductMaxStock;

    @FXML
    private Label sales_challan, sales_orders, sales_qoutation,lblTotalQty,lblTotalFreeQty;
    private Long stateCode = 0L, CompanyStateCode = 0L;
    private JsonArray deletedRows = new JsonArray();
    public static String input = "";               //for conversion

    public void setEditId(Integer id) {
        System.out.println("id 1 " + id);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        ResponsiveWiseCssPicker();

        // autofocus on TranxDate
        Platform.runLater(() -> {
            tfLedgerName.requestFocus();
        });
        System.out.println("input from sale order list-- " + input);
        initialEnterMethod();
        getSaleInvoiceByIdNew();
        btnSubmit.setOnAction(actionEvent -> {
            if (CommonValidationsUtils.validateForm(tfLedgerName, tfInvoiceNo, tfInvoiceDate)) {
                createPurchaseInvoice();
            }
        });

        tvPurchaseInvoiceTable.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                int selectedIndex = newValue.intValue();
                if (selectedIndex >= 0) {
                    System.out.println("Selected row index: " + selectedIndex);
                } else {
                    System.out.println("No row selected");
                }
            }
        });

        tvInvoiceProductHistory.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        setGSTFooterPart();
        sceneInitilization();
        responsiveCmpt();

        tvGST_Table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
//        purScrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        tfLedgerName.setEditable(false);

        tableInitiliazation();

        cbPurchaseAc.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.TAB && !event.isShiftDown() || event.getCode() == KeyCode.ENTER) {
                tvPurchaseInvoiceTable.edit(0, tvPurchaseInvoiceTable.getColumns().get(1));
                event.consume();
            }
        });

//        if (purchase_id > 0) {
        tfPurchaseSerial.setText(invoice_data_map.get("sales_sr_no"));
//        } else {
        getPurchaseSerial();
//        }

        tfPurchaseSerial.setEditable(false);
        tfPurchaseSerial.setFocusTraversable(false);

        tfTranxDate.setFocusTraversable(false);
        tfTranxDate.setEditable(false);

//        sceneInitilization();

        cbSupplierGSTIN.setFocusTraversable(true);

        getPurchaseAc();
//        tfLededgerNameEvent();           //commented because in conversion we dont change the ledger
//        AutoCompleteBox<CommonDTO> autoCompleteBox = new AutoCompleteBox<>(cbSupplierGSTIN, -1);
//        if (purchase_id > 0) {
//            tfLedgerName.setText(ledgersById(Long.valueOf(invoice_data_map.get("supplierId"))));
        if (supplierGSTINList != null && !supplierGSTINList.isEmpty()) {
            cbSupplierGSTIN.setItems(supplierGSTINList);
            cbSupplierGSTIN.setValue(supplierGSTINList.get(0));
        }

        invoiceProductHistoryTable();

        chbPurInvRoundOff.setOnAction(e -> {
            roundOffCalculations();
        });
        /*cmbPaymentMode.getSelectionModel().select("Credit");
        cmbPaymentMode.focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if (newValue) {
                    cmbPaymentMode.show();
                }
            }
        });
        if (purchase_id > 0) {
            String paymentMode = edit_response_map.get("paymentMode").toString();
            cmbPaymentMode.getSelectionModel().select(paymentMode);
        }*/
        tfInvoiceValidatition();
        tfInvoiceDateValidation();

        tfAddCharges.setEditable(false);
        tfAddCharges.setFocusTraversable(false);

    }
    //function for CMPT row width size for each field
    private void responsiveCmpt() {
        if (Globals.getUserControlsWithSlug("is_free_qty") == true) {
            tcFreeQuantity.prefWidthProperty().bind(tvPurchaseInvoiceTable.widthProperty().multiply(0.04));
            tcParticulars.prefWidthProperty().bind(tvPurchaseInvoiceTable.widthProperty().multiply(0.28));
            tcGrossAmount.prefWidthProperty().bind(tvPurchaseInvoiceTable.widthProperty().multiply(0.11));
            tcNetAmount.prefWidthProperty().bind(tvPurchaseInvoiceTable.widthProperty().multiply(0.08));
        } else {
            tcFreeQuantity.prefWidthProperty().bind(tvPurchaseInvoiceTable.widthProperty().multiply(0.00));
            tcParticulars.prefWidthProperty().bind(tvPurchaseInvoiceTable.widthProperty().multiply(0.28));
            tcGrossAmount.prefWidthProperty().bind(tvPurchaseInvoiceTable.widthProperty().multiply(0.11));
            tcNetAmount.prefWidthProperty().bind(tvPurchaseInvoiceTable.widthProperty().multiply(0.08));
        }
        tcSrNo.prefWidthProperty().bind(tvPurchaseInvoiceTable.widthProperty().multiply(0.03));
        tcPackage.prefWidthProperty().bind(tvPurchaseInvoiceTable.widthProperty().multiply(0.05));
        tcUnit.prefWidthProperty().bind(tvPurchaseInvoiceTable.widthProperty().multiply(0.07));
        tcQuantity.prefWidthProperty().bind(tvPurchaseInvoiceTable.widthProperty().multiply(0.05));
        tcRate.prefWidthProperty().bind(tvPurchaseInvoiceTable.widthProperty().multiply(0.08));
        tcDisPer.prefWidthProperty().bind(tvPurchaseInvoiceTable.widthProperty().multiply(0.05));
        tcDisAmt.prefWidthProperty().bind(tvPurchaseInvoiceTable.widthProperty().multiply(0.05));
        tcAction.prefWidthProperty().bind(tvPurchaseInvoiceTable.widthProperty().multiply(0.03));
        tcBatch.prefWidthProperty().bind(tvPurchaseInvoiceTable.widthProperty().multiply(0.07));

    }

    private void ResponsiveWiseCssPicker() {
        double height = Screen.getPrimary().getBounds().getHeight();
        double width = Screen.getPrimary().getBounds().getWidth();
        System.out.println("width" + width);
        if (width >= 992 && width <= 1024) {
            so2itopFirstRow.setSpacing(10);
            so2itopSecondRow.setSpacing(10);
            so2iTotalMainDiv.setSpacing(10);
            so2iTotalMainInnerDiv.setSpacing(8);
            // Set preferred widths as percentages of the HBox width
            so2iBottomFirstV.prefWidthProperty().bind(so2iBottomMain.widthProperty().multiply(0.5));
            so2iBottomSecondV.prefWidthProperty().bind(so2iBottomMain.widthProperty().multiply(0.22));
            so2iBottomThirdV.prefWidthProperty().bind(so2iBottomMain.widthProperty().multiply(0.24));
//            so2cBottomMain.setPrefHeight(150);
            tvGST_Table.getStylesheets().add(GenivisApplication.class.getResource("ui/css/purchase_challan_css/gst_table_css/gst_table_992_1024.css").toExternalForm());
            tvInvoiceProductHistory.getStylesheets().add(GenivisApplication.class.getResource("ui/css/purchase_challan_css/supplier_table_css/supplier_table_992_1024.css").toExternalForm());
            purScrollPane.getStylesheets().add(GenivisApplication.class.getResource("ui/css/TranxCommonCssStyles/tranxCommonCssStyle1.css").toExternalForm());
        } else if (width >= 1025 && width <= 1280) {
            so2itopFirstRow.setSpacing(10);
            so2itopSecondRow.setSpacing(10);
            so2iTotalMainDiv.setSpacing(10);
            so2iTotalMainInnerDiv.setSpacing(8);
            // Set preferred widths as percentages of the HBox width
            so2iBottomFirstV.prefWidthProperty().bind(so2iBottomMain.widthProperty().multiply(0.58));
            so2iBottomSecondV.prefWidthProperty().bind(so2iBottomMain.widthProperty().multiply(0.20));
            so2iBottomThirdV.prefWidthProperty().bind(so2iBottomMain.widthProperty().multiply(0.22));
//            so2cBottomMain.setPrefHeight(150);
            tvGST_Table.getStylesheets().add(GenivisApplication.class.getResource("ui/css/purchase_challan_css/gst_table_css/gst_table_1025_1280.css").toExternalForm());
            tvInvoiceProductHistory.getStylesheets().add(GenivisApplication.class.getResource("ui/css/purchase_challan_css/supplier_table_css/supplier_table_1025_1280.css").toExternalForm());
            purScrollPane.getStylesheets().add(GenivisApplication.class.getResource("ui/css/TranxCommonCssStyles/tranxCommonCssStyle2.css").toExternalForm());
        } else if (width >= 1281 && width <= 1368) {
            so2itopFirstRow.setSpacing(12);
            so2itopSecondRow.setSpacing(12);
            so2iTotalMainDiv.setSpacing(12);
            so2iTotalMainInnerDiv.setSpacing(8);
            // Set preferred widths as percentages of the HBox width
            so2iBottomFirstV.prefWidthProperty().bind(so2iBottomMain.widthProperty().multiply(0.6));
            so2iBottomSecondV.prefWidthProperty().bind(so2iBottomMain.widthProperty().multiply(0.20));
            so2iBottomThirdV.prefWidthProperty().bind(so2iBottomMain.widthProperty().multiply(0.20));
//            so2cBottomMain.setPrefHeight(150);
            tvGST_Table.getStylesheets().add(GenivisApplication.class.getResource("ui/css/purchase_challan_css/gst_table_css/gst_table_1281_1368.css").toExternalForm());
            tvInvoiceProductHistory.getStylesheets().add(GenivisApplication.class.getResource("ui/css/purchase_challan_css/supplier_table_css/supplier_table_1281_1368.css").toExternalForm());
            purScrollPane.getStylesheets().add(GenivisApplication.class.getResource("ui/css/TranxCommonCssStyles/tranxCommonCssStyle3.css").toExternalForm());
        } else if (width >= 1369 && width <= 1400) {
            so2itopFirstRow.setSpacing(15);
            so2itopSecondRow.setSpacing(15);
            so2iTotalMainDiv.setSpacing(15);
            so2iTotalMainInnerDiv.setSpacing(8);
            // Set preferred widths as percentages of the HBox width
            so2iBottomFirstV.prefWidthProperty().bind(so2iBottomMain.widthProperty().multiply(0.56));
            so2iBottomSecondV.prefWidthProperty().bind(so2iBottomMain.widthProperty().multiply(0.20));
            so2iBottomThirdV.prefWidthProperty().bind(so2iBottomMain.widthProperty().multiply(0.24));
//            so2cBottomMain.setPrefHeight(150);
            tvGST_Table.getStylesheets().add(GenivisApplication.class.getResource("ui/css/purchase_challan_css/gst_table_css/gst_table_1369_1400.css").toExternalForm());
            tvInvoiceProductHistory.getStylesheets().add(GenivisApplication.class.getResource("ui/css/purchase_challan_css/supplier_table_css/supplier_table_1369_1400.css").toExternalForm());
            purScrollPane.getStylesheets().add(GenivisApplication.class.getResource("ui/css/TranxCommonCssStyles/tranxCommonCssStyle4.css").toExternalForm());
        } else if (width >= 1401 && width <= 1440) {
            so2itopFirstRow.setSpacing(15);
            so2itopSecondRow.setSpacing(15);
            so2iTotalMainDiv.setSpacing(15);
            so2iTotalMainInnerDiv.setSpacing(8);
            // Set preferred widths as percentages of the HBox width
            so2iBottomFirstV.prefWidthProperty().bind(so2iBottomMain.widthProperty().multiply(0.58));
            so2iBottomSecondV.prefWidthProperty().bind(so2iBottomMain.widthProperty().multiply(0.20));
            so2iBottomThirdV.prefWidthProperty().bind(so2iBottomMain.widthProperty().multiply(0.22));
//            so2cBottomMain.setPrefHeight(150);
            tvGST_Table.getStylesheets().add(GenivisApplication.class.getResource("ui/css/purchase_challan_css/gst_table_css/gst_table_1401_1440.css").toExternalForm());
            tvInvoiceProductHistory.getStylesheets().add(GenivisApplication.class.getResource("ui/css/purchase_challan_css/supplier_table_css/supplier_table_1401_1440.css").toExternalForm());
            purScrollPane.getStylesheets().add(GenivisApplication.class.getResource("ui/css/TranxCommonCssStyles/tranxCommonCssStyle5.css").toExternalForm());
        } else if (width >= 1441 && width <= 1680) {
            so2itopFirstRow.setSpacing(20);
            so2itopSecondRow.setSpacing(20);
            so2iTotalMainDiv.setSpacing(20);
            so2iTotalMainInnerDiv.setSpacing(10);
            // Set preferred widths as percentages of the HBox width
            so2iBottomFirstV.prefWidthProperty().bind(so2iBottomMain.widthProperty().multiply(0.62));
            so2iBottomSecondV.prefWidthProperty().bind(so2iBottomMain.widthProperty().multiply(0.18));
            so2iBottomThirdV.prefWidthProperty().bind(so2iBottomMain.widthProperty().multiply(0.20));
//            so2cBottomMain.setPrefHeight(150);
            tvGST_Table.getStylesheets().add(GenivisApplication.class.getResource("ui/css/purchase_challan_css/gst_table_css/gst_table_1441_1680.css").toExternalForm());
            tvInvoiceProductHistory.getStylesheets().add(GenivisApplication.class.getResource("ui/css/purchase_challan_css/supplier_table_css/supplier_table_1441_1680.css").toExternalForm());
            purScrollPane.getStylesheets().add(GenivisApplication.class.getResource("ui/css/TranxCommonCssStyles/tranxCommonCssStyle6.css").toExternalForm());
        } else if (width >= 1681 && width <= 1920) {
            so2itopFirstRow.setSpacing(20);
            so2itopSecondRow.setSpacing(20);
            so2iTotalMainDiv.setSpacing(20);
            so2iTotalMainInnerDiv.setSpacing(10);
            // Set preferred widths as percentages of the HBox width
            so2iBottomFirstV.prefWidthProperty().bind(so2iBottomMain.widthProperty().multiply(0.6));
            so2iBottomSecondV.prefWidthProperty().bind(so2iBottomMain.widthProperty().multiply(0.18));
            so2iBottomThirdV.prefWidthProperty().bind(so2iBottomMain.widthProperty().multiply(0.22));
//            so2cBottomMain.setPrefHeight(150);
            tvGST_Table.getStylesheets().add(GenivisApplication.class.getResource("ui/css/gst_table.css").toExternalForm());
            tvInvoiceProductHistory.getStylesheets().add(GenivisApplication.class.getResource("ui/css/invoice_product_history_table.css").toExternalForm());
            purScrollPane.getStylesheets().add(GenivisApplication.class.getResource("ui/css/TranxCommonCssStyles/tranxCommonCssStyle7.css").toExternalForm());
        }
    }

    private void tfInvoiceDateValidation() {
        tfInvoiceDate.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal) {
                if (tfInvoiceDate.getText().isEmpty()) {
                    tfInvoiceDate.requestFocus();
                } else {
                    validateInvoiceDate();
                }
            }
        });
    }

    private void tfInvoiceValidatition() {
        tfInvoiceNo.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal) {
                if (tfInvoiceNo.getText().isEmpty()) {
                    tfInvoiceNo.requestFocus();
                } else {
                    String btText = btnSubmit.getText();
                    if (btText.equalsIgnoreCase("Submit")) {
                        validatePurchaseInvoiceNo();
                    } else {
                        validatePurchaseInvoiceUpdate();
                    }
                }
            }
        });
    }

    private void setGSTFooterPart() {
        tvGST_Table.setFocusTraversable(false);
        tc_gst.setSortable(false);
        tc_cgst.setSortable(false);
        tc_sgst.setSortable(false);
        tc_igst.setSortable(false);

        tc_gst.setCellValueFactory(cellData -> {
            StringProperty taxProperty = cellData.getValue().taxPerProperty();
            return Bindings.createStringBinding(

                    () -> "(" + (int) Double.parseDouble(!taxProperty.get().isEmpty() ? taxProperty.get() : "0.0") + " %)", taxProperty);
        });

        tc_gst.setStyle("-fx-alignment: CENTER;");
        tc_cgst.setCellValueFactory(cellData -> cellData.getValue().cgstProperty());
        tc_cgst.setCellFactory(col -> new TableCell<GstDTO, String>() {
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


        tc_cgst.setStyle("-fx-alignment: CENTER;");
        tc_sgst.setCellValueFactory(cellData -> cellData.getValue().sgstProperty());
        tc_sgst.setStyle("-fx-alignment: CENTER;");

        tc_sgst.setCellFactory(col -> new TableCell<GstDTO, String>() {
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

//    private void getPaymentModes() {
//        cmbPaymentMode.setItems(payment_mode_list);
//    }

    private void initialEnterMethod() {
        purScrollPane.addEventFilter(KeyEvent.KEY_PRESSED, (KeyEvent event) -> {
            if (event.getCode() == KeyCode.ENTER) {
                if (event.getTarget() instanceof Button targetButton && targetButton.getText().equalsIgnoreCase("modify")) {
                } else if (event.getTarget() instanceof Button targetButton && targetButton.getText().equalsIgnoreCase("submit")) {
                } else if (event.getTarget() instanceof Button targetButton && targetButton.getText().equalsIgnoreCase("cancel")) {
                } else if (event.getTarget() instanceof Button targetButton && targetButton.getText().equalsIgnoreCase("update")) {
                } else {
                    KeyEvent newEvent = new KeyEvent(null, null, KeyEvent.KEY_PRESSED, "", "\t", KeyCode.TAB, event.isShiftDown(), event.isControlDown(), event.isAltDown(), event.isMetaDown());
                    Event.fireEvent(event.getTarget(), newEvent);
                    event.consume();
                }
            } else if (event.isControlDown() && event.getCode() == KeyCode.S) {
//                String msg = "Do you want to Create ";
////                if (purchase_id > 0) msg = "Do you want to update ";
//                AlertUtility.AlertConfirmation(AlertUtility.alertTypeConfirmation, msg , input -> {
//                    if (input == 1) {
//                        invoiceValidations();
//                    }
//                });
                btnSubmit.fire();
            }
            else if (event.isControlDown() && event.getCode() == KeyCode.X) {
                btnCancel.fire();
            }
        });

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
            System.out.println("iddd in supplier list-- "+id);
            map.put("productId", id);
            String formData = Globals.mapToStringforFormData(map);
            logger.debug("Form Data for getSupplierListbyProductId in Purchase Invoice Controller:" + formData);
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
                        cmSupplierName.setCellValueFactory(new PropertyValueFactory<>("supplier_name"));
                        cmInvNo.setCellValueFactory(new PropertyValueFactory<>("invoice_no"));
                        cmInvDate.setCellValueFactory(new PropertyValueFactory<>("invoice_date"));
                        cmBatch.setCellValueFactory(new PropertyValueFactory<>("batch"));
                        cmMRP.setCellValueFactory(new PropertyValueFactory<>("mrp"));
                        cmQty.setCellValueFactory(new PropertyValueFactory<>("qty"));
                        cmRate.setCellValueFactory(new PropertyValueFactory<>("rate"));
                        cmCost.setCellValueFactory(new PropertyValueFactory<>("cost"));
                        cmDisPer.setCellValueFactory(new PropertyValueFactory<>("dis_per"));
                        cmDisAmt.setCellValueFactory(new PropertyValueFactory<>("dis_amt"));

                        tvInvoiceProductHistory.setItems(supplierDataList);

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

    private void getSaleInvoiceByIdNew() {

        try {
            System.out.println("input in getPurchaseInvoiceByIdNew " + input);
            Map<String, String> map = new HashMap<>();
            map.put("sales_order_ids", input);
            String formData = mapToStringforFormData(map);

            HttpResponse<String> response = APIClient.postFormDataRequest(formData, "get_sale_orders_with_ids");
            System.out.println("TRANX_SALES_INVOICE_EDIT_BY_ID response body => " + response.body());
            SalesEditResponseDTO1 salesEditResponseDTO = new Gson().fromJson(response.body(), SalesEditResponseDTO1.class);

            if (salesEditResponseDTO.getResponseStatus() == 200) {
                setEditResponse(salesEditResponseDTO);
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setEditResponse(SalesEditResponseDTO1 purchaseEditResponseDTO) {

        System.out.println("tranxDatee " + purchaseEditResponseDTO.getInvoiceData().getInvoiceDt());
        LocalDate tranxDate = LocalDate.parse(purchaseEditResponseDTO.getInvoiceData().getTransactionDt());
        tfTranxDate.setText(tranxDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        LocalDate invoiceDate = LocalDate.parse(purchaseEditResponseDTO.getInvoiceData().getInvoiceDt());
        tfInvoiceDate.setText(invoiceDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));

//        edit_response_map.put("paymentMode", purchaseEditResponseDTO.getPaymentMode());
        edit_response_map.put("discountLedgerId", purchaseEditResponseDTO.getDiscountLedgerId());
        edit_response_map.put("discountInAmt", purchaseEditResponseDTO.getDiscountInAmt());
        edit_response_map.put("discountInPer", purchaseEditResponseDTO.getDiscountInPer());
        edit_response_map.put("totalPurchaseDiscountAmt", purchaseEditResponseDTO.getTotalPurchaseDiscountAmt());
        edit_response_map.put("totalQty", purchaseEditResponseDTO.getTotalQty());
        edit_response_map.put("totalFreeQty", purchaseEditResponseDTO.getTotalFreeQty());
        edit_response_map.put("grossTotal", purchaseEditResponseDTO.getGrossTotal());
        edit_response_map.put("totalTax", purchaseEditResponseDTO.getTotalTax());
        edit_response_map.put("additionLedger1", purchaseEditResponseDTO.getAdditionLedger1());
        edit_response_map.put("additionLedgerAmt1", purchaseEditResponseDTO.getAdditionLedgerAmt1());
        edit_response_map.put("additionLedger2", purchaseEditResponseDTO.getAdditionLedger2());
        edit_response_map.put("additionLedgerAmt2", purchaseEditResponseDTO.getAdditionLedgerAmt2());
        edit_response_map.put("additionLedger3", purchaseEditResponseDTO.getAdditionLedger3());
        edit_response_map.put("additionLedgerAmt3", purchaseEditResponseDTO.getAdditionLedgerAmt3());
        edit_response_map.put("debitNoteReference", purchaseEditResponseDTO.getDebitNoteReference());
//        edit_response_map.put("reference_type", purchaseEditResponseDTO.getRow().getReferenceType());
//        edit_response_map.put("reference_id", purchaseEditResponseDTO.getRow().getReferenceId());

        rowListDTOS.addAll(purchaseEditResponseDTO.getRow());

        String product_id = String.valueOf(purchaseEditResponseDTO.getRow().get(0).getProductId());
        fetchSelectedProductData(product_id);
        edit_response_map.put("invoiceData", purchaseEditResponseDTO.getInvoiceData());
        SalesInvoiceDataResDTO1 invoiceDataResDTO = purchaseEditResponseDTO.getInvoiceData();
        invoice_data_map.put("id", String.valueOf(invoiceDataResDTO.getId()));
        invoice_data_map.put("invoice_dt", String.valueOf(invoiceDataResDTO.getInvoiceDt()));
        invoice_data_map.put("bill_dt", String.valueOf(invoiceDataResDTO.getInvoiceDt()));
        invoice_data_map.put("narration",invoiceDataResDTO.getNarration());
//        invoice_data_map.put("bill_no", String.valueOf(invoiceDataResDTO.getInvoiceNo()));              //to be uncomment
//        invoice_data_map.put("tranx_unique_code", String.valueOf(invoiceDataResDTO.getTranxUniqueCode()));  //to be uncomment
        invoice_data_map.put("sales_sr_no", String.valueOf(invoiceDataResDTO.getPurchaseSrNo()));
//        invoice_data_map.put("purchase_account_ledger_id", String.valueOf(invoiceDataResDTO.getPurchaseAccountLedgerId()));
        System.out.println("sales_acc_id== "+invoiceDataResDTO.getSalesAccountId());
        invoice_data_map.put("sales_acc_id", String.valueOf(invoiceDataResDTO.getSalesAccountId()));
        purchaseAcID = String.valueOf(invoiceDataResDTO.getSalesAccountId());

        invoice_data_map.put("supplierId",String.valueOf(invoiceDataResDTO.getDebtorsId()));
        ledger_id = invoice_data_map.get("supplierId");
        tfLedgerName.setText(invoiceDataResDTO.getDebtorsName());
        fetchSelectedLedgerData(ledger_id);
        ledgersById(Long.valueOf(ledger_id));

        tfNarration.setText(invoiceDataResDTO.getNarration() != null ? invoiceDataResDTO.getNarration() : "");
        invoice_data_map.put("transaction_dt", String.valueOf(invoiceDataResDTO.getTransactionDt()));
        invoice_data_map.put("gstNo", String.valueOf(invoiceDataResDTO.getGstNo()));

//        invoice_data_map.put("isRoundOffCheck", String.valueOf(invoiceDataResDTO.getIsRoundOffCheck()));
//        invoice_data_map.put("roundoff", String.valueOf(invoiceDataResDTO.getRoundoff()));
//        invoice_data_map.put("mode", String.valueOf(invoiceDataResDTO.getMode()));
//        invoice_data_map.put("image", String.valueOf(invoiceDataResDTO.getImage()));
//        edit_response_map.put("bills", purchaseEditResponseDTO.getBills());
//        edit_response_map.put("barcodeList", purchaseEditResponseDTO.getBarcodeList());
//        edit_response_map.put("additionalCharges", purchaseEditResponseDTO.getAdditionalCharges());

    }

    private void fetchSelectedLedgerData(String LedgerId) {                   //function for set the bottom ledger data
        try {
            logger.info("Fetching Selected Ledger Data to show in Ledger info");
            Map<String, String> body = new HashMap<>();
            body.put("ledger_id", LedgerId);
//            System.out.println("ledger_id 123 " + LedgerId);
            String requestBody = Globals.mapToStringforFormData(body);
            HttpResponse<String> response = APIClient.postFormDataRequest(requestBody, EndPoints.TRANSACTION_LEDGER_DETAILS);
            logger.info("response data of selected Ledger " + response.body());
            JsonObject jsonObject = new Gson().fromJson(response.body(), JsonObject.class);
            ObservableList<TranxLedgerWindowDTO> observableList = FXCollections.observableArrayList();
            System.out.println("fetchedSelectedLedgerData " + jsonObject);
            if (jsonObject.get("responseStatus").getAsInt() == 200) {
                //todo: activating the ledger tab
                tranxPurInvTabPane.getSelectionModel().select(tabPurInvLedger);
                CompanyStateCode = jsonObject.get("company_state_code").getAsLong();
                System.out.println("CompanyStateCode " + CompanyStateCode);
                JsonObject item = jsonObject.get("result").getAsJsonObject();
                stateCode = item.get("stateCode").getAsLong();
//                System.out.println("stateCode " + stateCode);
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

//setting data in Ledger details block in sales Order to invoice page
                System.out.println("gstNo=" + gstNo + " Area=" + Area + " bank=" + bank + " contact_person=" + contact_person + " credit_days=" + credit_days + " fssai=" + fssai +
                        " licence_no=" + licence_no + " route=" + route);
                lblPurInvGstNo.setText(gstNo);
                lblPurInvArea.setText(Area);
                lblPurInvBank.setText(bank);
                lblPurInvContactPerson.setText(contact_person);
                lblPurInvTransportName.setText("");
                lblPurInvCreditDays.setText(credit_days);
                lblPurInvFssai.setText(fssai);
                lblPurInvLicenseNo.setText(licence_no);
                lblPurInvRoute.setText(route);

            } else {

                logger.error("Error in response of fetching selected Ledger data");
            }
        } catch (Exception e) {
            logger.error("Error in Fetching Selected Ledger data is" + e.getMessage());
            e.printStackTrace();
        }
    }

    //function for fetch selected product data to the bottom
    private void fetchSelectedProductData(String productId) {
        try {
            logger.info("Fetching Selected Product Data to show in product info");
            Map<String, String> body = new HashMap<>();
            body.put("product_id", productId);
            System.out.println("ProductId 123 " + productId);
            String requestBody = Globals.mapToStringforFormData(body);
            HttpResponse<String> response = APIClient.postFormDataRequest(requestBody, EndPoints.TRANSACTION_PRODUCT_DETAILS);
            JsonObject jsonObject = new Gson().fromJson(response.body(), JsonObject.class);
            System.out.println("jsonObject of Product details " + jsonObject);
            ObservableList<TranxProductWindowDTO> observableList = FXCollections.observableArrayList();
            //todo: activating the product tab
//            tpPurOrderToInvoice.getSelectionModel().select(tabPurOrderToInvoiceProduct);
            if (jsonObject.get("responseStatus").getAsInt() == 200) {
                tranxPurInvTabPane.getSelectionModel().select(tabPurInvProduct);

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
                lblPurInvProductBrand.setText(brand);
                lblPurInvProductGroup.setText(group);
                lblPurInvProductSubGroup.setText(subgroup);
                lblPurInvProductCategory.setText(category);
                lblPurInvProductHsn.setText(hsn);
                lblPurInvProductTaxType.setText(tax_type);
                lblPurInvProductTaxPer.setText(tax);
                lblPurInvProductMarginPer.setText(margin);
                lblPurInvProductCost.setText(cost);
                lblPurInvProductShelfId.setText(shelfId);
                lblPurInvProductMinStock.setText(min_stock);
                lblPurInvProductMaxStock.setText(max_stock);

            } else {
                System.out.println("Error in response");
            }
        } catch (Exception e) {
            logger.error("Error in Fetching Selected Product data is" + e.getMessage());
            e.printStackTrace();
        }
    }

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

    private void invoiceProductHistoryTable() {

        tvInvoiceProductHistory.setFocusTraversable(false);
        cmSupplierName.setCellValueFactory(cellData -> cellData.getValue().supplier_nameProperty());
        cmSupplierName.setStyle("-fx-alignment: CENTER;");
        cmInvNo.setCellValueFactory(cellData -> cellData.getValue().invoice_noProperty());
        cmInvNo.setStyle("-fx-alignment: CENTER;");
        cmInvDate.setCellValueFactory(cellData -> cellData.getValue().invoice_dateProperty());
        cmInvDate.setStyle("-fx-alignment: CENTER;");
        cmBatch.setCellValueFactory(cellData -> cellData.getValue().batchProperty());
        cmBatch.setStyle("-fx-alignment: CENTER;");
        cmMRP.setCellValueFactory(cellData -> cellData.getValue().mrpProperty());
        cmMRP.setStyle("-fx-alignment: CENTER;");
        cmQty.setCellValueFactory(cellData -> cellData.getValue().qtyProperty());
        cmQty.setStyle("-fx-alignment: CENTER;");
        cmRate.setCellValueFactory(cellData -> cellData.getValue().rateProperty());
        cmRate.setStyle("-fx-alignment: CENTER;");
        cmCost.setCellValueFactory(cellData -> cellData.getValue().costProperty());
        cmCost.setStyle("-fx-alignment: CENTER;");
        cmDisPer.setCellValueFactory(cellData -> cellData.getValue().dis_perProperty());
        cmDisPer.setStyle("-fx-alignment: CENTER;");
        cmDisAmt.setCellValueFactory(cellData -> cellData.getValue().dis_amtProperty());
        cmDisAmt.setStyle("-fx-alignment: CENTER;");
    }

    public void sceneInitilization() {
        purScrollPane.sceneProperty().addListener((observable, oldScene, newScene) -> {
            if (newScene != null && newScene.getWindow() instanceof Stage) {
                Communicator.stage = (Stage) newScene.getWindow();
                Communicator.tranxDate = tfTranxDate.getText();
            }
        });
    }

    public void handlePayAction(ActionEvent actionEvent) {
        RadioButton selectedRadioButton = (RadioButton) toggleGroup.getSelectedToggle();
        if (selectedRadioButton != null) {
            selectedPayMode = selectedRadioButton.getText();
        }
    }


//    public void handleTfLedgerName() {
//        Stage stage = (Stage) purScrollPane.getScene().getWindow();
//        SingleInputDialogs.openSalesLedgerWithNamePopUp(stage, "Filter", ledgerName, input -> {
//            logger.debug("Callback to Get all the Details of the Selected ledger and Set it in the Fields", input);
//            ledgerName = (String) input[0];
//            ledger_id = (String) input[1];
//            ledgerStateCode = (String) input[6];
//            sales_qoutation.setText((String) input[3]);
//            sales_orders.setText((String) input[4]);
//            sales_challan.setText((String) input[5]);
//            tfLedgerName.setText(ledgerName);
//            ObservableList<GstDetailsDTO> gstDetailsDTOS = (ObservableList<GstDetailsDTO>) input[2];
//
//            for (GstDetailsDTO gstDetailsDTO : gstDetailsDTOS) {
//                supplierGSTINList.add(new CommonDTO(gstDetailsDTO.getGstNo(), gstDetailsDTO.getId()));
//            }
//            System.out.println(" supplierGSTINList >> : " + supplierGSTINList.size());
//            if (supplierGSTINList != null && !supplierGSTINList.isEmpty()) {
//                cbSupplierGSTIN.setItems(supplierGSTINList);
//                cbSupplierGSTIN.setValue(supplierGSTINList.get(0));
//                if (supplierGSTINList.size() > 1) {
//                    cbSupplierGSTIN.requestFocus();
//                } else {
//                    tfInvoiceNo.requestFocus();
//                }
//            } else {
//                tfInvoiceNo.requestFocus();
//            }
//            if (ledgerStateCode.equalsIgnoreCase(GlobalTranx.getCompanyStateCode().toString())) {
//                taxFlag = true;
//            }
//            logger.debug("Opened Ledger Bill Modal:On Clicking by selecting the Ledger Name ");
////            TranxCal();
//            tranxLedgerDetailsFun(ledger_id);
//        },isRedirect->{
//            System.out.println("isRedirect0"+isRedirect);
//        });
//
//    }

    private void tranxLedgerDetailsFun(String id) {
        tranxPurInvTabPane.getSelectionModel().select(tabPurInvLedger);

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

                        lblPurInvGstNo.setText(jsonObj.get("gst_number").getAsString());
                        lblPurInvArea.setText(jsonObj.get("area").getAsString());
                        lblPurInvBank.setText(jsonObj.get("bank_name").getAsString());
                        lblPurInvContactPerson.setText(jsonObj.get("contact_name").getAsString());
                        lblPurInvCreditDays.setText(jsonObj.get("credit_days").getAsString());
                        lblPurInvFssai.setText(jsonObj.get("fssai_number").getAsString());
                        lblPurInvLicenseNo.setText(jsonObj.get("license_number").getAsString());
                        lblPurInvRoute.setText(jsonObj.get("route").getAsString());
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
                if (current_node instanceof RadioButton radioButton) {
                    radioButton.setSelected(!radioButton.isSelected());
                    radioButton.fire();
                }
            }
        });
    }

    public void getPurchaseAc() {

        try {

            HttpResponse<String> response = APIClient.getRequest(EndPoints.SALESORDER_GET_SALES_ACCOUNTS);
            PurchaseAcRes purchaseAcRes = new Gson().fromJson(response.body(), PurchaseAcRes.class);

            if (purchaseAcRes.getResponseStatus() == 200) {

                List<PurchaseAcList> purchaseAcResLists = purchaseAcRes.getList();

                for (PurchaseAcList purchaseAcList1 : purchaseAcResLists) {
                    purchaseAcList.add(new CommonDTO(purchaseAcList1.getName(), purchaseAcList1.getId().toString()));
                }
                cbPurchaseAc.setItems(purchaseAcList);
                //  AutoCompleteBox<CommonDTO> autoCompleteBox = new AutoCompleteBox<>(cbPurchaseAc, -1);

            } else {
                System.out.println("responseObject is null");
            }
            cbPurchaseAc.getSelectionModel().selectFirst();
            System.out.println("Purchase Acc Id:" + cbPurchaseAc.getSelectionModel().getSelectedItem());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void getPurchaseSerial() {

        try {
            HttpResponse<String> response = APIClient.getRequest(EndPoints.TRANX_SALES_INVOICE_LAST_RECORD);
            PurchaseSerialRes purchaseSerialRes = new Gson().fromJson(response.body(), PurchaseSerialRes.class);

            if (purchaseSerialRes.getResponseStatus() == 200) {
                tfPurchaseSerial.setText(String.valueOf(purchaseSerialRes.getCount()));
                tfInvoiceNo.setText(String.valueOf(purchaseSerialRes.getSerialNo()));
//                tfTranxSalesInvoiceCreateInvoiceNo.setText(tranxSalesInvoiceLastRecordRes.getSerialNo());
//                tfTranxSalesInvoiceCreateInvoiceSrNo.setText("" + tranxSalesInvoiceLastRecordRes.getCount());
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
        System.out.println("Pur Acc :" + object[0].toString());
        if (object[0] != null) {
            for (CommonDTO commonDTO : purchaseAcList) {
                if (object[0].toString().equalsIgnoreCase(commonDTO.getText())) {
                    purchaseAcID = commonDTO.getId();
                }
            }
        }
        nodetraversal(cbSupplierGSTIN, tfInvoiceNo);
    }

    TableCellCallback<Object[]> callback = item -> {

        System.out.println("item: 0" + item[0]);
        System.out.println("item: 1" + item[1]);
        System.out.println("item: 2" + item[2]);
        System.out.println("item: 3" + item[3]);
        System.out.println("item: 4" + item[4]);
        System.out.println("item: 5" + item[5]);
        System.out.println("item: 6" + item[6]);
        System.out.println("item: 7" + item[7]);
        System.out.println("item: 8" + item[8]);
        System.out.println("taxCalculation 9" + item[9]);

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
            tc_cgst.setVisible(false);
            tc_sgst.setVisible(false);
        } else {
            tc_igst.setVisible(false);
            tc_cgst.setVisible(true);
            tc_sgst.setVisible(true);
        }

        if (ledgerStateCode.equalsIgnoreCase(GlobalTranx.getCompanyStateCode().toString())) {
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
        } else {
            JsonArray jsonArray = new JsonArray();
            for (GstDTO gstDTO : gstDTOObservableList) {
                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("d_gst", decimalFormat.format(Double.parseDouble(gstDTO.getTaxPer())));
                jsonObject.addProperty("gst", decimalFormat.format(Double.parseDouble(gstDTO.getTaxPer())));
                jsonObject.addProperty("amt", gstDTO.getIgst());
                jsonArray.add(jsonObject);
            }
            JsonObject jsonObject = new JsonObject();
            jsonObject.add("igst", jsonArray);
//            jsonObject.add("sgst", jsonArray);
            purchase_invoice_map.put("taxCalculation", jsonObject.toString());
        }

        lbBillAmount.setText((String) item[2]); //totalNetAmt for bill amount
        totalamt = (String) item[2];

        total_purchase_discount_amt = (String) item[4];

        System.out.println("ledgerStateCode =>" + ledgerStateCode + "GlobalTranx.getCompanyStateCode() =>" + GlobalTranx.getCompanyStateCode());


        if (ledgerStateCode.equalsIgnoreCase(GlobalTranx.getCompanyStateCode().toString())) {
            taxFlag = true;
        }

        purchase_invoice_map.put("total_qty", (String) item[0]);
        purchase_invoice_map.put("total_free_qty", (String) item[1]);

        purchase_invoice_map.put("total_base_amt", (String) item[3]);
        purchase_invoice_map.put("total_sales_discount_amt", (String) item[4]);
        purchase_invoice_map.put("taxable_amount", (String) item[5]);
        purchase_invoice_map.put("bill_amount", (String) item[2]);
        purchase_invoice_map.put("total_tax_amt", (String) item[6]);

        purchase_invoice_map.put("totaligst", (String) item[6]);
        purchase_invoice_map.put("totalsgst", (String) item[7]);
        purchase_invoice_map.put("totalcgst", (String) item[8]);


        purchase_invoice_map.put("taxFlag", String.valueOf(taxFlag));

        total_taxable_amt = Double.parseDouble((String) item[5]);

        lblTotalQty.setText((String) item[0]);
        lblTotalFreeQty.setText((String) item[1]);
        lbGrossTotal.setText((String) item[3]);
        lbDiscount.setText((String) item[4]);
        lbTotal.setText((String) item[5]);
        lbTax.setText((String) item[6]);
        chbPurInvRoundOff.setSelected(true);
        roundOffCalculations();
    };
    TableCellCallback<Object[]> delCallback = item -> {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("del_id", item[0].toString());
        deletedRows.add(jsonObject);
    };

    private void roundOffCalculations() {
        Double billamt = 0.00; // local variable to store row wise  Bill amount
        Double total_bill_amt = 0.00; // local variable to store Final bill Amount without roundoff
        Double final_r_off_bill_amt = 0.00; // local variable to store Final Bill amount with round off
        Double roundOffAmt = 0.00; // local variable to store Roundoff Amount
        for (SalesInvoiceTable cmpTRowDTO : tvPurchaseInvoiceTable.getItems()) {
            billamt = Double.valueOf(cmpTRowDTO.getNet_amount());
            total_bill_amt += billamt;
        }
        final_r_off_bill_amt = (double) Math.round(total_bill_amt);
        roundOffAmt = final_r_off_bill_amt - total_bill_amt;

        if (chbPurInvRoundOff.isSelected()) {
            lbBillAmount.setText(String.format("%.2f", final_r_off_bill_amt));
            lblPurInvRoundOff.setText(String.format("%.2f", roundOffAmt));
        } else {
            lbBillAmount.setText(String.format("%.2f", total_bill_amt));
            lblPurInvRoundOff.setText("0.00");
        }

    }

    //new start
    TableCellCallback<Integer> unit_callback = currentIndex -> {
        SalesInvoiceTable tranxRow = tvPurchaseInvoiceTable.getItems().get(currentIndex);
        System.out.println("ledgerStateCode in unit callback " + ledgerStateCode);
        //! ? Ledger State Code get selected
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

        tvPurchaseInvoiceTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);
        tvPurchaseInvoiceTable.setEditable(true);
        tvPurchaseInvoiceTable.setFocusTraversable(false);


        Label headerLabel = new Label("Sr\nNo.");
        tcSrNo.setGraphic(headerLabel);

        int count = 1;
        int index = 0;
        for (SalesRowListDTO rowListDTO : rowListDTOS) {

            SalesInvoiceTable purchaseInvoiceTable = new SalesInvoiceTable();
            purchaseInvoiceTable.setSr_no(String.valueOf(count));
            purchaseInvoiceTable.setDetails_id(String.valueOf(rowListDTO.getDetailsId()));
            purchaseInvoiceTable.setProduct_id(String.valueOf(rowListDTO.getProductId()));
            purchaseInvoiceTable.setParticulars(String.valueOf(rowListDTO.getProductName()));

            purchaseInvoiceTable.setLevelA_id(String.valueOf(rowListDTO.getLevelAId()));
            purchaseInvoiceTable.setLevelB_id(String.valueOf(rowListDTO.getLevelBId()));
            purchaseInvoiceTable.setLevelC_id(String.valueOf(rowListDTO.getLevelCId()));
            purchaseInvoiceTable.setUnit_id(String.valueOf(rowListDTO.getUnitId()));
            purchaseInvoiceTable.setUnit_conv(String.valueOf(rowListDTO.getUnitConv()));

            purchaseInvoiceTable.setPackages(String.valueOf(rowListDTO.getPacking()));

            System.out.println("getRowRateee " + rowListDTO.getRate());
            purchaseInvoiceTable.setQuantity(String.valueOf(rowListDTO.getQty()));
            purchaseInvoiceTable.setRate(String.valueOf(rowListDTO.getRate()));
            purchaseInvoiceTable.setBase_amt(String.valueOf(rowListDTO.getBaseAmt()));

            purchaseInvoiceTable.setDis_amt(String.valueOf(rowListDTO.getDisAmt()));
            purchaseInvoiceTable.setDis_per(String.valueOf(rowListDTO.getDisPer()));
            purchaseInvoiceTable.setDis_per_cal(String.valueOf(rowListDTO.getDisPerCal()));
            purchaseInvoiceTable.setDis_amt_cal(String.valueOf(rowListDTO.getDisAmtCal()));
            purchaseInvoiceTable.setTotal_amt(String.valueOf(rowListDTO.getTotalAmt()));


            purchaseInvoiceTable.setGst(String.valueOf(rowListDTO.getGst()));
            purchaseInvoiceTable.setIgst_per(String.valueOf(rowListDTO.getIgst()));  //igst in perc
            purchaseInvoiceTable.setSgst_per(String.valueOf(rowListDTO.getSgst()));
            System.out.println("rowListDTO.getCgst()-- "+rowListDTO.getCgst()+" totalCgst-- "+rowListDTO.getTotalCgst());
            purchaseInvoiceTable.setCgst_per(String.valueOf(rowListDTO.getCgst()));

            purchaseInvoiceTable.setIgst(String.valueOf(rowListDTO.getIgst()));    //igst in amount
            purchaseInvoiceTable.setCgst(String.valueOf(rowListDTO.getCgst()));
            purchaseInvoiceTable.setSgst(String.valueOf(rowListDTO.getSgst()));

            purchaseInvoiceTable.setNet_amount(String.valueOf(rowListDTO.getFinalAmt()));
            purchaseInvoiceTable.setFree(String.valueOf(rowListDTO.getFreeQty()));
            purchaseInvoiceTable.setRow_dis_amt(String.valueOf(rowListDTO.getRowDisAmt()));

            purchaseInvoiceTable.setGross_amount(String.valueOf(rowListDTO.getGrossAmt()));
            purchaseInvoiceTable.setGross_amount1(String.valueOf(rowListDTO.getGrossAmt()));
            purchaseInvoiceTable.setReference_type(rowListDTO.getReferenceType());
            purchaseInvoiceTable.setReference_id(rowListDTO.getReferenceId().toString());

//            purchaseInvoiceTable.setAdd_chg_amt(String.valueOf(rowListDTO.getAddChgAmt()));

            purchaseInvoiceTable.setInvoice_dis_amt(String.valueOf(rowListDTO.getInvoiceDisAmt()));
            purchaseInvoiceTable.setInventoryId(String.valueOf(rowListDTO.getInventoryId()));

            purchaseInvoiceTable.setTransaction_status(String.valueOf(rowListDTO.getTransactionStatus()));

            purchaseInvoiceTable.setIs_expired(String.valueOf(rowListDTO.getIsExpired()));

            purchaseInvoiceTable.setB_details_id(String.valueOf(rowListDTO.getbDetailsId()));

            purchaseInvoiceTable.setB_no(String.valueOf(rowListDTO.getBatchNo()));
//                purchaseInvoiceTable.setBatch_or_serial(String.valueOf(rowListDTO.getBatchNo()));   //dont set the batch initially
            purchaseInvoiceTable.setB_expiry(String.valueOf(rowListDTO.getbExpiry()));
            purchaseInvoiceTable.setB_rate(String.valueOf(rowListDTO.getbRate()));
            purchaseInvoiceTable.setB_purchase_rate(String.valueOf(rowListDTO.getPurchaseRate()));
            purchaseInvoiceTable.setIs_batch(String.valueOf(rowListDTO.getIsBatch()));

            purchaseInvoiceTable.setRate_a(String.valueOf(rowListDTO.getMinRateA() == null ? "" : rowListDTO.getMinRateA()));
            purchaseInvoiceTable.setRate_b(String.valueOf(rowListDTO.getMinRateB() == null ? "" : rowListDTO.getMinRateB()));
            purchaseInvoiceTable.setRate_c(String.valueOf(rowListDTO.getMinRateC() == null ? "" : rowListDTO.getMinRateB()));

            purchaseInvoiceTable.setManufacturing_date(String.valueOf(rowListDTO.getManufacturingDate()));
            purchaseInvoiceTable.setMin_margin(String.valueOf(rowListDTO.getMarginPer()));

            purchaseInvoiceTable.setCosting(String.valueOf(rowListDTO.getCosting()));
            purchaseInvoiceTable.setCosting_with_tax(String.valueOf(rowListDTO.getCostingWithTax()));
            purchaseInvoiceTable.setLedger_id(invoice_data_map.get("supplierId"));
            purchaseInvoiceTable.setTax(String.valueOf(rowListDTO.getGst()));

            //!Sales Invoice
            TranxSelectedProduct selectedProduct = TranxCommonPopUps.getSelectedProductFromProductId(rowListDTO.getProductName());
            purchaseInvoiceTable.setSelectedProduct(selectedProduct);

            NegBatchWindowTableDTO batchWindowTableDTO = new NegBatchWindowTableDTO();

            batchWindowTableDTO.setProductId(purchaseInvoiceTable.getProduct_id());
            batchWindowTableDTO.setLevelAId(purchaseInvoiceTable.getLevelA_id());
            batchWindowTableDTO.setLevelBId(purchaseInvoiceTable.getLevelB_id());
            batchWindowTableDTO.setLevelCId(purchaseInvoiceTable.getLevelC_id());
            batchWindowTableDTO.setUnitId(purchaseInvoiceTable.getUnit_id());

            System.out.println("getBatch_or_serial= " + purchaseInvoiceTable.getBatch_or_serial() + " batchDetid " + purchaseInvoiceTable.getB_details_id());
//            batchWindowTableDTO.setBatchNo(purchaseInvoiceTable.getBatch_or_serial());
//            batchWindowTableDTO.setB_details_id(purchaseInvoiceTable.getB_details_id());

//            batchWindowTableDTO.setMrp(purchaseInvoiceTable.getB_rate());
//            batchWindowTableDTO.setPurchaseRate(purchaseInvoiceTable.getB_purchase_rate());

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

            List<NegBatchWindowTableDTO> listBatch = new ArrayList<>();
            listBatch.add(batchWindowTableDTO);

            purchaseInvoiceTable.setBatchWindowTableDTOList(listBatch);

            tvPurchaseInvoiceTable.getItems().addAll(purchaseInvoiceTable);
            PurchaseInvoiceCalculationSOtoSI.rowCalculationForPurcInvoice(index, tvPurchaseInvoiceTable, callback);

            PurchaseInvoiceCalculationSOtoSI.calculateGst(tvPurchaseInvoiceTable, callback);
            List<LevelAForPurInvoice> levelAForPurInvoiceList = SalesProductUnitsPacking.getAllProductUnitsPackingFlavour(purchaseInvoiceTable.getProduct_id());

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
        setAddChargesInTable();

        tcSrNo.setCellFactory(new LineNumbersCellFactory());

        tcSrNo.setStyle("-fx-alignment: CENTER;");

        tcPackage.setCellValueFactory(cellData -> cellData.getValue().packagesProperty());
        tcPackage.setStyle("-fx-alignment: CENTER;");

        tcLevelA.setCellValueFactory(cellData -> cellData.getValue().levelAProperty());
        tcLevelA.setCellFactory(column -> new SalesComboBoxTableCellForLevelA("tcLevelA"));

        tcLevelB.setCellValueFactory(cellData -> cellData.getValue().levelBProperty());
        tcLevelB.setCellFactory(column -> new SalesComboBoxTableCellForLevelB("tcLevelB"));


        tcLevelC.setCellValueFactory(cellData -> cellData.getValue().levelCProperty());
        tcLevelC.setCellFactory(column -> new SalesComboBoxTableCellForLevelC("tcLevelC"));

        tcUnit.setCellValueFactory(cellData -> cellData.getValue().unitProperty());
        tcUnit.setCellFactory(column -> new SalesComboBoxTableCellForUnit("tcUnit", unit_callback));

        tcParticulars.setCellValueFactory(cellData -> cellData.getValue().particularsProperty());
        tcParticulars.setCellFactory(column -> new TextFieldTableCellForSalesInvoice("tcParticulars", callback, productID_callback, tfNarration));


        tcBatch.setCellValueFactory(cellData -> cellData.getValue().batch_or_serialProperty());
        tcBatch.setCellFactory(column -> new TextFieldTableCellForSalesInvoice("tcBatch", callback, productID_callback, tfNarration));


        tcQuantity.setCellValueFactory(cellData -> cellData.getValue().quantityProperty());
        tcQuantity.setCellFactory(column -> new TextFieldTableCellForSalesInvoice("tcQuantity", callback));
        tcQuantity.setStyle("-fx-alignment: CENTER-RIGHT;");
        tcFreeQuantity.setCellValueFactory(cellData -> cellData.getValue().freeProperty());
        tcFreeQuantity.setCellFactory(column -> new TextFieldTableCellForSalesInvoice("tcFreeQuantity", callback));
        tcFreeQuantity.setStyle("-fx-alignment: CENTER-RIGHT;");


        tcRate.setCellValueFactory(cellData -> cellData.getValue().rateProperty());
        tcRate.setCellFactory(column -> new TextFieldTableCellForSalesInvoice("tcRate", callback));
        tcRate.setStyle("-fx-alignment: CENTER-RIGHT;");

        tcGrossAmount.setCellValueFactory(cellData -> cellData.getValue().gross_amountProperty());
        //  tcGrossAmount.setCellFactory(column -> new TextFieldTableCellForPurInvoice("tcGrossAmount", callback));
        tcGrossAmount.setStyle("-fx-alignment: CENTER-RIGHT;");


        tcDisPer.setCellValueFactory(cellData -> cellData.getValue().dis_perProperty());
        tcDisPer.setCellFactory(column -> new TextFieldTableCellForSalesInvoice("tcDisPer", callback));
        tcDisPer.setStyle("-fx-alignment: CENTER-RIGHT;");


        //   tcDisPer.setCellValueFactory(cellData -> cellData.getValue().dis_perProperty());
        //  tcDisPer.setCellFactory(column -> new TextFieldTableCellForPurInvoice("tcDisPer", callback));

        tcDisAmt.setCellValueFactory(cellData -> cellData.getValue().dis_amtProperty());
        tcDisAmt.setCellFactory(column -> new TextFieldTableCellForSalesInvoice("tcDisAmt", callback));
        tcDisAmt.setStyle("-fx-alignment: CENTER-RIGHT;");


        tcTax.setCellValueFactory(cellData -> cellData.getValue().taxProperty());
        //   tcTax.setCellFactory(column -> new TextFieldTableCellForPurInvoice("tcTax", callback));
        tcTax.setStyle("-fx-alignment: CENTER-RIGHT;");


        tcNetAmount.setCellValueFactory(cellData -> cellData.getValue().net_amountProperty());
        //    tcNetAmount.setCellFactory(column -> new TextFieldTableCellForPurInvoice("tcNetAmount", callback));
        tcNetAmount.setStyle("-fx-alignment: CENTER-RIGHT;");


        tcAction.setCellValueFactory(cellData -> cellData.getValue().net_amountProperty());
        tcAction.setCellFactory(column -> new SalesButtonTableCellDelete(delCallback));
        columnVisibility(tcLevelA, Globals.getUserControlsWithSlug("is_level_a"));
        columnVisibility(tcLevelB, Globals.getUserControlsWithSlug("is_level_b"));
        columnVisibility(tcLevelC, Globals.getUserControlsWithSlug("is_level_c"));
        columnVisibility(tcFreeQuantity, Globals.getUserControlsWithSlug("is_free_qty"));

        tcLevelA.setText(Globals.getUserControlsNameWithSlug("is_level_a"));
        tcLevelB.setText(Globals.getUserControlsNameWithSlug("is_level_B"));
        tcLevelC.setText(Globals.getUserControlsNameWithSlug("is_level_C"));

    }

    private void columnVisibility(TableColumn<SalesInvoiceTable, String> column, boolean visible) {
        if (visible) {
//            column.setPrefWidth(USE_COMPUTED_SIZE);
//            column.setMinWidth(USE_PREF_SIZE);
            column.setMaxWidth(Double.MAX_VALUE);
        } else {
            column.prefWidthProperty().unbind();
            column.setPrefWidth(0);
            column.setMinWidth(0);
            column.setMaxWidth(0);
        }
    }

    private void tranxProductDetailsFun(String id) {
        tranxPurInvTabPane.getSelectionModel().select(tabPurInvProduct);

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
                        lblPurInvProductBrand.setText(productJsonObj.get("brand").getAsString());
                        lblPurInvProductGroup.setText(productJsonObj.get("group").getAsString());
                        lblPurInvProductSubGroup.setText(productJsonObj.get("subgroup").getAsString());
                        lblPurInvProductCategory.setText(productJsonObj.get("category").getAsString());
                        lblPurInvProductHsn.setText(productJsonObj.get("hsn").getAsString());
                        lblPurInvProductTaxType.setText(productJsonObj.get("tax_type").getAsString());
                        lblPurInvProductTaxPer.setText(productJsonObj.get("tax_per").getAsString());
                        lblPurInvProductMarginPer.setText(productJsonObj.get("margin_per").getAsString());
                        lblPurInvProductCost.setText(productJsonObj.get("cost").getAsString());
                        lblPurInvProductShelfId.setText(productJsonObj.get("shelf_id").getAsString());
                        lblPurInvProductMinStock.setText(productJsonObj.get("min_stocks").getAsString());
                        lblPurInvProductMaxStock.setText(productJsonObj.get("max_stocks").getAsString());
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

    public void onClickAdditionalCharges() {
        Stage stage = (Stage) purScrollPane.getScene().getWindow();
        AdditionalCharges.additionalCharges(stage, addChargesDTOList, total_taxable_amt, (input1, input2) -> {
            addChargesDTOList.clear();
            addChargesDTOList.addAll(input2);
            tfAddCharges.setText(input1[1]);
            PurchaseInvoiceCalculationSOtoSI.additionalChargesCalculation(input1[1], tvPurchaseInvoiceTable, callback);

        });
    }

    private void openAdjustBillWindow() {
//        Stage stage = (Stage) .getScene().getWindow()

        System.out.println("Adjust MEnt window");

        SingleInputDialogs.openAdjustBillPopUp(Communicator.stage, "Settlement of Bills", input -> {


            String debitNoteNo = (String) input[0];
            String debitNoteDate = (String) input[1];
            String id = (String) input[2];
            String totalAmt = (String) input[3];

        });
    }

//    private void invoiceValidations() {
//        //openAdjustBillWindow();
//        Stage stage = (Stage) purScrollPane.getScene().getWindow();
//        if (tfLedgerName.getText().isEmpty()) {
//            AlertUtility.AlertDialogForErrorWithStage(stage, "Validation Error", "Enter mandatory Ledger Name Field ", output -> {
//                if (output) {
//                    tfLedgerName.requestFocus();
//                }
//            });
//        } else if (tfInvoiceNo.getText().isEmpty()) {
//            AlertUtility.AlertDialogForErrorWithStage(stage, "Validation Error", "Enter mandatory " + "Invoice No Field", output -> {
//                if (output) {
//                    tfInvoiceNo.requestFocus();
//                }
//            });
//        } else if (tfInvoiceDate.getText().isEmpty()) {
//            AlertUtility.AlertDialogForErrorWithStage(stage, "Validation Error", "Enter mandatory " + "Invoice Date Field", output -> {
//                if (output) {
//                    tfInvoiceDate.requestFocus();
//                }
//            });
//        } else {
//            String msg = "Do you want to create ";
////            if (purchase_id > 0) msg = "Do you want to update ";
//            AlertUtility.AlertConfirmation(AlertUtility.alertTypeConfirmation, msg , input -> {
//                if (input == 1) {
//                    createPurchaseInvoice();
//                }
//            });
//
//        }
//
//    }

    private void validatePurchaseInvoiceNo() {
        try {
            logger.debug("Validating Pur Invoice Number");
            Map<String, String> map = new HashMap<>();
            map.put("supplier_id", ledger_id);
            map.put("bill_no", tfInvoiceNo.getText());

            String formData = Globals.mapToStringforFormData(map);
            HttpResponse<String> response = APIClient.postFormDataRequest(formData, EndPoints.VALIDATE_PUR_INV_NUMBER);
            String resBody = response.body();
            JsonObject obj = new Gson().fromJson(resBody, JsonObject.class);
            if (obj.get("responseStatus").getAsInt() == 409) {
                AlertUtility.AlertWarningTimeout(AlertUtility.alertTypeWarning, obj.get("message").getAsString(), callback -> {
                    logger.debug("Validate Pur Invoice Success");
                });
                tfInvoiceNo.setText(tfInvoiceNo.getText());
                tfInvoiceNo.requestFocus();
            }
        } catch (Exception e) {
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            String exceptionAsString = sw.toString();
            logger.error("Exception in Purchase invoice validate():" + exceptionAsString);
        }

    }

    private void validatePurchaseInvoiceUpdate() {
        try {
            logger.debug("Validating Pur Invoice Number in Edit");
            Map<String, String> map = new HashMap<>();
            map.put("supplier_id", ledger_id);
            map.put("bill_no", tfInvoiceNo.getText());
            map.put("invoice_id", "");
//            map.put("invoice_id", "" + purchase_id);
            String formData = Globals.mapToStringforFormData(map);
            HttpResponse<String> response = APIClient.postFormDataRequest(formData, EndPoints.VALIDATE_PUR_INV_NUMBER_EDIT);
            String resBody = response.body();
            JsonObject obj = new Gson().fromJson(resBody, JsonObject.class);
            if (obj.get("responseStatus").getAsInt() == 409) {
                AlertUtility.AlertWarningTimeout(AlertUtility.alertTypeWarning, obj.get("message").getAsString(), callback -> {
                    logger.debug("Validate Pur Invoice Success");
                });
                tfInvoiceNo.setText(tfInvoiceNo.getText());
                tfInvoiceNo.requestFocus();
            }
        } catch (Exception e) {
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            String exceptionAsString = sw.toString();
            logger.error("Exception in Purchase invoice validate():" + exceptionAsString);
        }

    }

    private void validateInvoiceDate() {
        try {
            logger.debug("Initiate the validateInvoiceDate() method");
            Map<String, String> map = new HashMap<>();
            String invoiceDate = Communicator.text_to_date.fromString(tfInvoiceDate.getText()).toString();
            map.put("invoiceDate", invoiceDate);
            String formData = Globals.mapToStringforFormData(map);
            HttpResponse<String> response = APIClient.postFormDataRequest(formData, EndPoints.VALIDATE_PUR_INV_DATE);
            String resBody = response.body();
            JsonObject obj = new Gson().fromJson(resBody, JsonObject.class);
            if (obj.get("responseStatus").getAsInt() == 404) {
                AlertUtility.AlertWarningTimeout(AlertUtility.alertTypeWarning, obj.get("message").getAsString(), callback -> {
                    logger.debug("Invoice Date : " + invoiceDate + " not in the Financial Year in Pur Invoice");
                    logger.debug("END of validateInvoiceDate() method in Pur Invoice");
                });
                tfInvoiceDate.requestFocus();
            }
        } catch (Exception e) {
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            String exceptionAsString = sw.toString();
            logger.error("Exception in validateInvoiceDate:" + exceptionAsString);
        }

    }

    public void createPurchaseInvoice() {
        AlertUtility.CustomCallback callback = number -> {
            if (number == 1) {
                Object idValue = invoice_data_map.get("id");
                if (idValue != null) {
                    purchase_invoice_map.put("id", idValue.toString());
                    purchase_invoice_map.put("rowDelDetailsIds", deletedRows.toString());
                    purchase_invoice_map.put("sales_disc_ledger", edit_response_map.get("discountLedgerId").toString());
//            Integer totalQty = rowListDTOS.stream().map(v -> v.getQty()).sum();
                    Integer totalQty = 0;
                    for (SalesRowListDTO dto : rowListDTOS) {
                        int actQty = dto.getQty() > 0 ? (int) Math.ceil(dto.getQty()) : 0;
                        totalQty += actQty;
                    }
                    purchase_invoice_map.put("totalqty", totalQty.toString());
                }

                purchase_invoice_map.put("bill_dt", Communicator.text_to_date.fromString(tfInvoiceDate.getText()).toString());

                purchase_invoice_map.put("transaction_date", Communicator.text_to_date.fromString(tfTranxDate.getText()).toString());

                purchase_invoice_map.put("newReference", "false");

                purchase_invoice_map.put("bill_no", tfInvoiceNo.getText());

                purchase_invoice_map.put("sales_acc_id", purchaseAcID);

                purchase_invoice_map.put("sales_sr_no", tfPurchaseSerial.getText());

                purchase_invoice_map.put("debtors_id", ledger_id);

                purchase_invoice_map.put("isRoundOffCheck", String.valueOf(chbPurInvRoundOff.isSelected()));

                purchase_invoice_map.put("roundoff", lblPurInvRoundOff.getText());

                purchase_invoice_map.put("totalamt", totalamt);

                purchase_invoice_map.put("total_sales_discount_amt", total_purchase_discount_amt);

                purchase_invoice_map.put("gstNo", gst_no);

                purchase_invoice_map.put("sale_type", "sales");
                purchase_invoice_map.put("salesmanId", "");
                purchase_invoice_map.put("print_type", "");
                purchase_invoice_map.put("paymentMode", "");
                purchase_invoice_map.put("tcs_per", "0.00");
                purchase_invoice_map.put("taxFlag", String.valueOf(taxFlag));

                purchase_invoice_map.put("tcs_amt", "0.00");

                purchase_invoice_map.put("tcs_mode", "");

                purchase_invoice_map.put("sales_discount", purchase_discount.getText().isEmpty() || purchase_discount == null ? "0.0" : purchase_discount.getText());

                purchase_invoice_map.put("sales_discount_amt", purchase_discount_amt.getText().isEmpty() || purchase_discount == null ? "0.0" : purchase_discount_amt.getText());

                purchase_invoice_map.put("additionalChargesTotal", tfAddCharges.getText().isEmpty() ? "0.0" : tfAddCharges.getText());

                purchase_invoice_map.put("additionalCharges", addChargesDTOList.toString());
                purchase_invoice_map.put("reference","SLSORD");
                purchase_invoice_map.put("reference_so_id",input);

                purchase_invoice_map.put("total_invoice_dis_amt", "0.0");
//        purchase_invoice_map.put("taxable_amount", "0.0");
//        purchase_invoice_map.put("total_tax_amt", String.valueOf(tfTranxTaxTotal.getText()));
//        purchase_invoice_map.put("bill_amount", String.valueOf(tfTranxBillAmtTotal.getText()));
                List<SalesInvoiceTable> currentItems = new ArrayList<>(tvPurchaseInvoiceTable.getItems());

                if (!currentItems.isEmpty()) {
                    SalesInvoiceTable lastItem = currentItems.get(currentItems.size() - 1);

                    if (lastItem.getParticulars() != null && lastItem.getParticulars().isEmpty()) {
                        currentItems.remove(currentItems.size() - 1);
                    }
                }

                List<SalesInvoiceTable> list = new ArrayList<>(currentItems);


                for (SalesInvoiceTable purchaseInvoiceTable : list) {
                    if (!purchaseInvoiceTable.getTotal_taxable_amt().equals("0.0")) {
                        purchaseInvoiceTable.setTotal_amt(purchaseInvoiceTable.getTotal_taxable_amt());
                    } else {
                        purchaseInvoiceTable.setTotal_amt(purchaseInvoiceTable.getTaxable_amt());
                    }
                }

                Double total_row_gross_amt = 0.0;

                for (SalesInvoiceTable purchaseInvoiceTable : list) {
                    total_row_gross_amt = total_row_gross_amt + Double.parseDouble(purchaseInvoiceTable.getGross_amount1());
                }

                purchase_invoice_map.put("total_row_gross_amt", String.valueOf(total_row_gross_amt));
                purchase_invoice_map.put("row", list.toString());
                System.out.println("final Map== "+purchase_invoice_map);

                try {
                    String formdata = mapToStringforFormData(purchase_invoice_map);

                    Map<String, String> headers = new HashMap<>();
                    headers.put("branch", Globals.headerBranch);

                    String response = null;
                    APIClient apiClient = null;

                    apiClient = new APIClient(EndPoints.TRANX_SALES_INVOICE_CREATE, purchase_invoice_map, headers, null, RequestType.MULTI_PART);
                    apiClient.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
                        @Override
                        public void handle(WorkerStateEvent workerStateEvent) {
                            String msg = "";
                            logger.info("Purchase Invoice create---> on Succeeded ");
//                    }
                            String response = workerStateEvent.getSource().getValue().toString();
                            JsonObject resDTO = new Gson().fromJson(response, JsonObject.class);
                            if (resDTO.get("responseStatus").getAsInt() == 200) {
                                msg = resDTO.get("message").getAsString();
                                AlertUtility.AlertSuccessTimeout(AlertUtility.alertTypeSuccess, msg, input -> {
                                    GlobalController.getInstance().addTabStatic(SALES_INVOICE_LIST_SLUG, false);
                                });
                            }
                        }
                    });
                    apiClient.start();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                System.out.println("working!");
            }
        };
        AlertUtility.AlertConfirmation(AlertUtility.alertTypeConfirmation, LedgerMessageConsts.msgConfirmationOnSubmit + tfInvoiceNo.getText() + LedgerMessageConsts.msgInvoiceNumber, callback);
    }

    public void purc_disc_per(KeyEvent keyEvent) {
        CommonValidationsUtils.restrictToDecimalNumbers(purchase_discount);
        String discText = purchase_discount.getText();
        if (!TextUtils.isEmpty(discText)) {
            double totalTaxableAmt = 0.0;
            for (int i = 0; i < tvPurchaseInvoiceTable.getItems().size(); i++) {
                System.out.println("Gross amt in Prop--->" + tvPurchaseInvoiceTable.getItems().get(i).getGross_amount());
                PurchaseInvoiceCalculationSOtoSI.rowCalculationForPurcInvoice(i, tvPurchaseInvoiceTable, callback);//call row calculation function
                SalesInvoiceTable purchaseInvoiceTable = tvPurchaseInvoiceTable.getItems().get(i);
                totalTaxableAmt = totalTaxableAmt + Double.parseDouble(purchaseInvoiceTable.getTaxable_amt());

            }
            double disc_per = Double.parseDouble(discText);
            System.out.println("totalTaxableAmount >> : " + totalTaxableAmt);
            Double amount = (totalTaxableAmt * disc_per) / 100;
            purchase_discount_amt.setText(String.valueOf(amount));
            PurchaseInvoiceCalculationSOtoSI.discountPropotionalCalculation(disc_per + "", "0", tfAddCharges.getText(), tvPurchaseInvoiceTable, callback);
            if (!tfAddCharges.getText().isEmpty()) {
                System.out.println("Additioanal Charges Called >> ");
                PurchaseInvoiceCalculationSOtoSI.additionalChargesCalculation(tfAddCharges.getText(), tvPurchaseInvoiceTable, callback);
            }
        } else {
            purchase_discount_amt.setText("");
            PurchaseInvoiceCalculationSOtoSI.discountPropotionalCalculation("0", "0", tfAddCharges.getText(), tvPurchaseInvoiceTable, callback);
            if (!tfAddCharges.getText().isEmpty()) {
                System.out.println("Additioanal Charges Called >> ");
                PurchaseInvoiceCalculationSOtoSI.additionalChargesCalculation(tfAddCharges.getText(), tvPurchaseInvoiceTable, callback);
            }
        }

        //? Calculate Tax for each Row
        PurchaseInvoiceCalculationSOtoSI.calculateGst(tvPurchaseInvoiceTable, callback);

    }

    public void purc_disc_amt(KeyEvent keyEvent) {
        CommonValidationsUtils.restrictToDecimalNumbers(purchase_discount_amt);
        String discAmtText = purchase_discount_amt.getText();
        if (!TextUtils.isEmpty(discAmtText)) {
            double totalTaxableAmt = 0.0;
            for (int i = 0; i < tvPurchaseInvoiceTable.getItems().size(); i++) {
                System.out.println("Gross amt in Prop--->" + tvPurchaseInvoiceTable.getItems().get(i).getGross_amount());
                PurchaseInvoiceCalculationSOtoSI.rowCalculationForPurcInvoice(i, tvPurchaseInvoiceTable, callback);//call row calculation function
                SalesInvoiceTable purchaseInvoiceTable = tvPurchaseInvoiceTable.getItems().get(i);
                totalTaxableAmt = totalTaxableAmt + Double.parseDouble(purchaseInvoiceTable.getTaxable_amt());

            }
            double disc_amt = Double.parseDouble(discAmtText);
            double percentage = (disc_amt / totalTaxableAmt) * 100;
            purchase_discount.setText(String.format("%.2f", percentage));

            PurchaseInvoiceCalculationSOtoSI.discountPropotionalCalculation("0", disc_amt + "", tfAddCharges.getText(), tvPurchaseInvoiceTable, callback);
            if (!tfAddCharges.getText().isEmpty()) {
                System.out.println("Additioanal CHarges Called >> ");
                PurchaseInvoiceCalculationSOtoSI.additionalChargesCalculation(tfAddCharges.getText(), tvPurchaseInvoiceTable, callback);
            }
        } else {
            purchase_discount.setText("");
            PurchaseInvoiceCalculationSOtoSI.discountPropotionalCalculation("0", "0", tfAddCharges.getText(), tvPurchaseInvoiceTable, callback);
            if (!tfAddCharges.getText().isEmpty()) {
                System.out.println("Additioanal CHarges Called >> ");
                PurchaseInvoiceCalculationSOtoSI.additionalChargesCalculation(tfAddCharges.getText(), tvPurchaseInvoiceTable, callback);
            }
        }

        //? Calculate Tax for each Row
        PurchaseInvoiceCalculationSOtoSI.calculateGst(tvPurchaseInvoiceTable, callback);
    }

    public void onClickCancel(ActionEvent actionEvent) {
        AlertUtility.AlertConfirmation(AlertUtility.alertTypeConfirmation, LedgerMessageConsts.msgGoList, input -> {
            if (input == 1) {
                GlobalController.getInstance().addTabStatic(SALES_ORDER_LIST_SLUG, false);

            }
        });
    }

    private void tranxBatchDetailsFun(String bNo, String bId) {
        APIClient apiClient = null;
        tranxPurInvTabPane.getSelectionModel().select(tabPurInvBatch);
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
                    lblPurInvBatchNo.setText(batchJsonObj.get("batchNo").getAsString());
                    lblPurInvMfgDate.setText(DateConvertUtil.convertDispDateFormat(batchJsonObj.get("mfgDate").getAsString()));
                    lblPurInvExpDate.setText(DateConvertUtil.convertDispDateFormat(batchJsonObj.get("expDate").getAsString()));
                    lblPurInvBarcode.setText(DateConvertUtil.convertDispDateFormat(batchJsonObj.get("barcode").getAsString()));
                    lblPurInvPurRate.setText(batchJsonObj.get("pur_rate").getAsString());
                    lblPurInvCost.setText(batchJsonObj.get("cost").getAsString());
                    lblPurInvCostWithTax.setText(batchJsonObj.get("cost_with_tax").getAsString());
                    lblPurInvMrp.setText(batchJsonObj.get("batchMrp").getAsString());
                    lblPurInvFsrmh.setText(batchJsonObj.get("fsrmh").getAsString());
                    lblPurInvFssai.setText(batchJsonObj.get("fsrai").getAsString());
                    lbPurInvBatchCsrmh.setText(batchJsonObj.get("csrmh").getAsString());
                    lbPurInvBatchCsrai.setText(batchJsonObj.get("csrai").getAsString());
                    lblPurInvFreeqty.setText(batchJsonObj.get("free_qty").getAsString());
                    lblPurInvMarginPer.setText(batchJsonObj.get("min_margin").getAsString());
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

    /*public void handleCmbPaymentMode(ActionEvent actionEvent) {
        selectedPayMode = cmbPaymentMode.getValue();

    }*/
}

class TextFieldTableCellForSalesInvoice extends TableCell<SalesInvoiceTable, String> {
    private final TextField textField;
    private final String columnName;

    private final TableCellCallback<Object[]> callback;
    private TableCellCallback<Object[]> productID_callback;

    private TextField button;


    public TextFieldTableCellForSalesInvoice(String columnName, TableCellCallback<Object[]> callback) {
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
        freeQtyColumn();
        rateColumn();
        DisPerColumn();
        DisAmtColumn();
        batchColumn();

        netAmountColumn();

    }

    public TextFieldTableCellForSalesInvoice(String columnName, TableCellCallback<Object[]> callback, TableCellCallback<Object[]> productID_callback, TextField button) {
        this.columnName = columnName;
        this.textField = new TextField();

        this.callback = callback;
        this.productID_callback = productID_callback;

        this.button = button;


        this.textField.setOnAction(event -> commitEdit(textField.getText()));

        // Commit when focus is lost
        this.textField.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal) {
                commitEdit(textField.getText());
            }
        });
        this.textField.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal) {
                this.textField.requestFocus();
                getTableView().getSelectionModel().select(getIndex());
                getTableView().getFocusModel().focus(getIndex());
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
        freeQtyColumn();
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
                }
                if (event.isShiftDown() && event.getCode() == KeyCode.TAB) {
                    TableColumn<SalesInvoiceTable, ?> colName = getTableView().getColumns().get(1);
                    getTableView().edit(getIndex(), colName);
                    event.consume();
                }else if (event.getCode() == KeyCode.ENTER || !event.isShiftDown() && event.getCode() == KeyCode.TAB) {
                    if (!textField.getText().isEmpty()) {
                        TableColumn<SalesInvoiceTable, ?> colName = getTableView().getColumns().get(7);
                        getTableView().edit(getIndex() , colName);
                    } else {
                        textField.requestFocus();
                    }
                    event.consume();
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
                }
                if (event.isShiftDown() && event.getCode() == KeyCode.TAB) {
                    if (getIndex() == 1) {
                        TableColumn<SalesInvoiceTable, ?> colName = getTableView().getColumns().get(13);
                        getTableView().edit(getIndex() - 1, colName);
                    } else {
                        TableColumn<SalesInvoiceTable, ?> colName = getTableView().getColumns().get(13);
                        getTableView().edit(getIndex() - 1, colName);
                    }
                }

                if (event.getCode() == KeyCode.ENTER || !event.isShiftDown() && event.getCode() == KeyCode.TAB) {
                    if (textField.getText().isEmpty() && getIndex() == getTableView().getItems().size() - 1 && getIndex() != 0) {
                        Platform.runLater(() -> {
                            button.requestFocus();
                        });
                    } else if (!textField.getText().isEmpty()) {
                        TableColumn<SalesInvoiceTable, ?> colName = getTableView().getColumns().get(6);
                        getTableView().edit(getIndex(), colName);
                    } else {
                        textField.requestFocus();
                    }
                    event.consume();
                }
            });

            textField.setOnMouseClicked(event -> {
                event.consume();
//                openProduct();
            });
        }
    }
    private void quantityColumn() {
        if ("tcQuantity".equals(columnName)) {
            textField.setEditable(true);
            textField.setOnKeyPressed(event -> {
                CommonValidationsUtils.restrictToNumbers(textField);
                if (event.isShiftDown() && event.getCode() == KeyCode.TAB) {
                    TableColumn<SalesInvoiceTable, ?> colName = getTableView().getColumns().get(7);
                    getTableView().edit(getIndex(), colName);
                    event.consume();

                } else if (event.getCode() == KeyCode.ENTER || event.getCode() == KeyCode.TAB) {
                    Integer index = getIndex();
                    if (!textField.getText().isEmpty()) {
                        int cnt = Globals.getUserControlsWithSlug("is_free_qty") == true ? 9 : 10;
                        boolean isNegetiveAllowed = getTableView().getItems().get(getIndex()).getSelectedProduct().getNegetive();
                        double actStk = getTableView().getItems().get(getIndex()).getUnitWiseactStock();
                        double actqty = Double.valueOf(textField.getText());
                        if (actqty > actStk && isNegetiveAllowed == false) {
                            AlertUtility.AlertError(AlertUtility.alertTypeError, "Out of Stock! Available Stock : " + actStk, input -> {
                            });
                            getTableRow().getItem().setQuantity("");
                            TableColumn<SalesInvoiceTable, ?> colName = getTableView().getColumns().get(8);
                            getTableView().edit(index, colName);
                        } else {
                            TableColumn<SalesInvoiceTable, ?> colName = getTableView().getColumns().get(cnt);
                            getTableView().edit(index, colName);
                        }
                    } else {
                        textField.requestFocus();
                    }

                    PurchaseInvoiceCalculationSOtoSI.rowCalculationForPurcInvoice(getIndex(), getTableView(), callback);
                    PurchaseInvoiceCalculationSOtoSI.calculateGst(getTableView(), callback);
                } else if (event.getCode() == KeyCode.DOWN) {
                    getTableView().getItems().addAll(new SalesInvoiceTable("", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", ""));
                    TableColumn<SalesInvoiceTable, ?> colName = getTableView().getColumns().get(1);
                    getTableView().edit(getIndex() + 1, colName);
                }
            });

        }
    }

    private void freeQtyColumn() {
        if ("tcFreeQuantity".equals(columnName)) {
            textField.setEditable(true);
            textField.setOnKeyPressed(event -> {
                CommonValidationsUtils.restrictToNumbers(textField);
                if (event.isShiftDown() && event.getCode() == KeyCode.TAB) {
                    TableColumn<SalesInvoiceTable, ?> colName = getTableView().getColumns().get(8);
                    System.out.println("Col:" + colName.getText());
                    getTableView().edit(getIndex(), colName);

                } else if (event.getCode() == KeyCode.ENTER || event.getCode() == KeyCode.TAB) {
                    int index = getIndex();
                    boolean isNegetiveAllowed = getTableView().getItems().get(getIndex()).getSelectedProduct().getNegetive();
                    double actStk = getTableView().getItems().get(getIndex()).getUnitWiseactStock();
                    double actqty = Double.valueOf(getTableView().getItems().get(getIndex()).getQuantity()) + Double.valueOf(textField.getText());
                    if (actqty > actStk && isNegetiveAllowed == false) {
                        AlertUtility.AlertError(AlertUtility.alertTypeError, "Out of Stock! Available Stock : " + actStk, input -> {
                        });
                        getTableRow().getItem().setFree("");
                        TableColumn<SalesInvoiceTable, ?> colName = getTableView().getColumns().get(9);
                        getTableView().edit(index, colName);
                    } else {
                        TableColumn<SalesInvoiceTable, ?> colName = getTableView().getColumns().get(10);
                        getTableView().edit(getIndex(), colName);
                    }
                    PurchaseInvoiceCalculationSOtoSI.rowCalculationForPurcInvoice(getIndex(), getTableView(), callback);
                    PurchaseInvoiceCalculationSOtoSI.calculateGst(getTableView(), callback);
                }
            });
        }
    }
    private void rateColumn() {
        if ("tcRate".equals(columnName)) {
            textField.setEditable(true);
            textField.setOnKeyPressed(event -> {
                CommonValidationsUtils.restrictToDecimalNumbers(textField);
                if (event.isShiftDown() && event.getCode() == KeyCode.TAB) {
//                    System.out.println("is_free_qty== "+Globals.getUserControlsWithSlug("is_free_qty"));
                    int cnt = Globals.getUserControlsWithSlug("is_free_qty") ==true ? 9 : 8;
                    TableColumn<SalesInvoiceTable, ?> colName = getTableView().getColumns().get(cnt);
                    getTableView().edit(getIndex() , colName);
                    event.consume();
                } else if (event.getCode() == KeyCode.ENTER || event.getCode() == KeyCode.TAB) {
                    int index = getIndex();

                    if (!textField.getText().isEmpty()) {
                        TableColumn<SalesInvoiceTable, ?> colName = getTableView().getColumns().get(12);
                        getTableView().edit(index, colName);
                    } else {
                        textField.requestFocus();
                    }
                    PurchaseInvoiceCalculationSOtoSI.rowCalculationForPurcInvoice(getIndex(), getTableView(), callback);
                    PurchaseInvoiceCalculationSOtoSI.calculateGst(getTableView(), callback);
                } else if (event.getCode() == KeyCode.DOWN) {
                    getTableView().getItems().addAll(new SalesInvoiceTable("", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", ""));
                    TableColumn<SalesInvoiceTable, ?> colName = getTableView().getColumns().get(1);
                    getTableView().edit(getIndex() + 1, colName);
                }
            });

        }
    }

    private void DisPerColumn() {
        if ("tcDisPer".equals(columnName)) {
            textField.setEditable(true);

            textField.setOnKeyPressed(event -> {
                CommonValidationsUtils.restrictToDecimalNumbers(textField);
                if (event.isShiftDown() && event.getCode() == KeyCode.TAB) {
                    TableColumn<SalesInvoiceTable, ?> colName = getTableView().getColumns().get(10);
                    getTableView().edit(getIndex(), colName);
                } else if (event.getCode() == KeyCode.ENTER || event.getCode() == KeyCode.TAB) {
                    int index = getIndex();
                    TableColumn<SalesInvoiceTable, ?> colName = getTableView().getColumns().get(13);
                    getTableView().edit(index, colName);
                    PurchaseInvoiceCalculationSOtoSI.rowCalculationForPurcInvoice(getIndex(), getTableView(), callback);
                    PurchaseInvoiceCalculationSOtoSI.calculateGst(getTableView(), callback);
                }
            });
        }
    }

    private void DisAmtColumn() {
        if ("tcDisAmt".equals(columnName)) {
            textField.setEditable(true);

            textField.setOnKeyPressed(event -> {
                CommonValidationsUtils.restrictToDecimalNumbers(textField);
                if (event.isShiftDown() && event.getCode() == KeyCode.TAB) {
                    TableColumn<SalesInvoiceTable, ?> colName = getTableView().getColumns().get(12);
                    getTableView().edit(getIndex(), colName);
                    event.consume();
                } else if (event.getCode() == KeyCode.ENTER || event.getCode() == KeyCode.TAB) {
                    int index = getIndex();
                    if ((getTableView().getItems().size() - 1) == index) {
                        getTableView().getItems().addAll(new SalesInvoiceTable("", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", ""));
                    }
                    TableColumn<SalesInvoiceTable, ?> colName = getTableView().getColumns().get(1);
                    getTableView().edit(getIndex() + 1, colName);
                    PurchaseInvoiceCalculationSOtoSI.rowCalculationForPurcInvoice(getIndex(), getTableView(), callback);
                    PurchaseInvoiceCalculationSOtoSI.calculateGst(getTableView(), callback);

                }
            });
        }
    }

    private void netAmountColumn() {

        if ("tcNetAmount".equals(columnName)) {
            textField.setEditable(false);
            textField.setOnKeyPressed(event -> {
                if (event.getCode() == KeyCode.ENTER) {
                    int current_index = getTableRow().getIndex();

                    int serial_no = Integer.parseInt(getTableRow().getItem().getSr_no());
                    getTableView().getItems().addAll(new SalesInvoiceTable("", String.valueOf(current_index + 1), String.valueOf(serial_no + 1), "", "", "", "", "", "", "", "", "", "", "", "", "", "", ""));
                }
            });
        }
    }

    private void openProduct() {
        SalesInvoiceTable selectedRow = getTableView().getItems().get(getIndex());

        TranxCommonPopUps.openProductPopUp(Communicator.stage, Integer.valueOf(selectedRow.getProduct_id()), "Product", input -> {
//            Integer selectedIndex = tblTranxSalesInvoiceCreateCMPTRow.getSelectionModel().getSelectedIndex();
//            System.out.println("input);
            if (input != null) {
                getTableRow().getItem().setParticulars(input.getProductName());
                getTableRow().getItem().setProduct_id(input.getProductId().toString());
                getTableRow().getItem().setPackages(input.getPackageName());
                getTableRow().getItem().setIs_batch(input.getBatch().toString());
                getTableRow().getItem().setTax(input.getIgst().toString());
//                getTableRow().getItem().setUnit(input.get());
                System.out.println("rate in openPro== "+input.getSalesRate());
                getTableRow().getItem().setSelectedProduct(input);
//                getTableRow().getItem().setRate(input.getSalesRate().toString());

                String productId = input.getProductId().toString();
                if (productID_callback != null) {
                    Object[] object = new Object[1];
                    object[0] = productId;
                    productID_callback.call(object);
                }
//                getTableRow().getItem().
                List<LevelAForPurInvoice> levelAForPurInvoiceList = ProductUnitsPacking.getAllProductUnitsPackingFlavour(input.getProductId().toString());
                System.out.println("levelAForPurInvoiceList" + levelAForPurInvoiceList);
                //                int index = getTableRow().getIndex();
                ObservableList<LevelAForPurInvoice> observableLevelAList = FXCollections.observableArrayList(levelAForPurInvoiceList);
                if (getIndex() >= 0 && getIndex() < PurInvoiceCommunicator.levelAForPurInvoiceObservableList.size()) {
                    PurInvoiceCommunicator.levelAForPurInvoiceObservableList.set(getIndex(), observableLevelAList);
                    getTableRow().getItem().setLevelA(null);
                    getTableRow().getItem().setLevelA(levelAForPurInvoiceList.get(0).getLabel());

                } else {
                    PurInvoiceCommunicator.levelAForPurInvoiceObservableList.add(observableLevelAList);
                    getTableRow().getItem().setLevelA(null);
                    getTableRow().getItem().setLevelA(levelAForPurInvoiceList.get(0).getLabel());
                }
                TableColumn<SalesInvoiceTable, ?> colName = getTableView().getColumns().get(1);
                getTableView().edit(getIndex(), colName);
            }
        },isRedirect->{
            System.out.println("isRedirect"+isRedirect);
        });
    }

    public void openBatchWindow() {
        SalesInvoiceTable selectedRow = getTableView().getItems().get(getIndex());
        if (Boolean.valueOf(selectedRow.getIs_batch()) == true) {
            TranxCommonPopUps.openBatchPopUpSales(Communicator.stage, selectedRow, Communicator.tranxDate, "Batch", input -> {
                if (input != null) {
                    selectedRow.setSelectedBatch(input);
                    selectedRow.setB_details_id("" + input.getId());
                    selectedRow.setB_no(input.getBatchNo());
                    selectedRow.setBatch_or_serial(input.getBatchNo());
                    if (productID_callback != null) {
                        Object[] object = new Object[2];
                        object[0] = "" + input.getBatchNo();
                        object[1] = "" + input.getId();
                        productID_callback.call(object);
                    }
//                                    selectedRow.setRate(input.minRateA);
                }
//                getTableView().getItems().set(getIndex(), selectedRow);
//                getBatchDetailsByBatchNoAndId(input.getBatchNo(), String.valueOf(input.getId()));
            },isNewBatch->{
                System.out.println("isNewBatch"+isNewBatch);
            });
        } else {
            selectedRow.setBatch_or_serial("#");
            selectedRow.setB_no("#");
//            getTableView().getItems().set(getIndex(), selectedRow);
        }
        PurchaseInvoiceCalculationSOtoSI.rowCalculationForPurcInvoice(getIndex(), getTableView(), callback);
        PurchaseInvoiceCalculationSOtoSI.calculateGst(getTableView(), callback);
        TableColumn<SalesInvoiceTable, ?> colName = getTableView().getColumns().get(7);

        getTableView().edit(getIndex(), colName);

    }


    public void textfieldStyle() {
        if (columnName.equals("tcParticulars")) {
            textField.setPromptText("Particulars");
            textField.setEditable(false);
        } else {
            textField.setPromptText("0");
        }

        /*textField.setPrefHeight(38);
        textField.setMaxHeight(38);
        textField.setMinHeight(38);*/

        if (columnName.equals("tcParticulars")) {
            textField.setStyle("-fx-background-color: #f6f6f9; -fx-border-radius: 0px; -fx-border-width: 1; -fx-border-color: #f6f6f9;");
            textField.focusedProperty().addListener((obs, wasFocused, isNowFocused) -> {
                if (isNowFocused) {
                    textField.setStyle("-fx-background-color: #fff9c4; -fx-border-radius: 0px; -fx-border-width: 2; -fx-border-color: #00a0f5;");
                } else {
                    textField.setStyle("-fx-background-color: #f6f6f9; -fx-border-radius: 0px; -fx-border-width: 2; -fx-border-color: #f6f6f9;");
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
        }

        if (columnName.equals("tcNetAmount")) {
            this.textField.setOnKeyPressed(event -> {
                if (event.getCode() == KeyCode.ENTER) {
                    commitEdit(textField.getText());
                    getTableView().getItems().addAll(new SalesInvoiceTable("", "", "1", "", "", "", "", "", "", "", "", "", "", "", "", "", "", ""));
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
        setText(getItem());
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
            textField.setText(item);
            setGraphic(vbox);
        }
    }

    @Override
    public void commitEdit(String newValue) {
        super.commitEdit(newValue);
        Object item = getTableRow().getItem();
        if (item != null && columnName.equals("tcParticulars")) {
            ((SalesInvoiceTable) item).setParticulars(newValue.isEmpty() ? "" : newValue);
        } else if (columnName.equals("tcQuantity")) {
            (getTableRow().getItem()).setQuantity(newValue.isEmpty() ? "0" : newValue);
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
        //! Row Level Calculation
        PurchaseInvoiceCalculationSOtoSI.rowCalculationForPurcInvoice(getIndex(), getTableView(), callback);
        PurchaseInvoiceCalculationSOtoSI.calculateGst(getTableView(), callback);
        /* else if (columnName.equals("tcBatch")) {
            (getTableRow().getItem()).setBatch_or_serial(newValue.isEmpty() ? "" : newValue);
        }*/
    }
}

class SalesComboBoxTableCellForLevelA extends TableCell<SalesInvoiceTable, String> {

    String columnName;
    private final ComboBox<String> comboBox;
    ObservableList<LevelAForPurInvoice> comboList = null;

    public SalesComboBoxTableCellForLevelA(String columnName) {

        this.columnName = columnName;
        this.comboBox = new ComboBox<>();

        this.comboBox.setPromptText("Select");

//        comboBox.setPrefHeight(38);
//        comboBox.setMaxHeight(38);
//        comboBox.setMinHeight(38);
//        comboBox.setPrefWidth(139);
//        comboBox.setMaxWidth(139);
//        comboBox.setMinWidth(84);

        this.comboBox.setStyle("-fx-background-color: #f6f6f9; -fx-border-radius: 0px; -fx-border-width: 1; -fx-border-color: #f6f6f9;");

        this.comboBox.focusedProperty().addListener((obs, wasFocused, isNowFocused) -> {
            if (isNowFocused) {
                this.comboBox.show();
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
        // AutoCompleteBox autoCompleteBox1 = new AutoCompleteBox<>(this.comboBox, -1);

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
        setText(getItem());
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
        TableRow<SalesInvoiceTable> row = getTableRow();
        if (row != null) {
            SalesInvoiceTable item = row.getItem();
            if (item != null) {
                item.setLevelA(newValue);
            }
        }
    }

}

class SalesComboBoxTableCellForLevelB extends TableCell<SalesInvoiceTable, String> {

    String columnName;
    private final ComboBox<String> comboBox;
    ObservableList<LevelBForPurInvoice> comboList = null;

    public SalesComboBoxTableCellForLevelB(String columnName) {
        this.columnName = columnName;
        this.comboBox = new ComboBox<>();

        this.comboBox.setPromptText("Select");

        /*comboBox.setPrefHeight(38);
        comboBox.setMaxHeight(38);
        comboBox.setMinHeight(38);
        comboBox.setPrefWidth(140);
        comboBox.setMaxWidth(140);
        comboBox.setMinWidth(85);*/


        this.comboBox.setStyle("-fx-background-color: #f6f6f9; -fx-border-radius: 0px; -fx-border-width: 1; -fx-border-color: #f6f6f9;");

        this.comboBox.focusedProperty().addListener((obs, wasFocused, isNowFocused) -> {
            if (isNowFocused) {
                this.comboBox.show();
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
        this.comboBox.setFocusTraversable(false);
        //  AutoCompleteBox autoCompleteBox1 = new AutoCompleteBox<>(this.comboBox, -1);

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
        setText(getItem());
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

class SalesComboBoxTableCellForLevelC extends TableCell<SalesInvoiceTable, String> {

    String columnName;
    private final ComboBox<String> comboBox;
    ObservableList<LevelAForPurInvoice> comboList = null;

    public SalesComboBoxTableCellForLevelC(String columnName) {


        this.columnName = columnName;
        this.comboBox = new ComboBox<>();

        this.comboBox.setPromptText("Select");

//        comboBox.setPrefHeight(38);
//        comboBox.setMaxHeight(38);
//        comboBox.setMinHeight(38);
//
//        comboBox.setPrefWidth(139);
//        comboBox.setMaxWidth(139);
//        comboBox.setMinWidth(84);

        this.comboBox.setStyle("-fx-background-color: #f6f6f9; -fx-border-radius: 0px; -fx-border-width: 1; -fx-border-color: #f6f6f9;");

        this.comboBox.focusedProperty().addListener((obs, wasFocused, isNowFocused) -> {
            if (isNowFocused) {
                this.comboBox.show();
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
        //AutoCompleteBox autoCompleteBox1 = new AutoCompleteBox<>(this.comboBox, -1);

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
        setText(getItem());
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

class SalesComboBoxTableCellForUnit extends TableCell<SalesInvoiceTable, String> {

    String columnName;
    private final ComboBox<String> comboBox;
    TableCellCallback<Integer> unit_callback;//new

    public SalesComboBoxTableCellForUnit(String columnName) {


        this.columnName = columnName;
        this.comboBox = new ComboBox<>();

        this.comboBox.setPromptText("Select");


        /*comboBox.setPrefHeight(38);
        comboBox.setMaxHeight(38);
        comboBox.setMinHeight(38);
        comboBox.setPrefWidth(180);
        comboBox.setMaxWidth(180);
        comboBox.setMinWidth(90);*/

        this.comboBox.setOnShowing(event -> {
            Node node = this.comboBox.lookup(".list-view");
            if (node instanceof ListView<?>) {
                ((ListView<?>) node).setMaxHeight(120);
                ((ListView<?>) node).setMinHeight(120);
                ((ListView<?>) node).setPrefHeight(120);
            }
        });

        this.comboBox.setStyle("-fx-background-color: #f6f6f9; -fx-border-radius: 0px; -fx-border-width: 1; -fx-border-color: #f6f6f9;");

        this.comboBox.focusedProperty().addListener((obs, wasFocused, isNowFocused) -> {
            if (isNowFocused) {
                this.comboBox.setStyle("-fx-background-color: #fff9c4; -fx-border-radius: 0px; -fx-border-width: 1.5; -fx-border-color: #00a0f5;");
                comboBox.show();
            } else {
                this.comboBox.setStyle("-fx-background-color: #f6f6f9; -fx-border-radius: 0px; -fx-border-width: 1.5; -fx-border-color: #f6f6f9;");
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


//        getTableView().getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
//            @Override
//            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
//                int selectedIndex = newValue.intValue();
//                if (selectedIndex >= 0) {
//                    comboBox.setStyle("-fx-background-color: #e6f5ff; -fx-border-radius: 0px; -fx-border-width: 1; -fx-border-color: #f6f6f9;");
//                } else {
//                    comboBox.setStyle("-fx-background-color: #f6f6f9; -fx-border-radius: 0px; -fx-border-width: 1; -fx-border-color: #f6f6f9;");
//                }
//            }
//        });

        comBoAction();


    }

    public SalesComboBoxTableCellForUnit(String columnName, TableCellCallback<Integer> unit_callback) {


        this.columnName = columnName;
        this.comboBox = new ComboBox<>();
        this.unit_callback = unit_callback;//new
        this.comboBox.setPromptText("Select");

        double height = Screen.getPrimary().getBounds().getHeight();

        if (height == 1080) {
            comboBox.setPrefHeight(38);
            comboBox.setMaxHeight(38);
            comboBox.setMinHeight(38);
            Platform.runLater(() -> {
                comboBox.setPrefWidth(getTableView().getColumns().get(7).getWidth() - 1);
                comboBox.setMaxWidth(getTableView().getColumns().get(7).getWidth() - 1);
                comboBox.setMinWidth(getTableView().getColumns().get(7).getWidth() - 1);
            });
        } else if (height == 1050) {
            comboBox.setPrefHeight(38);
            comboBox.setMaxHeight(38);
            comboBox.setMinHeight(38);
            Platform.runLater(() -> {
                comboBox.setPrefWidth(getTableView().getColumns().get(7).getWidth() - 1);
                comboBox.setMaxWidth(getTableView().getColumns().get(7).getWidth() - 1);
                comboBox.setMinWidth(getTableView().getColumns().get(7).getWidth() - 1);
            });
        } else if (height == 1024) {
            comboBox.setPrefHeight(32);
            comboBox.setMaxHeight(32);
            comboBox.setMinHeight(32);
            Platform.runLater(() -> {
                comboBox.setPrefWidth(getTableView().getColumns().get(7).getWidth() - 1);
                comboBox.setMaxWidth(getTableView().getColumns().get(7).getWidth() - 1);
                comboBox.setMinWidth(getTableView().getColumns().get(7).getWidth() - 1);
            });

        } else if (height == 960) {
            comboBox.setPrefHeight(32);
            comboBox.setMaxHeight(32);
            comboBox.setMinHeight(32);
            Platform.runLater(() -> {
                comboBox.setPrefWidth(getTableView().getColumns().get(7).getWidth() - 1);
                comboBox.setMaxWidth(getTableView().getColumns().get(7).getWidth() - 1);
                comboBox.setMinWidth(getTableView().getColumns().get(7).getWidth() - 1);
            });
        } else if (height == 900) {
            comboBox.setPrefHeight(32);
            comboBox.setMaxHeight(32);
            comboBox.setMinHeight(32);
            Platform.runLater(() -> {
                comboBox.setPrefWidth(getTableView().getColumns().get(7).getWidth() - 1);
                comboBox.setMaxWidth(getTableView().getColumns().get(7).getWidth() - 1);
                comboBox.setMinWidth(getTableView().getColumns().get(7).getWidth() - 1);
            });
        } else if (height == 800) {
            comboBox.setPrefHeight(27.5);
            comboBox.setMaxHeight(27.5);
            comboBox.setMinHeight(27.5);
            Platform.runLater(() -> {
                comboBox.setPrefWidth(getTableView().getColumns().get(7).getWidth() - 1);
                comboBox.setMaxWidth(getTableView().getColumns().get(7).getWidth() - 1);
                comboBox.setMinWidth(getTableView().getColumns().get(7).getWidth() - 1);
            });
        } else if (height == 768) {

        } else if (height == 720) {
        }

        this.comboBox.setOnShowing(event -> {
            Node node = this.comboBox.lookup(".list-view");
            if (node instanceof ListView<?>) {
                ((ListView<?>) node).setMaxHeight(120);
                ((ListView<?>) node).setMinHeight(120);
                ((ListView<?>) node).setPrefHeight(120);
            }
        });

        this.comboBox.setStyle("-fx-background-color: #f6f6f9; -fx-border-radius: 0px; -fx-border-width: 1; -fx-border-color: #f6f6f9;");

        this.comboBox.focusedProperty().addListener((obs, wasFocused, isNowFocused) -> {
            if (isNowFocused) {
                this.comboBox.setStyle("-fx-background-color: #fff9c4; -fx-border-radius: 0px; -fx-border-width: 1.5; -fx-border-color: #00a0f5;");
                comboBox.show();
            } else {
                this.comboBox.setStyle("-fx-background-color: #f6f6f9; -fx-border-radius: 0px; -fx-border-width: 1.5; -fx-border-color: #f6f6f9;");
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
                            getTableRow().getItem().setUnit_id(unitForPurInvoice.getValue());
                            getTableRow().getItem().setUnit_conv(String.valueOf(unitForPurInvoice.getUnitConversion()));
                            //!new
                            if (this.unit_callback != null) {
                                this.unit_callback.call(getIndex());
                            }
                        }
                    }
                }
            }

        });
        comboBox.setOnKeyPressed(event -> {
            if (event.isShiftDown() && event.getCode() == KeyCode.TAB) {
                TableColumn<SalesInvoiceTable, ?> colName = getTableView().getColumns().get(6);
                getTableView().edit(getIndex(), colName);
                event.consume();
            } else if (event.getCode() == KeyCode.ENTER || !event.isShiftDown() && event.getCode() == KeyCode.TAB) {
                TableColumn<SalesInvoiceTable, ?> colName = getTableView().getColumns().get(8);
                getTableView().edit(getIndex(), colName);
                event.consume();
            }
//            if (event.getCode() == KeyCode.DOWN) {
//                comboBox.show();
//                event.consume();
//            }
        });

    }

    @Override
    public void startEdit() {
        super.startEdit();
        setText(null);
        setGraphic(comboBox);
        comboBox.requestFocus();
    }

    @Override
    public void cancelEdit() {
        super.cancelEdit();
        setText(getItem());
        setGraphic(comboBox);
    }

    @Override
    protected void updateItem(String item, boolean empty) {
        super.updateItem(item, empty);
        if (empty) {
            if (getTableRow() == null || getTableRow().getItem() == null) {
                setGraphic(null);
            } else {
                setGraphic(comboBox);
            }
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

            setGraphic(comboBox);
        }
    }

    @Override
    public void commitEdit(String newValue) {
        super.commitEdit(newValue);
        (getTableRow().getItem()).setUnit(newValue);
    }

}

class ButtonTableCell extends TableCell<SalesInvoiceTable, String> {
    private final Button delete;

    public ButtonTableCell() {


        this.delete = createButtonWithImage();


        delete.setOnAction(actionEvent -> {
            SalesInvoiceTable table = getTableView().getItems().get(getIndex());
            getTableView().getItems().remove(table);
            getTableView().refresh();
        });

        delete.setOnKeyPressed(event -> {
            if (event.isShiftDown() && event.getCode() == KeyCode.TAB) {
                TableColumn<SalesInvoiceTable, ?> colName = getTableView().getColumns().get(7);
                getTableView().edit(getIndex(), colName);
            }

            if (event.getCode() == KeyCode.ENTER || !event.isShiftDown() && event.getCode() == KeyCode.TAB) {
                TableColumn<SalesInvoiceTable, ?> colName = getTableView().getColumns().get(1);
                getTableView().edit(getIndex() + 1, colName);
            }
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
        HBox vbox = new HBox(delete);
        vbox.setAlignment(Pos.CENTER);
        setGraphic(vbox);
        delete.requestFocus();

    }

    @Override
    public void cancelEdit() {
        super.cancelEdit();
        HBox vbox = new HBox(delete);
        vbox.setAlignment(Pos.CENTER);
        setGraphic(vbox);
        delete.requestFocus();
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


class SalesButtonTableCellDelete extends TableCell<SalesInvoiceTable, String> {
    private final Button delete;
    TableCellCallback<Object[]> callback;

    public SalesButtonTableCellDelete(TableCellCallback<Object[]> DelCallback) {


        this.delete = createButtonWithImage();

        callback = DelCallback;

        delete.setOnAction(actionEvent -> {
            SalesInvoiceTable table = getTableView().getItems().get(getIndex());
            getTableView().getItems().remove(table);
            Object object[] = new Object[1];
            if (callback != null) {
                object[0] = table.getDetails_id();
                callback.call(object);
            }
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

class SalesProductUnitsPacking {

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

class PurchaseInvoiceCalculationSOtoSI {

    public static void rowCalculationForPurcInvoice(int rowIndex, TableView<SalesInvoiceTable> tableView, TableCellCallback<Object[]> callback) {
        SalesInvoiceTable purchaseInvoiceTable = tableView.getItems().get(rowIndex);
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
        if (!purchaseInvoiceTable.getQuantity().isEmpty() && !purchaseInvoiceTable.getRate().isEmpty()){
            r_qty = purchaseInvoiceTable.getQuantity();
            r_rate = Double.parseDouble(purchaseInvoiceTable.getRate());
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
            SalesInvoiceTable selectedItem = tableView.getItems().get(rowIndex);
            selectedItem.setGross_amount(String.valueOf(base_amt));
            selectedItem.setNet_amount(String.valueOf(net_amt));
            selectedItem.setOrg_net_amt(String.valueOf(net_amt));
//        selectedItem.setOrg_taxable_amt(taxable_amt);
            selectedItem.setTaxable_amt(String.valueOf(taxable_amt));
            selectedItem.setTotal_taxable_amt(String.valueOf(total_taxable_amt));
            selectedItem.setIgst(String.valueOf(totalTax)); // for Tax Amount
            selectedItem.setSgst(String.valueOf(totalTax / 2)); // for Tax Amount
            selectedItem.setCgst(String.valueOf(totalTax / 2)); // for Tax  Amount
            selectedItem.setFinal_tax_amt(String.valueOf(totalTax));
            selectedItem.setFinal_dis_amt(String.valueOf(row_dis_amt));

            // Create API Payload Parameters Adding
            selectedItem.setBase_amt(String.valueOf(base_amt));
//        selectedItem.setGross_amt(String.valueOf(gross_amt));
            selectedItem.setDis_per(String.valueOf((r_dis_per)));
            selectedItem.setDis_per_cal(String.valueOf(total_dis_per));
            selectedItem.setDis_amt(String.valueOf((r_dis_amt)));
            selectedItem.setDis_amt_cal(String.valueOf(r_dis_amt));
            selectedItem.setRow_dis_amt(String.valueOf(row_dis_amt));
            selectedItem.setGross_amount1(String.valueOf(taxable_amt));
            selectedItem.setTotal_taxable_amt(String.valueOf(taxable_amt));

            // tax percentage store cmptrow
            selectedItem.setGst(String.valueOf(r_tax_per));
            selectedItem.setIgst_per(String.valueOf(r_tax_per));
            selectedItem.setSgst_per(String.valueOf(r_tax_per / 2));
            selectedItem.setCgst_per(String.valueOf(r_tax_per / 2));
            selectedItem.setTotal_igst(String.valueOf(totalTax));
            selectedItem.setTotal_sgst(String.valueOf(totalTax / 2));
            selectedItem.setTotal_cgst(String.valueOf(totalTax / 2));
            selectedItem.setFinal_amount(String.valueOf(net_amt));
            selectedItem.setDis_per2(String.valueOf(disPer2));
        }

    }

    public static void calculateGst(TableView<SalesInvoiceTable> tableView, TableCellCallback<Object[]> callback) {


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
        for (SalesInvoiceTable purchaseInvoiceTable : tableView.getItems()) {
            if (!purchaseInvoiceTable.getQuantity().isEmpty() && Double.parseDouble(purchaseInvoiceTable.getQuantity()) > 0 &&
                    !purchaseInvoiceTable.getRate().isEmpty() && Double.parseDouble(purchaseInvoiceTable.getRate()) > 0) {
                taxPercentage = Double.parseDouble(purchaseInvoiceTable.getTax());
                Double quantity = Double.parseDouble(purchaseInvoiceTable.getQuantity());
                if (purchaseInvoiceTable.getFree().isEmpty()) {
                    freeQuantity = 0.0;
                } else {
                    freeQuantity = Double.parseDouble(purchaseInvoiceTable.getFree());
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

                System.out.println("taxPercentage-- "+taxPercentage+"  cgst==>> "+cgst);
                cgstTotals.put(taxPercentage, cgstTotals.get(taxPercentage) + cgst); //0.0
                sgstTotals.put(taxPercentage, sgstTotals.get(taxPercentage) + sgst);
                igstTotals.put(taxPercentage, igstTotals.get(taxPercentage) + igst);    //igst issue

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


        }

        ObservableList<GstDTO> observableList = FXCollections.observableArrayList();
        for (Double taxPer : new Double[]{3.0, 5.0, 12.0, 18.0, 28.0}) {
            double totalCGST = cgstTotals.get(taxPer);
            double totalSGST = sgstTotals.get(taxPer);
            double totalIGST = igstTotals.get(taxPer);

            if (totalCGST > 0) {
//                GstDTO gstDto :
                System.out.println("Tax Percentage: " + taxPer + "%");
                System.out.println("Total CGST: " + totalCGST);
                System.out.println("Total SGST: " + totalSGST);
                System.out.println();

                observableList.add(new GstDTO(String.format("%.2f", taxPer), String.format("%.2f", totalCGST), String.format("%.2f", totalSGST), String.format("%.2f",totalIGST)));
            }
        }


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


    public static void discountPropotionalCalculation(String disproportionalPer, String disproportionalAmt, String additionalCharges, TableView<SalesInvoiceTable> tableView, TableCellCallback<Object[]> callback) {

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
//            rowCalculationForPurcInvoice(i, tableView, callback);//call row calculation function
            SalesInvoiceTable purchaseInvoiceTable = tableView.getItems().get(i);
            totalTaxableAmt = totalTaxableAmt + Double.parseDouble(purchaseInvoiceTable.getTaxable_amt());
        }
        System.out.println("totalTaxableAmt discountPropotionalCalculation :: " + totalTaxableAmt);

        Double rowDisc = 0.0;
        //get data of netamount, tax per,and row taxable amount from cmptrow
        for (SalesInvoiceTable purchaseInvoiceTable : tableView.getItems()) {
            if (!purchaseInvoiceTable.getQuantity().isEmpty() && Double.parseDouble(purchaseInvoiceTable.getQuantity()) > 0 &&
                    !purchaseInvoiceTable.getRate().isEmpty() && Double.parseDouble(purchaseInvoiceTable.getRate()) > 0){
                rowDisc = Double.parseDouble(purchaseInvoiceTable.getFinal_dis_amt());

                netAmt = Double.parseDouble(purchaseInvoiceTable.getNet_amount());

                r_tax_per = Double.parseDouble(purchaseInvoiceTable.getTax());
                rowTaxableAmt = Double.parseDouble(purchaseInvoiceTable.getTaxable_amt());

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

                //? Bill Level Disc Amount Calculation
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
                    if (r_tax_per >= 0) {
                        total_tax_amt = (r_tax_per / 100) * (rowTaxableAmt);
                        System.out.println("total_tax_amt" + total_tax_amt);
                        netAmt = rowTaxableAmt + total_tax_amt;
                        System.out.println("total netAmt  :" + netAmt + rowTaxableAmt);
//
                    }
                }

                //Set data to cmpTRow
                purchaseInvoiceTable.setNet_amount(String.format("%.2f", netAmt));
                purchaseInvoiceTable.setTotal_taxable_amt(String.valueOf(totalTaxableAmtAdditional));
                System.out.println("Total Tax Amt--->" + total_tax_amt);
                purchaseInvoiceTable.setIgst(String.valueOf(total_tax_amt));
                purchaseInvoiceTable.setCgst(String.valueOf(total_tax_amt / 2));
                purchaseInvoiceTable.setSgst(String.valueOf(total_tax_amt / 2));
//            purchaseInvoiceTable.setTotal_igst(String.valueOf(total_tax_amt));
//            purchaseInvoiceTable.setTotal_cgst(String.valueOf(total_tax_amt / 2));
//            purchaseInvoiceTable.setTotal_sgst(String.valueOf(total_tax_amt / 2));
                purchaseInvoiceTable.setFinal_tax_amt(String.valueOf(total_tax_amt));
                purchaseInvoiceTable.setFinal_dis_amt(String.valueOf(rowDisc));
                purchaseInvoiceTable.setInvoice_dis_amt(String.valueOf(rowDisPropAmt));
            }


        }
        //Check additional charge is Empty or not if not empty call to the additional charges function
        if (!additionalCharges.isEmpty()) {
            //additionalChargesCalculation();
        } /*else {
            System.out.println("Additional Charges is empty");
            additionalChargesCalculation();
        }*/
        System.out.println("from discountPropotionalCalculation");
//        calculateGst(tableView, callback);
    }


    public static void additionalChargesCalculation(String additionalCharges, TableView<SalesInvoiceTable> tableView, TableCellCallback<Object[]> callback) {


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
                SalesInvoiceTable purchaseInvoiceTable = tableView.getItems().get(i);
                totalTaxableAmt = totalTaxableAmt + Double.parseDouble(purchaseInvoiceTable.getTotal_taxable_amt());

            }
            for (SalesInvoiceTable purchaseInvoiceTable : tableView.getItems()) {

                System.out.println("row ->" + purchaseInvoiceTable.toString());
                netAmt = Double.parseDouble(purchaseInvoiceTable.getNet_amount());
                System.out.println("netAMout >> : " + netAmt);
                r_tax_per = Double.parseDouble(purchaseInvoiceTable.getTax());
                rowTaxableAmt = Double.parseDouble(purchaseInvoiceTable.getTotal_taxable_amt());
                System.out.println("rowTaxableAmt >> : " + rowTaxableAmt);
                System.out.println("GRoss AMT " + purchaseInvoiceTable.getGross_amount());


                totalDisPropAmt = additionalChargeAmt;
                rowDisPropAmt = (rowTaxableAmt * totalDisPropAmt) / totalTaxableAmt;
                rowTaxableAmt = rowTaxableAmt + rowDisPropAmt;

                if (r_tax_per >= 0) {
                    total_tax_amt = (r_tax_per / 100) * (rowTaxableAmt);
                    netAmt = rowTaxableAmt + total_tax_amt;
                }
//                purchaseInvoiceTable.setGross_amount();
                System.out.println("cgst in additional== "+total_tax_amt);
                purchaseInvoiceTable.setNet_amount(String.format("%.3f", netAmt));
                purchaseInvoiceTable.setCgst(String.valueOf(total_tax_amt / 2));
                purchaseInvoiceTable.setSgst(String.valueOf(total_tax_amt / 2));
                purchaseInvoiceTable.setFinal_tax_amt(String.valueOf(total_tax_amt));


            }

        }
        //call calculate gst function
        calculateGst(tableView, callback);

    }
}









