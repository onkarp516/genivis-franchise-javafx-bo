package com.opethic.genivis.controller.master;

import com.google.gson.*;
import com.opethic.genivis.controller.tranx_sales.SalesOrderListController;
import com.opethic.genivis.dto.FranchiseListDTO;
import com.opethic.genivis.dto.PaymentModeDTO;
import com.opethic.genivis.dto.ProductDTO;
import com.opethic.genivis.dto.ProductListBatchHistDTO;
import com.opethic.genivis.models.tranx.sales.TranxSelectedProduct;
import com.opethic.genivis.network.APIClient;
import com.opethic.genivis.network.EndPoints;
import com.opethic.genivis.network.RequestType;
import com.opethic.genivis.utils.AlertUtility;
import com.opethic.genivis.utils.FxmFileConstants;
import com.opethic.genivis.utils.GlobalController;
import com.opethic.genivis.utils.Globals;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.URL;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import static com.opethic.genivis.utils.Globals.salesOrderListDTO;

public class ProductListController implements Initializable {


    @FXML
    public Label lbUnit1,lbConv1,lbMrp1,lbPTRMH1,lbPTRAI1,lbCSRMH1,lbCSRAI1;

    @FXML
    public Label lbUnit2,lbConv2,lbMrp2,lbPTRMH2,lbPTRAI2,lbCSRMH2,lbCSRAI2;

    @FXML
    public Label lbUnit3,lbConv3,lbMrp3,lbPTRMH3,lbPTRAI3,lbCSRMH3,lbCSRAI3;

    @FXML
    BorderPane bpProductListRoot;

    private ObservableList<TranxSelectedProduct> lstProducts = FXCollections.observableArrayList();

    @FXML
    private TextField tfSearch;
    @FXML
    private Button btnSubmit;
    @FXML
    private TableView<TranxSelectedProduct> tvProductList;
    @FXML
    private TableColumn<TranxSelectedProduct, Long> tcProductId;
    @FXML
    private TableColumn<TranxSelectedProduct, String> tcSearchCode;

    @FXML
    private TableColumn<TranxSelectedProduct, String> tcProductName;
    @FXML
    private TableColumn<TranxSelectedProduct, String> tcPacking;

    @FXML
    private TableColumn<TranxSelectedProduct, String> tcBarcode;

    @FXML
    private TableColumn<TranxSelectedProduct, String> tcBrand;
    @FXML
    private TableColumn<TranxSelectedProduct, String> tcMrp;
    @FXML
    private TableColumn<TranxSelectedProduct, String> tcCurrentStock;
    @FXML
    private TableColumn<TranxSelectedProduct, String> tcUnit;
    @FXML
    private TableColumn<TranxSelectedProduct, String> tcSaleRate;


    @FXML
    public TableColumn<TranxSelectedProduct, String> tcUnit_1_Stock;
    @FXML
    public TableColumn<TranxSelectedProduct, String> tcUnit1;
    @FXML
    public TableColumn<TranxSelectedProduct, String> tcUnit_2_Stock;
    @FXML
    public TableColumn<TranxSelectedProduct, String> tcUnit2;
    @FXML
    public TableColumn<TranxSelectedProduct, String> tcUnit_3_Stock;
    @FXML
    public TableColumn<TranxSelectedProduct, String> tcUnit3;




    @FXML
    private TableView tvPrdLstBatchHistory;
    @FXML
    private TableColumn tcPrdLstBtachNo, tcPrdLstMFGDate, tcPrdLstExpDate, tcPrdLstAvlStk, tcPrdLstMRP, tcPrdLstPurRate, tcPrdLstDisPer, tcPrdLstDisAmt, tcPrdLstCessPer,
            tcPrdLstCessAmt, tcPrdLstBarcode, tcPrdLstMarginPer, tcPrdLstTotalAmount, tcPrdLstFSR, tcPrdLstCSR, tcPrdLstSalesRate,tcPrdLstFSRAI,tcPrdLstCSRAI;
    @FXML
    private ProgressBar pbProductBar;
    @FXML
    private Label lblInfo;
    @FXML
    private Tab tbProduct;
    @FXML
    private Tab tbBatch;
    @FXML
    private Button btnProductCreate;
    @FXML
    private AnchorPane apPrdLstHeaderSect;
    @FXML
    private Label lblPrdLstMFG, lblPrdLstFormulation, lblPrdLstCtg, lblPrdLstTaxType, lblPrdLstTaxPer, lblPrdLstMargPer,
            lblPrdLstCost, lblPrdLstPurRate, lblPrdLstHSN, lblPrdLstMinStk, lblPrdLstMaxStk, lblPrdLstShlfId;
    private static final Logger logger = LogManager.getLogger(ProductListController.class);
    /*** We are accessing selectedPrId in ProductCreateController therefore we declared it as public ****/
    public static Long selectedPrId = 0L;
    private JsonObject jsonObject = null;
    private ObservableList<TranxSelectedProduct> row = FXCollections.observableArrayList();
    private ObservableList<ProductListBatchHistDTO> histrow = FXCollections.observableArrayList();
    String idFirstDown="";

    //? Highlight the Record Start
    public static boolean isNewProductCreated = false; // Flag for new creation
    public static String editedProductId = null; // ID for edited franchise
    //? Highlight the Record End
    String id = "";
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Enter Functionality Method
        initialEnterMethod();
        getAllTranxProductList("","");


        btnProductCreate.setOnAction(event -> GlobalController.getInstance().addTabStatic("product-create", false));


        tcProductId.setVisible(false);
        tvProductList.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        Platform.runLater(() -> tfSearch.requestFocus());
        lblInfo.setVisible(false);
        // getProductData("");
        tfSearch.setOnKeyTyped(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                String searchKey = tfSearch.getText().trim();
                if (searchKey.length() > 0) {

                    getAllTranxProductList(searchKey,searchKey);
                }else {
                    getAllTranxProductList(searchKey,searchKey);
                }
            }});
        logger.debug("Before Double click event to get the id from selected row of product list table in " +
                "tvProductList.setRowFactory() ");


