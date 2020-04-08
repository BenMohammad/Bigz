package com.benmohammad.bigz.stats;

import android.os.Bundle;

import com.benmohammad.bigz.stats.domain.StatisticsState;
import com.benmohammad.bigz.tasks.domain.TasksListEffect;
import com.google.common.base.Optional;

import java.security.cert.PKIXRevocationChecker;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import static com.google.common.base.Preconditions.checkNotNull;

class StatisticsStateBundler {

    static Optional<Bundle> statisticsStateToBundle(StatisticsState state) {
        return state.map(
                loading -> Optional.absent(),
                loaded -> {
                    Bundle bundle = new Bundle();
                    bundle.putInt("active_count", loaded.activeCount());
                    bundle.putInt("completed_count", loaded.completedCount());
                    return Optional.of(bundle);
                }, failed -> Optional.absent());
    }

    static StatisticsState bundleToStatisticsState(@Nullable Bundle bundle) {
        if(bundle == null) return StatisticsState.loading();
        if(!bundle.containsKey("statistics")) return StatisticsState.loading();
        bundle = checkNotNull(bundle.getBundle("statistics"));

        if(bundle.containsKey("active_count") && bundle.containsKey("completed_count")) {
            return StatisticsState.loaded(
                    bundle.getInt("active_count"), bundle.getInt("completed_count"));
        }

        return StatisticsState.loading();
    }
}
