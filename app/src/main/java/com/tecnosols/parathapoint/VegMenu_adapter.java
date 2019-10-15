package com.tecnosols.parathapoint;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class VegMenu_adapter extends RecyclerView.Adapter<VegMenu_adapter.ViewHolder> {
    private List<VegMenu_model> vegMenuModelList;

    public VegMenu_adapter(List<VegMenu_model> vegMenuModelList) {
        this.vegMenuModelList = vegMenuModelList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.veg_menu_layout, parent, false);
        return new VegMenu_adapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String name=vegMenuModelList.get(position).getName();
        String price1=vegMenuModelList.get(position).getPrice_1piece();
        String price2=vegMenuModelList.get(position).getPrice_2piece();

        holder.setItemname(name);
        holder.setPrice1(price1);
        holder.setPrice2(price2);

    }

    @Override
    public int getItemCount() {
        return vegMenuModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView name,price1,price2;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name=itemView.findViewById(R.id.textView25itemname);
            price1=itemView.findViewById(R.id.textView28price1piece);
            price2=itemView.findViewById(R.id.textView29price2pcs);
        }
        private void setItemname(String name1){
            name.setText(name1);
        }
        private void setPrice1(String prices1){
            price1.setText("₹"+prices1);
        }
        private void setPrice2(String prices2){
            price2.setText("₹"+prices2);
        }
    }
}
