package com.example.earthmark;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;

import com.example.earthmark.Common.Urls;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class CategoryWiseEventActivity extends AppCompatActivity {

    SearchView searchCategorywiseEvent;
    ListView lvCategoryWiseEvent;
    TextView tvNoEventAvailable;
    String strCategoryName,strMobileNo;
    List<POJOCategoryWiseEvent> pojoCategoryWiseEventList;
    AdapterCategoryWiseEvent adapterCategoryWiseEvent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_category_wise_event);
        pojoCategoryWiseEventList = new ArrayList<>();

        searchCategorywiseEvent = findViewById(R.id.svCategorywiseEventSearch);
        lvCategoryWiseEvent = findViewById(R.id.lvCategorywiseEventlist);
        tvNoEventAvailable = findViewById(R.id.tvCateWiseEventNoEventAvilable);
        strCategoryName = getIntent().getStringExtra("categoryname");
        strMobileNo = getIntent().getStringExtra("mobile_no");

        setTitle("Events");

        getCategoryWiseEventList();

        searchCategorywiseEvent.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchCategoryNamewiseEvent(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                searchCategoryNamewiseEvent(query);
                return false;
            }
        });
    }

    private void searchCategoryNamewiseEvent(String query) {
        List<POJOCategoryWiseEvent> temlist= new ArrayList<>();
        temlist.clear();
        for(POJOCategoryWiseEvent obj:pojoCategoryWiseEventList)
        {
            if (obj.getCategoryname().toUpperCase().contains(query.toUpperCase()) ||
            obj.getCompanyname().toUpperCase().contains(query.toUpperCase()) ||
            obj.getBudget().toUpperCase().contains(query.toUpperCase()) ||
            obj.getCompanyaddress().toUpperCase().contains(query.toUpperCase()) ||
            obj.getEvenrating().toUpperCase().contains(query.toUpperCase()) ||
            obj.getEventoffer().toUpperCase().contains(query.toUpperCase())||
            obj.getEventimage().toUpperCase().contains(query.toUpperCase()) ||
            obj.getCategoryname().toUpperCase().contains(query.toUpperCase()) ||
            obj.getEventdescription().toUpperCase().contains(query.toUpperCase()) ||
            obj.getMobileNo().toUpperCase().contains(query.toUpperCase()))
            {
                temlist.add(obj);
            }
            adapterCategoryWiseEvent = new AdapterCategoryWiseEvent(temlist,this);
            lvCategoryWiseEvent.setAdapter(adapterCategoryWiseEvent);
        }
    }
    private void getCategoryWiseEventList() {
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("categoryname",strCategoryName);

        client.post(Urls.getAllCategoryWiseEvents,
                params,
                new JsonHttpResponseHandler()
                {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        super.onSuccess(statusCode, headers, response);
                        try {
                            JSONArray jsonArray = response.getJSONArray("getCategoryWiseEvent");
                            if (jsonArray==null)
                            {
                                lvCategoryWiseEvent.setVisibility(View.GONE);
                                tvNoEventAvailable.setText(View.VISIBLE);
                            }
                            for (int i=0;i<jsonArray.length();i++)
                            {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                String strid= jsonObject.getString("id");
                                String strcategoryname= jsonObject.getString("categoryname");
                                String strcompanyname= jsonObject.getString("companyname");
                                String streventImage= jsonObject.getString("eventimage");
                                String strbudget= jsonObject.getString("budget");
                                String streventRating= jsonObject.getString("evenrating");
                                String streventOffer= jsonObject.getString("eventoffer");
                                String streventDescription= jsonObject.getString("eventdescription");
                                String strcompanyAddress= jsonObject.getString("companyaddress");
                                String strMobileNo=jsonObject.getString("mobile_no");

                                pojoCategoryWiseEventList.add(new POJOCategoryWiseEvent(strid,strcategoryname,strcompanyname,streventImage,strbudget,streventRating,streventOffer,streventDescription,strcompanyAddress,strMobileNo));

                            }
                            adapterCategoryWiseEvent = new AdapterCategoryWiseEvent(pojoCategoryWiseEventList,CategoryWiseEventActivity.this);
                            lvCategoryWiseEvent.setAdapter(adapterCategoryWiseEvent);
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        super.onFailure(statusCode, headers, throwable, errorResponse);
                        Toast.makeText(CategoryWiseEventActivity.this, "Server Error", Toast.LENGTH_SHORT).show();
                    }
                }
        );
    }
}