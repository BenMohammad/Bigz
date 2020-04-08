package com.benmohammad.bigz.taskdetails;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.benmohammad.bigz.R;
import com.benmohammad.bigz.data.Task;
import com.benmohammad.bigz.data.TaskBundlePacker;
import com.benmohammad.bigz.util.ActivityUtils;

public class TaskDetailActivity extends AppCompatActivity {

    public static final String EXTRA_TASK_ID = "TASK_ID";

    public static Intent showTask(Context context, Task task) {
        Intent i = new Intent(context, TaskDetailActivity.class);
        i.putExtra(EXTRA_TASK_ID, TaskBundlePacker.taskToBundle(task));
        return i;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.taskdetail_act);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayShowHomeEnabled(true);
        ab.setDisplayHomeAsUpEnabled(true);

        Task task = TaskBundlePacker.taskFromBundle(getIntent().getBundleExtra(EXTRA_TASK_ID));
        TaskDetailFragment detailFragment = (TaskDetailFragment) getSupportFragmentManager().findFragmentById(R.id.contentFrame);

        if(detailFragment == null) {
            detailFragment = TaskDetailFragment.newInstance(task);

            ActivityUtils.addFragmentToActivity(
                    getSupportFragmentManager(), detailFragment, R.id.contentFrame);

        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
