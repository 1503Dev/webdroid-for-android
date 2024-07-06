package tc.webdroid.template;
import android.os.*;

public class Utils{
	public static String removeLastNewline(String str) {
		if (str != null && str.endsWith("\n")) {
			return str.substring(0, str.length() - 1);
		}
		return str;
	}
	public static int getSdk(){
		return Build.VERSION.SDK_INT;
	}
}
