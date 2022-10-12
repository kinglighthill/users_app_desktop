package com.scholarly.utme.network.model;

import com.google.gson.annotations.SerializedName;

public class Data {
    @SerializedName("id_token")
    private String idToken;
    private String refreshToken;
    private boolean isActivationActive;

    public Data() {

    }

    public Data(String idToken, String refreshToken, String token, boolean isActivationActive) {
        this.idToken = idToken;
        this.refreshToken = refreshToken;
        this.isActivationActive = isActivationActive;

    }

    public String getIdToken() {
        return idToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public boolean isActivationActive() {
        return isActivationActive;
    }


}
