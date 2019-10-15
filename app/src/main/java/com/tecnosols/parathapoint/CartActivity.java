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
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
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

public class CartActivity extends AppCompatActivity {
    private ImageView imageViewBack;
    private RecyclerView recyclerViewCartNew;
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
    static Context context;
    private Button payment_dialog;

    private Dialog paymentMethodDialog, detailsConfirmDialog;
    private ImageView paytm_payment, cod;
    private String user_phone, user_address;
    private TextView totalCartAmount;
    private EditText etDname,etDphone,etDaddress;
    private RadioButton rdbDcod,rdbDupi;
    private Button btnDcancel,btnDordernow;

    private final String upiID = "pallavikumar0209@okaxis";
    private final String note = "Paying to ParathaPoint";
    private final String name = "ParathaPoint";
    final int UPI_PAYMENT = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        imageViewBack = findViewById(R.id.imageView11_backCart);
        recyclerViewCartNew = findViewById(R.id.recyclerViewCartNew);
        payment_dialog = (Button) findViewById(R.id.buttonCheckoutCart);
        totalCartAmount = (TextView) findViewById(R.id.textViewCartTotal);
        totalCartAmount.setText("0");
        total_price=0;
        getData();

       /* paymentMethodDialog = new Dialog(CartActivity.this);
        paymentMethodDialog.setContentView(R.layout.payment_method);
        paymentMethodDialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.round_corners));
        paymentMethodDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        paytm_payment = paymentMethodDialog.findViewById(R.id.imageView6_paytm);
        cod = paymentMethodDialog.findViewById(R.id.imageView5_cod);*/

        detailsConfirmDialog = new Dialog(CartActivity.this);
        detailsConfirmDialog.setContentView(R.layout.user_detail_confirmation);
        //detailsConfirmDialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.round_corners));
        detailsConfirmDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        //detailsConfirmDialog.getWindow().setGravity(Gravity.BOTTOM);
        etDname=detailsConfirmDialog.findViewById(R.id.editText_nameDialog);
        etDphone=detailsConfirmDialog.findViewById(R.id.editText_phoneDialog);
        etDaddress=detailsConfirmDialog.findViewById(R.id.editText_addressialog);
        rdbDcod=detailsConfirmDialog.findViewById(R.id.radioButton_codDialog);
        rdbDupi=detailsConfirmDialog.findViewById(R.id.radioButton_upiDialog);
        btnDcancel=detailsConfirmDialog.findViewById(R.id.button_cancelDialog);
        btnDordernow=detailsConfirmDialog.findViewById(R.id.button_ordernowDialog);

        pd = new ProgressDialog(CartActivity.this);
        pd.setMessage("Placing Orders...");
        pd.setCancelable(true);
        fetchData();


        imageViewBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //onBackPressed();
                total_price = 0;
                totalCartAmount.setText("0");
                onBackPressed();
                /*Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();*/
            }
        });
        payment_dialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // paymentMethodDialog.show();
                detailsConfirmDialog.show();
            }
        });
       /* cod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                makeOrder();
            }
        });
        paytm_payment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "No payment gateway for cart right now", Toast.LENGTH_SHORT).show();
            }
        });*/
       rdbDcod.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               rdbDcod.setChecked(true);
               rdbDupi.setChecked(false);
           }
       });
        rdbDupi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rdbDcod.setChecked(false);
                rdbDupi.setChecked(true);
            }
        });
        btnDcancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                detailsConfirmDialog.cancel();
            }
        });
        btnDordernow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(rdbDcod.isChecked()){
                    makeOrderCOD();
                }
                else if(rdbDupi.isChecked()){
                    payUsingUPI(total_price.toString(),upiID,name,note);
                }
                else
                    Toast.makeText(getApplicationContext(),"Please select a payment method.",Toast.LENGTH_SHORT).show();

            }
        });

    }

    public void deleteData(final int position) {
        //cartDetails.remove(position);
        total_price = 0;

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String user_id = user.getUid();


        String key = cartDetails.get(position).getData_key();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Cart_Details/" + user_id);
        //cartDetails.remove(position);
        databaseReference.child(key).removeValue();
        //
        total_price = 0;

        recyclerAdapter.notifyDataSetChanged();

        //cartDetails.clear();
        //dref.child(key).removeValue();

        //Task<Void> task = dref.child(key).removeValue();
        //cartDetails.remove(position);
        //recyclerAdapter.notifyDataSetChanged();
        /*recyclerAdapter.notifyItemRemoved(position);
        recyclerAdapter.notifyItemRangeChanged(position,cartDetails.size());
        cartDetails.clear();
        recyclerAdapter.notifyDataSetChanged();*/
        //cartDetails.clear();



    }

    private void makeOrderCOD() {

        final String payStatus_COD = "Cash On Delivery.";
        String order_date = getCurrentDate();
        String order_time = getCurrentTime();

        String username = etDname.getText().toString().trim();
        String usermail = user.getEmail();
        String user_id = user.getUid();
        String userPhone=etDphone.getText().toString().trim();
        String userAddress=etDaddress.getText().toString().trim();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("OrderDetails");


        for (cart_details cd : cartDetails) {

            String id = databaseReference.push().getKey();
            myOrderModel mom = new myOrderModel(cd.getItemname(), cd.getItemprice(), cd.getQuantity(), order_time, order_date, username, userPhone, userAddress, usermail, total_price.toString(), user_id, payStatus_COD);
            databaseReference.child(id).setValue(mom).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isComplete()) {

                        Toast.makeText(getApplicationContext(), "Placing Orders...", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getApplicationContext(), task.getException().toString(), Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
            });

        }
        afterOrders();


    }



    private void getData() {

        context = getApplicationContext();

        user = FirebaseAuth.getInstance().getCurrentUser();
        final String user_id = user.getUid();

        dref = FirebaseDatabase.getInstance().getReference().child("Cart_Details/" + user_id);
        if (cartDetails.size() > 0) {
            cartDetails.clear();
        }

        linearLayoutManager = new LinearLayoutManager(CartActivity.this);
        linearLayoutManager.setOrientation(RecyclerView.VERTICAL);
        if (dref != null) {

            dref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    //pd.cancel();
                    cartDetails.clear();
                    total_price=0;
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        cart_details crd = ds.getValue(cart_details.class);
                        cartDetails.add(new cart_details(crd.item_img_url, crd.getItemname(), crd.itemprice, crd.quantity, user_id, crd.getData_key()));
                        Log.i("Qty", crd.quantity);
                        Integer temp = Integer.parseInt(crd.itemprice);
                        total_price = total_price + temp;

                    }

                    recyclerAdapter = new cartAdapter(cartDetails);
                    recyclerViewCartNew.setLayoutManager(new LinearLayoutManager(CartActivity.this));
                    recyclerViewCartNew.setAdapter(recyclerAdapter);


                    recyclerAdapter.notifyDataSetChanged();

                    DisplayPrice(total_price.toString());
                    Log.i("cartSize", Integer.toString(cartDetails.size()));


                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

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

    private void DisplayPrice(String amount) {
        totalCartAmount.setText("Total Price: ₹" + amount);
    }

    private void afterOrders() {

        Intent intent = new Intent(getApplicationContext(), AfterOrderActivity.class);
        intent.putExtra("amount", total_price.toString());
        totalCartAmount.setText("0");
        total_price=0;
        startActivity(intent);
    }

    private void fetchData() {
        DatabaseReference dref1 = FirebaseDatabase.getInstance().getReference().child("user_details");

        dref1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    user_details ud = ds.getValue(user_details.class);

                    if (user.getUid().contentEquals(ud.user_id)) {
                        user_phone = ud.user_mobile;
                        user_address = ud.user_address;

                    }
                }
                etDname.setText(user.getDisplayName());
                etDphone.setText(user_phone);
                etDaddress.setText(user_address);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        total_price = 0;
        //totalCartAmount.setText("0");
        super.onBackPressed();
        this.finish();
    }

    @Override
    protected void onPause() {
        total_price = 0;
        super.onPause();
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
            Toast.makeText(CartActivity.this, "No UPI app found, please install one to continue", Toast.LENGTH_SHORT).show();
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
        if (isConnectionAvailable(CartActivity.this)) {
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
                Toast.makeText(CartActivity.this, "Transaction successful.", Toast.LENGTH_SHORT).show();
                makeOrderUPI();
                //Log.d("UPI", "responseStr: " + approvalRefNo);
            } else if ("Payment cancelled by user.".equals(paymentCancel)) {
                Toast.makeText(CartActivity.this, "Payment cancelled by user.", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(CartActivity.this, "Transaction failed.Please try again", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(CartActivity.this, "Internet connection is not available. Please check and try again", Toast.LENGTH_SHORT).show();
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

    private void makeOrderUPI(){

        final String payStatus_COD = "Paid via UPI.";
        String order_date = getCurrentDate();
        String order_time = getCurrentTime();

        String username = etDname.getText().toString().trim();
        String usermail = user.getEmail();
        String user_id = user.getUid();
        String userPhone=etDphone.getText().toString().trim();
        String userAddress=etDaddress.getText().toString().trim();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("OrderDetails");


        for (cart_details cd : cartDetails) {

            String id = databaseReference.push().getKey();
            myOrderModel mom = new myOrderModel(cd.getItemname(), cd.getItemprice(), cd.getQuantity(), order_time, order_date, username, userPhone, userAddress, usermail, total_price.toString(), user_id, payStatus_COD);
            databaseReference.child(id).setValue(mom).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isComplete()) {

                        Toast.makeText(getApplicationContext(), "Placing Orders...", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getApplicationContext(), task.getException().toString(), Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
            });

        }
        afterOrders();

    }

}
