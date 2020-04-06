package com.benmohammad.bigz.addedittask.domain;

import com.benmohammad.bigz.data.TaskDetails;
import com.google.auto.value.AutoValue;

@AutoValue
public abstract class AddEditTaskModel {

    public abstract AddEditTaskMode mode();
    public abstract TaskDetails details();
    public AddEditTaskModel withDetails(TaskDetails details) {

        return toBuilder().details(details).build();
    }

    public abstract Builder toBuilder();

    public static Builder builder() {
        return new AutoValue_AddEditTaskModel.Builder();
    }

    @AutoValue.Builder
    public abstract static class Builder {
        public abstract Builder mode(AddEditTaskMode mode);
        public abstract Builder details(TaskDetails details);
        public abstract AddEditTaskModel build();
    }
}
