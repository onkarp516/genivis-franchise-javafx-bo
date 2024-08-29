package com.opethic.genivis.controller.master;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.opethic.genivis.dto.opening.*;
import com.opethic.genivis.network.APIClient;
import com.opethic.genivis.utils.AlertUtility;
import com.opethic.genivis.utils.Globals;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;

import java.net.URL;
import java.net.http.HttpResponse;
import java.util.*;

public class OpeningStock implements Initializable {


    @FXML
    public Label lbProduct,lbUnit,lbBatch,lbOpeningQty,lbFreeQty,lbMRP,lbPurRate,lbCosting,lbSaleRate,lbMfgDate,lbExpDate;

    @FXML
    public ComboBox<UnitForOpeining> cbUnit;

    @FXML
    public TextField tfProduct,tfBatch,tfOpeningQty,tfFreeQty,tfMRP,tfPurRate,tfCosting,tfSaleRate,tfMfgDate,tfExpDate;

    @FXML
    public TableView<ProAndUnisTable> tbOpeningStock;

    @FXML
    public TableColumn<ProAndUnisTable,String> tcUnit;

    @FXML
    public TableColumn<ProAndUnisTable,String> tcBatch;

    @FXML
    public TableColumn<ProAndUnisTable,String> tcOpeningQty;

    @FXML
    public TableColumn<ProAndUnisTable,String> tcFreeQty;

    @FXML
    public TableColumn<ProAndUnisTable,String> tcMRP;


    @FXML
    public TableColumn<ProAndUnisTable,String> tcPurRate;


    @FXML
    public TableColumn<ProAndUnisTable,String> tcCosting;

    @FXML
    public TableColumn<ProAndUnisTable,String> tcSaleRate;

    @FXML
    public TableColumn<ProAndUnisTable,String> tcMfgDate;


    @FXML
    public TableColumn<ProAndUnisTable,String> tcExpireDate;



    ObservableList<UnitForOpeining> unitList = FXCollections.observableArrayList();

    ObservableList<ProAndUnisTable> proAndUnisTablesList = FXCollections.observableArrayList();
    private Long productId = 0L;

    Map<String,String> editMap = new HashMap<>();

    Long unit_id = 0L;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        productId = ProductListController.getProductId();
        getProAndUni(productId);
        getProAndUniList(productId);

        Platform.runLater(()->{
            cbUnit.requestFocus();
        });

        cbUnit.setItems(unitList);
        tfProduct.setText(editMap.get("product_name"));

        cbUnit.setOnKeyPressed(event->{
            if(event.getCode() == KeyCode.DOWN){
                cbUnit.show();
            }
        });

        tfProduct.setEditable(false);
        tfProduct.setFocusTraversable(false);


