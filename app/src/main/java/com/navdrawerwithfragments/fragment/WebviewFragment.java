package com.navdrawerwithfragments.fragment;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.ufo.learnchinese2.MainActivity;
import com.ufo.learnchinese2.R;
import com.ufo.learnchinese2.database.Database;
import com.ufo.learnchinese2.utils.Utils;

public class WebviewFragment extends Fragment {
    String content;
    int grammarId;
    ProgressDialog loadingDialog;
    MainActivity mActivity;
    Database mDatabase;
    WebView mWebview = null;
    String title = "";

    class C04691 extends WebViewClient {
        C04691() {
        }

        public void onPageFinished(WebView webView, String str) {
            super.onPageFinished(webView, str);
            WebviewFragment.this.loadingDialog.dismiss();
        }
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        this.grammarId = getArguments().getInt(Utils.GRAMMAR_COLUMN_ID);
        this.mActivity = (MainActivity) getActivity();
        setupActionBar();
        this.title = getArguments().getString("TITLE");
        this.mDatabase = Database.newInstance(this.mActivity, Utils.GRAMMAR_DATABASE_NAME);
        this.content = this.mDatabase.getGrammarContent(this.grammarId);
        this.loadingDialog = ProgressDialog.show(this.mActivity, null, null);
    }

    public void setupActionBar() {
        if (this.mActivity.getActionBar() != null) {
            this.mActivity.getActionBar().setDisplayHomeAsUpEnabled(true);
            this.mActivity.getActionBar().setHomeButtonEnabled(true);
        }
        if (this.mActivity.getDrawerToogle() != null) {
            this.mActivity.getDrawerToogle().setDrawerIndicatorEnabled(false);
        }
    }

    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View view = layoutInflater.inflate(R.layout.webview, viewGroup, false);
        this.mWebview = (WebView) view.findViewById(R.id.web_view);
        this.mWebview.loadDataWithBaseURL(null, this.content, "text/html", "utf-8", null);
        this.mWebview.getSettings().setBuiltInZoomControls(true);
        this.mWebview.getSettings().setDisplayZoomControls(false);
        this.mWebview.setWebViewClient(new C04691());
        return view;
    }

    public void onResume() {
        super.onResume();
        this.mActivity.showBannerAd();
        getActivity().setTitle(this.title);
    }

    public void onDetach() {
        super.onDetach();
    }
}
