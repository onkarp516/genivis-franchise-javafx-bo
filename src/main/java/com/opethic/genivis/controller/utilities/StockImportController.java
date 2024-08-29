package com.opethic.genivis.controller.utilities;

import com.opethic.genivis.network.APIClient;
import com.opethic.genivis.network.EndPoints;
import com.opethic.genivis.network.RequestType;
import com.opethic.genivis.utils.AlertUtility;
import com.opethic.genivis.utils.Globals;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.stage.FileChooser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class StockImportController implements Initializable {

    @FXML
    public Button btnChooseFile;
    @FXML
    public TextField tfSelectedFileName;
    @FXML
    public Button btnSubmit;
    @FXML
    public ProgressBar pbProgressStockImport;
    @FXML
    public HBox hbProgress;
    @FXML
    public Region rSpace;
    @FXML
    private BorderPane bpRootProductImportPane;
    private static final Logger comCreateLogger = LoggerFactory.getLogger(StockImportController.class);

    FileChooser fileChooser = new FileChooser();
    File selectedFile;
    AlertUtility.CustomCallback callback;
    Double duration=0.01;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {


        pbProgressStockImport.prefWidthProperty().bind(hbProgress.widthProperty().subtract(50));


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
            System.out.println("Received : "+number);
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

                    pbProgressStockImport.setVisible(!pbProgressStockImport.isVisible());

                    Map<String, String> headers = new HashMap<>();
                    headers.put("branch", Globals.headerBranch);

                    Map<String, File> fileMap = null;
                    if (selectedFile != null) {
                        fileMap = new HashMap<>();
                        fileMap.put("productstockfile", selectedFile);
                    }

                    Globals.decimalFormat.format(10.0);
                    APIClient apiClient=new APIClient(EndPoints.IMPORT_STOCK_ENDPOINT,null,headers,fileMap, RequestType.MULTI_PART);

                    apiClient.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
                        @Override
                        public void handle(WorkerStateEvent event) {
                            pbProgressStockImport.setVisible(false);
                            if(event.getSource().getValue()!=null){
                                System.out.println("OnSucceed : "+event.getSource().getValue().toString());
                            }
                        }
                    });
                    apiClient.setOnFailed(new EventHandler<WorkerStateEvent>() {
                        @Override
                        public void handle(WorkerStateEvent event) {
                            pbProgressStockImport.setVisible(false);
                            System.out.println("OnFailed : "+event.getSource().getValue().toString());
                        }
                    });
                    apiClient.setOnCancelled(new EventHandler<WorkerStateEvent>() {
                        @Override
                        public void handle(WorkerStateEvent event) {
                            pbProgressStockImport.setVisible(false);
                            System.out.println("OnCncelled : "+event.getSource().getValue().toString());
                        }
                    });
                    apiClient.start();

                }
            }
        });
    }
}
