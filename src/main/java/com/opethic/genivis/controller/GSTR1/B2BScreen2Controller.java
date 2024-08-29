package com.opethic.genivis.controller.GSTR1;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.opethic.genivis.GenivisApplication;
import com.opethic.genivis.dto.GSTR1.GSTR1B2B1DTO;
import com.opethic.genivis.dto.GSTR1.GSTR1B2B2DTO;
import com.opethic.genivis.dto.GSTR1.GSTR1B2B2MainData;
import com.opethic.genivis.dto.GSTR1.GSTR1B2B2ResponseQbjData;
import com.opethic.genivis.dto.SalesQuotResObjDataDTO;
import com.opethic.genivis.dto.SalesQuotationMainDTO;
import com.opethic.genivis.dto.reqres.product.Communicator;
import com.opethic.genivis.network.APIClient;
import com.opethic.genivis.network.EndPoints;
import com.opethic.genivis.network.RequestType;
import com.opethic.genivis.utils.AlertUtility;
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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
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
import java.net.http.HttpResponse;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import static com.opethic.genivis.dto.reqres.product.Communicator.dateFormatter;
import static com.opethic.genivis.utils.FxmFileConstants.*;

public class B2BScreen2Controller implements Initializable {
    @FXML
    private ScrollPane spRootPane2;
    ObservableList<GSTR1B2B2DTO> GSTR1B2B2ObservableList = FXCollections.observableArrayList();
      @FXML
    private TableView<GSTR1B2B2DTO> tblvB2bScreen2List;

    @FXML
    private TableColumn tblcGstr1B2b2SerialNo, tblcGstr1B2b2Particulars, tblcGstr1B2b2Dates, tblcGstr1B2b2InvoiceNo, tblcGstr1B2b2TaxableAmt
            , tblcGstr1B2b2IgstAmt, tblcGstr1B2b2CgstAmt, tblcGstr1B2b2SgstAmt, tblcGstr1B2b2CessAmt, tblcGstr1B2b2TaxAmt, tblcGstr1B2b2InvoiceAmt;

    @FXML
    private Label lbGstr1B2B2totalLabel, lbGstr1B2B2Dates, lbGstr1B2B2InvoiceNo, lbGstr1B2B2TaxableAmt, lbGstr1B2B2TotalIgst,
            lbGstr1B2B2TotalCgst, lbGstr1B2B2TotalSgst, lbGstr1B2B2TotalTax, lbGstr1B2B2TotalAmt;

    @FXML
    private TextField tfGstr1B2b2FromDt, tfGstr1B2b2ToDt, tfGstr1B2b2Search;
    @FXML
    private Button GSTR1B2B2BtnPdf, GSTR1B2B2BtnExcel, GSTR1B2B2BtnCsv, GSTR1B2B2BtnPrint;
    public static final Logger B2BScreen2Logger = LoggerFactory.getLogger(B2BScreen1Controller.class);

    String Start_date = "", End_date = "";
    public static String  startDate = "", endDate ="";
    private ObservableList<GSTR1B2B2DTO> originalData;
    Integer ledger_id;

