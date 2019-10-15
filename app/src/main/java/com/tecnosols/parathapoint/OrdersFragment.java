package com.tecnosols.parathapoint;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class OrdersFragment extends Fragment {
    private RecyclerView recyclerViewOrders;
    FirebaseUser user;
    DatabaseReference dref;
    List<myOrderModel> orderModelList = new ArrayList<>();
    private ImageView goback;
    ProgressDialog pd;
    private ImageView gobacki;
    myOrderAdapter recyclerAdapter;


    public OrdersFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_orders, container, false);
        recyclerViewOrders = view.findViewById(R.id.recyclerViewOrders);
        gobacki = view.findViewById(R.id.imageView5backi);
        pd = new ProgressDialog(container.getContext());
        pd.setMessage("Loading Orders..!");
        pd.show();

        user = FirebaseAuth.getInstance().getCurrentUser();
        dref = FirebaseDatabase.getInstance().getReference().child("OrderDetails");

        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(container.getContext());
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);

        dref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                pd.cancel();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    myOrderModel mm = ds.getValue(myOrderModel.class);
                    if(mm.userid.equals(user.getUid())){
                        orderModelList.add(new myOrderModel(mm.item_name,mm.item_price,mm.quantity,mm.order_time,mm.order_date,mm.username,mm.userphone,mm.useraddress,mm.useremail,mm.total_amount,mm.userid,mm.payment_status));
                    }

                }
                Collections.reverse(orderModelList);
                recyclerAdapter = new myOrderAdapter(orderModelList);
                recyclerViewOrders.setLayoutManager(linearLayoutManager);
                recyclerViewOrders.setAdapter(recyclerAdapter);
                recyclerAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        gobacki.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                orderModelList.clear();
                recyclerAdapter.notifyDataSetChanged();
                Intent intent = new Intent(container.getContext(), MainActivity.class);
                startActivity(intent);

            }
        });

        return view;
    }

}
