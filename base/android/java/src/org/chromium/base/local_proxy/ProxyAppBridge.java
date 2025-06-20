package org.chromium.base.local_proxy;

import android.content.Context;
import android.os.Build;
import android.os.LocaleList;

import HttpProxy.AppBridge;

public class ProxyAppBridge implements AppBridge {

    private final Context mContext;

    public ProxyAppBridge(Context context) {
        mContext = context;
    }

    @Override
    public String getHttpProxyPort() {
        return ":9090";
    }

    @Override
    public String getPlatform() {
        return "Android";
    }

    @Override
    public String getSystemLanguage() {
        String localeString = "";
        if (mContext != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                localeString = LocaleList.getDefault().get(0).getLanguage();
            } else {
                // 這個取得語系的方法已棄用，但 Android 7 以下的裝置只能透過這個方式取得語系
                localeString = mContext.getResources().getConfiguration().locale.getLanguage();
            }
        }

        return localeString;
    }

    @Override
    public String getWritableDBPath() {
        String writableDirectory = "";
        if (mContext != null) {
            // 專屬於個別 App 的隱藏可讀寫路徑，一般來說使用者是看不到的除非使用者有 root 權限
            writableDirectory = mContext.getFilesDir().getAbsolutePath();
        }

        return writableDirectory;
    }

    @Override
    public void onTunnelStatusChanged(long l) {}
}
