package com.opethic.genivis.controller.tranx_purchase;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.opethic.genivis.GenivisApplication;
import com.opethic.genivis.controller.dialogs.SingleInputDialogs;
import com.opethic.genivis.controller.master.ledger.common.LedgerMessageConsts;
import com.opethic.genivis.dto.*;
import com.opethic.genivis.dto.pur_invoice.*;
import com.opethic.genivis.dto.reqres.product.Communicator;
import com.opethic.genivis.dto.reqres.product.TableCellCallback;
import com.opethic.genivis.dto.reqres.pur_tranx.PurTranxToProductRedirectionDTO;
import com.opethic.genivis.dto.reqres.pur_tranx.PurchaseOrderResponse;
import com.opethic.genivis.dto.reqres.pur_tranx.PurchaseOrderRow;
import com.opethic.genivis.dto.reqres.pur_tranx.TranxPurRowDTO;
import com.opethic.genivis.network.APIClient;
import com.opethic.genivis.network.EndPoints;
import com.opethic.genivis.network.RequestType;
import com.opethic.genivis.utils.*;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.WorkerStateEvent;
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
import javafx.scene.text.Text;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import org.apache.http.util.TextUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.URL;
import java.net.http.HttpResponse;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static com.opethic.genivis.utils.FxmFileConstants.PURCHASE_ORDER_LIST_SLUG;
import static com.opethic.genivis.utils.Globals.decimalFormat;
import static com.opethic.genivis.utils.Globals.mapToStringforFormData;

public class PurchaseOrderController implements Initializable {

    @FXML
    ComboBox<CommonDTO> cmbPurOrderPurchaAc;
    private JsonObject jsonObject = null;
    private static final Logger purchaseOrderLogger = LogManager.getLogger(PurchaseOrderController.class);
    @FXML
    private VBox totalmainDiv;
    @FXML
    private HBox topInnerHbOne, topInnerHbTwo, poBottomMain;
    @FXML
    private VBox poTotalMainInnerDiv, poBottomFirstV, poBottomSecondV, poTotalMainDiv;

    @FXML
    private BorderPane bpPurOrderRootPane;
    private String ledgerName;
    private String ledgerId;
    private String ledgerStateCode = "";
    private Boolean taxFlag = false;
    private String productId;
    private final String id = "";
    private final String finalroundOff = "";
    @FXML
    Button addRowInCmpTRow;
    @FXML
    Label lblPurOrderGrandTotal, lblPurOrderDiscount, lblPurOrderTotal, lblPurOrderTax, lblPurOrderBillAmount;
    @FXML
    private TableView<SalesOrderSupplierDetailsDTO> tvPurOrderSupplierDetails;
    @FXML
    private TableColumn<SalesOrderSupplierDetailsDTO, String> tcPurOrderSupplerName, tcPurOrderSupplerInvNo, tcPurOrderSupplerInvDate, tcPurOrderSupplerBatch, tcPurOrderSupplerMrp, tcPurOrderSupplerQty, tcPurOrderSupplerRate, tcPurOrderSupplerCost, tcPurOrderSupplerDisPer, tcPurOrderSupplerDisRs;
    @FXML
    public TableView tblPurchaseOrderHistory, tblvPurOrderLedWindtableview, tblvPurOrderPrdWindtableview;
    @FXML
    TableColumn tblcPurOrderLedWindCode, tblcPurOrderLedWindLedName, tblcPurOrderLedWindGrp, tblcPurOrderLedWindContNo, tblcPurOrderLedWindCurrBal, tblcPurOrderLedWindType, tblcPurOrderLedWindAction, tblcPurOrderPrdWindCode, tblcPurOrderPrdWindPrdName, tblcPurOrderPrdWindPacking, tblcPurOrderPrdWindBarcode, tblcPurOrderPrdWindBrand, tblcPurOrderPrdWindMrp, tblcPurOrderPrdWindCurrStk, tblcPurOrderPrdWindUnit, tblcPurOrderPrdWindSaleRate;
    @FXML
    TableView<PurchaseOrderTable> tblvPurOrderCmpTRow;
    @FXML
    TableColumn<PurchaseOrderTable, String> tblcPurOrderCmpTRowSrNo, tblcPurOrderCmpTRowParticulars, tblcPurOrderCmpTRowPacking, tblcPurOrderCmpTRowUnit, tblcPurOrderCmpTRowQuantity, tblcPurOrderCmpTRowFreeQty, tblcPurOrderCmpTRowRate, tblcPurOrderCmpTRowGrossAmt, tblcPurOrderCmpTRowDisPer, tblcPurOrderCmpTRowDisAmt, tblcPurOrderCmpTRowTaxPer, tblcPurOrderCmpTRowNetAmt, tblcPurOrderCmpTRowAction, tblcPurOrderCmpTRowA, tblcPurOrderCmpTRowB, tblcPurOrderCmpTRowC;
    @FXML
    CheckBox cbPurOrderIscreditors;
    @FXML
    private HBox hbapPurOrdLedgerWindow, hbPurOrderProductWindow;
    @FXML
    private TextField tfPurOrdLedgerName, tfPurOrdPurchaseSerial, tfPurOrdOrderNo, tfPurOrdNarration;
    @FXML
    private Button btnPurOrderSubmit, btnPurOrderCancel, btnPurOrderModify;
    @FXML
    DatePicker dpPurOrderTranxDate, dpPurOrdOrderDate;
    @FXML
    private TextField tfPurOrderTranxDate, tfPurOrderOrderDate;
    String invalidDate = "";
    @FXML
    ComboBox cmbPurOrderSupplierGST;
    private final String purOrderProductId = "";
    private static int purOrderRowIndexParticular;
    private String selectedRowPrdId;
    private String responseBody, message;
    @FXML
    TabPane tranxPurOrderTabPane;
    @FXML
    Tab tabPurOrderLedger, tabPurOrderProduct;

    private final ObservableList<CommonDTO> supplierGSTINList = FXCollections.observableArrayList();
    @FXML
    Text txtPurOrderGstNo, txtPurOrderArea, txtPurOrderBank, txtPurOrderContactPerson, txtPurOrderTransportName, txtPurOrderCreditDays, txtPurOrderFssai, txtPurOrderLicenseNo, txtPurOrderRoute, txtPurOrderProductBrand, txtPurOrderProductGroup, txtPurOrderProductCategory, txtPurOrderProductSubGroup, txtPurOrderProductHsn, txtPurOrderProductTaxType, txtPurOrderProductTaxPer, txtPurOrderProductMarginPer, txtPurOrderProductCost, txtPurOrderProductShelfId, txtPurOrderProductMinStock, txtPurOrderProductMaxStock;
    private static final ObservableList<String> unitList = FXCollections.observableArrayList();

    private Integer purOrderEditId = -1;

    Long stateCode = 0L, CompanyStateCode = 0L;
    public static Integer selectedRowIndex;

    private List<Integer> delIds = new ArrayList<>();
    private JsonArray deletedRows = new JsonArray();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        tblvPurOrderCmpTRow.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tvPurOrderSupplierDetails.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tblvPurOrderCmpTRow.setFocusTraversable(false);
        tvPurOrderSupplierDetails.setFocusTraversable(false);

        //todo : autofocus on TranxDate
        Platform.runLater(() -> tfPurOrdLedgerName.requestFocus());


        showDropDownOnKeyPress(cmbPurOrderSupplierGST);
        showDropDownOnKeyPress(cmbPurOrderPurchaAc);


        // Enter Functionality and Shortcut Key
        initialEnterMethod();

        // LedgerName Validation
        tfLedgerNameValidation();

        tfOrderNoValidatition();

        //confirmation popup if invoice date is less than current date
        CommonFunctionalUtils.tranxDateGreaterThanCurrentDate1(tfPurOrderOrderDate, cmbPurOrderPurchaAc);
//        validateOrderDate();

        tfPurOrdLedgerName.setOnMouseClicked(event -> {
            tfPurOrdLedgerName.setText("");
            handleTfLedgerName();
        });

