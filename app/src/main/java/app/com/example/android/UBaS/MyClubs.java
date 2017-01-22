package app.com.example.android.UBaS;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.ContentFrameLayout;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.firebase.client.Firebase;
import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;
import com.squareup.otto.ThreadEnforcer;

import java.util.ArrayList;

import static app.com.example.android.UBaS.R.id.fullname;

public class MyClubs extends AppCompatActivity {

    DrawerLayout mDrawerLayout;
    NavigationView mNavigationView;

    private ListView listView;
    public String userName;
    public ArrayList<String> clubs;
    public ArrayAdapter<String> adapter;
    int count =0;
    public String[] name = new String[1];
    private DatabaseReference databaseReference;

    private SharedPreferences mSharedPreferences;
    private SharedPreferences.Editor mEditor;

    public static Bus bus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_my_clubs);
        bus = new Bus(ThreadEnforcer.MAIN);
        bus.register(this);


        listView = (ListView) findViewById(R.id.listView) ;

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        mNavigationView = (NavigationView) findViewById(R.id.navView) ;


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        mEditor = mSharedPreferences.edit();

        databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://ubas-17afa.firebaseio.com/clubsMembers");

        DatabaseReference userNameRef = FirebaseDatabase.getInstance().getReferenceFromUrl("https://ubas-17afa.firebaseio.com/users");
        final Query query = userNameRef.orderByChild("email").equalTo(FirebaseAuth.getInstance().getCurrentUser().getEmail());

        query.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Log.d("name",dataSnapshot.child("fullname").getValue().toString());
                name[0] = dataSnapshot.child("fullname").getValue().toString();
                bus.post(name[0]);
                nextMethod(name);
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

        clubs  = new ArrayList<>();




        /**
         * Setup click events on the Navigation View Items.
         */

        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                mDrawerLayout.closeDrawers();


                if (menuItem.getItemId() == R.id.nav_first_fragment) {

                    Intent homescreenIntent = new Intent(MyClubs.this, HomeScreen.class);
                    startActivity(homescreenIntent);

//                    FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
//                    fragmentTransaction.replace(R.id.containerView,new TabFragment()).commit();

                }
                else if(menuItem.getItemId() == R.id.nav_third_fragment) {

                }

                else if(menuItem.getItemId() == R.id.nav_fourth_fragment) {
                    startActivity(new Intent(MyClubs.this, ProfileActivity.class));
                }
                else if (menuItem.getItemId() == R.id.nav_sixth_fragment) {
                    FirebaseAuth.getInstance().signOut();
                    Intent redirectLogin = new Intent(MyClubs.this, LoginActivity.class);
                    redirectLogin.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(redirectLogin);
                    finish();

                }


                return false;
            }

        });

//        android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);
        ActionBarDrawerToggle mDrawerToggle = new ActionBarDrawerToggle(this,mDrawerLayout, toolbar,R.string.app_name,
                R.string.app_name);

        mDrawerLayout.addDrawerListener(mDrawerToggle);

        mDrawerToggle.syncState();
    }

    @Subscribe
    public void nextMethod(String[] name) {
//        Log.d("kjdfnjadsnfsd", name[0] + "");
        Query memberQuery = FirebaseDatabase.getInstance().getReference().child("clubMembers").orderByChild("fullname").equalTo(name[0]);
        memberQuery.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                clubs.add(dataSnapshot.getKey());
                bus.post(clubs);
                thisMethod(clubs);
                Log.d("club", dataSnapshot.getKey());
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

    @Subscribe
    public void thisMethod(final ArrayList<String> clubs) {

//        for(int i = 0; i< clubs.size(); i ++) {
//            Log.d("club", clubs.get(i));
//        }

        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, clubs);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent detailIntent = new Intent(MyClubs.this, ClubDetail.class);
                mEditor.putString(getResources().getString(R.string.clubName), clubs.get(i)).apply();
                // Extras go here!
                startActivity(detailIntent);
            }
        });

        adapter.notifyDataSetChanged();
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



            Intent addClubIntent = new Intent(MyClubs.this, CreateClub.class);

            startActivity(addClubIntent);

        }
        return super.onOptionsItemSelected(item);
    }

    public void setUserName(String user) {
        this.userName = user;
    }
}
