package com.opethic.genivis.controller.tranx_sales;

import com.google.gson.*;
import com.opethic.genivis.GenivisApplication;
import com.opethic.genivis.controller.dialogs.AdditionalCharges;
//import com.opethic.genivis.controller.tranx_purchase.ComboBoxTableCellSalesChallanForUnit;
//import com.opethic.genivis.controller.tranx_purchase.ProductUnitsPacking;
import com.opethic.genivis.controller.master.ledger.common.LedgerMessageConsts;
import com.opethic.genivis.controller.tranx_purchase.PurchaseInvoiceListController;
import com.opethic.genivis.controller.tranx_sales.common.TranxCommonPopUps;
//import com.opethic.genivis.controller.tranx_sales.invoice.SalesInvoiceCalculation;
//import com.opethic.genivis.controller.tranx_sales.invoice.SalesProductUnitsPacking;
import com.opethic.genivis.dto.cmp_t_row.BatchWindowTableDTO;
import com.opethic.genivis.dto.pur_invoice.reqres.AddChargesListDTO;
import com.opethic.genivis.dto.pur_invoice.reqres.SalesRowListDTO;
import com.opethic.genivis.dto.reqres.product.TableCellCallback;
import com.opethic.genivis.controller.dialogs.SingleInputDialogs;
import com.opethic.genivis.controller.tranx_purchase.PurchaseChallanController;
import com.opethic.genivis.dto.*;
import com.opethic.genivis.dto.add_charges.CustomAddChargesDTO;
import com.opethic.genivis.dto.pur_invoice.*;
import com.opethic.genivis.dto.reqres.product.Communicator;
//import com.opethic.genivis.dto.reqres.product.TableCellCallback;
import com.opethic.genivis.dto.reqres.pur_tranx.PurTranxToProductRedirectionDTO;
import com.opethic.genivis.dto.reqres.pur_tranx.TranxPurRowDTO;
import com.opethic.genivis.dto.reqres.sales_tranx.*;
import com.opethic.genivis.dto.reqres.tranx.sales.invoice.TranxRowConvRes;
import com.opethic.genivis.dto.tranx_sales.CmpTRowDTOSoToSc;
import com.opethic.genivis.models.tranx.sales.TranxRow;
import com.opethic.genivis.models.tranx.sales.TranxSelectedBatch;
import com.opethic.genivis.models.tranx.sales.TranxSelectedProduct;
import com.opethic.genivis.network.APIClient;
import com.opethic.genivis.network.RequestType;
import com.opethic.genivis.utils.*;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.WorkerStateEvent;
import javafx.css.PseudoClass;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
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
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import org.apache.http.util.TextUtils;
import org.apache.logging.log4j.LogManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.opethic.genivis.network.EndPoints;
import com.google.gson.reflect.TypeToken;

import java.net.URL;
import java.net.http.HttpResponse;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static com.opethic.genivis.utils.FxmFileConstants.*;
import static com.opethic.genivis.utils.Globals.*;
import static javafx.scene.control.PopupControl.USE_COMPUTED_SIZE;
import static javafx.scene.control.PopupControl.USE_PREF_SIZE;

import javafx.scene.input.MouseEvent;

public class SalesChallanCreateController implements Initializable {
    //    @FXML
//    private ToggleButton btnSalesChallanCrEdLedgerInfo, btnSalesChallanCrEdProductInfo, btnSalesChallanCrEdBatchInfo;
    @FXML
    private TableView<SalesOrderSupplierDetailsDTO> tvSalesChallanSupplierDetails;
    private ObservableList<GstDetailsDTO> supplierGSTINList = FXCollections.observableArrayList();
    @FXML
    TabPane myTabPane;
    @FXML
    private HBox topFirstRow, topSecondRow, scBottomMain;
    @FXML
    private VBox scBottomFirstV, scBottomSecondV, scBottomThirdV, scTotalMainDiv, scTotalMainInnerDiv;

    private static final org.apache.logging.log4j.Logger logger = LogManager.getLogger(SalesChallanCreateController.class);


    private String finaltotalSGST = "", finaltotalCGST = "", finaltotalIGST = "", clientGSTIN = "", finalroundOff = "";

    DecimalFormat decimalFormat = new DecimalFormat("0.#");

    String gst_no = "";

    String purchaseAcID = "";

    String supplierGSTIN_ID = "";
    private Integer purchase_id = -1;
    private Integer purPrdRedId = -1;

    String ledgerId = "";
    String salesman_id = "";

    //    private ObservableList<CommonDTO> supplierGSTINList = FXCollections.observableArrayList();
    Map<String, String> purchase_invoice_map = new HashMap<>();

    private ObservableList<String> payment_mode_list = FXCollections.observableArrayList("Credit", "Cash");


    private String ledgerStateCode = "";
    private Boolean taxFlag, isRedirect = false;

    private String totalamt = "";

    private Double total_taxable_amt = 0.0;

    private String total_purchase_discount_amt = "";

    private final List<CmpTRowDTOSoToSc> rowListDTOS = new ArrayList<>();

    @FXML
    private HBox hbPurChallGstTotal;

    @FXML
    Tab tabSalesChallanProduct, tabSalesChallanLedger, tabSalesChallanBatch;

    @FXML
    private StackPane stkpnSalesChallanProductInfo, stkpnSalesChallanLedgerInfo, stkpnSalesChallanaBatchInfo;
    @FXML
    private HBox hbapSalesChallanLedgerWindow, hbapSalesChallanProductWindow, hbapSalesChallanBatchWindow, hbapSalesChallanAddnlChrgWindow, hbapSalesChallanAddnlChrgLedgerWindow;
    @FXML
    private TextField tfSalesChallanLedgerName, tfSalesChallanNo, tfSalesChallanSrNo, tfPurchaseChallanTotalAmt, tfSalesChallanNarrations, sctfaddcharges;

    @FXML
    private TextField dpSalesChallanCreateDate;
    @FXML
    private Button btnSalesChallanChooseFileButton, addRowInBatchWindowRow;
    @FXML
    private Button btnSalesChallanModify, btnSalesChallanCreate, btnSalesChallanCancel;
    @FXML
    private TableView<CmpTRowDTOSoToSc> tblvSalesChallanCmpTRow;

    @FXML
    private Hyperlink hlSaleInvCreateChallan;

    @FXML
    private Hyperlink hlSaleInvCreateOrder;

    @FXML
    private Hyperlink hlSaleInvCreateQuotation;
//    @FXML
//    private TableView<CmpTRowDTOSoToSc> tvCmpTRowDTOSoToSc;


    @FXML
    private TableView<AdditionalChargesRowDTO> tblcSalesChallanAddtnlChrgCmpTRowView;
    @FXML
    private TableColumn<CmpTRowDTOSoToSc, String> tblcSalesChallanCmpTRowSr, tblcSalesChallanCmpTRowFreeQty, tblcSalesChallanCmpTRowAction, tblcSalesChallanCmpTRowParticulars, tblcSalesChallanCmpTRowPacking, tblcSalesChallanCmpTRowUnit, tblcSalesChallanCmpTRowBatchNo, tblcSalesChallanCmpTRowQuantity, tblcSalesChallanCmpTRowRate, tblcSalesChallanCmpTRowGrossAmt, tblcSalesChallanCmpTRowDisPer, tblcSalesChallanCmpTRowDisAmt, tblcSalesChallanCmpTRowTaxPer, tblcSalesChallanCmpTRowNetAmt, tblcSalesChallanCmpTRowA, tblcSalesChallanCmpTRowDiscPerc, tblcSalesChallanCmpTRowB, tblcSalesChallanCmpTRowC;

    @FXML
    private TableColumn<AdditionalChargesRowDTO, String> tblcSalesChallanAddtnlChrgCmpTRowParticulars, tblcSalesChallanAddtnlChrgCmpTRowAmt, tblcSalesChallanAddtnlChrgChargesCmpTRowPercentage, tblcSalesChallanAddtnlChrgCmpTRowAction;
    @FXML
    private TableColumn tcSalesChallanSupplierName,
            tcSalesChallanSupplierInvNo,
            tcSalesChallanSupplierInvDate,
            tcSalesChallanSupplierBatch,
            tcSalesChallanSupplierMrp,
            tcSalesChallanSupplierQty,
            tcSalesChallanSupplierRate,
            tcSalesChallanSupplierCost,
            tcSalesChallanSupplierDisper,
            tcSalesChallanSupplierDisRs;
    private ObservableList<CmpTRowDTO> itemList;
    @FXML
    private Button btnSalesChallanLedgerWindowClose, btnSalesChallanAdditionalLedgerWindowClose, btnSalesChallanPrdWindowClose, btnSalesChallanBatWinClose, btnSalesChallanAddnlChargWindowClose;
    private ToggleGroup toggleGroup = new ToggleGroup();

    @FXML
    private TableView tblvSalesChallanLedWindtableview, tblvSalesChallanAdditionalChargesLedWindtableview, tblvSalesChallananPrdWindtableview;
    @FXML
    private TableColumn tblcSalesChallanLedWindCode, tblcSalesChallanLedWindLedName, tblcSalesChallanLedWindGrp, tblcSalesChallanLedWindContNo, tblcSalesChallanLedWindCurrBal, tblcSalesChallanLedWindType, tblcSalesChallanAdditionalChargesLedWindCode, tblcSalesChallanAdditionalChargesLedWindLedName, tblcSalesChallanAdditionalChargesLedWindGrp, tblcSalesChallanAdditionalChargesLedWindContNo, tblcSalesChallanAdditionalChargesLedWindCurrBal, tblcSalesChallanAdditionalChargesLedWindType, tblcSalesChallanLedWindAction, tblcSalesChallananPrdWindCode, tblcSalesChallananPrdWindPrdName, tblcSalesChallananPrdWindPacking, tblcSalesChallananPrdWindBarcode, tblcSalesChallananPrdWindBrand, tblcSalesChallananPrdWindMrp, tblcSalesChallananPrdWindCurrStk, tblcSalesChallananPrdWindUnit, tblcSalesChallananPrdWindSaleRate, tblcSalesChallananPrdWindAction;
    @FXML
    private TableView<TranxBatchWindowDTO> tblvSalesChallanBatWinCreTableview;
    @FXML
    private TableColumn<TranxBatchWindowDTO, String> tblcSalesChallanBatWinBatchNo, tblcSalesChallanBatWinMRP, tblcSalesChallanBatWinPurRate, tblcSalesChallanBatWinQty, tblcSalesChallanBatWinDisPer, tblcSalesChallanBatWinDisAmt, tblcSalesChallanBatWinBarcode, tblcSalesChallanBatWinMarginPer, tblcSalesChallanBatWinFSR, tblcSalesChallanBatWinCSR, tblcSalesChallanBatWinSaleRate;

    @FXML
    private TableColumn<TranxBatchWindowDTO, Void> tblcSalesChallanBatWinAction;

    @FXML
    private CheckBox creditorCheckBox;

    @FXML
    private TableColumn<TranxBatchWindowDTO, LocalDate> tblcSalesChallanBatWinPurDate, tblcSalesChallanBatWinMFGDate, tblcSalesChallanBatWinEXPDate;

    @FXML
    private ComboBox<GstDetailsDTO> cmbSalesChallanSupplierGST;
    @FXML
    private ComboBox<SalesmanMasterDTO> cmbSalesChallanSalesman;

//    @FXML
//    private ComboBox<String> cmbSalesChallanPaymentMode;


    @FXML
    private ComboBox cmbSalesChallanSalesAc;

    @FXML
    private Label lbSalesChallanGstNo, lbSalesChallanArea,
            lbSalesChallanBank,
            lbSalesChallanContactPerson,
            lbSalesChallanTransport,
            lbSalesChallanCreditDays,
            lbSalesChallanFssai,
            lbSalesChallanLisenceNo,
            lbSalesChallanRoute, lblPurChallTotalCGST, lblPurChallTotalSGST, lbtotalQty, lbTFreeQty;
    ;

//    private Label lbGrossTotal, lbDiscount, lbTotal, lbTax, lbBillAmount,

    @FXML
    private TextField tfPurChallAllRowDisPer, tfPurChallAllRowDisAmt;

    @FXML
    private TableColumn<GstDTO, String> tblcPurChallGST, tblcPurChallCGST, tblcPurChallSGST, tblcPurChallIGST;

    @FXML
    private TableView<GstDTO> tblvPurChallGST;

    @FXML
    private Label lblPurChallRoundOff, lblPurChallFreeQty, lblPurChallTotalQty, lblSCConvBillAmountEdit, lblSCConvTotalTaxableAmountEdit, lblSCConvTotalTaxEdit, lblSCConvTotalDiscountEdit, lblSCConvGrossTotalEdit;

    @FXML
    private Button btnPurChallAddChgsSubmit, btnPurChallAddChgsCancel, btnPurchaseChallanAddnlChargWindowClose, btnPurchaseChallanAddChgsLedgerWindowClose;

    @FXML
    private CheckBox chbPurChallRoundOff;


    private String SelectedLedgerId;

    private String selectedValue = "", id = "", message = "";


    private static ObservableList<String> unitList = FXCollections.observableArrayList();

    private static int rowIndexBatchNo;

    private String prdBatchTaxPer;
    private String selectedRowPrdId;

    private Double costValue, costWithTaxValue, taxAmt;
    private static int rowIndexParticular;

    private int selectedRowIndex;
    private String productIdSelected;

    List<CustomAddChargesDTO> listEdit = new ArrayList<>();

    List<CustomAddChargesDTO> addChargesDTOList = new ArrayList<>();

//    FXML
//    private ComboBox<GstDetailsDTO>cmbSalesChallanSalesAc;

//    private ComboBox<FranchiseAreaDTO>cmbAreaHeadCreateRArea;


    @FXML
    private Label lblSalesChallanCurrSTK, lblSalesChallanBatWinPrdName, lblSalesChallanBatWinCWT, lblSalesChallanBatWinCWOT, lblSalesChallanBatWinTax;

    private static final Logger SalesChallanLogger = LoggerFactory.getLogger(PurchaseChallanController.class);

    private String productId = "";


    @FXML
    private BorderPane spSalesChallanRootPane;
    //    private ScrollPane spSalesChallanRootPane;
    private ObservableList<TranxLedgerWindowDTO> observableList;
    private String ledgerName = "";
    //    private String ledgerId = "";
    private Integer rediCurrIndex = -1;
    private Boolean isProductRed = false;
    private Boolean isLedgerRed = false;

    private JsonArray deletedRows = new JsonArray();

    @FXML
    private Label sctfdisper, sctfdisamt, lblTotalFirst, lblTotalSecond, lbSalesChallanBrand, lbSalesChallanHsn, lbSalesChallanCost, lbSalesChallanGroup, lbSalesChallanTaxType, lbSalesChallanShelfid, lbSalesChallanCategory, lbSalesChallanTax, lbSalesChallanMinstock, lbSalesChallanSupplier, lbSalesChallanMargin, lbSalesChallanMaxstock;
    @FXML
    private Label lbSCBatchBatchNo,
            lbSCBatchMRP,
            lbSCBatchCessPer,
            lbSCBatchTotalAmt,
            lbSCBatchMfgDate,
            lbSCBatchPurRate,
            lbSCBatchCessAmt,
            lbSCBatchFsr,
            lbSCBatchExpDate,
            lbSCBatchDisPer,
            lbSCBatchBarcode,
            lbSCBatchCsr,
            lbSCBatchAvailStock,
            lbSCBatchDisAmt,
            lbSCBatchMarginPer,
            lbSCBatchSaleRate;

    private JsonObject jsonObject = null;
    private String responseBody;
    private JsonArray ledgerList;

    private List<Integer> referenceSCId; //? Sales Challan Ids
    private List<Integer> referenceSOId; //? Sales Order Ids
    private List<Integer> referenceSQId; //? Sales Qautation Ids

    private Stage stage = null;

    private ObservableList<TranxRow> tblRowData = FXCollections.observableArrayList();


    //    FNAdditional charges
    @FXML
    public void btnSalesChallanAddnlChrg() {
        System.out.println("in addnl charges");
        hbapSalesChallanAddnlChrgWindow.setVisible(true);

    }


    public void onClickAdditionalCharges(ActionEvent actionEvent) {
        Stage stage = (Stage) spSalesChallanRootPane.getScene().getWindow();


        AdditionalCharges.additionalCharges(stage, addChargesDTOList, total_taxable_amt, (input1, input2) -> {
            addChargesDTOList.clear();
            addChargesDTOList.addAll(input2);

//            System.out.println("acDelDetailsIds:"+input1[0]);
//            System.out.println("Add Charges:"+input1[1]);
//            System.out.println(addChargesDTOList);

            tfPurchaseChallanTotalAmt.setText(input1[1]);


        });
    }


    //    SalesAc Combo
    private static ObservableList<String> itemsSaleAc = FXCollections.observableArrayList("Sales A/C");


