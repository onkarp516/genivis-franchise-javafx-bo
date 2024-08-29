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
import com.opethic.genivis.dto.*;
import com.opethic.genivis.dto.add_charges.CustomAddChargesDTO;
import com.opethic.genivis.dto.cmp_t_row.BatchWindowTableDTO;
import com.opethic.genivis.dto.pur_invoice.LevelAForPurInvoice;
import com.opethic.genivis.dto.pur_invoice.PurInvoiceCommunicator;
import com.opethic.genivis.dto.pur_invoice.PurchaseInvoiceTable;
import com.opethic.genivis.dto.pur_invoice.PurchaseOrderTable;
import com.opethic.genivis.dto.reqres.PurTranxToProductRedirectionDTO;
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
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import org.apache.http.util.TextUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URL;
import java.net.http.HttpResponse;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static com.opethic.genivis.utils.FxmFileConstants.PURCHASE_CHALLAN_LIST_SLUG;
import static com.opethic.genivis.utils.Globals.mapToStringforFormData;
import static javafx.scene.control.PopupControl.USE_COMPUTED_SIZE;
import static javafx.scene.control.PopupControl.USE_PREF_SIZE;


public class PurchaseChallanController implements Initializable {
    @FXML
    private TextField tfPurChallLedgerName, tfPurChallChallanNo, tfPurChallPurSerialNo;
    @FXML
    private Button btnPurChallChooseFileButton;

    @FXML
    private HBox topFirstHboxId, topSecondHboxId, pcBottomMain;
    @FXML
    private VBox totalCalBox, pcBottomFirstV, pcBottomSecondV, pcBottomThirdV, pcTotalMainDiv, pcTotalMainInnerDiv;

    @FXML
    private TableView<PurchaseInvoiceTable> tblvPurChallCmpTRow;
    @FXML
    private BorderPane bpPurChallRootPane;
    private Boolean isRedirect=false;
    @FXML
    private TableColumn<PurchaseInvoiceTable, String> tblcPurChallCmpTRowSrNo, tblcPurChallCmpTRowParticulars, tblcPurChallCmpTRowPacking, tblcPurChallCmpTRowUnit, tblcPurChallCmpTRowBatchNo, tblcPurChallCmpTRowQuantity, tblcPurChallCmpTRowRate,
            tblcPurChallCmpTRowGrossAmt, tblcPurChallCmpTRowDisPer, tblcPurChallCmpTRowDisAmt, tblcPurChallCmpTRowTaxPer, tblcPurChallCmpTRowNetAmt, tblcPurChallCmpTRowLevelA, tblcPurChallCmpTRowLevelB,
            tblcPurChallCmpTRowLevelC, tblcPurChallCmpTRowFreeQty, tblcPurChallCmpTRowAction;
    private ObservableList<CmpTRowDTO> itemList;
    private ToggleGroup toggleGroup = new ToggleGroup();


    @FXML
    private ComboBox<CommonDTO> cmbPurChallSupplierGST;

    private int selectedRowIndex;
    private String SelectedLedgerId;

    private Integer purChallEditId = -1;
    @FXML
    private Label lblOrderValue;

    @FXML
    private TableView<GstDTO> tblvPurChallGST;
    @FXML
    private TableColumn<GstDTO, String> tblcPurChallGST, tblcPurChallCGST, tblcPurChallSGST, tblcPurChallIGST;
    @FXML
    private Label lblPurChallRoundOff, lblPurChallFreeQty, lblPurChallTotalQty, lblPurChallTotalBillAmount, lblPurChallGrossTotal, lblPurChallTotalTax,
            lblPurChallTotalTaxableAmount, lblPurChallTotalDiscount;
    @FXML
    private CheckBox chbPurChallRoundOff;

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
    private Button btnPurChallFinalSubmit, btnPurChallFinalCancel, btnPurChallFinalModify, btnPurchaseChallanAddCharges;
    DecimalFormat decimalFormat = new DecimalFormat("0.#");

    @FXML
    private TabPane tranxPurChallanTabPane;
    @FXML
    private Tab tabPurChallanLedger, tabPurChallanProduct, tabPurChallanBatch;

    @FXML
    private TextField tfPurChallTranxDate, tfPurChallPurChallanDate, tfPurChallNarrtion;
    @FXML
    private ComboBox<CommonDTO> cmbPurChallPurchaAc;


    @FXML
    private Text txtPurChallanGstNo, txtPurChallanArea, txtPurChallanBank, txtPurChallanContactPerson, txtPurChallanTransportName, txtPurChallanCreditDays, txtPurChallanFssai,
            txtPurChallanLicenseNo, txtPurChallanRoute, txtPurChallProductBrand, txtPurChallProductGroup, txtPurChallProductSubGroup, txtPurChallProductCategory, txtPurChallProductHsn,
            txtPurChallProductTaxType, txtPurChallProductTaxPer, txtPurChallProductMarginPer, txtPurChallProductCost, txtPurChallProductShelfId, txtPurChallProductMinStock, txtPurChallProductMaxStock,
            txtPurChallBatchBatch, txtPurChallBatchMRP, txtPurChallBatchFSRMH, txtPurChallBatchSaleRate, txtPurChallBatchHMfgDt, txtPurChallBatchPurRate, txtPurChallBatchFSRAI,
            txtPurChallBatchExpDt, txtPurChallBatchDisPer, txtPurChallBatchCSRMH, txtPurChallBatchAvlStk, txtPurChallBatchDisAmt, txtPurChallBatchCSRAI;

    Long stateCode = 0L, CompanyStateCode = 0L;

    // Array to hold focusable nodes
    private Node[] focusableNodes;

    ArrayList<Double> netAmountList;

    private JsonObject jsonObject1 = null;
    private JsonObject jsonObject = null;
    Integer batchIdToDelete;

    @FXML
    private TableView<SalesOrderSupplierDetailsDTO> tvPurChallanSupplierDetails;
    @FXML
    private TableColumn<SalesOrderSupplierDetailsDTO, String> tcPCInvPrdHistSupplierName, tcPCInvPrdHistInvNo,
            tcPCInvPrdHistInvDate, tcPCInvPrdHistBatch, tcPCInvPrdHistMRP, tcPCInvPrdHistQty,
            tcPCInvPrdHistRate, tcPCInvPrdHistCost, tcPCInvPrdHistDisPer, tcPCInvPrdHistDisAmt;
    String message = "";

    @FXML
    private Label tranxDateLabel;


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

    List<TranxPurRowResEditDTO> rowListDTOS = new ArrayList<>();
    private JsonArray deletedRows = new JsonArray();

    @FXML
    private HBox hbPurChallGstTotal;


    @FXML

    private TextField tfPurChallAllRowDisPer, tfPurChallAllRowDisAmt, tfPurchaseChallanAddChgAmt, tfPurChallChooseFileText;
    @FXML
    private Label lblPurChallTotalSGST, lblPurChallTotalCGST;

    List<CustomAddChargesDTO> listEdit = new ArrayList<>();
    private PurchaseOrderDTO purchaseOrderDTO;
    List<CustomAddChargesDTO> addChargesDTOList = new ArrayList<>();
    private final List<AdditionalCharge> addChargesResLst = new ArrayList<>();

    private PurTranxToProductRedirectionDTO prdRedDTO = null;
    private Integer rediCurrIndex = -1;
    private Boolean isProductRed = false;
    private Boolean isLedgerRed = false;

    private Integer purPrdRedId = -1;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        // Css Files Redirected  as Per Screen resolution
        resposiveScreenCss();
        tvPurChallanSupplierDetails.setFocusTraversable(false);
        tblvPurChallCmpTRow.setFocusTraversable(false);
        tblvPurChallGST.setFocusTraversable(false);

        tvPurChallanSupplierDetails.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);

        // GST Table Design and Data Set
        gstTableDesign();

        // autofocus on TranxDate
        Platform.runLater(() -> tfPurChallLedgerName.requestFocus());

        //Ledger Name Validations
        tfLedgerNameValidation();

        //challan Number Duplicate Check
        tfChallanNoValidatition();

        // Enter Functionality Method
        initialEnterMethod();

        // To Redirect the PurChallan List page
//        redirectToListScreen();

        sceneInitilization();

        showDropDownOnKeyPress(cmbPurChallSupplierGST);

        // Edit Scenario Getting the Id
        purChallEditId = PurchaseChallanListController.getPurchaseId();
        PurchaseChallanListController.purchaseChallanid = -1;
        if (purChallEditId > 0) {
            getPurchaseChallanById(purChallEditId);
        }

        // Table Initializtion for Create & Edit
        tableInitiliazation();

        tfPurChallLedgerName.setOnMouseClicked(event -> {
            handleTfLedgerName();
        });

        tfPurChallLedgerName.setOnKeyTyped(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                handleTfLedgerName();
            }
        });

        btnPurchaseChallanAddCharges.setOnAction(e -> {
            onClickAdditionalCharges();
        });

        // Date Format In TextField Code and Date set validation for Edit and Create
        dateSetValidation();

        // Auto Set of Purchase Serial Number
        if (purChallEditId > 0) {
            tfPurChallPurSerialNo.setText(invoice_data_map.get("purchase_sr_no"));
        } else {
            getPurChallSrNumber();
        }

        // By Ledger Id Setting the Ledger Name And Gst Number
        if (purChallEditId > 0) {
            tfPurChallLedgerName.setText(ledgersById(Long.valueOf(invoice_data_map.get("supplierId"))));
            if (supplierGSTINList != null && !supplierGSTINList.isEmpty()) {
                cmbPurChallSupplierGST.setItems(supplierGSTINList);
                cmbPurChallSupplierGST.setValue(supplierGSTINList.get(0));
            }
        }

        // As Per Discount Fields Calling the ProportionalDiscount Calculation method in Edit Scenario
        discPropDataSetInEdit();

        //Purchase Accounts Set In ComboBox
        getPurchaseAccounts();

        // To call PurChallan Create & Update API and To Redirect the PurChallan List Page
        createUpdateCall();

        // Responsive of Common Product Row table
        responsiveCmpTable();

        // RoundOff Calculations Method
        chbPurChallRoundOff.setOnAction(e -> {
            roundOffCalculations();
        });
