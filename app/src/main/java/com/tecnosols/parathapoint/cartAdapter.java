package com.tecnosols.parathapoint;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class cartAdapter extends RecyclerView.Adapter<cartAdapter.ViewHolder> {
    private List<cart_details> cartDetailsList;

    public cartAdapter(List<cart_details> cartDetailsList) {
        this.cartDetailsList = cartDetailsList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_layout, parent, false);
        return new cartAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String resource = cartDetailsList.get(position).getItem_img_url();
        String itemname = cartDetailsList.get(position).getItemname();
        String itemprice = cartDetailsList.get(position).getItemprice();
        String quantity=cartDetailsList.get(position).getQuantity();
        //Log.i("AgainQty",quantity);

        if (resource != null) {
            holder.setImages(resource);
        } else {
            holder.setImages(R.drawable.favicon);
        }

        holder.setNames(itemname);
        holder.setPrices(itemprice);
        holder.setQuantity1(quantity);
    }

    @Override
    public int getItemCount() {
        return cartDetailsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView image, img_delete;
        private TextView name, price,quantity;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.imageView5_pic);
            name = itemView.findViewById(R.id.textView6_itemname);
            price = itemView.findViewById(R.id.textView9_itemprice);
            img_delete = itemView.findViewById(R.id.imageView6_delete_item);
            quantity=itemView.findViewById(R.id.textViewcart_itemquantity);

            //img_delete.setOnClickListener(this);
            img_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    CartActivity ca = new CartActivity();
                    ca.deleteData(getAdapterPosition());
                    // CartActivity.deleteData(getAdapterPosition());
                    notifyDataSetChanged();
                }
            });

        }


        private void setImages(String resources) {
            Picasso.get().load(R.mipmap.ic_launcher).into(image);

        }

        private void setImages(int res) {
            Picasso.get().load(R.mipmap.ic_launcher).into(image);
        }

        private void setNames(String names) {
            name.setText(names);
        }

        private void setPrices(String prices) {
            price.setText("Rs. " + prices);
        }
        private void setQuantity1(String qty1)
        {
            quantity.setText("Qty:-"+qty1);

        }

        @Override
        public void onClick(View v) {
           /* CartActivity ca = new CartActivity();
            ca.deleteData(getAdapterPosition());
            // CartActivity.deleteData(getAdapterPosition());
            notifyDataSetChanged();*/

        }
    }


}
