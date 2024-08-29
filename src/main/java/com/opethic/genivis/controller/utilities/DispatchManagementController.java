package com.opethic.genivis.controller.utilities;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.opethic.genivis.dto.utilities.*;
import com.opethic.genivis.models.tranx.sales.TranxConversionTbl;
import com.opethic.genivis.network.APIClient;
import com.opethic.genivis.network.EndPoints;
import com.opethic.genivis.network.RequestType;
import com.opethic.genivis.utils.AlertUtility;
import com.opethic.genivis.utils.Globals;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import static com.opethic.genivis.network.EndPoints.DISPATCH_MANAGEMENT_ReadyFDSales_STATUS_UPDATE;

public class DispatchManagementController implements Initializable {

    private static final Logger DispatchManagementListLogger = LoggerFactory.getLogger(DispatchManagementController.class);

    @FXML
    private Button submitStatus, cancelStatus;
    @FXML
    private RadioButton rbDelivered, rbReturned;
    private final ToggleGroup tgStatus = new ToggleGroup();
    int allListCount = 0, pickupListCount = 0, packingListCount = 0, readyFDListCount = 0, deliveredListCount = 0, returedListCount = 0;

    @FXML
    private Label lblAllNo, lblAllTotalQty, lblAllTotalPrice, lblPickupNo, lblPickupTotalQty, lblPickupTotalPrice, lblPackingNo, lblPackingTotalQty, lblPackingTotalPrice,
            lblReadyFDNo, lblReadyFDTotalQty, lblReadyFDTotalPrice, lblDeliveredNo, lblDeliveredTotalQty, lblDeliveredTotalPrice, lblReturnedNo,
            lblReturnedTotalQty, lblReturnedTotalPrice;

    //    All Start
    @FXML
    private TableView<DispatchMngtAllSalesDTO> tblvAllSalesList;
    @FXML
    TableColumn<DispatchMngtAllSalesDTO, Boolean> tblvAllSalesSelect;
    @FXML
    private TableColumn<DispatchMngtAllSalesDTO, String> tblvAllSalesInvoiceNo, tblvAllSalesInvoiceDate,
            tblvAllSalesCustomer, tblvAllSalesNarration, tblvAllSalesStatus, tblvAllSalesBillAmount, tblvAllSalesItem;
    @FXML
    private TextField tfAllSalesListSearch;
    private ObservableList<DispatchMngtAllSalesDTO> originalDataAll;

    // Details
    @FXML
    private TableView<DispatchMngtAllSalesDetailsDTO> tblvAllSalesListDetails;
    @FXML
    private TableColumn<DispatchMngtAllSalesDetailsDTO, String> tblAllSalesListDetailSrNo, tblAllSalesListDetailDescription,
            tblAllSalesListDetailBatch, tblAllSalesListDetailQty, tblAllSalesListDetailPrice;
    private ObservableList<DispatchMngtAllSalesDetailsDTO> originalDataAllDetails;
    //    All End

    //    Pickup Start
    @FXML
    private TableView<DispatchMngtPickupSalesOrdersDTO> tblvPickupSalesOrdersList;
    @FXML
    TableColumn<DispatchMngtPickupSalesOrdersDTO, Boolean> tblPickupSalesOrdersSelect;
    @FXML
    private TableColumn<DispatchMngtPickupSalesOrdersDTO, String> tblPickupSalesOrdersInvoiceNo, tblPickupSalesOrdersInvoiceDate,
            tblPickupSalesOrdersCustomer, tblPickupSalesOrdersNarration, tblPickupSalesOrdersStatus, tblPickupSalesOrdersBillAmount, tblPickupSalesOrdersItem;
    @FXML
    private TextField tfPickupSalesOrdersListSearch;
    private ObservableList<DispatchMngtPickupSalesOrdersDTO> originalDataPickup;

    // Details
    @FXML
    private TableView<DispatchMngtPickupSalesOrdersDetailsDTO> tblvPickupSalesOrdersDetails;
    @FXML
    private TableColumn<DispatchMngtPickupSalesOrdersDetailsDTO, String> tblPickupSalesOrdersDetailSrNo, tblPickupSalesOrdersDetailDescription,
            tblPickupSalesOrdersDetailBatch, tblPickupSalesOrdersDetailQty, tblPickupSalesOrdersDetailPrice;
    private ObservableList<DispatchMngtPickupSalesOrdersDetailsDTO> originalDataPickupDetails;
    //    Pickup End


    // Packing Start
    @FXML
    private TableView<DispatchMngtPackingSalesChallanDTO> tblvPackingSalesChallanList;
    @FXML
    TableColumn<DispatchMngtPackingSalesChallanDTO, Boolean> tblPackingSalesChallanSelect;
    @FXML
    private TableColumn<DispatchMngtPackingSalesChallanDTO, String> tblPackingSalesChallanInvoiceNo, tblPackingSalesChallanInvoiceDate, tblPackingSalesChallanCustomer,
            tblPackingSalesChallanNarration, tblPackingSalesChallanStatus, tblPackingSalesChallanBillAmount, tblPackingSalesChallanItem;
    @FXML
    private TextField tfPackingSalesChallanListSearch;
    private ObservableList<DispatchMngtPackingSalesChallanDTO> originalDataPacking;

    // Details
    @FXML
    private TableView<DispatchMngtPackingSalesChallanDetailsDTO> tblvPackingSalesChallanDetails;
    @FXML
    private TableColumn<DispatchMngtPackingSalesChallanDetailsDTO, String> tblPackingSalesChallanDetailSrNo, tblPackingSalesChallanDetailDescription,
            tblPackingSalesChallanDetailBatch, tblPackingSalesChallanDetailQty, tblPackingSalesChallanDetailPrice;
    private ObservableList<DispatchMngtPackingSalesChallanDetailsDTO> originalDataPackingDetails;
    // Packing end


    // ReadyFD Start
    String selectedValue = "";
    String Selected_status = "";
    Map<String, String> map = new HashMap<>(); // update status
    @FXML
    private TableView<DispatchMngtReadyFDSalesInvoiceDTO> tblvReadyFDSalesInvoiceList;
    @FXML
    TableColumn<DispatchMngtReadyFDSalesInvoiceDTO, Boolean> tblReadyFDSalesInvoiceSelect;
    @FXML
    private TableColumn<DispatchMngtReadyFDSalesInvoiceDTO, String> tblReadyFDSalesInvoiceInvoiceNo, tblReadyFDSalesInvoiceInvoiceDate, tblReadyFDSalesInvoiceCustomer,
            tblReadyFDSalesInvoiceNarration, tblReadyFDSalesInvoiceStatus, tblReadyFDSalesInvoiceBillAmount, tblReadyFDSalesInvoiceItem;
    @FXML
    private TextField tfReadyFDSalesInvoiceListSearch;
    private ObservableList<DispatchMngtReadyFDSalesInvoiceDTO> originalDataReadyFD;

    // Details
    @FXML
    private TableView<DispatchMngtReadyFDSalesInvoiceDetailsDTO> tblvReadyFDSalesInvoiceDetails;
    @FXML
    private TableColumn<DispatchMngtReadyFDSalesInvoiceDetailsDTO, String> tblReadyFDSalesInvoiceSrNo, tblReadyFDSalesInvoiceDescription,
            tblReadyFDSalesInvoiceBatch, tblReadyFDSalesInvoiceQty, tblReadyFDSalesInvoicePrice;
    private ObservableList<DispatchMngtReadyFDSalesInvoiceDetailsDTO> originalDataReadyFDDetails;
    // ReadyFD end


    // Delivered Start
    @FXML
    private TableView<DispatchMngtDeliveredSalesInvoiceDTO> tblvDeliveredSalesInvoiceList;
    @FXML
    TableColumn<DispatchMngtDeliveredSalesInvoiceDTO, Boolean> tblDeliveredSalesInvoiceSelect;
    @FXML
    private TableColumn<DispatchMngtDeliveredSalesInvoiceDTO, String> tblDeliveredSalesInvoiceInvoiceNo, tblDeliveredSalesInvoiceInvoiceDate, tblDeliveredSalesInvoiceCustomer,
            tblDeliveredSalesInvoiceNarration, tblDeliveredSalesInvoiceStatus, tblDeliveredSalesInvoiceBillAmount, tblDeliveredSalesInvoiceItem;
    @FXML
    private TextField tfDeliveredSalesInvoiceListSearch;
    private ObservableList<DispatchMngtDeliveredSalesInvoiceDTO> originalDataDelivered;

    // Details
    @FXML
    private TableView<DispatchMngtDeliveredSalesInvoiceDetailsDTO> tblvDeliveredSalesInvoiceDetails;
    @FXML
    private TableColumn<DispatchMngtDeliveredSalesInvoiceDetailsDTO, String> tblDeliveredSalesInvoiceSrNo, tblDeliveredSalesInvoiceDescription,
            tblDeliveredSalesInvoiceBatch, tblDeliveredSalesInvoiceQty, tblDeliveredSalesInvoicePrice;
    private ObservableList<DispatchMngtDeliveredSalesInvoiceDetailsDTO> originalDataDeliveredDetails;
    // Delivered end


    // Returned Start
    @FXML
    private TableView<DispatchMngtReturnedSalesInvoiceDTO> tblvReturnedSalesInvoiceList;
    @FXML
    TableColumn<DispatchMngtReturnedSalesInvoiceDTO, Boolean> tblReturnedSalesInvoiceSelect;
    @FXML
    private TableColumn<DispatchMngtReturnedSalesInvoiceDTO, String> tblReturnedSalesInvoiceInvoiceNo, tblReturnedSalesInvoiceInvoiceDate, tblReturnedSalesInvoiceCustomer,
            tblReturnedSalesInvoiceNarration, tblReturnedSalesInvoiceStatus, tblReturnedSalesInvoiceBillAmount, tblReturnedSalesInvoiceItem;
    @FXML
    private TextField tfReturnedSalesInvoiceListSearch;
    private ObservableList<DispatchMngtReturnedSalesInvoiceDTO> originalDataReturned;

    // Details
    @FXML
    private TableView<DispatchMngtReturnedSalesInvoiceDetailsDTO> tblvReturnedSalesInvoiceDetails;
    @FXML
    private TableColumn<DispatchMngtReturnedSalesInvoiceDetailsDTO, String> tblvReturnedSalesInvoiceSrNo, tblvReturnedSalesInvoiceDescription,
            tblvReturnedSalesInvoiceBatch, tblvReturnedSalesInvoiceQty, tblvReturnedSalesInvoicePrice;
    private ObservableList<DispatchMngtReturnedSalesInvoiceDetailsDTO> originalDataReturnedDetails;
    // Returned end


    private JsonObject jsonObject = null;
    @FXML
    private Tab tbsAll, tbsPickup, tbsPacking, tbsReadyFD, tbsDelivered, tbsRtnCnl;
    Long Selected_index = 0L;
    String status = "";
//    String Selected_invoice = "";

    Boolean isRowSelected = false;

    @FXML
    private TabPane mainTabPane;

    /**
     * @ImplNote : Initialize the dispatch management
     * @author: prathemesh.yemul
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        mainTabPane.getTabs().remove(tbsReadyFD);
        mainTabPane.getTabs().remove(tbsDelivered);

        DispatchManagementListLogger.info("Start of Initialize method of : DispatchManagementController");
        Platform.runLater(() -> tfAllSalesListSearch.requestFocus());

        mainTabPane.addEventFilter(KeyEvent.KEY_PRESSED, (KeyEvent event) -> {
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

        // All Tab Checkbox
        initAllCheckboxTab();

        // Pickup Tab Checkbox
        initPickupCheckboxTab();

        // Packing Tab Checkbox
        initPackingCheckboxTab();

        // ReadyFD Tab Checkbox
        initReadyFDCheckboxTab();

        // Delivered Tab Checkbox
        initDeliveredCheckboxTab();

        // Returned Tab Checkbox
        initReturnedCheckboxTab();

    }



    /**
     * @ImplNote : Initalize the first tab of dispatchment
     * @author: prathemesh.yemul
     */

    // All Tab Checkbox
    private void initAllCheckboxTab() {
        tblvAllSalesList.setEditable(true);
//        todo: checkbox pickup select checkbox
        tblvAllSalesSelect.setCellFactory(column -> new CheckBoxTableCell<>());
        tblvAllSalesSelect.setCellValueFactory(cellData -> {
            DispatchMngtAllSalesDTO cellValue = cellData.getValue();
            BooleanProperty property = cellValue.is_row_selectedProperty();
            ObservableList<DispatchMngtAllSalesDTO> list = tblvAllSalesList.getItems();

            for (DispatchMngtAllSalesDTO object : list) {
                if (object.isIs_row_selected()) {
                    Selected_index = Long.valueOf(object.getOrder_id());
                    System.out.println("Selected Index Pickup--> "+ Selected_index);
                    handleAllCheckbox(Selected_index);
                    String Selected_invoice = String.valueOf(object.getInvoice_no());
                    Selected_status = String.valueOf(object.getOrder_status());
                    lblAllNo.setText(Selected_invoice);
                } else {
                    tblvAllSalesListDetails.getItems().clear();
                    calculateAllTotalAmount();
//                    lblAllNo.setText("");
                }
            }
            return property;
        });
    }

