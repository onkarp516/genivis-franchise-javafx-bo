package com.opethic.genivis.utils;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.opethic.genivis.models.tranx.sales.*;
import com.opethic.genivis.network.APIClient;
import com.opethic.genivis.network.EndPoints;
import com.opethic.genivis.sql_lite.UserToken;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;

import java.net.http.HttpResponse;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.opethic.genivis.utils.Globals.decimalFormat;

public class GlobalTranx {

    private static ObservableList<UnitLevelLst> lstLevelsUnits = FXCollections.observableArrayList();
    public static final Integer configDecimalPlace = 2;

    public static TranxRow newEmptyTranxRow() {
        return new TranxRow(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, false, 0, "", 0, 0, null, 0, 0, 0, 0, 0, 0, null, "", 0, Arrays.asList(""));
    }

    public static TranxRowNw newEmptyTranxRowNw() {
        return new TranxRowNw("", "", "", "", null, null, 0, "", null, 0, "", null, 0, "", null, 0, "", null, "", "", 0, "", 0, "", "", "", 0, 0.0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, false, 0, "", 0, 0, null, 0, 0, 0, 0, 0, 0, null, "", 0, Arrays.asList(""), false, null);
    }

    public static UnitLevelLst getUnitsFromProductId(Integer product_id) {
        UnitLevelLst fnLevelAOpts = lstLevelsUnits.stream().filter(s -> s.getProductId() == product_id).findAny().orElse(null);
        if (fnLevelAOpts != null) {
            return fnLevelAOpts;
        } else {
            Map<String, String> body = new HashMap<>();
            body.put("product_id", String.valueOf(product_id));
            String formData = Globals.mapToStringforFormData(body);
            HttpResponse<String> response = APIClient.postFormDataRequest(formData, EndPoints.TRANX_SALES_GET_UNITS_FROM_PRODUCT);
            JsonObject jsonObject = new Gson().fromJson(response.body(), JsonObject.class);
            if (jsonObject.get("responseStatus").getAsInt() == 200) {
                JsonObject resObj = jsonObject.get("responseObject").getAsJsonObject();
                JsonArray lstPackages = resObj.get("lst_packages").getAsJsonArray();
                List<LevelAOpts> levelAOpts = new ArrayList<>();
                for (JsonElement lstPackage : lstPackages) {
                    LevelAOpts lstLevelAOpts = new Gson().fromJson(lstPackage, LevelAOpts.class);
                    System.out.println("lstLevelAOPts" + lstLevelAOpts);
                    levelAOpts.add(lstLevelAOpts);
                }
                UnitLevelLst unitLevelLst = new UnitLevelLst();
                unitLevelLst.setProductId(product_id);
                unitLevelLst.setLevelAOpts(levelAOpts);
                lstLevelsUnits.add(unitLevelLst);
                return unitLevelLst;
            }
        }
        return null;
    }


