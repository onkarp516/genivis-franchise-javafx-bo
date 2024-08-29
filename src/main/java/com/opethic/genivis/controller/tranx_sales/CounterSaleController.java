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
import com.opethic.genivis.controller.master.ProductListController;
import com.opethic.genivis.controller.tranx_purchase.*;
import com.opethic.genivis.controller.tranx_sales.common.TranxCommonPopUps;
import com.opethic.genivis.controller.tranx_sales.invoice.TranxSalesInvoiceCreate;
import com.opethic.genivis.dto.*;
import com.opethic.genivis.controller.commons.SwitchButton;
import com.opethic.genivis.dto.CmpTRowDTO;
import com.opethic.genivis.dto.PatientDTO;
import com.opethic.genivis.dto.TranxBatchWindowDTO;
import com.opethic.genivis.dto.TranxProductWindowDTO;
import com.opethic.genivis.dto.cmp_t_row.BatchWindowTableDTO;
import com.opethic.genivis.dto.counter.CSToSInvDTO;
import com.opethic.genivis.dto.counter.CSToSInvRowDTO;
import com.opethic.genivis.dto.pur_invoice.*;
import com.opethic.genivis.dto.pur_invoice.reqres.RowListDTO;
import com.opethic.genivis.dto.reqres.ResponseMsg;
import com.opethic.genivis.dto.reqres.product.Communicator;
import com.opethic.genivis.dto.reqres.product.TableCellCallback;
import com.opethic.genivis.dto.reqres.pur_tranx.*;
import com.opethic.genivis.dto.reqres.sales_tranx.*;
import com.opethic.genivis.dto.tranx_sales.CmpTRowDTOSoToSc;
import com.opethic.genivis.models.tranx.sales.TranxSelectedBatch;
import com.opethic.genivis.models.tranx.sales.TranxSelectedProduct;
import com.opethic.genivis.network.APIClient;
import com.opethic.genivis.network.EndPoints;
import com.opethic.genivis.network.RequestType;
import com.opethic.genivis.utils.*;
import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.InputMethodEvent;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.util.StringConverter;
import org.apache.http.util.TextUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.controlsfx.control.tableview2.filter.filtereditor.SouthFilter;

import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URL;
import java.net.http.HttpResponse;
import java.sql.SQLOutput;
import java.sql.SQLTransactionRollbackException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

import static com.opethic.genivis.network.EndPoints.*;
import static com.opethic.genivis.utils.FxmFileConstants.SALES_INVOICE_CREATE_SLUG;
import static com.opethic.genivis.utils.FxmFileConstants.SALES_ORDER_CREATE_SLUG;
import static com.opethic.genivis.utils.Globals.decimalFormat;
import static com.opethic.genivis.utils.Globals.mapToStringforFormData;
import static javafx.scene.control.PopupControl.USE_COMPUTED_SIZE;
import static javafx.scene.control.PopupControl.USE_PREF_SIZE;

public class CounterSaleController implements Initializable {


    @FXML
    private Button addRowInCounterSale, btnCounterSaleBatWinClose, btnCounterSaleSubmit, btnCounterSaleCancel;

    @FXML
    private TableView<CounterSaleRowDTO> tblvCounterSaleView;
    @FXML
    private TableColumn<CounterSaleRowDTO, String> tblcCounterSaleSrNo, tblcCounterSaleParticular, tblcCounterSalePackage, tblcCounterSaleUnit, tblcCounterSaleBatchNo, tblcCounterSaleQty, tblcCounterSaleRate,
            tblcCounterSaleDIscPer, tblcCounterSaleNetAmt, tblcCounterSaleAction, tblcCounterSaleLevelA, tblcCounterSaleLevelB, tblcCounterSaleLevelC;
    @FXML
    private HBox hbapCounterSaleProductWindow, hbapCounterSaleBatchWindow, hbCSSubCan;
    @FXML
    private TableView tblvPurChallanPrdWindtableview, tblvConsumerSalePatientWindtableview;
    @FXML
    private TableColumn tblcCounterSalePrdWindCode, tblcCounterSalePrdWindPrdName, tblcCounterSalePrdWindPacking, tblcCounterSalePrdWindBarcode, tblcCounterSalePrdWindBrand,
            tblcCounterSalePrdWindMrp, tblcCounterSalePrdWindCurrStk, tblcCounterSalePrdWindUnit, tblcCounterSalePrdWindSaleRate, tblcCounterSalePrdWindAction;
    @FXML
    private Button btnCounterSalePrdWinAddProduct, btnCounterSalePrdWindowClose, btnCounterSaleToSaleInvoice, btnConsumerPrescription;

    @FXML
    private TableView tblvCounterSaleBatWinCreTableview;
    @FXML
    private TableColumn tblcCounterSaleBatWinBatchNo, tblcCounterSaleBatWinPurDate, tblcCounterSaleBatWinMFGDate, tblcCounterSaleBatWinEXPDate, tblcCounterSaleBatWinMRP, tblcCounterSaleBatWinPurRate,
            tblcCounterSaleBatWinQty, tblcCounterSaleBatWinFreeQty, tblcCounterSaleBatWinDisPer, tblcCounterSaleBatWinDisAmt, tblcCounterSaleBatWinBarcode, tblcCounterSaleBatWinMarginPer,
            tblcCounterSaleBatWinFSR, tblcCounterSaleBatWinCSR, tblcCounterSaleBatWinSaleRate, tblcCounterSaleBatWinAction;

    @FXML
    private TableView tblvCounterSaleList;

    @FXML
    private TableColumn<CounterSaleDTO, String> tblcCounterSaleListInvNo, tblcCounterSaleListInvDate, tblcCounterSaleListMobile, tblcCounterSaleListQty, tblcCounterSaleListBillAmt, tblcCounterSaleListBillAmtInv, tblcCounterSaleListPayMode, tblcCounterSaleListProduct,
            tblcCounterSaleListPackage, tblcCounterSaleListUnit, tblcCounterSaleListBatch, tblcCounterSaleListRate, tblcCounterSaleListDis;
    @FXML
    private TableColumn<CounterSaleDTO, Boolean> tblcCounterSaleSelect;

    @FXML
    private TableView tblcConsumerSaleList;

    @FXML
    private TableColumn tblcConsumerSaleListInvNo, tblcConsumerSaleListInvDate, tblcConsumerSaleListClientName, tblcConsumerSaleListClientAddress, tblcConsumerSaleListMobile,
            tblcConsumerSaleListQty, tblcConsumerSaleListBillAmt, tblcConsumerSaleListSelect, tblcConsumerSaleListPayMod;


    @FXML
    private Label lblCounterSaleBatWinPrdName, lblCounterSaleBatWinCWT, lblCounterSaleBatWinCWOT, lblCounterSaleBatWinTax;
    @FXML
    private TextField tfCounterSaleBatWinBatSearch;
    @FXML
    private HBox topRow, secondRow, secondInnerRightHb, thirdRow, dividerMain;
    @FXML
    private Label lblCounterSaleGrossTotal, lblCounterSaleDis, lblCounterSaleTotal, lblCounterSaleTax, lblCounterSaleBillAmt, lblCSDisPer;

    @FXML
    private RadioButton rbCounterSaleInvoice, rbCounterSaleProducts, rbCounterSalePMAll, rbCounterSalePMCash, rbCounterSalePMMulti;

    @FXML
    private TextField tfCounterCreateMobile, tfConsumerDoctorName, tfConsumerCName, tfConsumerCAddress, tfConsumerDoctorAddress, tfCounterSaleDiscPer;


    private static ObservableList<String> unitList = FXCollections.observableArrayList();

    private String productId = "";
    public String CounterToSaleInvId = "";

    private String responseBody;
    private int selectedRowIndex;
    private static int rowIndexParticular;
    private static int rowIndexBatchNo;
    private String prdBatchTaxPer;
    private String selectedRowPrdId;
    private String productIdSelected;

    private int rowIndexSelected;
    private String selectedValue = "", id = "", message = "", patientId = "", doctorId = "", counterId = "", consumerId = "",
            radioPaymentMode = "all", radioDataType = "invoice";


    private Boolean isConsumer = false;
    @FXML
    private SwitchButton switchConsumer;
    @FXML
    private VBox vboxConsumer;

    @FXML
    private HBox hbapCounterSalePatientMaster, hbapCounterSaleDoctorName;

    @FXML
    private TableView tblvCounterSalePatientMaster;

    @FXML
    private TableColumn tblcCounterSalePatMasCode, tblcCounterSalePatMasPatientName, tblcCounterSalePatMasMobileNo, tblcCounterSalePatMasAddress;

    @FXML
    private TableView tblvCounterSaleDoctor;

    @FXML
    private TableColumn tblcCounterSaleDoctorName;

    @FXML
    private Button btnCounterSaleDoctorClose, btnCounterSalePatientMasterClose, btnCounterSaleAddPatient, btnCounterSaleDoctorAdd;

    @FXML
    private HBox lblConsumerCName, lblConsumerDoctorName;

    @FXML
    private Label lblConsumerCAddress, lblConsumerDoctorAddress, lblConsumerPrescription;
    @FXML

    private BorderPane spCounterSaleRootPane;
    private JsonObject jsonObject = null;
    private static final Logger counterSaleLogger = LogManager.getLogger(CounterSaleController.class);

    private ToggleGroup toggleGroupPayMode = new ToggleGroup();
    private ToggleGroup toggleGroupDatType = new ToggleGroup();
    private ToggleGroup toggleGroup = new ToggleGroup();

    private int Selected_index = 0;
    private ObservableList<CounterSaleDTO> originalData;

    @FXML
    private VBox vboxCmpTRow;

    @FXML
    private ComboBox<String> cmbCSPaymentMode;

    private ObservableList<CommonDTO> doctorList = FXCollections.observableArrayList();

    private ObservableList<String> payment_mode_list = FXCollections.observableArrayList("Cash", "Multi");

    File selectedFileCS;

    @FXML
    private TabPane tpCounterSales;
    @FXML
    private Tab tabCSCounterSale, tabCSConsumerSale, tabCSHistory;

    private String focusInd = "";
    private Integer rowIndexCounter = -1;
    private Integer rowIndexConsumer = -1;

    private Integer multiIn = -1;

    Map<String, Object> multi_bank_map = new HashMap<>();

    Map<String, String> multi_mode_map = new HashMap<>();

    private CSMultiPaymentReqDTO multiPaymentDTO = null;

    private String csInvDate = "";

    private Integer rediCurrIndex = -1;
    private Boolean isProductRed = false;
    private Integer purPrdRedId = -1;
    private Integer drugIndex = -1;
    private Boolean isRedirect;

    private JsonArray deletedCSRows = new JsonArray();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        ResponsiveWiseCssPicker();

        // Resposive Tables
        responsiveCmpt();
        counterSaleListTableDesign();
        consumerSaleListTableDesign();

        cmbCSPaymentMode.setItems(payment_mode_list);
        cmbCSPaymentMode.getSelectionModel().select("Cash");
        cmbCSPaymentMode.focusedProperty().addListener((obs, wasFocused, isNowFocused) -> {
            if (isNowFocused) {
                this.cmbCSPaymentMode.show();
            }
        });
        multiPaymentDTO = new CSMultiPaymentReqDTO();
        cmbCSPaymentMode.getSelectionModel().selectedItemProperty().addListener((options, oldValue, newValue) -> {

            Stage stage = (Stage) spCounterSaleRootPane.getScene().getWindow();
            String selectedItem = cmbCSPaymentMode.getSelectionModel().getSelectedItem();
            Double totalAmt = lblCounterSaleBillAmt.getText().isEmpty() ? 0.0 : Double.valueOf(lblCounterSaleBillAmt.getText());

            if (multiIn == -1) {
                if (totalAmt > 0.0) {
                    if ("Multi".equals(selectedItem)) {
                        SingleInputDialogs.openMultiPaymentPopUp(stage, "Multi", totalAmt, multiPaymentDTO, input -> {
                            multiPaymentDTO = input;
                            btnCounterSaleSubmit.requestFocus();
                        });
                    }
                } else {
                    cmbCSPaymentMode.getSelectionModel().selectFirst();
                }

            }
        });
        cmbCSPaymentMode.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                multiIn = -1;
            }
        });



        //AutoFocus on Mobile Number Field
        Platform.runLater(() -> tfCounterCreateMobile.requestFocus());

        //Enter traversal
        initialEnterMethod();

        // TableInitialization i.e. first row of Table
        tableInitiliazation();

        // Consumer Switch ON/OFF Button Fields Visibilty
        consumerSaleFieldsVisibility();

        toggleGroupsInitial();

        //Counter sale list API
        getCounterSale();
//        populateUniqueEntries(tblvCounterSaleList, originalData, true);
        //Consumer sale list API
        getConsumerSale();

        //Convert toinvoice button logic
        btnCounterSaleToSaleInvoice.setOnAction(e -> {

            getCounterSaleToSaleInvByIdData();
        });

        tpCounterSales.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                if (newValue.getText().equalsIgnoreCase("Counter Sale")) {
                    getCounterSale();
                } else if (newValue.getText().equalsIgnoreCase("Consumer Sale")) {
                    getConsumerSale();
                }
            }
        });

        //get Countersale Id
        tblvCounterSaleList.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                CounterSaleDTO selectedItem = (CounterSaleDTO) tblvCounterSaleList.getSelectionModel().getSelectedItem();
                if (selectedItem != null) {
                    counterId = selectedItem.getId();
                    if (!counterId.isEmpty()) {
                        rowIndexCounter = tblvCounterSaleList.getSelectionModel().getSelectedIndex();
                        csInvDate = selectedItem.getInvoiceDate();
                        getCounterSaleByIdData();
                    }
                } else {
                    counterId = "";
                }

            }
        });

        //get Consumer by Id
        tblcConsumerSaleList.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                CounterSaleDTO selectedItem = (CounterSaleDTO) tblcConsumerSaleList.getSelectionModel().getSelectedItem();
                if (selectedItem != null) {
                    consumerId = selectedItem.getId();
                    if (!consumerId.isEmpty()) {
                        rowIndexConsumer = tblcConsumerSaleList.getSelectionModel().getSelectedIndex();

                        getConsumerSaleByIdData();
                    }
                } else {
                    consumerId = "";
                }
            }
        });


        //create counter sale API call
        btnCounterSaleSubmit.setOnAction(event -> {
            csInvoiceValidations();
        });

        btnCounterSaleCancel.setOnAction(e -> {
            AlertUtility.AlertConfirmation(AlertUtility.alertTypeConfirmation, "Are You Sure ?", input -> {
                if (input == 1) {
                    callClearTable();
                }
            });
        });

        //Add prescription pop up window open
        btnConsumerPrescription.setOnAction(event -> {
            consumerPrescriptionPopUp();
        });


        // Set cell factory for the "Serial Number" column
        tblcCounterSaleSrNo.setCellFactory(column -> {
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


        tfConsumerCName.setOnKeyTyped(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                patientPopUpWithCreate();
            }
        });
        tfConsumerCName.setOnMouseClicked(actionEvent -> {
            patientPopUpWithCreate();
        });
        tfConsumerDoctorName.setOnKeyTyped(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                doctorPopUpWithCreate();
            }
        });
        tfConsumerDoctorName.setOnMouseClicked(actionEvent -> {
            doctorPopUpWithCreate();
        });


        sceneInitilization();


        //checkbox creation in select column to Convert CS TO SI
        checkboxSelectionCStoSI();

        counterPaymentModeData();

        counterDataTypeFilterData();


        CommonValidationsUtils.restrictMobileNumber(tfCounterCreateMobile);

        rbCounterSaleInvoice.setOnAction(e -> populateUniqueEntries(tblvCounterSaleList, originalData, rbCounterSaleInvoice.isSelected()));

        shortcutKeysCS();

//        tfConsumerDoctorAddress.setOnKeyPressed(new EventHandler<KeyEvent>() {
//            @Override
//            public void handle(KeyEvent keyEvent) {
//                System.out.println("KeyEvent Pressed >> " + keyEvent);
//                System.out.println("KeyEvent Pressed GetCode  >> " + keyEvent.getCode());
//                if(keyEvent.getCode() == KeyCode.ENTER){
//                    System.out.println("KeyEvent Pressed in IF Block >> ");
//
//                }
//            }
//        });

