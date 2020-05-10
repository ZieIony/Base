package com.github.zieiony.base.navigation;

import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import java.io.Serializable;
import java.util.HashMap;

public interface Navigator {
    default Navigator getParentNavigator() {
        return null;
    }

    void navigateTo(@NonNull Class<? extends Fragment> fragmentClass, HashMap<String, Serializable> arguments);

    void navigateTo(@NonNull Fragment fragment);

    void navigateTo(@NonNull Intent intent);

    void navigateBack();

    @NonNull
    default HashMap<String, Serializable> getResults() {
        return getParentNavigator().getResults();
    }

    default <T extends Serializable> T getResult(@NonNull String key) {
        return getParentNavigator().getResult(key);
    }

    default <T extends Serializable> void setResult(@NonNull String key, T result) {
        if (result == null || !onResult(key, result))
            getParentNavigator().setResult(key, result);
    }

    default void clearResult(@NonNull String key) {
        getParentNavigator().clearResult(key);
    }

    default boolean onResult(@NonNull String key, Serializable result) {
        return false;
    }
}
