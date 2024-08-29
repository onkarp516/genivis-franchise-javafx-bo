package com.opethic.genivis.controller.Reports;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.opethic.genivis.dto.AccountsDayBookDTO;
import com.opethic.genivis.dto.LedgerReport2DTO;
import com.opethic.genivis.dto.StockReport1DTO;
import com.opethic.genivis.dto.StockReport2DTO;
import com.opethic.genivis.dto.reqres.product.Communicator;
import com.opethic.genivis.network.APIClient;
import com.opethic.genivis.network.EndPoints;
import com.opethic.genivis.network.RequestType;
import com.opethic.genivis.utils.DateValidator;
import com.opethic.genivis.utils.GlobalController;
import com.opethic.genivis.utils.Globals;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;
import java.net.http.HttpResponse;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.function.Function;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;

import static com.opethic.genivis.utils.FxmFileConstants.*;

public class StocksStockReport2Controller implements Initializable {
    private static final Logger StockReport2ListLogger = LoggerFactory.getLogger(StocksStockReport2Controller.class);
    @FXML
    public BorderPane borderpane;
    @FXML
    private CheckBox cbYearly;
    @FXML
    private Label cls_bln_qty;
    @FXML
    private Label cls_bln_unt;
    @FXML
    private Label cls_bln_vle;
    @FXML
    private ComboBox<String> cbType;
    @FXML
    private ComboBox<String> cbFilter;
    @FXML
    private Label lblPurchaseTotalQty;
    @FXML
    private Label lblPurchaseTotalAmount;
    @FXML
    private Label lblSaleTotalQty;
    @FXML
    private Label lblSaleTotalAmount;
    @FXML
    private Label lblClsBlnTotalQty;
    @FXML
    private Label lblClsBlnTotalAmount;
    @FXML
    private Label product_label;
    @FXML
    private TextField tfStockReport2ListSearch;
    @FXML
    private TextField tfStartDt;
    @FXML
    private TextField tfEndDt;
    String Start_date = "";
    String End_date = "", selectedType = "", selectedFilter = "";
    @FXML
    private TextField tfNumbFDt;
    @FXML
    private TableView<StockReport2DTO> tblvStockReport2List;
    @FXML
    private TableColumn<StockReport2DTO, String> tblStockReport2Date, tblStockReport2InvoiceNo, tblStockReport2Particulars, tblStockReport2VoucherType, 
            tblStockReport2PurchaseQty, tblStockReport2PurchaseUnit, tblStockReport2PurchaseValue, tblStockReport2SalesQty, tblStockReport2SalesUnit, 
            tblStockReport2SalesValue, tblStockReport2ClosingBalanceQty, tblStockReport2ClosingBalanceUnit, tblStockReport2ClosingBalanceValue;

    private ObservableList<StockReport2DTO> originalData;

    private JsonObject jsonObject = null;

    @FXML
    private Button btExportPdf, btExportExcel, btExportCsv, btExportPrint;

    public static String product_name = "";
    public static String closing_qty = "";
    public static String closing_value = "";

    private Integer stockReportId = -1;
    public void setEditId(Integer InId) {
        stockReportId = InId;
        System.out.println("stockReportId"+stockReportId);
        if (InId > -1) {
            setEditData();
        }
    }
    private void setEditData() {
        fetchStockReport2List(String.valueOf(stockReportId));
    }

