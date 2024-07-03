package tc.dedroid.util;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import java.io.IOException;
import org.json.JSONArray;
import org.json.JSONException;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.PackageManager;
import org.json.JSONObject;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;

public class DedroidWeb {
    static public class WebPage {
        private String defaultUrl;
        private String url;
        private WebView webView;
        private Activity _activity;
        public WebPage(Activity act, String url) {
            webPage(act, url);
        }
        public WebPage(Activity act, String url, int layoutResId) {
            if (DedroidNetwork.isNetworkAvailable(act)) {
                webPage(act, url);
            } else {
                act.setContentView(layoutResId);
            }
        }
        public WebPage(Activity act, String url, View view) {
            if (DedroidNetwork.isNetworkAvailable(act)) {
                webPage(act, url);
            } else {
                act.setContentView(view);
            }
        }
        public WebPage(Activity act, String url, String localHtml) {
            if (DedroidNetwork.isNetworkAvailable(act)) {
                webPage(act, url);
            } else {
                webPage(act, localHtml);
            }
        }
        public WebPage(Activity act, String url, String localHtml, String fileName) {
            boolean t=!DedroidFile.exists(fileName);

            if (DedroidNetwork.isNetworkAvailable(act) && t) {
                webPage(act, url);
            } else {
                webPage(act, localHtml);
            }
        }
        private void webPage(final Activity act, String url) {
            _activity = act;
            defaultUrl = Dedroid.strApi(_activity, url);
            this.url = defaultUrl;
            this.webView = new WebView(act);
            webView.setWebViewClient(new WebViewClient() {
                    @Override
                    public boolean shouldOverrideUrlLoading(WebView view, String url) {
                        if (url.substring(0, 10).equals("dedroid://")) {
                            String s=url.substring(10);
                            if (s.substring(0, 13).equals("networkDialog")) {
                                DedroidDialog.networkDialog(act, act, s.substring(14));
                                return true;
                            }
                            if (s.substring(0, 6).equals("finish")) {
                                act.finishAndRemoveTask();
                                return true;
                            }
                            return true;
                        }

                        view.loadUrl(url);
                        return true;
                    }
                });
            webView.getSettings().setJavaScriptEnabled(true);
            webView.getSettings().setDomStorageEnabled(true);
            JsBridge jsb=new DedroidWeb.JsBridge(act);
            JsBridge.Plugin jspb=new JsBridge.Plugin(act.getApplicationContext(), act);
            jsb.setWebView(webView);
            webView.addJavascriptInterface(jsb, "Dedroid");
            webView.addJavascriptInterface(jspb, "Plugin");
            webView.loadUrl(defaultUrl);
            act.setContentView(webView);
        }
        public WebView getWebView() {
            return this.webView;
        }
        public void setWebView(WebView v) {
            this.webView = v;
            _activity.setContentView(webView);
        }
        public void goBack() {
            webView.goBack();
        }
    }
    static public class JsBridge {
        private static DialogInterface dialog;
        private static Context _context;
        private static Activity _activity;
        private static WebView webView;
        public void setDialog(Dialog dia) {
            dialog = dia;
        }
        public void setActivity(Activity act) {
            _activity = act;
        }
        public void setWebView(WebView wv) {
            webView = wv;
        }
        public JsBridge(Context ctx) {
            _context = ctx;
        }
        @JavascriptInterface
        public void toast(String msg) {
            DedroidToast.toast(_context, msg);
        }
        @JavascriptInterface
        public void alert(String msg, String title) {
            DedroidDialog.alert(_context, msg, title, false);
        }
        @JavascriptInterface
        public void alert(String msg) {
            DedroidDialog.alert(_context, msg, false);
        }
        @JavascriptInterface
        public void prompt(final int symbo, final String title, final String content, final String callback, final String defaultText) {
            Handler handler = new Handler(Looper.getMainLooper());
            handler.post(new Runnable() {
                    @Override
                    public void run() {
                        DedroidDialog.prompt(_context, title, content, false, new DedroidDialog.PromptCallback(){

                                @Override
                                public void onClick(EditText et) {
                                    webView.loadUrl("javascript:" + callback + "(" + symbo + ",true,\"" + et.getText().toString().replace("\"","\\\"") + "\")");
                                }


                            }, new DedroidDialog.PromptCallback(){

                                @Override
                                public void onClick(EditText et) {
                                    webView.loadUrl("javascript:" + callback + "(" + symbo + ",false,\"" + et.getText().toString().replace("\"","\\\"")  + "\")");
                                }


                            }, defaultText);
                    }
                });

        }
        @JavascriptInterface
        public void get(final int symbo, final String url, final String callback) {
            Handler handler = new Handler(Looper.getMainLooper());
            handler.post(new Runnable() {
                    @Override
                    public void run() {

                        DedroidNetwork.get(url, new DedroidNetwork.HttpCallback(){

                                @Override
                                public void onResponse(String responseString, int httpCode) {
                                    DedroidToast.toast(_context, responseString); 
                                }

                                @Override
                                public void onFailure(Exception e) {
                                    DedroidToast.toast(_context, "失败");

                                }


                            });

                    }});
        }
        @JavascriptInterface
        public void prompt(int symbo, String title, final String callback) {
            prompt(symbo, title, null, callback, "");
        }
        @JavascriptInterface
        public void prompt(int symbo, String title, final String callback, String defaultText) {
            prompt(symbo, title, null, callback, defaultText);
        }
        @JavascriptInterface
        public void networkDialog(final String url) {

            DedroidDialog.networkDialog(_context, _activity, url);

        }
        @JavascriptInterface
        public boolean putString(String name, String key, String value) {
            return DedroidConfig.putString(_context, name, key, value);
        }
        @JavascriptInterface
        public String getString(String name, String key) {
            return DedroidConfig.getString(_context, name, key);
        }
        @JavascriptInterface
        public void closeDialog() {
            dialog.cancel();
        }
        @JavascriptInterface
        public void jumpUrl(String url) {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            _context.startActivity(intent);
        }
        @JavascriptInterface
        public boolean isAppInstalled(String pkg) {
            return Dedroid.isAppInstalled(_context, pkg);
        }
        @JavascriptInterface
        public String getApps() {
            return new JSONArray(DedroidPackage.getApps(_context,false)).toString();
        }
        @JavascriptInterface
        public String getAppInfo(String pkg) throws JSONException, PackageManager.NameNotFoundException{
            JSONObject json=new JSONObject();
            PackageManager pm=_context.getPackageManager();
            PackageInfo pi=DedroidPackage.getAppInfo(_context,pkg);
            ApplicationInfo ai=pm.getApplicationInfo(pi.packageName,0);
            json.put("name",ai.loadLabel(pm));
            json.put("version_name",pi.versionName);
            json.put("version_code",pi.versionCode);
            return json.toString();
        }
        @JavascriptInterface
        public void launchApp(String pkg) {
            Dedroid.launchApp(_context, pkg);
        }
        @JavascriptInterface
        public void startActivity(String name) throws ClassNotFoundException {
            Intent intent = new Intent(_context, Class.forName(name));
            _context.startActivity(intent);
        }
        @JavascriptInterface
        public void loadUrl(final String url) {
            Handler handler = new Handler(Looper.getMainLooper());
            handler.post(new Runnable() {
                    @Override
                    public void run() {
                        webView.loadUrl(url);
                    }});

        }
        @JavascriptInterface
        public void goBack() {
            Handler handler = new Handler(Looper.getMainLooper());
            handler.post(new Runnable() {
                    @Override
                    public void run() {
                        webView.goBack();
                    }});
        }
        @JavascriptInterface
        public String strApi(String str) {
            return Dedroid.strApi(_context, str);
        }
        @JavascriptInterface
        public void mkdir(String str) {
            DedroidFile.mkdir(str);
        }
        @JavascriptInterface
        public void mkfile(String str, String defc) {
            DedroidFile.mkfile(str, defc);
        }
        @JavascriptInterface
        public String readFile(String str) throws IOException {
            return DedroidFile.read(str);
        }
        @JavascriptInterface
        public void delFile(String str) {
            DedroidFile.del(str);
        }
        @JavascriptInterface
        public void copyFile(String str1, String str2) {
            DedroidFile.copy(str1, str2);
        }
        @JavascriptInterface
        public void writeFile(String str, String cont) throws IOException {
            DedroidFile.write(str, cont);
        }
        @JavascriptInterface
        public String getLargestFile(String dir) {
            return DedroidFile.getLargestFile(dir);
        }
        @JavascriptInterface
        public int getStatusBarHeight() {
            return Dedroid.getStatusBarHeight(_context);
        }
        @JavascriptInterface
        public String listFile(String dir) {
            return new JSONArray(DedroidFile.listName(dir)).toString();
        }
        @JavascriptInterface
        public void finish(final boolean i) {
            if (i) {
                _activity.finishAndRemoveTask();
            } else {
                _activity.finish();
            }
        }
        static public class Plugin {
            private static Context _context;
            private static Activity _activity;
            private static WebView webView;
            public void setWebView(WebView wv) {
                webView = wv;
            }
            public Plugin(Context ctx, Activity act) {
                _context = ctx;
                _activity = act;
            }
            @JavascriptInterface
            public void install(String url) {
                DedroidPlugin.install(url);
            }
            @JavascriptInterface
            public void unInstall(String id) {
                DedroidPlugin.unInstall(id);
            }
            @JavascriptInterface
            public String list() {
                return new JSONArray(DedroidPlugin.list()).toString();
            }
            @JavascriptInterface
            public String listAll() {
                return new JSONArray(DedroidPlugin.listAll()).toString();
            }
            @JavascriptInterface
            public String getInfo(String id) throws IOException, JSONException {
                return DedroidPlugin.getInfo(id).toString();
            }
            @JavascriptInterface
            public boolean isInstalled(String id) {
                return DedroidPlugin.isInstalled(id);
            }
        }
    }
}
