package com.opethic.genivis.controller.master;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.opethic.genivis.GenivisApplication;
import com.opethic.genivis.controller.master.ledger.common.LedgerMessageConsts;
import com.opethic.genivis.dto.AreaMasterDTO;
import com.opethic.genivis.dto.ContentMasterDTO;
import com.opethic.genivis.dto.DoctorMasterDTO;
import com.opethic.genivis.network.APIClient;
import com.opethic.genivis.network.EndPoints;
import com.opethic.genivis.network.RequestType;
import com.opethic.genivis.utils.AlertUtility;
import com.opethic.genivis.utils.CommonValidationsUtils;
import com.opethic.genivis.utils.GlobalController;
import com.opethic.genivis.utils.Globals;
import javafx.application.Platform;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
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
import javafx.scene.layout.HBox;
import javafx.stage.Screen;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import java.net.URL;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import static com.opethic.genivis.utils.FxmFileConstants.SALES_ORDER_LIST_SLUG;

public class ContentMasterController implements Initializable {
    @FXML
    private  BorderPane bpContentMaster;
    @FXML
    private Button btnContentMasterCreateSubmit, btnContentMasterCreateCancel;

    @FXML
    private HBox hbContentMasterHeaderSect,hbContentMasterSearchSect;
    @FXML
    private TextField tfContentMasterContentName, tfSearchData;
    ObservableList<ContentMasterDTO> orgRow = FXCollections.observableArrayList();
    @FXML
    private TableView<ContentMasterDTO> tcContentMasterView;
    @FXML
    private TableColumn<ContentMasterDTO, String> tcContentMasterContentName;
    private int idrow = 0;
    private static final Logger loggerContentMaster = LogManager.getLogger(ContentMasterController.class);

    public String id = "";
    String message = "";
    private JsonObject jsonObject = null;

    private String focusInd = "";
    private Integer rowIndex;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        ResponsiveWiseCssPicker();

        tcContentMasterView.setPlaceholder(
                new Label("No Data Found"));
        Platform.runLater(() -> {
            tfContentMasterContentName.requestFocus();
//            shortcutKeysContentMaster();
        });
        //? this include all the Shortcut Keys
        initShorcutKeys();
        CommonValidationsUtils.configureTextFieldToTitleCase(tfContentMasterContentName);
        tfContentMasterContentName.setOnKeyPressed(this::handleEnterKeyPressed);
        btnContentMasterCreateSubmit.setOnKeyPressed(this::handleEnterKeyPressed);
        btnContentMasterCreateCancel.setOnKeyPressed(this::handleEnterKeyPressed);
        tfSearchData.setOnKeyPressed(this::handleEnterKeyPressed);

        getOnLoadData();
        initLoadTableFunctions();
        tcContentMasterView.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                setEditData();
            }
        });
        tcContentMasterView.setOnKeyPressed(event -> {
            System.out.println(event);
            if (event.getCode() == KeyCode.TAB || event.getCode() == KeyCode.ENTER) {
                setEditData();
            }
        });
        btnContentMasterCreateCancel.setOnAction(event -> {
            AlertUtility.CustomCallback callback = number -> {
                if (number == 1) {
                    ClearForm();
                } else {
                }
            };
            AlertUtility.AlertConfirmation(AlertUtility.alertTypeConfirmation, LedgerMessageConsts.msgConfirmationOnClear, callback);
        });
        tfSearchData.textProperty().addListener((observable, oldValue, newValue) -> {
            FilterTableData(newValue);
        });
        btnContentMasterCreateSubmit.setOnAction(e ->
        {
            validateSubmitData();
        });

//        tfContentNameValidation();

