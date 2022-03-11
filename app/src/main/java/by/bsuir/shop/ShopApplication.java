package by.bsuir.shop;

import android.app.Application;
import android.content.Context;

public class ShopApplication extends Application {
    private static Context context;

    public void onCreate() {
        super.onCreate();
        ShopApplication.context = getApplicationContext();
    }

    public static Context getAppContext() {
        return ShopApplication.context;
    }
}
