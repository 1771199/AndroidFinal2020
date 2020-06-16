package com.hansung.android.androidfinal_schedule;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
        mRootView = (ViewGroup)inflater.inflate(R.layout.calendar_layout, container, false);
        gridView = (GridView) mRootView.findViewById(R.id.calendar_grid);
        dayList = new ArrayList<String>();
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timeByMillis);
        calendar.set(Calendar.DATE,1);
        //Calendar를 해당 달의 1일로 변경하여 1일이 무슨 요일인지 판단하고 요일에 맞게 dayList에 공백을 추가한다.
        int dayNum = calendar.get(Calendar.DAY_OF_WEEK);
        for (int i = 1; i < dayNum; i++){
            Log.d("dayNum", String.valueOf(dayNum));
            dayList.add("");
        }
        setCalendarDate(calendar.get(Calendar.MONTH)+1);
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
                    String YM = CalendarActivity.title;
                    String day = finalHolder.tvItemGridView.getText().toString();
                    Toast.makeText(context, YM + " " + day + "일", Toast.LENGTH_SHORT).show();

                }


            }));

            return convertView;
        }

    }

    private class ViewHolder{
        TextView tvItemGridView;
    }


}
