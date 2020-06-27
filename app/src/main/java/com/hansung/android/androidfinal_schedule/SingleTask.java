package com.hansung.android.androidfinal_schedule;

import java.io.Serializable;

public class SingleTask implements Serializable {
    public String taskName;
    public String date;
    public String startTime;
    public String endTime;
    public String place;
    public String textMemo;
    public String image;
    public String video;
    public String audio;
    public int _id;

    public SingleTask(int _id, String taskName, String date, String startTime, String endTime, String place, String textMemo, String image, String video, String audio){
        this._id = _id;
        this.taskName = taskName;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.place = place;
        this.textMemo = textMemo;
        this.image = image;
        this.video = video;
        this.audio = audio;
    }
}
