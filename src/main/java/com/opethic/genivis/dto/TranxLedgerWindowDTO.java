package com.opethic.genivis.dto;

import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;

public class TranxLedgerWindowDTO {

    private SimpleStringProperty index = new SimpleStringProperty("");
    private SimpleStringProperty id;
    private SimpleStringProperty code;
    private SimpleStringProperty unique_code;
    private SimpleStringProperty ledger_name;
    private SimpleStringProperty ledger_group;
    private SimpleStringProperty contact_number;
    private SimpleStringProperty current_balance;
    private SimpleStringProperty type;
    private SimpleStringProperty salesmanId;
    private SimpleStringProperty balancingMethod;
    private ObservableList<GstDetailsDTO> gstDetails;

    private String pendingQuotation;
    private String pendingOrders;
    private String pendingChallans;
    private String stateCode;

    private SimpleStringProperty balance_type;
    private String under_slug;

    public TranxLedgerWindowDTO() {
    }

    public String getUnder_slug() {
        return under_slug;
    }

    public void setUnder_slug(String under_slug) {
        this.under_slug = under_slug;
    }

    public ObservableList<GstDetailsDTO> getGstDetails() {
        return gstDetails;
    }

    public void setGstDetails(ObservableList<GstDetailsDTO> gstDetails) {
        this.gstDetails = gstDetails;
    }

    private SimpleStringProperty salesRate;

    public String getBalance_type() {
        return balance_type.get();
    }

    public SimpleStringProperty balance_typeProperty() {
        return balance_type;
    }

    public void setBalance_type(String balance_type) {
        this.balance_type = new SimpleStringProperty(balance_type);
    }


    public String getSalesRate() {
        return salesRate.get();
    }

    public SimpleStringProperty salesRateProperty() {
        return salesRate;
    }

    public void setSalesRate(String salesRate) {
        this.salesRate = new SimpleStringProperty(salesRate);
    }

    public TranxLedgerWindowDTO(String id, String code, String unique_code, String ledger_name, String ledger_group,
                                String contact_number, String current_balance, String type, String salesmanId,
                                String balancingMethod, String balance_type, ObservableList<GstDetailsDTO> gstDetails,
                                String salesRate) {
        this.id = new SimpleStringProperty(id);
        this.code = new SimpleStringProperty(code);
        this.unique_code = new SimpleStringProperty(unique_code);
        this.ledger_name = new SimpleStringProperty(ledger_name);
        this.ledger_group = new SimpleStringProperty(ledger_group);
        this.contact_number = new SimpleStringProperty(contact_number);
        this.current_balance = new SimpleStringProperty(current_balance);
        this.type = new SimpleStringProperty(type);
        this.salesmanId = new SimpleStringProperty(salesmanId);
        this.balancingMethod = new SimpleStringProperty(balancingMethod);
        this.balance_type = new SimpleStringProperty(balance_type);
        this.gstDetails = new SimpleListProperty<>(gstDetails);
        this.salesRate = new SimpleStringProperty(salesRate);
        this.pendingChallans = "";
        this.pendingOrders = "";
        this.pendingQuotation = "";
    }

    public TranxLedgerWindowDTO(SimpleStringProperty index, SimpleStringProperty id, SimpleStringProperty code, SimpleStringProperty unique_code, SimpleStringProperty ledger_name, SimpleStringProperty ledger_group, SimpleStringProperty contact_number, SimpleStringProperty current_balance, SimpleStringProperty type, SimpleStringProperty salesmanId, SimpleStringProperty balancingMethod, ObservableList<GstDetailsDTO> gstDetails, String pendingQuotation, String pendingOrders, String pendingChallans, String stateCode, SimpleStringProperty balance_type, String under_slug, SimpleStringProperty salesRate) {
        this.index = index;
        this.id = id;
        this.code = code;
        this.unique_code = unique_code;
        this.ledger_name = ledger_name;
        this.ledger_group = ledger_group;
        this.contact_number = contact_number;
        this.current_balance = current_balance;
        this.type = type;
        this.salesmanId = salesmanId;
        this.balancingMethod = balancingMethod;
        this.gstDetails = gstDetails;
        this.pendingQuotation = pendingQuotation;
        this.pendingOrders = pendingOrders;
        this.pendingChallans = pendingChallans;
        this.stateCode = stateCode;
        this.balance_type = balance_type;
        this.under_slug = under_slug;
        this.salesRate = salesRate;
    }

