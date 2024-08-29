package com.opethic.genivis.controller.tranx_sales;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.opethic.genivis.GenivisApplication;
import com.opethic.genivis.controller.users.UserRoleCreateController;
import com.opethic.genivis.dto.*;
import com.opethic.genivis.dto.reqres.product.Communicator;
import com.opethic.genivis.dto.reqres.pur_tranx.TranxPurRtnRowDataListDTO;
import com.opethic.genivis.network.APIClient;
import com.opethic.genivis.network.EndPoints;
import com.opethic.genivis.network.RequestType;
import com.opethic.genivis.utils.*;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.stage.Screen;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;
import java.net.http.HttpResponse;
import java.util.*;

import static com.opethic.genivis.utils.FxmFileConstants.*;
import static com.opethic.genivis.utils.Globals.salesOrderListDTO;
import static com.opethic.genivis.utils.Globals.userRoleModel;

public class SalesQuotationListController implements Initializable {
    private ObservableList<SalesQuotationDTO> salesQuotObservableList = FXCollections.observableArrayList();
    private static final Logger SalesQuotationListLogger = LoggerFactory.getLogger(SalesQuotationListController.class);
    @FXML
    private BorderPane spRootPane;
    @FXML
    private TableView<SalesQuotationDTO> tblvSalesQuotationList;
    @FXML
    private TableColumn<SalesQuotationDTO, String> tblcSalesQuotationListSQNo, tblcSalesQuotationListSQDate, tblcSalesQuotationListClientName, tblcSalesQuotationListNarration,
            tblcSalesQuotationListTaxable, tblcSalesQuotationListTax, tblcSalesQuotationListBillAmount, tblcSalesQuotationListPrint, tblcSalesQuotationListAction;
    @FXML
    TableColumn<SalesQuotationDTO, Boolean> tblcSalesQuotationListSelect;
    @FXML
    private TableColumn tblcSalesQuotationListTranxStatus;

    @FXML
    private TextField tfSalesQuotationListSearch;
    @FXML
    private TextField DPSalesQuotationListToDt, DPSalesQuotationListFromDt;
    @FXML
    private Button btnSalesQuotationListCreate, btnSalesQuotationListToOrder, btnSalesQuotationListToChallan, btnSalesQuotationListToInvoice;

    private Node[] focusableNodes;
    private ObservableList<SalesQuotationDTO> originalData;
    int i = 0, Selected_index = 0;
    private String responseBody, message;
    public static String focusStatus;
    public static Integer selectedRowIndex;
    Integer ListSize = 0;
    private Integer id1;


    public void initialize(URL url, ResourceBundle resourceBundle) {

        ResponsiveWiseCssPicker();

        SalesQuotationListLogger.info("Start of Initialize method of :SalesQuotationListController");
        originalData = FXCollections.observableArrayList();

        //         Enter traversal
        spRootPane.addEventFilter(KeyEvent.KEY_PRESSED, (KeyEvent event) -> {
            if (event.getCode() == KeyCode.ENTER) {

                if (tblvSalesQuotationList.isFocused()) {
//                        userRoleModel = tblvSalesQuotationList.getSelectionModel().getSelectedItem();
                    SalesQuotationDTO selectedItem = (SalesQuotationDTO) tblvSalesQuotationList.getSelectionModel().getSelectedItem();
                    String SalesQuotId = selectedItem.getId();
                    SalesQuotationCreateController.selectedRowIndex = tblvSalesQuotationList.getSelectionModel().getFocusedIndex();
                    System.out.println("SalesQuotId 1 -->" + SalesQuotId);

                    String status = selectedItem.getSales_quotation_status();
                    // Check if the status is "opened"
                    if (status != null && status.equalsIgnoreCase("opened")) {
                        GlobalController.getInstance().addTabStaticWithParam(SALES_QUOTATION_EDIT_SLUG, false, Integer.valueOf(SalesQuotId));
                    } else {

                        AlertUtility.AlertErrorTimeout(AlertUtility.alertTypeError, "The Quotation is closed", in -> {
                            tfSalesQuotationListSearch.requestFocus();
                        });
                    }
                }
            }
        });

        //todo : autofocus on TranxDate
        Platform.runLater(() -> tfSalesQuotationListSearch.requestFocus()
        );

        //todo : Focus on Enter
        tfSalesQuotationListSearch.setOnKeyPressed(event->{
            if(event.getCode()==KeyCode.ENTER){
                DPSalesQuotationListFromDt.requestFocus();
            } else if (event.getCode()==KeyCode.DOWN) {
                tblvSalesQuotationList.requestFocus();
                tblvSalesQuotationList.getSelectionModel().select(0);
                tblvSalesQuotationList.getFocusModel().focus(0);
            }
        });
        DPSalesQuotationListFromDt.setOnKeyPressed(event->{
            if(event.getCode()==KeyCode.ENTER){
                DPSalesQuotationListToDt.requestFocus();
            }else if (event.getCode()==KeyCode.DOWN) {
                tblvSalesQuotationList.requestFocus();
                tblvSalesQuotationList.getSelectionModel().select(0);
                tblvSalesQuotationList.getFocusModel().focus(0);
            }
        });
        DPSalesQuotationListToDt.setOnKeyPressed(event->{
            if(event.getCode()==KeyCode.ENTER){

                if (!DPSalesQuotationListFromDt.getText().isEmpty() && !DPSalesQuotationListToDt.getText().isEmpty()) {
                    fetchSalesQuotationInvoiceList("");
                }
                else {
                    btnSalesQuotationListCreate.requestFocus();
                }
            }else if (event.getCode()==KeyCode.DOWN) {
                tblvSalesQuotationList.requestFocus();
                tblvSalesQuotationList.getSelectionModel().select(0);
                tblvSalesQuotationList.getFocusModel().focus(0);
            }
        });
        btnSalesQuotationListCreate.setOnKeyPressed(event->{
            if(event.getCode()==KeyCode.ENTER){
                GlobalController.getInstance().addTabStatic(SALES_QUOTATION_CREATE_SLUG, false);
            }
            else if (event.getCode()==KeyCode.DOWN) {
                tblvSalesQuotationList.requestFocus();
                tblvSalesQuotationList.getSelectionModel().select(0);
                tblvSalesQuotationList.getFocusModel().focus(0);
            }
        });


        spRootPane.sceneProperty().addListener((observable, oldScene, newScene) -> {
            if (newScene != null && newScene.getWindow() instanceof Stage) {
                Communicator.stage = (Stage) newScene.getWindow();
            }
        });

        DateValidator.applyDateFormat(DPSalesQuotationListFromDt);
        DateValidator.applyDateFormat(DPSalesQuotationListToDt);

        tblcSalesQuotationListTranxStatus.setCellValueFactory(new PropertyValueFactory<>("tblcSalesQuotationListTranxStatus"));
        tblcSalesQuotationListTranxStatus.setCellFactory(column -> {
            return new TableCell<SalesQuotationDTO, String>() {
                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if (item == null || empty) {
                        setText(null);
                    } else {
                        setText(item);
                        // Set text color to white
//                        setTextFill(Color.WHITE);
                        setStyle("-fx-font-weight:bold;-fx-text-fill:#fff");

                        // Check the value of tcSalesOrderListTranxStatus
                        if (item.equalsIgnoreCase("opened")) {
                            // Set green background color for opened status
//                            setTextFill(Color.GREEN);
                            setStyle("-fx-font-weight:bold;-fx-text-fill:green");
                        } else if (item.equalsIgnoreCase("closed")) {
                            // Set red background color for closed status
//                            setTextFill(Color.RED);
                            setStyle("-fx-font-weight:bold;-fx-text-fill:red");
                        }
                    }
                }
            };
        });


