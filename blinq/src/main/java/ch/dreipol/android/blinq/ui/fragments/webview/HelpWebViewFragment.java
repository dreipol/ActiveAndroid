package ch.dreipol.android.blinq.ui.fragments.webview;

import ch.dreipol.android.blinq.R;

/**
 * Created by phil on 24.04.14.
 */
public class HelpWebViewFragment extends BaseWebViewFragment {
    @Override
    protected String getUrl() {
        return getString(R.string.helpUrl);
    }

    @Override
    protected String getTitle() {
        return "HELP";
    }
}
