package tc.webdroid;

import android.app.Activity;
import android.os.Bundle;

public class SelfFinishActivity extends Activity {
    
    public static final String TAG = "SelfFinishActivity";
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        finish();
    }
    
}