    private void handleTypeComboBoxAction(ActionEvent event) {
        selectedType = cbType.getSelectionModel().getSelectedItem();
//        System.out.println("Selected Type: " + selectedType);

        if (cbType.getSelectionModel().getSelectedItem() == "Sale Invoice") {
            selectedType = "sales invoice";
//            System.out.println("selectedType " + selectedType);
        } else if (cbType.getSelectionModel().getSelectedItem() == "Purchase Return") {
            selectedType = "purchase return";
//            System.out.println("selectedType " + selectedType);
        } else if (cbType.getSelectionModel().getSelectedItem() == "Sale Return") {
            selectedType = "sales return";
//            System.out.println("selectedType " + selectedType);
        } else if (cbType.getSelectionModel().getSelectedItem() == "Purchase Return") {
            selectedType = "purchase return";
//            System.out.println("selectedType " + selectedType);
        } else if (cbType.getSelectionModel().getSelectedItem() == "Purchase Challan") {
            selectedType = "purchase challan";
//            System.out.println("selectedType " + selectedType);
        } else if (cbType.getSelectionModel().getSelectedItem() == "Sales Challan") {
            selectedType = "sales challan";
//            System.out.println("selectedType " + selectedType);
        } else if (cbType.getSelectionModel().getSelectedItem() == "All") {
            selectedType = "all";
//            System.out.println("selectedType " + selectedType);
        }
        fetchStockReport2ListByFilter();
    }

    private void handleFilterComboBoxAction(ActionEvent event) {
        selectedFilter = cbFilter.getSelectionModel().getSelectedItem();
//        System.out.println("Selected Filter: " + selectedFilter);
        if (cbFilter.getSelectionModel().getSelectedItem() == "Supplier") {
            selectedFilter = "supplier";
//            System.out.println("selectedType- " + selectedFilter);
        } else if (cbFilter.getSelectionModel().getSelectedItem() == "Invoice No") {
            selectedFilter = "invoice no";
//            System.out.println("selectedType- " + selectedFilter);
        } else if (cbFilter.getSelectionModel().getSelectedItem() == "Voucher Type") {
            selectedFilter = "voucher type";
//            System.out.println("selectedType- " + selectedFilter);
        }
        fetchStockReport2ListByFilterSearch();
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        tblvStockReport2List.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        StocksStockReport2TableDesign();

        StockReport2ListLogger.info("Start of Initialize method of : StocksStockReport2Controller");
        Platform.runLater(() -> tfStockReport2ListSearch.requestFocus());
        product_label.setText("Product : " + product_name);
        borderpane.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ESCAPE) {
//                System.out.println("Esc Clicked....!");
                GlobalController.getInstance().addTabStatic(STOCKS_STOCK_REPORT1_LIST_SLUG, false);
            }
        });

        fetchStockReport2List("");

        cbYearly.selectedProperty().addListener((obs, oldValueYr, newValueYr) -> {
            if (newValueYr) {
//                System.out.println("Year CheckBox Selected");
                StocksStockReport3Controller.product_name = product_name;
//                System.out.println(product_name);
                GlobalController.getInstance().addTabStatic1(STOCKS_STOCK_REPORT3_LIST_SLUG, false, stockReportId);
            } else {
                System.out.println("Year CheckBox Not Selected");
            }
        });

//        tfNumbFDt.setOnKeyPressed(event ->{
//            if(event.getCode() == KeyCode.ENTER){
//                System.out.println("Entered Value " + tfNumbFDt.getText());
//                fetchStockReport2List("");
//
//            }
//        });

