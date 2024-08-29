package com.opethic.genivis.controller.tranx_purchase;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.opethic.genivis.GenivisApplication;
import com.opethic.genivis.controller.account_entry.PaymentCreateController;
import com.opethic.genivis.controller.tranx_sales.SalesQuotToOrderController;
import com.opethic.genivis.controller.tranx_sales.SalesQuotationToChallan;
import com.opethic.genivis.dto.*;
import com.opethic.genivis.dto.reqres.product.Communicator;
import com.opethic.genivis.network.APIClient;
import com.opethic.genivis.network.EndPoints;
import com.opethic.genivis.network.RequestType;
import com.opethic.genivis.utils.*;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Screen;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URL;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.StringJoiner;

import static com.opethic.genivis.utils.FxmFileConstants.*;
import static com.opethic.genivis.utils.Globals.tabPane;

public class PurchaseChallanListController implements Initializable {

    @FXML
    public Button btnPurChallToInvoice;
    @FXML
    TableView<PurchaseChallanDTO> tblvPurChallLst;
    @FXML
    private TableColumn tblcPurChallLstChallanNo, tblcPurChallLstChallanDate, tblcPurChallLstRefNo, tblcPurChallLstPrint, tblcPurChallLstAction,
            tblcPurChallLstSupplierName, tblcPurChallLstNarration, tblcPurChallLstTaxable, tblcPurChallLstTax, tblcPurChallLstBillAmount;

    @FXML
    TableColumn<PurchaseChallanDTO, Boolean> tblcPurChallLstSelect;
    @FXML
    private TableColumn tblcPurChallLstTranxStatus;
    @FXML
    private TextField tftranxPurChallLstSearchField;
    @FXML
    private TextField tftranxPurChallLstToDate, tftranxPurChallLstFromDate;
    @FXML
    private Button btnPurChallLstCreatebtn;
    @FXML
    private HBox hbPurChallHeaderSect;
    @FXML
    private BorderPane spRootPurchaseChallanListPane;
    public static Integer purchaseChallanid = -1;

    String message = "";
    private JsonObject jsonObject = null;

    private Integer selectedIndex = 0;


    public static final Logger purChallanListLogger = LoggerFactory.getLogger(PurchaseChallanListController.class);

    //? Highlight the Record Start
    public static boolean isNewPurchaseChallanCreated = false; // Flag for new creation
    public static String editedPurchaseChallanId = null; // ID for edited Sales Order
    //? Highlight the Record End

    private ObservableList<PurchaseChallanDTO> originalData;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        ResponsiveWiseCssPicker();

        //todo : autofocus on TranxDate
        Platform.runLater(() -> tftranxPurChallLstSearchField.requestFocus());

        tftranxPurChallLstSearchField.textProperty().addListener((observable, oldValue, newValue) -> {
            filterData(newValue.trim());
        });

        //   Enter traversal
        initialEnterMethod();

        // tableView Responsive Function
        purChallListTableDesign();

        DateValidator.applyDateFormat(tftranxPurChallLstFromDate);
        DateValidator.applyDateFormat(tftranxPurChallLstToDate);

        // checkbox Select Checking
        selectCheckBoxCheck();

        // Tranx Status CSS color Applying
        tranxStatusCheck();

        // List of all Company Admins Api call
        fetchDataOfAllPurchaseChallans("");

        // search Functionality Method
//        searchFunctionality();

        // OnDoubleClick And Enter Press GO to Edit Page
        doubleClickAndEnterMethod();


