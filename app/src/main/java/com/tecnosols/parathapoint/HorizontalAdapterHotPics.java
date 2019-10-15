package com.tecnosols.parathapoint;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class HorizontalAdapterHotPics extends RecyclerView.Adapter<HorizontalAdapterHotPics.ViewHolder> {
    private List<HorizontalModelHotPics> horizontalModelHotPics;

    public HorizontalAdapterHotPics(List<HorizontalModelHotPics> horizontalModelHotPics) {
        this.horizontalModelHotPics = horizontalModelHotPics;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.horizontal_scroll_hotpics_items, parent, false);
        return new HorizontalAdapterHotPics.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String resource=horizontalModelHotPics.get(position).getImageResoure();
        String title=horizontalModelHotPics.get(position).getImageTitle();

        holder.setImages(resource);
        holder.setNames(title);

    }

    @Override
    public int getItemCount() {
        return horizontalModelHotPics.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView image;
        private TextView name;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.hspImage);
            name = itemView.findViewById(R.id.hspProductTitle);
        }
        private void setImages(String resources) {
            Picasso.get().load(resources).into(image);

        }

        private void setNames(String names) {
            name.setText(names);
        }
    }

}
