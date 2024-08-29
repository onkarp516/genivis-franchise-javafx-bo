package com.opethic.genivis.controller.tranx_sales.invoice;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.opethic.genivis.GenivisApplication;
import com.opethic.genivis.controller.dialogs.AdditionalCharges;
import com.opethic.genivis.controller.dialogs.SingleInputDialogs;
import com.opethic.genivis.controller.master.ledger.common.LedgerMessageConsts;
import com.opethic.genivis.controller.tranx_sales.SalesQuotationListController;
import com.opethic.genivis.controller.tranx_sales.common.LineNumbersCellFactory;
import com.opethic.genivis.controller.tranx_sales.common.TranxCommonPopUps;
import com.opethic.genivis.dto.CommonDTO;
import com.opethic.genivis.dto.GstDTO;
import com.opethic.genivis.dto.GstDetailsDTO;
import com.opethic.genivis.dto.SalesQuotationProductDTO;
import com.opethic.genivis.dto.add_charges.CustomAddChargesDTO;
import com.opethic.genivis.dto.cmp_t_row.BatchWindowTableDTO;
import com.opethic.genivis.dto.pur_invoice.*;
import com.opethic.genivis.dto.pur_invoice.reqres.*;
import com.opethic.genivis.dto.reqres.product.Communicator;
import com.opethic.genivis.dto.reqres.product.TableCellCallback;
import com.opethic.genivis.dto.reqres.pur_tranx.TranxPurRowDTO;
import com.opethic.genivis.dto.reqres.sales_tranx.SalesInvoiceTable;
import com.opethic.genivis.dto.reqres.sales_tranx.SalesOrderTable;
import com.opethic.genivis.dto.reqres.sales_tranx.SalesQuotToChallanResponseDTO;
import com.opethic.genivis.dto.reqres.sales_tranx.SalesQuotToChallanRowMainDTO;
import com.opethic.genivis.dto.tranx_sales.CmpTRowDTOSoToSc;
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
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
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

public class TranxSalesChallanToInvoiceCreate implements Initializable {
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
    private HBox sc2iBottomMain, sc2itopFirstRow, sc2itopSecondRow;
    @FXML
    private VBox sc2iBottomFirstV, sc2iBottomSecondV, sc2iBottomThirdV, sc2itotalMainDiv, sc2itotalMainInnerDiv;

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
    public Button btnSubmit, btnCancel;
    //    @FXML
//    public Button btnModify;
    @FXML
    public TextField tfNarration;
    private ObservableList<String> payment_mode_list = FXCollections.observableArrayList("Credit", "Cash");

    @FXML
    BorderPane SalesChallToInvoiceScrollPane;

    @FXML
    private TableView<GstDTO> tvGST_Table;

    @FXML
    private TableColumn<GstDTO, String> tc_gst;

    @FXML
    private TableColumn<GstDTO, String> tc_cgst;

    @FXML
    private TableColumn<GstDTO, String> tc_sgst, tc_igst;


    @FXML
    private TableView<PurchaseInvoiceTable> tvPurchaseInvoiceTable;

    @FXML
    private TableColumn<PurchaseInvoiceTable, String> tcSrNo;

    @FXML
    private TableColumn<PurchaseInvoiceTable, String> tcParticulars;

    @FXML
    private TableColumn<PurchaseInvoiceTable, String> tcPackage;


    @FXML
    private TableColumn<PurchaseInvoiceTable, String> tcLevelA;


    @FXML
    private TableColumn<PurchaseInvoiceTable, String> tcLevelB;


    @FXML
    private TableColumn<PurchaseInvoiceTable, String> tcLevelC;


    @FXML
    private TableColumn<PurchaseInvoiceTable, String> tcUnit;


    @FXML
    private TableColumn<PurchaseInvoiceTable, String> tcBatch;

    @FXML
    private TableColumn<PurchaseInvoiceTable, String> tcQuantity;

    @FXML
    private TableColumn<PurchaseInvoiceTable, String> tcFreeQty;

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

    private final ObservableList<LevelAForPurInvoice> levelAList = FXCollections.observableArrayList();

    private final ObservableList<LevelBForPurInvoice> levelBList = FXCollections.observableArrayList();

    private final ObservableList<CommonDTO> levelCList = FXCollections.observableArrayList();

    private final ObservableList<CommonDTO> unitList = FXCollections.observableArrayList();

    @FXML
    private TextField tfTranxDate, tfInvoiceNo, tfLedgerName, tfInvoiceDate, tfSalesSerial;

    @FXML
    private ComboBox<CommonDTO> cbSalesAc;

    @FXML
    private ComboBox<CommonDTO> cbSupplierGSTIN;

    String purchaseAcID = "";

    String supplierGSTIN_ID = "";

    String ledger_id = "", ledgerName = "";

//    @FXML
//    RadioButton rbCredit, rbCash;

    private final Logger SalesChallanToInvoicelogger = LogManager.getLogger(TranxSalesChallanToInvoiceCreate.class);

    private final ObservableList<CommonDTO> purchaseAcList = FXCollections.observableArrayList();

    private final ObservableList<CommonDTO> supplierGSTINList = FXCollections.observableArrayList();

    private final ToggleGroup toggleGroup = new ToggleGroup();

    private String selectedPayMode = "Credit";

    @FXML
    private Label lbGrossTotal, lbDiscount, lbTotal, lbTax, lbBillAmount, lbtotalQty, lblFreeQty;
    @FXML
    private Label lblScToSiCgstTotal, lblScToSiSgstTotal, lblScToSiIgstTotal;


    private Map<String, String> purchase_invoice_map = new HashMap<>();
    private Map<String, String> deleted_rows_map = new HashMap<>();

    private String ledgerStateCode = "";
    private Boolean taxFlag = false;

    private String totalamt = "";

    private Double total_taxable_amt = 0.0;

    private String total_purchase_discount_amt = "";

    @FXML
    private TextField tfTotalDisPer, tfTotalDisAmt, tfAddCharges;
    String gst_no = "";
    List<CustomAddChargesDTO> addChargesDTOList = new ArrayList<>();
    private final List<AddChargesListDTO> addChargesResLst = new ArrayList<>();

    @FXML
    private TableView<InvoiceProductHistoryTable> tvInvoiceProductHistory;

    @FXML
    private TableColumn<InvoiceProductHistoryTable, String> cmSupplierName;

    @FXML
    private TableColumn<InvoiceProductHistoryTable, String> cmInvNo;

    @FXML
    private TableColumn<InvoiceProductHistoryTable, String> cmInvDate;

    @FXML
    private TableColumn<InvoiceProductHistoryTable, String> cmBatch;

    @FXML
    private TableColumn<InvoiceProductHistoryTable, String> cmMRP;

    @FXML
    private TableColumn<InvoiceProductHistoryTable, String> cmQty;

    @FXML
    private TableColumn<InvoiceProductHistoryTable, String> cmRate;

    @FXML
    private TableColumn<InvoiceProductHistoryTable, String> cmCost;

    @FXML
    private TableColumn<InvoiceProductHistoryTable, String> cmDisPer;

    @FXML
    private TableColumn<InvoiceProductHistoryTable, String> cmDisAmt;

    private Integer purchase_id = -1;

    private final Map<String, Object> edit_response_map = new HashMap<>();

    private final Map<String, String> invoice_data_map = new HashMap<>();

    private final List<SalesRowListDTO> rowListDTOS = new ArrayList<>();


    @FXML
    private Label lblPurInvGstNo, lblPurInvArea, lblPurInvBank, lblPurInvContactPerson, lblPurInvTransportName, lblPurInvCreditDays, lblPurInvFssai, lblPurInvLicenseNo, lblPurInvRoute, lblPurInvProductBrand, lblPurInvProductGroup, lblPurInvProductCategory, lblPurInvProductSubGroup, lblPurInvProductHsn, lblPurInvProductTaxType, lblPurInvProductTaxPer, lblPurInvProductMarginPer, lblPurInvProductCost, lblPurInvProductShelfId, lblPurInvProductMinStock, lblPurInvProductMaxStock;

    @FXML
    private Label sales_challan, sales_orders, sales_qoutation;
    private Long stateCode = 0L, CompanyStateCode = 0L;
    private String Debtor_id = "", id = "", productId = "";
    private JsonArray deletedRows = new JsonArray();

    public static String ChallanId = "";
    private String trackingNumber = "";
    private Map<String, String> map = new HashMap<>();


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        System.out.println("challanId in challan to invoice>>> " + ChallanId);
        ResponsiveWiseCssPicker();

        // autofocus on TranxDate
        Platform.runLater(() -> {
            tfLedgerName.requestFocus();
        });

        initialEnterMethod();
        getSalesChallanDataByIdNew(ChallanId);
//        }

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

        tvGST_Table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
//        SalesChallToInvoiceScrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        tfLedgerName.setEditable(true);

        tableInitiliazation();

