package com.opethic.genivis.controller.tranx_sales;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.opethic.genivis.GenivisApplication;
import com.opethic.genivis.controller.dialogs.SingleInputDialogs;
import com.opethic.genivis.controller.master.ledger.common.LedgerMessageConsts;
import com.opethic.genivis.controller.tranx_sales.common.TranxCommonPopUps;
import com.opethic.genivis.dto.*;
import com.opethic.genivis.dto.pur_invoice.*;
import com.opethic.genivis.dto.reqres.product.Communicator;
import com.opethic.genivis.dto.reqres.product.TableCellCallback;
import com.opethic.genivis.dto.reqres.pur_tranx.PurTranxToProductRedirectionDTO;
import com.opethic.genivis.dto.reqres.pur_tranx.TranxPurRowDTO;
import com.opethic.genivis.dto.reqres.sales_tranx.SalesInvoiceTable;
import com.opethic.genivis.dto.reqres.sales_tranx.SalesOrderResponse;
import com.opethic.genivis.dto.reqres.sales_tranx.SalesOrderRow;
import com.opethic.genivis.dto.reqres.sales_tranx.SalesOrderTable;
import com.opethic.genivis.dto.reqres.tranx.sales.invoice.TranxRowConvRes;
import com.opethic.genivis.models.tranx.sales.TranxRow;
import com.opethic.genivis.models.tranx.sales.TranxSelectedBatch;
import com.opethic.genivis.models.tranx.sales.TranxSelectedProduct;
import com.opethic.genivis.network.APIClient;
import com.opethic.genivis.network.EndPoints;
import com.opethic.genivis.network.RequestType;
import com.opethic.genivis.utils.*;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.WorkerStateEvent;
import javafx.css.PseudoClass;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.URL;
import java.net.http.HttpResponse;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.regex.Pattern;

import static com.opethic.genivis.utils.FxmFileConstants.SALES_ORDER_LIST_SLUG;
import static com.opethic.genivis.utils.Globals.decimalFormat;
import static com.opethic.genivis.utils.Globals.mapToStringforFormData;
import static javafx.scene.control.PopupControl.USE_COMPUTED_SIZE;
import static javafx.scene.control.PopupControl.USE_PREF_SIZE;


public class SalesOrderController implements Initializable {
    @FXML
    private TabPane tpSalesOrder;
    @FXML
    private Tab tabSalesOrderLedger, tabSalesOrderProduct;
    @FXML
    private VBox totalmainDiv, soBottomSecondV, soBottomFirstV;
    @FXML
    private HBox topInnerHbOne, topInnerHbTwo, soBottomMain;

    @FXML
    private Label lbSalesOrderGstNo, lbSalesOrderContactPerson, lbSalesOrderCreditDays, lbSalesOrderLisenceNo, lbSalesOrderArea, lbSalesOrderTransport,
            lbSalesOrderFssai, lbSalesOrderRoute, lbSalesOrderBank, lbSalesOrderBrand, lbSalesOrderHsn, lbSalesOrderCost, lbSalesOrderGroup, lbSalesOrderTaxType,
            lbSalesOrderShelfid, lbSalesOrderCategory, lbSalesOrderTax, lbSalesOrderMinstock, lbSalesOrderSupplier, lbSalesOrderMargin, lbSalesOrderMaxstock,
             lbSalesOrderGrossTotal, lbSalesOrderDiscount, lbSalesOrderTotal, lbSalesOrderTotalTax, lbSalesOrderBillAmount;
    @FXML
    private Hyperlink hlSalesOrderQuot;
    private List<Integer> referenceSOId; //? Sales Order Ids
    private List<Integer> referenceSQId; //? Sales Qautation Ids

    private ObservableList<TranxRow> tblRowData = FXCollections.observableArrayList();
    private Stage stage = null;
    @FXML
    ComboBox<CommonDTO> cmbSalesOrderSalesAc;
    private ObservableList<GstDetailsDTO> supplierGSTINList = FXCollections.observableArrayList();
    @FXML
    private TextField tfSalesOrderLedgerName, tfSalesOrderSalesSerial, tfSalesOrderOrderNo, tfSalesOrderNarrations;
    @FXML
    private Button btnPurChallChooseFileButton, addRowInCmpTRow;
    @FXML
    private TableView<SalesOrderTable> tvSalesOrderCmpTRow;
    @FXML
    private TableView<SalesOrderSupplierDetailsDTO> tvSalesOrderSupplierDetails;
    @FXML
    private TableColumn<SalesOrderSupplierDetailsDTO, String> tcSalesOrderSupplerName, tcSalesOrderSupplierInvNo, tcSalesOrderSupplierInvDate, tcSalesOrderSupplierBatch, tcSalesOrderSupplierMrp, tcSalesOrderSupplierQty,
            tcSalesOrderSupplierRate, tcSalesOrderSupplierCost, tcSalesOrderSupplierDisper, tcSalesOrderSupplierDisRs;
    @FXML
    private TableColumn<SalesOrderTable, String> tcSalesOrderSrNo, tcSalesOrderCmpTRowA, tcSalesOrderCmpTRowB, tcSalesOrderCmpTRowC, tcSalesOrderCmpTRowParticulars, tcSalesOrderCmpTRowPacking, tcSalesOrderCmpTRowUnit, tcSalesOrderCmpTRowQuantity, tcSalesOrderCmpTRowFreeqty, tcSalesOrderCmpTRowRate,
            tcSalesOrderCmpTRowGrossAmount, tcSalesOrderCmpTRow1Disper, tcSalesOrderCmpTRowDisRs, tcSalesOrderCmpTRowTaxPer, tcSalesOrderCmpTRowNetAmount, tcSalesOrderCmpTRowActions;
    //    @FXML
//    private TableColumn tcSalesOrderCmpTRowParticulars, tcSalesOrderCmpTRowPacking, tcSalesOrderCmpTRowUnit, tcSalesOrderCmpTRowQuantity, tcSalesOrderCmpTRowRate,
//            tcSalesOrderCmpTRowGrossAmount, tcSalesOrderCmpTRow1Disper, tcSalesOrderCmpTRowDisRs, tcSalesOrderCmpTRowTaxPer, tcSalesOrderCmpTRowNetAmount;
    private ObservableList<CmpTRowDTO> itemList;
    @FXML
    private Button btnSalesOrderLedgerWindowClose, btnPurChallPrdWindowClose;
    private ToggleGroup toggleGroup = new ToggleGroup();
    @FXML
    private ComboBox<GstDetailsDTO> cmbSalesOrderClientGstin;
    @FXML
    private TextField dpSalesOrderTranxDate, dpSalesOrderOrderDate;
    //? this is for the Loggers of this Page
//    private static final Logger SalesOrderLogger = LoggerFactory.getLogger(SalesOrderController.class);
    @FXML
    private BorderPane spSalesOrderRootPane;
    private ObservableList<TranxLedgerWindowDTO> observableList;
    private String ledgerName = "";
    private String ledgerId, ledgerStateCode;//new
    private String productId;
    private static ObservableList<String> unitList = FXCollections.observableArrayList();
    private String responseBody;
    @FXML
    private Button btnSalesOrderSubmit, btnSalesOrderCancel, btnSalesOrderModify;
    private String selectedValue = "", id = "", message = "";
    private String finaltotalSGST = "", finaltotalCGST = "", finaltotalIGST = "", clientGSTIN = "", finalroundOff = "";
    private TableView<GstDTO> tblvGST;
    DecimalFormat decimalFormat = new DecimalFormat("0.#");
    private static final Logger logger = LogManager.getLogger(SalesOrderController.class);

    private JsonObject jsonObject = null;
    @FXML
    private VBox vboxSalesOrderRoot;

    private Integer rediCurrIndex = -1;
    private Boolean isProductRed = false;
    private Boolean isLedgerRed = false;
    private Boolean taxFlag = false;
    private ObservableList<GstDTO> gstDTOObservableList = FXCollections.observableArrayList();
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        ResponsiveWiseCssPicker();

        //? this opens the Quot Pop bill by click on the Number
        QuotPopup();

        //? this include all the Shortcut Keys
        initShortcutKeys();

        //? this inculde all the design related properties and important Fields
        mandatoryFields();

        Platform.runLater(() -> {
            //Focus on the First Field of the Page
            tfSalesOrderLedgerName.requestFocus();

            //? set the TranxDate Serial No TextField to Readonly
            dpSalesOrderTranxDate.setDisable(true);
            dpSalesOrderTranxDate.setStyle("-fx-opacity: 1;"); // Make textfield fully visible
            dpSalesOrderTranxDate.pseudoClassStateChanged(PseudoClass.getPseudoClass("disabled"), false);

            tfSalesOrderSalesSerial.setDisable(true);
            tfSalesOrderSalesSerial.setStyle("-fx-opacity: 1;"); // Make textfield fully visible
            tfSalesOrderSalesSerial.pseudoClassStateChanged(PseudoClass.getPseudoClass("disabled"), false);

            //? Open Ledger Modal on Space and on Click and on Ket Typed
            //? Mouse click event listener
            tfSalesOrderLedgerName.setOnMouseClicked(actionEvent -> openLedgerModal());

            //? Key pressed event listener
            tfSalesOrderLedgerName.setOnKeyPressed(keyEvent -> {
                if (keyEvent.getCode() == KeyCode.SPACE || keyEvent.getCode() == KeyCode.ENTER || keyEvent.getCode() == KeyCode.TAB) {
                    openLedgerModal();
                }
            });

            //? Key pressed event listener
//            tfSalesOrderLedgerName.setOnKeyTyped(keyEvent -> {
//                openLedgerModal();
//            });
        });

        //?  get the Sales accounts
        getSalesAccounts();

        btnSalesOrderSubmit.setOnAction(actionEvent -> {
            if (CommonValidationsUtils.validateForm(tfSalesOrderLedgerName, tfSalesOrderOrderNo, dpSalesOrderOrderDate)) {
                createSalesOrder();
            }
        });

        //? Open Create and Edit Page On id if ID is Null Open Create Page and If ID is Not Null Open Edit Page
        if (Globals.salesOrderListDTO != null) {
            System.out.println("id->" + Globals.salesOrderListDTO.getId());
            btnSalesOrderSubmit.setText("Update");
            btnSalesOrderModify.setVisible(false);
            tfSalesOrderOrderNo.focusedProperty().addListener((observable, oldValue, newValue) -> {
                if (!newValue) {
                    duplicateSalesOrderEdit();
                }
            });
            getEditDataById();
        } else {
            //?  get the Serial No and the Order No
            getSalesOrderSerialNo();
            Globals.salesOrderListDTO = null;
            tfSalesOrderOrderNo.focusedProperty().addListener((observable, oldValue, newValue) -> {
                if (!newValue) {
                    duplicateSalesOrder();
                }
            });
            //? Delayed initialization to ensure tgFranchiseIsFunding's width is properly calculated
        }


        tableInitiliazation();
    }

    private void initHL() {
        hlSalesOrderQuot.setOnAction((e) -> {
            System.out.println("as-->");
            if (Integer.valueOf(hlSalesOrderQuot.getText()) > 0 && referenceSOId.size() == 0 && referenceSQId.size() == 0) {
                stage = (Stage) spSalesOrderRootPane.getScene().getWindow();
                TranxCommonPopUps.openGetPendings(stage, ledgerId, "sales_quotation", "Sales Quotation", input -> {
                    System.out.println("sales quotation input => " + input);
                    referenceSQId = input;
                    if (input.size() > 0) getPendingSalesOrderWithids();
                });
            }
        });
    }
    private void getPendingSalesOrderWithids() {
        List<TranxRowConvRes> lstRowRes = TranxCommonPopUps.getPendingSalesQuotationWithIds(referenceSQId);
        setRowConvData(lstRowRes);
    }
    private void setRowConvData(List<TranxRowConvRes> row) {
        tvSalesOrderCmpTRow.getItems().clear();
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
                TranxSelectedBatch selectedBatch = TranxCommonPopUps.getSelectedBatchFromProductId(currRow.getProductId(), dpSalesOrderOrderDate.getText(), currRowRes.getBatchNo());
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
                logger.error("Exception in setRowConvData() :" + Globals.getExceptionString(e));
            }
        }
