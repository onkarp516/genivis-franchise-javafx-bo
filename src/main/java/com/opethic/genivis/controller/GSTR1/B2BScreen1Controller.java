package com.opethic.genivis.controller.GSTR1;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.opethic.genivis.controller.tranx_sales.SalesQuotToOrderController;
import com.opethic.genivis.controller.tranx_sales.SalesQuotationCreateController;
import com.opethic.genivis.dto.GSTR1.*;
import com.opethic.genivis.dto.GSTR3BOutwardTaxableSuppliesDTO;
import com.opethic.genivis.dto.SalesQuotationDTO;
import com.opethic.genivis.dto.TranxLedgerWindowDTO;
import com.opethic.genivis.dto.reqres.product.Communicator;
import com.opethic.genivis.network.APIClient;
import com.opethic.genivis.network.EndPoints;
import com.opethic.genivis.network.RequestType;
import com.opethic.genivis.utils.DateValidator;
import com.opethic.genivis.utils.GlobalController;
import com.opethic.genivis.utils.Globals;
import javafx.animation.PauseTransition;
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
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.net.URL;
import java.net.http.HttpResponse;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import static com.opethic.genivis.controller.tranx_sales.SalesQuotationCreateController.SalesQuotationLogger;
import static com.opethic.genivis.utils.FxmFileConstants.*;

public class B2BScreen1Controller implements Initializable {

    ObservableList<GSTR1B2B1DTO> GSTR1B2B1ObservableList = FXCollections.observableArrayList();
    @FXML
    private ScrollPane spRootPane;
    @FXML
    private BorderPane root;
    @FXML
    private HBox hbGstr1B2b1Footer;
    @FXML
    private TableView<GSTR1B2B1DTO> tblvB2bScreen1List;
    @FXML
    private TextField tfGstr1B2b1FromDt, tfGstr1B2b1ToDt, tfGstr1B2b1Search;

    @FXML
    private Button GSTR1B2B1BtnPdf, GSTR1B2B1BtnExcel, GSTR1B2B1BtnCsv, GSTR1B2B1BtnPrint;

    @FXML
    private TableColumn<GSTR1B2B1DTO,String> tblcGstr1B2b1SerialNo, tblcGstr1B2b1Particulars, tblcGstr1B2b1Gstin, tblcGstr1B2b1VoucherCount, tblcGstr1B2b1TaxableAmt
            , tblcGstr1B2b1IgstAmt, tblcGstr1B2b1CgstAmt, tblcGstr1B2b1SgstAmt, tblcGstr1B2b1CessAmt, tblcGstr1B2b1TaxAmt, tblcGstr1B2b1InvoiceAmt;

    @FXML
    private Label lbGstr1B2B1totalLabel, lbGstr1B2B1LedgerName, lbGstr1B2B1GSTNo, lbGstr1B2B1VoucherCount, lbGstr1B2B1TaxableAmt, lbGstr1B2B1TotalIgst, lbGstr1B2B1TotalCgst, lbGstr1B2B1TotalSgst, lbGstr1B2B1TotalTax, lbGstr1B2B1TotalAmt;
    String Start_date = "", End_date = "";

    String startDate = "", endDate ="";


    public static final Logger B2BScreen1Logger = LoggerFactory.getLogger(B2BScreen1Controller.class);

    private ObservableList<GSTR1B2B1DTO> originalData;


