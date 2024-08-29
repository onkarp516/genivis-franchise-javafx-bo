package com.opethic.genivis.controller.master;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.opethic.genivis.dto.FranchiseListDTO;
import com.opethic.genivis.network.APIClient;
import com.opethic.genivis.network.EndPoints;
import com.opethic.genivis.network.RequestType;
import com.opethic.genivis.utils.AlertUtility;
import com.opethic.genivis.utils.CommonValidationsUtils;
import com.opethic.genivis.utils.GlobalController;
import com.opethic.genivis.utils.Globals;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.WorkerStateEvent;
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
import javafx.stage.Stage;
import javafx.util.Duration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.URL;
import java.net.http.HttpResponse;
import java.util.ResourceBundle;

import static com.opethic.genivis.utils.FxmFileConstants.FRANCHISE_CREATE_SLUG;
import static com.opethic.genivis.utils.FxmFileConstants.FRANCHISE_EDIT_SLUG;
import static com.opethic.genivis.utils.Globals.franchiseListDTO;


public class FranchiseListController implements Initializable {

    @FXML
    private TableView<FranchiseListDTO> tvfranchiseList;
    @FXML
    private TableColumn<FranchiseListDTO,String> tcfranchiseListId;
    @FXML
    private TableColumn<FranchiseListDTO,String>  tcfranchiseListName;
    @FXML
    private TableColumn<FranchiseListDTO,String>  tcfranchiseListCode;
    @FXML
    private TableColumn<FranchiseListDTO,String>  tcfranchiseListAddress;
    @FXML
    private TableColumn<FranchiseListDTO,String>  tcfranchiseListMobileNumber;
    @FXML
    private TableColumn<FranchiseListDTO,String>  tcfranchiseListPincode;

    @FXML
    private TextField tffieldSearch;
    @FXML
    private Button btnFranchiseListCreate;

    private Node[] focusableNodes;

    private ObservableList<FranchiseListDTO> originalData;
    @FXML
    private BorderPane spRootFranchiseListPane;
    private JsonObject jsonObject = null;
    private static final Logger logger = LogManager.getLogger(FranchiseListController.class);

    //? Highlight the Record Start
    public static boolean isNewFranchiseCreated = false; // Flag for new creation
    public static String editedFranchiseId = null; // ID for edited franchise
    //? Highlight the Record End
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        //? this include all the Shortcut Keys
        initShortcutKeys();

        //? this inculde all the design related properties and important Fields
        mandatoryFields();

        //? Function to Get the List of Franchise
        getFranchiseData();
        //Auto Set the Numbers 1,2,3,...
        autoSetSerialNo();

        //? When Open the Page Focus of the First Field
        Platform.runLater(() -> tffieldSearch.requestFocus());

