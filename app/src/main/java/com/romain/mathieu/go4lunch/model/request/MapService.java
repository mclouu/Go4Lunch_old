package com.romain.mathieu.go4lunch.model.request;

import com.facebook.stetho.okhttp3.StethoInterceptor;
import com.romain.mathieu.go4lunch.model.api.placeDetails.ResponseDetails;
import com.romain.mathieu.go4lunch.model.api.placeSearch.ResponseRestaurant;
import com.romain.mathieu.go4lunch.utils.MyConstant;

import io.reactivex.Observable;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface MapService {

//    MyConstant constant = new MyConstant();

    OkHttpClient okHttpClient = new OkHttpClient.Builder()
            .addNetworkInterceptor(new StethoInterceptor())
            .build();
    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(MyConstant.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build();

    @GET("nearbysearch/json?")
    Observable<ResponseRestaurant> getRestaurant(@Query("location") String location,
                                                 @Query("radius") String radius,
                                                 @Query("type") String type,
                                                 @Query("keyword") String keyword,
                                                 @Query("key") String key);


    @GET("details/json?")
    Observable<ResponseDetails> getDetails(@Query("placeid") String placeid,
                                           @Query("fields") String fields,
                                           @Query("key") String key);

}