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

import app.com.example.android.UBaS.DetailExchange;
import app.com.example.android.UBaS.ExchangeItem;
import app.com.example.android.UBaS.ExchangeItemInfo;
import app.com.example.android.UBaS.R;
import app.com.example.android.UBaS.SellItemInfo;
import app.com.example.android.UBaS.TakeItemPic;


/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment_Exchange extends Fragment {
    private GridView buyGrid;
    private ExchangeImageAdapter imageAdapter;
    private Button exchItem;
    int[] tempImg = {R.drawable.images};
    private SharedPreferences mSharedPreferences;
    private SharedPreferences.Editor mEditor;

    private DatabaseReference db;
    private ArrayList<ExchangeItemInfo> itemInfo = new ArrayList<>();
    private String[] childKeys = new String[100];
    private final String itemType = "exchangeItems";

    private int i = 0;

    public Fragment_Exchange() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = FirebaseDatabase.getInstance().getReference().child(itemType);

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView =  inflater.inflate(R.layout.fragment_exchange, container, false);
        buyGrid = (GridView) rootView.findViewById(R.id.gridView);
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        mEditor = mSharedPreferences.edit();
        exchItem = (Button) rootView.findViewById(R.id.exchange);
        exchItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent itemDesc = new Intent(getContext(), TakeItemPic.class);
                mEditor.putString(getResources().getString(R.string.prevFrag), "exchange").apply();
                startActivity(itemDesc);
            }
        });

        imageAdapter = new ExchangeImageAdapter(getContext(), dataRetrive(), childKeys, itemType);
        buyGrid.setAdapter(imageAdapter);
        buyGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            Intent exchangeIntent = null;
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                exchangeIntent = new Intent(getContext(), DetailExchange.class);
                exchangeIntent.putExtra("image",itemInfo.get(position).getImage());
                exchangeIntent.putExtra("category",itemInfo.get(position).getCategory());
                exchangeIntent.putExtra("description",itemInfo.get(position).getDescription());
                exchangeIntent.putExtra("email",itemInfo.get(position).getContactEmail());
                exchangeIntent.putExtra("terms",itemInfo.get(position).getTerms());
                startActivity(exchangeIntent);
            }
        });

        return rootView;
    }
    private void grabData(DataSnapshot dataSnapshot) {
        String key = dataSnapshot.getKey();
        ExchangeItemInfo info = (ExchangeItemInfo) dataSnapshot.getValue(ExchangeItemInfo.class);
        itemInfo.add(info);
        imageAdapter.notifyDataSetChanged();
    }

    public ArrayList<ExchangeItemInfo>  dataRetrive() {
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

            }
        });

        return itemInfo;
    }

}
