package com.opethic.genivis.controller.master;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.opethic.genivis.GenivisApplication;
import com.opethic.genivis.dto.AreaHeadListDTO;
//import com.opethic.genivis.dto.FranchiseListDTO;
import com.opethic.genivis.network.APIClient;
import com.opethic.genivis.network.EndPoints;
import com.opethic.genivis.utils.AlertUtility;
import com.opethic.genivis.utils.CommonValidationsUtils;
import com.opethic.genivis.utils.GlobalController;
import com.opethic.genivis.utils.Globals;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.net.URL;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import static com.opethic.genivis.utils.FxmFileConstants.*;
import static com.opethic.genivis.utils.Globals.areaHeadListDTO;
import static com.opethic.genivis.utils.Globals.franchiseListDTO;

public class AreaHeadListController implements Initializable {

    @FXML
    private TableView<AreaHeadListDTO> tvAreaHeadList;
    @FXML
    private TableColumn<AreaHeadListDTO, String> tcAreaHeadListId;
    @FXML
    private TableColumn<AreaHeadListDTO, String> tcAreaHeadListFullName;
    @FXML
    private TableColumn<AreaHeadListDTO, String> tcfAreaHeadListAddress;
    @FXML
    private TableColumn<AreaHeadListDTO, String> tcAreaHeadListMobile;
    @FXML
    private TableColumn<AreaHeadListDTO, String> tcAreaHeadListRole;
    @FXML
    private TableColumn<AreaHeadListDTO, String> tcAreaHeadListState;

    @FXML
    private BorderPane spRootPane;


    @FXML
    private TableColumn<AreaHeadListDTO, String> tcAreaHeadListZone;
    @FXML
    private TableColumn<AreaHeadListDTO, String> tcAreaHeadListRegion;
    @FXML
    private TableColumn<AreaHeadListDTO, String> tcAreaHeadListDistrict;
    @FXML
    private TextField tffieldSearch;
    @FXML
    private Button btnFranchiseListCreate;

    private String responseBody, message;
    public static boolean isNewAreaHeadCreated = false; // Flag for new creation
    public static String editedAreaHeadId = null; // ID for edited franchise
    private Node[] focusableNodes;

    private ObservableList<AreaHeadListDTO> originalData;

