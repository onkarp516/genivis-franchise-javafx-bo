package com.opethic.genivis.controller.tranx_sales;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.opethic.genivis.GenivisApplication;
import com.opethic.genivis.controller.commons.OverlaysEffect;
import com.opethic.genivis.controller.dialogs.BatchWindow;
import com.opethic.genivis.controller.dialogs.SingleInputDialogs;
import com.opethic.genivis.controller.master.ledger.common.LedgerMessageConsts;
import com.opethic.genivis.controller.tranx_purchase.*;
import com.opethic.genivis.controller.tranx_sales.common.TranxCommonPopUps;
import com.opethic.genivis.dto.*;
import com.opethic.genivis.dto.cmp_t_row.BatchWindowTableDTO;
import com.opethic.genivis.dto.pur_invoice.*;
import com.opethic.genivis.dto.reqres.product.Communicator;
import com.opethic.genivis.dto.reqres.product.TableCellCallback;
import com.opethic.genivis.dto.reqres.pur_tranx.*;
import com.opethic.genivis.dto.reqres.sales_tranx.Row1;
import com.opethic.genivis.dto.reqres.sales_tranx.SalesOrderTable;
import com.opethic.genivis.dto.reqres.sales_tranx.SalesQuotEditMainDTO;
import com.opethic.genivis.models.tranx.sales.TranxSelectedProduct;
import com.opethic.genivis.network.APIClient;
import com.opethic.genivis.network.EndPoints;
import com.opethic.genivis.network.RequestType;
import com.opethic.genivis.utils.*;
import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.WorkerStateEvent;
import javafx.css.PseudoClass;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.util.StringConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;
import java.net.http.HttpResponse;
import java.security.Key;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

//import static com.opethic.genivis.sql_lite.UserToken.url;
import static com.opethic.genivis.utils.FxmFileConstants.*;
import static com.opethic.genivis.utils.Globals.decimalFormat;
import static com.opethic.genivis.utils.Globals.mapToStringforFormData;
import static com.opethic.genivis.utils.Utils.showAlert;
import static java.lang.Double.parseDouble;
import static java.lang.Long.parseLong;

public class SalesQuotationCreateController implements Initializable {
    @FXML
    private BorderPane spSalesQuoatCreateRootPane;
    String message = "", id = "", finaltotalCGST = "", finaltotalSGST = "", finaltotalIGST = "", finalroundOff = "";
    @FXML
    Label lbSalesQuotCreateBillAmount, lbSalesQuotCreateDiscount, lbSalesQuotCreateTotal, lbSalesQuotCreateGrossTotal, lbSalesQuotCreateTax;

    public static final Logger SalesQuotationLogger = LoggerFactory.getLogger(SalesQuotationCreateController.class);
    private static ObservableList<String> unitList = FXCollections.observableArrayList();

    @FXML
    TableView<SalesQuotationProductDTO> tblvSalesQuotationCmptRow;
    @FXML
    TableView tvSalesQuotCreateprodInvoiceDetails;
    @FXML
    private ComboBox<CommonDTO> cmbSalesQuotationCreateSalesAC;
    @FXML
    private ComboBox<GstDetailsDTO> cmbSalesQuotationCreateClientGST;
    @FXML
    public TabPane tpSalesQuotation;
    @FXML
    public Tab tabSalesQuotaCreateLedger, tabSalesQuotaCreateProduct;

    @FXML
    private HBox topInnerHbTwo, topInnerHbOne, sqBottomMain;
    @FXML
    private VBox totalmainDiv, sqtotalInnervbox, sqBottomFirstV, sqBottomSecondV, sqtotalInnervbox1;

    @FXML
    private Button btnSalesQuotationCreateEdit, btnSalesQuotationCreateSubmit, btnSalesQuotationCreateCancle, addRowInCmpTRow;
    private ToggleGroup toggleGroup = new ToggleGroup();
    @FXML
    public TextField tfSalesQuotationCreateNarration, tfSalesQuotationCreateLedgerName, tfSalesQuotationCreateQuotationNo, tfSalesQuotationCreateSalesSerial;

    @FXML
    TableColumn<SalesQuotationProductDTO, String> tblcSalesQuotCreateSrNo, tblcSalesQuotCreateParticular, tblcSalesQuotCreatePackage, tblcSalesQuotCreateUnit, tcSalesQuotCreateCmpTRowLevelA, tcSalesQuotCreateCmpTRowLevelB, tcSalesQuotCreateCmpTRowLevelC, tblcPurChallCmpTRowBatchNo, tblcSalesQuotCreateQty, tblcSalesQuotCreateFreeqty, tblcSalesQuotCreateRate,
            tblcSalesQuotCreateGrossAmt, tblcSalesQuotCreateDisPer, tblcSalesQuotCreateDisRs, tblcSalesQuotCreateTax, tblcSalesQuotCreateNetAmt, tcSalesQuotCreateCmpTRowActions;
    //ledgerInfo label
    @FXML
    Label lbSalesQuotationCreateledInfogstNo, lbSalesQuotationCreateledInfoArea, lbSalesQuotationCreateledInfoBank, lbSalesQuotationCreateledInfoContPerson,
            lbSalesQuotationCreateledInfoMobNo, lbSalesQuotationCreateledInfoLiceNo, lbSalesQuotationCreateledInfoRoute, lbSalesQuotationCreateledInfoCreditDays, lbSalesQuotationCreateledInfoFssai;

    //productInfo label
    @FXML
    Label lbSalesQuotationCreateProdInfoBrand, lbSalesQuotationCreateProdInfoHSN, lbSalesQuotationCreateProdInfoGroup, lbSalesQuotationCreateProdInfoCategory, lbSalesQuotationCreateProdInfoSupplier, lbSalesQuotationCreateProdInfoTaxType, lbSalesQuotationCreateProdInfoTax, lbSalesQuotationCreateProdInfoMargin, lbSalesQuotationCreateProdInfoCost,
            lbSalesQuotationCreateProdInfoShelfId, lbSalesQuotationCreateProdInfoMinStock, lbSalesQuotationCreateProdInfoMaxStock;

    //   invoice Info tableCloumn
    @FXML
    TableColumn tblcSalesQuotationCreateClienName, tblcSalesQuotationCreateInvoiceNo, tblcSalesQuotationCreateInvoiceDate, tblcSalesQuotationCreateBatch, tblcSalesQuotationCreateMrp,
            tblcSalesQuotationCreateQuantity, tblcSalesQuotationCreateRate, tblcSalesQuotationCreateCost, tblcSalesQuotationCreateDisPer, tblcSalesQuotationCreateDis;

    private String productId = "";
    private String responseBody;
    @FXML
    private TextField dpSalesQuotationCreateTranxDate, dpSalesQuotationCreateQuotationDate;

    @FXML
    private VBox vboxSalesQuotationCreateRoot;

    private String ledgerName="";
    private String ledgerId, ledgerStateCode;
    private String Debtor_id = "";
    private Integer rediCurrIndex = -1;
    private Boolean isProductRed = false;
    private Boolean isLedgerRed= false;
    private Boolean taxFlag = false;
    private Integer id1;

    private Long stateCode = 0L, CompanyStateCode = 0L;
    DecimalFormat decimalFormat = new DecimalFormat("0.#");

    private Integer SalesQuotId = -1, details_id = -1;
    public static Integer selectedRowIndex;

    public void setEditId(Integer InId) {
        SalesQuotId = InId;
        System.out.println("Sales QuotId" + SalesQuotId);
        if (InId > -1) {
            setEditData();
        }
    }

    private void setEditData() {
        getSalesQuotById(String.valueOf(SalesQuotId));
        //todo: set Calculation footer
//        calculateEachRowTraxTotal();

        System.out.println("setEditData " + Debtor_id);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        ResponsiveWiseCssPicker();

        //? this include all the Shortcut Keys
        initShortcutKeys();

        //? this inculde all the design related properties and important Fields
        mandatoryFields();

        //? Quotation date set to current date in normal date format
        //? Set default value to current local date
        LocalDate tranxDate = LocalDate.now();
        LocalDate QuotDate = LocalDate.now();
        dpSalesQuotationCreateTranxDate.setText(tranxDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        dpSalesQuotationCreateQuotationDate.setText(QuotDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        //? DatePicker to TextField with validation
        DateValidator.applyDateFormat(dpSalesQuotationCreateTranxDate);
        DateValidator.applyDateFormat(dpSalesQuotationCreateQuotationDate);
        sceneInitilization();


        //        SalesQuotation Create Controller
        SalesQuotationLogger.info("Start of Initialize method of :SalesQuotationController");
//        btnSalesQuotationCreateEdit.setOnKeyPressed(event -> {
//
//        });

//        btnSalesQuotationCreateSubmit.addEventFilter(KeyEvent.KEY_PRESSED, event->{
//            if(btnSalesQuotationCreateSubmit.isFocused() && event.getCode() == KeyCode.ENTER){
//                event.consume();
//                createSalesQuotation();
//            }
//        });

        System.out.println("SalesQuotId for Update" + SalesQuotId);

        //todo: get the Serial No and the Order No
        getSalesQuotationSerialNo();

//        //todo:Transaction date set to current date in normal date format
//        dpSalesQuotationCreateTranxDate.setConverter(new com.opethic.genivis.controller.tranx_purchase.DateFormat());
//        dpSalesQuotationCreateTranxDate.setValue(LocalDate.now());

        getSalesAccounts();

        //todo:open GST dropdown on Space
        cmbSalesQuotationCreateClientGST.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode() == KeyCode.SPACE && !cmbSalesQuotationCreateClientGST.isShowing()) {
                cmbSalesQuotationCreateClientGST.show();
                event.consume(); // Consume the event to prevent other actions
            }
        });

        //todo: Responsive code for tableview
        responsiveTable();
        tblvSalesQuotationCmptRow.setFocusTraversable(false);
        //todo: Responsive code for bottom invoice Product detais tableview
        responsiveprodInfoTable();
        tvSalesQuotCreateprodInvoiceDetails.setFocusTraversable(false);

        //todo:Open Ledger Modal
        //todo:On Clicking on the Ledger Name TextField and Select the Ledger from the Modal
        SalesQuotationLogger.info("open Ledger window onClick on Ledger name  :SalesQuotationCreateController");
        Platform.runLater(() -> {
            tfSalesQuotationCreateLedgerName.requestFocus();

            //todo: set the TranxDate Serial No TextField to Readonly
            tfSalesQuotationCreateSalesSerial.setDisable(true);
            tfSalesQuotationCreateSalesSerial.setStyle("-fx-opacity: 1;"); // Make textfield fully visible
            tfSalesQuotationCreateSalesSerial.pseudoClassStateChanged(PseudoClass.getPseudoClass("disabled"), false);

            dpSalesQuotationCreateTranxDate.setDisable(true);
            dpSalesQuotationCreateTranxDate.setStyle("-fx-opacity: 1;"); // Make textfield fully visible
            dpSalesQuotationCreateTranxDate.pseudoClassStateChanged(PseudoClass.getPseudoClass("disabled"), false);

            //? Open Ledger Modal on Space and on Click and on Ket Typed
            //? Mouse click event listener
            tfSalesQuotationCreateLedgerName.setOnMouseClicked(actionEvent -> openLedgerModal());

            //? Key pressed event listener
            tfSalesQuotationCreateLedgerName.setOnKeyPressed(keyEvent -> {
                if (keyEvent.getCode() == KeyCode.SPACE || keyEvent.getCode() == KeyCode.ENTER || keyEvent.getCode() == KeyCode.TAB) {
                    openLedgerModal();
                }
            });

            //? Key pressed event listener
//            tfSalesQuotationCreateLedgerName.setOnKeyTyped(keyEvent -> {
//                openLedgerModal();
//            });
        });


        //todo : submit sales quotation
//        Platform.runLater(() -> btnSalesQuotationCreateSubmit.setOnAction(actionEvent -> {
//            String btnText = btnSalesQuotationCreateSubmit.getText();
//            AlertUtility.AlertConfirmation(AlertUtility.alertTypeConfirmation, "Do you want to Submit", input -> {
//                if (input == 1) {
//                    if (CommonValidationsUtils.validateForm(tfSalesQuotationCreateLedgerName, tfSalesQuotationCreateQuotationNo, dpSalesQuotationCreateQuotationDate)) {
//                        createSalesQuotation();
//                    }
//                }
//            });
//        }));
        btnSalesQuotationCreateSubmit.setOnAction(actionEvent -> {
            if (CommonValidationsUtils.validateForm(tfSalesQuotationCreateLedgerName, tfSalesQuotationCreateQuotationNo, dpSalesQuotationCreateQuotationDate)) {
                createSalesQuotation();
            }
        });
        btnSalesQuotationCreateCancle.setOnAction(actionEvent -> {
            if(SalesQuotId >0){
                AlertUtility.AlertConfirmation(AlertUtility.alertTypeConfirmation, "Are you sure want to Cancel and go to List ?", input -> {
                    if (input == 1) {
                        SalesQuotationListController.focusStatus = "edit";
                        SalesQuotationListController.selectedRowIndex = selectedRowIndex;
                        GlobalController.getInstance().addTabStatic(SALES_QUOTATION_LIST_SLUG, false);
                    }
                });

            }
            else{
                AlertUtility.AlertConfirmation(AlertUtility.alertTypeConfirmation, "Are you sure want to Cancel ?", input -> {
                    if (input == 1) {
                        clearLedAndProdInfo();
                        initialize(url, resourceBundle);
                    }
                });
            }
//            AlertUtility.AlertConfirmation(AlertUtility.alertTypeConfirmation, LedgerMessageConsts.msgGoList, input -> {
//                if (input == 1) {
//                    clearLedAndProdInfo();
//                    initialize(url, resourceBundle);
//                }
//            });
        });
        //todo: go to List Page onClick on Modify
//        btnSalesQuotationCreateEdit.setOnAction(event -> {
//            AlertUtility.AlertConfirmation(AlertUtility.alertTypeConfirmation, "Do you want to Cancel", input -> {
//                if (input == 1) {

//                    if (selectedRowIndex != null && !selectedRowIndex.equals("")) {
//                        SalesQuotationListController.focusStatus = "edit";
//                        SalesQuotationListController.selectedRowIndex = selectedRowIndex;
//                    } else {
//                        SalesQuotationListController.focusStatus = "cancle";
//                    }
//                    GlobalController.getInstance().addTabStatic(SALES_QUOTATION_LIST_SLUG, false);
//                }
//            });
//
//        });
        tableInitiliazation();
        columnVisibility(tcSalesQuotCreateCmpTRowLevelA, Globals.getUserControlsWithSlug("is_level_a"));
        columnVisibility(tcSalesQuotCreateCmpTRowLevelB, Globals.getUserControlsWithSlug("is_level_b"));
        columnVisibility(tcSalesQuotCreateCmpTRowLevelC, Globals.getUserControlsWithSlug("is_level_c"));
        //! Free Qty
        columnVisibility(tblcSalesQuotCreateFreeqty, Globals.getUserControlsWithSlug("is_free_qty"));