//        tfConsumerDoctorAddress.addEventFilter(KeyEvent.KEY_PRESSED, e->{
//            System.out.println("drugIndex >> "+ drugIndex);
//            if(e.getCode() == KeyCode.ENTER){
//                System.out.println("drugIndex >> "+ e.getCode());
//                TableColumn<CounterSaleRowDTO, ?> colName = tblvCounterSaleView.getColumns().get(1);
//                tblvCounterSaleView.edit(drugIndex, colName);
//            }
//        });

        tfConsumerDoctorName.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal) {
                if (tfConsumerDoctorName.getText().isEmpty()) {
                    tfConsumerDoctorName.requestFocus();
                }
            }
        });


    }

    private void patientPopUpWithCreate() {
        Stage stage = (Stage) spCounterSaleRootPane.getScene().getWindow();
        SingleInputDialogs.openPatientPopUp(stage, "Patients", input -> {
                    tfConsumerCName.setText(input[1].toString());
                    tfConsumerCAddress.setText(input[2].toString());
                    patientId = String.valueOf(input[0]);

                    if (tfConsumerCAddress != null && !tfConsumerCAddress.getText().isEmpty()) {
                        tfConsumerDoctorName.requestFocus();
                    } else {
                        tfConsumerCAddress.requestFocus();
                    }
                },
                isNewPat -> {
                    if (isNewPat == true) {
                        SingleInputDialogs.patientCreatePopUp(stage, "Add New Patient", input -> {

                            Map<String, String> map = new HashMap<>();
                            map.put("patientName", input[0]);
                            map.put("age", "");
                            map.put("weight", "");
                            map.put("mobileNumber", input[1]);
                            map.put("patientAddress", input[2]);
                            map.put("birthDate", "");
                            map.put("idNo", "");
                            map.put("gender", input[3]);
                            map.put("pincode", "");
                            map.put("bloodGroup", "");
                            map.put("tbDiagnosisDate", "");
                            map.put("tbTreatmentInitiationDate", "");

                            HttpResponse<String> response;
                            String formData = Globals.mapToStringforFormData(map);
                            response = APIClient.postFormDataRequest(formData, EndPoints.PATIENT_CREATE_ENDPOINT);
                            ResponseMsg responseBody = new Gson().fromJson(response.body(), ResponseMsg.class);
                            message = responseBody.getMessage();
                            if (responseBody.getResponseStatus() == 200) {
                                AlertUtility.AlertSuccessTimeout(AlertUtility.alertTypeSuccess, message, e -> {
                                    tfConsumerCName.setText(input[0].toString());
                                    tfConsumerCAddress.setText(input[2].toString());
                                    patientId = responseBody.getData();
                                    tfConsumerDoctorName.requestFocus();
                                });
                            }

                        });
                    }
                }
        );

    }

    private void doctorPopUpWithCreate(){
        Stage stage = (Stage) spCounterSaleRootPane.getScene().getWindow();
        SingleInputDialogs.openDoctorPopUp(stage, "Doctors", input -> {
                    doctorId = String.valueOf(input[0]);
                    tfConsumerDoctorName.setText(input[1].toString());
                    tfConsumerDoctorAddress.setText(input[2] != null ? input[2].toString() : "");

                    if (tfConsumerDoctorAddress != null && !tfConsumerDoctorAddress.getText().isEmpty()) {
                        TableColumn<CounterSaleRowDTO, ?> colName = tblvCounterSaleView.getColumns().get(1);
                        tblvCounterSaleView.edit(0, colName);
                    } else {
                        tfConsumerDoctorAddress.requestFocus();
                    }

                },
                isNewPat -> {
                    if (isNewPat == true) {
                        SingleInputDialogs.doctorCreatePopUp(stage, "Add New Doctor", input -> {

                            Map<String, String> map = new HashMap<>();
                            map.put("doctorName", input[0]);
                            map.put("mobileNumber", input[1]);
                            map.put("hospitalAddress", input[2]);
                            map.put("specialization", input[3]);
                            map.put("hospitalName", "");
                            map.put("commision", "");
                            map.put("qualification", "");
                            map.put("registerNo", "");

                            HttpResponse<String> response;
                            String formData = Globals.mapToStringforFormData(map);
                            response = APIClient.postFormDataRequest(formData, EndPoints.DOCTOR_CREATE_ENDPOINT);
                            ResponseMsg responseBody = new Gson().fromJson(response.body(), ResponseMsg.class);
                            message = responseBody.getMessage();
                            if (responseBody.getResponseStatus() == 200) {
                                AlertUtility.AlertSuccessTimeout(AlertUtility.alertTypeSuccess, message, e -> {
                                    tfConsumerDoctorName.setText(input[0].toString());
                                    tfConsumerDoctorAddress.setText(input[2].toString());
                                    doctorId = responseBody.getResponseObject().toString();
                                    TableColumn<CounterSaleRowDTO, ?> colName = tblvCounterSaleView.getColumns().get(1);
                                    tblvCounterSaleView.edit(0, colName);
                                });
                            }

                        });
                    }
                });
    }

    private void csInvoiceValidations() {
        //openAdjustBillWindow();
        Stage stage = (Stage) spCounterSaleRootPane.getScene().getWindow();
        if (isConsumer == true) {
            if (tfConsumerCName.getText().isEmpty()) {
                AlertUtility.AlertErrorTimeout(AlertUtility.alertTypeError, "Select Consumer Name", output -> {
                    tfConsumerCName.requestFocus();
                });
            } else if (tfConsumerDoctorName.getText().isEmpty()) {
                AlertUtility.AlertErrorTimeout(AlertUtility.alertTypeError, "Select Mandatory Doctor Name Field ", output -> {
                    tfConsumerDoctorName.requestFocus();
                });
            } else {
                if (btnCounterSaleSubmit.getText().equalsIgnoreCase("Submit")) {
                    AlertUtility.AlertConfirmation(AlertUtility.alertTypeConfirmation, "Do You Want to Submit ?", input -> {
                        if (input == 1) {
                            createCounterSale();
                        }
                    });
                } else {
                    AlertUtility.AlertConfirmation(AlertUtility.alertTypeConfirmation, "Do You Want to Update ?", input -> {
                        if (input == 1) {
                            createCounterSale();
                        }
                    });
                }

            }
        } else {
            if (btnCounterSaleSubmit.getText().equalsIgnoreCase("Submit")) {
                AlertUtility.AlertConfirmation(AlertUtility.alertTypeConfirmation, "Do You Want to Submit ?", input -> {
                    if (input == 1) {
                        createCounterSale();
                    }
                });
            } else {
                AlertUtility.AlertConfirmation(AlertUtility.alertTypeConfirmation, "Do You Want to Update ?", input -> {
                    if (input == 1) {
                        createCounterSale();
                    }
                });
            }

        }

    }

    private void ResponsiveWiseCssPicker() {
        double height = Screen.getPrimary().getBounds().getHeight();
        double width = Screen.getPrimary().getBounds().getWidth();
        if (width >= 992 && width <= 1024) {
            topRow.setSpacing(2);
            secondRow.setSpacing(10);
            secondInnerRightHb.setSpacing(6);
            thirdRow.setSpacing(6);
            secondRow.prefWidthProperty().bind(dividerMain.widthProperty().multiply(0.85));
            secondInnerRightHb.prefWidthProperty().bind(dividerMain.widthProperty().multiply(0.15));
            spCounterSaleRootPane.getStylesheets().add(GenivisApplication.class.getResource("ui/css/counterSaleStyle/counterSaleStyle1.css").toExternalForm());
        } else if (width >= 1025 && width <= 1280) {
            topRow.setSpacing(8);
            secondRow.setSpacing(12);
            secondInnerRightHb.setSpacing(7);
            thirdRow.setSpacing(7);
            secondRow.prefWidthProperty().bind(dividerMain.widthProperty().multiply(0.85));
            secondInnerRightHb.prefWidthProperty().bind(dividerMain.widthProperty().multiply(0.15));
            spCounterSaleRootPane.getStylesheets().add(GenivisApplication.class.getResource("ui/css/counterSaleStyle/counterSaleStyle2.css").toExternalForm());
        } else if (width >= 1281 && width <= 1368) {
            topRow.setSpacing(8);
            secondRow.setSpacing(12);
            secondInnerRightHb.setSpacing(7);
            thirdRow.setSpacing(7);
            secondRow.prefWidthProperty().bind(dividerMain.widthProperty().multiply(0.85));
            secondInnerRightHb.prefWidthProperty().bind(dividerMain.widthProperty().multiply(0.15));
            spCounterSaleRootPane.getStylesheets().add(GenivisApplication.class.getResource("ui/css/counterSaleStyle/counterSaleStyle3.css").toExternalForm());
        } else if (width >= 1369 && width <= 1400) {
            topRow.setSpacing(4);
            secondRow.setSpacing(15);
            secondInnerRightHb.setSpacing(8);
            thirdRow.setSpacing(8);
            secondRow.prefWidthProperty().bind(dividerMain.widthProperty().multiply(0.85));
            secondInnerRightHb.prefWidthProperty().bind(dividerMain.widthProperty().multiply(0.15));
            spCounterSaleRootPane.getStylesheets().add(GenivisApplication.class.getResource("ui/css/counterSaleStyle/counterSaleStyle4.css").toExternalForm());
        } else if (width >= 1401 && width <= 1440) {
            topRow.setSpacing(6);
            secondRow.setSpacing(15);
            secondInnerRightHb.setSpacing(8);
            thirdRow.setSpacing(8);
            secondRow.prefWidthProperty().bind(dividerMain.widthProperty().multiply(0.85));
            secondInnerRightHb.prefWidthProperty().bind(dividerMain.widthProperty().multiply(0.15));
            spCounterSaleRootPane.getStylesheets().add(GenivisApplication.class.getResource("ui/css/counterSaleStyle/counterSaleStyle5.css").toExternalForm());
        } else if (width >= 1441 && width <= 1680) {
            topRow.setSpacing(20);
            secondRow.setSpacing(20);
            secondInnerRightHb.setSpacing(10);
            thirdRow.setSpacing(10);
            secondRow.prefWidthProperty().bind(dividerMain.widthProperty().multiply(0.85));
            secondInnerRightHb.prefWidthProperty().bind(dividerMain.widthProperty().multiply(0.15));
            spCounterSaleRootPane.getStylesheets().add(GenivisApplication.class.getResource("ui/css/counterSaleStyle/counterSaleStyle6.css").toExternalForm());
        } else if (width >= 1681 && width <= 1920) {
            topRow.setSpacing(20);
            secondRow.setSpacing(20);
            secondInnerRightHb.setSpacing(10);
            thirdRow.setSpacing(10);
            secondRow.prefWidthProperty().bind(dividerMain.widthProperty().multiply(0.85));
            secondInnerRightHb.prefWidthProperty().bind(dividerMain.widthProperty().multiply(0.15));
            spCounterSaleRootPane.getStylesheets().add(GenivisApplication.class.getResource("ui/css/counterSaleStyle/counterSaleStyle7.css").toExternalForm());
        }
    }

    public void populateUniqueEntries(TableView<CounterSaleDTO> tableView, ObservableList<CounterSaleDTO> data, boolean isSelected) {
        // Filter unique entries
        if (isSelected) {
            Set<String> uniqueNames = new HashSet<>();
            ObservableList<CounterSaleDTO> uniqueData = FXCollections.observableArrayList();
            for (CounterSaleDTO dto : data) {
                if (uniqueNames.add(dto.getInvoiceNo())) {
                    double sumAmt = data.stream().filter((v) -> v.getInvoiceNo().equalsIgnoreCase(dto.getInvoiceNo())).mapToDouble((v) -> Double.valueOf(v.getBillAmt())).sum();
                    sumAmt = GlobalTranx.TranxRoundDigit(sumAmt, GlobalTranx.configDecimalPlace);
                    dto.setBillAmtInv(sumAmt + "");
                    uniqueData.add(dto);
                }
            }

            // Set the items in the TableView
            tableView.setItems(uniqueData);
            tblcCounterSaleListBillAmt.setVisible(false);
            tblcCounterSaleListBillAmtInv.setVisible(true);
        }

    }

    public void purc_disc_per(KeyEvent keyEvent) {
        String discText = tfCounterSaleDiscPer.getText();
        if (!TextUtils.isEmpty(discText)) {
            CommonValidationsUtils.restrictToDecimalNumbers(tfCounterSaleDiscPer);
            double totalTaxableAmt = 0.0;
            for (int i = 0; i < tblvCounterSaleView.getItems().size(); i++) {
                counterSaleCalculation.rowCalculationForPurcInvoice(i, tblvCounterSaleView, callback);//call row calculation function
                CounterSaleRowDTO purchaseInvoiceTable = tblvCounterSaleView.getItems().get(i);
                totalTaxableAmt = totalTaxableAmt + Double.parseDouble(purchaseInvoiceTable.getTaxable_amt());

            }
            double disc_per = Double.parseDouble(discText);
            Double amount = (totalTaxableAmt * disc_per) / 100;
            counterSaleCalculation.discountPropotionalCalculation(disc_per + "", "0", "", tblvCounterSaleView, callback);
        } else {
            counterSaleCalculation.discountPropotionalCalculation("0", "0", "", tblvCounterSaleView, callback);
        }
        //? Calculate Tax for each Row
        counterSaleCalculation.calculateGst(tblvCounterSaleView, callback);
    }


    private void initialEnterMethod() {
        spCounterSaleRootPane.addEventFilter(KeyEvent.KEY_PRESSED, (KeyEvent event) -> {
            if (event.getCode() == KeyCode.ENTER) {
                if (event.getTarget() instanceof Button targetButton && targetButton.getText().equalsIgnoreCase("Update")) {
                    targetButton.getText();
                } else if (event.getTarget() instanceof Button targetButton && targetButton.getText().equalsIgnoreCase("submit")) {
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
            }
        });
    }

    private void toggleGroupsInitial() {
        // Creating the Toggle Group
        rbCounterSaleInvoice.setToggleGroup(toggleGroupDatType);
        rbCounterSaleProducts.setToggleGroup(toggleGroupDatType);

        // Creating the Toggle Group
        rbCounterSalePMAll.setToggleGroup(toggleGroup);
        rbCounterSalePMCash.setToggleGroup(toggleGroup);
        rbCounterSalePMMulti.setToggleGroup(toggleGroup);

    }

    private void consumerSaleFieldsVisibility() {
        switchConsumer.setParentBox(vboxConsumer);
        switchConsumer.switchOnProperty().addListener((observable, oldValue, newValue) -> {
            isConsumer = newValue;
            if (newValue == false) {
                tfConsumerCName.setVisible(false);
                tfConsumerCAddress.setVisible(false);
                tfConsumerDoctorName.setVisible(false);
                tfConsumerDoctorAddress.setVisible(false);
                lblCSDisPer.setVisible(false);
                tfCounterSaleDiscPer.setVisible(false);
                lblConsumerCName.setVisible(false);
                lblConsumerCAddress.setVisible(false);
                lblConsumerDoctorName.setVisible(false);
                lblConsumerDoctorAddress.setVisible(false);
                lblConsumerPrescription.setVisible(false);
                btnConsumerPrescription.setVisible(false);
            } else {
                tfConsumerCName.setVisible(true);
                tfConsumerCAddress.setVisible(true);
                tfConsumerDoctorName.setVisible(true);
                tfConsumerDoctorAddress.setVisible(true);
                lblCSDisPer.setVisible(true);
                tfCounterSaleDiscPer.setVisible(true);
                lblConsumerCName.setVisible(true);
                lblConsumerCAddress.setVisible(true);
                lblConsumerDoctorName.setVisible(true);
                lblConsumerDoctorAddress.setVisible(true);
//                lblConsumerPrescription.setVisible(true);
//                btnConsumerPrescription.setVisible(true);
            }
        });
    }

    private void checkboxSelectionCStoSI() {
        tblcCounterSaleSelect.setCellFactory(column -> new CheckBoxTableCell<>());
        tblcCounterSaleSelect.setCellValueFactory(cellData -> {
            CounterSaleDTO cellValue = cellData.getValue();
            BooleanProperty property = cellValue.isRowSelectedProperty();


            // Add listener to handler change
            property.addListener((observable, oldValue, newValue) -> cellValue.setIsRowSelected(newValue));
            findOutSelectedRow();
            if (cellValue.isIsRowSelected()) {
                counterId = cellValue.getId();
                handleSelection(tblvCounterSaleList.getItems());

            } else {
                counterId = "";
            }
            return property;
        });
    }

    public void counterSaleListTableDesign() {

        if (tblcCounterSaleListProduct.isVisible()) {
            tblcCounterSaleSelect.prefWidthProperty().bind(tblvCounterSaleList.widthProperty().multiply(0.03));
            tblcCounterSaleListInvNo.prefWidthProperty().bind(tblvCounterSaleList.widthProperty().multiply(0.08));
            tblcCounterSaleListInvDate.prefWidthProperty().bind(tblvCounterSaleList.widthProperty().multiply(0.06));
            tblcCounterSaleListMobile.prefWidthProperty().bind(tblvCounterSaleList.widthProperty().multiply(0.06));
            tblcCounterSaleListQty.prefWidthProperty().bind(tblvCounterSaleList.widthProperty().multiply(0.05));
            tblcCounterSaleListBillAmtInv.prefWidthProperty().bind(tblvCounterSaleList.widthProperty().multiply(0.09));
            tblcCounterSaleListPayMode.prefWidthProperty().bind(tblvCounterSaleList.widthProperty().multiply(0.05));
            tblcCounterSaleListProduct.prefWidthProperty().bind(tblvCounterSaleList.widthProperty().multiply(0.25));
            tblcCounterSaleListPackage.prefWidthProperty().bind(tblvCounterSaleList.widthProperty().multiply(0.05));
            tblcCounterSaleListUnit.prefWidthProperty().bind(tblvCounterSaleList.widthProperty().multiply(0.05));
            tblcCounterSaleListBatch.prefWidthProperty().bind(tblvCounterSaleList.widthProperty().multiply(0.08));
            tblcCounterSaleListRate.prefWidthProperty().bind(tblvCounterSaleList.widthProperty().multiply(0.07));
            tblcCounterSaleListDis.prefWidthProperty().bind(tblvCounterSaleList.widthProperty().multiply(0.05));
        } else {
            tblcCounterSaleSelect.prefWidthProperty().bind(tblvCounterSaleList.widthProperty().multiply(0.04));
            tblcCounterSaleListInvNo.prefWidthProperty().bind(tblvCounterSaleList.widthProperty().multiply(0.3));
            tblcCounterSaleListInvDate.prefWidthProperty().bind(tblvCounterSaleList.widthProperty().multiply(0.14));
            tblcCounterSaleListMobile.prefWidthProperty().bind(tblvCounterSaleList.widthProperty().multiply(0.14));
            tblcCounterSaleListQty.prefWidthProperty().bind(tblvCounterSaleList.widthProperty().multiply(0.08));
            tblcCounterSaleListBillAmt.prefWidthProperty().bind(tblvCounterSaleList.widthProperty().multiply(0.2));
            tblcCounterSaleListPayMode.prefWidthProperty().bind(tblvCounterSaleList.widthProperty().multiply(0.1));
        }
    }

    public void consumerSaleListTableDesign() {
        tblcConsumerSaleListInvNo.prefWidthProperty().bind(tblcConsumerSaleList.widthProperty().multiply(0.12));
        tblcConsumerSaleListInvDate.prefWidthProperty().bind(tblcConsumerSaleList.widthProperty().multiply(0.07));
        tblcConsumerSaleListClientName.prefWidthProperty().bind(tblcConsumerSaleList.widthProperty().multiply(0.2));
        tblcConsumerSaleListClientAddress.prefWidthProperty().bind(tblcConsumerSaleList.widthProperty().multiply(0.2));
        tblcConsumerSaleListMobile.prefWidthProperty().bind(tblcConsumerSaleList.widthProperty().multiply(0.1));
        tblcConsumerSaleListQty.prefWidthProperty().bind(tblcConsumerSaleList.widthProperty().multiply(0.08));
        tblcConsumerSaleListBillAmt.prefWidthProperty().bind(tblcConsumerSaleList.widthProperty().multiply(0.12));
        tblcConsumerSaleListPayMod.prefWidthProperty().bind(tblcConsumerSaleList.widthProperty().multiply(0.08));

    }

    ///Radio Button selected value get
    public void counterDataTypeFilterData() {
        rbCounterSaleInvoice.setSelected(true);
        toggleGroupDatType.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                tblvCounterSaleList.setItems(originalData);
                RadioButton selectedRadioButton = (RadioButton) newValue;
                toggleGroupDatType.selectToggle(selectedRadioButton);
                radioDataType = selectedRadioButton.getText().toLowerCase();
                HashMap filterLst = new HashMap();
                Boolean data = selectedRadioButton.isSelected();
                if (radioDataType.equalsIgnoreCase("products")) {
                    tblcCounterSaleSelect.setVisible(true);
                    tblcCounterSaleListInvNo.setVisible(true);
                    tblcCounterSaleListInvDate.setVisible(true);
                    tblcCounterSaleListMobile.setVisible(true);
                    tblcCounterSaleListQty.setVisible(true);
                    tblcCounterSaleListBillAmt.setVisible(true);
                    tblcCounterSaleListBillAmtInv.setVisible(false);
                    tblcCounterSaleListProduct.setVisible(true);
                    tblcCounterSaleListPackage.setVisible(true);
                    tblcCounterSaleListUnit.setVisible(true);
                    tblcCounterSaleListBatch.setVisible(true);
                    tblcCounterSaleListRate.setVisible(true);
                    tblcCounterSaleListDis.setVisible(true);
                    tblcCounterSaleListPayMode.setVisible(true);
                    counterSaleListTableDesign();
                } else {
                    tblcCounterSaleSelect.setVisible(true);
                    tblcCounterSaleListInvNo.setVisible(true);
                    tblcCounterSaleListInvDate.setVisible(true);
                    tblcCounterSaleListMobile.setVisible(true);
                    tblcCounterSaleListQty.setVisible(true);
                    tblcCounterSaleListBillAmtInv.setVisible(true);
                    tblcCounterSaleListBillAmt.setVisible(false);
                    tblcCounterSaleListProduct.setVisible(false);
                    tblcCounterSaleListPackage.setVisible(false);
                    tblcCounterSaleListUnit.setVisible(false);
                    tblcCounterSaleListBatch.setVisible(false);
                    tblcCounterSaleListRate.setVisible(false);
                    tblcCounterSaleListDis.setVisible(false);
                    tblcCounterSaleListPayMode.setVisible(true);
                    counterSaleListTableDesign();
                }

            }
        });
    }


    ///Radio Button selected value get
    public void counterPaymentModeData() {
        toggleGroup.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                tblvCounterSaleList.setItems(originalData);
                RadioButton selectedRadioButton = (RadioButton) newValue;
                toggleGroup.selectToggle(selectedRadioButton);
                radioPaymentMode = selectedRadioButton.getText().toLowerCase();
                HashMap filterLst = new HashMap();
                filterTable(radioPaymentMode, tblvCounterSaleList);
            }
        });
    }

    public void filterTable(String filterString, TableView<CounterSaleDTO> tableView) {
        FilteredList<CounterSaleDTO> filteredData = new FilteredList<>(tableView.getItems(), p -> true);
        filteredData.setPredicate(person -> {
            if (filterString == null || filterString.isEmpty()) {

                return true;
            }
            String lowerCaseFilter = filterString.toLowerCase();
            if (person.getPaymentMode().toLowerCase().contains(lowerCaseFilter)) {
                return true;
            }
            return false;
        });

        if (!filteredData.isEmpty()) {
            tblvCounterSaleList.setItems(filteredData);
        } else if (filterString.equalsIgnoreCase("all")) {
            tblvCounterSaleList.setItems(originalData);
        }
    }


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
        Stage stage = (Stage) btnConsumerPrescription.getScene().getWindow();
        selectedFileCS = fileChooser.showOpenDialog(stage);

        // Handle selected file
        if (selectedFileCS != null) {
//            tfPurChallChooseFileText.setText(selectedFile.getName());
        }
    }

    private void handleSelection(ObservableList<CounterSaleDTO> items) {
        String errorMessage = "Different Payment Mode And Invoice Date Selected";
        String jsonFormat = " [%s] ";   //"selectedIds":

        StringJoiner selectedIds = new StringJoiner(",");
        JsonObject obj = new JsonObject();
        JsonArray array = new JsonArray();
        String paymentModeSel = null;

        for (CounterSaleDTO item : items) {
            if (item.isIsRowSelected()) {
                if (paymentModeSel == null) {
                    paymentModeSel = item.getPaymentMode();
                    csInvDate = item.getInvoiceDate();
                } else if (!paymentModeSel.equals(item.getPaymentMode()) || !csInvDate.equalsIgnoreCase(item.getInvoiceDate())) {
                    AlertUtility.AlertErrorTimeout(AlertUtility.alertTypeError, errorMessage, input -> {
                        item.setIsRowSelected(false);
                    });
                    return;
                }
                obj.addProperty("id", item.getId());
                selectedIds.add(String.valueOf(obj));
            }
        }
        array.add(selectedIds.toString());
        // Ledger names are the same, show JSON format
        String jsonResult = "";
        if (!selectedIds.toString().isEmpty()) {
            jsonResult = String.format(jsonFormat, selectedIds);
        }
        CounterToSaleInvId = jsonResult;

    }


    //consumer sale prescription
    public void consumerPrescriptionPopUp() {
        SingleInputDialogs.openAddPrescriptionPopUp(Communicator.stage, "Prescription", input -> {

        });
    }


    private void findOutSelectedRow() {
        Boolean canShowButton = false;
        ObservableList<CounterSaleDTO> list = tblvCounterSaleList.getItems();
        for (CounterSaleDTO object : list) {
            if (object.isIsRowSelected()) {
                canShowButton = true;
                Selected_index = Integer.valueOf(object.getId());
            }
        }
        if (canShowButton) {
            btnCounterSaleToSaleInvoice.setVisible(true);
        } else {
            btnCounterSaleToSaleInvoice.setVisible(false);
        }

    }


    // Scene Initialization
    public void sceneInitilization() {
        PurInvoiceCommunicator.resetFields();
        spCounterSaleRootPane.sceneProperty().addListener((observable, oldScene, newScene) -> {
            if (newScene != null && newScene.getWindow() instanceof Stage) {
                Communicator.stage = (Stage) newScene.getWindow();
                LocalDate current_date = LocalDate.now();
                Communicator.tranxDate = current_date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));

            }
        });
    }


    TableCellCallback<Object[]> callback = item -> {

        lblCounterSaleGrossTotal.setText(String.valueOf(item[3]));
        lblCounterSaleDis.setText(String.valueOf(item[4]));
        lblCounterSaleBillAmt.setText(String.valueOf(item[2]));
        lblCounterSaleTotal.setText(String.valueOf(item[5]));
        lblCounterSaleTax.setText((String) item[6]);

//    tvGST_Table.getItems().clear();
        ObservableList<GstDTO> gstDTOObservableList = FXCollections.observableArrayList();
        ObservableList<?> item9List = (ObservableList<?>) item[9];
        for (Object obj : item9List) {
            if (obj instanceof GstDTO) {
                gstDTOObservableList.add((GstDTO) obj);
            }
        }

//    tvGST_Table.getItems().addAll(gstDTOObservableList);

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

    };

    TableCellCallback<Object[]> consCallback = item -> {
        String calCheck = (String) item [2];
        if ((Boolean) item[0] == true) {
            switchConsumer.switchOnProperty().set(true);
            drugIndex =(Integer) item[1];
            Platform.runLater(() -> {
                if (tfConsumerCName.getText().isEmpty()) {
                    tfConsumerCName.requestFocus();
                } else {
                    TableColumn<CounterSaleRowDTO, ?> colName = tblvCounterSaleView.getColumns().get(6);
                    tblvCounterSaleView.edit((Integer) item[1], colName);
                }
            });
        } else if(calCheck.equalsIgnoreCase("calCheck")) {
            String discText = tfCounterSaleDiscPer.getText();
            if (!TextUtils.isEmpty(discText)) {
                CommonValidationsUtils.restrictToDecimalNumbers(tfCounterSaleDiscPer);
                double totalTaxableAmt = 0.0;
                for (int i = 0; i < tblvCounterSaleView.getItems().size(); i++) {
                    counterSaleCalculation.rowCalculationForPurcInvoice(i, tblvCounterSaleView, callback);//call row calculation function
                    CounterSaleRowDTO purchaseInvoiceTable = tblvCounterSaleView.getItems().get(i);
                    totalTaxableAmt = totalTaxableAmt + Double.parseDouble(purchaseInvoiceTable.getTaxable_amt());

                }
                double disc_per = Double.parseDouble(discText);
                Double amount = (totalTaxableAmt * disc_per) / 100;
                counterSaleCalculation.discountPropotionalCalculation(disc_per + "", "0", "", tblvCounterSaleView, callback);
            } else {
                counterSaleCalculation.discountPropotionalCalculation("0", "0", "", tblvCounterSaleView, callback);
            }
            //? Calculate Tax for each Row
            counterSaleCalculation.calculateGst(tblvCounterSaleView, callback);
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
//                setCSDataToProduct(); // Redirection Done but Code Is Commented
            }
        }
    };
    //new start
    TableCellCallback<Integer> unit_callback = currentIndex -> {
        CounterSaleRowDTO tranxRow = tblvCounterSaleView.getItems().get(currentIndex);
        //! ? Check Stock & isNegetive product Unit wise rate set
        boolean isNegetiveAllowed = tranxRow.getSelectedProduct().getNegetive();
        if (tranxRow.getUnitWiseactStock() > 0 || isNegetiveAllowed == true) {
//            if (tranxRow.getRate().isEmpty()) {
                tranxRow.setRate(tranxRow.getUnitWiseRateMH() + "");
//            }
        } else {
            AlertUtility.AlertError(AlertUtility.alertTypeError, "Selected Unit has Out of Stock! Available Stock : " + tranxRow.getUnitWiseactStock(), input -> {
                TableColumn<CounterSaleRowDTO, ?> colName = tblvCounterSaleView.getColumns().get(7);
                tblvCounterSaleView.edit(currentIndex, colName);
            });
        }
//        tvSalesOrderCmpTRow.getItems().set(currentIndex, tranxRow);
    };

    TableCellCallback<Object[]> delCallBack = item -> {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("del_id", item[0].toString());
        deletedCSRows.add(jsonObject);
        if(deletedCSRows.size() > 0){
            String discText = tfCounterSaleDiscPer.getText();
            if (!TextUtils.isEmpty(discText)) {
                CommonValidationsUtils.restrictToDecimalNumbers(tfCounterSaleDiscPer);
                double totalTaxableAmt = 0.0;
                for (int i = 0; i < tblvCounterSaleView.getItems().size(); i++) {
                    counterSaleCalculation.rowCalculationForPurcInvoice(i, tblvCounterSaleView, callback);//call row calculation function
                    CounterSaleRowDTO purchaseInvoiceTable = tblvCounterSaleView.getItems().get(i);
                    totalTaxableAmt = totalTaxableAmt + Double.parseDouble(purchaseInvoiceTable.getTaxable_amt());

                }
                double disc_per = Double.parseDouble(discText);
                Double amount = (totalTaxableAmt * disc_per) / 100;
                counterSaleCalculation.discountPropotionalCalculation(disc_per + "", "0", "", tblvCounterSaleView, callback);
            } else {
                counterSaleCalculation.discountPropotionalCalculation("0", "0", "", tblvCounterSaleView, callback);
            }
            //? Calculate Tax for each Row
            counterSaleCalculation.calculateGst(tblvCounterSaleView, callback);
        }
    };
    //new end


    public void tableInitiliazation() {

        tblvCounterSaleView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tblvCounterSaleView.setEditable(true);
        tblvCounterSaleView.setFocusTraversable(false);


//        Label headerLabel = new Label("Sr\nNo.");
//        tblcCounterSaleSrNo.setGraphic(headerLabel);

        tblvCounterSaleView.getItems().addAll(new CounterSaleRowDTO("", "0", "1", "", "", "", "", "", "", "", "", "", "", "", "", "", "", ""));

        tblcCounterSaleSrNo.setCellValueFactory(cellData -> cellData.getValue().sr_noProperty());
        tblcCounterSaleSrNo.setStyle("-fx-alignment: CENTER;");

        tblcCounterSalePackage.setCellValueFactory(cellData -> cellData.getValue().packagesProperty());
        tblcCounterSalePackage.setStyle("-fx-alignment: CENTER;");

        tblcCounterSaleLevelB.setCellValueFactory(cellData -> cellData.getValue().levelBProperty());
        tblcCounterSaleLevelB.setCellFactory(column -> new ComboBoxTableCellForCSLevelB("tblcCounterSaleLevelB"));

        tblcCounterSaleLevelA.setCellValueFactory(cellData -> cellData.getValue().levelAProperty());
        tblcCounterSaleLevelA.setCellFactory(column -> new ComboBoxTableCellForCSLevelA("tblcCounterSaleLevelA"));

        tblcCounterSaleLevelC.setCellValueFactory(cellData -> cellData.getValue().levelCProperty());
        tblcCounterSaleLevelC.setCellFactory(column -> new ComboBoxTableCellForCSLevelC("tblcCounterSaleLevelC"));

        tblcCounterSaleUnit.setCellValueFactory(cellData -> cellData.getValue().unitProperty());
        tblcCounterSaleUnit.setCellFactory(column -> new ComboBoxTableCellForCSUnit("tblcCounterSaleUnit", unit_callback));

        tblcCounterSaleParticular.setCellValueFactory(cellData -> cellData.getValue().particularsProperty());
        tblcCounterSaleParticular.setCellFactory(column -> new TextFieldTableCellForCounterSale("tblcCounterSaleParticular", callback, consCallback, cmbCSPaymentMode, addPrdCalbak));


        tblcCounterSaleBatchNo.setCellValueFactory(cellData -> cellData.getValue().batch_or_serialProperty());
        tblcCounterSaleBatchNo.setCellFactory(column -> new TextFieldTableCellForCounterSale("tblcCounterSaleBatchNo", callback));


        tblcCounterSaleQty.setCellValueFactory(cellData -> cellData.getValue().quantityProperty());
        tblcCounterSaleQty.setCellFactory(column -> new TextFieldTableCellForCounterSale("tblcCounterSaleQty", callback, consCallback));

        tblcCounterSaleRate.setCellValueFactory(cellData -> cellData.getValue().rateProperty());
        tblcCounterSaleRate.setCellFactory(column -> new TextFieldTableCellForCounterSale("tblcCounterSaleRate", callback));

        tblcCounterSaleDIscPer.setCellValueFactory(cellData -> cellData.getValue().dis_perProperty());
        tblcCounterSaleDIscPer.setCellFactory(column -> new TextFieldTableCellForCounterSale("tblcCounterSaleDIscPer", callback, cmbCSPaymentMode));

        tblcCounterSaleNetAmt.setCellValueFactory(cellData -> cellData.getValue().net_amountProperty());
//        tblcCounterSaleNetAmt.setCellFactory(column -> new TextFieldTableCellForCounterSale("tblcCounterSaleNetAmt", callback));

        tblcCounterSaleAction.setCellValueFactory(cellData -> cellData.getValue().net_amountProperty());
        tblcCounterSaleAction.setCellFactory(column -> new ButtonTableCellForCounterSale(delCallBack));

        columnVisibility(tblcCounterSaleLevelA, Globals.getUserControlsWithSlug("is_level_a"));
        columnVisibility(tblcCounterSaleLevelB, Globals.getUserControlsWithSlug("is_level_b"));
        columnVisibility(tblcCounterSaleLevelC, Globals.getUserControlsWithSlug("is_level_c"));

        tblcCounterSaleLevelA.setText(Globals.getUserControlsNameWithSlug("is_level_a") + "");
        tblcCounterSaleLevelB.setText(Globals.getUserControlsNameWithSlug("is_level_B") + "");
        tblcCounterSaleLevelC.setText(Globals.getUserControlsNameWithSlug("is_level_C") + "");


    }

    private void columnVisibility(TableColumn<CounterSaleRowDTO, String> column, boolean visible) {
        if (visible) {
//              column.setPrefWidth(USE_COMPUTED_SIZE);
//            column.setMinWidth(USE_PREF_SIZE);
            column.setMaxWidth(Double.MAX_VALUE);
        } else {
            column.prefWidthProperty().unbind();
            column.setPrefWidth(0.0);
            column.setMinWidth(0.0);
            column.setMaxWidth(0.0);
        }
    }


    public void responsiveCmpt() {
        tblcCounterSaleSrNo.prefWidthProperty().bind(tblvCounterSaleView.widthProperty().multiply(0.03));
        tblcCounterSaleParticular.prefWidthProperty().bind(tblvCounterSaleView.widthProperty().multiply(0.35));
        tblcCounterSalePackage.prefWidthProperty().bind(tblvCounterSaleView.widthProperty().multiply(0.04));
        tblcCounterSaleLevelA.prefWidthProperty().bind(tblvCounterSaleView.widthProperty().multiply(0.06));
        tblcCounterSaleLevelB.prefWidthProperty().bind(tblvCounterSaleView.widthProperty().multiply(0.06));
        tblcCounterSaleLevelC.prefWidthProperty().bind(tblvCounterSaleView.widthProperty().multiply(0.06));
        tblcCounterSaleUnit.prefWidthProperty().bind(tblvCounterSaleView.widthProperty().multiply(0.06));
        tblcCounterSaleBatchNo.prefWidthProperty().bind(tblvCounterSaleView.widthProperty().multiply(0.06));
        tblcCounterSaleQty.prefWidthProperty().bind(tblvCounterSaleView.widthProperty().multiply(0.05));
        tblcCounterSaleRate.prefWidthProperty().bind(tblvCounterSaleView.widthProperty().multiply(0.06));
        tblcCounterSaleDIscPer.prefWidthProperty().bind(tblvCounterSaleView.widthProperty().multiply(0.06));
        tblcCounterSaleNetAmt.prefWidthProperty().bind(tblvCounterSaleView.widthProperty().multiply(0.06));
        tblcCounterSaleAction.prefWidthProperty().bind(tblvCounterSaleView.widthProperty().multiply(0.06));

    }


    public void createCounterSale() {


        String mobileNo = tfCounterCreateMobile.getText();
        DateConvertUtil.convertDateToLocalDate(new Date());
        Map<String, String> map = new HashMap<>();
        map.put("id", counterId);

        map.put("bill_dt", DateConvertUtil.convertDateToLocalDate(new Date()).toString());
        if (!mobileNo.isEmpty()) {
            map.put("mobile_number", mobileNo);
        }
//        map.put("totalqty", totalQty.toString());

        map.put("paymentMode", cmbCSPaymentMode.getValue().toString());
        map.put("total_base_amt", lblCounterSaleGrossTotal.getText());
        map.put("total_row_gross_amt", lblCounterSaleGrossTotal.getText());
        map.put("bill_amount", lblCounterSaleBillAmt.getText());
        map.put("taxable_amount", lblCounterSaleTotal.getText());
        map.put("total_invoice_dis_amt", lblCounterSaleDis.getText());
        if (cmbCSPaymentMode.getValue().toString().equalsIgnoreCase("Multi")) {
            map.put("cashAmt", String.valueOf(multiPaymentDTO.getCashAmt()));
        } else {
            map.put("cashAmt", lblCounterSaleGrossTotal.getText());
        }


        List<CounterSaleRowDTO> currentItems = new ArrayList<>(tblvCounterSaleView.getItems());
        if (!currentItems.isEmpty()) {
            CounterSaleRowDTO lastItem = currentItems.get(currentItems.size() - 1);

            if (lastItem.getParticulars() != null && lastItem.getParticulars().isEmpty()) {
                currentItems.remove(currentItems.size() - 1);
            }
        }
        List<CounterSaleRowDTO> list = new ArrayList<>(currentItems);

        ArrayList<TranxPurRowDTO> rowData = new ArrayList<>();
        Integer qty = 0;
        Integer totalQty = 0;
        Double freeQty = 0.0;
        Double totalFreeQty = 0.0;

        for (CounterSaleRowDTO cmpTRowDTO : list) {
            TranxPurRowDTO purParticularRow = new TranxPurRowDTO();
            purParticularRow.setProductId(cmpTRowDTO.getProduct_id());
            if (!cmpTRowDTO.getDetails_id().isEmpty() || cmpTRowDTO.getDetails_id()
                    != null) {
                purParticularRow.setDetailsId(cmpTRowDTO.getDetails_id());
            } else {
                purParticularRow.setDetailsId("0");
            }
            if (!CounterToSaleInvId.isEmpty()) {
                purParticularRow.setCounterNo("");
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
            if (!cmpTRowDTO.getUnit().isEmpty()) {
                purParticularRow.setUnitId(cmpTRowDTO.getUnit_id());
            } else {
                purParticularRow.setUnitId("");
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
            if (!cmpTRowDTO.getFree().isEmpty()) {
                purParticularRow.setFreeQty(cmpTRowDTO.getFree());
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
            if (!cmpTRowDTO.getDis_per().isEmpty()) {
                purParticularRow.setDisPer2("0.0");
            } else {
                purParticularRow.setDisPer2("0.0");
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
            if (cmpTRowDTO.getGross_amount() != null) {
                purParticularRow.setGrossAmt(String.valueOf(cmpTRowDTO.getGross_amount()));
            } else {
                purParticularRow.setGrossAmt("0.0");
            }
            if (cmpTRowDTO.getAdd_chg_amt() != null) {
                purParticularRow.setAddChgAmt(String.valueOf(cmpTRowDTO.getAdd_chg_amt()));
            } else {
                purParticularRow.setAddChgAmt("0.0");
            }
            if (cmpTRowDTO.getGross_amount1() != null) {
                purParticularRow.setGrossAmt(String.valueOf(cmpTRowDTO.getGross_amount1()));
            } else {
                purParticularRow.setGrossAmt("0.0");
            }
            if (cmpTRowDTO.getInvoice_dis_amt() != null) {
                purParticularRow.setInvoiceDisAmt(String.valueOf(cmpTRowDTO.getInvoice_dis_amt()));
            } else {
                purParticularRow.setInvoiceDisAmt("0.0");
            }
            if (cmpTRowDTO.getTotal_amt() != null) {
                purParticularRow.setTotalAmt(String.valueOf(cmpTRowDTO.getTotal_amt()));
            } else {
                purParticularRow.setTotalAmt("0.0");
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
            if (!cmpTRowDTO.getBatch_or_serial().isEmpty()) {
                purParticularRow.setbNo(cmpTRowDTO.getBatch_or_serial());
            } else {
                purParticularRow.setbNo("");
            }
//hjkl
            if (!cmpTRowDTO.getB_rate().isEmpty()) {
                purParticularRow.setbRate(cmpTRowDTO.getB_rate());
            } else {
                purParticularRow.setbRate("");
            }
            if (!cmpTRowDTO.getRate_a().isEmpty()) {
                purParticularRow.setRateA(cmpTRowDTO.getRate_a());
            } else {
                purParticularRow.setRateA("0");
            }

            if (!cmpTRowDTO.getRate_b().isEmpty()) {
                purParticularRow.setRateB(cmpTRowDTO.getRate_b());
            } else {
                purParticularRow.setRateB("0");
            }
            if (!cmpTRowDTO.getRate_c().isEmpty()) {
                purParticularRow.setRateC(cmpTRowDTO.getRate_c());
            } else {
                purParticularRow.setRateC("0");
            }
            if (!cmpTRowDTO.getCosting().isEmpty()) {
                purParticularRow.setCosting(cmpTRowDTO.getCosting());
            } else {
                purParticularRow.setCosting("0");
            }
            if (!cmpTRowDTO.getCosting_with_tax().isEmpty()) {
                purParticularRow.setCostingWithTax(cmpTRowDTO.getCosting_with_tax());
            } else {
                purParticularRow.setCostingWithTax("0");
            }
            if (!cmpTRowDTO.getMin_margin().isEmpty()) {
                purParticularRow.setMinMargin(cmpTRowDTO.getMin_margin());
            } else {
                purParticularRow.setMinMargin("0");
            }
            if (cmpTRowDTO.getManufacturing_date() != null && !cmpTRowDTO.getManufacturing_date().isEmpty()) {
                purParticularRow.setManufacturingDate(cmpTRowDTO.getManufacturing_date());
            } else {
                purParticularRow.setManufacturingDate("");
            }
            if (!cmpTRowDTO.getB_purchase_rate().isEmpty()) {
                purParticularRow.setbPurchaseRate(cmpTRowDTO.getB_purchase_rate());
            } else {
                purParticularRow.setbPurchaseRate("0");
            }
            if (cmpTRowDTO.getB_expiry() != null && !cmpTRowDTO.getB_expiry().isEmpty()) {
                purParticularRow.setbExpiry(cmpTRowDTO.getB_expiry());
            } else {
                purParticularRow.setbExpiry("");
            }
            if (!cmpTRowDTO.getB_details_id().isEmpty()) {
                purParticularRow.setbDetailsId(cmpTRowDTO.getB_details_id());
            } else {
                purParticularRow.setbDetailsId("0");
            }
            if (!cmpTRowDTO.getIs_batch().isEmpty()) {
                purParticularRow.setBatch(cmpTRowDTO.getIs_batch());
            } else {
                purParticularRow.setBatch("0");
            }
            if (!cmpTRowDTO.getIs_serial().isEmpty()) {
                purParticularRow.setSerialNo(null);
            } else {
                purParticularRow.setSerialNo(null);
            }
            if (cmpTRowDTO.getReference_id().isEmpty()) {
                purParticularRow.setReferenceId(cmpTRowDTO.getReference_id());
            } else {
                purParticularRow.setReferenceId("");
            }
            if (cmpTRowDTO.getReference_type().isEmpty()) {
                purParticularRow.setReferenceType(cmpTRowDTO.getReference_type());
            } else {
                purParticularRow.setReferenceType("");
            }
            rowData.add(purParticularRow);

            qty = Integer.parseInt(cmpTRowDTO.getQuantity());
            totalQty += qty;
            freeQty = Double.parseDouble(cmpTRowDTO.getFree());
            totalFreeQty += freeQty;
        }

        map.put("totalqty", totalQty.toString());
        map.put("total_free_qty", totalFreeQty.toString());


        JsonArray payment_type = new JsonArray();
        if (multiPaymentDTO.getMultiAmt() != null) {
            for (CSMultiPaymentDTO data : multiPaymentDTO.getMultiAmt()) {
                if (payment_type.size() > 0) {
                    Boolean isExist = false;
                    int idx = 0;
                    for (JsonElement element : payment_type) {
                        JsonObject bankObj = new Gson().fromJson(element, JsonObject.class);

                        if (bankObj.get("bankId").getAsString().equalsIgnoreCase(data.getBankId())) {
                            JsonArray payment_modes = bankObj.get("payment_modes").getAsJsonArray();
                            JsonObject modesObj = new JsonObject();
                            modesObj.addProperty("label", data.getMode());
                            modesObj.addProperty("modeId", data.getModeId());
                            modesObj.addProperty("amount", data.getAmount());
                            modesObj.addProperty("refId", data.getRefID());
                            payment_modes.add(modesObj);
                            bankObj.add("payment_modes", payment_modes);
                            payment_type.set(idx, bankObj);
                            isExist = true;
                        }
                        idx++;
                    }
                    if (isExist == false) {
                        JsonObject paymentBanks = new JsonObject();
                        paymentBanks.addProperty("bank_name", data.getBank());
                        paymentBanks.addProperty("bankId", data.getBankId());
                        JsonArray payment_modes = new JsonArray();
                        JsonObject modesObj = new JsonObject();
                        modesObj.addProperty("label", data.getMode());
                        modesObj.addProperty("modeId", data.getModeId());
                        modesObj.addProperty("amount", data.getAmount());
                        modesObj.addProperty("refId", data.getRefID());
                        payment_modes.add(modesObj);
                        paymentBanks.add("payment_modes", payment_modes);
                        payment_type.add(paymentBanks);
                    }

                } else {
                    JsonObject paymentBanks = new JsonObject();
                    paymentBanks.addProperty("bank_name", data.getBank());
                    paymentBanks.addProperty("bankId", data.getBankId());
                    JsonArray payment_modes = new JsonArray();
                    JsonObject modesObj = new JsonObject();
                    modesObj.addProperty("label", data.getMode());
                    modesObj.addProperty("modeId", data.getModeId());
                    modesObj.addProperty("amount", data.getAmount());
                    modesObj.addProperty("refId", data.getRefID());
                    payment_modes.add(modesObj);
                    paymentBanks.add("payment_modes", payment_modes);
                    payment_type.add(paymentBanks);
                }

            }
        }

        map.put("payment_type", payment_type.toString());

        String mRowData = new Gson().toJson(rowData, new TypeToken<List<TranxPurRowDTO>>() {
        }.getType());
        map.put("row", mRowData);
        String finalReq = Globals.mapToString(map);


        HttpResponse<String> response;
        String formData = "";
        APIClient apiClient = null;
        if (isConsumer == true) {
            map.put("id", consumerId);
            if(!consumerId.equalsIgnoreCase("")){
                map.put("rowDelDetailsIds", deletedCSRows.toString());
            }
            map.put("debtors_id", patientId);
            map.put("client_name", tfConsumerCName.getText());
            map.put("patientName", tfConsumerCName.getText());
            map.put("client_address", tfConsumerCAddress.getText());
            map.put("doctorsId", doctorId);
            map.put("drAddress", tfConsumerDoctorAddress.getText());
            map.put("sales_discount", !tfCounterSaleDiscPer.getText().isEmpty() ? tfCounterSaleDiscPer.getText() : "0.0");
            map.put("prescriptionList", "[]");
            map.put("taxFlag", "false");
            if (!CounterToSaleInvId.isEmpty()) {
                map.put("cs_ids", CounterToSaleInvId);
            }
            Map<String, String> headers = new HashMap<>();
            headers.put("branch", Globals.headerBranch);
            Map<String, File> fileMap = null;
            if (selectedFileCS != null) {
                fileMap = new HashMap<>();
                fileMap.put("uploadImage", selectedFileCS);
            }


            if (consumerId.equalsIgnoreCase("")) {
                apiClient = new APIClient(EndPoints.CONSUMER_SALE_CREATE_ENDPOINT, map, headers, fileMap, RequestType.MULTI_PART);
            } else {
                apiClient = new APIClient(EndPoints.CONSUMER_SALE_UPDATE_ENDPOINT, map, headers, fileMap, RequestType.MULTI_PART);
            }
        } else {
            if(!counterId.equalsIgnoreCase("")){
                map.put("rowDelDetailsIds", deletedCSRows.toString());
            }
            formData = Globals.mapToStringforFormData(map);
            if (counterId.equalsIgnoreCase("")) {
                apiClient = new APIClient(EndPoints.COUNTER_SALE_CREATE_ENDPOINT, formData, RequestType.FORM_DATA);
            } else {
                apiClient = new APIClient(EndPoints.COUNTER_SALE_UPDATE_ENDPOINT, formData, RequestType.FORM_DATA);
            }
        }
        apiClient.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent workerStateEvent) {
                jsonObject = new Gson().fromJson(workerStateEvent.getSource().getValue().toString(), JsonObject.class);
                message = jsonObject.get("message").getAsString();
                if (jsonObject.get("responseStatus").getAsInt() == 200) {
                    AlertUtility.AlertSuccessTimeout(AlertUtility.alertTypeSuccess, message, e -> {
                        if (isConsumer == true) {
                            tpCounterSales.getSelectionModel().select(tabCSConsumerSale);
                            if (btnCounterSaleSubmit.getText().equalsIgnoreCase("update")) {
                                btnCounterSaleSubmit.setText("Submit");
                                focusInd = "update";
                            } else {
                                focusInd = "submit";
                            }
                            getConsumerSale();
//                            getCounterSale();
                        } else {
                            tpCounterSales.getSelectionModel().select(tabCSCounterSale);
                            if (btnCounterSaleSubmit.getText().equalsIgnoreCase("update")) {
                                btnCounterSaleSubmit.setText("Submit");
                                focusInd = "update";
                            } else {
                                focusInd = "submit";
                            }
                            getCounterSale();
//                            getConsumerSale();
                        }

                        // Clear Screen After Create
                        callClearTable();
                    });
                } else {
                    AlertUtility.AlertErrorTimeout(AlertUtility.alertTypeError, message, e -> {
                    });
                }
            }
        });

        apiClient.setOnCancelled(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent workerStateEvent) {
                counterSaleLogger.error("Network API cancelled in createCounterSale()" + workerStateEvent.getSource().getValue().toString());

            }
        });
        apiClient.setOnFailed(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent workerStateEvent) {
                counterSaleLogger.error("Network API cancelled in createCounterSale()" + workerStateEvent.getSource().getValue().toString());

            }
        });
        apiClient.start();
        counterSaleLogger.debug("Create Patient Data End...");

