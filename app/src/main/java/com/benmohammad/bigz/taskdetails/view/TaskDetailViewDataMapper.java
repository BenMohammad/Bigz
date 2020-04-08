package com.benmohammad.bigz.taskdetails.view;

import android.view.View;

import com.benmohammad.bigz.data.Task;
import com.benmohammad.bigz.data.TaskDetails;
import com.benmohammad.bigz.taskdetails.view.TaskDetailViewData.TextViewData;

import static com.google.common.base.Strings.isNullOrEmpty;

public class TaskDetailViewDataMapper {

    public static TaskDetailViewData taskToTaskViewData(Task task) {
        TaskDetails details = task.details();
        String title = details.title();
        String description = details.description();

        return TaskDetailViewData.builder()
                .title(TextViewData.create(isNullOrEmpty(title) ? View.GONE : View.VISIBLE, title))
                .description(TextViewData.create(isNullOrEmpty(description) ? View.GONE : View.VISIBLE, description))
                .completedChecked(details.completed())
                .build();
    }
}
