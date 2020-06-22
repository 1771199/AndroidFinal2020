package com.hansung.android.androidfinal_schedule;

import android.provider.BaseColumns;

public final class DataBases {
    public static final String DB_NAME = "/data/data/com.hansung.android.androidfinal_schedule/files/task.db";
    public static final int DATABASE_VERSION = 1;
    private static final String TEXT_TYPE = " TEXT";
    private static final String COMMA_SEP = ",";
    public static final class Tasks implements BaseColumns{

        public static final String TABLE_NAME="tasks";
        public static final String TASK_NAME = "name";
        public static final String DATE = "date";
        public static final String START_TIME = "start";
        public static final String END_TIME = "end";
        public static final String PLACE = "place";
        public static final String TEXT_MEMO = "memo";
        public static final String PICTURE = "picture";
        public static final String VIDEO = "video";
        
        public static final String CREATE_TABLE 
                = "CREATE TABLE " + TABLE_NAME + " (" + _ID + " INTEGER PRIMARY KEY" +
                COMMA_SEP + TASK_NAME + TEXT_TYPE + COMMA_SEP + DATE + TEXT_TYPE + COMMA_SEP +
                START_TIME +TEXT_TYPE + COMMA_SEP + END_TIME + TEXT_TYPE + COMMA_SEP +
                PLACE + TEXT_TYPE + COMMA_SEP + TEXT_MEMO + TEXT_TYPE + COMMA_SEP +
                PICTURE + TEXT_TYPE + COMMA_SEP + VIDEO + TEXT_TYPE + " )";
        public  static final String DELETE_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
    }
}
