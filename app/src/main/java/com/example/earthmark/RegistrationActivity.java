package com.example.earthmark;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.FirebaseException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;


import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class RegistrationActivity extends AppCompatActivity {
    EditText RName,RUsername,REmaild,RPassword,RPhone;
    CheckBox Rshowpass;
    ProgressDialog progressDialog;
    androidx.appcompat.widget.AppCompatButton Rbutton,AddPhoto;
    ImageView ivphoto;
    private int PICK_IMAGE_REQUEST=1;
    Bitmap bitmap;
    Uri Filepath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_registration);


        RName = findViewById(R.id.ETRegistrationName);
        RUsername = findViewById(R.id.ETRegistrationUsername);
        REmaild = findViewById(R.id.ETRegistrationEmailID);
        RPassword = findViewById(R.id.ETRegistrationPassword);
        RPhone = findViewById(R.id.ETRegistrationPhone);
        Rshowpass = findViewById(R.id.CBpassword);
        Rbutton = findViewById(R.id.BtnRegistration);
        ivphoto = findViewById(R.id.IVRegistrationPhoto);
        AddPhoto = findViewById(R.id.BtnRegistrationPhotoAddBtn);
        
        AddPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                        showFilechoser();
            }
        });

        Rshowpass.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(Rshowpass.isChecked())
                {
                    RPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }
                else
                {
                    RPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
            }
        });

        Rbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {

                if(RUsername.getText().toString().isEmpty())
                {
                    RUsername.setError("UserName is Required");
                }
                else if(RPassword.getText().toString().isEmpty())
                {
                    RPassword.setError("Password is Required");
                }
                else if (RUsername.getText().toString().length()<8)
                {
                    RUsername.setError("At least 8 Character is required ");
                }
                else if (RPassword.getText().toString().length()<8)
                {
                    RPassword.setError("Password should 8 character long");
                }
                else if(!RUsername.getText().toString().matches("^(?=.*[A-Z]).+$"))
                {
                    RUsername.setError("one Uppercase is required");
                }
                else if(!RUsername.getText().toString().matches("^(?=.*[a-z]).+$"))
                {
                    RUsername.setError("one Lowercase is required");
                }
                else if(!RUsername.getText().toString().matches("^(?=.*[0-9]).+$"))
                {
                    RUsername.setError("one number is required");
                }
                else if(!RUsername.getText().toString().matches("^(?=.*[@,#,$,%,!,_]).+$"))
                {
                    RUsername.setError("one Special Symbol is required");
                }
                else if (REmaild.getText().toString().isEmpty())
                {
                    REmaild.setError("Email ID required");
                }
                else if (!REmaild.getText().toString().contains("@") || !REmaild.getText().toString().contains(".com"))
                {
                    REmaild.setError(" Enter valid Email ID ");
                }
                else if (RPhone.getText().toString().isEmpty())
                {
                    RPhone.setError("Enter the Phone Number");
                }
                else if (RPhone.getText().toString().length()!=10)
                {
                    RPhone.setError("10 digit number required ");
                }
                else
                {
                    progressDialog= new ProgressDialog(RegistrationActivity.this);
                    progressDialog.setTitle("Please wait...");
                    progressDialog.setMessage("Registration is in processing");
                    progressDialog.show();

                    //Verify Mobile Number
                    PhoneAuthProvider.getInstance().verifyPhoneNumber(
                            "+91" + RPhone.getText().toString(),
                            60, TimeUnit.SECONDS,
                            RegistrationActivity.this,
                            new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                                @Override
                                public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                                    progressDialog.dismiss();
                                    Toast.makeText(RegistrationActivity.this,"Verifycation Completed",Toast.LENGTH_LONG).show();
                                }

                                @Override
                                public void onVerificationFailed(@NonNull FirebaseException e) {
                                    progressDialog.dismiss();
                                    Toast.makeText(RegistrationActivity.this,"Verifycation Failed",Toast.LENGTH_LONG).show();
                                }

                                @Override
                                public void onCodeSent(@NonNull String VerificationCode, @NonNull
                                PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                                    progressDialog.dismiss();
                                   Intent intent = new Intent(RegistrationActivity.this, VerifyOtpActivity.class);
                                   intent.putExtra("verificationCode",VerificationCode);
                                   intent.putExtra("Name",RName.getText().toString());
                                   intent.putExtra("username",RUsername.getText().toString());
                                   intent.putExtra("password",RPassword.getText().toString());
                                   intent.putExtra("emailid",REmaild.getText().toString());
                                   intent.putExtra("phone_no",RPhone.getText().toString());
                                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                                    bitmap.compress(Bitmap.CompressFormat.PNG,80,byteArrayOutputStream);
                                    byte[] byteArray = byteArrayOutputStream.toByteArray();
                                    intent.putExtra("profile_photo",byteArray);

                                   startActivity(intent);
                                }
                            }
                    );


                    //userRegisterDetails();

                }
            }
        });
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        
    }

    private void showFilechoser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Select Profile Photo"),PICK_IMAGE_REQUEST);
    }
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==PICK_IMAGE_REQUEST && resultCode== Activity.RESULT_OK && data!=null)
        {
            Filepath = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), Filepath);
                ivphoto.setImageBitmap(bitmap);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        }

    }
}