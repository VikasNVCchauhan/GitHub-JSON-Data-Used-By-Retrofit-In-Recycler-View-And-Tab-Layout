package com.example.signzyinternshala.InternetChecking;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.InsetDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.signzyinternshala.MainActivity;
import com.example.signzyinternshala.R;

public class NetworkStateChangeReciever extends BroadcastReceiver implements View.OnClickListener {
    private ConnectivityManager connectivityManager;
    private NetworkInfo networkInfo;
    private AlertDialog alertDialog;
    private AlertDialog.Builder builder;
    private TextView textViewReload, textViewSetting;
    private AppCompatActivity appCompatActivity;
    private Context context;
    Intent intent1;

    @Override
    public void onReceive(Context context, Intent intent) {
        intent1 = intent;
        connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            builder = new AlertDialog.Builder(context);
            networkInfo = connectivityManager.getActiveNetworkInfo();
            if (networkInfo != null && networkInfo.isConnected()) {
                if (alertDialog != null) {
                    alertDialog.dismiss();
                }
            } else {


                View view = LayoutInflater.from(context).inflate(R.layout.internet_connection, null);
                appCompatActivity = (AppCompatActivity) view.getContext();


                setIdForALlWidgets(view);


                builder.setView(view);
                builder.setCancelable(false);
                alertDialog = builder.show();

                ColorDrawable back = new ColorDrawable(Color.TRANSPARENT);
                InsetDrawable inset = new InsetDrawable(back, 40);
                alertDialog.getWindow().setBackgroundDrawable(inset);

                textViewReload.setOnClickListener(this);
                textViewSetting.setOnClickListener(this);
            }
        }
    }

    private void setIdForALlWidgets(View view) {
        textViewReload = view.findViewById(R.id.text_view_reload_alert_dialog);
        textViewSetting = view.findViewById(R.id.text_view_settings_alert_dialog);
    }

    @Override
    public void onClick(View view) {
        if (view == textViewReload) {
            textViewReload.setTextColor(Color.WHITE);
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    textViewReload.setTextColor(Color.parseColor("#E1FF7800"));
                }
            }, 200);

            Toast.makeText(appCompatActivity, "No Internet", Toast.LENGTH_SHORT).show();
            // appCompatActivity.startActivity(new Intent(appCompatActivity, MainActivity.class));
            //Toast.makeText(appCompatActivity, "Working On It", Toast.LENGTH_SHORT).show();

        } else if (view == textViewSetting) {
            textViewSetting.setTextColor(Color.WHITE);
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    textViewSetting.setTextColor(Color.parseColor("#E1FF7800"));
                    appCompatActivity.startActivity(new Intent(Settings.ACTION_SETTINGS));
                }
            }, 200);
        }
    }
}
