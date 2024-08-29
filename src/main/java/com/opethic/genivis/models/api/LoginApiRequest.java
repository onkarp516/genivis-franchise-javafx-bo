package com.opethic.genivis.models.api;

import java.io.Serializable;

public class LoginApiRequest implements Serializable {
    private String franchiseCode;
    private String usercode;
    private String password;

    public String getFranchiseCode() {
        return franchiseCode;
    }

    public void setFranchiseCode(String franchiseCode) {
        this.franchiseCode = franchiseCode;
    }

    public String getUsercode() {
        return usercode;
    }

    public void setUsercode(String usercode) {
        this.usercode = usercode;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
