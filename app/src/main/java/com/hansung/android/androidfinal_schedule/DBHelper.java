package com.hansung.android.androidfinal_schedule;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBHelper extends SQLiteOpenHelper {
    final static String TAG="TaskDB";

    public DBHelper(Context context) {
        super(context, DataBases.DB_NAME, null, DataBases.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DataBases.Tasks.CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL(DataBases.Tasks.DELETE_TABLE);
        onCreate(db);
    }

    public void insertTaskBySQL(String name, String date, String startTime, String endTime, String place, String memo, String pic, String vid) {
        try {
            String sql = String.format (
                    "INSERT INTO %s (%s, %s, %s, %s, %s, %s, %s, %s, %s) VALUES (NULL, '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s')",
                    DataBases.Tasks.TABLE_NAME,
                    DataBases.Tasks._ID,
                    DataBases.Tasks.TASK_NAME,
                    DataBases.Tasks.DATE,
                    DataBases.Tasks.START_TIME,
                    DataBases.Tasks.END_TIME,
                    DataBases.Tasks.PLACE,
                    DataBases.Tasks.TEXT_MEMO,
                    DataBases.Tasks.PICTURE,
                    DataBases.Tasks.VIDEO,

                    name,
                    date,
                    startTime, endTime,
                    place,
                    memo, pic, vid
            );

            getWritableDatabase().execSQL(sql);
        } catch (SQLException e) {
            Log.e(TAG,"Error in inserting recodes");
        }
    }

    public Cursor getAllTasksBySQL() {
        String sql = "Select * FROM " + DataBases.Tasks.TABLE_NAME;
        return getReadableDatabase().rawQuery(sql,null);
    }

    public void deleteTaskBySQL(String _id) {
        try {
            String sql = String.format (
                    "DELETE FROM %s WHERE %s = %s",
                    DataBases.Tasks.TABLE_NAME,
                    DataBases.Tasks._ID,
                    _id);
            getWritableDatabase().execSQL(sql);
        } catch (SQLException e) {
            Log.e(TAG,"Error in deleting recodes");
        }
    }

    public void updateTaskBySQL(String _id, String name, String date, String startTime, String endTime, String place, String memo, String pic, String vid) {
        try {
            String sql = String.format (
                    "UPDATE  %s SET %s = '%s', %s = '%s', %s = '%s', %s = '%s', %s = '%s', %s = '%s', %s = '%s', %s = '%s' WHERE %s = %s",
                    DataBases.Tasks.TABLE_NAME,
                    DataBases.Tasks.TASK_NAME, name,
                    DataBases.Tasks.DATE, date,
                    DataBases.Tasks.START_TIME, startTime,
                    DataBases.Tasks.END_TIME, endTime,
                    DataBases.Tasks.PLACE, place,
                    DataBases.Tasks.TEXT_MEMO, memo,
                    DataBases.Tasks.PICTURE, pic,
                    DataBases.Tasks.VIDEO, vid,
                    DataBases.Tasks._ID, _id) ;
            getWritableDatabase().execSQL(sql);
        } catch (SQLException e) {
            Log.e(TAG,"Error in updating recodes");
        }
    }

    public long insertTaskByMethod(String name, String date, String startTime, String endTime, String place, String memo, String pic, String vid) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DataBases.Tasks.TASK_NAME, name);
        values.put(DataBases.Tasks.DATE, date);
        values.put(DataBases.Tasks.START_TIME, startTime);
        values.put(DataBases.Tasks.END_TIME, endTime);
        values.put(DataBases.Tasks.PLACE, place);
        values.put(DataBases.Tasks.TEXT_MEMO, memo);
        values.put(DataBases.Tasks.PICTURE, pic);
        values.put(DataBases.Tasks.VIDEO, vid);


        return db.insert(DataBases.Tasks.TABLE_NAME,null,values);
    }

    public Cursor getAllTaskByMethod() {
        SQLiteDatabase db = getReadableDatabase();
        return db.query(DataBases.Tasks.TABLE_NAME,null,null,null,null,null,null);
    }

    public long deleteTaskByMethod(String title) {
        SQLiteDatabase db = getWritableDatabase();

        String whereClause = DataBases.Tasks.TASK_NAME +" = ?";
        String[] whereArgs ={title};
        return db.delete(DataBases.Tasks.TABLE_NAME, whereClause, whereArgs);
    }

    public long updateTaskByMethod(String _id, String name, String date, String startTime, String endTime, String place, String memo, String pic, String vid) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(DataBases.Tasks.TASK_NAME, name);
        values.put(DataBases.Tasks.DATE, date);
        values.put(DataBases.Tasks.START_TIME, startTime);
        values.put(DataBases.Tasks.END_TIME, endTime);
        values.put(DataBases.Tasks.PLACE, place);
        values.put(DataBases.Tasks.TEXT_MEMO, memo);
        values.put(DataBases.Tasks.PICTURE, pic);
        values.put(DataBases.Tasks.VIDEO, vid);

        String whereClause = DataBases.Tasks._ID +" = ?";
        String[] whereArgs ={_id};

        return db.update(DataBases.Tasks.TABLE_NAME, values, whereClause, whereArgs);
    }

    public void syncAllTask(SQLiteDatabase db, String name, String date, String startTime, String endTime, String place, String memo, String pic, String vic){
        Cursor cursor = getAllTasksBySQL();
        while (cursor.moveToNext()){

        }
        ContentValues values = new ContentValues();
        values.put(DataBases.Tasks.TASK_NAME, name);
        values.put(DataBases.Tasks.DATE, date);
        values.put(DataBases.Tasks.START_TIME, startTime);
        values.put(DataBases.Tasks.END_TIME, endTime);
        values.put(DataBases.Tasks.PLACE, place);
        values.put(DataBases.Tasks.TEXT_MEMO, memo);
        values.put(DataBases.Tasks.PICTURE, pic);
        values.put(DataBases.Tasks.VIDEO, vic);
        cursor.moveToNext();
        db.insertOrThrow(DataBases.Tasks.TABLE_NAME, null, values);
    }

}
