package com.opethic.genivis.controller.tranx_sales;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.opethic.genivis.GenivisApplication;
//import com.opethic.genivis.dto.SalesChallanDTO;
import com.opethic.genivis.controller.tranx_sales.invoice.TranxSalesChallanToInvoiceCreate;
import com.opethic.genivis.dto.SalesChallanDTO;
import com.opethic.genivis.dto.SalesChallanDTO;
import com.opethic.genivis.dto.SalesOrderListDTO;
import com.opethic.genivis.network.APIClient;
import com.opethic.genivis.network.EndPoints;
import com.opethic.genivis.utils.*;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
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
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Screen;
import javafx.stage.Stage;


import java.io.IOException;
import java.net.URL;
import java.net.http.HttpResponse;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.StringJoiner;

import static com.opethic.genivis.network.EndPoints.SALES_CHALLAN_TO_INVOICE_CREATE_SLUG;
import static com.opethic.genivis.utils.FxmFileConstants.*;
import static com.opethic.genivis.utils.Globals.*;
import static com.opethic.genivis.utils.Globals.areaHeadListDTO;

public class SalesChallanListController implements Initializable {

    DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    //    TableView tblvSalesChallan;
    @FXML
    TableColumn<SalesChallanDTO, Boolean> tcSalesChallanSelect;

    @FXML
    private Button btnPurChallToInvoice;

    private Node[] focusableNodes;

    private ObservableList<SalesChallanDTO> originalData;


    @FXML
    private BorderPane spRootSalesChallanListPane;


    @FXML
    private TableView<SalesChallanDTO> tblvSalesChallan;


    @FXML
    TableColumn tblcSalesChallanLstSelect;
    @FXML
    private TableColumn tblcSalesChallanLstTranxStatus;
    @FXML
    private TextField tftranxSalesChallanLstSearchField;
    @FXML
    private TextField dptranxSalesChallanLstToDate, dptranxSalesChallanLstFromDate;
    @FXML
    private Button btnSalesChallanLstCreatebtn;

    private String responseBody, message;
    public static Integer purchase_id = -1;

    ObservableList<SalesChallanDTO> observableList = FXCollections.observableArrayList();


    @FXML

    private TableColumn<SalesChallanDTO, String> tcSalesChallanSCNo,
            tcSalesChallanRefNo,
            tcSalesChallanDate,
            tcSalesChallanSupplierName,
            tcSalesChallanNarration,
            tcSalesChallanTaxable,
            tcSalesChallanTax,
            tcSalesChallanBillAmount,
            tcSalesChallanTranxStatus,
            tcSalesChallanPrint,
            tcSalesChallanAction;
    private String conversionId;

    //FNfind Selected Row

    private void filterData(String keyword) {
        ObservableList<SalesChallanDTO> filteredData = FXCollections.observableArrayList();

        if (keyword.isEmpty()) {
            tblvSalesChallan.setItems(originalData);
            return;
        }

        for (SalesChallanDTO item : originalData) {
            if (matchesKeyword(item, keyword)) {
                filteredData.add(item);
            }
        }

        tblvSalesChallan.setItems(filteredData);
    }

    private boolean matchesKeyword(SalesChallanDTO item, String keyword) {
        String lowerCaseKeyword = keyword.toLowerCase();

        // Check if any of the columns contain the keyword
        return item.getBill_no().toLowerCase().contains(lowerCaseKeyword) ||
                item.getReferenceNo().toLowerCase().contains(lowerCaseKeyword) ||
                item.getBill_date().toLowerCase().contains(lowerCaseKeyword) ||
                item.getSalesAccountName().toLowerCase().contains(lowerCaseKeyword) ||
                item.getSales_challan_status().toLowerCase().contains(lowerCaseKeyword);
    }


    private void findOutSelectedRow() {
        Boolean canShowButton = false;
        ObservableList<SalesChallanDTO> list = tblvSalesChallan.getItems();
        for (SalesChallanDTO object : list) {
            if (object.isIs_row_selected()) {
                canShowButton = true;
//                System.out.println("Selected Index " + object.getInvoice_no());
            }
        }
        if (canShowButton) {
            btnSalesChallanLstCreatebtn.setVisible(false);
            btnPurChallToInvoice.setVisible(true);

        } else {
            btnSalesChallanLstCreatebtn.setVisible(true);
            btnPurChallToInvoice.setVisible(false);

        }
    }

