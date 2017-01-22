package buyBorrowExchange;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.SyncStateContract;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;

import com.google.common.collect.ArrayListMultimap;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import app.com.example.android.UBaS.ChildKeys;
import app.com.example.android.UBaS.DetailBuy;
import app.com.example.android.UBaS.R;
import app.com.example.android.UBaS.SellItemInfo;
import app.com.example.android.UBaS.TakeItemPic;


/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment_Buy extends Fragment {

    private GridView buyGrid;
    private SellImageAdapter imageAdapter;
    private Button sellItem;
    int[] tempImg = {R.drawable.images};

    private SharedPreferences mSharedPreferences;
    private SharedPreferences.Editor mEditor;
    private DatabaseReference db;
    private DatabaseReference workRef;
    private ArrayList<SellItemInfo> itemInfo = new ArrayList<>();
    private String[] childKeys = new String[100];
    private final String itemType = "sellItems";

    private int i = 0;

    public Fragment_Buy() {
        // Required empty public constructorp
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        db = FirebaseDatabase.getInstance().getReference();
        workRef = db.child(itemType);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView =  inflater.inflate(R.layout.fragment_buy, container, false);
        buyGrid = (GridView) rootView.findViewById(R.id.gridView);
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        mEditor = mSharedPreferences.edit();
        sellItem = (Button) rootView.findViewById(R.id.sell);
        sellItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent itemDesc = new Intent(getContext(), TakeItemPic.class);
                mEditor.putString(getResources().getString(R.string.prevFrag), "buy").apply();
                startActivity(itemDesc);
            }
        });
        imageAdapter = new SellImageAdapter(getContext(), dataRetrive(), childKeys, itemType);
        buyGrid.setAdapter(imageAdapter);
        buyGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            Intent buyIntent = null;
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Transition to DetailFragment
                buyIntent = new Intent(getContext(), DetailBuy.class);
                buyIntent.putExtra("image",itemInfo.get(position).getImage());
                buyIntent.putExtra("category",itemInfo.get(position).getCategory());
                buyIntent.putExtra("description",itemInfo.get(position).getDescription());
                buyIntent.putExtra("email",itemInfo.get(position).getContactEmail());
                buyIntent.putExtra("price",itemInfo.get(position).getPrice());
                startActivity(buyIntent);
            }
        });

        return rootView;
    }
    private void grabData(DataSnapshot dataSnapshot) {
        String key = dataSnapshot.getKey();
        SellItemInfo info = new SellItemInfo();
        info.setContactEmail(dataSnapshot.child("contactEmail").getValue(String.class));
        info.setCategory(dataSnapshot.child("category").getValue(String.class));
        info.setDescription(dataSnapshot.child("description").getValue(String.class));
        info.setImage(dataSnapshot.child("image").getValue(String.class));
        info.setPrice(dataSnapshot.child("price").getValue(String.class));
        itemInfo.add(info);
        imageAdapter.notifyDataSetChanged();
    }
    public ArrayList<SellItemInfo>  dataRetrive() {
        itemInfo.clear();
        workRef.addListenerForSingleValueEvent(new ValueEventListener() {
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
            }
        });

        return itemInfo;
    }
}
