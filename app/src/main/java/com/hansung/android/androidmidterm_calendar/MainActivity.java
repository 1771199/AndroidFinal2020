package com.hansung.android.androidmidterm_calendar;


import android.content.Intent;
import android.os.Bundle;

public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initialize();
        Intent intent = new Intent(this, CalendarActivity.class);
        startActivity(intent);
    }
}
