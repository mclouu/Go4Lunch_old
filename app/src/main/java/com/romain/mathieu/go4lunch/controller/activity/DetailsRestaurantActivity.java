package com.romain.mathieu.go4lunch.controller.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.romain.mathieu.go4lunch.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailsRestaurantActivity extends Activity {
    String name, adresse, placeID, urlImg;

    @BindView(R.id.restaurant_image_view)
    ImageView img;
    @BindView(R.id.details_restaurant_name)
    TextView restaurantName;
    @BindView(R.id.details_retaurant_address)
    TextView restaurantAdresse;

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
    }

    public void updateUi() {
        restaurantName.setText(name);
        restaurantAdresse.setText(adresse);

        Glide.with(DetailsRestaurantActivity.this)
                .load(urlImg)
                .into(img);
    }
}
