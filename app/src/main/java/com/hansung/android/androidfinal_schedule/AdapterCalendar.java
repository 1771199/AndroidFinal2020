package com.hansung.android.androidfinal_schedule;

import android.util.Log;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.fragment.app.FragmentTransaction;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class AdapterCalendar extends FragmentStatePagerAdapter {
    private HashMap<Integer, CalendarFragment> frgMap;
    private ArrayList<Long> listMonthByMillis = new ArrayList<>();
    private int numOfMonth;
    public static SimpleDateFormat sdf;
    private CalendarFragment.OnFragmentListener onFragmentListener;

    public AdapterCalendar(FragmentManager fm) {
        super(fm);
        clearPrevFragments(fm);
        frgMap = new HashMap<Integer, CalendarFragment>();
    }

    private void clearPrevFragments(FragmentManager fm) {
        // FragmentManager에서 관리하는 모든 Fragment를 list에 담는다.
        List<Fragment> listFragment = fm.getFragments();
        // Fragment가 null이 아니라면 fragment를 모두 지운다(초기화).
        if (listFragment != null) {
            FragmentTransaction ft = fm.beginTransaction();

            for (Fragment f : listFragment) {
                if (f instanceof CalendarFragment) {
                    ft.remove(f);
                }
            }
            // ft.commit()과 비슷한 역할을 한다.
            ft.commitAllowingStateLoss();
        }
    }

    @Override
    public Fragment getItem(int position) {
        CalendarFragment frg = null;
        // frgMap에 저장된 값이 있다면 position에 따라 fragment를 가져온다.
        if (frgMap.size() > 0) {
            frg = frgMap.get(position);
        }
        // frgMap에 저장된 값이 없다면 아직 frg는 null이다. 즉 저장된 fragment가 없음을 뜻하므로 fragment를 생성하고 frgMap에 추가한다.
        if (frg == null) {
            frg = CalendarFragment.newInstance(position);
            frg.setOnFragmentListener(onFragmentListener);
            frgMap.put(position, frg);
        }
        // 해당 fragment의 날짜를 listMonthbyMillis의 position에 대한 값을 가져와 설정한다.
        frg.setTimeByMillis(listMonthByMillis.get(position));
        Calendar set = frg.getCalendar();
        frg.setCalendar(listMonthByMillis.get(position));

        return frg;
    }

    @Override
    public int getCount() {
        return listMonthByMillis.size();
    }

    public void setNumOfMonth(int numOfMonth) {
        // 이 메소드는 CalendarActivity에서 사용되며 파라미터로 COUNT_PAGE(상수 12)가 들어온다.
        this.numOfMonth = numOfMonth;
        //현재 시간의 Calendar 객체를 생성하여 12달 전 월의 1일로 변경한다.
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, -numOfMonth);
        calendar.set(Calendar.DATE, 1);
        // 25번 만큼 반복문을 돌리며 listMonthByMillis에 캘린더의 시간 값을 저장하고 캘린더의 월을 하나씩 더해 준다.
        // 이를 통해 COUNT_PAGE를 index로 가지는 listMonthByMillis에는 현재 달의 1일 시간 값이 표시되며, 전후로 12개 씩의 값을 가진다.
        for (int i = 0; i < numOfMonth * 2 + 1; i++) {
            listMonthByMillis.add(calendar.getTimeInMillis());
            calendar.add(Calendar.MONTH, 1);
        }

        notifyDataSetChanged();
    }

    public void setNumOfWeeks(int numOfWeeks){
        this.numOfMonth = numOfWeeks;
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.WEEK_OF_YEAR, -numOfWeeks);
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
        for (int i = 0; i < numOfMonth * 2 + 1; i++) {
            listMonthByMillis.add(calendar.getTimeInMillis());
            calendar.add(Calendar.WEEK_OF_YEAR, 1);
        }
        notifyDataSetChanged();
    }

    public void setDate(int numOfWeeks){
        this.numOfMonth = numOfWeeks;
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, -numOfWeeks);
        for (int i = 0; i < numOfMonth * 2 + 1; i++) {
            listMonthByMillis.add(calendar.getTimeInMillis());
            calendar.add(Calendar.DATE, 1);
        }
        notifyDataSetChanged();
    }

    public void addNext() {
        // 배열에 저장된 가장 마지막 값을 lastMonthMillis로 꺼내온다.
        long lastMonthMillis = listMonthByMillis.get(listMonthByMillis.size() - 1);
        // calendar를 가장 마지막 값으로 설정한 후 for문을 돌리며 한 달씩 증가한 값을 listMonthByMillis에 추가한다.
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(lastMonthMillis);
        if (MainActivity.calendarType == 0){
            for (int i = 0; i < numOfMonth; i++) {
                calendar.add(Calendar.MONTH, 1);
                listMonthByMillis.add(calendar.getTimeInMillis());
            }
        }
        else if (MainActivity.calendarType == 1){
            for (int i = 0; i < numOfMonth; i++) {
                calendar.add(Calendar.WEEK_OF_YEAR, 1);
                listMonthByMillis.add(calendar.getTimeInMillis());
            }
        }
        else{
            for (int i = 0; i < numOfMonth * 2 + 1; i++) {
                calendar.add(Calendar.DATE, 1);
                listMonthByMillis.add(calendar.getTimeInMillis());
            }

        }


        notifyDataSetChanged();
    }

    public void addPrev() {
        long lastMonthMillis = listMonthByMillis.get(0);

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(lastMonthMillis);
        if(MainActivity.calendarType == 0){
            calendar.set(Calendar.DATE, 1);
            for (int i = numOfMonth; i > 0; i--) {
                calendar.add(Calendar.MONTH, -1);
                listMonthByMillis.add(0, calendar.getTimeInMillis());
            }
        }
        else if(MainActivity.calendarType == 1){
            for (int i = numOfMonth; i > 0; i--) {
                calendar.add(Calendar.WEEK_OF_YEAR, -1);
                listMonthByMillis.add(0, calendar.getTimeInMillis());
            }
        }
        else {
            for (int i = numOfMonth; i > 0; i--) {
                calendar.add(Calendar.DATE, -1);
                listMonthByMillis.add(0, calendar.getTimeInMillis());
            }
        }

        notifyDataSetChanged();
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    public String getMonthDisplayed(int position) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(listMonthByMillis.get(position));
        sdf = new SimpleDateFormat("yyyy년 MM월");
        Date date = new Date();
        date.setTime(listMonthByMillis.get(position));
        return sdf.format(date);
    }

    public String getWeekDisplayed(int position){
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(listMonthByMillis.get(position));
        String weekOfMonth = String.valueOf(calendar.get(Calendar.WEEK_OF_MONTH));
        sdf = new SimpleDateFormat("yyyy년 MM월");
        Date date = new Date();
        date.setTime(listMonthByMillis.get(position));
        String titleWeeks = sdf.format(date) + " " +  weekOfMonth + "째 주";
        return titleWeeks;
    }

    public void setOnFragmentListener(CalendarFragment.OnFragmentListener onFragmentListener) {
        this.onFragmentListener = onFragmentListener;
    }


}
