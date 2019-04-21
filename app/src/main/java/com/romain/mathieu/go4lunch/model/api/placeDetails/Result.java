
package com.romain.mathieu.go4lunch.model.api.placeDetails;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Result {

    @SerializedName("formatted_address")
    @Expose
    private String formattedAddress;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("formatted_phone_number")
    @Expose
    private String formattedPhoneNumber;
    @SerializedName("opening_hours")
    @Expose
    private OpeningHours openingHours;
    @SerializedName("photos")
    @Expose
    private List<Photo> photos = null;
    @SerializedName("website")
    @Expose
    private String website;


    public String getFormattedAddress() {
        return formattedAddress;
    }

    public String getName() {
        return name;
    }

    public String getFormattedPhoneNumber() {
        return formattedPhoneNumber;
    }

    public OpeningHours getOpeningHours() {
        return openingHours;
    }

    public List<Photo> getPhotos() {
        return photos;
    }

    public String getWebsite() {
        return website;
    }


}
