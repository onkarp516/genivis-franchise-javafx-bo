package com.opethic.genivis.controller.tranx_purchase;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.opethic.genivis.GenivisApplication;
import com.opethic.genivis.dto.ProductContentsMasterDTO;
import com.opethic.genivis.dto.PurchaseInvoiceDTO;
import com.opethic.genivis.dto.reqres.product.Communicator;
import com.opethic.genivis.network.APIClient;
import com.opethic.genivis.network.EndPoints;
import com.opethic.genivis.utils.*;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Screen;

import java.net.URL;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import static com.opethic.genivis.utils.FxmFileConstants.*;

public class PurchaseInvoiceListController implements Initializable {
    /* @FXML
     public ScrollPane spRootPane;*/
    @FXML
    public BorderPane bpPurInvRootPane;
    @FXML
    public TextField tfPurInvListSearch;
    @FXML
    public TextField tfPurInvListToDt;
    @FXML
    public TextField tfPurInvListFromDt;
    @FXML
    public Button btnPurInvListCreate;

    @FXML
    private TableColumn<PurchaseInvoiceDTO, String> tblcPurInvListInvNo;
    @FXML
    private TableColumn<PurchaseInvoiceDTO, String> tblcPurInvListTranxId;
    @FXML
    private TableColumn<PurchaseInvoiceDTO, String> tblcPurInvListInvoiceDate;
    @FXML
    private TableColumn<PurchaseInvoiceDTO, String> tblcPurInvListSupplierName;
    @FXML
    private TableColumn<PurchaseInvoiceDTO, String> tblcPurInvListNarration;
    @FXML
    private TableColumn<PurchaseInvoiceDTO, String> tblcPurInvListTaxable;
    @FXML
    private TableColumn<PurchaseInvoiceDTO, String> tblcPurInvListTax;
    @FXML
    private TableColumn<PurchaseInvoiceDTO, String> tblcPurInvListBillAmount;
    @FXML
    private TableView<PurchaseInvoiceDTO> tblvPurInvList;
    @FXML
    private TableColumn tblcPurInvListPrint;
    @FXML
    private TableColumn tblcPurInvListBarcode;
    @FXML
    private TableColumn tblcPurInvListAction;
    private ObservableList<PurchaseInvoiceDTO> orgData;

    public static Integer purchase_id = -1;
    public static String focusStatus;
    public static Integer selectedRowIndex;
    Integer ListSize = 0;


