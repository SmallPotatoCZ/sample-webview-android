package com.zhangchaocloud.samplewebview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.JsPromptResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {
    private static final String TAG=MainActivity.class.getCanonicalName();

    private WebView webview;
    private ProgressBar mLoading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 之前基础的代码

//        webview=(WebView)findViewById(R.id.webView);
//
//        webview.setWebViewClient(new WebViewClient());
//        webview.getSettings().setJavaScriptEnabled(true);
//        webview.getSettings().setDomStorageEnabled(true);
//        webview.setOverScrollMode(WebView.OVER_SCROLL_NEVER);
//        webview.loadUrl("https://www.baidu.com");

        // 第二次加载的本地资源
        webview=(WebView)findViewById(R.id.wvPortal);
        mLoading=(ProgressBar)findViewById(R.id.pbLoading);
        webview.loadUrl("file:///android_asset/www/index.html");

        WebSettings mwebSettings=webview.getSettings();

        mwebSettings.setJavaScriptEnabled(true);
        webview.setWebChromeClient(new BridgeWCClient());

    }

    private class BridgeWCClient extends WebChromeClient{
        @Override

        public boolean onJsPrompt(WebView view, String url, String title, String message, JsPromptResult result){
            if(title.equals("BRIDGE_KEY")){
                JSONObject commandJSON=null;
                try{
                    commandJSON=new JSONObject(message);
                    processCommand(commandJSON);
                }catch (JSONException ex){
                    Log.e(TAG,"Invalid JSON: "+ex.getMessage());

                    result.confirm();

                    return true;
                }

                result.confirm();

                return true;
            }else{
                return false;
            }
        }
    }

    private void processCommand(JSONObject commandJSON)

            throws JSONException{

        String command = commandJSON.getString("method");

        if("login".equals(command)){

            int state = commandJSON.getInt("state");

            if(state == 0){

                MainActivity.this.runOnUiThread(new Runnable() {

                    @Override

                    public void run() {

                        mLoading.setVisibility(View.VISIBLE);

                    }

                });

            }

            else if(state == 1){

                MainActivity.this.runOnUiThread(new Runnable() {

                    @Override

                    public void run() {

                        mLoading.setVisibility(View.GONE);
                        mLoading.setVisibility(View.GONE);

                        webview.loadUrl("javascript:Bridge.callBack({success:true, message:\"logged in\"})");
                    }

                });

            }

        }

    }
}


