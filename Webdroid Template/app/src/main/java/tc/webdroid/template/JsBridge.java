package tc.webdroid.template;
import android.webkit.*;
import android.webkit.CookieManager;
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
import android.view.*;
import android.graphics.*;
import java.nio.charset.*;

public class JsBridge
{
	static private Context _context;
	static private Activity _activity;
	static private WebView _webview;
	static private int webdroidApiVersion=7;
	static public String darkChangeCalklback="void";
	static public List<String> blockedUrls=new ArrayList<>();

	static public void setAttr(Context ctx, Activity act, WebView wv)
	{
		_context = ctx;
		_activity = act;
		_webview = wv;
	}
	public static void run(Runnable r)
	{
		_activity.runOnUiThread(r);
	}
	public void runJs(final String js)
	{
		run(new Runnable(){

				@Override
				public void run()
				{
					_webview.evaluateJavascript("javascript:" + js, null);
				}

			});
	}
	@JavascriptInterface
	public int getVersion()
	{
		return webdroidApiVersion;
	}
	// 基本
	@JavascriptInterface
	public void alert(final String title, final String content, final boolean cancelable)
	{
		run(new Runnable(){
				@Override
				public void run()
				{
					DedroidDialog.alert(_activity, title, content, cancelable);
				}
			});
	}
	@JavascriptInterface
	public void alert(final String title, final String content)
	{
		run(new Runnable(){
				@Override
				public void run()
				{
					DedroidDialog.alert(_activity, title, content, true);
				}
			});
	}
	@JavascriptInterface
	public void alert(final String content, final boolean cancelable)
	{
		run(new Runnable(){
				@Override
				public void run()
				{
					DedroidDialog.alert(_activity, content, cancelable);
				}
			});
	}
	@JavascriptInterface
	public void alert(final String content)
	{
		run(new Runnable(){
				@Override
				public void run()
				{
					DedroidDialog.alert(_activity, content, true);
				}
			});
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
	public String getAssetsUrl()
	{
		return "file:///android_asset/";
	}
	@JavascriptInterface
	public String getStorageUrl()
	{
		return "file://" + DedroidFile.EXTERN_STO_PATH + "/";
	}
	@JavascriptInterface
	public void setStatusBarColor(final int colorR, final int colorG, final int colorB)
	{
		run(new Runnable(){

				@Override
				public void run()
				{
					Window window = _activity.getWindow();
					window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
					window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
					window.setStatusBarColor(Color.rgb(colorR, colorG, colorB));
				}
			});

	}
	@JavascriptInterface
	public void clearCaches()
	{
		_context.getCacheDir().delete();
		_webview.clearHistory();
		_webview.clearFormData();
		_webview.clearCache(true);
	}
    @JavascriptInterface
    public void clearCache()
	{
        clearCaches();
    }
    @JavascriptInterface
    public void clearCookies()
	{
        if (Build.VERSION.SDK_INT >= 20)
		{
            CookieManager cookieManager = CookieManager.getInstance();
            cookieManager.removeAllCookies(null);
            cookieManager.flush();
        }
		else
		{
            CookieSyncManager cookieSyncManager = CookieSyncManager.createInstance(_context);
            CookieManager cookieManager = CookieManager.getInstance();
            cookieManager.removeAllCookie();
            cookieSyncManager.sync();
        }
    }
    @JavascriptInterface
    public void clearCookie()
	{
        clearCookies();
	}
    @JavascriptInterface
    public void clearWebStorages()
	{
        WebStorage.getInstance().deleteAllData();
    }
    @JavascriptInterface
    public void clearWebStorage()
	{
        clearWebStorages();
	}
	//设置
	@JavascriptInterface
	public boolean setIndexUrl(String url)
	{
		return DedroidConfig.putString(_context, "settings", "index_url", url);
	}
	@JavascriptInterface
	public String getIndexUrl()
	{
		return DedroidConfig.getString(_context, "settings", "index_url");
	}
	//文件
	@JavascriptInterface
	public String getStoragePath()
	{
		return DedroidFile.EXTERN_STO_PATH;
	}
	@JavascriptInterface
	public boolean isFileExists(String path)
	{
		return DedroidFile.exists(path);
	}
	@JavascriptInterface
	public String fileGetContents(String path) throws IOException
	{
		return DedroidFile.read(path);
	}
	@JavascriptInterface
	public String listFile(String path)
	{
		return new JSONArray(DedroidFile.listName(path)).toString();
	}
	@JavascriptInterface
	public boolean openFile(String fp)
	{
		File file = new File(fp);
		if (file.exists())
		{
			try
			{
				Uri path;
				Intent intent = new Intent(Intent.ACTION_VIEW);
				if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
				{
					path = FileProvider.getUriForFile(_context, getPackageName()+".FileProvider", file);
				}
				else
				{
					path = Uri.fromFile(file);
				}
				intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
				String type=Utils.getMimeType(file);
				intent.setDataAndType(path, type);
				_activity.startActivity(intent);
				return true;
			}
			catch (ActivityNotFoundException e)
			{
				runJs("onException('ActivityNotFoundException','" + URLEncoder.encode(e.toString()) + "')");
				return false;
			}
		}
		else
		{
			return false;
		}
	}
	@JavascriptInterface
	public boolean isDir(String path)
	{
		return new File(path).isDirectory();
	}
	@JavascriptInterface
	public String readFile(String path)
	{
		try
		{
			return DedroidFile.read(path);
		}
		catch (IOException e)
		{
			runJs("onException('IOException','" + URLEncoder.encode(e + "") + "')");
			return null;
		}
	}
	@JavascriptInterface
	public boolean writeString(String path, String str)
	{
		try
		{
			DedroidFile.write(path, str);
			return true;
		}
		catch (IOException e)
		{
			runJs("onException('IOException','" + URLEncoder.encode(e + "") + "')");
			return false;
		}
	}
	// 软件包
	@JavascriptInterface
	public String getPackageName()
	{
		return _context.getPackageName();
	}
	@JavascriptInterface
	public String getAppName() throws PackageManager.NameNotFoundException
	{
		PackageManager pm=_context.getPackageManager();
		ApplicationInfo ai=pm.getApplicationInfo(_context.getPackageName(), 0);
		return pm.getApplicationLabel(ai).toString();
	}
	@JavascriptInterface
	public String getAppName(String pkg) throws PackageManager.NameNotFoundException
	{
		PackageManager pm=_context.getPackageManager();
		ApplicationInfo ai=pm.getApplicationInfo(pkg, 0);
		return pm.getApplicationLabel(ai).toString();
	}
	@JavascriptInterface
	public String getVersionName() throws PackageManager.NameNotFoundException
	{
		PackageManager pm=_context.getPackageManager();
		PackageInfo pi=pm.getPackageInfo(_context.getPackageName(), 0);
		return pi.versionName;
	}
	@JavascriptInterface
	public String getVersionName(String pkg) throws PackageManager.NameNotFoundException
	{
		PackageManager pm=_context.getPackageManager();
		PackageInfo pi=pm.getPackageInfo(pkg, 0);
		return pi.versionName;
	}
	@JavascriptInterface
	public int getVersionCode() throws PackageManager.NameNotFoundException
	{
		PackageManager pm=_context.getPackageManager();
		PackageInfo pi=pm.getPackageInfo(_context.getPackageName(), 0);
		return pi.versionCode;
	}
	@JavascriptInterface
	public int getVersionCode(String pkg) throws PackageManager.NameNotFoundException
	{
		PackageManager pm=_context.getPackageManager();
		PackageInfo pi=pm.getPackageInfo(pkg, 0);
		return pi.versionCode;
	}
	@JavascriptInterface
	public boolean isInstalled(String pkg)
	{
		return Dedroid.isAppInstalled(_context, pkg);
	}
	@JavascriptInterface
	public boolean appSettings(String packageName)
	{
		try
		{
			Intent intent = new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
			Uri uri = Uri.fromParts("package", packageName, null);
			intent.setData(uri);
			_context.startActivity(intent);
			return true;
		}
		catch (Exception e)
		{
			return false;
		}
	}
	// 配置
	@JavascriptInterface
	public void putString(String key, String value)
	{
		DedroidConfig.putString(_context, "webdroid_localstorage", key, value);
	}
	@JavascriptInterface
	public void putInt(String key, int value)
	{
		SharedPreferences sp=_context.getSharedPreferences("webdroid_localstorage", Context.MODE_PRIVATE);
		SharedPreferences.Editor e=sp.edit();
		e.putInt(key, value);
		e.apply();
	}
	@JavascriptInterface
	public void putFloat(String key, Float value)
	{
		SharedPreferences sp=_context.getSharedPreferences("webdroid_localstorage", Context.MODE_PRIVATE);
		SharedPreferences.Editor e=sp.edit();
		e.putFloat(key, value);
		e.apply();
	}
	@JavascriptInterface
	public void putBoolean(String key, Boolean value)
	{
		SharedPreferences sp=_context.getSharedPreferences("webdroid_localstorage", Context.MODE_PRIVATE);
		SharedPreferences.Editor e=sp.edit();
		e.putBoolean(key, value);
		e.apply();
	}
	@JavascriptInterface
	public void putLong(String key, Long value)
	{
		SharedPreferences sp=_context.getSharedPreferences("webdroid_localstorage", Context.MODE_PRIVATE);
		SharedPreferences.Editor e=sp.edit();
		e.putLong(key, value);
		e.apply();
	}
	@JavascriptInterface
	public String getString(String key)
	{
		return DedroidConfig.getString(_context, "webdroid_localstorage", key);
	}
	@JavascriptInterface
	public int getInt(String key)
	{
		SharedPreferences sp=_context.getSharedPreferences("webdroid_localstorage", Context.MODE_PRIVATE);
		return sp.getInt(key, 0);
	}
	@JavascriptInterface
	public float getFloat(String key)
	{
		SharedPreferences sp=_context.getSharedPreferences("webdroid_localstorage", Context.MODE_PRIVATE);
		return sp.getFloat(key, 0);
	}
	@JavascriptInterface
	public boolean getBoolean(String key)
	{
		SharedPreferences sp=_context.getSharedPreferences("webdroid_localstorage", Context.MODE_PRIVATE);
		return sp.getBoolean(key, false);
	}
	@JavascriptInterface
	public long getLong(String key)
	{
		SharedPreferences sp=_context.getSharedPreferences("webdroid_localstorage", Context.MODE_PRIVATE);
		return sp.getLong(key, 0);
	}
	@JavascriptInterface
	public boolean isSpExists(String key)
	{
		SharedPreferences sp=_context.getSharedPreferences("webdroid_localstorage", Context.MODE_PRIVATE);
		return sp.contains(key);
	}
	// 系统
	@JavascriptInterface
	public void finish()
	{
		_activity.runOnUiThread(new Runnable(){
				@Override
				public void run()
				{
					_activity.finish();
				}
			});
	}
	@JavascriptInterface
	public void finishAndRemoveTask()
	{
		_activity.runOnUiThread(new Runnable(){
				@Override
				public void run()
				{
					_activity.finishAndRemoveTask();
				}
			});
	}
	@JavascriptInterface
	public void exit()
	{
		System.exit(0);
	}
	@JavascriptInterface
	public void exit(int status)
	{
		System.exit(status);
	}
	@JavascriptInterface
	public void startActivity(String packageName)
	{
		Dedroid.launchApp(_context, packageName);
	}
	@JavascriptInterface
	public boolean startActivity(String packageName, String activityName)
	{
		Intent i=new Intent();
		i.setComponent(new ComponentName(packageName, activityName));
		if (i.resolveActivity(_context.getPackageManager()) != null)
		{
			_context.startActivity(i);
			return true;
		}
		else
		{
			return false;
		}
	}
	@JavascriptInterface
	public void requestPermission(String permissionName)
	{
		Dedroid.requestPermission(_activity, permissionName);
	}
	@JavascriptInterface
	public boolean hasPermission(String permissionName)
	{
		return ContextCompat.checkSelfPermission(_context, permissionName) == PackageManager.PERMISSION_GRANTED;
	}
	@JavascriptInterface
	public boolean isVpnConnected()
	{
		ConnectivityManager connectivityManager = (ConnectivityManager) _context.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connectivityManager != null)
		{
			NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
			if (activeNetwork != null)
			{
				return activeNetwork.getType() == ConnectivityManager.TYPE_VPN;
			}
		}
		return false;
	}
	@JavascriptInterface
	public boolean isDarkMode()
	{
		if (Utils.getSdk() < 29) return false;
		UiModeManager uiModeManager = (UiModeManager) _context.getSystemService(Context.UI_MODE_SERVICE);
		if (uiModeManager != null)
		{
			return uiModeManager.getNightMode() == UiModeManager.MODE_NIGHT_YES;
		}	
		return false;
	}
	@JavascriptInterface
	public void regDarkModeChangeEvent(String callback)
	{
		darkChangeCalklback = callback;
	}
	@JavascriptInterface 
	public int getSdk()
	{
		return Utils.getSdk();
	}
	// 意图
	@JavascriptInterface
	public boolean hasExtra(String extraName)
	{
		Intent i=_activity.getIntent();
		return i.hasExtra(extraName);
	}
	@JavascriptInterface
	public String getStringExtra(String extraName)
	{
		Intent i=_activity.getIntent();
		return i.getStringExtra(extraName);
	}
	@JavascriptInterface
	public int getIntExtra(String extraName)
	{
		Intent i=_activity.getIntent();
		return i.getIntExtra(extraName, 0);
	}
	// 网络
	@JavascriptInterface
	public boolean isNetworkAvailable()
	{
		return DedroidNetwork.isNetworkAvailable(_context);
	}
	@JavascriptInterface
	public void regNetworkChangeEvent(final String callback)
	{
		new DedroidNetwork.onChange(_activity, new DedroidNetwork.onChangeCallback(){

				@Override
				public void onChange(boolean state)
				{
					_webview.evaluateJavascript(callback + "(" + state + ")", null);
				}


			}, true);
	}
	@JavascriptInterface
	public void downloadFile(String url, String localPath)
	{
		DedroidNetwork.download(url, localPath);
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
	@JavascriptInterface
	public void httpPost(final String requestUrl, final String data, final String callback, final int symbo)
	{
		new Thread(new Runnable(){
				@Override
				public void run()
				{
					try
					{
						URL url = new URL(requestUrl);
						HttpURLConnection connection = (HttpURLConnection) url.openConnection();
						connection.setRequestMethod("POST");
						connection.setDoOutput(true);
						connection.setDoInput(true);
						connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
						try (DataOutputStream wr = new DataOutputStream(connection.getOutputStream())) {
							wr.writeBytes(data);
							wr.flush();
						}
						int responseCode = connection.getResponseCode();
						if (responseCode == HttpURLConnection.HTTP_OK)
						{
							InputStream inputStream = connection.getInputStream();
							InputStreamReader inputStreamReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
							StringBuilder response = new StringBuilder();
							try (BufferedReader reader = new BufferedReader(inputStreamReader)) {
								String line;
								while ((line = reader.readLine()) != null)
								{
									response.append(line);
								}
								runJs(callback+"('"+Utils.removeLastNewline(response.toString())+"',"+responseCode+","+symbo+")");
							}
						}
						else
						{
							runJs(callback+"(false,"+responseCode+","+symbo+")");
						}
					}
					catch (MalformedURLException | IOException e)
					{
						runJs(callback+"(false,0,"+symbo+")");
						runJs("onException('MalformedURLException|IOException','"+URLEncoder.encode(e.toString())+"')");
					}
				}
		}).start();
	}
}
