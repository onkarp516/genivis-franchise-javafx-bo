

package com.opethic.genivis.controller.tranx_purchase;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.opethic.genivis.GenivisApplication;
import com.opethic.genivis.controller.account_entry.PaymentCreateController;
import com.opethic.genivis.controller.dialogs.AdditionalCharges;
import com.opethic.genivis.controller.dialogs.BatchWindow;
import com.opethic.genivis.controller.dialogs.SingleInputDialogs;
import com.opethic.genivis.controller.master.ledger.common.LedgerMessageConsts;
import com.opethic.genivis.controller.tranx_sales.common.TranxCommonPopUps;
import com.opethic.genivis.dto.*;
import com.opethic.genivis.dto.add_charges.CustomAddChargesDTO;
import com.opethic.genivis.dto.cmp_t_row.BatchWindowTableDTO;
import com.opethic.genivis.dto.pur_invoice.LevelAForPurInvoice;
import com.opethic.genivis.dto.pur_invoice.PurInvoiceCommunicator;
import com.opethic.genivis.dto.pur_invoice.PurchaseInvoiceTable;
import com.opethic.genivis.dto.pur_invoice.reqres.AddChargesListDTO;
import com.opethic.genivis.dto.pur_invoice.reqres.InvoiceDataResDTO;
import com.opethic.genivis.dto.pur_invoice.reqres.RowListDTO;
import com.opethic.genivis.dto.reqres.creditNote.CreditNoteLastRecordResponse;
import com.opethic.genivis.dto.reqres.product.Communicator;
import com.opethic.genivis.dto.reqres.product.TableCellCallback;
import com.opethic.genivis.dto.reqres.pur_tranx.*;
import com.opethic.genivis.models.tranx.sales.*;
import com.opethic.genivis.network.APIClient;
import com.opethic.genivis.network.EndPoints;
import com.opethic.genivis.network.RequestType;
import com.opethic.genivis.utils.*;
import javafx.animation.KeyFrame;
import javafx.animation.PauseTransition;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.Duration;
import javafx.util.StringConverter;
import org.apache.http.util.TextUtils;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sound.midi.Soundbank;
import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URL;
import java.net.http.HttpResponse;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.function.DoubleUnaryOperator;

import static com.opethic.genivis.utils.FxmFileConstants.*;
import static com.opethic.genivis.utils.Globals.decimalFormat;
import static com.opethic.genivis.utils.Globals.mapToStringforFormData;
import static javafx.scene.control.PopupControl.USE_COMPUTED_SIZE;
import static javafx.scene.control.PopupControl.USE_PREF_SIZE;


public class PurOrderToChallanCreateController implements Initializable {
    @FXML
    private TextField tfPurOrdToChallLedgerName, tfPurOrdToChallChallanNo, tfPurOrdToChallPurSerialNo;
    @FXML
    private Button btnPurOrdToChallChooseFileButton;
    @FXML
    private HBox po2cTopFirstRow, po2cTopSecondRow, po2cBottomMain;
    @FXML
    private VBox po2cBottomFirstV, po2cBottomSecondV, po2cBottomThirdV, po2cTotalMainDiv, po2cTotalMainInnerDiv;
    @FXML
    private TableView<PurchaseInvoiceTable> tblvPurOrdToChallCmpTRow;
    @FXML
    private BorderPane bpPurOrdToChallRootPane;
    @FXML
    private TableColumn<PurchaseInvoiceTable, String> tblcPurOrdToChallCmpTRowSrNo, tblcPurOrdToChallCmpTRowParticulars, tblcPurOrdToChallCmpTRowPacking, tblcPurOrdToChallCmpTRowUnit, tblcPurOrdToChallCmpTRowBatchNo, tblcPurOrdToChallCmpTRowQuantity, tblcPurOrdToChallCmpTRowRate,
            tblcPurOrdToChallCmpTRowGrossAmt, tblcPurOrdToChallCmpTRowDisPer, tblcPurOrdToChallCmpTRowDisAmt, tblcPurOrdToChallCmpTRowTaxPer, tblcPurOrdToChallCmpTRowNetAmt, tblcPurOrdToChallCmpTRowLevelA, tblcPurOrdToChallCmpTRowLevelB,
            tblcPurOrdToChallCmpTRowLevelC, tblcPurOrdToChallCmpTRowFreeQty, tblcPurOrdToChallCmpTRowAction;
    private ObservableList<CmpTRowDTO> itemList;
    private ToggleGroup toggleGroup = new ToggleGroup();


    @FXML
    private ComboBox<CommonDTO> cmbPurOrdToChallSupplierGST;

    private int selectedRowIndex;
    private String SelectedLedgerId;

    private Integer purChallEditId = -1;

    @FXML
    private TableView<GstDTO> tblvPurOrdToChallGST;
    @FXML
    private TableColumn<GstDTO, String> tblcPurOrdToChallGST, tblcPurOrdToChallCGST, tblcPurOrdToChallSGST;
    @FXML
    private Label lblPurOrdToChallRoundOff, lblPurOrdToChallFreeQty, lblPurOrdToChallTotalQty, lblPurOrdToChallTotalBillAmount, lblPurOrdToChallGrossTotal, lblPurOrdToChallTotalTax,
            lblPurOrdToChallTotalTaxableAmount, lblPurOrdToChallTotalDiscount;
    @FXML
    private CheckBox chbPurOrdToChallRoundOff;

    private String productId = "";
    private String responseBody;
    private static ObservableList<String> unitList = FXCollections.observableArrayList();
    private ComboBox<CommonDTO> cmbPackingText;

    private String prdBatchTaxPer;
    private String selectedRowPrdId;
    private String gstNumber;

    private Double costValue, costWithTaxValue, taxAmt, totalCgstTax = 0.0, totalSgstTax = 0.0;
    private static int rowIndexParticular;
    private static final Logger purChallanLogger = LoggerFactory.getLogger(PurchaseChallanController.class);
    private List<TranxPurRowDTO> tranxPurRowDTOS = new ArrayList<>();

    @FXML
    private Button btnPurOrdToChallFinalSubmit, btnPurOrdToChallFinalCancel, btnPurOrdToChallFinalModify, btnPurOrdTOChallanAddCharges;
    DecimalFormat decimalFormat = new DecimalFormat("0.#");

    @FXML
    private TabPane tranxPurOrdToChallanTabPane;
    @FXML
    private Tab tabPurOrdToChallanLedger, tabPurOrdToChallanProduct, tabPurOrdToChallanBatch;

    @FXML
    private TextField tfPurOrdToChallTranxDate, tfPurOrdToChallPurOrdToChallanDate, tfPurOrdToChallNarrtion;
    @FXML
    private ComboBox<CommonDTO> cmbPurOrdToChallPurchaAc;


    @FXML
    private Text txtPurOrdToChallanGstNo, txtPurOrdToChallanArea, txtPurOrdToChallanBank, txtPurOrdToChallanContactPerson, txtPurOrdToChallanTransportName, txtPurOrdToChallanCreditDays, txtPurOrdToChallanFssai,
            txtPurOrdToChallanLicenseNo, txtPurOrdToChallanRoute, txtPurOrdToChallProductBrand, txtPurOrdToChallProductGroup, txtPurOrdToChallProductSubGroup, txtPurOrdToChallProductCategory, txtPurOrdToChallProductHsn,
            txtPurOrdToChallProductTaxType, txtPurOrdToChallProductTaxPer, txtPurOrdToChallProductMarginPer, txtPurOrdToChallProductCost, txtPurOrdToChallProductShelfId, txtPurOrdToChallProductMinStock, txtPurOrdToChallProductMaxStock,
            txtPurOrdToChallBatchBatch, txtPurOrdToChallBatchMRP, txtPurOrdToChallBatchFSRMH, txtPurOrdToChallBatchSaleRate, txtPurOrdToChallBatchHMfgDt, txtPurOrdToChallBatchPurRate, txtPurOrdToChallBatchFSRAI,
            txtPurOrdToChallBatchExpDt, txtPurOrdToChallBatchDisPer, txtPurOrdToChallBatchCSRMH, txtPurOrdToChallBatchAvlStk, txtPurOrdToChallBatchDisAmt, txtPurOrdToChallBatchCSRAI;

    Long stateCode = 0L, CompanyStateCode = 0L;

    // Array to hold focusable nodes
    private Node[] focusableNodes;

    ArrayList<Double> netAmountList;

    private JsonObject jsonObject1 = null;
    private JsonObject jsonObject = null;

    @FXML
    private TableView<SalesOrderSupplierDetailsDTO> tvPurOrdToChallanSupplierDetails;
    @FXML
    private TableColumn<SalesOrderSupplierDetailsDTO, String> tcPCInvPrdHistSupplierName, tcPCInvPrdHistInvNo,
            tcPCInvPrdHistInvDate, tcPCInvPrdHistBatch, tcPCInvPrdHistMRP, tcPCInvPrdHistQty,
            tcPCInvPrdHistRate, tcPCInvPrdHistCost, tcPCInvPrdHistDisPer, tcPCInvPrdHistDisAmt;
    String message = "";

    private String ledgerName;
    private String ledgerId;

    private String src = "create";

    String purchaseAcID = "";

    String supplierGSTIN_ID = "";
    String gst_no = "";

    String ledger_id = "";
    private String totalamt = "";
    private String total_purchase_discount_amt = "";
    private Boolean taxFlag = false;

    private String ledgerStateCode = "";

    private Double total_taxable_amt = 0.0;
    Map<String, String> purchase_challan_map = new HashMap<>();

    private ObservableList<CommonDTO> supplierGSTINList = FXCollections.observableArrayList();

    Map<String, Object> edit_response_map = new HashMap<>();

    Map<String, String> invoice_data_map = new HashMap<>();

    List<PurOrdToChallRowDTO> rowListDTOS = new ArrayList<>();


    @FXML

    private TextField tfPurOrdToChallAllRowDisPer, tfPurOrdToChallAllRowDisAmt, tfPurOrdToChallanAddChgAmt, tfPurOrdToChallChooseFileText;
    @FXML
    private Label lblPurOrdToChallTotalSGST, lblPurOrdToChallTotalCGST;

    List<CustomAddChargesDTO> listEdit = new ArrayList<>();
    private PurchaseOrderDTO purchaseOrderDTO;
    List<CustomAddChargesDTO> addChargesDTOList = new ArrayList<>();
    private final List<AdditionalCharge> addChargesResLst = new ArrayList<>();

    public static String purOrdToChallId = "";
    private String purchase_id = "";


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        resposiveScreenCss();
        tblvPurOrdToChallCmpTRow.setFocusTraversable(false);
        tvPurOrdToChallanSupplierDetails.setFocusTraversable(false);
        tblvPurOrdToChallGST.setFocusTraversable(false);

        // GST Table Design and Data Set
        gstTableDesign();

        // autofocus on TranxDate
        Platform.runLater(() -> tfPurOrdToChallLedgerName.requestFocus());

        //Ledger Name Validations
        tfLedgerNameValidation();

        //challan Number Duplicate Check
        tfChallanNoValidatition();

        // Enter Functionality Method
        initialEnterMethod();

        sceneInitilization();

        showDropDownOnKeyPress(cmbPurOrdToChallSupplierGST);

        if (!purOrdToChallId.isEmpty() && purOrdToChallId != null) {
            System.out.println("purchase Id >> : " + purchase_id);
            purchase_id = purOrdToChallId;
            purOrdToChallId = "";
            getPurOrderToPurChallById(purchase_id);
        }

        // Table Initializtion for Create & Edit
        tableInitiliazation();

        tfPurOrdToChallLedgerName.setOnMouseClicked(event -> {
            handleTfLedgerName();
        });

