package com.opethic.genivis.controller.GSTR3B;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.opethic.genivis.dto.GSTR3BOutwardTaxableSuppliesDTO;
import com.opethic.genivis.network.RequestType;
import com.opethic.genivis.utils.DateValidator;
import com.opethic.genivis.dto.LedgerReport1DTO;
import com.opethic.genivis.dto.LedgerReport2DTO;
import com.opethic.genivis.dto.StockReport3DTO;
import com.opethic.genivis.dto.reqres.product.Communicator;
import com.opethic.genivis.network.APIClient;
import com.opethic.genivis.network.EndPoints;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;
import java.net.http.HttpResponse;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import static com.opethic.genivis.utils.FxmFileConstants.*;

public class GSTR3BOutwardTaxableSuppliesController implements Initializable {
    private static final Logger GSTR3BOutwardTaxableSuppliesListLogger = LoggerFactory.getLogger(GSTR3BOutwardTaxableSuppliesController.class);
    @FXML
    private TableView<GSTR3BOutwardTaxableSuppliesDTO> tblvGSTR3BOutwardTaxableSuppliesList;
    @FXML
    private TableColumn<GSTR3BOutwardTaxableSuppliesDTO, String> tblGSTR3BOutwardTaxableSuppliesSrNo, tblGSTR3BOutwardTaxableSuppliesDates,
            tblGSTR3BOutwardTaxableSuppliesInvoiceNo, tblGSTR3BOutwardTaxableSuppliesParticulars, tblGSTR3BOutwardTaxableSuppliesGSTIN,
            tblGSTR3BOutwardTaxableSuppliesVoucherType, tblGSTR3BOutwardTaxableSuppliesTaxableAmt, tblGSTR3BOutwardTaxableSuppliesIGSTAmt,
            tblGSTR3BOutwardTaxableSuppliesCGSTAmt, tblGSTR3BOutwardTaxableSuppliesSGSTAmt, tblGSTR3BOutwardTaxableSuppliesCessAmt,
            tblGSTR3BOutwardTaxableSuppliesTaxAmt, tblGSTR3BOutwardTaxableSuppliesInvoiceAmt;
    @FXML
    private TextField tfGSTR3BGSTR3BOutwardTaxableSuppliesSearch;

    private ObservableList<GSTR3BOutwardTaxableSuppliesDTO> originalData;

    @FXML
    private Button btExportPdf, btExportExcel, btExportCsv, btExportPrint;

    @FXML
    private BorderPane borderpane;

    @FXML
    private Label lblTotalTaxable_amt, lblTotalIgst_amt, lblTotalCgst_amt, lblTotalSgst_amt, lblTotalTax_amt, lblTotalInvoice_amt;

    String Start_date = "", End_date = "";

    @FXML
    private TextField tfStartDt, tfEndDt;

    private JsonObject jsonObject = null;

    public void initialize(URL url, ResourceBundle resourceBundle) {

        //TODO: resizing the table columns as per the resolution.
        tblvGSTR3BOutwardTaxableSuppliesList.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);

        responsiveTableDesign();


