package tc.webdroid.template;
import android.os.*;
import android.graphics.*;
import java.io.*;
import android.webkit.*;

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
}
