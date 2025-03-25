package com.example.earthmark;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.bumptech.glide.Glide;
import com.example.earthmark.Common.Urls;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class MyProfileActivity extends AppCompatActivity {

    TextView Name,Username,Email,Password,Address,PhoneNo,State,District,City;
    ImageView ProfilePhoto;
    AppCompatButton singout,EditProfilePhoto,UpdateProfileDetails;

    GoogleSignInOptions googleSignInOptions;
    GoogleSignInClient googleSignInClient;
    SharedPreferences preferences;
    String strusername;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_my_profile);

        preferences = PreferenceManager.getDefaultSharedPreferences(MyProfileActivity.this);

        strusername=preferences.getString("username","");


        Name = findViewById(R.id.TVMyProfilename);
        Email = findViewById(R.id.TVMyProfileEmail);
        Username = findViewById(R.id.TVMyProfileUserName);
        Password = findViewById(R.id.TVMyProfilePassword);
        Address = findViewById(R.id.TVMyProfileAddress);
        PhoneNo = findViewById(R.id.TVMyProfilePhoneNo);
        State = findViewById(R.id.TVMyProfileState);
        District =findViewById(R.id.TVMyProfileDistrict);
        City = findViewById(R.id.TVMyProfileCity);
        singout = findViewById(R.id.myprofilebtnsingout);

        ProfilePhoto = findViewById(R.id.MyProfilePhoto);
        EditProfilePhoto = findViewById(R.id.MyProfileChangePhoto);
        UpdateProfileDetails = findViewById(R.id.myprofilebtnUpdateProfilet);

        googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        googleSignInClient = GoogleSignIn.getClient(MyProfileActivity.this,googleSignInOptions);
        GoogleSignInAccount googleSignInAccount = GoogleSignIn.getLastSignedInAccount(this);

        if(googleSignInAccount!=null)
        {
            String name= googleSignInAccount.getDisplayName();
            String email= googleSignInAccount.getEmail();

            Name.setText(name);
            Email.setText(email);

            singout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    googleSignInClient.signOut().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Intent intent = new Intent(MyProfileActivity.this,LoginActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    });
                }
            });
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        progressDialog = new ProgressDialog(MyProfileActivity.this);
        progressDialog.setTitle("My Profile");
        progressDialog.setMessage("Please wait...");
        progressDialog.setCanceledOnTouchOutside(true);
        progressDialog.show();

        getMydetails();
    }

    private void getMydetails() {
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();

        params.put("username",strusername);
        client.post(Urls.myDetailsWebservice,params,new JsonHttpResponseHandler()
                {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        super.onSuccess(statusCode, headers, response);
                        progressDialog.dismiss();

                        try {
                            JSONArray jsonArray = response.getJSONArray("getMyDetails");

                            for (int i=0;i<jsonArray.length();i++)
                            {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);

                                String strid = jsonObject.getString("id");
                                String strImage = jsonObject.getString("image");
                                String strname = jsonObject.getString("name");
                                String strusername = jsonObject.getString("username");
                                String stremailid = jsonObject.getString("emailid");
                                String strpassword = jsonObject.getString("password");
                                String straddress = jsonObject.getString("address");
                                String strphone_no = jsonObject.getString("phone_no");
                                String strstate = jsonObject.getString("state");
                                String strdistric = jsonObject.getString("distric");
                                String strcity = jsonObject.getString("city");


                                Name.setText(strname);
                                Username.setText(strusername);
                                Email.setText(stremailid);
                                Password.setText(strpassword);
                                Address.setText(straddress);
                                PhoneNo.setText(strphone_no);
                                State.setText(strstate);
                                District.setText(strdistric);
                                City.setText(strcity);


                                Glide.with(MyProfileActivity.this)
                                        .load(Urls.getAllCategoryImages+strImage)
                                        .skipMemoryCache(true)
                                        .error(R.drawable.noimg)
                                        .into(ProfilePhoto);
                                UpdateProfileDetails.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        Intent intent = new Intent(MyProfileActivity.this,UpdateMyProfileActivity.class);
                                        intent.putExtra("name",strname);
                                        intent.putExtra("phone_no",strphone_no);
                                        intent.putExtra("emailid",stremailid);
                                        intent.putExtra("username",strusername);

                                        startActivity(intent);
                                    }
                                });
                            }

                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        super.onFailure(statusCode, headers, throwable, errorResponse);
                        progressDialog.dismiss();
                        Toast.makeText(MyProfileActivity.this,"Server Error",Toast.LENGTH_LONG).show();
                    }
                }
        );
    }
}