    public void initialize(URL url, ResourceBundle resourceBundle)
    {
      System.out.println("success");
//        root.setBottom(hbGstr1B2b1Footer);
        Platform.runLater(() -> {
            tfGstr1B2b1FromDt.requestFocus();
            tfGstr1B2b1ToDt.setOnKeyPressed(event ->{
                if(event.getCode() == KeyCode.ENTER){
                    fetchGSTR1B2B1Data();
                }
            });
            //todo:excel Export
                        GSTR1B2B1BtnExcel.setOnAction(event->{
                            exportToExcel();
                        });

        });

        //todo: Responsive code for tableview
        B2BresponsiveTable();
        spRootPane.sceneProperty().addListener((observable, oldScene, newScene) -> {
            if (newScene != null && newScene.getWindow() instanceof Stage) {
                Communicator.stage = (Stage) newScene.getWindow();
            }
        });

        DateValidator.applyDateFormat(tfGstr1B2b1FromDt);
        DateValidator.applyDateFormat(tfGstr1B2b1ToDt);

        fetchGSTR1B2B1Data();


        tblvB2bScreen1List.setOnMouseClicked(event->{
            if(event.getClickCount()==2){
                GSTR1B2B1DTO selectedItem = (GSTR1B2B1DTO) tblvB2bScreen1List.getSelectionModel().getSelectedItem();
                String ledger_id = selectedItem.getLedgerId();
                B2BScreen2Controller.startDate = tfGstr1B2b1FromDt.getText();
                B2BScreen2Controller.endDate = tfGstr1B2b1ToDt.getText();
                System.out.println("ledger_id 1 -->" + ledger_id);
                GlobalController.getInstance().addTabStatic1(GSTR1_B2B_SCREEN2_LIST_SLUG, false, Integer.valueOf(ledger_id));
            }
        });

        // todo : search functionality
        tfGstr1B2b1Search.textProperty().addListener((observable, oldValue, newValue) -> {
            filterData(newValue.trim());
        });

        spRootPane.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ESCAPE) {
//                System.out.println("Esc Clicked....!");
                GlobalController.getInstance().addTabStatic(GSTR1_DASHBOARD_LIST_SLUG, false);
            }
        });

        GSTR1B2B1BtnPdf.focusedProperty().addListener((obs, wasFocused, isNowFocused) -> {
            if (isNowFocused) {
                GSTR1B2B1BtnPdf.setStyle("-fx-border-width: 2; -fx-border-color: #03dbfc");
            } else {
                GSTR1B2B1BtnPdf.setStyle("");
            }
        });

        GSTR1B2B1BtnPdf.hoverProperty().addListener((obs, wasHovered, isNowHovered) -> {
            if (isNowHovered) {
                GSTR1B2B1BtnPdf.setStyle("-fx-border-width: 2; -fx-border-color: #03dbfc");
            } else {
                GSTR1B2B1BtnPdf.setStyle("");
            }
        });

        GSTR1B2B1BtnExcel.focusedProperty().addListener((obs, wasFocused, isNowFocused) -> {
            if (isNowFocused) {
                GSTR1B2B1BtnExcel.setStyle("-fx-border-width: 2; -fx-border-color: #03dbfc");
            } else {
                GSTR1B2B1BtnExcel.setStyle("");
            }
        });

        GSTR1B2B1BtnExcel.hoverProperty().addListener((obs, wasHovered, isNowHovered) -> {
            if (isNowHovered) {
                GSTR1B2B1BtnExcel.setStyle("-fx-border-width: 2; -fx-border-color: #03dbfc");
            } else {
                GSTR1B2B1BtnExcel.setStyle("");
            }
        });

        GSTR1B2B1BtnCsv.focusedProperty().addListener((obs, wasFocused, isNowFocused) -> {
            if (isNowFocused) {
                GSTR1B2B1BtnCsv.setStyle("-fx-border-width: 2; -fx-border-color: #03dbfc");
            } else {
                GSTR1B2B1BtnCsv.setStyle("");
            }
        });

        GSTR1B2B1BtnCsv.hoverProperty().addListener((obs, wasHovered, isNowHovered) -> {
            if (isNowHovered) {
                GSTR1B2B1BtnCsv.setStyle("-fx-border-width: 2; -fx-border-color: #03dbfc");
            } else {
                GSTR1B2B1BtnCsv.setStyle("");
            }
        });

        GSTR1B2B1BtnPrint.focusedProperty().addListener((obs, wasFocused, isNowFocused) -> {
            if (isNowFocused) {
                GSTR1B2B1BtnPrint.setStyle("-fx-border-width: 2; -fx-border-color: #03dbfc");
            } else {
                GSTR1B2B1BtnPrint.setStyle("");
            }
        });

        GSTR1B2B1BtnPrint.hoverProperty().addListener((obs, wasHovered, isNowHovered) -> {
            if (isNowHovered) {
                GSTR1B2B1BtnPrint.setStyle("-fx-border-width: 2; -fx-border-color: #03dbfc");
            } else {
                GSTR1B2B1BtnPrint.setStyle("");
            }
        });

        nodetraversal(tfGstr1B2b1FromDt, tfGstr1B2b1ToDt);
        nodetraversal(tfGstr1B2b1ToDt, tfGstr1B2b1Search);
        nodetraversal(tfGstr1B2b1Search, GSTR1B2B1BtnPdf);
        nodetraversal(GSTR1B2B1BtnPdf, GSTR1B2B1BtnExcel);
        nodetraversal(GSTR1B2B1BtnExcel, GSTR1B2B1BtnCsv);
        nodetraversal(GSTR1B2B1BtnCsv,GSTR1B2B1BtnPrint);
        tblvB2bScreen1List.requestFocus();
        }

    //excelExport code
    private void exportToExcel() {
        ObservableList<GSTR1B2B1DTO> data = tblvB2bScreen1List.getItems();

        try {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Save Excel File");
            fileChooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("Excel Files", "*.xlsx")
            );
            File file = fileChooser.showSaveDialog(new Stage());
            if (file != null) {
                Workbook workbook = new XSSFWorkbook();
                Sheet sheet = workbook.createSheet("Data");

                // Create font for header
                Font headerFont = workbook.createFont();
                headerFont.setBold(true);
                headerFont.setFontHeightInPoints((short) 12);
                CellStyle headerCellStyle = workbook.createCellStyle();
//                headerCellStyle.setFillForegroundColor(IndexedColors.LIGHT_GREEN.getIndex());
//                headerCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
                // Create cell style with bold header font
                headerCellStyle.setFont(headerFont);

                // Create header row
                Row headerRow = sheet.createRow(0);

                // Populate header row
                String[] headers = getColumnHeaders();

                for (int colNum = 0; colNum < headers.length; colNum++) {
                    Cell headerCell = headerRow.createCell(colNum);
                    headerCell.setCellValue(headers[colNum]);
                    headerCell.setCellStyle(headerCellStyle);
                }
                int sumOfVoucherCount = 0;
                Double sumOfTaxableAmt = 0.0, sumOfIGSTAmt = 0.0, sumOfCGSTAmt = 0.0, sumOfSGSTAmt = 0.0, sumOfTaxAmt = 0.0, sumOfInvoiceAmt = 0.0;
                // Populate data rows
                int rowNum = 1;
                for (GSTR1B2B1DTO dto : data) {
                    Row row = sheet.createRow(rowNum++);
                    row.createCell(0).setCellValue(dto.getSr_no());
                    row.createCell(1).setCellValue(dto.getLedgerName());
                    row.createCell(2).setCellValue(dto.getGst_number());
                    row.createCell(3).setCellValue(dto.getTotal_invoices());
                    row.createCell(4).setCellValue(dto.getTaxable_amt());
                    row.createCell(5).setCellValue(dto.getTotal_igst());
                    row.createCell(6).setCellValue(dto.getTotal_cgst());
                    row.createCell(7).setCellValue(dto.getTotal_sgst());
                    row.createCell(9).setCellValue(dto.getTotal_tax());
                    row.createCell(10).setCellValue(dto.getTotal_amt());
//            // Add more cells if you have more columns
                    // Populate cells with data
                    // Adjust the code according to your DTO structure
                    sumOfVoucherCount += Integer.valueOf(dto.getTotal_invoices());
                    sumOfTaxableAmt += Double.parseDouble(dto.getTaxable_amt());
                    sumOfIGSTAmt += Double.parseDouble(dto.getTotal_igst());
                    sumOfCGSTAmt += Double.parseDouble(dto.getTotal_cgst());
                    sumOfSGSTAmt += Double.parseDouble(dto.getTotal_sgst());
                    sumOfTaxAmt += Double.parseDouble(dto.getTotal_tax());
                    sumOfInvoiceAmt += Double.parseDouble(dto.getTotal_amt());

                }

                Row prow = sheet.createRow(rowNum++);
//                    for (int i = 0; i < headers.length; i++) {
                prow.createCell(0).setCellValue("Total");
                prow.createCell(3).setCellValue(sumOfVoucherCount);
                prow.createCell(4).setCellValue(sumOfTaxableAmt);
                prow.createCell(5).setCellValue(sumOfIGSTAmt);
                prow.createCell(6).setCellValue(sumOfCGSTAmt);
                prow.createCell(7).setCellValue(sumOfSGSTAmt);
//                prow.createCell(8).setCellValue("");
                prow.createCell(9).setCellValue(sumOfTaxAmt);
                prow.createCell(10).setCellValue(sumOfInvoiceAmt);

                // Write the workbook content to a file
                FileOutputStream fileOut = new FileOutputStream(file);
                workbook.write(fileOut);
                fileOut.close();
                workbook.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String[] getColumnHeaders() {
        ObservableList<TableColumn<GSTR1B2B1DTO, ?>> columns = tblvB2bScreen1List.getColumns();
        String[] headers = {"Sr.No", "Particulars", "GSTIN/UIN","Voucher count", "Taxable Amt", "Integrated Tax Amt", "Central Tax Amt", "State Tax Amt", "Cess Amt", "Tax Amt", "Invoice Amt"};
        for (int i = 0; i < headers.length; i++) {
            headers[i] = columns.get(i).getText();
        }
        return headers;
    }


    public void B2BresponsiveTable(){
        //todo: Responsive code for tableview
        tblcGstr1B2b1SerialNo.prefWidthProperty().bind(tblvB2bScreen1List.widthProperty().multiply(0.05));
        tblcGstr1B2b1Particulars.prefWidthProperty().bind(tblvB2bScreen1List.widthProperty().multiply(0.28));
        tblcGstr1B2b1Gstin.prefWidthProperty().bind(tblvB2bScreen1List.widthProperty().multiply(0.1));
        tblcGstr1B2b1VoucherCount.prefWidthProperty().bind(tblvB2bScreen1List.widthProperty().multiply(0.07));
        tblcGstr1B2b1TaxableAmt.prefWidthProperty().bind(tblvB2bScreen1List.widthProperty().multiply(0.1));
        tblcGstr1B2b1IgstAmt.prefWidthProperty().bind(tblvB2bScreen1List.widthProperty().multiply(0.08));
        tblcGstr1B2b1CgstAmt.prefWidthProperty().bind(tblvB2bScreen1List.widthProperty().multiply(0.08));
        tblcGstr1B2b1SgstAmt.prefWidthProperty().bind(tblvB2bScreen1List.widthProperty().multiply(0.08));
        tblcGstr1B2b1CessAmt.prefWidthProperty().bind(tblvB2bScreen1List.widthProperty().multiply(0.05));
        tblcGstr1B2b1TaxAmt.prefWidthProperty().bind(tblvB2bScreen1List.widthProperty().multiply(0.055));
        tblcGstr1B2b1InvoiceAmt.prefWidthProperty().bind(tblvB2bScreen1List.widthProperty().multiply(0.054));

    }

    //todo : node traversal method
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


    //    todo: fetch GSTR1 B2b1 Api data
    public void fetchGSTR1B2B1Data() {
        try {
            B2BScreen1Logger.info("Fetching GSTR1B2B1 Data");
            Map<String, String> body = new HashMap<>();
            if(!tfGstr1B2b1FromDt.getText().equalsIgnoreCase("")  && !tfGstr1B2b1ToDt.getText().equalsIgnoreCase("")){
                System.out.println("date sdf " + tfGstr1B2b1FromDt + "  to  " + tfGstr1B2b1ToDt );
                body.put("start_date", Communicator.text_to_date.fromString(tfGstr1B2b1FromDt.getText()).toString());
                body.put("end_date", Communicator.text_to_date.fromString(tfGstr1B2b1ToDt.getText()).toString());
            }
            body.put("searchText", "");
            Map<String, Object> sortObject = new HashMap<>();
            sortObject.put("colId", null);
            sortObject.put("isAsc", true);
            body.put("sort", sortObject.toString());
            String FormDataRequest = Globals.mapToStringforFormData(body);
            System.out.println("requestBody 123 " + FormDataRequest);

           APIClient apiClient = new APIClient(EndPoints.GET_GSTR1_B2B1_REPORT_API, FormDataRequest, RequestType.FORM_DATA);
            apiClient.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
                @Override
                public void handle(WorkerStateEvent workerStateEvent) {

                    String res = workerStateEvent.getSource().getValue().toString();
                    GSTR1B2B1MainData responseBody = new Gson().fromJson(res, GSTR1B2B1MainData.class);
                    System.out.println("SalesQuotation " + ""+responseBody);
                    if (responseBody.getResponseStatus() == 200) {
                        tblvB2bScreen1List.getItems().clear();
                        System.out.println("GSTR1B2B1 "+responseBody);
                        tblvB2bScreen1List.getItems().clear();
                        startDate = responseBody.getStartDate();
                        endDate = responseBody.getEndDate();
                        LocalDate startLocalDt = LocalDate.parse(startDate);
                        LocalDate endLocalDt = LocalDate.parse(endDate);
                        List<GSTR1B2B1ResponseData> list = responseBody.getData();
                        Integer count = 1;
                        if (list != null && list.size()>0) {
                            tblvB2bScreen1List.setVisible(true);
                            for (GSTR1B2B1ResponseData element : list) {

                                String ledger_id = String.valueOf(element.getLedgerId());
                                String ledger_name = element.getLedgerName();
                                String gst_no = element.getGstNumber();
                                String voucher_count =  element.getTotalInvoices() != null ? element.getTotalInvoices().toString():"";
                                String taxable_amt = element.getTaxableAmt() != null ? element.getTaxableAmt().toString():"";
                                String igst_amt = element.getTotalIgst() != null ? element.getTotalIgst().toString(): "";
                                String sgst_amt = element.getTotalSgst() != null ? element.getTotalSgst().toString(): "";
                                String cgst_amt = element.getTotalCgst() != null ? element.getTotalCgst().toString(): "";
                                String tax_amt = element.getTotalTax() != null ? element.getTotalTax().toString() :"";
                                String invoice_amt = element.getTotalAmt() != null ? element.getTotalAmt().toString():"";

                                GSTR1B2B1ObservableList.add(new GSTR1B2B1DTO(String.valueOf(count++), ledger_id,ledger_name,gst_no,voucher_count, taxable_amt, igst_amt, sgst_amt, cgst_amt, tax_amt,
                                        invoice_amt)
                                );
                            }
                            System.out.println("responseBody.getStartDate() " + responseBody.getStartDate());
                            System.out.println("responseBody.getEndDate() " + responseBody.getEndDate());
                            tfGstr1B2b1FromDt.setText(startLocalDt.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
                            tfGstr1B2b1ToDt.setText(endLocalDt.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
                            tblcGstr1B2b1SerialNo.setCellValueFactory(new PropertyValueFactory<>("sr_no"));
                            tblcGstr1B2b1Particulars.setCellValueFactory(new PropertyValueFactory<>("ledger_name"));
                            tblcGstr1B2b1Gstin.setCellValueFactory(new PropertyValueFactory<>("gst_number"));
                            tblcGstr1B2b1VoucherCount.setCellValueFactory(new PropertyValueFactory<>("total_invoices"));
                            tblcGstr1B2b1TaxableAmt.setCellValueFactory(new PropertyValueFactory<>("taxable_amt"));
                            tblcGstr1B2b1IgstAmt.setCellValueFactory(new PropertyValueFactory<>("total_igst"));
                            tblcGstr1B2b1CgstAmt.setCellValueFactory(new PropertyValueFactory<>("total_cgst"));
                            tblcGstr1B2b1SgstAmt.setCellValueFactory(new PropertyValueFactory<>("total_sgst"));
                            tblcGstr1B2b1TaxAmt.setCellValueFactory(new PropertyValueFactory<>("total_tax"));
                            tblcGstr1B2b1InvoiceAmt.setCellValueFactory(new PropertyValueFactory<>("total_amt"));

                            tblvB2bScreen1List.setItems(GSTR1B2B1ObservableList);

                            originalData = GSTR1B2B1ObservableList;
                            calculateTotalAmount();

                            B2BScreen1Logger.info("Success in Displaying GSTR1B2C Large SCREEN1");

                        } else {
                            B2BScreen1Logger.debug("GSTR1B2C Large SCREEN1 List ResponseObject is null ");
                            tblvB2bScreen1List.getItems().clear();
                            calculateTotalAmount();
                        }
                    }

                }
            });
            apiClient.setOnCancelled(new EventHandler<WorkerStateEvent>() {

                @Override
                public void handle(WorkerStateEvent workerStateEvent) {
                    System.out.println("Api Client set on cancelled is 1"); //B2BScreen2Logger.error
                    tblvB2bScreen1List.getItems().clear();
                    calculateTotalAmount();
                }
            });
            apiClient.setOnFailed(new EventHandler<WorkerStateEvent>() {
                @Override
                public void handle(WorkerStateEvent workerStateEvent) {
                    System.out.println("Api Client setOnFailed is 2"); //B2BScreen2Logger.error
                    tblvB2bScreen1List.getItems().clear();
                    calculateTotalAmount();
                }
            });
            apiClient.start();
        } catch (Exception e) {
            B2BScreen1Logger.error("Error in Fetching GSTR1B2B1Data is" + e.getMessage());
            e.printStackTrace();
        }
    }

    private void filterData(String keyword) {
        ObservableList<GSTR1B2B1DTO> filteredData = FXCollections.observableArrayList();

        B2BScreen1Logger.error("Search GSTR3B Outward Taxable Supplies in GSTR3BOutwardTaxableSuppliesController");
        if (keyword.isEmpty()) {
            tblvB2bScreen1List.setItems(originalData);
            return;
        }

        for (GSTR1B2B1DTO item : originalData) {
            if (matchesKeyword(item, keyword)) {
                filteredData.add(item);
            }
        }

        tblvB2bScreen1List.setItems(filteredData);
        calculateTotalAmount();
    }

    private boolean matchesKeyword(GSTR1B2B1DTO item, String keyword) {
        String lowerCaseKeyword = keyword.toLowerCase();
        return
                item.getGst_number().toLowerCase().contains(lowerCaseKeyword) ||
                        item.getLedgerName().toLowerCase().contains(lowerCaseKeyword) ||
                        item.getTotal_invoices().toLowerCase().contains(lowerCaseKeyword) ||
                        item.getTaxable_amt().toLowerCase().contains(lowerCaseKeyword) ||
                        item.getTotal_igst().toLowerCase().contains(lowerCaseKeyword) ||
                        item.getTotal_cgst().toLowerCase().contains(lowerCaseKeyword) ||
                        item.getTotal_sgst().toLowerCase().contains(lowerCaseKeyword) ||
                        item.getTotal_tax().toLowerCase().contains(lowerCaseKeyword) ||
                        item.getTotal_amt().toLowerCase().contains(lowerCaseKeyword);
    }

    private void calculateTotalAmount() {
        ObservableList<GSTR1B2B1DTO> filteredData = tblvB2bScreen1List.getItems();
        // Calculate the Totals
        double totalTaxable_amt= 0.0;
        double totalIgst_amt = 0.0;
        double totalCgst_amt = 0.0;
        double totalSgst_amt = 0.0;
        double totalTax_amt = 0.0;
        double totalInvoice_amt = 0.0;
        Long totalVoucherCount = 0L;
        for (GSTR1B2B1DTO item : filteredData) {
            totalTaxable_amt += Double.parseDouble(item.getTaxable_amt().isEmpty() ? "0.0" : item.getTaxable_amt());
            totalIgst_amt += Double.parseDouble(item.getTotal_igst().isEmpty() ? "0.0" : item.getTotal_igst());
            totalCgst_amt += Double.parseDouble(item.getTotal_cgst().isEmpty() ? "0.0" : item.getTotal_cgst());
            totalSgst_amt += Double.parseDouble(item.getTotal_sgst().isEmpty() ? "0.0" : item.getTotal_sgst());
            totalTax_amt += Double.parseDouble(item.getTotal_tax().isEmpty() ? "0.0" : item.getTotal_tax());
            totalInvoice_amt += Double.parseDouble(item.getTotal_amt().isEmpty() ? "0.0" : item.getTotal_amt());
            totalVoucherCount += Long.parseLong(item.getTotal_invoices().isEmpty() ? "0" : item.getTotal_invoices());

        }

        // Update Labels in the FXML
        lbGstr1B2B1TaxableAmt.setText(String.format("%.2f", totalTaxable_amt));
        lbGstr1B2B1TotalIgst.setText(String.format("%.2f", totalIgst_amt));
        lbGstr1B2B1TotalCgst.setText(String.format("%.2f", totalCgst_amt));
        lbGstr1B2B1TotalSgst.setText(String.format("%.2f", totalSgst_amt));
        lbGstr1B2B1TotalTax.setText(String.format("%.2f", totalTax_amt));
        lbGstr1B2B1TotalAmt.setText(String.format("%.2f", totalInvoice_amt));
        lbGstr1B2B1VoucherCount.setText(String.valueOf(totalVoucherCount));
    }
}
