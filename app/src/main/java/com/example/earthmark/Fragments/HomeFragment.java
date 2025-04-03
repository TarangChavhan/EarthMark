package com.example.earthmark.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.earthmark.AdapterCategoryWiseEvent;
import com.example.earthmark.AdapterGetAllCategoryDetails;
import com.example.earthmark.CategoryWiseEventActivity;
import com.example.earthmark.Common.Urls;
import com.example.earthmark.POJOCategoryWiseEvent;
import com.example.earthmark.POJOGetAllCategoryDetails;
import com.example.earthmark.R;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class HomeFragment extends Fragment {
    GridView lvShowAllCategory;
    TextView tvNoCategoryAvilable;
    List<POJOGetAllCategoryDetails> pojoGetAllCategoryDetails;
    AdapterGetAllCategoryDetails adapterGetAllCategoryDetails;
    List<POJOCategoryWiseEvent> pojoCategoryWiseEventList;
    SearchView searchCategory;
    AdapterCategoryWiseEvent adapterCategoryWiseEvent;
    ListView listViewShowLand;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        pojoGetAllCategoryDetails = new ArrayList<>();
        pojoCategoryWiseEventList = new ArrayList<>();
        listViewShowLand = view.findViewById(R.id.ShowLands);
        lvShowAllCategory=view.findViewById(R.id.lvFragmentHomeListview);
        tvNoCategoryAvilable=view.findViewById(R.id.tvFragmentHomeNoCategoryAvliable);
        searchCategory=view.findViewById(R.id.svHomeFragmentSerchCategory);

        searchCategory.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchCategory(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                searchCategory(query);
                return false;
            }
        });

        getAllCategory();
        getAllLands();

        return view;
    }

    private void getAllLands() {
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();

        client.post(Urls.ShowAllLands,
                params,
                new JsonHttpResponseHandler()
                {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        super.onSuccess(statusCode, headers, response);
                        try {
                            JSONArray jsonArray = response.getJSONArray("ShowAllLands");
                            if (jsonArray==null)
                            {
                                listViewShowLand.setVisibility(View.GONE);
                                tvNoCategoryAvilable.setText(View.VISIBLE);
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
                            adapterCategoryWiseEvent = new AdapterCategoryWiseEvent(pojoCategoryWiseEventList, getActivity());
                            listViewShowLand.setAdapter(adapterCategoryWiseEvent);
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        super.onFailure(statusCode, headers, throwable, errorResponse);
                        Toast.makeText(getActivity(), "Server Error", Toast.LENGTH_SHORT).show();
                    }
                }
        );
    }

    private void searchCategory(String query) {
        List<POJOGetAllCategoryDetails> tempCategory = new ArrayList<>();
        tempCategory.clear();

        for (POJOGetAllCategoryDetails obj:pojoGetAllCategoryDetails)
        {
            if (obj.getCategoryName().toUpperCase().contains(query.toUpperCase())){
                tempCategory.add(obj);
            }
            else {
                tvNoCategoryAvilable.setVisibility(View.VISIBLE);
            }
            adapterGetAllCategoryDetails = new AdapterGetAllCategoryDetails(tempCategory,getActivity());
            lvShowAllCategory.setAdapter(adapterGetAllCategoryDetails);
        }
    }

    private void getAllCategory() {
        AsyncHttpClient client=new AsyncHttpClient();
        RequestParams params = new RequestParams();

        client.post(Urls.AllCategoryDetails,
                params,
                new JsonHttpResponseHandler(){

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        super.onSuccess(statusCode, headers, response);

                        try {
                            JSONArray jsonArray = response.getJSONArray("getAllCategory");
                            if(jsonArray.isNull(0))
                            {
                                tvNoCategoryAvilable.setVisibility(View.VISIBLE);
                            }
                            for(int i=0;i<jsonArray.length();i++)
                            {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                String strid = jsonObject.getString("id");
                                String strCategoryImage = jsonObject.getString("category_image");
                                String strCategoryName = jsonObject.getString("category_name");
                                pojoGetAllCategoryDetails.add(new POJOGetAllCategoryDetails(strid,strCategoryImage,strCategoryName));
                            }
                            adapterGetAllCategoryDetails = new AdapterGetAllCategoryDetails(pojoGetAllCategoryDetails,getActivity());


                            lvShowAllCategory.setAdapter(adapterGetAllCategoryDetails);

                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                        super.onFailure(statusCode, headers, throwable, errorResponse);
                        Toast.makeText(getActivity(), "Server Error", Toast.LENGTH_SHORT).show();
                    }
                }
                );
    }
}