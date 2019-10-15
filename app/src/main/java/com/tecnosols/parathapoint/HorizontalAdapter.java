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

public class HorizontalAdapter extends RecyclerView.Adapter<HorizontalAdapter.ViewHolder> {
    private List<HorizontalModel> horizontalModelList;

    public HorizontalAdapter(List<HorizontalModel> horizontalModelList) {
        this.horizontalModelList = horizontalModelList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.horizontal_scroll_item_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        String resources = horizontalModelList.get(position).getImageResource();
        String itemname = horizontalModelList.get(position).getItemName();
        String itemprice = horizontalModelList.get(position).getItemPrice();

        viewHolder.setImages(resources);
        viewHolder.setNames(itemname);
        viewHolder.setPrices(itemprice);

    }

    @Override
    public int getItemCount() {
        return horizontalModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView image;
        private TextView name, price;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.gsProductImage);
            name = itemView.findViewById(R.id.gsProductTitle);
            price = itemView.findViewById(R.id.gsProductPrice);
        }

        private void setImages(String resources) {
            Picasso.get().load(resources).into(image);

        }

        private void setNames(String names) {
            name.setText(names);
        }

        private void setPrices(String prices) {
            price.setText("Rs."+prices);
        }

    }
}