        cbSalesAc.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.TAB && !event.isShiftDown() || event.getCode() == KeyCode.ENTER) {
                tvPurchaseInvoiceTable.edit(0, tvPurchaseInvoiceTable.getColumns().get(1));
                event.consume();
            }
        });

        getPurchaseSerial();


        tfSalesSerial.setEditable(false);
        tfSalesSerial.setFocusTraversable(false);

        tfTranxDate.setFocusTraversable(false);
        tfTranxDate.setEditable(false);
        LocalDate current_date = LocalDate.now();
        //!Changed code
        if (purchase_id > 0) {
            LocalDate transaction_dt = LocalDate.parse(invoice_data_map.get("transaction_dt"));
            tfTranxDate.setText(transaction_dt.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
            System.out.println("Invoice DT:" + invoice_data_map.get("invoice_dt"));
            LocalDate invoice_dt = LocalDate.parse(invoice_data_map.get("invoice_dt"));
            tfInvoiceDate.setText(invoice_dt.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        } else {
            tfTranxDate.setText(current_date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
            DateValidator.applyDateFormat(tfInvoiceDate);
            LocalDate currentDate = LocalDate.now();
            tfInvoiceDate.setText(currentDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
            sceneInitilization();
        }

        //cbSalesAc.setFocusTraversable(true);

        cbSupplierGSTIN.setFocusTraversable(true);

        getPurchaseAc();
        tfLededgerNameEvent();
        AutoCompleteBox<CommonDTO> autoCompleteBox = new AutoCompleteBox<>(cbSupplierGSTIN, -1);
        if (purchase_id > 0) {
            tfLedgerName.setText(ledgersById(Long.valueOf(invoice_data_map.get("supplierId"))));
            if (supplierGSTINList != null && !supplierGSTINList.isEmpty()) {
                cbSupplierGSTIN.setItems(supplierGSTINList);
                cbSupplierGSTIN.setValue(supplierGSTINList.get(0));
                cbSupplierGSTIN.getSelectionModel().select(0);
            }
        }

        if (purchase_id > 0) {
            tfInvoiceNo.setText(invoice_data_map.get("bill_no"));
        }

        if (purchase_id > 0) {

            for (CommonDTO commonDTO : purchaseAcList) {
                if (commonDTO.getId().equals(invoice_data_map.get("purchase_account_ledger_id"))) {
                    cbSalesAc.setValue(commonDTO);
                    purchaseAcID = commonDTO.getId();
                }
            }
        }

        if (purchase_id > 0) {
            Float dis_per_float = (Float) edit_response_map.get("discountInPer");
            tfTotalDisPer.setText(String.valueOf(dis_per_float));
            String discText = tfTotalDisPer.getText();
            if (!TextUtils.isEmpty(discText)) {
                double totalTaxableAmt = 0.0;
                for (int i = 0; i < tvPurchaseInvoiceTable.getItems().size(); i++) {
                    SalesChallanToInvoiceCalculation.rowCalculationForPurcInvoice(i, tvPurchaseInvoiceTable, callback);//call row calculation function
                    PurchaseInvoiceTable purchaseInvoiceTable = tvPurchaseInvoiceTable.getItems().get(i);
                    totalTaxableAmt = totalTaxableAmt + Double.parseDouble(purchaseInvoiceTable.getTaxable_amt());
                }
                double disc_per = Double.parseDouble(discText);
                System.out.println("totalTaxableAmount >> : " + totalTaxableAmt);
                Double amount = (totalTaxableAmt * disc_per) / 100;
                tfTotalDisAmt.setText(String.valueOf(amount));
                SalesChallanToInvoiceCalculation.discountPropotionalCalculation(disc_per + "", "0", tfAddCharges.getText(), tvPurchaseInvoiceTable, callback);
                if (!tfAddCharges.getText().isEmpty()) {
                    System.out.println("Additioanal CHarges Called >> ");
                    SalesChallanToInvoiceCalculation.additionalChargesCalculation(tfAddCharges.getText(), tvPurchaseInvoiceTable, callback);
                }
            } else {
                tfTotalDisAmt.setText("");
                SalesChallanToInvoiceCalculation.discountPropotionalCalculation("0", "0", tfAddCharges.getText(), tvPurchaseInvoiceTable, callback);
                if (!tfAddCharges.getText().isEmpty()) {
                    System.out.println("Additioanal CHarges Called >> ");
                    SalesChallanToInvoiceCalculation.additionalChargesCalculation(tfAddCharges.getText(), tvPurchaseInvoiceTable, callback);
                }
            }

            //? Calculate Tax for each Row
            SalesChallanToInvoiceCalculation.calculateGst(tvPurchaseInvoiceTable, callback);


        }
        invoiceProductHistoryTable();

//        btnModify.setOnAction(actionEvent -> {
//            GlobalController.getInstance().addTabStatic(SALES_INVOICE_LIST_SLUG, false);
//        });

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
        tblResponsive();
    }

    public void tblResponsive() {
        //?  Responsive code for tableview
        tcSrNo.prefWidthProperty().bind(tvPurchaseInvoiceTable.widthProperty().multiply(0.04));
//        tcSalesOrderCmpTRowParticulars.prefWidthProperty().bind(tvSalesOrderCmpTRow.widthProperty().multiply(0.3));
        tcPackage.prefWidthProperty().bind(tvPurchaseInvoiceTable.widthProperty().multiply(0.05));
        tcUnit.prefWidthProperty().bind(tvPurchaseInvoiceTable.widthProperty().multiply(0.05));
        tcQuantity.prefWidthProperty().bind(tvPurchaseInvoiceTable.widthProperty().multiply(0.05));
//        tcSalesOrderCmpTRowGrossAmount.prefWidthProperty().bind(tvPurchaseInvoiceTable.widthProperty().multiply(0.15));
        tcDisPer.prefWidthProperty().bind(tvPurchaseInvoiceTable.widthProperty().multiply(0.05));
        tcDisAmt.prefWidthProperty().bind(tvPurchaseInvoiceTable.widthProperty().multiply(0.05));
        tcTax.prefWidthProperty().bind(tvPurchaseInvoiceTable.widthProperty().multiply(0.05));
//        tcSalesOrderCmpTRowNetAmount.prefWidthProperty().bind(tvPurchaseInvoiceTable.widthProperty().multiply(0.08));
        tcAction.prefWidthProperty().bind(tvPurchaseInvoiceTable.widthProperty().multiply(0.02));
        if (tcFreeQty.isVisible()) {
            tcFreeQty.prefWidthProperty().bind(tvPurchaseInvoiceTable.widthProperty().multiply(0.05));
            tcRate.prefWidthProperty().bind(tvPurchaseInvoiceTable.widthProperty().multiply(0.05));
        } else {
            tcRate.prefWidthProperty().bind(tvPurchaseInvoiceTable.widthProperty().multiply(0.05));
        }
//        columnVisibility(tcLevelA, Globals.getUserControlsWithSlug("is_level_a"));
//        columnVisibility(tcLevelB, Globals.getUserControlsWithSlug("is_level_b"));
//        columnVisibility(tcLevelC, Globals.getUserControlsWithSlug("is_level_c"));
        if (Globals.getUserControlsWithSlug("is_level_a") == true && Globals.getUserControlsWithSlug("is_level_b") == false && Globals.getUserControlsWithSlug("is_level_c") == false) {
//            System.out.println("inside first condn");
            tcParticulars.prefWidthProperty().bind(tvPurchaseInvoiceTable.widthProperty().multiply(0.30));
            tcGrossAmount.prefWidthProperty().bind(tvPurchaseInvoiceTable.widthProperty().multiply(0.05));
            tcNetAmount.prefWidthProperty().bind(tvPurchaseInvoiceTable.widthProperty().multiply(0.05));
            tcLevelA.prefWidthProperty().bind(tvPurchaseInvoiceTable.widthProperty().multiply(0.08));
        } else if (Globals.getUserControlsWithSlug("is_level_a") == true && Globals.getUserControlsWithSlug("is_level_b") == true && Globals.getUserControlsWithSlug("is_level_c") == false) {
//            System.out.println("inside second condn");
            tcParticulars.prefWidthProperty().bind(tvPurchaseInvoiceTable.widthProperty().multiply(0.25));
            tcGrossAmount.prefWidthProperty().bind(tvPurchaseInvoiceTable.widthProperty().multiply(0.05));
            tcNetAmount.prefWidthProperty().bind(tvPurchaseInvoiceTable.widthProperty().multiply(0.05));
            tcLevelA.prefWidthProperty().bind(tvPurchaseInvoiceTable.widthProperty().multiply(0.07));
            tcLevelB.prefWidthProperty().bind(tvPurchaseInvoiceTable.widthProperty().multiply(0.07));
        } else if (Globals.getUserControlsWithSlug("is_level_a") == true && Globals.getUserControlsWithSlug("is_level_b") == true && Globals.getUserControlsWithSlug("is_level_c") == true) {
//            System.out.println("inside thrd condn");
            tcParticulars.prefWidthProperty().bind(tvPurchaseInvoiceTable.widthProperty().multiply(0.20));
            tcGrossAmount.prefWidthProperty().bind(tvPurchaseInvoiceTable.widthProperty().multiply(0.05));
            tcNetAmount.prefWidthProperty().bind(tvPurchaseInvoiceTable.widthProperty().multiply(0.05));
            tcLevelA.prefWidthProperty().bind(tvPurchaseInvoiceTable.widthProperty().multiply(0.06));
            tcLevelB.prefWidthProperty().bind(tvPurchaseInvoiceTable.widthProperty().multiply(0.06));
            tcLevelC.prefWidthProperty().bind(tvPurchaseInvoiceTable.widthProperty().multiply(0.06));
        } else {
//            System.out.println("inside fourth condn");
            tcParticulars.prefWidthProperty().bind(tvPurchaseInvoiceTable.widthProperty().multiply(0.38));
            tcGrossAmount.prefWidthProperty().bind(tvPurchaseInvoiceTable.widthProperty().multiply(0.05));
            tcNetAmount.prefWidthProperty().bind(tvPurchaseInvoiceTable.widthProperty().multiply(0.05));
        }

        btnSubmit.setOnAction(event -> {
            invoiceValidations();
        });
        btnSubmit.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                invoiceValidations();
            }
        });
        btnSubmit.setOnMouseClicked(eventt -> {
            invoiceValidations();
        });

    }


    private void ResponsiveWiseCssPicker() {
        double height = Screen.getPrimary().getBounds().getHeight();
        double width = Screen.getPrimary().getBounds().getWidth();
        System.out.println("width" + width);
        if (width >= 992 && width <= 1024) {
            sc2itopFirstRow.setSpacing(10);
            sc2itopSecondRow.setSpacing(10);
            sc2itotalMainDiv.setSpacing(10);
            sc2itotalMainInnerDiv.setSpacing(8);
            // Set preferred widths as percentages of the HBox width
            sc2iBottomFirstV.prefWidthProperty().bind(sc2iBottomMain.widthProperty().multiply(0.54));
            sc2iBottomSecondV.prefWidthProperty().bind(sc2iBottomMain.widthProperty().multiply(0.24));
            sc2iBottomThirdV.prefWidthProperty().bind(sc2iBottomMain.widthProperty().multiply(0.22));
//            so2cBottomMain.setPrefHeight(150);
            tvGST_Table.getStylesheets().add(GenivisApplication.class.getResource("ui/css/purchase_challan_css/gst_table_css/gst_table_992_1024.css").toExternalForm());
            tvInvoiceProductHistory.getStylesheets().add(GenivisApplication.class.getResource("ui/css/purchase_challan_css/supplier_table_css/supplier_table_992_1024.css").toExternalForm());
            SalesChallToInvoiceScrollPane.getStylesheets().add(GenivisApplication.class.getResource("ui/css/TranxCommonCssStyles/tranxCommonCssStyle1.css").toExternalForm());
        } else if (width >= 1025 && width <= 1280) {
            sc2itopFirstRow.setSpacing(10);
            sc2itopSecondRow.setSpacing(10);
            sc2itotalMainDiv.setSpacing(10);
            sc2itotalMainInnerDiv.setSpacing(8);
            // Set preferred widths as percentages of the HBox width
            sc2iBottomFirstV.prefWidthProperty().bind(sc2iBottomMain.widthProperty().multiply(0.58));
            sc2iBottomSecondV.prefWidthProperty().bind(sc2iBottomMain.widthProperty().multiply(0.20));
            sc2iBottomThirdV.prefWidthProperty().bind(sc2iBottomMain.widthProperty().multiply(0.22));
//            so2cBottomMain.setPrefHeight(150);
            tvGST_Table.getStylesheets().add(GenivisApplication.class.getResource("ui/css/purchase_challan_css/gst_table_css/gst_table_1025_1280.css").toExternalForm());
            tvInvoiceProductHistory.getStylesheets().add(GenivisApplication.class.getResource("ui/css/purchase_challan_css/supplier_table_css/supplier_table_1025_1280.css").toExternalForm());
            SalesChallToInvoiceScrollPane.getStylesheets().add(GenivisApplication.class.getResource("ui/css/TranxCommonCssStyles/tranxCommonCssStyle2.css").toExternalForm());
        } else if (width >= 1281 && width <= 1368) {
            sc2itopFirstRow.setSpacing(12);
            sc2itopSecondRow.setSpacing(12);
            sc2itotalMainDiv.setSpacing(12);
            sc2itotalMainInnerDiv.setSpacing(8);
            // Set preferred widths as percentages of the HBox width
            sc2iBottomFirstV.prefWidthProperty().bind(sc2iBottomMain.widthProperty().multiply(0.6));
            sc2iBottomSecondV.prefWidthProperty().bind(sc2iBottomMain.widthProperty().multiply(0.20));
            sc2iBottomThirdV.prefWidthProperty().bind(sc2iBottomMain.widthProperty().multiply(0.20));
//            so2cBottomMain.setPrefHeight(150);
            tvGST_Table.getStylesheets().add(GenivisApplication.class.getResource("ui/css/purchase_challan_css/gst_table_css/gst_table_1281_1368.css").toExternalForm());
            tvInvoiceProductHistory.getStylesheets().add(GenivisApplication.class.getResource("ui/css/purchase_challan_css/supplier_table_css/supplier_table_1281_1368.css").toExternalForm());
            SalesChallToInvoiceScrollPane.getStylesheets().add(GenivisApplication.class.getResource("ui/css/TranxCommonCssStyles/tranxCommonCssStyle3.css").toExternalForm());
        } else if (width >= 1369 && width <= 1400) {
            sc2itopFirstRow.setSpacing(15);
            sc2itopSecondRow.setSpacing(15);
            sc2itotalMainDiv.setSpacing(15);
            sc2itotalMainInnerDiv.setSpacing(8);
            // Set preferred widths as percentages of the HBox width
            sc2iBottomFirstV.prefWidthProperty().bind(sc2iBottomMain.widthProperty().multiply(0.56));
            sc2iBottomSecondV.prefWidthProperty().bind(sc2iBottomMain.widthProperty().multiply(0.20));
            sc2iBottomThirdV.prefWidthProperty().bind(sc2iBottomMain.widthProperty().multiply(0.24));
//            so2cBottomMain.setPrefHeight(150);

            tvGST_Table.getStylesheets().add(GenivisApplication.class.getResource("ui/css/purchase_challan_css/gst_table_css/gst_table_1369_1400.css").toExternalForm());
            tvInvoiceProductHistory.getStylesheets().add(GenivisApplication.class.getResource("ui/css/purchase_challan_css/supplier_table_css/supplier_table_1369_1400.css").toExternalForm());
            SalesChallToInvoiceScrollPane.getStylesheets().add(GenivisApplication.class.getResource("ui/css/TranxCommonCssStyles/tranxCommonCssStyle4.css").toExternalForm());
        } else if (width >= 1401 && width <= 1440) {
            sc2itopFirstRow.setSpacing(15);
            sc2itopSecondRow.setSpacing(15);
            sc2itotalMainDiv.setSpacing(15);
            sc2itotalMainInnerDiv.setSpacing(8);
            // Set preferred widths as percentages of the HBox width
            sc2iBottomFirstV.prefWidthProperty().bind(sc2iBottomMain.widthProperty().multiply(0.58));
            sc2iBottomSecondV.prefWidthProperty().bind(sc2iBottomMain.widthProperty().multiply(0.20));
            sc2iBottomThirdV.prefWidthProperty().bind(sc2iBottomMain.widthProperty().multiply(0.22));
//            so2cBottomMain.setPrefHeight(150);

            tvGST_Table.getStylesheets().add(GenivisApplication.class.getResource("ui/css/purchase_challan_css/gst_table_css/gst_table_1401_1440.css").toExternalForm());
            tvInvoiceProductHistory.getStylesheets().add(GenivisApplication.class.getResource("ui/css/purchase_challan_css/supplier_table_css/supplier_table_1401_1440.css").toExternalForm());
            SalesChallToInvoiceScrollPane.getStylesheets().add(GenivisApplication.class.getResource("ui/css/TranxCommonCssStyles/tranxCommonCssStyle5.css").toExternalForm());
        } else if (width >= 1441 && width <= 1680) {
            sc2itopFirstRow.setSpacing(20);
            sc2itopSecondRow.setSpacing(20);
            sc2itotalMainDiv.setSpacing(20);
            sc2itotalMainInnerDiv.setSpacing(10);
            // Set preferred widths as percentages of the HBox width
            sc2iBottomFirstV.prefWidthProperty().bind(sc2iBottomMain.widthProperty().multiply(0.62));
            sc2iBottomSecondV.prefWidthProperty().bind(sc2iBottomMain.widthProperty().multiply(0.18));
            sc2iBottomThirdV.prefWidthProperty().bind(sc2iBottomMain.widthProperty().multiply(0.20));
//            scBottomMain.setPrefHeight(150);

            tvGST_Table.getStylesheets().add(GenivisApplication.class.getResource("ui/css/purchase_challan_css/gst_table_css/gst_table_1441_1680.css").toExternalForm());
            tvInvoiceProductHistory.getStylesheets().add(GenivisApplication.class.getResource("ui/css/purchase_challan_css/supplier_table_css/supplier_table_1441_1680.css").toExternalForm());
            SalesChallToInvoiceScrollPane.getStylesheets().add(GenivisApplication.class.getResource("ui/css/TranxCommonCssStyles/tranxCommonCssStyle6.css").toExternalForm());
        } else if (width >= 1681 && width <= 1920) {
            sc2itopFirstRow.setSpacing(20);
            sc2itopSecondRow.setSpacing(20);
            sc2itotalMainDiv.setSpacing(20);
            sc2itotalMainInnerDiv.setSpacing(10);
            // Set preferred widths as percentages of the HBox width
            sc2iBottomFirstV.prefWidthProperty().bind(sc2iBottomMain.widthProperty().multiply(0.6));
            sc2iBottomSecondV.prefWidthProperty().bind(sc2iBottomMain.widthProperty().multiply(0.18));
            sc2iBottomThirdV.prefWidthProperty().bind(sc2iBottomMain.widthProperty().multiply(0.22));
//            scBottomMain.setPrefHeight(150);

            tvGST_Table.getStylesheets().add(GenivisApplication.class.getResource("ui/css/gst_table.css").toExternalForm());
            tvInvoiceProductHistory.getStylesheets().add(GenivisApplication.class.getResource("ui/css/invoice_product_history_table.css").toExternalForm());
            SalesChallToInvoiceScrollPane.getStylesheets().add(GenivisApplication.class.getResource("ui/css/TranxCommonCssStyles/tranxCommonCssStyle7.css").toExternalForm());
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

    private void tfLededgerNameEvent() {
        tfLedgerName.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.SPACE) {
                handleTfLedgerName();
            }
        });

        tfLedgerName.focusedProperty().addListener((obs, oldVal, newVal) -> {
            System.out.println("LEEEE:" + newVal);
            if (!newVal) {
                if (tfLedgerName.getText().isEmpty()) {
                    tfLedgerName.requestFocus();
                }
            }
        });


        tfLedgerName.setOnMouseClicked(event -> {
            handleTfLedgerName();
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

        tvGST_Table.getItems().addAll(new GstDTO("", "", "", ""));

    }

    private void initialEnterMethod() {
        SalesChallToInvoiceScrollPane.addEventFilter(KeyEvent.KEY_PRESSED, (KeyEvent event) -> {
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
                btnSubmit.fire();
            } else if (event.getCode() == KeyCode.X && event.isControlDown()) {
                btnCancel.fire();
            }
        });

    }

    TableCellCallback<Object[]> productID_callback = item -> {
        System.out.println("done123");
        if (item.length == 1) {
//            tranxProductDetailsFun((String) item[0]);
            System.out.println("productID_callback data ProductId " + item[0]);
            getSupplierListbyProductId((String) item[0]);
            tranxProductDetailsFun((String) item[0]);
        } else {
            System.out.println("productID_callback data BatchNo " + item[0] + " batch_detailsId " + item[1]);
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
            SalesChallanToInvoicelogger.debug("Form Data for getSupplierListbyProductId in Purchase Invoice Controller:" + formData);
            SalesChallanToInvoicelogger.debug("Network call started getSupplierListbyProductId() in Purchase Invoice Controller:");
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
                        tvInvoiceProductHistory.setItems(supplierDataList);

                        SalesChallanToInvoicelogger.debug("Network call Successed getSupplierListbyProductId() in Purchase Invoice Controller:");
                    }

                }
            });
            apiClient.setOnCancelled(new EventHandler<WorkerStateEvent>() {
                @Override
                public void handle(WorkerStateEvent workerStateEvent) {
                    SalesChallanToInvoicelogger.error("Network API cancelled in getSupplierListbyProductId()" + workerStateEvent.getSource().getValue().toString());
                }
            });
            apiClient.setOnFailed(new EventHandler<WorkerStateEvent>() {
                @Override
                public void handle(WorkerStateEvent workerStateEvent) {
                    SalesChallanToInvoicelogger.error("Network API failed in getSupplierListbyProductId()" + workerStateEvent.getSource().getValue().toString());
                }
            });
            apiClient.start();
        } catch (Exception e) {
            System.out.println("exception:" + e);
        } finally {
            apiClient = null;
        }
        SalesChallanToInvoicelogger.debug("tranxBatchDetailsFun() Ended");
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
//        ledgerStateCode = responseObj.get("ledgerStateCode").getAsString();

        JsonArray gstdetailsArray = responseObj.getAsJsonArray("gstdetails");
        if (gstdetailsArray != null) {
            for (JsonElement element : gstdetailsArray) {
                JsonObject gstdetail = element.getAsJsonObject();
                String gstin = gstdetail.get("gstin").getAsString();
                String gst_id = gstdetail.get("id").getAsString();
                supplierGSTINList.add(new CommonDTO(gstin, gst_id));
            }
        }
