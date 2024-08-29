package com.opethic.genivis.controller.users;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import com.opethic.genivis.GenivisApplication;
import com.opethic.genivis.controller.master.CompanyUserCreateController;
import com.opethic.genivis.models.UserRoleModel;
import com.opethic.genivis.network.APIClient;
import com.opethic.genivis.network.EndPoints;
import com.opethic.genivis.network.RequestType;
import com.opethic.genivis.utils.AlertUtility;
import com.opethic.genivis.utils.GlobalSadminController;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.*;
import javafx.scene.layout.BorderPane;
import javafx.stage.Screen;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.URL;
import java.util.ResourceBundle;

import static com.opethic.genivis.utils.FxmFileConstants.ROLE_CREATE_SLUG;
import static com.opethic.genivis.utils.FxmFileConstants.ROLE_UPDATE_SLUG;
import static com.opethic.genivis.utils.Globals.userRoleModel;

public class UserRoleListController implements Initializable {
    @FXML
    public BorderPane bpRoot;
    String roleListData = "";
    @FXML
    TextField tfUserRoleSearch;
    @FXML
    Button btnCreate;
    private ObservableList<UserRoleModel> tableData;
    KeyCombination kcNewRole;
    Mnemonic mnemonic;

    Integer ListSize;

    private static final Logger userRoleLogger = LogManager.getLogger(UserRoleListController.class);
    @FXML
    private TableView<UserRoleModel> tblUserRoleList;
    public static String focusStatus;
    public static Integer SelectedRowIndex;


