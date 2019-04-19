package com.romain.mathieu.go4lunch.model.request;

import android.media.Image;

import com.romain.mathieu.go4lunch.model.api.placeSearch.ResponseMap;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;

import static io.reactivex.android.schedulers.AndroidSchedulers.mainThread;

public class MapStreams {

    public static Observable<ResponseMap> streamFetchMap(String location, String radius, String type, String keyword, String key) {
        MapService mapService = MapService.retrofit.create(MapService.class);
        return mapService.getMap(location, radius, type, keyword, key)
                .subscribeOn(Schedulers.io())
                .observeOn(mainThread())
                .timeout(10, TimeUnit.SECONDS);
    }

    public static Observable<Image> streamFetchPhoto(String maxWidth, String photoReference, String key) {
        MapService mapService = MapService.retrofit.create(MapService.class);
        return mapService.getPhoto(maxWidth, photoReference, key)
                .subscribeOn(Schedulers.io())
                .observeOn(mainThread())
                .timeout(10, TimeUnit.SECONDS);

    }
}
