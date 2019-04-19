package com.romain.mathieu.go4lunch.controller.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.romain.mathieu.go4lunch.R;
import com.romain.mathieu.go4lunch.model.api.placeDetails.ResponseDetails;
import com.romain.mathieu.go4lunch.model.request.MapStreams;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class DetailsRestaurantActivity extends Activity {


    private static final int RC_CALL_PERMS = 330;
    private static final String PERMS = Manifest.permission.CALL_PHONE;

    @BindView(R.id.restaurant_image_view)
    ImageView img;
    @BindView(R.id.details_restaurant_name)
    TextView restaurantName;
    @BindView(R.id.details_retaurant_address)
    TextView restaurantAdresse;

    private Disposable disposable;
    String name, adresse, placeID, urlImg;
    private String phoneNumber, webSite, opening_hours;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_restaurant);
        ButterKnife.bind(this);

        Intent intent = getIntent();

        urlImg = intent.getStringExtra("urlimg");
        name = intent.getStringExtra("name");
        adresse = intent.getStringExtra("adresse");
        placeID = intent.getStringExtra("placeID");


        this.updateUi();
        this.executeHttpRequestWithRetrofit();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.disposeWhenDestroy();
    }

    public void updateUi() {
        restaurantName.setText(name);
        restaurantAdresse.setText(adresse);

        Glide.with(DetailsRestaurantActivity.this)
                .load(urlImg)
                .into(img);
    }

    //-----------------------------------||
    //                                   ||
    //          HTTP (RxJava)            ||
    //                                   ||
    //-----------------------------------||


    // 1 - Execute our Stream
    private void executeHttpRequestWithRetrofit() {

        String fields = "formatted_phone_number,website,opening_hours";
        String API_KEY = "AIzaSyBW10_Ie5wh-vwbEXEfWzk2zOFOQ_xfDWk";

        this.disposable = MapStreams.streamFetchDetails(placeID, fields, API_KEY).subscribeWith(
                new DisposableObserver<ResponseDetails>() {
                    @Override
                    public void onNext(ResponseDetails response) {
                        phoneNumber = response.getResult().getFormattedPhoneNumber();
                        webSite = response.getResult().getWebsite();
                        opening_hours = " ";
                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(getApplicationContext(), getString(R.string.error), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onComplete() {
                    }
                }
        );
    }

    // 5 - Dispose subscription
    private void disposeWhenDestroy() {
        if (this.disposable != null && !this.disposable.isDisposed()) this.disposable.dispose();
    }

    //-----------------------------------||
    //              ACTION               ||
    //-----------------------------------||

    @OnClick(R.id.btn_call)
    @AfterPermissionGranted(RC_CALL_PERMS)
    public void onClickButtonCall() {
        if (phoneNumber != null) {
            Log.e("tdb", phoneNumber);
            if (!EasyPermissions.hasPermissions(this, PERMS)) {
                EasyPermissions.requestPermissions(this, "", RC_CALL_PERMS, PERMS);
                return;
            }
            Intent callIntent = new Intent(Intent.ACTION_CALL);
            callIntent.setData(Uri.parse("tel:" + phoneNumber));
            startActivity(callIntent);

        } else {
            Toast.makeText(this, "Ce restaurant ne possède pas de numéro de téléphone", Toast.LENGTH_SHORT).show();
        }
    }

    @OnClick(R.id.btn_like)
    public void onClickButtonLike() {
        Log.e("tdb", opening_hours);
    }

    @OnClick(R.id.btn_website)
    public void onClickButtonWebsite() {
        if (webSite != null) {
            Log.e("tdb", webSite);
            Intent intent = new Intent(this, WebViewActivity.class);
            intent.putExtra("website", webSite);
            startActivity(intent);
        } else {
            Toast.makeText(this, "Ce restaurant ne possède pas de site internet", Toast.LENGTH_SHORT).show();
        }
    }
}
