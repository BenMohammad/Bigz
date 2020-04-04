package com.benmohammad.bigz.tasks.view;

import androidx.annotation.Nullable;

import com.benmohammad.bigz.R;
import com.benmohammad.bigz.data.Task;
import com.benmohammad.bigz.tasks.domain.TaskFilters;
import com.benmohammad.bigz.tasks.domain.TasksFilterType;
import com.benmohammad.bigz.tasks.domain.TasksListModel;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;

import static com.benmohammad.bigz.tasks.view.EmptyTasksViewDataMapper.createEmptyTaskViewData;
import static com.benmohammad.bigz.tasks.view.ViewState.hasTasks;
import static com.google.common.collect.ImmutableList.*;
import static com.google.common.collect.Iterables.*;

public class TasksListViewDataMapper {

    public static TasksListViewData tasksListModelYoViewData(TasksListModel model) {
        return TasksListViewData.builder()
                .loading(model.loading())
                .filterLabel(getFilterLabel(model.filter()))
                .viewState(getViewState(model.tasks(), model.filter()))
                .build();
    }

    private static ViewState getViewState(@Nullable ImmutableList<Task> tasks, TasksFilterType filter) {
        if (tasks == null) return ViewState.awaitingTasks();
        ImmutableList<Task> filteredTasks = TaskFilters.filterTasks(tasks, filter);
        if (filteredTasks.isEmpty()) {
            return ViewState.emptyTasks(createEmptyTaskViewData(filter));

        } else {
            return hasTasks(copyOf(transform(filteredTasks, TaskViewDataMapper::createTaskViewData)));
        }
    }
    private static int getFilterLabel(TasksFilterType filterType) {
        switch (filterType) {
            case ACTIVE_TASKS:
                return R.string.label_active;
            case COMPLETED_TASKS:
                return R.string.label_completed;
            default:
                return R.string.label_all;
        }
    }
}
