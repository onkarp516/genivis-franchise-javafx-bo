package com.opethic.genivis.controller.Reports;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.opethic.genivis.controller.commons.SwitchButton;
import com.opethic.genivis.dto.LedgerReport1DTO;
import com.opethic.genivis.network.APIClient;
import com.opethic.genivis.network.EndPoints;
import com.opethic.genivis.network.RequestType;
import com.opethic.genivis.utils.CommonTraversa;
import com.opethic.genivis.utils.GlobalController;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import org.json.JSONString;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javafx.scene.control.CheckBox;

import java.net.URL;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import javafx.scene.control.TextField;

import static com.opethic.genivis.utils.FxmFileConstants.*;

public class AccountsLedgerReport1Controller implements Initializable {

    private static final Logger LedgerReport1ListLogger = LoggerFactory.getLogger(AccountsLedgerReport1Controller.class);
    @FXML
    private TableView<LedgerReport1DTO> tblLedgerReport1List;
    @FXML
    private TableColumn<LedgerReport1DTO, String> tblLedgerReport1Principle, tblLedgerReport1Name, tblLedgerReport1Type, tblLedgerReport1BalancingMethod, tblLedgerReport1DebitAmt, tblLedgerReport1CreditAmt;
    @FXML
    private TextField tfAccountsLedgerReport1ListSearch;

    private ObservableList<LedgerReport1DTO> originalData;

    @FXML
    private Label lblTotalDebit;
    @FXML
    private Label lblTotalCredit;

    private boolean isFunded = true, drSelected = false, crSelected = false;
    //    @FXML
//    private SwitchButton sbWithBalance;
    @FXML
    private CheckBox cbWithBalance;
    @FXML
    private CheckBox cbDebtors;
    @FXML
    private CheckBox cbCreditors;
    @FXML
    private TextField tfFromAmt;
    @FXML
    private TextField tfToAmt;
    @FXML
    private Button btReset;
    @FXML
    private BorderPane mainBorderPane;

    private JsonObject jsonObject = null;

    @FXML
    private Button btExportPdf, btExportExcel, btExportCsv, btExportPrint;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        LedgerReport1ListLogger.info("Start of Initialize method of : AccountsLedgerReport1Controller");
        fetchLedgerReport1List("");

        mainBorderPane.addEventFilter(KeyEvent.KEY_PRESSED, (KeyEvent event) -> {
            if (event.getCode() == KeyCode.DOWN && tfAccountsLedgerReport1ListSearch.isFocused()){
                tblLedgerReport1List.getSelectionModel().select(0);
                tblLedgerReport1List.requestFocus();
            }
        });

        // Setting the handler for each node
        setKeyPressedHandler(tfAccountsLedgerReport1ListSearch);
        setKeyPressedHandler(cbWithBalance);
        setKeyPressedHandler(cbDebtors);
        setKeyPressedHandler(cbCreditors);
        setKeyPressedHandler(tfFromAmt);
        setKeyPressedHandler(btReset);
        setKeyPressedHandler(btExportPdf);
        setKeyPressedHandler(btExportExcel);
        setKeyPressedHandler(btExportCsv);
        setKeyPressedHandler(btExportPrint);
        tblLedgerReport1List.requestFocus();

        tfToAmt.setOnKeyPressed(event->{
            if(event.getCode() == KeyCode.ENTER){
                if (!tfFromAmt.getText().isEmpty() && !tfToAmt.getText().isEmpty()) {
                    filterLedgerListByAmount();
                }
                Node nextNode = CommonTraversa.getNextFocusableNode(tfToAmt.getScene());
                if (nextNode != null) {
                    nextNode.requestFocus();
                }
            }
        });

