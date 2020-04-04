package com.benmohammad.bigz.data.source;

import androidx.annotation.NonNull;

import com.benmohammad.bigz.data.Task;
import com.google.common.base.Optional;

import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.internal.operators.flowable.FlowableAny;

public interface TasksDataSource {

    Flowable<List<Task>> getTasks();
    Flowable<Optional<Task>> getTask(@NonNull String taskId);
    void saveTask(@NonNull Task task);
    void deleteAllTasks();
    void deleteTask(@NonNull String taskId);
}
