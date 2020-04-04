package com.benmohammad.bigz.tasks.domain;

import com.benmohammad.bigz.data.Task;
import com.google.common.collect.ImmutableList;
import com.spotify.dataenum.DataEnum;
import com.spotify.dataenum.dataenum_case;

@DataEnum
interface TasksListEvent_dataenum {

    dataenum_case RefreshRequested();
    dataenum_case NewTaskClicked();
    dataenum_case NavigateToTaskDetailsRequested(String taskId);
    dataenum_case TaskMarkedComplete(String taskId);
    dataenum_case TaskMarkedActive(String taskId);
    dataenum_case ClearCompletedTasksRequested();
    dataenum_case FilterSelected(TasksFilterType filterType);
    dataenum_case TasksLoaded(ImmutableList<Task> task);
    dataenum_case TaskCreated();
    dataenum_case TasksRefreshed();
    dataenum_case TasksRefreshFailed();
    dataenum_case TasksLoadingFailed();
}
