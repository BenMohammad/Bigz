package com.benmohammad.bigz.data;

import com.google.auto.value.AutoValue;

@AutoValue
public abstract class Task {

    public abstract String id();
    public abstract TaskDetails details();

    public Task withDetails(TaskDetails details) {
        return create(id(), details);
    }

    public static Task create(String id, TaskDetails details) {
        return new AutoValue_Task(id, details);
    }

    public Task complete() {
        return withDetails(details().withCompleted(true));
    }

    public Task activate() {
        return withDetails(details().withCompleted(false));
    }
}
