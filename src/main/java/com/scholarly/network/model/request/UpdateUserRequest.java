package com.scholarly.network.model.request;

import com.google.gson.annotations.SerializedName;

public class UpdateUserRequest {
    @SerializedName("full_name")
    private String fullName;
    @SerializedName("phone_number")
    private String phone;
    private String gender;

    public UpdateUserRequest(String fullName, String phone, String gender) {
        this.fullName = fullName;
        this.phone = phone;
        this.gender = gender;
    }

    public String getFullName() {
        return fullName;
    }

    public String getPhone() {
        return phone;
    }

    public String getGender() {
        return gender;
    }
}
