package com.opethic.genivis.controller.GSTR3B;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.opethic.genivis.dto.GSTR3BAllOtherITCDTO;
import com.opethic.genivis.dto.GSTR3BOutwardTaxableSuppliesDTO;
//import com.opethic.genivis.dto.Transaction;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;
import java.net.http.HttpResponse;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import static com.opethic.genivis.utils.FxmFileConstants.GSTR3B_DASHBOARD_LIST_SLUG;

public class GSTR3BAllOtherITCController implements Initializable {
    private static final Logger GSTR3BAllOtherITCListLogger = LoggerFactory.getLogger(GSTR3BAllOtherITCController.class);
    @FXML
    private TableView<GSTR3BAllOtherITCDTO> tblvGSTR3BAllOtherITCList;
    @FXML
    private TableColumn<GSTR3BAllOtherITCDTO, String> tblGSTR3BAllOtherITCSrNo, tblGSTR3BAllOtherITCDates,
            tblGSTR3BAllOtherITCInvoiceNo, tblGSTR3BAllOtherITCParticulars, tblGSTR3BAllOtherITCGSTIN,
            tblGSTR3BAllOtherITCVoucherType, tblGSTR3BAllOtherITCTaxableAmt, tblGSTR3BAllOtherITCIGSTAmt,
            tblGSTR3BAllOtherITCCGSTAmt, tblGSTR3BAllOtherITCSGSTAmt, tblGSTR3BAllOtherITCCessAmt,
            tblGSTR3BAllOtherITCTaxAmt, tblGSTR3BAllOtherITCInvoiceAmt;
    @FXML
    private TextField tfGSTR3BGSTR3BAllOtherITCSearch;

    private ObservableList<GSTR3BAllOtherITCDTO> originalData;

    @FXML
    private BorderPane borderpane;

    @FXML
    private Label lblTotalTaxable_amt, lblTotalIgst_amt, lblTotalCgst_amt, lblTotalSgst_amt, lblTotalTax_amt, lblTotalInvoice_amt;

    String Start_date = "", End_date = "";

    @FXML
    private TextField tfStartDt, tfEndDt;

    @FXML
    private Button btExportPdf, btExportExcel, btExportCsv, btExportPrint;

    private JsonObject jsonObject = null;

    public void initialize(URL url, ResourceBundle resourceBundle) {


        //TODO: resizing the table columns as per the resolution.
        tblvGSTR3BAllOtherITCList.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);

        responsiveTableDesign();


        GSTR3BAllOtherITCListLogger.info("Start of Initialize method of : GSTR3BAllOtherITCController");
        Platform.runLater(() -> tfGSTR3BGSTR3BAllOtherITCSearch.requestFocus());
        fetchGSTR3BAllOtherITCList("");
        borderpane.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ESCAPE) {
//                System.out.println("Esc Clicked....!");
                GlobalController.getInstance().addTabStatic(GSTR3B_DASHBOARD_LIST_SLUG, false);
            }
        });

        tfEndDt.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
