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

public class SetUpNewPasswordActivity extends AppCompatActivity {
    String strMobileNo;
    EditText ETnewPass,ETConfrimPass;
    AppCompatButton BtnSetUpPass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_up_new_password);

        ETnewPass = findViewById(R.id.ETSetUpNewPass);
        ETConfrimPass = findViewById(R.id.ETsetUpConfrimNewPass);
        BtnSetUpPass = findViewById(R.id.BtnSetUpPass);

        strMobileNo = getIntent().getStringExtra("phone_no");
        Toast.makeText(SetUpNewPasswordActivity.this, strMobileNo, Toast.LENGTH_SHORT).show();
        BtnSetUpPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               if (ETnewPass.getText().toString().isEmpty() || ETConfrimPass.getText().toString().isEmpty())
               {
                   Toast.makeText(SetUpNewPasswordActivity.this,"Please Enter new Or Confirm Password",Toast.LENGTH_SHORT).show();
               } else if (!ETnewPass.getText().toString().equals(ETConfrimPass.getText().toString())) {
                   ETConfrimPass.setError("Password Not Matched");
               }
               else
               {
                   ProgressDialog progressDialog = new ProgressDialog(SetUpNewPasswordActivity.this);
                   progressDialog.setTitle("Updating Password");
                   progressDialog.setMessage("Please Wait...");
                   progressDialog.setCanceledOnTouchOutside(false);
                   progressDialog.show();
                   forgetPassword();
               }
            }
        });
    }

    private void forgetPassword() {
        AsyncHttpClient client= new AsyncHttpClient();
        RequestParams params = new RequestParams();

        params.put("phone_no",strMobileNo);
        params.put("password",ETnewPass.getText().toString());

        client.post(Urls.UserForgetPasswordWebService,
                params,
                new JsonHttpResponseHandler()
                {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        super.onSuccess(statusCode, headers, response);
                        try {
                            String status = response.getString("success");

                            if(status.equals("1"))
                            {
                                Intent intent = new Intent(SetUpNewPasswordActivity.this, LoginActivity.class);
                                startActivity(intent);
                            }
                            else {
                                Toast.makeText(SetUpNewPasswordActivity.this, "Password Not Changed", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        super.onFailure(statusCode, headers, throwable, errorResponse);
                        Toast.makeText(SetUpNewPasswordActivity.this, "Server Error", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}