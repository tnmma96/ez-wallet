package io.tnmma.ezwallet.base;

import android.app.Application;
import android.content.Context;

import io.tnmma.ezwallet.data.db.IvyDatabase;

public class IvyApplication extends Application {

    private static final String TAG = IvyApplication.class.getSimpleName();

    private static IvyApplication instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        IvyDatabase.getInstance();
    }

    public static IvyApplication getInstance() {
        return instance;
    }

    public Context getAppContext() {
        return getApplicationContext();
    }
}
