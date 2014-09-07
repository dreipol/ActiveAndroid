package ch.dreipol.android.blinq.ui.fragments.webview;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import ch.dreipol.android.blinq.R;
import ch.dreipol.android.blinq.ui.fragments.BlinqFragment;
import ch.dreipol.android.blinq.ui.fragments.IHeaderConfigurationProvider;
import ch.dreipol.android.blinq.ui.headers.IHeaderViewConfiguration;

/**
 * Created by phil on 24.04.14.
 */
public abstract class BaseWebViewFragment extends BlinqFragment implements IHeaderConfigurationProvider {


    protected abstract String getUrl();

    protected abstract String getTitle();

    @Override
    public void onStart() {
        super.onStart();
        WebView webView = (WebView) getView().findViewById(R.id.blinq_webView);
        webView.loadUrl(getUrl());

    }

    @Override
    protected int getLayoutResourceId() {
        return R.layout.fragment_webview;
    }

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

            @Override
            public boolean hasIcon() {
                return false;
            }

            @Override
            public int getIconDrawable() {
                return 0;
            }

            @Override
            public void iconTapped() {

            }
        };
    }
}
