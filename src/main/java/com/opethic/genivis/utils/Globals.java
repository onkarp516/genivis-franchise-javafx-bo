package com.opethic.genivis.utils;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.opethic.genivis.GenivisApplication;
import com.opethic.genivis.dto.*;
import com.opethic.genivis.dto.account_entry.ContraListDTO;
import com.opethic.genivis.dto.account_entry.GVBankNameDTO;
import com.opethic.genivis.dto.reqres.OutletAppConfigRes;
import com.opethic.genivis.dto.account_entry.JournalListDTO;
import com.opethic.genivis.models.CompanyUserModel;
import com.opethic.genivis.models.UserRoleModel;
import com.opethic.genivis.models.master.ledger.*;
import com.opethic.genivis.network.APIClient;
import com.opethic.genivis.network.EndPoints;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TabPane;

import com.opethic.genivis.dto.FranchiseListDTO;
import com.opethic.genivis.dto.CompanyListDTO;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.http.HttpResponse;
import java.text.DecimalFormat;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

public class Globals {
    //    public static String baseUrl = "http://localhost:9092/";
//    public static String baseUrl = "http://3.109.189.155:9093/";
    public static String baseUrl = "http://localhost:9093/";
//        public static String baseUrl = "http://192.168.1.39:9093/";
    public static boolean isLoggedIn = false;
    public static final String APP_TITLE = "Genivis Remedies FR";
    public static final Duration TWO_MIN_API_TIMEOUT = Duration.ofSeconds(120);
    public static final Duration ONE_MIN_API_TIMEOUT = Duration.ofSeconds(60);
    // public static final Duration TWO_MIN_API_TIMEOUT = Duration.ofSeconds(120);
    public static TabPane tabPane = new TabPane();
    public static String headerBranch = "";
    public static String USER_TOKEN = "";

    public static DecimalFormat decimalFormat = new DecimalFormat("0.#");
    public static DecimalFormat twoDecimalFormat = new DecimalFormat("#.00");
    public static TabPane sadminMenuTabPen = new TabPane();
    public static TabPane cadminMenuTabPen = new TabPane();
    public static UserRoleModel userRoleModel;

    public static String purchaseOrderListSrc="C";

    public static String barcode_home_path="";
    public static String prn_file_name="";

    public static final ObservableList<OutletAppConfigDTO> lstOutletAppConfig = FXCollections.observableArrayList();

    private static final Logger loggerGlobals = LogManager.getLogger(Globals.class);
    /**
     * @ImplNote - ledger country code
     * @author kirankumar.gadagi
     */

    public final static String countryCode = "101";
    public final static String gvLedgerCode = "gvmh001";
    public final static String mhStateCode = "27";
    public final static String bPrintFileName = "300524.prn";
    public static int last_text_edit_size=0;

    public static String mapToString(Map<String, ?> map) {
        String mapAsString = map.keySet().stream()
                .map(key -> "\"" + key + "\":\"" + map.get(key) + "\"")
                .collect(Collectors.joining(",", "{", "}"));
        return mapAsString;
    }


    public static String mapToStringforFormData(Map<String, String> map) {
        StringBuilder stringBuilder = new StringBuilder();
        for (Map.Entry<String, String> entry : map.entrySet()) {
            if (stringBuilder.length() > 0) {
                stringBuilder.append("&");
            }
            stringBuilder.append(entry.getKey()).append("=").append(entry.getValue());
        }
        return stringBuilder.toString();
    }

    public static FranchiseListDTO franchiseListDTO;
    public static SalesOrderListDTO salesOrderListDTO;
    public static JournalListDTO journalListDTO;
    public static PurchaseOrderDTO purchaseOrderDTO;
    public static ContraListDTO contraListDTO;

    public static CompanyListDTO companyListDTO;
    public static SalesChallanDTO salesChallanDTO;
    public static CompanyUserModel companyUserModelDTO;    //for company user
    public static AreaHeadListDTO areaHeadListDTO;


    public static List<BalanceType> getAllBalanceType() {
        List<BalanceType> balanceTypes = new ArrayList<>();
        balanceTypes.add(new BalanceType(1, "Dr"));
        balanceTypes.add(new BalanceType(2, "Cr"));
        return balanceTypes;
    }

