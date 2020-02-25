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
