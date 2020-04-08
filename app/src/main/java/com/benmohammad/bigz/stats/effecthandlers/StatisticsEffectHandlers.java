package com.benmohammad.bigz.stats.effecthandlers;

import android.content.Context;

import com.benmohammad.bigz.data.source.local.TasksLocalDataSource;
import com.benmohammad.bigz.stats.domain.StatisticsEffect;
import com.benmohammad.bigz.stats.domain.StatisticsEvent;
import com.benmohammad.bigz.util.schedulers.SchedulerProvider;
import com.google.common.collect.ImmutableList;
import com.spotify.mobius.rx2.RxMobius;

import io.reactivex.ObservableTransformer;

import static com.benmohammad.bigz.stats.domain.StatisticsEffect.*;

public class StatisticsEffectHandlers {

    public static ObservableTransformer<StatisticsEffect, StatisticsEvent> createEffectHandler(
            Context context) {
        TasksLocalDataSource localSource = TasksLocalDataSource.getInstance(context, SchedulerProvider.getInstance());
        return RxMobius.<StatisticsEffect, StatisticsEvent>subtypeEffectHandler()
              .addTransformer(LoadTasks.class, loadTaskHandler(localSource))
                .build();
    }

    private static ObservableTransformer<LoadTasks, StatisticsEvent> loadTaskHandler(
            TasksLocalDataSource localSource) {
        return effects -> effects.flatMap(
                loadTasks -> localSource
                .getTasks()
                .toObservable()
                .take(1)
                .map(ImmutableList::copyOf)
                .map(StatisticsEvent::tasksLoaded)
                .onErrorReturnItem(StatisticsEvent.tasksLoadingFailed()));
    }
}
