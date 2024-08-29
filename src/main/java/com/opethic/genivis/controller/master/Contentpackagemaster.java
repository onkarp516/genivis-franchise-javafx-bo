package com.opethic.genivis.controller.master;

import com.opethic.genivis.GenivisApplication;
import com.opethic.genivis.dto.ContentPackageMstDTO;
import com.opethic.genivis.network.APIClient;
import com.opethic.genivis.network.EndPoints;
import com.opethic.genivis.network.RequestType;
import com.opethic.genivis.utils.AlertUtility;
import com.opethic.genivis.utils.Globals;
import javafx.application.Platform;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Screen;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import java.net.URL;
import java.util.ResourceBundle;

public class Contentpackagemaster implements Initializable {
    @FXML
    private TableColumn<?, ?> tblvcol1contentpackage;

    @FXML
    private TableView<ContentPackageMstDTO> tblvcontentpackage;

    @FXML
    private TextField tfcontentpackagemaster;

    @FXML
    private BorderPane bpContentPackageMasterRootPane;

    ObservableList<ContentPackageMstDTO> orgRow = FXCollections.observableArrayList();

    private static final Logger loggerContentPackageMaster = LogManager.getLogger(Contentpackagemaster.class);
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        ResponsiveWiseCssPicker();

        tfcontentpackagemaster.setOnKeyPressed(this::handleEnterKeyPressed);
        tblvcontentpackage.setOnKeyPressed(this::handleEnterKeyPressed);

        Platform.runLater(() -> {
            tfcontentpackagemaster.requestFocus();
        });
        getOnLoadData();
        tfcontentpackagemaster.textProperty().addListener((observable, oldValue, newValue) -> {
            FilterTableData(newValue);
        });
    }

    private void ResponsiveWiseCssPicker() {
        double height = Screen.getPrimary().getBounds().getHeight();
        double width = Screen.getPrimary().getBounds().getWidth();
        System.out.println("width" + width);
        if (width >= 992 && width <= 1024) {
            bpContentPackageMasterRootPane.getStylesheets().add(GenivisApplication.class.getResource("ui/css/TranxCommonCssStyles/tranxCommonCssStyle1.css").toExternalForm());
        } else if (width >= 1025 && width <= 1280) {
            bpContentPackageMasterRootPane.getStylesheets().add(GenivisApplication.class.getResource("ui/css/TranxCommonCssStyles/tranxCommonCssStyle2.css").toExternalForm());
        } else if (width >= 1281 && width <= 1368) {
            bpContentPackageMasterRootPane.getStylesheets().add(GenivisApplication.class.getResource("ui/css/TranxCommonCssStyles/tranxCommonCssStyle3.css").toExternalForm());
        } else if (width >= 1369 && width <= 1400) {
            bpContentPackageMasterRootPane.getStylesheets().add(GenivisApplication.class.getResource("ui/css/TranxCommonCssStyles/tranxCommonCssStyle4.css").toExternalForm());
        } else if (width >= 1401 && width <= 1440) {
            bpContentPackageMasterRootPane.getStylesheets().add(GenivisApplication.class.getResource("ui/css/TranxCommonCssStyles/tranxCommonCssStyle5.css").toExternalForm());
        } else if (width >= 1441 && width <= 1680) {
            bpContentPackageMasterRootPane.getStylesheets().add(GenivisApplication.class.getResource("ui/css/TranxCommonCssStyles/tranxCommonCssStyle6.css").toExternalForm());
        } else if (width >= 1681 && width <= 1920) {
            bpContentPackageMasterRootPane.getStylesheets().add(GenivisApplication.class.getResource("ui/css/TranxCommonCssStyles/tranxCommonCssStyle7.css").toExternalForm());
        }
    }

    private void handleEnterKeyPressed(KeyEvent keyEvent) {
        Node source = (Node) keyEvent.getSource();
        if (keyEvent.getCode() == KeyCode.ENTER)
        {
            switch (source.getId()) {
                case "tfcontentpackagemaster":
                    if (!tfcontentpackagemaster.getText().isEmpty()) {
                        Platform.runLater(()->{
                            tblvcontentpackage.requestFocus();
                        });
                    }
                    break;
                default:
                    break;
            }
        }
    }


    public void getOnLoadData() {
        APIClient apiClient=null;
        try {
            tblvcontentpackage.getItems().clear();
            apiClient=new APIClient(EndPoints.getAllContentPackageMaster, "", RequestType.GET);
            apiClient.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
                @Override
                public void handle(WorkerStateEvent workerStateEvent) {
                    JSONObject jsonObject = new JSONObject(workerStateEvent.getSource().getValue().toString());
                    if (jsonObject.getInt("responseStatus") == 200) {
                        JSONArray jarr = jsonObject.getJSONArray("responseObject");
                        if (jarr.length() > 0) {
                            for (Object object : jarr) {
                                JSONObject obj = new JSONObject(object.toString());
                                orgRow.add(new ContentPackageMstDTO(obj.getInt("id"), obj.getString("contentPackageName")));
                            }
                            FilteredList<ContentPackageMstDTO> filteredList = new FilteredList<>(orgRow);
                            tblvcol1contentpackage.setCellValueFactory(new PropertyValueFactory<>("contentPackageName"));
                            tblvcontentpackage.setItems(filteredList);
                        }
                    } else {
                        Stage stage1 = (Stage) bpContentPackageMasterRootPane.getScene().getWindow();
                        String msg = jsonObject.getString("message");
                        AlertUtility.CustomCallback callback = number -> {
                            setFocus();
                        };
                        AlertUtility.AlertError(stage1,AlertUtility.alertTypeError, msg, callback);
                    }
                }
            });
            apiClient.setOnCancelled(new EventHandler<WorkerStateEvent>() {
                @Override
                public void handle(WorkerStateEvent workerStateEvent) {
                    loggerContentPackageMaster.error("Network API cancelled in getOnLoadData() Content Package Master" + workerStateEvent.getSource().getValue().toString());

                }
            });
            apiClient.setOnFailed(new EventHandler<WorkerStateEvent>() {
                @Override
                public void handle(WorkerStateEvent workerStateEvent) {
                    loggerContentPackageMaster.error("Network API failed in getOnLoadData() Content Package Master" + workerStateEvent.getSource().getValue().toString());
                }
            });
            apiClient.start();
        } catch (Exception e) {
            loggerContentPackageMaster.error("Exception getOnLoadData() Content Package Master: " + Globals.getExceptionString(e));
        }
    }

    private void setFocus() {
        tfcontentpackagemaster.requestFocus();
    }

    private void FilterTableData(String newValue) {
        try{
            FilteredList<ContentPackageMstDTO> filteredList;
            filteredList = new FilteredList<>(orgRow);
            final String searchString = newValue.toUpperCase();
            filteredList.setPredicate(logEvent -> {
                if (searchString.isEmpty()) {
                    return true;
                }
                String filterBy = "contentPackageName";
                String targetString = "";
                try {
                    targetString = ((StringProperty) logEvent.getPropertyByName(filterBy)).getValue();
                } catch (Exception e) {
                    loggerContentPackageMaster.error("Exception FilterTableData() Content Package Master: " + Globals.getExceptionString(e));
                }
                return targetString.toUpperCase().contains(searchString.toUpperCase());
            });
            tblvcontentpackage.setItems(filteredList);
        } catch (Exception e) {
            loggerContentPackageMaster.error("Exception FilterTableData() Content Package Master : " + Globals.getExceptionString(e));
        }
    }
}
