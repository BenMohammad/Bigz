package com.benmohammad.bigz.stats.domain;

import com.benmohammad.bigz.data.Task;
import com.google.common.collect.ImmutableList;
import com.spotify.mobius.First;
import com.spotify.mobius.Next;

import javax.annotation.Nonnull;

import static com.benmohammad.bigz.stats.domain.StatisticsEffect.loadTasks;
import static com.benmohammad.bigz.stats.domain.StatisticsState.*;
import static com.spotify.mobius.Effects.effects;
import static com.spotify.mobius.First.first;
import static com.spotify.mobius.Next.next;

public final class StatisticsLogic {

    @Nonnull
    public static First<StatisticsState, StatisticsEffect> init(StatisticsState state) {
        return state.map(
                loading -> first(state, effects(loadTasks())),
                First::first,
                failed -> first(loading(), effects(loadTasks())));
    }

    @Nonnull
    public static Next<StatisticsState, StatisticsEffect> update(StatisticsState state, StatisticsEvent event) {
        return event.map(
                tasksLoaded -> {
                    ImmutableList<Task> tasks = tasksLoaded.tasks();
                    int activeCount = 0;
                    int completedCount = 0;
                    for(Task task : tasks) {
                        if(task.details().completed()) completedCount++;
                        else activeCount++;
                    }
                    return next(loaded(activeCount, completedCount));
                }, taskLoadingFailed -> next(failed())
        );
    }
}