        //todo: checkbox creation in select column
        tblcSalesQuotationListSelect.setCellFactory(column -> new CheckBoxTableCell<>());
        tblcSalesQuotationListSelect.setCellValueFactory(cellData -> {
            SalesQuotationDTO cellValue = cellData.getValue();
            BooleanProperty property = cellValue.is_row_selectedProperty();

            // Add listener to handler change
            property.addListener((observable, oldValue, newValue) -> cellValue.setIs_row_selected(newValue));
            findOutSelectedRow();
            if (cellValue.isIs_row_selected()) {
                if (cellValue.getSales_quotation_status().equalsIgnoreCase("closed")) {
//                    AlertUtility.CustomCallback callback = (number) -> {
//                        cellValue.setIs_row_selected(false);
//                    };
//                    Stage stage = (Stage) spRootPane.getScene().getWindow();
//                    AlertUtility.AlertError(stage, AlertUtility.alertTypeError, "The Quotation is closed", callback);
                    AlertUtility.AlertErrorTimeout(AlertUtility.alertTypeError, "The Quotation is closed", in -> {
                        cellValue.setIs_row_selected(false);
                    });

                } else {
                    handleSelection(tblvSalesQuotationList.getItems());
                }
            }
            return property;
        });

        //todo: Responsive code for tableview
        responsiveTable();

        //todo: go to edit page
        tblvSalesQuotationList.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                salesQuotationEditPage();
//                GlobalController.getInstance().addTabStatic1(SALES_QUOTATION_EDIT_SLUG, false, Integer.valueOf(SalesQuotId));
            }
        });

        SalesQuotationListLogger.info("List api calling :SalesQuotationListController");
        fetchSalesQuotationInvoiceList("");

//        originalData = tblvSalesQuotationList.getItems();
        //Search without API Call in the Table
