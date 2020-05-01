package com.example.pfood.Activities;

import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.net.NetworkRequest;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.example.pfood.Classes.AppSettings;
import com.example.pfood.Classes.AppUsers;
import com.example.pfood.Classes.Food;
import com.example.pfood.Classes.Order;
import com.example.pfood.Classes.Team;
import com.example.pfood.Classes.User;
import com.example.pfood.Fragments.CartFragment;
import com.example.pfood.Fragments.MenuFragment;
import com.example.pfood.Fragments.OrderFragment;
import com.example.pfood.Fragments.ProfileFragment;
import com.example.pfood.Fragments.RatingFragment;
import com.example.pfood.NetworkClasses.NetworkTeams;
import com.example.pfood.R;
import com.example.pfood.ResponseModels.TeamModel;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.example.pfood.Activities.App.CHANNEL_ID;

public class NavigationActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, NetworkTeams.GetTeamsCallback {
    private DrawerLayout drawer;
    private NavigationView navigationView;
    private NotificationManagerCompat notificationManager;
    private MenuItem menuItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final ProgressDialog progressDialog = ProgressDialog.show(this, "", "Пожалуйста, подождите...");

        FirebaseApp.initializeApp(this);

        setContentView(R.layout.activity_navigation);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        AppSettings.getInstance().tvNum = toolbar.findViewById(R.id.circle_number);
        AppSettings.getInstance().ivCircle = toolbar.findViewById(R.id.rcircle);
        AppSettings.getInstance().tvNum.setVisibility(View.INVISIBLE);
        AppSettings.getInstance().ivCircle.setVisibility(View.INVISIBLE);

        notificationManager = NotificationManagerCompat.from(this);

