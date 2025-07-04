// Copyright 2021 The Chromium Authors
// Use of this source code is governed by a BSD-style license that can be
// found in the LICENSE file.

package org.chromium.chrome.browser.prefetch.settings;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import org.chromium.chrome.browser.settings.ChromeBaseSettingsFragment;
import org.chromium.components.browser_ui.settings.SettingsUtils;
import org.chromium.components.browser_ui.util.TraceEventVectorDrawableCompat;

/**
 * The base fragment class for Preload Pages settings fragments.
 */
public abstract class PreloadPagesSettingsFragmentBase extends ChromeBaseSettingsFragment {
    @Override
    public void onCreatePreferences(Bundle bundle, String s) {
        SettingsUtils.addPreferencesFromResource(this, getPreferenceResource());
        getActivity().setTitle(R.string.prefs_section_preload_pages_title);

        onCreatePreferencesInternal(bundle, s);

        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() != R.id.menu_id_targeted_help) {
            return false;
        }
        getHelpAndFeedbackLauncher().show(
                getActivity(), getString(R.string.help_context_privacy), null);
        return true;
    }

    /**
     * Called within {@link PreloadPagesSettingsFragmentBase#onCreatePreferences(Bundle, String)}.
     * If the child class needs to handle specific logic during preference creation, it can override
     * this method.
     *
     * @param bundle If the fragment is being re-created from a previous saved state, this is the
     *         state.
     * @param s If non-null, this preference should be rooted at the PreferenceScreen with this key.
     */
    protected void onCreatePreferencesInternal(Bundle bundle, String s) {}

    /**
     * @return The resource id of the preference.
     */
    protected abstract int getPreferenceResource();
}
