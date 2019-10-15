package com.tecnosols.parathapoint;


import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class CartFragment extends Fragment {
    private static RecyclerView recyclerViewcart;
    private static DatabaseReference dref;
    FirebaseUser user;
    static List<cart_details> cartDetails = new ArrayList<>();
    LinearLayoutManager linearLayoutManager;
    private ImageView goback;
    ProgressDialog pd;
    static Integer total_price = 0;
    Integer GST = 0;
    static Integer FinalPrice;
    TextView amt, t_amt;
    static cartAdapter recyclerAdapter;
    private Button payment_dialog;

    private Dialog paymentMethodDialog;
    private ImageView paytm_payment, cod;
    private String user_phone, user_address;


    public CartFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_cart, container, false);
        recyclerViewcart = view.findViewById(R.id.recyclerView_cart);
        amt = view.findViewById(R.id.textView14_amount);
        t_amt = view.findViewById(R.id.textView18_total_amount);
        payment_dialog = view.findViewById(R.id.button2_confirmOrder);
        goback = view.findViewById(R.id.imageView5_goback);
        pd = new ProgressDialog(container.getContext());
        pd.setMessage("Loading Cart..!");
        pd.show();

        if (cartDetails.size() > 0) {
            cartDetails.clear();
        }


        user = FirebaseAuth.getInstance().getCurrentUser();
        String user_id = user.getUid();

        dref = FirebaseDatabase.getInstance().getReference().child("Cart_Details/" + user_id);

        paymentMethodDialog = new Dialog(container.getContext());
        paymentMethodDialog.setContentView(R.layout.payment_method);
        //paymentMethodDialog.getWindow().setBackgroundDrawable(getResources(R.drawable.round_corners));
        paymentMethodDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        paytm_payment = paymentMethodDialog.findViewById(R.id.imageView6_paytm);
        cod = paymentMethodDialog.findViewById(R.id.imageView5_cod);

        amt.setText(Integer.toString(0));
        t_amt.setText(Integer.toString(0));

        linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);

        if (dref != null) {

            dref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    pd.cancel();
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        cart_details crd = ds.getValue(cart_details.class);
                        cartDetails.add(new cart_details(crd.item_img_url, crd.getItemname(), crd.itemprice, crd.getData_key()));
                        Integer temp = Integer.parseInt(crd.itemprice);
                        total_price = total_price + temp;

                    }

                    recyclerAdapter = new cartAdapter(cartDetails);
                    recyclerViewcart.setLayoutManager(new LinearLayoutManager(getActivity()));
                    recyclerViewcart.setAdapter(recyclerAdapter);


                    recyclerAdapter.notifyDataSetChanged();

                    displayPrice();
                    Log.i("cartSize", Integer.toString(cartDetails.size()));


                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

            fetchData();

        }


        goback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(container.getContext(), MainActivity.class);
                cartDetails.clear();
                // recyclerAdapter.notifyDataSetChanged();
                total_price = 0;
                FinalPrice = 0;
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                CartFragment cartFragment = new CartFragment();
                HomeFragment homeFragment = new HomeFragment();
                //getFragmentManager().beginTransaction().remove(cartFragment).commit();
                getFragmentManager().beginTransaction().replace(R.id.container1, homeFragment).remove(cartFragment).commit();


                //startActivity(intent);


            }
        });

        payment_dialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                paymentMethodDialog.show();
            }
        });
        cod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submitOrderCOD();
            }
        });
        paytm_payment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(container.getContext(), "Only COD is available in cart right now.", Toast.LENGTH_SHORT).show();
            }
        });


        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    private void displayPrice() {
        FinalPrice = (((total_price * 18)) / 100) + total_price;
        amt.setText(Integer.toString(total_price));
        t_amt.setText(Integer.toString(FinalPrice));
    }

    public static void deleteData(int position) {
        //cartDetails.remove(position);

        String key = cartDetails.get(position).getData_key();
        cartDetails.clear();
        dref.child(key).removeValue();

        //recyclerAdapter.notifyDataSetChanged();
        cartDetails.clear();
        recyclerAdapter.notifyDataSetChanged();
        total_price = 0;
        FinalPrice = 0;


    }

    private void fetchData() {
        DatabaseReference dref1 = FirebaseDatabase.getInstance().getReference().child("user_details");

        dref1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //pd.cancel();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    user_details ud = ds.getValue(user_details.class);

                    if (user.getUid().contentEquals(ud.user_id)) {
                        user_phone = ud.user_mobile;
                        user_address = ud.user_address;

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                //pd.cancel();

            }
        });
    }

    private void submitOrderCOD() {

        final String payStatus_COD = "Cash On Delivery";
        String order_date = getCurrentDate();
        String order_time = getCurrentTime();

        String username = user.getDisplayName();
        String usermail = user.getEmail();
        String user_id = user.getUid();

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("OrderDetails");
        String id = databaseReference.push().getKey();
        if (user_phone != null && user_address != null) {
            for (int i = 0; i < cartDetails.size(); i++) {
                myOrderModel mom = new myOrderModel(cartDetails.get(i).getItemname(), cartDetails.get(i).getItemprice(), "1", order_time, order_date, username, user_phone, user_address, usermail, cartDetails.get(i).getItemprice(), user_id, payStatus_COD);

                databaseReference.child(id).setValue(mom).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isComplete()) {

                            /*if (finalI == cartDetails.size() - 1) {
                                Toast.makeText(getContext(), "Order Placed Successfully", Toast.LENGTH_LONG).show();
                                deleteCart();
                            }*/



                       /* new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                onBackPressed();
                                finish();
                            }
                        }, 1 * 1000);*/
                        } else {
                            Toast.makeText(getContext(), task.getException().toString(), Toast.LENGTH_LONG).show();
                        }
                    }
                });

            }
            Toast.makeText(getContext(), "Order Placed Successfully", Toast.LENGTH_LONG).show();
            deleteCart();
            Intent i1=new Intent(getContext(),MainActivity.class);
            startActivity(i1);


        } else {
            Toast.makeText(getContext(), "Please update your profile", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(getContext(), SignupActivity.class);
            startActivity(intent);
            //finish();
        }
    }

    public String getCurrentDate() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat mdformat = new SimpleDateFormat("dd / MM / yyyy ");
        String strDate = mdformat.format(calendar.getTime());
        //display(strDate);
        return strDate;

    }

    public String getCurrentTime() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat mdformat = new SimpleDateFormat("HH:mm:ss");
        String strTime = mdformat.format(calendar.getTime());
        return strTime;
    }

    private void deleteCart() {
        String user_id = user.getUid();

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Cart_Details/" + user_id);
        databaseReference.removeValue();
    }

}
