package com.opethic.genivis.dto.Reports.Financials;

import javafx.beans.property.SimpleStringProperty;

public class TrialBalanceListDTO {
    private SimpleStringProperty id;
    private SimpleStringProperty foundations_name;
    private SimpleStringProperty principle_name;
    private SimpleStringProperty subprinciple_name;
    private SimpleStringProperty default_ledger;
    private SimpleStringProperty ledger_form_parameter_slug;
    private SimpleStringProperty unique_code;
    private SimpleStringProperty cr;
    private SimpleStringProperty dr;
    private SimpleStringProperty ledger_name;
    private SimpleStringProperty balancing_method;
    private SimpleStringProperty opening_balance;

    public TrialBalanceListDTO(String id, String foundations_name, String principle_name, String subprinciple_name, String default_ledger, String ledger_form_parameter_slug, String unique_code, String cr, String dr, String ledger_name, String balancing_method, String opening_balance) {
        this.id = new SimpleStringProperty( id);
        this.foundations_name = new SimpleStringProperty( foundations_name);
        this.principle_name = new SimpleStringProperty( principle_name);
        this.subprinciple_name = new SimpleStringProperty( subprinciple_name);
        this.default_ledger = new SimpleStringProperty( default_ledger);
        this.ledger_form_parameter_slug = new SimpleStringProperty( ledger_form_parameter_slug);
        this.unique_code = new SimpleStringProperty( unique_code);
        this.cr = new SimpleStringProperty( cr);
        this.dr = new SimpleStringProperty( dr);
        this.ledger_name = new SimpleStringProperty( ledger_name);
        this.balancing_method = new SimpleStringProperty( balancing_method);
        this.opening_balance = new SimpleStringProperty( opening_balance);
    }

    public String getId() {
        return id.get();
    }

    public SimpleStringProperty idProperty() {
        return id;
    }

    public void setId(String id) {
        this.id.set(id);
    }

    public String getFoundations_name() {
        return foundations_name.get();
    }

    public SimpleStringProperty foundations_nameProperty() {
        return foundations_name;
    }

    public void setFoundations_name(String foundations_name) {
        this.foundations_name.set(foundations_name);
    }

    public String getPrinciple_name() {
        return principle_name.get();
    }

    public SimpleStringProperty principle_nameProperty() {
        return principle_name;
    }

    public void setPrinciple_name(String principle_name) {
        this.principle_name.set(principle_name);
    }

    public String getSubprinciple_name() {
        return subprinciple_name.get();
    }

    public SimpleStringProperty subprinciple_nameProperty() {
        return subprinciple_name;
    }

    public void setSubprinciple_name(String subprinciple_name) {
        this.subprinciple_name.set(subprinciple_name);
    }

    public String getDefault_ledger() {
        return default_ledger.get();
    }

    public SimpleStringProperty default_ledgerProperty() {
        return default_ledger;
    }

    public void setDefault_ledger(String default_ledger) {
        this.default_ledger.set(default_ledger);
    }

    public String getLedger_form_parameter_slug() {
        return ledger_form_parameter_slug.get();
    }

    public SimpleStringProperty ledger_form_parameter_slugProperty() {
        return ledger_form_parameter_slug;
    }

    public void setLedger_form_parameter_slug(String ledger_form_parameter_slug) {
        this.ledger_form_parameter_slug.set(ledger_form_parameter_slug);
    }

    public String getUnique_code() {
        return unique_code.get();
    }

    public SimpleStringProperty unique_codeProperty() {
        return unique_code;
    }

    public void setUnique_code(String unique_code) {
        this.unique_code.set(unique_code);
    }

    public String getCr() {
        return cr.get();
    }

    public SimpleStringProperty crProperty() {
        return cr;
    }

    public void setCr(String cr) {
        this.cr.set(cr);
    }

    public String getDr() {
        return dr.get();
    }

    public SimpleStringProperty drProperty() {
        return dr;
    }

    public void setDr(String dr) {
        this.dr.set(dr);
    }

    public String getLedger_name() {
        return ledger_name.get();
    }

    public SimpleStringProperty ledger_nameProperty() {
        return ledger_name;
    }

    public void setLedger_name(String ledger_name) {
        this.ledger_name.set(ledger_name);
    }

    public String getBalancing_method() {
        return balancing_method.get();
    }

    public SimpleStringProperty balancing_methodProperty() {
        return balancing_method;
    }

    public void setBalancing_method(String balancing_method) {
        this.balancing_method.set(balancing_method);
    }

    public String getOpening_balance() {
        return opening_balance.get();
    }

    public SimpleStringProperty opening_balanceProperty() {
        return opening_balance;
    }

    public void setOpening_balance(String opening_balance) {
        this.opening_balance.set(opening_balance);
    }
}
