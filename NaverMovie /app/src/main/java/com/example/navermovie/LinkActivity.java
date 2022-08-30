package com.example.navermovie;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

public class LinkActivity extends AppCompatActivity {
    ProgressBar bar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_link);

        bar=findViewById(R.id.bar);
        bar.setVisibility(View.VISIBLE);
        Intent intent=getIntent();
        String link=intent.getStringExtra("link");

        getSupportActionBar().setTitle(link);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        WebView web=findViewById(R.id.web);
        web.setWebViewClient(new MyWeb());
        web.getSettings().setJavaScriptEnabled(true);
        web.getSettings().setDomStorageEnabled(true);
        web.loadUrl(link);
    }
class MyWeb extends WebViewClient{
    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url ) {
        return super.shouldOverrideUrlLoading(view, url);
    }
}
 
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        return super.onOptionsItemSelected(item);

    }

}