// Copyright 2016 The Chromium Authors
// Use of this source code is governed by a BSD-style license that can be
// found in the LICENSE file.

package org.chromium.chrome.browser;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ShortcutInfo;
import android.content.pm.ShortcutManager;
import android.graphics.drawable.Icon;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.annotation.VisibleForTesting;

import org.chromium.base.ResettersForTesting;
import org.chromium.base.shared_preferences.SharedPreferencesManager;
import org.chromium.chrome.R;
import org.chromium.chrome.browser.incognito.IncognitoUtils;
import org.chromium.chrome.browser.preferences.ChromePreferenceKeys;
import org.chromium.chrome.browser.preferences.ChromeSharedPreferences;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A helper activity for routing launcher shortcut intents.
 */
public class LauncherShortcutActivity extends Activity {
    public static final String ACTION_OPEN_NEW_TAB = "chromium.shortcut.action.OPEN_NEW_TAB";
    public static final String ACTION_OPEN_NEW_INCOGNITO_TAB =
            "chromium.shortcut.action.OPEN_NEW_INCOGNITO_TAB";

    private static String sLabelForTesting;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        String intentAction = intent.getAction();

        // Exit early if the original intent action isn't for opening a new tab.
        if (!intentAction.equals(ACTION_OPEN_NEW_TAB)
                && !intentAction.equals(ACTION_OPEN_NEW_INCOGNITO_TAB)) {
            finish();
            return;
        }

        Intent newIntent = getChromeLauncherActivityIntent(this, intentAction);
        // Retain FLAG_ACTIVITY_MULTIPLE_TASK in the intent if present, to support multi-instance
        // launch behavior.
        if ((intent.getFlags() & Intent.FLAG_ACTIVITY_MULTIPLE_TASK) != 0) {
            newIntent.setFlags(newIntent.getFlags() | Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
        }

        startActivity(newIntent);
        finish();
    }

    /**
     * @param context The context used to get the package and set the intent class.
     * @param launcherShortcutIntentAction The intent action that launched the
     *                                     LauncherShortcutActivity.
     * @return An intent for ChromeLauncherActivity that will open a new regular or incognito tab.
     */
    private static Intent getChromeLauncherActivityIntent(
            Context context, String launcherShortcutIntentAction) {
        Intent newIntent = IntentHandler.createTrustedOpenNewTabIntent(context,
                launcherShortcutIntentAction.equals(ACTION_OPEN_NEW_INCOGNITO_TAB));
        newIntent.putExtra(IntentHandler.EXTRA_INVOKED_FROM_SHORTCUT, true);

        return newIntent;
    }

    public static void setDynamicShortcutStringForTesting(String label) {
        sLabelForTesting = label;
        ResettersForTesting.register(() -> sLabelForTesting = null);
    }
}
