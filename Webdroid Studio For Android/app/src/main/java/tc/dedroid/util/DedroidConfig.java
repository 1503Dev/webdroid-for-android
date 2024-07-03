package tc.dedroid.util;
import android.content.SharedPreferences;
import android.content.Context;

public class DedroidConfig {
    
    static public boolean putString(Context context, String name, String key, String value){
        SharedPreferences sharedPreferences = context.getSharedPreferences(name, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        return editor.putString(key, value).commit();
    }
    
    static public String getString(Context context, String name, String key, String defaultStr){
        SharedPreferences sharedPreferences = context.getSharedPreferences(name, Context.MODE_PRIVATE);
        return sharedPreferences.getString(key,defaultStr);
    }
    
    static public String getString(Context context, String name, String key){
        return getString(context,name,key,"");
    }
    
}