        tblLedgerReport1List.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                LedgerReport1DTO selectedItem = (LedgerReport1DTO) tblLedgerReport1List.getSelectionModel().getSelectedItem();
                String id = selectedItem.getId();
                String ledger_name = selectedItem.getLedger_name();
                AccountsLedgerReport2Controller.ledger_name = ledger_name;
//                System.out.println("Id 1 -->" + id);
                // Pass both slug and id to addTabStatic1 method
                GlobalController.getInstance().addTabStatic1(ACCOUNTS_LEDGER_REPORT2_LIST_SLUG, false, Integer.valueOf(id));
            }
        });

        tblLedgerReport1List.addEventFilter(KeyEvent.ANY, (KeyEvent event) -> {
            if (event.getCode() == KeyCode.ENTER) {
                LedgerReport1DTO selectedItem = (LedgerReport1DTO) tblLedgerReport1List.getSelectionModel().getSelectedItem();
                String id = selectedItem.getId();
                String ledger_name = selectedItem.getLedger_name();
                AccountsLedgerReport2Controller.ledger_name = ledger_name;
                GlobalController.getInstance().addTabStatic1(ACCOUNTS_LEDGER_REPORT2_LIST_SLUG, false, Integer.valueOf(id));
            }
        });

        cbWithBalance.selectedProperty().set(true);
        cbWithBalance.selectedProperty().addListener((observable, wasPreviouslySelected, isNowSelectedDr) -> {
            isFunded = isNowSelectedDr;
            if (isNowSelectedDr) {
                filterLedgerListByFilter();
//                System.out.println("SwitchBox Selected");
            } else {
                filterLedgerListByFilter();
//                System.out.println("SwitchBox Not Selected");
            }
        });

        cbDebtors.selectedProperty().addListener((obs, wasPreviouslySelected, isNowSelectedDr) -> {
            drSelected = isNowSelectedDr;
            if (isNowSelectedDr) {
                filterLedgerListByFilter();
//                System.out.println("DR CheckBox Selected");
            } else {
                filterLedgerListByFilter();
//                System.out.println("DR CheckBox Not Selected");
            }
        });

        cbCreditors.selectedProperty().addListener((obs, wasPreviouslySelected, isNowSelectedCR) -> {
            crSelected = isNowSelectedCR;
            if (isNowSelectedCR) {
                filterLedgerListByFilter();
//                System.out.println("CR CheckBox Selected");
            } else {
                filterLedgerListByFilter();
//                System.out.println("CR CheckBox Not Selected");
            }
        });

        tfFromAmt.textProperty().addListener((obs, oldValue, newValueFromAmt) -> {
//            System.out.println("Fromt Amount: " + newValueFromAmt.trim());
//            tfFromAmt.setText(newValueFromAmt.trim());
            tfFromAmt.setText(newValueFromAmt.matches("^[0-9]*\\.?[0-9]*$") ? newValueFromAmt : oldValue);
        });

        tfToAmt.textProperty().addListener((obs, oldValue, newValueToAmt) -> {
//            System.out.println("To Amount: " + newValueToAmt.trim());
//            tfToAmt.setText(newValueToAmt.trim());
            tfToAmt.setText(newValueToAmt.matches("^[0-9]*\\.?[0-9]*$") ? newValueToAmt : oldValue);
        });

        tfToAmt.addEventFilter(KeyEvent.ANY, (KeyEvent event) -> {
            if (event.getCode() == KeyCode.TAB) {
//                btReset.requestFocus();
                if (!tfFromAmt.getText().isEmpty() && !tfToAmt.getText().isEmpty()) {
                    filterLedgerListByAmount();
                } else {
//                    filterLedgerListByFilter();
                }
            }
        });


