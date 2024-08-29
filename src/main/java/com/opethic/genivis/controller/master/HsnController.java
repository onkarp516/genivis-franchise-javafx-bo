package com.opethic.genivis.controller.master;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.opethic.genivis.GenivisApplication;
import com.opethic.genivis.dto.CompanyAdminDTO;
import com.opethic.genivis.dto.reqres.product.Communicator;
import com.opethic.genivis.network.APIClient;
import com.opethic.genivis.network.EndPoints;
import com.opethic.genivis.network.RequestType;
import com.opethic.genivis.utils.*;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.Event;
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
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.URL;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;


public class HsnController implements Initializable {

    Map<String,String> rowID = new HashMap<>();

    @FXML
    private TextField hsnNo;
    @FXML
    private TextField hsnDesc;

    @FXML
    private ComboBox<String> comboBox;

    @FXML
    private TableView<ObservableList<StringProperty>> tableView;

    @FXML
    private TableColumn<ObservableList<StringProperty>, String> column1;

    @FXML
    private TableColumn<ObservableList<StringProperty>, String> column2;

    @FXML
    private TableColumn<ObservableList<StringProperty>, String> column3;

    @FXML
    private TextField tfSearch;

    @FXML
    private Button submitButton;

    @FXML
    private Button clearButton;
    @FXML
    private VBox vbHSNHeaderSect;

    Integer selected_row_index;

    Integer previousNoOfRows;

    @FXML
    private BorderPane vbox;

    private static final Logger hsnlogger = LogManager.getLogger(HsnController.class);

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        ResponsiveWiseCssPicker();

        Platform.runLater(()->{

            hsnNo.requestFocus();
            shortcutKeysHSN();
        });
        CommonValidationsUtils.restrictHsnNumber(hsnNo);
        CommonValidationsUtils.configureTextFieldToTitleCase(hsnDesc);
//        comboBox.setPromptText("Select");
        comboBox.getItems().addAll("Services", "Goods");
        comboBox.getSelectionModel().select(1);
        getHsnData("");
        tfSearch.setOnKeyTyped(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                String searchKey = tfSearch.getText().trim();
                if (searchKey.length() >= 3) {
                    getHsnData(searchKey);
                }else if(searchKey.isEmpty()){
                    getHsnData("");
                }
            }
        });

        sceneInitilization();

        vbox.addEventFilter(KeyEvent.KEY_PRESSED, (KeyEvent eve) -> {
            if (eve.getCode() == KeyCode.ENTER)
                if (eve.getTarget() instanceof Button targetButton && targetButton.getText().equalsIgnoreCase("Submit")) {
                }else if(eve.getTarget() instanceof Button targetButton && targetButton.getText().equalsIgnoreCase("Clear")){

                }
                else if(eve.getTarget() instanceof Button targetButton && targetButton.getText().equalsIgnoreCase("Update")){

                }
                else{
                    KeyEvent newEvent = new KeyEvent(
                            null,
                            null,
                            KeyEvent.KEY_PRESSED,
                            "",
                            "\t",
                            KeyCode.TAB,
                            eve.isShiftDown(),
                            eve.isControlDown(),
                            eve.isAltDown(),
                            eve.isMetaDown()
                    );
                    Event.fireEvent(eve.getTarget(), newEvent);
                    eve.consume();

                }
        });

//
//        if(comboBox.isFocused()){
//            comboBox.show();
//        }
//        comboBox.setOnKeyPressed(event -> {
//            if(event.getCode() != KeyCode.TAB || event.getCode() != KeyCode.ENTER){
//                comboBox.show();
//            }
//        });

//        Node[] nodes =new Node[]{hsnNo,hsnDesc,comboBox,submitButton,clearButton,tfSearch};
//        CommonValidationsUtils.setupFocusNavigation(nodes);
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tableView.setFocusTraversable(true);

        //handled table row selection
        tableView.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2 && !tableView.getSelectionModel().isEmpty()) {
                handleRowSelection();
            }
        });

        tableView.addEventFilter(KeyEvent.KEY_PRESSED,event -> {
//            System.out.println("key pressseddd "+event.getCode());
            if (event.getCode() == KeyCode.TAB ) {
                handleRowSelection();
                event.consume();
            }
        });

        // Add a key event listener to the HBOX
