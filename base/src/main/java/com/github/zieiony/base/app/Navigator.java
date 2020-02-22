package com.github.zieiony.base.app;

import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import java.io.Serializable;
import java.util.HashMap;

public interface Navigator {
    default Navigator getParentNavigator() {
        return null;
    }

    default void navigateTo(@NonNull Class<? extends Fragment> fragment, HashMap<String, Serializable> arguments) {
        if (!onNavigateTo(fragment, arguments))
            getParentNavigator().navigateTo(fragment, arguments);
    }

    default void navigateTo(@NonNull Intent intent) {
        if (!onNavigateTo(intent))
            getParentNavigator().navigateTo(intent);
    }

    default boolean onNavigateTo(@NonNull Class<? extends Fragment> fragment, HashMap<String, Serializable> arguments) {
        return false;
    }

    default boolean onNavigateTo(@NonNull Intent intent) {
        return false;
    }

    default void navigateBack() {
        if (!onNavigateBack())
            getParentNavigator().navigateBack();
    }

    default boolean onNavigateBack() {
        return false;
    }

    default <T extends Serializable> T getResult() {
        return getParentNavigator().getResult();
    }

    default <T extends Serializable> void setResult(T result) {
        if (result == null || !onResult(result))
            getParentNavigator().setResult(result);
    }

    default boolean onResult(Serializable result) {
        return false;
    }
}