    public void editPage() {
        PurchaseInvoiceDTO selectedItem = (PurchaseInvoiceDTO) tblvPurInvList.getSelectionModel().getSelectedItem();
        String purchaseInvoiceId = selectedItem.getPurInvoiceId();
        PurchaseInvoiceEditController.id = Long.parseLong(purchaseInvoiceId);
        PurchaseInvoiceCreateController.selectedRowIndex = Integer.valueOf(purchaseInvoiceId);// for Focus on Edit in List
        System.out.println(" selectedRowIndex in List " + Integer.valueOf(purchaseInvoiceId));
        purchase_id = Integer.valueOf(purchaseInvoiceId);
        GlobalController.getInstance().addTabStaticWithParam(PURCHASE_INV_EDIT_SLUG, false, Integer.valueOf(purchaseInvoiceId));
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        ResponsiveWiseCssPicker();

        DateValidator.applyDateFormat(tfPurInvListFromDt);
        DateValidator.applyDateFormat(tfPurInvListToDt);

        //code to write when the window opens
        Platform.runLater(() -> {
            tfPurInvListSearch.requestFocus();
            System.out.println("selectedIndexin Lst" + selectedRowIndex + " focusStatus " + focusStatus);
        });
        responsiveList();
        orgData = tblvPurInvList.getItems();

        tblcPurInvListAction.setVisible(false);

        tblvPurInvList.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                editPage();
            }
        });


        tblvPurInvList.setOnKeyPressed(event -> {

            if (event.getCode() == KeyCode.SPACE) {
                System.out.println("Barcode Space");
            }
        });
        bpPurInvRootPane.addEventFilter(KeyEvent.KEY_PRESSED, (KeyEvent event) -> {
            if (event.getCode() == KeyCode.ENTER) {
                if (event.getTarget() instanceof Button targetButton && targetButton.getText().equalsIgnoreCase("create")) {
                }
                if (event.getTarget() instanceof TableView targetButton) {
                    editPage();
                } else {
                    //  editPage();
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
                    //   event.consume();
                }

            } else if (event.isControlDown() && event.getCode() == KeyCode.N) {
                GlobalController.getInstance().addTabStatic(PURCHASE_INV_CREATE_SLUG, false);
            }
            if (event.getCode() == KeyCode.N && event.isControlDown()) {
                btnPurInvListCreate.fire();
            } else if (event.getCode() == KeyCode.E && event.isControlDown()) {
                editPage();
            }
        });
        getPurchaseInvoiceList("");
        tfPurInvListSearch.setOnKeyTyped(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                String searchKey = tfPurInvListSearch.getText().trim();
                getPurchaseInvoiceList(searchKey);
            }
        });
        tfPurInvListToDt.setOnKeyPressed(actionEvent -> {
            if (actionEvent.getCode() == KeyCode.ENTER || actionEvent.getCode() == KeyCode.TAB) {
                getPurchaseInvoiceList("");
            }
        });
        btnPurInvListCreate.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                GlobalController.getInstance().addTabStatic(PURCHASE_INV_CREATE_SLUG, false);
            }
        });

        btnPurInvListCreate.setOnKeyPressed(event -> {

            if (event.getCode() == KeyCode.ENTER) {
                GlobalController.getInstance().addTabStatic(PURCHASE_INV_CREATE_SLUG, false);
            }
        });

        tfPurInvListToDt.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                String from_dt = Communicator.text_to_date.fromString(tfPurInvListFromDt.getText()).toString();
                String to_dt = Communicator.text_to_date.fromString(tfPurInvListToDt.getText()).toString();

                getPurchaseInvoiceListUsingDate(from_dt, to_dt);
            }
        });


    }

    private void ResponsiveWiseCssPicker() {
        double height = Screen.getPrimary().getBounds().getHeight();
        double width = Screen.getPrimary().getBounds().getWidth();
        System.out.println("width" + width);
        if (width >= 992 && width <= 1024) {
            bpPurInvRootPane.getStylesheets().add(GenivisApplication.class.getResource("ui/css/TranxCommonCssStyles/tranxCommonCssStyle1.css").toExternalForm());
        } else if (width >= 1025 && width <= 1280) {
            bpPurInvRootPane.getStylesheets().add(GenivisApplication.class.getResource("ui/css/TranxCommonCssStyles/tranxCommonCssStyle2.css").toExternalForm());
        } else if (width >= 1281 && width <= 1368) {
            bpPurInvRootPane.getStylesheets().add(GenivisApplication.class.getResource("ui/css/TranxCommonCssStyles/tranxCommonCssStyle3.css").toExternalForm());
        } else if (width >= 1369 && width <= 1400) {
            bpPurInvRootPane.getStylesheets().add(GenivisApplication.class.getResource("ui/css/TranxCommonCssStyles/tranxCommonCssStyle4.css").toExternalForm());
        } else if (width >= 1401 && width <= 1440) {
            bpPurInvRootPane.getStylesheets().add(GenivisApplication.class.getResource("ui/css/TranxCommonCssStyles/tranxCommonCssStyle5.css").toExternalForm());
        } else if (width >= 1441 && width <= 1680) {
            bpPurInvRootPane.getStylesheets().add(GenivisApplication.class.getResource("ui/css/TranxCommonCssStyles/tranxCommonCssStyle6.css").toExternalForm());
        } else if (width >= 1681 && width <= 1920) {
            bpPurInvRootPane.getStylesheets().add(GenivisApplication.class.getResource("ui/css/TranxCommonCssStyles/tranxCommonCssStyle7.css").toExternalForm());
        }
    }

    //function for divide the list column into a proportional value
    private void responsiveList() {
        tblcPurInvListInvNo.prefWidthProperty().bind(tblvPurInvList.widthProperty().multiply(0.07));
        tblcPurInvListTranxId.prefWidthProperty().bind(tblvPurInvList.widthProperty().multiply(0.1));
        tblcPurInvListInvoiceDate.prefWidthProperty().bind(tblvPurInvList.widthProperty().multiply(0.1));
        tblcPurInvListSupplierName.prefWidthProperty().bind(tblvPurInvList.widthProperty().multiply(0.15));
        tblcPurInvListNarration.prefWidthProperty().bind(tblvPurInvList.widthProperty().multiply(0.15));
        tblcPurInvListTaxable.prefWidthProperty().bind(tblvPurInvList.widthProperty().multiply(0.1));
        tblcPurInvListTax.prefWidthProperty().bind(tblvPurInvList.widthProperty().multiply(0.08));
        tblcPurInvListBillAmount.prefWidthProperty().bind(tblvPurInvList.widthProperty().multiply(0.12));
        tblcPurInvListPrint.prefWidthProperty().bind(tblvPurInvList.widthProperty().multiply(0.04));
        tblcPurInvListBarcode.prefWidthProperty().bind(tblvPurInvList.widthProperty().multiply(0.05));
        tblcPurInvListAction.prefWidthProperty().bind(tblvPurInvList.widthProperty().multiply(0.04));
    }

    public static Integer getPurchaseId() {
        return purchase_id;
    }

    //function to GET the list of purchase invoice
    public void getPurchaseInvoiceList(String key) {
        try {
            if (tfPurInvListFromDt != null && tfPurInvListFromDt.getText() != null && !tfPurInvListFromDt.getText().isEmpty()) {
//                productMap.put("taxApplicableDate", Communicator.text_to_date.fromString(tfPurInvListFromDt.getText()).toString());
                System.out.println("From Date-->" + Communicator.text_to_date.fromString(tfPurInvListFromDt.getText()).toString());
            }
            Map<String, String> map = new HashMap<>();
            Map<String, Object> sortObject = new HashMap<>();
            sortObject.put("colId", null);
            sortObject.put("isAsc", true);
            map.put("endDate", tfPurInvListToDt.getText());
            map.put("pageNo", "1");
            map.put("pageSize", "100");
            map.put("searchText", key);
            map.put("sort", sortObject.toString());
            map.put("startDate", tfPurInvListFromDt.getText());
            String formData = Globals.mapToString(map);
            HttpResponse<String> response = APIClient.postJsonRequest(formData, EndPoints.getPurchaseInvoiceList);
            JsonObject responseBody = new Gson().fromJson(response.body(), JsonObject.class);
            System.out.println("MK RES : "+response.body());
            ObservableList<PurchaseInvoiceDTO> observableList = FXCollections.observableArrayList();
            if (responseBody.get("responseStatus").getAsInt() == 200) {
                JsonObject obj = responseBody.get("responseObject").getAsJsonObject();
                JsonArray array = obj.get("data").getAsJsonArray();
                if (array.size() > 0) {
                    for (JsonElement element : array) {
                        JsonObject item = element.getAsJsonObject();
                        String id = item.get("id").toString();
                        String purInvoiceNumber = item.get("invoice_no").getAsString();
                        String tranxId = item.get("tranxCode").getAsString();
                        String invoiceDate = DateConvertUtil.convertDispDateFormat(item.get("invoice_date").getAsString());
                        String supplierName = item.get("sundry_creditor_name").getAsString();
                        String narration = !item.get("narration").getAsString().isEmpty() ? item.get("narration").getAsString() : "";
                        String taxable = item.get("taxable_amt").getAsString() != null ? item.get("taxable_amt").getAsString() : "";
                        String tax = item.get("tax_amt").getAsString();
                        String billAmount = String.valueOf(item.get("total_amount").getAsDouble());

                        System.out.println("NARRATION : "+narration);

                        //add all the above parameter into the constructor
                        observableList.add(new PurchaseInvoiceDTO(id, purInvoiceNumber, tranxId, invoiceDate, supplierName, narration, taxable, tax, billAmount));
                    }
                    //setting the value to the respective column
                    tblcPurInvListInvNo.setCellValueFactory(new PropertyValueFactory<>("purInvoiceNumber"));
                    tblcPurInvListTranxId.setCellValueFactory(new PropertyValueFactory<>("purInvoiceTranxId"));
                    tblcPurInvListInvoiceDate.setCellValueFactory(new PropertyValueFactory<>("purInvoiceDate"));
                    tblcPurInvListSupplierName.setCellValueFactory(new PropertyValueFactory<>("purInvoiceSupplierName"));
                    tblcPurInvListNarration.setCellValueFactory(new PropertyValueFactory<>("purInvoiceNarration"));
                    tblcPurInvListTaxable.setCellValueFactory(new PropertyValueFactory<>("purInvoiceTaxable"));
                    tblcPurInvListTax.setCellValueFactory(new PropertyValueFactory<>("purInvoiceTax"));
                    tblcPurInvListBillAmount.setCellValueFactory(new PropertyValueFactory<>("purInvoiceBillAmount"));

                    tblcPurInvListBarcode.setCellFactory(param -> {
                        final TableCell<PurchaseInvoiceDTO, Void> cell = new TableCell<>() {
                            private ImageView delImg = new ImageView(new Image(String.valueOf(GenivisApplication.class.getResource("ui/assets/barcode.png"))));

                            {
                                delImg.setFitHeight(20.0);
                                delImg.setFitWidth(20.0);
                            }

                            {

                                delImg.setOnMouseClicked(mouseEvent -> {
                                    System.out.println("Barcode Mouse");
                                    mouseEvent.consume();
                                });

                            }

                            HBox hbActions = new HBox();

                            {
                                hbActions.setAlignment(Pos.CENTER);
                                hbActions.getChildren().add(delImg);
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

                    ListSize = observableList.size();
                    System.out.println("PurchaseInvoice ObservableList size" + ListSize);
                    Platform.runLater(() -> {
                                if (focusStatus.equalsIgnoreCase("create")) {
//                        Integer listSize = originalData.size();
                                    System.out.println("listSize " + ListSize + " last index " + (ListSize - 1));
                                    tblvPurInvList.getSelectionModel().clearAndSelect(0);
                                    tblvPurInvList.requestFocus();
                                    tblvPurInvList.scrollTo(0);
                                } else if (focusStatus.equalsIgnoreCase("edit") && selectedRowIndex != null) {
                                    System.out.println("selected Row Index " + selectedRowIndex);
                                    tblvPurInvList.getSelectionModel().clearAndSelect(ListSize - selectedRowIndex);
                                    tblvPurInvList.requestFocus();
                                    tblvPurInvList.scrollTo(ListSize - selectedRowIndex);
                                } else {
                                    System.out.println("not create");
                                    tfPurInvListSearch.requestFocus();
                                }

                            }
                    );

                    tblvPurInvList.setItems(observableList);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();

        }

    }

    public void getPurchaseInvoiceListUsingDate(String from_date, String to_date) {
        try {
//            if (tfPurInvListFromDt != null && tfPurInvListFromDt.getText() != null && !tfPurInvListFromDt.getText().isEmpty()) {
////                productMap.put("taxApplicableDate", Communicator.text_to_date.fromString(tfPurInvListFromDt.getText()).toString());
//                System.out.println("From Date-->" + Communicator.text_to_date.fromString(tfPurInvListFromDt.getText()).toString());
//            }
            Map<String, String> map = new HashMap<>();
            Map<String, Object> sortObject = new HashMap<>();
            sortObject.put("colId", null);
            sortObject.put("isAsc", true);
            map.put("endDate", to_date);
            map.put("pageNo", "1");
            map.put("pageSize", "100");
            map.put("searchText", "");
            map.put("sort", sortObject.toString());
            map.put("startDate", from_date);
            String formData = Globals.mapToString(map);
            HttpResponse<String> response = APIClient.postJsonRequest(formData, EndPoints.getPurchaseInvoiceList);
            JsonObject responseBody = new Gson().fromJson(response.body(), JsonObject.class);
            ObservableList<PurchaseInvoiceDTO> observableList = FXCollections.observableArrayList();

            if (responseBody.get("responseStatus").getAsInt() == 200) {
                JsonObject obj = responseBody.get("responseObject").getAsJsonObject();
                JsonArray array = obj.get("data").getAsJsonArray();
                if (array.size() > 0) {
                    for (JsonElement element : array) {
                        JsonObject item = element.getAsJsonObject();
                        String id = item.get("id").toString();
                        String purInvoiceNumber = item.get("invoice_no").getAsString();
                        String tranxId = item.get("tranxCode").getAsString();
                        String invoiceDate = item.get("invoice_date").getAsString();
                        String supplierName = item.get("sundry_creditor_name").getAsString();
                        String narration = !item.get("narration").getAsString().isEmpty() ? item.get("narration").toString() : "";
                        String taxable = item.get("taxable_amt").getAsString() != null ? item.get("taxable_amt").getAsString() : "";
                        String tax = item.get("tax_amt").getAsString();
                        String billAmount = String.valueOf(item.get("total_amount").getAsDouble());

                        //add all the above parameter into the constructor
                        observableList.add(new PurchaseInvoiceDTO(id, purInvoiceNumber, tranxId, invoiceDate, supplierName, narration, taxable, tax, billAmount));
                    }
                    //setting the value to the respective column
                    tblcPurInvListInvNo.setCellValueFactory(new PropertyValueFactory<>("purInvoiceNumber"));
                    tblcPurInvListTranxId.setCellValueFactory(new PropertyValueFactory<>("purInvoiceTranxId"));
                    tblcPurInvListInvoiceDate.setCellValueFactory(new PropertyValueFactory<>("purInvoiceDate"));
                    tblcPurInvListSupplierName.setCellValueFactory(new PropertyValueFactory<>("purInvoiceSupplierName"));
                    tblcPurInvListNarration.setCellValueFactory(new PropertyValueFactory<>("purInvoiceNarration"));
                    tblcPurInvListTaxable.setCellValueFactory(new PropertyValueFactory<>("purInvoiceTaxable"));
                    tblcPurInvListTax.setCellValueFactory(new PropertyValueFactory<>("purInvoiceTax"));
                    tblcPurInvListBillAmount.setCellValueFactory(new PropertyValueFactory<>("purInvoiceBillAmount"));

                    tblcPurInvListBarcode.setCellFactory(param -> {
                        final TableCell<PurchaseInvoiceDTO, Void> cell = new TableCell<>() {
                            private ImageView delImg = new ImageView(new Image(String.valueOf(GenivisApplication.class.getResource("ui/assets/barcode.png"))));

                            {
                                delImg.setFitHeight(20.0);
                                delImg.setFitWidth(20.0);
                            }

                            {

                                delImg.setOnMouseClicked(mouseEvent -> {
                                    System.out.println("Barcode Mouse");
                                    mouseEvent.consume();
                                });

                            }

                            HBox hbActions = new HBox();

                            {
                                hbActions.setAlignment(Pos.CENTER);
                                hbActions.getChildren().add(delImg);
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

                    tblvPurInvList.setItems(observableList);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();

        }

    }


    public void handleSearch(String search) {   //for search in list
        ObservableList<PurchaseInvoiceDTO> filterData = FXCollections.observableArrayList();
        if (search.isEmpty()) {
            tblvPurInvList.setItems(orgData);
            return;
        }
        for (PurchaseInvoiceDTO item : orgData) {
            if (matchesKeyword(item, search)) {
                filterData.add(item);
            }
        }
        tblvPurInvList.setItems(filterData);
    }

    public boolean matchesKeyword(PurchaseInvoiceDTO dtoItems, String search) {
        String lowerCase = search.toLowerCase();

        return dtoItems.getPurInvoiceDate().toLowerCase().contains(lowerCase) ||
                dtoItems.getPurInvoiceSupplierName().toLowerCase().contains(lowerCase) ||
                dtoItems.getPurInvoiceBillAmount().toLowerCase().contains(lowerCase);
    }
}