        tfPurOrdLedgerName.setOnKeyTyped(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                tfPurOrdLedgerName.setText("");
                handleTfLedgerName();
            }
        });

        btnPurOrderSubmit.setOnAction(event -> {
            String btnText = btnPurOrderSubmit.getText();
            String msg = LedgerMessageConsts.msgConfirmationOnSubmit;
            if (btnText.equalsIgnoreCase("Update"))
                msg = LedgerMessageConsts.msgConfirmationOnUpdate;
            AlertUtility.AlertConfirmation(AlertUtility.alertTypeConfirmation, msg + tfPurOrdOrderNo.getText() + LedgerMessageConsts.msgInvoiceNumber, input -> {
                if (input == 1) {
                    if (purOrderEditId < 0) {
                        purchaseOrderCreateFun();
                    } else {
                        purchaseOrderUpdateFun();
                    }
                }
            });
        });
       /* btnPurOrderSubmit.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                if (keyEvent.getCode() == KeyCode.ENTER) {
                    String btnText = btnPurOrderSubmit.getText();
                    String msg = "Do you want to create ";
                    if (btnText.equalsIgnoreCase("Update"))
                        msg = "Do you want to update";
                    AlertUtility.AlertConfirmation(AlertUtility.alertTypeConfirmation, msg + tfPurOrdOrderNo.getText() + " Invoice Number?", input -> {
                        if (input == 1) {
                            if (purOrderEditId < 0) {
                                purchaseOrderCreateFun();
                            } else {
                                purchaseOrderUpdateFun();
                            }
                        }
                    });
                }
            }
        });*/

        //Set default value to current local date
        DateValidator.applyDateFormat(tfPurOrderTranxDate);
        DateValidator.applyDateFormat(tfPurOrderOrderDate);
        LocalDate tranxDate = LocalDate.now();
        LocalDate orderDate = LocalDate.now();
        tfPurOrderTranxDate.setText(tranxDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        tfPurOrderOrderDate.setText(orderDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        tfPurOrderTranxDate.setEditable(false);
        tfPurOrderTranxDate.setFocusTraversable(false);
        //DatePicker to TextField with validation

        sceneInitilization();

        //get the purchase accounts
        getPurchaseAccounts();


        //*** To get the purchase serial number ***
        getPurchaseSerialNo();
        tfPurOrdPurchaseSerial.setEditable(false);
        tfPurOrdPurchaseSerial.setFocusTraversable(false);

        tableInitiliazation();

        responsiveCmpTable();


        ResponsiveWiseCssPicker();

        btnPurOrderCancel.setOnAction(actionEvent -> {
            AlertUtility.AlertConfirmation(AlertUtility.alertTypeConfirmation, LedgerMessageConsts.msgGoList, input -> {
                if (input == 1) {
                    if (purOrderEditId < 0) {
                        //orderCreate
                        System.out.println("cancle on create");
                        clearLedAndProdInfo();
                        initialize(url, resourceBundle);
                    } else {
                        //OrderEdit
                        System.out.println("cancle on edit");
                        GlobalController.getInstance().addTabStatic(PURCHASE_ORDER_LIST_SLUG, false);
                        PurchaseOrderListController.focusStatus = "edit";
                        PurchaseOrderListController.selectedRowIndex = selectedRowIndex;

                    }
                }
            });
        });

        btnPurOrderModify.setOnAction(actionEvent -> {
            AlertUtility.AlertConfirmation(AlertUtility.alertTypeConfirmation, LedgerMessageConsts.msgGoList, input -> {
                if (input == 1) {
                    GlobalController.getInstance().addTabStatic(PURCHASE_ORDER_LIST_SLUG, false);
                    PurchaseOrderListController.focusStatus = "create";
                } else {
                }
            });
        });
    }

    private Integer rediCurrIndex = -1;
    private Boolean isProductRed = false;
    private Boolean isLedgerRed = false;

    public void setPurChallDataToProduct() {

        System.out.println("purOrderEditId >> "+ purOrderEditId);
        String tranxDate = Communicator.text_to_date.fromString(tfPurOrderTranxDate.getText()).toString();
        String ledgrId = ledgerId;
        String gstNum = cmbPurOrderSupplierGST.getId();
        String purSerNum = tfPurOrdPurchaseSerial.getText();
        String challanNo = tfPurOrdOrderNo.getText();
        String purAcc = cmbPurOrderPurchaAc.getId();
        String challanDate = Communicator.text_to_date.fromString(tfPurOrderOrderDate.getText()).toString();


        PurTranxToProductRedirectionDTO mData = new PurTranxToProductRedirectionDTO();
        mData.setRedirect(FxmFileConstants.PURCHASE_ORDER_CREATE_SLUG);
        mData.setTranxDate(tranxDate);
        mData.setLedegrId(ledgrId);
        mData.setGstNum(gstNum);
        mData.setPurSerNum(purSerNum);
        mData.setChallanNo(challanNo);
        mData.setPurAcc(purAcc);
        mData.setChallanDate(challanDate);
        mData.setLedgerRed(isLedgerRed);
        mData.setProductRed(isProductRed);
        mData.setPurChallEditId(purOrderEditId);
        mData.setTranxType("purchase");
        mData.setRedirection(true);


        mData.setRediPrdCurrIndex(rediCurrIndex);
        List<PurchaseOrderTable> currentItems = new ArrayList<>(tblvPurOrderCmpTRow.getItems());
        List<PurchaseOrderTable> list = new ArrayList<>(currentItems);

        mData.setRowPurchaseOrder(list);

//        for (PurchaseInvoiceTable rowData : tblvPurChallCmpTRow.getItems()) {
//            PurchaseInvoiceTable mData1 = new PurchaseInvoiceTable();
//            mData1.setProduct_id(rowData.getProduct_id());
////            mData1.set
//        }
        if(isProductRed ==true){
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
                supplierGSTINList.add(new CommonDTO(gstin, gst_id));
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

    public void setRedirectData(Object inObj) {
        PurTranxToProductRedirectionDTO franchiseDTO = (PurTranxToProductRedirectionDTO) inObj;
        Integer redirectIndex = franchiseDTO.getRediPrdCurrIndex();
        tfPurOrdOrderNo.setText(franchiseDTO.getChallanNo());
        ledgerId=franchiseDTO.getLedegrId();
        tfPurOrdPurchaseSerial.setText(franchiseDTO.getPurSerNum());
        tfPurOrdLedgerName.setText(ledgersById(Long.valueOf(ledgerId)));
        purOrderEditId=franchiseDTO.getPurChallEditId();
        Integer editId = franchiseDTO.getPurChallEditId();
        if(editId > 0){
            btnPurOrderSubmit.setText("Update");
            btnPurOrderModify.setVisible(false);
        }
        int cnt=0,index=0;
        tblvPurOrderCmpTRow.getItems().clear();
        PurInvoiceCommunicator.levelAForPurInvoiceObservableList.clear();

        System.out.println(" Called >> " + supplierGSTINList.size());
        System.out.println(" Called ledger_id >> " + ledgerId);
        tfPurOrdLedgerName.setText(ledgersById(Long.valueOf(ledgerId)));
        if (supplierGSTINList != null && !supplierGSTINList.isEmpty()) {
            cmbPurOrderSupplierGST.setItems(supplierGSTINList);
            cmbPurOrderSupplierGST.setValue(supplierGSTINList.get(0));
        }  System.out.println(" Called >> " + supplierGSTINList.size());
        System.out.println(" Called ledger_id >> " + ledgerId);
        tfPurOrdLedgerName.setText(ledgersById(Long.valueOf(ledgerId)));
        if (supplierGSTINList != null && !supplierGSTINList.isEmpty()) {
            cmbPurOrderSupplierGST.setItems(supplierGSTINList);
            cmbPurOrderSupplierGST.setValue(supplierGSTINList.get(0));
        }

        if(franchiseDTO.getProductRed()==true) {
            Long productId =Long.valueOf(franchiseDTO.getRedirectProductId());
            int rindex = franchiseDTO.getRediPrdCurrIndex();
            JsonObject obj = productRedirectById(productId);

            for (PurchaseOrderTable purchaseInvoiceTable : franchiseDTO.getRowPurchaseOrder()) {
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

                }

                tblvPurOrderCmpTRow.getItems().addAll(purchaseInvoiceTable);
                PurchaseOrderCalculation.rowCalculationForPurcInvoice(index, tblvPurOrderCmpTRow, callback);
                PurchaseOrderCalculation.calculateGst(tblvPurOrderCmpTRow, callback);
//                calculateGst(tableView, callback);

                List<LevelAForPurInvoice> levelAForPurInvoiceList = com.opethic.genivis.controller.tranx_purchase.ProductUnitsPacking.getAllProductUnitsPackingFlavour(purchaseInvoiceTable.getProduct_id());
                ObservableList<LevelAForPurInvoice> observableLevelAList = FXCollections.observableArrayList(levelAForPurInvoiceList);
                if (index >= 0 && index < PurInvoiceCommunicator.levelAForPurInvoiceObservableList.size()) {
                    PurInvoiceCommunicator.levelAForPurInvoiceObservableList.set(index, observableLevelAList);

                    tblvPurOrderCmpTRow.getItems().get(index).setLevelA(null);
                    tblvPurOrderCmpTRow.getItems().get(index).setLevelA(levelAForPurInvoiceList.get(0).getLabel());

                    for (LevelAForPurInvoice levelAForPurInvoice : PurInvoiceCommunicator.levelAForPurInvoiceObservableList.get(index)) {
                        if (tblvPurOrderCmpTRow.getItems().get(index).getLevelA_id().equals(levelAForPurInvoice.getValue())) {
                            tblvPurOrderCmpTRow.getItems().get(index).setLevelA(null);
                            tblvPurOrderCmpTRow.getItems().get(index).setLevelA(levelAForPurInvoice.getLabel());
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

        int inx=0;
        System.out.println("is ledgeriiiiiiiiiii"+franchiseDTO.getLedgerRed());
        // tblvPurChallCmpTRow.getItems().clear();
        if(franchiseDTO.getLedgerRed()==true) {
            System.out.println("is ledger"+franchiseDTO.getLedgerRed());
            for (PurchaseOrderTable rowData : franchiseDTO.getRowPurchaseOrder()) {


                System.out.println("String.valueOf(rowData.getRowData().get(0))" + rowData.getProduct_id());
//                mData1.setProduct_id(rowData.getProduct_id());
//                mData1.setParticulars(rowData.getParticulars());
//                mData1.setPackages(rowData.getPackages());
                tblvPurOrderCmpTRow.getItems().add(rowData);
                inx++;
            }
        }

        Platform.runLater(()->{
            TableColumn<PurchaseOrderTable, ?> colName = tblvPurOrderCmpTRow.getColumns().get(1);
            tblvPurOrderCmpTRow.edit(redirectIndex, colName);
        });

    }


    private void ResponsiveWiseCssPicker() {
        double height = Screen.getPrimary().getBounds().getHeight();
        double width = Screen.getPrimary().getBounds().getWidth();
        System.out.println("width" + width);
        if (width >= 992 && width <= 1024) {
            topInnerHbOne.setSpacing(10);
            topInnerHbTwo.setSpacing(10);
            poTotalMainDiv.setSpacing(10);
            poTotalMainInnerDiv.setSpacing(6);
            poBottomFirstV.prefWidthProperty().bind(poBottomMain.widthProperty().multiply(0.76));
            poBottomSecondV.prefWidthProperty().bind(poBottomMain.widthProperty().multiply(0.24));
            tvPurOrderSupplierDetails.getStylesheets().add(GenivisApplication.class.getResource("ui/css/purchase_challan_css/supplier_table_css/supplier_table_992_1024.css").toExternalForm());
            bpPurOrderRootPane.getStylesheets().add(GenivisApplication.class.getResource("ui/css/TranxCommonCssStyles/tranxCommonCssStyle1.css").toExternalForm());
        } else if (width >= 1025 && width <= 1280) {
            topInnerHbOne.setSpacing(12);
            topInnerHbTwo.setSpacing(12);
            poTotalMainDiv.setSpacing(12);
            poTotalMainInnerDiv.setSpacing(8);
            poBottomFirstV.prefWidthProperty().bind(poBottomMain.widthProperty().multiply(0.76));
            poBottomSecondV.prefWidthProperty().bind(poBottomMain.widthProperty().multiply(0.24));
            tvPurOrderSupplierDetails.getStylesheets().add(GenivisApplication.class.getResource("ui/css/purchase_challan_css/supplier_table_css/supplier_table_1025_1280.css").toExternalForm());
            bpPurOrderRootPane.getStylesheets().add(GenivisApplication.class.getResource("ui/css/TranxCommonCssStyles/tranxCommonCssStyle2.css").toExternalForm());
        } else if (width >= 1281 && width <= 1368) {
            topInnerHbOne.setSpacing(15);
            topInnerHbTwo.setSpacing(15);
            poTotalMainDiv.setSpacing(15);
            poTotalMainInnerDiv.setSpacing(8);
            poBottomFirstV.prefWidthProperty().bind(poBottomMain.widthProperty().multiply(0.76));
            poBottomSecondV.prefWidthProperty().bind(poBottomMain.widthProperty().multiply(0.24));
            tvPurOrderSupplierDetails.getStylesheets().add(GenivisApplication.class.getResource("ui/css/purchase_challan_css/supplier_table_css/supplier_table_1281_1368.css").toExternalForm());
            bpPurOrderRootPane.getStylesheets().add(GenivisApplication.class.getResource("ui/css/TranxCommonCssStyles/tranxCommonCssStyle3.css").toExternalForm());
        } else if (width >= 1369 && width <= 1400) {
            topInnerHbOne.setSpacing(18);
            topInnerHbTwo.setSpacing(18);
            poTotalMainDiv.setSpacing(18);
            poTotalMainInnerDiv.setSpacing(8);
            poBottomFirstV.prefWidthProperty().bind(poBottomMain.widthProperty().multiply(0.76));
            poBottomSecondV.prefWidthProperty().bind(poBottomMain.widthProperty().multiply(0.24));
            tvPurOrderSupplierDetails.getStylesheets().add(GenivisApplication.class.getResource("ui/css/purchase_challan_css/supplier_table_css/supplier_table_1369_1400.css").toExternalForm());
            bpPurOrderRootPane.getStylesheets().add(GenivisApplication.class.getResource("ui/css/TranxCommonCssStyles/tranxCommonCssStyle4.css").toExternalForm());
        } else if (width >= 1401 && width <= 1440) {
            topInnerHbOne.setSpacing(18);
            topInnerHbTwo.setSpacing(18);
            poTotalMainDiv.setSpacing(18);
            poTotalMainInnerDiv.setSpacing(8);
            poBottomFirstV.prefWidthProperty().bind(poBottomMain.widthProperty().multiply(0.78));
            poBottomSecondV.prefWidthProperty().bind(poBottomMain.widthProperty().multiply(0.22));
            bpPurOrderRootPane.getStylesheets().add(GenivisApplication.class.getResource("ui/css/TranxCommonCssStyles/tranxCommonCssStyle5.css").toExternalForm());
            tvPurOrderSupplierDetails.getStylesheets().add(GenivisApplication.class.getResource("ui/css/purchase_challan_css/supplier_table_css/supplier_table_1401_1440.css").toExternalForm());
        } else if (width >= 1441 && width <= 1680) {
            topInnerHbOne.setSpacing(18);
            topInnerHbTwo.setSpacing(18);
            poTotalMainDiv.setSpacing(18);
            poTotalMainInnerDiv.setSpacing(8);
            poBottomFirstV.prefWidthProperty().bind(poBottomMain.widthProperty().multiply(0.8));
            poBottomSecondV.prefWidthProperty().bind(poBottomMain.widthProperty().multiply(0.2));
            tvPurOrderSupplierDetails.getStylesheets().add(GenivisApplication.class.getResource("ui/css/purchase_challan_css/supplier_table_css/supplier_table_1441_1680.css").toExternalForm());
            bpPurOrderRootPane.getStylesheets().add(GenivisApplication.class.getResource("ui/css/TranxCommonCssStyles/tranxCommonCssStyle6.css").toExternalForm());
        } else if (width >= 1681 && width <= 1920) {
            topInnerHbOne.setSpacing(20);
            topInnerHbTwo.setSpacing(20);
            poTotalMainDiv.setSpacing(20);
            poTotalMainInnerDiv.setSpacing(10);
            poBottomFirstV.prefWidthProperty().bind(poBottomMain.widthProperty().multiply(0.8));
            poBottomSecondV.prefWidthProperty().bind(poBottomMain.widthProperty().multiply(0.2));
//            sqBottomMain.setPrefHeight(150);
            tvPurOrderSupplierDetails.getStylesheets().add(GenivisApplication.class.getResource("ui/css/invoice_product_history_table.css").toExternalForm());
            bpPurOrderRootPane.getStylesheets().add(GenivisApplication.class.getResource("ui/css/TranxCommonCssStyles/tranxCommonCssStyle7.css").toExternalForm());
        }
    }

    public void responsiveCmpTable() {
        tblcPurOrderCmpTRowSrNo.prefWidthProperty().bind(tblvPurOrderCmpTRow.widthProperty().multiply(0.02));
        tblcPurOrderCmpTRowPacking.prefWidthProperty().bind(tblvPurOrderCmpTRow.widthProperty().multiply(0.05));
        tblcPurOrderCmpTRowUnit.prefWidthProperty().bind(tblvPurOrderCmpTRow.widthProperty().multiply(0.05));
        tblcPurOrderCmpTRowQuantity.prefWidthProperty().bind(tblvPurOrderCmpTRow.widthProperty().multiply(0.05));
        tblcPurOrderCmpTRowDisPer.prefWidthProperty().bind(tblvPurOrderCmpTRow.widthProperty().multiply(0.05));
        tblcPurOrderCmpTRowDisAmt.prefWidthProperty().bind(tblvPurOrderCmpTRow.widthProperty().multiply(0.05));
        tblcPurOrderCmpTRowTaxPer.prefWidthProperty().bind(tblvPurOrderCmpTRow.widthProperty().multiply(0.04));
        tblcPurOrderCmpTRowAction.prefWidthProperty().bind(tblvPurOrderCmpTRow.widthProperty().multiply(0.04));
        if (tblcPurOrderCmpTRowFreeQty.isVisible()) {
            tblcPurOrderCmpTRowFreeQty.prefWidthProperty().bind(tblvPurOrderCmpTRow.widthProperty().multiply(0.04));
            tblcPurOrderCmpTRowRate.prefWidthProperty().bind(tblvPurOrderCmpTRow.widthProperty().multiply(0.06));
        } else {
            tblcPurOrderCmpTRowRate.prefWidthProperty().bind(tblvPurOrderCmpTRow.widthProperty().multiply(0.08));
        }
        if (tblcPurOrderCmpTRowA.isVisible() && !tblcPurOrderCmpTRowB.isVisible() && !tblcPurOrderCmpTRowC.isVisible()) {
            tblcPurOrderCmpTRowParticulars.prefWidthProperty().bind(tblvPurOrderCmpTRow.widthProperty().multiply(0.34));
            tblcPurOrderCmpTRowGrossAmt.prefWidthProperty().bind(tblvPurOrderCmpTRow.widthProperty().multiply(0.1));
            tblcPurOrderCmpTRowNetAmt.prefWidthProperty().bind(tblvPurOrderCmpTRow.widthProperty().multiply(0.1));
//            tblcPurChallCmpTRowBatchNo.prefWidthProperty().bind(tblvPurOrderCmpTRow.widthProperty().multiply(0.1));
            tblcPurOrderCmpTRowA.prefWidthProperty().bind(tblvPurOrderCmpTRow.widthProperty().multiply(0.08));
        } else if (tblcPurOrderCmpTRowA.isVisible() && tblcPurOrderCmpTRowB.isVisible() && !tblcPurOrderCmpTRowC.isVisible()) {
            tblcPurOrderCmpTRowA.prefWidthProperty().bind(tblvPurOrderCmpTRow.widthProperty().multiply(0.06));
            tblcPurOrderCmpTRowParticulars.prefWidthProperty().bind(tblvPurOrderCmpTRow.widthProperty().multiply(0.3));
            tblcPurOrderCmpTRowGrossAmt.prefWidthProperty().bind(tblvPurOrderCmpTRow.widthProperty().multiply(0.08));
            tblcPurOrderCmpTRowNetAmt.prefWidthProperty().bind(tblvPurOrderCmpTRow.widthProperty().multiply(0.07));
//            tblcPurChallCmpTRowBatchNo.prefWidthProperty().bind(tblvPurOrderCmpTRow.widthProperty().multiply(0.06));
            tblcPurOrderCmpTRowB.prefWidthProperty().bind(tblvPurOrderCmpTRow.widthProperty().multiply(0.06));
        } else if (tblcPurOrderCmpTRowA.isVisible() && tblcPurOrderCmpTRowB.isVisible() && tblcPurOrderCmpTRowC.isVisible()) {
            tblcPurOrderCmpTRowParticulars.prefWidthProperty().bind(tblvPurOrderCmpTRow.widthProperty().multiply(0.265));
            tblcPurOrderCmpTRowGrossAmt.prefWidthProperty().bind(tblvPurOrderCmpTRow.widthProperty().multiply(0.07));
            tblcPurOrderCmpTRowNetAmt.prefWidthProperty().bind(tblvPurOrderCmpTRow.widthProperty().multiply(0.07));
//            tblcPurChallCmpTRowBatchNo.prefWidthProperty().bind(tblvPurOrderCmpTRow.widthProperty().multiply(0.06));
            tblcPurOrderCmpTRowA.prefWidthProperty().bind(tblvPurOrderCmpTRow.widthProperty().multiply(0.06));
            tblcPurOrderCmpTRowB.prefWidthProperty().bind(tblvPurOrderCmpTRow.widthProperty().multiply(0.06));
            tblcPurOrderCmpTRowC.prefWidthProperty().bind(tblvPurOrderCmpTRow.widthProperty().multiply(0.06));
        } else {
            tblcPurOrderCmpTRowParticulars.prefWidthProperty().bind(tblvPurOrderCmpTRow.widthProperty().multiply(0.34));
            tblcPurOrderCmpTRowGrossAmt.prefWidthProperty().bind(tblvPurOrderCmpTRow.widthProperty().multiply(0.1));
            tblcPurOrderCmpTRowNetAmt.prefWidthProperty().bind(tblvPurOrderCmpTRow.widthProperty().multiply(0.1));
//            tblcPurChallCmpTRowBatchNo.prefWidthProperty().bind(tblvPurChallCmpTRow.widthProperty().multiply(0.08));
        }

    }

    public void tfLedgerNameValidation() {
        tfPurOrdLedgerName.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal) {
                if (tfPurOrdLedgerName.getText().isEmpty()) {
                    tfPurOrdLedgerName.requestFocus();
                }
            }
        });
    }

    private void initialEnterMethod() {
        //         Enter traversal
        bpPurOrderRootPane.addEventFilter(KeyEvent.KEY_PRESSED, (KeyEvent event) -> {
            if (event.getCode() == KeyCode.ENTER) {

                if (event.getTarget() instanceof Button targetButton && targetButton.getText().equalsIgnoreCase("modify")) {
                    targetButton.getText();
//                    backToList();
                } else if (event.getTarget() instanceof Button targetButton && (targetButton.getText().equalsIgnoreCase("submit") ||
                        targetButton.getText().equalsIgnoreCase("update"))) {
                    System.out.println("Entered : Submit");
                    targetButton.getText();
                    btnPurOrderSubmit.fire();

                } else if (event.getTarget() instanceof Button targetButton && targetButton.getText().equalsIgnoreCase("cancel")) {
                    targetButton.getText();
                    btnPurOrderCancel.fire();
                } else {
                    KeyEvent newEvent = new KeyEvent(null, null, KeyEvent.KEY_PRESSED, "", "\t", KeyCode.TAB, event.isShiftDown(), event.isControlDown(), event.isAltDown(), event.isMetaDown());
                    Event.fireEvent(event.getTarget(), newEvent);
                    event.consume();
                }
            }
            /*if (event.getCode() == KeyCode.S && event.isControlDown()) {
                btnPurOrderSubmit.fire();
            }*/
            if (event.getCode() == KeyCode.X && event.isControlDown()) {
                btnPurOrderCancel.fire();
            } else if (event.isControlDown() && event.getCode() == KeyCode.S) {
                String msg = LedgerMessageConsts.msgConfirmationOnSubmit;
                if (purOrderEditId > 0) {
                    msg = LedgerMessageConsts.msgConfirmationOnUpdate;
                }
                AlertUtility.AlertConfirmation(AlertUtility.alertTypeConfirmation, msg + tfPurOrdOrderNo.getText() + LedgerMessageConsts.msgInvoiceNumber, input -> {
                    if (input == 1) {
                        if (purOrderEditId < 0) {
                            purchaseOrderCreateFun();
                        } else {
                            purchaseOrderUpdateFun();
                        }
                    }
                });


            } else if (event.isControlDown() && event.getCode() == KeyCode.E) {
                if (purOrderEditId < 0) {
                    AlertUtility.AlertConfirmation(AlertUtility.alertTypeConfirmation, LedgerMessageConsts.msgGoList, input -> {
                        PurchaseOrderListController.focusStatus = "cancel";
                        GlobalController.getInstance().addTabStatic(PURCHASE_ORDER_LIST_SLUG, false);
                    });
                } else {
                }
            }
        });
    }

    public void handleTfLedgerName() {
        Stage stage = (Stage) bpPurOrderRootPane.getScene().getWindow();
        SingleInputDialogs.openLedgerPopUp(stage, "Creditors", input -> {
            supplierGSTINList.clear();
            tfPurOrdLedgerName.setText(input[0].toString());
            ledgerId = (String) input[1];
            ledgerStateCode = (String) input[3];
            tblvPurOrderCmpTRow.getItems().get(0).setLedger_id(ledgerId);
            tranxLedgerDetailsFun(ledgerId);

            @SuppressWarnings("unchecked") ObservableList<GstDetailsDTO> gstDetailsDTOS = (ObservableList<GstDetailsDTO>) input[2];

            for (GstDetailsDTO gstDetailsDTO : gstDetailsDTOS) {
                supplierGSTINList.add(new CommonDTO(gstDetailsDTO.getGstNo(), gstDetailsDTO.getId()));
            }
//            if (supplierGSTINList != null && !supplierGSTINList.isEmpty()) {
//                cmbPurOrderSupplierGST.setItems(supplierGSTINList);
//                cmbPurOrderSupplierGST.setValue(supplierGSTINList.get(0));
//                cmbPurOrderSupplierGST.requestFocus();
//            } else {
//                tfPurOrdOrderNo.requestFocus();
//                cmbPurOrderSupplierGST.setPromptText("No Gst Number");
//            }
            if (supplierGSTINList != null && !supplierGSTINList.isEmpty()) {
                cmbPurOrderSupplierGST.setItems(supplierGSTINList);
                cmbPurOrderSupplierGST.setValue(supplierGSTINList.get(0));
                if (supplierGSTINList.size() > 1) {
                    cmbPurOrderSupplierGST.requestFocus();
                } else {
                    tfPurOrdOrderNo.requestFocus();
                }
            } else {
                tfPurOrdOrderNo.requestFocus();
            }
        }, in -> {
            System.out.println("In >> Called >. " + in);
            if (in) {
                isLedgerRed = true;
                setPurChallDataToProduct();
            }
        });
    }

    //TODO: open dropdown list onKey DOWN and SPACE
    public static void showDropDownOnKeyPress(ComboBox<?> comboBox) {
        comboBox.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.SPACE || e.getCode() == KeyCode.DOWN) {
                comboBox.show();
            }
        });
    }


    //Set the SUpplier Details of the Selected Product Bottom in Supplier Table
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
//            JsonObject resultObj = jsonObject.getAsJsonObject("result");
                        JsonArray dataArray = jsonObject.getAsJsonArray("data");
                        // getting values
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
                        tcPurOrderSupplerName.setCellValueFactory(new PropertyValueFactory<>("supplierName"));
                        tcPurOrderSupplerInvNo.setCellValueFactory(new PropertyValueFactory<>("supplierInvNo"));
                        tcPurOrderSupplerInvDate.setCellValueFactory(new PropertyValueFactory<>("supplierInvDate"));
                        tcPurOrderSupplerBatch.setCellValueFactory(new PropertyValueFactory<>("supplierBatch"));
                        tcPurOrderSupplerMrp.setCellValueFactory(new PropertyValueFactory<>("supplierMrp"));
                        tcPurOrderSupplerQty.setCellValueFactory(new PropertyValueFactory<>("supplierQty"));
                        tcPurOrderSupplerRate.setCellValueFactory(new PropertyValueFactory<>("supplierRate"));
                        tcPurOrderSupplerCost.setCellValueFactory(new PropertyValueFactory<>("supplierCost"));
                        tcPurOrderSupplerDisPer.setCellValueFactory(new PropertyValueFactory<>("supplierDisPer"));
                        tcPurOrderSupplerDisRs.setCellValueFactory(new PropertyValueFactory<>("supplierDisAmt"));

                        tcPurOrderSupplerName.setStyle("-fx-alignment: CENTER;");
                        tcPurOrderSupplerInvNo.setStyle("-fx-alignment: CENTER;");
                        tcPurOrderSupplerInvDate.setStyle("-fx-alignment: CENTER;");
                        tcPurOrderSupplerBatch.setStyle("-fx-alignment: CENTER;");
                        tcPurOrderSupplerMrp.setStyle("-fx-alignment: CENTER;");
                        tcPurOrderSupplerQty.setStyle("-fx-alignment: CENTER;");
                        tcPurOrderSupplerRate.setStyle("-fx-alignment: CENTER;");
                        tcPurOrderSupplerCost.setStyle("-fx-alignment: CENTER;");
                        tcPurOrderSupplerDisPer.setStyle("-fx-alignment: CENTER;");
                        tcPurOrderSupplerDisRs.setStyle("-fx-alignment: CENTER;");

                        tvPurOrderSupplierDetails.setItems(supplierDataList);

                    }


                }
            });
            apiClient.setOnCancelled(new EventHandler<WorkerStateEvent>() {
                @Override
                public void handle(WorkerStateEvent workerStateEvent) {
                    purchaseOrderLogger.error("Network API cancelled in getSupplierListbyProductId()" + workerStateEvent.getSource().getValue().toString());
                }
            });
            apiClient.setOnFailed(new EventHandler<WorkerStateEvent>() {
                @Override
                public void handle(WorkerStateEvent workerStateEvent) {
                    purchaseOrderLogger.error("Network API failed in getSupplierListbyProductId()" + workerStateEvent.getSource().getValue().toString());
                }
            });
            apiClient.start();
        } catch (Exception e) {
            System.out.println("exception:" + e);
        } finally {
            apiClient = null;
        }
    }

    // for showing the details of product after selection of product.
    private void tranxProductDetailsFun(String id) {
        // activating the product tab
        tranxPurOrderTabPane.getSelectionModel().select(tabPurOrderProduct);

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
//                    System.out.println("jsonObject in product info--" + jsonObject);
                    if (jsonObject.get("responseStatus").getAsInt() == 200) {
                        JsonObject productJsonObj = jsonObject.getAsJsonObject("result");

                        // setting values
                        txtPurOrderProductBrand.setText(productJsonObj.get("brand").getAsString());
                        txtPurOrderProductGroup.setText(productJsonObj.get("group").getAsString());
                        txtPurOrderProductSubGroup.setText(productJsonObj.get("subgroup").getAsString());
                        txtPurOrderProductCategory.setText(productJsonObj.get("category").getAsString());
                        txtPurOrderProductHsn.setText(productJsonObj.get("hsn").getAsString());
                        txtPurOrderProductTaxType.setText(productJsonObj.get("tax_type").getAsString());
                        txtPurOrderProductTaxPer.setText(decimalFormat.format(productJsonObj.get("tax_per").getAsDouble()));
                        txtPurOrderProductMarginPer.setText(productJsonObj.get("margin_per").getAsString().isEmpty() ? "" : decimalFormat.format(productJsonObj.get("margin_per").getAsDouble()));
                        txtPurOrderProductCost.setText(productJsonObj.get("cost").getAsString().isEmpty() ? "" : decimalFormat.format(productJsonObj.get("cost").getAsDouble()));
                        txtPurOrderProductShelfId.setText(productJsonObj.get("shelf_id").getAsString());
                        txtPurOrderProductMinStock.setText(productJsonObj.get("min_stocks").getAsString().isEmpty() ? "" : decimalFormat.format(productJsonObj.get("min_stocks").getAsDouble()));
                        txtPurOrderProductMaxStock.setText(productJsonObj.get("max_stocks").getAsString().isEmpty() ? "" : decimalFormat.format(productJsonObj.get("max_stocks").getAsDouble()));
                    }
                }
            });
            apiClient.setOnCancelled(new EventHandler<WorkerStateEvent>() {
                @Override
                public void handle(WorkerStateEvent workerStateEvent) {
                    purchaseOrderLogger.error("Network API cancelled in tranxProductDetailsFun()" + workerStateEvent.getSource().getValue().toString());
                }
            });
            apiClient.setOnFailed(new EventHandler<WorkerStateEvent>() {
                @Override
                public void handle(WorkerStateEvent workerStateEvent) {
                    purchaseOrderLogger.error("Network API failed in tranxProductDetailsFun()" + workerStateEvent.getSource().getValue().toString());
                }
            });
            apiClient.start();
        } catch (Exception e) {
            System.out.println("exception:" + e);
        } finally {
            apiClient = null;
        }


    }

    // for showing the details of ledger after selection of ledger.
    private void tranxLedgerDetailsFun(String id) {
        // activating the product tab
        tranxPurOrderTabPane.getSelectionModel().select(tabPurOrderLedger);

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
//                        CompanyStateCode = jsonObject.get("company_state_code").getAsLong();
//                        stateCode = jsonObj.get("stateCode").getAsLong();

                        // setting values
                        txtPurOrderGstNo.setText(jsonObj.get("gst_number").getAsString());
                        txtPurOrderArea.setText(jsonObj.get("area").getAsString());
                        txtPurOrderBank.setText(jsonObj.get("bank_name").getAsString());
                        txtPurOrderContactPerson.setText(jsonObj.get("contact_name").getAsString());
//            txtPurOrderTransportName.setText(jsonObject.get("contact_name").getAsString());
                        txtPurOrderCreditDays.setText(jsonObj.get("credit_days").getAsString());
                        txtPurOrderFssai.setText(jsonObj.get("fssai_number").getAsString());
                        txtPurOrderLicenseNo.setText(jsonObj.get("license_number").getAsString());
                        txtPurOrderRoute.setText(jsonObj.get("route").getAsString());
                    }
                }
            });
            apiClient.setOnCancelled(new EventHandler<WorkerStateEvent>() {
                @Override
                public void handle(WorkerStateEvent workerStateEvent) {
                    purchaseOrderLogger.error("Network API cancelled in tranxLedgerDetailsFun()" + workerStateEvent.getSource().getValue().toString());
                }
            });
            apiClient.setOnFailed(new EventHandler<WorkerStateEvent>() {
                @Override
                public void handle(WorkerStateEvent workerStateEvent) {
                    purchaseOrderLogger.error("Network API failed in tranxLedgerDetailsFun()" + workerStateEvent.getSource().getValue().toString());
                }
            });
            apiClient.start();
        } catch (Exception e) {
            System.out.println("exception:" + e);
        } finally {
            apiClient = null;
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

    // On Clicking on the Cancel Button From Create or Edit page redirect Back to List page.
//    public void backToList() {
////        Globals.purchaseOrderDTO = null;
//
//
//        AlertUtility.CustomCallback callback = number -> {
//            if (number == 1) {
//                GlobalController.getInstance().addTabStatic(PURCHASE_ORDER_LIST_SLUG, false);
//            } else {
//                System.out.println("working!");
//            }
//        };
//        AlertUtility.AlertConfirmation(AlertUtility.alertTypeConfirmation, LedgerMessageConsts.msgGoList, callback);
//    }

    public void clearLedAndProdInfo() {
        ObservableList<PurchaseOrderTable> emptyList = FXCollections.observableArrayList();
        tblvPurOrderCmpTRow.setItems(emptyList);
        ObservableList<SalesOrderSupplierDetailsDTO> emptyList1 = FXCollections.observableArrayList();
//        Debtor_id = "";
        tfPurOrdOrderNo.setText("");
        tfPurOrdLedgerName.setText("");
        cmbPurOrderSupplierGST.getSelectionModel().clearSelection();
        tvPurOrderSupplierDetails.getItems().clear();
        tfPurOrdNarration.setText("");

        //todo clear Ledger Info
        txtPurOrderGstNo.setText("");
        txtPurOrderArea.setText("");
        txtPurOrderBank.setText("");
        txtPurOrderContactPerson.setText("");
        txtPurOrderTransportName.setText("");
        txtPurOrderCreditDays.setText("");
        txtPurOrderFssai.setText("");
        txtPurOrderLicenseNo.setText("");
        txtPurOrderRoute.setText("");

        //todo clear Product Info
        txtPurOrderProductBrand.setText("");
        txtPurOrderProductGroup.setText("");
        txtPurOrderProductSubGroup.setText("");
        txtPurOrderProductCategory.setText("");
        txtPurOrderProductHsn.setText("");
        txtPurOrderProductTaxType.setText("");
        txtPurOrderProductTaxPer.setText("");
        txtPurOrderProductMarginPer.setText("");
        txtPurOrderProductCost.setText("");
        txtPurOrderProductShelfId.setText("");
        txtPurOrderProductMinStock.setText("");
        txtPurOrderProductMaxStock.setText("");

        //clear all row calculation
        lblPurOrderBillAmount.setText("0.00");

        lblPurOrderGrandTotal.setText("0.00");

        lblPurOrderDiscount.setText("0.00");

        lblPurOrderTax.setText("0.00");

        lblPurOrderTotal.setText("0.00");
    }

    public void sceneInitilization() {
        PurInvoiceCommunicator.resetFields();
        bpPurOrderRootPane.sceneProperty().addListener((observable, oldScene, newScene) -> {
            if (newScene != null && newScene.getWindow() instanceof Stage) {
                Communicator.stage = (Stage) newScene.getWindow();
            }
        });
    }

    //todo : for getting the purchase order serial number.
    private void getPurchaseSerialNo() {
        APIClient apiClient = null;
        try {
            apiClient = new APIClient(EndPoints.GET_LAST_PO_INVOICE_RECORD, "", RequestType.GET);
            apiClient.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
                @Override
                public void handle(WorkerStateEvent workerStateEvent) {
                    jsonObject = new Gson().fromJson(workerStateEvent.getSource().getValue().toString(), JsonObject.class);

                    if (jsonObject.get("responseStatus").getAsInt() == 200) {
                        String count = jsonObject.get("count").getAsString();
                        String OrderNo = jsonObject.get("serialNo").getAsString();
//            System.out.println("count =>" + count);
                        tfPurOrdPurchaseSerial.setText(count);
                        tfPurOrdOrderNo.setText(OrderNo);
                    }

                }
            });
            apiClient.setOnCancelled(new EventHandler<WorkerStateEvent>() {
                @Override
                public void handle(WorkerStateEvent workerStateEvent) {
                    purchaseOrderLogger.error("Network API cancelled in getPurchaseSerialNo()" + workerStateEvent.getSource().getValue().toString());
                }
            });
            apiClient.setOnFailed(new EventHandler<WorkerStateEvent>() {
                @Override
                public void handle(WorkerStateEvent workerStateEvent) {
                    purchaseOrderLogger.error("Network API failed in getPurchaseSerialNo()" + workerStateEvent.getSource().getValue().toString());
                }
            });
            apiClient.start();

        } catch (Exception e) {
            System.out.println("exception:" + e);
        } finally {
            apiClient = null;
        }
    }

    // get the purchase accounts
    public void getPurchaseAccounts() {
        APIClient apiClient = null;
        try {
            apiClient = new APIClient(EndPoints.GET_PURCHASE_ACCOUNTS, "", RequestType.GET);
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
                                cmbPurOrderPurchaAc.setItems(observableList);
                                cmbPurOrderPurchaAc.getSelectionModel().selectFirst();
                                cmbPurOrderPurchaAc.setConverter(new StringConverter<CommonDTO>() {
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
                    purchaseOrderLogger.error("Network API cancelled in getPurchaseAccounts()" + workerStateEvent.getSource().getValue().toString());
                }
            });
            apiClient.setOnFailed(new EventHandler<WorkerStateEvent>() {
                @Override
                public void handle(WorkerStateEvent workerStateEvent) {
                    purchaseOrderLogger.error("Network API failed in getPurchaseAccounts()" + workerStateEvent.getSource().getValue().toString());
                }
            });
            apiClient.start();

            HttpResponse<String> response = APIClient.getRequest(EndPoints.GET_PURCHASE_ACCOUNTS);
            JsonObject jsonObject = new Gson().fromJson(response.body(), JsonObject.class);
//            System.out.println(jsonObject);
            ObservableList<CommonDTO> observableList = FXCollections.observableArrayList();
            if (jsonObject.get("responseStatus").getAsInt() == 200) {
                JsonArray responseObject = jsonObject.getAsJsonArray("list");
//                System.out.println("responseObject getPurchaseAccounts " + responseObject);


                if (responseObject.size() > 0) {
                    for (JsonElement element : responseObject) {
                        JsonObject item = element.getAsJsonObject();
                        String id = item.get("id").getAsString();
                        String name = item.get("name").getAsString();
                        String unique_code = item.get("unique_code").getAsString();


                        observableList.add(new CommonDTO(name, id));
                        cmbPurOrderPurchaAc.setItems(observableList);
                        cmbPurOrderPurchaAc.getSelectionModel().selectFirst();
                        cmbPurOrderPurchaAc.setConverter(new StringConverter<CommonDTO>() {
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
        } catch (Exception e) {
            System.out.println("exception:" + e);
        } finally {
            apiClient = null;
        }
    }

    //  Function for Check the entered date is in financial year or not
//    public void validateFinancialDate() {
//
//        LocalDate selectedDate = dpPurOrderTranxDate.getValue();
//
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
//        String formatedDate = selectedDate.format(formatter);
//
//        Map<String, String> map = new HashMap<>();
//        map.put("invoiceDate", formatedDate);
//        String formData = Globals.mapToStringforFormData(map);
//        HttpResponse<String> response = APIClient.postFormDataRequest(formData, "checkInvoiceDateIsBetweenFY");
//        String responseBody = response.body();
//        JsonObject jsonObject = new Gson().fromJson(responseBody, JsonObject.class);
//        if (jsonObject.get("responseStatus").getAsInt() != 200) {
//            Alert alert = new Alert(Alert.AlertType.WARNING);
//            alert.setTitle("WARNING");
//            alert.setHeaderText("Invoice date not valid as per Fiscal year");
//            alert.show();
//            PauseTransition delay = new PauseTransition(Duration.seconds(2));
//            delay.setOnFinished(event -> alert.close());
//            delay.play();
//        }
//    }

    public void validateFinancialDate() {
        APIClient apiClient = null;
        try {
            Map<String, String> map = new HashMap<>();
            map.put("invoiceDate", Communicator.text_to_date.fromString(tfPurOrderOrderDate.getText()).toString());
            String formData = Globals.mapToStringforFormData(map);
            System.out.println("formdata >>>  " + formData);
            apiClient = new APIClient(EndPoints.CHECK_INV_DATE_IS_BETWEEN_FY, formData, RequestType.FORM_DATA);
            apiClient.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
                @Override
                public void handle(WorkerStateEvent workerStateEvent) {
                    JsonObject jsonObject = new Gson().fromJson(workerStateEvent.getSource().getValue().toString(), JsonObject.class);
                    System.out.println(" jsonObject : >> " + jsonObject);
                    if (jsonObject.get("responseStatus").getAsInt() != 200) {
                        String message = "Invoice Date is Not Valid As Per Fiscal Year";
                        AlertUtility.AlertErrorTimeout(AlertUtility.alertTypeError, message, in -> {
                            tfPurOrderOrderDate.requestFocus();
                        });
                    }
                }
            });
            apiClient.setOnCancelled(new EventHandler<WorkerStateEvent>() {
                @Override
                public void handle(WorkerStateEvent workerStateEvent) {
                    purchaseOrderLogger.error("Network API cancelled in functionCompanyDuplicate()" + workerStateEvent.getSource().getValue().toString());
                }
            });
            apiClient.setOnFailed(new EventHandler<WorkerStateEvent>() {
                @Override
                public void handle(WorkerStateEvent workerStateEvent) {
                    purchaseOrderLogger.error("Network API failed in functionCompanyDuplicate()" + workerStateEvent.getSource().getValue().toString());
                }
            });
            apiClient.start();
        } catch (Exception e) {
            e.printStackTrace();
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

    private void validateOrderDate() {

//        tfPurOrderOrderDate.setOnKeyPressed(event -> {
//            if (event.getCode() == KeyCode.ENTER) {
//                validateFinancialDate();
//            }
//        });
        tfPurOrderOrderDate.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal) {
                if (tfPurOrderOrderDate.getText().isEmpty()) {
                    tfPurOrderOrderDate.requestFocus();
                } else {
                    System.out.println(" tfPurOrderOrderDate.getText() >> : " + tfPurOrderOrderDate.getText());
//                    validateFinancialDate();

                }

            }
        });
    }


    // CREATE API OF SALES ORDER
    public void purchaseOrderCreateFun() {

        LocalDate orderDate = LocalDate.parse(Communicator.text_to_date.fromString(tfPurOrderTranxDate.getText()).toString());
        LocalDate tranxDate = LocalDate.parse(Communicator.text_to_date.fromString(tfPurOrderOrderDate.getText()).toString());

        Map<String, String> map = new HashMap<>();
        // Form Data
        map.put("invoice_date", String.valueOf(orderDate));
        map.put("newReference", "false");
        map.put("invoice_no", tfPurOrdOrderNo.getText());
        map.put("purchase_id", cmbPurOrderPurchaAc.getValue().getId());
        map.put("purchase_sr_no", tfPurOrdPurchaseSerial.getText());
        map.put("transaction_date", String.valueOf(tranxDate));
        map.put("supplier_code_id", ledgerId);
        map.put("roundoff", finalroundOff == "" ? "0" : finalroundOff);
        map.put("narration", tfPurOrdNarration.getText());
        map.put("totalamt", lblPurOrderBillAmount.getText());
        map.put("total_purchase_discount_amt", lblPurOrderDiscount.getText());
        map.put("gstNo", cmbPurOrderSupplierGST.getValue() != null ? cmbPurOrderSupplierGST.getValue().toString() : "");
        map.put("tcs", "0");//static
        map.put("purchase_discount", "0");//static
        map.put("purchase_discount_amt", "0");//static
        map.put("additionalChargesTotal", "0");//static
        map.put("total_qty", "0");//static
        map.put("total_free_qty", "0.00");//static
        map.put("total_row_gross_amt", lblPurOrderGrandTotal.getText());
        map.put("total_base_amt", lblPurOrderGrandTotal.getText());
        map.put("total_invoice_dis_amt", lblPurOrderDiscount.getText());
        map.put("taxable_amount", lblPurOrderTotal.getText());
        map.put("total_tax_amt", lblPurOrderTax.getText());
        map.put("bill_amount", lblPurOrderBillAmount.getText());

        List<PurchaseOrderTable> currentItems = new ArrayList<>(tblvPurOrderCmpTRow.getItems());

        if (!currentItems.isEmpty()) {
            PurchaseOrderTable lastItem = currentItems.get(currentItems.size() - 1);
            if (lastItem.getParticulars() != null && lastItem.getParticulars().isEmpty()) {
                currentItems.remove(currentItems.size() - 1);
            }
        }

        List<PurchaseOrderTable> list = new ArrayList<>(currentItems);


        JsonObject jsonObject = new JsonObject();
        JsonObject jsonObject1 = new JsonObject();
        JsonArray jsonArray = new JsonArray();
        double taxPer = 0.0;
        for (PurchaseOrderTable cmpTRowDTO : list) {
            taxPer = cmpTRowDTO.getTax() != null && !cmpTRowDTO.getTax().isEmpty() ? Double.parseDouble(cmpTRowDTO.getTax()) : 0.0;

        }

        if (ledgerStateCode.equalsIgnoreCase(GlobalTranx.getCompanyStateCode().toString())) {
            map.put("taxFlag", "true");// static data as of now
            jsonObject.addProperty("gst", decimalFormat.format((taxPer) / 2));
            jsonObject.addProperty("amt", decimalFormat.format(Double.parseDouble(lblPurOrderTax.getText()) / 2));
            jsonArray.add(jsonObject);

            jsonObject1.add("cgst", jsonArray);
            jsonObject1.add("sgst", jsonArray);
            map.put("taxCalculation", jsonObject1.toString());
            map.put("totalcgst", decimalFormat.format(Double.parseDouble(lblPurOrderTax.getText()) / 2));
            map.put("totalsgst", decimalFormat.format(Double.parseDouble(lblPurOrderTax.getText()) / 2));
            map.put("totaligst", decimalFormat.format(Double.parseDouble(lblPurOrderTax.getText())));
        } else {
            map.put("taxFlag", "false");// static data as of now
            jsonObject.addProperty("gst", decimalFormat.format(taxPer));
            jsonObject.addProperty("amt", decimalFormat.format(Double.parseDouble(lblPurOrderTax.getText())));
            jsonArray.add(jsonObject);
            jsonObject1.add("igst", jsonArray);
            map.put("taxCalculation", jsonObject1.toString());
            map.put("totalcgst", decimalFormat.format(Double.parseDouble(lblPurOrderTax.getText()) / 2));
            map.put("totalsgst", decimalFormat.format(Double.parseDouble(lblPurOrderTax.getText()) / 2));
            map.put("totaligst", decimalFormat.format(Double.parseDouble(lblPurOrderTax.getText())));
        }

        // Header Data
        Map<String, String> headers = new HashMap<>();
        headers.put("branch", Globals.headerBranch);

        // Row Data
        /***** mapping Row Data into TranxPurRowDTO *****/
        ArrayList<TranxPurRowDTO> rowData = new ArrayList<>();
        for (PurchaseOrderTable cmpTRowDTO : list) {

            TranxPurRowDTO purParticularRow = new TranxPurRowDTO();
            purParticularRow.setProductId(cmpTRowDTO.getProduct_id());
            purParticularRow.setDetailsId("0");
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
            if (!cmpTRowDTO.getUnit().isEmpty()) {
                purParticularRow.setUnitId(cmpTRowDTO.getUnit_id());
            } else {
                purParticularRow.setUnitId("");
            }
            if (cmpTRowDTO.getUnit_conv() != null) {
                purParticularRow.setUnitConv(String.valueOf(cmpTRowDTO.getUnit_conv()));
            } else {
                purParticularRow.setUnitConv("1");
            }
            if (!cmpTRowDTO.getQuantity().isEmpty()) {
                purParticularRow.setQty(cmpTRowDTO.getQuantity());
            } else {
                purParticularRow.setQty("");
            }
            if (!cmpTRowDTO.getFree().isEmpty() || cmpTRowDTO.getFree() != "null") {
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
                purParticularRow.setDisPer2(cmpTRowDTO.getDis_per2());
            } else {
                purParticularRow.setDisPer2("0");
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
                purParticularRow.setGrossAmt1(String.valueOf(cmpTRowDTO.getGross_amount1()));
            } else {
                purParticularRow.setGrossAmt1("0");
            }
            if (cmpTRowDTO.getInvoice_dis_amt() != null) {
                purParticularRow.setInvoiceDisAmt(String.valueOf(cmpTRowDTO.getInvoice_dis_amt()));
            } else {
                purParticularRow.setInvoiceDisAmt("0");
            }
            if (cmpTRowDTO.getTotal_amt() != null) {
                purParticularRow.setTotalAmt(String.valueOf(cmpTRowDTO.getTotal_amt()));
            } else {
                purParticularRow.setTotalAmt("0");
            }
            System.out.println("cmpTRowDTO.getGst()" + cmpTRowDTO.getGst());
            if (cmpTRowDTO.getGst() != null) {
                purParticularRow.setGst(String.valueOf(cmpTRowDTO.getGst()));
            } else {
                purParticularRow.setGst("");
            }
            System.out.println("cmpTRowDTO.getCGst()" + cmpTRowDTO.getCgst_per());
            if (cmpTRowDTO.getCgst_per() != null) {
                purParticularRow.setCgst(String.valueOf(cmpTRowDTO.getCgst_per()));
            } else {
                purParticularRow.setCgst("");
            }
            System.out.println("cmpTRowDTO.getIGst()" + cmpTRowDTO.getIgst_per());
            if (cmpTRowDTO.getIgst_per() != null) {
                purParticularRow.setIgst(String.valueOf(cmpTRowDTO.getIgst_per()));
            } else {
                purParticularRow.setIgst("");
            }
            System.out.println("cmpTRowDTO.getSgst()" + cmpTRowDTO.getSgst_per());
            if (cmpTRowDTO.getSgst_per() != null) {
                purParticularRow.setSgst(String.valueOf(cmpTRowDTO.getSgst_per()));
            } else {
                purParticularRow.setSgst("");
            }
//                    System.out.println("IGST : " + cmpTRowDTO.getTotal_igst());
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
            if (!cmpTRowDTO.getProductCode().isEmpty()) {
                purParticularRow.setProductCode(cmpTRowDTO.getProductCode());
            } else {
                purParticularRow.setProductCode("");
            }
            if (!cmpTRowDTO.getUnitName().isEmpty()) {
                purParticularRow.setProductUnit(cmpTRowDTO.getUnitName());
            } else {
                purParticularRow.setProductUnit("");
            }

            rowData.add(purParticularRow);
        }

        String mRowData = new Gson().toJson(rowData, new TypeToken<List<TranxPurRowDTO>>() {
        }.getType());
        map.put("row", mRowData);
        APIClient apiClient = new APIClient(EndPoints.PURCHASR_ORDER_CREATE, map, headers, null, RequestType.MULTI_PART);
        apiClient.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent workerStateEvent) {
                JsonObject responseBody = new Gson().fromJson(workerStateEvent.getSource().getValue().toString(), JsonObject.class);
                message = responseBody.get("message").getAsString();
                int status = responseBody.get("responseStatus").getAsInt();
                if (responseBody.get("responseStatus").getAsInt() == 200) {

                    Globals.purchaseOrderListSrc = "M";

//                    AlertUtility.CustomCallback callback1 = (number1) -> {
//
//                        if (number1 == 1) {
//                            GlobalController.getInstance().addTabStatic(PURCHASE_ORDER_LIST_SLUG, false);
//                        }
//                    };

//                    Stage stage2 = (Stage) bpPurOrderRootPane.getScene().getWindow();
//                    if (status == 200) {
//                        PurchaseOrderListController.focusStatus = "create";
//
//                        AlertUtility.AlertSuccess(stage2, "Success", message, callback1);
//                        PurInvoiceCommunicator.resetFields();
//                    } else {
//                        AlertUtility.AlertError(stage2, AlertUtility.alertTypeError, message, callback1);
//                    }
                    if (status == 200) {
//                                AlertUtility.AlertSuccess(stage2, "Success", message, callback1);
                        AlertUtility.AlertSuccessTimeout(AlertUtility.alertTypeSuccess, message, input -> {
                            GlobalController.getInstance().addTabStatic(PURCHASE_ORDER_LIST_SLUG, false);
                        });
                        PurInvoiceCommunicator.resetFields();
                    } else {
//                                AlertUtility.AlertError(stage2, AlertUtility.alertTypeError, message, callback1);
                        AlertUtility.AlertErrorTimeout(AlertUtility.alertTypeError, message, in -> {
                            btnPurOrderSubmit.requestFocus();
                        });
                    }
                }
            }
        });
        apiClient.setOnCancelled(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent workerStateEvent) {
                System.out.println("from setOnCancelled");
            }
        });
        apiClient.setOnFailed(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent workerStateEvent) {
                System.out.println("from setOnFailed");
            }
        });
        apiClient.start();


    }

    // CREATE API OF SALES ORDER
    public void purchaseOrderUpdateFun() {

        LocalDate orderDate = LocalDate.parse(Communicator.text_to_date.fromString(tfPurOrderTranxDate.getText()).toString());
        LocalDate tranxDate = LocalDate.parse(Communicator.text_to_date.fromString(tfPurOrderOrderDate.getText()).toString());

        Map<String, String> map = new HashMap<>();
        // Form Data
        map.put("id", String.valueOf(purOrderEditId));
        map.put("invoice_date", String.valueOf(orderDate));
        map.put("newReference", "false");
        map.put("invoice_no", tfPurOrdOrderNo.getText());
        map.put("purchase_id", cmbPurOrderPurchaAc.getValue().getId());
        map.put("purchase_sr_no", tfPurOrdPurchaseSerial.getText());
        map.put("transaction_date", String.valueOf(tranxDate));
        map.put("supplier_code_id", ledgerId);
        map.put("roundoff", finalroundOff == "" ? "0" : finalroundOff);
        map.put("narration", tfPurOrdNarration.getText());
        map.put("totalamt", lblPurOrderBillAmount.getText());
        map.put("total_purchase_discount_amt", lblPurOrderDiscount.getText());
        map.put("gstNo", cmbPurOrderSupplierGST.getValue() != null ? cmbPurOrderSupplierGST.getValue().toString() : "");
        map.put("tcs", "0");//static
        map.put("purchase_discount", "0");//static
        map.put("purchase_discount_amt", "0");//static
        map.put("additionalChargesTotal", "0");//static
        map.put("total_qty", "0");//static
        map.put("total_free_qty", "0.00");//static
        map.put("total_row_gross_amt", lblPurOrderGrandTotal.getText());
        map.put("total_base_amt", lblPurOrderGrandTotal.getText());
        map.put("total_invoice_dis_amt", lblPurOrderDiscount.getText());
        map.put("taxable_amount", lblPurOrderTotal.getText());
        map.put("total_tax_amt", lblPurOrderTax.getText());
        map.put("bill_amount", lblPurOrderBillAmount.getText());

        JsonObject jsonObject = new JsonObject();
        JsonObject jsonObject1 = new JsonObject();
        JsonArray jsonArray = new JsonArray();
        double taxPer = 0.0;
        for (PurchaseOrderTable cmpTRowDTO : tblvPurOrderCmpTRow.getItems()) {
            taxPer = cmpTRowDTO.getTax() != null && !cmpTRowDTO.getTax().isEmpty() ? Double.parseDouble(cmpTRowDTO.getTax()) : 0.0;
        }

        if (CompanyStateCode.equals(stateCode)) {
            map.put("taxFlag", "true");// static data as of now
            jsonObject.addProperty("gst", decimalFormat.format((taxPer) / 2));
            jsonObject.addProperty("amt", decimalFormat.format(Double.parseDouble(lblPurOrderTax.getText()) / 2));
            jsonArray.add(jsonObject);

            jsonObject1.add("cgst", jsonArray);
            jsonObject1.add("sgst", jsonArray);
            map.put("taxCalculation", jsonObject1.toString());
            map.put("totalcgst", decimalFormat.format(Double.parseDouble(lblPurOrderTax.getText()) / 2));
            map.put("totalsgst", decimalFormat.format(Double.parseDouble(lblPurOrderTax.getText()) / 2));
            map.put("totaligst", decimalFormat.format(Double.parseDouble(lblPurOrderTax.getText())));
        } else {
            map.put("taxFlag", "false");// static data as of now
            jsonObject.addProperty("gst", decimalFormat.format(taxPer));
            jsonObject.addProperty("amt", decimalFormat.format(Double.parseDouble(lblPurOrderTax.getText())));
            jsonArray.add(jsonObject);
            jsonObject1.add("igst", jsonArray);
            map.put("taxCalculation", jsonObject1.toString());
            map.put("totalcgst", decimalFormat.format(Double.parseDouble(lblPurOrderTax.getText()) / 2));
            map.put("totalsgst", decimalFormat.format(Double.parseDouble(lblPurOrderTax.getText()) / 2));
            map.put("totaligst", decimalFormat.format(Double.parseDouble(lblPurOrderTax.getText())));
        }
        JsonArray delArr = new JsonArray();
        for (Integer delId : delIds) {
            JsonObject delObj = new JsonObject();
            delObj.addProperty("del_id", delId);
            delArr.add(delObj);
        }
        map.put("rowDelDetailsIds", delArr.toString());
        map.put("acDelDetailsIds", "[]");

        // Header Data
        Map<String, String> headers = new HashMap<>();
        headers.put("branch", Globals.headerBranch);

        // Row Data
        /***** mapping Row Data into TranxPurRowDTO *****/
        ArrayList<TranxPurRowDTO> rowData = new ArrayList<>();
        for (PurchaseOrderTable cmpTRowDTO : tblvPurOrderCmpTRow.getItems()) {
            TranxPurRowDTO purParticularRow = new TranxPurRowDTO();
            purParticularRow.setProductId(cmpTRowDTO.getProduct_id());
            if (cmpTRowDTO.getDetails_id() != null && !cmpTRowDTO.getDetails_id().isEmpty()) {
//                        System.out.println("Details ID1 " + cmpTRowDTO.getDetails_id());
                purParticularRow.setDetailsId((cmpTRowDTO.getDetails_id()));
            } else {
//                        System.out.println("Details ID2 " + cmpTRowDTO.getDetails_id());
                purParticularRow.setDetailsId(("0"));
            }
//            purParticularRow.setDetails_id(cmpTRowDTO.getDetails_id());
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
            if (!cmpTRowDTO.getUnit().isEmpty()) {
                purParticularRow.setUnitId(cmpTRowDTO.getUnit_id());
            } else {
                purParticularRow.setUnitId("");
            }
            if (cmpTRowDTO.getUnit_conv() != null) {
                purParticularRow.setUnitConv(String.valueOf(cmpTRowDTO.getUnit_conv()));
            } else {
                purParticularRow.setUnitConv("1");
            }
            if (!cmpTRowDTO.getQuantity().isEmpty()) {
                purParticularRow.setQty(cmpTRowDTO.getQuantity());
            } else {
                purParticularRow.setQty("");
            }
            purParticularRow.setFreeQty("0");

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
            if (!cmpTRowDTO.getDis_per2().isEmpty()) {
                purParticularRow.setDisPer2(cmpTRowDTO.getDis_per2());
            } else {
                purParticularRow.setDisPer2("0");
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
                purParticularRow.setGrossAmt1(String.valueOf(cmpTRowDTO.getGross_amount1()));
            } else {
                purParticularRow.setGrossAmt1("0");
            }
            if (cmpTRowDTO.getInvoice_dis_amt() != null) {
                purParticularRow.setInvoiceDisAmt(String.valueOf(cmpTRowDTO.getInvoice_dis_amt()));
            } else {
                purParticularRow.setInvoiceDisAmt("0.0");
            }
            if (cmpTRowDTO.getTotal_amt() != null) {
                purParticularRow.setTotalAmt(String.valueOf(cmpTRowDTO.getTotal_amt()));
            } else {
                purParticularRow.setTotalAmt("0");
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

            rowData.add(purParticularRow);
        }

        String mRowData = new Gson().toJson(rowData, new TypeToken<List<TranxPurRowDTO>>() {
        }.getType());
        map.put("row", mRowData);


        APIClient apiClient = new APIClient(EndPoints.PURCHASR_ORDER_UPDATE, map, headers, null, RequestType.MULTI_PART);
        apiClient.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent workerStateEvent) {
                JsonObject responseBody = new Gson().fromJson(workerStateEvent.getSource().getValue().toString(), JsonObject.class);
//                        System.out.println("responseBody Purchase Order =>" + responseBody);
                message = responseBody.get("message").getAsString();
                delIds.clear();
                int status = responseBody.get("responseStatus").getAsInt();
                if (responseBody.get("responseStatus").getAsInt() == 200) {

//                    AlertUtility.CustomCallback callback1 = (number1) -> {
//
//                        if (number1 == 1) {
//
//                            PurchaseOrderListController.focusStatus = "edit";
//                            PurchaseOrderListController.selectedRowIndex = selectedRowIndex;
//
//                            GlobalController.getInstance().addTabStatic(PURCHASE_ORDER_LIST_SLUG, false);
//                        }
//                    };
//
//                    Stage stage2 = (Stage) bpPurOrderRootPane.getScene().getWindow();
//                    if (status == 200) {
//                        AlertUtility.AlertSuccess(stage2, "Success", message, callback1);
//                    } else {
//                        AlertUtility.AlertError(stage2, AlertUtility.alertTypeError, message, callback1);
//                    }
                    if (status == 200) {
//                                AlertUtility.AlertSuccess(stage2, "Success", message, callback1);
                        AlertUtility.AlertSuccessTimeout(AlertUtility.alertTypeSuccess, message, input -> {
                            PurchaseOrderListController.focusStatus = "edit";
                            PurchaseOrderListController.selectedRowIndex = selectedRowIndex;
                            GlobalController.getInstance().addTabStatic(PURCHASE_ORDER_LIST_SLUG, false);
                        });
                    } else {
//                                AlertUtility.AlertError(stage2, AlertUtility.alertTypeError, message, callback1);
                        AlertUtility.AlertErrorTimeout(AlertUtility.alertTypeError, message, in -> {
                            btnPurOrderSubmit.requestFocus();
                        });
                    }
                }
            }
        });
        apiClient.setOnCancelled(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent workerStateEvent) {
                System.out.println("from setOnCancelled");
            }
        });
        apiClient.setOnFailed(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent workerStateEvent) {
                System.out.println("from setOnFailed");
            }
        });
        apiClient.start();

    }


    //     GetById API Integration
    public void setEditId(Integer InId) {
        purOrderEditId = InId;
        System.out.println("purchase order edit id==>" + purOrderEditId);
        if (InId > -1) {
            setEditData();
        }
    }

    private void setEditData() {
        getEditDataById(purOrderEditId);
    }

    //    set the data of purchase order by Id
    public void getEditDataById(Integer purOrderEditId) {
        APIClient apiClient = null;
        Map<String, String> body = new HashMap<>();
        body.put("id", String.valueOf(purOrderEditId));
        String formData = Globals.mapToStringforFormData(body);
        apiClient = new APIClient(EndPoints.PURCHASE_ORDER_GET_BY_ID, formData, RequestType.FORM_DATA);
        apiClient.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent workerStateEvent) {
                jsonObject = new Gson().fromJson(workerStateEvent.getSource().getValue().toString(), JsonObject.class);
                if (jsonObject.get("responseStatus").getAsInt() == 200) {
                    System.out.println(jsonObject);
                    PurchaseOrderResponse responseBody = new Gson().fromJson(jsonObject.getAsJsonObject(), PurchaseOrderResponse.class);
                    setPurOrderFormData(responseBody);
                }
            }
        });
        apiClient.setOnCancelled(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent workerStateEvent) {
                purchaseOrderLogger.error("Network API cancelled in getEditDataById()" + workerStateEvent.getSource().getValue().toString());

            }
        });
        apiClient.setOnFailed(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent workerStateEvent) {
                purchaseOrderLogger.error("Network API failed in getEditDataById()" + workerStateEvent.getSource().getValue().toString());
            }
        });
        apiClient.start();
    }

    private void setPurOrderFormData(PurchaseOrderResponse responseBody) {
        btnPurOrderSubmit.setText("Update");
        btnPurOrderModify.setVisible(false);

        LocalDate tranxDate = LocalDate.parse(responseBody.getInvoiceData().getTransactionDt());
        LocalDate orderDate = LocalDate.parse(responseBody.getInvoiceData().getInvoiceDt());
        tfPurOrderTranxDate.setText(tranxDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        tfPurOrderOrderDate.setText(orderDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        tfPurOrdOrderNo.setText(responseBody.getInvoiceData().getInvoiceNo());
        tfPurOrdPurchaseSerial.setText(String.valueOf(responseBody.getInvoiceData().getPoSrNo()));
        tfPurOrdLedgerName.setText(String.valueOf(responseBody.getInvoiceData().getSupplierName()));
        ledgerId = String.valueOf(responseBody.getInvoiceData().getSupplierId());
        tranxLedgerDetailsFun(ledgerId);
        tfPurOrdNarration.setText(responseBody.getNarration());

        if (responseBody.getGstDetails() != null) {
            ObservableList<CommonDTO> gstDetailsList = FXCollections.observableArrayList();
//            System.out.println("gst in Edit" + responseBody.getGstDetails());
//            gstDetailsList.addAll(responseBody.getGstDetails());
            for (GstDetailsDTO gstDetailsDTO : responseBody.getGstDetails()) {
                gstDetailsList.add(new CommonDTO(gstDetailsDTO.getGstNo(), gstDetailsDTO.getId()));
            }
            cmbPurOrderSupplierGST.setItems(gstDetailsList);
            String gstNo = responseBody.getInvoiceData().getGstNo();
            gstDetailsList.stream().filter((v) -> v.getText().equalsIgnoreCase(gstNo)).findAny().ifPresent((p) -> {
                cmbPurOrderSupplierGST.setValue(p);
            });
        }

        int index = 0;
        Double totaldis = 0.0, totalTax = 0.0;
        Double grossAmt = 0.0, totalGrossAmt = 0.0;
        Double taxableAmt = 0.0, totalTaxableAmt = 0.0;
        Double billAmt = 0.0, totalBillAmt = 0.0;

        tblvPurOrderCmpTRow.getItems().clear();
        int srNo = 1;
        PurInvoiceCommunicator.resetFields();
        for (PurchaseOrderRow mRow : responseBody.getRow()) {
            productId = String.valueOf(mRow.getProductId());
//            tranxProductDetailsFun(productId);
//            getSupplierListbyProductId(productId);
//            PurchaseOrderTable row = new PurchaseOrderTable();
            PurchaseOrderTable row = new PurchaseOrderTable("", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "");

            row.setProduct_id(mRow.getProductId().toString());
            row.setParticulars(mRow.getProductName());
            row.setPackages(mRow.getPackName());
            row.setQuantity(String.valueOf(mRow.getQty().intValue()));
            row.setUnit(mRow.getUnitName());
            row.setUnitName(mRow.getUnitName());
            row.setUnit_id(mRow.getUnitId().toString());
            row.setRate(String.valueOf(mRow.getRate()));
            row.setGross_amount(String.valueOf(mRow.getGrossAmt()));
            row.setDis_per(String.valueOf(mRow.getDisPer()));
            row.setDis_amt(String.valueOf(mRow.getDisAmt()));
            row.setTax(String.valueOf(mRow.getGst()));
            row.setNet_amount(String.valueOf(mRow.getFinalAmt()));
            row.setFree(String.valueOf(mRow.getFreeQty()));
            row.setGross_amount1(String.valueOf(mRow.getGrossAmt1()));
            row.setIgst(mRow.getIgst().toString());
            row.setSgst(mRow.getSgst().toString());
            row.setCgst(mRow.getCgst().toString());
            row.setTotal_igst(mRow.getTotalIgst().toString());
            row.setTotal_sgst(mRow.getTotalSgst().toString());
            row.setTotal_cgst(mRow.getTotalCgst().toString());
//            row.setTax(String.valueOf(mRow.getGst()));
            row.setTaxable_amt(mRow.getGrossAmt().toString());
            row.setDetails_id(String.valueOf(mRow.getDetailsId()));
//            row.setGross_amt(String.valueOf(mRow.getFinalAmt()));
//            row.setTax_per("0");
            row.setLevelA_id(String.valueOf(mRow.getLevelAId()));
            row.setLevelB_id(String.valueOf(mRow.getLevelBId()));
            row.setLevelC_id(String.valueOf(mRow.getLevelCId()));
            row.setUnit_conv(mRow.getUnitConv().toString());
            row.setSr_no("" + srNo);
            srNo++;
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
            // !UnitRate List
            List<UnitRateList> rateList = new ArrayList<>();
            rateList.addAll((Collection<? extends UnitRateList>) SingleInputDialogs.getUnitRateListFromProduct(mRow.getProductName()));
            row.setRateList(rateList);
            tblvPurOrderCmpTRow.getItems().add(row);
            PurchaseOrderCalculation.rowCalculationForPurcInvoice(index, tblvPurOrderCmpTRow, callback);
            PurchaseOrderCalculation.calculateGst(tblvPurOrderCmpTRow, callback);
            List<LevelAForPurInvoice> levelAForPurInvoiceList = ProductUnitsPacking2.getAllProductUnitsPackingFlavour(row.getProduct_id());
            ObservableList<LevelAForPurInvoice> observableLevelAList = FXCollections.observableArrayList(levelAForPurInvoiceList);
            if (index >= 0 && index < PurInvoiceCommunicator.levelAForPurInvoiceObservableList.size()) {
                PurInvoiceCommunicator.levelAForPurInvoiceObservableList.set(index, observableLevelAList);
                tblvPurOrderCmpTRow.getItems().get(index).setLevelA(null);
                tblvPurOrderCmpTRow.getItems().get(index).setLevelA(levelAForPurInvoiceList.get(0).getLabel());
                for (LevelAForPurInvoice levelAForPurInvoice : PurInvoiceCommunicator.levelAForPurInvoiceObservableList.get(index)) {
                    if (tblvPurOrderCmpTRow.getItems().get(index).getLevelA_id().equals(levelAForPurInvoice.getValue())) {
                        tblvPurOrderCmpTRow.getItems().get(index).setLevelA(null);
                        tblvPurOrderCmpTRow.getItems().get(index).setLevelA(levelAForPurInvoice.getLabel());
                    }
                }
            } else {
                PurInvoiceCommunicator.levelAForPurInvoiceObservableList.add(observableLevelAList);
            }
            index++;
        }
        tblvPurOrderCmpTRow.refresh();
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

        ObservableList<GstDTO> gstDTOObservableList = FXCollections.observableArrayList();
        ObservableList<?> item9List = (ObservableList<?>) item[9];
        for (Object obj : item9List) {
            if (obj instanceof GstDTO) {
                gstDTOObservableList.add((GstDTO) obj);
            }
        }

        lblPurOrderGrandTotal.setText((String) item[3]);
        lblPurOrderDiscount.setText((String) item[4]);
        lblPurOrderTotal.setText((String) item[5]);
        lblPurOrderTax.setText((String) item[6]);
        lblPurOrderBillAmount.setText((String) item[2]);

    };

    TableCellCallback<String> productID_callback = item -> {
        tranxProductDetailsFun(item);
        getSupplierListbyProductId(item);
    };
    TableCellCallback<Object[]> delCallback = item -> {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("del_id", item[0].toString());
        int delId = Integer.valueOf(item[0].toString());
        delIds.add(delId);
    };

    TableCellCallback<Object[]> addPrdCalbak = item -> {
        if (item != null) {
            System.out.println("item [0] " + item[0]);
            System.out.println("item [0] " + item[1]);
            if ((Boolean) item[0]) {
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
//    TableCellCallback<Boolean> addPrdCalbak = item -> {
//        if (item == true) {
//            System.out.println("item [0] "+ item [0] );
//            System.out.println("item [0] "+ item [1] );
//            if((Boolean)item [0] == true){
//                rediCurrIndex = (Integer) item [1];
//                String isProduct = (String) item[2];
//                if(isProduct.equalsIgnoreCase("isProduct")){
//                    isProductRed = true;
//                }
//                setPurChallDataToProduct();
//            }
//            System.out.println("isProductRed >> "+ isProductRed);
//        }
//    };

    /*** setting Purchase rate while Purchase Order from Franchise to GV ****/
    TableCellCallback<Integer> unit_callback = currentIndex -> {
        System.out.println("unitcallback" + currentIndex);
        PurchaseOrderTable tranxRow = tblvPurOrderCmpTRow.getItems().get(currentIndex);
//        if (tranxRow.getRate().isEmpty() || purOrderEditId < 0) {
//            *** get the purchase Rate from GV using current row index from table ***
        List<UnitRateList> rateList = tranxRow.getRateList();
        for (UnitRateList unitRateList : rateList) {
//                System.out.println("BEFORE:::");
//                System.out.println("Selected Unit ::" + unitRateList.getUnitName());
//                System.out.println("Selected Rate ::" + unitRateList.getPurRate());
            if (unitRateList.getUnitId() == Long.parseLong(tranxRow.getUnit_id())) {
//                    System.out.println("AFTER:::");
//                    System.out.println("Selected Unit ::" + unitRateList.getUnitName());
//                    System.out.println("Selected Rate ::" + unitRateList.getPurRate());
                tranxRow.setRate("" + unitRateList.getPurRate());
            }
        }
//        }
    };

    //new end
    public void tableInitiliazation() {

        tblvPurOrderCmpTRow.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tblvPurOrderCmpTRow.setEditable(true);
        tblvPurOrderCmpTRow.setFocusTraversable(false);

        Label headerLabel = new Label("Sr\nNo.");
        tblcPurOrderCmpTRowSrNo.setGraphic(headerLabel);

        tblvPurOrderCmpTRow.getItems().addAll(new PurchaseOrderTable("", "0", "1", "", "", "", "", "", "", "", "", "", "", "", "", "", "", ""));

        tblcPurOrderCmpTRowSrNo.setCellValueFactory(cellData -> cellData.getValue().sr_noProperty());
        tblcPurOrderCmpTRowSrNo.setStyle("-fx-alignment: CENTER;");

        tblcPurOrderCmpTRowPacking.setCellValueFactory(cellData -> cellData.getValue().packagesProperty());
        tblcPurOrderCmpTRowPacking.setStyle("-fx-alignment: CENTER;");


        tblcPurOrderCmpTRowA.setCellValueFactory(cellData -> cellData.getValue().levelAProperty());
        tblcPurOrderCmpTRowA.setCellFactory(column -> new ComboBoxTableCellForPurOrderLevelA("tblcPurOrderCmpTRowA"));

        tblcPurOrderCmpTRowB.setCellValueFactory(cellData -> cellData.getValue().levelBProperty());
        tblcPurOrderCmpTRowB.setCellFactory(column -> new ComboBoxTableCellForPurOrderLevelB("tblcPurOrderCmpTRowB"));

        tblcPurOrderCmpTRowC.setCellValueFactory(cellData -> cellData.getValue().levelCProperty());
        tblcPurOrderCmpTRowC.setCellFactory(column -> new ComboBoxTableCellForPurOrderLevelC("tblcPurOrderCmpTRowC"));

        tblcPurOrderCmpTRowUnit.setCellValueFactory(cellData -> cellData.getValue().unitProperty());
        tblcPurOrderCmpTRowUnit.setCellFactory(column -> new ComboBoxTableCellForSalesOrderUnit("tblcPurOrderCmpTRowUnit", unit_callback));

        tblcPurOrderCmpTRowParticulars.setCellValueFactory(cellData -> cellData.getValue().particularsProperty());
//        tblcPurOrderCmpTRowParticulars.setCellFactory(column -> new TextFieldTableCellForSalesOrder("tblcPurOrderCmpTRowParticulars", callback, productID_callback));
        tblcPurOrderCmpTRowParticulars.setCellFactory(column -> new TextFieldTableCellForSalesOrder("tblcPurOrderCmpTRowParticulars",
                callback, productID_callback, tfPurOrdNarration, addPrdCalbak));

        tblcPurOrderCmpTRowQuantity.setCellValueFactory(cellData -> cellData.getValue().quantityProperty());
        tblcPurOrderCmpTRowQuantity.setCellFactory(column -> new TextFieldTableCellForSalesOrder("tblcPurOrderCmpTRowQuantity", callback));

        tblcPurOrderCmpTRowFreeQty.setCellValueFactory(cellData -> cellData.getValue().freeProperty());
        tblcPurOrderCmpTRowFreeQty.setCellFactory(column -> new TextFieldTableCellForSalesOrder("tblcPurOrderCmpTRowFreeQty", callback));

        tblcPurOrderCmpTRowRate.setCellValueFactory(cellData -> cellData.getValue().rateProperty());
        tblcPurOrderCmpTRowRate.setCellFactory(column -> new TextFieldTableCellForSalesOrder("tblcPurOrderCmpTRowRate", callback));

        tblcPurOrderCmpTRowGrossAmt.setCellValueFactory(cellData -> cellData.getValue().gross_amountProperty());
        tblcPurOrderCmpTRowGrossAmt.setCellFactory(column -> new TextFieldTableCellForSalesOrder("tblcPurOrderCmpTRowGrossAmt", callback));

        tblcPurOrderCmpTRowDisPer.setCellValueFactory(cellData -> cellData.getValue().dis_perProperty());
        tblcPurOrderCmpTRowDisPer.setCellFactory(column -> new TextFieldTableCellForSalesOrder("tblcPurOrderCmpTRowDisPer", callback));

        tblcPurOrderCmpTRowDisAmt.setCellValueFactory(cellData -> cellData.getValue().dis_amtProperty());
        tblcPurOrderCmpTRowDisAmt.setCellFactory(column -> new TextFieldTableCellForSalesOrder("tblcPurOrderCmpTRowDisAmt", callback, tfPurOrdNarration));

        tblcPurOrderCmpTRowTaxPer.setCellValueFactory(cellData -> cellData.getValue().taxProperty());
        tblcPurOrderCmpTRowTaxPer.setCellFactory(column -> new TextFieldTableCellForSalesOrder("tblcPurOrderCmpTRowTaxPer", callback));

        tblcPurOrderCmpTRowNetAmt.setCellValueFactory(cellData -> cellData.getValue().net_amountProperty());
        tblcPurOrderCmpTRowNetAmt.setCellFactory(column -> new TextFieldTableCellForSalesOrder("tblcPurOrderCmpTRowNetAmt", callback));

        tblcPurOrderCmpTRowAction.setCellValueFactory(cellData -> cellData.getValue().net_amountProperty());
        tblcPurOrderCmpTRowAction.setCellFactory(column -> new DeleteButtonTableCell(delCallback));

        columnVisibility(tblcPurOrderCmpTRowA, Globals.getUserControlsWithSlug("is_level_a"));
        columnVisibility(tblcPurOrderCmpTRowB, Globals.getUserControlsWithSlug("is_level_b"));
        columnVisibility(tblcPurOrderCmpTRowC, Globals.getUserControlsWithSlug("is_level_c"));

    }

    private void columnVisibility(TableColumn<PurchaseOrderTable, String> column, boolean visible) {
        if (visible) {
//            column.setPrefWidth(USE_COMPUTED_SIZE);
//            column.setMinWidth(USE_PREF_SIZE);
            column.setMaxWidth(Double.MAX_VALUE);
        } else {
            column.prefWidthProperty().unbind();
            column.setPrefWidth(0.0);
            column.setMinWidth(0.0);
            column.setMaxWidth(0.0);
        }
    }

    public void validatePurchaseOrder() {
        APIClient apiClient = null;
        try {

            Map<String, String> map = new HashMap<>();
            map.put("supplier_id", ledgerId);
            map.put("bill_no", String.valueOf(tfPurOrdOrderNo.getText()));
            String formData = Globals.mapToStringforFormData(map);
            System.out.println("for,madata >>>  " + formData);
            apiClient = new APIClient(EndPoints.VALIDATE_PURCHASE_ORDER, formData, RequestType.FORM_DATA);
            apiClient.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
                @Override
                public void handle(WorkerStateEvent workerStateEvent) {
                    JsonObject jsonObject = new Gson().fromJson(workerStateEvent.getSource().getValue().toString(), JsonObject.class);
                    System.out.println(" jsonObject : >> " + jsonObject);
                    if (jsonObject.get("responseStatus").getAsInt() != 200) {
                        String message = jsonObject.get("message").getAsString();
                        AlertUtility.AlertWarningTimeout(AlertUtility.alertTypeError, message, in -> {
                            tfPurOrdOrderNo.requestFocus();
                        });
                    }
                }
            });
            apiClient.setOnCancelled(new EventHandler<WorkerStateEvent>() {
                @Override
                public void handle(WorkerStateEvent workerStateEvent) {
                    purchaseOrderLogger.error("Network API cancelled in functionCompanyDuplicate()" + workerStateEvent.getSource().getValue().toString());
                }
            });
            apiClient.setOnFailed(new EventHandler<WorkerStateEvent>() {
                @Override
                public void handle(WorkerStateEvent workerStateEvent) {
                    purchaseOrderLogger.error("Network API failed in functionCompanyDuplicate()" + workerStateEvent.getSource().getValue().toString());
                }
            });
            apiClient.start();
        } catch (Exception e) {
            e.printStackTrace();
            //TODO : use purchaseOrderLogger for alert messages
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

    public void validatePurchaseOrderUpdate() {
        APIClient apiClient = null;
        try {

            Map<String, String> map = new HashMap<>();
            map.put("invoice_id", String.valueOf(purOrderEditId));
            map.put("supplier_id", ledgerId);
            map.put("bill_no", String.valueOf(tfPurOrdOrderNo.getText()));
            String formData = Globals.mapToStringforFormData(map);
            apiClient = new APIClient(EndPoints.VALIDATE_PURCHASE_ORDER_UPDATE, formData, RequestType.FORM_DATA);
            apiClient.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
                @Override
                public void handle(WorkerStateEvent workerStateEvent) {
                    JsonObject jsonObject = new Gson().fromJson(workerStateEvent.getSource().getValue().toString(), JsonObject.class);
                    if (jsonObject.get("responseStatus").getAsInt() != 200) {
                        String message = jsonObject.get("message").getAsString();
                        AlertUtility.AlertWarningTimeout(AlertUtility.alertTypeError, message, in -> {
                            tfPurOrdOrderNo.requestFocus();
                        });
                    }
                }
            });
            apiClient.setOnCancelled(new EventHandler<WorkerStateEvent>() {
                @Override
                public void handle(WorkerStateEvent workerStateEvent) {
                    purchaseOrderLogger.error("Network API cancelled in functionCompanyDuplicate()" + workerStateEvent.getSource().getValue().toString());
                }
            });
            apiClient.setOnFailed(new EventHandler<WorkerStateEvent>() {
                @Override
                public void handle(WorkerStateEvent workerStateEvent) {
                    purchaseOrderLogger.error("Network API failed in functionCompanyDuplicate()" + workerStateEvent.getSource().getValue().toString());
                }
            });
            apiClient.start();
        } catch (Exception e) {
            e.printStackTrace();
            //TODO : use purchaseOrderLogger for alert messages
//            showAlert("Falied to connect server! ");
//            AlertUtility.AlertDialogForError("WARNING", LedgerMessageConsts.msgFailServer, input -> {
//            });
            AlertUtility.AlertErrorTimeout(AlertUtility.alertTypeError, LedgerMessageConsts.msgServerNotReady, in -> {
            });

        } finally {
            apiClient = null;
        }
    }

    private void tfOrderNoValidatition() {
        tfPurOrdOrderNo.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal) {
                if (tfPurOrdOrderNo.getText().isEmpty()) {
                    tfPurOrdOrderNo.requestFocus();
                } else {
                    String btText = btnPurOrderSubmit.getText();
                    if (btText.equalsIgnoreCase("Submit")) {
                        validatePurchaseOrder();
                    } else {
                        validatePurchaseOrderUpdate();
                    }
                }

            }
        });
    }

}

