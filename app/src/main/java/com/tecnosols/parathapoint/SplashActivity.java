package com.tecnosols.parathapoint;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Arrays;
import java.util.List;

public class SplashActivity extends AppCompatActivity {
    private static final int MY_REQUEST_CODE = 007;
    List<AuthUI.IdpConfig> providers;
    private Button getting_started;
    private TextView privacy_policy, termsofusage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        getting_started = (Button) findViewById(R.id.button_gettingstarted);
        privacy_policy = (TextView) findViewById(R.id.textView19_privacyPolicy);
        termsofusage = (TextView) findViewById(R.id.textView21_termsOfUsage);


        providers = Arrays.asList(
                new AuthUI.IdpConfig.EmailBuilder().build()
               /* new AuthUI.IdpConfig.PhoneBuilder().build(),
                new AuthUI.IdpConfig.EmailBuilder().build(),
                new AuthUI.IdpConfig.GoogleBuilder().build()*/
        );

        getting_started.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showSignInOptions();
               /* Intent intent=new Intent(getApplicationContext(),MainActivity.class);
                startActivity(intent);*/
            }
        });
        privacy_policy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://tecnosols.com/privacy_policy_pp/"));
                startActivity(browserIntent);
            }
        });
        termsofusage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent browserIntent1 = new Intent(Intent.ACTION_VIEW, Uri.parse("http://tecnosols.com/terms_and_conditions_pp/"));
                startActivity(browserIntent1);
            }
        });


    }

    private void showSignInOptions() {
        startActivityForResult(
                AuthUI.getInstance().createSignInIntentBuilder()
                        .setAvailableProviders(providers)
                        .setTheme(R.style.AppTheme)
                        .build(), MY_REQUEST_CODE
        );
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == MY_REQUEST_CODE) {
            IdpResponse response = IdpResponse.fromResultIntent(data);
            if (resultCode == RESULT_OK) {
                Toast.makeText(getApplicationContext(), "Successfully LoggedIn", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            Intent intent = new Intent(SplashActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

            startActivity(intent);
            finish();

        }
    }
}
