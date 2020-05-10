package com.hansung.android.androidmidterm_calendar;

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

        int dayNum = calendar.get(Calendar.DAY_OF_WEEK);
        for (int i = 1; i < dayNum; i++){
            Log.d("dayNum", String.valueOf(dayNum));
            dayList.add("");
        }
        setCalendarDate(calendar.get(Calendar.MONTH)+1);

        gridAdapter = new GridAdapter(getContext(), dayList);
        gridView.setAdapter(gridAdapter);

        return mRootView;
    }

    private void setCalendarDate(int month){
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
