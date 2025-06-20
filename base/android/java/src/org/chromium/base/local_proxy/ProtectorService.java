package org.chromium.base.local_proxy;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

public class ProtectorService extends Service {
    private NetworkingProtectorManager mProtectorManager;

    public class LocalBinder extends Binder {
        public ProtectorService getService() {
            return ProtectorService.this;
        }

        public NetworkingProtectorManager getProtectorManager() {
            return mProtectorManager;
        }
    }
    private final LocalBinder mLocalBinder = new LocalBinder();

    @Override
    public void onCreate() {
        mProtectorManager = NetworkingProtectorManager.getInstance(getApplicationContext());
        super.onCreate();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mLocalBinder;
    }
}