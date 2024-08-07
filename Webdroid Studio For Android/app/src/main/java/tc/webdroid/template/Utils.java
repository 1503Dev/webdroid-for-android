package tc.webdroid.template;
import android.os.*;
import android.graphics.*;
import java.io.*;
import android.webkit.*;
import android.util.*;
import android.content.res.*;
import android.content.*;
import android.app.*;
import android.view.*;

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
	public static int hexToColor(String hexColor) {
		if (hexColor.startsWith("#")) {
			hexColor = hexColor.substring(1); // 移除井号 (#)
		}
		return Color.parseColor("#" + hexColor);
	}
	public static String getMimeType(File file) {

		String extension = getFileExtension(file.getName());

		if (extension != null) {

			MimeTypeMap mime = MimeTypeMap.getSingleton();

			return mime.getMimeTypeFromExtension(extension);

        }

		return null;

    }

	public static String getFileExtension(String fileName) {

		int dotIndex = fileName.lastIndexOf('.');

		if (dotIndex < 0) {

			return null;

        }

		return fileName.substring(dotIndex + 1);

    }
	public static float dp2px(float dp) {
		return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, Resources.getSystem().getDisplayMetrics());
	}
	public static float px2dp(Context context,float px) {

		DisplayMetrics metrics = context.getResources().getDisplayMetrics();
		float pxValue = px;
		float dpValue = pxValue / metrics.density;
		return dpValue;
	}
	public static void hideStatusBar(Activity act) {
        act.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }
    public static void hideNavBar(Activity act) {
        //隐藏虚拟按键，并且全屏
        if (Build.VERSION.SDK_INT > 11 && Build.VERSION.SDK_INT < 19) { // lower api
            View v = act.getWindow().getDecorView();
            v.setSystemUiVisibility(View.GONE);
        } else if (Build.VERSION.SDK_INT >= 19) {
            //for new api versions.
            View decorView = act.getWindow().getDecorView();
            int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
				| View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_FULLSCREEN;
            decorView.setSystemUiVisibility(uiOptions);
        }
    }
	public static void showStatusBar(Activity act) {
        act.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }
    public static void showNavBar(Activity act) {
        //隐藏虚拟按键，并且全屏
        if (Build.VERSION.SDK_INT > 11 && Build.VERSION.SDK_INT < 19) { // lower api
            View v = act.getWindow().getDecorView();
            v.setSystemUiVisibility(View.VISIBLE);
        } else if (Build.VERSION.SDK_INT >= 19) {
            //for new api versions.
            View decorView = act.getWindow().getDecorView();
            int uiOptions = View.SYSTEM_UI_FLAG_VISIBLE;
            decorView.setSystemUiVisibility(uiOptions);
        }
    }
	public static void transparentStatusBar(final Window window) {
		
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) return;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
			window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
		} else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
			window.addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
			window.clearFlags(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M ? WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS : 0);
			window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
														| View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
			window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
			window.setStatusBarColor(Color.TRANSPARENT);
		}
    }
	public static void backStatusBar(final Window window) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) return;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
			window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
			window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
			window.clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
			int newOption = View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
			int vis = window.getDecorView().getSystemUiVisibility();
			vis &= ~View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;
			window.getDecorView().setSystemUiVisibility(newOption | vis);
			window.setStatusBarColor(JsBridge.statusBarColor);
			
        } else {
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }
}
