package com.opethic.genivis.controller.tranx_purchase;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.opethic.genivis.controller.dialogs.SingleInputDialogs;
import com.opethic.genivis.controller.master.ProductListController;
import com.opethic.genivis.dto.*;
import com.opethic.genivis.dto.reqres.product.Communicator;
import com.opethic.genivis.dto.reqres.pur_tranx.*;
import com.opethic.genivis.models.tranx.sales.TranxTaxCal;
import com.opethic.genivis.network.APIClient;
import com.opethic.genivis.network.EndPoints;
import com.opethic.genivis.network.RequestType;
import com.opethic.genivis.utils.*;
import javafx.animation.PauseTransition;
import javafx.application.Platform;
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
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.Duration;
import javafx.util.StringConverter;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URL;
import java.net.http.HttpResponse;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.zip.GZIPOutputStream;

import static com.opethic.genivis.utils.FxmFileConstants.*;

public class PurchaseReturnCreateController implements Initializable {

    @FXML
    private TableColumn tblcPurInvPrdWindCode, tblcPurInvPrdWindPrdName, tblcPurInvPrdWindPacking,
            tblcPurInvPrdWindBarcode, tblcPurInvPrdWindBrand, tblcPurInvPrdWindMrp, tblcPurInvPrdWindCurrStk,
            tblcPurInvPrdWindUnit, tblcPurInvPrdWindSaleRate, tblcPurInvPrdWindAction;
    @FXML
    private TableView tblvPurInvLedgerWindow, tblvPurInvPrdWindtableview;
    @FXML
    private Button btnModify, btnPurInvPrdWindowClose;

    @FXML
    private TextField tfPurInvLedgName, tfPurRtnsr, tfPurRtnInvNum, tfPurInvCreateTranxDate, tfPurRtnInvDate;

    @FXML
    private Button btnPurInvBatchWinClose, btnPurchaseInvAddnlChargWindowClose;

    @FXML
    private ComboBox<CommonDTO> cmbPurRtnPurAC;
    @FXML
    private ComboBox<GstDetailsDTO> cmbPurRtnLedgerGst;
    @FXML
    private CheckBox chbPurInvROff;

    @FXML
    private TableView<CmpTRowDTO> tvPurInvCreateCMPTRow;
    @FXML
    private TableColumn<CmpTRowDTO, String> tblcPurInvCreateCmptParticular, tblcPurInvCreateCmptPackage, tblcPurInvCreateCmptUnit, tblcPurInvCreateCmptQuantity, tblcPurInvCreateCmptBatch, tblcPurInvCreateCmptRate, tblcPurInvCreateCmptGrossAmt, tblcPurInvCreateCmptDiscPerc, tblcPurInvCreateCmptDiscAmt, tblcPurInvCreateCmptTaxPerc, tblcPurInvCreateCmptNetAmt;
    @FXML
    private TableColumn<TranxBatchWindowDTO, String> tblcPurInvBatWinBatchNo, tblcPurInvBatWinMRP, tblcPurInvBatWinPurRate, tblcPurInvBatWinQty, tblcPurInvBatWinFreeQty, tblcPurInvBatWinDisPer, tblcPurInvBatWinDisAmt, tblcPurInvBatWinBarcode, tblcPurInvBatWinMarginPer, tblcPurInvBatWinFSR, tblcPurInvBatWinCSR, tblcPurInvBatWinSaleRate;
    @FXML
    private TableColumn<TranxBatchWindowDTO, Void> tblcPurInvBatWinAction;
    @FXML
    private TableColumn<TranxBatchWindowDTO, LocalDate> tblcPurInvBatWinPurDate, tblcPurInvBatWinMFGDate, tblcPurInvBatWinEXPDate;
    @FXML
    private TableView<TranxBatchWindowDTO> tblvPurInvBatWinCreTableview;
    @FXML
    private TableView<AdditionalChargesRowDTO> tblcPurchaseInvAddtnlChrgCmpTRowView;  //additional charges tableView
    @FXML        //additional charges table column
    private TableColumn<AdditionalChargesRowDTO, String> tblcPurchaseInvAddtnlChrgCmpTRowParticulars, tblcPurchaseInvAddtnlChrgCmpTRowAmt, tblcPurchaseInvCmpTRowPercentage;

    @FXML
    private HBox hbapPurInvProductWindow, hbapPurInvBatchWindow, hbapPurchaseInvAddnlChrgWindow, hbapPurchaseInvAddnlChrgLedgerWindow;
    private int selectedRowIndex;
    @FXML
    private Label lblPurInvBatWinPrdName, lblPurInvBatWinTax, lblPurInvBatWinCWOT, lblPurInvBatWinCWT;
    private String productBatchTax;
    private String selectedRowProductdId;
    private String prdBatchTaxPer;
    private Double costValue = 0.0, costWithTaxValue = 0.0, taxAmt;
    private static int rowIndexParticular;
    private static ObservableList<String> unitList = FXCollections.observableArrayList();
    private static final Logger purInvCreateLogger = LoggerFactory.getLogger(PurchaseReturnCreateController.class);
    private String productId = "";
    private String responseBody;
    private Node[] focusableNodes;
    @FXML
    private AnchorPane rootAnchorPane;
    @FXML
    private RadioButton rbPaymentModeAdjust, rbPaymentModeCredit;
    private ToggleGroup radioGroup = new ToggleGroup();

    @FXML
    private TextField tfPurRtnDisPer, tfPurRtnDisAmt;
    @FXML
    private Label lblPurRtnTotalTax, lblPurRtnTaxableAmt, lblPurRtnTotalDisc, lblPurRtnTotalRowGross, lblPurRtnTotalBillAmt;
    @FXML
    private Label lblPurRtnTotalQty, lblPurRtnFreeQty, lblPurROff, lblPurInvtotalCGST, lblPurInvtotalSGST;
    ArrayList<Double> netAmountList;
    @FXML
    private StackPane stkpnPurInvLedgerInfo, stkpnPurInvBatchInfo, stkpnPurInvProductInfo;
    @FXML
    private ToggleButton btnPurInvCrEdBatchInfo, btnPurInvCrEdLedgerInfo, btnPurInvCrEdProductInfo;
    @FXML
    private Button btnPurInvAddChgsSubmit, btnPurInvAddChgsCancel, btnSubmit, btnCancel;  //for additional charges
    @FXML
    private TextField tfPurInvoiceAddTotalAmt;
    private Stage stage = null;
    private String ledgerName = "";
    private String ledgerId = "";
    private String ledgerStateCode = "";
    private String stateCode = "";

    private ObservableList<GstDetailsDTO> gstDetails = FXCollections.observableArrayList();
    private CmpTRowDTO newRow = null;
    private List<CmpTRowDTO> newRowList = new ArrayList<>();
    int index = 0;
    @FXML
    private TableView<GstDTO> tblvPurInvoiceGST;
    @FXML
    private TableColumn<GstDTO, String> tblcPurInvoiceGST, tblcPurInvoiceCGST, tblcPurInvoiceSGST;
    private DecimalFormat decimalFormat = new DecimalFormat("0.#");
    private String paymentMode = "";


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //todo : autofocus on TranxDate
        Platform.runLater(() -> tfPurInvCreateTranxDate.requestFocus());
//         Enter traversal
        rootAnchorPane.addEventFilter(KeyEvent.KEY_PRESSED, (KeyEvent event) -> {
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
            }
        });

        DateValidator.applyDateFormat(tfPurInvCreateTranxDate);
        DateValidator.applyDateFormat(tfPurRtnInvDate);
        LocalDate currentDate = LocalDate.now();
        tfPurInvCreateTranxDate.setText(currentDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        tfPurRtnInvDate.setText(currentDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        sceneInitilization();
        rbPaymentModeAdjust.setToggleGroup(radioGroup);
        rbPaymentModeCredit.setToggleGroup(radioGroup);
        /**** get Last record of purchase return ****/
        netAmountList = new ArrayList<>();
        rootAnchorPane.sceneProperty().addListener((obs, oldScene, newScene) -> {
            if (newScene != null) {
                Platform.runLater(() -> {
                    tfPurInvLedgName.requestFocus();  //for focus on ledger name
                });
            }
        });
        cmbPurRtnLedgerGst.setOnKeyPressed(event -> {    //code to focus on purchase serial from supplier GSTIN
            if (event.getCode() == KeyCode.ENTER || event.getCode() == KeyCode.TAB) {
                tfPurRtnsr.requestFocus();
            }
        });
        tfPurRtnsr.setOnKeyPressed(event -> {        //code to focus on invoice no. from purchase serial
            if (event.getCode() == KeyCode.ENTER || event.getCode() == KeyCode.TAB) {
                tfPurRtnInvNum.requestFocus();
            }
        });
        tfPurRtnInvNum.setOnKeyPressed(event -> {     //code to focus on invoice date from invoice no
            if (event.getCode() == KeyCode.ENTER || event.getCode() == KeyCode.TAB) {
                tfPurRtnInvDate.requestFocus();
            }
        });

        tfPurRtnInvDate.textProperty().addListener((observable, oldValue, newValue) -> {
            checkInvoiceDateIsBetweenFYFun(String.valueOf(newValue));
        });
        tfPurRtnInvDate.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER || event.getCode() == KeyCode.TAB) {
                System.out.println("inside tab or enter");

            }
        });
        cmbPurRtnPurAC.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER || event.getCode() == KeyCode.TAB) {
                rbPaymentModeAdjust.requestFocus();
            }
        });
        Platform.runLater(() -> {
            getLastPurRtnLastRecord();
            getPurchaseAccounts();
        });

