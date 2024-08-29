package com.opethic.genivis.controller.account_entry;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.opethic.genivis.GenivisApplication;
import com.opethic.genivis.controller.commons.OverlaysEffect;
import com.opethic.genivis.controller.dialogs.SingleInputDialogs;
import com.opethic.genivis.controller.master.ledger.common.LedgerCommonController;
import com.opethic.genivis.controller.master.ledger.common.LedgerMessageConsts;
import com.opethic.genivis.dto.CommonDTO;
import com.opethic.genivis.dto.TranxLedgerWindowDTO;
import com.opethic.genivis.dto.account_entry.GVBankNameDTO;
import com.opethic.genivis.dto.master.ledger.BankDetailsDTO;
import com.opethic.genivis.dto.reqres.creditNote.BillByBillId;
import com.opethic.genivis.dto.reqres.creditNote.BillByBillPerticulars;
import com.opethic.genivis.dto.reqres.creditNote.BillByBillRowReqDTO;
import com.opethic.genivis.dto.reqres.payment.*;
import com.opethic.genivis.dto.reqres.product.Communicator;
import com.opethic.genivis.dto.reqres.product.TableCellCallback;
import com.opethic.genivis.dto.reqres.pur_tranx.TranxPaymentInvoiceDTO;
import com.opethic.genivis.models.AccountEntry.ReceiptPaymentModeDTO;
import com.opethic.genivis.network.APIClient;
import com.opethic.genivis.network.EndPoints;
import com.opethic.genivis.network.RequestType;
import com.opethic.genivis.utils.*;
import javafx.application.Platform;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.WorkerStateEvent;
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
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
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

import static com.opethic.genivis.controller.account_entry.PaymentCreateController.paymentLogger;
import static com.opethic.genivis.utils.FxmFileConstants.PURCHASE_INV_CREATE_SLUG;
import static com.opethic.genivis.utils.FxmFileConstants.TRANX_PAYMENT_LIST_SLUG;


public class PaymentCreateController implements Initializable {
    @FXML
    public Label labelPaymentCreateTotalDebit;
    @FXML
    public Label labelPaymentCreateTotalCredit;
    @FXML
    public Text lblPaymentGstNo;
    @FXML
    private HBox payVoucherTotalMain, payVoucherTotalFirst, payVoucherTotalSecond, payVoucherTotalThird, payDivider;
    @FXML
    public Text lblPaymentArea;
    @FXML
    public Text lblPaymentBank;
    @FXML
    public Text lblPaymentContPerson;
    @FXML
    public Text lblPaymentCreditDays;
    @FXML
    public Text lblPaymentFssai;
    @FXML
    public Text lblPaymentLicense;
    @FXML
    public Text lblPaymentRoute;
    @FXML
    public Text lblPaymentContNum;
    @FXML
    public Text lblPaymentPaymentMode, lblPaymentChequeDdReceipt, lblPaymentBankName, lblPaymentPaymentDate;
    @FXML
    public TextField tfPaymentNarration;
    @FXML
    public TableView<PaymentRowDTO> tblPurchaseOrderHistory;
    @FXML
    public TableColumn<PaymentRowDTO, String> tcPurOrdSrcHistory;
    @FXML
    public TableColumn<PaymentRowDTO, String> tcPurOrdInvNoHistory;
    @FXML
    public TableColumn<PaymentRowDTO, String> tcPurOrdInvDateHistory;
    @FXML
    public TableColumn<PaymentRowDTO, String> tcPurOrdInvAmtHistory;
    @FXML
    public TableColumn<PaymentRowDTO, String> tcPurOrdPaidAmtHistory;
    @FXML
    public TableColumn<PaymentRowDTO, String> tcPurOrdPendingAmtHistory;

    @FXML
    private TextField tfTranxDate;
    @FXML
    private BorderPane bpRootPane;

    @FXML
    private TextField tfpaymentCreateVoucherSrNo, tfpaymentCreateVoucherNo;

    @FXML
    private TableView<PaymentRowDTO> tblvPaymentCreate;

    @FXML
    private TableColumn<PaymentRowDTO, String> tblcPaymentCreateParticulars;
    @FXML
    private TableColumn<PaymentRowDTO, String> tblcPaymentCreateDebit;
    @FXML
    private TableColumn<PaymentRowDTO, String> tblcPaymentCreateCredit;
    @FXML
    private TableColumn<PaymentRowDTO, String> tblcPaymentCreateType;

    @FXML
    private Button btnPaymentCreateCancle, btnPaymentCreateSubmit, btnPaymentList;
    public static final Logger paymentLogger = LoggerFactory.getLogger(PaymentCreateController.class);
    private Integer paymentInvoiceId = -1;
    private JsonObject jsonObject = null;
    private String message;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        ResponsiveWiseCssPicker();

