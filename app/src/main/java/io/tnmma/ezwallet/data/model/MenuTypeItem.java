package io.tnmma.ezwallet.data.model;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import io.tnmma.ezwallet.R;
import io.tnmma.ezwallet.base.constant.MenuType;

public class MenuTypeItem {

    private final MenuType menuType;

    private final MutableLiveData<Boolean> selectedLiveData;

    public MenuTypeItem(MenuType menuType) {
        this.menuType = menuType;
        selectedLiveData = new MutableLiveData<>(false);
    }

    public LiveData<Boolean> getSelectedLiveData() {
        return selectedLiveData;
    }

    public boolean isSelected() {
        return Boolean.TRUE.equals(selectedLiveData.getValue());
    }

    public void setSelected(boolean selected) {
        selectedLiveData.setValue(selected);
    }

    public int getLabelResId() {
        if (menuType == null) {
            return 0;
        }
        switch (menuType) {
            case HOME:
                return R.string.label_menu_home;
            case ACCOUNT:
                return R.string.label_menu_account;
            case CATEGORY:
                return R.string.label_menu_category;
            case USER:
                return R.string.label_menu_user;
        }
        return 0;
    }

    public int getIconResId() {
        if (menuType == null) {
            return 0;
        }
        switch (menuType) {
            case HOME:
                return R.drawable.icon_home;
            case ACCOUNT:
                return R.drawable.icon_account;
            case CATEGORY:
                return R.drawable.icon_category;
            case USER:
                return R.drawable.icon_user;
        }
        return 0;
    }
}
