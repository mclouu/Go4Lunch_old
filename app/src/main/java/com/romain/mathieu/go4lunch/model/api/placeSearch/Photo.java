
package com.romain.mathieu.go4lunch.model.api.placeSearch;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Photo {


    @SerializedName("photo_reference")
    @Expose
    private String photoReference;


    public String getPhotoReference() {
        return photoReference;
    }

}
