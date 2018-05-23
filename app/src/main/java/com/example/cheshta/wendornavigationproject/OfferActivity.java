package com.example.cheshta.wendornavigationproject;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.cheshta.wendornavigationproject.model.Offer;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class OfferActivity extends AppCompatActivity {

    private RecyclerView rvOffersList;
    Toolbar offerToolbar;

    private DatabaseReference mOfferDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offer);

        rvOffersList = findViewById(R.id.rvOffersList);
        mOfferDatabase = FirebaseDatabase.getInstance().getReference().child("Offers");
        mOfferDatabase.keepSynced(true);

        offerToolbar = findViewById(R.id.offerToolbar);
        setSupportActionBar(offerToolbar);
        getSupportActionBar().setTitle("Offers");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rvOffersList.setHasFixedSize(true);
        rvOffersList.setLayoutManager(linearLayoutManager);
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<Offer> options = new FirebaseRecyclerOptions.Builder<Offer>()
                .setQuery(mOfferDatabase, Offer.class)
                .build();

        FirebaseRecyclerAdapter<Offer, OfferViewHolder> firebaseOfferAdapter = new FirebaseRecyclerAdapter<Offer, OfferViewHolder>(options) {
            @NonNull
            @Override
            public OfferViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.single_offer_layout,parent,false);
                return new OfferViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull final OfferViewHolder holder, int position, @NonNull Offer model) {
                String offerTitle = model.getOfferTitle();
                holder.setTitle(offerTitle);
            }
        };

        rvOffersList.setAdapter(firebaseOfferAdapter);
    }

    public static class OfferViewHolder extends RecyclerView.ViewHolder{

        View mView;

        public OfferViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
        }

        public void setTitle(String message){
            TextView tvOfferTitle = mView.findViewById(R.id.tvOfferTitle);
            tvOfferTitle.setText(message);
        }
    }
}
