package app.com.example.android.UBaS;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
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

import static android.icu.lang.UCharacter.GraphemeClusterBreak.T;

public class EmailAct extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_email);
        final String ownerEmail = getIntent().getStringExtra("owneremail");
        final EditText subjectField = (EditText) findViewById(R.id.subject);
        final EditText messageField = (EditText) findViewById(R.id.message);
        final Button sendButton = (Button) findViewById(R.id.send);
        Button cancelButton = (Button) findViewById(R.id.cancel);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String subject = subjectField.getText().toString();
                String message = messageField.getText().toString();
                if(!TextUtils.isEmpty(subject) && !TextUtils.isEmpty(message)) {
                    Intent sendEmail = new Intent(Intent.ACTION_SEND);
                    sendEmail.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    sendEmail.putExtra(Intent.EXTRA_EMAIL, new String[]{ownerEmail});
                    sendEmail.putExtra(Intent.EXTRA_SUBJECT, subject);
                    sendEmail.putExtra(Intent.EXTRA_TEXT, message);
                    sendEmail.setType("message/rfc822");
                    Intent chooser = Intent.createChooser(sendEmail, "Send Email");
                    startActivity(chooser);

                    finish();
                }
            }
        });
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }
}