//        TranxCal();
    }
    private void QuotPopup() {
        hlSalesOrderQuot.setText(0 + "");
//        hlSaleInvCreateChallan.setText(0 + "");
        referenceSOId = new ArrayList<>();
        referenceSQId = new ArrayList<>();
        ledgerId = "";
        ledgerName = "";
//        tfTranxAdditionalTotal.clear();
        tfSalesOrderLedgerName.clear();
        cmbSalesOrderClientGstin.setValue(null);
        cmbSalesOrderSalesAc.setValue(null);
        tfSalesOrderSalesSerial.clear();
        tfSalesOrderOrderNo.clear();
//        cmbSaleInvCreateDeliveryType.setValue(null);
        tfSalesOrderNarrations.clear();
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
        tfSalesOrderLedgerName.setOnKeyPressed((e) -> {
            if (e.getCode().isLetterKey()) {
                openLedgerModal();
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
        GlobalTranx.requestFocusOrDieTrying(tfSalesOrderLedgerName, 3);
//        sceneInitilization();
//        initGstTable();
    }
    public void setPurChallDataToProduct() {

        String tranxDate = Communicator.text_to_date.fromString(dpSalesOrderTranxDate.getText()).toString();
        String ledgrId = ledgerId;
        String gstNum = cmbSalesOrderClientGstin.getId();
        String purSerNum = tfSalesOrderSalesSerial.getText();
        String challanNo = tfSalesOrderOrderNo.getText();
        String purAcc = cmbSalesOrderSalesAc.getId();
        String challanDate = Communicator.text_to_date.fromString(dpSalesOrderOrderDate.getText()).toString();
        // String saleOrderId = Globals.salesOrderListDTO.getId();
        // System.out.println("saleOrderId"+saleOrderId);

        PurTranxToProductRedirectionDTO mData = new PurTranxToProductRedirectionDTO();
        mData.setRedirect(FxmFileConstants.SALES_ORDER_CREATE_SLUG);
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

        //  mData.setPurChallEditId(Integer.valueOf(saleOrderId));
        mData.setRedirection(true);

        mData.setRediPrdCurrIndex(rediCurrIndex);
        List<SalesOrderTable> currentItems = new ArrayList<>(tvSalesOrderCmpTRow.getItems());
        List<SalesOrderTable> list = new ArrayList<>(currentItems);

        mData.setRowSaleOrder(list);

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
        PurTranxToProductRedirectionDTO franchiseDTO = (PurTranxToProductRedirectionDTO) inObj;
        Integer redirectIndex = franchiseDTO.getRediPrdCurrIndex();


        tfSalesOrderOrderNo.setText(franchiseDTO.getChallanNo());
        ledgerId=franchiseDTO.getLedegrId();
        tfSalesOrderSalesSerial.setText(franchiseDTO.getPurSerNum());
        tfSalesOrderLedgerName.setText(ledgersById(Long.valueOf(ledgerId)));
        int cnt=0,index=0;
        tvSalesOrderCmpTRow.getItems().clear();


        System.out.println(" Called >> " + supplierGSTINList.size());
        System.out.println(" Called ledger_id >> " + ledgerId);
        tfSalesOrderLedgerName.setText(ledgersById(Long.valueOf(ledgerId)));
        if (supplierGSTINList != null && !supplierGSTINList.isEmpty()) {
            cmbSalesOrderClientGstin.setItems(supplierGSTINList);
            cmbSalesOrderClientGstin.setValue(supplierGSTINList.get(0));
        }  System.out.println(" Called >> " + supplierGSTINList.size());
        System.out.println(" Called ledger_id >> " + ledgerId);
        tfSalesOrderLedgerName.setText(ledgersById(Long.valueOf(ledgerId)));
        if (supplierGSTINList != null && !supplierGSTINList.isEmpty()) {
            cmbSalesOrderClientGstin.setItems(supplierGSTINList);
            cmbSalesOrderClientGstin.setValue(supplierGSTINList.get(0));
        }

        PurInvoiceCommunicator.levelAForPurInvoiceObservableList.clear();
        if(franchiseDTO.getProductRed()==true) {
            Long productId = Long.valueOf(franchiseDTO.getRedirectProductId());
            int rindex = franchiseDTO.getRediPrdCurrIndex();
            JsonObject obj = productRedirectById(productId);

            for (SalesOrderTable purchaseInvoiceTable : franchiseDTO.getRowSaleOrder()) {
                if(index==rindex){
                    purchaseInvoiceTable.setProduct_id(productId+"");
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

                    purchaseInvoiceTable.setB_expiry("");
                    purchaseInvoiceTable.setManufacturing_date("");

                }
//            mData1.setParticulars(rowData.getParticulars());
//            mData1.setPackages(rowData.getPackages());
                tvSalesOrderCmpTRow.getItems().addAll(purchaseInvoiceTable);
                SalesOrderCalculation.rowCalculationForPurcInvoice(index, tvSalesOrderCmpTRow, callback);
                SalesOrderCalculation.calculateGst(tvSalesOrderCmpTRow, callback);
//                calculateGst(tableView, callback);

                List<LevelAForPurInvoice> levelAForPurInvoiceList = com.opethic.genivis.controller.tranx_sales.ProductUnitsPacking.getAllProductUnitsPackingFlavour(purchaseInvoiceTable.getProduct_id());
                ObservableList<LevelAForPurInvoice> observableLevelAList = FXCollections.observableArrayList(levelAForPurInvoiceList);
                if (index >= 0 && index < PurInvoiceCommunicator.levelAForPurInvoiceObservableList.size()) {
                    PurInvoiceCommunicator.levelAForPurInvoiceObservableList.set(index, observableLevelAList);

                    tvSalesOrderCmpTRow.getItems().get(index).setLevelA(null);
                    tvSalesOrderCmpTRow.getItems().get(index).setLevelA(levelAForPurInvoiceList.get(0).getLabel());

                    for (LevelAForPurInvoice levelAForPurInvoice : PurInvoiceCommunicator.levelAForPurInvoiceObservableList.get(index)) {
                        if (tvSalesOrderCmpTRow.getItems().get(index).getLevelA_id().equals(levelAForPurInvoice.getValue())) {
                            tvSalesOrderCmpTRow.getItems().get(index).setLevelA(null);
                            tvSalesOrderCmpTRow.getItems().get(index).setLevelA(levelAForPurInvoice.getLabel());
                        }
                    }

                } else {
//                    System.out.println("Index else >> : " + index);
                    PurInvoiceCommunicator.levelAForPurInvoiceObservableList.add(observableLevelAList);
                }

                cnt++;
                index++;
            }
        }

        int inx=0;
        System.out.println("is ledgeriiiiiiiiiii"+franchiseDTO.getLedgerRed());
        // tblvPurChallCmpTRow.getItems().clear();
        if(franchiseDTO.getLedgerRed()==true) {
            System.out.println("is ledger"+franchiseDTO.getLedgerRed());
            for (SalesOrderTable rowData : franchiseDTO.getRowSaleOrder()) {


                System.out.println("String.valueOf(rowData.getRowData().get(0))" + rowData.getProduct_id());
//                mData1.setProduct_id(rowData.getProduct_id());
//                mData1.setParticulars(rowData.getParticulars());
//                mData1.setPackages(rowData.getPackages());
                List<LevelAForPurInvoice> levelAForPurInvoiceList = com.opethic.genivis.controller.tranx_sales.ProductUnitsPacking.getAllProductUnitsPackingFlavour(rowData.getProduct_id());
                ObservableList<LevelAForPurInvoice> observableLevelAList = FXCollections.observableArrayList(levelAForPurInvoiceList);
                if (index >= 0 && index < PurInvoiceCommunicator.levelAForPurInvoiceObservableList.size()) {
                    PurInvoiceCommunicator.levelAForPurInvoiceObservableList.set(index, observableLevelAList);

                    tvSalesOrderCmpTRow.getItems().get(index).setLevelA(null);
                    tvSalesOrderCmpTRow.getItems().get(index).setLevelA(levelAForPurInvoiceList.get(0).getLabel());

                    for (LevelAForPurInvoice levelAForPurInvoice : PurInvoiceCommunicator.levelAForPurInvoiceObservableList.get(index)) {
                        if (tvSalesOrderCmpTRow.getItems().get(index).getLevelA_id().equals(levelAForPurInvoice.getValue())) {
                            tvSalesOrderCmpTRow.getItems().get(index).setLevelA(null);
                            tvSalesOrderCmpTRow.getItems().get(index).setLevelA(levelAForPurInvoice.getLabel());
                        }
                    }

                } else {
                    System.out.println("Index else >> : " + index);
                    PurInvoiceCommunicator.levelAForPurInvoiceObservableList.add(observableLevelAList);
                }


                tvSalesOrderCmpTRow.getItems().add(rowData);
                SalesOrderCalculation.rowCalculationForPurcInvoice(index, tvSalesOrderCmpTRow, callback);
                SalesOrderCalculation.calculateGst(tvSalesOrderCmpTRow, callback);
                inx++;
            }
        }

        Platform.runLater(() -> {
            TableColumn<SalesOrderTable, ?> colName = tvSalesOrderCmpTRow.getColumns().get(1);
            tvSalesOrderCmpTRow.edit(redirectIndex, colName);

        });


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
                // .add(new CommonDTO(gstin, gst_id));
            }
        }
        if (ledgerStateCode.equalsIgnoreCase(GlobalTranx.getCompanyStateCode().toString())) {
            taxFlag = true;
        }
        return ledgerName;
    }

    public JsonObject productRedirectById(Long id) {

        Map<String, String> map = new HashMap<>();
        map.put("product_id", String.valueOf(id));

        String formData = mapToStringforFormData(map);
        HttpResponse<String> response = APIClient.postFormDataRequest(formData, "transaction_product_details");

        String json = response.body();
        JsonObject jsonObject = new Gson().fromJson(json, JsonObject.class);
        JsonObject responseObj = jsonObject.getAsJsonObject("result");
//        System.out.println("responseObj" + responseObj);
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

    private void ResponsiveWiseCssPicker() {
        double height = Screen.getPrimary().getBounds().getHeight();
        double width = Screen.getPrimary().getBounds().getWidth();
//        System.out.println("width" + width);
        if (width >= 992 && width <= 1024) {
            totalmainDiv.setSpacing(10);
            topInnerHbOne.setSpacing(12);
            topInnerHbTwo.setSpacing(12);
            soBottomFirstV.prefWidthProperty().bind(soBottomMain.widthProperty().multiply(0.76));
            soBottomSecondV.prefWidthProperty().bind(soBottomMain.widthProperty().multiply(0.24));
            tvSalesOrderSupplierDetails.getStylesheets().add(GenivisApplication.class.getResource("ui/css/purchase_challan_css/supplier_table_css/supplier_table_992_1024.css").toExternalForm());
            spSalesOrderRootPane.getStylesheets().add(GenivisApplication.class.getResource("ui/css/TranxCommonCssStyles/tranxCommonCssStyle1.css").toExternalForm());
        } else if (width >= 1025 && width <= 1280) {
            totalmainDiv.setSpacing(10);
            topInnerHbOne.setSpacing(12);
            topInnerHbTwo.setSpacing(12);
            soBottomFirstV.prefWidthProperty().bind(soBottomMain.widthProperty().multiply(0.76));
            soBottomSecondV.prefWidthProperty().bind(soBottomMain.widthProperty().multiply(0.24));
            tvSalesOrderSupplierDetails.getStylesheets().add(GenivisApplication.class.getResource("ui/css/purchase_challan_css/supplier_table_css/supplier_table_1025_1280.css").toExternalForm());
            spSalesOrderRootPane.getStylesheets().add(GenivisApplication.class.getResource("ui/css/TranxCommonCssStyles/tranxCommonCssStyle2.css").toExternalForm());
        } else if (width >= 1281 && width <= 1368) {
            totalmainDiv.setSpacing(10);
            topInnerHbOne.setSpacing(12);
            topInnerHbTwo.setSpacing(12);
            soBottomFirstV.prefWidthProperty().bind(soBottomMain.widthProperty().multiply(0.76));
            soBottomSecondV.prefWidthProperty().bind(soBottomMain.widthProperty().multiply(0.24));
            tvSalesOrderSupplierDetails.getStylesheets().add(GenivisApplication.class.getResource("ui/css/purchase_challan_css/supplier_table_css/supplier_table_1281_1368.css").toExternalForm());
            spSalesOrderRootPane.getStylesheets().add(GenivisApplication.class.getResource("ui/css/TranxCommonCssStyles/tranxCommonCssStyle3.css").toExternalForm());
        } else if (width >= 1369 && width <= 1400) {
            totalmainDiv.setSpacing(10);
            topInnerHbOne.setSpacing(15);
            topInnerHbTwo.setSpacing(15);
            soBottomFirstV.prefWidthProperty().bind(soBottomMain.widthProperty().multiply(0.76));
            soBottomSecondV.prefWidthProperty().bind(soBottomMain.widthProperty().multiply(0.24));
            tvSalesOrderSupplierDetails.getStylesheets().add(GenivisApplication.class.getResource("ui/css/purchase_challan_css/supplier_table_css/supplier_table_1369_1400.css").toExternalForm());
            spSalesOrderRootPane.getStylesheets().add(GenivisApplication.class.getResource("ui/css/TranxCommonCssStyles/tranxCommonCssStyle4.css").toExternalForm());
        } else if (width >= 1401 && width <= 1440) {
            totalmainDiv.setSpacing(15);
            topInnerHbOne.setSpacing(15);
            topInnerHbTwo.setSpacing(15);
            soBottomFirstV.prefWidthProperty().bind(soBottomMain.widthProperty().multiply(0.78));
            soBottomSecondV.prefWidthProperty().bind(soBottomMain.widthProperty().multiply(0.22));
            spSalesOrderRootPane.getStylesheets().add(GenivisApplication.class.getResource("ui/css/TranxCommonCssStyles/tranxCommonCssStyle5.css").toExternalForm());
            tvSalesOrderSupplierDetails.getStylesheets().add(GenivisApplication.class.getResource("ui/css/purchase_challan_css/supplier_table_css/supplier_table_1401_1440.css").toExternalForm());
        } else if (width >= 1441 && width <= 1680) {
            totalmainDiv.setSpacing(18);
            topInnerHbOne.setSpacing(15);
            topInnerHbTwo.setSpacing(15);
            soBottomFirstV.prefWidthProperty().bind(soBottomMain.widthProperty().multiply(0.8));
            soBottomSecondV.prefWidthProperty().bind(soBottomMain.widthProperty().multiply(0.2));
            tvSalesOrderSupplierDetails.getStylesheets().add(GenivisApplication.class.getResource("ui/css/purchase_challan_css/supplier_table_css/supplier_table_1441_1680.css").toExternalForm());
            spSalesOrderRootPane.getStylesheets().add(GenivisApplication.class.getResource("ui/css/TranxCommonCssStyles/tranxCommonCssStyle6.css").toExternalForm());
        } else if (width >= 1681 && width <= 1920) {
            totalmainDiv.setSpacing(20);
            topInnerHbOne.setSpacing(20);
            topInnerHbTwo.setSpacing(20);
            soBottomFirstV.prefWidthProperty().bind(soBottomMain.widthProperty().multiply(0.8));
            soBottomSecondV.prefWidthProperty().bind(soBottomMain.widthProperty().multiply(0.2));
//            sqBottomMain.setPrefHeight(150);
            tvSalesOrderSupplierDetails.getStylesheets().add(GenivisApplication.class.getResource("ui/css/invoice_product_history_table.css").toExternalForm());
            spSalesOrderRootPane.getStylesheets().add(GenivisApplication.class.getResource("ui/css/TranxCommonCssStyles/tranxCommonCssStyle7.css").toExternalForm());
        }
    }

    private void initShortcutKeys() {
        //Enter,Tab Key Cursor Movement
        spSalesOrderRootPane.addEventFilter(KeyEvent.KEY_PRESSED, (KeyEvent event) -> {

            if (event.getCode() == KeyCode.ENTER) {
                if (event.getTarget() instanceof Button targetButton && targetButton.getText().equalsIgnoreCase("submit")) {
                } else if (event.getTarget() instanceof Button targetButton && targetButton.getText().equalsIgnoreCase("update")) {
                } else if (event.getTarget() instanceof Button targetButton && targetButton.getText().equalsIgnoreCase("cancel")) {
                } else if (event.getTarget() instanceof Button targetButton && targetButton.getText().equalsIgnoreCase("modify")) {
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
                btnSalesOrderSubmit.fire();
            } else if (event.getCode() == KeyCode.X && event.isControlDown()) {
                if (Globals.salesOrderListDTO != null) {
                    btnSalesOrderModify.fire();
                } else {
                    btnSalesOrderCancel.fire();
                }
            } else if (event.getCode() == KeyCode.E && event.isControlDown()) {
                btnSalesOrderModify.fire();
            }
        });
    }

    private void mandatoryFields() {

        tvSalesOrderCmpTRow.setFocusTraversable(false);
        tvSalesOrderSupplierDetails.setFocusTraversable(false);
        hlSalesOrderQuot.setFocusTraversable(false);

        //? Set default value to current local date
        LocalDate tranxDate = LocalDate.now();
        LocalDate orderDate = LocalDate.now();
        dpSalesOrderTranxDate.setText(tranxDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        dpSalesOrderOrderDate.setText(orderDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        //DatePicker to TextField with validation
        DateValidator.applyDateFormat(dpSalesOrderTranxDate);
        DateValidator.applyDateFormat(dpSalesOrderOrderDate);
        sceneInitilization();

        //Change the Color of * Mandatory Fields to Red
//        CommonValidationsUtils.changeStarColour(vboxSalesOrderRoot);

//        spSalesOrderRootPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
//        spSalesOrderRootPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        //? Row Design
        SalesOrderCmpTRowTableDesign();
        SalesOrderBottomTableDesign();

        //Set the TableView Columns with proper height and Width
        tvSalesOrderSupplierDetails.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        CommonFunctionalUtils.restrictTextFieldToDigitsAndDateFormat(dpSalesOrderTranxDate);
        CommonFunctionalUtils.tranxDateGreaterThanCurrentDate(dpSalesOrderOrderDate);
        CommonValidationsUtils.comboBoxDataShow(cmbSalesOrderClientGstin);
        CommonValidationsUtils.comboBoxDataShow(cmbSalesOrderSalesAc);

        //Stop the Cusor on the Field
        tfSalesOrderLedgerName.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal) {
                String text = tfSalesOrderLedgerName.getText().trim();
                if (text.isEmpty()) {
                    tfSalesOrderLedgerName.requestFocus();
                }
            }
        });
        tfSalesOrderOrderNo.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal) {
                String text = tfSalesOrderOrderNo.getText().trim();
                if (text.isEmpty()) {
                    tfSalesOrderOrderNo.requestFocus();
                }
            }
        });
        dpSalesOrderOrderDate.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal) {
                String text = dpSalesOrderOrderDate.getText().trim();
                if (text.isEmpty()) {
                    dpSalesOrderOrderDate.requestFocus();
                }
            }
        });
    }

    //? Validate Code for Franchise Create
    public void duplicateSalesOrder() {
        APIClient apiClient = null;
        try {
            logger.debug("Get duplicateSalesOrder Data Started...");
            Map<String, String> map = new HashMap<>();
            map.put("salesOrderNo", String.valueOf(tfSalesOrderOrderNo.getText()));
            String formData = Globals.mapToStringforFormData(map);

//            HttpResponse<String> response = APIClient.postFormDataRequest(formData, EndPoints.VALIDATE_PINCODE_ENDPOINT);
            apiClient = new APIClient(EndPoints.SALES_ORDER_NO_DUPLICATE_ENDPOINT, formData, RequestType.FORM_DATA);

            apiClient.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
                @Override
                public void handle(WorkerStateEvent workerStateEvent) {
                    jsonObject = new Gson().fromJson(workerStateEvent.getSource().getValue().toString(), JsonObject.class);

//                    responseBody = response.body();
//                    JsonObject jsonObject = new Gson().fromJson(responseBody, JsonObject.class);
                    if (jsonObject.get("responseStatus").getAsInt() == 200) {
                        Platform.runLater(() -> {
//                            tfFranchiseCreateFranchiseName.requestFocus(); // Set focus back to tfFranchiseCreateRPincode
                        });
                    } else {
                        String message = jsonObject.get("message").getAsString();

//                        if(!tfSalesOrderOrderNo.getText().isEmpty()){
//                        AlertUtility.AlertDialogForError("WARNING", message, input -> {
//                            if (input) {
//                                tfSalesOrderOrderNo.requestFocus();
//                            }
//                        });
                        AlertUtility.AlertErrorTimeout(AlertUtility.alertTypeError, message, in -> {
                            tfSalesOrderOrderNo.requestFocus();
                        });
                        Platform.runLater(() -> {
                            tfSalesOrderOrderNo.requestFocus(); // Set focus back to tfFranchiseCreateRPincode
                        });
//                        }

//                tfFranchiseCreatePincode.setText("");
                    }
                }
            });
            apiClient.setOnCancelled(new EventHandler<WorkerStateEvent>() {
                @Override
                public void handle(WorkerStateEvent workerStateEvent) {
                    logger.error("Network API cancelled in duplicateSalesOrder()" + workerStateEvent.getSource().getValue().toString());

                }
            });

            apiClient.setOnFailed(new EventHandler<WorkerStateEvent>() {
                @Override
                public void handle(WorkerStateEvent workerStateEvent) {
                    logger.error("Network API failed in duplicateSalesOrder()" + workerStateEvent.getSource().getValue().toString());
                }
            });
            apiClient.start();
            logger.debug("Get duplicateSalesOrder Data End...");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            apiClient = null;
        }
    }

    public void duplicateSalesOrderEdit() {
        APIClient apiClient = null;
        try {
            logger.debug("Get duplicateSalesOrderEdit Data Started...");
            String id = Globals.salesOrderListDTO != null ? Globals.salesOrderListDTO.getId() : "";
            Map<String, String> map = new HashMap<>();
            map.put("invoice_id", id);
            map.put("salesOrderNo", String.valueOf(tfSalesOrderOrderNo.getText()));
            String formData = Globals.mapToStringforFormData(map);
//            HttpResponse<String> response = APIClient.postFormDataRequest(formData, EndPoints.VALIDATE_PINCODE_ENDPOINT);
            apiClient = new APIClient(EndPoints.SALES_ORDER_NO_DUPLICATE_UPDATE_ENDPOINT, formData, RequestType.FORM_DATA);

            apiClient.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
                @Override
                public void handle(WorkerStateEvent workerStateEvent) {
                    jsonObject = new Gson().fromJson(workerStateEvent.getSource().getValue().toString(), JsonObject.class);
//                    responseBody = response.body();
//                    JsonObject jsonObject = new Gson().fromJson(responseBody, JsonObject.class);
                    if (jsonObject.get("responseStatus").getAsInt() == 200) {
                        Platform.runLater(() -> {
//                            tfFranchiseCreateFranchiseName.requestFocus(); // Set focus back to tfFranchiseCreateRPincode
                        });
                    } else {
                        String message = jsonObject.get("message").getAsString();

//                        if(!tfSalesOrderOrderNo.getText().isEmpty()){
//                        AlertUtility.AlertDialogForError("WARNING", message, input -> {
//                            if (input) {
//                                tfSalesOrderOrderNo.requestFocus();
//                            }
//                        });
                        AlertUtility.AlertErrorTimeout(AlertUtility.alertTypeError, message, in -> {
                            tfSalesOrderOrderNo.requestFocus();
                        });
                        Platform.runLater(() -> {
                            tfSalesOrderOrderNo.requestFocus(); // Set focus back to tfFranchiseCreateRPincode
                        });
//                        }

//                tfFranchiseCreatePincode.setText("");
                    }
                }
            });
            apiClient.setOnCancelled(new EventHandler<WorkerStateEvent>() {
                @Override
                public void handle(WorkerStateEvent workerStateEvent) {
                    logger.error("Network API cancelled in duplicateSalesOrderEdit()" + workerStateEvent.getSource().getValue().toString());

                }
            });

            apiClient.setOnFailed(new EventHandler<WorkerStateEvent>() {
                @Override
                public void handle(WorkerStateEvent workerStateEvent) {
                    logger.error("Network API failed in duplicateSalesOrderEdit()" + workerStateEvent.getSource().getValue().toString());
                }
            });
            apiClient.start();
            logger.debug("Get duplicateSalesOrderEdit Data End...");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            apiClient = null;
        }
    }

    // Function to open ledger modal
