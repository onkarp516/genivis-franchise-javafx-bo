package com.opethic.genivis.controller.master.ledger;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.opethic.genivis.GenivisApplication;
import com.opethic.genivis.dto.MasterLedgerReportDTO;
import com.opethic.genivis.dto.master.ledger.LedgerListDTO;
import com.opethic.genivis.network.APIClient;
import com.opethic.genivis.network.EndPoints;
import com.opethic.genivis.utils.GlobalController;
import com.opethic.genivis.utils.Globals;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import static com.opethic.genivis.utils.FxmFileConstants.*;

public class MasterLedgerTranxReportController implements Initializable {
    @FXML
    private TextField tfFromAmt, tfToAmt;
    @FXML
    private DatePicker fromDateSelected, toDateSelected;

    @FXML
    private TableColumn<MasterLedgerReportDTO, Void> tcAction;
    @FXML
    private Label lblTotalOpeningDebit, lblTotalOpeningCredit, lblCurrentTotalDebit, lblCurrentTotalCredit, lblClosingTotalDebit, lblClosingTotalCredit;
    private static final Logger MasterLedgerReportListLogger = LoggerFactory.getLogger(MasterLedgerTranxReportController.class);
    @FXML
    private TableView<MasterLedgerReportDTO> tblvMasterLedgerReportList;
    @FXML
    private TableColumn<MasterLedgerReportDTO, String> tblMasterLedgerReportParticulars, tblMasterLedgerReportDebit, tblMasterLedgerReportDate, tblMasterLedgerReportCredit, tblMasterLedgerReportInvoiceNo, tblMasterLedgerReportType;
    @FXML
    private TableColumn<MasterLedgerReportDTO, Void> tblMasterLedgerReportAction;
    private Integer ledgerReportId = -1, openingStock = 0;
    private String crdrType = "";
    @FXML
    public BorderPane ledger_details_report_borderpane;
    public void setEditId(Integer InId) {
        ledgerReportId = InId;
//        System.out.println("ledgerReportId"+ledgerReportId);
        if (InId > -1) {
            setEditData();
        }
    }

