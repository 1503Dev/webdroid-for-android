# Webdroid Studio For Android
简体中文

轻松把你的HTML打包成APK
## 如何使用
### 管理项目
**项目位置**

所有WEB项目都在`内部储存/1503Dev/WebProjects/`

**项目结构**

```
ProjectName/
  WebdroidManifest.json
  html/
    icon.png (应用图标)
    index.html
    script.js
    style.css
    ...
```
**WebdroidManifest.json结构**
```jsonc
{
    "manifest":{
        "application_name":"应用名",
        "launcher_name":"启动器名", // 可用<AppName>代替
        "version_name":"版本名",
        "version_code":"版本号",
        "package":"包名"
    },
    "settings":{
        "enabled_android_manifest_xml":false
    }
}
```
### 打包项目
- 点击打包
- 选择需要打包的项目
- 打包完成
- 找到打包后的apk
- 使用 `MT管理器` `NP管理器` 或 `ZipSigner`签名
- 安装，即可使用

## JS接口
### Dedroid
应用内集成了 [DedroidUtil](https://github.com/TheChuan1503/DedroidUtil/) (版本:9) 的 `Dedroid` 和 `Plugin` 接口
### Webdroid
所有api结构都是`webdroid_子类.功能(参数)`

`[]`内的为可选参数
#### Webdroid
- int **webdroid.getVersion()** *获取内核版本*

#### Toast
- **webdroid_toast.make(String text)** *弹出2秒提示*
- **webdroid_toast.makeLong(String text)** *弹出3秒提示*

#### Dialog
- **webdroid_dialog.alert([String 标题,]String 内容[,能否取消])** *弹出普通弹窗*