package com.github.zieiony.base.app;

import android.content.Intent;

import androidx.fragment.app.Fragment;

import java.io.Serializable;

public interface Navigator {
    default Navigator getParentNavigator() {
        return null;
    }

    default void navigateTo(Fragment fragment) {
        if (!onNavigateTo(fragment))
            getParentNavigator().navigateTo(fragment);
    }

    default boolean onNavigateTo(Fragment fragment) {
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
