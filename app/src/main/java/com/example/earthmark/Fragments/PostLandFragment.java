package com.example.earthmark.Fragments;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.example.earthmark.Common.Urls;
import com.example.earthmark.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import cz.msebera.android.httpclient.Header;

public class PostLandFragment extends Fragment {
    EditText OwnerName, LandRating, LandAddress, Budget, Offer, Category, LandDescription, Number;
    AppCompatButton SetPhoto, AddData;
    ImageView IvprofilePhoto;
    String encodedImage;
    Bitmap bitmap;
    static final int PICK_IMAGE_REQUEST = 1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_post_land, container, false);

        OwnerName = view.findViewById(R.id.ETPostLandPhotoName);
        LandRating = view.findViewById(R.id.ETPostLandPhotoRating);
        LandAddress = view.findViewById(R.id.ETPostLandPhotoAddress);
        Budget = view.findViewById(R.id.ETPostLandPhotoBudget);
        Offer = view.findViewById(R.id.ETPostLandPhotoOffer);
        Category = view.findViewById(R.id.ETPostLandPhotoCategoryName);
        LandDescription = view.findViewById(R.id.ETPostLandPhotoDescription);
        Number = view.findViewById(R.id.ETPostLandPhotoContactNumber);
        IvprofilePhoto = view.findViewById(R.id.AddEventCompanyPhoto);

        SetPhoto = view.findViewById(R.id.BtnPostLandPhoto);
        AddData = view.findViewById(R.id.BtnPostLandDataDetails);

        SetPhoto.setOnClickListener(view1 -> openGallery());

        AddData.setOnClickListener(view12 -> uploadData());

        return view;
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && data != null) {
            Uri selectedImage = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), selectedImage);
                IvprofilePhoto.setImageBitmap(bitmap);
                encodeImage(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void encodeImage(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        encodedImage = Base64.encodeToString(byteArray, Base64.DEFAULT);
    }

    private void uploadData() {
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("categoryname", Category.getText().toString());
        params.put("companyname", OwnerName.getText().toString());
        params.put("eventimage", encodedImage);
        params.put("budget", Budget.getText().toString());
        params.put("evenrating", LandRating.getText().toString() + "⭐");
        params.put("eventdescription", LandDescription.getText().toString());
        params.put("eventoffer", Offer.getText().toString() + "%OFF");
        params.put("companyaddress", LandAddress.getText().toString());
        params.put("mobile_no", Number.getText().toString());

        client.post(Urls.UploadData, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    String status = response.getString("success");
                    if (status.equals("1")) {
                        Toast.makeText(getActivity(), "Data Added Successfully", Toast.LENGTH_LONG).show();
                        clearFields();
                    } else {
                        Toast.makeText(getActivity(), response.getString("message"), Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Toast.makeText(getActivity(), "Failed to upload data", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void clearFields() {
        OwnerName.setText("");
        LandRating.setText("");
        LandAddress.setText("");
        Budget.setText("");
        Offer.setText("");
        Category.setText("");
        LandDescription.setText("");
        Number.setText("");
        IvprofilePhoto.setImageResource(R.drawable.noimg ); // Set a placeholder image
        encodedImage = null;
    }
}
