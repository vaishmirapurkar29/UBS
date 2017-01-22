package buyBorrowExchange;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import app.com.example.android.UBaS.DetailBorrow;
import app.com.example.android.UBaS.LendItemInfo;
import app.com.example.android.UBaS.R;
import app.com.example.android.UBaS.SellItemInfo;
import app.com.example.android.UBaS.TakeItemPic;


/**
 * A simple {@link Fragment} subclass.
 */


public class Fragment_Borrow extends Fragment {

    private GridView buyGrid;
    private LendImageAdapter imageAdapter;
    private Button lendItem;
    int[] tempImg = {R.drawable.images};
    private SharedPreferences mSharedPreferences;
    private SharedPreferences.Editor mEditor;

    private DatabaseReference db;
    private ArrayList<LendItemInfo> itemInfo = new ArrayList<>();
    private String[] childKeys = new String[100];
    private final String itemType = "lendItems";

    private int i = 0;


    public Fragment_Borrow() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = FirebaseDatabase.getInstance().getReference();
        db = db.child(itemType);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView =  inflater.inflate(R.layout.fragment_borrow, container, false);
        buyGrid = (GridView) rootView.findViewById(R.id.gridView);
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        mEditor = mSharedPreferences.edit();


        lendItem = (Button) rootView.findViewById(R.id.lend);
        lendItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent itemDesc = new Intent(getContext(), TakeItemPic.class);
                mEditor.putString(getResources().getString(R.string.prevFrag), "borrow").apply();
//                itemDesc.putExtra("Frag", "borrow");
                startActivity(itemDesc);
            }
        });

        imageAdapter = new LendImageAdapter(getContext(), dataRetrive(), childKeys, itemType);
        buyGrid.setAdapter(imageAdapter);

        buyGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            Intent borrowIntent = null;

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Transition to DetailFragment
                borrowIntent = new Intent(getContext(), DetailBorrow.class);
                borrowIntent.putExtra("image",itemInfo.get(position).getImage());
                borrowIntent.putExtra("category",itemInfo.get(position).getCategory());
                borrowIntent.putExtra("description",itemInfo.get(position).getDescription());
                borrowIntent.putExtra("email",itemInfo.get(position).getContactEmail());
                borrowIntent.putExtra("terms",itemInfo.get(position).getTerms());
                startActivity(borrowIntent);
            }
        });

        return rootView;
    }

    private void grabData(DataSnapshot dataSnapshot) {

//        itemInfo.clear();


//        Log.d("Fragmnet_buy", dataSnapshot.getChildrenCount()+"");
//        childKeys = new String[(int)dataSnapshot.getChildrenCount()];

        String key = dataSnapshot.getKey();
        LendItemInfo info = (LendItemInfo) dataSnapshot.getValue(LendItemInfo.class);
        itemInfo.add(info);

        imageAdapter.notifyDataSetChanged();

    }

    public ArrayList<LendItemInfo>  dataRetrive() {
        itemInfo.clear();

        db.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for(DataSnapshot ds : dataSnapshot.getChildren()) {
                    if(ds.exists()) {
                        grabData(ds);

                    }
                }
                childKeys[i] = dataSnapshot.getKey();

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("error", "error!!!");
            }
        });

        return itemInfo;
    }

}
