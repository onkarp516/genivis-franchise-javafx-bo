package com.opethic.genivis.controller.utilities;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.opethic.genivis.GenivisApplication;
import com.opethic.genivis.models.CompanyUserModel;
import com.opethic.genivis.network.APIClient;
import com.opethic.genivis.network.EndPoints;
import com.opethic.genivis.network.RequestType;
import com.opethic.genivis.utils.AlertUtility;
import com.opethic.genivis.utils.FxmFileConstants;
import com.opethic.genivis.utils.GlobalController;
import com.opethic.genivis.utils.Globals;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.StringConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.http.HttpResponse;
import java.util.*;

import static com.opethic.genivis.utils.FxmFileConstants.CREDIT_NOTE_LIST_SLUG;

public class ProductImportController implements Initializable {

    @FXML
    public Button btnChooseFile;
    @FXML
    public TextField tfSelectedFileName;
    @FXML
    public Button btnSubmit;
    @FXML
    public ProgressBar pbProgressProductImport;
    @FXML
    public HBox hbProgress;
    @FXML
    public Region rSpace;
    @FXML
    private BorderPane bpRootProductImportPane;
    private static final Logger comCreateLogger = LoggerFactory.getLogger(ProductImportController.class);

    FileChooser fileChooser = new FileChooser();
    File selectedFile;
    AlertUtility.CustomCallback callback;
    Double duration=0.01;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {


        pbProgressProductImport.prefWidthProperty().bind(hbProgress.widthProperty().subtract(50));


        /*pbProgressProductImport.setProgress(duration);

        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if(duration>2.0)
                duration+=0.01;
                System.out.println(""+duration);
                if(duration<=1.1){
                    pbProgressProductImport.setProgress(duration);
                }else{
                    timer.cancel();
                }
            }
        }, 0, 1000);*/


        callback = (number) -> {
            //System.out.println("Received : "+number);
        };

        btnChooseFile.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                selectedFile = fileChooser.showOpenDialog(btnChooseFile.getScene().getWindow());
                if(selectedFile!=null)
                    tfSelectedFileName.setText(selectedFile.getName());
            }
        });

        btnSubmit.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if(selectedFile==null){
                    System.out.println("No file is selected, Plz select File");
                    AlertUtility.AlertWarning("No File Selected","Please select file!",callback);
                }else{

                    pbProgressProductImport.setVisible(!pbProgressProductImport.isVisible());

                    Map<String, String> headers = new HashMap<>();
                    headers.put("branch", Globals.headerBranch);

                    Map<String, File> fileMap = null;
                    if (selectedFile != null) {
                        fileMap = new HashMap<>();
                        fileMap.put("productfile", selectedFile);
                    }


                    Globals.decimalFormat.format(10.0);
                    APIClient apiClient=new APIClient(EndPoints.IMPORT_PRODUCT_ENDPOINT,null,headers,fileMap, RequestType.MULTI_PART);

                    apiClient.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
                        @Override
                        public void handle(WorkerStateEvent event) {
                            pbProgressProductImport.setVisible(false);
                            if(event.getSource().getValue()!=null){
                                System.out.println("OnSucceed : "+event.getSource().getValue().toString());
                                JsonObject responseObject=new Gson().fromJson(event.getSource().getValue().toString(),JsonObject.class);
                                if(responseObject.get("responseStatus").getAsInt()==200){
                                    AlertUtility.AlertSuccess("Product Import",responseObject.get("message").getAsString(),callback);
                                }else{
                                    AlertUtility.AlertError("Product Import",responseObject.get("message").getAsString(),callback);
                                }
                            }
                        }
                    });
                    apiClient.setOnFailed(new EventHandler<WorkerStateEvent>() {
                        @Override
                        public void handle(WorkerStateEvent event) {
                            pbProgressProductImport.setVisible(false);
                            System.out.println("OnFailed : "+event.getSource().getValue().toString());
                        }
                    });
                    apiClient.setOnCancelled(new EventHandler<WorkerStateEvent>() {
                        @Override
                        public void handle(WorkerStateEvent event) {
                            pbProgressProductImport.setVisible(false);
                            System.out.println("OnCncelled : "+event.getSource().getValue().toString());
                        }
                    });
                    apiClient.start();

                }
            }
        });
    }
}
