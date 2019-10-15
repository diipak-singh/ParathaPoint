package com.tecnosols.parathapoint;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
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

public class MenuActivity_Veg extends AppCompatActivity {
    private RecyclerView recyclerView;
    List<VegMenu_model> vegMenu_models = new ArrayList<>();
    List<DetailMenuModel> detailMenu = new ArrayList<>();
    ProgressDialog pd;
    DatabaseReference dref;
    VegMenu_adapter recyclerAdapter;
    DetailMenuAdapter recyclerAdapter2;
    private ImageView go_back;
    private TextView header1, header2, header3;

    private Dialog paymentMethodDialog;
    private ImageView paytm_payment, cod;
    FirebaseUser user;
    DatabaseReference databaseReference;
    private final String upiID = "pallavikumar0209@okaxis";
    private final String note = "Paying to ParathaPoint";
    private final String name = "ParathaPoint";
    final int UPI_PAYMENT = 0;

    private String itemname, itemprice;
    private TextView pay_itemname, pay_itemprice;
    String user_phone,user_address;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu__veg);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView_vegMenu);
        go_back = (ImageView) findViewById(R.id.imageView11bacK);
        header1 = (TextView) findViewById(R.id.textView25veg);
        header2 = (TextView) findViewById(R.id.textView29veg);
        header3 = (TextView) findViewById(R.id.textView28veg);

        pd = new ProgressDialog(MenuActivity_Veg.this);
        pd.setMessage("Loading ..!");
        pd.show();

        final Intent intent = getIntent();
        String flag_check = intent.getStringExtra("flag");

        databaseReference = FirebaseDatabase.getInstance().getReference().child("user_details");
        user = FirebaseAuth.getInstance().getCurrentUser();
        fetchData();

        paymentMethodDialog = new Dialog(MenuActivity_Veg.this);
        paymentMethodDialog.setContentView(R.layout.payment_method_detail);
        paymentMethodDialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.round_corners));
        paymentMethodDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        paytm_payment = paymentMethodDialog.findViewById(R.id.imageView6_paytm);
        cod = paymentMethodDialog.findViewById(R.id.imageView5_cod);
        pay_itemname = paymentMethodDialog.findViewById(R.id.textView25_paymentitemname);
        pay_itemprice = paymentMethodDialog.findViewById(R.id.textView26_paymentitemprice);

        if (flag_check.contentEquals("veg_card")) {
            loadVegData();
        } else if (flag_check.contentEquals("pizza_card")) {
            loadPizzaData();
        } else if (flag_check.contentEquals("combo_card")) {
            loadComboData();
        } else if (flag_check.contentEquals("platter_card")) {
            loadPlatterData();
        } else if (flag_check.contentEquals("meal_card")) {
            loadMealData();
        } else {
            loadNonVegData();
        }

        paytm_payment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                payUsingUPI(itemprice, upiID, name, note);
            }
        });

        cod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submitOrderCOD();
            }
        });


        go_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
                finish();
            }
        });
    }

    private void loadVegData() {
        dref = FirebaseDatabase.getInstance().getReference().child("Menu/veg");
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);

        dref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                pd.cancel();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    VegMenu_model vmm = ds.getValue(VegMenu_model.class);
                    vegMenu_models.add(new VegMenu_model(vmm.name, vmm.price_1piece, vmm.price_2piece));
                }
                recyclerAdapter = new VegMenu_adapter(vegMenu_models);
                recyclerView.setLayoutManager(linearLayoutManager);
                recyclerView.setAdapter(recyclerAdapter);
                recyclerAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getApplicationContext(), recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                String itemname = vegMenu_models.get(position).getName();
                String price1 = vegMenu_models.get(position).getPrice_1piece();
                String price2 = vegMenu_models.get(position).getPrice_2piece();
                String flag_check="1";
                Intent intent = new Intent(getApplicationContext(), MenuOrderActivity.class);
                intent.putExtra("name", itemname);
                intent.putExtra("price1", price1);
                intent.putExtra("price2", price2);
                intent.putExtra("flag",flag_check);
                startActivity(intent);

            }

            @Override
            public void onLongItemClick(View view, int position) {

            }
        }));

    }

    private void loadNonVegData() {
        dref = FirebaseDatabase.getInstance().getReference().child("Menu/non_veg");
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);

        dref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                pd.cancel();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    VegMenu_model vmm = ds.getValue(VegMenu_model.class);
                    vegMenu_models.add(new VegMenu_model(vmm.name, vmm.price_1piece, vmm.price_2piece));
                }
                recyclerAdapter = new VegMenu_adapter(vegMenu_models);
                recyclerView.setLayoutManager(linearLayoutManager);
                recyclerView.setAdapter(recyclerAdapter);
                recyclerAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getApplicationContext(), recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                String itemname = vegMenu_models.get(position).getName();
                String price1 = vegMenu_models.get(position).getPrice_1piece();
                String price2 = vegMenu_models.get(position).getPrice_2piece();
                String flag_check="1";
                Intent intent = new Intent(getApplicationContext(), MenuOrderActivity.class);
                intent.putExtra("name", itemname);
                intent.putExtra("price1", price1);
                intent.putExtra("price2", price2);
                intent.putExtra("flag",flag_check);
                startActivity(intent);

            }

            @Override
            public void onLongItemClick(View view, int position) {

            }
        }));
    }

    private void loadPizzaData() {
        header1.setText("Paratha Pizza");
        header2.setVisibility(View.INVISIBLE);
        header3.setVisibility(View.INVISIBLE);

        dref = FirebaseDatabase.getInstance().getReference().child("Menu/pizzas");
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);

        dref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                pd.cancel();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    DetailMenuModel dmm = ds.getValue(DetailMenuModel.class);
                    detailMenu.add(new DetailMenuModel(dmm.name, dmm.price, dmm.description));
                }
                recyclerAdapter2 = new DetailMenuAdapter(detailMenu);
                recyclerView.setLayoutManager(linearLayoutManager);
                recyclerView.setAdapter(recyclerAdapter2);
                recyclerAdapter2.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getApplicationContext(), recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                /*itemname = detailMenu.get(position).getName();
                itemprice = detailMenu.get(position).getPrice();
                showDialog(itemname, itemprice);*/
                String itemname = detailMenu.get(position).getName();
                String price1 = detailMenu.get(position).getPrice();
                String price2 = "0";
                String flag_check="0";
                Intent intent = new Intent(getApplicationContext(), MenuOrderActivity.class);
                intent.putExtra("name", itemname);
                intent.putExtra("price1", price1);
                intent.putExtra("price2", price2);
                intent.putExtra("flag",flag_check);
                startActivity(intent);

            }

            @Override
            public void onLongItemClick(View view, int position) {

            }
        }));

    }

    private void loadComboData() {
        header1.setText("Paratha (Combo)");
        header2.setVisibility(View.INVISIBLE);
        header3.setVisibility(View.INVISIBLE);

        dref = FirebaseDatabase.getInstance().getReference().child("Menu/combo");
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);

        dref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                pd.cancel();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    DetailMenuModel dmm = ds.getValue(DetailMenuModel.class);
                    detailMenu.add(new DetailMenuModel(dmm.name, dmm.price, dmm.description));
                }
                recyclerAdapter2 = new DetailMenuAdapter(detailMenu);
                recyclerView.setLayoutManager(linearLayoutManager);
                recyclerView.setAdapter(recyclerAdapter2);
                recyclerAdapter2.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getApplicationContext(), recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
               /* itemname = detailMenu.get(position).getName();
                itemprice = detailMenu.get(position).getPrice();
                showDialog(itemname, itemprice);*/
                String itemname = detailMenu.get(position).getName();
                String price1 = detailMenu.get(position).getPrice();
                String price2 = "0";
                String flag_check="0";
                Intent intent = new Intent(getApplicationContext(), MenuOrderActivity.class);
                intent.putExtra("name", itemname);
                intent.putExtra("price1", price1);
                intent.putExtra("price2", price2);
                intent.putExtra("flag",flag_check);
                startActivity(intent);

            }

            @Override
            public void onLongItemClick(View view, int position) {

            }
        }));
    }

    private void loadPlatterData() {
        header1.setText("Paratha (Platter)");
        header2.setVisibility(View.INVISIBLE);
        header3.setVisibility(View.INVISIBLE);

        dref = FirebaseDatabase.getInstance().getReference().child("Menu/platter");
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);

        dref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                pd.cancel();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    DetailMenuModel dmm = ds.getValue(DetailMenuModel.class);
                    detailMenu.add(new DetailMenuModel(dmm.name, dmm.price, dmm.description));
                }
                recyclerAdapter2 = new DetailMenuAdapter(detailMenu);
                recyclerView.setLayoutManager(linearLayoutManager);
                recyclerView.setAdapter(recyclerAdapter2);
                recyclerAdapter2.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getApplicationContext(), recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
               /* itemname = detailMenu.get(position).getName();
                itemprice = detailMenu.get(position).getPrice();
                showDialog(itemname, itemprice);*/
                String itemname = detailMenu.get(position).getName();
                String price1 = detailMenu.get(position).getPrice();
                String price2 = "0";
                String flag_check="0";
                Intent intent = new Intent(getApplicationContext(), MenuOrderActivity.class);
                intent.putExtra("name", itemname);
                intent.putExtra("price1", price1);
                intent.putExtra("price2", price2);
                intent.putExtra("flag",flag_check);
                startActivity(intent);

            }

            @Override
            public void onLongItemClick(View view, int position) {

            }
        }));

    }

    private void loadMealData() {
        header1.setText("Paratha (Meal)");
        header2.setVisibility(View.INVISIBLE);
        header3.setVisibility(View.INVISIBLE);

        dref = FirebaseDatabase.getInstance().getReference().child("Menu/paratha_meal");
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);

        dref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                pd.cancel();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    DetailMenuModel dmm = ds.getValue(DetailMenuModel.class);
                    detailMenu.add(new DetailMenuModel(dmm.name, dmm.price, dmm.description));
                }
                recyclerAdapter2 = new DetailMenuAdapter(detailMenu);
                recyclerView.setLayoutManager(linearLayoutManager);
                recyclerView.setAdapter(recyclerAdapter2);
                recyclerAdapter2.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getApplicationContext(), recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
               /* itemname = detailMenu.get(position).getName();
                itemprice = detailMenu.get(position).getPrice();
                showDialog(itemname, itemprice);*/
                String itemname = detailMenu.get(position).getName();
                String price1 = detailMenu.get(position).getPrice();
                String price2 = "0";
                String flag_check="0";
                Intent intent = new Intent(getApplicationContext(), MenuOrderActivity.class);
                intent.putExtra("name", itemname);
                intent.putExtra("price1", price1);
                intent.putExtra("price2", price2);
                intent.putExtra("flag",flag_check);
                startActivity(intent);

            }

            @Override
            public void onLongItemClick(View view, int position) {

            }
        }));
    }

    private void showDialog(String name, String price) {
        paymentMethodDialog.show();
        pay_itemname.setText(name);
        pay_itemprice.setText("(₹" + price + ")");

    }


    private void payUsingUPI(String amount, String upiId, String name, String note) {
        Uri uri = Uri.parse("upi://pay").buildUpon()
                .appendQueryParameter("pa", upiId)
                .appendQueryParameter("pn", name)
                .appendQueryParameter("tn", note)
                .appendQueryParameter("am", amount)
                .appendQueryParameter("cu", "INR")
                .build();

        Intent upiPayIntent = new Intent(Intent.ACTION_VIEW);
        upiPayIntent.setData(uri);

        // will always show a dialog to user to choose an app
        Intent chooser = Intent.createChooser(upiPayIntent, "Pay with");

        // check if intent resolves
        if (null != chooser.resolveActivity(getPackageManager())) {
            startActivityForResult(chooser, UPI_PAYMENT);
        } else {
            Toast.makeText(MenuActivity_Veg.this, "No UPI app found, please install one to continue", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case UPI_PAYMENT:
                if ((RESULT_OK == resultCode) || (resultCode == 11)) {
                    if (data != null) {
                        String trxt = data.getStringExtra("response");
                        Log.d("UPI", "onActivityResult: " + trxt);
                        ArrayList<String> dataList = new ArrayList<>();
                        dataList.add(trxt);
                        upiPaymentDataOperation(dataList);
                    } else {
                        Log.d("UPI", "onActivityResult: " + "Return data is null");
                        ArrayList<String> dataList = new ArrayList<>();
                        dataList.add("nothing");
                        upiPaymentDataOperation(dataList);
                    }
                } else {
                    Log.d("UPI", "onActivityResult: " + "Return data is null"); //when user simply back without payment
                    ArrayList<String> dataList = new ArrayList<>();
                    dataList.add("nothing");
                    upiPaymentDataOperation(dataList);
                }
                break;
        }
    }

    private void upiPaymentDataOperation(ArrayList<String> data) {
        if (isConnectionAvailable(MenuActivity_Veg.this)) {
            String str = data.get(0);
            Log.d("UPIPAY", "upiPaymentDataOperation: " + str);
            String paymentCancel = "";
            if (str == null) str = "discard";
            String status = "";
            String approvalRefNo = "";
            String response[] = str.split("&");
            for (int i = 0; i < response.length; i++) {
                String equalStr[] = response[i].split("=");
                if (equalStr.length >= 2) {
                    if (equalStr[0].toLowerCase().equals("Status".toLowerCase())) {
                        status = equalStr[1].toLowerCase();
                    } else if (equalStr[0].toLowerCase().equals("ApprovalRefNo".toLowerCase()) || equalStr[0].toLowerCase().equals("txnRef".toLowerCase())) {
                        approvalRefNo = equalStr[1];
                    }
                } else {
                    paymentCancel = "Payment cancelled by user.";
                }
            }

            if (status.equals("success")) {
                //Code to handle successful transaction here.
                Toast.makeText(MenuActivity_Veg.this, "Transaction successful.", Toast.LENGTH_SHORT).show();
                submitOrderUPI();
                //Log.d("UPI", "responseStr: " + approvalRefNo);
            } else if ("Payment cancelled by user.".equals(paymentCancel)) {
                Toast.makeText(MenuActivity_Veg.this, "Payment cancelled by user.", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(MenuActivity_Veg.this, "Transaction failed.Please try again", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(MenuActivity_Veg.this, "Internet connection is not available. Please check and try again", Toast.LENGTH_SHORT).show();
        }
    }

    public static boolean isConnectionAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            NetworkInfo netInfo = connectivityManager.getActiveNetworkInfo();
            if (netInfo != null && netInfo.isConnected()
                    && netInfo.isConnectedOrConnecting()
                    && netInfo.isAvailable()) {
                return true;
            }
        }
        return false;
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

    private void fetchData() {

        databaseReference.addValueEventListener(new ValueEventListener() {
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

    private void submitOrderUPI() {
        final String payStatus_UPI = "Payed by UPI";

        String order_date = getCurrentDate();
        String order_time = getCurrentTime();

        String username = user.getDisplayName();
        String usermail = user.getEmail();
        String user_id=user.getUid();

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("OrderDetails");
        String id = databaseReference.push().getKey();
        if (user_phone != null && user_address != null) {
            myOrderModel mom = new myOrderModel(itemname, itemprice, "1", order_time, order_date, username, user_phone, user_address, usermail, itemprice, user_id,payStatus_UPI);
            databaseReference.child(id).setValue(mom).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isComplete()) {

                        Toast.makeText(getApplicationContext(),"Order Placed Successfully",Toast.LENGTH_LONG).show();

                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                onBackPressed();
                                finish();
                            }
                        }, 1 * 1000);
                    } else {
                        Toast.makeText(getApplicationContext(), task.getException().toString(), Toast.LENGTH_LONG).show();
                    }
                }
            });
        } else {
            Toast.makeText(getApplicationContext(), "Please update your profile", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(MenuActivity_Veg.this, SignupActivity.class);
            startActivity(intent);
        }
    }

    private void submitOrderCOD(){
        final String payStatus_COD = "Cash On Delivery";
        String order_date = getCurrentDate();
        String order_time = getCurrentTime();

        String username = user.getDisplayName();
        String usermail = user.getEmail();
        String user_id=user.getUid();

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("OrderDetails");
        String id = databaseReference.push().getKey();
        if (user_phone != null && user_address != null) {
            myOrderModel mom = new myOrderModel(itemname, itemprice, "1", order_time, order_date, username, user_phone, user_address, usermail, itemprice, user_id,payStatus_COD);
            databaseReference.child(id).setValue(mom).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isComplete()) {

                        Toast.makeText(getApplicationContext(),"Order Placed Successfully",Toast.LENGTH_LONG).show();

                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                onBackPressed();
                                finish();
                            }
                        }, 1 * 1000);
                    } else {
                        Toast.makeText(getApplicationContext(), task.getException().toString(), Toast.LENGTH_LONG).show();
                    }
                }
            });
        } else {
            Toast.makeText(getApplicationContext(), "Please update your profile", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(MenuActivity_Veg.this, SignupActivity.class);
            startActivity(intent);
        }
    }


}
