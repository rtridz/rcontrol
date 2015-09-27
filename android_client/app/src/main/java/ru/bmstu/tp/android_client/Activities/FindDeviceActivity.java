package ru.bmstu.tp.android_client.Activities;

import android.support.v4.app.Fragment;
import android.content.Intent;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import ru.bmstu.tp.android_client.R;


public class FindDeviceActivity extends FragmentActivity implements View.OnClickListener {

    final String LOG_TAG = "myLogs";
    ViewPager pager;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_device);

        Log.w(LOG_TAG, "FindDeviceActivity onCreate");
    }

    @Override
    public void onClick(View v) {
    }

    protected void onStart() {
        super.onStart();
        Log.w(LOG_TAG, "FindDeviceActivity onStart");
    }

    protected void onResume() {
        super.onResume();
        Log.w(LOG_TAG, "FindDeviceActivity onResume");
    }

    protected void onPause() {
        super.onPause();
        Log.w(LOG_TAG, "FindDeviceActivity onPause");
    }

    protected void onStop() {
        super.onStop();
        Log.w(LOG_TAG, "FindDeviceActivity onStop");
    }

    protected void onDestroy() {

        super.onDestroy();
        Log.w(LOG_TAG, "FindDeviceActivity onDestroy");
    }
}
