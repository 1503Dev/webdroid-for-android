package tc.dedroid.util;
import android.content.pm.PackageManager;
import android.content.Intent;
import android.content.Context;
import android.text.TextUtils;
import android.content.DialogInterface;
import android.webkit.WebView;
import android.webkit.JavascriptInterface;
import android.widget.EditText;
import android.app.Activity;
import android.net.Uri;
import java.util.UUID;
import android.os.Build;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.util.List;
import java.util.ArrayList;
import android.graphics.Rect;
import android.view.Window;

public class Dedroid {

    final private static int UtilVersion=9;

    static public int getVersion() {
        return UtilVersion;
    }
    static public void launchApp(Context ctx, String pkg) {
        PackageManager packageManager = ctx.getPackageManager();
        Intent it = packageManager.getLaunchIntentForPackage(pkg);
        ctx.startActivity(it);
    }
    public static boolean isAppInstalled(Context context, String pkgName) {
        if (TextUtils.isEmpty(pkgName)) {
            return false;
        }
        try {
            context.getPackageManager().getPackageInfo(pkgName, 0);
        } catch (Exception x) {
            return false;
        }
        return true;
    }
    static public String strApi(Context ctx, String oldStr) {
        long unix=System.currentTimeMillis();
        /*Drawable iconDrawable=null;
         // 获取Drawable类型的图标资源
         try {
         iconDrawable = ctx.getPackageManager().getApplicationIcon(ctx.getPackageName());
         } catch (PackageManager.NameNotFoundException e) {}

         // 将Drawable转换为Bitmap
         Bitmap iconBitmap = ((BitmapDrawable) iconDrawable).getBitmap();

         // 将Bitmap转换为字节数组
         ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
         iconBitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
         byte[] iconBytes = byteArrayOutputStream.toByteArray();

         // 将字节数组编码为Base64字符串
         String base64Icon = Base64.encodeToString(iconBytes, Base64.DEFAULT);
         */
        String newStr=oldStr;
        try {
            newStr = newStr.replace("{device.id}", getFeatureUUID())
                .replace("{device.sdk}", "" + Build.VERSION.SDK_INT)
                .replace("{util.ver}", "" + Dedroid.UtilVersion)
                .replace("{app.package}", ctx.getPackageName())
                .replace("{app.name}", ctx.getPackageManager().getApplicationLabel(ctx.getPackageManager().getApplicationInfo(ctx.getPackageName(), 0)))
                .replace("{app.vername}", ctx.getPackageManager().getPackageInfo(ctx.getPackageName(), 0).versionName)
                .replace("{app.ver}", "" + ctx.getPackageManager().getPackageInfo(ctx.getPackageName(), 0).versionCode)
                //.replace("{app.icon}",base64Icon)
                .replace("{time.unix}", "" + unix);
            Pattern pattern = Pattern.compile("\\{is_app_installed/(.*?)\\}");
            Matcher matcher = pattern.matcher(newStr);
            String packageName;
            if (matcher.find()) {
                packageName = matcher.group(1);

                // 调用方法并获取结果
                boolean isInstalled = Dedroid.isAppInstalled(ctx, packageName);

                // 根据结果替换字符串
                String replacement = isInstalled ? "true" : "false";
                newStr = newStr.replace(matcher.group(), replacement);
            }
            newStr = newStr.replace("{}", "{");
        } catch (PackageManager.NameNotFoundException e) {}
        return newStr;
    }
    private static String getFeatureUUID() {
        String serial = null;
        String m_szDevIDShort = "35" +
            Build.BOARD.length() % 10 + Build.BRAND.length() % 10 +
            Build.CPU_ABI.length() % 10 + Build.DEVICE.length() % 10 +
            Build.DISPLAY.length() % 10 + Build.HOST.length() % 10 +
            Build.ID.length() % 10 + Build.MANUFACTURER.length() % 10 +
            Build.MODEL.length() % 10 + Build.PRODUCT.length() % 10 +
            Build.TAGS.length() % 10 + Build.TYPE.length() % 10 +
            Build.USER.length() % 10; //13 位
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                serial = android.os.Build.getSerial();
            } else {
                serial = Build.SERIAL;
            }
            //API>=9 使用serial号
            return new UUID(m_szDevIDShort.hashCode(), serial.hashCode()).toString();
        } catch (Exception exception) {
            //serial需要一个初始化
            serial = "serial"; // 随便一个初始化
        }
        //使用硬件信息拼凑出来的15位号码
        return new UUID(m_szDevIDShort.hashCode(), serial.hashCode()).toString();
    }
    public static void startActivity(Context ctx, String name) throws ClassNotFoundException {
        Intent intent = new Intent(ctx, Class.forName(name));
        ctx.startActivity(intent);
    }
    public static void startActivity(Context ctx, Class<?> cls) {
        Intent intent = new Intent(ctx, cls);
        ctx.startActivity(intent);
    }
    public static void requestPermission(Activity activity, String[] permissions) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            List<String> permissionsToRequest = new ArrayList<>();

            for (String permission : permissions) {
                if (activity.checkSelfPermission(permission)
                    != PackageManager.PERMISSION_GRANTED) {
                    permissionsToRequest.add(permission);
                }
            }

            if (!permissionsToRequest.isEmpty()) {
                activity.requestPermissions(
                    permissionsToRequest.toArray(new String[0]),
                    1503);
            }
        }
    }
    public static void requestPermission(Activity activity, String permission) {
        requestPermission(activity,new String[]{permission});
    }
    public static int getStatusBarHeight(Context context) {
        Rect rectangle = new Rect();
        Window window = ((Activity) context).getWindow();
        window.getDecorView().getWindowVisibleDisplayFrame(rectangle);
        return rectangle.top;
    }
}
