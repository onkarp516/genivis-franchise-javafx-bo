package com.opethic.genivis.controller.GSTR1;

import com.google.gson.Gson;
import com.opethic.genivis.controller.tranx_sales.SalesQuotationCreateController;
import com.opethic.genivis.dto.GSTR1.*;
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
import javafx.scene.input.KeyEvent;
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
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import static com.opethic.genivis.utils.FxmFileConstants.*;

public class B2CLargeScreen1Controller implements Initializable {

    @FXML
    private ScrollPane spRootPaneB2CLarge1;
    ObservableList<GSTR1B2CLarge1DTO> GSTR1B2CLarge1ObservableList = FXCollections.observableArrayList();

    @FXML
    private TableView<GSTR1B2CLarge1DTO> tblvB2CLarge1List;
    @FXML
    private Button GSTR1B2CL1BtnPdf, GSTR11B2CL1BtnExcel, GSTR11B2CL1BtnCsv, GSTR11B2CL1BtnPrint;

    @FXML
    private TableColumn tblcGstr1B2CLarge1SerialNo, tblcGstr1B2CLarge1Particulars, tblcGstr1B2CLarge1RateOfTax, tblcGstr1B2CLarge1TaxableAmt, tblcGstr1B2CLarge1IgstAmt,
            tblcGstr1B2CLarge1CgstAmt, tblcGstr1B2CLarge1SgstAmt, tblcGstr1B2CLarge1CessAmt, tblcGstr1B2CLarge1TaxAmt, tblcGstr1B2CLarge1InvoiceAmt;

    @FXML
    private TextField tfGstr1B2CLarge1FromDt, tfGstr1B2CLarge1ToDt, tfGstr1B2CLarge1Search;
    @FXML
    private Label lbGstr1B2CL1Particulars, lbGstr1B2CL1RateOfTax, lbGstr1B2CL1TaxableAmt, lbGstr1B2CL1TotalIgst, lbGstr1B2CL1TotalCgst,
            lbGstr1B2CL1TotalSgst, lbGstr1B2CL1TotalCessAmt, lbGstr1B2CL1TotalTax, lbGstr1B2CL1TotalAmt;
    public static final Logger B2CLarge1Logger = LoggerFactory.getLogger(B2BScreen1Controller.class);
    private ObservableList<GSTR1B2CLarge1DTO> originalData;

