package com.opethic.genivis.controller.GSTR1;

import com.google.gson.Gson;
import com.opethic.genivis.dto.GSTR1.GSTR1B2CLarge2DTO;
import com.opethic.genivis.dto.GSTR1.GSTR1B2CSmall2DTO;
import com.opethic.genivis.dto.GSTR1.GSTR1B2CLargeSR2MainData;
import com.opethic.genivis.dto.GSTR1.GSTR1B2CLargeSR2RespData;
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
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import static com.opethic.genivis.utils.FxmFileConstants.GSTR1_B2CLARGE_SCREEN1_LIST_SLUG;
import static com.opethic.genivis.utils.FxmFileConstants.GSTR1_B2CSMALL_SCREEN1_LIST_SLUG;

public class B2CSmallScreen2Controller implements Initializable {
    @FXML
    private ScrollPane spRootPaneB2CSmall2;
    ObservableList<GSTR1B2CSmall2DTO> GSTR1B2CSmall2ObservableList = FXCollections.observableArrayList();

    @FXML
    private TableView<GSTR1B2CSmall2DTO> tblvB2CSmall2List;

    @FXML
    private TableColumn tblcGstr1B2CSmall2SerialNo, tblcGstr1B2CSmall2Particulars, tblcGstr1B2CSmall2InvoiceNo, tblcGstr1B2CSmall2Dates, tblcGstr1B2CSmall2VoucherType, tblcGstr1B2CSmall2TaxableAmt, tblcGstr1B2CSmall2IgstAmt,
            tblcGstr1B2CSmall2CgstAmt, tblcGstr1B2CSmall2SgstAmt, tblcGstr1B2CSmall2CessAmt, tblcGstr1B2CSmall2TaxAmt, tblcGstr1B2CSmall2InvoiceAmt;

    @FXML
    private TextField tfGstr1B2CSmall2FromDt, tfGstr1B2CSmall2ToDt, tfGstr1B2CSmall2Search;
    @FXML
    private Label lbGstr1B2CS2Particulars, lbGstr1B2CS2InvoiceNo, lbGstr1B2CS2TaxableAmt, lbGstr1B2CS2TotalIgst, lbGstr1B2CS2TotalCgst,
            lbGstr1B2CS2TotalSgst, lbGstr1B2CS2TotalCessAmt, lbGstr1B2CS2TotalTax, lbGstr1B2CS2TotalAmt;
    public static final Logger B2CSmall2Logger = LoggerFactory.getLogger(B2CSmallScreen2Controller.class);
    private ObservableList<GSTR1B2CSmall2DTO> originalData;

    public static String startDate = "", endDate ="";
    @FXML
    private Button GSTR1B2CS2BtnPdf, GSTR1B2CS2BtnExcel, GSTR1B2CS2BtnCsv, GSTR1B2CS2BtnPrint;

    //props from Previous page
    public static String state_id ="", rate_of_tax = "";

