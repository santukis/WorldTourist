package com.david.worldtourist.itemsdetail.presentation.view;


import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.RelativeLayout;

import com.david.worldtourist.R;
import com.david.worldtourist.utils.Constants;

import butterknife.BindView;
import butterknife.ButterKnife;


public class WebViewFragment extends Fragment {

    @BindView(R.id.web_view) WebView webView;
    @BindView(R.id.progress_bar_layout) RelativeLayout progressBarLayout;

    //////////////////////////////Fragment Lifecycle///////////////////////////////
    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle saveInstanceState) {

        View view = inflater.inflate(R.layout.fragment_web, container, false);

        String url = "";

        Bundle arguments = getArguments();
        if (arguments != null) {
            url = arguments.getString(Constants.URL_KEY);
        }

        ButterKnife.bind(this, view);

        setupWebView();

        setupWebViewListeners();

        webView.loadUrl(url);

        return view;
    }

    private void setupWebView() {
        webView.getSettings().setDisplayZoomControls(true);
        webView.getSettings().setBuiltInZoomControls(true);
    }

    private void setupWebViewListeners() {
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                progressBarLayout.setVisibility(View.GONE);
                webView.setVisibility(View.VISIBLE);
            }
        });
    }

}
