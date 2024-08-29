module com.opethic.genivis {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.dlsc.formsfx;
    requires gson;
    requires java.net.http;
    requires java.sql;
    requires lombok;
    requires org.apache.httpcomponents.httpclient;
    requires org.apache.httpcomponents.httpcore;
    requires httpmime;
    requires java.desktop;
    requires org.apache.logging.log4j;
    requires org.apache.logging.log4j.core;
    requires org.slf4j;
    requires org.controlsfx.controls;
    requires org.json;
    requires org.apache.poi.poi;
    requires org.apache.poi.ooxml;
    requires javafx.web;

//    export com.sun.javafx.event to org.controlsfx.controls;
    opens com.opethic.genivis.controller to javafx.fxml;
    opens com.opethic.genivis.models to javafx.base;
    opens com.opethic.genivis.controller.tranx_purchase to javafx.fxml;
    opens com.opethic.genivis.controller.account_entry to javafx.fxml;
    opens com.opethic.genivis.controller.Reports to javafx.fxml;
    opens com.opethic.genivis.controller.Reports.Transactions to javafx.fxml;
    opens com.opethic.genivis.controller.Reports.Financials to javafx.fxml;
    opens com.opethic.genivis.controller.GSTR3B to javafx.fxml;
    opens com.opethic.genivis.controller.tranx_sales to javafx.fxml;
    opens com.opethic.genivis.controller.utilities to javafx.fxml;
    opens com.opethic.genivis.controller.GSTR1 to javafx.fxml;
    opens com.opethic.genivis.controller.GSTR2 to javafx.fxml;

    opens com.opethic.genivis.dto;
    opens com.opethic.genivis.controller.dialogs to javafx.fxml;
    opens com.opethic.genivis.models.api;

    exports com.opethic.genivis;
    exports com.opethic.genivis.controller.master;
    exports com.opethic.genivis.controller.tranx_purchase;
    exports com.opethic.genivis.controller.account_entry;
    exports com.opethic.genivis.controller.Reports;
    exports com.opethic.genivis.controller.GSTR3B;
    exports com.opethic.genivis.controller.tranx_sales;
    exports com.opethic.genivis.controller.GSTR1;
    exports com.opethic.genivis.controller.GSTR2;
    exports com.opethic.genivis.controller.dialogs;
    exports com.opethic.genivis.controller.utilities;
    exports com.opethic.genivis.controller.Reports.Transactions;
    exports com.opethic.genivis.controller.Reports.Financials;


    exports com.opethic.genivis.models.api;
    exports com.opethic.genivis.controller.commons;
    exports com.opethic.genivis.dto.reqres;
    opens com.opethic.genivis.dto.reqres;
    exports com.opethic.genivis.dto.reqres.product;
    exports com.opethic.genivis.dto.reqres.payment;
    opens com.opethic.genivis.dto.reqres.payment;
    exports com.opethic.genivis.dto.reqres.receipt;
    opens com.opethic.genivis.dto.reqres.receipt;
    exports com.opethic.genivis.dto.reqres.creditNote;
    opens com.opethic.genivis.dto.reqres.creditNote;
    exports com.opethic.genivis.dto.reqres.journal;
    opens com.opethic.genivis.dto.reqres.journal;
    exports com.opethic.genivis.dto.reqres.debitNote;
    opens com.opethic.genivis.dto.reqres.debitNote;
    opens com.opethic.genivis.dto.reqres.product;
    exports com.opethic.genivis.dto.reqres.catalog;
    opens com.opethic.genivis.dto.reqres.catalog;
    opens com.opethic.genivis.controller.master to javafx.fxml;
    opens com.opethic.genivis.controller.users to javafx.fxml;
    exports com.opethic.genivis.dto.reqres.pur_tranx;
    opens com.opethic.genivis.dto.reqres.pur_tranx;
    exports com.opethic.genivis.dto;
    exports com.opethic.genivis.dto.Reports;
    opens com.opethic.genivis.dto.Reports;
    //?Ledger Related info
    exports com.opethic.genivis.models.master.ledger;
    opens com.opethic.genivis.models.master.ledger;
    exports com.opethic.genivis.dto.master.ledger;
    exports com.opethic.genivis.controller.master.ledger;
    exports com.opethic.genivis.controller.master.ledger.create;
    exports com.opethic.genivis.controller.master.ledger.edit;
    opens com.opethic.genivis.dto.master.ledger;
    opens com.opethic.genivis.controller.master.ledger to javafx.fxml;
    opens com.opethic.genivis.controller.master.ledger.create to javafx.fxml;
    opens com.opethic.genivis.controller.master.ledger.edit to javafx.fxml;
    exports com.opethic.genivis.dto.reqres.sales_tranx;
    opens com.opethic.genivis.dto.reqres.sales_tranx;
    //?Sales invoice
    opens com.opethic.genivis.controller.tranx_sales.invoice to javafx.fxml;

    //?Sales Invoice DTO
    exports com.opethic.genivis.dto.reqres.tranx.sales.invoice;
    opens com.opethic.genivis.dto.reqres.tranx.sales.invoice;

    //?Sales Invoice Models
    exports com.opethic.genivis.models.tranx.sales;
    opens com.opethic.genivis.models.tranx.sales;


    //?Sales invoice return
    exports com.opethic.genivis.controller.tranx_sales.rtn to javafx.fxml;
    opens com.opethic.genivis.controller.tranx_sales.rtn to javafx.fxml;
    //?GSTR
    exports com.opethic.genivis.dto.GSTR1;
    opens com.opethic.genivis.dto.GSTR1;

    exports com.opethic.genivis.dto.GSTR2;
    opens com.opethic.genivis.dto.GSTR2;


    exports com.opethic.genivis.dto.pur_invoice.reqres;
    opens com.opethic.genivis.dto.pur_invoice.reqres;

    exports com.opethic.genivis.dto.pur_invoice;
    opens com.opethic.genivis.dto.pur_invoice;

    exports com.opethic.genivis.dto.cmp_t_row;
    opens com.opethic.genivis.dto.cmp_t_row;
    //Account Entry
    exports com.opethic.genivis.dto.account_entry;
    opens com.opethic.genivis.dto.account_entry;

    exports com.opethic.genivis.controller.dashboard;
    opens com.opethic.genivis.controller.dashboard;

    exports com.opethic.genivis.dto.counter;
    opens com.opethic.genivis.dto.counter;

    exports com.opethic.genivis.dto.dashboard;
    opens com.opethic.genivis.dto.dashboard;

    exports com.opethic.genivis.controller.franchise_dashboard;
    opens com.opethic.genivis.controller.franchise_dashboard;

    exports com.opethic.genivis.dto.franchise_dashboard;
    opens com.opethic.genivis.dto.franchise_dashboard;

    exports com.opethic.genivis.dto.Reports.Financials;
    opens com.opethic.genivis.dto.Reports.Financials;

    exports com.opethic.genivis.dto.utilities;
    opens com.opethic.genivis.dto.utilities;
    exports com.opethic.genivis.controller;
    exports com.opethic.genivis.controller.master.ledger.common;
    opens com.opethic.genivis.controller.master.ledger.common to javafx.fxml;


    exports com.opethic.genivis.dto.opening;
    opens com.opethic.genivis.dto.opening;

}