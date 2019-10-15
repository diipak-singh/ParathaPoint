package com.tecnosols.parathapoint;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class DetailMenuAdapter extends RecyclerView.Adapter<DetailMenuAdapter.ViewHolder> {
    private List<DetailMenuModel> detailMenuModels;

    public DetailMenuAdapter(List<DetailMenuModel> detailMenuModels) {
        this.detailMenuModels = detailMenuModels;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.detail_menu_layout, parent, false);
        return new DetailMenuAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String name=detailMenuModels.get(position).getName();
        String price=detailMenuModels.get(position).getPrice();
        String description=detailMenuModels.get(position).getDescription();

        holder.setname(name);
        holder.setprice(price);
        holder.setdesc(description);

    }

    @Override
    public int getItemCount() {
        return detailMenuModels.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView name1,price1,description;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name1=itemView.findViewById(R.id.textView26_itemname_detail);
            price1=itemView.findViewById(R.id.textView26_itemprice_detail);
            description=itemView.findViewById(R.id.textView26_itemdescription_detail);
        }
        private void setname(String nam){
            name1.setText(nam);
        }
        private void setprice(String prce){
            price1.setText("₹"+prce);
        }
        private void setdesc(String dsc){
            description.setText(dsc);
        }
    }
}
