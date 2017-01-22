package app.com.example.android.UBaS;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.api.client.repackaged.org.apache.commons.codec.binary.StringUtils;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.T;
import static android.provider.LiveFolders.INTENT;

public class PaymentActivity extends AppCompatActivity {

    private Button mPayment;
    private Button mCancel;
    private EditText mCardNum;
    private EditText mExpDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_payment);

        mPayment = (Button) findViewById(R.id.pay);
        mCancel = (Button) findViewById(R.id.cancel);
        mCardNum = (EditText) findViewById(R.id.cardNum);
        mExpDate = (EditText) findViewById(R.id.expDate);


        mPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String cardNum = mCardNum.getText().toString();
                String expDate = mExpDate.getText().toString();

                if(!TextUtils.isEmpty(cardNum) && !TextUtils.isEmpty(expDate)) {

                    if(cardNum.length() < 16 || cardNum.length() > 16) {
                        Toast.makeText(PaymentActivity.this, "Make sure you have 16 digits for CC", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if(expDate.length() < 6 || expDate.length() > 6) {
                        Toast.makeText(PaymentActivity.this, "Make sure you have 6 digits for expiration date", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    try{
                        Long num = Long.parseLong(cardNum);
//                        Log.d("PaymentActivity", "woohoooo");
                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(PaymentActivity.this);

                        AlertDialog.Builder alerter = new AlertDialog.Builder(PaymentActivity.this);
                        alerter.setMessage("Success! The payment has been made");
                        alerter.setCancelable(true);

                        alerter.setPositiveButton(
                                "OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                        Intent startMain = new Intent(PaymentActivity.this, HomeScreen.class);
                                        startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(startMain);

                                    }
                                });

                        AlertDialog box = alerter.create();
                        box.show();

                    } catch (NumberFormatException e) {
                        Toast.makeText(PaymentActivity.this, "Make sure you have entered numbers", Toast.LENGTH_SHORT).show();
                    }

                }



            }
        });

        mCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent startMain = new Intent(PaymentActivity.this, HomeScreen.class);
                startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(startMain);
            }
        });




    }
}

