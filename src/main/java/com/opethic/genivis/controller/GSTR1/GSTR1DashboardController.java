package com.opethic.genivis.controller.GSTR1;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.opethic.genivis.controller.tranx_sales.SalesQuotationCreateController;
import com.opethic.genivis.dto.GSTR1.GSTR1DashboaedDTO;
import com.opethic.genivis.dto.GSTR3BDashboaedDTO;
import com.opethic.genivis.dto.SalesQuotationDTO;
import com.opethic.genivis.network.APIClient;
import com.opethic.genivis.network.EndPoints;
import com.opethic.genivis.network.RequestType;
import com.opethic.genivis.utils.AlertUtility;
import com.opethic.genivis.utils.DateConvertUtil;
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
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import org.json.JSONArray;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import static com.opethic.genivis.utils.FxmFileConstants.*;

public class GSTR1DashboardController implements Initializable {
    public static final Logger GSTR1B2BLogger = LoggerFactory.getLogger(B2BScreen1Controller.class);
    @FXML
    private BorderPane bpGSTR1DashboardRootPane;
    @FXML
    private TableView<GSTR1DashboaedDTO> tblvGSTR1DashboardList;
    @FXML
    private TextField tfStartDt, tfEndDt;

    @FXML
    private Button GSTR1DashboardBtnPdf, GSTR1DashboardBtnCsv, GSTR1DashboardBtnExcel,GSTR1DashboardBtnPrint;
    @FXML
    private TableColumn<GSTR1DashboaedDTO, String> tblGSTR1DashboardTable_no, tblGSTR1DashboardParticulars,tblGSTR1DashboardVoucherCount, tblGSTR1DashboardTaxableAmt,
            tblGSTR1DashboardIGSTAmt, tblGSTR1DashboardCGSTAmt, tblGSTR1DashboardSGSTAmt, tblGSTR1DashboardCessAmt, tblGSTR1DashboardTaxAmt, tblGSTR1DashboardTotalAmt;

