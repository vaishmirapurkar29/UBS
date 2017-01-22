package app.com.example.android.UBaS;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import static android.R.id.list;
import static android.icu.lang.UCharacter.GraphemeClusterBreak.L;

public class ClubDetail extends AppCompatActivity {

    private String clubName;

    private RecyclerView mRView;
    private DatabaseReference mRoot;
    private DatabaseReference tempRef;
    private CustomAdapter adapter;
    private ArrayList<ClubPostInfo> listInfo = listInfo = new ArrayList<>();;
    private ListView listView;
    private SharedPreferences mSharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_club_detail);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            actionBar.setTitle(clubName);
        }

        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        this.clubName = mSharedPreferences.getString(getResources().getString(R.string.clubName), null);

        listInfo = new ArrayList<>();

        listView = (ListView) findViewById(R.id.listView1);
        mRoot = FirebaseDatabase.getInstance().getReference();
        tempRef = mRoot.child("clubPosts").child(this.clubName);
//        mRoot.keepSynced(true);

        adapter = new CustomAdapter(dataRetrive(), this);
//        adapter.notifyDataSetChanged();
        listView.setAdapter(adapter);

    }

    private void grabData(DataSnapshot dataSnapshot) {

//        itemInfo.clear();


//        Log.d("Fragmnet_buy", dataSnapshot.getChildrenCount()+"");
//        childKeys = new String[(int)dataSnapshot.getChildrenCount()];

        String key = dataSnapshot.getKey();
//        ClubPostInfo info = new ClubPostInfo();
//        info.setFullname(dataSnapshot.child("fullname").getValue(String.class));
//        Log.d("name", info.getFullname());
//        Log.d("psot", info.getPost());
//        info.setPost(dataSnapshot.child("post").getValue(String.class));
//        for (DataSnapshot ds : dataSnapshot.getChildren()) {
            ClubPostInfo info = (ClubPostInfo) dataSnapshot.getValue(ClubPostInfo.class);
            Log.d("name", info.getFullname());
            listInfo.add(info);
//        }
        adapter.notifyDataSetChanged();

//
    }

    public ArrayList<ClubPostInfo> dataRetrive() {
        listInfo.clear();

        tempRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for(DataSnapshot ds : dataSnapshot.getChildren()) {

                    if(ds.exists()) {
//                        Log.d("tafasdf", ds.getKey());
                        grabData(ds);
                    }
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("eoor", "deeee");
            }
        });

        return listInfo;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
//        Log.d("dfasdnflasd","gayo");
        MenuInflater inf = getMenuInflater();
        inf.inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        if(item.getItemId() == R.id.action_add) {


            Intent newPost = new Intent(ClubDetail.this, ClubPost.class);
            newPost.putExtra("club", clubName);
            startActivity(newPost);

        }
        return super.onOptionsItemSelected(item);
    }

}