//        JsonObject responseBody = new Gson().fromJson(response.body(), JsonObject.class);


    }

    private void callClearTable() {
        tfCounterCreateMobile.setText("");
        switchConsumer.switchOnProperty().set(false);
        tfConsumerCName.setText("");
        tfConsumerCAddress.setText("");
        tfConsumerDoctorName.setText("");
        tfConsumerDoctorAddress.setText("");
        lblCounterSaleGrossTotal.setText("0.0");
        lblCounterSaleDis.setText("0.0");
        lblCounterSaleBillAmt.setText("0.0");
        lblCounterSaleTotal.setText("0.0");
        lblCounterSaleTax.setText("0.0");
        btnCounterSaleSubmit.setText("Submit");
        csInvDate = ""; // clearing Variable of date used in Edit Data Set
        cmbCSPaymentMode.getSelectionModel().select("Cash");
        tblvCounterSaleView.getItems().clear();
        tblvCounterSaleView.getItems().add(new CounterSaleRowDTO("", "0", "1", "", "", "", "", "", "", "", "", "", "", "", "", "", "", ""));
        tblvCounterSaleView.refresh();
        tblcConsumerSaleList.refresh();
        tblvCounterSaleList.refresh();
        PurInvoiceCommunicator.resetFields();
        tfCounterCreateMobile.requestFocus();
    }

    //Counter Sale list
    public void getCounterSale() {
        APIClient apiClient = null;
        try {
            rbCounterSalePMAll.setSelected(true);
            Map<String, String> map = new HashMap<>();
            map.put("payment_mode", radioPaymentMode);
            String formData = Globals.mapToStringforFormData(map);
            apiClient = new APIClient(EndPoints.COUNTER_SALE_GET_ENDPOINT, formData, RequestType.FORM_DATA);
            ObservableList<CounterSaleDTO> observableList = FXCollections.observableArrayList();
            apiClient.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
                @Override
                public void handle(WorkerStateEvent workerStateEvent) {
                    jsonObject = new Gson().fromJson(workerStateEvent.getSource().getValue().toString(), JsonObject.class);
                    if (jsonObject.get("responseStatus").getAsInt() == 200) {
                        JsonArray responseObject = jsonObject.getAsJsonArray("row");
                        if (responseObject.size() > 0) {
                            for (JsonElement element : responseObject) {
                                JsonObject item = element.getAsJsonObject();
                                String id = item.get("id").getAsString();
                                String details_id = item.get("details_id").getAsString();
                                String counterId = item.get("counterId").getAsString();
                                String countersrNo = item.get("countersrNo").getAsString();
                                String counterNo = item.get("counterNo").getAsString();
                                String invoiceDate = DateConvertUtil.convertDispDateFormat(item.get("invoiceDate").getAsString());
                                String payment_mode = item.get("payment_mode").getAsString();
                                String mobile_num = item.get("mobile_number") != null ? item.get("mobile_number").getAsString() : "";
                                String mobile_number = "";
                                if (mobile_num.equalsIgnoreCase("0")) {
                                    mobile_number = "";
                                } else {
                                    mobile_number = mobile_num;
                                }
                                String productName = item.get("productName").getAsString();
                                String unitName = item.get("unitName").getAsString();
                                String pack_name = item.get("pack_name").getAsString();
                                String qty = item.get("qty").getAsString();
                                String rate = item.get("rate").getAsString();
                                String total_amt = item.get("total_amt").getAsString();
                                String dis_amt = item.get("dis_amt").getAsString();
                                String batch_no = item.get("batch_no").getAsString();
                                String total_net_amt = item.get("totalNetAmt") != null ? item.get("totalNetAmt").getAsString() : "0.0";

                                observableList.add(new CounterSaleDTO(id, counterNo, invoiceDate, mobile_number, qty, total_amt, payment_mode, productName,
                                                pack_name, unitName, batch_no, rate, dis_amt, "", "", total_amt
                                        )
                                );
                            }
                            tblcCounterSaleListInvNo.setCellValueFactory(new PropertyValueFactory<>("invoiceNo"));
                            tblcCounterSaleListInvDate.setCellValueFactory(new PropertyValueFactory<>("invoiceDate"));
                            tblcCounterSaleListMobile.setCellValueFactory(new PropertyValueFactory<>("mobile"));
                            tblcCounterSaleListQty.setCellValueFactory(new PropertyValueFactory<>("qty"));
                            tblcCounterSaleListBillAmtInv.setCellValueFactory(new PropertyValueFactory<>("billAmtInv"));
                            tblcCounterSaleListBillAmt.setCellValueFactory(new PropertyValueFactory<>("billAmt"));
                            tblcCounterSaleListPayMode.setCellValueFactory(new PropertyValueFactory<>("paymentMode"));
                            tblcCounterSaleListProduct.setCellValueFactory(new PropertyValueFactory<>("productName"));
                            tblcCounterSaleListPackage.setCellValueFactory(new PropertyValueFactory<>("packaging"));
                            tblcCounterSaleListUnit.setCellValueFactory(new PropertyValueFactory<>("unit"));
                            tblcCounterSaleListBatch.setCellValueFactory(new PropertyValueFactory<>("batch"));
                            tblcCounterSaleListRate.setCellValueFactory(new PropertyValueFactory<>("rate"));
                            tblcCounterSaleListDis.setCellValueFactory(new PropertyValueFactory<>("discount"));

                            tblvCounterSaleList.setItems(observableList);
                            originalData = tblvCounterSaleList.getItems();
//                            if (rbCounterSaleInvoice.isSelected()) {
                            rbCounterSaleInvoice.setSelected(true);
                            populateUniqueEntries(tblvCounterSaleList, originalData, true);
//                            }

                            if (focusInd.equalsIgnoreCase("Submit")) {
                                tblvCounterSaleList.getSelectionModel().select(responseObject.size() - 1);
                                tblvCounterSaleList.scrollTo(responseObject.size() - 1);
                                tblvCounterSaleList.requestFocus();
                                focusInd = "";
                            } else if (focusInd.equalsIgnoreCase("Update")) {
                                tblvCounterSaleList.getSelectionModel().select(rowIndexCounter);
                                tblvCounterSaleList.scrollTo(rowIndexCounter);
                                tblvCounterSaleList.requestFocus();
                                focusInd = "";
                            }

                        } else {
                            counterSaleLogger.error("Response Object Is Null");
                        }
                    } else {
                        counterSaleLogger.error("Error in response");
                    }
                }
            });

            apiClient.setOnCancelled(new EventHandler<WorkerStateEvent>() {
                @Override
                public void handle(WorkerStateEvent workerStateEvent) {
                    counterSaleLogger.error("Network API cancelled in deleteAPICall()" + workerStateEvent.getSource().getValue().toString());

                }
            });

            apiClient.setOnFailed(new EventHandler<WorkerStateEvent>() {
                @Override
                public void handle(WorkerStateEvent workerStateEvent) {
                    counterSaleLogger.error("Network API cancelled in deleteAPICall()" + workerStateEvent.getSource().getValue().toString());

                }
            });
            apiClient.start();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            apiClient = null;
        }
    }


    //Consumer Sale list
    public void getConsumerSale() {
        APIClient apiClient = null;
        try {
            apiClient = new APIClient(EndPoints.CONSUMER_SALE_GET_ENDPOINT, "", RequestType.GET);
            ObservableList<CounterSaleDTO> observableList = FXCollections.observableArrayList();
            apiClient.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
                @Override
                public void handle(WorkerStateEvent workerStateEvent) {
                    jsonObject = new Gson().fromJson(workerStateEvent.getSource().getValue().toString(), JsonObject.class);
                    if (jsonObject.get("responseStatus").getAsInt() == 200) {
                        JsonArray responseObject = jsonObject.getAsJsonArray("response");
                        if (responseObject.size() > 0) {
                            for (JsonElement element : responseObject) {
                                JsonObject item = element.getAsJsonObject();
                                String id = item.get("id").getAsString();
                                String saleSrNo = item.get("saleSrNo").getAsString();
                                String saleNo = item.get("saleNo").getAsString();
                                String saleDate = DateConvertUtil.convertDispDateFormat(item.get("saleDate").getAsString());
                                String clientName = item.get("clientName").getAsString();
                                String clientAddress = item.get("clientAddress").getAsString();
                                String mobileNo = item.get("mobileNo").getAsString();
                                String totalQty = item.get("totalQty").getAsString();
                                String paymentMode = item.get("paymentMode").getAsString();
                                String totalBaseAmount = item.get("totalBaseAmount").getAsString();
                                String totalDiscount = item.get("totalDiscount").getAsString();
                                String totalsDiscountPer = item.get("totalsDiscountPer").getAsString();
                                String totalBill = item.get("totalBill").getAsString();
                                observableList.add(new CounterSaleDTO(id, saleNo, saleDate, mobileNo, totalQty, totalBill, paymentMode, "",
                                                "", "", "", totalBaseAmount, "", clientName, clientAddress, ""
                                        )
                                );
                            }

//                    tblcConsumerSaleListSelect.setCellValueFactory(new PropertyValueFactory<CounterSaleDTO, CheckBox>("select"));
                            // action.setCellFactory(column -> new CheckBoxTableCell<>());
//                    tblcConsumerSaleListSelect.setEditable(true);
                            tblcConsumerSaleListInvNo.setCellValueFactory(new PropertyValueFactory<>("invoiceNo"));
                            tblcConsumerSaleListInvDate.setCellValueFactory(new PropertyValueFactory<>("invoiceDate"));
                            tblcConsumerSaleListClientName.setCellValueFactory(new PropertyValueFactory<>("clientName"));
                            tblcConsumerSaleListClientAddress.setCellValueFactory(new PropertyValueFactory<>("clientAddress"));
                            tblcConsumerSaleListMobile.setCellValueFactory(new PropertyValueFactory<>("mobile"));
                            tblcConsumerSaleListQty.setCellValueFactory(new PropertyValueFactory<>("qty"));
                            tblcConsumerSaleListBillAmt.setCellValueFactory(new PropertyValueFactory<>("billAmt"));
                            tblcConsumerSaleListPayMod.setCellValueFactory(new PropertyValueFactory<>("paymentMode"));

                            tblcConsumerSaleList.setItems(observableList);

                            if (focusInd.equalsIgnoreCase("Submit")) {
                                tblcConsumerSaleList.getSelectionModel().select(responseObject.size() - 1);
                                tblcConsumerSaleList.scrollTo(responseObject.size() - 1);
                                tblcConsumerSaleList.requestFocus();
                                focusInd = "";
                            } else if (focusInd.equalsIgnoreCase("Update")) {
                                tblcConsumerSaleList.getSelectionModel().select(rowIndexConsumer);
                                tblcConsumerSaleList.scrollTo(rowIndexConsumer);
                                tblcConsumerSaleList.requestFocus();
                                focusInd = "";
                            }

                        } else {
                            counterSaleLogger.error("Response Object Is Null");
                        }
                    } else {
                        counterSaleLogger.error("Error in response");
                    }
                }
            });
            apiClient.setOnCancelled(new EventHandler<WorkerStateEvent>() {
                @Override
                public void handle(WorkerStateEvent workerStateEvent) {
                    counterSaleLogger.error("Network API cancelled in deleteAPICall()" + workerStateEvent.getSource().getValue().toString());

                }
            });

            apiClient.setOnFailed(new EventHandler<WorkerStateEvent>() {
                @Override
                public void handle(WorkerStateEvent workerStateEvent) {
                    counterSaleLogger.error("Network API cancelled in deleteAPICall()" + workerStateEvent.getSource().getValue().toString());

                }
            });
            apiClient.start();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            apiClient = null;
        }
    }

    public void getCounterSaleByIdData() {
        try {
            Map<String, String> formData = new HashMap<>();
            formData.put("id", counterId);
            String finalReq = Globals.mapToStringforFormData(formData);
            HttpResponse<String> response = APIClient.postFormDataRequest(finalReq, EndPoints.COUNTER_SALE_GET_By_Id_ENDPOINT);
            CounterResDTO responseBody = new Gson().fromJson(response.body(), CounterResDTO.class);
            if (responseBody.getResponseStatus() == 200) {
                setCounterSaleData(responseBody);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void setCounterSaleData(CounterResDTO resDTO) {
        btnCounterSaleSubmit.setText("Update");
        tfCounterCreateMobile.setText(resDTO.getResponse().getMobileNumber());

        multiIn = resDTO.getResponse().getId();
        cmbCSPaymentMode.setValue(resDTO.getResponse().getPaymentMode());
        if (resDTO.getResponse().getPaymentMode().equalsIgnoreCase("Multi")) {
            multiPaymentDTO.setCashAmt(resDTO.getResponse().getCashAmt());
            List<CSPaymentTypeResDTO> lst = resDTO.getResponse().getPaymentType();
            ObservableList<CSMultiPaymentDTO> inlst = FXCollections.observableArrayList();
            if (lst.size() > 0) {
                for (CSPaymentTypeResDTO paymentTypeResDTO : lst) {
                    if (paymentTypeResDTO.getPaymentModes().size() > 0) {
                        for (CSPaymentModeDTO paymentMode : paymentTypeResDTO.getPaymentModes()) {
                            CSMultiPaymentDTO newEntry = new CSMultiPaymentDTO(paymentMode.getDetailsId().toString(), paymentTypeResDTO.getBankName(), paymentTypeResDTO.getBankId().toString(), paymentMode.getLabel(), paymentMode.getModeId().toString(), paymentMode.getAmount().toString(), paymentMode.getRefId());

                            inlst.add(newEntry);
                        }
                    }
                }
            }
            multiPaymentDTO.setMultiAmt(inlst);
        }
        tblvCounterSaleView.getItems().clear();
        int count = 1;
        int index = 0;
        PurInvoiceCommunicator.levelAForPurInvoiceObservableList.clear();
        for (Row mRow : resDTO.getResponse().getRow()) {
            CounterSaleRowDTO cmpTRowDTO = new CounterSaleRowDTO();
            cmpTRowDTO.setParticulars(mRow.getProductName());
            cmpTRowDTO.setQuantity(String.valueOf(mRow.getQty().intValue()));
            cmpTRowDTO.setRate(String.valueOf(mRow.getRate()));

            cmpTRowDTO.setBatch_or_serial(mRow.getBatchNo());
            cmpTRowDTO.setGross_amount(String.valueOf(mRow.getFinalAmt()));
            cmpTRowDTO.setDis_per(String.valueOf(mRow.getDisPer()));
            cmpTRowDTO.setPackages(mRow.getPackName());
            cmpTRowDTO.setTax(mRow.getIgst().toString());
            cmpTRowDTO.setNet_amount(String.valueOf(mRow.getFinalAmt()));
            cmpTRowDTO.setDis_per(String.valueOf(mRow.getDisPer()));
            cmpTRowDTO.setProduct_id(String.valueOf(mRow.getProductId()));
            cmpTRowDTO.setLevelA_id(String.valueOf(mRow.getLevelAId()));
            cmpTRowDTO.setLevelB_id(String.valueOf(mRow.getLevelBId()));
            cmpTRowDTO.setLevelC_id(String.valueOf(mRow.getLevelCId()));
            cmpTRowDTO.setUnit_id(mRow.getUnitId().toString());
            cmpTRowDTO.setUnit_conv(String.valueOf(mRow.getUnitConv()));
            cmpTRowDTO.setFree(String.valueOf(mRow.getFreeQty()));
            // cmpTRowDTO.setBase_amt(String.valueOf(mRow.getBaseAmt()));
            cmpTRowDTO.setDis_amt(String.valueOf(mRow.getDisAmt()));
            cmpTRowDTO.setDis_per(String.valueOf(mRow.getDisPer()));
            cmpTRowDTO.setB_no(mRow.getBatchNo());
            cmpTRowDTO.setB_rate(String.valueOf(mRow.getbRate()));
            cmpTRowDTO.setRate_a(String.valueOf(mRow.getMinRateA()));
            cmpTRowDTO.setRate_b(String.valueOf(mRow.getMinRateB()));
            cmpTRowDTO.setRate_c(String.valueOf(mRow.getMinRateC()));
            cmpTRowDTO.setCosting(String.valueOf(mRow.getCosting()));
            cmpTRowDTO.setCosting_with_tax(String.valueOf(mRow.getCostingWithTax()));
            cmpTRowDTO.setMin_margin(String.valueOf(mRow.getMinMargin()));
            cmpTRowDTO.setB_expiry(mRow.getbExpiry());
            cmpTRowDTO.setIs_batch(String.valueOf(mRow.getIsBatch()));
            cmpTRowDTO.setManufacturing_date(String.valueOf(mRow.getManufacturingDate()));
            cmpTRowDTO.setB_purchase_rate(String.valueOf(mRow.getPurchaseRate()));
            cmpTRowDTO.setB_details_id(String.valueOf(mRow.getbDetailsId()));
            //  cmpTRowDTO.set("");
            cmpTRowDTO.setReference_id("");
            cmpTRowDTO.setReference_type("");
            cmpTRowDTO.setDetails_id(String.valueOf(mRow.getDetailsId()));
            cmpTRowDTO.setBase_amt(String.valueOf(0.0));
            cmpTRowDTO.setRow_dis_amt(String.valueOf(0.0));
            cmpTRowDTO.setDis_per2(String.valueOf(mRow.getDisPer2()));
            cmpTRowDTO.setTotal_amt(String.valueOf(0.0));
            cmpTRowDTO.setIgst(String.valueOf(mRow.getIgst()));
            cmpTRowDTO.setCgst(String.valueOf(mRow.getCgst()));
            cmpTRowDTO.setSgst(String.valueOf(mRow.getSgst()));
            cmpTRowDTO.setTotal_igst(String.valueOf(mRow.getTotalIgst()));
            cmpTRowDTO.setTotal_sgst(String.valueOf(mRow.getTotalSgst()));
            cmpTRowDTO.setTotal_cgst(String.valueOf(mRow.getTotalCgst()));
            cmpTRowDTO.setFinal_amount(String.valueOf(mRow.getFinalAmt()));
            cmpTRowDTO.setIs_batch(String.valueOf(mRow.getIsBatch()));
            if (mRow.getIsBatch() == true) {
//                TranxSelectedBatch selectedBatch = GlobalTranx.
                TranxSelectedBatch selectedBatch = TranxCommonPopUps.getSelectedBatchFromProductId(Integer.valueOf(mRow.getProductId()),
                        csInvDate, mRow.getBatchNo());
                cmpTRowDTO.setSelectedBatch(selectedBatch);
                cmpTRowDTO.setRate(cmpTRowDTO.getRate());
            }

            TranxSelectedProduct selectedProduct = TranxCommonPopUps.getSelectedProductFromProductId(mRow.getProductName());
            cmpTRowDTO.setSelectedProduct(selectedProduct);

            lblCounterSaleGrossTotal.setText(String.valueOf(mRow.getFinalAmt()));
            lblCounterSaleBillAmt.setText(String.valueOf(mRow.getFinalAmt()));


            List<LevelAForPurInvoice> levelAForPurInvoiceList = com.opethic.genivis.controller.tranx_sales.ProductUnitsPacking.getAllProductUnitsPackingFlavour(cmpTRowDTO.getProduct_id());
            ObservableList<LevelAForPurInvoice> observableLevelAList = FXCollections.observableArrayList(levelAForPurInvoiceList);
            if (index >= 0 && index < PurInvoiceCommunicator.levelAForPurInvoiceObservableList.size()) {
                PurInvoiceCommunicator.levelAForPurInvoiceObservableList.set(index, observableLevelAList);
                tblvCounterSaleView.getItems().get(index).setLevelA(null);
                tblvCounterSaleView.getItems().get(index).setLevelA(levelAForPurInvoiceList.get(0).getLabel());
                for (LevelAForPurInvoice levelAForPurInvoice : PurInvoiceCommunicator.levelAForPurInvoiceObservableList.get(index)) {
                    if (tblvCounterSaleView.getItems().get(index).getLevelA_id().equals(levelAForPurInvoice.getValue())) {
                        tblvCounterSaleView.getItems().get(index).setLevelA(null);
                        tblvCounterSaleView.getItems().get(index).setLevelA(levelAForPurInvoice.getLabel());
                    }
                }
            } else {
                PurInvoiceCommunicator.levelAForPurInvoiceObservableList.add(observableLevelAList);
            }


            tblvCounterSaleView.getItems().add(cmpTRowDTO);

            counterSaleCalculation.rowCalculationForPurcInvoice(index, tblvCounterSaleView, callback);
            counterSaleCalculation.calculateGst(tblvCounterSaleView, callback);
//            rowCalculation(selectedRowIndex);

            count++;
            index++;

        }

        if (tfCounterCreateMobile.getText().isEmpty()) {
            tfCounterCreateMobile.requestFocus();
        } else {
            Platform.runLater(() -> {
                TableColumn<CounterSaleRowDTO, ?> colName = tblvCounterSaleView.getColumns().get(1);
                tblvCounterSaleView.edit(0, colName);
            });

        }

    }

    public void getConsumerSaleByIdData() {
        try {
            Map<String, String> formData = new HashMap<>();
            formData.put("id", consumerId);
            String finalReq = Globals.mapToStringforFormData(formData);

            HttpResponse<String> response = APIClient.postFormDataRequest(finalReq, CONSUMER_SALE_GET_By_Id_ENDPOINT);
            ConsumerResDTO responseBody = new Gson().fromJson(response.body(), ConsumerResDTO.class);
            if (responseBody.getResponseStatus() == 200) {
                setConsumerSaleData(responseBody);
            }
        } catch (Exception e) {
            e.printStackTrace();

        }

    }

    public void setConsumerSaleData(ConsumerResDTO resDTO) {

        btnCounterSaleSubmit.setText("Update");
        multiIn = resDTO.getResponse().getId();
        cmbCSPaymentMode.setValue(resDTO.getResponse().getPaymentMode());
        if (resDTO.getResponse().getPaymentMode().equalsIgnoreCase("Multi")) {
            multiPaymentDTO.setCashAmt(resDTO.getResponse().getCashAmt());
            List<CSPaymentTypeResDTO> lst = resDTO.getResponse().getPaymentType();
            ObservableList<CSMultiPaymentDTO> inlst = FXCollections.observableArrayList();
            if (lst.size() > 0) {
                for (CSPaymentTypeResDTO paymentTypeResDTO : lst) {
                    if (paymentTypeResDTO.getPaymentModes().size() > 0) {
                        for (CSPaymentModeDTO paymentMode : paymentTypeResDTO.getPaymentModes()) {
                            CSMultiPaymentDTO newEntry = new CSMultiPaymentDTO(paymentMode.getDetailsId().toString(), paymentTypeResDTO.getBankName(), paymentTypeResDTO.getBankId().toString(), paymentMode.getLabel(), paymentMode.getModeId().toString(), paymentMode.getAmount().toString(), paymentMode.getRefId());
                            inlst.add(newEntry);
                        }
                    }
                }
            }
            multiPaymentDTO.setMultiAmt(inlst);
        }
        tfCounterCreateMobile.setText(resDTO.getResponse().getMobileNumber());
        switchConsumer.switchOnProperty().set(true);
        tfConsumerCName.setText(resDTO.getResponse().getClientName());
        tfCounterSaleDiscPer.setText(resDTO.getResponse().getSalesDiscount().toString());
        tfConsumerCAddress.setText(resDTO.getResponse().getClientAddress());
        tfConsumerDoctorName.setText(resDTO.getResponse().getDoctorName());
        tfConsumerDoctorAddress.setText(resDTO.getResponse().getDoctorAddress());
        patientId = "PAT#1";
        doctorId = resDTO.getResponse().getDoctorId();
        tblvCounterSaleView.getItems().clear();
        PurInvoiceCommunicator.levelAForPurInvoiceObservableList.clear();
        int count = 1;
        int index = 0;
        for (ConsumerRow mRow : resDTO.getResponse().getRow()) {
            CounterSaleRowDTO cmpTRowDTO = new CounterSaleRowDTO();
            cmpTRowDTO.setParticulars(mRow.getProductName());
            cmpTRowDTO.setQuantity(String.valueOf(mRow.getQty().intValue()));
            cmpTRowDTO.setRate(String.valueOf(mRow.getRate()));
            cmpTRowDTO.setBatch_or_serial(mRow.getBatchNo());
            cmpTRowDTO.setGross_amount(String.valueOf(mRow.getFinalAmt()));
            cmpTRowDTO.setDis_per(String.valueOf(mRow.getDisPer()));
            cmpTRowDTO.setTax(mRow.getIgst().toString());
            cmpTRowDTO.setNet_amount(String.valueOf(mRow.getFinalAmt()));
            cmpTRowDTO.setDis_per(String.valueOf(mRow.getDisPer()));
            cmpTRowDTO.setProduct_id(String.valueOf(mRow.getProductId()));
            cmpTRowDTO.setLevelA_id(String.valueOf(mRow.getLevelAId()));
            cmpTRowDTO.setLevelB_id(String.valueOf(mRow.getLevelBId()));
            cmpTRowDTO.setLevelC_id(String.valueOf(mRow.getLevelCId()));
            cmpTRowDTO.setUnit_id(mRow.getUnitId().toString());
            cmpTRowDTO.setUnit_conv(String.valueOf(mRow.getUnitConv()));
            cmpTRowDTO.setPackages(mRow.getPackName());
            cmpTRowDTO.setFree(String.valueOf(mRow.getFreeQty()));
            // cmpTRowDTO.setBase_amt(String.valueOf(mRow.getBaseAmt()));
            cmpTRowDTO.setDis_amt(String.valueOf(mRow.getDisAmt()));
            cmpTRowDTO.setDis_per(String.valueOf(mRow.getDisPer()));
            cmpTRowDTO.setB_no(mRow.getBatchNo());
            cmpTRowDTO.setB_rate(String.valueOf(mRow.getbRate()));
            cmpTRowDTO.setRate_a(String.valueOf(mRow.getMinRateA()));
            cmpTRowDTO.setRate_b(String.valueOf(mRow.getMinRateB()));
            cmpTRowDTO.setRate_c(String.valueOf(mRow.getMinRateC()));
            cmpTRowDTO.setCosting(String.valueOf(mRow.getCosting()));
            cmpTRowDTO.setCosting_with_tax(String.valueOf(mRow.getCostingWithTax()));
            cmpTRowDTO.setMin_margin(String.valueOf(mRow.getMinMargin()));
            cmpTRowDTO.setB_expiry(mRow.getbExpiry());
            cmpTRowDTO.setIs_batch(String.valueOf(mRow.getIsBatch()));
            cmpTRowDTO.setManufacturing_date(String.valueOf(mRow.getManufacturingDate()));
            cmpTRowDTO.setB_purchase_rate(String.valueOf(mRow.getPurchaseRate()));
            cmpTRowDTO.setB_details_id(String.valueOf(mRow.getbDetailsId()));
            //  cmpTRowDTO.setSerialNo("");
            cmpTRowDTO.setReference_id("");
            cmpTRowDTO.setReference_type("");
            cmpTRowDTO.setDetails_id(String.valueOf(mRow.getDetailsId()));
            cmpTRowDTO.setBase_amt(String.valueOf(0.0));
            cmpTRowDTO.setRow_dis_amt(String.valueOf(0.0));
            cmpTRowDTO.setDis_per2(String.valueOf(mRow.getDisPer2()));
            cmpTRowDTO.setTotal_amt(String.valueOf(0.0));
            cmpTRowDTO.setIgst(String.valueOf(mRow.getIgst()));
            cmpTRowDTO.setCgst(String.valueOf(mRow.getCgst()));
            cmpTRowDTO.setSgst(String.valueOf(mRow.getSgst()));
            cmpTRowDTO.setTotal_igst(String.valueOf(mRow.getTotalIgst()));
            cmpTRowDTO.setTotal_sgst(String.valueOf(mRow.getTotalSgst()));
            cmpTRowDTO.setTotal_cgst(String.valueOf(mRow.getTotalCgst()));
            cmpTRowDTO.setFinal_amount(String.valueOf(mRow.getFinalAmt()));
            cmpTRowDTO.setIs_batch(String.valueOf(mRow.getIsBatch()));
            cmpTRowDTO.setAdd_chg_amt(String.valueOf(mRow.getAddChgAmt()));
            cmpTRowDTO.setGross_amount1(String.valueOf(0.0));
            cmpTRowDTO.setDis_per_cal(String.valueOf(0.0));

            lblCounterSaleGrossTotal.setText(String.valueOf(mRow.getFinalAmt()));
            lblCounterSaleBillAmt.setText(String.valueOf(mRow.getFinalAmt()));

            if (mRow.getIsBatch() == true) {
//                TranxSelectedBatch selectedBatch = GlobalTranx.
                TranxSelectedBatch selectedBatch = TranxCommonPopUps.getSelectedBatchFromProductId(Integer.valueOf(mRow.getProductId()),
                        csInvDate, mRow.getBatchNo());
                cmpTRowDTO.setSelectedBatch(selectedBatch);
                cmpTRowDTO.setRate(cmpTRowDTO.getRate());
            }

            TranxSelectedProduct selectedProduct = TranxCommonPopUps.getSelectedProductFromProductId(mRow.getProductName());
            cmpTRowDTO.setSelectedProduct(selectedProduct);

            List<LevelAForPurInvoice> levelAForPurInvoiceList = com.opethic.genivis.controller.tranx_sales.ProductUnitsPacking.getAllProductUnitsPackingFlavour(cmpTRowDTO.getProduct_id());
            ObservableList<LevelAForPurInvoice> observableLevelAList = FXCollections.observableArrayList(levelAForPurInvoiceList);
            if (index >= 0 && index < PurInvoiceCommunicator.levelAForPurInvoiceObservableList.size()) {
                PurInvoiceCommunicator.levelAForPurInvoiceObservableList.set(index, observableLevelAList);

                tblvCounterSaleView.getItems().get(index).setLevelA(null);
                tblvCounterSaleView.getItems().get(index).setLevelA(levelAForPurInvoiceList.get(0).getLabel());

                for (LevelAForPurInvoice levelAForPurInvoice : PurInvoiceCommunicator.levelAForPurInvoiceObservableList.get(index)) {
                    if (tblvCounterSaleView.getItems().get(index).getLevelA_id().equals(levelAForPurInvoice.getValue())) {
                        tblvCounterSaleView.getItems().get(index).setLevelA(null);
                        tblvCounterSaleView.getItems().get(index).setLevelA(levelAForPurInvoice.getLabel());
                    }
                }
            } else {
                PurInvoiceCommunicator.levelAForPurInvoiceObservableList.add(observableLevelAList);
            }

            tblvCounterSaleView.getItems().add(cmpTRowDTO);

            counterSaleCalculation.rowCalculationForPurcInvoice(index, tblvCounterSaleView, callback);
            counterSaleCalculation.calculateGst(tblvCounterSaleView, callback);

            count++;
            index++;
        }

        if (!tfCounterSaleDiscPer.getText().isEmpty()) {
            String discText = tfCounterSaleDiscPer.getText();
            if (!TextUtils.isEmpty(discText)) {
                double totalTaxableAmt = 0.0;
                for (int i = 0; i < tblvCounterSaleView.getItems().size(); i++) {
                    counterSaleCalculation.rowCalculationForPurcInvoice(i, tblvCounterSaleView, callback);//call row calculation function
                    CounterSaleRowDTO purchaseInvoiceTable = tblvCounterSaleView.getItems().get(i);
                    totalTaxableAmt = totalTaxableAmt + Double.parseDouble(purchaseInvoiceTable.getTaxable_amt());

                }
                double disc_per = Double.parseDouble(discText);
                Double amount = (totalTaxableAmt * disc_per) / 100;
                counterSaleCalculation.discountPropotionalCalculation(disc_per + "", "0", "", tblvCounterSaleView, callback);
            } else {
                counterSaleCalculation.discountPropotionalCalculation("0", "0", "", tblvCounterSaleView, callback);
            }
            //? Calculate Tax for each Row
            counterSaleCalculation.calculateGst(tblvCounterSaleView, callback);
        }

        if (tfCounterCreateMobile.getText().isEmpty()) {
            tfCounterCreateMobile.requestFocus();
        } else {
            Platform.runLater(() -> {
                TableColumn<CounterSaleRowDTO, ?> colName = tblvCounterSaleView.getColumns().get(1);
                tblvCounterSaleView.edit(0, colName);
            });

        }

    }

//Counter Sale to sale convert code:set data to row

    public void getCounterSaleToSaleInvByIdData() {
        try {

            Map<String, String> body = new HashMap<>();
            body.put("csno", CounterToSaleInvId);
            String requestBody = Globals.mapToStringforFormData(body);
            HttpResponse<String> response = APIClient.postFormDataRequest(requestBody, EndPoints.COUNTER_SALE_TO_SALE_INVOICE_GET_By_Id_ENDPOINT);
            CSToSInvDTO responseBody = new Gson().fromJson(response.body(), CSToSInvDTO.class);
            if (responseBody.getResponseStatus() == 200) {
                setCounterSalToSaleConvertData(responseBody);
            } else {
                AlertUtility.AlertErrorTimeout(AlertUtility.alertTypeError, "Internal Server Error", input -> {
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    public void setCounterSalToSaleConvertData(CSToSInvDTO resDTO) {
        isConsumer = true;
        switchConsumer.switchOnProperty().set(true);
        btnCounterSaleSubmit.setText("Submit");
        tblvCounterSaleView.getItems().clear();
        PurInvoiceCommunicator.levelAForPurInvoiceObservableList.clear();
        int count = 1;
        int index = 0;
        for (CSToSInvRowDTO mRow : resDTO.getRow()) {
            CounterSaleRowDTO cmpTRowDTO = new CounterSaleRowDTO();
            cmpTRowDTO.setParticulars(mRow.getProductName());
            cmpTRowDTO.setQuantity(String.valueOf(mRow.getQty().intValue()));
            cmpTRowDTO.setRate(String.valueOf(mRow.getRate()));
            cmpTRowDTO.setBatch_or_serial(mRow.getBatchNo());
            cmpTRowDTO.setGross_amount(String.valueOf(mRow.getFinalAmt()));
            cmpTRowDTO.setDis_per(String.valueOf(mRow.getDisPer()));
            cmpTRowDTO.setTax(mRow.getIgst().toString());
            cmpTRowDTO.setNet_amount(String.valueOf(mRow.getFinalAmt()));
            cmpTRowDTO.setDis_per(String.valueOf(mRow.getDisPer()));
            cmpTRowDTO.setProduct_id(String.valueOf(mRow.getProductId()));
            cmpTRowDTO.setLevelA_id(String.valueOf(mRow.getLevelAId()));
            cmpTRowDTO.setLevelB_id(String.valueOf(mRow.getLevelBId()));
            cmpTRowDTO.setLevelC_id(String.valueOf(mRow.getLevelCId()));
            cmpTRowDTO.setUnit_id(mRow.getUnitId().toString());
            cmpTRowDTO.setUnit_conv(String.valueOf(mRow.getUnitConv()));
            cmpTRowDTO.setPackages(mRow.getPackName());
            cmpTRowDTO.setFree(String.valueOf(mRow.getFreeQty()));
            // cmpTRowDTO.setBase_amt(String.valueOf(mRow.getBaseAmt()));
            cmpTRowDTO.setDis_amt(String.valueOf(mRow.getDisAmt()));
            cmpTRowDTO.setDis_per(String.valueOf(mRow.getDisPer()));
            cmpTRowDTO.setB_no(mRow.getBatchNo());
            cmpTRowDTO.setB_rate(String.valueOf(mRow.getbRate()));
            cmpTRowDTO.setRate_a(String.valueOf(mRow.getMinRateA()));
            cmpTRowDTO.setRate_b(String.valueOf(mRow.getMinRateB()));
            cmpTRowDTO.setRate_c(String.valueOf(mRow.getMinRateC()));
            cmpTRowDTO.setCosting(String.valueOf(mRow.getCosting()));
            cmpTRowDTO.setCosting_with_tax(String.valueOf(mRow.getCostingWithTax()));
            cmpTRowDTO.setMin_margin(String.valueOf(mRow.getMinMargin()));
            cmpTRowDTO.setB_expiry(mRow.getbExpiry());
            cmpTRowDTO.setIs_batch(mRow.getBatch().toString());
            cmpTRowDTO.setManufacturing_date(String.valueOf(mRow.getManufacturingDate()));
            cmpTRowDTO.setB_purchase_rate(String.valueOf(mRow.getPurchaseRate()));
            cmpTRowDTO.setB_details_id(String.valueOf(mRow.getbDetailsId()));
            //  cmpTRowDTO.set("");
            cmpTRowDTO.setReference_id(mRow.getReferenceId().toString());
            cmpTRowDTO.setReference_type(mRow.getReferenceType());
            cmpTRowDTO.setDetails_id(String.valueOf(mRow.getDetailsId()));
            cmpTRowDTO.setBase_amt(String.valueOf(0.0));
            cmpTRowDTO.setRow_dis_amt(String.valueOf(0.0));
            cmpTRowDTO.setDis_per2(String.valueOf(mRow.getDisPer2()));
            cmpTRowDTO.setTotal_amt(String.valueOf(0.0));
            cmpTRowDTO.setIgst(String.valueOf(mRow.getIgst()));
            cmpTRowDTO.setCgst(String.valueOf(mRow.getCgst()));
            cmpTRowDTO.setSgst(String.valueOf(mRow.getSgst()));
            cmpTRowDTO.setTotal_igst(String.valueOf(mRow.getTotalIgst()));
            cmpTRowDTO.setTotal_sgst(String.valueOf(mRow.getTotalSgst()));
            cmpTRowDTO.setTotal_cgst(String.valueOf(mRow.getTotalCgst()));
            cmpTRowDTO.setFinal_amount(String.valueOf(mRow.getFinalAmt()));


            lblCounterSaleGrossTotal.setText(String.valueOf(mRow.getFinalAmt()));
            lblCounterSaleBillAmt.setText(String.valueOf(mRow.getFinalAmt()));

            if (mRow.getBatch() == true) {
//                TranxSelectedBatch selectedBatch = GlobalTranx.
                TranxSelectedBatch selectedBatch = TranxCommonPopUps.getSelectedBatchFromProductId(Integer.valueOf(mRow.getProductId() + ""),
                        csInvDate, mRow.getBatchNo());
                cmpTRowDTO.setSelectedBatch(selectedBatch);
                cmpTRowDTO.setRate(cmpTRowDTO.getRate());
            }

            TranxSelectedProduct selectedProduct = TranxCommonPopUps.getSelectedProductFromProductId(mRow.getProductName());
            cmpTRowDTO.setSelectedProduct(selectedProduct);

            List<LevelAForPurInvoice> levelAForPurInvoiceList = com.opethic.genivis.controller.tranx_sales.ProductUnitsPacking.getAllProductUnitsPackingFlavour(cmpTRowDTO.getProduct_id());
            ObservableList<LevelAForPurInvoice> observableLevelAList = FXCollections.observableArrayList(levelAForPurInvoiceList);
            if (index >= 0 && index < PurInvoiceCommunicator.levelAForPurInvoiceObservableList.size()) {
                PurInvoiceCommunicator.levelAForPurInvoiceObservableList.set(index, observableLevelAList);

                tblvCounterSaleView.getItems().get(index).setLevelA(null);
                tblvCounterSaleView.getItems().get(index).setLevelA(levelAForPurInvoiceList.get(0).getLabel());

                for (LevelAForPurInvoice levelAForPurInvoice : PurInvoiceCommunicator.levelAForPurInvoiceObservableList.get(index)) {
                    if (tblvCounterSaleView.getItems().get(index).getLevelA_id().equals(levelAForPurInvoice.getValue())) {
                        tblvCounterSaleView.getItems().get(index).setLevelA(null);
                        tblvCounterSaleView.getItems().get(index).setLevelA(levelAForPurInvoice.getLabel());
                    }
                }

            } else {
                PurInvoiceCommunicator.levelAForPurInvoiceObservableList.add(observableLevelAList);
            }

            tblvCounterSaleView.getItems().add(cmpTRowDTO);

            counterSaleCalculation.rowCalculationForPurcInvoice(index, tblvCounterSaleView, callback);
            counterSaleCalculation.calculateGst(tblvCounterSaleView, callback);

            index++;
            count++;

        }

        if (tfCounterCreateMobile.getText().isEmpty()) {
            tfCounterCreateMobile.requestFocus();
        } else {
            tfConsumerCName.requestFocus();
        }

    }

    private void shortcutKeysCS() {
        spCounterSaleRootPane.addEventFilter(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {

            @Override
            public void handle(KeyEvent event) {
                if (event.getCode() == KeyCode.S && event.isControlDown()) {
                    csInvoiceValidations();
                }
                if (event.getCode() == KeyCode.E && event.isControlDown()) {
//                    if (!tableView.getSelectionModel().isEmpty()) {
//                        CompanyAdminDTO selectedItem = (CompanyAdminDTO) tableView.getSelectionModel().getSelectedItem();
//                        selectedId = selectedItem.getId();
//                        get_company_admin_by_id(selectedId);
//                        event.consume();
//                    } else {
//                        AlertUtility.AlertErrorTimeout(AlertUtility.alertTypeError, "Select Company Admin", in -> {
//                            tableView.getSelectionModel().selectFirst();
//                            tableView.requestFocus();
//                            event.consume();
//                        });
//                    }
                }


                if (event.getCode() == KeyCode.X && event.isControlDown()) {
                    AlertUtility.AlertConfirmation(AlertUtility.alertTypeConfirmation, "Are You Sure ?", input -> {
                        if (input == 1) {
                            callClearTable();
                        }
                    });
                }

            }
        });
    }


}


class TextFieldTableCellForCounterSale extends TableCell<CounterSaleRowDTO, String> {
    private TextField textField;
    private String columnName;

    TableCellCallback<Object[]> callback;
    TableCellCallback<Object[]> consCallback;
    TableCellCallback<Object[]> addPrdCalbak;

    private ComboBox cmbCSPaymentMode;


    public TextFieldTableCellForCounterSale(String columnName, TableCellCallback<Object[]> callback) {
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
        qtyColumn();
        rateColumn();
        discountPer();
        restrictToDecimalFields();

    }

    public TextFieldTableCellForCounterSale(String columnName, TableCellCallback<Object[]> callback,  ComboBox cmbCSPaymentMode) {
        this.columnName = columnName;
        this.textField = new TextField();
        this.callback = callback;
        this.textField.setOnAction(event -> commitEdit(textField.getText()));
        this.cmbCSPaymentMode = cmbCSPaymentMode;
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
        qtyColumn();
        rateColumn();
        discountPer();
        restrictToDecimalFields();

    }

    public TextFieldTableCellForCounterSale(String columnName, TableCellCallback<Object[]> callback, TableCellCallback<Object[]> consCallback) {
        this.columnName = columnName;
        this.textField = new TextField();
        this.callback = callback;
        this.consCallback = consCallback;
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
        qtyColumn();
        rateColumn();
        discountPer();
        restrictToDecimalFields();

    }

    public TextFieldTableCellForCounterSale(String columnName, TableCellCallback<Object[]> callback, TableCellCallback<Object[]> consCallback, ComboBox cmbCSPaymentMode) {
        this.columnName = columnName;
        this.textField = new TextField();
        this.callback = callback;
        this.consCallback = consCallback;
        this.cmbCSPaymentMode = cmbCSPaymentMode;
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
        qtyColumn();
        rateColumn();
        discountPer();
        restrictToDecimalFields();

    }

    public TextFieldTableCellForCounterSale(String columnName, TableCellCallback<Object[]> callback, TableCellCallback<Object[]> consCallback, ComboBox cmbCSPaymentMode, TableCellCallback<Object[]> addPrdCalbak) {
        this.columnName = columnName;
        this.textField = new TextField();
        this.callback = callback;
        this.consCallback = consCallback;
        this.cmbCSPaymentMode = cmbCSPaymentMode;
        this.addPrdCalbak = addPrdCalbak;
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
            } else if (event.getCode() == KeyCode.ENTER) {
            }
        });

        textfieldStyle();
        particularsColumn();
        batchColumn();
        qtyColumn();
        rateColumn();
        discountPer();
        restrictToDecimalFields();

    }

    public void restrictToDecimalFields() {
        if ("tblcCounterSaleDIscPer".equals(columnName)) {
            CommonValidationsUtils.restrictToDecimalNumbers(textField);
        }
        if ("tblcCounterSaleQty".equals(columnName)) {
            CommonValidationsUtils.restrictToDecimalNumbers(textField);
        }
        if ("tblcCounterSaleRate".equals(columnName)) {
            CommonValidationsUtils.restrictToDecimalNumbers(textField);
        }
    }


    private void particularsColumn() {
        if ("tblcCounterSaleParticular".equals(columnName)) {
//            textField.setEditable(false);
            textField.setOnKeyPressed(event -> {
                if (event.getCode() == KeyCode.SPACE) {
                    openProduct();
                } else if (event.isShiftDown() && event.getCode() == KeyCode.TAB) {
                    if (getIndex() == 1) {
                        TableColumn<CounterSaleRowDTO, ?> colName = getTableView().getColumns().get(10);
                        getTableView().edit(getIndex() - 1, colName);
                    }
                } else if (event.getCode() == KeyCode.ENTER || !event.isShiftDown() && event.getCode() == KeyCode.TAB) {
                    if (textField.getText().isEmpty() && getIndex() == getTableView().getItems().size() - 1 && getIndex() != 0) {
                        Platform.runLater(() -> {
                            cmbCSPaymentMode.requestFocus();
                        });
                    } else if (!textField.getText().isEmpty()) {
                        TableColumn<CounterSaleRowDTO, ?> colName = getTableView().getColumns().get(6);
                        getTableView().edit(getIndex(), colName);
                        getDataByBarcode(textField.getText());
                    } else {
                        textField.requestFocus();
                    }
                    event.consume();
                }


            });
            textField.setOnMouseClicked(event -> {
                openProduct();
            });


        }
    }

    private void getDataByBarcode(String barcodeValue) {
//        CounterSaleRowDTO selectedRow = getTableView().getItems().get(getIndex());
        JsonArray mList = (JsonArray) TranxCommonPopUps.getAllTranxProductListByBarcode(barcodeValue, "");
        System.out.println("called It >>> : ");
        if (mList != null && mList.size() == 1) {
            JsonObject selectedProduct = mList.get(0).getAsJsonObject();
            getTableRow().getItem().setParticulars(selectedProduct.get("product_name").getAsString());
            getTableRow().getItem().setProduct_id(selectedProduct.get("id").getAsString());
            getTableRow().getItem().setPackages(selectedProduct.get("packing").getAsString());
            getTableRow().getItem().setIs_batch(selectedProduct.get("is_batch").getAsString());
            getTableRow().getItem().setTax(selectedProduct.get("igst").getAsString());
            getTableRow().getItem().setDrugType(selectedProduct.get("drugType").getAsString());
            getTableRow().getItem().setRate(selectedProduct.get("sales_rate").getAsString());
            List<LevelAForPurInvoice> levelAForPurInvoiceList = ProductUnitsPacking.getAllProductUnitsPackingFlavour(selectedProduct.get("id").getAsString());
            //                int index = getTableRow().getIndex();
            ObservableList<LevelAForPurInvoice> observableLevelAList = FXCollections.observableArrayList(levelAForPurInvoiceList);
            if (getIndex() >= 0 && getIndex() < PurInvoiceCommunicator.levelAForPurInvoiceObservableList.size()) {
                PurInvoiceCommunicator.levelAForPurInvoiceObservableList.set(getIndex(), observableLevelAList);
                getTableRow().getItem().setLevelA(null);
                getTableRow().getItem().setLevelA(levelAForPurInvoiceList.get(0).getLabel());

            } else {
                PurInvoiceCommunicator.levelAForPurInvoiceObservableList.add(observableLevelAList);
                getTableRow().getItem().setLevelA(null);
                getTableRow().getItem().setLevelA(levelAForPurInvoiceList.get(0).getLabel());
            }
            ObservableList<TranxSelectedBatch> lstBatch = TranxCommonPopUps.getAllTranxProductBatchListCounterSales(getTableRow().getItem(), Communicator.tranxDate);
            TranxSelectedBatch selectedBatchSingle = null;
            if (lstBatch != null) {
                String batchNo = selectedProduct.get("batch_data").getAsJsonArray().get(0).getAsJsonObject().get("batch_no").getAsString();
                for (TranxSelectedBatch selectedBatch :
                        lstBatch) {
                    if (selectedBatch.getBatchNo().equalsIgnoreCase(batchNo)) {
                        selectedBatchSingle = selectedBatch;
                        break;
                    }
                }

                if (selectedBatchSingle != null) {
                    Double mrp = selectedBatchSingle.getMrp();
                    Double id = (double) selectedBatchSingle.getId();
                    getTableRow().getItem().setB_details_id("" + selectedBatchSingle.getId());
                    getTableRow().getItem().setB_no(selectedBatchSingle.getBatchNo());
                    getTableRow().getItem().setBatch_or_serial(selectedBatchSingle.getBatchNo());
                    getTableRow().getItem().setRate(mrp.toString());
//                    getTableRow().getItem().setB_details_id(id.toString());
                    getTableRow().getItem().setRate_a(selectedBatchSingle.getUnit1FSRMH().toString());
                    getTableRow().getItem().setRate_b(selectedBatchSingle.getUnit1FSRAI().toString());
                    getTableRow().getItem().setRate_c(selectedBatchSingle.getUnit1CSRMH().toString());
                    getTableRow().getItem().setCosting(selectedBatchSingle.getUnit1CSRMH().toString());
                    getTableRow().getItem().setCosting_with_tax(selectedBatchSingle.getUnit1FSRAI().toString());
                    getTableRow().getItem().setMin_margin("0");
                    getTableRow().getItem().setB_purchase_rate(String.valueOf(selectedBatchSingle.getPurchaseRate()));
                    getTableRow().getItem().setB_rate(String.valueOf(selectedBatchSingle.getPurchaseRate()));
                    getTableRow().getItem().setIs_batch(String.valueOf(true));
                    getTableRow().getItem().setReference_id("");
                    getTableRow().getItem().setReference_type("");
                    if (selectedBatchSingle.getManufactureDate() != null && !selectedBatchSingle.getManufactureDate().isEmpty()) {
                        getTableRow().getItem().setManufacturing_date(String.valueOf(Communicator.text_to_date.fromString(selectedBatchSingle.getManufactureDate())));
                    } else {
                        getTableRow().getItem().setManufacturing_date("");
                    }
                    if (selectedBatchSingle.getExpiryDate() != null && !selectedBatchSingle.getExpiryDate().isEmpty()) {
                        getTableRow().getItem().setB_expiry(String.valueOf(Communicator.text_to_date.fromString(selectedBatchSingle.getExpiryDate())));
                    } else {
                        getTableRow().getItem().setManufacturing_date("");
                    }
                }

            }

            Platform.runLater(() -> {

                TableColumn<CounterSaleRowDTO, ?> colName = getTableView().getColumns().get(8);
                getTableView().edit(getIndex(), colName);
            });

        }
    }

    private void batchColumn() {
        if ("tblcCounterSaleBatchNo".equals(columnName)) {
            textField.setEditable(false);
            textField.setOnKeyPressed(event -> {
                if (event.getCode() == KeyCode.SPACE) {
                    openBatchWindow();
                }
                if (event.isShiftDown() && event.getCode() == KeyCode.TAB) {
                    if (!textField.getText().isEmpty()) {
                        TableColumn<CounterSaleRowDTO, ?> colName = getTableView().getColumns().get(1);
                        getTableView().edit(getIndex(), colName);
                    } else {
                        textField.requestFocus();
                    }
                } else if (event.getCode() == KeyCode.ENTER || !event.isShiftDown() && event.getCode() == KeyCode.TAB) {
                    if (textField.getText().isEmpty() && getIndex() == getTableView().getItems().size() - 1 && getIndex() != 0) {
                        Platform.runLater(() -> {
//                            button.requestFocus();
                        });
                    } else if (!textField.getText().isEmpty()) {
                        TableColumn<CounterSaleRowDTO, ?> colName = getTableView().getColumns().get(7);
                        getTableView().edit(getIndex(), colName);
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

    private void qtyColumn() {
        if ("tblcCounterSaleQty".equals(columnName)) {
            textField.setEditable(true);
            textField.setOnKeyPressed(event -> {
                if (event.isShiftDown() && event.getCode() == KeyCode.TAB) {
                    if (!textField.getText().isEmpty()) {
                        TableColumn<CounterSaleRowDTO, ?> colName = getTableView().getColumns().get(7);
                        getTableView().edit(getIndex(), colName);
                    } else {
                        textField.requestFocus();
                    }
                } else if (event.getCode() == KeyCode.ENTER || event.getCode() == KeyCode.TAB) {
                    if (!textField.getText().isEmpty()) {
                        Integer index = getIndex();
                        counterSaleCalculation.rowCalculationForPurcInvoice(index, getTableView(), callback);
                        counterSaleCalculation.calculateGst(getTableView(), callback);
                        TableColumn<CounterSaleRowDTO, ?> colName = getTableView().getColumns().get(9);
                        getTableView().edit(getIndex(), colName);
                        if (consCallback != null) {
                            Object[] object = new Object[3];
//                            if (!getTableRow().getItem().getDrugType().isEmpty()) {
//                                object[0] = true;
//                            }else{
//                                object[0] = false;
//                            }
                            object[0] = false;
                            object[2] = "CalCheck";
                            consCallback.call(object);
                        }
                    } else {
                        textField.requestFocus();
                    }
                }
            });
        }
    }

    private void rateColumn() {
        if ("tblcCounterSaleRate".equals(columnName)) {
            textField.setEditable(true);
            textField.setOnKeyPressed(event -> {
                if (event.isShiftDown() && event.getCode() == KeyCode.TAB) {
                    if (!textField.getText().isEmpty()) {
                        TableColumn<CounterSaleRowDTO, ?> colName = getTableView().getColumns().get(8);
                        getTableView().edit(getIndex(), colName);
                    } else {
                        textField.requestFocus();
                    }
                } else if (event.getCode() == KeyCode.ENTER || event.getCode() == KeyCode.TAB) {
                    if (!textField.getText().isEmpty()) {
                        Integer index = getIndex();
                        counterSaleCalculation.rowCalculationForPurcInvoice(index, getTableView(), callback);
                        counterSaleCalculation.calculateGst(getTableView(), callback);
                        TableColumn<CounterSaleRowDTO, ?> colName = getTableView().getColumns().get(10);
                        getTableView().edit(getIndex(), colName);
                        if (consCallback != null) {
                            Object[] object = new Object[3];
//                            if (!getTableRow().getItem().getDrugType().isEmpty()) {
//                                object[0] = true;
//                            }else{
//                                object[0] = false;
//                            }
                            object[0] = false;
                            object[2] = "CalCheck";
                            consCallback.call(object);
                        }
                    } else {
                        textField.requestFocus();
                    }
                }
            });
        }
    }

    private void discountPer() {
        if ("tblcCounterSaleDIscPer".equals(columnName)) {
            textField.setEditable(true);
            textField.setOnKeyPressed(event -> {
                if (event.isShiftDown() && event.getCode() == KeyCode.TAB) {
                    if (!textField.getText().isEmpty()) {
                        TableColumn<CounterSaleRowDTO, ?> colName = getTableView().getColumns().get(9);
                        getTableView().edit(getIndex(), colName);
                    } else {
                        textField.requestFocus();
                    }
                } else if (event.getCode() == KeyCode.ENTER || event.getCode() == KeyCode.TAB) {
                    int index = getIndex();
                    if (index >= getTableView().getItems().size() - 1){
                        System.out.println("Index of >>> "+ index);
                        CounterSaleRowDTO selectedRow = getTableView().getItems().get(index);
                        if(!selectedRow.getParticulars().isEmpty()){
                            getTableView().getItems().addAll(new CounterSaleRowDTO("", "", "" + (getIndex() + 2), "", "", "", "",
                                    "", "", "", "", "", "", "", "", "", "", ""));
                            TableColumn<CounterSaleRowDTO, ?> colName = getTableView().getColumns().get(1);
                            getTableView().edit(getIndex() + 1, colName);
                        }else{
                            cmbCSPaymentMode.requestFocus();
                        }
                    }else{
                        TableColumn<CounterSaleRowDTO, ?> colName = getTableView().getColumns().get(1);
                        getTableView().edit(getIndex() + 1, colName);
                    }

                    counterSaleCalculation.rowCalculationForPurcInvoice(index, getTableView(), callback);
                    counterSaleCalculation.calculateGst(getTableView(), callback);

                }
            });
        }
    }

    private void openProduct() {
        CounterSaleRowDTO selectedRow = getTableView().getItems().get(getIndex());
        TranxCommonPopUps.openProductPopUp(Communicator.stage, Integer.valueOf(selectedRow.getProduct_id()), "Counter Sale", input -> {
            if (input != null) {
                getTableRow().getItem().setParticulars(input.getProductName());
                getTableRow().getItem().setProduct_id(input.getProductId().toString());
                getTableRow().getItem().setPackages(input.getPackageName());
                getTableRow().getItem().setIs_batch(input.getBatch().toString());
                getTableRow().getItem().setTax(input.getIgst().toString());
                getTableRow().getItem().setDrugType(input.getDrugType());
                getTableRow().getItem().setSelectedProduct(input);

                if (!getTableRow().getItem().getDrugType().isEmpty()) {
                    if (consCallback != null) {
                        Object[] object = new Object[3];
                        object[0] = true;
                        object[1] = getIndex();
                        object[2] = "";
                        consCallback.call(object);
                    }

                }

//                getTableRow().getItem().setUnit(input.get());
                // getTableRow().getItem().setSelectedProduct(input);
//                getTableRow().getItem().setRate(input.getSalesRate().toString());
//                getTableRow().getItem().
                List<LevelAForPurInvoice> levelAForPurInvoiceList = ProductUnitsPacking.getAllProductUnitsPackingFlavour(input.getProductId().toString());
                //                int index = getTableRow().getIndex();
                ObservableList<LevelAForPurInvoice> observableLevelAList = FXCollections.observableArrayList(levelAForPurInvoiceList);
                if (getIndex() >= 0 && getIndex() < PurInvoiceCommunicator.levelAForPurInvoiceObservableList.size()) {
                    PurInvoiceCommunicator.levelAForPurInvoiceObservableList.set(getIndex(), observableLevelAList);
                    getTableRow().getItem().setLevelA(null);
                    getTableRow().getItem().setLevelA(levelAForPurInvoiceList.get(0).getLabel());

                } else {
                    PurInvoiceCommunicator.levelAForPurInvoiceObservableList.add(observableLevelAList);
                    getTableRow().getItem().setLevelA(null);
                    getTableRow().getItem().setLevelA(levelAForPurInvoiceList.get(0).getLabel());
                }
                TableColumn<CounterSaleRowDTO, ?> colName = getTableView().getColumns().get(6);
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
    }


    public void openBatchWindow() {
        CounterSaleRowDTO selectedRow = getTableView().getItems().get(getIndex());
        if (Boolean.valueOf(selectedRow.getIs_batch()) == true) {
            TranxCommonPopUps.openBatchCSPopUpSales(Communicator.stage, selectedRow, Communicator.tranxDate, "Batch", input -> {
                if (input != null) {
                    //  selectedRow.setSelectedBatch(input);
                    Double mrp = input.getMrp();
                    Double id = (double) input.getId();
                    selectedRow.setSelectedBatch(input);
                    selectedRow.setB_details_id("" + input.getId());
                    selectedRow.setB_no(input.getBatchNo());
                    selectedRow.setBatch_or_serial(input.getBatchNo());
                    selectedRow.setRate(input.getUnit1FSRMH().toString());
//                    selectedRow.setB_details_id(id.toString());
                    selectedRow.setRate_a(input.getUnit1FSRMH().toString());
                    selectedRow.setRate_b(input.getUnit1FSRAI().toString());
                    selectedRow.setRate_c(input.getUnit1CSRMH().toString());
                    selectedRow.setCosting(input.getUnit1CSRMH().toString());
                    selectedRow.setCosting_with_tax(input.getUnit1FSRAI().toString());
                    selectedRow.setMin_margin("0");
                    selectedRow.setB_purchase_rate(String.valueOf(input.getPurchaseRate()));
                    selectedRow.setB_rate(String.valueOf(input.getPurchaseRate()));
                    selectedRow.setIs_batch(String.valueOf(true));
                    selectedRow.setReference_id("");
                    selectedRow.setReference_type("");
                    if (input.getManufactureDate() != null && !input.getManufactureDate().isEmpty()) {
                        selectedRow.setManufacturing_date(String.valueOf(Communicator.text_to_date.fromString(input.getManufactureDate())));
                    } else {
                        selectedRow.setManufacturing_date("");
                    }
                    if (input.getExpiryDate() != null && !input.getExpiryDate().isEmpty()) {
                        selectedRow.setB_expiry(String.valueOf(Communicator.text_to_date.fromString(input.getExpiryDate())));
                    } else {
                        selectedRow.setManufacturing_date("");
                    }
                }
//                getTableView().getItems().set(getIndex(), selectedRow);

//                getBatchDetailsByBatchNoAndId(input.getBatchNo(), String.valueOf(input.getId()));

            });
        } else {
            selectedRow.setBatch_or_serial("#");
            selectedRow.setB_no("#");
//            getTableView().getItems().set(getIndex(), selectedRow);
        }
        Platform.runLater(() -> {
            TableColumn<CounterSaleRowDTO, ?> colName = getTableView().getColumns().get(7);
            getTableView().edit(getIndex(), colName);
        });
    }


    public void textfieldStyle() {
        if (columnName.equals("tblcCounterSaleParticular")) {
            textField.setPromptText("Particulars");
//            textField.setEditable(false);
        } else {
            textField.setPromptText("0");
        }

        /*textField.setPrefHeight(38);
        textField.setMaxHeight(38);
        textField.setMinHeight(38);*/

        if (columnName.equals("tblcCounterSaleParticular")) {
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


//        if (columnName.equals("tblcCounterSaleNetAmt")) {
//            this.textField.setOnKeyPressed(event -> {
//                if (event.getCode() == KeyCode.ENTER) {
//                    commitEdit(textField.getText());
//                    getTableView().getItems().addAll(new CounterSaleRowDTO("", "", "1", "", "", "", "", "", "", "", "", "", "", "", "", "", "", ""));
//                }
//            });
//        }
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
        if (item != null && columnName.equals("tblcCounterSaleParticular")) {
            ((CounterSaleRowDTO) item).setParticulars(newValue.isEmpty() ? "" : newValue);
        } else if (columnName.equals("tblcCounterSaleBatchNo")) {
            (getTableRow().getItem()).setB_no(newValue.isEmpty() ? "" : newValue);
        } else if (columnName.equals("tblcCounterSaleQty")) {
            (getTableRow().getItem()).setQuantity(newValue.isEmpty() ? "0" : newValue);
        } else if (columnName.equals("tblcCounterSaleRate")) {
            (getTableRow().getItem()).setRate(newValue.isEmpty() ? "0.0" : newValue);
        } else if (columnName.equals("tblcCounterSaleDIscPer")) {
            (getTableRow().getItem()).setDis_per(newValue.isEmpty() ? "0.0" : newValue);
        } else if (columnName.equals("tblcCounterSaleNetAmt")) {
            (getTableRow().getItem()).setNet_amount(newValue.isEmpty() ? "0.0" : newValue);
        }

    }
}

class ComboBoxTableCellForCSLevelA extends TableCell<CounterSaleRowDTO, String> {

    String columnName;
    private final ComboBox<String> comboBox;
    ObservableList<LevelAForPurInvoice> comboList = null;

    public ComboBoxTableCellForCSLevelA(String columnName) {

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
        TableRow<CounterSaleRowDTO> row = getTableRow();
        if (row != null) {
            CounterSaleRowDTO item = row.getItem();
            if (item != null) {
                item.setLevelA(newValue);
            }
        }
    }

}

class ComboBoxTableCellForCSLevelB extends TableCell<CounterSaleRowDTO, String> {

    String columnName;
    private final ComboBox<String> comboBox;
    ObservableList<LevelBForPurInvoice> comboList = null;

    public ComboBoxTableCellForCSLevelB(String columnName) {
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

class ComboBoxTableCellForCSLevelC extends TableCell<CounterSaleRowDTO, String> {

    String columnName;
    private final ComboBox<String> comboBox;
    ObservableList<LevelAForPurInvoice> comboList = null;

    public ComboBoxTableCellForCSLevelC(String columnName) {


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

class ComboBoxTableCellForCSUnit extends TableCell<CounterSaleRowDTO, String> {

    String columnName;
    private final ComboBox<String> comboBox;
    TableCellCallback<Integer> unit_callback;

    public ComboBoxTableCellForCSUnit(String columnName, TableCellCallback<Integer> unit_callback) {


        this.columnName = columnName;
        this.comboBox = new ComboBox<>();
        this.unit_callback = unit_callback;


        this.comboBox.setPromptText("Select");


        /*comboBox.setPrefHeight(38);
        comboBox.setMaxHeight(38);
        comboBox.setMinHeight(38);
        comboBox.setPrefWidth(179);
        comboBox.setMaxWidth(179);
        comboBox.setMinWidth(99);*/

        this.comboBox.setStyle("-fx-background-color: #f6f6f9; -fx-border-radius: 0px; -fx-border-width: 1; -fx-border-color: #f6f6f9;");

        this.comboBox.focusedProperty().addListener((obs, wasFocused, isNowFocused) -> {
            if (isNowFocused) {
                this.comboBox.show();
                this.comboBox.setStyle("-fx-background-color: #fff9c4; -fx-border-radius: 0px; -fx-border-width: 1.5; -fx-border-color: #00a0f5;");
            } else {
                commitEdit(String.valueOf(comboBox.getValue()));
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
        this.comboBox.setFocusTraversable(true);


//        this.comboBox.focusedProperty().addListener((obs, oldVal, newVal) -> {
////
//        });

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
                            getTableRow().getItem().setUnit_conv(String.valueOf(unitForPurInvoice.getUnitConversion()));
                            if (this.unit_callback != null) {
                                this.unit_callback.call(getIndex());
                            }
                        }

                    }
                }


             /*   Platform.runLater(() -> {
                    TableColumn<CounterSaleRowDTO, ?> colName = getTableView().getColumns().get(8);
                    getTableView().edit(getIndex(), colName);
                });*/

            }

        });

        comboBox.setOnKeyPressed(event -> {
            if (event.isShiftDown() && event.getCode() == KeyCode.TAB) {
                TableColumn<CounterSaleRowDTO, ?> colName = getTableView().getColumns().get(6);
                getTableView().edit(getIndex(), colName);
            }

            if (event.getCode() == KeyCode.ENTER || !event.isShiftDown() && event.getCode() == KeyCode.TAB) {
                TableColumn<CounterSaleRowDTO, ?> colName = getTableView().getColumns().get(8);
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

    @Override
    public void commitEdit(String newValue) {
        super.commitEdit(newValue);
        (getTableRow().getItem()).setUnit(newValue);
    }

}

class ButtonTableCellForCounterSale extends TableCell<CounterSaleRowDTO, String> {
    private Button delete;
    TableCellCallback<Object[]> callback;

    public ButtonTableCellForCounterSale(TableCellCallback<Object[]> DelCallback) {


        this.delete = createButtonWithImage();

        callback = DelCallback;

        delete.setOnAction(actionEvent -> {
            CounterSaleRowDTO table = getTableView().getItems().get(getIndex());
            getTableView().getItems().remove(table);
            Object object[] = new Object[1];
            if (callback != null) {
                object[0] = table.getDetails_id();
                callback.call(object);
            }
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

class ProductUnitsPackingCS {

    public static List<LevelAForPurInvoice> getAllProductUnitsPackingFlavour(String product_id) {


        Map<String, String> map = new HashMap<>();
        map.put("product_id", product_id);

        List<LevelAForPurInvoice> levelAForPurInvoicesList = new ArrayList<>();
        try {
            String formdata = mapToStringforFormData(map);

            HttpResponse<String> response = APIClient.postFormDataRequest(formdata, "get_all_product_units_packings_flavour");
            String responseBody = response.body();
            JsonObject jsonObject = new Gson().fromJson(responseBody, JsonObject.class);

            int responseStatus = jsonObject.get("responseStatus").getAsInt();

            if (responseStatus == 200) {
                JsonObject resObj = jsonObject.get("responseObject").getAsJsonObject();
                JsonArray lstPackages = resObj.get("lst_packages").getAsJsonArray();

//                LevelAForPurInvoice levelAForPurInvoice2 = new Gson().fromJson(lstPackages, LevelAForPurInvoice.class);
//


                for (JsonElement pack : lstPackages) {
                    try {
                        LevelAForPurInvoice levelAForPurInvoice = new Gson().fromJson(pack, LevelAForPurInvoice.class);
                        levelAForPurInvoicesList.add(levelAForPurInvoice);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
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

class counterSaleCalculation {

    public static void rowCalculationForPurcInvoice(int rowIndex, TableView<CounterSaleRowDTO> tableView, TableCellCallback<Object[]> callback) {

        CounterSaleRowDTO purchaseInvoiceTable = tableView.getItems().get(rowIndex);
        if (!purchaseInvoiceTable.getQuantity().isEmpty() && Double.parseDouble(purchaseInvoiceTable.getQuantity()) > 0 &&
                !purchaseInvoiceTable.getRate().isEmpty() && Double.parseDouble(purchaseInvoiceTable.getRate()) > 0) {
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
            Double totalDiscount = 0.0;
            Double rate_dis = 0.0;
            Double base_amt1 = 0.0;
            Double totalRate = 0.0;
            Double gstPerQty = 0.0;
            Double totalGstAmt = 0.0;
            Double disPer2 = 0.0;//to store discount per2

            //get the row level data by row index wise
            r_qty = purchaseInvoiceTable.getQuantity();
            r_rate = Double.parseDouble(purchaseInvoiceTable.getRate());
            if (!purchaseInvoiceTable.getDis_per().isEmpty())
                r_dis_per = Double.parseDouble(purchaseInvoiceTable.getDis_per());

            r_tax_per = Double.parseDouble(purchaseInvoiceTable.getTax());
            prevTaxPer = r_tax_per;


            //calculate base amount
            base_amt = Double.parseDouble(r_qty) * r_rate;
            gross_amt = base_amt;
            total_amt = base_amt;
            total_base_amt = base_amt;
            taxable_amt = base_amt;
            total_dis_amt = r_dis_amt;

            //calculate discount percentage
            if (r_dis_per > 0) {
                base_amt1 = base_amt;
                total_dis_per = (r_rate * r_dis_per) / 100;//per product dis

                totalDiscount = total_dis_per * Double.valueOf(r_qty); //total dis

                rate_dis = r_rate - total_dis_per;// per product discount minus to base amt

                total_amt = rate_dis * Double.valueOf(r_qty);

            }

            // tax GST and CGST and SGST Calculations
            if (r_tax_per >= 0) {
                if (rate_dis > 0) {
                    gstPerQty = (rate_dis * Double.valueOf(r_tax_per)) / (100 + r_tax_per);
                } else {
                    gstPerQty = (r_rate * r_tax_per) / (100 + r_tax_per);
                }
                totalGstAmt = (gstPerQty * Double.valueOf(r_qty));
                taxable_amt = total_amt - totalGstAmt;
                total_amt = taxable_amt + totalGstAmt;

            }


            net_amt = total_amt;

            // set all calculated above data to CMPTROW DTOString.format("%.2f", taxPer)
            CounterSaleRowDTO selectedItem = tableView.getItems().get(rowIndex);
            selectedItem.setGross_amount(String.valueOf(base_amt));
            selectedItem.setNet_amount(String.format("%.2f", net_amt));
            selectedItem.setOrg_net_amt(String.format("%.2f", net_amt));
//        selectedItem.setOrg_taxable_amt(taxable_amt);
            selectedItem.setTaxable_amt(String.valueOf(taxable_amt));
            selectedItem.setTotal_taxable_amt(String.valueOf(total_taxable_amt));
            selectedItem.setFinal_tax_amt(String.valueOf(totalGstAmt));
            selectedItem.setFinal_dis_amt(String.valueOf(totalDiscount));

            // Create API Payload Parameters Adding
            selectedItem.setBase_amt(String.valueOf(base_amt));
//        selectedItem.setGross_amt(String.valueOf(gross_amt);
            selectedItem.setDis_per(String.valueOf((r_dis_per)));
            selectedItem.setDis_per_cal(String.valueOf(total_dis_per));
            selectedItem.setDis_amt(String.valueOf((r_dis_amt)));
            selectedItem.setDis_amt_cal(String.valueOf(r_dis_amt));
            selectedItem.setRow_dis_amt(String.valueOf(row_dis_amt));
            selectedItem.setGross_amount1(String.valueOf(taxable_amt));
            // tax percentage store cmptrow
            selectedItem.setGst(String.valueOf(r_tax_per));
            selectedItem.setIgst(String.valueOf(r_tax_per));
            selectedItem.setSgst(String.valueOf(r_tax_per / 2));
            selectedItem.setCgst(String.valueOf(r_tax_per / 2));
            selectedItem.setTotal_igst(String.valueOf(totalTax));
            selectedItem.setTotal_sgst(String.valueOf(totalTax / 2));
            selectedItem.setTotal_cgst(String.valueOf(totalTax / 2));
            selectedItem.setFinal_amount(String.valueOf(net_amt));
            selectedItem.setDis_per2(String.valueOf(disPer2));

        }
    }

    public static void calculateGst(TableView<CounterSaleRowDTO> tableView, TableCellCallback<Object[]> callback) {


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
        for (CounterSaleRowDTO purchaseInvoiceTable : tableView.getItems()) {
            if (!purchaseInvoiceTable.getQuantity().isEmpty() && Double.parseDouble(purchaseInvoiceTable.getQuantity()) > 0 &&
                    !purchaseInvoiceTable.getRate().isEmpty() && Double.parseDouble(purchaseInvoiceTable.getRate()) > 0) {
                taxPercentage = Double.parseDouble(purchaseInvoiceTable.getTax());
                Double quantity = Double.parseDouble(purchaseInvoiceTable.getQuantity());
                if (purchaseInvoiceTable.getFree().isEmpty()) {
                    freeQuantity = 0.0;
                } else {
                    freeQuantity = Double.parseDouble(purchaseInvoiceTable.getFree());
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


                //Total Calculation of gross amt ,taxable ,tax,discount
                netAmount = Double.parseDouble(purchaseInvoiceTable.getNet_amount());
                totalNetAmt += netAmount;
                grossAmt = Double.parseDouble(purchaseInvoiceTable.getGross_amount());
                totalGrossAmt += grossAmt;
                taxableAmt = Double.parseDouble(purchaseInvoiceTable.getTaxable_amt().toString());
                totaltaxableAmt += taxableAmt;
                disAmt = Double.parseDouble(purchaseInvoiceTable.getFinal_dis_amt().toString());
                totalDisAmt += disAmt;
                taxAmt = Double.parseDouble(purchaseInvoiceTable.getFinal_tax_amt().toString());
                totalTaxAmt += taxAmt;

            }

        }

        ObservableList<GstDTO> observableList = FXCollections.observableArrayList();

        if (callback != null) {

            Object[] object = new Object[10];

            object[0] = decimalFormat.format(totalQuantity);

            object[1] = String.valueOf(totalFreeQuantity);

            object[2] = String.format("%.2f", totalNetAmt);

            object[3] = String.format("%.2f", totalGrossAmt);

            object[4] = String.format("%.2f", totalDisAmt);

            //  lblPurChallTotalTaxableAmount.setText(String.format("%.2f", (totalGrossAmt - totalDisAmt + addChargAmount)));
            object[5] = String.format("%.2f", (totaltaxableAmt));

            object[6] = String.format("%.2f", totalTaxAmt);

            object[7] = String.format("%.2f", totalFinalSgst);

            object[8] = String.format("%.2f", totalFinalCgst);

            object[9] = observableList;


            if (callback != null) {
                callback.call(object);
            }
        }
    }


    public static void discountPropotionalCalculation(String disproportionalPer, String disproportionalAmt, String additionalCharges, TableView<CounterSaleRowDTO> tableView, TableCellCallback<Object[]> callback) {

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
        Double totalNetAmt = 0.0;//Store net Amount after calculating  dis and tax
        Double rowTaxableAmt = 0.0; //store the row level calculated taxable amount
        Double gstPerQty = 0.0; //store the row level calculated taxable amount
        Double totalgstAmt = 0.0; //store the row level calculated taxable amount
        Double rowQty = 0.0; //store the row level calculated taxable amount
        Double rowNetAmt = 0.0; //store the row level calculated taxable amount
        Double totalTaxableAmt = 0.0;//store the total calculated taxable amount
        Double totalDisPropAmt = 0.0;//store the discount calculated proportional amt
        Double rowDisPropAmt = 0.0;//stotre the row level discount calculated amount
        Double totalTaxableAmtAdditional = 0.0;
        Double rowtotalTaxableAmtAdditional = 0.0;
        Double totalAmt = 0.0;
        Double baseAmt = 0.0;
        Double ini_tax_amt = 0.0;


        //calculate total taxable amount
        for (int i = 0; i < tableView.getItems().size(); i++) {
            rowCalculationForPurcInvoice(i, tableView, callback);//call row calculation function
            CounterSaleRowDTO purchaseInvoiceTable = tableView.getItems().get(i);
            totalTaxableAmt = totalTaxableAmt + Double.parseDouble(purchaseInvoiceTable.getTaxable_amt().toString());
        }

        Double rowDisc = 0.0;
        //get data of netamount, tax per,and row taxable amount from cmptrow
        for (CounterSaleRowDTO purchaseInvoiceTable : tableView.getItems()) {
            if (!purchaseInvoiceTable.getQuantity().isEmpty() && Double.parseDouble(purchaseInvoiceTable.getQuantity()) > 0 &&
                    !purchaseInvoiceTable.getRate().isEmpty() && Double.parseDouble(purchaseInvoiceTable.getRate()) > 0) {
                rowDisc = Double.parseDouble(purchaseInvoiceTable.getFinal_dis_amt().toString());
                netAmt = Double.parseDouble(purchaseInvoiceTable.getNet_amount());
                totalNetAmt = netAmt;
                r_tax_per = Double.parseDouble(purchaseInvoiceTable.getTax());
                rowTaxableAmt = Double.parseDouble(purchaseInvoiceTable.getTaxable_amt().toString());
                rowNetAmt = Double.parseDouble(purchaseInvoiceTable.getTaxable_amt().toString());
                rowQty = Double.parseDouble(purchaseInvoiceTable.getQuantity());
                baseAmt = Double.parseDouble(purchaseInvoiceTable.getGross_amount());
                ini_tax_amt = Double.parseDouble(purchaseInvoiceTable.getFinal_tax_amt());
                //calculate discount proportional percentage and  the taxable amount
                if (dis_proportional_per > 0.0) {
                    totalDisPropAmt = (totalNetAmt * dis_proportional_per) / 100;
                    totalNetAmt = totalNetAmt - totalDisPropAmt;
                    gstPerQty = (r_tax_per * totalNetAmt) / (100 + r_tax_per);
                    totalgstAmt = gstPerQty * rowQty;
                    total_tax_amt = totalgstAmt;
                    totalAmt = baseAmt - totalgstAmt;
                    rowDisc += totalDisPropAmt;
                    rowTaxableAmt = totalAmt - rowDisc;
                    rowNetAmt = rowTaxableAmt + totalgstAmt;
                    totalTaxableAmtAdditional = rowTaxableAmt;
                } else {
                    totalTaxableAmtAdditional = rowTaxableAmt;
                    rowNetAmt = netAmt;
                    total_tax_amt = ini_tax_amt;
                }

                netAmt = rowNetAmt;

                //Set data to cmpTRow
                purchaseInvoiceTable.setNet_amount(String.format("%.2f", netAmt));
                purchaseInvoiceTable.setTotal_taxable_amt(String.valueOf(totalTaxableAmtAdditional));
                purchaseInvoiceTable.setTaxable_amt(String.valueOf(totalTaxableAmtAdditional));
                purchaseInvoiceTable.setFinal_tax_amt(String.valueOf(total_tax_amt));
                purchaseInvoiceTable.setFinal_dis_amt(String.valueOf(rowDisc));
                purchaseInvoiceTable.setInvoice_dis_amt(String.valueOf(rowDisPropAmt));
            }
        }
        calculateGst(tableView, callback);
    }


}


