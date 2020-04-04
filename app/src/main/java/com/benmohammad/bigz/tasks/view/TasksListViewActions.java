package com.benmohammad.bigz.tasks.view;

public interface TasksListViewActions {

    void showTaskMarkedComplete();
    void showTaskMarkedActive();
    void showCompletedTasksCleared();
    void showLoadingTasksError();
    void showSuccessfullySavedMessage();
}
