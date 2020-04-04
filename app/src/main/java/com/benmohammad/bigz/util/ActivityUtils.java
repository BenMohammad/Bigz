package com.benmohammad.bigz.util;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import javax.annotation.Nonnull;

import static com.google.common.base.Preconditions.checkNotNull;

public class ActivityUtils {

    public static void addFragmentToActivity(@Nonnull FragmentManager manager,
                                             @Nonnull Fragment fragment,
                                             int frameId) {
        checkNotNull(manager);
        checkNotNull(fragment);
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.add(frameId, fragment);
        transaction.commit();
    }
}
