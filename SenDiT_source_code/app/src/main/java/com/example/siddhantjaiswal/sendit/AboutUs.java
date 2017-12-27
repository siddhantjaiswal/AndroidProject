package com.example.siddhantjaiswal.sendit;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;


public class AboutUs extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);

    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            onBackPressed();
        }
    return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        //    finish();
        Intent intent = new Intent(this,LoginActivity.class);
        startActivity(intent);
        this.finish();
    }

}

