package com.romain.mathieu.go4lunch.model;

public class DetailsData {

    private String phoneNumber, webSite, opening_hours;

    public DetailsData(String phoneNumber, String webSite, String opening_hours) {
        this.phoneNumber = phoneNumber;
        this.webSite = webSite;
        this.opening_hours = opening_hours;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getWebSite() {
        return webSite;
    }

    public String getOpening_hours() {
        return opening_hours;
    }
}
