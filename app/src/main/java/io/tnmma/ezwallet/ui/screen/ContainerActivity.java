package io.tnmma.ezwallet.ui.screen;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.TypedValue;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import io.tnmma.ezwallet.R;
import io.tnmma.ezwallet.base.constant.ConstantKeys;
import io.tnmma.ezwallet.base.constant.DetailScreenType;
import io.tnmma.ezwallet.databinding.ActivityContainerBinding;
import io.tnmma.ezwallet.ui.screen.detail.account.AccountDetailFragment;
import io.tnmma.ezwallet.ui.screen.detail.category.CategoryDetailFragment;
import io.tnmma.ezwallet.ui.screen.detail.transaction.TransactionDetailFragment;
import io.tnmma.ezwallet.ui.screen.main.MainFragment;
import io.tnmma.ezwallet.util.ColorUtil;

public class ContainerActivity extends AppCompatActivity {

    private static final String TAG = ContainerActivity.class.getSimpleName();

    private ActivityContainerBinding binding;

    private MainFragment mainFragment;

    private BroadcastReceiver openDetailScreenReceiver;

    private BroadcastReceiver closeDetailScreenReceiver;

    private BroadcastReceiver closeAppReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO: Save and restore states when this activity is recreated
        //      (after configuration change, process death, ...)
        // Currently savedInstanceState is ignored every time this activity is recreated,
        //      therefore states of child fragments are not restored
        super.onCreate(null);

        binding = ActivityContainerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setupIvyColor();
        setupMainFragment();
        setupOnBackPressedCallback();
        listenForOpenDetailScreenRequest();
        listenForCloseDetailScreenRequest();
        listenForCloseAppRequest();
    }

    private void setupIvyColor() {
        TypedValue typedValue = new TypedValue();
        getTheme().resolveAttribute(R.attr.ivyItemBackgroundColor, typedValue, true);
        ColorUtil.setIvyItemBackgroundColor(typedValue.data);
        getTheme().resolveAttribute(android.R.attr.textColor, typedValue, true);
        ColorUtil.setIvyItemForegroundColor(typedValue.data);
    }

    private void setupMainFragment() {
        mainFragment = new MainFragment();
        getSupportFragmentManager()
                .beginTransaction()
                .add(binding.activityMainContainer.getId(), mainFragment)
                .commit();
    }

    private void setupOnBackPressedCallback() {
        getOnBackPressedDispatcher()
                .addCallback(
                        this,
                        new OnBackPressedCallback(true) {
                            @Override
                            public void handleOnBackPressed() {
                                if (mainFragment == null) {
                                    return;
                                }

                                if (mainFragment.isHidden()) {
                                    getSupportFragmentManager().popBackStack();
                                } else {
                                    mainFragment.handleBackPressEvent();
                                }
                            }
                        });
    }

    private void listenForOpenDetailScreenRequest() {
        openDetailScreenReceiver =
                new BroadcastReceiver() {
                    @Override
                    public void onReceive(Context context, Intent intent) {
                        openDetailScreen(intent);
                    }
                };
        LocalBroadcastManager.getInstance(this)
                .registerReceiver(
                        openDetailScreenReceiver,
                        new IntentFilter(ConstantKeys.IntentAction.OPEN_DETAIL_SCREEN));
    }

    private void openDetailScreen(Intent intent) {
        DetailScreenType screenType =
                (DetailScreenType) intent.getSerializableExtra(ConstantKeys.DetailScreen.TYPE);
        if (screenType == null) {
            return;
        }

        Fragment fragment = getDetailScreenByType(screenType);
        if (fragment == null) {
            return;
        }

        fragment.setArguments(intent.getBundleExtra(ConstantKeys.DetailScreen.DATA));
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        Fragment currentFragment =
                getSupportFragmentManager().findFragmentById(binding.activityMainContainer.getId());
        if (currentFragment != null && currentFragment.isVisible()) {
            transaction.hide(currentFragment);
        }
        transaction
                .add(binding.activityMainContainer.getId(), fragment)
                .addToBackStack(null)
                .commit();
    }

    private Fragment getDetailScreenByType(DetailScreenType screenType) {
        switch (screenType) {
            case ACCOUNT:
                return new AccountDetailFragment();
            case CATEGORY:
                return new CategoryDetailFragment();
            case TRANSACTION:
                return new TransactionDetailFragment();
        }
        return null;
    }

    private void listenForCloseDetailScreenRequest() {
        closeDetailScreenReceiver =
                new BroadcastReceiver() {
                    @Override
                    public void onReceive(Context context, Intent intent) {
                        getSupportFragmentManager().popBackStack();
                    }
                };
        LocalBroadcastManager.getInstance(this)
                .registerReceiver(
                        closeDetailScreenReceiver,
                        new IntentFilter(ConstantKeys.IntentAction.CLOSE_DETAIL_SCREEN));
    }

    private void listenForCloseAppRequest() {
        closeAppReceiver =
                new BroadcastReceiver() {
                    @Override
                    public void onReceive(Context context, Intent intent) {
                        finish();
                    }
                };
        LocalBroadcastManager.getInstance(this)
                .registerReceiver(
                        closeAppReceiver, new IntentFilter(ConstantKeys.IntentAction.CLOSE_APP));
    }

    @Override
    protected void onDestroy() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(openDetailScreenReceiver);
        LocalBroadcastManager.getInstance(this).unregisterReceiver(closeDetailScreenReceiver);
        LocalBroadcastManager.getInstance(this).unregisterReceiver(closeAppReceiver);
        binding = null;
        super.onDestroy();
    }
}
