package app.com.example.android.UBaS;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class ClubPost extends AppCompatActivity {

    private EditText mDescription;
    private Button mPost;
    private String postDes;
    private String toClub;
    private ProgressDialog mProgress;
    private SharedPreferences mSharedPreferences;
    private String resultAcTag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_club_post);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            actionBar.setTitle("New Post");
        }

        mProgress = new ProgressDialog(this);
        mDescription = (EditText) findViewById(R.id.description);
        mPost = (Button) findViewById(R.id.post);
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        toClub = mSharedPreferences.getString(getResources().getString(R.string.clubName), null);
//        Log.d("club", toClub);




        mPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                if(!TextUtils.isEmpty(postDes)) {
//                    Log.d("fadsf", "Inside");
                    postDes = mDescription.getText().toString();
                    post();
//                }
            }
        });

    }

    private void post() {

        mProgress.setMessage("Creating post ...");
        mProgress.show();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("users");
        Query userRef = reference.orderByChild("email").equalTo(FirebaseAuth.getInstance().getCurrentUser().getEmail());
        userRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                String memberName = dataSnapshot.child("fullname").getValue().toString();
                DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("clubPosts/"+toClub).push();
                ref.child("post").setValue(postDes);
                ref.child("fullname").setValue(memberName);
//                        Log.d("dflasdnfl", memberName);
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

        mProgress.dismiss();

        Intent clubIntent = new Intent(this, ClubDetail.class);
        clubIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(clubIntent);
    }
}
