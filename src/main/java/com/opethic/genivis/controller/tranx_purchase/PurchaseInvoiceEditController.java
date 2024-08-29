package com.opethic.genivis.controller.tranx_purchase;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.opethic.genivis.controller.dialogs.SingleInputDialogs;
import com.opethic.genivis.dto.*;
import com.opethic.genivis.dto.reqres.pur_tranx.*;
import com.opethic.genivis.network.APIClient;
import com.opethic.genivis.network.EndPoints;
import com.opethic.genivis.utils.DatePickerTableCell;
import com.opethic.genivis.utils.Globals;
import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.Duration;
import javafx.util.StringConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;
import java.net.http.HttpResponse;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.*;

public class PurchaseInvoiceEditController implements Initializable {
    @FXML
    private ScrollPane rootAnchorPaneEdit;
    @FXML
    private DatePicker dpPurInvEditTranxDate, dpPurInvEditInvDate;
    @FXML
    private TextField tfPurInvEditInvNo, tfPurInvEditPurSerial, tfPurInvLedgNameEdit, tfPurInvAllRowDisPerEdit, tfPurInvAllRowDisAmtEdit, tfPurchaseInvoiceAddChrgTotalAmt;
    @FXML
    private ComboBox<CommonDTO> cmbPurInvEditPurcAC;
    @FXML
    private RadioButton rbPaymentModeCreditEdit, rbPaymentModeCashEdit;
    @FXML
    private TableView<CmpTRowDTO> tvPurInvEditCMPTRow;
    @FXML
    private TableColumn<CmpTRowDTO, String> tblcPurInvEditCmptSrNo, tblcPurInvEditCmptParticular, tblcPurInvEditCmptPackage, tblcPurInvEditCmptLevelA, tblcPurInvEditCmptLevelB,
            tblcPurInvEditCmptLevelC, tblcPurInvEditCmptUnit, tblcPurInvEditCmptBatch, tblcPurInvEditCmptQuantity, tblcPurInvEditCmptRate, tblcPurInvEditCmptGrossAmt, tblcPurInvEditCmptDiscPerc, tblcPurInvEditCmptDisPer2, tblcPurInvEditCmptDiscAmt, tblcPurInvEditCmptTaxPerc, tblcPurInvEditCmptNetAmt, tblcPurInvEditCmptActions;
    @FXML
    private TableView<TranxBatchWindowDTO> tblvPurInvBatWinEditTableview;
    @FXML          //for batch
    private TableColumn<TranxBatchWindowDTO, String> tblcPurInvBatWinBatchNoEdit, tblcPurInvBatWinMRPEdit, tblcPurInvBatWinPurRateEdit, tblcPurInvBatWinQtyEdit, tblcPurInvBatWinFreeQtyEdit,
            tblcPurInvBatWinDisPerEdit, tblcPurInvBatWinDisAmtEdit, tblcPurInvBatWinBarcodeEdit, tblcPurInvBatWinMarginPerEdit, tblcPurInvBatWinFSREdit, tblcPurInvBatWinCSREdit,
            tblcPurInvBatWinSaleRateEdit;
    @FXML
    private TableColumn<TranxBatchWindowDTO, Void> tblcPurInvBatWinActionEdit;
    @FXML
    private TableColumn<TranxBatchWindowDTO, LocalDate> tblcPurInvBatWinPurDateEdit, tblcPurInvBatWinMFGDateEdit, tblcPurInvBatWinEXPDateEdit;
    @FXML
    private Label lblPurInvBatWinPrdNameEdit, lblPurInvBatWinCWTEdit, lblPurInvBatWinCWOTEDit, lblPurInvBatWinTaxEdit;

    @FXML
    private Label lblPurInvBillAmountEdit, lblPurInvTotalDiscountEdit, lblPurInvTotalTaxableAmountEdit, lblPurInvTotalTaxEdit, lblPurInvTotalSGSTEdit, lblPurInvTotalQtyEdit, lblPurInvGrossTotalEdit, lblPurInvTotalCGSTEdit;
    @FXML
    private TableView tvPurInvEditSupplierDetails;
    @FXML
    private TableColumn tcPurInvEditSupplierName, tcPurInvEditSupplierInvNo, tcPurInvEditSupplierInvDate, tcPurInvEditSupplierBatch, tcPurInvEditSupplierMrp, tcPurInvEditSupplierQty,
            tcPurInvEditSupplierRate, tcPurInvEditSupplierCost, tcPurInvEditSupplierDisper, tcPurInvEditSupplierDisRs;
    @FXML
    private Label lblPurInvFreeQtyEdit, lblPurInvRoundOffEdit;
    @FXML
    private CheckBox chbRoundOffEdit;
    @FXML
    private ComboBox<GstDetailsDTO> cmbPurInvEditSupplierGST;
    @FXML
    private HBox hbapPurInvBatchWindowEdit, hbapPurchaseInvAddnlChrgWindowEdit;
    @FXML
    private TabPane myTabPane;     //id of bottom info Ledger,Batch,Product
    @FXML
    private Tab tabPurInvLedgerInfoEdit, tabPurInvProductInfoEdit, tabPurInvBatchInfoEdit;
    @FXML        //for bottom ledger info
    private Text lbPurInvGstNoEdit, lbPurInvAreaEdit, lbPurInvBankEdit, lbPurInvContactPersonEdit, lbPurInvTransportEdit, lbPurInvCreditDaysEdit, lbPurInvFssaiEdit, lbPurInvLisenceNoEdit,
            lbPurInvRouteEdit;
    @FXML
    private TableView<GstDTO> tblvPurInvCreateGSTEdit;
    @FXML
    private Button btnPurInvBatchWinCloseEdit, btnPurchaseInvAddnlChargWindowCloseEdit;
    @FXML
    private TableColumn<GstDTO, String> tcPurInvBottomGSTEdit, tcPurInvBottomCGSTEdit, tcPurInvBottomSGSTEdit;
    @FXML      //for bottom product info
    private Text txtPurInvProductBrandEdit, txtPurInvProductGroupEdit, txtPurInvProductSubGroupEdit, txtPurInvProductCategoryEdit, txtPurInvProductHsnEdit, txtPurInvProductTaxTypeEdit,
            txtPurInvProductTaxPerEdit, txtPurInvProductMarginPerEdit, txtPurInvProductCostEdit, txtPurInvProductShelfIdEdit, txtPurInvProductMinStockEdit, txtPurInvProductMaxStockEdit;
    private String ledgerName;
    private String productId = "";
    private String selectedLedgerId = "";
    private int selectedRowIndex;
    private String selectedRowProductdId;
    private String productBatchTax;
    private String prdBatchTaxPer;
    private Double costValue = 0.0, costWithTaxValue = 0.0, taxAmt;
    private static final Logger purInvCreateLogger = LoggerFactory.getLogger(PurchaseInvoiceCreateController.class);
    private static ObservableList<String> unitList = FXCollections.observableArrayList();
    DecimalFormat decimalFormat = new DecimalFormat("0.#");


    static Long id = 0L;   //here we get the purchase invoice id from list
    private Long supplierId = 0L;

    public void closeAddCharges(ActionEvent actionEvent) {
        hbapPurchaseInvAddnlChrgWindowEdit.setVisible(false);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //todo : autofocus on TranxDate
        Platform.runLater(() -> dpPurInvEditTranxDate.requestFocus());

//         Enter traversal
        rootAnchorPaneEdit.addEventFilter(KeyEvent.KEY_PRESSED, (KeyEvent event) -> {
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

        responsiveCmpt();
        responsiveSupplierDetails();
        getPurchaseAccounts();
        System.out.println("aaaaaaaa " + id);
        getPurInvEditDataById();
        cmpTRowData();
        batchRowData();
        btnPurInvBatchWinCloseEdit.setOnAction(event -> {
            hbapPurInvBatchWindowEdit.setVisible(false);
        });

        hbapPurInvBatchWindowEdit.setManaged(false);
        hbapPurchaseInvAddnlChrgWindowEdit.setManaged(false);
        rootAnchorPaneEdit.sceneProperty().addListener((obs, oldScene, newScene) -> {
            if (newScene != null) {
                javafx.application.Platform.runLater(() -> {
                    tfPurInvLedgNameEdit.requestFocus();  //for focus on ledger name
                });
            }
        });
        //code for open ledger window on mouse clicked - new
        Platform.runLater(() -> tfPurInvLedgNameEdit.setOnMouseClicked(actionEvent -> {
            Stage stage = (Stage) rootAnchorPaneEdit.getScene().getWindow();
            SingleInputDialogs.openLedgerPopUp(stage, "Filter", input -> {
                //todo:Callback to Get all the Details of the Selected ledger and Set it in the Fields
                ledgerName = (String) input[0];
                selectedLedgerId = (String) input[1];
                ObservableList<GstDetailsDTO> gstDetails = (ObservableList<GstDetailsDTO>) input[2];
                tfPurInvLedgNameEdit.setText(ledgerName);
                if (gstDetails != null) {
                    setLedgerGSTData(gstDetails);
                }
                selectedLedgerDataInfo(selectedLedgerId);
            },in->{
                System.out.println("In >> Called >. "+ in);
                if(in == true){
//                            isLedgerRed = true;
//                            setPurChallDataToProduct();
                }
            });

        }));
        //code  for open ledger window on space key - new
        tfPurInvLedgNameEdit.setOnKeyPressed(actionEvent -> {
            if (actionEvent.getCode() == KeyCode.SPACE) {
                Stage stage = (Stage) rootAnchorPaneEdit.getScene().getWindow();
                SingleInputDialogs.openLedgerPopUp(stage, "Filter", input -> {
                    //todo:Callback to Get all the Details of the Selected ledger and Set it in the Fields
                    ledgerName = (String) input[0];
                    selectedLedgerId = (String) input[1];
                    ObservableList<GstDetailsDTO> gstDetails = (ObservableList<GstDetailsDTO>) input[2];
                    tfPurInvLedgNameEdit.setText(ledgerName);
                    if (gstDetails != null) {
                        setLedgerGSTData(gstDetails);
                    }
                },in->{
                    System.out.println("In >> Called >. "+ in);
                    if(in == true){
//                            isLedgerRed = true;
//                            setPurChallDataToProduct();
                    }
                });
            }
        });

        tvPurInvEditCMPTRow.setEditable(true);  //for enable the edit the CMPT row
        tvPurInvEditCMPTRow.getSelectionModel().setCellSelectionEnabled(true);
        tvPurInvEditCMPTRow.getSelectionModel().selectFirst();
        tvPurInvEditCMPTRow.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection == null) {
                tvPurInvEditCMPTRow.getSelectionModel().select(oldSelection);
            }
        });

