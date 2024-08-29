package com.opethic.genivis.controller.utilities;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.opethic.genivis.controller.tranx_purchase.PurchaseInvoiceListController;
import com.opethic.genivis.dto.Reports.PurchaseRegisterDTO;
import com.opethic.genivis.dto.reqres.product.Communicator;
import com.opethic.genivis.dto.reqres.pur_tranx.PurInvoiceEditDTO;
import com.opethic.genivis.dto.utilities.BarcodePrintDTO;
import com.opethic.genivis.network.APIClient;
import com.opethic.genivis.network.EndPoints;
import com.opethic.genivis.network.RequestType;
import com.opethic.genivis.utils.AlertUtility;
import com.opethic.genivis.utils.DateValidator;
import com.opethic.genivis.utils.GlobalController;
import com.opethic.genivis.utils.Globals;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.URL;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.function.Function;

import static com.opethic.genivis.utils.FxmFileConstants.PURCHASE_REGISTER_YEARLY_SLUG;
import static com.opethic.genivis.utils.Globals.barcode_home_path;
import static com.opethic.genivis.utils.Globals.prn_file_name;

public class BarcodePrintingController implements Initializable {

    @FXML
    public Button btnPrintBarcode;
    @FXML
    private TableView<BarcodePrintDTO> tblPurProductList;
    @FXML
    private TableColumn<BarcodePrintDTO, String> tblSrNo,tblProductName,tblBarcode,tblbatchNo,tblMrp,tblPackingName,tblUnitName,tblQty,tblPrintQty;

    private ObservableList<BarcodePrintDTO> originalData;
    Map<String, String> body = new HashMap<>(); // this used for mapping the data to call getPurchaseRegisterData() as per requirement  sending the Formdata
    private JsonObject jsonObject = null;
    public static final Logger barcodePrintLogger = LoggerFactory.getLogger(BarcodePrintingController.class);

    public static int id = 0;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        originalData = FXCollections.observableArrayList();
        tblPurProductList.getSelectionModel().setCellSelectionEnabled(true);
        tblPurProductList.setEditable(true);
        getBarcodePath();

        getPurchaseInvoiceData(Long.parseLong(BarcodePrintingController.id+""));