    private void setEditData() {
        fetchMasterLedgerReportList();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        MasterLedgerReportListLogger.info("Start of Initialize method of : MasterLedgerTranxReportController " + ledgerReportId);


//        fromDateSelected.getValue().adjustInto((obs, oldValue, newValueFromAmt) -> {
////            System.out.println("Fromt Amount: " + newValueFromAmt.trim());
//            fromDateSelected.setValue(newValueFromAmt);
//        });

        toDateSelected.addEventFilter(KeyEvent.ANY, (KeyEvent event) -> {
            if (event.getCode() == KeyCode.ENTER) {
                fetchMasterLedgerReportList();


            }
        });


        Platform.runLater(() -> {
            fromDateSelected.requestFocus();
        });

        ledger_details_report_borderpane.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ESCAPE) {
//                System.out.println("Esc Clicked....!");
                GlobalController.getInstance().addTabStatic("ledger-list", false);
            }
        });

    }

    private void fetchMasterLedgerReportList() {
        MasterLedgerReportListLogger.info(" fetch Master Ledger Report List  : MasterLedgerTranxReportController");

        try {
            Map<String, String> body = new HashMap<>();
//            System.out.println("ledger_master_id  "+String.valueOf(ledgerReportId));
//            body.put("ledger_master_id", String.valueOf(ledgerReportId));
            if (fromDateSelected.getValue() != null) body.put("startDate", fromDateSelected.getValue().toString());
            if (toDateSelected.getValue() != null) body.put("endDate", toDateSelected.getValue().toString());
            body.put("id", ledgerReportId.toString());

            String requestBody = Globals.mapToStringforFormData(body);
//            System.out.println("requestBody :" + requestBody);
            HttpResponse<String> response = APIClient.postFormDataRequest(requestBody, EndPoints.MASTER_LEDGER_TRANX_REPORT_LIST);
            JsonObject responseBody = new Gson().fromJson(response.body(), JsonObject.class);
//            System.out.println("fetchMasterLedgerReportList" + responseBody);
            ObservableList<MasterLedgerReportDTO> observableList = FXCollections.observableArrayList();
            if (responseBody.get("responseStatus").getAsInt() == 200) {
                JsonArray responseList = responseBody.getAsJsonArray("response");
//                System.out.println("fetchMasterLedgerReportList" + responseList);
                if (responseList.size() > 0) {
//                    System.out.println("List" + responseList);
                    tblvMasterLedgerReportList.setVisible(true);
                    for (JsonElement element : responseList) {
                        JsonObject item = element.getAsJsonObject();
                        String ledgerId = item.get("id") != null ? item.get("id").getAsString() : "";
                        String tran_date = item.get("transaction_date") != null ? item.get("transaction_date").getAsString() : "";
                        String particulars = item.get("particulars") != null ? item.get("particulars").getAsString() : "";
                        String debit = item.get("debit") != null ? item.get("debit").getAsString() : "";
                        String credit = item.get("credit") != null ? item.get("credit").getAsString() : "";
                        String closing_balance = item.get("invoice_no") != null ? item.get("invoice_no").getAsString() : "";
                        String type = item.get("voucher_type") != null ? item.get("voucher_type").getAsString() : "";

                        observableList.add(new MasterLedgerReportDTO(Integer.valueOf(ledgerId), tran_date, particulars, debit, credit, closing_balance, type));

//                        System.out.println("observableList: " + item);
                    }


                    tblMasterLedgerReportDate.setCellValueFactory(new PropertyValueFactory<>("tran_date"));
                    tblMasterLedgerReportParticulars.setCellValueFactory(new PropertyValueFactory<>("particulars"));
                    tblMasterLedgerReportDebit.setCellValueFactory(new PropertyValueFactory<>("debit"));
                    tblMasterLedgerReportCredit.setCellValueFactory(new PropertyValueFactory<>("credit"));
                    tblMasterLedgerReportInvoiceNo.setCellValueFactory(new PropertyValueFactory<>("invoice_no"));
                    tblMasterLedgerReportType.setCellValueFactory(new PropertyValueFactory<>("type"));
                    tblMasterLedgerReportAction.setCellFactory(param -> {
                        final TableCell<MasterLedgerReportDTO, Void> cell = new TableCell<>() {

                            private ImageView viewImg = new ImageView(new Image(String.valueOf(GenivisApplication.class.getResource("ui/assets/view.png"))));


                            {
                                viewImg.setFitHeight(20.0);
                                viewImg.setFitWidth(20.0);
                            }

                            private final Button viewButton = new Button("", viewImg);

                            {

                                viewButton.setOnAction(actionEvent -> {
                                    Integer id = observableList.get(getIndex()).getId();
                                    System.out.println("id->>>>>>>>>>" + id);
                                    GlobalController.getInstance().addTabStaticWithParam(PURCHASE_INV_EDIT_SLUG, false, id);
                                });
                            }

                            HBox hbActions = new HBox();

                            {
                                hbActions.getChildren().add(viewButton);
                                hbActions.setSpacing(10.0);
                            }

// Set the action for the view button

                            @Override
                            protected void updateItem(Void item, boolean empty) {
                                super.updateItem(item, empty);
                                if (empty) {
                                    setGraphic(null);
                                } else {
                                    setGraphic(hbActions);
                                }
                            }
                        };
                        return cell;
                    });

                    tblvMasterLedgerReportList.setItems(observableList);
                    openingStock = responseBody.get("opening_stock").getAsInt();
                    crdrType = responseBody.get("crdrType").getAsString();
                    calculateTotalAmount();
                    MasterLedgerReportListLogger.debug("Success in Displaying Master Ledger Report List : MasterLedgerTranxReportController");
                } else {
                    MasterLedgerReportListLogger.debug("Master Ledger Report List ResponseObject is null : MasterLedgerTranxReportController");
                }
            } else {
                MasterLedgerReportListLogger.debug("Error in response of Master Ledger Report List : MasterLedgerTranxReportController");
            }
        } catch (Exception e) {
            MasterLedgerReportListLogger.error("Master Ledger Report List Error is " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void calculateTotalAmount() {

        ObservableList<MasterLedgerReportDTO> filteredData = tblvMasterLedgerReportList.getItems();

        double totalDebit = 0.0;
        double totalCredit = 0.0;


        DrOpeningAmount();
        CrOpeningAmount();

        for (MasterLedgerReportDTO item : filteredData) {
            totalDebit += Double.parseDouble(item.getDebit().isEmpty() ? "0.0" : item.getDebit());
            totalCredit += Double.parseDouble(item.getCredit().isEmpty() ? "0.0" : item.getCredit());
        }

        lblCurrentTotalDebit.setText(String.format("%.2f", totalDebit));
        lblCurrentTotalCredit.setText(String.format("%.2f", totalCredit));

        Double DrClosing, CrClosing;
        if (totalDebit >= totalCredit) {
            DrClosing = Double.parseDouble(lblTotalOpeningDebit.getText().toString()) + totalDebit - totalCredit;
        } else {
            DrClosing = 0.0;
        }

        if (totalDebit <= totalCredit) {
            CrClosing = Double.parseDouble(lblTotalOpeningCredit.getText().toString()) + totalCredit - totalDebit;
        } else {
            CrClosing = 0.0;
        }

        lblClosingTotalDebit.setText(String.format("%.2f", DrClosing));
        lblClosingTotalCredit.setText(String.format("%.2f", CrClosing));

    }


    private void DrOpeningAmount() {
        if (crdrType == "dr") {
            lblTotalOpeningDebit.setText(String.format("%.2f", openingStock));
        } else {
            lblTotalOpeningDebit.setText("0.00");
        }
    }

    private void CrOpeningAmount() {
        if (crdrType == "dr") {
            lblTotalOpeningCredit.setText(String.format("%.2f", openingStock));
        } else {
            lblTotalOpeningCredit.setText("0.00");
        }
    }

    @FXML
    private void handleResetAction() {
        fromDateSelected.setValue(null);
        toDateSelected.setValue(null);
        fetchMasterLedgerReportList();
        fromDateSelected.requestFocus();
    }


}