        //? get the id onDoubleClick for Edit
        tvfranchiseList.setRowFactory(tv ->{
            TableRow<FranchiseListDTO> row=new TableRow<>();
            row.setOnMouseClicked(new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent mouseEvent) {
                    if(mouseEvent.getClickCount()==2){
                        FranchiseListDTO franchiseListDTO=row.getItem();
                        franchiseEditPage();
                    }
                }
            });
            return row;
        });

        //? Search without API Call in the Table
        originalData = tvfranchiseList.getItems();
        tffieldSearch.textProperty().addListener((observable, oldValue, newValue) -> {
            filterData(newValue.trim());
            //Auto Set the Numbers 1,2,3,...
            autoSetSerialNo();
        });

        //Redirect to Create Page from List Page on CLick on the Create Button and in the Same Tab of TabPane
        btnFranchiseListCreate.setOnAction( event ->{
                    Globals.franchiseListDTO=null;
                    GlobalController.getInstance().addTabStatic(FRANCHISE_CREATE_SLUG,false);
                }
        );
    }

    private void autoSetSerialNo() {
        //Auto Set the Numbers 1,2,3,...
        tcfranchiseListId.setCellFactory(column -> {
            return new TableCell<>() {
                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);

                    if (empty || getIndex() < 0) {
                        setText(null);
                    } else {
                        // Set the serial number as the index of the row + 1
                        setText(String.valueOf(getIndex() + 1));
                    }
                }
            };
        });
    }

    private void mandatoryFields() {
        //Set the TableView Columns with proper height and Width
        tvfranchiseList.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }

    private void initShortcutKeys() {
        spRootFranchiseListPane.addEventFilter(KeyEvent.KEY_PRESSED, (KeyEvent event) -> {
            if (event.getCode() == KeyCode.ENTER) {
                if (event.getTarget() instanceof Button targetButton && targetButton.getText().equalsIgnoreCase("create")) {
                } else {
                    franchiseEditPage();
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

            if (event.getCode() == KeyCode.N && event.isControlDown()) {
                btnFranchiseListCreate.fire();
            }

            if (event.getCode() == KeyCode.DOWN && tffieldSearch.isFocused()) {
                tvfranchiseList.getSelectionModel().select(0);
                tvfranchiseList.requestFocus();
            } else if (event.getCode() == KeyCode.E && event.isControlDown()) {
                franchiseEditPage();
            }
        });
    }

    //? Search Function to Search in the Table
    private void filterData(String keyword) {
        ObservableList<FranchiseListDTO> filteredData = FXCollections.observableArrayList();

        if (keyword.isEmpty()) {
            tvfranchiseList.setItems(originalData);
            return;
        }

        for (FranchiseListDTO item : originalData) {
            if (matchesKeyword(item, keyword)) {
                filteredData.add(item);
            }
        }

        tvfranchiseList.setItems(filteredData);
    }

    //? Search Function to Search in the Table for columns
    private boolean matchesKeyword(FranchiseListDTO item, String keyword) {
        String lowerCaseKeyword = keyword.toLowerCase();

        // ? Check if any of the columns contain the keyword
        return item.getFranchiseName().toLowerCase().contains(lowerCaseKeyword) ||
                item.getFranchiseCode().toLowerCase().contains(lowerCaseKeyword) ||
                item.getFranchiseAddress().toLowerCase().contains(lowerCaseKeyword) ||
                item.getFranchisemobileNumber().toLowerCase().contains(lowerCaseKeyword) ||
                item.getFranchisepincode().toLowerCase().contains(lowerCaseKeyword);
    }
    //?  Open Create Page Only for Edit in the Same Tab
    public void franchiseEditPage() {
        try {

            franchiseListDTO = tvfranchiseList.getSelectionModel().getSelectedItem();
            if(Globals.franchiseListDTO!=null) {
                //? Highlight
                FranchiseListController.editedFranchiseId = franchiseListDTO.getId(); //? Set the ID for editing
                Integer id = Integer.valueOf(franchiseListDTO.getId());
                GlobalController.getInstance().addTabStaticWithParam(FRANCHISE_EDIT_SLUG, false, id);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //? Get and Set the  List Data of Franchise
    public void getFranchiseData(){
        APIClient apiClient=null;
        try{
            logger.debug("Get Franchise Data Started...");
//            String requestBody = Globals.mapToString(body);
            tvfranchiseList.getItems().clear();
//            HttpResponse<String> response = APIClient.getRequest(EndPoints.FRANCHISE_LIST_ENDPOINT);
            apiClient = new APIClient(EndPoints.FRANCHISE_LIST_ENDPOINT, "", RequestType.GET);
            apiClient.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
                @Override
                public void handle(WorkerStateEvent workerStateEvent) {
                    ObservableList<FranchiseListDTO> observableList = FXCollections.observableArrayList();
                    jsonObject = new Gson().fromJson(workerStateEvent.getSource().getValue().toString(), JsonObject.class);

//                    JsonObject jsonObject = new Gson().fromJson(response.body(), JsonObject.class);

                    if (jsonObject.get("responseStatus").getAsInt() == 200) {
                        JsonArray responseObject = jsonObject.getAsJsonArray("responseObject");

                        if (responseObject.size() > 0) {
                            for (JsonElement element : responseObject) {
                                Long i =0L;
                                JsonObject item = element.getAsJsonObject();
                                String index= String.valueOf(i+1);
                                String id = item.get("id").getAsString();
                                String franchiseName = item.get("franchiseName").getAsString();
                                String franchiseCode = item.get("franchiseCode").getAsString();
                                String franchiseAddress = item.get("franchiseAddress").getAsString();
                                String franchisemobileNumber = item.get("mobileNum").getAsString();
                                String franchisepincode = item.get("franchisePincode").getAsString();

                                observableList.add(new FranchiseListDTO(id,franchiseName,franchiseCode,
                                        franchiseAddress,franchisemobileNumber,franchisepincode)
                                );
                            }

                            tcfranchiseListName.setCellValueFactory(new PropertyValueFactory<>("franchiseName"));
                            tcfranchiseListCode.setCellValueFactory(new PropertyValueFactory<>("franchiseCode"));
                            tcfranchiseListAddress.setCellValueFactory(new PropertyValueFactory<>("franchiseAddress"));
                            tcfranchiseListMobileNumber.setCellValueFactory(new PropertyValueFactory<>("franchisemobileNumber"));
                            tcfranchiseListPincode.setCellValueFactory(new PropertyValueFactory<>("franchisepincode"));


                            // Update originalData with new items
                            originalData.setAll(observableList);
                            tvfranchiseList.setItems(observableList);

                            //******************************** Highlight on the Created/Edited Record in the List Start ********************************
                            if (FranchiseListController.isNewFranchiseCreated) {
                                tvfranchiseList.getSelectionModel().selectLast();
                                tvfranchiseList.scrollTo(tvfranchiseList.getItems().size() - 1);
                                FranchiseListController.isNewFranchiseCreated = false; // Reset the flag
                            } else if (FranchiseListController.editedFranchiseId != null) {
                                for (FranchiseListDTO franchise : observableList) {
                                    if (franchise.getId().equals(FranchiseListController.editedFranchiseId)) {
                                        tvfranchiseList.getSelectionModel().select(franchise);
                                        tvfranchiseList.scrollTo(franchise);
                                        FranchiseListController.editedFranchiseId = null; // Reset the ID
                                        break;
                                    }
                                }
                            }
                            //******************************** Highlight on the Created/Edited Record in the List End ********************************
                        } else {
                            logger.error("Failed to Load Data ");
                        }
                    } else {
                        //? use logger for alert messages
                        AlertUtility.CustomCallback callback = (number) -> {
                            if(number==0) {
                                tffieldSearch.requestFocus();
                            }
                        };
                        Stage stage = (Stage) spRootFranchiseListPane.getScene().getWindow();
                        AlertUtility.AlertError(stage,AlertUtility.alertTypeError, "Falied to connect server! ", callback);

                    }
                }
            });
            apiClient.setOnCancelled(new EventHandler<WorkerStateEvent>() {
                @Override
                public void handle(WorkerStateEvent workerStateEvent) {
                    logger.error("Network API cancelled in getFranchiseData()" + workerStateEvent.getSource().getValue().toString());

                }
            });
            apiClient.setOnFailed(new EventHandler<WorkerStateEvent>() {
                @Override
                public void handle(WorkerStateEvent workerStateEvent) {
                    logger.error("Network API failed in getFranchiseData()" + workerStateEvent.getSource().getValue().toString());
                }
            });
            apiClient.start();
            logger.debug("Get Franchise Data End...");
        } catch (Exception e) {
            AlertUtility.CustomCallback callback = (number) -> {
                if(number==0) {
                    tffieldSearch.requestFocus();
                }
            };
            Stage stage = (Stage) spRootFranchiseListPane.getScene().getWindow();
            AlertUtility.AlertError(stage,AlertUtility.alertTypeError, "Falied to connect server! ", callback);

            e.printStackTrace();
        }finally {
            apiClient=null;
        }
    }
}
