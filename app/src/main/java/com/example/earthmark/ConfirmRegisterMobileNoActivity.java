package com.example.earthmark;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.FirebaseException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class ConfirmRegisterMobileNoActivity extends AppCompatActivity {

    EditText ETMobileNo;
    AppCompatButton BtnVerify;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_confirm_register_mobile_no);

        ETMobileNo = findViewById(R.id.ETConfirmRegisterMobileNo);
        BtnVerify = findViewById(R.id.BtnconfrimRegiterMonileNoVerify);

        BtnVerify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(ETMobileNo.getText().toString().isEmpty())
                {
                    ETMobileNo.setError("Please Enter Mobile Number");
                } else if (ETMobileNo.getText().toString().length() !=10) {
                    ETMobileNo.setError("Please Enter Valid Mobile Number");
                }
                else {
                    PhoneAuthProvider.getInstance().verifyPhoneNumber(
                            "+91" + ETMobileNo.getText().toString(),
                            60, TimeUnit.SECONDS,
                            ConfirmRegisterMobileNoActivity.this,
                            new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                                @Override
                                public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                                    progressDialog.dismiss();
                                    Toast.makeText(ConfirmRegisterMobileNoActivity.this,"Verifycation Completed",Toast.LENGTH_LONG).show();
                                }

                                @Override
                                public void onVerificationFailed(@NonNull FirebaseException e) {
                                    progressDialog.dismiss();
                                    Toast.makeText(ConfirmRegisterMobileNoActivity.this,"Verifycation Failed",Toast.LENGTH_LONG).show();
                                }

                                @Override
                                public void onCodeSent(@NonNull String VerificationCode, @NonNull
                                PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                                    progressDialog.dismiss();
                                    Intent intent = new Intent(ConfirmRegisterMobileNoActivity.this, ForgetPasswordVerifyOtpActivity.class);
                                    intent.putExtra("VerificationCode",VerificationCode);
                                    intent.putExtra("phone_no",ETMobileNo.getText().toString());
                                    startActivity(intent);
                                }
                            }
                    );
                }
            }
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}