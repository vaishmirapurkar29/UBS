package app.com.example.android.UBaS;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.vision.text.Text;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;


public class CreateClub extends AppCompatActivity {

    ImageButton addImageB;
    EditText clubName;
    EditText clubDescription;
    Button submitButton;

    private static final int  GalReq = 1000;
    private DatabaseReference dbRef;
    private DatabaseReference rootRef;
    private StorageReference mStorage;
    private Uri imgUri;
    private ProgressDialog mProgress;
    private String memberName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_club);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        addImageB = (ImageButton) findViewById(R.id.imageButton);
        clubName = (EditText) findViewById(R.id.name);
        clubDescription = (EditText) findViewById(R.id.description);
        submitButton = (Button) findViewById(R.id.button2);
        mProgress = new ProgressDialog(this);
        dbRef = FirebaseDatabase.getInstance().getReference().child("clubs");
        rootRef = FirebaseDatabase.getInstance().getReference().child("users");
        mStorage = FirebaseStorage.getInstance().getReference();
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(TextUtils.isEmpty(addImageB.toString()) || TextUtils.isEmpty(clubDescription.toString()) || TextUtils.isEmpty(clubName.toString())) {
                    Toast.makeText(CreateClub.this, "All fields should be filled", Toast.LENGTH_SHORT).show();
                    return;
                }
                postItem();
            }
        });
        addImageB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent galIntent = new Intent();
                galIntent.setType("image/*");
                galIntent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(galIntent, "Select Picture"), GalReq);
            }
        });
    }

    private void postItem() {
        final String description = clubDescription.getText().toString();
        final String name = clubName.getText().toString();
        mProgress.setMessage("Creating club ...");
        mProgress.show();
        StorageReference filePath = mStorage.child(name).child(imgUri.getLastPathSegment());
        filePath.putFile(imgUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Uri downloadUri = taskSnapshot.getDownloadUrl();
                DatabaseReference newPost = dbRef.push();
                newPost.child("clubName").setValue(name);
                newPost.child("description").setValue(description);
                newPost.child("image").setValue(downloadUri.toString());
                Query userRef = rootRef.orderByChild("email").equalTo(FirebaseAuth.getInstance().getCurrentUser().getEmail());
                userRef.addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        CreateClub.this.memberName = dataSnapshot.child("fullname").getValue().toString();
                        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("clubMembers/"+name);
                        ref.child("fullname").setValue(CreateClub.this.memberName);
//                        DatabaseReference dref = FirebaseDatabase.getInstance().getReference().child("clubMembers/"+name);
//                        dref.child("fullname").setValue("madeup name");
//                        Log.d("dflasdnfl", memberName);
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
                mProgress.dismiss();
                Intent clubIntent = new Intent(CreateClub.this, MyClubs.class);
                clubIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(clubIntent);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(CreateClub.this, "Error occured while uploading!", Toast.LENGTH_SHORT).show();
            }
        });


    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode ==  GalReq && resultCode == RESULT_OK) {
            imgUri= data.getData();
            Picasso.with(this).load(getPathFromURI(imgUri)).resize(350,250).rotate(90).centerInside().into(addImageB);
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
}
