package com.opethic.genivis.controller.utilities;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
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
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.stage.DirectoryChooser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class SetBarcodeHomePathController implements Initializable {

    @FXML
    public Button btnChooseDir;
    @FXML
    public TextField tfSelectedDirName;
    @FXML
    public Button btnSubmit;
//    @FXML
//    public ProgressBar pbProgressProductImport;
    @FXML
    public HBox hbProgress;
    @FXML
    public Region rSpace;
    @FXML
    public TextField tfPrnFileName;
    @FXML
    private BorderPane bpRootProductImportPane;
    private static final Logger comCreateLogger = LoggerFactory.getLogger(SetBarcodeHomePathController.class);

    DirectoryChooser directoryChooser = new DirectoryChooser();
    File selectedDir;
    AlertUtility.CustomCallback callback;
    boolean isUpdate=true;
    private long rowId=0;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {


//        pbProgressProductImport.prefWidthProperty().bind(hbProgress.widthProperty().subtract(50));


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

        getData();

        callback = (number) -> {
            //System.out.println("Received : "+number);
        };

        btnChooseDir.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                selectedDir = directoryChooser.showDialog(btnChooseDir.getScene().getWindow());
                if(selectedDir!=null){
                    Globals.barcode_home_path=selectedDir.getAbsolutePath();
                    tfSelectedDirName.setText(Globals.barcode_home_path);
                }
            }
        });

        /* Barcode working code fine
        try {
            Process p = Runtime
                    .getRuntime()
                    .exec("cmd start cmd.exe /K \"cd D:/Barcode/SP && TVS.exe ETHIQ8.prn\"");
            BufferedReader r = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String line;
            while (true) {
                line = r.readLine();
                if (line == null) {
                    p.destroy();
                    break;
                }else{
                    System.out.println(line);
                }
            }

        } catch (Exception e) {
            System.out.printf("Error = > "+e.toString());
        }*/
        btnSubmit.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                    updateBarcodeHomePath();
            }
        });
    }

    private void updateBarcodeHomePath() {
        if(isValidData()){
            Globals.prn_file_name=tfPrnFileName.getText();
            Map<String, String> request=new HashMap();
            request.put("barcode_home_path",tfSelectedDirName.getText());
            request.put("prf_file_name",tfPrnFileName.getText());
            request.put("id",""+rowId);
            String reqBody = Globals.mapToStringforFormData(request);
            APIClient apiClient=new APIClient(EndPoints.UPDATE_BARCODE_PATH,reqBody, RequestType.FORM_DATA);
            apiClient.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
                @Override
                public void handle(WorkerStateEvent workerStateEvent) {
                    if(workerStateEvent.getSource().getValue()!=null){
                        System.out.println("OUT=>"+workerStateEvent.getSource().getValue().toString());
                        JsonObject responseObject=new Gson().fromJson(workerStateEvent.getSource().getValue().toString(),JsonObject.class);
                        System.out.printf("SOUT=>"+responseObject.toString());
                        String message = responseObject.get("message").getAsString();
                        if(responseObject.get("status").getAsInt()==200){
                            JsonObject dataObject=responseObject.get("data").getAsJsonObject();
                            if(dataObject.has("id")){
                                rowId=dataObject.get("id").getAsLong();
                                Globals.barcode_home_path=dataObject.get("barcode_home_path").getAsString();
                                Globals.prn_file_name=dataObject.get("prn_file_name").getAsString();
                            }
                            System.out.println("Response => "+message);
                            AlertUtility.AlertSuccess("Barcode Home Path Update",message,callback);
                        }else{
                            System.out.println("Response Failed => "+message);
                            AlertUtility.AlertError("Barcode Home Path Update",message,callback);
                        }
                    }
                }
            });
            apiClient.setOnCancelled(new EventHandler<WorkerStateEvent>() {
                @Override
                public void handle(WorkerStateEvent workerStateEvent) {
                    System.out.printf("OnCancelled=>"+workerStateEvent.getSource().getValue());
                    AlertUtility.AlertError("Barcode Home Path Update","Failed to update path!",callback);
                }
            });
            apiClient.setOnFailed(new EventHandler<WorkerStateEvent>() {
                @Override
                public void handle(WorkerStateEvent workerStateEvent) {
                    System.out.printf("OnFailed=>"+workerStateEvent.getSource().getValue());
                    AlertUtility.AlertError("Barcode Home Path Update","Failed to update path! try again",callback);
                }
            });

            apiClient.start();
        }
    }



    private boolean isValidData() {
        //boolean result=false;
        if(tfPrnFileName.getText().isEmpty() || !tfPrnFileName.getText().endsWith(".prn")){
            AlertUtility.AlertError("Error","Invalid Prn File Name",callback);
            return false;
        }
        if (tfSelectedDirName.getText().isEmpty()) {
            AlertUtility.AlertError("Error","Invalid Prn File Name",callback);
            return false;
        }
        return true;
    }

    private void getData() {
        APIClient apiClient=new APIClient(EndPoints.GET_BARCODE_PATH,"", RequestType.JSON);
        apiClient.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent workerStateEvent) {
                if(workerStateEvent.getSource().getValue()!=null){
                    System.out.println("OUT=>"+workerStateEvent.getSource().getValue().toString());
                    JsonObject responseObject=new Gson().fromJson(workerStateEvent.getSource().getValue().toString(),JsonObject.class);
                    System.out.printf("SOUT=>"+responseObject.toString());
                    if(responseObject.get("status").getAsInt()==200){
                        JsonObject jsonObject=responseObject.get("data").getAsJsonObject();
                        rowId=jsonObject.get("id").getAsLong();
                        tfSelectedDirName.setText(jsonObject.get("barcode_home_path").getAsString());
                        tfPrnFileName.setText(jsonObject.get("prn_file_name").getAsString());
                    }else{
                        tfSelectedDirName.setText("");
                        tfPrnFileName.setText("");
                    }
                }
            }
        });
        apiClient.setOnCancelled(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent workerStateEvent) {
                System.out.printf("OnCancelled=>"+workerStateEvent.getSource().getValue());
            }
        });
        apiClient.setOnFailed(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent workerStateEvent) {
                System.out.printf("OnFailed=>"+workerStateEvent.getSource().getValue());
            }
        });

        apiClient.start();
    }
}