//    private void openLedgerModal() {
//        Stage stage = (Stage) spSalesOrderRootPane.getScene().getWindow();
//        SingleInputDialogs.openLedgerPopUp(stage, "Debtors", input -> {
//            // Callback to Get all the Details of the Selected ledger and Set it in the Fields
//            ledgerName = (String) input[0];
//            ledgerId = (String) input[1];
//            System.out.println("input[3] in sale order--- " + input[3]);
//            ledgerStateCode = (String) input[3];//new
//            tranxLedgerDetailsFun(ledgerId);
//            ObservableList<GstDetailsDTO> gstDetails = (ObservableList<GstDetailsDTO>) input[2];
//            tfSalesOrderLedgerName.setText(ledgerName);
//            if (gstDetails != null) {
//                setLedgerGSTDATA(gstDetails);
//            }
//        },in->{
//                    System.out.println("In >> Called >. "+ in);
//                    if(in == true){
//                        isLedgerRed = true;
//                        setPurChallDataToProduct();
//                    }
//                }
//
//        );
//    }
    private void openLedgerModal() {
        Stage stage = (Stage) spSalesOrderRootPane.getScene().getWindow();
        SingleInputDialogs.openSalesLedgerWithNamePopUp(stage, "Filter", ledgerName, input -> {
                    // Callback to Get all the Details of the Selected ledger and Set it in the Fields
                    ledgerName = (String) input[0];
                    ledgerId = (String) input[1];
                    ledgerStateCode = (String) input[6];//new
                    hlSalesOrderQuot.setText((String) input[3]);
                    tranxLedgerDetailsFun(ledgerId);
                    ObservableList<GstDetailsDTO> gstDetails = (ObservableList<GstDetailsDTO>) input[2];
                    tfSalesOrderLedgerName.setText(ledgerName);
                    if (gstDetails != null) {
                        setLedgerGSTDATA(gstDetails);
                    }
                }, in -> {
//                    System.out.println("In >> Called >. " + in);
                    if (in == true) {
                        isLedgerRed = true;
                        setPurChallDataToProduct();
                    }
                }

        );
    }

    //? On Clicking on the Cancel Button From Create or Edit page redirect Back to List page
    public void backToList() {
        Globals.salesOrderListDTO = null;
            AlertUtility.AlertConfirmation(AlertUtility.alertTypeConfirmation, LedgerMessageConsts.msgConfirmationOnCancel + tfSalesOrderOrderNo.getText() + LedgerMessageConsts.msgInvoiceNumber, input -> {
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
        tfSalesOrderLedgerName.setText("");
        cmbSalesOrderClientGstin.getSelectionModel().clearSelection();
//        tvSalesQuotCreateprodInvoiceDetails.setItems(emptyList1);
        tfSalesOrderNarrations.setText("");

        //? clear Ledger Info
        lbSalesOrderGstNo.setText("");
        lbSalesOrderArea.setText("");
        lbSalesOrderBank.setText("");
        lbSalesOrderContactPerson.setText("");
        lbSalesOrderFssai.setText("");
        lbSalesOrderLisenceNo.setText("");
        lbSalesOrderRoute.setText("");
        lbSalesOrderTransport.setText("");
        lbSalesOrderCreditDays.setText("");

        //? clear Product Info
        lbSalesOrderBrand.setText("");
        lbSalesOrderGroup.setText("");
        lbSalesOrderSupplier.setText("");
        lbSalesOrderCategory.setText("");
        lbSalesOrderHsn.setText("");
        lbSalesOrderTaxType.setText("");
        lbSalesOrderTax.setText("");
        lbSalesOrderMargin.setText("");
        lbSalesOrderCost.setText("");
        lbSalesOrderShelfid.setText("");
        lbSalesOrderMinstock.setText("");
        lbSalesOrderMaxstock.setText("");

        //clear all row calculation
        lbSalesOrderBillAmount.setText("0.00");

        lbSalesOrderGrossTotal.setText("0.00");

        lbSalesOrderDiscount.setText("0.00");

        lbSalesOrderTotalTax.setText("0.00");

        lbSalesOrderTotal.setText("0.00");

        tvSalesOrderCmpTRow.getItems().clear();// Add a new blank row if needed
        tvSalesOrderCmpTRow.getItems().add(new SalesOrderTable("", "0", "1", "", "", "", "", "", "", "", "", "", "", "", "", "", "", ""));
        tfSalesOrderLedgerName.requestFocus();
        tvSalesOrderSupplierDetails.getItems().clear();
        PurInvoiceCommunicator.resetFields();//new 2.0 to Clear the CMPTROW ComboBox

    }

    //? On Clicking on the Modify Button From Create or Edit page redirect Back to List page
    public void backToListModify() {
        Globals.salesOrderListDTO = null;
        AlertUtility.AlertConfirmation(AlertUtility.alertTypeConfirmation, LedgerMessageConsts.msgGoList, input -> {
            if (input == 1) {
                GlobalController.getInstance().addTabStatic(SALES_ORDER_LIST_SLUG, false);
            }
        });
    }

    public void sceneInitilization() {
        PurInvoiceCommunicator.resetFields();//new 2.0 to Clear the CMPTROW ComboBox
        spSalesOrderRootPane.sceneProperty().addListener((observable, oldScene, newScene) -> {
            if (newScene != null && newScene.getWindow() instanceof Stage) {
                Communicator.stage = (Stage) newScene.getWindow();
            }
        });
    }

    public void SalesOrderCmpTRowTableDesign() {
        //?  Responsive code for tableview
        tcSalesOrderSrNo.prefWidthProperty().bind(tvSalesOrderCmpTRow.widthProperty().multiply(0.05));
//        tcSalesOrderCmpTRowParticulars.prefWidthProperty().bind(tvSalesOrderCmpTRow.widthProperty().multiply(0.3));
        tcSalesOrderCmpTRowPacking.prefWidthProperty().bind(tvSalesOrderCmpTRow.widthProperty().multiply(0.05));
        tcSalesOrderCmpTRowUnit.prefWidthProperty().bind(tvSalesOrderCmpTRow.widthProperty().multiply(0.05));
        tcSalesOrderCmpTRowQuantity.prefWidthProperty().bind(tvSalesOrderCmpTRow.widthProperty().multiply(0.05));
//        tcSalesOrderCmpTRowGrossAmount.prefWidthProperty().bind(tvSalesOrderCmpTRow.widthProperty().multiply(0.15));
        tcSalesOrderCmpTRow1Disper.prefWidthProperty().bind(tvSalesOrderCmpTRow.widthProperty().multiply(0.05));
        tcSalesOrderCmpTRowDisRs.prefWidthProperty().bind(tvSalesOrderCmpTRow.widthProperty().multiply(0.05));
        tcSalesOrderCmpTRowTaxPer.prefWidthProperty().bind(tvSalesOrderCmpTRow.widthProperty().multiply(0.05));
//        tcSalesOrderCmpTRowNetAmount.prefWidthProperty().bind(tvSalesOrderCmpTRow.widthProperty().multiply(0.08));
        tcSalesOrderCmpTRowActions.prefWidthProperty().bind(tvSalesOrderCmpTRow.widthProperty().multiply(0.02));
        if (Globals.getUserControlsWithSlug("is_free_qty")==true) {
            tcSalesOrderCmpTRowFreeqty.prefWidthProperty().bind(tvSalesOrderCmpTRow.widthProperty().multiply(0.05));
            tcSalesOrderCmpTRowRate.prefWidthProperty().bind(tvSalesOrderCmpTRow.widthProperty().multiply(0.05));
        } else {
            tcSalesOrderCmpTRowRate.prefWidthProperty().bind(tvSalesOrderCmpTRow.widthProperty().multiply(0.1));
        }


        if (Globals.getUserControlsWithSlug("is_level_a") == true && Globals.getUserControlsWithSlug("is_level_b") == false && Globals.getUserControlsWithSlug("is_level_c") == false) {
//            System.out.println("inside first condn");
            tcSalesOrderCmpTRowParticulars.prefWidthProperty().bind(tvSalesOrderCmpTRow.widthProperty().multiply(0.3));
            tcSalesOrderCmpTRowGrossAmount.prefWidthProperty().bind(tvSalesOrderCmpTRow.widthProperty().multiply(0.13));
            tcSalesOrderCmpTRowNetAmount.prefWidthProperty().bind(tvSalesOrderCmpTRow.widthProperty().multiply(0.05));
            tcSalesOrderCmpTRowA.prefWidthProperty().bind(tvSalesOrderCmpTRow.widthProperty().multiply(0.05));
        } else if (Globals.getUserControlsWithSlug("is_level_a") == true && Globals.getUserControlsWithSlug("is_level_b") == true && Globals.getUserControlsWithSlug("is_level_c") == false) {
//            System.out.println("inside second condn");
            tcSalesOrderCmpTRowA.prefWidthProperty().bind(tvSalesOrderCmpTRow.widthProperty().multiply(0.05));
            tcSalesOrderCmpTRowParticulars.prefWidthProperty().bind(tvSalesOrderCmpTRow.widthProperty().multiply(0.28));
            tcSalesOrderCmpTRowGrossAmount.prefWidthProperty().bind(tvSalesOrderCmpTRow.widthProperty().multiply(0.1));
            tcSalesOrderCmpTRowNetAmount.prefWidthProperty().bind(tvSalesOrderCmpTRow.widthProperty().multiply(0.05));
            tcSalesOrderCmpTRowB.prefWidthProperty().bind(tvSalesOrderCmpTRow.widthProperty().multiply(0.05));
        } else if (Globals.getUserControlsWithSlug("is_level_a") == true && Globals.getUserControlsWithSlug("is_level_b") == true && Globals.getUserControlsWithSlug("is_level_c") == true) {
//            System.out.println("inside thrd condn");
            tcSalesOrderCmpTRowParticulars.prefWidthProperty().bind(tvSalesOrderCmpTRow.widthProperty().multiply(0.23));
            tcSalesOrderCmpTRowGrossAmount.prefWidthProperty().bind(tvSalesOrderCmpTRow.widthProperty().multiply(0.1));
            tcSalesOrderCmpTRowNetAmount.prefWidthProperty().bind(tvSalesOrderCmpTRow.widthProperty().multiply(0.05));
            tcSalesOrderCmpTRowA.prefWidthProperty().bind(tvSalesOrderCmpTRow.widthProperty().multiply(0.05));
            tcSalesOrderCmpTRowB.prefWidthProperty().bind(tvSalesOrderCmpTRow.widthProperty().multiply(0.05));
            tcSalesOrderCmpTRowC.prefWidthProperty().bind(tvSalesOrderCmpTRow.widthProperty().multiply(0.05));
        } else {
//            System.out.println("inside fourth condn");
            tcSalesOrderCmpTRowParticulars.prefWidthProperty().bind(tvSalesOrderCmpTRow.widthProperty().multiply(0.35));
            tcSalesOrderCmpTRowGrossAmount.prefWidthProperty().bind(tvSalesOrderCmpTRow.widthProperty().multiply(0.1));
            tcSalesOrderCmpTRowNetAmount.prefWidthProperty().bind(tvSalesOrderCmpTRow.widthProperty().multiply(0.08));

        }

    }

    //Sales Quotation Bottom table data

    public void SalesOrderBottomTableDesign() {
        //?  Responsive code for bottom productInfo tableview
        tcSalesOrderSupplerName.prefWidthProperty().bind(tvSalesOrderSupplierDetails.widthProperty().multiply(0.2));
        tcSalesOrderSupplierInvNo.prefWidthProperty().bind(tvSalesOrderSupplierDetails.widthProperty().multiply(0.1));
        tcSalesOrderSupplierInvDate.prefWidthProperty().bind(tvSalesOrderSupplierDetails.widthProperty().multiply(0.1));
        tcSalesOrderSupplierBatch.prefWidthProperty().bind(tvSalesOrderSupplierDetails.widthProperty().multiply(0.1));
        tcSalesOrderSupplierMrp.prefWidthProperty().bind(tvSalesOrderSupplierDetails.widthProperty().multiply(0.1));
        tcSalesOrderSupplierQty.prefWidthProperty().bind(tvSalesOrderSupplierDetails.widthProperty().multiply(0.1));
        tcSalesOrderSupplierRate.prefWidthProperty().bind(tvSalesOrderSupplierDetails.widthProperty().multiply(0.05));
        tcSalesOrderSupplierCost.prefWidthProperty().bind(tvSalesOrderSupplierDetails.widthProperty().multiply(0.1));
        tcSalesOrderSupplierDisper.prefWidthProperty().bind(tvSalesOrderSupplierDetails.widthProperty().multiply(0.05));
        tcSalesOrderSupplierDisRs.prefWidthProperty().bind(tvSalesOrderSupplierDetails.widthProperty().multiply(0.1));

    }

    //? To Set the GST of the Ledger
    private void setLedgerGSTDATA(ObservableList<GstDetailsDTO> gstDetails) {
        if (gstDetails != null) {

            cmbSalesOrderClientGstin.setItems(gstDetails);
            cmbSalesOrderClientGstin.setConverter(new StringConverter<GstDetailsDTO>() {
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
            for (Object obj : cmbSalesOrderClientGstin.getItems()) {
                GstDetailsDTO gstDetailsDTO = (GstDetailsDTO) obj;
                if (gstDetailsDTO.getId().equals(gstDetails.get(0).getId())) {
                    selectedGst = gstDetailsDTO;
                    break;
                }
            }
            if (selectedGst != null) {
                cmbSalesOrderClientGstin.getSelectionModel().select(selectedGst);
                cmbSalesOrderClientGstin.requestFocus();
            } else {
                tfSalesOrderOrderNo.requestFocus();
            }
        }
    }

    //?  get and set the Sales accounts
    public void getSalesAccounts() {
        APIClient apiClient = null;
        try {
            logger.debug("Get Sales Account Data Started...");
//            HttpResponse<String> response = APIClient.getRequest(EndPoints.SALESORDER_GET_SALES_ACCOUNTS);
            apiClient = new APIClient(EndPoints.SALESORDER_GET_SALES_ACCOUNTS, "", RequestType.GET);

            apiClient.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
                @Override
                public void handle(WorkerStateEvent workerStateEvent) {
                    jsonObject = new Gson().fromJson(workerStateEvent.getSource().getValue().toString(), JsonObject.class);

//                    JsonObject jsonObject = new Gson().fromJson(response.body(), JsonObject.class);
//                    System.out.println(jsonObject);
                    ObservableList<CommonDTO> observableList = FXCollections.observableArrayList();
                    if (jsonObject.get("responseStatus").getAsInt() == 200) {
                        JsonArray responseObject = jsonObject.getAsJsonArray("list");
//                        System.out.println("responseObject getSalesAccounts " + responseObject);


                        if (responseObject.size() > 0) {
                            for (JsonElement element : responseObject) {
                                JsonObject item = element.getAsJsonObject();
                                String id = item.get("id").getAsString();
                                String name = item.get("name").getAsString();
                                String unique_code = item.get("unique_code").getAsString();


                                observableList.add(new CommonDTO(name, id));
                                cmbSalesOrderSalesAc.setItems(observableList);
                                cmbSalesOrderSalesAc.getSelectionModel().selectFirst();
                                cmbSalesOrderSalesAc.setConverter(new StringConverter<CommonDTO>() {
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
                            System.out.println("responseObject is null");
                        }
                    } else {
                        System.out.println("Error in response");
                    }
                }
            });
            apiClient.setOnCancelled(new EventHandler<WorkerStateEvent>() {
                @Override
                public void handle(WorkerStateEvent workerStateEvent) {
                    logger.error("Network API cancelled in getSalesAccounts()" + workerStateEvent.getSource().getValue().toString());

                }
            });

            apiClient.setOnFailed(new EventHandler<WorkerStateEvent>() {
                @Override
                public void handle(WorkerStateEvent workerStateEvent) {
                    logger.error("Network API failed in getSalesAccounts()" + workerStateEvent.getSource().getValue().toString());
                }
            });
            apiClient.start();
            logger.debug("Get Sales Account Data End...");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //?  get and set the Serial No and Order No
    private void getSalesOrderSerialNo() {
        APIClient apiClient = null;
        try {
            logger.debug("Get Sales Order Serial No Started...");
//        HttpResponse<String> response = APIClient.getRequest(EndPoints.SALESORDER_GET_LAST_SALES_ORDER_RECORD);
            apiClient = new APIClient(EndPoints.SALESORDER_GET_LAST_SALES_ORDER_RECORD, "", RequestType.GET);

            apiClient.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
                @Override
                public void handle(WorkerStateEvent workerStateEvent) {
                    jsonObject = new Gson().fromJson(workerStateEvent.getSource().getValue().toString(), JsonObject.class);
//                    String responseBody = response.body();
//                    JsonObject jsonObject = new Gson().fromJson(responseBody, JsonObject.class);
//                    System.out.println("get_last_po_invoice_record-->" + jsonObject);
                    if (jsonObject.get("responseStatus").getAsInt() == 200) {
                        String count = jsonObject.get("count").getAsString();
                        String OrderNo = jsonObject.get("serialNo").getAsString();
                        tfSalesOrderSalesSerial.setText(count);
                        tfSalesOrderOrderNo.setText(OrderNo);
                    }
                }
            });
            apiClient.setOnCancelled(new EventHandler<WorkerStateEvent>() {
                @Override
                public void handle(WorkerStateEvent workerStateEvent) {
                    logger.error("Network API cancelled in getSalesOrderSerialNo()" + workerStateEvent.getSource().getValue().toString());

                }
            });

            apiClient.setOnFailed(new EventHandler<WorkerStateEvent>() {
                @Override
                public void handle(WorkerStateEvent workerStateEvent) {
                    logger.error("Network API failed in getSalesOrderSerialNo()" + workerStateEvent.getSource().getValue().toString());
                }
            });
            apiClient.start();
            logger.debug("Get Sales Order Serial No Data End...");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            apiClient = null;
        }
    }

    //? Set the Ledger Details at the Bottom in ledger Tab
    private void tranxLedgerDetailsFun(String id) {
        APIClient apiClient = null;
        //?  activating the ledger tab
        tpSalesOrder.getSelectionModel().select(tabSalesOrderLedger);
        logger.debug("Get Ledger Details Data Started...");
        Map<String, String> map = new HashMap<>();
        map.put("ledger_id", id);
        String formData = Globals.mapToStringforFormData(map);
//        System.out.println("FormData: " + formData);
//        HttpResponse<String> response = APIClient.postFormDataRequest(formData, EndPoints.TRANSACTION_LEDGER_DETAILS);
        apiClient = new APIClient(EndPoints.TRANSACTION_LEDGER_DETAILS, formData, RequestType.FORM_DATA);

        apiClient.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent workerStateEvent) {
                jsonObject = new Gson().fromJson(workerStateEvent.getSource().getValue().toString(), JsonObject.class);

//                String responseBody = response.body();
//                JsonObject jsonObject = new Gson().fromJson(responseBody, JsonObject.class);
//                System.out.println("tranxLedgerDetailsFun Res ==>" + jsonObject);
                if (jsonObject.get("responseStatus").getAsInt() == 200) {
                    JsonObject jsonObj = jsonObject.getAsJsonObject("result");
                    //?  getting values
                    String gstNumber = jsonObj.get("gst_number").getAsString() != null ? jsonObj.get("gst_number").getAsString() : "";
                    String area = jsonObj.get("area").getAsString() != null ? jsonObj.get("area").getAsString() : "";
                    String bankName = jsonObj.get("bank_name").getAsString() != null ? jsonObj.get("bank_name").getAsString() : "";
                    String contactName = jsonObj.get("contact_name").getAsString() != null ? jsonObj.get("contact_name").getAsString() : "";
                    String creditDays = jsonObj.get("credit_days").getAsString() != null ? jsonObj.get("credit_days").getAsString() : "";
                    String fssaiNumber = jsonObj.get("fssai_number").getAsString() != null ? jsonObj.get("fssai_number").getAsString() : "";
                    String licenseNumber = jsonObj.get("license_number").getAsString() != null ? jsonObj.get("license_number").getAsString() : "";
                    String route = jsonObj.get("route").getAsString() != null ? jsonObj.get("route").getAsString() : "";
//            String transport = jsonObj.get("route").getAsString();

                    //?  setting values
                    lbSalesOrderGstNo.setText(gstNumber);
                    lbSalesOrderArea.setText(area);
                    lbSalesOrderBank.setText(bankName);
                    lbSalesOrderContactPerson.setText(contactName);
//            txtPurOrderTransportName.setText(jsonObject.get("contact_name").getAsString());
                    lbSalesOrderCreditDays.setText(creditDays);
                    lbSalesOrderFssai.setText(fssaiNumber);
                    lbSalesOrderLisenceNo.setText(licenseNumber);
                    lbSalesOrderRoute.setText(route);
                }
            }
        });
        apiClient.setOnCancelled(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent workerStateEvent) {
                logger.error("Network API cancelled in tranxLedgerDetailsFun()" + workerStateEvent.getSource().getValue().toString());

            }
        });

        apiClient.setOnFailed(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent workerStateEvent) {
                logger.error("Network API failed in tranxLedgerDetailsFun()" + workerStateEvent.getSource().getValue().toString());
            }
        });
        apiClient.start();
        logger.debug("Get Ledger Details Data End...");

    }

    //? Set the Product Details at the Bottom in Product Tab
    private void tranxProductDetailsFun(String id) {
        APIClient apiClient = null;
        //?  activating the product tab
        tpSalesOrder.getSelectionModel().select(tabSalesOrderProduct);
        logger.debug("Get Product Details Data Started...");
        Map<String, String> map = new HashMap<>();
        map.put("product_id", id);
        String formData = Globals.mapToStringforFormData(map);
        //  System.out.println("FormData: " + formData);
//        HttpResponse<String> response = APIClient.postFormDataRequest(formData, EndPoints.TRANSACTION_PRODUCT_DETAILS);
        apiClient = new APIClient(EndPoints.TRANSACTION_PRODUCT_DETAILS, formData, RequestType.FORM_DATA);

        apiClient.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent workerStateEvent) {
                jsonObject = new Gson().fromJson(workerStateEvent.getSource().getValue().toString(), JsonObject.class);
//                String responseBody = response.body();
//                JsonObject jsonObject = new Gson().fromJson(responseBody, JsonObject.class);
                //  System.out.println("tranxProductDetailsFun Res ==>" + jsonObject);
                if (jsonObject.get("responseStatus").getAsInt() == 200) {
                    JsonObject productJsonObj = jsonObject.getAsJsonObject("result");
                    //?  getting values
                    String brand = productJsonObj.get("brand").getAsString();
                    String group = productJsonObj.get("group").getAsString();
                    String subGroup = productJsonObj.get("subgroup").getAsString();
                    String category = productJsonObj.get("category").getAsString();
                    String hsn = productJsonObj.get("hsn").getAsString();
                    String taxType = productJsonObj.get("tax_type").getAsString();
                    String taxPer = productJsonObj.get("tax_per").getAsString();
                    String marginPer = productJsonObj.get("margin_per").getAsString();
                    String cost = productJsonObj.get("cost").getAsString();
                    String shelfId = productJsonObj.get("shelf_id").getAsString();
                    String minStock = productJsonObj.get("min_stocks").getAsString();
                    String maxStock = productJsonObj.get("max_stocks").getAsString();

                    //?  setting values
                    lbSalesOrderBrand.setText(brand);
                    lbSalesOrderGroup.setText(group);
                    lbSalesOrderSupplier.setText(subGroup);
                    lbSalesOrderCategory.setText(category);
                    lbSalesOrderHsn.setText(hsn);
                    lbSalesOrderTaxType.setText(taxType);
                    lbSalesOrderTax.setText(taxPer);
                    lbSalesOrderMargin.setText(marginPer);
                    lbSalesOrderCost.setText(cost);
                    lbSalesOrderShelfid.setText(shelfId);
                    lbSalesOrderMinstock.setText(minStock);
                    lbSalesOrderMaxstock.setText(maxStock);
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
        logger.debug("Get Product Details Data End...");

    }

    //? Set the SUpplier Details of the Selected Product Bottom in Supplier Table
    private void getSupplierListbyProductId(String id) {
        APIClient apiClient = null;
        logger.debug("Get Supplier List Data Started...");
        Map<String, String> map = new HashMap<>();
        map.put("productId", id);
        String formData = Globals.mapToStringforFormData(map);
//        System.out.println("FormData: " + formData);
//        HttpResponse<String> response = APIClient.postFormDataRequest(formData, EndPoints.GET_SUPPLIERLIST_BY_PRODUCTID_ENDPOINT);
        apiClient = new APIClient(EndPoints.GET_SUPPLIERLIST_BY_PRODUCTID_ENDPOINT, formData, RequestType.FORM_DATA);

        apiClient.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent workerStateEvent) {
                jsonObject = new Gson().fromJson(workerStateEvent.getSource().getValue().toString(), JsonObject.class);
//                String responseBody = response.body();
//                JsonObject jsonObject = new Gson().fromJson(responseBody, JsonObject.class);
//                System.out.println("Supplier List Res ==>" + jsonObject);
                if (jsonObject.get("responseStatus").getAsInt() == 200) {
//            JsonObject resultObj = jsonObject.getAsJsonObject("result");
                    JsonArray dataArray = jsonObject.getAsJsonArray("data");
                    //?  getting values
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
                    tcSalesOrderSupplerName.setCellValueFactory(new PropertyValueFactory<>("supplierName"));
                    tcSalesOrderSupplierInvNo.setCellValueFactory(new PropertyValueFactory<>("supplierInvNo"));
                    tcSalesOrderSupplierInvDate.setCellValueFactory(new PropertyValueFactory<>("supplierInvDate"));
                    tcSalesOrderSupplierBatch.setCellValueFactory(new PropertyValueFactory<>("supplierBatch"));
                    tcSalesOrderSupplierMrp.setCellValueFactory(new PropertyValueFactory<>("supplierMrp"));
                    tcSalesOrderSupplierQty.setCellValueFactory(new PropertyValueFactory<>("supplierQty"));
                    tcSalesOrderSupplierRate.setCellValueFactory(new PropertyValueFactory<>("supplierRate"));
                    tcSalesOrderSupplierCost.setCellValueFactory(new PropertyValueFactory<>("supplierCost"));
                    tcSalesOrderSupplierDisper.setCellValueFactory(new PropertyValueFactory<>("supplierDisPer"));
                    tcSalesOrderSupplierDisRs.setCellValueFactory(new PropertyValueFactory<>("supplierDisAmt"));

                    tvSalesOrderSupplierDetails.setItems(supplierDataList);

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

    //CREATE API OF SALES ORDER
    public void createSalesOrder() {
        AlertUtility.CustomCallback callback = number -> {
            if (number == 1) {
                LocalDate orderDate = LocalDate.parse(Communicator.text_to_date.fromString(dpSalesOrderOrderDate.getText()).toString());
                LocalDate tranxDate = LocalDate.parse(Communicator.text_to_date.fromString(dpSalesOrderTranxDate.getText()).toString());
                String id = Globals.salesOrderListDTO != null ? Globals.salesOrderListDTO.getId() : "";
                Map<String, String> map = new HashMap<>();
                map.put("id", id);
                map.put("bill_dt", String.valueOf(orderDate));
                map.put("newReference", "false");
                map.put("bill_no", tfSalesOrderOrderNo.getText());
                map.put("sales_acc_id", cmbSalesOrderSalesAc.getValue().getId());
                map.put("sales_sr_no", tfSalesOrderSalesSerial.getText());
                map.put("transaction_date", String.valueOf(tranxDate));
                map.put("debtors_id", ledgerId);
//      map.put("isRoundOffCheck", String.valueOf(chbPurChallRoundOff.isSelected()));
                if (!finalroundOff.isEmpty()) {
                    map.put("roundoff", finalroundOff);
                } else {
                    map.put("roundoff", "0");

                }
                map.put("narration", tfSalesOrderNarrations.getText());
                map.put("totalamt", lbSalesOrderBillAmount.getText());
                map.put("total_purchase_discount_amt", lbSalesOrderDiscount.getText());
                map.put("gstNo", cmbSalesOrderClientGstin.getValue() != null ? cmbSalesOrderClientGstin.getValue().getGstNo() : "");
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
//                map.put("totalcgst", finaltotalCGST);
//                map.put("totalsgst", finaltotalSGST);
//                map.put("totaligst", finaltotalIGST);
                map.put("tcs", "0");//static
                map.put("sales_discount", "0");//static
                map.put("sales_discount_amt", "0");//static
                map.put("total_sales_discount_amt", "0");//static
                map.put("additionalChargesTotal", "0");//static
                map.put("additionalCharges", "");
                map.put("total_qty", "0");//static
                map.put("total_free_qty", "0.00");//static
                map.put("total_row_gross_amt", lbSalesOrderGrossTotal.getText());
                map.put("total_base_amt", lbSalesOrderGrossTotal.getText());
                map.put("total_invoice_dis_amt", lbSalesOrderDiscount.getText());
                map.put("taxable_amount", lbSalesOrderTotal.getText());
                map.put("total_tax_amt", lbSalesOrderTotalTax.getText());
                map.put("bill_amount", lbSalesOrderBillAmount.getText());

                //!GST Tax
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
                    map.put("taxFlag", "true");//

                    map.put("taxCalculation", jsonObject.toString());
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
                    map.put("taxFlag", "false");//
                    map.put("taxCalculation", jsonObject.toString());
                }

                //new2.0 start
                List<SalesOrderTable> currentItems = new ArrayList<>(tvSalesOrderCmpTRow.getItems());

                if (!currentItems.isEmpty()) {
                    SalesOrderTable lastItem = currentItems.get(currentItems.size() - 1);

                    if (lastItem.getParticulars() != null && lastItem.getParticulars().isEmpty()) {
                        currentItems.remove(currentItems.size() - 1);
                    }
                }

                List<SalesOrderTable> list = new ArrayList<>(currentItems);
                //new2.0 end

                /***** mapping Row Data into TranxPurRowDTO *****/
                ArrayList<TranxPurRowDTO> rowData = new ArrayList<>();
                for (SalesOrderTable salesOrderTable : list) {//new2.0
                    TranxPurRowDTO purParticularRow = new TranxPurRowDTO();
                    purParticularRow.setProductId(salesOrderTable.getProduct_id());
                    if (!salesOrderTable.getLevelA_id().isEmpty()) {
                        purParticularRow.setLevelaId(salesOrderTable.getLevelA_id());
                    } else {
                        purParticularRow.setLevelaId("");
                    }
                    if (!salesOrderTable.getLevelB_id().isEmpty()) {
                        purParticularRow.setLevelbId(salesOrderTable.getLevelB_id());
                    } else {
                        purParticularRow.setLevelbId("");
                    }
                    if (!salesOrderTable.getLevelC_id().isEmpty()) {
                        purParticularRow.setLevelcId(salesOrderTable.getLevelC_id());
                    } else {
                        purParticularRow.setLevelcId("");
                    }
                    if (!salesOrderTable.getUnit().isEmpty()) {
                        purParticularRow.setUnitId(salesOrderTable.getUnit_id());
                    } else {
                        purParticularRow.setUnitId("");
                    }
                    if (salesOrderTable.getUnit_conv() != null) {
                        purParticularRow.setUnitConv(String.valueOf(salesOrderTable.getUnit_conv()));
                    } else {
                        purParticularRow.setUnitConv("0.0");
                    }
                    if (!salesOrderTable.getQuantity().isEmpty()) {
                        purParticularRow.setQty(salesOrderTable.getQuantity());
                    } else {
                        purParticularRow.setQty("");
                    }
                    if (!salesOrderTable.getFree().isEmpty()) {
                        purParticularRow.setFreeQty(salesOrderTable.getFree());
                    } else {
                        purParticularRow.setFreeQty("");
                    }
                    if (!salesOrderTable.getRate().isEmpty()) {
                        purParticularRow.setRate(salesOrderTable.getRate());
                    } else {
                        purParticularRow.setRate("");
                    }
                    if (salesOrderTable.getBase_amt() != null) {
                        purParticularRow.setBaseAmt(String.valueOf(salesOrderTable.getBase_amt()));
                    } else {
                        purParticularRow.setBaseAmt("");
                    }
                    if (!salesOrderTable.getDis_amt().isEmpty()) {
                        purParticularRow.setDisAmt(salesOrderTable.getDis_amt());
                    } else {
                        purParticularRow.setDisAmt("0.0");
                    }
                    if (!salesOrderTable.getDis_per().isEmpty()) {
                        purParticularRow.setDisPer(salesOrderTable.getDis_per());
                    } else {
                        purParticularRow.setDisPer("0.0");
                    }
                    if (!salesOrderTable.getDis_per().isEmpty()) {
                        purParticularRow.setDisPer2("0.0");
                    } else {
                        purParticularRow.setDisPer2("0.0");
                    }
                    if (salesOrderTable.getDis_per_cal() != null) {
                        purParticularRow.setDisPerCal(String.valueOf(salesOrderTable.getDis_per_cal()));
                    } else {
                        purParticularRow.setDisPerCal("");
                    }
                    if (salesOrderTable.getDis_amt_cal() != null) {
                        purParticularRow.setDisAmtCal(String.valueOf(salesOrderTable.getDis_amt_cal()));
                    } else {
                        purParticularRow.setDisAmtCal("");
                    }
                    if (salesOrderTable.getRow_dis_amt() != null) {
                        purParticularRow.setRowDisAmt(String.valueOf(salesOrderTable.getRow_dis_amt()));
                    } else {
                        purParticularRow.setRowDisAmt("");
                    }
                    if (salesOrderTable.getGross_amount() != null) {
                        purParticularRow.setGrossAmt(String.valueOf(salesOrderTable.getGross_amount()));
                    } else {
                        purParticularRow.setGrossAmt("");
                    }
                    if (salesOrderTable.getGross_amount1() != null) {
                        purParticularRow.setGrossAmt1(String.valueOf(salesOrderTable.getGross_amount1()));
                    } else {
                        purParticularRow.setGrossAmt1("0.0");
                    }
                    if (salesOrderTable.getAdd_chg_amt() != null) {
                        purParticularRow.setAddChgAmt(String.valueOf(salesOrderTable.getAdd_chg_amt()));
                    } else {
                        purParticularRow.setAddChgAmt("0.0");
                    }
                    if (salesOrderTable.getGross_amount1() != null) {
                        purParticularRow.setGrossAmt1(String.valueOf(salesOrderTable.getGross_amount1()));
                    } else {
                        purParticularRow.setGrossAmt1("0.0");
                    }
                    if (salesOrderTable.getInvoice_dis_amt() != null) {
                        purParticularRow.setInvoiceDisAmt(String.valueOf(salesOrderTable.getInvoice_dis_amt()));
                    } else {
                        purParticularRow.setInvoiceDisAmt("0.0");
                    }
                    if (salesOrderTable.getTotal_amt() != null) {
                        purParticularRow.setTotalAmt(String.valueOf(salesOrderTable.getTotal_amt()));
                    } else {
                        purParticularRow.setTotalAmt("0.0");
                    }
                    if (salesOrderTable.getGst() != null) {
                        purParticularRow.setGst(String.valueOf(salesOrderTable.getGst()));
                    } else {
                        purParticularRow.setGst("");
                    }
                    if (salesOrderTable.getCgst() != null) {
                        purParticularRow.setCgst(String.valueOf(salesOrderTable.getCgst()));
                    } else {
                        purParticularRow.setCgst("");
                    }
                    if (salesOrderTable.getIgst() != null) {
                        purParticularRow.setIgst(String.valueOf(salesOrderTable.getGst()));
                    } else {
                        purParticularRow.setIgst("");
                    }
                    if (salesOrderTable.getSgst() != null) {
                        purParticularRow.setSgst(String.valueOf(salesOrderTable.getSgst()));
                    } else {
                        purParticularRow.setSgst("");
                    }
                    if (salesOrderTable.getTotal_igst() != null) {
                        purParticularRow.setTotalIgst(String.valueOf(salesOrderTable.getTotal_igst()));
                    } else {
                        purParticularRow.setTotalIgst("");
                    }
                    if (salesOrderTable.getTotal_cgst() != null) {
                        purParticularRow.setTotalCgst(String.valueOf(salesOrderTable.getTotal_cgst()));
                    } else {
                        purParticularRow.setTotalCgst("");
                    }
                    if (salesOrderTable.getTotal_sgst() != null) {
                        purParticularRow.setTotalSgst(String.valueOf(salesOrderTable.getTotal_sgst()));
                    } else {
                        purParticularRow.setTotalSgst("");
                    }
                    if (salesOrderTable.getFinal_amount() != null) {
                        purParticularRow.setFinalAmt(String.valueOf(salesOrderTable.getFinal_amount()));
                    } else {
                        purParticularRow.setSgst("");
                    }
                    if (salesOrderTable.getSgst() != null) {
                        purParticularRow.setSgst(String.valueOf(salesOrderTable.getSgst()));
                    } else {
                        purParticularRow.setSgst("");
                    }
                    if (salesOrderTable.getDetails_id() != null) {
                        purParticularRow.setDetailsId(String.valueOf(salesOrderTable.getDetails_id()));
                    } else {
                        purParticularRow.setDetailsId("0");
                    }

//            if(salesOrderTable.getReference_id().isEmpty()) {
//                purParticularRow.setReferenceId(salesOrderTable.getReference_id());
//            }else {
//                purParticularRow.setReferenceId("");
//            }
//            if(salesOrderTable.getReference_type().isEmpty()) {
//                purParticularRow.setReferenceType(salesOrderTable.getReference_type());
//            }else {
//                purParticularRow.setReferenceType("");
//            }
                    rowData.add(purParticularRow);
                }

                String mRowData = new Gson().toJson(rowData, new TypeToken<List<TranxPurRowDTO>>() {
                }.getType());
//                System.out.println("mRowData=>" + mRowData);
                map.put("row", mRowData);
                String finalReq = Globals.mapToString(map);
//                System.out.println("FinalReq=> i am in sale order" + finalReq);

                HttpResponse<String> response;
                String formData = Globals.mapToStringforFormData(map);
//                System.out.println("formData----------------------------------------->" + formData);
                APIClient apiClient = null;
                if (Globals.salesOrderListDTO == null) {
//                    response = APIClient.postFormDataRequest(formData,EndPoints.SALES_ORDER_CREATE_ENDPOINT);
                    apiClient = new APIClient(EndPoints.SALES_ORDER_CREATE_ENDPOINT, formData, RequestType.FORM_DATA);
                    //? HIGHLIGHT
                    SalesOrderListController.isNewSalesOrderCreated = true; //? Set the flag for new creation

                } else {
//                    response = APIClient.postFormDataRequest(formData,EndPoints.SALES_ORDER_EDIT_ENDPOINT);
                    apiClient = new APIClient(EndPoints.SALES_ORDER_EDIT_ENDPOINT, formData, RequestType.FORM_DATA);
                    //? HIGHLIGHT
                    SalesOrderListController.editedSalesOrderId = id; //? Set the ID for editing
                }

                apiClient.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
                    @Override
                    public void handle(WorkerStateEvent workerStateEvent) {
//                        JsonObject responseBody = new Gson().fromJson(response.body(), JsonObject.class);
                        jsonObject = new Gson().fromJson(workerStateEvent.getSource().getValue().toString(), JsonObject.class);

//                        System.out.println("Response=>" + jsonObject);
                        message = jsonObject.get("message").getAsString();
                        int status = jsonObject.get("responseStatus").getAsInt();
                        if (jsonObject.get("responseStatus").getAsInt() == 200) {

//                            AlertUtility.CustomCallback callback1 = (number1) -> {
//
//                                if (number1 == 1) {
//                                    GlobalController.getInstance().addTabStatic(SALES_ORDER_LIST_SLUG, false);
//                                }
//                            };
//
//                            Stage stage2 = (Stage) spSalesOrderRootPane.getScene().getWindow();
                            if (status == 200) {
//                                AlertUtility.AlertSuccess(stage2, "Success", message, callback1);
                                AlertUtility.AlertSuccessTimeout(AlertUtility.alertTypeSuccess, message, input -> {
                                    GlobalController.getInstance().addTabStatic(SALES_ORDER_LIST_SLUG, false);
                                });
                            } else {
//                                AlertUtility.AlertError(stage2, AlertUtility.alertTypeError, message, callback1);
                                AlertUtility.AlertErrorTimeout(AlertUtility.alertTypeError, message, in -> {
                                    btnSalesOrderSubmit.requestFocus();
                                });
                            }
                            clearFieldsAfterSubmit();
                        }
                    }
                });

                apiClient.setOnCancelled(new EventHandler<WorkerStateEvent>() {
                    @Override
                    public void handle(WorkerStateEvent workerStateEvent) {
                        logger.error("Network API cancelled in createSalesOrder()" + workerStateEvent.getSource().getValue().toString());

                    }
                });
                apiClient.setOnFailed(new EventHandler<WorkerStateEvent>() {
                    @Override
                    public void handle(WorkerStateEvent workerStateEvent) {
                        logger.error("Network API failed in createSalesOrder()" + workerStateEvent.getSource().getValue().toString());

                    }
                });
                apiClient.start();
                logger.debug("Get Sales Order Data End...");
            } else {
                System.out.println("working!");
            }
        };
        if (Globals.salesOrderListDTO == null) {
            AlertUtility.AlertConfirmation(AlertUtility.alertTypeConfirmation, LedgerMessageConsts.msgConfirmationOnSubmit + tfSalesOrderOrderNo.getText() + LedgerMessageConsts.msgInvoiceNumber, callback);
        } else {
            AlertUtility.AlertConfirmation(AlertUtility.alertTypeConfirmation, LedgerMessageConsts.msgConfirmationOnUpdate + tfSalesOrderOrderNo.getText() + LedgerMessageConsts.msgInvoiceNumber, callback);
        }
    }

    public void clearFieldsAfterSubmit() {
        tfSalesOrderLedgerName.setText("");
        cmbSalesOrderClientGstin.getSelectionModel().clearSelection();
//            getPatient();
//            tfPatientCreateName.setText("");
//            tfPatientCreateAge.setText("");
//            tfPatientCreateWeight.setText("");
//            tfPatientCreateAddress.setText("");
//            tfPatientCreateMobileNo.setText("");
//            tfPatientCreateIdNo.setText("");
//            rbPatientCreateGenderMale.setSelected(false);
//            tfPatientCreateBloodGrp.setText("");
//            dpPatientCreateBirthDate.setValue(LocalDate.parse(""));
//            tfPatientCreatePin.setText("");
//            dpPatientCreateDignosisDate.setValue(LocalDate.parse(""));
//            dpPatientCreateTIDate.setValue(LocalDate.parse(""));


    }

    //? set the data of Frachise by Id
    public void getEditDataById() {
        APIClient apiClient = null;
        logger.debug("Get Edit by id Data Started...");
        Map<String, String> body = new HashMap<>();
        body.put("id", Globals.salesOrderListDTO.getId());
        String formData = Globals.mapToStringforFormData(body);
//        HttpResponse<String> response = APIClient.postFormDataRequest(formData, EndPoints.SALES_ORDER_GET_DATA_BY_ID_ENDPOINT);
        apiClient = new APIClient(EndPoints.SALES_ORDER_GET_DATA_BY_ID_ENDPOINT, formData, RequestType.FORM_DATA);

        apiClient.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent workerStateEvent) {
                jsonObject = new Gson().fromJson(workerStateEvent.getSource().getValue().toString(), JsonObject.class);
//                SalesOrderResponse responseBody = new Gson().fromJson(response.body(), SalesOrderResponse.class);
//                System.out.println("responseBody" + response.body());
                if (jsonObject.get("responseStatus").getAsInt() == 200) {
//                    System.out.println("GET DATA BY ID OF SALES ORDER" + jsonObject);
                    SalesOrderResponse responseBody = new Gson().fromJson(jsonObject.getAsJsonObject(), SalesOrderResponse.class);
                    setSalesOrderFormData(responseBody);
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

    private void setSalesOrderFormData(SalesOrderResponse responseBody) {
//        System.out.println("rada set " + responseBody);

        //fetchDataOfAllTransactionLedgers("SC");
//        String pi=responseBody.getInvoiceData().
        LocalDate tranxDate = LocalDate.parse(responseBody.getInvoiceData().getTransactionDt());
        LocalDate orderDate = LocalDate.parse(responseBody.getInvoiceData().getBillDate());
        dpSalesOrderTranxDate.setText(tranxDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        dpSalesOrderOrderDate.setText(orderDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        tfSalesOrderOrderNo.setText(responseBody.getInvoiceData().getSoBillNo());
        tfSalesOrderSalesSerial.setText(String.valueOf(responseBody.getInvoiceData().getSalesSrNo()));
        tfSalesOrderLedgerName.setText(String.valueOf(responseBody.getInvoiceData().getDebtorName()));
        ledgerId = String.valueOf(responseBody.getInvoiceData().getDebtorId());
        tranxLedgerDetailsFun(ledgerId);
        ledgerStateCode = responseBody.getInvoiceData().getLedgerStateCode();//new
        tfSalesOrderNarrations.setText(responseBody.getInvoiceData().getNarration());
        // Format the number to 2 decimal places
        String formattedGrossTotal = String.format("%.2f", responseBody.getInvoiceData().getTotalBaseAmount());
        lbSalesOrderGrossTotal.setText(formattedGrossTotal);
        String formattedTotal = String.format("%.2f", responseBody.getInvoiceData().getTaxableAmount());
        lbSalesOrderTotal.setText(formattedTotal);
        String formattedBillAmount = String.format("%.2f", responseBody.getInvoiceData().getTotalAmount());
        lbSalesOrderBillAmount.setText(formattedBillAmount);

        if (responseBody.getGstDetails() != null) {

//            System.out.println("GST" + responseBody.getGstDetails());

            //cmbSalesOrderClientGstin.setValue((responseBody.getInvoiceData().getGstNo()));
            ObservableList<GstDetailsDTO> gstDetailsList = FXCollections.observableArrayList();
            gstDetailsList.addAll(responseBody.getGstDetails());
            cmbSalesOrderClientGstin.setItems(gstDetailsList);

            String gstNo = responseBody.getInvoiceData().getGstNo();
            for (GstDetailsDTO gstDetailsDTO : gstDetailsList) {
                if (gstDetailsDTO.getGstNo().equalsIgnoreCase(gstNo)) {
                    cmbSalesOrderClientGstin.getSelectionModel().select(gstDetailsDTO);
                }
            }

            cmbSalesOrderClientGstin.setConverter(new StringConverter<GstDetailsDTO>() {
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

        int index = 0;
        Double totaldis = 0.0, totalTax = 0.0;

        tvSalesOrderCmpTRow.getItems().clear();
        for (SalesOrderRow mRow : responseBody.getRow()) {
            productId = String.valueOf(mRow.getProductId());
//            System.out.println("Edit Data" + productId);
//            System.out.println("mRow.getDetailsId()" + mRow.getDetailsId());
//            System.out.println("mRow.mRow.getProductName()" + mRow.getProductName());
            tranxProductDetailsFun(productId);
            getSupplierListbyProductId(productId);
            TranxSelectedProduct selectedProduct = TranxCommonPopUps.getSelectedProductFromProductId(mRow.getProductName());//new
            SalesOrderTable row = new SalesOrderTable("", "0", "1", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "");
//            System.out.println("rada row " + mRow);
//            CmpTRowDTO row = tvSalesOrderCmpTRow.getItems().get(rowIndex);
            row.setDetails_id(String.valueOf(mRow.getDetailsId()));
            row.setSelectedProduct(selectedProduct);//new
            row.setParticulars(mRow.getProductName());
            row.setProduct_id(mRow.getProductId().toString());
            row.setPackages(mRow.getPackName());
            row.setQuantity(String.valueOf(mRow.getQty().intValue()));
//            row.setBatchNo(mRow.getBatchNo());
            row.setUnit("");//new
            row.setUnit_id(mRow.getUnitId().toString());
            row.setRate(String.valueOf(mRow.getRate()));
            row.setGross_amount(String.valueOf(mRow.getGrossAmt()));
            row.setDis_per(String.valueOf(mRow.getDisPer()));
            row.setDis_amt(String.valueOf(mRow.getDisAmt()));
            row.setTax(String.valueOf(mRow.getIgst()));
            row.setNet_amount(String.valueOf(mRow.getFinalAmt()));
            row.setFree(String.valueOf(mRow.getFreeQty()));
            row.setIgst(mRow.getIgst().toString());
            row.setSgst(mRow.getSgst().toString());
            row.setCgst(mRow.getCgst().toString());
            row.setTotal_igst(mRow.getTotalIgst().toString());
            row.setTotal_sgst(mRow.getTotalSgst().toString());
            row.setTotal_cgst(mRow.getTotalCgst().toString());
//            row.setTax(String.valueOf(mRow.getGst()));
            row.setTaxable_amt(mRow.getGrossAmt().toString());
            row.setDetails_id(String.valueOf(mRow.getDetailsId()));


//            row.setGross_amount(String.valueOf(mRow.getFinalAmt()));
            row.setFinal_amount(mRow.getFinalAmt().toString());
            row.setGross_amount1(mRow.getGrossAmt1().toString());

//            salesOrderTable.setBatchNo(mRow.getBatchNo());
//            row.setTax(String.valueOf(mRow.getGst()));
            row.setLevelA_id(String.valueOf(mRow.getLevelAId()));
            row.setLevelB_id(String.valueOf(mRow.getLevelBId()));
            row.setLevelB_id(String.valueOf(mRow.getLevelCId()));
            row.setUnit_conv(mRow.getUnitConv().toString());
// salesOrderTable.setBase_amt(String.valueOf(mRow.getBaseAmt()));
//            salesOrderTable.setB_no(mRow.getBatchNo());
//            salesOrderTable.setB_rate(String.valueOf(mRow.getbRate()));
//            salesOrderTable.setRate_a(String.valueOf(mRow.getMinRateA()));
//            salesOrderTable.setRate_b(String.valueOf(mRow.getMinRateB()));
//            salesOrderTable.setRate_c(String.valueOf(mRow.getMinRateC()));
//            salesOrderTable.setCosting(String.valueOf(mRow.getCosting()));
//            salesOrderTable.setCosting_with_tax(String.valueOf(mRow.getCostingWithTax()));
//            salesOrderTable.setMin_margin(String.valueOf(mRow.getMinMargin()));
//            salesOrderTable.setB_expiry(mRow.getbExpiry());
//            salesOrderTable.setIs_batch(String.valueOf(mRow.getIsBatch()));
//            salesOrderTable.setManufacturing_date(String.valueOf(mRow.getManufacturingDate()));
//            salesOrderTable.setB_purchase_rate(String.valueOf(mRow.getPurchaseRate()));
//            salesOrderTable.setB_details_id(String.valueOf(mRow.getbDetailsId()));

            row.setFree(String.valueOf(mRow.getFreeQty()));
//            row.setSr_no("");
            row.setReference_id("");
            row.setReference_type("");
            row.setGst(Double.valueOf(mRow.getDetailsId()).toString());
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
            row.setFinal_amount(mRow.getFinalAmt().toString());
            row.setInvoice_dis_amt(mRow.getInvoiceDisAmt().toString());
            row.setDis_per_cal(mRow.getDisPerCal().toString());
            row.setDis_amt_cal(mRow.getDisAmtCal().toString());
            //new start
            List<LevelAForPurInvoice> levelAForPurInvoiceList = ProductUnitsPacking2.getAllProductUnitsPackingFlavour(row.getProduct_id());
            System.out.println("levelAForPurInvoiceList" + levelAForPurInvoiceList);

            //                int index = getTableRow().getIndex();
            ObservableList<LevelAForPurInvoice> observableLevelAList = FXCollections.observableArrayList(levelAForPurInvoiceList);
            PurInvoiceCommunicator.levelAForPurInvoiceObservableList.add(observableLevelAList);
            //new end
            // row.setRo(mRow.get());
//            salesOrderTable.setIs_batch(String.valueOf(mRow.getIsBatch()));


            totaldis = totaldis + (mRow.getRowDisAmt());
//            System.out.println("totaldis" + totaldis);
            totalTax = totalTax + (mRow.getTotalIgst());
//            System.out.println("totalTax" + totalTax);
            // Format the number to 2 decimal places
            String formattedDiscount = String.format("%.2f", totaldis);
            lbSalesOrderDiscount.setText(formattedDiscount);
            String formattedTax = String.format("%.2f", totalTax);
            lbSalesOrderTotalTax.setText(formattedTax);


            tvSalesOrderCmpTRow.getItems().add(row);

            /*if (index < 1)
                tvSalesOrderCmpTRow.getItems().set(index, row);
            else */

            index++;
//            rowCalculation(index);
// Add "+" symbol in the last column
//            TableColumn<CmpTRowDTO, Void> colActions = (TableColumn<CmpTRowDTO, Void>) tvSalesOrderCmpTRow.getColumns().get(tvSalesOrderCmpTRow.getColumns().size() - 1);
//            colActions.setCellFactory(column -> {
//                return new TableCell<>() {
//                    final Button addButton = new Button("+");
//
//                    {
//                        addButton.setStyle("-fx-font-size: 24px;-fx-background-color: transparent; -fx-border-color: transparent; -fx-text-fill: black;");
//                        addButton.setAlignment(Pos.CENTER);
//                        addButton.setMinWidth(10); // Set minimum width to maintain button visibility
//                        addButton.setMinHeight(10); // Set minimum height to maintain button visibility
//                        addButton.setPadding(new Insets(0,0,0,0)); // Add padding to increase button size
//
//                        addButton.setOnAction(event -> {
//                            // Handle action when "+" button is clicked
//                            // Implement the logic to add a new row or any other action you desire
//                            addRow(event);
//                        });
//                    }
//
//                    @Override
//                    protected void updateItem(Void item, boolean empty) {
//                        super.updateItem(item, empty);
//
//                        if (empty || productId == null) { // Hide the button if no product name is set
//                            setGraphic(null);
//                        } else {
//                            setGraphic(addButton);
//                        }
//                    }
//                };
//            });
            tcSalesOrderSrNo.setCellFactory(column -> {
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

        }
        tvSalesOrderCmpTRow.refresh();
    }

    TableCellCallback<Object[]> callback = item -> {

//        System.out.println("item: " + item[0]);
//        System.out.println("item: " + item[1]);
//        System.out.println("item: " + item[2]);
//        System.out.println("item: " + item[3]);
//        System.out.println("item: " + item[4]);
//        System.out.println("item: " + item[5]);
//        System.out.println("item: " + item[6]);
//        System.out.println("item: " + item[7]);
//        System.out.println("item: " + item[8]);
//
//        System.out.println("taxCalculation " + item[9]);

//        tvGST_Table.getItems().clear();
        gstDTOObservableList.clear();
        ObservableList<?> item9List = (ObservableList<?>) item[9];
        for (Object obj : item9List) {
            if (obj instanceof GstDTO) {
                gstDTOObservableList.add((GstDTO) obj);
            }
        }

/*        JsonArray jsonArray = new JsonArray();
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
//        purchase_invoice_map.put("taxCalculation", jsonObject.toString());
//
//        lbBillAmount.setText((String) item[2]); //totalNetAmt for bill amount
//        totalamt = (String) item[2];
//
//        total_purchase_discount_amt = (String) item[4];
//
//
//
//        if (ledgerStateCode.equalsIgnoreCase(GlobalTranx.getCompanyStateCode().toString())) {
//            taxFlag = true;
//        }
//
//        purchase_invoice_map.put("total_qty", (String) item[0]);
//        purchase_invoice_map.put("total_free_qty", (String) item[1]);
//
//        purchase_invoice_map.put("total_base_amt", (String) item[3]);
//        purchase_invoice_map.put("total_invoice_dis_amt", (String) item[4]);
//        purchase_invoice_map.put("taxable_amount", (String) item[5]);
//        purchase_invoice_map.put("bill_amount", (String) item[2]);
//        purchase_invoice_map.put("total_tax_amt", (String) item[6]);
//
//        purchase_invoice_map.put("totaligst", (String) item[6]);
//        purchase_invoice_map.put("totalsgst", (String) item[7]);
//        purchase_invoice_map.put("totalcgst", (String) item[8]);
//
//
//        purchase_invoice_map.put("taxFlag", String.valueOf(taxFlag));
//
//        total_taxable_amt = Double.parseDouble((String) item[5]);


        lbSalesOrderGrossTotal.setText((String) item[3]);
        lbSalesOrderDiscount.setText((String) item[4]);
        lbSalesOrderTotal.setText((String) item[5]);
        lbSalesOrderTotalTax.setText((String) item[6]);
        lbSalesOrderBillAmount.setText((String) item[2]);

    };
    TableCellCallback<Integer> productID_callback = item -> {
//        System.out.println("product id " + item);
        tranxProductDetailsFun(String.valueOf(item));
        getSupplierListbyProductId(String.valueOf(item));
    };
    TableCellCallback<Object[]> addPrdCalbak = item -> {
        if (item != null) {
//            System.out.println("item [0] " + item[0]);
//            System.out.println("item [0] " + item[1]);
            if ((Boolean) item[0] == true) {
                rediCurrIndex = (Integer) item[1];
                String isProduct = (String) item[2];
                if (isProduct.equalsIgnoreCase("isProduct")) {
                    isProductRed = true;
                }
                setPurChallDataToProduct();
            }
//            System.out.println("isProductRed >> " + isProductRed);
        }
    };
    //new start
    TableCellCallback<Integer> unit_callback = currentIndex -> {
        SalesOrderTable tranxRow = tvSalesOrderCmpTRow.getItems().get(currentIndex);
        //! ? Ledger State Code get selected
        if (ledgerStateCode != null) {
            if (ledgerStateCode.equalsIgnoreCase(Globals.mhStateCode)) {
                tranxRow.setRate(tranxRow.getUnitWiseRateMH() + "");
            } else {
                tranxRow.setRate(tranxRow.getUnitWiseRateAI() + "");
            }
        }
//        tvSalesOrderCmpTRow.getItems().set(currentIndex, tranxRow);
    };
    //new end

    public void tableInitiliazation() {

        tvSalesOrderCmpTRow.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tvSalesOrderCmpTRow.setEditable(true);
        tvSalesOrderCmpTRow.setFocusTraversable(false);


        Label headerLabel = new Label("Sr\nNo.");
        tcSalesOrderSrNo.setGraphic(headerLabel);

        tvSalesOrderCmpTRow.getItems().addAll(new SalesOrderTable("", "0", "1", "", "", "", "", "", "", "", "", "", "", "", "", "", "", ""));

        tcSalesOrderSrNo.setCellValueFactory(cellData -> cellData.getValue().sr_noProperty());
        tcSalesOrderSrNo.setStyle("-fx-alignment: CENTER;");

        tcSalesOrderCmpTRowPacking.setCellValueFactory(cellData -> cellData.getValue().packagesProperty());
        tcSalesOrderCmpTRowPacking.setStyle("-fx-alignment: CENTER;");

        tcSalesOrderCmpTRowA.setCellValueFactory(cellData -> cellData.getValue().levelAProperty());
        tcSalesOrderCmpTRowA.setCellFactory(column -> new ComboBoxTableCellForSalesOrderLevelA("tcSalesOrderCmpTRowA"));

        tcSalesOrderCmpTRowB.setCellValueFactory(cellData -> cellData.getValue().levelBProperty());
        tcSalesOrderCmpTRowB.setCellFactory(column -> new ComboBoxTableCellForSalesOrderLevelB("tcSalesOrderCmpTRowB"));

        tcSalesOrderCmpTRowC.setCellValueFactory(cellData -> cellData.getValue().levelCProperty());
        tcSalesOrderCmpTRowC.setCellFactory(column -> new ComboBoxTableCellForSalesOrderLevelC("tcSalesOrderCmpTRowC"));

        tcSalesOrderCmpTRowUnit.setCellValueFactory(cellData -> cellData.getValue().unitProperty());
        tcSalesOrderCmpTRowUnit.setCellFactory(column -> new ComboBoxTableCellForSalesOrderUnit("tcSalesOrderCmpTRowUnit", unit_callback));//new

        tcSalesOrderCmpTRowParticulars.setCellValueFactory(cellData -> cellData.getValue().particularsProperty());
//        tcSalesOrderCmpTRowParticulars.setCellFactory(column -> new TextFieldTableCellForSalesOrder("tcSalesOrderCmpTRowParticulars", callback, productID_callback));
        tcSalesOrderCmpTRowParticulars.setCellFactory(column -> new TextFieldTableCellForSalesOrder("tcSalesOrderCmpTRowParticulars", callback, productID_callback, tfSalesOrderNarrations, addPrdCalbak));//new2.0


        tcSalesOrderCmpTRowQuantity.setCellValueFactory(cellData -> cellData.getValue().quantityProperty());
        tcSalesOrderCmpTRowQuantity.setCellFactory(column -> new TextFieldTableCellForSalesOrder("tcSalesOrderCmpTRowQuantity", callback));

        tcSalesOrderCmpTRowFreeqty.setCellValueFactory(cellData -> cellData.getValue().freeProperty());//new2.0
        tcSalesOrderCmpTRowFreeqty.setCellFactory(column -> new TextFieldTableCellForSalesOrder("tcSalesOrderCmpTRowFreeqty", callback));//new2.0


        tcSalesOrderCmpTRowRate.setCellValueFactory(cellData -> cellData.getValue().rateProperty());
        tcSalesOrderCmpTRowRate.setCellFactory(column -> new TextFieldTableCellForSalesOrder("tcSalesOrderCmpTRowRate", callback));

        tcSalesOrderCmpTRowGrossAmount.setCellValueFactory(cellData -> cellData.getValue().gross_amountProperty());
        tcSalesOrderCmpTRowGrossAmount.setCellFactory(column -> new TextFieldTableCellForSalesOrder("tcSalesOrderCmpTRowGrossAmount", callback));

        tcSalesOrderCmpTRow1Disper.setCellValueFactory(cellData -> cellData.getValue().dis_perProperty());
        tcSalesOrderCmpTRow1Disper.setCellFactory(column -> new TextFieldTableCellForSalesOrder("tcSalesOrderCmpTRow1Disper", callback));

        tcSalesOrderCmpTRowDisRs.setCellValueFactory(cellData -> cellData.getValue().dis_amtProperty());
        tcSalesOrderCmpTRowDisRs.setCellFactory(column -> new TextFieldTableCellForSalesOrder("tcSalesOrderCmpTRowDisRs", callback));

        tcSalesOrderCmpTRowTaxPer.setCellValueFactory(cellData -> cellData.getValue().taxProperty());
        tcSalesOrderCmpTRowTaxPer.setCellFactory(column -> new TextFieldTableCellForSalesOrder("tcSalesOrderCmpTRowTaxPer", callback));

        tcSalesOrderCmpTRowNetAmount.setCellValueFactory(cellData -> cellData.getValue().net_amountProperty());
        tcSalesOrderCmpTRowNetAmount.setCellFactory(column -> new TextFieldTableCellForSalesOrder("tcSalesOrderCmpTRowNetAmount", callback));

        tcSalesOrderCmpTRowActions.setCellValueFactory(cellData -> cellData.getValue().net_amountProperty());
        tcSalesOrderCmpTRowActions.setCellFactory(column -> new DeleteButtonTableCell());

        columnVisibility(tcSalesOrderCmpTRowA, Globals.getUserControlsWithSlug("is_level_a"));
        columnVisibility(tcSalesOrderCmpTRowB, Globals.getUserControlsWithSlug("is_level_b"));
        columnVisibility(tcSalesOrderCmpTRowC, Globals.getUserControlsWithSlug("is_level_c"));
        //! Free Qty
        columnVisibility(tcSalesOrderCmpTRowFreeqty, Globals.getUserControlsWithSlug("is_free_qty"));

        //!Level A,B,C set Title as per configuration
        tcSalesOrderCmpTRowA.setText(Globals.getUserControlsNameWithSlug("is_level_a"));
        tcSalesOrderCmpTRowB.setText(Globals.getUserControlsNameWithSlug("is_level_B"));
        tcSalesOrderCmpTRowC.setText(Globals.getUserControlsNameWithSlug("is_level_C"));

    }

    private void columnVisibility(TableColumn<SalesOrderTable, String> column, boolean visible) {
        if (visible) {
           /* column.setPrefWidth(USE_COMPUTED_SIZE);
            column.setMinWidth(USE_PREF_SIZE);*/
            column.setMaxWidth(Double.MAX_VALUE);
        } else {
            column.prefWidthProperty().unbind();
            column.setPrefWidth(0.0);
            column.setMinWidth(0.0);
            column.setMaxWidth(0.0);
        }
    }

}

class ComboBoxTableCellForSalesOrderLevelA extends TableCell<SalesOrderTable, String> {

    String columnName;
    private final ComboBox<String> comboBox;
    ObservableList<LevelAForPurInvoice> comboList = null;

    public ComboBoxTableCellForSalesOrderLevelA(String columnName) {

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

                            getTableRow().getItem().setLevelA_id(levelAForPurInvoice.getValue());

                            ObservableList<LevelBForPurInvoice> observableLevelAList = FXCollections.observableArrayList(levelAForPurInvoice.getLevelBOpts());

                            int index = getTableRow().getIndex();

                            if (index >= 0 && index < PurInvoiceCommunicator.levelBForPurInvoiceObservableList.size()) {
                                PurInvoiceCommunicator.levelBForPurInvoiceObservableList.set(index, observableLevelAList);
                                getTableRow().getItem().setLevelB(null);
                                getTableRow().getItem().setLevelB(levelAForPurInvoice.getLevelBOpts().get(0).getLabel());

                            } else {
                                PurInvoiceCommunicator.levelBForPurInvoiceObservableList.add(observableLevelAList);
                                getTableRow().getItem().setLevelB(null);
                                getTableRow().getItem().setLevelB(levelAForPurInvoice.getLevelBOpts().get(0).getLabel());
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
        TableRow<SalesOrderTable> row = getTableRow();
        if (row != null) {
            SalesOrderTable item = row.getItem();
            if (item != null) {
//                item.setLevelA_id(newValue);
                item.setLevelA(newValue);
            }
        }
    }

}

class ComboBoxTableCellForSalesOrderLevelB extends TableCell<SalesOrderTable, String> {

    String columnName;
    private final ComboBox<String> comboBox;
    ObservableList<LevelBForPurInvoice> comboList = null;

    public ComboBoxTableCellForSalesOrderLevelB(String columnName) {
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
//                            System.out.println("LEVEL-C OPT"+observableLevelAList);
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

class ComboBoxTableCellForSalesOrderLevelC extends TableCell<SalesOrderTable, String> {

    String columnName;
    private final ComboBox<String> comboBox;
    ObservableList<LevelAForPurInvoice> comboList = null;

    public ComboBoxTableCellForSalesOrderLevelC(String columnName) {


        this.columnName = columnName;
        this.comboBox = new ComboBox<>();

        this.comboBox.setPromptText("Select");

      /*  comboBox.setPrefHeight(38);
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
//            System.out.println("LEVEL-C SELECTEDITEM"+selectedItem);
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

class TextFieldTableCellForSalesOrder extends TableCell<SalesOrderTable, String> {
    private TextField textField;
    private String columnName;

    TableCellCallback<Object[]> callback;
    TableCellCallback<Integer> productID_callback;
    private TextField button;//new2.0

    TableCellCallback<Object[]> addPrdCalbak;


    public TextFieldTableCellForSalesOrder(String columnName, TableCellCallback<Object[]> callback) {
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
        quantityColumn();

        freeQtyColumn();//new2.0
        rateColumn();//new2.0
        grossAmtColumn();
        DisPerColumn();
        DisAmtColumn();

        netAmountColumn();

    }

    public TextFieldTableCellForSalesOrder(String columnName, TableCellCallback<Object[]> callback, TableCellCallback<Integer> productID_callback) {
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
        quantityColumn();
        freeQtyColumn();//new2.0
        rateColumn();//new2.0
        grossAmtColumn();
        DisPerColumn();
        DisAmtColumn();

        netAmountColumn();

    }

    //new2.0 start
    public TextFieldTableCellForSalesOrder(String columnName, TableCellCallback<Object[]> callback, TableCellCallback<Integer> productID_callback, TextField button) {
        this.columnName = columnName;
        this.textField = new TextField();

        this.callback = callback;
        this.button = button;

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
        quantityColumn();
        freeQtyColumn();
        rateColumn();
        grossAmtColumn();
        DisPerColumn();
        DisAmtColumn();

        netAmountColumn();

    }

    //new2.0 End
    public TextFieldTableCellForSalesOrder(String columnName, TableCellCallback<Object[]> callback, TableCellCallback<Integer> productID_callback, TextField button, TableCellCallback<Object[]> addPrdCalbak) {
        this.columnName = columnName;
        this.textField = new TextField();

        this.callback = callback;
        this.button = button;
        this.addPrdCalbak = addPrdCalbak;

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
        quantityColumn();
        freeQtyColumn();
        rateColumn();
        grossAmtColumn();
        DisPerColumn();
        DisAmtColumn();

        netAmountColumn();

    }

    private void particularsColumn() {
        if ("tcSalesOrderCmpTRowParticulars".equals(columnName)) {
            textField.setEditable(false);
           /* textField.setOnKeyPressed(event -> {
                if (event.getCode() == KeyCode.SPACE) {
                    openProduct(getIndex());//new
                }
            });*/
            //new2.0 start
            textField.setOnKeyPressed(event -> {
                if (event.getCode() == KeyCode.SPACE) {
                    openProduct(getIndex());
                }
                if (event.isShiftDown() && event.getCode() == KeyCode.TAB) {
                    if (getIndex() == 1) {
                        TableColumn<SalesOrderTable, ?> colName = getTableView().getColumns().get(12);
                        getTableView().edit(getIndex() - 1, colName);
                    }
//                    else if(getIndex()==2){
//                        TableColumn<SalesOrderTable, ?> colName = getTableView().getColumns().get(12);
//                        getTableView().edit(getIndex() - 1, colName);
//                    }
                    else {
                        TableColumn<SalesOrderTable, ?> colName = getTableView().getColumns().get(15);
                        getTableView().edit(getIndex() - 1, colName);
                    }
                } else if (event.getCode() == KeyCode.ENTER || !event.isShiftDown() && event.getCode() == KeyCode.TAB) {
                    if (textField.getText().isEmpty() && getIndex() == getTableView().getItems().size() - 1 && getIndex() != 0) {
                        Platform.runLater(() -> {
                            button.requestFocus();
                        });
                    } else if (!textField.getText().isEmpty()) {
                        TableColumn<SalesOrderTable, ?> colName = getTableView().getColumns().get(6);
                        getTableView().edit(getIndex(), colName);
                    } else {
                        textField.requestFocus();
                    }
                    event.consume();
                }
                //new2.0 end
            });

            textField.setOnMouseClicked(event -> {
                openProduct(getIndex());//new
            });
        }
    }

    private void quantityColumn() {
        if ("tcSalesOrderCmpTRowQuantity".equals(columnName)) {
            textField.setEditable(true);
//new2.0 start
            textField.setOnKeyPressed(event -> {
                if(!textField.getText().isEmpty()){
                    CommonValidationsUtils.restrictToNumbers(textField);
                }
                if (event.isShiftDown() && event.getCode() == KeyCode.TAB) {
                    TableColumn<SalesOrderTable, ?> colName = getTableView().getColumns().get(6);
                    getTableView().edit(getIndex(), colName);

                } else if (event.getCode() == KeyCode.ENTER || event.getCode() == KeyCode.TAB) {
                    Integer index = getIndex();
                    if (!textField.getText().isEmpty()) {
                        int cnt = Globals.getUserControlsWithSlug("is_free_qty") == true ? 8 : 9;
                        TableColumn<SalesOrderTable, ?> colName = getTableView().getColumns().get(cnt);
                        getTableView().edit(index, colName);
                    } else {
                        textField.requestFocus();
                    }
                    SalesOrderCalculation.rowCalculationForPurcInvoice(index, getTableView(), callback);
                } else if (event.getCode() == KeyCode.DOWN) {
                    getTableView().getItems().addAll(new SalesOrderTable("", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", ""));
                    TableColumn<SalesOrderTable, ?> colName = getTableView().getColumns().get(1);
                    getTableView().edit(getIndex() + 1, colName);
                }
                //new2.0 end
            });


//            textField.setOnMouseClicked(event -> {
//                Integer index = getIndex();
//                System.out.println("Rada INDEX"+index);
//                SalesOrderCalculation.rowCalculationForPurcInvoice(index, getTableView(), callback);
//            });
        }
    }

    //new2.0 start
    private void freeQtyColumn() {
        if ("tcSalesOrderCmpTRowFreeqty".equals(columnName)) {
            textField.setEditable(true);
            textField.setOnKeyPressed(event -> {
                if(!textField.getText().isEmpty()){
                    CommonValidationsUtils.restrictToNumbers(textField);
                }
                if (event.isShiftDown() && event.getCode() == KeyCode.TAB) {
                    TableColumn<SalesOrderTable, ?> colName = getTableView().getColumns().get(7);
                    getTableView().edit(getIndex(), colName);

                } else if (event.getCode() == KeyCode.ENTER || event.getCode() == KeyCode.TAB) {
                    int index = getIndex();
                    TableColumn<SalesOrderTable, ?> colName = getTableView().getColumns().get(9);
                    getTableView().edit(getIndex(), colName);
                    SalesOrderCalculation.rowCalculationForPurcInvoice(index, getTableView(), callback);
                }
            });
           /* TableColumn<PurchaseOrderTable, ?> colName = getTableView().getColumns().get(8);
            getTableView().edit(getIndex(), colName);*/

        }
    }

    private void rateColumn() {
        if ("tcSalesOrderCmpTRowRate".equals(columnName)) {
            textField.setEditable(true);
            textField.setOnKeyPressed(event -> {
                if (!textField.getText().isEmpty()) {
                    CommonValidationsUtils.restrictToDecimalNumbers(textField);
                }
                if (event.isShiftDown() && event.getCode() == KeyCode.TAB) {
                    int cnt = Globals.getUserControlsWithSlug("is_free_qty") == true ? 8 : 7;
                    TableColumn<SalesOrderTable, ?> colName = getTableView().getColumns().get(cnt);
                    getTableView().edit(getIndex(), colName);
                } else if (event.getCode() == KeyCode.ENTER || event.getCode() == KeyCode.TAB) {
                    int index = getIndex();
                    if (!textField.getText().isEmpty()) {
                        TableColumn<SalesOrderTable, ?> colName = getTableView().getColumns().get(11);
                        getTableView().edit(index, colName);
                    } else {
                        textField.requestFocus();
                    }
                    SalesOrderCalculation.rowCalculationForPurcInvoice(index, getTableView(), callback);
                } else if (event.getCode() == KeyCode.DOWN) {
                    getTableView().getItems().addAll(new SalesOrderTable("", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", ""));
                    TableColumn<SalesOrderTable, ?> colName = getTableView().getColumns().get(1);
                    getTableView().edit(getIndex() + 1, colName);
                }
            });

        }
    }
    //new2.0 end

    private void grossAmtColumn() {
        if ("tcSalesOrderCmpTRowGrossAmount".equals(columnName)) {
            textField.setEditable(false);
            textField.setFocusTraversable(false);
            textField.setOnKeyPressed(event -> {

                if (event.isShiftDown() && event.getCode() == KeyCode.TAB) {
                    TableColumn<SalesOrderTable, ?> colName = getTableView().getColumns().get(7);
                    getTableView().edit(getIndex() - 1, colName);
                } else if (event.getCode() == KeyCode.ENTER || event.getCode() == KeyCode.TAB) {
                    int index = getIndex();
                    if (!textField.getText().isEmpty()) {
                        TableColumn<SalesOrderTable, ?> colName = getTableView().getColumns().get(11);
                        getTableView().edit(index, colName);
                    } else {
                        textField.requestFocus();
                    }
                    SalesOrderCalculation.rowCalculationForPurcInvoice(index, getTableView(), callback);
                } else if (event.getCode() == KeyCode.DOWN) {
                    getTableView().getItems().addAll(new SalesOrderTable("", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", ""));
                    TableColumn<SalesOrderTable, ?> colName = getTableView().getColumns().get(1);
                    getTableView().edit(getIndex() + 1, colName);
                }
            });

        }
    }

    //new2.0 end
    private void DisPerColumn() {
        if ("tcSalesOrderCmpTRow1Disper".equals(columnName)) {
            textField.setEditable(true);
//new2.0 start
            textField.setOnKeyPressed(event -> {
                if (!textField.getText().isEmpty()) {
                    CommonValidationsUtils.restrictToDecimalNumbers(textField);
                }
                if (event.isShiftDown() && event.getCode() == KeyCode.TAB) {
                    TableColumn<SalesOrderTable, ?> colName = getTableView().getColumns().get(9);
                    getTableView().edit(getIndex(), colName);
                } else if (event.getCode() == KeyCode.ENTER || event.getCode() == KeyCode.TAB) {
                    int index = getIndex();
                    TableColumn<SalesOrderTable, ?> colName = getTableView().getColumns().get(12);
                    getTableView().edit(index, colName);
                    SalesOrderCalculation.rowCalculationForPurcInvoice(index, getTableView(), callback);
//new2.0 end
                }
            });


//            textField.setOnMouseClicked(event -> {
//                Integer index = getIndex();
//                System.out.println("Rada INDEX"+index);
//                SalesOrderCalculation.rowCalculationForPurcInvoice(index, getTableView(), callback);
//            });
        }
    }

    private void DisAmtColumn() {
        if ("tcSalesOrderCmpTRowDisRs".equals(columnName)) {
            textField.setEditable(true);
//new2.0 start
            textField.setOnKeyPressed(event -> {
                if (!textField.getText().isEmpty()) {
                    CommonValidationsUtils.restrictToDecimalNumbers(textField);
                }
                if (event.isShiftDown() && event.getCode() == KeyCode.TAB) {
                    TableColumn<SalesOrderTable, ?> colName = getTableView().getColumns().get(11);
                    getTableView().edit(getIndex(), colName);
                } else if (event.getCode() == KeyCode.ENTER || event.getCode() == KeyCode.TAB) {
                    int index = getIndex();
                    int current_index = getTableRow().getIndex();
                    //new start
                    if (getTableRow().getItem().getSr_no() != null) {
                        int serial_no = Integer.parseInt(getTableRow().getItem().getSr_no());
                        getTableView().getItems().addAll(new SalesOrderTable("", "", String.valueOf(serial_no + 1), "", "", "", "", "", "", "", "", "", "", "", "", "", "", ""));
                    }
                    TableColumn<SalesOrderTable, ?> colName = getTableView().getColumns().get(1);
                    getTableView().edit(getIndex() + 1, colName);
                    SalesOrderCalculation.rowCalculationForPurcInvoice(index, getTableView(), callback);
//new2.0 end

                }
            });


//            textField.setOnMouseClicked(event -> {
//                Integer index = getIndex();
//                System.out.println("Rada INDEX"+index);
//                SalesOrderCalculation.rowCalculationForPurcInvoice(index, getTableView(), callback);
//            });
        }
    }

    private void netAmountColumn() {

        if ("tcSalesOrderCmpTRowNetAmount".equals(columnName)) {
            textField.setEditable(false);
            textField.setOnKeyPressed(event -> {
                if (event.getCode() == KeyCode.SPACE) {
                    //                    System.out.println(getTableRow().getItem().getNet_amount());
                    //

                    int current_index = getTableRow().getIndex();
                    //new start
                    if (getTableRow().getItem().getSr_no() != null) {
                        int serial_no = Integer.parseInt(getTableRow().getItem().getSr_no());
                        getTableView().getItems().addAll(new SalesOrderTable("", String.valueOf(current_index + 1), String.valueOf(serial_no + 1), "", "", "", "", "", "", "", "", "", "", "", "", "", "", ""));
                    }
                    //new end
                }
            });
        }
    }

    //new start
    private void openProduct(int currentIndex) {
        SalesOrderTable selectedRow = getTableView().getItems().get(currentIndex);

        TranxCommonPopUps.openProductPopUp(Communicator.stage, Integer.valueOf(selectedRow.getProduct_id()), "Product", input -> {
//            Integer selectedIndex = tblTranxSalesInvoiceCreateCMPTRow.getSelectionModel().getSelectedIndex();
//            System.out.println("input);
            if (input != null) {
                getTableRow().getItem().setParticulars(input.getProductName());
                getTableRow().getItem().setProduct_id(input.getProductId().toString());
                getTableRow().getItem().setPackages(input.getPackageName());
                getTableRow().getItem().setIs_batch(input.getBatch().toString());
                getTableRow().getItem().setTax(input.getIgst().toString());
                getTableRow().getItem().setIgst(input.getIgst().toString());
                getTableRow().getItem().setCgst(input.getCgst().toString());
                getTableRow().getItem().setSgst(input.getSgst().toString());
//                getTableRow().getItem().setUnit(input.get());
                getTableRow().getItem().setSelectedProduct(input);
                getTableRow().getItem().setRate(input.getSalesRate().toString());
                List<LevelAForPurInvoice> levelAForPurInvoiceList = ProductUnitsPacking2.getAllProductUnitsPackingFlavour(input.getProductId().toString());

                //                int index = getTableRow().getIndex();
                ObservableList<LevelAForPurInvoice> observableLevelAList = FXCollections.observableArrayList(levelAForPurInvoiceList);

                if (currentIndex >= 0 && currentIndex < PurInvoiceCommunicator.levelAForPurInvoiceObservableList.size()) {
                    PurInvoiceCommunicator.levelAForPurInvoiceObservableList.set(currentIndex, observableLevelAList);
                    getTableRow().getItem().setLevelA(null);
                    getTableRow().getItem().setLevelA(levelAForPurInvoiceList.get(0).getLabel());

                } else {
                    PurInvoiceCommunicator.levelAForPurInvoiceObservableList.add(observableLevelAList);
                    getTableRow().getItem().setLevelA(null);
                    getTableRow().getItem().setLevelA(levelAForPurInvoiceList.get(0).getLabel());
                }
                Integer product_id = Integer.valueOf(getTableRow().getItem().getProduct_id());
                if (productID_callback != null) {
                    productID_callback.call(product_id);
                }
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
//            if (productID_callback != null) {
//                productID_callback.call(productId);
//            }
//
//            getTableRow().getItem().setParticulars(productName);
//            getTableRow().getItem().setProduct_id(productId);
//            getTableRow().getItem().setPackages(packaging);
//            getTableRow().getItem().setIs_batch(is_batch);
//            getTableRow().getItem().setTax(taxper);
//            getTableRow().getItem().setUnit(unit);
//            getTableRow().getItem().setRate(mrp);
//
//            List<LevelAForPurInvoice> levelAForPurInvoiceList = ProductUnitsPacking2.getAllProductUnitsPackingFlavour(productId);
//            int index = getTableRow().getIndex();
//            ObservableList<LevelAForPurInvoice> observableLevelAList = FXCollections.observableArrayList(levelAForPurInvoiceList);
//
//            if (index >= 0 && index < PurInvoiceCommunicator.levelAForPurInvoiceObservableList.size()) {
//                PurInvoiceCommunicator.levelAForPurInvoiceObservableList.set(index, observableLevelAList);
//                getTableRow().getItem().setLevelA(null);
//                getTableRow().getItem().setLevelA(levelAForPurInvoiceList.get(0).getLabel());
//
//            } else {
//                PurInvoiceCommunicator.levelAForPurInvoiceObservableList.add(observableLevelAList);
//                getTableRow().getItem().setLevelA(null);
//                getTableRow().getItem().setLevelA(levelAForPurInvoiceList.get(0).getLabel());
//            }
//
//
//        });
//    }

    public void textfieldStyle() {
        if (columnName.equals("tcSalesOrderCmpTRowParticulars")) {
            textField.setPromptText("Particulars");
            textField.setEditable(false);
        } else {
            textField.setPromptText("0");
        }

//        textField.setPrefHeight(38);
//        textField.setMaxHeight(38);
//        textField.setMinHeight(38);

        if (columnName.equals("tcSalesOrderCmpTRowParticulars")) {
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


        if (columnName.equals("tcSalesOrderCmpTRowNetAmount")) {
            this.textField.setOnKeyPressed(event -> {
                if (event.getCode() == KeyCode.ENTER) {
                    commitEdit(textField.getText());
                    getTableView().getItems().addAll(new SalesOrderTable("", "", "1", "", "", "", "", "", "", "", "", "", "", "", "", "", "", ""));
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
        if (item != null && columnName.equals("tcSalesOrderCmpTRowParticulars")) {
            ((SalesOrderTable) item).setParticulars(newValue.isEmpty() ? "" : newValue);
        } else if (columnName.equals("tcBatch")) {
            (getTableRow().getItem()).setBatch_or_serial(newValue.isEmpty() ? "" : newValue);
        } else if (columnName.equals("tcSalesOrderCmpTRowQuantity")) {
            (getTableRow().getItem()).setQuantity(newValue.isEmpty() ? "" : newValue);//new2.0
        } else if (columnName.equals("tcSalesOrderCmpTRowFreeqty")) {//new2.0
            (getTableRow().getItem()).setFree(newValue.isEmpty() ? "0.0" : newValue);//new2.0
        } else if (columnName.equals("tcSalesOrderCmpTRowRate")) {
            (getTableRow().getItem()).setRate(newValue.isEmpty() ? "" : newValue);
        } else if (columnName.equals("tcSalesOrderCmpTRowGrossAmount")) {
            (getTableRow().getItem()).setGross_amount(newValue.isEmpty() ? "" : newValue);
        } else if (columnName.equals("tcSalesOrderCmpTRow1Disper")) {
            (getTableRow().getItem()).setDis_per(newValue.isEmpty() ? "" : newValue);
        } else if (columnName.equals("tcSalesOrderCmpTRowDisRs")) {
            (getTableRow().getItem()).setDis_amt(newValue.isEmpty() ? "" : newValue);
        } else if (columnName.equals("tcSalesOrderCmpTRowTaxPer")) {
            (getTableRow().getItem()).setTax(newValue.isEmpty() ? "" : newValue);
        } else if (columnName.equals("tcSalesOrderCmpTRowNetAmount")) {
            (getTableRow().getItem()).setNet_amount(newValue.isEmpty() ? "" : newValue);
        }

    }
}

class ComboBoxTableCellForSalesOrderUnit extends TableCell<SalesOrderTable, String> {

    String columnName;
    TableCellCallback<Integer> unit_callback;//new
    private final ComboBox<String> comboBox;

    public ComboBoxTableCellForSalesOrderUnit(String columnName, TableCellCallback<Integer> unit_callback) {//new


        this.columnName = columnName;
        this.comboBox = new ComboBox<>();
        this.unit_callback = unit_callback;//new

        this.comboBox.setPromptText("Select");

       /* comboBox.setPrefHeight(38);
        comboBox.setMaxHeight(38);
        comboBox.setMinHeight(38);*/

        this.comboBox.setStyle("-fx-background-color: #f6f6f9; -fx-border-radius: 0px; -fx-border-width: 1; -fx-border-color: #f6f6f9;");

        this.comboBox.focusedProperty().addListener((obs, wasFocused, isNowFocused) -> {
            if (isNowFocused) {
                comboBox.show();
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
        this.comboBox.setFocusTraversable(true);//new
//        AutoCompleteBox autoCompleteBox1 = new AutoCompleteBox<>(this.comboBox, -1);//new2.0

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
//                            getTableRow().getItem().setUnit(unitForPurInvoice.getUnitName());
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
                TableColumn<SalesOrderTable, ?> colName = getTableView().getColumns().get(1);
                getTableView().edit(getIndex(), colName);
            }

            if (event.getCode() == KeyCode.ENTER || !event.isShiftDown() && event.getCode() == KeyCode.TAB) {
                TableColumn<SalesOrderTable, ?> colName = getTableView().getColumns().get(7);
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
            /*else {
                int i = getTableRow().getIndex();
                if (i >= 0) {
                    comboBox.getItems().clear();
                }
            }
*/
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

class DeleteButtonTableCell extends TableCell<SalesOrderTable, String> {
    private Button delete;

    public DeleteButtonTableCell() {


        this.delete = createButtonWithImage();


        delete.setOnAction(actionEvent -> {
            SalesOrderTable table = getTableView().getItems().get(getIndex());
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

class SalesOrderCalculation {

    public static void rowCalculationForPurcInvoice(int rowIndex, TableView<SalesOrderTable> tableView, TableCellCallback<Object[]> callback) {
        SalesOrderTable purchaseInvoiceTable = tableView.getItems().get(rowIndex);
        if (!purchaseInvoiceTable.getQuantity().isEmpty() && Double.parseDouble(purchaseInvoiceTable.getQuantity()) > 0 && !purchaseInvoiceTable.getRate().isEmpty() && Double.parseDouble(purchaseInvoiceTable.getRate()) > 0) {
            String purchaseInvoiceTableData = purchaseInvoiceTable.getRate();
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
            SalesOrderTable selectedItem = tableView.getItems().get(rowIndex);
            selectedItem.setGross_amount(String.valueOf(base_amt));
            selectedItem.setNet_amount(String.valueOf(net_amt));
            selectedItem.setOrg_net_amt(String.valueOf(net_amt));
//        selectedItem.setOrg_taxable_amt(taxable_amt);
            selectedItem.setTaxable_amt(String.valueOf(taxable_amt));
            selectedItem.setTotal_taxable_amt(String.valueOf(total_taxable_amt));
/*            selectedItem.setIgst(String.valueOf(totalTax)); // for Tax Amount
            selectedItem.setSgst(String.valueOf(totalTax / 2)); // for Tax Amount
            selectedItem.setCgst(String.valueOf(totalTax / 2)); // for Tax  Amount*/
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
            // tax percentage store cmptrow
            selectedItem.setGst(String.valueOf(r_tax_per));
/*            selectedItem.setIgst_per(String.valueOf(r_tax_per));
            selectedItem.setSgst_per(String.valueOf(r_tax_per / 2));
            selectedItem.setCgst_per(String.valueOf(r_tax_per / 2));*/
            selectedItem.setTotal_igst(GlobalTranx.TranxRoundDigit(GlobalTranx.TranxCalculatePer(total_amt,r_tax_per),GlobalTranx.configDecimalPlace)+"");
            selectedItem.setTotal_sgst(GlobalTranx.TranxRoundDigit(GlobalTranx.TranxCalculatePer(total_amt,Double.valueOf(selectedItem.getCgst())),GlobalTranx.configDecimalPlace)+"");
            selectedItem.setTotal_cgst(GlobalTranx.TranxRoundDigit(GlobalTranx.TranxCalculatePer(total_amt,Double.valueOf(selectedItem.getSgst())),GlobalTranx.configDecimalPlace)+"");
            selectedItem.setFinal_amount(String.valueOf(net_amt));
            selectedItem.setDis_per2(String.valueOf(disPer2));

            calculateGst(tableView, callback);
        }

    }

    public static void calculateGst(TableView<SalesOrderTable> tableView, TableCellCallback<Object[]> callback) {

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
        for (SalesOrderTable salesOrderTable : tableView.getItems()) {

            if (!salesOrderTable.getQuantity().isEmpty() && Double.parseDouble(salesOrderTable.getQuantity()) > 0 && !salesOrderTable.getRate().isEmpty() && Double.parseDouble(salesOrderTable.getRate()) > 0) {
                taxPercentage = Double.parseDouble(salesOrderTable.getTax());
                Double quantity = Double.parseDouble(salesOrderTable.getQuantity());
                if (salesOrderTable.getFree().isEmpty()) {
                    freeQuantity = 0.0;
                } else {
                    freeQuantity = Double.parseDouble(salesOrderTable.getFree());
                }

                // Total Calculations of each IGST, SGST, CGST
                totalQuantity += quantity;
                totalFreeQuantity += freeQuantity;
                Double igst = Double.parseDouble(salesOrderTable.getIgst());
                Double cgst = Double.parseDouble(salesOrderTable.getCgst());
                Double sgst = Double.parseDouble(salesOrderTable.getSgst());
                totalFinalIgst += igst;
                totalFinalSgst += sgst;
                totalFinalCgst += cgst;

                if (cgstTotals.containsKey(taxPercentage)) {
                    // If the key exists, update the value
                    cgstTotals.put(taxPercentage, cgstTotals.get(taxPercentage) + cgst);
                } else {
                    // If the key doesn't exist, add it with the initial value of cgst
                    cgstTotals.put(taxPercentage, cgst);
                }
                if (sgstTotals.containsKey(taxPercentage)) {
                    // If the key exists, update the value
                    sgstTotals.put(taxPercentage, sgstTotals.get(taxPercentage) + sgst);
                } else {
                    // If the key doesn't exist, add it with the initial value of sgst
                    sgstTotals.put(taxPercentage, sgst);
                }

                //Total Calculation of gross amt ,taxable ,tax,discount
                netAmount = Double.parseDouble(salesOrderTable.getNet_amount());
                totalNetAmt += netAmount;
                grossAmt = Double.parseDouble(salesOrderTable.getGross_amount());
                totalGrossAmt += grossAmt;
                taxableAmt = Double.parseDouble(salesOrderTable.getTaxable_amt());
                totaltaxableAmt += taxableAmt;
                disAmt = salesOrderTable.getFinal_dis_amt().isEmpty() ? 0.0 : Double.valueOf(salesOrderTable.getFinal_dis_amt());
                totalDisAmt += disAmt;
                taxAmt = salesOrderTable.getFinal_tax_amt().isEmpty() ? 0.0 : Double.valueOf(salesOrderTable.getFinal_tax_amt());
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
            object[5] = String.format("%.2f", (totalGrossAmt - totalDisAmt + 0.0));//  total taxable

            object[6] = String.format("%.2f", totalTaxAmt);

            object[7] = String.format("%.2f", totalFinalSgst);

            object[8] = String.format("%.2f", totalFinalCgst);

            object[9] = observableList;


            if (callback != null) {
                callback.call(object);
            }
        }
    }


    public static void discountPropotionalCalculation(String disproportionalPer, String disproportionalAmt, String additionalCharges, TableView<SalesOrderTable> tableView, TableCellCallback<Object[]> callback) {

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
            rowCalculationForPurcInvoice(i, tableView, callback);//call row calculation function
            SalesOrderTable salesOrderTable = tableView.getItems().get(i);
            totalTaxableAmt = totalTaxableAmt + Double.parseDouble(salesOrderTable.getTaxable_amt());

        }

        Double rowDisc = 0.0;
        //get data of netamount, tax per,and row taxable amount from cmptrow
        for (SalesOrderTable salesOrderTable : tableView.getItems()) {
            rowDisc = Double.parseDouble(salesOrderTable.getFinal_dis_amt());
            netAmt = Double.parseDouble(salesOrderTable.getNet_amount());
            r_tax_per = Double.parseDouble(salesOrderTable.getTax());
            rowTaxableAmt = Double.parseDouble(salesOrderTable.getTaxable_amt());

            //calculate discount proportional percentage and  the taxable amount
            if (dis_proportional_per > 0.0) {
                totalDisPropAmt = (dis_proportional_per / 100) * (totalTaxableAmt);
                rowDisPropAmt = (rowTaxableAmt * totalDisPropAmt) / totalTaxableAmt;
                rowTaxableAmt = rowTaxableAmt - rowDisPropAmt;
                rowDisc += rowDisPropAmt;
                totalTaxableAmtAdditional = rowTaxableAmt;
            } else {
                totalTaxableAmtAdditional = rowTaxableAmt;
            }



//
//            if (dis_proportional_amt > 0.0) {
//                totalDisPropAmt = dis_proportional_amt;
//                rowDisPropAmt = (rowTaxableAmt * totalDisPropAmt) / totalTaxableAmt;
//                rowDisc += rowDisPropAmt;
//                rowTaxableAmt = rowTaxableAmt - rowDisPropAmt;
//                totalTaxableAmtAdditional = rowTaxableAmt;
//            } else {
//                totalTaxableAmtAdditional = rowTaxableAmt;
//
//            }


            //calculate tax per and store the data to final net AMount
            if (additionalCharges.isEmpty()) {
                if (r_tax_per > 0) {
                    total_tax_amt = (r_tax_per / 100) * (rowTaxableAmt);
                    netAmt = rowTaxableAmt + total_tax_amt;
//
                }
            }

            //Set data to cmpTRow
            salesOrderTable.setNet_amount(String.format("%.2f", netAmt));
            salesOrderTable.setTotal_taxable_amt(String.valueOf(totalTaxableAmtAdditional));
            salesOrderTable.setIgst(String.valueOf(total_tax_amt));
            salesOrderTable.setCgst(String.valueOf(total_tax_amt / 2));
            salesOrderTable.setSgst(String.valueOf(total_tax_amt / 2));
            salesOrderTable.setFinal_tax_amt(String.valueOf(total_tax_amt));
            salesOrderTable.setFinal_dis_amt(String.valueOf(rowDisc));
            salesOrderTable.setInvoice_dis_amt(String.valueOf(rowDisPropAmt));

        }
        //Check additional charge is Empty or not if not empty call to the additional charges function
        if (!additionalCharges.isEmpty()) {
            //additionalChargesCalculation();
        } /*else {
            additionalChargesCalculation();
        }*/
        calculateGst(tableView, callback);
    }


    public static void additionalChargesCalculation(String additionalCharges, TableView<SalesOrderTable> tableView, TableCellCallback<Object[]> callback) {


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
                SalesOrderTable salesOrderTable = tableView.getItems().get(i);
                totalTaxableAmt = totalTaxableAmt + Double.parseDouble(salesOrderTable.getTotal_taxable_amt());

            }
            for (SalesOrderTable salesOrderTable : tableView.getItems()) {

                netAmt = Double.parseDouble(salesOrderTable.getNet_amount());
                r_tax_per = Double.parseDouble(salesOrderTable.getTax());
                rowTaxableAmt = Double.parseDouble(salesOrderTable.getTotal_taxable_amt());


                totalDisPropAmt = additionalChargeAmt;
                rowDisPropAmt = (rowTaxableAmt * totalDisPropAmt) / totalTaxableAmt;
                rowTaxableAmt = rowTaxableAmt + rowDisPropAmt;

                if (r_tax_per > 0) {
                    total_tax_amt = (r_tax_per / 100) * (rowTaxableAmt);
                    netAmt = rowTaxableAmt + total_tax_amt;
                }

                salesOrderTable.setNet_amount(String.format("%.3f", netAmt));
                salesOrderTable.setCgst(String.valueOf(total_tax_amt / 2));
                salesOrderTable.setSgst(String.valueOf(total_tax_amt / 2));
                salesOrderTable.setFinal_tax_amt(String.valueOf(total_tax_amt));


            }

        }
        //call calculate gst function
        calculateGst(tableView, callback);

    }


}

class ProductUnitsPacking2 {

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
            System.out.println("Output=>" + "Error inside");
            e.printStackTrace();
        }

        return levelAForPurInvoicesList;
    }

}