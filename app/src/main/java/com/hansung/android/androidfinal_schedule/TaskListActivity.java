package com.hansung.android.androidfinal_schedule;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;

public class TaskListActivity extends AppCompatActivity {

    private DBHelper dbHelper;
    TextView taskName;
    SimpleDateFormat sdf;
    Button plus;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dbHelper = new DBHelper(this);
        taskName = findViewById(R.id.name);
        sdf = AdapterCalendar.sdf;
        setContentView(R.layout.activity_task_list);
        plus = findViewById(R.id.plus);
        View.OnClickListener btnListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("CLICK", "onClick: ");
                Intent intent = new Intent(getApplicationContext(), TaskActivity.class);
                startActivity(intent);
            }
        };
        plus.setOnClickListener(btnListener);
        //viewDayTask();
    }

    private void viewDayTask() {

        Cursor cursor = dbHelper.getAllTaskByMethod();
        if(cursor.getCount() != 0){
            SimpleCursorAdapter adapter = new SimpleCursorAdapter(getApplicationContext(),
                    R.layout.list_item, cursor, new String[]{
                    DataBases.Tasks.TASK_NAME, DataBases.Tasks.DATE},
                    new int[]{R.id.name}, 0);

            ListView lv = (ListView)findViewById(R.id.list_task);
            lv.setAdapter(adapter);

            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Adapter adapter = adapterView.getAdapter();
                    taskName.setText(((Cursor)adapter.getItem(i)).getString(0));
                }
            });
            lv.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        }


    }
}