    public static List<TaxType> getAllTaxTypes() {
        List<TaxType> taxTypes = new ArrayList<>();
        taxTypes.add(new TaxType("central_tax", "Central Tax"));
        taxTypes.add(new TaxType("state_tax", "State Tax"));
        taxTypes.add(new TaxType("integrated_tax", "Integrated Tax"));
        return taxTypes;
    }

    public static List<YesNoOption> getYesNoOptions() {
        List<YesNoOption> yesNoOptions = new ArrayList<>();
        yesNoOptions.add(new YesNoOption(true, "Yes"));
        yesNoOptions.add(new YesNoOption(false, "No"));
        return yesNoOptions;
    }

    public static List<GSTType> getGstTypeList() {
        List<GSTType> gstTypes = new ArrayList<>();
        HttpResponse<String> response = APIClient.getRequest(EndPoints.getGstType);
        JSONObject res = new JSONObject(response.body());
        if (res.getInt("responseStatus") == 200) {
//            System.out.println("res =>" + res);
            JSONArray jsonArray = res.getJSONArray("responseObject");
            if (jsonArray.length() > 0) {
                for (Object object : jsonArray) {
                    JSONObject obj = new JSONObject(object.toString());
                    gstTypes.add(new GSTType(obj.getInt("id"), obj.getString("gstType")));
                }
            }
        }
        return gstTypes;
    }

    public static List<CommonOption> getPaymentMode() {
        List<CommonOption> CommonOption = new ArrayList<>();
        HttpResponse<String> response = APIClient.getRequest(EndPoints.GET_PAYMENT_MODE);
        JSONObject res = new JSONObject(response.body());
        if (res.getInt("responseStatus") == 200) {
            JSONArray jsonArray = res.getJSONArray("responseObject");
            if (jsonArray.length() > 0) {
                for (Object object : jsonArray) {
                    JSONObject obj = new JSONObject(object.toString());
                    CommonOption.add(new CommonOption(obj.getInt("id"), obj.getString("payment_mode")));
                }
            }
        }
        return CommonOption;
    }

    public static List<CommonStringOption> getGvBanknames() {
        List<CommonStringOption> gvBankNames = new ArrayList<>();
        HttpResponse<String> response = APIClient.getRequest(EndPoints.GET_GV_BANK_NAMES);
        JsonObject res = new Gson().fromJson(response.body(), JsonObject.class);
        if (res.get("responseStatus").getAsLong() == 200) {
            JsonArray jsonArray = res.get("list").getAsJsonArray();
            if (jsonArray.size() > 0) {
                for (JsonElement object : jsonArray) {
                    JsonObject mObject = object.getAsJsonObject();
                    gvBankNames.add(new CommonStringOption(mObject.get("bank_name").getAsString(),
                            mObject.get("account_no").getAsString()));
                }
            }
        }
        return gvBankNames;
    }


    public static List<StateOption> getIndiaStateList() {
        List<StateOption> lstStates = new ArrayList<>();
        HttpResponse<String> response = APIClient.getRequest(EndPoints.getIndianState);
        JSONObject res = new JSONObject(response.body());
        if (res.getInt("responseStatus") == 200) {
//            System.out.println("res =>" + res);
            JSONArray jsonArray = res.getJSONArray("responseObject");
//            System.out.println("jsonArray" + jsonArray);
            if (jsonArray.length() > 0) {
                for (Object object : jsonArray) {
                    JSONObject obj = new JSONObject(object.toString());
                    lstStates.add(new StateOption(obj.getInt("id"), obj.getString("stateName"), obj.getInt("stateCode")));
                }
            }
        }
        return lstStates;
    }

    public static List<LicenseType> getLicenseTypeList() {
        List<LicenseType> lstLicenseType = new ArrayList<>();
        lstLicenseType.add(new LicenseType("Aadhar No", 1, "aadhar_number"));
        lstLicenseType.add(new LicenseType("PAN No", 2, "pan_number"));
        lstLicenseType.add(new LicenseType("FSSAI No", 3, "fssai_number"));
        lstLicenseType.add(new LicenseType("Drug License No", 4, "drug_number"));
        lstLicenseType.add(new LicenseType("Mfg Licence No", 5, "mfg_number"));
        lstLicenseType.add(new LicenseType("Shop Act No", 6, "shop_act_number"));
        return lstLicenseType;
    }


