package com.github.zieiony.base.app;

import android.content.Intent;

import java.io.Serializable;

public interface Navigator {
    default Navigator getParentNavigator() {
        return null;
    }

    default void navigateTo(BaseFragment fragment) {
        if (!onNavigateTo(fragment))
            getParentNavigator().navigateTo(fragment);
    }

    default boolean onNavigateTo(BaseFragment fragment) {
        return false;
    }

    default void navigateTo(Intent intent) {
        if (!onNavigateTo(intent))
            getParentNavigator().navigateTo(intent);
    }

    default boolean onNavigateTo(Intent intent) {
        return false;
    }

    default void navigateBack() {
        if (!onNavigateBack())
            getParentNavigator().navigateBack();
    }

    default <T extends Serializable> void navigateBack(T result) {
        if (!onNavigateBack(result))
            getParentNavigator().navigateBack(result);
    }

    default boolean onNavigateBack() {
        return false;
    }

    default <T extends Serializable> boolean onNavigateBack(T result) {
        return false;
    }

    default <T extends Serializable> T getResult() {
        return getParentNavigator().getResult();
    }
}
