package com.opethic.genivis.controller.tranx_sales;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.opethic.genivis.GenivisApplication;
import com.opethic.genivis.controller.dialogs.AdditionalCharges;
import com.opethic.genivis.controller.dialogs.SingleInputDialogs;
import com.opethic.genivis.controller.master.ledger.common.LedgerMessageConsts;
import com.opethic.genivis.controller.tranx_sales.common.LineNumbersCellFactory;
import com.opethic.genivis.controller.tranx_sales.common.TranxCommonPopUps;
import com.opethic.genivis.dto.*;
import com.opethic.genivis.dto.add_charges.CustomAddChargesDTO;
import com.opethic.genivis.dto.cmp_t_row.BatchWindowTableDTO;
import com.opethic.genivis.dto.pur_invoice.*;
import com.opethic.genivis.dto.reqres.product.Communicator;
import com.opethic.genivis.dto.reqres.product.TableCellCallback;
import com.opethic.genivis.dto.reqres.pur_tranx.PurTranxToProductRedirectionDTO;
import com.opethic.genivis.dto.reqres.pur_tranx.TranxPurRowDTO;
import com.opethic.genivis.dto.reqres.sales_tranx.SaleOrderToChallanResponse;
import com.opethic.genivis.dto.reqres.sales_tranx.SaleOrderToChallanRow;
import com.opethic.genivis.dto.tranx_sales.CmpTRowDTOSoToSc;
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
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import org.apache.http.util.TextUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;
import java.net.http.HttpResponse;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static com.opethic.genivis.utils.FxmFileConstants.SALES_CHALLAN_LIST_SLUG;
import static com.opethic.genivis.utils.FxmFileConstants.SALES_ORDER_LIST_SLUG;
import static com.opethic.genivis.utils.Globals.*;
import static javafx.scene.control.PopupControl.USE_COMPUTED_SIZE;
import static javafx.scene.control.PopupControl.USE_PREF_SIZE;

public class SaleOrderToChallanController implements Initializable {
    @FXML
    private BorderPane rootAnchorPaneSoToSc;
    private static final Logger soToscLogger = LoggerFactory.getLogger(SaleOrderToChallanController.class);
    public static String scConversionId;
    public static ArrayList<String> arrayList;
    public static String input = "";    //here we get the selected list id also  we can get multiple id
    @FXML
    private TextField dpSCConvTranxDate, tfSCConvLedgNameEdit, tfSCConvChallanSerial, tfSCConvInvNo, tfSCConvEditNarrations;
    @FXML
    private ComboBox<SalesmanMasterDTO> dpSCConvSalesman;

    @FXML
    private HBox so2ctopInnerHbOne, so2ctopInnerHbTwo, so2cBottomMain;
    @FXML
    private VBox so2cBottomFirstV, so2cBottomSecondV, so2cBottomThirdV, so2cTotalMainDiv, so2cTotalMainInnerDiv, so2cTotalInnerFirstDiv, so2cTotalInnerSecondDiv;

    @FXML
    private ComboBox cmbSCConvSupplierGST;
    @FXML
    private ComboBox<CommonDTO> cmbSCConvSalesAC;
    @FXML
    private TableView<CmpTRowDTOSoToSc> tvSCConvEditCMPTRow;
    @FXML
    private TableView tvSCConvSupplierDetails;
    @FXML
    private TableColumn<CmpTRowDTOSoToSc, String> tblcSCConvEditCmptSrNo, tblcSCConvEditCmptParticular, tblcSCConvEditCmptPackage, tblcSCConvEditCmptLevelA, tblcSCConvEditCmptLevelB, tblcSCConvEditCmptLevelC,
            tblcSCConvEditCmptUnit, tblcSCConvEditCmptBatch, tblcSCConvEditCmptQuantity, tblcSCConvEditCmptFreeQuantity, tblcSCConvEditCmptRate, tblcSCConvEditCmptGrossAmt, tblcSCConvEditCmptDiscPerc,
            tblcSCConvEditCmptDiscAmt, tblcSCConvEditCmptTaxPerc, tblcSCConvEditCmptNetAmt, tblcSCConvEditCmptActions;
    @FXML         //for bottom supplier datails table column
    private TableColumn tcSCConvEditSupplierName, tcSCConvEditSupplierInvNo, tcSCConvEditSupplierInvDate, tcSCConvEditSupplierBatch, tcSCConvEditSupplierMrp, tcSCConvEditSupplierQty,
            tcSCConvEditSupplierRate, tcSCConvEditSupplierCost, tcSCConvEditSupplierDisper, tcSCConvEditSupplierDisRs;
    @FXML
    private TabPane myTabPaneSCConv;

    @FXML
    private Tab tabSCConvLedgerInfoEdit, tabSCConvProductInfoEdit, tabSCConvBatchInfoEdit;

    //Ledger info text data
    @FXML
    private Text lbSCConvGstNoEdit, lbSCConvAreaEdit, lbSCConvBankEdit, lbSCConvContactPersonEdit, lbSCConvMobNoEdit, lbSCConvCreditDaysEdit, lbSCConvFssaiEdit, lbSCConvLisenceNoEdit, lbSCConvRouteEdit;
    @FXML
    private Text txtSCConvProductBrandEdit, txtSCConvProductGroupEdit, txtSCConvProductSubGroupEdit, txtSCConvProductCategoryEdit, txtSCConvProductHsnEdit, txtSCConvProductTaxTypeEdit,
            txtSCConvProductTaxPerEdit, txtSCConvProductMarginPerEdit, txtSCConvProductCostEdit, txtSCConvProductShelfIdEdit, txtSCConvProductMinStockEdit, txtSCConvProductMaxStockEdit;
    @FXML                //for overall dis% and dis Rs.
    private TextField tfSCConvAllRowDisPerEdit, tfSCConvAllRowDisAmtEdit;
    @FXML               //for bottom GST details
    private TableView<GstDTO> tblvSCConvGSTEdit;
    @FXML
    private TableColumn<GstDTO, String> tcSCConvBottomGSTEdit, tcSCConvBottomCGSTEdit, tcSCConvBottomSGSTEdit, tcSCConvBottomIGSTEdit;
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
    List<CustomAddChargesDTO> addChargesDTOList = new ArrayList<>();
    private String productId;
    private static ObservableList<String> unitList = FXCollections.observableArrayList();
    private static int rowIndexBatchNo;
    private String prdBatchTaxPer;
    private int selectedRowIndex;
    private String productIdSelected, ledgerName, selectedRowPrdId;
    private String selectedLedgerId, ledgerStateCode;
    private Double costValue, costWithTaxValue, taxAmt, total_taxable_amt = 0.0;
    private Long stateCode = 0L, CompanyStateCode = 0L;
    private String batchId = "0";
    Map<String, String> map = new HashMap<>();
    private String trackingNumber = "";
    private Boolean taxFlag = false;
    private Integer rediCurrIndex = -1;
    private Boolean isProductRed = false;
    private Boolean isLedgerRed= false;
    @FXML
    private Text lbSCConvBatchBatchNoEdit, lbSCConvBatchMrpEdit, lbSCConvBatchCessPerEdit, lbSCConvBatchTotalAmtEdit, lbSCConvBatchMfgDateEdit, lbSCConvBatchPurRateEdit, lbSCConvBatchCessAmtEdit,
            lbSCConvBatchFsrEdit, lbSCConvBatchExpDateEdit, lbSCConvBatchDisPerEdit, lbSCConvBatchBarcodeEdit, lbSCConvBatchCsrEdit, lbSCConvBatchAvailStockEdit, lbSCConvBatchDisAmtEdit,
            lbSCConvBatchMarginPerEdit, lbSCConvBatchSaleRateEdit;
    private static final KeyCombination com_user_new_CTRL_X = new KeyCodeCombination(KeyCode.X, KeyCombination.CONTROL_DOWN);
    private static final KeyCombination com_user_new_CTRL_S = new KeyCodeCombination(KeyCode.S, KeyCombination.CONTROL_DOWN);

    public void sceneInitilization() {
        rootAnchorPaneSoToSc.sceneProperty().addListener((observable, oldScene, newScene) -> {
            if (newScene != null && newScene.getWindow() instanceof Stage) {
                Communicator.stage = (Stage) newScene.getWindow();
                Communicator.tranxDate = dpSCConvTranxDate.getText();
            }
        });
    }

    private void nodetraversal(Node current_node, Node next_node) {
        current_node.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                next_node.requestFocus();
                event.consume();
            }

            if (event.getCode() == KeyCode.ENTER) {
                if (current_node instanceof Button button) {
                    button.fire();
                }
            } else if (event.getCode() == KeyCode.LEFT || event.getCode() == KeyCode.RIGHT) {
                if (current_node instanceof RadioButton radioButton) {
                    radioButton.setSelected(!radioButton.isSelected());
                    radioButton.fire();
                }
            }
        });
    }

    //function for cancel button
    public void cancelFunction() {
        GlobalController.getInstance().addTabStatic(SALES_ORDER_LIST_SLUG, false);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        resposiveScreenCss();

        Platform.runLater(() -> tfSCConvLedgNameEdit.requestFocus());

        //         Enter traversal
        rootAnchorPaneSoToSc.addEventFilter(KeyEvent.KEY_PRESSED, (KeyEvent event) -> {
            if (event.getCode() == KeyCode.ENTER) {
                if (event.getTarget() instanceof Button targetButton && targetButton.getText().equalsIgnoreCase("modify")) {
                } else if (event.getTarget() instanceof Button targetButton && targetButton.getText().equalsIgnoreCase("submit")) {
                } else if (event.getTarget() instanceof Button targetButton && targetButton.getText().equalsIgnoreCase("cancel")) {
                } else if (event.getTarget() instanceof Button targetButton && targetButton.getText().equalsIgnoreCase("update")) {
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
            } else if (event.isControlDown() && event.getCode() == KeyCode.S) {

                btnSCConvSubmitFinal.fire();
            } else if (event.getCode() == KeyCode.X && event.isControlDown()) {
                btnSCConvCancelEdit.fire();
            }


        });

        sceneInitilization();
        tableInitiliazation();
        responsiveCmpt();
        responsiveSupplierTable();
        getSalesAccounts();
        getLastSaleChallanRecord();
        getOutletSalesmanMasterRecord();
        getIndirectExpenseList();
        getSaleOrderWithId();

        DateValidator.applyDateFormat(dpSCConvTranxDate);

        tvSCConvEditCMPTRow.setEditable(true);  //for enable the edit the CMPT row
        tvSCConvEditCMPTRow.getSelectionModel().setCellSelectionEnabled(true);
        tvSCConvEditCMPTRow.getSelectionModel().selectFirst();
        tvSCConvEditCMPTRow.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection == null) {
                tvSCConvEditCMPTRow.getSelectionModel().select(oldSelection);
            }
        });

        btnSCConvSubmitFinal.setOnAction(event -> {
            AlertUtility.AlertConfirmation("Confirm", LedgerMessageConsts.msgConfirmationOnSubmit+tfSCConvInvNo.getText()+LedgerMessageConsts.msgInvoiceNumber, callback -> {
                if (callback == 1) {
                    if (CommonValidationsUtils.validateForm(tfSCConvLedgNameEdit, tfSCConvInvNo)) {
                        createSalesChallan();
                    }
                } else {
                    btnSCConvSubmitFinal.requestFocus();
                }
            });
        });
        btnSCConvSubmitFinal.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                AlertUtility.AlertConfirmation("Confirm", LedgerMessageConsts.msgConfirmationOnSubmit + tfSCConvInvNo.getText() + LedgerMessageConsts.msgInvoiceNumber, callback -> {
                    if (callback == 1) {
                        if (CommonValidationsUtils.validateForm(tfSCConvLedgNameEdit, tfSCConvInvNo)) {
                            createSalesChallan();
                        }
                    } else {
                        btnSCConvSubmitFinal.requestFocus();
                    }
                });
            }
        });
        btnSCConvSubmitFinal.setOnMouseClicked(event -> {
            AlertUtility.AlertConfirmation("Confirm", LedgerMessageConsts.msgConfirmationOnSubmit + tfSCConvInvNo.getText() + LedgerMessageConsts.msgInvoiceNumber, callback -> {
                if (callback == 1) {
                    if (CommonValidationsUtils.validateForm(tfSCConvLedgNameEdit, tfSCConvInvNo)) {
                        createSalesChallan();
                    }
                } else {
                    btnSCConvSubmitFinal.requestFocus();
                }
            });
        });


        btnSCConvSubmitFinal.sceneProperty().addListener((obsValue, oldValue, newValue) -> {
            if (newValue != null) {
                newValue.getAccelerators().put(com_user_new_CTRL_S, btnSCConvSubmitFinal::fire);
            }
        });
        btnSCConvCancelEdit.sceneProperty().addListener((obsValue, oldValue, newValue) -> {
            if (newValue != null) {
                newValue.getAccelerators().put(com_user_new_CTRL_X, btnSCConvCancelEdit::fire);
            }
        });

        btnSCConvCancelEdit.setOnAction(event -> {
            AlertUtility.AlertConfirmation("Confirm", LedgerMessageConsts.msgConfirmationOnCancel + tfSCConvInvNo.getText() + LedgerMessageConsts.msgInvoiceNumber, callback -> {
                if (callback == 1) {
                    GlobalController.getInstance().addTabStatic(SALES_ORDER_LIST_SLUG, false);
                } else {
                    btnSCConvCancelEdit.requestFocus();
                }
            });
        });
        nodetraversal(dpSCConvTranxDate, tfSCConvLedgNameEdit);
        nodetraversal(tfSCConvLedgNameEdit, cmbSCConvSupplierGST);
        nodetraversal(cmbSCConvSupplierGST, cmbSCConvSalesAC);
        nodetraversal(cmbSCConvSalesAC, tfSCConvInvNo);
        nodetraversal(tfSCConvInvNo, dpSCConvSalesman);
        cmbSCConvSalesAC.focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if (newValue) {
                    cmbSCConvSalesAC.show();
                }
            }
        });

        dpSCConvSalesman.focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if (newValue) {
                    dpSCConvSalesman.show();
                }
            }
        });
        tfSCConvInvNo.addEventFilter(KeyEvent.KEY_PRESSED, event -> {

            if (event.getCode() == KeyCode.TAB || event.getCode() == KeyCode.ENTER) {
                validateSalesChallan();
            } else if (event.isShiftDown() && event.getCode() == KeyCode.TAB) {
                tfSCConvChallanSerial.requestFocus();
            }
            event.consume();
        });
        dpSCConvSalesman.setOnKeyPressed(event -> {
            if(event.isShiftDown() && event.getCode() == KeyCode.TAB){
                tfSCConvInvNo.requestFocus();
            }else if(event.getCode() == KeyCode.SPACE){
                dpSCConvSalesman.show();
            }
        });
        cmbSCConvSupplierGST.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode() == KeyCode.SPACE){
                cmbSCConvSupplierGST.show();
            }
        });
        gstTableDesign();
        chbRoundOffEdit.setOnAction(event -> {
            roundOffCalculations();
        });
