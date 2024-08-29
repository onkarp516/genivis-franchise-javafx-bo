package com.opethic.genivis.models.master.ledger;

public class BalancingMethods {
    private int balancing_id;
    private String balance_method;

    public BalancingMethods(int balancing_id, String balance_method) {
        this.balancing_id = balancing_id;
        this.balance_method = balance_method;
    }

    public int getBalancing_id() {
        return balancing_id;
    }

    public void setBalancing_id(int balancing_id) {
        this.balancing_id = balancing_id;
    }

    public String getBalance_method() {
        return balance_method;
    }

    public void setBalance_method(String balance_method) {
        this.balance_method = balance_method;
    }
}
