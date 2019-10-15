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

public class myOrderAdapter extends RecyclerView.Adapter<myOrderAdapter.ViewHolder> {
    private List<myOrderModel> orderModels;

    public myOrderAdapter(List<myOrderModel> orderModels) {
        this.orderModels = orderModels;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_orders_layout, parent, false);
        return new myOrderAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String resources=orderModels.get(position).getImage_resource();
        String ordername=orderModels.get(position).getItem_name();
        String ordertime=orderModels.get(position).getOrder_time();
        String orderdate=orderModels.get(position).getOrder_date();
        String orderamount=orderModels.get(position).getTotal_amount();
        String orderquantity=orderModels.get(position).getQuantity();
        String paystatus=orderModels.get(position).getPayment_status();


        holder.setOrderName(ordername);
        holder.setOrderTime(ordertime);
        holder.setOrderDate(orderdate);
        holder.setOrderAmount(orderamount);
        holder.setOrderQuantity(orderquantity);
        holder.setOrderPayStatus(paystatus);
    }

    @Override
    public int getItemCount() {
        return orderModels.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private TextView order_name,order_time,order_date,order_amount,order_quantity,pay_status;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            order_name=itemView.findViewById(R.id.textView13_name);
            order_time=itemView.findViewById(R.id.textView18_order_time);
            order_date=itemView.findViewById(R.id.order_date);
            order_amount=itemView.findViewById(R.id.textView14_price);
            order_quantity=itemView.findViewById(R.id.textView20_quantity);
            pay_status=itemView.findViewById(R.id.textView25_paymentstatus);
        }

        private void setOrderPayStatus(String status){
            pay_status.setText(status);
        }
        private void setOrderName(String ordername){
            order_name.setText(ordername);
        }

        private void setOrderTime(String ordertime){
            order_time.setText("Order Time: "+ordertime);
        }

        private void setOrderDate(String orderdate){
            order_date.setText("Order Date: "+orderdate);
        }

        private void setOrderAmount(String orderamount){
            order_amount.setText("Rs."+orderamount);
        }

        private void setOrderQuantity(String orderquantity){
            order_quantity.setText("Qty: "+orderquantity);
        }
    }
}
