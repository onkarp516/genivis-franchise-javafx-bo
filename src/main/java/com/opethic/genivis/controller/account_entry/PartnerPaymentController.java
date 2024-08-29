package com.opethic.genivis.controller.account_entry;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.opethic.genivis.dto.PurchaseOrderDTO;
import com.opethic.genivis.dto.account_entry.PartnerPaymentDTO;
import com.opethic.genivis.dto.account_entry.PartnerPaymentModel;
import com.opethic.genivis.network.APIClient;
import com.opethic.genivis.network.EndPoints;
import com.opethic.genivis.network.RequestType;
import com.opethic.genivis.utils.DateValidator;
import com.opethic.genivis.utils.Globals;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableArray;
import javafx.collections.ObservableList;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.util.Callback;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Objects;
import java.util.ResourceBundle;

public class PartnerPaymentController implements Initializable {
    @FXML
    public TextField tfPPSearchLedger;
    @FXML
    public TextField tfPPFromDate;
    @FXML
    public TextField tfPPTodate;
    @FXML
    public Button btnPPSubmit;
    @FXML
    public TableView tblvpartnerpayment;
    @FXML
    public TableColumn tblcPPSelect,tblcPPFranchiseName,tblcPPSalesDate,tblcPPSalesInvoiceNumber,tblcPPSalesAmount,tblcPPTaxAmount,tblcPPPartnerName,tblcPPDesignation,tblcPPTaxableAmount,tblcPPIncentive,tblcPPTds,tblcPPPayment;
    LocalDate monthBegin,monthEnd;
    String startDate,endDate;
    ObservableList<PartnerPaymentModel> partnerPaymentList;
    DateTimeFormatter dmyFormatter;
    DateTimeFormatter ymdFormatter;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        dmyFormatter=DateTimeFormatter.ofPattern("dd/MM/yyyy");
        ymdFormatter=DateTimeFormatter.ofPattern("yyyy/MM/dd");

        tblvpartnerpayment.setEditable(true);
        DateValidator.applyDateFormat(tfPPFromDate);
        DateValidator.applyDateFormat(tfPPTodate);

        monthBegin = LocalDate.now().withDayOfMonth(1);
        monthEnd = LocalDate.now().plusMonths(1).withDayOfMonth(1).minusDays(1);

        startDate=monthBegin.format(dmyFormatter);
        endDate=monthEnd.format(dmyFormatter);

//        System.out.println("monthBegin:"+startDate);
//        System.out.println("monthEnd:"+endDate);


        tfPPFromDate.setText(startDate);
        tfPPTodate.setText(endDate);
//        System.out.println("monthEnd:"+(simpleDateFormat.format(monthEnd)));

//        tfPPFromDate.setText(simpleDateFormat.format(monthBegin));
//        tfPPTodate.setText(simpleDateFormat.format(monthEnd));
        partnerPaymentList= FXCollections.observableArrayList();

        Platform.runLater(()->{
            initialConfig();
        });

