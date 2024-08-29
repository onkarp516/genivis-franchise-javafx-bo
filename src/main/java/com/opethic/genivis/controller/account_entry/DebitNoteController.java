package com.opethic.genivis.controller.account_entry;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.opethic.genivis.GenivisApplication;
import com.opethic.genivis.controller.commons.OverlaysEffect;
import com.opethic.genivis.controller.dialogs.SingleInputDialogs;
import com.opethic.genivis.controller.master.ledger.common.LedgerMessageConsts;
import com.opethic.genivis.controller.tranx_purchase.PurchaseChallanController;
import com.opethic.genivis.dto.TranxLedgerWindowDTO;
import com.opethic.genivis.dto.reqres.creditNote.*;
import com.opethic.genivis.dto.reqres.debitNote.DebitNoteByIdMainDTO;
import com.opethic.genivis.dto.reqres.debitNote.DebitNoteByIdRowDTO;
import com.opethic.genivis.dto.reqres.payment.InvoiceListResDTO;
import com.opethic.genivis.dto.reqres.payment.LedgerPendingBillResDTO;
import com.opethic.genivis.dto.reqres.payment.PaymentRowDTO;
import com.opethic.genivis.dto.reqres.product.Communicator;
import com.opethic.genivis.dto.reqres.product.TableCellCallback;
import com.opethic.genivis.dto.reqres.pur_tranx.TranxPaymentInvoiceDTO;
import com.opethic.genivis.dto.reqres.pur_tranx.TranxPurInvoiceResEditDTO;
import com.opethic.genivis.dto.reqres.pur_tranx.TranxPurRowDTO;
import com.opethic.genivis.dto.reqres.pur_tranx.TranxPurRowResEditDTO;
import com.opethic.genivis.network.APIClient;
import com.opethic.genivis.network.EndPoints;
import com.opethic.genivis.network.RequestType;
import com.opethic.genivis.utils.*;
import javafx.application.Platform;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
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
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTableCell;
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
import javafx.util.Callback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URL;
import java.net.http.HttpResponse;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.function.BiConsumer;

import static com.opethic.genivis.controller.account_entry.CreditNoteController.creditNoteLogger;
import static com.opethic.genivis.controller.account_entry.PaymentCreateController.paymentLogger;
import static com.opethic.genivis.utils.FxmFileConstants.*;
import static com.opethic.genivis.utils.Globals.journalListDTO;

public class DebitNoteController implements Initializable {
    @FXML
    private BorderPane bpDebitNoteRootPane;

    @FXML
    private HBox debitNoteDivider, debitNoteDividerFirst, debitNoteDividerSecond, debitNoteDividerThird, debitNoteTwoDivider;

    @FXML
    private TextField tfDebitNoteCreateSrNo, tfDebitNoteVoucherNo, tfDebitNoteTranxDate, tfDebitNoteNarration;

    @FXML
    private TableView<PaymentRowDTO> tblvDebitNoteTable;
    @FXML
    private TableView tblvDebitNoteSupplierDetails;
    @FXML
    private TableColumn<PaymentRowDTO, String> tblcDebitNoteType, tblcDebitNoteParticulars, tblcDebitNoteDebit, tblcDebitNoteCredit;

    @FXML
    private Label lblDebitNoteTotalDebit, lblDebitNoteTotalCredit;
    @FXML
    private Label lblDebitNotePaymentMode, lblDebitNoteCheque, lblDebitNoteBankName, lblDebitNotePaymentDate;
    @FXML
    private Label lblDebitNoteGst, lblDebitNoteArea, lblDebitNoteBank, lblDebitNoteContactPerson, lblDebitNotetransport, lblDebitNoteCreditDays, lblDebitNoteFSSAI,
            lblDebitNoteLicenseNo, lblDebitNoteRoute;
    @FXML
    private Button btnDebitNoteCancel, btnDebitNoteSubmit, btnDebitNoteModify;

    private JsonObject jsonObject = null;

    String message = "";

    private Integer debitNoteEditId = -1;

    private static final Logger debitNoteLogger = LoggerFactory.getLogger(PurchaseChallanController.class);

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        ResponsiveWiseCssPicker();

        tblvDebitNoteTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);

        // DateValidator with Setting the Current Date
        DateValidator.applyDateFormat(tfDebitNoteTranxDate);
        LocalDate currentDate = LocalDate.now();
        tfDebitNoteTranxDate.setText(currentDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));

        // Focus on required Field after page Load
        Platform.runLater(this::onPageLoad);

        // Auto setting the Voucher Number and Voucher Serial Number
        getDebitNtVoucherNumber();

        // Center Table Responsive
        responsiveCmpt();

        // Scene Initialization
        sceneInitilization();

        //Table Initialization of Rows
        tableInitialization();

        //? this include all the Shortcut Keys
        initShortcutKeys();

        //  Create API Call
        btnDebitNoteSubmit.setOnAction((event) -> {
            if (CommonValidationsUtils.validateForm(tfDebitNoteTranxDate)) {
                createDebitNote();
            }
        });


    }

    private void ResponsiveWiseCssPicker() {
        double height = Screen.getPrimary().getBounds().getHeight();
        double width = Screen.getPrimary().getBounds().getWidth();
        System.out.println("width" + width);
        if (width >= 992 && width <= 1024) {
            debitNoteTwoDivider.setSpacing(10);
            debitNoteDividerFirst.prefWidthProperty().bind(debitNoteDivider.widthProperty().multiply(0.79));
            debitNoteDividerSecond.prefWidthProperty().bind(debitNoteDivider.widthProperty().multiply(0.13));
            debitNoteDividerThird.prefWidthProperty().bind(debitNoteDivider.widthProperty().multiply(0.08));
            tblvDebitNoteSupplierDetails.getStylesheets().add(GenivisApplication.class.getResource("ui/css/purchase_challan_css/supplier_table_css/supplier_table_992_1024.css").toExternalForm());
            bpDebitNoteRootPane.getStylesheets().add(GenivisApplication.class.getResource("ui/css/TranxCommonCssStyles/tranxCommonCssStyle1.css").toExternalForm());
        } else if (width >= 1025 && width <= 1280) {
            debitNoteTwoDivider.setSpacing(12);
            debitNoteDividerFirst.prefWidthProperty().bind(debitNoteDivider.widthProperty().multiply(0.79));
            debitNoteDividerSecond.prefWidthProperty().bind(debitNoteDivider.widthProperty().multiply(0.13));
            debitNoteDividerThird.prefWidthProperty().bind(debitNoteDivider.widthProperty().multiply(0.08));
            tblvDebitNoteSupplierDetails.getStylesheets().add(GenivisApplication.class.getResource("ui/css/purchase_challan_css/supplier_table_css/supplier_table_1025_1280.css").toExternalForm());
            bpDebitNoteRootPane.getStylesheets().add(GenivisApplication.class.getResource("ui/css/TranxCommonCssStyles/tranxCommonCssStyle2.css").toExternalForm());
        } else if (width >= 1281 && width <= 1368) {
            debitNoteTwoDivider.setSpacing(12);
            debitNoteDividerFirst.prefWidthProperty().bind(debitNoteDivider.widthProperty().multiply(0.79));
            debitNoteDividerSecond.prefWidthProperty().bind(debitNoteDivider.widthProperty().multiply(0.13));
            debitNoteDividerThird.prefWidthProperty().bind(debitNoteDivider.widthProperty().multiply(0.08));
            tblvDebitNoteSupplierDetails.getStylesheets().add(GenivisApplication.class.getResource("ui/css/purchase_challan_css/supplier_table_css/supplier_table_1281_1368.css").toExternalForm());
            bpDebitNoteRootPane.getStylesheets().add(GenivisApplication.class.getResource("ui/css/TranxCommonCssStyles/tranxCommonCssStyle3.css").toExternalForm());
        } else if (width >= 1369 && width <= 1400) {
            debitNoteTwoDivider.setSpacing(15);
            debitNoteDividerFirst.prefWidthProperty().bind(debitNoteDivider.widthProperty().multiply(0.79));
            debitNoteDividerSecond.prefWidthProperty().bind(debitNoteDivider.widthProperty().multiply(0.13));
            debitNoteDividerThird.prefWidthProperty().bind(debitNoteDivider.widthProperty().multiply(0.08));
            tblvDebitNoteSupplierDetails.getStylesheets().add(GenivisApplication.class.getResource("ui/css/purchase_challan_css/supplier_table_css/supplier_table_1369_1400.css").toExternalForm());
            bpDebitNoteRootPane.getStylesheets().add(GenivisApplication.class.getResource("ui/css/TranxCommonCssStyles/tranxCommonCssStyle4.css").toExternalForm());
        } else if (width >= 1401 && width <= 1440) {
            debitNoteTwoDivider.setSpacing(15);
            debitNoteDividerFirst.prefWidthProperty().bind(debitNoteDivider.widthProperty().multiply(0.79));
            debitNoteDividerSecond.prefWidthProperty().bind(debitNoteDivider.widthProperty().multiply(0.13));
            debitNoteDividerThird.prefWidthProperty().bind(debitNoteDivider.widthProperty().multiply(0.08));
            tblvDebitNoteSupplierDetails.getStylesheets().add(GenivisApplication.class.getResource("ui/css/purchase_challan_css/supplier_table_css/supplier_table_1401_1440.css").toExternalForm());
            bpDebitNoteRootPane.getStylesheets().add(GenivisApplication.class.getResource("ui/css/TranxCommonCssStyles/tranxCommonCssStyle5.css").toExternalForm());
        } else if (width >= 1441 && width <= 1680) {
            debitNoteTwoDivider.setSpacing(20);
            debitNoteDividerFirst.prefWidthProperty().bind(debitNoteDivider.widthProperty().multiply(0.82));
            debitNoteDividerSecond.prefWidthProperty().bind(debitNoteDivider.widthProperty().multiply(0.10));
            debitNoteDividerThird.prefWidthProperty().bind(debitNoteDivider.widthProperty().multiply(0.08));
            tblvDebitNoteSupplierDetails.getStylesheets().add(GenivisApplication.class.getResource("ui/css/purchase_challan_css/supplier_table_css/supplier_table_1441_1680.css").toExternalForm());
            bpDebitNoteRootPane.getStylesheets().add(GenivisApplication.class.getResource("ui/css/TranxCommonCssStyles/tranxCommonCssStyle6.css").toExternalForm());
        } else if (width >= 1681 && width <= 1920) {
            debitNoteTwoDivider.setSpacing(20);
            debitNoteDividerFirst.prefWidthProperty().bind(debitNoteDivider.widthProperty().multiply(0.84));
            debitNoteDividerSecond.prefWidthProperty().bind(debitNoteDivider.widthProperty().multiply(0.08));
            debitNoteDividerThird.prefWidthProperty().bind(debitNoteDivider.widthProperty().multiply(0.08));
            tblvDebitNoteSupplierDetails.getStylesheets().add(GenivisApplication.class.getResource("ui/css/invoice_product_history_table.css").toExternalForm());
            bpDebitNoteRootPane.getStylesheets().add(GenivisApplication.class.getResource("ui/css/TranxCommonCssStyles/tranxCommonCssStyle7.css").toExternalForm());
        }
    }

    private void initShortcutKeys() {
        //Enter,Tab Key Cursor Movement
        bpDebitNoteRootPane.addEventFilter(KeyEvent.KEY_PRESSED, (KeyEvent event) -> {

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
            }
            if (event.getCode() == KeyCode.S && event.isControlDown()) {
                btnDebitNoteSubmit.fire();
            } else if (event.getCode() == KeyCode.X && event.isControlDown()) {
                btnDebitNoteCancel.fire();
            } else if (event.getCode() == KeyCode.E && event.isControlDown()) {
                btnDebitNoteModify.fire();
            }
        });
    }

    private void onPageLoad() {
        tfDebitNoteCreateSrNo.requestFocus();
    }

    // Scene Initialization
    public void sceneInitilization() {
        bpDebitNoteRootPane.sceneProperty().addListener((observable, oldScene, newScene) -> {
            if (newScene != null && newScene.getWindow() instanceof Stage) {
                Communicator.stage = (Stage) newScene.getWindow();
            }
        });
    }

    //? To Redirect the PurChallan List page
    public void backToListModify() {
        AlertUtility.AlertConfirmation(AlertUtility.alertTypeConfirmation, LedgerMessageConsts.msgGoList, input -> {
            if (input == 1) {
                GlobalController.getInstance().addTabStatic(DEBIT_NOTE_LIST_SLUG, false);
            }
        });
    }

    //? Redirect To Create Payment Create Page
    public void backToList() {
        AlertUtility.AlertConfirmation(AlertUtility.alertTypeConfirmation, LedgerMessageConsts.msgConfirmationOnCancel + tfDebitNoteVoucherNo.getText(), input -> {
            if (input == 1) {
                clearAllFields();
            }
        });
    }

    private void clearAllFields() {
        tfDebitNoteTranxDate.setText("");
        tfDebitNoteNarration.setText("");

        lblDebitNoteTotalDebit.setText("0");
        lblDebitNoteTotalCredit.setText("0");

        //? clear Ledger Info
        lblDebitNoteGst.setText("");
        lblDebitNoteArea.setText("");
        lblDebitNoteBank.setText("");
        lblDebitNoteContactPerson.setText("");
        lblDebitNotetransport.setText("");
        lblDebitNoteCreditDays.setText("");
        lblDebitNoteFSSAI.setText("");
        lblDebitNoteLicenseNo.setText("");
        lblDebitNoteRoute.setText("");

        //? clear Product Info
        lblDebitNotePaymentMode.setText("");
        lblDebitNoteCheque.setText("");
        lblDebitNoteBankName.setText("");
        lblDebitNotePaymentDate.setText("");

        tblvDebitNoteTable.getItems().clear();// Add a new blank row if needed
        tblvDebitNoteTable.getItems().addAll(new PaymentRowDTO("", "Dr", "", "", ""));
        tfDebitNoteTranxDate.requestFocus();
        tblvDebitNoteSupplierDetails.getItems().clear();
    }

    // Auto Setting the Voucher Sr. No. and Voucher No.
    private void getDebitNtVoucherNumber() {
        APIClient apiClient = null;
        try {
            debitNoteLogger.debug("Get getDebitNtVoucherNumber Started...");
            apiClient = new APIClient(EndPoints.getCurrentDebitNtVoucherNo, "", RequestType.GET);
            apiClient.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
                @Override
                public void handle(WorkerStateEvent workerStateEvent) {
                    jsonObject = new Gson().fromJson(workerStateEvent.getSource().getValue().toString(), JsonObject.class);
                    if (jsonObject.get("responseStatus").getAsInt() == 200) {
                        tfDebitNoteCreateSrNo.setText(jsonObject.get("count").getAsString());
                        tfDebitNoteVoucherNo.setText(jsonObject.get("debitnoteNo").getAsString());
                    }
                }
            });
            apiClient.setOnCancelled(new EventHandler<WorkerStateEvent>() {
                @Override
                public void handle(WorkerStateEvent workerStateEvent) {
                    debitNoteLogger.error("Network API cancelled in getDebitNtVoucherNumber()" + workerStateEvent.getSource().getValue().toString());

                }
            });
            apiClient.setOnFailed(new EventHandler<WorkerStateEvent>() {
                @Override
                public void handle(WorkerStateEvent workerStateEvent) {
                    debitNoteLogger.error("Network API failed in getDebitNtVoucherNumber()" + workerStateEvent.getSource().getValue().toString());
                }
            });
            apiClient.start();
            debitNoteLogger.debug("Get getDebitNtVoucherNumber End...");
        } catch (Exception e) {
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            String exceptionAsString = sw.toString();
            debitNoteLogger.error("Exception in getVoucherNumber():" + exceptionAsString);
        }

    }

    // responsive Common Table
    public void responsiveCmpt() {
        tblcDebitNoteType.prefWidthProperty().bind(tblvDebitNoteTable.widthProperty().multiply(0.05));
        tblcDebitNoteParticulars.prefWidthProperty().bind(tblvDebitNoteTable.widthProperty().multiply(0.75));
        tblcDebitNoteDebit.prefWidthProperty().bind(tblvDebitNoteTable.widthProperty().multiply(0.10));
        tblcDebitNoteCredit.prefWidthProperty().bind(tblvDebitNoteTable.widthProperty().multiply(0.10));
    }

    // Table initialization
    private void tableInitialization() {

        TableCellCallback<String[]> callback = item -> {


            if (item[0].equals("true")) {
                lableInitialization();
            } else {
                lblDebitNoteGst.setText(item[0]);
                lblDebitNoteArea.setText(item[1]);
                lblDebitNoteBank.setText(item[2]);
                lblDebitNoteContactPerson.setText(item[3]);
                lblDebitNoteCreditDays.setText(item[5]);
                lblDebitNoteFSSAI.setText(item[6]);
                lblDebitNoteLicenseNo.setText(item[7]);
                lblDebitNoteRoute.setText(item[8]);
            }
        };

        tblvDebitNoteTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);
        tblvDebitNoteTable.setFocusTraversable(false);
        //Here also Using The PaymentRowDTO as the Fields are Common
        tblvDebitNoteTable.getItems().addAll(new PaymentRowDTO("", "Dr", "", "", ""));
        tblcDebitNoteType.setCellValueFactory(cellData -> cellData.getValue().typeProperty());
        tblcDebitNoteType.setCellFactory(column -> new ComboBoxTableCellForDebitNote("type"));

        tblcDebitNoteParticulars.setCellValueFactory(cellData -> cellData.getValue().particularsProperty());
        tblcDebitNoteParticulars.setCellFactory(column -> new TextFieldTableCellForDebitNote("particular", bpDebitNoteRootPane, callback));

        tblcDebitNoteDebit.setCellValueFactory(cellData -> cellData.getValue().debitProperty());
        tblcDebitNoteDebit.setCellFactory(column -> new TextFieldTableCellForDebitNote("debit", bpDebitNoteRootPane, callback));

        tblcDebitNoteCredit.setCellValueFactory(cellData -> cellData.getValue().creditProperty());
        tblcDebitNoteCredit.setCellFactory(column -> new TextFieldTableCellForDebitNote("credit", bpDebitNoteRootPane, callback));

    }

    // Total Amount Set Method
    private void lableInitialization() {
        List<PaymentRowDTO> tableList = tblvDebitNoteTable.getItems();
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
        lblDebitNoteTotalCredit.setText(String.valueOf(creditTotal));
        double debitTotal = GlobalTranx.TranxRoundDigit(totalDebit, GlobalTranx.configDecimalPlace);
        lblDebitNoteTotalDebit.setText(String.valueOf(debitTotal));

        if (totalCredit != totalDebit) {
            tblvDebitNoteTable.getItems().add(new PaymentRowDTO("", "Cr", "", "", ""));
        }

    }

    //Create Api Integration
    private void createDebitNote() {
        String btnText = btnDebitNoteSubmit.getText();
        AlertUtility.CustomCallback callback = number -> {
            if (number == 1) {
                APIClient apiClient = null;
                Double totalAmt = 0.0;
                try {
                    creditNoteLogger.debug("Get createDebitNote Started...");
                    Map<String, String> map = new HashMap<>();
                    if (btnText.equalsIgnoreCase("Update")) {
                        map.put("id", debitNoteEditId.toString());
                    }
                    map.put("voucher_debit_sr_no", tfDebitNoteCreateSrNo.getText());
                    map.put("voucher_debit_no", tfDebitNoteVoucherNo.getText());
                    map.put("total_amt", lblDebitNoteTotalCredit.getText());
                    map.put("narration", tfDebitNoteNarration.getText());
                    if (tfDebitNoteTranxDate != null && tfDebitNoteTranxDate.getText() != null && !tfDebitNoteTranxDate.getText().isEmpty()) {
                        map.put("transaction_dt", Communicator.text_to_date.fromString(tfDebitNoteTranxDate.getText()).toString());
                    }
                    ArrayList<BillByBillRowReqDTO> rowData = new ArrayList<>();
//            ObservableList<PaymentRowDTO> tableViewItems = tblvCreditNoteTable.getItems();
                    ObservableList<PaymentRowDTO> tableViewItems = tblvDebitNoteTable.getItems();
//            for (PaymentRowDTO paymentRowDTO : tableViewItems) {
//                BillByBillRowReqDTO billByBillRowReqDTO = new BillByBillRowReqDTO();
//                BillByBillPerticulars billByBillPerticulars = new BillByBillPerticulars();
//
//                /******************** Perticular Class Fields Start **************/
//                if (btnText.equalsIgnoreCase("Update")) {
//                    billByBillPerticulars.setDetailsId(paymentRowDTO.getDetailsId());
//                }
//                if (!paymentRowDTO.getParticulars().isEmpty()) {
//                    billByBillPerticulars.setId(paymentRowDTO.getLedgerId());
//                } else {
//                    billByBillPerticulars.setId(0L);
//                }
//                if (!paymentRowDTO.getParticulars().isEmpty()) {
//                    billByBillPerticulars.setType(paymentRowDTO.getLedgerType());
//                } else {
//                    billByBillPerticulars.setType("");
//                }
//                if (!paymentRowDTO.getParticulars().isEmpty()) {
//                    billByBillPerticulars.setLedgerName(paymentRowDTO.getLedgerName());
//                } else {
//                    billByBillPerticulars.setLedgerName("");
//                }
//                if (!paymentRowDTO.getParticulars().isEmpty()) {
//                    billByBillPerticulars.setBalancingMethod(paymentRowDTO.getBalancingMethod());
//                } else {
//                    billByBillPerticulars.setBalancingMethod("");
//                }
//
//                /******************** Perticular Class Fields End **************/
//
//                /******************** Main Row Class Fields Start **************/
//                if (!paymentRowDTO.getType().isEmpty()) {
//                    billByBillRowReqDTO.setType(paymentRowDTO.getType());
//                } else {
//                    billByBillRowReqDTO.setType("");
//                }
//                if (paymentRowDTO.getType().equalsIgnoreCase("Cr")) {
//                    billByBillRowReqDTO.setPaidAmt(Double.parseDouble(paymentRowDTO.getCredit()));
//                } else {
//                    billByBillRowReqDTO.setPaidAmt(Double.parseDouble(paymentRowDTO.getDebit()));
//                }
//                if (!paymentRowDTO.getParticulars().isEmpty()) {
//                    String data=new Gson().toJson(billByBillPerticulars);
//                    System.out.println("pay=>"+data);
//                    billByBillRowReqDTO.setPerticulars(billByBillPerticulars);
//                } else {
//                    billByBillRowReqDTO.setType("");
//                }
//                List<BillByBillRowReqDTO> bills = paymentRowDTO.getBillRowReqDTOS();
//                if (bills.size() > 0) {
//                    for (BillByBillRowReqDTO mBill : bills) {
//                        if (mBill.getPerticulars() != null) {
//                            billByBillRowReqDTO.setPerticulars(mBill.getPerticulars());
//                        }
//                    }
//                }
//                rowData.add(billByBillRowReqDTO);
//                /******************** Main Row Class Fields End **************/
//            }


                    for (PaymentRowDTO paymentRowDTO : tableViewItems) {
                        BillByBillRowReqDTO mRow = new BillByBillRowReqDTO();
                        if (!paymentRowDTO.getType().isEmpty()) {
                            mRow.setType(paymentRowDTO.getType());
                        } else {
                            mRow.setType("");
                        }
                        if (paymentRowDTO.getType().equalsIgnoreCase("Cr")) {
                            totalAmt = Double.parseDouble(paymentRowDTO.getCredit());
                            mRow.setPaidAmt(Double.parseDouble(paymentRowDTO.getCredit()));
                        } else {
                            mRow.setPaidAmt(Double.parseDouble(paymentRowDTO.getDebit()));
                        }
                        /**** Set the Particulars data ****/
                        List<BillByBillRowReqDTO> bills = paymentRowDTO.getBillRowReqDTOS();
                        System.out.println("Size of Rows onUpdate " + bills.size());
                        if (bills.size() > 0) {
                            for (BillByBillRowReqDTO mBill : bills) {
                                if (mBill.getPerticulars() != null) {
                                    System.out.println("Bill Rows DTO onUpdate" + mBill.getPerticulars().getDetailsId());
                                    if (btnText.equalsIgnoreCase("Update")) {
                                        mBill.getPerticulars().setDetailsId(paymentRowDTO.getDetailsId());
                                    } else {
                                        mBill.getPerticulars().setDetailsId(0L);
                                    }
                                    mRow.setPerticulars(mBill.getPerticulars());
                                }
                                if (paymentRowDTO.getType().equalsIgnoreCase("Cr")) {
                                    mRow.setPaymentType(mBill.getPaymentType());
                                    mRow.setTranxNo(mBill.getTranxNo());
                                    mRow.setBankName(mBill.getBankName());
                                    mRow.setPaymentDate(mBill.getPaymentDate());
                                }
                                rowData.add(mRow);
                            }
                        } else if (paymentRowDTO.getType().equalsIgnoreCase("Dr") &&
                                paymentRowDTO.getBalancingMethod().equalsIgnoreCase("on-account")) {
                            BillByBillPerticulars mParticular = new BillByBillPerticulars();
                            mParticular.setId(paymentRowDTO.getLedgerId());
                            mParticular.setType(paymentRowDTO.getType());
                            mParticular.setLedgerName(paymentRowDTO.getLedgerName());
                            mParticular.setBalancingMethod(paymentRowDTO.getBalancingMethod());
                            mRow.setPerticulars(mParticular);
                            rowData.add(mRow);
                        } else {
                            mRow.setBankName("Cash A/C");
                            mRow.setPaymentType("");
                            mRow.setTranxNo("");
                            BillByBillPerticulars mParticular = new BillByBillPerticulars();
                            mParticular.setId(paymentRowDTO.getLedgerId());
                            mParticular.setType("others");
                            mParticular.setLedgerName(paymentRowDTO.getLedgerName());
                            mRow.setPerticulars(mParticular);
                            rowData.add(mRow);
                        }
                    }


                    String mRowData = new Gson().toJson(rowData, new TypeToken<List<BillByBillRowReqDTO>>() {
                    }.getType());
                    System.out.println("hello >> : " + mRowData);
                    map.put("rows", mRowData);
                    String finalReq = Globals.mapToStringforFormData(map);
                    System.out.println("final Request >> : " + finalReq);
                    if (btnText.equalsIgnoreCase("Submit")) {
                        apiClient = new APIClient(EndPoints.createDebitNote, finalReq, RequestType.FORM_DATA);
                        //? HIGHLIGHT
                        DebitNoteListController.isNewDibitNoteCreated = true; //? Set the flag for new creation
                    } else {
                        apiClient = new APIClient(EndPoints.updateDebitNote, finalReq, RequestType.FORM_DATA);
                        //? HIGHLIGHT
                        DebitNoteListController.editedDebitNoteId = debitNoteEditId.toString(); //? Set the ID for editing
                    }

                    apiClient.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
                        @Override
                        public void handle(WorkerStateEvent workerStateEvent) {
                            jsonObject = new Gson().fromJson(workerStateEvent.getSource().getValue().toString(), JsonObject.class);
                            System.out.println("JsonObject >> :" + jsonObject);
                            message = jsonObject.get("message").getAsString();
                            int status = jsonObject.get("responseStatus").getAsInt();
//                    if (jsonObject.get("responseStatus").getAsInt() == 200) {
                            //Successfully Alert Popup After Edit and Redirect to List Page
//                        AlertUtility.CustomCallback callback = (number) -> {
//                            if (number == 1) {
//                                GlobalController.getInstance().addTabStatic(DEBIT_NOTE_LIST_SLUG, true);
//                            }
//                        };
//                        Stage stage2 = (Stage) bpDebitNoteRootPane.getScene().getWindow();
//                        if (status == 200) {
//                            AlertUtility.AlertSuccess(stage2, "Success", message, callback);
//                        } else {
//                            AlertUtility.AlertError(stage2, AlertUtility.alertTypeError, message, callback);
//                        }
//                    }
                            if (status == 200) {
                                AlertUtility.AlertSuccessTimeout(AlertUtility.alertTypeSuccess, message, input -> {
                                    GlobalController.getInstance().addTabStatic(DEBIT_NOTE_LIST_SLUG, false);
                                });
                            } else {
                                AlertUtility.AlertErrorTimeout(AlertUtility.alertTypeError, message, in -> {
                                    btnDebitNoteSubmit.requestFocus();
                                });
                            }

                        }
                    });
                    apiClient.setOnCancelled(new EventHandler<WorkerStateEvent>() {
                        @Override
                        public void handle(WorkerStateEvent workerStateEvent) {
                            creditNoteLogger.error("Network API cancelled in createDebitNote()" + workerStateEvent.getSource().getValue().toString());

                        }
                    });
                    apiClient.setOnFailed(new EventHandler<WorkerStateEvent>() {
                        @Override
                        public void handle(WorkerStateEvent workerStateEvent) {
                            creditNoteLogger.error("Network API failed in createDebitNote()" + workerStateEvent.getSource().getValue().toString());
                        }
                    });
                    apiClient.start();
                    creditNoteLogger.debug("Get createDebitNote End...");
                } catch (Exception e) {
                    StringWriter sw = new StringWriter();
                    e.printStackTrace(new PrintWriter(sw));
                    String exceptionAsString = sw.toString();
                    creditNoteLogger.error("Exception in getVoucherNumber():" + exceptionAsString);
                }
            } else {
                System.out.println("working!");
            }
        };
        if (btnText.equalsIgnoreCase("Submit")) {
            AlertUtility.AlertConfirmation(AlertUtility.alertTypeConfirmation, LedgerMessageConsts.msgConfirmationOnSubmit + tfDebitNoteVoucherNo.getText(), callback);
        } else {
            AlertUtility.AlertConfirmation(AlertUtility.alertTypeConfirmation, LedgerMessageConsts.msgConfirmationOnUpdate + tfDebitNoteVoucherNo.getText(), callback);
        }
    }


    public void setEditId(Integer InId) {
        debitNoteEditId = InId;
        System.out.println("DebitNoteEditID: " + debitNoteEditId);
        if (InId > -1) {
            setEditData();
        }
    }

    private void setEditData() {
        getDebitNoteById(debitNoteEditId);
    }

    private void getDebitNoteById(Integer debitNoteEditId) {
        APIClient apiClient = null;
        debitNoteLogger.debug("Get getDebitNoteById Started...");
        try {
            Map<String, String> body = new HashMap<>();
            body.put("debit_id", debitNoteEditId.toString());
            String formData = Globals.mapToStringforFormData(body);
            apiClient = new APIClient(EndPoints.getDebitNoteById, formData, RequestType.FORM_DATA);
            apiClient.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
                @Override
                public void handle(WorkerStateEvent workerStateEvent) {
                    String response = workerStateEvent.getSource().getValue().toString();
                    System.out.println("response >> : " + response);
                    DebitNoteByIdMainDTO debitNoteByIdMainDTO = new Gson().fromJson(response, DebitNoteByIdMainDTO.class);
                    if (debitNoteByIdMainDTO.getResponseStatus() == 200) {
                        debitNoteLogger.debug("Get getDebitNoteById Success...");
                        setDebitNoteFormData(debitNoteByIdMainDTO);
                    }
                }
            });

            apiClient.setOnFailed(new EventHandler<WorkerStateEvent>() {
                @Override
                public void handle(WorkerStateEvent workerStateEvent) {
                    debitNoteLogger.error("Network API cancelled in getDebitNoteById()" + workerStateEvent.getSource().getValue().toString());
                }
            });
            apiClient.setOnCancelled(new EventHandler<WorkerStateEvent>() {
                @Override
                public void handle(WorkerStateEvent workerStateEvent) {
                    debitNoteLogger.error("Network API cancelled in getDebitNoteById()" + workerStateEvent.getSource().getValue().toString());
                }
            });
            apiClient.start();
            debitNoteLogger.debug("Get getDebitNoteById End...");

        } catch (Exception e) {
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            String exceptionAsString = sw.toString();
            debitNoteLogger.error("Exception in getDebitNoteById():" + exceptionAsString);
        } finally {
            apiClient = null;
        }
    }

    private void setDebitNoteFormData(DebitNoteByIdMainDTO debitNoteByIdMainDTO) {
        debitNoteLogger.debug("Setting The Data in Form setDebitNoteFormData(): Start");
        btnDebitNoteSubmit.setText("Update");
        List<BillByBillRowReqDTO> billrowList = new ArrayList<>();
        String tranxDate = debitNoteByIdMainDTO.getTranxDate();
        if (tranxDate != null && tfDebitNoteTranxDate != null) {
            LocalDate applicable_date = LocalDate.parse(tranxDate);
            tfDebitNoteTranxDate.setText(applicable_date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        }
        tfDebitNoteCreateSrNo.setText(debitNoteByIdMainDTO.getDebitSrNo().toString());
        tfDebitNoteVoucherNo.setText(debitNoteByIdMainDTO.getDebitNo().toString());
        tfDebitNoteNarration.setText(debitNoteByIdMainDTO.getNarrations());
        lblDebitNoteTotalCredit.setText(debitNoteByIdMainDTO.getTotalAmt().toString());
        lblDebitNoteTotalDebit.setText(debitNoteByIdMainDTO.getTotalAmt().toString());

        int index = 0;
        tblvDebitNoteTable.getItems().clear();
        for (DebitNoteByIdRowDTO mRow : debitNoteByIdMainDTO.getDebitDetails()) {
            PaymentRowDTO row = new PaymentRowDTO();

            row.setDetailsId(mRow.getDetailsId());
            row.setType(mRow.getType());
            row.setParticulars(mRow.getLedgerName());
            row.setCredit(mRow.getCr().toString());
            row.setDebit(mRow.getDr().toString());
            row.setLedgerId(mRow.getLedgerId());
            row.setLedgerName(mRow.getLedgerName());
            row.setLedgerType(mRow.getLedgerType());
            row.setBalancingMethod(mRow.getBalancingMethod());

            BillByBillRowReqDTO billRowReqDTO = new BillByBillRowReqDTO();
            billRowReqDTO.setType(mRow.getType());
            if (mRow.getDr() != 0.0) {
                billRowReqDTO.setPaidAmt(mRow.getDr());
            } else {
                billRowReqDTO.setPaidAmt(mRow.getCr());
            }
//            if (mRow.getBankPaymentType() != null) billRowReqDTO.setPaymentType(mRow.getBankPaymentType().getLabel());
//            else {
//                billRowReqDTO.setPaymentType("");
//            }
            billRowReqDTO.setPaymentDate(mRow.getPaymentDate());
            billRowReqDTO.setTranxNo(mRow.getDebitnoteTranxNo());
//            billRowReqDTO.setBankName(mRow.getBankAccName());
            /**** setting Particular ****/
            BillByBillPerticulars mParticular = new BillByBillPerticulars();
            System.out.println("Particular Details Id--->" + mRow.getDetailsId());
            mParticular.setDetailsId(mRow.getDetailsId());
            mParticular.setId(mRow.getLedgerId());
            mParticular.setType(mRow.getLedgerType());
            mParticular.setLedgerName(mRow.getLedgerName());
            mParticular.setBalancingMethod(mRow.getBalancingMethod());
            mParticular.setPayableAmt(mRow.getPayableAmt());
            mParticular.setSelectedAmt(mRow.getSelectedAmt());
            mParticular.setRemainingAmt(mRow.getRemainingAmt());
            mParticular.setAdvanceCheck(mRow.getAdvanceCheck());
            mParticular.setBillids(mRow.getBills());
            /**** END ****/
            billRowReqDTO.setPerticulars(mParticular);
            billrowList.add(billRowReqDTO);
            row.setBillRowReqDTOS(billrowList);


            tblvDebitNoteTable.getItems().add(row);
            index++;
            debitNoteLogger.debug("Setting The Data in Form setDebitNoteFormData(): End");
        }


    }


}

