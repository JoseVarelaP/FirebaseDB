package com.jv.firebasedb;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MyViewHolder extends RecyclerView.ViewHolder {
    TextView tvTitle, tvAuthor, tvComposer, tvAlbum, tvYear, tvCompany;

    public MyViewHolder(@NonNull View itemView) {
        super (itemView);

        tvTitle = itemView.findViewById (R.id.tvTitle);
        tvAuthor = itemView.findViewById (R.id.tvAuthor);
        tvComposer = itemView.findViewById (R.id.tvComponser);
        tvAlbum = itemView.findViewById (R.id.tvAlbum);
        tvYear = itemView.findViewById (R.id.tvYear);
        tvCompany = itemView.findViewById (R.id.tvCompany);
    }
}
