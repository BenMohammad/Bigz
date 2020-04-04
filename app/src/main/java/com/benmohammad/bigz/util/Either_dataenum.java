package com.benmohammad.bigz.util;

import com.spotify.dataenum.DataEnum;
import com.spotify.dataenum.dataenum_case;

@DataEnum
public interface Either_dataenum<A, B> {

    dataenum_case Left(A value);
    dataenum_case Right(B Value);
}
