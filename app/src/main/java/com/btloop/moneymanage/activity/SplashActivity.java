package com.btloop.moneymanage.activity;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ProgressBar;

import com.btloop.moneymanage.MainActivity;
import com.btloop.moneymanage.R;

public class SplashActivity extends AppCompatActivity {
    private ProgressBar pro;
    int prog = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        getSupportActionBar().hide();
        pro = (ProgressBar) findViewById(R.id.progressBar1);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 5; i++) {
                    prog += 20;
                    new Handler().post(new Runnable() {

                        @Override
                        public void run() {
                            pro.setProgress(prog);
                            if (prog == 100) {
                                Intent intent = new Intent(SplashActivity.this,
                                        MainActivity.class);
                                startActivity(intent);
                            }
                        }
                    });
                }

            }
        }, 1000);
    }
}