        //code for open the product window
        tblcPurInvEditCmptParticular.setCellFactory(column -> {
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
                                Stage stage = (Stage) rootAnchorPaneEdit.getScene().getWindow();
                                SingleInputDialogs.openProductPopUp(stage, "Product", input -> {
//                                    System.out.println("inputtt " + input);
                                    // Assuming input[0] contains the product name
                                    String productName = (String) input[0];
                                    productId = (String) input[1];
                                    String packaging = (String) input[2];
                                    String mrp = (String) input[3];
                                    String unit = (String) input[4];
                                    String taxper = (String) input[5];
                                    String salesrate = (String) input[6];

                                    // Update the text field with the selected product name
                                    textField.setText(productName);
                                    // Get the index of the selected row
                                    int selectedIndex = getTableRow().getIndex();
                                    System.out.println("selected Index: " + selectedIndex);
                                    // Create a new instance of CmpTRowDTO with empty values
                                    CmpTRowDTO newRow = new CmpTRowDTO(String.valueOf(input[1]), productName, packaging, unit, "", "", mrp, "", "", "", taxper, "", "", "", "", "", "");
                                    // Add or update the row in the table
                                    if (selectedIndex < 0 || selectedIndex >= tvPurInvEditCMPTRow.getItems().size()) {
                                        // Add the new row if selected index is invalid
                                        tvPurInvEditCMPTRow.getItems().add(newRow);
                                    } else {
                                        // Update the existing row
                                        tvPurInvEditCMPTRow.getItems().set(selectedIndex, newRow);
                                    }
                                    //---------------- Add New Row Button -----------------//
                                    // Add "+" symbol in the last column
                                    TableColumn<CmpTRowDTO, Void> colActions = (TableColumn<CmpTRowDTO, Void>) tvPurInvEditCMPTRow.getColumns().get(tvPurInvEditCMPTRow.getColumns().size() - 1);
                                    colActions.setCellFactory(column -> {
                                        return new TableCell<>() {
                                            final Button addButton = new Button("+");

                                            {
                                                addButton.setStyle("-fx-font-size: 24px;-fx-background-color: transparent; -fx-border-color: transparent; -fx-text-fill: black;");
                                                addButton.setAlignment(Pos.CENTER_LEFT);
                                                addButton.setMinWidth(10); // Set minimum width to maintain button visibility
                                                addButton.setMinHeight(10); // Set minimum height to maintain button visibility
                                                addButton.setPadding(new Insets(0, 0, 0, -5)); // Add padding to increase button size

                                                addButton.setOnAction(event -> {
                                                    // Implement the logic to add a new row or any other action you desire
                                                    addRow(event);
                                                });
                                                getSelectedProdDataInfo(productId);
                                            }

                                            @Override
                                            protected void updateItem(Void item, boolean empty) {
                                                super.updateItem(item, empty);

                                                if (empty || productName == null) { // Hide the button if no product name is set
                                                    setGraphic(null);
                                                } else {
                                                    setGraphic(addButton);
                                                }
                                            }
                                        };
                                    });
                                });
                            }
                        });
                        textField.setOnKeyPressed(event -> {
                            if (event.getCode() == KeyCode.SPACE) {
                                if (!isEmpty()) {
                                    Stage stage = (Stage) rootAnchorPaneEdit.getScene().getWindow();
                                    SingleInputDialogs.openProductPopUp(stage, "Product", input -> {
                                        // Assuming input[0] contains the product name
                                        String productName = (String) input[0];
                                        productId = (String) input[1];
                                        String packaging = (String) input[2];
                                        String mrp = (String) input[3];
                                        String unit = (String) input[4];
                                        String taxper = (String) input[5];
                                        String salesrate = (String) input[6];
                                        // Update the text field with the selected product name
                                        textField.setText(productName);
                                        // Get the index of the selected row
                                        int selectedIndex = getTableRow().getIndex();
                                        System.out.println("selected Index: " + selectedIndex);
                                        // Create a new instance of CmpTRowDTO with empty values
                                        CmpTRowDTO newRow = new CmpTRowDTO(String.valueOf(input[1]), productName, packaging, unit, "", "", mrp, "", "", "", taxper, "", "", "", "", "", "");

                                        // Add or update the row in the table
                                        if (selectedIndex < 0 || selectedIndex >= tvPurInvEditCMPTRow.getItems().size()) {
                                            // Add the new row if selected index is invalid
                                            tvPurInvEditCMPTRow.getItems().add(newRow);
                                        } else {
                                            // Update the existing row
                                            tvPurInvEditCMPTRow.getItems().set(selectedIndex, newRow);
                                        }
//++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++Add New Row Button++++++++++++++++++++++++++++++++++++++++++++++++++++++
                                        // Add "+" symbol in the last column
                                        TableColumn<CmpTRowDTO, Void> colActions = (TableColumn<CmpTRowDTO, Void>) tvPurInvEditCMPTRow.getColumns().get(tvPurInvEditCMPTRow.getColumns().size() - 1);
                                        colActions.setCellFactory(column -> {
                                            return new TableCell<>() {
                                                final Button addButton = new Button("+");

                                                {
                                                    addButton.setStyle("-fx-font-size: 24px;-fx-background-color: transparent; -fx-border-color: transparent; -fx-text-fill: black;");
                                                    addButton.setAlignment(Pos.CENTER_LEFT);
                                                    addButton.setMinWidth(10); // Set minimum width to maintain button visibility
                                                    addButton.setMinHeight(10); // Set minimum height to maintain button visibility
                                                    addButton.setPadding(new Insets(0, 0, 0, -5)); // Add padding to increase button size

                                                    addButton.setOnAction(event -> {
                                                        // Implement the logic to add a new row or any other action you desire
                                                        addRow(event);
                                                    });
                                                    getSelectedProdDataInfo(productId);
                                                }

                                                @Override
                                                protected void updateItem(Void item, boolean empty) {
                                                    super.updateItem(item, empty);

                                                    if (empty || productName == null) { // Hide the button if no product name is set
                                                        setGraphic(null);
                                                    } else {
                                                        setGraphic(addButton);
                                                    }
                                                }
                                            };
                                        });
                                    });
                                }
                            }
                        });
                    }
                }
            };
        });

        tblvPurInvBatWinEditTableview.setEditable(true);  //for enable edit in batch window
        tblvPurInvBatWinEditTableview.getSelectionModel().setCellSelectionEnabled(true);
        tblvPurInvBatWinEditTableview.getSelectionModel().selectFirst();
        tblvPurInvBatWinEditTableview.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection == null) {
                tblvPurInvBatWinEditTableview.getSelectionModel().select(oldSelection);
            }
        });
        tblcPurInvEditCmptBatch.setCellFactory(column -> {     //for create the field of batch in CMPT row and open the batch window
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
                                System.out.println("SelectedItem" + selectedItem.getParticulars() + "Tax :" + selectedItem.getTax_per());
                                lblPurInvBatWinPrdNameEdit.setText(selectedItem.getParticulars());
                                lblPurInvBatWinTaxEdit.setText(selectedItem.getTax_per());
                                productBatchTax = selectedItem.getTax_per();
                                selectedRowProductdId = selectedItem.getProduct_id();
                                hbapPurInvBatchWindowEdit.setVisible(true);
                            }
                        });

                    }
                }
            };
        });

    }

    //function for add blank CMPT row
    //function for adding blank CMPT row
    public void addRow(ActionEvent actionEvent) {
        CmpTRowDTO newRow = new CmpTRowDTO("", "", "", "", "", "", "", "",
                "", "", "", "", "", "", "", "", "");
        tvPurInvEditCMPTRow.getItems().add(newRow);
    }

    public void responsiveCmpt() {
        tblcPurInvEditCmptSrNo.prefWidthProperty().bind(tvPurInvEditCMPTRow.widthProperty().multiply(0.03));
        tblcPurInvEditCmptPackage.prefWidthProperty().bind(tvPurInvEditCMPTRow.widthProperty().multiply(0.05));
        tblcPurInvEditCmptUnit.prefWidthProperty().bind(tvPurInvEditCMPTRow.widthProperty().multiply(0.05));
        tblcPurInvEditCmptQuantity.prefWidthProperty().bind(tvPurInvEditCMPTRow.widthProperty().multiply(0.05));
        tblcPurInvEditCmptRate.prefWidthProperty().bind(tvPurInvEditCMPTRow.widthProperty().multiply(0.08));
        tblcPurInvEditCmptDiscPerc.prefWidthProperty().bind(tvPurInvEditCMPTRow.widthProperty().multiply(0.05));
        tblcPurInvEditCmptDiscAmt.prefWidthProperty().bind(tvPurInvEditCMPTRow.widthProperty().multiply(0.05));
        tblcPurInvEditCmptNetAmt.prefWidthProperty().bind(tvPurInvEditCMPTRow.widthProperty().multiply(0.05));
        tblcPurInvEditCmptActions.prefWidthProperty().bind(tvPurInvEditCMPTRow.widthProperty().multiply(0.03));
        tblcPurInvEditCmptBatch.prefWidthProperty().bind(tvPurInvEditCMPTRow.widthProperty().multiply(0.07));
        if (tblcPurInvEditCmptLevelA.isVisible() == true && tblcPurInvEditCmptLevelB.isVisible() == false && tblcPurInvEditCmptLevelC.isVisible() == false) {
            tblcPurInvEditCmptParticular.prefWidthProperty().bind(tvPurInvEditCMPTRow.widthProperty().multiply(0.28));
            tblcPurInvEditCmptGrossAmt.prefWidthProperty().bind(tvPurInvEditCMPTRow.widthProperty().multiply(0.11));
            tblcPurInvEditCmptNetAmt.prefWidthProperty().bind(tvPurInvEditCMPTRow.widthProperty().multiply(0.04));
            tblcPurInvEditCmptLevelA.prefWidthProperty().bind(tvPurInvEditCMPTRow.widthProperty().multiply(0.05));
        } else if (tblcPurInvEditCmptLevelA.isVisible() == true && tblcPurInvEditCmptLevelB.isVisible() == true && tblcPurInvEditCmptLevelC.isVisible() == false) {
            System.out.println("inside second condn");
            tblcPurInvEditCmptLevelA.prefWidthProperty().bind(tvPurInvEditCMPTRow.widthProperty().multiply(0.05));
            tblcPurInvEditCmptParticular.prefWidthProperty().bind(tvPurInvEditCMPTRow.widthProperty().multiply(0.26));
            tblcPurInvEditCmptGrossAmt.prefWidthProperty().bind(tvPurInvEditCMPTRow.widthProperty().multiply(0.08));
            tblcPurInvEditCmptNetAmt.prefWidthProperty().bind(tvPurInvEditCMPTRow.widthProperty().multiply(0.04));
            tblcPurInvEditCmptLevelB.prefWidthProperty().bind(tvPurInvEditCMPTRow.widthProperty().multiply(0.05));
        } else if (tblcPurInvEditCmptLevelA.isVisible() == true && tblcPurInvEditCmptLevelB.isVisible() == true && tblcPurInvEditCmptLevelC.isVisible() == true) {
            System.out.println("inside thrd condn");
            tblcPurInvEditCmptParticular.prefWidthProperty().bind(tvPurInvEditCMPTRow.widthProperty().multiply(0.22));
            tblcPurInvEditCmptGrossAmt.prefWidthProperty().bind(tvPurInvEditCMPTRow.widthProperty().multiply(0.1));
            tblcPurInvEditCmptNetAmt.prefWidthProperty().bind(tvPurInvEditCMPTRow.widthProperty().multiply(0.04));
            tblcPurInvEditCmptLevelA.prefWidthProperty().bind(tvPurInvEditCMPTRow.widthProperty().multiply(0.04));
            tblcPurInvEditCmptLevelB.prefWidthProperty().bind(tvPurInvEditCMPTRow.widthProperty().multiply(0.04));
            tblcPurInvEditCmptLevelC.prefWidthProperty().bind(tvPurInvEditCMPTRow.widthProperty().multiply(0.04));
        } else {
            System.out.println("inside fourth condn");
            tblcPurInvEditCmptParticular.prefWidthProperty().bind(tvPurInvEditCMPTRow.widthProperty().multiply(0.29));
            tblcPurInvEditCmptGrossAmt.prefWidthProperty().bind(tvPurInvEditCMPTRow.widthProperty().multiply(0.13));
            tblcPurInvEditCmptNetAmt.prefWidthProperty().bind(tvPurInvEditCMPTRow.widthProperty().multiply(0.08));
        }
    }

    //    //function for responsive of bottom supplier details
    public void responsiveSupplierDetails() {
        tcPurInvEditSupplierName.prefWidthProperty().bind(tvPurInvEditSupplierDetails.widthProperty().multiply(0.2));
        tcPurInvEditSupplierInvNo.prefWidthProperty().bind(tvPurInvEditSupplierDetails.widthProperty().multiply(0.1));
        tcPurInvEditSupplierInvDate.prefWidthProperty().bind(tvPurInvEditSupplierDetails.widthProperty().multiply(0.1));
        tcPurInvEditSupplierBatch.prefWidthProperty().bind(tvPurInvEditSupplierDetails.widthProperty().multiply(0.1));
        tcPurInvEditSupplierMrp.prefWidthProperty().bind(tvPurInvEditSupplierDetails.widthProperty().multiply(0.1));
        tcPurInvEditSupplierQty.prefWidthProperty().bind(tvPurInvEditSupplierDetails.widthProperty().multiply(0.08));
        tcPurInvEditSupplierRate.prefWidthProperty().bind(tvPurInvEditSupplierDetails.widthProperty().multiply(0.08));
        tcPurInvEditSupplierCost.prefWidthProperty().bind(tvPurInvEditSupplierDetails.widthProperty().multiply(0.08));
        tcPurInvEditSupplierDisper.prefWidthProperty().bind(tvPurInvEditSupplierDetails.widthProperty().multiply(0.08));
        tcPurInvEditSupplierDisRs.prefWidthProperty().bind(tvPurInvEditSupplierDetails.widthProperty().multiply(0.08));
    }

    //function for set the gst details
    public void setLedgerGSTData(ObservableList<GstDetailsDTO> gstDetails) {

        cmbPurInvEditSupplierGST.setItems(gstDetails);   //setting the gst no to the Supplier GSTIN dropdown
        cmbPurInvEditSupplierGST.setConverter(new StringConverter<GstDetailsDTO>() {
            @Override
            public String toString(GstDetailsDTO gstDetailsDTO) {
                return gstDetailsDTO != null ? gstDetailsDTO.getGstNo() : "No GST details";
            }

            @Override
            public GstDetailsDTO fromString(String s) {
                return null;
            }
        });
        GstDetailsDTO selectedGst = null;
        for (Object obj : cmbPurInvEditSupplierGST.getItems()) {
            GstDetailsDTO gstDetailsDTO = (GstDetailsDTO) obj;
            if (gstDetailsDTO.getId().equals(gstDetails.get(0).getId())) {
                selectedGst = gstDetailsDTO;
                break;
            }
        }
        if (selectedGst != null) cmbPurInvEditSupplierGST.getSelectionModel().select(selectedGst);
        cmbPurInvEditSupplierGST.requestFocus();
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
            // activating the ledger tab
            myTabPane.getSelectionModel().select(tabPurInvLedgerInfoEdit);
            JsonObject item = jsonObject.get("result").getAsJsonObject();
            System.out.println("Item:" + item);
            String gstNo = item.get("gst_number").getAsString();
            String Area = item.get("area") != null ? item.get("area").getAsString() : "";
            String bank = item.get("bank_name").getAsString();
            String contact_person = item.get("contact_name").getAsString();
            String mobile_no = item.get("contact_no").getAsString();
            String credit_days = item.get("credit_days").getAsString();
            String fssai = item.get("fssai_number").getAsString();
            String licence_no = item.get("license_number").getAsString();
            String route = item.get("route").getAsString();

            //setting Ledger details at bottom
            lbPurInvGstNoEdit.setText(gstNo);
            lbPurInvAreaEdit.setText(Area);
            lbPurInvBankEdit.setText(bank);
            lbPurInvContactPersonEdit.setText(contact_person);
//            lb.setText(mobile_no);
            lbPurInvCreditDaysEdit.setText(credit_days);
            lbPurInvFssaiEdit.setText(fssai);
            lbPurInvLisenceNoEdit.setText(licence_no);
            lbPurInvRouteEdit.setText(route);

        }
    }

    //function for creating and setting the value of cell of CMPT Row
    public void cmpTRowData() {
        // Set up columns
        tblcPurInvEditCmptParticular.setCellValueFactory(cellData -> cellData.getValue().particularsProperty());
        tblcPurInvEditCmptParticular.setCellFactory(TextFieldTableCell.forTableColumn());
        tblcPurInvEditCmptParticular.setOnEditCommit(event -> {
            CmpTRowDTO person = event.getRowValue();
            person.setParticulars(event.getNewValue());
            moveFocusToNextCell(event.getTablePosition(), true);
        });

        tblcPurInvEditCmptPackage.setCellValueFactory(cellData -> cellData.getValue().packingProperty());
        tblcPurInvEditCmptPackage.setCellFactory(TextFieldTableCell.forTableColumn());
        tblcPurInvEditCmptPackage.setOnEditCommit(event -> {
            CmpTRowDTO person = event.getRowValue();
            person.setPacking(event.getNewValue());
            moveFocusToNextCell(event.getTablePosition(), true);
        });

        tblcPurInvEditCmptUnit.setCellValueFactory(cellData -> cellData.getValue().unitProperty());
        tblcPurInvEditCmptUnit.setCellFactory(ComboBoxTableCell.forTableColumn(unitList));
        tblcPurInvEditCmptUnit.setOnEditCommit(event -> {
            CmpTRowDTO person = event.getRowValue();
            if (event.getNewValue() != null) {
                person.setUnit(event.getNewValue());
                moveFocusToNextCell(event.getTablePosition(), true);
//
            }
        });
        tblcPurInvEditCmptBatch.setCellValueFactory(cellData -> cellData.getValue().batchNoProperty());
        tblcPurInvEditCmptBatch.setCellFactory(TextFieldTableCell.forTableColumn());
        tblcPurInvEditCmptBatch.setOnEditCommit(event -> {
            CmpTRowDTO person = event.getRowValue();
            person.setBatchNo(event.getNewValue());
            moveFocusToNextCell(event.getTablePosition(), true);
        });
        tblcPurInvEditCmptQuantity.setCellValueFactory(cellData -> cellData.getValue().quantityProperty());
        tblcPurInvEditCmptQuantity.setCellFactory(TextFieldTableCell.forTableColumn());
        tblcPurInvEditCmptQuantity.setOnEditCommit(event -> {
            CmpTRowDTO person = event.getRowValue();
            person.setQuantity(event.getNewValue());
            moveFocusToNextCell(event.getTablePosition(), true);
        });
        tblcPurInvEditCmptRate.setCellValueFactory(cellData -> cellData.getValue().rateProperty());
        tblcPurInvEditCmptRate.setCellFactory(TextFieldTableCell.forTableColumn());
        tblcPurInvEditCmptRate.setOnEditCommit(event -> {
            CmpTRowDTO person = event.getRowValue();
            person.setRate(event.getNewValue());
            moveFocusToNextCell(event.getTablePosition(), true);
        });

        tblcPurInvEditCmptGrossAmt.setCellValueFactory(cellData -> cellData.getValue().gross_amtProperty());
        tblcPurInvEditCmptGrossAmt.setCellFactory(TextFieldTableCell.forTableColumn());
        tblcPurInvEditCmptGrossAmt.setOnEditCommit(event -> {
            CmpTRowDTO person = event.getRowValue();
            if (event.getNewValue() != null) {
                person.setGross_amt(event.getNewValue());
                moveFocusToNextCell(event.getTablePosition(), true);
            }
        });
        tblcPurInvEditCmptDiscPerc.setCellValueFactory(cellData -> cellData.getValue().dis_perProperty());
        tblcPurInvEditCmptDiscPerc.setCellFactory(TextFieldTableCell.forTableColumn());
        tblcPurInvEditCmptDiscPerc.setOnEditCommit(event -> {
            CmpTRowDTO person = event.getRowValue();
            if (event.getNewValue() != null) {
                person.setDis_per(event.getNewValue());
                moveFocusToNextCell(event.getTablePosition(), true);
            }
        });
        tblcPurInvEditCmptDiscAmt.setCellValueFactory(cellData -> cellData.getValue().dis_amtProperty());
        tblcPurInvEditCmptDiscAmt.setCellFactory(TextFieldTableCell.forTableColumn());
        tblcPurInvEditCmptDiscAmt.setOnEditCommit(event -> {
            CmpTRowDTO person = event.getRowValue();
            if (event.getNewValue() != null) {
                person.setDis_amt(event.getNewValue());
                moveFocusToNextCell(event.getTablePosition(), true);
//                costingCalculation();
            }
        });
        tblcPurInvEditCmptTaxPerc.setCellValueFactory(cellData -> cellData.getValue().tax_perProperty());
        tblcPurInvEditCmptTaxPerc.setCellFactory(TextFieldTableCell.forTableColumn());
        tblcPurInvEditCmptTaxPerc.setOnEditCommit(event -> {
            CmpTRowDTO person = event.getRowValue();
            if (event.getNewValue() != null) {
                person.setTax_per(event.getNewValue());
                moveFocusToNextCell(event.getTablePosition(), true);
            }
        });
        tblcPurInvEditCmptNetAmt.setCellValueFactory(cellData -> cellData.getValue().net_amtProperty());
        tblcPurInvEditCmptNetAmt.setCellFactory(TextFieldTableCell.forTableColumn());
        tblcPurInvEditCmptNetAmt.setOnEditCommit(event -> {
            CmpTRowDTO person = event.getRowValue();
            if (event.getNewValue() != null) {
                person.setNet_amt(event.getNewValue());
                moveFocusToNextRow(event.getTablePosition());
            }
        });
        // Add an initial empty row
        tvPurInvEditCMPTRow.getItems().add(new CmpTRowDTO("", "", "", "", "", "", "",
                "", "", "", "", "", "", "", "", "", ""));

    }

    private void moveFocusToNextCell(TablePosition<CmpTRowDTO, ?> position, boolean nextColumn) {
        final int row = position.getRow();
        final int col = position.getColumn();

        if (nextColumn) {
            if (col + 1 < tvPurInvEditCMPTRow.getColumns().size()) {
                tvPurInvEditCMPTRow.getSelectionModel().select(row, tvPurInvEditCMPTRow.getColumns().get(col + 1));
                tvPurInvEditCMPTRow.edit(row, tvPurInvEditCMPTRow.getColumns().get(col + 1));
            } else if (row + 1 < tvPurInvEditCMPTRow.getItems().size()) {
                tvPurInvEditCMPTRow.getSelectionModel().select(row + 1, tvPurInvEditCMPTRow.getColumns().get(0));
                tvPurInvEditCMPTRow.edit(row + 1, tvPurInvEditCMPTRow.getColumns().get(0));
            }
        }
    }

    private void moveFocusToNextRow(TablePosition<CmpTRowDTO, ?> position) {
        final int row = position.getRow();

        if (row + 1 < tvPurInvEditCMPTRow.getItems().size()) {
            tvPurInvEditCMPTRow.getSelectionModel().select(row + 1, tvPurInvEditCMPTRow.getColumns().get(0));
            tvPurInvEditCMPTRow.edit(row + 1, tvPurInvEditCMPTRow.getColumns().get(0));
        }
    }

    //function for batch Window
    public void batchRowData() {
        // Set up columns
        tblcPurInvBatWinBatchNoEdit.setCellValueFactory(cellData -> cellData.getValue().b_noProperty());
        tblcPurInvBatWinBatchNoEdit.setCellFactory(TextFieldTableCell.forTableColumn());
        tblcPurInvBatWinBatchNoEdit.setOnEditCommit(event -> {
            TranxBatchWindowDTO person = event.getRowValue();
            person.setB_no(event.getNewValue());
            moveFocusToNextCellInBatchWin(event.getTablePosition(), true);
        });
        tblcPurInvBatWinPurDateEdit.setCellValueFactory(cellData -> cellData.getValue().pur_dateProperty());
        tblcPurInvBatWinPurDateEdit.setCellFactory(new Callback<TableColumn<TranxBatchWindowDTO, LocalDate>, TableCell<TranxBatchWindowDTO, LocalDate>>() {
            @Override
            public TableCell<TranxBatchWindowDTO, LocalDate> call(TableColumn<TranxBatchWindowDTO, LocalDate> param) {
                return new DatePickerTableCell();
            }
        });
        // Set up columns
        tblcPurInvBatWinMFGDateEdit.setCellValueFactory(cellData -> cellData.getValue().manufacturing_dateProperty());
        tblcPurInvBatWinMFGDateEdit.setCellFactory(new Callback<TableColumn<TranxBatchWindowDTO, LocalDate>, TableCell<TranxBatchWindowDTO, LocalDate>>() {
            @Override
            public TableCell<TranxBatchWindowDTO, LocalDate> call(TableColumn<TranxBatchWindowDTO, LocalDate> param) {
                return new DatePickerTableCell();
            }
        });

        tblcPurInvBatWinEXPDateEdit.setCellValueFactory(cellData -> cellData.getValue().b_expiryProperty());
        tblcPurInvBatWinEXPDateEdit.setCellFactory(new Callback<TableColumn<TranxBatchWindowDTO, LocalDate>, TableCell<TranxBatchWindowDTO, LocalDate>>() {
            @Override
            public TableCell<TranxBatchWindowDTO, LocalDate> call(TableColumn<TranxBatchWindowDTO, LocalDate> param) {
                return new DatePickerTableCell();
            }
        });
        // Assuming tblcPurChallBatWinEXPDate is your TableColumn
        tblcPurInvBatWinEXPDateEdit.setOnEditCommit(event -> {
            // Get the selected date from the event
            LocalDate selectedDate = event.getNewValue();

            // Get the index of the edited row
            int index = event.getTablePosition().getRow();

            // Assuming you have a list of TranxBatchWindowDTO items
            List<TranxBatchWindowDTO> items = tblvPurInvBatWinEditTableview.getItems();

            // Update the corresponding TranxBatchWindowDTO with the selected date
            items.get(index).setB_expiry(selectedDate);

            // You might want to do other actions with the updated value

            // If needed, refresh the table to reflect the changes
            tblvPurInvBatWinEditTableview.refresh();
        });
        // Set up columns
        tblcPurInvBatWinMRPEdit.setCellValueFactory(cellData -> cellData.getValue().mrpProperty());
        tblcPurInvBatWinMRPEdit.setCellFactory(TextFieldTableCell.forTableColumn());
        tblcPurInvBatWinMRPEdit.setOnEditCommit(event -> {
            TranxBatchWindowDTO person = event.getRowValue();
            person.setMrp(event.getNewValue());
            moveFocusToNextCellInBatchWin(event.getTablePosition(), true);
        });
        tblcPurInvBatWinPurRateEdit.setCellValueFactory(cellData -> cellData.getValue().purRateProperty());
        tblcPurInvBatWinPurRateEdit.setCellFactory(TextFieldTableCell.forTableColumn());
        tblcPurInvBatWinPurRateEdit.setOnEditCommit(event -> {
            TranxBatchWindowDTO person = event.getRowValue();
            person.setPurRate(event.getNewValue());
            moveFocusToNextCellInBatchWin(event.getTablePosition(), true);
        });
        tblcPurInvBatWinQtyEdit.setCellValueFactory(cellData -> cellData.getValue().b_qtyProperty());
        tblcPurInvBatWinQtyEdit.setCellFactory(TextFieldTableCell.forTableColumn());
        tblcPurInvBatWinQtyEdit.setOnEditCommit(event -> {
            TranxBatchWindowDTO person = event.getRowValue();
            person.setB_qty(event.getNewValue());
            moveFocusToNextCellInBatchWin(event.getTablePosition(), true);
            costingCalculation();
        });
        tblcPurInvBatWinFreeQtyEdit.setCellValueFactory(cellData -> cellData.getValue().b_freeQtyProperty());
        tblcPurInvBatWinFreeQtyEdit.setCellFactory(TextFieldTableCell.forTableColumn());
        tblcPurInvBatWinFreeQtyEdit.setOnEditCommit(event -> {
            TranxBatchWindowDTO person = event.getRowValue();
            person.setB_freeQty(event.getNewValue());
            moveFocusToNextCellInBatchWin(event.getTablePosition(), true);
            costingCalculation();
        });
        tblcPurInvBatWinDisPerEdit.setCellValueFactory(cellData -> cellData.getValue().b_dis_perProperty());
        tblcPurInvBatWinDisPerEdit.setCellFactory(TextFieldTableCell.forTableColumn());
        tblcPurInvBatWinDisPerEdit.setOnEditCommit(event -> {
            TranxBatchWindowDTO person = event.getRowValue();
            person.setB_dis_per(event.getNewValue());
            moveFocusToNextCellInBatchWin(event.getTablePosition(), true);
            costingCalculation();
        });
        tblcPurInvBatWinDisAmtEdit.setCellValueFactory(cellData -> cellData.getValue().b_dis_amtProperty());
        tblcPurInvBatWinDisAmtEdit.setCellFactory(TextFieldTableCell.forTableColumn());
        tblcPurInvBatWinDisAmtEdit.setOnEditCommit(event -> {
            TranxBatchWindowDTO person = event.getRowValue();
            person.setB_dis_amt(event.getNewValue());
            moveFocusToNextCellInBatchWin(event.getTablePosition(), true);
            costingCalculation();
        });
        tblcPurInvBatWinBarcodeEdit.setCellValueFactory(cellData -> cellData.getValue().barcodeProperty());
        tblcPurInvBatWinBarcodeEdit.setCellFactory(TextFieldTableCell.forTableColumn());
        tblcPurInvBatWinBarcodeEdit.setOnEditCommit(event -> {
            TranxBatchWindowDTO person = event.getRowValue();
            person.setBarcode(event.getNewValue());
            moveFocusToNextCellInBatchWin(event.getTablePosition(), true);
        });
        tblcPurInvBatWinMarginPerEdit.setCellValueFactory(cellData -> cellData.getValue().marginProperty());
        tblcPurInvBatWinMarginPerEdit.setCellFactory(TextFieldTableCell.forTableColumn());
        tblcPurInvBatWinMarginPerEdit.setOnEditCommit(event -> {
            TranxBatchWindowDTO person = event.getRowValue();
            person.setMargin(event.getNewValue());
            moveFocusToNextCellInBatchWin(event.getTablePosition(), true);
            if (!event.getNewValue().isEmpty()) {
                setFSRWithMargin();
            } else {
                moveFocusToNextCellInBatchWin(event.getTablePosition(), true);
            }
        });
        tblcPurInvBatWinFSREdit.setCellValueFactory(cellData -> cellData.getValue().b_rate_aProperty());
        tblcPurInvBatWinFSREdit.setCellFactory(TextFieldTableCell.forTableColumn());
        tblcPurInvBatWinFSREdit.setOnEditCommit(event -> {
            TranxBatchWindowDTO person = event.getRowValue();
            person.setB_rate_a(event.getNewValue());
            if (!event.getNewValue().isEmpty()) {
                validateFSRRate();
            } else {
                moveFocusToNextCellInBatchWin(event.getTablePosition(), true);
            }
        });
        tblcPurInvBatWinCSREdit.setCellValueFactory(cellData -> cellData.getValue().b_rate_bProperty());
        tblcPurInvBatWinCSREdit.setCellFactory(TextFieldTableCell.forTableColumn());
        tblcPurInvBatWinCSREdit.setOnEditCommit(event -> {
            TranxBatchWindowDTO person = event.getRowValue();
            person.setB_rate_b(event.getNewValue());
            if (!event.getNewValue().isEmpty()) {
                validateCSRRate();
            } else {
                moveFocusToNextCellInBatchWin(event.getTablePosition(), true);
            }
        });
        tblcPurInvBatWinSaleRateEdit.setCellValueFactory(cellData -> cellData.getValue().b_rate_cProperty());
        tblcPurInvBatWinSaleRateEdit.setCellFactory(TextFieldTableCell.forTableColumn());
        tblcPurInvBatWinSaleRateEdit.setOnEditCommit(event -> {
            TranxBatchWindowDTO person = event.getRowValue();
            person.setB_rate_c(event.getNewValue());
            if (!event.getNewValue().isEmpty()) {
                validateSaleRate();
            } else {
                moveFocusToNextCellInBatchWin(event.getTablePosition(), true);
            }
        });
        tblcPurInvBatWinActionEdit.setCellFactory(param -> {
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
        tblvPurInvBatWinEditTableview.getItems().add(new TranxBatchWindowDTO("", constantDate1, constantDate, constantDate, "", "", "",
                "", "", "", "", "", "", "", "", "", "", "", "",
                "", "", "", ""));

    }

    public void validateCSRRate() {
        ObservableList<TranxBatchWindowDTO> tableView2Items1 = tblvPurInvBatWinEditTableview.getItems();
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
            moveFocusToNextCellInBatchWin(tblvPurInvBatWinEditTableview.getFocusModel().getFocusedCell(), true);
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
                    moveFocusToNextCellInBatchWin(tblvPurInvBatWinEditTableview.getFocusModel().getFocusedCell(), true);
                } else if (response == buttonTypeCancel) {
                    // Keep focus on the current cell
                    Platform.runLater(() -> {
                        tblvPurInvBatWinEditTableview.requestFocus();
                        tblvPurInvBatWinEditTableview.getSelectionModel().select(tblvPurInvBatWinEditTableview.getFocusModel().getFocusedCell().getRow(),
                                tblvPurInvBatWinEditTableview.getFocusModel().getFocusedCell().getTableColumn());
                        tblvPurInvBatWinEditTableview.scrollTo(tblvPurInvBatWinEditTableview.getFocusModel().getFocusedCell().getRow());
                    });
                }
            });
        }
    }

    public void validateFSRRate() {
        ObservableList<TranxBatchWindowDTO> tableView2Items1 = tblvPurInvBatWinEditTableview.getItems();
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
            moveFocusToNextCellInBatchWin(tblvPurInvBatWinEditTableview.getFocusModel().getFocusedCell(), true);
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
                    moveFocusToNextCellInBatchWin(tblvPurInvBatWinEditTableview.getFocusModel().getFocusedCell(), true);
                } else if (response == buttonTypeCancel) {
                    System.out.println("Cancel Button Clicked");
                    // Keep focus on the current cell
                    Platform.runLater(() -> {
                        tblvPurInvBatWinEditTableview.requestFocus();
                        tblvPurInvBatWinEditTableview.getSelectionModel().select(tblvPurInvBatWinEditTableview.getFocusModel().getFocusedCell().getRow(),
                                tblvPurInvBatWinEditTableview.getFocusModel().getFocusedCell().getTableColumn());
                        tblvPurInvBatWinEditTableview.scrollTo(tblvPurInvBatWinEditTableview.getFocusModel().getFocusedCell().getRow());
                    });
                }
            });
        }
    }

    public void validateSaleRate() {
        ObservableList<TranxBatchWindowDTO> tableView2Items1 = tblvPurInvBatWinEditTableview.getItems();
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
            moveFocusToNextCellInBatchWin(tblvPurInvBatWinEditTableview.getFocusModel().getFocusedCell(), true);
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
                    moveFocusToNextCellInBatchWin(tblvPurInvBatWinEditTableview.getFocusModel().getFocusedCell(), true);
                } else if (response == buttonTypeCancel) {
                    // Keep focus on the current cell
                    Platform.runLater(() -> {
                        tblvPurInvBatWinEditTableview.requestFocus();
                        tblvPurInvBatWinEditTableview.getSelectionModel().select(tblvPurInvBatWinEditTableview.getFocusModel().getFocusedCell().getRow(),
                                tblvPurInvBatWinEditTableview.getFocusModel().getFocusedCell().getTableColumn());
                        tblvPurInvBatWinEditTableview.scrollTo(tblvPurInvBatWinEditTableview.getFocusModel().getFocusedCell().getRow());
                    });
                }
            });
        }
    }

    private void moveFocusToNextCellInBatchWin(TablePosition<TranxBatchWindowDTO, ?> position, boolean nextColumn) {
        final int row = position.getRow();
        final int col = position.getColumn();

        if (nextColumn) {
            if (col + 1 < tblvPurInvBatWinEditTableview.getColumns().size()) {
                tblvPurInvBatWinEditTableview.getSelectionModel().select(row, tblvPurInvBatWinEditTableview.getColumns().get(col + 1));
                tblvPurInvBatWinEditTableview.edit(row, tblvPurInvBatWinEditTableview.getColumns().get(col + 1));
            } else if (row + 1 < tblvPurInvBatWinEditTableview.getItems().size()) {
                tblvPurInvBatWinEditTableview.getSelectionModel().select(row + 1, tblvPurInvBatWinEditTableview.getColumns().get(0));
                tblvPurInvBatWinEditTableview.edit(row + 1, tblvPurInvBatWinEditTableview.getColumns().get(0));
            }
        }
    }

    //function for calculation of costing
    private void costingCalculation() {       //function for calculation
        ObservableList<TranxBatchWindowDTO> tableView2Items1 = tblvPurInvBatWinEditTableview.getItems();
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

        int b_qty = Integer.parseInt(b_qtyText);
        int b_freeQty = b_freeQtyText.isEmpty() ? 0 : Integer.parseInt(b_freeQtyText);
        int totalQty = b_qty + b_freeQty;

        double disPerAmt = 0;
        double totaldisAmt = 0;

        double b_rate = Integer.parseInt(b_rateText); // You'll need to get b_rate value from somewhere
        double disPer = b_dis_perText.isEmpty() ? 0 : Double.parseDouble(b_dis_perText);
        disPerAmt = (disPer * b_rate) / 100;
        totaldisAmt += disPerAmt;

        double disAmt = b_dis_amtText.isEmpty() ? 0 : Double.parseDouble(b_dis_amtText);
        totaldisAmt += disAmt;

        double totalAmt = b_rate * b_qty;

        double costVal = (totalAmt - totaldisAmt) / totalQty;
        costValue = costVal;

        double taxAmt = (Double.parseDouble(tax_perText) * totalAmt) / 100;

        double costWithTax = costVal + (taxAmt / totalQty);
        costWithTaxValue = costWithTax;


        lblPurInvBatWinCWOTEDit.setText(String.format("%.3f", costVal));
        lblPurInvBatWinCWTEdit.setText(String.format("%.3f", costWithTax));

        System.out.println("Costing: " + costVal);
        System.out.println("Costing with Tax: " + costWithTax);
    }

    //function for create batch
    private void createProductBatch() {
        purInvCreateLogger.info("Starting of Create product batch");
        try {
            ObservableList<TranxBatchWindowDTO> tableView2Items1 = tblvPurInvBatWinEditTableview.getItems();
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
                purRate = person.getPurRate() != null ? person.getPurRate() : "";
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
            System.out.println("purDate " + purDate + " mfgDate +" + mfgDate + " expDate " + expDate);
            map.put("product_id", selectedRowProductdId);
            map.put("level_a_id", level_a_id);
            map.put("level_b_id", level_b_id);
            map.put("level_c_id", level_c_id);
            map.put("unit_id", unit_id);
            map.put("supplier_id", supplier_id);
            map.put("b_cess_per", "");
            map.put("b_cess_amt", "");
            map.put("b_no", b_no);
            map.put("pur_date", "2023-05-05");
            map.put("manufacturing_date", "2023-06-06");
            map.put("b_expiry", "2025-05-05");
            map.put("mrp", mrp);
            map.put("b_rate", purRate);
            map.put("b_qty", qty);
            map.put("b_freeQty", freeQty);
            map.put("b_dis_per", disPer);
            map.put("b_dis_amt", disAmt);
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
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Success");
                alert.setHeaderText("Batch Created Successfully");
                alert.show();
                PauseTransition delay = new PauseTransition(Duration.seconds(4));
                delay.setOnFinished(event -> alert.close());
                delay.play();
                //  Add an initial empty row
                hbapPurInvBatchWindowEdit.setVisible(false);

            }
            System.out.println("selectedRowIndex" + selectedRowIndex);
            CmpTRowDTO selectedItem = tvPurInvEditCMPTRow.getItems().get(selectedRowIndex);

            System.out.println("selectedItem" + selectedItem.getParticulars());
            selectedItem.setBatchNo(b_no);
            selectedItem.setRate(purRate);
            selectedItem.setQuantity(qty);
            selectedItem.setDis_amt(disAmt);
            selectedItem.setDis_per(disPer);
            selectedItem.setB_no(b_no);
            selectedItem.setB_rate(purRate);
            selectedItem.setRate_a(fsr);
            selectedItem.setRate_b(csr);
            selectedItem.setRate_c(saleRate);
            selectedItem.setCosting(costValue.toString());
            selectedItem.setCosting_with_tax(costWithTaxValue.toString());
            selectedItem.setMin_margin(margin);
            selectedItem.setManufacturing_date(mfgDate);
            selectedItem.setB_purchase_rate(purRate);
            selectedItem.setB_expiry(expDate);
            selectedItem.setB_details_id(b_details_id);
            selectedItem.setIs_batch(String.valueOf(true));
            selectedItem.setSerialNo("");
            selectedItem.setReference_id("");
            selectedItem.setReference_type("");

            Platform.runLater(() -> {
                tvPurInvEditCMPTRow.refresh();
            });
            rowCalculation(selectedRowIndex);
        } catch (Exception e) {
            purInvCreateLogger.error("error in creating batch in pur invoice " + e.getMessage());
        }


    }

    private double roundDigit(double value, int decimalPlaces) {
        double factor = Math.pow(10, decimalPlaces);
        return Math.round(value * factor) / factor;
    }

    public void setFSRWithMargin() {
        ObservableList<TranxBatchWindowDTO> tableView2Items1 = tblvPurInvBatWinEditTableview.getItems();
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


        int rowIndex = tblvPurInvBatWinEditTableview.getSelectionModel().getSelectedIndex();
        TranxBatchWindowDTO selectedItem = tblvPurInvBatWinEditTableview.getItems().get(rowIndex);

        // Verify data
        System.out.println("New Sale Rate: " + saleRate);
        selectedItem.setB_rate_a(b_rate_aFSR);

        // Update UI on JavaFX Application Thread
        Platform.runLater(() -> {
            tblvPurInvBatWinEditTableview.refresh();
        });


    }

    //function for selected product data show at bottom
    public void getSelectedProdDataInfo(String productId) {
        try {
            purInvCreateLogger.info("start of fetching Selected Product Data show in product info");
            Map<String, String> body = new HashMap<>();
            body.put("product_id", productId);
            String requestBody = Globals.mapToStringforFormData(body);
            HttpResponse<String> response = APIClient.postFormDataRequest(requestBody, "transaction_product_details");
            System.out.println("Product Details response body" + response.body());
            JsonObject jsonObject = new Gson().fromJson(response.body(), JsonObject.class);
            ObservableList<TranxProductWindowDTO> observableList = FXCollections.observableArrayList();
            if (jsonObject.get("responseStatus").getAsInt() == 200) {
                myTabPane.getSelectionModel().select(tabPurInvProductInfoEdit);

                JsonObject item = jsonObject.get("result").getAsJsonObject();
                System.out.println("Item:" + item);
                String brand = item.get("brand").getAsString();
                String hsn = item.get("hsn").getAsString();
                String group = item.get("group").getAsString();
                String category = item.get("category").getAsString();
                String supplier = item.get("supplier").getAsString();
                String tax_type = item.get("tax_type") != null ? item.get("tax_type").getAsString() : "";
                String tax = item.get("tax_per") != null ? item.get("tax_per").getAsString() : "";
                String margin = item.get("margin_per") != null ? item.get("margin_per").toString() : "";
                String cost = item.get("cost").getAsString();
                String shelfId = item.get("shelf_id") != null ? item.get("shelf_id").getAsString() : "";
                String min_stock = item.get("min_stocks").getAsString();
                String max_stock = item.get("max_stocks").getAsString();
                //setting data in Product details block in sales quotation page
                txtPurInvProductBrandEdit.setText(brand);
                txtPurInvProductHsnEdit.setText(hsn);
                txtPurInvProductGroupEdit.setText(group);
                txtPurInvProductCategoryEdit.setText(category);
//                pr.setText(supplier);
                txtPurInvProductTaxTypeEdit.setText(tax_type);
                txtPurInvProductTaxPerEdit.setText(tax);
                txtPurInvProductMarginPerEdit.setText(margin);
                txtPurInvProductCostEdit.setText(cost);
                txtPurInvProductShelfIdEdit.setText(shelfId);
                txtPurInvProductMinStockEdit.setText(min_stock);
                txtPurInvProductMaxStockEdit.setText(max_stock);
            } else {
                System.out.println("Error in response");
            }
        } catch (Exception e) {
            purInvCreateLogger.error("Error in Fetching Selected Product data is" + e.getMessage());
            e.printStackTrace();
        }
    }

    //function for get the purchase accounts-->>
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
            cmbPurInvEditPurcAC.setItems(accList);
            cmbPurInvEditPurcAC.getSelectionModel().selectFirst();
            cmbPurInvEditPurcAC.setConverter(new StringConverter<CommonDTO>() {
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

    private void rowCalculation(int rowIndex) {
        CmpTRowDTO tableView2Items1 = tvPurInvEditCMPTRow.getItems().get(rowIndex);
        String r_qty = "";
        double r_rate = 0L;
        double base_amt = 0L;
        double row_fin_grossAmt = 0L;
        double r_dis_amt = 0L;
        double r_dis_per = 0L;
        double r_tax_per = 0L;
        double total_dis_amt = 0L;
        double total_dis_per = 0L;
        double row_dis_amt = 0L;
        double total_grossAmt = 0l;
        double totalTax = 0L;
        double total_amt = 0L;
        double net_amt = 0L;
        double gross_amt = 0L;
        double total_base_amt = 0L;
        double dis_amt_cal = 0L;
        Double total_taxable_amt = 0.0;
        double taxable_amt = 0;
        Double igst = 0.0;
        Double cgst = 0.0;
        Double sgst = 0.0;
        Double totalIgst = 0.0;
        Double totalCgst = 0.0;
        Double totalSgst = 0.0;
        Double prevTaxPer = 0.0;
        Double finDisAmtCal = 0.0;

        r_qty = tableView2Items1.getQuantity();
        r_rate = Double.parseDouble(tableView2Items1.getRate());
        if (!tableView2Items1.getDis_amt().isEmpty())
            r_dis_amt = Double.parseDouble(tableView2Items1.getDis_amt());
        if (!tableView2Items1.getDis_per().isEmpty())
            r_dis_per = Double.parseDouble(tableView2Items1.getDis_per());
        r_tax_per = Double.parseDouble(tableView2Items1.getTax_per());
        prevTaxPer = r_tax_per;
        base_amt = Integer.parseInt(r_qty) * r_rate;
        gross_amt = base_amt;
        total_amt = base_amt;
        System.out.println("total Amount 1 :" + total_amt);
        total_base_amt = base_amt;
        total_dis_amt = r_dis_amt;

        System.out.println("total_dis_amt:" + total_dis_amt);
        if (r_dis_amt > 0) {
            gross_amt = gross_amt - total_dis_amt;
            total_amt = total_amt - total_dis_amt;
            System.out.println("total Amount 2 :" + total_amt);
            total_base_amt = total_amt;
            dis_amt_cal = total_dis_amt;

            row_dis_amt = row_dis_amt + total_dis_amt;
        }

        if (r_dis_per > 0) {
            total_dis_per = (r_dis_per / 100) * (gross_amt);
            System.out.println("total_dis_per:" + total_dis_per);
            gross_amt = gross_amt - total_dis_per;
            total_base_amt = total_base_amt - total_dis_per;
            total_amt = total_amt - total_dis_per;
            System.out.println("total Amount 2 :" + total_amt);
            row_dis_amt = row_dis_amt + total_dis_per;
            taxable_amt = total_amt;
        }

        if (r_tax_per > 0) {
            totalTax = (r_tax_per / 100) * (total_amt);
            net_amt = total_amt + totalTax;
        }

        System.out.println("total Amount 3 :" + total_amt);


        CmpTRowDTO selectedItem = tvPurInvEditCMPTRow.getItems().get(rowIndex);
        selectedItem.setGross_amt(String.valueOf(base_amt));
        selectedItem.setNet_amt(String.valueOf(net_amt));
        selectedItem.setOrg_net_amt(String.valueOf(net_amt));
        selectedItem.setTaxable_amt(taxable_amt);
        selectedItem.setTotal_taxable_amt(total_taxable_amt);
        selectedItem.setSgst(totalTax / 2);
        selectedItem.setCgst(totalTax / 2);
        selectedItem.setFinTaxAmt(totalTax);
        selectedItem.setFinal_dis_amt(row_dis_amt);

        tblcPurInvEditCmptGrossAmt.setCellValueFactory(new PropertyValueFactory<>("gross_amt"));
        tblcPurInvEditCmptNetAmt.setCellValueFactory(new PropertyValueFactory<>("net_amt"));
        calculateGst();
    }

    //function for calculation of Bottom GST, CGST, SGST
    private void calculateGst() {

        Map<Double, Double> cgstTotals = new HashMap<>();
        Map<Double, Double> sgstTotals = new HashMap<>();

//        tblvPurChallGST.getItems().clear();

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

        Double totalFinalSgst = 0.00; // to store total SGST Amt
        Double totalFinalCgst = 0.00; // to store total CGST Amt
        Double totalFinalIgst = 0.00;

        // Calculate totals for each transaction
        for (CmpTRowDTO cmpTRowDTO : tvPurInvEditCMPTRow.getItems()) {
            taxPercentage = Double.parseDouble(cmpTRowDTO.getTax_per());
            int quantity = Integer.parseInt(cmpTRowDTO.getQuantity());

            if (cmpTRowDTO.getFree_qty().isEmpty()) {
                freeQuantity = 0;
            } else {
                freeQuantity = Integer.parseInt(cmpTRowDTO.getFree_qty());
            }

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

        // Print or display totals
        ObservableList<GstDTO> observableList = FXCollections.observableArrayList();
        for (Double taxPer : new Double[]{3.0, 5.0, 12.0, 18.0}) {
            double totalCGST = cgstTotals.get(taxPer);
            double totalSGST = sgstTotals.get(taxPer);
            double totalIGST = 0.0;

            if (totalCGST > 0) {
//                GstDTO gstDto :
                System.out.println("Tax Percentage: " + taxPer + "%");
                System.out.println("Total CGST: " + totalCGST);
                System.out.println("Total SGST: " + totalSGST);
                System.out.println();

                observableList.add(new GstDTO(String.format("%.2f", taxPer), String.format("%.2f", totalCGST), String.format("%.2f", totalSGST), String.valueOf(totalIGST))
                );

            }
            tcPurInvBottomGSTEdit.setCellValueFactory(new PropertyValueFactory<>("taxPer"));
            tcPurInvBottomCGSTEdit.setCellValueFactory(new PropertyValueFactory<>("cgst"));
            tcPurInvBottomSGSTEdit.setCellValueFactory(new PropertyValueFactory<>("sgst"));

        }
        tblvPurInvCreateGSTEdit.setItems(observableList);
//        tblvPurChallGST.getItems().addAll(observableList);
        Platform.runLater(() -> {
            tblvPurInvCreateGSTEdit.refresh();
        });


        lblPurInvTotalQtyEdit.setText(String.valueOf(totalQuantity));
        lblPurInvFreeQtyEdit.setText(String.valueOf(totalFreeQuantity));
        lblPurInvBillAmountEdit.setText(String.format("%.2f", totalNetAmt));
        lblPurInvGrossTotalEdit.setText(String.format("%.2f", totalGrossAmt));
        lblPurInvTotalDiscountEdit.setText(String.format("%.2f", totalDisAmt));
        lblPurInvTotalTaxableAmountEdit.setText(String.format("%.2f", totaltaxableAmt));
        lblPurInvTotalTaxEdit.setText(String.format("%.2f", totalTaxAmt));
        lblPurInvTotalSGSTEdit.setText(String.format("%.2f", totalFinalSgst));
        lblPurInvTotalCGSTEdit.setText(String.format("%.2f", totalFinalCgst));

    }

    private void discountPropotionalCalculation() {
        //get discount percentage and discount amount to textfield
        String disproportionalPer = tfPurInvAllRowDisPerEdit.getText();
        String disproportionalAmt = tfPurInvAllRowDisAmtEdit.getText();
        String additionalCharges = tfPurchaseInvoiceAddChrgTotalAmt.getText();
        Double dis_proportional_per = 0.0;//store the textfield discount per String to double
        Double dis_proportional_amt = 0.0;//store the textfield discount amt String to double
        if (!disproportionalPer.isEmpty()) {
            dis_proportional_per = Double.parseDouble(disproportionalPer);
        } else {
            dis_proportional_per = 0.0;
        }
        if (!disproportionalAmt.isEmpty()) {
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
        for (int i = 0; i < tvPurInvEditCMPTRow.getItems().size(); i++) {
            rowCalculation(i);//call row calculation function
            CmpTRowDTO cmpTRowDTO1 = tvPurInvEditCMPTRow.getItems().get(i);
            totalTaxableAmt = totalTaxableAmt + cmpTRowDTO1.getTaxable_amt();

        }
        System.out.println("totalTaxableAmt discountPropotionalCalculation :: " + totalTaxableAmt);


        //get data of netamount, tax per,and row taxable amount from cmptrow
        for (CmpTRowDTO cmpTRowDTO : tvPurInvEditCMPTRow.getItems()) {

            netAmt = Double.parseDouble(cmpTRowDTO.getNet_amt());
            r_tax_per = Double.parseDouble(cmpTRowDTO.getTax_per());
            rowTaxableAmt = cmpTRowDTO.getTaxable_amt();
            System.out.println("rowTaxableAmt discountPropotionalCalculation :: " + rowTaxableAmt + "netAmt" + netAmt);

            //calculate discount proportional percentage and  the taxable amount
            if (dis_proportional_per >= 0.0) {
                totalDisPropAmt = (dis_proportional_per / 100) * (totalTaxableAmt);
                rowDisPropAmt = (rowTaxableAmt * totalDisPropAmt) / totalTaxableAmt;
                rowTaxableAmt = rowTaxableAmt - rowDisPropAmt;
                totalTaxableAmtAdditional = rowTaxableAmt;
            } else {
                totalTaxableAmtAdditional = rowTaxableAmt;
            }

            //calculate discount proportional amount and  the taxable amount

            if (dis_proportional_amt >= 0.0) {
                totalDisPropAmt = dis_proportional_amt;
                rowDisPropAmt = (rowTaxableAmt * totalDisPropAmt) / totalTaxableAmt;
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
            cmpTRowDTO.setCgst(total_tax_amt / 2);
            cmpTRowDTO.setSgst(total_tax_amt / 2);
            cmpTRowDTO.setFinTaxAmt(total_tax_amt);
            cmpTRowDTO.setFinal_dis_amt(rowDisPropAmt);
            Platform.runLater(() -> {
                tvPurInvEditCMPTRow.refresh();
            });
            //Display net Amount to table view
            tblcPurInvEditCmptNetAmt.setCellValueFactory(new PropertyValueFactory<>("net_amt"));

        }
        //Check additional charge is Empty or not if not empty call to the additional charges function
        if (!tfPurchaseInvoiceAddChrgTotalAmt.getText().isEmpty()) {
            additionalChargesCalculation();
        } else {
            additionalChargesCalculation(); // need to discuss with Shrikant And Sneha
        }
        calculateGst();
    }

    private void additionalChargesCalculation() {
        //get data from additional charges text field
        String additionalCharges = tfPurchaseInvoiceAddChrgTotalAmt.getText();

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
            for (int i = 0; i < tvPurInvEditCMPTRow.getItems().size(); i++) {
                CmpTRowDTO cmpTRowDTO1 = tvPurInvEditCMPTRow.getItems().get(i);
                totalTaxableAmt = totalTaxableAmt + cmpTRowDTO1.getTotal_taxable_amt();

            }

//            System.out.println("additionalChargeAmt totalTaxableAmt" + totalTaxableAmt);
            //get all table view row data and add additinal charges to net amount
            for (CmpTRowDTO cmpTRowDTO : tvPurInvEditCMPTRow.getItems()) {

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
                    tvPurInvEditCMPTRow.refresh();
                });
                tblcPurInvEditCmptNetAmt.setCellValueFactory(new PropertyValueFactory<>("net_amt"));
            }
        }
        //call calculate gst function
        calculateGst();

    }

    //    public void calculateVal() {      //but we have make the additional charges window common
//        double totalAmt = 0.0;
//
//        for (AdditionalChargesRowDTO item : tblcPurchaseChallanAddtnlChrgCmpTRowView.getItems()) {
//            String amtValueStr = tblcPurchaseChallanAddtnlChrgCmpTRowAmt.getCellData(item);
//            double amtValue = Double.parseDouble(amtValueStr);
//            totalAmt += amtValue;
//            System.out.println(amtValueStr);
//        }
//
//        System.out.println("Total amount: " + totalAmt);
//
//        Double finalTotalAmt = totalAmt;
//
//        btnPurChallAddChgsSubmit.setOnAction(event -> {
//            hbapPurchaseChallanAddnlChrgWindow.setVisible(false);
//            tfPurchaseChallanTotalAmt.setText(String.valueOf(finalTotalAmt));
//            tfPurchaseChallanTotalAmt.requestFocus();
//            additionalChargesCalculation();
//        });
//
//
//        ObservableList<TablePosition> selectedCells = tblcPurchaseChallanAddtnlChrgCmpTRowView.getSelectionModel().getSelectedCells();
//        HashSet<Integer> rows = new HashSet<>();
//        for (TablePosition tablePosition : selectedCells) {
//            rows.add(tablePosition.getRow());
//        }
//
//    }
    //IMPORTANT -- function for get the edit data by id
    private void getPurInvEditDataById() {
        Map<String, String> body = new HashMap<>();
        body.put("id", id.toString());
        String formData = Globals.mapToStringforFormData(body);
        HttpResponse<String> response = APIClient.postFormDataRequest(formData, "get_purchase_invoice_by_id_new");
        PurInvoiceEditDTO responseBody = new Gson().fromJson(response.body(), PurInvoiceEditDTO.class);
        System.out.println("responseBody in purch Inv" + response.body());
        if (responseBody.getResponseStatus() == 200) {
            setPurInvoiceFormData(responseBody);
        }
    }

    private void setPurInvoiceFormData(PurInvoiceEditDTO responseBody) {
        System.out.println("TranxPurchaseEditMainDTO responseBody " + responseBody);
        LocalDate tranxDate = LocalDate.parse(responseBody.getInvoiceData().getTransactionDt());
        LocalDate invoiceDate = LocalDate.parse(responseBody.getInvoiceData().getInvoiceDt());
        supplierId = responseBody.getInvoiceData().getSupplierId();
        dpPurInvEditTranxDate.setValue(tranxDate);
        dpPurInvEditInvDate.setValue(invoiceDate);
        tfPurInvEditInvNo.setText(responseBody.getInvoiceData().getInvoiceNo());
        tfPurInvEditPurSerial.setText(String.valueOf(responseBody.getInvoiceData().getPurchaseSrNo()));
        lblPurInvGrossTotalEdit.setText(String.valueOf(responseBody.getGrossTotal()));
        lblPurInvTotalDiscountEdit.setText(String.valueOf(responseBody.getTotalPurchaseDiscountAmt()));
        lblPurInvTotalTaxableAmountEdit.setText(String.valueOf(responseBody.getTotalTax()));
        lblPurInvTotalTaxEdit.setText(String.valueOf(responseBody.getTotalTax()));
        tfPurInvLedgNameEdit.setText(String.valueOf(responseBody.getInvoiceData().getSupplierName()));
        tfPurInvAllRowDisPerEdit.setText(String.valueOf(responseBody.getDiscountInPer()));
        tfPurchaseInvoiceAddChrgTotalAmt.setText(String.valueOf(responseBody.getInvoiceData().getAdditionalChargesTotal()));
        lblPurInvTotalQtyEdit.setText(String.valueOf(responseBody.getTotalQty()));
        lblPurInvFreeQtyEdit.setText(String.valueOf(responseBody.getTotalFreeQty()));
        lblPurInvRoundOffEdit.setText(String.valueOf(responseBody.getInvoiceData().getRoundoff()));
        chbRoundOffEdit.setSelected(responseBody.getInvoiceData().getRoundOffCheck());
        if (responseBody.getInvoiceData().getPaymentMode().equalsIgnoreCase("cash")) {
            rbPaymentModeCashEdit.setSelected(true);
        } else rbPaymentModeCreditEdit.setSelected(true);
        System.out.println("responseBody DIscount AMount" + responseBody.getDiscountInAmt());
        if (responseBody.getDiscountInAmt() != null) {
            Platform.runLater(() -> {
                tfPurInvAllRowDisAmtEdit.setText(String.valueOf(responseBody.getDiscountInAmt()));
            });
        }

        if (responseBody.getGstDetails() != null) {
            System.out.println(responseBody.getGstDetails());

            ObservableList<GstDetailsDTO> gstDetailsList = FXCollections.observableArrayList();
            gstDetailsList.addAll(responseBody.getGstDetails());
            cmbPurInvEditSupplierGST.setItems(gstDetailsList);

            String gstNo = responseBody.getInvoiceData().getGstNo();
            for (GstDetailsDTO gstDetailsDTO : gstDetailsList) {
                if (gstDetailsDTO.getGstNo().equalsIgnoreCase(gstNo)) {
                    cmbPurInvEditSupplierGST.getSelectionModel().select(gstDetailsDTO);
                }
            }

            cmbPurInvEditSupplierGST.setConverter(new StringConverter<GstDetailsDTO>() {
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
        tvPurInvEditCMPTRow.getItems().clear();
        for (PurchaseInvoiceRowEditDTO mRow : responseBody.getRow()) {
            CmpTRowDTO row = new CmpTRowDTO();
            ArrayList<TranxPurRowDTO> rowData = new ArrayList<>();

            row.setParticulars(mRow.getProductName());
            row.setPacking(mRow.getPacking());
            row.setQuantity("" + mRow.getQty());
            row.setBatchNo(mRow.getBatchNo());
//            row.setUnit(mRow.getUnitId());
            row.setRate(String.valueOf(mRow.getRate()));
            row.setGross_amt(String.valueOf(mRow.getGrossAmt()));
            row.setDis_per(String.valueOf(mRow.getDisPer()));
            row.setDis_amt(String.valueOf(mRow.getDisAmt()));
            row.setTax_per(String.valueOf(mRow.getIgst()));
            row.setNet_amt(String.valueOf(mRow.getFinalAmt()));
            row.setFree_qty(String.valueOf(mRow.getFreeQty()));
            row.setIgst(mRow.getIgst());
            row.setSgst(mRow.getSgst());
            row.setCgst(mRow.getCgst());
            row.setTotal_igst(mRow.getTotalIgst());
            row.setTotal_sgst(mRow.getTotalSgst());
            row.setTotal_cgst(mRow.getTotalCgst());
            row.setTax_per(String.valueOf(mRow.getGst()));
            row.setTaxable_amt(mRow.getGrossAmt());
            String productId = String.valueOf(mRow.getProductId());
            System.out.println("prodcttidd " + productId);
            row.setProduct_id(productId);
            row.setLevel_a_id(mRow.getLevelAId());
            row.setLevel_b_id(mRow.getLevelBId());
            row.setLevel_c_id(mRow.getLevelCId());
            row.setUnit(mRow.getUnitName());
            row.setB_rate(String.valueOf(mRow.getbRate()));
            row.setRate_a(String.valueOf(mRow.getMinRateA()));
            row.setRate_b(String.valueOf(mRow.getMinRateB()));
            row.setRate_c(String.valueOf(mRow.getMinRateC()));
            row.setCosting(String.valueOf(mRow.getCosting()));
            row.setCosting_with_tax(String.valueOf(mRow.getCostingWithTax()));
            row.setMin_margin(String.valueOf(mRow.getMarginPer()));
            row.setManufacturing_date(mRow.getManufacturingDate() != null ? mRow.getManufacturingDate() : "");
            row.setB_purchase_rate(String.valueOf(mRow.getPurchaseRate()));
            row.setB_expiry(mRow.getbExpiry());
            row.setB_details_id(String.valueOf(mRow.getDetailsId()));
            row.setIs_batch(String.valueOf(true));
            row.setBatchNo(mRow.getBatchNo());
            row.setFinal_amt(mRow.getFinalAmt());
//            row.setSerialNo(mRow);

            tvPurInvEditCMPTRow.getItems().add(row);

            index++;
        }
        tvPurInvEditCMPTRow.refresh();


    }

    public void updatePurchaseInvoice() {
        LocalDate invoiceDate = dpPurInvEditInvDate.getValue();
        LocalDate tranxDate = dpPurInvEditTranxDate.getValue();
        Double cgst = 0.00;
        Double totalCgst = 0.00;
        Double sgst = 0.00;
        Double totalSgst = 0.00;
        Double totalIgst = 0.00;
        for (GstDTO gstdto : tblvPurInvCreateGSTEdit.getItems()) {
            cgst = Double.valueOf(gstdto.getCgst());
            totalCgst += cgst;
            sgst = Double.valueOf(gstdto.getCgst());
            totalSgst += sgst;
            totalIgst = totalSgst + totalCgst;

        }
        // FormData for Create API
        Map<String, String> map = new HashMap<>();
        System.out.println("iddd " + id + "   supplierId " + supplierId + "  selectedLedgerId " + selectedLedgerId);
        map.put("id", String.valueOf(id));
        map.put("supplier_code_id", String.valueOf(supplierId));
        map.put("invoice_date", String.valueOf(invoiceDate));
        map.put("newReference", "false");
        map.put("invoice_no", tfPurInvEditInvNo.getText());
        map.put("purchase_id", cmbPurInvEditPurcAC.getValue().getId());
        map.put("purchase_sr_no", "7");
        map.put("transaction_date", String.valueOf(tranxDate));
        map.put("isRoundOffCheck", "true");
        map.put("roundoff", lblPurInvRoundOffEdit.getText());
        map.put("totalamt", lblPurInvBillAmountEdit.getText());
        map.put("total_purchase_discount_amt", lblPurInvTotalDiscountEdit.getText());
        map.put("gstNo", cmbPurInvEditSupplierGST.getValue() != null ? cmbPurInvEditSupplierGST.getValue().getGstNo() : "");
        map.put("totalcgst", String.valueOf(totalCgst));
        map.put("totalsgst", String.valueOf(totalSgst));
        map.put("totaligst", String.valueOf(totalIgst));
        map.put("tcs", "0");
        map.put("purchase_discount", "0");
        map.put("purchase_discount_amt", "0");
        map.put("total_purchase_discount_amt", "0");
        map.put("additionalChargesTotal", "0");
        map.put("additionalCharges", "[]");
        map.put("total_qty", lblPurInvTotalQtyEdit.getText());
        map.put("total_free_qty", lblPurInvFreeQtyEdit.getText());
        map.put("total_row_gross_amt", lblPurInvTotalTaxableAmountEdit.getText());
        map.put("total_base_amt", lblPurInvGrossTotalEdit.getText());
        map.put("total_invoice_dis_amt", lblPurInvTotalDiscountEdit.getText());
        map.put("taxable_amount", lblPurInvTotalTaxableAmountEdit.getText());
        map.put("total_tax_amt", lblPurInvTotalTaxEdit.getText());
        map.put("bill_amount", lblPurInvBillAmountEdit.getText());
        map.put("taxFlag", "true");// static data as of now
        // taxCalulation Array adding in the formdata
        JsonArray jsonArray = new JsonArray();
        for (GstDTO gstDTO : tblvPurInvCreateGSTEdit.getItems()) {
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
        /***** mapping Row Data into TranxPurRowDTO *****/
        ArrayList<TranxPurRowDTO> rowData = new ArrayList<>();
        System.out.println("cmptroW length " + tvPurInvEditCMPTRow.getItems().size());

        for (CmpTRowDTO cmpTRowDTO : tvPurInvEditCMPTRow.getItems()) {
            if (!cmpTRowDTO.getProduct_id().equalsIgnoreCase("")) {
                TranxPurRowDTO purParticularRow = new TranxPurRowDTO();
                System.out.println("cmpTRowDTO.getProduct_id()) " + cmpTRowDTO.getProduct_id());

                purParticularRow.setProductId(cmpTRowDTO.getProduct_id());

                if (!cmpTRowDTO.getLevel_a_id().isEmpty()) {
                    purParticularRow.setLevelaId(cmpTRowDTO.getLevel_a_id());
                } else {
                    purParticularRow.setLevelaId("");
                }
                if (!cmpTRowDTO.getLevel_b_id().isEmpty()) {
                    purParticularRow.setLevelbId(cmpTRowDTO.getLevel_b_id());
                } else {
                    purParticularRow.setLevelbId("");
                }
                if (!cmpTRowDTO.getLevel_c_id().isEmpty()) {
                    purParticularRow.setLevelcId(cmpTRowDTO.getLevel_c_id());
                } else {
                    purParticularRow.setLevelcId("");
                }
                if (!cmpTRowDTO.getUnit().isEmpty()) {
                    purParticularRow.setUnitId("1");
                } else {
                    purParticularRow.setUnitId("1");
                }
                if (cmpTRowDTO.getUnit_conv() != null) {
                    purParticularRow.setUnitConv(String.valueOf(cmpTRowDTO.getUnit_conv()));
                } else {
                    purParticularRow.setUnitConv("0");
                }
                if (!cmpTRowDTO.getQuantity().isEmpty()) {
                    System.out.println("QTY=>" + (cmpTRowDTO.getQuantity().split("\\.")[0]));
                    purParticularRow.setQty(cmpTRowDTO.getQuantity().contains(".") ? cmpTRowDTO.getQuantity().split("\\.")[0] : cmpTRowDTO.getQuantity());
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
                    purParticularRow.setGrossAmt(String.valueOf(cmpTRowDTO.getGross_amt()));
                } else {
                    purParticularRow.setGrossAmt("0.0");
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
                if (cmpTRowDTO.getCgst() != null) {
                    purParticularRow.setCgst(String.valueOf(cmpTRowDTO.getCgst()));
                } else {
                    purParticularRow.setCgst("0");
                }
                if (cmpTRowDTO.getIgst() != null) {
                    purParticularRow.setIgst(String.valueOf(cmpTRowDTO.getIgst()));
                } else {
                    purParticularRow.setIgst("0");
                }
                if (cmpTRowDTO.getSgst() != null) {
                    purParticularRow.setSgst(String.valueOf(cmpTRowDTO.getSgst()));
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
                    purParticularRow.setSgst("0");
                }
                if(cmpTRowDTO.getSgst() != null) {
                    purParticularRow.setSgst(String.valueOf(cmpTRowDTO.getSgst()));
                } else {
                    purParticularRow.setSgst("0");
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
                purParticularRow.setDetailsId(String.valueOf(0));
//            if(cmpTRowDTO.getIs_serial() == true){      tobe uncomment
//                if(!cmpTRowDTO.getSerialNo().isEmpty()) {
//                    purParticularRow.setSerialNo(null);
//                }else {
//                    purParticularRow.setSerialNo(null);
//                }
//            }else purParticularRow.setSerialNo(null);

//            if(cmpTRowDTO.getReference_id().isEmpty()) {     tobe uncomment
//                purParticularRow.setReferenceId(cmpTRowDTO.getReference_id());
//            }else {
//                purParticularRow.setReferenceId("");
//            }
//            if(cmpTRowDTO.getReference_type().isEmpty()) {
//                purParticularRow.setReferenceType(cmpTRowDTO.getReference_type());
//            }else {
//                purParticularRow.setReferenceType("");
//            }
                rowData.add(purParticularRow);
            }

        }
        String mRowData = new Gson().toJson(rowData, new TypeToken<List<TranxPurRowDTO>>() {
        }.getType());
        map.put("row", mRowData);
        Map<String, String> headers = new HashMap<>();
        String finalReq = Globals.mapToString(map);
        System.out.println("final Request" + finalReq);
        String response;
        response = APIClient.postMultipartRequest(map, null, "edit_purchase_invoices", headers);
        JsonObject responseBody = new Gson().fromJson(response, JsonObject.class);
        System.out.println("responseBody Purchase Challan" + responseBody);

        if (responseBody.get("responseStatus").getAsInt() == 200) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Success");
            alert.setHeaderText("Purchase Invoice Updated Successfully");
            alert.show();
            PauseTransition delay = new PauseTransition(Duration.seconds(4));
            delay.setOnFinished(event -> alert.close());
            delay.play();

        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Internal Server Error Occured");
            alert.show();
            PauseTransition delay = new PauseTransition(Duration.seconds(4));
            delay.setOnFinished(event -> alert.close());
            delay.play();
        }
    }

}