//        originalData = tblvSalesQuotationList.getItems();
        tfSalesQuotationListSearch.textProperty().addListener((observable, oldValue, newValue) -> {
            filterData(newValue.trim());
        });

        btnSalesQuotationListCreate.setOnAction(event -> {
//            SalesQuotationCreateController.focusStatus = "create";
            GlobalController.getInstance().addTabStatic(SALES_QUOTATION_CREATE_SLUG, false);
        });
        shortcutKeysSalesQuotList();
    }

    private void ResponsiveWiseCssPicker() {
        double height = Screen.getPrimary().getBounds().getHeight();
        double width = Screen.getPrimary().getBounds().getWidth();
        System.out.println("width" + width);
        if (width >= 992 && width <= 1024) {
            spRootPane.getStylesheets().add(GenivisApplication.class.getResource("ui/css/TranxCommonCssStyles/tranxCommonCssStyle1.css").toExternalForm());
        } else if (width >= 1025 && width <= 1280) {
            spRootPane.getStylesheets().add(GenivisApplication.class.getResource("ui/css/TranxCommonCssStyles/tranxCommonCssStyle2.css").toExternalForm());
        } else if (width >= 1281 && width <= 1368) {
            spRootPane.getStylesheets().add(GenivisApplication.class.getResource("ui/css/TranxCommonCssStyles/tranxCommonCssStyle3.css").toExternalForm());
        } else if (width >= 1369 && width <= 1400) {
            spRootPane.getStylesheets().add(GenivisApplication.class.getResource("ui/css/TranxCommonCssStyles/tranxCommonCssStyle4.css").toExternalForm());
        } else if (width >= 1401 && width <= 1440) {
            spRootPane.getStylesheets().add(GenivisApplication.class.getResource("ui/css/TranxCommonCssStyles/tranxCommonCssStyle5.css").toExternalForm());
        } else if (width >= 1441 && width <= 1680) {
            spRootPane.getStylesheets().add(GenivisApplication.class.getResource("ui/css/TranxCommonCssStyles/tranxCommonCssStyle6.css").toExternalForm());
        } else if (width >= 1681 && width <= 1920) {
            spRootPane.getStylesheets().add(GenivisApplication.class.getResource("ui/css/TranxCommonCssStyles/tranxCommonCssStyle7.css").toExternalForm());
        }
    }

    private void salesQuotationEditPage() {
        SalesQuotationDTO selectedItem = (SalesQuotationDTO) tblvSalesQuotationList.getSelectionModel().getSelectedItem();
        String SalesQuotId = selectedItem.getId();
        SalesQuotationCreateController.selectedRowIndex = tblvSalesQuotationList.getSelectionModel().getFocusedIndex();
        System.out.println("SalesQuotId 1 -->" + SalesQuotId);

        String status = selectedItem.getSales_quotation_status();
        // Check if the status is "opened"
        if (status != null && status.equalsIgnoreCase("opened")) {
            GlobalController.getInstance().addTabStaticWithParam(SALES_QUOTATION_EDIT_SLUG, false, Integer.valueOf(SalesQuotId));
        } else {
            // Show an alert indicating that the order is already closed
            AlertUtility.AlertErrorTimeout(AlertUtility.alertTypeError, "The Quotation is closed", in -> {
                tfSalesQuotationListSearch.requestFocus();
            });
        }
    }
    @FXML
    private void handleCtrlDPressed() {
        SalesQuotationDTO selectedItem = (SalesQuotationDTO) tblvSalesQuotationList.getSelectionModel().getSelectedItem();
        String SalesQuotId = selectedItem.getId();
        SalesQuotationCreateController.selectedRowIndex = tblvSalesQuotationList.getSelectionModel().getFocusedIndex();
        System.out.println("SalesQuotId 1 -->" + SalesQuotId);

        String status = selectedItem.getSales_quotation_status();
        // Check if the status is "opened"
        if (status != null && status.equalsIgnoreCase("opened")) {
            deleteSaleQuotation(Integer.parseInt(SalesQuotId));
        } else {
            // Show an alert indicating that the order is already closed
            AlertUtility.AlertErrorTimeout(AlertUtility.alertTypeError, "The Quotation is closed", in -> {
                tfSalesQuotationListSearch.requestFocus();
            });
        }
    }

    private void shortcutKeysSalesQuotList() {
        spRootPane.addEventFilter(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {

            @Override
            public void handle(KeyEvent event) {
                if (event.getCode() == KeyCode.N && event.isControlDown()) {
//                    AlertUtility.AlertConfirmation(AlertUtility.alertTypeConfirmation, "Are you Really want to Create Sales Quotation?", input -> {
//                        if (input == 1) {

                    GlobalController.getInstance().addTabStatic(SALES_QUOTATION_CREATE_SLUG, false);

//                        }
//                    });
                }else if (event.getCode() == KeyCode.E && event.isControlDown()) {
                    salesQuotationEditPage();
                }else if (event.getCode() == KeyCode.D && event.isControlDown()) {
                    handleCtrlDPressed();
                }
            }
        });
    }

    public void responsiveTable() {
        //todo: Responsive code for tableview
        tblcSalesQuotationListSelect.prefWidthProperty().bind(tblvSalesQuotationList.widthProperty().multiply(0.05));
        tblcSalesQuotationListSQNo.prefWidthProperty().bind(tblvSalesQuotationList.widthProperty().multiply(0.1));
        tblcSalesQuotationListSQDate.prefWidthProperty().bind(tblvSalesQuotationList.widthProperty().multiply(0.1));
        tblcSalesQuotationListClientName.prefWidthProperty().bind(tblvSalesQuotationList.widthProperty().multiply(0.25));
        tblcSalesQuotationListNarration.prefWidthProperty().bind(tblvSalesQuotationList.widthProperty().multiply(0.1));
        tblcSalesQuotationListTaxable.prefWidthProperty().bind(tblvSalesQuotationList.widthProperty().multiply(0.10));
        tblcSalesQuotationListTax.prefWidthProperty().bind(tblvSalesQuotationList.widthProperty().multiply(0.05));
        tblcSalesQuotationListBillAmount.prefWidthProperty().bind(tblvSalesQuotationList.widthProperty().multiply(0.1));
        tblcSalesQuotationListPrint.prefWidthProperty().bind(tblvSalesQuotationList.widthProperty().multiply(0.07));
        tblcSalesQuotationListTranxStatus.prefWidthProperty().bind(tblvSalesQuotationList.widthProperty().multiply(0.08));
//        tblcSalesQuotationListAction.prefWidthProperty().bind(tblvSalesQuotationList.widthProperty().multiply(0.06));

    }

    //todo: fetch Sales Quotation Invoice List with pagination
    public void fetchSalesQuotationInvoiceList(String searchKey) {
        SalesQuotationListLogger.info(" fetch Sales Quotation invoice List : SalesQuotationListController");
        try {
            APIClient apiClient = null;

            Map<String, String> body = new HashMap<>();
            body.put("pageNo", "1");
            body.put("pageSize", "50");
            body.put("searchText", searchKey);
            body.put("sort", "");
            if (!DPSalesQuotationListFromDt.getText().equalsIgnoreCase("") && !DPSalesQuotationListToDt.getText().equalsIgnoreCase("")) {
//            if(DPSalesQuotationListFromDt.getText() !="" && DPSalesQuotationListToDt.getText() != ""){

                System.out.println("date sdf " + DPSalesQuotationListFromDt.getText() + "  to  " + DPSalesQuotationListToDt.getText());
                body.put("startDate", Communicator.text_to_date.fromString(DPSalesQuotationListFromDt.getText()).toString());
                body.put("endDate", Communicator.text_to_date.fromString(DPSalesQuotationListToDt.getText()).toString());
            } else {
                body.put("startDate", "");
                body.put("endDate", "");
            }

            String JsonReqdata = Globals.mapToString(body);

            apiClient = new APIClient(EndPoints.SALESQUOTATION_SALES_QUOTATION_LIST, JsonReqdata, RequestType.JSON);
            apiClient.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
                @Override
                public void handle(WorkerStateEvent workerStateEvent) {
                    String res = workerStateEvent.getSource().getValue().toString();
                    SalesQuotationMainDTO responseBody = new Gson().fromJson(res, SalesQuotationMainDTO.class);


//            HttpResponse<String> response = APIClient.postJsonRequest(JsonReqdata, EndPoints.SALESQUOTATION_SALES_QUOTATION_LIST);
                    System.out.println("SalesQuotation" + responseBody);
                    if (responseBody.getResponseStatus() == 200) {
                        tblvSalesQuotationList.getItems().clear();
                        List<SalesQuotResObjDataDTO> list = responseBody.getResponseObject().getData();
                        if (list != null && list.size() > 0) {
                            tblvSalesQuotationList.setVisible(true);
                            for (SalesQuotResObjDataDTO element : list) {

//                        System.out.println("response dataof Q_List"+element);
                                String id = element.getId().toString();
                                String ledgerId = element.getSundryDebtorsId().toString();
                                String sales_quotation_no = element.getBillNo();
//                        String referenceNo = item.get("referenceNo").getAsString();
                                String sales_quotation_date = DateConvertUtil.convertDispDateFormat(element.getBillDate());
                                String sundry_debtors_name = element.getSundryDebtorsName();
                                String narration = element.getNarration();
                                String taxable_amt = element.getTaxableAmt().toString();
                                String tax_amt = element.getTaxAmt().toString();
                                String total_amount = element.getTotalAmount().toString();
                                String sales_quotation_status = element.getSalesQuotationStatus();
                                String tcSalesQuotListPrint = "";
                                String tcSalesQuotListAction = "";

                                salesQuotObservableList.add(new SalesQuotationDTO(id, sales_quotation_no, sales_quotation_date, sundry_debtors_name, narration, taxable_amt, tax_amt, total_amount, sales_quotation_status, ledgerId)
                                );
                            }
                            tblcSalesQuotationListSQNo.setCellValueFactory(new PropertyValueFactory<>("sales_quotation_no"));
//                    tblcPurChallLstRefNo.setCellValueFactory(new PropertyValueFactory<>("referenceNo"));
                            tblcSalesQuotationListSQDate.setCellValueFactory(new PropertyValueFactory<>("sales_quotation_date"));
                            tblcSalesQuotationListClientName.setCellValueFactory(new PropertyValueFactory<>("sundry_debtors_name"));
                            tblcSalesQuotationListNarration.setCellValueFactory(new PropertyValueFactory<>("narration"));
                            tblcSalesQuotationListTaxable.setCellValueFactory(new PropertyValueFactory<>("taxable_amt"));
                            tblcSalesQuotationListTax.setCellValueFactory(new PropertyValueFactory<>("tax_amt"));
                            tblcSalesQuotationListBillAmount.setCellValueFactory(new PropertyValueFactory<>("total_amount"));
                            tblcSalesQuotationListTranxStatus.setCellValueFactory(new PropertyValueFactory<>("sales_quotation_status"));
//                            tblcSalesQuotationListAction.setCellFactory(param -> {
//                                final TableCell<SalesQuotationDTO, String> cell = new TableCell<>() {
//                                    private ImageView delImg = new ImageView(new Image(String.valueOf(GenivisApplication.class.getResource("ui/assets/del.png"))));
//                                    private ImageView edtImg = new ImageView(new Image(String.valueOf(GenivisApplication.class.getResource("ui/assets/edt.png"))));
////                            private ImageView viewImg = new ImageView(new Image(String.valueOf(GenivisApplication.class.getResource("ui/assets/view.png"))));
//
//                                    {
//                                        delImg.setFitHeight(20.0);
//                                        delImg.setFitWidth(20.0);
//                                        edtImg.setFitHeight(20.0);
//                                        edtImg.setFitWidth(20.0);
//                                    }
//
//                                    private final Button delButton = new Button("", delImg);
//                                    private final Button edtButton = new Button("", edtImg);
//
//                                    {
//                                        edtButton.setOnAction(actionEvent -> {
//                                            Integer id = Integer.valueOf(getTableView().getItems().get(getIndex()).getId());
//
//                                            SalesQuotationDTO Id = getTableView().getItems().get(getIndex());
//                                            String status = Id.getSales_quotation_status();
//                                            // Check if the status is "opened"
//                                            if (status != null && status.equalsIgnoreCase("opened")) {
//                                                Integer id1 = Integer.valueOf(Id.getId());
//                                                GlobalController.getInstance().addTabStaticWithParam(SALES_QUOTATION_EDIT_SLUG, false, id);
//                                            } else {
//                                                // Show an alert indicating that the order is already closed
//                                                AlertUtility.CustomCallback callback = (number) -> {
//                                                    if (number == 0) {
//                                                        tfSalesQuotationListSearch.requestFocus();
//                                                    }
//                                                };
//                                                Stage stage = (Stage) spRootPane.getScene().getWindow();
//                                                AlertUtility.AlertError(stage, AlertUtility.alertTypeError, "The Quotation is closed", callback);
//                                            }
//
//                                        });
//                                    }
//
//                                    {
//                                        delButton.setOnAction(actionEvent -> {
//                                            SalesQuotationDTO id = getTableView().getItems().get(getIndex());
//                                            String status = id.getSales_quotation_status();
//                                            // Check if the status is "opened"
//                                            if (status != null && status.equalsIgnoreCase("opened")) {
//                                                Integer id1 = Integer.valueOf(id.getId());
//
//                                                deleteSaleQuotation(id1);
//                                            } else {
//                                                // Show an alert indicating that the order is already closed
//                                                AlertUtility.CustomCallback callback = (number) -> {
//                                                    if (number == 0) {
//                                                        tfSalesQuotationListSearch.requestFocus();
//                                                    }
//                                                };
//                                                Stage stage = (Stage) spRootPane.getScene().getWindow();
//                                                AlertUtility.AlertError(stage, AlertUtility.alertTypeError, "The Quotation is closed", callback);
//                                            }
////                                    GlobalController.getInstance().addTabStaticWithParam(SALES_ORDER_EDIT_SLUG, false, id);
//                                        });
//                                    }
//
//                                    HBox hbActions = new HBox();
//
//                                    {
//                                        hbActions.getChildren().add(edtButton);
////                                hbActions.getChildren().add(viewButton);
//                                        hbActions.getChildren().add(delButton);
//                                        hbActions.setSpacing(10.0);
//                                    }
//
//                                    // Set the action for the view button
//                                    @Override
//                                    protected void updateItem(String item, boolean empty) {
//                                        super.updateItem(item, empty);
//                                        if (empty) {
//                                            setGraphic(null);
//                                        } else {
//                                            setGraphic(hbActions);
//                                        }
//                                    }
//                                };
//                                return cell;
//                            });
//                            tblcSalesQuotationListPrint.setCellValueFactory(new PropertyValueFactory<>("tblcSalesQuotationListPrint"));
                            tblcSalesQuotationListPrint.setCellFactory(param -> {
                                final TableCell<SalesQuotationDTO, String> cell = new TableCell<>() {
                                    private ImageView printImg = new ImageView(new Image(String.valueOf(GenivisApplication.class.getResource("ui/assets/print.png"))));
//                            private ImageView viewImg = new ImageView(new Image(String.valueOf(GenivisApplication.class.getResource("ui/assets/view.png"))));
                                    {
                                        printImg.setFitHeight(20.0);
                                        printImg.setFitWidth(20.0);
                                    }
                                    private final Button printButton = new Button("", printImg);
//                            private final Button viewButton = new Button("", viewImg);
                                    {
                                        printButton.setOnAction(actionEvent -> {
                                            SalesQuotationDTO id = getTableView().getItems().get(getIndex());
                                            String status = id.getSales_quotation_status();
                                            // Check if the status is "opened"
                                            if (status != null) {
                                                id1 = Integer.valueOf(id.getId());
//                                                printSaleQuotation(id1);
                                            } else {
                                                // Show an alert indicating that the order is already closed
                                                AlertUtility.CustomCallback callback = (number) -> {
                                                    if (number == 0) {
                                                        tfSalesQuotationListSearch.requestFocus();
                                                    }
                                                };
                                            }
//                                    GlobalController.getInstance().addTabStaticWithParam(SALES_ORDER_EDIT_SLUG, false, id);
                                        });
                                    }
                                    HBox hbActions = new HBox();
                                    {
//                                        hbActions.getChildren().add(edtButton);
//                                hbActions.getChildren().add(viewButton);
                                        hbActions.getChildren().add(printButton);
                                        hbActions.setSpacing(10.0);
                                    }

                                    // Set the action for the view button
                                    @Override
                                    protected void updateItem(String item, boolean empty) {
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
                            ListSize = salesQuotObservableList.size();
                            System.out.println("salesQuotObservableList " + ListSize);

                            Platform.runLater(() -> {
                                if(focusStatus!=null){
                                    if (focusStatus.equalsIgnoreCase("create")) {
                                        System.out.println("listSize " + ListSize + " last index " + (ListSize - 1));
                                        tblvSalesQuotationList.getSelectionModel().clearAndSelect(0);
                                        tblvSalesQuotationList.requestFocus();
                                        tblvSalesQuotationList.scrollTo(0);
                                    } else if (focusStatus.equalsIgnoreCase("edit") && selectedRowIndex != null) {
                                        System.out.println("selected Row Index " + selectedRowIndex);
                                        tblvSalesQuotationList.getSelectionModel().clearAndSelect(selectedRowIndex);
                                        tblvSalesQuotationList.requestFocus();
                                        tblvSalesQuotationList.scrollTo(selectedRowIndex);
                                    } else {
                                        System.out.println("not create");
                                        tfSalesQuotationListSearch.requestFocus();
                                    }
                                }

                            }
                            );
                            tblvSalesQuotationList.setItems(salesQuotObservableList);
                            originalData = salesQuotObservableList;
                            SalesQuotationListLogger.info("Success in Displaying Sales Quotation List : SalesQuotationListController");

                        } else {
                            SalesQuotationListLogger.debug("Sales Quotation invoice List ResponseObject is null : SalesQuotationListController");
                            tblvSalesQuotationList.getItems().clear();
                        }
                    }

                }
            });
            apiClient.setOnCancelled(new EventHandler<WorkerStateEvent>() {
                @Override
                public void handle(WorkerStateEvent workerStateEvent) {
                    SalesQuotationListLogger.debug("Sales Quotation List setOnCancelled Error ");
                    System.out.println("Api Client set on cancelled ");
                    tblvSalesQuotationList.getItems().clear();
                }
            });
            apiClient.setOnFailed(new EventHandler<WorkerStateEvent>() {
                @Override
                public void handle(WorkerStateEvent workerStateEvent) {
                    SalesQuotationListLogger.debug("Sales Quotation List setOnFailed Error ");
                    System.out.println("Api Client setOnFailed");
                    tblvSalesQuotationList.getItems().clear();
                }
            });
            apiClient.start();

        } catch (Exception e) {
            SalesQuotationListLogger.error("Sales Quotation List Catch Error  is " + e.getMessage());
            System.out.println("Api Client catch error");
            e.printStackTrace();
            tblvSalesQuotationList.getItems().clear();
        }
    }
    //? Print API Function
//    public void printSaleQuotation(int id) {
//        try {
//            AlertUtility.CustomCallback callback = number -> {
//                if (number == 1) {
//                    APIClient apiClient = null;
//                    try {
//                        logger.debug("Print Sales Order Data Started...");
//                        Map<String, String> map = new HashMap<>();
//                        map.put("id", String.valueOf(id));
//                        map.put("print_type", "list");
//                        map.put("source", "sales_order");
//                        String formData = Globals.mapToStringforFormData(map);
////                    HttpResponse<String> response = APIClient.postFormDataRequest(formData, EndPoints.SALES_ORDER_DELETE_ENDPOINT);
//                        apiClient = new APIClient(EndPoints.SALES_ORDER_PRINT_ENDPOINT, formData, RequestType.FORM_DATA);
//
//                        apiClient.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
//                            @Override
//                            public void handle(WorkerStateEvent workerStateEvent) {
//                                jsonObject = new Gson().fromJson(workerStateEvent.getSource().getValue().toString(), JsonObject.class);
//
////                            responseBody = response.body();
////                            JsonObject jsonObject = new Gson().fromJson(responseBody, JsonObject.class);
//                                message = jsonObject.get("message").getAsString();
//                                System.out.println("i am in Print" + jsonObject);
//                                if (jsonObject.get("responseStatus").getAsInt() == 200) {
//
//                                    Stage primaryStage = (Stage) spRootSalesOrdeListPane.getScene().getWindow();
//                                    primaryStage.setTitle("Print HTML Example");
//
//                                    // Call the handlePrintAction method to load the content and print
//                                    handlePrintAction(primaryStage, jsonObject);
////                                AlertUtility.AlertSuccessTimeout(AlertUtility.alertTypeSuccess, "Coming Soon...", input -> {
////                                });
//                                } else {
//                                    AlertUtility.AlertErrorTimeout(AlertUtility.alertTypeError, message, in -> {
//                                    });
//                                }
//                            }
//                        });
//                        apiClient.setOnCancelled(new EventHandler<WorkerStateEvent>() {
//                            @Override
//                            public void handle(WorkerStateEvent workerStateEvent) {
//                                logger.error("Network API cancelled in printSaleQuotation()" + workerStateEvent.getSource().getValue().toString());
//
//                            }
//                        });
//
//                        apiClient.setOnFailed(new EventHandler<WorkerStateEvent>() {
//                            @Override
//                            public void handle(WorkerStateEvent workerStateEvent) {
//                                logger.error("Network API failed in printSaleQuotation()" + workerStateEvent.getSource().getValue().toString());
//                            }
//                        });
//                        apiClient.start();
//                        logger.debug("Pint Sales Order Data End...");
//
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                } else {
//                    logger.error("\"Failed to Print: printSaleQuotation()");
//                }
//            };
//            AlertUtility.AlertConfirmation(AlertUtility.alertTypeConfirmation, "Do you want to Print ?", callback);
////        Globals.salesOrderListDTO=null;
////        GlobalController.getInstance().addTabStatic(FRANCHISE_LIST_SLUG,false);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//    }

    //todo: fetch All Sales Quotation Invoice List
    public void fetchAllSalesQuotationInvoiceList(String searchKey) {
        SalesQuotationListLogger.info(" fetch Sales Quotation invoice List : SalesQuotationListController");
        try {

            Map<String, String> body = new HashMap<>();

            if (!DPSalesQuotationListFromDt.getText().equalsIgnoreCase("") && !DPSalesQuotationListToDt.getText().equalsIgnoreCase("")) {
                System.out.println("date sdf " + DPSalesQuotationListFromDt.getText() + "  to  " + DPSalesQuotationListToDt.getText());
                body.put("startDate", Communicator.text_to_date.fromString(DPSalesQuotationListFromDt.getText()).toString());
                body.put("endDate", Communicator.text_to_date.fromString(DPSalesQuotationListToDt.getText()).toString());
            }

            String requestBody = Globals.mapToStringforFormData(body);
            System.out.println("requestBody of list " + requestBody);
            HttpResponse<String> response = APIClient.postFormDataRequest(requestBody, EndPoints.ALL_SALES_QUOTATION_LIST);
            System.out.println("SalesQuotation" + response.body());
            JsonObject jsonObject = new Gson().fromJson(response.body(), JsonObject.class);
//            System.out.println("111Q" +jsonObject);
            ObservableList<SalesQuotationDTO> observableList = FXCollections.observableArrayList();
            if (jsonObject.get("responseStatus").getAsInt() == 200) {
//                JsonObject responseObject = jsonObject.get("data").getAsJsonObject();
                JsonArray responseArray = jsonObject.get("data").getAsJsonArray();

                if (responseArray.size() > 0) {
                    tblvSalesQuotationList.setVisible(true);
                    for (JsonElement element : responseArray) {
                        JsonObject item = element.getAsJsonObject();
                        String id = item.get("id").getAsString();
                        String sales_quotation_no = item.get("bill_no") != null ? item.get("bill_no").getAsString() : "";
//                        String referenceNo = item.get("referenceNo").getAsString();
                        String ledgerId = item.get("sundry_debtor_id").getAsString();
                        String sales_quotation_date = item.get("bill_date").getAsString();
                        String sundry_debtors_name = item.get("sundry_debtors_name").getAsString();
                        String narration = item.get("narration") != null ? item.get("narration").getAsString() : "";
                        String taxable_amt = item.get("taxable_amt") != null ? item.get("taxable_amt").getAsString() : "";
                        String tax_amt = item.get("tax_amt") != null ? item.get("tax_amt").getAsString() : "";
                        String total_amount = item.get("total_amount").getAsString();
                        String sales_quotation_status = item.get("sales_quotation_status").getAsString();

                        observableList.add(new SalesQuotationDTO(id, sales_quotation_no, sales_quotation_date, sundry_debtors_name, narration, taxable_amt, tax_amt, total_amount, sales_quotation_status, ledgerId)
                        );
                    }
                    tblcSalesQuotationListSQNo.setCellValueFactory(new PropertyValueFactory<>("sales_quotation_no"));
//                    tblcPurChallLstRefNo.setCellValueFactory(new PropertyValueFactory<>("referenceNo"));
                    tblcSalesQuotationListSQDate.setCellValueFactory(new PropertyValueFactory<>("sales_quotation_date"));
                    tblcSalesQuotationListClientName.setCellValueFactory(new PropertyValueFactory<>("sundry_debtors_name"));
                    tblcSalesQuotationListNarration.setCellValueFactory(new PropertyValueFactory<>("narration"));
                    tblcSalesQuotationListTaxable.setCellValueFactory(new PropertyValueFactory<>("taxable_amt"));
                    tblcSalesQuotationListTax.setCellValueFactory(new PropertyValueFactory<>("tax_amt"));
                    tblcSalesQuotationListBillAmount.setCellValueFactory(new PropertyValueFactory<>("total_amount"));
                    tblcSalesQuotationListTranxStatus.setCellValueFactory(new PropertyValueFactory<>("sales_quotation_status"));

                    tblvSalesQuotationList.setItems(observableList);
                    originalData = observableList;
                    SalesQuotationListLogger.info("Success in Displaying Sales Quotation List : SalesQuotationListController");

                } else {

                    SalesQuotationListLogger.debug("Sales Quotation invoice List ResponseObject is null : SalesQuotationListController");
                    tblvSalesQuotationList.getItems().clear();
                }
            } else {
                SalesQuotationListLogger.debug("Error in response of Sales Quotation invoice List : SalesQuotationListController");
                tblvSalesQuotationList.getItems().clear();
            }
        } catch (Exception e) {
            SalesQuotationListLogger.error("Sales Quotation List Error is " + e.getMessage());
            tblvSalesQuotationList.getItems().clear();
            e.printStackTrace();
        }
    }

    //Search Function to Search in the Table
    private void filterData(String keyword) {
        ObservableList<SalesQuotationDTO> filteredData = FXCollections.observableArrayList();
        SalesQuotationListLogger.info("Search Sales Quotation invoice in SalesQuotationListController");
        if (keyword.isEmpty()) {
            tblvSalesQuotationList.setItems(originalData);
            return;
        }

        for (SalesQuotationDTO item : originalData) {
            if (matchesKeyword(item, keyword)) {
                filteredData.add(item);
            }
        }

        tblvSalesQuotationList.setItems(filteredData);
    }

    //Search Function to Search in the Table for columns
    private boolean matchesKeyword(SalesQuotationDTO item, String keyword) {
        String lowerCaseKeyword = keyword.toLowerCase();

        // Check if any of the columns contain the keyword
        return item.getSundry_debtors_name().toLowerCase().contains(lowerCaseKeyword) ||
                item.getSales_quotation_no().toLowerCase().contains(lowerCaseKeyword) ||
                item.getSales_quotation_date().toLowerCase().contains(lowerCaseKeyword) ||
                item.getTax_amt().toLowerCase().contains(lowerCaseKeyword) ||
                item.getTaxable_amt().toLowerCase().contains(lowerCaseKeyword) ||
                item.getTotal_amount().toLowerCase().contains(lowerCaseKeyword) ||
                item.getNarration().toLowerCase().contains(lowerCaseKeyword);

    }

    private void findOutSelectedRow() {
        Boolean canShowButton = false;
        ObservableList<SalesQuotationDTO> list = tblvSalesQuotationList.getItems();
        for (SalesQuotationDTO object : list) {
            if (object.isIs_row_selected()) {
                canShowButton = true;
//                for(int i= 0;i<Selected_index)
                Selected_index = Integer.valueOf(object.getId());
//                if(object.getSundry_debtors_name().toString())
                System.out.println("Selected Index " + Selected_index);
            }
        }
        if (canShowButton) {
            btnSalesQuotationListCreate.setVisible(false);
            btnSalesQuotationListToOrder.setVisible(true);
            btnSalesQuotationListToChallan.setVisible(true);
            btnSalesQuotationListToInvoice.setVisible(true);
        } else {
            btnSalesQuotationListCreate.setVisible(true);
            btnSalesQuotationListToOrder.setVisible(false);
            btnSalesQuotationListToChallan.setVisible(false);
            btnSalesQuotationListToInvoice.setVisible(false);
        }

    }

    public void deleteSaleQuotation(int id) {
        AlertUtility.CustomCallback callback = number -> {
            if (number == 1) {
                try {
                    Map<String, String> map = new HashMap<>();
                    map.put("id", String.valueOf(id));
                    String formData = Globals.mapToStringforFormData(map);
                    HttpResponse<String> response = APIClient.postFormDataRequest(formData, EndPoints.SALES_QUOTATION_DELETE_ENDPOINT);
                    responseBody = response.body();
                    JsonObject jsonObject = new Gson().fromJson(responseBody, JsonObject.class);
                    message = jsonObject.get("message").getAsString();
                    System.out.println("Delete->" + jsonObject.get("message"));
//                    AlertUtility.CustomCallback callback1 = (number1) -> {
//                        if (number1 == 1) {
//                            // Update the list after successful deletion
//                            salesQuotObservableList.removeIf(item -> item.getId().equals(String.valueOf(id)));
//                            tblvSalesQuotationList.setItems(salesQuotObservableList);
//                        }
//                    };
//                    Stage stage2 = (Stage) spRootPane.getScene().getWindow();
//                    if (jsonObject.get("responseStatus").getAsInt() == 200) {
//                        AlertUtility.AlertSuccess(stage2, "Success", message, callback1);
//                    } else {
//                        AlertUtility.AlertError(stage2, AlertUtility.alertTypeError, message, callback1);
//                    }
                    if (jsonObject.get("responseStatus").getAsInt() == 200) {
                        AlertUtility.AlertSuccessTimeout(AlertUtility.alertTypeSuccess, message, input -> {
                            // Update the list after successful deletion
                            salesQuotObservableList.removeIf(item -> item.getId().equals(String.valueOf(id)));
                            tblvSalesQuotationList.setItems(salesQuotObservableList);
                        });
                    } else {
                        AlertUtility.AlertErrorTimeout(AlertUtility.alertTypeError, message, in -> {
                        });
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                System.out.println("working!");
            }
        };
        AlertUtility.AlertConfirmation(AlertUtility.alertTypeConfirmation, "Do you want to Delete", callback);

    }


    //todo: On Clicking on the Cancel Button From Create or Edit page redirect Back to List page
    public void goToOrder() {

        ObservableList<SalesQuotationDTO> list = tblvSalesQuotationList.getItems();
        for (SalesQuotationDTO object : list) {
            if (object.isIs_row_selected()) {
                if (object.getSales_quotation_status().equalsIgnoreCase("closed")) {
//                    AlertUtility.CustomCallback callback = (number) -> {
//                        if (number == 0) {
//                            tfSalesQuotationListSearch.requestFocus();
//                        }
//                    };
//                    Stage stage = (Stage) spRootPane.getScene().getWindow();
//                    AlertUtility.AlertError(stage, AlertUtility.alertTypeError, "The Quotation is closed", callback);
                    AlertUtility.AlertErrorTimeout(AlertUtility.alertTypeError, "The Quotation is closed", in -> {
                        tfSalesQuotationListSearch.requestFocus();
                    });
                    System.out.println("cellValue.getId() " + object.getId() + " cellValue.getSales_quotation_status()  " + object.getSales_quotation_status());
                } else {
                    System.out.println("sales quotation to order Selected_index " + Selected_index);
                    GlobalController.getInstance().addTabStatic(SALES_QUOTATION_TO_ORDER_SLUG, false);
                }

            }
        }

    }

    public void goToChallan() {
        System.out.println(SalesQuotationToChallan.input);
        ObservableList<SalesQuotationDTO> list = tblvSalesQuotationList.getItems();
        for (SalesQuotationDTO object : list) {
            if (object.isIs_row_selected()) {
                if (object.getSales_quotation_status().equalsIgnoreCase("closed")) {

                    AlertUtility.AlertErrorTimeout(AlertUtility.alertTypeError, "The Quotation is closed", in -> {
                        tfSalesQuotationListSearch.requestFocus();
                    });
//                    System.out.println("cellValue.getId() " + object.getId() + " cellValue.getSales_quotation_status()  " + object.getSales_quotation_status());
                } else {
                    System.out.println("sales quotation to order " + Selected_index);
                    GlobalController.getInstance().addTabStatic(SALES_QUOTATION_TO_CHALLAN_SLUG, false);
                }

            }
        }
    }

    private void handleSelection(ObservableList<SalesQuotationDTO> items) {
        String errorMessage = "Error: Different Ledger selected!";
        String jsonFormat = " [%s] ";   //"selectedIds":

        StringJoiner selectedIds = new StringJoiner(",");
        JsonObject obj = new JsonObject();
        JsonArray array = new JsonArray();
        String lastLedgerName = null;

        for (SalesQuotationDTO item : items) {
            if (item.isIs_row_selected()) {
                if (lastLedgerName == null) {
                    lastLedgerName = item.getSundry_debtors_name();
                } else if (!lastLedgerName.equals(item.getSundry_debtors_name())) {
                    // Ledger names are different
                    System.out.println("error mes");
//                    showError(errorMessage);
                    AlertUtility.AlertErrorTimeout(AlertUtility.alertTypeError, errorMessage , input->{
                        item.setIs_row_selected(false);
                    });
                    return;
                }
                obj.addProperty("id", item.getId());
                selectedIds.add(String.valueOf(obj));
            }
        }
        array.add(selectedIds.toString());
        System.out.println("obj " + array);
        // Ledger names are the same, show JSON format
        String jsonResult = "";
        if (!selectedIds.toString().isEmpty()) {
            jsonResult = String.format(jsonFormat, selectedIds);
            System.out.println("jsonResult1>>>>>> " + selectedIds);
            System.out.println("jsonResult>>>>>> " + jsonResult);
        }
        SalesQuotToOrderController.QuotaToOrderId = jsonResult;
        SalesQuotationToChallan.QuotToChallanId = jsonResult;
        SalesQuotationToChallan.input = jsonResult;

    }


//    private void showError(String errorMessage) {
//        AlertUtility.CustomCallback callback = (number) -> {
//            if (number == 0) {
//                tfSalesQuotationListSearch.requestFocus();
//            }
//        };
//        Stage stage = (Stage) spRootPane.getScene().getWindow();
//        AlertUtility.AlertError(stage, AlertUtility.alertTypeError, errorMessage, callback);
//
//    }


}
