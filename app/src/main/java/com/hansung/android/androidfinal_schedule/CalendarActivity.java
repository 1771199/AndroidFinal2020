package com.hansung.android.androidfinal_schedule;

import androidx.viewpager.widget.ViewPager;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Debug;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;


public class CalendarActivity extends BaseActivity implements CalendarFragment.OnFragmentListener {

    private static final int COUNT_PAGE = 12;
    ViewPager pager;
    private AdapterCalendar adapter;
    private AdapterTasks adapterTasks;
    public static String title;
    public static String selected = "";
    private DBHelper dbHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        initialize();
        //viewAllToListView();

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

    private void viewAllToListView(){
        Cursor cursor = dbHelper.getAllTaskByMethod();

        SimpleCursorAdapter adapter = new SimpleCursorAdapter(getApplicationContext(),
                R.layout.task_item, cursor, new String[]{
                DataBases.Tasks.TASK_NAME,
                DataBases.Tasks.DATE},
                new int[]{R.id.name_of_task, R.id.date_of_task}, 0);

        ListView lv = (ListView)findViewById(R.id.task_list);
        lv.setAdapter(adapter);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Adapter adapter = adapterView.getAdapter();
                TextView mName = findViewById(R.id.name_of_task);
                TextView nDate = findViewById(R.id.date_of_task);
                mName.setText(((Cursor)adapter.getItem(i)).getString(1));
                nDate.setText(((Cursor)adapter.getItem(i)).getString(2));
            }
        });
        lv.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
    }
}
