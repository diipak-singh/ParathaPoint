package com.tecnosols.parathapoint;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.Image;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
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
import com.paytm.pgsdk.PaytmOrder;
import com.paytm.pgsdk.PaytmPGService;
import com.paytm.pgsdk.PaytmPaymentTransactionCallback;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class OrderConfirmationActivity extends AppCompatActivity {
    private ImageView itemPic, goback;
    private Button buton_payment;
    private TextView itemName, itemPrice, totalAmount, amount_text;
    private String imageResource, item_name, item_price, item_quantity, user_phone, user_address, user_id;
    private final String upiID = "pallavikumar0209@okaxis";
    private final String note = "Paying to ParathaPoint";
    private final String name = "ParathaPoint";
    final int UPI_PAYMENT = 0;
    Integer total_amount;
    private Dialog paymentMethodDialog;
    private ImageView paytm_payment,cod;

    ProgressDialog pd;
    FirebaseUser user;
    DatabaseReference dref;
    ConstraintLayout constraintLayout;
    String CHECKSUMHASH;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_confirmation);
        itemPic = (ImageView) findViewById(R.id.image_view_image);
        itemName = (TextView) findViewById(R.id.text_view_item_name);
        itemPrice = (TextView) findViewById(R.id.text_view_item_price);
        buton_payment = (Button) findViewById(R.id.button_pay);
        totalAmount = (TextView) findViewById(R.id.textView_total_amount);
        amount_text = (TextView) findViewById(R.id.textView_amount);
        constraintLayout = (ConstraintLayout) findViewById(R.id.constarintLayout);
        goback = (ImageView) findViewById(R.id.imageView4back);


        final Intent intent = getIntent();
        imageResource = intent.getStringExtra("image");
        item_name = intent.getStringExtra("name");
        item_price = intent.getStringExtra("price");
        item_quantity = intent.getStringExtra("quantity");

        dref = FirebaseDatabase.getInstance().getReference().child("user_details");
        user = FirebaseAuth.getInstance().getCurrentUser();
        pd = new ProgressDialog(getApplicationContext());
        pd.setMessage("Submitting Order...");
        user_id = user.getUid();
        //pd.show();
        fetchData();

        paymentMethodDialog = new Dialog(OrderConfirmationActivity.this);
        paymentMethodDialog.setContentView(R.layout.payment_method);
        paymentMethodDialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.round_corners));
        paymentMethodDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        paytm_payment = paymentMethodDialog.findViewById(R.id.imageView6_paytm);
        cod = paymentMethodDialog.findViewById(R.id.imageView5_cod);

        Integer price = Integer.parseInt(item_price) * Integer.parseInt(item_quantity);

        total_amount = (((price * 5)) / 100) + price;
        totalAmount.setText(Integer.toString(total_amount));
        amount_text.setText(item_quantity + "*" + item_price);

        goback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        Picasso.get().load(imageResource).into(itemPic);
        itemName.setText(item_name);
        itemPrice.setText("Rs. " + item_price);

        buton_payment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                paymentMethodDialog.show();
                //submitOrder();


            }
        });
        paytm_payment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                payUsingUPI(total_amount.toString(),upiID,name,note);
                paymentMethodDialog.dismiss();
            }
        });
        cod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submitOrderCOD();
                paymentMethodDialog.dismiss();
            }
        });
    }



    private void fetchData() {

        dref.addValueEventListener(new ValueEventListener() {
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
            Toast.makeText(OrderConfirmationActivity.this, "No UPI app found, please install one to continue", Toast.LENGTH_SHORT).show();
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
        if (isConnectionAvailable(OrderConfirmationActivity.this)) {
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
                Toast.makeText(OrderConfirmationActivity.this, "Transaction successful.", Toast.LENGTH_SHORT).show();
                submitOrderUPI();
                //Log.d("UPI", "responseStr: " + approvalRefNo);
            } else if ("Payment cancelled by user.".equals(paymentCancel)) {
                Toast.makeText(OrderConfirmationActivity.this, "Payment cancelled by user.", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(OrderConfirmationActivity.this, "Transaction failed.Please try again", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(OrderConfirmationActivity.this, "Internet connection is not available. Please check and try again", Toast.LENGTH_SHORT).show();
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
            myOrderModel mom = new myOrderModel(item_name, item_price, item_quantity, order_time, order_date, username, user_phone, user_address, usermail, total_amount.toString(), user_id,payStatus_UPI);
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
            Intent intent = new Intent(OrderConfirmationActivity.this, SignupActivity.class);
            startActivity(intent);
            finish();
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
            myOrderModel mom = new myOrderModel(item_name, item_price, item_quantity, order_time, order_date, username, user_phone, user_address, usermail, total_amount.toString(), user_id,payStatus_COD);
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
            Intent intent = new Intent(OrderConfirmationActivity.this, SignupActivity.class);
            startActivity(intent);
            finish();
        }
    }
}
