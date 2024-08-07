package tc.dedroid.util;
import android.content.Context;
import android.widget.Toast;

public class DedroidToast {
    
    static public void toast(Context context, String message, int length){
        Toast.makeText(context, message, length).show();
    }
    static public void toast(Context context, String message){
        toast(context, message, Toast.LENGTH_SHORT);
    }
    
}
