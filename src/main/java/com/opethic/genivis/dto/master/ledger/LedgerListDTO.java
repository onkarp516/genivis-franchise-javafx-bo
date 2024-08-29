package com.opethic.genivis.dto.master.ledger;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class LedgerListDTO {
    public SimpleIntegerProperty id;
    public SimpleStringProperty foundations_name;
    public SimpleStringProperty principle_name;
    public SimpleStringProperty subprinciple_name;
    public SimpleBooleanProperty default_ledger;
    public SimpleStringProperty ledger_form_parameter_slug;
    public SimpleStringProperty ledger_name;
    public SimpleDoubleProperty cr;

    public int getId() {
        return id.get();
    }

    public SimpleIntegerProperty idProperty() {
        return id;
    }

    public void setId(int id) {
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

    public boolean isDefault_ledger() {
        return default_ledger.get();
    }

    public SimpleBooleanProperty default_ledgerProperty() {
        return default_ledger;
    }

    public void setDefault_ledger(boolean default_ledger) {
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

    public String getLedger_name() {
        return ledger_name.get();
    }

    public SimpleStringProperty ledger_nameProperty() {
        return ledger_name;
    }

    public void setLedger_name(String ledger_name) {
        this.ledger_name.set(ledger_name);
    }

    public double getCr() {
        return cr.get();
    }

    public SimpleDoubleProperty crProperty() {
        return cr;
    }

    public void setCr(double cr) {
        this.cr.set(cr);
    }

    public double getDr() {
        return dr.get();
    }

    public SimpleDoubleProperty drProperty() {
        return dr;
    }

    public void setDr(double dr) {
        this.dr.set(dr);
    }

    public SimpleDoubleProperty dr;

    public LedgerListDTO(Integer id, String foundations_name, String principle_name, String subprinciple_name, boolean default_ledger, String ledger_form_parameter_slug, String ledger_name, double cr, double dr) {
        this.id = new SimpleIntegerProperty(id);
        this.foundations_name = new SimpleStringProperty(foundations_name);
        this.principle_name = new SimpleStringProperty(principle_name);
        this.subprinciple_name = new SimpleStringProperty(subprinciple_name);
        this.default_ledger = new SimpleBooleanProperty(default_ledger);
        this.ledger_form_parameter_slug = new SimpleStringProperty(ledger_form_parameter_slug);
        this.ledger_name = new SimpleStringProperty(ledger_name);
        this.cr = new SimpleDoubleProperty(cr);
        this.dr = new SimpleDoubleProperty(dr);

    }

    @Override
    public String toString() {
        return " id => " + this.id + "principle => " + this.principle_name + "subprinciple =>" + this.subprinciple_name + "foundations_name => " + this.foundations_name;
    }
}