//        rowData();
        batchRowData();
        initialTableViewCellFactory();
        cmpTRowData();
        tfPurRtnInvNum.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER || event.getCode() == KeyCode.TAB) {
                validatePurchaseInvoice();
            }
        });

        tfPurInvLedgName.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.DOWN) {
                tblvPurInvLedgerWindow.requestFocus();
            }
        });
        btnPurInvBatchWinClose.setOnAction(event -> {
            hbapPurInvBatchWindow.setVisible(false);
        });
        btnPurInvPrdWindowClose.setOnAction(event -> {
            hbapPurInvProductWindow.setVisible(false);
        });
        purInvCreateLogger.debug("Opened Ledger Modal:On Clicking on by selecting Ledger Name and " + "Select the Ledger from the Modal");
        tfPurInvLedgName.setOnMouseClicked(actionEvent -> {
            stage = (Stage) rootAnchorPane.getScene().getWindow();
            SingleInputDialogs.openLedgerPopUp(stage, "Creditors", input -> {
                purInvCreateLogger.debug("Callback to Get all the Details of the Selected ledger and Set it in the Fields");
                ledgerName = (String) input[0];
                ledgerId = (String) input[1];
                gstDetails = (ObservableList<GstDetailsDTO>) input[2];
                ledgerStateCode = (String) input[3];
                tfPurInvLedgName.setText(ledgerName);
                setLedgerGSTDATA(gstDetails);
                purInvCreateLogger.debug("Bill by Bill by Window --> Opened Ledger Bill Modal:On Clicking by " + "selecting the Ledger Name ");
                System.out.println("Ledger Id->" + input[0].toString());
                getBillbyBillWindow(stage, ledgerId);
            },in->{
                System.out.println("In >> Called >. "+ in);
                if(in == true){
//                            isLedgerRed = true;
//                            setPurChallDataToProduct();
                }
            });

        });
        tfPurInvLedgName.setOnKeyPressed(ev -> {
            if (ev.getCode() == KeyCode.SPACE) {
                stage = (Stage) rootAnchorPane.getScene().getWindow();
                SingleInputDialogs.openLedgerPopUp(stage, "Filter", input -> {
                    purInvCreateLogger.debug("Callback to Get all the Details of the Selected ledger and Set it in the Fields");
                    ledgerName = (String) input[0];
                    ledgerId = (String) input[1];
                    gstDetails = (ObservableList<GstDetailsDTO>) input[2];
                    ledgerStateCode = (String) input[3];
                    tfPurInvLedgName.setText(ledgerName);
                    setLedgerGSTDATA(gstDetails);
                    purInvCreateLogger.debug("Bill by Bill by Window --> Opened Ledger Bill Modal:On Clicking by " + "selecting the Ledger Name ");
                    getBillbyBillWindow(stage, ledgerId);
                },in->{
                    System.out.println("In >> Called >. "+ in);
                    if(in == true){
//                            isLedgerRed = true;
//                            setPurChallDataToProduct();
                    }
                });
                tfPurRtnInvDate.requestFocus();
            }

        });

        //code to get the GST details when double click on Ledger list
        purInvCreateLogger.info("starting of ledger window double click on ledge list");

        purInvCreateLogger.info("ending of ledger window double click on ledge list");

        //for info of batch
        btnPurInvCrEdBatchInfo.setOnAction(actionEvent -> {
            stkpnPurInvBatchInfo.setVisible(true);
            stkpnPurInvProductInfo.setVisible(false);
            stkpnPurInvLedgerInfo.setVisible(false);
        });
        //for showing the product info
        btnPurInvCrEdProductInfo.setOnAction(actionEvent -> {
            stkpnPurInvProductInfo.setVisible(true);
            stkpnPurInvBatchInfo.setVisible(false);
            stkpnPurInvLedgerInfo.setVisible(false);
        });
        //for showing the ledger info
        btnPurInvCrEdLedgerInfo.setOnAction(actionEvent -> {
            stkpnPurInvLedgerInfo.setVisible(true);
            stkpnPurInvBatchInfo.setVisible(false);
            stkpnPurInvProductInfo.setVisible(false);
        });
        //code to redirect to list page when click on modify button
        btnModify.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                FXMLLoader root = new FXMLLoader(getClass().getResource(FxmFileConstants.PURCHASE_RETURN_LIST_FXML));
                Parent loader = null;
                try {
                    loader = root.load();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                Stage stage = new Stage();
                Scene scene = new Scene(loader);
                stage.setScene(scene);
                stage.show();
                stage.setMaximized(true);
            }
        });
        btnModify.setOnAction(event -> GlobalController.getInstance().addTabStatic(PURCHASE_RETURN_LIST_SLUG, false));

        tblcPurInvCreateCmptBatch.setCellFactory(column -> {     //for create the field of batch in CMPT row
            return new TableCell<>() {
                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);

                    if (item == null || empty) {
                        setText(null);
                        setGraphic(null);
                    } else {
                        TextField textField = new TextField(item);
                        setGraphic(textField);

                        textField.setOnMouseClicked(event -> {
                            if (!isEmpty()) {
                                CmpTRowDTO selectedItem = getTableView().getItems().get(getIndex());
                                selectedRowIndex = getIndex();
                                lblPurInvBatWinPrdName.setText(selectedItem.getParticulars());
                                lblPurInvBatWinTax.setText(selectedItem.getTax_per());
                                productBatchTax = selectedItem.getTax_per();
                                selectedRowProductdId = selectedItem.getProduct_id();
                                hbapPurInvBatchWindow.setVisible(true);
                            }
                        });

                    }
                }
            };
        });
        tblvPurInvPrdWindtableview.setEditable(true);
        tfPurRtnDisPer.textProperty().addListener((observable, oldValue, newValue) -> {  //for calculation of dis percentage when changes in total dis field
            if (newValue.isEmpty()) {
                for (CmpTRowDTO cmpTRowDTO : tvPurInvCreateCMPTRow.getItems()) {
                    String netAmt = cmpTRowDTO.getOrg_net_amt();
                    cmpTRowDTO.setNet_amt(netAmt);
                    Platform.runLater(() -> {
                        tvPurInvCreateCMPTRow.refresh();
                    });
                    tblcPurInvCreateCmptNetAmt.setCellValueFactory(new PropertyValueFactory<>("net_amt"));
                }

            } else {
                discountPropotionalCalculation();
            } /*else if (!oldValue.isEmpty()) {
                discountPropotionalCalculation();
            }*/

            // Perform actions based on the new value
        });


        tblcPurchaseInvAddtnlChrgCmpTRowParticulars.setCellFactory(column -> {
            return new TableCell<>() {
                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);

                    if (item == null || empty) {
                        setText(null);
                        setGraphic(null);
                    } else {
                        TextField textField = new TextField(item);
                        setGraphic(textField);

                        textField.setOnMouseClicked(event -> {
                            if (!isEmpty()) {
                                //     getLedgerList("");    //fetchDataOfAllTransactionLedgers
                                AdditionalChargesRowDTO selectedItem = getTableView().getItems().get(getIndex());
                                hbapPurchaseInvAddnlChrgLedgerWindow.setVisible(true);
                                rowIndexParticular = getIndex();

                                // List of  All Ledgers Api call
                                getTranxProductList();
                            }
                        });
                    }
                }
            };
        });
        // set the change listener
        radioGroup.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
            public void changed(ObservableValue<? extends Toggle> ob, Toggle o, Toggle n) {
                if (radioGroup.getSelectedToggle() != null) {

                    // get the selected radio button
                    RadioButton r = (RadioButton) radioGroup.getSelectedToggle();

                    // get the label of the selected radio button

                    paymentMode = r.getText();

                    // change the label messges

                }
            }
        });
        /*btnSubmit.setText("Submit");
        purRtnInvoiceId = PurchaseReturnListController.getPurRtnInvoiceId();
        if (purRtnInvoiceId > 0) {
            btnSubmit.setText("Update");
            *//*** api calling to get By Id of purchase return ***//*
            purInvCreateLogger.debug("Api calling to get By Id of Purchase Return");
            getPurRtnById(purRtnInvoiceId);
        }*/
       /* nodetraversal(tfCode, tfName);
        nodetraversal(tfName, btnNamePlus);
        nodetraversal(btnNamePlus, cmbPack);
        nodetraversal(cmbPack, btnPackingPlus);
        nodetraversal(btnPackingPlus, cmbBrand);
        nodetraversal(cmbBrand, btnBrandPlus);
        nodetraversal(btnBrandPlus, cmbTaxType);
        nodetraversal(cmbTaxType, cmbTax);
        nodetraversal(cmbTax, tfApplicableFrom);
        nodetraversal(tfApplicableFrom, cmbHsn);
        nodetraversal(cmbHsn, btnHsnPlus);
        nodetraversal(btnHsnPlus, tfDiscPer);*/
        nodetraversal(tfPurRtnInvDate, cmbPurRtnPurAC);
        nodetraversal(cmbPurRtnPurAC, rbPaymentModeAdjust);
        reverseTraversal(tfPurRtnsr, cmbPurRtnLedgerGst);
        reverseTraversal(cmbPurRtnLedgerGst, tfPurInvLedgName);
        nodetraversal(tfPurRtnsr, tfPurRtnInvNum);

    }

    private void reverseTraversal(Node prev_node, Node next_node) {
        prev_node.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.SHIFT && event.getCode() == KeyCode.TAB) {
                next_node.requestFocus();
            }
        });
    }

    private void nodetraversal(Node current_node, Node next_node) {
        current_node.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                next_node.requestFocus();
                event.consume();
            } else if (event.getCode() == KeyCode.LEFT || event.getCode() == KeyCode.RIGHT) {
                if (current_node instanceof RadioButton radioButton) {
                    radioButton.setSelected(!radioButton.isSelected());
                    radioButton.fire();
                }
            }
        });
    }

    private void sceneInitilization() {
        rootAnchorPane.sceneProperty().addListener((observable, oldScene, newScene) -> {
            if (newScene != null && newScene.getWindow() instanceof Stage) {
                Communicator.stage = (Stage) newScene.getWindow();
            }
        });
    }

    private void getBillbyBillWindow(Stage stage, String ledgerId) {
        SingleInputDialogs.openInvoiceList(Long.parseLong(ledgerId), stage, "Select Bill", input -> {
            purInvCreateLogger.debug("Callback to Get all the Details of the Selected ledger and Set it in the Fields");
            getTranxRtnProductList(stage, input[0].toString());
        });
    }


    public void calculateVal() {
        double totalAmt = 0.0; // Assuming amtValue is of type double

        for (AdditionalChargesRowDTO item : tblcPurchaseInvAddtnlChrgCmpTRowView.getItems()) {
            String amtValueStr = tblcPurchaseInvAddtnlChrgCmpTRowAmt.getCellData(item);
            double amtValue = Double.parseDouble(amtValueStr);
            totalAmt += amtValue;
            System.out.println(amtValueStr);
        }

        System.out.println("Total amount: " + totalAmt);

        Double finalTotalAmt = totalAmt;

        btnPurInvAddChgsSubmit.setOnAction(event -> {
            hbapPurchaseInvAddnlChrgWindow.setVisible(false);
            tfPurInvoiceAddTotalAmt.setText(String.valueOf(finalTotalAmt));
            additionalChargesCalculation();
        });


        ObservableList<TablePosition> selectedCells = tblcPurchaseInvAddtnlChrgCmpTRowView.getSelectionModel().getSelectedCells();
        HashSet<Integer> rows = new HashSet<>();
        for (TablePosition tablePosition : selectedCells) {
            rows.add(tablePosition.getRow());
        }

//        for (Integer row : rows) {
//            TableColumn<AdditionalChargesRowDTO, String> column = (TableColumn<AdditionalChargesRowDTO, String>) tblcSalesChallanAddtnlChrgCmpTRowView.getColumns().get(columnIndex);
//            String value = column.getCellData(row);
//            System.out.println(value);
//        }


    }

    public void initialTableViewCellFactory() {

        tblcPurInvBatWinFSR.setCellValueFactory(new PropertyValueFactory<>("b_rate_a"));
        tblcPurInvCreateCmptRate.setCellValueFactory(new PropertyValueFactory<>("rate"));
        tblcPurInvCreateCmptQuantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        tblcPurInvCreateCmptDiscPerc.setCellValueFactory(new PropertyValueFactory<>("dis_per"));
        tblcPurInvCreateCmptDiscAmt.setCellValueFactory(new PropertyValueFactory<>("dis_amt"));

    }

    public void addRow(ActionEvent actionEvent) {
        CmpTRowDTO newRow = new CmpTRowDTO("", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "");
        tvPurInvCreateCMPTRow.getItems().add(newRow);
    }

    public GstDetailsDTO convertJsonToGstDetailsDTO(JsonElement jsonElement) {
        // Parse the JsonElement and create a GstDetailsDTO object
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        String gstNumber = jsonObject.get("gstNumber").getAsString();
        String id = jsonObject.get("id").getAsString();
        String state = jsonObject.get("state").getAsString();
        // Parse other properties as needed
        return new GstDetailsDTO(id, gstNumber, state);
    }

    //function for showing the list of Purchase A/C
    private void getPurchaseAccounts() {
        HttpResponse<String> response = APIClient.getRequest(EndPoints.PUR_ACCOUNTS_LIST);
        String purAccList = response.body();
        JsonObject jsonObject = new Gson().fromJson(purAccList, JsonObject.class);
        if (jsonObject.get("responseStatus").getAsInt() == 200) {
            JsonArray jsonArray = jsonObject.get("list").getAsJsonArray();
            ObservableList<CommonDTO> accList = FXCollections.observableArrayList();
            for (JsonElement element : jsonArray) {
                JsonObject obj = element.getAsJsonObject();
                String purAccId = obj.get("id").getAsString();
                String purAccName = obj.get("name").getAsString();
                accList.add(new CommonDTO(purAccName, purAccId));
            }
            cmbPurRtnPurAC.setItems(accList);
            cmbPurRtnPurAC.getSelectionModel().selectFirst();
            cmbPurRtnPurAC.setConverter(new StringConverter<CommonDTO>() {
                @Override
                public String toString(CommonDTO purchase) {
                    return purchase != null ? purchase.getText() : "";
                }

                @Override
                public CommonDTO fromString(String s) {
                    return null;
                }
            });
        }

    }

    private void getLastPurRtnLastRecord() {
        HttpResponse<String> response = APIClient.getRequest(EndPoints.GET_PUR_RTN_LAST_RECORD);
        String purAccList = response.body();
        JsonObject jsonObject = new Gson().fromJson(purAccList, JsonObject.class);
        if (jsonObject.get("responseStatus").getAsInt() == 200) {
            tfPurRtnsr.setText(jsonObject.get("count").getAsString());
            tfPurRtnInvNum.setText(jsonObject.get("purReturnNo").getAsString());
        }
    }

    //function for getting all the list of product
    public void getTranxProductList() {
        try {
            Map<String, String> map = new HashMap<>();

            map.put("search", "");
            map.put("barcode", "");
            String requestBody = Globals.mapToStringforFormData(map);
            HttpResponse<String> response = APIClient.postFormDataRequest(requestBody, EndPoints.GET_TRANX_PRODUCT_LIST);
            JsonObject jsonObject = new Gson().fromJson(response.body(), JsonObject.class);
            ObservableList<TranxProductWindowDTO> observableList = FXCollections.observableArrayList();
            if (jsonObject.get("responseStatus").getAsInt() == 200) {
                JsonArray responseArray = jsonObject.get("list").getAsJsonArray();

                if (responseArray.size() > 0) {
                    tblvPurInvPrdWindtableview.setVisible(true);
                    for (JsonElement element : responseArray) {
                        String barcode = "";
                        JsonObject item = element.getAsJsonObject();
                        String id = item.get("id").getAsString();
                        String code = item.get("code").getAsString();
                        String product_name = item.get("product_name").getAsString();
                        String packing = item.get("packing") != null ? item.get("packing").getAsString() : "";
                        if (item.get("barcode") != null) {
                            barcode = item.get("barcode").getAsString();
                        } else {
                            barcode = "";
                        }

                        String brand = item.get("brand").getAsString();
                        String mrp = item.get("mrp") != null ? item.get("mrp").getAsString() : "0.00";
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
                    tblcPurInvPrdWindCode.setCellValueFactory(new PropertyValueFactory<>("code"));
                    tblcPurInvPrdWindPrdName.setCellValueFactory(new PropertyValueFactory<>("product_name"));
                    tblcPurInvPrdWindPacking.setCellValueFactory(new PropertyValueFactory<>("packing"));
                    tblcPurInvPrdWindBarcode.setCellValueFactory(new PropertyValueFactory<>("barcode"));
                    tblcPurInvPrdWindBrand.setCellValueFactory(new PropertyValueFactory<>("brand"));
                    tblcPurInvPrdWindMrp.setCellValueFactory(new PropertyValueFactory<>("mrp"));
                    tblcPurInvPrdWindCurrStk.setCellValueFactory(new PropertyValueFactory<>("current_stock"));
                    tblcPurInvPrdWindUnit.setCellValueFactory(new PropertyValueFactory<>("unit"));
                    tblcPurInvPrdWindSaleRate.setCellValueFactory(new PropertyValueFactory<>("sales_rate"));

                    tblvPurInvPrdWindtableview.setItems(observableList);
                } else {
                    System.out.println("responseObject is null");
                }
            } else {
                System.out.println("Error in response");
            }

        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(e.getMessage());

        }
    }

    //function for search of Transaction product list - getTranxProductList1
    public void getTranxProductList1(String search) {
        try {
            Map<String, String> map = new HashMap<>();

            map.put("search", search);
            map.put("barcode", "");
            String requestBody = Globals.mapToStringforFormData(map);
            HttpResponse<String> response = APIClient.postFormDataRequest(requestBody, EndPoints.GET_TRANX_PRODUCT_LIST);
            JsonObject jsonObject = new Gson().fromJson(response.body(), JsonObject.class);

            ObservableList<TranxProductWindowDTO> observableList = FXCollections.observableArrayList();
            if (jsonObject.get("responseStatus").getAsInt() == 200) {
                JsonArray responseArray = jsonObject.get("list").getAsJsonArray();

                if (responseArray.size() > 0) {
                    tblvPurInvPrdWindtableview.setVisible(true);
                    for (JsonElement element : responseArray) {
                        String barcode = "";
                        JsonObject item = element.getAsJsonObject();
                        String id = item.get("id").getAsString();
                        String code = item.get("code").getAsString();
                        String product_name = item.get("product_name").getAsString();
                        String packing = item.get("packing") != null ? item.get("packing").getAsString() : "";
                        if (item.get("barcode") != null) {
                            barcode = item.get("barcode").getAsString();
                        } else {
                            barcode = "";
                        }

                        String brand = item.get("brand").getAsString();
                        String mrp = item.get("mrp") != null ? item.get("mrp").getAsString() : "0.00";
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
                    tblcPurInvPrdWindCode.setCellValueFactory(new PropertyValueFactory<>("code"));
                    tblcPurInvPrdWindPrdName.setCellValueFactory(new PropertyValueFactory<>("product_name"));
                    tblcPurInvPrdWindPacking.setCellValueFactory(new PropertyValueFactory<>("packing"));
                    tblcPurInvPrdWindBarcode.setCellValueFactory(new PropertyValueFactory<>("barcode"));
                    tblcPurInvPrdWindBrand.setCellValueFactory(new PropertyValueFactory<>("brand"));
                    tblcPurInvPrdWindMrp.setCellValueFactory(new PropertyValueFactory<>("mrp"));
                    tblcPurInvPrdWindCurrStk.setCellValueFactory(new PropertyValueFactory<>("current_stock"));
                    tblcPurInvPrdWindUnit.setCellValueFactory(new PropertyValueFactory<>("unit"));
                    tblcPurInvPrdWindSaleRate.setCellValueFactory(new PropertyValueFactory<>("sales_rate"));

                    tblvPurInvPrdWindtableview.setItems(observableList);
                } else {
                    observableList.add(new TranxProductWindowDTO("", "", "", "", "", "", "", "", "", "", "", "", "", "", ""));
                    tblvPurInvPrdWindtableview.setItems(observableList);
                }
            } else {
                System.out.println("Error in response");
            }

        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(e.getMessage());
            System.out.println("eeeeeeee " + e.getMessage());
        }
    }


    private double roundDigit(double value, int decimalPlaces) {
        double factor = Math.pow(10, decimalPlaces);
        return Math.round(value * factor) / factor;
    }

    //function for row calculation
    private void rowCalculation(int rowIndex) {
        CmpTRowDTO cmpTRowDTO = tvPurInvCreateCMPTRow.getItems().get(rowIndex);
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
        r_qty = cmpTRowDTO.getQuantity();
        r_rate = Double.parseDouble(cmpTRowDTO.getRate());
        if (!cmpTRowDTO.getDis_amt().isEmpty()) r_dis_amt = Double.parseDouble(cmpTRowDTO.getDis_amt());
        if (!cmpTRowDTO.getDis_per().isEmpty()) r_dis_per = Double.parseDouble(cmpTRowDTO.getDis_per());
        if (cmpTRowDTO.getDisPer2() != null && !cmpTRowDTO.getDisPer2().isEmpty()) {
            disPer2 = Double.parseDouble(cmpTRowDTO.getDisPer2());
        } else {
            disPer2 = 0.0;
        }
        r_tax_per = Double.parseDouble(cmpTRowDTO.getTax_per());
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
        CmpTRowDTO selectedItem = tvPurInvCreateCMPTRow.getItems().get(rowIndex);
        selectedItem.setGross_amt(String.valueOf(base_amt));
        selectedItem.setNet_amt(String.valueOf(net_amt));
        selectedItem.setOrg_net_amt(String.valueOf(net_amt));
//        selectedItem.setOrg_taxable_amt(taxable_amt);
        selectedItem.setTaxable_amt(taxable_amt);
        selectedItem.setTotal_taxable_amt(total_taxable_amt);
        selectedItem.setIgst(totalTax); // for Tax Amount
        selectedItem.setSgst(totalTax / 2); // for Tax Amount
        selectedItem.setCgst(totalTax / 2); // for Tax  Amount
        selectedItem.setFinTaxAmt(totalTax);
        selectedItem.setFinal_dis_amt(row_dis_amt);

        // Create API Payload Parameters Adding
        selectedItem.setBase_amt(base_amt);
//        selectedItem.setGross_amt(String.valueOf(gross_amt));
        selectedItem.setDis_per(String.valueOf((r_dis_per)));
        selectedItem.setDis_per_cal(total_dis_per);
        selectedItem.setDis_amt(String.valueOf((r_dis_amt)));
        selectedItem.setDis_amt_cal(r_dis_amt);
        selectedItem.setRow_dis_amt(row_dis_amt);
        selectedItem.setGross_amt1(taxable_amt);
        // tax percentage store cmptrow
        selectedItem.setGst(r_tax_per);
        selectedItem.setIgstPer(r_tax_per);
        selectedItem.setSgstPer(r_tax_per / 2);
        selectedItem.setCgstPer(r_tax_per / 2);
        selectedItem.setTotal_igst(totalTax);
        selectedItem.setTotal_sgst(totalTax / 2);
        selectedItem.setTotal_cgst(totalTax / 2);
        selectedItem.setFinal_amt(net_amt);
        selectedItem.setDisPer2(String.valueOf(disPer2));

        //Display data to table view
        tblcPurInvCreateCmptGrossAmt.setCellValueFactory(new PropertyValueFactory<>("gross_amt"));
        tblcPurInvCreateCmptNetAmt.setCellValueFactory(new PropertyValueFactory<>("net_amt"));
        calculateGst();

    }

    public void getUnitsData() {
        Map<String, String> levelAMap = new HashMap<>();
        Map<String, String> levelBMap = new HashMap<>();
        Map<String, String> levelCMap = new HashMap<>();
        Map<String, String> unitsMap = new HashMap<>();
        unitList.clear();
        Map<String, String> map = new HashMap<>();
        map.put("product_id", productId);
        String formData = Globals.mapToStringforFormData(map);

        HttpResponse<String> response = APIClient.postFormDataRequest(formData, "get_all_product_units_packings_flavour");
        responseBody = response.body();
        UnitResDTO resDTO = new Gson().fromJson(responseBody, UnitResDTO.class);
        if (resDTO.getResponseStatus() == 200) {
            ResponseObject responseObject = resDTO.getResponseObject();
            List<LstPackage> lstPackages = responseObject.getLstPackages();
            for (LstPackage levelA : lstPackages) {
                levelAMap.put(levelA.getLabel(), levelA.getValue());
                List<LevelBOpt> levelBOpts = levelA.getLevelBOpts();
                for (LevelBOpt levelB : levelBOpts) {
                    levelBMap.put(levelB.getLabel(), levelB.getValue());
                    List<LevelCOpt> levelCOpts = levelB.getLevelCOpts();
                    for (LevelCOpt levelC : levelCOpts) {
                        levelCMap.put(levelC.getLabel(), levelC.getValue());

                        List<UnitListDTO> units = levelC.getUnitOpts();
                        for (UnitListDTO unitListDTO : units) {
                            unitsMap.put(unitListDTO.getLabel(), unitListDTO.getValue().toString());
                            unitList.add(unitListDTO.getLabel());

                        }
                    }
                }
            }
            tblcPurInvCreateCmptUnit.setCellFactory(column -> {
                ComboBoxTableCell<CmpTRowDTO, String> cell = new ComboBoxTableCell<>();

                cell.getItems().addAll(unitList);

                cell.setConverter(new StringConverter<String>() {
                    @Override
                    public String toString(String s) {
                        return s.toString();
                    }

                    @Override
                    public String fromString(String s) {
                        return null;
                    }
                });
                return cell;
            });

        }

    }

    //function for creating and setting the value of cell of CMPT Row
    public void cmpTRowData() {
        // Set up columns
        tblcPurInvCreateCmptParticular.setCellValueFactory(cellData -> cellData.getValue().particularsProperty());
        tblcPurInvCreateCmptParticular.setCellFactory(TextFieldTableCell.forTableColumn());
        tblcPurInvCreateCmptParticular.setOnEditCommit(event -> {
            CmpTRowDTO person = event.getRowValue();
            person.setParticulars(event.getNewValue());
            moveFocusToNextCell(event.getTablePosition(), true);
        });

        tblcPurInvCreateCmptPackage.setCellValueFactory(cellData -> cellData.getValue().packingProperty());
        tblcPurInvCreateCmptPackage.setCellFactory(TextFieldTableCell.forTableColumn());
        tblcPurInvCreateCmptPackage.setOnEditCommit(event -> {
            CmpTRowDTO person = event.getRowValue();
            person.setPacking(event.getNewValue());
            moveFocusToNextCell(event.getTablePosition(), true);
        });

        tblcPurInvCreateCmptUnit.setCellValueFactory(cellData -> cellData.getValue().unitProperty());
        tblcPurInvCreateCmptUnit.setCellFactory(ComboBoxTableCell.forTableColumn(unitList));
        tblcPurInvCreateCmptUnit.setOnEditCommit(event -> {
            CmpTRowDTO person = event.getRowValue();
            if (event.getNewValue() != null) {
                person.setUnit(event.getNewValue());
                moveFocusToNextCell(event.getTablePosition(), true);
//
            }
        });
        tblcPurInvCreateCmptBatch.setCellValueFactory(cellData -> cellData.getValue().batchNoProperty());
        tblcPurInvCreateCmptBatch.setCellFactory(TextFieldTableCell.forTableColumn());
        tblcPurInvCreateCmptBatch.setOnEditCommit(event -> {
            CmpTRowDTO person = event.getRowValue();
            person.setBatchNo(event.getNewValue());
            moveFocusToNextCell(event.getTablePosition(), true);
        });
        tblcPurInvCreateCmptQuantity.setCellValueFactory(cellData -> cellData.getValue().quantityProperty());
        tblcPurInvCreateCmptQuantity.setCellFactory(TextFieldTableCell.forTableColumn());
        tblcPurInvCreateCmptQuantity.setOnEditCommit(event -> {
            CmpTRowDTO person = event.getRowValue();
            person.setQuantity(event.getNewValue());
            moveFocusToNextCell(event.getTablePosition(), true);
        });
        tblcPurInvCreateCmptRate.setCellValueFactory(cellData -> cellData.getValue().rateProperty());
        tblcPurInvCreateCmptRate.setCellFactory(TextFieldTableCell.forTableColumn());
        tblcPurInvCreateCmptRate.setOnEditCommit(event -> {
            CmpTRowDTO person = event.getRowValue();
            person.setRate(event.getNewValue());
            moveFocusToNextCell(event.getTablePosition(), true);
        });

        tblcPurInvCreateCmptGrossAmt.setCellValueFactory(cellData -> cellData.getValue().gross_amtProperty());
        tblcPurInvCreateCmptGrossAmt.setCellFactory(TextFieldTableCell.forTableColumn());
        tblcPurInvCreateCmptGrossAmt.setOnEditCommit(event -> {
            CmpTRowDTO person = event.getRowValue();
            if (event.getNewValue() != null) {
                person.setGross_amt(event.getNewValue());
                moveFocusToNextCell(event.getTablePosition(), true);
            }
        });
        tblcPurInvCreateCmptDiscPerc.setCellValueFactory(cellData -> cellData.getValue().dis_perProperty());
        tblcPurInvCreateCmptDiscPerc.setCellFactory(TextFieldTableCell.forTableColumn());
        tblcPurInvCreateCmptDiscPerc.setOnEditCommit(event -> {
            CmpTRowDTO person = event.getRowValue();
            if (event.getNewValue() != null) {
                person.setDis_per(event.getNewValue());
                moveFocusToNextCell(event.getTablePosition(), true);
            }
        });
        tblcPurInvCreateCmptDiscAmt.setCellValueFactory(cellData -> cellData.getValue().dis_amtProperty());
        tblcPurInvCreateCmptDiscAmt.setCellFactory(TextFieldTableCell.forTableColumn());
        tblcPurInvCreateCmptDiscAmt.setOnEditCommit(event -> {
            CmpTRowDTO person = event.getRowValue();
            if (event.getNewValue() != null) {
                person.setDis_amt(event.getNewValue());
                moveFocusToNextCell(event.getTablePosition(), true);
            }
        });
        tblcPurInvCreateCmptTaxPerc.setCellValueFactory(cellData -> cellData.getValue().tax_perProperty());
        tblcPurInvCreateCmptTaxPerc.setCellFactory(TextFieldTableCell.forTableColumn());
        tblcPurInvCreateCmptTaxPerc.setOnEditCommit(event -> {
            CmpTRowDTO person = event.getRowValue();
            if (event.getNewValue() != null) {
                person.setTax_per(event.getNewValue());
                moveFocusToNextCell(event.getTablePosition(), true);
            }
        });
        tblcPurInvCreateCmptNetAmt.setCellValueFactory(cellData -> cellData.getValue().net_amtProperty());
        tblcPurInvCreateCmptNetAmt.setCellFactory(TextFieldTableCell.forTableColumn());
        tblcPurInvCreateCmptNetAmt.setOnEditCommit(event -> {
            CmpTRowDTO person = event.getRowValue();
            if (event.getNewValue() != null) {
                person.setNet_amt(event.getNewValue());
                moveFocusToNextRow(event.getTablePosition());
            }
        });
        // Add an initial empty row
        tvPurInvCreateCMPTRow.getItems().add(new CmpTRowDTO("", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", ""));

    }

    //function for batch Window
    public void batchRowData() {

        // Set up columns
        tblcPurInvBatWinBatchNo.setCellValueFactory(cellData -> cellData.getValue().b_noProperty());
        tblcPurInvBatWinBatchNo.setCellFactory(TextFieldTableCell.forTableColumn());
        tblcPurInvBatWinBatchNo.setOnEditCommit(event -> {
            TranxBatchWindowDTO person = event.getRowValue();
            person.setB_no(event.getNewValue());
            moveFocusToNextCellInBatchWin(event.getTablePosition(), true);
        });
        tblcPurInvBatWinPurDate.setCellValueFactory(cellData -> cellData.getValue().pur_dateProperty());
        tblcPurInvBatWinPurDate.setCellFactory(new Callback<TableColumn<TranxBatchWindowDTO, LocalDate>, TableCell<TranxBatchWindowDTO, LocalDate>>() {
            @Override
            public TableCell<TranxBatchWindowDTO, LocalDate> call(TableColumn<TranxBatchWindowDTO, LocalDate> param) {
                return new DatePickerTableCell();
            }
        });
        // Set up columns
        tblcPurInvBatWinMFGDate.setCellValueFactory(cellData -> cellData.getValue().manufacturing_dateProperty());
        tblcPurInvBatWinMFGDate.setCellFactory(new Callback<TableColumn<TranxBatchWindowDTO, LocalDate>, TableCell<TranxBatchWindowDTO, LocalDate>>() {
            @Override
            public TableCell<TranxBatchWindowDTO, LocalDate> call(TableColumn<TranxBatchWindowDTO, LocalDate> param) {
                return new DatePickerTableCell();
            }
        });

        tblcPurInvBatWinEXPDate.setCellValueFactory(cellData -> cellData.getValue().b_expiryProperty());
        tblcPurInvBatWinEXPDate.setCellFactory(new Callback<TableColumn<TranxBatchWindowDTO, LocalDate>, TableCell<TranxBatchWindowDTO, LocalDate>>() {
            @Override
            public TableCell<TranxBatchWindowDTO, LocalDate> call(TableColumn<TranxBatchWindowDTO, LocalDate> param) {
                return new DatePickerTableCell();
            }
        });
        // Assuming tblcPurChallBatWinEXPDate is your TableColumn
        tblcPurInvBatWinEXPDate.setOnEditCommit(event -> {
            // Get the selected date from the event
            LocalDate selectedDate = event.getNewValue();

            // Get the index of the edited row
            int index = event.getTablePosition().getRow();

            // Assuming you have a list of TranxBatchWindowDTO items
            List<TranxBatchWindowDTO> items = tblvPurInvBatWinCreTableview.getItems();

            // Update the corresponding TranxBatchWindowDTO with the selected date
            items.get(index).setB_expiry(selectedDate);

            // You might want to do other actions with the updated value

            // If needed, refresh the table to reflect the changes
            tblvPurInvBatWinCreTableview.refresh();
        });
        // Set up columns
        tblcPurInvBatWinMRP.setCellValueFactory(cellData -> cellData.getValue().mrpProperty());
        tblcPurInvBatWinMRP.setCellFactory(TextFieldTableCell.forTableColumn());
        tblcPurInvBatWinMRP.setOnEditCommit(event -> {
            TranxBatchWindowDTO person = event.getRowValue();
            person.setMrp(event.getNewValue());
            moveFocusToNextCellInBatchWin(event.getTablePosition(), true);
        });
        tblcPurInvBatWinPurRate.setCellValueFactory(cellData -> cellData.getValue().purRateProperty());
        tblcPurInvBatWinPurRate.setCellFactory(TextFieldTableCell.forTableColumn());
        tblcPurInvBatWinPurRate.setOnEditCommit(event -> {
            TranxBatchWindowDTO person = event.getRowValue();
            person.setPurRate(event.getNewValue());
            moveFocusToNextCellInBatchWin(event.getTablePosition(), true);
        });
        tblcPurInvBatWinQty.setCellValueFactory(cellData -> cellData.getValue().b_qtyProperty());
        tblcPurInvBatWinQty.setCellFactory(TextFieldTableCell.forTableColumn());
        tblcPurInvBatWinQty.setOnEditCommit(event -> {
            TranxBatchWindowDTO person = event.getRowValue();
            person.setB_qty(event.getNewValue());
            moveFocusToNextCellInBatchWin(event.getTablePosition(), true);
            costingCalculation();
        });
        tblcPurInvBatWinFreeQty.setCellValueFactory(cellData -> cellData.getValue().b_freeQtyProperty());
        tblcPurInvBatWinFreeQty.setCellFactory(TextFieldTableCell.forTableColumn());
        tblcPurInvBatWinFreeQty.setOnEditCommit(event -> {
            TranxBatchWindowDTO person = event.getRowValue();
            person.setB_freeQty(event.getNewValue());
            moveFocusToNextCellInBatchWin(event.getTablePosition(), true);
            costingCalculation();
        });
        tblcPurInvBatWinDisPer.setCellValueFactory(cellData -> cellData.getValue().b_dis_perProperty());
        tblcPurInvBatWinDisPer.setCellFactory(TextFieldTableCell.forTableColumn());
        tblcPurInvBatWinDisPer.setOnEditCommit(event -> {
            TranxBatchWindowDTO person = event.getRowValue();
            person.setB_dis_per(event.getNewValue());
            moveFocusToNextCellInBatchWin(event.getTablePosition(), true);
            costingCalculation();
        });
        tblcPurInvBatWinDisAmt.setCellValueFactory(cellData -> cellData.getValue().b_dis_amtProperty());
        tblcPurInvBatWinDisAmt.setCellFactory(TextFieldTableCell.forTableColumn());
        tblcPurInvBatWinDisAmt.setOnEditCommit(event -> {
            TranxBatchWindowDTO person = event.getRowValue();
            person.setB_dis_amt(event.getNewValue());
            moveFocusToNextCellInBatchWin(event.getTablePosition(), true);
            costingCalculation();
        });
        tblcPurInvBatWinBarcode.setCellValueFactory(cellData -> cellData.getValue().barcodeProperty());
        tblcPurInvBatWinBarcode.setCellFactory(TextFieldTableCell.forTableColumn());
        tblcPurInvBatWinBarcode.setOnEditCommit(event -> {
            TranxBatchWindowDTO person = event.getRowValue();
            person.setBarcode(event.getNewValue());
            moveFocusToNextCellInBatchWin(event.getTablePosition(), true);
        });
        tblcPurInvBatWinMarginPer.setCellValueFactory(cellData -> cellData.getValue().marginProperty());
        tblcPurInvBatWinMarginPer.setCellFactory(TextFieldTableCell.forTableColumn());
        tblcPurInvBatWinMarginPer.setOnEditCommit(event -> {
            TranxBatchWindowDTO person = event.getRowValue();
            person.setMargin(event.getNewValue());
            moveFocusToNextCellInBatchWin(event.getTablePosition(), true);
            if (!event.getNewValue().isEmpty()) {
                setFSRWithMargin();
            } else {
                moveFocusToNextCellInBatchWin(event.getTablePosition(), true);
            }
        });
        tblcPurInvBatWinFSR.setCellValueFactory(cellData -> cellData.getValue().b_rate_aProperty());
        tblcPurInvBatWinFSR.setCellFactory(TextFieldTableCell.forTableColumn());
        tblcPurInvBatWinFSR.setOnEditCommit(event -> {
            TranxBatchWindowDTO person = event.getRowValue();
            person.setB_rate_a(event.getNewValue());
            if (!event.getNewValue().isEmpty()) {
                validateFSRRate();
            } else {
                moveFocusToNextCellInBatchWin(event.getTablePosition(), true);
            }
        });
        tblcPurInvBatWinCSR.setCellValueFactory(cellData -> cellData.getValue().b_rate_bProperty());
        tblcPurInvBatWinCSR.setCellFactory(TextFieldTableCell.forTableColumn());
        tblcPurInvBatWinCSR.setOnEditCommit(event -> {
            TranxBatchWindowDTO person = event.getRowValue();
            person.setB_rate_b(event.getNewValue());
            if (!event.getNewValue().isEmpty()) {
                validateCSRRate();
            } else {
                moveFocusToNextCellInBatchWin(event.getTablePosition(), true);
            }
        });
        tblcPurInvBatWinSaleRate.setCellValueFactory(cellData -> cellData.getValue().b_rate_cProperty());
        tblcPurInvBatWinSaleRate.setCellFactory(TextFieldTableCell.forTableColumn());
        tblcPurInvBatWinSaleRate.setOnEditCommit(event -> {
            TranxBatchWindowDTO person = event.getRowValue();
            person.setB_rate_c(event.getNewValue());
            if (!event.getNewValue().isEmpty()) {
                validateSaleRate();
            } else {
                moveFocusToNextCellInBatchWin(event.getTablePosition(), true);
            }
        });
        tblcPurInvBatWinAction.setCellFactory(param -> {
            final TableCell<TranxBatchWindowDTO, Void> cell = new TableCell<>() {
                private final Button batchAddButton = new Button("ADD");

                {
                    batchAddButton.setOnAction(event -> {
                        createProductBatch();

                    });
                }

                HBox hbActions = new HBox();

                {
                    hbActions.getChildren().add(batchAddButton);
                    hbActions.setPadding(new Insets(0, 15, 0, 15));
                }

                @Override
                protected void updateItem(Void item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setGraphic(null);
                    } else {
                        setGraphic(hbActions);
                    }
                }

            };
            return cell;
        });

        // Add an initial empty row
        LocalDate constantDate = null;
        LocalDate constantDate1 = LocalDate.now();
        tblvPurInvBatWinCreTableview.getItems().add(new TranxBatchWindowDTO("", constantDate1, constantDate, constantDate, "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", ""));

    }

    private void moveFocusToNextCellInBatchWin(TablePosition<TranxBatchWindowDTO, ?> position, boolean nextColumn) {
        final int row = position.getRow();
        final int col = position.getColumn();

        if (nextColumn) {
            if (col + 1 < tblvPurInvBatWinCreTableview.getColumns().size()) {
                tblvPurInvBatWinCreTableview.getSelectionModel().select(row, tblvPurInvBatWinCreTableview.getColumns().get(col + 1));
                tblvPurInvBatWinCreTableview.edit(row, tblvPurInvBatWinCreTableview.getColumns().get(col + 1));
            } else if (row + 1 < tblvPurInvBatWinCreTableview.getItems().size()) {
                tblvPurInvBatWinCreTableview.getSelectionModel().select(row + 1, tblvPurInvBatWinCreTableview.getColumns().get(0));
                tblvPurInvBatWinCreTableview.edit(row + 1, tblvPurInvBatWinCreTableview.getColumns().get(0));
            }
        }
    }

    //function for move the focus to next field in CMPT row
    private void moveFocusToNextCell(TablePosition<CmpTRowDTO, ?> position, boolean nextColumn) {
        final int row = position.getRow();
        final int col = position.getColumn();

        if (nextColumn) {
            if (col + 1 < tvPurInvCreateCMPTRow.getColumns().size()) {
                tvPurInvCreateCMPTRow.getSelectionModel().select(row, tvPurInvCreateCMPTRow.getColumns().get(col + 1));
                tvPurInvCreateCMPTRow.edit(row, tvPurInvCreateCMPTRow.getColumns().get(col + 1));
            } else if (row + 1 < tvPurInvCreateCMPTRow.getItems().size()) {
                tvPurInvCreateCMPTRow.getSelectionModel().select(row + 1, tvPurInvCreateCMPTRow.getColumns().get(0));
                tvPurInvCreateCMPTRow.edit(row + 1, tvPurInvCreateCMPTRow.getColumns().get(0));
            }
        }
    }

    //function for validation of FSR
    public void validateFSRRate() {
        ObservableList<TranxBatchWindowDTO> tableView2Items1 = tblvPurInvBatWinCreTableview.getItems();
        String mrpText = "";
        String saleRateText = "";
        String costingText = costValue.toString();

        for (TranxBatchWindowDTO person : tableView2Items1) {
            mrpText = person.getMrp();
            saleRateText = person.getB_rate_a();
        }
        double costing = Double.parseDouble(costingText);
        double FSR = Double.parseDouble(saleRateText);
        double MRP = Double.parseDouble(mrpText);

        if (costing <= FSR && FSR <= MRP) {
            moveFocusToNextCellInBatchWin(tblvPurInvBatWinCreTableview.getFocusModel().getFocusedCell(), true);
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning");
            alert.setHeaderText(null);
            alert.setContentText("Sales rate is always between Costing and MRP. Do you want to continue?");

            ButtonType buttonTypeOk = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
            ButtonType buttonTypeCancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
            alert.getButtonTypes().setAll(buttonTypeOk, buttonTypeCancel);


            alert.showAndWait().ifPresent(response -> {
                if (response == buttonTypeOk) {
                    moveFocusToNextCellInBatchWin(tblvPurInvBatWinCreTableview.getFocusModel().getFocusedCell(), true);
                } else if (response == buttonTypeCancel) {
                    System.out.println("Cancel Button Clicked");
                    // Keep focus on the current cell
                    Platform.runLater(() -> {
                        tblvPurInvBatWinCreTableview.requestFocus();
                        tblvPurInvBatWinCreTableview.getSelectionModel().select(tblvPurInvBatWinCreTableview.getFocusModel().getFocusedCell().getRow(), tblvPurInvBatWinCreTableview.getFocusModel().getFocusedCell().getTableColumn());
                        tblvPurInvBatWinCreTableview.scrollTo(tblvPurInvBatWinCreTableview.getFocusModel().getFocusedCell().getRow());
                    });
                }
            });
        }
    }

    //function for validation of CSR rate
    public void validateCSRRate() {
        ObservableList<TranxBatchWindowDTO> tableView2Items1 = tblvPurInvBatWinCreTableview.getItems();
        String mrpText = "";
        String saleRateText = "";
        String costingText = costValue.toString();

        for (TranxBatchWindowDTO person : tableView2Items1) {
            mrpText = person.getMrp();
            saleRateText = person.getB_rate_b();
        }
        double costing = Double.parseDouble(costingText);
        double CSR = Double.parseDouble(saleRateText);
        double MRP = Double.parseDouble(mrpText);

        if (costing <= CSR && CSR <= MRP) {
            moveFocusToNextCellInBatchWin(tblvPurInvBatWinCreTableview.getFocusModel().getFocusedCell(), true);
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning");
            alert.setHeaderText(null);
            alert.setContentText("Sales rate is always between Costing and MRP. Do you want to continue?");

            ButtonType buttonTypeOk = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
            ButtonType buttonTypeCancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
            alert.getButtonTypes().setAll(buttonTypeOk, buttonTypeCancel);


            alert.showAndWait().ifPresent(response -> {
                if (response == buttonTypeOk) {
                    moveFocusToNextCellInBatchWin(tblvPurInvBatWinCreTableview.getFocusModel().getFocusedCell(), true);
                } else if (response == buttonTypeCancel) {
                    // Keep focus on the current cell
                    Platform.runLater(() -> {
                        tblvPurInvBatWinCreTableview.requestFocus();
                        tblvPurInvBatWinCreTableview.getSelectionModel().select(tblvPurInvBatWinCreTableview.getFocusModel().getFocusedCell().getRow(), tblvPurInvBatWinCreTableview.getFocusModel().getFocusedCell().getTableColumn());
                        tblvPurInvBatWinCreTableview.scrollTo(tblvPurInvBatWinCreTableview.getFocusModel().getFocusedCell().getRow());
                    });
                }
            });
        }
    }

    //functio for sale rate
    public void validateSaleRate() {
        ObservableList<TranxBatchWindowDTO> tableView2Items1 = tblvPurInvBatWinCreTableview.getItems();
        String mrpText = "";
        String saleRateText = "";
        String costingText = costValue.toString();

        for (TranxBatchWindowDTO person : tableView2Items1) {
            mrpText = person.getMrp();
            saleRateText = person.getB_rate_c();
        }
        double costing = Double.parseDouble(costingText);
        double saleRate = Double.parseDouble(saleRateText);
        double MRP = Double.parseDouble(mrpText);

        if (costing <= saleRate && saleRate <= MRP) {
            moveFocusToNextCellInBatchWin(tblvPurInvBatWinCreTableview.getFocusModel().getFocusedCell(), true);
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning");
            alert.setHeaderText(null);
            alert.setContentText("Sales rate is always between Costing and MRP. Do you want to continue?");

            ButtonType buttonTypeOk = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
            ButtonType buttonTypeCancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
            alert.getButtonTypes().setAll(buttonTypeOk, buttonTypeCancel);


            alert.showAndWait().ifPresent(response -> {
                if (response == buttonTypeOk) {
                    moveFocusToNextCellInBatchWin(tblvPurInvBatWinCreTableview.getFocusModel().getFocusedCell(), true);
                } else if (response == buttonTypeCancel) {
                    // Keep focus on the current cell
                    Platform.runLater(() -> {
                        tblvPurInvBatWinCreTableview.requestFocus();
                        tblvPurInvBatWinCreTableview.getSelectionModel().select(tblvPurInvBatWinCreTableview.getFocusModel().getFocusedCell().getRow(), tblvPurInvBatWinCreTableview.getFocusModel().getFocusedCell().getTableColumn());
                        tblvPurInvBatWinCreTableview.scrollTo(tblvPurInvBatWinCreTableview.getFocusModel().getFocusedCell().getRow());
                    });
                }
            });
        }
    }

    //function to move next column
    private void moveFocusToNextRow(TablePosition<CmpTRowDTO, ?> position) {
        final int row = position.getRow();

        if (row + 1 < tvPurInvCreateCMPTRow.getItems().size()) {
            tvPurInvCreateCMPTRow.getSelectionModel().select(row + 1, tvPurInvCreateCMPTRow.getColumns().get(0));
            tvPurInvCreateCMPTRow.edit(row + 1, tvPurInvCreateCMPTRow.getColumns().get(0));
        }
    }

    //function for calculation of costing
    private void costingCalculation() {       //function for calculation
        ObservableList<TranxBatchWindowDTO> tableView2Items1 = tblvPurInvBatWinCreTableview.getItems();
        String b_qtyText = "";
        String b_freeQtyText = "";
        String b_dis_amtText = "";
        String b_dis_perText = "";
        String b_rateText = "";
        for (TranxBatchWindowDTO person : tableView2Items1) {
            b_qtyText = person.getB_qty();
            b_freeQtyText = person.getB_freeQty();
            b_dis_amtText = person.getB_dis_amt();
            b_dis_perText = person.getB_dis_per();
            b_rateText = person.getPurRate();
        }
        String tax_perText = prdBatchTaxPer;

        Double b_qty = Double.parseDouble(b_qtyText);
        Double b_freeQty = b_freeQtyText.isEmpty() ? 0 : Double.parseDouble(b_freeQtyText);
        Double totalQty = b_qty + b_freeQty;

        Double disPerAmt = 0.0;
        Double totaldisAmt = 0.0;

        Double b_rate = Double.parseDouble(b_rateText); // You'll need to get b_rate value from somewhere
        Double disPer = b_dis_perText.isEmpty() ? 0 : Double.parseDouble(b_dis_perText);
        disPerAmt = (disPer * b_rate) / 100;
        totaldisAmt += disPerAmt;

        Double disAmt = b_dis_amtText.isEmpty() ? 0 : Double.parseDouble(b_dis_amtText);
        totaldisAmt += disAmt;

        Double totalAmt = b_rate * b_qty;

        Double costVal = (totalAmt - totaldisAmt) / totalQty;
        costValue = costVal;


        Double taxAmt = (Double.parseDouble(tax_perText) * totalAmt) / 100;

        Double costWithTax = costVal + (taxAmt / totalQty);
        costWithTaxValue = costWithTax;


        lblPurInvBatWinCWOT.setText(String.format("%.3f", costVal));
        lblPurInvBatWinCWT.setText(String.format("%.3f", costWithTax));

    }

    //function for create batch
    private void createProductBatch() {

        ObservableList<TranxBatchWindowDTO> tableView2Items1 = tblvPurInvBatWinCreTableview.getItems();
        String b_no = "";
        String purDate = "";
        String mfgDate = "";
        String expDate = "";
        String mrp = "";
        String purRate = "";
        String qty = "";
        String freeQty = "";
        String disPer = "";
        String disAmt = "";
        String barcode = "";
        String margin = "";
        String fsr = "";
        String csr = "";
        String saleRate = "";
        String level_a_id = "";
        String level_b_id = "";
        String level_c_id = "";
        String unit_id = String.valueOf(1);
        String supplier_id = "";
        String b_details_id = String.valueOf(0);
        for (TranxBatchWindowDTO person : tableView2Items1) {

            b_no = person.getB_no();
            purDate = String.valueOf(person.getPur_date());
            mfgDate = String.valueOf(person.getManufacturing_date());
            expDate = String.valueOf(person.getB_expiry());
            mrp = person.getMrp();
            purRate = person.getPurRate();
            qty = person.getB_qty();
            freeQty = person.getB_freeQty();
            disPer = person.getB_dis_per();
            disAmt = person.getB_dis_amt();
            barcode = person.getBarcode();
            margin = person.getMargin();
            fsr = person.getB_rate_a();
            csr = person.getB_rate_b();
            saleRate = person.getB_rate_c();

        }

        Map<String, String> map = new HashMap<>();

        map.put("product_id", selectedRowProductdId);
        map.put("level_a_id", level_a_id);
        map.put("level_b_id", level_b_id);
        map.put("level_c_id", level_c_id);
        map.put("unit_id", unit_id);
        map.put("supplier_id", supplier_id);
        map.put("b_cess_per", "");
        map.put("b_cess_amt", "");
        map.put("b_no", b_no);
        map.put("purDate", purDate);
        map.put("mfgDate", mfgDate);
        map.put("expDate", expDate);
        map.put("mrp", mrp);
        map.put("b_rate", purRate);
        map.put("qty", qty);
        map.put("freeQty", freeQty);
        map.put("disPer", disPer);
        map.put("disAmt", disAmt);
        map.put("barcode", barcode);
        map.put("margin", margin);
        map.put("b_rate_a", fsr);
        map.put("b_rate_b", csr);
        map.put("b_rate_c", saleRate);
        map.put("costing", costValue.toString());
        map.put("costingWithTax", costWithTaxValue.toString());
        map.put("b_details_id", b_details_id);
        HttpResponse<String> response;
        String formData = Globals.mapToStringforFormData(map);
        System.out.println("formData Batch" + formData);
        response = APIClient.postFormDataRequest(formData, "create_batch_details");
        JsonObject responseBody = new Gson().fromJson(response.body(), JsonObject.class);
        System.out.println("responseBody Batch" + responseBody);

        if (responseBody.get("responseStatus").getAsInt() == 200) {
            //TODO : use util for alert messages
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Success");
            alert.setHeaderText("Batch Created Successfully");
            alert.show();
            PauseTransition delay = new PauseTransition(Duration.seconds(4));
            delay.setOnFinished(event -> alert.close());
            delay.play();

            //  Add an initial empty row
            LocalDate constantDate = null;

            hbapPurInvBatchWindow.setVisible(false);

        }
        System.out.println("selectedRowIndex" + selectedRowIndex);

        CmpTRowDTO selectedItem = tvPurInvCreateCMPTRow.getItems().get(selectedRowIndex);


        selectedItem.setRate(purRate);
        selectedItem.setQuantity(qty);
        selectedItem.setDis_amt(disAmt);
        selectedItem.setDis_per(disPer);

        Platform.runLater(() -> {
            tvPurInvCreateCMPTRow.refresh();
        });
        rowCalculation(selectedRowIndex);

    }

    private void roundOffCalculations() {
        chbPurInvROff.setOnAction(e -> {

            Double billamt = 0.00; // local variable to store row wise  Bill amount
            Double total_bill_amt = 0.00; // local variable to store Final bill Amount without roundoff
            Double final_r_off_bill_amt = 0.00; // local variable to store Final Bill amount with round off
            Double roundOffAmt = 0.00; // local variable to store Roundoff Amount
            for (CmpTRowDTO cmpTRowDTO : tvPurInvCreateCMPTRow.getItems()) {
                billamt = Double.valueOf(cmpTRowDTO.getNet_amt());
                total_bill_amt += billamt;
            }
            final_r_off_bill_amt = (double) Math.round(total_bill_amt);
            roundOffAmt = final_r_off_bill_amt - total_bill_amt;

            lblPurROff.setText(String.format("%.2f", roundOffAmt));
            if (chbPurInvROff.isSelected()) {
                lblPurRtnTotalBillAmt.setText(String.format("%.2f", final_r_off_bill_amt));
                System.out.println("total_bill_amt :: " + total_bill_amt);
                System.out.println("roundOffAmt :: " + final_r_off_bill_amt);
            } else {
                lblPurRtnTotalBillAmt.setText(String.format("%.2f", total_bill_amt));
                System.out.println("Checkbox Is False");
            }
        });
    }

    private void discountPropotionalCalculation() {

        //get discount percentage and discount amount to textfield
        String disproportionalPer = tfPurRtnDisPer.getText();
        String disproportionalAmt = tfPurRtnDisAmt.getText();
        String additionalCharges = tfPurInvoiceAddTotalAmt.getText();
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

        System.out.println("total Row Size:" + tvPurInvCreateCMPTRow.getItems().size());
        //calculate total taxable amount
        for (int i = 0; i < tvPurInvCreateCMPTRow.getItems().size(); i++) {
            System.out.println("Gross amt in Prop--->" + tvPurInvCreateCMPTRow.getItems().get(i).getGross_amt());
            rowCalculation(i);//call row calculation function
            CmpTRowDTO cmpTRowDTO1 = tvPurInvCreateCMPTRow.getItems().get(i);
            totalTaxableAmt = totalTaxableAmt + cmpTRowDTO1.getTaxable_amt();

        }
        System.out.println("totalTaxableAmt discountPropotionalCalculation :: " + totalTaxableAmt);

        Double rowDisc = 0.0;
        //get data of netamount, tax per,and row taxable amount from cmptrow
        for (CmpTRowDTO cmpTRowDTO : tvPurInvCreateCMPTRow.getItems()) {
            System.out.println("getFinal_dis_amt--->" + cmpTRowDTO.getFinal_dis_amt());
            rowDisc = cmpTRowDTO.getFinal_dis_amt();
            System.out.println("netAmt--->" + cmpTRowDTO.getNet_amt());
            netAmt = Double.parseDouble(cmpTRowDTO.getNet_amt());
            System.out.println("netAmt--->" + cmpTRowDTO.getTax_per());
            r_tax_per = Double.parseDouble(cmpTRowDTO.getTax_per());
            rowTaxableAmt = cmpTRowDTO.getTaxable_amt();
            System.out.println("rowTaxableAmt discountPropotionalCalculation :: " + rowTaxableAmt + "netAmt" + netAmt);

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

            //calculate discount proportional amount and  the taxable amount

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
            cmpTRowDTO.setNet_amt(String.format("%.2f", netAmt));
            cmpTRowDTO.setTotal_taxable_amt(totalTaxableAmtAdditional);
            System.out.println("Total Tax Amt--->" + total_tax_amt);
            cmpTRowDTO.setCgst(total_tax_amt / 2);
            cmpTRowDTO.setSgst(total_tax_amt / 2);
            cmpTRowDTO.setFinTaxAmt(total_tax_amt);
            cmpTRowDTO.setFinal_dis_amt(rowDisc);
            Platform.runLater(() -> {
                tvPurInvCreateCMPTRow.refresh();
            });
            //Display net Amount to table view
            tblcPurInvCreateCmptNetAmt.setCellValueFactory(new PropertyValueFactory<>("net_amt"));

        }
        //Check additional charge is Empty or not if not empty call to the additional charges function
        if (!tfPurInvoiceAddTotalAmt.getText().isEmpty()) {
            additionalChargesCalculation();
        } /*else {
            System.out.println("Additional Charges is empty");
            additionalChargesCalculation();
        }*/
        System.out.println("from discountPropotionalCalculation");
        calculateGst();
    }


    //Calculate CGST, SGST, and IGST as per Tax Percentage
    private void calculateGst() {

        Map<Double, Double> cgstTotals = new HashMap<>(); //used to merge same cgst Percentage
        Map<Double, Double> sgstTotals = new HashMap<>();//used to merge same sgst Percentage

//        tblvPurChallGST.getItems().clear();

        // Initialize totals to zero for each tax percentage
        for (Double taxPercentage : new Double[]{3.0, 5.0, 12.0, 18.0, 28.0}) {
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
        for (CmpTRowDTO cmpTRowDTO : tvPurInvCreateCMPTRow.getItems()) {
            taxPercentage = Double.parseDouble(cmpTRowDTO.getTax_per());
            Double quantity = Double.parseDouble(cmpTRowDTO.getQuantity());
            if (cmpTRowDTO.getFree_qty().isEmpty()) {
                freeQuantity = 0.0;
            } else {
                freeQuantity = Double.parseDouble(cmpTRowDTO.getFree_qty());
            }

            // Total Calculations of each IGST, SGST, CGST
            totalQuantity += quantity;
            totalFreeQuantity += freeQuantity;
            Double igst = cmpTRowDTO.getIgst();
            Double cgst = cmpTRowDTO.getCgst();
            Double sgst = cmpTRowDTO.getSgst();
            totalFinalIgst += igst;
            totalFinalSgst += sgst;
            totalFinalCgst += cgst;

            cgstTotals.put(taxPercentage, cgstTotals.get(taxPercentage) + cgst);
            sgstTotals.put(taxPercentage, sgstTotals.get(taxPercentage) + sgst);

            //Total Calculation of gross amt ,taxable ,tax,discount
            netAmount = Double.parseDouble(cmpTRowDTO.getNet_amt());
            totalNetAmt += netAmount;
            grossAmt = Double.parseDouble(cmpTRowDTO.getGross_amt());
            totalGrossAmt += grossAmt;
            taxableAmt = cmpTRowDTO.getTaxable_amt();
            totaltaxableAmt += taxableAmt;
            disAmt = cmpTRowDTO.getFinal_dis_amt();
            totalDisAmt += disAmt;
            taxAmt = cmpTRowDTO.getFinTaxAmt();
            totalTaxAmt += taxAmt;
        }


        // Print or display totals of Each GST percentage Amount in GST Table
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
            tblcPurInvoiceGST.setCellValueFactory(new PropertyValueFactory<>("taxPer"));
            tblcPurInvoiceCGST.setCellValueFactory(new PropertyValueFactory<>("cgst"));
            tblcPurInvoiceSGST.setCellValueFactory(new PropertyValueFactory<>("sgst"));
        }
        tblvPurInvoiceGST.setItems(observableList);
        Platform.runLater(() -> {
            tblvPurInvoiceGST.refresh();

        });
        // Lable Set To dispaly Amounts
        lblPurRtnTotalQty.setText(String.valueOf(totalQuantity));
        lblPurRtnFreeQty.setText(String.valueOf(totalFreeQuantity));
        lblPurRtnTotalBillAmt.setText(String.format("%.2f", totalNetAmt));
        lblPurRtnTotalRowGross.setText(String.format("%.2f", totalGrossAmt));
        lblPurRtnTotalDisc.setText(String.format("%.2f", totalDisAmt));
        lblPurRtnTaxableAmt.setText(String.format("%.2f", (totalGrossAmt - totalDisAmt)));
        lblPurRtnTotalTax.setText(String.format("%.2f", totalTaxAmt));
        lblPurInvtotalSGST.setText(String.format("%.2f", totalFinalSgst));
        lblPurInvtotalCGST.setText(String.format("%.2f", totalFinalCgst));

    }

    public void setFSRWithMargin() {
        ObservableList<TranxBatchWindowDTO> tableView2Items1 = tblvPurInvBatWinCreTableview.getItems();
        String mrpText = "";
        String purchaseRateText = "";
        String marginText = "";

        String saleRate = "";
        for (TranxBatchWindowDTO person : tableView2Items1) {
            mrpText = person.getMrp();
            purchaseRateText = person.getPurRate();
            marginText = person.getMargin();
            saleRate = person.getB_rate_c();
        }
        String costingText = costValue.toString();

        double costing = Double.parseDouble(costingText);
        double margin = Double.parseDouble(marginText);
        double mrp = Double.parseDouble(mrpText);
        double purchaseRate = Double.parseDouble(purchaseRateText);
        int configDecimalPlaces = 2;
        double costingPer = roundDigit((costing / 100) * margin, configDecimalPlaces);
        System.out.println("Sales Rate: " + (costingPer + costing));
        System.out.println("Costing Per: " + costingPer);

        double saleRateFSR = costingPer + costing;
        String b_rate_aFSR = String.format("%.2f", saleRateFSR);
        System.out.println("Sale Rate FSR: " + saleRateFSR);


        int rowIndex = tblvPurInvBatWinCreTableview.getSelectionModel().getSelectedIndex();
        TranxBatchWindowDTO selectedItem = tblvPurInvBatWinCreTableview.getItems().get(rowIndex);

        // Verify data
        System.out.println("New Sale Rate: " + saleRate);
        selectedItem.setB_rate_a(b_rate_aFSR);

        // Update UI on JavaFX Application Thread
        Platform.runLater(() -> {
            tblvPurInvBatWinCreTableview.refresh();
        });


    }

    //function for identify duplicate invoice number
    private void validatePurchaseInvoice() {
        try {
            Map<String, String> map = new HashMap<>();
            map.put("supplier_id", ledgerId);
            map.put("bill_no", tfPurRtnInvNum.getText());

            String formData = Globals.mapToStringforFormData(map);
            HttpResponse<String> response = APIClient.postFormDataRequest(formData, EndPoints.VALIDATE_PUR_INV_NUMBER);
            String resBody = response.body();
            JsonObject obj = new Gson().fromJson(resBody, JsonObject.class);
            if (obj.get("responseStatus").getAsInt() == 409) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(obj.get("message").getAsString());

                alert.showAndWait();
                alert.show();
                tfPurRtnInvNum.setText("");
                tfPurRtnInvNum.requestFocus();
            }
        } catch (Exception e) {
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            String exceptionAsString = sw.toString();
            purInvCreateLogger.error("Exception in Pur Return validatePurchaseInvoice():" + exceptionAsString);
        }

    }

    //FUNCTION FOR CHECKING THE ENTERED INVOICE DATE IS BETWEEN FINANCIAL YEAR OR NOT
    public void checkInvoiceDateIsBetweenFYFun(String newValue) {

        Map<String, String> map = new HashMap<>();
        if (tfPurRtnInvDate != null && tfPurRtnInvDate.getText() != null && !tfPurRtnInvDate.getText().isEmpty()) {
            map.put("invoiceDate", Communicator.text_to_date.fromString(tfPurRtnInvDate.getText()).toString());
        }
//        map.put("invoiceDate", tfPurRtnInvDate.getText());
        String formData = Globals.mapToStringforFormData(map);
        HttpResponse<String> resp = APIClient.postFormDataRequest(formData, EndPoints.CHECK_INV_DATE_IS_BETWEEN_FY);
        String resBody = resp.body();
        JsonObject jsonObject = new Gson().fromJson(resBody, JsonObject.class);
        if (jsonObject.get("responseStatus").getAsInt() == 404) {
            /*Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Error");
            alert.setHeaderText("Invoice date not valid as per Fiscal year");
            alert.showAndWait();
            alert.show();*/
            AlertUtility.CustomCallback callback = number -> {
                if (number == 0)
                    tfPurRtnInvDate.clear();
                tfPurRtnInvDate.requestFocus();
            };
            AlertUtility.AlertError(Communicator.stage, AlertUtility.alertTypeError, "Invoice date not valid as per Fiscal year", callback);

        } else {
            cmbPurRtnPurAC.requestFocus();
        }

    }

    //function for additional charges calculation
    private void additionalChargesCalculation() {

        //get data from additional charges text field
        String additionalCharges = tfPurInvoiceAddTotalAmt.getText();

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

            //calaculate total taxable amount
            for (int i = 0; i < tvPurInvCreateCMPTRow.getItems().size(); i++) {
                CmpTRowDTO cmpTRowDTO1 = tvPurInvCreateCMPTRow.getItems().get(i);
                totalTaxableAmt = totalTaxableAmt + cmpTRowDTO1.getTotal_taxable_amt();

            }


            System.out.println("additionalChargeAmt totalTaxableAmt" + totalTaxableAmt);
            //get all table view row data and add additinal charges to net amount
            for (CmpTRowDTO cmpTRowDTO : tvPurInvCreateCMPTRow.getItems()) {

                netAmt = Double.parseDouble(cmpTRowDTO.getNet_amt());
                r_tax_per = Double.parseDouble(cmpTRowDTO.getTax_per());
                rowTaxableAmt = cmpTRowDTO.getTotal_taxable_amt();


                totalDisPropAmt = additionalChargeAmt;
                rowDisPropAmt = (rowTaxableAmt * totalDisPropAmt) / totalTaxableAmt;
                rowTaxableAmt = rowTaxableAmt + rowDisPropAmt;
                System.out.println("rowTaxableAmt" + rowTaxableAmt + "rowDisPropAmt" + rowDisPropAmt + "totalDisPropAmt" + totalDisPropAmt);

                //calculate tax proportional
                if (r_tax_per > 0) {
                    total_tax_amt = (r_tax_per / 100) * (rowTaxableAmt);
                    System.out.println("additionalChargeAmt total_tax_amt" + total_tax_amt);
                    netAmt = rowTaxableAmt + total_tax_amt;
                    System.out.println("total additionalChargeAmt  :" + netAmt + "" + rowTaxableAmt);
                }
                //Set data to CmpTrow
                cmpTRowDTO.setNet_amt(String.format("%.3f", netAmt));
                cmpTRowDTO.setCgst(total_tax_amt / 2);
                cmpTRowDTO.setSgst(total_tax_amt / 2);
                Platform.runLater(() -> {
                    tvPurInvCreateCMPTRow.refresh();
                });
                tblcPurInvCreateCmptNetAmt.setCellValueFactory(new PropertyValueFactory<>("net_amt"));

            }

        }
        //call calculate gst function
        calculateGst();

    }

    private void setLedgerGSTDATA(ObservableList<GstDetailsDTO> gstDetails) {
        if (gstDetails != null) {

            cmbPurRtnLedgerGst.setItems(gstDetails);
            cmbPurRtnLedgerGst.setConverter(new StringConverter<GstDetailsDTO>() {
                @Override
                public String toString(GstDetailsDTO gstDetailsDTO) {
                    if (gstDetailsDTO != null) return gstDetailsDTO.getGstNo();
                    else return "";
                }

                @Override
                public GstDetailsDTO fromString(String s) {
                    return null;
                }
            });
            GstDetailsDTO selectedGst = null;
            for (Object obj : cmbPurRtnLedgerGst.getItems()) {
                GstDetailsDTO gstDetailsDTO = (GstDetailsDTO) obj;
                if (gstDetailsDTO.getId().equals(gstDetails.get(0).getId())) {
                    selectedGst = gstDetailsDTO;
                    break;
                }
            }
            if (selectedGst != null) {
                cmbPurRtnLedgerGst.getSelectionModel().select(selectedGst);
            }
        }
    }

    private void getTranxRtnProductList(Stage stage, String billId) {

        SingleInputDialogs.openTranxRtnProductList(billId, stage, "Select Products", (input1, input2) -> {

            purInvCreateLogger.debug("Callback to get all the Tranx purchase product details of the selected invoice number");
            /**** set api response to the input fields of the invoice data *****/
            int index = 0;
            /**** END *****/
            tvPurInvCreateCMPTRow.getItems().clear();
            Double rowTotalDis = 0.0;
            Double totalTax = 0.0;
            if (input1.size() > 0) {
                for (TranxPurRowResEditDTO mList : input1) {
                    purInvCreateLogger.debug("For " + mList.getProductName() + " setting up the row value started");
                    System.out.println("For " + mList.getProductName() + " setting up the row value started");
                    newRow = new CmpTRowDTO();
                    try {
                        newRow.setProduct_id("" + mList.getProductId());
                        purInvCreateLogger.info("ProductName->" + mList.getProductName());
                        System.out.println("ProductName->" + mList.getProductName());
                        newRow.setParticulars(mList.getProductName());
                        purInvCreateLogger.info("Packing->" + mList.getPacking());
                        System.out.println("Packing->" + mList.getPacking());
                        newRow.setPacking(mList.getPacking());
                        purInvCreateLogger.info("Unit->" + mList.getUnitName());
                        System.out.println("Unit->" + mList.getUnitName());
                        newRow.setUnit(mList.getUnitName());
                        purInvCreateLogger.info("Batch No->" + mList.getBatchNo());
                        System.out.println("Batch No->" + mList.getBatchNo());
                        newRow.setBatchNo(mList.getBatchNo());
                        purInvCreateLogger.info("Qty->" + mList.getQty());
                        System.out.println("Qty->" + mList.getQty());
                        newRow.setQuantity("" + mList.getQty());
                        purInvCreateLogger.info("Rate->" + mList.getRate());
                        System.out.println("Rate->" + mList.getRate());
                        newRow.setRate("" + mList.getRate());
                        purInvCreateLogger.info("Gross Amt->" + (mList.getRate() * mList.getQty()));
                        System.out.println("Gross Amt->" + (mList.getRate() * mList.getQty()));
                        newRow.setGross_amt("" + (mList.getRate() * mList.getQty()));
                        purInvCreateLogger.info("Dis Per->" + mList.getDisPer());
                        System.out.println("Dis Per->" + mList.getDisPer());
                        newRow.setDis_per("" + mList.getDisPer());
                        purInvCreateLogger.info("Dis Amt->" + mList.getDisAmt());
                        System.out.println("Dis Amt->" + mList.getDisAmt());
                        newRow.setDis_amt("" + mList.getDisAmt());
                        purInvCreateLogger.info("GST->" + mList.getDisAmt());
                        System.out.println("GST->" + mList.getDisAmt());
                        newRow.setGst(mList.getGst());
                        purInvCreateLogger.info("Final Amt->" + mList.getFinalAmt());
                        System.out.println("Final Amt->" + mList.getFinalAmt());
                        newRow.setNet_amt("" + mList.getFinalAmt());
                        purInvCreateLogger.info("Free Qty->" + mList.getFreeQty());
                        System.out.println("Free Qty->" + mList.getFreeQty());
                        newRow.setFree_qty(String.valueOf(mList.getFreeQty()));
                        purInvCreateLogger.info("IGST ->" + mList.getIgst());
                        System.out.println("IGST ->" + mList.getIgst());
                        newRow.setIgst(mList.getIgst());
                        purInvCreateLogger.info("SGST ->" + mList.getSgst());
                        System.out.println("SGST ->" + mList.getSgst());
                        newRow.setSgst(mList.getSgst());
                        purInvCreateLogger.info("CGST ->" + mList.getCgst());
                        System.out.println("CGST ->" + mList.getCgst());
                        newRow.setCgst(mList.getCgst());
                        purInvCreateLogger.info("Total IGST ->" + mList.getTotalIgst());
                        System.out.println("Total IGST ->" + mList.getTotalIgst());
                        newRow.setTotal_igst(mList.getTotalIgst());
                        purInvCreateLogger.info("Total SGST ->" + mList.getTotalSgst());
                        System.out.println("Total SGST ->" + mList.getTotalSgst());
                        newRow.setTotal_sgst(mList.getTotalSgst());
                        purInvCreateLogger.info("Total CGST ->" + mList.getTotalCgst());
                        System.out.println("Total CGST ->" + mList.getTotalCgst());
                        newRow.setTotal_cgst(mList.getTotalCgst());
                        purInvCreateLogger.info("Tax Per ->" + mList.getGst());
                        System.out.println("Tax Per ->" + mList.getGst());
                        newRow.setTax_per(String.valueOf(mList.getGst()));
                        purInvCreateLogger.info("Taxable Amt ->" + mList.getGrossAmt());
                        System.out.println("Taxable Amt ->" + mList.getGrossAmt());
                        newRow.setTaxable_amt(mList.getGrossAmt());
                        System.out.println("Row Discount--->" + mList.getRowDisAmt());
                        rowTotalDis += mList.getRowDisAmt();
                        newRow.setFinal_dis_amt(rowTotalDis);
                        System.out.println("total final Discount--->" + rowTotalDis);
                        //calculate tax per and store the data to final net AMount
                        if (mList.getGst() > 0) {
                            Double total_tax_amt = (mList.getGst() / 100) * (mList.getGrossAmt());
                            totalTax += total_tax_amt;
                            System.out.println("row total  Tax-->" + totalTax);
                            newRow.setFinTaxAmt(total_tax_amt);
                        }
                        //  tvPurInvCreateCMPTRow.getItems().add(newRow);
                        newRowList.add(newRow);
                        System.out.println("Gross amt--->" + newRowList.get(index).getGross_amt());
                        index++;
                        purInvCreateLogger.debug("For " + mList.getProductName() + " setting up the row value ended");
                        System.out.println("For " + mList.getProductName() + " setting up the row value ended");
                    } catch (Exception e) {
                        System.out.println("Exception---->" + e.getMessage());
                    }
                }
                tfPurRtnInvDate.requestFocus();
            } else {
                tfPurInvLedgName.requestFocus();
            }
            tvPurInvCreateCMPTRow.getItems().addAll(newRowList);
            tvPurInvCreateCMPTRow.refresh();
            if (input2 != null) {
                tfPurRtnDisPer.setText("" + input2.getDiscountInPer());
                tfPurRtnDisAmt.setText("" + input2.getDiscountInAmt());
            } else {
                tfPurRtnDisPer.setText("0.0");
                tfPurRtnDisAmt.setText("0.0");
            }
        });
    }

    // round off Calculations after Clicking on RoundOff Checkbox
    public void roundoffSelected(ActionEvent actionEvent) {

        Double billamt = 0.00; // local variable to store row wise  Bill amount
        Double total_bill_amt = 0.00; // local variable to store Final bill Amount without roundoff
        Double final_r_off_bill_amt = 0.00; // local variable to store Final Bill amount with round off
        Double roundOffAmt = 0.00; // local variable to store Roundoff Amount
        for (CmpTRowDTO cmpTRowDTO : tvPurInvCreateCMPTRow.getItems()) {
            billamt = Double.valueOf(cmpTRowDTO.getNet_amt());
            total_bill_amt += billamt;
        }
        final_r_off_bill_amt = (double) Math.round(total_bill_amt);
        roundOffAmt = final_r_off_bill_amt - total_bill_amt;

        lblPurROff.setText(String.format("%.2f", roundOffAmt));
        if (chbPurInvROff.isSelected()) {
            lblPurRtnTotalBillAmt.setText(String.format("%.2f", final_r_off_bill_amt));
            System.out.println("total_bill_amt :: " + total_bill_amt);
            System.out.println("roundOffAmt :: " + final_r_off_bill_amt);
        } else {
            lblPurRtnTotalBillAmt.setText(String.format("%.2f", total_bill_amt));
            System.out.println("Checkbox Is False");
        }
    }

    public void onSubmit(ActionEvent actionEvent) {
        createPurchaseReturn();
    }

    public void createPurchaseReturn() {
        Double cgst = 0.00;
        Double totalCgst = 0.00;
        Double sgst = 0.00;
        Double totalSgst = 0.00;
        Double totalIgst = 0.00;
        for (GstDTO gstdto : tblvPurInvoiceGST.getItems()) {
            cgst = Double.valueOf(gstdto.getCgst());
            totalCgst += cgst;
            sgst = Double.valueOf(gstdto.getCgst());
            totalSgst += sgst;
            totalIgst = totalSgst + totalCgst;
        }

        // FormData for Create API

        Map<String, String> map = new HashMap<>();
        if (tfPurInvCreateTranxDate != null && tfPurInvCreateTranxDate.getText() != null &&
                !tfPurInvCreateTranxDate.getText().isEmpty()) {
            map.put("transaction_date", Communicator.text_to_date.fromString(tfPurInvCreateTranxDate.getText()).toString());
        }
        if (tfPurRtnInvDate != null && tfPurRtnInvDate.getText() != null &&
                !tfPurRtnInvDate.getText().isEmpty()) {
            map.put("purchase_return_date", Communicator.text_to_date.fromString(tfPurRtnInvDate.getText()).toString());
        }
        map.put("newReference", "false");
        map.put("pur_return_invoice_no", tfPurRtnInvNum.getText());
        map.put("purchase_account_id", cmbPurRtnPurAC.getValue().getId());
        map.put("purchase_return_sr_no", tfPurRtnsr.getText());
        map.put("source", "pur_invoice");//
        map.put("pur_invoice_id", "7");//
        map.put("supplier_code_id", ledgerId);
        map.put("isRoundOffCheck", String.valueOf(chbPurInvROff.isSelected()));
        map.put("roundoff", lblPurROff.getText());
        map.put("totalamt", lblPurRtnTotalBillAmt.getText());
        map.put("gstNo", cmbPurRtnLedgerGst.getValue() != null ? cmbPurRtnLedgerGst.getValue().getGstNo() : "");
        map.put("totalcgst", String.valueOf(totalCgst));
        map.put("totalsgst", String.valueOf(totalSgst));
        map.put("totaligst", String.valueOf(totalIgst));
        map.put("tcs_mode", "");
        if (tfPurRtnDisPer.getText().isEmpty())
            map.put("purchase_discount", "0.0");
        else
            map.put("purchase_discount", tfPurRtnDisPer.getText());
        if (tfPurRtnDisAmt.getText().isEmpty())
            map.put("purchase_discount_amt", "0.0");
        else
            map.put("purchase_discount_amt", tfPurRtnDisAmt.getText());
        map.put("total_purchase_discount_amt", lblPurRtnTotalDisc.getText());
        map.put("additionalChargesTotal", "0");
        map.put("additionalCharges", "[]");
        map.put("total_qty", lblPurRtnTotalQty.getText());
        map.put("total_free_qty", lblPurRtnFreeQty.getText());
        map.put("total_row_gross_amt", lblPurRtnTotalRowGross.getText());
        map.put("total_base_amt", lblPurRtnTotalRowGross.getText());
        map.put("total_invoice_dis_amt", lblPurRtnTotalDisc.getText());
        map.put("taxable_amount", lblPurRtnTaxableAmt.getText());
        map.put("total_tax_amt", lblPurRtnTotalTax.getText());
        map.put("bill_amount", lblPurRtnTotalBillAmt.getText());
        Boolean taxFlag = false;
        JsonArray jsonArray = new JsonArray();
        JsonObject jsonObject1 = new JsonObject();
        if (ledgerStateCode.equalsIgnoreCase(GlobalTranx.getCompanyStateCode().toString())) {
            taxFlag = true;
            for (GstDTO gstDTO : tblvPurInvoiceGST.getItems()) {
                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("d_gst", decimalFormat.format(Double.parseDouble(gstDTO.getTaxPer())));
                jsonObject.addProperty("gst", decimalFormat.format(Double.parseDouble(gstDTO.getTaxPer()) / 2));
                jsonObject.addProperty("amt", gstDTO.getCgst());
                jsonArray.add(jsonObject);
            }
            JsonObject jsonObject = new JsonObject();
            jsonObject.add("cgst", jsonArray);
            jsonObject.add("sgst", jsonArray);
            map.put("taxCalculation", jsonObject.toString());
        }else{
            taxFlag = false;
//            jsonObject.addProperty("d_gst", decimalFormat.format(taxPer));
            JsonObject jsonObject = new JsonObject();
            /*jsonObject.addProperty("gst", decimalFormat.format(taxPer));
            jsonObject.addProperty("amt", decimalFormat.format(Double.parseDouble(lbSalesQuotCreateTax.getText())));*/
            jsonArray.add(jsonObject);
            jsonObject1.add("igst", jsonArray);
            map.put("taxCalculation", jsonObject1.toString());
        }
        map.put("taxFlag", String.valueOf(taxFlag));// static data as of now
        map.put("paymentMode", paymentMode);
        // taxCalulation Array adding in the formdata



        ArrayList<TranxPurRowDTO> rowData = new ArrayList<>();
        for (CmpTRowDTO cmpTRowDTO : tvPurInvCreateCMPTRow.getItems()) {
            TranxPurRowDTO purParticularRow = new TranxPurRowDTO();
            purParticularRow.setProductId(cmpTRowDTO.getProduct_id());
            if (cmpTRowDTO.getLevel_a_id() != null && !cmpTRowDTO.getLevel_a_id().isEmpty()) {
                purParticularRow.setLevelaId(cmpTRowDTO.getLevel_a_id());
            } else {
                purParticularRow.setLevelaId("");
            }
            if (cmpTRowDTO.getLevel_b_id() != null && !cmpTRowDTO.getLevel_b_id().isEmpty()) {
                purParticularRow.setLevelbId(cmpTRowDTO.getLevel_b_id());
            } else {
                purParticularRow.setLevelbId("");
            }
            if (cmpTRowDTO.getLevel_c_id() != null && !cmpTRowDTO.getLevel_c_id().isEmpty()) {
                purParticularRow.setLevelcId(cmpTRowDTO.getLevel_c_id());
            } else {
                purParticularRow.setLevelcId("");
            }
            if (!cmpTRowDTO.getUnit().isEmpty()) {
                purParticularRow.setUnitId(cmpTRowDTO.getUnit());
            } else {
                purParticularRow.setUnitId("1");
            }
            if (cmpTRowDTO.getUnit_conv() != null) {
                purParticularRow.setUnitConv(String.valueOf(cmpTRowDTO.getUnit_conv()));
            } else {
                purParticularRow.setUnitConv("0");
            }
            if (!cmpTRowDTO.getQuantity().isEmpty()) {
                purParticularRow.setQty(cmpTRowDTO.getQuantity());
            } else {
                purParticularRow.setQty("0");
            }
            if (!cmpTRowDTO.getFree_qty().isEmpty()) {
                purParticularRow.setFreeQty(cmpTRowDTO.getFree_qty());
            } else {
                purParticularRow.setFreeQty("0");
            }
            if (!cmpTRowDTO.getRate().isEmpty()) {
                purParticularRow.setRate(cmpTRowDTO.getRate());
            } else {
                purParticularRow.setRate("0");
            }
            if (cmpTRowDTO.getBase_amt() != null) {
                purParticularRow.setBaseAmt(String.valueOf(cmpTRowDTO.getBase_amt()));
            } else {
                purParticularRow.setBaseAmt("0");
            }
            if (!cmpTRowDTO.getDis_amt().isEmpty()) {
                purParticularRow.setDisAmt(cmpTRowDTO.getDis_amt());
            } else {
                purParticularRow.setDisAmt("0");
            }
            if (!cmpTRowDTO.getDis_per().isEmpty()) {
                purParticularRow.setDisPer(cmpTRowDTO.getDis_per());
            } else {
                purParticularRow.setDisPer("0");
            }
            if (cmpTRowDTO.getDisPer2() != null && !cmpTRowDTO.getDisPer2().isEmpty()) {
                purParticularRow.setDisPer2(cmpTRowDTO.getDisPer2());
            } else {
                purParticularRow.setDisPer2("0");
            }
            if (cmpTRowDTO.getDis_per_cal() != null) {
                purParticularRow.setDisPerCal(String.valueOf(cmpTRowDTO.getDis_per_cal()));
            } else {
                purParticularRow.setDisPerCal("0");
            }
            if (cmpTRowDTO.getDis_amt_cal() != null) {
                purParticularRow.setDisAmtCal(String.valueOf(cmpTRowDTO.getDis_amt_cal()));
            } else {
                purParticularRow.setDisAmtCal("0");
            }
            if (cmpTRowDTO.getRow_dis_amt() != null) {
                purParticularRow.setRowDisAmt(String.valueOf(cmpTRowDTO.getRow_dis_amt()));
            } else {
                purParticularRow.setRowDisAmt("0");
            }
            if (cmpTRowDTO.getGross_amt() != null) {
                purParticularRow.setGrossAmt(String.valueOf(cmpTRowDTO.getGross_amt1()));
            } else {
                purParticularRow.setGrossAmt("0");
            }
            if (cmpTRowDTO.getAdd_chg_amt() != null) {
                purParticularRow.setAddChgAmt(String.valueOf(cmpTRowDTO.getAdd_chg_amt()));
            } else {
                purParticularRow.setAddChgAmt("0");
            }
            if (cmpTRowDTO.getGross_amt1() != null) {
                purParticularRow.setGrossAmt1(String.valueOf(cmpTRowDTO.getGross_amt1()));
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
            if (cmpTRowDTO.getGst() != null) {
                purParticularRow.setGst(String.valueOf(cmpTRowDTO.getGst()));
            } else {
                purParticularRow.setGst("0");
            }
            if (cmpTRowDTO.getCgstPer() != null) {
                purParticularRow.setCgst(String.valueOf(cmpTRowDTO.getCgstPer()));
            } else {
                purParticularRow.setCgst("0");
            }
            if (cmpTRowDTO.getIgstPer() != null) {
                purParticularRow.setIgst(String.valueOf(cmpTRowDTO.getIgstPer()));
            } else {
                purParticularRow.setIgst("0");
            }
            if (cmpTRowDTO.getSgstPer() != null) {
                purParticularRow.setSgst(String.valueOf(cmpTRowDTO.getSgstPer()));
            } else {
                purParticularRow.setSgst("0");
            }
            if (cmpTRowDTO.getTotal_igst() != null) {
                purParticularRow.setTotalIgst(String.valueOf(cmpTRowDTO.getTotal_igst()));
            } else {
                purParticularRow.setTotalIgst("0");
            }
            if (cmpTRowDTO.getTotal_cgst() != null) {
                purParticularRow.setTotalCgst(String.valueOf(cmpTRowDTO.getTotal_cgst()));
            } else {
                purParticularRow.setTotalCgst("0");
            }
            if (cmpTRowDTO.getTotal_sgst() != null) {
                purParticularRow.setTotalSgst(String.valueOf(cmpTRowDTO.getTotal_sgst()));
            } else {
                purParticularRow.setTotalSgst("0");
            }
            if (cmpTRowDTO.getFinal_amt() != null) {
                purParticularRow.setFinalAmt(String.valueOf(cmpTRowDTO.getFinal_amt()));
            } else {
                purParticularRow.setFinalAmt("0");
            }
            if (!cmpTRowDTO.getBatchNo().isEmpty()) {
                purParticularRow.setbNo(cmpTRowDTO.getBatchNo());
            } else {
                purParticularRow.setbNo("0");
            }
            if (cmpTRowDTO.getB_rate() != null && !cmpTRowDTO.getB_rate().isEmpty()) {
                purParticularRow.setbRate(cmpTRowDTO.getB_rate());
            } else {
                purParticularRow.setbRate("0");
            }
            if (!cmpTRowDTO.getSales_rate().isEmpty()) {
                purParticularRow.setSalesRate(cmpTRowDTO.getB_rate());
            } else {
                purParticularRow.setSalesRate("0");
            }
            if (cmpTRowDTO.getRate_a() != null && !cmpTRowDTO.getRate_a().isEmpty()) {
                purParticularRow.setRateA(cmpTRowDTO.getRate_a());
            } else {
                purParticularRow.setRateA("0");
            }

            if (cmpTRowDTO.getRate_b() != null && !cmpTRowDTO.getRate_b().isEmpty()) {
                purParticularRow.setRateB(cmpTRowDTO.getRate_b());
            } else {
                purParticularRow.setRateB("0");
            }
            if (cmpTRowDTO.getRate_c() != null && !cmpTRowDTO.getRate_c().isEmpty()) {
                purParticularRow.setRateC(cmpTRowDTO.getRate_c());
            } else {
                purParticularRow.setRateC("0");
            }
            if (cmpTRowDTO.getCosting() != null && !cmpTRowDTO.getCosting().isEmpty()) {
                purParticularRow.setCosting(cmpTRowDTO.getCosting());
            } else {
                purParticularRow.setCosting("0");
            }
            if (cmpTRowDTO.getCosting_with_tax() != null && !cmpTRowDTO.getCosting_with_tax().isEmpty()) {
                purParticularRow.setCostingWithTax(cmpTRowDTO.getCosting_with_tax());
            } else {
                purParticularRow.setCostingWithTax("0");
            }
            if (cmpTRowDTO.getMin_margin() != null && !cmpTRowDTO.getMin_margin().isEmpty()) {
                purParticularRow.setMinMargin(cmpTRowDTO.getMin_margin());
            } else {
                purParticularRow.setMinMargin("0");
            }
            if (cmpTRowDTO.getManufacturing_date() != null && !cmpTRowDTO.getManufacturing_date().isEmpty()) {
                purParticularRow.setManufacturingDate("2022-03-28");
            } else {
                purParticularRow.setManufacturingDate("2022-03-28");
            }
            if (cmpTRowDTO.getB_purchase_rate() != null && !cmpTRowDTO.getB_purchase_rate().isEmpty()) {
                purParticularRow.setbPurchaseRate(cmpTRowDTO.getB_purchase_rate());
            } else {
                purParticularRow.setbPurchaseRate("0");
            }
            if (cmpTRowDTO.getB_expiry() != null && !cmpTRowDTO.getB_expiry().isEmpty()) {
                purParticularRow.setbExpiry("2026-03-28");
            } else {
                purParticularRow.setbExpiry("2026-03-28");
            }
            if (cmpTRowDTO.getB_details_id() != null && !cmpTRowDTO.getB_details_id().isEmpty()) {
                purParticularRow.setbDetailsId(cmpTRowDTO.getB_details_id());
            } else {
                purParticularRow.setbDetailsId("0");
            }
            if (cmpTRowDTO.getIs_batch() != null && !cmpTRowDTO.getIs_batch().isEmpty()) {
                purParticularRow.setBatch(cmpTRowDTO.getIs_batch());
            } else {
                purParticularRow.setBatch("");
            }
            if (cmpTRowDTO.getSerialNo() != null && !cmpTRowDTO.getSerialNo().isEmpty()) {
                purParticularRow.setSerialNo(null);
            } else {
                purParticularRow.setSerialNo(null);
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
        map.put("row", mRowData);
        String finalReq = Globals.mapToStringforFormData(map);
        purInvCreateLogger.debug("final Request of Purchase Return--->" + finalReq);
        HttpResponse<String> response;
//        response = APIClient.postFormDataRequest(finalReq, EndPoints.CREATE_PUR_RETURN_ENDPOINT);
        APIClient apiClient = null;
        apiClient = new APIClient(EndPoints.CREATE_PUR_RETURN_ENDPOINT, finalReq, RequestType.FORM_DATA);
        try {
            apiClient.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
                @Override
                public void handle(WorkerStateEvent workerStateEvent) {
                    String requestBody = workerStateEvent.getSource().getValue().toString();
                    JsonObject responseBody = new Gson().fromJson(requestBody, JsonObject.class);
                    purInvCreateLogger.debug("responseBody Purchase Return" + responseBody);
                    AlertUtility.CustomCallback callback = (number) -> {
                        if (number == 1) {
                            GlobalController.getInstance().addTabStatic("product-list", false);
                        }
                    };
                    Stage stage = (Stage) rootAnchorPane.getScene().getWindow();
                    if (responseBody.get("responseStatus").getAsInt() == 200) {
                        AlertUtility.AlertSuccess(stage, "Success", responseBody.get("message").getAsString(), callback);
                    } else {
                        AlertUtility.AlertError(stage, AlertUtility.alertTypeError, responseBody.get("message").getAsString(), callback);
                    }
                }
            });
            apiClient.setOnCancelled(new EventHandler<WorkerStateEvent>() {
                @Override
                public void handle(WorkerStateEvent workerStateEvent) {
                    purInvCreateLogger.debug("Purchase Return Received----> setOnCancelled() " +
                            workerStateEvent.getSource().getValue().toString());
                }
            });
            apiClient.setOnFailed(new EventHandler<WorkerStateEvent>() {
                @Override
                public void handle(WorkerStateEvent workerStateEvent) {
                    purInvCreateLogger.debug("Purchase Return Received---> setOnFailed() " +
                            workerStateEvent.getSource().getValue().toString());
                }
            });
            apiClient.start();
        } catch (Exception e) {
            apiClient = null;
        }
    }

} //end of class