//        CommonValidationsUtils.onlyEnterNumbersLimit1(tfSCConvAllRowDisPerEdit, 6);
//        CommonValidationsUtils.onlyEnterNumbersLimit(tfSCConvAllRowDisAmtEdit, 6);
        CommonValidationsUtils.restrictToDecimalNumbers(tfSCConvAllRowDisPerEdit);
        CommonValidationsUtils.restrictToDecimalNumbers(tfSCConvAllRowDisAmtEdit);
        //start code of open product window
    }

    public void setPurChallDataToProduct() {

        String tranxDate = Communicator.text_to_date.fromString(dpSCConvTranxDate.getText()).toString();
        String ledgrId = selectedLedgerId;
        String gstNum = cmbSCConvSupplierGST.getId();
        String purSerNum = tfSCConvChallanSerial.getText();
        String challanNo = tfSCConvInvNo.getText();
        String purAcc = cmbSCConvSalesAC.getId();
        String challanDate = Communicator.text_to_date.fromString(dpSCConvTranxDate.getText()).toString();

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

        mData.setRediPrdCurrIndex(rediCurrIndex);
        List<CmpTRowDTOSoToSc> currentItems = new ArrayList<>(tvSCConvEditCMPTRow.getItems());
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
        if(isLedgerRed == true){
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
                //  supplierGSTINList.add(new CommonDTO(gstin, gst_id));
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


        tfSCConvInvNo.setText(franchiseDTO.getChallanNo());
        selectedLedgerId=franchiseDTO.getLedegrId();
        tfSCConvChallanSerial.setText(franchiseDTO.getPurSerNum());
        tfSCConvLedgNameEdit.setText(ledgersById(Long.valueOf(selectedLedgerId)));
        int cnt=0,index=0;
        tvSCConvEditCMPTRow.getItems().clear();
        PurInvoiceCommunicator.levelAForPurInvoiceObservableList.clear();
        if(franchiseDTO.getProductRed()==true) {
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
//                    System.out.println("iceTable.getLevelA_id()" + purchaseInvoiceTable.getLevelA_id());

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
                tvSCConvEditCMPTRow.getItems().addAll(purchaseInvoiceTable);
                SalesChallanCalculations.rowCalculationForPurcInvoice(index, tvSCConvEditCMPTRow, callback);
                SalesChallanCalculations.calculateGst(tvSCConvEditCMPTRow, callback);
//                calculateGst(tableView, callback);

                List<LevelAForPurInvoice> levelAForPurInvoiceList = ProductUnitsPackingForSalesChallan.getAllProductUnitsPackingFlavour(purchaseInvoiceTable.getProduct_id());
                ObservableList<LevelAForPurInvoice> observableLevelAList = FXCollections.observableArrayList(levelAForPurInvoiceList);
                if (index >= 0 && index < PurInvoiceCommunicator.levelAForPurInvoiceObservableList.size()) {
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
//                    System.out.println("Index else >> : " + index);
                    PurInvoiceCommunicator.levelAForPurInvoiceObservableList.add(observableLevelAList);
                }

                cnt++;
                index++;
            }
        }

        int inx = 0;
//        System.out.println("is ledgeriiiiiiiiiii" + franchiseDTO.getLedgerRed());
        // tblvPurChallCmpTRow.getItems().clear();
        if (franchiseDTO.getLedgerRed() == true) {
//            System.out.println("is ledger" + franchiseDTO.getLedgerRed());
            for (CmpTRowDTOSoToSc rowData : franchiseDTO.getRowSaleChallan()) {
//                mData1.setProduct_id(rowData.getProduct_id());
//                mData1.setParticulars(rowData.getParticulars());
//                mData1.setPackages(rowData.getPackages());
                tvSCConvEditCMPTRow.getItems().add(rowData);
                inx++;
            }
        }

        Platform.runLater(() -> {
            TableColumn<CmpTRowDTOSoToSc, ?> colName = tvSCConvEditCMPTRow.getColumns().get(1);
            tvSCConvEditCMPTRow.edit(redirectIndex, colName);
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
//        System.out.println("responseObj" + responseObj);
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
        if (width >= 992 && width <= 1024) {
            so2ctopInnerHbOne.setSpacing(10);
            so2ctopInnerHbTwo.setSpacing(10);
            so2cTotalMainDiv.setSpacing(10);
            so2cTotalMainInnerDiv.setSpacing(8);
            so2cTotalInnerFirstDiv.setSpacing(8);
            so2cTotalInnerSecondDiv.setSpacing(8);
            // Set preferred widths as percentages of the HBox width
            so2cBottomFirstV.prefWidthProperty().bind(so2cBottomMain.widthProperty().multiply(0.5));
            so2cBottomSecondV.prefWidthProperty().bind(so2cBottomMain.widthProperty().multiply(0.22));
            so2cBottomThirdV.prefWidthProperty().bind(so2cBottomMain.widthProperty().multiply(0.24));
//            so2cBottomMain.setPrefHeight(150);
            tblvSCConvGSTEdit.getStylesheets().add(GenivisApplication.class.getResource("ui/css/purchase_challan_css/gst_table_css/gst_table_992_1024.css").toExternalForm());
            tvSCConvSupplierDetails.getStylesheets().add(GenivisApplication.class.getResource("ui/css/purchase_challan_css/supplier_table_css/supplier_table_992_1024.css").toExternalForm());
            rootAnchorPaneSoToSc.getStylesheets().add(GenivisApplication.class.getResource("ui/css/TranxCommonCssStyles/tranxCommonCssStyle1.css").toExternalForm());
        } else if (width >= 1025 && width <= 1280) {
            so2ctopInnerHbOne.setSpacing(10);
            so2ctopInnerHbTwo.setSpacing(10);
            so2cTotalMainDiv.setSpacing(10);
            so2cTotalMainInnerDiv.setSpacing(8);
            so2cTotalInnerFirstDiv.setSpacing(8);
            so2cTotalInnerSecondDiv.setSpacing(8);
            // Set preferred widths as percentages of the HBox width
            so2cBottomFirstV.prefWidthProperty().bind(so2cBottomMain.widthProperty().multiply(0.58));
            so2cBottomSecondV.prefWidthProperty().bind(so2cBottomMain.widthProperty().multiply(0.20));
            so2cBottomThirdV.prefWidthProperty().bind(so2cBottomMain.widthProperty().multiply(0.22));
//            so2cBottomMain.setPrefHeight(150);
            tblvSCConvGSTEdit.getStylesheets().add(GenivisApplication.class.getResource("ui/css/purchase_challan_css/gst_table_css/gst_table_1025_1280.css").toExternalForm());
            tvSCConvSupplierDetails.getStylesheets().add(GenivisApplication.class.getResource("ui/css/purchase_challan_css/supplier_table_css/supplier_table_1025_1280.css").toExternalForm());
            rootAnchorPaneSoToSc.getStylesheets().add(GenivisApplication.class.getResource("ui/css/TranxCommonCssStyles/tranxCommonCssStyle2.css").toExternalForm());
        } else if (width >= 1281 && width <= 1368) {
            so2ctopInnerHbOne.setSpacing(15);
            so2ctopInnerHbTwo.setSpacing(15);
            so2cTotalMainDiv.setSpacing(12);
            so2cTotalMainInnerDiv.setSpacing(8);
            so2cTotalInnerFirstDiv.setSpacing(8);
            so2cTotalInnerSecondDiv.setSpacing(8);
            // Set preferred widths as percentages of the HBox width
            so2cBottomFirstV.prefWidthProperty().bind(so2cBottomMain.widthProperty().multiply(0.6));
            so2cBottomSecondV.prefWidthProperty().bind(so2cBottomMain.widthProperty().multiply(0.18));
            so2cBottomThirdV.prefWidthProperty().bind(so2cBottomMain.widthProperty().multiply(0.22));
//            so2cBottomMain.setPrefHeight(150);
            tblvSCConvGSTEdit.getStylesheets().add(GenivisApplication.class.getResource("ui/css/purchase_challan_css/gst_table_css/gst_table_1281_1368.css").toExternalForm());
            tvSCConvSupplierDetails.getStylesheets().add(GenivisApplication.class.getResource("ui/css/purchase_challan_css/supplier_table_css/supplier_table_1281_1368.css").toExternalForm());
            rootAnchorPaneSoToSc.getStylesheets().add(GenivisApplication.class.getResource("ui/css/TranxCommonCssStyles/tranxCommonCssStyle3.css").toExternalForm());
        } else if (width >= 1369 && width <= 1400) {
            so2ctopInnerHbOne.setSpacing(15);
            so2ctopInnerHbTwo.setSpacing(15);
            so2cTotalMainDiv.setSpacing(12);
            so2cTotalMainInnerDiv.setSpacing(8);
            so2cTotalInnerFirstDiv.setSpacing(8);
            so2cTotalInnerSecondDiv.setSpacing(8);
            // Set preferred widths as percentages of the HBox width
            so2cBottomFirstV.prefWidthProperty().bind(so2cBottomMain.widthProperty().multiply(0.56));
            so2cBottomSecondV.prefWidthProperty().bind(so2cBottomMain.widthProperty().multiply(0.20));
            so2cBottomThirdV.prefWidthProperty().bind(so2cBottomMain.widthProperty().multiply(0.24));
//            so2cBottomMain.setPrefHeight(150);
            tblvSCConvGSTEdit.getStylesheets().add(GenivisApplication.class.getResource("ui/css/purchase_challan_css/gst_table_css/gst_table_1369_1400.css").toExternalForm());
            tvSCConvSupplierDetails.getStylesheets().add(GenivisApplication.class.getResource("ui/css/purchase_challan_css/supplier_table_css/supplier_table_1369_1400.css").toExternalForm());
            rootAnchorPaneSoToSc.getStylesheets().add(GenivisApplication.class.getResource("ui/css/TranxCommonCssStyles/tranxCommonCssStyle4.css").toExternalForm());
        } else if (width >= 1401 && width <= 1440) {
            so2ctopInnerHbOne.setSpacing(15);
            so2ctopInnerHbTwo.setSpacing(15);
            so2cTotalMainDiv.setSpacing(12);
            so2cTotalMainInnerDiv.setSpacing(8);
            so2cTotalInnerFirstDiv.setSpacing(8);
            so2cTotalInnerSecondDiv.setSpacing(8);
            // Set preferred widths as percentages of the HBox width
            so2cBottomFirstV.prefWidthProperty().bind(so2cBottomMain.widthProperty().multiply(0.58));
            so2cBottomSecondV.prefWidthProperty().bind(so2cBottomMain.widthProperty().multiply(0.20));
            so2cBottomThirdV.prefWidthProperty().bind(so2cBottomMain.widthProperty().multiply(0.22));
//            so2cBottomMain.setPrefHeight(150);
            tblvSCConvGSTEdit.getStylesheets().add(GenivisApplication.class.getResource("ui/css/purchase_challan_css/gst_table_css/gst_table_1401_1440.css").toExternalForm());
            tvSCConvSupplierDetails.getStylesheets().add(GenivisApplication.class.getResource("ui/css/purchase_challan_css/supplier_table_css/supplier_table_1401_1440.css").toExternalForm());
            rootAnchorPaneSoToSc.getStylesheets().add(GenivisApplication.class.getResource("ui/css/TranxCommonCssStyles/tranxCommonCssStyle5.css").toExternalForm());
        } else if (width >= 1441 && width <= 1680) {
            so2ctopInnerHbOne.setSpacing(15);
            so2ctopInnerHbTwo.setSpacing(15);
            so2cTotalMainDiv.setSpacing(15);
            so2cTotalMainInnerDiv.setSpacing(10);
            so2cTotalInnerFirstDiv.setSpacing(10);
            so2cTotalInnerSecondDiv.setSpacing(10);
            // Set preferred widths as percentages of the HBox width
            so2cBottomFirstV.prefWidthProperty().bind(so2cBottomMain.widthProperty().multiply(0.6));
            so2cBottomSecondV.prefWidthProperty().bind(so2cBottomMain.widthProperty().multiply(0.18));
            so2cBottomThirdV.prefWidthProperty().bind(so2cBottomMain.widthProperty().multiply(0.22));
//            so2cBottomMain.setPrefHeight(150);
            tblvSCConvGSTEdit.getStylesheets().add(GenivisApplication.class.getResource("ui/css/purchase_challan_css/gst_table_css/gst_table_1441_1680.css").toExternalForm());
            tvSCConvSupplierDetails.getStylesheets().add(GenivisApplication.class.getResource("ui/css/purchase_challan_css/supplier_table_css/supplier_table_1441_1680.css").toExternalForm());
            rootAnchorPaneSoToSc.getStylesheets().add(GenivisApplication.class.getResource("ui/css/TranxCommonCssStyles/tranxCommonCssStyle6.css").toExternalForm());
        } else if (width >= 1681 && width <= 1920) {

            so2ctopInnerHbOne.setSpacing(20);
            so2ctopInnerHbTwo.setSpacing(20);
            so2cTotalMainDiv.setSpacing(20);
            so2cTotalMainInnerDiv.setSpacing(10);
            so2cTotalInnerFirstDiv.setSpacing(10);
            so2cTotalInnerSecondDiv.setSpacing(10);
            // Set preferred widths as percentages of the HBox width
            so2cBottomFirstV.prefWidthProperty().bind(so2cBottomMain.widthProperty().multiply(0.6));
            so2cBottomSecondV.prefWidthProperty().bind(so2cBottomMain.widthProperty().multiply(0.18));
            so2cBottomThirdV.prefWidthProperty().bind(so2cBottomMain.widthProperty().multiply(0.22));
//            so2cBottomMain.setPrefHeight(150);
            tblvSCConvGSTEdit.getStylesheets().add(GenivisApplication.class.getResource("ui/css/gst_table.css").toExternalForm());
            tvSCConvSupplierDetails.getStylesheets().add(GenivisApplication.class.getResource("ui/css/invoice_product_history_table.css").toExternalForm());
            rootAnchorPaneSoToSc.getStylesheets().add(GenivisApplication.class.getResource("ui/css/TranxCommonCssStyles/tranxCommonCssStyle7.css").toExternalForm());
        }
    }

    private void getSaleOrderWithId() {
        try {
            Map<String, String> body = new HashMap<>();
            body.put("sales_order_ids", input);
            String requestBody = Globals.mapToStringforFormData(body);
            HttpResponse<String> response = APIClient.postFormDataRequest(requestBody, "get_sale_orders_with_ids");
            JsonObject resp = new Gson().fromJson(response.body(), JsonObject.class);
            System.out.println("RESPONSE OF BY ID " + resp);
            SaleOrderToChallanResponse responseBody = new Gson().fromJson(response.body(), SaleOrderToChallanResponse.class);
            if (responseBody.getResponseStatus() == 200) {
                setConversionData(responseBody);
            } else {
                soToscLogger.error("responesStatus " + responseBody.getResponseStatus() + "  error occured");
            }

        } catch (Exception e) {

            soToscLogger.error("error in getSaleOrderWithId()" + e.getMessage());
        }

    }

    private void setConversionData(SaleOrderToChallanResponse formData) {
        trackingNumber = formData.getInvoiceData().getTrackingNumber();
//        System.out.println("Tracking Number:"+trackingNumber);
        LocalDate challDate = LocalDate.now();
        String challanDate = challDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        dpSCConvTranxDate.setText(challanDate);
        Communicator.tranxDate = challanDate;
        tfSCConvLedgNameEdit.setText(formData.getInvoiceData().getDebtorsName());
        cmbSCConvSupplierGST.setValue(formData.getInvoiceData().getGstNo());
        tfSCConvEditNarrations.setText(formData.getInvoiceData().getNarration() != null ? formData.getInvoiceData().getNarration().toString() : "");
        selectedLedgerId = String.valueOf(formData.getInvoiceData().getDebtorsId());
        fetchSelectedLedgerData(selectedLedgerId);
        chbRoundOffEdit.setSelected(true);    //round off is not in sale order so make default checked

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
        double totalQty = 0;
        int totalFreeQty = 0;
        double totalDisAmount = 0.0;
        tvSCConvEditCMPTRow.getItems().clear();
        PurInvoiceCommunicator.levelAForPurInvoiceObservableList.clear();
        for (SaleOrderToChallanRow mRow : formData.getRow()) {
            CmpTRowDTOSoToSc row = new CmpTRowDTOSoToSc();
            soToscLogger.debug("rowww size--" + formData.getRow().size());
            soToscLogger.debug("product id on data set => " + mRow.toString());
            SingleInputDialogs.productIdSelected = String.valueOf(mRow.getProductId());
            String unitId = String.valueOf(mRow.getUnitId());
            productId = String.valueOf(mRow.getProductId());
            //Pass productId to ProductPopup for Focus in open ProductPopup in Edit sales Quot
            if (productId != "") {
                SingleInputDialogs.productId = productId;
            } else {
                SingleInputDialogs.productId = "";
            }
            TranxSelectedProduct selectedProduct = TranxCommonPopUps.getSelectedProductFromProductId(mRow.getProductName());//new
            row.setSelectedProduct(selectedProduct);
            fetchSelectedProductData(productId);   //function for setting the product data to bottom details/info
            getSupplierListbyProductId(productId);   //function for bottom supplier list
            row.setParticulars(mRow.getProductName());
            row.setUnit(mRow.getUnitName());
            row.setUnit_id(String.valueOf(mRow.getUnitId()));
            row.setPackages(mRow.getPackName());
            row.setQuantity(String.valueOf(mRow.getQty()));
            row.setBatch_or_serial("0");
            row.setRate(String.valueOf(mRow.getRate()));
            row.setGross_amount(String.valueOf(mRow.getBaseAmt()));    //getBaseAmt() amount is the gross amount inside the cmpt row
            row.setDis_per(String.valueOf(mRow.getDisPer()));
            row.setDis_amt(String.valueOf(mRow.getDisAmt()));
            row.setTax(String.valueOf(mRow.getIgst()));
            row.setNet_amount(String.valueOf(mRow.getFinalAmt()));
            row.setFree(String.valueOf(mRow.getFreeQty()));
            row.setIgst(String.valueOf(mRow.getIgst()));
            row.setSgst(String.valueOf(mRow.getSgst()));
            row.setCgst(String.valueOf(mRow.getCgst()));
            row.setTotal_igst(String.valueOf(mRow.getTotalIgst()));
            row.setTotal_sgst(String.valueOf(mRow.getTotalSgst()));
            row.setTotal_cgst(String.valueOf(mRow.getTotalCgst()));
            row.setTaxable_amt(String.valueOf(mRow.getGrossAmt()));
            row.setFinal_dis_amt(String.valueOf(mRow.getRowDisAmt()));
            row.setFinal_tax_amt(String.valueOf(mRow.getTotalIgst()));
            row.setIs_batch(String.valueOf(mRow.getIsBatch()));
            String productId = String.valueOf(mRow.getProductId());
            productIdSelected = productId;   //error when more than one CMPT row
//            System.out.println("prodcttidd " + productId + " mRow.getRowDisAmt() " + mRow.getRowDisAmt() + " finalTaxAmt " + mRow.getTotalIgst());
            row.setProduct_id(productId);
            row.setLevelA_id(mRow.getLevelAId());
            row.setLevelB_id(mRow.getLevelBId());
            row.setLevelC_id(mRow.getLevelCId());
            row.setDetails_id(String.valueOf(mRow.getDetailsId()));      //this details id is for product row not for batch...
            row.setIs_batch(String.valueOf(true));
//            row.setBatchNo(mRow.getBatchNo());     // we dont set batch no intially
            row.setFinal_amount(String.valueOf(mRow.getFinalAmt()));
            row.setReference_id(String.valueOf(mRow.getReferenceId()));
            row.setReference_type(mRow.getReferenceType());

//            System.out.println("referenceIdd " + mRow.getReferenceId() + "  getReferenceType " + mRow.getReferenceType() + "Unit" + mRow.getUnitName());
//            List<LevelAForPurInvoice> levelAForPurInvoiceList = ProductUnitsPacking2.getAllProductUnitsPackingFlavour(row.getProduct_id());
//            ObservableList<LevelAForPurInvoice> observableLevelAList = FXCollections.observableArrayList(levelAForPurInvoiceList);
//            PurInvoiceCommunicator.levelAForPurInvoiceObservableList.add(observableLevelAList);

            List<LevelAForPurInvoice> levelAForPurInvoiceList = ProductUnitsPacking2.getAllProductUnitsPackingFlavour(row.getProduct_id());
            ObservableList<LevelAForPurInvoice> observableLevelAList = FXCollections.observableArrayList(levelAForPurInvoiceList);
//            System.out.println("OBS1Size=>" + observableLevelAList.size());
//            System.out.println("OBS3Size=>" + observableLevelAList.get(0).getLabel() + " , " + observableLevelAList.get(0).getValue());
//            System.out.println("OBS2Size=>" + PurInvoiceCommunicator.levelAForPurInvoiceObservableList.size());
            if (index >= 0 && index < PurInvoiceCommunicator.levelAForPurInvoiceObservableList.size()) {
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
//                System.out.println("Index else >> : " + tvSCConvEditCMPTRow.getItems().size());
                PurInvoiceCommunicator.levelAForPurInvoiceObservableList.add(observableLevelAList);
//                System.out.println("Index else >>> : " + PurInvoiceCommunicator.levelAForPurInvoiceObservableList.size());

            }

            grossTotal = grossTotal + mRow.getBaseAmt();
            discountAmount = discountAmount + mRow.getRowDisAmt();
            totalAmount = totalAmount + mRow.getTotalAmt();
            totalTax = totalTax + mRow.getTotalIgst();
            billAmount = billAmount + mRow.getFinalAmt();
            totalQty = totalQty + mRow.getQty();
            totalFreeQty = totalFreeQty + mRow.getFreeQty();
            totalDisAmount = totalDisAmount + mRow.getRowDisAmt();

            tvSCConvEditCMPTRow.getItems().add(row);
            index++;

        }
        lblSCConvGrossTotalEdit.setText(String.valueOf(grossTotal));            //gross total -- botttom
        lblSCConvTotalDiscountEdit.setText(String.valueOf(discountAmount));     //Discount    -- bottom
        lblSCConvTotalTaxableAmountEdit.setText(String.format("%.2f", (grossTotal - totalDisAmount)));   //Total       -- bottom
        lblSCConvTotalTaxEdit.setText(String.format("%.2f", totalTax));                //Tax         -- bottom
        lblSCConvBillAmountEdit.setText(String.format("%.2f", billAmount));            //Bill Amount  -- bottom
        lblSCConvTotalQtyEdit.setText(String.valueOf(totalQty));
        lblSCConvFreeQtyEdit.setText(String.valueOf(totalFreeQty));
        tvSCConvEditCMPTRow.refresh();

    }

    //function for GST bottom details
    private void gstTableDesign() {
        tblvSCConvGSTEdit.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tblvSCConvGSTEdit.setFocusTraversable(false);
        tcSCConvBottomGSTEdit.setSortable(false);
        tcSCConvBottomCGSTEdit.setSortable(false);
        tcSCConvBottomSGSTEdit.setSortable(false);
        tcSCConvBottomIGSTEdit.setSortable(false);
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

        tcSCConvBottomIGSTEdit.setCellValueFactory(cellData -> cellData.getValue().igstProperty());
        tcSCConvBottomIGSTEdit.setStyle("-fx-alignment: CENTER;");
        tcSCConvBottomIGSTEdit.setCellFactory(col -> new TableCell<GstDTO, String>() {
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

    //function to open the additional charges window
    public void openAdditionalCharges(ActionEvent actionEvent) {
        Stage stage = (Stage) rootAnchorPaneSoToSc.getScene().getWindow();

        Double total_taxable_amt = 0.0;
        for (CmpTRowDTOSoToSc item : tvSCConvEditCMPTRow.getItems()) {
            total_taxable_amt += Double.valueOf(item.getTaxable_amt());
        }

        System.out.println("total_taxable_amttt " + total_taxable_amt);
        AdditionalCharges.additionalCharges(stage, addChargesDTOList, total_taxable_amt, (input1, input2) -> {
            addChargesDTOList.clear();
            addChargesDTOList.addAll(input2);

//            System.out.println("acDelDetailsIds:" + input1[0]);
//            System.out.println("Add Charges:" + input1[1]);

            tfSCConvAddChrgTotalAmt.setText(String.format(input1[1]));
            SaleOrderToChallanCalculation.additionalChargesCalculation(input1[1], tvSCConvEditCMPTRow, callback);

        });
    }

    //function for validation of Sales challan number
    private void validateSalesChallan() {
        try {
            String challlanNo = tfSCConvInvNo.getText().trim();
            soToscLogger.info("starting of sales challan number validation");
            Map<String, String> map = new HashMap<>();
            map.put("salesChallanNo", challlanNo);
            String reqBody = Globals.mapToStringforFormData(map);
            HttpResponse<String> response = APIClient.postFormDataRequest(reqBody, EndPoints.VALIDATE_SALES_CHALLAN_NUMBER);
            JsonObject jsonObject = new Gson().fromJson(response.body(), JsonObject.class);
//            System.out.println("jsonObject in validateSalesChallan " + jsonObject);
            if (jsonObject.get("responseStatus").getAsInt() == 409) {     //for duplicate challan number
                AlertUtility.AlertErrorTimeout("Error", "Duplicate Sales Challan Number", callback -> {
                    tfSCConvInvNo.requestFocus();
                });
            } else dpSCConvSalesman.requestFocus();

        } catch (Exception e) {
            soToscLogger.error("error in validate sales challan");
        }
    }

    //function for round off calculation
    private void roundOffCalculations() {
        Double billamt = 0.00; // local variable to store row wise  Bill amount
        Double total_bill_amt = 0.00; // local variable to store Final bill Amount without roundoff
        Double final_r_off_bill_amt = 0.00; // local variable to store Final Bill amount with round off
        Double roundOffAmt = 0.00; // local variable to store Roundoff Amount
        for (CmpTRowDTOSoToSc cmpTRowDTO : tvSCConvEditCMPTRow.getItems()) {
            billamt = !cmpTRowDTO.getNet_amount().isEmpty() ? Double.valueOf(cmpTRowDTO.getNet_amount()) : 0.0;
            total_bill_amt += billamt;
        }
        final_r_off_bill_amt = (double) Math.round(total_bill_amt);
        roundOffAmt = final_r_off_bill_amt - total_bill_amt;

        if (chbRoundOffEdit.isSelected()) {
            lblSCConvBillAmountEdit.setText(String.format("%.2f", final_r_off_bill_amt));
            lblSCConvRoundOffEdit.setText(String.format("%.2f", roundOffAmt));
        } else {
            lblSCConvBillAmountEdit.setText(String.format("%.2f", total_bill_amt));
            lblSCConvRoundOffEdit.setText("0.00");
        }

    }

    //todo:Set the SUpplier Details of the Selected Product Bottom in Supplier Table
    private void getSupplierListbyProductId(String id) {
        //todo: activating the product tab
//        tpSalesOrder.getSelectionModel().select(tabSalesOrderProduct);

        Map<String, String> map = new HashMap<>();
        map.put("productId", id);
        String formData = Globals.mapToStringforFormData(map);
        HttpResponse<String> response = APIClient.postFormDataRequest(formData, EndPoints.GET_SUPPLIERLIST_BY_PRODUCTID_ENDPOINT);
        String responseBody = response.body();
        JsonObject jsonObject = new Gson().fromJson(responseBody, JsonObject.class);
//        System.out.println("Supplier List Res ==>" + jsonObject);
        if (jsonObject.get("responseStatus").getAsInt() == 200) {
//            JsonObject resultObj = jsonObject.getAsJsonObject("result");
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
            String requestBody = Globals.mapToStringforFormData(body);
            HttpResponse<String> response = APIClient.postFormDataRequest(requestBody, EndPoints.TRANSACTION_PRODUCT_DETAILS);
//            System.out.println("Product Details response body" + response.body());
            JsonObject jsonObject = new Gson().fromJson(response.body(), JsonObject.class);
            ObservableList<TranxProductWindowDTO> observableList = FXCollections.observableArrayList();
            //todo: activating the product tab
            myTabPaneSCConv.getSelectionModel().select(tabSCConvProductInfoEdit);
            if (jsonObject.get("responseStatus").getAsInt() == 200) {
                myTabPaneSCConv.getSelectionModel().select(tabSCConvProductInfoEdit);

                JsonObject item = jsonObject.get("result").getAsJsonObject();
                String brand = item.get("brand").getAsString();
                String hsn = item.get("hsn").getAsString();
                String group = item.get("group").getAsString();
                String subgroup = item.get("group").getAsString();
                String category = item.get("category").getAsString();
//                String supplier = item.get("supplier").getAsString();
                String tax_type = item.get("tax_type").getAsString();
                String tax = item.get("tax_per").getAsString();
                String margin = item.get("margin_per").getAsString();
                String cost = item.get("cost").getAsString();
                String shelfId = item.get("shelf_id").getAsString();
                String min_stock = item.get("min_stocks").getAsString();
                String max_stock = item.get("max_stocks").getAsString();
                //setting data in Product details block in sales quotation page
                txtSCConvProductBrandEdit.setText(brand);
                txtSCConvProductSubGroupEdit.setText(subgroup);
                txtSCConvProductGroupEdit.setText(group);
                txtSCConvProductCategoryEdit.setText(category);
                txtSCConvProductHsnEdit.setText(hsn);
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

    //function for get the batch details
    private void getProductBatchsById(String bNo, String bId) {
        APIClient apiClient = null;
        tabPane.getSelectionModel().select(tabSCConvBatchInfoEdit);
        soToscLogger.debug("Get Supplier List Data Started...");

        Map<String, String> map = new HashMap<>();
        map.put("batchNo", bNo);
        map.put("id", bId);
        String formData = Globals.mapToStringforFormData(map);
//        System.out.println("FormData: " + formData);
        soToscLogger.debug("Form Data:" + formData);
        apiClient = new APIClient(EndPoints.GET_BATCH_DETAILS_ENDPOINT, formData, RequestType.FORM_DATA);

        apiClient.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent workerStateEvent) {
                JsonObject jsonObject = new Gson().fromJson(workerStateEvent.getSource().getValue().toString(), JsonObject.class);
                if (jsonObject.get("responseStatus").getAsInt() == 200) {
                    JsonObject batchJsonObj = jsonObject.getAsJsonObject("response");
                    lbSCConvBatchBatchNoEdit.setText(batchJsonObj.get("batchNo").getAsString());
                    lbSCConvBatchMrpEdit.setText(batchJsonObj.get("batchMrp").getAsString());
                    lbSCConvBatchMfgDateEdit.setText(batchJsonObj.get("mfgDate").getAsString());
                    lbSCConvBatchPurRateEdit.setText(batchJsonObj.get("pur_rate").getAsString());
                    lbSCConvBatchFsrEdit.setText(batchJsonObj.get("fsrai").getAsString());
                    lbSCConvBatchExpDateEdit.setText(batchJsonObj.get("expiry_date").getAsString());
                    lbSCConvBatchDisPerEdit.setText(batchJsonObj.get("b_dis_per").getAsString());
//                        lbSCBatchBarcode.setText(batchJsonObj.get("csrmh").getAsString());
                    lbSCConvBatchCsrEdit.setText(batchJsonObj.get("csrai").getAsString());
                    lbSCConvBatchAvailStockEdit.setText(batchJsonObj.get("free_qty").getAsString());
                    lbSCConvBatchDisAmtEdit.setText(batchJsonObj.get("b_dis_amt").getAsString());
                    lbSCConvBatchMarginPerEdit.setText(batchJsonObj.get("min_margin").getAsString());
                    lbSCConvBatchSaleRateEdit.setText(batchJsonObj.get("sales_rate").getAsString());


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

    //todo: fetch Selected ledger data
    public void fetchSelectedLedgerData(String LedgerId) {
        try {
            soToscLogger.info("Fetching Selected Ledger Data to show in Ledger info");
            Map<String, String> body = new HashMap<>();
            body.put("ledger_id", LedgerId);
//            System.out.println("ledger_id 123 " + LedgerId);
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
                JsonObject item = jsonObject.get("result").getAsJsonObject();
                stateCode = item.get("stateCode").getAsLong();
                ledgerStateCode = String.valueOf(item.get("stateCode").getAsLong());
//                System.out.println("ledgerStateCode--- "+ledgerStateCode);

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


    //function for responsicve of CMPT row Table
//    private void responsiveCmpt() {
//        tblcSCConvEditCmptSrNo.prefWidthProperty().bind(tvSCConvEditCMPTRow.widthProperty().multiply(0.03));
//        tblcSCConvEditCmptPackage.prefWidthProperty().bind(tvSCConvEditCMPTRow.widthProperty().multiply(0.05));
//        tblcSCConvEditCmptUnit.prefWidthProperty().bind(tvSCConvEditCMPTRow.widthProperty().multiply(0.05));
//        tblcSCConvEditCmptQuantity.prefWidthProperty().bind(tvSCConvEditCMPTRow.widthProperty().multiply(0.05));
//        tblcSCConvEditCmptRate.prefWidthProperty().bind(tvSCConvEditCMPTRow.widthProperty().multiply(0.08));
//        tblcSCConvEditCmptDiscPerc.prefWidthProperty().bind(tvSCConvEditCMPTRow.widthProperty().multiply(0.05));
//        tblcSCConvEditCmptDiscAmt.prefWidthProperty().bind(tvSCConvEditCMPTRow.widthProperty().multiply(0.05));
//        tblcSCConvEditCmptActions.prefWidthProperty().bind(tvSCConvEditCMPTRow.widthProperty().multiply(0.03));
//        tblcSCConvEditCmptBatch.prefWidthProperty().bind(tvSCConvEditCMPTRow.widthProperty().multiply(0.07));
//        if (tblcSCConvEditCmptLevelA.isVisible() == true && tblcSCConvEditCmptLevelB.isVisible() == false && tblcSCConvEditCmptLevelC.isVisible() == false) {
//            tblcSCConvEditCmptParticular.prefWidthProperty().bind(tvSCConvEditCMPTRow.widthProperty().multiply(0.28));
//            tblcSCConvEditCmptGrossAmt.prefWidthProperty().bind(tvSCConvEditCMPTRow.widthProperty().multiply(0.11));
//            tblcSCConvEditCmptNetAmt.prefWidthProperty().bind(tvSCConvEditCMPTRow.widthProperty().multiply(0.08));
//            tblcSCConvEditCmptLevelA.prefWidthProperty().bind(tvSCConvEditCMPTRow.widthProperty().multiply(0.0));
//        } else if (tblcSCConvEditCmptLevelA.isVisible() == true && tblcSCConvEditCmptLevelB.isVisible() == true && tblcSCConvEditCmptLevelC.isVisible() == false) {
//            tblcSCConvEditCmptLevelA.prefWidthProperty().bind(tvSCConvEditCMPTRow.widthProperty().multiply(0.00));
//            tblcSCConvEditCmptParticular.prefWidthProperty().bind(tvSCConvEditCMPTRow.widthProperty().multiply(0.26));
//            tblcSCConvEditCmptGrossAmt.prefWidthProperty().bind(tvSCConvEditCMPTRow.widthProperty().multiply(0.08));
//            tblcSCConvEditCmptNetAmt.prefWidthProperty().bind(tvSCConvEditCMPTRow.widthProperty().multiply(0.1));
//            tblcSCConvEditCmptLevelB.prefWidthProperty().bind(tvSCConvEditCMPTRow.widthProperty().multiply(0.0));
//        } else if (tblcSCConvEditCmptLevelA.isVisible() == true && tblcSCConvEditCmptLevelB.isVisible() == true && tblcSCConvEditCmptLevelC.isVisible() == true) {
//            tblcSCConvEditCmptParticular.prefWidthProperty().bind(tvSCConvEditCMPTRow.widthProperty().multiply(0.22));
//            tblcSCConvEditCmptGrossAmt.prefWidthProperty().bind(tvSCConvEditCMPTRow.widthProperty().multiply(0.1));
//            tblcSCConvEditCmptNetAmt.prefWidthProperty().bind(tvSCConvEditCMPTRow.widthProperty().multiply(0.1));
//            tblcSCConvEditCmptLevelA.prefWidthProperty().bind(tvSCConvEditCMPTRow.widthProperty().multiply(0.0));
//            tblcSCConvEditCmptLevelB.prefWidthProperty().bind(tvSCConvEditCMPTRow.widthProperty().multiply(0.0));
//            tblcSCConvEditCmptLevelC.prefWidthProperty().bind(tvSCConvEditCMPTRow.widthProperty().multiply(0.0));
//        } else {
//            tblcSCConvEditCmptParticular.prefWidthProperty().bind(tvSCConvEditCMPTRow.widthProperty().multiply(0.29));
//            tblcSCConvEditCmptGrossAmt.prefWidthProperty().bind(tvSCConvEditCMPTRow.widthProperty().multiply(0.12));
//            tblcSCConvEditCmptNetAmt.prefWidthProperty().bind(tvSCConvEditCMPTRow.widthProperty().multiply(0.08));
//        }
//    }

    private void responsiveCmpt() {
        if (Globals.getUserControlsWithSlug("is_free_qty") == true) {
            tblcSCConvEditCmptFreeQuantity.prefWidthProperty().bind(tvSCConvEditCMPTRow.widthProperty().multiply(0.04));
            tblcSCConvEditCmptParticular.prefWidthProperty().bind(tvSCConvEditCMPTRow.widthProperty().multiply(0.28));
            tblcSCConvEditCmptGrossAmt.prefWidthProperty().bind(tvSCConvEditCMPTRow.widthProperty().multiply(0.11));
            tblcSCConvEditCmptNetAmt.prefWidthProperty().bind(tvSCConvEditCMPTRow.widthProperty().multiply(0.08));
        } else {
            tblcSCConvEditCmptFreeQuantity.prefWidthProperty().bind(tvSCConvEditCMPTRow.widthProperty().multiply(0.00));
            tblcSCConvEditCmptParticular.prefWidthProperty().bind(tvSCConvEditCMPTRow.widthProperty().multiply(0.28));
            tblcSCConvEditCmptGrossAmt.prefWidthProperty().bind(tvSCConvEditCMPTRow.widthProperty().multiply(0.11));
            tblcSCConvEditCmptNetAmt.prefWidthProperty().bind(tvSCConvEditCMPTRow.widthProperty().multiply(0.08));
        }
        tblcSCConvEditCmptSrNo.prefWidthProperty().bind(tvSCConvEditCMPTRow.widthProperty().multiply(0.03));
        tblcSCConvEditCmptPackage.prefWidthProperty().bind(tvSCConvEditCMPTRow.widthProperty().multiply(0.05));
        tblcSCConvEditCmptUnit.prefWidthProperty().bind(tvSCConvEditCMPTRow.widthProperty().multiply(0.07));
        tblcSCConvEditCmptQuantity.prefWidthProperty().bind(tvSCConvEditCMPTRow.widthProperty().multiply(0.05));
        tblcSCConvEditCmptRate.prefWidthProperty().bind(tvSCConvEditCMPTRow.widthProperty().multiply(0.08));
        tblcSCConvEditCmptDiscPerc.prefWidthProperty().bind(tvSCConvEditCMPTRow.widthProperty().multiply(0.05));
        tblcSCConvEditCmptDiscAmt.prefWidthProperty().bind(tvSCConvEditCMPTRow.widthProperty().multiply(0.05));
        tblcSCConvEditCmptActions.prefWidthProperty().bind(tvSCConvEditCMPTRow.widthProperty().multiply(0.03));
        tblcSCConvEditCmptBatch.prefWidthProperty().bind(tvSCConvEditCMPTRow.widthProperty().multiply(0.07));

    }

    //function for responsive of Bottom supplier details
    private void responsiveSupplierTable() {
        tcSCConvEditSupplierName.prefWidthProperty().bind(tvSCConvSupplierDetails.widthProperty().multiply(0.2));
        tcSCConvEditSupplierInvNo.prefWidthProperty().bind(tvSCConvSupplierDetails.widthProperty().multiply(0.1));
        tcSCConvEditSupplierInvDate.prefWidthProperty().bind(tvSCConvSupplierDetails.widthProperty().multiply(0.08));
        tcSCConvEditSupplierBatch.prefWidthProperty().bind(tvSCConvSupplierDetails.widthProperty().multiply(0.08));
        tcSCConvEditSupplierMrp.prefWidthProperty().bind(tvSCConvSupplierDetails.widthProperty().multiply(0.12));
        tcSCConvEditSupplierQty.prefWidthProperty().bind(tvSCConvSupplierDetails.widthProperty().multiply(0.06));
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
        System.out.println("taxCalculation " + item[9]);

        tblvSCConvGSTEdit.getItems().clear();
        ObservableList<GstDTO> gstDTOObservableList = FXCollections.observableArrayList();
        ObservableList<?> item9List = (ObservableList<?>) item[9];
        for (Object obj : item9List) {
            if (obj instanceof GstDTO) {
                gstDTOObservableList.add((GstDTO) obj);
            }
        }
//        System.out.println("gstDTOObservableListtt-- " + gstDTOObservableList);
        tblvSCConvGSTEdit.getItems().addAll(gstDTOObservableList);

        if (!ledgerStateCode.equalsIgnoreCase(GlobalTranx.getCompanyStateCode().toString())) {
            tcSCConvBottomCGSTEdit.setVisible(false);
            tcSCConvBottomSGSTEdit.setVisible(false);
            tcSCConvBottomIGSTEdit.setVisible(true);
        } else {
            tcSCConvBottomCGSTEdit.setVisible(true);
            tcSCConvBottomSGSTEdit.setVisible(true);
            tcSCConvBottomIGSTEdit.setVisible(false);
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
        if (ledgerStateCode.equalsIgnoreCase(GlobalTranx.getCompanyStateCode().toString())) {
            jsonObject.add("cgst", jsonArray);
            jsonObject.add("sgst", jsonArray);
        } else {
            jsonObject.add("igst", jsonArray);
        }
        map.put("taxCalculation", jsonObject.toString());


        lblSCConvBillAmountEdit.setText((String) item[2]); //totalNetAmt for bill amount
        lblSCConvTotalQtyEdit.setText((String) item[0]);
        lblSCConvFreeQtyEdit.setText((String) item[1]);                 //for free qty
//        totalamt = (String) item[2];

//        total_purchase_discount_amt = (String) item[4];
        if (ledgerStateCode.equalsIgnoreCase(GlobalTranx.getCompanyStateCode().toString())) {
            taxFlag = true;
        }
        System.out.println("ledgerStateCodeee =>" + ledgerStateCode + " GlobalTranx.getCompanyStateCode()==>" + GlobalTranx.getCompanyStateCode() +
                " taxFlaggg.. " + taxFlag);      //for igst calc
        total_taxable_amt = Double.parseDouble((String) item[5]);
        lblSCConvGrossTotalEdit.setText((String) item[3]);
        lblSCConvTotalDiscountEdit.setText((String) item[4]);
        lblSCConvTotalTaxableAmountEdit.setText((String) item[5]);    //total taxable amount ==>> amount without applying the tax
        lblSCConvTotalTaxEdit.setText((String) item[6]);

        chbRoundOffEdit.setSelected(true);
        roundOffCalculations();

    };

    TableCellCallback<Integer> unit_callback = currentIndex -> {
        System.out.println("i am in" + currentIndex);
//        System.out.println("i am in ledgerStateCode" + ledgerStateCode);
        CmpTRowDTOSoToSc tranxRow = tvSCConvEditCMPTRow.getItems().get(currentIndex);
        //! ? Ledger State Code get selected
        if (ledgerStateCode != null) {
            if (ledgerStateCode.equalsIgnoreCase(Globals.mhStateCode)) {
                System.out.println("tranxRow.getUnitWiseRate() => " + tranxRow.getUnitWiseRateMH());
                if (tranxRow.getRate().isEmpty()) {
                    tranxRow.setRate(tranxRow.getUnitWiseRateMH() + "");
                }
            } else {
                if (tranxRow.getRate().isEmpty()) {
                    System.out.println("tranxRow.getUnitWiseRate() => " + tranxRow.getUnitWiseRateAI());
                    tranxRow.setRate(tranxRow.getUnitWiseRateAI() + "");
                }
            }
        }

    };
    TableCellCallback<Object[]> batchID_callback = item -> {
        getProductBatchsById((String) item[0], (String) item[1]);
        batchId = (String) item[1];

    };
    TableCellCallback<Object[]> productID_callback = item -> {
//        System.out.println("in productID_callback item[0]--" + item[0] + " [1]== " + item[1]);
        fetchSelectedProductData((String) item[1]);
        getSupplierListbyProductId((String) item[1]);

    };
    TableCellCallback<String> addRow = item -> {
//        System.out.println("in add Row");
//        System.out.println(item);
        tvSCConvEditCMPTRow.getItems().addAll(new CmpTRowDTOSoToSc("", "0", "1", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", ""));

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

    public void tableInitiliazation() {

        tvSCConvEditCMPTRow.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tvSCConvEditCMPTRow.setEditable(true);
        tvSCConvEditCMPTRow.setFocusTraversable(false);

//        Label headerLabel = new Label("Sr\nNo.");
//        tblcSCConvEditCmptSrNo.setGraphic(headerLabel);
        tblcSCConvEditCmptSrNo.setStyle("-fx-alignment: CENTER;");
        tblcSCConvEditCmptSrNo.setCellFactory(new LineNumbersCellFactory());

        tblcSCConvEditCmptPackage.setCellValueFactory(cellData -> cellData.getValue().packagesProperty());
        tblcSCConvEditCmptPackage.setStyle("-fx-alignment: CENTER;");

        tblcSCConvEditCmptLevelA.setCellValueFactory(cellData -> cellData.getValue().levelAProperty());
        tblcSCConvEditCmptLevelA.setCellFactory(column -> new ComboBoxTableCellForLevelASoToSc("tblcSCConvEditCmptLevelA"));

        tblcSCConvEditCmptLevelB.setCellValueFactory(cellData -> cellData.getValue().levelBProperty());
        tblcSCConvEditCmptLevelB.setCellFactory(column -> new ComboBoxTableCellForLevelBSoToSc("tblcSCConvEditCmptLevelB"));

        tblcSCConvEditCmptLevelC.setCellValueFactory(cellData -> cellData.getValue().levelCProperty());
        tblcSCConvEditCmptLevelC.setCellFactory(column -> new ComboBoxTableCellForLevelCSoToSc("tblcSCConvEditCmptLevelC"));

        tblcSCConvEditCmptUnit.setCellValueFactory(cellData -> cellData.getValue().unitProperty());
        tblcSCConvEditCmptUnit.setCellFactory(column -> new ComboBoxTableCellForUnitSoToSc("tblcSCConvEditCmptUnit", unit_callback));

        tblcSCConvEditCmptParticular.setCellValueFactory(cellData -> cellData.getValue().particularsProperty());
        tblcSCConvEditCmptParticular.setCellFactory(column -> new TextFieldTableCellForSOrderToSChallan("tblcSCConvEditCmptParticular", callback, productID_callback, tfSCConvEditNarrations, addPrdCalbak));

        tblcSCConvEditCmptBatch.setCellValueFactory(cellData -> cellData.getValue().batch_or_serialProperty());
        tblcSCConvEditCmptBatch.setCellFactory(column -> new TextFieldTableCellForSOrderToSChallan("tblcSCConvEditCmptBatch", callback, productID_callback, addRow, batchID_callback));


        tblcSCConvEditCmptQuantity.setCellValueFactory(cellData -> cellData.getValue().quantityProperty());
        tblcSCConvEditCmptQuantity.setCellFactory(column -> new TextFieldTableCellForSOrderToSChallan("tblcSCConvEditCmptQuantity", callback, tfSCConvEditNarrations));

        tblcSCConvEditCmptFreeQuantity.setCellValueFactory(cellData -> cellData.getValue().freeProperty());
        tblcSCConvEditCmptFreeQuantity.setCellFactory(column -> new TextFieldTableCellForSOrderToSChallan("tblcSCConvEditCmptFreeQuantity", callback, tfSCConvEditNarrations));

        tblcSCConvEditCmptRate.setCellValueFactory(cellData -> cellData.getValue().rateProperty());
        tblcSCConvEditCmptRate.setCellFactory(column -> new TextFieldTableCellForSOrderToSChallan("tblcSCConvEditCmptRate", callback, tfSCConvEditNarrations));

        tblcSCConvEditCmptGrossAmt.setCellValueFactory(cellData -> cellData.getValue().gross_amountProperty());
        tblcSCConvEditCmptGrossAmt.setCellFactory(column -> new TextFieldTableCellForSOrderToSChallan("tblcSCConvEditCmptGrossAmt", callback, tfSCConvEditNarrations));

        tblcSCConvEditCmptDiscPerc.setCellValueFactory(cellData -> cellData.getValue().dis_perProperty());
        tblcSCConvEditCmptDiscPerc.setCellFactory(column -> new TextFieldTableCellForSOrderToSChallan("tblcSCConvEditCmptDiscPerc", callback, tfSCConvEditNarrations));

        tblcSCConvEditCmptDiscPerc.setCellValueFactory(cellData -> cellData.getValue().dis_perProperty());
        tblcSCConvEditCmptDiscPerc.setCellFactory(column -> new TextFieldTableCellForSOrderToSChallan("tblcSCConvEditCmptDiscPerc", callback, tfSCConvEditNarrations));

        tblcSCConvEditCmptDiscAmt.setCellValueFactory(cellData -> cellData.getValue().dis_amtProperty());
        tblcSCConvEditCmptDiscAmt.setCellFactory(column -> new TextFieldTableCellForSOrderToSChallan("tblcSCConvEditCmptDiscAmt", callback, tfSCConvEditNarrations));

        tblcSCConvEditCmptTaxPerc.setCellValueFactory(cellData -> cellData.getValue().taxProperty());
        tblcSCConvEditCmptTaxPerc.setCellFactory(column -> new TextFieldTableCellForSOrderToSChallan("tblcSCConvEditCmptTaxPerc", callback, tfSCConvEditNarrations));

        tblcSCConvEditCmptNetAmt.setCellValueFactory(cellData -> cellData.getValue().net_amountProperty());
        tblcSCConvEditCmptNetAmt.setCellFactory(column -> new TextFieldTableCellForSOrderToSChallan("tblcSCConvEditCmptNetAmt", callback, tfSCConvEditNarrations));

//        tblcSCConvEditCmptActions.setCellValueFactory(cellData -> cellData.getValue().net_amountProperty());
        tblcSCConvEditCmptActions.setCellFactory(column -> new ButtonTableCellSoToSc());


        columnVisibility(tblcSCConvEditCmptLevelA, Globals.getUserControlsWithSlug("is_level_a"));
        columnVisibility(tblcSCConvEditCmptLevelB, Globals.getUserControlsWithSlug("is_level_b"));
        columnVisibility(tblcSCConvEditCmptLevelC, Globals.getUserControlsWithSlug("is_level_c"));
        //! Free Qty
        columnVisibility(tblcSCConvEditCmptFreeQuantity, Globals.getUserControlsWithSlug("is_free_qty"));

        //!Level A,B,C set Title as per configuration
        tblcSCConvEditCmptLevelA.setText(Globals.getUserControlsNameWithSlug("is_level_a"));
        tblcSCConvEditCmptLevelB.setText(Globals.getUserControlsNameWithSlug("is_level_B"));
        tblcSCConvEditCmptLevelC.setText(Globals.getUserControlsNameWithSlug("is_level_C"));

    }

    private void columnVisibility(TableColumn<CmpTRowDTOSoToSc, String> column, boolean visible) {
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

    //function for overall discount in percentage
    @FXML
    private void sales_disc_per(KeyEvent keyEvent) {
        String discText = tfSCConvAllRowDisPerEdit.getText();
        if (!TextUtils.isEmpty(discText)) {
            double totalTaxableAmt = 0.0;
            for (int i = 0; i < tvSCConvEditCMPTRow.getItems().size(); i++) {
                SaleOrderToChallanCalculation.rowCalculationForPurcInvoice(i, tvSCConvEditCMPTRow, callback);//call row calculation function
                CmpTRowDTOSoToSc purchaseInvoiceTable = tvSCConvEditCMPTRow.getItems().get(i);
                totalTaxableAmt = totalTaxableAmt + Double.parseDouble(purchaseInvoiceTable.getTaxable_amt());

            }
            double disc_per = Double.parseDouble(discText);
            Double amount = (totalTaxableAmt * disc_per) / 100;
            tfSCConvAllRowDisAmtEdit.setText(String.valueOf(amount));
            SaleOrderToChallanCalculation.discountPropotionalCalculation(disc_per + "", "0", tfSCConvAddChrgTotalAmt.getText(), tvSCConvEditCMPTRow, callback);
            if (!tfSCConvAddChrgTotalAmt.getText().isEmpty()) {
                SaleOrderToChallanCalculation.additionalChargesCalculation(tfSCConvAddChrgTotalAmt.getText(), tvSCConvEditCMPTRow, callback);
            }
        } else {
            tfSCConvAllRowDisAmtEdit.setText("");

            SaleOrderToChallanCalculation.discountPropotionalCalculation("0", "0", tfSCConvAddChrgTotalAmt.getText(), tvSCConvEditCMPTRow, callback);
            if (!tfSCConvAddChrgTotalAmt.getText().isEmpty()) {
                SaleOrderToChallanCalculation.additionalChargesCalculation(tfSCConvAddChrgTotalAmt.getText(), tvSCConvEditCMPTRow, callback);
            }
        }

        //? Calculate Tax for each Row
        SaleOrderToChallanCalculation.calculateGst(tvSCConvEditCMPTRow, callback);
    }

    //function for overall discount in Amount
    @FXML
    private void sales_disc_amt(KeyEvent keyEvent) {
        String discAmtText = tfSCConvAllRowDisAmtEdit.getText();
        if (!TextUtils.isEmpty(discAmtText)) {
            double totalTaxableAmt = 0.0;
            for (int i = 0; i < tvSCConvEditCMPTRow.getItems().size(); i++) {

                SaleOrderToChallanCalculation.rowCalculationForPurcInvoice(i, tvSCConvEditCMPTRow, callback);//call row calculation function
                CmpTRowDTOSoToSc purchaseInvoiceTable = tvSCConvEditCMPTRow.getItems().get(i);
                totalTaxableAmt = totalTaxableAmt + Double.parseDouble(purchaseInvoiceTable.getTaxable_amt());

            }
            double disc_amt = Double.parseDouble(discAmtText);
            double percentage = (disc_amt / totalTaxableAmt) * 100;
            tfSCConvAllRowDisPerEdit.setText(String.format("%.2f", percentage));

            SaleOrderToChallanCalculation.discountPropotionalCalculation("0", disc_amt + "", tfSCConvAddChrgTotalAmt.getText(), tvSCConvEditCMPTRow, callback);
            if (!tfSCConvAddChrgTotalAmt.getText().isEmpty()) {
                SaleOrderToChallanCalculation.additionalChargesCalculation(tfSCConvAddChrgTotalAmt.getText(), tvSCConvEditCMPTRow, callback);
            }
        } else {
            tfSCConvAllRowDisPerEdit.setText("");

            SaleOrderToChallanCalculation.discountPropotionalCalculation("0", "0", tfSCConvAddChrgTotalAmt.getText(), tvSCConvEditCMPTRow, callback);
            if (!tfSCConvAddChrgTotalAmt.getText().isEmpty()) {
//                System.out.println("Additioanal CHarges Called >> ");
                SaleOrderToChallanCalculation.additionalChargesCalculation(tfSCConvAddChrgTotalAmt.getText(), tvSCConvEditCMPTRow, callback);
            }
        }
        // Calculate Tax for each Row
        SaleOrderToChallanCalculation.calculateGst(tvSCConvEditCMPTRow, callback);
    }

    private void moveFocusToNextRow(TablePosition<CmpTRowDTOSoToSc, ?> position) {
        final int row = position.getRow();

        if (row + 1 < tvSCConvEditCMPTRow.getItems().size()) {
            tvSCConvEditCMPTRow.getSelectionModel().select(row + 1, tvSCConvEditCMPTRow.getColumns().get(0));
            tvSCConvEditCMPTRow.edit(row + 1, tvSCConvEditCMPTRow.getColumns().get(0));
        }
    }

    private void moveFocusToNextCell(TablePosition<CmpTRowDTOSoToSc, ?> position, boolean nextColumn) {
        final int row = position.getRow();
        final int col = position.getColumn();

        if (nextColumn) {
            if (col + 1 < tvSCConvEditCMPTRow.getColumns().size()) {
                tvSCConvEditCMPTRow.getSelectionModel().select(row, tvSCConvEditCMPTRow.getColumns().get(col + 1));
                tvSCConvEditCMPTRow.edit(row, tvSCConvEditCMPTRow.getColumns().get(col + 1));
            } else if (row + 1 < tvSCConvEditCMPTRow.getItems().size()) {
                tvSCConvEditCMPTRow.getSelectionModel().select(row + 1, tvSCConvEditCMPTRow.getColumns().get(0));
                tvSCConvEditCMPTRow.edit(row + 1, tvSCConvEditCMPTRow.getColumns().get(0));
            }
        }
    }

//    private void calculateGst() {    //reference from purchase challan
//
//        String addChgAmt = tfSCConvAddChrgTotalAmt.getText();
//        Double addChargAmount = 0.0;
//        if (!addChgAmt.isEmpty()) {
//            addChargAmount = Double.parseDouble(addChgAmt);
//        }
//
//        Map<Double, Double> cgstTotals = new HashMap<>(); //used to merge same cgst Percentage
//        Map<Double, Double> sgstTotals = new HashMap<>();//used to merge same sgst Percentage
//        Map<Double, Double> igstTotals = new HashMap<>();//used to merge same sgst Percentage
//        // Initialize totals to zero for each tax percentage
//        for (Double taxPercentage : new Double[]{3.0, 5.0, 12.0, 18.0, 28.0}) {
//            cgstTotals.put(taxPercentage, 0.0);
//            sgstTotals.put(taxPercentage, 0.0);
//            igstTotals.put(taxPercentage, 0.0);
//        }
//        Double taxPercentage = 0.0; // variable to store tax percantage
//        Double totalQuantity = 0.0; // variable to store  total Quantity
//        Double totalFreeQuantity = 0.0; // variable to store  totalFree Quantity
//        Double freeQuantity = 0.0; // variable to store row wise free qty
//        Double totalGrossAmt = 0.0, grossAmt = 0.0; // variable to store total Gross Amount and row wise gross amount
//        Double totaltaxableAmt = 0.0, taxableAmt = 0.0; // variable to store total taxable Amount and row wise taxable amount
//        Double totalDisAmt = 0.0, disAmt = 0.0; // variable to store total discount Amount and row wise discount amount
//        Double totalTaxAmt = 0.0, taxAmt = 0.0;// variable to store total taxamount Amount and row wise taxamount amount
//        Double totalNetAmt = 0.0, netAmount = 0.0; // variable to store total Net Amount and row wise Net amount
//
//        Double totalFinalSgst = 0.00; // variable to store total SGST Amount
//        Double totalFinalCgst = 0.00; // variable to store total CGST Amount
//        Double totalFinalIgst = 0.00; // variable to store total IGST Amount
//        Double rowDiscount = 0.0;
//
//        // Calculate totals for each transaction
//        for (CmpTRowDTOSoToSc cmpTRowDTO : tvSCConvEditCMPTRow.getItems()) {
//            taxPercentage = Double.parseDouble(cmpTRowDTO.getTax());
//            Double quantity = Double.parseDouble(cmpTRowDTO.getQuantity());
//            if (cmpTRowDTO.getFree().isEmpty()) {
//                freeQuantity = 0.0;
//            } else {
//                freeQuantity = Double.parseDouble(cmpTRowDTO.getFree());
//            }
//
//            // Total Calculations of each IGST, SGST, CGST
//            totalQuantity += quantity;
//            totalFreeQuantity += freeQuantity;
//            Double igst = Double.valueOf(cmpTRowDTO.getIgst());
//            Double cgst = Double.valueOf(cmpTRowDTO.getCgst());
//            Double sgst = Double.valueOf(cmpTRowDTO.getSgst());
//            totalFinalIgst += igst;
//            totalFinalSgst += sgst;
//            totalFinalCgst += cgst;
//
//            cgstTotals.put(taxPercentage, cgstTotals.get(taxPercentage) + cgst);
//            sgstTotals.put(taxPercentage, sgstTotals.get(taxPercentage) + sgst);
//            igstTotals.put(taxPercentage, sgstTotals.get(taxPercentage) + sgst);
//
//            //Total Calculation of gross amt ,taxable ,tax,discount
//            netAmount = Double.parseDouble(cmpTRowDTO.getNet_amount());
//            totalNetAmt += netAmount;
//            grossAmt = Double.parseDouble(cmpTRowDTO.getGross_amount());
//            totalGrossAmt += grossAmt;
//            taxableAmt = Double.valueOf(cmpTRowDTO.getTaxable_amt());
//            totaltaxableAmt += taxableAmt;
//            disAmt = Double.valueOf(cmpTRowDTO.getFinal_dis_amt());
//            totalDisAmt += disAmt;
//            taxAmt = Double.valueOf(cmpTRowDTO.getFinal_tax_amt());
//            totalTaxAmt += taxAmt;
//        }
//
//
//        // Print or display totals of Each GST percentage Amount in GST Table
//        ObservableList<GstDTO> observableList = FXCollections.observableArrayList();
//        for (Double taxPer : new Double[]{3.0, 5.0, 12.0, 18.0, 28.0}) {
//            double totalCGST = cgstTotals.get(taxPer);
//            double totalSGST = sgstTotals.get(taxPer);
//            double totalIGST = igstTotals.get(taxPer);
//
//            if (totalCGST > 0) {
//                System.out.println("Tax Percentage: " + taxPer + "%");
//                System.out.println("Total CGST: " + totalCGST);
//                System.out.println("Total SGST: " + totalSGST);
//                observableList.add(new GstDTO(String.format("%.2f", taxPer), String.format("%.2f", totalCGST), String.format("%.2f", totalSGST), String.format("%.2f",totalIGST)));
//            }
//            tcSCConvBottomGSTEdit.setCellValueFactory(new PropertyValueFactory<>("taxPer"));
//            tcSCConvBottomCGSTEdit.setCellValueFactory(new PropertyValueFactory<>("cgst"));
//            tcSCConvBottomSGSTEdit.setCellValueFactory(new PropertyValueFactory<>("sgst"));
//            tcSCConvBottomIGSTEdit.setCellValueFactory(new PropertyValueFactory<>("igst"));
//        }
//        tblvSCConvGSTEdit.setItems(observableList);
//        Platform.runLater(() -> {
//            tblvSCConvGSTEdit.refresh();
//
//        });
//        // Lable Set To dispaly Amounts
//        lblSCConvTotalQtyEdit.setText(String.valueOf(totalQuantity));
//        lblSCConvFreeQtyEdit.setText(String.valueOf(totalFreeQuantity));
//        //Display  of gross amt ,taxable ,tax,discount
//        lblSCConvBillAmountEdit.setText(String.format("%.2f", totalNetAmt));
//        lblSCConvGrossTotalEdit.setText(String.format("%.2f", totalGrossAmt));
//        lblSCConvTotalDiscountEdit.setText(String.format("%.2f", totalDisAmt));
//        lblSCConvTotalTaxableAmountEdit.setText(String.format("%.2f", (totalGrossAmt - totalDisAmt + addChargAmount)));
//        lblSCConvTotalTaxEdit.setText(String.format("%.2f", totalTaxAmt));
//        lblSCConvTotalSGSTEdit.setText(String.format("%.2f", totalFinalSgst));
//        lblSCConvTotalCGSTEdit.setText(String.format("%.2f", totalFinalCgst));
//
//    }

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
//                System.out.println("getIndirectExpenseList-->> " + data);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //function for create the sale challan
    public void createSalesChallan() {

        String finalOrerDate = Communicator.text_to_date.fromString(dpSCConvTranxDate.getText()).toString();

        Double cgst = 0.00;
        Double totalCgst = 0.00;
        Double sgst = 0.00;
        Double totalSgst = 0.00;
        Double totalIgst = 0.00;
        //for gst
        JsonArray jsonArray = new JsonArray();
//        for (GstDTO gstdto : tblvSCConvGSTEdit.getItems()) {
//            cgst = Double.valueOf(gstdto.getCgst());
//            totalCgst += cgst;
//            sgst = Double.valueOf(gstdto.getCgst());
//            totalSgst += sgst;
//            totalIgst = totalSgst + totalCgst;
//        }
        /*JSONArray jsonArrIgst = new JSONArray();
        JSONObject taxCal = new JSONObject();
        for (GstDTO gstdto : tblvSCConvGSTEdit.getItems()) {
            cgst = Double.valueOf(gstdto.getCgst());
            totalCgst += cgst;
            sgst = Double.valueOf(gstdto.getSgst());
            totalSgst += sgst;
            totalIgst = totalSgst + totalCgst;
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("gst", gstdto.getIgst());
            jsonObject.put("amt", totalIgst);
            jsonArrIgst.put(jsonObject);
        }
        taxCal.put("igst", jsonArrIgst);
        System.out.println("taxCalll in create API== "+taxCal);
//        map.put("taxCalculation", taxCal.toString());
        map.put("taxCalculation", taxCal.toString());*/
        map.put("bill_dt", finalOrerDate);
        map.put("bill_no", tfSCConvInvNo.getText());
        map.put("sales_acc_id", cmbSCConvSalesAC.getValue().getId());
        map.put("sales_sr_no", tfSCConvChallanSerial.getText());
        map.put("debtors_id", selectedLedgerId);
        map.put("gstNo", cmbSCConvSupplierGST.getValue() != null ? cmbSCConvSupplierGST.getValue().toString() : "");
        map.put("isRoundOffCheck", String.valueOf(chbRoundOffEdit.isSelected()));
        map.put("roundoff", lblSCConvRoundOffEdit.getText() != null && !lblSCConvRoundOffEdit.getText().isEmpty() ? lblSCConvRoundOffEdit.getText() : String.valueOf(0.0));
        map.put("narration", tfSCConvEditNarrations.getText() != null ? tfSCConvEditNarrations.getText() : "");
        map.put("totalamt", lblSCConvBillAmountEdit.getText());
        map.put("taxable_amount", lblSCConvTotalTaxableAmountEdit.getText());
        map.put("tcs", "0");//static
        map.put("sales_discount", !tfSCConvAllRowDisPerEdit.getText().isEmpty() ? tfSCConvAllRowDisPerEdit.getText() : "0");//static              //it is the bottom discount in percentage
        map.put("total_sales_discount_amt", "0");//static     //
        map.put("sales_discount_amt", !tfSCConvAllRowDisAmtEdit.getText().isEmpty() ? tfSCConvAllRowDisAmtEdit.getText() : "0");//static         //it is the discount in Amount at bottom
//        System.out.println("tfSCConvAddChrgTotalAmt-- " + tfSCConvAddChrgTotalAmt.getText() + " AddChargesss " + addChargesDTOList);
        map.put("additionalChargesTotal", !tfSCConvAddChrgTotalAmt.getText().isEmpty() ? tfSCConvAddChrgTotalAmt.getText() : "0");//static
        map.put("additionalCharges", addChargesDTOList.toString());
        map.put("sale_type", "sales_invoice");//static
        map.put("taxFlag", taxFlag.toString());// static data as of now
        map.put("total_qty", String.valueOf(Double.valueOf(lblSCConvTotalQtyEdit.getText()).longValue()));
        map.put("total_free_qty", lblSCConvFreeQtyEdit.getText());//static
        map.put("total_row_gross_amt", lblSCConvGrossTotalEdit.getText());
        map.put("total_base_amt", lblSCConvGrossTotalEdit.getText());
        map.put("total_invoice_dis_amt", lblSCConvTotalDiscountEdit.getText());
        map.put("total_tax_amt", lblSCConvTotalTaxEdit.getText());
        map.put("bill_amount", lblSCConvBillAmountEdit.getText());
        map.put("totalcgst", String.valueOf(totalCgst));
        map.put("totalsgst", String.valueOf(totalSgst));
        map.put("totaligst", String.valueOf(totalIgst));
        map.put("reference", "SLSORD");
        map.put("transactionTrackingNo", trackingNumber);
        map.put("reference_so_id", input);

        List<CmpTRowDTOSoToSc> currentItems = new ArrayList<>(tvSCConvEditCMPTRow.getItems());     //code for if the any cmptRow is blank then it will not take that ROW
//        System.out.println("currentItems--- " + currentItems.size());
        if (!currentItems.isEmpty()) {
            CmpTRowDTOSoToSc lastItem = currentItems.get(currentItems.size() - 1);    //due to this when two orders are selecting of same ledger then only one tranx is closed

            if (lastItem.getParticulars() != null && lastItem.getParticulars().isEmpty()) {
                currentItems.remove(currentItems.size() - 1);
            }
        }
        List<CmpTRowDTOSoToSc> list = new ArrayList<>(currentItems);
        /***** mapping Row Data into TranxPurRowDTO *****/
        ArrayList<TranxPurRowDTO> rowData = new ArrayList<>();
        for (CmpTRowDTOSoToSc cmpTRowDTO : list) {
            TranxPurRowDTO purParticularRow = new TranxPurRowDTO();
            purParticularRow.setProductId(cmpTRowDTO.getProduct_id());
            if (!cmpTRowDTO.getBatch_or_serial().isEmpty() && cmpTRowDTO.getBatch_or_serial() != "0") {
                purParticularRow.setBatch(cmpTRowDTO.getBatch_or_serial());
                purParticularRow.setbDetailsId(cmpTRowDTO.getB_details_id());
                purParticularRow.setIsBatch("true");
            } else {
                purParticularRow.setBatch("");
                purParticularRow.setIsBatch("false");
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
            System.out.println("unitIddd-- " + cmpTRowDTO.getUnit_id() + " unit_nameee " + cmpTRowDTO.getUnit());
            if (!cmpTRowDTO.getUnit_id().isEmpty()) {
                purParticularRow.setUnitId(cmpTRowDTO.getUnit_id());
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
//                System.out.println("gst in create API-- " + cmpTRowDTO.getGst());
                purParticularRow.setGst(String.valueOf(cmpTRowDTO.getGst()));
            } else {
                purParticularRow.setGst("");
            }
            if (cmpTRowDTO.getCgst() != null) {
//                System.out.println("cgst_per in create API-- " + cmpTRowDTO.getCgst_per() + " cgst= " + cmpTRowDTO.getCgst());
                purParticularRow.setCgst(String.valueOf(cmpTRowDTO.getCgst_per()));
            } else {
                purParticularRow.setCgst("");
            }
            if (cmpTRowDTO.getIgst() != null) {
//                System.out.println("igst in create API-- " + cmpTRowDTO.getIgst_per() + " igst= " + cmpTRowDTO.getIgst());
                purParticularRow.setIgst(String.valueOf(cmpTRowDTO.getIgst_per()));
            } else {
                purParticularRow.setIgst("");
            }
            if (cmpTRowDTO.getSgst() != null) {
                purParticularRow.setSgst(String.valueOf(cmpTRowDTO.getSgst_per()));
            } else {
                purParticularRow.setSgst("");
            }

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
                purParticularRow.setFinalAmt("");
            }
//            if (cmpTRowDTO.getSgst() != null) {
//                purParticularRow.setSgst(String.valueOf(cmpTRowDTO.getSgst()));
//            } else {
//                purParticularRow.setSgst("");
//            }
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
//            System.out.println("RowWise refType " + cmpTRowDTO.getReference_type() + " refId " + cmpTRowDTO.getReference_id());
            rowData.add(purParticularRow);
        }

        String mRowData = new Gson().toJson(rowData, new TypeToken<List<TranxPurRowDTO>>() {
        }.getType());
//        System.out.println("mRowData=>" + mRowData);
        map.put("row", mRowData);
        String finalReq = Globals.mapToString(map);
//        System.out.println("FinalReq=> i am in" + finalReq);

        HttpResponse<String> response;
        String formData = Globals.mapToStringforFormData(map);
//        System.out.println("formData" + formData);
        response = APIClient.postFormDataRequest(formData, EndPoints.SALES_CHALLAN_CREATE_ENDPOINT);

        JsonObject responseBody = new Gson().fromJson(response.body(), JsonObject.class);
        System.out.println("Response=>" + responseBody);
        String message = responseBody.get("message").getAsString();

        if (responseBody.get("responseStatus").getAsInt() == 200) {
            AlertUtility.AlertSuccessTimeout(AlertUtility.alertTypeSuccess, message, input -> {
                GlobalController.getInstance().addTabStatic(SALES_CHALLAN_LIST_SLUG, false);
            });

        } else {
            soToscLogger.error("response status is other than 200");
            AlertUtility.AlertErrorTimeout(AlertUtility.alertTypeError, message, in -> {
            });
        }
    }
}  //end of Sales order to challan Main Class

class TextFieldTableCellForSOrderToSChallan extends TableCell<CmpTRowDTOSoToSc, String> {
    private TextField textField;
    private String columnName;
    private TextField button;

    TableCellCallback<Object[]> callback;
    TableCellCallback<Object[]> product_callback;
    TableCellCallback<Object[]> batchID_callback;
    TableCellCallback<String> addRow;
    TableCellCallback<Object[]> addPrdCalbak;

    public TextFieldTableCellForSOrderToSChallan(String columnName, TableCellCallback<Object[]> callback, TextField button) {
        this.columnName = columnName;
        this.textField = new TextField();

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
        quantityColumn();
        freeQtyColumn();
        rateColumn();
        disPerc1Column();
        disAmtColumn();
        netAmountColumn();

    }

    public TextFieldTableCellForSOrderToSChallan(String columnName, TableCellCallback<Object[]> callback, TableCellCallback<Object[]> prod_callback, TextField button, TableCellCallback<Object[]> addPrdCalbak) {
        this.columnName = columnName;
        this.textField = new TextField();
        this.addPrdCalbak = addPrdCalbak;
        this.product_callback = prod_callback;
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
        quantityColumn();
        rateColumn();
        disPerc1Column();
        disAmtColumn();
        netAmountColumn();

    }

    public TextFieldTableCellForSOrderToSChallan(String columnName, TableCellCallback<Object[]> callback, TableCellCallback<Object[]> product_callback,
                                                 TableCellCallback<String> addRow, TableCellCallback<Object[]> batchID_callback) {
        this.columnName = columnName;
        this.textField = new TextField();

        this.product_callback = product_callback;
        this.batchID_callback = batchID_callback;
        this.addRow = addRow;
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

//        batchColumn();

        netAmountColumn();

        batchColumn();
        quantityColumn();
        freeQtyColumn();
        rateColumn();
        disPerc1Column();
        disAmtColumn();


    }


    private void batchColumn() {
        if ("tblcSCConvEditCmptBatch".equals(columnName)) {
            textField.setEditable(false);

            textField.setOnKeyPressed(event -> {
                if (event.getCode() == KeyCode.SPACE) {
                    openBatchWindow();
                } else if (event.isShiftDown() && event.getCode() == KeyCode.TAB) {
                    TableColumn<CmpTRowDTOSoToSc, ?> colName = getTableView().getColumns().get(1);
                    getTableView().edit(getIndex(), colName);
                    event.consume();
                } else if (event.getCode() == KeyCode.ENTER || event.getCode() == KeyCode.TAB) {
                    if (!textField.getText().isEmpty() && !textField.getText().equalsIgnoreCase("0")) {
                        TableColumn<CmpTRowDTOSoToSc, ?> colName = getTableView().getColumns().get(7);
                        getTableView().edit(getIndex(), colName);
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
        if ("tblcSCConvEditCmptParticular".equals(columnName)) {
            textField.setEditable(false);
            textField.setOnKeyPressed(event -> {
                if (event.getCode() == KeyCode.SPACE) {
                    if (textField.getText().isEmpty()) {
                        //Pass productId to ProductPopup for Focus in open ProductPopup on Space in Create if no product selected
                        SingleInputDialogs.productId = "";
                    }
                    openProduct(getIndex());
                }
                if (event.isShiftDown() && event.getCode() == KeyCode.TAB) {
                    if (getIndex() == 1) {
                        TableColumn<CmpTRowDTOSoToSc, ?> colName = getTableView().getColumns().get(13);
                        getTableView().edit(getIndex() - 1, colName);
                    } else {
                        TableColumn<CmpTRowDTOSoToSc, ?> colName = getTableView().getColumns().get(12);
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
                if (textField.getText().isEmpty()) {
                    //Pass productId to ProductPopup for Focus in open ProductPopup on Space in Create if no product selected
                    SingleInputDialogs.productId = "";
                }
                openProduct(getIndex());
            });
        }
    }

    private void quantityColumn() {                //function for calculation when we enter the qty in cmpt row
        if ("tblcSCConvEditCmptQuantity".equals(columnName)) {
            textField.setEditable(true);

            textField.setOnKeyPressed(event -> {
                if(!textField.getText().isEmpty()){
                    CommonValidationsUtils.restrictToNumbers(textField);
                }
                if (event.isShiftDown() && event.getCode() == KeyCode.TAB) {
                    TableColumn<CmpTRowDTOSoToSc, ?> colName = getTableView().getColumns().get(7);
                    getTableView().edit(getIndex(), colName);
                    event.consume();
                } else if (event.getCode() == KeyCode.ENTER || event.getCode() == KeyCode.TAB) {

                    Integer index = getIndex();
/*                    TableColumn<CmpTRowDTOSoToSc, ?> colName = getTableView().getColumns().get(9);
                    getTableView().edit(index, colName);*/
                    //!Check stock available for the selected unit
                    if (!textField.getText().isEmpty()) {
                        int cnt = Globals.getUserControlsWithSlug("is_free_qty") == true ? 9 : 10;
                        boolean isNegetiveAllowed = getTableView().getItems().get(getIndex()).getSelectedProduct().getNegetive();
                        double actStk = getTableView().getItems().get(getIndex()).getUnitWiseactStock();
                        double actqty = Double.valueOf(textField.getText());
                        if (actqty > actStk && isNegetiveAllowed == false) {
                            AlertUtility.AlertError(AlertUtility.alertTypeError, "Out of Stock! Available Stock : " + actStk, input -> {
                            });
//                            getTableRow().getItem().setQuantity("");
                            TableColumn<CmpTRowDTOSoToSc, ?> colName = getTableView().getColumns().get(8);
                            getTableView().edit(index, colName);
                        } else {
                            TableColumn<CmpTRowDTOSoToSc, ?> colName = getTableView().getColumns().get(cnt);
                            getTableView().edit(index, colName);
                        }
                    } else {
                        textField.requestFocus();
                    }

                    SaleOrderToChallanCalculation.rowCalculationForPurcInvoice(index, getTableView(), callback);
                    SaleOrderToChallanCalculation.calculateGst(getTableView(), callback);
                }
            });
        }
    }

    private void freeQtyColumn() {                //function for calculation when we enter the qty in cmpt row
        if ("tblcSCConvEditCmptFreeQuantity".equals(columnName)) {
            textField.setEditable(true);
            textField.setOnKeyPressed(event -> {
                if(!textField.getText().isEmpty()){
                    CommonValidationsUtils.restrictToNumbers(textField);
                }
                if (event.isShiftDown() && event.getCode() == KeyCode.TAB) {
                    TableColumn<CmpTRowDTOSoToSc, ?> colName = getTableView().getColumns().get(8);
                    getTableView().edit(getIndex(), colName);
                    event.consume();
                } else if (event.getCode() == KeyCode.ENTER || event.getCode() == KeyCode.TAB) {
                    int index = getIndex();
                    //!Check stock available for the selected unit
                    if (!textField.getText().isEmpty()) {
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
                    } else {
                        TableColumn<CmpTRowDTOSoToSc, ?> colName = getTableView().getColumns().get(10);
                        getTableView().edit(getIndex(), colName);
//                        textField.requestFocus();
                    }
                    SaleOrderToChallanCalculation.rowCalculationForPurcInvoice(index, getTableView(), callback);
                    SaleOrderToChallanCalculation.calculateGst(getTableView(), callback);
                }
            });
        }
    }

    private void rateColumn() {                //function for calculation when we enter the qty in cmpt row
        if ("tblcSCConvEditCmptRate".equals(columnName)) {
            textField.setEditable(true);
            textField.setOnKeyPressed(event -> {
                if(!textField.getText().isEmpty()){
                    CommonValidationsUtils.restrictToDecimalNumbers(textField);
                }
                if (event.isShiftDown() && event.getCode() == KeyCode.TAB) {
                    int cnt = Globals.getUserControlsWithSlug("is_free_qty") == true ? 9 : 8;
                    TableColumn<CmpTRowDTOSoToSc, ?> colName = getTableView().getColumns().get(cnt);
                    getTableView().edit(getIndex(), colName);
                    event.consume();
                } else if (event.getCode() == KeyCode.ENTER || event.getCode() == KeyCode.TAB) {
                    Integer index = getIndex();
                    TableColumn<CmpTRowDTOSoToSc, ?> colName = getTableView().getColumns().get(12);
                    getTableView().edit(index, colName);
                    SaleOrderToChallanCalculation.rowCalculationForPurcInvoice(index, getTableView(), callback);
                    SaleOrderToChallanCalculation.calculateGst(getTableView(), callback);
                }
            });
        }
    }

    private void disPerc1Column() {                //function for calculation when we enter the qty in cmpt row
        if ("tblcSCConvEditCmptDiscPerc".equals(columnName)) {
            textField.setEditable(true);

            textField.setOnKeyPressed(event -> {
                if(!textField.getText().isEmpty()){
                    CommonValidationsUtils.restrictToDecimalNumbers(textField);
                }
                if (event.isShiftDown() && event.getCode() == KeyCode.TAB) {
                    TableColumn<CmpTRowDTOSoToSc, ?> colName = getTableView().getColumns().get(10);
                    getTableView().edit(getIndex(), colName);
                } else if (event.getCode() == KeyCode.ENTER || event.getCode() == KeyCode.TAB) {
                    Integer index = getIndex();

                    TableColumn<CmpTRowDTOSoToSc, ?> colName = getTableView().getColumns().get(13);
                    getTableView().edit(index, colName);
                    SaleOrderToChallanCalculation.rowCalculationForPurcInvoice(index, getTableView(), callback);
                    SaleOrderToChallanCalculation.calculateGst(getTableView(), callback);
                }
            });
        }
    }

    private void disAmtColumn() {                //function for calculation when we enter the qty in cmpt row
        if ("tblcSCConvEditCmptDiscAmt".equals(columnName)) {
            textField.setEditable(true);

            textField.setOnKeyPressed(event -> {
                if(!textField.getText().isEmpty()){
                    CommonValidationsUtils.restrictToDecimalNumbers(textField);
                }
                if (event.isShiftDown() && event.getCode() == KeyCode.TAB) {
                    TableColumn<CmpTRowDTOSoToSc, ?> colName = getTableView().getColumns().get(12);
                    getTableView().edit(getIndex(), colName);
                    event.consume();
                } else if (event.getCode() == KeyCode.ENTER || event.getCode() == KeyCode.TAB) {
                    int index = getIndex();
                    if ((getTableView().getItems().size() - 1) == index) {
                        getTableView().getItems().addAll(new CmpTRowDTOSoToSc("", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", ""));
                    }
                    TableColumn<CmpTRowDTOSoToSc, ?> colName = getTableView().getColumns().get(1);
                    getTableView().edit(getIndex() + 1, colName);

                    SaleOrderToChallanCalculation.rowCalculationForPurcInvoice(index, getTableView(), callback);
                    SaleOrderToChallanCalculation.calculateGst(getTableView(), callback);
                }
            });
        }
    }

    private void netAmountColumn() {

        if ("tblcSCConvEditCmptNetAmt".equals(columnName)) {
            textField.setEditable(false);
            textField.setOnKeyPressed(event -> {
                if (event.getCode() == KeyCode.ENTER) {

                    int current_index = getTableRow().getIndex();

                    /*                    getTableView().getItems().addAll(new CmpTRowDTOSoToSc("", String.valueOf(current_index + 1), "1", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", ""));*/
                }
            });
        }
    }

    public void openBatchWindow() {
        CmpTRowDTOSoToSc selectedRow = getTableView().getItems().get(getIndex());

        if (Boolean.valueOf(selectedRow.getIs_batch()) == true) {
            TranxCommonPopUps.openBatchPopUpSales(Communicator.stage, selectedRow, Communicator.tranxDate, "Batch", input -> {
                if (input != null) {
                    selectedRow.setSelectedBatch(input);
                    selectedRow.setB_details_id("" + input.getId());
                    selectedRow.setB_no(input.getBatchNo());
                    selectedRow.setBatch_or_serial(input.getBatchNo());

//                    if (product_callback != null) {
//                        Object[] object = new Object[2];
//                        object[0] = "" + input.getBatchNo();
//                        object[1] = "" + input.getId();
//                        product_callback.call(object);
//                    }
                    if (batchID_callback != null) {
                        Object[] objects = new Object[2];
                        objects[0] = input.getBatchNo();
                        objects[1] = String.valueOf(input.getId());
                        batchID_callback.call(objects);
                    }
                }
            }, isNewBatch -> {
                System.out.println("isNewBatch" + isNewBatch);

            });

        } else {
            selectedRow.setBatch_or_serial("#");
            selectedRow.setB_no("#");
        }
        SaleOrderToChallanCalculation.rowCalculationForPurcInvoice(getIndex(), getTableView(), callback);
        SaleOrderToChallanCalculation.calculateGst(getTableView(), callback);

        TableColumn<CmpTRowDTOSoToSc, ?> colName = getTableView().getColumns().get(6);

        getTableView().edit(getIndex(), colName);
//        getTableView().getSelectionModel().select(getIndex(),colName);
    }

    private void openProduct(int currentIndex) {
        CmpTRowDTOSoToSc selectedRow = getTableView().getItems().get(currentIndex);
        TranxCommonPopUps.openProductPopUp(Communicator.stage, Integer.valueOf(selectedRow.getProduct_id()), "Product", input -> {
            if (input != null) {
                getTableRow().getItem().setParticulars(input.getProductName());
                getTableRow().getItem().setProduct_id(input.getProductId().toString());
                getTableRow().getItem().setPackages(input.getPackageName());
                getTableRow().getItem().setIs_batch(input.getBatch().toString());
                getTableRow().getItem().setTax(input.getIgst().toString());
//                getTableRow().getItem().setUnit(input.get());
                getTableRow().getItem().setSelectedProduct(input);
                getTableRow().getItem().setRate(input.getSalesRate().toString());
                if (product_callback != null) {
                    Object[] object = new Object[2];
                    object[0] = input.getProductId();
                    product_callback.call(object);
                }
                if (input.getUnit1ClsStock().equals(0.0)) {

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
                    TableColumn<CmpTRowDTOSoToSc, ?> colName = getTableView().getColumns().get(1);
                    getTableView().edit(getIndex(), colName);
                }
            }
        }, item -> {
            if (item) {
                if (addPrdCalbak != null) {
//                    System.out.println("hello Add product Called");
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


    public void textfieldStyle() {
        if (columnName.equals("tblcSCConvEditCmptParticular")) {
            textField.setPromptText("Particulars");
            textField.setEditable(false);
        } else {
            textField.setPromptText("0");
        }


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
        if (item != null && columnName.equals("tblcSCConvEditCmptParticular")) {
            ((CmpTRowDTOSoToSc) item).setParticulars(newValue.isEmpty() ? "" : newValue);
        } else if (columnName.equals("tcSCConvEditSupplierBatch")) {
            (getTableRow().getItem()).setBatch_or_serial(newValue.isEmpty() ? "" : newValue);
        } else if (columnName.equals("tblcSCConvEditCmptQuantity")) {
            (getTableRow().getItem()).setQuantity(newValue.isEmpty() ? "0" : newValue);
        } else if (columnName.equals("tblcSCConvEditCmptRate")) {
            (getTableRow().getItem()).setRate(newValue.isEmpty() ? "0.0" : newValue);
        } else if (columnName.equals("tblcSCConvEditCmptGrossAmt")) {
            (getTableRow().getItem()).setGross_amount(newValue.isEmpty() ? "0.0" : newValue);
        } else if (columnName.equals("tblcSCConvEditCmptDiscPerc")) {
            (getTableRow().getItem()).setDis_per(newValue.isEmpty() ? "0.0" : newValue);
        } else if (columnName.equals("tblcSCConvEditCmptDiscAmt")) {
            (getTableRow().getItem()).setDis_amt(newValue.isEmpty() ? "0.0" : newValue);
        } else if (columnName.equals("tblcSCConvEditCmptTaxPerc")) {
            (getTableRow().getItem()).setTax(newValue.isEmpty() ? "0.0" : newValue);
        } else if (columnName.equals("tblcSCConvEditCmptNetAmt")) {
            (getTableRow().getItem()).setNet_amount(newValue.isEmpty() ? "0.0" : newValue);
        }

    }
}

class ComboBoxTableCellForLevelASoToSc extends TableCell<CmpTRowDTOSoToSc, String> {

    String columnName;
    private final ComboBox<String> comboBox;
    ObservableList<LevelAForPurInvoice> comboList = null;

    public ComboBoxTableCellForLevelASoToSc(String columnName) {

        this.columnName = columnName;
        this.comboBox = new ComboBox<>();

        this.comboBox.setPromptText("Select");

        /*comboBox.setPrefHeight(38);
        comboBox.setMaxHeight(38);
        comboBox.setMinHeight(38);*/

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

//        System.out.println("inside comboAction --- LEVEL-A");

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
                            getTableRow().getItem().setLevelB(levelAForPurInvoice.getLevelBOpts().get(0).getLabel()); //to be uncomment

                        } else {
                            PurInvoiceCommunicator.levelBForPurInvoiceObservableList.add(observableLevelAList);
                            getTableRow().getItem().setLevelB(null);                                               //to be uncomment
                            getTableRow().getItem().setLevelB(levelAForPurInvoice.getLevelBOpts().get(0).getLabel());   //to be uncomment

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

//            System.out.println("level A list : " + PurInvoiceCommunicator.levelAForPurInvoiceObservableList);

            comboBox.getItems().clear();

            if (item != null) {

                comboBox.setValue(item);
                Platform.runLater(() -> {
                    int i = getTableRow().getIndex();

                    if (i >= 0 && i < PurInvoiceCommunicator.levelAForPurInvoiceObservableList.size()) {
                        for (LevelAForPurInvoice levelAForPurInvoice : PurInvoiceCommunicator.levelAForPurInvoiceObservableList.get(i)) {

                            comboBox.getItems().add(levelAForPurInvoice.getLabel());
                        }
                        String itemToSet = item;
                        if (comboBox.getItems().contains(itemToSet)) {
                            comboBox.setValue(itemToSet);
                            comBoAction();
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

class ComboBoxTableCellForLevelBSoToSc extends TableCell<CmpTRowDTOSoToSc, String> {

    String columnName;
    private final ComboBox<String> comboBox;
    ObservableList<LevelBForPurInvoice> comboList = null;

    public ComboBoxTableCellForLevelBSoToSc(String columnName) {
        this.columnName = columnName;
        this.comboBox = new ComboBox<>();

        this.comboBox.setPromptText("Select");

        /*comboBox.setPrefHeight(38);
        comboBox.setMaxHeight(38);
        comboBox.setMinHeight(38);*/

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
//            System.out.println("LEVEL-B COMBO ACTION");
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

//            System.out.println("level B list : " + PurInvoiceCommunicator.levelBForPurInvoiceObservableList);

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

class ComboBoxTableCellForLevelCSoToSc extends TableCell<CmpTRowDTOSoToSc, String> {

    String columnName;
    private final ComboBox<String> comboBox;
    ObservableList<LevelAForPurInvoice> comboList = null;

    public ComboBoxTableCellForLevelCSoToSc(String columnName) {


        this.columnName = columnName;
        this.comboBox = new ComboBox<>();

        this.comboBox.setPromptText("Select");

        /*comboBox.setPrefHeight(38);
        comboBox.setMaxHeight(38);
        comboBox.setMinHeight(38);*/

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
//            System.out.println("LEVEL-C COMBO ACTION");
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

class ComboBoxTableCellForUnitSoToSc extends TableCell<CmpTRowDTOSoToSc, String> {

    String columnName;
    private final ComboBox<String> comboBox;
    TableCellCallback<Integer> unit_callback;//new

    public ComboBoxTableCellForUnitSoToSc(String columnName, TableCellCallback<Integer> unit_callback) {


        this.columnName = columnName;
        this.comboBox = new ComboBox<>();
        this.unit_callback = unit_callback;
        this.comboBox.setPromptText("Select");

       /* comboBox.setPrefHeight(38);
        comboBox.setMaxHeight(38);
        comboBox.setMinHeight(38);*/
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
                this.comboBox.setStyle("-fx-background-color: #fff9c4; -fx-border-radius: 0px; -fx-border-width: 1.5; -fx-border-color: #00a0f5;");
                comboBox.show();
            } else {
                this.comboBox.setStyle("-fx-background-color: #f6f6f9; -fx-border-radius: 0px; -fx-border-width: 1.5; -fx-border-color: #f6f6f9;");
            }
        });


        this.comboBox.setFocusTraversable(true);
//        AutoCompleteBox autoCompleteBox1 = new AutoCompleteBox<>(this.comboBox, -1);

        this.comboBox.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal) {
                commitEdit(String.valueOf(comboBox.getValue()));
            }
        });

        comBoAction();

    }

    public void comBoAction() {                        //from sale challan
        comboBox.setOnAction(e -> {
//            System.out.println("hello ComboAction Called");
            String selectedItem = comboBox.getSelectionModel().getSelectedItem();
//            System.out.println("Selected Unit:" + selectedItem);
            if (selectedItem != null) {
                if (PurInvoiceCommunicator.unitForPurInvoiceList != null && !PurInvoiceCommunicator.unitForPurInvoiceList.isEmpty()) {

                    for (UnitForPurInvoice unitForPurInvoice : PurInvoiceCommunicator.unitForPurInvoiceList.get(getTableRow().getIndex())) {
                        if (selectedItem.equals(unitForPurInvoice.getLabel())) {
                            int index = getTableRow().getIndex();
                            getTableRow().getItem().setUnit_id(unitForPurInvoice.getValue());
                            getTableRow().getItem().setUnit_conv(String.valueOf(unitForPurInvoice.getUnitConversion()));
                            if (this.unit_callback != null) {
                                this.unit_callback.call(getIndex());
                            }

                        }
                    }
                }
            }
        });

        comboBox.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.SPACE) {
                comboBox.show();
            } else if (event.isShiftDown() && event.getCode() == KeyCode.TAB) {
                TableColumn<CmpTRowDTOSoToSc, ?> colName = getTableView().getColumns().get(6);
                getTableView().edit(getIndex(), colName);
                event.consume();
            } else if (event.getCode() == KeyCode.ENTER || !event.isShiftDown() && event.getCode() == KeyCode.TAB) {
                TableColumn<CmpTRowDTOSoToSc, ?> colName = getTableView().getColumns().get(8);
//                System.out.println("Col name:" + colName.getText());
                getTableView().edit(getIndex(), colName);
                event.consume();
            }
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

//            HBox hbox = new HBox();
//            hbox.getChildren().addAll(comboBox);
//            hbox.setSpacing(5);

            setGraphic(comboBox);
        }
    }

    @Override
    public void commitEdit(String newValue) {
        super.commitEdit(newValue);
        (getTableRow().getItem()).setUnit(newValue);
    }

}

class ButtonTableCellSoToSc extends TableCell<CmpTRowDTOSoToSc, String> {
    private Button delete;

    public ButtonTableCellSoToSc() {

        this.delete = createButtonWithImage();

        delete.setOnAction(actionEvent -> {
            CmpTRowDTOSoToSc table = getTableView().getItems().get(getIndex());
            getTableView().getItems().remove(table);
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

class ProductUnitsPackingSoToSc {

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
            e.printStackTrace();
        }


        return levelAForPurInvoicesList;
    }

}

class SaleOrderToChallanCalculation {

    public static void rowCalculationForPurcInvoice(int rowIndex, TableView<CmpTRowDTOSoToSc> tableView, TableCellCallback<Object[]> callback) {
        CmpTRowDTOSoToSc purchaseInvoiceTable = tableView.getItems().get(rowIndex);
//        if(!purchaseInvoiceTable.getQuantity().isEmpty() && Double.parseDouble(purchaseInvoiceTable.getQuantity()) > 0 &&
//            !purchaseInvoiceTable.getRate().isEmpty() && Double.parseDouble(purchaseInvoiceTable.getRate()) > 0){
//
//        }
        String purchaseInvoiceTableData = purchaseInvoiceTable.getRate();
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
            CmpTRowDTOSoToSc selectedItem = tableView.getItems().get(rowIndex);
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

    public static void calculateGst(TableView<CmpTRowDTOSoToSc> tableView, TableCellCallback<Object[]> callback) {


        Map<Double, Double> cgstTotals = new HashMap<>(); //used to merge same cgst Percentage
        Map<Double, Double> sgstTotals = new HashMap<>();//used to merge same sgst Percentage
        Map<Double, Double> igstTotals = new HashMap<>();//used to merge same sgst Percentage
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
        for (CmpTRowDTOSoToSc purchaseInvoiceTable : tableView.getItems()) {
            if (!purchaseInvoiceTable.getQuantity().isEmpty() && !purchaseInvoiceTable.getRate().isEmpty()) {
                taxPercentage = Double.parseDouble(purchaseInvoiceTable.getTax());
//                System.out.println("taxPercentage -> " + taxPercentage);
                Double quantity = Double.parseDouble(purchaseInvoiceTable.getQuantity());
                if (purchaseInvoiceTable.getFree().isEmpty()) {
                    freeQuantity = 0.0;
                } else {
                    freeQuantity = Double.parseDouble(purchaseInvoiceTable.getFree());
                }

                // Total Calculations of each IGST, SGST, CGST
                totalQuantity += quantity;
                totalFreeQuantity += freeQuantity;
                System.out.println("getIgst()= " + purchaseInvoiceTable.getIgst() + " getCgst= " + purchaseInvoiceTable.getCgst() + " SGST= " + purchaseInvoiceTable.getSgst());

                Double igst = Double.parseDouble(purchaseInvoiceTable.getIgst());
                Double cgst = Double.parseDouble(purchaseInvoiceTable.getCgst());
                Double sgst = Double.parseDouble(purchaseInvoiceTable.getSgst());
                totalFinalIgst += igst;
                totalFinalSgst += sgst;
                totalFinalCgst += cgst;
//                System.out.println("taxPercentage purchaseInvoiceTable" + purchaseInvoiceTable.getIgst());
//                System.out.println("taxPercentage purchaseInvoiceTable" + purchaseInvoiceTable.getCgst());
//                System.out.println("taxPercentage purchaseInvoiceTable" + purchaseInvoiceTable.getSgst());
//                System.out.println("taxPercentage purchaseInvoiceTable" + cgstTotals);
//                System.out.println("taxPercentage purchaseInvoiceTable" + sgstTotals);
//                System.out.println("taxPercentage purchaseInvoiceTable" + igstTotals);

                cgstTotals.put(taxPercentage, cgstTotals.get(taxPercentage) + cgst); //0.0
                sgstTotals.put(taxPercentage, sgstTotals.get(taxPercentage) + sgst);
                igstTotals.put(taxPercentage, igstTotals.get(taxPercentage) + igst);

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
//                System.out.println("Tax Percentage: " + taxPer + "%");
//                System.out.println("Total CGST: " + totalCGST);
//                System.out.println("Total SGST: " + totalSGST);
//                System.out.println("Total IGST: " + totalIGST);

                observableList.add(new GstDTO(GlobalTranx.setDecimalFromStr( taxPer), GlobalTranx.setDecimalFromStr( totalCGST), GlobalTranx.setDecimalFromStr( totalSGST), GlobalTranx.setDecimalFromStr( totalIGST)));

            }
        }

        if (callback != null) {

            Object[] object = new Object[10];

            object[0] = GlobalTranx.setQtyFormat(totalQuantity);

            object[1] = GlobalTranx.setQtyFormat(totalFreeQuantity);

            object[2] = GlobalTranx.setDecimalFromStr( totalNetAmt);

            object[3] = GlobalTranx.setDecimalFromStr( totalGrossAmt);

            object[4] = GlobalTranx.setDecimalFromStr( totalDisAmt);

            //  lblPurChallTotalTaxableAmount.setText(GlobalTranx.setDecimalFromStr( (totalGrossAmt - totalDisAmt + addChargAmount)));
            object[5] = GlobalTranx.setDecimalFromStr( (totalGrossAmt - totalDisAmt + 0.0));

            object[6] = GlobalTranx.setDecimalFromStr( totalTaxAmt);

            object[7] = GlobalTranx.setDecimalFromStr( totalFinalSgst);

            object[8] = GlobalTranx.setDecimalFromStr( totalFinalCgst);

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
            CmpTRowDTOSoToSc purchaseInvoiceTable = tableView.getItems().get(i);
            totalTaxableAmt = totalTaxableAmt + Double.parseDouble(purchaseInvoiceTable.getTaxable_amt());
        }
        System.out.println("totalTaxableAmt discountPropotionalCalculation :: " + totalTaxableAmt);

        Double rowDisc = 0.0;
        //get data of netamount, tax per,and row taxable amount from cmptrow
        for (CmpTRowDTOSoToSc purchaseInvoiceTable : tableView.getItems()) {
            if (!purchaseInvoiceTable.getQuantity().isEmpty() && Double.parseDouble(purchaseInvoiceTable.getQuantity()) > 0 &&
                    !purchaseInvoiceTable.getRate().isEmpty() && Double.parseDouble(purchaseInvoiceTable.getRate()) > 0) {
//                System.out.println("getFinal_dis_amt--->" + purchaseInvoiceTable.getFinal_dis_amt());
                rowDisc = purchaseInvoiceTable.getFinal_dis_amt().isEmpty() ? 0.0 : Double.parseDouble(purchaseInvoiceTable.getFinal_dis_amt());
//                System.out.println("netAmt--->" + purchaseInvoiceTable.getNet_amount());
                netAmt = purchaseInvoiceTable.getNet_amount().isEmpty() ? 0.0 : Double.parseDouble(purchaseInvoiceTable.getNet_amount());
//                System.out.println("netAmt--->" + purchaseInvoiceTable.getTax());
                r_tax_per = Double.parseDouble(purchaseInvoiceTable.getTax());
                rowTaxableAmt = Double.parseDouble(purchaseInvoiceTable.getTaxable_amt());
//                System.out.println("rowTaxableAmt discountPropotionalCalculation :: " + rowTaxableAmt + "netAmt" + netAmt);

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
                purchaseInvoiceTable.setNet_amount(GlobalTranx.setDecimalFromStr( netAmt));
                purchaseInvoiceTable.setTotal_taxable_amt(GlobalTranx.setDecimalFromStr(totalTaxableAmtAdditional));
                purchaseInvoiceTable.setIgst(GlobalTranx.setDecimalFromStr(total_tax_amt));
                purchaseInvoiceTable.setCgst(GlobalTranx.setDecimalFromStr(total_tax_amt / 2));
                purchaseInvoiceTable.setSgst(GlobalTranx.setDecimalFromStr(total_tax_amt / 2));
                purchaseInvoiceTable.setFinal_tax_amt(GlobalTranx.setDecimalFromStr(total_tax_amt));
                purchaseInvoiceTable.setFinal_dis_amt(GlobalTranx.setDecimalFromStr(rowDisc));
                purchaseInvoiceTable.setInvoice_dis_amt(GlobalTranx.setDecimalFromStr(rowDisPropAmt));
            }


        }
        //Check additional charge is Empty or not if not empty call to the additional charges function
        if (!additionalCharges.isEmpty()) {
            //additionalChargesCalculation();
        }

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
                CmpTRowDTOSoToSc purchaseInvoiceTable = tableView.getItems().get(i);
                totalTaxableAmt = totalTaxableAmt + Double.parseDouble(purchaseInvoiceTable.getTotal_taxable_amt());

            }
            for (CmpTRowDTOSoToSc purchaseInvoiceTable : tableView.getItems()) {

                System.out.println("row ->" + purchaseInvoiceTable.toString());
                netAmt = Double.parseDouble(purchaseInvoiceTable.getNet_amount());
                r_tax_per = Double.parseDouble(purchaseInvoiceTable.getTax());
                rowTaxableAmt = Double.parseDouble(purchaseInvoiceTable.getTotal_taxable_amt());

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


