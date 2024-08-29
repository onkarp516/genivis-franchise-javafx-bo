package com.opethic.genivis.controller.master;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.opethic.genivis.GenivisApplication;
import com.opethic.genivis.controller.commons.SwitchButton;
import com.opethic.genivis.dto.Reports.PayableReportDTO;
import com.opethic.genivis.models.CompanyUserModel;
import com.opethic.genivis.network.APIClient;
import com.opethic.genivis.network.EndPoints;
import com.opethic.genivis.network.RequestType;
import com.opethic.genivis.utils.AlertUtility;
import com.opethic.genivis.utils.FxmFileConstants;
import com.opethic.genivis.utils.GlobalController;
import com.opethic.genivis.utils.Globals;
import javafx.application.Platform;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.property.BooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import static com.opethic.genivis.utils.Globals.companyUserModelDTO;

public class CompanyUserController implements Initializable {
    @FXML
    private BorderPane spRootPane;
    private String companyUserList = "",selectedFilter="";
    @FXML
    private TableColumn<CompanyUserModel, String> tblcCmpUserSrNo, tblcCmpUserCompanyName, tblcCmpUserFullName, tblcCmpUserMobNo, tblcCmpUserEmail, tblcCmpUserGender,
            tblcCmpUserUsercode,tblcCmpUserAction;
    @FXML
    private TableView<CompanyUserModel> tblvCmpUserList;
    @FXML
    private Button createButton;
    @FXML
    private TextField searchComUser;
    private ObservableList<CompanyUserModel> orgData;
    private static final Logger comUserLogger = LoggerFactory.getLogger(CompanyUserController.class);    //logger for error handling
    private ObservableList<CompanyUserModel> originalData;
    @FXML
    private ComboBox<String> cmbUserFilter;
    private static final KeyCombination com_user_new_CTRL_N =  new KeyCodeCombination(KeyCode.N, KeyCombination.CONTROL_DOWN);
//    public static String focusId,focusStatus="";
    //? Highlight the Record Start
    public static boolean isNewCompanyUserCreated = false; // Flag for new creation
    public static String editedCompanyUserId = null; // ID for edited record


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        ResponsiveWiseCssPicker();

        Platform.runLater(() -> {
            fetchData();

//            searchComUser.requestFocus();
//            Integer currentNoOfRows =originalData!=null ? originalData.size():0;
//            System.out.println("currentNoOfRows "+currentNoOfRows+"  focsId from edit page "+focusId);
//            if(focusStatus.equalsIgnoreCase("create")){   //for create
//                System.out.println("focusStatussss "+focusStatus);
//                tblvCmpUserList.requestFocus();
//                tblvCmpUserList.getSelectionModel().select(currentNoOfRows-1);
//                tblvCmpUserList.getFocusModel().focus(currentNoOfRows-1);
//                tblvCmpUserList.scrollTo(currentNoOfRows-1);
////                tblvCmpUserList.getSelectionModel().focus(currentNoOfRows-1);
//
//            }
//            else if(focusId !=null){                             //for edit
//                tblvCmpUserList.requestFocus();
//                tblvCmpUserList.getSelectionModel().select(Integer.parseInt(focusId));
//                tblvCmpUserList.getFocusModel().focus(Integer.parseInt(focusId));
////                tblvCmpUserList.getSelectionModel().focus(Integer.parseInt(focusId));
//                tblvCmpUserList.scrollTo(Integer.parseInt(focusId));
//            }else {
//                System.out.println("in else conditionnn");
//                searchComUser.requestFocus();
//            }
        });

        tblvCmpUserList.setEditable(true);
        responsiveTabele();
        orgData = tblvCmpUserList.getItems();
        searchComUser.textProperty().addListener((observableValue, oldValue, newValue) -> {
            handleSearch(newValue.trim());
        });

//       tblvCmpUserList.getSelectionModel().getFocusedIndex();