class TextFieldTableCellForSalesOrder extends TableCell<PurchaseOrderTable, String> {
    private final TextField textField;
    private final String columnName;

    private final TableCellCallback<Object[]> callback;
    private TableCellCallback<String> productID_callback;
    private TableCellCallback<Object[]> addPrdCalbak;
    private TextField button;

    public TextFieldTableCellForSalesOrder(String columnName, TableCellCallback<Object[]> callback) {
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

        netAmountColumn();

    }

    public TextFieldTableCellForSalesOrder(String columnName, TableCellCallback<Object[]> callback,TextField button ) {
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
        DisPerColumn();
        DisAmtColumn();

        netAmountColumn();

    }

    public TextFieldTableCellForSalesOrder(String columnName, TableCellCallback<Object[]> callback, TableCellCallback<String> productID_callback) {
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
        freeQtyColumn();
        DisPerColumn();
        DisAmtColumn();

        netAmountColumn();

    }


    public TextFieldTableCellForSalesOrder(String columnName, TableCellCallback<Object[]> callback,
                                           TableCellCallback<String> productID_callback, TextField button) {
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
        DisPerColumn();
        DisAmtColumn();
        netAmountColumn();

    }

    public TextFieldTableCellForSalesOrder(String columnName, TableCellCallback<Object[]> callback,
                                           TableCellCallback<String> productID_callback,
                                           TextField button, TableCellCallback<Object[]> addPrdCalbak) {
        this.columnName = columnName;
        this.textField = new TextField();

        this.callback = callback;
        this.addPrdCalbak = addPrdCalbak;

        this.productID_callback = productID_callback;
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
        freeQtyColumn();
        DisPerColumn();
        DisAmtColumn();
        netAmountColumn();

    }


