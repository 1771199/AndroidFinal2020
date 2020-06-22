package com.hansung.android.androidfinal_schedule;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class CalendarFragment extends Fragment {
    private int position;
    private long timeByMillis;
    private OnFragmentListener onFragmentListener;
    private ViewGroup mRootView;
    private GridView gridView;
    private GridAdapter gridAdapter;
    public static ArrayList<String> dayList;

    public static Calendar calendar;

    public Calendar getCalendar(){
        return calendar;
    }

    public void setCalendar(Long date){
        calendar = Calendar.getInstance();

        calendar.setTimeInMillis(date);
    }


    public void setOnFragmentListener(OnFragmentListener onFragmentListener) {
        this.onFragmentListener = onFragmentListener;
    }

    public interface OnFragmentListener {
        public void onFragmentListener(View view);
    }

    public static CalendarFragment newInstance(int position) {
        CalendarFragment frg = new CalendarFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("position", position);
        frg.setArguments(bundle);
        return frg;
    }

    public CalendarFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        position = getArguments().getInt("position");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
       dayList = new ArrayList<String>();
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timeByMillis);
        if(MainActivity.calendarType == 0 ){
            mRootView = (ViewGroup)inflater.inflate(R.layout.calendar_layout, container, false);
            gridView = (GridView) mRootView.findViewById(R.id.calendar_grid);
            calendar.setFirstDayOfWeek(Calendar.SUNDAY);
            calendar.set(Calendar.DATE,1);
            gridView.setNumColumns(7);
            //Calendar를 해당 달의 1일로 변경하여 1일이 무슨 요일인지 판단하고 요일에 맞게 dayList에 공백을 추가한다.
            int dayNum = calendar.get(Calendar.DAY_OF_WEEK);
            for (int i = 1; i < dayNum; i++){
                dayList.add("");
            }

            setCalendarDate(calendar.get(Calendar.MONTH)+1);
        }

        else if(MainActivity.calendarType == 1){
            mRootView = (ViewGroup)inflater.inflate(R.layout.calendar_layout, container, false);
            gridView = (GridView) mRootView.findViewById(R.id.calendar_grid);
            calendar.setFirstDayOfWeek(Calendar.SUNDAY);
            gridView.setNumColumns(7);
            calendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
            setWeekCalendarDate(calendar);

            }
        else{
            mRootView = (ViewGroup)inflater.inflate(R.layout.day_calendar_layout, container, false);
            gridView = (GridView) mRootView.findViewById(R.id.calendar_grid);
            dayList.add(String.valueOf(calendar.get(Calendar.DATE)));
            TextView day = mRootView.findViewById(R.id.day);
            day.setText(dayOfWeek(calendar));
        }

        //GridAdapter에 dayList를 담아 gridView와 연결한 후 ViewGroup을 반환한다.
        gridAdapter = new GridAdapter(getContext(), dayList);
        gridView.setAdapter(gridAdapter);
        return mRootView;
    }

    private void setCalendarDate(int month){
        //공백만 담겨 있는 dayList에 해당 월의 마지막 날까지 숫자를 추가한다.
        calendar.set(Calendar.MONTH, month -1);

        for (int i = 0; i < calendar.getActualMaximum(Calendar.DAY_OF_MONTH); i++){
            dayList.add(""+(i+1));
        }
    }

    private void setWeekCalendarDate(Calendar calendar){
        for (int i = 0; i < 7; i++){
            dayList.add(String.valueOf(calendar.get(Calendar.DATE)));
            calendar.add(Calendar.DATE, 1);
        }
    }

    private String dayOfWeek(Calendar calendar){
        switch (calendar.get(Calendar.DAY_OF_WEEK)){
            case 1:
                return "SUN";
            case 2:
                return "MON";
            case 3:
                return "TUE";
            case 4:
                return "WED";
            case 5:
                return "THU";
            case 6:
                return "FRI";
            default:
                return "SAT";
        }
    }


    public void setTimeByMillis(long timeByMillis) {
        this.timeByMillis = timeByMillis;
    }

    public class GridAdapter extends BaseAdapter {

        private final List<String> list;
        private final LayoutInflater inflater;
        private Context context;

        public GridAdapter(Context context, ArrayList<String> list){
            this.list = list;
            for(int i = 0; i<list.size(); i++)
            {
                String a = list.get(i);
            }
            this.context = context;
            this.inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }
        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public String getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup container){

            ViewHolder holder = null;

            if(convertView == null){
                convertView = inflater.inflate(R.layout.oneday_item, container, false);
                holder = new ViewHolder();

                holder.tvItemGridView = (TextView)convertView.findViewById(R.id.oneDay);

                convertView.setTag(holder);
            }
            else{
                holder = (ViewHolder)convertView.getTag();
            }
            holder.tvItemGridView.setText(""+getItem(position));
            calendar = Calendar.getInstance();
            final ViewHolder finalHolder = holder;
            // 캘린더의 '일'이 클릭되었을 때, title로 표시되고 있는 연, 월과 해당 textView의 text를 조합하여 "연, 월, 일" 형태로 만들어 Toast로 출력한다.
            convertView.setOnClickListener((new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String YM = "";
                    if(MainActivity.calendarType == 1){
                        String WYM = String.valueOf(CalendarActivity.title);
                        YM = WYM.split(" ")[0] + " " + WYM.split(" ")[1];
                    }
                    else  YM = CalendarActivity.title;
                    String day = finalHolder.tvItemGridView.getText().toString();
                    CalendarActivity.selected = YM + " " + day + "일";
                    Toast.makeText(context, YM + " " + day + "일", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getActivity(), TaskListActivity.class);
                    startActivity(intent);

                }


            }));

            return convertView;
        }

    }

    private class ViewHolder{
        TextView tvItemGridView;
    }


}
