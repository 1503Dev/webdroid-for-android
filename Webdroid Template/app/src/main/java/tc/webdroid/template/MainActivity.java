package tc.webdroid.template;
 
import android.app.Activity;
import android.os.Bundle;
import android.app.ActionBar;
import tc.dedroid.util.DedroidWeb;
import android.webkit.*;
import android.widget.*;

public class MainActivity extends Activity {
     
	private WebView wv;
	private long exitTime = 0;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        DedroidWeb.WebPage wp=new DedroidWeb.WebPage(this,"file:///android_asset/index.html");
		wv=wp.getWebView();
		JsBridge.setAttr(this,this,wv);
		wv.addJavascriptInterface(new JsBridge.webdroid(),"webdroid");
		wv.addJavascriptInterface(new JsBridge.webdroid(),"wd");
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
}
