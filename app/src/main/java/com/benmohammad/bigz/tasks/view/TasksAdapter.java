package com.benmohammad.bigz.tasks.view;

import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.benmohammad.bigz.R;
import com.benmohammad.bigz.tasks.view.TasksListViewData.TaskViewData;
import com.google.common.collect.ImmutableList;

import static com.google.common.base.Preconditions.checkNotNull;

public class TasksAdapter extends BaseAdapter {

    private ImmutableList<TaskViewData> mTasks;
    private TaskItemListener mItemListener;

    public void setItemListener(TaskItemListener mItemListener) {
        this.mItemListener = mItemListener;
    }

    public void replaceData(ImmutableList<TaskViewData> tasks) {
        mTasks = checkNotNull(tasks);
        notifyDataSetChanged();

    }

    @Override
    public int getCount() {
        return mTasks == null ? 0 : mTasks.size();
    }

    @Override
    public TaskViewData getItem(int position) {
        return mTasks.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View rowView = convertView;
        if(rowView == null) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            rowView = inflater.inflate(R.layout.task_item, parent, false);
        }

        final TaskViewData task = getItem(position);

        TextView titleTv = rowView.findViewById(R.id.title);
        titleTv.setText(task.title());

        CheckBox completeCB = rowView.findViewById(R.id.complete);
        completeCB.setChecked(task.completed());



        completeCB.setOnClickListener(
                __ -> {
                    if(mItemListener == null) return;

                    if(!task.completed()) {
                    mItemListener.onCompleteTaskClick(task.id());
                    } else {
                        mItemListener.onActivateTaskClick(task.id());
                    }
                });

        rowView.setOnClickListener(__ -> {
            if(mItemListener != null) mItemListener.onTaskClick(task.id());
        });

        return rowView;
    }


    public interface TaskItemListener {

        void onTaskClick(String id);
        void onCompleteTaskClick(String id);
        void onActivateTaskClick(String id);
    }
}
