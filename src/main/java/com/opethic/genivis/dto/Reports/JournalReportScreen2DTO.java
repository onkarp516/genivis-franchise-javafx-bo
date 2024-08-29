package com.opethic.genivis.dto.Reports;

import javafx.beans.property.SimpleStringProperty;

public class JournalReportScreen2DTO {

    private SimpleStringProperty company_name;
    private SimpleStringProperty d_end_date;
    private SimpleStringProperty d_start_date;
    private SimpleStringProperty end_date;       //end date for the respective month
    private SimpleStringProperty start_date;       //start date for the respective month
    private SimpleStringProperty month;
    private SimpleStringProperty no_vouchers;
    private SimpleStringProperty total_amt;

    public JournalReportScreen2DTO(String month, String no_vouchers, String end_date, String total_amt, String start_date, String company_name) {
        this.month =new SimpleStringProperty(month);
        this.no_vouchers =new SimpleStringProperty(no_vouchers);
        this.end_date =new SimpleStringProperty(end_date);
        this.total_amt =new SimpleStringProperty(total_amt);
        this.start_date =new SimpleStringProperty(start_date);
        this.company_name =new SimpleStringProperty(company_name);
    }

    public String getCompany_name() {
        return company_name.get();
    }

    public SimpleStringProperty company_nameProperty() {
        return company_name;
    }

    public void setCompany_name(String company_name) {
        this.company_name.set(company_name);
    }

    public String getD_end_date() {
        return d_end_date.get();
    }

    public SimpleStringProperty d_end_dateProperty() {
        return d_end_date;
    }

    public void setD_end_date(String d_end_date) {
        this.d_end_date.set(d_end_date);
    }

    public String getD_start_date() {
        return d_start_date.get();
    }

    public SimpleStringProperty d_start_dateProperty() {
        return d_start_date;
    }

    public void setD_start_date(String d_start_date) {
        this.d_start_date.set(d_start_date);
    }

    public String getEnd_date() {
        return end_date.get();
    }

    public SimpleStringProperty end_dateProperty() {
        return end_date;
    }

    public void setEnd_date(String end_date) {
        this.end_date.set(end_date);
    }

    public String getStart_date() {
        return start_date.get();
    }

    public SimpleStringProperty start_dateProperty() {
        return start_date;
    }

    public void setStart_date(String start_date) {
        this.start_date.set(start_date);
    }

    public String getMonth() {
        return month.get();
    }

    public SimpleStringProperty monthProperty() {
        return month;
    }

    public void setMonth(String month) {
        this.month.set(month);
    }

    public String getNo_vouchers() {
        return no_vouchers.get();
    }

    public SimpleStringProperty no_vouchersProperty() {
        return no_vouchers;
    }

    public void setNo_vouchers(String no_vouchers) {
        this.no_vouchers.set(no_vouchers);
    }

    public String getTotal_amt() {
        return total_amt.get();
    }

    public SimpleStringProperty total_amtProperty() {
        return total_amt;
    }

    public void setTotal_amt(String total_amt) {
        this.total_amt.set(total_amt);
    }
}