    //    FNlast record
    public void getLastSalesRecord() {
        try {
            HttpResponse<String> response = APIClient.getRequest("get_last_sales_challan_record");
            JsonObject jsonObject = new Gson().fromJson(response.body(), JsonObject.class);
            System.out.println("i am list" + jsonObject);

            if (jsonObject.get("responseStatus").getAsInt() == 200) {
//                JsonObject responseObject = jsonObject.get("responseObject").getAsJsonObject();
//                JsonArray responseArray = responseObject.get("data").getAsJsonArray();
                tfSalesChallanSrNo.setText(jsonObject.get("count").getAsString());
                tfSalesChallanNo.setText(jsonObject.get("serialNo").getAsString());


            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void backToListModify() {
        Globals.salesChallanDTO = null;
        AlertUtility.AlertConfirmation(AlertUtility.alertTypeConfirmation, "Are you sure want to go list ?", input -> {
            if (input == 1) {
                GlobalController.getInstance().addTabStatic(SALES_CHALLAN_LIST_SLUG, false);
            }
        });
    }

    public void sceneInitilization() {
        // Reset and initialize the fields in PurInvoiceCommunicator
        PurInvoiceCommunicator.resetFields();

        spSalesChallanRootPane.sceneProperty().addListener((observable, oldScene, newScene) -> {
            if (newScene != null && newScene.getWindow() instanceof Stage) {
                Communicator.stage = (Stage) newScene.getWindow();
                Communicator.tranxDate = dpSalesChallanCreateDate.getText();
            }
        });

    }


//    private void getPaymentModes() {
//        cmbSalesChallanPaymentMode.setItems(payment_mode_list);
//    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        resposiveScreenCss();


        //todo : autofocus on TranxDate

        initSalesInvoiceForm();

//        getPaymentModes();

//        if (salesChallanDTO.getId() > 0) {
//            tfSalesChallanLedgerName.setText(ledgersById(Long.valueOf(invoice_data_map.get("supplierId"))));
//            if (supplierGSTINList != null && !supplierGSTINList.isEmpty()) {
//                cmbPurChallSupplierGST.setItems(supplierGSTINList);
//                cmbPurChallSupplierGST.setValue(supplierGSTINList.get(0));
//            }
//        }


        TextField[] textFields = {tfSalesChallanLedgerName, tfSalesChallanSrNo};

        ComboBox[] comboFields = {cmbSalesChallanSalesman, cmbSalesChallanSalesAc};


        for (TextField textField : textFields) {
            CommonFunctionalUtils.cursorMomentForTextField(textField);
        }

//        for (ComboBox comboField : comboFields) {
//            CommonFunctionalUtils.cursorMomentForComboBox(comboField);
//        }

//        purchase_id = SalesChallanListController.getPurchaseId();
//        System.out.println("purchase  id : " + purchase_id);
//        PurchaseInvoiceListController.purchase_id = -1;

        Platform.runLater(() -> {

            ComboBox[] cmbFields = {cmbSalesChallanSupplierGST,
                    cmbSalesChallanSalesAc,
                    cmbSalesChallanSalesman};

            for (ComboBox combo : cmbFields) {
                CommonValidationsUtils.comboBoxDataShow(combo);
            }
        });

        LocalDate tranxDate = LocalDate.now();
        LocalDate orderDate = LocalDate.now();
        dpSalesChallanCreateDate.setText(tranxDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
//        CommonFunctionalUtils.restrictTextFieldToDigitsAndDateFormat(dpSalesChallanCreateDate);
//
        DateValidator.applyDateFormat(dpSalesChallanCreateDate);
        CommonFunctionalUtils.restrictTextFieldToDigitsAndDateFormat(dpSalesChallanCreateDate);

        Platform.runLater(() -> tfSalesChallanLedgerName.requestFocus());
//        getOutletSalesmanMasterRecord();
        //         Enter traversal
//        spSalesChallanRootPane.addEventFilter(KeyEvent.KEY_PRESSED, (KeyEvent event) -> {
//            if (event.getCode() == KeyCode.ENTER) {
//                KeyEvent newEvent = new KeyEvent(
//                        null,
//                        null,
//                        KeyEvent.KEY_PRESSED,
//                        "",
//                        "\t",
//                        KeyCode.TAB,
//                        event.isShiftDown(),
//                        event.isControlDown(),
//                        event.isAltDown(),
//                        event.isMetaDown()
//                );
//
//                Event.fireEvent(event.getTarget(), newEvent);
//                event.consume();
//            }
//        });

        spSalesChallanRootPane.addEventFilter(KeyEvent.KEY_PRESSED, (KeyEvent event) -> {


            if (event.getCode() == KeyCode.ENTER) {
                if (event.getTarget() instanceof Button targetButton && targetButton.getText().equalsIgnoreCase("submit")) {
                    System.out.println(targetButton.getText());
                } else if (event.getTarget() instanceof Button targetButton && targetButton.getText().equalsIgnoreCase("cancel")) {
                    System.out.println(targetButton.getText());
                } else if (event.getTarget() instanceof Button targetButton && targetButton.getText().equalsIgnoreCase("update")) {
                    System.out.println(targetButton.getText());
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
            } else if (event.getCode() == KeyCode.S && event.isControlDown()) {
                btnSalesChallanCreate.fire();
            } else if (event.getCode() == KeyCode.X && event.isControlDown()) {
                if (salesChallanDTO != null) {
                    btnSalesChallanModify.fire();
                } else {
                    btnSalesChallanCancel.fire();
                }
            } else if (event.getCode() == KeyCode.E && event.isControlDown()) {
//                salesChallanEditPage();
                btnSalesChallanModify.fire();
            }
//            if (event.getCode() == KeyCode.D && event.isControlDown()) {
//                salesChallanDTO = tblvSalesChallan.getSelectionModel().getSelectedItem();
//                Integer id = Integer.valueOf(salesChallanDTO.getId());
////                dele(id);
//            }
        });


        tblvPurChallGST.setFocusTraversable(false);

        tblcPurChallGST.setSortable(false);
        tblcPurChallCGST.setSortable(false);
        tblcPurChallSGST.setSortable(false);
        tblcPurChallIGST.setSortable(false);

        tblcPurChallGST.setCellValueFactory(cellData -> {
            StringProperty taxProperty = cellData.getValue().taxPerProperty();
            return Bindings.createStringBinding(
                    () -> "(" + (int) Double.parseDouble(taxProperty.get()) + " %)",
                    taxProperty
            );
        });

        tblcPurChallGST.setStyle("-fx-alignment: CENTER;");
        tblcPurChallCGST.setCellValueFactory(cellData -> cellData.getValue().cgstProperty());
        tblcPurChallCGST.setCellFactory(col -> new TableCell<GstDTO, String>() {
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


        tblcPurChallCGST.setStyle("-fx-alignment: CENTER;");
        tblcPurChallSGST.setCellValueFactory(cellData -> cellData.getValue().sgstProperty());

        tblcPurChallSGST.setStyle("-fx-alignment: CENTER;");

        tblcPurChallSGST.setCellFactory(col -> new TableCell<GstDTO, String>() {
            private final Label label = new Label();
            private VBox vBox = new VBox(label);

            @Override
            protected void updateItem(String item, boolean empty) {
                // Setting the alignment and style for the VBox
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
                    // Parse the item to a double, format it to two decimal places, and then set the label's text
                    try {
                        double value = Double.parseDouble(item);
                        String formattedValue = String.format("%.2f", value);
                        label.setText(formattedValue);
                    } catch (NumberFormatException e) {
                        // In case item is not a valid double, just set it as is
                        label.setText(item);
                    }
                    // Add border with width
                    setGraphic(vBox);
                }
            }
        });

//IGST
        tblcPurChallIGST.setCellValueFactory(cellData -> cellData.getValue().igstProperty());
        tblcPurChallIGST.setStyle("-fx-alignment: CENTER;");

        tblcPurChallIGST.setCellFactory(col -> new TableCell<GstDTO, String>() {
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


        if (Globals.salesChallanDTO != null) {
            System.out.println("FranchiseListDTOId" + Globals.salesChallanDTO.getId());
            btnSalesChallanCreate.setText("Update");
            btnSalesChallanModify.setVisible(false);
            getEditDataById();
//            handleCheckBoxAction();
        } else {
            Globals.salesChallanDTO = null;
            System.out.println("SalesChallanListDTOId is NULL");
            // Delayed initialization to ensure tgFranchiseIsFunding's width is properly calculated
            getLastSalesRecord();


        }

//        tblvPurChallGST.getItems().addAll(new GstDTO("28", "12345678", "12345678", ""));


        setGSTFooterPart();
        sceneInitilization();

        tblvPurChallGST.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        tableInitialization();

        nodetraversal(tfSalesChallanLedgerName, cmbSalesChallanSupplierGST);
        nodetraversal(cmbSalesChallanSupplierGST, tfSalesChallanSrNo);
        nodetraversal(tfSalesChallanSrNo, tfSalesChallanNo);
        nodetraversal(tfSalesChallanNo, dpSalesChallanCreateDate);
        nodetraversal(dpSalesChallanCreateDate, cmbSalesChallanSalesAc);
        nodetraversal(cmbSalesChallanSalesAc, btnSalesChallanChooseFileButton);


        tfSalesChallanLedgerName.setOnMouseClicked(actionEvent -> {
            handleTfLedgerName();
        });

        tfSalesChallanLedgerName.setOnKeyPressed(event -> {
            KeyCode code = event.getCode();

            // Check if Ctrl is pressed along with X key
            if (!(event.isControlDown() && code == KeyCode.X || code == KeyCode.E)) {
                if (code == KeyCode.SPACE || code == KeyCode.ENTER || code == KeyCode.TAB) {
                    handleTfLedgerName();
                }
            }
        });


        tblvSalesChallanCmpTRow.setEditable(true);
        tblvSalesChallanCmpTRow.getSelectionModel().setCellSelectionEnabled(true);
        tblvSalesChallanCmpTRow.getSelectionModel().selectFirst();
        tblvSalesChallanCmpTRow.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection == null) {
                tblvSalesChallanCmpTRow.getSelectionModel().select(oldSelection);
            }
        });

        fetchDataOfAllTransactionLedgers("");

        cmbSalesChallanSalesAc.setItems(itemsSaleAc);

        if (!itemsSaleAc.isEmpty()) {
            cmbSalesChallanSalesAc.getSelectionModel().select(0);
        }
        LocalDate currentDate = LocalDate.now();
//        dpSalesChallanCreateDate.setValue(currentDate);

        responsiveCmpt();
//        gstTable();
        setMinColumnWidths();

        tblcSalesChallanCmpTRowSr.setCellFactory(column -> {
            return new TableCell<>() {
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

        tfSalesChallanSrNo.setDisable(true);
        tfSalesChallanSrNo.setStyle("-fx-opacity: 1;"); // Make textfield fully visible
        tfSalesChallanSrNo.pseudoClassStateChanged(PseudoClass.getPseudoClass("disabled"), false);

    }

    public void setPurChallDataToProduct() {

        String tranxDate = Communicator.text_to_date.fromString(dpSalesChallanCreateDate.getText()).toString();
        String ledgrId = ledgerId;
        String gstNum = cmbSalesChallanSupplierGST.getId();
        String purSerNum = tfSalesChallanSrNo.getText();
        String challanNo = tfSalesChallanNo.getText();
        String purAcc = cmbSalesChallanSalesAc.getId();
        String challanDate = Communicator.text_to_date.fromString(dpSalesChallanCreateDate.getText()).toString();

        PurTranxToProductRedirectionDTO mData = new PurTranxToProductRedirectionDTO();
        mData.setRedirect(FxmFileConstants.SALES_CHALLAN_CREATE_SLUG);
        mData.setTranxDate(tranxDate);
        mData.setLedegrId(ledgrId);
        mData.setGstNum(gstNum);
        mData.setPurSerNum(purSerNum);
        mData.setChallanNo(challanNo);
        mData.setPurAcc(purAcc);
        mData.setChallanDate(challanDate);
        mData.setLedgerRed(isLedgerRed);
        mData.setProductRed(isProductRed);
        mData.setTranxType("sale");

        mData.setPurChallEditId(purchase_id);
        mData.setRedirection(true);

        mData.setRediPrdCurrIndex(rediCurrIndex);
        List<CmpTRowDTOSoToSc> currentItems = new ArrayList<>(tblvSalesChallanCmpTRow.getItems());
        List<CmpTRowDTOSoToSc> list = new ArrayList<>(currentItems);

        mData.setRowSaleChallan(list);

//        for (PurchaseInvoiceTable rowData : tblvPurChallCmpTRow.getItems()) {
//            PurchaseInvoiceTable mData1 = new PurchaseInvoiceTable();
//            mData1.setProduct_id(rowData.getProduct_id());
////            mData1.set
//        }

        if (isProductRed == true) {
            GlobalController.getInstance().addTabStaticWithIsNewTab("product-create", false, mData);
        }
        if (isLedgerRed == true) {
            GlobalController.getInstance().addTabStaticWithIsNewTab("ledger-create", false, mData);
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
                String state = "";
                supplierGSTINList.add(new GstDetailsDTO(gstin, gst_id, state));
            }
        }
        if (ledgerStateCode.equalsIgnoreCase(GlobalTranx.getCompanyStateCode().toString())) {
            taxFlag = true;
        }

        return ledgerName;
    }


    public void setRedirectData(Object inObj) {
        PurTranxToProductRedirectionDTO franchiseDTO = (PurTranxToProductRedirectionDTO) inObj;
        Integer redirectIndex = franchiseDTO.getRediPrdCurrIndex();
        purPrdRedId = franchiseDTO.getPurChallEditId();
        isRedirect = franchiseDTO.getRedirection();
        tfSalesChallanNo.setText(franchiseDTO.getChallanNo());
        ledgerId = franchiseDTO.getLedegrId();
        tfSalesChallanSrNo.setText(franchiseDTO.getPurSerNum());
        tfSalesChallanLedgerName.setText(ledgersById(Long.valueOf(ledgerId)));
        int cnt = 0, index = 0;
        tblvSalesChallanCmpTRow.getItems().clear();
        System.out.println(" Called >> " + supplierGSTINList.size());
        System.out.println(" Called ledger_id >> " + ledgerId);
        tfSalesChallanLedgerName.setText(ledgersById(Long.valueOf(ledgerId)));
        if (supplierGSTINList != null && !supplierGSTINList.isEmpty()) {
            cmbSalesChallanSupplierGST.setItems(supplierGSTINList);
            cmbSalesChallanSupplierGST.setValue(supplierGSTINList.get(0));
        }
        System.out.println(" Called >> " + supplierGSTINList.size());
        System.out.println(" Called ledger_id >> " + ledgerId);
        tfSalesChallanLedgerName.setText(ledgersById(Long.valueOf(ledgerId)));
        if (supplierGSTINList != null && !supplierGSTINList.isEmpty()) {
            cmbSalesChallanSupplierGST.setItems(supplierGSTINList);
            cmbSalesChallanSupplierGST.setValue(supplierGSTINList.get(0));
        }
        PurInvoiceCommunicator.levelAForPurInvoiceObservableList.clear();
        if (franchiseDTO.getProductRed() == true) {
            Long productId = Long.valueOf(franchiseDTO.getRedirectProductId());
            int rindex = franchiseDTO.getRediPrdCurrIndex();
            JsonObject obj = productRedirectById(productId);

            for (CmpTRowDTOSoToSc purchaseInvoiceTable : franchiseDTO.getRowSaleChallan()) {
                if (index == rindex) {
                    purchaseInvoiceTable.setProduct_id(productId + "");
                    purchaseInvoiceTable.setParticulars(obj.get("productName").getAsString());
                    purchaseInvoiceTable.setPackages(obj.get("package").getAsString());
                    purchaseInvoiceTable.setIs_batch(obj.get("is_batch").getAsString());
                    purchaseInvoiceTable.setRate_a(obj.get("fsrmh").getAsString());
                    purchaseInvoiceTable.setRate_b(obj.get("fsrai").getAsString());
                    purchaseInvoiceTable.setRate_c(obj.get("csrmh").getAsString());
                    purchaseInvoiceTable.setRate_d(obj.get("csrai").getAsString());
                    purchaseInvoiceTable.setTax(obj.get("tax_per").getAsString());
                    purchaseInvoiceTable.setUnit_id(obj.get("unit_id").getAsString());
                    purchaseInvoiceTable.setB_rate(obj.get("mrp").getAsString());
                    purchaseInvoiceTable.setB_purchase_rate(obj.get("purchaseRate").getAsString());
                    purchaseInvoiceTable.setCurrent_index(String.valueOf(index));
                    BatchWindowTableDTO batchWindowTableDTO = new BatchWindowTableDTO();

                    batchWindowTableDTO.setProductId(purchaseInvoiceTable.getProduct_id());
                    batchWindowTableDTO.setCsr_mh(purchaseInvoiceTable.getRate_c());
                    batchWindowTableDTO.setCsr_ai(purchaseInvoiceTable.getRate_d().equals("0.0") ? "" : purchaseInvoiceTable.getRate_d());
                    batchWindowTableDTO.setFsr_mh(purchaseInvoiceTable.getRate_a());
                    batchWindowTableDTO.setFsr_ai(purchaseInvoiceTable.getRate_b());
                    batchWindowTableDTO.setMrp(purchaseInvoiceTable.getB_rate());
                    System.out.println("iceTable.getLevelA_id()" + purchaseInvoiceTable.getLevelA_id());

                    batchWindowTableDTO.setUnitId(purchaseInvoiceTable.getUnit_id());
                    batchWindowTableDTO.setBatchNo(purchaseInvoiceTable.getBatch_or_serial());
                    batchWindowTableDTO.setB_details_id("");
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
                    batchWindowTableDTO.setPurchaseRate(purchaseInvoiceTable.getB_purchase_rate());
                    List<BatchWindowTableDTO> listBatch = new ArrayList<>();
                    listBatch.add(batchWindowTableDTO);

                    purchaseInvoiceTable.setBatchWindowTableDTOList(listBatch);


                }
//            mData1.setParticulars(rowData.getParticulars());
//            mData1.setPackages(rowData.getPackages());
                tblvSalesChallanCmpTRow.getItems().addAll(purchaseInvoiceTable);
                SalesChallanCalculations.rowCalculationForPurcInvoice(index, tblvSalesChallanCmpTRow, callback);
                SalesChallanCalculations.calculateGst(tblvSalesChallanCmpTRow, callback);
//                calculateGst(tableView, callback);

                List<LevelAForPurInvoice> levelAForPurInvoiceList = ProductUnitsPackingForSalesChallan.getAllProductUnitsPackingFlavour(purchaseInvoiceTable.getProduct_id());
                ObservableList<LevelAForPurInvoice> observableLevelAList = FXCollections.observableArrayList(levelAForPurInvoiceList);
                if (index >= 0 && index < PurInvoiceCommunicator.levelAForPurInvoiceObservableList.size()) {
                    PurInvoiceCommunicator.levelAForPurInvoiceObservableList.set(index, observableLevelAList);

                    tblvSalesChallanCmpTRow.getItems().get(index).setLevelA(null);
                    tblvSalesChallanCmpTRow.getItems().get(index).setLevelA(levelAForPurInvoiceList.get(0).getLabel());

                    for (LevelAForPurInvoice levelAForPurInvoice : PurInvoiceCommunicator.levelAForPurInvoiceObservableList.get(index)) {
                        if (tblvSalesChallanCmpTRow.getItems().get(index).getLevelA_id().equals(levelAForPurInvoice.getValue())) {
                            tblvSalesChallanCmpTRow.getItems().get(index).setLevelA(null);
                            tblvSalesChallanCmpTRow.getItems().get(index).setLevelA(levelAForPurInvoice.getLabel());
                        }
                    }

                } else {
                    System.out.println("Index else >> : " + index);
                    PurInvoiceCommunicator.levelAForPurInvoiceObservableList.add(observableLevelAList);
                }

                cnt++;
                index++;
            }
        }

        int inx = 0;
        System.out.println("is ledgeriiiiiiiiiii" + franchiseDTO.getLedgerRed());
        // tblvPurChallCmpTRow.getItems().clear();
        if (franchiseDTO.getLedgerRed() == true) {
            System.out.println("is ledger" + franchiseDTO.getLedgerRed());
            for (CmpTRowDTOSoToSc rowData : franchiseDTO.getRowSaleChallan()) {


                System.out.println("String.valueOf(rowData.getRowData().get(0))" + rowData.getProduct_id());
//                mData1.setProduct_id(rowData.getProduct_id());
//                mData1.setParticulars(rowData.getParticulars());
//                mData1.setPackages(rowData.getPackages());
                List<LevelAForPurInvoice> levelAForPurInvoiceList = ProductUnitsPackingForSalesChallan.getAllProductUnitsPackingFlavour(rowData.getProduct_id());
                ObservableList<LevelAForPurInvoice> observableLevelAList = FXCollections.observableArrayList(levelAForPurInvoiceList);
                if (index >= 0 && index < PurInvoiceCommunicator.levelAForPurInvoiceObservableList.size()) {
                    PurInvoiceCommunicator.levelAForPurInvoiceObservableList.set(index, observableLevelAList);

                    tblvSalesChallanCmpTRow.getItems().get(index).setLevelA(null);
                    tblvSalesChallanCmpTRow.getItems().get(index).setLevelA(levelAForPurInvoiceList.get(0).getLabel());

                    for (LevelAForPurInvoice levelAForPurInvoice : PurInvoiceCommunicator.levelAForPurInvoiceObservableList.get(index)) {

                        if (tblvSalesChallanCmpTRow.getItems().get(index).getLevelA_id().equals(levelAForPurInvoice.getValue())) {
                            tblvSalesChallanCmpTRow.getItems().get(index).setLevelA(null);
                            tblvSalesChallanCmpTRow.getItems().get(index).setLevelA(levelAForPurInvoice.getLabel());
                        }
                    }

                } else {
                    System.out.println("Index else >> : " + index);
                    PurInvoiceCommunicator.levelAForPurInvoiceObservableList.add(observableLevelAList);
                }

                tblvSalesChallanCmpTRow.getItems().add(rowData);
                SalesChallanCalculations.rowCalculationForPurcInvoice(index, tblvSalesChallanCmpTRow, callback);
                SalesChallanCalculations.calculateGst(tblvSalesChallanCmpTRow, callback);
                inx++;
            }
        }

        Platform.runLater(() -> {
            TableColumn<CmpTRowDTOSoToSc, ?> colName = tblvSalesChallanCmpTRow.getColumns().get(1);
            tblvSalesChallanCmpTRow.edit(redirectIndex, colName);
        });


    }

    public JsonObject productRedirectById(Long id) {

        Map<String, String> map = new HashMap<>();
        map.put("product_id", String.valueOf(id));

        String formData = mapToStringforFormData(map);
        HttpResponse<String> response = APIClient.postFormDataRequest(formData, "transaction_product_details");

        String json = response.body();
        JsonObject jsonObject = new Gson().fromJson(json, JsonObject.class);
        JsonObject responseObj = jsonObject.getAsJsonObject("result");
        System.out.println("responseObj" + responseObj);
        String ledgerName = responseObj.get("productName").getAsString();
        //ledgerStateCode = responseObj.get("ledgerStateCode").getAsString();
//        JsonArray gstdetailsArray = responseObj.getAsJsonArray("gstdetails");
//        if (gstdetailsArray != null) {
//            for (JsonElement element : gstdetailsArray) {
//                JsonObject gstdetail = element.getAsJsonObject();
//                String gstin = gstdetail.get("gstin").getAsString();
//                String gst_id = gstdetail.get("id").getAsString();
//                supplierGSTINList.add(new CommonDTO(gstin, gst_id));
//            }
//        }
//        if (ledgerStateCode.equalsIgnoreCase(GlobalTranx.getCompanyStateCode().toString())) {
//            taxFlag = true;
//        }
        return responseObj;

    }

    private void resposiveScreenCss() {
        // For Responsive Resolutions Css Style Apply
        double height = Screen.getPrimary().getBounds().getHeight();
        double width = Screen.getPrimary().getBounds().getWidth();
        System.out.println("width" + width);
        if (width >= 992 && width <= 1024) {
            topFirstRow.setSpacing(10);
            topSecondRow.setSpacing(10);
            scTotalMainDiv.setSpacing(10);
            scTotalMainInnerDiv.setSpacing(8);
            // Set preferred widths as percentages of the HBox width
            scBottomFirstV.prefWidthProperty().bind(scBottomMain.widthProperty().multiply(0.54));
            scBottomSecondV.prefWidthProperty().bind(scBottomMain.widthProperty().multiply(0.24));
            scBottomThirdV.prefWidthProperty().bind(scBottomMain.widthProperty().multiply(0.22));
//            so2cBottomMain.setPrefHeight(150);

            tblvPurChallGST.getStylesheets().add(GenivisApplication.class.getResource("ui/css/purchase_challan_css/gst_table_css/gst_table_992_1024.css").toExternalForm());
            tvSalesChallanSupplierDetails.getStylesheets().add(GenivisApplication.class.getResource("ui/css/purchase_challan_css/supplier_table_css/supplier_table_992_1024.css").toExternalForm());
            spSalesChallanRootPane.getStylesheets().add(GenivisApplication.class.getResource("ui/css/TranxCommonCssStyles/tranxCommonCssStyle1.css").toExternalForm());
        } else if (width >= 1025 && width <= 1280) {
            topFirstRow.setSpacing(10);
            topSecondRow.setSpacing(10);
            scTotalMainDiv.setSpacing(10);
            scTotalMainInnerDiv.setSpacing(8);

            // Set preferred widths as percentages of the HBox width
            scBottomFirstV.prefWidthProperty().bind(scBottomMain.widthProperty().multiply(0.58));
            scBottomSecondV.prefWidthProperty().bind(scBottomMain.widthProperty().multiply(0.20));
            scBottomThirdV.prefWidthProperty().bind(scBottomMain.widthProperty().multiply(0.22));
//            so2cBottomMain.setPrefHeight(150);
            tblvPurChallGST.getStylesheets().add(GenivisApplication.class.getResource("ui/css/purchase_challan_css/gst_table_css/gst_table_1025_1280.css").toExternalForm());
            tvSalesChallanSupplierDetails.getStylesheets().add(GenivisApplication.class.getResource("ui/css/purchase_challan_css/supplier_table_css/supplier_table_1025_1280.css").toExternalForm());
            spSalesChallanRootPane.getStylesheets().add(GenivisApplication.class.getResource("ui/css/TranxCommonCssStyles/tranxCommonCssStyle2.css").toExternalForm());
        } else if (width >= 1281 && width <= 1368) {
            topFirstRow.setSpacing(12);
            topSecondRow.setSpacing(12);
            scTotalMainDiv.setSpacing(12);
            scTotalMainInnerDiv.setSpacing(8);
            // Set preferred widths as percentages of the HBox width
            scBottomFirstV.prefWidthProperty().bind(scBottomMain.widthProperty().multiply(0.6));
            scBottomSecondV.prefWidthProperty().bind(scBottomMain.widthProperty().multiply(0.20));
            scBottomThirdV.prefWidthProperty().bind(scBottomMain.widthProperty().multiply(0.20));
//            so2cBottomMain.setPrefHeight(150);

            tblvPurChallGST.getStylesheets().add(GenivisApplication.class.getResource("ui/css/purchase_challan_css/gst_table_css/gst_table_1281_1368.css").toExternalForm());
            tvSalesChallanSupplierDetails.getStylesheets().add(GenivisApplication.class.getResource("ui/css/purchase_challan_css/supplier_table_css/supplier_table_1281_1368.css").toExternalForm());
            spSalesChallanRootPane.getStylesheets().add(GenivisApplication.class.getResource("ui/css/TranxCommonCssStyles/tranxCommonCssStyle3.css").toExternalForm());
        } else if (width >= 1369 && width <= 1400) {
            topFirstRow.setSpacing(15);
            topSecondRow.setSpacing(15);
            scTotalMainDiv.setSpacing(15);
            scTotalMainInnerDiv.setSpacing(8);
            // Set preferred widths as percentages of the HBox width
            scBottomFirstV.prefWidthProperty().bind(scBottomMain.widthProperty().multiply(0.56));
            scBottomSecondV.prefWidthProperty().bind(scBottomMain.widthProperty().multiply(0.20));
            scBottomThirdV.prefWidthProperty().bind(scBottomMain.widthProperty().multiply(0.24));
//            so2cBottomMain.setPrefHeight(150);

            tblvPurChallGST.getStylesheets().add(GenivisApplication.class.getResource("ui/css/purchase_challan_css/gst_table_css/gst_table_1369_1400.css").toExternalForm());
            tvSalesChallanSupplierDetails.getStylesheets().add(GenivisApplication.class.getResource("ui/css/purchase_challan_css/supplier_table_css/supplier_table_1369_1400.css").toExternalForm());
            spSalesChallanRootPane.getStylesheets().add(GenivisApplication.class.getResource("ui/css/TranxCommonCssStyles/tranxCommonCssStyle4.css").toExternalForm());
        } else if (width >= 1401 && width <= 1440) {
            topFirstRow.setSpacing(15);
            topSecondRow.setSpacing(15);
            scTotalMainDiv.setSpacing(15);
            scTotalMainInnerDiv.setSpacing(8);
            // Set preferred widths as percentages of the HBox width
            scBottomFirstV.prefWidthProperty().bind(scBottomMain.widthProperty().multiply(0.58));
            scBottomSecondV.prefWidthProperty().bind(scBottomMain.widthProperty().multiply(0.20));
            scBottomThirdV.prefWidthProperty().bind(scBottomMain.widthProperty().multiply(0.22));
//            so2cBottomMain.setPrefHeight(150);

            tblvPurChallGST.getStylesheets().add(GenivisApplication.class.getResource("ui/css/purchase_challan_css/gst_table_css/gst_table_1401_1440.css").toExternalForm());
            tvSalesChallanSupplierDetails.getStylesheets().add(GenivisApplication.class.getResource("ui/css/purchase_challan_css/supplier_table_css/supplier_table_1401_1440.css").toExternalForm());
            spSalesChallanRootPane.getStylesheets().add(GenivisApplication.class.getResource("ui/css/TranxCommonCssStyles/tranxCommonCssStyle5.css").toExternalForm());
        } else if (width >= 1441 && width <= 1680) {

            topFirstRow.setSpacing(20);
            topSecondRow.setSpacing(20);
            scTotalMainDiv.setSpacing(20);
            scTotalMainInnerDiv.setSpacing(10);
            // Set preferred widths as percentages of the HBox width
            scBottomFirstV.prefWidthProperty().bind(scBottomMain.widthProperty().multiply(0.62));
            scBottomSecondV.prefWidthProperty().bind(scBottomMain.widthProperty().multiply(0.18));
            scBottomThirdV.prefWidthProperty().bind(scBottomMain.widthProperty().multiply(0.20));
//            scBottomMain.setPrefHeight(150);
            tblvPurChallGST.getStylesheets().add(GenivisApplication.class.getResource("ui/css/purchase_challan_css/gst_table_css/gst_table_1441_1680.css").toExternalForm());
            tvSalesChallanSupplierDetails.getStylesheets().add(GenivisApplication.class.getResource("ui/css/purchase_challan_css/supplier_table_css/supplier_table_1441_1680.css").toExternalForm());
            spSalesChallanRootPane.getStylesheets().add(GenivisApplication.class.getResource("ui/css/TranxCommonCssStyles/tranxCommonCssStyle6.css").toExternalForm());
        } else if (width >= 1681 && width <= 1920) {
            topFirstRow.setSpacing(20);
            topSecondRow.setSpacing(20);
            scTotalMainDiv.setSpacing(20);
            scTotalMainInnerDiv.setSpacing(10);
            // Set preferred widths as percentages of the HBox width
            scBottomFirstV.prefWidthProperty().bind(scBottomMain.widthProperty().multiply(0.6));
            scBottomSecondV.prefWidthProperty().bind(scBottomMain.widthProperty().multiply(0.18));
            scBottomThirdV.prefWidthProperty().bind(scBottomMain.widthProperty().multiply(0.22));
//            scBottomMain.setPrefHeight(150);
            tblvPurChallGST.getStylesheets().add(GenivisApplication.class.getResource("ui/css/gst_table.css").toExternalForm());
            tvSalesChallanSupplierDetails.getStylesheets().add(GenivisApplication.class.getResource("ui/css/invoice_product_history_table.css").toExternalForm());
            spSalesChallanRootPane.getStylesheets().add(GenivisApplication.class.getResource("ui/css/TranxCommonCssStyles/tranxCommonCssStyle7.css").toExternalForm());

        }
    }


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


    public void responsiveCmpt() {
        tblcSalesChallanCmpTRowSr.prefWidthProperty().bind(tblvSalesChallanCmpTRow.widthProperty().multiply(0.03));
        tblcSalesChallanCmpTRowParticulars.prefWidthProperty().bind(tblvSalesChallanCmpTRow.widthProperty().multiply(0.35));
        tblcSalesChallanCmpTRowPacking.prefWidthProperty().bind(tblvSalesChallanCmpTRow.widthProperty().multiply(0.04));
        tblcSalesChallanCmpTRowA.prefWidthProperty().bind(tblvSalesChallanCmpTRow.widthProperty().multiply(0.06));
        tblcSalesChallanCmpTRowB.prefWidthProperty().bind(tblvSalesChallanCmpTRow.widthProperty().multiply(0.06));
        tblcSalesChallanCmpTRowC.prefWidthProperty().bind(tblvSalesChallanCmpTRow.widthProperty().multiply(0.06));
        tblcSalesChallanCmpTRowBatchNo.prefWidthProperty().bind(tblvSalesChallanCmpTRow.widthProperty().multiply(0.06));

        tblcSalesChallanCmpTRowUnit.prefWidthProperty().bind(tblvSalesChallanCmpTRow.widthProperty().multiply(0.06));
        tblcSalesChallanCmpTRowQuantity.prefWidthProperty().bind(tblvSalesChallanCmpTRow.widthProperty().multiply(0.05));
        tblcSalesChallanCmpTRowFreeQty.prefWidthProperty().bind(tblvSalesChallanCmpTRow.widthProperty().multiply(0.04));
        tblcSalesChallanCmpTRowRate.prefWidthProperty().bind(tblvSalesChallanCmpTRow.widthProperty().multiply(0.06));
        tblcSalesChallanCmpTRowGrossAmt.prefWidthProperty().bind(tblvSalesChallanCmpTRow.widthProperty().multiply(0.06));
        tblcSalesChallanCmpTRowDiscPerc.prefWidthProperty().bind(tblvSalesChallanCmpTRow.widthProperty().multiply(0.05));
        tblcSalesChallanCmpTRowDisPer.prefWidthProperty().bind(tblvSalesChallanCmpTRow.widthProperty().multiply(0.05));
        tblcSalesChallanCmpTRowDisAmt.prefWidthProperty().bind(tblvSalesChallanCmpTRow.widthProperty().multiply(0.05));
        tblcSalesChallanCmpTRowTaxPer.prefWidthProperty().bind(tblvSalesChallanCmpTRow.widthProperty().multiply(0.05));
        tblcSalesChallanCmpTRowNetAmt.prefWidthProperty().bind(tblvSalesChallanCmpTRow.widthProperty().multiply(0.05));
        tblcSalesChallanCmpTRowAction.prefWidthProperty().bind(tblvSalesChallanCmpTRow.widthProperty().multiply(0.05));

    }

    public void gstTable() {
        tblcPurChallGST.prefWidthProperty().bind(tblvPurChallGST.widthProperty().multiply(0.25));
        tblcPurChallCGST.prefWidthProperty().bind(tblvPurChallGST.widthProperty().multiply(0.25));
        tblcPurChallSGST.prefWidthProperty().bind(tblvPurChallGST.widthProperty().multiply(0.25));
        tblcPurChallIGST.prefWidthProperty().bind(tblvPurChallGST.widthProperty().multiply(0.25));

    }

    private void setMinColumnWidths() {
        tblcPurChallGST.setMinWidth(100);
        tblcPurChallCGST.setMinWidth(100);
        tblcPurChallSGST.setMinWidth(100);
        tblcPurChallIGST.setMinWidth(100);
    }

    private double roundDigit(double value, int decimalPlaces) {
        double factor = Math.pow(10, decimalPlaces);
        return Math.round(value * factor) / factor;
    }


    //    FNcancel sales challan
    public void cancelSalesChallan() {
        salesChallanDTO = null;
        AlertUtility.AlertConfirmation(AlertUtility.alertTypeConfirmation, "Are you sure want to Cancel ?", input -> {
            if (input == 1) {
                clearLedAndProdInfo();
            }
        });
    }

    public void clearLedAndProdInfo() {
//        ObservableList<SalesQuotationProductDTO> emptyList = FXCollections.observableArrayList();
//        tvSalesOrderCmpTRow.setItems(emptyList);
        ObservableList<SalesOrderSupplierDetailsDTO> emptyList1 = FXCollections.observableArrayList();
//        Debtor_id = "";
        tfSalesChallanLedgerName.setText("");
        cmbSalesChallanSupplierGST.getSelectionModel().clearSelection();
//        tvSalesQuotCreateprodInvoiceDetails.setItems(emptyList1);
        tfSalesChallanNarrations.setText("");

        //? clear Ledger Info
        lbSalesChallanGstNo.setText("");
        lbSalesChallanArea.setText("");
        lbSalesChallanBank.setText("");
        lbSalesChallanContactPerson.setText("");
        lbSalesChallanFssai.setText("");
        lbSalesChallanLisenceNo.setText("");
        lbSalesChallanRoute.setText("");
        lbSalesChallanTransport.setText("");
        lbSalesChallanCreditDays.setText("");

        //? clear Product Info
        lbSalesChallanBrand.setText("");
        lbSalesChallanGroup.setText("");
        lbSalesChallanSupplier.setText("");
        lbSalesChallanCategory.setText("");
        lbSalesChallanHsn.setText("");
        lbSalesChallanTaxType.setText("");
        lbSalesChallanTax.setText("");
        lbSalesChallanMargin.setText("");
        lbSalesChallanCost.setText("");
        lbSalesChallanShelfid.setText("");
        lbSalesChallanMinstock.setText("");
        lbSalesChallanMaxstock.setText("");
//        sctfdisper.setText("");
//        sctfdisamt.setText("");
        tblvPurChallGST.getItems().clear();// Add a new blank row if needed
//        lblTotalFirst.setText("0.00");
//        lblTotalSecond.setText("0.00");
//        chbPurChallRoundOff.isIndeterminate(false);
//        lblPurChallRoundOff.setText("0.00");
//        sctfaddcharges.setText("");

        //clear all row calculation
        lblSCConvBillAmountEdit.setText("0.00");

        lblSCConvTotalTaxableAmountEdit.setText("0.00");

        lblSCConvTotalDiscountEdit.setText("0.00");

        lblSCConvTotalTaxEdit.setText("0.00");

//        lblSCConvTotalEdit.setText("0.00");

        tblvSalesChallanCmpTRow.getItems().clear();// Add a new blank row if needed
        tblvSalesChallanCmpTRow.getItems().add(new CmpTRowDTOSoToSc("", "0", "1", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", ""));
        tfSalesChallanLedgerName.requestFocus();
        PurInvoiceCommunicator.resetFields();//new 2.0 to Clear the CMPTROW ComboBox

    }

    private void getProductBatchsById(String bNo, String bId) {
        APIClient apiClient = null;
        myTabPane.getSelectionModel().select(tabSalesChallanBatch);
        logger.debug("Get Supplier List Data Started...");
        System.out.println("In Batch FUN >> " + bNo + " >> " + bId);
        Map<String, String> map = new HashMap<>();
        map.put("batchNo", bNo);
        map.put("id", bId);
        String formData = Globals.mapToStringforFormData(map);
        System.out.println("FormData: " + formData);
        logger.debug("Form Data:" + formData);
        apiClient = new APIClient(EndPoints.GET_BATCH_DETAILS_ENDPOINT, formData, RequestType.FORM_DATA);

        apiClient.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent workerStateEvent) {
                JsonObject jsonObject = new Gson().fromJson(workerStateEvent.getSource().getValue().toString(), JsonObject.class);
                if (jsonObject.get("responseStatus").getAsInt() == 200) {
                    JsonObject batchJsonObj = jsonObject.getAsJsonObject("response");
                    lbSCBatchBatchNo.setText(batchJsonObj.get("batchNo").getAsString());
//                    lbSCBatchMRP.setText(DateConvertUtil.convertDispDateFormat(batchJsonObj.get("batchMrp").getAsString()));

//                    lbSCBatchCessPer.setText(DateConvertUtil.convertDispDateFormat(batchJsonObj.get("expDate").getAsString()));
//                    lbSCBatchTotalAmt.setText(DateConvertUtil.convertDispDateFormat(batchJsonObj.get("barcode").getAsString()));
                    lbSCBatchMRP.setText(batchJsonObj.get("batchMrp").getAsString());
                    lbSCBatchMfgDate.setText(batchJsonObj.get("mfgDate").getAsString());
                    lbSCBatchBarcode.setText(DateConvertUtil.convertDispDateFormat(batchJsonObj.get("barcode").getAsString()));
                    lbSCBatchPurRate.setText(batchJsonObj.get("pur_rate").getAsString());

//                    lbSCBatchCessAmt.setText(batchJsonObj.get("cost_with_tax").getAsString());
                    lbSCBatchFsr.setText(batchJsonObj.get("fsrai").getAsString());

                    lbSCBatchExpDate.setText(batchJsonObj.get("expiry_date").getAsString());
                    lbSCBatchDisPer.setText(batchJsonObj.get("b_dis_per").getAsString());
//                        lbSCBatchBarcode.setText(batchJsonObj.get("csrmh").getAsString());
                    lbSCBatchCsr.setText(batchJsonObj.get("csrai").getAsString());
                    lbSCBatchAvailStock.setText(batchJsonObj.get("free_qty").getAsString());
                    lbSCBatchDisAmt.setText(batchJsonObj.get("b_dis_amt").getAsString());
                    lbSCBatchMarginPer.setText(batchJsonObj.get("min_margin").getAsString());
                    lbSCBatchSaleRate.setText(batchJsonObj.get("sales_rate").getAsString());


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
        logger.debug("Get Supplier List Data End...");

    }

    //    to get product details by id
    public void getProductDetailsById(String prod_id) {
        try {
//            logger.info("Fetching Selected Product Data to show in product info");
            Map<String, String> body = new HashMap<>();
            body.put("product_id", prod_id);
            System.out.println("ProductId 123 " + prod_id);
            String requestBody = Globals.mapToStringforFormData(body);
            HttpResponse<String> response = APIClient.postFormDataRequest(requestBody, EndPoints.TRANSACTION_PRODUCT_DETAILS);
            System.out.println("Product Details response body" + response.body());
            JsonObject jsonObject = new Gson().fromJson(response.body(), JsonObject.class);
            ObservableList<TranxProductWindowDTO> observableList = FXCollections.observableArrayList();
            //todo: activating the product tab
//            tpSalesQuotToOrder.getSelectionModel().select(tabSalesQuotaCreateProduct);
            if (jsonObject.get("responseStatus").getAsInt() == 200) {
                myTabPane.getSelectionModel().select(tabSalesChallanProduct);

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
                lbSalesChallanBrand.setText(brand);
                lbSalesChallanHsn.setText(hsn);
                lbSalesChallanGroup.setText(group);
                lbSalesChallanCategory.setText(category);
                lbSalesChallanSupplier.setText(supplier);
                lbSalesChallanTaxType.setText(tax_type);
                lbSalesChallanTax.setText(tax);
                lbSalesChallanMargin.setText(margin);
                lbSalesChallanCost.setText(cost);
                lbSalesChallanShelfid.setText(shelfId);
                lbSalesChallanMinstock.setText(min_stock);
                lbSalesChallanMaxstock.setText(max_stock);

            } else {
                System.out.println("Error in response");
            }
        } catch (Exception e) {
//            logger.error("Error in Fetching Selected Product data is" + e.getMessage());
            e.printStackTrace();
        }
    }


    //    to get sales man master data
    public void getOutletSalesmanMasterRecord() {
//        soToscLogger.info("starting of salesman master list API");
        try {

            Map<String, String> map = new HashMap<>();
            map.put("id", salesman_id);
            String formData = Globals.mapToStringforFormData(map);
            System.out.println("FormData: " + formData);
//            HttpResponse<String> response = APIClient.postFormDataRequest(formData, EndPoints.TRANSACTION_LEDGER_DETAILS);
//            String responseBody = response.body();
//            JsonObject jsonObject = new Gson().fromJson(responseBody, JsonObject.class);
//            System.out.println("tranxLedgerDetailsFun Res ==>" + jsonObject);
//
            HttpResponse<String> response = APIClient.postFormDataRequest(formData, "get_salesman_master_by_id");
            JsonObject jsonObject = new Gson().fromJson(response.body(), JsonObject.class);
            System.out.println("i am list" + jsonObject);


            if (jsonObject.get("responseStatus").getAsInt() == 200) {
                JsonObject responseArray = jsonObject.get("responseObject").getAsJsonObject();
                // Create an ObservableList to store items for the combo box
                ObservableList<SalesmanMasterDTO> salesmanList = FXCollections.observableArrayList();

                // Assuming the JSON array contains the required information to create a SalesmanMasterDTO instance
//                for (JsonElement element : responseArray) {
                // Extract information from the JSON element
//                responseArray = element.getAsJsonObject();
                int id = responseArray.get("id").getAsInt();
                String firstName = responseArray.get("firstName").getAsString();
                // Repeat for other fields

                // Create a new SalesmanMasterDTO instance with the extracted information
                SalesmanMasterDTO salesmanDTO = new SalesmanMasterDTO(id, firstName, "", "", "", "", "", "");
                salesmanList.add(salesmanDTO);
//                }
                // Set the items of the combo box
                cmbSalesChallanSalesman.setItems(salesmanList);
                cmbSalesChallanSalesman.setValue(salesmanList.get(0));

                cmbSalesChallanSalesman.setConverter(new StringConverter<SalesmanMasterDTO>() {
                    @Override
                    public String toString(SalesmanMasterDTO salesmanMasterDTO) {
                        return salesmanMasterDTO != null ? salesmanMasterDTO.getFirst_name() : "";
                    }

                    @Override
                    public SalesmanMasterDTO fromString(String s) {
                        return null;
                    }
                });
            }
        } catch (Exception e) {
//            e.printStackTrace();
//            soToscLogger.error("error in getting saleman " + e.getMessage());
        }
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
            tcSalesChallanSupplierName.setCellValueFactory(new PropertyValueFactory<>("supplierName"));
            tcSalesChallanSupplierInvNo.setCellValueFactory(new PropertyValueFactory<>("supplierInvNo"));
            tcSalesChallanSupplierInvDate.setCellValueFactory(new PropertyValueFactory<>("supplierInvDate"));
            tcSalesChallanSupplierBatch.setCellValueFactory(new PropertyValueFactory<>("supplierBatch"));
            tcSalesChallanSupplierMrp.setCellValueFactory(new PropertyValueFactory<>("supplierMrp"));
            tcSalesChallanSupplierQty.setCellValueFactory(new PropertyValueFactory<>("supplierQty"));
            tcSalesChallanSupplierRate.setCellValueFactory(new PropertyValueFactory<>("supplierRate"));
            tcSalesChallanSupplierCost.setCellValueFactory(new PropertyValueFactory<>("supplierCost"));
            tcSalesChallanSupplierDisper.setCellValueFactory(new PropertyValueFactory<>("supplierDisPer"));
            tcSalesChallanSupplierDisRs.setCellValueFactory(new PropertyValueFactory<>("supplierDisAmt"));

            tvSalesChallanSupplierDetails.setItems(supplierDataList);

        }
    }


    //    to get ledger data by id
    //todo:Set the Ledger Details at the Bottom in ledger Tab
    private void tranxLedgerDetailsFun(String id) {
        //todo: activating the ledger tab
        myTabPane.getSelectionModel().select(tabSalesChallanLedger);
        Map<String, String> map = new HashMap<>();
        map.put("ledger_id", id);
        String formData = Globals.mapToStringforFormData(map);
        System.out.println("FormData: " + formData);
        HttpResponse<String> response = APIClient.postFormDataRequest(formData, EndPoints.TRANSACTION_LEDGER_DETAILS);
        String responseBody = response.body();
        JsonObject jsonObject = new Gson().fromJson(responseBody, JsonObject.class);
        System.out.println("tranxLedgerDetailsFun Res ==>" + jsonObject);
        if (jsonObject.get("responseStatus").getAsInt() == 200) {
            JsonObject jsonObj = jsonObject.getAsJsonObject("result");
            //todo: getting values
            String gstNumber = jsonObj.get("gst_number").getAsString() != null ? jsonObj.get("gst_number").getAsString() : "";
            String area = jsonObj.get("area").getAsString() != null ? jsonObj.get("area").getAsString() : "";
            String bankName = jsonObj.get("bank_name").getAsString() != null ? jsonObj.get("bank_name").getAsString() : "";
            String contactName = jsonObj.get("contact_name").getAsString() != null ? jsonObj.get("contact_name").getAsString() : "";
            String creditDays = jsonObj.get("credit_days").getAsString() != null ? jsonObj.get("credit_days").getAsString() : "";
            String fssaiNumber = jsonObj.get("fssai_number").getAsString() != null ? jsonObj.get("fssai_number").getAsString() : "";
            String licenseNumber = jsonObj.get("license_number").getAsString() != null ? jsonObj.get("license_number").getAsString() : "";
            String route = jsonObj.get("route").getAsString() != null ? jsonObj.get("route").getAsString() : "";
//            String transport = jsonObj.get("route").getAsString();
            ledgerStateCode = jsonObj.get("stateCode").getAsString() != null ? jsonObj.get("stateCode").getAsString() : "";//new


            //todo: setting values
            lbSalesChallanGstNo.setText(gstNumber);
            lbSalesChallanArea.setText(area);
            lbSalesChallanBank.setText(bankName);
            lbSalesChallanContactPerson.setText(contactName);
//            lbSalesChallanTransport.setText(jsonObject.get("contact_name").getAsString());
            lbSalesChallanCreditDays.setText(creditDays);
            lbSalesChallanFssai.setText(fssaiNumber);
            lbSalesChallanLisenceNo.setText(licenseNumber);
            lbSalesChallanRoute.setText(route);
        }
    }


    // tp get ledgers all data
    public void fetchDataOfAllTransactionLedgers(String ledgerTypeFilter) {
        try {
            Map<String, String> body = new HashMap<>();
            body.put("searchText", ""); // Assuming search is not dependent on ledger type
            String requestBody = Globals.mapToString(body);
            HttpResponse<String> response = APIClient.postJsonRequest(requestBody, "transaction_ledger_list");
            JsonObject jsonObject = new Gson().fromJson(response.body(), JsonObject.class);
            ObservableList<TranxLedgerWindowDTO> observableList = FXCollections.observableArrayList();
            if (jsonObject.get("responseStatus").getAsInt() == 200) {
                JsonArray responseArray = jsonObject.get("list").getAsJsonArray();

                ledgerList = jsonObject.get("list").getAsJsonArray();

                if (responseArray.size() > 0) {
                    for (JsonElement element : responseArray) {
                        JsonObject item = element.getAsJsonObject();
                        String ledger_group_type = item.get("type").getAsString();
                        // Apply filter based on ledger type if provided
                        if (ledgerTypeFilter.isEmpty() || ledgerTypeFilter.equalsIgnoreCase(ledger_group_type)) {
                            // Proceed only if ledger type matches filter or filter is empty
                            String id = item.get("id").getAsString();
                            String code = item.get("code").getAsString();
                            String unique_code = item.get("unique_code").getAsString();
                            String ledger_name = item.get("ledger_name").getAsString();
                            String ledger_group = "";
                            if (ledger_group_type.equalsIgnoreCase("SC")) {
                                ledger_group = "Creditor";
                            } else if (ledger_group_type.equalsIgnoreCase("SD")) {
                                ledger_group = "Debtor";
                            }
                            String contact_number = item.get("contact_number").getAsString();
                            String current_balance = item.get("current_balance").getAsString();
                            String type = item.get("type").getAsString();
                            String salesmanId = item.get("salesmanId").getAsString();
                            String balancingMethod = item.get("balancingMethod").getAsString();
                            String balance_type = item.get("balance_type").getAsString();
                            String salesRate = item.get("salesRate").getAsString();

                            // Retrieving the gstDetails property
                            JsonArray gstDetailsJsonArray = item.getAsJsonArray("gstDetails");
                            ObservableList<GstDetailsDTO> gstDetailsList = FXCollections.observableArrayList();
                            System.out.println("gstDetailsJsonArray" + gstDetailsJsonArray);

                            for (JsonElement element1 : gstDetailsJsonArray) {
                                JsonObject item1 = element1.getAsJsonObject();
                                gstDetailsList.add(new GstDetailsDTO(item1.get("id").getAsString(), item1.get("gstNo").getAsString(), item1.get("state").getAsString()));
                            }

                            System.out.println("gstDetails: " + gstDetailsList);
                            observableList.add(new TranxLedgerWindowDTO(id, code, unique_code, ledger_name, ledger_group, contact_number, current_balance, type, salesmanId, balancingMethod, balance_type, gstDetailsList, salesRate));
                        }
                    }

                    // Check for null before setting cell value factories
                    if (tblcSalesChallanLedWindCode != null) {
                        tblcSalesChallanLedWindCode.setCellValueFactory(new PropertyValueFactory<>("code"));
                    }
                    if (tblcSalesChallanLedWindLedName != null) {
                        tblcSalesChallanLedWindLedName.setCellValueFactory(new PropertyValueFactory<>("ledger_name"));
                    }
                    if (tblcSalesChallanLedWindGrp != null) {
                        tblcSalesChallanLedWindGrp.setCellValueFactory(new PropertyValueFactory<>("ledger_group"));
                    }
                    if (tblcSalesChallanLedWindContNo != null) {
                        tblcSalesChallanLedWindContNo.setCellValueFactory(new PropertyValueFactory<>("contact_number"));
                    }
                    if (tblcSalesChallanLedWindCurrBal != null) {
                        tblcSalesChallanLedWindCurrBal.setCellValueFactory(new PropertyValueFactory<>("current_balance"));
                    }
                    if (tblcSalesChallanLedWindType != null) {
                        tblcSalesChallanLedWindType.setCellValueFactory(new PropertyValueFactory<>("type"));
                    }
                    if (tblcSalesChallanAdditionalChargesLedWindCode != null) {
                        tblcSalesChallanAdditionalChargesLedWindCode.setCellValueFactory(new PropertyValueFactory<>("code"));
                    }
                    if (tblcSalesChallanAdditionalChargesLedWindLedName != null) {
                        tblcSalesChallanAdditionalChargesLedWindLedName.setCellValueFactory(new PropertyValueFactory<>("ledger_name"));
                    }
                    if (tblcSalesChallanAdditionalChargesLedWindGrp != null) {
                        tblcSalesChallanAdditionalChargesLedWindGrp.setCellValueFactory(new PropertyValueFactory<>("ledger_group"));
                    }
                    if (tblcSalesChallanAdditionalChargesLedWindContNo != null) {
                        tblcSalesChallanAdditionalChargesLedWindContNo.setCellValueFactory(new PropertyValueFactory<>("contact_number"));
                    }
                    if (tblcSalesChallanAdditionalChargesLedWindCurrBal != null) {
                        tblcSalesChallanAdditionalChargesLedWindCurrBal.setCellValueFactory(new PropertyValueFactory<>("current_balance"));
                    }
                    if (tblcSalesChallanAdditionalChargesLedWindType != null) {
                        tblcSalesChallanAdditionalChargesLedWindType.setCellValueFactory(new PropertyValueFactory<>("type"));
                    }

                    if (tblvSalesChallanLedWindtableview != null) {
                        tblvSalesChallanLedWindtableview.setItems(observableList);
                    }
                    if (tblvSalesChallanAdditionalChargesLedWindtableview != null) {
                        tblvSalesChallanAdditionalChargesLedWindtableview.setItems(observableList);
                    }

                } else {
                    System.out.println("responseArray is empty");
                }
            } else {
                System.out.println("Error in response");
            }
        } catch (JsonSyntaxException e) {
            System.out.println("Error parsing JSON: " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    //    to get products data all
    public void fetchDataOfAllTransactionProducts() {

        try {
            Map<String, String> body = new HashMap<>();
            body.put("search", "");
            body.put("barcode", "");
            String requestBody = Globals.mapToString(body);
            HttpResponse<String> response = APIClient.postJsonRequest(requestBody, "transaction_product_list_new");
            JsonObject jsonObject = new Gson().fromJson(response.body(), JsonObject.class);
            ObservableList<TranxProductWindowDTO> observableList = FXCollections.observableArrayList();
            if (jsonObject.get("responseStatus").getAsInt() == 200) {
                JsonArray responseArray = jsonObject.get("list").getAsJsonArray();

                if (responseArray.size() > 0) {
                    tblvSalesChallananPrdWindtableview.setVisible(true);
                    for (JsonElement element : responseArray) {
                        JsonObject item = element.getAsJsonObject();
                        String id = item.get("id").getAsString();
                        String code = item.get("code").getAsString();
                        String product_name = item.get("product_name").getAsString();
                        String packing = item.get("packing").getAsString();
                        String barcode = item.get("barcode").getAsString() != null ? item.get("barcode").getAsString() : "";
                        String brand = item.get("brand").getAsString();
                        String mrp = item.get("mrp") != null ? item.get("mrp").getAsString() : "0.00";
                        //  System.out.println("heeelo"+item.get("current_stock"));
                        String current_stock = item.get("current_stock").getAsString();
                        String unit = item.get("unit").getAsString();
                        String sales_rate = item.get("sales_rate") != null ? item.get("sales_rate").getAsString() : "";
                        String is_negative = item.get("is_negative").getAsString();
                        String batch_expiry = item.get("batch_expiry") != null ? item.get("batch_expiry").getAsString() : "";
                        String tax_per = item.get("tax_per").getAsString();
                        String is_batch = item.get("is_batch").getAsString();
                        String purchaserate = item.get("purchaserate") != null ? item.get("purchaserate").getAsString() : "";

                        observableList.add(new TranxProductWindowDTO(id, code, product_name, packing, barcode, brand, mrp, current_stock, unit, sales_rate, is_negative, batch_expiry, tax_per, is_batch, purchaserate));
                    }

                    System.out.println(observableList);

                    tblcSalesChallananPrdWindCode.setCellValueFactory(new PropertyValueFactory<>("code"));
                    tblcSalesChallananPrdWindPrdName.setCellValueFactory(new PropertyValueFactory<>("product_name"));
                    tblcSalesChallananPrdWindPacking.setCellValueFactory(new PropertyValueFactory<>("packing"));
                    tblcSalesChallananPrdWindBarcode.setCellValueFactory(new PropertyValueFactory<>("barcode"));
                    tblcSalesChallananPrdWindBrand.setCellValueFactory(new PropertyValueFactory<>("brand"));
                    tblcSalesChallananPrdWindMrp.setCellValueFactory(new PropertyValueFactory<>("mrp"));
                    tblcSalesChallananPrdWindCurrStk.setCellValueFactory(new PropertyValueFactory<>("current_stock"));
                    tblcSalesChallananPrdWindUnit.setCellValueFactory(new PropertyValueFactory<>("unit"));
                    tblcSalesChallananPrdWindSaleRate.setCellValueFactory(new PropertyValueFactory<>("sales_rate"));

                    tblvSalesChallananPrdWindtableview.setItems(observableList);

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

    //    to create
    public void createSalesChallan() {
        LocalDate orderDate = LocalDate.parse(Communicator.text_to_date.fromString(dpSalesChallanCreateDate.getText()).toString());
//        LocalDate tranxDate = dpSalesChallanCreateDate.getValue();
        Double cgst = 0.00;
        Double totalCgst = 0.00;
        Double sgst = 0.00;
        Double totalSgst = 0.00;
        Double totalIgst = 0.00;
        for (GstDTO gstdto : tblvPurChallGST.getItems()) {
            cgst = Double.valueOf(gstdto.getCgst());
            totalCgst += cgst;
            sgst = Double.valueOf(gstdto.getCgst());
            totalSgst += sgst;
            totalIgst = totalSgst + totalCgst;

        }
        String id = Globals.salesChallanDTO != null ? Globals.salesChallanDTO.getId() : null;
        Map<String, String> map = new HashMap<>();

        if (id != null) {
            map.put("id", id);
            map.put("rowDelDetailsIds", deletedRows.toString());
        }

        map.put("bill_dt", String.valueOf(orderDate));
        map.put("bill_no", tfSalesChallanNo.getText());
//        map.put("sales_acc_id", cmbSalesChallanSalesAc.getValue().getId());
        map.put("sales_acc_id", "1");
        map.put("sales_sr_no", tfSalesChallanSrNo.getText());
        map.put("debtors_id", ledgerId);
//        map.put("gstNo", cmbSalesChallanSupplierGST.getValue().toString());
        map.put("gstNo", cmbSalesChallanSupplierGST.getValue() != null ? cmbSalesChallanSupplierGST.getValue().getGstNo() : "");
        map.put("gstType", cmbSalesChallanSupplierGST.getValue() != null ? cmbSalesChallanSupplierGST.getValue().getId() : "");
        map.put("isRoundOffCheck", String.valueOf(chbPurChallRoundOff.isSelected()));
        map.put("roundoff", "0.00");
        map.put("narration", tfSalesChallanNarrations.getText());
        map.put("totalamt", lblSCConvBillAmountEdit.getText());
        map.put("taxable_amount", lblSCConvTotalTaxableAmountEdit.getText());


//        map.put("gstNo", cmbSalesChallanSupplierGST.getValue() != null ? cmbSalesChallanSupplierGST.getValue().getGstNo() : "");
        if (!finaltotalCGST.isEmpty()) {
            map.put("totalcgst", finaltotalCGST);
        } else {
            map.put("totalcgst", "0");

        }
        if (!finaltotalSGST.isEmpty()) {
            map.put("totalsgst", finaltotalSGST);
        } else {
            map.put("totalsgst", "0");

        }
        if (!finaltotalIGST.isEmpty()) {
            map.put("totaligst", finaltotalIGST);
        } else {
            map.put("totaligst", "0");

        }

        if (!tfPurChallAllRowDisPer.getText().isEmpty()) {
            map.put("sales_discount", tfPurChallAllRowDisPer.getText());
        } else {
            map.put("sales_discount", "0");//static
        }
//        map.put("total_sales_discount_amt", "0.00");
        map.put("tcs", "0");//static
        map.put("total_sales_discount_amt", "0");//static


        if (!tfPurChallAllRowDisAmt.getText().isEmpty()) {
            map.put("sales_discount_amt", tfPurChallAllRowDisAmt.getText());//static
        }else {
            map.put("sales_discount_amt", "0");//static
        }
//        map.put("row", "[]");//static
//        map.put("additionalChargesTotal", "0");//static
//        System.out.println(" tfPurchaseChallanTotalAmt  ="+tfPurchaseChallanTotalAmt.getText().isEmpty());
        map.put("additionalChargesTotal", tfPurchaseChallanTotalAmt.getText().isEmpty() ? "0.0" : tfPurchaseChallanTotalAmt.getText());
        map.put("additionalCharges", "[]");
        map.put("sale_type", "sales_challan");//static
//        map.put("taxCalculation", "");//static
//        taxCalculation: {"cgst":[{"d_gst":12,"gst":6,"amt":0.23}],"sgst":[{"d_gst":12,"gst":6,"amt":0.23}]}
        if (ledgerStateCode.equalsIgnoreCase(GlobalTranx.getCompanyStateCode().toString())) {

            JsonArray jsonArray = new JsonArray();
            for (GstDTO gstDTO : tblvPurChallGST.getItems()) {
                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("d_gst", decimalFormat.format(Double.parseDouble(gstDTO.getTaxPer())));
                jsonObject.addProperty("gst", decimalFormat.format(Double.parseDouble(gstDTO.getTaxPer()) / 2));
                jsonObject.addProperty("amt", gstDTO.getCgst());
                jsonArray.add(jsonObject);
            }
            JsonObject jsonObject = new JsonObject();
            jsonObject.add("cgst", jsonArray);
            jsonObject.add("sgst", jsonArray);
            map.put("taxFlag", "true");//

            map.put("taxCalculation", jsonObject.toString());
        } else {
            JsonArray jsonArray = new JsonArray();
            for (GstDTO gstDTO : tblvPurChallGST.getItems()) {
                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("d_gst", decimalFormat.format(Double.parseDouble(gstDTO.getTaxPer())));
                jsonObject.addProperty("gst", decimalFormat.format(Double.parseDouble(gstDTO.getTaxPer())));
                jsonObject.addProperty("amt", gstDTO.getIgst());
                jsonArray.add(jsonObject);
            }
            JsonObject jsonObject = new JsonObject();
            jsonObject.add("igst", jsonArray);
//                jsonObject.add("sgst", jsonArray);
            map.put("taxFlag", "false");//
            map.put("taxCalculation", jsonObject.toString());
        }

        map.put("total_qty", "0");//static
        map.put("total_free_qty", "0.00");//static
        map.put("total_row_gross_amt", lblSCConvTotalTaxableAmountEdit.getText());
        map.put("total_base_amt", lblSCConvTotalTaxableAmountEdit.getText());
        map.put("total_invoice_dis_amt", lblSCConvTotalDiscountEdit.getText());
        map.put("total_tax_amt", lblSCConvTotalTaxEdit.getText());
        map.put("bill_amount", lblSCConvBillAmountEdit.getText());

        /***** mapping Row Data into TranxPurRowDTO *****/
//        map.put("taxCalculation", "");

        //new2.0 start
        List<CmpTRowDTOSoToSc> currentItems = new ArrayList<>(tblvSalesChallanCmpTRow.getItems());

        if (!currentItems.isEmpty()) {
            CmpTRowDTOSoToSc lastItem = currentItems.get(currentItems.size() - 1);

            if (lastItem.getParticulars() != null && lastItem.getParticulars().isEmpty()) {
                currentItems.remove(currentItems.size() - 1);
            }
        }

        List<CmpTRowDTOSoToSc> list = new ArrayList<>(currentItems);
        //new2.0 end


        ArrayList<TranxPurRowDTO> rowData = new ArrayList<>();
        for (CmpTRowDTOSoToSc cmpTRowDTOSoToSc : list) {
            System.out.println("cmpTRowDTOSoToSc" + cmpTRowDTOSoToSc);
            TranxPurRowDTO purParticularRow = new TranxPurRowDTO();
            purParticularRow.setProductId(cmpTRowDTOSoToSc.getProduct_id());
            if (!cmpTRowDTOSoToSc.getLevelA_id().isEmpty()) {
                purParticularRow.setLevelaId(cmpTRowDTOSoToSc.getLevelA_id());
            } else {
                purParticularRow.setLevelaId("");
            }
            if (!cmpTRowDTOSoToSc.getLevelB_id().isEmpty()) {
                purParticularRow.setLevelbId(cmpTRowDTOSoToSc.getLevelB_id());
            } else {
                purParticularRow.setLevelbId("");
            }
            if (!cmpTRowDTOSoToSc.getLevelC_id().isEmpty()) {
                purParticularRow.setLevelcId(cmpTRowDTOSoToSc.getLevelC_id());
            } else {
                purParticularRow.setLevelcId("");
            }
            if (!cmpTRowDTOSoToSc.getUnit().isEmpty()) {
                purParticularRow.setUnitId("1");
            } else {
                purParticularRow.setUnitId("");
            }
            if (cmpTRowDTOSoToSc.getUnit_conv() != null) {
                purParticularRow.setUnitConv(String.valueOf(cmpTRowDTOSoToSc.getUnit_conv()));
            } else {
                purParticularRow.setUnitConv("0.0");
            }
            if (!cmpTRowDTOSoToSc.getQuantity().isEmpty()) {
                purParticularRow.setQty(cmpTRowDTOSoToSc.getQuantity());
            } else {
                purParticularRow.setQty("0");
            }
            if (!cmpTRowDTOSoToSc.getFree().isEmpty()) {
                purParticularRow.setFreeQty(cmpTRowDTOSoToSc.getFree());
            } else {
                purParticularRow.setFreeQty("");
            }
            if (!cmpTRowDTOSoToSc.getRate().isEmpty()) {
                purParticularRow.setRate(cmpTRowDTOSoToSc.getRate());
            } else {
                purParticularRow.setRate("");
            }
            if (cmpTRowDTOSoToSc.getBase_amt() != null) {
                purParticularRow.setBaseAmt(String.valueOf(cmpTRowDTOSoToSc.getBase_amt()));
            } else {
                purParticularRow.setBaseAmt("");
            }
            if (!cmpTRowDTOSoToSc.getDis_amt().isEmpty()) {
                purParticularRow.setDisAmt(cmpTRowDTOSoToSc.getDis_amt());
            } else {
                purParticularRow.setDisAmt("0.0");
            }
            if (!cmpTRowDTOSoToSc.getDis_per().isEmpty()) {
                purParticularRow.setDisPer(cmpTRowDTOSoToSc.getDis_per());
            } else {
                purParticularRow.setDisPer("0.0");
            }
            if (cmpTRowDTOSoToSc.getDis_per2() != null && !cmpTRowDTOSoToSc.getDis_per2().isEmpty()) {
                purParticularRow.setDisPer2(cmpTRowDTOSoToSc.getDis_per2());
            } else {
                purParticularRow.setDisPer2("0.0");
            }
            if (cmpTRowDTOSoToSc.getDis_per_cal() != null) {
                purParticularRow.setDisPerCal(String.valueOf(cmpTRowDTOSoToSc.getDis_per_cal()));
            } else {
                purParticularRow.setDisPerCal("");
            }
            if (cmpTRowDTOSoToSc.getDis_amt_cal() != null) {
                purParticularRow.setDisAmtCal(String.valueOf(cmpTRowDTOSoToSc.getDis_amt_cal()));
            } else {
                purParticularRow.setDisAmtCal("");
            }
            if (cmpTRowDTOSoToSc.getRow_dis_amt() != null) {
                purParticularRow.setRowDisAmt(String.valueOf(cmpTRowDTOSoToSc.getRow_dis_amt()));
            } else {
                purParticularRow.setRowDisAmt("");
            }
            if (cmpTRowDTOSoToSc.getGross_amount() != null) {
                purParticularRow.setGrossAmt(String.valueOf(cmpTRowDTOSoToSc.getGross_amount()));
            } else {
                purParticularRow.setGrossAmt("");
            }
            if (cmpTRowDTOSoToSc.getAdd_chg_amt() != null) {
                purParticularRow.setAddChgAmt(String.valueOf(cmpTRowDTOSoToSc.getAdd_chg_amt()));
            } else {
                purParticularRow.setAddChgAmt("0.0");
            }
            if (cmpTRowDTOSoToSc.getGross_amount1() != null) {
                purParticularRow.setGrossAmt1(String.valueOf(cmpTRowDTOSoToSc.getGross_amount1()));
            } else {
                purParticularRow.setGrossAmt1("0.0");
            }
            if (cmpTRowDTOSoToSc.getInvoice_dis_amt() != null) {
                purParticularRow.setInvoiceDisAmt(String.valueOf(cmpTRowDTOSoToSc.getInvoice_dis_amt()));
            } else {
                purParticularRow.setInvoiceDisAmt("0.0");
            }
            if (cmpTRowDTOSoToSc.getTotal_amt() != null) {
                purParticularRow.setTotalAmt(String.valueOf(cmpTRowDTOSoToSc.getTotal_amt()));
            } else {
                purParticularRow.setTotalAmt("0.0");
            }
            if (cmpTRowDTOSoToSc.getGst() != null) {
                purParticularRow.setGst(String.valueOf(cmpTRowDTOSoToSc.getGst()));
            } else {
                purParticularRow.setGst("");
            }
            if (cmpTRowDTOSoToSc.getCgst() != null) {
                purParticularRow.setCgst(String.valueOf(cmpTRowDTOSoToSc.getCgst()));
            } else {
                purParticularRow.setCgst("");
            }
            if (cmpTRowDTOSoToSc.getIgst() != null) {
                purParticularRow.setIgst(String.valueOf(cmpTRowDTOSoToSc.getIgst()));
            } else {
                purParticularRow.setIgst("");
            }
            if (cmpTRowDTOSoToSc.getSgst() != null) {
                purParticularRow.setSgst(String.valueOf(cmpTRowDTOSoToSc.getSgst()));
            } else {
                purParticularRow.setSgst("");
            }
            if (cmpTRowDTOSoToSc.getTotal_igst() != null) {
                purParticularRow.setTotalIgst(String.valueOf(cmpTRowDTOSoToSc.getTotal_igst()));
            } else {
                purParticularRow.setTotalIgst("");
            }
            if (cmpTRowDTOSoToSc.getTotal_cgst() != null) {
                purParticularRow.setTotalCgst(String.valueOf(cmpTRowDTOSoToSc.getTotal_cgst()));
            } else {
                purParticularRow.setTotalCgst("");
            }
            if (cmpTRowDTOSoToSc.getTotal_sgst() != null) {
                purParticularRow.setTotalSgst(String.valueOf(cmpTRowDTOSoToSc.getTotal_sgst()));
            } else {
                purParticularRow.setTotalSgst("");
            }
            if (cmpTRowDTOSoToSc.getFinal_amount() != null) {
                purParticularRow.setFinalAmt(String.valueOf(cmpTRowDTOSoToSc.getFinal_amount()));
            } else {
                purParticularRow.setSgst("");
            }
            if (cmpTRowDTOSoToSc.getSgst() != null) {
                purParticularRow.setSgst(String.valueOf(cmpTRowDTOSoToSc.getSgst()));
            } else {
                purParticularRow.setSgst("");
            }
            if (cmpTRowDTOSoToSc.getDetails_id() != null) {
                purParticularRow.setDetailsId(String.valueOf(cmpTRowDTOSoToSc.getDetails_id()));
            } else {
                purParticularRow.setDetailsId("0");
            }
//hjkl
//            if (!cmpTRowDTOSoToSc.getB_rate().isEmpty()) {
//                purParticularRow.setbRate(CmpTRowDTOSoToSc.getB_rate());
//            } else {
//                purParticularRow.setbRate("0");
//            }
            if (!cmpTRowDTOSoToSc.getRate().isEmpty()) {
                purParticularRow.setSalesRate(cmpTRowDTOSoToSc.getB_rate());
            } else {
                purParticularRow.setSalesRate("0");
            }

            if (!cmpTRowDTOSoToSc.getIs_batch().isEmpty()) {
                purParticularRow.setBatch(cmpTRowDTOSoToSc.getIs_batch());
            } else {
                purParticularRow.setBatch("");
            }

            if (!cmpTRowDTOSoToSc.getB_no().isEmpty()) {
                purParticularRow.setBatch(cmpTRowDTOSoToSc.getB_no());
            } else {
                purParticularRow.setBatch("0");
            }
            if (!cmpTRowDTOSoToSc.getCosting().isEmpty()) {
                purParticularRow.setCosting(cmpTRowDTOSoToSc.getCosting());
            } else {
                purParticularRow.setCosting("0");
            }
            if (!cmpTRowDTOSoToSc.getCosting_with_tax().isEmpty()) {
                purParticularRow.setCostingWithTax(cmpTRowDTOSoToSc.getCosting_with_tax());
            } else {
                purParticularRow.setCostingWithTax("0");
            }
            if (!cmpTRowDTOSoToSc.getMin_margin().isEmpty()) {
                purParticularRow.setMinMargin(cmpTRowDTOSoToSc.getMin_margin());
            } else {
                purParticularRow.setMinMargin("0");
            }
            /*if (!cmpTRowDTOSoToSc.getManufacturing_date().isEmpty()) {
                purParticularRow.setManufacturingDate("2022-03-28");
            } else {
                purParticularRow.setManufacturingDate("2022-03-28");
            }*/
            if (!cmpTRowDTOSoToSc.getB_purchase_rate().isEmpty()) {
                purParticularRow.setbPurchaseRate(cmpTRowDTOSoToSc.getB_purchase_rate());
            } else {
                purParticularRow.setbPurchaseRate("0");
            }
           /* if (!cmpTRowDTOSoToSc.getB_expiry().isEmpty()) {
                purParticularRow.setbExpiry("2026-03-28");
            } else {
                purParticularRow.setbExpiry("2026-03-28");
            }*/
            if (!cmpTRowDTOSoToSc.getB_details_id().isEmpty()) {
                purParticularRow.setbDetailsId(cmpTRowDTOSoToSc.getB_details_id());
            } else {
                purParticularRow.setbDetailsId("0");
            }
            if (!cmpTRowDTOSoToSc.getIs_batch().isEmpty()) {
                purParticularRow.setBatch(cmpTRowDTOSoToSc.getIs_batch());
            } else {
                purParticularRow.setBatch("");
            }
            if (!cmpTRowDTOSoToSc.getSr_no().isEmpty()) {
                purParticularRow.setSerialNo(null);
            } else {
                purParticularRow.setSerialNo(null);
            }
            if (cmpTRowDTOSoToSc.getReference_id().isEmpty()) {
                purParticularRow.setReferenceId(cmpTRowDTOSoToSc.getReference_id());
            } else {
                purParticularRow.setReferenceId("");
            }
            if (cmpTRowDTOSoToSc.getReference_type().isEmpty()) {
                purParticularRow.setReferenceType(cmpTRowDTOSoToSc.getReference_type());
            } else {
                purParticularRow.setReferenceType("");
            }
            rowData.add(purParticularRow);
        }

        String mRowData = new Gson().toJson(rowData, new TypeToken<List<TranxPurRowDTO>>() {
        }.getType());
        System.out.println("mRowData=>" + mRowData);
        map.put("row", mRowData);
        String finalReq = Globals.mapToString(map);
        System.out.println("FinalReq=> i am in" + finalReq);

//        HttpResponse<String> response;
        String formData = Globals.mapToStringforFormData(map);
        System.out.println("formData" + formData);


        if (isRedirect == true) {
            System.out.println("purchase_id0" + purPrdRedId);

            purchase_id = purPrdRedId;
            System.out.println("purchase_id0----------" + purchase_id);


        }


        AlertUtility.CustomCallback callback = number -> {
            if (number == 1) {
                HttpResponse<String> response;
                //   if (Globals.salesChallanDTO == null) {
                if (purchase_id > 0) {
                    response = APIClient.postFormDataRequest(formData, EndPoints.SALES_CHALLAN_EDIT_ENDPOINT);
                    //? HIGHLIGHT
                    SalesChallanListController.editedSalesChallanId = id; //? Set the ID for editing

                } else {

                    response = APIClient.postFormDataRequest(formData, EndPoints.SALES_CHALLAN_CREATE_ENDPOINT);
                    //? HIGHLIGHT
                    SalesChallanListController.isNewSalesChallanCreated = true; //? Set the flag for new creation

                }


                JsonObject responseBody = new Gson().fromJson(response.body(), JsonObject.class);
                System.out.println("Response=>" + responseBody);
                message = responseBody.get("message").getAsString();


                if (responseBody.get("responseStatus").getAsInt() == 200) {
                    AlertUtility.AlertSuccessTimeout(AlertUtility.alertTypeSuccess, message, input -> {
                        GlobalController.getInstance().addTabStatic(SALES_CHALLAN_LIST_SLUG, false);
                    });
                } else {
                    AlertUtility.AlertErrorTimeout(AlertUtility.alertTypeError, message, in -> {
                    });
                }
            } else {
                System.out.println("working!");
            }
        };


//        if (Globals.salesChallanDTO.getId()!= null){
//            msg = LedgerMessageConsts.msgConfirmationOnUpdate;
//            msg += tfSalesChallanNo.getText() + LedgerMessageConsts.msgInvoiceNumber;
//        }else{
//            msg = LedgerMessageConsts.msgConfirmationOnSubmit;
//            msg += tfSalesChallanNo.getText() + LedgerMessageConsts.msgInvoiceNumber;
//        }
//        AlertUtility.AlertConfirmation(AlertUtility.alertTypeConfirmation, msg, input -> {
//            if (input == 1) {
//                createPurchaseInvoice();
//            }
//        });

        if (salesChallanDTO == null) {
            String msg = LedgerMessageConsts.msgConfirmationOnSubmit;
            msg = LedgerMessageConsts.msgConfirmationOnSubmit;
            msg += tfSalesChallanNo.getText() + LedgerMessageConsts.msgInvoiceNumber;
            AlertUtility.AlertConfirmation(AlertUtility.alertTypeConfirmation, msg, callback);
        } else {
            String msg = LedgerMessageConsts.msgConfirmationOnSubmit;
            msg = LedgerMessageConsts.msgConfirmationOnUpdate;
            msg += tfSalesChallanNo.getText() + LedgerMessageConsts.msgInvoiceNumber;
            AlertUtility.AlertConfirmation(AlertUtility.alertTypeConfirmation, msg, callback);
        }

    }

    //    to get by id
    public void getEditDataById() {
        APIClient apiClient = null;
        logger.debug("Get Edit by id Data Started...");
        Map<String, String> body = new HashMap<>();
        body.put("id", Globals.salesChallanDTO.getId());
        String formData = Globals.mapToStringforFormData(body);
//        HttpResponse<String> response = APIClient.postFormDataRequest(formData, EndPoints.SALES_ORDER_GET_DATA_BY_ID_ENDPOINT);
        apiClient = new APIClient(EndPoints.SALES_CHALLAN_GET_DATA_BY_ID_ENDPOINT, formData, RequestType.FORM_DATA);

        apiClient.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent workerStateEvent) {
                SalesChallanResponse responseBody = new Gson().fromJson(workerStateEvent.getSource().getValue().toString(), SalesChallanResponse.class);
//                SalesChallanResponse responseBody = new Gson().fromJson(response.body(), SalesChallanResponse.class);
                System.out.println("responseBody" + responseBody);
                if (responseBody.getResponseStatus() == 200) {
//                    System.out.println(jsonObject);
//                    System.out.println(jsonObject.getAsJsonObject());
//                    tfSalesChallanNarrations.setText(jsonObject.get("narration").getAsString());
//                    SalesChallanResponse responseBody = new Gson().fromJson(jsonObject, SalesChallanResponse.class);
                    System.out.println(responseBody);
                    System.out.println("ROW DATA" + responseBody.getRow());
                    setSalesChallanEditData(responseBody);
                }
            }
        });


        apiClient.setOnCancelled(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent workerStateEvent) {
                logger.error("Network API cancelled in getEditDataById()" + workerStateEvent.getSource().getValue().toString());

            }
        });
        apiClient.setOnFailed(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent workerStateEvent) {
                logger.error("Network API failed in getEditDataById()" + workerStateEvent.getSource().getValue().toString());
            }
        });
        apiClient.start();
        logger.debug("Get Edit data by id Data End...");


    }

    //to edit
    public void setSalesChallanEditData(SalesChallanResponse responseBody) {


        LocalDate orderDate = LocalDate.parse(responseBody.getInvoiceData().getBillDate());

        System.out.println("Res Bode:" + responseBody);
        System.out.println("Res Invoice:" + responseBody.getInvoiceData());
        System.out.println("Res Row:" + responseBody.getRow());

        tfSalesChallanSrNo.setText(responseBody.getInvoiceData().getSalesSrNo().toString());
        tfSalesChallanLedgerName.setText(responseBody.getInvoiceData().getDebtorName().toString());
//            System.out.println(responseBody.getInvoiceData().getSoBillNo().equals(null));
        tfSalesChallanNo.setText(responseBody.getInvoiceData().getScBillNo() == null ? "" : responseBody.getInvoiceData().getScBillNo().toString());
        tfSalesChallanNarrations.setText(responseBody.getInvoiceData().getNarration().toString());
        tfPurchaseChallanTotalAmt.setText(String.valueOf(responseBody.getAdditionalChargesTotal()));
        tfPurChallAllRowDisAmt.setText(responseBody.getDiscountInAmt().toString());
        tfPurChallAllRowDisPer.setText(responseBody.getDiscountInPer().toString());

        SelectedLedgerId = String.valueOf(responseBody.getInvoiceData().getDebtorId().longValue());
        int idToMatch = Integer.parseInt(responseBody.getInvoiceData().getDebtorId().toString()); // Example ID
//
//
//        LocalDate orderDate = LocalDate.parse(responseBody.getInvoiceData().getBillDate().toString());
        dpSalesChallanCreateDate.setText(orderDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        ledgerId = responseBody.getInvoiceData().getDebtorId().toString();

//        if(ledgerId!=null){
//            hlSaleInvCreateQuotation.setText((String) input[3]);
//            hlSaleInvCreateOrder.setText((String) input[4]);
//            getPendingSalesQuotationWithids();
//        }
//        ledgerId = responseBody.getInvoiceData().getDebtorId().toString();


//
//        chbPurChallRoundOff.setSelected(responseBody.getInvoiceData().getIsRoundOffCheck() == null ? false : responseBody.getInvoiceData().getIsRoundOffCheck());
        lblPurChallRoundOff.setText(responseBody.getInvoiceData().getRoundOff().toString());
//            lblSCConvBillAmountEdit.setText(invoiceData.get("totalamt").getAsString());
        lblSCConvTotalTaxableAmountEdit.setText(responseBody.getInvoiceData().getTaxableAmount().toString());
//        tblcSalesChallanCmpTRowGrossAmt.setText(responseBody.getGrossTotal().toString());
//            lblSCConvTotalDiscountEdit.setText(invoiceData.get("total_invoice_dis_amt").getAsString());
        lblSCConvTotalTaxEdit.setText(responseBody.getTotalTax().toString());
        lblSCConvBillAmountEdit.setText(responseBody.getInvoiceData().getTotalAmount().toString());
//
//        lblPurChallTotalQty.setText(responseBody.getTotalQty().toString());
//        lblPurChallFreeQty.setText(responseBody.getTotalFreeQty().toString());
        lblSCConvTotalDiscountEdit.setText(responseBody.getDiscountInAmt().toString());
        lblSCConvGrossTotalEdit.setText(responseBody.getGrossTotal().toString());
//        lblSCConvTotalTaxableAmountEdit.setText(responseBody.getTotalTax().toString());
//
//
        lblSCConvTotalTaxableAmountEdit.setText(responseBody.getInvoiceData().getTaxableAmount().toString());
//
        System.out.println("ia m in repo" + responseBody.getDiscountInPer());
        if (responseBody.getDiscountInPer() != null) {
            Platform.runLater(() -> {
                tfPurChallAllRowDisPer.setText(String.valueOf(responseBody.getDiscountInPer()));
                tfPurChallAllRowDisAmt.setText(String.valueOf(responseBody.getDiscountInAmt()));
            });

        }
        if (responseBody.getDiscountInAmt() != null) {
            Platform.runLater(() -> {
                tfPurChallAllRowDisAmt.setText(String.valueOf(responseBody.getAdditionalCharges()));
                tfPurChallAllRowDisAmt.setText(String.valueOf(responseBody.getDiscountInAmt()));
            });
        }


        tranxLedgerDetailsFun(ledgerId);

        if (responseBody.getGstDetails() != null) {

            System.out.println("GST" + responseBody.getGstDetails());

            //cmbSalesOrderClientGstin.setValue((responseBody.getInvoiceData().getGstNo()));
            ObservableList<GstDetailsDTO> gstDetailsList = FXCollections.observableArrayList();
            gstDetailsList.addAll(responseBody.getGstDetails());
            cmbSalesChallanSupplierGST.setItems(gstDetailsList);

            String gstNo = responseBody.getInvoiceData().getGstNo();
            for (GstDetailsDTO gstDetailsDTO : gstDetailsList) {
                if (gstDetailsDTO.getGstNo().equalsIgnoreCase(gstNo)) {
                    cmbSalesChallanSupplierGST.getSelectionModel().select(gstDetailsDTO);
                }
            }

            cmbSalesChallanSupplierGST.setConverter(new StringConverter<GstDetailsDTO>() {
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


//


        int index = 0;
        Double totaldis = 0.0, totalTax = 0.0;

        tblvSalesChallanCmpTRow.getItems().clear();
        for (SalesChallanRow mRow : responseBody.getRow()) {
            productId = String.valueOf(mRow.getProductId());
            System.out.println("Edit Data" + productId);
            getProductDetailsById(productId);

//                tranxProductDetailsFun(productId);
            getSupplierListbyProductId(productId);
            TranxSelectedProduct selectedProduct = TranxCommonPopUps.getSelectedProductFromProductId(mRow.getProductName());//new

//            CmpTRowDTOSoToSc row = new CmpTRowDTOSoToSc();
//            CmpTRowDTOSoToSc row = tblvSalesChallananPrdWindtableview.getItems().get(rowIndex);
            CmpTRowDTOSoToSc row = new CmpTRowDTOSoToSc("", "0", "1", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "");
            row.setSelectedProduct(selectedProduct);//new
            row.setUnit("");//new

//            row.setB_details_id(mRow.getbDetailsId().toString());
            row.setParticulars(mRow.getProductName());
            row.setProduct_id(mRow.getProductId().toString());
            row.setPackages((mRow.getPackName()));
            row.setQuantity(String.valueOf(mRow.getQty().intValue()));
//            row.setB_no(mRow.getBatchNo());
            row.setBatch_or_serial(mRow.getBatchNo());
//            row.setUnit(mRow.getUnitName());
            row.setUnit_id(String.valueOf(mRow.getUnitId()));
            row.setUnit_conv(String.valueOf(mRow.getUnitConv()));

            row.setRate(String.valueOf(mRow.getRate()));
            row.setGross_amount(String.valueOf(mRow.getGrossAmt()));
            row.setDis_per(String.valueOf(mRow.getDisPer()));
            row.setDis_amt(String.valueOf(mRow.getDisAmt()));
            row.setTax(String.valueOf(mRow.getIgst()));

//            row.setFinal_amount(mRow.getFinalAmt().toString());
            System.out.println("mRow Gross AMt1:" + mRow.getGrossAmt1());
            row.setGross_amount1(mRow.getGrossAmt1().toString());

            row.setFree(String.valueOf(mRow.getFreeQty()));
            row.setIgst(mRow.getIgst().toString());
            row.setSgst(mRow.getSgst().toString());
            row.setCgst(mRow.getCgst().toString());
            row.setIgst_per(mRow.getIgst().toString());
            row.setSgst_per(mRow.getSgst().toString());
            row.setCgst_per(mRow.getCgst().toString());
            row.setTotal_igst(mRow.getTotalIgst().toString());
            row.setTotal_sgst(mRow.getTotalSgst().toString());
            row.setTotal_cgst(mRow.getTotalCgst().toString());
//            row.setTax(String.valueOf(mRow.getGst()).toString());
            row.setTaxable_amt(mRow.getGrossAmt().toString());
            row.setDetails_id(String.valueOf(mRow.getDetailsId()));
            row.setGross_amount(String.valueOf(mRow.getGrossAmt()));
//            row.setTax("0");
            row.setLevelA_id(String.valueOf(mRow.getLevelAId()));
            row.setLevelB_id(String.valueOf(mRow.getLevelBId()));
            row.setLevelC_id(String.valueOf(mRow.getLevelCId()));
//            row.setUnit_conv(mRow.getUnitConv().toString());
            row.setBase_amt(String.valueOf(mRow.getBaseAmt()));
            row.setB_rate(String.valueOf(mRow.getbRate()));
            row.setRate_a(String.valueOf(mRow.getMinRateA()));
            row.setRate_b(String.valueOf(mRow.getMinRateB()));
            row.setRate_c(String.valueOf(mRow.getMinRateC()));
            row.setCosting(String.valueOf(mRow.getCosting()));
            row.setCosting_with_tax(String.valueOf(mRow.getCostingWithTax()));
            row.setMin_margin(String.valueOf(mRow.getMinMargin()));
            row.setB_expiry(mRow.getbExpiry());
//                row.setIs_batch(String.valueOf(mRow.getIsBatch()));
            row.setIs_batch(String.valueOf(mRow.getIsBatch()));

            row.setManufacturing_date(String.valueOf(mRow.getManufacturingDate()));
            row.setB_purchase_rate(String.valueOf(mRow.getPurchaseRate()));
            row.setB_details_id(String.valueOf(mRow.getbDetailsId()));

            row.setFree(String.valueOf(mRow.getFreeQty()));
            row.setSr_no("");
            row.setReference_id("");
            row.setReference_type("");
            row.setGst(String.valueOf(Double.valueOf(mRow.getDetailsId())));
            row.setBase_amt(String.valueOf(0.0));
            row.setRow_dis_amt(String.valueOf(0.0));
            row.setDis_per2(String.valueOf(mRow.getDisPer2()));
            row.setTotal_amt(String.valueOf(0.0));
            row.setIgst(mRow.getIgst().toString());
            row.setCgst(mRow.getCgst().toString());
            row.setSgst(mRow.getSgst().toString());
            row.setTotal_igst(mRow.getTotalIgst().toString());
            row.setTotal_sgst(mRow.getTotalSgst().toString());
            row.setTotal_cgst(mRow.getTotalCgst().toString());
//                row.setFinal_amt(mRow.getFinalAmt()).toString();
            row.setInvoice_dis_amt(mRow.getInvoiceDisAmt().toString());
            row.setDis_per_cal(mRow.getDisPerCal().toString());
            row.setDis_amt_cal(mRow.getDisAmtCal().toString());
            row.setNet_amount(String.valueOf(mRow.getFinalAmt()));
            // row.setRo(mRow.get());
//            CmpTRowDTOSoToSc.setIs_batch(String.valueOf(mRow.getIsBatch()));

            //new start
            List<LevelAForPurInvoice> levelAForPurInvoiceList = ProductUnitsPacking2.getAllProductUnitsPackingFlavour(row.getProduct_id());
            System.out.println("levelAForPurInvoiceList" + levelAForPurInvoiceList);

            //                int index = getTableRow().getIndex();
            ObservableList<LevelAForPurInvoice> observableLevelAList = FXCollections.observableArrayList(levelAForPurInvoiceList);
            PurInvoiceCommunicator.levelAForPurInvoiceObservableList.add(observableLevelAList);
            //new end

            totaldis = totaldis + (mRow.getRowDisAmt());
            System.out.println("totaldis" + totaldis);
            totalTax = Double.valueOf(totalTax + (mRow.getTotalIgst()));
            System.out.println("totalTax" + totalTax);

            String formattedDiscount = String.format("%.2f", totaldis);
            lbSCBatchDisAmt.setText(formattedDiscount);
            String formattedTax = String.format("%.2f", totalTax);
            lbSalesChallanTax.setText(formattedTax);
//                lbSalesChallanDiscount.setText(String.valueOf(totaldis));
//                lbSalesChallanTotalTax.setText(String.valueOf(totalTax));

            System.out.println("EDIT DATA SET ROW =>" + row);
            tblvSalesChallanCmpTRow.getItems().add(row);
            SalesChallanCalculations.rowCalculationForPurcInvoice(index, tblvSalesChallanCmpTRow, callback);

            /*if (index < 1)
                tblvSalesChallananPrdWindtableview.getItems().set(index, row);
            else */

            index++;
//            rowCalculation(index);
// Add "+" symbol in the last column

//                tcSalesOrderSrNo.setCellFactory(column -> {
//                    return new TableCell<>() {
//                        @Override
//                        protected void updateItem(String item, boolean empty) {
//                            super.updateItem(item, empty);
//
//                            if (empty || getIndex() < 0) {
//                                setText(null);
//                            } else {
//                                // Set the serial number as the index of the row + 1
//                                setText(String.valueOf(getIndex() + 1));
//                            }
//                        }
//                    };
//                });


        }
//        tblvSalesChallanCmpTRow.refresh();

        SalesChallanCalculations.calculateGst(tblvSalesChallanCmpTRow, callback);

    }

    private void setGSTFooterPart() {
        tblvPurChallGST.setFocusTraversable(false);
        tblcPurChallGST.setSortable(false);
        tblcPurChallCGST.setSortable(false);
        tblcPurChallSGST.setSortable(false);
        tblcPurChallIGST.setSortable(false);

        tblcPurChallGST.setCellValueFactory(cellData -> {
            StringProperty taxProperty = cellData.getValue().taxPerProperty();
            return Bindings.createStringBinding(

                    () -> "(" + (int) Double.parseDouble(!taxProperty.get().isEmpty() ? taxProperty.get() : "0.0") + " %)", taxProperty);
        });

        tblcPurChallGST.setStyle("-fx-alignment: CENTER;");
        tblcPurChallCGST.setCellValueFactory(cellData -> cellData.getValue().cgstProperty());
        tblcPurChallCGST.setCellFactory(col -> new TableCell<GstDTO, String>() {
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


        tblcPurChallCGST.setStyle("-fx-alignment: CENTER;");
        tblcPurChallSGST.setCellValueFactory(cellData -> cellData.getValue().sgstProperty());
        tblcPurChallSGST.setStyle("-fx-alignment: CENTER;");

        tblcPurChallSGST.setCellFactory(col -> new TableCell<GstDTO, String>() {
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
        tblcPurChallIGST.setCellValueFactory(cellData -> cellData.getValue().igstProperty());
        tblcPurChallIGST.setStyle("-fx-alignment: CENTER;");

        tblcPurChallIGST.setCellFactory(col -> new TableCell<GstDTO, String>() {
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

        tblvPurChallGST.getItems().addAll(new GstDTO("", "", "", ""));

    }


//    TableCellCallback<Object[]> callback = item -> {
//        System.out.println("item: " + item[0]);
//        System.out.println("item: " + item[1]);
//        System.out.println("item: " + item[2]);
//        System.out.println("item: " + item[3]);
//        System.out.println("item: " + item[4]);
//        System.out.println("item: " + item[5]);
//        System.out.println("item: " + item[6]);
//        System.out.println("item: " + item[7]);
//        System.out.println("item: " + item[8]);
//        System.out.println("taxCalculation " + item[9]);
//
//        tblvPurChallGST.getItems().clear();
//        ObservableList<GstDTO> gstDTOObservableList = FXCollections.observableArrayList();
//        ObservableList<?> item9List = (ObservableList<?>) item[9];
//        for (Object obj : item9List) {
//            if (obj instanceof GstDTO) {
//                gstDTOObservableList.add((GstDTO) obj);
//            }
//        }
//
//        tblvPurChallGST.getItems().addAll(gstDTOObservableList);
////        tvGST_Table.getItems().addAll(gstDTOObservableList);
//
//
//        JsonArray jsonArray = new JsonArray();
//        for (GstDTO gstDTO : gstDTOObservableList) {
//            JsonObject jsonObject = new JsonObject();
//            jsonObject.addProperty("d_gst", decimalFormat.format(Double.parseDouble(gstDTO.getTaxPer())));
//            jsonObject.addProperty("gst", decimalFormat.format(Double.parseDouble(gstDTO.getTaxPer()) / 2));
//            jsonObject.addProperty("amt", gstDTO.getCgst());
//            jsonArray.add(jsonObject);
//        }
//        JsonObject jsonObject = new JsonObject();
//        jsonObject.add("cgst", jsonArray);
//        jsonObject.add("sgst", jsonArray);
//        purchase_invoice_map.put("taxCalculation", jsonObject.toString());
//
//        lblSCConvBillAmountEdit.setText((String) item[2]); //totalNetAmt for bill amount
//        totalamt = (String) item[2];
//
//        total_purchase_discount_amt = (String) item[4];
//        if (ledgerStateCode.equalsIgnoreCase(GlobalTranx.getCompanyStateCode().toString())) {
//            taxFlag = true;
//        }
//
//
//        purchase_invoice_map.put("total_qty", (String) item[0]);
//        purchase_invoice_map.put("total_free_qty", (String) item[1]);
//
//        purchase_invoice_map.put("total_base_amt", (String) item[3]);
//        purchase_invoice_map.put("total_invoice_dis_amt", (String) item[4]);
//        purchase_invoice_map.put("taxable_amount", (String) item[5]);
//        purchase_invoice_map.put("bill_amount", (String) item[2]);
//        purchase_invoice_map.put("total_tax_amt", (String) item[6]);
//        purchase_invoice_map.put("totaligst", (String) item[6]);
//        purchase_invoice_map.put("totalsgst", (String) item[7]);
//        purchase_invoice_map.put("totalcgst", (String) item[8]);
//
//
//        purchase_invoice_map.put("taxFlag", String.valueOf(taxFlag));
//
//        total_taxable_amt = Double.parseDouble((String) item[5]);
//
//
//        lblSCConvTotalTaxableAmountEdit.setText((String) item[3]);
//        lblSCConvTotalDiscountEdit.setText((String) item[4]);
//        lblSCConvTotalTaxableAmountEdit.setText((String) item[5]);
//        lblSCConvTotalTaxEdit.setText((String) item[6]);
//
//    };


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

        tblvPurChallGST.getItems().clear();
        ObservableList<GstDTO> gstDTOObservableList = FXCollections.observableArrayList();
        ObservableList<?> item9List = (ObservableList<?>) item[9];
        for (Object obj : item9List) {
            if (obj instanceof GstDTO) {
                gstDTOObservableList.add((GstDTO) obj);
            }
        }
        tblvPurChallGST.getItems().addAll(gstDTOObservableList);
        if (cmbSalesChallanSupplierGST.getValue() != null && Integer.valueOf(cmbSalesChallanSupplierGST.getValue().getState()) != GlobalTranx.getCompanyStateCode()) {
            //? Igst Calculate
            tblcPurChallIGST.setVisible(true);
            tblcPurChallSGST.setVisible(false);
            tblcPurChallCGST.setVisible(false);
//            tblcPurChallGST.setPrefWidth(20.0);
//            tblcPurChallIGST.setPrefWidth(50.0);
//            tblcPurChallSGST.setPrefWidth(50.0);
//            tblcPurChallCGST.setPrefWidth(50.0);
        } else {
            //? Cgst Calculate
            tblcPurChallIGST.setVisible(false);
            tblcPurChallSGST.setVisible(true);
            tblcPurChallCGST.setVisible(true);
//            tblcPurChallGST.setPrefWidth(20.0);
//            tblcPurChallIGST.setPrefWidth(50.0);
//            tblcPurChallSGST.setPrefWidth(50.0);
//            tblcPurChallCGST.setPrefWidth(50.0);
        }
        JsonObject taxCalObject = new JsonObject();
        if (ledgerStateCode.equalsIgnoreCase(GlobalTranx.getCompanyStateCode().toString())) {
            JsonArray jsonArray = new JsonArray();
            for (GstDTO gstDTO : gstDTOObservableList) {
                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("d_gst", decimalFormat.format(Double.parseDouble(gstDTO.getTaxPer())));
                jsonObject.addProperty("gst", decimalFormat.format(Double.parseDouble(gstDTO.getTaxPer()) / 2));
                jsonObject.addProperty("amt", gstDTO.getCgst());
                jsonArray.add(jsonObject);
            }
            taxCalObject.add("cgst", jsonArray);
            taxCalObject.add("sgst", jsonArray);
        } else {
            JsonArray jsonArray = new JsonArray();
            for (GstDTO gstDTO : gstDTOObservableList) {
                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("d_gst", decimalFormat.format(Double.parseDouble(gstDTO.getTaxPer())));
                jsonObject.addProperty("gst", decimalFormat.format(Double.parseDouble(gstDTO.getTaxPer())));
                jsonObject.addProperty("amt", gstDTO.getIgst());
                jsonArray.add(jsonObject);
            }
            taxCalObject.add("igst", jsonArray);
//            jsonObject.add("sgst", jsonArray);
        }
        purchase_invoice_map.put("taxCalculation", taxCalObject.toString());

        totalamt = (String) item[2];


        total_purchase_discount_amt = (String) item[4];


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


        System.out.println("callBack TotalTaxableAMount :: >> " + Double.parseDouble((String) item[5]));
        total_taxable_amt = Double.parseDouble((String) item[5]);
//        lblPurChallTotalQty.setText((String) item[0]);
//        lblPurChallFreeQty.setText((String) item[1]);
//        lblPurChallGrossTotal.setText((String) item[3]);
//        lblPurChallTotalDiscount.setText((String) item[4]);
//        lblPurChallTotalTaxableAmount.setText((String) item[5]);
//        lblPurChallTotalTax.setText((String) item[6]);
        lblPurChallTotalCGST.setText((String) item[7]);
        lblPurChallTotalSGST.setText((String) item[8]);

        lblSCConvGrossTotalEdit.setText((String) item[3]);
        lblSCConvTotalDiscountEdit.setText((String) item[4]);
        lblSCConvTotalTaxableAmountEdit.setText((String) item[5]);
        lblSCConvTotalTaxEdit.setText((String) item[6]);


        lbtotalQty.setText((String) item[0]);

//        lbtotalQty.setText(String.valueOf(totalQty));
        lbTFreeQty.setText((String) item[1]);
//        tblvSalesChallanCmpTRow.refresh();

        /**
         * Info : RoundOff Calculation Method
         * @Author : Vinit Chilaka
         */
        chbPurChallRoundOff.setSelected(true);
        hbPurChallGstTotal.setVisible(true);

        roundOffCalculations();


    };

    TableCellCallback<Object[]> delCallback = item -> {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("del_id", item[0].toString());
        deletedRows.add(jsonObject);

//        SalesChallanCalculations.rowCalculationForPurcInvoice(item[0],tblvSalesChallanCmpTRow,callback);
//        SalesChallanCalculations.calculateGst(tblvSalesChallanCmpTRow,callback);
        SalesChallanCalculations.discountPropotionalCalculation(tfPurChallAllRowDisPer.getText(), tfPurChallAllRowDisAmt.getText(), tfPurchaseChallanTotalAmt.getText(), tblvSalesChallanCmpTRow, callback);
        SalesChallanCalculations.calculateGst(tblvSalesChallanCmpTRow, callback);
    };

    private void roundOffCalculations() {
        Double billamt = 0.00; // local variable to store row wise  Bill amount
        Double total_bill_amt = 0.00; // local variable to store Final bill Amount without roundoff
        Double final_r_off_bill_amt = 0.00; // local variable to store Final Bill amount with round off
        Double roundOffAmt = 0.00; // local variable to store Roundoff Amount
        for (CmpTRowDTOSoToSc cmpTRowDTO : tblvSalesChallanCmpTRow.getItems()) {
            billamt = cmpTRowDTO.getNet_amount().isEmpty() ? 0.0 : Double.valueOf(cmpTRowDTO.getNet_amount());
            total_bill_amt += billamt;
        }
        final_r_off_bill_amt = (double) Math.round(total_bill_amt);
        roundOffAmt = final_r_off_bill_amt - total_bill_amt;

        if (chbPurChallRoundOff.isSelected()) {
            lblSCConvBillAmountEdit.setText(String.format("%.2f", final_r_off_bill_amt));
            lblPurChallRoundOff.setText(String.format("%.2f", roundOffAmt));
        } else {
            lblSCConvBillAmountEdit.setText(String.format("%.2f", total_bill_amt));
            lblPurChallRoundOff.setText("0.00");
        }

    }

    TableCellCallback<Object[]> batchID_callback = item -> {
//        getProductBatchsById(item);
        System.out.println("i am the Data of Batch" + item);
        getProductBatchsById((String) item[0], (String) item[1]);

    };


    TableCellCallback<String> productID_callback = item -> {
        System.out.println("in batch row");
        System.out.println(item);
        getProductDetailsById(item);
        getSupplierListbyProductId(item);

//        getSupplierListbyProductId(item);
    };

    //new start
    TableCellCallback<Integer> unit_callback = currentIndex -> {
        System.out.println("i am in" + currentIndex);
        System.out.println("i am in ledgerStateCode" + ledgerStateCode);
        CmpTRowDTOSoToSc tranxRow = tblvSalesChallanCmpTRow.getItems().get(currentIndex);
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
//        tblvSalesChallanCmpTRow.getItems().set(currentIndex, tranxRow);
    };
    //new end

    TableCellCallback<String> addRow = item -> {
        System.out.println("in add Row");
        System.out.println(item);
//        tblvSalesChallanCmpTRow.getItems().addAll(new CmpTRowDTOSoToSc("", "0", "1", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", ""));

    };
    TableCellCallback<Object[]> addPrdCalbak = item -> {
        if (item != null) {
            System.out.println("item [0] " + item[0]);
            System.out.println("item [0] " + item[1]);
            if ((Boolean) item[0] == true) {
                rediCurrIndex = (Integer) item[1];
                String isProduct = (String) item[2];
                if (isProduct.equalsIgnoreCase("isProduct")) {
                    isProductRed = true;
                }
                setPurChallDataToProduct();
            }
            System.out.println("isProductRed >> " + isProductRed);
        }
    };


//    private void setAddChargesInTable() {
//        for (AddChargesListDTO addChgLst : addChargesResLst) {
//            CustomAddChargesDTO customAddChargesDTO = new CustomAddChargesDTO();
//            customAddChargesDTO.setAdditional_charges_details_id(String.valueOf(addChgLst.getAdditionalChargesDetailsId()));
//            customAddChargesDTO.setLedgerId(String.valueOf(addChgLst.getLedgerId()));
//            customAddChargesDTO.setAmt(String.valueOf(addChgLst.getAmt()));
//            customAddChargesDTO.setPercent(String.valueOf(addChgLst.getPercent()));
//            addChargesDTOList.add(customAddChargesDTO);
//        }
//    }


    public void tableInitialization() {
        tblvSalesChallanCmpTRow.refresh();

        tblvSalesChallanCmpTRow.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tblvSalesChallanCmpTRow.setEditable(true);
        tblvSalesChallanCmpTRow.setFocusTraversable(false);

        Label headerLabel = new Label("Sr No.");
        tblcSalesChallanCmpTRowSr.setGraphic(headerLabel);


        if (purchase_id > 0) {

            int count = 1;
            int index = 0;
            for (CmpTRowDTOSoToSc rowListDTO : rowListDTOS) {
                System.out.println("RowListDTO => " + rowListDTO);

                CmpTRowDTOSoToSc purchaseInvoiceTable = new CmpTRowDTOSoToSc();
                purchaseInvoiceTable.setId(String.valueOf(purchase_id));
                purchaseInvoiceTable.setSr_no(String.valueOf(count));
                purchaseInvoiceTable.setDetails_id(String.valueOf(rowListDTO.getDetails_id()));
                purchaseInvoiceTable.setProduct_id(String.valueOf(rowListDTO.getProduct_id()));
//                purchaseInvoiceTable.setParticulars(String.valueOf(rowListDTO.getProductName()));

                purchaseInvoiceTable.setLevelA_id(String.valueOf(rowListDTO.getLevelA_id()));
                purchaseInvoiceTable.setLevelB_id(String.valueOf(rowListDTO.getLevelB_id()));
                purchaseInvoiceTable.setLevelC_id(String.valueOf(rowListDTO.getLevelC_id()));
                purchaseInvoiceTable.setUnit_id(String.valueOf(rowListDTO.getUnit_id()));
                purchaseInvoiceTable.setUnit_conv(String.valueOf(rowListDTO.getUnit_conv()));

                purchaseInvoiceTable.setPackages(String.valueOf(rowListDTO.getPackages()));

                purchaseInvoiceTable.setQuantity(String.valueOf(rowListDTO.getQuantity()));
                purchaseInvoiceTable.setRate(String.valueOf(rowListDTO.getRate()));

                purchaseInvoiceTable.setBase_amt(String.valueOf(rowListDTO.getBase_amt()));

                purchaseInvoiceTable.setDis_amt(String.valueOf(rowListDTO.getDis_amt()));
                purchaseInvoiceTable.setDis_per(String.valueOf(rowListDTO.getDis_per()));
                purchaseInvoiceTable.setDis_per_cal(String.valueOf(rowListDTO.getDis_per_cal()));
                purchaseInvoiceTable.setDis_amt_cal(String.valueOf(rowListDTO.getDis_amt_cal()));
                purchaseInvoiceTable.setTotal_amt(String.valueOf(rowListDTO.getTotal_amt()));


                purchaseInvoiceTable.setGst(String.valueOf(rowListDTO.getGst()));
                purchaseInvoiceTable.setIgst_per(String.valueOf(rowListDTO.getIgst()));
                purchaseInvoiceTable.setSgst_per(String.valueOf(rowListDTO.getSgst()));
                purchaseInvoiceTable.setCgst_per(String.valueOf(rowListDTO.getCgst()));

                purchaseInvoiceTable.setIgst(String.valueOf(rowListDTO.getIgst()));
                purchaseInvoiceTable.setCgst(String.valueOf(rowListDTO.getCgst()));
                purchaseInvoiceTable.setSgst(String.valueOf(rowListDTO.getSgst()));

                purchaseInvoiceTable.setNet_amount(String.valueOf(rowListDTO.getFinal_amount()));
                purchaseInvoiceTable.setFree(String.valueOf(rowListDTO.getFree()));
                purchaseInvoiceTable.setRow_dis_amt(String.valueOf(rowListDTO.getRow_dis_amt()));

                purchaseInvoiceTable.setGross_amount(String.valueOf(rowListDTO.getGross_amount()));
                purchaseInvoiceTable.setGross_amount1(String.valueOf(rowListDTO.getGross_amount1()));

                purchaseInvoiceTable.setAdd_chg_amt(String.valueOf(rowListDTO.getAdd_chg_amt()));


                purchaseInvoiceTable.setInvoice_dis_amt(String.valueOf(rowListDTO.getInvoice_dis_amt()));
                purchaseInvoiceTable.setInventoryId(String.valueOf(rowListDTO.getInventoryId()));

//                purchaseInvoiceTable.setTransaction_status(String.valueOf(rowListDTO.getTransactionStatus()));

//                purchaseInvoiceTable.setIs_expired(String.valueOf(rowListDTO.getIsExpired()));


                purchaseInvoiceTable.setB_details_id(String.valueOf(rowListDTO.getB_details_id()));

//                purchaseInvoiceTable.setB_no(String.valueOf(rowListDTO.getBatchNo()));
                purchaseInvoiceTable.setB_no(String.valueOf(rowListDTO.getB_no()));
                purchaseInvoiceTable.setBatch_or_serial(String.valueOf(rowListDTO.getBatch_or_serial()));
                purchaseInvoiceTable.setB_expiry(String.valueOf(rowListDTO.getB_expiry()));
                purchaseInvoiceTable.setB_rate(String.valueOf(rowListDTO.getB_rate()));
                purchaseInvoiceTable.setB_purchase_rate(String.valueOf(rowListDTO.getB_purchase_rate()));
                purchaseInvoiceTable.setIs_batch(String.valueOf(rowListDTO.getIs_batch()));

//                purchaseInvoiceTable.setRate_a(String.valueOf(rowListDTO.getMinRateA() == null ? "" : rowListDTO.getMinRateA()));
//                purchaseInvoiceTable.setRate_b(String.valueOf(rowListDTO.getMinRateB() == null ? "" : rowListDTO.getMinRateB()));
//                purchaseInvoiceTable.setRate_c(String.valueOf(rowListDTO.getMinRateC() == null ? "" : rowListDTO.getMinRateB()));
                // purchaseInvoiceTable.setRate_d(String.valueOf(rowListDTO.getMinRateD()));

                purchaseInvoiceTable.setManufacturing_date(String.valueOf(rowListDTO.getManufacturing_date()));
//                purchaseInvoiceTable.setMin_margin(String.valueOf(rowListDTO.getMarginPer()));

                purchaseInvoiceTable.setCosting(String.valueOf(rowListDTO.getCosting()));
                purchaseInvoiceTable.setCosting_with_tax(String.valueOf(rowListDTO.getCosting()));
                purchaseInvoiceTable.setLedger_id(String.valueOf(rowListDTO.getCosting()));
//                purchaseInvoiceTable.setLedger_id(invoice_data_map.get("supplierId"));
                purchaseInvoiceTable.setTax(String.valueOf(rowListDTO.getGst()));


                // !Sales Invoice
                TranxSelectedProduct selectedProduct = TranxCommonPopUps
                        .getSelectedProductFromProductId(rowListDTO.getProduct_id());
                purchaseInvoiceTable.setSelectedProduct(selectedProduct);
//                 TranxSelectedBatch selectedBatch =
//                 TranxCommonPopUps.getSelectedBatchFromProductId(rowListDTO,
//                 invoice_data_map.get(""), currRowRes.getBatchNo());
                purchaseInvoiceTable.setSelectedBatch(rowListDTO.getSelectedBatch());

                BatchWindowTableDTO batchWindowTableDTO = new BatchWindowTableDTO();

                batchWindowTableDTO.setProductId(purchaseInvoiceTable.getProduct_id());
                batchWindowTableDTO.setLevelAId(purchaseInvoiceTable.getLevelA_id());
                batchWindowTableDTO.setLevelBId(purchaseInvoiceTable.getLevelB_id());
                batchWindowTableDTO.setLevelCId(purchaseInvoiceTable.getLevelC_id());
                batchWindowTableDTO.setUnitId(purchaseInvoiceTable.getUnit_id());

                batchWindowTableDTO.setBatchNo(purchaseInvoiceTable.getBatch_or_serial());
                batchWindowTableDTO.setB_details_id(purchaseInvoiceTable.getB_details_id());
                LocalDate manu_dt = null;
//                if (!purchaseInvoiceTable.getManufacturing_date().isEmpty()) {
//                    manu_dt = LocalDate.parse(purchaseInvoiceTable.getManufacturing_date());
//                    batchWindowTableDTO.setManufacturingDate(manu_dt.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
//                } else {
//                    batchWindowTableDTO.setManufacturingDate("");
//                }
//                LocalDate exp_dt = null;
//                if (purchaseInvoiceTable.getB_expiry().isEmpty()) {
//                    exp_dt = LocalDate.parse(purchaseInvoiceTable.getB_expiry());
//                    batchWindowTableDTO.setExpiryDate(exp_dt.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
//                } else {
//                    batchWindowTableDTO.setExpiryDate("");
//                }

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

                tblvSalesChallanCmpTRow.getItems().addAll(purchaseInvoiceTable);
                SalesChallanCalculations.rowCalculationForPurcInvoice(index, tblvSalesChallanCmpTRow, callback);

                SalesChallanCalculations.calculateGst(tblvSalesChallanCmpTRow, callback);
                List<LevelAForPurInvoice> levelAForPurInvoiceList = ProductUnitsPacking2.getAllProductUnitsPackingFlavour(purchaseInvoiceTable.getProduct_id());

                ObservableList<LevelAForPurInvoice> observableLevelAList = FXCollections.observableArrayList(levelAForPurInvoiceList);

                if (index >= 0 && index < PurInvoiceCommunicator.levelAForPurInvoiceObservableList.size()) {
                    PurInvoiceCommunicator.levelAForPurInvoiceObservableList.set(index, observableLevelAList);

                    tblvSalesChallanCmpTRow.getItems().get(index).setLevelA(null);
                    tblvSalesChallanCmpTRow.getItems().get(index).setLevelA(levelAForPurInvoiceList.get(0).getLabel());

                    for (LevelAForPurInvoice levelAForPurInvoice : PurInvoiceCommunicator.levelAForPurInvoiceObservableList.get(index)) {
                        if (tblvSalesChallanCmpTRow.getItems().get(index).getLevelA_id().equals(levelAForPurInvoice.getValue())) {
                            tblvSalesChallanCmpTRow.getItems().get(index).setLevelA(null);
                            tblvSalesChallanCmpTRow.getItems().get(index).setLevelA(levelAForPurInvoice.getLabel());
                        }
                    }

                } else {
                    PurInvoiceCommunicator.levelAForPurInvoiceObservableList.add(observableLevelAList);

                    tblvSalesChallanCmpTRow.getItems().get(index).setLevelA(null);
                    tblvSalesChallanCmpTRow.getItems().get(index).setLevelA(levelAForPurInvoiceList.get(0).getLabel());

                    for (LevelAForPurInvoice levelAForPurInvoice : PurInvoiceCommunicator.levelAForPurInvoiceObservableList.get(index)) {
                        if (tblvSalesChallanCmpTRow.getItems().get(index).getLevelA_id().equals(levelAForPurInvoice.getValue())) {
                            tblvSalesChallanCmpTRow.getItems().get(index).setLevelA(null);
                            tblvSalesChallanCmpTRow.getItems().get(index).setLevelA(levelAForPurInvoice.getLabel());
                        }
                    }
                }

                count++;
                index++;
            }
//            setAddChargesInTable();

/*            cmbPaymentMode.setOnKeyPressed(event -> {
                if (actionPerformed == 0 && event.getCode() == KeyCode.ENTER || event.getCode() == KeyCode.TAB && !event.isShiftDown()) {
                    actionPerformed = 1;
                    System.out.println("tblvSalesChallanCmpTRow : " + tblvSalesChallanCmpTRow.getItems().size());
                    tblvSalesChallanCmpTRow.getItems().addAll(new PurchaseInvoiceTable("", "0", "1", "", "", "", "", "", "", "", "", "", "", "", "", "", "", ""));
                    cmbPaymentMode.requestFocus();

                    PauseTransition pause = new PauseTransition(Duration.seconds(0.0001));
                    pause.setOnFinished(eve -> {
                        Platform.runLater(() -> {
                            tblvSalesChallanCmpTRow.edit(tblvSalesChallanCmpTRow.getItems().size() - 1, tblvSalesChallanCmpTRow.getColumns().get(1));
                        });
                    });
                    pause.play();

                    event.consume();

                } else {
                    Platform.runLater(() -> {
                        tblvSalesChallanCmpTRow.edit(tblvSalesChallanCmpTRow.getItems().size() - 1, tblvSalesChallanCmpTRow.getColumns().get(1));
                    });
                }
            });*/


        } else {
            tblvSalesChallanCmpTRow.getItems().addAll(new CmpTRowDTOSoToSc("", "0", "1", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", ""));
//            tblvSalesChallanCmpTRow.getItems().addAll(new CmpTRowDTOSoToSc("", "0", "1", "", "", "", "", "", "", "", "", "", "", "", "", "", "", ""));
        }
//        tblvSalesChallanCmpTRow.getItems().addAll(new CmpTRowDTOSoToSc("", "0", "1", "", "", "", "", "","", "", "", "", "", "", "", "", "", "", ""));

        tblcSalesChallanCmpTRowSr.setCellValueFactory(cellData -> cellData.getValue().sr_noProperty());
        tblcSalesChallanCmpTRowSr.setStyle("-fx-alignment: CENTER;");

        tblcSalesChallanCmpTRowPacking.setCellValueFactory(cellData -> cellData.getValue().packagesProperty());
        tblcSalesChallanCmpTRowPacking.setStyle("-fx-alignment: CENTER;");

//        tblcSalesChallanCmpTRowUnit.setCellValueFactory(cellData -> cellData.getValue().unitProperty());
//        tblcSalesChallanCmpTRowUnit.setCellFactory(column -> new ComboBoxTableCellSalesChallanForUnit("tblcSalesChallanCmpTRowUnit"));


//        tblcSalesChallanCmpTRowAction.setCellFactory(column -> new DeleteBtnForActionColumn("tblcSalesChallanCmpTRowAction"));

        tblcSalesChallanCmpTRowParticulars.setCellValueFactory(cellData -> cellData.getValue().particularsProperty());
        tblcSalesChallanCmpTRowParticulars.setCellFactory(column -> new TextFieldTableCellForSalesChallan("tblcSalesChallanCmpTRowParticulars", callback, productID_callback, tfSalesChallanNarrations, addPrdCalbak));

        tblcSalesChallanCmpTRowBatchNo.setCellValueFactory(cellData -> cellData.getValue().batch_or_serialProperty());
        tblcSalesChallanCmpTRowBatchNo.setCellFactory(column -> new TextFieldTableCellForSalesChallan("tblcSalesChallanCmpTRowBatchNo", callback, productID_callback, addRow, batchID_callback));

        tblcSalesChallanCmpTRowQuantity.setCellValueFactory(cellData -> cellData.getValue().quantityProperty());
        tblcSalesChallanCmpTRowQuantity.setCellFactory(column -> new TextFieldTableCellForSalesChallan("tblcSalesChallanCmpTRowQuantity", callback));


        tblcSalesChallanCmpTRowFreeQty.setCellValueFactory(cellData -> cellData.getValue().freeProperty());//new2.0
        tblcSalesChallanCmpTRowFreeQty.setCellFactory(column -> new TextFieldTableCellForSalesChallan("tblcSalesChallanCmpTRowFreeQty", callback));//new2.0


        tblcSalesChallanCmpTRowRate.setCellValueFactory(cellData -> cellData.getValue().rateProperty());
        tblcSalesChallanCmpTRowRate.setCellFactory(column -> new TextFieldTableCellForSalesChallan("tblcSalesChallanCmpTRowRate", callback));

        tblcSalesChallanCmpTRowGrossAmt.setCellValueFactory(cellData -> cellData.getValue().gross_amountProperty());
        tblcSalesChallanCmpTRowGrossAmt.setCellFactory(column -> new TextFieldTableCellForSalesChallan("tblcSalesChallanCmpTRowGrossAmt", callback));


        tblcSalesChallanCmpTRowB.setCellValueFactory(cellData -> cellData.getValue().levelBProperty());
        tblcSalesChallanCmpTRowB.setCellFactory(column -> new ComboBoxTableCellForSalesChallanLevelB("tblcSalesChallanCmpTRowB"));

        tblcSalesChallanCmpTRowA.setCellValueFactory(cellData -> cellData.getValue().levelAProperty());
        tblcSalesChallanCmpTRowA.setCellFactory(column -> new ComboBoxTableCellForSalesChallanLevelA("tblcSalesChallanCmpTRowA"));

        tblcSalesChallanCmpTRowC.setCellValueFactory(cellData -> cellData.getValue().levelCProperty());
        tblcSalesChallanCmpTRowC.setCellFactory(column -> new ComboBoxTableCellForSalesChallanLevelC("tblcSalesChallanCmpTRowC"));

//        columnVisibility(tblcSalesChallanCmpTRowA, Globals.getUserControlsWithSlug("is_level_a"));
//        columnVisibility(tblcSalesChallanCmpTRowB, Globals.getUserControlsWithSlug("is_level_b"));
//        columnVisibility(tblcSalesChallanCmpTRowC, Globals.getUserControlsWithSlug("is_level_c"));
//        //! Free Qty
//        columnVisibility(tblcSalesChallanCmpTRowFreeQty, Globals.getUserControlsWithSlug("is_free_qty"));


        tblcSalesChallanCmpTRowUnit.setCellValueFactory(cellData -> cellData.getValue().unitProperty());
        tblcSalesChallanCmpTRowUnit.setCellFactory(column -> new ComboBoxTableCellSalesChallanForUnit("tblcSalesChallanCmpTRowUnit", unit_callback));


        tblcSalesChallanCmpTRowDiscPerc.setCellValueFactory(cellData -> cellData.getValue().dis_perProperty());
        tblcSalesChallanCmpTRowDiscPerc.setCellFactory(column -> new TextFieldTableCellForSalesChallan("tblcSalesChallanCmpTRowDiscPerc", callback));

        tblcSalesChallanCmpTRowDisAmt.setCellValueFactory(cellData -> cellData.getValue().dis_amtProperty());
        tblcSalesChallanCmpTRowDisAmt.setCellFactory(column -> new TextFieldTableCellForSalesChallan("tblcSalesChallanCmpTRowDisAmt", callback, productID_callback, addRow));

        tblcSalesChallanCmpTRowTaxPer.setCellValueFactory(cellData -> cellData.getValue().taxProperty());
        tblcSalesChallanCmpTRowTaxPer.setCellFactory(column -> new TextFieldTableCellForSalesChallan("tblcSalesChallanCmpTRowTaxPer", callback));

        tblcSalesChallanCmpTRowNetAmt.setCellValueFactory(cellData -> cellData.getValue().net_amountProperty());
        tblcSalesChallanCmpTRowNetAmt.setCellFactory(column -> new TextFieldTableCellForSalesChallan("tblcSalesChallanCmpTRowNetAmt", callback));

//        tblcSalesChallanCmpTRowAction.setCellValueFactory(cellData -> cellData.getValue().net_amountProperty());
        tblcSalesChallanCmpTRowAction.setCellFactory(column -> new SalesChallanDeleteButtonTableCell(delCallback));


    }

    private void columnVisibility(TableColumn<CmpTRowDTOSoToSc, String> column, boolean visible) {
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
//    public void setCbSupplierGSTN(ActionEvent actionEvent) {
//        Object[] object = new Object[1];
//        object[0] = cmbSalesChallanSupplierGST.getSelectionModel().getSelectedItem();
//        if (object[0] != null) {
//            for (GstDetailsDTO commonDTO : supplierGSTINList) {
//                if (object[0].equals(commonDTO)) {
//                    supplierGSTIN_ID = commonDTO.getId();
//                    gst_no = commonDTO.getText();
//                }
//            }
//        }
//    }


    private void setLedgerGSTDATA(ObservableList<GstDetailsDTO> gstDetails) {
        if (gstDetails != null) {
            cmbSalesChallanSupplierGST.setItems(gstDetails);
            cmbSalesChallanSupplierGST.setConverter(new StringConverter<GstDetailsDTO>() {
                @Override
                public String toString(GstDetailsDTO gstDetailsDTO) {
                    clientGSTIN = gstDetailsDTO.getGstNo();
                    return gstDetailsDTO.getGstNo();
                }

                @Override
                public GstDetailsDTO fromString(String s) {
                    return null;
                }
            });
            GstDetailsDTO selectedGst = null;
            for (Object obj : cmbSalesChallanSupplierGST.getItems()) {
                GstDetailsDTO gstDetailsDTO = (GstDetailsDTO) obj;
                if (gstDetailsDTO.getId().equals(gstDetails.get(0).getId())) {
                    selectedGst = gstDetailsDTO;
                    break;
                }
            }
            if (selectedGst != null) {
                cmbSalesChallanSupplierGST.getSelectionModel().select(selectedGst);
                cmbSalesChallanSupplierGST.requestFocus();
            } else {
                tfSalesChallanNo.requestFocus();
            }
        }
    }

    private void setSalesManDATA(ObservableList<SalesmanMasterDTO> salesManDetails) {
        if (salesManDetails != null) {

            cmbSalesChallanSalesman.setItems(salesManDetails);
            cmbSalesChallanSalesman.setConverter(new StringConverter<SalesmanMasterDTO>() {
                @Override
                public String toString(SalesmanMasterDTO salesmanMasterDTO) {
                    clientGSTIN = salesmanMasterDTO.getFirst_name();
                    return salesmanMasterDTO.getFirst_name();
                }

                @Override
                public SalesmanMasterDTO fromString(String s) {
                    return null;
                }
            });
            SalesmanMasterDTO selectedGst = null;
            for (Object obj : cmbSalesChallanSalesman.getItems()) {
                SalesmanMasterDTO salesmanMasterDTO = (SalesmanMasterDTO) obj;
                System.out.println(salesmanMasterDTO.getId());
                System.out.println(salesManDetails.get(0).getId());

//                if (salesmanMasterDTO.getId()==(salesManDetails.get(0).getId())) {
//                    selectedGst = salesmanMasterDTO;
//                    break;
//                }
            }
            if (selectedGst != null) {
                cmbSalesChallanSalesman.getSelectionModel().select(selectedGst);
                cmbSalesChallanSalesman.requestFocus();
            } else {
                tfSalesChallanNo.requestFocus();
            }
        }
    }


    private void initSalesInvoiceForm() {
        hlSaleInvCreateQuotation.setText(0 + "");
        hlSaleInvCreateOrder.setText(0 + "");
//        hlSaleInvCreateChallan.setText(0 + "");
        referenceSCId = new ArrayList<>();
        referenceSOId = new ArrayList<>();
        referenceSQId = new ArrayList<>();
        ledgerId = "";
        ledgerName = "";
//        tfTranxAdditionalTotal.clear();
        tfSalesChallanLedgerName.clear();
        cmbSalesChallanSupplierGST.setValue(null);
        cmbSalesChallanSalesAc.setValue(null);
        tfSalesChallanSrNo.clear();
        tfSalesChallanNo.clear();
        cmbSalesChallanSalesman.setValue(null);
//        cmbSaleInvCreateDeliveryType.setValue(null);
        tfSalesChallanNarrations.clear();
        tfPurChallAllRowDisAmt.clear();
        tfPurChallAllRowDisPer.clear();
//        lblPurChallFreeQty.setText(0 + "");
//        lblPurChallTotalQty.setText(0 + "");
//        tblcSalesChallanCmpTRowGrossAmt.setText(0.0 + "");
//        tfTranxDiscountTotal.setText("");
//        tfTranxTotal.setText(0 + "");
//        tfTranxTaxTotal.setText("");
//        tfTranxBillAmtTotal.setText("");
//        tfTranxRoundOff.setText("");
//        initSaleLastInvoiceNo();
//        initSalesAcc();
//        initLedgerSelection();
//        initTableRow();
//        initRoundOffCheckbox();
//        initDiscLedger();
        initHL();
//        nodetraversal(dpSalesChallanCreateDate, tfTranxSalesInvoiceCreateLedgerName);
//        nodetraversal(tfTranxSalesInvoiceCreateLedgerName, cmbTranxSalesInvoiceCreateSupplierGST);
//        nodetraversal(cmbTranxSalesInvoiceCreateSupplierGST, cmbTranxSalesInvoiceCreateSalesAcc);
//        nodetraversal(cmbTranxSalesInvoiceCreateSalesAcc, tfTranxSalesInvoiceCreateInvoiceSrNo);
//        nodetraversal(tfTranxSalesInvoiceCreateInvoiceSrNo, tfTranxSalesInvoiceCreateInvoiceNo);
//        nodetraversal(tfTranxSalesInvoiceCreateInvoiceNo, cmbSaleInvCreateSalesMan);
//        nodetraversal(cmbSaleInvCreateSalesMan, cmbSaleInvCreateDeliveryType);
//        nodetraversal(cmbSaleInvCreateDeliveryType, hlSaleInvCreateQuotation);
//        nodetraversal(hlSaleInvCreateQuotation, hlSaleInvCreateOrder);
//        nodetraversal(hlSaleInvCreateOrder, hlSaleInvCreateChallan);
//        nodetraversal(hlSaleInvCreateChallan, tblvSalesChallanCmpTRow);
        tfSalesChallanLedgerName.setOnKeyPressed((e) -> {
            if (e.getCode().isLetterKey()) {
                handleTfLedgerName();
            }
        });


        /*hlSaleInvCreateChallan.setOnKeyPressed((e) -> {
            if (e.getCode() == KeyCode.ENTER) {
                tblvSalesChallanCmpTRow.edit(0, tcTranxParticulars);
            }
            e.consume();
        });
        tfTranxSaleInvCreateNarration.setOnKeyPressed((e) -> {
            if (e.getCode() == KeyCode.ENTER) {
                tfTranxSaleInvCreateDisPer.requestFocus();
            }
            e.consume();
        });
        tfTranxSaleInvCreateDisPer.setOnKeyPressed((e) -> {
            if (e.getCode() == KeyCode.ENTER) {
                tfTranxSaleInvCreateDisAmt.requestFocus();
            }
            e.consume();
        });
        tfTranxSaleInvCreateDisAmt.setOnKeyPressed((e) -> {
            if (e.getCode() == KeyCode.ENTER) {
                btnTranxSubmit.requestFocus();
            }
            e.consume();
        });*/
        GlobalTranx.requestFocusOrDieTrying(tfSalesChallanLedgerName, 3);
//        sceneInitilization();
//        initGstTable();
    }

    private void setRowConvData(List<TranxRowConvRes> row) {
        tblvSalesChallanCmpTRow.getItems().clear();
        for (TranxRowConvRes currRowRes : row) {
            try {
                TranxSelectedProduct selectedProduct = TranxCommonPopUps.getSelectedProductFromProductId(currRowRes.getProductName());
                TranxRow currRow = GlobalTranx.newEmptyTranxRow();
//                currRow.setDetailId(currRowRes.getbDetailsId());
                currRow.setProductId(currRowRes.getProductId());
                currRow.setProductName(currRowRes.getProductName());
                currRow.setSelectedProduct(selectedProduct);
                currRow.setQty(currRowRes.getQty());
                currRow.setRate(currRowRes.getRate());
                currRow.setBatch(currRowRes.getBatch());
                currRow.setDisPer(currRowRes.getDisPer());
                currRow.setDisPer2(Double.valueOf(currRowRes.getDisPer2()));
                currRow.setDisAmt(currRowRes.getDisAmt());
                currRow.setProductPackage(currRowRes.getPackName());
                currRow.setUnitId(currRowRes.getUnitId());
                currRow.setDispUnit(currRowRes.getUnitName());
                currRow.setbNo(currRowRes.getBatchNo());
                TranxSelectedBatch selectedBatch = TranxCommonPopUps.getSelectedBatchFromProductId(currRow, dpSalesChallanCreateDate.getText(), currRowRes.getBatchNo());
                currRow.setSelectedBatch(selectedBatch);
                currRow.setIgst(selectedProduct.getIgst());
                currRow.setCgst(selectedProduct.getCgst());
                currRow.setSgst(selectedProduct.getSgst());
                currRow.setReferenceId(currRowRes.getReferenceId());
                currRow.setReferenceType(currRowRes.getReferenceType());
                currRow.setLevelAId(currRowRes.getLevelAId().equalsIgnoreCase("") ? 0 : Integer.valueOf(currRowRes.getLevelAId()));
                currRow.setLevelBId(currRowRes.getLevelBId().equalsIgnoreCase("") ? 0 : Integer.valueOf(currRowRes.getLevelBId()));
                currRow.setLevelCId(currRowRes.getLevelCId().equalsIgnoreCase("") ? 0 : Integer.valueOf(currRowRes.getLevelCId()));
//                tblvSalesChallanCmpTRow.getItems().add(currRow);
                tblRowData.add(currRow);
            } catch (Exception e) {
//                e.printStackTrace();
                SalesChallanLogger.error("Exception in setRowConvData() :" + Globals.getExceptionString(e));
            }
        }
//        TranxCal();
    }


    private void getPendingSalesQuotationWithids() {
        List<TranxRowConvRes> lstRowRes = TranxCommonPopUps.getPendingSalesQuotationWithIds(referenceSQId);
        setRowConvData(lstRowRes);
    }

    private void getPendingSalesOrderWithids() {
        List<TranxRowConvRes> lstRowRes = TranxCommonPopUps.getPendingSalesOrdersWithIds(referenceSOId);
        setRowConvData(lstRowRes);
    }

    private void initHL() {
        hlSaleInvCreateQuotation.setOnAction((e) -> {
            System.out.println("as-->");
            if (Integer.valueOf(hlSaleInvCreateQuotation.getText()) > 0 && referenceSCId.size() == 0 && referenceSOId.size() == 0 && referenceSQId.size() == 0) {
                stage = (Stage) spSalesChallanRootPane.getScene().getWindow();
                TranxCommonPopUps.openGetPendings(stage, ledgerId, "sales_quotation", "Sales Quotation", input -> {
                    System.out.println("sales quotation input => " + input);
                    referenceSQId = input;
                    if (input.size() > 0) getPendingSalesQuotationWithids();
                });
            }
        });

        hlSaleInvCreateOrder.setOnAction((e) -> {
            if (Integer.valueOf(hlSaleInvCreateOrder.getText()) > 0 && referenceSCId.size() == 0 && referenceSOId.size() == 0 && referenceSQId.size() == 0) {
                stage = (Stage) spSalesChallanRootPane.getScene().getWindow();
                TranxCommonPopUps.openGetPendings(stage, ledgerId, "sales_order", "Sales Order", input -> {
//                    System.out.println("sales order input => " + input);
                    referenceSOId = input;
                    if (input.size() > 0) getPendingSalesOrderWithids();
                });
            }
        });
//        hlSaleInvCreateChallan.setOnAction((e) -> {
//            if (Integer.valueOf(hlSaleInvCreateChallan.getText()) > 0 && referenceSCId.size() == 0 && referenceSOId.size() == 0 && referenceSQId.size() == 0) {
//                stage = (Stage) spSalesChallanRootPane.getScene().getWindow();
//                TranxCommonPopUps.openGetPendings(stage, ledgerId, "sales_challan", "Sales Challan", input -> {
////                    System.out.println("sales challan input => " + input);
//                    referenceSCId = input;
//                    if (input.size() > 0) getPendingSalesChallanWithids();
//                });
//            }
//        });
    }

    //to open ledger
    public void handleTfLedgerName() {
        Stage stage = (Stage) spSalesChallanRootPane.getScene().getWindow();
        SingleInputDialogs.openSalesLedgerWithNamePopUp(stage, "Filter", ledgerName, input -> {

                    supplierGSTINList.clear();
                    ledgerName = (String) input[0];
                    tfSalesChallanLedgerName.setText(input[0].toString());
//            ledgerId = (String) input[1];
                    ledgerId = (String) input[1];
                    ledgerStateCode = (String) input[3];

                    salesman_id = (String) input[7];

                    tblvSalesChallanCmpTRow.getItems().get(0).setLedger_id(ledgerId);
                    tranxLedgerDetailsFun(ledgerId);

                    hlSaleInvCreateQuotation.setText((String) input[3]);
                    hlSaleInvCreateOrder.setText((String) input[4]);
//            hlSaleInvCreateChallan.setText((String) input[5]);

                    ObservableList<GstDetailsDTO> gstDetails = (ObservableList<GstDetailsDTO>) input[2];
                    System.out.println(salesman_id);

                    if (gstDetails != null) {
                        setLedgerGSTDATA(gstDetails);
                    }

                    if (salesman_id != null) {
                        getOutletSalesmanMasterRecord();
                    }

                }, in -> {
                    System.out.println("In >> Called >. " + in);
                    if (in == true) {
                        isLedgerRed = true;
                        setPurChallDataToProduct();
                    }
                }

        );


        cmbSalesChallanSupplierGST.getSelectionModel().selectedItemProperty().addListener((obs, old, nw) -> {
            System.out.println("done");
        });
    }

    public void purc_disc_per(KeyEvent keyEvent) {
        System.out.println("DISC PER");
        String discText = tfPurChallAllRowDisPer.getText();
        if (!TextUtils.isEmpty(discText)) {
            double disc_per = Double.parseDouble(discText);

            Double amount = (total_taxable_amt * disc_per) / 100;
            tfPurChallAllRowDisAmt.setText(String.valueOf(amount));
            SalesChallanCalculations.discountPropotionalCalculation(tfPurChallAllRowDisPer.getText(), tfPurChallAllRowDisAmt.getText(), tfPurchaseChallanTotalAmt.getText(), tblvSalesChallanCmpTRow, callback);

        } else {
            tfPurChallAllRowDisAmt.setText("");
            for (CmpTRowDTOSoToSc CmpTRowDTOSoToSc : tblvSalesChallanCmpTRow.getItems()) {
                String netAmt = CmpTRowDTOSoToSc.getNet_amount();
                if (!CmpTRowDTOSoToSc.getTaxable_amt().isEmpty()) {
                    Double orgTaxableAmt = Double.parseDouble(CmpTRowDTOSoToSc.getTaxable_amt());
                    CmpTRowDTOSoToSc.setNet_amount(netAmt);
                    CmpTRowDTOSoToSc.setTaxable_amt(String.valueOf(orgTaxableAmt));
                    CmpTRowDTOSoToSc.setTotal_taxable_amt(String.valueOf(orgTaxableAmt));
                }
            }

            SalesChallanCalculations.discountPropotionalCalculation(tfPurChallAllRowDisPer.getText(), tfPurChallAllRowDisAmt.getText(), tfPurchaseChallanTotalAmt.getText(), tblvSalesChallanCmpTRow, callback);


        }
        SalesChallanCalculations.calculateGst(tblvSalesChallanCmpTRow, callback);
    }

    public void purc_disc_amt(KeyEvent keyEvent) {
        System.out.println("DISC AMT");
        String discAmtText = tfPurChallAllRowDisAmt.getText();
        if (!TextUtils.isEmpty(discAmtText)) {
            double disc_amt = Double.parseDouble(discAmtText);
            double percentage = (disc_amt / total_taxable_amt) * 100;
            tfPurChallAllRowDisPer.setText(String.format("%.2f", percentage));
            SalesChallanCalculations.discountPropotionalCalculation(tfPurChallAllRowDisPer.getText(), tfPurChallAllRowDisAmt.getText(), tfPurchaseChallanTotalAmt.getText(), tblvSalesChallanCmpTRow, callback);

//            com.opethic.genivis.controller.tranx_purchase.SalesChallanCalculations.discountPropotionalCalculation(tfPurChallAllRowDisPer.getText(), tfPurChallAllRowDisAmt.getText(), tfPurchaseChallanAddChgAmt.getText(), tblvSalesChallanCmpTRow, callback);
        } else {
            tfPurChallAllRowDisPer.setText("");
            for (CmpTRowDTOSoToSc CmpTRowDTOSoToSc : tblvSalesChallanCmpTRow.getItems()) {
                String netAmt = CmpTRowDTOSoToSc.getNet_amount();
                if (!CmpTRowDTOSoToSc.getTaxable_amt().isEmpty()) {
                    Double orgTaxableAmt = Double.parseDouble(CmpTRowDTOSoToSc.getTaxable_amt());
                    CmpTRowDTOSoToSc.setNet_amount(netAmt);
                    CmpTRowDTOSoToSc.setTaxable_amt(String.valueOf(orgTaxableAmt));
                    CmpTRowDTOSoToSc.setTotal_taxable_amt(String.valueOf(orgTaxableAmt));
                }
            }

            SalesChallanCalculations.discountPropotionalCalculation(tfPurChallAllRowDisPer.getText(), tfPurChallAllRowDisAmt.getText(), tfPurchaseChallanTotalAmt.getText(), tblvSalesChallanCmpTRow, callback);

//            com.opethic.genivis.controller.tranx_purchase.SalesChallanCalculations.discountPropotionalCalculation("0", "0", tfPurchaseChallanAddChgAmt.getText(), tblvPurChallCmpTRow, callback);
        }
        SalesChallanCalculations.calculateGst(tblvSalesChallanCmpTRow, callback);
    }


    //Enter Functionality Code
    private void nodetraversal(Node current_node, Node next_node) {
        current_node.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                next_node.requestFocus();
                event.consume();
            }

            if (event.getCode() == KeyCode.SPACE) {
                if (current_node instanceof Button button) {
                    button.fire();
                }
            }
        });
    }


}


class SalesChallanDeleteButtonTableCell extends TableCell<CmpTRowDTOSoToSc, String> {
    private Button delete;
    TableCellCallback<Object[]> callback;

    public SalesChallanDeleteButtonTableCell(TableCellCallback<Object[]> DelCallback) {


        this.delete = createButtonWithImage();

        callback = DelCallback;

//        delete.setOnAction(actionEvent -> {
//            CmpTRowDTOSoToSc table = getTableView().getItems().get(getIndex());
//            getTableView().getItems().remove(table);
//
//            Object object[] = new Object[1];
//            if (callback != null) {
//                object[0] = table.getDetails_id();
//                callback.call(object);
//            }
//            getTableView().refresh();
//
//        });

        delete.setOnAction(actionEvent -> {
            CmpTRowDTOSoToSc table = getTableView().getItems().get(getIndex());
            getTableView().getItems().remove(table);
            System.out.println(table.getDetails_id());
            Object object[] = new Object[1];
            if (callback != null) {
                object[0] = table.getDetails_id() == null ? 0 : new Object[1];
                ;
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


class TextFieldTableCellForSalesChallan extends TableCell<CmpTRowDTOSoToSc, String> {
    private TextField textField;
    private String columnName;

    TableCellCallback<Object[]> callback;
    TableCellCallback<String> productID_callback;
    TableCellCallback<Object[]> batchID_callback;
    private TextField button;//new2.0

    TableCellCallback<Object[]> addPrdCalbak;
    TableCellCallback<String> addRow;

    public TextFieldTableCellForSalesChallan(String columnName, TableCellCallback<Object[]> callback, TableCellCallback<String> productID_callback, TableCellCallback<String> addRow) {
        this.columnName = columnName;
        this.textField = new TextField();

        this.callback = callback;
        this.productID_callback = productID_callback;
        this.addRow = addRow;
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

        quantityColumn();
        freeQtyColumn();//new2.0
        rateColumn();
        grossAmtColumn();
        DisPerColumn();
        DisPerColumn2();
        DisAmtColumn();
        TaxColumn();
    }

    public TextFieldTableCellForSalesChallan(String columnName, TableCellCallback<Object[]> callback, TableCellCallback<String> productID_callback, TableCellCallback<String> addRow, TableCellCallback<Object[]> batchID_callback) {
        this.columnName = columnName;
        this.textField = new TextField();

        this.callback = callback;
        this.productID_callback = productID_callback;
        this.addRow = addRow;
        this.batchID_callback = batchID_callback;
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

        quantityColumn();
        freeQtyColumn();//new2.0
        rateColumn();
        grossAmtColumn();
        DisPerColumn();
        DisPerColumn2();
        DisAmtColumn();
        TaxColumn();
    }

    public TextFieldTableCellForSalesChallan(String columnName, TableCellCallback<Object[]> callback) {
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

        quantityColumn();
        freeQtyColumn();//new2.0
        rateColumn();
        grossAmtColumn();

        DisPerColumn();
        DisPerColumn2();
        DisAmtColumn();
        TaxColumn();
    }

    public TextFieldTableCellForSalesChallan(String columnName, TableCellCallback<Object[]> callback, TableCellCallback<String> productID_callback, TextField button, TableCellCallback<Object[]> addPrdCalbak) {
        this.columnName = columnName;
        this.textField = new TextField();

        this.callback = callback;
        this.button = button;

        this.productID_callback = productID_callback;
        this.addPrdCalbak = addPrdCalbak;

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

        quantityColumn();
        freeQtyColumn();
        rateColumn();
        grossAmtColumn();

        DisPerColumn();
        DisPerColumn2();
        DisAmtColumn();
        TaxColumn();
        netAmountColumn();

    }
    //new2.0 End


    private void batchColumn() {

        if ("tblcSalesChallanCmpTRowBatchNo".equals(columnName)) {
            textField.setEditable(false);
            CommonFunctionalUtils.cursorMomentForTextField(textField);
//            textField.setOnKeyPressed(event -> {
//                if (event.getCode() == KeyCode.SPACE) {
//                    System.out.println("SPACE------------------------------");
//                    openBatchWindow();
//                }
//                if (event.getCode() == KeyCode.ENTER) {
//                    System.out.println("Enter------------------------------");
//                    openBatchWindow();
//                }


//            });

            textField.setOnKeyPressed(event -> {
                if (event.getCode() == KeyCode.SPACE) {
                    openBatchWindow();
                }
                if (event.isShiftDown() && event.getCode() == KeyCode.TAB) {
                    if (getIndex() == 1) {
                        TableColumn<CmpTRowDTOSoToSc, ?> colName = getTableView().getColumns().get(6);
                        getTableView().edit(getIndex() - 1, colName);
                    } else {
                        TableColumn<CmpTRowDTOSoToSc, ?> colName = getTableView().getColumns().get(15);
                        getTableView().edit(getIndex() - 1, colName);
                    }
                } else if (event.getCode() == KeyCode.ENTER || !event.isShiftDown() && event.getCode() == KeyCode.TAB) {
                    if (textField.getText().isEmpty() && getIndex() == getTableView().getItems().size() - 1 && getIndex() != 0) {
                        Platform.runLater(() -> {
                            button.requestFocus();
                        });
                    } else if (!textField.getText().isEmpty()) {
                        TableColumn<CmpTRowDTOSoToSc, ?> colName = getTableView().getColumns().get(8);
                        getTableView().edit(getIndex(), colName);
                    } else {
                        textField.requestFocus();
                    }
                    event.consume();
                }
                //new2.0 end
            });


            textField.setOnMouseClicked(event -> {
                openBatchWindow();
            });
        }

    }

    private void quantityColumn() {                //function for calculation when we enter the qty in cmpt row
//        if ("tblcSalesChallanCmpTRowQuantity".equals(columnName)) {
//            textField.setEditable(true);
//            CommonFunctionalUtils.cursorMomentForRowTextField(textField);
//
//
//            textField.setOnKeyPressed(event -> {
//                if (event.getCode() == KeyCode.ENTER || event.getCode() == KeyCode.TAB) {
//                    Integer index = getIndex();
//                    System.out.println("Rada INDEX" + index);
//                    SalesChallanCalculations.rowCalculationForPurcInvoice(index, getTableView(), callback);
//                }
//            });
//        }


        if ("tblcSalesChallanCmpTRowQuantity".equals(columnName)) {
            textField.setEditable(true);
            textField.setOnKeyPressed(event -> {
                if (event.isShiftDown() && event.getCode() == KeyCode.TAB) {
                    TableColumn<CmpTRowDTOSoToSc, ?> colName = getTableView().getColumns().get(7);
                    getTableView().edit(getIndex(), colName);
                    event.consume();

                } else if (event.getCode() == KeyCode.ENTER || event.getCode() == KeyCode.TAB) {
                    Integer index = getIndex();
                    if (textField.getText().isEmpty() || textField.getText().equals(0) || textField.getText().equals("0")) {
                        textField.requestFocus();
                    } else {
                        int cnt = Globals.getUserControlsWithSlug("is_free_qty") == true ? 9 : 10;
                        boolean isNegetiveAllowed = getTableView().getItems().get(getIndex()).getSelectedProduct().getNegetive();
                        double actStk = getTableView().getItems().get(getIndex()).getUnitWiseactStock();
                        double actqty = Double.valueOf(textField.getText());
                        if (actqty > actStk && isNegetiveAllowed == false) {
                            AlertUtility.AlertError(AlertUtility.alertTypeError, "Out of Stock! Available Stock : " + actStk, input -> {
                            });
                            getTableRow().getItem().setQuantity("");
                            TableColumn<CmpTRowDTOSoToSc, ?> colName = getTableView().getColumns().get(8);
                            getTableView().edit(index, colName);
                        } else {
                            TableColumn<CmpTRowDTOSoToSc, ?> colName = getTableView().getColumns().get(cnt);
                            getTableView().edit(index, colName);
                        }
                    }

                    SalesChallanCalculations.rowCalculationForPurcInvoice(getIndex(), getTableView(), callback);
                    SalesChallanCalculations.calculateGst(getTableView(), callback);
                } else if (event.getCode() == KeyCode.DOWN) {
                    getTableView().getItems().addAll(new CmpTRowDTOSoToSc("", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", ""));
                    TableColumn<CmpTRowDTOSoToSc, ?> colName = getTableView().getColumns().get(1);
                    getTableView().edit(getIndex() + 1, colName);
                }
            });

        }


    }

    //new2.0 start
    private void freeQtyColumn() {
        if ("tblcSalesChallanCmpTRowFreeQty".equals(columnName)) {
            textField.setEditable(true);
            textField.setOnKeyPressed(event -> {
                if (event.isShiftDown() && event.getCode() == KeyCode.TAB) {
                    TableColumn<CmpTRowDTOSoToSc, ?> colName = getTableView().getColumns().get(8);
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
                        TableColumn<CmpTRowDTOSoToSc, ?> colName = getTableView().getColumns().get(9);
                        getTableView().edit(index, colName);
                    } else {
                        TableColumn<CmpTRowDTOSoToSc, ?> colName = getTableView().getColumns().get(10);
                        getTableView().edit(getIndex(), colName);
                    }
                    SalesChallanCalculations.rowCalculationForPurcInvoice(getIndex(), getTableView(), callback);
                    SalesChallanCalculations.calculateGst(getTableView(), callback);
                }
            });
        }
    }

    //new2.0 end

    private void rateColumn() {
        if ("tblcSalesChallanCmpTRowRate".equals(columnName)) {
            textField.setEditable(true);
            textField.setOnKeyPressed(event -> {
                if (event.isShiftDown() && event.getCode() == KeyCode.TAB) {
                    TableColumn<CmpTRowDTOSoToSc, ?> colName = getTableView().getColumns().get(9);
                    getTableView().edit(getIndex() - 1, colName);
                } else if (event.getCode() == KeyCode.ENTER || event.getCode() == KeyCode.TAB) {
                    int index = getIndex();
                    if (!textField.getText().isEmpty()) {
                        TableColumn<CmpTRowDTOSoToSc, ?> colName = getTableView().getColumns().get(12);
                        getTableView().edit(index, colName);
                    } else {
                        textField.requestFocus();
                    }
                    SalesChallanCalculations.rowCalculationForPurcInvoice(index, getTableView(), callback);
                } else if (event.getCode() == KeyCode.DOWN) {
                    getTableView().getItems().addAll(new CmpTRowDTOSoToSc("", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", ""));
                    TableColumn<CmpTRowDTOSoToSc, ?> colName = getTableView().getColumns().get(1);
                    getTableView().edit(getIndex() + 1, colName);
                }
            });

        }
    }

    private void grossAmtColumn() {
        if ("tblcSalesChallanCmpTRowGrossAmt".equals(columnName)) {
            textField.setEditable(false);
            textField.setFocusTraversable(false);
//            textField.setOnKeyPressed(event -> {
//                if (event.isShiftDown() && event.getCode() == KeyCode.TAB) {
//                    TableColumn<CmpTRowDTOSoToSc, ?> colName = getTableView().getColumns().get(7);
//                    getTableView().edit(getIndex() - 1, colName);
//                } else if (event.getCode() == KeyCode.ENTER || event.getCode() == KeyCode.TAB) {
//                    int index = getIndex();
//                    if (!textField.getText().isEmpty()) {
//                        TableColumn<CmpTRowDTOSoToSc, ?> colName = getTableView().getColumns().get(11);
//                        getTableView().edit(index, colName);
//                    } else {
//                        textField.requestFocus();
//                    }
//                    SalesOrderCalculation.rowCalculationForPurcInvoice(index, getTableView(), callback);
//                } else if (event.getCode() == KeyCode.DOWN) {
//                    getTableView().getItems().addAll(new CmpTRowDTOSoToSc("", "", "", "", "", "", "", "","", "", "", "", "", "", "", "", "", "", ""));
//                    TableColumn<CmpTRowDTOSoToSc, ?> colName = getTableView().getColumns().get(1);
//                    getTableView().edit(getIndex() + 1, colName);
//                }
//            });

        }
    }

    private void DisPerColumn() {
        if ("tblcSalesChallanCmpTRowDiscPerc".equals(columnName)) {
            textField.setEditable(true);
//new2.0 start
            textField.setOnKeyPressed(event -> {
                if (event.isShiftDown() && event.getCode() == KeyCode.TAB) {
                    TableColumn<CmpTRowDTOSoToSc, ?> colName = getTableView().getColumns().get(10);
                    getTableView().edit(getIndex(), colName);
                } else if (event.getCode() == KeyCode.ENTER || event.getCode() == KeyCode.TAB) {
                    int index = getIndex();
                    TableColumn<CmpTRowDTOSoToSc, ?> colName = getTableView().getColumns().get(14);
                    getTableView().edit(index, colName);
                    SalesChallanCalculations.rowCalculationForPurcInvoice(index, getTableView(), callback);
//new2.0 end
                }
            });
        }
    }

    private void DisPerColumn2() {
        if ("tblcSalesChallanCmpTRowDisPer".equals(columnName)) {
            textField.setEditable(true);
            textField.setFocusTraversable(false);
//new2.0 start


//               textField.setOnMouseClicked(event -> {
//                Integer index = getIndex();
//                System.out.println("Rada INDEX"+index);
//                SalesChallanCalculations.rowCalculationForPurcInvoice(index, getTableView(), callback);
//            });
        }
    }


    private void DisAmtColumn() {
        if ("tblcSalesChallanCmpTRowDisAmt".equals(columnName)) {
            textField.setEditable(true);
//new2.0 start
            textField.setOnKeyPressed(event -> {
                if (event.isShiftDown() && event.getCode() == KeyCode.TAB) {
                    TableColumn<CmpTRowDTOSoToSc, ?> colName = getTableView().getColumns().get(12);
                    getTableView().edit(getIndex(), colName);
                    event.consume();
                } else if (event.getCode() == KeyCode.ENTER || event.getCode() == KeyCode.TAB) {
                    int index = getIndex();
                    if ((getTableView().getItems().size() - 1) == index) {
//                        getTableView().getItems().addAll(new CmpTRowDTOSoToSc("", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", ""));
                        getTableView().getItems().addAll(new CmpTRowDTOSoToSc("", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", ""));

                    }
                    TableColumn<CmpTRowDTOSoToSc, ?> colName = getTableView().getColumns().get(1);
                    getTableView().edit(getIndex() + 1, colName);
                    SalesChallanCalculations.rowCalculationForPurcInvoice(getIndex(), getTableView(), callback);
                    SalesChallanCalculations.calculateGst(getTableView(), callback);

                }
            });
        }

    }


    private void particularsColumn() {


        if ("tblcSalesChallanCmpTRowParticulars".equals(columnName)) {
            textField.setEditable(false);
            textField.setOnKeyPressed(event -> {
                if (event.getCode() == KeyCode.SPACE) {
                    openProduct();
                }
                if (event.isShiftDown() && event.getCode() == KeyCode.TAB) {
                    if (getIndex() == 1) {
                        TableColumn<CmpTRowDTOSoToSc, ?> colName = getTableView().getColumns().get(7);
                        getTableView().edit(getIndex() - 1, colName);
                    } else {
                        TableColumn<CmpTRowDTOSoToSc, ?> colName = getTableView().getColumns().get(15);
                        getTableView().edit(getIndex() - 1, colName);
                    }
                }

                if (event.getCode() == KeyCode.ENTER || !event.isShiftDown() && event.getCode() == KeyCode.TAB) {
                    if (textField.getText().isEmpty() && getIndex() == getTableView().getItems().size() - 1 && getIndex() != 0) {
                        Platform.runLater(() -> {
                            button.requestFocus();
                        });
                    } else if (!textField.getText().isEmpty()) {
                        TableColumn<CmpTRowDTOSoToSc, ?> colName = getTableView().getColumns().get(6);
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

    private void TaxColumn() {
        if ("tblcSalesChallanCmpTRowTaxPer".equals(columnName)) {
            textField.setEditable(true);
//new2.0 start
            textField.setOnKeyPressed(event -> {
                if (event.isShiftDown() && event.getCode() == KeyCode.TAB) {
                    TableColumn<CmpTRowDTOSoToSc, ?> colName = getTableView().getColumns().get(9);
                    getTableView().edit(getIndex(), colName);
                } else if (event.getCode() == KeyCode.ENTER || event.getCode() == KeyCode.TAB) {
                    int index = getIndex();
                    TableColumn<CmpTRowDTOSoToSc, ?> colName = getTableView().getColumns().get(12);
                    getTableView().edit(index, colName);
                    SalesChallanCalculations.rowCalculationForPurcInvoice(index, getTableView(), callback);
//new2.0 end
                }
            });
        }
    }

    private void netAmountColumn() {

        if ("tblcSalesChallanCmpTRowNetAmt".equals(columnName)) {
            textField.setEditable(false);
            textField.setOnKeyPressed(event -> {
                if (event.getCode() == KeyCode.ENTER) {

                    int current_index = getTableRow().getIndex();

                    getTableView().getItems().addAll(new CmpTRowDTOSoToSc("", String.valueOf(current_index + 1), "1", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", ""));
                }
            });
        }

    }

    //new2.0 start
    private void openProduct() {
        CmpTRowDTOSoToSc selectedRow = getTableView().getItems().get(getIndex());

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
                getTableRow().getItem().setSelectedProduct(input);
                getTableRow().getItem().setRate(input.getSalesRate().toString());
                //SI testing issue start
                String productId = input.getProductId().toString();
                System.out.println("productId of particular " + productId);
                if (productID_callback != null) {
                    Object[] object = new Object[1];
                    object[0] = productId;
//                    productID_callback.call(object);
                }
                //SI testing issue end
//                getTableRow().getItem().
                List<LevelAForPurInvoice> levelAForPurInvoiceList = ProductUnitsPackingForSalesChallan.getAllProductUnitsPackingFlavour(input.getProductId().toString());
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
                TableColumn<CmpTRowDTOSoToSc, ?> colName = getTableView().getColumns().get(1);
                getTableView().edit(getIndex(), colName);
            }
        }, item -> {
            if (item) {
                if (addPrdCalbak != null) {
                    System.out.println("hello Add product Called");
                    int cur_index = getTableRow().getIndex();
                    Object[] object = new Object[3];
                    object[0] = true;
                    object[1] = cur_index;
                    object[2] = "isProduct";
                    addPrdCalbak.call(object);
                }
            }
        });
    }

    private void onDiscount(int current_index) {
        if (addRow != null) {
            addRow.call(String.valueOf(current_index));
        }

    }

    public void openBatchWindow() {
//        CmpTRowDTOSoToSc selectedRow = getTableView().getItems().get(getIndex());
        CmpTRowDTOSoToSc selectedRow = getTableView().getItems().get(getIndex());

        if (Boolean.valueOf(selectedRow.getIs_batch()) == true) {
            TranxCommonPopUps.openBatchPopUpSalesChallan(Communicator.stage, selectedRow, Communicator.tranxDate, "Batch", input -> {
                if (input != null) {
                    selectedRow.setSelectedBatch(input);
                    selectedRow.setB_details_id("" + input.getId());
                    selectedRow.setB_no(input.getBatchNo());
                    selectedRow.setId(String.valueOf(input.getId()));
//                    selectedRow.setBatch_or_serial(input.getUnit1ClsStock());
//                    selectedRow.setB_no(input.getBatchNo());
                    selectedRow.setBatch_or_serial(input.getBatchNo());
                    // selectedRow.setRate(input.minRateA);


                    Object[] object = new Object[2];
                    object[0] = selectedRow.getB_no();  // casting to String is unnecessary here as input is already an array of Objects
                    object[1] = selectedRow.getId();  // same as above

                    if (batchID_callback != null) {
                        batchID_callback.call(object);  // call with the object array
                    }


                }
//                getTableView().getItems().set(getIndex(), selectedRow);
            });
        } else {
            selectedRow.setBatch_or_serial("#");
            selectedRow.setB_no("#");
//            getTableView().getItems().set(getIndex(), selectedRow);
        }

        SalesChallanCalculations.rowCalculationForPurcInvoice(getIndex(), getTableView(), callback);
        SalesChallanCalculations.calculateGst(getTableView(), callback);
        TableColumn<CmpTRowDTOSoToSc, ?> colName = getTableView().getColumns().get(7);

        getTableView().edit(getIndex(), colName);
    }


    public void textfieldStyle() {
        if (columnName.equals("tblcSalesChallanCmpTRowParticulars")) {
            textField.setPromptText("Particulars");
            textField.setEditable(false);
        } else {
            textField.setPromptText("0");
        }

        /*textField.setPrefHeight(38);
        textField.setMaxHeight(38);
        textField.setMinHeight(38);*/

        if (columnName.equals("tblcSalesChallanCmpTRowParticulars")) {
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


        if (columnName.equals("tblcSalesChallanCmpTRowNetAmt")) {
            this.textField.setOnKeyPressed(event -> {
                if (event.getCode() == KeyCode.ENTER) {
                    commitEdit(textField.getText());
                    getTableView().getItems().addAll(new CmpTRowDTOSoToSc("", "", "1", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", ""));
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
        if (item != null && columnName.equals("tblcSalesChallanCmpTRowParticulars")) {
            ((CmpTRowDTOSoToSc) item).setParticulars(newValue.isEmpty() ? "" : newValue);
        } else if (columnName.equals("tcSCConvEditSupplierBatch")) {
            (getTableRow().getItem()).setBatch_or_serial(newValue.isEmpty() ? "" : newValue);
        } else if (columnName.equals("tblcSalesChallanCmpTRowQuantity")) {
            (getTableRow().getItem()).setQuantity(newValue.isEmpty() ? "0" : newValue);
        } else if (columnName.equals("tblcSalesChallanCmpTRowFreeQty")) {
            (getTableRow().getItem()).setFree(newValue.isEmpty() ? "0" : newValue);
        } else if (columnName.equals("tblcSalesChallanCmpTRowRate")) {
            (getTableRow().getItem()).setRate(newValue.isEmpty() ? "0.0" : newValue);
        } else if (columnName.equals("tblcSalesChallanCmpTRowGrossAmt")) {
            (getTableRow().getItem()).setGross_amount(newValue.isEmpty() ? "0.0" : newValue);
        } else if (columnName.equals("tblcSalesChallanCmpTRowDiscPerc")) {
            (getTableRow().getItem()).setDis_per(newValue.isEmpty() ? "0.0" : newValue);
        } else if (columnName.equals("tblcSalesChallanCmpTRowDisAmt")) {
            (getTableRow().getItem()).setDis_amt(newValue.isEmpty() ? "0.0" : newValue);
        } else if (columnName.equals("tblcSalesChallanCmpTRowTaxPer")) {
            (getTableRow().getItem()).setTax(newValue.isEmpty() ? "0.0" : newValue);
        } else if (columnName.equals("tblcSalesChallanCmpTRowNetAmt")) {
            (getTableRow().getItem()).setNet_amount(newValue.isEmpty() ? "0.0" : newValue);
        }

    }
}

class ComboBoxTableCellSalesChallanForUnit extends TableCell<CmpTRowDTOSoToSc, String> {

    String columnName;

    TableCellCallback<Integer> unit_callback;//new

    private final ComboBox<String> comboBox;

    public ComboBoxTableCellSalesChallanForUnit(String columnName, TableCellCallback<Integer> unit_callback) {


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
                comboBox.setPrefWidth(getTableView().getColumns().get(6).getWidth() - 1);
                comboBox.setMaxWidth(getTableView().getColumns().get(6).getWidth() - 1);
                comboBox.setMinWidth(getTableView().getColumns().get(6).getWidth() - 1);
            });
        } else if (height == 1050) {
            comboBox.setPrefHeight(38);
            comboBox.setMaxHeight(38);
            comboBox.setMinHeight(38);
            Platform.runLater(() -> {
                comboBox.setPrefWidth(getTableView().getColumns().get(6).getWidth() - 1);
                comboBox.setMaxWidth(getTableView().getColumns().get(6).getWidth() - 1);
                comboBox.setMinWidth(getTableView().getColumns().get(6).getWidth() - 1);
            });
        } else if (height == 1024) {
            comboBox.setPrefHeight(32);
            comboBox.setMaxHeight(32);
            comboBox.setMinHeight(32);
            Platform.runLater(() -> {
                comboBox.setPrefWidth(getTableView().getColumns().get(6).getWidth() - 1);
                comboBox.setMaxWidth(getTableView().getColumns().get(6).getWidth() - 1);
                comboBox.setMinWidth(getTableView().getColumns().get(6).getWidth() - 1);
            });

        } else if (height == 960) {
            comboBox.setPrefHeight(32);
            comboBox.setMaxHeight(32);
            comboBox.setMinHeight(32);
            Platform.runLater(() -> {
                comboBox.setPrefWidth(getTableView().getColumns().get(6).getWidth() - 1);
                comboBox.setMaxWidth(getTableView().getColumns().get(6).getWidth() - 1);
                comboBox.setMinWidth(getTableView().getColumns().get(6).getWidth() - 1);
            });
        } else if (height == 900) {
            comboBox.setPrefHeight(32);
            comboBox.setMaxHeight(32);
            comboBox.setMinHeight(32);
            Platform.runLater(() -> {
                comboBox.setPrefWidth(getTableView().getColumns().get(6).getWidth() - 1);
                comboBox.setMaxWidth(getTableView().getColumns().get(6).getWidth() - 1);
                comboBox.setMinWidth(getTableView().getColumns().get(6).getWidth() - 1);
            });
        } else if (height == 800) {
            comboBox.setPrefHeight(27.5);
            comboBox.setMaxHeight(27.5);
            comboBox.setMinHeight(27.5);
            Platform.runLater(() -> {
                comboBox.setPrefWidth(getTableView().getColumns().get(6).getWidth() - 1);
                comboBox.setMaxWidth(getTableView().getColumns().get(6).getWidth() - 1);
                comboBox.setMinWidth(getTableView().getColumns().get(6).getWidth() - 1);
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
                TableColumn<CmpTRowDTOSoToSc, ?> colName = getTableView().getColumns().get(1);
                getTableView().edit(getIndex(), colName);
                event.consume();
            } else if (event.getCode() == KeyCode.ENTER || !event.isShiftDown() && event.getCode() == KeyCode.TAB) {
                TableColumn<CmpTRowDTOSoToSc, ?> colName = getTableView().getColumns().get(8);
                System.out.println("Col name:" + colName.getText());
                getTableView().edit(getIndex(), colName);
            } else if (event.getCode() == KeyCode.SPACE) {
                comboBox.show();
                event.consume();
            }
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
                    if (i >= 0 && i < PurInvoiceCommunicator.unitForPurInvoiceList.size()) {
                        for (UnitForPurInvoice unitForPurInvoice : PurInvoiceCommunicator.unitForPurInvoiceList.get(i)) {
                            if (!PurInvoiceCommunicator.emptyUnitStockId.equals(unitForPurInvoice.getUnitId().toString())) {
                                comboBox.getItems().add(unitForPurInvoice.getLabel());
                            }
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


class ComboBoxTableCellForSalesChallanLevelA extends TableCell<CmpTRowDTOSoToSc, String> {

    String columnName;
    private final ComboBox<String> comboBox;
    ObservableList<LevelAForPurInvoice> comboList = null;

    public ComboBoxTableCellForSalesChallanLevelA(String columnName) {

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
        TableRow<CmpTRowDTOSoToSc> row = getTableRow();
        if (row != null) {
            CmpTRowDTOSoToSc item = row.getItem();
            if (item != null) {
                item.setLevelA(newValue);
            }
        }
    }


}

class ComboBoxTableCellForSalesChallanLevelB extends TableCell<CmpTRowDTOSoToSc, String> {

    String columnName;
    private final ComboBox<String> comboBox;
    ObservableList<LevelBForPurInvoice> comboList = null;

    public ComboBoxTableCellForSalesChallanLevelB(String columnName) {
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

class ComboBoxTableCellForSalesChallanLevelC extends TableCell<CmpTRowDTOSoToSc, String> {

    String columnName;
    private final ComboBox<String> comboBox;
    ObservableList<LevelAForPurInvoice> comboList = null;

    public ComboBoxTableCellForSalesChallanLevelC(String columnName) {


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

class ProductUnitsPackingForSalesChallan {

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


class SalesChallanCalculations {

    public static void rowCalculationForPurcInvoice(int rowIndex, TableView<CmpTRowDTOSoToSc> tableView, TableCellCallback<Object[]> callback) {
        CmpTRowDTOSoToSc purchaseInvoiceTable = tableView.getItems().get(rowIndex);
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
            CmpTRowDTOSoToSc selectedItem = tableView.getItems().get(rowIndex);
            selectedItem.setGross_amount(String.valueOf(base_amt));
            selectedItem.setNet_amount(String.valueOf(net_amt));
            selectedItem.setOrg_net_amt(String.valueOf(net_amt));
//        selectedItem.setOrg_taxable_amt(taxable_amt);
            selectedItem.setTaxable_amt(String.valueOf(taxable_amt));
            selectedItem.setTotal_taxable_amt(String.valueOf(total_taxable_amt));
            selectedItem.setIgst(String.valueOf(r_tax_per));
            selectedItem.setSgst(String.valueOf(r_tax_per / 2));
            selectedItem.setCgst(String.valueOf(r_tax_per / 2));
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

    public static void calculateGst(TableView<CmpTRowDTOSoToSc> tableView, TableCellCallback<Object[]> callback) {


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
        for (CmpTRowDTOSoToSc purchaseInvoiceTable : tableView.getItems()) {
            if (!purchaseInvoiceTable.getQuantity().isEmpty() && !purchaseInvoiceTable.getRate().isEmpty()) {
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
                Double igst = Double.parseDouble(purchaseInvoiceTable.getTotal_igst());
                Double cgst = Double.parseDouble(purchaseInvoiceTable.getTotal_cgst());
                Double sgst = Double.parseDouble(purchaseInvoiceTable.getTotal_sgst());
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

                observableList.add(new GstDTO(String.format("%.2f", taxPer), String.format("%.2f", totalCGST), String.format("%.2f", totalSGST), String.valueOf(totalIGST)));

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


    public static void discountPropotionalCalculation(String disproportionalPer, String disproportionalAmt, String additionalCharges, TableView<CmpTRowDTOSoToSc> tableView, TableCellCallback<Object[]> callback) {

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
            System.out.println("DISC Row =>" + tableView.getItems().get(i));
            System.out.println("Gross amt in Prop--->" + tableView.getItems().get(i).getGross_amount());
            rowCalculationForPurcInvoice(i, tableView, callback);//call row calculation function
            CmpTRowDTOSoToSc purchaseInvoiceTable = tableView.getItems().get(i);
            totalTaxableAmt = totalTaxableAmt + Double.parseDouble(purchaseInvoiceTable.getTaxable_amt());
        }
        System.out.println("totalTaxableAmt discountPropotionalCalculation :: " + totalTaxableAmt);

        Double rowDisc = 0.0;
        //get data of netamount, tax per,and row taxable amount from cmptrow
        for (CmpTRowDTOSoToSc purchaseInvoiceTable : tableView.getItems()) {
            System.out.println("getFinal_dis_amt--->" + purchaseInvoiceTable.getFinal_dis_amt());
            rowDisc = purchaseInvoiceTable.getFinal_dis_amt().isEmpty() ? 0.0 : Double.parseDouble(purchaseInvoiceTable.getFinal_dis_amt());
            System.out.println("netAmt--->" + purchaseInvoiceTable.getNet_amount());
            netAmt = purchaseInvoiceTable.getNet_amount().isEmpty() ? 0.0 : Double.parseDouble(purchaseInvoiceTable.getNet_amount());
            System.out.println("netAmt--->" + purchaseInvoiceTable.getTax());
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
            if (r_tax_per >= 0) {
                total_tax_amt = (r_tax_per / 100) * (rowTaxableAmt);
                System.out.println("total_tax_amt" + total_tax_amt);
                netAmt = rowTaxableAmt + total_tax_amt;
                System.out.println("total netAmt  :" + netAmt + rowTaxableAmt);
            }
//


            //Set data to cmpTRow
            purchaseInvoiceTable.setNet_amount(String.format("%.2f", netAmt));
            purchaseInvoiceTable.setTotal_taxable_amt(String.valueOf(totalTaxableAmtAdditional));
            System.out.println("DIS PRO Total Tax Amt--->" + total_tax_amt);
            purchaseInvoiceTable.setTotal_igst(String.valueOf(total_tax_amt));
            purchaseInvoiceTable.setTotal_cgst(String.valueOf(total_tax_amt / 2));
            purchaseInvoiceTable.setTotal_sgst(String.valueOf(total_tax_amt / 2));
            purchaseInvoiceTable.setFinal_tax_amt(String.valueOf(total_tax_amt));
            purchaseInvoiceTable.setFinal_dis_amt(String.valueOf(rowDisc));
            purchaseInvoiceTable.setInvoice_dis_amt(String.valueOf(rowDisPropAmt));

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


    public static void additionalChargesCalculation(String
                                                            additionalCharges, TableView<CmpTRowDTOSoToSc> tableView, TableCellCallback<Object[]> callback) {


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
                CmpTRowDTOSoToSc purchaseInvoiceTable = tableView.getItems().get(i);
                totalTaxableAmt = totalTaxableAmt + Double.parseDouble(purchaseInvoiceTable.getTotal_taxable_amt());

            }
            for (CmpTRowDTOSoToSc purchaseInvoiceTable : tableView.getItems()) {

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

                purchaseInvoiceTable.setNet_amount(String.format("%.3f", netAmt));
                purchaseInvoiceTable.setTotal_cgst(String.valueOf(total_tax_amt / 2));
                purchaseInvoiceTable.setTotal_sgst(String.valueOf(total_tax_amt / 2));
                purchaseInvoiceTable.setTotal_igst(String.valueOf(total_tax_amt));
                purchaseInvoiceTable.setFinal_tax_amt(String.valueOf(total_tax_amt));


            }

        }
        //call calculate gst function
        calculateGst(tableView, callback);

    }


}