//        tfInvoiceDateValidation();

        //confirmation popup if invoice date is less than current date
        CommonFunctionalUtils.tranxDateGreaterThanCurrentDate1(tfPurChallPurChallanDate, cmbPurChallPurchaAc);

        // Add Charges Field Focus Disable and Editable False
        tfPurchaseChallanAddChgAmt.setEditable(false);
        tfPurchaseChallanAddChgAmt.setFocusTraversable(false);

    }

    public void setPurChallDataToProduct() {

        String tranxDate = Communicator.text_to_date.fromString(tfPurChallTranxDate.getText()).toString();
        String ledgrId = ledger_id;
        String gstNum = cmbPurChallSupplierGST.getId();
        String purSerNum = tfPurChallPurSerialNo.getText();
        String challanNo = tfPurChallChallanNo.getText();
        String purAcc = cmbPurChallPurchaAc.getId();
        String challanDate = Communicator.text_to_date.fromString(tfPurChallPurChallanDate.getText()).toString();

        com.opethic.genivis.dto.reqres.pur_tranx.PurTranxToProductRedirectionDTO mData = new com.opethic.genivis.dto.reqres.pur_tranx.PurTranxToProductRedirectionDTO();
        mData.setRedirect(FxmFileConstants.PURCHASE_CHALLAN_CREATE_SLUG);
        mData.setTranxDate(tranxDate);
        mData.setLedegrId(ledgrId);
        mData.setGstNum(gstNum);
        mData.setPurSerNum(purSerNum);
        mData.setChallanNo(challanNo);
        mData.setPurAcc(purAcc);
        mData.setChallanDate(challanDate);
        mData.setLedgerRed(isLedgerRed);
        mData.setProductRed(isProductRed);
        mData.setTranxType("purchase");
        mData.setPurChallEditId(purChallEditId);

        mData.setRediPrdCurrIndex(rediCurrIndex);
        List<PurchaseInvoiceTable> currentItems = new ArrayList<>(tblvPurChallCmpTRow.getItems());
        List<PurchaseInvoiceTable> list = new ArrayList<>(currentItems);

        mData.setRowData(list);

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


    public void setRedirectData(Object inObj) {
        com.opethic.genivis.dto.reqres.pur_tranx.PurTranxToProductRedirectionDTO franchiseDTO = (com.opethic.genivis.dto.reqres.pur_tranx.PurTranxToProductRedirectionDTO) inObj;
        Integer redirectIndex = franchiseDTO.getRediPrdCurrIndex();
        String tranxDate = Communicator.text_to_date.fromString(tfPurChallTranxDate.getText()).toString();
        String ledgrId = ledger_id;
        String gstNum = cmbPurChallSupplierGST.getId();
        String purSerNum = tfPurChallPurSerialNo.getText();
        String challanNo = tfPurChallChallanNo.getText();
        String purAcc = cmbPurChallPurchaAc.getId();
        String challanDate = Communicator.text_to_date.fromString(tfPurChallPurChallanDate.getText()).toString();

        purPrdRedId=franchiseDTO.getPurChallEditId();
        System.out.println("purchase_id----->"+purPrdRedId);
        tfPurChallChallanNo.setText(franchiseDTO.getChallanNo());
        ledger_id = franchiseDTO.getLedegrId();
        tfPurChallPurSerialNo.setText(franchiseDTO.getPurSerNum());
        tfPurChallLedgerName.setText(ledgersById(Long.valueOf(ledger_id)));
        int cnt = 0, index = 0;
        tblvPurChallCmpTRow.getItems().clear();
        //  purChallEditId = franchiseDTO.getPurChallEditId();
        isRedirect=franchiseDTO.getRedirection();
        PurInvoiceCommunicator.levelAForPurInvoiceObservableList.clear();
        System.out.println(" Called >> " + supplierGSTINList.size());
        System.out.println(" Called ledger_id >> " + ledger_id);
        tfPurChallLedgerName.setText(ledgersById(Long.valueOf(ledger_id)));
        if (supplierGSTINList != null && !supplierGSTINList.isEmpty()) {
            cmbPurChallSupplierGST.setItems(supplierGSTINList);
            cmbPurChallSupplierGST.setValue(supplierGSTINList.get(0));
        }  System.out.println(" Called >> " + supplierGSTINList.size());
        System.out.println(" Called ledger_id >> " + ledger_id);
        tfPurChallLedgerName.setText(ledgersById(Long.valueOf(ledger_id)));
        if (supplierGSTINList != null && !supplierGSTINList.isEmpty()) {
            cmbPurChallSupplierGST.setItems(supplierGSTINList);
            cmbPurChallSupplierGST.setValue(supplierGSTINList.get(0));
        }
        if (purPrdRedId > 0) {
            btnPurChallFinalSubmit.setText("Update");
            btnPurChallFinalModify.setVisible(false);
        }
        if (franchiseDTO.getProductRed() == true) {
            Long productId = Long.valueOf(franchiseDTO.getRedirectProductId());
            int rindex = franchiseDTO.getRediPrdCurrIndex();
            JsonObject obj = productRedirectById(productId);

            for (PurchaseInvoiceTable purchaseInvoiceTable : franchiseDTO.getRowData()) {
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
                    purchaseInvoiceTable.setB_expiry("");
                    purchaseInvoiceTable.setManufacturing_date("");
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
                tblvPurChallCmpTRow.getItems().addAll(purchaseInvoiceTable);
                PurchaseInvoiceCalculation.rowCalculationForPurcInvoice(index, tblvPurChallCmpTRow, callback);
                PurchaseInvoiceCalculation.calculateGst(tblvPurChallCmpTRow, callback);
//                calculateGst(tableView, callback);

                List<LevelAForPurInvoice> levelAForPurInvoiceList = com.opethic.genivis.controller.tranx_purchase.ProductUnitsPacking.getAllProductUnitsPackingFlavour(purchaseInvoiceTable.getProduct_id());
                ObservableList<LevelAForPurInvoice> observableLevelAList = FXCollections.observableArrayList(levelAForPurInvoiceList);
                if (index >= 0 && index < PurInvoiceCommunicator.levelAForPurInvoiceObservableList.size()) {
                    PurInvoiceCommunicator.levelAForPurInvoiceObservableList.set(index, observableLevelAList);

                    tblvPurChallCmpTRow.getItems().get(index).setLevelA(null);
                    tblvPurChallCmpTRow.getItems().get(index).setLevelA(levelAForPurInvoiceList.get(0).getLabel());

                    for (LevelAForPurInvoice levelAForPurInvoice : PurInvoiceCommunicator.levelAForPurInvoiceObservableList.get(index)) {
                        if (tblvPurChallCmpTRow.getItems().get(index).getLevelA_id().equals(levelAForPurInvoice.getValue())) {
                            tblvPurChallCmpTRow.getItems().get(index).setLevelA(null);
                            tblvPurChallCmpTRow.getItems().get(index).setLevelA(levelAForPurInvoice.getLabel());
                        }
                    }

                } else {
                    PurInvoiceCommunicator.levelAForPurInvoiceObservableList.add(observableLevelAList);
                }

                cnt++;
                index++;
            }
        }

        int inx = 0;
        // tblvPurChallCmpTRow.getItems().clear();
        if (franchiseDTO.getLedgerRed() == true) {
            for (PurchaseInvoiceTable rowData : franchiseDTO.getRowData()) {


                System.out.println("String.valueOf(rowData.getRowData().get(0))" + rowData.getProduct_id());
//                mData1.setProduct_id(rowData.getProduct_id());
//                mData1.setParticulars(rowData.getParticulars());
//                mData1.setPackages(rowData.getPackages());
                List<LevelAForPurInvoice> levelAForPurInvoiceList = com.opethic.genivis.controller.tranx_purchase.ProductUnitsPacking.getAllProductUnitsPackingFlavour(rowData.getProduct_id());
                ObservableList<LevelAForPurInvoice> observableLevelAList = FXCollections.observableArrayList(levelAForPurInvoiceList);
                if (index >= 0 && index < PurInvoiceCommunicator.levelAForPurInvoiceObservableList.size()) {
                    PurInvoiceCommunicator.levelAForPurInvoiceObservableList.set(index, observableLevelAList);

                    tblvPurChallCmpTRow.getItems().get(index).setLevelA(null);
                    tblvPurChallCmpTRow.getItems().get(index).setLevelA(levelAForPurInvoiceList.get(0).getLabel());

                    for (LevelAForPurInvoice levelAForPurInvoice : PurInvoiceCommunicator.levelAForPurInvoiceObservableList.get(index)) {
                        if (tblvPurChallCmpTRow.getItems().get(index).getLevelA_id().equals(levelAForPurInvoice.getValue())) {
                            tblvPurChallCmpTRow.getItems().get(index).setLevelA(null);
                            tblvPurChallCmpTRow.getItems().get(index).setLevelA(levelAForPurInvoice.getLabel());
                        }
                    }

                } else {
                    System.out.println("Index else >> : " + index);
                    PurInvoiceCommunicator.levelAForPurInvoiceObservableList.add(observableLevelAList);
                }
                rowData.setLedger_id(ledger_id);


                tblvPurChallCmpTRow.getItems().add(rowData);
                PurchaseInvoiceCalculation.rowCalculationForPurcInvoice(index, tblvPurChallCmpTRow, callback);
                PurchaseInvoiceCalculation.calculateGst(tblvPurChallCmpTRow, callback);
                inx++;
            }
        }

        Platform.runLater(() -> {
            TableColumn<PurchaseInvoiceTable, ?> colName = tblvPurChallCmpTRow.getColumns().get(1);
            tblvPurChallCmpTRow.edit(redirectIndex, colName);
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
        double widthInParent = tranxDateLabel.getBoundsInParent().getWidth();
        // For Responsive Resolutions Css Style Apply
        double height = Screen.getPrimary().getBounds().getHeight();
        double width = Screen.getPrimary().getBounds().getWidth();
        if (width >= 992 && width <= 1024) {
            topFirstHboxId.setSpacing(10);
            topSecondHboxId.setSpacing(10);
            pcTotalMainDiv.setSpacing(10);
            pcTotalMainInnerDiv.setSpacing(8);
            // Set preferred widths as percentages of the HBox width
            pcBottomFirstV.prefWidthProperty().bind(pcBottomMain.widthProperty().multiply(0.53));
            pcBottomSecondV.prefWidthProperty().bind(pcBottomMain.widthProperty().multiply(0.25));
            pcBottomThirdV.prefWidthProperty().bind(pcBottomMain.widthProperty().multiply(0.22));
//            so2cBottomMain.setPrefHeight(150);
            tblvPurChallGST.getStylesheets().add(GenivisApplication.class.getResource("ui/css/purchase_challan_css/gst_table_css/gst_table_992_1024.css").toExternalForm());
            tvPurChallanSupplierDetails.getStylesheets().add(GenivisApplication.class.getResource("ui/css/purchase_challan_css/supplier_table_css/supplier_table_992_1024.css").toExternalForm());
            bpPurChallRootPane.getStylesheets().add(GenivisApplication.class.getResource("ui/css/TranxCommonCssStyles/tranxCommonCssStyle1.css").toExternalForm());
        } else if (width >= 1025 && width <= 1280) {
            topFirstHboxId.setSpacing(10);
            topSecondHboxId.setSpacing(10);
            pcTotalMainDiv.setSpacing(10);
            pcTotalMainInnerDiv.setSpacing(8);
            // Set preferred widths as percentages of the HBox width
            pcBottomFirstV.prefWidthProperty().bind(pcBottomMain.widthProperty().multiply(0.56));
            pcBottomSecondV.prefWidthProperty().bind(pcBottomMain.widthProperty().multiply(0.22));
            pcBottomThirdV.prefWidthProperty().bind(pcBottomMain.widthProperty().multiply(0.22));
//            so2cBottomMain.setPrefHeight(150);
            tblvPurChallGST.getStylesheets().add(GenivisApplication.class.getResource("ui/css/purchase_challan_css/gst_table_css/gst_table_1025_1280.css").toExternalForm());
            tvPurChallanSupplierDetails.getStylesheets().add(GenivisApplication.class.getResource("ui/css/purchase_challan_css/supplier_table_css/supplier_table_1025_1280.css").toExternalForm());
            bpPurChallRootPane.getStylesheets().add(GenivisApplication.class.getResource("ui/css/TranxCommonCssStyles/tranxCommonCssStyle2.css").toExternalForm());
        } else if (width >= 1281 && width <= 1368) {
            topFirstHboxId.setSpacing(12);
            topSecondHboxId.setSpacing(12);
            pcTotalMainDiv.setSpacing(12);
            pcTotalMainInnerDiv.setSpacing(10);
            // Set preferred widths as percentages of the HBox width
            pcBottomFirstV.prefWidthProperty().bind(pcBottomMain.widthProperty().multiply(0.6));
            pcBottomSecondV.prefWidthProperty().bind(pcBottomMain.widthProperty().multiply(0.20));
            pcBottomThirdV.prefWidthProperty().bind(pcBottomMain.widthProperty().multiply(0.20));
//            so2cBottomMain.setPrefHeight(150);
            tblvPurChallGST.getStylesheets().add(GenivisApplication.class.getResource("ui/css/purchase_challan_css/gst_table_css/gst_table_1281_1368.css").toExternalForm());
            tvPurChallanSupplierDetails.getStylesheets().add(GenivisApplication.class.getResource("ui/css/purchase_challan_css/supplier_table_css/supplier_table_1281_1368.css").toExternalForm());
            bpPurChallRootPane.getStylesheets().add(GenivisApplication.class.getResource("ui/css/TranxCommonCssStyles/tranxCommonCssStyle3.css").toExternalForm());
        } else if (width >= 1369 && width <= 1400) {
            topFirstHboxId.setSpacing(15);
            topSecondHboxId.setSpacing(15);
            pcTotalMainDiv.setSpacing(15);
            pcTotalMainInnerDiv.setSpacing(10);
            // Set preferred widths as percentages of the HBox width
            pcBottomFirstV.prefWidthProperty().bind(pcBottomMain.widthProperty().multiply(0.57));
            pcBottomSecondV.prefWidthProperty().bind(pcBottomMain.widthProperty().multiply(0.22));
            pcBottomThirdV.prefWidthProperty().bind(pcBottomMain.widthProperty().multiply(0.21));
//            so2cBottomMain.setPrefHeight(150);
            tblvPurChallGST.getStylesheets().add(GenivisApplication.class.getResource("ui/css/purchase_challan_css/gst_table_css/gst_table_1369_1400.css").toExternalForm());
            tvPurChallanSupplierDetails.getStylesheets().add(GenivisApplication.class.getResource("ui/css/purchase_challan_css/supplier_table_css/supplier_table_1369_1400.css").toExternalForm());
            bpPurChallRootPane.getStylesheets().add(GenivisApplication.class.getResource("ui/css/TranxCommonCssStyles/tranxCommonCssStyle4.css").toExternalForm());
        } else if (width >= 1401 && width <= 1440) {
            topFirstHboxId.setSpacing(15);
            topSecondHboxId.setSpacing(15);
            pcTotalMainDiv.setSpacing(15);
            pcTotalMainInnerDiv.setSpacing(10);
            // Set preferred widths as percentages of the HBox width
            pcBottomFirstV.prefWidthProperty().bind(pcBottomMain.widthProperty().multiply(0.57));
            pcBottomSecondV.prefWidthProperty().bind(pcBottomMain.widthProperty().multiply(0.22));
            pcBottomThirdV.prefWidthProperty().bind(pcBottomMain.widthProperty().multiply(0.21));
//            so2cBottomMain.setPrefHeight(150);
            tblvPurChallGST.getStylesheets().add(GenivisApplication.class.getResource("ui/css/purchase_challan_css/gst_table_css/gst_table_1401_1440.css").toExternalForm());
            tvPurChallanSupplierDetails.getStylesheets().add(GenivisApplication.class.getResource("ui/css/purchase_challan_css/supplier_table_css/supplier_table_1401_1440.css").toExternalForm());
            bpPurChallRootPane.getStylesheets().add(GenivisApplication.class.getResource("ui/css/TranxCommonCssStyles/tranxCommonCssStyle5.css").toExternalForm());
        } else if (width >= 1441 && width <= 1680) {
            topFirstHboxId.setSpacing(20);
            topSecondHboxId.setSpacing(20);
            pcTotalMainDiv.setSpacing(20);
            pcTotalMainInnerDiv.setSpacing(10);
            // Set preferred widths as percentages of the HBox width
            pcBottomFirstV.prefWidthProperty().bind(pcBottomMain.widthProperty().multiply(0.6));
            pcBottomSecondV.prefWidthProperty().bind(pcBottomMain.widthProperty().multiply(0.20));
            pcBottomThirdV.prefWidthProperty().bind(pcBottomMain.widthProperty().multiply(0.20));
//            scBottomMain.setPrefHeight(150);
            tblvPurChallGST.getStylesheets().add(GenivisApplication.class.getResource("ui/css/purchase_challan_css/gst_table_css/gst_table_1441_1680.css").toExternalForm());
            tvPurChallanSupplierDetails.getStylesheets().add(GenivisApplication.class.getResource("ui/css/purchase_challan_css/supplier_table_css/supplier_table_1441_1680.css").toExternalForm());
            bpPurChallRootPane.getStylesheets().add(GenivisApplication.class.getResource("ui/css/TranxCommonCssStyles/tranxCommonCssStyle6.css").toExternalForm());
        } else if (width >= 1681 && width <= 1920) {
            topFirstHboxId.setSpacing(20);
            topSecondHboxId.setSpacing(20);
            pcTotalMainDiv.setSpacing(20);
            pcTotalMainInnerDiv.setSpacing(10);
            // Set preferred widths as percentages of the HBox width
            pcBottomFirstV.prefWidthProperty().bind(pcBottomMain.widthProperty().multiply(0.6));
            pcBottomSecondV.prefWidthProperty().bind(pcBottomMain.widthProperty().multiply(0.18));
            pcBottomThirdV.prefWidthProperty().bind(pcBottomMain.widthProperty().multiply(0.22));
//            scBottomMain.setPrefHeight(150);
            tblvPurChallGST.getStylesheets().add(GenivisApplication.class.getResource("ui/css/gst_table.css").toExternalForm());
            tvPurChallanSupplierDetails.getStylesheets().add(GenivisApplication.class.getResource("ui/css/invoice_product_history_table.css").toExternalForm());
            bpPurChallRootPane.getStylesheets().add(GenivisApplication.class.getResource("ui/css/TranxCommonCssStyles/tranxCommonCssStyle7.css").toExternalForm());

        }
    }

    private void tfInvoiceDateValidation() {
        tfPurChallPurChallanDate.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal) {
                if (tfPurChallPurChallanDate.getText().isEmpty()) {
                    tfPurChallPurChallanDate.requestFocus();
                } else {
//                    validateInvoiceDate();

                }
            }
        });
    }

    private void validateInvoiceDate() {
        try {
            purChallanLogger.debug("Initiate the validateInvoiceDate() method");
            Map<String, String> map = new HashMap<>();
            String invoiceDate = Communicator.text_to_date.fromString(tfPurChallPurChallanDate.getText()).toString();
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
                tfPurChallPurChallanDate.requestFocus();
            }
        } catch (Exception e) {
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            String exceptionAsString = sw.toString();
            purChallanLogger.error("Exception in validateInvoiceDate:" + exceptionAsString);
        }

    }

    public void tfLedgerNameValidation() {
        tfPurChallLedgerName.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal) {
                if (tfPurChallLedgerName.getText().isEmpty()) {
                    tfPurChallLedgerName.requestFocus();
                }
            }
        });
    }

    private void dateSetValidation() {
        DateValidator.applyDateFormat(tfPurChallTranxDate);
        DateValidator.applyDateFormat(tfPurChallPurChallanDate);

        // Set the current date to the TranxDate and ChallanDate
        LocalDate currDate = LocalDate.now();
        if (purChallEditId > 0) {
            LocalDate transaction_dt = LocalDate.parse(invoice_data_map.get("transaction_dt"));
            tfPurChallTranxDate.setText(transaction_dt.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
            LocalDate invoice_dt = LocalDate.parse(invoice_data_map.get("invoice_dt"));
            tfPurChallPurChallanDate.setText(invoice_dt.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        } else {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            tfPurChallTranxDate.setText(currDate.format(formatter));
            tfPurChallPurChallanDate.setText(currDate.format(formatter));
            sceneInitilization();
        }
    }

    private void discPropDataSetInEdit() {
        if (purChallEditId > 0) {
            Double dis_per_float = (Double) edit_response_map.get("discountInPer");
            tfPurChallAllRowDisPer.setText(String.valueOf(dis_per_float));

            String discText = tfPurChallAllRowDisPer.getText();
            if (!TextUtils.isEmpty(discText)) {
                double totalTaxableAmt = 0.0;
                for (int i = 0; i < tblvPurChallCmpTRow.getItems().size(); i++) {
                    PurchaseInvoiceCalculation.rowCalculationForPurcInvoice(i, tblvPurChallCmpTRow, callback);//call row calculation function
                    PurchaseInvoiceTable purchaseInvoiceTable = tblvPurChallCmpTRow.getItems().get(i);
                    totalTaxableAmt = totalTaxableAmt + Double.parseDouble(purchaseInvoiceTable.getTaxable_amt());

                }
                double disc_per = Double.parseDouble(discText);
                Double amount = (totalTaxableAmt * disc_per) / 100;
                tfPurChallAllRowDisAmt.setText(String.valueOf(amount));
                PurchaseInvoiceCalculation.discountPropotionalCalculation(disc_per + "", "0", tfPurchaseChallanAddChgAmt.getText(), tblvPurChallCmpTRow, callback);
                if (!tfPurchaseChallanAddChgAmt.getText().isEmpty()) {
                    PurchaseInvoiceCalculation.additionalChargesCalculation(tfPurchaseChallanAddChgAmt.getText(), tblvPurChallCmpTRow, callback);
                }
            } else {
                tfPurChallAllRowDisAmt.setText("");
                PurchaseInvoiceCalculation.discountPropotionalCalculation("0", "0", tfPurchaseChallanAddChgAmt.getText(), tblvPurChallCmpTRow, callback);
                if (!tfPurchaseChallanAddChgAmt.getText().isEmpty()) {
                    PurchaseInvoiceCalculation.additionalChargesCalculation(tfPurchaseChallanAddChgAmt.getText(), tblvPurChallCmpTRow, callback);
                }
            }

            //? Calculate Tax for each Row
            PurchaseInvoiceCalculation.calculateGst(tblvPurChallCmpTRow, callback);


        }
    }

    public static void showDropDownOnKeyPress(ComboBox<?> comboBox) {
        comboBox.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.SPACE || e.getCode() == KeyCode.DOWN) {
                comboBox.show();
            }
        });
    }

    public void responsiveCmpTable() {
        tblcPurChallCmpTRowSrNo.prefWidthProperty().bind(tblvPurChallCmpTRow.widthProperty().multiply(0.02));
        tblcPurChallCmpTRowPacking.prefWidthProperty().bind(tblvPurChallCmpTRow.widthProperty().multiply(0.05));
        tblcPurChallCmpTRowUnit.prefWidthProperty().bind(tblvPurChallCmpTRow.widthProperty().multiply(0.05));
        tblcPurChallCmpTRowQuantity.prefWidthProperty().bind(tblvPurChallCmpTRow.widthProperty().multiply(0.05));
        tblcPurChallCmpTRowDisPer.prefWidthProperty().bind(tblvPurChallCmpTRow.widthProperty().multiply(0.05));
        tblcPurChallCmpTRowDisAmt.prefWidthProperty().bind(tblvPurChallCmpTRow.widthProperty().multiply(0.05));
        tblcPurChallCmpTRowTaxPer.prefWidthProperty().bind(tblvPurChallCmpTRow.widthProperty().multiply(0.04));
        tblcPurChallCmpTRowAction.prefWidthProperty().bind(tblvPurChallCmpTRow.widthProperty().multiply(0.04));
        if (tblcPurChallCmpTRowFreeQty.isVisible()) {
            tblcPurChallCmpTRowFreeQty.prefWidthProperty().bind(tblvPurChallCmpTRow.widthProperty().multiply(0.04));
            tblcPurChallCmpTRowRate.prefWidthProperty().bind(tblvPurChallCmpTRow.widthProperty().multiply(0.06));
        } else {
            tblcPurChallCmpTRowRate.prefWidthProperty().bind(tblvPurChallCmpTRow.widthProperty().multiply(0.08));
        }
        if (tblcPurChallCmpTRowLevelA.isVisible() == true && tblcPurChallCmpTRowLevelB.isVisible() == false && tblcPurChallCmpTRowLevelC.isVisible() == false) {
            tblcPurChallCmpTRowParticulars.prefWidthProperty().bind(tblvPurChallCmpTRow.widthProperty().multiply(0.29));
            tblcPurChallCmpTRowGrossAmt.prefWidthProperty().bind(tblvPurChallCmpTRow.widthProperty().multiply(0.1));
            tblcPurChallCmpTRowNetAmt.prefWidthProperty().bind(tblvPurChallCmpTRow.widthProperty().multiply(0.07));
            tblcPurChallCmpTRowBatchNo.prefWidthProperty().bind(tblvPurChallCmpTRow.widthProperty().multiply(0.1));
            tblcPurChallCmpTRowLevelA.prefWidthProperty().bind(tblvPurChallCmpTRow.widthProperty().multiply(0.06));
        } else if (tblcPurChallCmpTRowLevelA.isVisible() == true && tblcPurChallCmpTRowLevelB.isVisible() == true && tblcPurChallCmpTRowLevelC.isVisible() == false) {
            tblcPurChallCmpTRowLevelA.prefWidthProperty().bind(tblvPurChallCmpTRow.widthProperty().multiply(0.05));
            tblcPurChallCmpTRowParticulars.prefWidthProperty().bind(tblvPurChallCmpTRow.widthProperty().multiply(0.28));
            tblcPurChallCmpTRowGrossAmt.prefWidthProperty().bind(tblvPurChallCmpTRow.widthProperty().multiply(0.08));
            tblcPurChallCmpTRowNetAmt.prefWidthProperty().bind(tblvPurChallCmpTRow.widthProperty().multiply(0.06));
            tblcPurChallCmpTRowBatchNo.prefWidthProperty().bind(tblvPurChallCmpTRow.widthProperty().multiply(0.06));
            tblcPurChallCmpTRowLevelB.prefWidthProperty().bind(tblvPurChallCmpTRow.widthProperty().multiply(0.05));
        } else if (tblcPurChallCmpTRowLevelA.isVisible() == true && tblcPurChallCmpTRowLevelB.isVisible() == true && tblcPurChallCmpTRowLevelC.isVisible() == true) {
            tblcPurChallCmpTRowParticulars.prefWidthProperty().bind(tblvPurChallCmpTRow.widthProperty().multiply(0.245));
            tblcPurChallCmpTRowGrossAmt.prefWidthProperty().bind(tblvPurChallCmpTRow.widthProperty().multiply(0.07));
            tblcPurChallCmpTRowNetAmt.prefWidthProperty().bind(tblvPurChallCmpTRow.widthProperty().multiply(0.06));
            tblcPurChallCmpTRowBatchNo.prefWidthProperty().bind(tblvPurChallCmpTRow.widthProperty().multiply(0.06));
            tblcPurChallCmpTRowLevelA.prefWidthProperty().bind(tblvPurChallCmpTRow.widthProperty().multiply(0.05));
            tblcPurChallCmpTRowLevelB.prefWidthProperty().bind(tblvPurChallCmpTRow.widthProperty().multiply(0.05));
            tblcPurChallCmpTRowLevelC.prefWidthProperty().bind(tblvPurChallCmpTRow.widthProperty().multiply(0.05));
        } else {
            tblcPurChallCmpTRowParticulars.prefWidthProperty().bind(tblvPurChallCmpTRow.widthProperty().multiply(0.30));
            tblcPurChallCmpTRowGrossAmt.prefWidthProperty().bind(tblvPurChallCmpTRow.widthProperty().multiply(0.08));
            tblcPurChallCmpTRowNetAmt.prefWidthProperty().bind(tblvPurChallCmpTRow.widthProperty().multiply(0.08));
            tblcPurChallCmpTRowBatchNo.prefWidthProperty().bind(tblvPurChallCmpTRow.widthProperty().multiply(0.08));
        }

    }

    private void gstTableDesign() {
        tblvPurChallGST.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
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
        tblcPurChallIGST.setCellValueFactory(cellData -> cellData.getValue().sgstProperty());
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
    }

    private void initialEnterMethod() {
        bpPurChallRootPane.addEventFilter(KeyEvent.KEY_PRESSED, (KeyEvent event) -> {
            if (event.getCode() == KeyCode.ENTER) {

                if (event.getTarget() instanceof Button targetButton && targetButton.getText().equalsIgnoreCase("modify")) {
                    targetButton.getText();
                } else if (event.getTarget() instanceof Button targetButton && targetButton.getText().equalsIgnoreCase("submit")) {
                    targetButton.getText();
                } else if (event.getTarget() instanceof Button targetButton && targetButton.getText().equalsIgnoreCase("update")) {
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
            } else if (event.getCode() == KeyCode.X && event.isControlDown()) {
                btnPurChallFinalCancel.fire();
            } else if (event.getCode() == KeyCode.E && event.isControlDown()) {
                btnPurChallFinalModify.fire();
            } else if (event.getCode() == KeyCode.S && event.isControlDown()) {
                btnPurChallFinalSubmit.fire();
            }
        });
    }

    //    private void redirectToListScreen() {
//        btnPurChallFinalCancel.setOnAction(event -> {
//            if (purChallEditId > 0) {
//                AlertUtility.AlertConfirmation(AlertUtility.alertTypeConfirmation, "Are you sure want to Cancel and go to list ?", input -> {
//                    if (input == 1) {
//                        GlobalController.getInstance().addTabStatic(PURCHASE_CHALLAN_LIST_SLUG, false);
//                    }
//                });
//            } else {
//
//                AlertUtility.AlertConfirmation(AlertUtility.alertTypeConfirmation, "Are you sure want to Cancel?", input -> {
//                    if (input == 1) {
//                        clearLedAndProdInfo();
//                    }
//                });
//            }
//        });
//
//        // To Redirect the PurChallan List page
//        btnPurChallFinalModify.setOnAction(event -> {
//            AlertUtility.AlertConfirmation(AlertUtility.alertTypeConfirmation, "Are you sure?", input -> {
//                if (input == 1) {
//                    GlobalController.getInstance().addTabStatic(PURCHASE_CHALLAN_LIST_SLUG, false);
//                }
//            });
//        });
//    }
//? On Clicking on the Modify Button From Create or Edit page redirect Back to List page
    public void backToListModify() {
        purChallEditId = null;
        AlertUtility.AlertConfirmation(AlertUtility.alertTypeConfirmation, LedgerMessageConsts.msgGoList, input -> {
            if (input == 1) {
                SingleInputDialogs.productId = "";
                SingleInputDialogs.ledId = "";
                if (batchIdToDelete != null && !batchIdToDelete.equals("")) {
                    deleteProductBatch(batchIdToDelete);
                }
                GlobalController.getInstance().addTabStatic(PURCHASE_CHALLAN_LIST_SLUG, false);
            }
        });
    }

    public void backToList() {
//        purChallEditId = null;
        if (purChallEditId > 0) {
            AlertUtility.AlertConfirmation(AlertUtility.alertTypeConfirmation, LedgerMessageConsts.msgGoList, input -> {
                if (input == 1) {
                    if (batchIdToDelete != null && !batchIdToDelete.equals("")) {
                        deleteProductBatch(batchIdToDelete);
                    }
                    GlobalController.getInstance().addTabStatic(PURCHASE_CHALLAN_LIST_SLUG, false);
                }
            });
        } else {
            AlertUtility.AlertConfirmation(AlertUtility.alertTypeConfirmation, LedgerMessageConsts.msgConfirmationOnCancel + tfPurChallChallanNo.getText() + LedgerMessageConsts.msgInvoiceNumber, input -> {
                if (input == 1) {
                    clearLedAndProdInfo();
                }
            });
        }
    }

    public void clearLedAndProdInfo() {
        tfPurChallLedgerName.setText("");
        cmbPurChallSupplierGST.getSelectionModel().clearSelection();
//        tvSalesQuotCreateprodInvoiceDetails.setItems(emptyList1);
        tfPurChallNarrtion.setText("");
        tfPurChallChallanNo.setText("");
        //to Focus on Selected Product passed ProductId. on Clear pass blank
        SingleInputDialogs.productId = "";
        SingleInputDialogs.ledId = "";

        //? clear Ledger Info
        txtPurChallanGstNo.setText("");
        txtPurChallanArea.setText("");
        txtPurChallanBank.setText("");
        txtPurChallanContactPerson.setText("");
        txtPurChallanFssai.setText("");
        txtPurChallanLicenseNo.setText("");
        txtPurChallanRoute.setText("");
        txtPurChallanTransportName.setText("");
        txtPurChallanCreditDays.setText("");

        //? clear Product Info
        txtPurChallProductBrand.setText("");
        txtPurChallProductGroup.setText("");
        txtPurChallProductSubGroup.setText("");
        txtPurChallProductCategory.setText("");
        txtPurChallProductHsn.setText("");
        txtPurChallProductTaxType.setText("");
        txtPurChallProductTaxPer.setText("");
        txtPurChallProductMarginPer.setText("");
        txtPurChallProductCost.setText("");
        txtPurChallProductShelfId.setText("");
        txtPurChallProductMaxStock.setText("");
        txtPurChallProductMinStock.setText("");

        //?clear batch info
        txtPurChallBatchBatch.setText("");
        txtPurChallBatchMRP.setText("");
        txtPurChallBatchHMfgDt.setText("");
        txtPurChallBatchExpDt.setText("");
        txtPurChallBatchPurRate.setText("");
        txtPurChallBatchAvlStk.setText("");
        txtPurChallBatchDisPer.setText("");
        txtPurChallBatchDisAmt.setText("");
        txtPurChallBatchSaleRate.setText("");
        txtPurChallBatchFSRMH.setText("");
        txtPurChallBatchFSRAI.setText("");
        txtPurChallBatchCSRMH.setText("");
        txtPurChallBatchCSRAI.setText("");
        txtPurChallBatchCSRAI.setText("");

        //clear all row calculation
        tfPurChallAllRowDisPer.setText("");

        tfPurChallAllRowDisAmt.setText("");

        lblPurChallGrossTotal.setText("0.00");

        lblPurChallTotalDiscount.setText("0.00");

        lblPurChallTotalTaxableAmount.setText("0.00");
        lblPurChallTotalTax.setText("0.00");
        lblPurChallTotalBillAmount.setText("0.00");

        tblvPurChallCmpTRow.getItems().clear();// Add a new blank row if needed
        tblvPurChallCmpTRow.getItems().add(new PurchaseInvoiceTable("", "0", "1", "", "", "", "", "", "", "", "", "", "", "", "", "", "", ""));
        tfPurChallLedgerName.requestFocus();
        lblPurChallFreeQty.setText("");
        lblPurChallTotalQty.setText("");
        lblPurChallRoundOff.setText("");
        tvPurChallanSupplierDetails.getItems().clear();
        PurInvoiceCommunicator.resetFields();//new 2.0 to Clear the CMPTROW ComboBox
        tblvPurChallGST.getItems().clear();

        //? Delete Created Batch

        if (batchIdToDelete != null && !batchIdToDelete.equals("")) {
            deleteProductBatch(batchIdToDelete);
        }
    }

    public void deleteProductBatch(int id) {
        try {
            Map<String, String> map = new HashMap<>();
            map.put("id", String.valueOf(id));
            String formData = Globals.mapToStringforFormData(map);
            System.out.println("batch deletion Form DataInput " + formData);
            HttpResponse<String> response = APIClient.postFormDataRequest(formData, EndPoints.DELETE_PRODUCT_BATCH);
            String responseBody = response.body();
            JsonObject jsonObject = new Gson().fromJson(responseBody, JsonObject.class);
            String message = jsonObject.get("message").getAsString();

            if (jsonObject.get("responseStatus").getAsInt() == 200) {
            } else {
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void createUpdateCall() {
        btnPurChallFinalSubmit.setOnAction((event) -> {

            if (tfPurChallLedgerName.getText().isEmpty()) {
                AlertUtility.AlertErrorTimeout(AlertUtility.alertTypeError, "Enter Mandatory Ledger Name Field", input -> {
                    if (input == 1) {
                        tfPurChallLedgerName.requestFocus();
                    }
                });
            } else if (tfPurChallChallanNo.getText().isEmpty()) {
                AlertUtility.AlertErrorTimeout(AlertUtility.alertTypeError, "Enter Mandatory Challan No Field", input -> {
                    if (input == 1) {
                        tfPurChallLedgerName.requestFocus();
                    }
                });
            } else {
                String btnText = btnPurChallFinalSubmit.getText();
                if (btnText.equalsIgnoreCase("Submit")) {
                    if (purchaseOrderDTO == null) {

                        AlertUtility.CustomCallback callback = number -> {
                            if (number == 1) {
                                createPurchaseChallanNew();
                            } else {
                            }
                        };
                        //Normal Purchase Challan Create
                        AlertUtility.AlertConfirmation(AlertUtility.alertTypeConfirmation, LedgerMessageConsts.msgConfirmationOnSubmit + tfPurChallChallanNo.getText() + LedgerMessageConsts.msgInvoiceNumber, callback);
                    }
                } else {
                    AlertUtility.CustomCallback callback = number -> {
                        if (number == 1) {
                            createPurchaseChallanNew();
                        } else {
                        }
                    };
                    // PurchaseChallanUpdate API Call
                    AlertUtility.AlertConfirmation(AlertUtility.alertTypeConfirmation, LedgerMessageConsts.msgConfirmationOnUpdate + tfPurChallChallanNo.getText() + LedgerMessageConsts.msgInvoiceNumber, callback);
                }
            }

        });

    }

    TableCellCallback<Object[]> callback = item -> {

        tblvPurChallGST.getItems().clear();
        ObservableList<GstDTO> gstDTOObservableList = FXCollections.observableArrayList();
        ObservableList<?> item9List = (ObservableList<?>) item[9];
        for (Object obj : item9List) {
            if (obj instanceof GstDTO) {
                gstDTOObservableList.add((GstDTO) obj);
            }
        }
        tblvPurChallGST.getItems().addAll(gstDTOObservableList);

        if (!ledgerStateCode.equalsIgnoreCase(GlobalTranx.getCompanyStateCode().toString())) {
            tblcPurChallIGST.setVisible(true);
            tblcPurChallCGST.setVisible(false);
            tblcPurChallSGST.setVisible(false);
        } else {
            tblcPurChallCGST.setVisible(true);
            tblcPurChallSGST.setVisible(true);
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
        purchase_challan_map.put("taxCalculation", jsonResult.toString());

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


        total_taxable_amt = Double.parseDouble((String) item[5]);
        lblPurChallTotalQty.setText((String) item[0]);
        lblPurChallFreeQty.setText((String) item[1]);
        lblPurChallGrossTotal.setText((String) item[3]);
        lblPurChallTotalDiscount.setText((String) item[4]);
        lblPurChallTotalTaxableAmount.setText((String) item[5]);
        lblPurChallTotalTax.setText((String) item[6]);
        lblPurChallTotalCGST.setText((String) item[7]);
        lblPurChallTotalSGST.setText((String) item[8]);

        /**
         * Info : RoundOff Calculation Method
         * @Author : Vinit Chilaka
         */
        chbPurChallRoundOff.setSelected(true);
        roundOffCalculations();

        hbPurChallGstTotal.setVisible(true);

    };
    TableCellCallback<Object[]> delCallback = item -> {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("del_id", item[0].toString());
        deletedRows.add(jsonObject);
        if (deletedRows.size() > 0) {
            String discText = tfPurChallAllRowDisPer.getText();
            if (!TextUtils.isEmpty(discText)) {
                double totalTaxableAmt = 0.0;
                for (int i = 0; i < tblvPurChallCmpTRow.getItems().size(); i++) {
                    PurchaseInvoiceCalculation.rowCalculationForPurcInvoice(i, tblvPurChallCmpTRow, callback);//call row calculation function
                    PurchaseInvoiceTable purchaseInvoiceTable = tblvPurChallCmpTRow.getItems().get(i);
                    totalTaxableAmt = totalTaxableAmt + Double.parseDouble(purchaseInvoiceTable.getTaxable_amt());

                }
                double disc_per = Double.parseDouble(discText);
                Double amount = (totalTaxableAmt * disc_per) / 100;
                tfPurChallAllRowDisAmt.setText(String.valueOf(amount));
                PurchaseInvoiceCalculation.discountPropotionalCalculation(disc_per + "", "0", tfPurchaseChallanAddChgAmt.getText(), tblvPurChallCmpTRow, callback);
                if (!tfPurchaseChallanAddChgAmt.getText().isEmpty()) {
                    PurchaseInvoiceCalculation.additionalChargesCalculation(tfPurchaseChallanAddChgAmt.getText(), tblvPurChallCmpTRow, callback);
                }
            } else {
                tfPurChallAllRowDisAmt.setText("");
                PurchaseInvoiceCalculation.discountPropotionalCalculation("0", "0", tfPurchaseChallanAddChgAmt.getText(), tblvPurChallCmpTRow, callback);
                if (!tfPurchaseChallanAddChgAmt.getText().isEmpty()) {
                    PurchaseInvoiceCalculation.additionalChargesCalculation(tfPurchaseChallanAddChgAmt.getText(), tblvPurChallCmpTRow, callback);
                }
            }

            //? Calculate Tax for each Row
            PurchaseInvoiceCalculation.calculateGst(tblvPurChallCmpTRow, callback);

        }
    };


    private void roundOffCalculations() {
        Double billamt = 0.00; // local variable to store row wise  Bill amount
        Double total_bill_amt = 0.00; // local variable to store Final bill Amount without roundoff
        Double final_r_off_bill_amt = 0.00; // local variable to store Final Bill amount with round off
        Double roundOffAmt = 0.00; // local variable to store Roundoff Amount
        for (PurchaseInvoiceTable cmpTRowDTO : tblvPurChallCmpTRow.getItems()) {
            if (!cmpTRowDTO.getNet_amount().isEmpty()) {
                billamt = Double.valueOf(cmpTRowDTO.getNet_amount());
            }
            total_bill_amt += billamt;
        }
        final_r_off_bill_amt = (double) Math.round(total_bill_amt);
        roundOffAmt = final_r_off_bill_amt - total_bill_amt;

        if (chbPurChallRoundOff.isSelected()) {
            lblPurChallTotalBillAmount.setText(String.format("%.2f", final_r_off_bill_amt));
            lblPurChallRoundOff.setText(String.format("%.2f", roundOffAmt));
        } else {
            lblPurChallTotalBillAmount.setText(String.format("%.2f", total_bill_amt));
            lblPurChallRoundOff.setText("0.00");
        }

    }


    TableCellCallback<Object[]> productID_callback = item -> {
        if (item.length == 1) {
            tranxProductDetailsFun((String) item[0]);
            getSupplierListbyProductId((String) item[0]);
        } else {
            tranxBatchDetailsFun((String) item[0], (String) item[1]);
            batchIdToDelete = Integer.valueOf((String) item[1]);
            String fieldCheck = (String) item[1];
            if (!fieldCheck.isEmpty()) {
                String discText = tfPurChallAllRowDisPer.getText();
                if (!TextUtils.isEmpty(discText)) {
                    double totalTaxableAmt = 0.0;
                    for (int i = 0; i < tblvPurChallCmpTRow.getItems().size(); i++) {
                        PurchaseInvoiceCalculation.rowCalculationForPurcInvoice(i, tblvPurChallCmpTRow, callback);//call row calculation function
                        PurchaseInvoiceTable purchaseInvoiceTable = tblvPurChallCmpTRow.getItems().get(i);
                        totalTaxableAmt = totalTaxableAmt + Double.parseDouble(purchaseInvoiceTable.getTaxable_amt());

                    }
                    double disc_per = Double.parseDouble(discText);
                    Double amount = (totalTaxableAmt * disc_per) / 100;
                    tfPurChallAllRowDisAmt.setText(String.valueOf(amount));
                    PurchaseInvoiceCalculation.discountPropotionalCalculation(disc_per + "", "0", tfPurchaseChallanAddChgAmt.getText(), tblvPurChallCmpTRow, callback);
                    if (!tfPurchaseChallanAddChgAmt.getText().isEmpty()) {
                        PurchaseInvoiceCalculation.additionalChargesCalculation(tfPurchaseChallanAddChgAmt.getText(), tblvPurChallCmpTRow, callback);
                    }
                } else {
                    tfPurChallAllRowDisAmt.setText("");
                    PurchaseInvoiceCalculation.discountPropotionalCalculation("0", "0", tfPurchaseChallanAddChgAmt.getText(), tblvPurChallCmpTRow, callback);
                    if (!tfPurchaseChallanAddChgAmt.getText().isEmpty()) {
                        PurchaseInvoiceCalculation.additionalChargesCalculation(tfPurchaseChallanAddChgAmt.getText(), tblvPurChallCmpTRow, callback);
                    }
                }

                //? Calculate Tax for each Row
                PurchaseInvoiceCalculation.calculateGst(tblvPurChallCmpTRow, callback);
            }
        }
    };

    TableCellCallback<Object[]> addPrdCalbak = item -> {
        if (item != null) {
            if ((Boolean) item[0] == true) {
                rediCurrIndex = (Integer) item[1];
                String isProduct = (String) item[2];
                if (isProduct.equalsIgnoreCase("isProduct")) {
                    isProductRed = true;
                }
                setPurChallDataToProduct();
            }
        }
    };


    public void tableInitiliazation() {

        tblvPurChallCmpTRow.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tblvPurChallCmpTRow.setEditable(true);
        tblvPurChallCmpTRow.setFocusTraversable(false);

        Label headerLabel = new Label("Sr\nNo.");
        tblcPurChallCmpTRowSrNo.setGraphic(headerLabel);

        if (purChallEditId > 0) {

            int count = 1;
            int index = 0;
            for (TranxPurRowResEditDTO rowListDTO : rowListDTOS) {
                PurchaseInvoiceTable purchaseInvoiceTable = new PurchaseInvoiceTable();
                purchaseInvoiceTable.setPurchase_id(String.valueOf(purChallEditId));
                purchaseInvoiceTable.setSr_no(String.valueOf(count));
                purchaseInvoiceTable.setDetails_id(String.valueOf(rowListDTO.getDetailsId()));
                purchaseInvoiceTable.setProduct_id(String.valueOf(rowListDTO.getProductId()));
                getSupplierListbyProductId(String.valueOf(rowListDTO.getProductId()));
                tranxProductDetailsFun(String.valueOf(rowListDTO.getProductId()));
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
                purchaseInvoiceTable.setFree(String.valueOf(rowListDTO.getFreeQty()));
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
                tranxBatchDetailsFun(String.valueOf(rowListDTO.getBatchNo()), String.valueOf(rowListDTO.getbDetailsId()));
                purchaseInvoiceTable.setBatch_or_serial(String.valueOf(rowListDTO.getBatchNo()));
                purchaseInvoiceTable.setB_expiry(String.valueOf(rowListDTO.getbExpiry()));
                purchaseInvoiceTable.setB_rate(String.valueOf(rowListDTO.getbRate()));
                purchaseInvoiceTable.setB_purchase_rate(String.valueOf(rowListDTO.getPurchaseRate()));
                purchaseInvoiceTable.setIs_batch(String.valueOf(rowListDTO.getBatch()));

                purchaseInvoiceTable.setRate_a(String.valueOf(rowListDTO.getMinRateA() == null ? "" : rowListDTO.getMinRateA()));
                purchaseInvoiceTable.setRate_b(String.valueOf(rowListDTO.getMinRateB() == null ? "" : rowListDTO.getMinRateB()));
                purchaseInvoiceTable.setRate_c(String.valueOf(rowListDTO.getMinRateC() == null ? "" : rowListDTO.getMinRateB()));
                // purchaseInvoiceTable.setRate_d(String.valueOf(rowListDTO.getMinRateD()));

                purchaseInvoiceTable.setManufacturing_date(String.valueOf(rowListDTO.getManufacturingDate()));
                purchaseInvoiceTable.setMin_margin(String.valueOf(rowListDTO.getMarginPer()));

                purchaseInvoiceTable.setCosting(String.valueOf(rowListDTO.getCosting()));
                purchaseInvoiceTable.setCosting_with_tax(String.valueOf(rowListDTO.getCosting()));
                purchaseInvoiceTable.setLedger_id(String.valueOf(rowListDTO.getCosting()));
                purchaseInvoiceTable.setLedger_id(invoice_data_map.get("supplierId"));
                purchaseInvoiceTable.setTax(String.valueOf(rowListDTO.getGst()));


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

                tblvPurChallCmpTRow.getItems().addAll(purchaseInvoiceTable);
                PurchaseInvoiceCalculation.rowCalculationForPurcInvoice(index, tblvPurChallCmpTRow, callback);
                PurchaseInvoiceCalculation.calculateGst(tblvPurChallCmpTRow, callback);
//                calculateGst(tableView, callback);

                List<LevelAForPurInvoice> levelAForPurInvoiceList = ProductUnitsPacking.getAllProductUnitsPackingFlavour(purchaseInvoiceTable.getProduct_id());

                ObservableList<LevelAForPurInvoice> observableLevelAList = FXCollections.observableArrayList(levelAForPurInvoiceList);

                if (index >= 0 && index < PurInvoiceCommunicator.levelAForPurInvoiceObservableList.size()) {
                    PurInvoiceCommunicator.levelAForPurInvoiceObservableList.set(index, observableLevelAList);

                    tblvPurChallCmpTRow.getItems().get(index).setLevelA(null);
                    tblvPurChallCmpTRow.getItems().get(index).setLevelA(levelAForPurInvoiceList.get(0).getLabel());

                    for (LevelAForPurInvoice levelAForPurInvoice : PurInvoiceCommunicator.levelAForPurInvoiceObservableList.get(index)) {
                        if (tblvPurChallCmpTRow.getItems().get(index).getLevelA_id().equals(levelAForPurInvoice.getValue())) {
                            tblvPurChallCmpTRow.getItems().get(index).setLevelA(null);
                            tblvPurChallCmpTRow.getItems().get(index).setLevelA(levelAForPurInvoice.getLabel());
                        }
                    }

                } else {
                    PurInvoiceCommunicator.levelAForPurInvoiceObservableList.add(observableLevelAList);

                    tblvPurChallCmpTRow.getItems().get(index).setLevelA(null);
                    tblvPurChallCmpTRow.getItems().get(index).setLevelA(levelAForPurInvoiceList.get(0).getLabel());

                    for (LevelAForPurInvoice levelAForPurInvoice : PurInvoiceCommunicator.levelAForPurInvoiceObservableList.get(index)) {
                        if (tblvPurChallCmpTRow.getItems().get(index).getLevelA_id().equals(levelAForPurInvoice.getValue())) {
                            tblvPurChallCmpTRow.getItems().get(index).setLevelA(null);
                            tblvPurChallCmpTRow.getItems().get(index).setLevelA(levelAForPurInvoice.getLabel());
                        }
                    }
                }


                count++;
                index++;
            }
            setAddChargesInTable();
        } else {
            tblvPurChallCmpTRow.getItems().addAll(new PurchaseInvoiceTable("", "0", "1", "", "", "", "", "", "", "", "", "", "", "", "", "", "", ""));
        }

//        tblvPurChallCmpTRow.getItems().addAll(new PurchaseInvoiceTable("", "0", "1", "", "", "", "", "", "", "", "", "", "", "", "", "", "", ""));

//        tblcPurChallCmpTRowSrNo.setCellValueFactory(cellData -> cellData.getValue().sr_noProperty());

//        tblcPurChallCmpTRowSrNo.setCellValueFactory(cellData ->
//                new SimpleStringProperty(String.valueOf(tblvPurChallCmpTRow.getItems().indexOf(cellData.getValue()) + 1))
//        );
        tblcPurChallCmpTRowSrNo.setCellFactory(new LineNumbersCellFactory());
        tblcPurChallCmpTRowSrNo.setStyle("-fx-alignment: CENTER;");

        tblcPurChallCmpTRowPacking.setCellValueFactory(cellData -> cellData.getValue().packagesProperty());
        tblcPurChallCmpTRowPacking.setStyle("-fx-alignment: CENTER;");

        tblcPurChallCmpTRowLevelB.setCellValueFactory(cellData -> cellData.getValue().levelBProperty());
        tblcPurChallCmpTRowLevelB.setCellFactory(column -> new ComboBoxTableCellForLevelB("tblcPurChallCmpTRowLevelB"));

        tblcPurChallCmpTRowLevelA.setCellValueFactory(cellData -> cellData.getValue().levelAProperty());
        tblcPurChallCmpTRowLevelA.setCellFactory(column -> new ComboBoxTableCellForLevelA("tblcPurChallCmpTRowLevelA"));

        tblcPurChallCmpTRowLevelC.setCellValueFactory(cellData -> cellData.getValue().levelCProperty());
        tblcPurChallCmpTRowLevelC.setCellFactory(column -> new ComboBoxTableCellForLevelC("tblcPurChallCmpTRowLevelC"));

        tblcPurChallCmpTRowUnit.setCellValueFactory(cellData -> cellData.getValue().unitProperty());
        tblcPurChallCmpTRowUnit.setCellFactory(column -> new ComboBoxTableCellForUnit("tblcPurChallCmpTRowUnit"));

        tblcPurChallCmpTRowParticulars.setCellValueFactory(cellData -> cellData.getValue().particularsProperty());
//        tblcPurChallCmpTRowParticulars.setCellFactory(column -> new TextFieldTableCellForPurChallan("tblcPurChallCmpTRowParticulars", callback, productID_callback));
        tblcPurChallCmpTRowParticulars.setCellFactory(column -> new TextFieldTableCellForPurChallan("tblcPurChallCmpTRowParticulars", callback, productID_callback, tfPurChallNarrtion, addPrdCalbak));

        tblcPurChallCmpTRowBatchNo.setCellValueFactory(cellData -> cellData.getValue().batch_or_serialProperty());
//        tblcPurChallCmpTRowBatchNo.setCellFactory(column -> new TextFieldTableCellForPurChallan("tblcPurChallCmpTRowBatchNo", callback, productID_callback));
        tblcPurChallCmpTRowBatchNo.setCellFactory(column -> new TextFieldTableCellForPurChallan("tblcPurChallCmpTRowBatchNo", callback, productID_callback, tfPurChallNarrtion));


        tblcPurChallCmpTRowQuantity.setCellValueFactory(cellData -> cellData.getValue().quantityProperty());
//        tblcPurChallCmpTRowQuantity.setCellFactory(column -> new TextFieldTableCellForPurChallan("tblcPurChallCmpTRowQuantity", callback));
        tblcPurChallCmpTRowQuantity.setStyle("-fx-alignment: CENTER-RIGHT;");

        tblcPurChallCmpTRowFreeQty.setCellValueFactory(cellData -> cellData.getValue().freeProperty());
//        tblcPurChallCmpTRowFreeQty.setCellFactory(column -> new TextFieldTableCellForPurChallan("tblcPurChallCmpTRowFreeQty", callback));
        tblcPurChallCmpTRowFreeQty.setStyle("-fx-alignment: CENTER-RIGHT;");

        tblcPurChallCmpTRowRate.setCellValueFactory(cellData -> cellData.getValue().rateProperty());
//        tblcPurChallCmpTRowRate.setCellFactory(column -> new TextFieldTableCellForPurChallan("tblcPurChallCmpTRowRate", callback));
        tblcPurChallCmpTRowRate.setStyle("-fx-alignment: CENTER-RIGHT;");

        tblcPurChallCmpTRowGrossAmt.setCellValueFactory(cellData -> cellData.getValue().gross_amountProperty());
//        tblcPurChallCmpTRowGrossAmt.setCellFactory(column -> new TextFieldTableCellForPurChallan("tblcPurChallCmpTRowGrossAmt", callback));
        tblcPurChallCmpTRowGrossAmt.setStyle("-fx-alignment: CENTER-RIGHT;");

        tblcPurChallCmpTRowDisPer.setCellValueFactory(cellData -> cellData.getValue().dis_perProperty());
//        tblcPurChallCmpTRowDisPer.setCellFactory(column -> new TextFieldTableCellForPurChallan("tblcPurChallCmpTRowDisPer", callback));
        tblcPurChallCmpTRowDisPer.setStyle("-fx-alignment: CENTER-RIGHT;");

        tblcPurChallCmpTRowDisAmt.setCellValueFactory(cellData -> cellData.getValue().dis_amtProperty());
//        tblcPurChallCmpTRowDisAmt.setCellFactory(column -> new TextFieldTableCellForPurChallan("tblcPurChallCmpTRowDisAmt", callback));
        tblcPurChallCmpTRowDisAmt.setStyle("-fx-alignment: CENTER-RIGHT;");

        tblcPurChallCmpTRowTaxPer.setCellValueFactory(cellData -> cellData.getValue().taxProperty());
//        tblcPurChallCmpTRowTaxPer.setCellFactory(column -> new TextFieldTableCellForPurChallan("tblcPurChallCmpTRowTaxPer", callback));
        tblcPurChallCmpTRowDisAmt.setStyle("-fx-alignment: CENTER-RIGHT;");

        tblcPurChallCmpTRowNetAmt.setCellValueFactory(cellData -> cellData.getValue().net_amountProperty());
//        tblcPurChallCmpTRowNetAmt.setCellFactory(column -> new TextFieldTableCellForPurChallan("tblcPurChallCmpTRowNetAmt", callback));
        tblcPurChallCmpTRowNetAmt.setStyle("-fx-alignment: CENTER-RIGHT;");

//        tblcPurChallCmpTRowAction.setCellValueFactory(cellData -> cellData.getValue().net_amountProperty());
//        tblcPurChallCmpTRowAction.setCellFactory(column -> new ButtonTableCell());
        tblcPurChallCmpTRowAction.setCellFactory(column -> new ButtonTableCellDelete(delCallback));

        columnVisibility(tblcPurChallCmpTRowLevelA, Globals.getUserControlsWithSlug("is_level_a"));
        columnVisibility(tblcPurChallCmpTRowLevelB, Globals.getUserControlsWithSlug("is_level_b"));
        columnVisibility(tblcPurChallCmpTRowLevelC, Globals.getUserControlsWithSlug("is_level_c"));

        tblcPurChallCmpTRowLevelA.setText(Globals.getUserControlsNameWithSlug("is_level_a") + "");
        tblcPurChallCmpTRowLevelB.setText(Globals.getUserControlsNameWithSlug("is_level_B") + "");
        tblcPurChallCmpTRowLevelC.setText(Globals.getUserControlsNameWithSlug("is_level_C") + "");

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
        object[0] = cmbPurChallSupplierGST.getSelectionModel().getSelectedItem();
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
        Stage stage = (Stage) bpPurChallRootPane.getScene().getWindow();
        SingleInputDialogs.openLedgerPopUp(stage, "Creditors", input -> {
                    supplierGSTINList.clear();
                    tfPurChallLedgerName.setText(input[0].toString());
                    ledger_id = (String) input[1];
                    ledgerStateCode = (String) input[3];
                    lblOrderValue.setText((String) input[10]);
                    tblvPurChallCmpTRow.getItems().get(0).setLedger_id(ledger_id);
                    tranxLedgerDetailsFun(ledger_id);

                    @SuppressWarnings("unchecked")
                    ObservableList<GstDetailsDTO> gstDetailsDTOS = (ObservableList<GstDetailsDTO>) input[2];

                    for (GstDetailsDTO gstDetailsDTO : gstDetailsDTOS) {
                        supplierGSTINList.add(new CommonDTO(gstDetailsDTO.getGstNo(), gstDetailsDTO.getId()));
                    }
                    if (supplierGSTINList != null && !supplierGSTINList.isEmpty()) {
                        cmbPurChallSupplierGST.setItems(supplierGSTINList);
                        cmbPurChallSupplierGST.setValue(supplierGSTINList.get(0));
                        if (supplierGSTINList.size() > 1) {
                            cmbPurChallSupplierGST.requestFocus();
                        } else {
                            tfPurChallChallanNo.requestFocus();
                        }
                    } else {
                        tfPurChallChallanNo.requestFocus();
                    }

                    if (ledgerStateCode.equalsIgnoreCase(GlobalTranx.getCompanyStateCode().toString())) {
                        taxFlag = true;
                    }

                }, in -> {
                    if (in == true) {
                        isLedgerRed = true;
                        setPurChallDataToProduct();
                    }
                }

        );

    }


    public void purc_disc_per(KeyEvent keyEvent) {
        CommonValidationsUtils.restrictToDecimalNumbers(tfPurChallAllRowDisPer);
        String discText = tfPurChallAllRowDisPer.getText();
        if (!TextUtils.isEmpty(discText)) {
            double totalTaxableAmt = 0.0;
            for (int i = 0; i < tblvPurChallCmpTRow.getItems().size(); i++) {
                PurchaseInvoiceCalculation.rowCalculationForPurcInvoice(i, tblvPurChallCmpTRow, callback);//call row calculation function
                PurchaseInvoiceTable purchaseInvoiceTable = tblvPurChallCmpTRow.getItems().get(i);
                totalTaxableAmt = totalTaxableAmt + Double.parseDouble(purchaseInvoiceTable.getTaxable_amt());

            }
            double disc_per = Double.parseDouble(discText);
            Double amount = (totalTaxableAmt * disc_per) / 100;
            tfPurChallAllRowDisAmt.setText(String.valueOf(amount));
            PurchaseInvoiceCalculation.discountPropotionalCalculation(disc_per + "", "0", tfPurchaseChallanAddChgAmt.getText(), tblvPurChallCmpTRow, callback);
            if (!tfPurchaseChallanAddChgAmt.getText().isEmpty()) {
                PurchaseInvoiceCalculation.additionalChargesCalculation(tfPurchaseChallanAddChgAmt.getText(), tblvPurChallCmpTRow, callback);
            }
        } else {
            tfPurChallAllRowDisAmt.setText("");
            /* for (PurchaseInvoiceTable purchaseInvoiceTable : tblvPurChallCmpTRow.getItems()) {
                String netAmt = purchaseInvoiceTable.getNet_amount();
                if (!purchaseInvoiceTable.getTaxable_amt().isEmpty()) {
                    Double orgTaxableAmt = Double.parseDouble(purchaseInvoiceTable.getTaxable_amt());
                    purchaseInvoiceTable.setNet_amount(netAmt);
                    purchaseInvoiceTable.setTaxable_amt(String.valueOf(orgTaxableAmt));
//                    purchaseInvoiceTable.setTotal_taxable_amt(String.valueOf(orgTaxableAmt));
                }
            }*/

            PurchaseInvoiceCalculation.discountPropotionalCalculation("0", "0", tfPurchaseChallanAddChgAmt.getText(), tblvPurChallCmpTRow, callback);
            if (!tfPurchaseChallanAddChgAmt.getText().isEmpty()) {
                PurchaseInvoiceCalculation.additionalChargesCalculation(tfPurchaseChallanAddChgAmt.getText(), tblvPurChallCmpTRow, callback);
            }
        }

        //? Calculate Tax for each Row
        PurchaseInvoiceCalculation.calculateGst(tblvPurChallCmpTRow, callback);
    }

    public void purc_disc_amt(KeyEvent keyEvent) {
        CommonValidationsUtils.restrictToDecimalNumbers(tfPurChallAllRowDisAmt);
        String discAmtText = tfPurChallAllRowDisAmt.getText();
        if (!TextUtils.isEmpty(discAmtText)) {
            double totalTaxableAmt = 0.0;
            for (int i = 0; i < tblvPurChallCmpTRow.getItems().size(); i++) {
                PurchaseInvoiceCalculation.rowCalculationForPurcInvoice(i, tblvPurChallCmpTRow, callback);//call row calculation function
                PurchaseInvoiceTable purchaseInvoiceTable = tblvPurChallCmpTRow.getItems().get(i);
                totalTaxableAmt = totalTaxableAmt + Double.parseDouble(purchaseInvoiceTable.getTaxable_amt());

            }
            double disc_amt = Double.parseDouble(discAmtText);
            double percentage = (disc_amt / totalTaxableAmt) * 100;
            tfPurChallAllRowDisPer.setText(String.format("%.2f", percentage));

            PurchaseInvoiceCalculation.discountPropotionalCalculation("0", disc_amt + "", tfPurchaseChallanAddChgAmt.getText(), tblvPurChallCmpTRow, callback);
            if (!tfPurchaseChallanAddChgAmt.getText().isEmpty()) {
                PurchaseInvoiceCalculation.additionalChargesCalculation(tfPurchaseChallanAddChgAmt.getText(), tblvPurChallCmpTRow, callback);
            }
        } else {
            tfPurChallAllRowDisPer.setText("");
           /* for (PurchaseInvoiceTable purchaseInvoiceTable : tblvPurChallCmpTRow.getItems()) {
                String netAmt = purchaseInvoiceTable.getNet_amount();
                if (!purchaseInvoiceTable.getTaxable_amt().isEmpty()) {
                    Double orgTaxableAmt = Double.parseDouble(purchaseInvoiceTable.getTaxable_amt());
                    purchaseInvoiceTable.setNet_amount(netAmt);
                    purchaseInvoiceTable.setTaxable_amt(String.valueOf(orgTaxableAmt));
                    purchaseInvoiceTable.setTotal_taxable_amt(String.valueOf(orgTaxableAmt));
                }
            }*/
            PurchaseInvoiceCalculation.discountPropotionalCalculation("0", "0", tfPurchaseChallanAddChgAmt.getText(), tblvPurChallCmpTRow, callback);
            if (!tfPurchaseChallanAddChgAmt.getText().isEmpty()) {
                PurchaseInvoiceCalculation.additionalChargesCalculation(tfPurchaseChallanAddChgAmt.getText(), tblvPurChallCmpTRow, callback);
            }
        }

        //? Calculate Tax for each Row
        PurchaseInvoiceCalculation.calculateGst(tblvPurChallCmpTRow, callback);
    }

    public void onClickAdditionalCharges() {
        Stage stage = (Stage) bpPurChallRootPane.getScene().getWindow();
        AdditionalCharges.additionalCharges(stage, addChargesDTOList, total_taxable_amt, (input1, input2) -> {
            addChargesDTOList.clear();
            addChargesDTOList.addAll(input2);
            tfPurchaseChallanAddChgAmt.setText(input1[1]);
            PurchaseInvoiceCalculation.additionalChargesCalculation(input1[1], tblvPurChallCmpTRow, callback);

        });
    }


    // Scene Initialization
    public void sceneInitilization() {
        PurInvoiceCommunicator.resetFields();
        bpPurChallRootPane.sceneProperty().addListener((observable, oldScene, newScene) -> {
            if (newScene != null && newScene.getWindow() instanceof Stage) {
                Communicator.stage = (Stage) newScene.getWindow();
            }
        });
    }
//    public void sceneInitilization() {
//        bpPurChallRootPane.sceneProperty().addListener((observable, oldScene, newScene) -> {
//            if (newScene != null && newScene.getWindow() instanceof Stage) {
//                Communicator.stage = (Stage) newScene.getWindow();
//                if (Communicator.stage != null) {
//                    Communicator.stage.getScene().addEventFilter(MouseEvent.MOUSE_CLICKED, (e) -> {
//                        if (e.getButton() == MouseButton.PRIMARY || e.getButton() == MouseButton.SECONDARY) {
//                            e.consume();
//                        }
//                    });
//                }
//            }
//        });
////        bpPurChallRootPane.getScene()
//        if (bpPurChallRootPane.getScene() != null) {
//            bpPurChallRootPane.getScene().addEventFilter(MouseEvent.MOUSE_RELEASED,
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
    private void getPurChallSrNumber() {
        APIClient apiClient = null;
        try {
            purChallanLogger.debug("Get getPurChallSrNumber Started...");
            apiClient = new APIClient(EndPoints.getCurrentPurChallSrNo, "", RequestType.GET);
            apiClient.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
                @Override
                public void handle(WorkerStateEvent workerStateEvent) {
                    jsonObject = new Gson().fromJson(workerStateEvent.getSource().getValue().toString(), JsonObject.class);
                    if (jsonObject.get("responseStatus").getAsInt() == 200) {
                        tfPurChallPurSerialNo.setText("" + jsonObject.get("count"));
                    }
                }
            });
            apiClient.setOnCancelled(new EventHandler<WorkerStateEvent>() {
                @Override
                public void handle(WorkerStateEvent workerStateEvent) {
                    purChallanLogger.error("Network API cancelled in getPurChallSrNumber()" + workerStateEvent.getSource().getValue().toString());

                }
            });
            apiClient.setOnFailed(new EventHandler<WorkerStateEvent>() {
                @Override
                public void handle(WorkerStateEvent workerStateEvent) {
                    purChallanLogger.error("Network API failed in getPurChallSrNumber()" + workerStateEvent.getSource().getValue().toString());
                }
            });
            apiClient.start();
            purChallanLogger.debug("Get getPurChallSrNumber End...");
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
                                cmbPurChallPurchaAc.setItems(observableList);
                                cmbPurChallPurchaAc.getSelectionModel().selectFirst();
                                cmbPurChallPurchaAc.setConverter(new StringConverter<CommonDTO>() {
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
                        }
                    } else {
                        purChallanLogger.debug("Get getPurchaseAccounts Error in response...");
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
        Stage stage = (Stage) btnPurChallChooseFileButton.getScene().getWindow();
        File selectedFile = fileChooser.showOpenDialog(stage);

        // Handle selected file
        if (selectedFile != null) {
            tfPurChallChooseFileText.setText(selectedFile.getName());
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
            purchase_challan_map.put("rowDelDetailsIds", deletedRows.toString());
        }

        purchase_challan_map.put("taxFlag", String.valueOf(taxFlag));

        purchase_challan_map.put("invoice_date", Communicator.text_to_date.fromString(tfPurChallPurChallanDate.getText()).toString());

        purchase_challan_map.put("transaction_date", Communicator.text_to_date.fromString(tfPurChallTranxDate.getText()).toString());

        purchase_challan_map.put("newReference", "false");

        purchase_challan_map.put("invoice_no", tfPurChallChallanNo.getText());

        purchase_challan_map.put("purchase_id", cmbPurChallPurchaAc.getValue().getId());

        purchase_challan_map.put("purchase_sr_no", tfPurChallPurSerialNo.getText());

        purchase_challan_map.put("narration", tfPurChallNarrtion.getText());

        purchase_challan_map.put("supplier_code_id", ledger_id);

        purchase_challan_map.put("isRoundOffCheck", String.valueOf(chbPurChallRoundOff.isSelected()));

        purchase_challan_map.put("roundoff", lblPurChallRoundOff.getText());

        purchase_challan_map.put("totalamt", lblPurChallTotalBillAmount.getText());

        purchase_challan_map.put("total_purchase_discount_amt", lblPurChallTotalDiscount.getText());

        purchase_challan_map.put("gstNo", gst_no);

        purchase_challan_map.put("tcs_per", "0.00");

        purchase_challan_map.put("tcs_amt", "0.00");

        purchase_challan_map.put("tcs_mode", "");

        purchase_challan_map.put("purchase_discount", tfPurChallAllRowDisPer.getText().isEmpty() || tfPurChallAllRowDisPer == null ? "0.0" : tfPurChallAllRowDisPer.getText());

        purchase_challan_map.put("purchase_discount_amt", tfPurChallAllRowDisAmt.getText().isEmpty() || tfPurChallAllRowDisAmt == null ? "0.0" : tfPurChallAllRowDisAmt.getText());

        purchase_challan_map.put("additionalChargesTotal", tfPurchaseChallanAddChgAmt.getText().isEmpty() ? "0" : tfPurchaseChallanAddChgAmt.getText());

        purchase_challan_map.put("additionalCharges", addChargesDTOList.toString());
        List<PurchaseInvoiceTable> currentItems = new ArrayList<>(tblvPurChallCmpTRow.getItems());

        if (!currentItems.isEmpty()) {
            PurchaseInvoiceTable lastItem = currentItems.get(currentItems.size() - 1);

            if (lastItem.getParticulars() != null && lastItem.getParticulars().isEmpty()) {
                currentItems.remove(currentItems.size() - 1);
            }
        }

        List<PurchaseInvoiceTable> list = new ArrayList<>(currentItems);

//        List<PurchaseInvoiceTable> list = tblvPurChallCmpTRow.getItems();

        for (PurchaseInvoiceTable purchaseInvoiceTable : list) {
            if (!purchaseInvoiceTable.getTotal_taxable_amt().equals("0.0")) {
                purchaseInvoiceTable.setTotal_amt(purchaseInvoiceTable.getTotal_taxable_amt());
            } else {
                purchaseInvoiceTable.setTotal_amt(purchaseInvoiceTable.getTaxable_amt());
            }
        }

        Double total_row_gross_amt = 0.0;

        for (PurchaseInvoiceTable purchaseInvoiceTable : list) {
            total_row_gross_amt = total_row_gross_amt + Double.parseDouble(
                    purchaseInvoiceTable.getGross_amount1().isEmpty() ? "0.0" : purchaseInvoiceTable.getGross_amount1());
        }

        purchase_challan_map.put("total_row_gross_amt", String.valueOf(total_row_gross_amt));

        purchase_challan_map.put("row", list.toString());

        APIClient apiClient = null;
        try {
            String formData = Globals.mapToStringforFormData(purchase_challan_map);

            if (purChallEditId > 0) {
                apiClient = new APIClient(EndPoints.EDIT_PURCHASE_CHALLAN, formData, RequestType.FORM_DATA);
                //? HIGHLIGHT
                PurchaseChallanListController.editedPurchaseChallanId = idValue.toString(); //? Set the ID for editing
            } else {
                apiClient = new APIClient(EndPoints.CREATE_PURCHASE_CHALLAN, formData, RequestType.FORM_DATA);
                //? HIGHLIGHT
                PurchaseChallanListController.isNewPurchaseChallanCreated = true; //? Set the flag for new creation
            }

            apiClient.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
                @Override
                public void handle(WorkerStateEvent workerStateEvent) {
                    jsonObject = new Gson().fromJson(workerStateEvent.getSource().getValue().toString(), JsonObject.class);
                    message = jsonObject.get("message").getAsString();
                    int status = jsonObject.get("responseStatus").getAsInt();
                    if (status == 200) {
                        //Successfully Alert Popup After Edit and Redirect to List Page
                        AlertUtility.AlertSuccessTimeout(AlertUtility.alertTypeSuccess, message, input -> {
                            GlobalController.getInstance().addTabStatic(PURCHASE_CHALLAN_LIST_SLUG, false);
                        });
                    } else {
                        AlertUtility.AlertErrorTimeout(AlertUtility.alertTypeError, message, in -> {
                            btnPurChallFinalSubmit.requestFocus();
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


    private void getPurchaseChallanById(Integer purChallEditId) {

        Map<String, String> body = new HashMap<>();
        body.put("id", purChallEditId.toString());
        String formData = Globals.mapToStringforFormData(body);
        HttpResponse<String> response = APIClient.postFormDataRequest(formData, "get_purchase_challan_by_id_new");

        TranxPurchaseEditMainDTO responseBody = new Gson().fromJson(response.body(), TranxPurchaseEditMainDTO.class);
        if (responseBody.getResponseStatus() == 200) {
            setPurChallFormData(responseBody);
        }
    }


    // Setting Data to the fields
    private void setPurChallFormData(TranxPurchaseEditMainDTO responseBody) {
        String responseBdy = new Gson().toJson(responseBody);
        if (purchaseOrderDTO == null) {
            btnPurChallFinalSubmit.setText("Update");
            btnPurChallFinalModify.setVisible(false);
        } else {
            btnPurChallFinalSubmit.setText("Submit");
        }
        tfPurChallChallanNo.setText(responseBody.getInvoiceData().getInvoiceNo());
        tfPurChallNarrtion.setText(responseBody.getNarration());
        lblPurChallTotalQty.setText(String.valueOf(responseBody.getTotalQty()));
        lblPurChallFreeQty.setText(String.valueOf(responseBody.getTotalFreeQty()));
        lblPurChallRoundOff.setText(String.valueOf(responseBody.getInvoiceData().getRoundoff()));
        chbPurChallRoundOff.setSelected(responseBody.getInvoiceData().getRoundOffCheck());
        ledgerId = String.valueOf(responseBody.getInvoiceData().getSupplierId());
        tranxLedgerDetailsFun(ledgerId);
        edit_response_map.put("tcs_mode", responseBody.getTcs());
        edit_response_map.put("narration", responseBody.getNarration());
        edit_response_map.put("discountLedgerId", responseBody.getDiscountLedgerId());
        edit_response_map.put("discountInAmt", responseBody.getDiscountInAmt());
        edit_response_map.put("discountInPer", responseBody.getDiscountInPer());
        edit_response_map.put("totalPurchaseDiscountAmt", responseBody.getTotalPurchaseDiscountAmt());
        edit_response_map.put("totalQty", responseBody.getTotalQty());
        edit_response_map.put("totalFreeQty", responseBody.getTotalFreeQty());
        edit_response_map.put("grossTotal", responseBody.getGrossTotal());
        edit_response_map.put("totalTax", responseBody.getTotalTax());
        edit_response_map.put("additionLedger1", responseBody.getAdditionLedger1());
        edit_response_map.put("additionLedgerAmt1", responseBody.getAdditionLedgerAmt1());
        edit_response_map.put("additionLedger2", responseBody.getAdditionLedger2());
        edit_response_map.put("additionLedgerAmt2", responseBody.getAdditionLedgerAmt2());
        edit_response_map.put("additionLedger3", responseBody.getAdditionLedger3());
        edit_response_map.put("additionLedgerAmt3", responseBody.getAdditionLedgerAmt3());
        edit_response_map.put("invoiceData", responseBody.getInvoiceData());
        edit_response_map.put("barcodeList", responseBody.getBarcodeList());
        edit_response_map.put("additionalCharges", responseBody.getAdditionalCharges());

        rowListDTOS.addAll(responseBody.getRow());
        addChargesResLst.addAll(responseBody.getAdditionalCharges());

        InvoiceData invoiceDataResDTO = responseBody.getInvoiceData();
        invoice_data_map.put("id", String.valueOf(invoiceDataResDTO.getId()));
        invoice_data_map.put("invoice_dt", String.valueOf(invoiceDataResDTO.getInvoiceDt()));
        invoice_data_map.put("invoice_no", String.valueOf(invoiceDataResDTO.getInvoiceNo()));
        invoice_data_map.put("tranx_unique_code", String.valueOf(invoiceDataResDTO.getTranxUniqueCode()));
        invoice_data_map.put("purchase_sr_no", String.valueOf(invoiceDataResDTO.getPurchaseSrNo()));
        invoice_data_map.put("purchase_account_ledger_id", String.valueOf(invoiceDataResDTO.getPurchaseAccountLedgerId()));
        invoice_data_map.put("supplierId", String.valueOf(invoiceDataResDTO.getSupplierId()));
        ledger_id = invoice_data_map.get("supplierId");
        invoice_data_map.put("transaction_dt", String.valueOf(invoiceDataResDTO.getTransactionDt()));
        invoice_data_map.put("additional_charges_total", String.valueOf(invoiceDataResDTO.getAdditionalChargesTotal()));
        invoice_data_map.put("gstNo", String.valueOf(invoiceDataResDTO.getGstNo()));
        invoice_data_map.put("isRoundOffCheck", String.valueOf(invoiceDataResDTO.getRoundOffCheck()));
        invoice_data_map.put("roundoff", String.valueOf(invoiceDataResDTO.getRoundoff()));
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

    private void setAddChargesInTable() {
        Double addChgAmt = 0.0;
        Double totalAddChgAmt = 0.0;
        for (AdditionalCharge addChgLst : addChargesResLst) {
            CustomAddChargesDTO customAddChargesDTO = new CustomAddChargesDTO();
            customAddChargesDTO.setAdditional_charges_details_id(String.valueOf(addChgLst.getAdditionalChargesDetailsId()));
            customAddChargesDTO.setLedgerId(String.valueOf(addChgLst.getLedgerId()));
            customAddChargesDTO.setAmt(String.valueOf(addChgLst.getAmt()));
            customAddChargesDTO.setPercent(String.valueOf(addChgLst.getPercent()));
            addChargesDTOList.add(customAddChargesDTO);

            addChgAmt = addChgLst.getAmt();
            totalAddChgAmt += addChgAmt;
        }
        tfPurchaseChallanAddChgAmt.setText(totalAddChgAmt.toString());
        PurchaseInvoiceCalculation.additionalChargesCalculation(totalAddChgAmt.toString(), tblvPurChallCmpTRow, callback);

    }


    /**
     * Info :   For showing the details of product after selection of product
     *
     * @Author : Vinit Chilaka
     */
    private void tranxProductDetailsFun(String id) {
        //todo: activating the product tab
        tranxPurChallanTabPane.getSelectionModel().select(tabPurChallanProduct);

        APIClient apiClient = null;
        try {
            Map<String, String> map = new HashMap<>();
            map.put("product_id", id);
            String formData = Globals.mapToStringforFormData(map);
            apiClient = new APIClient(EndPoints.TRANSACTION_PRODUCT_DETAILS, formData, RequestType.FORM_DATA);
            apiClient.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
                @Override
                public void handle(WorkerStateEvent workerStateEvent) {
                    jsonObject = new Gson().fromJson(workerStateEvent.getSource().getValue().toString(), JsonObject.class);
                    if (jsonObject.get("responseStatus").getAsInt() == 200) {
                        JsonObject productJsonObj = jsonObject.getAsJsonObject("result");

                        txtPurChallProductBrand.setText(productJsonObj.get("brand").getAsString());
                        txtPurChallProductGroup.setText(productJsonObj.get("group").getAsString());
                        txtPurChallProductSubGroup.setText(productJsonObj.get("subgroup").getAsString());
                        txtPurChallProductCategory.setText(productJsonObj.get("category").getAsString());
                        txtPurChallProductHsn.setText(productJsonObj.get("hsn").getAsString());
                        txtPurChallProductTaxType.setText(productJsonObj.get("tax_type").getAsString());
                        txtPurChallProductTaxPer.setText(productJsonObj.get("tax_per").getAsString());
                        txtPurChallProductMarginPer.setText(productJsonObj.get("margin_per").getAsString());
                        txtPurChallProductCost.setText(productJsonObj.get("cost").getAsString());
                        txtPurChallProductShelfId.setText(productJsonObj.get("shelf_id").getAsString());
                        txtPurChallProductMinStock.setText(productJsonObj.get("min_stocks").getAsString());
                        txtPurChallProductMaxStock.setText(productJsonObj.get("max_stocks").getAsString());
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
        tranxPurChallanTabPane.getSelectionModel().select(tabPurChallanLedger);

        APIClient apiClient = null;
        try {
            Map<String, String> map = new HashMap<>();
            map.put("ledger_id", id);
            String formData = Globals.mapToStringforFormData(map);
            apiClient = new APIClient(EndPoints.TRANSACTION_LEDGER_DETAILS, formData, RequestType.FORM_DATA);
            apiClient.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
                @Override
                public void handle(WorkerStateEvent workerStateEvent) {
                    jsonObject = new Gson().fromJson(workerStateEvent.getSource().getValue().toString(), JsonObject.class);
                    if (jsonObject.get("responseStatus").getAsInt() == 200) {
                        JsonObject jsonObj = jsonObject.getAsJsonObject("result");
                        CompanyStateCode = jsonObject.get("company_state_code").getAsLong();
                        stateCode = jsonObj.get("stateCode").getAsLong();
                        // setting values
                        txtPurChallanGstNo.setText(jsonObj.get("gst_number").getAsString());
                        txtPurChallanArea.setText(jsonObj.get("area").getAsString());
                        txtPurChallanBank.setText(jsonObj.get("bank_name").getAsString());
                        txtPurChallanContactPerson.setText(jsonObj.get("contact_name").getAsString());
                        txtPurChallanCreditDays.setText(jsonObj.get("credit_days").getAsString());
                        txtPurChallanFssai.setText(jsonObj.get("fssai_number").getAsString());
                        txtPurChallanLicenseNo.setText(jsonObj.get("license_number").getAsString());
                        txtPurChallanRoute.setText(jsonObj.get("route").getAsString());
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
        tranxPurChallanTabPane.getSelectionModel().select(tabPurChallanBatch);
        purChallanLogger.debug("Get Supplier List Data Started...");
        Map<String, String> map = new HashMap<>();
        map.put("batchNo", bNo);
        map.put("id", bId);
//        requestData.append("batchNo", batchNo.batch_no);
//        requestData.append("id", batchNo.b_details_id);
        String formData = Globals.mapToStringforFormData(map);
//        HttpResponse<String> response = APIClient.postFormDataRequest(formData, EndPoints.GET_SUPPLIERLIST_BY_PRODUCTID_ENDPOINT);
        apiClient = new APIClient(EndPoints.GET_BATCH_DETAILS_ENDPOINT, formData, RequestType.FORM_DATA);

        apiClient.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent workerStateEvent) {
                JsonObject jsonObject = new Gson().fromJson(workerStateEvent.getSource().getValue().toString(), JsonObject.class);
                if (jsonObject.get("responseStatus").getAsInt() == 200) {
                    JsonObject batchJsonObj = jsonObject.getAsJsonObject("response");

                    txtPurChallBatchBatch.setText(batchJsonObj.get("batchNo").getAsString());
                    txtPurChallBatchMRP.setText(batchJsonObj.get("batchMrp").getAsString());
                    txtPurChallBatchHMfgDt.setText(DateConvertUtil.convertDispDateFormat(batchJsonObj.get("mfgDate").getAsString()));
                    txtPurChallBatchExpDt.setText(DateConvertUtil.convertDispDateFormat(batchJsonObj.get("expDate").getAsString()));
                    txtPurChallBatchPurRate.setText(batchJsonObj.get("pur_rate").getAsString());
                    txtPurChallBatchAvlStk.setText(batchJsonObj.get("qty").getAsString());
                    txtPurChallBatchDisPer.setText(batchJsonObj.get("b_dis_per").getAsString());
                    txtPurChallBatchDisAmt.setText(batchJsonObj.get("b_dis_amt").getAsString());
                    txtPurChallBatchSaleRate.setText(batchJsonObj.get("sales_rate").getAsString());
                    txtPurChallBatchFSRMH.setText(batchJsonObj.get("fsrmh").getAsString());
                    txtPurChallBatchFSRAI.setText(batchJsonObj.get("fsrai").getAsString());
                    txtPurChallBatchCSRMH.setText(batchJsonObj.get("csrmh").getAsString());
                    txtPurChallBatchCSRAI.setText(batchJsonObj.get("csrai").getAsString());
                    txtPurChallBatchCSRAI.setText(batchJsonObj.get("csrai").getAsString());

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
            map.put("bill_no", String.valueOf(tfPurChallChallanNo.getText()));
            String formData = Globals.mapToStringforFormData(map);
            apiClient = new APIClient(EndPoints.VALIDATE_PURCHASE_CHALLAN, formData, RequestType.FORM_DATA);
            apiClient.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
                @Override
                public void handle(WorkerStateEvent workerStateEvent) {
                    JsonObject jsonObject = new Gson().fromJson(workerStateEvent.getSource().getValue().toString(), JsonObject.class);
                    if (jsonObject.get("responseStatus").getAsInt() != 200) {
                        String message = jsonObject.get("message").getAsString();
                        AlertUtility.AlertWarningTimeout(AlertUtility.alertTypeError, message, in -> {
                            tfPurChallChallanNo.requestFocus();
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
            map.put("bill_no", String.valueOf(tfPurChallChallanNo.getText()));
            String formData = Globals.mapToStringforFormData(map);
            apiClient = new APIClient(EndPoints.VALIDATE_PURCHASE_CHALLAN_UPDATE, formData, RequestType.FORM_DATA);
            apiClient.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
                @Override
                public void handle(WorkerStateEvent workerStateEvent) {
                    JsonObject jsonObject = new Gson().fromJson(workerStateEvent.getSource().getValue().toString(), JsonObject.class);
                    if (jsonObject.get("responseStatus").getAsInt() != 200) {
                        String message = jsonObject.get("message").getAsString();
                        AlertUtility.AlertWarningTimeout(AlertUtility.alertTypeError, message, in -> {
                            tfPurChallChallanNo.requestFocus();
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
            AlertUtility.AlertErrorTimeout(AlertUtility.alertTypeError, "Failed to Connect Server !", in -> {
            });
        } finally {
            apiClient = null;
        }
    }

    private void tfChallanNoValidatition() {
        tfPurChallChallanNo.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal) {
                if (tfPurChallChallanNo.getText().isEmpty()) {
                    tfPurChallChallanNo.requestFocus();
                } else {
                    String btText = btnPurChallFinalSubmit.getText();
                    if (btText.equalsIgnoreCase("Submit")) {
                        validatePurchaseChallan();
                    } else {
                        validatePurchaseChallanUpdate();
                    }
                }

            }
        });
    }


}

class TextFieldTableCellForPurChallan extends TableCell<PurchaseInvoiceTable, String> {
    private TextField textField;
    private String columnName;

    private TableCellCallback<Object[]> callback;
    private TableCellCallback<Object[]> productID_callback;
    private TableCellCallback<Object[]> addPrdCalbak;
    private TextField button;


    public TextFieldTableCellForPurChallan(String columnName, TableCellCallback<Object[]> callback, TableCellCallback<Object[]> productID_callback) {
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
        freeColumn();

        netAmountColumn();

    }


    public TextFieldTableCellForPurChallan(String columnName, TableCellCallback<Object[]> callback) {
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
        freeColumn();
        netAmountColumn();

    }


    public TextFieldTableCellForPurChallan(String columnName, TableCellCallback<Object[]> callback,
                                           TableCellCallback<Object[]> productID_callback, TextField button
    ) {
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

        netAmountColumn();

    }

    public TextFieldTableCellForPurChallan(String columnName, TableCellCallback<Object[]> callback,
                                           TableCellCallback<Object[]> productID_callback, TextField button,
                                           TableCellCallback<Object[]> addPrdCalbak) {
        this.columnName = columnName;
        this.textField = new TextField();

        this.callback = callback;
        this.productID_callback = productID_callback;
        this.addPrdCalbak = addPrdCalbak;

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

        netAmountColumn();

    }


    private void batchColumn() {
        if ("tblcPurChallCmpTRowBatchNo".equals(columnName)) {
            textField.setEditable(false);
            textField.setOnKeyPressed(event -> {
                if (event.getCode() == KeyCode.SPACE) {
                    openBatchWindow();
                }
                if (event.isShiftDown() && event.getCode() == KeyCode.TAB) {
                    TableColumn<PurchaseInvoiceTable, ?> colName = getTableView().getColumns().get(6);
                    getTableView().edit(getIndex(), colName);
                }

                if (event.getCode() == KeyCode.ENTER || !event.isShiftDown() && event.getCode() == KeyCode.TAB) {
                    if (!textField.getText().isEmpty()) {
                        int index = getIndex();
                        if (index >= getTableView().getItems().size() - 1)
                            getTableView().getItems().addAll(new PurchaseInvoiceTable("", "", "" + (getIndex() + 2), "", "", "", "", "", "", "", "", "", "", "", "", "", "", ""));
                        TableColumn<PurchaseInvoiceTable, ?> colName = getTableView().getColumns().get(1);
                        getTableView().edit(getIndex() + 1, colName);
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

    private void freeColumn() {
        if ("tblcPurChallCmpTRowFreeQty".equals(columnName)) {
            textField.setEditable(false);
        }
    }

    /* private void particularsColumn() {
         if ("tblcPurChallCmpTRowParticulars".equals(columnName)) {
             textField.setEditable(false);
             textField.setOnKeyPressed(event -> {
                 if (event.getCode() == KeyCode.SPACE) {
                     openProduct();
                 }
                 if (event.isShiftDown() && event.getCode() == KeyCode.TAB) {
                     if (getIndex() == 1) {
                         TableColumn<PurchaseInvoiceTable, ?> colName = getTableView().getColumns().get(7);
                         getTableView().edit(getIndex() - 1, colName);
                     } else {
                         TableColumn<PurchaseInvoiceTable, ?> colName = getTableView().getColumns().get(15);
                         getTableView().edit(getIndex() - 1, colName);
                     }
                 }

                 if (event.getCode() == KeyCode.ENTER || !event.isShiftDown() && event.getCode() == KeyCode.TAB) {
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
                 //openProduct();
             });
         }
     }*/
    private void particularsColumn() {
        if ("tblcPurChallCmpTRowParticulars".equals(columnName)) {
            textField.setEditable(false);
            textField.setOnKeyPressed(event -> {
                if (event.getCode() == KeyCode.SPACE) {
                    openProduct();
                }
                if (event.isShiftDown() && event.getCode() == KeyCode.TAB) {
                    if (getIndex() == 1) {
                        TableColumn<PurchaseInvoiceTable, ?> colName = getTableView().getColumns().get(7);
                        getTableView().edit(getIndex() - 1, colName);
                    } else {
                        TableColumn<PurchaseInvoiceTable, ?> colName = getTableView().getColumns().get(15);
                        getTableView().edit(getIndex() - 1, colName);
                    }
                }

                if (event.getCode() == KeyCode.ENTER || !event.isShiftDown() && event.getCode() == KeyCode.TAB) {
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
//                openProduct();
            });
        }
    }

    private void netAmountColumn() {

        if ("tblcPurChallCmpTRowNetAmt".equals(columnName)) {
            textField.setEditable(false);
            textField.setOnKeyPressed(event -> {
                if (event.getCode() == KeyCode.ENTER) {

                    int current_index = getTableRow().getIndex();

                    getTableView().getItems().addAll(new PurchaseInvoiceTable("", String.valueOf(current_index + 1), "1", "", "", "", "", "", "", "", "", "", "", "", "", "", "", ""));
                }
            });
        }
    }

    private void openProduct() {
        try {
            PurchaseInvoiceTable selectedRow = getTableView().getItems().get(getIndex());
            SingleInputDialogs.openProductPopUp(Communicator.stage, Integer.valueOf(selectedRow.getProduct_id()), "Product", input -> {
                if (input != null) {
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
                    TableColumn<PurchaseInvoiceTable, ?> colName = getTableView().getColumns().get(1);
                    getTableView().edit(getIndex(), colName);
                }
            }, item -> {
                if (item) {
                    if (addPrdCalbak != null) {
                        int cur_index = getTableRow().getIndex();
                        Object[] object = new Object[3];
                        object[0] = true;
                        object[1] = cur_index;
                        object[2] = "isProduct";
                        addPrdCalbak.call(object);
                    }
                }
            });
        } catch (Exception e) {
            System.out.println("Exception:" + e.getMessage());
        }
    }

    public void openBatchWindow() {
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

        if (current_table_row.getIs_batch().equals("true") && ledgerid != 0) {
            BatchWindow.batchWindow(String.valueOf(current_index), product_details_to_batch_window, batchWindowTableDTOList, input -> {

                getTableView().getItems().get(Integer.parseInt(input.get(0).getCurrentIndex())).setBatchWindowTableDTOList(input);


//
                for (BatchWindowTableDTO batchWindowTableDTOSs : input) {
                    getTableView().getItems().get(Integer.parseInt(input.get(0).getCurrentIndex())).setBatch_or_serial(batchWindowTableDTOSs.getBatchNo());
                    getTableView().getItems().get(Integer.parseInt(input.get(0).getCurrentIndex())).setQuantity(batchWindowTableDTOSs.getQuantity());
                    getTableView().getItems().get(Integer.parseInt(input.get(0).getCurrentIndex())).setFree(batchWindowTableDTOSs.getFree());
                    getTableView().getItems().get(Integer.parseInt(input.get(0).getCurrentIndex())).setRate(batchWindowTableDTOSs.getPurchaseRate());
                    getTableView().getItems().get(Integer.parseInt(input.get(0).getCurrentIndex())).setDis_per(batchWindowTableDTOSs.getDiscountPercentage());
                    getTableView().getItems().get(Integer.parseInt(input.get(0).getCurrentIndex())).setDis_amt(batchWindowTableDTOSs.getDiscountAmount());


                    getTableView().getItems().get(Integer.parseInt(input.get(0).getCurrentIndex())).setB_details_id(batchWindowTableDTOSs.getB_details_id());

                    getTableView().getItems().get(Integer.parseInt(input.get(0).getCurrentIndex())).setB_no(batchWindowTableDTOSs.getBatchNo());

                    getTableView().getItems().get(Integer.parseInt(input.get(0).getCurrentIndex())).setB_rate(batchWindowTableDTOSs.getMrp());

                    getTableView().getItems().get(Integer.parseInt(input.get(0).getCurrentIndex())).setB_purchase_rate(batchWindowTableDTOSs.getPurchaseRate());


                    getTableView().getItems().get(Integer.parseInt(input.get(0).getCurrentIndex())).setRate_a(batchWindowTableDTOSs.getFsr_mh());
                    getTableView().getItems().get(Integer.parseInt(input.get(0).getCurrentIndex())).setRate_b(batchWindowTableDTOSs.getFsr_ai());
                    getTableView().getItems().get(Integer.parseInt(input.get(0).getCurrentIndex())).setRate_c(batchWindowTableDTOSs.getCsr_mh());
                    getTableView().getItems().get(Integer.parseInt(input.get(0).getCurrentIndex())).setRate_d(batchWindowTableDTOSs.getCsr_ai());


                    getTableView().getItems().get(Integer.parseInt(input.get(0).getCurrentIndex())).setMin_margin(batchWindowTableDTOSs.getMargin());

                    if (batchWindowTableDTOSs.getExpiryDate() != null && !batchWindowTableDTOSs.getExpiryDate().isEmpty()) {
                        getTableView().getItems().get(Integer.parseInt(input.get(0).getCurrentIndex())).setB_expiry(String.valueOf(Communicator.text_to_date.fromString(batchWindowTableDTOSs.getExpiryDate())));
                    } else {
                        getTableView().getItems().get(Integer.parseInt(input.get(0).getCurrentIndex())).setB_expiry("");
                    }

                    if (batchWindowTableDTOSs.getManufacturingDate() != null && !batchWindowTableDTOSs.getManufacturingDate().isEmpty()) {
                        getTableView().getItems().get(Integer.parseInt(input.get(0).getCurrentIndex())).setManufacturing_date(String.valueOf(Communicator.text_to_date.fromString(batchWindowTableDTOSs.getManufacturingDate())));
                    } else {
                        getTableView().getItems().get(Integer.parseInt(input.get(0).getCurrentIndex())).setManufacturing_date("");
                    }

                }
                String batchNo = getTableView().getItems().get(Integer.parseInt(input.get(0).getCurrentIndex())).getBatch_or_serial();
                String bDetailsId = getTableView().getItems().get(Integer.parseInt(input.get(0).getCurrentIndex())).getB_details_id();

                //?Row Level Calculation
                PurchaseInvoiceCalculation.rowCalculationForPurcInvoice(Integer.parseInt(input.get(0).getCurrentIndex()), getTableView(), callback);

                //? Calculate Tax for each Row
                PurchaseInvoiceCalculation.calculateGst(getTableView(), callback);
//                getTableView().getItems().addAll(new PurchaseInvoiceTable("", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", ""));

//                TableColumn<PurchaseInvoiceTable, ?> colName = getTableView().getColumns().get(1);
//                getTableView().edit(getIndex() + 1, colName);

                int index = getIndex();
                if (index >= getTableView().getItems().size() - 1)
                    getTableView().getItems().addAll(new PurchaseInvoiceTable("", "", "" + (getIndex() + 2), "", "", "", "", "", "", "", "", "", "", "", "", "", "", ""));
                TableColumn<PurchaseInvoiceTable, ?> colName = getTableView().getColumns().get(1);
                getTableView().edit(getIndex() + 1, colName);

                if (productID_callback != null) {
                    Object[] object = new Object[2];
                    object[0] = batchNo;
                    object[1] = bDetailsId;
                    productID_callback.call(object);
                }

            });
        }
    }


    public void textfieldStyle() {
        if (columnName.equals("tblcPurChallCmpTRowParticulars")) {
            textField.setPromptText("Particulars");
            textField.setEditable(false);
        } else {
            textField.setPromptText("0");
        }

//        textField.setPrefHeight(38);
//        textField.setMaxHeight(38);
//        textField.setMinHeight(38);

        if (columnName.equals("tblcPurChallCmpTRowParticulars")) {
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


        if (columnName.equals("tblcPurChallCmpTRowNetAmt")) {
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
        if (item != null && columnName.equals("tblcPurChallCmpTRowParticulars")) {
            ((PurchaseInvoiceTable) item).setParticulars(newValue.isEmpty() ? "" : newValue);
        } else if (columnName.equals("tblcPurChallCmpTRowBatchNo")) {
            (getTableRow().getItem()).setBatch_or_serial(newValue.isEmpty() ? "" : newValue);
        } else if (columnName.equals("tblcPurChallCmpTRowQuantity")) {
            (getTableRow().getItem()).setQuantity(newValue.isEmpty() ? "0" : newValue);
        } else if (columnName.equals("tblcPurChallCmpTRowFreeQty")) {
            (getTableRow().getItem()).setFree(newValue.isEmpty() ? "0" : newValue);
        } else if (columnName.equals("tblcPurChallCmpTRowRate")) {
            (getTableRow().getItem()).setRate(newValue.isEmpty() ? "0.0" : newValue);
        } else if (columnName.equals("tblcPurChallCmpTRowGrossAmt")) {
            (getTableRow().getItem()).setGross_amount(newValue.isEmpty() ? "0.0" : newValue);
        } else if (columnName.equals("tblcPurChallCmpTRowDisPer")) {
            (getTableRow().getItem()).setDis_per(newValue.isEmpty() ? "0.0" : newValue);
        } else if (columnName.equals("tblcPurChallCmpTRowDisAmt")) {
            (getTableRow().getItem()).setDis_amt(newValue.isEmpty() ? "0.0" : newValue);
        } else if (columnName.equals("tblcPurChallCmpTRowTaxPer")) {
            (getTableRow().getItem()).setTax(newValue.isEmpty() ? "0.0" : newValue);
        } else if (columnName.equals("tblcPurChallCmpTRowNetAmt")) {
            (getTableRow().getItem()).setNet_amount(newValue.isEmpty() ? "0.0" : newValue);
        }

    }
}









