package com.example.earthmark.Common;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatButton;

import com.example.earthmark.R;

public class NetworkChangeListner extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if(!NetworkDeatils.isConnectedToInternet(context))
        {
            AlertDialog.Builder ad=new AlertDialog.Builder(context);
            View Layout_dailog= LayoutInflater.from(context).inflate(R.layout.check_internet_connection_dailog,null);
            ad.setView(Layout_dailog);

            AppCompatButton btnRetry = Layout_dailog.findViewById(R.id.btncheckInternentconnection);

            AlertDialog alertDialog=ad.create();
            alertDialog.show();
            alertDialog.setCanceledOnTouchOutside(false);

            btnRetry.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    alertDialog.dismiss();
                    onReceive(context,intent);
                }
            });
        }
        else
        {
            Toast.makeText(context,"Your Internet is Connected",Toast.LENGTH_SHORT).show();
        }
    }
}
