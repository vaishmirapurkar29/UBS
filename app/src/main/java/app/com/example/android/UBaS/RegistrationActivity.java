package app.com.example.android.UBaS;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import static app.com.example.android.UBaS.R.id.fullname;

public class RegistrationActivity extends AppCompatActivity {

    private EditText mEmail;
    private EditText mFullname;
    private EditText mPassword;
    private EditText mUtaId;
    private Button register;

    private DatabaseReference mFirebaseDatabase;
    private FirebaseDatabase mFirebaseInstance;

    private FirebaseAuth mAuth;

    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_registration);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        mFirebaseInstance = FirebaseDatabase.getInstance();

        // get reference to 'users' node
        mFirebaseDatabase = mFirebaseInstance.getReference("users");
        mAuth = FirebaseAuth.getInstance();


        mEmail = (EditText) findViewById(R.id.email);
        mFullname = (EditText) findViewById(fullname);
        mPassword = (EditText) findViewById(R.id.password);
        mUtaId = (EditText) findViewById(R.id.utaID);
        register = (Button) findViewById(R.id.register);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register();
            }
        });
    }

    private void register() {
        final String email = mEmail.getText().toString().trim();
        final String fullname = mFullname.getText().toString().trim();
        final String password = mPassword.getText().toString().trim();
        final String utaId = mUtaId.getText().toString();


        //checking if email and passwords are empty
        if(TextUtils.isEmpty(email) || TextUtils.isEmpty(fullname) || TextUtils.isEmpty(password) || TextUtils.isEmpty(utaId)){
            Toast.makeText(this,"Please fill all the fields",Toast.LENGTH_LONG).show();
            return;
        }

        if(password.length() < 6) {
            Toast.makeText(this, "Please enter more than 5 characters for password", Toast.LENGTH_SHORT).show();
            return;
        }
//        if(TextUtils.isEmpty(userId)) {
//            if (TextUtils.isEmpty(userId)) {
//                userId = mFirebaseDatabase.push().getKey();
//            }
//
//            User user = new User(fullname, email, password, utaId);
//
//            mFirebaseDatabase.child(userId).setValue(user);
//
////            addUserChangeListener();
//            Toast.makeText(this, "You are now a member!", Toast.LENGTH_LONG).show();
//        }
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(RegistrationActivity.this, "You are now a member!", Toast.LENGTH_LONG).show();
                    if (TextUtils.isEmpty(userId)) {
                        userId = mFirebaseDatabase.push().getKey();
                    }
                    User user = new User(fullname, email, password, utaId);
                    mFirebaseDatabase.child(userId).setValue(user);
                    Intent homeScreen = new Intent(RegistrationActivity.this, HomeScreen.class);
                    homeScreen.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(homeScreen);
                    finish();

                } else {
                    Toast.makeText(RegistrationActivity.this, "Sorry, the email address is used already!", Toast.LENGTH_LONG).show();
                    mEmail.setText("");
                }
            }
        });
    }



}
