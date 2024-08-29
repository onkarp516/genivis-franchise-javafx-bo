package com.opethic.genivis.controller.tranx_purchase;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.opethic.genivis.GenivisApplication;
import com.opethic.genivis.controller.tranx_sales.SalesQuotationCreateController;
import com.opethic.genivis.dto.ProductDTO;
import com.opethic.genivis.dto.PurchaseInvoiceDTO;
import com.opethic.genivis.dto.SalesQuotationDTO;
import com.opethic.genivis.dto.reqres.pur_tranx.TranxPurRtnListResDTO;
import com.opethic.genivis.dto.reqres.pur_tranx.TranxPurRtnRowDataListDTO;
import com.opethic.genivis.network.APIClient;
import com.opethic.genivis.network.EndPoints;
import com.opethic.genivis.network.RequestType;
import com.opethic.genivis.utils.DateConvertUtil;
import com.opethic.genivis.utils.FxmFileConstants;
import com.opethic.genivis.utils.GlobalController;
import com.opethic.genivis.utils.Globals;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Screen;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

public class PurchaseReturnListController implements Initializable {
    @FXML
    private BorderPane bpPurReturnRootPane;
    @FXML
    private TableColumn<PurchaseInvoiceDTO, String> tblcPurRtnListNo;
    @FXML
    private TableColumn<PurchaseInvoiceDTO, String> tblcPurInvListTranxId;
    @FXML
    private TableColumn<PurchaseInvoiceDTO, String> tblcPurRtnListDate;
    @FXML
    private TableColumn<PurchaseInvoiceDTO, String> tblcPurRtnListSupplierName;
    @FXML
    private TableColumn<PurchaseInvoiceDTO, String> tblcPurRtnListNarration;
    @FXML
    private TableColumn<PurchaseInvoiceDTO, String> tblcPurRtnListTaxable;
    @FXML
    private TableColumn<PurchaseInvoiceDTO, String> tblcPurRtnListTax;
    @FXML
    private TableColumn<PurchaseInvoiceDTO, String> tblcPurRtnListBillAmount;
    @FXML
    private TableColumn<PurchaseInvoiceDTO, String> tblcPurRtnListPrint;
    @FXML
    private TableView<PurchaseInvoiceDTO> tblvPurRtnList;
    @FXML
    private Button btnPurRtnListCreate;
    private ObservableList<PurchaseInvoiceDTO> orgData;
    @FXML
    private TextField tfPurRtnListSearch, tfPurRtnListFromDt, tfPurRtnListToDt;
    @FXML
    private ScrollPane spRootPane;
    public static Long selectedPrId = 0L;
    public static final Logger purRtnListLogger = LoggerFactory.getLogger(PurchaseReturnListController.class);

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        ResponsiveWiseCssPicker();