class TextFieldTableCellForDebitNote extends TableCell<PaymentRowDTO, String> {
    private TextField textField;
    private String columnName;
    private ObservableList<TranxPaymentInvoiceDTO> mLedgerInvoiceList = FXCollections.observableArrayList();
    private ObservableList<TranxPaymentInvoiceDTO> mLedgerDebitNoteList = FXCollections.observableArrayList();
    private List<BillByBillId> billids = new ArrayList<>();
    private List<BillByBillId> billidsDn = new ArrayList<>();

    private BillByBillPerticulars billParticular = new BillByBillPerticulars();
    private List<BillByBillRowReqDTO> billRow = new ArrayList<>();
    public int tableIndex = -1;
    public Boolean isChecked = false;
    private Double totalBalance = 0.0;
    private Double totalBalanceDn = 0.0;
    private static Double totalPaidAmt = 0.0;
    private Double totalPaidAmtDn = 0.0;
    private Double totalBalanceAmt = 0.0;
    private Double totalBalanceAmtDn = 0.0;
    private Label lblPaidAmt = new Label();
    private Label lblBalance = new Label();
    private Label lblInvoiceBalance = new Label();
    private Label lblBalanceDN = new Label();
    private Label lblPaidAmtDN = new Label();
    private Label lblInvoiceBalanceDN = new Label();
    private Label lblInvoicePaidAmt = new Label();
    private Label lblSelectedAmt = new Label();
    private TextField tfSelectedAmt = new TextField();
    private final TableCellCallback<String[]> callback;
    private Stage stage = null;
    private static Double rowSelectedAmt = 0.0;
    private static Double rowInputAmt = 0.0;
    private TableView<TranxPaymentInvoiceDTO> tableView = null;
    private TableView<TranxPaymentInvoiceDTO> tableViewDN = null;

