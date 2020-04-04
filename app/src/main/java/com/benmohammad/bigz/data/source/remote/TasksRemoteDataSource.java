package com.benmohammad.bigz.data.source.remote;

import androidx.annotation.NonNull;

import com.benmohammad.bigz.data.Task;
import com.benmohammad.bigz.data.TaskDetails;
import com.benmohammad.bigz.data.source.TasksDataSource;
import com.google.common.base.Optional;

import java.sql.Time;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.sql.StatementEvent;

import io.reactivex.Flowable;

public class TasksRemoteDataSource implements TasksDataSource {

    private static TasksRemoteDataSource INSTANCE;
    private static final int SERVICE_LATENCY_IN_MILLIS = 3000;
    private static final Map<String, Task> TASKS_SERVICE_DATA;

    static {
        TASKS_SERVICE_DATA = new LinkedHashMap<>();
        addTask("1234", "Build Better Apps", "Hacking Hacking ");
        addTask("5678", "Learn everything!!!!", "Becky was a lovely Dog WhiteFang");
    }

    public static TasksRemoteDataSource getInstance() {
        if(INSTANCE == null) {
            INSTANCE = new TasksRemoteDataSource();
        }
        return INSTANCE;
    }

    private TasksRemoteDataSource(){}

    private static void addTask(String id, String title, String description) {
        Task newTask = Task.create(id, TaskDetails.create(title, description, false));
        TASKS_SERVICE_DATA.put(newTask.id(), newTask);
    }

    @Override
    public Flowable<List<Task>> getTasks() {
        return Flowable.fromIterable(TASKS_SERVICE_DATA.values())
                .delay(SERVICE_LATENCY_IN_MILLIS, TimeUnit.MILLISECONDS)
                .toList()
                .toFlowable();
    }

    @Override
    public Flowable<Optional<Task>> getTask(@NonNull String taskId) {
        final Task task = TASKS_SERVICE_DATA.get(taskId);
        if(task != null) {
            return Flowable.just(Optional.of(task))
                    .delay(SERVICE_LATENCY_IN_MILLIS, TimeUnit.MILLISECONDS);
        } else {
            return Flowable.empty();
        }
    }

    @Override
    public void saveTask(@NonNull Task task) {
        TASKS_SERVICE_DATA.put(task.id(), task);
    }

    @Override
    public void deleteAllTasks() {
        TASKS_SERVICE_DATA.clear();
    }

    @Override
    public void deleteTask(@NonNull String taskId) {
        TASKS_SERVICE_DATA.remove(taskId);
    }
}
