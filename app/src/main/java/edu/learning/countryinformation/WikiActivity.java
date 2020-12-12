package edu.learning.countryinfo;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import edu.learning.fit2081.countryinfo.R;

import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;

public class WikiActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.AppTheme_AppBarOverlay);
        setContentView(R.layout.activity_wiki2);
        Button homeBt = findViewById(R.id.backBt);
        homeBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        Intent intent = getIntent();
        final String wikiURL = intent.getStringExtra("wikiURL"); // gets wikipedia url from last activity
        final WebView webView = (WebView) findViewById(R.id.webView);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setAllowFileAccess(true);
        webView.getSettings().setLoadsImagesAutomatically(true);
        webView.getSettings().setBlockNetworkLoads(false);
        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.setWebViewClient(new WebViewClient()); // ensures it doesnt open a default browser and instead runs the website inside the webview

        webView.post(new Runnable() { // runs it on a new thread
            @Override
            public void run() {
                webView.loadUrl(wikiURL);
            }
        });

    }

}
