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
	private static View dialog_creator_view;
	private static AlertDialog dialog_creator;
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
		String indexUrl=DedroidConfig.getString(this,"configs","index_editor_url");
		Intent i=getIntent();
		if(indexUrl.equals("")){
			indexUrl="file:///android_asset/local_html/editor.html?project="+URLEncoder.encode(i.getStringExtra("project"));
		}
		DedroidWeb.WebPage wp=new DedroidWeb.WebPage(this,indexUrl);
		wv=wp.getWebView();
		MainActivity.JsBridge.setAttr(this,this,wv);
		wv.addJavascriptInterface(new MainActivity.JsBridge(),"wds");
		tc.webdroid.template.JsBridge.setAttr(this,this,wv);
		wv.addJavascriptInterface(new tc.webdroid.template.JsBridge.webdroid(),"wd");
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
