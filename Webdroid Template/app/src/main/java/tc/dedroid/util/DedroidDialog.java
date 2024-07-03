package tc.dedroid.util;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import java.util.function.Function;
import android.widget.ExpandableListView.OnChildClickListener;
import android.view.View.OnClickListener;
import android.location.OnNmeaMessageListener;
import android.widget.LinearLayout;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.Magnifier.Builder;
import java.time.LocalDateTime;
import org.json.JSONObject;
import org.json.JSONException;
import android.app.Activity;
import android.content.Intent;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.os.Build;
import java.util.UUID;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.graphics.Bitmap;
import java.io.ByteArrayOutputStream;
import android.graphics.drawable.BitmapDrawable;
import android.util.Base64;
import android.view.inputmethod.InputMethodManager;
import android.webkit.JavascriptInterface;
import android.net.Uri;
import android.text.TextUtils;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class DedroidDialog {

    final static int UtilVersion=Dedroid.getVersion();
    static Context _context=null;
    static Activity _activity=null;

    static private LinearLayout getPromptView(Context context, String dt) {
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(   
            LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT); 
        LinearLayout PromptView = new LinearLayout(context); 
        PromptView.setLayoutParams(lp);//设置布局参数 
        PromptView.setOrientation(LinearLayout.HORIZONTAL);// 设置子View的Linearlayout// 为垂直方向布局 
        //定义子View中两个元素的布局 
        ViewGroup.LayoutParams vlp = new ViewGroup.LayoutParams( 
            ViewGroup.LayoutParams.MATCH_PARENT, 
            ViewGroup.LayoutParams.WRAP_CONTENT);
        final EditText et=new EditText(context);
        et.setId(0x50726F6D7074566965777948F7);
        et.setLayoutParams(new ViewGroup.LayoutParams( 
                               ViewGroup.LayoutParams.MATCH_PARENT, 
                               ViewGroup.LayoutParams.WRAP_CONTENT));
        et.setText(dt);
        PromptView.addView(et);
        PromptView.setLayoutParams(vlp);
        PromptView.setPadding(40, 0, 40, 0);
        return PromptView;
    }
    public interface PromptCallback {
        void onClick(EditText et);
    }
    public interface SelectCallback {
        void onSelect(DialogInterface dialog, int Which);
    }
    static public void alert(Context context, String Title, String Content, boolean Cancelable) {
        AlertDialog dialog = new AlertDialog.Builder(context)
            .setTitle(Title)
            .setMessage(Content)
            .setPositiveButton("确定", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dia, int which) {

                }
            })

            .setCancelable(Cancelable)
            .create();
        dialog.show();
    }
    static public void alert(Context context, String Content, boolean Cancelable) {
        AlertDialog dialog = new AlertDialog.Builder(context)
            //.setTitle(Title)
            .setMessage(Content)
            .setPositiveButton("确定", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dia, int which) {

                }
            })

            .setCancelable(Cancelable)
            .create();
        dialog.show();
    }
    static public void HTMLAlert(Context context, String Title, String HTMLContent, boolean Cancelable) {
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(   
            LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT); 
        LinearLayout PromptView = new LinearLayout(context); 
        PromptView.setLayoutParams(lp);//设置布局参数 
        PromptView.setOrientation(LinearLayout.HORIZONTAL);// 设置子View的Linearlayout// 为垂直方向布局 
        //定义子View中两个元素的布局 
        ViewGroup.LayoutParams vlp = new ViewGroup.LayoutParams( 
            ViewGroup.LayoutParams.MATCH_PARENT, 
            ViewGroup.LayoutParams.WRAP_CONTENT);
        final TextView tb=new TextView(context);
        tb.setLayoutParams(new ViewGroup.LayoutParams( 
                               ViewGroup.LayoutParams.MATCH_PARENT, 
                               ViewGroup.LayoutParams.WRAP_CONTENT));
        tb.setText(Html.fromHtml(HTMLContent, Html.FROM_HTML_MODE_LEGACY));

        tb.setMovementMethod(LinkMovementMethod.getInstance());
        PromptView.addView(tb);
        PromptView.setLayoutParams(vlp);
        PromptView.setPadding(56, 8, 56, 0);
        AlertDialog dialog = new AlertDialog.Builder(context)
            .setView(PromptView)
            .setTitle(Title)
            //.setMessage(Content)
            .setPositiveButton("确定", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dia, int which) {

                }
            })

            .setCancelable(Cancelable)
            .create();
        dialog.show();
    }
    static public void HTMLAlert(Context context, String HTMLContent, boolean Cancelable) {
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(   
            LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT); 
        LinearLayout PromptView = new LinearLayout(context); 
        PromptView.setLayoutParams(lp);//设置布局参数 
        PromptView.setOrientation(LinearLayout.HORIZONTAL);// 设置子View的Linearlayout// 为垂直方向布局 
        //定义子View中两个元素的布局 
        ViewGroup.LayoutParams vlp = new ViewGroup.LayoutParams( 
            ViewGroup.LayoutParams.MATCH_PARENT, 
            ViewGroup.LayoutParams.WRAP_CONTENT);
        final TextView tb=new TextView(context);
        tb.setLayoutParams(new ViewGroup.LayoutParams( 
                               ViewGroup.LayoutParams.MATCH_PARENT, 
                               ViewGroup.LayoutParams.WRAP_CONTENT));
        tb.setText(Html.fromHtml(HTMLContent, Html.FROM_HTML_MODE_LEGACY));

        tb.setMovementMethod(LinkMovementMethod.getInstance());
        PromptView.addView(tb);
        PromptView.setLayoutParams(vlp);
        PromptView.setPadding(56, 48, 56, 0);
        AlertDialog dialog = new AlertDialog.Builder(context)
            .setView(PromptView)
            .setPositiveButton("确定", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dia, int which) {

                }
            })

            .setCancelable(Cancelable)
            .create();
        dialog.show();
    }
    static public void web(final Context context, final Activity activity , String title, String PositiveButton, String PositiveButtonCmd, String NegativeButton, String NegativeButtonCmd, String NeutralButton, String NeutralButtonCmd, String url, boolean Cancelable) {
        _context = context;
        _activity = activity;
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(   
            LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT); 
        LinearLayout PromptView = new LinearLayout(context); 
        PromptView.setLayoutParams(lp);//设置布局参数 
        PromptView.setOrientation(LinearLayout.HORIZONTAL);// 设置子View的Linearlayout// 为垂直方向布局 
        //定义子View中两个元素的布局 
        ViewGroup.LayoutParams vlp = new ViewGroup.LayoutParams( 
            ViewGroup.LayoutParams.MATCH_PARENT, 
            ViewGroup.LayoutParams.WRAP_CONTENT);
        final WebView wv=new WebView(context);
        wv.setLayoutParams(new ViewGroup.LayoutParams( 
                               ViewGroup.LayoutParams.MATCH_PARENT, 
                               ViewGroup.LayoutParams.WRAP_CONTENT));
        wv.setWebViewClient(new WebViewClient() {
                //设置在webView点击打开的新网页在当前界面显示,而不跳转到新的浏览器中
                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    view.loadUrl(url);
                    return true;
                }
            });
        wv.getSettings().setJavaScriptEnabled(true);  //设置WebView属性,运行执行js脚本
        wv.getSettings().setDomStorageEnabled(true);
        DedroidWeb.JsBridge jsb=new DedroidWeb.JsBridge(context);
        jsb.setWebView(wv);

        PromptView.addView(wv);
        PromptView.setLayoutParams(vlp);
        //PromptView.setPadding(56, 48, 56, 0);
        AlertDialog dialog = new AlertDialog.Builder(context)
            .setTitle(title)
            .setView(PromptView)
            .setPositiveButton(PositiveButton, getNetworkDialogOnClick(context, activity, PositiveButtonCmd))
            .setNegativeButton(NegativeButton, getNetworkDialogOnClick(context, activity, NegativeButtonCmd))
            .setNeutralButton(NeutralButton, getNetworkDialogOnClick(context, activity, NeutralButtonCmd))
            .setCancelable(Cancelable)
            .create();
        jsb.setDialog(dialog);
        jsb.setActivity(activity);
        wv.addJavascriptInterface(jsb, "nativeApi");
        wv.addJavascriptInterface(jsb, "Dedroid");
        wv.loadUrl(Dedroid.strApi(context, url));
        dialog.show();
    }
    static public void confirm(Context context, String Title, String Content, boolean Cancelable, DialogInterface.OnClickListener OnClick) {
        confirm(context, Title, Content, Cancelable, OnClick, null);
    }
    static public void confirm(Context context, String Title, String Content, boolean Cancelable) {
        confirm(context, Title, Content, Cancelable, null);
    }
    static public void confirm(Context context, String Title, String Content, boolean Cancelable, DialogInterface.OnClickListener OnClick, DialogInterface.OnClickListener OnNegativeClick) {
        AlertDialog dialog = new AlertDialog.Builder(context)
            .setTitle(Title)
            .setMessage(Content)
            .setPositiveButton("确定", OnClick)
            .setNegativeButton("取消", OnNegativeClick)
            .setCancelable(Cancelable)
            .create();
        dialog.show();
    }
    static public void prompt(Context context, String Title, String Content, boolean Cancelable, final PromptCallback onPositiveClickCallback, final PromptCallback OnNegativeClickCallback, String DefaultText) {
        final LinearLayout view=getPromptView(context, DefaultText);
        AlertDialog dialog = new AlertDialog.Builder(context)
            .setView(view)
            .setTitle(Title)
            .setMessage(Content)
            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dia, int which) {
                    onPositiveClickCallback.onClick((EditText)view.findViewById(0x50726F6D7074566965777948F7));
                }
            })
            .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dia, int which) {
                    OnNegativeClickCallback.onClick((EditText)view.findViewById(0x50726F6D7074566965777948F7));
                }
            })
            .setCancelable(Cancelable)
            .create();
        dialog.show();
    }
    static public void prompt(Context context, String Title, String Content, boolean Cancelable, final PromptCallback onPositiveClickCallback, String DefaultText) {
        final LinearLayout view=getPromptView(context, DefaultText);
        AlertDialog dialog = new AlertDialog.Builder(context)
            .setView(view)
            .setTitle(Title)
            .setMessage(Content)
            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dia, int which) {
                    onPositiveClickCallback.onClick((EditText)view.findViewById(0x50726F6D7074566965777948F7));
                }
            })
            .setCancelable(Cancelable)
            .create();
        dialog.show();
    }
    static public void prompt(Context context, String Title, String Content, boolean Cancelable, final PromptCallback onPositiveClickCallback, final PromptCallback OnNegativeClickCallback) {
        prompt(context, Title, Content, Cancelable, onPositiveClickCallback, OnNegativeClickCallback, "");
    }
    static public void prompt(Context context, String Title, String Content, boolean Cancelable, final PromptCallback onPositiveClickCallback) {
        prompt(context, Title, Content, Cancelable, onPositiveClickCallback, new PromptCallback(){
                @Override
                public void onClick(EditText v) {

                }
            });
    }
    static public void select(Context context, String Title, boolean Cancelable, CharSequence[] Items, int SelectedItem, final SelectCallback OnSelectCallback) {
        AlertDialog dialog = new AlertDialog.Builder(context)
            .setTitle(Title)
            .setSingleChoiceItems(Items, SelectedItem, new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dia, int which) {
                    OnSelectCallback.onSelect(dia, which);
                }
            })
            .setCancelable(Cancelable)
            .create();
        dialog.show();
    }
    static public void select(Context context, boolean Cancelable, CharSequence[] Items, int SelectedItem, final SelectCallback OnSelectCallback) {
        AlertDialog dialog = new AlertDialog.Builder(context)
            .setSingleChoiceItems(Items, SelectedItem, new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dia, int which) {
                    OnSelectCallback.onSelect(dia, which);
                }
            })
            .setCancelable(Cancelable)
            .create();
        dialog.show();
    }
    static public void select(Context context, String Title, boolean Cancelable, CharSequence[] Items, final SelectCallback OnSelectCallback) {
        select(context, Title, Cancelable, Items, -1, OnSelectCallback);
    }
    static public void select(Context context, boolean Cancelable, CharSequence[] Items, final SelectCallback OnSelectCallback) {
        select(context, Cancelable, Items, -1, OnSelectCallback);
    }
    static public void list(Context context, String Title, boolean Cancelable, CharSequence[] Items, int SelectedItem, final SelectCallback OnSelectCallback) {
        AlertDialog dialog = new AlertDialog.Builder(context)
            .setTitle(Title)
            .setItems(Items, new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dia, int which) {
                    OnSelectCallback.onSelect(dia, which);
                }
            })
            .setCancelable(Cancelable)
            .create();
        dialog.show();
    }
    static public void list(Context context, boolean Cancelable, CharSequence[] Items, final SelectCallback OnSelectCallback) {
        AlertDialog dialog = new AlertDialog.Builder(context)

            .setItems(Items, new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dia, int which) {
                    OnSelectCallback.onSelect(dia, which);
                }
            })
            .setCancelable(Cancelable)
            .create();
        dialog.show();
    }
    static public DialogInterface customDialog(Context context, String Title, View view, boolean Cancelable) {
        AlertDialog dialog = new AlertDialog.Builder(context)
            .setTitle(Title)
            .setView(view)
            .setCancelable(Cancelable)
            .create();
        dialog.show();
        return dialog;
    }
    static public DialogInterface customDialog(Context context, View view, boolean Cancelable) {
        AlertDialog dialog = new AlertDialog.Builder(context)
            .setView(view)
            .setCancelable(Cancelable)
            .create();
        dialog.show();
        return dialog;
    }
    static public void networkDialog(final Context context, final Activity activity, String url) {
        _context = context;
        _activity = activity;
        DedroidNetwork.get(Dedroid.strApi(context, url), new DedroidNetwork.HttpCallback(){

                @Override
                public void onResponse(String responseString, int httpCode) {
                    final String data=responseString;
                    try {
                        JSONObject json=new JSONObject(data);
                        String type=json.getString("type");

                        final JSONObject PositiveButton=json.getJSONObject("PositiveButton");
                        final JSONObject NegativeButton=json.getJSONObject("NegativeButton");
                        final JSONObject NeutralButton=json.getJSONObject("NeutralButton");
                        final String pb_txt=PositiveButton.getString("text");
                        final String nb_txt=NegativeButton.getString("text");
                        final String nb_txt2=NeutralButton.getString("text");
                        final String pb_cmd=Dedroid.strApi(context, PositiveButton.getString("cmd"));
                        final String nb_cmd=Dedroid.strApi(context, NegativeButton.getString("cmd"));
                        final String nb_cmd2=Dedroid.strApi(context, NeutralButton.getString("cmd"));
                        final String title=Dedroid.strApi(context, json.getString("title"));
                        final String content=Dedroid.strApi(context, json.getString("content"));
                        final Boolean cancelable=json.getBoolean("cancelable");
                        final String HTML=json.getString("html");

                        switch (type.toLowerCase()) {
                            case "alert":


                                if (!title.equals("")) {
                                    activity.runOnUiThread(new Runnable(){
                                            @Override
                                            public void run() {
                                                DedroidDialog.alert(context, title, content, cancelable);
                                            }
                                        });
                                } else {
                                    activity.runOnUiThread(new Runnable(){

                                            @Override
                                            public void run() {
                                                DedroidDialog.alert(context, content, cancelable);
                                            }
                                        });
                                }
                                break;
                            case "toast":
                                activity.runOnUiThread(new Runnable(){
                                        @Override
                                        public void run() {
                                            DedroidToast.toast(activity, content);
                                        }
                                    });

                                break;
                            case "htmlalert":


                                if (!title.equals("")) {
                                    activity.runOnUiThread(new Runnable(){
                                            @Override
                                            public void run() {
                                                DedroidDialog.HTMLAlert(context, title, content, cancelable);
                                            }
                                        });
                                } else {
                                    activity.runOnUiThread(new Runnable(){

                                            @Override
                                            public void run() {
                                                DedroidDialog.HTMLAlert(context, content, cancelable);
                                            }
                                        });
                                }
                                break;
                            case "html":
                                activity.runOnUiThread(new Runnable(){
                                        @Override
                                        public void run() {
                                            DedroidDialog.web(context, activity , title, pb_txt, pb_cmd, nb_txt, nb_cmd, nb_txt2, nb_cmd2, "data:text/html," + HTML, cancelable);
                                        }
                                    });

                                break;
                            case "web":
                                final String url=json.getString("url");
                                activity.runOnUiThread(new Runnable(){
                                        @Override
                                        public void run() {
                                            DedroidDialog.web(context, activity, title, pb_txt, pb_cmd, nb_txt, nb_cmd, nb_txt2, nb_cmd2, url, cancelable);
                                        }
                                    });

                                break;
                            case "custom":


                                activity.runOnUiThread(new Runnable(){
                                        @Override
                                        public void run() {

                                            DedroidDialog.emptyDialog(context)
                                                .setTitle(title)
                                                .setMessage(content)
                                                .setCancelable(cancelable)
                                                .setNeutralButton(nb_txt2, getNetworkDialogOnClick(context, activity, nb_cmd2))
                                                .setPositiveButton(pb_txt, getNetworkDialogOnClick(context, activity, pb_cmd))
                                                .setNegativeButton(nb_txt, getNetworkDialogOnClick(context, activity, nb_cmd))
                                                .create()
                                                .show();

                                        }
                                    });


                                break;
                        }
                    } catch (JSONException e) {}
                }

                @Override
                public void onFailure(Exception e) {
                    /*activity.runOnUiThread(new Runnable(){

                     @Override
                     public void run() {
                     DedroidDialog.alert(context,"获取网络弹窗失败",true);
                     }


                     });*/
                }


            });
    }
    static private DialogInterface.OnClickListener getNetworkDialogOnClick(final Context context, final Activity activity, final String str) {
        if (str.equals("")) {
            return null;
        }
        if (str.substring(0, 5).equals("toast")) {
            return new DialogInterface.OnClickListener(){

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    DedroidToast.toast(context, str.substring(6));
                }

            };
        }
        if (str.substring(0, 13).equals("networkDialog")) {
            return new DialogInterface.OnClickListener(){

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    networkDialog(context, activity, str.substring(14));
                }

            };
        }
        if (str.substring(0, 7).equals("jumpUrl")) {
            return new DialogInterface.OnClickListener(){

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(str.substring(8)));

                    // 判断是否有可用的应用能够处理这个Intent
                    if (intent.resolveActivity(context.getPackageManager()) != null) {
                        context.startActivity(intent);
                    }
                }

            };
        }
        return null;
    }

    static public AlertDialog.Builder emptyDialog(Context context) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(context);
        return dialog;
    }



}
