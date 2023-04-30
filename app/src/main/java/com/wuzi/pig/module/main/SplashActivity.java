package com.wuzi.pig.module.main;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.wuzi.pig.R;
import com.wuzi.pig.manager.LoginManager;
import com.wuzi.pig.module.user.LoginActivity;
import com.wuzi.pig.utils.StatusBarUtils;
import com.wuzi.pig.utils.StatusbarColorUtils;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarUtils.immersive(getWindow());
        StatusbarColorUtils.setStatusBarDarkIcon(getWindow(), true);

        setContentView(R.layout.activity_splash);

        if (LoginManager.isLogin()) {
            startActivity(new Intent(this, MainActivity.class));
        } else {
            startActivity(new Intent(this, LoginActivity.class));
        }

        finish();
    }
}