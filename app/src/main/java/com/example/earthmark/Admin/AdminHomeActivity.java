package com.example.earthmark.Admin;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.earthmark.Admin.AdapterClass.AdapterGetAllCategoryDetailsRV;
import com.example.earthmark.Admin.AdapterClass.ViewAllUserLocationMapActivity;
import com.example.earthmark.Common.Urls;
import com.example.earthmark.POJOGetAllCategoryDetails;
import com.example.earthmark.R;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class AdminHomeActivity extends AppCompatActivity {
    RecyclerView rvrecyclerView;
    List<POJOGetAllCategoryDetails> pojoGetAllCategoryDetails;
    AdapterGetAllCategoryDetailsRV adapterGetAllCategoryDetailsRV;
    CardView cvALlCoustomerLoactionMap,getCvALlCoustomerDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_admin_home);

        rvrecyclerView = findViewById(R.id.rvadminHomeListview);
        cvALlCoustomerLoactionMap = findViewById(R.id.cvAdminHomeCoustomerLocation);
        getCvALlCoustomerDetails = findViewById(R.id.cvAdminHomeCoustomerDetails);

        rvrecyclerView.setLayoutManager(new GridLayoutManager(AdminHomeActivity.this,2,GridLayoutManager.HORIZONTAL,false));



        pojoGetAllCategoryDetails = new ArrayList<>();
        adapterGetAllCategoryDetailsRV = new AdapterGetAllCategoryDetailsRV(pojoGetAllCategoryDetails,this);
        rvrecyclerView.setAdapter(adapterGetAllCategoryDetailsRV);

        getAllCategoryRV();

        cvALlCoustomerLoactionMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminHomeActivity.this, ViewAllUserLocationMapActivity.class);
                startActivity(intent);
            }
        });
        getCvALlCoustomerDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminHomeActivity.this, ViewAllCustomerActivity.class);
                startActivity(intent);
            }
        });


    }

    private void getAllCategoryRV() {
        RequestQueue requestQueue = Volley.newRequestQueue(AdminHomeActivity.this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET,
                Urls.AllCategoryDetails, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("getAllCategory");
                    for (int i=0;i<jsonArray.length();i++)
                    {
                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                        String strid = jsonObject1.getString("id");
                        String strCategory = jsonObject1.getString("category_name");
                        String strCategoryImage = jsonObject1.getString("category_image");

                        pojoGetAllCategoryDetails.add(new POJOGetAllCategoryDetails(strid,strCategoryImage,strCategory));
                    }
                    adapterGetAllCategoryDetailsRV.notifyDataSetChanged();

                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(AdminHomeActivity.this, "Server Error", Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(stringRequest);
    }
}