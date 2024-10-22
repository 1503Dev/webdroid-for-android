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
import android.webkit.*;
import android.os.*;
import android.graphics.*;

public class MainActivity extends Activity {
	
	private static Activity mc;
	private static View dialog_creator_view;
	private static AlertDialog dialog_creator;
	private static int creator_code=0;
	private static boolean creator_isMy=false;
	private static String[] templates={
		"template_no_activity",
		"template_default_contents_activity",
	    "template_empty_activity",
		"template_basic_activity",
		"template_blank_activity_drawer",
		"template_bottom_navigation_activity",
		"template_blank_activity_tabs"
	};
	private WebView wv;
	private long exitTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mc=this;
		Dedroid.requestPermission(mc,Manifest.permission.WRITE_EXTERNAL_STORAGE);
		String indexUrl=DedroidConfig.getString(this,"configs","index_url");
		if(indexUrl.equals("")){
			indexUrl="file:///android_asset/local_html/index.html";
		}
		DedroidWeb.WebPage wp=new DedroidWeb.WebPage(this,indexUrl);
		wv=wp.getWebView();
		JsBridge jsb=new JsBridge();
		jsb.setAttr(this,this,wv);
		wv.addJavascriptInterface(jsb,"wds");
		tc.webdroid.template.JsBridge jsb2=new tc.webdroid.template.JsBridge();
		jsb2.setAttr(this,this,wv);
		wv.addJavascriptInterface(jsb2,"wd");
		WebSettings settings = wv.getSettings();
        settings.setUseWideViewPort(true);//设定支持viewport
        settings.setLoadWithOverviewMode(true);   //自适应屏幕
        settings.setBuiltInZoomControls(true);
        settings.setDisplayZoomControls(false);
        settings.setSupportZoom(true);//设定支持缩放
        // 启用JavaScript
        settings.setJavaScriptEnabled(true);
        // 允许从文件加载
        settings.setAllowFileAccessFromFileURLs(true);
        settings.setAllowUniversalAccessFromFileURLs(true);
        settings.setDomStorageEnabled(true);
		Utils.sign(this);
    }
	@Override
    public void onBackPressed() {
        if (wv.canGoBack()) {
            wv.goBack();
        } else {
            if ((System.currentTimeMillis() - exitTime) > 2000) {
                Toast.makeText(getApplicationContext(), "再按一次退出",
                               Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
                super.onBackPressed();
            }

        }
    }
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		return;
	}
	public static class JsBridge{
		private Context _context;
		private Activity _activity;
	    private WebView _webview;
		static private String projectPath=DedroidFile.EXTERN_STO_PATH+"/1503Dev/WebProjects/";

		public void setAttr(Context ctx, Activity act, WebView wv)
		{
			_context = ctx;
			_activity = act;
			_webview = wv;
		}
		public void run(Runnable r){
			_activity.runOnUiThread(r);
		}
		@JavascriptInterface
		public boolean putString(String key,String val){
			DedroidConfig.putString(_context,"configs",key,val);
			return true;
		}
		@JavascriptInterface
		public String getString(String key){
			return DedroidConfig.getString(_context,"configs",key);
		}
		@JavascriptInterface
		public void create(int code, boolean isMy){
			creator_code=code;
			creator_isMy=isMy;
			run(new Runnable(){
					@Override
					public void run(){
						AlertDialog.Builder dialog = DedroidDialog.emptyDialog(_context);
						dialog_creator_view=_activity.getLayoutInflater().inflate(R.layout.dialog_create,null);
						dialog.setView(dialog_creator_view);
						dialog_creator=dialog.show();
					}
			});
		}
		@JavascriptInterface
		public String listProject() throws JSONException{
			DedroidFile.mkdir(DedroidFile.EXTERN_STO_PATH+"/1503Dev/WebProjects");
			final String[] projects=DedroidFile.listName(DedroidFile.EXTERN_STO_PATH+"/1503Dev/WebProjects/").toArray(new String[0]);
			if(projects.length==0){
				return "[]";
			}
			return new JSONArray(projects).toString();
		}
		@JavascriptInterface
		public String getProjectDir(String n){
			return projectPath+n+"/";
		}
		@JavascriptInterface
		public String getProjectInfo(String n) throws JSONException, IOException{
			JSONObject manifestRoot=new JSONObject(DedroidFile.read(projectPath+n+"/WebdroidManifest.json"));
			JSONObject manifest=manifestRoot.getJSONObject("manifest");
			JSONObject info=new JSONObject();
			info.put("app_name",manifest.getString("application_name"));
			info.put("version_name",manifest.getString("version_name"));
			info.put("version_code",manifest.getInt("version_code"));
			return info.toString();
		}
		@JavascriptInterface
		public void save(String n,String an,String vn,int vc) throws JSONException, IOException{
			JSONObject manifestRoot=new JSONObject(DedroidFile.read(projectPath+n+"/WebdroidManifest.json"));
			JSONObject manifest=manifestRoot.getJSONObject("manifest");
			manifest.put("application_name",an);
			manifest.put("version_name",vn);
			manifest.put("version_code",vc);
			manifestRoot.put("manifest",manifest);
			DedroidFile.write(projectPath+n+"/WebdroidManifest.json",manifestRoot.toString());
			DedroidToast.toast(_activity,"保存成功");
		}
		@JavascriptInterface
		public void code(String n,boolean isMD){
			Intent i=new Intent();
			i.setClass(_context,EditorActivity.class);
			i.putExtra("project",n);
			i.putExtra("isMD",isMD);
			_activity.startActivity(i);
		}
		@JavascriptInterface
		public void pack(final String n){
			MainActivity.pack(n);
		}
		@JavascriptInterface
		public void run(String n){
			Intent i=new Intent();
			i.setClass(_context,ProjectViewerActivity.class);
			i.putExtra("project",n);
			_activity.startActivity(i);
		}
		@JavascriptInterface
		public void runFile(String n,String f){
			Intent i=new Intent();
			i.setClass(_context,ProjectViewerActivity.class);
			i.putExtra("project",n);
			i.putExtra("file",f);
			_activity.startActivity(i);
		}
		@JavascriptInterface
		public void del(String n){
			Utils.deleteDir(new File(DedroidFile.EXTERN_STO_PATH+"/1503Dev/WebProjects/"+n));
			Toast.makeText(_activity,"删除成功",Toast.LENGTH_SHORT);
		}
		@JavascriptInterface
		public void setMyEnable(boolean s){
			SharedPreferences sp=_context.getSharedPreferences("configs",_context.MODE_PRIVATE);
			SharedPreferences.Editor spe=sp.edit();
			spe.putBoolean("enable_my",s);
			spe.apply();
		}
		@JavascriptInterface
		public void template(){
			run(new Runnable(){
					@Override
					public void run(){
						
					}
				});
		}
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
		String temppath="project_core/html/index.html";
		String after="";
		if(creator_isMy&&creator_code>1){
			after="_my";
		}
		if(creator_code!=1){
			temppath="project_templates/"+templates[creator_code]+after+".html";
		}
		if(creator_code>1){
			DedroidFile.mkdir(projectPath+"/html/lib");
			Utils.copyAssetToExternalStorage(mc,"local_html/jq.js",projectPath+"/html/lib/jq.js");
		}
		if(creator_code>1&&creator_isMy){
			Utils.copyAssetToExternalStorage(mc,"lib-mdui.zip",projectPath+"/html/lib.zip");
			Utils.unzip(projectPath+"/html/lib.zip",projectPath+"/html/lib");
			DedroidFile.del(projectPath+"/html/lib.zip");
		}
		if(creator_code>1&&!creator_isMy){
			Utils.copyAssetToExternalStorage(mc,"lib-mdui-1.zip",projectPath+"/html/lib2.zip");
			Utils.unzip(projectPath+"/html/lib2.zip",projectPath+"/html/lib");
			DedroidFile.del(projectPath+"/html/lib2.zip");
		}
		String temp=Utils.readAssetsFile(mc.getAssets(),temppath);
		DedroidFile.write(projectPath+"/html/index.html",temp);
		Utils.copyAssetToExternalStorage(mc, "project_core/icon.png", projectPath+"/icon.png");
		DedroidToast.toast(mc,"创建成功");
		DedroidDialog.alert(mc,"创建成功","项目已在\n"+projectPath+"\n创建",true);
		dialog_creator.cancel();
	}
	public static void pack(final String projectName){
		mc.runOnUiThread(new Runnable(){

                @Override
                public void run() {
					
                    String rootPath=DedroidFile.EXTERN_STO_PATH+"/1503Dev/.WebdroidAppCoreTemp";
                    String projectPath=DedroidFile.EXTERN_STO_PATH+"/1503Dev/WebProjects/"+projectName;
                    //DedroidFile.del(rootPath);
                    DedroidFile.mkdir(rootPath);
                    try
                    {
                        Utils.copyAssetToExternalStorage(mc, "app_core.zip", rootPath+"/app_core.zip");
						DedroidFile.mkdir(rootPath+"/res/drawable");
						DedroidFile.mkdir(rootPath+"/res/xml");
						DedroidFile.mkdir(rootPath+"/META-INF/proguard");
                        Utils.unzip(rootPath+"/app_core.zip",rootPath);
                        DedroidFile.del(rootPath+"/app_core.zip");
                    }
                    catch (IOException e)
                    {
                        DedroidToast.toast(mc,"内核加载失败\n"+e);
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
							DedroidFile.mkdir(projectPath+"/build");
                            Utils.zipFolderWithoutRoot(rootPath,projectPath+"/build/app.apk");
                            DedroidFile.del(rootPath);
                            DedroidToast.toast(mc,"项目打包成功，请自行签名");
                            DedroidDialog.alert(mc,"成功","文件已保存在\n"+projectPath+"/build/app.apk\n请自行签名",true);
                            
                        }
                        catch (IOException e)
                        {
                            DedroidToast.toast(mc,"项目打包出错，但打包未中断");
                        }
                        catch (XmlPullParserException e)
                        {
                            DedroidToast.toast(mc,"项目打包失败，AndroidManifest.xml编译错误");
                        }
                    }
                    catch (JSONException e)
                    {
                        DedroidToast.toast(mc,"项目读取失败，请检查WebdroidManifest.json");
                    }
                    catch (IOException e)
                    {
                        DedroidToast.toast(mc,"项目读取失败，请检查是否拥有权限");
                    }
                }
            });
		
	}
}
