package com.tecnosols.parathapoint;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;


/**
 * A simple {@link Fragment} subclass.
 */
public class SettingsFragment extends Fragment {
    private ImageView user_pic, update_profile,back_go;
    private TextView userName, userCity, userPhone, userEmail, userAddress, userPermanentAddress;
    private Button buttonSignOut;

    ProgressDialog pd;
    FirebaseUser user;
    DatabaseReference dref;


    public SettingsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        dref = FirebaseDatabase.getInstance().getReference().child("user_details");
        user = FirebaseAuth.getInstance().getCurrentUser();
        pd = new ProgressDialog(container.getContext());
        pd.setMessage("Loading...");
        pd.show();

        user_pic = view.findViewById(R.id.user_profile_pic);
        userName = view.findViewById(R.id.user_name);
        userAddress = view.findViewById(R.id.user_address);
        userCity = view.findViewById(R.id.user_city);
        userPhone = view.findViewById(R.id.user_phone);
        userEmail = view.findViewById(R.id.user_email);
        userPermanentAddress = view.findViewById(R.id.user_permanent_address);
        buttonSignOut = view.findViewById(R.id.button_sign_out);
        update_profile = view.findViewById(R.id.editDetails);
        back_go=view.findViewById(R.id.imageViewBack);

        back_go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(container.getContext(),MainActivity.class);
                startActivity(intent);
            }
        });

        update_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(container.getContext(), SignupActivity.class);
                startActivity(intent);
            }
        });

        buttonSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(container.getContext(), SplashActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });

        dref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                pd.cancel();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    user_details ud = ds.getValue(user_details.class);

                    if (user.getUid().contentEquals(ud.user_id)) {

                        Picasso.get().load(ud.user_pic).transform(new CircleTransform()).into(user_pic);
                        userName.setText(ud.user_name);
                        userAddress.setText(ud.user_city);
                        userCity.setText(ud.user_city);
                        userPhone.setText(ud.user_mobile);
                        userEmail.setText(ud.user_email);
                        userPermanentAddress.setText(ud.user_address);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                pd.cancel();

            }
        });



        return view;
    }






}