    public TextFieldTableCellForDebitNote(String columnName, Parent rootPane, TableCellCallback<String[]> callback) {

        stage = (Stage) rootPane.getScene().getWindow();
        this.columnName = columnName;
        this.textField = new TextField();
        /* this.textField.setOnAction(event -> commitEdit(textField.getText()));*/
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
        this.callback = callback;
        this.textField.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal) {
                textField.setStyle("-fx-background-color: #fff9c4; -fx-border-radius: 0px; -fx-border-width: 2; -fx-border-color: #00a0f5;");
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
            } else {
                textField.setStyle("-fx-background-color: #f6f6f9; -fx-border-radius: 0px; -fx-border-width: 2; -fx-border-color: #f6f6f9;");
            }
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

        textField.setStyle("-fx-background-color: #f6f6f9; -fx-border-radius: 0px; -fx-border-width: 1; -fx-border-color: #f6f6f9;");

        textField.setPrefHeight(38);
        textField.setMaxHeight(38);
        textField.setMinHeight(38);

        textField.focusedProperty().addListener((obs, oldVal, newVal) -> {
           /* if (newVal) {
                textField.setStyle("-fx-background-color: #fff9c4; -fx-border-radius: 0px; -fx-border-width: 2; -fx-border-color: #00a0f5;");
            } else {
                textField.setStyle("-fx-background-color: #f6f6f9; -fx-border-radius: 0px; -fx-border-width: 2; -fx-border-color: #f6f6f9;");
            }*/

        });
        textField.setOnKeyPressed(ev -> {
            if (ev.getCode() == KeyCode.SPACE) {
                Stage stage = (Stage) rootPane.getScene().getWindow();
                if (columnName.equalsIgnoreCase("particular")) {
                    PaymentRowDTO paymentRowDTO = getTableView().getItems().get(getIndex());
                    if (!paymentRowDTO.getType().isEmpty()) {
                        SingleInputDialogs.openLedgerPopUp(stage, "Filter", input -> {
                            paymentLogger.debug("Payment Ledger Popup opened");
                            String ledgerName = (String) input[0];
                            String ledgerId = (String) input[1];
                            String ledgerType = (String) input[4];
                            String balancingMethod = (String) input[5];
                            String underSlug = (String) input[6];
                            paymentLogger.debug("Payment Ledger Popup ---> Ledger Id->> " + ledgerId + " Ledger Type->> " + ledgerType + " Balancing Method->> " + balancingMethod);
                            paymentLogger.debug("Bill by Bill by Window --> Opened Ledger Bill Modal:On Clicking by " + "selecting the Ledger Name ");
                            textField.setText(ledgerName);
                            getTableView().getItems().get(getIndex()).setParticulars(ledgerName);
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
//                            selectedLedgerDataInfo(ledgerId);
//                            if (newRow.getType().equalsIgnoreCase("Dr") && underSlug.equalsIgnoreCase("sundry_creditors") && balancingMethod.equalsIgnoreCase("bill-by-bill")) {
//                                getLedgerPendingBills(stage, ledgerId, ledgerType, balancingMethod, ledgerName, underSlug);
//                            }

                            if (underSlug.equalsIgnoreCase("sundry_debtors") || underSlug.equalsIgnoreCase("sundry_creditors")) {
                                selectedLedgerDataInfo(ledgerId);
                            }

                            /**** getting Bills List from selected Table Row if Available--> Edit Case *****/
                            List<BillByBillRowReqDTO> mBillRow = newRow.getBillRowReqDTOS();
                            List<BillByBillId> selectedBills = new ArrayList<>();
                            System.out.println("Before Selected Bills--->:" + selectedBills.size());
                            if (mBillRow.size() > 0) {
                                for (BillByBillRowReqDTO mRowDTO : mBillRow) {
                                    if (mRowDTO.getPerticulars().getBillids() != null)
                                        selectedBills.addAll(mRowDTO.getPerticulars().getBillids());
                                }
                                System.out.println("After Selected Bills--->:" + selectedBills.size());
                            }
                            /**** SC bill by bill ****/
                            if (balancingMethod.equalsIgnoreCase("bill-by-bill")) {
                                getLedgerPendingBills(selectedBills, stage, ledgerId, ledgerType, balancingMethod, ledgerName, underSlug);
                            }
                            /**** SC On-account ****/
                            else {
                                getLedgerPendingBills(selectedBills, stage, ledgerId, ledgerType, balancingMethod, ledgerName, underSlug);
                            }

                        },in->{
                            System.out.println("In >> Called >. " + in);
                        });
                    }
                }
            }

        });
        textField.setOnMouseClicked(actionEvent -> {
            Stage stage = (Stage) rootPane.getScene().getWindow();
            if (columnName.equalsIgnoreCase("particular")) {
                PaymentRowDTO paymentRowDTO = getTableView().getItems().get(getIndex());
                if (!paymentRowDTO.getType().isEmpty()) {
                    SingleInputDialogs.openLedgerPopUp(stage, "Filter", input -> {
                        paymentLogger.debug("Payment Ledger Popup opened");
                        String ledgerName = (String) input[0];
                        String ledgerId = (String) input[1];
                        String ledgerType = (String) input[4];
                        String balancingMethod = (String) input[5];
                        String underSlug = (String) input[6];
                        paymentLogger.debug("Payment Ledger Popup ---> Ledger Id->> " + ledgerId + " Ledger Type->> " + ledgerType + " Balancing Method->> " + balancingMethod);
                        paymentLogger.debug("Bill by Bill by Window --> Opened Ledger Bill Modal:On Clicking by " + "selecting the Ledger Name ");
                        textField.setText(ledgerName);
                        getTableView().getItems().get(getIndex()).setParticulars(ledgerName);
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
//                        selectedLedgerDataInfo(ledgerId); // need to ask only CR or Both
//                        if (newRow.getType().equalsIgnoreCase("Dr") && underSlug.equalsIgnoreCase("sundry_creditors") && balancingMethod.equalsIgnoreCase("bill-by-bill")) {
//                            getLedgerPendingBills(stage, ledgerId, ledgerType, balancingMethod, ledgerName, underSlug);
//                        }
                        if (underSlug.equalsIgnoreCase("sundry_debtors") || underSlug.equalsIgnoreCase("sundry_creditors")) {
                            selectedLedgerDataInfo(ledgerId);
                        }

                        /**** getting Bills List from selected Table Row if Available--> Edit Case *****/
                        List<BillByBillRowReqDTO> mBillRow = newRow.getBillRowReqDTOS();
                        List<BillByBillId> selectedBills = new ArrayList<>();
                        System.out.println("Before Selected Bills--->:" + selectedBills.size());
                        if (mBillRow.size() > 0) {
                            for (BillByBillRowReqDTO mRowDTO : mBillRow) {
                                if (mRowDTO.getPerticulars().getBillids() != null)
                                    selectedBills.addAll(mRowDTO.getPerticulars().getBillids());
                            }
                            System.out.println("After Selected Bills--->:" + selectedBills.size());
                        }
                        /**** SC bill by bill ****/
                        if (balancingMethod.equalsIgnoreCase("bill-by-bill")) {
                            getLedgerPendingBills(selectedBills, stage, ledgerId, ledgerType, balancingMethod, ledgerName, underSlug);
                        }
                        /**** SC On-account ****/
                        else {
                            getLedgerPendingBills(selectedBills, stage, ledgerId, ledgerType, balancingMethod, ledgerName, underSlug);
                        }
                    },in->{
                        System.out.println("In >> Called >. " + in);
                    });
                }
            }
        });

    }

    private void selectedLedgerDataInfo(String ledgerId) {
        Map<String, String> body = new HashMap<>();
        body.put("ledger_id", ledgerId);
        String requestBody = Globals.mapToStringforFormData(body);
        APIClient apiClient = null;
        apiClient = new APIClient(EndPoints.TRANSACTION_LEDGER_DETAILS, requestBody, RequestType.FORM_DATA);
        apiClient.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent workerStateEvent) {
                String res = workerStateEvent.getSource().getValue().toString();
                JsonObject jsonObject = new Gson().fromJson(res, JsonObject.class);
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
        });
        apiClient.setOnFailed(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent workerStateEvent) {

            }
        });
        apiClient.setOnCancelled(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent workerStateEvent) {

            }
        });
        apiClient.start();

    }

    private void getLedgerPendingBills(List<BillByBillId> selectedBills, Stage stage, String ledgerId, String ledgerType, String balancingMethod, String ledgerName, String underSlug) {
        /**** SC Bill by Bill ****/
        if (balancingMethod.equalsIgnoreCase("bill-by-bill")) {
            openLedgerPendingInvoiceList(selectedBills, stage, "Bill By Bill", ledgerId, ledgerType, balancingMethod, ledgerName, (input1, input2) -> {
                billParticular = input1;
                String mType = "";
                BillByBillRowReqDTO row = null;
                if (underSlug.equalsIgnoreCase("sundry_creditors")) {
                    if (input1.getType().equalsIgnoreCase("SC")) mType = "dr";
                    else mType = "cr";
                    getTableView().getItems().get(getIndex()).setDebit("" + input1.getSelectedAmt());
                    rowSelectedAmt = input1.getSelectedAmt();
                    rowInputAmt = input1.getSelectedAmt();
                }
                row = new BillByBillRowReqDTO(mType, input1.getSelectedAmt(), input1, "", "", "", "");
                billRow.add(row);
                getTableView().getItems().get(getIndex()).setBillRowReqDTOS(billRow);
                System.out.println("selected Bill Size--->" + selectedBills.size());
                if (selectedBills.size() == 0)
                    lableInitializationBillByBill();
            });
        } else {
            if (getIndex() != 0 && (balancingMethod.equalsIgnoreCase("on-account") || (balancingMethod.isEmpty()))) {
                rowSelectedAmt = Double.parseDouble(getTableView().getItems().get(getIndex() - 1).getDebit());
                getTableView().getItems().get(getIndex()).setCredit("" + rowSelectedAmt);
                lableInitializationBillByBill();
            }
        }
    }

    private void lableInitializationBillByBill() {
        List<PaymentRowDTO> tableList = getTableView().getItems();
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
        if (totalCredit != totalDebit) {
            getTableView().getItems().add(new PaymentRowDTO("", "Cr", "", "", ""));
        }
    }


    public <T> void openLedgerPendingInvoiceList(List<BillByBillId> selectedBills, Stage stage, String title, String ledgerId, String ledgerType, String balancingMethod, String ledgerName, BiConsumer<BillByBillPerticulars, List<BillByBillId>> callback) {
        BillByBillPerticulars particular = new BillByBillPerticulars();
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
        lblSelectedAmt = new Label("Selected Amt");
        lblSelectedAmt.setStyle("-fx-font-size: 15; -fx-text-fill: #404040;-fx-font-weight: bold;");
        HBox.setMargin(lblSelectedAmt, new Insets(0, 0, 0, 15));
        tfSelectedAmt = new TextField();
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
        Platform.runLater(() -> tfPayableAmt.requestFocus());
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
            /**** if user clears the date filter then ,call fetch invoice api to reset date filter and fetch all the invoices ****/
            fetchInvoiceList(ledgerId, ledgerType, balancingMethod, "", "", selectedBills);
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
        tableView = new TableView();
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        String tableStyle = ".table-row-cell:selected {-fx-background-color: #E1E0E0;-fx-text-background-color:black;}";
        tableView.setStyle(tableStyle);
        tableView.setEditable(true);
        tableView.setPrefHeight(170);
        tableView.setMaxHeight(170);
        tableView.setMinHeight(170);
        TableColumn<TranxPaymentInvoiceDTO, Boolean> tcSelect = new TableColumn<>("Select");
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
        tableView.getColumns().addAll(tcSelect, tcInvoiceNo, tcInvoiceAmt, tcType, tcBillDate, tcBalance, tcDueDate, tcDay, tcPaidAmt, tcBalanceAmt);
        //       tcSelect.setCellValueFactory(cellData -> cellData.getValue().is_row_selectedProperty());
        tcSelect.setCellFactory(CheckBoxTableCell.forTableColumn(tcSelect));
        tcSelect.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<TranxPaymentInvoiceDTO, Boolean>, ObservableValue<Boolean>>() {
            @Override
            public ObservableValue<Boolean> call(TableColumn.CellDataFeatures<TranxPaymentInvoiceDTO, Boolean> param) {
                TranxPaymentInvoiceDTO mPayment = param.getValue();
                SimpleBooleanProperty booleanProperty = new SimpleBooleanProperty(mPayment.isIs_row_selected());
                booleanProperty.addListener(new ChangeListener<Boolean>() {
                    @Override
                    public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                        mPayment.setIs_row_selected(newValue);
                        if (newValue) {
                            mPayment.setPaidAmt(mPayment.getBalance());
                            mPayment.setBalanceAmt("0.00");
                            BillByBillId bill = new BillByBillId();
                            bill.setInvoiceId(Long.parseLong(mPayment.getId()));
                            bill.setAmount(Double.parseDouble(mPayment.getBalance()));
                            bill.setTotalAmt(Double.parseDouble(mPayment.getBalance()));
                            bill.setInvoiceNo(mPayment.getInvoiceNo());
                            bill.setLedgerId(Long.parseLong(ledgerId));
                            bill.setDueDays(Integer.parseInt(mPayment.getDays()));
                            bill.setBalancingType(mPayment.getType());
                            bill.setPaidAmt(Double.parseDouble(mPayment.getPaidAmt()));
                            bill.setBillDetailsId(0L);
//                            bill.setBillDetailsId(mPayment.getBillDetailsId());
                            bill.setInvoiceDate(mPayment.getBilldate());
                            bill.setInvoiceDate(mPayment.getBilldate());
                            bill.setRemainingAmt(Double.parseDouble(mPayment.getBalance()) - Double.parseDouble(mPayment.getPaidAmt()));
                            bill.setInvoiceUniqueId("pur_invoice," + mPayment.getId());
                            bill.setSource("pur_invoice");
                            billids.add(bill);
                        } else {
                            mPayment.setPaidAmt("0.00");
                            mPayment.setBalanceAmt(mPayment.getBalance());
                            billids.remove(mPayment);
                        }
                        calculateInvoicePaidAmt(tableView, "pi", ledgerId);
                    }

                });
                return booleanProperty;
            }

        });
        tcInvoiceNo.setCellValueFactory(data -> data.getValue().invoiceNoProperty());
        tcInvoiceAmt.setCellValueFactory(data -> data.getValue().invoiceAmtProperty());
        tcType.setCellValueFactory(data -> data.getValue().typeProperty());
        tcBillDate.setCellValueFactory(data -> data.getValue().billdateProperty());
        tcBalance.setCellValueFactory(data -> data.getValue().balanceProperty());
        tcDueDate.setCellValueFactory(data -> data.getValue().dueDateProperty());
        tcDay.setCellValueFactory(data -> data.getValue().daysProperty());
        tcPaidAmt.setCellValueFactory(data -> data.getValue().paidAmtProperty());
        tcBalanceAmt.setCellValueFactory(data -> data.getValue().balanceAmtProperty());

        /**** END TableView Invoice List****/

        /**** Setting up all Component for Hbox2 ****/
        Label lblInvoiceTotal = new Label("Total");
        lblInvoiceTotal.setMaxWidth(570);
        lblInvoiceTotal.setMinWidth(570);
        lblInvoiceTotal.setPrefWidth(570);
        lblInvoiceTotal.setStyle("-fx-font-size: 15; -fx-text-fill: #404040; -fx-font-weight: bold;");
        HBox.setMargin(lblInvoiceTotal, new Insets(0, 0, 0, 5));

        lblBalance = new Label("0.00");
        lblBalance.setStyle("-fx-font-size: 15; -fx-text-fill: #404040; -fx-font-weight: bold;");
        lblBalance.setMaxWidth(290);
        lblBalance.setMinWidth(290);
        lblBalance.setPrefWidth(290);

        lblPaidAmt = new Label("0.00");
        //lblPaidAmt.textProperty().bind(new SimpleObjectProperty<>(totalPaidAmt.toString()));

        lblPaidAmt.setStyle("-fx-font-size: 15; -fx-text-fill: #404040; -fx-font-weight: bold;");
        lblPaidAmt.setMaxWidth(150);
        lblPaidAmt.setMinWidth(150);
        lblPaidAmt.setPrefWidth(150);

        lblInvoiceBalance = new Label("0.00");
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
        tableViewDN = new TableView();
        tableViewDN.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tableViewDN.setPrefHeight(155);
        tableViewDN.setMaxHeight(155);
        tableViewDN.setMinHeight(155);
        tableViewDN.setEditable(true);