    String startDate = "", endDate ="";
    public void initialize(URL url, ResourceBundle resourceBundle) {
        spRootPaneB2CLarge1.sceneProperty().addListener((observable, oldScene, newScene) -> {
            if (newScene != null && newScene.getWindow() instanceof Stage) {
                Communicator.stage = (Stage) newScene.getWindow();
            }
        });

        Platform.runLater(() ->{
                    tfGstr1B2CLarge1FromDt.requestFocus();
            //todo : Date  filter
            tfGstr1B2CLarge1ToDt.setOnKeyPressed(event ->{
                if(event.getCode() == KeyCode.ENTER){
                    System.out.println("tfGstr1B2CLarge1FromDt.getText() " + tfGstr1B2CLarge1FromDt.getText() + "tfGstr1B2CLarge1ToDt.getText()  " + tfGstr1B2CLarge1ToDt.getText());
                    fetchGSTR1B2CLarge();
                    tfGstr1B2CLarge1Search.requestFocus();
                }

            });

            //todo:excel Export
            GSTR11B2CL1BtnExcel.setOnAction(event->{
                exportToExcel();

            });

                //todo:go to next B2CLargeScreen2 on Enter

//                   spRootPaneB2CLarge1.addEventFilter(KeyEvent.KEY_PRESSED, (KeyEvent event) -> {
//                        if (event.getCode() == KeyCode.ENTER) {
//
//                    if (tblvB2CLarge1List.isFocused()) {
//                        GSTR1B2CLarge1DTO selectedItem = (GSTR1B2CLarge1DTO) tblvB2CLarge1List.getSelectionModel().getSelectedItem();
//                        String state_id = selectedItem.getState_id();
//                        String rate_of_tax = selectedItem.getRate_of_tax();
//                        System.out.println("state_id 1 -->  " + state_id + "  rate_of_tax  " + rate_of_tax);
//                        B2CLargeScreen2Controller.state_id = state_id;
//                        B2CLargeScreen2Controller.rate_of_tax = rate_of_tax;
//                        if(tfGstr1B2CLarge1FromDt.getText()!="" || tfGstr1B2CLarge1FromDt.getText()!="") {
//                            B2CLargeScreen2Controller.startDate = tfGstr1B2CLarge1FromDt.getText();
//                            B2CLargeScreen2Controller.endDate = tfGstr1B2CLarge1ToDt.getText();
//                        }
//                        GlobalController.getInstance().addTabStatic(GSTR1_B2CLARGE_SCREEN2_LIST_SLUG,false);
//                    }
//                }
//            });
        });

        spRootPaneB2CLarge1.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ESCAPE) {
//                System.out.println("Esc Clicked....!");
                GlobalController.getInstance().addTabStatic(GSTR1_DASHBOARD_LIST_SLUG, false);
            }
        });

        //todo: api call for GSTR1 B2C Large Screen1
        fetchGSTR1B2CLarge();

        //todo: Responsive code for tableview
        B2CLarge1responsiveTable();


        DateValidator.applyDateFormat(tfGstr1B2CLarge1FromDt);
        DateValidator.applyDateFormat(tfGstr1B2CLarge1ToDt);

        // todo : search functionality
        tfGstr1B2CLarge1Search.textProperty().addListener((observable, oldValue, newValue) -> {
            filterData(newValue.trim());
        });


        tblvB2CLarge1List.setOnMouseClicked(event->{
            if(event.getClickCount()==2){
                GSTR1B2CLarge1DTO selectedItem = (GSTR1B2CLarge1DTO) tblvB2CLarge1List.getSelectionModel().getSelectedItem();
                String state_id = selectedItem.getState_id();
                String rate_of_tax = selectedItem.getRate_of_tax();
                System.out.println("state_id 1 -->  " + state_id + "  rate_of_tax  " + rate_of_tax);
                B2CLargeScreen2Controller.state_id = state_id;
                B2CLargeScreen2Controller.rate_of_tax = rate_of_tax;
                if(tfGstr1B2CLarge1FromDt.getText()!="" || tfGstr1B2CLarge1FromDt.getText()!="") {
                    B2CLargeScreen2Controller.startDate = tfGstr1B2CLarge1FromDt.getText();
                    B2CLargeScreen2Controller.endDate = tfGstr1B2CLarge1ToDt.getText();
                }
                GlobalController.getInstance().addTabStatic(GSTR1_B2CLARGE_SCREEN2_LIST_SLUG,false);
            }
        });

        GSTR1B2CL1BtnPdf.focusedProperty().addListener((obs, wasFocused, isNowFocused) -> {
            if (isNowFocused) {
                GSTR1B2CL1BtnPdf.setStyle("-fx-border-width: 2; -fx-border-color: #03dbfc");
            } else {
                GSTR1B2CL1BtnPdf.setStyle("");
            }
        });

        GSTR1B2CL1BtnPdf.hoverProperty().addListener((obs, wasHovered, isNowHovered) -> {
            if (isNowHovered) {
                GSTR1B2CL1BtnPdf.setStyle("-fx-border-width: 2; -fx-border-color: #03dbfc");
            } else {
                GSTR1B2CL1BtnPdf.setStyle("");
            }
        });

        GSTR11B2CL1BtnExcel.focusedProperty().addListener((obs, wasFocused, isNowFocused) -> {
            if (isNowFocused) {
                GSTR11B2CL1BtnExcel.setStyle("-fx-border-width: 2; -fx-border-color: #03dbfc");
            } else {
                GSTR11B2CL1BtnExcel.setStyle("");
            }
        });

        GSTR11B2CL1BtnExcel.hoverProperty().addListener((obs, wasHovered, isNowHovered) -> {
            if (isNowHovered) {
                GSTR11B2CL1BtnExcel.setStyle("-fx-border-width: 2; -fx-border-color: #03dbfc");
            } else {
                GSTR11B2CL1BtnExcel.setStyle("");
            }
        });

        GSTR11B2CL1BtnCsv.focusedProperty().addListener((obs, wasFocused, isNowFocused) -> {
            if (isNowFocused) {
                GSTR11B2CL1BtnCsv.setStyle("-fx-border-width: 2; -fx-border-color: #03dbfc");
            } else {
                GSTR11B2CL1BtnCsv.setStyle("");
            }
        });

        GSTR11B2CL1BtnCsv.hoverProperty().addListener((obs, wasHovered, isNowHovered) -> {
            if (isNowHovered) {
                GSTR11B2CL1BtnCsv.setStyle("-fx-border-width: 2; -fx-border-color: #03dbfc");
            } else {
                GSTR11B2CL1BtnCsv.setStyle("");
            }
        });

        GSTR11B2CL1BtnPrint.focusedProperty().addListener((obs, wasFocused, isNowFocused) -> {
            if (isNowFocused) {
                GSTR11B2CL1BtnPrint.setStyle("-fx-border-width: 2; -fx-border-color: #03dbfc");
            } else {
                GSTR11B2CL1BtnPrint.setStyle("");
            }
        });

        GSTR11B2CL1BtnPrint.hoverProperty().addListener((obs, wasHovered, isNowHovered) -> {
            if (isNowHovered) {
                GSTR11B2CL1BtnPrint.setStyle("-fx-border-width: 2; -fx-border-color: #03dbfc");
            } else {
                GSTR11B2CL1BtnPrint.setStyle("");
            }
        });

        nodetraversal(tfGstr1B2CLarge1FromDt, tfGstr1B2CLarge1ToDt);
        nodetraversal(tfGstr1B2CLarge1ToDt, tfGstr1B2CLarge1Search);
        nodetraversal(tfGstr1B2CLarge1Search, GSTR1B2CL1BtnPdf);
        nodetraversal(GSTR1B2CL1BtnPdf, GSTR11B2CL1BtnExcel);
        nodetraversal(GSTR11B2CL1BtnExcel, GSTR11B2CL1BtnCsv);
        nodetraversal(GSTR11B2CL1BtnCsv,GSTR11B2CL1BtnPrint);
        tblvB2CLarge1List.requestFocus();

    }

    //excelExport code
    private void exportToExcel() {
        ObservableList<GSTR1B2CLarge1DTO> data = tblvB2CLarge1List.getItems();

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
                for (GSTR1B2CLarge1DTO dto : data) {
                    Row row = sheet.createRow(rowNum++);
                    row.createCell(0).setCellValue(dto.getSr_no());
                    row.createCell(1).setCellValue(dto.getState_name());
                    row.createCell(2).setCellValue(dto.getRate_of_tax());
                    row.createCell(3).setCellValue(dto.getTaxable_amt());
                    row.createCell(4).setCellValue(dto.getIgst_amt());
                    row.createCell(5).setCellValue(dto.getCgst_amt());
                    row.createCell(6).setCellValue(dto.getSgst_amt());
//                    row.createCell(7).setCellValue(dto.getTotal_sgst());
                    row.createCell(8).setCellValue(dto.getTax_amt());
                    row.createCell(9).setCellValue(dto.getInvoice_amt());
//            // Add more cells if you have more columns
                    // Populate cells with data
                    // Adjust the code according to your DTO structure
                    sumOfTaxableAmt += Double.parseDouble(dto.getTaxable_amt());
                    sumOfIGSTAmt += Double.parseDouble(dto.getIgst_amt());
                    sumOfCGSTAmt += Double.parseDouble(dto.getCgst_amt());
                    sumOfSGSTAmt += Double.parseDouble(dto.getSgst_amt());
                    sumOfTaxAmt += Double.parseDouble(dto.getTax_amt());
                    sumOfInvoiceAmt += Double.parseDouble(dto.getInvoice_amt());

                }

                Row prow = sheet.createRow(rowNum++);
//                    for (int i = 0; i < headers.length; i++) {
                prow.createCell(0).setCellValue("Total");
                prow.createCell(3).setCellValue(sumOfTaxableAmt);
                prow.createCell(4).setCellValue(sumOfIGSTAmt);
                prow.createCell(5).setCellValue(sumOfCGSTAmt);
                prow.createCell(6).setCellValue(sumOfSGSTAmt);
//                prow.createCell(8).setCellValue("");
                prow.createCell(8).setCellValue(sumOfTaxAmt);
                prow.createCell(9).setCellValue(sumOfInvoiceAmt);

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
        ObservableList<TableColumn<GSTR1B2CLarge1DTO, ?>> columns = tblvB2CLarge1List.getColumns();
        String[] headers = {"Sr.No", "Particulars", "Rate Of Tax", "Taxable Amt", "Integrated Tax Amt", "Central Tax Amt", "State Tax Amt", "Cess Amt", "Tax Amt", "Invoice Amt"};
        for (int i = 0; i < headers.length; i++) {
            headers[i] = columns.get(i).getText();
        }
        return headers;
    }

    public void B2CLarge1responsiveTable(){
        //todo: Responsive code for tableview
        tblcGstr1B2CLarge1SerialNo.prefWidthProperty().bind(tblvB2CLarge1List.widthProperty().multiply(0.05));
        tblcGstr1B2CLarge1Particulars.prefWidthProperty().bind(tblvB2CLarge1List.widthProperty().multiply(0.2));
        tblcGstr1B2CLarge1RateOfTax.prefWidthProperty().bind(tblvB2CLarge1List.widthProperty().multiply(0.08));
        tblcGstr1B2CLarge1TaxableAmt.prefWidthProperty().bind(tblvB2CLarge1List.widthProperty().multiply(0.12));
        tblcGstr1B2CLarge1IgstAmt.prefWidthProperty().bind(tblvB2CLarge1List.widthProperty().multiply(0.1));
        tblcGstr1B2CLarge1CgstAmt.prefWidthProperty().bind(tblvB2CLarge1List.widthProperty().multiply(0.1));
        tblcGstr1B2CLarge1SgstAmt.prefWidthProperty().bind(tblvB2CLarge1List.widthProperty().multiply(0.1));
        tblcGstr1B2CLarge1CessAmt.prefWidthProperty().bind(tblvB2CLarge1List.widthProperty().multiply(0.1));
        tblcGstr1B2CLarge1TaxAmt.prefWidthProperty().bind(tblvB2CLarge1List.widthProperty().multiply(0.07));
        tblcGstr1B2CLarge1InvoiceAmt.prefWidthProperty().bind(tblvB2CLarge1List.widthProperty().multiply(0.08));

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


    public void fetchGSTR1B2CLarge() {
        try{
            APIClient apiClient = null;

            Map<String, String> body = new HashMap<>();
            Map<String, Object> sortObject = new HashMap<>();
            sortObject.put("colId", null);
            sortObject.put("isAsc", true);
            body.put("sort", sortObject.toString());


            System.out.println("date sdf " + tfGstr1B2CLarge1FromDt + "  to  " + tfGstr1B2CLarge1ToDt );

            System.out.println("date sdf " + tfGstr1B2CLarge1FromDt.getText() + "  to  " + tfGstr1B2CLarge1ToDt.getText() );
            if(!tfGstr1B2CLarge1FromDt.getText().equalsIgnoreCase("")  || !tfGstr1B2CLarge1ToDt.getText().equalsIgnoreCase("")) {
                body.put("start_date", Communicator.text_to_date.fromString(tfGstr1B2CLarge1FromDt.getText()).toString());
                body.put("end_date", Communicator.text_to_date.fromString(tfGstr1B2CLarge1ToDt.getText()).toString());
            }
            String formDataReq = Globals.mapToStringforFormData(body);
            System.out.println("formDataReq " + formDataReq);

            apiClient = new APIClient(EndPoints.GET_GSTR1_B2CL1_DATA_API, formDataReq, RequestType.FORM_DATA);
            apiClient.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
                @Override
                public void handle(WorkerStateEvent workerStateEvent) {
                    String res = workerStateEvent.getSource().getValue().toString();
                    GSTR1B2CLargeSR1MainData responseBody = new Gson().fromJson(res, GSTR1B2CLargeSR1MainData.class);
                    System.out.println("SalesQuotation " + ""+responseBody);
                    if (responseBody.getResponseStatus() == 200) {
                        System.out.println("GSTR1B2B2 "+responseBody);
                        tblvB2CLarge1List.getItems().clear();
                        startDate = responseBody.getStartDate();
                        endDate = responseBody.getEndDate();
                        LocalDate startLocalDt = LocalDate.parse(startDate);
                        LocalDate endLocalDt = LocalDate.parse(endDate);
                        List<GSTR1B2CLargeSR1RespData> list = responseBody.getData();
                        int count = 1;
                        if (list != null && list.size()>0) {
                            tblvB2CLarge1List.setVisible(true);
                            for (GSTR1B2CLargeSR1RespData element : list) {
                                String state_name = element.getStateName();
                                String state_id = element.getStateId();
                                String state_code = element.getStateCode();
                                String rate_of_tax = element.getRateOfTax().toString();
                                String taxable_amt = element.getTaxableAmt().toString();
                                String igst_amt = element.getIgstAmt() != null ? element.getIgstAmt().toString(): "";
                                String sgst_amt = element.getSgstAmt() != null ? element.getSgstAmt().toString(): "";
                                String cgst_amt = element.getCgstAmt() != null ? element.getCgstAmt().toString(): "";
                                String tax_amt = element.getTaxAmt() != null ? element.getTaxAmt().toString() :"";
                                String invoice_amt = element.getInvoiceAmt().toString();
                                System.out.println("response State_id "+state_id);

                                GSTR1B2CLarge1ObservableList.add(new GSTR1B2CLarge1DTO(String.valueOf(count++),state_name,state_id, state_code, rate_of_tax, taxable_amt, igst_amt, sgst_amt, cgst_amt, tax_amt,
                                        invoice_amt)
                                );
                            }
                            System.out.println("responseBody.getStartDate() " + responseBody.getStartDate());
                            System.out.println("responseBody.getEndDate() " + responseBody.getEndDate());
                            tfGstr1B2CLarge1FromDt.setText(startLocalDt.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
                            tfGstr1B2CLarge1ToDt.setText(endLocalDt.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
                            tblcGstr1B2CLarge1SerialNo.setCellValueFactory(new PropertyValueFactory<>("sr_no"));
                            tblcGstr1B2CLarge1Particulars.setCellValueFactory(new PropertyValueFactory<>("state_name"));
                            tblcGstr1B2CLarge1RateOfTax.setCellValueFactory(new PropertyValueFactory<>("rate_of_tax"));
                            tblcGstr1B2CLarge1TaxableAmt.setCellValueFactory(new PropertyValueFactory<>("taxable_amt"));
                            tblcGstr1B2CLarge1IgstAmt.setCellValueFactory(new PropertyValueFactory<>("igst_amt"));
                            tblcGstr1B2CLarge1CgstAmt.setCellValueFactory(new PropertyValueFactory<>("cgst_amt"));
                            tblcGstr1B2CLarge1SgstAmt.setCellValueFactory(new PropertyValueFactory<>("sgst_amt"));
                            tblcGstr1B2CLarge1TaxAmt.setCellValueFactory(new PropertyValueFactory<>("tax_amt"));
                            tblcGstr1B2CLarge1InvoiceAmt.setCellValueFactory(new PropertyValueFactory<>("invoice_amt"));

                            tblvB2CLarge1List.setItems(GSTR1B2CLarge1ObservableList);

                            originalData = GSTR1B2CLarge1ObservableList;
                            calculateTotalAmount();

                            B2CLarge1Logger.info("Success in Displaying GSTR1B2C Large SCREEN1");

                        } else {
                            B2CLarge1Logger.debug("GSTR1B2C Large SCREEN1 List ResponseObject is null ");
                            tblvB2CLarge1List.getItems().clear();
                            calculateTotalAmount();
                        }
                    }

                }
            });
            apiClient.setOnCancelled(new EventHandler<WorkerStateEvent>() {

                @Override
                public void handle(WorkerStateEvent workerStateEvent) {
                    System.out.println("Api Client set on cancelled is 1"); //B2BScreen2Logger.error
                    tblvB2CLarge1List.getItems().clear();
                    calculateTotalAmount();
                }
            });
            apiClient.setOnFailed(new EventHandler<WorkerStateEvent>() {
                @Override
                public void handle(WorkerStateEvent workerStateEvent) {
                    System.out.println("Api Client setOnFailed is 2"); //B2BScreen2Logger.error
                    tblvB2CLarge1List.getItems().clear();
                    calculateTotalAmount();
                }
            });
            apiClient.start();

        } catch (Exception e) {
            B2CLarge1Logger.error("GSTR1B2C Large SCREEN1 List Error is " + e.getMessage());
            System.out.println("Api Client catch error 2");
            tblvB2CLarge1List.getItems().clear();
            calculateTotalAmount();
            e.printStackTrace();
        }

    }


    private void filterData(String keyword) {
        ObservableList<GSTR1B2CLarge1DTO> filteredData = FXCollections.observableArrayList();

        B2CLarge1Logger.error("Search GSTR3B Outward Taxable Supplies in GSTR3BOutwardTaxableSuppliesController");
        if (keyword.isEmpty()) {
            tblvB2CLarge1List.setItems(originalData);
            return;
        }

        for (GSTR1B2CLarge1DTO item : originalData) {
            if (matchesKeyword(item, keyword)) {
                filteredData.add(item);
            }
        }

        tblvB2CLarge1List.setItems(filteredData);
        calculateTotalAmount();
    }

    private boolean matchesKeyword(GSTR1B2CLarge1DTO item, String keyword) {
        String lowerCaseKeyword = keyword.toLowerCase();
        return
                item.getState_name().toLowerCase().contains(lowerCaseKeyword) ||
                        item.getRate_of_tax().toLowerCase().contains(lowerCaseKeyword) ||
                        item.getTaxable_amt().toLowerCase().contains(lowerCaseKeyword) ||
                        item.getIgst_amt().toLowerCase().contains(lowerCaseKeyword) ||
                        item.getCgst_amt().toLowerCase().contains(lowerCaseKeyword) ||
                        item.getSgst_amt().toLowerCase().contains(lowerCaseKeyword) ||
                        item.getInvoice_amt().toLowerCase().contains(lowerCaseKeyword) ||
                        item.getTax_amt().toLowerCase().contains(lowerCaseKeyword);
    }

    private void calculateTotalAmount() {
        ObservableList<GSTR1B2CLarge1DTO> filteredData = tblvB2CLarge1List.getItems();
        // Calculate the Totals
        double totalTaxable_amt= 0.0;
        double totalIgst_amt = 0.0;
        double totalCgst_amt = 0.0;
        double totalSgst_amt = 0.0;
        double totalTax_amt = 0.0;
        double totalInvoice_amt = 0.0;
        for (GSTR1B2CLarge1DTO item : filteredData) {
            totalTaxable_amt += Double.parseDouble(item.getTaxable_amt().isEmpty() ? "0.0" : item.getTaxable_amt());
            totalIgst_amt += Double.parseDouble(item.getIgst_amt().isEmpty() ? "0.0" : item.getIgst_amt());
            totalCgst_amt += Double.parseDouble(item.getCgst_amt().isEmpty() ? "0.0" : item.getCgst_amt());
            totalSgst_amt += Double.parseDouble(item.getSgst_amt().isEmpty() ? "0.0" : item.getSgst_amt());
            totalTax_amt += Double.parseDouble(item.getTax_amt().isEmpty() ? "0.0" : item.getTax_amt());
            totalInvoice_amt += Double.parseDouble(item.getInvoice_amt().isEmpty() ? "0.0" : item.getInvoice_amt());
        }

        // Update Labels in the FXML
        lbGstr1B2CL1TaxableAmt.setText(String.format("%.2f", totalTaxable_amt));
        lbGstr1B2CL1TotalIgst.setText(String.format("%.2f", totalIgst_amt));
        lbGstr1B2CL1TotalCgst.setText(String.format("%.2f", totalCgst_amt));
        lbGstr1B2CL1TotalSgst.setText(String.format("%.2f", totalSgst_amt));
        lbGstr1B2CL1TotalTax.setText(String.format("%.2f", totalTax_amt));
        lbGstr1B2CL1TotalAmt.setText(String.format("%.2f", totalInvoice_amt));
    }

}
