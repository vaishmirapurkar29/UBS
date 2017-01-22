package app.com.example.android.UBaS;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by MandipSilwal on 11/29/16.
 */

public class CustomAdapter extends BaseAdapter{

    ArrayList<ClubPostInfo> clubPostInfos;
    Context context;

    public CustomAdapter(ArrayList<ClubPostInfo> clubPostInfos, Context context) {
        this.clubPostInfos = clubPostInfos;
        this.context = context;
    }
    @Override
    public int getCount() {
        return clubPostInfos.size();
    }
    @Override
    public Object getItem(int i) {
        return clubPostInfos.get(i);
    }
    @Override
    public long getItemId(int i) {
        return i;
    }
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if(view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.singlepost, viewGroup, false);
        }
        TextView fullname = (TextView) view.findViewById(R.id.poster);
        TextView post = (TextView) view.findViewById(R.id.postInfo);
        final ClubPostInfo info = (ClubPostInfo) this.getItem(i);
        fullname.setText(info.getFullname());
        post.setText(info.getPost());
        return view;
    }
}
