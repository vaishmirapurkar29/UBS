package app.com.example.android.UBaS;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class DetailBuy extends AppCompatActivity {

    private ImageView mImage;
    private TextView mOwner;
    private TextView mDescription;
    private TextView mCategory;
    private TextView mPrice;
    private FloatingActionButton mChat;
    private FloatingActionButton mPay;

    private String ownerEmail;
    private String postId;

    private DatabaseReference db;
    private ArrayList<User> relevantUsers = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_buy);
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar_buy);
        setSupportActionBar(mToolbar);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        actionBar.setTitle("");
        mImage = (ImageView) findViewById(R.id.item_image);
        mOwner = (TextView) findViewById(R.id.ownerName);
        mDescription = (TextView) findViewById(R.id.description);
        mCategory = (TextView) findViewById(R.id.category);
        mPrice = (TextView) findViewById(R.id.price);
        mChat = (FloatingActionButton) findViewById(R.id.chatFab);
        mPay = (FloatingActionButton) findViewById(R.id.payFab);
        final String email = getIntent().getStringExtra("email");
        ownerEmail = email;
        mDescription.setText(getIntent().getStringExtra("description"));
        mCategory.setText(getIntent().getStringExtra("category"));
        mPrice.setText(getIntent().getStringExtra("price"));
        mChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent emailAct = new Intent(DetailBuy.this, EmailAct.class);
                emailAct.putExtra("owneremail", email);
                emailAct.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK );
                startActivity(emailAct);
            }
        });
        mPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent emailAct = new Intent(DetailBuy.this, PaymentActivity.class);
                startActivity(emailAct);
            }
        });
        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference().child("users");
        final Query query = myRef.orderByChild("email").equalTo(email);
        query.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                 mOwner.setText(dataSnapshot.child("fullname").getValue().toString());
            }
            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }
            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }
            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        Picasso.with(this).load(getIntent().getStringExtra("image")).resize(400,400).rotate(90).centerInside().into(mImage);
    }



}