    public String getIndex() {
        return index.get();
    }

    public SimpleStringProperty indexProperty() {
        return index;
    }

    public void setIndex(String index) {
        this.index.set(index);
    }

    public String getId() {
        return id.get();
    }

    public SimpleStringProperty idProperty() {
        return id;
    }

    public void setId(String id) {
        this.id = new SimpleStringProperty(id);
    }

    public String getCode() {
        return code.get();
    }

    public SimpleStringProperty codeProperty() {
        return code;
    }

    public void setCode(String code) {
        this.code = new SimpleStringProperty(code);
    }

    public String getUnique_code() {
        return unique_code.get();
    }

    public SimpleStringProperty unique_codeProperty() {
        return unique_code;
    }

    public void setUnique_code(String unique_code) {
        this.unique_code = new SimpleStringProperty(unique_code);
    }

    public String getLedger_name() {
        return ledger_name.get();
    }

    public SimpleStringProperty ledger_nameProperty() {
        return ledger_name;
    }

    public void setLedger_name(String ledger_name) {
        this.ledger_name = new SimpleStringProperty(ledger_name);
    }

    public String getLedger_group() {
        return ledger_group.get();
    }

    public SimpleStringProperty ledger_groupProperty() {
        return ledger_group;
    }

    public void setLedger_group(String ledger_group) {
        this.ledger_group = new SimpleStringProperty(ledger_group);
    }

    public String getContact_number() {
        return contact_number.get();
    }

    public SimpleStringProperty contact_numberProperty() {
        return contact_number;
    }

    public void setContact_number(String contact_number) {
        this.contact_number = new SimpleStringProperty(contact_number);
    }

    public String getCurrent_balance() {
        return current_balance.get();
    }

    public SimpleStringProperty current_balanceProperty() {
        return current_balance;
    }

    public void setCurrent_balance(String current_balance) {
        this.current_balance = new SimpleStringProperty(current_balance);
    }

    public String getType() {
        return type.get();
    }

    public SimpleStringProperty typeProperty() {
        return type;
    }

    public void setType(String type) {
        this.type = new SimpleStringProperty(type);
    }


    public String getSalesmanId() {
        return salesmanId.get();
    }

    public SimpleStringProperty salesmanIdProperty() {
        return salesmanId;
    }

    public void setSalesmanId(String salesmanId) {
        this.salesmanId = new SimpleStringProperty(salesmanId);
    }

    public String getBalancingMethod() {
        return balancingMethod.get();
    }

    public SimpleStringProperty balancingMethodProperty() {
        return balancingMethod;
    }

    public void setBalancingMethod(String balancingMethod) {
        this.balancingMethod = new SimpleStringProperty(balancingMethod);
    }

    public String getStateCode() {
        return stateCode;
    }

    public void setStateCode(String stateCode) {
        this.stateCode = stateCode;
    }

    public String getPendingQuotation() {
        return pendingQuotation;
    }

    public void setPendingQuotation(String pendingQuotation) {
        this.pendingQuotation = pendingQuotation;
    }

    public String getPendingOrders() {
        return pendingOrders;
    }

    public void setPendingOrders(String pendingOrders) {
        this.pendingOrders = pendingOrders;
    }

    public String getPendingChallans() {
        return pendingChallans;
    }

    public void setPendingChallans(String pendingChallans) {
        this.pendingChallans = pendingChallans;
    }


    @Override
    public String toString() {
        return "TranxLedgerWindowDTO{" +
                "index=" + index +
                ", id=" + id +
                ", code=" + code +
                ", unique_code=" + unique_code +
                ", ledger_name=" + ledger_name +
                ", ledger_group=" + ledger_group +
                ", contact_number=" + contact_number +
                ", current_balance=" + current_balance +
                ", type=" + type +
                ", salesmanId=" + salesmanId +
                ", balancingMethod=" + balancingMethod +
                ", gstDetails=" + gstDetails +
                ", pendingQuotation='" + pendingQuotation + '\'' +
                ", pendingOrders='" + pendingOrders + '\'' +
                ", pendingChallans='" + pendingChallans + '\'' +
                ", stateCode='" + stateCode + '\'' +
                ", balance_type=" + balance_type +
                ", under_slug='" + under_slug + '\'' +
                ", salesRate=" + salesRate +
                '}';
    }
}
