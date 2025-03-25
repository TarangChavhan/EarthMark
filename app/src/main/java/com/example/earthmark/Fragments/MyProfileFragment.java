package com.example.earthmark.Fragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.earthmark.Common.Urls;
import com.example.earthmark.LoginActivity;
import com.example.earthmark.MyProfileActivity;
import com.example.earthmark.R;
import com.example.earthmark.UpdateMyProfileActivity;
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

import java.io.IOException;

import cz.msebera.android.httpclient.Header;

public class MyProfileFragment extends Fragment {
    TextView Name,Username,Email,Password,Address,PhoneNo,State,District,City;
    ImageView ProfilePhoto;
    AppCompatButton singout,EditProfilePhoto,UpdateProfileDetails;
    private int PICK_IMAGE_REQUEST=1;
    Bitmap bitmap;
    Uri Filepath;

    GoogleSignInOptions googleSignInOptions;
    GoogleSignInClient googleSignInClient;
    SharedPreferences preferences;
    String strusername;
    ProgressDialog progressDialog;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_my_profile, container, false);

        preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());

        strusername=preferences.getString("username","");


        Name = view.findViewById(R.id.TVMyProfilename);
        Email = view.findViewById(R.id.TVMyProfileEmail);
        Username = view.findViewById(R.id.TVMyProfileUserName);
        Password = view.findViewById(R.id.TVMyProfilePassword);
        Address = view.findViewById(R.id.TVMyProfileAddress);
        PhoneNo = view.findViewById(R.id.TVMyProfilePhoneNo);
        State = view.findViewById(R.id.TVMyProfileState);
        District =view.findViewById(R.id.TVMyProfileDistrict);
        City = view.findViewById(R.id.TVMyProfileCity);
        singout = view.findViewById(R.id.myprofilebtnsingout);

        ProfilePhoto = view.findViewById(R.id.MyProfilePhoto);
        EditProfilePhoto = view.findViewById(R.id.MyProfileChangePhoto);
        UpdateProfileDetails = view.findViewById(R.id.myprofilebtnUpdateProfilet);



        googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        googleSignInClient = GoogleSignIn.getClient(getActivity(),googleSignInOptions);
        GoogleSignInAccount googleSignInAccount = GoogleSignIn.getLastSignedInAccount(getActivity());

        EditProfilePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showFilechoser();
            }
        });

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
                            Intent intent = new Intent(getActivity(), LoginActivity.class);
                            startActivity(intent);
                            requireActivity().finish();
                        }
                    });
                }
            });
        }
        return view;
    }

    private void showFilechoser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Select Profile Photo"),PICK_IMAGE_REQUEST);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==PICK_IMAGE_REQUEST && resultCode== Activity.RESULT_OK && data!=null)
        {
            Filepath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(requireActivity().getContentResolver(), Filepath);
                ProfilePhoto.setImageBitmap(bitmap);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        }

    }

    @Override
    public void onStart() {
        super.onStart();
        progressDialog = new ProgressDialog(getActivity());
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


                                Glide.with(getActivity())
                                        .load(Urls.getAllCategoryImages+strImage)
                                        .skipMemoryCache(true)
                                        .error(R.drawable.noimg)
                                        .into(ProfilePhoto);
                                UpdateProfileDetails.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        Intent intent = new Intent(getActivity(), UpdateMyProfileActivity.class);
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
                        Toast.makeText(getActivity(),"Server Error",Toast.LENGTH_LONG).show();
                    }
                }
        );
    }
}