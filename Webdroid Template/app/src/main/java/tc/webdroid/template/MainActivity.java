package tc.webdroid.template;
 
import android.app.Activity;
import android.os.Bundle;
import android.app.ActionBar;
import tc.dedroid.util.DedroidWeb;
import android.webkit.*;
import android.widget.*;
import tc.dedroid.util.*;
import android.content.res.*;

public class MainActivity extends Activity {
     
	private WebView wv;
	private long exitTime = 0;
	private JsBridge jsb;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
		String indexUrl=DedroidConfig.getString(this,"settings","index_url");
		if(indexUrl.equals("")){
			indexUrl="file:///android_asset/index.html";
		}
        DedroidWeb.WebPage wp=new DedroidWeb.WebPage(this,indexUrl);
		wv=wp.getWebView();
		jsb=new JsBridge();
		jsb.setAttr(this,this,wv);
		wv.addJavascriptInterface(jsb,"webdroid");
		wv.addJavascriptInterface(jsb,"wd");
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
		//jsb.fullScreen();
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
		if(Utils.getSdk()<29) return;
		int currentNightMode = newConfig.uiMode & Configuration.UI_MODE_NIGHT_MASK;
		switch (currentNightMode) {
			case Configuration.UI_MODE_NIGHT_YES:
				wv.evaluateJavascript("javascript:"+jsb.darkChangeCalklback+"(true)",null);
				break;
			case Configuration.UI_MODE_NIGHT_NO:
				wv.evaluateJavascript("javascript:"+jsb.darkChangeCalklback+"(false)",null);
				break;
			case Configuration.UI_MODE_NIGHT_UNDEFINED:
				break;
		}
	}
}
