package com.romain.mathieu.go4lunch.model.request;

import com.romain.mathieu.go4lunch.model.api.placeDetails.ResponseDetails;
import com.romain.mathieu.go4lunch.model.api.placeSearch.ResponseRestaurant;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;

import static io.reactivex.android.schedulers.AndroidSchedulers.mainThread;

public class MapStreams {

    public static Observable<ResponseRestaurant> streamFetchRestaurant(String location, String radius, String type, String keyword, String key) {
        MapService mapService = MapService.retrofit.create(MapService.class);
        return mapService.getRestaurant(location, radius, type, keyword, key)
                .subscribeOn(Schedulers.io())
                .observeOn(mainThread())
                .timeout(10, TimeUnit.SECONDS);
    }

    public static Observable<ResponseDetails> streamFetchDetails(String placeid, String fields, String key) {
        MapService mapService = MapService.retrofit.create(MapService.class);
        return mapService.getDetails(placeid, fields, key)
                .subscribeOn(Schedulers.io())
                .observeOn(mainThread())
                .timeout(10, TimeUnit.SECONDS);

    }
}