//        // Add a key event listener to the text field
//        apPrdLstHeaderSect.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
//            if (event.getCode() == KeyCode.DOWN) {
//                tvProductList.getSelectionModel().selectFirst();
//                tvProductList.requestFocus();
//                event.consume();
//            }
//        });
        prdBatchHistoryTblResponsive();


        tcSearchCode.setCellValueFactory(new PropertyValueFactory<>("code"));
        tcProductName.setCellValueFactory(new PropertyValueFactory<>("productName"));
        tcPacking.setCellValueFactory(new PropertyValueFactory<>("packageName"));
        tcBarcode.setCellValueFactory(new PropertyValueFactory<>("barCode"));
        tcBrand.setCellValueFactory(new PropertyValueFactory<>("brandName"));
        tcUnit_1_Stock.setCellValueFactory(new PropertyValueFactory<>("unit1ClsStock"));
        tcUnit1.setCellValueFactory(new PropertyValueFactory<>("unit1Name"));
        tcUnit_2_Stock.setCellValueFactory(new PropertyValueFactory<>("unit2ClsStock"));
        tcUnit2.setCellValueFactory(new PropertyValueFactory<>("unit2Name"));
        tcUnit_3_Stock.setCellValueFactory(new PropertyValueFactory<>("unit3ClsStock"));
        tcUnit3.setCellValueFactory(new PropertyValueFactory<>("unit3Name"));

        tvProductList.getFocusModel().focusedCellProperty().addListener((obs, old, nw) -> {

            // System.out.println("test mk : "+tvProductList.getSelectionModel().getSelectedItem().getProductId());

            tranxProductDetailsFun(tvProductList.getSelectionModel().getSelectedItem().getProductId().toString());
            lbUnit1.setText(tvProductList.getSelectionModel().getSelectedItem() != null ? tvProductList.getSelectionModel().getSelectedItem().getUnit1Name() : "");
            lbConv1.setText(tvProductList.getSelectionModel().getSelectedItem() != null ? String.valueOf(tvProductList.getSelectionModel().getSelectedItem().getUnit1Conv()) : "");
            lbMrp1.setText(tvProductList.getSelectionModel().getSelectedItem() != null ? String.valueOf(tvProductList.getSelectionModel().getSelectedItem().getUnit1Mrp()) : "");
            lbPTRMH1.setText(tvProductList.getSelectionModel().getSelectedItem() != null ? String.valueOf(tvProductList.getSelectionModel().getSelectedItem().getUnit1FSRMH()) : "");
            lbPTRAI1.setText(tvProductList.getSelectionModel().getSelectedItem() != null ? String.valueOf(tvProductList.getSelectionModel().getSelectedItem().getUnit1FSRAI()) : "");
            lbCSRMH1.setText(tvProductList.getSelectionModel().getSelectedItem() != null ? String.valueOf(tvProductList.getSelectionModel().getSelectedItem().getUnit1CSRMH()) : "");
            lbCSRAI1.setText(tvProductList.getSelectionModel().getSelectedItem() != null ? String.valueOf(tvProductList.getSelectionModel().getSelectedItem().getUnit1CSRAI()) : "");

            lbUnit2.setText(tvProductList.getSelectionModel().getSelectedItem() != null ? tvProductList.getSelectionModel().getSelectedItem().getUnit2Name() : "");
            lbConv2.setText(tvProductList.getSelectionModel().getSelectedItem() != null ? String.valueOf(tvProductList.getSelectionModel().getSelectedItem().getUnit2Conv()) : "");
            lbMrp2.setText(tvProductList.getSelectionModel().getSelectedItem() != null ? String.valueOf(tvProductList.getSelectionModel().getSelectedItem().getUnit2Mrp()) : "");
            lbPTRMH2.setText(tvProductList.getSelectionModel().getSelectedItem() != null ? String.valueOf(tvProductList.getSelectionModel().getSelectedItem().getUnit2FSRMH()) : "");
            lbPTRAI2.setText(tvProductList.getSelectionModel().getSelectedItem() != null ? String.valueOf(tvProductList.getSelectionModel().getSelectedItem().getUnit2FSRAI()) : "");
            lbCSRMH2.setText(tvProductList.getSelectionModel().getSelectedItem() != null ? String.valueOf(tvProductList.getSelectionModel().getSelectedItem().getUnit2CSRMH()) : "");
            lbCSRAI2.setText(tvProductList.getSelectionModel().getSelectedItem() != null ? String.valueOf(tvProductList.getSelectionModel().getSelectedItem().getUnit2CSRAI()) : "");

            lbUnit3.setText(tvProductList.getSelectionModel().getSelectedItem() != null ? tvProductList.getSelectionModel().getSelectedItem().getUnit3Name() : "");
            lbConv3.setText(tvProductList.getSelectionModel().getSelectedItem() != null ? String.valueOf(tvProductList.getSelectionModel().getSelectedItem().getUnit3Conv()) : "");
            lbMrp3.setText(tvProductList.getSelectionModel().getSelectedItem() != null ? String.valueOf(tvProductList.getSelectionModel().getSelectedItem().getUnit3Mrp()) : "");
            lbPTRMH3.setText(tvProductList.getSelectionModel().getSelectedItem() != null ? String.valueOf(tvProductList.getSelectionModel().getSelectedItem().getUnit3FSRMH()) : "");
            lbPTRAI3.setText(tvProductList.getSelectionModel().getSelectedItem() != null ? String.valueOf(tvProductList.getSelectionModel().getSelectedItem().getUnit3FSRAI()) : "");
            lbCSRMH3.setText(tvProductList.getSelectionModel().getSelectedItem() != null ? String.valueOf(tvProductList.getSelectionModel().getSelectedItem().getUnit3CSRMH()) : "");
            lbCSRAI3.setText(tvProductList.getSelectionModel().getSelectedItem() != null ? String.valueOf(tvProductList.getSelectionModel().getSelectedItem().getUnit3CSRAI()) : "");
        });




    }

    public void prdBatchHistoryTblResponsive() {
        //Set the TableView Columns with proper height and Width
        tvPrdLstBatchHistory.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
//        tcPrdLstBtachNo.prefWidthProperty().bind(tvPrdLstBatchHistory.widthProperty().multiply(0.11));
//        tcPrdLstMFGDate.prefWidthProperty().bind(tvPrdLstBatchHistory.widthProperty().multiply(0.07));
//        tcPrdLstExpDate.prefWidthProperty().bind(tvPrdLstBatchHistory.widthProperty().multiply(0.07));
//        tcPrdLstAvlStk.prefWidthProperty().bind(tvPrdLstBatchHistory.widthProperty().multiply(0.05));
//        tcPrdLstMRP.prefWidthProperty().bind(tvPrdLstBatchHistory.widthProperty().multiply(0.07));
//        tcPrdLstPurRate.prefWidthProperty().bind(tvPrdLstBatchHistory.widthProperty().multiply(0.07));
//        tcPrdLstDisPer.prefWidthProperty().bind(tvPrdLstBatchHistory.widthProperty().multiply(0.04));
//        tcPrdLstDisAmt.prefWidthProperty().bind(tvPrdLstBatchHistory.widthProperty().multiply(0.04));
//        tcPrdLstCessPer.prefWidthProperty().bind(tvPrdLstBatchHistory.widthProperty().multiply(0.04));
//        tcPrdLstCessAmt.prefWidthProperty().bind(tvPrdLstBatchHistory.widthProperty().multiply(0.04));
//        tcPrdLstBarcode.prefWidthProperty().bind(tvPrdLstBatchHistory.widthProperty().multiply(0.08));
//        tcPrdLstMarginPer.prefWidthProperty().bind(tvPrdLstBatchHistory.widthProperty().multiply(0.04));
//        tcPrdLstTotalAmount.prefWidthProperty().bind(tvPrdLstBatchHistory.widthProperty().multiply(0.07));
//        tcPrdLstFSR.prefWidthProperty().bind(tvPrdLstBatchHistory.widthProperty().multiply(0.07));
//        tcPrdLstCSR.prefWidthProperty().bind(tvPrdLstBatchHistory.widthProperty().multiply(0.07));
//        tcPrdLstSalesRate.prefWidthProperty().bind(tvPrdLstBatchHistory.widthProperty().multiply(0.07));
    }


    private void initialEnterMethod() {
        bpProductListRoot.addEventFilter(KeyEvent.KEY_PRESSED, (KeyEvent event) -> {
            if (event.getCode() == KeyCode.ENTER) {
                if (event.getTarget() instanceof Button targetButton && targetButton.getText().equalsIgnoreCase("create")) {
                }  else {
                    productEditPage();
                    KeyEvent newEvent = new KeyEvent(
                            null,
                            null,
                            KeyEvent.KEY_PRESSED,
                            "",
                            "\t",
                            KeyCode.TAB,
                            event.isShiftDown(),
                            event.isControlDown(),
                            event.isAltDown(),
                            event.isMetaDown()
                    );
                    Event.fireEvent(event.getTarget(), newEvent);
                    event.consume();
                }
            }
            if (event.getCode() == KeyCode.N && event.isControlDown()) {
                btnProductCreate.fire();
            }
            if (event.getCode() == KeyCode.DOWN && tfSearch.isFocused()){
                tvProductList.getSelectionModel().select(0);
                if(idFirstDown!="") {
                    //  tranxProductDetailsFun(idFirstDown);
                    getProductBatchData(idFirstDown);
                }
                tvProductList.requestFocus();
            }else if (event.getCode() == KeyCode.DOWN && btnProductCreate.isFocused()){
                tvProductList.getSelectionModel().select(0);
                if(idFirstDown!="") {
                    //tranxProductDetailsFun(idFirstDown);
                    getProductBatchData(idFirstDown);
                }
                tvProductList.requestFocus();
            }else if (event.getCode() == KeyCode.E && event.isControlDown()) {
                productEditPage();
            }else if (event.getCode() == KeyCode.D && event.isControlDown()) {
                handleDeleteProduct();
            }
            else if (event.getCode() == KeyCode.O && event.isControlDown()) {
                productOpeningStockPage();
            }
        });


        tvProductList.setRowFactory(tv -> {
            TableRow<TranxSelectedProduct> row = new TableRow<>();

            // Double click event handler
            row.setOnMouseClicked(event -> {
                if (!row.isEmpty() && event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2) {
                    TranxSelectedProduct rowData = row.getItem();
                    productEditPage();
                }
            });

            // Focus event handler
            row.focusedProperty().addListener((obs, oldVal, newVal) -> {
                if (newVal && !row.isEmpty() && tvProductList.getSelectionModel().getSelectedItem() == row.getItem()) {
                    TranxSelectedProduct rowData = row.getItem();
                    //    tranxProductDetailsFun(String.valueOf(rowData.getProductId()));
                    getProductBatchData(String.valueOf(rowData.getProductId()));
                }
            });

            return row;
        });
    }

