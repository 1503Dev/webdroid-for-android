package tc.webdroid.template;
import android.webkit.*;
import tc.dedroid.util.*;
import android.content.*;
import android.app.*;
import android.widget.*;
import java.net.*;

public class JsBridge{
	static private Context _context;
	static private Activity _activity;
	static private WebView _webview;
	static private int webdroidApiVersion=2;
	static public void setAttr(Context ctx,Activity act,WebView wv){
		_context=ctx;
		_activity=act;
		_webview=wv;
	}
	static public class webdroid{
		@JavascriptInterface
		public int getVersion(){
			return webdroidApiVersion;
		}
	}
	static public class webdroid_toast{
		@JavascriptInterface
		public void make(String text){
			DedroidToast.toast(_activity,text);
		}
		@JavascriptInterface
		public void makeLong(String text){
			DedroidToast.toast(_activity,text,android.widget.Toast.LENGTH_LONG);
		}
	}
	static public class webdroid_dialog{
		@JavascriptInterface
		public void alert(String title,String content,boolean cancelable){
			DedroidDialog.alert(_activity,title,content,cancelable);
		}
		@JavascriptInterface
		public void alert(String title,String content){
			DedroidDialog.alert(_activity,title,content,true);
		}
		@JavascriptInterface
		public void alert(String content,boolean cancelable){
			DedroidDialog.alert(_activity,content,cancelable);
		}
		@JavascriptInterface
		public void alert(String content){
			DedroidDialog.alert(_activity,content,true);
		}
	}
	static public class webdroid_http{
		@JavascriptInterface
		public void get(String url,final String callback,final int symbo){
			DedroidNetwork.get(url, new DedroidNetwork.HttpCallback(){

					@Override
					public void onResponse(String responseString, int httpCode)
					{
						_webview.loadUrl("javascript:"+
						    callback+
							"('"+
							URLEncoder.encode(responseString)+
							"',"+
							httpCode+
							","+
							symbo+
							")");
					}

					@Override
					public void onFailure(Exception e)
					{
					    _webview.loadUrl("javascript:"+callback+"(false,0,"+symbo+")");
					}
					
				
			});
		}
	}
}