    @Override
    public void initialize(URL location, ResourceBundle resources) {

        ResponsiveWiseCssPicker();


        kcNewRole=new KeyCodeCombination(KeyCode.N,KeyCombination.ALT_DOWN);
        tableData = FXCollections.observableArrayList();
        userRoleModel = null;

        Platform.runLater(() -> {
            runLaterCode();
        });


        tfUserRoleSearch.textProperty().addListener(((observable, oldValue, newValue) -> {
            if (!newValue.isEmpty()) {
                search(tfUserRoleSearch.getText().toLowerCase());
            } else {
                tblUserRoleList.setItems(tableData);
                tblUserRoleList.refresh();
            }
        }));

        TableColumn<UserRoleModel, String> srNoColumn =
                new TableColumn<>("Sr. No.");

        srNoColumn.setCellValueFactory(
                new PropertyValueFactory<>("id"));
        srNoColumn.setPrefWidth(100);

        TableColumn<UserRoleModel, String> roleNamecolumn =
                new TableColumn<>("Role Name");

        roleNamecolumn.setCellValueFactory(
                new PropertyValueFactory<>("roleName"));
        roleNamecolumn.setPrefWidth(300);


        tfUserRoleSearch.focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if (newValue) {
                    tblUserRoleList.getSelectionModel().clearSelection();
                }
            }
        });

        tblUserRoleList.getColumns().add(srNoColumn);
        tblUserRoleList.getColumns().add(roleNamecolumn);

        //tableView.setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY);



        tblUserRoleList.setRowFactory(tv -> {
            TableRow<UserRoleModel> row = new TableRow<>();

            row.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    if (mouseEvent.getClickCount() == 2) {
                        userRoleModel = row.getItem();
                        Integer selectedIndex = tblUserRoleList.getSelectionModel().getFocusedIndex();
                        System.out.println("List On Double click " + selectedIndex);
                        UserRoleCreateController.SelectedRowIndex =  selectedIndex;
                        try {
                            GlobalSadminController.getInstance().addTabStatic(ROLE_UPDATE_SLUG, false);
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }

                    }
                }
            });
            return row;
        });

        tblUserRoleList.setItems(tableData);
        /*btnCreate.setOnAction( (event) ->{
            System.out.println("Btn Clicked");
            GlobalSadminController.getInstance().addTabStatic(ROLE_CREATE_SLUG,false);
        });*/
        btnCreate.setOnKeyPressed(event->{
            if(event.getCode() == KeyCode.SPACE){
                AlertUtility.AlertConfirmation(AlertUtility.alertTypeConfirmation, "Are you sure want go User Role Create ?", input -> {
                    if (input == 1) {
                        createRole();
                    }
                });
            }
            else if(event.getCode() == KeyCode.ENTER){
                tblUserRoleList.requestFocus();
            }
        });
        btnCreate.setOnMouseClicked(event->{
            AlertUtility.AlertConfirmation(AlertUtility.alertTypeConfirmation, "Are you sure want go User Role Create ?", input -> {
                if (input == 1) {
                    createRole();
                }
            });
        });


        btnCreate.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                createRole();
            }
        });
        fetchData();

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

    private void createRole() {
        GlobalSadminController.getInstance().addTabStatic(ROLE_CREATE_SLUG,false);
    }

    private void search(String keyword) {
        ObservableList<UserRoleModel> filteredData = FXCollections.observableArrayList();
        for (UserRoleModel roleModel : tableData) {
            if (roleModel.getRoleName().toLowerCase().contains(keyword))
                filteredData.add(roleModel);
        }

        tblUserRoleList.setItems(filteredData);
    }

    private void runLaterCode() {
        tfUserRoleSearch.requestFocus();
        bpRoot.getScene().addEventFilter(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {

                if(event.getCode()==KeyCode.ENTER){
                    if(tblUserRoleList.isFocused()){
                        userRoleModel = tblUserRoleList.getSelectionModel().getSelectedItem();
                        try {
                             Integer selectedIndex = tblUserRoleList.getSelectionModel().getFocusedIndex();
                            System.out.println("list on Enter selected index " + selectedIndex);
                            UserRoleCreateController.SelectedRowIndex=selectedIndex;
                            GlobalSadminController.getInstance().addTabStatic(ROLE_UPDATE_SLUG, false);
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                    }else if(tfUserRoleSearch.isFocused()){
                        btnCreate.requestFocus();
                    }else if(btnCreate.isFocused()){
                        btnCreate.fire();
                    }
                }



                if(event.getCode()==KeyCode.N && event.isControlDown()){
                    createRole();
                }

                if (event.getCode()==KeyCode.DOWN && !tblUserRoleList.isFocused()) {
                    tblUserRoleList.getSelectionModel().select(0);
                    tblUserRoleList.requestFocus();
                }


                if((event.getCode().isModifierKey() || event.getCode().isLetterKey() || event.getCode()==KeyCode.BACK_SPACE) && tblUserRoleList.isFocused()){
                    tfUserRoleSearch.requestFocus();
                }

                if(event.getCode()==KeyCode.UP && tblUserRoleList.getSelectionModel().getSelectedIndex()==0){
                    tblUserRoleList.getSelectionModel().clearSelection();
                    tfUserRoleSearch.requestFocus();
                }
            }
        });
    }


    private void fetchData() {
        APIClient apiClient = new APIClient(EndPoints.GET_ROLE_PERMISSION_LIST_ENDPOINT, null, RequestType.GET);
        apiClient.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent workerStateEvent) {
                String response = workerStateEvent.getSource().getValue().toString();
                if (response != null && !response.isEmpty()) {
                    roleListData = response;

                    JsonObject jsonObject = new Gson().fromJson(roleListData, JsonObject.class);
                    JsonArray jsonArray = jsonObject.get("responseObject").getAsJsonArray();
                    for (int i = 0; i < jsonArray.size(); i++) {
                        JsonObject roleModel = jsonArray.get(i).getAsJsonObject();
                        tableData.add(new UserRoleModel(roleModel.get("id").getAsLong(), roleModel.get("roleName").getAsString()));
                    }
                    ListSize = tableData.size();
                    if(focusStatus.equalsIgnoreCase("create")){
                        System.out.println("listSize "+ ListSize + " last index " + (ListSize-1));
                        tblUserRoleList.getSelectionModel().clearAndSelect(ListSize-1);
                        tblUserRoleList.requestFocus();
                        tblUserRoleList.scrollTo(ListSize-1);
                    }
                    else if(focusStatus.equalsIgnoreCase("edit")){
                        System.out.println("Selected Row index in edit " + SelectedRowIndex);
                        tblUserRoleList.getSelectionModel().clearAndSelect(SelectedRowIndex);
                        tblUserRoleList.requestFocus();
                        tblUserRoleList.scrollTo(SelectedRowIndex);
                    }
                    else if(focusStatus.equalsIgnoreCase("cancle") && SelectedRowIndex!=null){
                        System.out.println("Selected Row index in Cancle " + SelectedRowIndex);
                        tblUserRoleList.getSelectionModel().clearAndSelect(SelectedRowIndex);
                        tblUserRoleList.requestFocus();
                        tblUserRoleList.scrollTo(SelectedRowIndex);
                    }
                    else{
                        System.out.println("on Cancle");
                        tfUserRoleSearch.requestFocus();
                    }
                    tblUserRoleList.refresh();

                }
            }
        });

        apiClient.setOnFailed(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent workerStateEvent) {
                if (workerStateEvent.getSource().getValue() != null) {
                    userRoleLogger.error("UserRoleList Error : " + workerStateEvent.getSource().getValue().toString());
                } else {
                    userRoleLogger.error("UserRoleList Error : API response null");
                }
            }
        });

        apiClient.setOnFailed(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent workerStateEvent) {
                if (workerStateEvent.getSource().getValue() != null) {
                    userRoleLogger.error("UserRoleList Error : " + workerStateEvent.getSource().getValue().toString());
                } else {
                    userRoleLogger.error("UserRoleList Error : API response null");
                }
            }
        });
        apiClient.start();
        /*HttpResponse<String> response = APIClient.getRequest("get_role_permission_list");
        if(response!=null){
            roleListData = response.body();
        }*/
    }
}
