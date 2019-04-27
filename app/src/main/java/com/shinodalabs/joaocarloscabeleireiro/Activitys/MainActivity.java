package com.shinodalabs.joaocarloscabeleireiro.Activitys;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.shinodalabs.joaocarloscabeleireiro.Fragments.MySchedulesFragment;
import com.shinodalabs.joaocarloscabeleireiro.Fragments.ProfileFragment;
import com.shinodalabs.joaocarloscabeleireiro.Fragments.ScheduleTimeFragment;
import com.shinodalabs.joaocarloscabeleireiro.R;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();

        toolbar = findViewById(R.id.toolbar);
        drawerLayout = findViewById(R.id.drawer_layout);
        setSupportActionBar(toolbar);

        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawe_open, R.string.navigation_drawe_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ScheduleTimeFragment())
                    .commit();
            getSupportActionBar().setSubtitle(getString(R.string.scedule_time));
            navigationView.setCheckedItem(R.id.nav_schedule_time);
        }

    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.nav_schedule_time:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ScheduleTimeFragment())
                        .commit();
                getSupportActionBar().setSubtitle(getString(R.string.scedule_time));
                ViewCompat.setElevation(toolbar, 5);
                break;
            case R.id.nav_my_schedules:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new MySchedulesFragment())
                        .commit();
                getSupportActionBar().setSubtitle(getString(R.string.my_schedules));
                ViewCompat.setElevation(toolbar, 0);
                break;
            case R.id.nav_about:
                Intent intent = new Intent(getApplicationContext(), AboutActivity.class);
                startActivity(intent);
                break;
            case R.id.nav_edit_profile:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ProfileFragment())
                        .commit();
                getSupportActionBar().setSubtitle(getString(R.string.profile));
                ViewCompat.setElevation(toolbar, 5);
                break;
            case R.id.nav_exit:
                logout();
                break;
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onResume() {
        mUser = FirebaseAuth.getInstance().getCurrentUser();
        if (mUser == null) {
            Intent intent = new Intent(getApplicationContext(), SignInActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        } else {
            setupHeader();
        }
        super.onResume();
    }

    private void logout() {
        mAuth.signOut();
        Intent intent = new Intent(getApplicationContext(), SignInActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    private void setupHeader() {
        View headerView = navigationView.getHeaderView(0);
        CircleImageView ivImage = headerView.findViewById(R.id.ivImage);
        TextView tvName = headerView.findViewById(R.id.tvName);
        TextView tvEmail = headerView.findViewById(R.id.tvEmail);

        Picasso.get().load(mAuth.getCurrentUser().getPhotoUrl()).into(ivImage);
        tvName.setText(mAuth.getCurrentUser().getDisplayName());
        tvEmail.setText(mAuth.getCurrentUser().getEmail());
    }

}