        searchComUser.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                cmbUserFilter.requestFocus();
            }else if(event.getCode() == KeyCode.DOWN){
                tblvCmpUserList.requestFocus();
                tblvCmpUserList.getSelectionModel().select(0);
                tblvCmpUserList.getFocusModel().focus(0);
            }
        });
        cmbUserFilter.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                createButton.requestFocus();
            }if(event.getCode() == KeyCode.SPACE){
                cmbUserFilter.show();
            }
        });
        cmbUserFilter.getItems().addAll("All","Enabled","Disabled");
        cmbUserFilter.setValue(cmbUserFilter.getItems().get(0));
        cmbUserFilter.setOnAction(this::handleFilterComboBox);

        createButton.sceneProperty().addListener((obsValue, oldValue, newValue)->{
            if(newValue!=null){
                newValue.getAccelerators().put(com_user_new_CTRL_N,createButton::fire);
            }
        });
        tblvCmpUserList.setRowFactory(tv -> {
            TableRow<CompanyUserModel> row = new TableRow<>();
            row.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    if (mouseEvent.getClickCount() == 2) {
                        CompanyUserModel companyUserModel = row.getItem();   //selected row info will store here
//                        System.out.println("companyUserModel " + companyUserModel.getId());
                        Integer sizeList=0 ;
//                        tblvCmpUserList.getSelectionModel().select(sizeList-1);
//                        CompanyUserCreateController.selectedFocusId = String.valueOf(tblvCmpUserList.getSelectionModel().getFocusedIndex());
//                        focusId = String.valueOf(tblvCmpUserList.getSelectionModel().getFocusedIndex());
//                        System.out.println("selectedFocus id in list "+focusId);
                        editCompanyUser();
                    }
                }
            });
            return row;
        });
        tblvCmpUserList.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER){
                TableRow<CompanyUserModel> row = new TableRow<>();
                CompanyUserModel companyUserModel = row.getItem();
//                CompanyUserCreateController.selectedFocusId = String.valueOf(tblvCmpUserList.getSelectionModel().getFocusedIndex());
                editCompanyUser();
            }
        });

        shortcutKey();

        tblvCmpUserList.setEditable(true);

        tblvCmpUserList.setOnKeyPressed(event->{
            if(event.getCode() == KeyCode.TAB) {
                int focusedIndex = tblvCmpUserList.getSelectionModel().getFocusedIndex();
                tblvCmpUserList.edit(focusedIndex, tblvCmpUserList.getColumns().get(7));
            }
        });

    }

    private void ResponsiveWiseCssPicker() {
        double height = Screen.getPrimary().getBounds().getHeight();
        double width = Screen.getPrimary().getBounds().getWidth();
        System.out.println("width" + width);
        if (width >= 992 && width <= 1024) {
            spRootPane.getStylesheets().add(GenivisApplication.class.getResource("ui/css/TranxCommonCssStyles/tranxCommonCssStyle1.css").toExternalForm());
        } else if (width >= 1025 && width <= 1280) {
            spRootPane.getStylesheets().add(GenivisApplication.class.getResource("ui/css/TranxCommonCssStyles/tranxCommonCssStyle2.css").toExternalForm());
        } else if (width >= 1281 && width <= 1368) {
            spRootPane.getStylesheets().add(GenivisApplication.class.getResource("ui/css/TranxCommonCssStyles/tranxCommonCssStyle3.css").toExternalForm());
        } else if (width >= 1369 && width <= 1400) {
            spRootPane.getStylesheets().add(GenivisApplication.class.getResource("ui/css/TranxCommonCssStyles/tranxCommonCssStyle4.css").toExternalForm());
        } else if (width >= 1401 && width <= 1440) {
            spRootPane.getStylesheets().add(GenivisApplication.class.getResource("ui/css/TranxCommonCssStyles/tranxCommonCssStyle5.css").toExternalForm());
        } else if (width >= 1441 && width <= 1680) {
            spRootPane.getStylesheets().add(GenivisApplication.class.getResource("ui/css/TranxCommonCssStyles/tranxCommonCssStyle6.css").toExternalForm());
        } else if (width >= 1681 && width <= 1920) {
            spRootPane.getStylesheets().add(GenivisApplication.class.getResource("ui/css/TranxCommonCssStyles/tranxCommonCssStyle7.css").toExternalForm());
        }
    }

    private void shortcutKey(){
        spRootPane.addEventFilter(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                if(event.getCode() == KeyCode.E && event.isControlDown()){
//                    CompanyUserCreateController.selectedFocusId = String.valueOf(tblvCmpUserList.getSelectionModel().getFocusedIndex());
                    editCompanyUser();
                }
            }
        });
    }

    //function for responsive of list table
    private void responsiveTabele() {
        tblcCmpUserSrNo.prefWidthProperty().bind(tblvCmpUserList.widthProperty().multiply(0.06));
        tblcCmpUserCompanyName.prefWidthProperty().bind(tblvCmpUserList.widthProperty().multiply(0.2));
        tblcCmpUserFullName.prefWidthProperty().bind(tblvCmpUserList.widthProperty().multiply(0.2));
        tblcCmpUserMobNo.prefWidthProperty().bind(tblvCmpUserList.widthProperty().multiply(0.12));
        tblcCmpUserEmail.prefWidthProperty().bind(tblvCmpUserList.widthProperty().multiply(0.18));
        tblcCmpUserGender.prefWidthProperty().bind(tblvCmpUserList.widthProperty().multiply(0.07));
        tblcCmpUserUsercode.prefWidthProperty().bind(tblvCmpUserList.widthProperty().multiply(0.12));
        tblcCmpUserAction.prefWidthProperty().bind(tblvCmpUserList.widthProperty().multiply(0.05));
    }

    //function to get the company user list
    private void fetchData() {
        try {
            comUserLogger.info("start of cmpany user list");
            tblvCmpUserList.getItems().clear();
            HttpResponse<String> response = APIClient.getRequest(EndPoints.GET_COMPANY_USER_LIST);
            companyUserList = response.body();
//            System.out.println("companyUserList Body =>" + response.body());
            JsonObject jsonObject = new Gson().fromJson(companyUserList, JsonObject.class);
            JsonArray length = jsonObject.get("responseObject").getAsJsonArray();
            ObservableList<CompanyUserModel> observList = FXCollections.observableArrayList();
            int serialNo = 0;
            orgData = observList;
            if (jsonObject.get("responseStatus").getAsInt() == 200) {
                JsonArray jsonArray = jsonObject.get("responseObject").getAsJsonArray();

                if (jsonArray.size() > 0) {
                    for (JsonElement element : jsonArray) {
                        JsonObject item = element.getAsJsonObject();
//                        System.out.println("itemm " + item);
                        serialNo = 1 + serialNo;
                        String comName = item.get("companyName").getAsString();
                        String fullName = item.get("fullName").getAsString();
                        String moNo = item.get("mobileNumber").getAsString();
                        String mail = item.get("email").getAsString();
                        String gender = item.get("gender").getAsString();
                        String usercode = item.get("usercode").getAsString();
                        String id = item.get("id").getAsString();
                        String  isSwitch = item.get("isSwitch").getAsString();
                        String status = "false";
                        if(isSwitch.equalsIgnoreCase("1")){
                            status ="true";
                        }

                        observList.add(new CompanyUserModel(serialNo,id, comName, fullName,
                                moNo, mail, gender, usercode,status));
                    }
                    tblcCmpUserSrNo.setCellValueFactory(new PropertyValueFactory<>("srNo"));
                    tblcCmpUserCompanyName.setCellValueFactory(new PropertyValueFactory<>("companyName"));
                    tblcCmpUserFullName.setCellValueFactory(new PropertyValueFactory<>("fullName"));
                    tblcCmpUserMobNo.setCellValueFactory(new PropertyValueFactory<>("mobileNumber"));
                    tblcCmpUserEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
                    tblcCmpUserGender.setCellValueFactory(new PropertyValueFactory<>("gender"));
                    tblcCmpUserUsercode.setCellValueFactory(new PropertyValueFactory<>("usercode"));
                    tblcCmpUserAction.setCellValueFactory(cellData -> cellData.getValue().isSwitchProperty());
                    tblcCmpUserAction.setCellFactory(celldata -> new SwitchToogleTable("isSwitch"));
                    tblvCmpUserList.setItems(observList);
                    originalData = observList;

                    //******************************** Highlight on the Created/Edited Record in the List Start ********************************
                    if (CompanyUserController.isNewCompanyUserCreated) {
                        tblvCmpUserList.getSelectionModel().selectLast();
                        tblvCmpUserList.scrollTo(tblvCmpUserList.getItems().size() - 1);
                        CompanyUserController.isNewCompanyUserCreated = false; // Reset the flag
                    } else if (CompanyUserController.editedCompanyUserId != null) {
                        for (CompanyUserModel companyuser : observList) {
                            if (companyuser.getId().equals(CompanyUserController.editedCompanyUserId)) {
                                tblvCmpUserList.getSelectionModel().select(companyuser);
                                tblvCmpUserList.scrollTo(companyuser);
                                CompanyUserController.editedCompanyUserId = null; // Reset the ID
                                break;
                            }
                        }
                    }
                    //******************************** Highlight on the Created/Edited Record in the List End ********************************

                } else System.out.println("reposneObject is blank");
            }

        } catch (Exception e) {
            comUserLogger.error("Exception in comUser " + e.getMessage());
        }

    }

    public void createUserPage() {
        try {
            companyUserModelDTO = null;
            GlobalController.getInstance().addTabStatic(FxmFileConstants.COM_USER_CREATE_SLUG, false);
        } catch (Exception e) {
            comUserLogger.error("Exception " + e.getMessage());
            throw new RuntimeException();
        }
    }

    public void editCompanyUser() {        //function for edit the company user
        try {
            companyUserModelDTO = tblvCmpUserList.getSelectionModel().getSelectedItem();
            String selectedId = companyUserModelDTO.getId();
            //? Highlight
            CompanyUserController.editedCompanyUserId = companyUserModelDTO.getId(); //? Set the ID for editing
            GlobalController.getInstance().addTabStatic1(FxmFileConstants.COM_USER_EDIT_SLUG, false, Integer.valueOf(selectedId));
        } catch (Exception e) {
            comUserLogger.error("Exception " + e.getMessage());
        }
    }

    public void handleSearch(String search) {
        ObservableList<CompanyUserModel> filterData = FXCollections.observableArrayList();
        if (search.isEmpty()) {
            tblvCmpUserList.setItems(orgData);
            return;
        }
        for (CompanyUserModel item : orgData) {
            if (matchesKeyword(item, search)) {
                filterData.add(item);
            }
        }
        tblvCmpUserList.setItems(filterData);
    }

    public boolean matchesKeyword(CompanyUserModel dtoItems, String search) {
        String lowerCase = search.toLowerCase();

        return dtoItems.getCompanyName().toLowerCase().contains(lowerCase) ||
                dtoItems.getFullName().toLowerCase().contains(lowerCase) ||
                dtoItems.getMobileNumber().toLowerCase().contains(lowerCase) ||
                dtoItems.getEmail().toLowerCase().contains(lowerCase);
    }
    private void handleFilterComboBox(ActionEvent event){
        selectedFilter = cmbUserFilter.getSelectionModel().getSelectedItem();
        if(cmbUserFilter.getSelectionModel().getSelectedItem() == "Enabled"){
            selectedFilter = "Enabled";
        }else if(cmbUserFilter.getSelectionModel().getSelectedItem() == "Disabled"){
            selectedFilter = "Disabled";
        }else if(cmbUserFilter.getSelectionModel().getSelectedItem() == "All"){
            selectedFilter="All";
        }
        System.out.println("selectedFilter "+selectedFilter);
        filterCompanyUser();
    }
    private void filterCompanyUser() {
        ObservableList<CompanyUserModel> filteredData = FXCollections.observableArrayList();
        if (selectedFilter == "Enabled") {

            filteredData = originalData.stream().filter((item -> item.getIsSwitch().equalsIgnoreCase("true"))).collect(Collectors.toCollection(FXCollections::observableArrayList));
            System.out.println("enabled filteredData "+filteredData);
        }if (selectedFilter == "Disabled") {
            filteredData = originalData.stream().filter((item -> item.getIsSwitch().equalsIgnoreCase("false"))).collect(Collectors.toCollection(FXCollections::observableArrayList));
            System.out.println("disabled filteredData "+filteredData);
        }
        else if(selectedFilter == "All") {
            filteredData = originalData.stream().collect(Collectors.toCollection(FXCollections::observableArrayList));
            System.out.println("else filteredData "+filteredData);
            System.out.println("else filteredData "+filteredData);
        }
        tblvCmpUserList.setItems(filteredData);
    }
}
//class for switch button
class SwitchToogleTable extends TableCell<CompanyUserModel, String> {
    private SwitchButton switchButton;

