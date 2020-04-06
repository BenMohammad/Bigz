package com.benmohammad.bigz.data;

import com.google.auto.value.AutoValue;

@AutoValue
public abstract class TaskDetails {

    public static final TaskDetails DEFAULT = TaskDetails.builder().build();

    public abstract String title();
    public abstract String description();
    public abstract boolean completed();

    public TaskDetails withTitle(String title) {
        return toBuilder().title(title).build();
    }

    public TaskDetails withDescription(String description) {
        return toBuilder().description(description).build();
    }

    public TaskDetails withCompleted(boolean completed) {
        return toBuilder().completed(completed).build();
    }

    public abstract Builder toBuilder();

    public static TaskDetails create(String title, String description, boolean completed) {
        return builder().title(title).description(description).completed(completed).build();
    }

    public TaskDetails create(String title, String description) {
        return builder().title(title).description(description).completed(false).build();
    }

    public static Builder builder() {
        return new AutoValue_TaskDetails.Builder().completed(false).title("").description("");
    }



    @AutoValue.Builder
    public abstract static class Builder {
        public abstract Builder title(String title);
        public abstract Builder description(String description);
        public abstract Builder completed(boolean completed);
        public abstract TaskDetails build();
    }
}
