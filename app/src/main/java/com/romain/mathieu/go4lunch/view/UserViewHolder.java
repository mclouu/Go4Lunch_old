package com.romain.mathieu.go4lunch.view;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.romain.mathieu.go4lunch.R;

import androidx.recyclerview.widget.RecyclerView;

public class UserViewHolder extends RecyclerView.ViewHolder {

    private View view;
    private Context context;

    public UserViewHolder(View itemView) {
        super(itemView);
        view = itemView;
        context = itemView.getContext();
    }

    public void setUserName(String userName) {
        TextView textView = view.findViewById(R.id.user_name);
        textView.setText(userName);
    }

    public void setChoice(String Choice) {
        TextView textView = view.findViewById(R.id.choice_restaurant);
        textView.setText(Choice);
    }

    public ImageView getPhoto() {
        return view.findViewById(R.id.user_photo);
    }

    public void setVisibilityRecyclerView(int visibility) {
        View rv = view.findViewById(R.id.relativeLayout_workmates);
        rv.setVisibility(visibility);
    }
}