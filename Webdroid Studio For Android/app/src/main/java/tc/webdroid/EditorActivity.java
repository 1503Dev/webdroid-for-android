package tc.webdroid;

import android.app.Activity;
import android.os.Bundle;
import android.view.*;
import android.app.*;
import android.webkit.*;
import tc.dedroid.util.*;
import tc.webdroid.template.*;
import android.*;
import android.widget.*;
import java.net.*;
import android.content.*;
import android.content.res.*;
import android.os.*;

public class EditorActivity extends Activity {
    
    public static final String TAG = "EditorActivity";
    private static Activity mc;
	private WebView wv;
	private long exitTime = 0;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		ActionBar actionBar = getActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        mc=this;
		Dedroid.requestPermission(mc,Manifest.permission.WRITE_EXTERNAL_STORAGE);
		Intent i=getIntent();
		String indexUrl="file:///android_asset/local_html/editor.html";
		if(!this.getSharedPreferences("configs",this.MODE_PRIVATE).getBoolean("enable_my",true)){
			indexUrl="file:///android_asset/local_html/editor-md.html";
		}
		indexUrl=indexUrl+"?project="+URLEncoder.encode(i.getStringExtra("project"));
		DedroidWeb.WebPage wp=new DedroidWeb.WebPage(this,indexUrl);
		wv=wp.getWebView();
		MainActivity.JsBridge jsb=new MainActivity.JsBridge();
		jsb.setAttr(this,this,wv);
		wv.addJavascriptInterface(jsb,"wds");
		tc.webdroid.template.JsBridge jsb2=new tc.webdroid.template.JsBridge();
		jsb2.setAttr(this,this,wv);
		wv.addJavascriptInterface(jsb2,"wd");
		WebSettings settings = wv.getSettings();
        settings.setUseWideViewPort(true);//设定支持viewport
        settings.setLoadWithOverviewMode(true);   //自适应屏幕
        settings.setBuiltInZoomControls(true);
        settings.setDisplayZoomControls(false);
        settings.setSupportZoom(true);//设定支持缩放
        // 启用JavaScript
        settings.setJavaScriptEnabled(true);
        // 允许从文件加载
        settings.setAllowFileAccessFromFileURLs(true);
        settings.setAllowUniversalAccessFromFileURLs(true);
        settings.setDomStorageEnabled(true);
    }
	@Override
    public void onBackPressed() {
        if (wv.canGoBack()) {
            wv.goBack();
        } else {
            if ((System.currentTimeMillis() - exitTime) > 2000) {
                Toast.makeText(getApplicationContext(), "再按一次退出",
                               Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
                super.onBackPressed();
            }

        }
    }
    @Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		if(Build.VERSION.SDK_INT<29) return;
		int currentNightMode = newConfig.uiMode & Configuration.UI_MODE_NIGHT_MASK;
		switch (currentNightMode) {
			case Configuration.UI_MODE_NIGHT_YES:
				wv.evaluateJavascript("javascript:"+JsBridge.darkChangeCalklback+"(true)",null);
				break;
			case Configuration.UI_MODE_NIGHT_NO:
				wv.evaluateJavascript("javascript:"+JsBridge.darkChangeCalklback+"(false)",null);
				break;
			case Configuration.UI_MODE_NIGHT_UNDEFINED:
				break;
		}
	}
}
