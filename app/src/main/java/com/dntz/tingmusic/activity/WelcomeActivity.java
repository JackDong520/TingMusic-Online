package com.dntz.tingmusic.activity;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.widget.ImageView;
import android.widget.Toast;

import com.dntz.tingmusic.R;
import com.dntz.tingmusic.database.DBManager;

import java.util.Timer;
import java.util.TimerTask;


public class WelcomeActivity extends BaseActivity {

    private static final String TAG = "WelcomeActivity";
    private DBManager dbManager;
    private SharedPreferences sharepreferences;
    private SharedPreferences.Editor editor;
    private static final int PERMISSON_REQUESTCODE = 1;
    private ImageView bingIv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        bingIv = (ImageView)findViewById(R.id.welcome_bing_iv);
        loadBingPic();
        dbManager = DBManager.getInstance(getApplicationContext());
        sharepreferences=this.getSharedPreferences("check", MODE_PRIVATE);
        editor=sharepreferences.edit();
        initPermission();

    }

    private void loadBingPic() {
        bingIv.setImageResource(R.drawable.bg_playlist);
    }


    private void checkSkip() {
        Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                startMusicActivity();
            }
        };
        timer.schedule(task, 1000);
    }

    private void startMusicActivity() {
        Intent intent = new Intent();
        intent.setClass(this, HomeActivity.class);
        startActivity(intent);
        finish();
    }



    private void initPermission() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            checkSkip();
            return;
        }
        if (ContextCompat.checkSelfPermission(WelcomeActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(WelcomeActivity.this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    PERMISSON_REQUESTCODE);
        }else {
            checkSkip();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case PERMISSON_REQUESTCODE:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    checkSkip();
                } else {
                    Toast.makeText(this, "必须同意所有权限才能使用本程序", Toast.LENGTH_SHORT).show();
                    finish();
                }
                break;
            default:
        }
    }

}
