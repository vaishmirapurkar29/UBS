package app.com.example.android.UBaS;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.util.Date;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Locale;

import static android.provider.MediaStore.Files.FileColumns.PARENT;

public class TakeItemPic extends AppCompatActivity implements View.OnClickListener {


    Button choiceGallery;
    Button choiceCamera;
    private static final int CAMERA_REQUEST = 1888;
    private static final int GALLERY_REQUEST = 1900;
    private static final String IMAGE_DIRECTORY_NAME = "Suhhh duuude";
    public static final int MEDIA_TYPE_IMAGE = 1;
    public static final int MEDIA_TYPE_VIDEO = 2;
    private static int tabIdx;
    Context context = this;
    String resultAcTag;
    Uri tempUri;
    File imageFile;
    private int keeper;
    private SharedPreferences mSharedPreferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_pic);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        resultAcTag = mSharedPreferences.getString(getResources().getString(R.string.prevFrag), null);
        Log.d("OnCREATE", resultAcTag);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        choiceGallery = (Button) findViewById(R.id.chooseGallery);
        choiceCamera = (Button) findViewById(R.id.chooseCamera);
        choiceGallery.setOnClickListener(this);
        choiceCamera.setOnClickListener(this);
    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable("file_uri", tempUri);
        outState.putCharSequence("resultActivity", resultAcTag);
    }
    @Override
    protected void onRestart() {
        super.onRestart();
        String storeTag = resultAcTag;
        resultAcTag = storeTag;
    }
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        tempUri = savedInstanceState.getParcelable("file_uri");
        resultAcTag = savedInstanceState.getString("resultActivity");
    }
    @Override
    public void onClick(View v) {
        Intent camIntent = null;
        Intent galleryIntent = null;
        if (v.getId() == R.id.chooseGallery) {
            galleryIntent = new Intent();
            galleryIntent.setType("image/*");
            galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(galleryIntent, "Select Picture"), GALLERY_REQUEST);
        }
        if (v.getId() == R.id.chooseCamera) {
            camIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            tempUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);
            camIntent.putExtra(MediaStore.EXTRA_OUTPUT, tempUri);
            startActivityForResult(camIntent, CAMERA_REQUEST);
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK){
            if (requestCode == CAMERA_REQUEST) {
                try {
                    switch (resultAcTag) {
                        case "buy":
                            Intent sellPost = new Intent(this, SellPost.class);
                            sellPost.putExtra("fileUri", tempUri.getPath());
                            sellPost.putExtra("URI", tempUri+"");
                            startActivity(sellPost);
                            break;
                        case "borrow":
                            Intent lendPost = new Intent(this, LendPost.class);
                            lendPost.putExtra("fileUri", tempUri.getPath());
                            lendPost.putExtra("URI", tempUri+"");
                            startActivity(lendPost);
                            break;
                        case "exchange":
                            Intent exchangePost = new Intent(this, ExchangePost.class);
                            exchangePost.putExtra("fileUri", tempUri.getPath());
                            exchangePost.putExtra("URI", tempUri+"");
                            startActivity(exchangePost);
                            break;
                    }
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
            }
            else if(requestCode == GALLERY_REQUEST) {
                Uri selectedImageUri = data.getData();
                if (null != selectedImageUri) {
                    String path = getPathFromURI(selectedImageUri);
                    try {
                        switch (resultAcTag) {
                            case "buy":
                                Intent sellPost = new Intent(this, SellPost.class);
                                sellPost.putExtra("fileUri", path);
                                sellPost.putExtra("URI", selectedImageUri+"");
                                startActivity(sellPost);
                                break;
                            case "borrow":
                                Intent lendPost = new Intent(this, LendPost.class);
                                lendPost.putExtra("fileUri",path);
                                lendPost.putExtra("URI", selectedImageUri+"");
                                startActivity(lendPost);
                                break;
                            case "exchange":
                                Intent exchangePost = new Intent(this, ExchangePost.class);
                                exchangePost.putExtra("fileUri", path);
                                exchangePost.putExtra("URI", selectedImageUri+"");
                                startActivity(exchangePost);
                                break;
                        }
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    private String getPathFromURI(Uri content) {
        String res = null;
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(content, proj, null, null, null);
        if (cursor.moveToFirst()) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            res = cursor.getString(column_index);
        }
        cursor.close();
        return res;
    }



    /**
     * Creating file uri to store image/video
     */
    public Uri getOutputMediaFileUri(int type) {
        return Uri.fromFile(getOutputMediaFile(type));
    }

    //
    private static File getOutputMediaFile(int type) {

        // External sdcard location
        File mediaStorageDir = new File(
                Environment
                        .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                IMAGE_DIRECTORY_NAME);

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d(IMAGE_DIRECTORY_NAME, "Oops! Failed create "
                        + IMAGE_DIRECTORY_NAME + " directory");
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
                Locale.getDefault()).format(new Date());
        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator
                    + "IMG_" + timeStamp + ".jpg");
        } else if (type == MEDIA_TYPE_VIDEO) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator
                    + "VID_" + timeStamp + ".mp4");
        } else {
            return null;
        }

        return mediaFile;
    }
}
