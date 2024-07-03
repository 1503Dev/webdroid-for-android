package tc.dedroid.util;
import java.util.ArrayList;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.pm.PackageInfo;
import java.util.List;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager.NameNotFoundException;

public class DedroidPackage {
    
    public static final String TAG = "DedroidPackage";
    
    public static ArrayList<String> getApps(Context ctx,boolean isFilterSystem) {
        ArrayList<String> appBeanList = new ArrayList<>();
        PackageManager packageManager = ctx.getPackageManager();
        List<PackageInfo> list = packageManager.getInstalledPackages(0);
        for (PackageInfo p : list) {
            int flags = p.applicationInfo.flags;
            // 判断是否是属于系统的apk
            if ((flags & ApplicationInfo.FLAG_SYSTEM) != 0&&isFilterSystem) {
//                bean.setSystem(true);
            } else {
                appBeanList.add(p.packageName);
            }
        }
        return appBeanList;
    }
    public static PackageInfo getAppInfo(Context c,String pkg) throws PackageManager.NameNotFoundException{
        PackageManager pm = c.getPackageManager();
        return pm.getPackageInfo(pkg,0);
    }
    
}
