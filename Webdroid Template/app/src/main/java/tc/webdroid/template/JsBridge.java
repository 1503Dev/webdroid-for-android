package tc.webdroid.template;
import android.webkit.*;
import tc.dedroid.util.*;
import android.content.*;
import android.app.*;
import android.widget.*;

public class JsBridge{
	static private Context _context;
	static private Activity _activity;
	static private int webdroidApiVersion=1;
	static public void setAttr(Context ctx,Activity act){
		_context=ctx;
		_activity=act;
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
	static public class webdroid{
		@JavascriptInterface
		public int getVersion(){
			return webdroidApiVersion;
		}
	}
}
