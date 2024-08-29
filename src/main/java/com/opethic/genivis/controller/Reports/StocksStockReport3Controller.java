package com.opethic.genivis.controller.Reports;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import com.opethic.genivis.dto.AccountsDayBookDTO;
import com.opethic.genivis.dto.StockReport2DTO;
import com.opethic.genivis.dto.StockReport3DTO;
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
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;

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

import static com.opethic.genivis.utils.FxmFileConstants.STOCKS_STOCK_REPORT2_LIST_SLUG;
import static com.opethic.genivis.utils.FxmFileConstants.STOCKS_STOCK_REPORT3_LIST_SLUG;

public class StocksStockReport3Controller implements Initializable {
    private static final Logger StockReport3ListLogger = LoggerFactory.getLogger(StocksStockReport3Controller.class);
    @FXML
    private TableView<StockReport3DTO> tblvStockReport3List;
    @FXML
    private TableColumn<StockReport3DTO, String> tblStockReport3Particulars, tblStockReport3PurchaseQty, tblStockReport3PurchaseUnit, tblStockReport3PurchaseValue,
    tblStockReport3SalesQty, tblStockReport3SalesUnit, tblStockReport3SalesValue, tblStockReport3ClosingBalanceQty, tblStockReport3ClosingBalanceUnit, tblStockReport3ClosingBalanceValue;

    private ObservableList<StockReport3DTO> originalData;

    public static String product_name = "";
    public static String closing_qty = "";
    public static String closing_value = "";


    private Integer stockReportId = -1;
    public void setEditId(Integer InId) {
        stockReportId = InId;
//        System.out.println("stockReportId"+stockReportId);
        if (InId > -1) {
            setEditData();
        }
    }
    private void setEditData() {
        fetchStockReport3List(String.valueOf(stockReportId));
    }

    @FXML
    private Label lblPurchaseTotalAmount;
    @FXML
    private Label lblSaleTotalAmount;
    @FXML
    private Label lblCloseBalTotalAmount;
    @FXML
    private Label product_label, closing_qty_label, closing_value_label;

    @FXML
    private TextField tfStockReport3ListSearch;

    @FXML
    private BorderPane borderpane;

    @FXML
    private TextField tfStartDt;
    String startDate = "";
    @FXML
    private TextField tfEndDt;
    String endDate = "";

    String Start_date = "";
    String End_date = "";

    @FXML
    private Button btExportPdf, btExportExcel, btExportCsv, btExportPrint;

    private JsonObject jsonObject = null;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        tblvStockReport3List.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        StocksStockReport3TableDesign();

        StockReport3ListLogger.info("Start of Initialize method of : StocksStockReport3Controller");
        Platform.runLater(() -> tfStockReport3ListSearch.requestFocus());
        fetchStockReport3List("");
        product_label.setText("Stock Report Month wise - Product : " + product_name);
        closing_qty_label.setText("Qty : " + closing_qty);
        closing_value_label.setText("Value : " + closing_value);
//        System.out.println(product_name);
//        System.out.println("closing_qty------------------->  " + closing_qty);
//        System.out.println("closing_value------------------->  " + closing_value);

        borderpane.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ESCAPE) {
//                System.out.println("Esc Clicked....!");
                GlobalController.getInstance().addTabStatic1(STOCKS_STOCK_REPORT2_LIST_SLUG, false, stockReportId);
            }
        });


        tfEndDt.setOnKeyPressed(event ->{
            if(event.getCode() == KeyCode.ENTER){
                endDate = tfEndDt.getText().toString();
                System.out.println("DateFilter From Date " + tfStartDt.getText().toString());
                System.out.println("DateFilter To Date " + tfEndDt.getText().toString());
                fetchStockReport3List("");
            }
        });


        borderpane.sceneProperty().addListener((observable, oldScene, newScene) -> {
                if (newScene != null && newScene.getWindow() instanceof Stage) {
                    Communicator.stage = (Stage) newScene.getWindow();
                }
            });

        DateValidator.applyDateFormat(tfStartDt);
        DateValidator.applyDateFormat(tfEndDt);