        tableInitilization();


    }

    private void tableInitilization() {
        tbOpeningStock.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);
        tcUnit.setCellValueFactory(cellData -> cellData.getValue().unitNameProperty());
        tcUnit.setStyle("-fx-alignment: CENTER;");

        tcBatch.setCellValueFactory(cellData -> cellData.getValue().batchNoProperty());
        tcBatch.setStyle("-fx-alignment: CENTER;");

        tcOpeningQty.setCellValueFactory(cellData -> cellData.getValue().openingQtyProperty());
        tcOpeningQty.setStyle("-fx-alignment: CENTER-RIGHT;");

        tcFreeQty.setCellValueFactory(cellData -> cellData.getValue().freeQtyProperty());
        tcFreeQty.setStyle("-fx-alignment: CENTER-RIGHT;");

        tcMRP.setCellValueFactory(cellData -> cellData.getValue().mrpProperty());
        tcMRP.setStyle("-fx-alignment: CENTER-RIGHT;");

        tcPurRate.setCellValueFactory(cellData -> cellData.getValue().purchaseRateProperty());
        tcPurRate.setStyle("-fx-alignment: CENTER-RIGHT;");

        tcCosting.setCellValueFactory(cellData -> cellData.getValue().costingProperty());
        tcCosting.setStyle("-fx-alignment: CENTER-RIGHT;");

        tcSaleRate.setCellValueFactory(cellData -> cellData.getValue().saleRateProperty());
        tcSaleRate.setStyle("-fx-alignment: CENTER-RIGHT;");

        tcMfgDate.setCellValueFactory(cellData -> cellData.getValue().mfgDateProperty());
        tcMfgDate.setStyle("-fx-alignment: CENTER;");

        tcExpireDate.setCellValueFactory(cellData -> cellData.getValue().expireDateProperty());
        tcExpireDate.setStyle("-fx-alignment: CENTER;");

    }

    public void getProAndUniList(Long id) {
        proAndUnisTablesList.clear();

        try {
            Map<String, String> map = new HashMap<>();
            map.put("id", String.valueOf(id));
            String formData = Globals.mapToStringforFormData(map);

            HttpResponse<String> response = APIClient.postFormDataRequest(formData, "get_pro_and_unis");
            ProAndUnisListRes res = new Gson().fromJson(response.body(), ProAndUnisListRes.class);

            System.out.println("List: "+res);

            List<ProAndUnisList> proAndUnisList = new ArrayList<>();
            proAndUnisList.addAll(res.getList());

            if (res.getResponseStatus() == 200) {
                for(ProAndUnisList proAndUnisList1: proAndUnisList){
                    ProAndUnisTable proAndUnisTable = new ProAndUnisTable();

                    proAndUnisTable.setProductId(proAndUnisList1.getProductId() == null ? "": proAndUnisList1.getProductId().toString());
                    proAndUnisTable.setProductName(proAndUnisList1.getProductName() == null ? "": proAndUnisList1.getProductName());
                    proAndUnisTable.setUnitId(proAndUnisList1.getUnitId() == null ? "": proAndUnisList1.getUnitId().toString());
                    proAndUnisTable.setUnitName(proAndUnisList1.getUnitName() == null ? "": proAndUnisList1.getUnitName());
                    proAndUnisTable.setBatchId(proAndUnisList1.getBatchId()  == null ? "": proAndUnisList1.getBatchId().toString());
                    proAndUnisTable.setBatchNo(proAndUnisList1.getBatchNo() == null ? "": proAndUnisList1.getBatchNo());
                    proAndUnisTable.setMrp(proAndUnisList1.getMrp() == null ? "": proAndUnisList1.getMrp().toString());
                    proAndUnisTable.setPurchaseRate(proAndUnisList1.getPurchaseRate() == null ? "": proAndUnisList1.getPurchaseRate().toString());
                    proAndUnisTable.setSaleRate(proAndUnisList1.getSaleRate() == null ? "": proAndUnisList1.getSaleRate().toString());
                    proAndUnisTable.setFreeQty(proAndUnisList1.getFreeQty() == null ? "": proAndUnisList1.getFreeQty().toString());
                    proAndUnisTable.setMfgDate(proAndUnisList1.getMfgDate() == null ? "": proAndUnisList1.getMfgDate());
                    proAndUnisTable.setExpireDate(proAndUnisList1.getExpireDate() == null ? "": proAndUnisList1.getExpireDate());
                    proAndUnisTable.setOpeningQty(proAndUnisList1.getOpeningQty() == null ? "": proAndUnisList1.getOpeningQty().toString());
                    proAndUnisTable.setCosting(proAndUnisList1.getCosting() == null ? "": proAndUnisList1.getCosting().toString());
                    proAndUnisTablesList.add(proAndUnisTable);
                    tbOpeningStock.setItems(proAndUnisTablesList);
                }


            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void getProAndUni(Long id) {

        try {
            Map<String, String> map = new HashMap<>();
            map.put("id", String.valueOf(id));
            String formData = Globals.mapToStringforFormData(map);

            HttpResponse<String> response = APIClient.postFormDataRequest(formData, "get_product_info");
            ProAndUniRes res = new Gson().fromJson(response.body(), ProAndUniRes.class);

            if (res.getResponseStatus() == 200) {
                editMap.put("product_id",res.getProductForOpening().getProduct_id().toString());
                editMap.put("product_name",res.getProductForOpening().getProduct_name().toString());
                unitList.addAll(res.getUnitForOpeining());
            }

            System.out.println("map : "+editMap);
            System.out.println("unitList : "+unitList);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void createProAndUni() {

        try {
            Map<String, String> map = new HashMap<>();
            map.put("product_id",editMap.get("product_id"));
            map.put("unit_id", String.valueOf(unit_id));
            map.put("batch_no", tfBatch.getText());
            map.put("mrp", tfMRP.getText());
            map.put("purchase_rate", tfPurRate.getText());
            map.put("sale_rate", tfSaleRate.getText());
            map.put("free_qty", tfFreeQty.getText());
            map.put("mfg_date",tfMfgDate.getText());
            map.put("expire_date", tfExpDate.getText());
            map.put("opening_qty", tfOpeningQty.getText());
            map.put("costing", tfCosting.getText());

            String formData = Globals.mapToStringforFormData(map);

            HttpResponse<String> response = APIClient.postFormDataRequest(formData, "create_product_open_stock");

            String responseBody = response.body();
            JsonObject jsonResponse = new Gson().fromJson(responseBody, JsonObject.class);

            String message = jsonResponse.get("message").getAsString();
            int responseStatus = jsonResponse.get("responseStatus").getAsInt();

            if (responseStatus == 200) {
                AlertUtility.AlertSuccessTimeout(AlertUtility.alertTypeSuccess, message, input -> {
                    cbUnit.requestFocus();
                    getProAndUniList(productId);
                });
            }


        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    public void handleCbUnit(ActionEvent actionEvent) {
        Object[] pack = new Object[1];
        pack[0] = cbUnit.getSelectionModel().getSelectedItem();
        if (pack[0] != null) {

            for (UnitForOpeining unitForOpeining : unitList) {
                if (pack[0].equals(unitForOpeining)) {
                    unit_id = Long.valueOf(unitForOpeining.getId());
                }
            }
        }
    }

    public void onClickSubmit(ActionEvent actionEvent) {
        createProAndUni();
    }

    public void onClickCancel(ActionEvent actionEvent) {
    }
}
