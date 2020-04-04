package com.benmohammad.bigz.data.source.local;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.benmohammad.bigz.data.Task;
import com.benmohammad.bigz.data.TaskDetails;
import com.benmohammad.bigz.data.source.TasksDataSource;
import com.benmohammad.bigz.util.schedulers.BaseSchedulerProvider;
import com.google.common.base.Optional;
import com.squareup.sqlbrite2.BriteDatabase;
import com.squareup.sqlbrite2.SqlBrite;

import java.util.List;

import javax.annotation.Nonnull;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.functions.Function;

import static com.benmohammad.bigz.data.source.local.TasksPersistenceContract.TaskEntry.COLUMN_NAME_COMPLETED;
import static com.benmohammad.bigz.data.source.local.TasksPersistenceContract.TaskEntry.COLUMN_NAME_DESCRIPTION;
import static com.benmohammad.bigz.data.source.local.TasksPersistenceContract.TaskEntry.COLUMN_NAME_ENTRY_ID;
import static com.benmohammad.bigz.data.source.local.TasksPersistenceContract.TaskEntry.COLUMN_NAME_TITLE;
import static com.benmohammad.bigz.data.source.local.TasksPersistenceContract.TaskEntry.TABLE_NAME;
import static com.google.common.base.Preconditions.checkNotNull;

public class TasksLocalDataSource implements TasksDataSource {

    @Nullable private static TasksLocalDataSource INSTANCE;
    @NonNull private final BriteDatabase  mDatabaseHelper;
    @NonNull private Function<Cursor, Task> mTaskMapperFunction;

    private TasksLocalDataSource(@Nonnull Context context,
                                 @Nonnull BaseSchedulerProvider schedulerProvider) {
        checkNotNull(context, "context cannot be null");
        checkNotNull(schedulerProvider, "schedulerProvider cannot be null");
        TasksDbHelper dbHelper = new TasksDbHelper(context);
        SqlBrite sqlBrite = new  SqlBrite.Builder().build();
        mDatabaseHelper = sqlBrite.wrapDatabaseHelper(dbHelper, schedulerProvider.io());
        mTaskMapperFunction = this::getTask;
    }

    @NonNull
    private Task getTask(@Nonnull Cursor cursor) {
        String itemId = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME_ENTRY_ID));
        String title = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME_TITLE));
        String description = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME_DESCRIPTION));
        boolean isCompleted = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_NAME_COMPLETED)) == 1;
        TaskDetails details = TaskDetails.builder().title(title).description(description).completed(isCompleted).build();
        return Task.create(itemId, details);
    }

    public static TasksLocalDataSource getInstance(@NonNull Context context,
                                                   @Nonnull BaseSchedulerProvider schedulerProvider) {
        if(INSTANCE == null) {
            INSTANCE = new TasksLocalDataSource(context, schedulerProvider);
        }
        return INSTANCE;
    }

    public static void  destroyInstance() {
        INSTANCE = null;
    }

    @Override
    public Flowable<List<Task>> getTasks() {
        String[] projection = {
                COLUMN_NAME_ENTRY_ID,
                COLUMN_NAME_TITLE,
                COLUMN_NAME_DESCRIPTION,
                COLUMN_NAME_COMPLETED
        };
        String sql = String.format("SELECT %s FROM %s", TextUtils.join(",", projection), TABLE_NAME);
        return mDatabaseHelper
                .createQuery(TABLE_NAME, sql)
                .mapToList(mTaskMapperFunction)
                .toFlowable(BackpressureStrategy.BUFFER);
    }

    @Override
    public Flowable<Optional<Task>> getTask(@NonNull String taskId) {
        String[] projection = {
                COLUMN_NAME_ENTRY_ID,
                COLUMN_NAME_TITLE,
                COLUMN_NAME_DESCRIPTION,
                COLUMN_NAME_COMPLETED
        };

        String sql = String.format("Select $s From $s WHERE $s LIKE ?", TextUtils.join(",", projection), TABLE_NAME, COLUMN_NAME_ENTRY_ID);
        return mDatabaseHelper
                .createQuery(TABLE_NAME, sql, taskId)
                .mapToOneOrDefault(cursor -> Optional.of(mTaskMapperFunction.apply(cursor)), Optional.<Task>absent())
                .toFlowable(BackpressureStrategy.BUFFER);
    }

    @Override
    public void saveTask(@NonNull Task task) {
        checkNotNull(task);
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME_ENTRY_ID, task.id());
        values.put(COLUMN_NAME_TITLE, task.details().title());
        values.put(COLUMN_NAME_DESCRIPTION, task.details().description());
        values.put(COLUMN_NAME_COMPLETED, task.details().completed());
        mDatabaseHelper.insert(TABLE_NAME, values, SQLiteDatabase.CONFLICT_REPLACE);
    }

    @Override
    public void deleteAllTasks() {
        mDatabaseHelper.delete(TABLE_NAME, null);
    }

    @Override
    public void deleteTask(@NonNull String taskId) {
        String selection = COLUMN_NAME_ENTRY_ID + " LIKE ?";
        String[] selectionArgs = {taskId};
        mDatabaseHelper.delete(TABLE_NAME, selection, selectionArgs);
    }
}