//        originalData = tblLedgerReport1List.getItems();
        tfAccountsLedgerReport1ListSearch.textProperty().addListener((observable, oldValue, newValue) -> {
            filterData(newValue.trim());
        });
        Platform.runLater(() -> {
            tfAccountsLedgerReport1ListSearch.requestFocus();
        });
    }

    // Method to handle key pressed event for any Node
    private void setKeyPressedHandler(Node node) {
        node.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                Node nextNode = CommonTraversa.getNextFocusableNode(node.getScene());
                if (nextNode != null) {
                    nextNode.requestFocus();
                }
            }
        });
    }

    // Method to handle key pressed event for exporting buttons
    private void setDayBookEnterKeyPressedHandler(Button textf) {
        textf.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                Node nextNode = CommonTraversa.getNextFocusableNode(textf.getScene());
                if (nextNode != null) {
                    nextNode.requestFocus();
                }
            }
        });
    }

    //Filter Ledger List By Depends on Selected Filters
    private void filterLedgerListByFilter() {
        ObservableList<LedgerReport1DTO> filteredData = FXCollections.observableArrayList();
//        System.out.println("isFunded->>>>" + isFunded);

        LedgerReport1ListLogger.error("Search Accounts Ledger Report 1 in AccountsLedgerReport1Controller");
//        System.out.println("drSelected->>>>" + drSelected);
        if (drSelected && !crSelected) {
            if (isFunded) {
                filteredData = originalData.stream().filter((item -> Double.parseDouble(item.getDebit()) > 0 && item.getUniqueCode().toString().equalsIgnoreCase("SUDR"))).collect(Collectors.toCollection(FXCollections::observableArrayList));
//                filteredData = originalData.stream().collect(Collectors.toCollection(FXCollections::observableArrayList));

            } else {
                filteredData = originalData.stream().filter((item -> item.getUniqueCode().toString().equalsIgnoreCase("SUDR"))).collect(Collectors.toCollection(FXCollections::observableArrayList));

            }
        } else if (!drSelected && crSelected) {
            if (isFunded) {
                filteredData = originalData.stream().filter((item -> Double.parseDouble(item.getCredit()) > 0 && item.getUniqueCode().toString().equalsIgnoreCase("SUCR"))).collect(Collectors.toCollection(FXCollections::observableArrayList));

            } else {
                filteredData = originalData.stream().filter((item -> item.getUniqueCode().toString().equalsIgnoreCase("SUCR"))).collect(Collectors.toCollection(FXCollections::observableArrayList));

            }
        } else if (drSelected && crSelected) {
            if (isFunded) {
                filteredData = originalData.stream().filter((item -> (Double.parseDouble(item.getCredit()) > 0 || Double.parseDouble(item.getDebit()) > 0) && (item.getUniqueCode().equalsIgnoreCase("SUCR") || item.getUniqueCode().equalsIgnoreCase("SUDR")))).collect(Collectors.toCollection(FXCollections::observableArrayList));

            } else {
                filteredData = originalData.stream().filter((item -> item.getUniqueCode().equalsIgnoreCase("SUCR") || item.getUniqueCode().equalsIgnoreCase("SUDR"))).collect(Collectors.toCollection(FXCollections::observableArrayList));

            }
        } else if (!drSelected && !crSelected) {
            if (isFunded) {
                filteredData = originalData.stream().filter((item -> Double.parseDouble(item.getDebit()) > 0 || Double.parseDouble(item.getCredit()) > 0)).collect(Collectors.toCollection(FXCollections::observableArrayList));

            } else {
                filteredData = originalData.stream().collect(Collectors.toCollection(FXCollections::observableArrayList));

            }

        }


        tblLedgerReport1List.setItems(filteredData);

        calculateTotalAmount();


//        if (isFunded) {
//
//
////            filteredData = originalData.stream().filter((item -> Double.parseDouble(item.getDebit()) > 0 ||  item.getUniqueCode().toString()=="SUDR")).collect(Collectors.toCollection(FXCollections::observableArrayList));
//
//
//            filteredData = originalData.stream().filter((item -> Double.parseDouble(item.getDebit()) > 0 || Double.parseDouble(item.getCredit()) > 0)).collect(Collectors.toCollection(FXCollections::observableArrayList));
//            tblLedgerReport1List.setItems(filteredData);
//        } else {
//            tblLedgerReport1List.setItems(originalData.stream().collect(Collectors.toCollection(FXCollections::observableArrayList)));
//        }

    }

    private void calculateTotalAmount() {

        ObservableList<LedgerReport1DTO> filteredData = tblLedgerReport1List.getItems();
        // Calculate the Totals
        double totalDebit = 0.0;
        double totalCredit = 0.0;
        for (LedgerReport1DTO item : filteredData) {
            totalDebit += Double.parseDouble(item.getDebit().isEmpty() ? "0.0" : item.getDebit());
            totalCredit += Double.parseDouble(item.getCredit().isEmpty() ? "0.0" : item.getCredit());
        }

        // Update Labels in the FXML
        lblTotalDebit.setText(String.format("%.2f", totalDebit));
        lblTotalCredit.setText(String.format("%.2f", totalCredit));
    }


    //Filter Ledger List By Depends on From & To Amount
    private void filterLedgerListByAmount() {
        ObservableList<LedgerReport1DTO> filteredData = FXCollections.observableArrayList();

        LedgerReport1ListLogger.error("Search Accounts Ledger Report 1 in AccountsLedgerReport1Controller");
        Double tfFromAmount = Double.parseDouble(tfFromAmt.getText());
        Double tfToAmount = Double.parseDouble(tfToAmt.getText());

        if (isFunded) {
            if (drSelected && !crSelected) {
                filteredData = originalData.stream().filter((item -> Double.parseDouble(item.getDebit()) > 0 && Double.parseDouble(item.getDebit()) >= tfFromAmount && Double.parseDouble(item.getDebit()) <= tfToAmount && item.getUniqueCode().toString().equalsIgnoreCase("SUDR"))).collect(Collectors.toCollection(FXCollections::observableArrayList));

            } else if (!drSelected && crSelected) {
                filteredData = originalData.stream().filter((item -> Double.parseDouble(item.getCredit()) > 0 && Double.parseDouble(item.getCredit()) >= tfFromAmount && Double.parseDouble(item.getCredit()) <= tfToAmount && item.getUniqueCode().toString().equalsIgnoreCase("SUCR"))).collect(Collectors.toCollection(FXCollections::observableArrayList));


            } else if (drSelected && crSelected) {

                filteredData = originalData.stream().filter((item -> ((Double.parseDouble(item.getDebit()) > 0 && Double.parseDouble(item.getDebit()) >= tfFromAmount && Double.parseDouble(item.getDebit()) <= tfToAmount) || (Double.parseDouble(item.getCredit()) > 0 && Double.parseDouble(item.getCredit()) >= tfFromAmount && Double.parseDouble(item.getCredit()) <= tfToAmount)) && (item.getUniqueCode().equalsIgnoreCase("SUCR") || item.getUniqueCode().equalsIgnoreCase("SUDR")))).collect(Collectors.toCollection(FXCollections::observableArrayList));

            } else if (!drSelected && !crSelected) {

                filteredData = originalData.stream().filter((item -> (Double.parseDouble(item.getDebit()) > 0 && Double.parseDouble(item.getDebit()) >= tfFromAmount && Double.parseDouble(item.getDebit()) <= tfToAmount) || (Double.parseDouble(item.getCredit()) > 0 && Double.parseDouble(item.getCredit()) >= tfFromAmount && Double.parseDouble(item.getCredit()) <= tfToAmount))).collect(Collectors.toCollection(FXCollections::observableArrayList));
            }
        } else {
            if (drSelected && !crSelected) {
                filteredData = originalData.stream().filter((item -> !item.getDebit().isEmpty() && Double.parseDouble(item.getDebit()) >= tfFromAmount && Double.parseDouble(item.getDebit()) <= tfToAmount && item.getUniqueCode().toString().equalsIgnoreCase("SUDR"))).collect(Collectors.toCollection(FXCollections::observableArrayList));

            } else if (!drSelected && crSelected) {
                filteredData = originalData.stream().filter((item -> !item.getCredit().isEmpty() && Double.parseDouble(item.getCredit()) >= tfFromAmount && Double.parseDouble(item.getCredit()) <= tfToAmount && item.getUniqueCode().toString().equalsIgnoreCase("SUCR"))).collect(Collectors.toCollection(FXCollections::observableArrayList));

            } else if (drSelected && crSelected) {

                filteredData = originalData.stream().filter((item -> ((!item.getDebit().isEmpty() && Double.parseDouble(item.getDebit()) >= tfFromAmount && Double.parseDouble(item.getDebit()) <= tfToAmount) || (!item.getCredit().isEmpty() && Double.parseDouble(item.getCredit()) >= tfFromAmount && Double.parseDouble(item.getCredit()) <= tfToAmount)) && (item.getUniqueCode().equalsIgnoreCase("SUCR") || item.getUniqueCode().equalsIgnoreCase("SUDR")))).collect(Collectors.toCollection(FXCollections::observableArrayList));

            } else if (!drSelected && !crSelected) {

                filteredData = originalData.stream().filter((item -> ((!item.getDebit().isEmpty() && Double.parseDouble(item.getDebit()) >= tfFromAmount && Double.parseDouble(item.getDebit()) <= tfToAmount) || (!item.getCredit().isEmpty() && Double.parseDouble(item.getCredit()) >= tfFromAmount && Double.parseDouble(item.getCredit()) <= tfToAmount)))).collect(Collectors.toCollection(FXCollections::observableArrayList));
            }
        }
        tblLedgerReport1List.setItems(filteredData);
        calculateTotalAmount();
    }

    private void fetchLedgerReport1List(String searchKey) {
        LedgerReport1ListLogger.info("fetch Accounts Ledger Report 1 List : AccountsLedgerReport1Controller");

        APIClient apiClient = null;

        try {
            LedgerReport1ListLogger.debug("Get Accounts Ledger Report 1 Data Started...");
            Map<String, String> body = new HashMap<>();
//            HttpResponse<String> response = APIClient.getRequest(EndPoints.ACCOUNTS_LEDGER_REPORT1_LIST);
            apiClient = new APIClient(EndPoints.ACCOUNTS_LEDGER_REPORT1_LIST, "", RequestType.GET);
            apiClient.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
                @Override
                public void handle(WorkerStateEvent workerStateEvent) {
//                    JsonObject responseBody = new Gson().fromJson(response.body(), JsonObject.class);
                    jsonObject = new Gson().fromJson(workerStateEvent.getSource().getValue().toString(), JsonObject.class);
//            System.out.println("LedgerReport1_list" + jsonObject);
                    ObservableList<LedgerReport1DTO> observableList = FXCollections.observableArrayList();
                    if (jsonObject.get("responseStatus").getAsInt() == 200) {
                        JsonArray responseList = jsonObject.getAsJsonArray("responseList");
//                System.out.println("LedgerReport1_list" + responseBody);
                        if (responseList.size() > 0) {
//                    System.out.println("LedgerReport1_list" + responseBody);
                            tblLedgerReport1List.setVisible(true);
                            for (JsonElement element : responseList) {
                                JsonObject item = element.getAsJsonObject();
                                String id = item.get("id").getAsString();
                                String principle = item.get("principle_name").getAsString();
                                String ledger_name = item.get("ledger_name").getAsString();
                                String type = item.get("subprinciple_name") != null ? item.get("subprinciple_name").getAsString() : "";
                                String balancing_method = item.get("balancing_method").getAsString();
                                String debit = item.get("dr").getAsString();
                                String credit = item.get("cr").getAsString();
                                String unique_code = item.get("unique_code").getAsString();

                                observableList.add(new LedgerReport1DTO(id, principle, ledger_name, type, balancing_method, debit, credit, unique_code));
                            }

//                    // Calculate the Totals
//                    double totalDebit = 0.0;
//                    double totalCredit = 0.0;
//                    for (LedgerReport1DTO item : observableList) {
//                        totalDebit += Double.parseDouble(item.getDebit().isEmpty() ? "0.0" : item.getDebit());
//                        totalCredit += Double.parseDouble(item.getCredit().isEmpty() ? "0.0" : item.getCredit());
//                    }
//
//                    // Update Labels in the FXML
//                    lblTotalDebit.setText(String.format("%.2f", totalDebit));
//                    lblTotalCredit.setText(String.format("%.2f", totalCredit));


                            tblLedgerReport1Principle.setCellValueFactory(new PropertyValueFactory<>("principle"));
                            tblLedgerReport1Name.setCellValueFactory(new PropertyValueFactory<>("ledger_name"));
                            tblLedgerReport1Type.setCellValueFactory(new PropertyValueFactory<>("type"));
                            tblLedgerReport1BalancingMethod.setCellValueFactory((new PropertyValueFactory<>("balancing_method")));
                            tblLedgerReport1DebitAmt.setCellValueFactory(new PropertyValueFactory<>("debit"));
                            tblLedgerReport1CreditAmt.setCellValueFactory(new PropertyValueFactory<>("credit"));

//                    tblLedgerReport1List.setItems(observableList);
                            originalData = observableList;
                            filterLedgerListByFilter();
                            LedgerReport1ListLogger.debug("Success in Displaying Accounts Ledger Report 1 List : AccountsLedgerReport1Controller");
                        } else {
                            LedgerReport1ListLogger.debug("Accounts Ledger Report 1 List ResponseObject is null : AccountsLedgerReport1Controller");
                        }
                    } else {
                        LedgerReport1ListLogger.debug("Error in response of Accounts Ledger Report 1 List : AccountsLedgerReport1Controller");
                    }
                }
            });

            apiClient.setOnCancelled(new EventHandler<WorkerStateEvent>() {
                @Override
                public void handle(WorkerStateEvent workerStateEvent) {
                    LedgerReport1ListLogger.error("Network API cancelled in fetchLedgerReport1List()" + workerStateEvent.getSource().getValue().toString());

                }
            });

            apiClient.setOnFailed(new EventHandler<WorkerStateEvent>() {
                @Override
                public void handle(WorkerStateEvent workerStateEvent) {
                    LedgerReport1ListLogger.error("Network API failed in fetchLedgerReport1List()" + workerStateEvent.getSource().getValue().toString());
                }
            });
            apiClient.start();
            LedgerReport1ListLogger.debug("Get Accounts Ledger Report 1 Data End...");
        } catch (Exception e) {
            LedgerReport1ListLogger.error("Accounts Ledger Report 1 Error is " + e.getMessage());
            e.printStackTrace();
        }
        finally {
            apiClient = null;
        }
    }

    private void filterData(String keyword) {
        ObservableList<LedgerReport1DTO> filteredData = FXCollections.observableArrayList();

        LedgerReport1ListLogger.error("Search Accounts Ledger Report 1 in AccountsLedgerReport1Controller");
        if (keyword.isEmpty()) {
            tblLedgerReport1List.setItems(originalData);
            calculateTotalAmount();
            return;
        }

        for (LedgerReport1DTO item : originalData) {
            if (matchesKeyword(item, keyword)) {
                filteredData.add(item);
            }
        }

        tblLedgerReport1List.setItems(filteredData);
        calculateTotalAmount();
    }

    private boolean matchesKeyword(LedgerReport1DTO item, String keyword) {
        String lowerCaseKeyword = keyword.toLowerCase();

        return item.getPrinciple().toLowerCase().contains(lowerCaseKeyword) ||
                item.getLedger_name().toLowerCase().contains(lowerCaseKeyword) ||
                item.getType().toLowerCase().contains(lowerCaseKeyword) ||
                item.getBalancing_method().toLowerCase().contains(lowerCaseKeyword) ||
                item.getDebit().toLowerCase().contains(lowerCaseKeyword) ||
                item.getCredit().toLowerCase().contains(lowerCaseKeyword) ||
                item.getUniqueCode().toLowerCase().contains(lowerCaseKeyword);
    }

    @FXML
    private void handleResetAction() {
        tfFromAmt.setText("");
        tfToAmt.setText("");
        cbCreditors.setSelected(false);
        cbDebtors.setSelected(false);

        tblLedgerReport1List.setItems(originalData);
        calculateTotalAmount();
        tfAccountsLedgerReport1ListSearch.requestFocus();
    }
}
