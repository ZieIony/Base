package com.github.zieiony.base.app;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.io.Serializable;
import java.util.HashMap;

public interface Navigator {
    default Navigator getParentNavigator() {
        return null;
    }

    default void navigateTo(@NonNull Class<?> target, @Nullable HashMap<String, Serializable> arguments) {
        if (!onNavigateTo(target, arguments))
            getParentNavigator().navigateTo(target, arguments);
    }

    default boolean onNavigateTo(@NonNull Class<?> target, @Nullable HashMap<String, Serializable> arguments) {
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
