package com.example.earthmark.Common;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class NetworkDeatils {

    public static boolean isConnectedToInternet(Context context)
    {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(context.CONNECTIVITY_SERVICE);
        if (connectivityManager!=null)
        {
            NetworkInfo[] networkInfo = connectivityManager.getAllNetworkInfo();

            if(networkInfo!=null)
            {
                for (int i=0;i<networkInfo.length;i++)
                {
                    if(networkInfo[i].getState()== NetworkInfo.State.CONNECTED)
                    {
                        return true;
                    }
                }
            }
        }
        return false;
    }

}
