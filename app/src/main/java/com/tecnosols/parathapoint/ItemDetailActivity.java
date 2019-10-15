package com.tecnosols.parathapoint;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class ItemDetailActivity extends AppCompatActivity {
    private ImageView itemImage,go_back;
    private TextView itemName, itemPrice;
    private String imageResource;
    private String item_name, item_price;
    private Toolbar toolbar;
    private Button buy_now,addToCart;
    FirebaseUser user;
    ConstraintLayout clayout;
    private EditText quantity;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_detail);
        itemImage = (ImageView) findViewById(R.id.imageViewItemDetailImage2);
        itemName = (TextView) findViewById(R.id.textViewItemNameDetail2);
        itemPrice = (TextView) findViewById(R.id.textViewItemPriceDetail2);
        go_back=(ImageView) findViewById(R.id.imageView_arrow_back2);
        buy_now=(Button) findViewById(R.id.button_buyNow);
        addToCart=(Button) findViewById(R.id.button_addToCart);
        clayout=(ConstraintLayout) findViewById(R.id.constraintL);
        quantity=(EditText) findViewById(R.id.editText_quantity2);


        user = FirebaseAuth.getInstance().getCurrentUser();

        final Intent intent = getIntent();
        imageResource=intent.getStringExtra("image_resource");
        item_name=intent.getStringExtra("item_name");
        item_price=intent.getStringExtra("item_price");

       // itemImage.setImageResource(imageResource);

        Picasso.get().load(imageResource).into(itemImage);
        itemName.setText(item_name);
        itemPrice.setText("Rs. "+item_price);

        go_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ItemDetailActivity.super.onBackPressed();
            }
        });
        buy_now.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String  item_quntity=quantity.getText().toString();
                Intent intent1=new Intent(getApplicationContext(),OrderConfirmationActivity.class);
                intent1.putExtra("image",imageResource);
                intent1.putExtra("name",item_name);
                intent1.putExtra("price",item_price);
                intent1.putExtra("quantity",item_quntity);
                startActivity(intent1);
            }
        });

        addToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveCartData();
            }
        });

    }

    private void saveCartData(){
        String user_id=user.getUid();

        String  item_quntity=quantity.getText().toString();
        Integer price=Integer.parseInt(item_quntity)*Integer.parseInt(item_price);
        DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference().child("Cart_Details/"+user_id);
        String id = databaseReference.push().getKey();
        cart_details cd=new cart_details(imageResource,item_name,price.toString(),item_quntity,user_id,id);
        databaseReference.child(id).setValue(cd).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isComplete()){
                    Snackbar snackbar = Snackbar
                            .make(clayout, "Added to cart!", Snackbar.LENGTH_LONG);
                    snackbar.show();
                   // Toast.makeText(getApplicationContext(),"Added to cart successfully",Toast.LENGTH_LONG);
                    //onBackPressed();
                }
            }
        });
    }



}
