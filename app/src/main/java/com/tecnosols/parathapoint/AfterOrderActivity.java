package com.tecnosols.parathapoint;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AfterOrderActivity extends AppCompatActivity {
    String totalPrice;
    private TextView textAmount;
    private Button goBackToApp;
    FirebaseUser user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_after_order);
        Intent intent = getIntent();
        totalPrice = intent.getStringExtra("amount");
        textAmount = findViewById(R.id.textView27TotalAmount);
        textAmount.setText("Total Amount:- " + totalPrice);
        goBackToApp = findViewById(R.id.goBackToApp);
        user = FirebaseAuth.getInstance().getCurrentUser();
        DeleteUserCart();

        goBackToApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(AfterOrderActivity.this, MainActivity.class);
                intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent1);
            }
        });

    }

    private void DeleteUserCart() {
        String user_id=user.getUid();
        DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference().child("Cart_Details/"+user_id);
        databaseReference.removeValue();
    }
}