//        TableColumn<TranxPaymentInvoiceDTO, CheckBox> tcSelectDN = new TableColumn<>("Select");
        TableColumn<TranxPaymentInvoiceDTO, Boolean> tcSelectDN = new TableColumn<>("Select");
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

        tableViewDN.getColumns().addAll(tcSelectDN, tcInvoiceNoDN, tcInvoiceAmtDN, tcTypeDN, tcBillDateDN, tcBalanceDN, tcDueDateDN, tcDayDN, tcPaidAmtDN, tcBalanceAmtDN);

        tcSelectDN.setCellValueFactory(cellData -> cellData.getValue().is_row_selectedProperty());
        tcSelectDN.setCellFactory(cellData -> new CheckBoxTableCellForPayment("Select"));
        tcSelectDN.setCellFactory(CheckBoxTableCell.forTableColumn(tcSelect));
        tcSelectDN.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<TranxPaymentInvoiceDTO, Boolean>, ObservableValue<Boolean>>() {
            @Override
            public ObservableValue<Boolean> call(TableColumn.CellDataFeatures<TranxPaymentInvoiceDTO, Boolean> param) {
                TranxPaymentInvoiceDTO mPayment = param.getValue();
                SimpleBooleanProperty booleanProperty = new SimpleBooleanProperty(mPayment.isIs_row_selected());
                booleanProperty.addListener(new ChangeListener<Boolean>() {
                    @Override
                    public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                        mPayment.setIs_row_selected(newValue);
                        if (newValue) {
                            mPayment.setPaidAmt(mPayment.getBalance());
                            mPayment.setBalanceAmt("0.00");
                            BillByBillId bill = new BillByBillId();
                            bill.setInvoiceId(Long.parseLong(mPayment.getId()));
                            bill.setAmount(Double.parseDouble(mPayment.getBalance()));
                            bill.setTotalAmt(Double.parseDouble(mPayment.getBalance()));
                            bill.setInvoiceNo(mPayment.getInvoiceNo());
                            bill.setLedgerId(Long.parseLong(ledgerId));
                            bill.setDueDays(Integer.parseInt(mPayment.getDays()));
                            bill.setBalancingType(mPayment.getType());
                            bill.setPaidAmt(Double.parseDouble(mPayment.getPaidAmt()));
                            bill.setBillDetailsId(0L);
//                            bill.setBillDetailsId(mPayment.getBillDetailsId());
                            bill.setInvoiceDate(mPayment.getBilldate());
                            bill.setRemainingAmt(Double.parseDouble(mPayment.getBalance()) - Double.parseDouble(mPayment.getPaidAmt()));
                            bill.setInvoiceUniqueId("debit_note," + mPayment.getId());
                            bill.setSource("debit_note");
                            billidsDn.add(bill);
                        } else {
                            mPayment.setPaidAmt("0.00");
                            mPayment.setBalanceAmt(mPayment.getBalance());
                            billidsDn.remove(mPayment);
                        }
                        calculateInvoicePaidAmt(tableViewDN, "dn", ledgerId);

                    }

                });
                return booleanProperty;
            }

        });
        tcInvoiceNoDN.setCellValueFactory(data -> data.getValue().invoiceNoProperty());
        tcInvoiceAmtDN.setCellValueFactory(data -> data.getValue().invoiceAmtProperty());
        tcTypeDN.setCellValueFactory(data -> data.getValue().typeProperty());
        tcBillDateDN.setCellValueFactory(data -> data.getValue().billdateProperty());
        tcBalanceDN.setCellValueFactory(data -> data.getValue().balanceProperty());
        tcDueDateDN.setCellValueFactory(data -> data.getValue().dueDateProperty());
        tcDayDN.setCellValueFactory(data -> data.getValue().daysProperty());
        tcPaidAmtDN.setCellValueFactory(data -> data.getValue().paidAmtProperty());
        tcBalanceAmtDN.setCellValueFactory(data -> data.getValue().balanceAmtProperty());


        /**** Setting up all Component for Hbox4 ****/
        Label lblInvoiceTotalDN = new Label("Total");
        lblInvoiceTotalDN.setMaxWidth(570);
        lblInvoiceTotalDN.setMinWidth(570);
        lblInvoiceTotalDN.setPrefWidth(570);
        lblInvoiceTotalDN.setStyle("-fx-font-size: 15; -fx-text-fill: #404040; -fx-font-weight: bold;");
        HBox.setMargin(lblInvoiceTotalDN, new Insets(0, 0, 0, 5));

        lblBalanceDN = new Label("0.00");
        lblBalanceDN.setStyle("-fx-font-size: 15; -fx-text-fill: #404040; -fx-font-weight: bold;");
        lblBalanceDN.setMaxWidth(290);
        lblBalanceDN.setMinWidth(290);
        lblBalanceDN.setPrefWidth(290);

        lblPaidAmtDN = new Label("0.00");
        lblPaidAmtDN.setStyle("-fx-font-size: 15; -fx-text-fill: #404040; -fx-font-weight: bold;");
        lblPaidAmtDN.setMaxWidth(150);
        lblPaidAmtDN.setMinWidth(150);
        lblPaidAmtDN.setPrefWidth(150);

        lblInvoiceBalanceDN = new Label("0.00");
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
        Platform.runLater(() -> {
            fetchInvoiceList(ledgerId, ledgerType, balancingMethod, "", "", selectedBills);
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
        lblInvoicePaidAmt = new Label("0.00");
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

//        final String IDLE_BUTTON_STYLE = "-fx-padding: 5px 14px;-fx-font-family: \"Inter\"; sans-serif;" + "-fx-pref-width: 90px;-fx-pref-height: 34px;-fx-margin-left: 10px;-fx-font-size: 14px;" + "-fx-text-fill: white;-fx-background-color: #21c78a; -fx-font-weight: bold;-fx-background-radius: 4px;" + "-fx-border-radius: 4px;";
//        final String HOVERED_BUTTON_STYLE = "-fx-padding: 5px 14px;-fx-font-family: \"Inter\", sans-serif;" + "-fx-pref-width: 90px;-fx-pref-height: 34px;-fx-margin-left: 10px;-fx-font-size: 14px;" + "-fx-text-fill: white;-fx-border-color: #20a574; -fx-background-color: #20a574;-fx-cursor: hand;" + "-fx-font-weight: bold;-fx-background-radius: 4px" + "-fx-border-radius: 4px;";
//        submit.setStyle(IDLE_BUTTON_STYLE);
//        submit.setOnMouseExited(e -> submit.setStyle(IDLE_BUTTON_STYLE));
//        submit.setOnMouseEntered(e -> submit.setStyle(HOVERED_BUTTON_STYLE));

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
                if (tfFromDt != null && tfFromDt.getText() != null && !tfFromDt.getText().isEmpty()) {
                    fromDate = Communicator.text_to_date.fromString(tfFromDt.getText()).toString();
                }
                if (tfToDt != null && tfToDt.getText() != null && !tfToDt.getText().isEmpty()) {
                    toDate = Communicator.text_to_date.fromString(tfToDt.getText()).toString();
                }
                if (!fromDate.equalsIgnoreCase("") && !toDate.equalsIgnoreCase("")) {
                    fetchInvoiceList(ledgerId, ledgerType, balancingMethod, fromDate, toDate, selectedBills);
                } else {
//                    AlertUtility.AlertDialogForError("Warning", "Enter From and To Date ", input -> {
//                        paymentLogger.debug("Missing From Date and To Date in Pending Bill Popup Payment Tranx");
//                    });
                    AlertUtility.AlertErrorTimeout(AlertUtility.alertTypeError, "Enter From and To Date ", in -> {
                        paymentLogger.debug("Missing From Date and To Date in Pending Bill Popup Debit Note Tranx");
                    });
                }
            }
        });
        submit.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                List<BillByBillId> mergedList = new ArrayList<>();
                System.out.println("Bill ID---->" + billids.size());
                if (billids.size() > 0) {
                    mergedList.addAll(billids);
                }
                System.out.println("Bill IDDN---->" + billidsDn.size());
                if (billidsDn.size() > 0) {
                    mergedList.addAll(billidsDn);
                }
                particular.setId(Long.parseLong(ledgerId));
                particular.setType(ledgerType);
                particular.setLedgerName(ledgerName);
                particular.setBalancingMethod(balancingMethod);
                if (tfPayableAmt.getText().equalsIgnoreCase("")) {
                    particular.setPayableAmt(0.0);
                } else {
                    particular.setPayableAmt(Double.parseDouble(tfPayableAmt.getText()));
                }
                particular.setSelectedAmt(Double.parseDouble(tfSelectedAmt.getText()));
                particular.setRemainingAmt(0.0);
                particular.setAdvanceCheck(cbAdvanceAmt.isSelected());
                particular.setBillids(mergedList);
                callback.accept(particular, mergedList);
                primaryStage.close();
                OverlaysEffect.removeOverlaysEffect(stage);
            }
        });
        System.out.println("Selected Size in Table Row--->" + selectedBills.size());
        tableView.setItems(mLedgerInvoiceList);
        tableViewDN.setItems(mLedgerDebitNoteList);
    }

    private void calculateInvoicePaidAmt(TableView<TranxPaymentInvoiceDTO> tableView, String key, String ledgerId) {
        totalPaidAmt = 0.0;
        totalPaidAmtDn = 0.0;
        double totalbalunselected = 0.0;
        double totalbalunselectedDn = 0.0;
        Double totalInvoice = 0.0;
        for (TranxPaymentInvoiceDTO mList : tableView.getItems()) {
            if (mList.isIs_row_selected()) {
              /*  BillByBillId bill = new BillByBillId();
                bill.setInvoiceId(Long.parseLong(mList.getId()));
                bill.setAmount(Double.parseDouble(mList.getBalance()));
                bill.setTotalAmt(Double.parseDouble(mList.getBalance()));
                bill.setInvoiceNo(mList.getInvoiceNo());
                bill.setLedgerId(Long.parseLong(ledgerId));
                bill.setDueDays(Integer.parseInt(mList.getDays()));
                bill.setBalancingType(mList.getType());
                bill.setPaidAmt(Double.parseDouble(mList.getPaidAmt()));
                bill.setBillDetailsId(0L);
                bill.setInvoiceDate(mList.getBilldate());
                bill.setRemainingAmt(Double.parseDouble(mList.getBalance()) - Double.parseDouble(mList.getPaidAmt()));*/
                if (key.equalsIgnoreCase("pi")) {
                    totalPaidAmt += Double.parseDouble(mList.getPaidAmt());
                  /*  bill.setInvoiceUniqueId("pur_invoice," + mList.getId());
                    bill.setSource("pur_invoice");
                    billids.add(bill);*/
                } else {
                    totalPaidAmtDn += Double.parseDouble(mList.getPaidAmt());
                  /*  bill.setInvoiceUniqueId("debit_note," + mList.getId());
                    bill.setSource("debit_note");
                    billidsDn.add(bill);*/
                   /* System.out.println("Invoice Id--->" + bill.getInvoiceId());
                    System.out.println("Amt -->" + bill.getAmount());
                    System.out.println("Total Amt -->" + bill.getTotalAmt());
                    System.out.println("Invoice No -->" + bill.getInvoiceNo());
                    System.out.println("Ledger Id -->" + bill.getLedgerId());
                    System.out.println("Due Days -->" + bill.getDueDays());
                    System.out.println("PaidAmt-->" + bill.getPaidAmt());
                    System.out.println("Invoice Date-->" + bill.getInvoiceDate());
                    System.out.println("Remaining amt-->" + bill.getRemainingAmt());
                    System.out.println("Invoice Unique No-->" + bill.getInvoiceUniqueId());
                    System.out.println("Source-->" + bill.getSource());

                    System.out.println("Size of BillDn Ids-->" + billidsDn.size());
                    System.out.println("----------------------------------------------------------");*/
                }
            } else {
                if (key.equalsIgnoreCase("pi")) totalbalunselected += Double.parseDouble(mList.getBalance());
                else totalbalunselectedDn += Double.parseDouble(mList.getBalance());
            }

        }
        if (key.equalsIgnoreCase("pi")) {
            lblPaidAmt.setText(totalPaidAmt.toString());
            lblInvoiceBalance.setText("" + totalbalunselected);
        } else {
            lblPaidAmtDN.setText(totalPaidAmtDn.toString());
            lblInvoiceBalanceDN.setText("" + totalbalunselectedDn);
        }
        totalInvoice = (Double.parseDouble(lblPaidAmt.getText()) - Double.parseDouble(lblPaidAmtDN.getText()));
        lblInvoicePaidAmt.setText("" + totalInvoice);
        tfSelectedAmt.setText("" + totalInvoice);
    }


    private void sceneInitilization(BorderPane borderPane) {
        borderPane.sceneProperty().addListener((observable, oldScene, newScene) -> {
            if (newScene != null && newScene.getWindow() instanceof Stage) {
                Communicator.stage = (Stage) newScene.getWindow();
            }
        });
    }

    private void fetchInvoiceList(String ledgerId, String ledgerType, String balancingMethod, String fromDate,
                                  String toDate, List<BillByBillId> selectedBills) {
        mLedgerInvoiceList.clear();
        mLedgerDebitNoteList.clear();
        APIClient apiClient = null;
        System.out.println("Ledger Type->" + ledgerType);
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
            apiClient = new APIClient(EndPoints.GET_CR_PENDING_BILLS, reqData, RequestType.FORM_DATA);
            apiClient.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
                @Override
                public void handle(WorkerStateEvent workerStateEvent) {
                    String response = workerStateEvent.getSource().getValue().toString();
                    LedgerPendingBillResDTO pendingBillResDTO = new Gson().fromJson(response, LedgerPendingBillResDTO.class);
                    if (pendingBillResDTO.getResponseStatus() == 200) {
                        totalBalance = 0.0;
                        totalBalanceDn = 0.0;
                        totalBalanceAmtDn = 0.0;
                        totalBalanceAmt = 0.0;
                        for (InvoiceListResDTO mList : pendingBillResDTO.getList()) {
                            TranxPaymentInvoiceDTO invoiceData = new TranxPaymentInvoiceDTO("" + mList.getInvoiceId(), mList.getInvoiceNo(), "" + mList.getAmount(), mList.getBalancingType(), mList.getInvoiceDate(), "" + mList.getAmount(), "", mList.getDueDays() != null ? mList.getDueDays().toString() : "0", "0.00", "" + mList.getAmount(), mList.getSource());

                            if (mList.getSource().equalsIgnoreCase("pur_invoice")) {
                                mLedgerInvoiceList.add(invoiceData);
                                totalBalance = totalBalance + mList.getAmount();
                                totalBalanceAmt = totalBalance;
                            } else {
                                mLedgerDebitNoteList.add(invoiceData);
                                totalBalanceDn = totalBalanceDn + mList.getAmount();
                                totalBalanceAmtDn = totalBalanceDn;
                            }
                        }
                        lblBalance.setText("" + totalBalance);
                        lblBalanceDN.setText("" + totalBalanceDn);
                        lblInvoiceBalance.setText("" + totalBalanceAmt);
                        lblInvoiceBalanceDN.setText("" + totalBalanceAmtDn);
                        /***** Update case *****/
                        if (selectedBills.size() > 0) {
                            for (BillByBillId mSinglebill : selectedBills) {
                                System.out.println("Selected Ledger ID-->" + ledgerId + " DB Ledger Id:" + mSinglebill.getLedgerId());
                                if (ledgerId.equalsIgnoreCase(mSinglebill.getLedgerId().toString())) {
                                    if (mSinglebill.getSource().equalsIgnoreCase("pur_invoice")) {
                                        System.out.println("Before Size of mLedgerInvoiceList--->" + mLedgerInvoiceList.size());
                                        System.out.println("Invoice ID and No--->" + mSinglebill.getInvoiceId() + " " + mSinglebill.getInvoiceNo());
                                        TranxPaymentInvoiceDTO mTableRow = new TranxPaymentInvoiceDTO(
                                                mSinglebill.getBillDetailsId().toString(), mSinglebill.getInvoiceNo(),
                                                mSinglebill.getTotalAmt().toString(), mSinglebill.getBalancingType(),
                                                mSinglebill.getInvoiceDate(), mSinglebill.getAmount().toString(), "", "0",
                                                mSinglebill.getPaidAmt().toString(), mSinglebill.getRemainingAmt().toString(),
                                                "pur_invoice");
                                        mTableRow.setIs_row_selected(true);
                                        mLedgerInvoiceList.add(mTableRow);
                                        BillByBillId bill = new BillByBillId();
                                        bill.setInvoiceId(mSinglebill.getInvoiceId());
                                        bill.setAmount(mSinglebill.getAmount());
                                        bill.setTotalAmt(mSinglebill.getTotalAmt());
                                        bill.setInvoiceNo(mSinglebill.getInvoiceNo());
                                        bill.setLedgerId(mSinglebill.getLedgerId());
                                        bill.setDueDays(mSinglebill.getDueDays());
                                        bill.setBalancingType(mSinglebill.getBalancingType());
                                        bill.setPaidAmt(mSinglebill.getPaidAmt());
                                        bill.setBillDetailsId(mSinglebill.getBillDetailsId());
                                        bill.setInvoiceDate(mSinglebill.getInvoiceDate());
                                        bill.setRemainingAmt(mSinglebill.getRemainingAmt());
                                        bill.setInvoiceUniqueId("pur_invoice," + mSinglebill.getInvoiceId());
                                        bill.setSource("pur_invoice");
                                        billids.add(bill);
                                        calculateInvoicePaidAmt(tableView, "pi", ledgerId);
                                    } else if (mSinglebill.getSource().equalsIgnoreCase("debit_note")) {
                                        TranxPaymentInvoiceDTO mTableRow = new TranxPaymentInvoiceDTO(
                                                mSinglebill.getBillDetailsId().toString(), mSinglebill.getInvoiceNo(),
                                                mSinglebill.getTotalAmt().toString(), mSinglebill.getBalancingType(),
                                                mSinglebill.getInvoiceDate(), mSinglebill.getAmount().toString(), "", "0",
                                                mSinglebill.getPaidAmt().toString(), mSinglebill.getRemainingAmt().toString(),
                                                "debit_note");
                                        mTableRow.setIs_row_selected(true);
                                        mLedgerDebitNoteList.add(mTableRow);
                                        BillByBillId billDn = new BillByBillId();
                                        billDn.setInvoiceId(mSinglebill.getInvoiceId());
                                        billDn.setAmount(mSinglebill.getAmount());
                                        billDn.setTotalAmt(mSinglebill.getTotalAmt());
                                        billDn.setInvoiceNo(mSinglebill.getInvoiceNo());
                                        billDn.setLedgerId(mSinglebill.getLedgerId());
                                        billDn.setDueDays(mSinglebill.getDueDays());
                                        billDn.setBalancingType(mSinglebill.getBalancingType());
                                        billDn.setPaidAmt(mSinglebill.getPaidAmt());
                                        billDn.setBillDetailsId(mSinglebill.getBillDetailsId());
                                        billDn.setInvoiceDate(mSinglebill.getInvoiceDate());
                                        billDn.setRemainingAmt(mSinglebill.getRemainingAmt());
                                        billDn.setInvoiceUniqueId("debit_note," + mSinglebill.getInvoiceId());
                                        billDn.setSource("debit_note");
                                        billidsDn.add(billDn);
                                        calculateInvoicePaidAmt(tableViewDN, "dn", ledgerId);
                                    }
                                }
                            }
                            /**** END Update Case ****/
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

    @Override
    public void startEdit() {
        super.startEdit();
        setText((String) getItem());
        setGraphic(new VBox(textField));
        //textField.requestFocus();
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
//            ((PaymentRowDTO) getTableRow().getItem()).setParticulars(newValue.isEmpty() ? "" : newValue);
        } else if (columnName.equalsIgnoreCase("debit")) {
            ((PaymentRowDTO) getTableRow().getItem()).setDebit(newValue.isEmpty() ? "00" : newValue);
        } else if (columnName.equalsIgnoreCase("credit")) {
            ((PaymentRowDTO) getTableRow().getItem()).setCredit(newValue.isEmpty() ? "" : newValue);
        }
    }


}

class ComboBoxTableCellForDebitNote extends TableCell<PaymentRowDTO, String> {

    private ComboBox<String> comboBox = null;
    private final String columnName;

    private ObservableList<String> comboList = FXCollections.observableArrayList("Dr", "Cr");

    public ComboBoxTableCellForDebitNote(String columnName) {


        ImageView imageView1 = new ImageView(new Image(getClass().getResourceAsStream("/com/opethic/genivis/ui/assets/add.png")));
        imageView1.setFitWidth(20);
        imageView1.setFitHeight(20);
        this.comboBox = new ComboBox<>();
        comboBox.setItems(comboList);
        comboBox.getSelectionModel().selectFirst();
//        this.comboBox.getItems().addAll(comboList);
//        comboBox.setValue(comboList.getFirst());
//        comboBox.setConverter(new StringConverter<String>() {
//            @Override
//            public String toString(String object) {
//                return object;
//            }
//
//            @Override
//            public String fromString(String string) {
//                return string;
//            }
//        });
//        this.comboBox.setValue("Dr");
//        comboBox.getSelectionModel().selectFirst();
        ImageView imageView2 = new ImageView(new Image(getClass().getResourceAsStream("/com/opethic/genivis/ui/assets/sub.png")));
        imageView2.setFitWidth(20);
        imageView2.setFitHeight(20);
        this.columnName = columnName;


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
            System.out.println("NewValue : " + newVal + " OldValue : > " + oldVal);
            if (!newVal) {
                commitEdit(comboBox.getValue().toString());
                System.out.println("comboBox.getValue().toString() : " + comboBox.getValue().toString());
                if (comboBox.getValue().toString().equalsIgnoreCase("Cr")) {
                    System.out.println("Hello I am here : " + getTableView().getColumns().get(3).getText());
                    getTableView().getSelectionModel().setCellSelectionEnabled(false); //credit
                    getTableView().getColumns().get(3).setEditable(false);//debit
                } else {
                    System.out.println("Hello Hii I am here" + getTableView().getColumns().get(2).getText());
                    getTableView().getColumns().get(2).setEditable(false);
                    getTableView().getColumns().get(3).setEditable(true);
                }
                getTableView().refresh();
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