    public void initialize(URL url, ResourceBundle resourceBundle) {
        tblGSTR1DashboardParticulars.setCellValueFactory(new PropertyValueFactory<>("particulars"));
        Platform.runLater(() -> {
            tfStartDt.requestFocus();
           });


        tblGSTR1DashboardParticulars.setCellFactory(column -> new TableCell<GSTR1DashboaedDTO, String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);

                if (item == null || empty) {
                    setText(null);
                    setStyle("");
                } else {
                    setText(item);
                    if (item.contains("B2B Invoice - 4A,4B,4C,6B,6C")) {
                        setStyle("-fx-font-weight: normal; -fx-text-fill: #257dff; -fx-underline: true;");
                    } else if (item.contains("B2C ( Large ) Invoice - 5A- 6B")){
                        setStyle("-fx-font-weight: normal; -fx-text-fill: #257dff; -fx-underline: true;");
                    } else if (item.contains("B2C ( Small ) Invoice - 7")){
                        setStyle("-fx-font-weight: normal; -fx-text-fill: #257dff; -fx-underline: true;");
                    } else if (item.contains("Credit / Debit notes ( Registered ) 9B")){
                        setStyle("-fx-font-weight: normal; -fx-text-fill: #257dff; -fx-underline: true;");
                    } else if (item.contains("Credit / Debit notes (Unregistered ) 9B")){
                        setStyle("-fx-font-weight: normal; -fx-text-fill: #257dff; -fx-underline: true;");
                    } else if (item.contains("Nil Rated / Exempted Invoice - 8a,8B,8C,8D")){
                        setStyle("-fx-font-weight: normal; -fx-text-fill: #257dff; -fx-underline: true;");
                    } else {
                        setStyle("-fx-font-weight: normal; -fx-text-fill: black;");
                    }
                }
            }
        });

        tblvGSTR1DashboardList.setRowFactory(tv -> {
            TableRow<GSTR1DashboaedDTO> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (!row.isEmpty() && event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2) {
                    GSTR1DashboaedDTO clickedRow = row.getItem();
                    if (clickedRow.getParticulars().contains("B2B Invoice - 4A,4B,4C,6B,6C")) {
                        navigateToNextPage3_1A();
                    } else if (clickedRow.getParticulars().contains("B2C ( Large ) Invoice - 5A- 6B")) {
                        navigateToNextPage3_1C();
                    }else if (clickedRow.getParticulars().contains("Credit / Debit notes ( Registered ) 9B")) {
                        GlobalController.getInstance().addTabStatic(GSTR1_CREDIT_DEBIT_REGISTERED_9B_LIST_SLUG, false);
                    }else if (clickedRow.getParticulars().contains("Credit / Debit notes (Unregistered ) 9B")) {
                        GlobalController.getInstance().addTabStatic(GSTR1_CREDIT_DEBIT_UNREGISTERED_9B_LIST_SLUG, false);
                    }else if (clickedRow.getParticulars().contains("Nil Rated / Exempted Invoice - 8a,8B,8C,8D")) {
                        navigateToNextPage9_1C();
                    }
                    else if (clickedRow.getParticulars().contains("B2C ( Small ) Invoice - 7")) {
                        navigateToB2CSmall1();
                    }
                }
            });
            return row;
        });

        fetchGSTR1DashboardList("");

        GSTR1DashboardBtnPdf.focusedProperty().addListener((obs, wasFocused, isNowFocused) -> {
            if (isNowFocused) {
                GSTR1DashboardBtnPdf.setStyle("-fx-border-width: 2; -fx-border-color: #03dbfc");
            } else {
                GSTR1DashboardBtnPdf.setStyle("");
            }
        });

        GSTR1DashboardBtnPdf.hoverProperty().addListener((obs, wasHovered, isNowHovered) -> {
            if (isNowHovered) {
                GSTR1DashboardBtnPdf.setStyle("-fx-border-width: 2; -fx-border-color: #03dbfc");
            } else {
                GSTR1DashboardBtnPdf.setStyle("");
            }
        });

        GSTR1DashboardBtnExcel.focusedProperty().addListener((obs, wasFocused, isNowFocused) -> {
            if (isNowFocused) {
                GSTR1DashboardBtnExcel.setStyle("-fx-border-width: 2; -fx-border-color: #03dbfc");
            } else {
                GSTR1DashboardBtnExcel.setStyle("");
            }
        });

        GSTR1DashboardBtnExcel.hoverProperty().addListener((obs, wasHovered, isNowHovered) -> {
            if (isNowHovered) {
                GSTR1DashboardBtnExcel.setStyle("-fx-border-width: 2; -fx-border-color: #03dbfc");
            } else {
                GSTR1DashboardBtnExcel.setStyle("");
            }
        });

        GSTR1DashboardBtnCsv.focusedProperty().addListener((obs, wasFocused, isNowFocused) -> {
            if (isNowFocused) {
                GSTR1DashboardBtnCsv.setStyle("-fx-border-width: 2; -fx-border-color: #03dbfc");
            } else {
                GSTR1DashboardBtnCsv.setStyle("");
            }
        });

        GSTR1DashboardBtnCsv.hoverProperty().addListener((obs, wasHovered, isNowHovered) -> {
            if (isNowHovered) {
                GSTR1DashboardBtnCsv.setStyle("-fx-border-width: 2; -fx-border-color: #03dbfc");
            } else {
                GSTR1DashboardBtnCsv.setStyle("");
            }
        });

        GSTR1DashboardBtnPrint.focusedProperty().addListener((obs, wasFocused, isNowFocused) -> {
            if (isNowFocused) {
                GSTR1DashboardBtnPrint.setStyle("-fx-border-width: 2; -fx-border-color: #03dbfc");
            } else {
                GSTR1DashboardBtnPrint.setStyle("");
            }
        });

        GSTR1DashboardBtnPrint.hoverProperty().addListener((obs, wasHovered, isNowHovered) -> {
            if (isNowHovered) {
                GSTR1DashboardBtnPrint.setStyle("-fx-border-width: 2; -fx-border-color: #03dbfc");
            } else {
                GSTR1DashboardBtnPrint.setStyle("");
            }
        });

        nodetraversal(tfStartDt, tfEndDt);
        nodetraversal(tfEndDt, GSTR1DashboardBtnPdf);
        nodetraversal(GSTR1DashboardBtnPdf, GSTR1DashboardBtnExcel);
        nodetraversal(GSTR1DashboardBtnExcel, GSTR1DashboardBtnCsv);
        nodetraversal(GSTR1DashboardBtnCsv, GSTR1DashboardBtnPrint);
