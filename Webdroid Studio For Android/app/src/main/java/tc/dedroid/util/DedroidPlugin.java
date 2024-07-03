package tc.dedroid.util;
import android.content.Context;
import org.json.JSONObject;
import java.io.IOException;
import org.json.JSONException;
import java.lang.reflect.Array;
import java.io.File;
import java.util.List;
import java.util.ArrayList;
import dalvik.system.DexClassLoader;
import java.lang.reflect.Method;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Constructor;
import android.app.Activity;
import android.util.Log;

public class DedroidPlugin {
    
    private static Context context;
    private static String path;
    
    public DedroidPlugin(Context ctx){
        this.context=ctx;
        this.path=ctx.getFilesDir().toString()+"/plugins/";
        DedroidFile.mkdir(path);
    }
    public static boolean isInstalled(String pluginId){
        return DedroidFile.exists(path+"/"+pluginId+"/manifest.json");
    }
    public static List<String> list(){
        List<String> r=new ArrayList<>();
        File[] d=DedroidFile.list(path);
        for(int i=0;i<d.length;i++){
            if(isInstalled(d[i].getName())){
                r.add(d[i].getName());
            }
        }
        return r;
    }
    public static List<String> listAll(){
        List<String> r=new ArrayList<>();
        File[] d=DedroidFile.list(path);
        for(int i=0;i<d.length;i++){
                r.add(d[i].getName());
        }
        return r;
    }
    public static JSONObject getInfo(String pluginId) throws IOException, JSONException{
        if (isInstalled(pluginId)){
            return new JSONObject(DedroidFile.read(path+"/"+pluginId+"/manifest.json"));
        }
        return null;
    }
    public static void install(final String url){
        DedroidNetwork.get(url+"/manifest.json", new DedroidNetwork.HttpCallback(){

                @Override
                public void onResponse(String responseString, int httpCode) {
                    if(httpCode==200){
                        try {
                            final JSONObject data=new JSONObject(responseString);
                            DedroidFile.mkdir(path + "/" + data.getString("id") + "/classes");
                            //DedroidFile.mkdir(path + "/" + data.getString("id") + "/classes_cache");
                            try {
                                DedroidFile.write(path + "/" + data.getString("id") + "/manifest.json", responseString);

                            } catch (IOException e) {} catch (JSONException e) {}
                            DedroidNetwork.get(url + "/classes.dex", new DedroidNetwork.HttpByteCallback(){

                                    @Override
                                    public void onResponse(byte[] b, int httpCode) {
                                        if(httpCode==200){
                                            try {
                                                DedroidFile.write(path + "/" + data.getString("id") + "/classes.dex", b);
                                            } catch (IOException e) {} catch (JSONException e) {}
                                        }
                                    }

                                    @Override
                                    public void onFailure(Exception e) {
                                    }
                                    
                                
                            });
                        } catch (JSONException e) {}
                    }
                }

                @Override
                public void onFailure(Exception e) {
                }
                
            
        });
    }
    public static void unInstall(String id){
        DedroidFile.del(path+"/"+id+"/manifest.json");
    }
    public static void load(Context ctx,Activity act,String met) throws IOException, JSONException, IllegalArgumentException, SecurityException {
        List<String> list=list();
        for(int i=0;i<list.size();i++){
            JSONObject info=getInfo(list.get(i));
            try {
                // 创建DexClassLoader实例
                DexClassLoader dexClassLoader = new DexClassLoader(
                    path+"/"+list.get(i)+"/classes.dex", // DEX文件的路径
                    context.getCacheDir().getAbsolutePath(), // 解压后的DEX存放路径
                    null, // 父类加载器
                    context.getClassLoader() // 当前类加载器
                );

                // 加载DEX中的类
                Class<?> clazz = dexClassLoader.loadClass(info.getString("class")); // 替换为你的类名

                // 获取并调用该类的main方法
                Method method = clazz.getMethod(met, Context.class, Activity.class);
                method.invoke(null, ctx, act); // 调用方法，传递参数

            } catch (Exception e) {
                Log.e("", "Failed to load and invoke plugin", e);
            }
        }
    }
    private void loadDebug(Context context, Activity activity) {
        try {
            // 创建DexClassLoader实例
            DexClassLoader dexClassLoader = new DexClassLoader(
                "/storage/emulated/0/2/plugins/ex/classes/classes.dex", // DEX文件的路径
                context.getCacheDir().getAbsolutePath(), // 解压后的DEX存放路径
                null, // 父类加载器
                context.getClassLoader() // 当前类加载器
            );

            // 加载DEX中的类
            Class<?> clazz = dexClassLoader.loadClass("tc.ex.main"); // 替换为你的类名

            // 获取并调用该类的main方法
            Method method = clazz.getMethod("main", Context.class, Activity.class);
            method.invoke(null, context, activity); // 调用方法，传递参数

        } catch (Exception e) {
            Log.e("", "Failed to load and invoke plugin", e);
        }
    }
}
