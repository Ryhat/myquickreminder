package com.chrisfeb.myquicktask;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.preference.Preference;

import android.Manifest;
import android.app.Activity;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Color;
import android.icu.util.Calendar;
import android.icu.util.TimeZone;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.CalendarContract;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import cn.qqtheme.framework.picker.DateTimePicker;
import cn.qqtheme.framework.picker.TimePicker;

public class MainActivity extends AppCompatActivity implements View.OnClickListener,
        CompoundButton.OnCheckedChangeListener {

    private static final String TAG = "MainActivity";

    private TextView[] mTextViews = new TextView[5];
    private Switch[] mSwitches = new Switch[5];
    private TimePicker picker;
    private SharedPreferences mSharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initUI();

        CalendarUtils.fetchPermission(CalendarUtils.REQUEST_CALENDAR, this);
        CustomizeTile0.str = "eqae";
//        boolean isInsertSuccess = insertCalendarEvent(this,
//                "打游戏",
//                "gaming time",
//                "",
//                5,qweqwe
//                0,
//                0);
//        if (isInsertSuccess){
//            Toast.makeText(this, "添加日历事件成功", Toast.LENGTH_LONG).show();
//        }
    }

    private void initUI() {
        mSharedPreferences = getPreferences(MODE_PRIVATE);
        Resources res = getResources();
        int id;
        for (int i = 0; i < 5; i++) {
            id = res.getIdentifier("text" + i, "id", getPackageName());
            mTextViews[i] = findViewById(id);
            mTextViews[i].setOnClickListener(this);
            String time = mSharedPreferences.getString("time"+i, "1:00");
            mTextViews[i].setText(time);

            id = res.getIdentifier("switch" + i, "id", getPackageName());
            mSwitches[i] = findViewById(id);
            boolean isChecked = mSharedPreferences.getBoolean("switch"+i, false);
            mSwitches[i].setChecked(isChecked);
            mSwitches[i].setOnCheckedChangeListener(this);
        }
        picker = new TimePicker(this, DateTimePicker.HOUR_24);
        picker.setRangeStart(1, 0);


    }



    @Override
    public void onClick(View pView) {

        final SharedPreferences.Editor editor = mSharedPreferences.edit();
        switch (pView.getId()) {
            case R.id.text0:
                Log.d(TAG, "onClick: 0");

                picker.setOnTimePickListener(new TimePicker.OnTimePickListener() {
                    @Override
                    public void onTimePicked(String hour, String minute) {
                        String time = hour + ":" + minute;
                        Log.d(TAG, "onTimePicked:0 " + time);
                        mTextViews[0].setText(time);
                        editor.putString("time0", time);
                        editor.apply();
                    }
                });
                picker.show();
                break;

            case R.id.text1:
                Log.d(TAG, "onClick: 1");

                picker.setOnTimePickListener(new TimePicker.OnTimePickListener() {
                    @Override
                    public void onTimePicked(String hour, String minute) {
                        String time = hour + ":" + minute;
                        Log.d(TAG, "onTimePicked:1 " + time);
                        mTextViews[1].setText(time);
                        editor.putString("time1", time);
                        editor.apply();
                    }
                });
                picker.show();
                break;

            case R.id.text2:
                Log.d(TAG, "onClick: 2");

                picker.setOnTimePickListener(new TimePicker.OnTimePickListener() {
                    @Override
                    public void onTimePicked(String hour, String minute) {
                        String time = hour + ":" + minute;
                        Log.d(TAG, "onTimePicked:2 " + time);
                        mTextViews[2].setText(time);
                        editor.putString("time2", time);
                        editor.apply();
                    }
                });
                picker.show();
                break;

            case R.id.text3:
                Log.d(TAG, "onClick: 3");

                picker.setOnTimePickListener(new TimePicker.OnTimePickListener() {
                    @Override
                    public void onTimePicked(String hour, String minute) {
                        String time = hour + ":" + minute;
                        Log.d(TAG, "onTimePicked:3 " + time);
                        mTextViews[3].setText(time);
                        editor.putString("time3", time);
                        editor.apply();
                    }
                });
                picker.show();
                break;

            case R.id.text4:
                Log.d(TAG, "onClick: 4");

                picker.setOnTimePickListener(new TimePicker.OnTimePickListener() {
                    @Override
                    public void onTimePicked(String hour, String minute) {
                        String time = hour + ":" + minute;
                        Log.d(TAG, "onTimePicked:4 " + time);
                        mTextViews[4].setText(time);
                        editor.putString("time4", time);
                        editor.apply();
                    }
                });
                picker.show();
                break;
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton pCompoundButton, boolean pB) {

        SharedPreferences.Editor editor = mSharedPreferences.edit();
        switch (pCompoundButton.getId()){
            case R.id.switch0:
                Log.d(TAG, "onCheckedChanged:0 " + pB);
                editor.putBoolean("switch0", pB);
                editor.apply();
                break;

            case R.id.switch1:
                Log.d(TAG, "onCheckedChanged:1 " + pB);
                editor.putBoolean("switch1", pB);
                editor.apply();
                break;

            case R.id.switch2:
                Log.d(TAG, "onCheckedChanged:2 " + pB);
                editor.putBoolean("switch2", pB);
                editor.apply();
                break;

            case R.id.switch3:
                Log.d(TAG, "onCheckedChanged:3 " + pB);
                editor.putBoolean("switch3", pB);
                editor.apply();
                break;

            case R.id.switch4:
                Log.d(TAG, "onCheckedChanged:4 " + pB);
                editor.putBoolean("switch4", pB);
                editor.apply();
                break;
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == CalendarUtils.REQUEST_CALENDAR) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // 用户同意的授权请求
                Toast.makeText(this, "获取权限成功，请重新提交", Toast.LENGTH_SHORT).show();
            } else {
                if (!ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_CALENDAR)) {
                    // 如果用户不是点击了拒绝就跳转到系统设置页
                    Toast.makeText(this, "获取权限失败，请给我权限", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
}
