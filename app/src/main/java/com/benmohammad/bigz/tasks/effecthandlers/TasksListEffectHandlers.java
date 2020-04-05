package com.benmohammad.bigz.tasks.effecthandlers;

import android.content.Context;

import com.benmohammad.bigz.data.Task;
import com.benmohammad.bigz.data.TaskDetails;
import com.benmohammad.bigz.data.source.TasksDataSource;
import com.benmohammad.bigz.data.source.local.TasksLocalDataSource;
import com.benmohammad.bigz.data.source.remote.TasksRemoteDataSource;
import com.benmohammad.bigz.tasks.domain.TasksListEffect;
import com.benmohammad.bigz.tasks.domain.TasksListEffect.DeleteTasks;
import com.benmohammad.bigz.tasks.domain.TasksListEffect.LoadTasks;
import com.benmohammad.bigz.tasks.domain.TasksListEffect.NavigateToTaskDetails;
import com.benmohammad.bigz.tasks.domain.TasksListEffect.RefreshTasks;
import com.benmohammad.bigz.tasks.domain.TasksListEffect.SaveTask;
import com.benmohammad.bigz.tasks.domain.TasksListEffect.ShowFeedback;
import com.benmohammad.bigz.tasks.domain.TasksListEvent;
import com.benmohammad.bigz.tasks.view.TasksListViewActions;
import com.benmohammad.bigz.util.Either;
import com.benmohammad.bigz.util.schedulers.BaseSchedulerProvider;
import com.benmohammad.bigz.util.schedulers.SchedulerProvider;
import com.google.common.collect.ImmutableList;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.CompletableObserver;
import io.reactivex.Observable;
import io.reactivex.ObservableTransformer;
import io.reactivex.Single;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;

public class TasksListEffectHandlers {

    public static ObservableTransformer<TasksListEffect, TasksListEvent> createEffectHandler(
            Context context,
            TasksListViewActions view,
            Action showAddTask,
            Consumer<Task> showTaskDetails
    ) {
        TasksRemoteDataSource remoteSource = TasksRemoteDataSource.getInstance();
        TasksLocalDataSource localSource = TasksLocalDataSource.getInstance(context, SchedulerProvider.getInstance());

        return null;
    }

    static ObservableTransformer<RefreshTasks, TasksListEvent> refreshTaskHandler(
            TasksDataSource remoteSource, TasksDataSource localSource
    ) {
        Single<TasksListEvent> refreshTasksOperation =
                remoteSource
                .getTasks()
                .singleOrError()
                .map(Either::<Throwable, List<Task>> right)
                .flatMap(
                        either -> either.map(
                                left -> Single.just(TasksListEvent.tasksLoadingFailed()),
                                right -> Observable.fromIterable(right.Value())
                                .concatMapCompletable(
                                        t -> Completable.fromAction(() -> localSource.saveTask(t)))
                                .andThen(Single.just(TasksListEvent.tasksRefreshed()))
                                .onErrorReturnItem(TasksListEvent.tasksLoadingFailed())));

        return refreshTasks -> refreshTasks.flatMapSingle(__ -> refreshTasksOperation);
    }

    static ObservableTransformer<LoadTasks, TasksListEvent> loadTaskHandler(
            TasksDataSource dataSource) {
        return loadTasks ->
                loadTasks.flatMap(
                        effect ->
                                dataSource
                        .getTasks()
                        .toObservable()
                        .take(1)
                        .map(tasks -> TasksListEvent.tasksLoaded(ImmutableList.copyOf(tasks)))
                        .onErrorReturnItem(TasksListEvent.tasksLoadingFailed()));

    }

    static Consumer<SaveTask> saveTaskHandler(
            TasksDataSource remoteSource, TasksDataSource localSource
    ) {
        return saveTasks -> {
            remoteSource.saveTask(saveTasks.task());
            localSource.saveTask(saveTasks.task());
        };
    }

    static Consumer<DeleteTasks> deleteTaskHandler(
            TasksDataSource remoteSource, TasksDataSource localSource
    ) {
        return deleteTasks  -> {
            for(Task task : deleteTasks.tasks()) {
                remoteSource.deleteTask(task.id());
                localSource.deleteTask(task.id());
            }
        };
    }

    static Consumer<ShowFeedback> showFeedbackHandler(TasksListViewActions view) {
        return showFeedback -> {
            switch(showFeedback.feedbackType()) {
                case SAVED_SUCCESSFULLY:
                    view.showSuccessfullySavedMessage();
                    break;
                case MARKED_ACTIVE:
                    view.showTaskMarkedActive();
                    break;
                case MARKED_COMPLETE:
                    view.showTaskMarkedComplete();
                    break;
                case CLEARED_COMPLETED:
                    view.showCompletedTasksCleared();
                    break;
                case LOADING_ERROR:
                    view.showLoadingTasksError();
                    break;
            }
        };

    }

    static Consumer<NavigateToTaskDetails> navigateToDetailsHandler(Consumer<Task> command) {
        return navigateEffect -> command.accept(navigateEffect.task());
    }

}
