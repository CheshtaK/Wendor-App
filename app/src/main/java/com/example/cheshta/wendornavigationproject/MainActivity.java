package com.example.cheshta.wendornavigationproject;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.cheshta.wendornavigationproject.model.Offer;
import com.facebook.login.LoginManager;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageListener;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    FirebaseAuth mAuth;
    FirebaseUser mUser;

    TextView tvName, tvEmail, tvMainName;
    NavigationView navigationView;
    ImageView ivProfile, ivMachine, ivQuickPay;

    //Carousel
    CarouselView carouselView;
    int[] sampleImages = {R.drawable.image1, R.drawable.image2, R.drawable.image3, R.drawable.image4, R.drawable.image5};

    //OffersList
    private RecyclerView rvOffersList;
    DatabaseReference mOfferDatabase;
    FirebaseRecyclerAdapter<Offer, MainActivity.OfferViewHolder> firebaseOfferAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        
        tvMainName = findViewById(R.id.tvMainName);
        ivProfile = findViewById(R.id.ivProfile);
        ivMachine = findViewById(R.id.ivMachine);
        ivQuickPay = findViewById(R.id.ivQuickPay);

        navigationView = findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);
        tvName = headerView.findViewById(R.id.tvName);
        tvEmail = headerView.findViewById(R.id.tvEmail);

        mAuth = FirebaseAuth.getInstance();
        mUser = FirebaseAuth.getInstance().getCurrentUser();

        if(mUser != null){
            tvName.setText(mUser.getDisplayName());
            tvEmail.setText(mUser.getEmail());

            tvMainName.setText(mUser.getDisplayName());
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ivProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent profileIntent = new Intent(MainActivity.this, ProfileActivity.class);
                startActivity(profileIntent);
            }
        });

        ivMachine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent machineIntent = new Intent(MainActivity.this, MachineActivity.class);
                startActivity(machineIntent);
            }
        });

        ivQuickPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent payIntent = new Intent(MainActivity.this, QuickPayActivity.class);
                startActivity(payIntent);
            }
        });

        //Carousel
        carouselView = (CarouselView) findViewById(R.id.carouselView);
        carouselView.setPageCount(sampleImages.length);

        carouselView.setImageListener(imageListener);

        //OffersList
        rvOffersList = findViewById(R.id.rvOffersList);
        mOfferDatabase = FirebaseDatabase.getInstance().getReference().child("offers");
        mOfferDatabase.keepSynced(true);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rvOffersList.setHasFixedSize(true);
        rvOffersList.setLayoutManager(linearLayoutManager);
    }

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
        if (id == R.id.action_logout) {
            FirebaseAuth.getInstance().signOut();
            LoginManager.getInstance().logOut();
            Intent homeIntent = new Intent(MainActivity.this, MainActivity.class);
            startActivity(homeIntent);
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            Intent homeIntent = new Intent(MainActivity.this, MainActivity.class);
            startActivity(homeIntent);
        }
        else if (id == R.id.nav_offer) {
            Intent offerIntent = new Intent(MainActivity.this, OfferActivity.class);
            startActivity(offerIntent);
        }
        else if (id == R.id.nav_profile) {
            Intent profileIntent = new Intent(MainActivity.this, ProfileActivity.class);
            startActivity(profileIntent);
        }
        else if (id == R.id.nav_machine) {
            Intent machineIntent = new Intent(MainActivity.this, MachineActivity.class);
            startActivity(machineIntent);
        }
        else if (id == R.id.nav_quickpay) {
            Intent payIntent = new Intent(MainActivity.this, QuickPayActivity.class);
            startActivity(payIntent);
        }
        else if (id == R.id.nav_login) {
            Intent loginIntent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(loginIntent);
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    //Carousel
    ImageListener imageListener = new ImageListener() {
        @Override
        public void setImageForPosition(int position, ImageView imageView) {
            imageView.setImageResource(sampleImages[position]);
        }
    };


    //OffersList
    @Override
    protected void onStart() {
        super.onStart();

        Query query = mOfferDatabase.orderByKey();

        FirebaseRecyclerOptions<Offer> options = new FirebaseRecyclerOptions.Builder<Offer>()
                .setLifecycleOwner(this)
                .setQuery(query, Offer.class)
                .build();

        firebaseOfferAdapter = new FirebaseRecyclerAdapter<Offer, MainActivity.OfferViewHolder>(options) {
            @NonNull
            @Override
            public MainActivity.OfferViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.single_offer_layout,parent,false);
                return new MainActivity.OfferViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull final MainActivity.OfferViewHolder holder, int position, @NonNull Offer model) {
                String offerTitle = model.getTitle();
                String desc = model.getDesc();
                holder.setTitle(offerTitle);
                holder.setDescription(desc);
            }
        };

        rvOffersList.setAdapter(firebaseOfferAdapter);
    }

    private static class OfferViewHolder extends RecyclerView.ViewHolder{

        View mView;
        TextView tvOfferTitle, tvOfferDesc;

        public OfferViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
        }

        public void setTitle(String message){
            tvOfferTitle = mView.findViewById(R.id.tvOfferTitle);
            tvOfferTitle.setText(message);
        }

        public void setDescription(String message){
            tvOfferDesc = mView.findViewById(R.id.tvOfferDesc);
            tvOfferDesc.setText(message);
        }
    }
}