    public static TranxRtnCal TranxCalculation(List<TranxRow> rows, Boolean isDiscPer, Double ledgerDiscPer, Double ledgerDiscAmt, Double additionalChargesTotal) {
//        System.out.println("Global TranxCalculation");
//        System.out.println("rows ->" + rows);
//        System.out.println("ledgerDic Per -> " + ledgerDiscPer);
//        System.out.println("ledgerDic Amt -> " + ledgerDiscAmt);
//        System.out.println("additionalChargesTotal Amt -> " + additionalChargesTotal);


        List<TranxTaxCal> taxIgstCal = new ArrayList<>();
        List<TranxTaxCal> taxSgstCal = new ArrayList<>();
        List<TranxTaxCal> taxCgstCal = new ArrayList<>();

        Double totalBaseAmt = 0.0;
        Double totalDisAmt = 0.0;
        Double totalGrossAmt = 0.0;
        Double totalTaxAmt = 0.0;
        Double totalBillAmt = 0.0;
        Integer totalQty = 0;
        Double totalFreeQty = 0.0;

        for (TranxRow v : rows) {
            //! Calculate Base Amt
            if (v.getQty() > 0 && v.getRate() > 0) {
                Double baseAmt = v.getQty() * v.getRate();
                v.setBaseAmt(TranxRoundDigit(baseAmt, configDecimalPlace));
                v.setGrossAmt(TranxRoundDigit(baseAmt, configDecimalPlace));
                v.setTotalAmt(TranxRoundDigit(baseAmt, configDecimalPlace));
                v.setFinalAmt(TranxRoundDigit(baseAmt, configDecimalPlace));
            }

            //!Calculate Row level Discount
            if (v.getQty() > 0 && v.getRate() > 0) {
                Double baseAmt = v.getQty() * v.getRate();
                if (v.getDisAmt() > 0) {
                    baseAmt -= v.getDisAmt();
                    v.setGrossAmt(TranxRoundDigit(baseAmt, configDecimalPlace));
                    v.setTotalAmt(TranxRoundDigit(baseAmt, configDecimalPlace));
                    v.setFinalAmt(TranxRoundDigit(baseAmt, configDecimalPlace));
                    v.setDisAmtCal(v.getDisAmt());
                }

                if (v.getDisPer() > 0) {
                    Double grossAmt = v.getGrossAmt();
                    Double perAmt = TranxCalculatePer(grossAmt, v.getDisPer());
                    v.setDisPerCal(perAmt);
                    Double innTotalGrossAmt = TranxRoundDigit(grossAmt - perAmt, configDecimalPlace);
                    v.setGrossAmt(innTotalGrossAmt);
                    v.setGrossAmt1(innTotalGrossAmt);
                    v.setTotalAmt(innTotalGrossAmt);
                    v.setFinalAmt(innTotalGrossAmt);
                }
                if (v.getDisPer2() > 0) {
                    Double grossAmt = v.getGrossAmt();
                    Double perAmt = TranxCalculatePer(grossAmt, v.getDisPer2());
                    v.setDisPerCal(perAmt);
                    Double innTotalGrossAmt = TranxRoundDigit(grossAmt - perAmt, configDecimalPlace);
                    v.setGrossAmt(innTotalGrossAmt);
                    v.setGrossAmt1(innTotalGrossAmt);
                    v.setTotalAmt(innTotalGrossAmt);
                    v.setFinalAmt(innTotalGrossAmt);
                }
            }

            totalGrossAmt += v.getGrossAmt();
        }

        //! Invoice level Discount Amt & Percentage
        Double actDiscAmt = 0.0;
        Double actDiscPer = 0.0;
        if (isDiscPer == true) {
            actDiscAmt = GlobalTranx.TranxCalculatePer(totalGrossAmt, ledgerDiscPer);
            actDiscPer = ledgerDiscPer;
        } else {
            actDiscAmt = ledgerDiscAmt;
            actDiscPer = actDiscAmt * 100 / totalGrossAmt;
        }
        actDiscAmt = TranxRoundDigit(actDiscAmt, configDecimalPlace);
        actDiscPer = Double.isNaN(actDiscPer) ? 0.0 : actDiscPer;
        for (TranxRow v : rows) {
            Double invoiceDisAmt = GlobalTranx.TranxCalculateAddChrgVal(totalGrossAmt, actDiscAmt, v.getGrossAmt());
            Double totalAmt = TranxRoundDigit(v.getTotalAmt() - invoiceDisAmt, configDecimalPlace);
            v.setInvoiceDisAmt(invoiceDisAmt);
            v.setGrossAmt(totalAmt);
            v.setTotalAmt(totalAmt);
            v.setFinalAmt(totalAmt);
        }
        //!Addition Charges
        Double totalRowGrossAmt1 = 0.0;
        for (TranxRow v : rows) {
            if (additionalChargesTotal > 0 && v.getQty() > 0) {
                Double addChgAmt = GlobalTranx.TranxRoundDigit(GlobalTranx.TranxCalculateAddChrgVal(totalGrossAmt, additionalChargesTotal, v.getGrossAmt()), configDecimalPlace);
                Double actAmt = TranxRoundDigit(v.getGrossAmt() + addChgAmt, configDecimalPlace);
                v.setGrossAmt(actAmt);
                v.setGrossAmt1(actAmt);
                v.setTotalAmt(actAmt);
                v.setFinalAmt(actAmt);
                v.setAddChargesAmt(addChgAmt);
            } else {
                Double addChgAmt = 0.0;
                Double actAmt = TranxRoundDigit(v.getGrossAmt() + addChgAmt, configDecimalPlace);
                v.setGrossAmt(actAmt);
                v.setGrossAmt1(actAmt);
                v.setTotalAmt(actAmt);
                v.setFinalAmt(actAmt);
                v.setAddChargesAmt(addChgAmt);
            }
            totalRowGrossAmt1 += v.getGrossAmt1();
        }

        //! Invoice level Discount Amt & Percentage
        totalBaseAmt = 0.0;
        totalDisAmt = 0.0;
        totalGrossAmt = 0.0;
        totalTaxAmt = 0.0;
        totalBillAmt = 0.0;
        totalQty = 0;
        totalFreeQty = 0.0;
        //!Final Gst Calculations
        for (TranxRow v : rows) {
            //! Calculate tax
            v.setTotalIgst(TranxRoundDigit(TranxCalculatePer(v.getGrossAmt(), v.getIgst()), configDecimalPlace));
            v.setTotalCgst(TranxRoundDigit(TranxCalculatePer(v.getGrossAmt(), v.getCgst()), configDecimalPlace));
            v.setTotalSgst(TranxRoundDigit(TranxCalculatePer(v.getGrossAmt(), v.getSgst()), configDecimalPlace));
            v.setTotalAmt(TranxRoundDigit(v.getGrossAmt() + v.getTotalIgst(), configDecimalPlace));
            v.setFinalAmt(TranxRoundDigit(v.getGrossAmt() + v.getTotalIgst(), configDecimalPlace));

            totalBaseAmt += v.getBaseAmt();
            totalDisAmt += v.getDisAmtCal() + v.getDisPerCal();
            totalGrossAmt += v.getGrossAmt();
            totalTaxAmt += v.getTotalIgst();
            totalBillAmt += v.getFinalAmt();
            totalQty += v.getQty();
            totalFreeQty += v.getFreeqty();
            //! GST SUM of Same
            if (v.getIgst() > 0) {
                TranxTaxCal tranxTaxCal = taxIgstCal.stream().filter(fl -> fl.getTax() == v.getIgst()).findAny().orElse(null);
                if (tranxTaxCal != null) {
                    taxIgstCal.stream().filter(fl -> fl.getTax() == v.getIgst()).findAny().ifPresent((ob) -> ob.setTaxAmt(ob.getTaxAmt() + v.getTotalIgst()));
                } else {
                    TranxTaxCal nwTaxCal = new TranxTaxCal(v.getIgst(), v.getTotalIgst());
                    taxIgstCal.add(nwTaxCal);
                }
            }
            if (v.getCgst() > 0) {
                TranxTaxCal tranxTaxCal = taxCgstCal.stream().filter(fl -> fl.getTax() == v.getCgst()).findAny().orElse(null);
                if (tranxTaxCal != null) {
                    taxCgstCal.stream().filter(fl -> fl.getTax() == v.getCgst()).findAny().ifPresent((ob) -> ob.setTaxAmt(ob.getTaxAmt() + v.getTotalCgst()));
                } else {
                    TranxTaxCal nwTaxCal = new TranxTaxCal(v.getCgst(), v.getTotalCgst());
                    taxCgstCal.add(nwTaxCal);
                }
            }
            if (v.getSgst() > 0) {
                TranxTaxCal tranxTaxCal = taxSgstCal.stream().filter(fl -> fl.getTax() == v.getSgst()).findAny().orElse(null);
                if (tranxTaxCal != null) {
                    taxSgstCal.stream().filter(fl -> fl.getTax() == v.getSgst()).findAny().ifPresent((ob) -> ob.setTaxAmt(ob.getTaxAmt() + v.getTotalSgst()));
                } else {
                    TranxTaxCal nwTaxCal = new TranxTaxCal(v.getSgst(), v.getTotalSgst());
                    taxSgstCal.add(nwTaxCal);
                }
            }
        }
        //! Add rowlevel + bill-level discount sum
        totalDisAmt += actDiscAmt;
        totalBaseAmt = TranxRoundDigit(totalBaseAmt, configDecimalPlace);
        totalDisAmt = TranxRoundDigit(totalDisAmt, configDecimalPlace);
        totalGrossAmt = TranxRoundDigit(totalGrossAmt, configDecimalPlace);
        totalTaxAmt = TranxRoundDigit(totalTaxAmt, configDecimalPlace);
        totalBillAmt = TranxRoundDigit(totalBillAmt, configDecimalPlace);
        totalFreeQty = TranxRoundDigit(totalFreeQty, configDecimalPlace);
        TranxRtnCal rtnCal = new TranxRtnCal(rows, taxIgstCal, taxCgstCal, taxSgstCal, totalBaseAmt, totalDisAmt, totalGrossAmt, totalTaxAmt, totalBillAmt, totalQty, totalFreeQty, actDiscAmt, actDiscPer);
        return rtnCal;
    }