    // All Pickup Checkbox
    private void initPickupCheckboxTab() {
        tblvPickupSalesOrdersList.setEditable(true);
//        todo: checkbox pickup select checkbox
        tblPickupSalesOrdersSelect.setCellFactory(column -> new CheckBoxTableCell<>());
        tblPickupSalesOrdersSelect.setCellValueFactory(cellData -> {
            DispatchMngtPickupSalesOrdersDTO cellValue = cellData.getValue();
            BooleanProperty property = cellValue.is_row_selectedProperty();
            ObservableList<DispatchMngtPickupSalesOrdersDTO> list = tblvPickupSalesOrdersList.getItems();

            for (DispatchMngtPickupSalesOrdersDTO object : list) {
                if (object.isIs_row_selected()) {
                    Selected_index = Long.valueOf(object.getOrder_id());
                   System.out.println("Selected Index Pickup--> "+ Selected_index);
                    handlePickupCheckbox(Selected_index);
                    String Selected_invoice = String.valueOf(object.getInvoice_no());
//                    System.out.println("Selected Invoice Pickup--> "+ Selected_invoice);
                    lblPickupNo.setText(Selected_invoice);
                } else {
                    tblvPickupSalesOrdersDetails.getItems().clear();
                    calculatePickupTotalAmount();
//                    lblPickupNo.setText("");
                }
            }
            return property;
        });
    }

    // All Packing Checkbox
    private void initPackingCheckboxTab() {
        tblvPackingSalesChallanList.setEditable(true);
//        todo: checkbox packing select checkbox
        tblPackingSalesChallanSelect.setCellFactory(column -> new CheckBoxTableCell<>());
        tblPackingSalesChallanSelect.setCellValueFactory(cellData -> {
            DispatchMngtPackingSalesChallanDTO cellValue = cellData.getValue();
            BooleanProperty property = cellValue.is_row_selectedProperty();
            ObservableList<DispatchMngtPackingSalesChallanDTO> list = tblvPackingSalesChallanList.getItems();

            for (DispatchMngtPackingSalesChallanDTO object : list) {
                if (object.isIs_row_selected()) {
                    Selected_index = Long.valueOf(object.getOrder_id());
//                    System.out.println("Selected Index "+ Selected_index);
                    handlePackingCheckbox(Selected_index);
                    String Selected_invoice = String.valueOf(object.getInvoice_no());
//                    System.out.println("Selected Invoice Pickup--> "+ Selected_invoice);
                    lblPackingNo.setText(Selected_invoice);
                } else {
                    tblvPackingSalesChallanDetails.getItems().clear();
                    calculatePackingTotalAmount();
//                    lblPackingNo.setText("");
                }
            }
            return property;
        });
    }

    // ReadyFD Tab Checkbox
    private void initReadyFDCheckboxTab() {
        tblvReadyFDSalesInvoiceList.setEditable(true);
//        todo: checkbox packing select checkbox
        tblReadyFDSalesInvoiceSelect.setCellFactory(column -> new CheckBoxTableCell<>());

        tblReadyFDSalesInvoiceSelect.setCellValueFactory(cellData -> {
            DispatchMngtReadyFDSalesInvoiceDTO cellValue = cellData.getValue();
            BooleanProperty property = cellValue.is_row_selectedProperty();
            ObservableList<DispatchMngtReadyFDSalesInvoiceDTO> list = tblvReadyFDSalesInvoiceList.getItems();

            for (DispatchMngtReadyFDSalesInvoiceDTO object : list) {
                if (object.isIs_row_selected()) {
                    Selected_index = Long.valueOf(object.getOrder_id());
                    System.out.println("Selected Index " + Selected_index);
                    handleReadyFDCheckbox(Selected_index);
                    String Selected_invoice = String.valueOf(object.getInvoice_no());
//                    System.out.println("Selected Invoice Pickup--> "+ Selected_invoice);
                    lblReadyFDNo.setText(Selected_invoice);
                } else {
                    tblvReadyFDSalesInvoiceDetails.getItems().clear();
                    calculateReadyFDTotalAmount();
//                    lblReadyFDNo.setText("");
                }
            }
            return property;
        });
    }

    // Delivered Tab Checkbox
    private void initDeliveredCheckboxTab() {
        tblvDeliveredSalesInvoiceList.setEditable(true);
//        todo: checkbox packing select checkbox
        tblDeliveredSalesInvoiceSelect.setCellFactory(column -> new CheckBoxTableCell<>());
        tblDeliveredSalesInvoiceSelect.setCellValueFactory(cellData -> {
            DispatchMngtDeliveredSalesInvoiceDTO cellValue = cellData.getValue();
            BooleanProperty property = cellValue.is_row_selectedProperty();
            ObservableList<DispatchMngtDeliveredSalesInvoiceDTO> list = tblvDeliveredSalesInvoiceList.getItems();

            for (DispatchMngtDeliveredSalesInvoiceDTO object : list) {
                if (object.isIs_row_selected()) {
                    Selected_index = Long.valueOf(object.getOrder_id());
//                    System.out.println("Selected Index "+ Selected_index);
                    handleDeliveredCheckbox(Selected_index);
                    String Selected_invoice = String.valueOf(object.getInvoice_no());
//                    System.out.println("Selected Invoice Pickup--> "+ Selected_invoice);
                    lblDeliveredNo.setText(Selected_invoice);
                } else {
                    tblvDeliveredSalesInvoiceDetails.getItems().clear();
                    calculateDeliveredTotalAmount();
//                    lblDeliveredNo.setText("");
                }
            }
            return property;
        });
    }

    // Returned Tab Checkbox
    private void initReturnedCheckboxTab() {
        tblvReturnedSalesInvoiceList.setEditable(true);
//        todo: checkbox packing select checkbox
        tblReturnedSalesInvoiceSelect.setCellFactory(column -> new CheckBoxTableCell<>());
        tblReturnedSalesInvoiceSelect.setCellValueFactory(cellData -> {
            DispatchMngtReturnedSalesInvoiceDTO cellValue = cellData.getValue();
            BooleanProperty property = cellValue.is_row_selectedProperty();
            ObservableList<DispatchMngtReturnedSalesInvoiceDTO> list = tblvReturnedSalesInvoiceList.getItems();

            for (DispatchMngtReturnedSalesInvoiceDTO object : list) {
                if (object.isIs_row_selected()) {
                    Selected_index = Long.valueOf(object.getOrder_id());
//                    System.out.println("Selected Index "+ Selected_index);
                    handleReturnedCheckbox(Selected_index);
                    String Selected_Returned_invoice = String.valueOf(object.getInvoice_no());
//                    System.out.println("Selected Invoice Pickup--> "+ Selected_Returned_invoice);
                    lblReturnedNo.setText(Selected_Returned_invoice);
                } else {
                    tblvReturnedSalesInvoiceDetails.getItems().clear();
                    calculateReturnedTotalAmount();
//                    lblReturnedNo.setText("");
                }
            }
            return property;
        });
    }

    // Tabs Initialize & Tab Change Action
    @FXML
    private void handleTabChange(Event event) {
        Tab selectedTab = (Tab) event.getSource();
        if (selectedTab.isSelected()) {
            if (selectedTab == tbsAll) {
                Platform.runLater(() -> tfAllSalesListSearch.requestFocus());
                // list call
                fetchPickupAllSalesList("");
                // list search
                tfAllSalesListSearch.textProperty().addListener((observable, oldValue, newValue) -> {
                    filterAllData(newValue.trim());
                });
                // list column status color
                tblvAllSalesStatus.setCellValueFactory(new PropertyValueFactory<>("order_status"));
                tblvAllSalesStatus.setCellFactory(column -> {
                    return new TableCell<DispatchMngtAllSalesDTO, String>() {
                        @Override
                        protected void updateItem(String item, boolean empty) {
                            super.updateItem(item, empty);
                            if (item == null || empty) {
                                setText(null);
//                        setStyle("-fx-background-color: white;");
                            } else {
                                setText(item);
                                setTextFill(Color.BLACK);

                                System.out.println("item : "+item);

                                if (item.equalsIgnoreCase("pickup")) {
                                    setStyle("-fx-background-color: #ffdf8b;");
                                    setText("Order Placed");
                                } else if (item.equalsIgnoreCase("packing")) {
                                    setStyle("-fx-background-color: #d9bbff;");
                                    setText("Awaiting Confirmation");
                                } else if (item.equalsIgnoreCase("Ready For Delivery")) {
                                    setStyle("-fx-background-color: #90ddff;");
                                    setText("Accepted");
                                } else if (item.equalsIgnoreCase("delivered")) {
                                    setStyle("-fx-background-color: #aee8ad;");
                                    setText("Dispatched");
                                } else if (item.equalsIgnoreCase("cancelled")) {
                                    setStyle("-fx-background-color: #ffacac;");
                                    setText("Cancelled");
                                }
                                else if (item.equalsIgnoreCase("pending")) {
                                    setStyle("-fx-background-color:   #ff4da6; -fx-text-fill: white;");
                                    setText("Pending");
                                }

                                else if (item.equalsIgnoreCase("")) {
                                    setStyle(null);
                                    setText("");
                                }
                            }
                        }
                    };
                });
            } else if (selectedTab == tbsPickup) {
                Platform.runLater(() -> tfPickupSalesOrdersListSearch.requestFocus());
                // list call
                fetchPickupAllSalesOrdersList("");
                // list search
                tfPickupSalesOrdersListSearch.textProperty().addListener((observable, oldValue, newValue) -> {
                    filterPickupData(newValue.trim());
                });
                // list column status color
                tblPickupSalesOrdersStatus.setCellValueFactory(new PropertyValueFactory<>("order_status"));
                tblPickupSalesOrdersStatus.setCellFactory(column -> {
                    return new TableCell<DispatchMngtPickupSalesOrdersDTO, String>() {
                        @Override
                        protected void updateItem(String item, boolean empty) {
                            super.updateItem(item, empty);
                            if (item == null || empty) {
                                setText(null);
//                        setStyle("-fx-background-color: white;");
                            } else {
                                setText(item);
                                setTextFill(Color.BLACK);
                                if (item.equalsIgnoreCase("pickUp")) {
                                    setStyle("-fx-background-color: #ffdf8b;");
                                    setText("Order Placed");
                                }
                            }
                        }
                    };
                });
            } else if (selectedTab == tbsPacking) {
                Platform.runLater(() -> tfPackingSalesChallanListSearch.requestFocus());
                // list call
                fetchPackingAllSalesChallanList("");
                // list search
                tfPackingSalesChallanListSearch.textProperty().addListener((observable, oldValue, newValue) -> {
                    filterPackingData(newValue.trim());
                });
                // list column status color
                tblPackingSalesChallanStatus.setCellValueFactory(new PropertyValueFactory<>("order_status"));
                tblPackingSalesChallanStatus.setCellFactory(column -> {
                    return new TableCell<DispatchMngtPackingSalesChallanDTO, String>() {
                        @Override
                        protected void updateItem(String item, boolean empty) {
                            super.updateItem(item, empty);
                            if (item == null || empty) {
                                setText(null);
//                        setStyle("-fx-background-color: white;");
                            } else {
                                setText(item);
                                setTextFill(Color.BLACK);
                                if (item.equalsIgnoreCase("Ready For Delivery")) {
                                    setStyle("-fx-background-color: #d9bbff;");
                                    setText("Accepted");
                                }
                            }
                        }
                    };
                });
            } else if (selectedTab == tbsReadyFD) {
                Platform.runLater(() -> tfReadyFDSalesInvoiceListSearch.requestFocus());
                // list call
                fetchReadyFDAllSalesInvoiceList("");
                // list search
                tfReadyFDSalesInvoiceListSearch.textProperty().addListener((observable, oldValue, newValue) -> {
                    filterReadyFDData(newValue.trim());
                });
                // list column status color
                tblReadyFDSalesInvoiceStatus.setCellValueFactory(new PropertyValueFactory<>("order_status"));
                tblReadyFDSalesInvoiceStatus.setCellFactory(column -> {
                    return new TableCell<DispatchMngtReadyFDSalesInvoiceDTO, String>() {
                        @Override
                        protected void updateItem(String item, boolean empty) {
                            super.updateItem(item, empty);
                            if (item == null || empty) {
                                setText(null);
//                        setStyle("-fx-background-color: white;");
                            } else {
                                setText(item);
                                setTextFill(Color.BLACK);
                                if (item.equalsIgnoreCase("ReadyForDelivery")) {
                                    setStyle("-fx-background-color: #90ddff;");
                                    setText("Ready For Delivery");
                                } else if (item.equalsIgnoreCase("delivered")) {
                                    setStyle("-fx-background-color: #aee8ad;");
                                    setText("Delivered");
                                } else if (item.equalsIgnoreCase("cancelled")) {
                                    setStyle("-fx-background-color: #ffacac;");
                                    setText("Cancelled");
                                }
                            }
                        }
                    };
                });
                // radion button action for payload
                initStatusRadioButtonGroup();
            } else if (selectedTab == tbsDelivered) {
                Platform.runLater(() -> tfDeliveredSalesInvoiceListSearch.requestFocus());
                // list call
                fetchDeliveredAllSalesInvoiceList("");
                // list search
                tfDeliveredSalesInvoiceListSearch.textProperty().addListener((observable, oldValue, newValue) -> {
                    filterDeliveredData(newValue.trim());
                });
                // list column status color
                tblDeliveredSalesInvoiceStatus.setCellValueFactory(new PropertyValueFactory<>("order_status"));
                tblDeliveredSalesInvoiceStatus.setCellFactory(column -> {
                    return new TableCell<DispatchMngtDeliveredSalesInvoiceDTO, String>() {
                        @Override
                        protected void updateItem(String item, boolean empty) {
                            super.updateItem(item, empty);
                            if (item == null || empty) {
                                setText(null);
//                        setStyle("-fx-background-color: white;");
                            } else {
                                setText(item);
                                setTextFill(Color.BLACK);
                                if (item.equalsIgnoreCase("delivered")) {
                                    setStyle("-fx-background-color: #aee8ad;");
                                    setText("Dispatched");
                                }
                            }
                        }
                    };
                });
            } else if (selectedTab == tbsRtnCnl) {
                Platform.runLater(() -> tfReturnedSalesInvoiceListSearch.requestFocus());
                // list call
                fetchReturnedAllSalesInvoiceList("");
                // list search
                tfReturnedSalesInvoiceListSearch.textProperty().addListener((observable, oldValue, newValue) -> {
                    filterReturnedData(newValue.trim());
                });
                // list column status color
                tblReturnedSalesInvoiceStatus.setCellValueFactory(new PropertyValueFactory<>("order_status"));
                tblReturnedSalesInvoiceStatus.setCellFactory(column -> {
                    return new TableCell<DispatchMngtReturnedSalesInvoiceDTO, String>() {
                        @Override
                        protected void updateItem(String item, boolean empty) {
                            super.updateItem(item, empty);
                            if (item == null || empty) {
                                setText(null);
//                        setStyle("-fx-background-color: white;");
                            } else {
                                setText(item);
                                setTextFill(Color.BLACK);
                                if (item.equalsIgnoreCase("delivered")) {
                                    setStyle("-fx-background-color: #aee8ad;");
                                    setText("Dispatched");
                                }
                            }
                        }
                    };
                });
            }
        }
    }

