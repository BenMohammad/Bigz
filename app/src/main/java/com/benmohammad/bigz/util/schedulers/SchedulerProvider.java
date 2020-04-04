package com.benmohammad.bigz.util.schedulers;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import javax.annotation.Nonnull;

import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class SchedulerProvider implements BaseSchedulerProvider {

    @Nullable private static SchedulerProvider INSTANCE;

    private SchedulerProvider(){}

    @NonNull
    @Override
    public Scheduler computation() {
        return Schedulers.computation();
    }

    @NonNull
    @Override
    public Scheduler io() {
        return Schedulers.io();
    }

    @Nonnull
    @Override
    public Scheduler ui() {
        return AndroidSchedulers.mainThread();
    }
}
