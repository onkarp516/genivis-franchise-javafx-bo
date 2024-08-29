package com.opethic.genivis.controller.master;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.opethic.genivis.controller.commons.SwitchButton;
import com.opethic.genivis.dto.OutletAppConfigDTO;
import com.opethic.genivis.dto.reqres.OutletAppConfigRes;
import com.opethic.genivis.network.APIClient;
import com.opethic.genivis.network.EndPoints;
import com.opethic.genivis.utils.AlertUtility;
import com.opethic.genivis.utils.Globals;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URL;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class UserControlController implements Initializable {

    @FXML
    private VBox vboxBalance;
    ObservableList<OutletAppConfigDTO> lstOutletAppConfig = FXCollections.observableArrayList();

    private static final Logger loggerUserController = LogManager.getLogger(UserControlController.class);


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        onLoadData();
    }


    private void onLoadData() {
        try {
            // Call the API to get outlet app config
            HttpResponse<String> response = APIClient.getRequest(EndPoints.getOutletAppConfig);
            JsonObject jsonObject = new Gson().fromJson(response.body(), JsonObject.class);

            // Handle response
            if (jsonObject.get("responseStatus").getAsInt() == 200) {
                // Process responseObject if needed
                JsonArray responseObject = jsonObject.getAsJsonArray("responseObject");
                // Example: Print out the response
                lstOutletAppConfig.clear();
                for (JsonElement element : responseObject) {
                    OutletAppConfigRes outletAppConfigRes = new Gson().fromJson(element, OutletAppConfigRes.class);
                    lstOutletAppConfig.add(new OutletAppConfigDTO(outletAppConfigRes.getId(), outletAppConfigRes.getSlug(), outletAppConfigRes.getDisplayName(), outletAppConfigRes.getValue(), outletAppConfigRes.getIsLabel(), outletAppConfigRes.getLabel()));
                }
                addDesign();
            } else {
                // Handle error response
                String errorMessage = jsonObject.get("message").getAsString();

            }
        } catch (Exception e) {
            loggerUserController.error("Exception onLoadData() : " + Globals.getExceptionString(e));

        }
    }

    private void addDesign() {
        vboxBalance.getChildren().clear();
        vboxBalance.setSpacing(10.0);
        vboxBalance.setPadding(new Insets(10.0));
        for (OutletAppConfigDTO appConfigDTO : lstOutletAppConfig) {
            vboxBalance.getChildren().add(addConfigComponent(appConfigDTO));
        }
    }

    private Node addConfigComponent(OutletAppConfigDTO appConfigDTO) {
        System.out.println("isLabel ->" + appConfigDTO.getIsLabel());
        HBox hBox = new HBox();
        VBox vBox = new VBox();
        vBox.setSpacing(20.0);
        hBox.setSpacing(20.0);
        SwitchButton swBtn = new SwitchButton();
        vBox.getChildren().add(swBtn);
        Label lbl = new Label(appConfigDTO.getDisplayName());
        lbl.setStyle("-fx-font-size:14px;");
        TextField tf = new TextField();
        tf.setText(appConfigDTO.getLabel());
        tf.setVisible(appConfigDTO.getIsLabel());
        tf.textProperty().addListener((obs, old, nw) -> {
            appConfigDTO.setLabel(nw);
        });
        swBtn.switchOnProperty().addListener((observable, oldValue, newValue) -> {
            appConfigDTO.setValue(newValue == true ? 1 : 0);
            // todo:Toggle visibility of tfFranchiseCreateFundingAmount based on the value of tgFranchiseIsFunding
            if (appConfigDTO.getIsLabel() == true) {
                if (newValue) {
                    tf.setVisible(true);
                } else {
                    tf.setVisible(false);
                }
            }
        });
        Platform.runLater(() -> {
            if (appConfigDTO.getValue() == 1) {
                swBtn.setSwitchedOn(true);
            }
            if (appConfigDTO.getIsLabel() == true && appConfigDTO.getValue() == 0) {
                tf.setVisible(false);
            }
        });

        hBox.getChildren().addAll(vBox, swBtn, lbl, tf);
        return hBox;
    }

    private void submitAPICall() {
        try {
            JSONArray userJsonArr = new JSONArray();
            for (OutletAppConfigDTO configDTO : lstOutletAppConfig) {
                JSONObject userObj = new JSONObject();
                userObj.put("id", configDTO.getId());
                userObj.put("display_name", configDTO.getDisplayName());
                userObj.put("slug", configDTO.getSlug());
                userObj.put("is_label", configDTO.getIsLabel());
                userObj.put("value", configDTO.getValue());
                userObj.put("label", configDTO.getLabel());
                userJsonArr.put(userObj);
            }
            Map<String, String> body = new HashMap<>();
            body.put("userControlData", userJsonArr.toString());
            String formData = Globals.mapToStringforFormData(body);
            Map<String, String> headers = new HashMap<>();
            headers.put("branch", Globals.headerBranch);
            String response = APIClient.postMultipartRequest(body, null, EndPoints.updateAppConfig, headers);
            JSONObject resObj = new JSONObject(response);

            if (resObj != null && resObj.getInt("responseStatus") == 200) {
                AlertUtility.AlertSuccess(AlertUtility.alertTypeSuccess, resObj.getString("message"), in -> {
                    Globals.getUserControlsAPICall();
                });
            } else {
                AlertUtility.AlertError(AlertUtility.alertTypeError, resObj.getString("message"), in -> {
                });
            }
        } catch (Exception e) {
            loggerUserController.error("Exception onSubmit() : " + Globals.getExceptionString(e));
        }

    }

    public void onSubmit(javafx.event.ActionEvent actionEvent) {
        AlertUtility.AlertConfirmation(AlertUtility.alertTypeConfirmation, "Are you Sure?", input -> {
            if (input == 1) submitAPICall();
        });


    }

    public void onCancel(javafx.event.ActionEvent actionEvent) {
        AlertUtility.AlertConfirmation(AlertUtility.alertTypeConfirmation, "Do you want to cancel?", input -> {
            if (input == 1) onLoadData();
        });

    }
}