    // Radio button action
    private void initStatusRadioButtonGroup() {
        rbDelivered.setToggleGroup(tgStatus);
        rbReturned.setToggleGroup(tgStatus);

        tgStatus.selectedToggleProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                RadioButton selectedRadioButton = (RadioButton) newValue;
//                String selectedValue = selectedRadioButton.getText();
                selectedValue = selectedRadioButton.getId().equals("rbDelivered") ? "delivered" : "cancelled";
//                System.out.println("Selected value for invoiceStatus : " + selectedValue);
//                System.out.println("Selected_index for invoiceId : " + Selected_index);
            }
        });

        cancelStatus.setOnAction(event -> {
            tgStatus.selectToggle(null);
        });
    }

    // ReadyFD Form Submit
    public void onClickSubmit(ActionEvent actionEvent) {
        ObservableList<DispatchMngtReadyFDSalesInvoiceDTO> list = tblvReadyFDSalesInvoiceList.getItems();

        long cnt = list.stream()
                .filter(obj -> obj.isIs_row_selected())
                .count();

        if (cnt == 0) {
            AlertUtility.AlertError(AlertUtility.alertTypeError, "Please Select Checkbox", output -> {
            });
        } else {
            if (!selectedValue.isEmpty()) {
                AlertUtility.AlertConfirmation(AlertUtility.alertTypeConfirmation, "Are you sure?", input -> {
                    if (input == 1) {
                        updateStatusListData();

                        tblvReadyFDSalesInvoiceDetails.getItems().clear();

                        // Fetch the updated data
                        tgStatus.selectToggle(null);
                        fetchReadyFDAllSalesInvoiceList("");
                    }
                });
            } else {
                AlertUtility.AlertError(AlertUtility.alertTypeError, "Please Select Option", output -> {
                });
            }


        }
//        System.out.println("Submit Button Clicked " + Selected_index);
    }

    // ReadyFD List Update
    private void updateStatusListData() {
        map.put("invoiceStatus", selectedValue);
        map.put("invoiceId", String.valueOf(Selected_index));

        String formData = Globals.mapToStringforFormData(map);
        System.out.println("formData - " + formData);
        HttpResponse<String> response = APIClient.postFormDataRequest(formData, DISPATCH_MANAGEMENT_ReadyFDSales_STATUS_UPDATE);
        JsonObject responseBody = new Gson().fromJson(response.body(), JsonObject.class);
        System.out.println("Response=>" + responseBody);
        if (responseBody.get("responseStatus").getAsInt() == 200) {
            AlertUtility.AlertDialogForSuccess("success", responseBody.get("message").getAsString(), input -> {
                // list call
                fetchReadyFDAllSalesInvoiceList("");
            });
        } else {
            AlertUtility.AlertDialogForError("error", responseBody.get("message").getAsString(), input -> {
            });
        }
    }

    // All Start
    private void fetchPickupAllSalesList(String searchKey) {
        DispatchManagementListLogger.info("fetch All Dispatch Management Sales List : DispatchManagementController");

        APIClient apiClient = null;

        try {
            DispatchManagementListLogger.info("fetch All Dispatch Management Sales List : DispatchManagementController");
            Map<String, String> body = new HashMap<>();
            // HttpResponse<String> response = APIClient.getRequest(EndPoints.DISPATCH_MANAGEMENT_ALL_LIST);
            apiClient = new APIClient("get_all_pur_orders", "", RequestType.GET);
            apiClient.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
                @Override
                public void handle(WorkerStateEvent workerStateEvent) {
                    // JsonObject responseBody = new Gson().fromJson(response.body(), JsonObject.class);
                    jsonObject = new Gson().fromJson(workerStateEvent.getSource().getValue().toString(), JsonObject.class);
                  //  System.out.println(jsonObject);

//                    System.out.println("DISPATCH_MANAGEMENT_ALL_LIST" + jsonObject);
                    ObservableList<DispatchMngtAllSalesDTO> observableList = FXCollections.observableArrayList();
                    if (jsonObject.get("responseStatus").getAsInt() == 200) {
//                          System.out.println("DispatchManagementController---> " + responseList);

                        // All Sales Pickup List
//                        JsonArray responseListOrderData = jsonObject.getAsJsonArray("OrderData");
//                        // All Sales Packing List
//                        JsonArray responseListChallanData = jsonObject.getAsJsonArray("ChallanData");
                        // All Sales ReadyFD, Delivered & Cancelled List
                        JsonArray responseListAllData = jsonObject.getAsJsonArray("data");

                        System.out.println("data : "+responseListAllData);
                        if (responseListAllData.size() > 0) {
//                            System.out.println("DispatchManagementController---> " + responseListAllData);
                            tblvAllSalesList.setVisible(true);

//                            for (JsonElement element : responseListOrderData) {
////                            System.out.println("DispatchManagementController---> " + responseList);
//                                JsonObject item = element.getAsJsonObject();
//                                String order_id = item.get("order_id") != null ? item.get("order_id").getAsString() : "";
//                                String invoice_no = item.get("bill_no") != null ? item.get("bill_no").getAsString() : "";
//                                String invoice_date = item.get("bill_date") != null ? item.get("bill_date").getAsString() : "";
//                                String customer_name = item.get("sundry_debtor_name") != null ? item.get("sundry_debtor_name").getAsString() : "";
//                                String narration = item.get("narration") != null ? item.get("narration").getAsString() : "";
//                                String order_status = item.get("orderStatus") != null ? item.get("orderStatus").getAsString() : "";
//                                String bill_amount = item.get("total_amount") != null ? item.get("total_amount").getAsString() : "";
//                                String item_count = item.get("product_count") != null ? item.get("product_count").getAsString() : "";
//
//                                observableList.add(new DispatchMngtAllSalesDTO(order_id, invoice_no, invoice_date, customer_name, narration, order_status, bill_amount, item_count));
//                            }
//                            for (JsonElement element : responseListChallanData) {
////                            System.out.println("DispatchManagementController---> " + responseList);
//                                JsonObject item = element.getAsJsonObject();
//                                String order_id = item.get("challan_id") != null ? item.get("challan_id").getAsString() : "";
//                                String invoice_no = item.get("bill_no") != null ? item.get("bill_no").getAsString() : "";
//                                String invoice_date = item.get("bill_date") != null ? item.get("bill_date").getAsString() : "";
//                                String customer_name = item.get("sundry_debtors_name") != null ? item.get("sundry_debtors_name").getAsString() : "";
//                                String narration = item.get("narration") != null ? item.get("narration").getAsString() : "";
//                                String order_status = item.get("orderStatus") != null ? item.get("orderStatus").getAsString() : "";
//                                String bill_amount = item.get("total_amount") != null ? item.get("total_amount").getAsString() : "";
//                                String item_count = item.get("product_count") != null ? item.get("product_count").getAsString() : "";
//
//                                observableList.add(new DispatchMngtAllSalesDTO(order_id, invoice_no, invoice_date, customer_name, narration, order_status, bill_amount, item_count));
//                            }
                            for (JsonElement element : responseListAllData) {
//                            System.out.println("DispatchManagementController---> " + responseList);
                                JsonObject item = element.getAsJsonObject();
                                String order_id = item.get("order_id") != null ? item.get("order_id").getAsString() : "";
                                String invoice_no = item.get("bill_no") != null ? item.get("bill_no").getAsString() : "";
                                String invoice_date = item.get("bill_date") != null ? item.get("bill_date").getAsString() : "";
                                String customer_name = item.get("debtors_name") != null ? item.get("debtors_name").getAsString() : "";
                                String narration = item.get("narration") != null ? item.get("narration").getAsString() : "";
                                String order_status = item.get("orderStatus").isJsonNull() ? "" :item.get("orderStatus").getAsString();
                                String bill_amount = item.get("totalamt") != null ? item.get("totalamt").getAsString() : "";
                                String item_count = item.get("product_count") != null ? item.get("product_count").getAsString() : "";

                                observableList.add(new DispatchMngtAllSalesDTO(order_id, invoice_no, invoice_date, customer_name, narration, order_status, bill_amount, item_count));
                            }

                            tblvAllSalesInvoiceNo.setCellValueFactory(new PropertyValueFactory<>("invoice_no"));
                            tblvAllSalesInvoiceDate.setCellValueFactory(new PropertyValueFactory<>("invoice_date"));
                            tblvAllSalesCustomer.setCellValueFactory(new PropertyValueFactory<>("customer_name"));
                            tblvAllSalesNarration.setCellValueFactory(new PropertyValueFactory<>("narration"));
                            tblvAllSalesStatus.setCellValueFactory(new PropertyValueFactory<>("order_status"));
                            tblvAllSalesBillAmount.setCellValueFactory(new PropertyValueFactory<>("bill_amount"));
                            tblvAllSalesItem.setCellValueFactory(new PropertyValueFactory<>("item_count"));

                            tblvAllSalesList.setItems(observableList);
                            originalDataAll = observableList;
                            allListCount = originalDataAll.size();
//                            System.out.println("allListCount----> " + allListCount);
                            tbsAll.setText("(" + allListCount + ")");

                            DispatchManagementListLogger.debug("Success in Displaying All Dispatch Management Sales List : DispatchManagementController");
                        } else {
                            tblvAllSalesList.getItems().clear();
                            DispatchManagementListLogger.debug("All Dispatch Management Sales List ResponseObject is null : DispatchManagementController");
                        }
                    } else {
                        tblvAllSalesList.getItems().clear();
                        DispatchManagementListLogger.debug("Error in response of All Dispatch Management Sales List : DispatchManagementController");
                    }
                }
            });

            apiClient.setOnCancelled(new EventHandler<WorkerStateEvent>() {
                @Override
                public void handle(WorkerStateEvent workerStateEvent) {
                    DispatchManagementListLogger.error("Network API cancelled in fetchAllSalesList()" + workerStateEvent.getSource().getValue().toString());

                }
            });

            apiClient.setOnFailed(new EventHandler<WorkerStateEvent>() {
                @Override
                public void handle(WorkerStateEvent workerStateEvent) {
                    DispatchManagementListLogger.error("Network API failed in fetchAllSalesList()" + workerStateEvent.getSource().getValue().toString());
                }
            });
            apiClient.start();
            DispatchManagementListLogger.debug("Get All Dispatch Management Sales Data End...");
        } catch (Exception e) {
            DispatchManagementListLogger.error("All Dispatch Management Sales Error is " + e.getMessage());
            e.printStackTrace();
        } finally {
            apiClient = null;
        }

    }

    // Details
    private void handleAllCheckbox(Long selectedIndex) {
//        System.out.println("Selected Index "+ selectedIndex);
        DispatchManagementListLogger.info("fetch All Dispatch Management Sales Details : DispatchManagementController");

        APIClient apiClient = null;

        try {
            DispatchManagementListLogger.info("fetch All Dispatch Management Sales Details : DispatchManagementController");
            Map<String, String> body = new HashMap<>();
            body.put("id", String.valueOf(selectedIndex));
            System.out.println("body ----------->  " + body);
            String requestBody = Globals.mapToStringforFormData(body);

            apiClient = new APIClient(EndPoints.PURCHASE_ORDER_GET_BY_ID, requestBody, RequestType.FORM_DATA);
            apiClient.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
                @Override
                public void handle(WorkerStateEvent workerStateEvent) {
                    // JsonObject responseBody = new Gson().fromJson(response.body(), JsonObject.class);
                    jsonObject = new Gson().fromJson(workerStateEvent.getSource().getValue().toString(), JsonObject.class);
                    System.out.println("DISPATCH_MANAGEMENT_PickupSalesOrders_DETAILS" + jsonObject);
                    ObservableList<DispatchMngtAllSalesDetailsDTO> observableList = FXCollections.observableArrayList();
                    if (jsonObject.get("responseStatus").getAsInt() == 200) {
                        JsonArray responseListOrderDetails = jsonObject.getAsJsonArray("row");
                        if (responseListOrderDetails.size() > 0) {
                            System.out.println("DispatchManagementController111---> " + responseListOrderDetails);
                            tblvAllSalesListDetails.setVisible(true);

                            int index = 1; // Starting serial number
                            for (JsonElement element : responseListOrderDetails) {
//                                System.out.println("DispatchManagementController222---> " + responseList);
                                JsonObject item = element.getAsJsonObject();
                                String description = item.get("product_name") != null ? item.get("product_name").getAsString() : "";
                                String batch = item.get("batch_no") != null ? item.get("batch_no").getAsString() : "";
                                String qty = item.get("qty") != null ? item.get("qty").getAsString() : "";
                                String price = item.get("total_amt") != null ? item.get("total_amt").getAsString() : "";

                                observableList.add(new DispatchMngtAllSalesDetailsDTO(String.valueOf(index++), description, batch, qty, price));
                            }

                            tblAllSalesListDetailSrNo.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getId()));
                            tblAllSalesListDetailDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
                            tblAllSalesListDetailBatch.setCellValueFactory(new PropertyValueFactory<>("batch"));
                            tblAllSalesListDetailQty.setCellValueFactory(new PropertyValueFactory<>("qty"));
                            tblAllSalesListDetailPrice.setCellValueFactory(new PropertyValueFactory<>("price"));

                            tblvAllSalesListDetails.setItems(observableList);
                            originalDataAllDetails = observableList;
                            calculateAllTotalAmount();
                            DispatchManagementListLogger.debug("Success in Displaying All Dispatch Management Sales Details : DispatchManagementController");
                        } else {
                            tblvAllSalesListDetails.getItems().clear();
                            calculateAllTotalAmount();
                            DispatchManagementListLogger.debug("All Dispatch Management Sales Details ResponseObject is null : DispatchManagementController");
                        }
                    } else {
                        tblvAllSalesListDetails.getItems().clear();
                        calculateAllTotalAmount();
                        DispatchManagementListLogger.debug("Error in response of All Dispatch Management Sales Details : DispatchManagementController");
                    }
                }
            });
            apiClient.setOnCancelled(new EventHandler<WorkerStateEvent>() {
                @Override
                public void handle(WorkerStateEvent workerStateEvent) {
                    DispatchManagementListLogger.error("Network API cancelled in handleAllCheckbox()" + workerStateEvent.getSource().getValue().toString());

                }
            });

            apiClient.setOnFailed(new EventHandler<WorkerStateEvent>() {
                @Override
                public void handle(WorkerStateEvent workerStateEvent) {
                    DispatchManagementListLogger.error("Network API failed in handleAllCheckbox()" + workerStateEvent.getSource().getValue().toString());
                }
            });
            apiClient.start();
            DispatchManagementListLogger.debug("Get All Dispatch Management Sales Data End...");
        } catch (Exception e) {
            DispatchManagementListLogger.error("All Dispatch Management Sales Details Error is " + e.getMessage());
            e.printStackTrace();
        } finally {
            apiClient = null;
        }
    }

    // calculations
    private void calculateAllTotalAmount() {
        ObservableList<DispatchMngtAllSalesDetailsDTO> filteredData = tblvAllSalesListDetails.getItems();
        // Calculate the Totals
        double totalQty = 0.0;
        double totalPrice = 0.0;
        for (DispatchMngtAllSalesDetailsDTO item : filteredData) {
            totalQty += Double.parseDouble(item.getQty().isEmpty() ? "0.0" : item.getQty());
            totalPrice += Double.parseDouble(item.getPrice().isEmpty() ? "0.0" : item.getPrice());
        }
        // Update Labels in the FXML
        lblAllTotalQty.setText(String.valueOf(totalQty));
        lblAllTotalPrice.setText(String.valueOf(totalPrice));
    }

    // Search
    private void filterAllData(String keyword) {
        ObservableList<DispatchMngtAllSalesDTO> filterAllData = FXCollections.observableArrayList();


        DispatchManagementListLogger.error("Search All Dispatch Management Sales Orders List in DispatchManagementController");
        if (keyword.isEmpty()) {
            tblvAllSalesList.setItems(originalDataAll);
            return;
        }

        for (DispatchMngtAllSalesDTO item : originalDataAll) {
            if (matchesKeywordAll(item, keyword)) {
                filterAllData.add(item);
            }
        }

        tblvAllSalesList.setItems(filterAllData);
//        calculatePickupTotalAmount();
    }

    // Search keyword
    private boolean matchesKeywordAll(DispatchMngtAllSalesDTO item, String keyword) {
        String lowerCaseKeyword = keyword.toLowerCase();
        return
                item.getInvoice_no().toLowerCase().contains(lowerCaseKeyword) ||
                        item.getInvoice_date().toLowerCase().contains(lowerCaseKeyword) ||
                        item.getCustomer_name().toLowerCase().contains(lowerCaseKeyword) ||
                        item.getNarration().toLowerCase().contains(lowerCaseKeyword) ||
                        item.getOrder_status().toLowerCase().contains(lowerCaseKeyword) ||
                        item.getBill_amount().toLowerCase().contains(lowerCaseKeyword) ||
                        item.getItem_count().toLowerCase().contains(lowerCaseKeyword);
    }
    // All End


    //    Pickup Start
    private void fetchPickupAllSalesOrdersList(String searchKey) {
        DispatchManagementListLogger.info("fetch Pickup Dispatch Management Sales Orders List : DispatchManagementController");

        APIClient apiClient = null;

        try {
            DispatchManagementListLogger.info("fetch Pickup Dispatch Management Sales Orders List : DispatchManagementController");
            Map<String, String> body = new HashMap<>();
            // HttpResponse<String> response = APIClient.getRequest(EndPoints.DISPATCH_MANAGEMENT_PickupSalesOrders_LIST);
            apiClient = new APIClient("get_all_pur_orders", "", RequestType.GET);
            apiClient.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
                @Override
                public void handle(WorkerStateEvent workerStateEvent) {
                    // JsonObject responseBody = new Gson().fromJson(response.body(), JsonObject.class);
                    jsonObject = new Gson().fromJson(workerStateEvent.getSource().getValue().toString(), JsonObject.class);
//                    System.out.println("DISPATCH_MANAGEMENT_PickupSalesOrders_LIST" + jsonObject);
                    ObservableList<DispatchMngtPickupSalesOrdersDTO> observableList = FXCollections.observableArrayList();
                    if (jsonObject.get("responseStatus").getAsInt() == 200) {
                        JsonArray responseList = jsonObject.getAsJsonArray("data");
//                        System.out.println("DispatchManagementController---> " + responseList);
                        if (responseList.size() > 0) {
//                            System.out.println("DispatchManagementController---> " + responseList);
                            tblvPickupSalesOrdersList.setVisible(true);
                            for (JsonElement element : responseList) {
//                            System.out.println("DispatchManagementController---> " + responseList);
                                JsonObject item = element.getAsJsonObject();
                                String order_id = item.get("order_id") != null ? item.get("order_id").getAsString() : "";
                                String invoice_no = item.get("bill_no") != null ? item.get("bill_no").getAsString() : "";
                                String invoice_date = item.get("bill_date") != null ? item.get("bill_date").getAsString() : "";
                                String customer_name = item.get("debtors_name") != null ? item.get("debtors_name").getAsString() : "";
                                String narration = item.get("narration") != null ? item.get("narration").getAsString() : "";
                                String order_status = item.get("orderStatus").isJsonNull() ? "" : item.get("orderStatus").getAsString();
                                String bill_amount = item.get("totalamt") != null ? item.get("totalamt").getAsString() : "";
                                String item_count = item.get("product_count") != null ? item.get("product_count").getAsString() : "";
//                                System.out.println(order_id + invoice_no + invoice_date + customer_name + narration + order_status + bill_amount + item_count);

                                if (order_status.equals("pickup")) {
                                    observableList.add(new DispatchMngtPickupSalesOrdersDTO(order_id, invoice_no, invoice_date, customer_name, narration, order_status, bill_amount, item_count));

                                }
                            }

                            tblPickupSalesOrdersInvoiceNo.setCellValueFactory(new PropertyValueFactory<>("invoice_no"));
                            tblPickupSalesOrdersInvoiceDate.setCellValueFactory(new PropertyValueFactory<>("invoice_date"));
                            tblPickupSalesOrdersCustomer.setCellValueFactory(new PropertyValueFactory<>("customer_name"));
                            tblPickupSalesOrdersNarration.setCellValueFactory(new PropertyValueFactory<>("narration"));
                            tblPickupSalesOrdersStatus.setCellValueFactory(new PropertyValueFactory<>("order_status"));
                            tblPickupSalesOrdersBillAmount.setCellValueFactory(new PropertyValueFactory<>("bill_amount"));
                            tblPickupSalesOrdersItem.setCellValueFactory(new PropertyValueFactory<>("item_count"));

                            tblvPickupSalesOrdersList.setItems(observableList);
                            originalDataPickup = observableList;
                            pickupListCount = originalDataPickup.size();
                            tbsPickup.setText("(" + pickupListCount + ")");

                            DispatchManagementListLogger.debug("Success in Displaying Pickup Dispatch Management Sales Orders List : DispatchManagementController");
                        } else {
                            tblvPickupSalesOrdersList.getItems().clear();
                            DispatchManagementListLogger.debug("Pickup Dispatch Management Sales Orders List ResponseObject is null : DispatchManagementController");
                        }
                    } else {
                        tblvPickupSalesOrdersList.getItems().clear();
                        DispatchManagementListLogger.debug("Error in response of Pickup Dispatch Management Sales Orders List : DispatchManagementController");
                    }
                }
            });

            apiClient.setOnCancelled(new EventHandler<WorkerStateEvent>() {
                @Override
                public void handle(WorkerStateEvent workerStateEvent) {
                    DispatchManagementListLogger.error("Network API cancelled in fetchPickupAllSalesOrdersList()" + workerStateEvent.getSource().getValue().toString());

                }
            });

            apiClient.setOnFailed(new EventHandler<WorkerStateEvent>() {
                @Override
                public void handle(WorkerStateEvent workerStateEvent) {
                    DispatchManagementListLogger.error("Network API failed in fetchPickupAllSalesOrdersList()" + workerStateEvent.getSource().getValue().toString());
                }
            });
            apiClient.start();
            DispatchManagementListLogger.debug("Get Pickup Dispatch Management Sales Orders Data End...");
        } catch (Exception e) {
            DispatchManagementListLogger.error("Pickup Dispatch Management Sales Orders Error is " + e.getMessage());
            e.printStackTrace();
        } finally {
            apiClient = null;
        }

    }

    // Details
    private void handlePickupCheckbox(Long selectedIndex) {
//        System.out.println("Selected Index "+ selectedIndex);
        DispatchManagementListLogger.info("fetch Pickup Dispatch Management Sales Orders Details : DispatchManagementController");

        APIClient apiClient = null;

        try {
            DispatchManagementListLogger.info("fetch Pickup Dispatch Management Sales Orders Details : DispatchManagementController");
            Map<String, String> body = new HashMap<>();
            body.put("id", String.valueOf(selectedIndex));
//            System.out.println("body ----------->  " + body);
            String requestBody = Globals.mapToStringforFormData(body);
//            HttpResponse<String> response = APIClient.postFormDataRequest(requestBody, EndPoints.DISPATCH_MANAGEMENT_PickupSalesOrders_DETAILS);
            apiClient = new APIClient(EndPoints.PURCHASE_ORDER_GET_BY_ID, requestBody, RequestType.FORM_DATA);
            apiClient.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
                @Override
                public void handle(WorkerStateEvent workerStateEvent) {
                    // JsonObject responseBody = new Gson().fromJson(response.body(), JsonObject.class);
                    jsonObject = new Gson().fromJson(workerStateEvent.getSource().getValue().toString(), JsonObject.class);
//                    System.out.println("DISPATCH_MANAGEMENT_PickupSalesOrders_DETAILS" + jsonObject);
                    ObservableList<DispatchMngtPickupSalesOrdersDetailsDTO> observableList = FXCollections.observableArrayList();
                    if (jsonObject.get("responseStatus").getAsInt() == 200) {
                        JsonArray responseList = jsonObject.getAsJsonArray("row");
//                        System.out.println("DispatchManagementController---> " + responseList);
                        if (responseList.size() > 0) {
//                            System.out.println("DispatchManagementController111---> " + responseList);
                            tblvPickupSalesOrdersDetails.setVisible(true);


                            int index = 1; // Starting serial number
                            for (JsonElement element : responseList) {
//                                System.out.println("DispatchManagementController222---> " + responseList);
                                JsonObject item = element.getAsJsonObject();
                                String description = item.get("product_name") != null ? item.get("product_name").getAsString() : "";
                                String batch = item.get("batch_no") != null ? item.get("batch_no").getAsString() : "";
                                String qty = item.get("qty") != null ? item.get("qty").getAsString() : "";
                                String price = item.get("total_amt") != null ? item.get("total_amt").getAsString() : "";


                                observableList.add(new DispatchMngtPickupSalesOrdersDetailsDTO(String.valueOf(index++), description, batch, qty, price));
                            }

                            tblPickupSalesOrdersDetailSrNo.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getId()));
                            tblPickupSalesOrdersDetailDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
                            tblPickupSalesOrdersDetailBatch.setCellValueFactory(new PropertyValueFactory<>("batch"));
                            tblPickupSalesOrdersDetailQty.setCellValueFactory(new PropertyValueFactory<>("qty"));
                            tblPickupSalesOrdersDetailPrice.setCellValueFactory(new PropertyValueFactory<>("price"));

                            tblvPickupSalesOrdersDetails.setItems(observableList);
                            originalDataPickupDetails = observableList;
                            calculatePickupTotalAmount();
                            DispatchManagementListLogger.debug("Success in Displaying Pickup Dispatch Management Sales Orders Details : DispatchManagementController");
                        } else {
                            tblvPickupSalesOrdersDetails.getItems().clear();
                            calculatePickupTotalAmount();
                            DispatchManagementListLogger.debug("Pickup Dispatch Management Sales Orders Details ResponseObject is null : DispatchManagementController");
                        }
                    } else {
                        tblvPickupSalesOrdersDetails.getItems().clear();
                        calculatePickupTotalAmount();
                        DispatchManagementListLogger.debug("Error in response of Pickup Dispatch Management Sales Orders Details : DispatchManagementController");
                    }
                }
            });
            apiClient.setOnCancelled(new EventHandler<WorkerStateEvent>() {
                @Override
                public void handle(WorkerStateEvent workerStateEvent) {
                    DispatchManagementListLogger.error("Network API cancelled in handlePickupCheckbox()" + workerStateEvent.getSource().getValue().toString());

                }
            });

            apiClient.setOnFailed(new EventHandler<WorkerStateEvent>() {
                @Override
                public void handle(WorkerStateEvent workerStateEvent) {
                    DispatchManagementListLogger.error("Network API failed in handlePickupCheckbox()" + workerStateEvent.getSource().getValue().toString());
                }
            });
            apiClient.start();
            DispatchManagementListLogger.debug("Get Pickup Dispatch Management Sales Orders Data End...");
        } catch (Exception e) {
            DispatchManagementListLogger.error("Pickup Dispatch Management Sales Orders Details Error is " + e.getMessage());
            e.printStackTrace();
        } finally {
            apiClient = null;
        }
    }

    // calculations
    private void calculatePickupTotalAmount() {
        ObservableList<DispatchMngtPickupSalesOrdersDetailsDTO> filteredData = tblvPickupSalesOrdersDetails.getItems();
        // Calculate the Totals
        double totalQty = 0.0;
        double totalPrice = 0.0;
        for (DispatchMngtPickupSalesOrdersDetailsDTO item : filteredData) {
            totalQty += Double.parseDouble(item.getQty().isEmpty() ? "0.0" : item.getQty());
            totalPrice += Double.parseDouble(item.getPrice().isEmpty() ? "0.0" : item.getPrice());
        }
        // Update Labels in the FXML
        lblPickupTotalQty.setText(String.valueOf(totalQty));
        lblPickupTotalPrice.setText(String.valueOf(totalPrice));
    }

    // Search
    private void filterPickupData(String keyword) {
        ObservableList<DispatchMngtPickupSalesOrdersDTO> filterPickupData = FXCollections.observableArrayList();

        DispatchManagementListLogger.error("Search Pickup Dispatch Management Sales Orders Orders List in DispatchManagementController");
        if (keyword.isEmpty()) {
            tblvPickupSalesOrdersList.setItems(originalDataPickup);
            return;
        }

        for (DispatchMngtPickupSalesOrdersDTO item : originalDataPickup) {
            if (matchesKeywordPickup(item, keyword)) {
                filterPickupData.add(item);
            }
        }

        tblvPickupSalesOrdersList.setItems(filterPickupData);
        calculatePickupTotalAmount();
    }

    // Search keyword
    private boolean matchesKeywordPickup(DispatchMngtPickupSalesOrdersDTO item, String keyword) {
        String lowerCaseKeyword = keyword.toLowerCase();
        return
                item.getInvoice_no().toLowerCase().contains(lowerCaseKeyword) ||
                        item.getInvoice_date().toLowerCase().contains(lowerCaseKeyword) ||
                        item.getCustomer_name().toLowerCase().contains(lowerCaseKeyword) ||
                        item.getNarration().toLowerCase().contains(lowerCaseKeyword) ||
                        item.getOrder_status().toLowerCase().contains(lowerCaseKeyword) ||
                        item.getBill_amount().toLowerCase().contains(lowerCaseKeyword) ||
                        item.getItem_count().toLowerCase().contains(lowerCaseKeyword);
    }
    //    Pickup End


    //    Packing Start
    private void fetchPackingAllSalesChallanList(String searchKey) {
        DispatchManagementListLogger.info("fetch Packing Dispatch Management Sales Challan List : DispatchManagementController");

        APIClient apiClient = null;

        try {
            DispatchManagementListLogger.info("fetch Packing Dispatch Management Sales Challan List : DispatchManagementController");
            Map<String, String> body = new HashMap<>();
            // HttpResponse<String> response = APIClient.getRequest(EndPoints.DISPATCH_MANAGEMENT_PackingSalesChallan_LIST);
            apiClient = new APIClient("get_all_pur_orders", "", RequestType.GET);
            apiClient.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
                @Override
                public void handle(WorkerStateEvent workerStateEvent) {
                    // JsonObject responseBody = new Gson().fromJson(response.body(), JsonObject.class);
                    jsonObject = new Gson().fromJson(workerStateEvent.getSource().getValue().toString(), JsonObject.class);
                   System.out.println("Response : " + jsonObject);
                    ObservableList<DispatchMngtPackingSalesChallanDTO> observableList = FXCollections.observableArrayList();
                    if (jsonObject.get("responseStatus").getAsInt() == 200) {
                        JsonArray responseList = jsonObject.getAsJsonArray("data");
//                        System.out.println("DispatchManagementController---> " + responseList);
                        if (responseList.size() > 0) {
//                            System.out.println("DispatchManagementController---> " + responseList);
                            tblvPackingSalesChallanList.setVisible(true);
                            for (JsonElement element : responseList) {
//                            System.out.println("DispatchManagementController---> " + responseList);
                                JsonObject item = element.getAsJsonObject();
                                String order_id = item.get("order_id") != null ? item.get("order_id").getAsString() : "";
                                String invoice_no = item.get("bill_no") != null ? item.get("bill_no").getAsString() : "";
                                String invoice_date = item.get("bill_date") != null ? item.get("bill_date").getAsString() : "";
                                String customer_name = item.get("debtors_name") != null ? item.get("debtors_name").getAsString() : "";
                                String narration = item.get("narration") != null ? item.get("narration").getAsString() : "";
                                String order_status = item.get("orderStatus").isJsonNull() ? "" : item.get("orderStatus").getAsString();
                                String bill_amount = item.get("totalamt") != null ? item.get("totalamt").getAsString() : "";
                                String item_count = item.get("product_count") != null ? item.get("product_count").getAsString() : "";

//                                System.out.println(order_id + invoice_no + invoice_date + customer_name + narration + order_status + bill_amount + item_count);

                                if(order_status.equalsIgnoreCase("Ready For Delivery")){
                                    observableList.add(new DispatchMngtPackingSalesChallanDTO(order_id, invoice_no, invoice_date, customer_name, narration, order_status, bill_amount, item_count));
                                }
                            }

                            tblPackingSalesChallanInvoiceNo.setCellValueFactory(new PropertyValueFactory<>("invoice_no"));
                            tblPackingSalesChallanInvoiceDate.setCellValueFactory(new PropertyValueFactory<>("invoice_date"));
                            tblPackingSalesChallanCustomer.setCellValueFactory(new PropertyValueFactory<>("customer_name"));
                            tblPackingSalesChallanNarration.setCellValueFactory(new PropertyValueFactory<>("narration"));
                            tblPackingSalesChallanStatus.setCellValueFactory(new PropertyValueFactory<>("order_status"));
                            tblPackingSalesChallanBillAmount.setCellValueFactory(new PropertyValueFactory<>("bill_amount"));
                            tblPackingSalesChallanItem.setCellValueFactory(new PropertyValueFactory<>("item_count"));

                            tblvPackingSalesChallanList.setItems(observableList);
                            originalDataPacking = observableList;
                            packingListCount = originalDataPacking.size();
                            tbsPacking.setText("(" + packingListCount + ")");

                            DispatchManagementListLogger.debug("Success in Displaying Pickup Dispatch Management Sales Orders List : DispatchManagementController");
                        } else {
                            tblvPackingSalesChallanList.getItems().clear();
                            DispatchManagementListLogger.debug("Packing Dispatch Management Sales Challan List ResponseObject is null : DispatchManagementController");
                        }
                    } else {
                        tblvPackingSalesChallanList.getItems().clear();
                        DispatchManagementListLogger.debug("Error in response of Packing Dispatch Management Sales Challan List : DispatchManagementController");
                    }
                }
            });

            apiClient.setOnCancelled(new EventHandler<WorkerStateEvent>() {
                @Override
                public void handle(WorkerStateEvent workerStateEvent) {
                    DispatchManagementListLogger.error("Network API cancelled in fetchPackingAllSalesChallanList()" + workerStateEvent.getSource().getValue().toString());

                }
            });

            apiClient.setOnFailed(new EventHandler<WorkerStateEvent>() {
                @Override
                public void handle(WorkerStateEvent workerStateEvent) {
                    DispatchManagementListLogger.error("Network API failed in fetchPackingAllSalesChallanList()" + workerStateEvent.getSource().getValue().toString());
                }
            });
            apiClient.start();
            DispatchManagementListLogger.debug("Get Packing Dispatch Management Sales Challan Data End...");
        } catch (Exception e) {
            DispatchManagementListLogger.error("Packing Dispatch Management Sales Challan Error is " + e.getMessage());
            e.printStackTrace();
        } finally {
            apiClient = null;
        }
    }

    // Details
    private void handlePackingCheckbox(Long selectedIndex) {


              System.out.println("Selected Index "+ selectedIndex);
        DispatchManagementListLogger.info("fetch Packing Dispatch Management Sales Challan Details : DispatchManagementController");

        APIClient apiClient = null;

        try {
            DispatchManagementListLogger.info("fetch Packing Dispatch Management Sales Challan Details : DispatchManagementController");
            Map<String, String> body = new HashMap<>();
            body.put("id", String.valueOf(selectedIndex));
//            System.out.println("body ----------->  " + body);
            String requestBody = Globals.mapToStringforFormData(body);
//            HttpResponse<String> response = APIClient.postFormDataRequest(requestBody, EndPoints.DISPATCH_MANAGEMENT_PackingSalesChallan_DETAILS);
            apiClient = new APIClient(EndPoints.PURCHASE_ORDER_GET_BY_ID, requestBody, RequestType.FORM_DATA);
            apiClient.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
                @Override
                public void handle(WorkerStateEvent workerStateEvent) {
                    // JsonObject responseBody = new Gson().fromJson(response.body(), JsonObject.class);
                    jsonObject = new Gson().fromJson(workerStateEvent.getSource().getValue().toString(), JsonObject.class);
//                    System.out.println("DISPATCH_MANAGEMENT_PackingSalesChallan_DETAILS" + jsonObject);
                    ObservableList<DispatchMngtPackingSalesChallanDetailsDTO> observableList = FXCollections.observableArrayList();
                    if (jsonObject.get("responseStatus").getAsInt() == 200) {
                        JsonArray responseList = jsonObject.getAsJsonArray("row");
//                        System.out.println("DispatchManagementController---> " + responseList);
                        if (responseList.size() > 0) {
//                            System.out.println("DispatchManagementController111---> " + responseList);
                            tblvPackingSalesChallanDetails.setVisible(true);

                            int index = 1;
                            for (JsonElement element : responseList) {
//                                System.out.println("DispatchManagementController222---> " + responseList);
                                JsonObject item = element.getAsJsonObject();
                                String description = item.get("product_name") != null ? item.get("product_name").getAsString() : "";
                                String batch = item.get("batch_no") != null ? item.get("batch_no").getAsString() : "";
                                String qty = item.get("qty") != null ? item.get("qty").getAsString() : "";
                                String price = item.get("total_amt") != null ? item.get("total_amt").getAsString() : "";


                                observableList.add(new DispatchMngtPackingSalesChallanDetailsDTO(String.valueOf(index++), description, batch, qty, price));
                            }


                            tblPackingSalesChallanDetailSrNo.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getId()));
                            tblPackingSalesChallanDetailDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
                            tblPackingSalesChallanDetailBatch.setCellValueFactory(new PropertyValueFactory<>("batch"));
                            tblPackingSalesChallanDetailQty.setCellValueFactory(new PropertyValueFactory<>("qty"));
                            tblPackingSalesChallanDetailPrice.setCellValueFactory(new PropertyValueFactory<>("price"));

                            tblvPackingSalesChallanDetails.setItems(observableList);
                            originalDataPackingDetails = observableList;
                            calculatePackingTotalAmount();
                            DispatchManagementListLogger.debug("Success in Displaying Packing Dispatch Management Sales Challan Details : DispatchManagementController");
                        } else {
                            tblvPackingSalesChallanDetails.getItems().clear();
                            calculatePackingTotalAmount();
                            DispatchManagementListLogger.debug("Packing Dispatch Management Sales Challan Details ResponseObject is null : DispatchManagementController");
                        }
                    } else {
                        tblvPackingSalesChallanDetails.getItems().clear();
                        calculatePackingTotalAmount();
                        DispatchManagementListLogger.debug("Error in response of Packing Dispatch Management Sales Challan Details : DispatchManagementController");
                    }
                }
            });
            apiClient.setOnCancelled(new EventHandler<WorkerStateEvent>() {
                @Override
                public void handle(WorkerStateEvent workerStateEvent) {
                    DispatchManagementListLogger.error("Network API cancelled in handlePackingCheckbox()" + workerStateEvent.getSource().getValue().toString());

                }
            });

            apiClient.setOnFailed(new EventHandler<WorkerStateEvent>() {
                @Override
                public void handle(WorkerStateEvent workerStateEvent) {
                    DispatchManagementListLogger.error("Network API failed in handlePackingCheckbox()" + workerStateEvent.getSource().getValue().toString());
                }
            });
            apiClient.start();
            DispatchManagementListLogger.debug("Get Packing Dispatch Management Sales Challan Data End...");
        } catch (Exception e) {
            DispatchManagementListLogger.error("Packing Dispatch Management Sales Challan Details Error is " + e.getMessage());
            e.printStackTrace();
        } finally {
            apiClient = null;
        }
    }

    // calculations
    private void calculatePackingTotalAmount() {
        ObservableList<DispatchMngtPackingSalesChallanDetailsDTO> filteredData = tblvPackingSalesChallanDetails.getItems();
        // Calculate the Totals
        double totalQty = 0.0;
        double totalPrice = 0.0;
        for (DispatchMngtPackingSalesChallanDetailsDTO item : filteredData) {
            totalQty += Double.parseDouble(item.getQty().isEmpty() ? "0.0" : item.getQty());
            totalPrice += Double.parseDouble(item.getPrice().isEmpty() ? "0.0" : item.getPrice());
        }
        // Update Labels in the FXML
        lblPackingTotalQty.setText(String.valueOf(totalQty));
        lblPackingTotalPrice.setText(String.valueOf(totalPrice));
    }

    // Search
    private void filterPackingData(String keyword) {
        ObservableList<DispatchMngtPackingSalesChallanDTO> filterPackingData = FXCollections.observableArrayList();

        DispatchManagementListLogger.error("Search Packing Dispatch Management Sales Challan List in DispatchManagementController");
        if (keyword.isEmpty()) {
            tblvPackingSalesChallanList.setItems(originalDataPacking);
            return;
        }

        for (DispatchMngtPackingSalesChallanDTO item : originalDataPacking) {
            if (matchesKeywordPacking(item, keyword)) {
                filterPackingData.add(item);
            }
        }

        tblvPackingSalesChallanList.setItems(filterPackingData);
        calculatePackingTotalAmount();
    }

    // Search keyword
    private boolean matchesKeywordPacking(DispatchMngtPackingSalesChallanDTO item, String keyword) {
        String lowerCaseKeyword = keyword.toLowerCase();
        return
                item.getInvoice_no().toLowerCase().contains(lowerCaseKeyword) ||
                        item.getInvoice_date().toLowerCase().contains(lowerCaseKeyword) ||
                        item.getCustomer_name().toLowerCase().contains(lowerCaseKeyword) ||
                        item.getNarration().toLowerCase().contains(lowerCaseKeyword) ||
                        item.getOrder_status().toLowerCase().contains(lowerCaseKeyword) ||
                        item.getBill_amount().toLowerCase().contains(lowerCaseKeyword) ||
                        item.getItem_count().toLowerCase().contains(lowerCaseKeyword);
    }
    //    Packing End

    //    ReadyFD Start
    private void fetchReadyFDAllSalesInvoiceList(String searchKey) {
        DispatchManagementListLogger.info("fetch ReadyFD Dispatch Management Sales Invoice List : DispatchManagementController");

        APIClient apiClient = null;

        try {
            DispatchManagementListLogger.info("fetch ReadyFD Dispatch Management Sales Invoice List : DispatchManagementController");
            Map<String, String> body = new HashMap<>();
            // HttpResponse<String> response = APIClient.getRequest(EndPoints.DISPATCH_MANAGEMENT_ReadyFDSalesInvoice_LIST);
            apiClient = new APIClient("get_all_pur_orders", "", RequestType.GET);
            apiClient.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
                @Override
                public void handle(WorkerStateEvent workerStateEvent) {
                    // JsonObject responseBody = new Gson().fromJson(response.body(), JsonObject.class);
                    jsonObject = new Gson().fromJson(workerStateEvent.getSource().getValue().toString(), JsonObject.class);
//                    System.out.println("DISPATCH_MANAGEMENT_ReadyFDSalesInvoice_LIST" + jsonObject);
                    ObservableList<DispatchMngtReadyFDSalesInvoiceDTO> observableList = FXCollections.observableArrayList();
                    if (jsonObject.get("responseStatus").getAsInt() == 200) {
                        JsonArray responseList = jsonObject.getAsJsonArray("data");
//                        System.out.println("DispatchManagementController---> " + responseList);

                        if (responseList.size() > 0) {
//                            System.out.println("DispatchManagementController---> " + responseList);
                            tblvReadyFDSalesInvoiceList.setVisible(true);
                            observableList.clear();
                            for (JsonElement element : responseList) {
//                            System.out.println("DispatchManagementController---> " + responseList);
                                JsonObject item = element.getAsJsonObject();
                                String order_id = item.get("order_id") != null ? item.get("order_id").getAsString() : "";
                                String invoice_no = item.get("bill_no") != null ? item.get("bill_no").getAsString() : "";
                                String invoice_date = item.get("bill_date") != null ? item.get("bill_date").getAsString() : "";
                                String customer_name = item.get("debtors_name") != null ? item.get("debtors_name").getAsString() : "";
                                String narration = item.get("narration") != null ? item.get("narration").getAsString() : "";
                                String order_status = item.get("orderStatus").isJsonNull() ? "" : item.get("orderStatus").getAsString();
                                String bill_amount = item.get("totalamt") != null ? item.get("totalamt").getAsString() : "";
                                String item_count = item.get("product_count") != null ? item.get("product_count").getAsString() : "";

//                                System.out.println(order_id + invoice_no + invoice_date + customer_name + narration + order_status + bill_amount + item_count);
                                if (order_status.equalsIgnoreCase("delivered")) {
                                    observableList.add(new DispatchMngtReadyFDSalesInvoiceDTO(order_id, invoice_no, invoice_date, customer_name, narration, order_status, bill_amount, item_count));
                                }
                            }

                            tblReadyFDSalesInvoiceInvoiceNo.setCellValueFactory(new PropertyValueFactory<>("invoice_no"));
                            tblReadyFDSalesInvoiceInvoiceDate.setCellValueFactory(new PropertyValueFactory<>("invoice_date"));
                            tblReadyFDSalesInvoiceCustomer.setCellValueFactory(new PropertyValueFactory<>("customer_name"));
                            tblReadyFDSalesInvoiceNarration.setCellValueFactory(new PropertyValueFactory<>("narration"));
                            tblReadyFDSalesInvoiceStatus.setCellValueFactory(new PropertyValueFactory<>("order_status"));
                            tblReadyFDSalesInvoiceBillAmount.setCellValueFactory(new PropertyValueFactory<>("bill_amount"));
                            tblReadyFDSalesInvoiceItem.setCellValueFactory(new PropertyValueFactory<>("item_count"));

                            tblvReadyFDSalesInvoiceList.setItems(observableList);
                            originalDataReadyFD = observableList;
                            tblvReadyFDSalesInvoiceList.refresh();
                            readyFDListCount = originalDataReadyFD.size();
                            tbsReadyFD.setText("(" + readyFDListCount + ")");

                            DispatchManagementListLogger.debug("Success in Displaying Ready For Deliver Dispatch Management Sales Invoice List : DispatchManagementController");
                        } else {
                            tblvReadyFDSalesInvoiceList.getItems().clear();
                            DispatchManagementListLogger.debug("Ready For Deliver Dispatch Management Sales Invoice List ResponseObject is null : DispatchManagementController");
                        }
                    } else {
                        tblvReadyFDSalesInvoiceList.getItems().clear();
                        DispatchManagementListLogger.debug("Error in response of Ready For Deliver Dispatch Management Sales Invoice List : DispatchManagementController");
                    }
                }
            });

            apiClient.setOnCancelled(new EventHandler<WorkerStateEvent>() {
                @Override
                public void handle(WorkerStateEvent workerStateEvent) {
                    DispatchManagementListLogger.error("Network API cancelled in fetchReadyFDAllSalesInvoiceList()" + workerStateEvent.getSource().getValue().toString());

                }
            });

            apiClient.setOnFailed(new EventHandler<WorkerStateEvent>() {
                @Override
                public void handle(WorkerStateEvent workerStateEvent) {
                    DispatchManagementListLogger.error("Network API failed in fetchReadyFDAllSalesInvoiceList()" + workerStateEvent.getSource().getValue().toString());
                }
            });
            apiClient.start();
            DispatchManagementListLogger.debug("Get Ready For Deliver Dispatch Management Sales Invoice Data End...");
        } catch (Exception e) {
            DispatchManagementListLogger.error("Ready For Deliver Dispatch Management Sales Invoice Error is " + e.getMessage());
            e.printStackTrace();
        } finally {
            apiClient = null;
        }
    }

    // Details
    private void handleReadyFDCheckbox(Long selectedIndex) {
        //        System.out.println("Selected Index "+ selectedIndex);
        DispatchManagementListLogger.info("fetch ReadyFD Dispatch Management Sales Invoice Details : DispatchManagementController");

        APIClient apiClient = null;

        try {
            DispatchManagementListLogger.info("fetch ReadyFD Dispatch Management Sales Invoice Details : DispatchManagementController");
            Map<String, String> body = new HashMap<>();
            body.put("id", String.valueOf(selectedIndex));
//            System.out.println("body ----------->  " + body);
            String requestBody = Globals.mapToStringforFormData(body);
//            HttpResponse<String> response = APIClient.postFormDataRequest(requestBody, EndPoints.DISPATCH_MANAGEMENT_ReadyFDSalesInvoice_DETAILS);
            apiClient = new APIClient(EndPoints.DISPATCH_MANAGEMENT_ReadyFDSalesInvoice_DETAILS, requestBody, RequestType.FORM_DATA);
            apiClient.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
                @Override
                public void handle(WorkerStateEvent workerStateEvent) {
                    // JsonObject responseBody = new Gson().fromJson(response.body(), JsonObject.class);
                    jsonObject = new Gson().fromJson(workerStateEvent.getSource().getValue().toString(), JsonObject.class);
//                    System.out.println("DISPATCH_MANAGEMENT_ReadyFDSalesInvoice_DETAILS" + jsonObject);
                    ObservableList<DispatchMngtReadyFDSalesInvoiceDetailsDTO> observableList = FXCollections.observableArrayList();
                    if (jsonObject.get("responseStatus").getAsInt() == 200) {
                        JsonArray responseList = jsonObject.getAsJsonArray("row");
//                        System.out.println("DispatchManagementController---> " + responseList);
                        if (responseList.size() > 0) {
//                            System.out.println("DispatchManagementController111---> " + responseList);
                            tblvReadyFDSalesInvoiceDetails.setVisible(true);

                            int index = 1;
                            for (JsonElement element : responseList) {
//                                System.out.println("DispatchManagementController222---> " + responseList);
                                JsonObject item = element.getAsJsonObject();
                                String description = item.get("product_name") != null ? item.get("product_name").getAsString() : "";
                                String batch = item.get("batch_no") != null ? item.get("batch_no").getAsString() : "";
                                String qty = item.get("qty") != null ? item.get("qty").getAsString() : "";
                                String price = item.get("total_amt") != null ? item.get("total_amt").getAsString() : "";

                                observableList.add(new DispatchMngtReadyFDSalesInvoiceDetailsDTO(String.valueOf(index++), description, batch, qty, price));
                            }

                            tblReadyFDSalesInvoiceSrNo.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getId()));
                            tblReadyFDSalesInvoiceDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
                            tblReadyFDSalesInvoiceBatch.setCellValueFactory(new PropertyValueFactory<>("batch"));
                            tblReadyFDSalesInvoiceQty.setCellValueFactory(new PropertyValueFactory<>("qty"));
                            tblReadyFDSalesInvoicePrice.setCellValueFactory(new PropertyValueFactory<>("price"));

                            tblvReadyFDSalesInvoiceDetails.setItems(observableList);
                            originalDataReadyFDDetails = observableList;
                            calculateReadyFDTotalAmount();
                            DispatchManagementListLogger.debug("Success in Displaying ReadyFD Dispatch Management Sales Invoice Details : DispatchManagementController");
                        } else {
                            tblvReadyFDSalesInvoiceDetails.getItems().clear();
                            calculateReadyFDTotalAmount();
                            DispatchManagementListLogger.debug("ReadyFD Dispatch Management Sales Invoice Details ResponseObject is null : DispatchManagementController");
                        }
                    } else {
                        tblvReadyFDSalesInvoiceDetails.getItems().clear();
                        calculateReadyFDTotalAmount();
                        DispatchManagementListLogger.debug("Error in response of ReadyFD Dispatch Management Sales Invoice Details : DispatchManagementController");
                    }
                }
            });
            apiClient.setOnCancelled(new EventHandler<WorkerStateEvent>() {
                @Override
                public void handle(WorkerStateEvent workerStateEvent) {
                    DispatchManagementListLogger.error("Network API cancelled in handleReadyFDCheckbox()" + workerStateEvent.getSource().getValue().toString());

                }
            });

            apiClient.setOnFailed(new EventHandler<WorkerStateEvent>() {
                @Override
                public void handle(WorkerStateEvent workerStateEvent) {
                    DispatchManagementListLogger.error("Network API failed in handleReadyFDCheckbox()" + workerStateEvent.getSource().getValue().toString());
                }
            });
            apiClient.start();
            DispatchManagementListLogger.debug("Get ReadyFD Dispatch Management Sales Invoice Data End...");
        } catch (Exception e) {
            DispatchManagementListLogger.error("ReadyFD Dispatch Management Sales Invoice Details Error is " + e.getMessage());
            e.printStackTrace();
        } finally {
            apiClient = null;
        }
    }

    // calculations
    private void calculateReadyFDTotalAmount() {
        ObservableList<DispatchMngtReadyFDSalesInvoiceDetailsDTO> filteredData = tblvReadyFDSalesInvoiceDetails.getItems();
        // Calculate the Totals
        double totalQty = 0.0;
        double totalPrice = 0.0;
        for (DispatchMngtReadyFDSalesInvoiceDetailsDTO item : filteredData) {
            totalQty += Double.parseDouble(item.getQty().isEmpty() ? "0.0" : item.getQty());
            totalPrice += Double.parseDouble(item.getPrice().isEmpty() ? "0.0" : item.getPrice());
        }
        // Update Labels in the FXML
        lblReadyFDTotalQty.setText(String.valueOf(totalQty));
        lblReadyFDTotalPrice.setText(String.valueOf(totalPrice));
    }

    // Search
    private void filterReadyFDData(String keyword) {
        ObservableList<DispatchMngtReadyFDSalesInvoiceDTO> filterReadyFDData = FXCollections.observableArrayList();

        DispatchManagementListLogger.error("Search ReadyFD Dispatch Management Sales Invoice List in DispatchManagementController");
        if (keyword.isEmpty()) {
            tblvReadyFDSalesInvoiceList.setItems(originalDataReadyFD);
            return;
        }

        for (DispatchMngtReadyFDSalesInvoiceDTO item : originalDataReadyFD) {
            if (matchesKeywordReadyFD(item, keyword)) {
                filterReadyFDData.add(item);
            }
        }

        tblvReadyFDSalesInvoiceList.setItems(filterReadyFDData);
        calculateReadyFDTotalAmount();
    }

    // Search keyword
    private boolean matchesKeywordReadyFD(DispatchMngtReadyFDSalesInvoiceDTO item, String keyword) {
        String lowerCaseKeyword = keyword.toLowerCase();
        return
                item.getInvoice_no().toLowerCase().contains(lowerCaseKeyword) ||
                        item.getInvoice_date().toLowerCase().contains(lowerCaseKeyword) ||
                        item.getCustomer_name().toLowerCase().contains(lowerCaseKeyword) ||
                        item.getNarration().toLowerCase().contains(lowerCaseKeyword) ||
                        item.getOrder_status().toLowerCase().contains(lowerCaseKeyword) ||
                        item.getBill_amount().toLowerCase().contains(lowerCaseKeyword) ||
                        item.getItem_count().toLowerCase().contains(lowerCaseKeyword);
    }
    //    ReadyFD End

    //    Delivered Start
    private void fetchDeliveredAllSalesInvoiceList(String searchKey) {
        DispatchManagementListLogger.info("fetch Delivered Dispatch Management Sales Invoice List : DispatchManagementController");

        APIClient apiClient = null;

        try {
            DispatchManagementListLogger.info("fetch Delivered Dispatch Management Sales Invoice List : DispatchManagementController");
            Map<String, String> body = new HashMap<>();
            // HttpResponse<String> response = APIClient.getRequest(EndPoints.DISPATCH_MANAGEMENT_DeliveredSalesInvoice_LIST);
            apiClient = new APIClient(EndPoints.DISPATCH_MANAGEMENT_DeliveredSalesInvoice_LIST, "", RequestType.GET);
            apiClient.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
                @Override
                public void handle(WorkerStateEvent workerStateEvent) {
                    // JsonObject responseBody = new Gson().fromJson(response.body(), JsonObject.class);
                    jsonObject = new Gson().fromJson(workerStateEvent.getSource().getValue().toString(), JsonObject.class);
//                    System.out.println("DISPATCH_MANAGEMENT_DeliveredSalesInvoice_LIST" + jsonObject);
                    ObservableList<DispatchMngtDeliveredSalesInvoiceDTO> observableList = FXCollections.observableArrayList();
                    if (jsonObject.get("responseStatus").getAsInt() == 200) {
                        JsonArray responseList = jsonObject.getAsJsonArray("data");
//                        System.out.println("DispatchManagementController---> " + responseList);
                        if (responseList.size() > 0) {
//                            System.out.println("DispatchManagementController---> " + responseList);
                            tblvDeliveredSalesInvoiceList.setVisible(true);
                            for (JsonElement element : responseList) {
//                            System.out.println("DispatchManagementController---> " + responseList);
                                JsonObject item = element.getAsJsonObject();
                                String order_id = item.get("invoice_id") != null ? item.get("invoice_id").getAsString() : "";
                                String invoice_no = item.get("invoice_no") != null ? item.get("invoice_no").getAsString() : "";
                                String invoice_date = item.get("invoice_date") != null ? item.get("invoice_date").getAsString() : "";
                                String customer_name = item.get("sundry_debtor_name") != null ? item.get("sundry_debtor_name").getAsString() : "";
                                String narration = item.get("narration") != null ? item.get("narration").getAsString() : "";
                                String order_status = item.get("orderStatus") != null ? item.get("orderStatus").getAsString() : "";
                                String bill_amount = item.get("total_amount") != null ? item.get("total_amount").getAsString() : "";
                                String item_count = item.get("product_count") != null ? item.get("product_count").getAsString() : "";

//                                System.out.println(order_id + invoice_no + invoice_date + customer_name + narration + order_status + bill_amount + item_count);
                                observableList.add(new DispatchMngtDeliveredSalesInvoiceDTO(order_id, invoice_no, invoice_date, customer_name, narration, order_status, bill_amount, item_count));
                            }

                            tblDeliveredSalesInvoiceInvoiceNo.setCellValueFactory(new PropertyValueFactory<>("invoice_no"));
                            tblDeliveredSalesInvoiceInvoiceDate.setCellValueFactory(new PropertyValueFactory<>("invoice_date"));
                            tblDeliveredSalesInvoiceCustomer.setCellValueFactory(new PropertyValueFactory<>("customer_name"));
                            tblDeliveredSalesInvoiceNarration.setCellValueFactory(new PropertyValueFactory<>("narration"));
                            tblDeliveredSalesInvoiceStatus.setCellValueFactory(new PropertyValueFactory<>("order_status"));
                            tblDeliveredSalesInvoiceBillAmount.setCellValueFactory(new PropertyValueFactory<>("bill_amount"));
                            tblDeliveredSalesInvoiceItem.setCellValueFactory(new PropertyValueFactory<>("item_count"));

                            tblvDeliveredSalesInvoiceList.setItems(observableList);
                            originalDataDelivered = observableList;
                            deliveredListCount = originalDataDelivered.size();
                            tbsDelivered.setText("(" + deliveredListCount + ")");

                            DispatchManagementListLogger.debug("Success in Displaying Delivered Dispatch Management Sales Invoice List : DispatchManagementController");
                        } else {
                            tblvDeliveredSalesInvoiceList.getItems().clear();
                            DispatchManagementListLogger.debug("Delivered Dispatch Management Sales Invoice List ResponseObject is null : DispatchManagementController");
                        }
                    } else {
                        tblvDeliveredSalesInvoiceList.getItems().clear();
                        DispatchManagementListLogger.debug("Error in response of Delivered Dispatch Management Sales Invoice List : DispatchManagementController");
                    }
                }
            });

            apiClient.setOnCancelled(new EventHandler<WorkerStateEvent>() {
                @Override
                public void handle(WorkerStateEvent workerStateEvent) {
                    DispatchManagementListLogger.error("Network API cancelled in fetchDeliveredAllSalesInvoiceList()" + workerStateEvent.getSource().getValue().toString());

                }
            });

            apiClient.setOnFailed(new EventHandler<WorkerStateEvent>() {
                @Override
                public void handle(WorkerStateEvent workerStateEvent) {
                    DispatchManagementListLogger.error("Network API failed in fetchDeliveredAllSalesInvoiceList()" + workerStateEvent.getSource().getValue().toString());
                }
            });
            apiClient.start();
            DispatchManagementListLogger.debug("Get Delivered Dispatch Management Sales Invoice Data End...");
        } catch (Exception e) {
            DispatchManagementListLogger.error("Delivered Dispatch Management Sales Invoice Error is " + e.getMessage());
            e.printStackTrace();
        } finally {
            apiClient = null;
        }
    }

    // Details
    private void handleDeliveredCheckbox(Long selectedIndex) {
        //        System.out.println("Selected Index "+ selectedIndex);
        DispatchManagementListLogger.info("fetch Delivered Dispatch Management Sales Invoice Details : DispatchManagementController");

        APIClient apiClient = null;

        try {
            DispatchManagementListLogger.info("fetch Delivered Dispatch Management Sales Invoice Details : DispatchManagementController");
            Map<String, String> body = new HashMap<>();
            body.put("id", String.valueOf(selectedIndex));
//            System.out.println("body ----------->  " + body);
            String requestBody = Globals.mapToStringforFormData(body);
//            HttpResponse<String> response = APIClient.postFormDataRequest(requestBody, EndPoints.DISPATCH_MANAGEMENT_DeliveredSalesInvoice_DETAILS);
            apiClient = new APIClient(EndPoints.DISPATCH_MANAGEMENT_DeliveredSalesInvoice_DETAILS, requestBody, RequestType.FORM_DATA);
            apiClient.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
                @Override
                public void handle(WorkerStateEvent workerStateEvent) {
                    // JsonObject responseBody = new Gson().fromJson(response.body(), JsonObject.class);
                    jsonObject = new Gson().fromJson(workerStateEvent.getSource().getValue().toString(), JsonObject.class);
//                    System.out.println("DISPATCH_MANAGEMENT_DeliveredSalesInvoice_DETAILS" + jsonObject);
                    ObservableList<DispatchMngtDeliveredSalesInvoiceDetailsDTO> observableList = FXCollections.observableArrayList();
                    if (jsonObject.get("responseStatus").getAsInt() == 200) {
                        JsonArray responseList = jsonObject.getAsJsonArray("row");
//                        System.out.println("DispatchManagementController---> " + responseList);
                        if (responseList.size() > 0) {
//                            System.out.println("DispatchManagementController111---> " + responseList);
                            tblvDeliveredSalesInvoiceDetails.setVisible(true);


                            int index = 1;
                            for (JsonElement element : responseList) {
//                                System.out.println("DispatchManagementController222---> " + responseList);
                                JsonObject item = element.getAsJsonObject();
                                String description = item.get("product_name") != null ? item.get("product_name").getAsString() : "";
                                String batch = item.get("batch_no") != null ? item.get("batch_no").getAsString() : "";
                                String qty = item.get("qty") != null ? item.get("qty").getAsString() : "";
                                String price = item.get("total_amt") != null ? item.get("total_amt").getAsString() : "";

                                observableList.add(new DispatchMngtDeliveredSalesInvoiceDetailsDTO(String.valueOf(index++), description, batch, qty, price));
                            }

                            tblDeliveredSalesInvoiceSrNo.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getId()));
                            tblDeliveredSalesInvoiceDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
                            tblDeliveredSalesInvoiceBatch.setCellValueFactory(new PropertyValueFactory<>("batch"));
                            tblDeliveredSalesInvoiceQty.setCellValueFactory(new PropertyValueFactory<>("qty"));
                            tblDeliveredSalesInvoicePrice.setCellValueFactory(new PropertyValueFactory<>("price"));

                            tblvDeliveredSalesInvoiceDetails.setItems(observableList);
                            originalDataDeliveredDetails = observableList;
                            calculateDeliveredTotalAmount();
                            DispatchManagementListLogger.debug("Success in Displaying Delivered Dispatch Management Sales Invoice Details : DispatchManagementController");
                        } else {
                            tblvDeliveredSalesInvoiceDetails.getItems().clear();
                            calculateDeliveredTotalAmount();
                            DispatchManagementListLogger.debug("Delivered Dispatch Management Sales Invoice Details ResponseObject is null : DispatchManagementController");
                        }
                    } else {
                        tblvDeliveredSalesInvoiceDetails.getItems().clear();
                        calculateDeliveredTotalAmount();
                        DispatchManagementListLogger.debug("Error in response of Delivered Dispatch Management Sales Invoice Details : DispatchManagementController");
                    }
                }
            });
            apiClient.setOnCancelled(new EventHandler<WorkerStateEvent>() {
                @Override
                public void handle(WorkerStateEvent workerStateEvent) {
                    DispatchManagementListLogger.error("Network API cancelled in handleDeliveredCheckbox()" + workerStateEvent.getSource().getValue().toString());

                }
            });

            apiClient.setOnFailed(new EventHandler<WorkerStateEvent>() {
                @Override
                public void handle(WorkerStateEvent workerStateEvent) {
                    DispatchManagementListLogger.error("Network API failed in handleDeliveredCheckbox()" + workerStateEvent.getSource().getValue().toString());
                }
            });
            apiClient.start();
            DispatchManagementListLogger.debug("Get Displaying Dispatch Management Sales Invoice Data End...");
        } catch (Exception e) {
            DispatchManagementListLogger.error("Displaying Dispatch Management Sales Invoice Details Error is " + e.getMessage());
            e.printStackTrace();
        } finally {
            apiClient = null;
        }
    }

    // calculations
    private void calculateDeliveredTotalAmount() {
        ObservableList<DispatchMngtDeliveredSalesInvoiceDetailsDTO> filteredData = tblvDeliveredSalesInvoiceDetails.getItems();
        // Calculate the Totals
        double totalQty = 0.0;
        double totalPrice = 0.0;
        for (DispatchMngtDeliveredSalesInvoiceDetailsDTO item : filteredData) {
            totalQty += Double.parseDouble(item.getQty().isEmpty() ? "0.0" : item.getQty());
            totalPrice += Double.parseDouble(item.getPrice().isEmpty() ? "0.0" : item.getPrice());
        }
        // Update Labels in the FXML
        lblDeliveredTotalQty.setText(String.valueOf(totalQty));
        lblDeliveredTotalPrice.setText(String.valueOf(totalPrice));
    }

    // Search
    private void filterDeliveredData(String keyword) {
        ObservableList<DispatchMngtDeliveredSalesInvoiceDTO> filterDeliveredData = FXCollections.observableArrayList();

        DispatchManagementListLogger.error("Search Delivered Dispatch Management Sales Invoice List in DispatchManagementController");
        if (keyword.isEmpty()) {
            tblvDeliveredSalesInvoiceList.setItems(originalDataDelivered);
            return;
        }

        for (DispatchMngtDeliveredSalesInvoiceDTO item : originalDataDelivered) {
            if (matchesKeywordDelivered(item, keyword)) {
                filterDeliveredData.add(item);
            }
        }

        tblvDeliveredSalesInvoiceList.setItems(filterDeliveredData);
        calculateDeliveredTotalAmount();
    }

    // Search keyword
    private boolean matchesKeywordDelivered(DispatchMngtDeliveredSalesInvoiceDTO item, String keyword) {
        String lowerCaseKeyword = keyword.toLowerCase();
        return
                item.getInvoice_no().toLowerCase().contains(lowerCaseKeyword) ||
                        item.getInvoice_date().toLowerCase().contains(lowerCaseKeyword) ||
                        item.getCustomer_name().toLowerCase().contains(lowerCaseKeyword) ||
                        item.getNarration().toLowerCase().contains(lowerCaseKeyword) ||
                        item.getOrder_status().toLowerCase().contains(lowerCaseKeyword) ||
                        item.getBill_amount().toLowerCase().contains(lowerCaseKeyword) ||
                        item.getItem_count().toLowerCase().contains(lowerCaseKeyword);
    }
    //    Delivered End

    //    Returned Start
    private void fetchReturnedAllSalesInvoiceList(String searchKey) {
        DispatchManagementListLogger.info("fetch Returned Dispatch Management Sales Invoice List : DispatchManagementController");

        APIClient apiClient = null;

        try {
            DispatchManagementListLogger.info("fetch Returned Dispatch Management Sales Invoice List : DispatchManagementController");
            Map<String, String> body = new HashMap<>();
            // HttpResponse<String> response = APIClient.getRequest(EndPoints.DISPATCH_MANAGEMENT_ReturnedSalesInvoice_LIST);
            apiClient = new APIClient("get_all_pur_orders", "", RequestType.GET);
            apiClient.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
                @Override
                public void handle(WorkerStateEvent workerStateEvent) {
                    // JsonObject responseBody = new Gson().fromJson(response.body(), JsonObject.class);
                    jsonObject = new Gson().fromJson(workerStateEvent.getSource().getValue().toString(), JsonObject.class);
//                    System.out.println("DISPATCH_MANAGEMENT_ReturnedSalesInvoice_LIST" + jsonObject);
                    ObservableList<DispatchMngtReturnedSalesInvoiceDTO> observableList = FXCollections.observableArrayList();
                    if (jsonObject.get("responseStatus").getAsInt() == 200) {
                        JsonArray responseList = jsonObject.getAsJsonArray("data");
//                        System.out.println("DispatchManagementController---> " + responseList);
                        if (responseList.size() > 0) {
//                            System.out.println("DispatchManagementController---> " + responseList);
                            tblvReturnedSalesInvoiceList.setVisible(true);
                            for (JsonElement element : responseList) {
//                            System.out.println("DispatchManagementController---> " + responseList);
                                JsonObject item = element.getAsJsonObject();
                                String order_id = item.get("order_id") != null ? item.get("order_id").getAsString() : "";
                                String invoice_no = item.get("bill_no") != null ? item.get("bill_no").getAsString() : "";
                                String invoice_date = item.get("bill_date") != null ? item.get("bill_date").getAsString() : "";
                                String customer_name = item.get("debtors_name") != null ? item.get("debtors_name").getAsString() : "";
                                String narration = item.get("narration") != null ? item.get("narration").getAsString() : "";
                                String order_status = item.get("orderStatus").isJsonNull() ? "" : item.get("orderStatus").getAsString();
                                String bill_amount = item.get("totalamt") != null ? item.get("totalamt").getAsString() : "";
                                String item_count = item.get("product_count") != null ? item.get("product_count").getAsString() : "";
//                                System.out.println(order_id + invoice_no + invoice_date + customer_name + narration + order_status + bill_amount + item_count);
                                if(order_status.equalsIgnoreCase("delivered")){
                                observableList.add(new DispatchMngtReturnedSalesInvoiceDTO(order_id, invoice_no, invoice_date, customer_name, narration, order_status, bill_amount, item_count));
                            }
                                }

                            tblReturnedSalesInvoiceInvoiceNo.setCellValueFactory(new PropertyValueFactory<>("invoice_no"));
                            tblReturnedSalesInvoiceInvoiceDate.setCellValueFactory(new PropertyValueFactory<>("invoice_date"));
                            tblReturnedSalesInvoiceCustomer.setCellValueFactory(new PropertyValueFactory<>("customer_name"));
                            tblReturnedSalesInvoiceNarration.setCellValueFactory(new PropertyValueFactory<>("narration"));
                            tblReturnedSalesInvoiceStatus.setCellValueFactory(new PropertyValueFactory<>("order_status"));
                            tblReturnedSalesInvoiceBillAmount.setCellValueFactory(new PropertyValueFactory<>("bill_amount"));
                            tblReturnedSalesInvoiceItem.setCellValueFactory(new PropertyValueFactory<>("item_count"));

                            tblvReturnedSalesInvoiceList.setItems(observableList);
                            originalDataReturned = observableList;
                            returedListCount = originalDataReturned.size();
                            tbsRtnCnl.setText("(" + returedListCount + ")");

                            DispatchManagementListLogger.debug("Success in Displaying Returned Dispatch Management Sales Invoice List : DispatchManagementController");
                        } else {
                            tblvReturnedSalesInvoiceList.getItems().clear();
                            DispatchManagementListLogger.debug("Returned Dispatch Management Sales Invoice List ResponseObject is null : DispatchManagementController");
                        }
                    } else {
                        tblvReturnedSalesInvoiceList.getItems().clear();
                        DispatchManagementListLogger.debug("Error in response of Returned Dispatch Management Sales Invoice List : DispatchManagementController");
                    }
                }
            });

            apiClient.setOnCancelled(new EventHandler<WorkerStateEvent>() {
                @Override
                public void handle(WorkerStateEvent workerStateEvent) {
                    DispatchManagementListLogger.error("Network API cancelled in fetchReturnedAllSalesInvoiceList()" + workerStateEvent.getSource().getValue().toString());

                }
            });

            apiClient.setOnFailed(new EventHandler<WorkerStateEvent>() {
                @Override
                public void handle(WorkerStateEvent workerStateEvent) {
                    DispatchManagementListLogger.error("Network API failed in fetchReturnedAllSalesInvoiceList()" + workerStateEvent.getSource().getValue().toString());
                }
            });
            apiClient.start();
            DispatchManagementListLogger.debug("Get Returned Dispatch Management Sales Invoice Data End...");
        } catch (Exception e) {
            DispatchManagementListLogger.error("Returned Dispatch Management Sales Invoice Error is " + e.getMessage());
            e.printStackTrace();
        } finally {
            apiClient = null;
        }
    }

    // Details
    private void handleReturnedCheckbox(Long selectedIndex) {
        //        System.out.println("Selected Index "+ selectedIndex);
        DispatchManagementListLogger.info("fetch Returned Dispatch Management Sales Invoice Details : DispatchManagementController");

        APIClient apiClient = null;

        try {
            DispatchManagementListLogger.info("fetch Returned Dispatch Management Sales Invoice Details : DispatchManagementController");
            Map<String, String> body = new HashMap<>();
            body.put("id", String.valueOf(selectedIndex));
//            System.out.println("body ----------->  " + body);
            String requestBody = Globals.mapToStringforFormData(body);
//            HttpResponse<String> response = APIClient.postFormDataRequest(requestBody, EndPoints.DISPATCH_MANAGEMENT_ReturnedSalesInvoice_DETAILS);
            apiClient = new APIClient(EndPoints.PURCHASE_ORDER_GET_BY_ID, requestBody, RequestType.FORM_DATA);
            apiClient.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
                @Override
                public void handle(WorkerStateEvent workerStateEvent) {
                    // JsonObject responseBody = new Gson().fromJson(response.body(), JsonObject.class);
                    jsonObject = new Gson().fromJson(workerStateEvent.getSource().getValue().toString(), JsonObject.class);
//                    System.out.println("DISPATCH_MANAGEMENT_ReturnedSalesInvoice_DETAILS" + jsonObject);
                    ObservableList<DispatchMngtReturnedSalesInvoiceDetailsDTO> observableList = FXCollections.observableArrayList();
                    if (jsonObject.get("responseStatus").getAsInt() == 200) {
                        JsonArray responseList = jsonObject.getAsJsonArray("row");
//                        System.out.println("DispatchManagementController---> " + responseList);
                        if (responseList.size() > 0) {
//                            System.out.println("DispatchManagementController111---> " + responseList);
                            tblvReturnedSalesInvoiceDetails.setVisible(true);

                            int index = 1;
                            for (JsonElement element : responseList) {
//                                System.out.println("DispatchManagementController222---> " + responseList);
                                JsonObject item = element.getAsJsonObject();
                                String description = item.get("product_name") != null ? item.get("product_name").getAsString() : "";
                                String batch = item.get("batch_no") != null ? item.get("batch_no").getAsString() : "";
                                String qty = item.get("qty") != null ? item.get("qty").getAsString() : "";
                                String price = item.get("total_amt") != null ? item.get("total_amt").getAsString() : "";

                                observableList.add(new DispatchMngtReturnedSalesInvoiceDetailsDTO(String.valueOf(index++), description, batch, qty, price));
                            }

                            tblvReturnedSalesInvoiceSrNo.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getId()));
                            tblvReturnedSalesInvoiceDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
                            tblvReturnedSalesInvoiceBatch.setCellValueFactory(new PropertyValueFactory<>("batch"));
                            tblvReturnedSalesInvoiceQty.setCellValueFactory(new PropertyValueFactory<>("qty"));
                            tblvReturnedSalesInvoicePrice.setCellValueFactory(new PropertyValueFactory<>("price"));

                            tblvReturnedSalesInvoiceDetails.setItems(observableList);
                            originalDataReturnedDetails = observableList;
                            calculateReturnedTotalAmount();
                            DispatchManagementListLogger.debug("Success in Displaying Returned Dispatch Management Sales Invoice Details : DispatchManagementController");
                        } else {
                            tblvReturnedSalesInvoiceDetails.getItems().clear();
                            calculateReturnedTotalAmount();
                            DispatchManagementListLogger.debug("Returned Dispatch Management Sales Invoice Details ResponseObject is null : DispatchManagementController");
                        }
                    } else {
                        tblvReturnedSalesInvoiceDetails.getItems().clear();
                        calculateReturnedTotalAmount();
                        DispatchManagementListLogger.debug("Error in response of Returned Dispatch Management Sales Invoice Details : DispatchManagementController");
                    }
                }
            });
            apiClient.setOnCancelled(new EventHandler<WorkerStateEvent>() {
                @Override
                public void handle(WorkerStateEvent workerStateEvent) {
                    DispatchManagementListLogger.error("Network API cancelled in handleReturnedCheckbox()" + workerStateEvent.getSource().getValue().toString());

                }
            });

            apiClient.setOnFailed(new EventHandler<WorkerStateEvent>() {
                @Override
                public void handle(WorkerStateEvent workerStateEvent) {
                    DispatchManagementListLogger.error("Network API failed in handleReturnedCheckbox()" + workerStateEvent.getSource().getValue().toString());
                }
            });
            apiClient.start();
            DispatchManagementListLogger.debug("Get Displaying Returned Dispatch Management Sales Invoice Data End...");
        } catch (Exception e) {
            DispatchManagementListLogger.error("Displaying Returned Dispatch Management Sales Invoice Details Error is " + e.getMessage());
            e.printStackTrace();
        } finally {
            apiClient = null;
        }
    }

    // calculations
    private void calculateReturnedTotalAmount() {
        ObservableList<DispatchMngtReturnedSalesInvoiceDetailsDTO> filteredData = tblvReturnedSalesInvoiceDetails.getItems();
        // Calculate the Totals
        double totalQty = 0.0;
        double totalPrice = 0.0;
        for (DispatchMngtReturnedSalesInvoiceDetailsDTO item : filteredData) {
            totalQty += Double.parseDouble(item.getQty().isEmpty() ? "0.0" : item.getQty());
            totalPrice += Double.parseDouble(item.getPrice().isEmpty() ? "0.0" : item.getPrice());
        }
        // Update Labels in the FXML
        lblReturnedTotalQty.setText(String.valueOf(totalQty));
        lblReturnedTotalPrice.setText(String.valueOf(totalPrice));
    }

    // Search
    private void filterReturnedData(String keyword) {
        ObservableList<DispatchMngtReturnedSalesInvoiceDTO> filterReturnedData = FXCollections.observableArrayList();

        DispatchManagementListLogger.error("Search Returned Dispatch Management Sales Invoice List in DispatchManagementController");
        if (keyword.isEmpty()) {
            tblvReturnedSalesInvoiceList.setItems(originalDataReturned);
            return;
        }

        for (DispatchMngtReturnedSalesInvoiceDTO item : originalDataReturned) {
            if (matchesKeywordReturned(item, keyword)) {
                filterReturnedData.add(item);
            }
        }

        tblvReturnedSalesInvoiceList.setItems(filterReturnedData);
        calculateReturnedTotalAmount();
    }

    // Search keyword
    private boolean matchesKeywordReturned(DispatchMngtReturnedSalesInvoiceDTO item, String keyword) {
        String lowerCaseKeyword = keyword.toLowerCase();
        return
                item.getInvoice_no().toLowerCase().contains(lowerCaseKeyword) ||
                        item.getInvoice_date().toLowerCase().contains(lowerCaseKeyword) ||
                        item.getCustomer_name().toLowerCase().contains(lowerCaseKeyword) ||
                        item.getNarration().toLowerCase().contains(lowerCaseKeyword) ||
                        item.getOrder_status().toLowerCase().contains(lowerCaseKeyword) ||
                        item.getBill_amount().toLowerCase().contains(lowerCaseKeyword) ||
                        item.getItem_count().toLowerCase().contains(lowerCaseKeyword);
    }
    //    Returned End
}