    //? Highlight the Record Start
    public static boolean isNewSalesChallanCreated = false; // Flag for new creation
    public static String editedSalesChallanId = null; // ID for edited Sales Order
    //? Highlight the Record End


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        ResponsiveWiseCssPicker();

        salesChallanDTO = null;

        Platform.runLater(() -> tftranxSalesChallanLstSearchField.requestFocus());
        //         Enter traversal
        spRootSalesChallanListPane.addEventFilter(KeyEvent.KEY_PRESSED, (KeyEvent event) -> {
            if (event.getCode() == KeyCode.DOWN && tftranxSalesChallanLstSearchField.isFocused()) {
//                btnFranchiseListCreate.fire();
                tblvSalesChallan.getSelectionModel().select(0);
                tblvSalesChallan.requestFocus();
            }
            if (event.getCode() == KeyCode.ENTER) {
                if (event.getTarget() instanceof Button targetButton && targetButton.getText().equalsIgnoreCase("Create")) {
                    System.out.println(targetButton.getText());
                } else {
                    salesChallanEditPage();
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
            if (event.getCode() == KeyCode.N && event.isControlDown()) {
                btnSalesChallanLstCreatebtn.fire();
            }
            if (event.getCode() == KeyCode.E && event.isControlDown()) {
                salesChallanEditPage();
            }
            if (event.getCode() == KeyCode.D && event.isControlDown()) {
                salesChallanDTO = tblvSalesChallan.getSelectionModel().getSelectedItem();
                Integer id = Integer.valueOf(salesChallanDTO.getId());
                deleteSaleOrder(id);
            }

//            if (event.getCode() == KeyCode.X && event.isControlDown()) {
//                btnAreaHeadCreateCancel.fire();
//            }
        });


        //todo:focus next from Todate
        dptranxSalesChallanLstToDate.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.TAB && event.isShiftDown()) {
                dptranxSalesChallanLstFromDate.requestFocus();
                event.consume();
            } else if (event.getCode() == KeyCode.ENTER || event.getCode() == KeyCode.TAB) {
                System.out.println("enter/tab presseddd from ledger name");
                btnSalesChallanLstCreatebtn.requestFocus();
            }
        });

//        tftranxSalesChallanLstSearchField.requestFocus();

        Platform.runLater(() -> tftranxSalesChallanLstSearchField.requestFocus());

        // Populate focusable nodes array with the desired order and Focus To next and Previous Element
//        focusableNodes = new Node[]{tftranxSalesChallanLstSearchField, dptranxSalesChallanLstFromDate, dptranxSalesChallanLstToDate};
//        CommonValidationsUtils.setupFocusNavigation(focusableNodes);

        //? Assuming tcSalesChallanSelect is of type String
        tcSalesChallanTranxStatus.setCellValueFactory(new PropertyValueFactory<>("tcSalesChallanTranxStatus"));
        tcSalesChallanTranxStatus.setCellFactory(column -> {
            return new TableCell<SalesChallanDTO, String>() {
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

                        // Check the value of tcSalesChallanSelect
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

        tcSalesChallanSelect.setCellFactory(column -> new CheckBoxTableCell<>());
        tcSalesChallanSelect.setCellValueFactory(cellData -> {
            SalesChallanDTO cellValue = cellData.getValue();
            BooleanProperty property = cellValue.is_row_selectedProperty();
            property.addListener((observable, oldValue, newValue) -> cellValue.setIs_row_selected(newValue));
            findOutSelectedRow();
            if (cellValue.isIs_row_selected()) {
                if (cellValue.getSales_challan_status().equalsIgnoreCase("closed")) {
//                    AlertUtility.CustomCallback callback = (number) -> {
//                        cellValue.setIs_row_selected(false);
//                    };
//                    Stage stage = (Stage) spRootSalesChallanListPane.getScene().getWindow();
//                    AlertUtility.AlertError(stage, AlertUtility.alertTypeError, "challan is already closed", callback);
                    AlertUtility.AlertErrorTimeout(AlertUtility.alertTypeError, "The Challan is closed", in -> {
                        cellValue.setIs_row_selected(false);
                    });

                } else {
                    handleSelection(tblvSalesChallan.getItems());
                }
            }

            // Add listener to handler change
//            property.addListener((observable, oldValue, newValue) -> {
//                System.out.println("New Value -->" + newValue);
//                cellValue.setIs_row_selected(newValue);
//            });
//            findOutSelectedRow();
            return property;
        });

        //        //todo:get the id onDoubleClick for Edit
        tblvSalesChallan.setRowFactory(tv -> {
            TableRow<SalesChallanDTO> row = new TableRow<>();
            row.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    if (mouseEvent.getClickCount() == 2) {
                        SalesChallanDTO salesChallanDTO = row.getItem();
                        salesChallanEditPage();
                    }
                }
            });
            return row;
        });

        // List of all Company Admins Api call
        fetchDataOfAllPurchaseChallans("");
        tftranxSalesChallanLstSearchField.setOnKeyTyped(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                String searchKey = tftranxSalesChallanLstSearchField.getText().trim();
//                if (searchKey.length() >= 3) {
//                    fetchDataOfAllPurchaseChallans(searchKey);
//                } else if (searchKey.isEmpty()) {
//                    fetchDataOfAllPurchaseChallans("");
//                }
            }
        });

        btnSalesChallanLstCreatebtn.setOnAction(event -> {
//            @Override
//            public void handle(ActionEvent actionEvent) {
//                try {
//                    Stage stage= new Stage();
//                    Parent loader = FXMLLoader.load(GenivisApplication.class.getResource("ui/tranx_sales/sales_challan_create.fxml"));
//                    Scene scene = new Scene(loader);
//                    stage.setScene(scene);
//                    stage.show();
//                    stage.setMaximized(true);
//                } catch (IOException e) {
//                    throw new RuntimeException(e);
//                }
//            }
//            Globals.areaHeadListDTO=null;


            GlobalController.getInstance().addTabStatic(SALES_CHALLAN_CREATE_SLUG, false);
        });


        originalData = tblvSalesChallan.getItems();
        tftranxSalesChallanLstSearchField.textProperty().addListener((observable, oldValue, newValue) -> {
            filterData(newValue.trim());
        });

