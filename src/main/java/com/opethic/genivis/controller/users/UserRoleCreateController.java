package com.opethic.genivis.controller.users;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.opethic.genivis.GenivisApplication;
import com.opethic.genivis.controller.master.CompanyUserController;
import com.opethic.genivis.network.APIClient;
import com.opethic.genivis.network.EndPoints;
import com.opethic.genivis.network.RequestType;
import com.opethic.genivis.utils.*;
import javafx.application.Platform;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.stage.Screen;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.URL;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import static com.opethic.genivis.utils.FxmFileConstants.COMPANY_LIST_SLUG;
import static com.opethic.genivis.utils.FxmFileConstants.ROLE_LIST_SLUG;
import static com.opethic.genivis.utils.Globals.userRoleModel;

public class UserRoleCreateController implements Initializable {
    @FXML
    public Button btnRoleCreateSubmit;
    @FXML
    public BorderPane bpRoot;
    @FXML
    VBox vbaccess;
    HBox hBox;
    int index = 0;
    @FXML
    TextField tfroletitle;
    @FXML
    HBox topMainDiv;
    private static final Logger userRoleCreateLogger = LogManager.getLogger(UserRoleListController.class);
    GridPane gpgrid;
    JsonArray permHeadArray, permDataArray, masterFunction;
    JsonObject mappingActions;
    String colData;
    String permData;
    Font font;
    @FXML
    Button btnRoleCreateCancel;
    int permissionCount=0;
    public static Integer SelectedRowIndex;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        CommonValidationsUtils.changeStarColour2(topMainDiv);
        tfroletitle.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal) {
                String text = tfroletitle.getText().trim();
                if (text.isEmpty()) {
                    tfroletitle.requestFocus();
                }
            }
        });

        ResponsiveWiseCssPicker();
        if(userRoleModel != null){
            btnRoleCreateSubmit.setText("Update");
        }

        font = Font.font(Font.getDefault().getFamily(), FontWeight.BOLD, FontPosture.REGULAR, 14);

        masterFunction = new JsonArray();
        ScrollPane scrollPane = new ScrollPane();
        gpgrid = new GridPane();
        gpgrid.setVgap(10.0);
        gpgrid.setHgap(20.0);
        gpgrid.setStyle("-fx-padding:8px");

        Platform.runLater(() -> {
            runLaterCode();
        });
        bpRoot.addEventFilter(javafx.scene.input.KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {

                if (event.getCode() == KeyCode.ENTER) {
                    if (tfroletitle.isFocused()) {
                        gpgrid.getChildren().stream().toList().get(8).requestFocus();
                    } else if (btnRoleCreateSubmit.isFocused()) {
                        btnRoleCreateSubmit.fire();
                    } else if (btnRoleCreateCancel.isFocused()) {
                        btnRoleCreateCancel.fire();
                    }

                }

                if (event.getCode() == KeyCode.DOWN && tfroletitle.isFocused()) {
                    gpgrid.getChildren().stream().toList().get(8).requestFocus();
                }

                if (event.getCode() == KeyCode.S && event.isControlDown()) {
                    if(btnRoleCreateSubmit.getText().equalsIgnoreCase("Submit")){
                        AlertUtility.AlertConfirmation(AlertUtility.alertTypeConfirmation, "Are you sure want to Submit ?", input -> {
                            if (input == 1) {
                                submitData();
                            }
                        });
                    }
                    else{
                        AlertUtility.AlertConfirmation(AlertUtility.alertTypeConfirmation, "Are you sure want to Update ?", input -> {
                            if (input == 1) {
                                submitData();
                            }
                        });
                    }
                }

                if (event.getCode() == KeyCode.X && event.isControlDown()) {
                    UserRoleListController.focusStatus = "cancle";
                    if(SelectedRowIndex != null && !SelectedRowIndex.equals("")){
                        UserRoleListController.SelectedRowIndex = SelectedRowIndex;
                    }
                    GlobalSadminController.getInstance().addTabStatic(ROLE_LIST_SLUG, true);
                }
            }
        });

        hBox = new HBox();
        hBox.setAlignment(Pos.CENTER_RIGHT);
        hBox.setPadding(new Insets(10, 10, 10, 10));
        hBox.setSpacing(20);

        if (userRoleModel != null) {
            btnRoleCreateSubmit.setText("Update");
        }

        Platform.runLater(() -> {
            btnRoleCreateSubmit.setOnKeyPressed(event->{
                if(event.getCode() == KeyCode.SPACE){
                    if(btnRoleCreateSubmit.getText().equalsIgnoreCase("Submit")){
                        AlertUtility.AlertConfirmation(AlertUtility.alertTypeConfirmation, "Are you sure want to Submit ?", input -> {
                            if (input == 1) {
                                submitData();
                            }
                        });
                    }
                    else{
                        AlertUtility.AlertConfirmation(AlertUtility.alertTypeConfirmation, "Are you sure want to Update ?", input -> {
                            if (input == 1) {
                                submitData();
                            }
                        });
                    }

                }
                else if (event.getCode() == KeyCode.ENTER || event.getCode() == KeyCode.TAB) {
                            btnRoleCreateCancel.requestFocus();
                        }
            });
        });

        btnRoleCreateSubmit.setOnMouseClicked(event->{
            if(btnRoleCreateSubmit.getText().equalsIgnoreCase("Submit")){
                AlertUtility.AlertConfirmation(AlertUtility.alertTypeConfirmation, "Are you sure want to Submit ?", input -> {
                    if (input == 1) {
                        submitData();
                    }
                });
            }
            else{
                AlertUtility.AlertConfirmation(AlertUtility.alertTypeConfirmation, "Are you sure want to Update ?", input -> {
                    if (input == 1) {
                        submitData();
                    }
                });
            }
        });
        btnRoleCreateSubmit.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                submitData();
            }
        });


        Label label = new Label();
        label.setText("Perticulars");
        label.setFont(font);
        gpgrid.add(label, 0, 0);
        //gpgrid.getColumnConstraints().add(firstColumnConstraints);

        btnRoleCreateCancel.setOnAction((event) -> {
            AlertUtility.AlertConfirmation(AlertUtility.alertTypeConfirmation, "Are you sure want Cancel ?", input -> {
                if (input == 1) {
                    UserRoleListController.focusStatus = "cancle";
                    if(SelectedRowIndex != null && !SelectedRowIndex.equals("")){
                        UserRoleListController.SelectedRowIndex = SelectedRowIndex;
                    }
                    GlobalSadminController.getInstance().addTabStatic(ROLE_LIST_SLUG, false);

                }
            });

        });
        scrollPane.setContent(gpgrid);
        vbaccess.getChildren().add(scrollPane);
        vbaccess.getChildren().add(hBox);

        /**** get API calling ******/
        getColData();
        getPermData();

    }

    private void ResponsiveWiseCssPicker() {
        double height = Screen.getPrimary().getBounds().getHeight();
        double width = Screen.getPrimary().getBounds().getWidth();
        System.out.println("width" + width);
        if (width >= 992 && width <= 1024) {
            bpRoot.getStylesheets().add(GenivisApplication.class.getResource("ui/css/TranxCommonCssStyles/tranxCommonCssStyle1.css").toExternalForm());
        } else if (width >= 1025 && width <= 1280) {
            bpRoot.getStylesheets().add(GenivisApplication.class.getResource("ui/css/TranxCommonCssStyles/tranxCommonCssStyle2.css").toExternalForm());
        } else if (width >= 1281 && width <= 1368) {
            bpRoot.getStylesheets().add(GenivisApplication.class.getResource("ui/css/TranxCommonCssStyles/tranxCommonCssStyle3.css").toExternalForm());
        } else if (width >= 1369 && width <= 1400) {
            bpRoot.getStylesheets().add(GenivisApplication.class.getResource("ui/css/TranxCommonCssStyles/tranxCommonCssStyle4.css").toExternalForm());
        } else if (width >= 1401 && width <= 1440) {
            bpRoot.getStylesheets().add(GenivisApplication.class.getResource("ui/css/TranxCommonCssStyles/tranxCommonCssStyle5.css").toExternalForm());
        } else if (width >= 1441 && width <= 1680) {
            bpRoot.getStylesheets().add(GenivisApplication.class.getResource("ui/css/TranxCommonCssStyles/tranxCommonCssStyle6.css").toExternalForm());
        } else if (width >= 1681 && width <= 1920) {
            bpRoot.getStylesheets().add(GenivisApplication.class.getResource("ui/css/TranxCommonCssStyles/tranxCommonCssStyle7.css").toExternalForm());
        }
    }

    private void submitData() {
        if (tfroletitle.getText().isEmpty()) {
            AlertUtility.CustomCallback callback = number -> {
                if (number == 1) {
                    tfroletitle.requestFocus();
                }
            };
            AlertUtility.AlertError("Role Name", "Role name is mandatory", callback);
        }else if(permissionCount<=0){
            AlertUtility.CustomCallback callback = number -> {

            };
            AlertUtility.AlertError("Access Permission", "Please select atleast one access permission", callback);
        } else {

            if (userRoleModel == null) {
                createUserRole();
            } else {
                updateUserRole();
            }
        }
    }

    private void runLaterCode() {
        tfroletitle.requestFocus();
    }


    private void updateUserRole() {
        String roleName = tfroletitle.getText();
        String response = "";
        masterFunction = new JsonArray();
        for (Node node : gpgrid.getChildren()) {
            if (node instanceof CheckBox) {
                int rowIndex = GridPane.getRowIndex(node);
                int colIndex = GridPane.getColumnIndex(node);
                CheckBox checkBox = (CheckBox) node;

                if (colIndex > 0 && checkBox.isSelected()) {

                    boolean isRecordFound = false;
                    for (int s = 0; s < masterFunction.size(); s++) {
                        JsonObject jsonObject = masterFunction.get(s).getAsJsonObject();
                        if (jsonObject.get("mapping_id").getAsInt() == rowIndex) {
                            masterFunction.get(s).getAsJsonObject().get("actions").getAsJsonArray().add((colIndex) + "");
                            isRecordFound = true;
                        }
                    }
                    if (!isRecordFound) {
                        //Add in masterFunctions
                        JsonObject jsonObject = new JsonObject();
                        JsonArray actions = new JsonArray();

                        //JsonObject mappingData=getMappingId(rowIndex-1,colIndex-1);
                        JsonObject mappingData = permDataArray.get(rowIndex - 1).getAsJsonObject().get("level").getAsJsonObject();
                        jsonObject.addProperty("mapping_id", mappingData.get("id").getAsInt());
                        jsonObject.addProperty("mapping_name", mappingData.get("name").getAsString());
                        actions.add((colIndex) + "");
                        jsonObject.add("actions", actions);
                        masterFunction.add(jsonObject);

                    }
                }
            }
        }
        String mstFunctions = new Gson().toJson(masterFunction);

        Map<String, String> params = new HashMap<>();
        params.put("id", userRoleModel.getId() + "");
        params.put("roleName", roleName);
        params.put("role_permissions", mstFunctions);
        params.put("del_role_permissions", "[]");
        String req = Globals.mapToStringforFormData(params);

        APIClient apiClient = new APIClient(EndPoints.updateUserRole, req, RequestType.FORM_DATA);

        AlertUtility.CustomCallback callback = number -> {
            if (number == 1) {
                GlobalSadminController.getInstance().addTabStatic(ROLE_LIST_SLUG, false);
            }
        };

        apiClient.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent workerStateEvent) {


                if (workerStateEvent.getSource().getValue() != null) {
                    JsonObject jsonObject = new Gson().fromJson(workerStateEvent.getSource().getValue().toString(), JsonObject.class);

                    if (jsonObject.get("responseStatus").getAsInt() == 200) {
                        UserRoleListController.focusStatus = "edit";
                        UserRoleListController.SelectedRowIndex = SelectedRowIndex;
                        AlertUtility.AlertSuccessTimeout("User Role Update", jsonObject.get("message").getAsString(), callback);
                    } else {
                        AlertUtility.AlertErrorTimeout("Failed", jsonObject.get("message").getAsString(), callback);
                    }

                    userRoleModel = null;
                } else {

                    AlertUtility.AlertError("Failed", "User Role Update Failed, Try Later!", callback);
                }
            }
        });
        apiClient.setOnFailed(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent workerStateEvent) {
                AlertUtility.AlertError("Failed", "User Role Update Failed, Try Later!", callback);
                userRoleCreateLogger.error("User Role Update Failed : " + workerStateEvent.getSource().getValue().toString());
            }
        });
        apiClient.setOnCancelled(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent workerStateEvent) {
                AlertUtility.AlertError("Failed", "User Role Update Failed, Try Later!", callback);
                userRoleCreateLogger.error("User Role Update Cancelled : " + workerStateEvent.getSource().getValue().toString());
            }
        });
        apiClient.start();


    }

    public Node getNodeByRowColumnIndex(int row, int column, GridPane gridPane) {
        Node result = null;
//        System.out.println("Size=>"+gridPane.getChildren().size()+"Row=>"+row+"Column=>"+column);
        ObservableList<Node> childrens = gridPane.getChildren();

        for (Node node : childrens) {
            if (gridPane.getRowIndex(node) == row && gridPane.getColumnIndex(node) == column) {
                result = node;
                break;
            }
        }

        return result;
    }

    private void getPermDataById() {
        Map<String, String> reqData = new HashMap<>();
        reqData.put("id", userRoleModel.getId() + "");
        String request = Globals.mapToStringforFormData(reqData);
        /*HttpResponse<String> response = APIClient.postFormDataRequest(request, "get_role_by_id");
        if(response!=null){
            response.body();
        }*/

        APIClient apiClient = new APIClient(EndPoints.GET_ROLE_BY_ID, request, RequestType.FORM_DATA);
        apiClient.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent workerStateEvent) {

                if (workerStateEvent.getSource().getValue() != null) {
                    System.out.println("Config Permissions=>" + workerStateEvent.getSource().getValue().toString());
                    JsonObject permDataJsonObject = new Gson().fromJson(workerStateEvent.getSource().getValue().toString(), JsonObject.class).get("responseObject").getAsJsonObject();
                    tfroletitle.setText(permDataJsonObject.get("roleName").getAsString());
                    JsonArray permDataJsonArray = permDataJsonObject.get("permissions").getAsJsonArray();

                    for (JsonElement jsonElement :
                            permDataJsonArray) {
                        JsonObject jObject = jsonElement.getAsJsonObject();
                        JsonArray jsonArray = jObject.get("actions").getAsJsonArray();
                        int rowIndex = jObject.get("mapping_id").getAsInt();
                        int counter = 0;
                        for (int s = 0; s < jsonArray.size(); s++) {
                            if (!jsonArray.get(s).getAsString().isEmpty()) {
                                int colIndex = jsonArray.get(s).getAsInt();
                                Node node = getNodeByRowColumnIndex(rowIndex, colIndex, gpgrid);
                                if (node instanceof CheckBox) {
                                    CheckBox checkBox = (CheckBox) node;
                                    checkBox.setSelected(true);
                                    counter++;
//                            System.out.println(rowIndex+" Counter : "+counter+" ColumnCount : "+(gpgrid.getColumnCount()-1));
                                    if (counter == gpgrid.getColumnCount() - 1) {
                                        Node mNode = getNodeByRowColumnIndex(rowIndex, 0, gpgrid);
                                        CheckBox cb = (CheckBox) mNode;
                                        cb.setSelected(true);
//                                System.out.println(gpgrid.getColumnCount()+"-"+cb.getText());
                                    }
                                }
                            }

                        }

                    }
                }
            }
        });
        apiClient.setOnCancelled(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent workerStateEvent) {
                if (workerStateEvent.getSource().getValue() != null) {
                    userRoleCreateLogger.error("UserRoleUpdate : " + workerStateEvent.getSource().getValue().toString());
                } else {
                    userRoleCreateLogger.error("UserRoleUpdate : API response empty and null");
                }
            }
        });
        apiClient.setOnFailed(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent workerStateEvent) {
                if (workerStateEvent.getSource().getValue() != null) {
                    userRoleCreateLogger.error("UserRoleUpdate : " + workerStateEvent.getSource().getValue().toString());
                } else {
                    userRoleCreateLogger.error("UserRoleUpdate : API response empty and null");
                }
            }
        });
        apiClient.start();
    }


    private void setCheckedSelectedRow(Boolean flag, int rowIndex) {
        for (Node node : gpgrid.getChildren()) {
            if (node instanceof CheckBox) {
                if (GridPane.getRowIndex(node) == rowIndex) {
                    if (GridPane.getColumnIndex(node) != 0) {
                        CheckBox ckbox = (CheckBox) node;
                        ckbox.setSelected(flag);
                    }
                }
            }
        }
    }

    public void getColData() {

//        System.out.println("Edit : "+userRoleModel.getId()+" "+userRoleModel.getRoleName());
        /*HttpResponse<String> response = APIClient.getRequest("get_master_actions");
        return response.body();*/


        APIClient apiClient = new APIClient(EndPoints.GET_MASTER_ACTIONS, null, RequestType.GET);
        apiClient.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent workerStateEvent) {
                if (workerStateEvent.getSource().getValue() != null) {
                    colData = workerStateEvent.getSource().getValue().toString();
                    JsonObject jsonObject = new Gson().fromJson(colData, JsonObject.class);
                    permHeadArray = jsonObject.get("list").getAsJsonArray();
//                    System.out.println("permHeadArray=>"+permHeadArray.toString());
                    for (int cols = 0; cols < permHeadArray.size(); cols++) {
                        if (cols == 0) {
                            ColumnConstraints columnConstraints = new ColumnConstraints();
                            columnConstraints.setMinWidth(250);
                            gpgrid.getColumnConstraints().add(columnConstraints);
                        } else {
                            ColumnConstraints columnConstraints = new ColumnConstraints();
                            columnConstraints.setMinWidth(100);
                            gpgrid.getColumnConstraints().add(columnConstraints);
                        }

                        JsonObject object = permHeadArray.get(cols).getAsJsonObject();
                        Label label1 = new Label();
                        label1.setText(object.get("name").getAsString());
                        label1.setFont(font);
                        gpgrid.add(label1, cols + 1, 0);

                    }
                } else {
                    colData = "";
                }
            }
        });
        apiClient.setOnCancelled(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent workerStateEvent) {
                if (workerStateEvent.getSource().getValue() != null) {
                    userRoleCreateLogger.error("UserRoleList Error : " + workerStateEvent.getSource().getValue().toString());
                } else {
                    userRoleCreateLogger.error("UserRoleList Error : API response null");
                }
            }
        });
        apiClient.setOnFailed(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent workerStateEvent) {
                if (workerStateEvent.getSource().getValue() != null) {
                    userRoleCreateLogger.error("UserRoleList Error : " + workerStateEvent.getSource().getValue().toString());
                } else {
                    userRoleCreateLogger.error("UserRoleList Error : API response null");
                }
            }
        });

        apiClient.start();

    }

    public void getPermData() {
        /*HttpResponse<String> response = APIClient.getRequest("get_systems_all_permissions");
        return response.body();*/

        APIClient apiClient = new APIClient(EndPoints.GET_SYSTEM_ALL_PERMISSIONS_ENDPOINT, null, RequestType.GET);
        apiClient.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent workerStateEvent) {
                if (workerStateEvent.getSource().getValue() != null) {
                    permData = workerStateEvent.getSource().getValue().toString();
                    permDataArray = new Gson().fromJson(permData, JsonObject.class).get("level").getAsJsonArray();
                    for (int row = 0; row < permDataArray.size(); row++) {
                        JsonObject jsonObject1 = permDataArray.get(row).getAsJsonObject();
                        JsonObject levelObject = jsonObject1.get("level").getAsJsonObject();
                        JsonArray cbs = levelObject.get("actions").getAsJsonArray();
                        int rowIndex = row + 1;
                        CheckBox checkBox = new CheckBox();
                        checkBox.setText(levelObject.get("name").getAsString());

                        checkBox.selectedProperty().addListener((ObservableValue<? extends Boolean> ov, Boolean oldValue, Boolean newValue) -> {
//                System.out.println("Old : " + oldValue + ", New : " + newValue);

                            setCheckedSelectedRow(newValue, rowIndex);
                        });


                        gpgrid.add(checkBox, 0, rowIndex);
                        for (int s = 0; s < cbs.size(); s++) {
                            int cb = cbs.get(s).getAsInt();
                            int rowindex = row + 1;
                            CheckBox checkBox1 = new CheckBox();
                            checkBox1.selectedProperty().addListener((obs,oldValue,newValue)->{
                                if(newValue){
                                    permissionCount++;
                                }else{
                                    permissionCount--;
                                }
                                System.out.println("Permission Count : "+permissionCount);
                            });
                            gpgrid.add(checkBox1, cb, rowindex);

                        }

                    }

                    if (userRoleModel != null){
                        getPermDataById();
                    }


                } else {
                    permData = "";
                }
            }
        });
        apiClient.setOnCancelled(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent workerStateEvent) {
                if (workerStateEvent.getSource().getValue() != null) {
                    userRoleCreateLogger.error("UserRoleList Error : " + workerStateEvent.getSource().getValue().toString());
                } else {
                    userRoleCreateLogger.error("UserRoleList Error : API response null");
                }
            }
        });

        apiClient.start();
    }

    public void createUserRole() {
        System.out.println("Index : " + (index++));
        String roleName = tfroletitle.getText();
        String response = "";
        masterFunction = new JsonArray();
        for (Node node : gpgrid.getChildren()) {
            if (node instanceof CheckBox) {
                int rowIndex = GridPane.getRowIndex(node);
                int colIndex = GridPane.getColumnIndex(node);
                CheckBox checkBox = (CheckBox) node;

                if (colIndex > 0 && checkBox.isSelected()) {

                    boolean isRecordFound = false;
                    for (int s = 0; s < masterFunction.size(); s++) {
                        JsonObject jsonObject = masterFunction.get(s).getAsJsonObject();
                        if (jsonObject.get("mapping_id").getAsInt() == rowIndex) {
                            masterFunction.get(s).getAsJsonObject().get("actions").getAsJsonArray().add((colIndex) + "");
                            isRecordFound = true;
                        }
                    }
                    if (!isRecordFound) {
                        //Add in masterFunctions
                        JsonObject jsonObject = new JsonObject();
                        JsonArray actions = new JsonArray();

                        //JsonObject mappingData=getMappingId(rowIndex-1,colIndex-1);
                        JsonObject mappingData = permDataArray.get(rowIndex - 1).getAsJsonObject().get("level").getAsJsonObject();
                        jsonObject.addProperty("mapping_id", mappingData.get("id").getAsInt());
                        jsonObject.addProperty("mapping_name", mappingData.get("name").getAsString());
                        actions.add((colIndex) + "");
                        jsonObject.add("actions", actions);
                        masterFunction.add(jsonObject);

                    }
                }
            }
        }
        String mstFunctions = new Gson().toJson(masterFunction);
//        System.out.println("Final Json => " + mstFunctions);

        Map<String, String> params = new HashMap<>();
        params.put("roleName", roleName);
        params.put("roles_permissions", mstFunctions);
        String req = Globals.mapToStringforFormData(params);


        APIClient apiClient = new APIClient(EndPoints.createUserRole, req, RequestType.FORM_DATA);
        apiClient.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent workerStateEvent) {
                AlertUtility.CustomCallback callback = number -> {
                    if (number == 1)
                        GlobalSadminController.getInstance().addTabStatic(ROLE_LIST_SLUG, false);
                };

                if (workerStateEvent.getSource().getValue() != null) {
                    JsonObject jsonObject = new Gson().fromJson(workerStateEvent.getSource().getValue().toString(), JsonObject.class);
                    System.out.println(jsonObject.toString());
                     UserRoleListController.focusStatus = "create";
                    AlertUtility.AlertSuccessTimeout("Success", jsonObject.get("message").getAsString(), callback);

                    userRoleModel = null;

                } else {
                    AlertUtility.AlertErrorTimeout("Success", "Failed to create role.", callback);
                }

            }
        });
        apiClient.setOnFailed(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent workerStateEvent) {

                AlertUtility.CustomCallback callback = number -> {

                };
                AlertUtility.AlertErrorTimeout("Failed", "Failed to create User Role, Try Later!", callback);
            }
        });
        apiClient.setOnCancelled(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent workerStateEvent) {
                AlertUtility.CustomCallback callback = number -> {
                    if (number == 1) {

                    } else {

                    }
                };
                AlertUtility.AlertError("Failed", "Failed to create User Role, Try Later!", callback);
            }
        });
        apiClient.start();

    }


}