//        vbHSNHeaderSect.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
//            if (event.getCode() == KeyCode.DOWN) {
//                tfSearch.requestFocus();
////                event.consume();
//            }
//        });

        // Add a key event listener to the text field
        tfSearch.addEventHandler(KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode() == KeyCode.DOWN) {
                tableView.requestFocus();
                tableView.getSelectionModel().select(0);
                tableView.getFocusModel().focus(0);
                event.consume();
            }
            if (!event.isShiftDown() && event.getCode() == KeyCode.TAB) {
                tableView.requestFocus();
                tableView.getSelectionModel().select(0);
                tableView.getFocusModel().focus(0);
                event.consume();
            }
            if (event.isShiftDown() && event.getCode() == KeyCode.TAB) {
                clearButton.requestFocus();
            }

            if (event.getCode() == KeyCode.UP) {
                hsnNo.requestFocus();
            }
        });

        hsnNo.setOnKeyPressed(event -> {
            if(event.getCode() == KeyCode.DOWN){
                tfSearch.requestFocus();
                event.consume();
            }

            if(event.getCode() == KeyCode.ENTER || event.getCode() == KeyCode.TAB && !event.isShiftDown()){
                if (hsnNo.getText().isEmpty()) {
                    hsnNo.requestFocus();
                } else {
                    String btText = submitButton.getText();
                    if(btText.equalsIgnoreCase("Submit")){
                        hsnNoValidate();
                    }else{
                        hsnNoUpdateValidate();
                    }
                }
                event.consume();
            }
        });

        hsnDesc.setOnKeyPressed(event -> {
            if(event.getCode() == KeyCode.DOWN){
                tfSearch.requestFocus();
            }
        });

        comboBox.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal) {
                comboBox.show();
            }
        });


        submitButton.setOnKeyPressed(event -> {
            if(event.getCode() == KeyCode.RIGHT){
                clearButton.requestFocus();
            }
            if(event.getCode() == KeyCode.DOWN){
                tfSearch.requestFocus();
            }
        });
        clearButton.setOnKeyPressed(event -> {
            if(event.getCode() == KeyCode.LEFT){
                submitButton.requestFocus();
            }
            if(event.getCode() == KeyCode.DOWN){
                tfSearch.requestFocus();
            }
        });
        clearButton.setOnAction(e->{
            onClickClear();
        });


    }

    private void ResponsiveWiseCssPicker() {
        double height = Screen.getPrimary().getBounds().getHeight();
        double width = Screen.getPrimary().getBounds().getWidth();
        System.out.println("width" + width);
        if (width >= 992 && width <= 1024) {
            vbox.getStylesheets().add(GenivisApplication.class.getResource("ui/css/TranxCommonCssStyles/tranxCommonCssStyle1.css").toExternalForm());
        } else if (width >= 1025 && width <= 1280) {
            vbox.getStylesheets().add(GenivisApplication.class.getResource("ui/css/TranxCommonCssStyles/tranxCommonCssStyle2.css").toExternalForm());
        } else if (width >= 1281 && width <= 1368) {
            vbox.getStylesheets().add(GenivisApplication.class.getResource("ui/css/TranxCommonCssStyles/tranxCommonCssStyle3.css").toExternalForm());
        } else if (width >= 1369 && width <= 1400) {
            vbox.getStylesheets().add(GenivisApplication.class.getResource("ui/css/TranxCommonCssStyles/tranxCommonCssStyle4.css").toExternalForm());
        } else if (width >= 1401 && width <= 1440) {
            vbox.getStylesheets().add(GenivisApplication.class.getResource("ui/css/TranxCommonCssStyles/tranxCommonCssStyle5.css").toExternalForm());
        } else if (width >= 1441 && width <= 1680) {
            vbox.getStylesheets().add(GenivisApplication.class.getResource("ui/css/TranxCommonCssStyles/tranxCommonCssStyle6.css").toExternalForm());
        } else if (width >= 1681 && width <= 1920) {
            vbox.getStylesheets().add(GenivisApplication.class.getResource("ui/css/TranxCommonCssStyles/tranxCommonCssStyle7.css").toExternalForm());
        }
    }

    public void sceneInitilization() {
        vbox.sceneProperty().addListener((observable, oldScene, newScene) -> {
            if (newScene != null && newScene.getWindow() instanceof Stage) {
                Communicator.stage = (Stage) newScene.getWindow();
            }
        });
    }

    private void handleRowSelection() {
        selected_row_index = tableView.getSelectionModel().getSelectedIndex();
        ObservableList<StringProperty> rowData = tableView.getSelectionModel().getSelectedItem();
        String hsnNoValue = rowData.get(0).getValue();
        String hsnDescValue = rowData.get(1).getValue();
        String typeValue = rowData.get(2).getValue();
        String idValue = rowData.get(3).getValue();
//        System.out.println("Selected Row Data: ");
//        System.out.println("HSN No: " + hsnNoValue);
//        System.out.println("HSN Description: " + hsnDescValue);
//        System.out.println("Type: " + typeValue);
//        System.out.println("Id: " + idValue);
        hsnNo.requestFocus();
        hsnNo.setText(hsnNoValue);
        hsnDesc.setText(hsnDescValue);
        comboBox.setValue(typeValue);

        rowID.put("id",idValue);

        submitButton.setText("Update");
    }

    public void getHsnData(String searchTerm){
        try {
            //Clear previous table data
            tableView.getItems().clear();

            //Columns initialization
            column1.setCellValueFactory(cellData -> {
                if (!cellData.getValue().isEmpty()) {
                    return cellData.getValue().get(0);
                } else {
                    return new SimpleStringProperty("");
                }
            });
            column2.setCellValueFactory(cellData -> {
                if (!cellData.getValue().isEmpty() && cellData.getValue().size() > 1) {
                    return cellData.getValue().get(1);
                } else {
                    return new SimpleStringProperty("");
                }
            });
            column3.setCellValueFactory(cellData -> {
                if (!cellData.getValue().isEmpty() && cellData.getValue().size() > 2) {
                    return cellData.getValue().get(2);
                } else {
                    return new SimpleStringProperty("");
                }
            });

            Map<String,String> map = new HashMap<>();
            map.put("search_term",searchTerm);
            String formData=Globals.mapToStringforFormData(map);

            //Called API for get HSN Data
            HttpResponse<String> response = APIClient.postFormDataRequest(formData,"search_hsn");
            JsonObject jsonObject = new Gson().fromJson(response.body(), JsonObject.class);
            System.out.println(jsonObject);

            if (jsonObject.get("responseStatus").getAsInt() == 200) {
                JsonArray responseObject = jsonObject.getAsJsonArray("responseObject");

                if (responseObject.size() > 0) {
                    for (JsonElement element : responseObject) {
                        JsonObject item = element.getAsJsonObject();
                        //String hsnNo = item.get("hsnno").getAsString();
                        JsonElement typeElement3  = item.get("hsnno");
                        String hsnNo = (typeElement3 != null && !typeElement3.isJsonNull()) ? typeElement3.getAsString() : "";
                        JsonElement typeElement2  = item.get("hsndesc");
                        String hsnDesc = (typeElement2 != null && !typeElement2.isJsonNull()) ? typeElement2.getAsString() : "";
                        JsonElement typeElement = item.get("type");
                        String type = (typeElement != null && !typeElement.isJsonNull()) ? typeElement.getAsString() : "";

                        //String id = item.get("id").getAsString();

                        JsonElement typeElement4 = item.get("id");
                        String id = (typeElement4 != null && !typeElement4.isJsonNull()) ? typeElement4.getAsString() : "";

                        System.out.println("hsnno: " + hsnNo);
                        System.out.println("hsndesc: " + hsnDesc);
                        System.out.println("type: " + type);
                        System.out.println("id: " + id);


                        ObservableList<StringProperty> row = FXCollections.observableArrayList(
                                new SimpleStringProperty(hsnNo),
                                new SimpleStringProperty(hsnDesc),
                                new SimpleStringProperty(type),
                                new SimpleStringProperty(id)
                        );

                        tableView.getItems().add(0, row);
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
        if(previousNoOfRows!=null && currentNoOfRows>previousNoOfRows) {
            tableView.getSelectionModel().select(0);
        }

        if(selected_row_index!=null) {
            tableView.getSelectionModel().select(selected_row_index);
            selected_row_index = null;
        }
    }

    public void submitHsnData(){
        ObservableList<ObservableList<StringProperty>> rows = tableView.getItems();
        previousNoOfRows = rows.size();

        String hsnNumber = hsnNo.getText();
        String hsnDescrition = hsnDesc.getText();

        Map<String,String> map = new HashMap<>();
        map.put("hsnNumber",hsnNumber);
        map.put("description",(hsnDescrition!=null?hsnDescrition:""));
        map.put("type",(comboBox.getValue() != null ? comboBox.getValue() : ""));

        String formData=Globals.mapToStringforFormData(map);
        System.out.println("hsnNumber-- "+hsnNumber);
        if(hsnNumber.equalsIgnoreCase("")){
            AlertUtility.AlertErrorTimeout("Error","Please Fill HSN number",callback->{
                hsnNo.requestFocus();
            });
        }else{
            HttpResponse<String> response = APIClient.postFormDataRequest(formData,"create_hsn");
            JsonObject responseBody = new Gson().fromJson(response.body(), JsonObject.class);
            System.out.println("Response=>"+responseBody);
            if (responseBody.get("responseStatus").getAsInt() == 200) {

                AlertUtility.AlertSuccessTimeout("success",responseBody.get("message").getAsString(),input->{});

                onClickClear();

                getHsnData("");
            }
            else {
                AlertUtility.AlertErrorTimeout("Error",responseBody.get("message").getAsString(),input->{});
            }
        }
    }

    public void updateHsnData(){
        String hsnNumber = hsnNo.getText();
        String hsnDescrition = hsnDesc.getText();

        Map<String,String> map = new HashMap<>();
        map.put("hsnNumber",hsnNumber);
        map.put("description",(hsnDescrition!=null?hsnDescrition:""));
        map.put("type",(comboBox.getValue() != null ? comboBox.getValue() : ""));
        map.put("id",rowID.get("id"));


        if(hsnNumber.equalsIgnoreCase("")){
            AlertUtility.AlertErrorTimeout("Error","Please Fill HSN number",callback->{
                hsnNo.requestFocus();
            });
        }else{
            String formData=Globals.mapToStringforFormData(map);

            HttpResponse<String> response = APIClient.postFormDataRequest(formData,"update_hsn");

            JsonObject responseBody = new Gson().fromJson(response.body(), JsonObject.class);
            System.out.println("Response=>"+responseBody);

            if (responseBody.get("responseStatus").getAsInt() == 200) {
                AlertUtility.AlertSuccessTimeout("success",responseBody.get("message").getAsString(),input->{});
                onClickClear();
                getHsnData("");
            }
            else {
                AlertUtility.AlertErrorTimeout("Error",responseBody.get("message").getAsString(),input->{});
            }
        }

    }



    @FXML
    private void onClickClear() {

        hsnNo.clear();
        hsnDesc.clear();
//        comboBox.getSelectionModel().clearSelection();
        comboBox.getSelectionModel().select(1);

        // set the promptText to ComboBox
        comboBox.setPromptText("Select");
        comboBox.setButtonCell(new ListCell<String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty) ;
                if (empty || item == null) {
                    setText("Select");
                } else {
                    setText(item);
                }
            }
        });
        //-------------------------------------------------------------------------------


        submitButton.setText("Submit");
        hsnNo.requestFocus();

    }

    public void hsnNoValidate() {
        APIClient apiClient = null;
        try {

            Map<String, String> map = new HashMap<>();
            map.put("hsnNumber", String.valueOf(hsnNo.getText()));
            String formData = Globals.mapToStringforFormData(map);
            apiClient = new APIClient(EndPoints.PRODUCT_VALIDATE_HSN_ENDPOINT, formData, RequestType.FORM_DATA);
            apiClient.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
                @Override
                public void handle(WorkerStateEvent workerStateEvent) {
                    JsonObject jsonObject = new Gson().fromJson(workerStateEvent.getSource().getValue().toString(), JsonObject.class);
                    if (jsonObject.get("responseStatus").getAsInt() != 200) {
                        String message = jsonObject.get("message").getAsString();
                        AlertUtility.AlertErrorTimeout(AlertUtility.alertTypeError, message, in -> {
                            hsnNo.requestFocus();
                        });
                    }
                }
            });
            apiClient.setOnCancelled(new EventHandler<WorkerStateEvent>() {
                @Override
                public void handle(WorkerStateEvent workerStateEvent) {
                    hsnlogger.error("Network API cancelled in functionCompanyDuplicate()" + workerStateEvent.getSource().getValue().toString());
                }
            });
            apiClient.setOnFailed(new EventHandler<WorkerStateEvent>() {
                @Override
                public void handle(WorkerStateEvent workerStateEvent) {
                    hsnlogger.error("Network API failed in functionCompanyDuplicate()" + workerStateEvent.getSource().getValue().toString());
                }
            });
            apiClient.start();
        } catch (Exception e) {
            e.printStackTrace();
            //TODO : use logger for alert messages
//            showAlert("Falied to connect server! ");
            AlertUtility.AlertDialogForError("WARNING", "Failed to Connect Server !", input -> {
                if (input) {
                }
            });
        } finally {
            apiClient = null;
        }
    }

    public void hsnNoUpdateValidate() {
        APIClient apiClient = null;
        try {

            ObservableList<StringProperty> rowData = tableView.getSelectionModel().getSelectedItem();
            String idValue = rowData.get(3).getValue();
            Map<String, String> map = new HashMap<>();
            map.put("id", String.valueOf(idValue));
            map.put("hsnNumber", String.valueOf(hsnNo.getText()));
            String formData = Globals.mapToStringforFormData(map);
            apiClient = new APIClient(EndPoints.PRODUCT_VALIDATE_HSN_UPDATE_ENDPOINT, formData, RequestType.FORM_DATA);
            apiClient.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
                @Override
                public void handle(WorkerStateEvent workerStateEvent) {
                    JsonObject jsonObject = new Gson().fromJson(workerStateEvent.getSource().getValue().toString(), JsonObject.class);
                    if (jsonObject.get("responseStatus").getAsInt() != 200) {
                        String message = jsonObject.get("message").getAsString();
                        AlertUtility.AlertError(AlertUtility.alertTypeError, message, in -> {
                            hsnNo.requestFocus();
                        });
                    }
                }
            });
            apiClient.setOnCancelled(new EventHandler<WorkerStateEvent>() {
                @Override
                public void handle(WorkerStateEvent workerStateEvent) {
                    hsnlogger.error("Network API cancelled in functionCompanyDuplicate()" + workerStateEvent.getSource().getValue().toString());
                }
            });
            apiClient.setOnFailed(new EventHandler<WorkerStateEvent>() {
                @Override
                public void handle(WorkerStateEvent workerStateEvent) {
                    hsnlogger.error("Network API failed in functionCompanyDuplicate()" + workerStateEvent.getSource().getValue().toString());
                }
            });
            apiClient.start();
        } catch (Exception e) {
            e.printStackTrace();
            //TODO : use logger for alert messages
//            showAlert("Falied to connect server! ");
            AlertUtility.AlertDialogForError("WARNING", "Failed to Connect Server !", input -> {
                if (input) {
                }
            });
        } finally {
            apiClient = null;
        }
    }

    private void shortcutKeysHSN() {
        vbox.addEventFilter(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {

            @Override
            public void handle(KeyEvent event) {
                if(event.getCode()==KeyCode.S && event.isControlDown()){
                    if (CommonValidationsUtils.validateForm(hsnNo)) {
                        System.out.println("hello");
                        if (submitButton.getText().equalsIgnoreCase("submit")) {
                            AlertUtility.AlertConfirmation(AlertUtility.alertTypeConfirmation, "Do you want to Submit", input -> {
                                if (input == 1) {
                                    submitHsnData();
                                }
                            });
                        } else {
                            AlertUtility.AlertConfirmation(AlertUtility.alertTypeConfirmation, "Do you want to Update", input -> {
                                if (input == 1) {
                                    updateHsnData();
                                }
                            });
                        }
                    }
                }
                if (event.getCode() == KeyCode.E && event.isControlDown()) {
                    event.consume();
                    if (!tableView.getSelectionModel().isEmpty()) {
                        handleRowSelection();

                    } else{
                        AlertUtility.AlertError(AlertUtility.alertTypeError, "Select HSN First", in -> {
                            tableView.getSelectionModel().selectFirst();
                            tableView.requestFocus();
                            event.consume();
                        });
                    }
                }

                if (event.getCode()==KeyCode.X && event.isControlDown()) {
                    AlertUtility.AlertConfirmation(AlertUtility.alertTypeConfirmation, "Do you want to Clear", input -> {
                        if (input == 1) {
                            onClickClear();
                        }
                    });
                }

            }
        });
    }


    public void onCLickSubmitAndUpdate(ActionEvent actionEvent) {

        if(submitButton.getText().equalsIgnoreCase("submit")) {
            submitHsnData();
        }

        if(submitButton.getText().equalsIgnoreCase("update")) {

            updateHsnData();
        }

    }
}