//        tftranxSalesChallanLstSearchField.textProperty().addListener((observable, oldValue, newValue) -> {
//            filterData(newValue.trim());
//        });

    }

    public static Integer getPurchaseId() {
        return purchase_id;
    }
    private void ResponsiveWiseCssPicker() {
        double height = Screen.getPrimary().getBounds().getHeight();
        double width = Screen.getPrimary().getBounds().getWidth();
        System.out.println("width" + width);
        if (width >= 992 && width <= 1024) {
            spRootSalesChallanListPane.getStylesheets().add(GenivisApplication.class.getResource("ui/css/TranxCommonCssStyles/tranxCommonCssStyle1.css").toExternalForm());
        } else if (width >= 1025 && width <= 1280) {
            spRootSalesChallanListPane.getStylesheets().add(GenivisApplication.class.getResource("ui/css/TranxCommonCssStyles/tranxCommonCssStyle2.css").toExternalForm());
        } else if (width >= 1281 && width <= 1368) {
            spRootSalesChallanListPane.getStylesheets().add(GenivisApplication.class.getResource("ui/css/TranxCommonCssStyles/tranxCommonCssStyle3.css").toExternalForm());
        } else if (width >= 1369 && width <= 1400) {
            spRootSalesChallanListPane.getStylesheets().add(GenivisApplication.class.getResource("ui/css/TranxCommonCssStyles/tranxCommonCssStyle4.css").toExternalForm());
        } else if (width >= 1401 && width <= 1440) {
            spRootSalesChallanListPane.getStylesheets().add(GenivisApplication.class.getResource("ui/css/TranxCommonCssStyles/tranxCommonCssStyle5.css").toExternalForm());
        } else if (width >= 1441 && width <= 1680) {
            spRootSalesChallanListPane.getStylesheets().add(GenivisApplication.class.getResource("ui/css/TranxCommonCssStyles/tranxCommonCssStyle6.css").toExternalForm());
        } else if (width >= 1681 && width <= 1920) {
            spRootSalesChallanListPane.getStylesheets().add(GenivisApplication.class.getResource("ui/css/TranxCommonCssStyles/tranxCommonCssStyle7.css").toExternalForm());
        }
    }

    //    FNEdit
    public void salesChallanEditPage() {
        try {
            salesChallanDTO = tblvSalesChallan.getSelectionModel().getSelectedItem();
            if(Globals.salesChallanDTO!=null) {
                //? Highlight
                SalesChallanListController.editedSalesChallanId = salesChallanDTO.getId();
                Integer id = Integer.valueOf(salesChallanDTO.getId());
                purchase_id = id;

                System.out.println("id-->" + id);
                GlobalController.getInstance().addTabStaticWithParam(SALES_CHALLAN_EDIT_SLUG, false, id);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void fetchDataOfAllPurchaseChallans(String searchKey) {
        try {
//            HttpResponse<String> response = APIClient.getRequest("list_sale_challan");
//            JsonObject jsonObject = new Gson().fromJson(response.body(), JsonObject.class);
//            System.out.println("i am list"+jsonObject);
//            ObservableList<SalesChallanDTO> observableList = FXCollections.observableArrayList();

            String startDate = dptranxSalesChallanLstFromDate.getText() != null ? dptranxSalesChallanLstFromDate.getText() : "";
            String endDate = dptranxSalesChallanLstToDate.getText() != null ? dptranxSalesChallanLstToDate.getText() : "";

            Map<String, String> body = new HashMap<>();
            body.put("pageNo", "1");
            body.put("pageSize", "50");
            body.put("searchText", searchKey);
            body.put("sort", "{ \\\"colId\\\": null, \\\"isAsc\\\": true }");
            body.put("startDate", startDate);
            body.put("endDate", endDate);


//            body.put("startDate", dptranxSalesChallanLstFromDate.getValue()!= null ? String.valueOf(dptranxSalesChallanLstFromDate.getValue()) : "");
//            body.put("endDate",dptranxSalesChallanLstToDate.getValue()!= null ? String.valueOf(dptranxSalesChallanLstToDate.getValue()) : "");
            String requestBody = Globals.mapToString(body);
            HttpResponse<String> response = APIClient.postJsonRequest(requestBody, EndPoints.SALES_CHALLAN_LIST_ENDPOINT);
            System.out.println("SalesOrder" + response.body());
            JsonObject jsonObject = new Gson().fromJson(response.body(), JsonObject.class);
            System.out.println("111Q" + jsonObject);
//            ObservableList<SalesOrderListDTO> salesChallanObservableList = FXCollections.observableArrayList();


//            JsonObject jsonObject = new Gson().fromJson(response.body(), JsonObject.class);

//            JsonObject jsonObject = new Gson().fromJson(response.body(), JsonObject.class);
//            ObservableList<SalesChallanDTO> observableList = FXCollections.observableArrayList();


//            System.out.println("hello555"+response.body());
//            JsonObject jsonObject = new Gson().fromJson(response.body(), JsonObject.class);
//            System.out.println("111" +jsonObject);
//            ObservableList<SalesChallanDTO> observableList = FXCollections.observableArrayList();
            if (jsonObject.get("responseStatus").getAsInt() == 200) {
                JsonObject responseObject = jsonObject.get("responseObject").getAsJsonObject();
                JsonArray responseArray = responseObject.get("data").getAsJsonArray();

//                JsonArray responseArray = responseObject.get("data").getAsJsonArray();

                if (responseArray.size() > 0) {
//                    tblvSalesChallanLst.setVisible(true);


                    for (JsonElement element : responseArray) {
                        JsonObject item = element.getAsJsonObject();

                        System.out.println(item.get("referenceType").isJsonNull());

                        String id = item.get("id").getAsString();
                        String SC_id = item.get("id").getAsString();
                        String SC_bill_no = item.get("bill_no").getAsString();
                        String SC_bill_date = DateConvertUtil.convertDispDateFormat(item.get("bill_date").getAsString());
                        String SC_narration = item.get("narration").getAsString();
                        String SC_sales_challan_status = item.get("sales_challan_status").getAsString();
                        String SC_sale_account_name = item.get("sale_account_name").getAsString();
                        String SC_sundry_debtors_id = item.get("sundry_debtors_id").getAsString();
                        String SC_sundry_debtors_name = item.get("sundry_debtors_name").getAsString();
                        String SC_tax_amt = item.get("tax_amt").getAsString();
                        String SC_taxable_amt = item.get("taxable_amt").getAsString();
                        String SC_total_base_amount = item.get("total_base_amount").getAsString();
                        String SC_total_amount = item.get("total_amount").getAsString();
                        String SC_referenceNo = item.get("referenceNo").getAsString();
                        String SC_referenceType = item.get("referenceType").isJsonNull() ? "" : item.get("referenceType").getAsString();
                        String SC_transactionTrackingNo = item.get("transactionTrackingNo").getAsString();
                        String SC_tranxCode = item.get("tranxCode").getAsString();


                        observableList.add(new SalesChallanDTO(id, SC_bill_no, SC_referenceNo, SC_bill_date, SC_sundry_debtors_name, SC_narration, SC_taxable_amt, SC_tax_amt, SC_total_amount, SC_sales_challan_status,SC_sundry_debtors_id));
                    }
                    tcSalesChallanSCNo.setCellValueFactory(new PropertyValueFactory<>("bill_no"));
                    tcSalesChallanRefNo.setCellValueFactory(new PropertyValueFactory<>("referenceNo"));
                    tcSalesChallanDate.setCellValueFactory(new PropertyValueFactory<>("bill_date"));
                    tcSalesChallanSupplierName.setCellValueFactory(new PropertyValueFactory<>("sundry_debtors_name"));
                    tcSalesChallanNarration.setCellValueFactory(new PropertyValueFactory<>("narration"));
                    tcSalesChallanTaxable.setCellValueFactory(new PropertyValueFactory<>("taxable_amt"));
                    tcSalesChallanTax.setCellValueFactory(new PropertyValueFactory<>("tax_amt"));
                    tcSalesChallanBillAmount.setCellValueFactory(new PropertyValueFactory<>("total_amount"));
                    tcSalesChallanTranxStatus.setCellValueFactory(new PropertyValueFactory<>("sales_challan_status"));
//                    tcSalesChallanPrint.setCellValueFactory(new PropertyValueFactory<>("sales_challan_status"));
//                    tcSalesChallanAction.setCellValueFactory(new PropertyValueFactory<>("sales_challan_status"));


//                    tcSalesChallanAction.setCellFactory(param -> {
//                        final TableCell<SalesChallanDTO, String> cell = new TableCell<>() {
//                            private ImageView delImg = new ImageView(new Image(String.valueOf(GenivisApplication.class.getResource("ui/assets/del.png"))));
//                            private ImageView edtImg = new ImageView(new Image(String.valueOf(GenivisApplication.class.getResource("ui/assets/edt.png"))));
////                            private ImageView viewImg = new ImageView(new Image(String.valueOf(GenivisApplication.class.getResource("ui/assets/view.png"))));
//
//                            {
//                                delImg.setFitHeight(20.0);
//                                delImg.setFitWidth(20.0);
//                                edtImg.setFitHeight(20.0);
//                                edtImg.setFitWidth(20.0);
////                                viewImg.setFitHeight(20.0);
////                                viewImg.setFitWidth(20.0);
//                            }
//
//                            private final Button delButton = new Button("", delImg);
//                            private final Button edtButton = new Button("", edtImg);
////                            private final Button viewButton = new Button("", viewImg);
//
//                            {
//                                edtButton.setOnAction(actionEvent -> {
//                                    Integer id = Integer.valueOf(getTableView().getItems().get(getIndex()).getId());
//                                    GlobalController.getInstance().addTabStaticWithParam(SALES_CHALLAN_EDIT_SLUG, false, id);
//                                });
//                            }
//
//                            {
//                                delButton.setOnAction(actionEvent -> {
//                                    SalesChallanDTO id = getTableView().getItems().get(getIndex());
//                                    String status = id.getSales_challan_status();
//                                    // Check if the status is "opened"
//                                    if (status != null && status.equalsIgnoreCase("opened")) {
//                                        Integer id1 = Integer.valueOf(id.getId());
//
//                                        deleteSaleOrder(id1);
//                                    } else {
//                                        // Show an alert indicating that the order is already closed
//                                        AlertUtility.CustomCallback callback = (number) -> {
//                                            if (number == 0) {
//                                                tftranxSalesChallanLstSearchField.requestFocus();
//                                            }
//                                        };
//                                        Stage stage = (Stage) spRootSalesChallanListPane.getScene().getWindow();
//                                        AlertUtility.AlertError(stage, AlertUtility.alertTypeError, "Order is already closed", callback);
//                                    }
////                                    GlobalController.getInstance().addTabStaticWithParam(SALES_ORDER_EDIT_SLUG, false, id);
//                                });
//                            }
//
//                            HBox hbActions = new HBox();
//
//                            {
//                                hbActions.getChildren().add(edtButton);
////                                hbActions.getChildren().add(viewButton);
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


                    tblvSalesChallan.setItems(observableList);

                    //******************************** Highlight on the Created/Edited Record in the List Start ********************************
                    if (SalesChallanListController.isNewSalesChallanCreated) {
                        tblvSalesChallan.getSelectionModel().selectFirst();
                        tblvSalesChallan.scrollTo(0);
                        SalesChallanListController.isNewSalesChallanCreated = false; // Reset the flag
                    } else if (SalesChallanListController.editedSalesChallanId != null) {
                        for (SalesChallanDTO salesChallan : observableList) {
                            if (salesChallan.getId().equals(SalesChallanListController.editedSalesChallanId)) {
                                tblvSalesChallan.getSelectionModel().select(salesChallan);
                                tblvSalesChallan.scrollTo(salesChallan);
                                SalesChallanListController.editedSalesChallanId = null; // Reset the ID
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


    public void deleteSaleOrder(int id) {
        try {
            AlertUtility.CustomCallback callback = number -> {
                if (number == 1) {
//                GlobalController.getInstance().addTabStatic(FRANCHISE_LIST_SLUG, false);
                    System.out.println("this is idyes" + id);
                    try {
                        Map<String, String> map = new HashMap<>();
                        map.put("id", String.valueOf(id));
                        String formData = Globals.mapToStringforFormData(map);
                        HttpResponse<String> response = APIClient.postFormDataRequest(formData, EndPoints.SALES_CHALLAN_DELETE_ENDPOINT);
                        responseBody = response.body();
                        JsonObject jsonObject = new Gson().fromJson(responseBody, JsonObject.class);
                        message = jsonObject.get("message").getAsString();
                        System.out.println("Delete->" + jsonObject.get("message"));
//                        AlertUtility.CustomCallback callback1 = (number1) -> {
//                            if (number1 == 1) {
//                                // Update the list after successful deletion
//                                observableList.removeIf(item -> item.getId().equals(String.valueOf(id)));
//                                tblvSalesChallan.setItems(observableList);
//                            }
//                        };
//                        Stage stage2 = (Stage) spRootSalesChallanListPane.getScene().getWindow();
//                        if (jsonObject.get("responseStatus").getAsInt() == 200) {
//                            AlertUtility.AlertSuccess(stage2, "Success", message, callback1);
//                        } else {
//                            AlertUtility.AlertError(stage2, AlertUtility.alertTypeError, message, callback1);
//                        }
                        if (jsonObject.get("responseStatus").getAsInt() == 200) {
                            AlertUtility.AlertSuccessTimeout(AlertUtility.alertTypeSuccess, message, input -> {
                                // Update the list after successful deletion
                                observableList.removeIf(item -> item.getId().equals(String.valueOf(id)));
                                tblvSalesChallan.setItems(observableList);
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
//                System.out.println("this is idno"+id);
                }
            };
            AlertUtility.AlertConfirmation(AlertUtility.alertTypeConfirmation, "Do you want to Delete", callback);
//        Globals.salesChallanListDTO=null;
//        GlobalController.getInstance().addTabStatic(FRANCHISE_LIST_SLUG,false);
        } catch (Exception e) {

        }

    }

    public void redirectToChallan() {
        System.out.println("idddd " + conversionId);
        GlobalController.getInstance().addTabStatic1(SALES_ORDER_TO_CHALLAN_SLUG, false, Integer.valueOf(conversionId));
    }


    public void SalesChallanToInvoiceRedirect(ActionEvent actionEvent) {
//        SALES_CHALLAN_TO_INVOICE_CREATE_SLUG

        GlobalController.getInstance().addTabStatic(FxmFileConstants.SALES_CHALLAN_TO_INVOICE_CREATE_SLUG, false);
    }

    private void handleSelection(ObservableList<SalesChallanDTO> items) {
        String errorMessage = "Error: Different Ledger selected!";
        String jsonFormat = " [%s] ";   //"selectedIds":

        StringJoiner selectedIds = new StringJoiner(",");
        JsonObject obj = new JsonObject();
        JsonArray array = new JsonArray();
        String lastLedgerName = null;

        for (SalesChallanDTO item : items) {
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
        TranxSalesChallanToInvoiceCreate.ChallanId =jsonResult;
//                SalesQuotToOrderController.QuotaToOrderId = jsonResult;
//        SalesQuotationToChallan.QuotToChallanId = jsonResult;
//        SalesQuotationToChallan.input = jsonResult;

    }
}