//                System.out.println("DateFilter From Date " + tfStartDt.getText().toString());
//                System.out.println("DateFilter To Date " + tfEndDt.getText().toString());
                fetchGSTR3BAllOtherITCList("");
            }
        });

        DateValidator.applyDateFormat(tfStartDt);
        DateValidator.applyDateFormat(tfEndDt);

        tfGSTR3BGSTR3BAllOtherITCSearch.textProperty().addListener((observable, oldValue, newValue) -> {
            filterData(newValue.trim());
        });

        nodetraversal(tfGSTR3BGSTR3BAllOtherITCSearch, tfStartDt);
        nodetraversal(tfStartDt, tfEndDt);
        nodetraversal(tfEndDt, btExportPdf);
        nodetraversal(btExportPdf, btExportExcel);
        nodetraversal(btExportExcel, btExportCsv);
        nodetraversal(btExportCsv, btExportPrint);
        tblvGSTR3BAllOtherITCList.requestFocus();

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
        fetchGSTR3BAllOtherITCList("");
    }

    private void responsiveTableDesign() {
        tblGSTR3BAllOtherITCDates.prefWidthProperty().bind(tblvGSTR3BAllOtherITCList.widthProperty().multiply(0.08));
        tblGSTR3BAllOtherITCInvoiceNo.prefWidthProperty().bind(tblvGSTR3BAllOtherITCList.widthProperty().multiply(0.08));
        tblGSTR3BAllOtherITCParticulars.prefWidthProperty().bind(tblvGSTR3BAllOtherITCList.widthProperty().multiply(0.12));
        tblGSTR3BAllOtherITCGSTIN.prefWidthProperty().bind(tblvGSTR3BAllOtherITCList.widthProperty().multiply(0.08));
        tblGSTR3BAllOtherITCVoucherType.prefWidthProperty().bind(tblvGSTR3BAllOtherITCList.widthProperty().multiply(0.08));
        tblGSTR3BAllOtherITCTaxableAmt.prefWidthProperty().bind(tblvGSTR3BAllOtherITCList.widthProperty().multiply(0.08));
        tblGSTR3BAllOtherITCIGSTAmt.prefWidthProperty().bind(tblvGSTR3BAllOtherITCList.widthProperty().multiply(0.08));
        tblGSTR3BAllOtherITCCGSTAmt.prefWidthProperty().bind(tblvGSTR3BAllOtherITCList.widthProperty().multiply(0.08));
        tblGSTR3BAllOtherITCSGSTAmt.prefWidthProperty().bind(tblvGSTR3BAllOtherITCList.widthProperty().multiply(0.08));
        tblGSTR3BAllOtherITCCessAmt.prefWidthProperty().bind(tblvGSTR3BAllOtherITCList.widthProperty().multiply(0.08));
        tblGSTR3BAllOtherITCTaxAmt.prefWidthProperty().bind(tblvGSTR3BAllOtherITCList.widthProperty().multiply(0.08));
        tblGSTR3BAllOtherITCInvoiceAmt.prefWidthProperty().bind(tblvGSTR3BAllOtherITCList.widthProperty().multiply(0.08));
    }


    private void fetchGSTR3BAllOtherITCList(String searchKey) {
        GSTR3BAllOtherITCListLogger.info("Fetch GSTR3B All Other ITC List  : GSTR3BAllOtherITCController");
        APIClient apiClient = null;
        try {
            GSTR3BAllOtherITCListLogger.debug("Get GSTR3B All Other ITC Data Started...");
            Map<String, String> body = new HashMap<>();
            body.put("searchText", "");
//            body.put("start_date", "");
//            body.put("end_date", "");
            if (!tfStartDt.getText().equalsIgnoreCase("") || !tfEndDt.getText().equalsIgnoreCase("")) {
//                System.out.println("date sdf " );
                body.put("start_date", Communicator.text_to_date.fromString(tfStartDt.getText()).toString());
                body.put("end_date", Communicator.text_to_date.fromString(tfEndDt.getText()).toString());
            }
//            System.out.println("body ----------->  " + body);
            String requestBody = Globals.mapToStringforFormData(body);
//            HttpResponse<String> response = APIClient.postFormDataRequest(requestBody, EndPoints.GSTR3B_AllOtherITC_LIST);
            apiClient = new APIClient(EndPoints.GSTR3B_AllOtherITC_LIST, requestBody, RequestType.FORM_DATA);
            apiClient.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
                @Override
                public void handle(WorkerStateEvent workerStateEvent) {
//                    JsonObject responseBody = new Gson().fromJson(response.body(), JsonObject.class);
                    jsonObject = new Gson().fromJson(workerStateEvent.getSource().getValue().toString(), JsonObject.class);
//            System.out.println("fetchGSTR3BAllOtherITCList" + responseBody);
                    ObservableList<GSTR3BAllOtherITCDTO> observableList = FXCollections.observableArrayList();
                    if (jsonObject.get("responseStatus").getAsInt() == 200) {
                        JsonArray responseList = jsonObject.getAsJsonArray("data");
//                System.out.println("fetchGSTR3BOutwardTaxableSuppliesList" + responseList);
                        Start_date = (jsonObject.get("start_date").getAsString());
                        End_date = jsonObject.get("end_date").getAsString();
                        LocalDate startLocalDt = LocalDate.parse(Start_date);
                        LocalDate endLocalDt = LocalDate.parse(End_date);

                        if (responseList.size() > 0) {
                            tblvGSTR3BAllOtherITCList.setVisible(true);
                            for (JsonElement element : responseList) {
                                JsonObject item = element.getAsJsonObject();
                                String id = item.get("id") != null ? item.get("id").getAsString() : "";
                                String dates = item.get("bill_date") != null ? item.get("bill_date").getAsString() : "";
                                String invoice_no = item.get("purchase_invoice") != null ? item.get("purchase_invoice").getAsString() : "";
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

                                observableList.add(new GSTR3BAllOtherITCDTO(id, dates, invoice_no, particulars, gstin_uin, voucher_type, taxable_amt, igst_amt, cgst_amt, sgst_amt, cess_amt, tax_amt, invoice_amt));
                            }

                            tfStartDt.setText(startLocalDt.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
                            tfEndDt.setText(endLocalDt.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));

                            tblGSTR3BAllOtherITCDates.setCellValueFactory(new PropertyValueFactory<>("dates"));
                            tblGSTR3BAllOtherITCInvoiceNo.setCellValueFactory(new PropertyValueFactory<>("invoice_no"));
                            tblGSTR3BAllOtherITCParticulars.setCellValueFactory(new PropertyValueFactory<>("particulars"));
                            tblGSTR3BAllOtherITCGSTIN.setCellValueFactory(new PropertyValueFactory<>("gstin_uin"));
                            tblGSTR3BAllOtherITCVoucherType.setCellValueFactory(new PropertyValueFactory<>("voucher_type"));
                            tblGSTR3BAllOtherITCTaxableAmt.setCellValueFactory(new PropertyValueFactory<>("taxable_amt"));
                            tblGSTR3BAllOtherITCIGSTAmt.setCellValueFactory(new PropertyValueFactory<>("igst_amt"));
                            tblGSTR3BAllOtherITCCGSTAmt.setCellValueFactory(new PropertyValueFactory<>("cgst_amt"));
                            tblGSTR3BAllOtherITCSGSTAmt.setCellValueFactory(new PropertyValueFactory<>("sgst_amt"));
                            tblGSTR3BAllOtherITCCessAmt.setCellValueFactory(new PropertyValueFactory<>("cess_amt"));
                            tblGSTR3BAllOtherITCTaxAmt.setCellValueFactory(new PropertyValueFactory<>("tax_amt"));
                            tblGSTR3BAllOtherITCInvoiceAmt.setCellValueFactory(new PropertyValueFactory<>("invoice_amt"));


                            tblvGSTR3BAllOtherITCList.setItems(observableList);

                            originalData = observableList;
                            calculateTotalAmount();
                            GSTR3BAllOtherITCListLogger.debug("Success in Displaying GSTR3B All Other ITC List : GSTR3BAllOtherITCController");

                        } else {
                            GSTR3BAllOtherITCListLogger.debug("GSTR3B All Other ITC List ResponseObject is null : GSTR3BAllOtherITCController");
                            tblvGSTR3BAllOtherITCList.getItems().clear();
                            calculateTotalAmount();
                        }
                    } else {
                        GSTR3BAllOtherITCListLogger.debug("Error in response of GSTR3B All Other ITC List : GSTR3BAllOtherITCController");
                        tblvGSTR3BAllOtherITCList.getItems().clear();
                        calculateTotalAmount();
                    }
                }
            });
            apiClient.setOnCancelled(new EventHandler<WorkerStateEvent>() {
                @Override
                public void handle(WorkerStateEvent workerStateEvent) {
                    GSTR3BAllOtherITCListLogger.error("Network API cancelled in fetchGSTR3BAllOtherITCList()" + workerStateEvent.getSource().getValue().toString());

                }
            });

            apiClient.setOnFailed(new EventHandler<WorkerStateEvent>() {
                @Override
                public void handle(WorkerStateEvent workerStateEvent) {
                    GSTR3BAllOtherITCListLogger.error("Network API failed in fetchGSTR3BAllOtherITCList()" + workerStateEvent.getSource().getValue().toString());
                }
            });
            apiClient.start();
            GSTR3BAllOtherITCListLogger.debug("Get GSTR3B All Other ITC Data End...");

        } catch (Exception e) {
            GSTR3BAllOtherITCListLogger.error("GSTR3B All Other ITC List Error is " + e.getMessage());
            e.printStackTrace();
        }
        finally {
            apiClient = null;
        }

    }

    private void calculateTotalAmount() {
        ObservableList<GSTR3BAllOtherITCDTO> filteredData = tblvGSTR3BAllOtherITCList.getItems();
        // Calculate the Totals
        double totalTaxable_amt = 0.0;
        double totalIgst_amt = 0.0;
        double totalCgst_amt = 0.0;
        double totalSgst_amt = 0.0;
        double totalTax_amt = 0.0;
        double totalInvoice_amt = 0.0;
        for (GSTR3BAllOtherITCDTO item : filteredData) {
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
        ObservableList<GSTR3BAllOtherITCDTO> filteredData = FXCollections.observableArrayList();

        GSTR3BAllOtherITCListLogger.error("Search GSTR3B Outward Taxable Supplies in GSTR3BAllOtherITCController");
        if (keyword.isEmpty()) {
            tblvGSTR3BAllOtherITCList.setItems(originalData);
            return;
        }

        for (GSTR3BAllOtherITCDTO item : originalData) {
            if (matchesKeyword(item, keyword)) {
                filteredData.add(item);
            }
        }

        tblvGSTR3BAllOtherITCList.setItems(filteredData);
        calculateTotalAmount();
    }

    private boolean matchesKeyword(GSTR3BAllOtherITCDTO item, String keyword) {
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