//        originalData = tblvStockReport3List.getItems();
        tfStockReport3ListSearch.textProperty().addListener((observable, oldValue, newValue) -> {
            filterData(newValue.trim());
        });

        nodetraversal(tfStockReport3ListSearch, tfStartDt);
        nodetraversal(tfStartDt, tfEndDt);
        nodetraversal(tfEndDt, btExportPdf);
        nodetraversal(btExportPdf, btExportExcel);
        nodetraversal(btExportExcel, btExportCsv);
        nodetraversal(btExportCsv, btExportPrint);
        tblvStockReport3List.requestFocus();

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

                if("tfEndDt".equals(current_node.getId())){
                    getEndDate();
                }

                next_node.requestFocus();
                event.consume();
            }
            if (event.getCode() == KeyCode.TAB) {
                if("tfEndDt".equals(current_node.getId())){
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
//        System.out.println("tfEndDt-------> " + tfEndDt.getText());
        fetchStockReport3List("");
    }

    private void StocksStockReport3TableDesign() {
        //todo: Responsive code for tableview
        tblStockReport3Particulars.prefWidthProperty().bind(tblvStockReport3List.widthProperty().multiply(0.06));
        tblStockReport3PurchaseQty.prefWidthProperty().bind(tblvStockReport3List.widthProperty().multiply(0.08));
        tblStockReport3PurchaseUnit.prefWidthProperty().bind(tblvStockReport3List.widthProperty().multiply(0.10));
        tblStockReport3PurchaseValue.prefWidthProperty().bind(tblvStockReport3List.widthProperty().multiply(0.09));
        tblStockReport3SalesQty.prefWidthProperty().bind(tblvStockReport3List.widthProperty().multiply(0.08));
        tblStockReport3SalesUnit.prefWidthProperty().bind(tblvStockReport3List.widthProperty().multiply(0.10));
        tblStockReport3SalesValue.prefWidthProperty().bind(tblvStockReport3List.widthProperty().multiply(0.09));
        tblStockReport3ClosingBalanceQty.prefWidthProperty().bind(tblvStockReport3List.widthProperty().multiply(0.08));
        tblStockReport3ClosingBalanceUnit.prefWidthProperty().bind(tblvStockReport3List.widthProperty().multiply(0.10));
        tblStockReport3ClosingBalanceValue.prefWidthProperty().bind(tblvStockReport3List.widthProperty().multiply(0.09));
    }

    private void fetchStockReport3List(String searchKey) {
        StockReport3ListLogger.info("Fetch Stock Report 3 List  : StocksStockReport3Controller");

        APIClient apiClient = null;

        try {
//            System.out.println("tfStartDt.getText()).toString()" + tfStartDt.getText().toString());
//            System.out.println("tfEndDt.getText()).toString()" + tfEndDt.getText().toString());
            StockReport3ListLogger.debug("Get Stock Report 3 Data Started...");
            Map<String, String> body = new HashMap<>();
//            System.out.println("stockReportId  "+String.valueOf(stockReportId));
//            System.out.println("startDate---------> " + startDate);
//            System.out.println("endDate---------> " + endDate);
            if(!tfStartDt.getText().equalsIgnoreCase("")  || !tfEndDt.getText().equalsIgnoreCase("")){
//                body.put("start_date", startDate.toString());
//                body.put("end_date", endDate.toString());
                body.put("start_date", Communicator.text_to_date.fromString(tfStartDt.getText()).toString());
                body.put("end_date", Communicator.text_to_date.fromString(tfEndDt.getText()).toString());
            }
            body.put("productId", stockReportId.toString());

            String requestBody = Globals.mapToStringforFormData(body);
//            System.out.println("requestBody :" + requestBody);
//            HttpResponse<String> response = APIClient.postFormDataRequest(requestBody, EndPoints.STOCKS_STOCK_REPORT3_LIST);
            apiClient = new APIClient(EndPoints.STOCKS_STOCK_REPORT3_LIST, requestBody, RequestType.FORM_DATA);
            apiClient.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
                @Override
                public void handle(WorkerStateEvent workerStateEvent) {
//                    JsonObject responseBody = new Gson().fromJson(response.body(), JsonObject.class);
                    jsonObject = new Gson().fromJson(workerStateEvent.getSource().getValue().toString(), JsonObject.class);
//            System.out.println("fetchStockReport3List" + responseBody);
                    ObservableList<StockReport3DTO> observableList = FXCollections.observableArrayList();
                    if(jsonObject.get("responseStatus").getAsInt() == 200){
                        JsonArray responseList = jsonObject.getAsJsonArray("response");
                        Start_date = (jsonObject.get("d_start_date").getAsString());
                        End_date = jsonObject.get("d_end_date").getAsString();
                        LocalDate startLocalDt = LocalDate.parse(Start_date);
                        LocalDate endLocalDt = LocalDate.parse(End_date);


                        JsonObject item =  new JsonObject();
                        JsonObject monthData =  new JsonObject();
                        JsonArray purchase = new JsonArray();
                        JsonArray sale = new JsonArray();
                        JsonArray closing = new JsonArray();


//                System.out.println("fetchStockReport3List" + responseList);
                        if (responseList.size() > 0){
//                    System.out.println("List" + responseList);
                            tblvStockReport3List.setVisible(true);
                            for (JsonElement element : responseList){
                                item = element.getAsJsonObject();
                                monthData = item.getAsJsonObject("responseObject");
                                purchase = monthData.getAsJsonArray("purchase");
                                sale = monthData.getAsJsonArray("sale");
                                closing = monthData.getAsJsonArray("closing");

                                String purchase_qty = "";
                                String purchase_unit = "";
                                String purchase_value = "";
                                String sales_qty = "";
                                String sales_unit = "";
                                String sales_value = "";
                                String closing_balance_qty = "";
                                String closing_balance_unit = "";
                                String closing_balance_value = "";

                                String particulars = item.get("month_name") != null ? item.get("month_name").getAsString() : "";
                                if(purchase.size()>0) {
                                    for (JsonElement element1 : purchase) {
                                        JsonObject details = element1.getAsJsonObject();
                                        double Qty = details.get("qty").getAsDouble();
                                        if (Qty != 0.0) {
//                                    System.out.println("element1" + Qty);
                                            purchase_qty = details.get("qty").getAsString();
                                            purchase_unit =  details.get("unit").getAsString();
                                            purchase_value =  details.get("value").getAsString();
                                        }

                                    }
                                }

                                if(sale.size()>0) {
                                    for (JsonElement element1 : sale) {
                                        JsonObject details = element1.getAsJsonObject();
                                        double Qty = details.get("qty").getAsDouble();
                                        if (Qty != 0.0) {
//                                    System.out.println("element1" + Qty);
                                            sales_qty = details.get("qty").getAsString();
                                            sales_unit =  details.get("unit").getAsString();
                                            sales_value =  details.get("value").getAsString();
                                        }

                                    }
                                }

                                if(closing.size()>0) {
                                    for (JsonElement element1 : closing) {
                                        JsonObject details = element1.getAsJsonObject();
                                        double Qty = details.get("qty").getAsDouble();
                                        if (Qty != 0.0) {
//                                    System.out.println("element1" + Qty);
                                            closing_balance_qty = details.get("qty").getAsString();
                                            closing_balance_unit =  details.get("unit").getAsString();
                                            closing_balance_value =  details.get("value").getAsString();
                                        }

                                    }
                                }

                                observableList.add(new StockReport3DTO(particulars, purchase_qty,purchase_unit ,purchase_value ,sales_qty ,sales_unit,
                                        sales_value ,closing_balance_qty ,closing_balance_unit ,closing_balance_value ));
//                        System.out.println("observableList: " + item);
                            }

//                    tfStartDt.setText(String.valueOf(LocalDate.parse(String.format(Start_date))));
//                    tfEndDt.setText(String.valueOf(LocalDate.parse(String.format(End_date))));

                            tfStartDt.setText(startLocalDt.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
                            tfEndDt.setText(endLocalDt.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));



                            tblStockReport3Particulars.setCellValueFactory(new PropertyValueFactory<>("particulars"));
                            tblStockReport3PurchaseQty.setCellValueFactory(new PropertyValueFactory<>("purchase_qty"));
                            tblStockReport3PurchaseUnit.setCellValueFactory(new PropertyValueFactory<>("purchase_unit"));
                            tblStockReport3PurchaseValue.setCellValueFactory(new PropertyValueFactory<>("purchase_value"));
                            tblStockReport3SalesQty.setCellValueFactory(new PropertyValueFactory<>("sales_qty"));
                            tblStockReport3SalesUnit.setCellValueFactory(new PropertyValueFactory<>("sales_unit"));
                            tblStockReport3SalesValue.setCellValueFactory(new PropertyValueFactory<>("sales_value"));
                            tblStockReport3ClosingBalanceQty.setCellValueFactory(new PropertyValueFactory<>("closing_balance_qty"));
                            tblStockReport3ClosingBalanceUnit.setCellValueFactory(new PropertyValueFactory<>("closing_balance_unit"));
                            tblStockReport3ClosingBalanceValue.setCellValueFactory(new PropertyValueFactory<>("closing_balance_value"));


                            tblvStockReport3List.setItems(observableList);
                            originalData = observableList;
                            calculateTotalAmount();
                            StockReport3ListLogger.debug("Success in Displaying Stock Report 3 List : StocksStockReport3Controller");
                        }else {
                            StockReport3ListLogger.debug("Stock Report 3 List ResponseObject is null : StocksStockReport3Controller");
                            calculateTotalAmount();
                        }
                    }else {
                        StockReport3ListLogger.debug("Error in response of Stock Report 3 List : StocksStockReport3Controller");
                        calculateTotalAmount();
                    }
                }
            });
            apiClient.setOnCancelled(new EventHandler<WorkerStateEvent>() {
                @Override
                public void handle(WorkerStateEvent workerStateEvent) {
                    StockReport3ListLogger.error("Network API cancelled in fetchStockReport3List()" + workerStateEvent.getSource().getValue().toString());

                }
            });

            apiClient.setOnFailed(new EventHandler<WorkerStateEvent>() {
                @Override
                public void handle(WorkerStateEvent workerStateEvent) {
                    StockReport3ListLogger.error("Network API failed in fetchStockReport3List()" + workerStateEvent.getSource().getValue().toString());
                }
            });
            apiClient.start();
            StockReport3ListLogger.debug("Get Stock Report 3 Data End...");

        } catch (Exception e) {
            StockReport3ListLogger.error("Stock Report 3 List Error is " + e.getMessage());
            e.printStackTrace();
        }
        finally {
            apiClient = null;
        }
    }

    private void filterData(String keyword) {
        ObservableList<StockReport3DTO> filteredData = FXCollections.observableArrayList();
        StockReport3ListLogger.info("Search Stock Report 3 List in StocksStockReport3Controller");
        if (keyword.isEmpty()){
            tblvStockReport3List.setItems(originalData);
            calculateTotalAmount();
            return;
        }

        for (StockReport3DTO item : originalData){
            if (matchesKeyword(item, keyword)){
                filteredData.add(item);
            }
        }

        tblvStockReport3List.setItems(filteredData);
        calculateTotalAmount();
    }

    private boolean matchesKeyword(StockReport3DTO item, String keyword) {
        String lowerCaseKeyword = keyword.toLowerCase();
        return  item.getParticulars().toLowerCase().contains(lowerCaseKeyword);
    }

    private void calculateTotalAmount() {
        ObservableList<StockReport3DTO> filteredData = tblvStockReport3List.getItems();
        // Calculate the Totals
        double totalPurchaseAmount = 0.0;
        double totalSaleAmount = 0.0;
        for (StockReport3DTO item : filteredData) {
                totalPurchaseAmount += Double.parseDouble(item.getPurchase_value().isEmpty() ? "0.0" : item.getPurchase_value());
                totalSaleAmount += Double.parseDouble(item.getSales_value().isEmpty() ? "0.0" : item.getSales_value());
        }

        // Update Labels in the FXML
        lblPurchaseTotalAmount.setText(String.format("%.2f", totalPurchaseAmount));
        lblSaleTotalAmount.setText(String.format("%.2f", totalSaleAmount));
    }
}