    public void initialize(URL url, ResourceBundle resourceBundle) {
        spRootPaneB2CSmall2.sceneProperty().addListener((observable, oldScene, newScene) -> {
            if (newScene != null && newScene.getWindow() instanceof Stage) {
                Communicator.stage = (Stage) newScene.getWindow();
            }
        });


        Platform.runLater(() ->{
            tfGstr1B2CSmall2FromDt.requestFocus();
            //todo : Date  filter
            tfGstr1B2CSmall2ToDt.setOnKeyPressed(event ->{
                if(event.getCode() == KeyCode.ENTER){
                    System.out.println("tfGstr1B2CSmall2FromDt.getText() " + tfGstr1B2CSmall2FromDt.getText() + "tfGstr1B2CSmall2ToDt.getText()  " + tfGstr1B2CSmall2ToDt.getText());
                    fetchGSTR1B2CSmall2();
                    tfGstr1B2CSmall2Search.requestFocus();
                }

            });
            //todo:excel Export
            GSTR1B2CS2BtnExcel.setOnAction(event->{
                exportToExcel();
            });
        } );

        spRootPaneB2CSmall2.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ESCAPE) {
//                System.out.println("Esc Clicked....!");
                GlobalController.getInstance().addTabStatic(GSTR1_B2CSMALL_SCREEN1_LIST_SLUG, false);
            }
        });

        //todo:set date to fromDate and toDate
        System.out.println("startDate1 " + startDate + " endDate1 " + endDate );
        tfGstr1B2CSmall2FromDt.setText(startDate);
        tfGstr1B2CSmall2ToDt.setText(endDate);

        //todo: api call for GSTR1 B2C Large Screen1
        fetchGSTR1B2CSmall2();

        //todo: Responsive code for tableview
        B2CLarge2responsiveTable();



        DateValidator.applyDateFormat(tfGstr1B2CSmall2ToDt);
        DateValidator.applyDateFormat(tfGstr1B2CSmall2FromDt);

        // todo : search functionality
        tfGstr1B2CSmall2Search.textProperty().addListener((observable, oldValue, newValue) -> {
            filterData(newValue.trim());
        });

        //todo: Cursor movements
        GSTR1B2CS2BtnPdf.focusedProperty().addListener((obs, wasFocused, isNowFocused) -> {
            if (isNowFocused) {
                GSTR1B2CS2BtnPdf.setStyle("-fx-border-width: 2; -fx-border-color: #03dbfc");
            } else {
                GSTR1B2CS2BtnPdf.setStyle("");
            }
        });

        GSTR1B2CS2BtnPdf.hoverProperty().addListener((obs, wasHovered, isNowHovered) -> {
            if (isNowHovered) {
                GSTR1B2CS2BtnPdf.setStyle("-fx-border-width: 2; -fx-border-color: #03dbfc");
            } else {
                GSTR1B2CS2BtnPdf.setStyle("");
            }
        });

        GSTR1B2CS2BtnExcel.focusedProperty().addListener((obs, wasFocused, isNowFocused) -> {
            if (isNowFocused) {
                GSTR1B2CS2BtnExcel.setStyle("-fx-border-width: 2; -fx-border-color: #03dbfc");
            } else {
                GSTR1B2CS2BtnExcel.setStyle("");
            }
        });

        GSTR1B2CS2BtnExcel.hoverProperty().addListener((obs, wasHovered, isNowHovered) -> {
            if (isNowHovered) {
                GSTR1B2CS2BtnExcel.setStyle("-fx-border-width: 2; -fx-border-color: #03dbfc");
            } else {
                GSTR1B2CS2BtnExcel.setStyle("");
            }
        });

        GSTR1B2CS2BtnCsv.focusedProperty().addListener((obs, wasFocused, isNowFocused) -> {
            if (isNowFocused) {
                GSTR1B2CS2BtnCsv.setStyle("-fx-border-width: 2; -fx-border-color: #03dbfc");
            } else {
                GSTR1B2CS2BtnCsv.setStyle("");
            }
        });

        GSTR1B2CS2BtnCsv.hoverProperty().addListener((obs, wasHovered, isNowHovered) -> {
            if (isNowHovered) {
                GSTR1B2CS2BtnCsv.setStyle("-fx-border-width: 2; -fx-border-color: #03dbfc");
            } else {
                GSTR1B2CS2BtnCsv.setStyle("");
            }
        });

        GSTR1B2CS2BtnPrint.focusedProperty().addListener((obs, wasFocused, isNowFocused) -> {
            if (isNowFocused) {
                GSTR1B2CS2BtnPrint.setStyle("-fx-border-width: 2; -fx-border-color: #03dbfc");
            } else {
                GSTR1B2CS2BtnPrint.setStyle("");
            }
        });

        GSTR1B2CS2BtnPrint.hoverProperty().addListener((obs, wasHovered, isNowHovered) -> {
            if (isNowHovered) {
                GSTR1B2CS2BtnPrint.setStyle("-fx-border-width: 2; -fx-border-color: #03dbfc");
            } else {
                GSTR1B2CS2BtnPrint.setStyle("");
            }
        });

        nodetraversal(tfGstr1B2CSmall2FromDt, tfGstr1B2CSmall2ToDt);
        nodetraversal(tfGstr1B2CSmall2ToDt, tfGstr1B2CSmall2Search);
        nodetraversal(tfGstr1B2CSmall2Search, GSTR1B2CS2BtnPdf);
        nodetraversal(GSTR1B2CS2BtnPdf, GSTR1B2CS2BtnExcel);
        nodetraversal(GSTR1B2CS2BtnExcel, GSTR1B2CS2BtnCsv);
        nodetraversal(GSTR1B2CS2BtnCsv,GSTR1B2CS2BtnPrint);
        tblvB2CSmall2List.requestFocus();
    }

    //excelExport code
    private void exportToExcel() {
        ObservableList<GSTR1B2CSmall2DTO> data = tblvB2CSmall2List.getItems();

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
                for (GSTR1B2CSmall2DTO dto : data) {
                    Row row = sheet.createRow(rowNum++);
                    row.createCell(0).setCellValue(dto.getSr_no());
                    row.createCell(1).setCellValue(dto.getInvoice_date());
                    row.createCell(2).setCellValue(dto.getInvoice_no());
                    row.createCell(3).setCellValue(dto.getSundry_debtor_name());
                    row.createCell(4).setCellValue(dto.getSale_account_name());
                    row.createCell(5).setCellValue(dto.getTaxable_amt());
                    row.createCell(6).setCellValue(dto.getTotaligst());
                    row.createCell(7).setCellValue(dto.getTotalcgst());
                    row.createCell(8).setCellValue(dto.getTotalsgst());
                    row.createCell(10).setCellValue(dto.getTax_amt());
                    row.createCell(11).setCellValue(dto.getTotal_amount());
//            // Add more cells if you have more columns
                    // Populate cells with data
                    // Adjust the code according to your DTO structure
                    sumOfTaxableAmt += Double.parseDouble(dto.getTaxable_amt());
                    sumOfIGSTAmt += Double.parseDouble(dto.getTotaligst());
                    sumOfCGSTAmt += Double.parseDouble(dto.getTotalcgst());
                    sumOfSGSTAmt += Double.parseDouble(dto.getTotalsgst());
                    sumOfTaxAmt += Double.parseDouble(dto.getTax_amt());
                    sumOfInvoiceAmt += Double.parseDouble(dto.getTotal_amount());

                }

                Row prow = sheet.createRow(rowNum++);
//                    for (int i = 0; i < headers.length; i++) {
                prow.createCell(0).setCellValue("Total");
                prow.createCell(5).setCellValue(sumOfTaxableAmt);
                prow.createCell(6).setCellValue(sumOfIGSTAmt);
                prow.createCell(7).setCellValue(sumOfCGSTAmt);
                prow.createCell(8).setCellValue(sumOfSGSTAmt);
//                prow.createCell(8).setCellValue("");
                prow.createCell(10).setCellValue(sumOfTaxAmt);
                prow.createCell(11).setCellValue(sumOfInvoiceAmt);

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
        ObservableList<TableColumn<GSTR1B2CSmall2DTO, ?>> columns = tblvB2CSmall2List.getColumns();
        String[] headers = {"Sr.No","Dates", "Invoice No.", "Particulars","Voucher Type", "Taxable Amt", "Integrated Tax Amt", "Central Tax Amt", "State Tax Amt", "Cess Amt", "Tax Amt", "Invoice Amt"};
        for (int i = 0; i < headers.length; i++) {
            headers[i] = columns.get(i).getText();
        }
        return headers;
    }



    public void B2CLarge2responsiveTable(){
        //todo: Responsive code for tableview
        tblcGstr1B2CSmall2SerialNo.prefWidthProperty().bind(tblvB2CSmall2List.widthProperty().multiply(0.05));
        tblcGstr1B2CSmall2Dates.prefWidthProperty().bind(tblvB2CSmall2List.widthProperty().multiply(0.07));
        tblcGstr1B2CSmall2InvoiceNo.prefWidthProperty().bind(tblvB2CSmall2List.widthProperty().multiply(0.08));
        tblcGstr1B2CSmall2Particulars.prefWidthProperty().bind(tblvB2CSmall2List.widthProperty().multiply(0.2));
        tblcGstr1B2CSmall2VoucherType.prefWidthProperty().bind(tblvB2CSmall2List.widthProperty().multiply(0.06));
        tblcGstr1B2CSmall2TaxableAmt.prefWidthProperty().bind(tblvB2CSmall2List.widthProperty().multiply(0.06));
        tblcGstr1B2CSmall2IgstAmt.prefWidthProperty().bind(tblvB2CSmall2List.widthProperty().multiply(0.076));
        tblcGstr1B2CSmall2CgstAmt.prefWidthProperty().bind(tblvB2CSmall2List.widthProperty().multiply(0.076));
        tblcGstr1B2CSmall2SgstAmt.prefWidthProperty().bind(tblvB2CSmall2List.widthProperty().multiply(0.076));
        tblcGstr1B2CSmall2CessAmt.prefWidthProperty().bind(tblvB2CSmall2List.widthProperty().multiply(0.05));
        tblcGstr1B2CSmall2TaxAmt.prefWidthProperty().bind(tblvB2CSmall2List.widthProperty().multiply(0.1));
        tblcGstr1B2CSmall2InvoiceAmt.prefWidthProperty().bind(tblvB2CSmall2List.widthProperty().multiply(0.1));

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

    public void fetchGSTR1B2CSmall2() {
        try{
            APIClient apiClient = null;

            Map<String, String> body = new HashMap<>();
            Map<String, Object> sortObject = new HashMap<>();
            sortObject.put("colId", null);
            sortObject.put("isAsc", true);
            body.put("sort", sortObject.toString());
            body.put("searchText", "");
            body.put("state_id", state_id);
            body.put("tax_rate", rate_of_tax);

            System.out.println("date sdf " + tfGstr1B2CSmall2FromDt.getText() + "  to  " + tfGstr1B2CSmall2ToDt.getText() );
            if(!tfGstr1B2CSmall2FromDt.getText().equalsIgnoreCase("")  || !tfGstr1B2CSmall2ToDt.getText().equalsIgnoreCase("")) {
                body.put("startDate", Communicator.text_to_date.fromString(tfGstr1B2CSmall2FromDt.getText()).toString());
                body.put("endDate", Communicator.text_to_date.fromString(tfGstr1B2CSmall2ToDt.getText()).toString());
            }
            else{
                System.out.println("startDate " + startDate + " endDate  " + endDate );
                body.put("startDate", Communicator.text_to_date.fromString(startDate).toString());
                body.put("endDate", Communicator.text_to_date.fromString(endDate).toString());
            }
            String JsonReq = Globals.mapToString(body);
            System.out.println("JsonReq " + JsonReq);

            apiClient = new APIClient(EndPoints.GET_GSTR1_B2CS2_DATA_API, JsonReq, RequestType.JSON);
            apiClient.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
                @Override
                public void handle(WorkerStateEvent workerStateEvent) {
                    String res = workerStateEvent.getSource().getValue().toString();
                    GSTR1B2CLargeSR2MainData responseBody = new Gson().fromJson(res, GSTR1B2CLargeSR2MainData.class);
                    System.out.println("responseBody of GSTR1B2CS2 " + ""+responseBody);
                    if (responseBody.getResponseStatus() == 200) {
                        System.out.println("GSTR1B2CS2 "+responseBody);
                        tblvB2CSmall2List.getItems().clear();
//                        LocalDate startLocalDt = LocalDate.parse(startDate);
//                        LocalDate endLocalDt = LocalDate.parse(endDate);
                        List<GSTR1B2CLargeSR2RespData> list = responseBody.getResponseObject();
                        if (list != null && list.size()>0) {
//                            tblvB2CLarge2List.setVisible(true);
                            int count = 1;
                            for (GSTR1B2CLargeSR2RespData element : list) {

                                System.out.println("response dataof GSTR1B2C Small SCREEN1"+element);
                                String id = element.getId().toString();
                                String dates = element.getInvoiceDate();
                                String invoice_no = element.getInvoiceNo();
                                String sales_serial_no = element.getSaleSerialNumber().toString();
                                String sundry_debtor_name = element.getSundryDebtorName().toString();
                                String sundry_debtor_id = element.getSundryDebtorId().toString();
                                String voucher_type = element.getSaleAccountName().toString();
                                String taxable_amt = element.getTaxableAmt().toString();
                                String igst_amt = element.getTotaligst() != null ? element.getTotaligst().toString(): "";
                                String sgst_amt = element.getTotalsgst() != null ? element.getTotalsgst().toString(): "";
                                String cgst_amt = element.getTotalcgst() != null ? element.getTotalcgst().toString(): "";
                                String tax_amt = element.getTaxAmt() != null ? element.getTaxAmt().toString() :"";
                                String invoice_amt = element.getTotalAmount().toString();
                                String narration = element.getNarration();

                                GSTR1B2CSmall2ObservableList.add(new GSTR1B2CSmall2DTO( String.valueOf(count++), id,invoice_no,dates,sales_serial_no,invoice_amt,sundry_debtor_name,
                                        sundry_debtor_id,voucher_type,tax_amt,narration,taxable_amt,igst_amt,cgst_amt, sgst_amt)
                                );
                            }

                            tblcGstr1B2CSmall2SerialNo.setCellValueFactory(new PropertyValueFactory<>("sr_no"));
                            tblcGstr1B2CSmall2Particulars.setCellValueFactory(new PropertyValueFactory<>("sundry_debtor_name"));
                            tblcGstr1B2CSmall2InvoiceNo.setCellValueFactory(new PropertyValueFactory<>("invoice_no"));
                            tblcGstr1B2CSmall2VoucherType.setCellValueFactory(new PropertyValueFactory<>("sale_account_name"));
                            tblcGstr1B2CSmall2Dates.setCellValueFactory(new PropertyValueFactory<>("invoice_date"));
                            tblcGstr1B2CSmall2TaxableAmt.setCellValueFactory(new PropertyValueFactory<>("taxable_amt"));
                            tblcGstr1B2CSmall2IgstAmt.setCellValueFactory(new PropertyValueFactory<>("totaligst"));
                            tblcGstr1B2CSmall2CgstAmt.setCellValueFactory(new PropertyValueFactory<>("totalcgst"));
                            tblcGstr1B2CSmall2SgstAmt.setCellValueFactory(new PropertyValueFactory<>("totalsgst"));
                            tblcGstr1B2CSmall2TaxAmt.setCellValueFactory(new PropertyValueFactory<>("tax_amt"));
                            tblcGstr1B2CSmall2InvoiceAmt.setCellValueFactory(new PropertyValueFactory<>("total_amount"));

                            tblvB2CSmall2List.setItems(GSTR1B2CSmall2ObservableList);

                            originalData = GSTR1B2CSmall2ObservableList;
                            calculateTotalAmount();

                            B2CSmall2Logger.info("Success in Displaying GSTR1B2C Small SCREEN2");

                        } else {
                            B2CSmall2Logger.debug("GSTR1B2C Small SCREEN2 List ResponseObject is null ");
                            tblvB2CSmall2List.getItems().clear();
                            calculateTotalAmount();
                        }
                    }

                }
            });
            apiClient.setOnCancelled(new EventHandler<WorkerStateEvent>() {

                @Override
                public void handle(WorkerStateEvent workerStateEvent) {
                    System.out.println("Api Client set on cancelled is 1"); //B2BScreen2Logger.error
                    tblvB2CSmall2List.getItems().clear();
                    calculateTotalAmount();
                }
            });
            apiClient.setOnFailed(new EventHandler<WorkerStateEvent>() {
                @Override
                public void handle(WorkerStateEvent workerStateEvent) {
                    System.out.println("Api Client setOnFailed is 2"); //B2BScreen2Logger.error
                    tblvB2CSmall2List.getItems().clear();
                    calculateTotalAmount();
                }
            });
            apiClient.start();

        } catch (Exception e) {
            B2CSmall2Logger.error("GSTR1B2C Small SCREEN2 List Error is " + e.getMessage());
            System.out.println("Api Client catch error 2");
            tblvB2CSmall2List.getItems().clear();
            calculateTotalAmount();
            e.printStackTrace();
        }

    }


    private void filterData(String keyword) {
        ObservableList<GSTR1B2CSmall2DTO> filteredData = FXCollections.observableArrayList();

        B2CSmall2Logger.error("Search GSTR1 B2CSmall2 data in GSTR1B2CSmall screen2Controller");
        if (keyword.isEmpty()) {
            tblvB2CSmall2List.setItems(originalData);
            return;
        }

        for (GSTR1B2CSmall2DTO item : originalData) {
            if (matchesKeyword(item, keyword)) {
                filteredData.add(item);
            }
        }

        tblvB2CSmall2List.setItems(filteredData);
        calculateTotalAmount();
    }

    private boolean matchesKeyword(GSTR1B2CSmall2DTO item, String keyword) {
        String lowerCaseKeyword = keyword.toLowerCase();
        return
                item.getInvoice_date().toLowerCase().contains(lowerCaseKeyword) ||
                        item.getInvoice_no().toLowerCase().contains(lowerCaseKeyword) ||
                        item.getSale_account_name().toLowerCase().contains(lowerCaseKeyword) ||
                        item.getTaxable_amt().toLowerCase().contains(lowerCaseKeyword) ||
                        item.getSundry_debtor_name().toLowerCase().contains(lowerCaseKeyword) ||
                        item.getTax_amt().toLowerCase().contains(lowerCaseKeyword) ||
                        item.getTotal_amount().toLowerCase().contains(lowerCaseKeyword) ||
                        item.getTotalcgst().toLowerCase().contains(lowerCaseKeyword) ||
                        item.getTotaligst().toLowerCase().contains(lowerCaseKeyword)||
                        item.getTotalsgst().toLowerCase().contains(lowerCaseKeyword);
    }

    private void calculateTotalAmount() {
        ObservableList<GSTR1B2CSmall2DTO> filteredData = tblvB2CSmall2List.getItems();
        // Calculate the Totals
        double totalTaxable_amt= 0.0;
        double totalIgst_amt = 0.0;
        double totalCgst_amt = 0.0;
        double totalSgst_amt = 0.0;
        double totalTax_amt = 0.0;
        double totalInvoice_amt = 0.0;
        for (GSTR1B2CSmall2DTO item : filteredData) {
            totalTaxable_amt += Double.parseDouble(item.getTaxable_amt().isEmpty() ? "0.0" : item.getTaxable_amt());
            totalIgst_amt += Double.parseDouble(item.getTotaligst().isEmpty() ? "0.0" : item.getTotaligst());
            totalCgst_amt += Double.parseDouble(item.getTotalcgst().isEmpty() ? "0.0" : item.getTotalcgst());
            totalSgst_amt += Double.parseDouble(item.getTotalsgst().isEmpty() ? "0.0" : item.getTotalsgst());
            totalTax_amt += Double.parseDouble(item.getTax_amt().isEmpty() ? "0.0" : item.getTax_amt());

//            totalTax_amt += Double.parseDouble(!item.getTax_amt().isEmpty() && item.getTax_amt()!=null ? item.getTax_amt() :"0.0");
            totalInvoice_amt += Double.parseDouble(item.getTotal_amount().isEmpty() ? "0.0" : item.getTotal_amount());
        }

        // Update Labels in the FXML
        lbGstr1B2CS2TaxableAmt.setText(String.format("%.2f", totalTaxable_amt));
        lbGstr1B2CS2TotalIgst.setText(String.format("%.2f", totalIgst_amt));
        lbGstr1B2CS2TotalCgst.setText(String.format("%.2f", totalCgst_amt));
        lbGstr1B2CS2TotalSgst.setText(String.format("%.2f", totalSgst_amt));
        lbGstr1B2CS2TotalTax.setText(String.format("%.2f", totalTax_amt));
        lbGstr1B2CS2TotalAmt.setText(String.format("%.2f", totalInvoice_amt));
    }
}
