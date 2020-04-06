package com.benmohammad.bigz.addedittask.effecthandlers;

import android.content.Context;

import com.benmohammad.bigz.addedittask.domain.AddEditTaskEffect;
import com.benmohammad.bigz.addedittask.domain.AddEditTaskEffect.SaveTask;
import com.benmohammad.bigz.addedittask.domain.AddEditTaskEvent;
import com.benmohammad.bigz.data.Task;
import com.benmohammad.bigz.data.TaskDetails;
import com.benmohammad.bigz.data.source.TasksDataSource;
import com.benmohammad.bigz.data.source.local.TasksLocalDataSource;
import com.benmohammad.bigz.data.source.remote.TasksRemoteDataSource;
import com.benmohammad.bigz.util.schedulers.SchedulerProvider;
import com.spotify.mobius.rx2.RxMobius;

import java.util.UUID;

import io.reactivex.ObservableTransformer;
import io.reactivex.functions.Action;
import io.reactivex.functions.Function;

import static com.benmohammad.bigz.addedittask.domain.AddEditTaskEffect.*;
import static io.reactivex.android.schedulers.AndroidSchedulers.mainThread;

public class AddEditTaskEffectHandlers {

    public static ObservableTransformer<AddEditTaskEffect, AddEditTaskEvent> createEffectHandlers(
            Context context, Action showTasksList, Action showEmptyTaskError) {
        TasksRemoteDataSource remoteSource = TasksRemoteDataSource.getInstance();
        TasksLocalDataSource localSource =  TasksLocalDataSource.getInstance(context, SchedulerProvider.getInstance());

        return RxMobius.<AddEditTaskEffect, AddEditTaskEvent>subtypeEffectHandler()
                .addAction(NotifyEmptyTaskNotAllowed.class, showEmptyTaskError, mainThread())
                .addAction(Exit.class, showTasksList, mainThread())
                .addFunction(CreateTask.class, createTaskHandler(remoteSource, localSource))
                .addFunction(SaveTask.class, saveTaskHandler(remoteSource, localSource))
                .build();
    }

    static Function<CreateTask, AddEditTaskEvent> createTaskHandler(
            TasksDataSource remoteSource, TasksDataSource localSource) {
        return createTaskEffect -> {
            Task task = Task.create(UUID.randomUUID().toString(), createTaskEffect.taskDetails());
            try {
                remoteSource.saveTask(task);
                localSource.saveTask(task);
                return AddEditTaskEvent.taskCreatedSuccessfully();
            } catch (Exception e) {
                return AddEditTaskEvent.taskCreationFailed("Failed to create task...");
            }
        };
    }

    static Function<SaveTask, AddEditTaskEvent> saveTaskHandler(
            TasksDataSource remoteSource, TasksDataSource localSource) {
        return saveTasks -> {
            try {
                remoteSource.saveTask(saveTasks.task());
                localSource.saveTask(saveTasks.task());
                return AddEditTaskEvent.taskUpdatedSuccessfully();
            } catch (Exception e) {
                return AddEditTaskEvent.taskCreationFailed("Failed to update task");
            }
        };
    }
}
