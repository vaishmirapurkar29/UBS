package buyBorrowExchange;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import app.com.example.android.UBaS.R;
import app.com.example.android.UBaS.SellItemInfo;

/**
 * Created by MandipSilwal on 11/16/16.
 */

public class SellImageAdapter extends BaseAdapter{

    private Context context;
    private ArrayList<SellItemInfo> dataPaths;
    private String[] childKeys;
    private String itemType;

    public SellImageAdapter(Context context, ArrayList<SellItemInfo> dataPaths, String[] childKeys, String type) {
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
