package com.benmohammad.bigz.tasks.view;

import androidx.annotation.DrawableRes;
import androidx.annotation.StringRes;

import com.google.auto.value.AutoValue;
import com.google.common.collect.ImmutableList;
import com.spotify.dataenum.DataEnum;
import com.spotify.dataenum.dataenum_case;

@AutoValue
public abstract class TasksListViewData {


    @StringRes
    public abstract int filterLabel();
    public abstract boolean loading();
    public abstract ViewState viewState();

    public static Builder builder() {
        return new AutoValue_TasksListViewData.Builder();
    }

    @AutoValue.Builder
    public abstract static class Builder {
        public abstract Builder filterLabel(int filterLabel);
        public abstract Builder loading(boolean loading);
        public abstract Builder viewState(ViewState viewState);

        public abstract TasksListViewData build();

    }

    @AutoValue
    public abstract static class EmptyTaskViewData {
        @StringRes
        public abstract int title();
        @DrawableRes
        public abstract int icon();
        public abstract int addViewVisibility();

        public static Builder builder() {
            return new AutoValue_TasksListViewData_EmptyTaskViewData.Builder();

        }

    @AutoValue.Builder
    public abstract static class Builder {
        public abstract Builder title(int title);
        public abstract Builder icon(int icon);
        public abstract Builder addViewVisibility(int addViewVisibility);
        public abstract EmptyTaskViewData build();

    }
    }

    @AutoValue
    public abstract static class TaskViewData {
        public abstract String title();
        public abstract boolean completed();
        @DrawableRes
        public abstract int backgroundDrawableId();

        public abstract String id();

        public static TaskViewData create(
                String title, boolean isCompleted, int backgroundDrawableId, String id
        ) {
            return new AutoValue_TasksListViewData_TaskViewData(title, isCompleted, backgroundDrawableId, id);

        }
    }
    @DataEnum
    public interface ViewState_dataenum {
        dataenum_case AwaitingTasks();
        dataenum_case EmptyTasksViewData(ViewState.EmptyTasksViewData viewData);
        dataenum_case HasTasks(ImmutableList<TaskViewData> taskViewData);
    }
}
