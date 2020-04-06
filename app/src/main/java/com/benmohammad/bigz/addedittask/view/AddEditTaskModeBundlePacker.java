package com.benmohammad.bigz.addedittask.view;

import android.os.Bundle;

import com.benmohammad.bigz.addedittask.domain.AddEditTaskMode;
import com.benmohammad.bigz.addedittask.domain.AddEditTaskModel;
import com.benmohammad.bigz.data.TaskBundlePacker;
import com.google.common.base.Optional;

import static com.benmohammad.bigz.data.TaskBundlePacker.taskDetailsFromBundle;
import static com.google.common.base.Preconditions.checkNotNull;

public class AddEditTaskModeBundlePacker {

        public static Bundle addEditTaskModelToBundle(AddEditTaskModel model) {
            Bundle b = new Bundle();
            b.putBundle("task_details", TaskBundlePacker.taskDetailsToBundle(model.details()));
            Optional<Bundle> modeBundle = addEditTaskModeToBundle(model.mode());
            if(modeBundle.isPresent()) b.putBundle("add_edit_mode", modeBundle.get());
            return b;
        }

        public static AddEditTaskModel addEditTaskModelFromBundle(Bundle bundle) {
            return AddEditTaskModel.builder()
                    .details(taskDetailsFromBundle(checkNotNull(bundle.getBundle("task_details"))))
                    .mode(addEditTaskModeFromBundle(bundle.getBundle("add_edit_Mode")))
                    .build();

        }

        private static Optional<Bundle> addEditTaskModeToBundle(AddEditTaskMode mode) {
            return mode.map(create -> Optional.absent(),
                    update -> {
                        Bundle b = new Bundle();
                        b.putString("task_id", update.id());
                        return Optional.of(b);

                    });

        }

        private static AddEditTaskMode addEditTaskModeFromBundle(Bundle bundle) {
            if(bundle == null) return AddEditTaskMode.create();
            return AddEditTaskMode.update(checkNotNull(bundle.getString("task_id")));
        }
}
