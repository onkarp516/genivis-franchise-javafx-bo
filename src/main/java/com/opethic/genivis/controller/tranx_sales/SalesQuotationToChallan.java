package com.opethic.genivis.controller.tranx_sales;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.opethic.genivis.GenivisApplication;
import com.opethic.genivis.controller.dialogs.AdditionalCharges;
import com.opethic.genivis.controller.dialogs.BatchWindow;
import com.opethic.genivis.controller.dialogs.SingleInputDialogs;
import com.opethic.genivis.controller.tranx_sales.common.TranxCommonPopUps;
import com.opethic.genivis.dto.*;
import com.opethic.genivis.dto.add_charges.CustomAddChargesDTO;
import com.opethic.genivis.dto.cmp_t_row.BatchWindowTableDTO;
import com.opethic.genivis.dto.pur_invoice.*;
import com.opethic.genivis.dto.pur_invoice.reqres.PurchaseAcList;
import com.opethic.genivis.dto.pur_invoice.reqres.PurchaseAcRes;
import com.opethic.genivis.dto.pur_invoice.reqres.PurchaseSerialRes;
import com.opethic.genivis.dto.reqres.product.Communicator;
import com.opethic.genivis.dto.reqres.product.TableCellCallback;
import com.opethic.genivis.dto.reqres.pur_tranx.TranxPurRowDTO;
import com.opethic.genivis.dto.reqres.sales_tranx.*;
import com.opethic.genivis.dto.tranx_sales.CmpTRowDTOSoToSc;
import com.opethic.genivis.models.tranx.sales.TranxSelectedProduct;
import com.opethic.genivis.network.APIClient;
import com.opethic.genivis.network.EndPoints;
import com.opethic.genivis.network.RequestType;
import com.opethic.genivis.utils.*;
import javafx.animation.PauseTransition;
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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.Duration;
import javafx.util.StringConverter;
import org.apache.http.util.TextUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;
import java.net.http.HttpResponse;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static com.opethic.genivis.utils.FxmFileConstants.*;
import static com.opethic.genivis.utils.Globals.decimalFormat;
import static com.opethic.genivis.utils.Globals.mapToStringforFormData;
import static java.lang.Double.parseDouble;

public class SalesQuotationToChallan implements Initializable {
    @FXML
    private BorderPane rootAnchorPaneSoToSc;

    @FXML
    private VBox sq2cTotalMainDiv, sq2cTotalMainInnerDiv, sq2cTotalMainInnerDivFirst, sq2cTotalMainInnerDivSecond, sq2cBottomFirstV, sq2cBottomSecondV, sq2cBottomThirdV;
    @FXML
    private HBox sq2cTopFirstRow, sq2cTopSecondRow, sq2cBottomMain;

    @FXML
    private VBox vboxSalesQuotToChall;
    private static final Logger soToscLogger = LoggerFactory.getLogger(SalesQuotationToChallan.class);
    public static String scConversionId;
    public static ArrayList<String> arrayList;
    public static String input = "";    //here we get the selected list id also  we can get multiple id
    @FXML
    private TextField dpSCConvChallanDate, tfSCConvLedgNameEdit, tfSCConvChallanSerial, tfSCConvInvNo, tfSCConvEditNarrations;
    @FXML
    private ComboBox<SalesmanMasterDTO> dpSCConvSalesman;
    @FXML
//    private ComboBox cmbSCConvSupplierGST;
    private ComboBox<GstDetailsDTO> cmbSCConvSupplierGST;

    @FXML
    private ComboBox<CommonDTO> cmbSCConvSalesAC;
    @FXML
    private TableView<CmpTRowDTOSoToSc> tvSCConvEditCMPTRow;
    @FXML
    private TableView tvSCConvSupplierDetails;
    @FXML
    private TableColumn<CmpTRowDTOSoToSc, String> tblcSCConvEditCmptSrNo, tblcSCConvEditCmptParticular, tblcSCConvEditCmptPackage, tblcSCConvEditCmptLevelA, tblcSCConvEditCmptLevelB, tblcSCConvEditCmptLevelC,
            tblcSCConvEditCmptUnit, tblcSCConvEditCmptBatch, tblcSCConvEditCmptQuantity, tblcSCConvEditCmptFreeQty, tblcSCConvEditCmptRate, tblcSCConvEditCmptGrossAmt, tblcSCConvEditCmptDiscPerc, tblcSCConvEditCmptDisPer2,
            tblcSCConvEditCmptDiscAmt, tblcSCConvEditCmptTaxPerc, tblcSCConvEditCmptNetAmt, tblcSCConvEditCmptActions;

    //for bottom supplier datails table column
    @FXML
    private TableColumn tcSCConvEditSupplierName, tcSCConvEditSupplierInvNo, tcSCConvEditSupplierInvDate, tcSCConvEditSupplierBatch, tcSCConvEditSupplierMrp, tcSCConvEditSupplierQty,
            tcSCConvEditSupplierRate, tcSCConvEditSupplierCost, tcSCConvEditSupplierDisper, tcSCConvEditSupplierDisRs;
    @FXML
    private TabPane myTabPaneSCConv;
    //    @FXML         //for batch window
//    private HBox hbapSalesChallanBatchWindow;
    @FXML          //for batch window
    private Label lblScConvBatWinPrdName, lblScConvBatWinCWT, lblScConvBatWinCWOT, lblScConvBatWinTax, lblScConvCurrSTK;
    //    @FXML      //for batch window
//    private Button btnSScConvBatWinClose;
    @FXML    //for batch window
    private TextField tfScConvBatWinBatSearch;
    @FXML    //for batch window
    private CheckBox chbScConvBatNearExp;
    //    @FXML           //batch window
//    private TableView<TranxBatchWindowDTO> tblvScConvBatWinCreTableview;
//    @FXML       //for batch window
//    private TableColumn<TranxBatchWindowDTO, String> tblcScConvBatWinBatchNo, tblcScConvBatWinMRP,
//            tblcScConvBatWinPurRate, tblcScConvBatWinQty, tblcScConvBatWinFreeQty, tblcScConvBatWinDisPer, tblcScConvBatWinDisAmt, tblcScConvBatWinBarcode, tblcScConvBatWinMarginPer, tblcScConvBatWinFSR, tblcScConvBatWinCSR, tblcScConvBatWinSaleRate;
//    @FXML
//    private TableColumn<TranxBatchWindowDTO, Void> tblcScConvBatWinAction;
//    @FXML
//    private TableColumn<TranxBatchWindowDTO, LocalDate> tblcScConvBatWinPurDate, tblcScConvBatWinMFGDate, tblcScConvBatWinEXPDate;
    @FXML
    private Tab tabSCConvLedgerInfoEdit, tabSCConvProductInfoEdit, tabSCConvBatchInfoEdit;
    //batch info Labels
    @FXML
    private Text lbSCConvBatchBatchNoEdit, lbSCConvBatchMrpEdit, lbSCConvBatchCessPerEdit, lbSCConvBatchTotalAmtEdit,
            lbSCConvBatchMfgDateEdit, lbSCConvBatchPurRateEdit, lbSCConvBatchCessAmtEdit, lbSCConvBatchFsrEdit,
            lbSCConvBatchExpDateEdit, lbSCConvBatchDisPerEdit, lbSCConvBatchBarcodeEdit, lbSCConvBatchCsrEdit,
            lbSCConvBatchAvailStockEdit, lbSCConvBatchDisAmtEdit, lbSCConvBatchMarginPerEdit, lbSCConvBatchSaleRateEdit;


    //Ledger Info Labels
    @FXML
    private Text lbSCConvGstNoEdit, lbSCConvAreaEdit, lbSCConvBankEdit, lbSCConvContactPersonEdit, lbSCConvMobNoEdit,
            lbSCConvCreditDaysEdit, lbSCConvFssaiEdit, lbSCConvLisenceNoEdit, lbSCConvRouteEdit;
    //Product Info Labels
    @FXML
    private Text txtSCConvProductBrandEdit, txtSCConvProductGroupEdit, txtSCConvProductSubGroupEdit, txtSCConvProductCategoryEdit, txtSCConvProductHsnEdit, txtSCConvProductTaxTypeEdit,
            txtSCConvProductTaxPerEdit, txtSCConvProductMarginPerEdit, txtSCConvProductCostEdit, txtSCConvProductShelfIdEdit, txtSCConvProductMinStockEdit, txtSCConvProductMaxStockEdit;
    @FXML                //for overall dis% and dis Rs.
    private TextField tfSCConvAllRowDisPerEdit, tfSCConvAllRowDisAmtEdit;
    @FXML               //for bottom GST details
    private TableView<GstDTO> tblvSCConvGSTEdit;
    @FXML
    private TableColumn<GstDTO, String> tcSCConvBottomGSTEdit, tcSCConvBottomCGSTEdit, tcSCConvBottomSGSTEdit;
    @FXML               //for bottom GST total display label
    private Label lblSCConvTotalCGSTEdit, lblSCConvTotalSGSTEdit;
    @FXML               //for bottom GST total display label
    private Label lblSCConvTotalQtyEdit, lblSCConvFreeQtyEdit, lblSCConvRoundOffEdit;
    @FXML
    private CheckBox chbRoundOffEdit;
    @FXML
    private TextField tfSCConvAddChrgTotalAmt;
    @FXML                    ///for final bill amount display
    private Label lblSCConvGrossTotalEdit, lblSCConvTotalDiscountEdit, lblSCConvTotalTaxableAmountEdit, lblSCConvTotalTaxEdit, lblSCConvBillAmountEdit;
    @FXML
    private Button btnSCConvCancelEdit, btnSCConvSubmitFinal;
    private String productId;
    private static ObservableList<String> unitList = FXCollections.observableArrayList();
    private static int rowIndexBatchNo;
    private String prdBatchTaxPer;
    private int selectedRowIndex;
    private String selectedRowPrdId;
    private String productIdSelected;
    private String ledgerName;
    private String Debtor_id;
    private Double costValue, costWithTaxValue, taxAmt;

    String gst_no = "";

    String purchaseAcID = "";

    String supplierGSTIN_ID = "";

    String ledger_id = "";

    private ObservableList<CommonDTO> supplierGSTINList = FXCollections.observableArrayList();
//    Map<String, String> purchase_invoice_map = new HashMap<>();


    private String ledgerStateCode = "";
    private Boolean taxFlag;

    private String totalamt = "";

    private Double total_taxable_amt = 0.0;

    private String total_purchase_discount_amt = "";

    List<CustomAddChargesDTO> addChargesDTOList = new ArrayList<>();
    Long stateCode = 0L, CompanyStateCode = 0L;
    public static String QuotToChallanId = "";

    public void sceneInitilization() {
//        PurInvoiceCommunicator.resetFields();//new 2.0 to Clear the CMPTROW ComboBox
        rootAnchorPaneSoToSc.sceneProperty().addListener((observable, oldScene, newScene) -> {
            if (newScene != null && newScene.getWindow() instanceof Stage) {
                Communicator.stage = (Stage) newScene.getWindow();
                Communicator.tranxDate = dpSCConvChallanDate.getText().toString();
            }
        });
    }

    public void cancelSalesQuotationToChallan(ActionEvent actionEvent) {
        System.out.println("cancel fn");
        AlertUtility.AlertConfirmation(AlertUtility.alertTypeConfirmation, "Are you sure want to cancel ?", input -> {
            if (input == 1) {
                GlobalController.getInstance().addTabStatic(SALES_QUOTATION_LIST_SLUG, false);
            }
        });
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        resposiveScreenCss();

        CommonValidationsUtils.changeStarColour(vboxSalesQuotToChall);
        //todo : autofocus on challan Date
        Platform.runLater(() -> {
            tfSCConvLedgNameEdit.requestFocus();
//            dpSCConvSalesman.setOnKeyPressed(e->{
//                if(e.getCode()==KeyCode.ENTER || e.getCode()==KeyCode.TAB){
//                    tblvSCConvGSTEdit.requestFocus();
//                }
//            });
        });
        System.out.println("QuotToChallanId>>> " + QuotToChallanId);

        //         Enter traversal
        rootAnchorPaneSoToSc.addEventFilter(KeyEvent.KEY_PRESSED, (KeyEvent event) -> {
            if (event.getCode() == KeyCode.ENTER) {
                if (event.getTarget() instanceof Button targetButton && targetButton.getText().equalsIgnoreCase("Cancel")) {
                } else if (event.getTarget() instanceof Button targetButton && targetButton.getText().equalsIgnoreCase("Submit")) {
                } else {
                    if (tfSCConvInvNo.getText() != "") {
                        validateSalesChallanNo();
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
                if (tfSCConvInvNo.getText() != "") {
                    validateSalesChallanNo();
                }
            }
        });

        sceneInitilization();

        System.out.println("inpputtt " + input);
        responsiveCmpt();
        responsiveSupplierTable();
        getSalesAccounts();
        getLastSaleChallanRecord();
        getOutletSalesmanMasterRecord();
        getIndirectExpenseList();
        getSalesQuotationWithId();
        calculateEachRowTraxTotal();
        gstTableDesign();

        System.out.println("scConversionId " + scConversionId); //we get the id of sales order which list we have selected
        DateValidator.applyDateFormat(dpSCConvChallanDate);

        //todo: set the TranxDate Serial No TextField to Readonly
        tfSCConvChallanSerial.setDisable(true);

        dpSCConvChallanDate.setDisable(true);

//        btnSScConvBatWinClose.setOnAction(event -> {
//            hbapSalesChallanBatchWindow.setVisible(false);
//        });
//        hbapSalesChallanBatchWindow.setManaged(false);

        tvSCConvEditCMPTRow.setEditable(true);  //for enable the edit the CMPT row
        tvSCConvEditCMPTRow.getSelectionModel().setCellSelectionEnabled(true);
        tvSCConvEditCMPTRow.getSelectionModel().selectFirst();
        tvSCConvEditCMPTRow.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection == null) {
                tvSCConvEditCMPTRow.getSelectionModel().select(oldSelection);
            }
        });


        tblvSCConvGSTEdit.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);


        tableInitiliazation();


        btnSCConvSubmitFinal.setOnAction(event -> {
            AlertUtility.AlertConfirmation(AlertUtility.alertTypeConfirmation, "Do you want to Create", input -> {
                if (CommonValidationsUtils.validateForm(tfSCConvLedgNameEdit, tfSCConvInvNo, tfSCConvChallanSerial, dpSCConvChallanDate)) {
                    if (input == 1) {
                        createSalesChallan();
                    }
                }
            });
        });