    VBox vBoxSwitch;
    private String columnName;
    Boolean aBoolean=true;
    private static final org.apache.logging.log4j.Logger companyUserlogger = LogManager.getLogger(SwitchToogleTableCell.class);

    public  SwitchToogleTable(String columnName) {
        this.columnName = columnName;
        this.switchButton = new SwitchButton();

        this.switchButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                switchButton.switchOnProperty().set(!switchButton.switchOnProperty().get());
                CompanyUserModel companyAdminDTO = (CompanyUserModel) getTableView().getItems().get(getIndex());
                String selctedId = companyAdminDTO.getId();
                String isEnable = String.valueOf(switchButton.switchOnProperty().get());
                commitEdit(String.valueOf(switchButton.switchOnProperty().get()));
                System.out.println("selctedIddd "+selctedId+"  isEnable "+isEnable);
                userEnableDisable(selctedId, isEnable);
            }
        });
//        switchButton.addEventFilter(KeyEvent.KEY_PRESSED, keyEvent -> {    //important
//            if (keyEvent.getCode() == KeyCode.TAB) {
//                keyEvent.consume(); // Prevent default tab action
//
//            }else if(keyEvent.isShiftDown() && keyEvent.getCode() == KeyCode.TAB){
//                keyEvent.consume();
//                switchButton.requestFocus();
//            }
//        });

