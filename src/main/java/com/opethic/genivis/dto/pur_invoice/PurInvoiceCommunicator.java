package com.opethic.genivis.dto.pur_invoice;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.List;

public class PurInvoiceCommunicator  {

    public static String ledger_id = "";
    public static String emptyUnitStockId = "";

    public static List<ObservableList<LevelAForPurInvoice>> levelAForPurInvoiceObservableList = new ArrayList<>();
    public static List<ObservableList<LevelBForPurInvoice>> levelBForPurInvoiceObservableList = new ArrayList<>();
    public static List<ObservableList<LevelCForPurInvoice>> levelCForPurInvoiceObservableList = new ArrayList<>();
    public static List<ObservableList<UnitForPurInvoice>> unitForPurInvoiceList = new ArrayList<>();

    // Method to reset all fields
    public static void resetFields() {
        ledger_id = "";
        levelAForPurInvoiceObservableList.clear();
        levelBForPurInvoiceObservableList.clear();
        levelCForPurInvoiceObservableList.clear();
        unitForPurInvoiceList.clear();
        unitForPurInvoiceList = FXCollections.observableArrayList(); // Initialize the list as empty ObservableList
    }

}
