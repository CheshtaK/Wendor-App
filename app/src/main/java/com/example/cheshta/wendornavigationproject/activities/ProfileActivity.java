package com.example.cheshta.wendornavigationproject.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.cheshta.wendornavigationproject.R;
import com.example.cheshta.wendornavigationproject.model.Offer;
import com.example.cheshta.wendornavigationproject.model.Transaction;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseUser mUser;

    private Toolbar profileToolbar;
    private CircleImageView civProfileImage;
    private TextView tvProfileName, tvProfileEmail;

    //Transaction List
    private RecyclerView rvTransactionList;
    private DatabaseReference mTransactionDatabase;
    private FirebaseRecyclerAdapter<Transaction, TransactionViewHolder> firebaseTransactionAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        civProfileImage = findViewById(R.id.civProfileImage);
        tvProfileName = findViewById(R.id.tvProfileName);
        tvProfileEmail = findViewById(R.id.tvProfileEmail);

        mAuth = FirebaseAuth.getInstance();
        mUser = FirebaseAuth.getInstance().getCurrentUser();

        profileToolbar = findViewById(R.id.profileToolbar);
        setSupportActionBar(profileToolbar);
        getSupportActionBar().setTitle("Profile");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if(mUser != null){
            tvProfileName.setText(mUser.getDisplayName());
            tvProfileEmail.setText(mUser.getEmail());

            String url = mUser.getPhotoUrl().toString();
            Picasso.get().load(url).placeholder(R.drawable.me).into(civProfileImage);
        }

        //Transaction List
        rvTransactionList = findViewById(R.id.rvTransactionList);
        mTransactionDatabase = FirebaseDatabase.getInstance().getReference().child("transactions");
        mTransactionDatabase.keepSynced(true);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rvTransactionList.setHasFixedSize(true);
        rvTransactionList.setLayoutManager(linearLayoutManager);
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser == null){
            displayAlertMessage("You need to be logged in to view your profile",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent loginIntent = new Intent(ProfileActivity.this, LoginActivity.class);
                            startActivity(loginIntent);
                            finish();
                        }
                    });
        }


        //Transaction List
        Query query = mTransactionDatabase.orderByKey();

        FirebaseRecyclerOptions<Transaction> options = new FirebaseRecyclerOptions.Builder<Transaction>()
                .setLifecycleOwner(this)
                .setQuery(query, Transaction.class)
                .build();

        firebaseTransactionAdapter = new FirebaseRecyclerAdapter<Transaction, TransactionViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull TransactionViewHolder holder, int position, @NonNull Transaction model) {
                holder.setDate(model.getDate());
                holder.setDetail(model.getTransactionDetail());
                holder.setPrice(model.getPrice());
            }

            @NonNull
            @Override
            public TransactionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.single_transaction_layout,parent,false);
                return new TransactionViewHolder(view);
            }
        };

        rvTransactionList.setAdapter(firebaseTransactionAdapter);
    }

    private static class TransactionViewHolder extends RecyclerView.ViewHolder{

        View mView;
        TextView tvDate, tvDetail, tvPrice;

        public TransactionViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
        }

        public void setDate(String date){
            tvDate = mView.findViewById(R.id.tvDate);
            tvDate.setText(date);
        }

        public void setDetail(String detail){
            tvDetail = mView.findViewById(R.id.tvDetail);
            tvDetail.setText(detail);
        }

        public void setPrice(String price){
            tvPrice = mView.findViewById(R.id.tvPrice);
            tvPrice.setText(price);
        }
    }

    public void displayAlertMessage(String message, DialogInterface.OnClickListener listener){
        new AlertDialog.Builder(ProfileActivity.this)
                .setMessage(message)
                .setPositiveButton("LOGIN",listener)
                .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent homeIntent = new Intent(ProfileActivity.this, MainActivity.class);
                        startActivity(homeIntent);
                        finish();
                    }
                })
                .create()
                .show();
    }
}