        dateWiseFilterMethod();
        // Add a key event listener to the text field
        hbPurChallHeaderSect.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode() == KeyCode.DOWN) {
                tblvPurChallLst.getSelectionModel().selectFirst();
                tblvPurChallLst.requestFocus();
                event.consume();
            }
        });

        btnPurChallLstCreatebtn.setOnAction(event -> GlobalController.getInstance().addTabStatic(PURCHASE_CHALLAN_CREATE_SLUG, false));

        btnPurChallToInvoice.setOnAction(e -> {
            goToPurInvoice();
        });

    }

    //Search Function to Search in the Table
    private void filterData(String keyword) {
        ObservableList<PurchaseChallanDTO> filteredData = FXCollections.observableArrayList();
        purChallanListLogger.info("Search Sales Quotation invoice in SalesQuotationListController");
        if (keyword.isEmpty()) {
            tblvPurChallLst.setItems(originalData);
            return;
        }

        for (PurchaseChallanDTO item : originalData) {
            if (matchesKeyword(item, keyword)) {
                filteredData.add(item);
            }
        }

        tblvPurChallLst.setItems(filteredData);
    }

    //Search Function to Search in the Table for columns
    private boolean matchesKeyword(PurchaseChallanDTO item, String keyword) {
        String lowerCaseKeyword = keyword.toLowerCase();

        // Check if any of the columns contain the keyword
        return item.getSundry_creditor_name().toLowerCase().contains(lowerCaseKeyword) ||
                item.getInvoice_no().toLowerCase().contains(lowerCaseKeyword) ||
                item.getInvoice_date().toLowerCase().contains(lowerCaseKeyword) ||
                item.getTax_amt().toLowerCase().contains(lowerCaseKeyword) ||
                item.getTaxable_amt().toLowerCase().contains(lowerCaseKeyword) ||
                item.getTotal_amount().toLowerCase().contains(lowerCaseKeyword) ||
                item.getNarration().toLowerCase().contains(lowerCaseKeyword);

    }

    private void ResponsiveWiseCssPicker() {
        double height = Screen.getPrimary().getBounds().getHeight();
        double width = Screen.getPrimary().getBounds().getWidth();
        System.out.println("width" + width);
        if (width >= 992 && width <= 1024) {
            spRootPurchaseChallanListPane.getStylesheets().add(GenivisApplication.class.getResource("ui/css/TranxCommonCssStyles/tranxCommonCssStyle1.css").toExternalForm());
        } else if (width >= 1025 && width <= 1280) {
            spRootPurchaseChallanListPane.getStylesheets().add(GenivisApplication.class.getResource("ui/css/TranxCommonCssStyles/tranxCommonCssStyle2.css").toExternalForm());
        } else if (width >= 1281 && width <= 1368) {
            spRootPurchaseChallanListPane.getStylesheets().add(GenivisApplication.class.getResource("ui/css/TranxCommonCssStyles/tranxCommonCssStyle3.css").toExternalForm());
        } else if (width >= 1369 && width <= 1400) {
            spRootPurchaseChallanListPane.getStylesheets().add(GenivisApplication.class.getResource("ui/css/TranxCommonCssStyles/tranxCommonCssStyle4.css").toExternalForm());
        } else if (width >= 1401 && width <= 1440) {
            spRootPurchaseChallanListPane.getStylesheets().add(GenivisApplication.class.getResource("ui/css/TranxCommonCssStyles/tranxCommonCssStyle5.css").toExternalForm());
        } else if (width >= 1441 && width <= 1680) {
            spRootPurchaseChallanListPane.getStylesheets().add(GenivisApplication.class.getResource("ui/css/TranxCommonCssStyles/tranxCommonCssStyle6.css").toExternalForm());
        } else if (width >= 1681 && width <= 1920) {
            spRootPurchaseChallanListPane.getStylesheets().add(GenivisApplication.class.getResource("ui/css/TranxCommonCssStyles/tranxCommonCssStyle7.css").toExternalForm());
        }
    }


    private void initialEnterMethod() {
        spRootPurchaseChallanListPane.addEventHandler(KeyEvent.KEY_PRESSED, (KeyEvent event) -> {
            if (event.getCode() == KeyCode.ENTER) {
                if (event.getTarget() instanceof Button targetButton && targetButton.getText().equalsIgnoreCase("create")) {
                    targetButton.getText();
                } else {
                    purchaseChallanEditPage();
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
            }else if(event.getCode() == KeyCode.N && event.isControlDown()) {
                btnPurChallLstCreatebtn.fire();
            }else if (event.getCode() == KeyCode.E && event.isControlDown()) {
                purchaseChallanEditPage();
            }else if (event.getCode() == KeyCode.D && event.isControlDown()) {
                handleCtrlDPressed();
            }
        });
    }

    private void selectCheckBoxCheck() {
        tblvPurChallLst.setEditable(true);
        tblcPurChallLstSelect.setCellFactory(column -> new CheckBoxTableCell<>());
        tblcPurChallLstSelect.setCellValueFactory(cellData -> {
            PurchaseChallanDTO cellValue = cellData.getValue();
            BooleanProperty property = cellValue.is_row_selectedProperty();

            // Add listener to handler change
            property.addListener((observable, oldValue, newValue) -> cellValue.setIs_row_selected(newValue));
            findOutSelectedRow();
            if (cellValue.isIs_row_selected()) {
                if (cellValue.getPurchase_challan_status().equalsIgnoreCase("closed")) {

                    AlertUtility.AlertErrorTimeout(AlertUtility.alertTypeError, "Challan is Already Closed", e -> {
                        cellValue.setIs_row_selected(false);
                    });

                } else {
                    handleSelection(tblvPurChallLst.getItems());
                }
            }
            return property;

        });
    }

    private void tranxStatusCheck() {

        tblcPurChallLstTranxStatus.setCellValueFactory(new PropertyValueFactory<>("tblcPurChallLstTranxStatus"));
        tblcPurChallLstTranxStatus.setCellFactory(column -> {
            return new TableCell<PurchaseOrderDTO, String>() {
                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if (item == null || empty) {
                        setText(null);
                    } else {
                        setText(item);
                        setStyle("-fx-font-weight: bold; -fx-text-fill: white;");
                        if (item.equalsIgnoreCase("opened")) {
                            setStyle("-fx-font-weight: bold; -fx-text-fill: green;");
                        } else if (item.equalsIgnoreCase("closed")) {
                            setStyle("-fx-font-weight: bold; -fx-text-fill: red;");
                        }
                    }
                }
            };
        });
    }

