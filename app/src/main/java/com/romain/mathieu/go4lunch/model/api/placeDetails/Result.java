
package com.romain.mathieu.go4lunch.model.api.placeDetails;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Result {

    @SerializedName("formatted_phone_number")
    @Expose
    private String formattedPhoneNumber;
    @SerializedName("opening_hours")
    @Expose
    private OpeningHours openingHours;
    @SerializedName("website")
    @Expose
    private String website;

    public String getFormattedPhoneNumber() {
        return formattedPhoneNumber;
    }

    public OpeningHours getOpeningHours() {
        return openingHours;
    }

    public String getWebsite() {
        return website;
    }


}