//        if (ledgerStateCode.equalsIgnoreCase(GlobalTranx.getCompanyStateCode().toString())) {
//            taxFlag = true;
//        }

        return ledgerName;
    }

    private void getSalesChallanDataByIdNew(String SalesChallan_id) {

        try {
//            Map<String, String> map = new HashMap<>();
//            map.put("id", String.valueOf(SalesChallan_id));
//            String formData = mapToStringforFormData(map);
            Map<String, String> body = new HashMap<>();
            body.put("sales_challan_ids", SalesChallan_id);
            System.out.println("salesChallan to Invoice challan id " + SalesChallan_id);
            String formData = Globals.mapToStringforFormData(body);

            HttpResponse<String> response = APIClient.postFormDataRequest(formData, EndPoints.TRANX_SALES_CHALLAN_DATA_WITH_IDS);
            System.out.println("TRANX_SALES_CHALLAN_DATA_WITH_IDS response body => " + response.body());
            SalesQuotToChallanResponseDTO responseBody = new Gson().fromJson(response.body(), SalesQuotToChallanResponseDTO.class);
            System.out.println("Res Invoice:" + response.body());
            if (responseBody.getResponseStatus() == 200) {
//                setEditResponse(salesEditResponseDTO);
                setConversionData(responseBody);
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setConversionData(SalesQuotToChallanResponseDTO formData) {
        trackingNumber = formData.getInvoiceData().getTrackingNumber();
        System.out.println("Tracking Number :" + trackingNumber);
        LocalDate InvoiceDate = LocalDate.now();
        tfInvoiceDate.setText(InvoiceDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        tfLedgerName.setText(formData.getInvoiceData().getDebtorsName());
        Debtor_id = String.valueOf(formData.getInvoiceData().getDebtorsId());
        /**** settin GST No to Combo Box****/
        ledgersById(Long.parseLong(Debtor_id));
        if (supplierGSTINList != null && !supplierGSTINList.isEmpty()) {
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    cbSupplierGSTIN.setItems(supplierGSTINList);
                    cbSupplierGSTIN.setValue(supplierGSTINList.get(0));
                    cbSupplierGSTIN.getSelectionModel().select(0);
                }
            });

        } else {
            System.out.println("No data for supplierGSTINList");
        }
        //  cbSupplierGSTIN.setValue();
        tfNarration.setText(formData.getInvoiceData().getNarration() != null ? formData.getInvoiceData().getNarration().toString() : "");
//        lblSCConvRoundOffEdit.setText(String.valueOf(formData.getInvoiceData().getRoundoff()));   roundoff is not in sale order so while conversion roundoff is not come in data

        ledgerStateCode = formData.getInvoiceData().getLedgerStateCode();
//        System.out.println("getSalesChallanDataByIdNew getLedgerStateCode  ---- " + ledgerStateCode);
        tranxLedgerDetailsFun(Debtor_id);
//        System.out.println("set Conversion Data Debtor_id==>> " + Debtor_id);
        chbPurInvRoundOff.setSelected(true);
        roundOffCalculations();
        //round off is not in sale order so make default checked
//        Debtor_id = String.valueOf(formData)

        tfTotalDisAmt.setText(String.valueOf(formData.getInvoiceData().getDiscountInAmt()));
        tfTotalDisPer.setText(String.valueOf(formData.getInvoiceData().getDiscountInPer()));

        int index = 0;
        double grossTotal = 0.0;     //for total bottom calculation
        double discountAmount = 0.0;
        double totalAmount = 0.0;
        double totalTax = 0.0;
        double billAmount = 0.0;
        int totalQty = 0;
        double totalFreeQty = 0.0;
        tvPurchaseInvoiceTable.getItems().clear();
        PurInvoiceCommunicator.levelAForPurInvoiceObservableList.clear();


        for (SalesQuotToChallanRowMainDTO mRow : formData.getRow()) {
//            CmpTRowDTOSoToSc row = new CmpTRowDTOSoToSc();
            PurchaseInvoiceTable row = new PurchaseInvoiceTable("", "0", "1", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "");
            System.out.println("productNamee " + mRow.getProductName());
            System.out.println("product id on data set" + mRow.getProductId());
            productId = String.valueOf(mRow.getProductId());
            SingleInputDialogs.productIdSelected = productId;

            System.out.println("setConversion data ProductId " + mRow.getBatchno());
            getSupplierListbyProductId(String.valueOf(mRow.getProductId()));
            tranxProductDetailsFun(String.valueOf(mRow.getProductId()));
//            fetchSelectedProductData(String.valueOf( mRow.getProductId()));
            row.setParticulars(mRow.getProductName());
            row.setUnit(mRow.getUnitName());
            row.setUnit_conv(mRow.getUnitConv().toString());
            row.setUnit_id(mRow.getUnitId().toString());
            row.setPackages(mRow.getPackName());
            row.setQuantity(String.valueOf(mRow.getQty()));
            //  Batch Details
            row.setBatch_or_serial(mRow.getBatchno() != null ? mRow.getBatchno() : "");
            row.setIs_batch(mRow.getIsBatch() != null ? mRow.getIsBatch().toString() : "");
            row.setB_details_id(mRow.getbDetailsId() != null ? mRow.getbDetailsId() : "");
            row.setB_no(mRow.getBatchno() != null ? mRow.getBatchno() : "");
            System.out.println("set Conversion Data  batchNo " + mRow.getBatchno() + " bDetailsId " + mRow.getbDetailsId());
            if (mRow.getBatchno() != null && mRow.getbDetailsId() != null) {
                tranxBatchDetailsFun(mRow.getBatchno().toString(), mRow.getbDetailsId().toString());
            }
            System.out.println("row ->" + mRow.getBatchno());
            if (mRow.getIsBatch() == true) {
                Integer productId = mRow.getProductId();
                TranxSelectedBatch selectedBatch = TranxCommonPopUps.getSelectedBatchFromProductId(productId, InvoiceDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")), mRow.getBatchno());
                row.setSelectedBatch(selectedBatch);
            }

            TranxSelectedProduct selectedProduct = TranxCommonPopUps.getSelectedProductFromProductId(mRow.getProductName());
            row.setSelectedProduct(selectedProduct);
            List<LevelAForPurInvoice> levelAForPurInvoiceList = ProductUnitsPackingSalesChallToInvoi.getAllProductUnitsPackingFlavour(mRow.getProductId().toString());

            ObservableList<LevelAForPurInvoice> observableLevelAList = FXCollections.observableArrayList(levelAForPurInvoiceList);
            PurInvoiceCommunicator.levelAForPurInvoiceObservableList.add(observableLevelAList);
            row.setRate(String.valueOf(mRow.getRate()));
            row.setGross_amount(mRow.getBaseAmt() != null ? String.valueOf(mRow.getBaseAmt()) : "0");    //getBaseAmt() amount is the gross amount inside the cmpt row
            row.setDis_per(String.valueOf(mRow.getDisPer()));
            row.setDis_amt(String.valueOf(mRow.getDisAmt()));
            row.setTax(String.valueOf(mRow.getIgst()));
            row.setNet_amount(String.valueOf(mRow.getFinalAmt()));
            row.setFree(String.valueOf(mRow.getFreeQty()));
            row.setIgst(String.valueOf(mRow.getIgst()));
            System.out.println("igst " + mRow.getIgst() + " sgst " + mRow.getSgst() + " cgst " + mRow.getCgst());
            System.out.println("totaligst " + mRow.getTotalIgst() + " totalsgst " + mRow.getTotalSgst() + " totalcgst " + mRow.getTotalCgst());
            row.setSgst(String.valueOf(mRow.getSgst()));
            row.setCgst(String.valueOf(mRow.getCgst()));
            row.setTotal_igst(String.valueOf(mRow.getTotalIgst()));
            row.setTotal_sgst(String.valueOf(mRow.getTotalSgst()));
            row.setTotal_cgst(String.valueOf(mRow.getTotalCgst()));
            row.setTax(String.valueOf(mRow.getGst()));
            row.setTaxable_amt(String.valueOf(mRow.getGrossAmt()));
            row.setFinal_dis_amt(String.valueOf(mRow.getRowDisAmt()));
            row.setFinal_tax_amt(String.valueOf(mRow.getTotalIgst()));
            String productId = String.valueOf(mRow.getProductId());
//            productIdSelected
            System.out.println("prodcttidd " + productId + " mRow.getRowDisAmt() " + mRow.getRowDisAmt() + " finalTaxAmt " + mRow.getTotalIgst());
            row.setProduct_id(productId);
            row.setLevelA_id(mRow.getLevelAId());
            row.setLevelB_id(mRow.getLevelBId());
            row.setLevelC_id(mRow.getLevelCId());
            row.setUnit(mRow.getUnitName());
            row.setUnit_conv(mRow.getUnitConv().toString());
            System.out.println("refId " + mRow.getReferenceId() + " refType " + mRow.getReferenceType());
            row.setDetails_id(String.valueOf(mRow.getDetailsId()));      //this details id is for product row not for batch...
            row.setIs_batch(String.valueOf(true));
            row.setFinal_amount(String.valueOf(mRow.getFinalAmt()));
            row.setReference_id(String.valueOf(mRow.getReferenceId()));
            row.setReference_type(mRow.getReferenceType());

            if (mRow.getBaseAmt() != null) {
                grossTotal = grossTotal + mRow.getBaseAmt();
            } else {
                grossTotal = 0.0;
            }
            discountAmount = discountAmount + Double.parseDouble(mRow.getRowDisAmt());
            totalAmount = totalAmount + mRow.getTotalAmt();
            totalTax = totalTax + mRow.getTotalIgst();
            billAmount = billAmount + mRow.getFinalAmt();
            totalQty = (int) (totalQty + mRow.getQty());
            System.out.println("totalFreeQtyyy-- " + mRow.getFreeQty());
            totalFreeQty = totalFreeQty + Double.parseDouble(!mRow.getFreeQty().equalsIgnoreCase("") && mRow.getFreeQty() != null ? mRow.getFreeQty() : "0");
            System.out.println("getReferenceTypeuu " + mRow.getReferenceType() + "  refId " + mRow.getReferenceId());
            tvPurchaseInvoiceTable.getItems().add(row);
            index++;
        }
        lbGrossTotal.setText(String.valueOf(grossTotal));
        lbDiscount.setText(String.format("%.2f", discountAmount));
        lbTotal.setText(String.format("%.2f", (grossTotal - discountAmount)));
        lbTax.setText(String.format("%.2f", totalTax));
        lbBillAmount.setText(String.format("%.2f", billAmount));

        lbtotalQty.setText(String.valueOf(totalQty));
        lblFreeQty.setText(String.valueOf(totalFreeQty));
        tvPurchaseInvoiceTable.refresh();

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
        SalesChallToInvoiceScrollPane.sceneProperty().addListener((observable, oldScene, newScene) -> {
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

    public void handleTfLedgerName() {
        Stage stage = (Stage) SalesChallToInvoiceScrollPane.getScene().getWindow();
        SingleInputDialogs.openSalesLedgerWithNamePopUp(stage, "Filter", ledgerName, input -> {
            SalesChallanToInvoicelogger.debug("Callback to Get all the Details of the Selected ledger and Set it in the Fields", input);
            ledgerName = (String) input[0];
            ledger_id = (String) input[1];
            ledgerStateCode = (String) input[6];
            sales_qoutation.setText((String) input[3]);
            sales_orders.setText((String) input[4]);
            sales_challan.setText((String) input[5]);
            tfLedgerName.setText(ledgerName);
            ObservableList<GstDetailsDTO> gstDetailsDTOS = (ObservableList<GstDetailsDTO>) input[2];

            cbSupplierGSTIN.getItems().clear();       //when ledger is selected then again open ledger pop up and select ledger then this clear the previous gst list
            for (GstDetailsDTO gstDetailsDTO : gstDetailsDTOS) {
                supplierGSTINList.add(new CommonDTO(gstDetailsDTO.getGstNo(), gstDetailsDTO.getId()));
            }
//            System.out.println(" supplierGSTINList >> : " + supplierGSTINList.size());
            if (supplierGSTINList != null && !supplierGSTINList.isEmpty()) {
                cbSupplierGSTIN.setItems(supplierGSTINList);
                cbSupplierGSTIN.setValue(supplierGSTINList.get(0));
                cbSupplierGSTIN.getSelectionModel().select(0);
                if (supplierGSTINList.size() > 1) {
                    cbSupplierGSTIN.requestFocus();
                } else {
                    tfInvoiceDate.requestFocus();      //SI testing issue
                }
            } else {
                tfInvoiceDate.requestFocus();         //SI testing issue
            }
            if (ledgerStateCode.equalsIgnoreCase(GlobalTranx.getCompanyStateCode().toString())) {
                taxFlag = true;
            }
            SalesChallanToInvoicelogger.debug("Opened Ledger Bill Modal:On Clicking by selecting the Ledger Name ");
//            TranxCal();
            tranxLedgerDetailsFun(ledger_id);
        }, in -> {
            System.out.println("In >> Called >. " + in);
            if (in == true) {
/*                isLedgerRed = true;
                setPurChallDataToProduct();*/
            }
        });
    }

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
                    SalesChallanToInvoicelogger.error("Network API cancelled in tranxLedgerDetailsFun() Pur Invoice" + workerStateEvent.getSource().getValue().toString());
                }
            });
            apiClient.setOnFailed(new EventHandler<WorkerStateEvent>() {
                @Override
                public void handle(WorkerStateEvent workerStateEvent) {
                    SalesChallanToInvoicelogger.error("Network API failed in tranxLedgerDetailsFun() Pur Invoice" + workerStateEvent.getSource().getValue().toString());
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
                    if (textField.getId().equals("tfLedgerName")) {
                        handleTfLedgerName();
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

            HttpResponse<String> response = APIClient.getRequest(EndPoints.SALESORDER_GET_SALES_ACCOUNTS);
            PurchaseAcRes purchaseAcRes = new Gson().fromJson(response.body(), PurchaseAcRes.class);

            if (purchaseAcRes.getResponseStatus() == 200) {

                List<PurchaseAcList> purchaseAcResLists = purchaseAcRes.getList();

                for (PurchaseAcList purchaseAcList1 : purchaseAcResLists) {
                    purchaseAcList.add(new CommonDTO(purchaseAcList1.getName(), purchaseAcList1.getId().toString()));
                }
                cbSalesAc.setItems(purchaseAcList);
                //  AutoCompleteBox<CommonDTO> autoCompleteBox = new AutoCompleteBox<>(cbSalesAc, -1);

            } else {
                System.out.println("responseObject is null");
            }
            cbSalesAc.getSelectionModel().selectFirst();
            System.out.println("Purchase Acc Id:" + cbSalesAc.getSelectionModel().getSelectedItem());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void getPurchaseSerial() {

        try {
            HttpResponse<String> response = APIClient.getRequest(EndPoints.TRANX_SALES_INVOICE_LAST_RECORD);
            PurchaseSerialRes SalesSerialRes = new Gson().fromJson(response.body(), PurchaseSerialRes.class);

            if (SalesSerialRes.getResponseStatus() == 200) {
                tfSalesSerial.setText(String.valueOf(SalesSerialRes.getCount()));
                tfInvoiceNo.setText(String.valueOf(SalesSerialRes.getSerialNo()));
//                tfTranxSalesInvoiceCreateInvoiceNo.setText(tranxSalesInvoiceLastRecordRes.getSerialNo());
//                tfTranxSalesInvoiceCreateInvoiceSrNo.setText("" + tranxSalesInvoiceLastRecordRes.getCount());
            } else {
                System.out.println("responseObject is null");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void setCbSalesAc(ActionEvent actionEvent) {
        Object[] object = new Object[1];
        object[0] = cbSalesAc.getSelectionModel().getSelectedItem();
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
        System.out.println("callback item ->");
        System.out.println("item: 0 -> " + item[0]);
        System.out.println("item: 1 -> " + item[1]);
        System.out.println("item: 2 ->" + item[2]);
        System.out.println("item: 3 ->" + item[3]);
        System.out.println("item: 4 ->" + item[4]);
        System.out.println("item: 5 ->" + item[5]);
        System.out.println("item: 6 ->" + item[6]);    //gst total
        System.out.println("item: 7 ->" + item[7]);   //sgst total
        System.out.println("item: 8 ->" + item[8]);   //cgst total
        System.out.println("taxCalculation 9 ->" + item[9]);

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

            lblScToSiSgstTotal.setVisible(false);
            lblScToSiCgstTotal.setVisible(false);
            lblScToSiIgstTotal.setVisible(true);
        } else {
            tc_igst.setVisible(false);
            tc_cgst.setVisible(true);
            tc_sgst.setVisible(true);

            lblScToSiSgstTotal.setVisible(true);
            lblScToSiCgstTotal.setVisible(true);
            lblScToSiIgstTotal.setVisible(false);
        }

       /* JsonArray jsonArray = new JsonArray();
        for (GstDTO gstDTO : gstDTOObservableList) {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("d_gst", decimalFormat.format(Double.parseDouble(gstDTO.getTaxPer())));
            jsonObject.addProperty("gst", decimalFormat.format(Double.parseDouble(gstDTO.getTaxPer()) / 2));
            jsonObject.addProperty("amt", gstDTO.getCgst());
            jsonArray.add(jsonObject);
        }
        JsonObject jsonObject = new JsonObject();
        jsonObject.add("cgst", jsonArray);
        jsonObject.add("sgst", jsonArray);*/
        System.out.println("ledgerStateCodeTax " + ledgerStateCode + " GlobalTranx.getCompanyStateCode() ");
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
            System.out.println("jsonObject.toString()cgst " + jsonObject.toString());
            map.put("taxCalculation", jsonObject.toString());
            taxFlag = true;
            map.put("taxFlag", "true");

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
//            System.out.println("jsonObject.toString()igst " + jsonObject.toString());
            map.put("taxCalculation", jsonObject.toString());
            taxFlag = false;
            map.put("taxFlag", "false");
        }

        lbBillAmount.setText((String) item[2]); //totalNetAmt for bill amount
        totalamt = (String) item[2];

        total_purchase_discount_amt = (String) item[4];

        total_taxable_amt = Double.parseDouble((String) item[5]);

        lblScToSiSgstTotal.setText((String) item[7]);
        lblScToSiCgstTotal.setText((String) item[8]);
        lblScToSiIgstTotal.setText((String) item[6]);
        lbtotalQty.setText((String) item[0]);
        lblFreeQty.setText((String) item[1]);
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
        for (PurchaseInvoiceTable cmpTRowDTO : tvPurchaseInvoiceTable.getItems()) {
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
        PurchaseInvoiceTable tranxRow = tvPurchaseInvoiceTable.getItems().get(currentIndex);
        System.out.println("unit-callback ledgerStateCode " + ledgerStateCode);
        //! ? Ledger State Code get selected
        if (ledgerStateCode != null) {
            if (ledgerStateCode.equalsIgnoreCase(Globals.mhStateCode)) {
                tranxRow.setRate(tranxRow.getUnitWiseRateMH() + "");
            } else {
                tranxRow.setRate(tranxRow.getUnitWiseRateAI() + "");
            }
        }
    };

    //new end
    public void tableInitiliazation() {

        tvPurchaseInvoiceTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);
        tvPurchaseInvoiceTable.setEditable(true);
        tvPurchaseInvoiceTable.setFocusTraversable(false);

        Label headerLabel = new Label("Sr\nNo.");
        tcSrNo.setGraphic(headerLabel);
        tcSrNo.setCellFactory(new LineNumbersCellFactory());

        tcSrNo.setStyle("-fx-alignment: CENTER;");

        tcParticulars.setCellValueFactory(cellData -> cellData.getValue().particularsProperty());
        tcParticulars.setCellFactory(column -> new TextFieldTableCellForSalesChallanToInvoi("tcParticulars", callback, productID_callback, tfNarration));

        tcPackage.setCellValueFactory(cellData -> cellData.getValue().packagesProperty());
        tcPackage.setStyle("-fx-alignment: CENTER;");

        tcLevelA.setCellValueFactory(cellData -> cellData.getValue().levelAProperty());
        tcLevelA.setCellFactory(column -> new ComboBoxTableCellForLevelASalesChallToInvoi("tcLevelA"));

        tcLevelB.setCellValueFactory(cellData -> cellData.getValue().levelBProperty());
        tcLevelB.setCellFactory(column -> new ComboBoxTableCellForLevelBSalesChallToInvoi("tcLevelB"));


        tcLevelC.setCellValueFactory(cellData -> cellData.getValue().levelCProperty());
        tcLevelC.setCellFactory(column -> new ComboBoxTableCellForLevelCSalesChallToInvoi("tcLevelC"));

        tcUnit.setCellValueFactory(cellData -> cellData.getValue().unitProperty());
        tcUnit.setCellFactory(column -> new ComboBoxTableCellForUnitSalesChallToInvoi("tcUnit", unit_callback));

        tcBatch.setCellValueFactory(cellData -> cellData.getValue().batch_or_serialProperty());
        tcBatch.setCellFactory(column -> new TextFieldTableCellForSalesChallanToInvoi("tcBatch", callback, productID_callback, tfNarration));


        tcQuantity.setCellValueFactory(cellData -> cellData.getValue().quantityProperty());
        tcQuantity.setCellFactory(column -> new TextFieldTableCellForSalesChallanToInvoi("tcQuantity", callback));
        tcQuantity.setStyle("-fx-alignment: CENTER-RIGHT;");

        tcFreeQty.setCellValueFactory(cellData -> cellData.getValue().freeProperty());
        tcFreeQty.setCellFactory(column -> new TextFieldTableCellForSalesChallanToInvoi("tcFreeQty", callback));
        tcFreeQty.setStyle("-fx-alignment: CENTER-RIGHT;");


        tcRate.setCellValueFactory(cellData -> cellData.getValue().rateProperty());
        tcRate.setCellFactory(column -> new TextFieldTableCellForSalesChallanToInvoi("tcRate", callback));
        tcRate.setStyle("-fx-alignment: CENTER-RIGHT;");

        tcGrossAmount.setCellValueFactory(cellData -> cellData.getValue().gross_amountProperty());
        //  tcGrossAmount.setCellFactory(column -> new TextFieldTableCellForPurInvoice("tcGrossAmount", callback));
        tcGrossAmount.setStyle("-fx-alignment: CENTER-RIGHT;");


        tcDisPer.setCellValueFactory(cellData -> cellData.getValue().dis_perProperty());
        tcDisPer.setCellFactory(column -> new TextFieldTableCellForSalesChallanToInvoi("tcDisPer", callback));
        tcDisPer.setStyle("-fx-alignment: CENTER-RIGHT;");


        //   tcDisPer.setCellValueFactory(cellData -> cellData.getValue().dis_perProperty());
        //  tcDisPer.setCellFactory(column -> new TextFieldTableCellForPurInvoice("tcDisPer", callback));

        tcDisAmt.setCellValueFactory(cellData -> cellData.getValue().dis_amtProperty());
        tcDisAmt.setCellFactory(column -> new TextFieldTableCellForSalesChallanToInvoi("tcDisAmt", callback));
        tcDisAmt.setStyle("-fx-alignment: CENTER-RIGHT;");


        tcTax.setCellValueFactory(cellData -> cellData.getValue().taxProperty());
        //   tcTax.setCellFactory(column -> new TextFieldTableCellForPurInvoice("tcTax", callback));
        tcTax.setStyle("-fx-alignment: CENTER-RIGHT;");


        tcNetAmount.setCellValueFactory(cellData -> cellData.getValue().net_amountProperty());
        //    tcNetAmount.setCellFactory(column -> new TextFieldTableCellForPurInvoice("tcNetAmount", callback));
        tcNetAmount.setStyle("-fx-alignment: CENTER-RIGHT;");


        tcAction.setCellValueFactory(cellData -> cellData.getValue().net_amountProperty());
        tcAction.setCellFactory(column -> new ButtonTableCellSalesChallToInvoi());
        columnVisibility(tcLevelA, Globals.getUserControlsWithSlug("is_level_a"));
        columnVisibility(tcLevelB, Globals.getUserControlsWithSlug("is_level_b"));
        columnVisibility(tcLevelC, Globals.getUserControlsWithSlug("is_level_c"));
        //! Free Qty
        columnVisibility(tcFreeQty, Globals.getUserControlsWithSlug("is_free_qty"));

        tcLevelA.setText(Globals.getUserControlsNameWithSlug("is_level_a"));
        tcLevelB.setText(Globals.getUserControlsNameWithSlug("is_level_B"));
        tcLevelC.setText(Globals.getUserControlsNameWithSlug("is_level_C"));

    }

    private void columnVisibility(TableColumn<PurchaseInvoiceTable, String> column, boolean visible) {
        if (visible) {
            column.setPrefWidth(USE_COMPUTED_SIZE);
            column.setMinWidth(USE_PREF_SIZE);
            column.setMaxWidth(Double.MAX_VALUE);
//            column.setVisible(visible);
        } else {
//            column.setVisible(visible);
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
                    SalesChallanToInvoicelogger.error("Network API cancelled in tranxProductDetailsFun()" + workerStateEvent.getSource().getValue().toString());
                }
            });
            apiClient.setOnFailed(new EventHandler<WorkerStateEvent>() {
                @Override
                public void handle(WorkerStateEvent workerStateEvent) {
                    SalesChallanToInvoicelogger.error("Network API failed in tranxProductDetailsFun()" + workerStateEvent.getSource().getValue().toString());
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
        } else {
            System.out.println("Selected Item Null cbSupplierGSTIN");
        }
    }

    public void onClickAdditionalCharges() {
        Stage stage = (Stage) SalesChallToInvoiceScrollPane.getScene().getWindow();
        AdditionalCharges.additionalCharges(stage, addChargesDTOList, total_taxable_amt, (input1, input2) -> {
            addChargesDTOList.clear();
            addChargesDTOList.addAll(input2);
            tfAddCharges.setText(input1[1]);
            SalesChallanToInvoiceCalculation.additionalChargesCalculation(input1[1], tvPurchaseInvoiceTable, callback);

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

    public void onClickSubmit(ActionEvent actionEvent) {
        invoiceValidations();

    }

    private void invoiceValidations() {
        //openAdjustBillWindow();
        Stage stage = (Stage) SalesChallToInvoiceScrollPane.getScene().getWindow();
        if (tfLedgerName.getText().isEmpty()) {
            AlertUtility.AlertDialogForErrorWithStage(stage, "Validation Error", "Enter mandatory Ledger Name Field ", output -> {
                if (output) {
                    tfLedgerName.requestFocus();
                }
            });
        } else if (tfInvoiceNo.getText().isEmpty()) {
            AlertUtility.AlertDialogForErrorWithStage(stage, "Validation Error", "Enter mandatory " + "Invoice No Field", output -> {
                if (output) {
                    tfInvoiceNo.requestFocus();
                }
            });
        } else if (tfInvoiceDate.getText().isEmpty()) {
            AlertUtility.AlertDialogForErrorWithStage(stage, "Validation Error", "Enter mandatory " + "Invoice Date Field", output -> {
                if (output) {
                    tfInvoiceDate.requestFocus();
                }
            });
        } else {
            String msg = LedgerMessageConsts.msgConfirmationOnSubmit;
            if (purchase_id > 0) msg = LedgerMessageConsts.msgConfirmationOnUpdate;
            msg += tfInvoiceNo.getText() + LedgerMessageConsts.msgInvoiceNumber;
            AlertUtility.AlertConfirmation(AlertUtility.alertTypeConfirmation, msg, input -> {
                if (input == 1) {
                    createSalesChallanToInvoice();
                }
            });

        }

    }

    private void validatePurchaseInvoiceNo() {
        try {
            SalesChallanToInvoicelogger.debug("Validating Pur Invoice Number");
            Map<String, String> map = new HashMap<>();
            map.put("supplier_id", ledger_id);
            map.put("bill_no", tfInvoiceNo.getText());
//            System.out.println("Ledger Id:" + ledger_id);
//            System.out.println("Bill No:" + tfInvoiceNo.getText());
            String formData = Globals.mapToStringforFormData(map);
            HttpResponse<String> response = APIClient.postFormDataRequest(formData, EndPoints.VALIDATE_SALES_INV_NUMBER);
            String resBody = response.body();
            JsonObject obj = new Gson().fromJson(resBody, JsonObject.class);
            if (obj.get("responseStatus").getAsInt() == 409) {
                AlertUtility.AlertWarningTimeout(AlertUtility.alertTypeWarning, obj.get("message").getAsString(), callback -> {
                    SalesChallanToInvoicelogger.debug("Validate Sales Invoice Success");
                });
                tfInvoiceNo.setText(tfInvoiceNo.getText());
                tfInvoiceNo.requestFocus();
            }
        } catch (Exception e) {
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            String exceptionAsString = sw.toString();
            SalesChallanToInvoicelogger.error("Exception in Purchase invoice validate():" + exceptionAsString);
        }

    }

    private void validatePurchaseInvoiceUpdate() {
        try {
            SalesChallanToInvoicelogger.debug("Validating Pur Invoice Number in Edit");
            Map<String, String> map = new HashMap<>();
            map.put("supplier_id", ledger_id);
            map.put("bill_no", tfInvoiceNo.getText());
            map.put("invoice_id", "" + purchase_id);
            String formData = Globals.mapToStringforFormData(map);
            HttpResponse<String> response = APIClient.postFormDataRequest(formData, EndPoints.VALIDATE_PUR_INV_NUMBER_EDIT);
            String resBody = response.body();
            JsonObject obj = new Gson().fromJson(resBody, JsonObject.class);
            if (obj.get("responseStatus").getAsInt() == 409) {
                AlertUtility.AlertWarningTimeout(AlertUtility.alertTypeWarning, obj.get("message").getAsString(), callback -> {
                    SalesChallanToInvoicelogger.debug("Validate Pur Invoice Success");
                });
                tfInvoiceNo.setText(tfInvoiceNo.getText());
                tfInvoiceNo.requestFocus();
            }
        } catch (Exception e) {
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            String exceptionAsString = sw.toString();
            SalesChallanToInvoicelogger.error("Exception in Purchase invoice validate():" + exceptionAsString);
        }

    }

    private void validateInvoiceDate() {
        try {
            SalesChallanToInvoicelogger.debug("Initiate the validateInvoiceDate() method");
            Map<String, String> map = new HashMap<>();
            String invoiceDate = Communicator.text_to_date.fromString(tfInvoiceDate.getText()).toString();
            map.put("invoiceDate", invoiceDate);
            String formData = Globals.mapToStringforFormData(map);
            HttpResponse<String> response = APIClient.postFormDataRequest(formData, EndPoints.VALIDATE_PUR_INV_DATE);
            String resBody = response.body();
            JsonObject obj = new Gson().fromJson(resBody, JsonObject.class);
            if (obj.get("responseStatus").getAsInt() == 404) {
                AlertUtility.AlertWarningTimeout(AlertUtility.alertTypeWarning, obj.get("message").getAsString(), callback -> {
                    SalesChallanToInvoicelogger.debug("Invoice Date : " + invoiceDate + " not in the Financial Year in Pur Invoice");
                    SalesChallanToInvoicelogger.debug("END of validateInvoiceDate() method in Pur Invoice");
                });
                tfInvoiceDate.requestFocus();
            }
        } catch (Exception e) {
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            String exceptionAsString = sw.toString();
            SalesChallanToInvoicelogger.error("Exception in validateInvoiceDate:" + exceptionAsString);
        }

    }


    //todo : Create and update sales Quotation api
    public void createSalesChallanToInvoice() {
        LocalDate QuotationDate = LocalDate.parse(Communicator.text_to_date.fromString(tfInvoiceDate.getText()).toString());
        LocalDate tranxDate = LocalDate.parse(Communicator.text_to_date.fromString(tfTranxDate.getText()).toString());

//        id = ChallanId.toString();
        System.out.println("ChallanId in invoice creation api " + ChallanId);

//        if (!id.equalsIgnoreCase("-1")) {
//            map.put("id", id);
//            map.put("debtors_id", Debtor_id);
//            System.out.println("debtor_id in payload " + Debtor_id);
//        } else {
        map.put("debtors_id", Debtor_id);
//        }
        map.put("bill_dt", String.valueOf(QuotationDate));
        map.put("newReference", "false");
        map.put("bill_no", tfInvoiceNo.getText());
        map.put("sales_acc_id", cbSalesAc.getValue().getId());
        map.put("sales_sr_no", tfSalesSerial.getText());
        map.put("transaction_date", String.valueOf(tranxDate));
        map.put("isRoundOffCheck", String.valueOf(chbPurInvRoundOff.isSelected()));
        map.put("roundoff", lblPurInvRoundOff.getText());
        map.put("narration", tfNarration.getText());
        map.put("totalamt", lbBillAmount.getText());
        map.put("total_purchase_discount_amt", lbDiscount.getText());
//        CommonDTO cmbDTO = cbSupplierGSTIN.getSelectionModel().getSelectedItem();
        System.out.println("gstNo:" + cbSupplierGSTIN.getEditor().getText());
        map.put("gstNo", cbSupplierGSTIN.getEditor().getText());
//        map.put("gstNo", cmbDTO.getText());
        System.out.println("cbSupplierGSTIN no in invoice create Payload " + cbSupplierGSTIN.getValue());

        map.put("tcs", "0");//static
        map.put("sales_discount", "0");//static
        map.put("sales_discount_amt", "0");//static
        map.put("total_sales_discount_amt", "0");//static
        map.put("additionalChargesTotal", "0");//static
//        map.put("additionalCharges", "");
        map.put("total_qty", lbtotalQty.getText());//static
        map.put("total_free_qty", lblFreeQty.getText().equalsIgnoreCase("") ? "0.00" : lblFreeQty.getText());//static
        map.put("total_row_gross_amt", lbGrossTotal.getText());
        map.put("total_base_amt", lbGrossTotal.getText());
        map.put("total_invoice_dis_amt", lbDiscount.getText());
        map.put("taxable_amount", lbTotal.getText());
        map.put("total_tax_amt", lbTax.getText());
        map.put("totalamt", lbBillAmount.getText());
        map.put("bill_amount", lbBillAmount.getText());

        map.put("sale_type", "sales");
        map.put("paymentMode", "credit");
        map.put("salesmanId", "");
        map.put("print_type", "");
        map.put("paymentMode", "");
        map.put("tcs_per", "0.00");
        map.put("tcs_amt", "0.00");
        map.put("tcs_mode", "");
        map.put("reference_sc_id", ChallanId);
        map.put("reference", "SLSCHN");
        map.put("transactionTrackingNo", trackingNumber);

        List<PurchaseInvoiceTable> currentItems = new ArrayList<>(tvPurchaseInvoiceTable.getItems());

        if (!currentItems.isEmpty()) {
            PurchaseInvoiceTable lastItem = currentItems.get(currentItems.size() - 1);

            if (lastItem.getParticulars() != null && lastItem.getParticulars().isEmpty()) {
                currentItems.remove(currentItems.size() - 1);
            }
        }

        List<PurchaseInvoiceTable> list = new ArrayList<>(currentItems);

        JsonArray jsonArray = new JsonArray();
        JsonObject jsonObject = new JsonObject();
        JsonObject jsonObject1 = new JsonObject();
        double taxPer = 0.0;
        for (PurchaseInvoiceTable PurchaseInvoiceTable : tvPurchaseInvoiceTable.getItems()) {
            System.out.println("SalesQuotDTOTaxPer " + PurchaseInvoiceTable.getTax());
            taxPer = PurchaseInvoiceTable.getTax() != null && PurchaseInvoiceTable.getTax() != "" ? Double.parseDouble(PurchaseInvoiceTable.getTax()) : 0.0;
            System.out.println("taxPer----- " + taxPer);
        }
        if (CompanyStateCode.equals(stateCode)) {
            /*map.put("taxFlag", "true");// static data as of now
            jsonObject.addProperty("gst", decimalFormat.format((taxPer) / 2));
            jsonObject.addProperty("amt", decimalFormat.format(Double.parseDouble(lbTax.getText()) / 2));
            jsonArray.add(jsonObject);
            jsonObject1.add("cgst", jsonArray);
            jsonObject1.add("sgst", jsonArray);
            map.put("taxCalculation", jsonObject1.toString());*/
            map.put("totalcgst", decimalFormat.format(Double.parseDouble(lbTax.getText()) / 2));
            map.put("totalsgst", decimalFormat.format(Double.parseDouble(lbTax.getText()) / 2));
            map.put("totaligst", "0.0");
            System.out.println("taxCalculation_cgst_sgst" + jsonObject1.toString());
        } else {
           /* map.put("taxFlag", "false");// static data as of now
            jsonObject.addProperty("gst", decimalFormat.format(taxPer));
            jsonObject.addProperty("amt", decimalFormat.format(Double.parseDouble(lbTax.getText())));
            jsonArray.add(jsonObject);
            jsonObject1.add("igst", jsonArray);*/
            // map.put("taxCalculation", jsonObject1.toString());
            map.put("totalcgst", "0.0");
            map.put("totalsgst", "0.0");
            map.put("totaligst", decimalFormat.format(Double.parseDouble(lbTax.getText())));
            System.out.println("taxCalculation_igst" + jsonObject1.toString());
        }
        /***** mapping Row Data into TranxPurRowDTO *****/
        ArrayList<TranxPurRowDTO> rowData = new ArrayList<>();
        for (PurchaseInvoiceTable SalesChToInData : list) {
            TranxPurRowDTO purParticularRow = new TranxPurRowDTO();
            if (!SalesChToInData.getProduct_id().isEmpty()) {
                purParticularRow.setProductId(SalesChToInData.getProduct_id());

                if (!SalesChToInData.getLevelA_id().isEmpty()) {
                    purParticularRow.setLevelaId(SalesChToInData.getLevelA_id());
                } else {
                    purParticularRow.setLevelaId("");
                }
                if (!SalesChToInData.getLevelB_id().isEmpty()) {
                    purParticularRow.setLevelbId(SalesChToInData.getLevelB_id());
                } else {
                    purParticularRow.setLevelbId("");
                }
                if (!SalesChToInData.getLevelC_id().isEmpty()) {
                    purParticularRow.setLevelcId(SalesChToInData.getLevelC_id());
                } else {
                    purParticularRow.setLevelcId("");
                }
                if (!SalesChToInData.getUnit_id().isEmpty()) {
                    purParticularRow.setUnitId(SalesChToInData.getUnit_id());
                } else {
                    purParticularRow.setUnitId("");
                }
                if (SalesChToInData.getUnit_conv() != null) {
                    purParticularRow.setUnitConv(String.valueOf(SalesChToInData.getUnit_conv()));
                } else {
                    purParticularRow.setUnitConv("0.0");
                }
                if (!SalesChToInData.getQuantity().isEmpty()) {
                    purParticularRow.setQty(SalesChToInData.getQuantity());
                } else {
                    purParticularRow.setQty("0.0");
                }
                if (!SalesChToInData.getFree().isEmpty()) {
                    purParticularRow.setFreeQty(SalesChToInData.getFree());
                } else {
                    purParticularRow.setFreeQty("0.0");
                }
                if (!SalesChToInData.getRate().isEmpty()) {
                    purParticularRow.setRate(SalesChToInData.getRate());
                } else {
                    purParticularRow.setRate("0.0");
                }
                if (SalesChToInData.getBase_amt() != null) {
                    purParticularRow.setBaseAmt(String.valueOf(SalesChToInData.getBase_amt()));
                } else {
                    purParticularRow.setBaseAmt("0.0");
                }
                if (!SalesChToInData.getDis_amt().isEmpty()) {
                    purParticularRow.setDisAmt(SalesChToInData.getDis_amt());
                } else {
                    purParticularRow.setDisAmt("0.0");
                }
                if (!SalesChToInData.getDis_per().isEmpty()) {
                    purParticularRow.setDisPer(SalesChToInData.getDis_per());
                } else {
                    purParticularRow.setDisPer("0.0");
                }
                if (!SalesChToInData.getDis_per2().isEmpty()) {
                    purParticularRow.setDisPer2(SalesChToInData.getDis_per2());
                } else {
                    purParticularRow.setDisPer2("0.0");
                }
                if (SalesChToInData.getDis_per_cal() != null) {
                    purParticularRow.setDisPerCal(String.valueOf(SalesChToInData.getDis_per_cal()));
                } else {
                    purParticularRow.setDisPerCal("0.0");
                }
                if (SalesChToInData.getDis_amt_cal() != null) {
                    purParticularRow.setDisAmtCal(String.valueOf(SalesChToInData.getDis_amt_cal()));
                } else {
                    purParticularRow.setDisAmtCal("0.0");
                }
                if (SalesChToInData.getRow_dis_amt() != null) {
                    purParticularRow.setRowDisAmt(String.valueOf(SalesChToInData.getRow_dis_amt()));
                } else {
                    purParticularRow.setRowDisAmt("0.0");
                }
                if (SalesChToInData.getGross_amount() != null) {
                    purParticularRow.setGrossAmt(String.valueOf(SalesChToInData.getGross_amount()));
                } else {
                    purParticularRow.setGrossAmt("0.0");
                }
                if (SalesChToInData.getAdd_chg_amt() != null) {
                    purParticularRow.setAddChgAmt(String.valueOf(SalesChToInData.getAdd_chg_amt()));
                } else {
                    purParticularRow.setAddChgAmt("0.0");
                }
                if (SalesChToInData.getGross_amount1() != null) {
                    purParticularRow.setGrossAmt1(String.valueOf(SalesChToInData.getGross_amount1()));
                } else {
                    purParticularRow.setGrossAmt1("0.0");
                }
                if (SalesChToInData.getInvoice_dis_amt() != null) {
                    purParticularRow.setInvoiceDisAmt(String.valueOf(SalesChToInData.getInvoice_dis_amt()));
                } else {
                    purParticularRow.setInvoiceDisAmt("0.0");
                }
                if (SalesChToInData.getTotal_amt() != null) {
                    purParticularRow.setTotalAmt(String.valueOf(SalesChToInData.getTotal_amt()));
                } else {
                    purParticularRow.setTotalAmt("0.0");
                }
                if (SalesChToInData.getGst() != null) {
                    purParticularRow.setGst(String.valueOf(SalesChToInData.getGst()));
                } else {
                    purParticularRow.setGst("0.0");
                }
                if (SalesChToInData.getCgst() != null) {
                    purParticularRow.setCgst(String.valueOf(SalesChToInData.getCgst_per()));
                } else {
                    purParticularRow.setCgst("0.0");
                }
                if (SalesChToInData.getIgst() != null) {
                    purParticularRow.setIgst(String.valueOf(SalesChToInData.getIgst_per()));
                } else {
                    purParticularRow.setIgst("0.0");
                }
                if (SalesChToInData.getSgst() != null) {
                    purParticularRow.setSgst(String.valueOf(SalesChToInData.getSgst_per()));
                } else {
                    purParticularRow.setSgst("0.0");
                }
                if (SalesChToInData.getTotal_igst() != null) {
                    purParticularRow.setTotalIgst(String.valueOf(SalesChToInData.getTotal_igst()));
                } else {
                    purParticularRow.setTotalIgst("0.0");
                }
                if (SalesChToInData.getTotal_cgst() != null) {
                    purParticularRow.setTotalCgst(String.valueOf(SalesChToInData.getTotal_cgst()));
                } else {
                    purParticularRow.setTotalCgst("0.0");
                }
                if (SalesChToInData.getTotal_sgst() != null) {
                    purParticularRow.setTotalSgst(String.valueOf(SalesChToInData.getTotal_sgst()));
                } else {
                    purParticularRow.setTotalSgst("0.0");
                }
                if (SalesChToInData.getFinal_amount() != null) {
                    purParticularRow.setFinalAmt(String.valueOf(SalesChToInData.getFinal_amount()));
                } else {
                    purParticularRow.setFinalAmt("0.0");
                }
                if (!SalesChToInData.getB_no().isEmpty()) {
                    purParticularRow.setBatch(SalesChToInData.getB_no());
                } else {
                    purParticularRow.setBatch("0");
                }
                if (!SalesChToInData.getB_details_id().isEmpty()) {
                    purParticularRow.setbDetailsId(SalesChToInData.getB_details_id());
                } else {
                    purParticularRow.setbDetailsId("0");
                }
                if (!SalesChToInData.getIs_batch().isEmpty()) {
                    purParticularRow.setBatch(SalesChToInData.getIs_batch());
                } else {
                    purParticularRow.setBatch("");
                }
                if (!SalesChToInData.getSr_no().isEmpty()) {
                    purParticularRow.setSerialNo(null);
                } else {
                    purParticularRow.setSerialNo(null);
                }
                purParticularRow.setDetailsId("0");
                if (!SalesChToInData.getReference_id().isEmpty()) {
                    purParticularRow.setReferenceId(SalesChToInData.getReference_id());
                } else {
                    purParticularRow.setReferenceId("");
                }
                if (!SalesChToInData.getReference_type().isEmpty()) {
                    purParticularRow.setReferenceType(SalesChToInData.getReference_type());
                } else {
                    purParticularRow.setReferenceType("");
                }


                rowData.add(purParticularRow);
                System.out.println("refTypeee " + SalesChToInData.getReference_type() + " refIddd " + SalesChToInData.getReference_id());
            }

        }

        String mRowData = new Gson().toJson(rowData, new TypeToken<List<TranxPurRowDTO>>() {
        }.getType());
        map.put("row", mRowData);

        HttpResponse<String> response;
        String formData = Globals.mapToStringforFormData(map);
        System.out.println("FinalReq=> in Sales challan to invoice formData" + formData);

        APIClient apiClient = null;
        apiClient = new APIClient(EndPoints.TRANX_SALES_INVOICE_CREATE, formData, RequestType.FORM_DATA);
        try {
            apiClient.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
                @Override
                public void handle(WorkerStateEvent workerStateEvent) {
                    String requestBody = workerStateEvent.getSource().getValue().toString();
                    JsonObject responseBody = new Gson().fromJson(requestBody, JsonObject.class);
                    SalesChallanToInvoicelogger.debug("responseBody of Sales Challan to Invoice" + responseBody);
                    AlertUtility.CustomCallback callback = (number) -> {
                        if (number == 1) {
                            GlobalController.getInstance().addTabStatic(SALES_INVOICE_LIST_SLUG, false);
                        }
                    };
                    Stage stage = (Stage) SalesChallToInvoiceScrollPane.getScene().getWindow();
                    if (responseBody.get("responseStatus").getAsInt() == 200) {

                        AlertUtility.AlertSuccessTimeout("Success", responseBody.get("message").getAsString(), callback);
                    } else {
                        AlertUtility.AlertErrorTimeout(AlertUtility.alertTypeError, responseBody.get("message").getAsString(), callback);
                    }
                }
            });
            apiClient.setOnCancelled(new EventHandler<WorkerStateEvent>() {

                @Override
                public void handle(WorkerStateEvent workerStateEvent) {
                    SalesChallanToInvoicelogger.debug("Sales Quotation----> setOnCancelled() " +
                            workerStateEvent.getSource().getValue().toString());
                }
            });
            apiClient.setOnFailed(new EventHandler<WorkerStateEvent>() {
                @Override
                public void handle(WorkerStateEvent workerStateEvent) {
                    SalesChallanToInvoicelogger.debug("Sales Challan to invoice---> setOnFailed() " +
                            workerStateEvent.getSource().getValue().toString());
                }
            });
            apiClient.start();
        } catch (Exception e) {
            apiClient = null;
        }
    }

    public void purc_disc_per(KeyEvent keyEvent) {
        CommonValidationsUtils.restrictToDecimalNumbers(tfTotalDisPer);
        String discText = tfTotalDisPer.getText();
        if (!TextUtils.isEmpty(discText)) {
            double totalTaxableAmt = 0.0;
            for (int i = 0; i < tvPurchaseInvoiceTable.getItems().size(); i++) {
                System.out.println("Gross amt in Prop--->" + tvPurchaseInvoiceTable.getItems().get(i).getGross_amount());
                SalesChallanToInvoiceCalculation.rowCalculationForPurcInvoice(i, tvPurchaseInvoiceTable, callback);//call row calculation function
                PurchaseInvoiceTable purchaseInvoiceTable = tvPurchaseInvoiceTable.getItems().get(i);
                totalTaxableAmt = totalTaxableAmt + Double.parseDouble(purchaseInvoiceTable.getTaxable_amt());

            }
            double disc_per = Double.parseDouble(discText);
            System.out.println("totalTaxableAmount >> : " + totalTaxableAmt);
            Double amount = (totalTaxableAmt * disc_per) / 100;
            tfTotalDisAmt.setText(String.valueOf(amount));
            SalesChallanToInvoiceCalculation.discountPropotionalCalculation(disc_per + "", "0", tfAddCharges.getText(), tvPurchaseInvoiceTable, callback);
            if (!tfAddCharges.getText().isEmpty()) {
                System.out.println("Additioanal Charges Called >> ");
                SalesChallanToInvoiceCalculation.additionalChargesCalculation(tfAddCharges.getText(), tvPurchaseInvoiceTable, callback);
            }
        } else {
            tfTotalDisAmt.setText("");
            SalesChallanToInvoiceCalculation.discountPropotionalCalculation("0", "0", tfAddCharges.getText(), tvPurchaseInvoiceTable, callback);
            if (!tfAddCharges.getText().isEmpty()) {
                System.out.println("Additioanal Charges Called >> ");
                SalesChallanToInvoiceCalculation.additionalChargesCalculation(tfAddCharges.getText(), tvPurchaseInvoiceTable, callback);
            }
        }

        //? Calculate Tax for each Row
        SalesChallanToInvoiceCalculation.calculateGst(tvPurchaseInvoiceTable, callback);

    }

    public void purc_disc_amt(KeyEvent keyEvent) {
        CommonValidationsUtils.restrictToDecimalNumbers(tfTotalDisAmt);
        String discAmtText = tfTotalDisAmt.getText();
        if (!TextUtils.isEmpty(discAmtText)) {
            double totalTaxableAmt = 0.0;
            for (int i = 0; i < tvPurchaseInvoiceTable.getItems().size(); i++) {
                System.out.println("Gross amt in Prop--->" + tvPurchaseInvoiceTable.getItems().get(i).getGross_amount());
                SalesChallanToInvoiceCalculation.rowCalculationForPurcInvoice(i, tvPurchaseInvoiceTable, callback);//call row calculation function
                PurchaseInvoiceTable purchaseInvoiceTable = tvPurchaseInvoiceTable.getItems().get(i);
                totalTaxableAmt = totalTaxableAmt + Double.parseDouble(purchaseInvoiceTable.getTaxable_amt());

            }
            double disc_amt = Double.parseDouble(discAmtText);
            double percentage = (disc_amt / totalTaxableAmt) * 100;
            tfTotalDisPer.setText(String.format("%.2f", percentage));

            SalesChallanToInvoiceCalculation.discountPropotionalCalculation("0", disc_amt + "", tfAddCharges.getText(), tvPurchaseInvoiceTable, callback);
            if (!tfAddCharges.getText().isEmpty()) {
                System.out.println("Additioanal CHarges Called >> ");
                SalesChallanToInvoiceCalculation.additionalChargesCalculation(tfAddCharges.getText(), tvPurchaseInvoiceTable, callback);
            }
        } else {
            tfTotalDisPer.setText("");
            SalesChallanToInvoiceCalculation.discountPropotionalCalculation("0", "0", tfAddCharges.getText(), tvPurchaseInvoiceTable, callback);
            if (!tfAddCharges.getText().isEmpty()) {
                System.out.println("Additioanal CHarges Called >> ");
                SalesChallanToInvoiceCalculation.additionalChargesCalculation(tfAddCharges.getText(), tvPurchaseInvoiceTable, callback);
            }
        }

        //? Calculate Tax for each Row
        SalesChallanToInvoiceCalculation.calculateGst(tvPurchaseInvoiceTable, callback);
    }

    public void onClickCancel(ActionEvent actionEvent) {
//        GlobalController.getInstance().addTabStatic(SALES_CHALLAN_LIST_SLUG, false);
        AlertUtility.AlertConfirmation(AlertUtility.alertTypeConfirmation, LedgerMessageConsts.msgConfirmationOnCancel + tfInvoiceNo.getText() + LedgerMessageConsts.msgInvoiceNumber, input -> {
            if (input == 1) {
                GlobalController.getInstance().addTabStatic(SALES_CHALLAN_LIST_SLUG, false);
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
        SalesChallanToInvoicelogger.debug("Form Data for GET_BATCH_DETAILS_ENDPOINT in Purchase Invoice Controller:" + formData);
        SalesChallanToInvoicelogger.debug("Network call started GET_BATCH_DETAILS_ENDPOINT in Purchase Invoice Controller:");

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
                    SalesChallanToInvoicelogger.debug("Network call Success GET_BATCH_DETAILS_ENDPOINT:");

                }
            }
        });
        apiClient.setOnCancelled(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent workerStateEvent) {
                SalesChallanToInvoicelogger.error("Network API cancelled in tranxBatchDetailsFun()" + workerStateEvent.getSource().getValue().toString());

            }
        });


        apiClient.setOnFailed(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent workerStateEvent) {
                SalesChallanToInvoicelogger.error("Network API failed in tranxBatchDetailsFun()" + workerStateEvent.getSource().getValue().toString());
            }
        });
        apiClient.start();
        SalesChallanToInvoicelogger.debug("tranxBatchDetailsFun() Ended");
    }

    /*public void handleCmbPaymentMode(ActionEvent actionEvent) {
        selectedPayMode = cmbPaymentMode.getValue();

    }*/
}

class TextFieldTableCellForSalesChallanToInvoi extends TableCell<PurchaseInvoiceTable, String> {
    private final TextField textField;
    private final String columnName;

    private final TableCellCallback<Object[]> callback;
    private TableCellCallback<Object[]> productID_callback;

    private TextField button;


    public TextFieldTableCellForSalesChallanToInvoi(String columnName, TableCellCallback<Object[]> callback) {
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
        freeQtyColumn();
        rateColumn();
        disAmtColumn();
        discountPercColumn();
        batchColumn();

        netAmountColumn();

    }

    public TextFieldTableCellForSalesChallanToInvoi(String columnName, TableCellCallback<Object[]> callback, TableCellCallback<Object[]> productID_callback, TextField button) {
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
        batchColumn();
        qtyColumn();
        freeQtyColumn();//new2.0
        rateColumn();
        disAmtColumn();
        discountPercColumn();
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
                    TableColumn<PurchaseInvoiceTable, ?> colName = getTableView().getColumns().get(1);
                    getTableView().edit(getIndex(), colName);
                }

                if (event.getCode() == KeyCode.ENTER || !event.isShiftDown() && event.getCode() == KeyCode.TAB) {
                    if (!textField.getText().isEmpty()) {
                        TableColumn<PurchaseInvoiceTable, ?> colName = getTableView().getColumns().get(7);
                        getTableView().edit(getIndex(), colName);
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
                    openProduct(getIndex());
                } else if (event.isShiftDown() && event.getCode() == KeyCode.TAB) {
                    if (getIndex() == 0) {
                        TableColumn<PurchaseInvoiceTable, ?> colName = getTableView().getColumns().get(7);
                        getTableView().edit(getIndex() - 1, colName);
                    } else {
                        TableColumn<PurchaseInvoiceTable, ?> colName = getTableView().getColumns().get(13);
                        getTableView().edit(getIndex() - 1, colName);
                    }
                    event.consume();
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
                event.consume();
                openProduct(getIndex());
            });
        }
    }

    private void qtyColumn() {
        if ("tcQuantity".equals(columnName)) {
            textField.setEditable(true);
            textField.setOnKeyPressed(event -> {
                if (!textField.getText().isEmpty()) {
                    CommonValidationsUtils.restrictToNumbers(textField);
                }
                if (event.isShiftDown() && event.getCode() == KeyCode.TAB) {
                    TableColumn<PurchaseInvoiceTable, ?> colName = getTableView().getColumns().get(7);
                    getTableView().edit(getIndex(), colName);
                } else if (event.getCode() == KeyCode.ENTER || !event.isShiftDown() && event.getCode() == KeyCode.TAB) {
                    Integer index = getIndex();
                    if (!textField.getText().isEmpty()) {
                        int cnt = Globals.getUserControlsWithSlug("is_free_qty") == true ? 9 : 10;
                        TableColumn<PurchaseInvoiceTable, ?> colName = getTableView().getColumns().get(cnt);
                        getTableView().edit(index, colName);
                    } else {
                        textField.requestFocus();
                    }
                    SalesChallanToInvoiceCalculation.rowCalculationForPurcInvoice(index, getTableView(), callback);
                    event.consume();
                } else if (event.getCode() == KeyCode.DOWN) {
                    getTableView().getItems().addAll(new PurchaseInvoiceTable("", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", ""));
                    TableColumn<PurchaseInvoiceTable, ?> colName = getTableView().getColumns().get(1);
                    getTableView().edit(getIndex() + 1, colName);
                }
            });
        }
    }

    private void rateColumn() {                //function for calculation when we enter the qty in cmpt row
        if ("tcRate".equals(columnName)) {
            textField.setEditable(true);

//            textField.setOnKeyPressed(event -> {
//                if (event.getCode() == KeyCode.ENTER || event.getCode() == KeyCode.TAB) {
//                    Integer index = getIndex();
//                    System.out.println("Rada INDEX" + index);
//                    SalesChallanToInvoiceCalculation.rowCalculationForPurcInvoice(index, getTableView(), callback);
//                    SalesChallanToInvoiceCalculation.calculateGst(getTableView(), callback);
//                }
//            });

            textField.setOnKeyPressed(event -> {
                if (!textField.getText().isEmpty()) {
                    CommonValidationsUtils.restrictToDecimalNumbers(textField);
                }
                if (event.isShiftDown() && event.getCode() == KeyCode.TAB) {
                    TableColumn<PurchaseInvoiceTable, ?> colName = getTableView().getColumns().get(8);
                    getTableView().edit(getIndex(), colName);
//                    TableColumn<PurchaseInvoiceTable, ?> colName = getTableView().getColumns().get(8);
//                    getTableView().edit(getIndex(), colName);

                } else if (event.getCode() == KeyCode.ENTER || !event.isShiftDown() && event.getCode() == KeyCode.TAB) {
                    Integer index = getIndex();
                    if (!textField.getText().isEmpty()) {

                        TableColumn<PurchaseInvoiceTable, ?> colName = getTableView().getColumns().get(12);
                        getTableView().edit(index, colName);
                        SalesChallanToInvoiceCalculation.rowCalculationForPurcInvoice(index, getTableView(), callback);
                        SalesChallanToInvoiceCalculation.calculateGst(getTableView(), callback);
                    } else {
                        textField.requestFocus();
                    }
                    SalesChallanToInvoiceCalculation.rowCalculationForPurcInvoice(index, getTableView(), callback);
                } else if (event.getCode() == KeyCode.DOWN) {
                    getTableView().getItems().addAll(new PurchaseInvoiceTable("", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", ""));
                    TableColumn<PurchaseInvoiceTable, ?> colName = getTableView().getColumns().get(1);
                    getTableView().edit(getIndex() + 1, colName);
                }
            });
        }
    }

    private void discountPercColumn() {                //function for calculation when we enter the qty in cmpt row
        if ("tcDisPer".equals(columnName)) {
            textField.setEditable(true);

            textField.setOnKeyPressed(event -> {
                if (!textField.getText().isEmpty()) {
                    CommonValidationsUtils.restrictToDecimalNumbers(textField);
                }
                if (event.isShiftDown() && event.getCode() == KeyCode.TAB) {
                    TableColumn<PurchaseInvoiceTable, ?> colName = getTableView().getColumns().get(10);
                    getTableView().edit(getIndex(), colName);
//                    TableColumn<PurchaseInvoiceTable, ?> colName = getTableView().getColumns().get(10);
//                    getTableView().edit(getIndex(), colName);

                } else if (event.getCode() == KeyCode.ENTER || !event.isShiftDown() && event.getCode() == KeyCode.TAB) {
                    Integer index = getIndex();
                    TableColumn<PurchaseInvoiceTable, ?> colName = getTableView().getColumns().get(13);
                    getTableView().edit(index, colName);
                    System.out.println("Rada INDEX" + index);
                    SalesChallanToInvoiceCalculation.rowCalculationForPurcInvoice(index, getTableView(), callback);
                    SalesChallanToInvoiceCalculation.calculateGst(getTableView(), callback);
                } else if (event.getCode() == KeyCode.DOWN) {
                    getTableView().getItems().addAll(new PurchaseInvoiceTable("", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", ""));
                    TableColumn<PurchaseInvoiceTable, ?> colName = getTableView().getColumns().get(1);
                    getTableView().edit(getIndex() + 1, colName);
                }
            });
        }
    }

//    private void discountPerc2Column() {                //function for calculation when we enter the qty in cmpt row
//        if ("tblcSCConvEditCmptDisPer2".equals(columnName)) {
//            textField.setEditable(true);
//
//            textField.setOnKeyPressed(event -> {
//                if (event.getCode() == KeyCode.ENTER || event.getCode() == KeyCode.TAB) {
//                    Integer index = getIndex();
//                    System.out.println("Rada INDEX" + index);
//                    SalesChallanToInvoiceCalculation.rowCalculationForPurcInvoice(index, getTableView(), callback);
//                    SalesChallanToInvoiceCalculation.calculateGst(getTableView(), callback);
//                }
//            });
//        }
//    }

    private void disAmtColumn() {                //function for calculation when we enter the qty in cmpt row
        if ("tcDisAmt".equals(columnName)) {
            textField.setEditable(true);

            textField.setOnKeyPressed(event -> {
                if (!textField.getText().isEmpty()) {
                    CommonValidationsUtils.restrictToDecimalNumbers(textField);
                }
                if (event.isShiftDown() && event.getCode() == KeyCode.TAB) {
                    TableColumn<PurchaseInvoiceTable, ?> colName = getTableView().getColumns().get(12);
                    getTableView().edit(getIndex(), colName);
                } else if (event.getCode() == KeyCode.ENTER || !event.isShiftDown() && event.getCode() == KeyCode.TAB) {
                    Integer index = getIndex();
                    System.out.println("Rada INDEX" + index);
                    int current_index = getTableRow().getIndex();
                    //new start
                  /*  if (getTableRow().getItem().getSr_no() != null) {
                        int serial_no = Integer.parseInt(getTableRow().getItem().getSr_no());
                        getTableView().getItems().addAll(new PurchaseInvoiceTable("", "", String.valueOf(serial_no + 1), "", "", "", "", "", "", "", "", "", "", "", "", "", "", ""));
                    }*/
                    if (index >= getTableView().getItems().size() - 1) {
                        getTableView().getItems().addAll(new PurchaseInvoiceTable("", "", "" + (getIndex() + 2), "", "", "", "", "", "", "", "", "", "", "", "", "", "", ""));
                    }
                    TableColumn<PurchaseInvoiceTable, ?> colName = getTableView().getColumns().get(1);
                    getTableView().edit(getIndex() + 1, colName);
                    SalesChallanToInvoiceCalculation.rowCalculationForPurcInvoice(index, getTableView(), callback);
                    SalesChallanToInvoiceCalculation.calculateGst(getTableView(), callback);
                    //new2.0 end
                }
            });
        }
    }

    //new2.0 start
    private void freeQtyColumn() {
        if ("tcFreeQty".equals(columnName)) {
            textField.setEditable(true);
            textField.setOnKeyPressed(event -> {
                if (!textField.getText().isEmpty()) {
                    CommonValidationsUtils.restrictToNumbers(textField);
                }
                if (event.isShiftDown() && event.getCode() == KeyCode.TAB) {
                    TableColumn<PurchaseInvoiceTable, ?> colName = getTableView().getColumns().get(8);
                    getTableView().edit(getIndex(), colName);
                } else if (event.getCode() == KeyCode.ENTER || !event.isShiftDown() && event.getCode() == KeyCode.TAB) {
                    int index = getIndex();
                    TableColumn<PurchaseInvoiceTable, ?> colName = getTableView().getColumns().get(10);
                    getTableView().edit(getIndex(), colName);
                    SalesChallanToInvoiceCalculation.rowCalculationForPurcInvoice(index, getTableView(), callback);
                }
            });
           /* TableColumn<PurchaseOrderTable, ?> colName = getTableView().getColumns().get(8);
            getTableView().edit(getIndex(), colName);*/

        }
    }

    private void netAmountColumn() {
        if ("tcNetAmount".equals(columnName)) {
            textField.setEditable(false);
            textField.setOnKeyPressed(event -> {
                if (event.getCode() == KeyCode.ENTER) {
                    int current_index = getTableRow().getIndex();

//                    int serial_no = Integer.parseInt(getTableRow().getItem().getSr_no());
//                    getTableView().getItems().addAll(new PurchaseInvoiceTable("", String.valueOf(current_index + 1), String.valueOf(serial_no + 1), "", "", "", "", "", "", "", "", "", "", "", "", "", "", ""));
                    //new start
                    if (getTableRow().getItem().getSr_no() != null) {
                        int serial_no = Integer.parseInt(getTableRow().getItem().getSr_no());
                        getTableView().getItems().addAll(new PurchaseInvoiceTable("", String.valueOf(current_index + 1), String.valueOf(serial_no + 1), "", "", "", "", "", "", "", "", "", "", "", "", "", "", ""));
                    }
                    //new end
                }
            });
        }
    }

    //new start
    private void openProduct(int currentIndex) {
        PurchaseInvoiceTable selectedRow = getTableView().getItems().get(currentIndex);

        TranxCommonPopUps.openProductPopUp(Communicator.stage, Integer.valueOf(selectedRow.getProduct_id()), "Product", input -> {
//            Integer selectedIndex = tblTranxSalesInvoiceCreateCMPTRow.getSelectionModel().getSelectedIndex();
            System.out.println("input" + input);
            if (input != null) {
                getTableRow().getItem().setParticulars(input.getProductName());
                getTableRow().getItem().setProduct_id(input.getProductId().toString());
                getTableRow().getItem().setPackages(input.getPackageName());
                getTableRow().getItem().setIs_batch(input.getBatch().toString());
                getTableRow().getItem().setTax(input.getIgst().toString());
//                getTableRow().getItem().setUnit(input.get());
                getTableRow().getItem().setSelectedProduct(input);
                getTableRow().getItem().setRate(input.getSalesRate().toString());
                List<LevelAForPurInvoice> levelAForPurInvoiceList = ProductUnitsPackingSalesChallToInvoi.getAllProductUnitsPackingFlavour(input.getProductId().toString());
                System.out.println("levelAForPurInvoiceList" + levelAForPurInvoiceList);

                //                int index = getTableRow().getIndex();
                ObservableList<LevelAForPurInvoice> observableLevelAList = FXCollections.observableArrayList(levelAForPurInvoiceList);
                System.out.println("observableLevelAList1234" + observableLevelAList);
                if (currentIndex >= 0 && currentIndex < PurInvoiceCommunicator.levelAForPurInvoiceObservableList.size()) {
                    PurInvoiceCommunicator.levelAForPurInvoiceObservableList.set(currentIndex, observableLevelAList);
//                        getTableRow().getItem().setLevelA(null);
                    getTableRow().getItem().setLevelA(levelAForPurInvoiceList.get(0) != null ? levelAForPurInvoiceList.get(0).getLabel() : "");

                } else {
                    PurInvoiceCommunicator.levelAForPurInvoiceObservableList.add(observableLevelAList);
                    getTableRow().getItem().setLevelA(levelAForPurInvoiceList.get(0) != null ? levelAForPurInvoiceList.get(0).getLabel() : "");
                }

                String product_id = input.getProductId().toString();
                System.out.println("OpenProduct ProductId " + product_id);
                if (productID_callback != null) {
                    System.out.println("open Product product Id passed");
                    Object[] object = new Object[1];
                    object[0] = product_id;
                    productID_callback.call(object);
                }
            }
        }, addProductCallback -> {
            //TODO: add Product redirection work
        });

    }
    //new end

    public void openBatchWindow() {
        PurchaseInvoiceTable selectedRow = getTableView().getItems().get(getIndex());
        if (Boolean.valueOf(selectedRow.getIs_batch()) == true) {
            TranxCommonPopUps.openBatchPopUpSales(Communicator.stage, selectedRow, Communicator.tranxDate, "Batch", input -> {
                if (input != null) {
                    selectedRow.setSelectedBatch(input);
                    selectedRow.setB_details_id(String.valueOf(input.getId()));
                    selectedRow.setB_no(input.getBatchNo());
                    selectedRow.setBatch_or_serial(input.getBatchNo());
//                    selectedRow.setSelectedBatch(input);
//                    selectedRow.setB_details_id("" + input.getId());
//                    selectedRow.setB_no(input.getBatchNo());
//                    selectedRow.setBatch_or_serial(input.getBatchNo());
//                                    selectedRow.setRate(input.minRateA);
                    System.out.println("batchNo " + input.getBatchNo() + "b_detailsId " + input.getId());
                    if (productID_callback != null) {
                        Object[] object = new Object[2];
                        object[0] = input.getBatchNo().toString();
                        object[1] = String.valueOf(input.getId());
                        productID_callback.call(object);
                    }
                }
                getTableView().getItems().set(getIndex(), selectedRow);
//                getBatchDetailsByBatchNoAndId(input.getBatchNo(), String.valueOf(input.getId()));
            }, isNewBatch -> {
            //TODO: is Negetive case add product batch
        });


        } else {
            selectedRow.setBatch_or_serial("#");
            selectedRow.setB_no("#");
            getTableView().getItems().set(getIndex(), selectedRow);
        }
        TableColumn<PurchaseInvoiceTable, ?> colName = getTableView().getColumns().get(7);

        getTableView().edit(getIndex(), colName);

    }

/*    public void openBatchWindow() {

        int current_index = getTableRow().getIndex();
        List<PurchaseInvoiceTable> tableList = getTableView().getItems();

        List<BatchWindowTableDTO> batchWindowTableDTOList;
        PurchaseInvoiceTable current_table_row = tableList.get(current_index);
        batchWindowTableDTOList = current_table_row.getBatchWindowTableDTOList();

        Map<String, String> product_details_to_batch_window = new HashMap<>();
        product_details_to_batch_window.put("product_id", current_table_row.getProduct_id());
        product_details_to_batch_window.put("product_name", current_table_row.getParticulars());
        product_details_to_batch_window.put("levelA_id", current_table_row.getLevelA_id());
        product_details_to_batch_window.put("levelB_id", current_table_row.getLevelB_id());
        product_details_to_batch_window.put("levelC_id", current_table_row.getLevelC_id());
        product_details_to_batch_window.put("unit_id", current_table_row.getUnit_id());
        product_details_to_batch_window.put("tax", current_table_row.getTax());
        product_details_to_batch_window.put("ledger_id", getTableView().getItems().get(0).getLedger_id());

        */

    /**** get the PTR & CSR Rates using current row index from table ****//*
        List<UnitRateList> rateList = current_table_row.getRateList();
        for (UnitRateList unitRateList : rateList) {
            if (unitRateList.getUnitId() == Long.parseLong(current_table_row.getUnit_id())) {
                product_details_to_batch_window.put("fsrmh", "" + unitRateList.getFsrmh());
                product_details_to_batch_window.put("fsrai", "" + unitRateList.getFsrai());
                product_details_to_batch_window.put("csrmh", "" + unitRateList.getCsrmh());
                product_details_to_batch_window.put("csrai", "" + unitRateList.getCsrai());

            }
        }

        Long ledgerid = Long.parseLong(getTableView().getItems().get(0).getLedger_id());

        if (current_table_row.getIs_batch().equals("true") && ledgerid != 0) {
            BatchWindow.batchWindow(String.valueOf(current_index), product_details_to_batch_window, batchWindowTableDTOList, input -> {

                getTableView().getItems().get(Integer.parseInt(input.get(0).getCurrentIndex())).setBatchWindowTableDTOList(input);

//                System.out.println("table batch: "+getTableView().getItems().get(Integer.parseInt(input.get(0).getCurrentIndex())).getBatchWindowTableDTOList());
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


                    getTableView().getItems().get(Integer.parseInt(input.get(0).getCurrentIndex())).setB_expiry(String.valueOf(Communicator.text_to_date.fromString(batchWindowTableDTOSs.getExpiryDate())));

                    getTableView().getItems().get(Integer.parseInt(input.get(0).getCurrentIndex())).setRate_a(batchWindowTableDTOSs.getFsr_mh().isEmpty() ? String.valueOf(0.0) : batchWindowTableDTOSs.getFsr_mh());
                    getTableView().getItems().get(Integer.parseInt(input.get(0).getCurrentIndex())).setRate_b(batchWindowTableDTOSs.getFsr_ai().isEmpty() ? String.valueOf(0.0) : batchWindowTableDTOSs.getFsr_ai());
                    getTableView().getItems().get(Integer.parseInt(input.get(0).getCurrentIndex())).setRate_c(batchWindowTableDTOSs.getCsr_mh().isEmpty() ? String.valueOf(0.0) : batchWindowTableDTOSs.getCsr_mh());
                    getTableView().getItems().get(Integer.parseInt(input.get(0).getCurrentIndex())).setRate_d(batchWindowTableDTOSs.getCsr_ai().isEmpty() ? String.valueOf(0.0) : batchWindowTableDTOSs.getCsr_ai());

                    getTableView().getItems().get(Integer.parseInt(input.get(0).getCurrentIndex())).setMin_margin(batchWindowTableDTOSs.getMargin());

                    getTableView().getItems().get(Integer.parseInt(input.get(0).getCurrentIndex())).setManufacturing_date(String.valueOf(Communicator.text_to_date.fromString(batchWindowTableDTOSs.getManufacturingDate())));

                }
                String batchNo = getTableView().getItems().get(Integer.parseInt(input.get(0).getCurrentIndex())).getBatch_or_serial();
                String bDetailsId = getTableView().getItems().get(Integer.parseInt(input.get(0).getCurrentIndex())).getB_details_id();
                if (productID_callback != null) {
                    Object[] object = new Object[2];
                    object[0] = batchNo;
                    object[1] = bDetailsId;
                    productID_callback.call(object);
                }
                PurchaseInvoiceCalculation.rowCalculationForPurcInvoice(Integer.parseInt(input.get(0).getCurrentIndex()), getTableView(), callback);
                PurchaseInvoiceCalculation.calculateGst(getTableView(), callback);
                getTableView().getItems().addAll(new PurchaseInvoiceTable("", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", ""));

                TableColumn<PurchaseInvoiceTable, ?> colName = getTableView().getColumns().get(1);
                System.out.println("Col Name:" + colName.getText() + "Index:" + getIndex());
                getTableView().edit(getIndex() + 1, colName);
            });
        }
    }*/
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
            ((PurchaseInvoiceTable) item).setParticulars(newValue.isEmpty() ? "" : newValue);
        } else if (columnName.equals("tcQuantity")) {
            (getTableRow().getItem()).setQuantity(newValue.isEmpty() ? "0" : newValue);
        } else if (columnName.equals("tfFreeQty")) {//new2.0
            (getTableRow().getItem()).setFree(newValue.isEmpty() ? "0.0" : newValue);//new2.0
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
        SalesChallanToInvoiceCalculation.rowCalculationForPurcInvoice(getIndex(), getTableView(), callback);
        SalesChallanToInvoiceCalculation.calculateGst(getTableView(), callback);
    }
}

class ComboBoxTableCellForLevelASalesChallToInvoi extends TableCell<PurchaseInvoiceTable, String> {

    String columnName;
    private final ComboBox<String> comboBox;
    ObservableList<LevelAForPurInvoice> comboList = null;

    public ComboBoxTableCellForLevelASalesChallToInvoi(String columnName) {

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

class ComboBoxTableCellForLevelBSalesChallToInvoi extends TableCell<PurchaseInvoiceTable, String> {

    String columnName;
    private final ComboBox<String> comboBox;
    ObservableList<LevelBForPurInvoice> comboList = null;

    public ComboBoxTableCellForLevelBSalesChallToInvoi(String columnName) {
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

class ComboBoxTableCellForLevelCSalesChallToInvoi extends TableCell<PurchaseInvoiceTable, String> {

    String columnName;
    private final ComboBox<String> comboBox;
    ObservableList<LevelAForPurInvoice> comboList = null;

    public ComboBoxTableCellForLevelCSalesChallToInvoi(String columnName) {


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

class ComboBoxTableCellForUnitSalesChallToInvoi extends TableCell<PurchaseInvoiceTable, String> {

    String columnName;
    TableCellCallback<Integer> unit_callback;//new
    private final ComboBox<String> comboBox;

    public ComboBoxTableCellForUnitSalesChallToInvoi(String columnName, TableCellCallback<Integer> unit_callback) {


        this.columnName = columnName;
        this.comboBox = new ComboBox<>();
        this.unit_callback = unit_callback;//new

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
//       AutoCompleteBox autoCompleteBox1 = new AutoCompleteBox<>(this.comboBox, -1);

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
        //new2.0 start
        comboBox.setOnKeyPressed(event -> {
            if (event.isShiftDown() && event.getCode() == KeyCode.TAB) {
                TableColumn<PurchaseInvoiceTable, ?> colName = getTableView().getColumns().get(6);
                getTableView().edit(getIndex(), colName);
            }

            if (event.getCode() == KeyCode.ENTER || !event.isShiftDown() && event.getCode() == KeyCode.TAB) {
                TableColumn<PurchaseInvoiceTable, ?> colName = getTableView().getColumns().get(8);
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

class ButtonTableCellSalesChallToInvoi extends TableCell<PurchaseInvoiceTable, String> {
    private Button delete;

    public ButtonTableCellSalesChallToInvoi() {


        this.delete = createButtonWithImage();


        delete.setOnAction(actionEvent -> {
            PurchaseInvoiceTable table = getTableView().getItems().get(getIndex());
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

class ProductUnitsPackingSalesChallToInvoi {

    public static List<LevelAForPurInvoice> getAllProductUnitsPackingFlavour(String product_id) {


        Map<String, String> map = new HashMap<>();
        map.put("product_id", product_id);

        List<LevelAForPurInvoice> levelAForPurInvoicesList = new ArrayList<>();
        try {
            String formdata = Globals.mapToStringforFormData(map);

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

class SalesChallanToInvoiceCalculation {

    public static void rowCalculationForPurcInvoice(int rowIndex, TableView<PurchaseInvoiceTable> tableView, TableCellCallback<Object[]> callback) {
        PurchaseInvoiceTable purchaseInvoiceTable = tableView.getItems().get(rowIndex);
//        String purchaseInvoiceTableData = purchaseInvoiceTable.getRate();
//        System.out.println("In Row Calculation CMPTDATA=>" + purchaseInvoiceTableData);
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
        if (!purchaseInvoiceTable.getQuantity().isEmpty() && !purchaseInvoiceTable.getRate().isEmpty()) {
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
            selectedItem.setTotal_taxable_amt(GlobalTranx.setDecimalFromStr(taxable_amt));

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

        }

    }

    public static void calculateGst(TableView<PurchaseInvoiceTable> tableView, TableCellCallback<Object[]> callback) {


        Map<Double, Double> cgstTotals = new HashMap<>(); //used to merge same cgst Percentage
        Map<Double, Double> sgstTotals = new HashMap<>();//used to merge same sgst Percentage
        Map<Double, Double> igstTotals = new HashMap<>();   //igst issue
        // Initialize totals to zero for each tax percentage
        for (Double taxPercentage : new Double[]{0.0, 3.0, 5.0, 12.0, 18.0, 28.0}) {
            cgstTotals.put(taxPercentage, 0.0);
            sgstTotals.put(taxPercentage, 0.0);
            igstTotals.put(taxPercentage, 0.0);  //igst issue
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
            if (!purchaseInvoiceTable.getQuantity().isEmpty() && !purchaseInvoiceTable.getRate().isEmpty()) {
                taxPercentage = Double.parseDouble(purchaseInvoiceTable.getTax());
                Double quantity = Double.parseDouble(purchaseInvoiceTable.getQuantity());
                System.out.println("KK free QTY ==> " + purchaseInvoiceTable.getFree());
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
            double totalIGST = igstTotals.get(taxPer);   //igst issue

            if (totalCGST > 0) {
//                GstDTO gstDto :
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
//            rowCalculationForPurcInvoice(i, tableView, callback);//call row calculation function
            PurchaseInvoiceTable purchaseInvoiceTable = tableView.getItems().get(i);
            totalTaxableAmt = totalTaxableAmt + Double.parseDouble(purchaseInvoiceTable.getTaxable_amt());
        }
        System.out.println("totalTaxableAmt discountPropotionalCalculation :: " + totalTaxableAmt);

        Double rowDisc = 0.0;
        //get data of netamount, tax per,and row taxable amount from cmptrow
        for (PurchaseInvoiceTable purchaseInvoiceTable : tableView.getItems()) {
            System.out.println("getFinal_dis_amt--->" + purchaseInvoiceTable.getFinal_dis_amt());
            rowDisc = purchaseInvoiceTable.getFinal_dis_amt().isEmpty() ? 0.0 : Double.parseDouble(purchaseInvoiceTable.getFinal_dis_amt());
            System.out.println("netAmt--->" + purchaseInvoiceTable.getNet_amount());
            netAmt = purchaseInvoiceTable.getNet_amount().isEmpty() ? 0.0 : Double.parseDouble(purchaseInvoiceTable.getNet_amount());
            System.out.println("netAmt--->" + purchaseInvoiceTable.getTax());
            r_tax_per = !purchaseInvoiceTable.getTax().isEmpty() ? Double.parseDouble(purchaseInvoiceTable.getTax()) : 0.0;
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
//        calculateGst(tableView, callback);
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
