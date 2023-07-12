package com.scholarly.utme.network.model;

import com.google.gson.annotations.SerializedName;

public class User {
    private String id;
    @SerializedName("full_name")
    private String fullName;
    private String email;
    @SerializedName("phone_number")
    private String phoneNumber;
    private String country;
    @SerializedName("email_verified")
    private boolean emailVerified;
    @SerializedName("profile_pic_url")
    private String profilePicUrl;
    @SerializedName("referral_code")
    private String referralCode;
    private String gender;

    public User(String id, String fullName, String email, String phoneNumber, String country, boolean emailVerified, String profilePicUrl, String referralCode, String gender) {
        this.id = id;
        this.fullName = fullName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.country = country;
        this.emailVerified = emailVerified;
        this.profilePicUrl = profilePicUrl;
        this.referralCode = referralCode;
        this.gender = gender;
    }

    public String getId() {
        return id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public boolean isEmailVerified() {
        return emailVerified;
    }

    public void setEmailVerified(boolean emailVerified) {
        this.emailVerified = emailVerified;
    }

    public String getProfilePicUrl() {
        return profilePicUrl;
    }

    public void setProfilePicUrl(String profilePicUrl) {
        this.profilePicUrl = profilePicUrl;
    }

    public String getReferralCode() {
        return referralCode;
    }

    public void setReferralCode(String referralCode) {
        this.referralCode = referralCode;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }
}
