package com.benmohammad.bigz.stats.domain;

import com.spotify.dataenum.DataEnum;
import com.spotify.dataenum.dataenum_case;

@DataEnum
public interface StatisticsState_dataenum {

    dataenum_case Loading();
    dataenum_case Loaded(int activeCount, int completedCount);
    dataenum_case Failed();
}
