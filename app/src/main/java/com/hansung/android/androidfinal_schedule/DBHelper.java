package com.hansung.android.androidfinal_schedule;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.ArrayList;

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


    public Cursor getAllTasksBySQL() {
        String sql = "Select * FROM " + DataBases.Tasks.TABLE_NAME;
        return getReadableDatabase().rawQuery(sql,null);
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

    public long deleteUserByMethod(String _id) {
        SQLiteDatabase db = getWritableDatabase();

        String whereClause = DataBases.Tasks._ID + " = ?";
        String[] whereArgs = {_id};
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

    public void syncAllTask(SQLiteDatabase db, String name, String date, String startTime, String endTime, String place, String memo, String pic, String vid){
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
        values.put(DataBases.Tasks.VIDEO, vid);
        cursor.moveToNext();
        db.insertOrThrow(DataBases.Tasks.TABLE_NAME, null, values);
    }

    public ArrayList<SingleTask> selectDate(ArrayList<SingleTask> taskArray, String start, String end) {
        SQLiteDatabase db = this.getReadableDatabase();
        String SELECT = "SELECT * FROM " + DataBases.Tasks.TABLE_NAME +
                " WHERE " + DataBases.Tasks.DATE + " BETWEEN '" + start + "' AND '"
                + end + "' ORDER BY " + DataBases.Tasks.DATE + " ASC, " +
                DataBases.Tasks._ID + " ASC;";
        Cursor cursor = db.rawQuery(SELECT, null);
        while(cursor.moveToNext()){
            SingleTask addedTask = new SingleTask(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4),
                    cursor.getString(5), cursor.getString(6), cursor.getString(7), cursor.getString(8));
            taskArray.add(addedTask);
        }
        return taskArray;
    }

}