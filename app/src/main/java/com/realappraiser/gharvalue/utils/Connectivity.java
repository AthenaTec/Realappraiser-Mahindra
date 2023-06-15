package com.realappraiser.gharvalue.utils;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.Settings;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import com.realappraiser.gharvalue.R;


/****
 * Created by kaptas,
 * Connectivity class to check Internet Network availability
 ****/
@SuppressWarnings("ALL")
public class Connectivity {

    /**
     * Get the network info
     * @param context
     * @return
     */
    public static NetworkInfo getNetworkInfo(Context context){
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo();
    }

    /**
     * Check if there is any connectivity
     * @param context
     * @return
     */
    public static boolean isConnected(Context context){
        NetworkInfo info = Connectivity.getNetworkInfo(context);
        return (info != null && info.isConnected());
    }

    @SuppressWarnings("ConstantConditions")
    public static void showNoConnectionDialog(final Context context) {

        Activity activity = (Activity) context;

        View view  = activity.getLayoutInflater().inflate(R.layout.save_pop_up,null);
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(context,R.style.CustomDialog);
        builder.setView(view);

        AlertDialog networkPopup = builder.create();
        networkPopup.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        TextView title = view.findViewById(R.id.title);
        title.setText("Network Information");
        TextView subTitle = view.findViewById(R.id.sub_title);
        subTitle.setText("Please check your Internet connection");
        subTitle.setVisibility(View.VISIBLE);
        Button setting = view.findViewById(R.id.btn_save);
        setting.setText("Settings");
        Button btnCancel = view.findViewById(R.id.btn_no);
        btnCancel.setText("Cancel");
        networkPopup.setCancelable(false);
        networkPopup.setCanceledOnTouchOutside(false);
        networkPopup.show();

        setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /* Go to Settings page Intent */
                Intent i = new Intent(Settings.ACTION_SETTINGS);
                context.startActivity(i);
                networkPopup.dismiss();
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                networkPopup.dismiss();
            }
        });


       /* AlertDialog.Builder alert_build = new AlertDialog.Builder(new ContextThemeWrapper(context, R.style.AlertDialogCustom));
        alert_build.setTitle("Network Information");
        alert_build.setMessage("Please check your Internet connection");
        alert_build.setCancelable(false);
        alert_build.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                *//* Go to Settings page Intent *//*
                Intent i = new Intent(Settings.ACTION_SETTINGS);
                context.startActivity(i);
                dialog.dismiss();

            }
        });
        alert_build.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog alert_show = alert_build.create();
        alert_show.show();*/

       /* final Context ctx = context;
        AlertDialog.Builder builder = new AlertDialog.Builder(ctx, R.style.NewDialog);
        builder.setCancelable(false);
        builder.setMessage("You don't have internet connection. Please check your connection settings and try again.");
        builder.setTitle("No Internet Connection");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                return;
            }
        });
        builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
            public void onCancel(DialogInterface dialog) {
                return;
            }
        });
        builder.show();*/

    }
}