//        tblvGSTR1DashboardList.requestFocus() ;
    }


    private void navigateToNextPage3_1A() {
        System.out.println("Next Page Calling  GSTR1 B2B1 ---> ");
        //todo :GSTR1B2B screen Navigation
       GlobalController.getInstance().addTabStatic(GSTR1_B2B_SCREEN1_LIST_SLUG,false);
    }

    private void navigateToNextPage3_1C() {
        System.out.println("Next Page Calling GSTR1 B2CLarge ---> ");
        GlobalController.getInstance().addTabStatic(GSTR1_B2CLARGE_SCREEN1_LIST_SLUG,false);
    }
    private void navigateToB2CSmall1() {
        System.out.println("Next Page Calling GSTR1 B2CLarge ---> ");
        GlobalController.getInstance().addTabStatic(GSTR1_B2CSMALL_SCREEN1_LIST_SLUG,false);
    }

    private void nodetraversal(Node current_node, Node next_node) {
        current_node.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                next_node.requestFocus();
                event.consume();
            }

            if (event.getCode() == KeyCode.SPACE) {
                if (current_node instanceof Button button) {
                    button.fire();
                }
            }
        });
    }
    private void navigateToNextPage9_1C() {
            System.out.println("Next Page Calling GSTR1 Nil Rated ---> ");
            GlobalController.getInstance().addTabStatic(GSTR1_NIL_RATED_LIST_SLUG,false);
        }

    private void fetchGSTR1DashboardList(String s) {
        tblGSTR1DashboardTable_no.setCellValueFactory(new PropertyValueFactory<>("table_no"));
        tblGSTR1DashboardParticulars.setCellValueFactory(new PropertyValueFactory<>("particulars"));
        tblGSTR1DashboardVoucherCount.setCellValueFactory(new PropertyValueFactory<>("voucher_count"));
        tblGSTR1DashboardTaxableAmt.setCellValueFactory(new PropertyValueFactory<>("taxable_amt"));
        tblGSTR1DashboardIGSTAmt.setCellValueFactory(new PropertyValueFactory<>("igst_amt"));
        tblGSTR1DashboardCGSTAmt.setCellValueFactory(new PropertyValueFactory<>("cgst_amt"));
        tblGSTR1DashboardSGSTAmt.setCellValueFactory(new PropertyValueFactory<>("sgst_amt"));
        tblGSTR1DashboardCessAmt.setCellValueFactory(new PropertyValueFactory<>("cess_amt"));
        tblGSTR1DashboardTaxAmt.setCellValueFactory(new PropertyValueFactory<>("tax_amt"));
        tblGSTR1DashboardTotalAmt.setCellValueFactory(new PropertyValueFactory<>("total_amt"));
//        getGSTR1B2Bdata();
        // Add data to the table
        tblvGSTR1DashboardList.setItems(getGSTR1DashboardData());
    }

    private ObservableList<GSTR1DashboaedDTO> getGSTR1DashboardData() {
        ObservableList<GSTR1DashboaedDTO> data = FXCollections.observableArrayList();
        data.add(new GSTR1DashboaedDTO("1", "B2B Invoice - 4A,4B,4C,6B,6C", "0.00", "", "0.00", "0.00", "", "0.00", "", ""));
        data.add(new GSTR1DashboaedDTO("", "Taxable Sales", "0.00", "", "0.00", "0.00", "", "0.00", "", ""));
        data.add(new GSTR1DashboaedDTO("", "Reverse Charge Supplies", "", "", "", "", "", "", "", ""));
        data.add(new GSTR1DashboaedDTO("2", "B2C ( Large ) Invoice - 5A- 6B", "0.00", "", "", "", "", "", "", ""));
        data.add(new GSTR1DashboaedDTO("3", "B2C ( Small ) Invoice - 7", "", "", "", "", "", "", "", ""));
        data.add(new GSTR1DashboaedDTO("4", "Credit / Debit notes ( Registered ) 9B", "", "", "", "", "", "", "", ""));
        data.add(new GSTR1DashboaedDTO("5", "Credit / Debit notes (Unregistered ) 9B", "", "", "", "", "", "", "", ""));
        data.add(new GSTR1DashboaedDTO("6", "Export Invoices - 6A", "", "", "", "", "", "", "", ""));
        data.add(new GSTR1DashboaedDTO("7", "Tax Liability ( Advance received ) -11A(1),11A(2)", "", "", "", "", "", "", "", ""));
        data.add(new GSTR1DashboaedDTO("8", "Adjustment of advances -11B(1) , 11B(2)", "", "", "", "", "", "", "", ""));
        data.add(new GSTR1DashboaedDTO("9", "Nil Rated / Exempted Invoice - 8a,8B,8C,8D", "", "", "", "", "", "", "", ""));
        data.add(new GSTR1DashboaedDTO("", "", "", "", "", "", "", "", "", ""));
        data.add(new GSTR1DashboaedDTO("", "", "", "", "", "", "", "", "", ""));
        data.add(new GSTR1DashboaedDTO("", "", "", "", "", "", "", "", "", ""));
        data.add(new GSTR1DashboaedDTO("", "", "", "", "", "", "", "", "", ""));
        data.add(new GSTR1DashboaedDTO("", "", "", "", "", "", "", "", "", ""));
        data.add(new GSTR1DashboaedDTO("Total", "", "", "", "", "", "", "", "", ""));
        data.add(new GSTR1DashboaedDTO("", "HSN /SAC Summary", "0.00", "0.00", "0.00", "0.00", "0.00", "0.00", "", ""));
        data.add(new GSTR1DashboaedDTO("", "Document Summary", "", "", "", "", "", "", "", ""));
        return data;
    }

    public void pdf(){
        System.out.println("pdf clicked");
    }
    public void excel(){
        System.out.println("excel clicked");
    }
    public void csv(){
        System.out.println("csv clicked");
    }
    public void print(){
        System.out.println("print clicked");
    }

            //? Api to call gstr1 data Dynamic
    private void getGSTR1B2Bdata() {
        APIClient apiClient = null;
//        tpTranxBottom.getSelectionModel().select(tbTranxBatchBottom);
//        loggerTranxSalesInvoiceCreate.debug("Get Supplier List Data Started...");
        Map<String, String> map = new HashMap<>();
        map.put("start_date", "");
        map.put("end_date", "");
        Map<String, Object> sortObject = new HashMap<>();
        sortObject.put("colId", null);
        sortObject.put("isAsc", true);
        map.put("sort", sortObject.toString());
        String formData = Globals.mapToStringforFormData(map);
        System.out.println("FormData: " + formData);

        ObservableList<GSTR1DashboaedDTO> data = FXCollections.observableArrayList();

        //todo: getGSTR1B2Bdata
        apiClient = new APIClient(EndPoints.GET_GSTR1_B2B_DATA, formData, RequestType.FORM_DATA);

        apiClient.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent workerStateEvent) {
                JsonObject jsonObject = new Gson().fromJson(workerStateEvent.getSource().getValue().toString(), JsonObject.class);
//                System.out.println("jsonObject =>" + jsonObject);
                if (jsonObject.get("responseStatus").getAsInt() == 200) {
                    JsonObject jsonObject1 = jsonObject.getAsJsonObject();
                    String taxable_amt = jsonObject1.get("taxable_amt") != null ? jsonObject1.get("taxable_amt").toString():"";
                    String voucher_count = jsonObject1.get("voucher_count") !=null ? jsonObject1.get("voucher_count").toString():"";
                    String igst_amt = jsonObject1.get("total_igst") != null ? jsonObject1.get("total_igst").toString(): "";
                    String cgst_amt = jsonObject1.get("total_cgst") != null ? jsonObject1.get("total_cgst").toString(): "";
                    String sgst_amt = jsonObject1.get("total_sgst") != null ? jsonObject1.get("total_sgst").toString(): "";
                    String tax_amt = jsonObject1.get("total_tax") != null ? jsonObject1.get("total_tax").toString() :"";
                    String invoice_amt = jsonObject1.get("total_amt") != null ? jsonObject1.get("total_amt").toString():"";

                    data.add(new GSTR1DashboaedDTO("1", "B2B Invoice - 4A,4B,4C,6B,6C",voucher_count, taxable_amt, igst_amt, cgst_amt, sgst_amt, "", tax_amt, invoice_amt));
                    data.add(new GSTR1DashboaedDTO("", "Taxable Sales", "","", "", "", "", "", "", ""));
                    data.add(new GSTR1DashboaedDTO("", "Reverse Charge Supplies", "", "", "", "", "", "", "", ""));

                        tblvGSTR1DashboardList.setItems(data);
                    System.out.println("Success in Displaying data1");
                    GSTR1B2BLogger.info("Success in Displaying GSTR1B2C Large SCREEN1");
                }
            }
        });
        apiClient.setOnCancelled(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent workerStateEvent) {
//                loggerTranxSalesInvoiceCreate.error("Network API cancelled in getSupplierListbyProductId()" + workerStateEvent.getSource().getValue().toString());

            }
        });

        apiClient.setOnFailed(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent workerStateEvent) {
//                loggerTranxSalesInvoiceCreate.error("Network API failed in getSupplierListbyProductId()" + workerStateEvent.getSource().getValue().toString());
            }
        });
        apiClient.start();