    /**
     * @return the list of options which required to Applicable from
     * @author kirankumar.gadagi
     */
    public static List<CommonOption> getApplicableFromList() {
        List<CommonOption> lstApplicableFrom = new ArrayList<>();
        lstApplicableFrom.add(new CommonOption(1, "Credit Bill Date"));
        lstApplicableFrom.add(new CommonOption(2, "Lr Bill Date"));
        return lstApplicableFrom;
    }


    /**
     * @param e Accept the exception
     * @return the exception in string with stringwriter format
     * @author kirankumar.gadagi
     */
    public static String getExceptionString(Exception e) {
        StringWriter sw = new StringWriter();
        e.printStackTrace(new PrintWriter(sw));
        return sw.toString();
    }


    public static ImageView getPlusImage() {
        return new ImageView(new Image(String.valueOf(GenivisApplication.class.getResource("ui/assets/plus.png"))));
    }

    public static ImageView getDelImage() {
        return new ImageView(new Image(String.valueOf(GenivisApplication.class.getResource("ui/assets/del.png"))));
    }

    public static ImageView getEdtImage() {
        return new ImageView(new Image(String.valueOf(GenivisApplication.class.getResource("ui/assets/edt.png"))));
    }


    public static List<CommonStringOption> getFilterType() {
        List<CommonStringOption> lstCommonOpt = new ArrayList<>();
        lstCommonOpt.add(new CommonStringOption("Supplier", "Supplier"));
        lstCommonOpt.add(new CommonStringOption("Invoice", "Invoice"));
        return lstCommonOpt;
    }

    public static List<CommonStringOption> getDurationType() {
        List<CommonStringOption> lstCommonOpt = new ArrayList<>();
        lstCommonOpt.add(new CommonStringOption("This Month", "month"));
        lstCommonOpt.add(new CommonStringOption("Last Month", "lastMonth"));
        lstCommonOpt.add(new CommonStringOption("Half Year", "halfYear"));
        lstCommonOpt.add(new CommonStringOption("Full Year", "fullYear"));
        return lstCommonOpt;
    }


    public static void getUserControlsAPICall() {
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
            } else {
                // Handle error response
                String errorMessage = jsonObject.get("message").getAsString();
            }
        } catch (Exception e) {
            loggerGlobals.error("Exception onLoadData() : " + Globals.getExceptionString(e));
        }
    }

    public static Boolean getUserControlsWithSlug(String slug) {
        AtomicReference<Boolean> rtnValue = new AtomicReference<>(false);
        if (lstOutletAppConfig.size() == 0) {
            getUserControlsAPICall();
        }

        if (lstOutletAppConfig.size() > 0) {
            lstOutletAppConfig.stream().filter((s) -> s.getSlug().equalsIgnoreCase(slug)).findAny().ifPresent((o) -> {
                if (o.getValue() == 1) rtnValue.set(true);
            });
        }

        return rtnValue.get();
    }

    public static String getUserControlsNameWithSlug(String slug) {
        AtomicReference<String> rtnValue = new AtomicReference<>("");
        if (lstOutletAppConfig.size() == 0) {
            getUserControlsAPICall();
        }

        if (lstOutletAppConfig.size() > 0) {
            lstOutletAppConfig.stream().filter((s) -> s.getSlug().equalsIgnoreCase(slug)).findAny().ifPresent((o) -> {
                if (o.getValue() == 1) {
                    rtnValue.set(o.getLabel());
                }
            });
        }

        return rtnValue.get();
    }

    //TODO: open dropdown list onKey DOWN and SPACE
    public static void showDropDownOnKeyPress(ComboBox<?> comboBox) {
        comboBox.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.SPACE || e.getCode() == KeyCode.DOWN) {
                comboBox.show();
            }
        });
    }

    public static List<String> getAllTrades() {
//        return new ArrayList<String>("Retailer", "Distributor", "Manufacturer");
        List<String> lstAllTrades = new ArrayList<>();
        lstAllTrades.add("Retailer");
        lstAllTrades.add("Distributor");
        lstAllTrades.add("Manufacturer");
        return lstAllTrades;
    }
}
