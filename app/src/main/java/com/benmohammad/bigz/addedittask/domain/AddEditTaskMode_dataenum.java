package com.benmohammad.bigz.addedittask.domain;

import com.spotify.dataenum.DataEnum;
import com.spotify.dataenum.dataenum_case;

@DataEnum
public interface AddEditTaskMode_dataenum {

    dataenum_case Create();
    dataenum_case Update(String id);
}
