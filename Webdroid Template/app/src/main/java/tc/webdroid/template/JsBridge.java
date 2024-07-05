package tc.webdroid.template;
import android.webkit.*;
import tc.dedroid.util.*;
import android.content.*;
import android.app.*;
import android.widget.*;
import java.net.*;
import android.os.*;
import android.content.pm.*;
import android.health.connect.datatypes.*;
import android.content.pm.PackageManager.*;
import java.security.*;
import android.net.*;
import java.io.*;
import org.json.*;
import androidx.core.content.*;
import java.util.concurrent.*;
import java.util.*;

public class JsBridge
{
	static private Context _context;
	static private Activity _activity;
	static private WebView _webview;
	static private int webdroidApiVersion=3;
	static public List<String> blockedUrls=new ArrayList<>();
	
	static public void setAttr(Context ctx, Activity act, WebView wv)
	{
		_context = ctx;
		_activity = act;
		_webview = wv;
	}
	static public class webdroid
	{
		@JavascriptInterface
		public int getVersion()
		{
			return webdroidApiVersion;
		}
		// 基本
		@JavascriptInterface
		public void alert(String title, String content, boolean cancelable)
		{
			DedroidDialog.alert(_activity, title, content, cancelable);
		}
		@JavascriptInterface
		public void alert(String title, String content)
		{
			DedroidDialog.alert(_activity, title, content, true);
		}
		@JavascriptInterface
		public void alert(String content, boolean cancelable)
		{
			DedroidDialog.alert(_activity, content, cancelable);
		}
		@JavascriptInterface
		public void alert(String content)
		{
			DedroidDialog.alert(_activity, content, true);
		}
		@JavascriptInterface
		public void toast(String text)
		{
			DedroidToast.toast(_activity, text);
		}
		@JavascriptInterface
		public void toastLong(String text)
		{
			DedroidToast.toast(_activity, text, android.widget.Toast.LENGTH_LONG);
		}
		@JavascriptInterface
		public String getAssetsUrl(){
			return "file:///android_asset/";
		}
		@JavascriptInterface
		public String getStorageUrl(){
			return "file://"+DedroidFile.EXTERN_STO_PATH+"/";
		}
		//文件
		@JavascriptInterface
		public String getStoragePath(){
			return DedroidFile.EXTERN_STO_PATH;
		}
		@JavascriptInterface
		public boolean isFileExists(String path){
			return DedroidFile.exists(path);
		}
		@JavascriptInterface
		public String fileGetContents(String path) throws IOException{
			return DedroidFile.read(path);
		}
		@JavascriptInterface
		public String listFile(String path){
			return new JSONArray(DedroidFile.listName(path)).toString();
		}
		// 软件包
		@JavascriptInterface
		public String getPackageName(){
			return _context.getPackageName();
		}
		@JavascriptInterface
		public String getAppName() throws PackageManager.NameNotFoundException{
			PackageManager pm=_context.getPackageManager();
			ApplicationInfo ai=pm.getApplicationInfo(_context.getPackageName(),0);
			return pm.getApplicationLabel(ai).toString();
		}
		@JavascriptInterface
		public String getAppName(String pkg) throws PackageManager.NameNotFoundException{
			PackageManager pm=_context.getPackageManager();
			ApplicationInfo ai=pm.getApplicationInfo(pkg,0);
			return pm.getApplicationLabel(ai).toString();
		}
		@JavascriptInterface
		public String getVersionName() throws PackageManager.NameNotFoundException{
			PackageManager pm=_context.getPackageManager();
			PackageInfo pi=pm.getPackageInfo(_context.getPackageName(),0);
			return pi.versionName;
		}
		@JavascriptInterface
		public String getVersionName(String pkg) throws PackageManager.NameNotFoundException{
			PackageManager pm=_context.getPackageManager();
			PackageInfo pi=pm.getPackageInfo(pkg,0);
			return pi.versionName;
		}
		@JavascriptInterface
		public int getVersionCode() throws PackageManager.NameNotFoundException{
			PackageManager pm=_context.getPackageManager();
			PackageInfo pi=pm.getPackageInfo(_context.getPackageName(),0);
			return pi.versionCode;
		}
		@JavascriptInterface
		public int getVersionCode(String pkg) throws PackageManager.NameNotFoundException{
			PackageManager pm=_context.getPackageManager();
			PackageInfo pi=pm.getPackageInfo(pkg,0);
			return pi.versionCode;
		}
		@JavascriptInterface
		public boolean isInstalled(String pkg){
			return Dedroid.isAppInstalled(_context,pkg);
		}
		@JavascriptInterface
		public boolean appSettings(String packageName){
			try {
				Intent intent = new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
				Uri uri = Uri.fromParts("package", packageName, null);
				intent.setData(uri);
				_context.startActivity(intent);
				return true;
			} catch (Exception e) {
				return false;
			}
		}
		// 系统
		@JavascriptInterface
		public void finish(){
			_activity.runOnUiThread(new Runnable(){
					@Override
					public void run()
					{
						_activity.finish();
					}
			});
		}
		@JavascriptInterface
		public void finishAndRemoveTask(){
			_activity.runOnUiThread(new Runnable(){
					@Override
					public void run()
					{
						_activity.finishAndRemoveTask();
					}
				});
		}
		@JavascriptInterface
		public void exit(){
			System.exit(0);
		}
		@JavascriptInterface
		public void exit(int status){
			System.exit(status);
		}
		@JavascriptInterface
		public boolean startActivity(String packageName){
			try
			{
				Dedroid.startActivity(_context, packageName);
				return true;
			}
			catch (ClassNotFoundException e)
			{
				return false;
			}
		}
		@JavascriptInterface
		public boolean startActivity(String packageName,String activityName){
			Intent i=new Intent();
			i.setComponent(new ComponentName(packageName,activityName));
			if(i.resolveActivity(_context.getPackageManager())!=null){
				_context.startActivity(i);
				return true;
			} else {
				return false;
			}
		}
		@JavascriptInterface
		public void requestPermission(String permissionName){
			Dedroid.requestPermission(_activity,permissionName);
		}
		@JavascriptInterface
		public boolean hasPermission(String permissionName){
			return ContextCompat.checkSelfPermission(_context,permissionName)==PackageManager.PERMISSION_GRANTED;
		}
		@JavascriptInterface
		public boolean isVpnConnected() {
			ConnectivityManager connectivityManager = (ConnectivityManager) _context.getSystemService(Context.CONNECTIVITY_SERVICE);
			if (connectivityManager != null) {
				NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
				if (activeNetwork != null) {
					return activeNetwork.getType() == ConnectivityManager.TYPE_VPN;
				}
			}
			return false;
		}
		// 网络
		@JavascriptInterface
		public void downloadFile(String url,String localPath){
			DedroidNetwork.download(url,localPath);
		}
		@JavascriptInterface
		public void httpGet(final String url, final String callback, final int symbo)
		{
			new Thread(new Runnable(){
					@Override
					public void run()
					{
						DedroidNetwork.get(url, new DedroidNetwork.HttpCallback(){
								@Override
								public void onResponse(final String responseString, final int httpCode)
								{
									_activity.runOnUiThread(new Runnable(){
											@Override
											public void run()
											{
												_webview.evaluateJavascript("javascript:" +
																			callback +
																			"('" +
																			URLEncoder.encode(Utils.removeLastNewline(responseString)) +
																			"'," +
																			httpCode +
																			"," +
																			symbo +
																			");", null);
											}
										});
								}
								@Override
								public void onFailure(Exception e)
								{
									_activity.runOnUiThread(new Runnable(){

											@Override
											public void run()
											{
												_webview.evaluateJavascript("javascript:" + callback + "(false,0," + symbo + ");", null);
											}
										});
								}
							});
					}
				}).start();
		}
	}
}
