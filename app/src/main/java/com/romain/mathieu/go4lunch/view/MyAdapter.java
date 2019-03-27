/*
 * Created by Romain Mathieu => https://github.com/mclouu
 */

package com.romain.mathieu.go4lunch.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.romain.mathieu.go4lunch.R;
import com.romain.mathieu.go4lunch.model.CardData;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ArticleViewHolder> {

    private Context context;
    private ArrayList<CardData> mdatas;

    public MyAdapter(ArrayList<CardData> mlist) {
        this.mdatas = mlist;
    }

    @NonNull
    @Override
    public ArticleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item, parent, false);
        return new ArticleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ArticleViewHolder holder, int position) {
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
                .placeholder(R.drawable.imagedownloading)
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

    class ArticleViewHolder extends RecyclerView.ViewHolder {


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


        ArticleViewHolder(final View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            context = itemView.getContext();
        }

        @OnClick(R.id.relativeLayout)
        void submit() {
            int position = getAdapterPosition();
        }
    }
}