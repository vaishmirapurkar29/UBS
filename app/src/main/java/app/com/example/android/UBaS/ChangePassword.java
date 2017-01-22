package app.com.example.android.UBaS;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ChangePassword extends AppCompatActivity {

    private EditText mEmail;
    private Button mSend;
    private Button mCancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        mEmail = (EditText) findViewById(R.id.newEmail);
        mSend = (Button) findViewById(R.id.send);
        mCancel = (Button) findViewById(R.id.cancel);
        mSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = mEmail.getText().toString();
                if(TextUtils.isEmpty(email)) {
                    Toast.makeText(ChangePassword.this, "You must enter your valid email!", Toast.LENGTH_SHORT).show();
                    return;
                }
                FirebaseAuth auth = FirebaseAuth.getInstance();
                auth.sendPasswordResetEmail(email)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Log.d("ChangePassword", "Email sent.");
                                    startActivity(new Intent(ChangePassword.this, LoginActivity.class));
                                }
                                else {
                                    Toast.makeText(ChangePassword.this, "Error occured while submitting reset", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });


            }
        });
        mCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ChangePassword.this, LoginActivity.class));
            }
        });
    }
}
