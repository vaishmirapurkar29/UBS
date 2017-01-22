package app.com.example.android.UBaS;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;

public class SellPost extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    private ImageView itemImage;
    private Button poster;
    private EditText descriptionText;
    private Spinner itemSelects;
    private String imagePath;
    private EditText priceText;
    private AutoCompleteTextView textView;

    private StorageReference mStorage;
    private DatabaseReference mDatabase;
    private Uri mImageUri = null;
    private ProgressDialog mProgress;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sell_post);
        mStorage = FirebaseStorage.getInstance().getReference();
        DatabaseReference tempRef = FirebaseDatabase.getInstance().getReference();
        mDatabase = tempRef.child("sellItems");
        mProgress = new ProgressDialog(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        descriptionText = (EditText) findViewById(R.id.description);
        priceText = (EditText) findViewById(R.id.price);
        textView = (AutoCompleteTextView) findViewById(R.id.edit);
        itemImage = (ImageView) findViewById(R.id.userImage);
        poster = (Button) findViewById(R.id.post);
        String[] categories = getResources().getStringArray(R.array.planets_array);
        textView = (AutoCompleteTextView) findViewById(R.id.edit);
        ArrayAdapter<CharSequence> adapter = new ArrayAdapter(this,
                android.R.layout.simple_dropdown_item_1line, categories);
        textView.setAdapter(adapter);
        imagePath = getIntent().getStringExtra("fileUri");
        mImageUri = Uri.parse(getIntent().getStringExtra("URI"));
        poster.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                postItem();
            }
        });

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // On selecting a spinner item
        String item = parent.getItemAtPosition(position).toString();
        Toast.makeText(parent.getContext(), "Selected: " + item, Toast.LENGTH_LONG).show();

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    protected void onResume() {
        super.onResume();
        // bimatp factory
        BitmapFactory.Options options = new BitmapFactory.Options();
//         downsizing image as it throws OutOfMemory Exception for larger
//         images
        options.inSampleSize = 8;

        final Bitmap bitmap = BitmapFactory.decodeFile(imagePath,options);   // GOT THE PIC. USE INTENT TO NEW ACITIVITY
        itemImage.setImageBitmap(imageOreintationValidator(bitmap, imagePath));
    }

    private void postItem() {


        final String description = descriptionText.getText().toString();
        final String category = textView.getText().toString();
        final String price = priceText.getText().toString();

        if(TextUtils.isEmpty(description) || TextUtils.isEmpty(category) || TextUtils.isEmpty(price)) {
            Toast.makeText(SellPost.this, "All the fields should be filled!", Toast.LENGTH_SHORT).show();
            return;
        }
        mProgress.setMessage("Posting your item ...");
        mProgress.show();
        StorageReference filePath = mStorage.child("sellItems").child(mImageUri.getLastPathSegment());
        filePath.putFile(mImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {


                Uri downloadUri = taskSnapshot.getDownloadUrl();
                DatabaseReference newPost = mDatabase.push();
                newPost.child("category").setValue(category);
                newPost.child("price").setValue(price);
                newPost.child("description").setValue(description);
                newPost.child("image").setValue(downloadUri.toString());
                newPost.child("contactEmail").setValue(FirebaseAuth.getInstance().getCurrentUser().getEmail());

                mProgress.dismiss();

//                Log.d("Inside post", "posting");

                Intent homeScreenIntent = new Intent(SellPost.this, HomeScreen.class);
                homeScreenIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(homeScreenIntent);
                finish();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(SellPost.this, "Error occured!", Toast.LENGTH_SHORT).show();
            }
        });


    }

    private Bitmap imageOreintationValidator(Bitmap bitmap, String path) {

        ExifInterface ei;
        try {
            ei = new ExifInterface(path);
            int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    bitmap = rotateImage(bitmap, 90);
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    bitmap = rotateImage(bitmap, 180);
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    bitmap = rotateImage(bitmap, 270);
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return bitmap;
    }


    private Bitmap rotateImage(Bitmap source, float angle) {

        Bitmap bitmap = null;
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        try {
            bitmap = Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(),
                    matrix, true);
        } catch (OutOfMemoryError err) {
            err.printStackTrace();
        }
        return bitmap;
    }

}
