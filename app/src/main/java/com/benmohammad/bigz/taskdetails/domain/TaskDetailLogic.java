package com.benmohammad.bigz.taskdetails.domain;

import androidx.annotation.NonNull;

import com.benmohammad.bigz.data.Task;
import com.spotify.mobius.Next;

import static com.benmohammad.bigz.taskdetails.domain.TaskDetailEffect.deleteTask;
import static com.benmohammad.bigz.taskdetails.domain.TaskDetailEffect.exit;
import static com.benmohammad.bigz.taskdetails.domain.TaskDetailEffect.notifyTaskMarkedActive;
import static com.benmohammad.bigz.taskdetails.domain.TaskDetailEffect.notifyTaskMarkedComplete;
import static com.benmohammad.bigz.taskdetails.domain.TaskDetailEffect.openTaskEditor;
import static com.spotify.mobius.Effects.effects;

public class TaskDetailLogic {

    @NonNull
    public static Next<Task, TaskDetailEffect> update(Task task, TaskDetailEvent event) {
        return event.map(
                deleteTaskRequested -> Next.dispatch(effects(deleteTask(task))),
                completeTaskRequested -> onCompleteTaskRequested(task),
                activateTaskRequested -> onActivateTaskRequested(task),
                editTaskRequested -> Next.dispatch(effects(openTaskEditor(task))),
                taskDeleted -> Next.dispatch(effects(exit())),
                taskCompleted -> Next.dispatch(effects(notifyTaskMarkedComplete())),
                taskActivated -> Next.dispatch(effects(notifyTaskMarkedActive())),
                taskSaveFailed ->Next.noChange(),
                taskDeletionFailed -> Next.noChange());
    }


    private static Next<Task, TaskDetailEffect> onActivateTaskRequested(Task task){
        if(!task.details().completed()) {
            return Next.noChange();
        }
        Task activateTask = task.activate();
        return Next.next(activateTask, effects(TaskDetailEffect.saveTask(activateTask)));
    }

    private static Next<Task, TaskDetailEffect> onCompleteTaskRequested(Task task) {
        if(task.details().completed()) {
            return Next.noChange();
        }
        Task completedTask = task.complete();
        return Next.next(completedTask, effects(TaskDetailEffect.saveTask(completedTask)));
    }
}
