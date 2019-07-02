package com.example.pill_f;

import android.app.Activity;
import android.webkit.WebView;

public class GotoWeb {
    Activity mainActivity;
    WebView webView;

    public GotoWeb(Activity aa){
        mainActivity = aa;
    }
    public void GotoServer(){
        mainActivity.setContentView(R.layout.web_main);

        webView = (WebView)mainActivity.findViewById(R.id.webView);

        webView.loadUrl("http://13.125.23.142:3000/view");
    }
}
