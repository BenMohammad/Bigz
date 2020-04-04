package com.benmohammad.bigz.util.schedulers;

import androidx.annotation.NonNull;

import javax.annotation.Nonnull;

import io.reactivex.Scheduler;

public interface BaseSchedulerProvider {

    @NonNull
    Scheduler computation();

    @NonNull
    Scheduler io();

    @Nonnull
    Scheduler ui();
}