    public void setEditId(Integer InId) {
        ledger_id = InId;
        System.out.println("Sales ledger_id"+ledger_id);
        if (InId > -1) {
            setEditData();
        }
    }
    private void setEditData() {
        System.out.println("setEditData " + ledger_id);
        fetchGSTR1B2B2Datamain();
    }
    public void initialize(URL url, ResourceBundle resourceBundle)
    {
        spRootPane2.sceneProperty().addListener((observable, oldScene, newScene) -> {
            if (newScene != null && newScene.getWindow() instanceof Stage) {
                Communicator.stage = (Stage) newScene.getWindow();
            }
        });
        Platform.runLater(() -> {
            tfGstr1B2b2FromDt.requestFocus();
            //todo : Date  filter
            tfGstr1B2b2ToDt.setOnKeyPressed(event ->{
                if(event.getCode() == KeyCode.ENTER){
                    System.out.println("tfGstr1B2b2FromDt.getText() " + tfGstr1B2b2FromDt.getText() + "tfGstr1B2b2ToDt.getText()  " + tfGstr1B2b2ToDt.getText());

                    fetchGSTR1B2B2Datamain();
                    tfGstr1B2b2Search.requestFocus();
                }
            });
                //todo:excel Export
                GSTR1B2B2BtnExcel .setOnAction(event->{
                    exportToExcel();

            });
        });

        spRootPane2.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ESCAPE) {
//                System.out.println("Esc Clicked....!");
                GlobalController.getInstance().addTabStatic(GSTR1_B2B_SCREEN1_LIST_SLUG, false);
            }
        });


        //todo: Responsive code for tableview
        B2B2responsiveTable();
        System.out.println("startDate-------------- " + startDate);
        System.out.println("endDate-------------- " + endDate);
        // Setting formatted dates to text fields
        tfGstr1B2b2FromDt.setText(startDate);
        tfGstr1B2b2ToDt.setText(endDate);




        //todo: date Validation
        DateValidator.applyDateFormat(tfGstr1B2b2FromDt);
        DateValidator.applyDateFormat(tfGstr1B2b2ToDt);

        // todo : search functionality
        tfGstr1B2b2Search.textProperty().addListener((observable, oldValue, newValue) -> {
            filterData(newValue.trim());
        });

        GSTR1B2B2BtnPdf.focusedProperty().addListener((obs, wasFocused, isNowFocused) -> {
            if (isNowFocused) {
                GSTR1B2B2BtnPdf.setStyle("-fx-border-width: 2; -fx-border-color: #03dbfc");
            } else {
                GSTR1B2B2BtnPdf.setStyle("");
            }
        });

        GSTR1B2B2BtnPdf.hoverProperty().addListener((obs, wasHovered, isNowHovered) -> {
            if (isNowHovered) {
                GSTR1B2B2BtnPdf.setStyle("-fx-border-width: 2; -fx-border-color: #03dbfc");
            } else {
                GSTR1B2B2BtnPdf.setStyle("");
            }
        });

        GSTR1B2B2BtnExcel.focusedProperty().addListener((obs, wasFocused, isNowFocused) -> {
            if (isNowFocused) {
                GSTR1B2B2BtnExcel.setStyle("-fx-border-width: 2; -fx-border-color: #03dbfc");
            } else {
                GSTR1B2B2BtnExcel.setStyle("");
            }
        });

        GSTR1B2B2BtnExcel.hoverProperty().addListener((obs, wasHovered, isNowHovered) -> {
            if (isNowHovered) {
                GSTR1B2B2BtnExcel.setStyle("-fx-border-width: 2; -fx-border-color: #03dbfc");
            } else {
                GSTR1B2B2BtnExcel.setStyle("");
            }
        });

        GSTR1B2B2BtnCsv.focusedProperty().addListener((obs, wasFocused, isNowFocused) -> {
            if (isNowFocused) {
                GSTR1B2B2BtnCsv.setStyle("-fx-border-width: 2; -fx-border-color: #03dbfc");
            } else {
                GSTR1B2B2BtnCsv.setStyle("");
            }
        });

        GSTR1B2B2BtnCsv.hoverProperty().addListener((obs, wasHovered, isNowHovered) -> {
            if (isNowHovered) {
                GSTR1B2B2BtnCsv.setStyle("-fx-border-width: 2; -fx-border-color: #03dbfc");
            } else {
                GSTR1B2B2BtnCsv.setStyle("");
            }
        });

        GSTR1B2B2BtnPrint.focusedProperty().addListener((obs, wasFocused, isNowFocused) -> {
            if (isNowFocused) {
                GSTR1B2B2BtnPrint.setStyle("-fx-border-width: 2; -fx-border-color: #03dbfc");
            } else {
                GSTR1B2B2BtnPrint.setStyle("");
            }
        });

        GSTR1B2B2BtnPrint.hoverProperty().addListener((obs, wasHovered, isNowHovered) -> {
            if (isNowHovered) {
                GSTR1B2B2BtnPrint.setStyle("-fx-border-width: 2; -fx-border-color: #03dbfc");
            } else {
                GSTR1B2B2BtnPrint.setStyle("");
            }
        });

        nodetraversal(tfGstr1B2b2FromDt, tfGstr1B2b2ToDt);
        nodetraversal(tfGstr1B2b2ToDt, tfGstr1B2b2Search);
        nodetraversal(tfGstr1B2b2Search, GSTR1B2B2BtnPdf);
        nodetraversal(GSTR1B2B2BtnPdf, GSTR1B2B2BtnExcel);
        nodetraversal(GSTR1B2B2BtnExcel, GSTR1B2B2BtnCsv);
        nodetraversal(GSTR1B2B2BtnCsv,GSTR1B2B2BtnPrint);
        tblvB2bScreen2List.requestFocus();
    }

    //excelExport code
    private void exportToExcel() {
        ObservableList<GSTR1B2B2DTO> data = tblvB2bScreen2List.getItems();

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
                Double sumOfTaxableAmt = 0.0, sumOfIGSTAmt = 0.0, sumOfCGSTAmt = 0.0, sumOfSGSTAmt = 0.0, sumOfTaxAmt = 0.0, sumOfInvoiceAmt = 0.0;
                // Populate data rows
                int rowNum = 1;
                for (GSTR1B2B2DTO dto : data) {
                    Row row = sheet.createRow(rowNum++);
                    row.createCell(0).setCellValue(dto.getSr_no());
                    row.createCell(1).setCellValue(dto.getSundry_debtor_name());
                    row.createCell(2).setCellValue(dto.getInvoice_date());
                    row.createCell(3).setCellValue(dto.getInvoice_no());
                    row.createCell(4).setCellValue(dto.getTaxable_amt());
                    row.createCell(5).setCellValue(dto.getTotaligst());
                    row.createCell(6).setCellValue(dto.getTotalcgst());
                    row.createCell(7).setCellValue(dto.getTotalsgst());
                    row.createCell(9).setCellValue(dto.getTax_amt());
                    row.createCell(10).setCellValue(dto.getTotal_amount());
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
        ObservableList<TableColumn<GSTR1B2B2DTO, ?>> columns = tblvB2bScreen2List.getColumns();
        String[] headers = {"Sr.No", "Particulars", "Dates","Invoice No.", "Taxable Amt", "Integrated Tax Amt", "Central Tax Amt", "State Tax Amt", "Cess Amt", "Tax Amt", "Invoice Amt"};
        for (int i = 0; i < headers.length; i++) {
            headers[i] = columns.get(i).getText();
        }
        return headers;
    }


    public void B2B2responsiveTable(){
        //todo: Responsive code for tableview
        tblcGstr1B2b2SerialNo.prefWidthProperty().bind(tblvB2bScreen2List.widthProperty().multiply(0.05));
        tblcGstr1B2b2Particulars.prefWidthProperty().bind(tblvB2bScreen2List.widthProperty().multiply(0.15));
        tblcGstr1B2b2Dates.prefWidthProperty().bind(tblvB2bScreen2List.widthProperty().multiply(0.1));
        tblcGstr1B2b2InvoiceNo.prefWidthProperty().bind(tblvB2bScreen2List.widthProperty().multiply(0.1));
        tblcGstr1B2b2TaxableAmt.prefWidthProperty().bind(tblvB2bScreen2List.widthProperty().multiply(0.1));
        tblcGstr1B2b2IgstAmt.prefWidthProperty().bind(tblvB2bScreen2List.widthProperty().multiply(0.1));
        tblcGstr1B2b2CgstAmt.prefWidthProperty().bind(tblvB2bScreen2List.widthProperty().multiply(0.1));
        tblcGstr1B2b2SgstAmt.prefWidthProperty().bind(tblvB2bScreen2List.widthProperty().multiply(0.1));
        tblcGstr1B2b2CessAmt.prefWidthProperty().bind(tblvB2bScreen2List.widthProperty().multiply(0.05));
        tblcGstr1B2b2TaxAmt.prefWidthProperty().bind(tblvB2bScreen2List.widthProperty().multiply(0.05));
        tblcGstr1B2b2InvoiceAmt.prefWidthProperty().bind(tblvB2bScreen2List.widthProperty().multiply(0.1));

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

    //    todo: fetch GSTR1 B2b2 Api data
    public void fetchGSTR1B2B2Datamain() {
        try{
            APIClient apiClient = null;

            Map<String, String> body = new HashMap<>();
            body.put("searchText", "");

            Map<String, Object> sortObject = new HashMap<>();
            sortObject.put("colId", null);
            sortObject.put("isAsc", true);
            body.put("sort", sortObject.toString());
//            System.out.println("date sdf " + tfGstr1B2b2FromDt + "  to  " + tfGstr1B2b2ToDt );
            body.put("debtor_id", ledger_id.toString());
            body.put("searchText", "");

            if(tfGstr1B2b2FromDt.getText() != "" || tfGstr1B2b2ToDt.getText() != "" ){
                System.out.println("date sdf " + tfGstr1B2b2FromDt.getText() + "  to  " + tfGstr1B2b2ToDt.getText() );
                body.put("startDate", Communicator.text_to_date.fromString(tfGstr1B2b2FromDt.getText()).toString());
                body.put("endDate", Communicator.text_to_date.fromString(tfGstr1B2b2ToDt.getText()).toString());
            }
            else{
                System.out.println("date sdf1 " + startDate + "  to  " + endDate );
                body.put("startDate", Communicator.text_to_date.fromString(startDate).toString());
                body.put("endDate", Communicator.text_to_date.fromString(endDate).toString());
            }
            //            }
            String JsonReqdata = Globals.mapToString(body);
            System.out.println("JsonReqdata123 " + JsonReqdata);

            apiClient = new APIClient(EndPoints.GET_GSTR1_B2B2_REPORT_API, JsonReqdata, RequestType.JSON);
            apiClient.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
                @Override
                public void handle(WorkerStateEvent workerStateEvent) {
                    String res = workerStateEvent.getSource().getValue().toString();
                    GSTR1B2B2MainData responseBody = new Gson().fromJson(res, GSTR1B2B2MainData.class);
                    System.out.println("SalesQuotation " + ""+responseBody);
                    if (responseBody.getResponseStatus() == 200) {
                        tblvB2bScreen2List.getItems().clear();
                        System.out.println("GSTR1B2B2 "+responseBody);
                        List<GSTR1B2B2ResponseQbjData> list = responseBody.getResponseObject();
                        int count = 1;
                        if (list != null && list.size()>0) {

                            tblvB2bScreen2List.setVisible(true);
                            for (GSTR1B2B2ResponseQbjData element : list) {

                                System.out.println("response dataof GSTR1b2b screen2"+element);
                                String id = element.getId().toString();
                                String Invoice_no = element.getInvoiceNo();
                                String Invoice_date = element.getInvoiceDate();
                                String sales_serial_no = element.getSaleSerialNumber().toString();
                                String total_amount = element.getTotalAmount().toString();
                                String sundry_debtor_name = element.getSundryDebtorName();
                                String Sundry_debtors_id = element.getSundryDebtorId().toString();
                                String taxable_amt = element.getTaxableAmt().toString();
                                String totaligst = element.getTotaligst().toString() != null ? element.getTotaligst().toString() :"";
                                String totalcgst = element.getTotalcgst().toString();
                                String totalsgst = element.getTotalsgst().toString();
                                String tax_amt = element.getTaxAmt().toString();
                                String TranxCode = element.getTranxCode() != null ? element.getTranxCode() : "";

                                GSTR1B2B2ObservableList.add(new GSTR1B2B2DTO(String.valueOf(count++), id,Invoice_no, Invoice_date, sales_serial_no, total_amount, sundry_debtor_name,Sundry_debtors_id,"", "",
                                        tax_amt, taxable_amt, "", "", "", totalcgst, totalsgst, totaligst, TranxCode)
                                );
                            }


                            tblcGstr1B2b2SerialNo.setCellValueFactory(new PropertyValueFactory<>("sr_no"));
                            tblcGstr1B2b2Particulars.setCellValueFactory(new PropertyValueFactory<>("sundry_debtor_name"));
                            tblcGstr1B2b2Dates.setCellValueFactory(new PropertyValueFactory<>("Invoice_date"));
                            tblcGstr1B2b2InvoiceNo.setCellValueFactory(new PropertyValueFactory<>("Invoice_no"));
                            tblcGstr1B2b2TaxableAmt.setCellValueFactory(new PropertyValueFactory<>("taxable_amt"));
                            tblcGstr1B2b2IgstAmt.setCellValueFactory(new PropertyValueFactory<>("totaligst"));
                            tblcGstr1B2b2CgstAmt.setCellValueFactory(new PropertyValueFactory<>("totalcgst"));
                            tblcGstr1B2b2SgstAmt.setCellValueFactory(new PropertyValueFactory<>("totalsgst"));
                            tblcGstr1B2b2TaxAmt.setCellValueFactory(new PropertyValueFactory<>("tax_amt"));
                            tblcGstr1B2b2InvoiceAmt.setCellValueFactory(new PropertyValueFactory<>("total_amount"));

                            tblvB2bScreen2List.setItems(GSTR1B2B2ObservableList);

                            originalData = GSTR1B2B2ObservableList;
                            calculateTotalAmount();

                            B2BScreen2Logger.info("Success in Displaying GSTR1B2B Screen2");

                        } else {
                            B2BScreen2Logger.debug("GSTR1B2B Screen2 List ResponseObject is null ");
                            tblvB2bScreen2List.getItems().clear();
                            calculateTotalAmount();
                        }
                    }

                }
            });
            apiClient.setOnCancelled(new EventHandler<WorkerStateEvent>() {

                @Override
                public void handle(WorkerStateEvent workerStateEvent) {
                    System.out.println("Api Client set on cancelled is 1"); //B2BScreen2Logger.error
                    tblvB2bScreen2List.getItems().clear();
                    calculateTotalAmount();
                }
            });
            apiClient.setOnFailed(new EventHandler<WorkerStateEvent>() {
                @Override
                public void handle(WorkerStateEvent workerStateEvent) {
                    System.out.println("Api Client setOnFailed is 2"); //B2BScreen2Logger.error
                    tblvB2bScreen2List.getItems().clear();
                    calculateTotalAmount();
                }
            });
            apiClient.start();

        } catch (Exception e) {
            B2BScreen2Logger.error("GSTR1B2B SCREEN2 List Error is " + e.getMessage());
            System.out.println("Api Client catch error 2");
            tblvB2bScreen2List.getItems().clear();
            calculateTotalAmount();
            e.printStackTrace();
        }
//        try {
//            B2BScreen2Logger.info("Fetching GSTR1B2B1 Data");
//            Map<String, String> body = new HashMap<>();
//            if(!tfGstr1B2b2FromDt.getText().equalsIgnoreCase("")  || !tfGstr1B2b2ToDt.getText().equalsIgnoreCase("")){
//                System.out.println("date sdf " + tfGstr1B2b2FromDt + "  to  " + tfGstr1B2b2ToDt );
//                body.put("start_date", Communicator.text_to_date.fromString(tfGstr1B2b2FromDt.getText()).toString());
//                body.put("end_date", Communicator.text_to_date.fromString(tfGstr1B2b2ToDt.getText()).toString());
//            }
//            body.put("searchText", "");
//            Map<String, Object> sortObject = new HashMap<>();
//            sortObject.put("colId", null);
//            sortObject.put("isAsc", true);
//            body.put("sort", sortObject.toString());
//            String requestBody = Globals.mapToStringforFormData(body);
//            System.out.println("requestBody 123 " + requestBody);
//            HttpResponse<String> response = APIClient.postFormDataRequest(requestBody, EndPoints.GET_GSTR1_B2B2_REPORT_API);
//            B2BScreen2Logger.info("response data of GSTR1B2B2Data " + response.body());
//            JsonObject jsonObject = new Gson().fromJson(response.body(), JsonObject.class);
//            ObservableList<GSTR1B2B2DTO> observableList = FXCollections.observableArrayList();
//            System.out.println("response data of GSTR1B2B2Data " + jsonObject );
//            if (jsonObject.get("responseStatus").getAsInt() == 200) {
//                Start_date = (jsonObject.get("start_date").getAsString());
//                End_date = jsonObject.get("end_date").getAsString();
//                LocalDate startLocalDt = LocalDate.parse(Start_date);
//                LocalDate endLocalDt = LocalDate.parse(End_date);
////                JsonObject responseObject = jsonObject.get("responseObject").getAsJsonObject();
//                JsonArray responseArray = jsonObject.get("data").getAsJsonArray();
////                    int i = 0;
//                if (responseArray.size() > 0) {
//                    tblvB2bScreen2List.setVisible(true);
//                    for (JsonElement element : responseArray) {
////                         i++;
//                        JsonObject item = element.getAsJsonObject();
////                        String serialNo = String.valueOf(i);
//                        String ledger_id = item.get("ledger_id").getAsString();
//                        String ledger_name = item.get("ledger_name").getAsString();
//                        String gst_number = item.get("gst_number") != null ? item.get("gst_number").getAsString() : "";
////                        String referenceNo = item.get("referenceNo").getAsString();
//                        String total_invoices = item.get("total_invoices") != null ? item.get("total_invoices").getAsString() : "";
//                        String total_igst = item.get("total_igst") != null ? item.get("total_igst").getAsString() : "";
//                        String total_cgst = item.get("total_cgst") != null ? item.get("total_cgst").getAsString() : "";
//                        String total_sgst = item.get("total_sgst") != null ? item.get("total_sgst").getAsString() : "";
//                        String total_tax = item.get("total_tax") != null ? item.get("total_tax").getAsString() : "";
//                        String total_amt = item.get("total_amt") != null ? item.get("total_amt").getAsString() : "";
//                        String taxable_amt = item.get("taxable_amt") != null ? item.get("taxable_amt").getAsString() : "";
//
//                        observableList.add(new GSTR1B2B2DTO(ledger_id, ledger_name, gst_number, total_invoices, taxable_amt, total_igst, total_cgst, total_sgst, total_tax, total_amt)
//                        );
//                    }
//
//                    tfGstr1B2b2FromDt.setText(startLocalDt.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
//                    tfGstr1B2b2ToDt.setText(endLocalDt.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
////                    tfGstr1B2b1FromDt.setText(jsonObject.get("start_date").toString());
////                    tfGstr1B2b1ToDt.setText(jsonObject.get("end_date").toString());
////                    tblcGstr1B2b1SerialNo.setCellValueFactory(new PropertyValueFactory<>("serialNo"));
//                    tblcGstr1B2b1Particulars.setCellValueFactory(new PropertyValueFactory<>("ledger_name"));
//                    tblcGstr1B2b1TaxableAmt.setCellValueFactory(new PropertyValueFactory<>("taxable_amt"));
//                    tblcGstr1B2b1Gstin.setCellValueFactory(new PropertyValueFactory<>("gst_number"));
//                    tblcGstr1B2b1VoucherCount.setCellValueFactory(new PropertyValueFactory<>("total_invoices"));
//                    tblcGstr1B2b1IgstAmt.setCellValueFactory(new PropertyValueFactory<>("total_igst"));
//                    tblcGstr1B2b1CgstAmt.setCellValueFactory(new PropertyValueFactory<>("total_cgst"));
//                    tblcGstr1B2b1SgstAmt.setCellValueFactory(new PropertyValueFactory<>("total_sgst"));
//                    tblcGstr1B2b1TaxAmt.setCellValueFactory(new PropertyValueFactory<>("total_tax"));
//                    tblcGstr1B2b1InvoiceAmt.setCellValueFactory(new PropertyValueFactory<>("total_amt"));
//
//                    tblvB2bScreen2List.setItems(observableList);
//                    originalData = observableList;
//                    calculateTotalAmount();
//
//                    B2BScreen2Logger.info("Success in Displaying GSTR1B2B1 List : SalesQuotationListController");
//                } else {
//
//                    B2BScreen2Logger.error("Error in response of fetching GSTR1B2B1Data");
//                    tblvB2bScreen2List.getItems().clear();
//                    calculateTotalAmount();
//                }
//            }
//        } catch (Exception e) {
//            B2BScreen2Logger.error("Error in Fetching GSTR1B2B1Data is" + e.getMessage());
//            e.printStackTrace();
//        }
    }

    private void filterData(String keyword) {
        ObservableList<GSTR1B2B2DTO> filteredData = FXCollections.observableArrayList();

        B2BScreen2Logger.error("Search GSTR3B Outward Taxable Supplies in GSTR3BOutwardTaxableSuppliesController");
        if (keyword.isEmpty()) {
            tblvB2bScreen2List.setItems(originalData);
            return;
        }

        for (GSTR1B2B2DTO item : originalData) {
            if (matchesKeyword(item, keyword)) {
                filteredData.add(item);
            }
        }

        tblvB2bScreen2List.setItems(filteredData);
        calculateTotalAmount();
    }

    private boolean matchesKeyword(GSTR1B2B2DTO item, String keyword) {
        String lowerCaseKeyword = keyword.toLowerCase();
        return
                item.getInvoice_date().toLowerCase().contains(lowerCaseKeyword) ||
                        item.getInvoice_no().toLowerCase().contains(lowerCaseKeyword) ||
                        item.getSundry_debtor_name().toLowerCase().contains(lowerCaseKeyword) ||
                        item.getTaxable_amt().toLowerCase().contains(lowerCaseKeyword) ||
                        item.getTax_amt().toLowerCase().contains(lowerCaseKeyword) ||
                        item.getTotal_amount().toLowerCase().contains(lowerCaseKeyword) ||
                        item.getTotaligst().toLowerCase().contains(lowerCaseKeyword) ||
                        item.getTotalcgst().toLowerCase().contains(lowerCaseKeyword) ||
                        item.getTotalsgst().toLowerCase().contains(lowerCaseKeyword);
    }

    private void calculateTotalAmount() {
        ObservableList<GSTR1B2B2DTO> filteredData = tblvB2bScreen2List.getItems();
        // Calculate the Totals
        double totalTaxable_amt= 0.0;
        double totalIgst_amt = 0.0;
        double totalCgst_amt = 0.0;
        double totalSgst_amt = 0.0;
        double totalTax_amt = 0.0;
        double totalInvoice_amt = 0.0;
        for (GSTR1B2B2DTO item : filteredData) {
            totalTaxable_amt += Double.parseDouble(item.getTaxable_amt().isEmpty() ? "0.0" : item.getTaxable_amt());
            totalIgst_amt += Double.parseDouble(item.getTotaligst().isEmpty() ? "0.0" : item.getTotaligst());
            totalCgst_amt += Double.parseDouble(item.getTotalcgst().isEmpty() ? "0.0" : item.getTotalcgst());
            totalSgst_amt += Double.parseDouble(item.getTotalsgst().isEmpty() ? "0.0" : item.getTotalsgst());
            totalTax_amt += Double.parseDouble(item.getTax_amt().isEmpty() ? "0.0" : item.getTax_amt());
            totalInvoice_amt += Double.parseDouble(item.getTotal_amount().isEmpty() ? "0.0" : item.getTotal_amount());
        }

        // Update Labels in the FXML
        lbGstr1B2B2TaxableAmt.setText(String.format("%.2f", totalTaxable_amt));
        lbGstr1B2B2TotalIgst.setText(String.format("%.2f", totalIgst_amt));
        lbGstr1B2B2TotalCgst.setText(String.format("%.2f", totalCgst_amt));
        lbGstr1B2B2TotalSgst.setText(String.format("%.2f", totalSgst_amt));
        lbGstr1B2B2TotalTax.setText(String.format("%.2f", totalTax_amt));
        lbGstr1B2B2TotalAmt.setText(String.format("%.2f", totalInvoice_amt));
    }
}
