package com.benmohammad.bigz.tasks.domain;


import androidx.annotation.NonNull;

import com.benmohammad.bigz.data.Task;
import com.benmohammad.bigz.tasks.domain.TasksListEvent.NavigateToTaskDetailsRequested;
import com.benmohammad.bigz.tasks.domain.TasksListEvent.TaskMarkedActive;
import com.benmohammad.bigz.tasks.domain.TasksListEvent.TasksLoaded;
import com.google.common.base.Optional;
import com.google.common.collect.ImmutableList;
import com.spotify.mobius.First;
import com.spotify.mobius.Next;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;

import static com.benmohammad.bigz.tasks.domain.TasksListEffect.deleteTasks;
import static com.benmohammad.bigz.tasks.domain.TasksListEffect.loadTasks;
import static com.benmohammad.bigz.tasks.domain.TasksListEffect.navigateToTaskDetails;
import static com.benmohammad.bigz.tasks.domain.TasksListEffect.refreshTasks;
import static com.benmohammad.bigz.tasks.domain.TasksListEffect.saveTask;
import static com.benmohammad.bigz.tasks.domain.TasksListEffect.showFeedback;
import static com.benmohammad.bigz.tasks.domain.TasksListEffect.startTaskCreationFlow;
import static com.benmohammad.bigz.tasks.domain.TasksListEvent.*;
import static com.google.common.base.Preconditions.checkNotNull;
import static com.spotify.mobius.Effects.effects;
import static com.spotify.mobius.Next.dispatch;
import static com.spotify.mobius.Next.next;

public final class TasksListLogic {

    private TasksListLogic(){}

    @NonNull
    public static First<TasksListModel, TasksListEffect> init(TasksListModel model) {
        if(model.tasks() == null) {
            return First.first(model.withLoading(true), effects(refreshTasks(), loadTasks()));
        } else {
            return First.first(model, effects(loadTasks()));
        }
    }

    @NonNull
    public static Next<TasksListModel, TasksListEffect> update(TasksListModel model, TasksListEvent event) {
        return event.map(
                refreshRequest -> onRefreshRequested(model),
                newTaskClicked -> onNewTaskClicked(),
                navigateToTaskDetails -> onNavigateToTaskDetailsRequested(model, navigateToTaskDetails),
                taskCompleted -> onTaskCompleted(model, taskCompleted),
                taskActivated -> onTaskActivated(model, taskActivated),
                completedTasksCleared -> onCompletedTasksCleared(model),
                filterSelected -> onFilteredSelected(model, filterSelected),
                tasksLoaded -> onTaskLoaded(model, tasksLoaded),
                tasksCreated -> onTaskCreated(),
                taskRefreshed -> onTaskRefreshed(model),
                taskRefreshFailed -> onTaskRefreshFail(model),
                taskLoadingFailed -> onTaskLoadingFailed(model));
    }

    private static Next<TasksListModel, TasksListEffect> onRefreshRequested(TasksListModel model) {
        return next(model.withLoading(true), effects(refreshTasks()));
    }

    private static Next<TasksListModel, TasksListEffect> onNewTaskClicked() {
        return Next.dispatch(effects(startTaskCreationFlow()));
    }

    private static Next<TasksListModel, TasksListEffect> onNavigateToTaskDetailsRequested(TasksListModel model, NavigateToTaskDetailsRequested event) {
        Optional<Task> task = model.findTaskById(event.taskId());
        if(!task.isPresent()) throw new IllegalArgumentException("task does not exist");
        return Next.dispatch(effects(navigateToTaskDetails(task.get())));
    }

    private static Next<TasksListModel, TasksListEffect> onTaskCompleted(TasksListModel model, TaskMarkedComplete event) {
        int taskIndex = model.findTaskIndexById(event.taskId());
        if(taskIndex < 0)  new IllegalArgumentException("Task does not exist");
        Task updatedTask = checkNotNull(model.tasks().get(taskIndex).complete());
        return updateTask(updatedTask, model, taskIndex, FeedbackType.MARKED_COMPLETE);
    }

    private static Next<TasksListModel, TasksListEffect> onTaskActivated(TasksListModel model, TaskMarkedActive event) {
        int taskIndex = model.findTaskIndexById(event.taskId());
        if(taskIndex < 0) throw new IllegalArgumentException("task does not exist");
        Task updatedTask = checkNotNull(model.tasks().get(taskIndex).complete());
        return updateTask(updatedTask, model, taskIndex, FeedbackType.MARKED_ACTIVE);
    }

    private static Next<TasksListModel, TasksListEffect> updateTask(Task updatedTask, TasksListModel model, int index, FeedbackType feedbackType) {
        return next(model.withTaskAtIndex(updatedTask, index),
                effects(saveTask(updatedTask), showFeedback(feedbackType)));
    }

    private static Next<TasksListModel, TasksListEffect> onCompletedTasksCleared(TasksListModel model) {
        ImmutableList<Task> allTasks = checkNotNull(model.tasks());
        List<Task> completedTasks = Observable.fromIterable(allTasks)
                .filter(t -> t.details().completed())
                .toList()
                .blockingGet();
        if(completedTasks.isEmpty())
            return Next.noChange();
        ArrayList<Task> newTasks = new ArrayList<>(allTasks);
        newTasks.removeAll(completedTasks);
        return next(model.withTasks(ImmutableList.copyOf(newTasks)),
                effects(deleteTasks(ImmutableList.copyOf(completedTasks)),
                        showFeedback(FeedbackType.CLEARED_COMPLETED)));

    }

    private static Next<TasksListModel, TasksListEffect> onFilteredSelected(TasksListModel model, FilterSelected event) {
        return next(model.withTasksFilter(event.filterType()));
    }

    private static Next<TasksListModel, TasksListEffect> onTaskLoaded(TasksListModel model, TasksLoaded event) {
        if(model.loading() && event.task().isEmpty()) {
            return Next.noChange();
        } else {
            return event.task().equals(model.tasks()) ? Next.noChange() : next(model.withTasks(event.task()));
        }
    }

    private static Next<TasksListModel, TasksListEffect> onTaskCreated() {
        return dispatch(effects(showFeedback(FeedbackType.SAVED_SUCCESSFULLY)));
    }

    private static Next<TasksListModel, TasksListEffect> onTaskRefreshed(TasksListModel model) {
        return next(model.withLoading(false), effects(loadTasks()));
    }

    private static Next<TasksListModel, TasksListEffect> onTaskRefreshFail(TasksListModel model) {
        return next(model.withLoading(false), effects(showFeedback(FeedbackType.LOADING_ERROR)));
    }

    private static Next<TasksListModel, TasksListEffect> onTaskLoadingFailed(TasksListModel model) {
        return next(model.withLoading(false), effects(showFeedback(FeedbackType.LOADING_ERROR)));
    }
}
