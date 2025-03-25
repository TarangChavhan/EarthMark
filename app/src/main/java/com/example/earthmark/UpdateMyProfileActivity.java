package com.example.earthmark;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.example.earthmark.Common.Urls;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class UpdateMyProfileActivity extends AppCompatActivity {
    EditText etName,etMobileNo,etEmailID,etUsername;
    AppCompatButton btnUpdate;
    String strName,strMobileNo,strEmail,strUsername;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_my_profile);
        etName = findViewById(R.id.ETUpdateMyProfileName);
        etEmailID = findViewById(R.id.ETUpdateMyProfileEmail);
        etUsername = findViewById(R.id.ETUpdateMyProfileUsername);
        etMobileNo = findViewById(R.id.ETUpdateMyProfileMobileNo);

        btnUpdate = findViewById(R.id.btnUpdatemyprofilebtnUpdateProfilet);

        strName = getIntent().getStringExtra("name");
        strEmail = getIntent().getStringExtra("emailid");
        strUsername = getIntent().getStringExtra("username");
        strMobileNo = getIntent().getStringExtra("phone_no");

        etName.setText(strName);
        etUsername.setText(strUsername);
        etMobileNo.setText(strMobileNo);
        etEmailID.setText(strEmail);

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog = new ProgressDialog(UpdateMyProfileActivity.this);
                progressDialog.setTitle("Updating Profile");
                progressDialog.setMessage("Please Wait...");
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.show();
                updateUserInformation();
            }
        });
    }

    private void updateUserInformation() {
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("name",etName.getText().toString());
        params.put("phone_no",etMobileNo.getText().toString());
        params.put("emailid",etEmailID.getText().toString());
        params.put("username",etUsername.getText().toString());
        client.post(Urls.updateMyProfileWebService,params, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                try {
                    String status = response.getString("success");

                    if (status.equals("1"))
                    {
                        Toast.makeText(UpdateMyProfileActivity.this, "profile Update Successfully", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(UpdateMyProfileActivity.this,MyProfileActivity.class);
                        startActivity(intent);
                        finish();
                    }
                    else {
                        Toast.makeText(UpdateMyProfileActivity.this, "Update Not Done", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                super.onFailure(statusCode, headers, throwable, errorResponse);
                Toast.makeText(UpdateMyProfileActivity.this, "404 Server Error", Toast.LENGTH_SHORT).show();

            }
        });
    }
}