        //todo:clear all and refresh page on Submit
//        btnSalesQuotationCreateCancle.setOnAction(event -> {
//            AlertUtility.AlertConfirmation(AlertUtility.alertTypeConfirmation, "Are you sure want to Cancel ?", input -> {
//                if (input == 1) {
//                    clearLedAndProdInfo();
//                    initialize(url, resourceBundle);
//                }
//            });
//        });
//        shortcutKeysSalesQuot();


    }

    /**
     * Info : This Method to off the Visibility of the LevelA, LevelB, LevelC Columns.
     */
    private void columnVisibility(TableColumn<SalesQuotationProductDTO, String> column, boolean visible) {
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
//    private void columnVisibility(TableColumn<SalesQuotationProductDTO, String> column, boolean visible) {
//        if (visible) {
////            column.setPrefWidth(USE_COMPUTED_SIZE);
////            column.setMinWidth(USE_PREF_SIZE);
//            column.setMaxWidth(Double.MAX_VALUE);
//        } else {
//            column.prefWidthProperty().unbind();
//            column.setPrefWidth(0);
//            column.setMinWidth(0);
//            column.setMaxWidth(0);
//        }
//    }

    private void initShortcutKeys() {
        //         Enter traversal
        spSalesQuoatCreateRootPane.addEventFilter(KeyEvent.KEY_PRESSED, (KeyEvent event) -> {
            if (event.getCode() == KeyCode.ENTER) {
                if (event.getTarget() instanceof Button targetButton && targetButton.getText().equalsIgnoreCase("Modify")) {
                } else if (event.getTarget() instanceof Button targetButton && targetButton.getText().equalsIgnoreCase("Submit")) {
                } else if (event.getTarget() instanceof Button targetButton && targetButton.getText().equalsIgnoreCase("Update")) {
                } else if (event.getTarget() instanceof Button targetButton && targetButton.getText().equalsIgnoreCase("Cancel")) {
                } else {
                    if (tfSalesQuotationCreateQuotationNo.getText() != "") {
                        if (SalesQuotId > -1) {
                            validateQuotationNoInEdit();
                        }
//
                        else
                            validateQuotationNo();
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
                if (tfSalesQuotationCreateQuotationNo.getText() != "") {
                    if (SalesQuotId > -1) {
                        validateQuotationNoInEdit();
                    } else
                        validateQuotationNo();
                }
            } else if (event.getCode() == KeyCode.S && event.isControlDown()) {
                btnSalesQuotationCreateSubmit.fire();
            } else if (event.getCode() == KeyCode.X && event.isControlDown()) {
                if (SalesQuotId > -1) {
                    btnSalesQuotationCreateCancle.fire();
                } else {
                    btnSalesQuotationCreateCancle.fire();
                }
            } else if (event.getCode() == KeyCode.E && event.isControlDown()) {
                btnSalesQuotationCreateEdit.fire();
            }
        });
    }

    //? On Clicking on the Modify Button From Create or Edit page redirect Back to List page
    public void backToListModify() {
        AlertUtility.AlertConfirmation(AlertUtility.alertTypeConfirmation, "Are you sure want to go list ?", input -> {
            if (input == 1) {
                GlobalController.getInstance().addTabStatic(SALES_QUOTATION_LIST_SLUG, false);
            }
        });
    }

    private void mandatoryFields() {

        tblvSalesQuotationCmptRow.setFocusTraversable(false);
        tvSalesQuotCreateprodInvoiceDetails.setFocusTraversable(false);

        CommonValidationsUtils.changeStarColour(vboxSalesQuotationCreateRoot);
        CommonFunctionalUtils.restrictTextFieldToDigitsAndDateFormat(dpSalesQuotationCreateTranxDate);
        CommonFunctionalUtils.tranxDateGreaterThanCurrentDate(dpSalesQuotationCreateQuotationDate);
        CommonValidationsUtils.comboBoxDataShow(cmbSalesQuotationCreateClientGST);
        CommonValidationsUtils.comboBoxDataShow(cmbSalesQuotationCreateSalesAC);

        //Stop the Cusor on the Field
        tfSalesQuotationCreateLedgerName.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal) {
                String text = tfSalesQuotationCreateLedgerName.getText().trim();
                if (text.isEmpty()) {
                    tfSalesQuotationCreateLedgerName.requestFocus();
                }
            }
        });
        tfSalesQuotationCreateQuotationNo.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal) {
                String text = tfSalesQuotationCreateQuotationNo.getText().trim();
                if (text.isEmpty()) {
                    tfSalesQuotationCreateQuotationNo.requestFocus();
                }
            }
        });
        dpSalesQuotationCreateQuotationDate.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal) {
                String text = dpSalesQuotationCreateQuotationDate.getText().trim();
                if (text.isEmpty()) {
                    dpSalesQuotationCreateQuotationDate.requestFocus();
                }
            }
        });
    }

    private void openLedgerModal() {
        Stage stage = (Stage) spSalesQuoatCreateRootPane.getScene().getWindow();

        //Pass LedgerId to LedgerPopup for Focus in open ledgerPopup on Click
        if (Debtor_id != "" && !tfSalesQuotationCreateLedgerName.getText().isEmpty()) {
            SingleInputDialogs.ledId = Debtor_id;
        } else {
            SingleInputDialogs.ledId = "";
        }
        SingleInputDialogs.openSalesLedgerWithNamePopUp(stage, "Filter", ledgerName, input -> {
                    //todo:Callback to Get all the Details of the Selected ledger and Set it in the Fields
                    ledgerName = (String) input[0];
                    ledgerId = (String) input[1];
//                stateCode = (Long) input[3];
                    ledgerStateCode = (String) input[6];//new
                    System.out.println("ledger selection  state_code" + ledgerStateCode);
                    Debtor_id = ledgerId;

                    ObservableList<GstDetailsDTO> gstDetails = (ObservableList<GstDetailsDTO>) input[2];
                    tfSalesQuotationCreateLedgerName.setText(ledgerName);
                    cmbSalesQuotationCreateClientGST.requestFocus();
                    if (gstDetails != null) {
                        setLedgerGSTDATA(gstDetails);

                    }
                    fetchSelectedLedgerData(ledgerId);
                    System.out.println("done1");

                }, in -> {
                    System.out.println("In >> Called >. " + in);
                    if (in == true) {
                        isLedgerRed = true;
                        setPurChallDataToProduct();
                    }
                }

        );
    }
    public void setPurChallDataToProduct() {

        System.out.println("ledgerId For Edit PurPose >> " + ledgerId);
        String tranxDate = Communicator.text_to_date.fromString(dpSalesQuotationCreateTranxDate.getText()).toString();
        String ledgrId = ledgerId;
        String gstNum = cmbSalesQuotationCreateClientGST.getId();
        String purSerNum = tfSalesQuotationCreateSalesSerial.getText();
        String challanNo = tfSalesQuotationCreateQuotationNo.getText();
        String purAcc = cmbSalesQuotationCreateSalesAC.getId();
        String challanDate = Communicator.text_to_date.fromString(dpSalesQuotationCreateQuotationDate.getText()).toString();
        System.out.println("ledgerId ddd" + ledgerId);

        PurTranxToProductRedirectionDTO mData = new PurTranxToProductRedirectionDTO();
        mData.setRedirect(FxmFileConstants.SALES_QUOTATION_CREATE_SLUG);
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

        mData.setPurChallEditId(SalesQuotId);
        mData.setRedirection(true);


        mData.setRediPrdCurrIndex(rediCurrIndex);
        List<SalesQuotationProductDTO> currentItems = new ArrayList<>(tblvSalesQuotationCmptRow.getItems());
        List<SalesQuotationProductDTO> list = new ArrayList<>(currentItems);

        mData.setRowSaleQoq(list);

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
        String tranxDate = Communicator.text_to_date.fromString(dpSalesQuotationCreateTranxDate.getText()).toString();
        String ledgrId = ledgerId;
        String gstNum = cmbSalesQuotationCreateClientGST.getId();
        String purSerNum = tfSalesQuotationCreateSalesSerial.getText();
        String challanNo = tfSalesQuotationCreateQuotationNo.getText();
        String purAcc = cmbSalesQuotationCreateSalesAC.getId();
        String challanDate = Communicator.text_to_date.fromString(dpSalesQuotationCreateQuotationDate.getText()).toString();


        tfSalesQuotationCreateQuotationNo.setText(franchiseDTO.getChallanNo());
        ledgerId=franchiseDTO.getLedegrId();
        tfSalesQuotationCreateSalesSerial.setText(franchiseDTO.getPurSerNum());
        tfSalesQuotationCreateLedgerName.setText(ledgersById(Long.valueOf(ledgerId)));
        int cnt=0,index=0;
        tblvSalesQuotationCmptRow.getItems().clear();
        PurInvoiceCommunicator.levelAForPurInvoiceObservableList.clear();
        if(franchiseDTO.getProductRed()==true) {
            Long productId = Long.valueOf(franchiseDTO.getRedirectProductId());
            int rindex = franchiseDTO.getRediPrdCurrIndex();
            JsonObject obj = productRedirectById(productId);

            for (SalesQuotationProductDTO purchaseInvoiceTable : franchiseDTO.getRowSaleQoq()) {
                if(index==rindex){
                    purchaseInvoiceTable.setProduct_id(productId+"");
                    purchaseInvoiceTable.setParticulars(obj.get("productName").getAsString());
                    purchaseInvoiceTable.setPacking(obj.get("package").getAsString());
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
                tblvSalesQuotationCmptRow.getItems().addAll(purchaseInvoiceTable);
                salesQuotationCalculation.rowCalculationForPurcInvoice(index, tblvSalesQuotationCmptRow, callback);
                salesQuotationCalculation.calculateGst(tblvSalesQuotationCmptRow, callback);
//                calculateGst(tableView, callback);

                List<LevelAForPurInvoice> levelAForPurInvoiceList = com.opethic.genivis.controller.tranx_sales.ProductUnitsPacking.getAllProductUnitsPackingFlavour(purchaseInvoiceTable.getProduct_id());
                ObservableList<LevelAForPurInvoice> observableLevelAList = FXCollections.observableArrayList(levelAForPurInvoiceList);
                if (index >= 0 && index < PurInvoiceCommunicator.levelAForPurInvoiceObservableList.size()) {
                    PurInvoiceCommunicator.levelAForPurInvoiceObservableList.set(index, observableLevelAList);

                    tblvSalesQuotationCmptRow.getItems().get(index).setLevelA(null);
                    tblvSalesQuotationCmptRow.getItems().get(index).setLevelA(levelAForPurInvoiceList.get(0).getLabel());

                    for (LevelAForPurInvoice levelAForPurInvoice : PurInvoiceCommunicator.levelAForPurInvoiceObservableList.get(index)) {
                        if (tblvSalesQuotationCmptRow.getItems().get(index).getLevel_a_id().equals(levelAForPurInvoice.getValue())) {
                            tblvSalesQuotationCmptRow.getItems().get(index).setLevelA(null);
                            tblvSalesQuotationCmptRow.getItems().get(index).setLevelA(levelAForPurInvoice.getLabel());
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
            for (SalesQuotationProductDTO rowData : franchiseDTO.getRowSaleQoq()) {


                System.out.println("String.valueOf(rowData.getRowData().get(0))" + rowData.getProduct_id());
//                mData1.setProduct_id(rowData.getProduct_id());
//                mData1.setParticulars(rowData.getParticulars());
//                mData1.setPackages(rowData.getPackages());

//                calculateGst(tableView, callback);

                List<LevelAForPurInvoice> levelAForPurInvoiceList = com.opethic.genivis.controller.tranx_sales.ProductUnitsPacking.getAllProductUnitsPackingFlavour(rowData.getProduct_id());
                ObservableList<LevelAForPurInvoice> observableLevelAList = FXCollections.observableArrayList(levelAForPurInvoiceList);
//                if (index >= 0 && index < PurInvoiceCommunicator.levelAForPurInvoiceObservableList.size()) {
//                    PurInvoiceCommunicator.levelAForPurInvoiceObservableList.set(index, observableLevelAList);
//
//                    tblvSalesQuotationCmptRow.getItems().get(index).setLevelA(null);
//                    tblvSalesQuotationCmptRow.getItems().get(index).setLevelA(levelAForPurInvoiceList.get(0).getLabel());
//
//                    for (LevelAForPurInvoice levelAForPurInvoice : PurInvoiceCommunicator.levelAForPurInvoiceObservableList.get(index)) {
//                        if (tblvSalesQuotationCmptRow.getItems().get(index).getLevel_a_id().equals(levelAForPurInvoice.getValue())) {
//                            tblvSalesQuotationCmptRow.getItems().get(index).setLevelA(null);
//                            tblvSalesQuotationCmptRow.getItems().get(index).setLevelA(levelAForPurInvoice.getLabel());
//                        }
//                    }
//
//                } else {
//                    System.out.println("Index else >> : " + index);
//                    PurInvoiceCommunicator.levelAForPurInvoiceObservableList.add(observableLevelAList);
//                }
//                List<LevelAForPurInvoice> levelAForPurInvoiceList = ProductUnitsPacking.getAllProductUnitsPackingFlavour(rowData.getProduct_id());
//                ObservableList<LevelAForPurInvoice> observableLevelAList = FXCollections.observableArrayList(levelAForPurInvoiceList);
                if (index >= 0 && index < PurInvoiceCommunicator.levelAForPurInvoiceObservableList.size()) {
                    PurInvoiceCommunicator.levelAForPurInvoiceObservableList.set(index, observableLevelAList);

                    tblvSalesQuotationCmptRow.getItems().get(index).setLevelA(null);
                    tblvSalesQuotationCmptRow.getItems().get(index).setLevelA(levelAForPurInvoiceList.get(0).getLabel());

                    for (LevelAForPurInvoice levelAForPurInvoice : PurInvoiceCommunicator.levelAForPurInvoiceObservableList.get(index)) {
                        if (tblvSalesQuotationCmptRow.getItems().get(index).getLevel_a_id().equals(levelAForPurInvoice.getValue())) {
                            tblvSalesQuotationCmptRow.getItems().get(index).setLevelA(null);
                            tblvSalesQuotationCmptRow.getItems().get(index).setLevelA(levelAForPurInvoice.getLabel());
                        }
                    }

                } else {
                    System.out.println("Index else >> : " + index);
                    PurInvoiceCommunicator.levelAForPurInvoiceObservableList.add(observableLevelAList);
                }
                tblvSalesQuotationCmptRow.getItems().add(rowData);
                salesQuotationCalculation.rowCalculationForPurcInvoice(index, tblvSalesQuotationCmptRow, callback);
                salesQuotationCalculation.calculateGst(tblvSalesQuotationCmptRow, callback);
                inx++;
            }
        }

        Platform.runLater(() -> {
            TableColumn<SalesQuotationProductDTO, ?> colName = tblvSalesQuotationCmptRow.getColumns().get(1);
            tblvSalesQuotationCmptRow.edit(redirectIndex, colName);
        });


    }
    public String ledgersById(Long id) {
        System.out.println("id---ledger" + id);

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

    private void ResponsiveWiseCssPicker() {
        double height = Screen.getPrimary().getBounds().getHeight();
        double width = Screen.getPrimary().getBounds().getWidth();
        System.out.println("width" + width);
        if (width >= 992 && width <= 1024) {
            topInnerHbOne.setSpacing(8);
            topInnerHbTwo.setSpacing(8);
            totalmainDiv.setSpacing(10);
            sqtotalInnervbox.setSpacing(10);
            sqtotalInnervbox1.setSpacing(10);
            // Set preferred widths as percentages of the HBox width
            sqBottomFirstV.prefWidthProperty().bind(sqBottomMain.widthProperty().multiply(0.76));
            sqBottomSecondV.prefWidthProperty().bind(sqBottomMain.widthProperty().multiply(0.24));
//            sqBottomMain.setPrefHeight(260);
            tvSalesQuotCreateprodInvoiceDetails.getStylesheets().add(GenivisApplication.class.getResource("ui/css/purchase_challan_css/supplier_table_css/supplier_table_992_1024.css").toExternalForm());
            spSalesQuoatCreateRootPane.getStylesheets().add(GenivisApplication.class.getResource("ui/css/TranxCommonCssStyles/tranxCommonCssStyle1.css").toExternalForm());
        } else if (width >= 1025 && width <= 1280) {
            topInnerHbOne.setSpacing(8);
            topInnerHbTwo.setSpacing(8);
            totalmainDiv.setSpacing(10);
            sqtotalInnervbox.setSpacing(10);
            sqtotalInnervbox1.setSpacing(10);
            // Set preferred widths as percentages of the HBox width
            sqBottomFirstV.prefWidthProperty().bind(sqBottomMain.widthProperty().multiply(0.76));
            sqBottomSecondV.prefWidthProperty().bind(sqBottomMain.widthProperty().multiply(0.24));
//            sqBottomMain.setPrefHeight(260);
            tvSalesQuotCreateprodInvoiceDetails.getStylesheets().add(GenivisApplication.class.getResource("ui/css/purchase_challan_css/supplier_table_css/supplier_table_1025_1280.css").toExternalForm());
            spSalesQuoatCreateRootPane.getStylesheets().add(GenivisApplication.class.getResource("ui/css/TranxCommonCssStyles/tranxCommonCssStyle2.css").toExternalForm());
        } else if (width >= 1281 && width <= 1368) {
            topInnerHbOne.setSpacing(8);
            topInnerHbTwo.setSpacing(8);
            totalmainDiv.setSpacing(10);
            sqtotalInnervbox.setSpacing(10);
            sqtotalInnervbox1.setSpacing(10);
            // Set preferred widths as percentages of the HBox width
            sqBottomFirstV.prefWidthProperty().bind(sqBottomMain.widthProperty().multiply(0.76));
            sqBottomSecondV.prefWidthProperty().bind(sqBottomMain.widthProperty().multiply(0.24));
//            sqBottomMain.setPrefHeight(260);
            tvSalesQuotCreateprodInvoiceDetails.getStylesheets().add(GenivisApplication.class.getResource("ui/css/purchase_challan_css/supplier_table_css/supplier_table_1281_1368.css").toExternalForm());
            spSalesQuoatCreateRootPane.getStylesheets().add(GenivisApplication.class.getResource("ui/css/TranxCommonCssStyles/tranxCommonCssStyle3.css").toExternalForm());
        } else if (width >= 1369 && width <= 1400) {
            topInnerHbOne.setSpacing(15);
            topInnerHbTwo.setSpacing(15);
            totalmainDiv.setSpacing(10);
            sqtotalInnervbox.setSpacing(15);
            sqtotalInnervbox1.setSpacing(15);
            // Set preferred widths as percentages of the HBox width
            sqBottomFirstV.prefWidthProperty().bind(sqBottomMain.widthProperty().multiply(0.76));
            sqBottomSecondV.prefWidthProperty().bind(sqBottomMain.widthProperty().multiply(0.24));
//            sqBottomMain.setPrefHeight(150);
            tvSalesQuotCreateprodInvoiceDetails.getStylesheets().add(GenivisApplication.class.getResource("ui/css/purchase_challan_css/supplier_table_css/supplier_table_1369_1400.css").toExternalForm());
            spSalesQuoatCreateRootPane.getStylesheets().add(GenivisApplication.class.getResource("ui/css/TranxCommonCssStyles/tranxCommonCssStyle4.css").toExternalForm());
        } else if (width >= 1401 && width <= 1440) {
            topInnerHbOne.setSpacing(15);
            topInnerHbTwo.setSpacing(15);
            totalmainDiv.setSpacing(10);
            sqtotalInnervbox.setSpacing(15);
            sqtotalInnervbox1.setSpacing(15);
            // Set preferred widths as percentages of the HBox width
            sqBottomFirstV.prefWidthProperty().bind(sqBottomMain.widthProperty().multiply(0.78));
            sqBottomSecondV.prefWidthProperty().bind(sqBottomMain.widthProperty().multiply(0.22));
//            sqBottomMain.setPrefHeight(150);
            spSalesQuoatCreateRootPane.getStylesheets().add(GenivisApplication.class.getResource("ui/css/TranxCommonCssStyles/tranxCommonCssStyle5.css").toExternalForm());
            tvSalesQuotCreateprodInvoiceDetails.getStylesheets().add(GenivisApplication.class.getResource("ui/css/purchase_challan_css/supplier_table_css/supplier_table_1401_1440.css").toExternalForm());
        } else if (width >= 1441 && width <= 1680) {
            topInnerHbOne.setSpacing(15);
            topInnerHbTwo.setSpacing(15);
            totalmainDiv.setSpacing(10);
            sqtotalInnervbox.setSpacing(15);
            sqtotalInnervbox1.setSpacing(15);
            // Set preferred widths as percentages of the HBox width
            sqBottomFirstV.prefWidthProperty().bind(sqBottomMain.widthProperty().multiply(0.8));
            sqBottomSecondV.prefWidthProperty().bind(sqBottomMain.widthProperty().multiply(0.2));
//            sqBottomMain.setPrefHeight(310);
            tvSalesQuotCreateprodInvoiceDetails.getStylesheets().add(GenivisApplication.class.getResource("ui/css/purchase_challan_css/supplier_table_css/supplier_table_1441_1680.css").toExternalForm());
            spSalesQuoatCreateRootPane.getStylesheets().add(GenivisApplication.class.getResource("ui/css/TranxCommonCssStyles/tranxCommonCssStyle6.css").toExternalForm());
        } else if (width >= 1681 && width <= 1920) {
            topInnerHbOne.setSpacing(20);
            topInnerHbTwo.setSpacing(20);
            totalmainDiv.setSpacing(20);
            sqtotalInnervbox.setSpacing(20);
            sqtotalInnervbox1.setSpacing(20);
            // Set preferred widths as percentages of the HBox width
            sqBottomFirstV.prefWidthProperty().bind(sqBottomMain.widthProperty().multiply(0.8));
            sqBottomSecondV.prefWidthProperty().bind(sqBottomMain.widthProperty().multiply(0.2));
//            sqBottomMain.setPrefHeight(150);
            tvSalesQuotCreateprodInvoiceDetails.getStylesheets().add(GenivisApplication.class.getResource("ui/css/invoice_product_history_table.css").toExternalForm());
            spSalesQuoatCreateRootPane.getStylesheets().add(GenivisApplication.class.getResource("ui/css/TranxCommonCssStyles/tranxCommonCssStyle7.css").toExternalForm());
        }
    }

    TableCellCallback<Object[]> callback = item -> {

        System.out.println("item: " + item[0]);
        System.out.println("item    ZXzx: " + item[1]);
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


        lbSalesQuotCreateGrossTotal.setText((String) item[3]);
        lbSalesQuotCreateDiscount.setText((String) item[4]);
        lbSalesQuotCreateTotal.setText((String) item[5]);
        lbSalesQuotCreateTax.setText((String) item[6]);
        lbSalesQuotCreateBillAmount.setText((String) item[2]); //totalNetAmt for bill amount
    };

    TableCellCallback<Integer> productCall = item -> {
        System.out.println("product id " + item);
        fetchSelectedProductData(String.valueOf(item));
        getSupplierListbyProductId(String.valueOf(item));
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

    TableCellCallback<Integer> unit_callback = currentIndex -> {
        System.out.println("i am in" + currentIndex);
        System.out.println("i am in ledgerStateCode" + ledgerStateCode);
        SalesQuotationProductDTO tranxRow = tblvSalesQuotationCmptRow.getItems().get(currentIndex);
        //! ? Ledger State Code get selected
        if (ledgerStateCode != null) {
            if (ledgerStateCode.equals(Globals.mhStateCode)) {
                System.out.println("tranxRow.getUnitWiseRate() => " + tranxRow.getUnitWiseRateMH());
                tranxRow.setRate(tranxRow.getUnitWiseRateMH() + "");
            } else {
                System.out.println("tranxRow.getUnitWiseRate() => " + tranxRow.getUnitWiseRateAI());
                tranxRow.setRate(tranxRow.getUnitWiseRateAI() + "");
            }
        }
//        tvSalesOrderCmpTRow.getItems().set(currentIndex, tranxRow);
    };


    public void tableInitiliazation() {

        tblvSalesQuotationCmptRow.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tblvSalesQuotationCmptRow.setEditable(true);
        tblvSalesQuotationCmptRow.setFocusTraversable(false);


        Label headerLabel = new Label("Sr\nNo.");
        tblcSalesQuotCreateSrNo.setGraphic(headerLabel);

        tblvSalesQuotationCmptRow.getItems().add(new SalesQuotationProductDTO("1", "", "", "", "", "", "", "",
                "", "", "", "", "", "", "", "", "", "", ""));

        tblcSalesQuotCreateSrNo.setCellValueFactory(cellData -> cellData.getValue().sr_noProperty());
        tblcSalesQuotCreateSrNo.setStyle("-fx-alignment: CENTER;");

        tblcSalesQuotCreatePackage.setCellValueFactory(cellData -> cellData.getValue().packingProperty());
        tblcSalesQuotCreatePackage.setStyle("-fx-alignment: CENTER;");

        tcSalesQuotCreateCmpTRowLevelA.setCellValueFactory(cellData -> cellData.getValue().levelAProperty());
        tcSalesQuotCreateCmpTRowLevelA.setCellFactory(column -> new ComboBoxTableCellForLevelASalesQuot("tcSalesQuotCreateCmpTRowLevelA"));

        tcSalesQuotCreateCmpTRowLevelB.setCellValueFactory(cellData -> cellData.getValue().levelBProperty());
        tcSalesQuotCreateCmpTRowLevelB.setCellFactory(column -> new ComboBoxTableCellForLevelBSalesQuot("tcSalesQuotCreateCmpTRowLevelB"));


        tcSalesQuotCreateCmpTRowLevelC.setCellValueFactory(cellData -> cellData.getValue().levelCProperty());
        tcSalesQuotCreateCmpTRowLevelC.setCellFactory(column -> new ComboBoxTableCellForLevelCSalesQuot("tcSalesQuotCreateCmpTRowLevelC"));

        tblcSalesQuotCreateUnit.setCellValueFactory(cellData -> cellData.getValue().unitProperty());
        tblcSalesQuotCreateUnit.setCellFactory(column -> new ComboBoxTableCellForUnitSalesQuot("tblcSalesQuotCreateUnit", unit_callback));

        tblcSalesQuotCreateParticular.setCellValueFactory(cellData -> cellData.getValue().particularsProperty());
        //     tblcSalesQuotCreateParticular.setCellFactory(column -> new TextFieldTableCellForSaleQuotation("tblcSalesQuotCreateParticular",callback,productCall));
        tblcSalesQuotCreateParticular.setCellFactory(column -> new TextFieldTableCellForSaleQuotation("tblcSalesQuotCreateParticular", callback, productCall, tfSalesQuotationCreateNarration, addPrdCalbak));


//        tcBatch.setCellValueFactory(cellData -> cellData.getValue().batch_or_serialProperty());
//        tcBatch.setCellFactory(column -> new com.opethic.genivis.controller.tranx_purchase.TextFieldTableCellForSaleQuotation("tcBatch",callback));


        tblcSalesQuotCreateQty.setCellValueFactory(cellData -> cellData.getValue().quantityProperty());
        tblcSalesQuotCreateQty.setCellFactory(column -> new TextFieldTableCellForSaleQuotation("tblcSalesQuotCreateQty", callback));

        tblcSalesQuotCreateRate.setCellValueFactory(cellData -> cellData.getValue().rateProperty());
        tblcSalesQuotCreateRate.setCellFactory(column -> new TextFieldTableCellForSaleQuotation("tblcSalesQuotCreateRate", callback));

        tblcSalesQuotCreateGrossAmt.setCellValueFactory(cellData -> cellData.getValue().gross_amtProperty());
        tblcSalesQuotCreateGrossAmt.setCellFactory(column -> new TextFieldTableCellForSaleQuotation("tblcSalesQuotCreateGrossAmt", callback));

        tblcSalesQuotCreateDisPer.setCellValueFactory(cellData -> cellData.getValue().dis_perProperty());
        tblcSalesQuotCreateDisPer.setCellFactory(column -> new TextFieldTableCellForSaleQuotation("tblcSalesQuotCreateDisPer", callback));

//        tblcSalesQuotCreateDisPer.setCellValueFactory(cellData -> cellData.getValue().dis_perProperty());
//        tblcSalesQuotCreateDisPer.setCellFactory(column -> new com.opethic.genivis.controller.tranx_purchase.TextFieldTableCellForSaleQuotation("tcDisPer",callback));

        tblcSalesQuotCreateDisRs.setCellValueFactory(cellData -> cellData.getValue().dis_amtProperty());
        tblcSalesQuotCreateDisRs.setCellFactory(column -> new TextFieldTableCellForSaleQuotation("tblcSalesQuotCreateDisRs", callback));

        tblcSalesQuotCreateTax.setCellValueFactory(cellData -> cellData.getValue().tax_perProperty());
        tblcSalesQuotCreateTax.setCellFactory(column -> new TextFieldTableCellForSaleQuotation("tblcSalesQuotCreateTax", callback));

        tblcSalesQuotCreateNetAmt.setCellValueFactory(cellData -> cellData.getValue().net_amtProperty());
        tblcSalesQuotCreateNetAmt.setCellFactory(column -> new TextFieldTableCellForSaleQuotation("tblcSalesQuotCreateNetAmt", callback));
        tcSalesQuotCreateCmpTRowActions.setCellValueFactory(cellData -> cellData.getValue().actionProperty());
        tcSalesQuotCreateCmpTRowActions.setCellFactory(column -> new ButtonTableCellQuot());

        columnVisibility(tcSalesQuotCreateCmpTRowLevelA, Globals.getUserControlsWithSlug("is_level_a"));
        columnVisibility(tcSalesQuotCreateCmpTRowLevelB, Globals.getUserControlsWithSlug("is_level_b"));
        columnVisibility(tcSalesQuotCreateCmpTRowLevelC, Globals.getUserControlsWithSlug("is_level_c"));

        tcSalesQuotCreateCmpTRowLevelA.setText(Globals.getUserControlsNameWithSlug("is_level_a") + "");
        tcSalesQuotCreateCmpTRowLevelB.setText(Globals.getUserControlsNameWithSlug("is_level_B") + "");
        tcSalesQuotCreateCmpTRowLevelC.setText(Globals.getUserControlsNameWithSlug("is_level_C") + "");

    }


    public void responsiveTable() {
        //todo: Responsive code for tableview
        tblcSalesQuotCreateSrNo.prefWidthProperty().bind(tblvSalesQuotationCmptRow.widthProperty().multiply(0.05));
//        tblcSalesQuotCreateParticular.prefWidthProperty().bind(tblvSalesQuotationCmptRow.widthProperty().multiply(0.3));
        tblcSalesQuotCreatePackage.prefWidthProperty().bind(tblvSalesQuotationCmptRow.widthProperty().multiply(0.05));
        tblcSalesQuotCreateUnit.prefWidthProperty().bind(tblvSalesQuotationCmptRow.widthProperty().multiply(0.05));
        tblcSalesQuotCreateQty.prefWidthProperty().bind(tblvSalesQuotationCmptRow.widthProperty().multiply(0.05));
//        tblcSalesQuotCreateGrossAmt.prefWidthProperty().bind(tblvSalesQuotationCmptRow.widthProperty().multiply(0.15));
        tblcSalesQuotCreateDisPer.prefWidthProperty().bind(tblvSalesQuotationCmptRow.widthProperty().multiply(0.05));
        tblcSalesQuotCreateDisRs.prefWidthProperty().bind(tblvSalesQuotationCmptRow.widthProperty().multiply(0.05));
        tblcSalesQuotCreateTax.prefWidthProperty().bind(tblvSalesQuotationCmptRow.widthProperty().multiply(0.05));
//        tblcSalesQuotCreateNetAmt.prefWidthProperty().bind(tblvSalesQuotationCmptRow.widthProperty().multiply(0.08));
        tcSalesQuotCreateCmpTRowActions.prefWidthProperty().bind(tblvSalesQuotationCmptRow.widthProperty().multiply(0.02));
        if (tblcSalesQuotCreateFreeqty.isVisible()) {
            tblcSalesQuotCreateFreeqty.prefWidthProperty().bind(tblvSalesQuotationCmptRow.widthProperty().multiply(0.05));
            tblcSalesQuotCreateRate.prefWidthProperty().bind(tblvSalesQuotationCmptRow.widthProperty().multiply(0.05));
        } else {
            tblcSalesQuotCreateRate.prefWidthProperty().bind(tblvSalesQuotationCmptRow.widthProperty().multiply(0.1));
        }


        if (tcSalesQuotCreateCmpTRowLevelA.isVisible() == true && tcSalesQuotCreateCmpTRowLevelB.isVisible() == false && tcSalesQuotCreateCmpTRowLevelC.isVisible() == false) {
            System.out.println("inside first condn");
            tblcSalesQuotCreateParticular.prefWidthProperty().bind(tblvSalesQuotationCmptRow.widthProperty().multiply(0.3));
            tblcSalesQuotCreateGrossAmt.prefWidthProperty().bind(tblvSalesQuotationCmptRow.widthProperty().multiply(0.13));
            tblcSalesQuotCreateNetAmt.prefWidthProperty().bind(tblvSalesQuotationCmptRow.widthProperty().multiply(0.05));
            tcSalesQuotCreateCmpTRowLevelA.prefWidthProperty().bind(tblvSalesQuotationCmptRow.widthProperty().multiply(0.05));
        } else if (tcSalesQuotCreateCmpTRowLevelA.isVisible() == true && tcSalesQuotCreateCmpTRowLevelB.isVisible() == true && tcSalesQuotCreateCmpTRowLevelC.isVisible() == false) {
            System.out.println("inside second condn");
            tcSalesQuotCreateCmpTRowLevelA.prefWidthProperty().bind(tblvSalesQuotationCmptRow.widthProperty().multiply(0.05));
            tblcSalesQuotCreateParticular.prefWidthProperty().bind(tblvSalesQuotationCmptRow.widthProperty().multiply(0.28));
            tblcSalesQuotCreateGrossAmt.prefWidthProperty().bind(tblvSalesQuotationCmptRow.widthProperty().multiply(0.1));
            tblcSalesQuotCreateNetAmt.prefWidthProperty().bind(tblvSalesQuotationCmptRow.widthProperty().multiply(0.05));
            tcSalesQuotCreateCmpTRowLevelB.prefWidthProperty().bind(tblvSalesQuotationCmptRow.widthProperty().multiply(0.05));
        } else if (tcSalesQuotCreateCmpTRowLevelA.isVisible() == true && tcSalesQuotCreateCmpTRowLevelB.isVisible() == true && tcSalesQuotCreateCmpTRowLevelC.isVisible() == true) {
            System.out.println("inside thrd condn");
            tblcSalesQuotCreateParticular.prefWidthProperty().bind(tblvSalesQuotationCmptRow.widthProperty().multiply(0.23));
            tblcSalesQuotCreateGrossAmt.prefWidthProperty().bind(tblvSalesQuotationCmptRow.widthProperty().multiply(0.1));
            tblcSalesQuotCreateNetAmt.prefWidthProperty().bind(tblvSalesQuotationCmptRow.widthProperty().multiply(0.05));
            tcSalesQuotCreateCmpTRowLevelA.prefWidthProperty().bind(tblvSalesQuotationCmptRow.widthProperty().multiply(0.05));
            tcSalesQuotCreateCmpTRowLevelB.prefWidthProperty().bind(tblvSalesQuotationCmptRow.widthProperty().multiply(0.05));
            tcSalesQuotCreateCmpTRowLevelC.prefWidthProperty().bind(tblvSalesQuotationCmptRow.widthProperty().multiply(0.05));
        } else {
            System.out.println("inside fourth condn");
            tblcSalesQuotCreateParticular.prefWidthProperty().bind(tblvSalesQuotationCmptRow.widthProperty().multiply(0.35));
            tblcSalesQuotCreateGrossAmt.prefWidthProperty().bind(tblvSalesQuotationCmptRow.widthProperty().multiply(0.1));
            tblcSalesQuotCreateNetAmt.prefWidthProperty().bind(tblvSalesQuotationCmptRow.widthProperty().multiply(0.08));

        }

    }

    public void sceneInitilization() {
        PurInvoiceCommunicator.unitForPurInvoiceList = FXCollections.observableArrayList();//new
        spSalesQuoatCreateRootPane.sceneProperty().addListener((observable, oldScene, newScene) -> {
            if (newScene != null && newScene.getWindow() instanceof Stage) {
                Communicator.stage = (Stage) newScene.getWindow();
            }
        });
    }

    /**** ADDING ROW for product adding ****/
    public void addRow(ActionEvent actionEvent) {
        SalesQuotationLogger.info("Adding new row to add another product in sales Quotation");
        tblvSalesQuotationCmptRow.getItems().add(new SalesQuotationProductDTO("", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", ""));
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

    //todo: fetch Selected ledger data
    public void fetchSelectedLedgerData(String LedgerId) {
        try {
            SalesQuotationLogger.info("Fetching Selected Ledger Data to show in Ledger info");
            Map<String, String> body = new HashMap<>();
            body.put("ledger_id", LedgerId);
            System.out.println("ledger_id 123 " + LedgerId);
            String requestBody = Globals.mapToStringforFormData(body);
            HttpResponse<String> response = APIClient.postFormDataRequest(requestBody, EndPoints.TRANSACTION_LEDGER_DETAILS);
            SalesQuotationLogger.info("response data of selected Ledger " + response.body());
            JsonObject jsonObject = new Gson().fromJson(response.body(), JsonObject.class);
            ObservableList<TranxLedgerWindowDTO> observableList = FXCollections.observableArrayList();
            System.out.println("fetchedSelectedLedgerData " + jsonObject);
            if (jsonObject.get("responseStatus").getAsInt() == 200) {
                //todo: activating the ledger tab
                tpSalesQuotation.getSelectionModel().select(tabSalesQuotaCreateLedger);

                CompanyStateCode = jsonObject.get("company_state_code").getAsLong();
                System.out.println("CompanyStateCode " + CompanyStateCode);
                JsonObject item = jsonObject.get("result").getAsJsonObject();
                stateCode = item.get("stateCode").getAsLong();
//                ledgerStateCode = String.valueOf(item.get("state_code").getAsInt());
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

                lbSalesQuotationCreateledInfogstNo.setText(gstNo);
                lbSalesQuotationCreateledInfoArea.setText(Area);
                lbSalesQuotationCreateledInfoBank.setText(bank);
                lbSalesQuotationCreateledInfoContPerson.setText(contact_person);
                lbSalesQuotationCreateledInfoMobNo.setText(mobile_no);
                lbSalesQuotationCreateledInfoCreditDays.setText(credit_days);
                lbSalesQuotationCreateledInfoFssai.setText(fssai);
                lbSalesQuotationCreateledInfoLiceNo.setText(licence_no);
                lbSalesQuotationCreateledInfoRoute.setText(route);

            } else {

                SalesQuotationLogger.error("Error in response of fetching selected Ledger data");
            }
        } catch (Exception e) {
            SalesQuotationLogger.error("Error in Fetching Selected Ledger data is" + e.getMessage());
            e.printStackTrace();
        }
    }

    public void clearLedAndProdInfo() {
        ObservableList<SalesQuotationProductDTO> emptyList = FXCollections.observableArrayList();
        tblvSalesQuotationCmptRow.setItems(emptyList);
        ObservableList<SalesOrderSupplierDetailsDTO> emptyList1 = FXCollections.observableArrayList();
        Debtor_id = "";
        tfSalesQuotationCreateLedgerName.setText("");
        cmbSalesQuotationCreateClientGST.getSelectionModel().clearSelection();
        tvSalesQuotCreateprodInvoiceDetails.setItems(emptyList1);
        tfSalesQuotationCreateNarration.setText("");

        //todo clear Ledger Info
        lbSalesQuotationCreateledInfogstNo.setText("");
        lbSalesQuotationCreateledInfoArea.setText("");
        lbSalesQuotationCreateledInfoBank.setText("");
        lbSalesQuotationCreateledInfoContPerson.setText("");
        lbSalesQuotationCreateledInfoMobNo.setText("");
        lbSalesQuotationCreateledInfoCreditDays.setText("");
        lbSalesQuotationCreateledInfoFssai.setText("");
        lbSalesQuotationCreateledInfoLiceNo.setText("");
        lbSalesQuotationCreateledInfoRoute.setText("");

        //todo clear Product Info
        lbSalesQuotationCreateProdInfoBrand.setText("");
        lbSalesQuotationCreateProdInfoHSN.setText("");
        lbSalesQuotationCreateProdInfoGroup.setText("");
        lbSalesQuotationCreateProdInfoCategory.setText("");
        lbSalesQuotationCreateProdInfoSupplier.setText("");
        lbSalesQuotationCreateProdInfoTaxType.setText("");
        lbSalesQuotationCreateProdInfoTax.setText("");
        lbSalesQuotationCreateProdInfoMargin.setText("");
        lbSalesQuotationCreateProdInfoCost.setText("");
        lbSalesQuotationCreateProdInfoShelfId.setText("");
        lbSalesQuotationCreateProdInfoMinStock.setText("");
        lbSalesQuotationCreateProdInfoMaxStock.setText("");

        //clear all row calculation
        lbSalesQuotCreateBillAmount.setText("0.00");

        lbSalesQuotCreateGrossTotal.setText("0.00");

        lbSalesQuotCreateDiscount.setText("0.00");

        lbSalesQuotCreateTax.setText("0.00");

        lbSalesQuotCreateTotal.setText("0.00");
    }

    //todo: fetch Selected Product data for product info
    public void fetchSelectedProductData(String ProductId) {
        try {
            SalesQuotationLogger.info("Fetching Selected Product Data to show in product info");
            Map<String, String> body = new HashMap<>();
            body.put("product_id", ProductId);
            System.out.println("ProductId 123 " + ProductId);
            String requestBody = Globals.mapToStringforFormData(body);
            HttpResponse<String> response = APIClient.postFormDataRequest(requestBody, EndPoints.TRANSACTION_PRODUCT_DETAILS);
            System.out.println("Product Details response body" + response.body());
            JsonObject jsonObject = new Gson().fromJson(response.body(), JsonObject.class);
            ObservableList<TranxProductWindowDTO> observableList = FXCollections.observableArrayList();
            //todo: activating the product tab
            tpSalesQuotation.getSelectionModel().select(tabSalesQuotaCreateProduct);
            if (jsonObject.get("responseStatus").getAsInt() == 200) {
                tpSalesQuotation.getSelectionModel().select(tabSalesQuotaCreateProduct);

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
                lbSalesQuotationCreateProdInfoBrand.setText(brand);
                lbSalesQuotationCreateProdInfoHSN.setText(hsn);
                lbSalesQuotationCreateProdInfoGroup.setText(group);
                lbSalesQuotationCreateProdInfoCategory.setText(category);
                lbSalesQuotationCreateProdInfoSupplier.setText(supplier);
                lbSalesQuotationCreateProdInfoTaxType.setText(tax_type);
                lbSalesQuotationCreateProdInfoTax.setText(tax);
                lbSalesQuotationCreateProdInfoMargin.setText(margin);
                lbSalesQuotationCreateProdInfoCost.setText(cost);
                lbSalesQuotationCreateProdInfoShelfId.setText(shelfId);
                lbSalesQuotationCreateProdInfoMinStock.setText(min_stock);
                lbSalesQuotationCreateProdInfoMaxStock.setText(max_stock);

            } else {
                System.out.println("Error in response");
            }
        } catch (Exception e) {
            SalesQuotationLogger.error("Error in Fetching Selected Product data is" + e.getMessage());
            e.printStackTrace();
        }
    }

    //todo:get sales account dropdown option using api
    public void getSalesAccounts() {
        try {
            HttpResponse<String> response = APIClient.getRequest(EndPoints.SALESQUOTATION_GET_SALES_QUOTATION_ACCOUNTS);
            JsonObject jsonObject = new Gson().fromJson(response.body(), JsonObject.class);
            System.out.println(jsonObject);
            ObservableList<CommonDTO> observableList = FXCollections.observableArrayList();
            if (jsonObject.get("responseStatus").getAsInt() == 200) {
                JsonArray responseObject = jsonObject.getAsJsonArray("list");
                System.out.println("responseObject getPurchaseAccounts " + responseObject);
                if (responseObject.size() > 0) {
                    for (JsonElement element : responseObject) {
                        JsonObject item = element.getAsJsonObject();
                        String id = item.get("id").getAsString();
                        String name = item.get("name").getAsString();
                        String unique_code = item.get("unique_code").getAsString();


                        observableList.add(new CommonDTO(name, id));
                        cmbSalesQuotationCreateSalesAC.setItems(observableList);
                        cmbSalesQuotationCreateSalesAC.getSelectionModel().selectFirst();
                        cmbSalesQuotationCreateSalesAC.setConverter(new StringConverter<CommonDTO>() {
                            @Override
                            public String toString(CommonDTO o) {
                                return o.getText();
                            }

                            @Override
                            public CommonDTO fromString(String s) {
                                return null;
                            }

                        });
//                        tfSalesQuotationCreateQuotationNo.requestFocus();

                    }


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

    //todo:To Set the GST of the Ledger
    private void setLedgerGSTDATA(ObservableList<GstDetailsDTO> gstDetails) {
        if (gstDetails != null) {

            cmbSalesQuotationCreateClientGST.setItems(gstDetails);
            cmbSalesQuotationCreateClientGST.setConverter(new StringConverter<GstDetailsDTO>() {
                @Override
                public String toString(GstDetailsDTO gstDetailsDTO) {
                    return gstDetailsDTO.getGstNo();
                }

                @Override
                public GstDetailsDTO fromString(String s) {
                    return null;
                }
            });
            GstDetailsDTO selectedGst = null;
            for (Object obj : cmbSalesQuotationCreateClientGST.getItems()) {
                GstDetailsDTO gstDetailsDTO = (GstDetailsDTO) obj;
                if (gstDetailsDTO.getId().equals(gstDetails.get(0).getId())) {
                    selectedGst = gstDetailsDTO;
                    break;
                }
            }
            if (selectedGst != null) {
                cmbSalesQuotationCreateClientGST.getSelectionModel().select(selectedGst);
            }

        }
//        cmbSalesQuotationCreateSalesAC.requestFocus();
    }


    //todo: get and set the Serial No and Order No
    private void getSalesQuotationSerialNo() {
        HttpResponse<String> response = APIClient.getRequest(EndPoints.SALESQUOTATION_GET_LAST_SALES_QUOTATION_RECORD);
        String responseBody = response.body();
        JsonObject jsonObject = new Gson().fromJson(responseBody, JsonObject.class);
        System.out.println("get_last_sales_quotation_record-->" + jsonObject);
        if (jsonObject.get("responseStatus").getAsInt() == 200) {
            String count = jsonObject.get("count").getAsString();
            String saleQuotNo = jsonObject.get("serialNo").getAsString();
            System.out.println("count =>" + count);
            tfSalesQuotationCreateSalesSerial.setText(count);
            tfSalesQuotationCreateQuotationNo.setText(saleQuotNo);
//            dpSalesQuotationCreateQuotationDate.requestFocus();  //for focus on Quotation date field
        }
    }

    //    todo: calculate GST
    private void calculateGst() {

        Map<Double, Double> cgstTotals = new HashMap<>();
        Map<Double, Double> sgstTotals = new HashMap<>();

        // Initialize totals to zero for each tax percentage
        for (Double taxPercentage : new Double[]{3.0, 5.0, 12.0, 18.0}) {
            cgstTotals.put(taxPercentage, 0.0);
            sgstTotals.put(taxPercentage, 0.0);
        }
        Double taxPercentage = 0.0;
        int totalQuantity = 0;
        int totalFreeQuantity = 0;
        int freeQuantity = 0;
        Double totalGrossAmt = 0.0, grossAmt = 0.0;
        Double totaltaxableAmt = 0.0, taxableAmt = 0.0;
        Double totalDisAmt = 0.0, disAmt = 0.0;
        Double totalTaxAmt = 0.0, taxAmt = 0.0;
        Double totalNetAmt = 0.0, netAmount = 0.0;

        System.out.println("TBBLL" + tblvSalesQuotationCmptRow.getItems());
        // Calculate totals for each transaction
        for (SalesQuotationProductDTO SalesQuotationProductDTO : tblvSalesQuotationCmptRow.getItems()) {
            taxPercentage = Double.parseDouble(SalesQuotationProductDTO.getTax_per());
            int quantity = Integer.parseInt(SalesQuotationProductDTO.getQuantity());

            if (SalesQuotationProductDTO.getFree_qty().isEmpty()) {
                freeQuantity = 0;
            } else {
                freeQuantity = Integer.parseInt(SalesQuotationProductDTO.getFree_qty());
            }

            totalQuantity += quantity;
            totalFreeQuantity += freeQuantity;
            double cgst = Double.parseDouble(SalesQuotationProductDTO.getCgst().toString());
            double sgst = Double.parseDouble(SalesQuotationProductDTO.getSgst().toString());

            cgstTotals.put(taxPercentage, cgstTotals.get(taxPercentage) + cgst);
            sgstTotals.put(taxPercentage, sgstTotals.get(taxPercentage) + sgst);

            // Total Calculation of gross amt ,taxable ,tax,discount
            // netAmount = Double.parseDouble(SalesQuotationProductDTO.getNet_amt());
            // totalNetAmt += netAmount;
            // grossAmt = Double.parseDouble(SalesQuotationProductDTO.getGross_amt());
            // totalGrossAmt += grossAmt;
            // taxableAmt = SalesQuotationProductDTO.getTaxable_amt();
            // totaltaxableAmt += taxableAmt;
            // disAmt = SalesQuotationProductDTO.getFinal_dis_amt();
            // totalDisAmt += disAmt;
            // taxAmt = SalesQuotationProductDTO.getFinTaxAmt();
            // totalTaxAmt += taxAmt;
        }

        // Print or display totals
        ObservableList<GstDTO> observableList = FXCollections.observableArrayList();
        for (Double taxPer : new Double[]{3.0, 5.0, 12.0, 18.0}) {
            double totalCGST = cgstTotals.get(taxPer);
            double totalSGST = sgstTotals.get(taxPer);
            double totalIGST = 0.0;

            if (totalCGST > 0) {
                // GstDTO gstDto :
                System.out.println("Tax Percentage: " + taxPer + "%");
                System.out.println("Total CGST: " + totalCGST);
                System.out.println("Total SGST: " + totalSGST);
                finaltotalCGST = String.valueOf(totalCGST);
                finaltotalSGST = String.valueOf(totalSGST);
                finaltotalIGST = String.valueOf(totalIGST);
                System.out.println();

                observableList.add(new GstDTO(String.format("%.2f", taxPer), String.format("%.2f", totalCGST),
                        String.format("%.2f", totalSGST), String.valueOf(totalIGST)));

            }
        }
    }


    // todo:   all Row Calculation
    private void calculateEachRowTraxTotal() {

        Double totalGrossAmt = 0.0, grossAmt = 0.0;
        Double totaltaxableAmt = 0.0, taxableAmt = 0.0;
        Double totalDisAmt = 0.0, disAmt = 0.0;
        Double totalTaxAmt = 0.0, taxAmt = 0.0;
        Double totalNetAmt = 0.0, netAmount = 0.0;

        for (SalesQuotationProductDTO SalesQuotationProductDTO : tblvSalesQuotationCmptRow.getItems()) {

//            Double taxAmt1 = SalesQuotationProductDTO.getFinTaxAmt();
//            Double disAmt2 = SalesQuotationProductDTO.getFinal_dis_amt();
//            System.out.println("sasasasasasas"+taxAmt1+disAmt2);

            //Total Calculation of gross amt ,taxable ,tax,discount
            //todo: Net amount
            if (SalesQuotationProductDTO.getNet_amt() != null) {

                netAmount = parseDouble(SalesQuotationProductDTO.getNet_amt());
                totalNetAmt += netAmount;
            } else {
                totalNetAmt = 0.0;
            }
            System.out.println("total net amount : " + totalNetAmt);

            //todo: Gross amount
            if (SalesQuotationProductDTO.getGross_amt() != null) {
                grossAmt = parseDouble(SalesQuotationProductDTO.getGross_amt());
                totalGrossAmt += grossAmt;
            } else {
                totalGrossAmt = 0.0;
            }
            System.out.println("total gross amount : " + totalGrossAmt);


            //todo: taxable amount
            if (SalesQuotationProductDTO.getTaxable_amt() != null) {
                taxableAmt = Double.valueOf(SalesQuotationProductDTO.getTaxable_amt());
                totaltaxableAmt += taxableAmt;
            } else {
                totaltaxableAmt = 0.0;
            }
            System.out.println("total amount : " + totaltaxableAmt);


            //todo: discount amount
            if (SalesQuotationProductDTO.getRow_dis_amt() != null) {
                disAmt = Double.valueOf(SalesQuotationProductDTO.getRow_dis_amt());
                totalDisAmt += disAmt;
            } else {
                totalDisAmt = 0.0;
            }
            System.out.println("total discount amount : " + totalDisAmt);


            //todo: Tax amount
            if (SalesQuotationProductDTO.getTotaligst() != null) {
                taxAmt = Double.valueOf(SalesQuotationProductDTO.getTotaligst());
                totalTaxAmt += taxAmt;
            } else {
                totalTaxAmt = 0.0;
            }
            System.out.println("total tax amount : " + totalTaxAmt);

        }


        //Display  of gross amt ,taxable ,tax,discount
        lbSalesQuotCreateBillAmount.setText(String.format("%.2f", totalNetAmt));

        lbSalesQuotCreateGrossTotal.setText(String.format("%.2f", totalGrossAmt));

        lbSalesQuotCreateDiscount.setText(String.format("%.2f", totalDisAmt));

        lbSalesQuotCreateTax.setText(String.format("%.2f", totalTaxAmt));

        lbSalesQuotCreateTotal.setText(String.format("%.2f", (totalGrossAmt - totalDisAmt)));

    }

    //todo : Create and update sales Quotation api
    public void createSalesQuotation() {
        id = SalesQuotId.toString();
        System.out.println("saleQuotaion id >. " + SalesQuotId);
        AlertUtility.CustomCallback callback = number -> {
            if (number == 1) {
                LocalDate QuotationDate = LocalDate.parse(Communicator.text_to_date.fromString(dpSalesQuotationCreateQuotationDate.getText()).toString());
                LocalDate tranxDate = LocalDate.parse(Communicator.text_to_date.fromString(dpSalesQuotationCreateTranxDate.getText()).toString());

//        id = SalesQuotId.toString();

                System.out.println("SalesQuotId >> " + id);
                System.out.println("SalesQuotId >> " + Debtor_id);

                Map<String, String> map = new HashMap<>();
                if (!id.equalsIgnoreCase("-1")) {
                    map.put("id", id);
                    map.put("debtors_id", Debtor_id);
                    System.out.println("debtor_idup " + Debtor_id);
                } else {
                    map.put("debtors_id", ledgerId);
                }
                map.put("bill_dt", String.valueOf(QuotationDate));
                map.put("newReference", "false");
                map.put("bill_no", tfSalesQuotationCreateQuotationNo.getText());
                map.put("sales_acc_id", cmbSalesQuotationCreateSalesAC.getValue().getId());
                map.put("sales_sr_no", tfSalesQuotationCreateSalesSerial.getText());
                map.put("transaction_date", String.valueOf(tranxDate));

                map.put("roundoff", "0.00");
                map.put("narration", tfSalesQuotationCreateNarration.getText());
                map.put("totalamt", lbSalesQuotCreateBillAmount.getText());
                map.put("total_purchase_discount_amt", lbSalesQuotCreateDiscount.getText());
                map.put("gstNo", cmbSalesQuotationCreateClientGST.getValue() != null ? cmbSalesQuotationCreateClientGST.getValue().getGstNo() : "");
//        map.put("totalcgst", finaltotalCGST);
//        map.put("totalsgst", finaltotalSGST);
//        map.put("totaligst", finaltotalIGST);
                map.put("tcs", "0");//static
                map.put("sales_discount", "0");//static
                map.put("sales_discount_amt", "0");//static
                map.put("total_sales_discount_amt", "0");//static
                map.put("additionalChargesTotal", "0");//static
                map.put("additionalCharges", "");
                map.put("total_qty", "0");//static
                map.put("total_free_qty", "0.00");//static
                map.put("total_row_gross_amt", lbSalesQuotCreateGrossTotal.getText());
                map.put("total_base_amt", lbSalesQuotCreateGrossTotal.getText());
                map.put("total_invoice_dis_amt", lbSalesQuotCreateDiscount.getText());
                map.put("taxable_amount", lbSalesQuotCreateTotal.getText());
                map.put("total_tax_amt", lbSalesQuotCreateTax.getText());
                map.put("bill_amount", lbSalesQuotCreateBillAmount.getText());

                List<SalesQuotationProductDTO> currentItems = new ArrayList<>(tblvSalesQuotationCmptRow.getItems());

                if (!currentItems.isEmpty()) {
                    SalesQuotationProductDTO lastItem = currentItems.get(currentItems.size() - 1);

                    if (lastItem.getParticulars() != null && lastItem.getParticulars().isEmpty()) {
                        currentItems.remove(currentItems.size() - 1);
                    }
                }

                List<SalesQuotationProductDTO> list = new ArrayList<>(currentItems);

                JsonArray jsonArray = new JsonArray();
                JsonObject jsonObject = new JsonObject();
                JsonObject jsonObject1 = new JsonObject();
                double taxPer = 0.0;
                for (SalesQuotationProductDTO SalesQuotationProductDTO : list) {
                    System.out.println("SalesQuotDTOTaxPer " + SalesQuotationProductDTO.getTax_per());
                    taxPer = SalesQuotationProductDTO.getTax_per() != null && SalesQuotationProductDTO.getTax_per() != "" ? Double.parseDouble(SalesQuotationProductDTO.getTax_per()) : 0.0;
                    System.out.println("taxPer----- " + taxPer);
                }
                if (CompanyStateCode.equals(stateCode)) {
                    map.put("taxFlag", "true");// static data as of now
//            jsonObject.addProperty("d_gst", decimalFormat.format(taxPer));
                    jsonObject.addProperty("gst", decimalFormat.format((taxPer) / 2));
                    jsonObject.addProperty("amt", decimalFormat.format(Double.parseDouble(lbSalesQuotCreateTax.getText()) / 2));
                    jsonArray.add(jsonObject);

                    jsonObject1.add("cgst", jsonArray);
                    jsonObject1.add("sgst", jsonArray);
                    map.put("taxCalculation", jsonObject1.toString());
                    map.put("totalcgst", decimalFormat.format(Double.parseDouble(lbSalesQuotCreateTax.getText()) / 2));
                    map.put("totalsgst", decimalFormat.format(Double.parseDouble(lbSalesQuotCreateTax.getText()) / 2));
                    map.put("totaligst", "0.0");
                    System.out.println("taxCalculation_cgst_sgst" + jsonObject1.toString());
                } else {
                    map.put("taxFlag", "false");// static data as of now
//            jsonObject.addProperty("d_gst", decimalFormat.format(taxPer));
                    jsonObject.addProperty("gst", decimalFormat.format(taxPer));
                    jsonObject.addProperty("amt", decimalFormat.format(Double.parseDouble(lbSalesQuotCreateTax.getText())));
                    jsonArray.add(jsonObject);
                    jsonObject1.add("igst", jsonArray);
                    map.put("taxCalculation", jsonObject1.toString());
                    map.put("totalcgst", "0.0");
                    map.put("totalsgst", "0.0");
                    map.put("totaligst", decimalFormat.format(Double.parseDouble(lbSalesQuotCreateTax.getText())));
                    System.out.println("taxCalculation_igst" + jsonObject1.toString());
                }
//            jsonArray.add(jsonObject);
                System.out.println("taxCalculation>>> " + jsonObject1);


                System.out.println("qwerty " + map);
                /***** mapping Row Data into TranxPurRowDTO *****/
                ArrayList<TranxPurRowDTO> rowData = new ArrayList<>();
                for (SalesQuotationProductDTO SalesQuotationProductDTO : list) {
                    TranxPurRowDTO purParticularRow = new TranxPurRowDTO();
                    purParticularRow.setProductId(SalesQuotationProductDTO.getProduct_id());
                    if (!SalesQuotationProductDTO.getLevel_a_id().isEmpty()) {
                        purParticularRow.setLevelaId(SalesQuotationProductDTO.getLevel_a_id());
                    } else {
                        purParticularRow.setLevelaId("");
                    }
                    if (!SalesQuotationProductDTO.getLevel_b_id().isEmpty()) {
                        purParticularRow.setLevelbId(SalesQuotationProductDTO.getLevel_b_id());
                    } else {
                        purParticularRow.setLevelbId("");
                    }
                    if (!SalesQuotationProductDTO.getLevel_c_id().isEmpty()) {
                        purParticularRow.setLevelcId(SalesQuotationProductDTO.getLevel_c_id());
                    } else {
                        purParticularRow.setLevelcId("");
                    }
                    if (!SalesQuotationProductDTO.getUnit_id().isEmpty()) {
                        purParticularRow.setUnitId(SalesQuotationProductDTO.getUnit_id());
                    } else {
                        purParticularRow.setUnitId("");
                    }
                    if (SalesQuotationProductDTO.getUnit_conv() != null) {
                        purParticularRow.setUnitConv(String.valueOf(SalesQuotationProductDTO.getUnit_conv()));
                    } else {
                        purParticularRow.setUnitConv("0.0");
                    }
                    if (!SalesQuotationProductDTO.getQuantity().isEmpty()) {
                        purParticularRow.setQty(SalesQuotationProductDTO.getQuantity());
                    } else {
                        purParticularRow.setQty("0.0");
                    }
                    if (!SalesQuotationProductDTO.getFree_qty().isEmpty()) {
                        purParticularRow.setFreeQty(SalesQuotationProductDTO.getFree_qty());
                    } else {
                        purParticularRow.setFreeQty("0.0");
                    }
                    if (!SalesQuotationProductDTO.getRate().isEmpty()) {
                        purParticularRow.setRate(SalesQuotationProductDTO.getRate());
                    } else {
                        purParticularRow.setRate("0.0");
                    }
                    if (SalesQuotationProductDTO.getBase_amt() != null) {
                        purParticularRow.setBaseAmt(String.valueOf(SalesQuotationProductDTO.getBase_amt()));
                    } else {
                        purParticularRow.setBaseAmt("0.0");
                    }
                    if (!SalesQuotationProductDTO.getDis_amt().isEmpty()) {
                        purParticularRow.setDisAmt(SalesQuotationProductDTO.getDis_amt());
                    } else {
                        purParticularRow.setDisAmt("0.0");
                    }
                    if (!SalesQuotationProductDTO.getDis_per().isEmpty()) {
                        purParticularRow.setDisPer(SalesQuotationProductDTO.getDis_per());
                    } else {
                        purParticularRow.setDisPer("0.0");
                    }
                    if (!SalesQuotationProductDTO.getDisPer2().isEmpty()) {
                        purParticularRow.setDisPer2(SalesQuotationProductDTO.getDisPer2());
                    } else {
                        purParticularRow.setDisPer2("0.0");
                    }
                    if (SalesQuotationProductDTO.getDis_per_cal() != null) {
                        purParticularRow.setDisPerCal(String.valueOf(SalesQuotationProductDTO.getDis_per_cal()));
                    } else {
                        purParticularRow.setDisPerCal("0.0");
                    }
                    if (SalesQuotationProductDTO.getDis_amt_cal() != null) {
                        purParticularRow.setDisAmtCal(String.valueOf(SalesQuotationProductDTO.getDis_amt_cal()));
                    } else {
                        purParticularRow.setDisAmtCal("0.0");
                    }
                    if (SalesQuotationProductDTO.getRow_dis_amt() != null) {
                        purParticularRow.setRowDisAmt(String.valueOf(SalesQuotationProductDTO.getRow_dis_amt()));
                    } else {
                        purParticularRow.setRowDisAmt("0.0");
                    }
                    if (SalesQuotationProductDTO.getGross_amt() != null) {
                        purParticularRow.setGrossAmt(String.valueOf(SalesQuotationProductDTO.getGross_amt()));
                    } else {
                        purParticularRow.setGrossAmt("0.0");
                    }
                    if (SalesQuotationProductDTO.getAdd_chg_amt() != null) {
                        purParticularRow.setAddChgAmt(String.valueOf(SalesQuotationProductDTO.getAdd_chg_amt()));
                    } else {
                        purParticularRow.setAddChgAmt("0.0");
                    }
                    if (SalesQuotationProductDTO.getGross_amt1() != null) {
                        purParticularRow.setGrossAmt1(String.valueOf(SalesQuotationProductDTO.getGross_amt1()));
                    } else {
                        purParticularRow.setGrossAmt1("0.0");
                    }
                    if (SalesQuotationProductDTO.getInvoice_dis_amt() != null) {
                        purParticularRow.setInvoiceDisAmt(String.valueOf(SalesQuotationProductDTO.getInvoice_dis_amt()));
                    } else {
                        purParticularRow.setInvoiceDisAmt("0.0");
                    }
                    if (SalesQuotationProductDTO.getTotal_amt() != null) {
                        purParticularRow.setTotalAmt(String.valueOf(SalesQuotationProductDTO.getTotal_amt()));
                    } else {
                        purParticularRow.setTotalAmt("0.0");
                    }
                    if (SalesQuotationProductDTO.getGst() != null) {
                        purParticularRow.setGst(String.valueOf(SalesQuotationProductDTO.getGst()));
                    } else {
                        purParticularRow.setGst("0.0");
                    }
                    if (SalesQuotationProductDTO.getCgst() != null) {
                        purParticularRow.setCgst(String.valueOf(SalesQuotationProductDTO.getCgst()));
                    } else {
                        purParticularRow.setCgst("0.0");
                    }
                    if (SalesQuotationProductDTO.getIgst() != null) {
                        purParticularRow.setIgst(String.valueOf(SalesQuotationProductDTO.getIgst()));
                    } else {
                        purParticularRow.setIgst("0.0");
                    }
                    if (SalesQuotationProductDTO.getSgst() != null) {
                        purParticularRow.setSgst(String.valueOf(SalesQuotationProductDTO.getSgst()));
                    } else {
                        purParticularRow.setSgst("0.0");
                    }
                    if (SalesQuotationProductDTO.getTotal_igst() != null) {
                        purParticularRow.setTotalIgst(String.valueOf(SalesQuotationProductDTO.getTotal_igst()));
                    } else {
                        purParticularRow.setTotalIgst("0.0");
                    }
                    if (SalesQuotationProductDTO.getTotal_cgst() != null) {
                        purParticularRow.setTotalCgst(String.valueOf(SalesQuotationProductDTO.getTotal_cgst()));
                    } else {
                        purParticularRow.setTotalCgst("0.0");
                    }
                    if (SalesQuotationProductDTO.getTotal_sgst() != null) {
                        purParticularRow.setTotalSgst(String.valueOf(SalesQuotationProductDTO.getTotal_sgst()));
                    } else {
                        purParticularRow.setTotalSgst("0.0");
                    }
                    if (SalesQuotationProductDTO.getFinal_amt() != null) {
                        purParticularRow.setFinalAmt(String.valueOf(SalesQuotationProductDTO.getFinal_amt()));
                    } else {
                        purParticularRow.setFinalAmt("0.0");
                    }
                    if (SalesQuotationProductDTO.getDetails_id() != null) {
                        purParticularRow.setDetailsId(String.valueOf(SalesQuotationProductDTO.getDetails_id()));
                    } else {
                        purParticularRow.setDetailsId("0");
                    }

//            if(SalesQuotationProductDTO.getReference_id().isEmpty()) {
//                purParticularRow.setReferenceId(SalesQuotationProductDTO.getReference_id());
//            }else {
//                purParticularRow.setReferenceId("");
//            }
//            if(SalesQuotationProductDTO.getReference_type().isEmpty()) {
//                purParticularRow.setReferenceType(SalesQuotationProductDTO.getReference_type());
//            }else {
//                purParticularRow.setReferenceType("");
//            }
                    rowData.add(purParticularRow);
                }

                String mRowData = new Gson().toJson(rowData, new TypeToken<List<TranxPurRowDTO>>() {
                }.getType());
                System.out.println("mRowData=>" + mRowData);
                map.put("row", mRowData);
                String finalReq = Globals.mapToString(map);
                System.out.println("FinalReq=> i am in" + finalReq);

                HttpResponse<String> response;
                String formData = Globals.mapToStringforFormData(map);
                System.out.println("formData" + formData);

                APIClient apiClient = null;

                if (id.equalsIgnoreCase("-1")) {
                    apiClient = new APIClient(EndPoints.SALESQUOTATION_CREATE_SALES_QUOTATION, formData, RequestType.FORM_DATA);
//            response = APIClient.postFormDataRequest(formData,EndPoints.SALESQUOTATION_CREATE_SALES_QUOTATION);

                } else {
                    apiClient = new APIClient(EndPoints.SALESQUOTATION_UPDATE_SALES_QUOTATION, formData, RequestType.FORM_DATA);
//            response = APIClient.postFormDataRequest(formData,EndPoints.SALESQUOTATION_UPDATE_SALES_QUOTATION);
                }

                try {
                    apiClient.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
                        @Override
                        public void handle(WorkerStateEvent workerStateEvent) {
                            String requestBody = workerStateEvent.getSource().getValue().toString();
                            JsonObject responseBody = new Gson().fromJson(requestBody, JsonObject.class);
                            SalesQuotationLogger.debug("responseBody of Sales Quotation" + responseBody);
                            AlertUtility.CustomCallback callback = (number) -> {
                                if (number == 1) {
                                    GlobalController.getInstance().addTabStatic(SALES_QUOTATION_LIST_SLUG, false);
//                            GlobalController.getInstance().addTabStatic("product-list", false);
                                }
                            };
//                            Stage stage = (Stage) spSalesQuoatCreateRootPane.getScene().getWindow();
                            if (responseBody.get("responseStatus").getAsInt() == 200) {
                                if (id.equalsIgnoreCase("-1")) {
                                    SalesQuotationListController.focusStatus = "create";
                                } else {
                                    SalesQuotationListController.focusStatus = "edit";
                                    SalesQuotationListController.selectedRowIndex = selectedRowIndex;

                                }
                                AlertUtility.AlertSuccessTimeout("Success", responseBody.get("message").getAsString(), callback);
                            } else {
                                AlertUtility.AlertErrorTimeout(AlertUtility.alertTypeError, responseBody.get("message").getAsString(), callback);
                            }
                        }
                    });
                    apiClient.setOnCancelled(new EventHandler<WorkerStateEvent>() {

                        @Override
                        public void handle(WorkerStateEvent workerStateEvent) {
                            SalesQuotationLogger.debug("Sales Quotation----> setOnCancelled() " +
                                    workerStateEvent.getSource().getValue().toString());
                        }
                    });
                    apiClient.setOnFailed(new EventHandler<WorkerStateEvent>() {
                        @Override
                        public void handle(WorkerStateEvent workerStateEvent) {
                            SalesQuotationLogger.debug("Sales Quotation---> setOnFailed() " +
                                    workerStateEvent.getSource().getValue().toString());
                        }
                    });
                    apiClient.start();
                } catch (Exception e) {
                    apiClient = null;
                }


//        if(id.equalsIgnoreCase("-1"))
//        {
//            response = APIClient.postFormDataRequest(formData,EndPoints.SALESQUOTATION_CREATE_SALES_QUOTATION);
//
//        }else{
//            response = APIClient.postFormDataRequest(formData,EndPoints.SALESQUOTATION_UPDATE_SALES_QUOTATION);
//        }


//        JsonObject responseBody = new Gson().fromJson(response.body(), JsonObject.class);
//        System.out.println("Response=>"+responseBody);
//        message=responseBody.get("message").getAsString();

//        if (responseBody.get("responseStatus").getAsInt() == 200) {
//
//            Alert alert = new Alert(Alert.AlertType.INFORMATION);
//            alert.setTitle("Success");
//            alert.setHeaderText(message);
//            // alert.setContentText("HSN has been created successfully.");
//            alert.show();
//            PauseTransition delay = new PauseTransition(Duration.seconds(1));
//            delay.setOnFinished(event -> alert.close());
//            delay.play();
//            GlobalController.getInstance().addTabStatic(SALES_QUOTATION_LIST_SLUG,false);
////            tfSalesQuotationCreateLedgerName.setText("");
////            cmbSalesQuotationCreateClientGST.getSelectionModel().clearSelection();
//        }
            } else {
                System.out.println("working!");
            }
        };
//        if (SalesQuotId == -1) {
//            AlertUtility.AlertConfirmation(AlertUtility.alertTypeConfirmation, "Do you want to Create " + tfSalesQuotationCreateQuotationNo.getText() + " Quotation No.", callback);
//        } else {
//            AlertUtility.AlertConfirmation(AlertUtility.alertTypeConfirmation, "Do you want to Update " + tfSalesQuotationCreateQuotationNo.getText() + " Quotation No.", callback);
//        }
        if (SalesQuotId == -1   ) {
            AlertUtility.AlertConfirmation(AlertUtility.alertTypeConfirmation, LedgerMessageConsts.msgConfirmationOnSubmit + tfSalesQuotationCreateQuotationNo.getText() + LedgerMessageConsts.msgInvoiceNumber, callback);
        } else {
            AlertUtility.AlertConfirmation(AlertUtility.alertTypeConfirmation, LedgerMessageConsts.msgConfirmationOnUpdate + tfSalesQuotationCreateQuotationNo.getText() + LedgerMessageConsts.msgInvoiceNumber, callback);
        }
    }

    public void responsiveprodInfoTable() {
        //todo: Responsive code for bottom productInfo tableview
        tblcSalesQuotationCreateClienName.prefWidthProperty().bind(tvSalesQuotCreateprodInvoiceDetails.widthProperty().multiply(0.2));
        tblcSalesQuotationCreateInvoiceNo.prefWidthProperty().bind(tvSalesQuotCreateprodInvoiceDetails.widthProperty().multiply(0.1));
        tblcSalesQuotationCreateInvoiceDate.prefWidthProperty().bind(tvSalesQuotCreateprodInvoiceDetails.widthProperty().multiply(0.1));
        tblcSalesQuotationCreateBatch.prefWidthProperty().bind(tvSalesQuotCreateprodInvoiceDetails.widthProperty().multiply(0.1));
        tblcSalesQuotationCreateMrp.prefWidthProperty().bind(tvSalesQuotCreateprodInvoiceDetails.widthProperty().multiply(0.1));
        tblcSalesQuotationCreateQuantity.prefWidthProperty().bind(tvSalesQuotCreateprodInvoiceDetails.widthProperty().multiply(0.1));
        tblcSalesQuotationCreateRate.prefWidthProperty().bind(tvSalesQuotCreateprodInvoiceDetails.widthProperty().multiply(0.05));
        tblcSalesQuotationCreateCost.prefWidthProperty().bind(tvSalesQuotCreateprodInvoiceDetails.widthProperty().multiply(0.1));
        tblcSalesQuotationCreateDisPer.prefWidthProperty().bind(tvSalesQuotCreateprodInvoiceDetails.widthProperty().multiply(0.05));
        tblcSalesQuotationCreateDis.prefWidthProperty().bind(tvSalesQuotCreateprodInvoiceDetails.widthProperty().multiply(0.1));

    }

    //todo: Validate quotation Number
    public void validateQuotationNo() {
        try {
            SalesQuotationLogger.info("Validate Quotation Number");
            Map<String, String> body = new HashMap<>();
            body.put("salesQuotationNo", tfSalesQuotationCreateQuotationNo.getText());
            System.out.println("tfSalesQuotationCreateQuotationNo.getText()edit " + tfSalesQuotationCreateQuotationNo.getText());
            String requestBody = Globals.mapToStringforFormData(body);
            HttpResponse<String> response = APIClient.postFormDataRequest(requestBody, EndPoints.SALESQUOTATION_VALIDATE_SALES_QUOTATION_NO);
            SalesQuotationLogger.info("response data of Validating Quotation number " + response.body());
            JsonObject jsonObject = new Gson().fromJson(response.body(), JsonObject.class);
            System.out.println("validateQuotationNo " + jsonObject);

            if (jsonObject.get("responseStatus").getAsInt() == 200) {
                System.out.println("SUCCESS " + jsonObject.get("message").getAsString());
                SalesQuotationLogger.debug(jsonObject.get("message").getAsString());
                dpSalesQuotationCreateQuotationDate.requestFocus();
            } else {
                AlertUtility.CustomCallback callback = (number) -> {
                };
                Stage stage = (Stage) spSalesQuoatCreateRootPane.getScene().getWindow();
                AlertUtility.AlertErrorTimeout(AlertUtility.alertTypeError, jsonObject.get("message").getAsString(), callback);
//                showAlert(jsonObject.get("message").getAsString());
                System.out.println("FAIL " + jsonObject.get("message").getAsString());
                SalesQuotationLogger.debug(jsonObject.get("message").getAsString());
                tfSalesQuotationCreateQuotationNo.requestFocus();
            }
        } catch (Exception e) {
            SalesQuotationLogger.error("Error in Validating Quotation number is" + e.getMessage());
            e.printStackTrace();
        }
    }

    public void validateQuotationNoInEdit() {
        try {
            SalesQuotationLogger.info("Validate Quotation Number");
            Map<String, String> body = new HashMap<>();
            body.put("invoice_id", SalesQuotId.toString());
            body.put("salesQuotationNo", tfSalesQuotationCreateQuotationNo.getText());
            System.out.println("tfSalesQuotationEditQuotationNo.getText() edit " + tfSalesQuotationCreateQuotationNo.getText());
            String requestBody = Globals.mapToStringforFormData(body);
            HttpResponse<String> response = APIClient.postFormDataRequest(requestBody, EndPoints.SALESQUOTATION_VALIDATE_SALES_QUOTATIONEDIT_NO);
            SalesQuotationLogger.info("response data of Validating Quotation number " + response.body());
            JsonObject jsonObject = new Gson().fromJson(response.body(), JsonObject.class);
            System.out.println("validateQuotationNo " + jsonObject);

            if (jsonObject.get("responseStatus").getAsInt() == 200) {
                System.out.println("SUCCESS " + jsonObject.get("message").getAsString());
                SalesQuotationLogger.debug(jsonObject.get("message").getAsString());
                dpSalesQuotationCreateQuotationDate.requestFocus();
            } else {
                AlertUtility.CustomCallback callback = (number) -> {
                };
                Stage stage = (Stage) spSalesQuoatCreateRootPane.getScene().getWindow();
                AlertUtility.AlertErrorTimeout(AlertUtility.alertTypeError, jsonObject.get("message").getAsString(), callback);
//                showAlert(jsonObject.get("message").getAsString());
                System.out.println("FAIL " + jsonObject.get("message").getAsString());
                SalesQuotationLogger.debug(jsonObject.get("message").getAsString());
                tfSalesQuotationCreateQuotationNo.requestFocus();
            }
        } catch (Exception e) {
            SalesQuotationLogger.error("Error in Validating Quotation number is" + e.getMessage());
            e.printStackTrace();
        }
    }


    public void getSalesQuotById(String SalesQuotInvId) {
        try {
            SalesQuotationLogger.info("Fetching SalesQuotation Databy id");
            System.out.println("SalesQuotId to update " + SalesQuotId);
            //todo update button name change
            if (SalesQuotId > -1)
                btnSalesQuotationCreateSubmit.setText("Update");
            btnSalesQuotationCreateEdit.setVisible(false);

            Map<String, String> body = new HashMap<>();
            body.put("id", SalesQuotInvId);
            SalesQuotationLogger.debug("SalesQuotInvId 123 " + SalesQuotInvId);
            String requestBody = Globals.mapToStringforFormData(body);
            HttpResponse<String> response = APIClient.postFormDataRequest(requestBody, EndPoints.GET_SALES_QUOTATION_BY_ID_NEW);
            SalesQuotationLogger.debug("response data of selected Ledger " + response.body());
            SalesQuotEditMainDTO responseBody = new Gson().fromJson(response.body(), SalesQuotEditMainDTO.class);
            System.out.println("SalesQuotInvData " + responseBody);
            if (responseBody.getResponseStatus() == 200) {
                //todo: activating the ledger tab

                setSalesQuotationFormData(responseBody);
//
                //todo: setting data in Ledger details block in sales quotation page

            } else {

                SalesQuotationLogger.error("Error in response of fetching Sales Quotation Data by id");
            }
        } catch (Exception e) {
            SalesQuotationLogger.error("Error in Fetching Sales Quotation Data by id is" + e.getMessage());
            e.printStackTrace();
        }
    }

    public void setSalesQuotationFormData(SalesQuotEditMainDTO responseBody) {
//        fetchSelectedLedgerData(responseBody.getInvoiceData().getDebtorId().toString());
        LocalDate tranxDate = LocalDate.parse(responseBody.getInvoiceData().getBillDate());
        LocalDate QuotationDate = LocalDate.parse(responseBody.getInvoiceData().getBillDate());
        dpSalesQuotationCreateTranxDate.setText(String.valueOf(tranxDate));
        dpSalesQuotationCreateQuotationDate.setText(String.valueOf(QuotationDate));
        tfSalesQuotationCreateQuotationNo.setText(responseBody.getInvoiceData().getSqBillNo());
        tfSalesQuotationCreateSalesSerial.setText(String.valueOf(responseBody.getInvoiceData().getSalesSrNo()));
        Debtor_id = responseBody.getInvoiceData().getDebtorId().toString();
        ledgerId = responseBody.getInvoiceData().getDebtorId().toString();
        fetchSelectedLedgerData(Debtor_id);
        System.out.println("set Debtor_id  " + Debtor_id);
//        stateCode = Long.valueOf(responseBody.getInvoiceData().getLedgerStateCode());
        ledgerStateCode = responseBody.getInvoiceData().getLedgerStateCode();//new
        tfSalesQuotationCreateLedgerName.setText(String.valueOf(responseBody.getInvoiceData().getDebtorName()));
        tfSalesQuotationCreateNarration.setText(responseBody.getNarration());
//        tfPurChallAllRowDisPer.setText(String.valueOf(responseBody.));
//        lbSalesQuotCreateDiscount.setText( String.format("%.2f", responseBody.getInvoiceData().getTotalDisAmt()));
//        lbSalesQuotCreateTotal.setText(String.valueOf(responseBody.getInvoiceData().getTaxableAmount()));
//        lbSalesQuotCreateTax.setText(String.valueOf(responseBody.getInvoiceData().getTotalTax()));
//        lbSalesQuotCreateGrossTotal.setText(responseBody.getRow1().get(0).getBaseAmt().toString());// total Base Amt
//        lbSalesQuotCreateBillAmount.setText(responseBody.getRow1().get(0).getFinalAmt().toString());

        if (responseBody.getGstDetails() != null) {
            ObservableList<GstDetailsDTO> gstDetailsList = FXCollections.observableArrayList();
            gstDetailsList.addAll(responseBody.getGstDetails());
            cmbSalesQuotationCreateClientGST.setItems(gstDetailsList);

            String gstNo = responseBody.getInvoiceData().getGstNo();
            for (GstDetailsDTO gstDetailsDTO : gstDetailsList) {
                if (gstDetailsDTO.getGstNo().equalsIgnoreCase(gstNo)) {
                    cmbSalesQuotationCreateClientGST.getSelectionModel().select(gstDetailsDTO);
                }
            }

            cmbSalesQuotationCreateClientGST.setConverter(new StringConverter<GstDetailsDTO>() {
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


        tblvSalesQuotationCmptRow.getItems().clear();
        int index = 0;

        Double totaldis = 0.0, totalTax = 0.0;
        Double totalGrossAmt = 0.0, totalTaxable = 0.0, totalBillAmt = 0.0;

        int sr = 1;
        for (Row1 mRow : responseBody.getRow1()) {
            System.out.println("Row1>>>>>>>>> " + mRow);
            productId = String.valueOf(mRow.getProductId());
            //Pass productId to ProductPopup for Focus in open ProductPopup in Edit sales Quot
            if (productId != "") {
                SingleInputDialogs.productId = productId;
            } else {
                SingleInputDialogs.productId = "";
            }
            fetchSelectedProductData(productId);
            getSupplierListbyProductId(productId);
            TranxSelectedProduct selectedProduct = TranxCommonPopUps.getSelectedProductFromProductId(mRow.getProductName());//new

            PurInvoiceCommunicator.levelAForPurInvoiceObservableList.clear();

            SalesQuotationProductDTO row = new SalesQuotationProductDTO();
            row.setSr_no(String.valueOf(sr));
            row.setSelectedProduct(selectedProduct);//new
            row.setParticulars(mRow.getProductName());
            row.setProduct_id(mRow.getProductId().toString());
            row.setPacking(mRow.getPackName());
            row.setQuantity(String.valueOf(decimalFormat.format(Double.valueOf(mRow.getQty().intValue()))));
//            row.setBatchNo(mRow.getBatchNo());
            System.out.println("setUnit " + mRow.getUnitName() + " setUnit_id " + mRow.getUnitId() + " setRate " + mRow.getRate());
            row.setUnit(mRow.getUnitName().toString());//new
            row.setUnit_id(mRow.getUnitId().toString());
            row.setUnit_conv(mRow.getUnitConv());
            row.setRate(String.valueOf(mRow.getRate()));
            row.setGross_amt(String.valueOf(mRow.getGrossAmt()));
            row.setDis_per(String.valueOf(mRow.getDisPer()));
            row.setDis_amt(String.valueOf(mRow.getDisAmt()));
            row.setTax_per(String.valueOf(mRow.getIgst()));
            row.setNet_amt(String.valueOf(mRow.getFinalAmt()));
            row.setFree_qty(String.valueOf(decimalFormat.format(Double.valueOf(mRow.getFreeQty()))));
            row.setIgst(mRow.getIgst());
            row.setSgst(mRow.getSgst());
            row.setCgst(mRow.getCgst());
            row.setTotal_igst(mRow.getTotalIgst());
            row.setTotal_sgst(mRow.getTotalSgst());
            row.setTotal_cgst(mRow.getTotalCgst());
            row.setTax_per(String.valueOf(mRow.getGst()));
            row.setTaxable_amt(mRow.getTotalAmt());
            row.setDetails_id(String.valueOf(mRow.getDetailsId()));
            row.setGross_amt(String.valueOf(mRow.getGrossAmt()));

//            SalesQuotationProductDTO.setBatchNo(mRow.getBatchNo());
//            row.setTax_per(String.valueOf(mRow.getIgst()));
//            row.setLevelA(mRow.getLevelA()!=null?mRow.getLevelA():"");
//            row.setLevelB(mRow.getLevelA()!=null?mRow.getLevelB():"");
//            row.setLevelB(mRow.getLevelA()!=null?mRow.getLevelC():"");
            row.setLevel_a_id(mRow.getLevelAId());
            row.setLevel_b_id(mRow.getLevelBId());
            row.setLevel_c_id(mRow.getLevelCId());
            // SalesQuotationProductDTO.setBase_amt(String.valueOf(mRow.getBaseAmt()));
//            SalesQuotationProductDTO.setB_no(mRow.getBatchNo());
//            SalesQuotationProductDTO.setB_rate(String.valueOf(mRow.getbRate()));
//            SalesQuotationProductDTO.setRate_a(String.valueOf(mRow.getMinRateA()));
//            SalesQuotationProductDTO.setRate_b(String.valueOf(mRow.getMinRateB()));
//            SalesQuotationProductDTO.setRate_c(String.valueOf(mRow.getMinRateC()));
//            SalesQuotationProductDTO.setCosting(String.valueOf(mRow.getCosting()));
//            SalesQuotationProductDTO.setCosting_with_tax(String.valueOf(mRow.getCostingWithTax()));
//            SalesQuotationProductDTO.setMin_margin(String.valueOf(mRow.getMinMargin()));
//            SalesQuotationProductDTO.setB_expiry(mRow.getbExpiry());
//            SalesQuotationProductDTO.setIs_batch(String.valueOf(mRow.getIsBatch()));
//            SalesQuotationProductDTO.setManufacturing_date(String.valueOf(mRow.getManufacturingDate()));
//            SalesQuotationProductDTO.setB_purchase_rate(String.valueOf(mRow.getPurchaseRate()));
//            SalesQuotationProductDTO.setB_details_id(String.valueOf(mRow.getbDetailsId()));

            row.setFree_qty(String.valueOf(mRow.getFreeQty()));
            row.setSerialNo("");
            row.setReference_id("");
            row.setReference_type("");
            row.setGst(Double.valueOf(mRow.getDetailsId()));
            row.setBase_amt(0.0);
            row.setRow_dis_amt(0.0);
            row.setDisPer2(String.valueOf(mRow.getDisPer2()));
            row.setTotal_amt(0.0);
//            row.setIgst(mRow.getIgst());
//            row.setCgst(mRow.getCgst());
//            row.setSgst(mRow.getSgst());
//            row.setTotal_igst(mRow.getTotalIgst());
//            row.setTotal_sgst(mRow.getTotalSgst());
//            row.setTotal_cgst(mRow.getTotalCgst());
            row.setFinal_amt(mRow.getFinalAmt());
            tblvSalesQuotationCmptRow.getItems().addAll(row);
            totalGrossAmt = totalGrossAmt + (mRow.getGrossAmt());
            System.out.println("row totalTaxable " + mRow.getBaseAmt());
            totalTaxable = totalTaxable + (mRow.getBaseAmt() != null ? Double.valueOf(mRow.getBaseAmt()) : 0.0);
            System.out.println("mRow.getFinalAmt() " + mRow.getFinalAmt());
            totalBillAmt = totalBillAmt + mRow.getFinalAmt();

            totaldis = totaldis + (mRow.getRowDisAmt());
            System.out.println("totaldis" + totaldis);
            totalTax = totalTax + (mRow.getTotalIgst());
            System.out.println("totalTax" + totalTax);
            lbSalesQuotCreateDiscount.setText(String.format( "%.2f", totaldis));
            lbSalesQuotCreateTax.setText(String.format( "%.2f", totalTax));
            lbSalesQuotCreateBillAmount.setText(String.format("%.2f", totalBillAmt));
            lbSalesQuotCreateTotal.setText(String.format( "%.2f",(totalGrossAmt - totaldis)));
            lbSalesQuotCreateGrossTotal.setText(String.format( "%.2f",(totalGrossAmt)));
            List<LevelAForPurInvoice> levelAForPurInvoiceList = ProductUnitsPacking2.getAllProductUnitsPackingFlavour(row.getProduct_id());
            System.out.println("levelAForPurInvoiceList" + levelAForPurInvoiceList);
            ObservableList<LevelAForPurInvoice> observableLevelAList = FXCollections.observableArrayList(levelAForPurInvoiceList);
            if (index >= 0 && index < PurInvoiceCommunicator.levelAForPurInvoiceObservableList.size()) {
                System.out.println("Index If >> : " + index);
                PurInvoiceCommunicator.levelAForPurInvoiceObservableList.set(index, observableLevelAList);
                tblvSalesQuotationCmptRow.getItems().get(index).setLevelA(null);
                tblvSalesQuotationCmptRow.getItems().get(index).setLevelA(levelAForPurInvoiceList.get(0).getLabel());
                for (LevelAForPurInvoice levelAForPurInvoice : PurInvoiceCommunicator.levelAForPurInvoiceObservableList.get(index)) {
                    if (tblvSalesQuotationCmptRow.getItems().get(index).getLevel_a_id().equals(levelAForPurInvoice.getValue())) {
                        tblvSalesQuotationCmptRow.getItems().get(index).setLevelA(null);
                        tblvSalesQuotationCmptRow.getItems().get(index).setLevelA(levelAForPurInvoice.getLabel());
                    }
                }
            } else {
                System.out.println("Index else >> : " + index);
                PurInvoiceCommunicator.levelAForPurInvoiceObservableList.add(observableLevelAList);
                tblvSalesQuotationCmptRow.getItems().get(index).setLevelA(null);
                tblvSalesQuotationCmptRow.getItems().get(index).setLevelA(levelAForPurInvoiceList.get(0).getLabel());

                for (LevelAForPurInvoice levelAForPurInvoice : PurInvoiceCommunicator.levelAForPurInvoiceObservableList.get(index)) {
                    if (tblvSalesQuotationCmptRow.getItems().get(index).getLevel_a_id().equals(levelAForPurInvoice.getValue())) {
                        tblvSalesQuotationCmptRow.getItems().get(index).setLevelA(null);
                        tblvSalesQuotationCmptRow.getItems().get(index).setLevelA(levelAForPurInvoice.getLabel());
                    }
                }
            }
            index++;

            sr++;
        }
//        tblvSalesQuotationCmptRow.refresh();

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
            System.out.println("i am the bosh" + dataArray);
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
            tblcSalesQuotationCreateClienName.setCellValueFactory(new PropertyValueFactory<>("supplierName"));
            tblcSalesQuotationCreateInvoiceNo.setCellValueFactory(new PropertyValueFactory<>("supplierInvNo"));
            tblcSalesQuotationCreateInvoiceDate.setCellValueFactory(new PropertyValueFactory<>("supplierInvDate"));
            tblcSalesQuotationCreateBatch.setCellValueFactory(new PropertyValueFactory<>("supplierBatch"));
            tblcSalesQuotationCreateMrp.setCellValueFactory(new PropertyValueFactory<>("supplierMrp"));
            tblcSalesQuotationCreateQuantity.setCellValueFactory(new PropertyValueFactory<>("supplierQty"));
            tblcSalesQuotationCreateRate.setCellValueFactory(new PropertyValueFactory<>("supplierRate"));
            tblcSalesQuotationCreateCost.setCellValueFactory(new PropertyValueFactory<>("supplierCost"));
            tblcSalesQuotationCreateDisPer.setCellValueFactory(new PropertyValueFactory<>("supplierDisPer"));
            tblcSalesQuotationCreateDis.setCellValueFactory(new PropertyValueFactory<>("supplierDisAmt"));

            tvSalesQuotCreateprodInvoiceDetails.setItems(supplierDataList);

        }
    }

    //todo: On Clicking on the Cancel Button From Create or Edit page redirect Back to List page
    public void backToSalesQuotationList() {
        AlertUtility.CustomCallback callback = number -> {
            if (number == 1) {
                GlobalController.getInstance().addTabStatic(SALES_QUOTATION_LIST_SLUG, false);
            } else {
                System.out.println("working!");
            }
        };
        AlertUtility.AlertConfirmation(AlertUtility.alertTypeConfirmation, LedgerMessageConsts.msgGoList, callback);
    }

}// end of main function

class TextFieldTableCellForSaleQuotation extends TableCell<SalesQuotationProductDTO, String> {
    private TextField textField;
    private String columnName;

    private TableCellCallback<Object[]> callback;

    private TableCellCallback<Integer> product_callback;
    private TableCellCallback<Object[]> addPrdCalbak;

    private TextField button;


    public TextFieldTableCellForSaleQuotation(String columnName, TableCellCallback<Object[]> callback) {
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
        rateColumn();
        discountAmount();
        discountPer();
//        taxAmount();

//        batchColumn();

        netAmountColumn();

    }

    public TextFieldTableCellForSaleQuotation(String columnName, TableCellCallback<Object[]> callback, TableCellCallback<Integer> product_callback) {
        this.columnName = columnName;
        this.textField = new TextField();

        this.product_callback = product_callback;

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

    }

    public TextFieldTableCellForSaleQuotation(String columnName, TableCellCallback<Object[]> callback, TableCellCallback<Integer> product_callback, TextField button) {
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

//        batchColumn();

        netAmountColumn();

    }

    public TextFieldTableCellForSaleQuotation(String columnName, TableCellCallback<Object[]> callback, TableCellCallback<Integer> product_callback, TextField button, TableCellCallback<Object[]> addPrdCalbak) {
        this.columnName = columnName;
        this.textField = new TextField();

        this.product_callback = product_callback;
        this.addPrdCalbak = addPrdCalbak;
        this.callback = callback;
        this.button = button;
        this.addPrdCalbak =addPrdCalbak;
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
    }

    private void particularsColumn() {
        if ("tblcSalesQuotCreateParticular".equals(columnName)) {
            textField.setEditable(false);
            textField.setOnKeyPressed(event -> {
                if (event.getCode() == KeyCode.SPACE) {
                    System.out.println("i am in" + event.getCode());
                    openProduct(getIndex());
                }

                if (event.isShiftDown() && event.getCode() == KeyCode.TAB) {
                    if (getIndex() == 1) {
                        TableColumn<SalesQuotationProductDTO, ?> colName = getTableView().getColumns().get(12);
                        getTableView().edit(getIndex() - 1, colName);
                    } else {
                        TableColumn<SalesQuotationProductDTO, ?> colName = getTableView().getColumns().get(12);
                        getTableView().edit(getIndex() - 1, colName);
                    }
                }

                if (event.getCode() == KeyCode.ENTER || !event.isShiftDown() && event.getCode() == KeyCode.TAB) {
                    if (textField.getText().isEmpty() && getIndex() == getTableView().getItems().size() - 1 && getIndex() != 0) {
                        Platform.runLater(() -> {
                            button.requestFocus();
                        });
                    } else if (!textField.getText().isEmpty()) {
                        TableColumn<SalesQuotationProductDTO, ?> colName = getTableView().getColumns().get(6);
                        getTableView().edit(getIndex(), colName);
                    } else {
                        textField.requestFocus();
                    }
                    event.consume();
                }

            });

            textField.setOnMouseClicked(event -> {
                openProduct(getIndex());
            });
        }
    }

    private void qtyColumn() {
        if ("tblcSalesQuotCreateQty".equals(columnName)) {
            textField.setEditable(true);

            textField.setOnKeyPressed(event -> {
                CommonValidationsUtils.restrictToDecimalNumbers(textField);
                if (event.isShiftDown() && event.getCode() == KeyCode.TAB) {
                    TableColumn<SalesQuotationProductDTO, ?> colName = getTableView().getColumns().get(6);
                    getTableView().edit(getIndex(), colName);

                } else if (event.getCode() == KeyCode.ENTER || !event.isShiftDown() && event.getCode() == KeyCode.TAB) {
                    Integer index = getIndex();
                    if (!textField.getText().isEmpty() || !textField.getText().equalsIgnoreCase("0")) {
                        TableColumn<SalesQuotationProductDTO, ?> colName = getTableView().getColumns().get(8);
                        getTableView().edit(index, colName);
                    } else {
                        textField.requestFocus();
                    }
                    salesQuotationCalculation.rowCalculationForPurcInvoice(index, getTableView(), callback);
                }
//                else if (event.getCode() == KeyCode.DOWN) {
//                    getTableView().getItems().addAll(new SalesQuotationProductDTO("", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", ""));
//                    TableColumn<SalesQuotationProductDTO, ?> colName = getTableView().getColumns().get(1);
//                    getTableView().edit(getIndex() + 1, colName);
//                }

            });
        }
    }

    //    private void freeQtyColumn() {
//        if ("tblcPurOrderCmpTRowFreeQty".equals(columnName)) {
//            textField.setEditable(true);
//            textField.setOnKeyPressed(event -> {
//                if (event.isShiftDown() && event.getCode() == KeyCode.TAB) {
//                    TableColumn<SalesQuotationProductDTO, ?> colName = getTableView().getColumns().get(7);
//                    System.out.println("Col:" + colName.getText());
//                    getTableView().edit(getIndex(), colName);
//
//                } else if (event.getCode() == KeyCode.ENTER || event.getCode() == KeyCode.TAB) {
//                    int index = getIndex();
//                    TableColumn<SalesQuotationProductDTO, ?> colName = getTableView().getColumns().get(9);
//                    getTableView().edit(getIndex(), colName);
//                    salesQuotationCalculation.rowCalculationForPurcInvoice(index, getTableView(), callback);
//                }
//            });
//           /* TableColumn<SalesQuotationProductDTO, ?> colName = getTableView().getColumns().get(8);
//            getTableView().edit(getIndex(), colName);*/
//
//        }
//    }
    private void rateColumn() {
        if ("tblcSalesQuotCreateRate".equals(columnName)) {
            textField.setEditable(true);

            textField.setOnKeyPressed(event -> {
                if (event.isShiftDown() && event.getCode() == KeyCode.TAB) {
                    TableColumn<SalesQuotationProductDTO, ?> colName = getTableView().getColumns().get(7);
                    getTableView().edit(getIndex() - 1, colName);
                } else if (event.getCode() == KeyCode.ENTER || !event.isShiftDown() && event.getCode() == KeyCode.TAB) {
                    int index = getIndex();
                    if (!textField.getText().isEmpty() || !textField.getText().equalsIgnoreCase("0.0")) {
                        TableColumn<SalesQuotationProductDTO, ?> colName = getTableView().getColumns().get(11);
                        getTableView().edit(index, colName);
                    } else {
                        textField.requestFocus();
                        event.consume();
                    }
                    salesQuotationCalculation.rowCalculationForPurcInvoice(index, getTableView(), callback);
                }
//                else if (event.getCode() == KeyCode.DOWN) {
//                    getTableView().getItems().addAll(new SalesQuotationProductDTO("", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", ""));
//                    TableColumn<SalesQuotationProductDTO, ?> colName = getTableView().getColumns().get(1);
//                    getTableView().edit(getIndex() + 1, colName);
//                }
            });
        }
    }

    private void discountPer() {
        if ("tblcSalesQuotCreateDisPer".equals(columnName)) {
            textField.setEditable(true);

            textField.setOnKeyPressed(event -> {
                if (event.isShiftDown() && event.getCode() == KeyCode.TAB) {
                    TableColumn<SalesQuotationProductDTO, ?> colName = getTableView().getColumns().get(9);
                    getTableView().edit(getIndex(), colName);
                }
                if (event.getCode() == KeyCode.ENTER || !event.isShiftDown() && event.getCode() == KeyCode.TAB) {
                    int index = getIndex();
                    TableColumn<SalesQuotationProductDTO, ?> colName = getTableView().getColumns().get(12);
                    getTableView().edit(index, colName);
                    salesQuotationCalculation.rowCalculationForPurcInvoice(index, getTableView(), callback);
                }
            });
        }
    }

    private void discountAmount() {
        if ("tblcSalesQuotCreateDisRs".equals(columnName)) {
            textField.setEditable(true);

            textField.setOnKeyPressed(event -> {
                if (event.isShiftDown() && event.getCode() == KeyCode.TAB) {
                    TableColumn<SalesQuotationProductDTO, ?> colName = getTableView().getColumns().get(11);
                    getTableView().edit(getIndex(), colName);
                } else if (event.getCode() == KeyCode.ENTER || !event.isShiftDown() && event.getCode() == KeyCode.TAB) {
                    int index = getIndex();
                    getTableView().getItems().addAll(new SalesQuotationProductDTO("", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", ""));
                    TableColumn<SalesQuotationProductDTO, ?> colName = getTableView().getColumns().get(1);
                    getTableView().edit(getIndex() + 1, colName);
                    salesQuotationCalculation.rowCalculationForPurcInvoice(index, getTableView(), callback);

                }
            });
        }
    }
//    private void taxAmount(){
//        if("tblcSalesQuotCreateTax".equals(columnName)){
//            textField.setEditable(true);
//
//            textField.setOnKeyPressed(event ->{
//                if(event.getCode() == KeyCode.ENTER || event.getCode() == KeyCode.TAB){
//                    Integer index = getIndex();
//                    salesQuotationCalculation.rowCalculationForPurcInvoice(index, getTableView(),callback);
//                }
//            });
//        }
//    }

    private void netAmountColumn() {

        if ("tblcSalesQuotCreateNetAmt".equals(columnName)) {
            textField.setEditable(false);
            Platform.runLater(() -> {
                textField.setOnKeyPressed(event -> {
                    if (event.getCode() == KeyCode.SPACE) {
                        int current_index = getTableRow().getIndex();
                        //new start
                        if (getTableRow().getItem().getSr_no() != null) {
                            int serial_no = Integer.parseInt(getTableRow().getItem().getSr_no());
                            getTableView().getItems().addAll(new SalesQuotationProductDTO("", String.valueOf(current_index + 1), "1",
                                    "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", ""));
                        }
                        //new end


                    }
                });
            });
        }
    }


    //new start
    private void openProduct(int currentIndex) {
        System.out.println("i am in");
        SalesQuotationProductDTO selectedRow = getTableView().getItems().get(currentIndex);
        System.out.println("selectedRow" + selectedRow);
//        System.out.println("selectedRow" +selectedRow.getProduct_id().isEmpty()?0: Integer.valueOf(selectedRow.getProduct_id()));
        TranxCommonPopUps.openProductPopUp(Communicator.stage, selectedRow.getProduct_id().isEmpty() ? 0 : Integer.valueOf(selectedRow.getProduct_id()), "Product", input -> {
//            Integer selectedIndex = tblTranxSalesInvoiceCreateCMPTRow.getSelectionModel().getSelectedIndex();
//            System.out.println("input);
            if (input != null) {
                getTableRow().getItem().setParticulars(input.getProductName());
                getTableRow().getItem().setProduct_id(input.getProductId().toString());
                getTableRow().getItem().setPacking(input.getPackageName());
                getTableRow().getItem().setIs_batch(input.getBatch().toString());
                getTableRow().getItem().setTax_per(input.getIgst().toString());
//                getTableRow().getItem().setUnit(input.get());
                getTableRow().getItem().setSelectedProduct(input);
                getTableRow().getItem().setRate(input.getSalesRate().toString());
                List<LevelAForPurInvoice> levelAForPurInvoiceList = ProductUnitsPacking2.getAllProductUnitsPackingFlavour(input.getProductId().toString());
                System.out.println("levelAForPurInvoiceList" + levelAForPurInvoiceList);

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
                TableColumn<SalesQuotationProductDTO, ?> colName = getTableView().getColumns().get(6);
                getTableView().edit(getIndex(),colName);
                Integer product_id = Integer.valueOf(getTableRow().getItem().getProduct_id());
                if (product_callback != null) {
                    product_callback.call(product_id);
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
//
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
//            getTableRow().getItem().setPacking(packaging);
//            getTableRow().getItem().setIs_batch(is_batch);
//            getTableRow().getItem().setTax_per(taxper);
//            getTableRow().getItem().setUnit(unit);
//            getTableRow().getItem().setRate(salesrate);
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
//
//            List<LevelAForPurInvoice> levelAForPurInvoiceList = ProductUnitsPacking.getAllProductUnitsPackingFlavour(productId);
//            int index = getTableRow().getIndex();
//            ObservableList<LevelAForPurInvoice> observableLevelAList = FXCollections.observableArrayList(levelAForPurInvoiceList);
//
//            if (index >= 0 && index < PurInvoiceCommunicator.levelAForPurInvoiceObservableList.size()) {
//                PurInvoiceCommunicator.levelAForPurInvoiceObservableList.set(index, observableLevelAList);
//                getTableRow().getItem().setLevel_a_id(null);
//                getTableRow().getItem().setLevel_a_id(levelAForPurInvoiceList.get(0).getLabel());
//
//            }
//            else {
//                PurInvoiceCommunicator.levelAForPurInvoiceObservableList.add(observableLevelAList);
//                getTableRow().getItem().setLevel_a_id(null);
//                getTableRow().getItem().setLevel_a_id(levelAForPurInvoiceList.get(0).getLabel());
//            }
//
//            Integer product_id = Integer.parseInt(productId);
//            if(product_callback!=null){
//                product_callback.call(product_id);
//            }
//
//        });
//    }

//    public void openBatchWindow(){
//        int current_index = getTableRow().getIndex();
//        List<SalesQuotationProductDTO> tableList = getTableView().getItems();
//
//        List<BatchWindowTableDTO> batchWindowTableDTOList;
//        SalesQuotationProductDTO current_table_row = tableList.get(current_index);
//        batchWindowTableDTOList = current_table_row.getBatchWindowTableDTOList();
//
//        Map<String,String> product_details_to_batch_window = new HashMap<>();
//        product_details_to_batch_window.put("product_id",current_table_row.getProduct_id());
//        product_details_to_batch_window.put("levelA_id",current_table_row.getLevelA_id());
//        product_details_to_batch_window.put("levelB_id",current_table_row.getLevelB_id());
//        product_details_to_batch_window.put("levelC_id",current_table_row.getLevelC_id());
//        product_details_to_batch_window.put("unit_id",current_table_row.getUnit_id());
//        product_details_to_batch_window.put("tax",current_table_row.getTax());
//        product_details_to_batch_window.put("ledger_id",getTableView().getItems().get(0).getLedger_id());
//
//        Long ledgerid = Long.parseLong(getTableView().getItems().get(0).getLedger_id());
//
//        if(current_table_row.getIs_batch().equals("true") && ledgerid!=0) {
//            BatchWindow.batchWindow(String.valueOf(current_index),product_details_to_batch_window, batchWindowTableDTOList, input -> {
//
//                getTableView().getItems().get(Integer.parseInt(input.get(0).getCurrentIndex())).setBatchWindowTableDTOList(input);
//
//
//
////                System.out.println("table batch: "+getTableView().getItems().get(Integer.parseInt(input.get(0).getCurrentIndex())).getBatchWindowTableDTOList());
////
//                for(BatchWindowTableDTO batchWindowTableDTOSs: input){
//                    getTableView().getItems().get(Integer.parseInt(input.get(0).getCurrentIndex())).setBatch_or_serial(batchWindowTableDTOSs.getBatchNo());
//                    getTableView().getItems().get(Integer.parseInt(input.get(0).getCurrentIndex())).setQuantity(batchWindowTableDTOSs.getQuantity());
//                    getTableView().getItems().get(Integer.parseInt(input.get(0).getCurrentIndex())).setRate(batchWindowTableDTOSs.getPurchaseRate());
//                    getTableView().getItems().get(Integer.parseInt(input.get(0).getCurrentIndex())).setDis_per(batchWindowTableDTOSs.getDiscountPercentage());
//                    getTableView().getItems().get(Integer.parseInt(input.get(0).getCurrentIndex())).setDis_amt(batchWindowTableDTOSs.getDiscountAmount());
//
//
//                    getTableView().getItems().get(Integer.parseInt(input.get(0).getCurrentIndex())).setB_details_id(batchWindowTableDTOSs.getB_details_id());
//
//                    getTableView().getItems().get(Integer.parseInt(input.get(0).getCurrentIndex())).setB_no(batchWindowTableDTOSs.getBatchNo());
//
//                    getTableView().getItems().get(Integer.parseInt(input.get(0).getCurrentIndex())).setB_rate(batchWindowTableDTOSs.getMrp());
//
//                    getTableView().getItems().get(Integer.parseInt(input.get(0).getCurrentIndex())).setB_purchase_rate(batchWindowTableDTOSs.getPurchaseRate());
//
//                    getTableView().getItems().get(Integer.parseInt(input.get(0).getCurrentIndex())).setB_expiry(batchWindowTableDTOSs.getExpiryDate());
//
//                    getTableView().getItems().get(Integer.parseInt(input.get(0).getCurrentIndex())).setRate_a(batchWindowTableDTOSs.getFsr_mh());
//                    getTableView().getItems().get(Integer.parseInt(input.get(0).getCurrentIndex())).setRate_b(batchWindowTableDTOSs.getFsr_ai());
//                    getTableView().getItems().get(Integer.parseInt(input.get(0).getCurrentIndex())).setRate_c(batchWindowTableDTOSs.getCsr_mh());
//                    getTableView().getItems().get(Integer.parseInt(input.get(0).getCurrentIndex())).setRate_d(batchWindowTableDTOSs.getCsr_ai());
//
//
//                    getTableView().getItems().get(Integer.parseInt(input.get(0).getCurrentIndex())).setMin_margin(batchWindowTableDTOSs.getMargin());
//
//                    getTableView().getItems().get(Integer.parseInt(input.get(0).getCurrentIndex())).setManufacturing_date(batchWindowTableDTOSs.getManufacturingDate());
//
//
//                }
//
//                PurchaseInvoiceCalculation.rowCalculationForPurcInvoice(Integer.parseInt(input.get(0).getCurrentIndex()),getTableView(),callback);
//            });
//        }
//    }


    public void textfieldStyle() {
        if (columnName.equals("tblcSalesQuotCreateParticular")) {
            textField.setPromptText("Particulars");
            textField.setEditable(false);
        } else {
            textField.setPromptText("0");
        }

        /*textField.setPrefHeight(38);
        textField.setMaxHeight(38);
        textField.setMinHeight(38);*/

        if (columnName.equals("tblcSalesQuotCreateParticular")) {
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


        if (columnName.equals("tblcSalesQuotCreateNetAmt")) {
            this.textField.setOnKeyPressed(event -> {
                if (event.getCode() == KeyCode.ENTER) {
                    commitEdit(textField.getText());
                    getTableView().getItems().addAll(new SalesQuotationProductDTO("", "", "", "1", "", "", "", "", "", "", "", "", "", "", "", "", "", "", ""));
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
        if (item != null && columnName.equals("tblcSalesQuotCreateParticular")) {
            ((SalesQuotationProductDTO) item).setParticulars(newValue.isEmpty() ? "" : newValue);
        }
//        else if (columnName.equals("tcBatch")) {
//            ( getTableRow().getItem()).setBatch_or_serial(newValue.isEmpty() ? "" : newValue);
//        }
        else if (columnName.equals("tblcSalesQuotCreateQty")) {
            (getTableRow().getItem()).setQuantity(newValue.isEmpty() ? "0" : newValue);
        } else if (columnName.equals("tblcSalesQuotCreateRate")) {
            (getTableRow().getItem()).setRate(newValue.isEmpty() ? "0.0" : newValue);
        } else if (columnName.equals("tblcSalesQuotCreateGrossAmt")) {
            (getTableRow().getItem()).setGross_amt(newValue.isEmpty() ? "0.0" : newValue);
        } else if (columnName.equals("tblcSalesQuotCreateDisPer")) {
            (getTableRow().getItem()).setDis_per(newValue.isEmpty() ? "0.0" : newValue);
        } else if (columnName.equals("tblcSalesQuotCreateDisRs")) {
            (getTableRow().getItem()).setDis_amt(newValue.isEmpty() ? "0.0" : newValue);
        } else if (columnName.equals("tblcSalesQuotCreateTax")) {
            (getTableRow().getItem()).setTax_per(newValue.isEmpty() ? "0.0" : newValue);
        } else if (columnName.equals("tblcSalesQuotCreateNetAmt")) {
            (getTableRow().getItem()).setNet_amt(newValue.isEmpty() ? "0.0" : newValue);
        }

    }
}

class ComboBoxTableCellForLevelASalesQuot extends TableCell<SalesQuotationProductDTO, String> {

    String columnName;
    private final ComboBox<String> comboBox;
    ObservableList<LevelAForPurInvoice> comboList = null;

    public ComboBoxTableCellForLevelASalesQuot(String columnName) {

        this.columnName = columnName;
        this.comboBox = new ComboBox<>();

        this.comboBox.setPromptText("Select");

       /* comboBox.setPrefHeight(38);
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

                            getTableRow().getItem().setLevel_a_id(levelAForPurInvoice.getValue());

                            ObservableList<LevelBForPurInvoice> observableLevelAList = FXCollections.observableArrayList(levelAForPurInvoice.getLevelBOpts());

                            int index = getTableRow().getIndex();

                            if (index >= 0 && index < PurInvoiceCommunicator.levelBForPurInvoiceObservableList.size()) {
                                PurInvoiceCommunicator.levelBForPurInvoiceObservableList.set(index, observableLevelAList);
                                getTableRow().getItem().setLevelB(null);
                                getTableRow().getItem().setLevelB(levelAForPurInvoice.getLevelBOpts().get(0).getLabel());

                                for (LevelBForPurInvoice levelBForPurInvoice : PurInvoiceCommunicator.levelBForPurInvoiceObservableList.get(index)) {
                                    if (levelBForPurInvoice.getValue().equals(getTableRow().getItem().getLevel_b_id())) {
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
                                    if (levelBForPurInvoice.getValue().equals(getTableRow().getItem().getLevel_b_id())) {
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
        TableRow<SalesQuotationProductDTO> row = getTableRow();
        if (row != null) {
            SalesQuotationProductDTO item = row.getItem();
            if (item != null) {
                item.setLevelA(newValue);
            }
        }
    }

}

class ComboBoxTableCellForLevelBSalesQuot extends TableCell<SalesQuotationProductDTO, String> {

    String columnName;
    private final ComboBox<String> comboBox;
    ObservableList<LevelBForPurInvoice> comboList = null;

    public ComboBoxTableCellForLevelBSalesQuot(String columnName) {
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
        this.comboBox.setFocusTraversable(true);
        AutoCompleteBox autoCompleteBox1 = new AutoCompleteBox<>(this.comboBox, -1);

        this.comboBox.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal) {
                commitEdit(String.valueOf(comboBox.getValue()));
            }
        });

        comBoAction();
    }

//    public void comBoAction() {
//        comboBox.setOnAction(e -> {
//            String selectedItem = comboBox.getSelectionModel().getSelectedItem();
//
//            if (selectedItem != null) {
//                if (PurInvoiceCommunicator.levelBForPurInvoiceObservableList != null && !PurInvoiceCommunicator.levelBForPurInvoiceObservableList.isEmpty()) {
//                    for (LevelBForPurInvoice levelBForPurInvoice : PurInvoiceCommunicator.levelBForPurInvoiceObservableList.get(getTableRow().getIndex())) {
//                        if (selectedItem.equals(levelBForPurInvoice.getLabel())) {
//
//                            getTableRow().getItem().setLevel_b_id(levelBForPurInvoice.getValue());
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
//
//                                for (LevelCForPurInvoice levelCForPurInvoice : PurInvoiceCommunicator.levelCForPurInvoiceObservableList.get(index)) {
//                                    if (levelCForPurInvoice.getValue().equals(getTableRow().getItem().getLevel_c_id())) {
//                                        getTableRow().getItem().setLevelC(null);
//                                        getTableRow().getItem().setLevelC(levelCForPurInvoice.getLabel());
//                                    }
//                                }
//
//                            }
//                            else {
//                                PurInvoiceCommunicator.levelCForPurInvoiceObservableList.add(observableLevelAList);
//                                getTableRow().getItem().setLevelC(null);
//                                getTableRow().getItem().setLevelC(levelBForPurInvoice.getLevelCOpts().get(0).getLabel());
//
//                                for (LevelCForPurInvoice levelCForPurInvoice : PurInvoiceCommunicator.levelCForPurInvoiceObservableList.get(index)) {
//                                    if (levelCForPurInvoice.getValue().equals(getTableRow().getItem().getLevel_c_id())) {
//                                        getTableRow().getItem().setLevelC(null);
//                                        getTableRow().getItem().setLevelC(levelCForPurInvoice.getLabel());
//                                    }
//                                }
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

    public void comBoAction() {
        comboBox.setOnAction(e -> {
            String selectedItem = comboBox.getSelectionModel().getSelectedItem();

            if (selectedItem != null) {
                if (PurInvoiceCommunicator.levelBForPurInvoiceObservableList != null && !PurInvoiceCommunicator.levelBForPurInvoiceObservableList.isEmpty()) {
                    int rowIndex = getTableRow().getIndex();
                    // Ensure the index is within the bounds of the list
                    if (rowIndex >= 0 && rowIndex < PurInvoiceCommunicator.levelBForPurInvoiceObservableList.size()) {
                        List<LevelBForPurInvoice> levelBForPurInvoiceList = PurInvoiceCommunicator.levelBForPurInvoiceObservableList.get(rowIndex);

                        for (LevelBForPurInvoice levelBForPurInvoice : levelBForPurInvoiceList) {
                            if (selectedItem.equals(levelBForPurInvoice.getLabel())) {

                                getTableRow().getItem().setLevel_b_id(levelBForPurInvoice.getValue());

                                ObservableList<LevelCForPurInvoice> observableLevelAList = FXCollections.observableArrayList(levelBForPurInvoice.getLevelCOpts());

                                if (rowIndex >= 0 && rowIndex < PurInvoiceCommunicator.levelCForPurInvoiceObservableList.size()) {
                                    PurInvoiceCommunicator.levelCForPurInvoiceObservableList.set(rowIndex, observableLevelAList);
                                    getTableRow().getItem().setLevelC(null);

                                    List<LevelCForPurInvoice> levelCOpts = levelBForPurInvoice.getLevelCOpts();
                                    if (!levelCOpts.isEmpty()) {
                                        getTableRow().getItem().setLevelC(levelCOpts.get(0).getLabel());
                                    }

                                    for (LevelCForPurInvoice levelCForPurInvoice : PurInvoiceCommunicator.levelCForPurInvoiceObservableList.get(rowIndex)) {
                                        if (levelCForPurInvoice.getValue().equals(getTableRow().getItem().getLevel_c_id())) {
                                            getTableRow().getItem().setLevelC(null);
                                            getTableRow().getItem().setLevelC(levelCForPurInvoice.getLabel());
                                        }
                                    }

                                } else {
                                    PurInvoiceCommunicator.levelCForPurInvoiceObservableList.add(observableLevelAList);
                                    getTableRow().getItem().setLevelC(null);
                                    getTableRow().getItem().setLevelC(levelBForPurInvoice.getLevelCOpts().get(0).getLabel());

                                    // Check the bounds before accessing the list again
                                    if (rowIndex >= 0 && rowIndex < PurInvoiceCommunicator.levelCForPurInvoiceObservableList.size()) {
                                        for (LevelCForPurInvoice levelCForPurInvoice : PurInvoiceCommunicator.levelCForPurInvoiceObservableList.get(rowIndex)) {
                                            if (levelCForPurInvoice.getValue().equals(getTableRow().getItem().getLevel_c_id())) {
                                                getTableRow().getItem().setLevelC(null);
                                                getTableRow().getItem().setLevelC(levelCForPurInvoice.getLabel());
                                            }
                                        }
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

class ComboBoxTableCellForLevelCSalesQuot extends TableCell<SalesQuotationProductDTO, String> {

    String columnName;
    private final ComboBox<String> comboBox;
    ObservableList<LevelAForPurInvoice> comboList = null;

    public ComboBoxTableCellForLevelCSalesQuot(String columnName) {


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
                if (PurInvoiceCommunicator.levelCForPurInvoiceObservableList != null && !PurInvoiceCommunicator.levelCForPurInvoiceObservableList.isEmpty()) {

                    for (LevelCForPurInvoice levelCForPurInvoice : PurInvoiceCommunicator.levelCForPurInvoiceObservableList.get(getTableRow().getIndex())) {
                        if (selectedItem.equals(levelCForPurInvoice.getLabel())) {

                            getTableRow().getItem().setLevel_c_id(levelCForPurInvoice.getValue());
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

class ComboBoxTableCellForUnitSalesQuot extends TableCell<SalesQuotationProductDTO, String> {

    String columnName;
    TableCellCallback<Integer> unit_callback;//new
    private final ComboBox<String> comboBox;

    public ComboBoxTableCellForUnitSalesQuot(String columnName, TableCellCallback<Integer> unit_callback) {


        this.columnName = columnName;
        this.comboBox = new ComboBox<>();
        this.unit_callback = unit_callback;//new

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

            if (selectedItem != null) {
                if (PurInvoiceCommunicator.unitForPurInvoiceList != null && !PurInvoiceCommunicator.unitForPurInvoiceList.isEmpty()) {

                    for (UnitForPurInvoice unitForPurInvoice : PurInvoiceCommunicator.unitForPurInvoiceList.get(getTableRow().getIndex())) {
                        if (selectedItem.equals(unitForPurInvoice.getLabel())) {
                            getTableRow().getItem().setUnit_id(unitForPurInvoice.getValue());
                            getTableRow().getItem().setUnit_conv(unitForPurInvoice.getUnitConversion());
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
                TableColumn<SalesQuotationProductDTO, ?> colName = getTableView().getColumns().get(1);
                getTableView().edit(getIndex(), colName);
            }

            if (event.getCode() == KeyCode.ENTER || !event.isShiftDown() && event.getCode() == KeyCode.TAB) {
                TableColumn<SalesQuotationProductDTO, ?> colName = getTableView().getColumns().get(7);
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

//    @Override
//    protected void updateItem(String item, boolean empty) {
//        super.updateItem(item, empty);
//        if (empty) {
//            setGraphic(null);
//        } else {
//            comboBox.getItems().clear();
//
//            if(item!=null) {
//                comboBox.setValue(item);
//                HBox hbox = new HBox();
//                hbox.getChildren().addAll(comboBox);
//                hbox.setSpacing(5);
//                setGraphic(hbox);
//            }
//        }
//
//
//    }


    @Override
    public void commitEdit(String newValue) {
        super.commitEdit(newValue);
        (getTableRow().getItem()).setUnit(newValue);
    }

}

class ButtonTableCellQuot extends TableCell<SalesQuotationProductDTO, String> {
    private Button delete;

    public ButtonTableCellQuot() {


        this.delete = createButtonWithImage();


        delete.setOnAction(actionEvent -> {
            SalesQuotationProductDTO table = getTableView().getItems().get(getIndex());
            getTableView().getItems().remove(table);
//            getTableView().refresh();
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

class ProductUnitsPacking {

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

class salesQuotationCalculation {

    public static void rowCalculationForPurcInvoice(int rowIndex, TableView<SalesQuotationProductDTO> tableView, TableCellCallback<Object[]> callback) {
        SalesQuotationProductDTO purchaseInvoiceTable = tableView.getItems().get(rowIndex);
        if (!purchaseInvoiceTable.getQuantity().isEmpty() && Double.parseDouble(purchaseInvoiceTable.getQuantity()) > 0 && !purchaseInvoiceTable.getRate().isEmpty() && Double.parseDouble(purchaseInvoiceTable.getRate()) > 0) {

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
            r_qty = purchaseInvoiceTable.getQuantity();
            r_rate = Double.parseDouble(purchaseInvoiceTable.getRate());
            if (!purchaseInvoiceTable.getDis_amt().isEmpty())
                r_dis_amt = Double.parseDouble(purchaseInvoiceTable.getDis_amt());
            if (!purchaseInvoiceTable.getDis_per().isEmpty())
                r_dis_per = Double.parseDouble(purchaseInvoiceTable.getDis_per());
            if (purchaseInvoiceTable.getDisPer2() != null && !purchaseInvoiceTable.getDisPer2().isEmpty()) {
                disPer2 = Double.parseDouble(purchaseInvoiceTable.getDisPer2());
            } else {
                disPer2 = 0.0;
            }
            r_tax_per = !purchaseInvoiceTable.getTax_per().isEmpty()?Double.parseDouble(purchaseInvoiceTable.getTax_per()):0;
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
            SalesQuotationProductDTO selectedItem = tableView.getItems().get(rowIndex);
            selectedItem.setGross_amt(String.valueOf(base_amt));
            selectedItem.setNet_amt(String.valueOf(net_amt));
            selectedItem.setOrg_net_amt(String.valueOf(net_amt));
//        selectedItem.setOrg_taxable_amt(taxable_amt);
            selectedItem.setTaxable_amt(Double.valueOf(String.valueOf(taxable_amt)));
            selectedItem.setTotal_taxable_amt(Double.valueOf(String.valueOf(total_taxable_amt)));
            selectedItem.setIgst(Double.valueOf(String.valueOf(totalTax))); // for Tax Amount
            selectedItem.setSgst(Double.valueOf(String.valueOf(totalTax / 2))); // for Tax Amount
            selectedItem.setCgst(Double.valueOf(String.valueOf(totalTax / 2))); // for Tax  Amount
            selectedItem.setFinTaxAmt(Double.valueOf(String.valueOf(totalTax)));
            selectedItem.setFinal_dis_amt(Double.valueOf(String.valueOf(row_dis_amt)));

            // Create API Payload Parameters Adding
            selectedItem.setBase_amt(Double.valueOf(String.valueOf(base_amt)));
//        selectedItem.setGross_amt(String.valueOf(gross_amt));
            selectedItem.setDis_per(String.valueOf((r_dis_per)));
            selectedItem.setDis_per_cal(Double.valueOf(String.valueOf(total_dis_per)));
            selectedItem.setDis_amt(String.valueOf((r_dis_amt)));
            selectedItem.setDis_amt_cal(Double.valueOf(String.valueOf(r_dis_amt)));
            selectedItem.setRow_dis_amt(Double.valueOf(String.valueOf(row_dis_amt)));
            selectedItem.setGross_amt1(Double.valueOf(String.valueOf(taxable_amt)));
            // tax percentage store cmptrow
            selectedItem.setGst(Double.valueOf(String.valueOf(r_tax_per)));
            selectedItem.setIgst(Double.valueOf(String.valueOf(r_tax_per)));
            selectedItem.setSgst(Double.valueOf(String.valueOf(r_tax_per / 2)));
            selectedItem.setCgst(Double.valueOf(String.valueOf(r_tax_per / 2)));
            selectedItem.setTotal_igst(Double.valueOf(String.valueOf(totalTax)));
            selectedItem.setTotal_sgst(Double.valueOf(String.valueOf(totalTax / 2)));
            selectedItem.setTotal_cgst(Double.valueOf(String.valueOf(totalTax / 2)));
            selectedItem.setFinal_amt(Double.valueOf(String.valueOf(net_amt)));
            selectedItem.setDisPer2(String.valueOf(disPer2));

//        //Display data to table view
//        tblcPurChallCmpTRowGrossAmt.setCellValueFactory(new PropertyValueFactory<>("gross_amt"));
//        tblcPurChallCmpTRowNetAmt.setCellValueFactory(new PropertyValueFactory<>("net_amt"));
            calculateGst(tableView, callback);
        }
    }

    public static void calculateGst(TableView<SalesQuotationProductDTO> tableView, TableCellCallback<Object[]> callback) {


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
        for (SalesQuotationProductDTO purchaseInvoiceTable : tableView.getItems()) {
            if (!purchaseInvoiceTable.getQuantity().isEmpty() && Double.parseDouble(purchaseInvoiceTable.getQuantity()) > 0 && !purchaseInvoiceTable.getRate().isEmpty() && Double.parseDouble(purchaseInvoiceTable.getRate()) > 0) {

                taxPercentage = Double.parseDouble(purchaseInvoiceTable.getTax_per());
                Double quantity = Double.parseDouble(purchaseInvoiceTable.getQuantity());
                if (purchaseInvoiceTable.getFree_qty().isEmpty()) {
                    freeQuantity = 0.0;
                } else {
                    freeQuantity = Double.parseDouble(purchaseInvoiceTable.getFree_qty());
                }

                // Total Calculations of each IGST, SGST, CGST
                totalQuantity += quantity;
                totalFreeQuantity += freeQuantity;
                Double igst = Double.parseDouble(purchaseInvoiceTable.getIgst().toString());
                Double cgst = Double.parseDouble(purchaseInvoiceTable.getCgst().toString());
                Double sgst = Double.parseDouble(purchaseInvoiceTable.getSgst().toString());
                totalFinalIgst += igst;
                totalFinalSgst += sgst;
                totalFinalCgst += cgst;

                cgstTotals.put(taxPercentage, cgstTotals.get(taxPercentage) + cgst); //0.0
                sgstTotals.put(taxPercentage, sgstTotals.get(taxPercentage) + sgst);

                //Total Calculation of gross amt ,taxable ,tax,discount
                netAmount = Double.parseDouble(purchaseInvoiceTable.getNet_amt());
                totalNetAmt += netAmount;
                grossAmt = Double.parseDouble(purchaseInvoiceTable.getGross_amt());
                totalGrossAmt += grossAmt;
                taxableAmt = Double.parseDouble(purchaseInvoiceTable.getTaxable_amt().toString());
                totaltaxableAmt += taxableAmt;
                disAmt = Double.parseDouble(purchaseInvoiceTable.getFinal_dis_amt().toString());
                totalDisAmt += disAmt;
                taxAmt = Double.parseDouble(purchaseInvoiceTable.getFinTaxAmt().toString());
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

    public static void discountPropotionalCalculation(String disproportionalPer, String disproportionalAmt, String additionalCharges, TableView<SalesQuotationProductDTO> tableView, TableCellCallback<Object[]> callback) {

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
            System.out.println("Gross amt in Prop--->" + tableView.getItems().get(i).getGross_amt());
            rowCalculationForPurcInvoice(i, tableView, callback);//call row calculation function
            SalesQuotationProductDTO purchaseInvoiceTable = tableView.getItems().get(i);
            totalTaxableAmt = totalTaxableAmt + Double.parseDouble(purchaseInvoiceTable.getTaxable_amt().toString());

        }
        System.out.println("totalTaxableAmt discountPropotionalCalculation :: " + totalTaxableAmt);

        Double rowDisc = 0.0;
        //get data of netamount, tax per,and row taxable amount from cmptrow
        for (SalesQuotationProductDTO purchaseInvoiceTable : tableView.getItems()) {
            System.out.println("getFinal_dis_amt--->" + purchaseInvoiceTable.getFinal_dis_amt());
            rowDisc = Double.parseDouble(purchaseInvoiceTable.getFinal_dis_amt().toString());
            System.out.println("netAmt--->" + purchaseInvoiceTable.getNet_amt());
            netAmt = Double.parseDouble(purchaseInvoiceTable.getNet_amt());
            System.out.println("netAmt--->" + purchaseInvoiceTable.getTax_per());
            r_tax_per = Double.parseDouble(purchaseInvoiceTable.getTax_per());
            rowTaxableAmt = Double.parseDouble(purchaseInvoiceTable.getTaxable_amt().toString());
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
                    System.out.println("total netAmt  :" + netAmt + "" + rowTaxableAmt);
//
                }
            }

            //Set data to cmpTRow
            purchaseInvoiceTable.setNet_amt(String.format("%.2f", netAmt));
            purchaseInvoiceTable.setTotal_taxable_amt(Double.parseDouble(String.valueOf(totalTaxableAmtAdditional)));
            System.out.println("Total Tax Amt--->" + total_tax_amt);
            purchaseInvoiceTable.setIgst(Double.valueOf(String.valueOf(total_tax_amt)));
            purchaseInvoiceTable.setCgst(Double.valueOf(String.valueOf(total_tax_amt / 2)));
            purchaseInvoiceTable.setSgst(Double.valueOf(String.valueOf(total_tax_amt / 2)));
            purchaseInvoiceTable.setFinTaxAmt(Double.valueOf(String.valueOf(total_tax_amt)));
            purchaseInvoiceTable.setFinal_dis_amt(Double.valueOf(String.valueOf(rowDisc)));
            purchaseInvoiceTable.setInvoice_dis_amt(Double.valueOf(String.valueOf(rowDisPropAmt)));

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



//    public static void rowCalculationForPurcInvoice(int rowIndex, TableView<SalesQuotationProductDTO> tableView, TableCellCallback<Object[]> callback) {
//        SalesQuotationProductDTO purchaseInvoiceTable = tableView.getItems().get(rowIndex);
//        String purchaseInvoiceTableData = purchaseInvoiceTable.getRate();
//        System.out.println("In Row Calculation CMPTDATA=>" + purchaseInvoiceTableData);
//        // Local parameter  to calculate gross amt and net amt
//        String r_qty = "";  //declare to store row qty data
//        Double r_rate = 0.0; //declare to store row rate data
//        Double base_amt = 0.0; //declare to store base Amount data
//        Double row_fin_grossAmt = 0.0; //declare to store row final gross amount
//        Double r_dis_amt = 0.0; //declare to store row discount amount
//        Double r_dis_per = 0.0; //declare to store row discount percent
//        Double r_tax_per = 0.0; //declare to store row tax percent
//        Double total_dis_amt = 0.0; //declare to store row total discount amount
//        Double total_dis_per = 0.0; //declare to store row total discount percent
//        Double row_dis_amt = 0.0; //declare to store total discount amt data
//        Double total_grossAmt = 0.0;
//        Double totalTax = 0.0; //to store total tax amt
//        Double total_amt = 0.0; //to store total amt with discount minus
//        Double net_amt = 0.0; // to store net  amount
//        Double gross_amt = 0.0; //to store gross amount
//        Double total_base_amt = 0.0; //store base amt
//        Double dis_amt_cal = 0.0;
//        Double total_taxable_amt = 0.0; //store total taxable amount
//        Double taxable_amt = 0.0;//to store taxable amount
//        Double igst = 0.0;
//        Double cgst = 0.0;
//        Double sgst = 0.0;
//        Double totalIgst = 0.0;
//        Double totalCgst = 0.0;
//        Double totalSgst = 0.0;
//        Double prevTaxPer = 0.0;
//        Double finDisAmtCal = 0.0;
//        Double disPer2 = 0.0;//to store discount per2
//
//        //get the row level data by row index wise
//        r_qty = purchaseInvoiceTable.getQuantity();
//        r_rate = Double.parseDouble(purchaseInvoiceTable.getRate());
//        if (!purchaseInvoiceTable.getDis_amt().isEmpty())
//            r_dis_amt = Double.parseDouble(purchaseInvoiceTable.getDis_amt());
//        if (!purchaseInvoiceTable.getDis_per().isEmpty())
//            r_dis_per = Double.parseDouble(purchaseInvoiceTable.getDis_per());
//        if (purchaseInvoiceTable.getDisPer2() != null && !purchaseInvoiceTable.getDisPer2().isEmpty()) {
//            disPer2 = Double.parseDouble(purchaseInvoiceTable.getDisPer2());
//        } else {
//            disPer2 = 0.0;
//        }
//        r_tax_per = Double.parseDouble(purchaseInvoiceTable.getTax_per());
//        prevTaxPer = r_tax_per;
//
//
//        //calculate base amount
//        base_amt = Double.parseDouble(r_qty) * r_rate;
//        gross_amt = base_amt;
//        total_amt = base_amt;
//        total_base_amt = base_amt;
//        taxable_amt = base_amt;
//        total_dis_amt = r_dis_amt;
//
//        //calculate discount amt with gross amt
//
//        if (r_dis_amt > 0) {
//            gross_amt = gross_amt - total_dis_amt;
//            total_amt = total_amt - total_dis_amt;
//            total_base_amt = total_amt;
//            dis_amt_cal = total_dis_amt;
//
//            row_dis_amt = row_dis_amt + total_dis_amt;
//            taxable_amt = total_amt;
//        }
//
//        //calculate discount percentage
//        if (r_dis_per > 0) {
//            total_dis_per = (r_dis_per / 100) * (gross_amt);
//
//            gross_amt = gross_amt - total_dis_per;
//            total_base_amt = total_base_amt - total_dis_per;
//            total_amt = total_amt - total_dis_per;
//            row_dis_amt = row_dis_amt + total_dis_per;
//            taxable_amt = total_amt;
//
//        }
//
//        // tax GST and CGST and SGST Calculations
//
//        if (r_tax_per >= 0) {
//            totalTax = (r_tax_per / 100) * (total_amt);
//            net_amt = total_amt + totalTax;
//
//        }
//        // set all calculated above data to CMPTROW DTO
//        SalesQuotationProductDTO selectedItem = tableView.getItems().get(rowIndex);
//        selectedItem.setGross_amt(String.valueOf(base_amt));
//        selectedItem.setNet_amt(String.valueOf(net_amt));
//        selectedItem.setOrg_net_amt(String.valueOf(net_amt));
////        selectedItem.setOrg_taxable_amt(taxable_amt);
//        selectedItem.setTaxable_amt(Double.valueOf(String.valueOf(taxable_amt)));
//        selectedItem.setTotal_taxable_amt(Double.valueOf(String.valueOf(total_taxable_amt)));
//        selectedItem.setIgst(Double.valueOf(String.valueOf(totalTax))); // for Tax Amount
//        selectedItem.setSgst(Double.valueOf(String.valueOf(totalTax / 2))); // for Tax Amount
//        selectedItem.setCgst(Double.valueOf(String.valueOf(totalTax / 2))); // for Tax  Amount
//        selectedItem.setFinTaxAmt(Double.valueOf(String.valueOf(totalTax)));
//        selectedItem.setFinal_dis_amt(Double.valueOf(String.valueOf(row_dis_amt)));
//
//        // Create API Payload Parameters Adding
//        selectedItem.setBase_amt(Double.valueOf(String.valueOf(base_amt)));
////        selectedItem.setGross_amt(String.valueOf(gross_amt));
//        selectedItem.setDis_per(String.valueOf((r_dis_per)));
//        selectedItem.setDis_per_cal(Double.valueOf(String.valueOf(total_dis_per)));
//        selectedItem.setDis_amt(String.valueOf((r_dis_amt)));
//        selectedItem.setDis_amt_cal(Double.valueOf(String.valueOf(r_dis_amt)));
//        selectedItem.setRow_dis_amt(Double.valueOf(String.valueOf(row_dis_amt)));
//        selectedItem.setGross_amt1(Double.valueOf(String.valueOf(taxable_amt)));
//        // tax percentage store cmptrow
//        selectedItem.setGst(Double.valueOf(String.valueOf(r_tax_per)));
//        selectedItem.setIgst(Double.valueOf(String.valueOf(r_tax_per)));
//        selectedItem.setSgst(Double.valueOf(String.valueOf(r_tax_per / 2)));
//        selectedItem.setCgst(Double.valueOf(String.valueOf(r_tax_per / 2)));
//        selectedItem.setTotal_igst(Double.valueOf(String.valueOf(totalTax)));
//        selectedItem.setTotal_sgst(Double.valueOf(String.valueOf(totalTax / 2)));
//        selectedItem.setTotal_cgst(Double.valueOf(String.valueOf(totalTax / 2)));
//        selectedItem.setFinal_amt(Double.valueOf(String.valueOf(net_amt)));
//        selectedItem.setDisPer2(String.valueOf(disPer2));
//
////        //Display data to table view
////        tblcPurChallCmpTRowGrossAmt.setCellValueFactory(new PropertyValueFactory<>("gross_amt"));
////        tblcPurChallCmpTRowNetAmt.setCellValueFactory(new PropertyValueFactory<>("net_amt"));
//        calculateGst(tableView, callback);
//
//    }
//
//    public static void calculateGst(TableView<SalesQuotationProductDTO> tableView, TableCellCallback<Object[]> callback) {
//
//
////        String addChgAmt = tfPurchaseChallanAddChgAmt.getText();
////        Double addChargAmount = 0.0;
////        if(!addChgAmt.isEmpty()){
////            addChargAmount = Double.parseDouble(addChgAmt);
////        }
//
//        Map<Double, Double> cgstTotals = new HashMap<>(); //used to merge same cgst Percentage
//        Map<Double, Double> sgstTotals = new HashMap<>();//used to merge same sgst Percentage
//        // Initialize totals to zero for each tax percentage
//        for (Double taxPercentage : new Double[]{0.0, 3.0, 5.0, 12.0, 18.0, 28.0}) {
//            cgstTotals.put(taxPercentage, 0.0);
//            sgstTotals.put(taxPercentage, 0.0);
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
//
//        Double totalFinalSgst = 0.00; // variable to store total SGST Amount
//        Double totalFinalCgst = 0.00; // variable to store total CGST Amount
//        Double totalFinalIgst = 0.00; // variable to store total IGST Amount
//        Double rowDiscount = 0.0;
//
//        // Calculate totals for each transaction
//        for (SalesQuotationProductDTO purchaseInvoiceTable : tableView.getItems()) {
//            taxPercentage = purchaseInvoiceTable.getTax_per()!=null && !purchaseInvoiceTable.getTax_per().isEmpty()? Double.parseDouble(purchaseInvoiceTable.getTax_per()):0.0;
//            Double quantity = Double.parseDouble(purchaseInvoiceTable.getQuantity());
//            if (purchaseInvoiceTable.getFree_qty().isEmpty()) {
//                freeQuantity = 0.0;
//            } else {
//                freeQuantity = Double.parseDouble(purchaseInvoiceTable.getFree_qty());
//            }
//
//            // Total Calculations of each IGST, SGST, CGST
//            totalQuantity += quantity;
//            totalFreeQuantity += freeQuantity;
//            Double igst = Double.parseDouble(purchaseInvoiceTable.getIgst().toString());
//            Double cgst = Double.parseDouble(purchaseInvoiceTable.getCgst().toString());
//            Double sgst = Double.parseDouble(purchaseInvoiceTable.getSgst().toString());
//            totalFinalIgst += igst;
//            totalFinalSgst += sgst;
//            totalFinalCgst += cgst;
//
//            cgstTotals.put(taxPercentage, cgstTotals.get(taxPercentage) + cgst); //0.0
//            sgstTotals.put(taxPercentage, sgstTotals.get(taxPercentage) + sgst);
//
//            //Total Calculation of gross amt ,taxable ,tax,discount
//            netAmount = Double.parseDouble(purchaseInvoiceTable.getNet_amt());
//            totalNetAmt += netAmount;
//            grossAmt = Double.parseDouble(purchaseInvoiceTable.getGross_amt());
//            totalGrossAmt += grossAmt;
//            taxableAmt = Double.parseDouble(purchaseInvoiceTable.getTaxable_amt().toString());
//            totaltaxableAmt += taxableAmt;
//            disAmt = Double.parseDouble(purchaseInvoiceTable.getFinal_dis_amt().toString());
//            totalDisAmt += disAmt;
//            taxAmt = Double.parseDouble(purchaseInvoiceTable.getFinTaxAmt().toString());
//            totalTaxAmt += taxAmt;
//
//        }
//
//        ObservableList<GstDTO> observableList = FXCollections.observableArrayList();
//        for (Double taxPer : new Double[]{3.0, 5.0, 12.0, 18.0, 28.0}) {
//            double totalCGST = cgstTotals.get(taxPer);
//            double totalSGST = sgstTotals.get(taxPer);
//            double totalIGST = 0.0;
//
//            if (totalCGST > 0) {
////                GstDTO gstDto :
//                System.out.println("Tax Percentage: " + taxPer + "%");
//                System.out.println("Total CGST: " + totalCGST);
//                System.out.println("Total SGST: " + totalSGST);
//                System.out.println();
//
//                observableList.add(new GstDTO(String.format("%.2f", taxPer), String.format("%.2f", totalCGST), String.format("%.2f", totalSGST), String.valueOf(totalIGST)));
//
//            }
////            tblcPurChallGST.setCellValueFactory(new PropertyValueFactory<>("taxPer"));
////            tblcPurChallCGST.setCellValueFactory(new PropertyValueFactory<>("cgst"));
////            tblcPurChallSGST.setCellValueFactory(new PropertyValueFactory<>("sgst"));
//        }
////        tblvPurChallGST.setItems(observableList);
////        Platform.runLater(() -> {
////            tblvPurChallGST.refresh();
////
////        });
//
//        if (callback != null) {
//
//            Object[] object = new Object[10];
//
//            object[0] = decimalFormat.format(totalQuantity);
//
//            object[1] = String.valueOf(totalFreeQuantity);
//
//            object[2] = String.format("%.2f", totalNetAmt);
//
//            object[3] = String.format("%.2f", totalGrossAmt);
//
//            object[4] = String.format("%.2f", totalDisAmt);
//
//            //  lblPurChallTotalTaxableAmount.setText(String.format("%.2f", (totalGrossAmt - totalDisAmt + addChargAmount)));
//            object[5] = String.format("%.2f", (totalGrossAmt - totalDisAmt + 0.0));
//
//            object[6] = String.format("%.2f", totalTaxAmt);
//
//            object[7] = String.format("%.2f", totalFinalSgst);
//
//            object[8] = String.format("%.2f", totalFinalCgst);
//
//            object[9] = observableList;
//
//
//            if (callback != null) {
//                callback.call(object);
//            }
//        }
//    }
//
//
//    public static void discountPropotionalCalculation(String disproportionalPer, String disproportionalAmt, String additionalCharges, TableView<SalesQuotationProductDTO> tableView, TableCellCallback<Object[]> callback) {
//
//        //get discount percentage and discount amount to textfield
//
//        Double dis_proportional_per = 0.0;//store the textfield discount per String to double
//        Double dis_proportional_amt = 0.0;//store the textfield discount amt String to double
//        if (!disproportionalPer.isEmpty()) {// check if row level discount per is not empty
//            //row level Discount Percentage
//            dis_proportional_per = Double.parseDouble(disproportionalPer);
//        } else {
//            dis_proportional_per = 0.0;
//        }
//        if (!disproportionalAmt.isEmpty()) {// check if row level discount amt is not empty
//            //row level Discount amount
//            dis_proportional_amt = Double.parseDouble(disproportionalAmt);
//        } else {
//            dis_proportional_amt = 0.0;
//        }
//        System.out.println("dis_proportional_per discountPropotionalCalculation :: " + dis_proportional_per);
//        Double finalNetAmt = 0.0;
//        Double r_tax_per = 0.0;//store the Tax per data
//        Double total_tax_amt = 0.0;//store total tax amount of each row level
//        Double netAmt = 0.0;//Store net Amount after calculating  dis and tax
//        Double rowTaxableAmt = 0.0; //store the row level calculated taxable amount
//        Double totalTaxableAmt = 0.0;//store the total calculated taxable amount
//        Double totalDisPropAmt = 0.0;//store the discount calculated proportional amt
//        Double rowDisPropAmt = 0.0;//stotre the row level discount calculated amount
//        Double totalTaxableAmtAdditional = 0.0;
//        Double rowtotalTaxableAmtAdditional = 0.0;
//
//
//        //calculate total taxable amount
//        for (int i = 0; i < tableView.getItems().size(); i++) {
//            System.out.println("Gross amt in Prop--->" + tableView.getItems().get(i).getGross_amt());
//            rowCalculationForPurcInvoice(i, tableView, callback);//call row calculation function
//            SalesQuotationProductDTO purchaseInvoiceTable = tableView.getItems().get(i);
//            totalTaxableAmt = totalTaxableAmt + Double.parseDouble(purchaseInvoiceTable.getTaxable_amt().toString());
//
//        }
//        System.out.println("totalTaxableAmt discountPropotionalCalculation :: " + totalTaxableAmt);
//
//        Double rowDisc = 0.0;
//        //get data of netamount, tax per,and row taxable amount from cmptrow
//        for (SalesQuotationProductDTO purchaseInvoiceTable : tableView.getItems()) {
//            System.out.println("getFinal_dis_amt--->" + purchaseInvoiceTable.getFinal_dis_amt());
//            rowDisc = Double.parseDouble(purchaseInvoiceTable.getFinal_dis_amt().toString());
//            System.out.println("netAmt--->" + purchaseInvoiceTable.getNet_amt());
//            netAmt = Double.parseDouble(purchaseInvoiceTable.getNet_amt());
//            System.out.println("netAmt--->" + purchaseInvoiceTable.getTax_per());
//            r_tax_per = Double.parseDouble(purchaseInvoiceTable.getTax_per());
//            rowTaxableAmt = Double.parseDouble(purchaseInvoiceTable.getTaxable_amt().toString());
//            System.out.println("rowTaxableAmt discountPropotionalCalculation :: " + rowTaxableAmt + "netAmt" + netAmt);
//
//            //calculate discount proportional percentage and  the taxable amount
//            if (dis_proportional_per > 0.0) {
//                totalDisPropAmt = (dis_proportional_per / 100) * (totalTaxableAmt);
//                rowDisPropAmt = (rowTaxableAmt * totalDisPropAmt) / totalTaxableAmt;
//                System.out.println("row dis pro amt: " + rowDisPropAmt);
//                rowTaxableAmt = rowTaxableAmt - rowDisPropAmt;
//                rowDisc += rowDisPropAmt;
//                totalTaxableAmtAdditional = rowTaxableAmt;
//            } else {
//                totalTaxableAmtAdditional = rowTaxableAmt;
//            }
//
//            System.out.println("rowDisc : " + rowDisc);
//
//
////
////            if (dis_proportional_amt > 0.0) {
////                totalDisPropAmt = dis_proportional_amt;
////                rowDisPropAmt = (rowTaxableAmt * totalDisPropAmt) / totalTaxableAmt;
////                rowDisc += rowDisPropAmt;
////                rowTaxableAmt = rowTaxableAmt - rowDisPropAmt;
////                totalTaxableAmtAdditional = rowTaxableAmt;
////            } else {
////                totalTaxableAmtAdditional = rowTaxableAmt;
////
////            }
//
//
//            //calculate tax per and store the data to final net AMount
//            if (additionalCharges.isEmpty()) {
//                if (r_tax_per > 0) {
//                    total_tax_amt = (r_tax_per / 100) * (rowTaxableAmt);
//                    System.out.println("total_tax_amt" + total_tax_amt);
//                    netAmt = rowTaxableAmt + total_tax_amt;
//                    System.out.println("total netAmt  :" + netAmt + "" + rowTaxableAmt);
////
//                }
//            }
//
//            //Set data to cmpTRow
//            purchaseInvoiceTable.setNet_amt(String.format("%.2f", netAmt));
//            purchaseInvoiceTable.setTotal_taxable_amt(Double.parseDouble(String.valueOf(totalTaxableAmtAdditional)));
//            System.out.println("Total Tax Amt--->" + total_tax_amt);
//            purchaseInvoiceTable.setIgst(Double.valueOf(String.valueOf(total_tax_amt)));
//            purchaseInvoiceTable.setCgst(Double.valueOf(String.valueOf(total_tax_amt / 2)));
//            purchaseInvoiceTable.setSgst(Double.valueOf(String.valueOf(total_tax_amt / 2)));
//            purchaseInvoiceTable.setFinTaxAmt(Double.valueOf(String.valueOf(total_tax_amt)));
//            purchaseInvoiceTable.setFinal_dis_amt(Double.valueOf(String.valueOf(rowDisc)));
//            purchaseInvoiceTable.setInvoice_dis_amt(Double.valueOf(String.valueOf(rowDisPropAmt)));
//
//        }
//        //Check additional charge is Empty or not if not empty call to the additional charges function
//        if (!additionalCharges.isEmpty()) {
//            //additionalChargesCalculation();
//        } /*else {
//            System.out.println("Additional Charges is empty");
//            additionalChargesCalculation();
//        }*/
//        System.out.println("from discountPropotionalCalculation");
//        calculateGst(tableView, callback);
//    }


//    public static void additionalChargesCalculation(String additionalCharges, TableView<SalesQuotationProductDTO> tableView,TableCellCallback<Object[]> callback) {
//
//
//        Double additionalChargeAmt = 0.0;//store data textfield string to double
//
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
//                PurchaseInvoiceTable purchaseInvoiceTable = tableView.getItems().get(i);
//                totalTaxableAmt = totalTaxableAmt + Double.parseDouble(purchaseInvoiceTable.getTotal_taxable_amt());
//
//            }
//            for (SalesQuotationProductDTO purchaseInvoiceTable : tableView.getItems()) {
//
//                netAmt = Double.parseDouble(purchaseInvoiceTable.getNet_amount());
//                r_tax_per = Double.parseDouble(purchaseInvoiceTable.getTax());
//                rowTaxableAmt = Double.parseDouble(purchaseInvoiceTable.getTotal_taxable_amt());
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
//                purchaseInvoiceTable.setNet_amount(String.format("%.3f", netAmt));
//                purchaseInvoiceTable.setCgst(String.valueOf(total_tax_amt / 2));
//                purchaseInvoiceTable.setSgst(String.valueOf(total_tax_amt / 2));
//                purchaseInvoiceTable.setFinal_tax_amt(String.valueOf(total_tax_amt));
//
//
//            }
//
//        }
//        //call calculate gst function
//        calculateGst(tableView,callback);
//
//    }


}






