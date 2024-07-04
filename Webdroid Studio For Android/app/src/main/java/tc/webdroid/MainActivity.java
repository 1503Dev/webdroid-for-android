package tc.webdroid;

import android.app.Activity;
import android.os.Bundle;
import android.app.AlertDialog;
import android.content.DialogInterface;
import com.apk.axml.aXMLDecoder;
import java.io.FileInputStream;
import java.io.IOException;
import org.xmlpull.v1.XmlPullParserException;
import com.apk.axml.*;
import android.view.View;
import tc.dedroid.util.DedroidFile;
import tc.dedroid.util.DedroidDialog;
import tc.dedroid.util.*;
import android.content.*;
import android.*;
import java.util.*;
import org.json.*;
import java.io.*;
import android.os.Handler;
import android.os.Looper;
import android.net.wifi.aware.*;
import android.widget.*;
import android.content.res.*;

public class MainActivity extends Activity {
	
	private Activity mc;
	private View dialog_creator_view;
	private AlertDialog dialog_creator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mc=this;
		Dedroid.requestPermission(mc,Manifest.permission.WRITE_EXTERNAL_STORAGE);
    }
    public void about(View v){
        DedroidDialog.HTMLAlert(this,getString(R.string.s_about),"Source Code<br><a href='https://github.com/1503Dev/webdroid-for-android/'>GITHUB</a><br>"+getString(R.string.s_open_source)+"<br><a href='https://github.com/TheChuan1503/DedroidUtil/'>DedroidUtil</a> <a href='https://github.com/AbdurazaaqMohammed/AXML-Editor/'>AXML-Editor</a>",true);
    }
	public void items(final View v){
		DedroidFile.mkdir(DedroidFile.EXTERN_STO_PATH+"/1503Dev/WebProjects");
		final String[] projects=DedroidFile.listName(DedroidFile.EXTERN_STO_PATH+"/1503Dev/WebProjects/").toArray(new String[0]);
		if(projects.length==0){
			DedroidToast.toast(mc,"无项目，请查看"+getString(R.string.s_explanation));
			return;
		}
		DedroidDialog.list(mc,"选择要打包的项目", true, projects,-1, new DedroidDialog.SelectCallback(){

				@Override
				public void onSelect(DialogInterface dialog, int Which)
				{
                    DedroidToast.toast(mc,"正在打包项目");
					pack(v,projects[Which]);
				}
				
			
		});
	}
	public void requestPermission(View v){
		Dedroid.requestPermission(mc,Manifest.permission.WRITE_EXTERNAL_STORAGE);
	}
	public void explanation(View v){
		new DedroidWeb.JsBridge(this).jumpUrl("https://github.com/1503Dev/webdroid-for-android/");
	}
    public void create(View v){
        AlertDialog.Builder dialog = DedroidDialog.emptyDialog(mc);
		dialog_creator_view=getLayoutInflater().inflate(R.layout.dialog_create,null);
		dialog.setView(dialog_creator_view);
		dialog_creator=dialog.show();
    }
    public void creator_create(View v) throws JSONException, IOException{
		String rootPath=DedroidFile.EXTERN_STO_PATH+"/1503Dev/WebProjects";
		String project_name=((TextView)dialog_creator_view.findViewById(R.id.creator_project_name)).getText().toString();
		String app_name=((TextView)dialog_creator_view.findViewById(R.id.creator_app_name)).getText().toString();
		String package_name=((TextView)dialog_creator_view.findViewById(R.id.creator_package)).getText().toString();
		String projectPath=DedroidFile.EXTERN_STO_PATH+"/1503Dev/WebProjects/"+project_name;
		if(project_name.equals("")){
			DedroidToast.toast(mc,"项目名不能为空");
			return;
		}
		if(app_name.equals("")){
			DedroidToast.toast(mc,"应用名不能为空");
			return;
		}
		if(package_name.equals("")){
			DedroidToast.toast(mc,"包名不能为空");
			return;
		}
		if(package_name.indexOf(".")==-1){
			DedroidToast.toast(mc,"包名必须有一个.");
			return;
		}
		if(DedroidFile.exists(rootPath+"/"+project_name)){
			DedroidToast.toast(mc,"项目已存在");
			return;
		}
		JSONObject manifestRoot=new JSONObject(Utils.readAssetsFile(getAssets(),"project_core/WebdroidManifest.json"));
		JSONObject manifest=manifestRoot.getJSONObject("manifest");
		manifest.put("application_name",app_name);
		manifest.put("package",package_name);
		manifestRoot.put("manifest",manifest);
		DedroidFile.write(projectPath+"/WebdroidManifest.json",manifestRoot.toString());
		DedroidFile.mkdir(projectPath+"/html");
		Utils.copyAssetToExternalStorage(mc,"project_core/html/index.html",projectPath+"/html/index.html");
		Utils.copyAssetToExternalStorage(mc, "app_core/res/drawable/ic_launcher.png", projectPath+"/icon.png");
		DedroidToast.toast(mc,"创建成功");
		DedroidDialog.alert(mc,"创建成功","项目已在\n"+projectPath+"\n创建",true);
		dialog_creator.cancel();
	}
	public void pack(final View v,final String projectName){
		new Handler(Looper.getMainLooper()).postDelayed(new Runnable(){

                @Override
                public void run() {

                    v.setEnabled(false);
                    String rootPath=DedroidFile.EXTERN_STO_PATH+"/1503Dev/.WebdroidAppCoreTemp";
                    String projectPath=DedroidFile.EXTERN_STO_PATH+"/1503Dev/WebProjects/"+projectName;
                    
                    DedroidFile.del(rootPath);
                    DedroidFile.mkdir(rootPath+"/META-INF");
                    DedroidFile.mkdir(rootPath+"/res/drawable");
                    try
                    {
                        Utils.copyAssetToExternalStorage(mc, "app_core/AndroidManifest.xml", rootPath+"/AndroidManifest.xml");
                        Utils.copyAssetToExternalStorage(mc, "app_core/classes.dex", rootPath+"/classes.dex");
                        Utils.copyAssetToExternalStorage(mc, "app_core/resources.arsc", rootPath+"/resources.arsc");
                        Utils.copyAssetToExternalStorage(mc, "app_core/res/drawable/ic_launcher.png", rootPath+"/res/drawable/ic_launcher.png");
                        Utils.copyAssetToExternalStorage(mc, "app_core/META-INF/ANDROID.RSA", rootPath+"/META-INF/ANDROID.RSA");
                        Utils.copyAssetToExternalStorage(mc, "app_core/META-INF/ANDROID.SF", rootPath+"/META-INF/ANDROID.SF");
                        Utils.copyAssetToExternalStorage(mc, "app_core/META-INF/MANIFEST.MF", rootPath+"/META-INF/MANIFEST.MF");
                    }
                    catch (IOException e)
                    {
                        v.setEnabled(true);
                        DedroidToast.toast(mc,"内核加载失败，请检查是否拥有权限");
                        return;
                    }
                    try
                    {
                        JSONObject project_manifest=new JSONObject(DedroidFile.read(projectPath + "/WebdroidManifest.json")).getJSONObject("manifest");
                        //DedroidToast.toast(this,"正在打包项目"+project_manifest.getString("package"));
                        String manifestXml=DedroidFile.read(rootPath+"/AndroidManifest.xml");
						manifestXml=manifestXml.replace("<LauncherName>",project_manifest.getString("launcher_name"));
                        manifestXml=manifestXml.replace("<AppName>",project_manifest.getString("application_name"));
                        manifestXml=manifestXml.replace("<VersionName>",project_manifest.getString("version_name"));
                        manifestXml=manifestXml.replace("<VersionCode>",""+project_manifest.getInt("version_code"));
                        manifestXml=manifestXml.replace("<Package>",project_manifest.getString("package"));
                        Utils.copyDirectory(new File(projectPath+"/html/"),new File(rootPath+"/assets/"));
                        try
                        {
                            DedroidFile.write(rootPath+"/AndroidManifest.xml",new aXMLEncoder().encodeString(mc, manifestXml));
							DedroidFile.copy(projectPath+"/icon.png",rootPath+"/res/drawable/ic_launcher.png");
                            Utils.zipFolderWithoutRoot(rootPath,projectPath+"/app.apk");
                            DedroidFile.del(rootPath);
                            DedroidToast.toast(mc,"项目打包成功，请自行签名");
                            DedroidDialog.alert(mc,"成功","文件已保存在\n"+projectPath+"/app.apk\n请自行签名",true);
                            v.setEnabled(true);
                        }
                        catch (IOException e)
                        {
                            v.setEnabled(true);
                            DedroidToast.toast(mc,"项目打包出错，但打包未中断");
                        }
                        catch (XmlPullParserException e)
                        {
                            v.setEnabled(true);
                            DedroidToast.toast(mc,"项目打包失败，AndroidManifest.xml编译错误");
                        }
                    }
                    catch (JSONException e)
                    {
                        v.setEnabled(true);
                        DedroidToast.toast(mc,"项目读取失败，请检查WebdroidManifest.json");
                    }
                    catch (IOException e)
                    {
                        v.setEnabled(true);
                        DedroidToast.toast(mc,"项目读取失败，请检查是否拥有权限");
                    }
                }
            }, 1000);
		
	}
}
