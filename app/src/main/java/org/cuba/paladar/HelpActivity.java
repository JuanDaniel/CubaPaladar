package org.cuba.paladar;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.webkit.WebView;

public class HelpActivity extends ActionBarActivity {

    private ActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.help);

        WebView webView = (WebView) findViewById(R.id.web_help);
        webView.setBackgroundColor(Color.WHITE);
        webView.setVerticalScrollBarEnabled(true);
        webView.setVerticalFadingEdgeEnabled(true);

        webView.loadUrl("file:///android_asset/help.html");
    }
}
