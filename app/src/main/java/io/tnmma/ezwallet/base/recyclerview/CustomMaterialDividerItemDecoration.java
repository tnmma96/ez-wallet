package io.tnmma.ezwallet.base.recyclerview;

import android.content.Context;

import androidx.annotation.ColorRes;
import androidx.annotation.DimenRes;
import androidx.annotation.NonNull;

import com.google.android.material.divider.MaterialDividerItemDecoration;

/** Custom MaterialDividerItemDecoration with lastItemDecorated default to false */
public class CustomMaterialDividerItemDecoration extends MaterialDividerItemDecoration {

    private static final String TAG = CustomMaterialDividerItemDecoration.class.getSimpleName();

    public CustomMaterialDividerItemDecoration(@NonNull Context context, int orientation) {
        super(context, orientation);
        setLastItemDecorated(false);
    }

    public CustomMaterialDividerItemDecoration withThicknessResource(
            Context context, @DimenRes int thicknessId) {
        setDividerThicknessResource(context, thicknessId);
        return this;
    }

    public CustomMaterialDividerItemDecoration withColorResource(
            Context context, @ColorRes int colorId) {
        setDividerColorResource(context, colorId);
        return this;
    }

    public CustomMaterialDividerItemDecoration withLastItemDecorated(boolean lastItemDecorated) {
        setLastItemDecorated(lastItemDecorated);
        return this;
    }
}
