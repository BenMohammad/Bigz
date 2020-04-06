package com.benmohammad.bigz.tasks;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.benmohammad.bigz.R;
import com.benmohammad.bigz.data.Task;
import com.benmohammad.bigz.tasks.domain.TasksFilterType;
import com.benmohammad.bigz.tasks.domain.TasksListEvent;
import com.benmohammad.bigz.tasks.domain.TasksListModel;
import com.benmohammad.bigz.tasks.view.DeferredEventSource;
import com.benmohammad.bigz.tasks.view.TasksAdapter;
import com.benmohammad.bigz.tasks.view.TasksListViewDataMapper;
import com.benmohammad.bigz.tasks.view.TasksViews;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.spotify.mobius.MobiusLoop;

import io.reactivex.subjects.PublishSubject;

import static com.benmohammad.bigz.tasks.domain.TasksListEvent.clearCompletedTasksRequested;
import static com.benmohammad.bigz.tasks.domain.TasksListEvent.filterSelected;
import static com.benmohammad.bigz.tasks.domain.TasksListEvent.refreshRequested;
import static com.benmohammad.bigz.tasks.domain.TasksListModelBundlePacker.taskListModelToBundle;
import static com.benmohammad.bigz.tasks.domain.TasksListModelBundlePacker.tasksListModelFromBundle;
import static com.benmohammad.bigz.tasks.effecthandlers.TasksListEffectHandlers.createEffectHandler;
import static com.spotify.mobius.extras.Connectables.contramap;

public class TasksFragment extends Fragment {

    private MobiusLoop.Controller<TasksListModel, TasksListEvent> mController;
    private PublishSubject<TasksListEvent> mMenuEvents = PublishSubject.create();
    private TasksViews mViews;
    private DeferredEventSource<TasksListEvent> mEventSource = new DeferredEventSource<>();

    public static TasksFragment newInstance() {
        return new TasksFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        FloatingActionButton fab = getActivity().findViewById(R.id.fab_add_task);
        mViews = new TasksViews(inflater, container, fab, mMenuEvents);

        mController =
                TasksInjector.createController(
                        createEffectHandler(
                                getContext(), mViews, this::showAddTask, this::showTaskDetailsUI),
                        mEventSource,
                        resolveDefaultModel(savedInstanceState));

        mController.connect(contramap(TasksListViewDataMapper::tasksListModelYoViewData, mViews));
        setHasOptionsMenu(true);
        return mViews.getRootView();
    }

    private TasksListModel resolveDefaultModel(@Nullable Bundle savedInstanceState) {
        return savedInstanceState != null ? tasksListModelFromBundle(savedInstanceState.getBundle("model")) : TasksListModel.DEFAULT;
    }

    @Override
    public void onResume() {
        super.onResume();
        mController.start();
    }

    @Override
    public void onPause() {
        mController.stop();
        super.onPause();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBundle("model", taskListModelToBundle(mController.getModel()));
    }

    @Override
    public void onDestroyView() {
        mController.disconnect();
        super.onDestroyView();
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.tasks_fragment_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()) {
            case R.id.menu_clear:
                mMenuEvents.onNext(clearCompletedTasksRequested());
                break;
            case R.id.menu_filter:
                showFilteringPopupMenu();
                break;
            case R.id.menu_refresh:
                mMenuEvents.onNext(refreshRequested());
        }

        return true;
    }

    private void onFilterSelected(TasksFilterType filter) {
        mMenuEvents.onNext(filterSelected(filter));
    }

    private void showFilteringPopupMenu() {
        PopupMenu popup = new PopupMenu(getContext(), getActivity().findViewById(R.id.menu_filter));
        popup.getMenuInflater().inflate(R.menu.filter_tasks, popup.getMenu());

        popup.setOnMenuItemClickListener(
                item -> {
                    switch(item.getItemId()) {
                        case R.id.active:
                            onFilterSelected(TasksFilterType.ACTIVE_TASKS);
                            break;
                        case R.id.completed:
                            onFilterSelected(TasksFilterType.COMPLETED_TASKS);
                            break;
                        default:
                            onFilterSelected(TasksFilterType.ALL_TASKS);
                    }
                    return true;
                }
        );
        popup.show();
    }

    public void showAddTask() {
//        startActivityForResult(AddEditTaskActivity.addTask(getContext()), AddEditTaskActivity.REQUEST_ADD_TASK);
    }

    public void showTaskDetailsUI(Task task) {
//        startActivity(TaskDetailActivity.showTask(getContext(), task));
    }
}