//        tblvScConvBatWinCreTableview.setEditable(true);
//        tblvScConvBatWinCreTableview.getSelectionModel().setCellSelectionEnabled(true);
//        tblvScConvBatWinCreTableview.getSelectionModel().selectFirst();
//        tblvScConvBatWinCreTableview.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
//            if (newSelection == null) {
//                tblvScConvBatWinCreTableview.getSelectionModel().select(oldSelection);
//            }
//        });
        shortcutKeysSalesQuotToChallan();
    }


    private void resposiveScreenCss() {
        // For Responsive Resolutions Css Style Apply
        double height = Screen.getPrimary().getBounds().getHeight();
        double width = Screen.getPrimary().getBounds().getWidth();
        System.out.println("width" + width);
        if (width >= 992 && width <= 1024) {
            sq2cTopFirstRow.setSpacing(10);
            sq2cTopSecondRow.setSpacing(10);
            sq2cTotalMainDiv.setSpacing(10);
            sq2cTotalMainInnerDiv.setSpacing(8);
            sq2cTotalMainInnerDivFirst.setSpacing(8);
            sq2cTotalMainInnerDivSecond.setSpacing(8);
            // Set preferred widths as percentages of the HBox width
            sq2cBottomFirstV.prefWidthProperty().bind(sq2cBottomMain.widthProperty().multiply(0.54));
            sq2cBottomSecondV.prefWidthProperty().bind(sq2cBottomMain.widthProperty().multiply(0.22));
            sq2cBottomThirdV.prefWidthProperty().bind(sq2cBottomMain.widthProperty().multiply(0.24));
//            so2cBottomMain.setPrefHeight(150);
            tblvSCConvGSTEdit.getStylesheets().add(GenivisApplication.class.getResource("ui/css/purchase_challan_css/gst_table_css/gst_table_992_1024.css").toExternalForm());
            tvSCConvSupplierDetails.getStylesheets().add(GenivisApplication.class.getResource("ui/css/purchase_challan_css/supplier_table_css/supplier_table_992_1024.css").toExternalForm());
            rootAnchorPaneSoToSc.getStylesheets().add(GenivisApplication.class.getResource("ui/css/TranxCommonCssStyles/tranxCommonCssStyle1.css").toExternalForm());
        } else if (width >= 1025 && width <= 1280) {
            sq2cTopFirstRow.setSpacing(12);
            sq2cTopSecondRow.setSpacing(12);
            sq2cTotalMainDiv.setSpacing(12);
            sq2cTotalMainInnerDiv.setSpacing(10);
            sq2cTotalMainInnerDivFirst.setSpacing(10);
            sq2cTotalMainInnerDivSecond.setSpacing(10);
            // Set preferred widths as percentages of the HBox width
            sq2cBottomFirstV.prefWidthProperty().bind(sq2cBottomMain.widthProperty().multiply(0.56));
            sq2cBottomSecondV.prefWidthProperty().bind(sq2cBottomMain.widthProperty().multiply(0.22));
            sq2cBottomThirdV.prefWidthProperty().bind(sq2cBottomMain.widthProperty().multiply(0.22));
//            so2cBottomMain.setPrefHeight(150);
            tblvSCConvGSTEdit.getStylesheets().add(GenivisApplication.class.getResource("ui/css/purchase_challan_css/gst_table_css/gst_table_1025_1280.css").toExternalForm());
            tvSCConvSupplierDetails.getStylesheets().add(GenivisApplication.class.getResource("ui/css/purchase_challan_css/supplier_table_css/supplier_table_1025_1280.css").toExternalForm());
            rootAnchorPaneSoToSc.getStylesheets().add(GenivisApplication.class.getResource("ui/css/TranxCommonCssStyles/tranxCommonCssStyle2.css").toExternalForm());
        } else if (width >= 1281 && width <= 1368) {
            sq2cTopFirstRow.setSpacing(12);
            sq2cTopSecondRow.setSpacing(12);
            sq2cTotalMainDiv.setSpacing(12);
            sq2cTotalMainInnerDiv.setSpacing(10);
            sq2cTotalMainInnerDivFirst.setSpacing(10);
            sq2cTotalMainInnerDivSecond.setSpacing(10);
            // Set preferred widths as percentages of the HBox width
            sq2cBottomFirstV.prefWidthProperty().bind(sq2cBottomMain.widthProperty().multiply(0.58));
            sq2cBottomSecondV.prefWidthProperty().bind(sq2cBottomMain.widthProperty().multiply(0.20));
            sq2cBottomThirdV.prefWidthProperty().bind(sq2cBottomMain.widthProperty().multiply(0.22));
//            so2cBottomMain.setPrefHeight(150);
            tblvSCConvGSTEdit.getStylesheets().add(GenivisApplication.class.getResource("ui/css/purchase_challan_css/gst_table_css/gst_table_1281_1368.css").toExternalForm());
            tvSCConvSupplierDetails.getStylesheets().add(GenivisApplication.class.getResource("ui/css/purchase_challan_css/supplier_table_css/supplier_table_1281_1368.css").toExternalForm());
            rootAnchorPaneSoToSc.getStylesheets().add(GenivisApplication.class.getResource("ui/css/TranxCommonCssStyles/tranxCommonCssStyle3.css").toExternalForm());
        } else if (width >= 1369 && width <= 1400) {
            sq2cTopFirstRow.setSpacing(15);
            sq2cTopSecondRow.setSpacing(15);
            sq2cTotalMainDiv.setSpacing(15);
            sq2cTotalMainInnerDiv.setSpacing(10);
            sq2cTotalMainInnerDivFirst.setSpacing(10);
            sq2cTotalMainInnerDivSecond.setSpacing(10);
            // Set preferred widths as percentages of the HBox width
            sq2cBottomFirstV.prefWidthProperty().bind(sq2cBottomMain.widthProperty().multiply(0.54));
            sq2cBottomSecondV.prefWidthProperty().bind(sq2cBottomMain.widthProperty().multiply(0.22));
            sq2cBottomThirdV.prefWidthProperty().bind(sq2cBottomMain.widthProperty().multiply(0.24));
//            so2cBottomMain.setPrefHeight(150);
            tblvSCConvGSTEdit.getStylesheets().add(GenivisApplication.class.getResource("ui/css/purchase_challan_css/gst_table_css/gst_table_1369_1400.css").toExternalForm());
            tvSCConvSupplierDetails.getStylesheets().add(GenivisApplication.class.getResource("ui/css/purchase_challan_css/supplier_table_css/supplier_table_1369_1400.css").toExternalForm());
            rootAnchorPaneSoToSc.getStylesheets().add(GenivisApplication.class.getResource("ui/css/TranxCommonCssStyles/tranxCommonCssStyle4.css").toExternalForm());
        } else if (width >= 1401 && width <= 1440) {
            sq2cTopFirstRow.setSpacing(15);
            sq2cTopSecondRow.setSpacing(15);
            sq2cTotalMainDiv.setSpacing(15);
            sq2cTotalMainInnerDiv.setSpacing(10);
            sq2cTotalMainInnerDivFirst.setSpacing(10);
            sq2cTotalMainInnerDivSecond.setSpacing(10);
            // Set preferred widths as percentages of the HBox width
            sq2cBottomFirstV.prefWidthProperty().bind(sq2cBottomMain.widthProperty().multiply(0.56));
            sq2cBottomSecondV.prefWidthProperty().bind(sq2cBottomMain.widthProperty().multiply(0.22));
            sq2cBottomThirdV.prefWidthProperty().bind(sq2cBottomMain.widthProperty().multiply(0.22));
//            so2cBottomMain.setPrefHeight(150);
            tblvSCConvGSTEdit.getStylesheets().add(GenivisApplication.class.getResource("ui/css/purchase_challan_css/gst_table_css/gst_table_1401_1440.css").toExternalForm());
            tvSCConvSupplierDetails.getStylesheets().add(GenivisApplication.class.getResource("ui/css/purchase_challan_css/supplier_table_css/supplier_table_1401_1440.css").toExternalForm());
            rootAnchorPaneSoToSc.getStylesheets().add(GenivisApplication.class.getResource("ui/css/TranxCommonCssStyles/tranxCommonCssStyle5.css").toExternalForm());
        } else if (width >= 1441 && width <= 1680) {
            sq2cTopFirstRow.setSpacing(20);
            sq2cTopSecondRow.setSpacing(20);
            sq2cTotalMainDiv.setSpacing(20);
            sq2cTotalMainInnerDiv.setSpacing(10);
            sq2cTotalMainInnerDivFirst.setSpacing(10);
            sq2cTotalMainInnerDivSecond.setSpacing(10);
            // Set preferred widths as percentages of the HBox width
            sq2cBottomFirstV.prefWidthProperty().bind(sq2cBottomMain.widthProperty().multiply(0.6));
            sq2cBottomSecondV.prefWidthProperty().bind(sq2cBottomMain.widthProperty().multiply(0.20));
            sq2cBottomThirdV.prefWidthProperty().bind(sq2cBottomMain.widthProperty().multiply(0.20));
//            scBottomMain.setPrefHeight(150);
            tblvSCConvGSTEdit.getStylesheets().add(GenivisApplication.class.getResource("ui/css/purchase_challan_css/gst_table_css/gst_table_1441_1680.css").toExternalForm());
            tvSCConvSupplierDetails.getStylesheets().add(GenivisApplication.class.getResource("ui/css/purchase_challan_css/supplier_table_css/supplier_table_1441_1680.css").toExternalForm());
            rootAnchorPaneSoToSc.getStylesheets().add(GenivisApplication.class.getResource("ui/css/TranxCommonCssStyles/tranxCommonCssStyle6.css").toExternalForm());
        } else if (width >= 1681 && width <= 1920) {
            sq2cTopFirstRow.setSpacing(20);
            sq2cTopSecondRow.setSpacing(20);
            sq2cTotalMainDiv.setSpacing(20);
            sq2cTotalMainInnerDiv.setSpacing(10);
            sq2cTotalMainInnerDivFirst.setSpacing(10);
            sq2cTotalMainInnerDivSecond.setSpacing(10);
            // Set preferred widths as percentages of the HBox width
            sq2cBottomFirstV.prefWidthProperty().bind(sq2cBottomMain.widthProperty().multiply(0.6));
            sq2cBottomSecondV.prefWidthProperty().bind(sq2cBottomMain.widthProperty().multiply(0.18));
            sq2cBottomThirdV.prefWidthProperty().bind(sq2cBottomMain.widthProperty().multiply(0.22));
//            scBottomMain.setPrefHeight(150);
            tblvSCConvGSTEdit.getStylesheets().add(GenivisApplication.class.getResource("ui/css/gst_table.css").toExternalForm());
            tvSCConvSupplierDetails.getStylesheets().add(GenivisApplication.class.getResource("ui/css/invoice_product_history_table.css").toExternalForm());
            rootAnchorPaneSoToSc.getStylesheets().add(GenivisApplication.class.getResource("ui/css/TranxCommonCssStyles/tranxCommonCssStyle7.css").toExternalForm());

        }
    }

    private void shortcutKeysSalesQuotToChallan() {
        rootAnchorPaneSoToSc.addEventFilter(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {

            @Override
            public void handle(KeyEvent event) {
                if (event.getCode() == KeyCode.S && event.isControlDown()) {
                    AlertUtility.AlertConfirmation(AlertUtility.alertTypeConfirmation, "Do you want to Create", input -> {
                        if (input == 1) {
                            if (CommonValidationsUtils.validateForm(tfSCConvLedgNameEdit, tfSCConvChallanSerial, tfSCConvInvNo, dpSCConvChallanDate)) {
                                createSalesChallan();
                            }
                        }
                    });
                }

                if (event.getCode() == KeyCode.X && event.isControlDown()) {
//                    AlertUtility.AlertConfirmation(AlertUtility.alertTypeConfirmation, "Are you sure want to cancel ?", input -> {
//                        if (input == 1) {
//                            GlobalController.getInstance().addTabStatic(SALES_QUOTATION_LIST_SLUG, false);
//                        }
//                    });
                    btnSCConvCancelEdit.fire();
                }
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
            tcSCConvEditSupplierName.setCellValueFactory(new PropertyValueFactory<>("supplierName"));
            tcSCConvEditSupplierInvNo.setCellValueFactory(new PropertyValueFactory<>("supplierInvNo"));
            tcSCConvEditSupplierInvDate.setCellValueFactory(new PropertyValueFactory<>("supplierInvDate"));
            tcSCConvEditSupplierBatch.setCellValueFactory(new PropertyValueFactory<>("supplierBatch"));
            tcSCConvEditSupplierMrp.setCellValueFactory(new PropertyValueFactory<>("supplierMrp"));
            tcSCConvEditSupplierQty.setCellValueFactory(new PropertyValueFactory<>("supplierQty"));
            tcSCConvEditSupplierRate.setCellValueFactory(new PropertyValueFactory<>("supplierRate"));
            tcSCConvEditSupplierCost.setCellValueFactory(new PropertyValueFactory<>("supplierCost"));
            tcSCConvEditSupplierDisper.setCellValueFactory(new PropertyValueFactory<>("supplierDisPer"));
            tcSCConvEditSupplierDisRs.setCellValueFactory(new PropertyValueFactory<>("supplierDisAmt"));

            tvSCConvSupplierDetails.setItems(supplierDataList);

        }
    }

    //todo: fetch Selected Product data for product info
    public void fetchSelectedProductData(String ProductId) {
        try {
            soToscLogger.info("Fetching Selected Product Data to show in product info");
            Map<String, String> body = new HashMap<>();
            body.put("product_id", ProductId);
            System.out.println("ProductId 123 " + ProductId);
            String requestBody = Globals.mapToStringforFormData(body);
            HttpResponse<String> response = APIClient.postFormDataRequest(requestBody, EndPoints.TRANSACTION_PRODUCT_DETAILS);
            System.out.println("Product Details response body" + response.body());
            JsonObject jsonObject = new Gson().fromJson(response.body(), JsonObject.class);
            ObservableList<TranxProductWindowDTO> observableList = FXCollections.observableArrayList();
            //todo: activating the product tab
            myTabPaneSCConv.getSelectionModel().select(tabSCConvProductInfoEdit);
            if (jsonObject.get("responseStatus").getAsInt() == 200) {
                myTabPaneSCConv.getSelectionModel().select(tabSCConvProductInfoEdit);

                JsonObject item = jsonObject.get("result").getAsJsonObject();
                System.out.println("Item:" + item);
                String brand = item.get("brand").getAsString();
                String hsn = item.get("hsn").getAsString();
                String group = item.get("group").getAsString();
                String subGroup = item.get("subgroup").getAsString();
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
                txtSCConvProductBrandEdit.setText(brand);
                txtSCConvProductHsnEdit.setText(hsn);
                txtSCConvProductGroupEdit.setText(group);
                txtSCConvProductSubGroupEdit.setText(subGroup);
                txtSCConvProductCategoryEdit.setText(category);
//                lbSalesQuotationCreateProdInfoSupplier.setText(supplier);
                txtSCConvProductTaxTypeEdit.setText(tax_type);
                txtSCConvProductTaxPerEdit.setText(tax);
                txtSCConvProductMarginPerEdit.setText(margin);
                txtSCConvProductCostEdit.setText(cost);
                txtSCConvProductShelfIdEdit.setText(shelfId);
                txtSCConvProductMinStockEdit.setText(min_stock);
                txtSCConvProductMaxStockEdit.setText(max_stock);

            } else {
                System.out.println("Error in response");
            }
        } catch (Exception e) {
            soToscLogger.error("Error in Fetching Selected Product data is" + e.getMessage());
            e.printStackTrace();
        }
    }

    //todo: fetch Selected ledger data
    public void fetchSelectedLedgerData(String LedgerId) {
        try {
            soToscLogger.info("Fetching Selected Ledger Data to show in Ledger info");
            Map<String, String> body = new HashMap<>();
            body.put("ledger_id", LedgerId);
            System.out.println("ledger_id 123 " + LedgerId);
            String requestBody = Globals.mapToStringforFormData(body);
            HttpResponse<String> response = APIClient.postFormDataRequest(requestBody, EndPoints.TRANSACTION_LEDGER_DETAILS);
            soToscLogger.info("response data of selected Ledger " + response.body());
            JsonObject jsonObject = new Gson().fromJson(response.body(), JsonObject.class);
            ObservableList<TranxLedgerWindowDTO> observableList = FXCollections.observableArrayList();
            System.out.println("fetchedSelectedLedgerData " + jsonObject);
            if (jsonObject.get("responseStatus").getAsInt() == 200) {
                //todo: activating the ledger tab
                myTabPaneSCConv.getSelectionModel().select(tabSCConvLedgerInfoEdit);

                CompanyStateCode = jsonObject.get("company_state_code").getAsLong();
                System.out.println("CompanyStateCode " + CompanyStateCode);
                JsonObject item = jsonObject.get("result").getAsJsonObject();
                stateCode = item.get("stateCode").getAsLong();
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

                lbSCConvGstNoEdit.setText(gstNo);
                lbSCConvAreaEdit.setText(Area);
                lbSCConvBankEdit.setText(bank);
                lbSCConvContactPersonEdit.setText(contact_person);
                lbSCConvMobNoEdit.setText(mobile_no);
                lbSCConvCreditDaysEdit.setText(credit_days);
                lbSCConvFssaiEdit.setText(fssai);
                lbSCConvLisenceNoEdit.setText(licence_no);
                lbSCConvRouteEdit.setText(route);

            } else {

                soToscLogger.error("Error in response of fetching selected Ledger data");
            }
        } catch (Exception e) {
            soToscLogger.error("Error in Fetching Selected Ledger data is" + e.getMessage());
            e.printStackTrace();
        }
    }

    //todo: fetch Selected Batch Data for Batch Info
    private void tranxBatchDetailsFun(String bNo, String bId) {
        APIClient apiClient = null;
        myTabPaneSCConv.getSelectionModel().select(tabSCConvBatchInfoEdit);
        soToscLogger.debug("Get Supplier List Data Started...");
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
                    lbSCConvBatchBatchNoEdit.setText(batchJsonObj.get("batchNo").getAsString());
                    lbSCConvBatchMrpEdit.setText(batchJsonObj.get("batchMrp").getAsString());
                    lbSCConvBatchMfgDateEdit.setText(DateConvertUtil.convertDispDateFormat(batchJsonObj.get("mfgDate").getAsString()));
                    lbSCConvBatchExpDateEdit.setText(DateConvertUtil.convertDispDateFormat(batchJsonObj.get("expDate").getAsString()));
                    lbSCConvBatchPurRateEdit.setText(batchJsonObj.get("pur_rate").getAsString());
                    lbSCConvBatchAvailStockEdit.setText(batchJsonObj.get("qty").getAsString());
                    lbSCConvBatchDisPerEdit.setText(batchJsonObj.get("b_dis_per").getAsString());
                    lbSCConvBatchDisAmtEdit.setText(batchJsonObj.get("b_dis_amt").getAsString());
                    lbSCConvBatchSaleRateEdit.setText(batchJsonObj.get("sales_rate").getAsString());
                    lbSCConvBatchFsrEdit.setText(batchJsonObj.get("fsrmh").getAsString());
                    lbSCConvBatchTotalAmtEdit.setText("0.0");
                    lbSCConvBatchCsrEdit.setText(batchJsonObj.get("csrmh").getAsString());
                    lbSCConvBatchCessPerEdit.setText("0.0");
                    lbSCConvBatchCessAmtEdit.setText("0.0");
                    lbSCConvBatchBarcodeEdit.setText(batchJsonObj.get("barcode") != null ? batchJsonObj.get("barcode").toString() : "0.0");
                }
            }
        });
        apiClient.setOnCancelled(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent workerStateEvent) {
                soToscLogger.error("Network API cancelled in getSupplierListbyProductId()" + workerStateEvent.getSource().getValue().toString());

            }
        });

        apiClient.setOnFailed(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent workerStateEvent) {
                soToscLogger.error("Network API failed in getSupplierListbyProductId()" + workerStateEvent.getSource().getValue().toString());
            }
        });
        apiClient.start();
        soToscLogger.debug("Get Supplier List Data End...");

    }

    @FXML
    private void onClickAdditionalCharges(ActionEvent actionEvent) {
        Stage stage = (Stage) rootAnchorPaneSoToSc.getScene().getWindow();

        Double totalTaxableAmount = 0.0;

        AdditionalCharges.additionalCharges(stage, addChargesDTOList, total_taxable_amt, (input1, input2) -> {
            addChargesDTOList.clear();
            addChargesDTOList.addAll(input2);

//            System.out.println("acDelDetailsIds:"+input1[0]);
//            System.out.println("Add Charges:"+input1[1]);
//            System.out.println(addChargesDTOList);

            tfSCConvAddChrgTotalAmt.setText(input1[1]);
            salesQuoToChalCalculation.additionalChargesCalculation(input1[1], tvSCConvEditCMPTRow, callback);

        });
    }


    //function for add Blank CMPT row
    public void addRow(ActionEvent actionEvent) {
        // Check if any row is selected
        CmpTRowDTOSoToSc newRow = new CmpTRowDTOSoToSc("", "", "", "", "", "", "", "", "", "", "","", "", "", "", "", "", "", "");
        tvSCConvEditCMPTRow.getItems().add(newRow);
    }

    private void gstTableDesign() {
        tblvSCConvGSTEdit.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tcSCConvBottomGSTEdit.setCellValueFactory(cellData -> {
            StringProperty taxProperty = cellData.getValue().taxPerProperty();
            return Bindings.createStringBinding(
                    () -> "(" + (int) Double.parseDouble(taxProperty.get()) + " %)",
                    taxProperty
            );
        });

        tcSCConvBottomGSTEdit.setStyle("-fx-alignment: CENTER;");
        tcSCConvBottomCGSTEdit.setCellValueFactory(cellData -> cellData.getValue().cgstProperty());
        tcSCConvBottomCGSTEdit.setCellFactory(col -> new TableCell<GstDTO, String>() {
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


        tcSCConvBottomCGSTEdit.setStyle("-fx-alignment: CENTER;");
        tcSCConvBottomSGSTEdit.setCellValueFactory(cellData -> cellData.getValue().sgstProperty());
        tcSCConvBottomSGSTEdit.setStyle("-fx-alignment: CENTER;");
        tcSCConvBottomSGSTEdit.setCellFactory(col -> new TableCell<GstDTO, String>() {
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

    //function for responsicve of CMPT row Table
    private void responsiveCmpt() {
        tblcSCConvEditCmptSrNo.prefWidthProperty().bind(tvSCConvEditCMPTRow.widthProperty().multiply(0.03));
        tblcSCConvEditCmptPackage.prefWidthProperty().bind(tvSCConvEditCMPTRow.widthProperty().multiply(0.05));
        tblcSCConvEditCmptUnit.prefWidthProperty().bind(tvSCConvEditCMPTRow.widthProperty().multiply(0.05));
        tblcSCConvEditCmptQuantity.prefWidthProperty().bind(tvSCConvEditCMPTRow.widthProperty().multiply(0.05));
        tblcSCConvEditCmptRate.prefWidthProperty().bind(tvSCConvEditCMPTRow.widthProperty().multiply(0.08));
        tblcSCConvEditCmptDiscPerc.prefWidthProperty().bind(tvSCConvEditCMPTRow.widthProperty().multiply(0.05));
        tblcSCConvEditCmptDiscAmt.prefWidthProperty().bind(tvSCConvEditCMPTRow.widthProperty().multiply(0.05));
//        tblcSCConvEditCmptNetAmt.prefWidthProperty().bind(tvSCConvEditCMPTRow.widthProperty().multiply(0.05));
        tblcSCConvEditCmptActions.prefWidthProperty().bind(tvSCConvEditCMPTRow.widthProperty().multiply(0.03));
        tblcSCConvEditCmptBatch.prefWidthProperty().bind(tvSCConvEditCMPTRow.widthProperty().multiply(0.07));
        if (tblcSCConvEditCmptLevelA.isVisible() == true && tblcSCConvEditCmptLevelB.isVisible() == false && tblcSCConvEditCmptLevelC.isVisible() == false) {
            tblcSCConvEditCmptParticular.prefWidthProperty().bind(tvSCConvEditCMPTRow.widthProperty().multiply(0.28));
            tblcSCConvEditCmptGrossAmt.prefWidthProperty().bind(tvSCConvEditCMPTRow.widthProperty().multiply(0.11));
            tblcSCConvEditCmptNetAmt.prefWidthProperty().bind(tvSCConvEditCMPTRow.widthProperty().multiply(0.04));
            tblcSCConvEditCmptLevelA.prefWidthProperty().bind(tvSCConvEditCMPTRow.widthProperty().multiply(0.05));
        } else if (tblcSCConvEditCmptLevelA.isVisible() == true && tblcSCConvEditCmptLevelB.isVisible() == true && tblcSCConvEditCmptLevelC.isVisible() == false) {
            tblcSCConvEditCmptLevelA.prefWidthProperty().bind(tvSCConvEditCMPTRow.widthProperty().multiply(0.05));
            tblcSCConvEditCmptParticular.prefWidthProperty().bind(tvSCConvEditCMPTRow.widthProperty().multiply(0.26));
            tblcSCConvEditCmptGrossAmt.prefWidthProperty().bind(tvSCConvEditCMPTRow.widthProperty().multiply(0.08));
            tblcSCConvEditCmptNetAmt.prefWidthProperty().bind(tvSCConvEditCMPTRow.widthProperty().multiply(0.04));
            tblcSCConvEditCmptLevelB.prefWidthProperty().bind(tvSCConvEditCMPTRow.widthProperty().multiply(0.05));
        } else if (tblcSCConvEditCmptLevelA.isVisible() == true && tblcSCConvEditCmptLevelB.isVisible() == true && tblcSCConvEditCmptLevelC.isVisible() == true) {
            tblcSCConvEditCmptParticular.prefWidthProperty().bind(tvSCConvEditCMPTRow.widthProperty().multiply(0.22));
            tblcSCConvEditCmptGrossAmt.prefWidthProperty().bind(tvSCConvEditCMPTRow.widthProperty().multiply(0.1));
            tblcSCConvEditCmptNetAmt.prefWidthProperty().bind(tvSCConvEditCMPTRow.widthProperty().multiply(0.04));
            tblcSCConvEditCmptLevelA.prefWidthProperty().bind(tvSCConvEditCMPTRow.widthProperty().multiply(0.04));
            tblcSCConvEditCmptLevelB.prefWidthProperty().bind(tvSCConvEditCMPTRow.widthProperty().multiply(0.04));
            tblcSCConvEditCmptLevelC.prefWidthProperty().bind(tvSCConvEditCMPTRow.widthProperty().multiply(0.04));
        } else {
            tblcSCConvEditCmptParticular.prefWidthProperty().bind(tvSCConvEditCMPTRow.widthProperty().multiply(0.34));
            tblcSCConvEditCmptGrossAmt.prefWidthProperty().bind(tvSCConvEditCMPTRow.widthProperty().multiply(0.12));
            tblcSCConvEditCmptNetAmt.prefWidthProperty().bind(tvSCConvEditCMPTRow.widthProperty().multiply(0.08));
        }
    }

    //function for responsive of Bottom supplier details
    private void responsiveSupplierTable() {
        tcSCConvEditSupplierName.prefWidthProperty().bind(tvSCConvSupplierDetails.widthProperty().multiply(0.2));
        tcSCConvEditSupplierInvNo.prefWidthProperty().bind(tvSCConvSupplierDetails.widthProperty().multiply(0.1));
        tcSCConvEditSupplierInvDate.prefWidthProperty().bind(tvSCConvSupplierDetails.widthProperty().multiply(0.1));
        tcSCConvEditSupplierBatch.prefWidthProperty().bind(tvSCConvSupplierDetails.widthProperty().multiply(0.06));
        tcSCConvEditSupplierMrp.prefWidthProperty().bind(tvSCConvSupplierDetails.widthProperty().multiply(0.12));
        tcSCConvEditSupplierQty.prefWidthProperty().bind(tvSCConvSupplierDetails.widthProperty().multiply(0.08));
        tcSCConvEditSupplierRate.prefWidthProperty().bind(tvSCConvSupplierDetails.widthProperty().multiply(0.12));
        tcSCConvEditSupplierCost.prefWidthProperty().bind(tvSCConvSupplierDetails.widthProperty().multiply(0.1));
        tcSCConvEditSupplierDisper.prefWidthProperty().bind(tvSCConvSupplierDetails.widthProperty().multiply(0.06));
        tcSCConvEditSupplierDisRs.prefWidthProperty().bind(tvSCConvSupplierDetails.widthProperty().multiply(0.06));
    }

    //function for get Sales Accounts
    private void getSalesAccounts() {
        try {
            HttpResponse<String> response = APIClient.getRequest("get_sales_accounts");
            String respBody = response.body();
            JsonObject jsonObject = new Gson().fromJson(respBody, JsonObject.class);
            if (jsonObject.get("responseStatus").getAsInt() == 200) {
                JsonArray array = jsonObject.get("list").getAsJsonArray();
                ObservableList<CommonDTO> salesAccList = FXCollections.observableArrayList();
                for (JsonElement element : array) {
                    JsonObject obj = element.getAsJsonObject();
                    String salesAccId = obj.get("id").getAsString();
                    String salesAccName = obj.get("name").getAsString();
                    salesAccList.add(new CommonDTO(salesAccName, salesAccId));
                }
                cmbSCConvSalesAC.setItems(salesAccList);
                cmbSCConvSalesAC.getSelectionModel().selectFirst();
                cmbSCConvSalesAC.setConverter(new StringConverter<CommonDTO>() {
                    @Override
                    public String toString(CommonDTO commonDTO) {
                        return commonDTO != null ? commonDTO.getText() : "";
                    }

                    @Override
                    public CommonDTO fromString(String s) {
                        return null;
                    }
                });
            }
        } catch (Exception e) {
            soToscLogger.error("error in getSalesAccounts " + e.getMessage());
        }

    }

    //function for Challan Serial No
    private void getLastSaleChallanRecord() {
        soToscLogger.info("start of getLastSaleChallanRecord");
        HttpResponse<String> response = APIClient.getRequest("get_last_sales_challan_record");
        JsonObject jsonObject = new Gson().fromJson(response.body(), JsonObject.class);
        System.out.println("getLastSaleChallanRecord Res " + jsonObject);
        try {
            if (jsonObject.get("responseStatus").getAsInt() == 200) {
                tfSCConvChallanSerial.setText(jsonObject.get("count").getAsString());
                tfSCConvInvNo.setText(jsonObject.get("serialNo").getAsString());
            } else {
                soToscLogger.error("responseStatus " + jsonObject.get("responseStatus") + "  Message==>>" + jsonObject.get("message"));
            }

        } catch (Exception e) {
            soToscLogger.error("error in getLastSaleChallanRecord " + e.getMessage());
        }
    }

    //function for validation of Sales challan number
    private void validateSalesChallanNo() {
        try {
            String challlanNo = tfSCConvInvNo.getText().trim();
            System.out.println("challlanNo " + challlanNo);
            soToscLogger.info("starting of sales challan number validation");
            Map<String, String> map = new HashMap<>();
            map.put("salesChallanNo", challlanNo);
            String reqBody = Globals.mapToStringforFormData(map);
            HttpResponse<String> response = APIClient.postFormDataRequest(reqBody, EndPoints.VALIDATE_SALES_CHALLAN_NUMBER);
            JsonObject jsonObject = new Gson().fromJson(response.body(), JsonObject.class);
            System.out.println("jsonObject in validateSalesChallanNo " + jsonObject);
            if (jsonObject.get("responseStatus").getAsInt() == 409) {     //for duplicate challan number
//                AlertUtility.AlertError("Error", "Duplicate Sales Challan Number", callback -> {
//                });
                AlertUtility.AlertErrorTimeout(AlertUtility.alertTypeError, "Duplicate Sales Challan Number", in -> {
                    tfSCConvInvNo.requestFocus();
                });
            } else dpSCConvSalesman.requestFocus();

        } catch (Exception e) {
            soToscLogger.error("error in validate sales challan No");
        }
    }


    public void totalCalculation(int rowIndex) {
        Double totalGrossAmt = 0.0;
        Double grossAmt = 0.0;
        CmpTRowDTOSoToSc cmpTRowDTO = tvSCConvEditCMPTRow.getItems().get(rowIndex);
        grossAmt = Double.parseDouble(cmpTRowDTO.getGross_amount());

        totalGrossAmt = totalGrossAmt + grossAmt;
        System.out.println("totalGrossAmt" + totalGrossAmt);
    }

    //function for get List of salesman
    public void getOutletSalesmanMasterRecord() {
        soToscLogger.info("starting of salesman master list API");
        try {
            HttpResponse<String> response = APIClient.getRequest("get_outlet_salesman_master");
            JsonObject jsonObject = new Gson().fromJson(response.body(), JsonObject.class);

            if (jsonObject.get("responseStatus").getAsInt() == 200) {
                JsonArray responseArray = jsonObject.get("responseObject").getAsJsonArray();
                // Create an ObservableList to store items for the combo box
                ObservableList<SalesmanMasterDTO> salesmanList = FXCollections.observableArrayList();

                // Assuming the JSON array contains the required information to create a SalesmanMasterDTO instance
                for (JsonElement element : responseArray) {
                    // Extract information from the JSON element
                    JsonObject salesmanJson = element.getAsJsonObject();
                    int id = salesmanJson.get("id").getAsInt();
                    String firstName = salesmanJson.get("firstName").getAsString();
                    // Repeat for other fields

                    // Create a new SalesmanMasterDTO instance with the extracted information
                    SalesmanMasterDTO salesmanDTO = new SalesmanMasterDTO(id, firstName, "", "", "", "", "", "");
                    salesmanList.add(salesmanDTO);
                }
                // Set the items of the combo box
                dpSCConvSalesman.setItems(salesmanList);
                dpSCConvSalesman.setConverter(new StringConverter<SalesmanMasterDTO>() {
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
            soToscLogger.error("error in getting saleman " + e.getMessage());
        }
    }

    //function for get indirect expenses list
    public void getIndirectExpenseList() {
        try {
            HttpResponse<String> response = APIClient.getRequest("get_indirect_expenses");
            JsonObject responseBody = new Gson().fromJson(response.body(), JsonObject.class);
            JsonArray data = responseBody.get("list").getAsJsonArray();

            if (responseBody.get("responseStatus").getAsInt() == 200) {
                System.out.println(data);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //function for calculate the amount of all row
    private void calculateEachRowTraxTotal() {

        Double totalGrossAmt = 0.0, grossAmt = 0.0;
        Double totaltaxableAmt = 0.0, taxableAmt = 0.0;
        Double totalDisAmt = 0.0, disAmt = 0.0;
        Double totalTaxAmt = 0.0, taxAmt = 0.0;
        Double totalNetAmt = 0.0, netAmount = 0.0;
        Double totalQuantity = 0.0, rowQty = 0.0;
        Double totalFreeQty = 0.0, freeQty = 0.0;

        for (CmpTRowDTOSoToSc cmpTRowDTO : tvSCConvEditCMPTRow.getItems()) {
            //todo: Net amount
            if (cmpTRowDTO.getNet_amount() != null) {

                netAmount = parseDouble(cmpTRowDTO.getNet_amount());
                totalNetAmt += netAmount;
            } else {
                totalNetAmt = 0.0;
            }
            //todo: Gross amount
            if (cmpTRowDTO.getGross_amount() != null) {
                grossAmt = parseDouble(cmpTRowDTO.getGross_amount());
                totalGrossAmt += grossAmt;
            } else {
                totalGrossAmt = 0.0;
            }
            //todo: taxable amount
            if (cmpTRowDTO.getTaxable_amt() != null) {
                taxableAmt = Double.valueOf(cmpTRowDTO.getTaxable_amt());
                totaltaxableAmt += taxableAmt;
            } else {
                totaltaxableAmt = 0.0;
            }
            System.out.println("total amount : "+totaltaxableAmt);
            //todo: discount amount
            if (cmpTRowDTO.getRow_dis_amt() != null && !cmpTRowDTO.getRow_dis_amt().isEmpty()) {
                disAmt = Double.valueOf(cmpTRowDTO.getRow_dis_amt());
                totalDisAmt += disAmt;
            } else {
                totalDisAmt = 0.0;
            }
            System.out.println("total discount amount : "+totalDisAmt);
            //todo: Tax amount
            if (cmpTRowDTO.getTotal_igst() != null) {
                taxAmt = Double.valueOf(cmpTRowDTO.getTotal_igst());
                totalTaxAmt += taxAmt;
            } else {
                totalTaxAmt = 0.0;
            }
            //for total qty of all rows
            if (cmpTRowDTO.getQuantity() != null) {
                rowQty = Double.valueOf(cmpTRowDTO.getQuantity());
                totalQuantity += rowQty;
            } else {
                totalQuantity = 0.0;
            }
            //for free qty of total rows
            if (cmpTRowDTO.getFree() != null && !cmpTRowDTO.getFree().isEmpty()) {
                freeQty = Double.valueOf(cmpTRowDTO.getFree());
                totalFreeQty += freeQty;
            } else {
                totalFreeQty = 0.0;
            }

        }

        //Display  of gross amt ,taxable ,tax,discount
        lblSCConvBillAmountEdit.setText(String.format("%.2f", totalNetAmt));

        lblSCConvGrossTotalEdit.setText(String.format("%.2f", totalGrossAmt));

        lblSCConvTotalDiscountEdit.setText(String.format("%.2f", totalDisAmt));

        lblSCConvTotalTaxEdit.setText(String.format("%.2f", totalTaxAmt));

        lblSCConvTotalTaxableAmountEdit.setText(String.format("%.2f", (totalGrossAmt - totalDisAmt)));
        lblSCConvTotalQtyEdit.setText(String.format("%.2f", totalQuantity));
        lblSCConvFreeQtyEdit.setText(String.format("%.2f", totalFreeQty));

    }

    //function for display the supplier list at bottom
//    private void getSupplierListbyProductId(String id) {   //call in initialize()
//        Map<String, String> map = new HashMap<>();
//        map.put("productId", id);
//        String formData = Globals.mapToStringforFormData(map);
//        System.out.println("FormData: " + formData);
//        HttpResponse<String> response = APIClient.postFormDataRequest(formData, EndPoints.GET_SUPPLIERLIST_BY_PRODUCTID_ENDPOINT);
//        String responseBody = response.body();
//        JsonObject jsonObject = new Gson().fromJson(responseBody, JsonObject.class);
//        System.out.println("Supplier List Res ==>" + jsonObject);
//        if (jsonObject.get("responseStatus").getAsInt() == 200) {
//            JsonArray dataArray = jsonObject.getAsJsonArray("data");
//            //todo: getting values
//            //            ObservableList<SalesOrderSupplierDetailsDTO> supplierDataList = FXCollections.observableArrayList();
//            for (JsonElement element : dataArray) {
//                JsonObject item = element.getAsJsonObject();
//                String supplierName = item.get("supplier_name").getAsString();
//                String supplierInvNo = item.get("invoice_no").getAsString();
//                String supplierInvDate = item.get("invoice_date").getAsString();
//                String supplierBatch = item.get("batch").getAsString();
//                String supplierMrp = item.get("mrp").getAsString();
//                String supplierQty = item.get("rate").getAsString();
//                String supplierRate = item.get("quantity").getAsString();
//                String supplierCost = item.get("cost").getAsString();
//                String supplierDisPer = item.get("dis_per").getAsString();
//                String supplierDisAmt = item.get("dis_amt").getAsString();
//
//                supplierDataList.add(new SalesOrderSupplierDetailsDTO(supplierName, supplierInvNo, supplierInvDate, supplierBatch, supplierMrp, supplierRate, supplierQty, supplierCost, supplierDisPer, supplierDisAmt));
//            }
//            tcSCConvEditSupplierName.setCellValueFactory(new PropertyValueFactory<>("supplierName"));
//            tcSCConvEditSupplierInvNo.setCellValueFactory(new PropertyValueFactory<>("supplierInvNo"));
//            tcSCConvEditSupplierInvDate.setCellValueFactory(new PropertyValueFactory<>("supplierInvDate"));
//            tcSCConvEditSupplierBatch.setCellValueFactory(new PropertyValueFactory<>("supplierBatch"));
//            tcSCConvEditSupplierMrp.setCellValueFactory(new PropertyValueFactory<>("supplierMrp"));
//            tcSCConvEditSupplierQty.setCellValueFactory(new PropertyValueFactory<>("supplierQty"));
//            tcSCConvEditSupplierRate.setCellValueFactory(new PropertyValueFactory<>("supplierRate"));
//            tcSCConvEditSupplierCost.setCellValueFactory(new PropertyValueFactory<>("supplierCost"));
//            tcSCConvEditSupplierDisper.setCellValueFactory(new PropertyValueFactory<>("supplierDisPer"));
//            tcSCConvEditSupplierDisRs.setCellValueFactory(new PropertyValueFactory<>("supplierDisAmt"));
//
//            tvSCConvSupplierDetails.setItems(supplierDataList);
//
//        }
//    }

    //function for create the sale challan
    public void createSalesChallan() {
//      LocalDate chDate = LocalDate.parse(dpSCConvChallanDate.getText());
//      String formatedChallanDate = chDate.format(DateTimeFormatter.ofPattern(""));
        System.out.println("--->>> "+Communicator.text_to_date.fromString(dpSCConvChallanDate.getText()));
        LocalDate ChallanDate =Communicator.text_to_date.fromString(dpSCConvChallanDate.getText());
        Double cgst = 0.00;
        Double totalCgst = 0.00;
        Double sgst = 0.00;
        Double totalSgst = 0.00;
        Double totalIgst = 0.00;
        for (GstDTO gstdto : tblvSCConvGSTEdit.getItems()) {
            cgst = Double.valueOf(gstdto.getCgst());
            totalCgst += cgst;
            sgst = Double.valueOf(gstdto.getCgst());
            totalSgst += sgst;
            totalIgst = totalSgst + totalCgst;
        }

        Map<String, String> map = new HashMap<>();

        map.put("bill_dt", String.valueOf(ChallanDate));
        map.put("bill_no", tfSCConvInvNo.getText());
        map.put("sales_acc_id", cmbSCConvSalesAC.getValue().getId());
        map.put("sales_sr_no", tfSCConvChallanSerial.getText());
        map.put("debtors_id", Debtor_id);
        map.put("gstNo", cmbSCConvSupplierGST.getValue() != null ? cmbSCConvSupplierGST.getValue().toString() : "");
        map.put("isRoundOffCheck", String.valueOf(chbRoundOffEdit.isSelected()));
        map.put("roundoff", lblSCConvRoundOffEdit.getText() != null && !lblSCConvRoundOffEdit.getText().isEmpty() ? lblSCConvRoundOffEdit.getText() : String.valueOf(0.0));
        map.put("narration", tfSCConvEditNarrations.getText() != null ? tfSCConvEditNarrations.getText() : "");
        map.put("totalamt", lblSCConvBillAmountEdit.getText());
        map.put("taxable_amount", lblSCConvTotalTaxableAmountEdit.getText());
        map.put("tcs", "0");//static
        map.put("sales_discount", "0");//static
        map.put("total_sales_discount_amt", "0");//static
        map.put("sales_discount_amt", "0");//static
        map.put("additionalCharges", "[]");
        map.put("additionalChargesTotal", "0");//static
        map.put("sale_type", "sales_challan");//static
//        map.put("taxCalculation", "12");//static
//        map.put("taxFlag", "false");// static data as of now
        map.put("total_qty", String.valueOf(Double.valueOf(lblSCConvTotalQtyEdit.getText()).longValue()));
        System.out.println("total qty " + lblSCConvTotalQtyEdit.getText() + "   free qty " + lblSCConvFreeQtyEdit.getText());
        map.put("total_free_qty", lblSCConvFreeQtyEdit.getText());//static
        map.put("total_row_gross_amt", lblSCConvGrossTotalEdit.getText());
        map.put("total_base_amt", lblSCConvGrossTotalEdit.getText());
        map.put("total_invoice_dis_amt", lblSCConvTotalDiscountEdit.getText());
        map.put("total_tax_amt", lblSCConvTotalTaxEdit.getText());
        map.put("bill_amount", lblSCConvBillAmountEdit.getText());
//        map.put("totalcgst", String.valueOf(totalCgst));
//        map.put("totalsgst", String.valueOf(totalSgst));
//        map.put("totaligst", String.valueOf(totalIgst));
        map.put("reference_sq_id", QuotToChallanId);
        map.put("reference", "SLSQTN");


        JsonArray jsonArray = new JsonArray();
        JsonObject jsonObject = new JsonObject();
        JsonObject jsonObject1 = new JsonObject();
        double taxPer = 0.0;
        for (CmpTRowDTOSoToSc CmpTRowDTOSoToSc : tvSCConvEditCMPTRow.getItems()) {
            System.out.println("SalesQuottoChallanDToTaxPer " + CmpTRowDTOSoToSc.getTax());
            taxPer = CmpTRowDTOSoToSc.getTax() != null && CmpTRowDTOSoToSc.getTax() != "" ? Double.parseDouble(CmpTRowDTOSoToSc.getTax()) : 0.0;
            System.out.println("taxPer----- " + taxPer);
        }

        if (CompanyStateCode.equals(stateCode)) {
            map.put("taxFlag", "true");// static data as of now
//            jsonObject.addProperty("d_gst", decimalFormat.format(taxPer));
            jsonObject.addProperty("gst", decimalFormat.format((taxPer) / 2));
            jsonObject.addProperty("amt", decimalFormat.format(Double.parseDouble(lblSCConvTotalTaxEdit.getText()) / 2));
            jsonArray.add(jsonObject);

            jsonObject1.add("cgst", jsonArray);
            jsonObject1.add("sgst", jsonArray);
            map.put("taxCalculation", jsonObject1.toString());
            map.put("totalcgst", decimalFormat.format(Double.parseDouble(lblSCConvTotalTaxEdit.getText()) / 2));
            map.put("totalsgst", decimalFormat.format(Double.parseDouble(lblSCConvTotalTaxEdit.getText()) / 2));
            map.put("totaligst", "0.0");
            System.out.println("taxCalculation_cgst_sgst" + jsonObject1.toString());
        } else {
            map.put("taxFlag", "false");// static data as of now
//            jsonObject.addProperty("d_gst", decimalFormat.format(taxPer));
            jsonObject.addProperty("gst", decimalFormat.format(taxPer));
            jsonObject.addProperty("amt", decimalFormat.format(Double.parseDouble(lblSCConvTotalTaxEdit.getText())));
            jsonArray.add(jsonObject);
            jsonObject1.add("igst", jsonArray);
            map.put("taxCalculation", jsonObject1.toString());
            map.put("totalcgst", "0.0");
            map.put("totalsgst", "0.0");
            map.put("totaligst", decimalFormat.format(Double.parseDouble(lblSCConvTotalTaxEdit.getText())));
            System.out.println("taxCalculation_igst" + jsonObject1.toString());
        }
//            jsonArray.add(jsonObject);
        System.out.println("taxCalculation>>> " + jsonObject1);

        //new2.0 start
        List<CmpTRowDTOSoToSc> currentItems = new ArrayList<>(tvSCConvEditCMPTRow.getItems());

        if (!currentItems.isEmpty()) {
            CmpTRowDTOSoToSc lastItem = currentItems.get(currentItems.size() - 1);

            if (lastItem.getParticulars() != null && lastItem.getParticulars().isEmpty()) {
                currentItems.remove(currentItems.size() - 1);
            }
        }

        List<CmpTRowDTOSoToSc> list = new ArrayList<>(currentItems);
        //new2.0 end

        /***** mapping Row Data into TranxPurRowDTO *****/
        ArrayList<TranxPurRowDTO> rowData = new ArrayList<>();
        for (CmpTRowDTOSoToSc cmpTRowDTO : list) { //new2.0
            TranxPurRowDTO purParticularRow = new TranxPurRowDTO();
            System.out.println("=====>>>> " + cmpTRowDTO.getBatch_or_serial());
            purParticularRow.setProductId(cmpTRowDTO.getProduct_id());
            purParticularRow.setReferenceId(cmpTRowDTO.getReference_id());
            purParticularRow.setReferenceType(cmpTRowDTO.getReference_type());
            if (!cmpTRowDTO.getBatch_or_serial().isEmpty() && cmpTRowDTO.getBatch_or_serial() != "0") {
                purParticularRow.setBatch(cmpTRowDTO.getBatch_or_serial());
            } else {
                purParticularRow.setBatch("");
            }
            if (!cmpTRowDTO.getLevelA_id().isEmpty()) {
                purParticularRow.setLevelaId(cmpTRowDTO.getLevelA_id());
            } else {
                purParticularRow.setLevelaId("");
            }
            if (!cmpTRowDTO.getLevelB_id().isEmpty()) {
                purParticularRow.setLevelbId(cmpTRowDTO.getLevelB_id());
            } else {
                purParticularRow.setLevelbId("");
            }
            if (!cmpTRowDTO.getLevelC_id().isEmpty()) {
                purParticularRow.setLevelcId(cmpTRowDTO.getLevelC_id());
            } else {
                purParticularRow.setLevelcId("");
            }
            if (!cmpTRowDTO.getB_details_id().isEmpty()) {
                purParticularRow.setbDetailsId(cmpTRowDTO.getB_details_id());
            } else {
                purParticularRow.setbDetailsId("");
            }
            if (!cmpTRowDTO.getB_no().isEmpty()) {
                purParticularRow.setbNo(cmpTRowDTO.getB_no());
            } else {
                purParticularRow.setbNo("");
            }
            if (!cmpTRowDTO.getIs_batch().isEmpty()) {
                purParticularRow.setIsBatch(cmpTRowDTO.getIs_batch());
            } else {
                purParticularRow.setIsBatch("");
            }
            if (!cmpTRowDTO.getUnit().isEmpty()) {
                purParticularRow.setUnitId("1");
            } else {
                purParticularRow.setUnitId("");
            }
            if (cmpTRowDTO.getUnit_conv() != null) {
                purParticularRow.setUnitConv(String.valueOf(cmpTRowDTO.getUnit_conv()));
            } else {
                purParticularRow.setUnitConv("");
            }
            if (!cmpTRowDTO.getQuantity().isEmpty()) {
                purParticularRow.setQty(cmpTRowDTO.getQuantity());
            } else {
                purParticularRow.setQty("");
            }
            if (!cmpTRowDTO.getFree().isEmpty()) {
                purParticularRow.setFreeQty(cmpTRowDTO.getFree());
            } else {
                purParticularRow.setFreeQty("");
            }
            if (!cmpTRowDTO.getRate().isEmpty()) {
                purParticularRow.setRate(cmpTRowDTO.getRate());
            } else {
                purParticularRow.setRate("");
            }
            if (cmpTRowDTO.getBase_amt() != null) {
                purParticularRow.setBaseAmt(String.valueOf(cmpTRowDTO.getBase_amt()));
            } else {
                purParticularRow.setBaseAmt("");
            }
            if (!cmpTRowDTO.getDis_amt().isEmpty()) {
                purParticularRow.setDisAmt(cmpTRowDTO.getDis_amt());
            } else {
                purParticularRow.setDisAmt("");
            }
            if (!cmpTRowDTO.getDis_per().isEmpty()) {
                purParticularRow.setDisPer(cmpTRowDTO.getDis_per());
            } else {
                purParticularRow.setDisPer("");
            }
            if (!cmpTRowDTO.getDis_per().isEmpty()) {
                purParticularRow.setDisPer2("");
            } else {
                purParticularRow.setDisPer2("");
            }
            if (cmpTRowDTO.getDis_per_cal() != null) {
                purParticularRow.setDisPerCal(String.valueOf(cmpTRowDTO.getDis_per_cal()));
            } else {
                purParticularRow.setDisPerCal("");
            }
            if (cmpTRowDTO.getDis_amt_cal() != null) {
                purParticularRow.setDisAmtCal(String.valueOf(cmpTRowDTO.getDis_amt_cal()));
            } else {
                purParticularRow.setDisAmtCal("");
            }
            if (cmpTRowDTO.getRow_dis_amt() != null) {
                purParticularRow.setRowDisAmt(String.valueOf(cmpTRowDTO.getRow_dis_amt()));
            } else {
                purParticularRow.setRowDisAmt("");
            }
            if (cmpTRowDTO.getGross_amount() != null) {
                purParticularRow.setGrossAmt(String.valueOf(cmpTRowDTO.getGross_amount()));
            } else {
                purParticularRow.setGrossAmt("");
            }
            if (cmpTRowDTO.getAdd_chg_amt() != null) {
                purParticularRow.setAddChgAmt(String.valueOf(cmpTRowDTO.getAdd_chg_amt()));
            } else {
                purParticularRow.setAddChgAmt("");
            }
            if (cmpTRowDTO.getGross_amount1() != null) {
                purParticularRow.setGrossAmt(String.valueOf(cmpTRowDTO.getGross_amount1()));
            } else {
                purParticularRow.setGrossAmt("");
            }
            if (cmpTRowDTO.getInvoice_dis_amt() != null) {
                purParticularRow.setInvoiceDisAmt(String.valueOf(cmpTRowDTO.getInvoice_dis_amt()));
            } else {
                purParticularRow.setInvoiceDisAmt("");
            }
            if (cmpTRowDTO.getTotal_amt() != null) {
                purParticularRow.setTotalAmt(String.valueOf(cmpTRowDTO.getTotal_amt()));
            } else {
                purParticularRow.setTotalAmt("");
            }
            if (cmpTRowDTO.getGst() != null) {
                purParticularRow.setGst(String.valueOf(cmpTRowDTO.getGst()));
            } else {
                purParticularRow.setGst("");
            }
            if (cmpTRowDTO.getCgst() != null) {
                purParticularRow.setCgst(String.valueOf(cmpTRowDTO.getCgst()));
            } else {
                purParticularRow.setCgst("");
            }
            if (cmpTRowDTO.getIgst() != null) {
                purParticularRow.setIgst(String.valueOf(cmpTRowDTO.getIgst()));
            } else {
                purParticularRow.setIgst("");
            }
            if (cmpTRowDTO.getSgst() != null) {
                purParticularRow.setSgst(String.valueOf(cmpTRowDTO.getSgst()));
            } else {
                purParticularRow.setSgst("");
            }
            System.out.println("total igsttt " + cmpTRowDTO.getTotal_igst());
            if (cmpTRowDTO.getTotal_igst() != null) {
                purParticularRow.setTotalIgst(String.valueOf(cmpTRowDTO.getTotal_igst()));
            } else {
                purParticularRow.setTotalIgst("");
            }
            if (cmpTRowDTO.getTotal_cgst() != null) {
                purParticularRow.setTotalCgst(String.valueOf(cmpTRowDTO.getTotal_cgst()));
            } else {
                purParticularRow.setTotalCgst("");
            }
            if (cmpTRowDTO.getTotal_sgst() != null) {
                purParticularRow.setTotalSgst(String.valueOf(cmpTRowDTO.getTotal_sgst()));
            } else {
                purParticularRow.setTotalSgst("");
            }
            if (cmpTRowDTO.getFinal_amount() != null) {
                purParticularRow.setFinalAmt(String.valueOf(cmpTRowDTO.getFinal_amount()));
            } else {
                purParticularRow.setSgst("");
            }
            if (cmpTRowDTO.getSgst() != null) {
                purParticularRow.setSgst(String.valueOf(cmpTRowDTO.getSgst()));
            } else {
                purParticularRow.setSgst("");
            }
            if (cmpTRowDTO.getReference_id() != null && !cmpTRowDTO.getReference_id().isEmpty()) {
                purParticularRow.setReferenceId(cmpTRowDTO.getReference_id());
            } else {
                purParticularRow.setReferenceId("");
            }
            if (cmpTRowDTO.getReference_type() != null && !cmpTRowDTO.getReference_type().isEmpty()) {
                purParticularRow.setReferenceType(cmpTRowDTO.getReference_type());
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

        HttpResponse<String> response;
        String formData = Globals.mapToStringforFormData(map);
        System.out.println("formData" + formData);
        response = APIClient.postFormDataRequest(formData, EndPoints.SALES_CHALLAN_CREATE_ENDPOINT);

        JsonObject responseBody = new Gson().fromJson(response.body(), JsonObject.class);
        System.out.println("Response=>" + responseBody);
        String message = responseBody.get("message").getAsString();

        if (responseBody.get("responseStatus").getAsInt() == 200) {
            System.out.println("responseBody " + responseBody);
            AlertUtility.CustomCallback callback = (number) -> {
                if (number == 1) {
                    GlobalController.getInstance().addTabStatic(SALES_CHALLAN_LIST_SLUG, false);
                }
            };
            Stage stage = (Stage) rootAnchorPaneSoToSc.getScene().getWindow();
            if (responseBody.get("responseStatus").getAsInt() == 200) {
                AlertUtility.AlertSuccessTimeout("Success", responseBody.get("message").getAsString(), callback);
            }

        } else {
            AlertUtility.AlertErrorTimeout("Error", responseBody.get("message").getAsString(), input -> {
            });
            soToscLogger.error("response status is other than 200");
        }

    }

    private void getSalesQuotationWithId() {
        try {
//            System.out.println("input in getSaleOrderWithId " + input);
            Map<String, String> body = new HashMap<>();
            body.put("sale_quotation_ids", input);
            String requestBody = Globals.mapToStringforFormData(body);
            HttpResponse<String> response = APIClient.postFormDataRequest(requestBody, "get_sale_quotation_with_ids");
            JsonObject resp = new Gson().fromJson(response.body(), JsonObject.class);
            System.out.println("resp1234 " + resp);
            System.out.println("RESPONSE OF BY ID " + resp);
            SalesQuotToChallanResponseDTO responseBody = new Gson().fromJson(response.body(), SalesQuotToChallanResponseDTO.class);
//            System.out.println("jsonObject11111 " + responseBody.getInvoiceData().getInvoiceDt());
            if (responseBody.getResponseStatus() == 200) {
                setConversionData(responseBody);
            } else {
                soToscLogger.error("responesStatus " + responseBody.getResponseStatus() + "  error occured");
            }

        } catch (Exception e) {
            System.out.println("Exception----." + e.getMessage());
            soToscLogger.error("error in getting the data " + e.getMessage());
        }

    }

    private void setConversionData(SalesQuotToChallanResponseDTO formData) {
        LocalDate challDate = LocalDate.parse(formData.getInvoiceData().getInvoiceDt());
        dpSCConvChallanDate.setText(challDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        System.out.println("dpChallanDateee " + dpSCConvChallanDate.getText());
        tfSCConvLedgNameEdit.setText(formData.getInvoiceData().getDebtorsName());
//        cmbSCConvSupplierGST.setValue(formData.getInvoiceData().getGstNo());
        tfSCConvEditNarrations.setText(formData.getInvoiceData().getNarration() != null ? formData.getInvoiceData().getNarration().toString() : "");
//        lblSCConvRoundOffEdit.setText(String.valueOf(formData.getInvoiceData().getRoundoff()));   roundoff is not in sale order so while conversion roundoff is not come in data
        Debtor_id = String.valueOf(formData.getInvoiceData().getDebtorsId());
        ledgerStateCode = formData.getInvoiceData().getLedgerStateCode();
        System.out.println("getLedgerStateCode ---- " + ledgerStateCode);
        fetchSelectedLedgerData(Debtor_id);
        System.out.println("Debtor_id==>> " + Debtor_id);
        chbRoundOffEdit.setSelected(true);    //round off is not in sale order so make default checked
//        Debtor_id = String.valueOf(formData)


        if (formData.getDiscountInAmt() != null) {
            Platform.runLater(() -> {
                tfSCConvAllRowDisAmtEdit.setText(String.valueOf(formData.getDiscountInAmt()));
            });
        }

        int index = 0;
        double grossTotal = 0.0;     //for total bottom calculation
        double discountAmount = 0.0;
        double totalAmount = 0.0;
        double totalTax = 0.0;
        double billAmount = 0.0;
        int totalQty = 0;
        double totalFreeQty = 0.0;
        tvSCConvEditCMPTRow.getItems().clear();


        PurInvoiceCommunicator.levelAForPurInvoiceObservableList.clear();
        for (SalesQuotToChallanRowMainDTO mRow : formData.getRow()) {
//            CmpTRowDTOSoToSc row = new CmpTRowDTOSoToSc();
            CmpTRowDTOSoToSc row = new CmpTRowDTOSoToSc("", "0", "1", "", "", "", "", "", "", "","", "", "", "", "", "", "", "", "");
            System.out.println("productNamee " + mRow.getProductName());
            System.out.println("product id on data set" + mRow.getProductId());
            productId = String.valueOf(mRow.getProductId());
            SingleInputDialogs.productIdSelected = productId;
            //Pass productId to ProductPopup for Focus in open ProductPopup in Edit sales Quot
            if (productId != "") {
                SingleInputDialogs.productId = productId;
            } else {
                SingleInputDialogs.productId = "";
            }
            getSupplierListbyProductId(String.valueOf(mRow.getProductId()));
            fetchSelectedProductData(String.valueOf(mRow.getProductId()));
            TranxSelectedProduct selectedProduct = TranxCommonPopUps.getSelectedProductFromProductId(mRow.getProductName());//new
            row.setParticulars(mRow.getProductName());
            row.setSelectedProduct(selectedProduct);//new

            //!changed
            System.out.println("setUnit " + mRow.getUnitName() +  " setUnit_id "+ mRow.getUnitId() + " setUnit_conv " + mRow.getUnitConv());
            row.setUnit(mRow.getUnitName().toString());//new
            row.setUnit_id(mRow.getUnitId().toString());
            row.setUnit_conv(mRow.getUnitConv().toString());
            row.setPackages(mRow.getPackName());
            row.setQuantity(String.valueOf(mRow.getQty()));
            row.setBatch_or_serial("0");
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
//            row.setLevelA(mRow.getLevelA());
//            row.setLevelB(mRow.getLevelB());
//            row.setLevelC(mRow.getLevelC());
            row.setLevelA_id(mRow.getLevelAId());
            row.setLevelB_id(mRow.getLevelBId());
            row.setLevelC_id(mRow.getLevelCId());

            System.out.println("refId " + mRow.getReferenceId() + " refType " + mRow.getReferenceType());

            row.setDetails_id(String.valueOf(mRow.getDetailsId()));      //this details id is for product row not for batch...
            row.setIs_batch(String.valueOf(true));
//            row.setBatchNo(mRow.getBatchNo());     // we dont set batch no intially
            row.setFinal_amount(String.valueOf(mRow.getFinalAmt()));
            row.setReference_id(String.valueOf(mRow.getReferenceId()));
            row.setReference_type(mRow.getReferenceType());

            List<LevelAForPurInvoice> levelAForPurInvoiceList = ProductUnitsPackingQuoToChal.getAllProductUnitsPackingFlavour(String.valueOf(mRow.getProductId()));
//            System.out.println("levelAForPurInvoiceList >>> " + levelAForPurInvoiceList);
            ObservableList<LevelAForPurInvoice> observableLevelAList = FXCollections.observableArrayList(levelAForPurInvoiceList);
//            System.out.println("observableLevelAList >>> " + observableLevelAList.getFirst());
            System.out.println("Index >> Before : " + index);
            if (index >= 0 && index < PurInvoiceCommunicator.levelAForPurInvoiceObservableList.size()) {
                System.out.println("Index If >> : " + index);
                PurInvoiceCommunicator.levelAForPurInvoiceObservableList.set(index, observableLevelAList);
                tvSCConvEditCMPTRow.getItems().get(index).setLevelA(null);
                tvSCConvEditCMPTRow.getItems().get(index).setLevelA(levelAForPurInvoiceList.get(0).getLabel());

                for (LevelAForPurInvoice levelAForPurInvoice : PurInvoiceCommunicator.levelAForPurInvoiceObservableList.get(index)) {
                    if (tvSCConvEditCMPTRow.getItems().get(index).getLevelA_id().equals(levelAForPurInvoice.getValue())) {
                        tvSCConvEditCMPTRow.getItems().get(index).setLevelA(null);
                        tvSCConvEditCMPTRow.getItems().get(index).setLevelA(levelAForPurInvoice.getLabel());
                    }
                }

            } else {
                System.out.println("Index else >> : " + index);
                PurInvoiceCommunicator.levelAForPurInvoiceObservableList.add(observableLevelAList);
                System.out.println("observableLevelAList >>>>>> " + PurInvoiceCommunicator.levelAForPurInvoiceObservableList);
//                tvSCConvEditCMPTRow.getItems().get(index).setLevelA("");
            }


            if (mRow.getBaseAmt() != null) {
                grossTotal = grossTotal + mRow.getBaseAmt();
            } else {
                grossTotal = 0.0;
            }
            discountAmount = Double.parseDouble(String.valueOf(discountAmount + mRow.getDisAmt()));
            totalAmount = totalAmount + mRow.getTotalAmt();
            totalTax = totalTax + mRow.getTotalIgst();
            billAmount = billAmount + mRow.getFinalAmt();
            totalQty = (int) (totalQty + mRow.getQty());
            totalFreeQty = totalFreeQty + Double.parseDouble(mRow.getFreeQty());
            System.out.println("getReferenceTypeuu " + mRow.getReferenceType() + "  refId " + mRow.getReferenceId());
            tvSCConvEditCMPTRow.getItems().add(row);
            index++;
        }
        System.out.println("totalQty= " + totalQty + " totalFreeQty= " + totalFreeQty);
        lblSCConvGrossTotalEdit.setText(String.valueOf(grossTotal));
        lblSCConvTotalDiscountEdit.setText(String.valueOf(discountAmount));
        lblSCConvTotalTaxableAmountEdit.setText(String.valueOf(totalAmount));
        lblSCConvTotalTaxEdit.setText(String.valueOf(totalTax));
        lblSCConvBillAmountEdit.setText(String.valueOf(billAmount));

        lblSCConvTotalQtyEdit.setText(String.valueOf(totalQty));
        lblSCConvFreeQtyEdit.setText(String.valueOf(totalFreeQty));
        tvSCConvEditCMPTRow.refresh();

    }


    TableCellCallback<Object[]> callback = item -> {

        System.out.println("item1: " + item[0]);
        System.out.println("item2: " + item[1]);
        System.out.println("item3: " + item[2]);
        System.out.println("item4: " + item[3]);
        System.out.println("item5: " + item[4]);
        System.out.println("item6: " + item[5]);
        System.out.println("item7: " + item[6]);
        System.out.println("item8: " + item[7]);
        System.out.println("item9: " + item[8]);
        System.out.println("taxCalculation " + item[9]);

        tblvSCConvGSTEdit.getItems().clear();
        ObservableList<GstDTO> gstDTOObservableList = FXCollections.observableArrayList();
        ObservableList<?> item9List = (ObservableList<?>) item[9];
        for (Object obj : item9List) {
            if (obj instanceof GstDTO) {
                gstDTOObservableList.add((GstDTO) obj);
            }
        }

        tblvSCConvGSTEdit.getItems().addAll(gstDTOObservableList);

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

        lblSCConvBillAmountEdit.setText((String) item[2]); //totalNetAmt for bill amount
        totalamt = (String) item[2];
//        lblSCConvTotalQtyEdit.setText((String) item[0]);

        total_purchase_discount_amt = (String) item[4];
//        if (ledgerStateCode.equalsIgnoreCase(GlobalTranx.getCompanyStateCode().toString())) {
//            taxFlag = true;
//        }

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


//        purchase_invoice_map.put("taxFlag", String.valueOf(taxFlag));

        total_taxable_amt = Double.parseDouble((String) item[5]);

        lblSCConvFreeQtyEdit.setText((String) item[1]);
        lblSCConvTotalQtyEdit.setText((String) item[0]);
        lblSCConvGrossTotalEdit.setText((String) item[3]);
        lblSCConvTotalDiscountEdit.setText((String) item[4]);
        lblSCConvTotalTaxableAmountEdit.setText((String) item[5]);
        lblSCConvTotalTaxEdit.setText((String) item[6]);


        /**
         * Info : RoundOff Calculation Method
         * @Author : Vinit Chilaka
         */
        chbRoundOffEdit.setSelected(true);
        roundOffCalculations();

    };

    TableCellCallback<Object[]> product_callback = item -> {
        if (item.length == 1) {
//            tranxProductDetailsFun((String) item[0]);
            System.out.println("product Callback Product Id " +  item[0]);
            getSupplierListbyProductId((String) item[0]);
        } else {
            System.out.println("product Callback batchNo " + item[0] + "b_details_id " + item[1]);
            tranxBatchDetailsFun((String) item[0], (String) item[1]);
        }
    };

    //new start
    TableCellCallback<Integer> unit_callback = currentIndex -> {
        System.out.println("i am in" + currentIndex);
        System.out.println("i am in ledgerStateCode" + ledgerStateCode);
        CmpTRowDTOSoToSc tranxRow = tvSCConvEditCMPTRow.getItems().get(currentIndex);
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

    private void roundOffCalculations() {
        Double billamt = 0.00; // local variable to store row wise  Bill amount
        Double total_bill_amt = 0.00; // local variable to store Final bill Amount without roundoff
        Double final_r_off_bill_amt = 0.00; // local variable to store Final Bill amount with round off
        Double roundOffAmt = 0.00; // local variable to store Roundoff Amount
        for (CmpTRowDTOSoToSc cmpTRowDTO : tvSCConvEditCMPTRow.getItems()) {
            billamt = Double.valueOf(cmpTRowDTO.getNet_amount());
            total_bill_amt += billamt;
        }
        final_r_off_bill_amt = (double) Math.round(total_bill_amt);
        roundOffAmt = final_r_off_bill_amt - total_bill_amt;

        if (chbRoundOffEdit.isSelected()) {
            lblSCConvBillAmountEdit.setText(String.format("%.2f", final_r_off_bill_amt));
            chbRoundOffEdit.setText(String.format("%.2f", roundOffAmt));
        } else {
            lblSCConvBillAmountEdit.setText(String.format("%.2f", total_bill_amt));
            chbRoundOffEdit.setText("0.00");
        }

    }


//    TableCellCallback<Integer> productCall = item -> {
////        if (item.length == 1) {
//            System.out.println("product id " + item);
//            fetchSelectedProductData(String.valueOf(item));
//            getSupplierListbyProductId(String.valueOf(item));
////        }
////        else {
//////            tranxBatchDetailsFun((String) item[0], (String) item[1]);
////        }
//    };


    public void tableInitiliazation() {

        tvSCConvEditCMPTRow.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tvSCConvEditCMPTRow.setEditable(true);
        tvSCConvEditCMPTRow.setFocusTraversable(false);

        Label headerLabel = new Label("Sr\nNo.");
        tblcSCConvEditCmptSrNo.setGraphic(headerLabel);

//        tvSCConvEditCMPTRow.getItems().addAll(new CmpTRowDTOSoToSc("", "0", "1", "", "", "", "", "", "", "", "", "", "", "", "", "", "", ""));

//        tblcSCConvEditCmptSrNo.setCellValueFactory(cellData -> cellData.getValue().sr_noProperty());
        tblcSCConvEditCmptSrNo.setStyle("-fx-alignment: CENTER;");

        tblcSCConvEditCmptPackage.setCellValueFactory(cellData -> cellData.getValue().packagesProperty());
        tblcSCConvEditCmptPackage.setStyle("-fx-alignment: CENTER;");

        tblcSCConvEditCmptLevelB.setCellValueFactory(cellData -> cellData.getValue().levelBProperty());
        tblcSCConvEditCmptLevelB.setCellFactory(column -> new ComboBoxTableCellForLevelBQuoToChal("tblcSCConvEditCmptLevelB"));

        tblcSCConvEditCmptLevelA.setCellValueFactory(cellData -> cellData.getValue().levelAProperty());
        tblcSCConvEditCmptLevelA.setCellFactory(column -> new ComboBoxTableCellForLevelAQuoToChal("tblcSCConvEditCmptLevelA"));

        tblcSCConvEditCmptLevelC.setCellValueFactory(cellData -> cellData.getValue().levelCProperty());
        tblcSCConvEditCmptLevelC.setCellFactory(column -> new ComboBoxTableCellForLevelCQuoToChal("tblcSCConvEditCmptLevelC"));

        tblcSCConvEditCmptUnit.setCellValueFactory(cellData -> cellData.getValue().unitProperty());
        tblcSCConvEditCmptUnit.setCellFactory(column -> new ComboBoxTableCellForUnitQuoToChal("tblcSCConvEditCmptUnit", unit_callback));//new

        tblcSCConvEditCmptParticular.setCellValueFactory(cellData -> cellData.getValue().particularsProperty());
        tblcSCConvEditCmptParticular.setCellFactory(column -> new TextFieldTableCellForSQuoToChall("tblcSCConvEditCmptParticular", callback, product_callback, tfSCConvEditNarrations));
//        rbCash.setOnKeyPressed(event -> {       //mode of payment is not available
//            tvSCConvEditCMPTRow.edit(0, tblcSCConvEditCmptParticular);
//        });
        tblcSCConvEditCmptBatch.setCellValueFactory(cellData -> cellData.getValue().batch_or_serialProperty());
        tblcSCConvEditCmptBatch.setCellFactory(column -> new TextFieldTableCellForSQuoToChall("tblcSCConvEditCmptBatch", callback, product_callback, tfSCConvEditNarrations));


        tblcSCConvEditCmptQuantity.setCellValueFactory(cellData -> cellData.getValue().quantityProperty());
        tblcSCConvEditCmptQuantity.setCellFactory(column -> new TextFieldTableCellForSQuoToChall("tblcSCConvEditCmptQuantity", callback));

        tblcSCConvEditCmptFreeQty.setCellValueFactory(cellData -> cellData.getValue().freeProperty());//new2.0
        tblcSCConvEditCmptFreeQty.setCellFactory(column -> new TextFieldTableCellForSQuoToChall("tblcSCConvEditCmptFreeQty", callback));//new2.0


        tblcSCConvEditCmptRate.setCellValueFactory(cellData -> cellData.getValue().rateProperty());
        tblcSCConvEditCmptRate.setCellFactory(column -> new TextFieldTableCellForSQuoToChall("tblcSCConvEditCmptRate", callback));

        tblcSCConvEditCmptGrossAmt.setCellValueFactory(cellData -> cellData.getValue().gross_amountProperty());
        tblcSCConvEditCmptGrossAmt.setCellFactory(column -> new TextFieldTableCellForSQuoToChall("tblcSCConvEditCmptGrossAmt", callback));

        tblcSCConvEditCmptDiscPerc.setCellValueFactory(cellData -> cellData.getValue().dis_perProperty());
        tblcSCConvEditCmptDiscPerc.setCellFactory(column -> new TextFieldTableCellForSQuoToChall("tblcSCConvEditCmptDiscPerc", callback));

        tblcSCConvEditCmptDiscPerc.setCellValueFactory(cellData -> cellData.getValue().dis_perProperty());
        tblcSCConvEditCmptDiscPerc.setCellFactory(column -> new TextFieldTableCellForSQuoToChall("tblcSCConvEditCmptDiscPerc", callback));

        tblcSCConvEditCmptDiscAmt.setCellValueFactory(cellData -> cellData.getValue().dis_amtProperty());
        tblcSCConvEditCmptDiscAmt.setCellFactory(column -> new TextFieldTableCellForSQuoToChall("tblcSCConvEditCmptDiscAmt", callback));

        tblcSCConvEditCmptTaxPerc.setCellValueFactory(cellData -> cellData.getValue().taxProperty());
        tblcSCConvEditCmptTaxPerc.setCellFactory(column -> new TextFieldTableCellForSQuoToChall("tblcSCConvEditCmptTaxPerc", callback));

        tblcSCConvEditCmptNetAmt.setCellValueFactory(cellData -> cellData.getValue().net_amountProperty());
        tblcSCConvEditCmptNetAmt.setCellFactory(column -> new TextFieldTableCellForSQuoToChall("tblcSCConvEditCmptNetAmt", callback));

        tblcSCConvEditCmptActions.setCellValueFactory(cellData -> cellData.getValue().net_amountProperty());
        tblcSCConvEditCmptActions.setCellFactory(column -> new ButtonTableCellQuoToChal());

    }

    public void setCbSupplierGSTN(ActionEvent actionEvent) {
        Object[] object = new Object[1];
        object[0] = cmbSCConvSupplierGST.getSelectionModel().getSelectedItem();
        if (object[0] != null) {
            for (CommonDTO commonDTO : supplierGSTINList) {
                if (object[0].equals(commonDTO)) {
                    supplierGSTIN_ID = commonDTO.getId();
                    gst_no = commonDTO.getText();
                }
            }
        }
    }


//    public void handleTfLedgerName() {
//        Stage stage = (Stage) rootAnchorPaneSoToSc.getScene().getWindow();
//             SingleInputDialogs.openLedgerPopUp(stage, "Filter", input -> {
//            tfSCConvLedgNameEdit.setText(input[0].toString());
//            ledger_id = (String) input[1];
//            ledgerStateCode = (String) input[3];
//            tvSCConvEditCMPTRow.getItems().get(0).setLedger_id(ledger_id);
//
//
//            @SuppressWarnings("unchecked")
//            ObservableList<GstDetailsDTO> gstDetailsDTOS = (ObservableList<GstDetailsDTO>) input[2];
//
//            for (GstDetailsDTO gstDetailsDTO : gstDetailsDTOS) {
//                supplierGSTINList.add(new CommonDTO(gstDetailsDTO.getGstNo(), gstDetailsDTO.getId()));
//            }
////            if (supplierGSTINList != null && !supplierGSTINList.isEmpty()) {
////                cmbSCConvSupplierGST.setItems(supplierGSTINList);
////                cmbSCConvSupplierGST.setValue(supplierGSTINList.get(0));
////            }
//        });
//    }

    //function for overall discount in percentage
//    @FXML
//    private void sales_disc_per(KeyEvent keyEvent) {
//        String discText = tfSCConvAllRowDisPerEdit.getText();
//        if (!TextUtils.isEmpty(discText)) {
//            double disc_per = Double.parseDouble(discText);
//
//            Double amount = (total_taxable_amt * disc_per) / 100;
//            tfSCConvAllRowDisAmtEdit.setText(String.valueOf(amount));
//            salesQuoToChalCalculation.discountPropotionalCalculation(tfSCConvAllRowDisPerEdit.getText(), tfSCConvAllRowDisAmtEdit.getText(), tfSCConvAddChrgTotalAmt.getText(), tvSCConvEditCMPTRow, callback);
//            if (!tfSCConvAddChrgTotalAmt.getText().isEmpty()) {
//                System.out.println("Additioanal CHarges Called >> ");
//                salesQuoToChalCalculation.additionalChargesCalculation(tfSCConvAddChrgTotalAmt.getText(), tvSCConvEditCMPTRow, callback);
//            }
//        } else {
//            tfSCConvAllRowDisAmtEdit.setText("");
//            for (CmpTRowDTOSoToSc saleChallanTable : tvSCConvEditCMPTRow.getItems()) {
//                String netAmt = saleChallanTable.getNet_amount();
//                if (!saleChallanTable.getTaxable_amt().isEmpty()) {
//                    Double orgTaxableAmt = Double.parseDouble(saleChallanTable.getTaxable_amt());
//                    saleChallanTable.setNet_amount(netAmt);
//                    saleChallanTable.setTaxable_amt(String.valueOf(orgTaxableAmt));
//                    saleChallanTable.setTotal_taxable_amt(String.valueOf(orgTaxableAmt));
//                }
//            }
//
//            salesQuoToChalCalculation.discountPropotionalCalculation("0", "0", tfSCConvAddChrgTotalAmt.getText(), tvSCConvEditCMPTRow, callback);
//            if (!tfSCConvAddChrgTotalAmt.getText().isEmpty()) {
//                System.out.println("Additioanal CHarges Called >> ");
//                salesQuoToChalCalculation.additionalChargesCalculation(tfSCConvAddChrgTotalAmt.getText(), tvSCConvEditCMPTRow, callback);
//            }
//
//        }
//    }

    public void sales_disc_per(KeyEvent keyEvent) {
        String discText = tfSCConvAllRowDisPerEdit.getText();
        if (!TextUtils.isEmpty(discText)) {
            double totalTaxableAmt = 0.0;
            for (int i = 0; i < tvSCConvEditCMPTRow.getItems().size(); i++) {
                System.out.println("Gross amt in Prop--->" + tvSCConvEditCMPTRow.getItems().get(i).getGross_amount());
                salesQuoToChalCalculation.rowCalculationForPurcInvoice(i, tvSCConvEditCMPTRow, callback);//call row calculation function
                CmpTRowDTOSoToSc purchaseInvoiceTable = tvSCConvEditCMPTRow.getItems().get(i);
                totalTaxableAmt = totalTaxableAmt + Double.parseDouble(purchaseInvoiceTable.getTaxable_amt());

            }
            double disc_per = Double.parseDouble(discText);
            System.out.println("totalTaxableAmount >> : " + totalTaxableAmt);
            Double amount = (totalTaxableAmt * disc_per) / 100;
            tfSCConvAllRowDisAmtEdit.setText(String.valueOf(amount));
            salesQuoToChalCalculation.discountPropotionalCalculation(disc_per + "", "0", tfSCConvAddChrgTotalAmt.getText(), tvSCConvEditCMPTRow, callback);
            if (!tfSCConvAddChrgTotalAmt.getText().isEmpty()) {
                System.out.println("Additioanal CHarges Called >> ");
                salesQuoToChalCalculation.additionalChargesCalculation(tfSCConvAddChrgTotalAmt.getText(), tvSCConvEditCMPTRow, callback);
            }
        } else {
            tfSCConvAllRowDisAmtEdit.setText("");

            salesQuoToChalCalculation.discountPropotionalCalculation("0", "0", tfSCConvAddChrgTotalAmt.getText(), tvSCConvEditCMPTRow, callback);
            if (!tfSCConvAddChrgTotalAmt.getText().isEmpty()) {
                System.out.println("Additioanal CHarges Called >> ");
                salesQuoToChalCalculation.additionalChargesCalculation(tfSCConvAddChrgTotalAmt.getText(), tvSCConvEditCMPTRow, callback);
            }
        }

        //? Calculate Tax for each Row
        salesQuoToChalCalculation.calculateGst(tvSCConvEditCMPTRow, callback);
    }

    //function for overall discount in Amount
//    @FXML
//    private void sales_disc_amt(KeyEvent keyEvent) {
//        String discAmtText = tfSCConvAllRowDisAmtEdit.getText();
//        if (!TextUtils.isEmpty(discAmtText)) {
//            double disc_amt = Double.parseDouble(discAmtText);
//            double percentage = (disc_amt / total_taxable_amt) * 100;
//            tfSCConvAllRowDisPerEdit.setText(String.format("%.2f", percentage));
//
//            salesQuoToChalCalculation.discountPropotionalCalculation(tfSCConvAllRowDisPerEdit.getText(), tfSCConvAllRowDisAmtEdit.getText(), tfSCConvAddChrgTotalAmt.getText(), tvSCConvEditCMPTRow, callback);
//            if (!tfSCConvAddChrgTotalAmt.getText().isEmpty()) {
//                System.out.println("Additioanal CHarges Called >> ");
//                salesQuoToChalCalculation.additionalChargesCalculation(tfSCConvAddChrgTotalAmt.getText(), tvSCConvEditCMPTRow, callback);
//            }
//        } else {
//            tfSCConvAllRowDisPerEdit.setText("");
//            for (CmpTRowDTOSoToSc purchaseInvoiceTable : tvSCConvEditCMPTRow.getItems()) {
//                String netAmt = purchaseInvoiceTable.getNet_amount();
//                if (!purchaseInvoiceTable.getTaxable_amt().isEmpty()) {
//                    Double orgTaxableAmt = Double.parseDouble(purchaseInvoiceTable.getTaxable_amt());
//                    purchaseInvoiceTable.setNet_amount(netAmt);
//                    purchaseInvoiceTable.setTaxable_amt(String.valueOf(orgTaxableAmt));
//                    purchaseInvoiceTable.setTotal_taxable_amt(String.valueOf(orgTaxableAmt));
//                }
//            }
//            salesQuoToChalCalculation.discountPropotionalCalculation("0", "0", tfSCConvAddChrgTotalAmt.getText(), tvSCConvEditCMPTRow, callback);
//            if (!tfSCConvAddChrgTotalAmt.getText().isEmpty()) {
//                System.out.println("Additioanal CHarges Called >> ");
//                salesQuoToChalCalculation.additionalChargesCalculation(tfSCConvAddChrgTotalAmt.getText(), tvSCConvEditCMPTRow, callback);
//            }
//        }
//    }

    public void sales_disc_amt(KeyEvent keyEvent) {
        String discAmtText = tfSCConvAllRowDisAmtEdit.getText();
        if (!TextUtils.isEmpty(discAmtText)) {
            double totalTaxableAmt = 0.0;
            for (int i = 0; i < tvSCConvEditCMPTRow.getItems().size(); i++) {
                System.out.println("Gross amt in Prop--->" + tvSCConvEditCMPTRow.getItems().get(i).getGross_amount());
                salesQuoToChalCalculation.rowCalculationForPurcInvoice(i, tvSCConvEditCMPTRow, callback);//call row calculation function
                CmpTRowDTOSoToSc purchaseInvoiceTable = tvSCConvEditCMPTRow.getItems().get(i);
                totalTaxableAmt = totalTaxableAmt + Double.parseDouble(purchaseInvoiceTable.getTaxable_amt());

            }
            double disc_amt = Double.parseDouble(discAmtText);
            double percentage = (disc_amt / totalTaxableAmt) * 100;
            tfSCConvAllRowDisPerEdit.setText(String.format("%.2f", percentage));

            salesQuoToChalCalculation.discountPropotionalCalculation("0", disc_amt + "", tfSCConvAddChrgTotalAmt.getText(), tvSCConvEditCMPTRow, callback);
            if (!tfSCConvAddChrgTotalAmt.getText().isEmpty()) {
                System.out.println("Additioanal CHarges Called >> ");
                salesQuoToChalCalculation.additionalChargesCalculation(tfSCConvAddChrgTotalAmt.getText(), tvSCConvEditCMPTRow, callback);
            }
        } else {
            tfSCConvAllRowDisPerEdit.setText("");
           /* for (PurchaseInvoiceTable purchaseInvoiceTable : tvSCConvEditCMPTRow.getItems()) {
                String netAmt = purchaseInvoiceTable.getNet_amount();
                if (!purchaseInvoiceTable.getTaxable_amt().isEmpty()) {
                    Double orgTaxableAmt = Double.parseDouble(purchaseInvoiceTable.getTaxable_amt());
                    purchaseInvoiceTable.setNet_amount(netAmt);
                    purchaseInvoiceTable.setTaxable_amt(String.valueOf(orgTaxableAmt));
                    purchaseInvoiceTable.setTotal_taxable_amt(String.valueOf(orgTaxableAmt));
                }
            }*/
            salesQuoToChalCalculation.discountPropotionalCalculation("0", "0", tfSCConvAddChrgTotalAmt.getText(), tvSCConvEditCMPTRow, callback);
            if (!tfSCConvAddChrgTotalAmt.getText().isEmpty()) {
                System.out.println("Additioanal CHarges Called >> ");
                salesQuoToChalCalculation.additionalChargesCalculation(tfSCConvAddChrgTotalAmt.getText(), tvSCConvEditCMPTRow, callback);
            }
        }

        //? Calculate Tax for each Row
        salesQuoToChalCalculation.calculateGst(tvSCConvEditCMPTRow, callback);
    }

//    public void purc_disc_per(KeyEvent keyEvent) {
//        String discText = tfSCConvAllRowDisPerEdit.getText();
//        if (!TextUtils.isEmpty(discText)) {
//            double disc_per = Double.parseDouble(discText);
//
//            Double amount = (total_taxable_amt * disc_per) / 100;
//            tfSCConvAllRowDisAmtEdit.setText(String.valueOf(amount));
//            salesQuoToChalCalculation.discountPropotionalCalculation(tfSCConvAllRowDisPerEdit.getText(), tfSCConvAllRowDisAmtEdit.getText(), tfSCConvAddChrgTotalAmt.getText(), tvSCConvEditCMPTRow, callback);
//
////            com.opethic.genivis.controller.tranx_purchase.salesQuoToChalCalculation.discountPropotionalCalculation(tfSCConvAllRowDisPerEdit.getText(), tfSCConvAllRowDisAmtEdit.getText(), tfSCConvAddChrgTotalAmt.getText(), tvSCConvEditCMPTRow, callback);
//
//        } else {
//            tfSCConvAllRowDisAmtEdit.setText("");
//            for (CmpTRowDTOSoToSc CmpTRowDTOSoToSc : tvSCConvEditCMPTRow.getItems()) {
//                String netAmt = CmpTRowDTOSoToSc.getNet_amount();
//                if (!CmpTRowDTOSoToSc.getTaxable_amt().isEmpty()) {
//                    Double orgTaxableAmt = Double.parseDouble(CmpTRowDTOSoToSc.getTaxable_amt());
//                    CmpTRowDTOSoToSc.setNet_amount(netAmt);
//                    CmpTRowDTOSoToSc.setTaxable_amt(String.valueOf(orgTaxableAmt));
//                    CmpTRowDTOSoToSc.setTotal_taxable_amt(String.valueOf(orgTaxableAmt));
//                }
//            }
//
//            salesQuoToChalCalculation.discountPropotionalCalculation(tfSCConvAllRowDisPerEdit.getText(), tfSCConvAllRowDisAmtEdit.getText(), tfSCConvAddChrgTotalAmt.getText(), tvSCConvEditCMPTRow, callback);
//
//
//        }
//    }
//
//    public void purc_disc_amt(KeyEvent keyEvent) {
//        String discAmtText = tfSCConvAllRowDisAmtEdit.getText();
//        if (!TextUtils.isEmpty(discAmtText)) {
//            double disc_amt = Double.parseDouble(discAmtText);
//            double percentage = (disc_amt / total_taxable_amt) * 100;
//            tfSCConvAllRowDisPerEdit.setText(String.format("%.2f", percentage));
//            salesQuoToChalCalculation.discountPropotionalCalculation(tfSCConvAllRowDisPerEdit.getText(), tfSCConvAllRowDisAmtEdit.getText(), tfSCConvAddChrgTotalAmt.getText(), tvSCConvEditCMPTRow, callback);
//
////            com.opethic.genivis.controller.tranx_purchase.salesQuoToChalCalculation.discountPropotionalCalculation(tfSCConvAllRowDisPerEdit.getText(), tfSCConvAllRowDisAmtEdit.getText(), tfSCConvAddChrgTotalAmt.getText(), tvSCConvEditCMPTRow, callback);
//        } else {
//            tfSCConvAllRowDisPerEdit.setText("");
//            for (CmpTRowDTOSoToSc CmpTRowDTOSoToSc : tvSCConvEditCMPTRow.getItems()) {
//                String netAmt = CmpTRowDTOSoToSc.getNet_amount();
//                if (!CmpTRowDTOSoToSc.getTaxable_amt().isEmpty()) {
//                    Double orgTaxableAmt = Double.parseDouble(CmpTRowDTOSoToSc.getTaxable_amt());
//                    CmpTRowDTOSoToSc.setNet_amount(netAmt);
//                    CmpTRowDTOSoToSc.setTaxable_amt(String.valueOf(orgTaxableAmt));
//                    CmpTRowDTOSoToSc.setTotal_taxable_amt(String.valueOf(orgTaxableAmt));
//                }
//            }
//
//            salesQuoToChalCalculation.discountPropotionalCalculation(tfSCConvAllRowDisPerEdit.getText(), tfSCConvAllRowDisAmtEdit.getText(), tfSCConvAddChrgTotalAmt.getText(), tvSCConvEditCMPTRow, callback);
//
////            com.opethic.genivis.controller.tranx_purchase.salesQuoToChalCalculation.discountPropotionalCalculation("0", "0", tfPurchaseChallanAddChgAmt.getText(), tvSCConvEditCMPTRow, callback);
//        }
//    }


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


}  //end of Sales order to challan Main Class


class TextFieldTableCellForSQuoToChall extends TableCell<CmpTRowDTOSoToSc, String> {
    private TextField textField;
    private String columnName;
    TableCellCallback<Object[]> product_callback;

    TableCellCallback<Object[]> callback;
    private TextField button;//new2.0
    TableCellCallback<Object[]> batchID_callback;

    public TextFieldTableCellForSQuoToChall(String columnName, TableCellCallback<Object[]> callback) {
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
        freeQtyColumn();
        rateColumn();
        grossAmtColumn();
        discountPercColumn();
//        discountPerc2Column();
        disAmtColumn();
    }

    public TextFieldTableCellForSQuoToChall(String columnName, TableCellCallback<Object[]> callback, TableCellCallback<Object[]> product_callback, TextField button) {
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

        batchColumn();

        netAmountColumn();

        quantityColumn();
        freeQtyColumn();
        rateColumn();
        grossAmtColumn();
        discountPercColumn();
//        discountPerc2Column();
        disAmtColumn();
    }


    private void batchColumn() {
        if ("tblcSCConvEditCmptBatch".equals(columnName)) {
            textField.setEditable(false);

            textField.setOnKeyPressed(event -> {
                if (event.getCode() == KeyCode.SPACE) {
                    openBatchWindow();
                }
                if (event.isShiftDown() && event.getCode() == KeyCode.TAB) {
                    TableColumn<CmpTRowDTOSoToSc, ?> colName = getTableView().getColumns().get(1);
                    getTableView().edit(getIndex(), colName);
                }

                if (event.getCode() == KeyCode.ENTER || !event.isShiftDown() && event.getCode() == KeyCode.TAB) {
                    if (!textField.getText().isEmpty()) {
                        TableColumn<CmpTRowDTOSoToSc, ?> colName = getTableView().getColumns().get(7);
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

    private void quantityColumn() {                //function for calculation when we enter the qty in cmpt row
        if ("tblcSCConvEditCmptQuantity".equals(columnName)) {
            textField.setEditable(true);

//            textField.setOnKeyPressed(event -> {
//                if (event.getCode() == KeyCode.ENTER || event.getCode() == KeyCode.TAB) {
//                    Integer index = getIndex();
//                    System.out.println("Rada INDEX" + index);
//                    salesQuoToChalCalculation.rowCalculationForPurcInvoice(index, getTableView(), callback);
//                    salesQuoToChalCalculation.calculateGst(getTableView(), callback);
//                }
//            });
            //new2.0 start
            textField.setOnKeyPressed(event -> {
                CommonValidationsUtils.restrictToDecimalNumbers(textField);
                if (event.isShiftDown() && event.getCode() == KeyCode.TAB) {
                    TableColumn<CmpTRowDTOSoToSc, ?> colName = getTableView().getColumns().get(7);
                    getTableView().edit(getIndex(), colName);

                } else if (event.getCode() == KeyCode.ENTER || event.getCode() == KeyCode.TAB) {
                    Integer index = getIndex();
                    if (!textField.getText().isEmpty()) {
                        TableColumn<CmpTRowDTOSoToSc, ?> colName = getTableView().getColumns().get(10);
                        getTableView().edit(index, colName);
                        System.out.println("Rada INDEX" + index);

                    } else {
                        textField.requestFocus();
                    }
                    salesQuoToChalCalculation.rowCalculationForPurcInvoice(index, getTableView(), callback);
                    salesQuoToChalCalculation.calculateGst(getTableView(), callback);

                } else if (event.getCode() == KeyCode.DOWN) {
                    getTableView().getItems().addAll(new CmpTRowDTOSoToSc("", "", "", "", "", "", "", "", "", "","", "", "", "", "", "", "", "", ""));
                    TableColumn<CmpTRowDTOSoToSc, ?> colName = getTableView().getColumns().get(1);
                    getTableView().edit(getIndex() + 1, colName);
                }
                //new2.0 end
            });
        }
    }

    private void freeQtyColumn() {
        if ("tblcSCConvEditCmptFreeQty".equals(columnName)) {
            textField.setEditable(true);
            textField.setOnKeyPressed(event -> {
                CommonValidationsUtils.restrictToDecimalNumbers(textField);
                if (event.isShiftDown() && event.getCode() == KeyCode.TAB) {
                    TableColumn<CmpTRowDTOSoToSc, ?> colName = getTableView().getColumns().get(8);
                    System.out.println("Col:" + colName.getText());
                    getTableView().edit(getIndex(), colName);

                } else if (event.getCode() == KeyCode.ENTER || !event.isShiftDown() && event.getCode() == KeyCode.TAB) {
                    int index = getIndex();
                    TableColumn<CmpTRowDTOSoToSc, ?> colName = getTableView().getColumns().get(10);
                    getTableView().edit(getIndex(), colName);
                    salesQuoToChalCalculation.rowCalculationForPurcInvoice(index, getTableView(), callback);
                }
            });
           /* TableColumn<PurchaseOrderTable, ?> colName = getTableView().getColumns().get(8);
            getTableView().edit(getIndex(), colName);*/

        }
    }

    private void rateColumn() {                //function for calculation when we enter the qty in cmpt row
        if ("tblcSCConvEditCmptRate".equals(columnName)) {
            textField.setEditable(true);

//            textField.setOnKeyPressed(event -> {
//                if (event.getCode() == KeyCode.ENTER || event.getCode() == KeyCode.TAB) {
//                    Integer index = getIndex();
//                    System.out.println("Rada INDEX" + index);
//                    salesQuoToChalCalculation.rowCalculationForPurcInvoice(index, getTableView(), callback);
//                    salesQuoToChalCalculation.calculateGst(getTableView(), callback);
//                }
//            });

            textField.setOnKeyPressed(event -> {
                if (event.isShiftDown() && event.getCode() == KeyCode.TAB) {
                    TableColumn<CmpTRowDTOSoToSc, ?> colName = getTableView().getColumns().get(8);
                    getTableView().edit(getIndex() - 1, colName);
                } else if (event.getCode() == KeyCode.ENTER || !event.isShiftDown() && event.getCode() == KeyCode.TAB) {
                    int index = getIndex();
                    if (!textField.getText().isEmpty()) {
                        TableColumn<CmpTRowDTOSoToSc, ?> colName = getTableView().getColumns().get(12);
                        getTableView().edit(index, colName);
                    } else {
                        textField.requestFocus();
                    }
                    salesQuoToChalCalculation.rowCalculationForPurcInvoice(index, getTableView(), callback);
                    salesQuoToChalCalculation.calculateGst(getTableView(), callback);
                } else if (event.getCode() == KeyCode.DOWN) {
                    getTableView().getItems().addAll(new CmpTRowDTOSoToSc("", "", "", "", "", "", "", "","", "", "", "", "", "", "", "", "", "", ""));
                    TableColumn<CmpTRowDTOSoToSc, ?> colName = getTableView().getColumns().get(1);
                    getTableView().edit(getIndex() + 1, colName);
                }
            });
        }
    }
    private void grossAmtColumn() {
        if ("tblcSCConvEditCmptGrossAmt".equals(columnName)) {
            textField.setEditable(false);
            textField.setFocusTraversable(false);

        }
    }

    private void discountPercColumn() {                //function for calculation when we enter the qty in cmpt row
        if ("tblcSCConvEditCmptDiscPerc".equals(columnName)) {
            textField.setEditable(true);

//            textField.setOnKeyPressed(event -> {
//                if (event.getCode() == KeyCode.ENTER || event.getCode() == KeyCode.TAB) {
//                    Integer index = getIndex();
//                    System.out.println("Rada INDEX" + index);
//                    salesQuoToChalCalculation.rowCalculationForPurcInvoice(index, getTableView(), callback);
//                    salesQuoToChalCalculation.calculateGst(getTableView(), callback);
//                }
//            });
            //new2.0 start
            textField.setOnKeyPressed(event -> {
                if (event.isShiftDown() && event.getCode() == KeyCode.TAB) {
                    TableColumn<CmpTRowDTOSoToSc, ?> colName = getTableView().getColumns().get(10);
                    getTableView().edit(getIndex(), colName);
                } else if (event.getCode() == KeyCode.ENTER || !event.isShiftDown() && event.getCode() == KeyCode.TAB) {
                    int index = getIndex();
                    TableColumn<CmpTRowDTOSoToSc, ?> colName = getTableView().getColumns().get(13);
                    getTableView().edit(index, colName);
                    salesQuoToChalCalculation.rowCalculationForPurcInvoice(index, getTableView(), callback);
                    salesQuoToChalCalculation.calculateGst(getTableView(), callback);
//new2.0 end
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
//                    salesQuoToChalCalculation.rowCalculationForPurcInvoice(index, getTableView(), callback);
//                    salesQuoToChalCalculation.calculateGst(getTableView(), callback);
//                }
//            });
//        }
//    }

    private void disAmtColumn() {                //function for calculation when we enter the qty in cmpt row
        if ("tblcSCConvEditCmptDiscAmt".equals(columnName)) {
            textField.setEditable(true);

//            textField.setOnKeyPressed(event -> {
//                if (event.getCode() == KeyCode.ENTER || event.getCode() == KeyCode.TAB) {
//                    Integer index = getIndex();
//                    System.out.println("Rada INDEX" + index);
//                    salesQuoToChalCalculation.rowCalculationForPurcInvoice(index, getTableView(), callback);
//                    salesQuoToChalCalculation.calculateGst(getTableView(), callback);
//                }
//            });

            textField.setEditable(true);
//new2.0 start
            textField.setOnKeyPressed(event -> {
                if (event.isShiftDown() && event.getCode() == KeyCode.TAB) {
                    TableColumn<CmpTRowDTOSoToSc, ?> colName = getTableView().getColumns().get(12);
                    getTableView().edit(getIndex(), colName);
                } else if (event.getCode() == KeyCode.ENTER || !event.isShiftDown() && event.getCode() == KeyCode.TAB) {
                    int index = getIndex();
                    int current_index = getTableRow().getIndex();
                    //new start
                    if (getTableRow().getItem().getSr_no() != null) {
                        int serial_no = Integer.parseInt(getTableRow().getItem().getSr_no());
                        getTableView().getItems().addAll(new CmpTRowDTOSoToSc("", "", String.valueOf(serial_no + 1), "", "", "", "", "", "", "", "","", "", "", "", "", "", "", ""));
                    }
                    TableColumn<CmpTRowDTOSoToSc, ?> colName = getTableView().getColumns().get(1);
                    getTableView().edit(getIndex() + 1, colName);
                    salesQuoToChalCalculation.rowCalculationForPurcInvoice(index, getTableView(), callback);
                    salesQuoToChalCalculation.calculateGst(getTableView(), callback);
//new2.0 end

                }
            });
        }
    }

    private void particularsColumn() {
        if ("tblcSCConvEditCmptParticular".equals(columnName)) {
            textField.setEditable(false);

            textField.setOnKeyPressed(event -> {
                if (event.getCode() == KeyCode.SPACE) {
                    openProduct(getIndex());
                }
                if (event.isShiftDown() && event.getCode() == KeyCode.TAB) {
                    if (getIndex() == 0) {
                        TableColumn<CmpTRowDTOSoToSc, ?> colName = getTableView().getColumns().get(7);
                        getTableView().edit(getIndex() - 1, colName);
                    } else {
                        TableColumn<CmpTRowDTOSoToSc, ?> colName = getTableView().getColumns().get(13);
                        getTableView().edit(getIndex() - 1, colName);
                    }
                } else if (event.getCode() == KeyCode.ENTER || !event.isShiftDown() && event.getCode() == KeyCode.TAB) {
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
                if (textField.getText().isEmpty()) {
                    //Pass productId to ProductPopup for Focus in open ProductPopup on Space in Create if no product selected
                    SingleInputDialogs.productId = "";
                }
                openProduct(getIndex());
            });
        }
    }

    private void netAmountColumn() {

        if ("tblcSCConvEditCmptNetAmt".equals(columnName)) {
            textField.setEditable(false);
            Platform.runLater(() -> {
                textField.setOnKeyPressed(event -> {
                    if (event.getCode() == KeyCode.SPACE) {
                        int current_index = getTableRow().getIndex();
                        //new start
                        if (getTableRow().getItem().getSr_no() != null) {
//                            int serial_no = Integer.parseInt(getTableRow().getItem().getSr_no());
                            getTableView().getItems().addAll(new CmpTRowDTOSoToSc("", String.valueOf(current_index + 1), "1", "", "", "", "", "", "", "","", "", "", "", "", "", "", "", ""));
                        }
                        //new end

//
                    }
                });
            });
//            textField.setOnKeyPressed(event -> {
//                if (event.getCode() == KeyCode.ENTER) {
//
//                    int current_index = getTableRow().getIndex();
//
//                    getTableView().getItems().addAll(new CmpTRowDTOSoToSc("", String.valueOf(current_index + 1), "1", "", "", "", "", "", "", "","", "", "", "", "", "", "", "", ""));
//                }
//            });
        }

    }

    //new start
    private void openProduct(int currentIndex) {
        CmpTRowDTOSoToSc selectedRow = getTableView().getItems().get(currentIndex);

        TranxCommonPopUps.openProductPopUp(Communicator.stage, Integer.valueOf(selectedRow.getProduct_id()), "Product", input -> {
//            Integer selectedIndex = tblTranxSalesInvoiceCreateCMPTRow.getSelectionModel().getSelectedIndex();
//            System.out.println("input);
            if (input != null) {
                getTableRow().getItem().setParticulars(input.getProductName());
                getTableRow().getItem().setProduct_id(input.getProductId().toString());
                String product_id = input.getProductId().toString();

                if (product_callback != null) {
                    Object[] object = new Object[1];
                    object[0] = product_id;
                    product_callback.call(object);
                }

                getTableRow().getItem().setPackages(input.getPackageName());
                getTableRow().getItem().setIs_batch(input.getBatch().toString());
                getTableRow().getItem().setTax(input.getIgst().toString());
//                getTableRow().getItem().setUnit(input.get());
                getTableRow().getItem().setSelectedProduct(input);
                getTableRow().getItem().setRate(input.getSalesRate().toString());
//                getTableRow().getItem().
                List<LevelAForPurInvoice> levelAForPurInvoiceList = ProductUnitsPackingQuoToChal.getAllProductUnitsPackingFlavour(input.getProductId().toString());
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
            }
        },null);
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
//            getTableRow().getItem().setPackages(packaging);
//            getTableRow().getItem().setIs_batch(is_batch);
//            getTableRow().getItem().setTax(taxper);
//            getTableRow().getItem().setUnit(unit);
//
//            if(productId!=""){
//                //Pass productId to ProductPopup for Focus in open ProductPopup after Product is Selected in Create
//                SingleInputDialogs.productId= productId;
//            }
//            else{
//                SingleInputDialogs.productId= "";
//            }
//
//            System.out.println("particulers "+getTableView().getItems().get(0).getParticulars());
//            List<LevelAForPurInvoice> levelAForPurInvoiceList = com.opethic.genivis.controller.tranx_sales.ProductUnitsPackingQuoToChal.getAllProductUnitsPackingFlavour(productId);
//            int index = getTableRow().getIndex();
//            ObservableList<LevelAForPurInvoice> observableLevelAList = FXCollections.observableArrayList(levelAForPurInvoiceList);
//
//            if (index >= 0 && index < PurInvoiceCommunicator.levelAForPurInvoiceObservableList.size()) {
//                PurInvoiceCommunicator.levelAForPurInvoiceObservableList.set(index, observableLevelAList);
////                getTableRow().getItem().setLevelA(null);
////                getTableRow().getItem().setLevelA(levelAForPurInvoiceList.get(0).getLabel());
//
//            } else {
//                PurInvoiceCommunicator.levelAForPurInvoiceObservableList.add(observableLevelAList);
////                getTableRow().getItem().setLevelA(null);
////                getTableRow().getItem().setLevelA(levelAForPurInvoiceList.get(0).getLabel());
//            }
//            Integer product_id = Integer.parseInt(productId);
//            if (product_callback != null) {
//                Object[] object = new Object[1];
//                object[0] = productId;
//                product_callback.call(object);
//            }
//
//
////            if(product_callback!=null){
////                product_callback.call(product_id);
////            }
//
//        });
//
//    }

    //    public void openBatchWindow() {
//        SingleInputDialogs.openBatchPopUp(Communicator.stage, "Batch", input -> {
//            getTableRow().getItem().setBatch_or_serial((String) input[0]);
//            int selectedIndex = getIndex();
//            Platform.runLater(() -> {
//                getTableView().refresh();
//            });
//
//            System.out.println("batchNo "+input[0]+" input1 "+input[1]+" input2 "+input[2]+" input3 "+input[3]+" input4 "+input[4]+" input5 "+input[5]+" input6 "+input[6]);
//            System.out.println("input7 "+input[7]+" input8 "+input[8]+" input9 "+input[9]+" input10 "+input[10]+" input11 "+input[11]+" input12 "+input[12]+" input13 "+input[13]);
//            System.out.println("input14 "+input[14]+" input15 "+input[15]+" input16 "+input[16]+" b_details_id "+input[17]+" input18 "+input[18]);
//
//            if (product_callback != null) {
//                Object[] object = new Object[2];
//                object[0] = input[0];
//                object[1] = input[17];
//                product_callback.call(object);
//            }
//        });
//
//
//    }
    public void openBatchWindow() {
        CmpTRowDTOSoToSc selectedRow = getTableView().getItems().get(getIndex());
        System.out.println("getIsBatchhh " + selectedRow.getIs_batch());
        if (Boolean.valueOf(selectedRow.getIs_batch()) == true) {

            TranxCommonPopUps.openBatchPopUpSalesChallan(Communicator.stage, selectedRow, Communicator.tranxDate, "Batch", input -> {
                if (input != null) {
                    System.out.println("afterb batch selected callbackkk " + input.getBatchNo() + " " + input);
                    selectedRow.setSelectedBatch(input);
                    selectedRow.setB_details_id(String.valueOf(input.getId()));
                    selectedRow.setB_no(input.getBatchNo());
                    selectedRow.setBatch_or_serial(input.getBatchNo());

                    if (product_callback != null) {
                        System.out.println("selectedRow.getB_no() " + input.getBatchNo() + " selectedRow.getId() " + input.getId());
                        Object[] object = new Object[2];
                        object[0] = input.getBatchNo().toString();
                        object[1] = String.valueOf(input.getId());
                        product_callback.call(object);
                    }
//                Object[] object = new Object[2];
//                object[0] = selectedRow.getB_no();  // casting to String is unnecessary here as input is already an array of Objects
//                object[1] = selectedRow.getId();  // same as above

//                if (batchID_callback != null) {
//                    batchID_callback.call(object);  // call with the object array
//                }
                    TableColumn<CmpTRowDTOSoToSc, ?> colName = getTableView().getColumns().get(1);
                    getTableView().edit(getIndex(), colName);
                }


                textField.requestFocus();
                getTableView().getItems().set(getIndex(), selectedRow);


//                if (batchID_callback != null) {
//                    Object[] object = new Object[2];
//                    object[0] = input.getBatchNo();  // casting to String is unnecessary here as input is already an array of Objects
//                    object[1] = input.getId();  // same as above
//
//                    batchID_callback.call(object);  // call with the object array
//                }
                //            if (batchID_callback != null) {
//                batchID_callback.call(object);  // call with the object array
//            }
                // getBatchDetailsByBatchNoAndId(input.getBatchNo(), String.valueOf(input.getId()));
            });
        } else {
            selectedRow.setBatch_or_serial("#");
            selectedRow.setB_no("#");
            getTableView().getItems().set(getIndex(), selectedRow);
        }
        TableColumn<CmpTRowDTOSoToSc, ?> colName = getTableView().getColumns().get(8);

        getTableView().edit(getIndex(), colName);

    }

    public void textfieldStyle() {
        if (columnName.equals("tblcSCConvEditCmptParticular")) {
            textField.setPromptText("Particulars");
            textField.setEditable(false);
        } else {
            textField.setPromptText("0");
        }

        textField.setPrefHeight(38);
        textField.setMaxHeight(38);
        textField.setMinHeight(38);

        if (columnName.equals("tblcSCConvEditCmptParticular")) {
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


        if (columnName.equals("tblcSCConvEditCmptNetAmt")) {
            this.textField.setOnKeyPressed(event -> {
                if (event.getCode() == KeyCode.ENTER) {
                    commitEdit(textField.getText());
                    getTableView().getItems().addAll(new CmpTRowDTOSoToSc("", "", "1", "", "", "", "", "", "", "", "", "","", "", "", "", "", "", ""));
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
        if (item != null && columnName.equals("tblcSCConvEditCmptParticular")) {
            ((CmpTRowDTOSoToSc) item).setParticulars(newValue.isEmpty() ? "" : newValue);
        } else if (columnName.equals("tcSCConvEditSupplierBatch")) {
            (getTableRow().getItem()).setBatch_or_serial(newValue.isEmpty() ? "" : newValue);
        } else if (columnName.equals("tblcSCConvEditCmptQuantity")) {
            (getTableRow().getItem()).setQuantity(newValue.isEmpty() ? "0" : newValue);
        } else if (columnName.equals("tblcSCConvEditCmptFreeQty")) {//new2.0
            (getTableRow().getItem()).setFree(newValue.isEmpty() ? "0.0" : newValue);//new2.0
        } else if (columnName.equals("tblcSCConvEditCmptRate")) {
            (getTableRow().getItem()).setRate(newValue.isEmpty() ? "0.0" : newValue);
        } else if (columnName.equals("tblcSalesChallanCmpTRowGrossAmt")) {
            (getTableRow().getItem()).setGross_amount(newValue.isEmpty() ? "0.0" : newValue);
        } else if (columnName.equals("tblcSCConvEditCmptDiscPerc")) {
            (getTableRow().getItem()).setDis_per(newValue.isEmpty() ? "0.0" : newValue);
        } else if (columnName.equals("tblcSCConvEditCmptDiscAmt")) {
            (getTableRow().getItem()).setDis_amt(newValue.isEmpty() ? "0.0" : newValue);
        } else if (columnName.equals("tblcSalesChallanCmpTRowTaxPer")) {
            (getTableRow().getItem()).setTax(newValue.isEmpty() ? "0.0" : newValue);
        } else if (columnName.equals("tblcSCConvEditCmptNetAmt")) {
            (getTableRow().getItem()).setNet_amount(newValue.isEmpty() ? "0.0" : newValue);
        }

    }
}

class ComboBoxTableCellForLevelAQuoToChal extends TableCell<CmpTRowDTOSoToSc, String> {

    String columnName;
    private final ComboBox<String> comboBox;
    ObservableList<LevelAForPurInvoice> comboList = null;

    public ComboBoxTableCellForLevelAQuoToChal(String columnName) {

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

                            }
                            else {
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

                comboBox.setValue(item);
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
                item.setLevelA(newValue);                            //to be uncomment
            }
        }
    }

}

class ComboBoxTableCellForLevelBQuoToChal extends TableCell<CmpTRowDTOSoToSc, String> {
    String columnName;
    private final ComboBox<String> comboBox;
    ObservableList<LevelBForPurInvoice> comboList = null;

    public ComboBoxTableCellForLevelBQuoToChal(String columnName) {
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

                            }
                            else {
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

class ComboBoxTableCellForLevelCQuoToChal extends TableCell<CmpTRowDTOSoToSc, String> {

    String columnName;
    private final ComboBox<String> comboBox;
    ObservableList<LevelAForPurInvoice> comboList = null;

    public ComboBoxTableCellForLevelCQuoToChal(String columnName) {


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

class ComboBoxTableCellForUnitQuoToChal extends TableCell<CmpTRowDTOSoToSc, String> {

    String columnName;
    TableCellCallback<Integer> unit_callback;//new
    private final ComboBox<String> comboBox;

    public ComboBoxTableCellForUnitQuoToChal(String columnName, TableCellCallback<Integer> unit_callback) {

        this.unit_callback = unit_callback;//new
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
                if (PurInvoiceCommunicator.unitForPurInvoiceList != null && !PurInvoiceCommunicator.unitForPurInvoiceList.isEmpty()) {

                    for (UnitForPurInvoice unitForPurInvoice : PurInvoiceCommunicator.unitForPurInvoiceList.get(getTableRow().getIndex())) {
                        if (selectedItem.equals(unitForPurInvoice.getLabel())) {
                            getTableRow().getItem().setUnit_id(unitForPurInvoice.getValue());
                            getTableRow().getItem().setUnit_conv(String.valueOf(unitForPurInvoice.getUnitConversion()));
                            //!new
                            System.out.println("getTableRow().getItem().getUnit() "+ getTableRow().getItem().getUnit());
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
                TableColumn<CmpTRowDTOSoToSc, ?> colName = getTableView().getColumns().get(6);
                getTableView().edit(getIndex(), colName);
            }

            if (event.getCode() == KeyCode.ENTER || !event.isShiftDown() && event.getCode() == KeyCode.TAB) {
                TableColumn<CmpTRowDTOSoToSc, ?> colName = getTableView().getColumns().get(8);
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
            } else {
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

class ButtonTableCellQuoToChal extends TableCell<CmpTRowDTOSoToSc, String> {
    private Button delete;

    public ButtonTableCellQuoToChal() {


        this.delete = createButtonWithImage();


        delete.setOnAction(actionEvent -> {
            CmpTRowDTOSoToSc table = getTableView().getItems().get(getIndex());
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

class ProductUnitsPackingQuoToChal {

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
//            System.out.println("responseStatus: " + responseStatus);

            if (responseStatus == 200) {
                JsonObject resObj = jsonObject.get("responseObject").getAsJsonObject();
                JsonArray lstPackages = resObj.get("lst_packages").getAsJsonArray();

                for (JsonElement lstPackage : lstPackages) {
                    LevelAForPurInvoice levelAForPurInvoice = new Gson().fromJson(lstPackage, LevelAForPurInvoice.class);
                    levelAForPurInvoicesList.add(levelAForPurInvoice);
                }


            } else {
                System.out.println("getAllProductUnitsPackingFlavour() responseObject is null");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }


        return levelAForPurInvoicesList;
    }

}


class salesQuoToChalCalculation {

    public static void rowCalculationForPurcInvoice(int rowIndex, TableView<CmpTRowDTOSoToSc> tableView, TableCellCallback<Object[]> callback) {
        CmpTRowDTOSoToSc CmpTRowDTOSoToSc = tableView.getItems().get(rowIndex);
        String CmpTRowDTOSoToScData = CmpTRowDTOSoToSc.getRate();
        System.out.println("In Row Calculation CMPTDATA=>" + CmpTRowDTOSoToScData);
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
        r_qty = CmpTRowDTOSoToSc.getQuantity();
        r_rate = Double.parseDouble(CmpTRowDTOSoToSc.getRate());
        if (!CmpTRowDTOSoToSc.getDis_amt().isEmpty())
            r_dis_amt = Double.parseDouble(CmpTRowDTOSoToSc.getDis_amt());
        if (!CmpTRowDTOSoToSc.getDis_per().isEmpty())
            r_dis_per = Double.parseDouble(CmpTRowDTOSoToSc.getDis_per());
        if (CmpTRowDTOSoToSc.getDis_per2() != null && !CmpTRowDTOSoToSc.getDis_per2().isEmpty()) {
            disPer2 = Double.parseDouble(CmpTRowDTOSoToSc.getDis_per2());
        } else {
            disPer2 = 0.0;
        }
        r_tax_per = Double.parseDouble(CmpTRowDTOSoToSc.getTax());
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

//        //Display data to table view
//        tblcPurChallCmpTRowGrossAmt.setCellValueFactory(new PropertyValueFactory<>("gross_amt"));
//        tblcPurChallCmpTRowNetAmt.setCellValueFactory(new PropertyValueFactory<>("net_amt"));
//        calculateGst(tableView, callback);

    }

    public static void calculateGst(TableView<CmpTRowDTOSoToSc> tableView, TableCellCallback<Object[]> callback) {


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
        for (CmpTRowDTOSoToSc CmpTRowDTOSoToSc : tableView.getItems()) {
            taxPercentage = Double.parseDouble(CmpTRowDTOSoToSc.getTax());
            Double quantity = Double.parseDouble(CmpTRowDTOSoToSc.getQuantity());
            if (CmpTRowDTOSoToSc.getFree().isEmpty()) {
                freeQuantity = 0.0;
            } else {
                freeQuantity = Double.parseDouble(CmpTRowDTOSoToSc.getFree());
            }


            // Total Calculations of each IGST, SGST, CGST
            totalQuantity += quantity;
            totalFreeQuantity += freeQuantity;
            Double igst = Double.parseDouble(CmpTRowDTOSoToSc.getIgst());
            Double cgst = Double.parseDouble(CmpTRowDTOSoToSc.getCgst());
            Double sgst = Double.parseDouble(CmpTRowDTOSoToSc.getSgst());
            totalFinalIgst += igst;
            totalFinalSgst += sgst;
            totalFinalCgst += cgst;

            cgstTotals.put(taxPercentage, cgstTotals.get(taxPercentage) + cgst); //0.0
            sgstTotals.put(taxPercentage, sgstTotals.get(taxPercentage) + sgst);

            //Total Calculation of gross amt ,taxable ,tax,discount
            netAmount = Double.parseDouble(CmpTRowDTOSoToSc.getNet_amount());
            totalNetAmt += netAmount;
            grossAmt = Double.parseDouble(CmpTRowDTOSoToSc.getGross_amount());
            totalGrossAmt += grossAmt;
            taxableAmt = Double.parseDouble(CmpTRowDTOSoToSc.getTaxable_amt());
            totaltaxableAmt += taxableAmt;
            disAmt = Double.parseDouble(CmpTRowDTOSoToSc.getFinal_dis_amt());
            totalDisAmt += disAmt;
            taxAmt = Double.parseDouble(CmpTRowDTOSoToSc.getFinal_tax_amt());
            totalTaxAmt += taxAmt;

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

            //  lblSCConvTotalTaxEditableAmount.setText(String.format("%.2f", (totalGrossAmt - totalDisAmt + addChargAmount)));
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
            System.out.println("Gross amt in Prop--->" + tableView.getItems().get(i).getGross_amount());
//            rowCalculationForPurcInvoice(i, tableView, callback);//call row calculation function
            CmpTRowDTOSoToSc CmpTRowDTOSoToSc = tableView.getItems().get(i);
            totalTaxableAmt = totalTaxableAmt + Double.parseDouble(CmpTRowDTOSoToSc.getTaxable_amt());

        }
        System.out.println("totalTaxableAmt discountPropotionalCalculation :: " + totalTaxableAmt);

        Double rowDisc = 0.0;
        //get data of netamount, tax per,and row taxable amount from cmptrow
        for (CmpTRowDTOSoToSc CmpTRowDTOSoToSc : tableView.getItems()) {
            System.out.println("getFinal_dis_amt--->" + CmpTRowDTOSoToSc.getFinal_dis_amt());
            rowDisc = Double.parseDouble(CmpTRowDTOSoToSc.getFinal_dis_amt());
            System.out.println("netAmt--->" + CmpTRowDTOSoToSc.getNet_amount());
            netAmt = Double.parseDouble(CmpTRowDTOSoToSc.getNet_amount());
            System.out.println("netAmt--->" + CmpTRowDTOSoToSc.getTax());
            r_tax_per = Double.parseDouble(CmpTRowDTOSoToSc.getTax());
            rowTaxableAmt = Double.parseDouble(CmpTRowDTOSoToSc.getTaxable_amt());
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
            CmpTRowDTOSoToSc.setNet_amount(String.format("%.2f", netAmt));
            CmpTRowDTOSoToSc.setTotal_taxable_amt(String.valueOf(totalTaxableAmtAdditional));
            System.out.println("Total Tax Amt--->" + total_tax_amt);
            CmpTRowDTOSoToSc.setIgst(String.valueOf(total_tax_amt));
            CmpTRowDTOSoToSc.setCgst(String.valueOf(total_tax_amt / 2));
            CmpTRowDTOSoToSc.setSgst(String.valueOf(total_tax_amt / 2));
            CmpTRowDTOSoToSc.setFinal_tax_amt(String.valueOf(total_tax_amt));
            CmpTRowDTOSoToSc.setFinal_dis_amt(String.valueOf(rowDisc));
            CmpTRowDTOSoToSc.setInvoice_dis_amt(String.valueOf(rowDisPropAmt));

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


    public static void additionalChargesCalculation(String additionalCharges, TableView<CmpTRowDTOSoToSc> tableView, TableCellCallback<Object[]> callback) {

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
                CmpTRowDTOSoToSc CmpTRowDTOSoToSc = tableView.getItems().get(i);
                totalTaxableAmt = totalTaxableAmt + Double.parseDouble(CmpTRowDTOSoToSc.getTotal_taxable_amt());

            }
            for (CmpTRowDTOSoToSc CmpTRowDTOSoToSc : tableView.getItems()) {

                System.out.println("row ->" + CmpTRowDTOSoToSc.toString());
                netAmt = Double.parseDouble(CmpTRowDTOSoToSc.getNet_amount());
                System.out.println("netAMout >> : " + netAmt);
                r_tax_per = Double.parseDouble(CmpTRowDTOSoToSc.getTax());
                rowTaxableAmt = Double.parseDouble(CmpTRowDTOSoToSc.getTotal_taxable_amt());
                System.out.println("rowTaxableAmt >> : " + rowTaxableAmt);
                System.out.println("GRoss AMT " + CmpTRowDTOSoToSc.getGross_amount());


                totalDisPropAmt = additionalChargeAmt;
                rowDisPropAmt = (rowTaxableAmt * totalDisPropAmt) / totalTaxableAmt;
                rowTaxableAmt = rowTaxableAmt + rowDisPropAmt;

                if (r_tax_per > 0) {
                    total_tax_amt = (r_tax_per / 100) * (rowTaxableAmt);
                    netAmt = rowTaxableAmt + total_tax_amt;
                }
//                CmpTRowDTOSoToSc.setGross_amount();

                CmpTRowDTOSoToSc.setNet_amount(String.format("%.3f", netAmt));
                CmpTRowDTOSoToSc.setCgst(String.valueOf(total_tax_amt / 2));
                CmpTRowDTOSoToSc.setSgst(String.valueOf(total_tax_amt / 2));
                CmpTRowDTOSoToSc.setFinal_tax_amt(String.valueOf(total_tax_amt));


            }

        }


//        Double additionalChargeAmt = 0.0;//store data textfield string to double

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
//                CmpTRowDTOSoToSc CmpTRowDTOSoToSc = tableView.getItems().get(i);
//                totalTaxableAmt = totalTaxableAmt + Double.parseDouble(CmpTRowDTOSoToSc.getTotal_taxable_amt());
//
//            }
//            for (CmpTRowDTOSoToSc CmpTRowDTOSoToSc : tableView.getItems()) {
//
//                netAmt = Double.parseDouble(CmpTRowDTOSoToSc.getNet_amount());
//                r_tax_per = Double.parseDouble(CmpTRowDTOSoToSc.getTax());
//                rowTaxableAmt = Double.parseDouble(CmpTRowDTOSoToSc.getTotal_taxable_amt());
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
//                CmpTRowDTOSoToSc.setNet_amount(String.format("%.3f", netAmt));
//                CmpTRowDTOSoToSc.setCgst(String.valueOf(total_tax_amt / 2));
//                CmpTRowDTOSoToSc.setSgst(String.valueOf(total_tax_amt / 2));
//                CmpTRowDTOSoToSc.setFinal_tax_amt(String.valueOf(total_tax_amt));
//
//
//            }
//
//        }
        //call calculate gst function
        calculateGst(tableView, callback);

    }


}