        NetworkTeams.onGetTeamsCallback(this);
        NetworkTeams.getTeams();

        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        toolbar.findViewById(R.id.cart_container_rl).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CartFragment cartFragment = (CartFragment) getSupportFragmentManager().findFragmentByTag("CartFragment");
                if (cartFragment == null || !cartFragment.isVisible()) {
                    getSupportFragmentManager().beginTransaction()
                            .setCustomAnimations(R.anim.enter_right_to_left, R.anim.exit_right_to_left,
                                    R.anim.enter_left_to_right, R.anim.exit_left_to_right)
                            .replace(R.id.fragment_container, new CartFragment(), "CartFragment").addToBackStack(null).commit();
                    navigationView.setCheckedItem(R.id.nav_basket);
                }
            }
        });

        drawer = findViewById(R.id.drawer_layout);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        FirebaseDatabase.getInstance().getReference("chef_ids").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                AppSettings.getInstance().chefIds.clear();

                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    AppSettings.getInstance().chefIds.add(postSnapshot.getValue(String.class));
                }

                if (FirebaseAuth.getInstance().getCurrentUser() != null) {
                    if (AppSettings.getInstance().chefIds.contains(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                        navigationView.getMenu().clear();
                        navigationView.inflateMenu(R.menu.admin_drawer_menu);
                        AppSettings.getInstance().menuItem = navigationView.getMenu().findItem(R.id.nav_orders);

                        menuItem = navigationView.getMenu().findItem(R.id.nav_orders);

                        FirebaseDatabase.getInstance().getReference("orders").addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                AppSettings.getInstance().orderList.clear();

                                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                                    String address = postSnapshot.child("address").getValue(String.class);
                                    String commentary = postSnapshot.child("commentary").getValue(String.class);
                                    String name = postSnapshot.child("name").getValue(String.class);
                                    String phone = postSnapshot.child("phone").getValue(String.class);
                                    Integer price = postSnapshot.child("price").getValue(Integer.class);
                                    String time = postSnapshot.child("time").getValue(String.class);
                                    String userId = postSnapshot.child("userId").getValue(String.class);
                                    String paymentType = postSnapshot.child("paymentType").getValue(String.class);
                                    ArrayList<String> foodCart = new ArrayList<>();

                                    for (DataSnapshot exPostSnapshot : postSnapshot.child("foodCart").getChildren()) {
                                        foodCart.add(exPostSnapshot.getValue(String.class));
                                    }

                                    Order order = new Order(userId, name, address, phone, time, foodCart, commentary, price, paymentType);
                                    order.setKey(postSnapshot.getKey());
                                    AppSettings.getInstance().orderList.add(order);

                                    if (menuItem != null) {
                                        if (AppSettings.getInstance().orderList.isEmpty()) {
                                            menuItem.setTitle("Заказы");
                                        }
                                        else {
                                            menuItem.setTitle("Заказы (" + AppSettings.getInstance().orderList.size() + ")");
                                        }
                                    }

                                    if (AppSettings.getInstance().orderAdapter != null) {
                                        AppSettings.getInstance().orderAdapter.notifyDataSetChanged();
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });

                        FirebaseDatabase.getInstance().getReference("orders").addChildEventListener(new ChildEventListener() {
                            @Override
                            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                                sendNotification();
                            }

                            @Override
                            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                            }

                            @Override
                            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

                            }

                            @Override
                            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        FirebaseDatabase.getInstance().getReference("delivery_settings").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                AppSettings.getInstance().deliveryCost = dataSnapshot.child("delivery_cost").getValue(Integer.class);
                AppSettings.getInstance().freeDeliveryCost = dataSnapshot.child("free_delivery_cost").getValue(Integer.class);
                AppSettings.getInstance().earliestTime = dataSnapshot.child("earliest_time").getValue(String.class);
                AppSettings.getInstance().latestTime = dataSnapshot.child("latest_time").getValue(String.class);

                TextView contacts = navigationView.getHeaderView(0).findViewById(R.id.contacts);
                contacts.setText(dataSnapshot.child("phones").getValue(String.class));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        FirebaseDatabase.getInstance().getReference("categories").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    for (DataSnapshot exPostSnapshot : postSnapshot.getChildren()) {
                        String name = exPostSnapshot.child("name").getValue(String.class);
                        Integer price = exPostSnapshot.child("price").getValue(Integer.class);
                        String description = exPostSnapshot.child("description").getValue(String.class);
                        String imageUrl = exPostSnapshot.child("imageUrl").getValue(String.class);
                        String products = exPostSnapshot.child("products").getValue(String.class);
                        Boolean sale = exPostSnapshot.child("sale").getValue(Boolean.class);

                        if (sale == null) sale = false;

                        AppSettings.getInstance().foodCache.add(new Food(name, price, description, products, imageUrl, sale));
                        progressDialog.dismiss();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        FirebaseDatabase.getInstance().getReference("courier_ids").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                AppSettings.getInstance().courierIds.clear();

                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    AppSettings.getInstance().courierIds.add(postSnapshot.getValue(String.class));
                }

                if (FirebaseAuth.getInstance().getCurrentUser() != null) {
                    if (AppSettings.getInstance().courierIds.contains(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                        navigationView.getMenu().clear();
                        navigationView.inflateMenu(R.menu.admin_drawer_menu);
                        navigationView.setCheckedItem(R.id.nav_menu);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        FirebaseDatabase.getInstance().getReference("users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                AppUsers.getInstance().userList.clear();

                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    String name = postSnapshot.child("name").getValue(String.class);
                    String address = postSnapshot.child("address").getValue(String.class);
                    String inviteCode = postSnapshot.child("inviteCode").getValue(String.class);
                    Integer bonusesCount = postSnapshot.child("bonusesCount").getValue(Integer.class);
                    Integer rating = postSnapshot.child("rating").getValue(Integer.class);
                    Integer ratingPosition = postSnapshot.child("ratingPosition").getValue(Integer.class);
                    Integer monthRating = postSnapshot.child("monthRating").getValue(Integer.class);
                    Integer monthRatingPosition = postSnapshot.child("monthRatingPosition").getValue(Integer.class);

                    User user = new User(name, address, inviteCode, bonusesCount, rating, ratingPosition, monthRating, monthRatingPosition);

                    if (FirebaseAuth.getInstance().getCurrentUser() != null) {
                        if (FirebaseAuth.getInstance().getCurrentUser().getUid() != null) {
                            if (FirebaseAuth.getInstance().getCurrentUser().getUid().equals(postSnapshot.getKey()))
                            {
                                AppUsers.getInstance().currentUser = user;

                                TextView userName = navigationView.getHeaderView(0).findViewById(R.id.header_name);
                                TextView userPhone = navigationView.getHeaderView(0).findViewById(R.id.header_phone);
                                TextView contacts = navigationView.getHeaderView(0).findViewById(R.id.contacts);

                                userName.setText(user.getName());
                                userPhone.setText(FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber());
                                contacts.setText(AppSettings.getInstance().contactPhone);
                            }
                        }
                    }


                    AppUsers.getInstance().userList.add(user);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

        FirebaseDatabase.getInstance().getReference("teams").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                AppUsers.getInstance().teamList.clear();

                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    String teamName = postSnapshot.child("teamName").getValue(String.class);
                    Integer teamRating = postSnapshot.child("teamRating").getValue(Integer.class);
                    Integer teamPlace = postSnapshot.child("teamPlace").getValue(Integer.class);
                    ArrayList<String> teamMembers = new ArrayList<>();

                    for (DataSnapshot exPostSnapshot : postSnapshot.child("teamMembers").getChildren()) {
                        teamMembers.add(exPostSnapshot.getValue(String.class));
                    }

                    AppUsers.getInstance().teamList.add(new Team(teamName, teamPlace, teamRating));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

        ConnectivityManager.NetworkCallback networkCallback = new ConnectivityManager.NetworkCallback() {
            @Override
            public void onAvailable(@NonNull Network network) {
                super.onAvailable(network);
            }

            @Override
            public void onLost(@NonNull Network network) {
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(NavigationActivity.this);
                View mView = getLayoutInflater().inflate(R.layout.network_error, null);

                mBuilder.setView(mView);
                final AlertDialog dialog = mBuilder.create();
                dialog.show();

                mView.findViewById(R.id.error_ok_button).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        finish();
                        System.exit(0);
                    }
                });

                dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialogInterface) {
                        finish();
                        System.exit(0);
                    }
                });
            }
        };

        if (!isNetworkAvailable()) {
            AlertDialog.Builder mBuilder = new AlertDialog.Builder(NavigationActivity.this);
            View mView = getLayoutInflater().inflate(R.layout.network_error, null);

            mBuilder.setView(mView);
            final AlertDialog dialog = mBuilder.create();
            dialog.show();

            mView.findViewById(R.id.error_ok_button).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    finish();
                    System.exit(0);
                }
            });

            dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialogInterface) {
                    finish();
                    System.exit(0);
                }
            });
        }

        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            connectivityManager.registerDefaultNetworkCallback(networkCallback);
        } else {
            NetworkRequest request = new NetworkRequest.Builder()
                    .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET).build();
            connectivityManager.registerNetworkCallback(request, networkCallback);
        }

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new MenuFragment(), "MenuFragment").commit();

            navigationView.setCheckedItem(R.id.nav_menu);
        }

        if (AppSettings.getInstance().fullNumPrice != 0) {
            AppSettings.getInstance().tvNum.setVisibility(View.VISIBLE);
            AppSettings.getInstance().ivCircle.setVisibility(View.VISIBLE);

            AppSettings.getInstance().tvNum.setText(AppSettings.getInstance().fullNumPrice + " \u20BD");
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_menu:
                MenuFragment menuFragment = (MenuFragment) getSupportFragmentManager().findFragmentByTag("MenuFragment");
                if (menuFragment == null || !menuFragment.isVisible()) {
                    getSupportFragmentManager().beginTransaction()
                            .setCustomAnimations(R.anim.enter_right_to_left, R.anim.exit_right_to_left,
                                    R.anim.enter_left_to_right, R.anim.exit_left_to_right)
                            .replace(R.id.fragment_container, new MenuFragment(), "MenuFragment").addToBackStack(null).commit();
                    navigationView.setCheckedItem(R.id.nav_menu);
                }
                break;

            case R.id.nav_profile:
                ProfileFragment profileFragment = (ProfileFragment) getSupportFragmentManager().findFragmentByTag("ProfileFragment");
                if (profileFragment == null || !profileFragment.isVisible()) {
                    getSupportFragmentManager().beginTransaction()
                            .setCustomAnimations(R.anim.enter_right_to_left, R.anim.exit_right_to_left,
                                    R.anim.enter_left_to_right, R.anim.exit_left_to_right)
                            .replace(R.id.fragment_container, new ProfileFragment(), "ProfileFragment").addToBackStack(null).commit();
                    navigationView.setCheckedItem(R.id.nav_profile);
                }
                break;

            case R.id.nav_basket:
                CartFragment cartFragment = (CartFragment) getSupportFragmentManager().findFragmentByTag("CartFragment");
                if (cartFragment == null || !cartFragment.isVisible()) {
                    getSupportFragmentManager().beginTransaction()
                            .setCustomAnimations(R.anim.enter_right_to_left, R.anim.exit_right_to_left,
                                    R.anim.enter_left_to_right, R.anim.exit_left_to_right)
                            .replace(R.id.fragment_container, new CartFragment(), "CartFragment").addToBackStack(null).commit();
                    navigationView.setCheckedItem(R.id.nav_basket);
                }
                break;

            case R.id.nav_rating:
                RatingFragment ratingFragment = (RatingFragment) getSupportFragmentManager().findFragmentByTag("RatingFragment");
                if (ratingFragment == null || !ratingFragment.isVisible()) {
                    getSupportFragmentManager().beginTransaction()
                            .setCustomAnimations(R.anim.enter_right_to_left, R.anim.exit_right_to_left,
                                    R.anim.enter_left_to_right, R.anim.exit_left_to_right)
                            .replace(R.id.fragment_container, new RatingFragment(), "RatingFragment").addToBackStack(null).commit();
                    navigationView.setCheckedItem(R.id.nav_rating);
                }
                break;

            case R.id.nav_orders:
                OrderFragment orderFragment = (OrderFragment) getSupportFragmentManager().findFragmentByTag("OrderFragment");
                if (orderFragment == null || !orderFragment.isVisible()) {
                    getSupportFragmentManager().beginTransaction()
                            .setCustomAnimations(R.anim.enter_right_to_left, R.anim.exit_right_to_left,
                                    R.anim.enter_left_to_right, R.anim.exit_left_to_right)
                            .replace(R.id.fragment_container, new OrderFragment(), "OrderFragment").addToBackStack(null).commit();
                    navigationView.setCheckedItem(R.id.nav_orders);
                }
                break;

        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        OrderFragment orderFragment = (OrderFragment) getSupportFragmentManager().findFragmentByTag("OrderToFragment");
        if (orderFragment == null || !orderFragment.isVisible()) {
            if (drawer.isDrawerOpen(GravityCompat.START)) {
                drawer.closeDrawer(GravityCompat.START);
            } else
                super.onBackPressed();
        }
    }

    @Override
    public void onResultCode(Integer resultCode) {

    }

    @Override
    public void onResult(List<TeamModel> result) {

    }

    public void sendNotification() {
        String title = "Уведомление о заказе";
        String message = "Поступил новый заказ";

        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_cart_white)
                .setContentTitle(title)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .build();

        notificationManager.notify(1, notification);
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
