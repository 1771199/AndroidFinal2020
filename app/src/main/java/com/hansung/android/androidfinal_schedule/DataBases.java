package com.hansung.android.androidfinal_schedule;

import android.provider.BaseColumns;
import android.provider.ContactsContract;

import java.io.Serializable;

public final class DataBases  {
    //public static final String DB_NAME = "/data/data/com.hansung.android.androidfinal_schedule/files/task.db";
    public static final String DB_NAME = "Tasks.db";
    public static final int DATABASE_VERSION = 3;
    private DataBases() {}

    public static final class Tasks implements BaseColumns{

        public static final String TABLE_NAME="Tasks";
        public static final String TASK_NAME = "Name";
        public static final String DATE = "Date";
        public static final String START_TIME = "Start";
        public static final String END_TIME = "End";
        public static final String PLACE = "Place";
        public static final String TEXT_MEMO = "Memo";
        public static final String PICTURE = "Picture";
        public static final String VIDEO = "Video";
        public static final String AUDIO = "Audio";

        public static final String CREATE_TABLE =
        "CREATE TABLE " + "Tasks" + " (" + "_ID" + "	INTEGER PRIMARY KEY" +","+
                "Name"+" TEXT NOT NULL" + "," +
                "Date"+" TEXT NOT NULL" + ","+
                "Start"	+ " TEXT" + "," +
                "End" + " TEXT" + "," +
                "Place" + "	TEXT" + "," +
                "Memo" + " TEXT" + ","+
                "Picture"+ " TEXT" + "," +
                "Video"	+ " TEXT" + "," +
                "Audio" + " TEXT" +" )";

        public  static final String DELETE_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;

    }
}
