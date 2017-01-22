package buyBorrowExchange;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import app.com.example.android.UBaS.LendItem;
import app.com.example.android.UBaS.LendItemInfo;
import app.com.example.android.UBaS.R;
import app.com.example.android.UBaS.SellItemInfo;

/**
 * Created by MandipSilwal on 11/16/16.
 */

public class LendImageAdapter extends BaseAdapter{
    private Context context;
    private ArrayList<LendItemInfo> dataPaths;
    private String[] childKeys;
    private String itemType;
    public LendImageAdapter(Context context, ArrayList<LendItemInfo> dataPaths, String[] childKeys, String type) {
        this.context = context;
        this.dataPaths = dataPaths;
        this.childKeys = childKeys;
        this.itemType = type;
    }
    @Override
    public int getCount() {
        return dataPaths.size();
    }
    @Override
    public Object getItem(int position) {

        return dataPaths.get(position);
    }
    @Override
    public long getItemId(int position) {
        return position;
    }
    class ViewHolder{

        ImageView iView;
        ViewHolder(ImageView img) {
            iView = img;
        }

        ImageView getImageView() {
            return iView;
        }

    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView = (ImageView) convertView;
        ViewHolder holder = null;
        if(imageView == null) {
            LayoutInflater inflater = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            imageView = (ImageView) inflater.inflate(R.layout.grid_item,parent,false);
            holder = new ViewHolder(imageView);
            imageView.setTag(holder);
        }
        else {
            holder = (ViewHolder) imageView.getTag();
        }
        Picasso.
                with(context)
                .load(dataPaths.get(position).getImage()).resize(400,400).rotate(90).centerInside()
                .into(holder.getImageView());
        return imageView;
    }
}