    public void franchiseListTableDesign() {
        tcAreaHeadListId.prefWidthProperty().bind(tvAreaHeadList.widthProperty().multiply(0.06));
        tcAreaHeadListFullName.prefWidthProperty().bind(tvAreaHeadList.widthProperty().multiply(0.23));
        tcfAreaHeadListAddress.prefWidthProperty().bind(tvAreaHeadList.widthProperty().multiply(0.23));
        tcAreaHeadListMobile.prefWidthProperty().bind(tvAreaHeadList.widthProperty().multiply(0.23));
        tcAreaHeadListRole.prefWidthProperty().bind(tvAreaHeadList.widthProperty().multiply(0.20));
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        ResponsiveWiseCssPicker();
//        spRootPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
//        spRootPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);




        spRootPane.addEventFilter(KeyEvent.KEY_PRESSED, (KeyEvent event) -> {
//            if (event.getCode() == KeyCode.DOWN && tffieldSearch.isFocused()){
//                tvAreaHeadList.getSelectionModel().select(0);
//                tvAreaHeadList.requestFocus();
//            }else if (event.getCode() == KeyCode.DOWN && tffieldSearch.isFocused()){
//                tvAreaHeadList.getSelectionModel().select(0);
//                tvAreaHeadList.requestFocus();
//            }
            if (event.getCode() == KeyCode.DOWN && tffieldSearch.isFocused()) {
//                btnFranchiseListCreate.fire();
                tvAreaHeadList.getSelectionModel().select(0);
                tvAreaHeadList.requestFocus();
            }

            if (event.getCode() == KeyCode.ENTER) {
                franchiseEditPage();
                if (event.getTarget() instanceof Button targetButton && targetButton.getText().equalsIgnoreCase("Create")) {
                    System.out.println(targetButton.getText());
                }
                else {
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
            }
            if (event.getCode() == KeyCode.E && event.isControlDown()) {
                franchiseEditPage();
            }
            if (event.getCode() == KeyCode.D && event.isControlDown()) {
                franchiseDeletePage();
            }
            if (event.getCode() == KeyCode.N && event.isControlDown()) {
                btnFranchiseListCreate.fire();
            }
//            if (event.getCode() == KeyCode.E && event.isControlDown()) {
////                salesChallanEditPage();
//                btnSalesChallanModify.fire();
//            }

//            if (event.getCode() == KeyCode.X && event.isControlDown()) {
//                btnAreaHeadCreateCancel.fire();
//            }
        });

        franchiseListTableDesign();
        getAreaHeadData();
        Platform.runLater(() -> tffieldSearch.requestFocus());
        //get the id onDoubleClick for Edit
        tvAreaHeadList.setRowFactory(tv -> {
            TableRow<AreaHeadListDTO> row = new TableRow<>();
            row.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    if (mouseEvent.getClickCount() == 2) {
                        AreaHeadListDTO areaHeadListDTO = row.getItem();
                        franchiseEditPage();
                    }
                }
            });
            return row;
        });

        tvAreaHeadList.setOnKeyPressed(event -> {
            System.out.println(event);
            TableRow<AreaHeadListDTO> row = new TableRow<>();

            if (event.getCode() == KeyCode.TAB || event.getCode() == KeyCode.ENTER) {
                AreaHeadListDTO areaHeadListDTO = row.getItem();
                franchiseEditPage();
            }
        });


        btnFranchiseListCreate.setOnAction(event -> {
                    Globals.areaHeadListDTO = null;
                    GlobalController.getInstance().addTabStatic(AREA_HEAD_CREATE_SLUG, false);
                }
        );

        // Populate focusable nodes array with the desired order and Focus To next and Previous Element
        focusableNodes = new Node[]{tffieldSearch, btnFranchiseListCreate, tvAreaHeadList};
        CommonValidationsUtils.setupFocusNavigation(focusableNodes);

        //Search without API Call in the Table
        originalData = tvAreaHeadList.getItems();
        tffieldSearch.textProperty().addListener((observable, oldValue, newValue) -> {
            filterData(newValue.trim());
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

    //    Search Function to Search in the Table
    private void filterData(String keyword) {
        ObservableList<AreaHeadListDTO> filteredData = FXCollections.observableArrayList();

        if (keyword.isEmpty()) {
            tvAreaHeadList.setItems(originalData);
            return;
        }

        for (AreaHeadListDTO item : originalData) {
            if (matchesKeyword(item, keyword)) {
                filteredData.add(item);
            }
        }

        tvAreaHeadList.setItems(filteredData);
    }

    //
//    //Search Function to Search in the Table for columns
    private boolean matchesKeyword(AreaHeadListDTO item, String keyword) {
        String lowerCaseKeyword = keyword.toLowerCase();

        // Check if any of the columns contain the keyword
        return item.getFullName().toLowerCase().contains(lowerCaseKeyword);
//                item.get().toLowerCase().contains(lowerCaseKeyword) ||
//                item.getMobileNumber().toLowerCase().contains(lowerCaseKeyword) ||
//                item.getState().toLowerCase().contains(lowerCaseKeyword) ||
//                item.getAreaRole().toLowerCase().contains(lowerCaseKeyword);
    }

    public void franchiseCreatePage(ActionEvent event) {
        try {
            Globals.franchiseListDTO = null;
            Parent parent = FXMLLoader.load(getClass().getResource("/com/opethic/genivis/ui/master/areaHeadCreate.fxml"));
            Stage stage = new Stage();
            stage.setTitle("Create Page");
            stage.setMaximized(true);
            stage.setScene(new Scene(parent, 1920, 1080));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void franchiseEditPage() {
        try {
            areaHeadListDTO = tvAreaHeadList.getSelectionModel().getSelectedItem();
            Integer id = Integer.valueOf(areaHeadListDTO.getId());
            System.out.println("id-->" + id);
            GlobalController.getInstance().addTabStaticWithParam(AREA_HEAD_EDIT_SLUG, false, id);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void deleteSaleOrder(int id) {
        try {
            AlertUtility.CustomCallback callback = number -> {
                if (number == 1) {
//                GlobalController.getInstance().addTabStatic(FRANCHISE_LIST_SLUG, false);
                    System.out.println("this is idyes" + id);
                    try {
                        Map<String, String> map = new HashMap<>();
                        map.put("id", String.valueOf(id));
                        String formData = Globals.mapToStringforFormData(map);
                        HttpResponse<String> response = APIClient.postFormDataRequest(formData, EndPoints.deleteAreaHead);
                        responseBody = response.body();
                        JsonObject jsonObject = new Gson().fromJson(responseBody, JsonObject.class);
                        message = jsonObject.get("message").getAsString();
                        System.out.println("Delete->" + jsonObject.get("message"));
//                        AlertUtility.CustomCallback callback1 = (number1) -> {
//                            if (number1 == 1) {
//                                // Update the list after successful deletion
//                                observableList.removeIf(item -> item.getId().equals(String.valueOf(id)));
//                                tblvSalesChallan.setItems(observableList);
//                            }
//                        };
//                        Stage stage2 = (Stage) spRootSalesChallanListPane.getScene().getWindow();
//                        if (jsonObject.get("responseStatus").getAsInt() == 200) {
//                            AlertUtility.AlertSuccess(stage2, "Success", message, callback1);
//                        } else {
//                            AlertUtility.AlertError(stage2, AlertUtility.alertTypeError, message, callback1);
//                        }
                        if (jsonObject.get("responseStatus").getAsInt() == 200) {
                            AlertUtility.AlertSuccessTimeout(AlertUtility.alertTypeSuccess, message, input -> {
                                // Update the list after successful deletion
//                                observableList.removeIf(item -> item.getId().equals(String.valueOf(id)));
//                                tblvSalesChallan.setItems(observableList);
                                getAreaHeadData();
                            });
                        } else {
                            AlertUtility.AlertErrorTimeout(AlertUtility.alertTypeError, message, in -> {
                            });
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    System.out.println("working!");
//                System.out.println("this is idno"+id);
                }
            };
            AlertUtility.AlertConfirmation(AlertUtility.alertTypeConfirmation, "Do you want to Delete", callback);
//        Globals.salesChallanListDTO=null;
//        GlobalController.getInstance().addTabStatic(FRANCHISE_LIST_SLUG,false);
        } catch (Exception e) {

        }

    }


    public void franchiseDeletePage() {
        try {
            areaHeadListDTO = tvAreaHeadList.getSelectionModel().getSelectedItem();
            Integer id = Integer.valueOf(areaHeadListDTO.getId());
            System.out.println("id-->" + id);
            deleteSaleOrder(id);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void getAreaHeadData() {
        try {
            tvAreaHeadList.getItems().clear();
            HttpResponse<String> response = APIClient.getRequest("get_all_area_head");
            JsonObject jsonObject = new Gson().fromJson(response.body(), JsonObject.class);
            System.out.println("i am list" + jsonObject);
            ObservableList<AreaHeadListDTO> observableList = FXCollections.observableArrayList();
            if (jsonObject.get("responseStatus").getAsInt() == 200) {
                JsonArray responseObject = jsonObject.getAsJsonArray("responseObject");

                if (responseObject.size() > 0) {
                    for (JsonElement element : responseObject) {
                        Long i = 0L;
                        JsonObject item = element.getAsJsonObject();
                        String index = String.valueOf(i + 1);
                        String id = item.get("id").getAsString();
                        String fullName = item.get("fullName").getAsString();

                        String firstName = item.get("firstName").getAsString();
                        String middleName = item.get("middleName").getAsString();
                        String lastName = item.get("lastName").getAsString();
                        String email = item.get("email").getAsString();
                        String mobileNumber = item.get("mobileNumber").getAsString();
                        String whatsAppNumber = item.get("whatsAppNumber").getAsString();
                        String DOB = item.get("DOB").getAsString();
                        String address = item.get("address").getAsString();
                        String gender = item.get("gender").getAsString();
                        String aadharCard = item.get("aadharCard").getAsString();
                        String panCard = item.get("panCard").getAsString();
                        String areaRole = item.get("areaRole").getAsString();
                        String isActive = item.get("isActive").getAsString();
//                        String state = item.get("stateName").getAsString();
//

                        observableList.add(new AreaHeadListDTO(id, fullName,
                                address,
                                mobileNumber,
                                areaRole
//                                state
                        ));
//                                franchiseCode,
//                                franchiseAddress,franchisemobileNumber,franchisepincode)

                    }
                    tcAreaHeadListId.setCellValueFactory(new PropertyValueFactory<>("id"));
                    tcAreaHeadListFullName.setCellValueFactory(new PropertyValueFactory<>("fullName"));
                    tcfAreaHeadListAddress.setCellValueFactory(new PropertyValueFactory<>("address"));
                    tcAreaHeadListMobile.setCellValueFactory(new PropertyValueFactory<>("mobileNumber"));
                    tcAreaHeadListRole.setCellValueFactory(new PropertyValueFactory<>("areaRole"));
                    tcAreaHeadListState.setCellValueFactory(new PropertyValueFactory<>("State"));
//                    tcAreaHeadListZone.setCellValueFactory(new PropertyValueFactory<>("Zone"));
//                    tcAreaHeadListRegion.setCellValueFactory(new PropertyValueFactory<>("Region"));
//                    tcAreaHeadListDistrict.setCellValueFactory(new PropertyValueFactory<>("District"));

                    tvAreaHeadList.setItems(observableList);

                    //******************************** Highlight on the Created/Edited Record in the List Start ********************************
                    if (AreaHeadListController.isNewAreaHeadCreated) {
                        tvAreaHeadList.getSelectionModel().selectLast();
                        tvAreaHeadList.scrollTo(tvAreaHeadList.getItems().size() - 1);
                        AreaHeadListController.isNewAreaHeadCreated = false; // Reset the flag
                    } else if (AreaHeadListController.editedAreaHeadId != null) {
                        for (AreaHeadListDTO franchise : observableList) {
                            if (franchise.getId().equals(AreaHeadListController.editedAreaHeadId)) {
                                tvAreaHeadList.getSelectionModel().select(franchise);
                                tvAreaHeadList.scrollTo(franchise);
                                AreaHeadListController.editedAreaHeadId = null; // Reset the ID
                                break;
                            }
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


    }


}