//        loggerTranxSalesInvoiceCreate.debug("Get Supplier List Data End...");


        //todo: getGSTR1B2CLargedata
        apiClient = new APIClient(EndPoints.GET_GSTR1_B2C_LARGE_DATA, formData, RequestType.FORM_DATA);

        apiClient.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent workerStateEvent) {
                JsonObject jsonObject = new Gson().fromJson(workerStateEvent.getSource().getValue().toString(), JsonObject.class);
//                System.out.println("jsonObject =>" + jsonObject);
                if (jsonObject.get("responseStatus").getAsInt() == 200) {
                    JsonArray JsonArray = jsonObject.get("data").getAsJsonArray();
                    JsonObject JsonObject1 =JsonArray.get(0).getAsJsonObject();
                    String taxable_amt = JsonObject1.get("taxable_amt") != null ? JsonObject1.get("taxable_amt").getAsString():"";
                    String igst_amt = JsonObject1.get("total_igst") != null ? JsonObject1.get("total_igst").getAsString(): "";
                    String cgst_amt = JsonObject1.get("total_cgst") != null ? JsonObject1.get("total_cgst").getAsString(): "";
                    String sgst_amt = JsonObject1.get("total_sgst") != null ? JsonObject1.get("total_sgst").getAsString(): "";
                    String tax_amt = JsonObject1.get("total_tax") != null ? JsonObject1.get("total_tax").getAsString() :"";
                    String invoice_amt = JsonObject1.get("total_amt") != null ? JsonObject1.get("total_amt").getAsString():"";
                    String voucher_count = JsonObject1.get("voucher_count") != null ? JsonObject1.get("voucher_count").getAsString() :"";

                    data.add(new GSTR1DashboaedDTO("2", "B2C ( Large ) Invoice - 5A- 6B", voucher_count, taxable_amt, igst_amt, cgst_amt, sgst_amt, "", tax_amt,invoice_amt));

                    tblvGSTR1DashboardList.setItems(data);
                    System.out.println("Success in Displaying data2");
                    GSTR1B2BLogger.info("Success in Displaying GSTR1B2C Large SCREEN1");
                }
            }
        });
        apiClient.setOnCancelled(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent workerStateEvent) {
//                loggerTranxSalesInvoiceCreate.error("Network API cancelled in getSupplierListbyProductId()" + workerStateEvent.getSource().getValue().toString());

            }
        });

        apiClient.setOnFailed(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent workerStateEvent) {
//                loggerTranxSalesInvoiceCreate.error("Network API failed in getSupplierListbyProductId()" + workerStateEvent.getSource().getValue().toString());
            }
        });
        apiClient.start();


        //todo: getGSTR1B2CSmalldata
        apiClient = new APIClient(EndPoints.GET_GSTR1_B2C_SMALL_DATA, formData, RequestType.FORM_DATA);

        apiClient.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent workerStateEvent) {
                JsonObject jsonObject = new Gson().fromJson(workerStateEvent.getSource().getValue().toString(), JsonObject.class);
//                System.out.println("jsonObject =>" + jsonObject);
                if (jsonObject.get("responseStatus").getAsInt() == 200) {
                    JsonArray JsonArray = jsonObject.get("data").getAsJsonArray();
                    JsonObject JsonObject1 = JsonArray.get(0).getAsJsonObject();
                    String taxable_amt = JsonObject1.get("taxable_amt") != null ? JsonObject1.get("taxable_amt").getAsString():"";
                    String igst_amt = JsonObject1.get("total_igst") != null ? JsonObject1.get("total_igst").getAsString(): "";
                    String cgst_amt = JsonObject1.get("total_cgst") != null ? JsonObject1.get("total_cgst").getAsString(): "";
                    String sgst_amt = JsonObject1.get("total_sgst") != null ? JsonObject1.get("total_sgst").getAsString(): "";
                    String tax_amt = JsonObject1.get("total_tax") != null ? JsonObject1.get("total_tax").getAsString() :"";
                    String invoice_amt = JsonObject1.get("total_amt") != null ? JsonObject1.get("total_amt").getAsString():"";
                    String voucher_count = JsonObject1.get("voucher_count") != null ? JsonObject1.get("voucher_count").getAsString():"";

                    data.add(new GSTR1DashboaedDTO("3", "B2C ( Small ) Invoice - 7", voucher_count, taxable_amt, igst_amt, cgst_amt, sgst_amt, "", tax_amt,invoice_amt));

                    tblvGSTR1DashboardList.setItems(data);
                    System.out.println("Success in Displaying data3");
                    GSTR1B2BLogger.info("Success in Displaying GSTR1B2C Large SCREEN1");
                }
            }
        });
        apiClient.setOnCancelled(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent workerStateEvent) {
//                loggerTranxSalesInvoiceCreate.error("Network API cancelled in getSupplierListbyProductId()" + workerStateEvent.getSource().getValue().toString());

            }
        });

        apiClient.setOnFailed(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent workerStateEvent) {
//                loggerTranxSalesInvoiceCreate.error("Network API failed in getSupplierListbyProductId()" + workerStateEvent.getSource().getValue().toString());
            }
        });
        apiClient.start();

        //todo:getGSTR1CreditNoteRegistered

        apiClient = new APIClient(EndPoints.GET_GSTR1_CRNOTE_REG_DATA, formData, RequestType.FORM_DATA);

        apiClient.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent workerStateEvent) {
                JsonObject jsonObject = new Gson().fromJson(workerStateEvent.getSource().getValue().toString(), JsonObject.class);
//                System.out.println("jsonObject =>" + jsonObject);
                if (jsonObject.get("responseStatus").getAsInt() == 200) {
                    JsonObject JsonObject1 = jsonObject.getAsJsonObject();
                    String taxable_amt = JsonObject1.get("taxable_amt") != null ? JsonObject1.get("taxable_amt").getAsString():"";
                    String igst_amt = JsonObject1.get("total_igst") != null ? JsonObject1.get("total_igst").getAsString(): "";
                    String cgst_amt = JsonObject1.get("total_cgst") != null ? JsonObject1.get("total_cgst").getAsString(): "";
                    String sgst_amt = JsonObject1.get("total_sgst") != null ? JsonObject1.get("total_sgst").getAsString(): "";
                    String tax_amt = JsonObject1.get("total_tax") != null ? JsonObject1.get("total_tax").getAsString() :"";
                    String invoice_amt = JsonObject1.get("total_amt") != null ? JsonObject1.get("total_amt").getAsString():"";
                    String voucher_count = JsonObject1.get("voucher_count") != null ? JsonObject1.get("voucher_count").getAsString():"";

                    data.add(new GSTR1DashboaedDTO("4", "Credit / Debit notes ( Registered ) 9B", voucher_count, taxable_amt, igst_amt, cgst_amt, sgst_amt, "", tax_amt,invoice_amt));

                    tblvGSTR1DashboardList.setItems(data);
                    System.out.println("Success in Displaying data4");
                    GSTR1B2BLogger.info("Success in Displaying GSTR1B2C Large SCREEN1");
                }
            }
        });
        apiClient.setOnCancelled(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent workerStateEvent) {
//                loggerTranxSalesInvoiceCreate.error("Network API cancelled in getSupplierListbyProductId()" + workerStateEvent.getSource().getValue().toString());

            }
        });

        apiClient.setOnFailed(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent workerStateEvent) {
//                loggerTranxSalesInvoiceCreate.error("Network API failed in getSupplierListbyProductId()" + workerStateEvent.getSource().getValue().toString());
            }
        });
        apiClient.start();

        //todo:getGSTR1CreditNoteUnRegistered

        apiClient = new APIClient(EndPoints.GET_GSTR1_CRNOTE_UNREG_DATA, formData, RequestType.FORM_DATA);

        apiClient.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent workerStateEvent) {
                JsonObject jsonObject = new Gson().fromJson(workerStateEvent.getSource().getValue().toString(), JsonObject.class);
//                System.out.println("jsonObject =>" + jsonObject);
                if (jsonObject.get("responseStatus").getAsInt() == 200) {
                    JsonObject JsonObject1 = jsonObject.getAsJsonObject();
                    String taxable_amt = JsonObject1.get("taxable_amt") != null ? JsonObject1.get("taxable_amt").getAsString():"";
                    String igst_amt = JsonObject1.get("total_igst") != null ? JsonObject1.get("total_igst").getAsString(): "";
                    String cgst_amt = JsonObject1.get("total_cgst") != null ? JsonObject1.get("total_cgst").getAsString(): "";
                    String sgst_amt = JsonObject1.get("total_sgst") != null ? JsonObject1.get("total_sgst").getAsString(): "";
                    String tax_amt = JsonObject1.get("total_tax") != null ? JsonObject1.get("total_tax").getAsString() :"";
                    String invoice_amt = JsonObject1.get("total_amt") != null ? JsonObject1.get("total_amt").getAsString():"";
                    String voucher_count = JsonObject1.get("voucher_count") != null ? JsonObject1.get("voucher_count").getAsString():"";

                    data.add(new GSTR1DashboaedDTO("5", "Credit / Debit notes (Unregistered ) 9B", voucher_count, taxable_amt, igst_amt, cgst_amt, sgst_amt, "", tax_amt,invoice_amt));

                    tblvGSTR1DashboardList.setItems(data);
                    System.out.println("Success in Displaying data5");
                    GSTR1B2BLogger.info("Success in Displaying GSTR1B2C Large SCREEN1");
                }
            }
        });
        apiClient.setOnCancelled(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent workerStateEvent) {
//                loggerTranxSalesInvoiceCreate.error("Network API cancelled in getSupplierListbyProductId()" + workerStateEvent.getSource().getValue().toString());

            }
        });

        apiClient.setOnFailed(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent workerStateEvent) {
//                loggerTranxSalesInvoiceCreate.error("Network API failed in getSupplierListbyProductId()" + workerStateEvent.getSource().getValue().toString());
            }
        });
        apiClient.start();

        //todo:getGSTR1HSN

        apiClient = new APIClient(EndPoints.GET_GSTR1_HSN_DATA, formData, RequestType.FORM_DATA);

        apiClient.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent workerStateEvent) {
                JsonObject jsonObject = new Gson().fromJson(workerStateEvent.getSource().getValue().toString(), JsonObject.class);
//                System.out.println("jsonObject =>" + jsonObject);
                if (jsonObject.get("responseStatus").getAsInt() == 200) {
                    JsonObject JsonObject1 = jsonObject.getAsJsonObject();
                    String taxable_amt = JsonObject1.get("taxable_amt") != null ? JsonObject1.get("taxable_amt").getAsString():"";
                    String igst_amt = JsonObject1.get("total_igst") != null ? JsonObject1.get("total_igst").getAsString(): "";
                    String cgst_amt = JsonObject1.get("total_cgst") != null ? JsonObject1.get("total_cgst").getAsString(): "";
                    String sgst_amt = JsonObject1.get("total_sgst") != null ? JsonObject1.get("total_sgst").getAsString(): "";
                    String tax_amt = JsonObject1.get("total_tax") != null ? JsonObject1.get("total_tax").getAsString() :"";
                    String invoice_amt = JsonObject1.get("total_amt") != null ? JsonObject1.get("total_amt").getAsString():"";
                    String voucher_count = JsonObject1.get("voucher_count") != null ? JsonObject1.get("voucher_count").getAsString():"";


                    data.add(new GSTR1DashboaedDTO("6", "Export Invoices - 6A", "", "", "", "", "", "", "",""));
                    data.add(new GSTR1DashboaedDTO("7", "Tax Liability ( Advance received ) -11A(1),11A(2)", "", "", "", "", "", "", "",""));
                    data.add(new GSTR1DashboaedDTO("8", "Adjustment of advances -11B(1) , 11B(2)","", "", "", "", "", "", "", ""));
                    data.add(new GSTR1DashboaedDTO("9", "Nil Rated / Exempted Invoice - 8a,8B,8C,8D", "", "", "", "", "", "", "",""));
                    data.add(new GSTR1DashboaedDTO("", "", "", "", "", "", "", "", "", ""));
                    data.add(new GSTR1DashboaedDTO("", "", "", "", "", "", "", "", "",""));
                    data.add(new GSTR1DashboaedDTO("", "", "", "", "", "", "", "", "",""));
                    data.add(new GSTR1DashboaedDTO("", "", "", "", "", "", "", "", "",""));
                    data.add(new GSTR1DashboaedDTO("", "", "", "", "", "", "", "", "",""));
                    data.add(new GSTR1DashboaedDTO("Total", "", "", "", "", "", "", "", "",  ""));
                    data.add(new GSTR1DashboaedDTO("", "HSN /SAC Summary", voucher_count, taxable_amt, igst_amt, cgst_amt, sgst_amt, "", tax_amt, invoice_amt));
                    data.add(new GSTR1DashboaedDTO("", "Document Summary", "", "", "", "", "", "", "", ""));
//                    return data;
                    tblvGSTR1DashboardList.setItems(data);
                    System.out.println("Success in Displaying data6");
                    GSTR1B2BLogger.info("Success in Displaying GSTR1B2C Large SCREEN1");
                }
            }
        });
        apiClient.setOnCancelled(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent workerStateEvent) {
//                loggerTranxSalesInvoiceCreate.error("Network API cancelled in getSupplierListbyProductId()" + workerStateEvent.getSource().getValue().getAsString());

            }
        });

        apiClient.setOnFailed(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent workerStateEvent) {
//                loggerTranxSalesInvoiceCreate.error("Network API failed in getSupplierListbyProductId()" + workerStateEvent.getSource().getValue().toString());
            }
        });
        apiClient.start();



    }