//    private void getProductData(String searchKey) {
//        APIClient apiClient = null;
//        try {
//            logger.debug("Get Product Data Started...");
//            Map<String, String> body = new HashMap<>();
//            body.put("pageNo", "1");
//            body.put("pageSize", "50");
//            body.put("searchText", searchKey);
//            String requestBody = Globals.mapToString(body);
//            System.out.println("rrrrrrrrrrrrrrrrrrrrrrrrrrrrrrr"+requestBody);
////            HttpResponse<String> response = APIClient.postJsonRequest(requestBody, EndPoints.PRODUCT_LIST_ENDPOINT);
//            apiClient = new APIClient(EndPoints.PRODUCT_LIST_ENDPOINT, requestBody, RequestType.JSON);
//            apiClient.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
//                @Override
//                public void handle(WorkerStateEvent workerStateEvent) {
//                    jsonObject = new Gson().fromJson(workerStateEvent.getSource().getValue().toString(), JsonObject.class);
//                    if (!jsonObject.isJsonNull() && jsonObject.get("responseStatus").getAsInt() == 200) {
//                        tvProductList.getItems().clear();
//                        JsonObject responseObject = jsonObject.get("responseObject").getAsJsonObject();
//                        JsonArray responseArray = responseObject.get("data").getAsJsonArray();
//                        if (responseArray.size() > 0) {
//                            tvProductList.setVisible(true);
//                            lblInfo.setVisible(false);
//                            //? to get the First Record ID
//                            JsonElement idElement=responseArray.get(0);
//                            if (idElement.isJsonObject()) {
//                                JsonObject idObject = idElement.getAsJsonObject();
//                                idFirstDown = idObject.get("id").getAsString();
//                            } else {
//                                logger.error("The first element is not a JSON object.");
//                            }
//                            for (JsonElement mElement : responseArray) {
//                                JsonObject item = mElement.getAsJsonObject();
//                                String id = item.has("id") && !(item.get("id") instanceof JsonNull) ? item.get("id").getAsString() : "0";
//                                String searchCode = item.has("search_code") && !(item.get("search_code") instanceof JsonNull) ? item.get("search_code").getAsString() : "";
//                                String productName = item.has("product_name") && !(item.get("product_name") instanceof JsonNull) ? item.get("product_name").getAsString() : "";
//                                String packing = item.has("packing") && !(item.get("packing") instanceof JsonNull) ? item.get("packing").getAsString() : "";
//                                String barcode = item.has("barcode") && !(item.get("barcode") instanceof JsonNull) ? item.get("barcode").getAsString() : "";
//                                String brand = item.has("brand") && !(item.get("brand") instanceof JsonNull) ? item.get("brand").getAsString() : "";
//                                String mrp = item.has("mrp") && !(item.get("mrp") instanceof JsonNull) ? item.get("mrp").getAsString() : "";
////                                String currentStock = item.has("closing_stocks") && !(item.get("closing_stocks") instanceof JsonNull) ? item.get("closing_stocks").getAsString() : "";
//                                // Convert currentStock to a whole number string
//                                String currentStock = item.has("closing_stocks") && !(item.get("closing_stocks") instanceof JsonNull) ?
//                                        String.valueOf(Math.round(item.get("closing_stocks").getAsDouble())) : "0";
//                                String unit = item.has("unit") && !(item.get("unit") instanceof JsonNull) ? item.get("unit").getAsString() : "";
//                                String saleRate = item.has("sales_rate") && !(item.get("sales_rate") instanceof JsonNull) ? item.get("sales_rate").getAsString() : "";
//                                row.add(new ProductDTO(id, searchCode, productName, packing, barcode, brand, mrp, currentStock,
//                                        unit, saleRate));
//                            }
//                            tcSearchCode.setCellValueFactory(new PropertyValueFactory<>("searchCode"));
//                            tcProductName.setCellValueFactory(new PropertyValueFactory<>("productName"));
//                            tcPacking.setCellValueFactory(new PropertyValueFactory<>("packing"));
//                            tcBarcode.setCellValueFactory(new PropertyValueFactory<>("barcode"));
//                            tcBrand.setCellValueFactory(new PropertyValueFactory<>("brand"));
//                            tcMrp.setCellValueFactory(new PropertyValueFactory<>("mrp"));
//                            tcCurrentStock.setCellValueFactory(new PropertyValueFactory<>("currentStock"));
//                            tcUnit.setCellValueFactory(new PropertyValueFactory<>("unit"));
//                            tcSaleRate.setCellValueFactory(new PropertyValueFactory<>("saleRate"));
//                            tvProductList.setItems(row);
//                            //******************************** Highlight on the Created/Edited Record in the List Start ********************************
//                            if (ProductListController.isNewProductCreated) {
////                                tvProductList.getSelectionModel().selectLast();
////                                tvProductList.scrollTo(tvProductList.getItems().size() - 1);
//                                tvProductList.getSelectionModel().selectFirst();
//                                tvProductList.scrollTo(0);
//                                ProductListController.isNewProductCreated = false; // Reset the flag
//                            } else if (ProductListController.editedProductId != null) {
//                                for (TranxSelectedProduct product : row) {
//                                    if (product.getProductId().toString().equals(ProductListController.editedProductId)) {
//                                        tvProductList.getSelectionModel().select(product);
//                                        tvProductList.scrollTo(product);
//                                        ProductListController.editedProductId = null; // Reset the ID
//                                        break;
//                                    }
//                                }
//                            }
//                            //******************************** Highlight on the Created/Edited Record in the List End ********************************
//                        } else {
//                            lblInfo.setVisible(true);
//                            tvProductList.setVisible(false);
//                        }
//                    }
//                }
//            });
//            apiClient.setOnCancelled(new EventHandler<WorkerStateEvent>() {
//                @Override
//                public void handle(WorkerStateEvent workerStateEvent) {
//                    logger.error("Network API cancelled in getProductData()" + workerStateEvent.getSource().getValue().toString());
//                }
//            });
//            apiClient.setOnFailed(new EventHandler<WorkerStateEvent>() {
//                @Override
//                public void handle(WorkerStateEvent workerStateEvent) {
//                    logger.error("Network API failed in getProductData()" + workerStateEvent.getSource().getValue().toString());
//                }
//            });
//            apiClient.start();
//            logger.debug("Get Product Data End...");
//        } catch (Exception e) {
//            logger.error("exception:" + e);
//        } finally {
//            apiClient = null;
//        }
//    }

    private void productEditPage() {
        try {

            TranxSelectedProduct productDTO = tvProductList.getSelectionModel().getSelectedItem();
            //? Highlight
            ProductListController.editedProductId = String.valueOf(productDTO.getProductId());
            selectedPrId = Long.valueOf(productDTO.getProductId());
            GlobalController.getInstance().addTabStatic(FxmFileConstants.PRODUCT_EDIT_SLUG, false);
        } catch (Exception e) {

        }

    }

    private void productOpeningStockPage() {
        try {

            TranxSelectedProduct productDTO = tvProductList.getSelectionModel().getSelectedItem();
            //? Highlight
            ProductListController.editedProductId = String.valueOf(productDTO.getProductId());
            selectedPrId = Long.valueOf(productDTO.getProductId());
            GlobalController.getInstance().addTabStatic(FxmFileConstants.OPENING_STOCK_SLUG, false);
        } catch (Exception e) {

        }

    }

    public static Long getProductId() {
        return selectedPrId;
    }

    //todo: for showing the details of product after selection of product.
    private void tranxProductDetailsFun(String id) {
        //todo: activating the product tab
//        tranxPurOrderTabPane.getSelectionModel().select(tabPurOrderProduct);

        APIClient apiClient = null;
        try {
            Map<String, String> map = new HashMap<>();
            map.put("product_id", id);
            String formData = Globals.mapToStringforFormData(map);
            apiClient = new APIClient(EndPoints.TRANSACTION_PRODUCT_DETAILS, formData, RequestType.FORM_DATA);
            apiClient.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
                @Override
                public void handle(WorkerStateEvent workerStateEvent) {
                    jsonObject = new Gson().fromJson(workerStateEvent.getSource().getValue().toString(), JsonObject.class);
                    if (jsonObject.get("responseStatus").getAsInt() == 200) {
                        JsonObject productJsonObj = jsonObject.getAsJsonObject("result");
                        System.out.println("i am the response of Bottom data"+productJsonObj);
                        //todo: setting values
                        lblPrdLstMFG.setText(productJsonObj.get("group").getAsString());
                        lblPrdLstFormulation.setText(productJsonObj.get("subgroup").getAsString());
                        lblPrdLstPurRate.setText(productJsonObj.get("purchase_rate").getAsString());
                        lblPrdLstCtg.setText(productJsonObj.get("category").getAsString());
                        lblPrdLstHSN.setText(productJsonObj.get("hsn").getAsString());
                        lblPrdLstTaxType.setText(productJsonObj.get("tax_type").getAsString());
                        lblPrdLstTaxPer.setText(productJsonObj.get("tax_per").getAsString());
                        lblPrdLstMargPer.setText(productJsonObj.get("margin_per").getAsString());
                        lblPrdLstCost.setText(productJsonObj.get("cost").getAsString());
                        lblPrdLstShlfId.setText(productJsonObj.get("shelf_id").getAsString());
//                        lblPrdLstUnit.setText(productJsonObj.get("unit").getAsString());
//
//
////                        if (productJsonObj.has("unit") && !productJsonObj.get("unit").isJsonNull()) {
////                            lblPrdLstUnit.setText(productJsonObj.get("unit").getAsString());
////                        } else {
////                            lblPrdLstUnit.setText("N/A");
////                        }
//
//                        lblPrdLstfsrmh.setText(productJsonObj.get("fsrmh").getAsString());
//                        lblPrdLstfsrai.setText(productJsonObj.get("fsrai").getAsString());
//                        lblPrdLstcsrmh.setText(productJsonObj.get("csrmh").getAsString());
//                        lblPrdLstcsrai.setText(productJsonObj.get("csrai").getAsString());
                        double minStockValue = Double.parseDouble(productJsonObj.get("min_stocks").getAsString());
                        int minStockIntValue = (int) minStockValue;
                        lblPrdLstMinStk.setText(String.valueOf(minStockIntValue));
                        double maxStockValue = Double.parseDouble(productJsonObj.get("max_stocks").getAsString());
                        int maxStockIntValue = (int) maxStockValue;
                        lblPrdLstMaxStk.setText(String.valueOf(maxStockIntValue));
                    }
                }
            });
            apiClient.setOnCancelled(new EventHandler<WorkerStateEvent>() {
                @Override
                public void handle(WorkerStateEvent workerStateEvent) {
                    logger.error("Network API cancelled in tranxProductDetailsFun()" + workerStateEvent.getSource().getValue().toString());
                }
            });
            apiClient.setOnFailed(new EventHandler<WorkerStateEvent>() {
                @Override
                public void handle(WorkerStateEvent workerStateEvent) {
                    logger.error("Network API failed in tranxProductDetailsFun()" + workerStateEvent.getSource().getValue().toString());
                }
            });
            apiClient.start();
        } catch (Exception e) {
            logger.error("exception:" + e);
        } finally {
            apiClient = null;
        }


    }

    private void getProductBatchData(String id) {
        APIClient apiClient = null;
        try {
            logger.debug("Get Product Data Started...");
            Map<String, String> body = new HashMap<>();
            body.put("product_id", id);
            String requestBody = Globals.mapToStringforFormData(body);
//            HttpResponse<String> response = APIClient.postJsonRequest(requestBody, EndPoints.PRODUCT_LIST_ENDPOINT);
            apiClient = new APIClient(EndPoints.PRODUCT_BATCH_DETAILS, requestBody, RequestType.FORM_DATA);
            apiClient.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
                @Override
                public void handle(WorkerStateEvent workerStateEvent) {
                    jsonObject = new Gson().fromJson(workerStateEvent.getSource().getValue().toString(), JsonObject.class);
                    if (!jsonObject.isJsonNull() && jsonObject.get("responseStatus").getAsInt() == 200) {
                        tvPrdLstBatchHistory.getItems().clear();
                        JsonArray responseArray = jsonObject.get("response").getAsJsonArray();
                        if (responseArray.size() > 0) {
                            tvPrdLstBatchHistory.setVisible(true);
                            for (JsonElement mElement : responseArray) {
                                JsonObject item = mElement.getAsJsonObject();
                                String batch_no = item.has("batch_no") && !(item.get("batch_no") instanceof JsonNull) ? item.get("batch_no").getAsString() : "";
                                String manufacturingDate = item.has("manufacturing_date") && !(item.get("manufacturing_date") instanceof JsonNull) ? item.get("manufacturing_date").getAsString() : "";
                                String expiryDate = item.has("expiry_date") && !(item.get("expiry_date") instanceof JsonNull) ? item.get("expiry_date").getAsString() : "";
                                String closingStock = item.has("closing_stock") && !(item.get("closing_stock") instanceof JsonNull) ? item.get("closing_stock").getAsString() : "";
                                String mrp = item.has("mrp") && !(item.get("mrp") instanceof JsonNull) ? item.get("mrp").getAsString() : "";
                                String purchaseRate = item.has("purchase_rate") && !(item.get("purchase_rate") instanceof JsonNull) ? item.get("purchase_rate").getAsString() : "";
                                String disPer = item.has("dis_per") && !(item.get("dis_per") instanceof JsonNull) ? item.get("dis_per").getAsString() : "";
                                String disAmt = item.has("dis_amt") && !(item.get("dis_amt") instanceof JsonNull) ? item.get("dis_amt").getAsString() : "";
                                String cessPer = item.has("cess_per") && !(item.get("cess_per") instanceof JsonNull) ? item.get("cess_per").getAsString() : "";
                                String cessAmt = item.has("cess_amt") && !(item.get("cess_amt") instanceof JsonNull) ? item.get("cess_amt").getAsString() : "";
                                String barcode = item.has("barcode") && !(item.get("barcode") instanceof JsonNull) ? item.get("barcode").getAsString() : "";
                                String marginPer = item.has("min_margin") && !(item.get("min_margin") instanceof JsonNull) ? item.get("min_margin").getAsString() : "";
                                String totalAmount = item.has("totalAmount") && !(item.get("totalAmount") instanceof JsonNull) ? item.get("totalAmount").getAsString() : "";
                                String FSR = item.has("FSR") && !(item.get("FSR") instanceof JsonNull) ? item.get("FSR").getAsString() : "";
                                String CSR = item.has("CSR") && !(item.get("CSR") instanceof JsonNull) ? item.get("CSR").getAsString() : "";
                                String saleRate = item.has("saleRate") && !(item.get("saleRate") instanceof JsonNull) ? item.get("saleRate").getAsString() : "";
                                histrow.add(new ProductListBatchHistDTO(batch_no, manufacturingDate, expiryDate, closingStock, mrp, purchaseRate, disPer,
                                        disAmt, cessPer, cessAmt, barcode, marginPer, totalAmount, FSR, CSR, saleRate));
                            }
                            tcPrdLstBtachNo.setCellValueFactory(new PropertyValueFactory<>("batchNo"));
                            tcPrdLstMFGDate.setCellValueFactory(new PropertyValueFactory<>("manufacturingDate"));
                            tcPrdLstExpDate.setCellValueFactory(new PropertyValueFactory<>("expiryDate"));
                            tcPrdLstAvlStk.setCellValueFactory(new PropertyValueFactory<>("closingStock"));
                            tcPrdLstMRP.setCellValueFactory(new PropertyValueFactory<>("mrp"));
                            tcPrdLstPurRate.setCellValueFactory(new PropertyValueFactory<>("purchaseRate"));
                            tcPrdLstDisPer.setCellValueFactory(new PropertyValueFactory<>("disPer"));
                            tcPrdLstDisAmt.setCellValueFactory(new PropertyValueFactory<>("disAmt"));
                            tcPrdLstCessPer.setCellValueFactory(new PropertyValueFactory<>("cessPer"));
                            tcPrdLstCessAmt.setCellValueFactory(new PropertyValueFactory<>("cessAmt"));
                            tcPrdLstBarcode.setCellValueFactory(new PropertyValueFactory<>("barcode"));
                            tcPrdLstMarginPer.setCellValueFactory(new PropertyValueFactory<>("marginPer"));
//                            tcPrdLstTotalAmount.setCellValueFactory(new PropertyValueFactory<>("totalAmount"));
//                            tcPrdLstFSR.setCellValueFactory(new PropertyValueFactory<>("FSR"));
//                            tcPrdLstFSRAI.setCellValueFactory(new PropertyValueFactory<>("FSR"));
//                            tcPrdLstCSR.setCellValueFactory(new PropertyValueFactory<>("CSR"));
//                            tcPrdLstCSRAI.setCellValueFactory(new PropertyValueFactory<>("CSR"));
//                            tcPrdLstSalesRate.setCellValueFactory(new PropertyValueFactory<>("saleRate"));
                            tvPrdLstBatchHistory.setItems(histrow);
                        }
                    }
                }
            });
            apiClient.setOnCancelled(new EventHandler<WorkerStateEvent>() {
                @Override
                public void handle(WorkerStateEvent workerStateEvent) {
                    logger.error("Network API cancelled in getProductData()" + workerStateEvent.getSource().getValue().toString());
                }
            });
            apiClient.setOnFailed(new EventHandler<WorkerStateEvent>() {
                @Override
                public void handle(WorkerStateEvent workerStateEvent) {
                    logger.error("Network API failed in getProductData()" + workerStateEvent.getSource().getValue().toString());
                }
            });
            apiClient.start();
            logger.debug("Get Product Data End...");
        } catch (Exception e) {
            logger.error("exception:" + e);
        } finally {
            apiClient = null;
        }
    }
    //***********************************************************************Delete Product START*****************************************************
    private void handleDeleteProduct() {
        System.out.println("i am in");
        TranxSelectedProduct selectedItem = tvProductList.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            System.out.println("i am in selectedItem"+selectedItem);
            id = String.valueOf(selectedItem.getProductId());
            System.out.println("i am in id"+id);
            deleteProduct();
        }
    }

    private void deleteProduct() {

        int index  =  tvProductList.getSelectionModel().getFocusedIndex();
        AlertUtility.CustomCallback callback = number -> {
            if (number == 1) {
                APIClient apiClient = null;
                Map<String, String> map = new HashMap<>();
                map.put("id", id);
                map.put("source","");
                HttpResponse<String> response;
                String formData = Globals.mapToStringforFormData(map);
                System.out.println("i am in formData"+formData);
                apiClient = new APIClient(EndPoints.DELETE_PRODUCT_ENDPOINT, formData, RequestType.FORM_DATA);
                apiClient.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
                    @Override
                    public void handle(WorkerStateEvent workerStateEvent) {
                        // JsonObject responseBody = new Gson().fromJson(response.body(), JsonObject.class);
                        jsonObject = new Gson().fromJson(workerStateEvent.getSource().getValue().toString(), JsonObject.class);
                        System.out.println("jsonOnject"+jsonObject);
                        String message="";
                        message = jsonObject.get("message").getAsString();

                        if (jsonObject.get("responseStatus").getAsInt() == 409) {
                            AlertUtility.AlertErrorTimeout(AlertUtility.alertTypeError, message, input -> {
                                //  getAllTranxProductList("","");
                                //    ProductListController.editedProductId = String.valueOf(id); //? Set the ID for editing
                                tvProductList.getSelectionModel().select(index);
                                tvProductList.scrollTo(index);
                                id="";
                            });
//                                AlertUtility.AlertSuccess(stage2, "Success", message, callback);
//                                tfPaymentCreateMode.requestFocus();
                        }

                        if (jsonObject.get("responseStatus").getAsInt() == 200) {
                            AlertUtility.AlertSuccessTimeout(AlertUtility.alertTypeSuccess, message, input -> {
                                //getAllTranxProductList("","");
                                // tfSearch.requestFocus();
                                tvProductList.getSelectionModel().select(index);
                                // tvProductList.scrollTo(index);
                                id="";
                            });
//                                AlertUtility.AlertSuccess(stage2, "Success", message, callback);
//                                tfPaymentCreateMode.requestFocus();
                        } else {
//                                AlertUtility.AlertError(stage2, AlertUtility.alertTypeError, message, callback);
                            AlertUtility.AlertErrorTimeout(AlertUtility.alertTypeError,message , in -> {
//                                    tftranxPurChallLstSearchField.requestFocus();
                            });
                        }
                        //   getProductData("");
                        getAllTranxProductList("","");
                        tfSearch.setText("");

                    }
                });

                apiClient.setOnCancelled(new EventHandler<WorkerStateEvent>() {
                    @Override
                    public void handle(WorkerStateEvent workerStateEvent) {
                        logger.error("Network API cancelled in deletePaymentMode()" + workerStateEvent.getSource().getValue().toString());

                    }
                });
                apiClient.setOnFailed(new EventHandler<WorkerStateEvent>() {
                    @Override
                    public void handle(WorkerStateEvent workerStateEvent) {
                        logger.error("Network API failed in deletePaymentMode()" + workerStateEvent.getSource().getValue().toString());

                    }
                });
                apiClient.start();
                logger.debug("create payment mode data end ...");
            } else {
            }
        };
        AlertUtility.AlertConfirmation(AlertUtility.alertTypeConfirmation, "Do you want to Delete", callback);
    }

    private ObservableList<TranxSelectedProduct> getAllTranxProductList(String searchQuery, String barcodeQuery) {
        lstProducts.clear();
        try {
            Map<String, String> body = new HashMap<>();
            body.put("search", searchQuery);
            body.put("barcode", barcodeQuery);
            String formData = Globals.mapToStringforFormData(body);
            HttpResponse<String> response = APIClient.postFormDataRequest(formData, EndPoints.GET_TRANX_PRODUCT_LIST);
            JsonObject jsonObject = new Gson().fromJson(response.body(), JsonObject.class);
            if (jsonObject.get("responseStatus").getAsInt() == 200) {
                JsonArray responseArray = jsonObject.get("list").getAsJsonArray();
                if (responseArray.size() > 0) {
                    for (JsonElement element : responseArray) {
                        JsonObject jobj = new Gson().fromJson(element, JsonObject.class);
                        JsonArray jarr = jobj.get("unit_lst").getAsJsonArray();
                        String unit1Name = "";
                        Integer unit1Id = 0;
                        Double unit1Mrp = 0.0;
                        Double unit1PurchaseRate = 0.0;
                        Double unit1FSRMH = 0.0;
                        Double unit1FSRAI = 0.0;
                        Double unit1CSRMH = 0.0;
                        Double unit1CSRAI = 0.0;
                        Double unit1Conv = 0.0;
                        Double unit1ClsStock = 0.0;
                        Double unit1ActualStock = 0.0;
                        Boolean unit1IsNegetive = false;
                        if (jarr.size() >= 1) {
                            JsonObject unit1Obj = jarr.get(0).getAsJsonObject();
                            unit1Id = unit1Obj.get("unitid").getAsInt();
                            unit1Name = unit1Obj.get("unitName").getAsString();
                            unit1Mrp =  unit1Obj.get("mrp").getAsDouble();
                            unit1PurchaseRate =  unit1Obj.get("purchaserate").getAsDouble();
                            unit1FSRMH = unit1Obj.get("fsrmh").getAsDouble();
                            unit1FSRAI = unit1Obj.get("fsrai").getAsDouble();
                            unit1CSRMH = unit1Obj.get("csrmh").getAsDouble();
                            unit1CSRAI = unit1Obj.get("csrai").getAsDouble();
                            unit1Conv = unit1Obj.get("unitConv").getAsDouble();
                            unit1ClsStock = unit1Obj.get("closingstk").getAsDouble();
                            unit1ActualStock = unit1Obj.get("actstkcheck").getAsDouble();
                            unit1IsNegetive = unit1Obj.get("is_negetive").getAsBoolean();
                        }
                        String unit2Name = "";
                        Integer unit2Id = 0;
                        Double unit2Mrp = 0.0;
                        Double unit2PurchaseRate = 0.0;
                        Double unit2FSRMH = 0.0;
                        Double unit2FSRAI = 0.0;
                        Double unit2CSRMH = 0.0;
                        Double unit2CSRAI = 0.0;
                        Double unit2Conv = 0.0;
                        Double unit2ClsStock = 0.0;
                        Double unit2ActualStock = 0.0;
                        Boolean unit2IsNegetive = false;
                        if (jarr.size() >= 2) {
                            JsonObject unit2Obj = jarr.get(1).getAsJsonObject();
                            unit2Id = unit2Obj.get("unitid").getAsInt();
                            unit2Name = unit2Obj.get("unitName").getAsString();
                            unit2Mrp =  unit2Obj.get("mrp").getAsDouble();
                            unit2PurchaseRate =  unit2Obj.get("purchaserate").getAsDouble();
                            unit2FSRMH = unit2Obj.get("fsrmh").getAsDouble();
                            unit2FSRAI = unit2Obj.get("fsrai").getAsDouble();
                            unit2CSRMH = unit2Obj.get("csrmh").getAsDouble();
                            unit2CSRAI = unit2Obj.get("csrai").getAsDouble();
                            unit2Conv = unit2Obj.get("unitConv").getAsDouble();
                            unit2ClsStock = unit2Obj.get("closingstk").getAsDouble();
                            unit2ActualStock = unit2Obj.get("actstkcheck").getAsDouble();
                            unit2IsNegetive = unit2Obj.get("is_negetive").getAsBoolean();
                        }
                        String unit3Name = "";
                        Integer unit3Id = 0;
                        Double unit3FSRMH = 0.0;
                        Double unit3Mrp = 0.0;
                        Double unit3PurchaseRate = 0.0;
                        Double unit3FSRAI = 0.0;
                        Double unit3CSRMH = 0.0;
                        Double unit3CSRAI = 0.0;
                        Double unit3Conv = 0.0;
                        Double unit3ClsStock = 0.0;
                        Double unit3ActualStock = 0.0;
                        Boolean unit3IsNegetive = false;
                        if (jarr.size() >= 3) {
                            JsonObject unit3Obj = jarr.get(2).getAsJsonObject();
                            unit3Name = unit3Obj.get("unitName").getAsString();
                            unit3Id = unit3Obj.get("unitid").getAsInt();
                            unit3Mrp =  unit3Obj.get("mrp").getAsDouble();
                            unit3PurchaseRate =  unit3Obj.get("purchaserate").getAsDouble();
                            unit3FSRMH = unit3Obj.get("fsrmh").getAsDouble();
                            unit3FSRAI = unit3Obj.get("fsrai").getAsDouble();
                            unit3CSRMH = unit3Obj.get("csrmh").getAsDouble();
                            unit3CSRAI = unit3Obj.get("csrai").getAsDouble();
                            unit3Conv = unit3Obj.get("unitConv").getAsDouble();
                            unit3ClsStock = unit3Obj.get("closingstk").getAsDouble();
                            unit3ActualStock = unit3Obj.get("actstkcheck").getAsDouble();
                            unit3IsNegetive = unit3Obj.get("is_negetive").getAsBoolean();

                        }


                        lstProducts.add(new TranxSelectedProduct(jobj.get("id").getAsInt(), jobj.get("product_name").getAsString(), jobj.get("igst").getAsDouble(), jobj.get("cgst").getAsDouble(), jobj.get("sgst").getAsDouble(), 0, jobj.get("packing").getAsString(),
                                jobj.get("code").getAsString(), jobj.get("barcode").getAsString(), jobj.get("is_negative").getAsBoolean(), jobj.get("is_batch").getAsBoolean(), jobj.get("is_serial").getAsBoolean(), 0.0, 0.0, 0.0, jobj.get("current_stock").getAsDouble(),
                                false, false, false, 0, "", 0, "", 0, "", jobj.get("brand").getAsString(), jobj.get("unit").getAsString(), 0.0, jobj.get("drugType").getAsString(),0.0,
                                unit1Id,unit1Name,unit1Mrp,unit1PurchaseRate,unit1FSRMH, unit1FSRAI, unit1CSRMH, unit1CSRAI, unit1Conv, unit1ClsStock, unit1ActualStock,unit1IsNegetive, unit2Id,unit2Name,unit2Mrp,unit2PurchaseRate, unit2FSRMH, unit2FSRAI, unit2CSRMH, unit2CSRAI, unit2Conv, unit2ClsStock, unit2ActualStock, unit2IsNegetive,unit3Id,unit3Name, unit3Mrp,unit3PurchaseRate, unit3FSRMH,
                                unit3FSRAI, unit3CSRMH, unit3CSRAI, unit3Conv, unit3ClsStock, unit3ActualStock,unit3IsNegetive));
                    }

                    tvProductList.setItems(lstProducts);

                    //******************************** Highlight on the Created/Edited Record in the List Start ********************************
                    if (ProductListController.isNewProductCreated) {
//                                tvProductList.getSelectionModel().selectLast();
//                                tvProductList.scrollTo(tvProductList.getItems().size() - 1);
                        tvProductList.getSelectionModel().selectFirst();
                        tvProductList.scrollTo(0);
                        ProductListController.isNewProductCreated = false; // Reset the flag
                    } else if (ProductListController.editedProductId != null) {
                        for (TranxSelectedProduct product : lstProducts) {
                            if (product.getProductId().toString().equals(ProductListController.editedProductId)) {
                                tvProductList.getSelectionModel().select(product);
                                tvProductList.scrollTo(product);
                                ProductListController.editedProductId = null; // Reset the ID
                                break;
                            }
                        }
                    }
//                            //******************************** Highlight on the Created/Edited Record in the List End ********************************
                } else {
                    System.out.println("responseObject is null");
                }
            } else {
                System.out.println("Error in response");
            }
        } catch (Exception e) {
            System.out.println("Exception in product List:"+e.getMessage());
        }

        return lstProducts;
    }

}
