package tc.webdroid;

import android.app.Activity;
import android.os.Bundle;
import android.app.*;
import tc.dedroid.util.*;
import tc.webdroid.template.*;
import android.webkit.*;
import android.widget.*;
import android.content.*;
import android.content.res.*;
import android.os.*;

public class ProjectViewerActivity extends Activity {
    
    public static final String TAG = "ProjectViewerActivity";
    private WebView wv;
	private long exitTime = 0;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
		Intent i=getIntent();
		String project=i.getExtras().getString("project");
		String file="html/index.html";
		if(i.hasExtra("file")){
			file=i.getStringExtra("file");
		}
        DedroidWeb.WebPage wp=new DedroidWeb.WebPage(this,DedroidFile.EXTERN_STO_PATH+"/1503Dev/WebProjects/"+project+"/"+file);
		wv=wp.getWebView();
		JsBridge.setAttr(this,this,wv);
		wv.addJavascriptInterface(new JsBridge.webdroid(),"webdroid");
		wv.addJavascriptInterface(new JsBridge.webdroid(),"wd");
		//tc.webdroid.MainActivity.JsBridge wds=new tc.webdroid.MainActivity.JsBridge();
		//wds.setAttr(this,this,wv);
		//wv.addJavascriptInterface(wds,"wds");
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
