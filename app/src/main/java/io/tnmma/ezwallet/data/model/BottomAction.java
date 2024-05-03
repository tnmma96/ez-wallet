package io.tnmma.ezwallet.data.model;

import io.tnmma.ezwallet.R;
import io.tnmma.ezwallet.base.constant.BottomActionType;
import io.tnmma.ezwallet.util.ColorUtil;

public class BottomAction {

    private static final String TAG = BottomAction.class.getSimpleName();

    private final BottomActionType actionType;

    public BottomAction(BottomActionType actionType) {
        this.actionType = actionType;
    }

    public int getLabelResId() {
        if (actionType == null) {
            return 0;
        }
        switch (actionType) {
            case ADD:
                return R.string.label_bottom_action_add;
            case DELETE:
                return R.string.label_bottom_action_delete;
            case EDIT:
                return R.string.label_bottom_action_edit;
            case SAVE:
                return R.string.label_bottom_action_save;
            case SELECT:
                return R.string.label_bottom_action_select;
            case SET:
                return R.string.label_bottom_action_set;
        }
        return 0;
    }

    public int getIconResId() {
        if (actionType == null) {
            return 0;
        }
        switch (actionType) {
            case ADD:
                return R.drawable.icon_add;
            case DELETE:
                return R.drawable.icon_delete;
            case EDIT:
                return R.drawable.icon_edit;
            case SAVE:
                return R.drawable.icon_save;
            case SELECT:
            case SET:
                return R.drawable.icon_check;
        }
        return 0;
    }

    public int getButtonConfirmBackgroundColor() {
        int colorResId = actionType == BottomActionType.DELETE ? R.color.ivy_red : R.color.ivy_blue;
        return ColorUtil.getColorFromResId(colorResId);
    }
}
