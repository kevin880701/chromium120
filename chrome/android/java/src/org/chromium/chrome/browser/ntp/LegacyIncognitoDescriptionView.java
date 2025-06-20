// Copyright 2019 The Chromium Authors
// Use of this source code is governed by a BSD-style license that can be
// found in the LICENSE file.

package org.chromium.chrome.browser.ntp;

import static org.chromium.ui.base.ViewUtils.dpToPx;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.text.style.ForegroundColorSpan;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.IdRes;
import androidx.annotation.StringRes;
import androidx.appcompat.widget.SwitchCompat;

import org.chromium.chrome.R;
import org.chromium.components.content_settings.CookieControlsEnforcement;
import org.chromium.ui.base.ViewUtils;
import org.chromium.ui.text.NoUnderlineClickableSpan;
import org.chromium.ui.text.SpanApplier;
import org.chromium.ui.widget.ChromeBulletSpan;

/**
 * The view to describle incognito mode.
 */
public class LegacyIncognitoDescriptionView
        extends LinearLayout implements IncognitoDescriptionView {
    private int mWidthDp;
    private int mHeightDp;

    private LinearLayout mContainer;
    private TextView mHeader;
    private TextView mSubtitle;
    private LinearLayout mBulletpointsContainer;
    private TextView[] mParagraphs;
    private ViewGroup mCookieControlsCard;
    private SwitchCompat mCookieControlsToggle;
    private ImageView mCookieControlsManagedIcon;
    private TextView mCookieControlsTitle;
    private TextView mCookieControlsSubtitle;

    private static final int BULLETPOINTS_HORIZONTAL_SPACING_DP = 40;
    private static final int BULLETPOINTS_MARGIN_BOTTOM_DP = 12;
    private static final int CONTENT_WIDTH_DP = 600;
    private static final int WIDE_LAYOUT_THRESHOLD_DP = 720;
    private static final int COOKIES_CONTROL_MARGIN_TOP_DP = 12;

    /** Default constructor needed to inflate via XML. */
    public LegacyIncognitoDescriptionView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void setLearnMoreOnclickListener(OnClickListener listener) {

    }

    @Override
    public void setCookieControlsToggleOnCheckedChangeListener(OnCheckedChangeListener listener) {
        if (!findCookieControlElements()) return;
        mCookieControlsToggle.setOnCheckedChangeListener(listener);
    }

    @Override
    public void setCookieControlsToggle(boolean enabled) {
        if (!findCookieControlElements()) return;
        mCookieControlsToggle.setChecked(enabled);
    }

    @Override
    public void setCookieControlsIconOnclickListener(OnClickListener listener) {
        if (!findCookieControlElements()) return;
        mCookieControlsManagedIcon.setOnClickListener(listener);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        mWidthDp = getContext().getResources().getConfiguration().screenWidthDp;
        mHeightDp = getContext().getResources().getConfiguration().screenHeightDp;

        mContainer = findViewById(R.id.new_tab_incognito_container);
        mHeader = findViewById(R.id.new_tab_incognito_title);
        mSubtitle = findViewById(R.id.new_tab_incognito_subtitle);
        mParagraphs = new TextView[] {mSubtitle, findViewById(R.id.new_tab_incognito_features),
                findViewById(R.id.new_tab_incognito_warning)};
        mBulletpointsContainer = findViewById(R.id.new_tab_incognito_bulletpoints_container);

        adjustView();
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // View#onConfigurationChanged() doesn't get called when resizing this view in
        // multi-window mode, so #onMeasure() is used instead.
        Configuration config = getContext().getResources().getConfiguration();
        if (mWidthDp != config.screenWidthDp || mHeightDp != config.screenHeightDp) {
            mWidthDp = config.screenWidthDp;
            mHeightDp = config.screenHeightDp;
            adjustView();
        }

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    public void formatTrackingProtectionText(Context context, View layout) {
        IncognitoDescriptionView.super.formatTrackingProtectionText(context, layout);
        adjustCookieControlsCard();
    }

    private void adjustView() {
        adjustIcon();
        adjustLayout();
        adjustSubtitle();
        adjustCookieControlsCard();
    }

    /** Adjusts the paddings, margins, and the orientation of bulletpoints. */
    private void adjustLayout() {
        int paddingHorizontalDp;
        int paddingVerticalDp;

        boolean bulletpointsArrangedHorizontally;

        if (mWidthDp <= WIDE_LAYOUT_THRESHOLD_DP) {
            // Small padding.
            paddingHorizontalDp = mWidthDp <= 240 ? 24 : 32;
            paddingVerticalDp = 32;

            // Align to the center.
            mContainer.setGravity(Gravity.CENTER_HORIZONTAL);

            // Decide the bulletpoints orientation.
            bulletpointsArrangedHorizontally = false;

            // The subtitle is sized automatically, but not wider than CONTENT_WIDTH_DP.
            mSubtitle.setLayoutParams(
                    new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT));
            mSubtitle.setMaxWidth(dpToPx(getContext(), CONTENT_WIDTH_DP));

            // The bulletpoints container takes the same width as subtitle. Since the width can
            // not be directly measured at this stage, we must calculate it manually.
            mBulletpointsContainer.getLayoutParams().width = dpToPx(
                    getContext(), Math.min(CONTENT_WIDTH_DP, mWidthDp - 2 * paddingHorizontalDp));
        } else {
            // Large padding.
            paddingHorizontalDp = 0; // Should not be necessary on a screen this large.
            paddingVerticalDp = mHeightDp <= 320 ? 16 : 72;

            // Align to the center.
            mContainer.setGravity(Gravity.CENTER_HORIZONTAL);

            // Decide the bulletpoints orientation.
            bulletpointsArrangedHorizontally = true;

            int contentWidthPx = dpToPx(getContext(), CONTENT_WIDTH_DP);
            mSubtitle.setLayoutParams(new LinearLayout.LayoutParams(
                    contentWidthPx, LinearLayout.LayoutParams.WRAP_CONTENT));
            mBulletpointsContainer.getLayoutParams().width = contentWidthPx;
        }

        // Apply the bulletpoints orientation.
        if (bulletpointsArrangedHorizontally) {
            mBulletpointsContainer.setOrientation(LinearLayout.HORIZONTAL);
        } else {
            mBulletpointsContainer.setOrientation(LinearLayout.VERTICAL);
        }

        // Set up paddings and margins.
        int paddingTop;
        int paddingBottom;
        paddingTop = paddingBottom = dpToPx(getContext(), paddingVerticalDp);
        mContainer.setPadding(dpToPx(getContext(), paddingHorizontalDp), paddingTop,
                dpToPx(getContext(), paddingHorizontalDp), paddingBottom);

        // Total space between adjacent paragraphs (Including margins, paddings, etc.)
        int totalSpaceBetweenViews = getContext().getResources().getDimensionPixelSize(
                R.dimen.incognito_ntp_total_space_between_views);

        for (TextView paragraph : mParagraphs) {
            // If bulletpoints are arranged horizontally, there should be space between them.
            int rightMarginPx = (bulletpointsArrangedHorizontally
                                        && paragraph == mBulletpointsContainer.getChildAt(0))
                    ? dpToPx(getContext(), BULLETPOINTS_HORIZONTAL_SPACING_DP)
                    : 0;

            ((LinearLayout.LayoutParams) paragraph.getLayoutParams())
                    .setMargins(0, totalSpaceBetweenViews, rightMarginPx, 0);
            paragraph.setLayoutParams(paragraph.getLayoutParams()); // Apply the new layout.
        }
    }

    /** Adjust the Incognito icon. */
    private void adjustIcon() {
        ImageView icon = (ImageView) findViewById(R.id.new_tab_incognito_icon);
        ViewGroup.MarginLayoutParams iconLayoutParams = (ViewGroup.MarginLayoutParams) icon.getLayoutParams();
        iconLayoutParams.bottomMargin = 48;
        icon.setLayoutParams(iconLayoutParams);
    }

    /** Adjust the Incognito subtitle. */
    private void adjustSubtitle() {
        final String subtitleText = getContext().getResources().getString(R.string.ub_homepage_message);

        mSubtitle.setText(subtitleText);
        mSubtitle.setMovementMethod(null);
    }

    /** Adjust the Cookie Controls Card. */
    private void adjustCookieControlsCard() {
        mCookieControlsCard = findViewById(R.id.cookie_controls_card);
        if (mCookieControlsCard == null) {
            mCookieControlsCard = findViewById(R.id.tracking_protection_card);
        }
        // Still null - not inflated yet.
        if (mCookieControlsCard == null) return;
        if (mWidthDp <= WIDE_LAYOUT_THRESHOLD_DP) {
            // Portrait
            mCookieControlsCard.getLayoutParams().width = LinearLayout.LayoutParams.MATCH_PARENT;
        } else {
            // Landscape
            mCookieControlsCard.getLayoutParams().width = dpToPx(getContext(), CONTENT_WIDTH_DP);
        }
        mCookieControlsCard.setVisibility(View.GONE);
    }

    @Override
    public void setCookieControlsEnforcement(@CookieControlsEnforcement int enforcement) {
        // No cookie controls toggle on the page.
        if (!findCookieControlElements()) return;

        boolean enforced = enforcement != CookieControlsEnforcement.NO_ENFORCEMENT;
        mCookieControlsToggle.setEnabled(!enforced);
        mCookieControlsManagedIcon.setVisibility(enforced ? View.VISIBLE : View.GONE);
        mCookieControlsTitle.setEnabled(!enforced);
        mCookieControlsSubtitle.setEnabled(!enforced);

        Resources resources = getContext().getResources();
        StringBuilder subtitleText = new StringBuilder();
        subtitleText.append(resources.getString(R.string.new_tab_otr_third_party_cookie_sublabel));
        if (!enforced) {
            mCookieControlsSubtitle.setText(subtitleText.toString());
            return;
        }

        int iconRes;
        String addition;
        switch (enforcement) {
            case CookieControlsEnforcement.ENFORCED_BY_POLICY:
                iconRes = R.drawable.ic_business_small;
                addition = resources.getString(R.string.managed_by_your_organization);
                break;
            case CookieControlsEnforcement.ENFORCED_BY_COOKIE_SETTING:
                iconRes = R.drawable.settings_cog;
                addition = resources.getString(
                        R.string.new_tab_otr_cookie_controls_controlled_tooltip_text);
                break;
            default:
                return;
        }
        mCookieControlsManagedIcon.setImageResource(iconRes);
        subtitleText.append("\n");
        subtitleText.append(addition);
        mCookieControlsSubtitle.setText(subtitleText.toString());
    }

    /** Finds the 3PC controls and returns true if they exist. */
    private boolean findCookieControlElements() {
        mCookieControlsToggle = findViewById(R.id.cookie_controls_card_toggle);
        if (mCookieControlsToggle == null) return false;
        mCookieControlsManagedIcon = findViewById(R.id.cookie_controls_card_managed_icon);
        mCookieControlsTitle = findViewById(R.id.cookie_controls_card_title);
        mCookieControlsSubtitle = findViewById(R.id.cookie_controls_card_subtitle);
        return true;
    }
}
