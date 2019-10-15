package com.tecnosols.parathapoint;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.Dialog;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
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

public class MenuOrderActivity extends AppCompatActivity {
    private ImageView go_back;
    String itemname, price1_piece, price2_piece,user_phone, user_address;
    private TextView text_itemname, paybale_amount;
    private RadioButton one, two;
    Integer price = 0, total_price;
    private EditText qty;
    private Button buy_now,addToCart;
    private Dialog paymentMethodDialog;
    private ImageView paytm_payment,cod;
    FirebaseUser user;
    DatabaseReference dref;
    private ConstraintLayout constraintLayout;

    private final String upiID = "pallavikumar0209@okaxis";
    private final String note = "Paying to ParathaPoint";
    private final String name = "ParathaPoint";
    final int UPI_PAYMENT = 0;

    final String payStatus_UPI="Payed by UPI";
    final String payStatus_COD="Cash On Delivery";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_order);
        text_itemname = (TextView) findViewById(R.id.textViewItemNameDetail2);
        one = (RadioButton) findViewById(R.id.radioButton_1piece);
        two = (RadioButton) findViewById(R.id.radioButton_2piece);
        qty = (EditText) findViewById(R.id.editText_quantity2);
        paybale_amount = (TextView) findViewById(R.id.textView14);
        buy_now=(Button) findViewById(R.id.button_buyNow);
        addToCart=(Button) findViewById(R.id.button_addToCart);
        constraintLayout=(ConstraintLayout) findViewById(R.id.constarint_layout_menu);

        //one.setChecked(true);


        final Intent intent = getIntent();
        itemname = intent.getStringExtra("name");
        price1_piece = intent.getStringExtra("price1");
        price2_piece = intent.getStringExtra("price2");
        String flag=intent.getStringExtra("flag");

        dref = FirebaseDatabase.getInstance().getReference().child("user_details");
        user = FirebaseAuth.getInstance().getCurrentUser();
        fetchData();

        paymentMethodDialog = new Dialog(MenuOrderActivity.this);
        paymentMethodDialog.setContentView(R.layout.payment_method);
        paymentMethodDialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.round_corners));
        paymentMethodDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        paytm_payment = paymentMethodDialog.findViewById(R.id.imageView6_paytm);
        cod=paymentMethodDialog.findViewById(R.id.imageView5_cod);


        text_itemname.setText(itemname);

        go_back = findViewById(R.id.imageView_arrow_back2);
        go_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        if(flag.contentEquals("1"))
        {
            one.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (two.isChecked()) {
                        two.setChecked(false);
                    }
                    calculateTotalPrice();

                }
            });
            two.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (one.isChecked()) {
                        one.setChecked(false);
                    }
                    calculateTotalPrice();
                    //two.setChecked(true);
                }
            });

            buy_now.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(!one.isChecked() && !two.isChecked()){
                        Toast.makeText(getApplicationContext(),"Please Select the size of parathas above",Toast.LENGTH_LONG).show();
                        return;
                    }
                    paymentMethodDialog.show();
                }
            });

        }

        else if(flag.contentEquals("0"))
        {
            two.setVisibility(View.INVISIBLE);
            one.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    calculateTotalPrice();

                }
            });
        }

        addToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!one.isChecked() && !two.isChecked()){
                    Toast.makeText(getApplicationContext(),"Please Select the size of parathas above",Toast.LENGTH_LONG).show();
                    return;
                }
                AddToCart();
            }
        });

        paytm_payment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String amount=Integer.toString(total_price);
                paymentMethodDialog.dismiss();
                payUsingUPI(amount,upiID,name,note);
            }
        });
        cod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submitOrderCOD();
            }
        });

    }

    private void fetchData() {

        dref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
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

            }
        });
    }

    private void calculateTotalPrice() {
        qty.setEnabled(false);
        if (one.isChecked()) {
            price = Integer.parseInt(price1_piece);
            Integer quantity = Integer.parseInt(qty.getText().toString());
            total_price = price * quantity;
            paybale_amount.setText("Total Payable Amount: ₹" + total_price);
        } else if (two.isChecked()) {
            price = Integer.parseInt(price2_piece);
            Integer quantity = Integer.parseInt(qty.getText().toString());
            total_price = price * quantity;
            paybale_amount.setText("Total Payable Amount: ₹" + total_price);
        }
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
            Toast.makeText(MenuOrderActivity.this, "No UPI app found, please install one to continue", Toast.LENGTH_SHORT).show();
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
        if (isConnectionAvailable(MenuOrderActivity.this)) {
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
                Toast.makeText(MenuOrderActivity.this, "Transaction successful.", Toast.LENGTH_SHORT).show();
                submitOrderUPI();
                //Log.d("UPI", "responseStr: " + approvalRefNo);
            } else if ("Payment cancelled by user.".equals(paymentCancel)) {
                Toast.makeText(MenuOrderActivity.this, "Payment cancelled by user.", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(MenuOrderActivity.this, "Transaction failed.Please try again", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(MenuOrderActivity.this, "Internet connection is not available. Please check and try again", Toast.LENGTH_SHORT).show();
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
            myOrderModel mom = new myOrderModel(itemname, price.toString(), qty.getText().toString(), order_time, order_date, username, user_phone, user_address, usermail, total_price.toString(), user_id,payStatus_UPI);
            databaseReference.child(id).setValue(mom).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isComplete()) {

                        Toast.makeText(getApplicationContext(),"Order Placed Successfully",Toast.LENGTH_LONG).show();

                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                onBackPressed();
                            }
                        }, 1 * 1000);
                    } else {
                        Toast.makeText(getApplicationContext(), task.getException().toString(), Toast.LENGTH_LONG).show();
                    }
                }
            });
        } else {
            Toast.makeText(getApplicationContext(), "Please update your profile", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(MenuOrderActivity.this, SignupActivity.class);
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
            myOrderModel mom = new myOrderModel(itemname, price.toString(), qty.getText().toString(), order_time, order_date, username, user_phone, user_address, usermail, total_price.toString(), user_id,payStatus_COD);
            databaseReference.child(id).setValue(mom).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isComplete()) {

                        Toast.makeText(getApplicationContext(),"Order Placed Successfully",Toast.LENGTH_LONG).show();

                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                onBackPressed();
                            }
                        }, 1 * 1000);
                    } else {
                        Toast.makeText(getApplicationContext(), task.getException().toString(), Toast.LENGTH_LONG).show();
                    }
                }
            });
        } else {
            Toast.makeText(getApplicationContext(), "Please update your profile", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(MenuOrderActivity.this, SignupActivity.class);
            startActivity(intent);
        }
    }
    private void AddToCart(){
        String user_id=user.getUid();
        String imageResource="";

        String quantity=qty.getText().toString().trim();
        DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference().child("Cart_Details/"+user_id);
        String id = databaseReference.push().getKey();
        cart_details cd=new cart_details(imageResource,itemname,total_price.toString(),quantity,user_id,id);
        databaseReference.child(id).setValue(cd).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isComplete()){
                    Snackbar snackbar = Snackbar
                            .make(constraintLayout, "Added to cart!", Snackbar.LENGTH_LONG);
                    snackbar.show();
                    // Toast.makeText(getApplicationContext(),"Added to cart successfully",Toast.LENGTH_LONG);
                    //onBackPressed();
                }
            }
        });
    }

}
