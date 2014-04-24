package ch.dreipol.android.blinq.ui.fragments.webview;

/**
 * Created by phil on 24.04.14.
 */
public class HelpWebViewFragment extends BaseWebViewFragment {
    @Override
    protected String getUrl() {
        return "http://www.joinblinq.com/help/webview/lang/en";
    }

    @Override
    protected String getTitle() {
        return "HELP";
    }
}