    public static double TranxCalculatePerReverse(Double amt, Double per) {
        return (per * amt) / (100 + per);
    }

    public static double TranxCalculatePerVal(Double finalAmt, Double discAmt, Double totalAmt) {
        return (discAmt * totalAmt) / finalAmt;
    }

    public static double TranxCalculateAddChrgVal(Double totalGrossAmt, Double discAmt, Double grossAmt) {
        return (discAmt * grossAmt) / totalGrossAmt;
    }


    public static Double TranxCalculatePer(Double grossAmt, double disPer) {
        return (disPer / 100) * grossAmt;
    }


    public static Double TranxRoundDigit(Double value, Integer decimalPlaces) {
        double factor = Math.pow(10, decimalPlaces);
        return Math.round(value * factor) / factor;
    }

    public static Integer getCompanyStateCode() {
        JsonObject userToken = UserToken.getAccessTokenDetails();
        Integer stateCode = 0;
        if (userToken.has("state")) {
            stateCode = Integer.valueOf(userToken.get("state").getAsString());
        }
        return stateCode;
    }

    public static String getUserName() {
        JsonObject userToken = UserToken.getAccessTokenDetails();
        String stateCode = "";
        if (userToken.has("fullName")) {
            stateCode = userToken.get("fullName").getAsString();
        }
        return stateCode;
    }

    public static String getUserCode() {
        JsonObject userToken = UserToken.getAccessTokenDetails();
        String userCode = "";
        if (userToken.has("sub")) {
            userCode = userToken.get("sub").getAsString();
        }
        return userCode;
    }

    public static void requestFocusOrDieTrying(Node node, Integer maxCnt) {
        Platform.runLater(() -> {
            if (!node.isFocused()) {
                node.requestFocus();
                if (maxCnt > 0) requestFocusOrDieTrying(node, maxCnt - 1);
            }
        });
    }

    //! Convert the calculation to 2 Decimal Points
    public static String setDecimalFromStr(double in) {
        return String.valueOf(TranxRoundDigit(in, configDecimalPlace));
    }

    public static String setQtyFormat(double in){
        return String.valueOf(decimalFormat.format(in));
    }

}