        tfPurOrdToChallLedgerName.setOnKeyTyped(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                handleTfLedgerName();
            }
        });

        btnPurOrdTOChallanAddCharges.setOnAction(e -> {
            onClickAdditionalCharges();
        });

        // Date Format In TextField Code
        // Set the current date to the TranxDate and ChallanDate
        DateValidator.applyDateFormat(tfPurOrdToChallTranxDate);
        DateValidator.applyDateFormat(tfPurOrdToChallPurOrdToChallanDate);
        dateValidation();

        // Auto Set of Purchase Serial Number
        getPurOrdToChallSrNumber();


        // By Ledger Id Setting the Ledger Name And Gst Number
        if (!purchase_id.isEmpty() && purchase_id != null) {
            tfPurOrdToChallLedgerName.setText(ledgersById(Long.valueOf(invoice_data_map.get("supplierId"))));
            if (supplierGSTINList != null && !supplierGSTINList.isEmpty()) {
                cmbPurOrdToChallSupplierGST.setItems(supplierGSTINList);
                cmbPurOrdToChallSupplierGST.setValue(supplierGSTINList.get(0));
            }
        }

        //Purchase Accounts Set In ComboBox
        getPurchaseAccounts();

        // To call PurOrdToChallan Create & Update API and To Redirect the PurOrdToChallan List Page
        createUpdateCall();

        // Responsive of Common Product Row table
        responsiveCmpTable();

        // RoundOff Calculations Method
        chbPurOrdToChallRoundOff.setOnAction(e -> {
            roundOffCalculations();
        });
        tfInvoiceDateValidation();

        tfPurOrdToChallanAddChgAmt.setEditable(false);
        tfPurOrdToChallanAddChgAmt.setFocusTraversable(false);
    }

    private void resposiveScreenCss() {
        // For Responsive Resolutions Css Style Apply
        double height = Screen.getPrimary().getBounds().getHeight();
        double width = Screen.getPrimary().getBounds().getWidth();
        System.out.println("width" + width);
        if (width >= 992 && width <= 1024) {
            po2cTopFirstRow.setSpacing(10);
            po2cTopSecondRow.setSpacing(10);
            po2cTotalMainDiv.setSpacing(10);
            po2cTotalMainInnerDiv.setSpacing(8);
            // Set preferred widths as percentages of the HBox width
            po2cBottomFirstV.prefWidthProperty().bind(po2cBottomMain.widthProperty().multiply(0.54));
            po2cBottomSecondV.prefWidthProperty().bind(po2cBottomMain.widthProperty().multiply(0.24));
            po2cBottomThirdV.prefWidthProperty().bind(po2cBottomMain.widthProperty().multiply(0.22));
//            so2cBottomMain.setPrefHeight(150);
            tblvPurOrdToChallGST.getStylesheets().add(GenivisApplication.class.getResource("ui/css/purchase_challan_css/gst_table_css/gst_table_992_1024.css").toExternalForm());
            tvPurOrdToChallanSupplierDetails.getStylesheets().add(GenivisApplication.class.getResource("ui/css/purchase_challan_css/supplier_table_css/supplier_table_992_1024.css").toExternalForm());
            bpPurOrdToChallRootPane.getStylesheets().add(GenivisApplication.class.getResource("ui/css/TranxCommonCssStyles/tranxCommonCssStyle1.css").toExternalForm());
        } else if (width >= 1025 && width <= 1280) {
            po2cTopFirstRow.setSpacing(10);
            po2cTopSecondRow.setSpacing(10);
            po2cTotalMainDiv.setSpacing(10);
            po2cTotalMainInnerDiv.setSpacing(8);
            // Set preferred widths as percentages of the HBox width
            po2cBottomFirstV.prefWidthProperty().bind(po2cBottomMain.widthProperty().multiply(0.58));
            po2cBottomSecondV.prefWidthProperty().bind(po2cBottomMain.widthProperty().multiply(0.20));
            po2cBottomThirdV.prefWidthProperty().bind(po2cBottomMain.widthProperty().multiply(0.22));
//            so2cBottomMain.setPrefHeight(150);
            tblvPurOrdToChallGST.getStylesheets().add(GenivisApplication.class.getResource("ui/css/purchase_challan_css/gst_table_css/gst_table_1025_1280.css").toExternalForm());
            tvPurOrdToChallanSupplierDetails.getStylesheets().add(GenivisApplication.class.getResource("ui/css/purchase_challan_css/supplier_table_css/supplier_table_1025_1280.css").toExternalForm());
            bpPurOrdToChallRootPane.getStylesheets().add(GenivisApplication.class.getResource("ui/css/TranxCommonCssStyles/tranxCommonCssStyle2.css").toExternalForm());
        } else if (width >= 1281 && width <= 1368) {
            po2cTopFirstRow.setSpacing(12);
            po2cTopSecondRow.setSpacing(12);
            po2cTotalMainDiv.setSpacing(12);
            po2cTotalMainInnerDiv.setSpacing(8);
            // Set preferred widths as percentages of the HBox width
            po2cBottomFirstV.prefWidthProperty().bind(po2cBottomMain.widthProperty().multiply(0.6));
            po2cBottomSecondV.prefWidthProperty().bind(po2cBottomMain.widthProperty().multiply(0.20));
            po2cBottomThirdV.prefWidthProperty().bind(po2cBottomMain.widthProperty().multiply(0.20));
//            so2cBottomMain.setPrefHeight(150);
            tblvPurOrdToChallGST.getStylesheets().add(GenivisApplication.class.getResource("ui/css/purchase_challan_css/gst_table_css/gst_table_1281_1368.css").toExternalForm());
            tvPurOrdToChallanSupplierDetails.getStylesheets().add(GenivisApplication.class.getResource("ui/css/purchase_challan_css/supplier_table_css/supplier_table_1281_1368.css").toExternalForm());
            bpPurOrdToChallRootPane.getStylesheets().add(GenivisApplication.class.getResource("ui/css/TranxCommonCssStyles/tranxCommonCssStyle3.css").toExternalForm());
        } else if (width >= 1369 && width <= 1400) {
            po2cTopFirstRow.setSpacing(15);
            po2cTopSecondRow.setSpacing(15);
            po2cTotalMainDiv.setSpacing(15);
            po2cTotalMainInnerDiv.setSpacing(10);
            // Set preferred widths as percentages of the HBox width
            po2cBottomFirstV.prefWidthProperty().bind(po2cBottomMain.widthProperty().multiply(0.54));
            po2cBottomSecondV.prefWidthProperty().bind(po2cBottomMain.widthProperty().multiply(0.22));
            po2cBottomThirdV.prefWidthProperty().bind(po2cBottomMain.widthProperty().multiply(0.24));
//            so2cBottomMain.setPrefHeight(150);
            tblvPurOrdToChallGST.getStylesheets().add(GenivisApplication.class.getResource("ui/css/purchase_challan_css/gst_table_css/gst_table_1369_1400.css").toExternalForm());
            tvPurOrdToChallanSupplierDetails.getStylesheets().add(GenivisApplication.class.getResource("ui/css/purchase_challan_css/supplier_table_css/supplier_table_1369_1400.css").toExternalForm());
            bpPurOrdToChallRootPane.getStylesheets().add(GenivisApplication.class.getResource("ui/css/TranxCommonCssStyles/tranxCommonCssStyle4.css").toExternalForm());
        } else if (width >= 1401 && width <= 1440) {
            po2cTopFirstRow.setSpacing(15);
            po2cTopSecondRow.setSpacing(15);
            po2cTotalMainDiv.setSpacing(15);
            po2cTotalMainInnerDiv.setSpacing(10);
            // Set preferred widths as percentages of the HBox width
            po2cBottomFirstV.prefWidthProperty().bind(po2cBottomMain.widthProperty().multiply(0.58));
            po2cBottomSecondV.prefWidthProperty().bind(po2cBottomMain.widthProperty().multiply(0.20));
            po2cBottomThirdV.prefWidthProperty().bind(po2cBottomMain.widthProperty().multiply(0.22));
//            so2cBottomMain.setPrefHeight(150);
            tblvPurOrdToChallGST.getStylesheets().add(GenivisApplication.class.getResource("ui/css/purchase_challan_css/gst_table_css/gst_table_1401_1440.css").toExternalForm());
            tvPurOrdToChallanSupplierDetails.getStylesheets().add(GenivisApplication.class.getResource("ui/css/purchase_challan_css/supplier_table_css/supplier_table_1401_1440.css").toExternalForm());
            bpPurOrdToChallRootPane.getStylesheets().add(GenivisApplication.class.getResource("ui/css/TranxCommonCssStyles/tranxCommonCssStyle5.css").toExternalForm());
        } else if (width >= 1441 && width <= 1680) {
            po2cTopFirstRow.setSpacing(20);
            po2cTopSecondRow.setSpacing(20);
            po2cTotalMainDiv.setSpacing(20);
            po2cTotalMainInnerDiv.setSpacing(10);
            // Set preferred widths as percentages of the HBox width
            po2cBottomFirstV.prefWidthProperty().bind(po2cBottomMain.widthProperty().multiply(0.62));
            po2cBottomSecondV.prefWidthProperty().bind(po2cBottomMain.widthProperty().multiply(0.18));
            po2cBottomThirdV.prefWidthProperty().bind(po2cBottomMain.widthProperty().multiply(0.20));
//            scBottomMain.setPrefHeight(150);
            tblvPurOrdToChallGST.getStylesheets().add(GenivisApplication.class.getResource("ui/css/purchase_challan_css/gst_table_css/gst_table_1441_1680.css").toExternalForm());
            tvPurOrdToChallanSupplierDetails.getStylesheets().add(GenivisApplication.class.getResource("ui/css/purchase_challan_css/supplier_table_css/supplier_table_1441_1680.css").toExternalForm());
            bpPurOrdToChallRootPane.getStylesheets().add(GenivisApplication.class.getResource("ui/css/TranxCommonCssStyles/tranxCommonCssStyle6.css").toExternalForm());
        } else if (width >= 1681 && width <= 1920) {
            po2cTopFirstRow.setSpacing(20);
            po2cTopSecondRow.setSpacing(20);
            po2cTotalMainDiv.setSpacing(20);
            po2cTotalMainInnerDiv.setSpacing(10);
            // Set preferred widths as percentages of the HBox width
            po2cBottomFirstV.prefWidthProperty().bind(po2cBottomMain.widthProperty().multiply(0.6));
            po2cBottomSecondV.prefWidthProperty().bind(po2cBottomMain.widthProperty().multiply(0.18));
            po2cBottomThirdV.prefWidthProperty().bind(po2cBottomMain.widthProperty().multiply(0.22));
//            scBottomMain.setPrefHeight(150);
            tblvPurOrdToChallGST.getStylesheets().add(GenivisApplication.class.getResource("ui/css/gst_table.css").toExternalForm());
            tvPurOrdToChallanSupplierDetails.getStylesheets().add(GenivisApplication.class.getResource("ui/css/invoice_product_history_table.css").toExternalForm());
            bpPurOrdToChallRootPane.getStylesheets().add(GenivisApplication.class.getResource("ui/css/TranxCommonCssStyles/tranxCommonCssStyle7.css").toExternalForm());

        }
    }

    private void tfInvoiceDateValidation() {
        tfPurOrdToChallPurOrdToChallanDate.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal) {
                if (tfPurOrdToChallPurOrdToChallanDate.getText().isEmpty()) {
                    tfPurOrdToChallPurOrdToChallanDate.requestFocus();
                } else {
                    validateInvoiceDate();
                }
            }
        });
    }

    private void validateInvoiceDate() {
        try {
            purChallanLogger.debug("Initiate the validateInvoiceDate() method");
            Map<String, String> map = new HashMap<>();
            String invoiceDate = Communicator.text_to_date.fromString(tfPurOrdToChallPurOrdToChallanDate.getText()).toString();
            map.put("invoiceDate", invoiceDate);
            String formData = Globals.mapToStringforFormData(map);
            HttpResponse<String> response = APIClient.postFormDataRequest(formData, EndPoints.VALIDATE_PUR_INV_DATE);
            String resBody = response.body();
            JsonObject obj = new Gson().fromJson(resBody, JsonObject.class);
            if (obj.get("responseStatus").getAsInt() == 404) {
                AlertUtility.AlertWarningTimeout(AlertUtility.alertTypeWarning, obj.get("message").getAsString(), callback -> {
                    purChallanLogger.debug("Invoice Date : " + invoiceDate + " not in the Financial Year in Pur Challan");
                    purChallanLogger.debug("END of validateInvoiceDate() method in Pur Challan");
                });
                tfPurOrdToChallPurOrdToChallanDate.requestFocus();
            }
        } catch (Exception e) {
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            String exceptionAsString = sw.toString();
            purChallanLogger.error("Exception in validateInvoiceDate:" + exceptionAsString);
        }

    }

    public void tfLedgerNameValidation() {
        tfPurOrdToChallLedgerName.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal) {
                if (tfPurOrdToChallLedgerName.getText().isEmpty()) {
                    tfPurOrdToChallLedgerName.requestFocus();
                }
            }
        });
    }

    public static void showDropDownOnKeyPress(ComboBox<?> comboBox) {
        comboBox.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.SPACE || e.getCode() == KeyCode.DOWN) {
                comboBox.show();
            }
        });
    }

    public void responsiveCmpTable() {
        tblcPurOrdToChallCmpTRowSrNo.prefWidthProperty().bind(tblvPurOrdToChallCmpTRow.widthProperty().multiply(0.02));
        tblcPurOrdToChallCmpTRowPacking.prefWidthProperty().bind(tblvPurOrdToChallCmpTRow.widthProperty().multiply(0.05));
        tblcPurOrdToChallCmpTRowUnit.prefWidthProperty().bind(tblvPurOrdToChallCmpTRow.widthProperty().multiply(0.05));
        tblcPurOrdToChallCmpTRowQuantity.prefWidthProperty().bind(tblvPurOrdToChallCmpTRow.widthProperty().multiply(0.05));
        tblcPurOrdToChallCmpTRowDisPer.prefWidthProperty().bind(tblvPurOrdToChallCmpTRow.widthProperty().multiply(0.05));
        tblcPurOrdToChallCmpTRowDisAmt.prefWidthProperty().bind(tblvPurOrdToChallCmpTRow.widthProperty().multiply(0.05));
        tblcPurOrdToChallCmpTRowTaxPer.prefWidthProperty().bind(tblvPurOrdToChallCmpTRow.widthProperty().multiply(0.04));
        tblcPurOrdToChallCmpTRowAction.prefWidthProperty().bind(tblvPurOrdToChallCmpTRow.widthProperty().multiply(0.04));
        if (tblcPurOrdToChallCmpTRowFreeQty.isVisible()) {
            tblcPurOrdToChallCmpTRowFreeQty.prefWidthProperty().bind(tblvPurOrdToChallCmpTRow.widthProperty().multiply(0.04));
            tblcPurOrdToChallCmpTRowRate.prefWidthProperty().bind(tblvPurOrdToChallCmpTRow.widthProperty().multiply(0.06));
        } else {
            tblcPurOrdToChallCmpTRowRate.prefWidthProperty().bind(tblvPurOrdToChallCmpTRow.widthProperty().multiply(0.08));
        }
        if (tblcPurOrdToChallCmpTRowLevelA.isVisible() == true && tblcPurOrdToChallCmpTRowLevelB.isVisible() == false && tblcPurOrdToChallCmpTRowLevelC.isVisible() == false) {
            tblcPurOrdToChallCmpTRowParticulars.prefWidthProperty().bind(tblvPurOrdToChallCmpTRow.widthProperty().multiply(0.29));
            tblcPurOrdToChallCmpTRowGrossAmt.prefWidthProperty().bind(tblvPurOrdToChallCmpTRow.widthProperty().multiply(0.1));
            tblcPurOrdToChallCmpTRowNetAmt.prefWidthProperty().bind(tblvPurOrdToChallCmpTRow.widthProperty().multiply(0.07));
            tblcPurOrdToChallCmpTRowBatchNo.prefWidthProperty().bind(tblvPurOrdToChallCmpTRow.widthProperty().multiply(0.1));
            tblcPurOrdToChallCmpTRowLevelA.prefWidthProperty().bind(tblvPurOrdToChallCmpTRow.widthProperty().multiply(0.06));
        } else if (tblcPurOrdToChallCmpTRowLevelA.isVisible() == true && tblcPurOrdToChallCmpTRowLevelB.isVisible() == true && tblcPurOrdToChallCmpTRowLevelC.isVisible() == false) {
            tblcPurOrdToChallCmpTRowLevelA.prefWidthProperty().bind(tblvPurOrdToChallCmpTRow.widthProperty().multiply(0.05));
            tblcPurOrdToChallCmpTRowParticulars.prefWidthProperty().bind(tblvPurOrdToChallCmpTRow.widthProperty().multiply(0.28));
            tblcPurOrdToChallCmpTRowGrossAmt.prefWidthProperty().bind(tblvPurOrdToChallCmpTRow.widthProperty().multiply(0.08));
            tblcPurOrdToChallCmpTRowNetAmt.prefWidthProperty().bind(tblvPurOrdToChallCmpTRow.widthProperty().multiply(0.06));
            tblcPurOrdToChallCmpTRowBatchNo.prefWidthProperty().bind(tblvPurOrdToChallCmpTRow.widthProperty().multiply(0.06));
            tblcPurOrdToChallCmpTRowLevelB.prefWidthProperty().bind(tblvPurOrdToChallCmpTRow.widthProperty().multiply(0.05));
        } else if (tblcPurOrdToChallCmpTRowLevelA.isVisible() == true && tblcPurOrdToChallCmpTRowLevelB.isVisible() == true && tblcPurOrdToChallCmpTRowLevelC.isVisible() == true) {
            tblcPurOrdToChallCmpTRowParticulars.prefWidthProperty().bind(tblvPurOrdToChallCmpTRow.widthProperty().multiply(0.245));
            tblcPurOrdToChallCmpTRowGrossAmt.prefWidthProperty().bind(tblvPurOrdToChallCmpTRow.widthProperty().multiply(0.07));
            tblcPurOrdToChallCmpTRowNetAmt.prefWidthProperty().bind(tblvPurOrdToChallCmpTRow.widthProperty().multiply(0.06));
            tblcPurOrdToChallCmpTRowBatchNo.prefWidthProperty().bind(tblvPurOrdToChallCmpTRow.widthProperty().multiply(0.06));
            tblcPurOrdToChallCmpTRowLevelA.prefWidthProperty().bind(tblvPurOrdToChallCmpTRow.widthProperty().multiply(0.05));
            tblcPurOrdToChallCmpTRowLevelB.prefWidthProperty().bind(tblvPurOrdToChallCmpTRow.widthProperty().multiply(0.05));
            tblcPurOrdToChallCmpTRowLevelC.prefWidthProperty().bind(tblvPurOrdToChallCmpTRow.widthProperty().multiply(0.05));
        } else {
            tblcPurOrdToChallCmpTRowParticulars.prefWidthProperty().bind(tblvPurOrdToChallCmpTRow.widthProperty().multiply(0.30));
            tblcPurOrdToChallCmpTRowGrossAmt.prefWidthProperty().bind(tblvPurOrdToChallCmpTRow.widthProperty().multiply(0.08));
            tblcPurOrdToChallCmpTRowNetAmt.prefWidthProperty().bind(tblvPurOrdToChallCmpTRow.widthProperty().multiply(0.08));
            tblcPurOrdToChallCmpTRowBatchNo.prefWidthProperty().bind(tblvPurOrdToChallCmpTRow.widthProperty().multiply(0.08));
        }

    }

    private void gstTableDesign() {
        tblvPurOrdToChallGST.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tblcPurOrdToChallGST.setCellValueFactory(cellData -> {
            StringProperty taxProperty = cellData.getValue().taxPerProperty();
            return Bindings.createStringBinding(
                    () -> "(" + (int) Double.parseDouble(taxProperty.get()) + " %)",
                    taxProperty
            );
        });

        tblcPurOrdToChallGST.setStyle("-fx-alignment: CENTER;");
        tblcPurOrdToChallCGST.setCellValueFactory(cellData -> cellData.getValue().cgstProperty());
        tblcPurOrdToChallCGST.setCellFactory(col -> new TableCell<GstDTO, String>() {
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


        tblcPurOrdToChallCGST.setStyle("-fx-alignment: CENTER;");
        tblcPurOrdToChallSGST.setCellValueFactory(cellData -> cellData.getValue().sgstProperty());
        tblcPurOrdToChallSGST.setStyle("-fx-alignment: CENTER;");
        tblcPurOrdToChallSGST.setCellFactory(col -> new TableCell<GstDTO, String>() {
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
    }

    private void initialEnterMethod() {
        bpPurOrdToChallRootPane.addEventFilter(KeyEvent.KEY_PRESSED, (KeyEvent event) -> {
            if (event.getCode() == KeyCode.ENTER) {

                if (event.getTarget() instanceof Button targetButton && targetButton.getText().equalsIgnoreCase("modify")) {
                    targetButton.getText();
                } else if (event.getTarget() instanceof Button targetButton && targetButton.getText().equalsIgnoreCase("submit")) {
                    targetButton.getText();
                } else if (event.getTarget() instanceof Button targetButton && targetButton.getText().equalsIgnoreCase("cancel")) {
                    targetButton.getText();
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
            }else if (event.getCode() == KeyCode.S && event.isControlDown()) {
                btnPurOrdToChallFinalSubmit.fire();
            } else if (event.getCode() == KeyCode.X && event.isControlDown()) {
                btnPurOrdToChallFinalCancel.fire();
            }
        });
    }

    private void dateValidation() {

        LocalDate current_date = LocalDate.now();

        if (!purchase_id.isEmpty() && purchase_id != null) {
            LocalDate currentDate = LocalDate.now();
            tfPurOrdToChallTranxDate.setText(currentDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));

            System.out.println("Date Get >> " + invoice_data_map.get("invoice_dt"));
            LocalDate invoice_dt = LocalDate.parse(invoice_data_map.get("invoice_dt"));
            tfPurOrdToChallPurOrdToChallanDate.setText(invoice_dt.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        } else {
            tfPurOrdToChallTranxDate.setText(current_date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
            DateValidator.applyDateFormat(tfPurOrdToChallPurOrdToChallanDate);
            LocalDate currentDate = LocalDate.now();
            tfPurOrdToChallPurOrdToChallanDate.setText(currentDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
            sceneInitilization();
        }
    }

    //? On Clicking on the Modify Button From Create or Edit page redirect Back to List page
    public void backToList() {
        AlertUtility.AlertConfirmation(AlertUtility.alertTypeConfirmation, LedgerMessageConsts.msgGoList, input -> {
            if (input == 1) {
                GlobalController.getInstance().addTabStatic(PURCHASE_ORDER_LIST_SLUG, false);
            }
        });
    }

    private void createUpdateCall() {
        btnPurOrdToChallFinalSubmit.setOnAction((event) -> {
            if (tfPurOrdToChallLedgerName.getText().isEmpty()) {
                AlertUtility.AlertErrorTimeout(AlertUtility.alertTypeError, "Enter Mandatory Ledger Name Field", input -> {
                    if (input == 1) {
                        tfPurOrdToChallLedgerName.requestFocus();
                    }
                });
            } else if (tfPurOrdToChallChallanNo.getText().isEmpty()) {
                AlertUtility.AlertErrorTimeout(AlertUtility.alertTypeError, "Enter Mandatory Challan No Field", input -> {
                    if (input == 1) {
                        tfPurOrdToChallChallanNo.requestFocus();
                    }
                });
            } else {
                String btnText = btnPurOrdToChallFinalSubmit.getText();
                if (btnText.equalsIgnoreCase("Submit")) {
                    if (purchaseOrderDTO == null) {
                        //Normal Purchase Challan Create
                        AlertUtility.AlertConfirmation(AlertUtility.alertTypeConfirmation, LedgerMessageConsts.msgConfirmationOnSubmit + tfPurOrdToChallChallanNo.getText() + LedgerMessageConsts.msgInvoiceNumber, input -> {
                            if (input == 1) {
                                createPurchaseChallanNew();
                            }
                        });
                    }
                }
            }

        });

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

        tblvPurOrdToChallGST.getItems().clear();
        ObservableList<GstDTO> gstDTOObservableList = FXCollections.observableArrayList();
        ObservableList<?> item9List = (ObservableList<?>) item[9];
        for (Object obj : item9List) {
            if (obj instanceof GstDTO) {
                gstDTOObservableList.add((GstDTO) obj);
            }
        }
        tblvPurOrdToChallGST.getItems().addAll(gstDTOObservableList);

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
        purchase_challan_map.put("taxCalculation", jsonObject.toString());

        totalamt = (String) item[2];


        total_purchase_discount_amt = (String) item[4];


        purchase_challan_map.put("total_qty", (String) item[0]);
        purchase_challan_map.put("total_free_qty", (String) item[1]);

        purchase_challan_map.put("total_base_amt", (String) item[3]);
        purchase_challan_map.put("total_invoice_dis_amt", (String) item[4]);
        purchase_challan_map.put("taxable_amount", (String) item[5]);
        purchase_challan_map.put("bill_amount", (String) item[2]);
        purchase_challan_map.put("total_tax_amt", (String) item[6]);

        purchase_challan_map.put("totaligst", (String) item[6]);
        purchase_challan_map.put("totalsgst", (String) item[7]);
        purchase_challan_map.put("totalcgst", (String) item[8]);


        System.out.println("callBack TotalTaxableAMount :: >> " + Double.parseDouble((String) item[5]));
        total_taxable_amt = Double.parseDouble((String) item[5]);
        lblPurOrdToChallTotalQty.setText((String) item[0]);
        lblPurOrdToChallFreeQty.setText((String) item[1]);
        lblPurOrdToChallGrossTotal.setText((String) item[3]);
        lblPurOrdToChallTotalDiscount.setText((String) item[4]);
        lblPurOrdToChallTotalTaxableAmount.setText((String) item[5]);
        lblPurOrdToChallTotalTax.setText((String) item[6]);
        lblPurOrdToChallTotalCGST.setText((String) item[7]);
        lblPurOrdToChallTotalSGST.setText((String) item[8]);

        /**
         * Info : RoundOff Calculation Method
         * @Author : Vinit Chilaka
         */
        chbPurOrdToChallRoundOff.setSelected(true);
        roundOffCalculations();

    };

    private void roundOffCalculations() {
        Double billamt = 0.00; // local variable to store row wise  Bill amount
        Double total_bill_amt = 0.00; // local variable to store Final bill Amount without roundoff
        Double final_r_off_bill_amt = 0.00; // local variable to store Final Bill amount with round off
        Double roundOffAmt = 0.00; // local variable to store Roundoff Amount
        for (PurchaseInvoiceTable cmpTRowDTO : tblvPurOrdToChallCmpTRow.getItems()) {
            billamt = Double.valueOf(cmpTRowDTO.getNet_amount());
            total_bill_amt += billamt;
        }
        final_r_off_bill_amt = (double) Math.round(total_bill_amt);
        roundOffAmt = final_r_off_bill_amt - total_bill_amt;

        if (chbPurOrdToChallRoundOff.isSelected()) {
            lblPurOrdToChallTotalBillAmount.setText(String.format("%.2f", final_r_off_bill_amt));
            lblPurOrdToChallRoundOff.setText(String.format("%.2f", roundOffAmt));
        } else {
            lblPurOrdToChallTotalBillAmount.setText(String.format("%.2f", total_bill_amt));
            lblPurOrdToChallRoundOff.setText("0.00");
        }

    }


    TableCellCallback<Object[]> productID_callback = item -> {
        if (item.length == 1) {
            tranxProductDetailsFun((String) item[0]);
            getSupplierListbyProductId((String) item[0]);
        } else {
            tranxBatchDetailsFun((String) item[0], (String) item[1]);
        }
    };


    public void tableInitiliazation() {

        tblvPurOrdToChallCmpTRow.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tblvPurOrdToChallCmpTRow.setEditable(true);
        tblvPurOrdToChallCmpTRow.setFocusTraversable(false);

        Label headerLabel = new Label("Sr\nNo.");
        tblcPurOrdToChallCmpTRowSrNo.setGraphic(headerLabel);

        if (!purchase_id.isEmpty() && purchase_id != null) {

            int count = 1;
            int index = 0;
            for (PurOrdToChallRowDTO rowListDTO : rowListDTOS) {
                PurchaseInvoiceTable purchaseInvoiceTable = new PurchaseInvoiceTable();
                purchaseInvoiceTable.setPurchase_id(String.valueOf(purchase_id));
                purchaseInvoiceTable.setCurrent_index(index + "");
                purchaseInvoiceTable.setSr_no(String.valueOf(count));
                purchaseInvoiceTable.setDetails_id(String.valueOf("0"));
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
                purchaseInvoiceTable.setB_purchase_rate(String.valueOf(rowListDTO.getRate()));
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
                purchaseInvoiceTable.setFree(String.valueOf(rowListDTO.getFreeQty()));
                purchaseInvoiceTable.setRow_dis_amt(String.valueOf(rowListDTO.getRowDisAmt()));

                purchaseInvoiceTable.setGross_amount(String.valueOf(rowListDTO.getGrossAmt()));

                purchaseInvoiceTable.setGross_amount1(String.valueOf(rowListDTO.getGrossAmt()));
                purchaseInvoiceTable.setLedger_id(invoice_data_map.get("supplierId"));
                purchaseInvoiceTable.setTax(String.valueOf(rowListDTO.getGst()));
                if (rowListDTO.getBatch() != null) {
                    purchaseInvoiceTable.setIs_batch(rowListDTO.getBatch().toString());
                } else
                    purchaseInvoiceTable.setIs_batch("");

                purchaseInvoiceTable.setReference_id(String.valueOf(rowListDTO.getReferenceId()));
                purchaseInvoiceTable.setReference_type(String.valueOf(rowListDTO.getReferenceType()));


                BatchWindowTableDTO batchWindowTableDTO = new BatchWindowTableDTO();

                batchWindowTableDTO.setProductId(purchaseInvoiceTable.getProduct_id());
                batchWindowTableDTO.setLevelAId(purchaseInvoiceTable.getLevelA_id());
                batchWindowTableDTO.setLevelBId(purchaseInvoiceTable.getLevelB_id());
                batchWindowTableDTO.setLevelCId(purchaseInvoiceTable.getLevelC_id());
                batchWindowTableDTO.setUnitId(purchaseInvoiceTable.getUnit_id());

                batchWindowTableDTO.setBatchNo(purchaseInvoiceTable.getBatch_or_serial());
                batchWindowTableDTO.setB_details_id(purchaseInvoiceTable.getB_details_id());

                if (purchaseInvoiceTable.getManufacturing_date() != null && !purchaseInvoiceTable.getManufacturing_date().isEmpty()) {
                    LocalDate manu_dt = LocalDate.parse(purchaseInvoiceTable.getManufacturing_date());
                    batchWindowTableDTO.setManufacturingDate(manu_dt.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
                } else {
                    batchWindowTableDTO.setManufacturingDate("");
                }

                if (purchaseInvoiceTable.getB_expiry() != null && !purchaseInvoiceTable.getB_expiry().isEmpty()) {
                    LocalDate exp_dt = LocalDate.parse(purchaseInvoiceTable.getB_expiry());
                    batchWindowTableDTO.setExpiryDate(exp_dt.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
                } else {
                    batchWindowTableDTO.setExpiryDate("");
                }

                batchWindowTableDTO.setMrp(purchaseInvoiceTable.getB_rate());
                batchWindowTableDTO.setPurchaseRate(purchaseInvoiceTable.getB_purchase_rate());

                Double qty = Double.parseDouble(purchaseInvoiceTable.getQuantity());
                batchWindowTableDTO.setQuantity(decimalFormat.format(qty));
                Double freeqty = Double.parseDouble(purchaseInvoiceTable.getFree());
                batchWindowTableDTO.setFree(decimalFormat.format(freeqty));

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

                tblvPurOrdToChallCmpTRow.getItems().addAll(purchaseInvoiceTable);
                PurchaseInvoiceCalculation.rowCalculationForPurcInvoice(index, tblvPurOrdToChallCmpTRow, callback);
                PurchaseInvoiceCalculation.calculateGst(tblvPurOrdToChallCmpTRow, callback);
//                calculateGst(tableView, callback);

                List<LevelAForPurInvoice> levelAForPurInvoiceList = ProductUnitsPacking.getAllProductUnitsPackingFlavour(purchaseInvoiceTable.getProduct_id());

                ObservableList<LevelAForPurInvoice> observableLevelAList = FXCollections.observableArrayList(levelAForPurInvoiceList);

                if (index >= 0 && index < PurInvoiceCommunicator.levelAForPurInvoiceObservableList.size()) {
                    PurInvoiceCommunicator.levelAForPurInvoiceObservableList.set(index, observableLevelAList);

                    tblvPurOrdToChallCmpTRow.getItems().get(index).setLevelA(null);
                    tblvPurOrdToChallCmpTRow.getItems().get(index).setLevelA(levelAForPurInvoiceList.get(0).getLabel());

                    for (LevelAForPurInvoice levelAForPurInvoice : PurInvoiceCommunicator.levelAForPurInvoiceObservableList.get(index)) {
                        if (tblvPurOrdToChallCmpTRow.getItems().get(index).getLevelA_id().equals(levelAForPurInvoice.getValue())) {
                            tblvPurOrdToChallCmpTRow.getItems().get(index).setLevelA(null);
                            tblvPurOrdToChallCmpTRow.getItems().get(index).setLevelA(levelAForPurInvoice.getLabel());
                        }
                    }

                } else {
                    PurInvoiceCommunicator.levelAForPurInvoiceObservableList.add(observableLevelAList);

                    tblvPurOrdToChallCmpTRow.getItems().get(index).setLevelA(null);
                    tblvPurOrdToChallCmpTRow.getItems().get(index).setLevelA(levelAForPurInvoiceList.get(0).getLabel());

                    for (LevelAForPurInvoice levelAForPurInvoice : PurInvoiceCommunicator.levelAForPurInvoiceObservableList.get(index)) {
                        if (tblvPurOrdToChallCmpTRow.getItems().get(index).getLevelA_id().equals(levelAForPurInvoice.getValue())) {
                            tblvPurOrdToChallCmpTRow.getItems().get(index).setLevelA(null);
                            tblvPurOrdToChallCmpTRow.getItems().get(index).setLevelA(levelAForPurInvoice.getLabel());
                        }
                    }
                }
                count++;
                index++;
            }
//            setAddChargesInTable();
        } else {
            tblvPurOrdToChallCmpTRow.getItems().addAll(new PurchaseInvoiceTable("", "0", "1", "", "", "", "", "", "", "", "", "", "", "", "", "", "", ""));
        }

//        tblvPurOrdToChallCmpTRow.getItems().addAll(new PurchaseInvoiceTable("", "0", "1", "", "", "", "", "", "", "", "", "", "", "", "", "", "", ""));

        tblcPurOrdToChallCmpTRowSrNo.setCellValueFactory(cellData -> cellData.getValue().sr_noProperty());
        tblcPurOrdToChallCmpTRowSrNo.setStyle("-fx-alignment: CENTER;");

        tblcPurOrdToChallCmpTRowPacking.setCellValueFactory(cellData -> cellData.getValue().packagesProperty());
        tblcPurOrdToChallCmpTRowPacking.setStyle("-fx-alignment: CENTER;");

        tblcPurOrdToChallCmpTRowLevelB.setCellValueFactory(cellData -> cellData.getValue().levelBProperty());
        tblcPurOrdToChallCmpTRowLevelB.setCellFactory(column -> new ComboBoxTableCellForLevelB("tblcPurOrdToChallCmpTRowLevelB"));

        tblcPurOrdToChallCmpTRowLevelA.setCellValueFactory(cellData -> cellData.getValue().levelAProperty());
        tblcPurOrdToChallCmpTRowLevelA.setCellFactory(column -> new ComboBoxTableCellForLevelA("tblcPurOrdToChallCmpTRowLevelA"));

        tblcPurOrdToChallCmpTRowLevelC.setCellValueFactory(cellData -> cellData.getValue().levelCProperty());
        tblcPurOrdToChallCmpTRowLevelC.setCellFactory(column -> new ComboBoxTableCellForLevelC("tblcPurOrdToChallCmpTRowLevelC"));
        tblcPurOrdToChallCmpTRowUnit.setCellValueFactory(cellData -> cellData.getValue().unitProperty());
        tblcPurOrdToChallCmpTRowUnit.setCellFactory(column -> new ComboBoxTableCellForUnit("tblcPurOrdToChallCmpTRowUnit"));

        tblcPurOrdToChallCmpTRowParticulars.setCellValueFactory(cellData -> cellData.getValue().particularsProperty());
        tblcPurOrdToChallCmpTRowParticulars.setCellFactory(column -> new TextFieldTableCellForPurOrdToChallan("tblcPurOrdToChallCmpTRowParticulars", callback, productID_callback));

        tblcPurOrdToChallCmpTRowBatchNo.setCellValueFactory(cellData -> cellData.getValue().batch_or_serialProperty());
        tblcPurOrdToChallCmpTRowBatchNo.setCellFactory(column -> new TextFieldTableCellForPurOrdToChallan("tblcPurOrdToChallCmpTRowBatchNo", callback, productID_callback));

        tblcPurOrdToChallCmpTRowQuantity.setCellValueFactory(cellData -> cellData.getValue().quantityProperty());
        tblcPurOrdToChallCmpTRowQuantity.setCellFactory(column -> new TextFieldTableCellForPurOrdToChallan("tblcPurOrdToChallCmpTRowQuantity", callback));

        tblcPurOrdToChallCmpTRowRate.setCellValueFactory(cellData -> cellData.getValue().rateProperty());
        tblcPurOrdToChallCmpTRowRate.setCellFactory(column -> new TextFieldTableCellForPurOrdToChallan("tblcPurOrdToChallCmpTRowRate", callback));

        tblcPurOrdToChallCmpTRowGrossAmt.setCellValueFactory(cellData -> cellData.getValue().gross_amountProperty());
        tblcPurOrdToChallCmpTRowGrossAmt.setCellFactory(column -> new TextFieldTableCellForPurOrdToChallan("tblcPurOrdToChallCmpTRowGrossAmt", callback));

        tblcPurOrdToChallCmpTRowDisPer.setCellValueFactory(cellData -> cellData.getValue().dis_perProperty());
        tblcPurOrdToChallCmpTRowDisPer.setCellFactory(column -> new TextFieldTableCellForPurOrdToChallan("tblcPurOrdToChallCmpTRowDisPer", callback));


        tblcPurOrdToChallCmpTRowDisAmt.setCellValueFactory(cellData -> cellData.getValue().dis_amtProperty());
        tblcPurOrdToChallCmpTRowDisAmt.setCellFactory(column -> new TextFieldTableCellForPurOrdToChallan("tblcPurOrdToChallCmpTRowDisAmt", callback));

        tblcPurOrdToChallCmpTRowTaxPer.setCellValueFactory(cellData -> cellData.getValue().taxProperty());
        tblcPurOrdToChallCmpTRowTaxPer.setCellFactory(column -> new TextFieldTableCellForPurOrdToChallan("tblcPurOrdToChallCmpTRowTaxPer", callback));

        tblcPurOrdToChallCmpTRowNetAmt.setCellValueFactory(cellData -> cellData.getValue().net_amountProperty());
        tblcPurOrdToChallCmpTRowNetAmt.setCellFactory(column -> new TextFieldTableCellForPurOrdToChallan("tblcPurOrdToChallCmpTRowNetAmt", callback));

        tblcPurOrdToChallCmpTRowAction.setCellValueFactory(cellData -> cellData.getValue().net_amountProperty());
        tblcPurOrdToChallCmpTRowAction.setCellFactory(column -> new ButtonTableCell());

        columnVisibility(tblcPurOrdToChallCmpTRowLevelA, Globals.getUserControlsWithSlug("is_level_a"));
        columnVisibility(tblcPurOrdToChallCmpTRowLevelB, Globals.getUserControlsWithSlug("is_level_b"));
        columnVisibility(tblcPurOrdToChallCmpTRowLevelC, Globals.getUserControlsWithSlug("is_level_c"));

        tblcPurOrdToChallCmpTRowLevelA.setText(Globals.getUserControlsNameWithSlug("is_level_a") + "");
        tblcPurOrdToChallCmpTRowLevelB.setText(Globals.getUserControlsNameWithSlug("is_level_B") + "");
        tblcPurOrdToChallCmpTRowLevelC.setText(Globals.getUserControlsNameWithSlug("is_level_C") + "");

    }

    /**
     * Info : This Method to off the Visibility of the LevelA, LevelB, LevelC Columns.
     */
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
        object[0] = cmbPurOrdToChallSupplierGST.getSelectionModel().getSelectedItem();
        if (object[0] != null) {
            for (CommonDTO commonDTO : supplierGSTINList) {
                if (object[0].equals(commonDTO)) {
                    supplierGSTIN_ID = commonDTO.getId();
                    gst_no = commonDTO.getText();
                }
            }
        }
    }


    public void handleTfLedgerName() {
        Stage stage = (Stage) bpPurOrdToChallRootPane.getScene().getWindow();
        SingleInputDialogs.openLedgerPopUp(stage, "Creditors", input -> {
            supplierGSTINList.clear();
            tfPurOrdToChallLedgerName.setText(input[0].toString());
            ledger_id = (String) input[1];
            ledgerStateCode = (String) input[3];
            tblvPurOrdToChallCmpTRow.getItems().get(0).setLedger_id(ledger_id);
            tranxLedgerDetailsFun(ledger_id);

            @SuppressWarnings("unchecked")
            ObservableList<GstDetailsDTO> gstDetailsDTOS = (ObservableList<GstDetailsDTO>) input[2];

            for (GstDetailsDTO gstDetailsDTO : gstDetailsDTOS) {
                supplierGSTINList.add(new CommonDTO(gstDetailsDTO.getGstNo(), gstDetailsDTO.getId()));
            }
//            if (supplierGSTINList != null && !supplierGSTINList.isEmpty()) {
//                cmbPurOrdToChallSupplierGST.setItems(supplierGSTINList);
//                cmbPurOrdToChallSupplierGST.setValue(supplierGSTINList.get(0));
//                cmbPurOrdToChallSupplierGST.requestFocus();
//            } else {
//                tfPurOrdToChallChallanNo.requestFocus();
//            }

            if (supplierGSTINList != null && !supplierGSTINList.isEmpty()) {
                cmbPurOrdToChallSupplierGST.setItems(supplierGSTINList);
                cmbPurOrdToChallSupplierGST.setValue(supplierGSTINList.get(0));
                if (supplierGSTINList.size() > 1) {
                    cmbPurOrdToChallSupplierGST.requestFocus();
                } else {
                    tfPurOrdToChallChallanNo.requestFocus();
                }
            } else {
                tfPurOrdToChallChallanNo.requestFocus();
            }

            if (ledgerStateCode.equalsIgnoreCase(GlobalTranx.getCompanyStateCode().toString())) {
                taxFlag = true;
            }

        }, in -> {
            System.out.println("In >> Called >. " + in);
            if (in == true) {
//                            isLedgerRed = true;
//                            setPurChallDataToProduct();
            }
        });
    }


    public void purc_disc_per(KeyEvent keyEvent) {
        String discText = tfPurOrdToChallAllRowDisPer.getText();
        CommonValidationsUtils.restrictToDecimalNumbers(tfPurOrdToChallAllRowDisPer);
        if (!TextUtils.isEmpty(discText)) {
            double totalTaxableAmt = 0.0;
            for (int i = 0; i < tblvPurOrdToChallCmpTRow.getItems().size(); i++) {
                System.out.println("Gross amt in Prop--->" + tblvPurOrdToChallCmpTRow.getItems().get(i).getGross_amount());
                PurchaseInvoiceCalculation.rowCalculationForPurcInvoice(i, tblvPurOrdToChallCmpTRow, callback);//call row calculation function
                PurchaseInvoiceTable purchaseInvoiceTable = tblvPurOrdToChallCmpTRow.getItems().get(i);
                totalTaxableAmt = totalTaxableAmt + Double.parseDouble(purchaseInvoiceTable.getTaxable_amt());

            }
            double disc_per = Double.parseDouble(discText);
            System.out.println("totalTaxableAmount >> : " + totalTaxableAmt);
            Double amount = (totalTaxableAmt * disc_per) / 100;
            tfPurOrdToChallAllRowDisAmt.setText(String.valueOf(amount));
            PurchaseInvoiceCalculation.discountPropotionalCalculation(disc_per + "", "0", tfPurOrdToChallanAddChgAmt.getText(), tblvPurOrdToChallCmpTRow, callback);
            if (!tfPurOrdToChallanAddChgAmt.getText().isEmpty()) {
                System.out.println("Additioanal CHarges Called >> ");
                PurchaseInvoiceCalculation.additionalChargesCalculation(tfPurOrdToChallanAddChgAmt.getText(), tblvPurOrdToChallCmpTRow, callback);
            }
        } else {
            tfPurOrdToChallAllRowDisAmt.setText("");
            /* for (PurchaseInvoiceTable purchaseInvoiceTable : tblvPurOrdToChallCmpTRow.getItems()) {
                String netAmt = purchaseInvoiceTable.getNet_amount();
                if (!purchaseInvoiceTable.getTaxable_amt().isEmpty()) {
                    Double orgTaxableAmt = Double.parseDouble(purchaseInvoiceTable.getTaxable_amt());
                    purchaseInvoiceTable.setNet_amount(netAmt);
                    purchaseInvoiceTable.setTaxable_amt(String.valueOf(orgTaxableAmt));
//                    purchaseInvoiceTable.setTotal_taxable_amt(String.valueOf(orgTaxableAmt));
                }
            }*/

            PurchaseInvoiceCalculation.discountPropotionalCalculation("0", "0", tfPurOrdToChallanAddChgAmt.getText(), tblvPurOrdToChallCmpTRow, callback);
            if (!tfPurOrdToChallanAddChgAmt.getText().isEmpty()) {
                System.out.println("Additioanal CHarges Called >> ");
                PurchaseInvoiceCalculation.additionalChargesCalculation(tfPurOrdToChallanAddChgAmt.getText(), tblvPurOrdToChallCmpTRow, callback);
            }
        }

        //? Calculate Tax for each Row
        PurchaseInvoiceCalculation.calculateGst(tblvPurOrdToChallCmpTRow, callback);
    }

    public void purc_disc_amt(KeyEvent keyEvent) {
        String discAmtText = tfPurOrdToChallAllRowDisAmt.getText();
        CommonValidationsUtils.restrictToDecimalNumbers(tfPurOrdToChallAllRowDisAmt);
        if (!TextUtils.isEmpty(discAmtText)) {
            double totalTaxableAmt = 0.0;
            for (int i = 0; i < tblvPurOrdToChallCmpTRow.getItems().size(); i++) {
                System.out.println("Gross amt in Prop--->" + tblvPurOrdToChallCmpTRow.getItems().get(i).getGross_amount());
                PurchaseInvoiceCalculation.rowCalculationForPurcInvoice(i, tblvPurOrdToChallCmpTRow, callback);//call row calculation function
                PurchaseInvoiceTable purchaseInvoiceTable = tblvPurOrdToChallCmpTRow.getItems().get(i);
                totalTaxableAmt = totalTaxableAmt + Double.parseDouble(purchaseInvoiceTable.getTaxable_amt());

            }
            double disc_amt = Double.parseDouble(discAmtText);
            double percentage = (disc_amt / totalTaxableAmt) * 100;
            tfPurOrdToChallAllRowDisPer.setText(String.format("%.2f", percentage));

            PurchaseInvoiceCalculation.discountPropotionalCalculation("0", disc_amt + "", tfPurOrdToChallanAddChgAmt.getText(), tblvPurOrdToChallCmpTRow, callback);
            if (!tfPurOrdToChallanAddChgAmt.getText().isEmpty()) {
                System.out.println("Additioanal CHarges Called >> ");
                PurchaseInvoiceCalculation.additionalChargesCalculation(tfPurOrdToChallanAddChgAmt.getText(), tblvPurOrdToChallCmpTRow, callback);
            }
        } else {
            tfPurOrdToChallAllRowDisPer.setText("");
           /* for (PurchaseInvoiceTable purchaseInvoiceTable : tblvPurOrdToChallCmpTRow.getItems()) {
                String netAmt = purchaseInvoiceTable.getNet_amount();
                if (!purchaseInvoiceTable.getTaxable_amt().isEmpty()) {
                    Double orgTaxableAmt = Double.parseDouble(purchaseInvoiceTable.getTaxable_amt());
                    purchaseInvoiceTable.setNet_amount(netAmt);
                    purchaseInvoiceTable.setTaxable_amt(String.valueOf(orgTaxableAmt));
                    purchaseInvoiceTable.setTotal_taxable_amt(String.valueOf(orgTaxableAmt));
                }
            }*/
            PurchaseInvoiceCalculation.discountPropotionalCalculation("0", "0", tfPurOrdToChallanAddChgAmt.getText(), tblvPurOrdToChallCmpTRow, callback);
            if (!tfPurOrdToChallanAddChgAmt.getText().isEmpty()) {
                System.out.println("Additioanal CHarges Called >> ");
                PurchaseInvoiceCalculation.additionalChargesCalculation(tfPurOrdToChallanAddChgAmt.getText(), tblvPurOrdToChallCmpTRow, callback);
            }
        }

        //? Calculate Tax for each Row
        PurchaseInvoiceCalculation.calculateGst(tblvPurOrdToChallCmpTRow, callback);
    }

    public void onClickAdditionalCharges() {
        Stage stage = (Stage) bpPurOrdToChallRootPane.getScene().getWindow();
        AdditionalCharges.additionalCharges(stage, addChargesDTOList, total_taxable_amt, (input1, input2) -> {
            addChargesDTOList.clear();
            addChargesDTOList.addAll(input2);
            tfPurOrdToChallanAddChgAmt.setText(input1[1]);
            PurchaseInvoiceCalculation.additionalChargesCalculation(input1[1], tblvPurOrdToChallCmpTRow, callback);

        });
    }


    // Scene Initialization
    public void sceneInitilization() {
        bpPurOrdToChallRootPane.sceneProperty().addListener((observable, oldScene, newScene) -> {
            if (newScene != null && newScene.getWindow() instanceof Stage) {
                Communicator.stage = (Stage) newScene.getWindow();
            }
        });
    }
//    public void sceneInitilization() {
//        bpPurOrdToChallRootPane.sceneProperty().addListener((observable, oldScene, newScene) -> {
//            if (newScene != null && newScene.getWindow() instanceof Stage) {
//                Communicator.stage = (Stage) newScene.getWindow();
//                if (Communicator.stage != null) {
//                    Communicator.stage.getScene().addEventFilter(MouseEvent.MOUSE_CLICKED, (e) -> {
//                        if (e.getButton() == MouseButton.PRIMARY || e.getButton() == MouseButton.SECONDARY) {
//                            System.out.println(":MouseButton => " + e.getButton());
//                            e.consume();
//                        }
//                    });
//                }
//            }
//        });
////        bpPurOrdToChallRootPane.getScene()
//        if (bpPurOrdToChallRootPane.getScene() != null) {
//            bpPurOrdToChallRootPane.getScene().addEventFilter(MouseEvent.MOUSE_RELEASED,
//                    new EventHandler<MouseEvent>() {
//                        public void handle(MouseEvent event) {
//                            if (event.getButton() == MouseButton.SECONDARY) {
//                                event.consume();
//                            } else if (event.getButton() == MouseButton.PRIMARY) {
//                                event.consume();
//                            }
//                        }
//                    });
//        }
//
//    }

    // Auto Setting the PurChase Challan Sr. No.
    private void getPurOrdToChallSrNumber() {
        APIClient apiClient = null;
        try {
            purChallanLogger.debug("Get getPurOrdToChallSrNumber Started...");
            apiClient = new APIClient(EndPoints.getCurrentPurChallSrNo, "", RequestType.GET);
            apiClient.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
                @Override
                public void handle(WorkerStateEvent workerStateEvent) {
                    jsonObject = new Gson().fromJson(workerStateEvent.getSource().getValue().toString(), JsonObject.class);
                    if (jsonObject.get("responseStatus").getAsInt() == 200) {
                        tfPurOrdToChallPurSerialNo.setText("" + jsonObject.get("count"));
                    }
                }
            });
            apiClient.setOnCancelled(new EventHandler<WorkerStateEvent>() {
                @Override
                public void handle(WorkerStateEvent workerStateEvent) {
                    purChallanLogger.error("Network API cancelled in getPurOrdToChallSrNumber()" + workerStateEvent.getSource().getValue().toString());

                }
            });
            apiClient.setOnFailed(new EventHandler<WorkerStateEvent>() {
                @Override
                public void handle(WorkerStateEvent workerStateEvent) {
                    purChallanLogger.error("Network API failed in getPurOrdToChallSrNumber()" + workerStateEvent.getSource().getValue().toString());
                }
            });
            apiClient.start();
            purChallanLogger.debug("Get getPurOrdToChallSrNumber End...");
        } catch (Exception e) {
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            String exceptionAsString = sw.toString();
            purChallanLogger.error("Exception in getVoucherNumber():" + exceptionAsString);
        }

    }

    // Get  All Purchase Accounts
    public void getPurchaseAccounts() {
        APIClient apiClient = null;
        try {
            purChallanLogger.debug("Get getPurchaseAccounts Started...");
            apiClient = new APIClient(EndPoints.getPurChallPurchaseAccounts, "", RequestType.GET);
            apiClient.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
                @Override
                public void handle(WorkerStateEvent workerStateEvent) {
                    jsonObject = new Gson().fromJson(workerStateEvent.getSource().getValue().toString(), JsonObject.class);
                    ObservableList<CommonDTO> observableList = FXCollections.observableArrayList();
                    if (jsonObject.get("responseStatus").getAsInt() == 200) {
                        JsonArray responseObject = jsonObject.getAsJsonArray("list");


                        if (responseObject.size() > 0) {
                            for (JsonElement element : responseObject) {
                                JsonObject item = element.getAsJsonObject();
                                String id = item.get("id").getAsString();
                                String name = item.get("name").getAsString();
                                String unique_code = item.get("unique_code").getAsString();


                                observableList.add(new CommonDTO(name, id));
                                cmbPurOrdToChallPurchaAc.setItems(observableList);
                                cmbPurOrdToChallPurchaAc.getSelectionModel().selectFirst();
                                cmbPurOrdToChallPurchaAc.setConverter(new StringConverter<CommonDTO>() {
                                    @Override
                                    public String toString(CommonDTO o) {
                                        return o.getText();
                                    }

                                    @Override
                                    public CommonDTO fromString(String s) {
                                        return null;
                                    }
                                });

                            }

                        } else {
                            purChallanLogger.debug("Get getPurchaseAccounts responseObject is null...");
                            System.out.println("responseObject is null");
                        }
                    } else {
                        purChallanLogger.debug("Get getPurchaseAccounts Error in response...");
                        System.out.println("Error in response");
                    }
                }
            });
            apiClient.setOnCancelled(new EventHandler<WorkerStateEvent>() {
                @Override
                public void handle(WorkerStateEvent workerStateEvent) {
                    purChallanLogger.error("Network API cancelled in getPurchaseAccounts()" + workerStateEvent.getSource().getValue().toString());

                }
            });

            apiClient.setOnFailed(new EventHandler<WorkerStateEvent>() {
                @Override
                public void handle(WorkerStateEvent workerStateEvent) {
                    purChallanLogger.error("Network API cancelled in getPurchaseAccounts()" + workerStateEvent.getSource().getValue().toString());

                }
            });
            apiClient.start();
            purChallanLogger.debug("Get getPurchaseAccounts  end");


        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            apiClient = null;
        }
    }

    // to Upload Image
    @FXML
    private void handleChooseFile() {

        FileChooser fileChooser = new FileChooser();

        // Set extension filters
        FileChooser.ExtensionFilter textFilter = new FileChooser.ExtensionFilter("Text files (*.txt)", "*.txt");
        FileChooser.ExtensionFilter pdfFilter = new FileChooser.ExtensionFilter("PDF Files (*.pdf)", "*.pdf");
        FileChooser.ExtensionFilter excelFilter = new FileChooser.ExtensionFilter("Excel Files (*.xlsx, *.xls)", "*.xlsx", "*.xls");
        FileChooser.ExtensionFilter jsonFilter = new FileChooser.ExtensionFilter("JSON Files (*.json)", "*.json");

        // Add filters to fileChooser
        fileChooser.getExtensionFilters().addAll(textFilter, pdfFilter, excelFilter, jsonFilter);
        // Show open file dialog
        Stage stage = (Stage) btnPurOrdToChallChooseFileButton.getScene().getWindow();
        File selectedFile = fileChooser.showOpenDialog(stage);

        // Handle selected file
        if (selectedFile != null) {
            tfPurOrdToChallChooseFileText.setText(selectedFile.getName());
        }
    }

    // GstDetails DTO to Set GST
    public GstDetailsDTO convertJsonToGstDetailsDTO(JsonElement jsonElement) {
        // Parse the JsonElement and create a GstDetailsDTO object
        // For example:
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        String gstNumber = jsonObject.get("gstNumber").getAsString();
        String id = jsonObject.get("id").getAsString();
        String state = jsonObject.get("state").getAsString();
        // Parse other properties as needed

        return new GstDetailsDTO(id, gstNumber, state); // Assuming GstDetailsDTO constructor accepts gstNumber
    }

    // Helper method to round the value to a specified decimal places String.format("%.3f"; costVal)
    private double roundDigit(double value, int decimalPlaces) {
        double factor = Math.pow(10, decimalPlaces);
        return Math.round(value * factor) / factor;
    }

    //     Calling Create API of Purchase Challan Start
    public void createPurchaseChallanNew() {

        Object idValue = invoice_data_map.get("id");
        if (idValue != null) {
            purchase_challan_map.put("id", idValue.toString());
        }

        purchase_challan_map.put("taxFlag", String.valueOf(taxFlag));

        purchase_challan_map.put("invoice_date", Communicator.text_to_date.fromString(tfPurOrdToChallPurOrdToChallanDate.getText()).toString());

        purchase_challan_map.put("transaction_date", Communicator.text_to_date.fromString(tfPurOrdToChallTranxDate.getText()).toString());

        purchase_challan_map.put("newReference", "false");

        purchase_challan_map.put("invoice_no", tfPurOrdToChallChallanNo.getText());

        purchase_challan_map.put("purchase_id", cmbPurOrdToChallPurchaAc.getValue().getId());

        purchase_challan_map.put("purchase_sr_no", tfPurOrdToChallPurSerialNo.getText());

        purchase_challan_map.put("narration", tfPurOrdToChallNarrtion.getText());

        purchase_challan_map.put("supplier_code_id", ledger_id);

        purchase_challan_map.put("isRoundOffCheck", String.valueOf(chbPurOrdToChallRoundOff.isSelected()));

        purchase_challan_map.put("roundoff", lblPurOrdToChallRoundOff.getText());

        purchase_challan_map.put("totalamt", lblPurOrdToChallTotalBillAmount.getText());

        purchase_challan_map.put("total_purchase_discount_amt", lblPurOrdToChallTotalDiscount.getText());

        purchase_challan_map.put("gstNo", gst_no);

        purchase_challan_map.put("tcs_per", "0.00");

        purchase_challan_map.put("tcs_amt", "0.00");

        purchase_challan_map.put("tcs_mode", "");

        purchase_challan_map.put("purchase_discount", tfPurOrdToChallAllRowDisPer.getText().isEmpty() || tfPurOrdToChallAllRowDisPer == null ? "0.0" : tfPurOrdToChallAllRowDisPer.getText());

        purchase_challan_map.put("purchase_discount_amt", tfPurOrdToChallAllRowDisAmt.getText().isEmpty() || tfPurOrdToChallAllRowDisAmt == null ? "0.0" : tfPurOrdToChallAllRowDisAmt.getText());

        purchase_challan_map.put("additionalChargesTotal", tfPurOrdToChallanAddChgAmt.getText().isEmpty() ? "0" : tfPurOrdToChallanAddChgAmt.getText());

        purchase_challan_map.put("additionalCharges", addChargesDTOList.toString());
        purchase_challan_map.put("reference_po_ids", purchase_id);
        purchase_challan_map.put("reference", edit_response_map.get("reference_type").toString());
        purchase_challan_map.put("reference_type", edit_response_map.get("reference_type").toString());
//        purchase_challan_map.put("additionalCharges", addChargesDTOList.toString());


        List<PurchaseInvoiceTable> list = tblvPurOrdToChallCmpTRow.getItems();

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

        purchase_challan_map.put("total_row_gross_amt", String.valueOf(total_row_gross_amt));

        purchase_challan_map.put("row", list.toString());

        System.out.println(purchase_challan_map);
        APIClient apiClient = null;
        try {
            String formData = Globals.mapToStringforFormData(purchase_challan_map);

            System.out.println("hello In Create");
            apiClient = new APIClient(EndPoints.CREATE_PURCHASE_CHALLAN, formData, RequestType.FORM_DATA);


            apiClient.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
                @Override
                public void handle(WorkerStateEvent workerStateEvent) {
                    jsonObject = new Gson().fromJson(workerStateEvent.getSource().getValue().toString(), JsonObject.class);
                    System.out.println("jsonObject >> : " + jsonObject);
                    message = jsonObject.get("message").getAsString();
                    int status = jsonObject.get("responseStatus").getAsInt();
                    if (status == 200) {
                        //Successfully Alert Popup After Edit and Redirect to List Page
                        AlertUtility.AlertSuccessTimeout(AlertUtility.alertTypeSuccess, message, input -> {
                            GlobalController.getInstance().addTabStatic(PURCHASE_CHALLAN_LIST_SLUG, false);
                        });
                    }
                }
            });
            apiClient.setOnCancelled(new EventHandler<WorkerStateEvent>() {
                @Override
                public void handle(WorkerStateEvent workerStateEvent) {
//                    creditNoteLogger.error("Network API cancelled in createCreditNote()" + workerStateEvent.getSource().getValue().toString());

                }
            });
            apiClient.setOnFailed(new EventHandler<WorkerStateEvent>() {
                @Override
                public void handle(WorkerStateEvent workerStateEvent) {
//                    creditNoteLogger.error("Network API failed in createCreditNote()" + workerStateEvent.getSource().getValue().toString());
                }
            });
            apiClient.start();
        } catch (Exception e) {
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            String exceptionAsString = sw.toString();
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

        if (ledgerStateCode.equalsIgnoreCase(GlobalTranx.getCompanyStateCode().toString())) {
            taxFlag = true;
        }

        return ledgerName;
    }


    /**
     * Info :   For showing the details of product after selection of product
     *
     * @Author : Vinit Chilaka
     */
    private void tranxProductDetailsFun(String id) {
        //todo: activating the product tab
        tranxPurOrdToChallanTabPane.getSelectionModel().select(tabPurOrdToChallanProduct);

        APIClient apiClient = null;
        try {
            Map<String, String> map = new HashMap<>();
            map.put("product_id", id);
            String formData = Globals.mapToStringforFormData(map);
//        System.out.println("FormData: " + formData);
            apiClient = new APIClient(EndPoints.TRANSACTION_PRODUCT_DETAILS, formData, RequestType.FORM_DATA);
            apiClient.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
                @Override
                public void handle(WorkerStateEvent workerStateEvent) {
                    jsonObject = new Gson().fromJson(workerStateEvent.getSource().getValue().toString(), JsonObject.class);
//                    System.out.println("jsonObject Res ==>" + jsonObject);
                    if (jsonObject.get("responseStatus").getAsInt() == 200) {
                        JsonObject productJsonObj = jsonObject.getAsJsonObject("result");

                        txtPurOrdToChallProductBrand.setText(productJsonObj.get("brand").getAsString());
                        txtPurOrdToChallProductGroup.setText(productJsonObj.get("group").getAsString());
                        txtPurOrdToChallProductSubGroup.setText(productJsonObj.get("subgroup").getAsString());
                        txtPurOrdToChallProductCategory.setText(productJsonObj.get("category").getAsString());
                        txtPurOrdToChallProductHsn.setText(productJsonObj.get("hsn").getAsString());
                        txtPurOrdToChallProductTaxType.setText(productJsonObj.get("tax_type").getAsString());
                        txtPurOrdToChallProductTaxPer.setText(productJsonObj.get("tax_per").getAsString());
                        txtPurOrdToChallProductMarginPer.setText(productJsonObj.get("margin_per").getAsString());
                        txtPurOrdToChallProductCost.setText(productJsonObj.get("cost").getAsString());
                        txtPurOrdToChallProductShelfId.setText(productJsonObj.get("shelf_id").getAsString());
                        txtPurOrdToChallProductMinStock.setText(productJsonObj.get("min_stocks").getAsString());
                        txtPurOrdToChallProductMaxStock.setText(productJsonObj.get("max_stocks").getAsString());
                    }
                }
            });
            apiClient.setOnCancelled(new EventHandler<WorkerStateEvent>() {
                @Override
                public void handle(WorkerStateEvent workerStateEvent) {
                    purChallanLogger.error("Network API cancelled in tranxProductDetailsFun()" + workerStateEvent.getSource().getValue().toString());
                }
            });
            apiClient.setOnFailed(new EventHandler<WorkerStateEvent>() {
                @Override
                public void handle(WorkerStateEvent workerStateEvent) {
                    purChallanLogger.error("Network API failed in tranxProductDetailsFun()" + workerStateEvent.getSource().getValue().toString());
                }
            });
            apiClient.start();
        } catch (Exception e) {
            System.out.println("exception:" + e);
        } finally {
            apiClient = null;
        }


    }

    /**
     * Info :  For showing the details of ledger after selection of ledger.
     *
     * @Author : Vinit Chilaka
     */
    private void tranxLedgerDetailsFun(String id) {
        //todo: activating the product tab
        tranxPurOrdToChallanTabPane.getSelectionModel().select(tabPurOrdToChallanLedger);

        APIClient apiClient = null;
        try {
            Map<String, String> map = new HashMap<>();
            map.put("ledger_id", id);
            String formData = Globals.mapToStringforFormData(map);
//        System.out.println("FormData: " + formData);
            apiClient = new APIClient(EndPoints.TRANSACTION_LEDGER_DETAILS, formData, RequestType.FORM_DATA);
            apiClient.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
                @Override
                public void handle(WorkerStateEvent workerStateEvent) {
                    jsonObject = new Gson().fromJson(workerStateEvent.getSource().getValue().toString(), JsonObject.class);
//                    System.out.println("jsonObject Res ==>" + jsonObject);
                    if (jsonObject.get("responseStatus").getAsInt() == 200) {
                        JsonObject jsonObj = jsonObject.getAsJsonObject("result");
//                        System.out.println("jsonObj " + jsonObj);
                        CompanyStateCode = jsonObject.get("company_state_code").getAsLong();
                        stateCode = jsonObj.get("stateCode").getAsLong();
                        // setting values
                        txtPurOrdToChallanGstNo.setText(jsonObj.get("gst_number").getAsString());
                        txtPurOrdToChallanArea.setText(jsonObj.get("area").getAsString());
                        txtPurOrdToChallanBank.setText(jsonObj.get("bank_name").getAsString());
                        txtPurOrdToChallanContactPerson.setText(jsonObj.get("contact_name").getAsString());
                        txtPurOrdToChallanCreditDays.setText(jsonObj.get("credit_days").getAsString());
                        txtPurOrdToChallanFssai.setText(jsonObj.get("fssai_number").getAsString());
                        txtPurOrdToChallanLicenseNo.setText(jsonObj.get("license_number").getAsString());
                        txtPurOrdToChallanRoute.setText(jsonObj.get("route").getAsString());
                    }
                }
            });
            apiClient.setOnCancelled(new EventHandler<WorkerStateEvent>() {
                @Override
                public void handle(WorkerStateEvent workerStateEvent) {
                    purChallanLogger.error("Network API cancelled in tranxLedgerDetailsFun()" + workerStateEvent.getSource().getValue().toString());
                }
            });
            apiClient.setOnFailed(new EventHandler<WorkerStateEvent>() {
                @Override
                public void handle(WorkerStateEvent workerStateEvent) {
                    purChallanLogger.error("Network API failed in tranxLedgerDetailsFun()" + workerStateEvent.getSource().getValue().toString());
                }
            });
            apiClient.start();
        } catch (Exception e) {
            System.out.println("exception:" + e);
        } finally {
            apiClient = null;
        }


    }

    /**
     * Info :  Set the SUpplier Details of the Selected Product Bottom in Supplier Table
     *
     * @Author : Vinit Chilaka
     */
    private void getSupplierListbyProductId(String id) {
        APIClient apiClient = null;
        try {
            Map<String, String> map = new HashMap<>();
            map.put("productId", id);
            String formData = Globals.mapToStringforFormData(map);
            apiClient = new APIClient(EndPoints.GET_SUPPLIERLIST_BY_PRODUCTID_ENDPOINT, formData, RequestType.FORM_DATA);
            apiClient.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
                @Override
                public void handle(WorkerStateEvent workerStateEvent) {
                    jsonObject = new Gson().fromJson(workerStateEvent.getSource().getValue().toString(), JsonObject.class);
                    if (jsonObject.get("responseStatus").getAsInt() == 200) {
                        JsonArray dataArray = jsonObject.getAsJsonArray("data");
                        System.out.println("dataArray >> : " + dataArray);
                        //todo: getting values
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
                        tcPCInvPrdHistSupplierName.setCellValueFactory(new PropertyValueFactory<>("supplierName"));
                        tcPCInvPrdHistInvNo.setCellValueFactory(new PropertyValueFactory<>("supplierInvNo"));
                        tcPCInvPrdHistInvDate.setCellValueFactory(new PropertyValueFactory<>("supplierInvDate"));
                        tcPCInvPrdHistBatch.setCellValueFactory(new PropertyValueFactory<>("supplierBatch"));
                        tcPCInvPrdHistMRP.setCellValueFactory(new PropertyValueFactory<>("supplierMrp"));
                        tcPCInvPrdHistQty.setCellValueFactory(new PropertyValueFactory<>("supplierQty"));
                        tcPCInvPrdHistRate.setCellValueFactory(new PropertyValueFactory<>("supplierRate"));
                        tcPCInvPrdHistCost.setCellValueFactory(new PropertyValueFactory<>("supplierCost"));
                        tcPCInvPrdHistDisPer.setCellValueFactory(new PropertyValueFactory<>("supplierDisPer"));
                        tcPCInvPrdHistDisAmt.setCellValueFactory(new PropertyValueFactory<>("supplierDisAmt"));

//                        tvPurOrderSupplierDetails.setItems(supplierDataList);

                    }


                }
            });
            apiClient.setOnCancelled(new EventHandler<WorkerStateEvent>() {
                @Override
                public void handle(WorkerStateEvent workerStateEvent) {
                    purChallanLogger.error("Network API cancelled in getSupplierListbyProductId()" + workerStateEvent.getSource().getValue().toString());
                }
            });
            apiClient.setOnFailed(new EventHandler<WorkerStateEvent>() {
                @Override
                public void handle(WorkerStateEvent workerStateEvent) {
                    purChallanLogger.error("Network API failed in getSupplierListbyProductId()" + workerStateEvent.getSource().getValue().toString());
                }
            });
            apiClient.start();
        } catch (Exception e) {
            System.out.println("exception:" + e);
        } finally {
            apiClient = null;
        }
    }

    /**
     * Info :  For showing the details of Batch after Creating the Batch.
     *
     * @Author : Vinit Chilaka
     */
    private void tranxBatchDetailsFun(String bNo, String bId) {
        APIClient apiClient = null;
        tranxPurOrdToChallanTabPane.getSelectionModel().select(tabPurOrdToChallanBatch);
        purChallanLogger.debug("Get Supplier List Data Started...");
        System.out.println("In Batch FUN >> " + bNo + " >> " + bId);
        Map<String, String> map = new HashMap<>();
        map.put("batchNo", bNo);
        map.put("id", bId);
//        requestData.append("batchNo", batchNo.batch_no);
//        requestData.append("id", batchNo.b_details_id);
        String formData = Globals.mapToStringforFormData(map);
        System.out.println("FormData: " + formData);
//        HttpResponse<String> response = APIClient.postFormDataRequest(formData, EndPoints.GET_SUPPLIERLIST_BY_PRODUCTID_ENDPOINT);
        apiClient = new APIClient(EndPoints.GET_BATCH_DETAILS_ENDPOINT, formData, RequestType.FORM_DATA);

        apiClient.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent workerStateEvent) {
                JsonObject jsonObject = new Gson().fromJson(workerStateEvent.getSource().getValue().toString(), JsonObject.class);
//                System.out.println("jsonObject =>" + jsonObject);
                if (jsonObject.get("responseStatus").getAsInt() == 200) {
                    JsonObject batchJsonObj = jsonObject.getAsJsonObject("response");

                    txtPurOrdToChallBatchBatch.setText(batchJsonObj.get("batchNo").getAsString());
                    txtPurOrdToChallBatchMRP.setText(batchJsonObj.get("batchMrp").getAsString());
                    txtPurOrdToChallBatchHMfgDt.setText(DateConvertUtil.convertDispDateFormat(batchJsonObj.get("mfgDate").getAsString()));
                    txtPurOrdToChallBatchExpDt.setText(DateConvertUtil.convertDispDateFormat(batchJsonObj.get("expDate").getAsString()));
                    txtPurOrdToChallBatchPurRate.setText(batchJsonObj.get("pur_rate").getAsString());
                    txtPurOrdToChallBatchAvlStk.setText(batchJsonObj.get("qty").getAsString());
                    txtPurOrdToChallBatchDisPer.setText(batchJsonObj.get("b_dis_per").getAsString());
                    txtPurOrdToChallBatchDisAmt.setText(batchJsonObj.get("b_dis_amt").getAsString());
                    txtPurOrdToChallBatchSaleRate.setText(batchJsonObj.get("sales_rate").getAsString());
                    txtPurOrdToChallBatchFSRMH.setText(batchJsonObj.get("fsrmh").getAsString());
                    txtPurOrdToChallBatchFSRAI.setText(batchJsonObj.get("fsrai").getAsString());
                    txtPurOrdToChallBatchCSRMH.setText(batchJsonObj.get("csrmh").getAsString());
                    txtPurOrdToChallBatchCSRAI.setText(batchJsonObj.get("csrai").getAsString());
                    txtPurOrdToChallBatchCSRAI.setText(batchJsonObj.get("csrai").getAsString());

                }
            }
        });
        apiClient.setOnCancelled(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent workerStateEvent) {
                purChallanLogger.error("Network API cancelled in getSupplierListbyProductId()" + workerStateEvent.getSource().getValue().toString());

            }
        });

        apiClient.setOnFailed(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent workerStateEvent) {
                purChallanLogger.error("Network API failed in getSupplierListbyProductId()" + workerStateEvent.getSource().getValue().toString());
            }
        });
        apiClient.start();
        purChallanLogger.debug("Get Supplier List Data End...");

    }

    public void validatePurchaseChallan() {
        APIClient apiClient = null;
        try {

            Map<String, String> map = new HashMap<>();
            map.put("supplier_id", ledger_id);
            map.put("bill_no", String.valueOf(tfPurOrdToChallChallanNo.getText()));
            String formData = Globals.mapToStringforFormData(map);
            System.out.println("for,madata >>>  " + formData);
            apiClient = new APIClient(EndPoints.VALIDATE_PURCHASE_CHALLAN, formData, RequestType.FORM_DATA);
            apiClient.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
                @Override
                public void handle(WorkerStateEvent workerStateEvent) {
                    JsonObject jsonObject = new Gson().fromJson(workerStateEvent.getSource().getValue().toString(), JsonObject.class);
                    System.out.println(" jsonObject : >> " + jsonObject);
                    if (jsonObject.get("responseStatus").getAsInt() != 200) {
                        String message = jsonObject.get("message").getAsString();
                        AlertUtility.AlertWarningTimeout(AlertUtility.alertTypeError, message, in -> {
                            tfPurOrdToChallChallanNo.requestFocus();
                        });
                    }
                }
            });
            apiClient.setOnCancelled(new EventHandler<WorkerStateEvent>() {
                @Override
                public void handle(WorkerStateEvent workerStateEvent) {
                    purChallanLogger.error("Network API cancelled in functionCompanyDuplicate()" + workerStateEvent.getSource().getValue().toString());
                }
            });
            apiClient.setOnFailed(new EventHandler<WorkerStateEvent>() {
                @Override
                public void handle(WorkerStateEvent workerStateEvent) {
                    purChallanLogger.error("Network API failed in functionCompanyDuplicate()" + workerStateEvent.getSource().getValue().toString());
                }
            });
            apiClient.start();
        } catch (Exception e) {
            e.printStackTrace();
            //TODO : use logger for alert messages
//            showAlert("Falied to connect server! ");
//            AlertUtility.AlertDialogForError("WARNING", "Failed to Connect Server !", input -> {
//                if (input) {
//                }
//            });
            AlertUtility.AlertErrorTimeout(AlertUtility.alertTypeError, "Failed to Connect Server !", in -> {
            });
        } finally {
            apiClient = null;
        }
    }

    public void validatePurchaseChallanUpdate() {
        APIClient apiClient = null;
        try {

            Map<String, String> map = new HashMap<>();
            map.put("challan_id", String.valueOf(purChallEditId));
            map.put("supplier_id", ledger_id);
            map.put("bill_no", String.valueOf(tfPurOrdToChallChallanNo.getText()));
            String formData = Globals.mapToStringforFormData(map);
            apiClient = new APIClient(EndPoints.VALIDATE_PURCHASE_CHALLAN_UPDATE, formData, RequestType.FORM_DATA);
            apiClient.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
                @Override
                public void handle(WorkerStateEvent workerStateEvent) {
                    JsonObject jsonObject = new Gson().fromJson(workerStateEvent.getSource().getValue().toString(), JsonObject.class);
                    if (jsonObject.get("responseStatus").getAsInt() != 200) {
                        String message = jsonObject.get("message").getAsString();
                        AlertUtility.AlertWarningTimeout(AlertUtility.alertTypeError, message, in -> {
                            tfPurOrdToChallChallanNo.requestFocus();
                        });
                    }
                }
            });
            apiClient.setOnCancelled(new EventHandler<WorkerStateEvent>() {
                @Override
                public void handle(WorkerStateEvent workerStateEvent) {
                    purChallanLogger.error("Network API cancelled in functionCompanyDuplicate()" + workerStateEvent.getSource().getValue().toString());
                }
            });
            apiClient.setOnFailed(new EventHandler<WorkerStateEvent>() {
                @Override
                public void handle(WorkerStateEvent workerStateEvent) {
                    purChallanLogger.error("Network API failed in functionCompanyDuplicate()" + workerStateEvent.getSource().getValue().toString());
                }
            });
            apiClient.start();
        } catch (Exception e) {
            e.printStackTrace();
            //TODO : use logger for alert messages
//            showAlert("Falied to connect server! ");
//            AlertUtility.AlertDialogForError("WARNING", "Failed to Connect Server !", input -> {
//                if (input) {
//                }
//            });
            AlertUtility.AlertErrorTimeout(AlertUtility.alertTypeError, "Failed to Connect Server !", in -> {
            });
        } finally {
            apiClient = null;
        }
    }

    private void tfChallanNoValidatition() {
        tfPurOrdToChallChallanNo.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal) {
                if (tfPurOrdToChallChallanNo.getText().isEmpty()) {
                    tfPurOrdToChallChallanNo.requestFocus();
                } else {
                    String btText = btnPurOrdToChallFinalSubmit.getText();
                    if (btText.equalsIgnoreCase("Submit")) {
                        validatePurchaseChallan();
                    } else {
                        validatePurchaseChallanUpdate();
                    }
                }

            }

        });
    }


    /************************Conversion Started ***********************/

    private void getPurOrderToPurChallById(String purchase_id) {

        try {
            Map<String, String> map = new HashMap<>();
            map.put("purchase_order_id", String.valueOf(purchase_id));
            String formData = mapToStringforFormData(map);
            System.out.println("Hello FormData >> : " + formData);
            HttpResponse<String> response = APIClient.postFormDataRequest(formData, EndPoints.GET_PUR_ORD_TO_CHALL_BY_ID);
            PurOrdToChallMainDTO purchaseEditResponseDTO = new Gson().fromJson(response.body(), PurOrdToChallMainDTO.class);
            System.out.println("res update: " + purchaseEditResponseDTO);

            if (purchaseEditResponseDTO.getResponseStatus() == 200) {
                setDataPurOrdToPurChall(purchaseEditResponseDTO);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    // Setting Data to the fields
    private void setDataPurOrdToPurChall(PurOrdToChallMainDTO purchaseEditResponseDTO) {
//        System.out.println("Hello Called");
        tfPurOrdToChallChallanNo.setText(purchaseEditResponseDTO.getInvoiceData().getPurchaseOrder());
        tfPurOrdToChallNarrtion.setText(purchaseEditResponseDTO.getInvoiceData().getNarration());
        ledger_id = String.valueOf(purchaseEditResponseDTO.getInvoiceData().getSupplierId());
        tranxLedgerDetailsFun(ledger_id);

        edit_response_map.put("discountLedgerId", purchaseEditResponseDTO.getDiscountLedgerId());
        edit_response_map.put("discountInAmt", purchaseEditResponseDTO.getDiscountInAmt());
        edit_response_map.put("discountInPer", purchaseEditResponseDTO.getDiscountInPer());
        edit_response_map.put("totalPurchaseDiscountAmt", purchaseEditResponseDTO.getTotalPurchaseDiscountAmt());
        edit_response_map.put("reference_type", purchaseEditResponseDTO.getReferenceType());

        rowListDTOS.addAll(purchaseEditResponseDTO.getRow());
        addChargesResLst.addAll(purchaseEditResponseDTO.getAdditionalCharges());

        edit_response_map.put("invoiceData", purchaseEditResponseDTO.getInvoiceData());
        edit_response_map.put("additionalCharges", purchaseEditResponseDTO.getAdditionalCharges());

        PurOrdToChallIvoiceData invoiceDataResDTO = purchaseEditResponseDTO.getInvoiceData();
        invoice_data_map.put("id", String.valueOf(invoiceDataResDTO.getId()));
        System.out.println("Helllo >> Invoice Date >> " + invoiceDataResDTO.getInvoiceDt());
        invoice_data_map.put("invoice_dt", String.valueOf(invoiceDataResDTO.getInvoiceDt()));
        invoice_data_map.put("invoice_no", String.valueOf(invoiceDataResDTO.getPurchaseOrder()));
        invoice_data_map.put("purchase_account_ledger_id", String.valueOf(invoiceDataResDTO.getPurchaseId()));
        invoice_data_map.put("purchase_name", String.valueOf(invoiceDataResDTO.getPurchaseName()));
        invoice_data_map.put("supplierId", String.valueOf(invoiceDataResDTO.getSupplierId()));
        ledger_id = invoice_data_map.get("supplierId");
        invoice_data_map.put("supplier_name", String.valueOf(invoiceDataResDTO.getSupplierName()));
        invoice_data_map.put("po_sr_no", String.valueOf(invoiceDataResDTO.getPoSrNo()));
        invoice_data_map.put("gstNo", String.valueOf(invoiceDataResDTO.getGstNo()));
        invoice_data_map.put("po_date", String.valueOf(invoiceDataResDTO.getPiTransactionDt()));
        invoice_data_map.put("transport_name", String.valueOf(invoiceDataResDTO.getTransportName()));
        invoice_data_map.put("narration", String.valueOf(invoiceDataResDTO.getNarration()));
        invoice_data_map.put("reference", String.valueOf(invoiceDataResDTO.getReference()));

    }


}

class TextFieldTableCellForPurOrdToChallan extends TableCell<PurchaseInvoiceTable, String> {
    private TextField textField;
    private String columnName;

    TableCellCallback<Object[]> callback;
    TableCellCallback<Object[]> productID_callback;


    public TextFieldTableCellForPurOrdToChallan(String columnName, TableCellCallback<Object[]> callback, TableCellCallback<Object[]> productID_callback) {
        this.columnName = columnName;
        this.textField = new TextField();

        this.callback = callback;
        this.productID_callback = productID_callback;
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

        batchColumn();

        netAmountColumn();

    }


    public TextFieldTableCellForPurOrdToChallan(String columnName, TableCellCallback<Object[]> callback) {
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

        batchColumn();

        netAmountColumn();

    }


    private void batchColumn() {
        if ("tblcPurOrdToChallCmpTRowBatchNo".equals(columnName)) {
            textField.setEditable(false);
            textField.setOnKeyPressed(event -> {
                if (event.getCode() == KeyCode.SPACE) {
                    openBatchWindow();
                }
            });

            textField.setOnMouseClicked(event -> {
                System.out.println("hello");
                openBatchWindow();
            });
        }
    }

    private void particularsColumn() {
        if ("tblcPurOrdToChallCmpTRowParticulars".equals(columnName)) {
            textField.setEditable(false);
            textField.setOnKeyPressed(event -> {
                if (event.getCode() == KeyCode.SPACE) {
                    openProduct();
                }
            });

            textField.setOnMouseClicked(event -> {
                openProduct();
            });
        }
    }

    private void netAmountColumn() {

        if ("tblcPurOrdToChallCmpTRowNetAmt".equals(columnName)) {
            textField.setEditable(false);
            textField.setOnKeyPressed(event -> {
                if (event.getCode() == KeyCode.ENTER) {
                    System.out.println(getTableRow().getItem().getNet_amount());

                    int current_index = getTableRow().getIndex();

                    getTableView().getItems().addAll(new PurchaseInvoiceTable("", String.valueOf(current_index + 1), "1", "", "", "", "", "", "", "", "", "", "", "", "", "", "", ""));
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

            if (productID_callback != null) {
                Object[] object = new Object[1];
                object[0] = productId;
                productID_callback.call(object);
            }

            System.out.println("packaging >> :" + packaging);
            getTableRow().getItem().setParticulars(productName);
            getTableRow().getItem().setProduct_id(productId);
            getTableRow().getItem().setPackages(packaging);
            getTableRow().getItem().setIs_batch(is_batch);
            getTableRow().getItem().setTax(taxper);

            List<LevelAForPurInvoice> levelAForPurInvoiceList = ProductUnitsPacking.getAllProductUnitsPackingFlavour(productId);
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


        });
    }

    public void openBatchWindow() {
        int current_index = getTableRow().getIndex();
        System.out.println("current index >> :" + current_index);
        List<PurchaseInvoiceTable> tableList = getTableView().getItems();

        List<BatchWindowTableDTO> batchWindowTableDTOList;
        PurchaseInvoiceTable current_table_row = tableList.get(current_index);
        current_table_row.setCurrent_index(String.valueOf(current_index));
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
        /**** get the PTR & CSR Rates using current row index from table ****/
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
        System.out.println("current_table_row >> " + current_table_row.getIs_batch());
        if (current_table_row.getIs_batch().equals("true") && ledgerid != 0) {
            BatchWindow.batchWindow(String.valueOf(current_index), product_details_to_batch_window, batchWindowTableDTOList, input -> {
                System.out.println("input" + input);
                getTableView().getItems().get(current_index).setBatchWindowTableDTOList(input);
                System.out.println("current Row" + getTableView().getItems().get(current_index));


//                System.out.println("table batch: "+getTableView().getItems().get(Integer.parseInt(input.get(0).getCurrentIndex())).getBatchWindowTableDTOList());
//
                for (BatchWindowTableDTO batchWindowTableDTOSs : input) {
                    getTableView().getItems().get(current_index).setBatch_or_serial(batchWindowTableDTOSs.getBatchNo());
                    getTableView().getItems().get(current_index).setQuantity(batchWindowTableDTOSs.getQuantity());
                    getTableView().getItems().get(current_index).setRate(batchWindowTableDTOSs.getPurchaseRate());
                    getTableView().getItems().get(current_index).setDis_per(batchWindowTableDTOSs.getDiscountPercentage());
                    getTableView().getItems().get(current_index).setDis_amt(batchWindowTableDTOSs.getDiscountAmount());


                    getTableView().getItems().get(current_index).setB_details_id(batchWindowTableDTOSs.getB_details_id());

                    getTableView().getItems().get(current_index).setB_no(batchWindowTableDTOSs.getBatchNo());

                    getTableView().getItems().get(current_index).setB_rate(batchWindowTableDTOSs.getMrp());

                    getTableView().getItems().get(current_index).setB_purchase_rate(batchWindowTableDTOSs.getPurchaseRate());


                    getTableView().getItems().get(current_index).setRate_a(batchWindowTableDTOSs.getFsr_mh());
                    getTableView().getItems().get(current_index).setRate_b(batchWindowTableDTOSs.getFsr_ai());
                    getTableView().getItems().get(current_index).setRate_c(batchWindowTableDTOSs.getCsr_mh());
                    getTableView().getItems().get(current_index).setRate_d(batchWindowTableDTOSs.getCsr_ai());


                    getTableView().getItems().get(current_index).setMin_margin(batchWindowTableDTOSs.getMargin());

                    if (batchWindowTableDTOSs.getExpiryDate() != null && !batchWindowTableDTOSs.getExpiryDate().isEmpty()) {
                        getTableView().getItems().get(current_index).setB_expiry(String.valueOf(Communicator.text_to_date.fromString(batchWindowTableDTOSs.getExpiryDate())));
                    } else {
                        getTableView().getItems().get(current_index).setB_expiry("");
                    }

                    if (batchWindowTableDTOSs.getManufacturingDate() != null && !batchWindowTableDTOSs.getManufacturingDate().isEmpty()) {
                        getTableView().getItems().get(current_index).setManufacturing_date(String.valueOf(Communicator.text_to_date.fromString(batchWindowTableDTOSs.getManufacturingDate())));
                    } else {
                        getTableView().getItems().get(current_index).setManufacturing_date("");
                    }

                }
                String batchNo = getTableView().getItems().get(current_index).getBatch_or_serial();
                String bDetailsId = getTableView().getItems().get(current_index).getB_details_id();
                if (productID_callback != null) {
                    Object[] object = new Object[2];
                    object[0] = batchNo;
                    object[1] = bDetailsId;
                    productID_callback.call(object);
                }
                //?Row Level Calculation
                PurchaseInvoiceCalculation.rowCalculationForPurcInvoice(current_index, getTableView(), callback);

                //? Calculate Tax for each Row
                PurchaseInvoiceCalculation.calculateGst(getTableView(), callback);
                ///

//                System.out.println("current index >> :"+ getTableView().getItems().size());
//                Integer numRows = getTableView().getItems().size();
//                System.out.println("New Row Check index >> :" +  (getTableView().getItems().get(numRows - 1).getParticulars().isEmpty() ? "hello ": "hiiii"));
////                if(!getTableView().getItems().get(current_index).getParticulars().isEmpty()
////                || !getTableView().getItems().get(numRows - 1).getParticulars().isEmpty() ) {
//                    Integer srNo = Integer.parseInt(getTableView().getItems().get(Integer.parseInt(input.get(0).getCurrentIndex())).getSr_no()) + 1;
//                    Integer crIndex = Integer.parseInt(getTableView().getItems().get(Integer.parseInt(input.get(0).getCurrentIndex())).getCurrent_index()) + 1;
//                    getTableView().getItems().addAll(new PurchaseInvoiceTable("", String.valueOf(crIndex), String.valueOf(srNo), "", "", "", "", "", "",
//                            "", "", "", "", "", "", "", "", ""));
//                    getTableView().getItems().get(Integer.parseInt(input.get(0).getCurrentIndex()));
////                }
            });
        }
    }


    public void textfieldStyle() {
        if (columnName.equals("tblcPurOrdToChallCmpTRowParticulars")) {
            textField.setPromptText("Particulars");
            textField.setEditable(false);
        } else {
            textField.setPromptText("0");
        }

        textField.setPrefHeight(38);
        textField.setMaxHeight(38);
        textField.setMinHeight(38);

        if (columnName.equals("tblcPurOrdToChallCmpTRowParticulars")) {
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


        if (columnName.equals("tblcPurOrdToChallCmpTRowNetAmt")) {
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
        if (item != null && columnName.equals("tblcPurOrdToChallCmpTRowParticulars")) {
            ((PurchaseInvoiceTable) item).setParticulars(newValue.isEmpty() ? "" : newValue);
        } else if (columnName.equals("tblcPurOrdToChallCmpTRowBatchNo")) {
            (getTableRow().getItem()).setBatch_or_serial(newValue.isEmpty() ? "" : newValue);
        } else if (columnName.equals("tblcPurOrdToChallCmpTRowQuantity")) {
            (getTableRow().getItem()).setQuantity(newValue.isEmpty() ? "0" : newValue);
        } else if (columnName.equals("tblcPurOrdToChallCmpTRowRate")) {
            (getTableRow().getItem()).setRate(newValue.isEmpty() ? "0.0" : newValue);
        } else if (columnName.equals("tblcPurOrdToChallCmpTRowGrossAmt")) {
            (getTableRow().getItem()).setGross_amount(newValue.isEmpty() ? "0.0" : newValue);
        } else if (columnName.equals("tblcPurOrdToChallCmpTRowDisPer")) {
            (getTableRow().getItem()).setDis_per(newValue.isEmpty() ? "0.0" : newValue);
        } else if (columnName.equals("tblcPurOrdToChallCmpTRowDisAmt")) {
            (getTableRow().getItem()).setDis_amt(newValue.isEmpty() ? "0.0" : newValue);
        } else if (columnName.equals("tblcPurOrdToChallCmpTRowTaxPer")) {
            (getTableRow().getItem()).setTax(newValue.isEmpty() ? "0.0" : newValue);
        } else if (columnName.equals("tblcPurOrdToChallCmpTRowNetAmt")) {
            (getTableRow().getItem()).setNet_amount(newValue.isEmpty() ? "0.0" : newValue);
        }

    }
}









