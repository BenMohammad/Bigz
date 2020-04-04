package com.benmohammad.bigz.util.schedulers;

import androidx.annotation.NonNull;

import javax.annotation.Nonnull;

import io.reactivex.Scheduler;
import io.reactivex.schedulers.Schedulers;

public class ImmediateSchedulerProvider implements BaseSchedulerProvider {



    @NonNull
    @Override
    public Scheduler computation() {
        return Schedulers.trampoline();
    }

    @NonNull
    @Override
    public Scheduler io() {
        return Schedulers.trampoline();
    }

    @Nonnull
    @Override
    public Scheduler ui() {
        return Schedulers.trampoline();
    }
}
