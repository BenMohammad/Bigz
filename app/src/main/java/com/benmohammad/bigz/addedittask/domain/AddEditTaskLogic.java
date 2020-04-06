package com.benmohammad.bigz.addedittask.domain;

import androidx.annotation.NonNull;

import com.benmohammad.bigz.addedittask.domain.AddEditTaskEvent.TaskDefinitionCompleted;
import com.benmohammad.bigz.data.Task;
import com.benmohammad.bigz.data.TaskDetails;
import com.spotify.mobius.Next;

import static com.benmohammad.bigz.addedittask.domain.AddEditTaskEffect.createTask;
import static com.benmohammad.bigz.addedittask.domain.AddEditTaskEffect.exit;
import static com.benmohammad.bigz.addedittask.domain.AddEditTaskEffect.notifyEmptyTaskNotAllowed;
import static com.benmohammad.bigz.addedittask.domain.AddEditTaskEffect.saveTask;
import static com.google.common.base.Strings.isNullOrEmpty;
import static com.spotify.mobius.Effects.effects;

public class AddEditTaskLogic {

    @NonNull
    public static Next<AddEditTaskModel, AddEditTaskEffect> update(AddEditTaskModel model, AddEditTaskEvent event) {
        return event.map(
                taskDefinitionCompleted -> onTaskDefinitionCompleted(model, taskDefinitionCompleted),
                taskCreatedSuccessfully -> exitWithSuccess(),
                taskCreatedFailed -> exitWithFailure(),
                taskUpdatedSuccessfully ->exitWithSuccess(),
                taskUpdatedFailed -> exitWithFailure()
        );
    }

    private static Next<AddEditTaskModel, AddEditTaskEffect> onTaskDefinitionCompleted(AddEditTaskModel model, TaskDefinitionCompleted definitionCompleted) {
        String title = definitionCompleted.title().trim();
        String description = definitionCompleted.description().trim();

        if(isNullOrEmpty(title) && isNullOrEmpty(description)) {
            return Next.dispatch(effects(notifyEmptyTaskNotAllowed()));

        }
        TaskDetails details = model.details().toBuilder().title(title).description(description).build();
        AddEditTaskModel newModel = model.withDetails(details);
        return newModel
                .mode()
                .map(
                        create -> Next.next(newModel, effects(createTask(newModel.details()))),
                        update -> Next.next(newModel, (effects(saveTask(Task.create(update.id(), newModel.details()))))));
    }

    private static Next<AddEditTaskModel, AddEditTaskEffect> exitWithSuccess() {
        return Next.dispatch(effects(exit(true)));

    }

    private static Next<AddEditTaskModel, AddEditTaskEffect> exitWithFailure() {
        return Next.dispatch(effects(exit(false)));
    }
}