//    private void getGSTR1B2CLargedata() {
//        APIClient apiClient = null;
////        tpTranxBottom.getSelectionModel().select(tbTranxBatchBottom);
////        loggerTranxSalesInvoiceCreate.debug("Get Supplier List Data Started...");
//        Map<String, String> map = new HashMap<>();
//        map.put("start_date", "");
//        map.put("end_date", "");
//        Map<String, Object> sortObject = new HashMap<>();
//        sortObject.put("colId", null);
//        sortObject.put("isAsc", true);
//        map.put("sort", sortObject.toString());
//        String formData = Globals.mapToStringforFormData(map);
//        System.out.println("FormData: " + formData);
//        apiClient = new APIClient(EndPoints.GET_GSTR1_B2C_LARGE_DATA, formData, RequestType.FORM_DATA);
//
//        apiClient.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
//            @Override
//            public void handle(WorkerStateEvent workerStateEvent) {
//                JsonObject jsonObject = new Gson().fromJson(workerStateEvent.getSource().getValue().toString(), JsonObject.class);
////                System.out.println("jsonObject =>" + jsonObject);
//                if (jsonObject.get("responseStatus").getAsInt() == 200) {
//                    JsonArray batchJsonArray = jsonObject.get("data").getAsJsonArray();
//                    JsonObject batchJsonObject = batchJsonArray.get(0).getAsJsonObject();
//                    String taxable_amt = batchJsonObject.get("taxable_amt") != null ? batchJsonObject.get("taxable_amt").toString():"";
//                    String igst_amt = batchJsonObject.get("total_igst") != null ? batchJsonObject.get("total_igst").toString(): "";
//                    String cgst_amt = batchJsonObject.get("total_cgst") != null ? batchJsonObject.get("total_cgst").toString(): "";
//                    String sgst_amt = batchJsonObject.get("total_sgst") != null ? batchJsonObject.get("total_sgst").toString(): "";
//                    String tax_amt = batchJsonObject.get("total_tax") != null ? batchJsonObject.get("total_tax").toString() :"";
//                    String invoice_amt = batchJsonObject.get("total_amt") != null ? batchJsonObject.get("total_amt").toString():"";
//                    String voucher_count = batchJsonObject.get("voucher_count") != null ? batchJsonObject.get("total_amt").toString():"";
//
//                    ObservableList<GSTR1DashboaedDTO> data = FXCollections.observableArrayList();
////                    data.add(new GSTR1DashboaedDTO("1", "B2B Invoice - 4A,4B,4C,6B,6C", voucher_count, taxable_amt, igst_amt, cgst_amt, sgst_amt, "", tax_amt, invoice_amt));
////                    data.add(new GSTR1DashboaedDTO("","", "Taxable Sales", "0.00", "", "0.00", "0.00", "", "0.00",""));
////                    data.add(new GSTR1DashboaedDTO("", "","Reverse Charge Supplies", "", "", "", "", "", "",""));
//
//
//                    data.add(new GSTR1DashboaedDTO("2", "B2C ( Large ) Invoice - 5A- 6B", voucher_count, taxable_amt, igst_amt, cgst_amt, sgst_amt, "", tax_amt,invoice_amt));
//                    data.add(new GSTR1DashboaedDTO("3", "B2C ( Small ) Invoice - 7", "", "", "", "", "", "", "",""));
//                    data.add(new GSTR1DashboaedDTO("4", "Credit / Debit notes ( Registered ) 9B", "", "", "", "", "", "", "",""));
//                    data.add(new GSTR1DashboaedDTO("5", "Credit / Debit notes (Unregistered ) 9B", "", "", "", "", "", "", "",""));
//                    data.add(new GSTR1DashboaedDTO("6", "Export Invoices - 6A", "", "", "", "", "", "", "",""));
//                    data.add(new GSTR1DashboaedDTO("7", "Tax Liability ( Advance received ) -11A(1),11A(2)", "", "", "", "", "", "", "",""));
//                    data.add(new GSTR1DashboaedDTO("8", "Adjustment of advances -11B(1) , 11B(2)","", "", "", "", "", "", "", ""));
//                    data.add(new GSTR1DashboaedDTO("9", "Nil Rated / Exempted Invoice - 8a,8B,8C,8D", "", "", "", "", "", "", "",""));
//                    data.add(new GSTR1DashboaedDTO("", "", "", "", "", "", "", "", "", ""));
//                    data.add(new GSTR1DashboaedDTO("", "", "", "", "", "", "", "", "",""));
//                    data.add(new GSTR1DashboaedDTO("", "", "", "", "", "", "", "", "",""));
//                    data.add(new GSTR1DashboaedDTO("", "", "", "", "", "", "", "", "",""));
//                    data.add(new GSTR1DashboaedDTO("", "", "", "", "", "", "", "", "",""));
//                    data.add(new GSTR1DashboaedDTO("Total", "", "", "", "", "", "", "", "",  ""));
//                    data.add(new GSTR1DashboaedDTO("", "HSN /SAC Summary", "", "0.00", "0.00", "0.00", "0.00", "0.00", "0.00", ""));
//                    data.add(new GSTR1DashboaedDTO("", "Document Summary", "", "", "", "", "", "", "", ""));
////                    return data;
//                    tblvGSTR1DashboardList.setItems(data);
//                    System.out.println("Success in Displaying data");
//                    GSTR1B2BLogger.info("Success in Displaying GSTR1B2C Large SCREEN1");
//                }
//            }
//        });
//        apiClient.setOnCancelled(new EventHandler<WorkerStateEvent>() {
//            @Override
//            public void handle(WorkerStateEvent workerStateEvent) {
////                loggerTranxSalesInvoiceCreate.error("Network API cancelled in getSupplierListbyProductId()" + workerStateEvent.getSource().getValue().toString());
//
//            }
//        });
//
//        apiClient.setOnFailed(new EventHandler<WorkerStateEvent>() {
//            @Override
//            public void handle(WorkerStateEvent workerStateEvent) {
////                loggerTranxSalesInvoiceCreate.error("Network API failed in getSupplierListbyProductId()" + workerStateEvent.getSource().getValue().toString());
//            }
//        });
//        apiClient.start();
////        loggerTranxSalesInvoiceCreate.debug("Get Supplier List Data End...");
//
//    }
}