        GSTR3BOutwardTaxableSuppliesListLogger.info("Start of Initialize method of : GSTR3BOutwardTaxableSuppliesController");
        Platform.runLater(() -> tfGSTR3BGSTR3BOutwardTaxableSuppliesSearch.requestFocus());
        fetchGSTR3BOutwardTaxableSuppliesList("");
        borderpane.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ESCAPE) {
//                System.out.println("Esc Clicked....!");
                GlobalController.getInstance().addTabStatic(GSTR3B_DASHBOARD_LIST_SLUG, false);
            }
        });

        tfEndDt.setOnKeyPressed(event ->{
            if(event.getCode() == KeyCode.ENTER){
//                System.out.println("DateFilter From Date " + tfStartDt.getText().toString());
//                System.out.println("DateFilter To Date " + tfEndDt.getText().toString());
                fetchGSTR3BOutwardTaxableSuppliesList("");
            }
        });

        DateValidator.applyDateFormat(tfStartDt);
        DateValidator.applyDateFormat(tfEndDt);

        tfGSTR3BGSTR3BOutwardTaxableSuppliesSearch.textProperty().addListener((observable, oldValue, newValue) -> {
            filterData(newValue.trim());
        });

        nodetraversal(tfGSTR3BGSTR3BOutwardTaxableSuppliesSearch, tfStartDt);
        nodetraversal(tfStartDt, tfEndDt);
        nodetraversal(tfEndDt, btExportPdf);
        nodetraversal(btExportPdf, btExportExcel);
        nodetraversal(btExportExcel, btExportCsv);
        nodetraversal(btExportCsv, btExportPrint);
        tblvGSTR3BOutwardTaxableSuppliesList.requestFocus();
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

            if (event.getCode() == KeyCode.SPACE) {
                if (current_node instanceof Button button) {
                    button.fire();
                }
            }
        });
    }

    private void getEndDate() {
//        System.out.println("tfEndDt-------> " + tfEndDt.getText());
        fetchGSTR3BOutwardTaxableSuppliesList("");
    }

    private void responsiveTableDesign() {
        tblGSTR3BOutwardTaxableSuppliesDates.prefWidthProperty().bind(tblvGSTR3BOutwardTaxableSuppliesList.widthProperty().multiply(0.08));
        tblGSTR3BOutwardTaxableSuppliesInvoiceNo.prefWidthProperty().bind(tblvGSTR3BOutwardTaxableSuppliesList.widthProperty().multiply(0.08));
        tblGSTR3BOutwardTaxableSuppliesParticulars.prefWidthProperty().bind(tblvGSTR3BOutwardTaxableSuppliesList.widthProperty().multiply(0.12));
        tblGSTR3BOutwardTaxableSuppliesGSTIN.prefWidthProperty().bind(tblvGSTR3BOutwardTaxableSuppliesList.widthProperty().multiply(0.08));
        tblGSTR3BOutwardTaxableSuppliesVoucherType.prefWidthProperty().bind(tblvGSTR3BOutwardTaxableSuppliesList.widthProperty().multiply(0.08));
        tblGSTR3BOutwardTaxableSuppliesTaxableAmt.prefWidthProperty().bind(tblvGSTR3BOutwardTaxableSuppliesList.widthProperty().multiply(0.08));
        tblGSTR3BOutwardTaxableSuppliesIGSTAmt.prefWidthProperty().bind(tblvGSTR3BOutwardTaxableSuppliesList.widthProperty().multiply(0.08));
        tblGSTR3BOutwardTaxableSuppliesCGSTAmt.prefWidthProperty().bind(tblvGSTR3BOutwardTaxableSuppliesList.widthProperty().multiply(0.08));
        tblGSTR3BOutwardTaxableSuppliesSGSTAmt.prefWidthProperty().bind(tblvGSTR3BOutwardTaxableSuppliesList.widthProperty().multiply(0.08));
        tblGSTR3BOutwardTaxableSuppliesCessAmt.prefWidthProperty().bind(tblvGSTR3BOutwardTaxableSuppliesList.widthProperty().multiply(0.08));
        tblGSTR3BOutwardTaxableSuppliesTaxAmt.prefWidthProperty().bind(tblvGSTR3BOutwardTaxableSuppliesList.widthProperty().multiply(0.08));
        tblGSTR3BOutwardTaxableSuppliesInvoiceAmt.prefWidthProperty().bind(tblvGSTR3BOutwardTaxableSuppliesList.widthProperty().multiply(0.08));
    }

    private void fetchGSTR3BOutwardTaxableSuppliesList(String searchKey) {
        GSTR3BOutwardTaxableSuppliesListLogger.info("Fetch GSTR3B Outward Taxable Supplies List  : GSTR3BOutwardTaxableSuppliesController");

        APIClient apiClient = null;

        try{
            GSTR3BOutwardTaxableSuppliesListLogger.debug("Get GSTR3B Outward Taxable Supplies Data Started...");
            Map<String, String> body = new HashMap<>();
            body.put("searchText", "");
//            body.put("start_date", "");
//            body.put("end_date", "");
            if(!tfStartDt.getText().equalsIgnoreCase("")  || !tfEndDt.getText().equalsIgnoreCase("")){
//                System.out.println("date sdf " );
                body.put("start_date", Communicator.text_to_date.fromString(tfStartDt.getText()).toString());
                body.put("end_date", Communicator.text_to_date.fromString(tfEndDt.getText()).toString());
            }
//            System.out.println("body ----------->  " + body);
            String requestBody = Globals.mapToStringforFormData(body);
//            HttpResponse<String> response = APIClient.postFormDataRequest(requestBody, EndPoints.GSTR3B_OutwardTaxableSupplies_LIST);
            apiClient = new APIClient(EndPoints.GSTR3B_OutwardTaxableSupplies_LIST, requestBody, RequestType.FORM_DATA);
            apiClient.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
                @Override
                public void handle(WorkerStateEvent workerStateEvent) {
//                    JsonObject responseBody = new Gson().fromJson(response.body(), JsonObject.class);
                    jsonObject = new Gson().fromJson(workerStateEvent.getSource().getValue().toString(), JsonObject.class);
//            System.out.println("fetchGSTR3BOutwardTaxableSuppliesList" + responseBody);
//                    System.out.println("jsonObject--------------------------" + jsonObject);
                    ObservableList<GSTR3BOutwardTaxableSuppliesDTO> observableList = FXCollections.observableArrayList();
                    if(jsonObject.get("responseStatus").getAsInt() == 200){
                        JsonArray responseList = jsonObject.getAsJsonArray("data");
//                System.out.println("fetchGSTR3BOutwardTaxableSuppliesList" + responseList);
                        Start_date = (jsonObject.get("start_date").getAsString());
                        End_date = jsonObject.get("end_date").getAsString();
                        LocalDate startLocalDt = LocalDate.parse(Start_date);
                        LocalDate endLocalDt = LocalDate.parse(End_date);

                        if (responseList.size() > 0){
                            tblvGSTR3BOutwardTaxableSuppliesList.setVisible(true);
                            for (JsonElement element : responseList){
                                JsonObject item = element.getAsJsonObject();
                                String id = item.get("id") != null ? item.get("id").getAsString() : "";
                                String dates = item.get("bill_date") != null ? item.get("bill_date").getAsString() : "";
                                String invoice_no = item.get("sales_invoice") != null ? item.get("sales_invoice").getAsString() : "";
                                String particulars = item.get("ledger_name") != null ? item.get("ledger_name").getAsString() : "";
                                String gstin_uin = item.get("gstin") != null ? item.get("gstin").getAsString() : "";
                                String voucher_type = item.get("voucher_type") != null ? item.get("voucher_type").getAsString() : "";
                                String taxable_amt = item.get("taxable_amt") != null ? item.get("taxable_amt").getAsString() : "";
                                String igst_amt = item.get("totaligst") != null ? item.get("totaligst").getAsString() : "";
                                String cgst_amt = item.get("totalcgst") != null ? item.get("totalcgst").getAsString() : "";
                                String sgst_amt = item.get("totalsgst") != null ? item.get("totalsgst").getAsString() : "";
                                String cess_amt = item.get("") != null ? item.get("").getAsString() : "";
                                String tax_amt = item.get("total_tax") != null ? item.get("total_tax").getAsString() : "";
                                String invoice_amt = item.get("total_amount") != null ? item.get("total_amount").getAsString() : "";

                                observableList.add(new GSTR3BOutwardTaxableSuppliesDTO(id, dates, invoice_no, particulars, gstin_uin, voucher_type, taxable_amt, igst_amt, cgst_amt, sgst_amt, cess_amt, tax_amt, invoice_amt ));
                            }

                            tfStartDt.setText(startLocalDt.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
                            tfEndDt.setText(endLocalDt.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));

                            tblGSTR3BOutwardTaxableSuppliesDates.setCellValueFactory(new PropertyValueFactory<>("dates"));
                            tblGSTR3BOutwardTaxableSuppliesInvoiceNo.setCellValueFactory(new PropertyValueFactory<>("invoice_no"));
                            tblGSTR3BOutwardTaxableSuppliesParticulars.setCellValueFactory(new PropertyValueFactory<>("particulars"));
                            tblGSTR3BOutwardTaxableSuppliesGSTIN.setCellValueFactory(new PropertyValueFactory<>("gstin_uin"));
                            tblGSTR3BOutwardTaxableSuppliesVoucherType.setCellValueFactory(new PropertyValueFactory<>("voucher_type"));
                            tblGSTR3BOutwardTaxableSuppliesTaxableAmt.setCellValueFactory(new PropertyValueFactory<>("taxable_amt"));
                            tblGSTR3BOutwardTaxableSuppliesIGSTAmt.setCellValueFactory(new PropertyValueFactory<>("igst_amt"));
                            tblGSTR3BOutwardTaxableSuppliesCGSTAmt.setCellValueFactory(new PropertyValueFactory<>("cgst_amt"));
                            tblGSTR3BOutwardTaxableSuppliesSGSTAmt.setCellValueFactory(new PropertyValueFactory<>("sgst_amt"));
                            tblGSTR3BOutwardTaxableSuppliesCessAmt.setCellValueFactory(new PropertyValueFactory<>("cess_amt"));
                            tblGSTR3BOutwardTaxableSuppliesTaxAmt.setCellValueFactory(new PropertyValueFactory<>("tax_amt"));
                            tblGSTR3BOutwardTaxableSuppliesInvoiceAmt.setCellValueFactory(new PropertyValueFactory<>("invoice_amt"));

                            tblvGSTR3BOutwardTaxableSuppliesList.setItems(observableList);
                            originalData = observableList;
                            calculateTotalAmount();
                            GSTR3BOutwardTaxableSuppliesListLogger.debug("Success in Displaying GSTR3B Outward Taxable Supplies List : GSTR3BOutwardTaxableSuppliesController");

                        } else {
                            GSTR3BOutwardTaxableSuppliesListLogger.debug("GSTR3B Outward Taxable Supplies List ResponseObject is null : GSTR3BOutwardTaxableSuppliesController");
                            tblvGSTR3BOutwardTaxableSuppliesList.getItems().clear();
                            calculateTotalAmount();
                        }
                    } else {
                        GSTR3BOutwardTaxableSuppliesListLogger.debug("Error in response of GSTR3B Outward Taxable Supplies List : GSTR3BOutwardTaxableSuppliesController");
                        tblvGSTR3BOutwardTaxableSuppliesList.getItems().clear();
                        calculateTotalAmount();
                    }
                }
            });
            apiClient.setOnCancelled(new EventHandler<WorkerStateEvent>() {
                @Override
                public void handle(WorkerStateEvent workerStateEvent) {
                    GSTR3BOutwardTaxableSuppliesListLogger.error("Network API cancelled in fetchGSTR3BOutwardTaxableSuppliesList()" + workerStateEvent.getSource().getValue().toString());

                }
            });

            apiClient.setOnFailed(new EventHandler<WorkerStateEvent>() {
                @Override
                public void handle(WorkerStateEvent workerStateEvent) {
                    GSTR3BOutwardTaxableSuppliesListLogger.error("Network API failed in fetchGSTR3BOutwardTaxableSuppliesList()" + workerStateEvent.getSource().getValue().toString());
                }
            });
            apiClient.start();
            GSTR3BOutwardTaxableSuppliesListLogger.debug("Get GSTR3B Outward Taxable Supplies Data End...");

        } catch (Exception e) {
            GSTR3BOutwardTaxableSuppliesListLogger.error("GSTR3B Outward Taxable Supplies List Error is " + e.getMessage());
            e.printStackTrace();
        }
        finally {
            apiClient = null;
        }

    }

    private void calculateTotalAmount() {
        ObservableList<GSTR3BOutwardTaxableSuppliesDTO> filteredData = tblvGSTR3BOutwardTaxableSuppliesList.getItems();
        // Calculate the Totals
        double totalTaxable_amt= 0.0;
        double totalIgst_amt = 0.0;
        double totalCgst_amt = 0.0;
        double totalSgst_amt = 0.0;
        double totalTax_amt = 0.0;
        double totalInvoice_amt = 0.0;
        for (GSTR3BOutwardTaxableSuppliesDTO item : filteredData) {
            totalTaxable_amt += Double.parseDouble(item.getTaxable_amt().isEmpty() ? "0.0" : item.getTaxable_amt());
            totalIgst_amt += Double.parseDouble(item.getIgst_amt().isEmpty() ? "0.0" : item.getIgst_amt());
            totalCgst_amt += Double.parseDouble(item.getCgst_amt().isEmpty() ? "0.0" : item.getCgst_amt());
            totalSgst_amt += Double.parseDouble(item.getSgst_amt().isEmpty() ? "0.0" : item.getSgst_amt());
            totalTax_amt += Double.parseDouble(item.getTax_amt().isEmpty() ? "0.0" : item.getTax_amt());
            totalInvoice_amt += Double.parseDouble(item.getInvoice_amt().isEmpty() ? "0.0" : item.getInvoice_amt());
        }

        // Update Labels in the FXML
        lblTotalTaxable_amt.setText(String.format("%.2f", totalTaxable_amt));
        lblTotalIgst_amt.setText(String.format("%.2f", totalIgst_amt));
        lblTotalCgst_amt.setText(String.format("%.2f", totalCgst_amt));
        lblTotalSgst_amt.setText(String.format("%.2f", totalSgst_amt));
        lblTotalTax_amt.setText(String.format("%.2f", totalTax_amt));
        lblTotalInvoice_amt.setText(String.format("%.2f", totalInvoice_amt));
    }


    private void filterData(String keyword) {
        ObservableList<GSTR3BOutwardTaxableSuppliesDTO> filteredData = FXCollections.observableArrayList();

        GSTR3BOutwardTaxableSuppliesListLogger.error("Search GSTR3B Outward Taxable Supplies in GSTR3BOutwardTaxableSuppliesController");
        if (keyword.isEmpty()) {
            tblvGSTR3BOutwardTaxableSuppliesList.setItems(originalData);
            return;
        }

        for (GSTR3BOutwardTaxableSuppliesDTO item : originalData) {
            if (matchesKeyword(item, keyword)) {
                filteredData.add(item);
            }
        }

        tblvGSTR3BOutwardTaxableSuppliesList.setItems(filteredData);
        calculateTotalAmount();
    }

    private boolean matchesKeyword(GSTR3BOutwardTaxableSuppliesDTO item, String keyword) {
        String lowerCaseKeyword = keyword.toLowerCase();
        return
                item.getDates().toLowerCase().contains(lowerCaseKeyword) ||
                item.getInvoice_no().toLowerCase().contains(lowerCaseKeyword) ||
                item.getParticulars().toLowerCase().contains(lowerCaseKeyword) ||
                item.getGstin_uin().toLowerCase().contains(lowerCaseKeyword) ||
                item.getVoucher_type().toLowerCase().contains(lowerCaseKeyword) ||
                item.getTaxable_amt().toLowerCase().contains(lowerCaseKeyword) ||
                item.getIgst_amt().toLowerCase().contains(lowerCaseKeyword) ||
                item.getCgst_amt().toLowerCase().contains(lowerCaseKeyword) ||
                item.getSgst_amt().toLowerCase().contains(lowerCaseKeyword) ||
                item.getCess_amt().toLowerCase().contains(lowerCaseKeyword) ||
                item.getTax_amt().toLowerCase().contains(lowerCaseKeyword) ||
                item.getInvoice_amt().toLowerCase().contains(lowerCaseKeyword);
    }
}
