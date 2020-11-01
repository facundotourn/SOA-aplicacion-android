package com.example.soa2020ea3.model;

import com.google.gson.annotations.SerializedName;

public class AuthTokens {
    private String token;

    @SerializedName("token_refresh")
    private String refreshToken;

    private Boolean success;

    public AuthTokens(String token, String refreshToken, Boolean success) {
        this.token = token;
        this.refreshToken = refreshToken;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }
}