    private void particularsColumn() {
        if ("tblcPurOrderCmpTRowParticulars".equals(columnName)) {
            textField.setEditable(false);
            textField.setOnKeyPressed(event -> {
                if (event.getCode() == KeyCode.SPACE) {
                    openProduct();
                } else if (event.isShiftDown() && event.getCode() == KeyCode.TAB) {
                    if (getIndex() > 0) {
                        TableColumn<PurchaseOrderTable, ?> colName = getTableView().getColumns().get(12);
                        getTableView().edit(getIndex() - 1, colName);
                    } /*else {
                        TableColumn<PurchaseOrderTable, ?> colName = getTableView().getColumns().get(12);
                        getTableView().edit(getIndex() - 1, colName);
                    }*/
                } else if (event.getCode() == KeyCode.ENTER || !event.isShiftDown() && event.getCode() == KeyCode.TAB) {
                    if (textField.getText().isEmpty() && getIndex() == getTableView().getItems().size() - 1 && getIndex() != 0) {
                        Platform.runLater(() -> {
                            button.requestFocus();
                        });
                    } else if (!textField.getText().isEmpty()) {
                        TableColumn<PurchaseOrderTable, ?> colName = getTableView().getColumns().get(6);
                        getTableView().edit(getIndex(), colName);
                    } else {
                        textField.requestFocus();
                    }
                    event.consume();
                }
            });

            textField.setOnMouseClicked(event -> {
                event.consume();
                //  openProduct();
            });
        }
    }

