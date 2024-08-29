package com.opethic.genivis.controller.account_entry;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.opethic.genivis.GenivisApplication;
import com.opethic.genivis.controller.commons.OverlaysEffect;
import com.opethic.genivis.controller.dialogs.SingleInputDialogs;
import com.opethic.genivis.controller.master.ledger.common.LedgerMessageConsts;
import com.opethic.genivis.controller.tranx_sales.SalesOrderController;
import com.opethic.genivis.dto.SalesOrderSupplierDetailsDTO;
import com.opethic.genivis.dto.TranxLedgerWindowDTO;
import com.opethic.genivis.dto.pur_invoice.PurInvoiceCommunicator;
import com.opethic.genivis.dto.reqres.creditNote.BillByBillPerticulars;
import com.opethic.genivis.dto.reqres.creditNote.BillByBillRowReqDTO;
import com.opethic.genivis.dto.reqres.creditNote.CreditNoteByIdMainDTO;
import com.opethic.genivis.dto.reqres.creditNote.CreditNoteByIdRowDTO;
import com.opethic.genivis.dto.reqres.journal.JournalByIdMainDTO;
import com.opethic.genivis.dto.reqres.journal.JournalByIdRowDTO;
import com.opethic.genivis.dto.reqres.journal.JournalRowDTO;
import com.opethic.genivis.dto.reqres.journal.TranxJournalInvoiceDTO;
import com.opethic.genivis.dto.reqres.payment.InvoiceListResDTO;
import com.opethic.genivis.dto.reqres.payment.LedgerPendingBillResDTO;
import com.opethic.genivis.dto.reqres.payment.PaymentRowDTO;
import com.opethic.genivis.dto.reqres.payment.VoucherLastRecordResponse;
import com.opethic.genivis.dto.reqres.product.Communicator;
import com.opethic.genivis.dto.reqres.product.TableCellCallback;
import com.opethic.genivis.dto.reqres.pur_tranx.*;
import com.opethic.genivis.dto.reqres.sales_tranx.SalesOrderTable;
import com.opethic.genivis.network.APIClient;
import com.opethic.genivis.network.EndPoints;
import com.opethic.genivis.network.RequestType;
import com.opethic.genivis.utils.*;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.WorkerStateEvent;
import javafx.css.PseudoClass;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URL;
import java.net.http.HttpResponse;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.function.BiConsumer;

import static com.opethic.genivis.controller.account_entry.ContraController.contralogger;
import static com.opethic.genivis.controller.account_entry.PaymentCreateController.paymentLogger;
import static com.opethic.genivis.utils.FxmFileConstants.*;
import static com.opethic.genivis.utils.Globals.contraListDTO;
import static com.opethic.genivis.utils.Globals.mapToStringforFormData;

public class ContraController implements Initializable {

    @FXML
    private BorderPane spContraRootPane;
    @FXML
    private HBox hboxContraRoot, contraDivider, contraDividerFirst, contraDividerSecond, contraDividerThird;
    @FXML
    public TextField tfContraSrNo, tfContraVoucherNo, tfContraTranxDate, tfContraNarration;
    @FXML
    TableView<PaymentRowDTO> tvJournal;
    @FXML
    TableColumn<PaymentRowDTO, String> tcContraType, tcContraParticulars, tcContraDebit, tcContraCredit;

    @FXML
    private TableView tvContraSupplierDetails;

    @FXML
    private Button btnJournalModify, btnJournalSubmit, btnJournalCancel;
    @FXML
    private TableColumn tcContraSource, tcContraSupplierInvNo, tcContraSupplierInvDate, tcContraSupplierBillAmount, tcContraSupplierPaidAmount, tcContraSupplierRemainingAmount;
    @FXML
    private Label lblContraGstNo, lblContraArea, lblContraBank, lblContraContactPerson, lblContraTransport, lblContraFSSAI,
            lblContraCreditDays, lblContraLicenseNo, lblContraRoute, labelJournalTotalDebit, labelJournalTotalCredit;

    private static final Logger logger = LogManager.getLogger(ContraController.class);
    public static final org.slf4j.Logger contralogger = LoggerFactory.getLogger(ContraController.class);

    private JsonObject jsonObject = null;

    private String message;

    private Integer creditNoteEditId = -1;

    private Integer rediCurrIndex = -1;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        ResponsiveWiseCssPicker();

        Platform.runLater(() -> {
            tfContraTranxDate.requestFocus();
        });

        //? this include all the Shortcut Keys
        initShorcutKeys();

        //? this inculde all the design related properties and important Fields
        mandatoryFields();

        //? this include all the table view operations - Edit , Conversions , search , etc..
        tableViewOperations();


        //? All Main Functions
        tableInitialization();

        //  Create API Call
        btnJournalSubmit.setOnAction((event) -> {
            if (CommonValidationsUtils.validateForm(tfContraTranxDate)) {
                createContra();
            }
        });