        btnPrintBarcode.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                ObservableList<BarcodePrintDTO> list=tblPurProductList.getItems();
                for (int i = 0; i < list.size(); i++) {
                    try {
                        BarcodePrintDTO barcodePrintDTO=list.get(i);
                        String content = new String(Files.readAllBytes(Paths.get(barcode_home_path+File.separator+prn_file_name)));
                        content = content.replace("company_name",barcodePrintDTO.getProduct_id());
                        content = content.replace("mobile_no",barcodePrintDTO.getProduct_id());
                        content = content.replace("supplier_code",barcodePrintDTO.getProduct_id());
                        content = content.replace("supplier_name",barcodePrintDTO.getProduct_id());
                        content = content.replace("purchase_rate",barcodePrintDTO.getProduct_id());
                        content = content.replace("mrp_rate",barcodePrintDTO.getMrp());
                        content = content.replace("ptr_mh",barcodePrintDTO.getProduct_id());
                        content = content.replace("ptr_ai",barcodePrintDTO.getProduct_id());
                        content = content.replace("csr_mh",barcodePrintDTO.getProduct_id());
                        content = content.replace("csr_ai",barcodePrintDTO.getProduct_id());
                        content = content.replace("category_name",barcodePrintDTO.getProduct_id());
                        content = content.replace("subcategory_name",barcodePrintDTO.getProduct_id());
                        content = content.replace("manufacturer_name",barcodePrintDTO.getProduct_id());
                        content = content.replace("brand_name",barcodePrintDTO.getProduct_id());
                        content = content.replace("formation_name",barcodePrintDTO.getProduct_id());
                        content = content.replace("barcode_no",barcodePrintDTO.getBarcode_no());
                        content = content.replace("batch_no",barcodePrintDTO.getBatch_no());
                        content = content.replace("expiry_date",barcodePrintDTO.getProduct_id());
                        content = content.replace("manufacture_date",barcodePrintDTO.getProduct_id());
                        content = content.replace("serial_no",barcodePrintDTO.getProduct_id());
                        content = content.replace("warranty_date",barcodePrintDTO.getProduct_id());
                        content = content.replace("product_quantity",barcodePrintDTO.getProduct_qty());
                        content = content.replace("product_discount",barcodePrintDTO.getProduct_id());
                        content = content.replace("product_code",barcodePrintDTO.getProduct_id());
                        content = content.replace("product_name",barcodePrintDTO.getProduct_name());
                        content = content.replace("package_name",barcodePrintDTO.getPacking_name());
                        content = content.replace("shelf_id",barcodePrintDTO.getProduct_id());
                        content = content.replace("product_cost",barcodePrintDTO.getProduct_id());
                        content = content.replace("product_cost_with_tax",barcodePrintDTO.getProduct_id());
                        content = content.replace("total_quantity",barcodePrintDTO.getProduct_id());
                        content = content.replace("pruchase_date",barcodePrintDTO.getProduct_id());
                        content = content.replace("content_name",barcodePrintDTO.getProduct_id());
                        content = content.replace("product_description",barcodePrintDTO.getProduct_id());
                        content = content.replace("product_margin",barcodePrintDTO.getProduct_id());
                        content = content.replace("print_quantity",barcodePrintDTO.getPrint_qty());
//                        System.out.println("Content=>"+content);

                        Path mFile = Paths.get(barcode_home_path+File.separator+Globals.bPrintFileName);
                        Files.write(mFile, content.getBytes());

                        try {
                            Process p = Runtime
                                    .getRuntime()
                                    .exec("cmd start cmd.exe /K \"cd "+barcode_home_path+" && TVS.exe "+Globals.bPrintFileName+" \"");
                            BufferedReader r = new BufferedReader(new InputStreamReader(p.getInputStream()));

                            while (r.readLine() == null){
                                p.destroy();
                                //System.out.println("Destroy : "+p.isAlive());
                            }

                        } catch (Exception e) {
                           //System.out.printf("Error = > "+e.toString());
                        }

                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        });

    }

    private void getBarcodePath() {


        APIClient apiClient = null;
        barcodePrintLogger.debug("Get getBarcodePath Started...");
        try {
            String formData = Globals.mapToStringforFormData(body);
            apiClient = new APIClient(EndPoints.GET_BARCODE_PATH, "", RequestType.FORM_DATA);
            apiClient.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
                @Override
                public void handle(WorkerStateEvent workerStateEvent) {

                    if(workerStateEvent.getSource().getValue()!=null){
                        jsonObject = new Gson().fromJson(workerStateEvent.getSource().getValue().toString(), JsonObject.class);
                        //System.out.println("BPATH=>"+jsonObject);
                        JsonObject jsonObject1=jsonObject.get("data").getAsJsonObject();
                        if(jsonObject1!=null && !jsonObject1.entrySet().isEmpty()){
                            barcode_home_path=jsonObject1.get("barcode_home_path").getAsString();
                            prn_file_name=jsonObject1.get("prn_file_name").getAsString();
                        }

                    }
                }
            });

            apiClient.setOnFailed(new EventHandler<WorkerStateEvent>() {
                @Override
                public void handle(WorkerStateEvent workerStateEvent) {
                    barcodePrintLogger.error("Network API cancelled in getBarcodePath" + workerStateEvent.getSource().getValue().toString());
                }
            });
            apiClient.setOnCancelled(new EventHandler<WorkerStateEvent>() {
                @Override
                public void handle(WorkerStateEvent workerStateEvent) {
                    barcodePrintLogger.error("Network API cancelled in getBarcodePath" + workerStateEvent.getSource().getValue().toString());
                }
            });
            apiClient.start();
            barcodePrintLogger.debug("Get getBarcodePath End...");

        }catch (Exception e){
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            String exceptionAsString = sw.toString();
            barcodePrintLogger.error("Exception in getBarcodePath:" + exceptionAsString);
        } finally {
            apiClient = null;
        }
    }


    private void getPurchaseInvoiceData(Long invoiceId) {

        Map<String, String> body = new HashMap<>();
        body.put("id", invoiceId.toString());
        String reqBody = Globals.mapToStringforFormData(body);
        //System.out.println("ReqqqBody=>"+reqBody);
        APIClient apiClient = null;
        barcodePrintLogger.debug("Get getPurchaseRegisterData Started...");
        try {
            String formData = Globals.mapToStringforFormData(body);
            apiClient = new APIClient(EndPoints.GET_PUR_INVOICE_BY_ID, reqBody, RequestType.FORM_DATA);
            apiClient.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
                @Override
                public void handle(WorkerStateEvent workerStateEvent) {

                    if(workerStateEvent.getSource().getValue()!=null){
                        jsonObject = new Gson().fromJson(workerStateEvent.getSource().getValue().toString(), JsonObject.class);

                        //System.out.println("BarcoDe=>"+jsonObject);
                        if(jsonObject.get("responseStatus").getAsInt()==200){

                            JsonArray barcodeListArray=jsonObject.get("barcode_list").getAsJsonArray();
                            //System.out.println("Out=>"+barcodeListArray);
                            originalData.clear();
                            for (int i = 0; i < barcodeListArray.size(); i++) {
                                int srNo=i+1;
                                JsonObject barcodeObject=barcodeListArray.get(i).getAsJsonObject();
                                //System.out.println("OBJ=>"+barcodeObject.toString());
                                BarcodePrintDTO barcodePrintDTO=new BarcodePrintDTO(
                                        barcodeObject.get("product_id").getAsString(),
                                        barcodeObject.get("product_name").getAsString(),
                                        barcodeObject.get("barcode_no").getAsString(),
                                        barcodeObject.get("batch_no").getAsString(),
                                        barcodeObject.get("mrp").getAsString(),
                                        barcodeObject.get("packing_name").getAsString(),
                                        barcodeObject.get("units_name").getAsString(),
                                        barcodeObject.get("product_qty").getAsString(),
                                        barcodeObject.get("print_qty").getAsString(),
                                        ""+srNo
                                );
                                originalData.add(barcodePrintDTO);

                                tblSrNo.setCellValueFactory(new PropertyValueFactory<>("sr_no"));
                                tblPrintQty.setCellFactory(TextFieldTableCell.forTableColumn());
                                tblProductName.setCellValueFactory(new PropertyValueFactory<>("product_name"));
                                tblBarcode.setCellValueFactory(new PropertyValueFactory<>("barcode_no"));
                                tblbatchNo.setCellValueFactory(new PropertyValueFactory<>("batch_no"));
                                tblMrp.setCellValueFactory(new PropertyValueFactory<>("mrp"));
                                tblPackingName.setCellValueFactory(new PropertyValueFactory<>("packing_name"));
                                tblUnitName.setCellValueFactory(new PropertyValueFactory<>("units_name"));
                                tblQty.setCellValueFactory(new PropertyValueFactory<>("product_qty"));
                                tblPrintQty.setCellValueFactory(new PropertyValueFactory<>("print_qty"));
                            }
                            tblPurProductList.setItems(originalData);
                        }

                    }
                }
            });

            apiClient.setOnFailed(new EventHandler<WorkerStateEvent>() {
                @Override
                public void handle(WorkerStateEvent workerStateEvent) {
                    barcodePrintLogger.error("Network API cancelled in getPurchaseRegisterData()" + workerStateEvent.getSource().getValue().toString());
                }
            });
            apiClient.setOnCancelled(new EventHandler<WorkerStateEvent>() {
                @Override
                public void handle(WorkerStateEvent workerStateEvent) {
                    barcodePrintLogger.error("Network API cancelled in getPurchaseRegisterData()" + workerStateEvent.getSource().getValue().toString());
                }
            });
            apiClient.start();
            barcodePrintLogger.debug("Get getPurchaseRegisterData End...");

        }catch (Exception e){
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            String exceptionAsString = sw.toString();
            barcodePrintLogger.error("Exception in getPurchaseRegisterData():" + exceptionAsString);
        } finally {
            apiClient = null;
        }
    }


}

