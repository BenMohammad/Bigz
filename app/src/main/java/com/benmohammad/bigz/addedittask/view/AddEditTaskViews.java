package com.benmohammad.bigz.addedittask.view;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.benmohammad.bigz.R;
import com.benmohammad.bigz.addedittask.domain.AddEditTaskEvent;
import com.benmohammad.bigz.addedittask.domain.AddEditTaskModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.spotify.mobius.Connectable;
import com.spotify.mobius.Connection;
import com.spotify.mobius.ConnectionLimitExceededException;
import com.spotify.mobius.functions.Consumer;

import javax.annotation.Nonnull;

public class AddEditTaskViews implements Connectable<AddEditTaskModel, AddEditTaskEvent> {

    private final View mRoot;
    private final FloatingActionButton mFab;
    private final TextView mTitle;
    private final TextView mDescription;

    public AddEditTaskViews(LayoutInflater inflater, ViewGroup parent, FloatingActionButton fab) {
        mRoot = inflater.inflate(R.layout.addtask_frag, parent, false);
        mTitle = mRoot.findViewById(R.id.add_task_title);
        mDescription = mRoot.findViewById(R.id.add_task_description);
        fab.setImageResource(R.drawable.ic_done);
        mFab = fab;
    }

    public View getRootView() {
        return mRoot;
    }

    public void showEmptyTaskError() {
        Snackbar.make(mTitle, R.string.empty_task_message, Snackbar.LENGTH_SHORT).show();
    }

    public void setTitle(String title) {
        mTitle.setText(title);
    }

    public void setDescription(String description) {
        mDescription.setText(description);
    }



    @Nonnull
    @Override
    public Connection<AddEditTaskModel> connect(Consumer<AddEditTaskEvent> output) throws ConnectionLimitExceededException {
        mFab.setOnClickListener(
                __ -> output.accept(AddEditTaskEvent.taskDefinitionCompleted(mTitle.getText().toString(), mDescription.getText().toString())));

        return new Connection<AddEditTaskModel>() {
            @Override
            public void accept(AddEditTaskModel value) {
                setTitle(value.details().title());
                setDescription(value.details().description());
            }

            @Override
            public void dispose() {
                mFab.setOnClickListener(null);
            }
        };
    }
}
