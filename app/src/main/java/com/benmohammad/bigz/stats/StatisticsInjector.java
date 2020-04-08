package com.benmohammad.bigz.stats;

import com.benmohammad.bigz.stats.domain.StatisticsEffect;
import com.benmohammad.bigz.stats.domain.StatisticsEvent;
import com.benmohammad.bigz.stats.domain.StatisticsLogic;
import com.benmohammad.bigz.stats.domain.StatisticsState;
import com.spotify.mobius.MobiusLoop;
import com.spotify.mobius.android.MobiusAndroid;
import com.spotify.mobius.rx2.RxMobius;

import io.reactivex.ObservableTransformer;

public class StatisticsInjector {

    public static MobiusLoop.Controller<StatisticsState, StatisticsEvent> createController(
            ObservableTransformer<StatisticsEffect, StatisticsEvent> effectHandler,
            StatisticsState state) {
        return MobiusAndroid.controller(createLoop(effectHandler), state);
    }

    private static MobiusLoop.Factory<StatisticsState, StatisticsEvent, StatisticsEffect> createLoop(
            ObservableTransformer<StatisticsEffect, StatisticsEvent> effectHandler) {
        return RxMobius.loop(StatisticsLogic::update, effectHandler).init(StatisticsLogic::init);
    }
}
