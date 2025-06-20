package org.chromium.base.local_proxy;

import android.content.Context;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import HttpProxy.HttpProxy;

public class NetworkingProtectorManager {

    private static volatile NetworkingProtectorManager instance;

    private final ProxyAppBridge mAppBridge;
    private final static String INDEX_DB_NAME = "index.db";

    private NetworkingProtectorManager(Context context) {
        mAppBridge = new ProxyAppBridge(context);
        copyDatabaseIfNonexistent(context);

        Thread HttpProxyServeThread = new Thread(() -> {
            HttpProxy.serve(mAppBridge);
        });
        HttpProxyServeThread.start();
    }

    public static NetworkingProtectorManager getInstance(Context context) {
        if (instance == null) {
            synchronized (NetworkingProtectorManager.class) {
                if (instance == null) {
                    instance = new NetworkingProtectorManager(context);
                }
            }
        }
        return instance;
    }

    public String getClientIP() {
        String bbForwarded = getBbForwarded();
        if (bbForwarded.isEmpty()) {
            return "";
        }

        String[] components = bbForwarded.split(",");
        return components[0].trim();
    }

    public String getBbForwarded() {
        return HttpProxy.getBBforwarded();
    }

    public float getTunnelAliveSeconds() {
        return (float) HttpProxy.getTunnelAliveSeconds();
    }

    public String lookupRoute(String domain) {
        return HttpProxy.lookupRoute(domain);
    }

    public int getRouteCacheCount(String domain) {
        return (int) HttpProxy.getRouteCacheCount(domain);
    }

    public int getIndexVersion() {
        return (int) HttpProxy.getIndexVersion();
    }

    private void copyDatabaseIfNonexistent(Context context) {
        File dbFile = new File(context.getFilesDir(), INDEX_DB_NAME);

        if (!dbFile.exists()) {
            try {
                InputStream inputStream = context.getResources().openRawResource(org.chromium.base.R.raw.index);
                OutputStream outputStream = new FileOutputStream(dbFile);

                copy(inputStream, outputStream);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static void copy(InputStream inputStream, OutputStream outputStream) throws IOException {
        if (inputStream == null || outputStream == null) {
            return;
        }

        byte[] buf = new byte[1024];
        int len;
        try {
            while ((len = inputStream.read(buf)) > 0) {
                outputStream.write(buf, 0, len);
            }
            outputStream.flush();
        } finally {
            outputStream.close();
            inputStream.close();
        }
    }
}
