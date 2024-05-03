package io.tnmma.ezwallet.ui.screen.main;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import io.tnmma.ezwallet.base.constant.ConstantArrays;
import io.tnmma.ezwallet.base.constant.MenuType;
import io.tnmma.ezwallet.data.model.MenuTypeItem;

import java.util.Map;

public class MainViewModel extends ViewModel {

    private static final String TAG = MainViewModel.class.getSimpleName();

    private final Map<MenuType, MenuTypeItem> menuMap;

    private final MutableLiveData<MenuType> menuTypeLiveData;

    public MainViewModel() {
        menuMap = ConstantArrays.getMainMenuMap();
        menuTypeLiveData = new MutableLiveData<>();
    }

    public MenuTypeItem getMenu(MenuType menuType) {
        if (menuType == null) {
            return null;
        }
        return menuMap.get(menuType);
    }

    public LiveData<MenuType> getMenuTypeLiveData() {
        return menuTypeLiveData;
    }

    public MenuType getCurrentMenuType() {
        return menuTypeLiveData.getValue();
    }

    public void onSelectMenu(MenuType menuType) {
        if (menuType == null || menuTypeLiveData.getValue() == menuType) {
            return;
        }

        if (menuTypeLiveData.getValue() != null) {
            menuMap.get(menuTypeLiveData.getValue()).setSelected(false);
        }
        menuMap.get(menuType).setSelected(true);
        menuTypeLiveData.setValue(menuType);
    }
}