        responsiveCmpt();
        tblvPaymentCreate.requestFocus();
        DateValidator.applyDateFormat(tfTranxDate);
        LocalDate currentDate = LocalDate.now();
        tfTranxDate.setText(currentDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        initialEnterMethod();
        sceneInitilization();
        getVoucherNumber();
        tblPurchaseOrderHistory.setFocusTraversable(false);
        bpRootPane.sceneProperty().addListener((obs, oldScene, newScene) -> {
            if (newScene != null) {
                Platform.runLater(() -> {
                    tfTranxDate.requestFocus();
                });
            }
        });
        tableInitialization();

        tfTranxDate.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                if (keyEvent.getCode() == KeyCode.ENTER) {
//                    tblvPaymentCreate.getFocusModel().focus(0,tblcPaymentCreateType);
                    tblvPaymentCreate.requestFocus();
                    tblvPaymentCreate.edit(1, tblcPaymentCreateType);

                }
            }
        });
        tblcPaymentCreateType.setOnEditCommit(event -> {
            moveFocusToNextCell(event.getTablePosition(), true);
        });
        btnPaymentCreateSubmit.setOnAction(e -> {
            if (CommonValidationsUtils.validateForm(tfTranxDate)) {
                createPayment();
            }
                }
        );

    }


    private void ResponsiveWiseCssPicker() {
        double height = Screen.getPrimary().getBounds().getHeight();
        double width = Screen.getPrimary().getBounds().getWidth();
        System.out.println("width" + width);
        if (width >= 992 && width <= 1024) {
            payDivider.setSpacing(10);
            payVoucherTotalFirst.prefWidthProperty().bind(payVoucherTotalMain.widthProperty().multiply(0.8));
            payVoucherTotalSecond.prefWidthProperty().bind(payVoucherTotalMain.widthProperty().multiply(0.1));
            payVoucherTotalThird.prefWidthProperty().bind(payVoucherTotalMain.widthProperty().multiply(0.1));
            tblPurchaseOrderHistory.getStylesheets().add(GenivisApplication.class.getResource("ui/css/purchase_challan_css/supplier_table_css/supplier_table_992_1024.css").toExternalForm());
            bpRootPane.getStylesheets().add(GenivisApplication.class.getResource("ui/css/TranxCommonCssStyles/tranxCommonCssStyle1.css").toExternalForm());
        } else if (width >= 1025 && width <= 1280) {
            payDivider.setSpacing(12);
            payVoucherTotalFirst.prefWidthProperty().bind(payVoucherTotalMain.widthProperty().multiply(0.8));
            payVoucherTotalSecond.prefWidthProperty().bind(payVoucherTotalMain.widthProperty().multiply(0.1));
            payVoucherTotalThird.prefWidthProperty().bind(payVoucherTotalMain.widthProperty().multiply(0.1));
            tblPurchaseOrderHistory.getStylesheets().add(GenivisApplication.class.getResource("ui/css/purchase_challan_css/supplier_table_css/supplier_table_1025_1280.css").toExternalForm());
            bpRootPane.getStylesheets().add(GenivisApplication.class.getResource("ui/css/TranxCommonCssStyles/tranxCommonCssStyle2.css").toExternalForm());
        } else if (width >= 1281 && width <= 1368) {
            payDivider.setSpacing(12);
            payVoucherTotalFirst.prefWidthProperty().bind(payVoucherTotalMain.widthProperty().multiply(0.8));
            payVoucherTotalSecond.prefWidthProperty().bind(payVoucherTotalMain.widthProperty().multiply(0.1));
            payVoucherTotalThird.prefWidthProperty().bind(payVoucherTotalMain.widthProperty().multiply(0.1));
            tblPurchaseOrderHistory.getStylesheets().add(GenivisApplication.class.getResource("ui/css/purchase_challan_css/supplier_table_css/supplier_table_1281_1368.css").toExternalForm());
            bpRootPane.getStylesheets().add(GenivisApplication.class.getResource("ui/css/TranxCommonCssStyles/tranxCommonCssStyle3.css").toExternalForm());
        } else if (width >= 1369 && width <= 1400) {
            payDivider.setSpacing(15);
            payVoucherTotalFirst.prefWidthProperty().bind(payVoucherTotalMain.widthProperty().multiply(0.8));
            payVoucherTotalSecond.prefWidthProperty().bind(payVoucherTotalMain.widthProperty().multiply(0.1));
            payVoucherTotalThird.prefWidthProperty().bind(payVoucherTotalMain.widthProperty().multiply(0.1));
            tblPurchaseOrderHistory.getStylesheets().add(GenivisApplication.class.getResource("ui/css/purchase_challan_css/supplier_table_css/supplier_table_1369_1400.css").toExternalForm());
            bpRootPane.getStylesheets().add(GenivisApplication.class.getResource("ui/css/TranxCommonCssStyles/tranxCommonCssStyle4.css").toExternalForm());
        } else if (width >= 1401 && width <= 1440) {
            payDivider.setSpacing(15);
            payVoucherTotalFirst.prefWidthProperty().bind(payVoucherTotalMain.widthProperty().multiply(0.8));
            payVoucherTotalSecond.prefWidthProperty().bind(payVoucherTotalMain.widthProperty().multiply(0.1));
            payVoucherTotalThird.prefWidthProperty().bind(payVoucherTotalMain.widthProperty().multiply(0.1));
            tblPurchaseOrderHistory.getStylesheets().add(GenivisApplication.class.getResource("ui/css/purchase_challan_css/supplier_table_css/supplier_table_1401_1440.css").toExternalForm());
            bpRootPane.getStylesheets().add(GenivisApplication.class.getResource("ui/css/TranxCommonCssStyles/tranxCommonCssStyle5.css").toExternalForm());
        } else if (width >= 1441 && width <= 1680) {
            payDivider.setSpacing(20);
            payVoucherTotalFirst.prefWidthProperty().bind(payVoucherTotalMain.widthProperty().multiply(0.8));
            payVoucherTotalSecond.prefWidthProperty().bind(payVoucherTotalMain.widthProperty().multiply(0.1));
            payVoucherTotalThird.prefWidthProperty().bind(payVoucherTotalMain.widthProperty().multiply(0.1));
            tblPurchaseOrderHistory.getStylesheets().add(GenivisApplication.class.getResource("ui/css/purchase_challan_css/supplier_table_css/supplier_table_1441_1680.css").toExternalForm());
            bpRootPane.getStylesheets().add(GenivisApplication.class.getResource("ui/css/TranxCommonCssStyles/tranxCommonCssStyle6.css").toExternalForm());
        } else if (width >= 1681 && width <= 1920) {
            payDivider.setSpacing(20);
            payVoucherTotalFirst.prefWidthProperty().bind(payVoucherTotalMain.widthProperty().multiply(0.8));
            payVoucherTotalSecond.prefWidthProperty().bind(payVoucherTotalMain.widthProperty().multiply(0.1));
            payVoucherTotalThird.prefWidthProperty().bind(payVoucherTotalMain.widthProperty().multiply(0.1));
            tblPurchaseOrderHistory.getStylesheets().add(GenivisApplication.class.getResource("ui/css/invoice_product_history_table.css").toExternalForm());
            bpRootPane.getStylesheets().add(GenivisApplication.class.getResource("ui/css/TranxCommonCssStyles/tranxCommonCssStyle7.css").toExternalForm());
        }
    }

    private void initialEnterMethod() {
        bpRootPane.addEventFilter(KeyEvent.KEY_PRESSED, (KeyEvent event) -> {
            if (event.getCode() == KeyCode.ENTER) {
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

            } else if (event.getCode() == KeyCode.S && event.isControlDown()) {
                btnPaymentCreateSubmit.fire();
            } else if (event.getCode() == KeyCode.X && event.isControlDown()) {
                btnPaymentCreateCancle.fire();
            } else if (event.getCode() == KeyCode.E && event.isControlDown()) {
                btnPaymentList.fire();
            }
        });
    }

    //? To Redirect the PurChallan List page
    public void backToListModify() {
        AlertUtility.AlertConfirmation(AlertUtility.alertTypeConfirmation, LedgerMessageConsts.msgGoList, input -> {
            if (input == 1) {
                GlobalController.getInstance().addTabStatic(TRANX_PAYMENT_LIST_SLUG, false);
            }
        });
    }

    //? Redirect To Create Payment Create Page
    public void backToList() {
        AlertUtility.AlertConfirmation(AlertUtility.alertTypeConfirmation, LedgerMessageConsts.msgConfirmationOnCancel + tfpaymentCreateVoucherNo.getText() , input -> {
            if (input == 1) {
                clearAllFields();
            }
        });
    }

    private void clearAllFields() {
        tfTranxDate.setText("");
        tfPaymentNarration.setText("");

        labelPaymentCreateTotalCredit.setText("0");
        labelPaymentCreateTotalDebit.setText("0");

        //? clear Ledger Info
        lblPaymentGstNo.setText("");
        lblPaymentArea.setText("");
        lblPaymentBank.setText("");
        lblPaymentContPerson.setText("");
        lblPaymentContNum.setText("");
        lblPaymentCreditDays.setText("");
        lblPaymentFssai.setText("");
        lblPaymentLicense.setText("");
        lblPaymentRoute.setText("");

        //? clear Product Info
        lblPaymentPaymentMode.setText("");
        lblPaymentChequeDdReceipt.setText("");
        lblPaymentBankName.setText("");
        lblPaymentPaymentDate.setText("");

        tblvPaymentCreate.getItems().clear();// Add a new blank row if needed
        tblvPaymentCreate.getItems().addAll(new PaymentRowDTO("", "Dr", "", "", ""));
        tfTranxDate.requestFocus();
        tblPurchaseOrderHistory.getItems().clear();
    }

    /**************** GetById API Integration Start ******************/

    public void setEditId(Integer InId) {
        paymentInvoiceId = InId;
        if (InId > -1) {
            setEditData();
        }
    }

    private void setEditData() {
        getPaymentById(paymentInvoiceId);
    }

    private void getPaymentById(Integer paymentInvoiceId) {
        APIClient apiClient = null;
        paymentLogger.debug(" getPaymentById Started....");
        try {
            Map<String, String> body = new HashMap<>();
            body.put("payment_id", paymentInvoiceId.toString());
            String formData = Globals.mapToStringforFormData(body);
            apiClient = new APIClient(EndPoints.GET_PAYMENT_BY_ID, formData, RequestType.FORM_DATA);
            apiClient.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
                @Override
                public void handle(WorkerStateEvent workerStateEvent) {
                    String response = workerStateEvent.getSource().getValue().toString();
                    System.out.println("Payment Response--->" + response);
                    PaymentMasterRes paymentMasterRes = new Gson().fromJson(response, PaymentMasterRes.class);
                    System.out.println("Payment Response POJO--->" + paymentMasterRes);
                    if (paymentMasterRes.getResponseStatus() == 200) {
                        paymentLogger.debug("Get getCreditNoteById Success...");
                        setPaymentFormData(paymentMasterRes);
                    }
                }
            });

            apiClient.setOnFailed(new EventHandler<WorkerStateEvent>() {
                @Override
                public void handle(WorkerStateEvent workerStateEvent) {
                    paymentLogger.error("Network API cancelled in getCreditNoteById()" + workerStateEvent.getSource().getValue().toString());
                }
            });
            apiClient.setOnCancelled(new EventHandler<WorkerStateEvent>() {
                @Override
                public void handle(WorkerStateEvent workerStateEvent) {
                    paymentLogger.error("Network API cancelled in getCreditNoteById()" + workerStateEvent.getSource().getValue().toString());
                }
            });
            apiClient.start();
            paymentLogger.debug("Get getCreditNoteById End...");

        } catch (Exception e) {
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            String exceptionAsString = sw.toString();
            paymentLogger.error("Exception in getCreditNoteById():" + exceptionAsString);
        } finally {
            apiClient = null;
        }
    }

    private void setPaymentFormData(PaymentMasterRes paymentMasterRes) {
        btnPaymentCreateSubmit.setText("Update");
//        btnPaymentList.setText("List");
        List<BillByBillRowReqDTO> billrowList = new ArrayList<>();
        String tranxDate = paymentMasterRes.getTransactionDt();
        if (tranxDate != null && tfTranxDate != null) {
            LocalDate applicable_date = LocalDate.parse(tranxDate);
            tfTranxDate.setText(applicable_date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        }
        tfpaymentCreateVoucherSrNo.setText("" + paymentMasterRes.getPaymentSrNo());
        tfpaymentCreateVoucherNo.setText(paymentMasterRes.getPaymentCode());
        tfPaymentNarration.setText(paymentMasterRes.getNarrations());
        labelPaymentCreateTotalDebit.setText(paymentMasterRes.getTotalAmt().toString());
        labelPaymentCreateTotalCredit.setText(paymentMasterRes.getTotalAmt().toString());
        int index = 0;
        tblvPaymentCreate.getItems().clear();
        for (PaymentParticularRes mRow : paymentMasterRes.getPerticulars()) {
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
            if (mRow.getBankPaymentType() != null) billRowReqDTO.setPaymentType(mRow.getBankPaymentType().getLabel());
            else {
                billRowReqDTO.setPaymentType("");
            }
            billRowReqDTO.setPaymentDate(mRow.getPaymentDate());
            billRowReqDTO.setTranxNo(mRow.getPaymentTranxNo());
            billRowReqDTO.setBankName(mRow.getBankAccName());
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
            tblvPaymentCreate.getItems().add(row);
            index++;
        }
    }

    private void moveFocusToNextCell(TablePosition<PaymentRowDTO, String> position, boolean nextColumn) {
        final int row = position.getRow();
        final int col = position.getColumn();
        if (nextColumn) {
            if (col + 1 < tblvPaymentCreate.getColumns().size()) {
                tblvPaymentCreate.getSelectionModel().select(row, tblvPaymentCreate.getColumns().get(col + 2));
                tblvPaymentCreate.edit(row, tblvPaymentCreate.getColumns().get(col + 2));
            } else if (row + 2 < tblvPaymentCreate.getItems().size()) {
                tblvPaymentCreate.getSelectionModel().select(row + 2, tblvPaymentCreate.getColumns().get(0));
                tblvPaymentCreate.edit(row + 1, tblvPaymentCreate.getColumns().get(0));
            }
        }
    }

    private void tableInitialization() {

        TableCellCallback<String[]> callback = item -> {

            if (item[0].equals("true")) {
                lableInitialization();
            } else {
                lblPaymentGstNo.setText(item[0]);
                lblPaymentArea.setText(item[1]);
                lblPaymentBank.setText(item[2]);
                lblPaymentContPerson.setText(item[3]);
                lblPaymentContNum.setText(item[4]);
                lblPaymentCreditDays.setText(item[5]);
                lblPaymentFssai.setText(item[6]);
                lblPaymentLicense.setText(item[7]);
                lblPaymentRoute.setText(item[8]);
            }
        };
        tblvPaymentCreate.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tblvPaymentCreate.setEditable(true);
        tblvPaymentCreate.setFocusTraversable(false);

        tblvPaymentCreate.getItems().addAll(new PaymentRowDTO("", "Dr", "", "", ""));


        tblcPaymentCreateType.setCellValueFactory(cellData -> cellData.getValue().typeProperty());
        tblcPaymentCreateType.setCellFactory(column -> new ComboBoxTableCellForPayment("type"));

        tblcPaymentCreateParticulars.setCellValueFactory(cellData -> cellData.getValue().particularsProperty());
        tblcPaymentCreateParticulars.setCellFactory(column -> new TextFieldTableCellForPayment("particular", bpRootPane, callback));

        tblcPaymentCreateDebit.setCellValueFactory(cellData -> cellData.getValue().debitProperty());
        tblcPaymentCreateDebit.setCellFactory(column -> new TextFieldTableCellForPayment("debit", bpRootPane, callback));

        tblcPaymentCreateCredit.setCellValueFactory(cellData -> cellData.getValue().creditProperty());
        tblcPaymentCreateCredit.setCellFactory(column -> new TextFieldTableCellForPayment("credit", bpRootPane, callback));

    }

    private void lableInitialization() {
        List<PaymentRowDTO> tableList = tblvPaymentCreate.getItems();
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
        labelPaymentCreateTotalCredit.setText(String.valueOf(totalCredit));
        labelPaymentCreateTotalDebit.setText(String.valueOf(totalDebit));

        if (totalCredit != totalDebit) {
            tblvPaymentCreate.getItems().add(new PaymentRowDTO("", "Cr", "", "0.0", "0.0"));
        }
    }

    private void getVoucherNumber() {
        try {
            HttpResponse<String> response = APIClient.getRequest(EndPoints.GET_PAYMENT_INVOICE_LAST_RECORDS);
            if (response != null && response.statusCode() == 200) {
                VoucherLastRecordResponse voucherLastRecordResponse = new Gson().fromJson(response.body(), VoucherLastRecordResponse.class);
                tfpaymentCreateVoucherSrNo.setText("" + voucherLastRecordResponse.getPaymentSrNo());
                tfpaymentCreateVoucherNo.setText(voucherLastRecordResponse.getPaymentCode());
            } else {
                paymentLogger.debug("Response is empty");
            }
        } catch (Exception e) {
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            String exceptionAsString = sw.toString();
            paymentLogger.error("Exception in getVoucherNumber():" + exceptionAsString);
        }

    }

    public void paymentEditPage() {
        try {
            FXMLLoader parent = new FXMLLoader(getClass().getResource("/com/opethic/genivis/ui/account_entry/payment_create.fxml"));
            Parent root = parent.load();
            Stage stage = new Stage();
            stage.setTitle("Payment Edit Page");
            stage.setMaximized(true);
            stage.setScene(new Scene(root, 1920, 1080));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void sceneInitilization() {
        bpRootPane.sceneProperty().addListener((observable, oldScene, newScene) -> {
            if (newScene != null && newScene.getWindow() instanceof Stage) {
                Communicator.stage = (Stage) newScene.getWindow();
            }
        });
    }

    public void responsiveCmpt() {
        tblcPaymentCreateType.prefWidthProperty().bind(tblvPaymentCreate.widthProperty().multiply(0.05));
        tblcPaymentCreateParticulars.prefWidthProperty().bind(tblvPaymentCreate.widthProperty().multiply(0.75));
        tblcPaymentCreateDebit.prefWidthProperty().bind(tblvPaymentCreate.widthProperty().multiply(0.10));
        tblcPaymentCreateCredit.prefWidthProperty().bind(tblvPaymentCreate.widthProperty().multiply(0.10));
    }

    //Create Api Integration
    public void createPayment() {
        String btnText = btnPaymentCreateSubmit.getText();
        AlertUtility.CustomCallback callback = number -> {
            if (number == 1) {
        Double totalAmt = 0.0;
        APIClient apiClient = null;
        try {
            paymentLogger.debug(" createPayment() Started...");
            Map<String, String> map = new HashMap<>();
            if (btnText.equalsIgnoreCase("Update")) {
                map.put("payment_id", paymentInvoiceId.toString());
            }
            map.put("payment_sr_no", tfpaymentCreateVoucherSrNo.getText());
            map.put("payment_code", tfpaymentCreateVoucherNo.getText());
            map.put("narration", tfPaymentNarration.getText());
            if (tfTranxDate != null && tfTranxDate.getText() != null && !tfTranxDate.getText().isEmpty()) {
                map.put("transaction_dt", Communicator.text_to_date.fromString(tfTranxDate.getText()).toString());
            }

            ArrayList<BillByBillRowReqDTO> rowData = new ArrayList<>();
            ObservableList<PaymentRowDTO> tableViewItems = tblvPaymentCreate.getItems();
            System.out.println("Table row DTO --->" + tableViewItems.size());
            for (PaymentRowDTO paymentRowDTO : tableViewItems) {
                if (paymentRowDTO.getPaymentMode() != null)
                    map.put("gv_bank_payment_type", paymentRowDTO.getPaymentMode());
                if (paymentRowDTO.getBankAccount() != null)
                    map.put("gv_bank_name", paymentRowDTO.getBankAccount());
                if (paymentRowDTO.getTranxNum() != null)
                    map.put("gv_payment_tranx_no", paymentRowDTO.getTranxNum());
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
            map.put("total_amt", String.valueOf(totalAmt));
            String mRowData = new Gson().toJson(rowData, new TypeToken<List<BillByBillRowReqDTO>>() {
            }.getType());
            map.put("row", mRowData);
            String finalReq = Globals.mapToStringforFormData(map);
            System.out.println("final Request >> : " + finalReq);

            if (btnText.equalsIgnoreCase("Submit")) {
                apiClient = new APIClient(EndPoints.CREATE_PAYMENT_ENDPOINT, finalReq, RequestType.FORM_DATA);
            } else {
                apiClient = new APIClient(EndPoints.UPDATE_PAYMENT_ENDPOINT, finalReq, RequestType.FORM_DATA);
            }

            apiClient.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
                @Override
                public void handle(WorkerStateEvent workerStateEvent) {
                    jsonObject = new Gson().fromJson(workerStateEvent.getSource().getValue().toString(), JsonObject.class);
                    System.out.println("JsonObject >> :" + jsonObject);
                    message = jsonObject.get("message").getAsString();
                    int status = jsonObject.get("responseStatus").getAsInt();
//                    if (jsonObject.get("responseStatus").getAsInt() == 200) {
//                        AlertUtility.AlertDialogForSuccess("Success", message, callback -> {
//                            if (callback) {
//                                GlobalController.getInstance().addTabStatic(TRANX_PAYMENT_LIST_SLUG, false);
//                            }
//                        });
//                       /* AlertUtility.CustomCallback callback = (number) -> {
//                            if (number == 1) {
//                                GlobalController.getInstance().addTabStatic(TRANX_PAYMENT_CREATE_SLUG, false);
//                            }
//                        };
//                        Stage stage2 = (Stage) bpRootPane.getScene().getWindow();*/
//
//
//                    }
                    if (status == 200) {
                        AlertUtility.AlertSuccessTimeout(AlertUtility.alertTypeSuccess, message, input -> {
                            GlobalController.getInstance().addTabStatic(TRANX_PAYMENT_LIST_SLUG, false);
                        });
                    } else {
                        AlertUtility.AlertErrorTimeout(AlertUtility.alertTypeError, message, in -> {
                            btnPaymentCreateSubmit.requestFocus();
                        });
                    }
                }
            });
            apiClient.setOnCancelled(new EventHandler<WorkerStateEvent>() {
                @Override
                public void handle(WorkerStateEvent workerStateEvent) {
                    paymentLogger.error("Network API cancelled in createPayment()" + workerStateEvent.getSource().getValue().toString());

                }
            });
            apiClient.setOnFailed(new EventHandler<WorkerStateEvent>() {
                @Override
                public void handle(WorkerStateEvent workerStateEvent) {
                    paymentLogger.error("Network API failed in createPayment()" + workerStateEvent.getSource().getValue().toString());
                }
            });
            apiClient.start();
            paymentLogger.debug("createPayment End...");
        } catch (Exception e) {
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            String exceptionAsString = sw.toString();
            paymentLogger.error("Exception in createPayment():" + exceptionAsString);
        }
    } else {
        System.out.println("working!");
    }
};
        if (btnText.equalsIgnoreCase("Submit")) {
                AlertUtility.AlertConfirmation(AlertUtility.alertTypeConfirmation, LedgerMessageConsts.msgConfirmationOnSubmit + tfpaymentCreateVoucherNo.getText() + LedgerMessageConsts.msgInvoiceNumber, callback);
                } else {
                AlertUtility.AlertConfirmation(AlertUtility.alertTypeConfirmation, LedgerMessageConsts.msgConfirmationOnUpdate + tfpaymentCreateVoucherNo.getText() + LedgerMessageConsts.msgInvoiceNumber, callback);
                }
    }
}

class TextFieldTableCellForPayment extends TableCell<PaymentRowDTO, String> {
    private TextField textField;
    private String columnName;
    private ObservableList<TranxPaymentInvoiceDTO> mLedgerInvoiceList = FXCollections.observableArrayList();
    private ObservableList<TranxPaymentInvoiceDTO> mLedgerDebitNoteList = FXCollections.observableArrayList();
    private ObservableList<CommonDTO> paymentMode = FXCollections.observableArrayList();

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

    public TextFieldTableCellForPayment(String columnName, Parent rootPane, TableCellCallback<String[]> callback) {

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
            if (!newVal) {
                commitEdit(textField.getText());
            }
        });
        textField.focusedProperty().addListener((obs, wasFocused, isNowFocused) -> {
            if (isNowFocused) {
                textField.setStyle("-fx-background-color: #fff9c4; -fx-border-radius: 0px; -fx-border-width: 2; -fx-border-color: #00a0f5;");
            } else {
                textField.setStyle("-fx-background-color: #f6f6f9; -fx-border-radius: 0px; -fx-border-width: 2; -fx-border-color: #f6f6f9;");
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
                if (columnName.equalsIgnoreCase("particular")) {
                    ReceiptPaymentModeDTO inPaymentMode = new ReceiptPaymentModeDTO();
                    GVBankNameDTO inGvBankName = new GVBankNameDTO();
                    SingleInputDialogs.openLedgerPopUp(stage, "Creditors", input -> {
                        paymentLogger.debug("Payment Ledger Popup opened");
                        String ledgerName = (String) input[0];
                        String ledgerId = (String) input[1];
                        String ledgerType = (String) input[4];
                        String balancingMethod = (String) input[5];
                        String underSlug = (String) input[6];
                        String ledgerCode = (String) input[8];
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
                        if (ledgerCode.equalsIgnoreCase("gvmh001")) {
                            /**** opening GV Bank Details popup while payment to GV *****/
                            SingleInputDialogs.openBankPopUpforGvPmt(stage, "GV Bank Details",
                                    inPaymentMode, inGvBankName, (input1, input2) -> {
                                        if (input1 != null && input2 != null) {
                                            newRow.setPaymentMode("" + input1.getId());
                                            newRow.setBankAccount(input2.getAccountNo());
                                            newRow.setTranxNum(input1.getRefrenceNo());
                                            /**** getting Bills List from selected Table Row if Available--> Edit Case *****/
                                            List<BillByBillRowReqDTO> mBillRow = newRow.getBillRowReqDTOS();
                                            List<BillByBillId> selectedBills = new ArrayList<>();
                                            if (mBillRow.size() > 0) {
                                                for (BillByBillRowReqDTO mRowDTO : mBillRow) {
                                                    selectedBills.addAll(mRowDTO.getPerticulars().getBillids());
                                                }
                                            }
                                            if (balancingMethod.equalsIgnoreCase("bill-by-bill")) {
                                                getLedgerPendingBills(selectedBills, stage, ledgerId, ledgerType, balancingMethod, ledgerName, underSlug);
                                            } else if (underSlug.equalsIgnoreCase("bank_account")) {
                                                getLedgerPendingBills(selectedBills, stage, ledgerId, ledgerType, balancingMethod, ledgerName, underSlug);
                                            } else {
                                                getLedgerPendingBills(selectedBills, stage, ledgerId, ledgerType, balancingMethod, ledgerName, underSlug);

                                            }
                                        }
                                    });
                        } else {
//                        /**** getting Bills List from selected Table Row if Available--> Edit Case *****/
                            List<BillByBillRowReqDTO> mBillRow = newRow.getBillRowReqDTOS();
                            List<BillByBillId> selectedBills = new ArrayList<>();
                            if (mBillRow.size() > 0) {
                                for (BillByBillRowReqDTO mRowDTO : mBillRow) {
                                    selectedBills.addAll(mRowDTO.getPerticulars().getBillids());
                                }
                            }
                            if (balancingMethod.equalsIgnoreCase("bill-by-bill")) {
                                getLedgerPendingBills(selectedBills, stage, ledgerId, ledgerType, balancingMethod, ledgerName, underSlug);
                            } else if (underSlug.equalsIgnoreCase("bank_account")) {
                                getLedgerPendingBills(selectedBills, stage, ledgerId, ledgerType, balancingMethod, ledgerName, underSlug);
                            } else {
//                            rowSelectedAmt = Double.parseDouble(getTableView().getItems().get(getIndex()-1).getDebit());
                                getLedgerPendingBills(selectedBills, stage, ledgerId, ledgerType, balancingMethod, ledgerName, underSlug);

                            }
                        }

                    }, in -> {
                        System.out.println("In >> Called >. " + in);
                        if (in == true) {
//                            isLedgerRed = true;
//                            setPurChallDataToProduct();
                        }
                    });
                }
            }

        });
        textField.setOnMouseClicked(actionEvent -> {
            Stage stage = (Stage) rootPane.getScene().getWindow();
            if (columnName.equalsIgnoreCase("particular")) {
                ReceiptPaymentModeDTO inPaymentMode = new ReceiptPaymentModeDTO();
                GVBankNameDTO inGvBankName = new GVBankNameDTO();
                SingleInputDialogs.openLedgerPopUp(stage, "Creditors", input -> {
                    paymentLogger.debug("Payment Ledger Popup opened");
                    String ledgerName = (String) input[0];
                    String ledgerId = (String) input[1];
                    String ledgerType = (String) input[4];
                    String balancingMethod = (String) input[5];
                    String underSlug = (String) input[6];
                    String ledgerCode = (String) input[8];
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
                    selectedLedgerDataInfo(ledgerId);
                    if (ledgerCode.equalsIgnoreCase("gvmh001")) {
                        /**** opening GV Bank Details popup while payment to GV *****/
                        SingleInputDialogs.openBankPopUpforGvPmt(stage, "GV Bank Details", inPaymentMode,
                                inGvBankName, (input1, input2) -> {
                                    if (input1 != null && input2 != null) {
                                        newRow.setPaymentMode("" + input1.getId());
                                        newRow.setBankAccount(input2.getAccountNo());
                                        newRow.setTranxNum(input1.getRefrenceNo());
                                        /**** getting Bills List from selected Table Row if Available--> Edit Case *****/
                                        List<BillByBillRowReqDTO> mBillRow = newRow.getBillRowReqDTOS();
                                        List<BillByBillId> selectedBills = new ArrayList<>();
                                        if (mBillRow.size() > 0) {
                                            for (BillByBillRowReqDTO mRowDTO : mBillRow) {
                                                selectedBills.addAll(mRowDTO.getPerticulars().getBillids());
                                            }
                                        }
                                        if (balancingMethod.equalsIgnoreCase("bill-by-bill")) {
                                            getLedgerPendingBills(selectedBills, stage, ledgerId, ledgerType, balancingMethod, ledgerName, underSlug);
                                        } else if (underSlug.equalsIgnoreCase("bank_account")) {
                                            getLedgerPendingBills(selectedBills, stage, ledgerId, ledgerType, balancingMethod, ledgerName, underSlug);
                                        } else {
                                            getLedgerPendingBills(selectedBills, stage, ledgerId, ledgerType, balancingMethod, ledgerName, underSlug);

                                        }
                                    }
                                });
                    } else {
                        /**** getting Bills List from selected Table Row if Available--> Edit Case *****/
                        List<BillByBillRowReqDTO> mBillRow = newRow.getBillRowReqDTOS();
                        List<BillByBillId> selectedBills = new ArrayList<>();
                        System.out.println("Before Selected Bills--->:" + selectedBills.size());
                        if (mBillRow.size() > 0) {
                            for (BillByBillRowReqDTO mRowDTO : mBillRow) {
                                selectedBills.addAll(mRowDTO.getPerticulars().getBillids());
                            }
                            System.out.println("After Selected Bills--->:" + selectedBills.size());
                        }
                        /**** SC bill by bill ****/
                        if (balancingMethod.equalsIgnoreCase("bill-by-bill")) {
                            getLedgerPendingBills(selectedBills, stage, ledgerId, ledgerType, balancingMethod, ledgerName, underSlug);
                        }
                        /**** Bank account ****/
                        else if (balancingMethod.equalsIgnoreCase("on-account") && underSlug.equalsIgnoreCase("bank_account")) {
                            getLedgerPendingBills(selectedBills, stage, ledgerId, ledgerType, balancingMethod, ledgerName, underSlug);
                        }
                        /**** Cash account ****/
                        else if (underSlug.equalsIgnoreCase("others")) {
                            getLedgerPendingBills(selectedBills, stage, ledgerId, ledgerType, balancingMethod, ledgerName, underSlug);
                        }
                        /**** SC On-account ****/
                        else {
                            getLedgerPendingBills(selectedBills, stage, ledgerId, ledgerType, balancingMethod, ledgerName, underSlug);
                        }
                    }
                }, in -> {
                    System.out.println("In >> Called >. " + in);
                    if (in == true) {
//                            isLedgerRed = true;
//                            setPurChallDataToProduct();
                    }
                });
            }

        });

    }

    private <T> void openBankAccountPopup(Stage stage, String ledgerId, String ledgerType, String balancingMethod,
                                          String ledgerName, String underSlug, String title, BiConsumer<BillByBillPerticulars, List<String>> callback) {
        BillByBillPerticulars particular = new BillByBillPerticulars();

        OverlaysEffect.setOverlaysEffect(stage);
        Stage primaryStage = new Stage();

        primaryStage.initOwner(stage);
        primaryStage.initStyle(StageStyle.TRANSPARENT);
        primaryStage.initModality(Modality.APPLICATION_MODAL);

        //Main Layout................................................................................................................................
        BorderPane borderPane = new BorderPane();
        borderPane.getStylesheets().add(GenivisApplication.class.getResource("/com/opethic/genivis/ui/css/popup_for_catalog.css").toExternalForm());
        borderPane.setStyle("-fx-background-radius: 5; -fx-background-color: white; -fx-border-color: #bfbfc0; -fx-border-radius: 5; -fx-border-width: 0.8;");

        Platform.runLater(() -> {
            borderPane.requestFocus();
        });
//        fetchPaymentMode();

        //BorderPan under Top Layout....................................................................................................................
        HBox hbox_top = new HBox();
        hbox_top.setMinWidth(998);
        hbox_top.setMaxWidth(998);
        hbox_top.setPrefWidth(998);
        hbox_top.setMaxHeight(50);
        hbox_top.setMinHeight(50);
        hbox_top.setPrefHeight(50);

        Label popup_title = new Label("Bank Account");
        popup_title.setStyle("-fx-font-size: 16; -fx-text-fill: #404040; -fx-font-weight: bold;");
        popup_title.setPadding(new Insets(0, 10, 0, 0));

        Image image = new Image(GenivisApplication.class.getResource("/com/opethic/genivis/ui/assets/close.png").toExternalForm());
        ImageView imageView = new ImageView(image);
        imageView.setStyle("-fx-cursor: hand;");
        imageView.setOnMouseClicked(event -> {
            primaryStage.close();
            OverlaysEffect.removeOverlaysEffect(stage);
        });
        HBox.setMargin(imageView, new Insets(0, 10, 0, 0));
        imageView.setFitWidth(30);
        imageView.setFitHeight(30);

        hbox_top.setAlignment(Pos.CENTER_LEFT);
        hbox_top.getChildren().add(popup_title);
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        HBox.setMargin(popup_title, new Insets(0, 0, 0, 10));
        hbox_top.getChildren().add(spacer);
        hbox_top.getChildren().add(imageView);
        hbox_top.setStyle("-fx-background-radius: 5 5 0 0; -fx-background-color: #d9f0fb; -fx-border-color: #ffffff; -fx-border-width: 0 0 3 0;");

        //BorderPane Under Center Layout.....................................................................................................
        HBox hbox_center = new HBox();
        hbox_center.setMinWidth(998);
        hbox_center.setMaxWidth(998);
        hbox_center.setPrefWidth(998);
        hbox_center.setAlignment(Pos.CENTER_LEFT);
        hbox_center.setStyle("-fx-background-color: #e6f2f8;");
        Label lbPayMode = LedgerCommonController.createLabel("Payment Mode");
        Label lblbPayModeReq = LedgerCommonController.createLabelWithRequired();
        ComboBox<CommonDTO> cmPaymentMode = new ComboBox<>();
        cmPaymentMode.setMinWidth(120);
        cmPaymentMode.setMaxWidth(120);
        cmPaymentMode.setPrefWidth(120);

        cmPaymentMode.setItems(FXCollections.observableArrayList(fetchPaymentMode()));
//        AutoCompleteBox autoCompleteBox1 = new AutoCompleteBox(cmPaymentMode, -1);

        Label lbRefNo = LedgerCommonController.createLabel("Payment Tranx Number");
        TextField tfRefNo = LedgerCommonController.createTextField("Reference Number");

        Label lblPaymentDate = LedgerCommonController.createLabel("Payment Date");
        TextField tfPayDate = LedgerCommonController.createTextField("DD/MM/YYYY");
        if (!tfPayDate.getText().equalsIgnoreCase("")) DateValidator.applyDateFormat(tfPayDate);
        /**** for Date *****/
        borderPane.sceneProperty().addListener((observable, oldScene, newScene) -> {
            if (newScene != null && newScene.getWindow() instanceof Stage) {
                Communicator.stage = (Stage) newScene.getWindow();
            }
        });
        Insets Margin = new Insets(0, 10, 0, 10);
        HBox.setMargin(lbPayMode, new Insets(0, 0, 0, 10));
        HBox.setMargin(cmPaymentMode, Margin);
        HBox.setMargin(lbRefNo, Margin);
        HBox.setMargin(tfRefNo, Margin);
        HBox.setMargin(lblPaymentDate, Margin);
        HBox.setMargin(tfPayDate, Margin);
        hbox_center.getChildren().addAll(lbPayMode, lblbPayModeReq, cmPaymentMode, lbRefNo, tfRefNo, lblPaymentDate, tfPayDate);
        HBox hbox_bottom = new HBox();
        hbox_bottom.setMinWidth(998);
        hbox_bottom.setMaxWidth(998);
        hbox_bottom.setPrefWidth(998);
        hbox_bottom.setMaxHeight(55);
        hbox_bottom.setMinHeight(55);
        hbox_bottom.setPrefHeight(55);
        hbox_bottom.setSpacing(10);
        hbox_bottom.setStyle("-fx-background-radius: 0 0 5 5; -fx-background-color: white;");
        hbox_bottom.setAlignment(Pos.CENTER_RIGHT);

        Button suButton = new Button("Submit");
        HBox.setMargin(suButton, new Insets(0, 10, 0, 0));
        Button clButton = new Button("Clear");
        suButton.setId("submit-btn");
        clButton.setId("cancel-btn");
        hbox_bottom.getChildren().addAll(clButton, suButton);
        borderPane.setTop(hbox_top);
        borderPane.setCenter(hbox_center);
        borderPane.setBottom(hbox_bottom);

        Node[] nodes = new Node[]{cmPaymentMode, tfRefNo, tfPayDate};
        CommonValidationsUtils.setupFocusNavigation(nodes);

        Scene scene = new Scene(borderPane, 1000, 170);

        primaryStage.setScene(scene);
        primaryStage.setTitle(title);
        primaryStage.setResizable(false);

        scene.setFill(Color.TRANSPARENT);

        primaryStage.centerOnScreen();
        primaryStage.setOnShown((e) -> {
            Platform.runLater(cmPaymentMode::requestFocus);
        });
        primaryStage.show();
        List<String> selectedItem = new ArrayList<>();
        cmPaymentMode.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                Object object[] = new Object[1];
                object[0] = cmPaymentMode.getSelectionModel().getSelectedItem();
                if (object[0] != null) {
                    for (CommonDTO commonDTO : paymentMode) {
                        if (object[0].equals(commonDTO)) {
                            selectedItem.add(0, commonDTO.getId());//Bank Payment Mode Id
                            selectedItem.add(1, commonDTO.getText());// Bank Payment mode Text i.e UPI,RTGS
                        }
                    }
                }
            }
        });
        suButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                particular.setLedgerName(ledgerName);
                particular.setId(Long.parseLong(ledgerId));
                particular.setType("bank_account");
                selectedItem.add(2, tfRefNo.getText());//
                selectedItem.add(3, tfPayDate.getText());//
                selectedItem.add(4, ledgerName);//Bank Account name
                callback.accept(particular, selectedItem);
                primaryStage.close();
                OverlaysEffect.removeOverlaysEffect(stage);
            }
        });

    }

    private List<CommonDTO> fetchPaymentMode() {
        try {
            paymentMode.clear();
            HttpResponse<String> response = APIClient.getRequest(EndPoints.PAYMENT_MODE_LIST_ENDPOINT);
            JsonObject jsonObject = new Gson().fromJson(response.body(), JsonObject.class);
            if (jsonObject.get("responseStatus").getAsInt() == 200) {
                JsonArray responseArray = jsonObject.get("responseObject").getAsJsonArray();
                if (responseArray.size() > 0) {
                    for (JsonElement element : responseArray) {
                        JsonObject item = element.getAsJsonObject();
                        String payment_mode = item.get("payment_mode").getAsString();
                        paymentMode.add(new CommonDTO(payment_mode, item.get("id").getAsString()));
                    }
                } else {
                    paymentLogger.debug("Response is Empty fetchPaymentMode()");
                }
            } else {
                paymentLogger.debug("Error in response fetchPaymentMode()");
            }

        } catch (Exception e) {
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            String exceptionAsString = sw.toString();
            paymentLogger.error("Exception in paymentLogger()---->paymentLogger()" + exceptionAsString);
        }
        return paymentMode;
    }


    private void selectedLedgerDataInfo(String ledgerId) {
        String[] strings = new String[16];
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


    private void getLedgerPendingBills(List<BillByBillId> selectedBills, Stage stage, String ledgerId,
                                       String ledgerType, String balancingMethod, String ledgerName, String underSlug) {
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
                if (selectedBills.size() == 0)
                    lableInitializationBillByBill();
            });
        }
        /**** Bank Account ****/
        else if (underSlug.equalsIgnoreCase("bank_account")) {
            openBankAccountPopup(stage, ledgerId, ledgerType, balancingMethod, ledgerName, underSlug, "Bank Account", (billParticular, rowData) -> {
                BillByBillRowReqDTO row = null;
                String mType = "cr";
                Double selectedAmt = 0.0;
                if (underSlug.equalsIgnoreCase("bank_account")) {
                   /* int rowIndex = getIndex() - 1;
                    System.out.println("Selected Row Amt BA--->" + rowSelectedAmt);
                    Double amt = rowSelectedAmt - rowInputAmt;*/
                    int rowIndex = getIndex() - 1;
                   /* if (rowSelectedAmt == 0)
                        rowSelectedAmt = Double.parseDouble(getTableView().getItems().get(rowIndex).getDebit());
                    else {
                        rowSelectedAmt = Double.parseDouble(getTableView().getItems().get(getIndex()).getDebit());
                    }*/
                    rowSelectedAmt = Double.parseDouble(getTableView().getItems().get(getIndex() - 1).getDebit());
                    getTableView().getItems().get(getIndex()).setCredit("" + rowSelectedAmt);

                }

                String payDate = rowData.get(3);
                if (!payDate.equalsIgnoreCase("")) {
                    payDate = Communicator.text_to_date.fromString(rowData.get(3)).toString();
                }
                row = new BillByBillRowReqDTO(mType, selectedAmt, billParticular, rowData.get(0), rowData.get(2), rowData.get(4), payDate);
                billRow.add(row);
                getTableView().getItems().get(getIndex()).setBillRowReqDTOS(billRow);
                lableInitializationBillByBill();
            });
        }
        /**** Cash Accounts ****/
        else if (underSlug.equalsIgnoreCase("others")) {
           /* if (rowSelectedAmt == 0)
                rowSelectedAmt = Double.parseDouble(getTableView().getItems().get(getIndex()).getDebit());
            else {
                rowSelectedAmt = Double.parseDouble(getTableView().getItems().get(getIndex()).getDebit());
            }*/
//            Double amt = rowSelectedAmt - rowInputAmt;
            rowSelectedAmt = Double.parseDouble(getTableView().getItems().get(getIndex() - 1).getDebit());
            getTableView().getItems().get(getIndex()).setCredit("" + rowSelectedAmt);
//            rowInputAmt = Double.parseDouble(getTableView().getItems().get(getIndex()).getCredit());
            System.out.println("Selected Bill--->" + selectedBills.size());
            lableInitializationBillByBill();
        }
        /*** SC On-Account ***/
       /* else {
            rowSelectedAmt = Double.parseDouble(getTableView().getItems().get(getIndex()).getDebit());
        }*/
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
            getTableView().getItems().add(new PaymentRowDTO("", "Cr", "", "0.0", "0.0"));
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
            System.out.println("Item--->" + item.toString());
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
            ((PaymentRowDTO) getTableRow().getItem()).setCredit(newValue.isEmpty() ? "00" : newValue);
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
                            bill.setBillDetailsId(mPayment.getBillDetailsId());
                            bill.setInvoiceDate(mPayment.getBilldate());
                            bill.setRemainingAmt(Double.parseDouble(mPayment.getBalance()) - Double.parseDouble(mPayment.getPaidAmt()));
                            bill.setInvoiceUniqueId("pur_invoice," + mPayment.getId());
                            bill.setSource("pur_invoice");
                            bill.setTranxTrackingCode(mPayment.getTransactionTrackCode());
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
                            bill.setBillDetailsId(mPayment.getBillDetailsId());
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
        hbBottom2.getChildren().add(spacer1);
        hbBottom2.getChildren().add(submit);
        HBox.setMargin(submit, new Insets(8, 8, 5, 0));
        submit.setId("sub");
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
                        paymentLogger.debug("Missing From Date and To Date in Pending Bill Popup Payment Tranx");
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
                if (key.equalsIgnoreCase("pi")) {
                    totalPaidAmt += Double.parseDouble(mList.getPaidAmt());
                } else {
                    totalPaidAmtDn += Double.parseDouble(mList.getPaidAmt());
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
                                invoiceData.setTransactionTrackCode(mList.getTranxTrackingCode());
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
                                        //!tracking code
                                        bill.setTranxTrackingCode(mSinglebill.getTranxTrackingCode());
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

}

