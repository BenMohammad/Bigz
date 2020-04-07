package com.benmohammad.bigz.taskdetails.view;

import com.google.auto.value.AutoValue;

@AutoValue
public abstract class TaskDetailViewData {

    public abstract TextViewData title();

    public abstract TextViewData description();

    public abstract boolean completedChecked();

    public static Builder builder() {
        return new AutoValue_TaskDetailViewData.Builder();
    }

    @AutoValue.Builder
    public abstract static class Builder {
        public abstract Builder title(TextViewData title);
        public abstract Builder description(TextViewData description);
        public abstract Builder completedChecked(boolean completedChecked);

        public abstract TaskDetailViewData build();
    }

    @AutoValue
    public abstract static class TextViewData {
        public abstract int visibility();
        public abstract String text();
        public static TextViewData create(int visibility, String text) {
            return new AutoValue_TaskDetailViewData_TextViewData(visibility, text);
        }
    }
}