//        this.switchButton.setOnKeyPressed(event ->  {
////            @Override
////            public void handle(KeyEvent keyEvent) {
//            if(event.getCode() == KeyCode.SPACE){
//                switchButton.switchOnProperty().set(!switchButton.switchOnProperty().get());
//                CompanyUserModel companyAdminDTO = (CompanyUserModel) getTableView().getItems().get(getIndex());
//                String selctedId = companyAdminDTO.getId();
//                String isEnable = String.valueOf(switchButton.switchOnProperty().get());
//                commitEdit(String.valueOf(switchButton.switchOnProperty().get()));
//                System.out.println("selctedIddd on space.0"+selctedId+"  isEnable "+isEnable);
//                userEnableDisable(selctedId, isEnable);
//            }
//
////            }
//        });

        this.vBoxSwitch = new VBox();
        vBoxSwitch.setPrefHeight(23.0);
        vBoxSwitch.setMaxHeight(23.0);
        vBoxSwitch.setMinHeight(23.0);
        vBoxSwitch.setPrefWidth(35.5);
        vBoxSwitch.setMinWidth(35.5);
        vBoxSwitch.setMaxWidth(35.5);
        vBoxSwitch.setStyle("-fx-background-color: #f8f4fc; -fx-background-radius: 4; -fx-border-radius: 4; -fx-border-color: #f8f4fc; -fx-border-width: 2;");

        switchButton.setMinWidth(32.0);
        switchButton.setMaxWidth(32.0);
        switchButton.setPrefWidth(32.0);
        vBoxSwitch.getChildren().add(switchButton);
        switchButton.setParentBox(vBoxSwitch);
    }
    public void userEnableDisable(String selectedItem, String isEnable) {
        Map<String, String> map = new HashMap<>();
        map.put("isEnable", isEnable);
        map.put("id", selectedItem);
        String formData = Globals.mapToStringforFormData(map);

        APIClient apiClient=new APIClient(EndPoints.DISABLE_USER,formData,RequestType.FORM_DATA);
        apiClient.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent event) {
                String message;

                if(event.getSource().getValue()!=null){
                    JsonObject responseBody = new Gson().fromJson(event.getSource().getValue().toString(), JsonObject.class);
                    if (responseBody.get("responseStatus").getAsInt() == 200) {
                        message=responseBody.get("message").getAsString();
                        AlertUtility.AlertSuccess(AlertUtility.alertTypeSuccess, message, input -> {
                        });
                    }else{
                        message=responseBody.get("message").getAsString();
                        AlertUtility.AlertError(AlertUtility.alertTypeError, message, in -> {
                        });
                    }
                }else{
                    message="Please try later!";
                    AlertUtility.AlertError(AlertUtility.alertTypeError, message, in -> {
                    });
                }
            }
        });
        apiClient.setOnFailed(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent event) {
                companyUserlogger.debug("Erorr in user enable disable onFailed : "+event.getSource().getValue());
            }
        });
        apiClient.setOnCancelled(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent event) {
                companyUserlogger.debug("Erorr in user enable disable onCanceled : "+event.getSource().getValue());
            }
        });
        apiClient.start();

    }
    @Override
    public void startEdit() {
        super.startEdit();
        setText(null);
        HBox vbox = new HBox(vBoxSwitch);
        vbox.setAlignment(Pos.CENTER);
        setGraphic(vbox);
        switchButton.requestButtonFocus();
    }
    @Override
    public void cancelEdit() {
        super.cancelEdit();
        HBox vbox = new HBox(vBoxSwitch);
        vbox.setAlignment(Pos.CENTER);
        setGraphic(vbox);
    }
    @Override
    protected void updateItem(String item, boolean empty) {
        super.updateItem(item, empty);
        if (empty) {
            setGraphic(null);
        } else {

            switchButton.switchOnProperty().set(Boolean.parseBoolean(item));
            HBox vbox = new HBox(vBoxSwitch);
            vbox.setAlignment(Pos.CENTER);
            setGraphic(vbox);
        }
    }
    @Override
    public void commitEdit(String newValue) {
        super.commitEdit(newValue);
        ((CompanyUserModel) getTableRow().getItem()).setIsSwitch(newValue);
    }


}