class ComboBoxTableCellForPayment extends TableCell<PaymentRowDTO, String> {

    private ComboBox<String> comboBox = null;
    private final String columnName;


    public ComboBoxTableCellForPayment(String columnName) {


        ImageView imageView1 = new ImageView(new Image(getClass().getResourceAsStream("/com/opethic/genivis/ui/assets/add.png")));
        imageView1.setFitWidth(20);
        imageView1.setFitHeight(20);
        this.comboBox = new ComboBox<>();
        this.comboBox.getItems().addAll("Dr", "Cr");
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

        this.comboBox.setOnAction(event -> {
//            if (comboBox.getValue() != null)
            commitEdit(comboBox.getValue().toString());
        });

        // Commit when focus is lost
        this.comboBox.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal) {
                commitEdit(comboBox.getValue().toString());
            }
        });

//        AutoCompleteBox autoCompleteBox1 = new AutoCompleteBox(this.comboBox, -1);


        this.comboBox.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.TAB || event.getCode() == KeyCode.ENTER) {
                commitEdit(comboBox.getValue());
              /*  final int row = getIndex();
                final int col = getTableView().getColumns().indexOf(getTableColumn());
                final TableColumn<PaymentRowDTO, ?> column = getTableView().getColumns().get(col + 1);
                getTableView().edit(row, column);*/

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

class CheckBoxTableCellForPayment extends TableCell<TranxPaymentInvoiceDTO, Boolean> {

    private CheckBox cbSelect = null;
    private final String columnName;


    public CheckBoxTableCellForPayment(String columnName) {

        this.cbSelect = new CheckBox();
        this.columnName = columnName;

        cbSelect.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {

                TranxPaymentInvoiceDTO mPayment = null;
                if (cbSelect.isSelected()) {
                    mPayment = getTableView().getItems().get(getIndex());
                    mPayment.setPaidAmt(mPayment.getBalance());
                    mPayment.setBalanceAmt("0.00");
                } else {
                    mPayment = getTableView().getItems().get(getIndex());
                    mPayment.setPaidAmt("0.00");
                    mPayment.setBalanceAmt(mPayment.getBalance());
                }
            }
        });
    }

    @Override
    public void startEdit() {
        super.startEdit();
        setText(null);
        setGraphic(new HBox(cbSelect));
        cbSelect.requestFocus();
    }


    @Override
    protected void updateItem(Boolean item, boolean empty) {
        super.updateItem(item, empty);
        if (empty) {
            setGraphic(null);
        } else {

            Boolean itemToSet = item;
            HBox hbox = new HBox();
            hbox.getChildren().addAll(cbSelect);
            hbox.setAlignment(Pos.CENTER);
            hbox.setSpacing(5);
            setGraphic(hbox);
        }
    }

    @Override
    public void commitEdit(Boolean newValue) {
        super.commitEdit(newValue);
        if (columnName.equalsIgnoreCase("Select")) {
            System.out.println("New Value-->" + newValue);
            ((TranxPaymentInvoiceDTO) getTableRow().getItem()).setIs_row_selected(newValue);
        }
    }
}
