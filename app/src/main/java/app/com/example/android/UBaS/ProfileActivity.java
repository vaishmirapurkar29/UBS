package app.com.example.android.UBaS;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class ProfileActivity extends AppCompatActivity {

    private Button mOk;
    private TextView mEmail;
    private TextView mName;
    private TextView mID;

    private DatabaseReference rootRef;
    private DatabaseReference tempRef;
    private String userEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        userEmail = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        rootRef = FirebaseDatabase.getInstance().getReference();
        tempRef = rootRef.child("users");
        mOk = (Button) findViewById(R.id.ok);
        mEmail = (TextView) findViewById(R.id.email);
        mName = (TextView) findViewById(R.id.name);
        mID = (TextView) findViewById(R.id.ID);
        mOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ProfileActivity.this, HomeScreen.class));
            }
        });
        Query query = tempRef.orderByChild("email").equalTo(userEmail);
        query.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                mName.setText(dataSnapshot.child("fullname").getValue().toString());
                mID.setText(dataSnapshot.child("utaId").getValue().toString());
                mEmail.setText(userEmail);
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
    }
}
