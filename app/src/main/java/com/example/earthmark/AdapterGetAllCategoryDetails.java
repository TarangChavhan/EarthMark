package com.example.earthmark;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;

import com.bumptech.glide.Glide;
import com.example.earthmark.Common.Urls;

import java.util.List;

public class AdapterGetAllCategoryDetails extends BaseAdapter {

    List<POJOGetAllCategoryDetails> pojoGetAllCategoryDetailsList;
    Activity activity;
    public AdapterGetAllCategoryDetails(List<POJOGetAllCategoryDetails> list, Activity activity) {
        this.pojoGetAllCategoryDetailsList = list;
        this.activity = activity;
    }
    @Override
    public int getCount() {
        return pojoGetAllCategoryDetailsList.size();
    }

    @Override
    public Object getItem(int position) {

        return pojoGetAllCategoryDetailsList.get(position);
    }

    @Override
    public long getItemId(int position) {

        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        final ViewHoplder hoplder;
        LayoutInflater inflater =(LayoutInflater) activity.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

        if(view==null)
        {
            hoplder = new ViewHoplder();
            view =inflater.inflate(R.layout.lv_get_all_category,null);
            hoplder.ivCategoryImage = view.findViewById(R.id.ivgetAllCategoryImageCategory);
            hoplder.tvcategoryName = view.findViewById(R.id.tvgetAllCategoryTvCategoryName);
            hoplder.cvCategoryList = view.findViewById(R.id.cvCategoryList);

            view.setTag(hoplder);
        }
        else {
            hoplder = (ViewHoplder) view.getTag();

        }
        final POJOGetAllCategoryDetails obj = pojoGetAllCategoryDetailsList.get(position);
        hoplder.tvcategoryName.setText(obj.getCategoryName());


        Glide.with(activity)
                .load(Urls.getAllCategoryImages +obj.categoryImage)
                .skipMemoryCache(true)
                .error(R.drawable.noimg)
                .into(hoplder.ivCategoryImage);

        hoplder.cvCategoryList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity,CategoryWiseEventActivity.class);
                intent.putExtra("categoryname",obj.getCategoryName());
                activity.startActivity(intent);
            }
        });

        return view;
    }
    class ViewHoplder
    {
        CardView cvCategoryList;
        ImageView ivCategoryImage;
        TextView tvcategoryName;
    }
}
