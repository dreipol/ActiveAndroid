package ch.dreipol.android.blinq.ui.fragments.webview;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import ch.dreipol.android.blinq.R;
import ch.dreipol.android.blinq.ui.fragments.IHeaderConfigurationProvider;
import ch.dreipol.android.blinq.ui.headers.IHeaderViewConfiguration;

/**
 * Created by phil on 24.04.14.
 */
public abstract class BaseWebViewFragment extends Fragment implements IHeaderConfigurationProvider {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_webview, container, false);
        WebView webView = (WebView) v.findViewById(R.id.blinq_webView);
        webView.loadUrl(getUrl());
        return v;
    }

    protected abstract String getUrl();

    protected abstract String getTitle();


    @Override
    public IHeaderViewConfiguration getHeaderConfiguration() {
        return new IHeaderViewConfiguration() {
            @Override
            public boolean showTitle() {
                return true;
            }

            @Override
            public String getTitle() {
                return BaseWebViewFragment.this.getTitle();
            }
        };
    }
}
