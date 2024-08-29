package com.opethic.genivis.controller.master;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.opethic.genivis.GenivisApplication;
import com.opethic.genivis.controller.tranx_sales.SalesQuotationListController;
import com.opethic.genivis.dto.reqres.product.Communicator;
import com.opethic.genivis.key_manager.ShortCutKey;
import com.opethic.genivis.network.APIClient;
import com.opethic.genivis.utils.*;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.net.URL;
import java.net.http.HttpResponse;
import java.util.*;

import static com.opethic.genivis.utils.FxmFileConstants.ROLE_UPDATE_SLUG;
import static com.opethic.genivis.utils.FxmFileConstants.SALES_QUOTATION_LIST_SLUG;
import static com.opethic.genivis.utils.Globals.userRoleModel;

public class LedgerGroupController implements Initializable {

    @FXML
    private TextField tfLedgerGroup;

    @FXML
    private ComboBox cbUnder;
    @FXML
    private BorderPane ledgerGroupRoot;

    @FXML
    private TextField tfSearch;

    @FXML
    private TableView<ObservableList<StringProperty>> tableView;

    @FXML
    private TableColumn<ObservableList<StringProperty>, String> tcLedgerGroup;

    @FXML
    private TableColumn<ObservableList<StringProperty>, String> tcFoundation;

    @FXML
    private TableColumn<ObservableList<StringProperty>, String> tcPrinciple;

    @FXML
    private TableColumn<ObservableList<StringProperty>, String> tcSubPrinciple;

    @FXML
    private Button submitButton;

    Integer selected_row_index;

    Integer previousNoOfRows;

    @FXML
    private Button cancelButton;

    @FXML
    VBox rootVBox;


    Map<String, String> map = new HashMap<>(); //this for create ledger group
    Map<String, String> updateMap = new HashMap<>(); //this for update ledger group

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        ResponsiveWiseCssPicker();

        Platform.runLater(() -> {
            cbUnder.requestFocus();
//            cbUnder.setOnKeyPressed(event->{
//                if(event.getCode()==KeyCode.ENTER || event.getCode()==KeyCode.TAB){
//                    if(cbUnder.getValue().equals("")){
//                        cbUnder.requestFocus();
//                    }
//                    else{
//                        tfLedgerGroup.requestFocus();
//                    }
//                }
//            });
        });
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        CommonValidationsUtils.configureTextFieldToTitleCase(tfLedgerGroup);

        handleComboBoxList();

        getLedgerGroupData("");

        tfSearch.setOnKeyTyped(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                String searchKey = tfSearch.getText().trim();
                getLedgerGroupData(searchKey);
                /*if (searchKey.length() >= 3) {
                    getLedgerGroupData(searchKey);
                } else if (searchKey.isEmpty()) {
                    getLedgerGroupData("");
                }*/
            }
        });

        cbUnder.addEventFilter(KeyEvent.KEY_PRESSED, event -> {

            if (event.getCode() == KeyCode.ENTER || event.getCode() == KeyCode.TAB) {
                event.consume();

                if (cbUnder.getValue() != null || !cbUnder.getSelectionModel().isEmpty()) {
                    tfLedgerGroup.requestFocus();
                } else {
                    AlertUtility.AlertErrorTimeout3("Validation Error", "Under field is empty", output -> {
                        if (output) {
                            cbUnder.requestFocus();
                            event.consume();
                        }
                    });

                }

            }else if (event.getCode()==KeyCode.DOWN) {
                tableView.requestFocus();
                tableView.getSelectionModel().select(0);
                tableView.getFocusModel().focus(0);
            }

        });