//        hbContentMasterHeaderSect.s
        // Add a key event listener to the text field
        tfContentMasterContentName.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode() == KeyCode.DOWN) {
                tfSearchData.requestFocus();
                event.consume();
            }
        });

        tfContentMasterContentName.setOnKeyPressed(event->{

            if (event.getCode() == KeyCode.ENTER || !event.isShiftDown() && event.getCode() == KeyCode.TAB) {
                if (tfContentMasterContentName.getText().isEmpty()){
                    tfContentMasterContentName.requestFocus();
                    event.consume();
                }

            }
        });

        tfSearchData.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode() == KeyCode.DOWN) {
                tcContentMasterView.getSelectionModel().selectFirst();
                tcContentMasterView.requestFocus();
                event.consume();
            }
        });

    }

    private void ResponsiveWiseCssPicker() {
        double height = Screen.getPrimary().getBounds().getHeight();
        double width = Screen.getPrimary().getBounds().getWidth();
        System.out.println("width" + width);
        if (width >= 992 && width <= 1024) {
            bpContentMaster.getStylesheets().add(GenivisApplication.class.getResource("ui/css/TranxCommonCssStyles/tranxCommonCssStyle1.css").toExternalForm());
        } else if (width >= 1025 && width <= 1280) {
            bpContentMaster.getStylesheets().add(GenivisApplication.class.getResource("ui/css/TranxCommonCssStyles/tranxCommonCssStyle2.css").toExternalForm());
        } else if (width >= 1281 && width <= 1368) {
            bpContentMaster.getStylesheets().add(GenivisApplication.class.getResource("ui/css/TranxCommonCssStyles/tranxCommonCssStyle3.css").toExternalForm());
        } else if (width >= 1369 && width <= 1400) {
            bpContentMaster.getStylesheets().add(GenivisApplication.class.getResource("ui/css/TranxCommonCssStyles/tranxCommonCssStyle4.css").toExternalForm());
        } else if (width >= 1401 && width <= 1440) {
            bpContentMaster.getStylesheets().add(GenivisApplication.class.getResource("ui/css/TranxCommonCssStyles/tranxCommonCssStyle5.css").toExternalForm());
        } else if (width >= 1441 && width <= 1680) {
            bpContentMaster.getStylesheets().add(GenivisApplication.class.getResource("ui/css/TranxCommonCssStyles/tranxCommonCssStyle6.css").toExternalForm());
        } else if (width >= 1681 && width <= 1920) {
            bpContentMaster.getStylesheets().add(GenivisApplication.class.getResource("ui/css/TranxCommonCssStyles/tranxCommonCssStyle7.css").toExternalForm());
        }
    }

    public void tfContentNameValidation() {
        tfContentMasterContentName.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal) {
                if (tfContentMasterContentName.getText().isEmpty()) {
                    tfContentMasterContentName.requestFocus();
                }
            }
        });
    }

    private void setFocus() {
        Platform.runLater(() -> {
            tfContentMasterContentName.requestFocus();
        });
    }

    private void handleEnterKeyPressed(KeyEvent keyEvent) {
        Node source = (Node) keyEvent.getSource();
        if (keyEvent.getCode() == KeyCode.ENTER)
        {
            switch (source.getId()) {
                case "tfContentMasterContentName":
                    if (!tfContentMasterContentName.getText().isEmpty()) {
                        Platform.runLater(()->{
                            btnContentMasterCreateSubmit.requestFocus();
                        });
                    }
                    break;
                case "btnContentMasterCreateSubmit":
                    validateSubmitData();
                    break;
                default:
                    break;
            }
        }
    }

    private void FilterTableData(String newValue) {
        FilteredList<ContentMasterDTO> filteredList;
        filteredList = new FilteredList<>(orgRow);
        final String searchString = newValue.toUpperCase();
        filteredList.setPredicate(logEvent -> {
            if (searchString.isEmpty()) {
                return true;
            }
            String filterBy = "contentName";
            String targetString = "";
            try {
                targetString = ((StringProperty) logEvent.getPropertyByName(filterBy)).getValue();
            } catch (Exception e) {
                loggerContentMaster.error("Exception FilterTableData() : " + Globals.getExceptionString(e));
            }
            return targetString.toUpperCase().contains(searchString.toUpperCase());
        });
        tcContentMasterView.setItems(filteredList);
    }

    public void getOnLoadData() {
        try {
            tcContentMasterView.getItems().clear();
            HttpResponse<String> response = APIClient.getRequest(EndPoints.getAllContentMaster);
            JSONObject jsonObject = new JSONObject(response.body());
            if (jsonObject.getInt("responseStatus") == 200) {
                JSONArray jarr = jsonObject.getJSONArray("responseObject");
                orgRow.clear();
                if (jarr.length() > 0) {
                    for (Object object : jarr) {
                        JSONObject obj = new JSONObject(object.toString());
                        orgRow.add(new ContentMasterDTO(String.valueOf(obj.getInt("id")), obj.getString("contentName")));
                    }
                    FilteredList<ContentMasterDTO> filteredList = new FilteredList<>(orgRow);
                    tcContentMasterContentName.setCellValueFactory(new PropertyValueFactory<>("contentName"));
                    tcContentMasterView.getItems().clear();
                    tcContentMasterView.getItems().addAll(filteredList);
                }
                if (focusInd.equalsIgnoreCase("focus")) {
                    tcContentMasterView.getSelectionModel().select(jarr.length() - 1);
                    tcContentMasterView.scrollTo(jarr.length() - 1);
                    tcContentMasterView.requestFocus();
                    focusInd = "";
                } else if (focusInd.equalsIgnoreCase("update")) {
                    tcContentMasterView.getSelectionModel().select(rowIndex);
                    tcContentMasterView.scrollTo(rowIndex);
                    tcContentMasterView.requestFocus();
                    focusInd = "";
                    btnContentMasterCreateSubmit.setText("Submit");
                }
            } else {
                String msg = jsonObject.getString("message");
//                AlertUtility.CustomCallback callback = number -> {
//                    setFocus();
//                };
//                AlertUtility.AlertError(AlertUtility.alertTypeError, msg, callback);
                AlertUtility.AlertSuccessTimeout(AlertUtility.alertTypeSuccess, msg, input -> {
                   setFocus();
                });
            }
        } catch (Exception e) {
            loggerContentMaster.error("Exception getOnLoadData() : " + Globals.getExceptionString(e));
        }
    }

    public void initLoadTableFunctions() {
        try {
            tcContentMasterView.setOnKeyPressed(event -> {
                if (event.getCode() == KeyCode.ENTER) {
                    ContentMasterDTO selectedItem = tcContentMasterView.getSelectionModel().getSelectedItem();
                    if (selectedItem != null) {
                        idrow = Integer.parseInt(selectedItem.getId());
                        tfContentMasterContentName.setText(selectedItem.getContentName());
                        btnContentMasterCreateSubmit.setText("Update");
                        setFocus();
                    } else {
                        String msg = "Record Not Found!";
                        AlertUtility.AlertSuccessTimeout(AlertUtility.alertTypeSuccess, msg, input -> {
                            setFocus();
                        });
                    }
                }
            });
        } catch (Exception e) {
            loggerContentMaster.error("Exception initLoadTableFunctions() : " + Globals.getExceptionString(e));
        }
    }

    private void setEditData() {
        rowIndex = tcContentMasterView.getSelectionModel().getSelectedIndex();
        ContentMasterDTO selectedItem = tcContentMasterView.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            idrow = Integer.parseInt(selectedItem.getId());
            tfContentMasterContentName.setText(selectedItem.getContentName());
            btnContentMasterCreateSubmit.setText("Update");
            setFocus();
        } else {
            String msg = "Record Not Found!";
            AlertUtility.AlertSuccessTimeout(AlertUtility.alertTypeSuccess, msg, input -> {
                setFocus();
            });
        }
    }


    private Boolean ValidateBeforeAddingNewData(String contentName) {
        try {
            Map<String, String> map = new HashMap<>();
            map.put("contentName", contentName);
            String formData = Globals.mapToStringforFormData(map);
            HttpResponse<String> response = APIClient.postFormDataRequest(formData, EndPoints.validateContentMaster);
            JSONObject jsonObject = new JSONObject(response.body());
            if (jsonObject.getInt("responseStatus") == 200) {
                return true;
            } else if (jsonObject.getInt("responseStatus") == 409) {
                String msg = jsonObject.getString("message");
                AlertUtility.AlertSuccessTimeout(AlertUtility.alertTypeSuccess, msg, input -> {
                    setFocus();
                });
                return false;
            }
        } catch (Exception e) {
            loggerContentMaster.error("Exception ValidateBeforeAddingNewData() : " + Globals.getExceptionString(e));
        }
        return false;
    }

    private void ClearForm() {
        idrow = 0;
        tfContentMasterContentName.clear();
        tfSearchData.clear();
        btnContentMasterCreateSubmit.setText("Submit");
        setFocus();
    }

    private void UpdateExistingData(int idrow, String contentName) {
        try {
            Map<String, String> map = new HashMap<>();
            map.put("id", idrow + "");
            map.put("contentName", contentName);
            String formData = Globals.mapToStringforFormData(map);
            HttpResponse<String> response = APIClient.postFormDataRequest(formData, EndPoints.updateContentMaster);
            JSONObject jsonObject = new JSONObject(response.body());
                            String msg = jsonObject.getString("message");
//            if (jsonObject.getInt("responseStatus") == 200) {
//                String msg = jsonObject.getString("message");
//                AlertUtility.CustomCallback callback = number -> {
//                    ClearForm();
//                };
//                AlertUtility.AlertSuccess(AlertUtility.alertTypeSuccess, msg, callback);
//                getOnLoadData();
//            } else {
//                String msg = jsonObject.getString("message");
//                AlertUtility.CustomCallback callback = number -> {
//                    setFocus();
//                };
//                AlertUtility.AlertError(AlertUtility.alertTypeError, msg, callback);
//            }
            if (jsonObject.getInt("responseStatus") == 200) {
                focusInd = "update";
                AlertUtility.AlertSuccessTimeout(AlertUtility.alertTypeSuccess, msg, input -> {
                    ClearForm();
                    getOnLoadData();
                });
            } else {
                AlertUtility.AlertErrorTimeout(AlertUtility.alertTypeError, msg, in -> {
                    setFocus();
                });
            }
        } catch (Exception e) {
            loggerContentMaster.error("Exception UpdateExistingData() : " + Globals.getExceptionString(e));
        }
    }

    private void CreateNewData(String contentName) {
        try {
            Boolean isCreateFlag = ValidateBeforeAddingNewData(contentName);
            if (isCreateFlag == true) {
                Map<String, String> map = new HashMap<>();
                map.put("contentName", contentName);
                String formData = Globals.mapToStringforFormData(map);
                HttpResponse<String> response = APIClient.postFormDataRequest(formData, EndPoints.createContentMaster);
                JSONObject jsonObject = new JSONObject(response.body());
                String msg = jsonObject.getString("message");
//                if (jsonObject.getInt("responseStatus") == 200) {
//                    getOnLoadData();
//                    ClearForm();
//                } else {
//                    String msg = jsonObject.getString("message");
//                    AlertUtility.CustomCallback callback = number -> {
//                        setFocus();
//                    };
//                    AlertUtility.AlertError(AlertUtility.alertTypeError, msg, callback);
//                }
                if (jsonObject.getInt("responseStatus") == 200) {
                    focusInd = "focus";
                    AlertUtility.AlertSuccessTimeout(AlertUtility.alertTypeSuccess, msg, input -> {
                        ClearForm();
                        getOnLoadData();
                    });
                } else {
                    AlertUtility.AlertErrorTimeout(AlertUtility.alertTypeError, msg, in -> {
                        setFocus();
                    });
                }
            } else {
                String msg = "Record Is Already Present !";
                AlertUtility.AlertErrorTimeout(AlertUtility.alertTypeError, msg, in -> {
                    ClearForm();
                    setFocus();
                });
            }
        } catch (Exception e) {
            loggerContentMaster.error("Exception CreateNewData() : " + Globals.getExceptionString(e));
        }

    }


    private void validateSubmitData() {
        try {
            if (CommonValidationsUtils.validateForm(tfContentMasterContentName)) {
                if (idrow > 0) {
                                        AlertUtility.AlertConfirmation(AlertUtility.alertTypeConfirmation, LedgerMessageConsts.msgConfirmationOnUpdate, input -> {
                        if (input == 1) UpdateExistingData(idrow, tfContentMasterContentName.getText());
                    });
                }else {
                                        AlertUtility.AlertConfirmation(AlertUtility.alertTypeConfirmation, LedgerMessageConsts.msgConfirmationOnSubmit , input ->{
                        if(input == 1) CreateNewData(tfContentMasterContentName.getText());
                    });
                }
            }
//            if (!tfContentMasterContentName.getText().isEmpty()) {
//                if (idrow > 0) {
//                    AlertUtility.AlertConfirmation(AlertUtility.alertTypeConfirmation, LedgerMessageConsts.msgConfirmationOnUpdate, input -> {
//                        if (input == 1) UpdateExistingData(idrow, tfContentMasterContentName.getText());
//                    });
//                } else {
//                    AlertUtility.AlertConfirmation(AlertUtility.alertTypeConfirmation, LedgerMessageConsts.msgConfirmationOnSubmit , input ->{
//                        if(input == 1) CreateNewData(tfContentMasterContentName.getText());
//                    });
//                }
//            } else {
//                String msg = "Content Name Should Not Be Blank!";
//                AlertUtility.CustomCallback callback = number -> {
//                    setFocus();
//                };
//                AlertUtility.AlertError(AlertUtility.alertTypeError, msg, callback);
//            }
        } catch (Exception e) {
            loggerContentMaster.error("Exception validateSubmitData() : " + Globals.getExceptionString(e));
        }

    }