        //Open Create and Edit Page On id if ID is Null Open Create Page and If ID is Not Null Open Edit Page
        if (contraListDTO != null) {
            System.out.println("id->" + contraListDTO.getId());
            getEditDataById();
        } else {
            //On Page Load get the latest Serial No and Voucher No and Set in the Field
            getJournalSrNo();
            contraListDTO = null;
            //todo:Delayed initialization to ensure tgFranchiseIsFunding's width is properly calculated
        }
    }


    public void setPurChallDataToProduct() {

        String tranxDate = Communicator.text_to_date.fromString(tfContraTranxDate.getText()).toString();
        String voucherNo=tfContraVoucherNo.getText();
        String receiptNo=tfContraSrNo.getText();
        String narration=tfContraNarration.getText();

        PurTranxToProductRedirectionDTO mData = new PurTranxToProductRedirectionDTO();
        mData.setRedirect(FxmFileConstants.CONTRA_CREATE_SLUG);
        mData.setTranxDate(tranxDate);
        mData.setChallanNo(receiptNo);
        mData.setPurSerNum(voucherNo);
        //  mData.setNarration(narration);
        mData.setTranxType("account");
        mData.setRediPrdCurrIndex(rediCurrIndex);
        List<PaymentRowDTO> currentItems = new ArrayList<>(tvJournal.getItems());
        List<PaymentRowDTO> list = new ArrayList<>(currentItems);

        mData.setRowReceipt(list);

        GlobalController.getInstance().addTabStaticWithIsNewTab("ledger-create", false, mData);




    }
    public JsonObject ledgersById(Long id) {
        System.out.println("ledgerId"+id);

        Map<String, String> map = new HashMap<>();
        map.put("id", String.valueOf(id));

        String formData = mapToStringforFormData(map);
        HttpResponse<String> response = APIClient.postFormDataRequest(formData, "get_ledgers_by_id");

        String json = response.body();
        JsonObject jsonObject = new Gson().fromJson(json, JsonObject.class);
        JsonObject responseObj = jsonObject.getAsJsonObject("response");
        String ledgerName = responseObj.get("ledger_name").getAsString();
        // ledgerStateCode = responseObj.get("ledgerStateCode").getAsString();
        System.out.println("responseObj"+responseObj);

        JsonArray gstdetailsArray = responseObj.getAsJsonArray("gstdetails");
        if (gstdetailsArray != null) {
            for (JsonElement element : gstdetailsArray) {
                JsonObject gstdetail = element.getAsJsonObject();
                String gstin = gstdetail.get("gstin").getAsString();
                String gst_id = gstdetail.get("id").getAsString();
                //  supplierGSTINList.add(new CommonDTO(gstin, gst_id));
            }
        }
//        if (ledgerStateCode.equalsIgnoreCase(GlobalTranx.getCompanyStateCode().toString())) {
//            taxFlag = true;
//        }

        return responseObj;
    }


    public void setRedirectData(Object inObj) {
        PurTranxToProductRedirectionDTO franchiseDTO = (PurTranxToProductRedirectionDTO) inObj;

        Integer redirectIndex = franchiseDTO.getRediPrdCurrIndex();

        int cnt = 0, index = 0;
        tfContraTranxDate.setText(franchiseDTO.getTranxDate());
        tfContraSrNo.setText(franchiseDTO.getPurSerNum());
        tfContraVoucherNo.setText(franchiseDTO.getChallanNo());
        //tfReceiptNarration.setText(franchiseDTO.getNarration());
        tvJournal.getItems().clear();
        PurInvoiceCommunicator.levelAForPurInvoiceObservableList.clear();

        Long ledgerId = Long.valueOf(franchiseDTO.getLedegrId());
        int rindex = franchiseDTO.getRediPrdCurrIndex();
        JsonObject obj = ledgersById(ledgerId);
        System.out.println("obj---" + obj.get("ledger_name"));

        for (PaymentRowDTO purchaseInvoiceTable : franchiseDTO.getRowReceipt()) {
            if (index == rindex) {
                purchaseInvoiceTable.setType("Dr");
                purchaseInvoiceTable.setLedgerId(Long.valueOf(ledgerId + ""));
                purchaseInvoiceTable.setParticulars(obj.get("ledger_name").getAsString());
                purchaseInvoiceTable.setCredit("");
                purchaseInvoiceTable.setDebit("");
            }
            tvJournal.getItems().addAll(purchaseInvoiceTable);
            cnt++;
            index++;
        }

        Platform.runLater(() -> {
            TableColumn<PaymentRowDTO, ?> colName = tvJournal.getColumns().get(1);
            tvJournal.edit(redirectIndex, colName);
        });


    }

    TableCellCallback<Object[]> addPrdCalbak = item -> {
        if (item != null) {
            System.out.println("item [0] "+ item [0] );
            System.out.println("item [0] "+ item [1] );
            if((Boolean)item [0] == true){
                rediCurrIndex = (Integer) item [1];
                String isProduct = (String) item[2];

                setPurChallDataToProduct();
            }
            //  System.out.println("isProductRed >> "+ isProductRed);
        }
    };

    private void ResponsiveWiseCssPicker() {
        double height = Screen.getPrimary().getBounds().getHeight();
        double width = Screen.getPrimary().getBounds().getWidth();
        System.out.println("width" + width);
        if (width >= 992 && width <= 1024) {
            contraDividerFirst.prefWidthProperty().bind(contraDivider.widthProperty().multiply(0.84));
            contraDividerSecond.prefWidthProperty().bind(contraDivider.widthProperty().multiply(0.08));
            contraDividerThird.prefWidthProperty().bind(contraDivider.widthProperty().multiply(0.08));
            tvContraSupplierDetails.getStylesheets().add(GenivisApplication.class.getResource("ui/css/purchase_challan_css/supplier_table_css/supplier_table_992_1024.css").toExternalForm());
            spContraRootPane.getStylesheets().add(GenivisApplication.class.getResource("ui/css/TranxCommonCssStyles/tranxCommonCssStyle1.css").toExternalForm());
        } else if (width >= 1025 && width <= 1280) {
            contraDividerFirst.prefWidthProperty().bind(contraDivider.widthProperty().multiply(0.84));
            contraDividerSecond.prefWidthProperty().bind(contraDivider.widthProperty().multiply(0.08));
            contraDividerThird.prefWidthProperty().bind(contraDivider.widthProperty().multiply(0.08));
            tvContraSupplierDetails.getStylesheets().add(GenivisApplication.class.getResource("ui/css/purchase_challan_css/supplier_table_css/supplier_table_1025_1280.css").toExternalForm());
            spContraRootPane.getStylesheets().add(GenivisApplication.class.getResource("ui/css/TranxCommonCssStyles/tranxCommonCssStyle2.css").toExternalForm());
        } else if (width >= 1281 && width <= 1368) {
            contraDividerFirst.prefWidthProperty().bind(contraDivider.widthProperty().multiply(0.84));
            contraDividerSecond.prefWidthProperty().bind(contraDivider.widthProperty().multiply(0.08));
            contraDividerThird.prefWidthProperty().bind(contraDivider.widthProperty().multiply(0.08));
            tvContraSupplierDetails.getStylesheets().add(GenivisApplication.class.getResource("ui/css/purchase_challan_css/supplier_table_css/supplier_table_1281_1368.css").toExternalForm());
            spContraRootPane.getStylesheets().add(GenivisApplication.class.getResource("ui/css/TranxCommonCssStyles/tranxCommonCssStyle3.css").toExternalForm());
        } else if (width >= 1369 && width <= 1400) {
            contraDividerFirst.prefWidthProperty().bind(contraDivider.widthProperty().multiply(0.84));
            contraDividerSecond.prefWidthProperty().bind(contraDivider.widthProperty().multiply(0.08));
            contraDividerThird.prefWidthProperty().bind(contraDivider.widthProperty().multiply(0.08));
            tvContraSupplierDetails.getStylesheets().add(GenivisApplication.class.getResource("ui/css/purchase_challan_css/supplier_table_css/supplier_table_1369_1400.css").toExternalForm());
            spContraRootPane.getStylesheets().add(GenivisApplication.class.getResource("ui/css/TranxCommonCssStyles/tranxCommonCssStyle4.css").toExternalForm());
        } else if (width >= 1401 && width <= 1440) {
            contraDividerFirst.prefWidthProperty().bind(contraDivider.widthProperty().multiply(0.84));
            contraDividerSecond.prefWidthProperty().bind(contraDivider.widthProperty().multiply(0.08));
            contraDividerThird.prefWidthProperty().bind(contraDivider.widthProperty().multiply(0.08));
            spContraRootPane.getStylesheets().add(GenivisApplication.class.getResource("ui/css/TranxCommonCssStyles/tranxCommonCssStyle5.css").toExternalForm());
            tvContraSupplierDetails.getStylesheets().add(GenivisApplication.class.getResource("ui/css/purchase_challan_css/supplier_table_css/supplier_table_1401_1440.css").toExternalForm());
        } else if (width >= 1441 && width <= 1680) {
            contraDividerFirst.prefWidthProperty().bind(contraDivider.widthProperty().multiply(0.84));
            contraDividerSecond.prefWidthProperty().bind(contraDivider.widthProperty().multiply(0.08));
            contraDividerThird.prefWidthProperty().bind(contraDivider.widthProperty().multiply(0.08));
            tvContraSupplierDetails.getStylesheets().add(GenivisApplication.class.getResource("ui/css/purchase_challan_css/supplier_table_css/supplier_table_1441_1680.css").toExternalForm());
            spContraRootPane.getStylesheets().add(GenivisApplication.class.getResource("ui/css/TranxCommonCssStyles/tranxCommonCssStyle6.css").toExternalForm());
        } else if (width >= 1681 && width <= 1920) {
            contraDividerFirst.prefWidthProperty().bind(contraDivider.widthProperty().multiply(0.84));
            contraDividerSecond.prefWidthProperty().bind(contraDivider.widthProperty().multiply(0.08));
            contraDividerThird.prefWidthProperty().bind(contraDivider.widthProperty().multiply(0.08));
            tvContraSupplierDetails.getStylesheets().add(GenivisApplication.class.getResource("ui/css/invoice_product_history_table.css").toExternalForm());
            spContraRootPane.getStylesheets().add(GenivisApplication.class.getResource("ui/css/TranxCommonCssStyles/tranxCommonCssStyle7.css").toExternalForm());
        }
    }


    private void initShorcutKeys() {
        spContraRootPane.addEventFilter(KeyEvent.KEY_PRESSED, (KeyEvent event) -> {
            if (event.getCode() == KeyCode.ENTER) {
                if (event.getTarget() instanceof Button targetButton && targetButton.getText().equalsIgnoreCase("submit")) {
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
            } else if (event.getCode() == KeyCode.S && event.isControlDown()) {
                btnJournalSubmit.fire();
            } else if (event.getCode() == KeyCode.X && event.isControlDown()) {
                btnJournalCancel.fire();
            } else if (event.getCode() == KeyCode.E && event.isControlDown()) {
                btnJournalModify.fire();
            }
        });
    }

    private void mandatoryFields() {
        CommonValidationsUtils.changeStarColour2(hboxContraRoot);

        //Date Field with Validation
        DateValidator.applyDateFormat(tfContraTranxDate);
        LocalDate currentDate = LocalDate.now();
        tfContraTranxDate.setText(currentDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        sceneInitilization();

        //ReadOnly Fields
        tfContraSrNo.setDisable(true);
        tfContraSrNo.setStyle("-fx-opacity: 1;"); // Make textfield fully visible
        tfContraSrNo.pseudoClassStateChanged(PseudoClass.getPseudoClass("disabled"), false);
        tfContraVoucherNo.setDisable(true);
        tfContraVoucherNo.setStyle("-fx-opacity: 1;"); // Make textfield fully visible
        tfContraVoucherNo.pseudoClassStateChanged(PseudoClass.getPseudoClass("disabled"), false);

    }

    private void tableViewOperations() {
        //Hide the Scroll of the ScrollPane parent
//        spContraRootPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
//        spContraRootPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

        //Responsive Table Columns Design wihtout Height Width
//        tvJournal.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);
        tvContraSupplierDetails.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        //Design of Bottom Table by Percentage
        ContraCmptRowTableDesign();
    }

    //? On Clicking on the Cancel Button From Create or Edit page redirect Back to List page
    public void backToList() {
        Globals.contraListDTO = null;
        AlertUtility.AlertConfirmation(AlertUtility.alertTypeConfirmation, LedgerMessageConsts.msgConfirmationOnCancel + tfContraVoucherNo.getText(), input -> {
            if (input == 1) {
                clearLedAndProdInfo();
            }
        });
    }

    public void clearLedAndProdInfo() {
        tfContraTranxDate.setText("");
        tfContraNarration.setText("");

        //? clear Ledger Info
        lblContraGstNo.setText("");
        lblContraArea.setText("");
        lblContraBank.setText("");
        lblContraContactPerson.setText("");
        lblContraFSSAI.setText("");
        lblContraLicenseNo.setText("");
        lblContraRoute.setText("");
        lblContraTransport.setText("");
        lblContraCreditDays.setText("");


        tvJournal.getItems().clear();// Add a new blank row if needed
        tvJournal.getItems().addAll(new PaymentRowDTO("", "Dr", "", "", ""));// 1st row;
        tfContraTranxDate.requestFocus();
        tvContraSupplierDetails.getItems().clear();
        PurInvoiceCommunicator.resetFields();//new 2.0 to Clear the CMPTROW ComboBox

    }

    //? On Clicking on the Modify Button From Create or Edit page redirect Back to List page
    public void backToListModify() {
        Globals.contraListDTO = null;
        AlertUtility.AlertConfirmation(AlertUtility.alertTypeConfirmation, LedgerMessageConsts.msgGoList, input -> {
            if (input == 1) {
                GlobalController.getInstance().addTabStatic(CONTRA_LIST_SLUG, false);
            }
        });
    }

    //Date Validation Function
    public void sceneInitilization() {
        spContraRootPane.sceneProperty().addListener((observable, oldScene, newScene) -> {
            if (newScene != null && newScene.getWindow() instanceof Stage) {
                Communicator.stage = (Stage) newScene.getWindow();
            }
        });
    }

    //Responsive Design by Percentage
    public void ContraCmptRowTableDesign() {
        tcContraType.prefWidthProperty().bind(tvJournal.widthProperty().multiply(0.04));
        tcContraParticulars.prefWidthProperty().bind(tvJournal.widthProperty().multiply(0.8));
        tcContraDebit.prefWidthProperty().bind(tvJournal.widthProperty().multiply(0.08));
        tcContraCredit.prefWidthProperty().bind(tvJournal.widthProperty().multiply(0.08));
    }

    //Function to get and set the Serial No and Voucher No
    private void getJournalSrNo() {
        APIClient apiClient = null;
        try {
            logger.debug("Get Journal Serial No Started...");
//        HttpResponse<String> response = APIClient.getRequest(EndPoints.GET_LAST_JOURNAL_RECORD_ENDPOINT);
            apiClient = new APIClient(EndPoints.GET_LAST_CONTRA_RECORD_ENDPOINT, "", RequestType.GET);

            apiClient.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
                @Override
                public void handle(WorkerStateEvent workerStateEvent) {
                    jsonObject = new Gson().fromJson(workerStateEvent.getSource().getValue().toString(), JsonObject.class);
//                    String responseBody = response.body();
//                    JsonObject jsonObject = new Gson().fromJson(responseBody, JsonObject.class);
                    System.out.println("Journal on page load response-->" + jsonObject);
                    if (jsonObject.get("responseStatus").getAsInt() == 200) {
                        String count = jsonObject.get("count").getAsString();
                        String journalNo = jsonObject.get("contraNo").getAsString();
                        tfContraSrNo.setText(count);
                        tfContraVoucherNo.setText(journalNo);
                    }
                }
            });
            apiClient.setOnCancelled(new EventHandler<WorkerStateEvent>() {
                @Override
                public void handle(WorkerStateEvent workerStateEvent) {
                    logger.error("Network API cancelled in getJournalSrNo()" + workerStateEvent.getSource().getValue().toString());

                }
            });

            apiClient.setOnFailed(new EventHandler<WorkerStateEvent>() {
                @Override
                public void handle(WorkerStateEvent workerStateEvent) {
                    logger.error("Network API failed in getJournalSrNo()" + workerStateEvent.getSource().getValue().toString());
                }
            });
            apiClient.start();
            logger.debug("Get Journal Serial No Data End...");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            apiClient = null;
        }
    }

    private void tableInitialization() {
        TableCellCallback<String[]> callback = item -> {


            if (item[0].equals("true")) {
                lableInitialization();
            } else {
                lblContraGstNo.setText(item[0]);
                lblContraArea.setText(item[1]);
                lblContraBank.setText(item[2]);
                lblContraContactPerson.setText(item[3]);
                lblContraCreditDays.setText(item[5]);
                lblContraFSSAI.setText(item[6]);
                lblContraLicenseNo.setText(item[7]);
                lblContraRoute.setText(item[8]);
            }
        };
        tvJournal.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tvJournal.setFocusTraversable(false);
        tvJournal.getItems().addAll(new PaymentRowDTO("", "DR", "", "", ""));
        tcContraType.setCellValueFactory(cellData -> cellData.getValue().typeProperty());
        tcContraType.setCellFactory(column -> new ComboBoxTableCellForContra("type"));

        tcContraParticulars.setCellValueFactory(cellData -> cellData.getValue().particularsProperty());
        tcContraParticulars.setCellFactory(column -> new TextFieldTableCellForContra("particular", spContraRootPane, callback,addPrdCalbak));

        tcContraDebit.setCellValueFactory(cellData -> cellData.getValue().debitProperty());
        tcContraDebit.setCellFactory(column -> new TextFieldTableCellForContra("debit", spContraRootPane, callback));

        tcContraCredit.setCellValueFactory(cellData -> cellData.getValue().creditProperty());
        tcContraCredit.setCellFactory(column -> new TextFieldTableCellForContra("credit", spContraRootPane, callback));

    }

    // Total Amount Set Method
    private void lableInitialization() {
        List<PaymentRowDTO> tableList = tvJournal.getItems();
        double creditAmt = 0.0;
        double debitAmt = 0.0;
        double totalCredit = 0.0;
        double totalDebit = 0.0;
        for (PaymentRowDTO paymentRowDTO : tableList) {
            creditAmt = paymentRowDTO.getCredit() != "" ? Double.parseDouble(paymentRowDTO.getCredit()) : 0.00;
            totalCredit += creditAmt;
            debitAmt = paymentRowDTO.getDebit() != "" ? Double.parseDouble(paymentRowDTO.getDebit()) : 0.00;
            totalDebit += debitAmt;
        }
        //? 2 Decimal
        double creditTotal = GlobalTranx.TranxRoundDigit(totalCredit, GlobalTranx.configDecimalPlace);
        labelJournalTotalCredit.setText(String.valueOf(creditTotal));
        double debitTotal = GlobalTranx.TranxRoundDigit(totalDebit, GlobalTranx.configDecimalPlace);
        labelJournalTotalDebit.setText(String.valueOf(debitTotal));

        if (totalCredit != totalDebit) {
            tvJournal.getItems().add(new PaymentRowDTO("", "", "", "", ""));
        }

    }

    //Create Api Integration
    private void createContra() {
        AlertUtility.CustomCallback callback = number -> {
            if (number == 1) {
                APIClient apiClient = null;
                try {
                    contralogger.debug("Get createContra Started...");
                    String id = contraListDTO != null ? contraListDTO.getId() : "";
                    Map<String, String> map = new HashMap<>();
                    map.put("journal_id", id);
                    map.put("voucher_journal_sr_no", tfContraSrNo.getText());
                    map.put("voucher_journal_no", tfContraVoucherNo.getText());
                    map.put("total_amt", labelJournalTotalCredit.getText());
                    map.put("narration", tfContraNarration.getText());
                    if (tfContraTranxDate != null && tfContraTranxDate.getText() != null && !tfContraTranxDate.getText().isEmpty()) {
                        map.put("transaction_dt", Communicator.text_to_date.fromString(tfContraTranxDate.getText()).toString());
                    }
                    ArrayList<BillByBillRowReqDTO> rowData = new ArrayList<>();
//            ObservableList<PaymentRowDTO> tableViewItems = tblvCreditNoteTable.getItems();
                    ObservableList<PaymentRowDTO> tableViewItems = tvJournal.getItems();
                    for (PaymentRowDTO paymentRowDTO : tableViewItems) {
                        BillByBillRowReqDTO billByBillRowReqDTO = new BillByBillRowReqDTO();
                        BillByBillPerticulars billByBillPerticulars = new BillByBillPerticulars();

                        /******************** Perticular Class Fields Start **************/
                        if (Globals.contraListDTO != null) {
                            billByBillPerticulars.setDetailsId(paymentRowDTO.getDetailsId());
                        }
                        if (!paymentRowDTO.getParticulars().isEmpty()) {
                            billByBillPerticulars.setId(paymentRowDTO.getLedgerId());
                        } else {
                            billByBillPerticulars.setId(0L);
                        }
                        if (!paymentRowDTO.getParticulars().isEmpty()) {
                            billByBillPerticulars.setType(paymentRowDTO.getLedgerType());
                        } else {
                            billByBillPerticulars.setType("");
                        }
                        if (!paymentRowDTO.getParticulars().isEmpty()) {
                            billByBillPerticulars.setLedgerName(paymentRowDTO.getLedgerName());
                        } else {
                            billByBillPerticulars.setLedgerName("");
                        }
                        if (!paymentRowDTO.getParticulars().isEmpty()) {
                            billByBillPerticulars.setBalancingMethod(paymentRowDTO.getBalancingMethod());
                        } else {
                            billByBillPerticulars.setBalancingMethod("");
                        }

                        /******************** Perticular Class Fields End **************/

                        /******************** Main Class Fields Start **************/
                        if (!paymentRowDTO.getType().isEmpty()) {
                            billByBillRowReqDTO.setType(paymentRowDTO.getType());
                        } else {
                            billByBillRowReqDTO.setType("");
                        }
                        if (paymentRowDTO.getType().equalsIgnoreCase("Cr")) {
                            billByBillRowReqDTO.setPaidAmt(Double.parseDouble(paymentRowDTO.getCredit()));
                        } else {
                            billByBillRowReqDTO.setPaidAmt(Double.parseDouble(paymentRowDTO.getDebit()));
                        }
                        if (!paymentRowDTO.getParticulars().isEmpty()) {
                            String data = new Gson().toJson(billByBillPerticulars);
                            System.out.println("pay=>" + data);
                            billByBillRowReqDTO.setPerticulars(billByBillPerticulars);
                        } else {
                            System.out.println("In Else Perticulars");
                            billByBillRowReqDTO.setType("");
                        }
                        rowData.add(billByBillRowReqDTO);
                        /******************** Main Class Fields End **************/
                    }

                    String mRowData = new Gson().toJson(rowData, new TypeToken<List<TranxPurRowDTO>>() {
                    }.getType());
                    map.put("rows", mRowData);
                    String finalReq = Globals.mapToStringforFormData(map);
                    System.out.println("final Request >> : " + finalReq);
                    if (Globals.contraListDTO == null) {
//                    response = APIClient.postFormDataRequest(finalReq,EndPoints.SALES_ORDER_CREATE_ENDPOINT);
                        apiClient = new APIClient(EndPoints.CONTRA_CREATE_ENDPOINT, finalReq, RequestType.FORM_DATA);
                        //? HIGHLIGHT
                        ContraListController.isNewContraCreated = true; //? Set the flag for new creation

                    } else {
//                    response = APIClient.postFormDataRequest(finalReq,EndPoints.SALES_ORDER_EDIT_ENDPOINT);
                        apiClient = new APIClient(EndPoints.CONTRA_UPDATE_ENDPOINT, finalReq, RequestType.FORM_DATA);
                        //? HIGHLIGHT
                        ContraListController.editedContraId = id; //? Set the ID for editing
                    }

                    apiClient.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
                        @Override
                        public void handle(WorkerStateEvent workerStateEvent) {
                            jsonObject = new Gson().fromJson(workerStateEvent.getSource().getValue().toString(), JsonObject.class);
                            System.out.println("JsonObject >> :" + jsonObject);
                            message = jsonObject.get("message").getAsString();
                            int status = jsonObject.get("responseStatus").getAsInt();
                            if (jsonObject.get("responseStatus").getAsInt() == 200) {
                                //Successfully Alert Popup After Edit and Redirect to List Page
//                                AlertUtility.CustomCallback callback = (number) -> {
//                                    if (number == 1) {
//                                        GlobalController.getInstance().addTabStatic(CONTRA_LIST_SLUG, false);
//                                    }
//                                };
//                                Stage stage2 = (Stage) spContraRootPane.getScene().getWindow();
//                                if(status==200){
//                                    AlertUtility.AlertSuccess(stage2,"Success",message,callback);
//                                }
//                                else{
//                                    AlertUtility.AlertError(stage2,AlertUtility.alertTypeError, message, callback);
//                                }

                                if (status == 200) {
                                    AlertUtility.AlertSuccessTimeout(AlertUtility.alertTypeSuccess, message, input -> {
                                        GlobalController.getInstance().addTabStatic(CONTRA_LIST_SLUG, false);
                                    });
                                } else {
                                    AlertUtility.AlertErrorTimeout(AlertUtility.alertTypeError, message, in -> {
                                        btnJournalSubmit.requestFocus();
                                    });
                                }
                            }

                        }
                    });
                    apiClient.setOnCancelled(new EventHandler<WorkerStateEvent>() {
                        @Override
                        public void handle(WorkerStateEvent workerStateEvent) {
                            contralogger.error("Network API cancelled in createContra()" + workerStateEvent.getSource().getValue().toString());

                        }
                    });
                    apiClient.setOnFailed(new EventHandler<WorkerStateEvent>() {
                        @Override
                        public void handle(WorkerStateEvent workerStateEvent) {
                            contralogger.error("Network API failed in createContra()" + workerStateEvent.getSource().getValue().toString());
                        }
                    });
                    apiClient.start();
                    contralogger.debug("Get createContra End...");
                } catch (Exception e) {
                    StringWriter sw = new StringWriter();
                    e.printStackTrace(new PrintWriter(sw));
                    String exceptionAsString = sw.toString();
                    contralogger.error("Exception in getVoucherNumber():" + exceptionAsString);
                }
            } else {
                contralogger.error("Exception in getVoucherNumber():");
            }
        };
        if (Globals.contraListDTO == null) {
            AlertUtility.AlertConfirmation(AlertUtility.alertTypeConfirmation, LedgerMessageConsts.msgConfirmationOnSubmit + tfContraVoucherNo.getText(), callback);
        } else {
            AlertUtility.AlertConfirmation(AlertUtility.alertTypeConfirmation, LedgerMessageConsts.msgConfirmationOnUpdate + tfContraVoucherNo.getText(), callback);
        }
    }

    /**************** GetById API Integration Start ******************/

    private void getEditDataById() {
        APIClient apiClient = null;
        contralogger.debug("Get getEditDataById Started...");
        try {
            Map<String, String> body = new HashMap<>();
            body.put("journal_id", Globals.contraListDTO.getId());
            String formData = Globals.mapToStringforFormData(body);
            apiClient = new APIClient(EndPoints.CONTRA_GET_DATA_BY_ID_ENDPOINT, formData, RequestType.FORM_DATA);
            apiClient.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
                @Override
                public void handle(WorkerStateEvent workerStateEvent) {
                    String response = workerStateEvent.getSource().getValue().toString();
                    JournalByIdMainDTO journalByIdMainDTO = new Gson().fromJson(response, JournalByIdMainDTO.class);
                    if (journalByIdMainDTO.getResponseStatus() == 200) {
                        contralogger.debug("Get getEditDataById Success...");
                        System.out.println("blue eyes" + journalByIdMainDTO);
                        setJournalFormData(journalByIdMainDTO);
                    }
                }
            });

            apiClient.setOnFailed(new EventHandler<WorkerStateEvent>() {
                @Override
                public void handle(WorkerStateEvent workerStateEvent) {
                    contralogger.error("Network API cancelled in getEditDataById()" + workerStateEvent.getSource().getValue().toString());
                }
            });
            apiClient.setOnCancelled(new EventHandler<WorkerStateEvent>() {
                @Override
                public void handle(WorkerStateEvent workerStateEvent) {
                    contralogger.error("Network API cancelled in getEditDataById()" + workerStateEvent.getSource().getValue().toString());
                }
            });
            apiClient.start();
            contralogger.debug("Get getEditDataById End...");

        } catch (Exception e) {
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            String exceptionAsString = sw.toString();
            contralogger.error("Exception in getEditDataById():" + exceptionAsString);
        } finally {
            apiClient = null;
        }
    }

    private void setJournalFormData(JournalByIdMainDTO journalByIdMainDTO) {
        btnJournalSubmit.setText("Update");
        String tranxDate = journalByIdMainDTO.getTranxDate();
        System.out.println("mydate " + tranxDate);
        if (tranxDate != null) {
            LocalDateTime dateTime = LocalDateTime.parse(tranxDate, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.S"));
            LocalDate date = dateTime.toLocalDate();
            tfContraTranxDate.setText(date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        }

        double journalSrNo = Double.parseDouble(journalByIdMainDTO.getJournalSrNo());
        int journalSrNoInt = (int) journalSrNo; // Convert double to int
        tfContraSrNo.setText(String.valueOf(journalSrNoInt)); // Set the integer value as text

        tfContraVoucherNo.setText(journalByIdMainDTO.getJournalNo());
        tfContraNarration.setText(journalByIdMainDTO.getNarrations());
        labelJournalTotalDebit.setText(journalByIdMainDTO.getTotalAmt().toString());
        labelJournalTotalCredit.setText(journalByIdMainDTO.getTotalAmt().toString());
        int index = 0;
        tvJournal.getItems().clear();
        for (JournalByIdRowDTO mRow : journalByIdMainDTO.getPerticulars()) {
            PaymentRowDTO row = new PaymentRowDTO();

            row.setDetailsId(mRow.getDetailsId());
            row.setType(mRow.getType());
            row.setParticulars(mRow.getLedgerName());
            row.setCredit(mRow.getCr() == 0.0 ? "" : mRow.getCr().toString());
            row.setDebit(mRow.getDr() == 0.0 ? "" : mRow.getDr().toString());
            row.setLedgerId(mRow.getLedgerId());
            row.setLedgerName(mRow.getLedgerName());
            row.setLedgerType(mRow.getLedgerType());
            row.setBalancingMethod(mRow.getBalancingMethod());


            tvJournal.getItems().add(row);
            index++;


        }


    }

    /**************** GetById API Integration End ******************/
}

class TextFieldTableCellForContra extends TableCell<PaymentRowDTO, String> {
    private TextField textField;
    private String columnName;
    ObservableList<TranxPaymentInvoiceDTO> mLedgerInvoiceList = FXCollections.observableArrayList();
    ObservableList<TranxPaymentInvoiceDTO> mLedgerDebitNoteList = FXCollections.observableArrayList();

    private final TableCellCallback<String[]> callback;

    TableCellCallback<Object[]> addPrdCalbak;


    public TextFieldTableCellForContra(String columnName, Parent rootPane, TableCellCallback<String[]> callback) {
        this.columnName = columnName;
        this.callback = callback;
        this.textField = new TextField();


        this.textField.setOnAction(event -> {

            commitEdit(textField.getText());


        });

        // Commit when focus is lost
        this.textField.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal) {
                commitEdit(textField.getText());
            }
        });

        this.textField.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal) {
                if (getTableRow().getItem().getType().equalsIgnoreCase("cr")) {
                    if (columnName.equals("debit")) {
                        textField.setEditable(false);
                        textField.setFocusTraversable(false);
                    }
                }
                if (getTableRow().getItem().getType().equalsIgnoreCase("dr")) {
                    if (columnName.equals("credit")) {
                        textField.setEditable(false);
                        textField.setFocusTraversable(false);
                    }
                }
            }
        });

        // Commit when Tab key is pressed
        this.textField.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.TAB) {
                commitEdit(textField.getText());
            }
        });

        textField.setStyle("-fx-background-color: #f6f6f9; -fx-border-radius: 0px; -fx-border-width: 1; -fx-border-color: #f6f6f9;");

        textField.setPrefHeight(38);
        textField.setMaxHeight(38);
        textField.setMinHeight(38);

        textField.focusedProperty().addListener((obs, wasFocused, isNowFocused) -> {
            if (isNowFocused) {
                textField.setStyle("-fx-background-color: #fff9c4; -fx-border-radius: 0px; -fx-border-width: 2; -fx-border-color: #00a0f5;");
            } else {
                textField.setStyle("-fx-background-color: #f6f6f9; -fx-border-radius: 0px; -fx-border-width: 2; -fx-border-color: #f6f6f9;");
            }
        });
        textField.setOnKeyPressed(ev -> {
            if (ev.getCode() == KeyCode.SPACE) {
                Stage stage = (Stage) rootPane.getScene().getWindow();
                if (columnName.equalsIgnoreCase("particular")) {
                    SingleInputDialogs.openLedgerPopUp(stage, "Filter", input -> {
                        contralogger.debug("Journal Ledger Popup opened");
                        String ledgerId = (String) input[1];
                        String ledgerName = (String) input[0];
                        String ledgerType = (String) input[4];
                        String balancingType = (String) input[5];
                        String balancingMethod = (String) input[6];
                        textField.setText(ledgerName);
                        PaymentRowDTO newRow;
                        // Get the index of the selected row
                        int selectedIndex = getTableRow().getIndex();

                        newRow = getTableView().getItems().get(selectedIndex);
                        // Add or update the row in the table
                        if (newRow != null) {
                            // Add the new row if selected index is invalid
                            newRow.setLedgerId(Long.valueOf(ledgerId));
                            newRow.setLedgerType(ledgerType);
                            newRow.setBalancingMethod(balancingMethod);
                            newRow.setLedgerName(ledgerName);
                            newRow.setParticulars(ledgerName);
                            newRow.setType(newRow.getType());
                        }
                        selectedLedgerDataInfo(ledgerId);
                        contralogger.debug("Journal Ledger Popup ---> Ledger Id->> " + ledgerId + " Ledger Type->> " + ledgerType + " Balancing Method->> " + balancingMethod);
                        contralogger.debug("Bill by Bill by Window --> Opened Ledger Bill Modal:On Clicking by " + "selecting the Ledger Name ");
                        if (balancingMethod.equalsIgnoreCase("bill-by-bill")) {
                            getLedgerPendingBills(stage, ledgerId, ledgerType, balancingMethod);
                        }
                    },in->{
                        System.out.println("In >> Called >. " + in);
                    });
                }
            }
        });
        textField.setOnMouseClicked(actionEvent -> {
            Stage stage = (Stage) rootPane.getScene().getWindow();
            if (columnName.equalsIgnoreCase("particular")) {
                SingleInputDialogs.openLedgerPopUp(stage, "Filter", input -> {
                    contralogger.debug("Journal Ledger Popup opened");
                    String ledgerId = (String) input[1];
                    String ledgerName = (String) input[0];
                    String ledgerType = (String) input[4];
                    String balancingType = (String) input[5];
                    String balancingMethod = (String) input[6];
                    textField.setText(ledgerName);
                    PaymentRowDTO newRow;
                    // Get the index of the selected row
                    int selectedIndex = getTableRow().getIndex();

                    newRow = getTableView().getItems().get(selectedIndex);
                    // Add or update the row in the table
                    if (newRow != null) {
                        // Add the new row if selected index is invalid
                        newRow.setLedgerId(Long.valueOf(ledgerId));
                        newRow.setLedgerType(ledgerType);
                        newRow.setBalancingMethod(balancingMethod);
                        newRow.setLedgerName(ledgerName);
                        newRow.setParticulars(ledgerName);
                        newRow.setType(newRow.getType());
                    }
                    selectedLedgerDataInfo(ledgerId);
                    contralogger.debug("Journal Ledger Popup ---> Ledger Id->> " + ledgerId + " Ledger Type->> " + ledgerType + " Balancing Method->> " + balancingMethod);
                    contralogger.debug("Bill by Bill by Window --> Opened Ledger Bill Modal:On Clicking by " + "selecting the Ledger Name ");
                    if (balancingMethod.equalsIgnoreCase("bill-by-bill")) {
                        getLedgerPendingBills(stage, ledgerId, ledgerType, balancingMethod);
                    }
                },in->{
                    System.out.println("In >> Called >. " + in);
                });
            }

        });
        textField.setOnAction(event -> {
            Stage stage = (Stage) rootPane.getScene().getWindow();
            if (columnName.equalsIgnoreCase("credit")) {
                commitEdit(textField.getText());
                String[] s = new String[]{"true"};
                if (callback != null) {
                    callback.call(s);
                }
            } else if (columnName.equalsIgnoreCase("debit")) {
                commitEdit(textField.getText());
                String[] s = new String[]{"true"};
                if (callback != null) {
                    callback.call(s);
                }

            }
        });

    }

    public TextFieldTableCellForContra(String columnName, Parent rootPane, TableCellCallback<String[]> callback,TableCellCallback<Object[]> addPrdCalbak) {
        this.columnName = columnName;
        this.callback = callback;
        this.textField = new TextField();
        this.addPrdCalbak = addPrdCalbak;


        this.textField.setOnAction(event -> {

            commitEdit(textField.getText());


        });

        // Commit when focus is lost
        this.textField.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal) {
                commitEdit(textField.getText());
            }
        });

        this.textField.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal) {
                if (getTableRow().getItem().getType().equalsIgnoreCase("cr")) {
                    if (columnName.equals("debit")) {
                        textField.setEditable(false);
                        textField.setFocusTraversable(false);
                    }
                }
                if (getTableRow().getItem().getType().equalsIgnoreCase("dr")) {
                    if (columnName.equals("credit")) {
                        textField.setEditable(false);
                        textField.setFocusTraversable(false);
                    }
                }
            }
        });

        // Commit when Tab key is pressed
        this.textField.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.TAB) {
                commitEdit(textField.getText());
            }
        });

        textField.setStyle("-fx-background-color: #f6f6f9; -fx-border-radius: 0px; -fx-border-width: 1; -fx-border-color: #f6f6f9;");

        textField.setPrefHeight(38);
        textField.setMaxHeight(38);
        textField.setMinHeight(38);

        textField.focusedProperty().addListener((obs, wasFocused, isNowFocused) -> {
            if (isNowFocused) {
                textField.setStyle("-fx-background-color: #fff9c4; -fx-border-radius: 0px; -fx-border-width: 2; -fx-border-color: #00a0f5;");
            } else {
                textField.setStyle("-fx-background-color: #f6f6f9; -fx-border-radius: 0px; -fx-border-width: 2; -fx-border-color: #f6f6f9;");
            }
        });
        textField.setOnKeyPressed(ev -> {
            if (ev.getCode() == KeyCode.SPACE) {
                Stage stage = (Stage) rootPane.getScene().getWindow();
                if (columnName.equalsIgnoreCase("particular")) {
                    SingleInputDialogs.openLedgerPopUp(stage, "Filter", input -> {
                        contralogger.debug("Journal Ledger Popup opened");
                        String ledgerId = (String) input[1];
                        String ledgerName = (String) input[0];
                        String ledgerType = (String) input[4];
                        String balancingType = (String) input[5];
                        String balancingMethod = (String) input[6];
                        textField.setText(ledgerName);
                        PaymentRowDTO newRow;
                        // Get the index of the selected row
                        int selectedIndex = getTableRow().getIndex();

                        newRow = getTableView().getItems().get(selectedIndex);
                        // Add or update the row in the table
                        if (newRow != null) {
                            // Add the new row if selected index is invalid
                            newRow.setLedgerId(Long.valueOf(ledgerId));
                            newRow.setLedgerType(ledgerType);
                            newRow.setBalancingMethod(balancingMethod);
                            newRow.setLedgerName(ledgerName);
                            newRow.setParticulars(ledgerName);
                            newRow.setType(newRow.getType());
                        }
                        selectedLedgerDataInfo(ledgerId);
                        contralogger.debug("Journal Ledger Popup ---> Ledger Id->> " + ledgerId + " Ledger Type->> " + ledgerType + " Balancing Method->> " + balancingMethod);
                        contralogger.debug("Bill by Bill by Window --> Opened Ledger Bill Modal:On Clicking by " + "selecting the Ledger Name ");
                        if (balancingMethod.equalsIgnoreCase("bill-by-bill")) {
                            getLedgerPendingBills(stage, ledgerId, ledgerType, balancingMethod);
                        }
                    },in->{
                        System.out.println("In >> Called >. " + in);
                    });
                }
            }
        });
        textField.setOnMouseClicked(actionEvent -> {
            Stage stage = (Stage) rootPane.getScene().getWindow();
            if (columnName.equalsIgnoreCase("particular")) {
                SingleInputDialogs.openLedgerPopUp(stage, "Filter", input -> {
                    contralogger.debug("Journal Ledger Popup opened");
                    String ledgerId = (String) input[1];
                    String ledgerName = (String) input[0];
                    String ledgerType = (String) input[4];
                    String balancingType = (String) input[5];
                    String balancingMethod = (String) input[6];
                    textField.setText(ledgerName);
                    PaymentRowDTO newRow;
                    // Get the index of the selected row
                    int selectedIndex = getTableRow().getIndex();

                    newRow = getTableView().getItems().get(selectedIndex);
                    // Add or update the row in the table
                    if (newRow != null) {
                        // Add the new row if selected index is invalid
                        newRow.setLedgerId(Long.valueOf(ledgerId));
                        newRow.setLedgerType(ledgerType);
                        newRow.setBalancingMethod(balancingMethod);
                        newRow.setLedgerName(ledgerName);
                        newRow.setParticulars(ledgerName);
                        newRow.setType(newRow.getType());
                    }
                    selectedLedgerDataInfo(ledgerId);
                    contralogger.debug("Journal Ledger Popup ---> Ledger Id->> " + ledgerId + " Ledger Type->> " + ledgerType + " Balancing Method->> " + balancingMethod);
                    contralogger.debug("Bill by Bill by Window --> Opened Ledger Bill Modal:On Clicking by " + "selecting the Ledger Name ");
                    if (balancingMethod.equalsIgnoreCase("bill-by-bill")) {
                        getLedgerPendingBills(stage, ledgerId, ledgerType, balancingMethod);
                    }
                },in->{
                    System.out.println("In >> Called >. " + in);
                });
            }

        });
        textField.setOnAction(event -> {
            Stage stage = (Stage) rootPane.getScene().getWindow();
            if (columnName.equalsIgnoreCase("credit")) {
                commitEdit(textField.getText());
                String[] s = new String[]{"true"};
                if (callback != null) {
                    callback.call(s);
                }
            } else if (columnName.equalsIgnoreCase("debit")) {
                commitEdit(textField.getText());
                String[] s = new String[]{"true"};
                if (callback != null) {
                    callback.call(s);
                }

            }
        });

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
        if (columnName.equalsIgnoreCase("type")) {
            ((PaymentRowDTO) getTableRow().getItem()).setType(newValue.isEmpty() ? "DR" : newValue);
        } else if (columnName.equalsIgnoreCase("particular")) {
//            System.out.println("ColumnName 2 : "+ columnName + "NewValue : "+ newValue);
//            ((PaymentRowDTO) getTableRow().getItem()).setParticulars(newValue.isEmpty() ? "" : newValue);
        } else if (columnName.equalsIgnoreCase("debit")) {
//            System.out.println("ColumnName 3 : "+ columnName);
            ((PaymentRowDTO) getTableRow().getItem()).setDebit(newValue.isEmpty() ? "" : newValue);
        } else if (columnName.equalsIgnoreCase("credit")) {
//            System.out.println("ColumnName 4 : "+ columnName);
            ((PaymentRowDTO) getTableRow().getItem()).setCredit(newValue.isEmpty() ? "" : newValue);
        }
    }

    public void commitEdit(String newValue, int index) {
        if (columnName.equalsIgnoreCase("particular")) {
            System.out.println("Value : " + newValue + " Index : " + index);
            ((PaymentRowDTO) getTableRow().getItem()).setParticulars(newValue.isEmpty() ? "" : newValue);
        }

    }

    //function for showing the ledger info at bottom
    public void selectedLedgerDataInfo(String ledgerId) {
        Map<String, String> body = new HashMap<>();
        body.put("ledger_id", ledgerId);

        String requestBody = Globals.mapToStringforFormData(body);
        HttpResponse<String> response = APIClient.postFormDataRequest(requestBody, EndPoints.TRANSACTION_LEDGER_DETAILS);
        JsonObject jsonObject = new Gson().fromJson(response.body(), JsonObject.class);
        ObservableList<TranxLedgerWindowDTO> observableList = FXCollections.observableArrayList();
        if (jsonObject.get("responseStatus").getAsInt() == 200) {
            JsonObject item = jsonObject.get("result").getAsJsonObject();
            String[] strings = new String[10];
            strings[0] = item.get("gst_number").getAsString();
            strings[1] = item.get("area") != null ? item.get("area").getAsString() : "";
            strings[2] = item.get("bank_name").getAsString();
            strings[3] = item.get("contact_name").getAsString();
            strings[4] = item.get("contact_no").getAsString();
            strings[5] = item.get("credit_days").getAsString();
            strings[6] = item.get("fssai_number").getAsString();
            strings[7] = item.get("license_number").getAsString();
            strings[8] = item.get("route").getAsString();

            if (callback != null) {
                callback.call(strings);
            }

        }
    }

    private void getLedgerPendingBills(Stage stage, String ledgerId, String ledgerType, String balancingMethod) {
        openLedgerPendingInvoiceList(stage, "Bill By Bill", ledgerId, ledgerType, balancingMethod, (input1, input2) -> {

        });
    }

    public <T> void openLedgerPendingInvoiceList(Stage stage, String title, String ledgerId, String ledgerType, String balancingMethod, BiConsumer<ObservableList<TranxPurRowResEditDTO>, TranxPurInvoiceResEditDTO> callback) {
        List<Long> prIds = new ArrayList<>();
        OverlaysEffect.setOverlaysEffect(stage);
        Stage primaryStage = new Stage();
        primaryStage.initOwner(stage); // Set the owner stage
        primaryStage.initStyle(StageStyle.TRANSPARENT);
        primaryStage.initModality(Modality.APPLICATION_MODAL);
        //Main Layout................................................................................................................................
        BorderPane borderPane = new BorderPane();
        borderPane.getStylesheets().add(GenivisApplication.class.getResource("/com/opethic/genivis/ui/css/popup_for_catalog.css").toExternalForm());
        borderPane.setStyle("-fx-background-radius: 5; -fx-background-color: white; -fx-border-color: #bfbfc0; -fx-border-radius: 5; -fx-border-width: 0.8;");
        Platform.runLater(() -> borderPane.requestFocus());
        /*** TOP LAYOUT.................................................................................................................... ***/
        VBox vbox_parent_top = new VBox();
        HBox hbox_top1 = new HBox();
        HBox hbox_top2 = new HBox();
        HBox hbox_top3 = new HBox();
        /**** setup all hbox_top1 component ****/
        hbox_top1.setMinWidth(1098);
        hbox_top1.setMaxWidth(1098);
        hbox_top1.setPrefWidth(1098);
        hbox_top1.setMaxHeight(50);
        hbox_top1.setMinHeight(50);
        hbox_top1.setPrefHeight(50);
        Label popup_title = new Label(title);
        popup_title.setStyle("-fx-font-size: 16; -fx-text-fill: #404040; -fx-font-weight: bold;");
        popup_title.setPadding(new Insets(0, 10, 0, 0));
        Image image = new Image(GenivisApplication.class.getResource("/com/opethic/genivis/ui/assets/close.png").toExternalForm());
        ImageView imageView = new ImageView(image);
        imageView.setStyle("-fx-cursor: hand;");
        imageView.setOnMouseClicked(event -> {
            primaryStage.close();
            OverlaysEffect.removeOverlaysEffect(stage);
        });
        HBox.setMargin(imageView, new Insets(10, 10, 0, 10));
        imageView.setFitWidth(30);
        imageView.setFitHeight(30);
        hbox_top1.setAlignment(Pos.CENTER_LEFT);
        hbox_top1.getChildren().add(popup_title);
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        HBox.setMargin(popup_title, new Insets(0, 0, 0, 10));
        hbox_top1.getChildren().add(spacer);
        hbox_top1.getChildren().add(imageView);
        hbox_top1.setStyle("-fx-background-radius: 5 5 0 0; -fx-background-color: #D9F0FB; -fx-border-color: #C7C7CD; -fx-border-width: 0 0 0.5 0;");
        /**** END hox_top1 ****/

        /**** setup all hbox_top2 component ****/
        hbox_top2.setMinWidth(1098);
        hbox_top2.setMaxWidth(1098);
        hbox_top2.setPrefWidth(1098);
        hbox_top2.setMaxHeight(55);
        hbox_top2.setMinHeight(55);
        hbox_top2.setPrefHeight(55);
        hbox_top2.setAlignment(Pos.CENTER_LEFT);
        hbox_top2.setStyle("-fx-background-radius: 5 5 0 0; -fx-background-color: #E6F2F8; -fx-border-color: #C7C7CD; -fx-border-width: 0 0 0.5 0;");
        Label lblPayableAmt = new Label("Payable Amt");
        lblPayableAmt.setStyle("-fx-font-size: 15; -fx-text-fill: #404040;-fx-font-weight: bold;");
        HBox.setMargin(lblPayableAmt, new Insets(0, 0, 0, 8));
        TextField tfPayableAmt = new TextField();
        tfPayableAmt.setPromptText("Payable Amt.");
        HBox.setMargin(tfPayableAmt, new Insets(0, 0, 0, 30));
        Label lblSelectedAmt = new Label("Selected Amt");
        lblSelectedAmt.setStyle("-fx-font-size: 15; -fx-text-fill: #404040;-fx-font-weight: bold;");
        HBox.setMargin(lblSelectedAmt, new Insets(0, 0, 0, 15));
        TextField tfSelectedAmt = new TextField();
        tfSelectedAmt.setPromptText("Selected Amt.");
        HBox.setMargin(tfSelectedAmt, new Insets(0, 0, 0, 30));
        Label lblRemainingAmt = new Label("Remaining Amt");
        lblRemainingAmt.setStyle("-fx-font-size: 15; -fx-text-fill: #404040;-fx-font-weight: bold;");
        HBox.setMargin(lblRemainingAmt, new Insets(0, 0, 0, 15));
        TextField tfRemainingAmt = new TextField();
        tfRemainingAmt.setPromptText("Remaining Amt.");
        HBox.setMargin(tfRemainingAmt, new Insets(0, 0, 0, 30));
        CheckBox cbAdvanceAmt = new CheckBox("Advance Amt.");
        cbAdvanceAmt.setStyle("-fx-font-size: 15; -fx-text-fill: #404040;-fx-font-weight: bold;");
        HBox.setMargin(cbAdvanceAmt, new Insets(0, 0, 0, 30));
        hbox_top2.getChildren().addAll(lblPayableAmt, tfPayableAmt, lblSelectedAmt, tfSelectedAmt, lblRemainingAmt, tfRemainingAmt, cbAdvanceAmt);
        /**** END hox_top2 ****/
        /**** setup all hbox_top3 component ****/
        hbox_top3.setAlignment(Pos.CENTER_RIGHT);
        Label lblFromDt = new Label("From Date");
        lblFromDt.setStyle("-fx-font-size: 15; -fx-text-fill: #404040;-fx-font-weight: bold;");
        HBox.setMargin(lblFromDt, new Insets(10, 0, 0, 8));
        TextField tfFromDt = new TextField();
        tfFromDt.setPromptText("DD/MM/YYYY");
        DateValidator.applyDateFormat(tfFromDt);
        HBox.setMargin(tfFromDt, new Insets(10, 0, 0, 30));
        Label lblToDt = new Label("To Date");
        lblToDt.setStyle("-fx-font-size: 15; -fx-text-fill: #404040;-fx-font-weight: bold;");
        HBox.setMargin(lblToDt, new Insets(10, 0, 0, 15));
        TextField tfToDt = new TextField();
        tfToDt.setPromptText("DD/MM/YYYY");
        DateValidator.applyDateFormat(tfToDt);
        sceneInitilization(borderPane);
        HBox.setMargin(tfToDt, new Insets(10, 0, 0, 30));
        Label lblClose = new Label("Clear");
        lblClose.setStyle("-fx-font-size: 15; -fx-text-fill: #404040;-fx-font-weight: bold;");
        HBox.setMargin(lblClose, new Insets(10, 0, 0, 15));
        Image imgClear = new Image(GenivisApplication.class.getResource("/com/opethic/genivis/ui/assets/cross_.png").toExternalForm());
        Image imgFilter = new Image(GenivisApplication.class.getResource("/com/opethic/genivis/ui/assets/filter.png").toExternalForm());
        ImageView imageClear = new ImageView(imgClear);
        ImageView filterView = new ImageView(imgFilter);
        imageClear.setStyle("-fx-cursor: hand;");
        filterView.setStyle("-fx-cursor: hand;");
        lblFromDt.setManaged(false);
        lblFromDt.setVisible(false);
        tfFromDt.setManaged(false);
        tfFromDt.setVisible(false);
        lblToDt.setManaged(false);
        lblToDt.setVisible(false);
        tfToDt.setManaged(false);
        tfToDt.setVisible(false);
        lblClose.setText("Filter");
        imageClear.setManaged(false);
        imageClear.setVisible(false);
        filterView.setManaged(true);
        filterView.setVisible(true);
        imageClear.setOnMouseClicked(event -> {
            lblFromDt.setManaged(false);
            lblFromDt.setVisible(false);
            tfFromDt.setManaged(false);
            tfFromDt.setVisible(false);
            lblToDt.setManaged(false);
            lblToDt.setVisible(false);
            tfToDt.setManaged(false);
            tfToDt.setVisible(false);
            lblClose.setText("Filter");
            imageClear.setManaged(false);
            imageClear.setVisible(false);
            filterView.setManaged(true);
            filterView.setVisible(true);
            fetchInvoiceList(ledgerId, ledgerType, balancingMethod, "", "");
        });
        filterView.setOnMouseClicked(event -> {
            lblFromDt.setManaged(true);
            lblFromDt.setVisible(true);
            tfFromDt.setManaged(true);
            tfFromDt.setVisible(true);
            lblToDt.setManaged(true);
            lblToDt.setVisible(true);
            tfToDt.setManaged(true);
            tfToDt.setVisible(true);
            lblClose.setText("Clear");
            imageClear.setManaged(true);
            imageClear.setVisible(true);
            filterView.setManaged(false);
            filterView.setVisible(false);
        });
        HBox.setMargin(imageClear, new Insets(10, 10, 0, 10));
        HBox.setMargin(filterView, new Insets(10, 10, 0, 10));

        imageClear.setFitWidth(20);
        imageClear.setFitHeight(20);
        filterView.setFitWidth(18);
        filterView.setFitHeight(18);
        hbox_top3.getChildren().addAll(lblFromDt, tfFromDt, lblToDt, tfToDt, lblClose, imageClear, filterView);
        /**** END hox_top3 ****/
        vbox_parent_top.getChildren().addAll(hbox_top1, hbox_top2, hbox_top3);
        borderPane.setTop(vbox_parent_top);
        /*** END TOP LAYOUT ***/

        /*** CENTER LAYOUT.................................................................................................................... ***/
        VBox vBoxCenter = new VBox();
        HBox hBoxCenter1 = new HBox();
        /**** Setting up all Component for hBoxCenter1 ****/
        Label lblInvoiceTable = new Label("Invoice List :");
        lblInvoiceTable.setStyle("-fx-font-size: 15; -fx-text-fill: #404040; -fx-font-weight: bold;");
        HBox.setMargin(lblInvoiceTable, new Insets(5, 0, 8, 5));
        hBoxCenter1.getChildren().add(lblInvoiceTable);
        /**** END hBoxCenter1 ****/
        /**** Setting up all Component for TableView Invoice List ****/
        TableView<TranxPaymentInvoiceDTO> tableView = new TableView();
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        String tableStyle = ".table-row-cell:selected {-fx-background-color: #E1E0E0;-fx-text-background-color:black;}";
        tableView.setStyle(tableStyle);

        tableView.setPrefHeight(170);
        tableView.setMaxHeight(170);
        tableView.setMinHeight(170);
        TableColumn<TranxPaymentInvoiceDTO, CheckBox> tcSelect = new TableColumn<>("Select");
        TableColumn<TranxPaymentInvoiceDTO, String> tcInvoiceNo = new TableColumn<>("Invoice No");
        TableColumn<TranxPaymentInvoiceDTO, String> tcInvoiceAmt = new TableColumn<>("Invoice Amt.");
        TableColumn<TranxPaymentInvoiceDTO, String> tcType = new TableColumn<>("Type");
        TableColumn<TranxPaymentInvoiceDTO, String> tcBillDate = new TableColumn<>("Bill Date");
        TableColumn<TranxPaymentInvoiceDTO, String> tcBalance = new TableColumn<>("Balance");
        TableColumn<TranxPaymentInvoiceDTO, String> tcDueDate = new TableColumn<>("Due Date");
        TableColumn<TranxPaymentInvoiceDTO, String> tcDay = new TableColumn<>("Day");
        TableColumn<TranxPaymentInvoiceDTO, String> tcPaidAmt = new TableColumn<>("Paid Amt.");
        TableColumn<TranxPaymentInvoiceDTO, String> tcBalanceAmt = new TableColumn<>("Balance Amt.");
        tcInvoiceAmt.setStyle("-fx-alignment: CENTER-RIGHT;");
        tcBalance.setStyle("-fx-alignment: CENTER-RIGHT;");
        tcPaidAmt.setStyle("-fx-alignment: CENTER-RIGHT;");
        tcBalanceAmt.setStyle("-fx-alignment: CENTER-RIGHT;");
        /**** Table Responsive Code ****/
        tcSelect.prefWidthProperty().bind(tableView.widthProperty().multiply(0.08));
        tcInvoiceNo.prefWidthProperty().bind(tableView.widthProperty().multiply(0.25));
        tcInvoiceAmt.prefWidthProperty().bind(tableView.widthProperty().multiply(0.21));
        tcType.prefWidthProperty().bind(tableView.widthProperty().multiply(0.08));
        tcBillDate.prefWidthProperty().bind(tableView.widthProperty().multiply(0.15));
        tcBalance.prefWidthProperty().bind(tableView.widthProperty().multiply(0.21));
        tcDueDate.prefWidthProperty().bind(tableView.widthProperty().multiply(0.15));
        tcDay.prefWidthProperty().bind(tableView.widthProperty().multiply(0.08));
        tcPaidAmt.prefWidthProperty().bind(tableView.widthProperty().multiply(0.22));
        tcBalanceAmt.prefWidthProperty().bind(tableView.widthProperty().multiply(0.22));
        /*** END ****/
        tableView.getColumns().addAll(tcSelect, tcInvoiceNo, tcInvoiceAmt, tcType, tcBillDate, tcBalance, tcDueDate,
                tcDay, tcPaidAmt, tcBalanceAmt);
        tcSelect.setCellValueFactory(new PropertyValueFactory<TranxPaymentInvoiceDTO, CheckBox>("isSelect"));
        tcInvoiceNo.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getInvoiceNo()));
        tcInvoiceAmt.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getInvoiceAmt()));
        tcType.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getType()));
        tcBillDate.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getBilldate()));
        tcBalance.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getBalance()));
        tcDueDate.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getDueDate()));
        tcDay.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getDays()));
        tcPaidAmt.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getPaidAmt()));
        tcBalanceAmt.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getBalanceAmt()));
        /**** END TableView Invoice List****/

        /**** Setting up all Component for Hbox2 ****/
        Label lblInvoiceTotal = new Label("Total");
        lblInvoiceTotal.setMaxWidth(570);
        lblInvoiceTotal.setMinWidth(570);
        lblInvoiceTotal.setPrefWidth(570);
        lblInvoiceTotal.setStyle("-fx-font-size: 15; -fx-text-fill: #404040; -fx-font-weight: bold;");
        HBox.setMargin(lblInvoiceTotal, new Insets(0, 0, 0, 5));

        Label lblBalance = new Label("0.00");
        lblBalance.setStyle("-fx-font-size: 15; -fx-text-fill: #404040; -fx-font-weight: bold;");
        lblBalance.setMaxWidth(290);
        lblBalance.setMinWidth(290);
        lblBalance.setPrefWidth(290);

        Label lblPaidAmt = new Label("0.00");
        lblPaidAmt.setStyle("-fx-font-size: 15; -fx-text-fill: #404040; -fx-font-weight: bold;");
        lblPaidAmt.setMaxWidth(150);
        lblPaidAmt.setMinWidth(150);
        lblPaidAmt.setPrefWidth(150);

        Label lblInvoiceBalance = new Label("0.00");
        lblInvoiceBalance.setStyle("-fx-font-size: 15; -fx-text-fill: #404040; -fx-font-weight: bold;");
        HBox hBoxCenter2 = new HBox();
        hBoxCenter2.setStyle("-fx-background-color: #D2F6E9;");
        hBoxCenter2.setMaxHeight(35);
        hBoxCenter2.setMinHeight(35);
        hBoxCenter2.setPrefHeight(35);
        hBoxCenter2.setAlignment(Pos.CENTER_LEFT);
        hBoxCenter2.getChildren().addAll(lblInvoiceTotal, lblBalance, lblPaidAmt, lblInvoiceBalance);
        /**** END Hbox2 ****/

        /**** DEBIT NOTE START ****/
        HBox hBoxCenter3 = new HBox();
        /**** Setting up all Component for hBoxCenter3 ****/
        Label lblDebitNoteTable = new Label("Debit Note :");
        lblDebitNoteTable.setStyle("-fx-font-size: 15; -fx-text-fill: #404040; -fx-font-weight: bold;");
        HBox.setMargin(lblDebitNoteTable, new Insets(20, 0, 8, 5));
        hBoxCenter3.getChildren().add(lblDebitNoteTable);
        /**** END hBoxCenter3 ****/
        /**** Setting up all Component for TableView Debit Note****/
        TableView<TranxPaymentInvoiceDTO> tableViewDN = new TableView();
        tableViewDN.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tableViewDN.setPrefHeight(155);
        tableViewDN.setMaxHeight(155);
        tableViewDN.setMinHeight(155);
        TableColumn<TranxPaymentInvoiceDTO, CheckBox> tcSelectDN = new TableColumn<>("Select");
        TableColumn<TranxPaymentInvoiceDTO, String> tcInvoiceNoDN = new TableColumn<>("Invoice No");
        TableColumn<TranxPaymentInvoiceDTO, String> tcInvoiceAmtDN = new TableColumn<>("Invoice Amt.");
        TableColumn<TranxPaymentInvoiceDTO, String> tcTypeDN = new TableColumn<>("Type");
        TableColumn<TranxPaymentInvoiceDTO, String> tcBillDateDN = new TableColumn<>("Bill Date");
        TableColumn<TranxPaymentInvoiceDTO, String> tcBalanceDN = new TableColumn<>("Balance");
        TableColumn<TranxPaymentInvoiceDTO, String> tcDueDateDN = new TableColumn<>("Due Date");
        TableColumn<TranxPaymentInvoiceDTO, String> tcDayDN = new TableColumn<>("Day");
        TableColumn<TranxPaymentInvoiceDTO, String> tcPaidAmtDN = new TableColumn<>("Paid Amt.");
        TableColumn<TranxPaymentInvoiceDTO, String> tcBalanceAmtDN = new TableColumn<>("Balance Amt.");
        tcInvoiceAmtDN.setStyle("-fx-alignment: CENTER-RIGHT;");
        tcBalanceDN.setStyle("-fx-alignment: CENTER-RIGHT;");
        tcPaidAmtDN.setStyle("-fx-alignment: CENTER-RIGHT;");
        tcBalanceAmtDN.setStyle("-fx-alignment: CENTER-RIGHT;");
        /**** Table Responsive Code ****/
        tcSelectDN.prefWidthProperty().bind(tableViewDN.widthProperty().multiply(0.08));
        tcInvoiceNoDN.prefWidthProperty().bind(tableViewDN.widthProperty().multiply(0.25));
        tcInvoiceAmtDN.prefWidthProperty().bind(tableViewDN.widthProperty().multiply(0.21));
        tcTypeDN.prefWidthProperty().bind(tableViewDN.widthProperty().multiply(0.08));
        tcBillDateDN.prefWidthProperty().bind(tableViewDN.widthProperty().multiply(0.15));
        tcBalanceDN.prefWidthProperty().bind(tableViewDN.widthProperty().multiply(0.21));
        tcDueDateDN.prefWidthProperty().bind(tableViewDN.widthProperty().multiply(0.15));
        tcDayDN.prefWidthProperty().bind(tableViewDN.widthProperty().multiply(0.08));
        tcPaidAmtDN.prefWidthProperty().bind(tableViewDN.widthProperty().multiply(0.22));
        tcBalanceAmtDN.prefWidthProperty().bind(tableViewDN.widthProperty().multiply(0.22));
        /*** END ****/

        tableViewDN.getColumns().addAll(tcSelectDN, tcInvoiceNoDN, tcInvoiceAmtDN, tcTypeDN, tcBillDateDN, tcBalanceDN,
                tcDueDateDN, tcDayDN, tcPaidAmtDN, tcBalanceAmtDN);
        tcSelectDN.setCellValueFactory(new PropertyValueFactory<TranxPaymentInvoiceDTO, CheckBox>("isSelect"));
        tcInvoiceNoDN.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getInvoiceNo()));
        tcInvoiceAmtDN.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getInvoiceAmt()));
        tcTypeDN.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getType()));
        tcBillDateDN.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getBilldate()));
        tcBalanceDN.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getBalance()));
        tcDueDateDN.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getDueDate()));
        tcDayDN.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getDays()));
        tcPaidAmtDN.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getPaidAmt()));
        tcBalanceAmtDN.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getBalanceAmt()));
        /**** Setting up all Component for Hbox4 ****/
        Label lblInvoiceTotalDN = new Label("Total");
        lblInvoiceTotalDN.setMaxWidth(570);
        lblInvoiceTotalDN.setMinWidth(570);
        lblInvoiceTotalDN.setPrefWidth(570);
        lblInvoiceTotalDN.setStyle("-fx-font-size: 15; -fx-text-fill: #404040; -fx-font-weight: bold;");
        HBox.setMargin(lblInvoiceTotalDN, new Insets(0, 0, 0, 5));

        Label lblBalanceDN = new Label("0.00");
        lblBalanceDN.setStyle("-fx-font-size: 15; -fx-text-fill: #404040; -fx-font-weight: bold;");
        lblBalanceDN.setMaxWidth(290);
        lblBalanceDN.setMinWidth(290);
        lblBalanceDN.setPrefWidth(290);

        Label lblPaidAmtDN = new Label("0.00");
        lblPaidAmtDN.setStyle("-fx-font-size: 15; -fx-text-fill: #404040; -fx-font-weight: bold;");
        lblPaidAmtDN.setMaxWidth(150);
        lblPaidAmtDN.setMinWidth(150);
        lblPaidAmtDN.setPrefWidth(150);

        Label lblInvoiceBalanceDN = new Label("0.00");
        lblInvoiceBalanceDN.setStyle("-fx-font-size: 15; -fx-text-fill: #404040; -fx-font-weight: bold;");
        HBox hBoxCenter4 = new HBox();
        hBoxCenter4.setStyle("-fx-background-color: #D2F6E9;");
        hBoxCenter4.setMaxHeight(35);
        hBoxCenter4.setMinHeight(35);
        hBoxCenter4.setPrefHeight(35);
        hBoxCenter4.setAlignment(Pos.CENTER_LEFT);
        hBoxCenter4.getChildren().addAll(lblInvoiceTotalDN, lblBalanceDN, lblPaidAmtDN, lblInvoiceBalanceDN);
        /**** END Hbox2 ****/
        /**** API call of Ledger Invoice Pending List ****/
        Platform.runLater(() ->
        {
            fetchInvoiceList(ledgerId, ledgerType, balancingMethod, "", "");
        });
        vBoxCenter.getChildren().addAll(hBoxCenter1, tableView, hBoxCenter2, hBoxCenter3, tableViewDN, hBoxCenter4);
        borderPane.setCenter(vBoxCenter);
        /*** END CENTER LAYOUT ***/

        /****** BOTTOM LAYOUT ******/
        VBox vbBottom = new VBox();
        HBox hbBottom1 = new HBox();
        hbBottom1.setStyle("-fx-background-color: #A0EFD2;");
        hbBottom1.setAlignment(Pos.CENTER_LEFT);
        hbBottom1.setMaxHeight(35);
        hbBottom1.setMinHeight(35);
        hbBottom1.setPrefHeight(35);
        Label lblInvoiceGrandTotal = new Label("Grand Total");
        lblInvoiceGrandTotal.setStyle("-fx-font-size: 17; -fx-text-fill: #404040; -fx-font-weight: bold;");
        lblInvoiceGrandTotal.setMaxWidth(860);
        lblInvoiceGrandTotal.setMinWidth(860);
        lblInvoiceGrandTotal.setPrefWidth(860);
        HBox.setMargin(lblInvoiceGrandTotal, new Insets(0, 0, 0, 5));
        Label lblInvoicePaidAmt = new Label("0.00");
        lblInvoicePaidAmt.setStyle("-fx-font-size: 15; -fx-text-fill: #404040; -fx-font-weight: bold;");
        hbBottom1.getChildren().addAll(lblInvoiceGrandTotal, lblInvoicePaidAmt);

        Region spacer1 = new Region();
        HBox.setHgrow(spacer1, Priority.ALWAYS);
        HBox hbBottom2 = new HBox();
        Button submit = new Button("Submit");
        submit.setMinWidth(80);
        submit.setMaxWidth(80);
        submit.setMinHeight(30);
        submit.setMaxHeight(30);
        submit.setId("sub");

        hbBottom2.getChildren().add(spacer1);
        hbBottom2.getChildren().add(submit);
        HBox.setMargin(submit, new Insets(8, 8, 5, 0));

        final String IDLE_BUTTON_STYLE = "-fx-padding: 5px 14px;-fx-font-family: \"Inter\"; sans-serif;" + "-fx-pref-width: 90px;-fx-pref-height: 34px;-fx-margin-left: 10px;-fx-font-size: 14px;" + "-fx-text-fill: white;-fx-background-color: #21c78a; -fx-font-weight: bold;-fx-background-radius: 4px;" + "-fx-border-radius: 4px;";
        final String HOVERED_BUTTON_STYLE = "-fx-padding: 5px 14px;-fx-font-family: \"Inter\", sans-serif;" + "-fx-pref-width: 90px;-fx-pref-height: 34px;-fx-margin-left: 10px;-fx-font-size: 14px;" + "-fx-text-fill: white;-fx-border-color: #20a574; -fx-background-color: #20a574;-fx-cursor: hand;" + "-fx-font-weight: bold;-fx-background-radius: 4px" + "-fx-border-radius: 4px;";
        submit.setStyle(IDLE_BUTTON_STYLE);
        submit.setOnMouseExited(e -> submit.setStyle(IDLE_BUTTON_STYLE));
        submit.setOnMouseEntered(e -> submit.setStyle(HOVERED_BUTTON_STYLE));

        vbBottom.getChildren().addAll(hbBottom1, hbBottom2);
        borderPane.setBottom(vbBottom);

        Scene scene = new Scene(borderPane, 1100, 750);

        primaryStage.setScene(scene);

        primaryStage.setResizable(false);

        scene.setFill(Color.TRANSPARENT);

        primaryStage.centerOnScreen();

        primaryStage.show();

        tfFromDt.setOnKeyPressed(event -> {    //code to focus on purchase serial from supplier GSTIN
            if (event.getCode() == KeyCode.ENTER || event.getCode() == KeyCode.TAB) {
                tfToDt.requestFocus();
            }
        });
        tfToDt.setOnKeyPressed(event -> {    //code to focus on purchase serial from supplier GSTIN
            if (event.getCode() == KeyCode.ENTER || event.getCode() == KeyCode.TAB) {
                String fromDate = "";
                String toDate = "";
                if (tfFromDt != null && tfFromDt.getText() != null &&
                        !tfFromDt.getText().isEmpty()) {
                    fromDate = Communicator.text_to_date.fromString(tfFromDt.getText()).toString();
                }
                if (tfToDt != null && tfToDt.getText() != null &&
                        !tfToDt.getText().isEmpty()) {
                    toDate = Communicator.text_to_date.fromString(tfToDt.getText()).toString();
                }
                fetchInvoiceList(ledgerId, ledgerType, balancingMethod, fromDate, toDate);
            }
        });
        tableView.setItems(mLedgerInvoiceList);
        tableViewDN.setItems(mLedgerDebitNoteList);
    }

    private void sceneInitilization(BorderPane borderPane) {
        borderPane.sceneProperty().addListener((observable, oldScene, newScene) -> {
            if (newScene != null && newScene.getWindow() instanceof Stage) {
                Communicator.stage = (Stage) newScene.getWindow();
            }
        });
    }

    private void fetchInvoiceList(String ledgerId, String ledgerType, String balancingMethod, String fromDate, String toDate) {
        mLedgerInvoiceList.clear();
        mLedgerDebitNoteList.clear();
        APIClient apiClient = null;
        try {
            Map<String, String> body = new HashMap<>();
            body.put("ledger_id", ledgerId);
            body.put("type", ledgerType);
            body.put("balancing_method", balancingMethod);
            if (!fromDate.equalsIgnoreCase("") && !toDate.equalsIgnoreCase("")) {
                body.put("start_date", "");
                body.put("end_date", "");
            }
            String reqData = Globals.mapToStringforFormData(body);
            System.out.println("Form Data:" + reqData);
            apiClient = new APIClient(EndPoints.GET_DEBTORS_PENDING_BILLS, reqData, RequestType.FORM_DATA);
            System.out.println("APICLIENT Success : " + apiClient);
            apiClient.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
                @Override
                public void handle(WorkerStateEvent workerStateEvent) {
                    String response = workerStateEvent.getSource().getValue().toString();
                    System.out.println("Getting response : " + response);
                    LedgerPendingBillResDTO pendingBillResDTO = new Gson().fromJson(response, LedgerPendingBillResDTO.class);
                    System.out.println("Getting pendingBillResDTO : " + pendingBillResDTO);
                    if (pendingBillResDTO.getResponseStatus() == 200) {
                        for (InvoiceListResDTO mList : pendingBillResDTO.getList()) {
                            TranxPaymentInvoiceDTO invoiceData = new TranxPaymentInvoiceDTO();
                            System.out.println("Invoice DAta  ; " + invoiceData);
//                            invoiceData.setIsSelect(new CheckBox());
                            invoiceData.setId("" + mList.getInvoiceId());
                            invoiceData.setInvoiceNo(mList.getInvoiceNo());
                            invoiceData.setInvoiceAmt("" + mList.getAmount());
                            invoiceData.setType(mList.getBalancingType());
                            invoiceData.setBilldate(mList.getInvoiceDate());
                            invoiceData.setBalance("" + mList.getRemainingAmt());
                            invoiceData.setPaidAmt("" + 0.0);
                            invoiceData.setBalanceAmt("" + mList.getAmount());
                            if (mList.getSource().equalsIgnoreCase("sales_invoice")) {
                                mLedgerInvoiceList.add(invoiceData);
                            } else {
                                invoiceData.setBalance("" + mList.getAmount());
                                mLedgerDebitNoteList.add(invoiceData);
                            }
                        }
                    } else {
                        mLedgerInvoiceList.clear();
                        mLedgerDebitNoteList.clear();
                    }
                }
            });
            apiClient.setOnFailed(new EventHandler<WorkerStateEvent>() {
                @Override
                public void handle(WorkerStateEvent workerStateEvent) {
                    mLedgerInvoiceList.clear();
                    mLedgerDebitNoteList.clear();
                }
            });
            apiClient.setOnCancelled(new EventHandler<WorkerStateEvent>() {
                @Override
                public void handle(WorkerStateEvent workerStateEvent) {
                    mLedgerInvoiceList.clear();
                    mLedgerDebitNoteList.clear();
                }
            });
            apiClient.start();
        } catch (Exception e) {

        } finally {
            apiClient = null;
        }
    }

}

