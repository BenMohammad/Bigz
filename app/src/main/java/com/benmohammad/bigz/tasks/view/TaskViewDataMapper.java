package com.benmohammad.bigz.tasks.view;

import com.benmohammad.bigz.R;
import com.benmohammad.bigz.data.Task;
import com.benmohammad.bigz.data.TaskDetails;
import com.google.common.base.Strings;

public class TaskViewDataMapper {

    public static TasksListViewData.TaskViewData createTaskViewData(Task task) {
        if(task == null) return null;
        return TasksListViewData.TaskViewData.create(
                getTitleForList(task.details()),
                task.details().completed(),
                task.details().completed() ? R.drawable.list_completed_touch_feedback : R.drawable.touch_feedback,
                task.id()
        );
    }

    private static String getTitleForList(TaskDetails details) {
        if(!Strings.isNullOrEmpty(details.title())) {
            return details.title();
        } else {
            return details.description();
        }
    }
}