        tblcPPSelect.setCellFactory(column -> new CheckBoxTableCell<>());
        tblcPPSelect.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<PartnerPaymentModel, Boolean>, ObservableValue<Boolean>>() {
            @Override
            public ObservableValue<Boolean> call(TableColumn.CellDataFeatures<PartnerPaymentModel, Boolean> param) {
                PartnerPaymentModel pod=param.getValue();
                BooleanProperty booleanProperty=new SimpleBooleanProperty(pod.selectProperty().get());
                booleanProperty.addListener(new ChangeListener<Boolean>() {
                    @Override
                    public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                        pod.setSelect(newValue);

                        if(newValue){
                            System.out.println("POD1=>"+newValue);
//                            pod.setTax_amt(pod.getTaxable_amt());
//                            pod.setTotal_amount("0.00");
                            //findOutSelectedRow();
                        }else{
                            System.out.println("POD2=>"+newValue);
//                            pod.setTax_amt("0.00");
//                            pod.setTotal_amount(pod.getTaxable_amt());
                        }
                        //findOutSelectedRow();
                    }
                });
                return booleanProperty;
            }
        });



        btnPPSubmit.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                System.out.println("Button Pressed");
            }
        });
    }

    private void updateTableViewData() {
        tblcPPFranchiseName.setCellValueFactory(new PropertyValueFactory<>("franchise_name"));
        tblcPPSalesDate.setCellValueFactory(new PropertyValueFactory<>("sales_date"));
        tblcPPSalesAmount.setCellValueFactory(new PropertyValueFactory<>("sales_invoice_amt"));
        tblcPPSalesInvoiceNumber.setCellValueFactory(new PropertyValueFactory<>("sales_invoice_no"));
        tblcPPTaxAmount.setCellValueFactory(new PropertyValueFactory<>("sales_tax_amt"));
        tblcPPPartnerName.setCellValueFactory(new PropertyValueFactory<>("partner_name"));
        tblcPPDesignation.setCellValueFactory(new PropertyValueFactory<>("designation"));
        tblcPPTaxableAmount.setCellValueFactory(new PropertyValueFactory<>("taxable_amt"));
        tblcPPIncentive.setCellValueFactory(new PropertyValueFactory<>("incentive_amt"));
        tblcPPTds.setCellValueFactory(new PropertyValueFactory<>("tds"));
        tblcPPPayment.setCellValueFactory(new PropertyValueFactory<>("payment"));

        tblvpartnerpayment.setItems(partnerPaymentList);
        tblvpartnerpayment.refresh();
    }

    private void initialConfig() {
        tfPPSearchLedger.requestFocus();
        nextElementFocus();
        getData(monthBegin.toString(),monthEnd.toString());
    }

    private void getData(String sDate,String eDate) {
        HashMap<String, String> req=new HashMap<>();
        req.put("fromDate",sDate);
        req.put("toDate",eDate);

        String request = Globals.mapToStringforFormData(req);
        APIClient apiClient=new APIClient(EndPoints.GET_PARTNER_COMMISSION_PAYMENT,request, RequestType.FORM_DATA);
        apiClient.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent event) {
                if(event.getSource().getValue()!=null){
//                    System.out.println("Response => "+event.getSource().getValue());
                    JsonObject jObject=new Gson().fromJson(event.getSource().getValue().toString(), JsonObject.class);
                    if(jObject.get("responseStatus").getAsInt()==200){
                        JsonArray list=jObject.get("list").getAsJsonArray();
                        for(int i=0;i<list.size();i++){
                            JsonObject jsonObject=list.get(i).getAsJsonObject();
                            PartnerPaymentModel partnerPaymentModel=new PartnerPaymentModel();
                            partnerPaymentModel.setSelect(false);
                            partnerPaymentModel.setFranchise_name(jsonObject.get("franchise_name").getAsString());
                            partnerPaymentModel.setSales_date(jsonObject.get("sales_date").getAsString());
                            partnerPaymentModel.setSales_invoice_no(jsonObject.get("sales_invoice_no").getAsString());
                            partnerPaymentModel.setSales_invoice_amt(jsonObject.get("sales_invoice_amt").getAsDouble());
                            partnerPaymentModel.setSales_tax_amt(jsonObject.get("sales_tax_amt").getAsDouble());
                            partnerPaymentModel.setPartner_name(jsonObject.get("partner_name").getAsString());
                            partnerPaymentModel.setDesignation(jsonObject.get("designation").getAsString());
                            partnerPaymentModel.setTaxable_amt(jsonObject.get("taxable_amt").getAsDouble());
                            partnerPaymentModel.setIncentive_amt(jsonObject.get("incentive_amt").getAsDouble());
                            partnerPaymentModel.setTds(jsonObject.get("tds").getAsDouble());
                            partnerPaymentModel.setPayment(jsonObject.get("payment").getAsDouble());

                            partnerPaymentList.add(partnerPaymentModel);
                        }
                    }else{

                    }
                    updateTableViewData();
                }
            }
        });
        apiClient.setOnFailed(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent event) {
                System.out.println("Event Failed => "+event.getSource().getValue());
            }
        });
        apiClient.setOnCancelled(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent event) {
                System.out.println("OnCancelled => "+event.getSource().getValue());
            }
        });
        apiClient.start();

    }

    public void nextElementFocus() {
        nodeNextElement(tfPPSearchLedger, tfPPFromDate);
        nodeNextElement(tfPPFromDate, tfPPTodate);
        nodeNextElement(tfPPTodate, btnPPSubmit);
    }
    private void nodeNextElement(Node current_node, Node next_node) {
        current_node.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                if (current_node instanceof Button) {
                    ((Button)current_node).fire();
                }else{
                    next_node.requestFocus();
                }
            }
        });
    }
}
