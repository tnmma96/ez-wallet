package io.tnmma.ezwallet.data.model;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import io.tnmma.ezwallet.R;
import io.tnmma.ezwallet.data.pojo.Account;
import io.tnmma.ezwallet.util.ColorUtil;
import io.tnmma.ezwallet.util.StringUtil;

import kotlin.jvm.functions.Function1;

import java.util.UUID;

public class TransactionDetailAccount {

    private static final String TAG = TransactionDetailAccount.class.getSimpleName();

    private final MutableLiveData<Account> accountLiveData;

    private final LiveData<String> nameLiveData;

    private final LiveData<Integer> backgroundColorLiveData;

    private final LiveData<Integer> iconColorLiveData;

    private final LiveData<Integer> textColorLiveData;

    public TransactionDetailAccount(Account account) {
        accountLiveData = new MutableLiveData<>(account);
        nameLiveData = setupNameLiveData();
        backgroundColorLiveData = setupBackgroundColorLiveData();
        iconColorLiveData = setupIconColorLiveData();
        textColorLiveData = setupTextColorLiveData();
    }

    private LiveData<String> setupNameLiveData() {
        return Transformations.map(
                accountLiveData,
                new Function1<Account, String>() {
                    @Override
                    public String invoke(Account account) {
                        return account != null
                                ? account.getName()
                                : StringUtil.getStringFromResId(R.string.hint_select_account);
                    }
                });
    }

    private LiveData<Integer> setupBackgroundColorLiveData() {
        return Transformations.map(
                accountLiveData,
                new Function1<Account, Integer>() {
                    @Override
                    public Integer invoke(Account account) {
                        return account != null
                                ? account.getColor()
                                : ColorUtil.getColorFromResId(R.color.ivy_medium_white);
                    }
                });
    }

    private LiveData<Integer> setupIconColorLiveData() {
        return Transformations.map(
                accountLiveData,
                new Function1<Account, Integer>() {
                    @Override
                    public Integer invoke(Account account) {
                        return account != null
                                ? ColorUtil.getItemForegroundColor(account.getColor())
                                : ColorUtil.getColorFromResId(R.color.ivy_black);
                    }
                });
    }

    private LiveData<Integer> setupTextColorLiveData() {
        return Transformations.map(
                accountLiveData,
                new Function1<Account, Integer>() {
                    @Override
                    public Integer invoke(Account account) {
                        return account != null
                                ? ColorUtil.getItemForegroundColor(account.getColor())
                                : ColorUtil.getColorFromResId(R.color.ivy_grey);
                    }
                });
    }

    public LiveData<Account> getAccountLiveData() {
        return accountLiveData;
    }

    public LiveData<String> getNameLiveData() {
        return nameLiveData;
    }

    public LiveData<Integer> getBackgroundColorLiveData() {
        return backgroundColorLiveData;
    }

    public LiveData<Integer> getIconColorLiveData() {
        return iconColorLiveData;
    }

    public LiveData<Integer> getTextColorLiveData() {
        return textColorLiveData;
    }

    public Account getAccount() {
        return accountLiveData.getValue();
    }

    public UUID getAccountId() {
        return accountLiveData.getValue() != null ? accountLiveData.getValue().getId() : null;
    }

    public void setAccount(Account account) {
        accountLiveData.setValue(account);
    }
}
