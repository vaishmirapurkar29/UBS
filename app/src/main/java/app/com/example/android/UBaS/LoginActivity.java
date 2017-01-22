package app.com.example.android.UBaS;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class LoginActivity extends AppCompatActivity {

    public static final String debugStr = "INTENT";
    private Button mLogin;
    private Button mRegistration;
    private EditText mEmail;
    private EditText mPassword;
    private TextView mPasswordReset;
    private Firebase mRef;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        mLogin = (Button) findViewById(R.id.login);
        mRegistration = (Button) findViewById(R.id.register);
        mEmail = (EditText) findViewById(R.id.email);
        mPassword = (EditText) findViewById(R.id.password);
        mPasswordReset = (TextView) findViewById(R.id.reset);

        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if(firebaseAuth.getCurrentUser() != null) {
                    Intent homeScreenIn = new Intent(LoginActivity.this, HomeScreen.class);
                    homeScreenIn.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(homeScreenIn);
                }
            }
        };

        mPasswordReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, ChangePassword.class));
            }
        });

        mLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Log.d(debugStr, "Hurray!");
//                Intent loginSuccess = new Intent(getApplicationContext(), HomeScreen.class);
//                startActivity(loginSuccess);
                startSignIn();
            }
        });

        mRegistration.setOnClickListener(new View.OnClickListener() {
            @Override
            public  void onClick(View v) {
                Intent toRegistration = new Intent(LoginActivity.this, RegistrationActivity.class);
                startActivity(toRegistration);
            }
        });



    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    private void startSignIn() {
        String email = mEmail.getText().toString();
        String password = mPassword.getText().toString();

        if(!email.contains("@mavs.uta.edu") && !email.isEmpty()) {
            Toast.makeText(LoginActivity.this, "Make sure you have UTA email", Toast.LENGTH_LONG).show();
            return;
        }

        if(TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
            Toast.makeText(LoginActivity.this, "Email and Password fields should not be empty", Toast.LENGTH_LONG).show();
            return;
        }


        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(!task.isSuccessful()) {
                    Toast.makeText(LoginActivity.this, "SignIn Problem", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
