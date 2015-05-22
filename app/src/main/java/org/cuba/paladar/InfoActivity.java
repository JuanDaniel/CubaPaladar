package org.cuba.paladar;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class InfoActivity extends ActionBarActivity {

    private WebView webView;
    private ProgressDialog progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.info);

        progressBar = new ProgressDialog(this);
        progressBar.setIndeterminate(true);
        progressBar.setMessage(getString(R.string.msg_loading));
        progressBar.show();

        webView = (WebView) findViewById(R.id.web_info);
        webView.setBackgroundColor(Color.WHITE);
        webView.setVerticalScrollBarEnabled(true);
        webView.setVerticalFadingEdgeEnabled(true);

        webView.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onLoadResource(WebView view, String url) {
                if (!progressBar.isShowing()) {
                    progressBar.show();
                }
                super.onLoadResource(view, url);
            }

            public void onPageFinished(WebView view, String url) {
                if (progressBar.isShowing()) {
                    progressBar.dismiss();
                }
            }
        });

		/*
         * InputStream in = null; try { in = getAssets().open("info2.html"); }
		 * catch (IOException e) { // TODO Auto-generated catch block
		 * e.printStackTrace(); } BufferedReader br = new BufferedReader(new
		 * InputStreamReader(in)); String info = ""; String line; try {
		 * while((line = br.readLine()) != null){ info += line; } } catch
		 * (IOException e) { // TODO Auto-generated catch block
		 * e.printStackTrace(); }
		 * 
		 * webView.loadData(info, "text/html; charset=utf-8", "UTF-8");
		 */

        webView.loadUrl("file:///android_asset/info2.html");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);

                return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
