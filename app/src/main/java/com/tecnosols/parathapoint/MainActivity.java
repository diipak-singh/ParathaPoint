package com.tecnosols.parathapoint;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import android.view.View;

import androidx.core.view.GravityCompat;
import androidx.appcompat.app.ActionBarDrawerToggle;

import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        user = FirebaseAuth.getInstance().getCurrentUser();


        /*FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/
        final DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        //navigationView.setCheckedItem(R.id.nav_home);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.getMenu().getItem(0).setChecked(true);
        getSupportFragmentManager().beginTransaction().replace(R.id.container1, homeFragment).commit();

        View headerView = navigationView.inflateHeaderView(R.layout.nav_header_main);
        ImageView imageView = headerView.findViewById(R.id.imageView_nav_header);
        TextView user_name = headerView.findViewById(R.id.textViewNameHeader);
        TextView user_email = headerView.findViewById(R.id.textViewEmailHeader);
        if (user.getPhotoUrl() != null) {
            Picasso.get().load(user.getPhotoUrl()).transform(new CircleTransform()).into(imageView);
        }
        if (user.getDisplayName() != null) {
            user_name.setText(user.getDisplayName());
        }
        if (user.getEmail() != null) {
            user_email.setText(user.getEmail());
        }


        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawer.closeDrawer(GravityCompat.START);
                getSupportFragmentManager().beginTransaction().replace(R.id.container1, settingsFragment).commit();

            }
        });

    }

    HomeFragment homeFragment = new HomeFragment();
    OrdersFragment ordersFragment = new OrdersFragment();
    CartFragment cartFragment = new CartFragment();
    SettingsFragment settingsFragment = new SettingsFragment();
    AboutusFragment aboutusFragment = new AboutusFragment();

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        /*if (id == R.id.action_settings) {
            getSupportFragmentManager().beginTransaction().replace(R.id.container1,settingsFragment).commit();
            return true;
        }*/
        if (id == R.id.action_cart) {
            //getSupportFragmentManager().beginTransaction().replace(R.id.container1, cartFragment).commit();
            Intent ci=new Intent(getApplicationContext(),CartActivity.class);
            startActivity(ci);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            getSupportFragmentManager().beginTransaction().replace(R.id.container1, homeFragment).commit();
            // return true;
            // Handle the camera action
        } else if (id == R.id.nav_orders) {
            getSupportFragmentManager().beginTransaction().replace(R.id.container1, ordersFragment).commit();
            // return true;

        } /*else if (id == R.id.nav_cart) {
            getSupportFragmentManager().beginTransaction().replace(R.id.container1,cartFragment).commit();

        } */ else if (id == R.id.nav_setting) {
            getSupportFragmentManager().beginTransaction().replace(R.id.container1, settingsFragment).commit();

        } else if (id == R.id.nav_about_us) {
            getSupportFragmentManager().beginTransaction().replace(R.id.container1, aboutusFragment).commit();

        } else if (id == R.id.nav_logout) {
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(getApplicationContext(), SplashActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