class ComboBoxTableCellForContra extends TableCell<PaymentRowDTO, String> {

    private ComboBox<String> comboBox = null;
    private final String columnName;

    private ObservableList<String> comboList = FXCollections.observableArrayList("Dr", "Cr");

    public ComboBoxTableCellForContra(String columnName) {


        ImageView imageView1 = new ImageView(new Image(getClass().getResourceAsStream("/com/opethic/genivis/ui/assets/add.png")));
        imageView1.setFitWidth(20);
        imageView1.setFitHeight(20);
        this.comboBox = new ComboBox<>();
        this.comboBox.getItems().addAll("Dr", "Cr");
        this.comboBox.setValue("Dr");
        ImageView imageView2 = new ImageView(new Image(getClass().getResourceAsStream("/com/opethic/genivis/ui/assets/sub.png")));
        imageView2.setFitWidth(20);
        imageView2.setFitHeight(20);
        this.columnName = columnName;
        this.comboBox.setPromptText("Select");

        this.comboBox.setStyle("-fx-background-color: #f6f6f9; -fx-border-radius: 0px; -fx-border-width: 1; -fx-border-color: #f6f6f9;");

        this.comboBox.focusedProperty().addListener((obs, wasFocused, isNowFocused) -> {
            if (isNowFocused) {
                this.comboBox.setStyle("-fx-background-color: #fff9c4; -fx-border-radius: 0px; -fx-border-width: 2; -fx-border-color: #00a0f5;");
            } else {
                this.comboBox.setStyle("-fx-background-color: #f6f6f9; -fx-border-radius: 0px; -fx-border-width: 2; -fx-border-color: #f6f6f9;");
            }
        });

        this.comboBox.setOnAction(event -> commitEdit(comboBox.getValue().toString()));

        // Commit when focus is lost
        this.comboBox.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal) {
                commitEdit(comboBox.getValue().toString());
            }
        });

        AutoCompleteBox autoCompleteBox1 = new AutoCompleteBox(this.comboBox, -1);


        this.comboBox.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal) {
                commitEdit(comboBox.getValue());
            }
        });

        this.comboBox.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.TAB) {
                commitEdit(comboBox.getValue());
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


            String itemToSet = item;
            if (comboBox.getItems().contains(itemToSet)) {
                comboBox.setValue(itemToSet);
            }
//            if (columnName.equals("unit"))
//                System.out.println(item);

            //comboBox.setValue(item);
            HBox hbox = new HBox();
            hbox.getChildren().addAll(comboBox);
            hbox.setAlignment(Pos.CENTER);
            hbox.setSpacing(5);

            setGraphic(hbox);
        }
    }

    @Override
    public void commitEdit(String newValue) {
        super.commitEdit(newValue);
        if (columnName.equalsIgnoreCase("type")) {
            ((PaymentRowDTO) getTableRow().getItem()).setType(newValue.isEmpty() ? "DR" : newValue);
        } else if (columnName.equalsIgnoreCase("particular")) {
            ((PaymentRowDTO) getTableRow().getItem()).setParticulars(newValue.isEmpty() ? "" : newValue);
        } else if (columnName.equalsIgnoreCase("debit")) {
            ((PaymentRowDTO) getTableRow().getItem()).setDebit(newValue.isEmpty() ? "" : newValue);
        } else if (columnName.equalsIgnoreCase("credit")) {
            ((PaymentRowDTO) getTableRow().getItem()).setCredit(newValue.isEmpty() ? "" : newValue);
        }
    }

}
