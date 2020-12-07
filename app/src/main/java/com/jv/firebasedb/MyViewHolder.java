package com.jv.firebasedb;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MyViewHolder extends RecyclerView.ViewHolder {
    TextView tvTitle, tvComposer, tvAlbum, tvYear, tvCompany;
    ImageView img;

    public MyViewHolder(@NonNull View itemView) {
        super (itemView);

        img = itemView.findViewById(R.id.imgset);
        tvTitle = itemView.findViewById (R.id.tvTitle);
        tvComposer = itemView.findViewById (R.id.tvComponser);
        tvAlbum = itemView.findViewById (R.id.tvAlbum);
        tvYear = itemView.findViewById (R.id.tvYear);
        tvCompany = itemView.findViewById (R.id.tvCompany);
    }
}