//    private void searchFunctionality() {
//        tftranxPurChallLstSearchField.setOnKeyTyped(new EventHandler<KeyEvent>() {
//            @Override
//            public void handle(KeyEvent keyEvent) {
//                String searchKey = tftranxPurChallLstSearchField.getText().trim();
//                if (searchKey.length() >= 3) {
//                    fetchDataOfAllPurchaseChallans(searchKey);
//                } else if (searchKey.isEmpty()) {
//                    fetchDataOfAllPurchaseChallans("");
//                }
//            }
//        });
//
//    }

    private void doubleClickAndEnterMethod() {
        // OnDoubleClick In Edit Page of Particular Purchase Challan Screen
        tblvPurChallLst.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                purchaseChallanEditPage();
            }
        });

        // Add a key event listener to the TableView
//        tblvPurChallLst.addEventHandler(KeyEvent.KEY_PRESSED, event -> {
//            if (event.getCode() == KeyCode.ENTER) {
//                if (!tblvPurChallLst.getSelectionModel().isEmpty()) {
//                    PurchaseChallanDTO selectedItem = (PurchaseChallanDTO) tblvPurChallLst.getSelectionModel().getSelectedItem();
//                    purchaseChallanid = Integer.valueOf(selectedItem.getId());
//                    GlobalController.getInstance().addTabStatic(PURCHASE_CHALLAN_EDIT_SLUG, false);
//                    event.consume();
//                }
//            }
//        });
    }

    private void dateWiseFilterMethod() {
        tftranxPurChallLstToDate.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ENTER) {
                if (!tftranxPurChallLstFromDate.getText().isEmpty() && !tftranxPurChallLstToDate.getText().isEmpty()) {
                    fetchDataOfAllPurchaseChallans("");
                }
            }
        });
        tftranxPurChallLstFromDate.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ENTER) {
                if (!tftranxPurChallLstFromDate.getText().isEmpty() && !tftranxPurChallLstToDate.getText().isEmpty()) {
                    fetchDataOfAllPurchaseChallans("");
                }
            }
        });
    }

    public static Integer getPurchaseId() {
        return purchaseChallanid;
    }

    // purChallanList Table Responsive
    public void purChallListTableDesign() {
        tblvPurChallLst.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tblvPurChallLst.setEditable(true);
        tblcPurChallLstSelect.prefWidthProperty().bind(tblvPurChallLst.widthProperty().multiply(0.04));
        tblcPurChallLstChallanNo.prefWidthProperty().bind(tblvPurChallLst.widthProperty().multiply(0.08));
        tblcPurChallLstRefNo.prefWidthProperty().bind(tblvPurChallLst.widthProperty().multiply(0.08));
        tblcPurChallLstChallanDate.prefWidthProperty().bind(tblvPurChallLst.widthProperty().multiply(0.06));
        tblcPurChallLstSupplierName.prefWidthProperty().bind(tblvPurChallLst.widthProperty().multiply(0.16));
        tblcPurChallLstNarration.prefWidthProperty().bind(tblvPurChallLst.widthProperty().multiply(0.18));
        tblcPurChallLstTaxable.prefWidthProperty().bind(tblvPurChallLst.widthProperty().multiply(0.07));
        tblcPurChallLstTax.prefWidthProperty().bind(tblvPurChallLst.widthProperty().multiply(0.05));
        tblcPurChallLstBillAmount.prefWidthProperty().bind(tblvPurChallLst.widthProperty().multiply(0.08));
        tblcPurChallLstTranxStatus.prefWidthProperty().bind(tblvPurChallLst.widthProperty().multiply(0.07));
        tblcPurChallLstPrint.prefWidthProperty().bind(tblvPurChallLst.widthProperty().multiply(0.05));
//        tblcPurChallLstAction.prefWidthProperty().bind(tblvPurChallLst.widthProperty().multiply(0.07));
    }

    private void findOutSelectedRow() {
        Boolean canShowButton = false;
        ObservableList<PurchaseChallanDTO> list = tblvPurChallLst.getItems();
        for (PurchaseChallanDTO object : list) {
            if (object.isIs_row_selected()) {
                canShowButton = true;
                selectedIndex = Integer.valueOf(object.getId());
            }
        }
        if (canShowButton) {
            btnPurChallLstCreatebtn.setVisible(false);
            btnPurChallToInvoice.setVisible(true);

        } else {
            btnPurChallLstCreatebtn.setVisible(true);
            btnPurChallToInvoice.setVisible(false);

        }
    }

    private void handleSelection(ObservableList<PurchaseChallanDTO> items) {
        String errorMessage = "Different Ledger selected!";
        String jsonFormat = " [%s] ";   //"selectedIds":

        StringJoiner selectedIds = new StringJoiner(",");
        JsonObject obj = new JsonObject();
        JsonArray array = new JsonArray();
        String lastLedgerName = null;

        for (PurchaseChallanDTO item : items) {
            if (item.isIs_row_selected()) {
                if (lastLedgerName == null) {
                    lastLedgerName = item.getSundry_creditor_name();
                } else if (!lastLedgerName.equals(item.getSundry_creditor_name())) {
                    // Ledger names are different
                    System.out.println("error mes");
                    AlertUtility.AlertErrorTimeout(AlertUtility.alertTypeError, errorMessage, input -> {
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

        System.out.println("Json Result : >> " + jsonResult);
        PurChallToInvoiceCreateController.purChallToInvId = jsonResult;

    }

    public void fetchDataOfAllPurchaseChallans(String searchKey) {
        try {
            Map<String, String> body = new HashMap<>();
            body.put("pageNo", "1");
            body.put("pageSize", "50");
            body.put("searchText", searchKey);
            body.put("sort", "");
            if (tftranxPurChallLstFromDate != null && tftranxPurChallLstFromDate.getText() != null && !tftranxPurChallLstFromDate.getText().isEmpty()) {
                body.put("startDate", Communicator.text_to_date.fromString(tftranxPurChallLstFromDate.getText()).toString());
            } else {
                body.put("startDate", "");
            }
            if (tftranxPurChallLstToDate != null && tftranxPurChallLstToDate.getText() != null && !tftranxPurChallLstToDate.getText().isEmpty()) {
                body.put("endDate", Communicator.text_to_date.fromString(tftranxPurChallLstToDate.getText()).toString());
            } else {
                body.put("endDate", "");
            }
            String requestBody = Globals.mapToString(body);
            HttpResponse<String> response = APIClient.postJsonRequest(requestBody, "list_po_challan_invoice");
            JsonObject jsonObject = new Gson().fromJson(response.body(), JsonObject.class);
            ObservableList<PurchaseChallanDTO> observableList = FXCollections.observableArrayList();
            if (jsonObject.get("responseStatus").getAsInt() == 200) {
                JsonObject responseObject = jsonObject.get("responseObject").getAsJsonObject();
                JsonArray responseArray = responseObject.get("data").getAsJsonArray();

                if (responseArray.size() > 0) {
                    tblvPurChallLst.setVisible(true);
                    for (JsonElement element : responseArray) {
                        JsonObject item = element.getAsJsonObject();
                        String id = item.get("id").getAsString();
                        String invoice_no = item.get("invoice_no").getAsString();
                        String referenceNo = item.get("referenceNo").getAsString();
                        String invoice_date = DateConvertUtil.convertDispDateFormat(item.get("invoice_date").getAsString());
                        String sundry_creditor_name = item.get("sundry_creditor_name").getAsString();
                        String narration = item.get("narration").getAsString();
                        String taxable_amt = item.get("taxable_amt").getAsString();
                        String tax_amt = item.get("tax_amt").getAsString();
                        String total_amount = item.get("total_amount").getAsString();
                        String purchase_order_status = item.get("purchase_challan_status").getAsString();

                        observableList.add(new PurchaseChallanDTO(id, invoice_no, referenceNo, invoice_date, sundry_creditor_name, narration, taxable_amt, tax_amt, total_amount, purchase_order_status)
                        );
                    }
                    tblcPurChallLstChallanNo.setCellValueFactory(new PropertyValueFactory<>("invoice_no"));
                    tblcPurChallLstRefNo.setCellValueFactory(new PropertyValueFactory<>("referenceNo"));
                    tblcPurChallLstChallanDate.setCellValueFactory(new PropertyValueFactory<>("invoice_date"));
                    tblcPurChallLstSupplierName.setCellValueFactory(new PropertyValueFactory<>("sundry_creditor_name"));
                    tblcPurChallLstNarration.setCellValueFactory(new PropertyValueFactory<>("narration"));
                    tblcPurChallLstTaxable.setCellValueFactory(new PropertyValueFactory<>("taxable_amt"));
                    tblcPurChallLstTax.setCellValueFactory(new PropertyValueFactory<>("tax_amt"));
                    tblcPurChallLstBillAmount.setCellValueFactory(new PropertyValueFactory<>("total_amount"));
                    tblcPurChallLstTranxStatus.setCellValueFactory(new PropertyValueFactory<>("purchase_challan_status"));
//                    tblcPurChallLstAction.setCellFactory(param -> {
//                        final TableCell<PurchaseChallanDTO, String> cell = new TableCell<>() {
//                            private ImageView delImg = new ImageView(new Image(String.valueOf(GenivisApplication.class.getResource("ui/assets/del.png"))));
//                            private ImageView edtImg = new ImageView(new Image(String.valueOf(GenivisApplication.class.getResource("ui/assets/edt.png"))));
//
//                            {
//                                delImg.setFitHeight(20.0);
//                                delImg.setFitWidth(20.0);
//                                edtImg.setFitHeight(20.0);
//                                edtImg.setFitWidth(20.0);
//                            }
//
//                            private final Button delButton = new Button("", delImg);
//                            private final Button edtButton = new Button("", edtImg);
//
//                            {
//                                edtButton.setOnAction(actionEvent -> {
//                                    purchaseChallanid = Integer.valueOf(getTableView().getItems().get(getIndex()).getId());
//                                    GlobalController.getInstance().addTabStatic(PURCHASE_CHALLAN_EDIT_SLUG, false);
//                                });
//                            }
//
//                            {
//                                delButton.setOnAction(actionEvent -> {
//                                    AlertUtility.AlertConfirmation(AlertUtility.alertTypeConfirmation, "Are you Sure?", input -> {
//                                        if (input == 1) {
//                                            int delId = Integer.parseInt(observableList.get(getIndex()).getId());
//                                            deleteAPICall(delId);
//                                        }
//                                    });
//                                });
//                            }
//
//                            HBox hbActions = new HBox();
//
//                            {
//                                hbActions.getChildren().add(edtButton);
//                                hbActions.getChildren().add(delButton);
//                                hbActions.setSpacing(10.0);
//                            }
//
//                            // Set the action for the view button
//                            @Override
//                            protected void updateItem(String item, boolean empty) {
//                                super.updateItem(item, empty);
//                                if (empty) {
//                                    setGraphic(null);
//                                } else {
//                                    setGraphic(hbActions);
//                                }
//                            }
//                        };
//                        return cell;
//                    });
//                    tblcPurChallLstPrint.setCellValueFactory(new PropertyValueFactory<>("tblcPurChallLstPrint"));
                    tblcPurChallLstPrint.setCellFactory(param -> {
                        final TableCell<SalesOrderListDTO, String> cell = new TableCell<>() {
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
                                    SalesOrderListDTO id = getTableView().getItems().get(getIndex());
                                    String status = id.getTcSalesOrderListTranxStatus();
                                    // Check if the status is "opened"
                                    if (status != null) {
//                                        id1 = Integer.valueOf(id.getId());
//                                        printSaleOrder(id1);
                                    } else {
                                        // Show an alert indicating that the order is already closed
                                        AlertUtility.CustomCallback callback = (number) -> {
                                            if (number == 0) {
                                                tftranxPurChallLstSearchField.requestFocus();
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

                    tblvPurChallLst.setItems(observableList);
                    originalData = observableList;
                    //******************************** Highlight on the Created/Edited Record in the List Start ********************************
                    if (PurchaseChallanListController.isNewPurchaseChallanCreated) {
                        tblvPurChallLst.scrollTo(0);
                        PurchaseChallanListController.isNewPurchaseChallanCreated = false; // Reset the flag
                    } else if (PurchaseChallanListController.editedPurchaseChallanId != null) {
                        for (PurchaseChallanDTO purchaseChallan : observableList) {
                            if (purchaseChallan.getId().equals(PurchaseChallanListController.editedPurchaseChallanId)) {
                                tblvPurChallLst.getSelectionModel().select(purchaseChallan);
                                tblvPurChallLst.scrollTo(purchaseChallan);
                                PurchaseChallanListController.editedPurchaseChallanId = null; // Reset the ID
                                break;
                            }
                        }
                    }
                    //******************************** Highlight on the Created/Edited Record in the List End ********************************

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

    // OnCLick of Delete Icon Deleting the Credit Note
    private void deleteAPICall(Integer id) {
        APIClient apiClient = null;
        try {
            purChallanListLogger.debug("Get deleteAPICall Started...");
            Map<String, String> map = new HashMap<>();
            map.put("id", String.valueOf(id));
            String formData = Globals.mapToStringforFormData(map);
            apiClient = new APIClient(EndPoints.DELETE_PURCHASE_CHALLAN, formData, RequestType.FORM_DATA);
            apiClient.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
                @Override
                public void handle(WorkerStateEvent workerStateEvent) {
                    jsonObject = new Gson().fromJson(workerStateEvent.getSource().getValue().toString(), JsonObject.class);
                    System.out.println("jsonObject ::: >>" + jsonObject);
                    message = jsonObject.get("message").getAsString();
                    if (jsonObject.get("responseStatus").getAsInt() == 200) {
                        AlertUtility.AlertSuccessTimeout(AlertUtility.alertTypeSuccess, message, input -> {
                            fetchDataOfAllPurchaseChallans("");
                            tftranxPurChallLstSearchField.requestFocus();
                        });
                    } else {
                        AlertUtility.AlertErrorTimeout(AlertUtility.alertTypeError, "Failed to Delete Purchase Challan.", in -> {
                            tftranxPurChallLstSearchField.requestFocus();
                        });
                    }
                }
            });
            apiClient.setOnCancelled(new EventHandler<WorkerStateEvent>() {
                @Override
                public void handle(WorkerStateEvent workerStateEvent) {
                    purChallanListLogger.error("Network API cancelled in deleteAPICall()" + workerStateEvent.getSource().getValue().toString());

                }
            });

            apiClient.setOnFailed(new EventHandler<WorkerStateEvent>() {
                @Override
                public void handle(WorkerStateEvent workerStateEvent) {
                    purChallanListLogger.error("Network API cancelled in deleteAPICall()" + workerStateEvent.getSource().getValue().toString());

                }
            });
            apiClient.start();
            purChallanListLogger.debug("Get deleteAPICall end");

        } catch (Exception e) {
            e.printStackTrace();
            purChallanListLogger.error("Exception deleteAPICall() : " + Globals.getExceptionString(e));
        } finally {
            apiClient = null;
        }
    }

    public void goToPurInvoice() {
//        System.out.println(SalesQuotationToChallan.input);
        ObservableList<PurchaseChallanDTO> list = tblvPurChallLst.getItems();
        for (PurchaseChallanDTO object : list) {
            if (object.isIs_row_selected()) {
                if (object.getPurchase_challan_status().equalsIgnoreCase("closed")) {
                    AlertUtility.CustomCallback callback = (number) -> {
                        if (number == 0) {
                            tftranxPurChallLstSearchField.requestFocus();
                        }
                    };
                    AlertUtility.AlertErrorTimeout(AlertUtility.alertTypeError, "Error", input -> {
                    });
                } else {
                    if (!PurChallToInvoiceCreateController.purChallToInvId.isEmpty()) {
                        GlobalController.getInstance().addTabStatic(PURCHASE_CHALL_TO_INV_CREATE_SLUG, false);
                    }
                }

            }
        }

    }
    private void purchaseChallanEditPage() {
        PurchaseChallanDTO selectedItem = (PurchaseChallanDTO) tblvPurChallLst.getSelectionModel().getSelectedItem();
        if(selectedItem!=null){
            purchaseChallanid = Integer.valueOf(selectedItem.getId());
            //? Highlight
            PurchaseChallanListController.editedPurchaseChallanId = String.valueOf(purchaseChallanid);
            GlobalController.getInstance().addTabStatic(PURCHASE_CHALLAN_EDIT_SLUG, false);
        }
    }

    @FXML
    private void handleCtrlDPressed() {
        try {
            PurchaseChallanDTO selectedItem = (PurchaseChallanDTO) tblvPurChallLst.getSelectionModel().getSelectedItem();
            purchaseChallanid = Integer.valueOf(selectedItem.getId());
            if(purchaseChallanid!=null) {
                Integer id = purchaseChallanid;
                System.out.println("ctrl+D"+id);
                deleteAPICall(purchaseChallanid);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
