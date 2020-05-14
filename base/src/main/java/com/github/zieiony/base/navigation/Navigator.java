package com.github.zieiony.base.navigation;

import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

public interface Navigator {
    default Navigator getParentNavigator() {
        return null;
    }

    int getNavigatorId();

    void navigateTo(@NonNull Class<? extends Fragment> fragmentClass, HashMap<String, Serializable> arguments);

    void navigateTo(@NonNull Fragment fragment);

    void navigateTo(@NonNull Navigator originalNavigator, @NonNull Fragment fragment);

    void navigateTo(@NonNull Intent intent);

    void navigateBack();

    @NonNull
    default List<Result> getResults() {
        return getParentNavigator().getResults();
    }

    default Result getResult(int navigatorId, @NonNull String key) {
        return getParentNavigator().getResult(navigatorId, key);
    }

    default void setResult(@NonNull String key, Serializable result) {
        getParentNavigator().setResult(new Result(getResultTarget(), key, result));
    }

    default void setResult(@NonNull Result result) {
        if (result.getTarget() != getNavigatorId() || !onResult(result.getKey(), result.getValue()))
            getParentNavigator().setResult(result);
    }

    default void clearResult(@NonNull Result result) {
        getParentNavigator().clearResult(result);
    }

    default boolean onResult(@NonNull String key, Serializable result) {
        return false;
    }

    void setResultTarget(int resultTarget);

    int getResultTarget();
}