    private void quantityColumn() {
        if ("tblcPurOrderCmpTRowQuantity".equals(columnName)) {
            textField.setEditable(true);
            textField.setOnKeyPressed(event -> {
                if (!textField.getText().isEmpty()) {
                    CommonValidationsUtils.restrictToNumbers(textField);
                }
                if (event.isShiftDown() && event.getCode() == KeyCode.TAB) {
                    TableColumn<PurchaseOrderTable, ?> colName = getTableView().getColumns().get(6);
                    getTableView().edit(getIndex(), colName);

                } else if (event.getCode() == KeyCode.ENTER || event.getCode() == KeyCode.TAB) {
                    Integer index = getIndex();
                    if (!textField.getText().isEmpty()) {
                        TableColumn<PurchaseOrderTable, ?> colName = getTableView().getColumns().get(8);
                        getTableView().edit(index, colName);
                    } else {
                        textField.requestFocus();
                    }
                    PurchaseOrderCalculation.rowCalculationForPurcInvoice(index, getTableView(), callback);
                } else if (event.getCode() == KeyCode.DOWN) {
                    getTableView().getItems().addAll(new PurchaseOrderTable("", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", ""));
                    TableColumn<PurchaseOrderTable, ?> colName = getTableView().getColumns().get(1);
                    getTableView().edit(getIndex() + 1, colName);
                }
            });

        }
    }

    private void freeQtyColumn() {
        if ("tblcPurOrderCmpTRowFreeQty".equals(columnName)) {
            textField.setEditable(true);
            textField.setOnKeyPressed(event -> {
                if (!textField.getText().isEmpty()) {
                    CommonValidationsUtils.restrictToNumbers(textField);
                }
                if (event.isShiftDown() && event.getCode() == KeyCode.TAB) {
                    TableColumn<PurchaseOrderTable, ?> colName = getTableView().getColumns().get(7);
                    System.out.println("Col:" + colName.getText());
                    getTableView().edit(getIndex(), colName);

                } else if (event.getCode() == KeyCode.ENTER || event.getCode() == KeyCode.TAB) {
                    int index = getIndex();
                    TableColumn<PurchaseOrderTable, ?> colName = getTableView().getColumns().get(9);
                    getTableView().edit(getIndex(), colName);
                    PurchaseOrderCalculation.rowCalculationForPurcInvoice(index, getTableView(), callback);
                }

            });
           /* TableColumn<PurchaseOrderTable, ?> colName = getTableView().getColumns().get(8);
            getTableView().edit(getIndex(), colName);*/

        }
    }

    private void rateColumn() {
        if ("tblcPurOrderCmpTRowRate".equals(columnName)) {
            textField.setEditable(true);
            textField.setOnKeyPressed(event -> {
                if (!textField.getText().isEmpty()) {
                    CommonValidationsUtils.restrictToDecimalNumbers(textField);
                }
                if (event.isShiftDown() && event.getCode() == KeyCode.TAB) {
                    TableColumn<PurchaseOrderTable, ?> colName = getTableView().getColumns().get(8);
                    getTableView().edit(getIndex(), colName);
                } else if (event.getCode() == KeyCode.ENTER || event.getCode() == KeyCode.TAB) {
                    int index = getIndex();
                    if (!textField.getText().isEmpty()) {
                        TableColumn<PurchaseOrderTable, ?> colName = getTableView().getColumns().get(11);
                        getTableView().edit(index, colName);
                    } else {
                        textField.requestFocus();
                    }
                    PurchaseOrderCalculation.rowCalculationForPurcInvoice(index, getTableView(), callback);
                } else if (event.getCode() == KeyCode.DOWN) {
                    getTableView().getItems().addAll(new PurchaseOrderTable("", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", ""));
                    TableColumn<PurchaseOrderTable, ?> colName = getTableView().getColumns().get(1);
                    getTableView().edit(getIndex() + 1, colName);
                }
            });

        }
    }

    private void DisPerColumn() {
        if ("tblcPurOrderCmpTRowDisPer".equals(columnName)) {
            textField.setEditable(true);

            textField.setOnKeyPressed(event -> {
                if (!textField.getText().isEmpty()) {
                    CommonValidationsUtils.restrictToDecimalNumbers(textField);
                }
                if (event.isShiftDown() && event.getCode() == KeyCode.TAB) {
                    TableColumn<PurchaseOrderTable, ?> colName = getTableView().getColumns().get(9);
                    getTableView().edit(getIndex(), colName);
                } else if (event.getCode() == KeyCode.ENTER || event.getCode() == KeyCode.TAB) {
                    int index = getIndex();
                    TableColumn<PurchaseOrderTable, ?> colName = getTableView().getColumns().get(12);
                    getTableView().edit(index, colName);
                    PurchaseOrderCalculation.rowCalculationForPurcInvoice(index, getTableView(), callback);
                }
            });
        }
    }

    private void DisAmtColumn() {
        if ("tblcPurOrderCmpTRowDisAmt".equals(columnName)) {
            textField.setEditable(true);

            textField.setOnKeyPressed(event -> {
                if (!textField.getText().isEmpty()) {
                    CommonValidationsUtils.restrictToDecimalNumbers(textField);
                }
                if (event.isShiftDown() && event.getCode() == KeyCode.TAB) {
                    TableColumn<PurchaseOrderTable, ?> colName = getTableView().getColumns().get(11);
                    getTableView().edit(getIndex(), colName);
                } else if (event.getCode() == KeyCode.ENTER || event.getCode() == KeyCode.TAB) {
                    int index = getIndex();
                    if (index >= getTableView().getItems().size() - 1){
                        System.out.println("Index of >>> "+ index);
                        PurchaseOrderTable selectedRow = getTableView().getItems().get(index);
                        if(!selectedRow.getParticulars().isEmpty()){
                            getTableView().getItems().addAll(new PurchaseOrderTable("", "", "" + (getIndex() + 2), "", "", "", "",
                                    "", "", "", "", "", "", "", "", "", "", ""));
                            TableColumn<PurchaseOrderTable, ?> colName = getTableView().getColumns().get(1);
                            getTableView().edit(getIndex() + 1, colName);
                        }else{
                            System.out.println("hello I am Here ");
                            button.requestFocus();
                        }

                    }




                    PurchaseOrderCalculation.rowCalculationForPurcInvoice(index, getTableView(), callback);

                }
            });
        }
    }

    private void netAmountColumn() {

        if ("tblcPurOrderCmpTRowNetAmt".equals(columnName)) {
            textField.setEditable(false);
            textField.setOnKeyPressed(event -> {
                if (event.getCode() == KeyCode.ENTER) {
                    int current_index = getTableRow().getIndex();
                    getTableView().getItems().addAll(new PurchaseOrderTable("", String.valueOf(current_index + 1), "1", "", "", "", "", "", "", "", "", "", "", "", "", "", "", ""));
                }
            });
        }
    }

    private void openProduct() {
        PurchaseOrderTable selectedRow = getTableView().getItems().get(getIndex());
        SingleInputDialogs.openProductPopUp(Communicator.stage, Integer.parseInt(selectedRow.getProduct_id()), "Product", input -> {

            String productName = (String) input[0];
            String productId = (String) input[1];
            String packaging = (String) input[2];
            String mrp = (String) input[3];
            String unit = (String) input[4];
            String taxper = (String) input[5];
            String salesrate = (String) input[6];
            String purchaseRate = (String) input[7];
            System.out.println("Pur Rate:" + purchaseRate);
            String is_batch = (String) input[8];
            String productCode = (String) input[10];
            /**** Multi Unit Rate : rateList contains unit wise rate ****/
            List<UnitRateList> rateList = new ArrayList<>();
            rateList.addAll((Collection<? extends UnitRateList>) input[9]);
            getTableRow().getItem().setRateList(rateList);
            if (productID_callback != null) {
                productID_callback.call(productId);
            }

            getTableRow().getItem().setParticulars(productName);
            getTableRow().getItem().setProduct_id(productId);
            getTableRow().getItem().setPackages(packaging);
            getTableRow().getItem().setIs_batch(is_batch);
            getTableRow().getItem().setTax(taxper);
            getTableRow().getItem().setUnit(unit);
            getTableRow().getItem().setRate(mrp);
            getTableRow().getItem().setProductCode(productCode);

            List<LevelAForPurInvoice> levelAForPurInvoiceList = ProductUnitsPacking2.getAllProductUnitsPackingFlavour(productId);
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
            TableColumn<PurchaseOrderTable, ?> colName = getTableView().getColumns().get(1);
            getTableView().edit(getIndex(), colName);
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

    public void textfieldStyle() {
        if (columnName.equals("tblcPurOrderCmpTRowParticulars")) {
            textField.setPromptText("Particulars");
            textField.setEditable(false);
        } else {
            textField.setPromptText("0");
        }

//        textField.setPrefHeight(38);
//        textField.setMaxHeight(38);
//        textField.setMinHeight(38);

        if (columnName.equals("tblcPurOrderCmpTRowParticulars")) {
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


        if (columnName.equals("tblcPurOrderCmpTRowNetAmt")) {
            this.textField.setOnKeyPressed(event -> {
                if (event.getCode() == KeyCode.ENTER) {
                    commitEdit(textField.getText());
                    getTableView().getItems().addAll(new PurchaseOrderTable("", "", "1", "", "", "", "", "", "", "", "", "", "", "", "", "", "", ""));
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
        if (item != null && columnName.equals("tblcPurOrderCmpTRowParticulars")) {
            ((PurchaseOrderTable) item).setParticulars(newValue.isEmpty() ? "" : newValue);
        } else if (columnName.equals("tcBatch")) {
            (getTableRow().getItem()).setBatch_or_serial(newValue.isEmpty() ? "" : newValue);
        } else if (columnName.equals("tblcPurOrderCmpTRowQuantity")) {
            (getTableRow().getItem()).setQuantity(newValue.isEmpty() ? "" : newValue);
        } else if (columnName.equals("tblcPurOrderCmpTRowFreeQty")) {
            (getTableRow().getItem()).setFree(newValue.isEmpty() ? "0" : newValue);
        } else if (columnName.equals("tblcPurOrderCmpTRowRate")) {
            (getTableRow().getItem()).setRate(newValue.isEmpty() ? "" : newValue);
        } else if (columnName.equals("tblcPurOrderCmpTRowGrossAmt")) {
            (getTableRow().getItem()).setGross_amount(newValue.isEmpty() ? "" : newValue);
        } else if (columnName.equals("tblcPurOrderCmpTRowDisPer")) {
            (getTableRow().getItem()).setDis_per(newValue.isEmpty() ? "" : newValue);
        } else if (columnName.equals("tblcPurOrderCmpTRowDisAmt")) {
            (getTableRow().getItem()).setDis_amt(newValue.isEmpty() ? "" : newValue);
        } else if (columnName.equals("tblcPurOrderCmpTRowTaxPer")) {
            (getTableRow().getItem()).setTax(newValue.isEmpty() ? "" : newValue);
        } else if (columnName.equals("tblcPurOrderCmpTRowNetAmt")) {
            (getTableRow().getItem()).setNet_amount(newValue.isEmpty() ? "" : newValue);
        }
    }
}


class ComboBoxTableCellForPurOrderLevelA extends TableCell<PurchaseOrderTable, String> {

    String columnName;
    private final ComboBox<String> comboBox;
    ObservableList<LevelAForPurInvoice> comboList = null;

    public ComboBoxTableCellForPurOrderLevelA(String columnName) {

        this.columnName = columnName;
        this.comboBox = new ComboBox<>();

        this.comboBox.setPromptText("Select");

       /* comboBox.setPrefHeight(38);
        comboBox.setMaxHeight(38);
        comboBox.setMinHeight(38);
        comboBox.setPrefWidth(139);
        comboBox.setMaxWidth(139);
        comboBox.setMinWidth(84);*/

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
        TableRow<PurchaseOrderTable> row = getTableRow();
        if (row != null) {
            PurchaseOrderTable item = row.getItem();
            if (item != null) {
                item.setLevelA(newValue);
            }
        }
    }

}

class ComboBoxTableCellForPurOrderLevelB extends TableCell<PurchaseOrderTable, String> {

    String columnName;
    private final ComboBox<String> comboBox;
    ObservableList<LevelBForPurInvoice> comboList = null;

    public ComboBoxTableCellForPurOrderLevelB(String columnName) {
        this.columnName = columnName;
        this.comboBox = new ComboBox<>();

        this.comboBox.setPromptText("Select");

        /*comboBox.setPrefHeight(38);
        comboBox.setMaxHeight(38);
        comboBox.setMinHeight(38);
        comboBox.setPrefWidth(139);
        comboBox.setMaxWidth(139);
        comboBox.setMinWidth(84);*/


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

class ComboBoxTableCellForPurOrderLevelC extends TableCell<PurchaseOrderTable, String> {

    String columnName;
    private final ComboBox<String> comboBox;
    ObservableList<LevelAForPurInvoice> comboList = null;

    public ComboBoxTableCellForPurOrderLevelC(String columnName) {


        this.columnName = columnName;
        this.comboBox = new ComboBox<>();

        this.comboBox.setPromptText("Select");

        /*comboBox.setPrefHeight(38);
        comboBox.setMaxHeight(38);
        comboBox.setMinHeight(38);

        comboBox.setPrefWidth(139);
        comboBox.setMaxWidth(139);
        comboBox.setMinWidth(84);*/

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

class ComboBoxTableCellForSalesOrderUnit extends TableCell<PurchaseOrderTable, String> {

    String columnName;
    private final ComboBox<String> comboBox;
    TableCellCallback<Integer> unit_callback;//new

    public ComboBoxTableCellForSalesOrderUnit(String columnName, TableCellCallback<Integer> unit_callback) {


        this.columnName = columnName;
        this.comboBox = new ComboBox<>();
        this.unit_callback = unit_callback;//new

        this.comboBox.setPromptText("Select");

        /*comboBox.setPrefHeight(38);
        comboBox.setMaxHeight(38);
        comboBox.setMinHeight(38);
        comboBox.setPrefWidth(180);
        comboBox.setMaxWidth(180);
        comboBox.setMinWidth(90);*/

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

        this.comboBox.setFocusTraversable(false);


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
            System.out.println("comboAction Unit -> " + selectedItem);
            if (selectedItem != null) {
                if (PurInvoiceCommunicator.unitForPurInvoiceList != null && !PurInvoiceCommunicator.unitForPurInvoiceList.isEmpty()) {
                    for (UnitForPurInvoice unitForPurInvoice : PurInvoiceCommunicator.unitForPurInvoiceList.get(getTableRow().getIndex())) {
                        if (selectedItem.equals(unitForPurInvoice.getLabel())) {
                            int index = getTableRow().getIndex();
                            getTableRow().getItem().setUnitName(unitForPurInvoice.getLabel());
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
            if (event.isShiftDown() && event.getCode() == KeyCode.TAB) {
                TableColumn<PurchaseOrderTable, ?> colName = getTableView().getColumns().get(1);
                getTableView().edit(getIndex(), colName);
            }

            if (event.getCode() == KeyCode.ENTER || !event.isShiftDown() && event.getCode() == KeyCode.TAB) {
                TableColumn<PurchaseOrderTable, ?> colName = getTableView().getColumns().get(7);
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
            System.out.println("item ->" + item);
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

//class ComboBoxTableCellForPurOrderLevelB extends TableCell<PurchaseOrderTable, String> {
//
//    String columnName;
//    private final ComboBox<String> comboBox;
//    ObservableList<LevelBForPurInvoice> comboList = null;
//
//    public ComboBoxTableCellForPurOrderLevelB(String columnName) {
//        this.columnName = columnName;
//        this.comboBox = new ComboBox<>();
//
//        this.comboBox.setPromptText("Select");
//
//        comboBox.setPrefHeight(38);
//        comboBox.setMaxHeight(38);
//        comboBox.setMinHeight(38);
//
//        this.comboBox.setStyle("-fx-background-color: #f6f6f9; -fx-border-radius: 0px; -fx-border-width: 1; -fx-border-color: #f6f6f9;");
//
//        this.comboBox.focusedProperty().addListener((obs, wasFocused, isNowFocused) -> {
//            if (isNowFocused) {
//                this.comboBox.setStyle("-fx-background-color: #fff9c4; -fx-border-radius: 0px; -fx-border-width: 1.5; -fx-border-color: #00a0f5;");
//            } else {
//                this.comboBox.setStyle("-fx-background-color: #f6f6f9; -fx-border-radius: 0px; -fx-border-width: 1.5; -fx-border-color: #f6f6f9;");
//            }
//        });
//
//        this.comboBox.hoverProperty().addListener((obs, wasHovered, isNowHovered) -> {
//            if (isNowHovered && !comboBox.isFocused()) {
//                this.comboBox.setStyle("-fx-background-color: #f6f6f9; -fx-border-radius: 0px; -fx-border-width: 1; -fx-border-color:   #0099ff;");
//
//            } else if (!comboBox.isFocused()) {
//                this.comboBox.setStyle("-fx-background-color: #f6f6f9; -fx-border-radius: 0px; -fx-border-width: 1; -fx-border-color: #f6f6f9;");
//            }
//        });
//
//        if (comboList != null) {
//            for (LevelBForPurInvoice commonDTO : comboList) {
//                this.comboBox.getItems().add(commonDTO.getLabel());
//            }
//        }
//        this.comboBox.setFocusTraversable(true);
//        AutoCompleteBox autoCompleteBox1 = new AutoCompleteBox<>(this.comboBox, -1);
//
//        this.comboBox.focusedProperty().addListener((obs, oldVal, newVal) -> {
//            if (!newVal) {
//                commitEdit(String.valueOf(comboBox.getValue()));
//            }
//        });
//
//        comBoAction();
//    }
//
//    public void comBoAction() {
//        comboBox.setOnAction(e -> {
//            String selectedItem = comboBox.getSelectionModel().getSelectedItem();
//
//            if (selectedItem != null) {
//                if (PurInvoiceCommunicator.levelBForPurInvoiceObservableList != null && !PurInvoiceCommunicator.levelBForPurInvoiceObservableList.isEmpty()) {
//                    for (LevelBForPurInvoice levelBForPurInvoice : PurInvoiceCommunicator.levelBForPurInvoiceObservableList.get(getTableRow().getIndex())) {
//                        if (selectedItem.equals(levelBForPurInvoice.getLabel())) {
//
//                            getTableRow().getItem().setLevelB_id(levelBForPurInvoice.getValue());
//
//                            ObservableList<LevelCForPurInvoice> observableLevelAList = FXCollections.observableArrayList(levelBForPurInvoice.getLevelCOpts());
//
//                            int index = getTableRow().getIndex();
//
//
//                            if (index >= 0 && index < PurInvoiceCommunicator.levelCForPurInvoiceObservableList.size()) {
//                                PurInvoiceCommunicator.levelCForPurInvoiceObservableList.set(index, observableLevelAList);
//                                getTableRow().getItem().setLevelC(null);
//
//                                List<LevelCForPurInvoice> levelCOpts = levelBForPurInvoice.getLevelCOpts();
//                                if (!levelCOpts.isEmpty()) {
//                                    getTableRow().getItem().setLevelC(levelCOpts.get(0).getLabel());
//                                }
//
//                            } else {
//                                PurInvoiceCommunicator.levelCForPurInvoiceObservableList.add(observableLevelAList);
//                                getTableRow().getItem().setLevelC(null);
//                                getTableRow().getItem().setLevelC(levelBForPurInvoice.getLevelCOpts().get(0).getLabel());
//                            }
//
//                        }
//                    }
//                }
//
//            }
//
//        });
//    }
//
//    @Override
//    public void startEdit() {
//        super.startEdit();
//        setText(null);
//        setGraphic(new HBox(comboBox));
//        comboBox.requestFocus();
//    }
//
//    @Override
//    public void cancelEdit() {
//        super.cancelEdit();
//        setText((String) getItem());
//        setGraphic(null);
//    }
//
//    @Override
//    protected void updateItem(String item, boolean empty) {
//        super.updateItem(item, empty);
//        if (empty) {
//            setGraphic(null);
//        } else {
//
//
//            comboBox.getItems().clear();
//
//            if (item != null) {
//
//                Platform.runLater(() -> {
//                    int i = getTableRow().getIndex();
//                    if (i >= 0 && i < PurInvoiceCommunicator.levelBForPurInvoiceObservableList.size()) {
//                        for (LevelBForPurInvoice levelBForPurInvoice : PurInvoiceCommunicator.levelBForPurInvoiceObservableList.get(i)) {
//                            comboBox.getItems().add(levelBForPurInvoice.getLabel());
//                        }
//                        String itemToSet = item;
//                        if (comboBox.getItems().contains(itemToSet)) {
//                            comboBox.setValue(itemToSet);
//                        }
//                    }
//                });
//            }
//
//
//            HBox hbox = new HBox();
//            hbox.getChildren().addAll(comboBox);
//            hbox.setSpacing(5);
//
//            setGraphic(hbox);
//        }
//    }
//
//    @Override
//    public void commitEdit(String newValue) {
//        super.commitEdit(newValue);
//        (getTableRow().getItem()).setLevelB(newValue);
//    }
//
//}
//
//class ComboBoxTableCellForPurOrderLevelC extends TableCell<PurchaseOrderTable, String> {
//
//    String columnName;
//    private final ComboBox<String> comboBox;
//    ObservableList<LevelAForPurInvoice> comboList = null;
//
//    public ComboBoxTableCellForPurOrderLevelC(String columnName) {
//
//
//        this.columnName = columnName;
//        this.comboBox = new ComboBox<>();
//
//        this.comboBox.setPromptText("Select");
//
//        comboBox.setPrefHeight(38);
//        comboBox.setMaxHeight(38);
//        comboBox.setMinHeight(38);
//
//        this.comboBox.setStyle("-fx-background-color: #f6f6f9; -fx-border-radius: 0px; -fx-border-width: 1; -fx-border-color: #f6f6f9;");
//
//        this.comboBox.focusedProperty().addListener((obs, wasFocused, isNowFocused) -> {
//            if (isNowFocused) {
//                this.comboBox.setStyle("-fx-background-color: #fff9c4; -fx-border-radius: 0px; -fx-border-width: 1.5; -fx-border-color: #00a0f5;");
//            } else {
//                this.comboBox.setStyle("-fx-background-color: #f6f6f9; -fx-border-radius: 0px; -fx-border-width: 1.5; -fx-border-color: #f6f6f9;");
//            }
//        });
//
//        this.comboBox.hoverProperty().addListener((obs, wasHovered, isNowHovered) -> {
//            if (isNowHovered && !comboBox.isFocused()) {
//                this.comboBox.setStyle("-fx-background-color: #f6f6f9; -fx-border-radius: 0px; -fx-border-width: 1; -fx-border-color:   #0099ff;");
//
//            } else if (!comboBox.isFocused()) {
//                this.comboBox.setStyle("-fx-background-color: #f6f6f9; -fx-border-radius: 0px; -fx-border-width: 1; -fx-border-color: #f6f6f9;");
//            }
//        });
//
//        if (comboList != null) {
//            for (LevelAForPurInvoice commonDTO : comboList) {
//                this.comboBox.getItems().add(commonDTO.getLabel());
//            }
//        }
//        this.comboBox.setFocusTraversable(true);
//        AutoCompleteBox autoCompleteBox1 = new AutoCompleteBox<>(this.comboBox, -1);
//
//        this.comboBox.focusedProperty().addListener((obs, oldVal, newVal) -> {
//            if (!newVal) {
//                commitEdit(String.valueOf(comboBox.getValue()));
//            }
//        });
//
//        comBoAction();
//
//    }
//
//    public void comBoAction() {
//        comboBox.setOnAction(e -> {
//            String selectedItem = comboBox.getSelectionModel().getSelectedItem();
//
//            if (selectedItem != null) {
//                if (PurInvoiceCommunicator.levelCForPurInvoiceObservableList != null && !PurInvoiceCommunicator.levelCForPurInvoiceObservableList.isEmpty()) {
//
//                    for (LevelCForPurInvoice levelCForPurInvoice : PurInvoiceCommunicator.levelCForPurInvoiceObservableList.get(getTableRow().getIndex())) {
//                        if (selectedItem.equals(levelCForPurInvoice.getLabel())) {
//
//                            getTableRow().getItem().setLevelC_id(levelCForPurInvoice.getValue());
//                            ObservableList<UnitForPurInvoice> unitForPurInvoiceObservableList = FXCollections.observableArrayList(levelCForPurInvoice.getUnitOpts());
//
//                            int index = getTableRow().getIndex();
//
//
//                            if (index >= 0 && index < PurInvoiceCommunicator.unitForPurInvoiceList.size()) {
//                                PurInvoiceCommunicator.unitForPurInvoiceList.set(index, unitForPurInvoiceObservableList);
//                                getTableRow().getItem().setUnit(null);
//
//                                List<UnitForPurInvoice> unitForPurInvoiceList = levelCForPurInvoice.getUnitOpts();
//                                if (!unitForPurInvoiceList.isEmpty()) {
//                                    getTableRow().getItem().setUnit(unitForPurInvoiceList.get(0).getLabel());
//                                }
//
//                            } else {
//                                PurInvoiceCommunicator.unitForPurInvoiceList.add(unitForPurInvoiceObservableList);
//                                getTableRow().getItem().setUnit(null);
//                                getTableRow().getItem().setUnit(levelCForPurInvoice.getUnitOpts().get(0).getLabel());
//                            }
//
//                        }
//                    }
//                }
//            }
//
//        });
//    }
//
//    @Override
//    public void startEdit() {
//        super.startEdit();
//        setText(null);
//        setGraphic(new HBox(comboBox));
//        comboBox.requestFocus();
//    }
//
//    @Override
//    public void cancelEdit() {
//        super.cancelEdit();
//        setText((String) getItem());
//        setGraphic(null);
//    }
//
//    @Override
//    protected void updateItem(String item, boolean empty) {
//        super.updateItem(item, empty);
//        if (empty) {
//            setGraphic(null);
//        } else {
//
//            comboBox.getItems().clear();
//
//            if (item != null) {
//
//                Platform.runLater(() -> {
//                    int i = getTableRow().getIndex();
//                    if (i >= 0 && i < PurInvoiceCommunicator.levelCForPurInvoiceObservableList.size()) {
//                        for (LevelCForPurInvoice levelCForPurInvoice : PurInvoiceCommunicator.levelCForPurInvoiceObservableList.get(i)) {
//                            comboBox.getItems().add(levelCForPurInvoice.getLabel());
//                        }
//                        String itemToSet = item;
//                        if (comboBox.getItems().contains(itemToSet)) {
//                            comboBox.setValue(itemToSet);
//                        }
//                    }
//                });
//            }
//
//
//            HBox hbox = new HBox();
//            hbox.getChildren().addAll(comboBox);
//            hbox.setSpacing(5);
//
//            setGraphic(hbox);
//        }
//    }
//
//    @Override
//    public void commitEdit(String newValue) {
//        super.commitEdit(newValue);
//        (getTableRow().getItem()).setLevelC(newValue);
//    }
//
//}
//
//class ComboBoxTableCellForSalesOrderUnit extends TableCell<PurchaseOrderTable, String> {
//
//    String columnName;
//    private final ComboBox<String> comboBox;
//
//    public ComboBoxTableCellForSalesOrderUnit(String columnName) {
//
//
//        this.columnName = columnName;
//        this.comboBox = new ComboBox<>();
//
//
//        this.comboBox.setPromptText("Select");
//
//        comboBox.setPrefHeight(38);
//        comboBox.setMaxHeight(38);
//        comboBox.setMinHeight(38);
//
//        this.comboBox.setStyle("-fx-background-color: #f6f6f9; -fx-border-radius: 0px; -fx-border-width: 1; -fx-border-color: #f6f6f9;");
//
//        this.comboBox.focusedProperty().addListener((obs, wasFocused, isNowFocused) -> {
//            if (isNowFocused) {
//                this.comboBox.setStyle("-fx-background-color: #fff9c4; -fx-border-radius: 0px; -fx-border-width: 1.5; -fx-border-color: #00a0f5;");
//            } else {
//                this.comboBox.setStyle("-fx-background-color: #f6f6f9; -fx-border-radius: 0px; -fx-border-width: 1.5; -fx-border-color: #f6f6f9;");
//            }
//        });
//
//        this.comboBox.hoverProperty().addListener((obs, wasHovered, isNowHovered) -> {
//            if (isNowHovered && !comboBox.isFocused()) {
//                this.comboBox.setStyle("-fx-background-color: #f6f6f9; -fx-border-radius: 0px; -fx-border-width: 1; -fx-border-color:   #0099ff;");
//
//            } else if (!comboBox.isFocused()) {
//                this.comboBox.setStyle("-fx-background-color: #f6f6f9; -fx-border-radius: 0px; -fx-border-width: 1; -fx-border-color: #f6f6f9;");
//            }
//        });
//
////        if (comboList != null) {
////            for (LevelAForPurInvoice commonDTO : comboList) {
////                this.comboBox.getItems().add(commonDTO.getLabel());
////            }
////        }
//        this.comboBox.setFocusTraversable(true);
//        AutoCompleteBox autoCompleteBox1 = new AutoCompleteBox<>(this.comboBox, -1);
//
//        this.comboBox.focusedProperty().addListener((obs, oldVal, newVal) -> {
//            if (!newVal) {
//                commitEdit(String.valueOf(comboBox.getValue()));
//            }
//        });
//
//        comBoAction();
//
//    }
//
//    public void comBoAction() {
//        comboBox.setOnAction(e -> {
//            String selectedItem = comboBox.getSelectionModel().getSelectedItem();
//
//            if (selectedItem != null) {
//                if (PurInvoiceCommunicator.unitForPurInvoiceList != null && !PurInvoiceCommunicator.unitForPurInvoiceList.isEmpty()) {
//
//                    for (UnitForPurInvoice unitForPurInvoice : PurInvoiceCommunicator.unitForPurInvoiceList.get(getTableRow().getIndex())) {
//                        if (selectedItem.equals(unitForPurInvoice.getLabel())) {
//                            int index = getTableRow().getIndex();
//                            getTableRow().getItem().setUnit_id(unitForPurInvoice.getValue());
//                            getTableRow().getItem().setUnit_conv(String.valueOf(unitForPurInvoice.getUnitConversion()));
//
//                        }
//                    }
//                }
//            }
//
//        });
//    }
//
//
//    @Override
//    public void startEdit() {
//        super.startEdit();
//        setText(null);
//        setGraphic(new HBox(comboBox));
//        comboBox.requestFocus();
//    }
//
//    @Override
//    public void cancelEdit() {
//        super.cancelEdit();
//        setText((String) getItem());
//        setGraphic(null);
//    }
//
//    @Override
//    protected void updateItem(String item, boolean empty) {
//        super.updateItem(item, empty);
//        if (empty) {
//            setGraphic(null);
//        } else {
//
//            comboBox.getItems().clear();
//
//            if (item != null) {
//
//                Platform.runLater(() -> {
//                    int i = getTableRow().getIndex();
//                    if (i >= 0 && i < PurInvoiceCommunicator.unitForPurInvoiceList.size()) {
//                        for (UnitForPurInvoice unitForPurInvoice : PurInvoiceCommunicator.unitForPurInvoiceList.get(i)) {
//                            comboBox.getItems().add(unitForPurInvoice.getLabel());
//                        }
//                        String itemToSet = item;
//                        if (comboBox.getItems().contains(itemToSet)) {
//                            comboBox.setValue(itemToSet);
//                        }
//                    }
//                });
//            }
//
//
//            HBox hbox = new HBox();
//            hbox.getChildren().addAll(comboBox);
//            hbox.setSpacing(5);
//
//            setGraphic(hbox);
//        }
//    }
//
//    @Override
//    public void commitEdit(String newValue) {
//        super.commitEdit(newValue);
//        (getTableRow().getItem()).setUnit(newValue);
//    }
//
//}


class DeleteButtonTableCell extends TableCell<PurchaseOrderTable, String> {
    private final Button delete;
    TableCellCallback<Object[]> callback;

    public DeleteButtonTableCell(TableCellCallback<Object[]> DelCallback) {


        this.delete = createButtonWithImage();

        callback = DelCallback;
        delete.setOnAction(actionEvent -> {
            PurchaseOrderTable table = getTableView().getItems().get(getIndex());
            Object object[] = new Object[1];
            if (callback != null) {
                object[0] = table.getDetails_id();
                callback.call(object);
            }
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

class PurchaseOrderCalculation {

    public static void rowCalculationForPurcInvoice(int rowIndex, TableView<PurchaseOrderTable> tableView, TableCellCallback<Object[]> callback) {
        PurchaseOrderTable purchaseOrderTable = tableView.getItems().get(rowIndex);
        if (!purchaseOrderTable.getQuantity().isEmpty() && Double.parseDouble(purchaseOrderTable.getQuantity()) > 0 && !purchaseOrderTable.getRate().isEmpty() && Double.parseDouble(purchaseOrderTable.getRate()) > 0) {

            String purchaseOrderTableData = purchaseOrderTable.getRate();
            System.out.println("In Row Calculation CMPTDATA=>" + purchaseOrderTableData);
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
            r_qty = purchaseOrderTable.getQuantity();
            r_rate = Double.parseDouble(purchaseOrderTable.getRate());
            if (!purchaseOrderTable.getDis_amt().isEmpty())
                r_dis_amt = Double.parseDouble(purchaseOrderTable.getDis_amt());
            if (!purchaseOrderTable.getDis_per().isEmpty())
                r_dis_per = Double.parseDouble(purchaseOrderTable.getDis_per());
            if (purchaseOrderTable.getDis_per2() != null && !purchaseOrderTable.getDis_per2().isEmpty()) {
                disPer2 = Double.parseDouble(purchaseOrderTable.getDis_per2());
            } else {
                disPer2 = 0.0;
            }
            r_tax_per = Double.parseDouble(purchaseOrderTable.getTax());
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
            /**** getting all Discount ****/

            // set all calculated above data to CMPTROW DTO
            PurchaseOrderTable selectedItem = tableView.getItems().get(rowIndex);
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
            calculateGst(tableView, callback);
        }

    }

    public static void calculateGst(TableView<PurchaseOrderTable> tableView, TableCellCallback<Object[]> callback) {


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
        System.out.println("Table Size--->" + tableView.getItems().size());
        for (PurchaseOrderTable purchaseOrderTable : tableView.getItems()) {
            if (!purchaseOrderTable.getQuantity().isEmpty() && Double.parseDouble(purchaseOrderTable.getQuantity()) > 0 && !purchaseOrderTable.getRate().isEmpty() && Double.parseDouble(purchaseOrderTable.getRate()) > 0) {

                if (Integer.parseInt(purchaseOrderTable.getProduct_id()) > 0) {
                    taxPercentage = Double.parseDouble(purchaseOrderTable.getIgst());
                    Double quantity = Double.parseDouble(purchaseOrderTable.getQuantity());
                    if (purchaseOrderTable.getFree().isEmpty()) {
                        freeQuantity = 0.0;
                    } else {
                        freeQuantity = Double.parseDouble(purchaseOrderTable.getFree());
                    }

                    // Total Calculations of each IGST, SGST, CGST
                    totalQuantity += quantity;
                    totalFreeQuantity += freeQuantity;
                    Double igst = Double.parseDouble(purchaseOrderTable.getIgst());
                    Double cgst = Double.parseDouble(purchaseOrderTable.getCgst());
                    Double sgst = Double.parseDouble(purchaseOrderTable.getSgst());
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
                    netAmount = Double.parseDouble(purchaseOrderTable.getNet_amount());
                    totalNetAmt += netAmount;
                    grossAmt = Double.parseDouble(purchaseOrderTable.getGross_amount());
                    totalGrossAmt += grossAmt;
                    taxableAmt = Double.parseDouble(purchaseOrderTable.getTaxable_amt());
                    totaltaxableAmt += taxableAmt;
                    System.out.println("Final Discount Amt--->" + purchaseOrderTable.getFinal_dis_amt());
                    disAmt = purchaseOrderTable.getFinal_dis_amt().isEmpty() ? 0.0 : Double.valueOf(purchaseOrderTable.getFinal_dis_amt());
                    totalDisAmt += disAmt;
                    taxAmt = purchaseOrderTable.getFinal_tax_amt().isEmpty() ? 0.0 : Double.valueOf(purchaseOrderTable.getFinal_tax_amt());
                    totalTaxAmt += taxAmt;

                }
            }
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


    public static void discountPropotionalCalculation(String disproportionalPer, String disproportionalAmt, String additionalCharges, TableView<PurchaseOrderTable> tableView, TableCellCallback<Object[]> callback) {

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
            rowCalculationForPurcInvoice(i, tableView, callback);//call row calculation function
            PurchaseOrderTable purchaseOrderTable = tableView.getItems().get(i);
            totalTaxableAmt = totalTaxableAmt + Double.parseDouble(purchaseOrderTable.getTaxable_amt());

        }
        System.out.println("totalTaxableAmt discountPropotionalCalculation :: " + totalTaxableAmt);

        Double rowDisc = 0.0;
        //get data of netamount, tax per,and row taxable amount from cmptrow
        for (PurchaseOrderTable purchaseOrderTable : tableView.getItems()) {
            System.out.println("getFinal_dis_amt--->" + purchaseOrderTable.getFinal_dis_amt());
            rowDisc = Double.parseDouble(purchaseOrderTable.getFinal_dis_amt());
            System.out.println("netAmt--->" + purchaseOrderTable.getNet_amount());
            netAmt = Double.parseDouble(purchaseOrderTable.getNet_amount());
            System.out.println("netAmt--->" + purchaseOrderTable.getTax());
            r_tax_per = Double.parseDouble(purchaseOrderTable.getTax());
            rowTaxableAmt = Double.parseDouble(purchaseOrderTable.getTaxable_amt());
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
                    System.out.println("total_tax_amt" + total_tax_amt);
                    netAmt = rowTaxableAmt + total_tax_amt;
                    System.out.println("total netAmt  :" + netAmt + rowTaxableAmt);
                }
            }

            //Set data to cmpTRow
            purchaseOrderTable.setNet_amount(String.format("%.2f", netAmt));
            purchaseOrderTable.setTotal_taxable_amt(String.valueOf(totalTaxableAmtAdditional));
            System.out.println("Total Tax Amt--->" + total_tax_amt);
            purchaseOrderTable.setIgst(String.valueOf(total_tax_amt));
            purchaseOrderTable.setCgst(String.valueOf(total_tax_amt / 2));
            purchaseOrderTable.setSgst(String.valueOf(total_tax_amt / 2));
            purchaseOrderTable.setFinal_tax_amt(String.valueOf(total_tax_amt));
            purchaseOrderTable.setFinal_dis_amt(String.valueOf(rowDisc));
            purchaseOrderTable.setInvoice_dis_amt(String.valueOf(rowDisPropAmt));

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


    public static void additionalChargesCalculation(String additionalCharges, TableView<PurchaseOrderTable> tableView, TableCellCallback<Object[]> callback) {


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
                PurchaseOrderTable purchaseOrderTable = tableView.getItems().get(i);
                totalTaxableAmt = totalTaxableAmt + Double.parseDouble(purchaseOrderTable.getTotal_taxable_amt());

            }
            for (PurchaseOrderTable purchaseOrderTable : tableView.getItems()) {

                netAmt = Double.parseDouble(purchaseOrderTable.getNet_amount());
                r_tax_per = Double.parseDouble(purchaseOrderTable.getTax());
                rowTaxableAmt = Double.parseDouble(purchaseOrderTable.getTotal_taxable_amt());


                totalDisPropAmt = additionalChargeAmt;
                rowDisPropAmt = (rowTaxableAmt * totalDisPropAmt) / totalTaxableAmt;
                rowTaxableAmt = rowTaxableAmt + rowDisPropAmt;

                if (r_tax_per > 0) {
                    total_tax_amt = (r_tax_per / 100) * (rowTaxableAmt);
                    netAmt = rowTaxableAmt + total_tax_amt;
                }

                purchaseOrderTable.setNet_amount(String.format("%.3f", netAmt));
                purchaseOrderTable.setCgst(String.valueOf(total_tax_amt / 2));
                purchaseOrderTable.setSgst(String.valueOf(total_tax_amt / 2));
                purchaseOrderTable.setFinal_tax_amt(String.valueOf(total_tax_amt));
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

