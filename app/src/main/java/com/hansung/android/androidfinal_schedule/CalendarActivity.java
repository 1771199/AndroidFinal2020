package com.hansung.android.androidfinal_schedule;

import androidx.viewpager.widget.ViewPager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Debug;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Toast;


public class CalendarActivity extends BaseActivity implements CalendarFragment.OnFragmentListener {

    private static final int COUNT_PAGE = 12;
    ViewPager pager;
    private AdapterCalendar adapter;
    public static String title;
    public static String selected = "";
    Button plus;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);
        plus = findViewById(R.id.plus_task);
        plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selected == ""){
                    Toast.makeText(getApplicationContext(), "일정을 추가할 날짜를 선택해 주세요.", Toast.LENGTH_SHORT).show();
                }
                else Toast.makeText(getApplicationContext(), selected, Toast.LENGTH_SHORT).show();
                //Intent intent = new Intent(getApplicationContext(), CalendarActivity.class);
                //startActivity(intent);
            }
        });
        initialize();

    }

    @Override
    public void initView() {
        super.initView();
        pager = (ViewPager) findViewById(R.id.pager);
    }

    public void initControl() {
        adapter = new AdapterCalendar(getSupportFragmentManager());
        pager.setAdapter(adapter);
        adapter.setOnFragmentListener(this);
        switch (MainActivity.calendarType){
            case 0:
                adapter.setNumOfMonth(COUNT_PAGE);
                title = adapter.getMonthDisplayed(COUNT_PAGE);
                break;
            case 1:
                adapter.setNumOfWeeks(COUNT_PAGE);
                title = adapter.getWeekDisplayed(COUNT_PAGE);
                break;
            case 2:
                adapter.setDate(COUNT_PAGE);
                title = adapter.getMonthDisplayed(COUNT_PAGE);
                break;
        }
        pager.setCurrentItem(COUNT_PAGE);

        getSupportActionBar().setTitle(title);

        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch(MainActivity.calendarType){
                    case 0:
                    case 2:
                        title = adapter.getMonthDisplayed(position); break;
                    case 1:
                        title = adapter.getWeekDisplayed(position); break;
                }
                getSupportActionBar().setTitle(title);

                if (position == 0) {
                    adapter.addPrev();
                    pager.setCurrentItem(COUNT_PAGE, false);
                } else if (position == adapter.getCount() - 1) {
                    adapter.addNext();
                    pager.setCurrentItem(adapter.getCount() - (COUNT_PAGE + 1), false);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public void onFragmentListener(View view) {
    }
}
