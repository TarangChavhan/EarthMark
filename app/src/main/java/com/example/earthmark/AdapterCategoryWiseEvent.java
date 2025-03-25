package com.example.earthmark;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.earthmark.Common.Urls;


import java.util.List;

public class AdapterCategoryWiseEvent extends BaseAdapter {
    List<POJOCategoryWiseEvent> pojoCategoryWiseEventList;
    Activity activity;

    public AdapterCategoryWiseEvent(List<POJOCategoryWiseEvent> list, Activity activity) {
        this.pojoCategoryWiseEventList = list;
        this.activity = activity;
    }

    @Override
    public int getCount() {
        return pojoCategoryWiseEventList.size();
    }

    @Override
    public Object getItem(int position) {

        return pojoCategoryWiseEventList.get(position);
    }

    @Override
    public long getItemId(int position) {

        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        final Viewholder holder;
        LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if(view==null)
        {
            holder = new Viewholder();
            view = inflater.inflate(R.layout.lv_categorywise_event,null);

            holder.ivCategoryWiseImage = view.findViewById(R.id.ivCategoryWiseEventImage);
            holder.tvCategorywiseCompanyName = view.findViewById(R.id.ivCategoryWiseCompanyName);
            holder.tvCategorywiseRating = view.findViewById(R.id.ivCategoryWiseEventRating);
            holder.tvCategorywiseCompanyAddress = view.findViewById(R.id.ivCategoryWiseEventAddress);
            holder.tvCategorywiseBudget = view.findViewById(R.id.ivCategoryWiseCompanyBudget);
            holder.tvCategorywiseeventOffer = view.findViewById(R.id.ivCategoryWiseEventOffer);
            holder.tvCategorywiseEventDescription = view.findViewById(R.id.ivCategoryWiseEventDesscription);
            holder.TvCategorywiseCategoryName = view.findViewById(R.id.ivCategoryWiseCategoryName);
            holder.TvMobileNo = view.findViewById(R.id.ivCategoryWiseEventMobileNo);

            holder.openwhatsappcv = view.findViewById(R.id.cvCategorywiseEvent);
            view.setTag(holder);


        }
        else
        {
            holder = (Viewholder) view.getTag();
        }
        final POJOCategoryWiseEvent obj = pojoCategoryWiseEventList.get(position);
        holder.tvCategorywiseCompanyName.setText(obj.companyname);
        holder.tvCategorywiseRating.setText(obj.evenrating);
        holder.tvCategorywiseCompanyAddress.setText(obj.companyaddress);
        holder.tvCategorywiseBudget.setText(obj.budget);
        holder.tvCategorywiseeventOffer.setText(obj.eventoffer);
        holder.tvCategorywiseEventDescription.setText(obj.eventdescription);
        holder.TvCategorywiseCategoryName.setText(obj.categoryname);
        holder.TvMobileNo.setText(obj.MobileNo);

        Glide.with(activity)
                .load(Urls.getAllCategoryImages + obj.eventimage)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        Log.e("GlideError", "Load failed", e);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        return false;
                    }
                })
                .into(holder.ivCategoryWiseImage);


        holder.openwhatsappcv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Intent sendIntent = new Intent(Intent.ACTION_VIEW);
                    sendIntent.putExtra("mobile_no",obj.getMobileNo());
                    String phoneNumber = obj.getMobileNo(); // Replace with the phone number without the + sign or leading zeros, but with country code
                    sendIntent.setData(Uri.parse("smsto:" +phoneNumber));
                    sendIntent.setPackage("com.whatsapp");
                    activity.startActivity(sendIntent);
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(activity, "WhatsApp not installed", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return view;
    }

    private void openWhatsApp(View view) {

    }
}

    class Viewholder
    {
        CardView openwhatsappcv;
        ImageView ivCategoryWiseImage;
        TextView tvCategorywiseCompanyName,tvCategorywiseRating,tvCategorywiseCompanyAddress,tvCategorywiseBudget,tvCategorywiseeventOffer,tvCategorywiseEventDescription,TvCategorywiseCategoryName,TvMobileNo;

    }
