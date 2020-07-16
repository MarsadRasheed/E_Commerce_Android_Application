package com.hamlet.MrFixer;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

public class LiveChatActivity extends AppCompatActivity {

    private WebView webView;
    private ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live_chat);

        View view = findViewById(android.R.id.content);
        String message = "Let's talk about your queries.";
        Snackbar.make(view,message,Snackbar.LENGTH_LONG).setAction("Action",null).show();

        webView = findViewById(R.id.liveChatWebView);
        progressBar = findViewById(R.id.liveChatProgressBar);

        getSupportActionBar().setTitle("Live Chat");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        WebSettings webSettings = webView.getSettings();
        webView.loadUrl("https://tawk.to/chat/5d167f9b7a48df6da2421422/default");
        webSettings.setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                Toast.makeText(LiveChatActivity.this, "Please wait!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                progressBar.setVisibility(View.GONE);
                webView.setVisibility(View.VISIBLE);
            }
        });
    }
}