//    private void shortcutKeysContentMaster() {
//        bpContentMaster.addEventFilter(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {
//
//            @Override
//            public void handle(KeyEvent event) {
//                if(event.getCode()==KeyCode.S && event.isControlDown()){
//                    if (CommonValidationsUtils.validateForm(tfContentMasterContentName)) {
//                        System.out.println("hello");
//                        if (btnContentMasterCreateSubmit.getText().equalsIgnoreCase("submit")) {
//                            validateSubmitData();
//                        } else {
//                            validateSubmitData();
//                        }
//                    }
//                }
//                if (event.getCode()==KeyCode.E && event.isControlDown()) {
//                    if (!tcContentMasterView.getSelectionModel().isEmpty()) {
//                        ContentMasterDTO selectedItem = tcContentMasterView.getSelectionModel().getSelectedItem();
//                        if (selectedItem != null) {
//                            idrow = Integer.parseInt(selectedItem.getId());
//                            tfContentMasterContentName.setText(selectedItem.getContentName());
//                            btnContentMasterCreateSubmit.setText("Update");
//                            setFocus();
//                        } else {
//                            String msg = "Record Not Found!";
//                            AlertUtility.CustomCallback callback = number -> {
//                                setFocus();
//                            };
//                            AlertUtility.AlertError(AlertUtility.alertTypeError, msg, callback);
//                        }
//                        event.consume();
//                    } else{
//                        AlertUtility.AlertError(AlertUtility.alertTypeError, "Select Content First", in -> {
//                            tcContentMasterView.getSelectionModel().selectFirst();
//                            tcContentMasterView.requestFocus();
//                            event.consume();
//                        });
//                    }
//                }
//
//                if (event.getCode()==KeyCode.X && event.isControlDown()) {
//                    AlertUtility.AlertConfirmation(AlertUtility.alertTypeConfirmation, "Do you want to Clear", input -> {
//                        if (input == 1) {
//                            ClearForm();
//                        }
//                    });
//                }
//
//            }
//        });
//    }

    private void initShorcutKeys() {
        bpContentMaster.addEventFilter(KeyEvent.KEY_PRESSED, (KeyEvent event) -> {

//            if (event.getCode() == KeyCode.DOWN && tfSearchData.isFocused()) {
//                tcContentMasterView.getSelectionModel().select(0);
//                tcContentMasterView.requestFocus();
//            } else if (event.getCode() == KeyCode.DOWN && tfContentMasterContentName.isFocused()) {
//                tcContentMasterView.getSelectionModel().select(0);
//                tcContentMasterView.requestFocus();
//            }

            if (event.getCode() == KeyCode.ENTER) {
                if (event.getTarget() instanceof Button targetButton && targetButton.getText().equalsIgnoreCase("submit")) {
                    System.out.println(targetButton.getText());
                } else if (event.getTarget() instanceof Button targetButton && targetButton.getText().equalsIgnoreCase("Clear")) {
                    System.out.println(targetButton.getText());
                } else if (event.getTarget() instanceof Button targetButton && targetButton.getText().equalsIgnoreCase("update")) {
                    System.out.println(targetButton.getText());
                } else {
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
            }else if (event.getCode() == KeyCode.S && event.isControlDown()) {
                btnContentMasterCreateSubmit.fire();
            }else if (event.getCode() == KeyCode.X && event.isControlDown()) {
                btnContentMasterCreateCancel.fire();
            }else if (event.getCode() == KeyCode.E && event.isControlDown()) {
                setEditData();
            }else if (event.getCode() == KeyCode.D && event.isControlDown()) {
                handleDeletePressed();
            }else if (event.getCode() == KeyCode.N && event.isControlDown()) {
                tfContentMasterContentName.requestFocus();
            }
//            if (event.getCode() == KeyCode.D && event.isControlDown()) {
//                setDeleteData();
//            }

        });
    }
    @FXML
    private void handleDeletePressed() {
        ContentMasterDTO selectedItem = tcContentMasterView.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            id = String.valueOf(selectedItem.getId());
            deleteContentMaster();
        }
    }
    public void deleteContentMaster() {
        int index  =  tcContentMasterView.getSelectionModel().getFocusedIndex();
        AlertUtility.CustomCallback callback = number -> {
            if (number == 1) {
                APIClient apiClient = null;
                Map<String, String> map = new HashMap<>();
                map.put("id", id);

                HttpResponse<String> response;
                String formData = Globals.mapToStringforFormData(map);
                apiClient = new APIClient(EndPoints.CONTENT_MASTER_DELETE_ENDPOINT, formData, RequestType.FORM_DATA);

                apiClient.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
                    @Override
                    public void handle(WorkerStateEvent workerStateEvent) {
                        // JsonObject responseBody = new Gson().fromJson(response.body(), JsonObject.class);
                        jsonObject = new Gson().fromJson(workerStateEvent.getSource().getValue().toString(), JsonObject.class);

                        message = jsonObject.get("message").getAsString();
                        if (jsonObject.get("responseStatus").getAsInt() == 409) {
                            AlertUtility.AlertErrorTimeout(AlertUtility.alertTypeError, message, input -> {
                                tcContentMasterView.getSelectionModel().select(index);
                                tcContentMasterView.scrollTo(index);
                                id="";
                            });
                        }

                        if (jsonObject.get("responseStatus").getAsInt() == 200) {
                            AlertUtility.AlertSuccessTimeout(AlertUtility.alertTypeSuccess, message, input -> {
                                tfContentMasterContentName.requestFocus();
                                id = "";
                            });
                        } else {
                            AlertUtility.AlertErrorTimeout(AlertUtility.alertTypeError, message, in -> {
                            });
                        }
                        getOnLoadData();
                        ClearForm();

                    }
                });

                apiClient.setOnCancelled(new EventHandler<WorkerStateEvent>() {
                    @Override
                    public void handle(WorkerStateEvent workerStateEvent) {
                        loggerContentMaster.error("Network API cancelled in deleteContentMaster()" + workerStateEvent.getSource().getValue().toString());

                    }
                });
                apiClient.setOnFailed(new EventHandler<WorkerStateEvent>() {
                    @Override
                    public void handle(WorkerStateEvent workerStateEvent) {
                        loggerContentMaster.error("Network API failed in deleteContentMaster()" + workerStateEvent.getSource().getValue().toString());

                    }
                });
                apiClient.start();
                loggerContentMaster.debug("delete doctor mode data end ...");
            } else {
            }
        };
        AlertUtility.AlertConfirmation(AlertUtility.alertTypeConfirmation, LedgerMessageConsts.msgConfirmationOnDelete, callback);
    }
}
