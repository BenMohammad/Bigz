package com.benmohammad.bigz.taskdetails.domain;

import com.spotify.dataenum.DataEnum;
import com.spotify.dataenum.dataenum_case;

@DataEnum
public interface TaskDetailEvent_dataenum {

    dataenum_case DeleteTaskRequested();
    dataenum_case CompleteTaskRequested();
    dataenum_case ActivateTaskRequested();
    dataenum_case EditTaskRequested();
    dataenum_case TaskDeleted();
    dataenum_case TaskMarkedComplete();
    dataenum_case TaskMarkedActive();
    dataenum_case TaskSaveFailed();
    dataenum_case TaskDeletionFailed();

}
