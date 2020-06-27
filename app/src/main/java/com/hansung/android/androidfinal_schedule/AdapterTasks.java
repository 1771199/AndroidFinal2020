package com.hansung.android.androidfinal_schedule;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class AdapterTasks extends BaseAdapter {
    Context context;
    ArrayList<SingleTask> task;

    public AdapterTasks(Context context, ArrayList<SingleTask> task){
        this.context = context;
        this.task = task;
    }

    @Override
    public int getCount() { return task.size(); }

    @Override
    public Object getItem(int position) { return task.get(position); }

    @Override
    public long getItemId(int position) { return position; }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        TaskViewHolder taskViewHolder;
        if(convertView == null){
            //LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.task_item, null, false);

            taskViewHolder = new TaskViewHolder();
            taskViewHolder.taskName = convertView.findViewById(R.id.name_of_task);
            taskViewHolder.taskDate = convertView.findViewById(R.id.date_of_task);

            convertView.setTag(taskViewHolder);
        }
        else{
            taskViewHolder = (TaskViewHolder)convertView.getTag();
        }


        TextView taskDate = convertView.findViewById(R.id.date_of_task);
        TextView taskName = convertView.findViewById(R.id.name_of_task);

        SingleTask singleTask = task.get(position);
        Log.e("Position", String.valueOf(position) );

        taskDate.setText(singleTask.date);
        taskName.setText(singleTask.taskName);

        convertView.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, TaskActivity.class);
                intent.putExtra("isNew", false);
                intent.putExtra("SingleTask", task.get(position));
                context.startActivity(intent);

            }


        }));

        return convertView;
    }

    class TaskViewHolder{
        TextView taskName;
        TextView taskDate;
    }

    public void addTask(SingleTask singleTask){
        task.add(singleTask);
    }


}