        //todo : autofocus on TranxDate
        Platform.runLater(() -> tfPurRtnListSearch.requestFocus());
//         Enter traversal
        bpPurReturnRootPane.addEventFilter(KeyEvent.KEY_PRESSED, (KeyEvent event) -> {
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
        //code to write when the window opens
        tblvPurRtnList.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        getPurchaseInvoiceList();
        orgData = tblvPurRtnList.getItems();
        tfPurRtnListSearch.textProperty().addListener((observableValue, oldValue, newValue) -> {
            handleSearch(newValue.trim());
        });
        purRtnListLogger.debug("Before Double click event to get the id from selected row of product list table in " +
                "tvProductList.setRowFactory() ");
        tblvPurRtnList.setRowFactory(tv -> {
            TableRow<PurchaseInvoiceDTO> row = new TableRow<>();
            row.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    if (mouseEvent.getClickCount() == 2) {
                        purRtnEditPage();
                    }
                }
            });
            return row;
        });

    }

    private void ResponsiveWiseCssPicker() {
        double height = Screen.getPrimary().getBounds().getHeight();
        double width = Screen.getPrimary().getBounds().getWidth();
        System.out.println("width" + width);
        if (width >= 992 && width <= 1024) {
            bpPurReturnRootPane.getStylesheets().add(GenivisApplication.class.getResource("ui/css/TranxCommonCssStyles/tranxCommonCssStyle1.css").toExternalForm());
        } else if (width >= 1025 && width <= 1280) {
            bpPurReturnRootPane.getStylesheets().add(GenivisApplication.class.getResource("ui/css/TranxCommonCssStyles/tranxCommonCssStyle2.css").toExternalForm());
        } else if (width >= 1281 && width <= 1368) {
            bpPurReturnRootPane.getStylesheets().add(GenivisApplication.class.getResource("ui/css/TranxCommonCssStyles/tranxCommonCssStyle3.css").toExternalForm());
        } else if (width >= 1369 && width <= 1400) {
            bpPurReturnRootPane.getStylesheets().add(GenivisApplication.class.getResource("ui/css/TranxCommonCssStyles/tranxCommonCssStyle4.css").toExternalForm());
        } else if (width >= 1401 && width <= 1440) {
            bpPurReturnRootPane.getStylesheets().add(GenivisApplication.class.getResource("ui/css/TranxCommonCssStyles/tranxCommonCssStyle5.css").toExternalForm());
        } else if (width >= 1441 && width <= 1680) {
            bpPurReturnRootPane.getStylesheets().add(GenivisApplication.class.getResource("ui/css/TranxCommonCssStyles/tranxCommonCssStyle6.css").toExternalForm());
        } else if (width >= 1681 && width <= 1920) {
            bpPurReturnRootPane.getStylesheets().add(GenivisApplication.class.getResource("ui/css/TranxCommonCssStyles/tranxCommonCssStyle7.css").toExternalForm());
        }
    }

    //function to GET the list of purchase invoice
    public void getPurchaseInvoiceList() {
        APIClient apiClient = null;
        ObservableList<PurchaseInvoiceDTO> observableList = FXCollections.observableArrayList();
        try {
            Map<String, String> map = new HashMap<>();
            Map<String, Object> sortObject = new HashMap<>();
            sortObject.put("colId", null);
            sortObject.put("isAsc", true);
            map.put("endDate", "");
            map.put("pageNo", "1");
            map.put("pageSize", "100");
            map.put("searchText", "");
            map.put("sort", sortObject.toString());
            map.put("startDate", tfPurRtnListFromDt.getText());
            map.put("endDate", tfPurRtnListToDt.getText());
            String formData = Globals.mapToString(map);
            //            HttpResponse<String> response = APIClient.postJsonRequest(formData, EndPoints.GET_PUR_RETURN_LIST);
            apiClient = new APIClient(EndPoints.GET_PUR_RETURN_LIST, formData, RequestType.JSON);
            apiClient.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
                @Override
                public void handle(WorkerStateEvent workerStateEvent) {
                    String res = workerStateEvent.getSource().getValue().toString();
                    TranxPurRtnListResDTO responseBody = new Gson().fromJson(res, TranxPurRtnListResDTO.class);
                    if (responseBody.getResponseStatus() == 200) {
                        List<TranxPurRtnRowDataListDTO> list = responseBody.getResponseObject().getData();
                        if (list.size() > 0) {
                            for (TranxPurRtnRowDataListDTO element : list) {
                                String id = "" + element.getId();
                                String purInvoiceNumber = "" + element.getPurReturnNo();
                                String tranxId = element.getTranxCode();
                                String invoiceDate = DateConvertUtil.convertDispDateFormat(element.getPurchaseReturnDate());
                                String supplierName = element.getSundryCreditorName();
                                String narration = element.getNarration();
                                String taxable = element.getTaxableAmt() != null ? element.getTaxableAmt().toString() : "";
                                String tax = element.getTaxAmt() != null ? element.getTaxAmt().toString() : "";
                                String billAmount = String.valueOf(element.getTotalAmount());
                                System.out.println("id " + id + " purInvoiceNumber " + purInvoiceNumber + " tranxId " + tranxId + " invoicedate " + invoiceDate + " suppName " + supplierName +
                                        " narra " + narration + " taxable " + taxable + " tax " + tax + " billamt " + billAmount);
                                observableList.add(new PurchaseInvoiceDTO(id, purInvoiceNumber, tranxId, invoiceDate,
                                        supplierName, narration, taxable, tax, billAmount));
                            }
                            tblcPurRtnListNo.setCellValueFactory(new PropertyValueFactory<>("purInvoiceNumber"));
                            tblcPurInvListTranxId.setCellValueFactory(new PropertyValueFactory<>("purInvoiceTranxId"));
                            tblcPurRtnListDate.setCellValueFactory(new PropertyValueFactory<>("purInvoiceDate"));
                            tblcPurRtnListSupplierName.setCellValueFactory(new PropertyValueFactory<>("purInvoiceSupplierName"));
                            tblcPurRtnListNarration.setCellValueFactory(new PropertyValueFactory<>("purInvoiceNarration"));
                            tblcPurRtnListTaxable.setCellValueFactory(new PropertyValueFactory<>("purInvoiceTaxable"));
                            tblcPurRtnListTax.setCellValueFactory(new PropertyValueFactory<>("purInvoiceTax"));
                            tblcPurRtnListBillAmount.setCellValueFactory(new PropertyValueFactory<>("purInvoiceBillAmount"));

                            tblvPurRtnList.setItems(observableList);
                        }
                    }
                }
            });
            apiClient.setOnCancelled(new EventHandler<WorkerStateEvent>() {
                @Override
                public void handle(WorkerStateEvent workerStateEvent) {

                }
            });
            apiClient.setOnFailed(new EventHandler<WorkerStateEvent>() {
                @Override
                public void handle(WorkerStateEvent workerStateEvent) {

                }
            });
            apiClient.start();


        } catch (Exception e) {
            e.printStackTrace();

        }


    }

    public void handleSearch(String search) {   //for search in list
        ObservableList<PurchaseInvoiceDTO> filterData = FXCollections.observableArrayList();
        if (search.isEmpty()) {
            tblvPurRtnList.setItems(orgData);
            return;
        }
        for (PurchaseInvoiceDTO item : orgData) {
            if (matchesKeyword(item, search)) {
                filterData.add(item);
            }
        }
        tblvPurRtnList.setItems(filterData);
    }

    public boolean matchesKeyword(PurchaseInvoiceDTO dtoItems, String search) {
        String lowerCase = search.toLowerCase();

        return dtoItems.getPurInvoiceDate().toLowerCase().contains(lowerCase) ||
                dtoItems.getPurInvoiceSupplierName().toLowerCase().contains(lowerCase) ||
                dtoItems.getPurInvoiceBillAmount().toLowerCase().contains(lowerCase);
    }

    private void purRtnEditPage() {
        try {
            PurchaseInvoiceDTO returnDto = tblvPurRtnList.getSelectionModel().getSelectedItem();
            selectedPrId = Long.parseLong(returnDto.getPurInvoiceId());
            GlobalController.getInstance().addTabStatic1(FxmFileConstants.PURCHASE_RETURN_EDIT_SLUG, false, selectedPrId.intValue());
        } catch (Exception e) {

        }

    }

    public static Long getPurRtnInvoiceId() {
        return selectedPrId;
    }
}