//        tfEndDt.setOnKeyPressed(event ->{
//            if(event.getCode() == KeyCode.ENTER){
////                System.out.println("DateFilter From Date " + tfStartDt.getText().toString());
////                System.out.println("DateFilter To Date " + tfEndDt.getText().toString());
//                fetchStockReport2List("");
//            }
//        });


        borderpane.sceneProperty().addListener((observable, oldScene, newScene) -> {
            if (newScene != null && newScene.getWindow() instanceof Stage) {
                Communicator.stage = (Stage) newScene.getWindow();
            }
        });

        DateValidator.applyDateFormat(tfStartDt);
        DateValidator.applyDateFormat(tfEndDt);

        cbType.getItems().addAll("All", "Sale Invoice", "Purchase Invoice", "Sale Return", "Purchase Return", "Purchase Challan", "Sales Challan");
        cbType.setPromptText("Select Type");
        cbType.setOnAction(this::handleTypeComboBoxAction);
        cbType.setValue(cbType.getItems().get(0));
        selectedType = "all";
        //todo:open Filter dropdown on Space
        cbType.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode() == KeyCode.SPACE && !cbType.isShowing()) {
                cbType.show();
                event.consume(); // Consume the event to prevent other actions
            }
        });

        cbFilter.getItems().addAll("Supplier", "Invoice No", "Voucher Type");
        cbFilter.setPromptText("Filter Search");
        cbFilter.setOnAction(this::handleFilterComboBoxAction);
        cbFilter.setValue("Supplier");
        handleFilterComboBoxAction(null);
        //todo:open Filter dropdown on Space
        cbFilter.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode() == KeyCode.SPACE && !cbFilter.isShowing()) {
                cbFilter.show();
                event.consume(); // Consume the event to prevent other actions
            }
        });

        nodetraversal(cbFilter, tfStockReport2ListSearch);
        nodetraversal(tfStockReport2ListSearch, tfNumbFDt);
        nodetraversal(tfNumbFDt, tfStartDt);
        nodetraversal(tfStartDt, tfEndDt);
        nodetraversal(tfEndDt, cbYearly);
        nodetraversal(cbYearly, cbType);
        nodetraversal(cbType, btExportPdf);
        nodetraversal(btExportPdf, btExportExcel);
        nodetraversal(btExportExcel, btExportCsv);
        nodetraversal(btExportCsv, btExportPrint);
        tblvStockReport2List.requestFocus();

        btExportPdf.focusedProperty().addListener((obs, wasFocused, isNowFocused) -> {
            if (isNowFocused) {
                btExportPdf.setStyle("-fx-border-width: 2; -fx-border-color: #03dbfc; -fx-background-color: transparent;");
            } else {
                btExportPdf.setStyle("-fx-background-color: transparent;");
            }
        });
        btExportPdf.hoverProperty().addListener((obs, wasHovered, isNowHovered) -> {
            if (isNowHovered) {
                btExportPdf.setStyle("-fx-border-width: 2; -fx-border-color: #03dbfc; -fx-background-color: transparent;");
            } else {
                btExportPdf.setStyle("-fx-background-color: transparent;");
            }
        });

        btExportExcel.focusedProperty().addListener((obs, wasFocused, isNowFocused) -> {
            if (isNowFocused) {
                btExportExcel.setStyle("-fx-border-width: 2; -fx-border-color: #03dbfc; -fx-background-color: transparent;");
            } else {
                btExportExcel.setStyle("-fx-background-color: transparent;");
            }
        });
        btExportExcel.hoverProperty().addListener((obs, wasHovered, isNowHovered) -> {
            if (isNowHovered) {
                btExportExcel.setStyle("-fx-border-width: 2; -fx-border-color: #03dbfc; -fx-background-color: transparent;");
            } else {
                btExportExcel.setStyle("-fx-background-color: transparent;");
            }
        });

        btExportCsv.focusedProperty().addListener((obs, wasFocused, isNowFocused) -> {
            if (isNowFocused) {
                btExportCsv.setStyle("-fx-border-width: 2; -fx-border-color: #03dbfc; -fx-background-color: transparent;");
            } else {
                btExportCsv.setStyle("-fx-background-color: transparent;");
            }
        });
        btExportCsv.hoverProperty().addListener((obs, wasHovered, isNowHovered) -> {
            if (isNowHovered) {
                btExportCsv.setStyle("-fx-border-width: 2; -fx-border-color: #03dbfc; -fx-background-color: transparent;");
            } else {
                btExportCsv.setStyle("-fx-background-color: transparent;");
            }
        });

        btExportPrint.focusedProperty().addListener((obs, wasFocused, isNowFocused) -> {
            if (isNowFocused) {
                btExportPrint.setStyle("-fx-border-width: 2; -fx-border-color: #03dbfc; -fx-background-color: transparent;");
            } else {
                btExportPrint.setStyle("-fx-background-color: transparent;");
            }
        });
        btExportPrint.hoverProperty().addListener((obs, wasHovered, isNowHovered) -> {
            if (isNowHovered) {
                btExportPrint.setStyle("-fx-border-width: 2; -fx-border-color: #03dbfc; -fx-background-color: transparent;");
            } else {
                btExportPrint.setStyle("-fx-background-color: transparent;");
            }
        });
    }

    private void nodetraversal(Node current_node, Node next_node) {
        current_node.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {

                if("tfNumbFDt".equals(current_node.getId())){
                    getDays();
                } else if("tfEndDt".equals(current_node.getId())){
                    getEndDate();
                }

                next_node.requestFocus();
                event.consume();
            }
            if (event.getCode() == KeyCode.TAB) {
                if("tfNumbFDt".equals(current_node.getId())){
                    getDays();
                } else if("tfEndDt".equals(current_node.getId())){
                    getEndDate();
                }
            }

            if (event.getCode() == KeyCode.SPACE) {
                if (current_node instanceof Button button) {
                    button.fire();
                }
            }
        });
    }

    private void getEndDate() {
//        System.out.println("DateFilter From Date " + tfStartDt.getText().toString());
//        System.out.println("DateFilter To Date " + tfEndDt.getText().toString());
        fetchStockReport2List("");
    }

    private void getDays() {
//        System.out.println("text " +tfNumbFDt.getText());
        fetchStockReport2List("");
    }

    private void StocksStockReport2TableDesign() {
        //todo: Responsive code for tableview
        tblStockReport2Date.prefWidthProperty().bind(tblvStockReport2List.widthProperty().multiply(0.07));
        tblStockReport2InvoiceNo.prefWidthProperty().bind(tblvStockReport2List.widthProperty().multiply(0.07));
        tblStockReport2Particulars.prefWidthProperty().bind(tblvStockReport2List.widthProperty().multiply(0.12));
        tblStockReport2VoucherType.prefWidthProperty().bind(tblvStockReport2List.widthProperty().multiply(0.09));
        tblStockReport2PurchaseQty.prefWidthProperty().bind(tblvStockReport2List.widthProperty().multiply(0.06));
        tblStockReport2PurchaseUnit.prefWidthProperty().bind(tblvStockReport2List.widthProperty().multiply(0.09));
        tblStockReport2PurchaseValue.prefWidthProperty().bind(tblvStockReport2List.widthProperty().multiply(0.07));
        tblStockReport2SalesQty.prefWidthProperty().bind(tblvStockReport2List.widthProperty().multiply(0.06));
        tblStockReport2SalesUnit.prefWidthProperty().bind(tblvStockReport2List.widthProperty().multiply(0.09));
        tblStockReport2SalesValue.prefWidthProperty().bind(tblvStockReport2List.widthProperty().multiply(0.07));
        tblStockReport2ClosingBalanceQty.prefWidthProperty().bind(tblvStockReport2List.widthProperty().multiply(0.06));
        tblStockReport2ClosingBalanceUnit.prefWidthProperty().bind(tblvStockReport2List.widthProperty().multiply(0.09));
        tblStockReport2ClosingBalanceValue.prefWidthProperty().bind(tblvStockReport2List.widthProperty().multiply(0.06));
    }

    private void fetchStockReport2List(String searchKey) {
        StockReport2ListLogger.info("Fetch Stock Report 2 List  : StocksStockReport2Controller");

        APIClient apiClient = null;

        try {
            StockReport2ListLogger.debug("Get Stock Report 2 Data Started...");
//            String purchase_qty;
//            String purchase_unit;
//            String purchase_value;
//            String sales_qty;
//            String sales_unit;
//            String sales_value;
            Map<String, String> body = new HashMap<>();
            body.put("productId", String.valueOf(stockReportId));
            if(!tfNumbFDt.getText().equalsIgnoreCase("")){
                body.put("daysInNumber", tfNumbFDt.getText());
            }
            else if(!tfStartDt.getText().equalsIgnoreCase("")  || !tfEndDt.getText().equalsIgnoreCase("")){
                System.out.println("date sdf " );
                body.put("start_date", Communicator.text_to_date.fromString(tfStartDt.getText()).toString());
                body.put("end_date", Communicator.text_to_date.fromString(tfEndDt.getText()).toString());
            }else {
                body.put("duration", "month");
            }
//            System.out.println("body ----------->  " + body);
            String requestBody = Globals.mapToStringforFormData(body);
//            System.out.println("fetchStockReport2List requestBody" + requestBody);
//            HttpResponse<String> response = APIClient.postFormDataRequest(requestBody, EndPoints.STOCKS_STOCK_REPORT2_LIST);
            apiClient = new APIClient(EndPoints.STOCKS_STOCK_REPORT2_LIST, requestBody, RequestType.FORM_DATA);
            apiClient.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
                String purchase_qty;
                String purchase_unit;
                String purchase_value;
                String sales_qty;
                String sales_unit;
                String sales_value;
                @Override
                public void handle(WorkerStateEvent workerStateEvent) {
//                    JsonObject responseBody = new Gson().fromJson(response.body(), JsonObject.class);
                    jsonObject = new Gson().fromJson(workerStateEvent.getSource().getValue().toString(), JsonObject.class);
//            System.out.println("fetchStockReport2List response" + response);
//            System.out.println("fetchStockReport2List responseBody" + responseBody);
                    ObservableList<StockReport2DTO> observableList = FXCollections.observableArrayList();
                    if(jsonObject.get("responseStatus").getAsInt() == 200){
                        JsonArray responseList = jsonObject.getAsJsonArray("data");
//                System.out.println("fetchStockReport2List" + responseList);
                        String Opening_bal_qty = jsonObject.get("total_opening_qty").getAsString();
                        String Opening_bal_unit = jsonObject.get("unit").getAsString();
                        String Opening_bal_costing = jsonObject.get("costing").getAsString();
                        Double cost_for_closing = jsonObject.get("cost_for_closing").getAsDouble();
                        Long totalClosingQty = jsonObject.get("total_opening_qty").getAsLong();
                        StocksStockReport3Controller.closing_qty = Opening_bal_qty; // passing qty
                        StocksStockReport3Controller.closing_value = Opening_bal_costing; // passing value
                        Start_date = (jsonObject.get("d_start_date").getAsString());
                        End_date = jsonObject.get("d_end_date").getAsString();
                        LocalDate startLocalDt = LocalDate.parse(Start_date);
                        LocalDate endLocalDt = LocalDate.parse(End_date);

                        double closingQty = 0.0;
                        double closingRate = 0.0;

                        // Outside the loop
                        Long lastClosingBalanceQty = 0L;
                        Double lastFormattedClosingValue = 0.0;

                        if (!responseList.equals("") && responseList.size() > 0){
                            tblvStockReport2List.setVisible(true);
                            for (JsonElement element : responseList){
                                JsonObject item = element.getAsJsonObject();
                                String id = item.get("id") != null ? item.get("id").getAsString() : "";
                                String date = item.get("date") != null ? item.get("date").getAsString() : "";
                                String invoice_no = item.get("invoice_no") != null ? item.get("invoice_no").getAsString() : "";
                                String particulars_name = item.get("particular") != null ? item.get("particular").getAsString() : "";
                                String voucher_type = item.get("tranx_type") != null ? item.get("tranx_type").getAsString() : "";
                                String tranx_unique_code = item.get("tranx_unique_code") != null ? item.get("tranx_unique_code").getAsString() : "";
                                if (tranx_unique_code.equalsIgnoreCase("PRS") ||
                                        tranx_unique_code.equalsIgnoreCase("PRSRT") ||
                                        tranx_unique_code.equalsIgnoreCase("PRSCHN")  ){
                                    purchase_qty = item.get("qty") != null ? item.get("qty").getAsString() : "";
                                    purchase_unit = item.get("unit") != null ? item.get("unit").getAsString() : "";
                                    purchase_value = item.get("value") != null ? item.get("value").getAsString() : "";
                                }else {
                                    purchase_qty =  "";
                                    purchase_unit = "";
                                    purchase_value = "";
                                }

                                if (tranx_unique_code.equalsIgnoreCase("SLS") ||
                                        tranx_unique_code.equalsIgnoreCase("CNTS") ||
                                        tranx_unique_code.equalsIgnoreCase("SLSRT") ||
                                        tranx_unique_code.equalsIgnoreCase("SLSCHN") ){
                                    sales_qty = item.get("qty") != null ? item.get("qty").getAsString() : "";
                                    sales_unit = item.get("unit") != null ? item.get("unit").getAsString() : "";
                                    sales_value = item.get("value") != null ? item.get("value").getAsString() : "";
                                }else {
                                    sales_qty =  "";
                                    sales_unit = "";
                                    sales_value = "";
                                }

                                Long closing_balance_qty1 = item.get("qty") != null ? item.get("qty").getAsLong() : 0;

                                if (tranx_unique_code.equalsIgnoreCase("PRS") ||
                                        tranx_unique_code.equalsIgnoreCase("PRSCHN") ||
                                        tranx_unique_code.equalsIgnoreCase("SLSRT")) {
                                    totalClosingQty = totalClosingQty + closing_balance_qty1;
                                    System.out.println("totalClosingQty purchase "+ totalClosingQty);
                                } else if (tranx_unique_code.equalsIgnoreCase("SLSCHN") ||
                                        tranx_unique_code.equalsIgnoreCase("SLS") ||
                                        tranx_unique_code.equalsIgnoreCase("PRSRT") ||
                                        tranx_unique_code.equalsIgnoreCase("CNTS")) {
                                    totalClosingQty = totalClosingQty - closing_balance_qty1;
                                }

                                double totalValue = cost_for_closing * totalClosingQty;
//                        System.out.println("totalValue" + totalValue);

                                JsonElement valueElement = item.get("value");
                                if (valueElement != null && !valueElement.isJsonNull()) {
                                    closingRate = valueElement.getAsDouble();
                                } else {
                                    closingRate = 0.0; // Default to 0 if value is not available
                                }


                                // Get the closing_balance_unit and formattedClosingValue from the last item
                                lastClosingBalanceQty = totalClosingQty;
                                lastFormattedClosingValue = totalValue;

                                String closing_balance_qty = item.get("closing_qty") != null ? item.get("closing_qty").getAsString() : "";
                                String closing_balance_unit = item.get("unit") != null ? item.get("unit").getAsString() : "";
                                String closing_balance_value = item.get("closing_rate") != null ? item.get("closing_rate").getAsString() : "";

                                observableList.add(new StockReport2DTO(id, date, invoice_no, particulars_name, voucher_type, purchase_qty, purchase_unit,
                                        purchase_value, sales_qty, sales_unit, sales_value, totalClosingQty.toString(), closing_balance_unit, String.valueOf(totalValue)));
                            }


                            // Set the last values to the corresponding labels
                            lblClsBlnTotalQty.setText(lastClosingBalanceQty.toString());
                            lblClsBlnTotalAmount.setText(lastFormattedClosingValue.toString());

                            cls_bln_qty.setText(String.valueOf("Qty : " + Opening_bal_qty));
                            cls_bln_unt.setText(String.valueOf("Unit : " + Opening_bal_unit));
                            cls_bln_vle.setText(String.valueOf("Value : " + Opening_bal_costing));

                            tfStartDt.setText(startLocalDt.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
                            tfEndDt.setText(endLocalDt.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));

                            tblStockReport2Date.setCellValueFactory(new PropertyValueFactory<>("date"));
                            tblStockReport2InvoiceNo.setCellValueFactory(new PropertyValueFactory<>("invoice_no"));
                            tblStockReport2Particulars.setCellValueFactory(new PropertyValueFactory<>("particulars_name"));
                            tblStockReport2VoucherType.setCellValueFactory(new PropertyValueFactory<>("voucher_type"));
                            tblStockReport2PurchaseQty.setCellValueFactory(new PropertyValueFactory<>("purchase_qty"));
                            tblStockReport2PurchaseUnit.setCellValueFactory(new PropertyValueFactory<>("purchase_unit"));
                            tblStockReport2PurchaseValue.setCellValueFactory(new PropertyValueFactory<>("purchase_value"));
                            tblStockReport2SalesQty.setCellValueFactory(new PropertyValueFactory<>("sales_qty"));
                            tblStockReport2SalesUnit.setCellValueFactory(new PropertyValueFactory<>("sales_unit"));
                            tblStockReport2SalesValue.setCellValueFactory(new PropertyValueFactory<>("sales_value"));
                            tblStockReport2ClosingBalanceQty.setCellValueFactory(new PropertyValueFactory<>("closing_balance_qty"));
                            tblStockReport2ClosingBalanceUnit.setCellValueFactory(new PropertyValueFactory<>("closing_balance_unit"));
                            tblStockReport2ClosingBalanceValue.setCellValueFactory(new PropertyValueFactory<>("closing_balance_value"));

                            tblvStockReport2List.setItems(observableList);
                            originalData = observableList;
                            calculateTotalAmount();
                            StockReport2ListLogger.debug("Success in Displaying Stock Report 2 List : StocksStockReport2Controller");
                        } else {
                            observableList.add(new StockReport2DTO("", "", "", "", "", "", "",
                                    "", "", "", "", "", "", ""));
                            tblvStockReport2List.setItems(observableList);
                            calculateTotalAmount();
//                    System.out.println("Response List is Blank");
                            StockReport2ListLogger.debug("Stock Report 2 List ResponseObject is null : StocksStockReport2Controller");
                        }
                    }else {
//                System.out.println("Response List is Blank2");
                        calculateTotalAmount();
                        StockReport2ListLogger.debug("Error in response of Stock Report 2 List : StocksStockReport2Controller");
                    }
                }
            });
            apiClient.setOnCancelled(new EventHandler<WorkerStateEvent>() {
                @Override
                public void handle(WorkerStateEvent workerStateEvent) {
                    StockReport2ListLogger.error("Network API cancelled in fetchStockReport2List()" + workerStateEvent.getSource().getValue().toString());

                }
            });

            apiClient.setOnFailed(new EventHandler<WorkerStateEvent>() {
                @Override
                public void handle(WorkerStateEvent workerStateEvent) {
                    StockReport2ListLogger.error("Network API failed in fetchStockReport2List()" + workerStateEvent.getSource().getValue().toString());
                }
            });
            apiClient.start();
            StockReport2ListLogger.debug("Get Stock Report 2 Data End...");
        } catch (Exception e) {
//            System.out.println("error in api");
            StockReport2ListLogger.error("Stock Report 2 List Error is " + e.getMessage());
            e.printStackTrace();
        }
        finally {
            apiClient = null;
        }
    }

    private void fetchStockReport2ListByFilter() {
//        System.out.println("fetchStockReport2ListByFilter() working..............");
        ObservableList<StockReport2DTO> filteredData = FXCollections.observableArrayList();

        if (selectedType != "all") {
            filteredData = originalData.stream().filter((item -> item.getVoucher_type().toString().equalsIgnoreCase(selectedType))).collect(Collectors.toCollection(FXCollections::observableArrayList));

        } else {
            filteredData = originalData.stream().collect(Collectors.toCollection(FXCollections::observableArrayList));
        }

        tblvStockReport2List.setItems(filteredData);
        calculateTotalAmount();
    }


    private void fetchStockReport2ListByFilterSearch() {
        ObservableList<StockReport2DTO> filteredData = FXCollections.observableArrayList();

        if (selectedFilter.equals("supplier")) {
            tfStockReport2ListSearch.textProperty().addListener((observable, oldValue, newValue) -> {
                filterData(newValue.trim(), StockReport2DTO::getParticulars_name);
            });
            calculateTotalAmount();
        } else if (selectedFilter.equals("invoice no")) {
            tfStockReport2ListSearch.textProperty().addListener((observable, oldValue, newValue) -> {
                filterData(newValue.trim(), StockReport2DTO::getInvoice_no);
            });
            calculateTotalAmount();
        } else if (selectedFilter.equals("voucher type")) {
            tfStockReport2ListSearch.textProperty().addListener((observable, oldValue, newValue) -> {
                filterData(newValue.trim(), StockReport2DTO::getVoucher_type);
            });
            calculateTotalAmount();
        }
    }

    private void filterData(String keyword, Function<StockReport2DTO, String> fieldExtractor) {
        ObservableList<StockReport2DTO> filteredData = FXCollections.observableArrayList();
        StockReport2ListLogger.error("Search Stock Report 2 List in StocksStockReport2Controller");
        if (keyword.isEmpty()) {
            tblvStockReport2List.setItems(originalData);
            calculateTotalAmount();
            return;
        }
        for (StockReport2DTO item : originalData) {
            if (matchesKeyword(item, keyword, fieldExtractor)) {
                filteredData.add(item);
            }
        }
        tblvStockReport2List.setItems(filteredData);
        calculateTotalAmount();
    }

    private boolean matchesKeyword(StockReport2DTO item, String keyword, Function<StockReport2DTO, String> fieldExtractor) {
        String lowerCaseKeyword = keyword.toLowerCase();
        String fieldValue = fieldExtractor.apply(item).toLowerCase();
        return fieldValue.contains(lowerCaseKeyword);
    }

    private void calculateTotalAmount() {
        ObservableList<StockReport2DTO> filteredData = tblvStockReport2List.getItems();
        // Calculate the Totals
        double totalPurchaseQty = 0.0;
        double totalPurchaseAmount = 0.0;
        double totalSaleQty = 0.0;
        double totalSaleAmount = 0.0;
        for (StockReport2DTO item : filteredData) {
            if(item.getVoucher_type().toString().equalsIgnoreCase("purchase invoice")){
                totalPurchaseQty += Double.parseDouble(item.getPurchase_qty().isEmpty() ? "0.0" : item.getPurchase_qty());
                totalPurchaseAmount += Double.parseDouble(item.getPurchase_value().isEmpty() ? "0.0" : item.getPurchase_value());
            } else if(item.getVoucher_type().toString().equalsIgnoreCase("purchase challan")){
                totalPurchaseQty += Double.parseDouble(item.getPurchase_qty().isEmpty() ? "0.0" : item.getPurchase_qty());
                totalPurchaseAmount += Double.parseDouble(item.getPurchase_value().isEmpty() ? "0.0" : item.getPurchase_value());
            } else if(item.getVoucher_type().toString().equalsIgnoreCase("purchase return")){
                totalPurchaseQty += Double.parseDouble(item.getPurchase_qty().isEmpty() ? "0.0" : item.getPurchase_qty());
                totalPurchaseAmount += Double.parseDouble(item.getPurchase_value().isEmpty() ? "0.0" : item.getPurchase_value());
            }

            if(item.getVoucher_type().toString().equalsIgnoreCase("sales invoice")){
                totalSaleQty += Double.parseDouble(item.getSales_qty().isEmpty() ? "0.0" : item.getSales_qty());
                totalSaleAmount += Double.parseDouble(item.getSales_value().isEmpty() ? "0.0" : item.getSales_value());
            } else if(item.getVoucher_type().toString().equalsIgnoreCase("sales challan")){
                totalSaleQty += Double.parseDouble(item.getSales_qty().isEmpty() ? "0.0" : item.getSales_qty());
                totalSaleAmount += Double.parseDouble(item.getSales_value().isEmpty() ? "0.0" : item.getSales_value());
            } else if(item.getVoucher_type().toString().equalsIgnoreCase("sales return")){
                totalSaleQty -= Double.parseDouble(item.getSales_qty().isEmpty() ? "0.0" : item.getSales_qty());
                totalSaleAmount -= Double.parseDouble(item.getSales_value().isEmpty() ? "0.0" : item.getSales_value());
            }
        }

        // Update Labels in the FXML
        lblPurchaseTotalQty.setText(String.format("%.2f", totalPurchaseQty));
        lblPurchaseTotalAmount.setText(String.format("%.2f", totalPurchaseAmount));
        lblSaleTotalQty.setText(String.format("%.2f", totalSaleQty));
        lblSaleTotalAmount.setText(String.format("%.2f", totalSaleAmount));
    }

}
