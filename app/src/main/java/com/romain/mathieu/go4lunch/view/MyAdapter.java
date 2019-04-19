/*
 * Created by Romain Mathieu => https://github.com/mclouu
 */

package com.romain.mathieu.go4lunch.view;

import android.content.Context;
import android.content.Intent;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.romain.mathieu.go4lunch.R;
import com.romain.mathieu.go4lunch.controller.activity.DetailsRestaurantActivity;
import com.romain.mathieu.go4lunch.model.CardData;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class MyAdapter extends RecyclerView.Adapter<MyAdapter.RestaurantsViewHolder> {


    private ArrayList<CardData> mdatas;
    private Context context;

    public MyAdapter(ArrayList<CardData> mlist) {
        this.mdatas = mlist;
    }

    @NonNull
    @Override
    public RestaurantsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_restaurant, parent, false);
        return new RestaurantsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RestaurantsViewHolder holder, int position) {
        final CardData object = mdatas.get(position);

        float rating = (float) object.getRating();

        holder.name.setText(Html.fromHtml(object.getName()));
        holder.adresse.setText(Html.fromHtml(object.getAdresse()));
        holder.horary.setText(Html.fromHtml(object.getHorary()));
        holder.distance.setText(Html.fromHtml(object.getDistance()));
        holder.people_txt.setText(Html.fromHtml(object.getNumberWorkmates()));
        holder.ratingBar.setRating(rating);


        String photoReference = object.getImageUrl();
        String key = "&key=AIzaSyBW10_Ie5wh-vwbEXEfWzk2zOFOQ_xfDWk";
        String url = "https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photoreference=" + photoReference + key;
        Picasso.get()
                .load(url)
                .centerCrop()
                .resize(100, 100)
                .error(R.drawable.imageempty)
                .into(holder.thumbnail);
    }


    @Override
    public int getItemCount() {
        if (mdatas != null) {
            return mdatas.size();
        }
        return 0;
    }

    class RestaurantsViewHolder extends RecyclerView.ViewHolder {


        @BindView(R.id.name)
        TextView name;
        @BindView(R.id.adresses)
        TextView adresse;
        @BindView(R.id.horary)
        TextView horary;
        @BindView(R.id.distance)
        TextView distance;
        @BindView(R.id.people_txt)
        TextView people_txt;
        @BindView(R.id.ratingBar)
        RatingBar ratingBar;
        @BindView(R.id.thumbnail)
        ImageView thumbnail;


        RestaurantsViewHolder(final View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            context = itemView.getContext();
        }

        @OnClick(R.id.relativeLayout)
        void submit() {
            int position = getAdapterPosition();
            Intent intent = new Intent(context, DetailsRestaurantActivity.class);
            final CardData object = mdatas.get(position);

            String photoReference = object.getImageUrl();
            String key = "&key=AIzaSyBW10_Ie5wh-vwbEXEfWzk2zOFOQ_xfDWk";
            String url = "https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photoreference=" + photoReference + key;

            intent.putExtra("urlimg", url);
            intent.putExtra("name", object.getName());
            intent.putExtra("adresse", object.getAdresse());
            context.startActivity(intent);

        }
    }
}