//        cbUnder.setOnKeyPressed(event -> {
//
//            if (event.getCode() == KeyCode.ENTER || event.getCode() == KeyCode.TAB && !event.isShiftDown()) {
//                if (cbUnder.getValue() != null || !cbUnder.getSelectionModel().isEmpty()) {
//                    tfLedgerGroup.requestFocus();
//                } else {
//                    AlertUtility.AlertErrorTimeout3("Validation Error", "Under field is empty", output -> {
//                        if (output) {
//                            cbUnder.requestFocus();
//                        }
//                    });
//
//                }
//                event.consume();
//            }
//            else if (event.getCode()==KeyCode.DOWN) {
//                tableView.requestFocus();
//                tableView.getSelectionModel().select(0);
//                tableView.getFocusModel().focus(0);
//            }
//            else if(event.getCode() == KeyCode.ESCAPE){
//                cbUnder.hide();
//            }
//
//        });




        tfLedgerGroup.setOnKeyPressed(event -> {
            if(event.isShiftDown() && event.getCode() == KeyCode.TAB){
                cbUnder.requestFocus();
            }
            else if (event.getCode() == KeyCode.ENTER || event.getCode() == KeyCode.TAB) {
                if (tfLedgerGroup.getText().isEmpty()) {
                    AlertUtility.AlertErrorTimeout2("Validation Error", "Ledger Group is Required", callBack -> {
                        tfLedgerGroup.requestFocus();
                    });
                } else {
                    submitButton.requestFocus();
                    event.consume();
                }
            }else if (event.getCode()==KeyCode.DOWN) {
                tableView.requestFocus();
                tableView.getSelectionModel().select(0);
                tableView.getFocusModel().focus(0);
            }
        });
        submitButton.setOnKeyPressed(event->{
            if(event.isShiftDown() && event.getCode() == KeyCode.TAB){
                tfLedgerGroup.requestFocus();
            }  else if(event.getCode() == KeyCode.TAB){
                cancelButton.requestFocus();
            }
            else if (event.getCode()==KeyCode.DOWN) {
                tableView.requestFocus();
                tableView.getSelectionModel().select(0);
                tableView.getFocusModel().focus(0);
            } else if(event.getCode() == KeyCode.RIGHT){
                cancelButton.requestFocus();
            }
        });
        cancelButton.setOnKeyPressed(event->{
            if(event.isShiftDown() && event.getCode() == KeyCode.TAB){
                submitButton.requestFocus();
            }
            else if(event.getCode() == KeyCode.TAB){
                tfSearch.requestFocus();
            }else if (event.getCode()==KeyCode.DOWN) {
                tableView.requestFocus();
                tableView.getSelectionModel().select(0);
                tableView.getFocusModel().focus(0);
            }else if(event.getCode() == KeyCode.LEFT){
                submitButton.requestFocus();
            }
        });
        tfSearch.setOnKeyPressed(event->{
            if(event.isShiftDown() && event.getCode() == KeyCode.TAB){
                cancelButton.requestFocus();
            } else if (event.getCode()==KeyCode.DOWN) {
                tableView.requestFocus();
                tableView.getSelectionModel().select(0);
                tableView.getFocusModel().focus(0);
            }
        });

        tfLedgerGroup.setOnKeyTyped(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                tfLedgerGroup.setStyle(null);
            }
        });

        //handled table row selection
        tableView.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2 && !tableView.getSelectionModel().isEmpty()) {
                handleRowSelection();
            }
        });

        tableView.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER && !tableView.getSelectionModel().isEmpty()) {
                handleRowSelection();
            }
        });

        sceneInitilization();

       /* submitButton.sceneProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                newValue.getAccelerators().put(ShortCutKey.product_save_CTRL_S, submitButton::fire);
            }
        });

        cancelButton.sceneProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                newValue.getAccelerators().put(ShortCutKey.product_cancel_CTRL_X, cancelButton::fire);
            }
        });*/

        rootVBox.addEventFilter(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {

                if ((event.getCode().isModifierKey() || event.getCode().isLetterKey() ||
                        event.getCode().isDigitKey() || event.getCode() == KeyCode.BACK_SPACE)
                        && tableView.isFocused()) {
                    tfSearch.requestFocus();
                } else if (event.getCode() == KeyCode.DOWN && tfSearch.isFocused()) {
                    tableView.getSelectionModel().select(0);
                    tableView.requestFocus();
                } else if (event.isControlDown() && event.getCode() == KeyCode.S) {
                    if (submitButton.getText().equalsIgnoreCase("Update")){
                        onClickUpdate();
                    }else {
                        submitLedgerGroup();
                    }
                }
            }
        });
        shortcutKeysSalesQuot();

    }

    private void ResponsiveWiseCssPicker() {
        double height = Screen.getPrimary().getBounds().getHeight();
        double width = Screen.getPrimary().getBounds().getWidth();
        System.out.println("width" + width);
        if (width >= 992 && width <= 1024) {
            ledgerGroupRoot.getStylesheets().add(GenivisApplication.class.getResource("ui/css/TranxCommonCssStyles/tranxCommonCssStyle1.css").toExternalForm());
        } else if (width >= 1025 && width <= 1280) {
            ledgerGroupRoot.getStylesheets().add(GenivisApplication.class.getResource("ui/css/TranxCommonCssStyles/tranxCommonCssStyle2.css").toExternalForm());
        } else if (width >= 1281 && width <= 1368) {
            ledgerGroupRoot.getStylesheets().add(GenivisApplication.class.getResource("ui/css/TranxCommonCssStyles/tranxCommonCssStyle3.css").toExternalForm());
        } else if (width >= 1369 && width <= 1400) {
            ledgerGroupRoot.getStylesheets().add(GenivisApplication.class.getResource("ui/css/TranxCommonCssStyles/tranxCommonCssStyle4.css").toExternalForm());
        } else if (width >= 1401 && width <= 1440) {
            ledgerGroupRoot.getStylesheets().add(GenivisApplication.class.getResource("ui/css/TranxCommonCssStyles/tranxCommonCssStyle5.css").toExternalForm());
        } else if (width >= 1441 && width <= 1680) {
            ledgerGroupRoot.getStylesheets().add(GenivisApplication.class.getResource("ui/css/TranxCommonCssStyles/tranxCommonCssStyle6.css").toExternalForm());
        } else if (width >= 1681 && width <= 1920) {
            ledgerGroupRoot.getStylesheets().add(GenivisApplication.class.getResource("ui/css/TranxCommonCssStyles/tranxCommonCssStyle7.css").toExternalForm());
        }
    }

    private void shortcutKeysSalesQuot() {
        rootVBox.addEventFilter(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {

            @Override
            public void handle(KeyEvent event) {
                if (event.getCode() == KeyCode.S && event.isControlDown()) {
//                    if (CommonValidationsUtils.validateForm(tfCmpAdmUsername, tfCmpAdmPassword)) {

                    if (submitButton.getText().equalsIgnoreCase("Submit")) {
                        AlertUtility.AlertConfirmation(AlertUtility.alertTypeConfirmation, "Are you Really want to Submit?", input -> {
                            if (input == 1) {
                                if (CommonValidationsUtils.validateForm(tfLedgerGroup,cbUnder)) {
                                    createLedgerGroupData();
                                }
                            }
                        });

                    }
                    else {
                        AlertUtility.AlertConfirmation(AlertUtility.alertTypeConfirmation, "Are you Really want to Update?", input -> {
                            if (input == 1) {
                                if (CommonValidationsUtils.validateForm(tfLedgerGroup,cbUnder)) {
                                    updateLedgerGroupData();
                                }
                            }
                        });
                    }
                }

                if (event.getCode() == KeyCode.E && event.isControlDown()) {
                    handleRowSelection();
//                    AlertUtility.AlertConfirmation(AlertUtility.alertTypeConfirmation, "Are you sure want to go list ?", input -> {
//                        if (input == 1) {
//                           int index =  tableView.getSelectionModel().getFocusedIndex();
//                            SalesQuotationListController.focusStatus = "edit";
//                            SalesQuotationListController.selectedRowIndex = selectedRowIndex;
//                            GlobalController.getInstance().addTabStatic(SALES_QUOTATION_LIST_SLUG,false);
//                        }
//                    });
                }
                if (event.getCode() == KeyCode.X && event.isControlDown()) {
                    onClickClear();
                }

            }
        });
    }


    public void sceneInitilization() {
        rootVBox.sceneProperty().addListener((observable, oldScene, newScene) -> {
            if (newScene != null && newScene.getWindow() instanceof Stage) {
                Communicator.stage = (Stage) newScene.getWindow();
            }
        });
    }

    public void handleComboBoxList() {

        ObservableList<Principle> list_principle = FXCollections.observableArrayList(getUnderList());

        System.out.println("mk: " + list_principle);
        cbUnder.setItems(list_principle);
        Integer initialIndex = -1;
        AutoCompleteBox autoCompleteBox = new AutoCompleteBox(cbUnder, initialIndex);
//        cbUnder.setEditable(true);

        cbUnder.setOnAction(event -> {
            Object selectedPrincipleName = cbUnder.getSelectionModel().getSelectedItem();
            if (selectedPrincipleName != null) {
                for (Principle principle : list_principle) {
                    if (selectedPrincipleName.toString().equals(principle.getPrinciple_name())) {
                        map.put("principle_id", principle.getPrinciple_id());
                        map.put("principle_name", principle.getPrinciple_name());
                        map.put("under_prefix", principle.getUnder_prefix());
                        if (Integer.parseInt(principle.getSub_principle_id()) > 0) {
                            map.put("sub_principle_id", principle.getSub_principle_id());
                        }
                    }
                }
            } else {
                System.out.println("No principle selected.");
            }
        });

    }

    //This function for get Ledger Group for adding in ComboBox
    public List<Principle> getUnderList() {

        List<Principle> list = new ArrayList<>();
        try {

            HttpResponse<String> response = APIClient.getRequest("get_under_list");
            JsonObject jsonObject = new Gson().fromJson(response.body(), JsonObject.class);

            if (jsonObject.get("responseStatus").getAsInt() == 200) {
                JsonArray responseObject = jsonObject.getAsJsonArray("responseObject");

                System.out.println("responseObject " + responseObject);

                if (responseObject.size() > 0) {
                    for (JsonElement element : responseObject) {
                        JsonObject jsonItem = element.getAsJsonObject();
                        String principleName = jsonItem.get("principle_name").getAsString();
                        String associatesName = jsonItem.get("associates_name").getAsString();
                        String subPrincipleName = jsonItem.get("subprinciple_name").getAsString();
                        String pricipleID = jsonItem.get("principle_id").getAsString();
                        String underPrefix = jsonItem.get("under_prefix").getAsString();
                        String subPrincipleId = jsonItem.get("sub_principle_id").getAsString();


                        // System.out.println("p id"+pricipleID);
                        if (associatesName.isEmpty()) {
                            // cbUnder.getItems().add(principleName);
                            if (!principleName.isEmpty() && !subPrincipleName.isEmpty()) {
                                list.add(new Principle(pricipleID, subPrincipleName, underPrefix, subPrincipleId));
                            } else {
                                list.add(new Principle(pricipleID, principleName, underPrefix, subPrincipleId));
                            }
                        } else if (!associatesName.isEmpty()) {
                            // cbUnder.getItems().add(associatesName);
                            list.add(new Principle(pricipleID, associatesName, underPrefix, subPrincipleId));
                        }

                    }

                } else {
                    System.out.println("responseObject is null");
                }
            } else {
                System.out.println("Error in response");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }


    public void getLedgerGroupData(String searchTerm) {

        try {
            //Clear previous table data
            tableView.getItems().clear();

            //Columns initialization
            tcLedgerGroup.setCellValueFactory(cellData -> {
                if (!cellData.getValue().isEmpty()) {
                    return cellData.getValue().get(0);
                } else {
                    return new SimpleStringProperty("");
                }
            });
            tcFoundation.setCellValueFactory(cellData -> {
                if (!cellData.getValue().isEmpty() && cellData.getValue().size() > 1) {
                    return cellData.getValue().get(1);
                } else {
                    return new SimpleStringProperty("");
                }
            });
            tcPrinciple.setCellValueFactory(cellData -> {
                if (!cellData.getValue().isEmpty() && cellData.getValue().size() > 2) {
                    return cellData.getValue().get(2);
                } else {
                    return new SimpleStringProperty("");
                }
            });

            tcSubPrinciple.setCellValueFactory(cellData -> {
                if (!cellData.getValue().isEmpty() && cellData.getValue().size() > 3) {
                    return cellData.getValue().get(3);
                } else {
                    return new SimpleStringProperty("");
                }
            });

            Map<String, String> map = new HashMap<>();
            map.put("search", searchTerm);
            String formData = Globals.mapToStringforFormData(map);

            //Called API for get HSN Data
            HttpResponse<String> response = APIClient.postFormDataRequest(formData, "search_associate_groups");
            JsonObject jsonObject = new Gson().fromJson(response.body(), JsonObject.class);

            if (jsonObject.get("responseStatus").getAsInt() == 200) {
                JsonArray responseObject = jsonObject.getAsJsonArray("responseObject");

                if (responseObject.size() > 0) {
                    for (JsonElement element : responseObject) {
                        JsonObject item = element.getAsJsonObject();

                        JsonElement subprinciple_name_j = item.get("subprinciple_name");
                        String subprinciple_name = (subprinciple_name_j != null && !subprinciple_name_j.isJsonNull()) ? subprinciple_name_j.getAsString() : "";

                        JsonElement associates_name_j = item.get("associates_name");
                        String associates_name = (associates_name_j != null && !associates_name_j.isJsonNull()) ? associates_name_j.getAsString() : "";

                        JsonElement foundation_name_j = item.get("foundation_name");
                        String foundation_name = (foundation_name_j != null && !foundation_name_j.isJsonNull()) ? foundation_name_j.getAsString() : "";

                        JsonElement principle_name_j = item.get("principle_name");
                        String principle_name = (principle_name_j != null && !principle_name_j.isJsonNull()) ? principle_name_j.getAsString() : "";

                        JsonElement associates_id_j = item.get("associates_id");
                        String associates_id = (associates_id_j != null && !associates_id_j.isJsonNull()) ? associates_id_j.getAsString() : "";

                        JsonElement sub_principle_id_j = item.get("sub_principle_id");
                        String sub_principle_id = (sub_principle_id_j != null && !sub_principle_id_j.isJsonNull()) ? sub_principle_id_j.getAsString() : "";

                        JsonElement under_prefix_j = item.get("under_prefix");
                        String under_prefix = (under_prefix_j != null && !under_prefix_j.isJsonNull()) ? under_prefix_j.getAsString() : "";

                        JsonElement principle_id_j = item.get("principle_id");
                        String principle_id = (principle_id_j != null && !principle_id_j.isJsonNull()) ? principle_id_j.getAsString() : "";

                        JsonElement under_name_j = item.get("under_name");
                        String under_name = (under_name_j != null && !under_name_j.isJsonNull()) ? under_name_j.getAsString() : "";


                        ObservableList<StringProperty> row = FXCollections.observableArrayList(
                                new SimpleStringProperty(associates_name),
                                new SimpleStringProperty(foundation_name),
                                new SimpleStringProperty(principle_name),
                                new SimpleStringProperty(subprinciple_name),

                                //hidden data
                                new SimpleStringProperty(associates_id),
                                new SimpleStringProperty(sub_principle_id),
                                new SimpleStringProperty(under_prefix),
                                new SimpleStringProperty(principle_id),
                                new SimpleStringProperty(under_name)
                        );

                        tableView.getItems().add(row);
                    }
                    tableView.refresh();

                } else {
                    System.out.println("responseObject is null");
                }
            } else {
                System.out.println("Error in response");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        ObservableList<ObservableList<StringProperty>> rows = tableView.getItems();
        Integer currentNoOfRows = rows.size();
        if (previousNoOfRows != null && currentNoOfRows > previousNoOfRows) {
            tableView.getSelectionModel().select(0);
        }
        if (selected_row_index != null) {
            tableView.getSelectionModel().select(selected_row_index);
            selected_row_index = null;
        }
    }

    public void createLedgerGroupData() {

        //set the size of table to compare with new table size
        ObservableList<ObservableList<StringProperty>> rows = tableView.getItems();
        previousNoOfRows = rows.size();

        map.put("associates_group_name", tfLedgerGroup.getText());

        String formData = Globals.mapToStringforFormData(map);

        System.out.println("fk: " + formData);

        HttpResponse<String> response = APIClient.postFormDataRequest(formData, "create_associate_groups");

        JsonObject responseBody = new Gson().fromJson(response.body(), JsonObject.class);
        System.out.println("Response=>" + responseBody);

        if (responseBody.get("responseStatus").getAsInt() == 200) {
            AlertUtility.AlertSuccessTimeout("success", responseBody.get("message").getAsString(), input -> {
            });
            onClickClear();
            tableView.requestFocus();
            getLedgerGroupData("");
        } else {
            AlertUtility.AlertErrorTimeout("error", responseBody.get("message").getAsString(), input -> {
            });
        }
    }

    public void updateLedgerGroupData() {

        updateMap.put("associates_group_name", tfLedgerGroup.getText());

        if (!map.isEmpty()) {
            updateMap.put("principle_id", map.get("principle_id"));
            updateMap.put("sub_principle_id", map.get("sub_principle_id"));
            updateMap.put("under_prefix", map.get("under_prefix"));
        }

        String formData = Globals.mapToStringforFormData(updateMap);
        HttpResponse<String> response = APIClient.postFormDataRequest(formData, "edit_associate_groups");

        JsonObject responseBody = new Gson().fromJson(response.body(), JsonObject.class);
        System.out.println("Response=>" + responseBody);

        if (responseBody.get("responseStatus").getAsInt() == 200) {

            AlertUtility.AlertSuccessTimeout("success", responseBody.get("message").getAsString(), input -> {
            });
            onClickClear();
            getLedgerGroupData("");
        } else {
            AlertUtility.AlertErrorTimeout("error", responseBody.get("message").getAsString(), input -> {
            });
        }
    }

    @FXML
    private void onClickClear() {

        map.clear();
        updateMap.clear();

        tfLedgerGroup.clear();

        cbUnder.getSelectionModel().clearSelection();

// Clear editable text in ComboBox
        cbUnder.setValue(null);

// Set the promptText to ComboBox
        cbUnder.setPromptText("Select");
        cbUnder.setButtonCell(new ListCell<String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText("Select");
                } else {
                    setText(item);
                }
            }
        });


        submitButton.setText("Submit");
        submitButton.setOnAction(event -> onClickSubmit());
    }


    @FXML
    public void onClickSubmit() {
        submitLedgerGroup();
    }

    public void submitLedgerGroup() {
        Stage stage = (Stage) rootVBox.getScene().getWindow();

        if (cbUnder.getValue() != null && !tfLedgerGroup.getText().trim().isEmpty()) {
            tfLedgerGroup.setStyle(null);
            AlertUtility.AlertConfirmation(AlertUtility.alertTypeConfirmation, "Are you Really want to Submit?", input -> {
                if (input == 1) {
                    if (CommonValidationsUtils.validateForm(tfLedgerGroup,cbUnder)) {
                        createLedgerGroupData();
                    }
                }
            });
        } else {
            if (cbUnder.getValue() == null || cbUnder.getValue()=="") {
                AlertUtility.AlertErrorTimeout3("Validation Error",
                        "Under field is empty", output -> {
                            if (output) {
                                cbUnder.requestFocus();
                            }
                        });
            } else if (tfLedgerGroup.getText().trim().isEmpty() && cbUnder.getValue() != null) {
                cbUnder.setStyle(null);
                AlertUtility.AlertErrorTimeout3("Validation Error", "Ledger Group is empty", output -> {
                    if (output) {
                        tfLedgerGroup.requestFocus();
                    }
                });
            } else {
                cbUnder.requestFocus();
            }
        }
    }

    public void onClickUpdate() {
        AlertUtility.AlertConfirmation(AlertUtility.alertTypeConfirmation, "Are you Really want to Update?", input -> {
            if (input == 1) {
                if (CommonValidationsUtils.validateForm(tfLedgerGroup,cbUnder)) {
                    updateLedgerGroupData();
                }
            }
        });
    }

    private void handleRowSelection() {
        cbUnder.requestFocus();
        selected_row_index = tableView.getSelectionModel().getSelectedIndex();
        ObservableList<StringProperty> rowData = tableView.getSelectionModel().getSelectedItem();
        updateMap.put("associates_id", rowData.get(4).getValue());

        updateMap.put("principle_id", rowData.get(7).getValue());

        updateMap.put("sub_principle_id", rowData.get(5).getValue());
        updateMap.put("under_prefix", rowData.get(6).getValue());
        tfLedgerGroup.setText(rowData.get(0).getValue());//set associates_name
        cbUnder.setValue(rowData.get(8).getValue()); //set under_name
        submitButton.setText("Update");
        submitButton.setOnAction(event -> onClickUpdate());
    }


}


class Principle {
    private String principle_id;
    private String principle_name;
    private String under_prefix;
    private String sub_principle_id;

    @Override
    public String toString() {
        return principle_name;
    }

    public Principle(String principle_id, String principle_name, String under_prefix, String sub_principle_id) {
        this.principle_id = principle_id;
        this.principle_name = principle_name;
        this.under_prefix = under_prefix;
        this.sub_principle_id = sub_principle_id;
    }

    public String getPrinciple_id() {
        return principle_id;
    }

    public void setPrinciple_id(String principle_id) {
        this.principle_id = principle_id;
    }

    public String getPrinciple_name() {
        return principle_name;
    }

    public void setPrinciple_name(String principle_name) {
        this.principle_name = principle_name;
    }

    public String getUnder_prefix() {
        return under_prefix;
    }

    public void setUnder_prefix(String under_prefix) {
        this.under_prefix = under_prefix;
    }

    public String getSub_principle_id() {
        return sub_principle_id;
    }

    public void setSub_principle_id(String sub_principle_id) {
        this.sub_principle_id = sub_principle_id;
    }
}
