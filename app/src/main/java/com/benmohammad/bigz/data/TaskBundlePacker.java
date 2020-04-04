package com.benmohammad.bigz.data;

import android.os.Bundle;

import static com.google.common.base.Preconditions.checkNotNull;

public class TaskBundlePacker {

    private static class TaskDetailsBundleIdentifiers {
        static final String TITLE = "task_title";
        static final String DESCRIPTION = "task_description";
        static final String STATUS = "task_status";
    }


    private static class TaskBundleIdentifiers {
        static final String ID = "task_id";
        static final String DETAILS = "task_details";
    }

    public static Bundle taskToBundle(Task task) {
        Bundle b = new Bundle();
        b.putString(TaskBundleIdentifiers.ID, task.id());
        b.putBundle(TaskBundleIdentifiers.DETAILS, taskDetailsToBundle(task.details()));
        return b;
    }

    public static Task taskFromBundle(Bundle bundle) {
        return Task.create(
                checkNotNull(bundle.getString(TaskBundleIdentifiers.ID)),
                taskDetailsFromBundle(checkNotNull(bundle.getBundle(TaskBundleIdentifiers.DETAILS))));
    }

    public static Bundle taskDetailsToBundle(TaskDetails details) {
        Bundle bundle = new Bundle();
        bundle.putString(TaskDetailsBundleIdentifiers.TITLE, details.title());
        bundle.putString(TaskDetailsBundleIdentifiers.DESCRIPTION, details.description());
        bundle.putBoolean(TaskDetailsBundleIdentifiers.STATUS, details.completed());
        return bundle;
    }

    public static TaskDetails taskDetailsFromBundle(Bundle bundle) {
        String title = checkNotNull(bundle.getString(TaskDetailsBundleIdentifiers.TITLE));
        String description = checkNotNull(bundle.getString(TaskDetailsBundleIdentifiers.DESCRIPTION));
        return TaskDetails.builder()
                    .title(title)
                    .description(description)
                    .completed(bundle.getBoolean(TaskDetailsBundleIdentifiers.STATUS))
                    .build();
    }
}
